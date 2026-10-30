# Retomada da etapa após morte — primeira quest da Rukia

30/10/2026, branch `feat/jigui/quests`, base `74a4bb37`; preservadas as mudanças anteriores.

## Regra específica

Somente a morte em `MAIN-SUB-01` usa checkpoints: aceite e introdução permanecem concluídos; no combate o boss antigo é removido, o progresso do confronto volta a zero e a tentativa recebe UUID novo. A quest continua `ACCEPTED`, vinculada à mesma Rukia. O jogador pode tentar novamente pelo Journal dentro da área válida ou pela mesma NPC, sem repetir a introdução. Morte antes da conversa mantém o aceite; morte após vitória mantém a etapa de retorno. O restante das quests conserva a falha completa existente.

O reset de etapa é uma operação explícita de `QuestProgress`; sua política é chamada apenas pela primeira quest no evento de morte. Não foi alterado o comportamento de falha explícita genérica nem criado um padrão global de checkpoint. Não há mudança de definição da quest, portanto o progresso atual não exige migração.

## Evidência

- Baseline anterior da conversa: 163 regressões e 230 GameTests aprovados.
- `check build runGameTestServer runClient --offline -PstoryQuestQa=true --console=plain`: exit 0, **164 regressões e 231 GameTests obrigatórios aprovados**, cliente real com `Story quest QA complete`. Log: `run/rukia-death-checkpoint-validation.log`.
- GameTest novo cobre mortes antes da introdução, durante o boss e no retorno; remoção do alvo antigo; nova tentativa; preservação do vínculo; clonagem Forge após invalidação das capabilities; repetição sem duplicar boss; rejeição do alvo antigo; entrega única da Asauchi. Também confirma que a segunda quest ainda passa a `FAILED` na morte.
- Regressão nova verifica persistência NBT do checkpoint, dos objetivos concluídos, do UUID do destino e da nova tentativa, mantendo a operação genérica de falha completa intacta.
- Cliente real: morte por `ServerPlayer.kill`, respawn por `LocalPlayer.respawn`, Journal exibindo a introdução concluída e o confronto pendente; retomada pelo pacote real do botão e conclusão posterior. Cenário isolado: `run/story-quest-qa/evidence/Story-Quest-QA-1793369223584/`. Captura de respawn inspecionada e copiada neste diretório. O rótulo foi encurtado para **Tentar novamente** após a captura para melhorar a leitura.

Minecraft 1.20.1, Forge 47.4.10, Java 17.0.20.1. Cache Gradle autorizado; nenhuma dependência instalada. O mundo de QA é descartável e criativo, com dano de teste para vencer o boss. Não houve validação com dois clientes ou balanceamento de luta manual. Sem commit/push.
