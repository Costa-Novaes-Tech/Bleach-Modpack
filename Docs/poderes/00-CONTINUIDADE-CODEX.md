# Continuidade do projeto — leia primeiro

Atualizado em 18/10/2026. Resumo de passagem para nova conversa; consulte o código/Git antes de alterar. Este arquivo evita precisar importar o histórico completo do chat. Não substitui instruções atuais do usuário.

**Entrega vigente — [ciclo 62: câmera Higashi e piloto Photon](62-higashi-camera-dirigida-piloto-photon-2026-10-18.md):** vídeo 60 revelou execução sem enquadramento legível. ArmorStand substituído por três tomadas no viewport Forge, prévia antes do impacto 12, pose própria até 44 e recuperação/cancelamento. Incineração somente após morte confirmada; prévia não reserva execução. Photon 1.1.17/LDLib 1.0.42 Forge opcionais: pacote experimental sete JARs, emissores autorais, comandos locais `/bleachfx photon test/stop/export/status` e export `.fx/.fxproj`. Pacote normal segue cinco JARs. Rede **2.24**, schema **9**, **113 regressões e 137 GameTests**; GameTests passaram também no perfil Photon, mas scanner LDLib registrou tentativas recuperadas de carregar FXEditor no dedicado. Manter bancada experimental/singleplayer até validar cliente/servidor real. Nova câmera e API/editor Photon ainda sem playtest gráfico. Nenhum merge Zaraki nesta etapa.

**Planejamento vigente — [revisão do roadmap, ciclo 61](61-revisao-roadmap-pendencias-integracao-zaraki-2026-10-18.md):** playtest 60 em andamento. A ref local `origin/bankai-zaraki` já contém o kit/cinemática em `c17d274`; não continuar dizendo que falta o commit. Integração ainda não feita: conciliar câmeras/animações, rede (Zaraki 2.18 × nossa 2.23), PlayerAnimator único, Better Combat e testes. WorldEdit da branch da equipe é ferramenta local, não requisito do pack. Depois: assets/áudio/medição e piloto Photon; Ishida próximo kit, Mentor/Proficiências aguardam equipe. Revisões elementais já implementadas; preservá-las e receber feedback. Esta revisão só mudou documentos, sem fetch/merge/runtime.

**Entrega anterior — ciclo 60:** [Higashi: execução e Jardim persistente](60-higashi-execucao-jardim-persistente-2026-10-18.md). Higashi Bankai executa um alvo elegível mais alinhado à mira com vida positiva até 7% antes do impacto, ignorando defesas/Totem; política de aliados/PvP/invulneráveis e eventos Forge preservados. Câmera local opcional de 32 ticks após morte confirmada, chamas/cinzas; morte/loot normais. Jardim mantém dois terços das pétalas do solo no interior durante a flor, reforça cobertura/subida e conserva raio 35/janela 2–6 s. Rede **2.23**, schema **9**, **110 regressões/135 GameTests aprovados**, pacote cinco JARs e mesmas dependências. Câmera, densidade/FPS e dois clientes reais aguardam playtest. Demais kits e roadmap preservados.

**Entrega anterior — ciclo 59:** [liberações e gestos Senbonzakura/Benihime/Asauchi](59-liberacoes-gestos-senbon-benihime-asauchi-2026-10-17.md). Autorizada implementação das animações restantes: Shikai/Bankai dessas três identidades na timeline servidor; duas fileiras de lâminas na ativação Senbon, surgimento gradual da manifestação Benihime, liberação espiritual Asauchi, 18 gestos de técnicas/continuações e alternância cosmética dos swings vanilla Senbon/Beni. Capacidades e visuais homologados preservados como base; Ryūjin/Hyōrinmaru mantidas. Rede **2.22**, schema **9**, 107 regressões/132 GameTests. Pacote cinco JARs, mesmas dependências. Conferência visual e dois clientes reais pendentes. Better Combat autoritativo continua somente Asauchi/Ryūjin/Hyōrinmaru; não cortar o alcance passivo Senbon ao migrar hitboxes.

**Entrega anterior — ciclo 58:** [Ryūjin: coreografias e chamas implementadas](58-ryujin-coreografias-chamas-implementadas-2026-10-17.md). Estudos 57 aprovados e aplicados: dez movimentos próprios via PlayerAnimator, chamas nativas substituem quads Ryūjin, Higashi com preparo no quadril/contato quente, Minami emerge visualmente. Pose permanente removida; selo Bankai aponta para fora, mantém somente nos ticks 47–53 e recupera até 60. Investida customizada sem auto-spin vanilla. Hyōrinmaru preservada. Rede **2.21**, schema **9**, 104 regressões/128 GameTests aprovados; pacote cinco JARs, mesmas dependências. Playtest visual e dois clientes reais pendentes. Não afirmar correspondência exata às imagens sem conferir o runtime.

**Estudos aprovados — ciclo 57:** [protótipos Ryūjin antes de codificar](57-prototipos-correcao-visual-ryujin-2026-10-17.md). Onze GIFs e dois painéis conceituais; agora referência da implementação 58, sem alterar Hyōrinmaru.

**Entrega anterior — ciclo 56:** [repertório elemental, pose final e combos](56-repertorio-elemental-cinematica-combos-2026-10-17.md). Concluída a migração dos kits Ryūjin/Hyōrinmaru: Rajada/Leque distintos, Jōkaku, Higashi ofensiva, Minami, Corrente Crescente, Ryūsenka, Sennen e continuação Hyakkasō (C entre 3–6 s). Pose final Ryūjin após selar chamas, FOV opcional, dragão Shikai alcance 15 e mordida ampliada; três dragões Bankai em escala 5,25 com mandíbula/dentes, ~33 blocos de extensão visual. Combos Better Combat próprios nas duas armas, além da Asauchi. Rede **2.20**, schema **9**, técnicas semânticas revisão **2**, 101 regressões/127 GameTests. Pacote cinco JARs; quatro dependências mantidas do ciclo 55. Playtest visual/multiplayer real pendente; não declarar pose exatamente homologada. Não há Photon/LDLib instalado; roadmap 51 e integração futura Zaraki preservados.

**Entrega anterior — ciclo 55:** [liberações animadas e dragões triplos](55-primeira-entrega-liberacoes-dragoes-triplos-2026-10-17.md). Primeira fatia, já testada pelo usuário; substituída/refinada pela matriz do ciclo 56. GeckoLib Forge 4.8.4 fixado pelo ID Modrinth aC5KMoNg (mclib 20 embutido).

**Registro da proposta — ciclo 54:** usuário pediu novo repertório Ryūjin/Hyōrinmaru mais fiel à obra, preservando ataques selecionados e passivas, e liberações animadas inspiradas na qualidade do vídeo Zaraki. [Pesquisa, matriz proposta por forma e plano de animações](54-proposta-refatoracao-ryujin-hyorinmaru-liberacoes-2026-10-17.md). Preservar fogo: Corte de Vapor, Rajada, Investida, Tornado e Leque; gelo: Choque, Zonas normal/ampliada e Dragão com novo modelo/ataque. Barreira de Geada substituível. Reorganização por forma, novos poderes e timings estão propostos, não implementados. Próxima execução deve fechar contratos e integrar ferramentas/pilotos conforme roadmap 51. Esta era a base anterior à primeira fatia do ciclo 55 acima.

**Entrega anterior: ciclo 53 — animação Asauchi aprovada pelo usuário; novo modelo 3D e pose de bloqueio com X entregues para teste.** [Contrato visual e continuidade](53-asauchi-3d-pose-bloqueio-2026-10-17.md). Pose nas cinco zanpakutō, primeira/terceira pessoa, via snapshot de guarda e PlayerAnimator. [Dependências e contrato Better Combat](52-better-combat-piloto-asauchi-2026-10-16.md). Better Combat 1.9.0 Forge + PlayerAnimator + Cloth Config necessários; pacote completo em `build/combat-test-pack/mods`. Rede **2.18**, schema **8**, 99 regressões/118 GameTests aprovados. Conferência visual desta entrega e dois clientes reais pendentes. Zaraki ainda sem commit; roadmap do ciclo 51 preservado.

## 1. Repositório e regras mantidas pelo usuário

- Projeto local: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack`.
- GitHub atual: https://github.com/Costa-Novaes-Tech/Bleach-Modpack. Origin local ainda usa GabrielCNovaesDev/Bleach-Modpack.git, que redireciona; não foi alterado.
- Branch de trabalho: `Feature-Poderes-bankais`. Nunca comitar na main. Commits locais são autorizados após validação; isso não autoriza push, merge, exclusões ou outras publicações não solicitadas.
- A branch antiga `Feature-Bankais-Poderes` foi retirada pelo usuário do fluxo por duplicação/atraso. A listagem local ainda contém refs com esse nome; não reutilizar nem confundir com `Feature-Poderes-bankais`, e não excluir refs nesta revisão.
- Documentação desta frente: modificar somente `Docs/poderes`. Documentos de outras pastas podem ser consultados, mas não editados sem nova orientação.
- Preservar texturas/modelos homologados salvo pedido explícito de revisão. Atualização do formato Asauchi autorizada no ciclo 53. Aizen e Bankai original para ele estão adiados.
- Trabalhar em português; informar alterações, validação e limites. Não pedir novamente autorização para tarefas já solicitadas; não presumir que proposta futura equivale a pedido de implementação.
- Conferir status, branch e instruções locais; preservar alterações de outras pessoas. Não havia AGENTS.md localizado na revisão anterior; conferir novamente se o repositório mudou.

## 2. Estado entregue e homologado

Minecraft 1.20.1, Forge 47.4.10, Java 17, Gradle wrapper. Mod 0.2.0; protocolo de rede 2.24; schema persistente 9. Cliente e servidor devem usar a mesma build.

- Ryujin Jakka e Hyōrinmaru tiveram seus kits refeitos por solicitação no ciclo 56; matriz vigente e migração no relatório desse ciclo. Aceite histórico não substitui playtest desta revisão.
- Uma zanpakutō vinculada por vez. Pode trocar identidade, mantendo progresso separado. Asauchi é identidade própria, com Shikai/Bankai de bônus de dano e sem slots elementais; formato 3D atualizado no ciclo 53, com paletas prateada/azul/vermelha.
- Maestria por prática válida de forma/técnica, requisitos graduais e feedback no HUD. Modos administrativos gratuitos suspendem treino. Personagens legados preservam repertório conforme migração anterior ao schema 5.
- Combat State automático: dano elegível inicia/renova 200 ticks. HUD informa combate; morte/reconexão/dimensão limpam estado. Não é Combat Mode manual. Ainda não altera recuperação de reiatsu ou penaliza logout.
- Stamina máxima = 100 + 100 × R / (R + 20), com R sendo nível comprado de Resistência; R20 = 150, limite 200. Compra não recarrega saldo; redução limita saldo. Ainda existem sete atributos atuais, não a migração futura de cinco atributos.
- Corrida em movimento gasta 5/s; tentativa manual de golpe com zanpakutō contra alvo vivo gasta até 4. Recupera 20/s após um segundo sem esforço. Exaustão impede sprint até recuperar 10; ataque básico continua permitido a zero. Técnicas especiais não recebem custo de stamina, apenas suas regras de reiatsu.
- Stamina, pausa e exaustão persistem; reconexão não recarrega, respawn restaura máximo. Tooltip de Resistência na tela K informa stamina máxima em pt_br/en_us.
- Asauchi/Ryujin/Hyōrinmaru são indestrutíveis, inclusive itens antigos com Damage salvo; sem barra. Armas vanilla continuam danificáveis; NPC reparador não foi criado.
- Arma vinculada no inventário aparece embainhada na cintura; empunhada mantém bainha vazia. Só uma bainha, mesmo com duplicatas. Snapshot público informa apenas ausência/guardada/mão principal/secundária.
- Bainha final homologada: curva afunilada, paletas por identidade, esquerda anatômica para destros e espelhamento para canhotos. Escala 0,9775; rotação X -26 graus; Y ±25 graus. Não alterar esses valores sem necessidade aprovada.
- Saque/guarda cosmético nativo de oito ticks, terceira/primeira pessoa; seleção do slot é o gatilho. Não trava ataque. Ação dedicada de embainhar sem mudar slot ainda não existe.
- Esquiva direcional: Alt esquerdo remapeável + direção, sem direção recuo, diagonais normalizadas; custo 15 stamina, cinco ticks, **3,5 blocos nominais** (0,7 por tick), intervalo de 12 ticks. Somente terrestre, sem invulnerabilidade. Agora permitida também no criativo, no chão/sem voo. Refinamento testado e homologado pelo usuário no ciclo 44. [Atualização do ciclo 40](40-refinamento-escudo-criativo-esquiva-ishida-2026-10-15.md), [contrato-base](37-esquiva-direcional-stamina-2026-10-15.md).
- Bloqueio/Postura/Guard Break: segurar X remapeável, escudo acima do personagem com preenchimento/rachaduras, Postura 100, abertura de 20 ticks ao zerar. Guarda mitiga golpes físicos frontais, sem cobrar stamina. Permitida no criativo/sem voo; mantém invulnerabilidade vanilla desse modo. Novo desenho do [ciclo 40](40-refinamento-escudo-criativo-esquiva-ishida-2026-10-15.md) homologado pelo usuário no ciclo 44. Recuperação/persistência no [relatório 39](39-bloqueio-postura-guard-break-2026-10-15.md). Guard Break impede esquiva durante a abertura. F mantém troca vanilla entre mãos.

Aceite final de stamina/progressão e aparência da bainha recebido. Isso não comprova FPS, latência ou homologação com dois clientes reais.

## 3. Onde retomar sem ler todo o histórico

1. Este arquivo.
2. [Consolidação dos ciclos 30–35 e próximos passos](36-consolidacao-homologacao-proximos-passos-2026-10-13.md): contratos, arquivos e validações.
3. Apenas os documentos/trechos pertinentes à tarefa: [combate M06](../game-design/06-habilidades-e-combate.md), [atributos M05](../game-design/05-atributos-e-recursos.md) ou [contrato de novos poderes](contrato-criacao-novos-poderes.md).

Relatórios antigos preservam decisões superadas e avisos de teste pendente da época. O aceite posterior e o código atual devem ser considerados antes de repetir trabalho. Módulos futuros listados no mapa de design não estão necessariamente escritos/implementados. O checklist MVP de setembro não representa sozinho o estado de outubro.

## 4. Mapa técnico mínimo

| Frente | Arquivos em src/main/java/com/bleachmod |
|---|---|
| Recursos/progressão | common/StaminaPolicy, StaminaService, BasicAttackStamina, ProgressionService; common/data/ResourcesData, PlayerData, AttributeData, StatusData |
| Presença e rede | common/ZanpakutoCarry; common/data/CharacterData; common/network/SyncHelper; server/events/TickHandler, CapabilityEvents |
| Bainha/gesto | client/model/ZanpakutoSheathLayer, ZanpakutoMotion; common/visual/ZanpakutoDraw; client/events/ModEntityRenderers, ClientForgeEvents |
| Interface e itens | client/hud/ReiatsuHud; client/gui/StatusScreen; client/input/ModKeybinds; common/item/ZanpakutoItem; init/ModItems |
| Poderes | common/technique; common/visual; gametest |
| Guarda/Postura | common/GuardPolicy, GuardService; common/data/GuardData; common/network/c2s/GuardC2S; client/hud/GuardVisuals |

Traduções: src/main/resources/assets/bleachmod/lang/pt_br.json e en_us.json. Testes gerais: src/test/java/com/bleachmod/MvpRegressionTest.java.

## 5. Git e validação conhecida

Antes do ciclo 37, HEAD era `0b09578` (handoff), precedido por `5bc94be` (consolidação) e `349f016` (bainha final). O ciclo 37 acrescenta a esquiva; consultar `git log` para o commit atual. Branch pode receber integrações da equipe; conferir novamente.

Ciclo 33: 73 regressões e 63 GameTests obrigatórios aprovados. Ciclos 34/35: check/build e 73 regressões aprovados, sem repetir GameTests de lógica inalterada. Logs locais em build/cycle33-validation.log e build/cycle35-validation.log; podem não existir em outro checkout. JAR local: build/libs/bleachmod-0.2.0.jar, gerado e não equivalente a release publicada.

Ciclo 37: check/build e **76 regressões**, **67 GameTests obrigatórios** aprovados. Logs `build/cycle37-validation.log` e `build/cycle37-integration.log`. Não equivale a homologação de teclado, visual ou multiplayer real.

Ciclo 39: check/build e **80 regressões**, **71 GameTests obrigatórios** aprovados. Logs `build/cycle39-validation.log` e `build/cycle39-integration.log`. Escudo/guarda/quebra ainda precisam de playtest visual e de rede. Schema 8 persiste Postura/pausa/quebra sem restaurar tecla pressionada; legado inicia Postura cheia, demais progressos preservados.

Ciclo 40: playtest do usuário recebido com pedidos de refinamento. Check/build, **80 regressões e 73 GameTests obrigatórios** aprovados; log `build/cycle40-integration.log`. Prévia do novo escudo conferida a partir do mesmo código de desenho; não é homologação no cliente. Protocolo 2.11/schema 8 mantidos. Novos visuais/alcance/criativo aguardam nova conferência em jogo.

PowerShell, na raiz, com JDK 17 instalado:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot'
.\gradlew.bat check build --offline --console=plain
# Para alterações de lógica que exigem integração:
.\gradlew.bat runGameTestServer --offline --console=plain
```

O caminho do JDK é desta máquina; conferir em outro ambiente. Offline depende do cache já preparado. Não iniciar duas execuções Gradle simultâneas. Documentação isolada requer revisão de links/diff, não nova build. Nunca apresentar teste automatizado como homologação visual.

## 6. Histórico das escolhas — ciclos 37–43 (roadmap vigente no ciclo 61)

O usuário escolheu implementar esquiva direcional universal com stamina. Após discutir dois toques em WASD, preferiu **Alt + direção**. Implementação do ciclo 37 concluída para playtest; não voltar a propor dois toques como controle vigente. Após testar esta feature, retomar a escolha da próxima implementação com o usuário.

Usuário realizou playtest da esquiva/defesa e solicitou os refinamentos entregues no [ciclo 40](40-refinamento-escudo-criativo-esquiva-ishida-2026-10-15.md); esquiva e defesa foram homologadas posteriormente no ciclo 44. Ordem vigente: **Bloqueio/Postura + Guard Break → Byakuya → Urahara → Uryū Ishida → Mentor → Proficiências → revisão final de Hyōrinmaru/Tōshirō**. Ishida foi acrescentado pelo usuário; posição após Urahara adotada para preservar os kits já priorizados. **Gin retirado; Mentor e Proficiências adiados até confirmação com a equipe.** Byakuya foi implementado/refinado nos ciclos 43–45 e homologado pelo usuário no ciclo 46; frente atual é discutir Urahara. A revisão de "hyori" refere-se ao kit existente de Hyōrinmaru/Tōshirō e fica ao final. [Roadmap revisado](38-roadmap-defesa-treinamento-gin-byakuya-2026-10-15.md).

Outras opções revisadas: mentor com cadeia curta de treinamento; Hohō/Shunpo; pequeno repertório de Kidō; proficiência de Zanjutsu/Hakuda/Hohō/Kidō; migração dos atributos com preservação de saves; novos encontros Hollow/boss; Soul Society/viagens; cooperação. História e regras de aquisição dependem da equipe. Antes de trabalhar em dimensões/mobs, conferir integrações de outras branches para não duplicar trabalho.

Pontos de design a preservar: atributos ≠ proficiência ≠ maestria; Postura é exclusiva de Guard Break e separada da stamina; sem nível global e sem skill tree obrigatória; vínculo principal único. Pesquisa de Byakuya: [41](41-byakuya-pesquisa-possibilidades-kit-2026-10-15.md), proposta histórica [42](42-byakuya-cortes-campo-floral-alcance-2026-10-15.md). O usuário autorizou completar o kit e adotar recomendações próprias no ciclo 43; as pendências de aprovação desses documentos históricos foram sucedidas por essa autorização.

**Entrega atual — [Senbonzakura completa, ciclo 43](43-senbonzakura-kit-completo-prototipo-2026-10-15.md):** item/vínculo `senbonzakura`, quatro slots Shikai (corte direita→esquerda, corrente guiada, convergência, guarda) e quatro Bankai (cinco cortes, jardim, Senkei/Ikka contextual, Hakuteiken). Jardim fixo de raio 10, primeira explosão de dano 6, reativação única entre 100 e 200 ticks dentro do campo; flor por 16 ticks e segundo pulso de dano 14, sem dano contínuo na espera. Passiva +1,5 ENTITY_REACH somente com equipamento/Bankai legítimos. Cinco modelos próprios, bainha branca/violeta, partículas controladas com sprites vanilla de cerejeira e HUD contextual. Protocolo 2.12/schema 8. Valores de balanceamento e visuais são protótipos para playtest, não homologados pelo usuário. Aquisição narrativa permanece pendente; setup administrativo e roteiro no relatório 43. Aizen continua adiado.

Ciclo 43: check/build e **86 regressões, 84 GameTests obrigatórios aprovados**. Log local `build/cycle43-integration.log`; JAR `build/libs/bleachmod-0.2.0.jar`. Cinco modelos/refs/texturas e doze sprites vanilla conferidos; prévia geométrica inspecionada. GameTests de área isolados e fixture de duelo de gelo separado verticalmente para evitar interferência entre arenas. Ainda sem homologação visual no cliente, FPS ou multiplayer real. O próximo trabalho é o playtest da Senbonzakura; não voltar ao plano de implementar apenas o corte Shikai, pois todos os oito slots foram solicitados e entregues.

## 7. Playtest e refinamento vigente — ciclo 44

Usuário homologou esquiva e defesa e pediu refinamento da Senbonzakura. [Contrato/valores/roteiro atuais](44-senbonzakura-vento-prisao-hakuteiken-2026-10-15.md) substituem os parâmetros do protótipo 43: cortes 8, corrente 14, mira da prisão 11/raio 5, Ikka 14 e Hakuteiken 12 blocos; jardim fixo de **raio 40**, com janela de reativação 5–10 s preservada. Pétalas menores, menos densas e com vento; Senkei possui 32 espadas luminosas em duas fileiras, Hakuteiken asas brancas/halo e estocada frontal clara. Lâmina espectral de vidro rosa/branco na Shikai/Bankai. Selada passa a ter quatro slots iguais aos da Shikai, com requisitos/maestria separados; para playtest completo usar `/bleachdev mastery set @s zanpakuto sealed 100`.

Convergência virou prisão: 12 ticks de preparação, 60 de controle, três pulsos de dano bruto 3, levitação I/lentidão V e atração suave; lançador livre após preparar. Controle renovado em concessões de oito ticks; cancelamento interrompe dano/renovação e deixa a concessão expirar sem apagar efeitos de terceiros. Protocolo 2.13/schema 8; builds idênticas nos clientes/servidor. Parâmetros/visuais novos de Byakuya aguardam playtest; esquiva e defesa estão concluídas.

Ciclo 44 final: check/build e **89 regressões + 89 GameTests obrigatórios aprovados**; log `build/cycle44-final-validation.log`, JAR `build/libs/bleachmod-0.2.0.jar`. Inclui correção de timing da corrente para preservar o primeiro pulso quando o primeiro processamento ocorre após a ativação. Modelos/texturas/render types/traduções e conteúdo do JAR conferidos; prévia da geometria de Senkei/Hakuteiken em `Docs/poderes/media/44-senkei-hakuteiken-geometria.png`. Não houve novo playtest visual nem medição de FPS/multiplayer real: confirmar com usuário antes de homologar o refinamento de Byakuya.

## 8. Estado vigente — ciclo 45

Playtest do ciclo 44 recebido: usuário aprovou especialmente o visual da Hakuteiken e solicitou ajustes funcionais. [Contrato e roteiro atuais](45-hakuteiken-dash-jardim-senkei-area-2026-10-15.md): Hakuteiken preserva carga/asas/halo, mas leva o jogador num dash horizontal de **16 blocos em dez ticks**, com dano bruto 20 uma vez por alvo do caminho, colisão em subpassos de 0,2 e correção vanilla ao dono. Guarda/esquiva/ataque manual impedidos durante a execução; sem invulnerabilidade. Cancelamento remove movimento residual. Testar sensação da câmera em cliente real.

Jardim fixo agora tem **raio 35**, pétalas reforçadas (tapete 128 a cada dois ticks, flor 128, expansão/subida 112) e reativação única **a partir de 40 e antes de 120 ticks / 2–6 segundos**. Campo termina aos seis segundos; formação floral é encurtada perto do prazo para concluir a explosão aceita antes de expirar. Custos/cooldowns e danos 6/14 preservados. Ikka deixou de ser uma estocada frontal: reativação da Senkei atinge círculo de **raio 14 em 360 graus**, com lâminas radiais e cobertura/aliados respeitados.

Protocolo **2.14**, schema **8**. **91 regressões e 94 GameTests obrigatórios aprovados**, log `build/cycle45-validation.log`, JAR `build/libs/bleachmod-0.2.0.jar`. Ajustes do ciclo 45 homologados pelo usuário no ciclo 46; Senbonzakura/Byakuya, esquiva e defesa concluídos. Próxima etapa escolhida pelo usuário é **discutir o kit do Urahara**, sem iniciar a implementação dele automaticamente. Aizen adiado e demais prioridades preservadas.

## 9. Próxima discussão — ciclo 46

Registro histórico: a discussão abaixo foi sucedida pela aprovação/implementação do ciclo 47.

Usuário confirmou **Bankai e poderes de Senbonzakura/Byakuya concluídos e prontos**; homologação abrange a entrega do ciclo 45. Não retomar refinamentos desse kit sem pedido novo. Base continua protocolo 2.14/schema 8, 91 regressões/94 GameTests da última implementação.

Frente ativa: **discutir o kit de Urahara / Benihime**, sem implementar ainda. [Pesquisa e proposta inicial](46-homologacao-senbonzakura-proposta-urahara-2026-10-15.md): Shikai candidata com disparo carmesim, barreira, rede e detonação encadeada; Bankai candidata com dissecação, reconstrução, aprimoramento corporal e abertura de caminho. Só os nomes de referência são canônicos; rótulos/efeitos numéricos de gameplay permanecem propostas. Modelo gigante/mãos/costuras como direção visual; decidir primeiro cura própria versus suporte a aliados, depois manifestação móvel/ancorada e escopo do corredor. Não presumir aceite de todos os slots por esta discussão, nem aplicar automaticamente a exceção de arsenal selado de Byakuya a Urahara. Aizen e demais prioridades preservados.

## 10. Entrega vigente — ciclo 47

Usuário aprovou o kit Urahara com suporte a aliados, maior regeneração própria na Bankai e design exclusivo. Implementados item/vínculo `benihime`, quatro slots Shikai (Nake, Chikasumi, Shibari, Hiasobi sobre rede) e quatro Bankai (Dissecação, Reconstrução própria/aliada, três golpes reforçados, corredor). Selada sem slots especiais. [Contrato e roteiro completo](47-benihime-kit-suporte-reestruturacao-2026-10-15.md).

Bankai empunhada: 1 HP/s próprio, 1 HP/2 s aos aliados em raio 8 com visão; time formal do scoreboard ou pet próprio, sem empilhar auras e sem treino passivo. Manifestação visual móvel atrás do dono, braços articulados/costuras e modelos próprios de bengala/Benihime. Corredor até 12, revalida colisão/apoio, não atravessa nem destrói terreno; dissipa projéteis hostis e barreiras Benihime compatíveis. Maestria/custos/cancelamento integrados. Não modificar armas/visuais homologados de outros kits.

Rede **2.15**, schema **8**, builds idênticas necessárias. **Check/build, 95 regressões e 108 GameTests obrigatórios aprovados**, log `build/cycle47-final-validation.log`, JAR `build/libs/bleachmod-0.2.0.jar`. Modelos/assets/traduções/JAR e duas prévias estáticas conferidos. Sem homologação visual em Minecraft, FPS ou multiplayer real. Próxima etapa: playtest do Urahara pelo usuário, com parâmetros de equilíbrio ainda iniciais. Aizen adiado; Uryū Ishida depois, Mentor/Proficiências aguardam equipe, revisão Hyōrinmaru ao final, Gin removido.

## 11. Refinamento vigente — ciclo 48

Usuário pediu rede maior/contrátil, reestruturação com impacto, corredor de três cargas vertical e redesenho de Benihime. Diâmetro 20 confirmado. [Contrato atual e prévias PNG/GIF](48-benihime-rede-cargas-marionete-2026-10-15.md). Protocolo 2.16/schema 8. Manifestação elevada, escala 1,35, 11 blocos atrás, mãos/dedos/mechas continuamente animados e acompanhamento suave. Arma preservada. Shibari expande 1 s, captura até 64 alvos elegíveis e contrai até 5 s; Hiasobi continua raio 4 no centro. Reestruturação cria impactos/empurrão radial nos três golpes. Corredor segue mira 3D; terceiro uso inicia cooldown de 13 s; saída vertical pode ficar no ar, com gravidade normal. Playtest visual/multiplayer ainda necessário.


Validação final: **check/build, 97 regressões e 110 GameTests obrigatórios aprovados**. Log `build/cycle48-verified.log`; JAR `build/libs/bleachmod-0.2.0.jar`. Inclui arrasto real de mob distante, três travessias verticais/recarga, preservação de cargas na troca de forma, empurrão sem dano adicional e animação de repouso. Prévia PNG e 24 quadros do GIF conferidos, traduções e links válidos. Ainda requer playtest no cliente.


## 12. Correção vigente — ciclo 49

Playtest do usuário aprovou a melhora visual e pediu correções de solo/área/contenção da rede. [Contrato atual](49-shibari-solo-hiasobi-radial-2026-10-15.md). Malha ancorada sobre a superfície, sem o deslocamento de -0,8. Hiasobi usa o raio atual ao consumir a rede, mantém contenção durante ignição de 16 ticks e explode radialmente em todos os inimigos elegíveis presentes, sem exigir visão/mira do lançador. Cobertura sólida a partir do centro, aliados e limite 64 respeitados. Puxão até 0,55/tick com subpassos/colisão, Lentidão V, impulso horizontal de fuga suprimido; contração continua cinco segundos. Protocolo 2.17/schema 8. Demais visuais/habilidades preservados; novo playtest da rede pendente.


Validação final: **check/build, 98 regressões e 113 GameTests obrigatórios aprovados**. Log `build/cycle49-validation.log`. Inclui piso real/mira nos pés/quatro quadrantes, dano radial com lançador sem visão, proteção de aliado/alvo fora da área e resistência ao deslocamento de fuga. Malha acima da âncora verificada por regressão. Ainda requer conferência visual e de sensação da contenção no cliente.


## 13. Homologação e discussão vigente — ciclo 50

Em 16/10/2026, usuário confirmou testes e **homologou Urahara/Benihime**, incluindo correções do ciclo 49. Não retomar refinamentos sem pedido novo. Frente atual é discutir melhorias gerais de animações, transformação, modelos, texturas, inimigos, ferramentas e bibliotecas. [Pesquisa com fontes primárias e ordem recomendada](50-homologacao-urahara-pesquisa-ferramentas-2026-10-16.md). Dependência funcional atual continua só Forge: bibliotecas listadas na arquitetura histórica pertencem sobretudo à referência Dragon Mine Z. Candidata GeckoLib 4.8.4 para Forge 1.20.1; PlayerAnimator legado existe, mas manutenção regular encerrada, PAL atual ainda exige verificar artefato/plataforma; Photon 1.x/LDLib em piloto separado. Não confundir documentação moderna NeoForge com nosso Forge 47.4.10. Better Combat/Epic Fight são alterações amplas de arquitetura, não uma instalação cosmética trivial. Nenhuma integração/runtime aprovado por esta pesquisa. Última build permanece 98 regressões/113 GameTests, rede 2.17/schema 8. Documentação isolada revisada; sem novos testes de código. A fila de personagens/prioridades segue preservada até escolha explícita da próxima frente.


## 14. Mudança de direção — ciclo 51

Usuário autorizou Better Combat e inclusão das bibliotecas/ferramentas do ciclo 50 no roadmap, pediu revisar também Ryūjin além de Hyōrinmaru, sempre com base nas documentações da equipe. Vídeo Nozarashi/Berserk analisado visualmente; skill externo game-design-architect lido e aplicado a contratos/estados/aceite. [Plano atual](51-roadmap-better-combat-ferramentas-refatoracao-2026-10-16.md) e [roadmap atualizado](38-roadmap-defesa-treinamento-gin-byakuya-2026-10-15.md). Proposta: inventário do trabalho do colega → Better Combat/adaptação Bleach → autoria/GeckoLib → VFX/Photon/áudio/performance → Hyōrinmaru → Ryūjin → Ishida; Mentor/Proficiências aguardam equipe; Curios/IA/SmartBrainLib/TerraBlender nos módulos correspondentes. Essa ordem fina é proposta, não aprovação fictícia de novos poderes. A restrição histórica sobre adicionar libs/corrigir apenas gelo não deve bloquear a nova autorização. APIs/versões da integração do colega ainda precisam ser identificadas; Nozarashi não foi encontrado no checkout atual/consulta de refs locais. Nenhum fetch/merge/runtime alterado nesta etapa. Última build continua rede 2.17/schema 8, 98 regressões/113 GameTests; só docs revisados. Não duplicar dano/custo/alcance/prática entre Better Combat e Bleach. Aizen adiado, Gin removido.


## 15. Compatibilização inicial — ciclo 52

Usuário autorizou começar enquanto a equipe providencia commit Zaraki. [Piloto entregue](52-better-combat-piloto-asauchi-2026-10-16.md): Better Combat 1.9.0+1.20.1-forge, PlayerAnimator 1.0.2-rc1+1.20-forge, Cloth Config 11.1.136+forge. Dependências externas obrigatórias; `prepareCombatTestPack` gera pasta com quatro JARs. Asauchi tem três movimentos herdados da espada, alcance 3 e multiplicadores 1; demais itens Bleach excluídos do fallback até revisão. Modelos/texturas preservados. Três mixins delimitam contexto por golpe, isolam piloto e desativam dual wield Bleach; custo até 4 por tentativa elegível uma vez por golpe, servidor revalida estado/alvo/alcance/visão. Cliente cancela preparação incompatível, sem substituir validação do servidor. Check/build + 99 regressões/118 GameTests aprovados, log `build/cycle52-validation.log`, rede 2.18/schema 8. Falta playtest de cliente/duas conexões reais e comparação com versão/recursos do colega. Não fazer fetch/merge automático de conteúdo não identificado nem duplicar Nozarashi. Refatorações de Hyōrinmaru e Ryūjin continuam após estabilização da base.
