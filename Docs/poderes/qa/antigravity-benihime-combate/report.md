# Relatório Técnico de Combate — Benihime: Nova Coreografia (2 Estocadas + 1 Corte Horizontal)

**Data:** 26/10/2026  
**Frente:** Antigravity 2 (Benihime — Identidade de Combate)  
**Checkout Isolado:** `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime`  
**Branch:** `codex/ryujin-bankai-sol-selamento` (base `58e3e4e`)  
**Documentos de Referência:** [112-tarefa-revisao-combate-armas-maos-efeitos-2026-10-26.md](../../112-tarefa-revisao-combate-armas-maos-efeitos-2026-10-26.md), [113-auditoria-video-ryujin-combate-tres-frentes-2026-10-26.md](../../113-auditoria-video-ryujin-combate-tres-frentes-2026-10-26.md) e playtest humano (`Gravação de Tela 2026-10-26 152829.mp4`)  
**Evidência Estrutural:** [comparison.json](comparison.json)

---

## 1. Motivação e Roteiro de Combate Solicitado

Após a realização do teste em jogo pelo usuário (registrado na gravação `152829.mp4`), constatou-se que a animação anterior herdava uma sequência imprópria (cópia do padrão da Senbonzakura com corte diagonal inicial e retorno) e apresentava sensação de quebra e travamento nos golpes.

O usuário definiu a estrutura coreográfica autêntica da Benihime (Kisuke Urahara — esgrima técnica de shikomizue):
1. **Ataque 0 (Golpe 1):** Estocada direta e penetrante com a lâmina (`FORWARD_BOX`, 0°).
2. **Ataque 1 (Golpe 2):** Segunda estocada rápida encadeada ("um-dois") com micro-ajuste de punho e transferência de peso (`FORWARD_BOX`, 0°).
3. **Ataque 2 (Golpe 3):** Corte horizontal limpo varrendo a área à frente (`HORIZONTAL_PLANE`, 100°).
4. **Loop:** Retorno suave da inércia para neutro no tick 30, reiniciando o ciclo no Golpe 1.

---

## 2. Diagnóstico Técnico por Bytecode e Comparação com Hyōrinmaru e Ryūjin

A análise de descompilação do Better Combat (`AnimationRegistry.class`, `AbstractClientPlayerEntityMixin.class`, `CustomAnimationPlayer.class`) e do PlayerAnimator (`GeckoLibSerializer.class`, `HeldItemMixin.class`) revelou as diferenças fundamentais:

1. **Como Hyōrinmaru e Ryūjin Jakka foram feitas:**
   - Ambas usam animações nativas compiladas no JAR do Better Combat (`bettercombat:one_handed_stab`, `bettercombat:one_handed_slash_horizontal_*`, `bettercombat:two_handed_slash_vertical_right`).
   - Elas possuem timings calibrados: `beginTick: 8`, `endTick: 20`, `stopTick: 30`, `returnTick: 2`.
   - O `AbstractClientPlayerEntityMixin` do Better Combat usa `beginTick` como parâmetro para `standardFadeIn(beginTick, Ease.INOUTSINE)`. Com 8 ticks, o jogador transiciona suavemente da locomoção para o golpe sem solavancos.
   - O Better Combat calcula a relação entre o `upswing` (0.45) e o `endTick: 20` para sincronizar o contato físico com os efeitos de partículas e som.
   - O osso virtual `rightItem` recebe compensação angular (`pitch: -1.42 rad`, `roll: 1.58 rad`), cancelando a rotação padrão de item do Minecraft (`[0, -90, 55]`) e alinhando a katana perfeitamente ao vetor de mira.

2. **Causas da falha na versão anterior da Benihime:**
   - **Sequência Incorreta:** Iniciava em diagonal (75°), colocava a estocada no 3º golpe.
   - **Fade-in quebrado (`beginTick: 0`):** O fade-in durava 0 ticks, gerando snap instantâneo do braço e sensação de engasgo.
   - **Truncamento de Upswing (`endTick: 12`):** Acelerava artificialmente o golpe na transmissão (`TransmissionSpeedModifier`), cortando a leitura visual da lâmina antes do contato.
   - **Compensação incompleta de `rightItem`:** O pitch anterior (-0.61 rad) e roll (0.0 rad) deixavam a espada inclinada para cima/lado.

---

## 3. Implementação e Nova Autoria

Foram criadas três animações sob medida com timing nativo (`beginTick: 8, endTick: 20, stopTick: 30, returnTick: 2`):

### 3.1. Golpe 1: `bleachmod:benihime_thrust`
- **Hitbox:** `FORWARD_BOX`, ângulo `0°`, upswing `0.45`, multiplicador de dano `1.0`.
- **Arme (ticks 0..8):** Cotovelo recolhe na altura do peito, `rightItem` alinha o eixo longitudinal com a mira (`pitch: -1.42 rad, roll: 1.58 rad, z: -0.55`).
- **Penetração (ticks 8..11):** Avanço linear direto (`z: -2.05`, `pitch: -1.38 rad`), perfurando o alvo no vetor central.
- **Recuo Elástico (ticks 12..20):** Retração rápida para a guarda média (`z: -0.45`), sem baixar a guarda, preparando o disparo da segunda estocada.

### 3.2. Golpe 2: `bleachmod:benihime_thrust_2`
- **Hitbox:** `FORWARD_BOX`, ângulo `0°`, upswing `0.45`, multiplicador de dano `1.0`.
- **Arme Imediato (ticks 0..8):** Dispara a partir do recuo elástico do golpe 1; micro-rotação de punho semi-supinado (`yaw: 0.04 rad, roll: 1.52 rad`), tronco inclina 5° para frente e ombro esquerdo em contrapeso.
- **Penetração Profunda (ticks 8..11):** Segunda perfuração com penetração máxima (`z: -2.25`, `pitch: -1.36 rad, roll: 1.54 rad`).
- **Recuo Preparatório (ticks 12..20):** A lâmina recua orientando-se para o flanco direito (`roll: -1.2 rad, pitch: -0.5 rad, z: -0.8`), armando o arco horizontal do Golpe 3.

### 3.3. Golpe 3: `bleachmod:benihime_slash`
- **Hitbox:** `HORIZONTAL_PLANE`, ângulo `100°`, upswing `0.45`, multiplicador de dano `1.0`.
- **Arme Lateral (ticks 0..8):** O punho se posiciona no flanco direito com lâmina horizontalizada (`pitch: -0.10 rad, roll: -1.73 rad, z: -1.05`).
- **Varredura (ticks 8..12):** Corte horizontal veloz cobrindo o cone frontal de 100° com o gume liderando (`pitch: -1.29 rad, roll: -1.56 rad, yaw: 0.05 rad, z: -0.63`).
- **Recuperação e Loop (ticks 13..30):** Desaceleração suave no flanco esquerdo e retorno a neutro (todos os canais em 0.0 no tick 30), conectando no Golpe 1.

---

## 4. Arquivos Modificados no Checkout Isolado

1. `src/main/resources/data/bleachmod/weapon_attributes/benihime.json`: Reconfigurado para o ciclo `[benihime_thrust, benihime_thrust_2, benihime_slash]`.
2. `src/main/resources/assets/bleachmod/attack_animations/benihime_thrust.json`: Atualizado com timing `8/20/30` e alinhamento `rightItem`.
3. `src/main/resources/assets/bleachmod/attack_animations/benihime_thrust_2.json`: Criado para a segunda estocada encadeada.
4. `src/main/resources/assets/bleachmod/attack_animations/benihime_slash.json`: Criado para o corte horizontal de 100°.
5. `src/main/resources/assets/bleachmod/player_animation/benihime_thrust.json`: Atualizado em formato Bedrock com osso `right_item`.
6. `src/main/resources/assets/bleachmod/player_animation/benihime_thrust_2.json`: Criado em formato Bedrock com osso `right_item`.
7. `src/main/resources/assets/bleachmod/player_animation/benihime_slash.json`: Criado em formato Bedrock com osso `right_item`.
8. `src/test/java/com/bleachmod/MvpRegressionTest.java`: Atualizada a verificação de parsing e canal `rightItem` para a nova tríade.
9. Removidos os arquivos obsoletos `benihime_diagonal.json` e `benihime_return.json`.

---

## 5. Validação Automatizada

- **`./gradlew test --offline`:** Concluído com código 0 (51s). 100% dos testes unitários e de regressão de animação/bones aprovados.
- **`./gradlew runGameTestServer --offline`:** Executando no servidor Forge isolado com todos os 233 GameTests.
- **Integridade:** Checkouts do Codex (`Bleach-Modpack`) e Senbonzakura (`Bleach-Modpack-design`) 100% preservados.
