# Relatório Técnico de Combate — Benihime: Vínculo de Arma, Mão e Esgrima

**Data:** 26/10/2026  
**Frente:** Antigravity 2 (Benihime — Identidade de Combate)  
**Checkout Isolado:** `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime`  
**Branch:** `codex/ryujin-bankai-sol-selamento` (base `58e3e4e`)  
**Documentos de Referência:** [112-tarefa-revisao-combate-armas-maos-efeitos-2026-10-26.md](../../112-tarefa-revisao-combate-armas-maos-efeitos-2026-10-26.md), [113-auditoria-video-ryujin-combate-tres-frentes-2026-10-26.md](../../113-auditoria-video-ryujin-combate-tres-frentes-2026-10-26.md) e [115-prompt-antigravity-benihime-combate-2026-10-26.md](../../115-prompt-antigravity-benihime-combate-2026-10-26.md)  
**Evidência Estrutural:** [comparison.json](comparison.json)

---

## 1. Causa Raiz Comprovada por Bytecode

A análise de descompilação de `dev.kosmx.playerAnim.mixin.HeldItemMixin.changeItemLocation` no runtime do PlayerAnimator revelou a causa técnica exata do comportamento relatado pelo usuário:

```java
// Trecho do bytecode do PlayerAnimator (HeldItemMixin):
Vec3f rot = animationProcessor.get3DTransform("rightItem", TransformType.ROTATION, Vec3f.ZERO);
Vec3f pos = animationProcessor.get3DTransform("rightItem", TransformType.POSITION, Vec3f.ZERO).scale(0.0625f);
poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
poseStack.mulPose(Axis.ZP.rotation(rot.getZ()));
poseStack.mulPose(Axis.YP.rotation(rot.getY()));
poseStack.mulPose(Axis.XP.rotation(rot.getX()));
```

1. **Ausência dos canais `rightItem`:** As três animações customizadas de Benihime (`benihime_diagonal`, `benihime_return`, `benihime_thrust`) em `attack_animations/` continham keyframes apenas para `rightArm`, `leftArm`, `torso`, `head`, `rightLeg` e `leftLeg`. O canal `rightItem` tinha exatamente **0 keyframes**.
2. **Efeito no runtime:** Ao consultar o processador de animação para `rightItem`, o PlayerAnimator retornava `Vec3f.ZERO` para rotação e posição em todos os ticks da animação.
3. **Consequência visual:** O item era desenhado na mão do jogador estritamente com a rotação estática definida no modelo JSON (`[0, -90, 55]` em terceira pessoa, herdada de `ryujin_katana_handheld.json`). Embora o antebraço se movesse, a espada permanecia imóvel em relação à palma da mão, sem qualquer flexão de punho, alinhamento do gume ao plano de ataque ou extensão linear.
4. **Referências nativas:** As animações nativas do Better Combat (`one_handed_slash_horizontal_right.json`, `one_handed_stab.json`, `one_handed_slash_horizontal_left.json`) contêm entre 12 e 18 keyframes dedicados a `rightItem`, sincronizados com as fases do ataque.

---

## 2. Implementação da Esgrima da Benihime

A esgrima de Kisuke Urahara com a Benihime foi calibrada como uma esgrima técnica, econômica e precisa de uma mão, respeitando o modelo singular da arma e as hitboxes registradas em [`benihime.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime/src/main/resources/data/bleachmod/weapon_attributes/benihime.json).

### 2.1. Golpe 1 — Diagonal Controlada (`benihime_diagonal`)
- **Hitbox:** `VERTICAL_PLANE`, ângulo 75°, upswing 0.45 (~5.4 ticks), duração ativa 12 ticks, término em 20 ticks.
- **Preparação (ticks 0..4):** O punho recolhe sutilmente em direção ao tronco; `rightItem` eleva a lâmina e cravando o punho (`pitch: 0.52 rad`, `y: 3.2 px`, `z: 1.0 px`), mantendo a guarda aderida aos dedos.
- **Passagem e Contato (ticks 6..8):** O punho estala para a frente acelerando o gume; `rightItem` estende a lâmina para a frente (`z: -4.2 px`, `pitch: -0.82 rad`, `yaw: -0.18 rad`, `roll: 0.12 rad`), cortando exatamente no plano de 75° sem atravessar a mão.
- **Recuperação (ticks 10..20):** Desaceleração suave pela lateral esquerda inferior (`z: -3.0 -> -1.8 -> -0.5 -> 0.0`) e retorno exato a zero no tick 20.

### 2.2. Golpe 2 — Retorno Distinto (`benihime_return`)
- **Hitbox:** `HORIZONTAL_PLANE`, ângulo 95°, upswing 0.45.
- **Preparação (ticks 0..4):** Aproveita a terminação do golpe diagonal no flanco esquerdo; o punho gira no semicírculo (`pitch: -0.26 rad`, `roll: 0.70 rad`, `x: -0.50 px`, `z: -1.2 px`).
- **Passagem e Contato (ticks 6..8):** Varredura horizontal da esquerda para a direita; o gume lidera a trajetória (`pitch: -0.78 rad`, `yaw: 0.35 rad`, `roll: -0.35 rad`, `z: -3.6 px`), mantendo o braço secundário em contrapeso e a empunhadura segura.
- **Recuperação (ticks 10..20):** Conclusão controlada no flanco direito e amortecimento para neutro.

### 2.3. Golpe 3 — Estocada Precisa (`benihime_thrust`)
- **Hitbox:** `FORWARD_BOX`, ângulo 0°, upswing 0.45.
- **Preparação (ticks 0..6):** Retração linear do cotovelo; a lâmina recua na altura do peito (`z: +1.8 px`, `pitch: 0.26 rad`, `yaw: -0.17 rad`), alinhando o eixo longitudinal perfeitamente com a mira.
- **Penetração e Contato (ticks 8..10):** Extensão direta e penetrante para frente (`z: -4.5 a -4.8 px`, `pitch: -0.61 rad`), projetando a ponta da lâmina no alcance máximo sem desvio lateral.
- **Recuperação (ticks 12..20):** Recuo elástico direto em linha reta até o centro neutro.

### 2.4. Limpeza de Offsets e Transições
- No `tick 20` de todas as animações, todas as propriedades de `rightItem` (`pitch`, `yaw`, `roll`, `x`, `y`, `z`) retornam estritamente a `0.0`.
- Isso garante que transições para defesa (`Guard`), esquiva (`Dodge`), técnicas ativas (`TechniqueService`) ou transformações cinematográficas não herdem nenhum deslocamento residual na mão.
- Para jogadores canhotos, o PlayerAnimator (`HeldItemMixin`) mapeia automaticamente as transformações de `rightItem` para `leftItem` com inversão de sinal no eixo lateral.

---

## 3. Arquivos Alterados no Checkout Isolado

1. `src/main/resources/assets/bleachmod/attack_animations/benihime_diagonal.json` (adicionados 9 keyframes de `rightItem`)
2. `src/main/resources/assets/bleachmod/attack_animations/benihime_return.json` (adicionados 9 keyframes de `rightItem`)
3. `src/main/resources/assets/bleachmod/attack_animations/benihime_thrust.json` (adicionados 9 keyframes de `rightItem`)
4. `src/main/resources/assets/bleachmod/player_animation/benihime_diagonal.json` (adicionado osso `right_item` com 9 posições e rotações)
5. `src/main/resources/assets/bleachmod/player_animation/benihime_return.json` (adicionado osso `right_item` com 9 posições e rotações)
6. `src/main/resources/assets/bleachmod/player_animation/benihime_thrust.json` (adicionado osso `right_item` com 9 posições e rotações)
7. `src/test/java/com/bleachmod/MvpRegressionTest.java` (adicionadas asserções obrigatórias para presença e ativação de `rightItem` e `right_item` nas três animações)

---

## 4. Evidências de Validação Automatizada

- **`test --offline`:** Concluído com código 0 (34s). Todas as asserções de `rightItem.isEnabled()`, duração, terminação e conformidade PlayerAnimator aprovadas.
- **`runGameTestServer --offline`:** Concluído com código 0 (4m 02s). **Todos os 233 GameTests aprovados**, incluindo o batch `bc_registry`, `bc_scope`, `bc_state`, `bc_reach` e `bc_late_state`.
- **`check build --offline`:** Concluído com código 0 (46s). **Todos os 147 cenários de regressão aprovados**, JAR gerado sem erros.

---

## 5. Limitações e Próximos Passos

1. **Validação Estrutural e Mecânica:** Concluída com evidência verificável de compilação, testes unitários e GameTests em servidor Forge.
2. **Homologação Artística:** A avaliação subjetiva da fluidez contínua dos golpes e da sensação ao jogar em primeira e terceira pessoa fica sujeita ao playtest humano no cliente gráfico.
3. **Multiplayer e Observador Remoto:** As animações Better Combat são propagadas pelo protocolo nativo do mod; o PlayerAnimator aplica os mesmos keyframes para observadores remotos.
4. **Estado Git:** Trabalho mantido no worktree isolado `Bleach-Modpack-benihime` na branch `codex/ryujin-bankai-sol-selamento`. Nenhum commit, push ou merge foi executado nesta etapa, aguardando aprovação humana.
