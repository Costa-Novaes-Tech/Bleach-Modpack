# Localização de quests e bússola — 29/10/2026

Base: `99064862874ec466b73a52b3dacc27882997fc02`, branch `feat/jigui/quests`.
O checkout já continha a implementação do HUD de quests aceitas, ainda sem commit; essas alterações foram preservadas.
Minecraft 1.20.1, Forge 47.4.10, Java Temurin 17.0.20.1, Gradle Wrapper 8.8. Perfil base, sem Photon.

## Comportamento implementado

- Rukia mantém somente `rukia_npc_test`, a quest existente do Hollow Boss, com objetivos, recompensas e spawn preservados. O treinamento antigo `rukia_basic_training` deixa de ser gerado e é ignorado no carregamento, inclusive em saves existentes. A retirada usa o ID legado explícito, permitindo criar novas quests da Rukia depois. Os JSONs e o histórico do jogador ficam preservados em disco; não se reinicia a campanha nem a quest do boss.
- NPCs registram automaticamente UUID, identidade, dimensão e bloco em `bleachmod_quest_npcs` (SavedData do mundo). Isso também cobre futuros NPCs gerados com estruturas, quando suas entidades forem adicionadas ao mundo; esta alteração não implementa a geração aleatória de estruturas.
- Aceitação pela interface associa a quest ao UUID do NPC validado pelo servidor. Outra instância do mesmo personagem não substitui esse destino. Quests antigas/administrativas sem vínculo escolhem um NPC conhecido, priorizando a dimensão atual e a distância, e fixam esse vínculo.
- Descarregar chunks mantém o destino. Recarregar ou mover o NPC atualiza a posição. Destruir o NPC elimina a posição conhecida. Não se carregam chunks nem se geram estruturas para procurar destinos.
- HUD mostra nome do NPC e distância em metros (1 bloco = 1 m, distância tridimensional arredondada). Distância e direção usam a posição atual do jogador; destinos são atualizados pelo servidor a cada segundo, e somente alterações geram sincronização adicional.
- A quest rastreada ganha uma bússola integrada ao seu card: marca branca indica a frente do jogador, agulha dourada aponta para o NPC. Quando está no mesmo ponto horizontal, mostra um ponto central. Ao concluir/falhar, a próxima quest elegível assume o rastreamento.
- Outra dimensão é informada por nome, sem agulha ou distância enganosa. Sem NPC conhecido, mostra localização desconhecida. Quests sem `quest_giver` continuam exibindo seus objetivos, sem inventar um destino.
- Mantidas prioridade de história, preferência manual no mesmo nível e ocultação com F1/debug. Escala compacta dá preferência ao título e à navegação; progresso continua no diário. Nenhum item foi criado.

## Validação

- Baseline: `check --offline --console=plain`, exit 0, 150 cenários.
- Primeira rodada após implementação: `check build runGameTestServer --offline --console=plain`, exit 0, 154 regressões e 225 GameTests obrigatórios aprovados (`integration.log`). A última movimentação antes de unload e a preservação de futuras quests da Rukia receberam ajustes posteriores; a rodada final recompila e repete os casos.
- Novas regressões: persistência do índice e vínculo com múltiplas instâncias; ângulos cardeais/rotação e distância vertical; snapshots de navegação e conclusão/falha/reaceitação; retirada do catálogo de Rukia preservando boss e outros NPCs.
- Dois GameTests exercitam eventos reais de spawn/unload/reload/destruição e aceitação com vínculo/snapshot/falha.
- Cenário gráfico opt-in: `runClient -PquestNavigationQa=true --offline --console=plain`. Restrito a desenvolvimento e `run/quest-navigation-qa`. Cria somente um save novo `Quest-Navigation-QA-*`. Aceita a quest do boss pelo serviço real e descarta exclusivamente seu mob neste cenário de apresentação; cria Rukia original e uma duplicada mais próxima; testa rotação, aproximação, escalas 2/3/1, transferência da NPC ao Nether, destino desconhecido, fallback e F1. Capturas vêm do framebuffer real, com metadados em `frames.jsonl`.

- Rodada completa recompilada: `check build runClient runGameTestServer -PquestNavigationQa=true --rerun-tasks --offline --console=plain`, exit 0, 154 regressões e 225 GameTests obrigatórios aprovados (`client-integration.log`). Reexecutou também a preparação/cache Forge (`downloadMCMeta`, `downloadAssets`, `extractNatives`); duração total 1h12min. Usou Java e dependências já configurados no projeto, sem atualização de versões. O diretório gráfico novo exibiu a tela de boas-vindas do Minecraft; foi usado apenas o botão Continue pela skill Computer Use para liberar o cenário.
- A primeira sequência gráfica registrou a troca de dimensão e a seleção de Ichigo antes de chegarem ao cliente, durante a geração do Nether. O cenário foi corrigido para esperar a tarefa de servidor e a confirmação do estado sincronizado antes dessas capturas. Nenhum resultado visual foi inferido desses frames antecipados.
- Rodada gráfica final: `check build runClient -PquestNavigationQa=true --offline --console=plain`, exit 0, 154 regressões, duração 3min17s (`client-final.log`). Após o segundo passe verde dos GameTests, mudaram apenas a espera/asserções do cenário gráfico e as traduções de distância/local desconhecido; não se repetiu a bateria de servidor sem necessidade.
- Dez capturas finais reais, todas inspecionadas, em `screenshots/`; metadados em `frames.jsonl`. Origem: `run/quest-navigation-qa/evidence/Quest-Navigation-QA-1793321985304/`. Mundo de QA novo e separado. `bleachmod_quest_npcs.dat` foi gravado no save ao encerrar.
- `01`/`02`/`03`: Rukia original a 80m, apesar de existir uma duplicada mais próxima; agulha para frente/esquerda/trás com yaw 0/90/180. `04`: aproximação reduz a distância para 40m. `05`: escala 3 mantém bússola e distância (texto em duas linhas). `06`: escala 1 mostra os três cards, incluindo distância de Orihime e Ichigo não localizado.
- `07`: Rukia efetivamente transferida ao Nether, texto informa a dimensão e oculta agulha/distância. `08`: Ichigo rastreado, sem destino conhecido, sem agulha. `09`: Orihime rastreada a 40m com direção à esquerda. `10`: F1 oculta o HUD.
- Legibilidade avaliada em 4/5: navegação legível, sem sobreposição com status/guia/hotbar nas capturas finais; escala 1 usa naturalmente fonte menor e escala 3 quebra o texto da distância em duas linhas. A notificação temporária de início de missão foi limpa somente no cenário de QA, antes das capturas; a sobreposição transitória existente de toasts não foi alterada no produto.
- JSONs de idioma e `git diff --check` válidos. Logs comprimidos rotacionados na raiz pelo executor de regressão restaurados aos bytes de HEAD. Mudanças anteriores do HUD preservadas; sem commit ou push.

GameTestServer validado. Servidor dedicado convencional com jogadores, sessão com dois clientes e perfil Photon não exercitados nesta rodada. Sem medição de FPS ou frametime.

Falhas intermediárias preservadas: comparação estrita de `0.0` com `-0.0` corrigida no teste de ângulo (`initial-regression.log`); uso de `LazyOptional.orElseThrow` sem o fornecedor exigido pelo Forge corrigido nos cenários de QA (`initial-compile.log`); teste novo compilado contra uma classe anterior durante edição concorrente ao build, resolvido com fontes estabilizadas e `--rerun-tasks` (`intermediate-build.log`). Nenhum teste foi desativado.
