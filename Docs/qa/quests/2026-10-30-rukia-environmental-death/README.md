# Fishbone morto sem crédito — recuperação do confronto

30/10/2026. Branch `feat/jigui/quests`, base `33e341d3` (Primeiro Encontro). Estado inicial limpo.

## Correção

A morte do Fishbone por lava ou por outro jogador não validava a vitória, mas deixava `encounterStarted` ativo. A etapa ficava sem alvo e sem possibilidade de nova tentativa.

Um handler de `LivingDeathEvent` em prioridade `LOWEST`, sem receber eventos cancelados, verifica o estado depois do processamento normal do crédito. Somente o boss da primeira quest, do proprietário e da tentativa atual pode liberar o checkpoint de combate quando a vitória não foi validada. A introdução e o destino de Rukia permanecem; o Journal e a mesma Rukia permitem repetir. A vitória legítima já avançou ao retorno e não é resetada. Outra quest não recebe essa política.

Também há recuperação de saves já travados: interagir com a Rukia vinculada procura um alvo vivo carregado com a identidade da tentativa. Se ele não existe, libera a retomada, sem forçar chunks. Se um alvo antigo estiver descarregado, a nova identidade de tentativa o invalida quando voltar a carregar com o dono online. Um boss vivo carregado não é resetado só por conversar com Rukia.

A limpeza agora reúne os alvos antes de removê-los, evitando invalidar a coleção viva de entidades durante a iteração.

## Validação

- Baseline anterior da conversa: 164 regressões, 231 GameTests e cliente aprovados.
- Execução final: `check build runGameTestServer runClient --offline -PstoryQuestQa=true --console=plain`, **exit 0, 164 regressões e 233 GameTests obrigatórios aprovados**. Log: `run/rukia-environmental-death-final.log`.
- Dois GameTests novos usam o boss real: morte com `DamageSources.lava()`, golpe de outro jogador, morte cancelada, evento de tentativa antiga, preservação da introdução/UUID do destino, próxima missão oculta, recuperação de boss removido, conversa sem resetar alvo vivo e conclusão legítima posterior com uma Asauchi.
- A primeira execução revelou uma falha de iteração na limpeza de entidades no teste existente de morte/respawn. Corrigida a remoção durante a varredura; a rodada final completa passou.
- Cliente Forge real: cenário isolado `run/story-quest-qa/evidence/Story-Quest-QA-1793406382693/`, encerrado com `Story quest QA complete`. Após a morte/respawn anterior do fixture, o boss da nova tentativa recebe dano de lava, a missão permanece aceita com introdução concluída, o botão **Tentar novamente** é habilitado, e o confronto é repetido pela ação da Rukia. Vitória pelo jogador e diálogo final continuam entregando uma única Asauchi e liberando a próxima missão.

A captura `04b-lava-death-retry.png` foi inspecionada: objetivo inicial 1/1, confronto 0/1, botão de retomada ativo e mensagem explicando a morte sem crédito. É uma captura do framebuffer real. A lava foi aplicada como fonte de dano ao boss real, não por construir uma piscina no mundo. Modo criativo e dano de teste; sem medição de desempenho ou sessão com dois clientes. Nenhum save pessoal foi utilizado.

Minecraft 1.20.1, Forge 47.4.10, Java 17.0.20.1. Cache Gradle autorizado; nenhuma dependência instalada. Logs rotativos versionados restaurados. Nenhum commit/push.
