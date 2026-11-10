# Resolucao do merge de quests — 05/11/2026

Branch: `feat/jigui/quests`. Commit local: `4c7ca63b` (Rukia and Orihime Tasks).
Commit remoto integrado: `31e5c71c` (Fix: Invalid way to die).
Base comum: `33e341d3`. O merge ja estava em andamento antes desta intervencao.

## Resolucao

- Estado inicial: tres arquivos `UU` e nove arquivos com alteracoes remotas ja no index.
- `RukiaOpeningQuest.java`: preservadas as etapas locais de Rukia/Orihime e a recuperacao periodica; incorporados o tratamento remoto de morte sem credito e a recuperacao ao interagir com Rukia. Mantidas as duas assinaturas de `recoverMissingEncounter` usadas pelo codigo e pelos testes de ambos os lados.
- `StoryQuestService.java`: mantida a implementacao local que coleta entidades antes de remove-las; a correcao remota era equivalente.
- M13: preservadas as duas notas, sobre Acidwire e morte do boss sem vitoria valida.
- Os tres arquivos foram marcados como resolvidos com `git add`. Nenhum commit, push, reset, stash ou troca de branch foi executado.
- Estado apos a resolucao: nenhum arquivo nao mesclado e nenhum marcador de conflito nos tres arquivos. `git diff --cached --check`: exit 0.
- Durante os testes, o merge foi concluido externamente no commit `88544a21` (`asd`); a branch passou a aparecer sincronizada com `origin/feat/jigui/quests`. O agente nao criou esse commit.

## Ajuste revelado pelos GameTests

A primeira rodada detectou que `onEntityKill` e `openingBossDiedWithoutCredit` eram subscribers independentes na mesma prioridade `LOWEST`. O handler de recuperacao podia reiniciar a tentativa antes de o outro conceder o credito da morte legitima. O teste remoto `environmentalOrForeignBossDeathAllowsRetryWithoutGrantingVictory` falhou em "Legitimate victory was reset".

`openingBossDiedWithoutCredit` agora e um helper privado chamado explicitamente por `onEntityKill`, imediatamente depois de `creditQuestKill`. Assim, a verificacao de recuperacao sempre observa o resultado do credito. O mesmo GameTest existente cobre a regressao, sem enfraquecer assercoes.

## Ambiente e validacao

Windows, Minecraft 1.20.1, Forge 47.4.10, Gradle Wrapper 8.8.
Java Eclipse Adoptium 17.0.20.1+1, ja instalado no cache Gradle do usuario.
`JAVA_HOME` ajustado somente no processo de validacao, sem alterar configuracoes globais.
O primeiro comando nao iniciou por falta de Java utilizavel no terminal. A tentativa com JDK 17 no sandbox foi bloqueada pelo lock do cache; a execucao seguinte teve acesso autorizado ao cache existente.

Comando de validacao:

```powershell
$env:JAVA_HOME = 'C:/Users/jguic/.gradle/jdks/eclipse_adoptium-17-amd64-windows/jdk-17.0.20.1+1'
.\gradlew.bat check runGameTestServer -PstoryQuestQa=true --console=plain
```

Primeira rodada: `run-gametest/merge-quests-validation.log`.
`check` e compilacao aprovados, 165 cenarios de regressao passaram. Foram executados 240 GameTests: 225 passaram e 15 falharam. Uma falha era a ordem dos eventos da quest, corrigida acima. As outras 14 envolveram IA de Hollow, Acidwire, treino de habilidades e poderes fora dos trechos modificados; esta rodada nao comprova que sejam falhas preexistentes.

Segunda rodada, apos a correcao: mesmo comando com `--offline`, usando o cache existente. Log: `run-gametest/merge-quests-validation-final.log`.
GameTests usam o diretorio de QA separado `run-gametest/story-quest-start`.
Resultado da segunda rodada: compilacao e `check` aprovados, 165 cenarios de regressao passaram. Dos 240 GameTests, 239 passaram e um falhou. Todos os testes de StoryQuest/Orihime passaram, inclusive o de morte ambiental, morte por outro jogador e vitoria legitima que revelou a ordem incorreta dos eventos.

Falha restante: `AcidwireGameTests.clawCanBeDodgedAndCancelRestoresIdle`, mensagem "Dodged claw still damaged distant victim". Esse teste tambem falhou na primeira rodada, e o teste e a implementacao do ataque do Acidwire nao foram alterados nesta resolucao. Nao foi investigada a causa nesta tarefa de conflito Git; a suite completa permanece reprovada. Gradle: exit 1, `BUILD FAILED in 2m 34s`; processo GameTestServer: exit 1. A primeira rodada terminou com Gradle exit 1 e GameTestServer exit 15.

Os logs compactados versionados que a segunda execucao rotacionou foram preservados antes da rodada e restaurados depois. O ajuste de ordem de eventos e este relatorio atualizado ficam locais para revisao; nenhum commit adicional foi criado pelo agente.

Nao foi realizada nova validacao visual no cliente Minecraft, multiplayer com dois clientes ou perfil Photon. As evidencias visuais dos commits originais foram preservadas; nao constituem uma nova homologacao deste merge.
