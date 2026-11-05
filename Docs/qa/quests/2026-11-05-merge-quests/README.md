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

Log completo local: `run-gametest/merge-quests-validation.log`.
GameTests usam o diretorio de QA separado `run-gametest/story-quest-start`.
Resultado final: em execucao.

Nao foi realizada nova validacao visual no cliente Minecraft, multiplayer com dois clientes ou perfil Photon. As evidencias visuais dos commits originais foram preservadas; nao constituem uma nova homologacao deste merge.
