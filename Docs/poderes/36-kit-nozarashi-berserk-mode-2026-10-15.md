# Ciclo 36 — Kit Nozarashi (Zaraki Kenpachi) e Berserk Mode

Data: 15/10/2026. Branch: `bankai-zaraki` (autorizada pelo usuário). Base: `1c9cac4`.

## Decisões aprovadas

- Identidade `nozarashi`; a Bankai se chama **Berserk Mode** (`form.bleachmod.nozarashi.bankai`).
- Espadas sempre em modelo 3D por cubos com texturas vanilla (padrão Hyōrinmaru); cutelo bem grande e fiel ao anime/mangá.
- Animação de corpo com **PlayerAnimator** (KosmX), dependência obrigatória do modpack.
- Cinemática de ativação **sempre completa**.
- Pele: por enquanto, recolorir só os tons de pele; sistema de skins estilo Dragon Block C fica para o futuro.

## Arquivos

| Área | Arquivo |
| --- | --- |
| Identidade/item | `CharacterData.NOZARASHI`, `ModItems.NOZARASHI`, `ZanpakutoCarry`, `TechniqueProgression`, `BleachCommands` (bind) |
| Nome das formas | `common/evolution/FormNames.java` (HUD, telas, mensagens) |
| Modelos | `models/item/nozarashi*.json`, gerados por `tools/models/nozarashi_models.py` |
| Kit | `TechniqueDefinitions` (12 entradas), `NozarashiTechniqueService` |
| Liberação/cinemática (servidor) | `NozarashiReleaseEffects`, `ReleaseCinematicS2C` |
| Cinemática (cliente) | `client/cinematic/CinematicDirector.java`, `player_animation/berserk_release.json` (gerado por `tools/animations/berserk_release.py`) |
| Pele/olhos | `client/berserk/BerserkSkins.java`, `BerserkSkinLayer.java` |
| Visuais | `EffectLibrary.Palette.BERSERK`, `common/visual/BerserkVisuals.java` |
| Carregar sem bainha | `ZanpakutoSheathLayer.carryBare` (katana na cintura, Nozarashi nas costas) |

Protocolo de rede **2.10** (novo pacote). Schema de save inalterado: a identidade é texto e é aceita por `knownIdentity`.

## Modelos

- **Selada:** katana muito longa e maltratada, com vários trechos realmente ausentes no fio, mordidas escuras, aço enegrecido, guarda dourada gasta e cabo marrom enfaixado. Escala na mão: 1,15.
- **Shikai:** cabo longo claro com amarrações marrons, virola dourada com furo e borlas verdes, e uma **cabeça de cutelo negra colossal**, larga e quase retangular, com fio branco contínuo. Cabo, encaixe e lâmina compartilham o mesmo eixo longitudinal; a curvatura fica concentrada no dorso e o afunilamento apenas na ponta curta. Escala na mão: 2,05.
- **Berserk Mode:** reutiliza a geometria e a orientação axial do Shikai, trocando o corpo por aço negro, o fio por vermelho e acrescentando rachaduras vermelhas emissivas (`forge_data` com luz 15). Escala na mão: 1,78.
- Os elementos JSON ficam limitados a [-16, 32]; o tamanho vem dos `display`. Para ajustar, edite o gerador e rode `python tools/models/nozarashi_models.py`.

## Tabela de técnicas

Requisito de maestria por slot: 0/20/40/60 (regra comum).

| Forma | Slot | Técnica | Custo | Recarga (ticks) | Efeito |
| --- | --- | --- | --- | --- | --- |
| Selada | 1 | Corte Brutal | 10 | 60 | Cone 4,5 blocos, 6 de dano, empurrão |
| Selada | 2 | Pressão Espiritual | 15 | 200 | Raio 6: Lentidão II + Fraqueza I por 3 s, empurra; controle (sem dano) |
| Selada | 3 | Remover o Tapa-olho | 0 + 0,1/tick | 100 | Liga/desliga; +30% de dano de zanjutsu e aura enquanto houver reiatsu |
| Selada | 4 | Kendō a Duas Mãos | 20 | 300 | Gasta toda a stamina: dano 6 + 0,12 × stamina gasta, num alvo |
| Shikai | 1 | Corte Nozarashi | 20 | 120 | Fenda de 12 blocos, 10 de dano, **sem quebrar blocos** |
| Shikai | 2 | Salto Esmagador | 25 | 160 | Salto à frente; ao aterrissar, impacto em raio 4,5 com 9 de dano; sem dano de queda |
| Shikai | 3 | Sede de Batalha | 25 | 400 | 10 s: cada golpe recebido soma +5% de dano (máximo +40%); rouba 15% do dano causado |
| Shikai | 4 | Beba, Nozarashi | 40 | 400 | Meia-lua vertical, 10 blocos, 22 de dano |
| Berserk | 1 | Investida Berserker | 25 | 100 | Avanço de 10 ticks; 10 de dano em cada alvo atravessado |
| Berserk | 2 | Fúria | 30 | 160 | Três golpes em 12 ticks (8/8/12 de dano) |
| Berserk | 3 | Rugido | 35 | 300 | Raio 10: Lentidão III + Fraqueza II por 5 s, 4 de dano, empurrão |
| Berserk | 4 | Corte que Racha o Mundo | 60 | 1200 | Carga de 1 s parado; linha de 30 blocos com 45 de dano. **Recuo:** 15% da vida máxima (nunca letal), Fraqueza II e Cansaço II por 10 s |

Todo dano passa por `MasteryPracticeService.hurt` e os alvos por `TechniqueTargets`. O estado é transitório e limpo ao trocar de forma, morrer, sair, mudar de dimensão ou parar o servidor.

## Cinemática do Berserk Mode (100 ticks)

| Ticks | Servidor | Cliente de quem ativa |
| --- | --- | --- |
| 0–30 | Batimentos, anéis convergindo, jogador congelado e invulnerável | Close no rosto → plano de cima girando; pulso vermelho; barras de cinema |
| 30 | Pilar de reiatsu com caveira, explosão, rugido, empurrão em raio 8 | Plano aberto e baixo, tremor forte, flash que **esconde a troca de pele** (que aparece no tick 32) |
| 55–80 | Aura, rugido do Ravager no tick 60 | Contra-plongée enquanto ele se levanta, olhos amarelos |
| 82–100 | Golpe de impacto ao pegar a espada | Movimento da lateral para trás do ombro, zoom de impacto, título "Berserk Mode" |

Quem está por perto vê a animação do corpo e as partículas. A câmera, o HUD oculto e o bloqueio de input valem só para quem ativou, e o modo de câmera anterior é restaurado no fim.

## Validação

- `compileJava`, `regressionTest` (inclui o teste novo do kit Nozarashi) e `runGameTestServer`: 63/63 nas duas últimas execuções.
- O teste `restoringWeatherAndClearCommandSynchronizeConnectedClients` falhou uma vez de forma intermitente; na base `1c9cac4` e em duas reexecuções desta branch ele passou. Ele depende do estado global de clima compartilhado entre testes.
- **Não verificado no cliente:** aparência e escala dos modelos na mão e nas costas, os sinais de rotação da animação (convenção Bedrock), os enquadramentos de câmera, a pele recolorida e os olhos. Isso precisa de homologação visual.

## Roteiro de teste no cliente

1. `/bleachdev zanpakuto bind @s nozarashi` e pegue a Nozarashi na aba criativa.
2. `/bleachdev skill set @s zanpakuto 2` e `/bleachdev mastery set @s zanpakuto sealed 100` (repita para `shikai` e `bankai`) para liberar slots e formas.
3. Selada: teste os 4 slots, principalmente ligar e desligar o tapa-olho e o Kendō com stamina cheia e vazia.
4. Shikai: confira o modelo na mão (1ª e 3ª pessoa) e nas costas; teste o Salto Esmagador caindo de uma altura.
5. Berserk Mode: assista à cinemática inteira em 1ª e 3ª pessoa; repita com um segundo jogador olhando; confira a pele (skins clara, escura e "estranha") e os olhos à noite.
6. Pontos a ajustar sem mexer em Java: escala e posição em `tools/models/nozarashi_models.py`, e os keyframes em `tools/animations/berserk_release.py` (ou abra o JSON no Blockbench).

## Limites conhecidos

- A câmera não desvia de paredes: em espaços fechados ela pode atravessar blocos.
- Os olhos ficam na posição padrão das skins (linha 4 do rosto); skins com olhos em outro lugar ficam desalinhadas.
- O modelo da espada já muda para o Berserk Mode quando a cinemática começa; só a pele espera o flash.
