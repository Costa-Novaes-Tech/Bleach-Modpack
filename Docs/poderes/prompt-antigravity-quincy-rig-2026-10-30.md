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
