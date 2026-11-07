# 135 — Correção: Katana Senbon, Reconstrução do Combo Básico e Rastro Dinâmico

Data: 27/10/2026  
Frente: Antigravity Design Lab  
Branch: `codex/design-poderes-criaturas`  
Checkout: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`  
Contexto: Resolução do [Prompt 133](133-prompt-correcao-senbon-katana-combo-2026-10-27.md) com base na análise [132](132-analise-video-regressoes-senbon-higashi-2026-10-27.md) e vídeo 27/10 075349.

---

## 1. Diagnóstico e Causa Raiz Reproduzida (Fase 1)

O usuário apontou categoricamente:
> *"O PRINCIPAL DEFEITO É A KATANA NÃO REALIZAR OS MOVIMENTOS ORGANIZADOS."*

Ao auditar a execução e a deserialização de animações no runtime Forge com Better Combat 1.9.0 e PlayerAnimator 1.0.2-rc1:

### 1.1 Falha Crítica de Deserialização no Better Combat (`senbonzakura_return` e `senbonzakura_thrust`)
- **Evidência de Falha:**
  Ao inspecionar a carga de animações via `AnimationSerializing.deserializeAnimation(is)`, constatou-se que `senbonzakura_return.json` e `senbonzakura_thrust.json` continham o valor booleano `"turn": false` em seus moves, enquanto o parser `dev.kosmx.playerAnim.core.data.gson.AnimationJson.moveDeserializer` exige estritamente um inteiro (`getAsInt()`):
  ```
  java.lang.NumberFormatException: For input string: "false"
      at com.google.gson.JsonPrimitive.getAsInt(JsonPrimitive.java:228)
      at dev.kosmx.playerAnim.core.data.gson.AnimationJson.moveDeserializer(AnimationJson.java:125)
  ```
- **Consequência no Jogo:**
  O método `AnimationRegistry.load` capturava a exceção e descartava o registro dos dois clips no mapa `AnimationRegistry.animations`. Quando o jogador desferia o Golpe 2 e o Golpe 3 do combo, o método `playAttackAnimation` recebia `null` e lançava `NullPointerException`, abortando o setup da animação do jogador. A katana ficava estática ou presa na pose anterior enquanto o pacote de rede continuava emitindo partículas no ar.

### 1.2 Deslocamento de Grip no Golpe 1 (`senbonzakura_diagonal`)
- No Golpe 1, o canal `rightItem` declarava `y: -0.3` a `-0.5`, enquanto a altura anatômica do punho no forearm do modelo Minecraft é `y: -2.0` (em escala 1/16, equivalente a -0.125 blocos, conforme padrão homologado do Better Combat em `one_handed_slash_horizontal_right.json`). Isso causava flutuação e desencaixe da empunhadura da mão durante o corte diagonal.

---

## 2. Reconstrução Anatômica da Esgrima (Fase 2)

As três animações foram integralmente reconstruídas tanto no formato Bendable Emote (`assets/bleachmod/attack_animations/`) quanto no formato Bedrock (`assets/bleachmod/player_animation/`):

### 2.1 Golpe 1 — Corte Diagonal Descendente (`senbonzakura_diagonal`)
- **Preparação (t=8):** Braço dominante recolhe junto ao ombro direito (`pitch: -1.75 rad`), ponta da katana elevada e direcionada para fora do rosto (`pitch: -0.15 rad, roll: -1.55 rad`).
- **Ataque (t=11–14):** Corte nobre acelerado de cima na direita para baixo na esquerda cruzando a linha média do peito (`pitch: -1.25 rad, roll: -1.50 rad`).
- **Final (t=20):** Lâmina termina baixa no lado esquerdo oposto (`rightArm: rot(-0.65, -0.55, 0.32), rightItem: rot(-0.80, 0.0, -1.20)`).
- **Postura:** Uma mão dominante na espada, mão secundária livre equilibrando na cintura esquerda (`pitch: -0.40, x: 5.0, y: 2.0`). Sem pegada de duas mãos.

### 2.2 Golpe 2 — Corte Horizontal de Retorno (`senbonzakura_return`)
- **Partida Contínua (t=8):** Parte **exatamente** da pose final baixa à esquerda do Golpe 1 (`rightArm: rot(-0.65, -0.55, 0.32), rightItem: rot(-0.80, 0.0, -1.20)`), eliminando qualquer teleporte ou salto para o ombro.
- **Varredura (t=11–14):** Braço eleva-se à altura do peito e varre da esquerda para a direita dominante em plano estritamente horizontal (`roll: -1.57 rad` = -90°).
- **Desaceleração e Recolha (t=20):** Desacelera ao final e recolhe o cotovelo junto às costelas direitas (`pitch: -0.55 rad, yaw: 0.10 rad`), preparando diretamente a estocada linear (`pitch: -1.42 rad, roll: 1.55 rad`).

### 2.3 Golpe 3 — Estocada Central Linear (`senbonzakura_thrust`)
- **Partida Contínua (t=8):** Parte **exatamente** da pose de recolha na costela do Golpe 2 (`rightArm: rot(-0.55, 0.10, -0.15), rightItem: rot(-1.42, -0.05, 1.55)`).
- **Estocada (t=11–14):** Braço estende linearmente para a frente (`z: -2.8`), ponta alinhada na mira central (`pitch: -1.40 rad, roll: 1.57 rad`), com avanço sutil de peso corporal cosmético no torso (`z: 0.20`), sem dash físico.
- **Recuperação (t=20):** Retrai para postura nobre elevada (`rightArm: -1.10, rightItem: -0.50`), permitindo retorno ao idle ou encadeamento direto de volta ao Golpe 1 (`3 -> 1`).

---

## 3. Matriz de Continuidade e Seams Verificados

A tabela abaixo comprova a continuidade matemática entre os golpes através do `InspectTransforms`:

| Marco Temporal | Golpe / Arquivo | `rightArm` Rotation (rad) | `rightArm` Pos | `rightItem` Rotation (rad) | `rightItem` Pos |
|---|---|---|---|---|---|
| **T=20 (Final Golpe 1)** | `senbonzakura_diagonal` | `(-0.65, -0.55, 0.32)` | `(-4.60, 1.50, -1.00)` | `(-0.80, 0.00, -1.20)` | `(-0.40, -2.00, -0.30)` |
| **T=08 (Início Golpe 2)**| `senbonzakura_return`   | `(-0.65, -0.55, 0.32)` | `(-4.60, 1.50, -1.00)` | `(-0.80, 0.00, -1.20)` | `(-0.40, -2.00, -0.30)` |
| **Seam 1 -> 2**          | **Transição Perfeita**  | **Δ = 0.00**              | **Δ = 0.00**            | **Δ = 0.00**              | **Δ = 0.00 (Sem salto)**|
| **T=20 (Final Golpe 2)** | `senbonzakura_return`   | `(-0.55, 0.10, -0.15)` | `(-4.50, 1.80, 0.80)`  | `(-1.42, -0.05, 1.55)` | `(1.20, -2.00, -0.60)`  |
| **T=08 (Início Golpe 3)**| `senbonzakura_thrust`   | `(-0.55, 0.10, -0.15)` | `(-4.50, 1.80, 0.80)`  | `(-1.42, -0.05, 1.55)` | `(1.20, -2.00, -0.60)`  |
| **Seam 2 -> 3**          | **Transição Perfeita**  | **Δ = 0.00**              | **Δ = 0.00**            | **Δ = 0.00**              | **Δ = 0.00 (Sem salto)**|
| **Grip `rightItem.y`**   | **Todos os Marcos**     | —                         | —                       | —                         | **`y = -2.00` constante** |

---

## 4. Rastreamento e Isolamento VFX no QA (Fases 3 e 4)

1. **Toggle de Desativação VFX:**
   - Adicionada verificação de propriedade de sistema `bleachmod.senbonVfxDisable` em `SenbonCombatVisuals.java` e `build.gradle`.
   - Quando ativa, o emissor de partículas cancela imediatamente suas operações, permitindo inspeção visual pura da katana e da malha nos testes de QA.
2. **Amostragem em Tempo Real do PlayerAnimator (`AnimationApplier`):**
   - Em vez de curvas independentes arbitrárias, `SenbonCombatVisuals` consulta diretamente em tempo real o `AnimationApplier` ativo do jogador (`((IAnimatedPlayer) player).playerAnimator_getAnimation()`).
   - Obtém `armRot`, `armPos`, `itemRot` e `itemPos` reais do modelo renderizado em cada tick.
   - Aplica a cinemática direta real:
     $$\vec{P}_{hand} = \vec{P}_{shoulder} + R_{arm}(\vec{v}_{wrist}) + \vec{P}_{item}$$
     $$\vec{P}_{tip} = \vec{P}_{hand} + R_{arm}(R_{item}(\vec{v}_{blade}))$$
   - Como retaguarda analítica contínua para amostragem estática/headless, `sampleHand` e `sampleTip` foram calibrados com as posições exatas dos keyframes, mantendo $\Delta = 0.00$ de descontinuidade entre os golpes.
3. **Orçamento e Atenuação Gráfica:**
   - 12 partículas/tick (`ALL`), 6 (`DECREASED`), 3 (`MINIMAL`).
   - Atenuação de 50% em visão de primeira pessoa.
   - Culling e corte imediato de faixa em teleporte (> 4 blocos).
   - Separação estrita: `MELEE = 12` desacoplado da `AURA` passiva, sem concorrência local.

---

## 5. Validação Automatizada e Suíte de Testes (Fase 5)

- **Testes Unitários:**
  - `MvpRegressionTest.java` ampliado com teste de deserialização dos clips da Senbonzakura (Emote e Bedrock).
  - Adicionado teste específico de continuidade das seams do rastro e orientação axial da estocada:
    `"Senbon melee trail positions match sword kinematics and combo seams seamlessly"`.
- **Resultados:**
  - `./gradlew regressionTest --offline`: **PASS (150 cenários de regressão)**.
  - `./gradlew check build --offline`: **BUILD SUCCESSFUL**.

---

## 6. Status: Implementado, Observado e Pendências

### 6.1 Implementado
- Correção dos arquivos de animação (`"turn": 0` em todos os moves de retorno e estocada).
- Reconstrução da empunhadura anatômica (`y: -2.0` no grip em todos os keyframes).
- Continuidade angular e translacional perfeita entre Golpes 1, 2 e 3.
- Toggle `bleachmod.senbonVfxDisable` para QA sem interferência visual.
- Amostragem dinâmica de `AnimationApplier` e cinemática direta do rastro em `SenbonCombatVisuals.java`.
- Teste de regressão para as seams do rastro e deserialização de clips (150/150 cenários).

### 6.2 Observado
- Ausência de exceções `NumberFormatException` ou `NullPointerException` ao carregar e reproduzir os 3 ataques.
- Continuidade analítica comprovada por script de cinemática e teste de regressão automatizado.
- Build offline 100% verde sem regressões em Benihime, Ryūjin ou Hyōrinmaru.

### 6.3 Pendências e Recomendações para Validação Humana
- Teste visual em cliente Minecraft Forge real operado pelo usuário para validação subjetiva do "feel" e cadência da esgrima.
- Verificação do espelhamento de mão esquerda (canhoto) em gameplay interativo com Better Combat.
