# Ciclo 61 — roadmap revisto e próximas entregas

Revisão de 18/10/2026, solicitada enquanto o usuário testa Higashi/Jardim. Git limpo ao iniciar, branch `Feature-Poderes-bankais`, base `971824f`. Revisão documental e leitura de refs locais; sem fetch, merge, instalação ou alteração de gameplay. Este documento atualiza a ordem técnica dos ciclos 38/51 sem aprovar automaticamente novos kits.

## Estado real das entregas

- **Homologado anteriormente:** stamina/Resistência, bainhas, esquiva, defesa/Postura/Guard Break; kits Byakuya e Urahara em suas entregas anteriores. Não reabrir essas bases sem feedback ou necessidade concreta de integração.
- **Implementado e entregue para teste:** revisão Ryūjin/Hyōrinmaru, combos próprios, liberações e gestos dos demais kits (52–59). A aprovação anterior de um kit não homologa automaticamente cada animação adicionada depois. Hyōrinmaru recebeu aceite recente do usuário de que está boa no momento; preservar esse estado.
- **Em playtest agora:** Higashi com execução até 7% e câmera opcional; Jardim com pétalas internas persistentes, ciclo 60. Última build: rede **2.23**, schema **9**, técnicas revisão **2**, **110 regressões/135 GameTests aprovados**. Falta retorno visual, FPS e multiplayer real.
- **Better Combat autoritativo:** Asauchi, Ryūjin e Hyōrinmaru. Senbonzakura/Benihime possuem gestos e swings cosméticos, mas continuam fora das hitboxes do adaptador. Uma migração futura precisa preservar o bônus de alcance Bankai Senbon (+1,5), sem multiplicá-lo ou truncá-lo.
- **GeckoLib já utilizada:** dragão de gelo possui `geo/ice_dragon.geo.json` e `animations/ice_dragon.animation.json`. Isso não significa que todos os modelos e manifestações tenham sido convertidos; o modelo Hollow Boss atual ainda usa `HierarchicalModel` vanilla.

## Novidade: Zaraki disponível para análise local

A referência local **`origin/bankai-zaraki` existe** e aponta para `c17d274` (`Merge origin/main into bankai-zaraki`), com `b0a0e81` de voz Bankai e os commits do kit/modelos/Berserk. Foi lida diretamente com `git show`; nenhum checkout/fetch foi feito. Não se afirma que essa ref corresponda ao estado mais recente do GitHub.

O diff desde o ancestral comum envolve 68 arquivos, incluindo Nozarashi, pele/aura Berserk, `CinematicDirector`, animações PlayerAnimator, bainhas/HUD, rede, progressão, áudio, testes e destruição permanente de terreno natural. Os relatórios dessa branch são `36-kit-nozarashi-berserk-mode-2026-10-15.md`, `37-berserk-intensidade-espiral-martelo-2026-10-15.md`, `38-visao-limpa-destruicao-terreno-2026-10-15.md` e `39-voz-bankai-zaraki-2026-10-18.md`, sob `Docs/poderes` na própria ref. Seus números de ciclo colidem com os nossos: referenciar branch/arquivo, sem sobrescrever relatórios locais.

**A pendência deixou de ser “aguardar o commit existir”; agora é compatibilizar e integrar o conteúdo disponível.** O vídeo não é mais a única referência de implementação.

Pontos concretos para a integração:

1. Preservar nossa baseline 60 e comparar contratos/arquivos compartilhados; não substituir em bloco `TechniqueDefinitions`, `CharacterData`, `CombatEvents`, HUD, bainha ou testes.
2. Ambas as branches usam MC 1.20.1/Forge 47.4.10 e PlayerAnimator 1.0.2-rc1. A branch Zaraki obtém a biblioteca pelo Maven KosmX; a nossa usa o artefato Modrinth para Forge. Resolver como **uma biblioteca**, sem empacotar dois JARs PlayerAnimator. Manter Better Combat/GeckoLib e revisar metadados de runtime.
3. A branch Zaraki declara rede **2.18**, enquanto a nossa está em **2.23**. Conciliar registros/direções dos pacotes e escolher protocolo da integração; nunca retroceder a 2.18 nem resolver conflito apenas aceitando um lado.
4. Conciliar `CinematicDirector` com `ReleaseAnimationService`, poses/gestos e câmera de execução Higashi: uma ação deve possuir a câmera e a camada de animação de cada vez, com prioridade e restauração em cancelamento/morte/dimensão. Preservar opções locais de câmera/FOV e servidor como dono da transformação.
5. Nozarashi ainda precisa de decisão de perfil Better Combat: os swings de martelo da outra branch não comprovam adaptação ao nosso débito único de stamina e validação por golpe. Conferir dano, alcance, múltiplos alvos e prática, sem duplicação.
6. O Gradle Zaraki inclui WorldEdit local em `run/validation-dependencies` para desenvolvimento. Não torná-lo requisito do pacote/jogador nem transportar caminho local como dependência obrigatória. Avaliar separadamente os ajustes de memória/build.
7. Preservar as regras documentadas pela equipe para destruição natural permanente e testar restrições; não propagar esse comportamento automaticamente às outras Bankais. Trazer os testes correspondentes e conciliar os ajustes de clima Hyōrinmaru com nossas fixtures atuais.
8. Validar toda a suíte combinada, montagem de pacote, servidor dedicado e dois clientes; conferir simultaneamente Nozarashi e os kits existentes. Os 64 GameTests relatados pela outra branch não substituem nossos 135 nem comprovam o resultado de um merge.

## Ferramentas: o que já entrou e o que falta

**Runtime atual:** Better Combat **1.9.0 Forge**, PlayerAnimator **1.0.2-rc1 Forge**, Cloth Config **11.1.136 Forge**, GeckoLib **4.8.4 Forge** fixada em `aC5KMoNg`, com mclib 20 embutida. Pacote contém cinco JARs contando Bleach. Confirmado em Gradle/properties e no pacote existente; nenhuma versão nova pesquisada nesta revisão.

**Autoria ainda precisa de padronização:** Blockbench para modelo/UV/animação; Aseprite ou LibreSprite para texturas/sprites; Audacity para sons e Blender como apoio. Não são mods para instalar no servidor. Definir pastas, nomes, pivôs, escala em blocos, paleta, fonte editável, exportação e origem dos assets. Não foram encontrados arquivos `.bbmodel`, `.blend`, `.aseprite` ou `.aup3` na listagem de arquivos desta branch. A branch Zaraki já possui geradores em `tools/models`, `tools/animations` e conversor de áudio: avaliar seu reuso antes de criar outro fluxo.

**Ainda não integrados ao Gradle/pacote atual:** Photon/LDLib, Curios, SmartBrainLib, TerraBlender e spark. Continuam candidatos do roadmap; não declarar instalação concluída só porque foram pesquisados. Revalidar artefatos/versões Forge 1.20.1 na execução de cada piloto.

- **spark/medição:** preparar bancada reproduzível; medir cliente (tempo de quadro/FPS) e servidor (MSPT/TPS) separadamente. Cenários: Jardim raio 35, dragões triplos, manifestação Benihime, Berserk e várias pessoas lançando técnicas. Registrar máquina, distância de renderização, gráficos e quantidade de jogadores/efeitos. Não inventar metas numéricas sem baseline.
- **Photon + LDLib:** piloto isolado de uma trilha/liberação depois da compatibilização, comparado ao efeito nativo; avaliar legibilidade, transparência/shaders, limpeza e custo antes de migrar técnicas. Não é pré-requisito para considerar os kits atuais funcionais.
- **Curios:** quando o contrato de acessórios/equipamentos Quincy justificar slots; não substitui vínculo ou bainha.
- **SmartBrainLib:** piloto coordenado com autores dos Hollows/boss, comparando com Goals/Brain atuais. Não criar um segundo Hollow independente.
- **TerraBlender:** somente na frente de biomas/worldgen, coordenada com dimensões. Não bloqueia combate, animações ou novos poderes.

## Próxima ordem recomendada

1. **Receber o playtest do ciclo 60** e corrigir problemas reproduzidos, preservando Hyōrinmaru. Fechar sensação da câmera, leitura da incineração e cobertura do Jardim.
2. **Compatibilização Zaraki/Nozarashi**, agora que há código local para comparar. Primeiro inventário e plano de integração; em seguida implementação em etapa própria, com validação combinada e pacote reproduzível.
3. **Consolidar apresentação e medir custo:** fluxo compartilhado de assets, sons/legendas, prioridades de câmera/animação, testes com observador e redução de efeitos. Avaliar migração dos ataques básicos Senbon/Benihime sem perder alcance, como entrega própria.
4. **Bancada Photon/LDLib** com um efeito e comparação; escolher adoção com resultado observado. Modelos/IA de inimigos podem ser a frente seguinte se coordenados com a equipe, sem alterar silenciosamente a prioridade do próximo personagem.
5. **Uryū Ishida continua o próximo kit de personagem planejado.** Antes de codificar: pesquisa específica e contrato Quincy de identidade/aquisição, arma/projétil, recurso, equipamentos, progressão e repertório. O NPC Uryū existente não equivale a um kit jogável Quincy. O M06 local ainda lista Quincy como bloco pendente; o contrato de quatro slots de zanpakutō não deve ser reutilizado automaticamente.
6. **Mentor e Proficiências continuam adiados**, aguardando confirmação com a equipe. Aproveitar NPC/quests existentes quando forem liberados. Conservar distinção atributo × proficiência × maestria; sem migração automática dos sete atributos atuais para cinco.

**Aizen segue adiado; Gin continua fora da fila.** Não há necessidade de começar do zero outra revisão Ryūjin/Hyōrinmaru: a refatoração solicitada já foi implementada nos ciclos 55–58; faltam aceite/refinos específicos e integração.

## Documentos que orientam os próximos contratos

- Entrada: [continuidade](00-CONTINUIDADE-CODEX.md), [entrega 60](60-higashi-execucao-jardim-persistente-2026-10-18.md), [animações 59](59-liberacoes-gestos-senbon-benihime-asauchi-2026-10-17.md) e [matriz 56](56-repertorio-elemental-cinematica-combos-2026-10-17.md).
- Arquitetura/ferramentas: [51](51-roadmap-better-combat-ferramentas-refatoracao-2026-10-16.md), [piloto 52](52-better-combat-piloto-asauchi-2026-10-16.md), [contrato de novos poderes](contrato-criacao-novos-poderes.md).
- Equipe: [M05](../game-design/05-atributos-e-recursos.md), [M06](../game-design/06-habilidades-e-combate.md), [raças/origens](../game-design/04-racas-e-origens.md), [NPCs/quests existentes](../planejamento/plano-npcs-quests.md). O mapa conceitual inclui sistemas futuros; conferir implementação antes de assumir que existem.

O manual em `Docs/poderes` ainda continha no topo “Byakuya próximo”; recebeu apontamento para o estado atual. O manual de `Docs/jogador` e outros documentos fora desta pasta não foram editados. Uma revisão completa do manual de poderes/controles e do roteiro administrativo é recomendada após estabilizar a integração Zaraki, para não publicar instruções contraditórias a cada etapa.

Validação desta revisão: refs, código/build, documentos e links locais conferidos; `git diff --check`. Sem repetir testes de código, pois não houve mudança de runtime. Build e pacote do ciclo 60 permanecem os mesmos, sem nova homologação implícita.
