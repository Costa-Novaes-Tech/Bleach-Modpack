# 121 — Antigravity 1: Senbonzakura — Etapa 1: Reconstrução do Golpe Diagonal (Sem Partículas)

**Data:** 26/10/2026  
**Frente:** Antigravity 1 — Senbonzakura (checkout isolado `Bleach-Modpack-design`)  
**Branch:** `codex/design-poderes-criaturas` (base técnica `569f4d6`)  
**Status:** Etapa 1 implementada e demonstrada no Minecraft Forge real. Sem commit/push/merge. Pronto para validação do usuário antes de prosseguir para os Golpes 2 e 3.

---

## 1. Escopo e Diretrizes da Etapa 1

Conforme a instrução mandatária de execução em etapas:
> *"Primeiro entregue somente o golpe diagonal completo, sem partículas, demonstrado no Minecraft real. Depois complete retorno e estocada. Só então acrescente os rastros."*

A reconstrução do Golpe 1 descarta o ritmo anterior de 12 ticks sem fade-in e adota a cadência canônica do Better Combat (`beginTick: 8, endTick: 20, stopTick: 30, returnTick: 2`), garantindo fluidez pura de esgrima, aderência física da empunhadura e empunhadura estritamente a uma mão.

---

## 2. Anatomia e Biomecânica do Golpe 1 Reconstruído

### A. Empunhadura Estrita a Uma Mão
- **Mão Principal (`rightArm` / `rightItem`):** Empunha a katana durante todo o ciclo.
- **Mão Secundária (`leftArm`):** Permanece 100% livre e relaxada junto ao quadril esquerdo/flanco (`pitch: -0.42 rad`, `yaw: -0.15 rad`, `roll: -0.22 rad`), garantindo contrabalanço elegante e nobre (estilo Byakuya Kuchiki). Não há pegada com duas mãos em nenhum momento.
- **Espelhamento:** Configurado com `degrees: false`, compatível com o `MirrorModifier` nativo do Better Combat (para jogadores canhotos ou mão secundária).

### B. Os 4 Momentos do Golpe Diagonal
1. **Preparação Contida (Tick 8 do asset / Age 38 do teste):**
   - O jogador recolhe a espada junto ao ombro direito dominante (`rightArm`: `x: -4.8, y: 2.2, z: -0.6`, `pitch: -1.75 rad`).
   - O punho entra em extensão (`pitch: +0.48 rad`, `roll: -0.70 rad`), mantendo a ponta da lâmina elevada e claramente afastada da cabeça e do pescoço.
   - Tronco realiza leve rotação para a direita (+12°), enquanto a cabeça permanece travada no oponente.
2. **Passagem Ativa do Corte (Ticks 11–13 do asset / Age 42 do teste):**
   - A lâmina descreve um arco diagonal descendente (kesagiri) cortando do alto da direita para baixo na esquerda através da região do peito.
   - O punho flexiona e prona ativamente (`pitch: -0.65 rad`, `roll: -0.52 rad`, avanço da ponta `z: -1.2`), fazendo o gume afiado conduzir o corte sem girar em falso na mão.
   - O tronco transfere o peso para a perna esquerda dianteira; **sem giro de 360°**.
3. **Impacto e Desaceleração Firme (Tick 14 do asset / Age 44 do teste):**
   - Janela do upswing (0.40). Desaceleração sólida no quadrante frontal-esquerdo (`pitch: -0.75 rad`, `roll: -0.35 rad`).
   - Guarda perfeitamente colada à mão, sem folgas ou deslocamentos no modelo.
4. **Final do Golpe (Tick 20 do asset / Age 50 do teste):**
   - A espada conclui baixa no lado oposto (lado esquerdo inferior).
   - Esta pose estática é o ponto de partida idêntico para o Golpe 2 (corte horizontal de retorno), eliminando qualquer salto ou interpolação abrupta de armas.
5. **Recuperação Neutra (Tick 30 do asset / Age 76 do teste):**
   - Caso o jogador não aperte o ataque para encadear o combo (fora do `returnTick: 2`), o personagem retorna em desaceleração suave à postura neutra (chudan-no-kamae), zerando todos os ossos.

---

## 3. Alinhamento com o Codex e Indicação do Emissor ARC

Identificamos no código compartilhado (`BetterCombatCompat.java`, linhas 43–47) que o servidor dispara um pacote de partículas estáticas:
```java
if(CURRENT.get().allowed && combo==1 && p.getMainHandItem().is(ModItems.SENBONZAKURA.get())){
    Vec3 f=SenbonGeometry.forward(p.getYRot()),o=p.position().add(0,1.2,0);
    var packet=new SenbonPetalsS2C(SenbonPetalsS2C.ARC,o,f,2.8F,1F,1,...);
    ...
}
```
Como `BetterCombatCompat.java` pertence à governança do Codex:
- **Respeito às fronteiras:** Não alteramos o controlador comum em paralelo.
- **Indicação formal ao Codex:** Na Etapa 3 (adição dos rastros dinâmicos ancorados na lâmina), o Codex deverá remover esse bloco de emissão ARC fixo para evitar duplicação de partículas desconexas.

---

## 4. Evidências de Validação em Jogo Real

### Testes Automatizados
- **Regressões Globais (`./gradlew regressionTest --offline`):**  
  `BUILD SUCCESSFUL in 1m 32s` — **147/147 cenários APROVADOS (100% verde)**.
- **Sincronização Rigorosa:**  
  `attack_animations/senbonzakura_diagonal.json` (Better Combat) e `player_animation/senbonzakura_diagonal.json` (Bedrock) mantêm exatamente os mesmos 5 marcos temporais e curvas de rotação/translação.

### Capturas no Framebuffer Real do Minecraft Forge
- **Comando executado:** `./gradlew runClient -PbenihimeQa=true -PsenbonCombatQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline`
- **Resultado:** `BUILD SUCCESSFUL in 3m 10s`.
- **Arquivos salvos em:** [`Docs/poderes/qa/antigravity-senbon-combate/stage1/`](qa/antigravity-senbon-combate/stage1/)

| Momento do Golpe 1 | Visão Perfil (`02_combo_profile`) | Visão Costas (`03_combo_back`) | Visão Frontal (`01_combo_front`) |
|---|---|---|---|
| **Preparação (Tick 8)** | `stage1_02_combo_profile_frame-t038.png` | `stage1_03_combo_back_frame-t038.png` | — |
| **Passagem do Corte (Tick 12)** | `stage1_02_combo_profile_frame-t042.png` | `stage1_03_combo_back_frame-t042.png` | `stage1_01_combo_front_frame-t042.png` |
| **Impacto Firme (Tick 14)** | `stage1_02_combo_profile_frame-t044.png` | `stage1_03_combo_back_frame-t044.png` | `stage1_01_combo_front_frame-t044.png` |
| **Posição Final Baixa (Tick 20)** | `stage1_02_combo_profile_frame-t050.png` | `stage1_03_combo_back_frame-t050.png` | — |
| **Recuperação Neutra (Tick 30)** | `stage1_02_combo_profile_frame-t076.png` | `stage1_03_combo_back_frame-t076.png` | `stage1_01_combo_front_frame-t076.png` |

---

## 5. Próximo Passo Imediato

Após a sua aprovação da postura, alinhamento e silhueta deste Golpe 1 isolado, prosseguiremos imediatamente para a **Etapa 2**: reconstrução contínua do Golpe 2 (retorno horizontal a partir da posição baixa) e do Golpe 3 (estocada central com recuo de cotovelo), mantendo o combate sem partículas antes da fase final de efeitos.
