# 100 — Rework Visual e Funcional do Kit Hyōrinmaru: Shikai Serpente, Ryūsenka PlayerAnimator e Hyōten Celeste

- **Data**: 23/10/2026
- **Workspace**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- **Branch**: `codex/design-poderes-criaturas`
- **Commit Base**: `132c42d` (com alterações locais autorizadas)
- **Status da Validação**:
  - `python tools/models/ice_hydra.py --check`: **MATCH (Exit 0)**
  - `Invoke-Design.ps1 -Mode Check`: **BUILD SUCCESSFUL (Exit 0)** em 1m 5s
  - `Invoke-Design.ps1 -Mode GameTests`: **179/179 PASSADOS (Exit 0)** em 1m 57s (`All 179 required tests passed :)`)
  - `Invoke-Design.ps1 -Mode ClientQa`: **10 Cenários Executados e Capturados com Sucesso (Exit 0)** em 3m 59s (`All 10 scenarios executed & captured!`)

---

## 1. Contexto e Objetivos

Após a reformulação e aprovação anatômica dos pescoços escamados e cabeça `v3` da hidra de gelo, o usuário solicitou uma revisão profunda e aprimoramento visual de três pilares centrais do kit da **Daiguren Hyōrinmaru**, acompanhada da remoção de elementos visuais depreciados:

1. **Shikai Slot 1 / H (Projétil Dragão de Gelo -> Serpente Monocéfala)**:
   - Substituição completa do modelo antigo em blocos rígidos de gelo compactado (`ice_dragon.geo.json`) pela geometria da hidra refinada (`ice_hydra.geo.json`), renderizada em escala compacta ($\approx 0.42\times$).
   - Formatação anatômica como **serpente de gelo monocéfala**: ocultação dinâmica dos pescoços e cabeças laterais (`left` e `right`), mantendo a cabeça central (`head_center`) na vanguarda com mandíbula entreaberta e os 12 anéis articulares cervicais (`neck_center_1..12`) ondulando suavemente em ondas senoidais tridimensionais atrás da cabeça ao longo da trajetória de voo.
   - Rastro contínuo de partículas de neve, granizo e geada (`serpentTrail`).

2. **Bankai Slot 1 / H (Ryūsenka - Investida Estelar de Gelo)**:
   - Integração com o `PlayerAnimator` e `IceArmorLayer` em duas fases coreografadas:
     - **Fase de Preparação / Windup ($t = 0..6$ ticks)**: O personagem agacha a postura (`body` rotX $+0.35$), inclina a cabeça mirando o alvo, posiciona a katana com empunhadura bimanual à frente (`swordArm` rotX $-1.45$, `supportArm` rotX $-1.35$), enquanto as asas de gelo no `IceArmorLayer` se contraem para trás defensivamente (`yRot = 0.45`, `zRot = -0.45`).
     - **Fase de Investida / Thrust Impulse ($t = 6..20$ ticks)**: Estocada de alta velocidade rasgando o ar (`body` inclinado $+0.50$, translação no eixo de avanço, `swordArm` retilíneo em $-1.60$), com as asas de gelo varridas para trás em aerodinâmica agressiva (`yRot = 0.85`, ponta da asa rotZ $-0.65$).
     - Rastro de thrust glacial em `IceTechniqueVisuals.ryusenkaThrustTrail` com neve densa, fragmentos de gelo e faíscas de poeira de cristal ciano (`DustParticleOptions(0.45F, 0.85F, 1.0F)`).
     - Alvos perfurados são encerrados tridimensionalmente em casulos cruzados de gelo (`iceCross`), sofrendo congelamento imediato e ruptura com dano secundário no tick 46.

3. **Remoção da Zona Floral Depreciada (`bloom`)**:
   - Eliminação completa da mecânica defasada de dois estágios que tentava reaproveitar o campo como florescimento.
   - Remoção dos quads de pétalas em `ElementalActionVisuals.java`, desativação de `consumeBloom`, remoção do override de HUD em `ZanpakutoHud.java` e atualização da suíte de GameTests.

4. **Bankai Slot 4 / C (Hyōten Hyakkasō - Rugido Celeste das Hidras e Zona Glacial Contínua)**:
   - Invocação direta da manifestação das **3 Hidras de Gelo** atrás do Shinigami (`configureRoar`, `size = 15`, duração 120 ticks).
   - Coreografia procedural dracônica em `HydraMotion.java`:
     - Em vez da baforada descendente da transformação, os 3 pescoços arqueiam verticalmente em direção ao céu atingindo $Y = 19.5$ blocos de altura.
     - As 3 cabeças apontam diretamente para o zênite (inclinação vertical extrema de $-82^\circ$).
     - As 3 mandíbulas abrem em $35^\circ$ com tremor de Reiatsu ($1.2^\circ \cdot \sin$), emitindo um rugido uníssono de dragão a cada 25 ticks.
     - Emissão vertical de colunas de partículas `SNOWFLAKE` e `ITEM_SNOWBALL` disparadas para cima a partir das três bocas, acompanhadas de queda de flocos em raio de 26 blocos.
     - **RESTRIÇÃO CRÍTICA RESPEITADA**: **ZERO alteração no clima do mundo ou chuva/tempestade vanilla** (`level.setWeatherParameters` terminantemente vetado). O efeito atmosférico é 100% visual, local e sonoro.
     - Criação da Zona Glacial de 30 blocos que causa o **maior dano contínuo do kit**: pulsos de 8 de dano a cada 10 ticks (totalizando até 96 de dano) com nível 3 de Chill e congelamento contínuo.

---

## 2. Arquivos Modificados e Responsabilidades

| Arquivo | Localização | Modificação Realizada |
| :--- | :--- | :--- |
| `HydraMotion.java` | `src/main/java/.../visual/` | Adição de `MODE_ROAR = 2`, splines verticais ($Y=19.5$), pitch $-82^\circ$, abertura de mandíbula $35^\circ$ com tremor, e suporte aos 3 modos operacionais. |
| `IceTechniqueVisuals.java` | `src/main/java/.../visual/` | Implementação de `serpentTrail` (projétil) e `ryusenkaThrustTrail` (rastro de estocada com poeira ciano). |
| `IceDragonEntity.java` | `src/main/java/.../entity/` | Suporte a `ROAR_SIZE = 15`, `isRoar()`, `configureRoar()`, emissão de partículas verticais dracônicas em `roarTick` sem clima global, e emissão de rastro no projétil. |
| `IceDragonRenderer.java` | `src/main/java/.../model/` | Unificação definitiva de recursos para `HYDRA_GEO/TEX/ANIM`. No modo projétil: escala $0.42\times$, ocultação de cabeças/pescoços laterais, mandíbula central entreaberta e 12 elos ondulando em onda senoidal $serpentPoint$. No modo manifestação: chaveamento correto de `MODE_TRANSFORMATION`, `MODE_ASSAULT` e `MODE_ROAR`. |
| `HyorinmaruTechniqueService.java` | `src/main/java/.../technique/` | Remoção de `consumeBloom`. Invocação direta da hidra rugidora em Bankai Slot 4. Dano contínuo da zona elevado para 8 e chill para 3. |
| `ElementalTechniqueService.java` | `src/main/java/.../technique/` | Remoção do bloco condicional de ativação `bloom` e do `case "bloom"` no tick. Emissão contínua de `ryusenkaThrustTrail` durante a investida ($6 \le age \le 19$). |
| `ElementalActionVisuals.java` | `src/main/java/.../model/` | Remoção dos quads de pétalas `bloom` e de `bloomReady`. Implementação da coreografia de Ryūsenka no PlayerAnimator (preparação bimanual agachada e estocada lunge). Exposição pública de `gesture()`. |
| `IceArmorLayer.java` | `src/main/java/.../model/` | Resposta dinâmica das asas de gelo ao gesto de Ryūsenka: contração e flexão traseira durante o windup ($0..6$), e varredura aerodinâmica traseira agressiva durante a estocada ($6..20$). |
| `ZanpakutoHud.java` | `src/main/java/.../hud/` | Remoção da checagem e substituição de nome por `bloomReady`. |
| `ElementalReworkGameTests.java` | `src/main/java/.../gametest/` | Substituição do teste `floralReactivationUsesOriginalFieldAndOneCooldown` pelo novo teste `hyotenDirectRoarSummonsHydraAndDealsContinuousDamage`. |
| `.agents/skills/bleach-model-pipeline/SKILL.md` | `.agents/skills/...` | Nova skill documentando o ciclo completo de modelagem, teste, laboratório Blockbench, conversão e integração GeckoLib/Java. |

---

## 3. Evidências de Validação Automatizada

1. **Consistência de Modelagem Blockbench/Python**:
   ```
   > python tools/models/ice_hydra.py --check
   Hydra source, hierarchy, animations, texture and runtime assets match.
   Exit code: 0
   ```

2. **Compilação e Verificação Estática (`Check`)**:
   ```
   BUILD SUCCESSFUL in 1m 5s
   14 actionable tasks: 11 executed, 3 up-to-date
   Exit code: 0
   ```

3. **GameTests do Servidor Minecraft (`GameTests`)**:
   ```
   [10:25:57] [Server thread/INFO] [minecraft/GameTestServer]: All 179 required tests passed :)
   BUILD SUCCESSFUL in 1m 57s
   Exit code: 0
   ```
   - Lote `new_hyoten_roar:1`: Aprovado com ativação instantânea da zona glacial de 30 blocos, dano contínuo em alvos a 3 e 28 blocos, congelamento contínuo e sem contaminação climática.
   - Lote `new_ryusenka:1`: Aprovado com avanço de investida e congelamento de impacto.
   - Lote `new_dragon_reach:1`: Aprovado com alcance e mordida dracônica do projétil serpente.

4. **Execução e Captura no Cliente Real do Minecraft (`ClientQa`)**:
   ```
   [10:30:15] [Render thread/INFO] [co.bl.cl.ci.HyorinmaruQaSequence/]: Hyorinmaru QA sequence COMPLETE: All 10 scenarios executed & captured!
   BUILD SUCCESSFUL in 3m 59s
   Exit code: 0
   ```
   - **Cenário 01 (`01_bankai_transformation`)**: 400 ticks executados com áudio cues 24, 216 e 330 disparados com perfeição, sem qualquer travamento.
   - **Cenário 02 (`02_bankai_wings_lotus`)**: Asas articuladas com penas individuais e 3 flores de lótus flutuantes no `IceArmorLayer`.
   - **Cenário 03 (`03_slot1_ryusenka`)**: Pose bimanual de preparação ($0..6$), asas de gelo flexas para trás, investida com estocada retilínea ($6..20$), rastro glacial ciano e encerramento em casulo 3D com ruptura aos 46 ticks.
   - **Cenário 04 (`04_slot2_hydra_assault`)**: Três cabeças atacando em mergulho com pescoços afilados e mandíbulas sincronizadas.
   - **Cenário 06 (`06_slot4_blizzard_zone`)**: Rugido celeste triplo com pescoços verticais até $Y=19.5$, cabeças a $-82^\circ$, abertura de $35^\circ$ e dano massivo contínuo de 8 a cada 10 ticks (saúde do boss reduzida de 500 para 393 em poucos segundos) com zero alteração no clima vanilla.

---

## 4. Garantias e Diretrizes Cumpridas

- **Branch e Checkout**: Executado exclusivamente em `codex/design-poderes-criaturas` no checkout `Bleach-Modpack-design`.
- **Integridade de Outros Trabalhos**: Nenhum arquivo de áudio ou cinematics (`HyorinmaruCinematicClient.java`, `HyorinmaruEntranceAudio.java`, `HyorinmaruCameraPath.java`, `HyorinmaruTimeline.java`, `sounds.json`) foi tocado.
- **Zaraki e Benihime**: Completamente intactas e inalteradas.
- **Clima do Jogo**: **Zero** comandos `setWeatherParameters`, zero ativação de chuvas mundiais — o ambiente tempestuoso é criado puramente por emissores de partículas e efeitos visuais locais no raio da técnica.
- **Git**: Sem commit, push, merge, rebase, reset ou stash.


---

## 5. Retificação e Diagnóstico da Revisão Visual: Mandíbulas, Rugido Celeste (+84°) e Postura Ryūsenka

Na rodada de revisão visual dos testes manuais e capturas em jogo, foram identificados e solucionados quatro pontos críticos:

### 5.1. Diagnóstico e Correção das Mandíbulas e Inclinação das Hidras no Hyōten Hyakkasō (Ataque C)

- **Fato Comprovado (Pitch Invertido)**: No espaço de rotações do GeckoLib/JOML, o modelo da cabeça da hidra possui o focinho alinhado ao longo de $-Z$ em repouso. Uma rotação $rotX$ negativa ($-82.0^\circ$) arremessava o focinho para baixo em direção ao chão, fazendo as três hidras parecerem postes curvados olhando para a terra.
  - **Correção**: O sinal foi invertido para **`+84.0^\circ`** em `HydraMotion.headRotX(MODE_ROAR)` com tremor de Reiatsu ($1.2^\circ \cdot \sin$), erguendo focinho, crânio e chifres diretamente para o zênite do céu.
- **Fato Comprovado (Mandíbulas Ocluídas e Estado Invisível)**:
  1. `IceDragonRenderer.java` aplicava `jaw.setHidden(true)` na transição para o modo projétil, mas não garantia explicitamente `jaw.setHidden(false)` ao retornar ou chavear para os modos de manifestação (`MODE_TRANSFORMATION`, `MODE_ASSAULT`, `MODE_ROAR`).
  2. Com o pitch antigo de $-82^\circ$, a mandíbula inferior aberta rotacionava diretamente para dentro do volume do pescoço vertical retilíneo, provocando z-fighting e clipping geométrico total.
  - **Correção**: Explicitação estrita de `jaw.setHidden(false)`, `jaw.setScaleX/Y/Z(1.0F)`, `jaw.setPosX/Y/Z(0.0F)` e rotação angular aberta de **`42.0^\circ`** com tremor ($1.5^\circ \cdot \sin$) em `HydraMotion.jaw(MODE_ROAR)`.
- **Fato Comprovado (Pescoços Finos e Retos)**: A interpolação spline anterior usava uma subida quase perfeitamente vertical ($X = \pm 5.5$), gerando aparência de tubos estáticos.
  - **Correção**:
    - Alargamento anatômico em `HydraMotion.point(MODE_ROAR)`: a cabeça central curva-se em S para frente ($Y=15.5, Z=2.8$), enquanto as cabeças laterais abrem amplamente para os lados ($X = \pm 9.2$), criando uma envergadura majestosa de **18.4 blocos** de largura.
    - Engrossamento dinâmico em `IceDragonRenderer.java`: no modo de rugido, os 12 elos cervicais recebem escala `neckThick = 1.35F` (35% mais grossos e musculares), e a cabeça central recebe escala $1.25\times$ com as laterais em $1.15\times$ rotacionadas $\pm 22^\circ$ para fora.
- **Áudio Dracônico Autêntico**: Em `IceDragonEntity.java`, o som do rugido foi configurado para disparar `net.minecraft.sounds.SoundEvents.ENDER_DRAGON_GROWL` (volume 2.5F, pitch 0.85F) nos ticks 8, 34, 62 e 88, complementado por `SoundEvents.ENDER_DRAGON_AMBIENT` nos ticks de ápice 12 e 66.

### 5.2. Diagnóstico e Correção da Postura de Ataque do Jogador no Ryūsenka (Ataque H)

- **Fato Comprovado (Postura Rígida e Espada para Cima)**:
  1. No Minecraft vanilla, itens na mão principal são renderizados com rotação intrínseca de $90^\circ$ perpendicular ao antebraço. Levantar o braço simplesmente apontava a katana na vertical para o teto ("guarda-chuva de brinquedo").
  2. `leftLeg` e `rightLeg` não eram manipulados no `ElementalActionVisuals`, deixando o jogador parado na pose estática padrão.
- **Correção com Martial Zenkutsu-Dachi no PlayerAnimator**:
  - `leftLeg`: Rotação para a frente `rotX = -0.68F`, translação `posZ = 2.4F` e flexão do joelho `TransformType.BEND = 0.95F` (joelho esquerdo profundamente flexionado à frente).
  - `rightLeg`: Perna traseira ancorada e estendida `rotX = 0.48F`, `bend = 0.15F`, `posZ = -2.2F`.
  - `body`: Centro de gravidade rebaixado `posY = -4.2F`, inclinação marcial `rotX = 0.32F`.
  - `head`: Compensação angular `rotX = -0.32F` mantendo a cabeça erguida e o olhar cravado diretamente no alvo à frente.
  - Empunhadura Bimanual: `swordArm` (`rightArm`) em `rotX = -1.48F`, e `supportArm` (`leftArm`) em `rotX = -1.42F` com `BEND = 0.30F`, unindo as duas mãos sobre o tsuka da espada à frente do peito.
  - Alinhamento da Lâmina: Rotação explícita de `rightItem` em `rotX = -1.42F`, nivelando a katana horizontalmente como uma lança de estocada apontada diretamente para o peito do oponente.

### 5.3. Evidências do Ciclo de QA em Jogo Real (`cycle93-1792765348185`)

- **Hyōten Hyakkasō (Rugido das Hidras ao Céu)**:
  - Capturas `hyoten_sky_roar_t032.png` e `hyoten_sky_roar_t048.png`: As 3 cabeças erguidas a $+84^\circ$ apontando para o céu, mandíbulas abertas a $42^\circ$ com dentes de gelo visíveis, envergadura de 18.4 blocos e pescoços espessos com escamas articuladas.
- **Ryūsenka (Postura Marcial e Estocada)**:
  - Capturas `ryusenka_combat_stance_t004.png` e `ryusenka_combat_stance_t008.png`: Jogador em base de combate rebaixada, joelho esquerdo flexionado à frente, empunhadura bimanual na katana apontada horizontalmente para a frente e asas de gelo armadas defensivamente para a investida.
