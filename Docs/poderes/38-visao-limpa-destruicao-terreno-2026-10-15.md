# Ciclo 38 — Visão limpa no Berserk e destruição real do terreno

Data: 15/10/2026. Branch: `bankai-zaraki`. Base: `494dc74`.

## Pedidos do usuário

1. Com o overdrive do ciclo 37, as partículas cobriam a visão no Berserk Mode: reduzir até não atrapalhar.
2. Os ataques não destruíam blocos: fazer destruir de verdade.

Decisões do usuário (perguntadas antes de implementar): destruição **permanente**, só em **terreno natural**.

## Partículas

- `BerserkVisuals.DENSITY = 0.4`: todo emissor do kit roda a 40% da contagem do ciclo 37. Tamanhos menores (sangue 1,8→1,3; grande 3,6→2,4; escuro 3,0→2,0; brasa 1,6→1,2).
- `BerserkAura`: 2 fios × 2 passos por tick (antes 3 × 4), menos fumaça e brasas. **Em primeira pessoa**, a espiral do próprio jogador fica abaixo do peito (até 1 bloco de altura) e mais larga (raio 1,35), sem fumaça: ela emoldura a visão em vez de cobri-la. Os outros jogadores continuam vendo a espiral completa.
- Cinemática: anéis de pressão a cada 4 ticks (antes 2); anéis pós-erupção a cada 6 ticks (antes 3) e sem pilares repetidos.

## Destruição do terreno — `BerserkTerrain`

- **Terreno natural:** terra/grama/micélio (tag DIRT), areia, cascalho, argila, pedras-base do Overworld e do Nether, nylium, terracota, neve, gelo, folhas, plantas, arenitos, end stone, calcita, espeleotema, soul sand/soil e magma.
- **Nunca quebra:** blocos com inventário/tile entity (baús, fornalhas), minérios, blocos construídos (tábuas, tijolos, vidro, metais), fluidos e blocos inquebráveis.
- **Proteção:** cada bloco passa por `level.mayInteract` (proteção de spawn/borda) e por `BlockEvent.BreakEvent` em nome de quem ataca, então mods de claim/proteção podem bloquear. Ao encontrar um bloco protegido ou não natural, a coluna para de ser cavada.
- **Sem drops**, para evitar dezenas de itens no chão. Até 12 blocos por golpe são lançados como entulho (`FallingBlockEntity`, sem drop) e caem em volta.
- **Limite:** 420 blocos por chamada. Só os 20 primeiros tocam som e partícula de quebra; o resto some silenciosamente.
- **Forma:** crateras em bacia e trincheiras que seguem a superfície de cada coluna (funciona em encostas). Plantas e camadas de neve são removidas sem contar na profundidade.

| Ataque | Destruição |
| --- | --- |
| Golpe comum (martelo) | Cratera no alvo: raio 1 e profundidade 1 (selada), 1,6/1 (Shikai), 2,2/2 (Berserk) |
| Corte Brutal | Cratera 1,5/1 |
| Kendō | Cratera 1,5–2,5 (pela stamina gasta), profundidade 2 |
| Corte Nozarashi | Trincheira de 12 blocos (meia-largura 0,9, profundidade 2) + cratera 2/2 |
| Salto Esmagador | Cratera no ponto de queda: 3,2/2 |
| Beba, Nozarashi | Trincheira de 14 (1,3, prof. 3) + cratera 2,5/3 |
| Investida Berserker | Abre caminho no terreno natural à frente, em altura de corpo |
| Fúria | Crateras 1,5/1; o golpe final faz 2,5/2 |
| Corte que Racha o Mundo | Trincheira de 30 (1,6, prof. 4) + cratera 3,5/3 |
| Cinemática | Socos: crateras 1,3/1; erupção: cratera em anel de raio 5,5 e profundidade 3, preservando um pilar de raio 1,3 sob o Zaraki; golpe final: trincheira de 18 (1,2, prof. 3) + cratera 3/2 |

As trincheiras começam 1 a 1,5 bloco à frente para não cavar embaixo de quem ataca.

## Validação

- `compileJava` e `regressionTest` passam.
- Novo GameTest `BerserkTerrainGameTests.craterCarvesOnlyNaturalTerrain`: pedra, grama e planta somem; baú e bloco de ferro ficam; minério e tábuas não são naturais; deepslate e areia são.
- **Teste de clima da Hyōrinmaru:** neste ambiente ele passou a falhar sempre. A investigação mostrou que o código não é a causa. O commit do ciclo 37 e este ciclo aplicado numa worktree limpa passaram 5/5. Ele só falha quando o **WorldEdit** (dependência local, staged em `build.gradle` e não commitada) está carregado. É uma condição de corrida antiga do teste, que o WorldEdit torna frequente.
- **Não verificado no cliente:** a visibilidade em primeira pessoa, o tamanho das crateras na prática e o desempenho de muitas quebras em sequência.

## Ajustes rápidos

- Densidade geral: `BerserkVisuals.DENSITY`. Aura: `BerserkAura.STRANDS/STEPS/FIRST_PERSON_*`.
- Tamanho dos buracos: chamadas a `BerserkTerrain.crater/trench` em `NozarashiTechniqueService` e `NozarashiReleaseEffects`; raio do golpe comum em `BerserkTerrain.hammerRadius/hammerDepth`.
- Blocos aceitos: `BerserkTerrain.natural`.

## Correção do teste de clima (mesmo dia)

Causa real: `zone_legacy` e `zone_radius` (Bankai da Hyōrinmaru) chamavam o caster de clima em `onEachTick`. Depois do cancelamento no `finally`, um tick seguinte ainda com Bankai **readquiria** o lease global de clima, que ficava até 40 ticks. Quando `weather_packets` rodava logo depois (ordem frequente com o WorldEdit carregado), o `acquire`/`release` dele virava no-op e o `STOP_RAINING` nunca era enviado.

- Os dois testes voltam o jogador para a forma selada antes de cancelar, para o caster liberar o lease em vez de renovar.
- `BlizzardWeatherData.idle()`: o teste de pacotes espera o lease global ficar livre antes de executar (timeout de 120 ticks), sem mudar nenhuma asserção.
- Resultado: 64/64 em 5 execuções seguidas com WorldEdit (antes falhava 7/7) e 3/3 sem WorldEdit.

