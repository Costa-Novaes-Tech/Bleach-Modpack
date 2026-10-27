# 135 — Correção: Katana Senbon e Combo Básico Reconstruído

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
2. **Harmonização do Rastro de Pétalas:**
   - Trajetória dos pontos da lâmina em `SenbonCombatVisuals` (base, 1/3, 2/3, ponta) alinhada rigorosamente aos novos planos anatômicos:
     - Golpe 1: de ombro direito alto cruzando para peito baixo esquerdo.
     - Golpe 2: varredura horizontal de peito esquerdo para direito dominante.
     - Golpe 3: estocada linear axial mirando para a frente.
   - Orçamento e atenuação preservados: 12 partículas (`ALL`), 6 (`DECREASED`), 3 (`MINIMAL`), redução de 50% em primeira pessoa, corte imediato em caso de teleporte > 4 blocos.

---

## 5. Validação Automatizada e Suíte de Testes (Fase 5)

- **Teste Unitário Ampliado:**
  Atualizado `src/test/java/com/bleachmod/MvpRegressionTest.java` para incluir as 3 animações de Senbonzakura no teste de integridade PlayerAnimator (`AnimationSerializing.deserializeAnimation` em `attack_animations` e `player_animation`).
- **Resultado dos Testes:**
  - `./gradlew regressionTest --offline`: **PASS (149 cenários de regressão)**.
  - Zero falhas de serialização ou regressão em Benihime, Ryūjin ou Hyōrinmaru.
