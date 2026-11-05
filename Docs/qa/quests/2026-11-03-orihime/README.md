# MAIN-SUB-02 — QA das etapas de Laços além da morte

Data: 03/11/2026. Branch observada antes das alterações: `feat/jigui/quests`, HEAD de referência `33e341d3`; checkout inicialmente limpo e um commit atrás de `origin/feat/jigui/quests`. Nenhum checkout, commit ou publicação nesta validação.

Ambiente confirmado no projeto: Minecraft Java 1.20.1, Forge 47.4.10, toolchain Java 17; execução observada com Eclipse Adoptium 17.0.20.1+1 amd64. Perfil base; Photon não faz parte do escopo.

## Escopo

O usuário solicitou introdução com a segunda Rukia (`karakura_orihime`, junto da Orihime), combate, retorno à mesma Rukia, recompensa e liberação da missão de Chad. A primeira Rukia (`karakura_inicio`) não deve iniciar nem concluir este caso. Morte do jogador preserva as etapas concluídas; perda do ator do encontro não deve deixar o combate sem possibilidade de retomada.

Não há recompensa aprovada na ficha anterior de M13: a implementação usa **100 pontos de XP vanilla provisórios para teste**, pagos na conclusão, uma vez por personagem. O boss canônico `bleachmod:story_orihime_brother` continua pendente: por direção explícita, não usar Hollow/Boss genérico como substituto. Portanto esta entrega não equivale a um confronto completo jogável.

## Testes e limites

`OrihimeQuestGameTests` (quatro cenários) cobre a exigência de concluir a primeira missão, isolamento do contexto da segunda Rukia, aceite sem início involuntário, conservação da introdução quando o boss está pendente, morte/respawn com checkpoint, save/load da etapa de retorno, conclusão e XP uma única vez e liberação de Chad somente após a conversa final. O quarto cenário verifica migração do contrato antigo, preservação de conclusões, recusa de contratos personalizados e backup exato do arquivo antigo; seus artefatos ficam em `qa-orihime-migration/<UUID>` dentro do diretório descartável dos GameTests.

Para verificar morte durante combate e conclusão sem inventar um inimigo implementado, o teste cria **estado sintético de combate/progresso**. O terceiro cenário usa um zumbi exclusivamente como ator sintético para verificar rastreamento persistente, morte ambiental e remoção, chamando o mesmo recuperador usado no tick do jogador. Isso valida a máquina de estados, a persistência, a recuperação e a recompensa; não comprova ataques, morte real ou apresentação do futuro boss.

| Verificação | Resultado |
|---|---|
| Compilação/check/build | PASSOU: execução final `check build runClient`, `BUILD SUCCESSFUL`, exit code 0 |
| Regressões Java | PASSOU: **165 cenários**, incluindo proteção de contratos personalizados, na execução final |
| GameTests do servidor Forge | PASSOU na reexecução: 235 testes obrigatórios; `run/orihime-validation-retry.log`, `BUILD SUCCESSFUL`, exit code 0 |
| Perda/morte ambiental de ator | PASSOU na primeira execução: cenário com ator sintético; sem equivaler ao boss canônico |
| Cliente gráfico e evidências visuais | PASSOU no recorte introdução/pending/respawn: execução real com 13 capturas, sem boss substituto |
| Boss canônico, combate e morte real | PENDENTE: ator não implementado |
| Servidor dedicado com dois clientes | PENDENTE |

`StoryQuestQaBootstrap` foi estendido preservando a sequência original: depois da primeira missão, aceita a segunda pelo painel, verifica a segunda Rukia junto da Orihime, abre o diálogo, confirma a introdução com o boss pendente e mata/renasce o jogador para verificar retenção da introdução. As capturas 08–12 são produzidas pelo framebuffer real; não há vitória sintética de boss no cliente.

A primeira execução terminou com 234/235 testes passando e falha por entidade nula em `deathandrespawnpreserveopeningcheckpoints`. A limpeza do encontro foi corrigida para coletar os atores antes de descartá-los, evitando alterar a coleção viva durante a iteração. A reexecução passou os 235 testes obrigatórios em 03/11/2026; os quatro cenários de Orihime passaram em ambas as execuções. O guard posterior para contratos personalizados foi compilado e validado na execução final com 165 cenários de regressão, seguida do cliente gráfico.

Diretórios de QA existentes são separados dos saves pessoais: `run-gametest/story-quest-start` para GameTests com `-PstoryQuestQa=true` e `run/story-quest-qa` para o cliente de teste. Resultados só devem ser marcados como passaram após observar saída e exit code; capturas apenas do framebuffer real do Minecraft.


## Evidência da execução final

Comandos executados pelo coordenador com Java 17 e caches locais:

```powershell
.\gradlew.bat check build runGameTestServer --offline -PstoryQuestQa=true --console=plain
.\gradlew.bat check build runClient --offline -PstoryQuestQa=true --console=plain
```

Servidor: 235 testes obrigatórios passaram na reexecução, `BUILD SUCCESSFUL in 5m 14s`, exit code 0. Cliente/check/build final: 165 regressões passaram e o cenário registrou `Story quest QA complete` às 20:13:34 em America/Sao_Paulo (relógio do log, conferido pelo timestamp do cenário), terminando com `BUILD SUCCESSFUL in 5m 22s`, exit code 0. Logs copiados: [servidor](gametest-validation.log), [cliente/check/build](client-validation.log).

Mundo descartável: `run/story-quest-qa/saves/Story-Quest-QA-1793747535696`. Origem das imagens: `run/story-quest-qa/evidence/Story-Quest-QA-1793747535696/screenshots`, capturadas por `Minecraft Screenshot.grab` do framebuffer real em 1280×720. As 13 capturas e o [manifesto de ticks](frames.jsonl) foram copiados para esta pasta; não são renders externos.

Inspeção visual das capturas 08–12:

- [08 — dupla e navegação](screenshots/08-orihime-pair-and-navigation.png): Orihime e segunda Rukia distintas visíveis, objetivo inicial e navegação a 5 blocos.
- [09 — introdução](screenshots/09-orihime-introduction-dialogue.png): texto legível, nome da missão e recompensa provisória de 100 XP visíveis.
- [10 — combate pendente](screenshots/10-orihime-combat-pending-hud.png) e [11 — diálogo pendente](screenshots/11-orihime-combat-pending-dialogue.png): introdução concluída 1/1; boss 0/1; interface explica que aguarda implementação. Nenhum inimigo substituto foi criado.
- [12 — após morte e respawn](screenshots/12-orihime-respawn-retains-introduction.png): mesma dupla e missão permanecem; asserções de servidor confirmam retenção da introdução e UUID da segunda Rukia.

O recorte observado no cliente passou. A conversa final/recompensa/liberação de Chad foi verificada tecnicamente nos GameTests por progresso sintético; continua **pendente de validação visual com o boss canônico real**, assim como ataques, morte do boss e sua apresentação. Sem validação multiplayer com dois clientes, Photon ou medição de desempenho nesta rodada. Nenhum save pessoal foi usado e nenhum processo de QA foi deixado em execução pelo subagente.
