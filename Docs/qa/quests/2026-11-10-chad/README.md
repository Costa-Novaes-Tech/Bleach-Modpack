# A alma no periquito — QA das etapas e da bússola

10/11/2026. Branch `feat/jigui/quests`, base `88544a21`. Os ajustes locais do merge em `QuestEvents.java` e seu relatório já estavam no índice no início da tarefa. Minecraft Java 1.20.1, Forge 47.4.10, JDK Adoptium 17.0.20.1 e Gradle 8.8.

O cenário cobre o desbloqueio após Orihime, a distinção entre ID de entidade, ID de diálogo, contexto e UUID de Chad, o aceite separado do diálogo e a troca de destino da bússola. `bleachmod:story_shrieker` continua reservado. Um GameTest usa `minecraft:husk` apenas em seu catálogo temporário para percorrer spawn, movimento, perda de ator, retomada e vitória pelas rotas reais de servidor. A definição de produção conserva o alvo canônico. O teste de migração cobre o backup de `03.json`, conclusões antigas e configurações customizadas.

## Reprodução

```powershell
.\gradlew.bat check build runGameTestServer --offline -PstoryQuestQa=true --console=plain
.\gradlew.bat runClient --offline -PacidwireQa=true --console=plain
```

As execuções usam, respectivamente, `run-gametest/story-quest-start` e `run/acidwire-qa`. O cenário gráfico gera um save plano novo e usa os pacotes de aceite e confirmação. A validação de combate real de Shrieker, mapa definitivo e sessão com dois clientes depende de trabalho posterior.

## Cliente observado

`runClient` terminou com exit code 0. O cenário registrou `Chad introduction and compass verified` às 16:31:04 no log `run/acidwire-qa/chad-client.log`. O mundo descartável `Acidwire-QA-1794339005778` mostrou o Chad provisionado após Orihime, a oferta, o objetivo inicial e a distância de 4 m. Depois da confirmação, o HUD exibiu `Conteúdo em preparação` e conservou Chad como destino. Um aviso da missão anterior se sobrepõe a parte do cabeçalho do HUD nas duas capturas de navegação. Objetivo, seta e distância permanecem legíveis.

- [Introdução de Chad](screenshots/16-chad-introduction.png)
- [Bússola no HUD](screenshots/17-chad-compass.png)
- [Combate em preparação](screenshots/18-chad-combat-pending.png)
- [Manifesto do framebuffer](frames.jsonl)

## Servidor observado

O `check` de base e o `check` final passaram com 165 regressões Java. `build` concluiu antes do servidor. A primeira execução do GameTestServer encontrou `SenbonzakuraTechniqueService$Prison` ausente enquanto o cliente recompilava a árvore compartilhada. A execução isolada chegou ao fim, sem o erro de classe.

Dos 243 GameTests, 242 passaram, incluindo os três novos casos da missão de Chad. A falha restante foi `AcidwireGameTests.clawCanBeDodgedAndCancelRestoresIdle`, já registrada no [QA do merge](../2026-11-05-merge-quests/README.md). O Gradle retornou exit code 1 pela falha da suíte. Log da execução isolada em `run-gametest/chad-validation-sequential.log`.
