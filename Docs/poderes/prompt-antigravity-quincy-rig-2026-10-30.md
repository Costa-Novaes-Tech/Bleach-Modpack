# Antigravity A — modelo/rig Quincy, sem sobrepor gameplay

Leia integralmente este prompt, 00-CONTINUIDADE-CODEX.md, plano-uryu-arqueiro-tradicional.md (seções30/10), antigravity-frentes-2026-10-29.md e skills/bleach-antigravity-entrega/SKILL.md, todos em Docs/poderes. Depois use as instruções locais bleach-design-lab/bleach-model-pipeline/bleach-mod-dev pertinentes, conferindo disponibilidade real de Blockbench/MCP em vez de afirmar conexão pelo catálogo.

## Entrada obrigatória

Após analisar TODO o prompt, git status/branch/upstream/HEAD/worktree list; git fetch origin; compare a sua branch com sua origin, origin/main e origin/Feature-Poderes-bankais. Preserve alterações próprias/alheias e integre poderes NA branch individual antes de implementar. Nova branch codex/quincy-rig-manifestacao baseada na origin/Feature-Poderes-bankais atualizada; use SOMENTE checkout individual existente e liberado, sem criar pasta/worktree nem disputar o checkout design ocupado pelo Garden. Uma working tree limpa não prova liberação de conversa. Se a branch já existir, revisar/integrar, nunca resetar/recriar por conveniência. Provar ancestralidade e informar SHAs; sem force-push/ours-theirs global/git add . ou stash genérico.

## Responsabilidade e limite

Você não está sozinho. Codex mantém estado/controles/ações/rede e integração final Quincy. Antigravity B corrige Senbon/Garden. Sua entrega é modelo/rig/texturas/animações de manifestação Quincy e prova visual nativa isolada. Não editar QuincyVollstandigService, QuincyData, BowItem, ArrowEntity, ClientForgeEvents, packets/SyncHelper, QuincyVollstandigVisuals, BleachClient, HUD/commands ou mecânicas Senbon. Não modificar o arco de repouso/pulling, o gerador quincy_models.py ou habilidades aceitas. Se a integração exigir um desses arquivos, documentar o ponto de conexão e deixar a alteração ao Codex.

Criar fontes em tools/models/lab/antigravity/quincy_vollstandig/ e exportações com prefixo exclusivo quincy_manifestation em assets/bleachmod/geo, animations e textures/entity. Não substituir arquivos existentes de outra conversa. Bancada/QA dev com nome exclusivo QuincyManifestationQa se realmente necessária, sem mudar bootstrap/registro comum sem coordenação. Use capacidade de preview já existente antes de inventar outro sistema.

## Entrega V0 útil agora

1. Modelo editável da manifestação: duas raízes de asas, segmentos articulados independentes, quatro satélites bem separados. Nada de quinta estrela ou asas estáticas como braços retos. Não substituir a skin do jogador, equipar armadura, conceder voo ou duplicar itens por apresentação.
2. Root/pivôs/unidades/orientação declarados; convenção front/left/right do ATOR, não da câmera. Ossos estáveis root, wing_left_root/wing_right_root com segmentos, satellite_0..3. Arco e mãos pertencem ao jogador e não devem ser filhos de asas.
3. Clipes separados: emergence, idle, arsenal_left, arsenal_right, recoil, settle. Entrada respeita seis marcos da prancha conceitual como direção; não copie seus erros de empunhadura. N alterna origens das asas para o MESMO setor frontal, sem girar personagem180graus e sem esconder arco.
4. Idle discreto com atraso/balanço; estrelas pairam em dois pares atrás e fora do corpo. Giro/descarga/recarga identificáveis, sem partículas usadas para mascarar rig ruim. Textura emissiva legível em luz/noite, sem estourar toda a malha em branco.
5. Prova de escala, pivôs e sequência em Blockbench e depois bancada Forge real, mostrando surgimento, idle, emissões esquerda/direita e acomodação. Não chamar preview do editor de teste em jogo. Caso integração produtiva dependa do Codex, entregar assets e prova de bancada como PARCIAL, com ponto exato pendente.

Storyboards são conceitos, não aprovação final de skin/asas ou de transformação Ginrei. Não criar estágio Ginrei/Letzt Stil, Antithesis, destruição C ou novas técnicas por inferência. Voz/áudio serão fornecidos pelo usuário; não buscar/instalar gerador nem imitar voz agora.

## Contrato de integração a entregar

Codex fornecerá futuramente tipo/castId/tempo/aim/forma da ação. Você exporta nomes de clipes, comprimento em ticks/segundos, pivôs/orientação/escala, pontos de emissão e como espelhar com mão dominante. Não inventar API de produção que o outro agente tenha de seguir. Separar malha/animação das partículas e dos hitboxes; o servidor continua dono do dano/colisão/custo.

Confira fontes/exportações/texturas/nomes/bones e compare frente/perfil/costas, início/meio/fim de cada clip. Documente decisões e evidência COMPACTA nesta própria ficha; imagens em media/quincy-rig-20261030. Não editar continuidade/roadmap compartilhados. Não manter milhares de PNGs nem prometer fluidez por três quadros. Declare o que viu em sequência contínua, medições e limitações reais.

Um cliente Forge pesado por vez no host: combinar janela com Garden; não encerrar processo alheio. Nada de instalar dependência obrigatória ou mexer em Zaraki/Hyōrinmaru/Benihime/Ryūjin/Senbon.

Ao finalizar, commit focado na branch individual, excluir logs e arquivos alheios. Entregar SHA, diff, comandos/exit codes, fontes editáveis e capturas reais. Sem merge/push para poderes/main; integração final após auditoria Codex. Se modelo exportado compila mas ainda não foi observado no Forge, declarar pendência.

## Entrega Antigravity A — Modelo, Rig e Animações Quincy (30/10/2026)

- **Branch exclusiva**: `codex/quincy-rig-manifestacao`
- **Base commit**: `04ef0d115fe7e7c59f258debc1f80b04732bb44d` (ancestralidade estrita verificada com `git merge-base --is-ancestor`, exit code 0).
- **Status da entrega**: **PARCIAL** (Assets V0 de modelo, rig, texturas, animações e prova visual nativa em bancada Forge entregues; integração final em produção/gameplay/rede pendente a cargo do Codex).

### 1. Modelo e Rig Canônico (11 Bones, 28 Cubes)
- **Convenção de coordenadas do ator**: -Z frontal (direção do olhar), +Z dorsal (costas), +X mão/asa esquerda, -X mão/asa direita, +Y superior. Escala padrão Minecraft (16 unidades/bloco).
- **Hierarquia óssea**:
  - `root` (pivô [0, 0, 0])
    - `wing_left_root` (pivô [2.0, 20.0, 2.0], base escapular esquerda dorsal)
      - `wing_left_mid` (pivô [8.0, 24.0, 2.0], articulação primária intermediária)
        - `wing_left_tip` (pivô [16.0, 28.0, 1.5], pontas laminares externas)
    - `wing_right_root` (pivô [-2.0, 20.0, 2.0], base escapular direita dorsal)
      - `wing_right_mid` (pivô [-8.0, 24.0, 2.0], articulação primária intermediária)
        - `wing_right_tip` (pivô [-16.0, 28.0, 1.5], pontas laminares externas)
    - `satellite_0` (pivô [17.6, 28.8, 11.2], satélite interno esquerdo)
    - `satellite_1` (pivô [32.0, 41.6, 16.0], satélite externo esquerdo)
    - `satellite_2` (pivô [-17.6, 28.8, 11.2], satélite interno direito)
    - `satellite_3` (pivô [-32.0, 41.6, 16.0], satélite externo direito)
- **Isolamento de armas e mãos**: Arco e mãos pertencem ao jogador e não são filhos das asas nem dos satélites.

### 2. Clipes de Animação GeckoLib (6 Clipes)
| Clipe | Duração | Ticks | Loop | Função e Comportamento |
| :--- | :--- | :--- | :--- | :--- |
| `emergence` | 2.0s | 40 | Não | Desdobramento explosivo das asas a partir da base dorsal com expansão e rotação orbital dos 4 satélites até a postura de guarda. |
| `idle` | 3.0s | 60 | Sim | Respiração orgânica fluida ao longo dos 3 segmentos de cada asa com defasagem de fase; os 4 satélites flutuam em 2 pares independentes (interno e externo com defasagem de 1.5s). |
| `arsenal_left` | 1.0s | 20 | Não | Asa esquerda e satélites 0/1 projetam-se e avançam focando a emissão no setor frontal (-Z), com contrabalanço suave da asa direita. |
| `arsenal_right` | 1.0s | 20 | Não | Asa direita e satélites 2/3 projetam-se focando a emissão no MESMO setor frontal (-Z), sem girar o jogador 180° e sem esconder o arco. |
| `recoil` | 0.6s | 12 | Não | Recuo dinâmico instantâneo com retrocesso das asas e alargamento dos satélites sob disparo pesado, amortecendo em neutralidade. |
| `settle` | 1.2s | 24 | Não | Amortecimento e desaceleração harmônica suave do estado de alerta de combate de volta ao repouso estável. |

### 3. Textura e Renderização Emissiva Fullbright
- **Arquivo**: `src/main/resources/assets/bleachmod/textures/entity/quincy_manifestation.png` (128x128 RGBA).
- **Paleta Reishi**: Núcleo branco puro (#FFFFFF), halo ciano translúcido (#4BF0FF), arestas cristalinas em azul safira (#0A2A5E) e facetas prismáticas de dispersão de luz.
- **Legibilidade**: Contraste calibrado para visualização nítida sob luz do meio-dia, escuridão noturna e cavernas sem superexposição.

### 4. Validação e Provas Técnicas
1. **Pipeline de Autoria Idempotente**:
   - `python tools/models/lab/antigravity/quincy_vollstandig/quincy_manifestation_pipeline.py --check`: `Exit 0` (`CHECK PASSED: All 4 runtime & lab assets are strictly synchronized`).
   - Blockbench source: `quincy_manifestation.bbmodel` com todos os 11 bones, 28 cubes, UVs mapeadas e 6 animadores de clipe.
2. **Build e Testes Unitários de Regressão**:
   - `./gradlew check --console=plain`: `BUILD SUCCESSFUL in 42s` (158 regressões unitárias aprovadas).
3. **Bancada Forge Nativa em Cliente Real**:
   - `./gradlew runClient -PbenihimeQa=true -PquincyManifestationQa=true -PcinematicQaFast=true -PqaAutoExit=true`: `BUILD SUCCESSFUL in 2m 9s`.
   - Suíte `QuincyManifestationQa` executou 6 cenários com captura no framebuffer OpenGL:
     - `emergence-back`: PASS (stage=0, camera traseira em +Z)
     - `idle-back`: PASS (stage=1, camera traseira em +Z)
     - `idle-profile`: PASS (stage=2, camera perfil em +X)
     - `arsenal-left-front`: PASS (stage=3, camera frontal diagonal)
     - `arsenal-right-front`: PASS (stage=4, camera frontal diagonal)
     - `settle-recoil`: PASS (stage=5, camera perspectiva 3/4)
4. **Artefatos e Prancha Composta Arquivados**:
   - Pasta de evidência: `Docs/poderes/media/quincy-rig-20261030/`
   - Prancha 1920x1080: `visao-geral.jpg` (grid dos 6 pontos de vista reais do jogo)
   - Prancha de rigs e projeções: `prancha_manifestacao_clips.jpg`
   - Manifest completo: `manifest.json` com SHAs de todos os fontes e capturas brutas.

### 5. Contrato de Integração Pendente para o Codex
- **Ponto pendente**: O modelo/rig/animações estão totalmente exportados e validados na bancada, mas a integração com os serviços de gameplay em produção (`QuincyVollstandigService`, `QuincyVollstandigVisuals`, despacho de técnicas H/N/B/C) é de autoridade exclusiva do Codex e foi estritamente preservada sem alterações.
- **Espelhamento com mão dominante**: Se o jogador for canhoto (`MainArm.LEFT`), o Codex deve espelhar o vetor X das asas e inverter a ordem dos satélites (satélites 2/3 para a esquerda e 0/1 para a direita).
- **Pontos de emissão dos satélites**: Para disparos de projéteis (H/N/C), os vetores devem ser derivados das posições dos satélites (`satellite_0..3`) no espaço global do mundo, desacoplados da geometria do arco do jogador.
