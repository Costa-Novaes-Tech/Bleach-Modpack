# Catálogo principal e ordem do Mission Journal

30/10/2026 — branch `feat/jigui/quests`, base `74a4bb37`, preservando a alteração anterior da Asauchi.

## Resultado

- Apenas as 28 missões de M13/10.32 entram no catálogo ativo: 10 Substituto e 18 Soul Society.
- Removida a geração das três missões demonstrativas da saga antiga e oito quests de teste dos NPCs. Definições antigas já existentes no save não são carregadas. Arquivos e histórico pessoal ficam preservados, sem ofertas/HUD/diário para essas missões.
- HUD mantém prioridade máxima da história, já implementada por `Quest.getHudPriority` e verificada pelas regressões. Fixação de uma quest antiga não a reativa.
- Journal usa a ordem explícita do catálogo, independente de ordem de aceitação, leitura ou sincronização. Preservadas as regras de ocultação de futuras missões.
- Fixtures de QA migrados das quests retiradas para a história atual; teste de recompensa continua verificando entrega única usando recompensas temporárias restauradas ao terminar.

## Verificação

Baseline desta conversa: 161 regressões, 229 GameTests e cliente aprovados após a mudança da Asauchi.

`check build runGameTestServer runClient --offline -PstoryQuestQa=true --console=plain`: exit 0, 162 regressões e 229 GameTests obrigatórios aprovados, cliente Forge real encerrado automaticamente com `Story quest QA complete`. Java 17.0.20.1, Minecraft 1.20.1, Forge 47.4.10. Cache acessado com autorização; nenhuma dependência instalada.

Nova regressão cria catálogo de mundo com arquivos obsoletos extras e verifica que só as 28 quests/duas sagas entram, sem apagar os arquivos antigos. Também inverte as definições e confirma a sequência completa do Journal, incluindo transição entre arcos e missões 10–18 da Soul Society. Cobertura anterior de prioridade máxima do HUD permanece passando.

Evidência real: `run/story-quest-qa/evidence/Story-Quest-QA-1793365507777/`, dez capturas. Inspecionada `09-journal-chronological-order.png`: primeiras cinco entradas são Um Hollow na área, Laços além da morte, A alma no periquito, Uma alma fora do lugar, Grand Fisher. O fixture marca missões concluídas apenas no save descartável para expor o histórico ordenado. Qualidade visual desse marco: 4/5 de legibilidade; sem medição de FPS, combate manual completo ou sessão com dois clientes. Os perfis separados de HUD/navegação foram adaptados e compilados, mas não executados visualmente nesta rodada.

Log completo: `run/campaign-only-validation.log`. Logs rotativos versionados restaurados após executar os testes. Nenhum commit/push.
