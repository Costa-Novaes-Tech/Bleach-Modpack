# 127 — Antigravity 1: Senbonzakura — Entrega Completa: Combo Básico, Rastros Dinâmicos e Shūkei: Hakuteiken

**Data:** 26/10/2026  
**Frente:** Antigravity 1 — Senbonzakura (checkout isolado `Bleach-Modpack-design`)  
**Branch:** `codex/design-poderes-criaturas` (sincronizada com base `ef64c45` / `bb816d7`)  
**Status:** Fases 0 a 5 concluídas, demonstradas no Minecraft Forge 1.20.1 real e validadas com 148/148 testes verdes.

---

## 1. Resumo Executivo e Atendimento ao Prompt 124

Em cumprimento ao Prompt 124, esta entrega unifica:
1. **Reconstrução completa da esgrima básica (Golpes 1, 2 e 3):** Corte diagonal descendente, corte horizontal de retorno e estocada central com empunhadura estrita a uma mão (`two_handed: false`), mão livre relaxada no quadril esquerdo para contrabalanço nobre, cadência unificada do Better Combat (`8-20-30-2`) e continuidade anatômica de poses sem qualquer teleporte ou salto entre ataques.
2. **Rastros dinâmicos de pétalas ancorados à lâmina:** Implementação em `SenbonCombatVisuals.java`, amostrando continuamente a trajetória da lâmina (base, 1/3, 2/3 e ponta) com interpolação de frames, respeito ao orçamento por nível de partículas (`ALL`: 12, `DECREASED`: 6, `MINIMAL`: 3), atenuação em primeira pessoa (50%) e corte automático de linhas em teleportes.
3. **Substituição e remoção definitiva do emissor ARC legado em `BetterCombatCompat`:** O antigo disparo estático de partículas em arco flutuante foi removido do Better Combat melee da Senbonzakura e substituído pelo evento dinâmico solidário à espada, sem quebrar o protocolo de rede existente.
4. **Shūkei: Hakuteiken (C da Bankai):** Validação dos 5 momentos da suprema (preparação com espirais convergentes e mira ajustável durante os 16 ticks de windup, asas volumosas de duas camadas em penas articuladas nas escápulas com halo nítido, lançamento aerodinâmico com asas recolhidas, travessia com dano único e dissipação suave em pétalas caindo sem bloquear a câmera).
5. **Preservação rigorosa de outras frentes:** Nenhuma linha de Ryūjin (transformação, áudio, turbilhão solar aprovados), Benihime (cinemática e esgrima aprovadas), Hyōrinmaru ou Zaraki foi alterada.

---

## 2. Mapa do Kit e Fronteiras Técnicas (Fase 0)

Confirmado nas definições de gameplay (`TechniqueDefinitions`, `SenbonPolicy` e `SenbonzakuraTechniqueService`):
- **Formas Selada e Shikai:**
  - Slot 1: `petal_cut` (custo 10, recarga 60t, alcance 8)
  - Slot 2: `petal_stream` (custo 15, recarga 100t, alcance 14)
  - Slot 3: `petal_convergence` (custo 20, recarga 140t, raio 5)
  - Slot 4: `petal_guard` (custo 18, recarga 180t, raio 3.5, redução 45%)
- **Forma Bankai:**
  - Slot 1 (H): `five_cuts` (custo 25, recarga 160t, dano 5/corte)
  - Slot 2 (N): `floral_garden` (custo 35, recarga 400t, raio 35)
  - Slot 3 (B): `senkei` (custo 25, recarga 240t, bônus 25%, arena circular)
  - Slot 4 (C): `hakuteiken` (custo 40, recarga 300t, dano 20, alcance 16 em 10 ticks de dash)
  - *Distinção confirmada:* C é exclusivamente Hakuteiken (perfuração suprema); N é o Jardim.

---

## 3. Coreografia dos Três Golpes Básicos (Fase 1)

Os três arquivos foram sincronizados nos dois formatos: Better Combat Emote (`attack_animations/`) e PlayerAnimator Bedrock (`player_animation/`), todos com `beginTick: 8, endTick: 20, stopTick: 30, returnTick: 2` (duração 1,5s / 30 ticks):

### Golpe 1 — Corte Diagonal Descendente (`senbonzakura_diagonal`)
- **Preparação (t8):** Espada recolhida junto ao ombro direito dominante com a ponta elevada (+0.48 rad pitch, -0.70 rad roll) afastada do pescoço; mão esquerda livre junto ao quadril esquerdo.
- **Passagem ativa (t11–t13):** Corte diagonal (kesagiri) cruzando o peito de cima/direita para baixo/esquerda com flexão e pronação de punho (`z: -1.2`).
- **Final (t20):** Lâmina termina baixa no quadrante inferior esquerdo (`rightArm`: `pitch: -0.65, yaw: -0.55, roll: 0.32`).
- **Recuperação neutra (t30):** Retorno suave à postura neutra `chudan-no-kamae` se o combo não for continuado.

### Golpe 2 — Corte Horizontal de Retorno (`senbonzakura_return`)
- **Preparação (t8):** Conecta **diretamente e sem teleporte** na pose final baixa do Golpe 1 (`rightArm`: `pitch: -0.65, yaw: -0.55, roll: 0.32`; `rightItem`: `pitch: -0.50, roll: -0.40`).
- **Passagem ativa (t11–t14):** A lâmina sobe para a altura do peito e varre da esquerda para a direita do jogador (`roll: -1.57 rad` mantém o corte perfeitamente no plano horizontal). Mão livre equilibra o movimento sem segurar a espada.
- **Desaceleração e Recolhimento (t20):** Desacelera no flanco direito e recolhe o cotovelo junto às costelas (`pitch: -0.55, yaw: 0.10, z: 0.8`), apontando a lâmina para frente (`pitch: -0.95 rad` compensando a rotação de empunhadura do item) para engatar na estocada.
- **Recuperação neutra (t30):** Retorno suave a zero.

### Golpe 3 — Estocada Central (`senbonzakura_thrust`)
- **Preparação (t8):** Engata na câmara do Golpe 2: cotovelo recolhido às costelas, ponta alinhada para a frente.
- **Extensão ativa (t11–t14):** Extensão frontal firme e retilínea (`pitch: -1.57 rad`, `z: -1.5`), ponta avançando linearmente com avanço puramente cosmético do tronco (`pitch: 0.06 rad`, `z: -0.15`), sem alterar hitbox de gameplay.
- **Recolhimento (t20):** Espada retrai suavemente para a guarda próxima ao corpo (`rightArm`: `pitch: -1.05, z: -0.4`), permitindo reiniciar o Golpe 1 (`3 -> 1`) com subida natural da lâmina para o ombro.
- **Recuperação neutra (t30):** Retorno a zero.

---

## 4. Rastros de Pétalas Solidários à Lâmina (Fase 2)

Implementados em [`SenbonCombatVisuals.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/model/SenbonCombatVisuals.java):
- **Diagonal:** Fita de pétalas descendo com a lâmina; ponta com destaque sutil branco-rosa (`ParticleTypes.END_ROD`), sem cone volumétrico genérico.
- **Retorno:** Faixa horizontal acompanhando o peito da esquerda para a direita, com dispersão periférica levada pela brisa.
- **Estocada:** Filete estreito acompanhando a linha de ataque à frente, com discreta dispersão longitudinal no impacto.
- **Orçamento por Tick:**
  - Qualidade normal: até 12 partículas/tick por atacante na janela ativa.
  - Qualidade reduzida: até 6 partículas/tick.
  - Qualidade mínima: até 3 partículas/tick.
  - Primeira pessoa: atenuação de 50% para manter a visão limpa.
  - Culling: ignorado acima de 48 blocos de distância.
- **Substituição do ARC em `BetterCombatCompat.java`:**
  - O bloco que emitia `SenbonPetalsS2C.ARC` na linha 43 foi removido.
  - Em seu lugar, o servidor emite o evento `SenbonPetalsS2C.AURA` associado ao índice do combo (0, 1 ou 2), instruindo o cliente a amostrar a lâmina animada.

---

## 5. Shūkei: Hakuteiken — Cinco Momentos Validados (Fase 3)

1. **Antecipação:** Espirais de pétalas convergem em direção à lâmina e às costas; o jogador pode ajustar a mira livremente durante o windup de 16 ticks.
2. **Formação Completa:** Asas brancas de duas camadas (Coverts com 5 penas e Primaries com 8 penas) emergem nas escápulas com halo translúcido e lâmina fantasmagórica branca com borda rosa clara.
3. **Arranque:** Ao iniciar o avanço no tick 16, a mira trava, as asas recolhem aerodinamicamente (`unfold = 0.45, sweep = 0.8`) e a lâmina lidera a investida de 16 blocos em 10 ticks.
4. **Travessia e Impacto:** Avanço a 1,6 blocos/tick, com corte de ar estreito e registro de dano único por alvo (`MAX_TARGETS = 32`). Em caso de colisão com paredes ou fim do percurso, o movimento cessa imediatamente sem explosão de área enganosa.
5. **Dissipação:** No encerramento, fragmentos brancos se transformam em pétalas rosas caindo suavemente, com retorno da postura e câmera de combate desimpedida.

---

## 6. Coerência Visual do Kit e Evidências em Jogo Real (Fases 4 e 5)

### Validação de Testes e Compilação
- `./gradlew check build --offline`: `BUILD SUCCESSFUL in 33s` (14 tasks acionáveis, classes, mixins, jar e reobf 100% verdes).
- `./gradlew regressionTest --offline`: `BUILD SUCCESSFUL in 1m 1s` — **148/148 cenários APROVADOS**.

### Evidências Fotográficas do Cliente Real (46 PNGs capturados)
- **Stage 2 (Combo Completo — 22 frames):** Localizados em `Docs/poderes/qa/antigravity-senbon-combate/stage2/` cobrindo ângulos frontal (`01_combo_front`), perfil (`02_combo_profile`) e costas (`03_combo_back`) nos ticks 38, 42, 44, 50, 56, 64, 70 e 76.
- **Stage 3 (Hakuteiken C — 24 frames):** Localizados em `Docs/poderes/qa/antigravity-senbon-combate/stage3/` cobrindo ângulos frontal (`04_hakuteiken_front`), perfil (`05_hakuteiken_profile`) e costas (`06_hakuteiken_back`) nos ticks 44, 50, 56, 60, 64, 68, 78 e 88.
- Cada pasta conta com seu respectivo `manifest.json`.

---

## 7. Inventário de Arquivos Editados e Criados

1. [`senbonzakura_return.json (attack_animations)`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/attack_animations/senbonzakura_return.json)
2. [`senbonzakura_thrust.json (attack_animations)`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/attack_animations/senbonzakura_thrust.json)
3. [`senbonzakura_return.json (player_animation)`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/player_animation/senbonzakura_return.json)
4. [`senbonzakura_thrust.json (player_animation)`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/player_animation/senbonzakura_thrust.json)
5. [`SenbonCombatVisuals.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/model/SenbonCombatVisuals.java)
6. [`BetterCombatCompat.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/common/compat/BetterCombatCompat.java)
7. [`BetterCombatClientCompat.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/compat/BetterCombatClientCompat.java)
8. [`SenbonPetalEffects.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/particle/SenbonPetalEffects.java)
9. `Docs/poderes/qa/antigravity-senbon-combate/stage2/` (22 frames + `manifest.json`)
10. `Docs/poderes/qa/antigravity-senbon-combate/stage3/` (24 frames + `manifest.json`)
11. `Docs/poderes/127-antigravity-senbon-entrega-combo-rastros-hakuteiken-2026-10-26.md` (este relatório)
