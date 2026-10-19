# Ciclo 63 — integração da main, Zaraki e conflitos

19/10/2026. Pedido: explicar Photon, estudar a implementação/documentação do Zaraki já na main e resolver a divergência que impede integrar `Feature-Poderes-bankais`. Git limpo ao iniciar, HEAD `48734cc`. `git fetch origin` atualizou `origin/main` de `a33ffd4` para **`611f8a6`**. Trabalho permanece na feature; nenhum commit direto na main.

## O que a main já contém

- `33743a1`: merge `bankai-zaraki`, Nozarashi/Berserk e cinemática.
- `a33ffd4`: merge de dimensões, modo História, tipo de mundo, manifesto/mapas e viagens.
- `611f8a6`: merge de personalização, editor de aparência, catálogo de skins e ampliação dos NPCs.

A integração considera os três incrementos. Não fazer checkout antigo do Zaraki por cima da main atual nem resolver conflitos apagando o conteúdo de dimensões/aparência. A consulta remota não encontrou PR aberto para esta feature no início; preparar uma proposta de integração após validar a branch.

## Engenharia reversa da cinemática Zaraki

Leitura do código e assets efetivos, além dos relatórios da equipe [36](36-kit-nozarashi-berserk-mode-2026-10-15.md), [37](37-berserk-intensidade-espiral-martelo-2026-10-15.md), [38](38-visao-limpa-destruicao-terreno-2026-10-15.md) e [39](39-voz-bankai-zaraki-2026-10-18.md). O primeiro relatório ainda descreve a timeline inicial de 100 ticks; **o código vigente e os refinamentos usam 140 ticks / sete segundos**.

O fluxo encontrado:

```text
Transformação autorizada / forma Nozarashi Bankai
    → FormChangeEvent
    → NozarashiReleaseEffects: timeline no servidor, 140 ticks
    → ReleaseCinematicS2C: entidade, tipo, duração
    → CinematicDirector: corpo + câmera + FOV + HUD
    → BerserkSkins/BerserkSkinLayer: aparência na revelação
```

O servidor agenda sons, impactos, empurrão, destruição natural e proteção específica desta transformação. A forma é ativada antes de rodar a apresentação; esse contrato difere da liberação comum dos nossos outros kits, que prepara e confirma em um marco da timeline. Não trocar os contratos automaticamente por causa do merge.

Marcos efetivos: socos nos ticks **24/32/40**, erupção **45**, revelação visual da pele **47**, segundo rugido **68**, pegar a arma **112**, golpe final **124**, término **140**. A fala começa no zero e ocupa até 84 ticks; sons concorrentes têm volume reduzido nesse trecho.

`CinematicDirector` contém cinco tomadas:

1. Até 20: close no rosto, aproximação e tremor.
2. 20–45: câmera de cima durante queda/socos.
3. 45–80: plano aberto e baixo para mostrar a erupção/pilar.
4. 80–112: plano baixo orbitando enquanto o corpo se ergue.
5. 112–140: lateral da arma e transição para trás do ombro no golpe final.

Cada tomada calcula **posição da câmera, ponto de interesse, roll e FOV** em coordenadas relativas à âncora/direção do jogador. A câmera é aplicada no evento de viewport Forge, por quadro com partial tick. A troca de perspectiva permite enxergar o próprio corpo; barras/HUD, tremor, flash e título pertencem ao controlador. Efeitos de impacto podem emitir tremor para observadores próximos sem tomar sua câmera inteira.

O corpo usa **PlayerAnimator**, com keyframes de posição/rotação por bone em `assets/bleachmod/player_animation/berserk_release.json`, reproduzidos por `KeyframeAnimationPlayer`. Fonte reproduzível: `tools/animations/berserk_release.py`; poses de ombro/martelo ficam no gerador `nozarashi_stance.py`. O script usa tempos em segundos, rotações em graus e translações em pixels, alinhados aos marcos de servidor. Não basta aumentar partículas para reproduzir o resultado: a arma precisa chegar ao golpe no mesmo instante do som, câmera e impacto.

Os modelos da arma são JSONs gerados por `tools/models/nozarashi_models.py`, com volumes, material vanilla, transforms e emissividade no Berserk. A pele é uma camada dinâmica própria, agora coexistindo com a personalização da main. Não há Photon nessa implementação original: aura/geometria/partículas e câmera foram programadas pela equipe.

Para evoluir Higashi e outras técnicas, reaproveitar a organização por tomadas e marcos, autoria de keyframes e prioridade/limpeza, mantendo duração e regras próprias de cada ação. A primeira adaptação Higashi do ciclo 62 já usa viewport e três tomadas; ainda requer retorno visual. Este ciclo resolve coexistência e conflitos, sem afirmar que refez/homologou toda a arte.

## Photon em termos práticos

Photon é o sistema/editor de **efeitos visuais**: emissores, materiais, partículas, trilhas e feixes. LDLib fornece a infraestrutura usada pelo editor e pela integração. Na nossa bancada, a definição FX cria um runtime temporário; o mod escolhe quando iniciar/encerrar. Isso permite editar distribuição, tempo, cor, tamanho e movimento dos detalhes sem construir cada efeito inteiro por comandos de partículas.

PlayerAnimator continua responsável por braços, tronco, pernas e gesto da espada; Forge/controlador dirigem a câmera; o servidor Bleach decide alvo, dano e recursos. Photon pode enriquecer o rastro de um corte ou as cinzas depois de um impacto, mas não define por conta própria como enquadrar dois personagens nem quando aplicar execução.

O [piloto 62](62-higashi-camera-dirigida-piloto-photon-2026-10-18.md) continua opcional: Photon **1.1.17**, LDLib **1.0.42** Forge, pacote experimental separado e comandos `/bleachfx photon status/test/stop/export`. Não substituir pela documentação Photon 2/LDLib2/NeoForge. O pacote normal tem cinco JARs; o piloto sete. Os erros recuperados de scanner/editor em dedicado continuam uma pendência conhecida; testar UI/export, FPS e duas conexões antes da adoção geral.

## Resolução dos conflitos e decisões de integração

`git merge --no-commit --no-ff origin/main` encontrou **13 arquivos** conflitantes. A resolução preserva:

- **Gradle:** MixinGradle/refmap e processador da aparência, Better Combat, GeckoLib e perfil Photon. Apenas um artefato PlayerAnimator, o Forge já fixado. WorldEdit de validação passa a opt-in `-PvalidationWorldEdit=true`, mantendo a capacidade de desenvolvimento da main sem exigir seu JAR local na build comum. Removidas declarações duplicadas de configuração/mixins no run/manifesto.
- **Pacotes:** Higashi, liberação Zaraki e editor de aparência têm registros próprios e direções preservadas. Protocolo combinado **2.25**, substituindo 2.24 da feature e 2.19 da main; versões antigas não devem conectar à nova build.
- **Sincronização:** `appearance`, `releaseView`, `signatureView`, `elementalView` e `guardView` permanecem no snapshot, incluindo envio a observador. Não escolher entre skin e animação como se fossem o mesmo campo.
- **Poderes:** matriz revisada Ryūjin/Hyōrinmaru continua; acrescentadas as 12 definições Nozarashi. Senbonzakura/Benihime, execução Higashi e Jardim são mantidos. `FormNames` preserva o nome Berserk Mode no feedback/HUD.
- **Mixins:** três adaptadores Better Combat e mixin cliente de aparência coexistem, com refmap gerado para os métodos Minecraft. Inicialização inclui todos os controladores.
- **Idiomas/testes:** merge de chaves e casos de ambas as branches; nenhum conjunto de regressões substituído pelo outro. Entradas novas da main mantidas, traduções revisadas da feature preservadas.
- **Persistência:** schema **9** e revisão de técnicas **2**. Aparência é um bloco separado; Nozarashi continua com chaves numéricas de técnica, enquanto Ryūjin/Hyōrinmaru preservam migração semântica. Um teste combinado verifica progresso Nozarashi + aparência e migração elemental no mesmo save/clone.

Além dos marcadores Git, foram tratados encontros reais de comportamento:

1. `ReleaseAnimationService.busy` inclui a cinemática Nozarashi para que guarda, esquiva, técnicas, ataques físicos e nova transformação respeitem o estado comum. Mantida a proteção original do Zaraki, incluindo a exceção a fontes que ignoram invulnerabilidade.
2. Zaraki recebe a câmera da Higashi por handoff explícito; a Higashi não assume a câmera enquanto Zaraki a possui. A perspectiva é restaurada antes de passar ao próximo controlador.
3. A camada da cinemática Zaraki passa à prioridade 5000, acima das poses/gestos que agora existem na feature; ombro/martelo permanecem abaixo. Limpeza cliente passa a considerar mundo, desconexão, morte e entidade ausente, restaurando a câmera em vez de somente apagar a referência.
4. Nozarashi permanece fora do fallback/hitbox Better Combat, mantendo o executor e golpes de martelo da equipe. Migrar sua hitbox futuramente exige contrato específico; não conceder alcance/combos silenciosamente pelo reconhecimento automático de espada.

Os documentos recebidos de arquitetura/jogador/planejamento foram preservados como conteúdo da main. Nova documentação desta tarefa fica somente em `Docs/poderes`. Mapas binários e catálogo/NPCs também são preservados; os pacotes de JARs para teste não representam uma nova distribuição completa dos mapas externos do modo História.

## Validação e entrega

`check build prepareCombatTestPack preparePhotonTestPack runGameTestServer` concluídos: **123 regressões e 139 GameTests aprovados**, log `build/cycle63-validation-final.log`. A primeira rodada tinha 122 regressões; a final inclui persistência cruzada e coexistência Nozarashi/Bleach. Uma falha inicial do teste novo foi corrigida na preparação do personagem (maestria Shikai obrigatória para liberar Bankai), sem relaxar as regras de produção.

Os mundos de GameTest são isolados por perfil em `run-gametest/base` e `run-gametest/photon`, evitando mapeamentos de blocos LDLib ausentes no perfil base. JAR final conferido com cinemática/skin, assets, mixins e refmap de aparência. Pacotes de teste recriados: `build/combat-test-pack/bleach-bettercombat-piloto.zip` (cinco JARs) e `build/photon-test-pack/bleach-photon-piloto.zip` (sete JARs e configuração do piloto).

O perfil opcional `-PphotonPilot=true runGameTestServer` também passou os **139 GameTests**, log `build/cycle63-photon-validation.log`. O scanner LDLib registrou nove tentativas recuperadas de carregar FXEditor no dedicado; manter a pendência de compatibilidade e o caráter experimental, mesmo com a suíte concluída.

O GameTest novo confere exclusão de fallback Better Combat, ativação da timeline Nozarashi, bloqueio de guarda/esquiva/ataques/técnicas sem custo extra, proteção de transformação e exceção de fonte de execução, e remoção do estado ao descer de forma. Roteiros anteriores de Higashi, gelo, campos, cura/cargas, aparência, NPCs e terreno continuam.

Ainda dependem de playtest gráfico: prioridade/retorno de poses, troca de câmera Zaraki↔Higashi, nova skin classic/slim com a arma, observador, editor/Photon, mundo História e viagens. Compilação e GameTests não comprovam FPS ou renderização. O feedback de que a animação Higashi ainda precisa melhorar continua aberto.

Entrega Git: merge resolvido e validado **na feature**, preparado para publicação dessa branch e proposta de revisão contra main. Não há arquivos com conflitos remanescentes; a revisão inclui `611f8a6` como ancestral. Não criar commit direto na main nem publicar mapas/artefatos gerados fora do conteúdo já versionado pela equipe. Aizen adiado, Gin removido e prioridades restantes preservadas.

## Estado remoto confirmado

Merge publicado na `Feature-Poderes-bankais` em **`7a87f26`**, com pais `48734cc` e `611f8a6`; main permanece intacta. A comparação no GitHub confirmou **Able to merge / These branches can be automatically merged**. [Comparação para revisão e abertura do PR](https://github.com/Costa-Novaes-Tech/Bleach-Modpack/compare/main...Feature-Poderes-bankais).

A tentativa de criar o PR pelo conector recebeu HTTP **403 — Resource not accessible by integration**. O navegador disponível está sem login; nenhum PR foi criado. A resolução dos conflitos e o push da feature foram concluídos; abertura/revisão do PR é a etapa restante para integrar à main.
