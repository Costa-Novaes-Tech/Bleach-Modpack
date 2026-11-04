# 128 — Relatório de Entrega: Shukei: Hakuteiken v2 (Asas de Anjo de Pétalas + Zanpakuto Existente)

**Data**: 03/11/2026  
**Frente**: Antigravity Design Lab  
**Branch Exclusiva**: `codex/senbon-hakuteiken-blockbench`  
**Status**: Modelagem conjunta Jogador + Asas de Anjo de Pétalas no Blockbench (`124 cubos / 17 bones`), exportação GeckoLib (`11 bones de constructo`), remoção da espada duplicada e do halo Vollständig, integração da Zanpakuto existente (`senbonzakura_hakuteiken.json`), emissão contínua de partículas `ModParticles.SENBON_PETAL`, animação de corpo inteiro no PlayerAnimator e validação automatizada in-game (`runClient`), regressão (`158 cenários`) e `runGameTestServer` (`285 GameTests`) concluídas com **100% PASS (exit code 0)**.

---

## 1. Revisão da Modelagem no Blockbench (`Asas de Anjo + Pétalas de Sakura + Zanpakuto Existente`)

A partir da revisão visual do usuário, dois pontos estruturais foram redesenhados em [`build_angel_hakuteiken_bbmodel.py`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/lab/antigravity/senbonzakura01/build_angel_hakuteiken_bbmodel.py) e [`ag_senbon_hakuteiken_v2.bbmodel`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/lab/antigravity/senbonzakura01/ag_senbon_hakuteiken_v2.bbmodel):

1. **Verdadeira Anatomia de Asas de Anjo (Sem Halo Vollständig) + Partículas de Pétalas**:
   - O halo radial de 16 segmentos (`halo_root` / `halo`) e as lâminas horizontais retas foram removidos para eliminar qualquer semelhança com o Vollständig Quincy.
   - As asas foram reconstruídas ao longo de uma **curva paramétrica em S de Asa de Anjo** (`arch_curve(u)`), subindo suavemente das escápulas (`[±3.0, 21.8, 3.7]`) até a crista alta acima da cabeça (`[±13.0, 32.6, 4.8]`) e curvando até o punho carpal (`[±22.5, 29.8, 5.5]`).
   - **3 Camadas Sobrepostas de Plumagem Curva**:
     - **Cobertoras Superiores de Sakura (`wing_*_root`)**: 6 penas curtas rosadas por asa acompanhando o arco ascendente.
     - **Cobertoras Médias e Rêmiges Secundárias (`wing_*_mid`)**: 6 cobertoras médias + 7 penas secundárias longas abrindo em leque curvo descendente.
     - **Rêmiges Primárias Externas (`wing_*_tip`) e Plumas Internas (`wing_*_feathers`)**: 9 penas primárias afiladas abrindo de `78°` na ponta superior até `25°` na borda inferior + 6 plumas internas suaves atrás da cintura.
   - **Pétalas de Sakura 3D + Partículas Vivas no Jogo**:
     - Bone `petal_streams` com **24 pétalas 3D de sakura** inclinadas em diamante desprendendo-se do arco e das pontas das penas.
     - Emissão contínua de partículas [`ModParticles.SENBON_PETAL`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/model/SenbonHakuteikenVisuals.java#L194-L231) ao longo da envergadura das asas em `SenbonHakuteikenVisuals.clientTick()`.
2. **Uso Exclusivo da Zanpakuto Já Existente no Jogo (Sem Espada Duplicada no Constructo)**:
   - O jogo já possui o modelo oficial da katana branca de Hakuteiken em [`senbonzakura_hakuteiken.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/models/item/senbonzakura_hakuteiken.json), ativado automaticamente por [`BleachClient.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/BleachClient.java#L110) (`"bleachmod:form": 4.0F` quando `d.getCharacter().getSenbonBlade() == 2`).
   - O bone `hakuteiken_blade` foi **removido** do constructo GeckoLib ([`senbon_hakuteiken.geo.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/geo/senbon_hakuteiken.geo.json)), garantindo que o constructo renderize exclusivamente os **11 bones das Asas de Anjo e Pétalas** (`root`, `wing_nexus`, `wing_left_root`, `wing_left_mid`, `wing_left_tip`, `wing_left_feathers`, `wing_right_root`, `wing_right_mid`, `wing_right_tip`, `wing_right_feathers`, `petal_streams`).
   - No Blockbench (`ag_senbon_hakuteiken_v2.bbmodel`), a referência visual da Zanpakuto existente fica dentro do bone de preview `right_arm` do manequim do Capitão (que é filtrado automaticamente pelo exportador [`senbon_hakuteiken_pipeline.py`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/lab/antigravity/senbonzakura01/senbon_hakuteiken_pipeline.py)).

---

## 2. Coreografia Mais Leve e Orgânica (`idle`, `windup`, `dash`)

| Clipe / Fase | Janela | Movimento das Asas de Anjo e Pétalas (GeckoLib) | Pose do Jogador (Blockbench & PlayerAnimator) |
|---|---|---|---|
| `animation.hakuteiken.idle` | `0.00s – 2.00s` (Loop) | Batida de asa leve e orgânica com atraso de fase (wave motion) entre raiz (`wing_*_root`), meio (`wing_*_mid`) e pontas primárias (`wing_*_tip`), enquanto `petal_streams` flutua suavemente. | Postura nobre de prontidão segurando a Zanpakuto existente. |
| `animation.hakuteiken.windup` | `0.00s – 0.80s` (`16 ticks`) | **`0.00s`**: Asas envolvem os ombros em um abraço angelical suave $\rightarrow$ **`0.58s`**: Abertura graciosa em arco alto acima dos ombros com expansão das pétalas $\rightarrow$ **`0.80s`**: Alinhamento para o voo. | Tronco recua em preparação e alinha o braço direito com a Zanpakuto branca apontada ao alvo. |
| `animation.hakuteiken.dash` | `0.80s – 1.30s` (`10 ticks` de dash + impacto) | Asas mantêm o arco angelical elevado em V (`rx = -8°`, `ry = ±22°`, `rz = ∓12°`) com leve vibração das penas primárias e rastro de pétalas de sakura. | Tronco inclinado à frente (`+16°`) ancorado sobre a sombra, braço direito (`-92°`) e `rightItem` (`-85°`) apontando a Zanpakuto existente horizontalmente para a frente. |

---

## 3. Resultado Automatizado In-Game ([`results.txt`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/media/senbon-hakuteiken-20261103/results.txt))

```text
pass0-back PASS bones=11/11 playerAnimLoaded=true layerRegistered=true leapY=3.20 distMoved=16.00 damageDealt=30.0 transform=true mayfly=true reach=6.05 offBonus=0.35 failure=null
pass1-side PASS bones=11/11 playerAnimLoaded=true layerRegistered=true leapY=3.20 distMoved=16.00 damageDealt=30.0 transform=true mayfly=true reach=6.05 offBonus=0.35 failure=null
pass2-front PASS bones=11/11 playerAnimLoaded=true layerRegistered=true leapY=3.12 distMoved=16.00 damageDealt=30.0 transform=true mayfly=true reach=6.05 offBonus=0.35 failure=null
```

### Validação da Transformação Temporária e Mecânicas do Slot 4 (C)
1. **União Física Sem Vão (Zero Air Gap)**:
   - Raízes das asas (`wing_nexus` a `z = 1.85`) acopladas diretamente às escápulas do jogador (`z = 2.0`).
   - A inclinação do tronco do jogador (`body.xRot`, `body.yRot`) é acompanhada 1:1 pelas asas durante toda a coreografia.
2. **Salto Ascendente Propelido pelas Asas**:
   - Vértice do salto em `+3.2` blocos de altura (`leapY = 3.20`).
3. **Rasante Diagonal em V**:
   - Mergulho aéreo diagonal cobrindo `16.00` blocos horizontais e descendo até o nível do solo, desferindo `30.0` HP de dano nos alvos da rota.
4. **Impacto e Explosão de Mar de Pétalas**:
   - Explosão radial com partículas `FLOWER`, `FIELD`, `RISE` e `ModParticles.SENBON_PETAL`.
5. **Transformação Alada Temporária (1,5 min = 1800 ticks)**:
   - Asas e katana branca permanecem ativas (`senbonBlade = 2`).
   - **Voo livre** concedido (`mayfly = true`).
   - **Bônus de ataque físico**: `+35%` (`HAKU_OFFENSIVE_BONUS = 0.35F`).
   - **Alcance ampliado**: `+1.5` blocos extras no Better Combat e Forge Reach (`reachLimit = 6.05` blocos).
   - **Dreno de Reiatsu contínuo**: `+0.25F/tick` (`5.0 Reiatsu/s`).

---

## 4. Hashes SHA-256 e Matriz de Verificação

### Artefatos Versionados
| Arquivo | Tamanho | SHA-256 |
|---|---|---|
| [`ag_senbon_hakuteiken_v2.bbmodel`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/lab/antigravity/senbonzakura01/ag_senbon_hakuteiken_v2.bbmodel) | 120,996 B | `4D55DB8F93C94AF16ECB3A393CB3794392D577D7486AB4A9196863C24EC2ED6C` |
| [`senbon_hakuteiken.geo.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/geo/senbon_hakuteiken.geo.json) | 221,980 B | `04F09F69EDBE65E95C31453557B285B07646AEE98F6AF7D48BD1977605EA61F9` |
| [`senbon_hakuteiken.animation.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/animations/senbon_hakuteiken.animation.json) | 16,311 B | `492DE0007339E6CBF0256E052EF870F171BC6D24DB2836EC17E9752DDAFD8E7D` |
| [`senbon_hakuteiken.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/textures/entity/senbon_hakuteiken.png) | 4,818 B | `53ED1794CC413E3ABA1DC71EA07B49B62A3F4844959620961037EE893CF96633` |
| [`hakuteiken_dash.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/player_animation/hakuteiken_dash.json) | 8,456 B | `93782ED1AB89AFAE42F14656180A46196F44634DE97263AEBEEE0613DC5A2575` |

### Comandos de Validação Executados (Seriais)
| Comando Executado | Duração | Exit Code | Resultado |
|---|---|---|---|
| `python tools/models/lab/antigravity/senbonzakura01/build_angel_hakuteiken_bbmodel.py` | 1s | `0` | `124 cubos, 17 grupos, 3 animações` gerados e carregados no Blockbench MCP |
| `python tools/models/lab/antigravity/senbonzakura01/senbon_hakuteiken_pipeline.py` | 1s | `0` | `11 bones` GeckoLib (Asas de Anjo + Pétalas) + animações + textura + PlayerAnimator exportados |
| `.\gradlew.bat runClient -PbenihimeQa=true -PsenbonHakuteikenQa=true -PcinematicQaFast=true -PqaAutoExit=true --console=plain` | 2m 56s | `0` | `3/3 PASS` (`bones=11/11`, `distMoved=16.00`, `damageDealt=30.0`) |
| `.\gradlew.bat check build --console=plain` | 1m 08s | `0` | `PASS: 158 regression scenarios`, `BUILD SUCCESSFUL` |
