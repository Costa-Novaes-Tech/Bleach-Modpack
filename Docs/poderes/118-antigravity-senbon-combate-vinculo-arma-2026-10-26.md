# 118 — Antigravity 1: Senbonzakura — Combate Básico Próprio e Vínculo Arma/Punho

**Data:** 26/10/2026  
**Frente:** Antigravity 1 — Senbonzakura (checkout isolado `Bleach-Modpack-design`)  
**Branch:** `codex/design-poderes-criaturas` (base técnica `58e3e4e`)  
**Status da Entrega:** Implementado, testado e validado em runtime real. Pronto para revisão e playtest. Sem commit/push/merge por inferência.

---

## 1. Diagnóstico e Causa Raiz Comprovada

Na auditoria da gravação do usuário (00:43–00:45 do vídeo analisado no doc 113), observou-se que a lâmina do Senbonzakura mudava de orientação, mas a trajetória da espada e os efeitos não possuíam leitura coesa com os braços, aparentando travamento vertical ou deslocamento em relação ao punho.

A inspeção detalhada do bytecode do **PlayerAnimator 1.0.2** (`dev.kosmx.playerAnim.mixin.HeldItemMixin`) e do **Better Combat 1.9.0** (`net.bettercombat.client.animation.AttackAnimationSubStack` e `PoseSubStack`) revelou a causa concreta:

1. **Injeção do Item pelo PlayerAnimator:**
   O `HeldItemMixin` calcula dinamicamente a matriz da mão direita a partir de:
   ```java
   Vec3f rot = animationProcessor.get3DTransform("rightItem", TransformType.ROTATION, Vec3f.ZERO);
   Vec3f pos = animationProcessor.get3DTransform("rightItem", TransformType.POSITION, Vec3f.ZERO).scale(0.0625f);
   poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
   poseStack.mulPose(Axis.ZP.rotation(rot.getZ()));
   poseStack.mulPose(Axis.YP.rotation(rot.getY()));
   poseStack.mulPose(Axis.XP.rotation(rot.getX()));
   ```
2. **Defeito nos Assets Customizados:**
   Diferente das animações nativas de espada do Better Combat (como `one_handed_slash_horizontal_right` e `two_handed_slash_vertical_right`, que possuem até 12 keyframes no canal `rightItem`), os assets de Senbonzakura possuíam apenas movimentação do `rightArm`.
3. **Consequência Biomecânica:**
   O Minecraft não possui articulação de punho; o braço humanoide é um osso rígido. Sem rotação de `rightItem`, a espada mantinha os ângulos fixos do modelo JSON (`senbonzakura.json` herdando `rotation: [0, -90, 55]`). Quando o braço subia no windup (pitch de -140°), a lâmina apontava rigidamente para trás/baixo atravessando o ombro; durante a descida e no follow-through, o gume não liderava o corte e a empunhadura deslizava pela mão.

---

## 2. Metodologia de Implementação

Seguindo rigorosamente o roteiro mandatário:
1. **Prova Vertical do Golpe 1 (Corte Diagonal):**
   - Construção dos 4 marcos biomecânicos com canais sincronizados de `rightArm` e `rightItem`:
     - **Preparação contida (ticks 0–4):** Punho em extensão (`pitch: +0.52 rad`, `roll: -0.70 rad`, hilt em `x: 0.2, y: -0.5, z: 0.3`). A lâmina repousa elevada e inclinada ao longo da linha de entrada, com o tsuka perfeitamente aninhado na palma e tsuba encostada no nó dos dedos.
     - **Passagem do corte (ticks 6–8):** Pronação e flexão ativa do punho (`pitch: -0.60 rad`, `roll: -0.52 rad`, avanço linear `z: -1.2`). O gume lidera a varredura diagonal (kesagiri) sem oscilações laterais.
     - **Impacto/Contato (ticks 8–10, upswing 0.40):** Desaceleração sólida na extensão média (`pitch: -0.75 rad`, `roll: -0.38 rad`, `z: -1.5`).
     - **Recuperação elegante (ticks 12–20):** Desaceleração contínua e retorno à postura neutra (chudan-no-kamae) alcançando `0.0, 0.0, 0.0` no tick 20 sem solavancos, trancos ou offset residual.
2. **Expansão para os Golpes 2 e 3:**
   - **Golpe 2 — Corte Horizontal de Retorno (`senbonzakura_return`):**
     - Transição natural partindo do fim do corte diagonal na esquerda para varrer horizontalmente da esquerda para a direita.
     - Banco do punho com `roll: -1.57 rad` (-90°), alinhando o gume estritamente com o plano horizontal do Better Combat (`HORIZONTAL_PLANE`, 85°).
     - Rastro e retorno contínuos à guarda.
   - **Golpe 3 — Estocada Precisa (`senbonzakura_thrust`):**
     - Recuo preparatório do cotovelo junto às costelas direitas com ponta alinhada ao centro.
     - Disparo linear penetrante (`pitch: -0.96 rad`, neutralizando a inclinação nativa de 55° para projetar a lâmina perfeitamente horizontal e reta ao longo da mira, `z: -4.0` no braço e `z: -2.5` no punho).
     - Retração imediata e retorno ao repouso.
3. **Sincronização Dupla dos Formatos de Asset:**
   - `attack_animations/*.json`: Formato Emote PlayerAnimator consumido pelo Better Combat (`AnimationRegistry`).
   - `player_animation/*.json`: Formato Bedrock/GeckoLib consumido pela API de animações de jogador (`PlayerAnimationRegistry`).
   - Ambos mantêm rigorosamente os mesmos ângulos, curvas e tempos de execução.

---

## 3. Arquivos Alterados (Fronteira Autorizada)

| Arquivo | Modificação |
|---|---|
| `src/main/resources/assets/bleachmod/attack_animations/senbonzakura_diagonal.json` | Adicionado canal `rightItem` (9 keyframes) e ajustado `rightArm`/`torso`/`head` para corte diagonal nobre. |
| `src/main/resources/assets/bleachmod/attack_animations/senbonzakura_return.json` | Adicionado canal `rightItem` (9 keyframes) com banco horizontal de 90° e trajetória de retorno elegante. |
| `src/main/resources/assets/bleachmod/attack_animations/senbonzakura_thrust.json` | Adicionado canal `rightItem` (9 keyframes) com compensação de 55° para estocada linear perfeita. |
| `src/main/resources/assets/bleachmod/player_animation/senbonzakura_diagonal.json` | Sincronizado formato Bedrock com os mesmos keyframes e curvas. |
| `src/main/resources/assets/bleachmod/player_animation/senbonzakura_return.json` | Sincronizado formato Bedrock com os mesmos keyframes e curvas. |
| `src/main/resources/assets/bleachmod/player_animation/senbonzakura_thrust.json` | Sincronizado formato Bedrock com os mesmos keyframes e curvas. |
| `Docs/poderes/qa/antigravity-senbon-combate/` | Diretório de evidências contendo os frames capturados em alta resolução e o manifesto `manifest.json`. |

*Nota:* `src/main/resources/data/bleachmod/weapon_attributes/senbonzakura.json` já continha a configuração aprovada de atributos (alcance 3.0, multiplicador 1.0, 3 ataques próprios) e foi preservado intacto.

---

## 4. Evidências de Validação Técnica

### A. Regressões Globais (`./gradlew regressionTest --offline`)
- **Comando:** `./gradlew regressionTest --offline`
- **Resultado:** `BUILD SUCCESSFUL in 19s`
- **Cenários:** **147/147 cenários APROVADOS**.
- **Comprovações:**
  - `Benihime Better Combat animations parse as finite original fencing motions` (validação de integridade dos JSONs via `AnimationSerializing`).
  - Contratos de stamina e reiatsu 100% íntegros (sem débito duplicado em golpes normais).
  - Isolamento de cooldowns e ausência de resíduos no estado de combate do jogador.

### B. GameTests do Servidor Minecraft (`./gradlew runGameTestServer --offline`)
- **Comando:** `./gradlew runGameTestServer --offline`
- **Resultado:** `BUILD SUCCESSFUL in 4m 17s`
- **Cenários:** **224/224 GameTests APROVADOS**.
- **Comprovações:**
  - `BetterCombatGameTests.senbonReachBonusIsSingleAndCannotLeakToAnotherWeaponOrForm`: Garante alcance único de 3.0 blocos e impede acúmulo duplicado de alcance na Bankai.
  - Ataques utilizam estritamente o prefixo `bleachmod:senbonzakura_` e mantêm o multiplicador de dano 1.0.
  - Todas as suítes de habilidades (Jardim de Pétalas, Hakuteiken, Ikka Senjinka, etc.) mantêm seus contratos autoritativos.

### C. Captura Automatizada em Cliente Forge Real (`runClient`)
- **Comando:** `./gradlew runClient -PbenihimeQa=true -PsenbonCombatQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline`
- **Resultado:** `BUILD SUCCESSFUL in 1m 52s`
- **Execução:** Todos os 6 cenários do `SenbonzakuraCombatQaSequence` executados contra Husk alvo em mundo criativo controlado, registrando 44 frames indexados:
  - `01_combo_front` (ticks 38 a 76)
  - `02_combo_profile` (ticks 32 a 76 — visão lateral evidenciando alinhamento da guarda e ponta)
  - `03_combo_back` (ticks 32 a 76 — visão traseira evidenciando ausência de colisão com ombro/pescoço)
  - `04_hakuteiken_front`, `05_hakuteiken_profile`, `06_hakuteiken_back` (ticks 44 a 88 — asas volumétricas, mira dinâmica, lançamento aerodinâmico e dissipação)

---

## 5. Manifesto de Frames e Inspeção Visual

As imagens completas foram salvas e indexadas em `Docs/poderes/qa/antigravity-senbon-combate/manifest.json`.

Principais marcos comprovados:
1. **Golpe 1 — Preparação (Tick 32):**
   - Frontal/Costas: `senbon_strike1_back_prep.png`
   - Perfil: `senbon_strike1_profile_prep.png`
   - *Observação:* A tsuka permanece aninhada na mão; a lâmina sobe com o gume apontado para o quadrante diagonal superior sem cruzar o corpo de forma bizarra.
2. **Golpe 1 — Passagem e Contato (Tick 38):**
   - Frontal/Costas: `senbon_strike1_back_contact.png`
   - Perfil: `senbon_strike1_profile_contact.png`
   - *Observação:* A ponta da espada avança no plano de corte; o punho flexiona acompanhando a lâmina sem folga.
3. **Golpe 2 — Retorno Horizontal (Ticks 46–52):**
   - Visível em `02_combo_profile_frame-t046.png` e `02_combo_profile_frame-t052.png`.
   - *Observação:* A lâmina gira 90° e corta na horizontal no quadrante central.
4. **Golpe 3 — Estocada Linear (Ticks 60–68):**
   - Visível em `02_combo_profile_frame-t060.png` e `02_combo_profile_frame-t068.png`.
   - *Observação:* A espada forma uma linha perfeitamente reta da ponta ao cotovelo, eliminando o ângulo padrão de 55°.
5. **Recuperação Neutra (Tick 76):**
   - Visível em `senbon_strike1_profile_recovery.png` e `senbon_strike1_back_recovery.png`.
   - *Observação:* Postura chudan-no-kamae restabelecida sem offsets residuais.

---

## 6. Limitações e Próximos Passos

1. **Aprovação Artística do Usuário:** O build e os testes automatizados comprovam o funcionamento estrutural e a correção do vínculo mão/arma; a aprovação estética subjetiva da cadência e da fluidez depende do playtest humano.
2. **Sem Git Commit/Push/Merge:** A alteração permanece isolada na branch `codex/design-poderes-criaturas` no checkout de design, respeitando a coordenação de frentes.
