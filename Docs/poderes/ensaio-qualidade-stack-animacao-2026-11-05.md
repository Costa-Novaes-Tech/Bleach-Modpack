# Ensaio de qualidade — merge Rukia e stack de animação

**05/11/2026.** Trabalho na `Feature-Poderes-bankais`, HEAD inicial `9eb6a05` (merge da missão Rukia). Os worktrees `main`, Quincy e design foram conferidos e preservados. Este ensaio não alterou gameplay, assets distribuídos, dependências de produção nem fez merge/commit. Os arquivos de laboratório e logs brutos ficam em diretórios ignorados pelo Git.

## Resultado reproduzido

| Gate | Resultado e evidência |
|---|---|
| Compilação/regressões | `gradlew check build --offline`: sucesso, **176 cenários de regressão**. O primeiro lançamento no sandbox parou no lock do cache Gradle fora do workspace; a execução com acesso autorizado ao cache passou. |
| GameTests integrados | `gradlew runGameTestServer -PstoryQuestQa=true --offline`: **295/295 obrigatórios**; `run-gametest/story-quest-start/logs/latest.log` registra `All 295 required tests passed :)`. O mundo de teste foi criado nesse perfil isolado. |
| Bankai Ryūjin, cliente base | `runClient -PbenihimeQa=true -PcinematicQaFast=true -PryujinBankaiQa=true -PryujinBankaiQaCase=0 -PqaAutoExit=true -PanimationCompatQa=base --offline`: QA `day_back` concluiu em terceira pessoa. 36 capturas e `frames.jsonl` em `run-benihime-qa/animation-compat/base/ryujin-bankai-qa/cycle118-1793907109075/01_day_back/`. Nos quadros inspecionados, a espada sobe antes do campo de chamas; sol e chamas aparecem atrás do ator. Isto verifica uma amostra diurna, não fluidez contínua, escuta, multiplayer ou aceite artístico. |
| Rukia, cliente integrado | `runClient -PstoryQuestQa=true --offline`: `Story quest QA complete` e `BUILD SUCCESSFUL in 2m 32s`; nove PNGs e `frames.jsonl` em `run/story-quest-qa/evidence/Story-Quest-QA-1793907618681/`. A sequência exercita aceite no Journal, Rukia correta, morte/retomada, morte ambiental do boss sem crédito, vitória válida, retorno, diálogo final, Asauchi única e liberação da missão seguinte. Conferidos visualmente diálogo inicial e Journal concluído. É fixture em mundo plano/criativo; não demonstra balanceamento manual, multiplayer ou persistência após relog. |

O JAR `build/libs/bleachmod-0.2.0.jar` foi gerado pelo build. O QA de cliente acima usa o runtime de desenvolvimento Forge 47.4.10/MC 1.20.1/Java 17, sem um launcher Forge empacotado.

O parâmetro `animationCompatQa=base` existiu apenas durante o laboratório para redirecionar o diretório do cliente e foi retirado junto com a tentativa de carregar mods externos no userdev. Para repetir a cena no `build.gradle` atual, usar os mesmos parâmetros **sem** `-PanimationCompatQa=base`; as capturas novas irão ao perfil QA normal. Na evidência de Rukia, o painel de diálogo e o Journal preservam informações legíveis a 1280×720, mas o HUD/toast ao fundo mantém a tela carregada; revisar opacidade e hierarquia visual em playtest humano antes de chamá-la de apresentação final.

Leitura dos três marcos Ryūjin inspecionados: no tick 40, jogador grande no plano e lâmina vertical; no 80, câmera aberta mostra a massa de fogo atrás, cobrindo a formação do sol; no 130, o sol modelado reaparece inteiro acima do jogador, com chamas separadas na base. A ordem visual preparação → explosão → revelação se sustenta nesses quadros. Eles não mostram a interpolação entre marcos nem provam sincronização do áudio. O tamanho e a direção do sol já receberam retorno do usuário em rodada anterior; qualquer alteração artística deve responder a um novo playtest, e não apenas a essa amostra.

## E1 — mods de apoio: o que o teste mostrou

Foram obtidos dos releases oficiais, verificados por SHA-256 e guardados **apenas** em `build/animation-compat-mods/`:

| Mod | Release 1.20.1 Forge | SHA-256 |
|---|---|---|
| [Not Enough Animations 1.12.7](https://github.com/tr7zw/NotEnoughAnimations/releases/tag/1.12.7) | `notenoughanimations-forge-1.12.7-mc1.20.1.jar` | `e32732b87c6e0cf5031959c5746d1d3e8125dd39a9ba98627a2d2599d4085fb0` |
| [Skin Layers 3D 1.11.4](https://github.com/tr7zw/3d-Skin-Layers/releases/tag/1.11.4) | `skinlayers3d-forge-1.11.4-mc1.20.1.jar` | `df839af815c55fff84136136bc02868d93771f4a7fcbbf9a9f9d0e04c0d29904` |

O lançamento de NEA pelo diretório `mods` do `runClient` falhou antes do mundo: `NoSuchMethodError` em métodos `m_...` de Minecraft. O [ForgeGradle explica](https://docs.minecraftforge.net/en/fg-6.x/dependencies/) que JARs de produção precisam passar por `fg.deobf` no userdev. O segundo lançamento remapeou o JAR principal via dependência **temporária de QA**, mas a biblioteca `TRansition-1.0.28-1.20.1-forge-SNAPSHOT.jar` embutida no release permaneceu em nomes de produção e falhou no construtor (`DetectedVersion.m_195834_`, seguido de `NoSuchFieldError`). Skin Layers 3D também embute `TRansition` e `TRender`, inspecionados no índice do JAR; **não foi executado** com sucesso e sua compatibilidade não está avaliada. A configuração temporária de Gradle foi removida depois do diagnóstico. `BUILD SUCCESSFUL` de `runClient` nesse caso refletia o término do processo Gradle, não sucesso de carregamento do jogo; o log `build/animation-compat-nea-remapped.log` contém o erro real.

**Conclusão limitada:** não há evidência de que NEA ou Skin Layers 3D quebrem a versão distribuída do mod. Também não há evidência para recomendá-los no pack. E1 continua aberto. O próximo teste válido deve usar uma instalação **Forge de produção isolada** com o JAR do Bleach Mod e as mesmas versões dos mods, em quatro perfis (`base`, `NEA`, `Skin`, `ambos`), cada qual com mundo/skin próprios e sem reutilizar saves pessoais. Essa matriz deve observar Bankai Ryūjin, golpes Better Combat/Senbon, armadura, skin classic/slim, primeira/terceira pessoa e um segundo observador. Um crash em qualquer perfil deve ser atribuído pelo log de carregamento antes de avaliar a cena.

## Contrato para reduzir retrabalho em poderes novos

1. **Ficha de cena antes de modelar:** origem da habilidade, âncora do prop (`mão`, `jogador`, `mundo`, `alvo`), eixo frente/trás, duração em ticks, preparação, execução, sustentação, retorno e cancelamento. Desenhar os marcos em quatro vistas: jogador, alvo, lado e observador.
2. **Composição do corpo:** PlayerAnimator controla pose do jogador; Better Combat controla golpes; GeckoLib anima prop/entidade. Para cada clip, declarar camada/prioridade, membros afetados e quando a camada é retirada. A branch já usa, por exemplo, 3800/3950 no Hakuteiken, 4000 no release, 4500 nas entradas cinematográficas e 5000 no diretor; qualquer novo clip deve ser testado contra esses estados reais, sem escolher prioridade por tentativa visual isolada.
3. **Relógio único:** o servidor decide início, custo, forma, dano e cancelamento. O cliente usa o mesmo tick de cena para pose, prop, VFX, câmera e som. Guardar o evento de início, não cada partícula, e comparar jogador/observador após relog, distância e troca de dimensão.
4. **Gate visual repetível:** captura contínua com áudio para fluidez/sincronia e frames com telemetria para geometria. Checar silhueta do corpo, direção da lâmina/prop, leitura da área antes do dano, retorno sem salto de pose e limite de partículas. FPS de um único arranque não é benchmark; comparar após aquecimento, na mesma cena/configuração, com mediana e quedas.
5. **Gate de compatibilidade separado:** usar mods de apoio como cosmético/QA somente após provar carregamento no cliente empacotado. NEA amplia gestos vanilla em terceira pessoa; Skin Layers 3D dá volume à segunda camada da skin. Nenhum deles substitui autoria de animação Bankai. APIs de quest/equipe/configuração seguem decisão de arquitetura e P0 de saves, não entram por associação com BleachCraft.

**Próxima fatia recomendada:** concluir E1 em Forge empacotado; em paralelo, executar E2 com um clip de quatro fases e prop sincronizado, começando por uma cena que já tem referência (Ryūjin) e congelando o contrato de âncora/câmera antes de adicionar novas partículas. Para a missão Rukia, o merge `9eb6a05` substitui a premissa antiga de “fora da branch”; a abertura aprovada em 30/10 entrega Asauchi **ao concluir**, não ao criar personagem. Atualizar P0/roadmap antes de desenhar mentores ou uma segunda recompensa.
