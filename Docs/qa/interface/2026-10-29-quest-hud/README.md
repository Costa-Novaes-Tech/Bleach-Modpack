# HUD de quests aceitas — 29/10/2026

Base: `99064862874ec466b73a52b3dacc27882997fc02`, branch `feat/jigui/quests`.
Checkout inicialmente limpo. Minecraft 1.20.1, Forge 47.4.10, Java Temurin 17.0.20.1.

## Comportamento

- Quests aceitas aparecem por importância: `SAGA` (história) antes de `SIDEQUEST`.
- Dentro da mesma importância, preferência manual do diário; depois ordem de aceitação.
- Aceitar uma secundária não substitui uma quest de história rastreada.
- Concluir/falhar remove a quest do HUD e seleciona automaticamente a próxima elegível.
- Preferência manual conserva o campo NBT existente `trackedQuestId`; limpar a preferência retorna ao modo automático.
- Painel no canto superior direito, com destaque dourado da rastreada, título e progresso dos objetivos pendentes ativos. Até três cards, conforme espaço; contador das demais no diário.
- Respeita o painel de status, guia da Zanpakutō e hotbar. F1 e tela de debug ocultam o tracker.
- Categorias adicionais de importância ainda não existem no modelo atual; não se infere prioridade de nomes, IDs ou texto livre de categoria.

## Verificação

- Baseline: `check --offline --console=plain`, 146 cenários, exit 0.
- Após implementação: `check build --offline --console=plain`, 150 cenários, exit 0.
- Quatro novos cenários: prioridade sobre secundária aceita/fixada, desempate manual/ordem de aceitação, exclusão de inativas/definições ausentes e fallback, persistência NBT.
- Rodada final e cliente real: `check build runClient -PquestHudQa=true --offline --console=plain`, exit 0, 150 cenários; log em `validation-final.log`.
- Primeira abertura (`validation.log`) parou no menu: a checagem do diretório não normalizava o sufixo `/.`. Normalização corrigida, cliente reiniciado e sequência final executada integralmente.
- Perfil opt-in `questHudQa`: somente desenvolvimento e diretório `run/quest-hud-qa`; cria save novo `Quest-Hud-QA-*`, usa definições existentes e não inicia os confrontos/spawns das quests de teste.
- Cenário: aceita secundárias e história, fixa uma secundária, confirma história rastreada no servidor, registra progresso 2/6, captura escalas GUI 2/3/1, conclui história e confirma fallback à secundária preferida; captura também F1.
- Resultado visual: cinco capturas reais inspecionadas em `screenshots/`, metadados em `frames.jsonl`. Framebuffer 1280×720; escalas 2/3/1 com duas/uma/três quests visíveis, respectivamente. História em dourado com 2/6; secundária preferida logo abaixo. Depois da conclusão, secundária preferida assume o topo, restando três aceitas. F1 oculta HUD inteiro. Cliente encerrou automaticamente e Gradle concluiu com sucesso.
- Limite observado: notificações temporárias do tutorial vanilla podem cobrir parte do cabeçalho no canto superior direito (`03`/`04`); painel não reserva uma área exclusiva para toasts. Sem sobreposição com status, guia da Zanpakutō ou hotbar no cenário capturado. Capturas em inglês; chaves novas também disponíveis em português.
- Logs rotacionados na raiz pelo executor de regressão foram restaurados aos bytes de HEAD; nenhum log anterior foi incluído como alteração da feature.

Sem GameTests adicionais: a mudança é de seleção/apresentação, coberta pela regressão comum e exercício no cliente. Servidor dedicado e dois clientes não exercitados nesta rodada. Nenhuma medição de FPS.
