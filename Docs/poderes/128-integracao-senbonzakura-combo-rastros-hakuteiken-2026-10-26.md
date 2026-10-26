# 128 — Integração Senbonzakura: Combo Completo, Rastros Dinâmicos e Hakuteiken na Branch Feature-Poderes-bankais

**Data:** 26/10/2026  
**Frente:** Antigravity 1 — Senbonzakura (checkout isolado `Bleach-Modpack-design`)  
**Branch de Origem:** `codex/design-poderes-criaturas` (HEAD `382620d`)  
**Branch de Destino:** `Feature-Poderes-bankais` (checkout `Bleach-Modpack`)  
**Status da Integração:** Concluída, validada e compilada offline com 148/148 cenários de regressão verdes.

---

## 1. Identificação de Commits e Rastreabilidade

- **Commits de Origem Integrados:**
  1. `9bfec03` — `feat(senbonzakura): complete 3-hit fencing combo with return cut and central thrust`
  2. `157323a` — `feat(senbonzakura): implement blade-bound petal trails and replace legacy ARC in BetterCombat`
  3. `382620d` — `docs: report 127 delivery of Senbonzakura complete combo, blade trails and Hakuteiken QA`
- **Base de Destino em `Feature-Poderes-bankais` antes do merge:**  
  `ef64c45` — `docs: registra integracao e validacao em cliente real do turbilhao solar da Ryujin`
- **Merge Realizado:**  
  Avanço limpo Fast-forward (`ef64c45..382620d`).
- **HEAD Consolidado de `Feature-Poderes-bankais`:**  
  `382620d`
- **Confirmação de Ancestralidade:**  
  - `c7c2b9f` (Etapa 1 original): `git merge-base --is-ancestor c7c2b9f HEAD` -> retorno 0 (ancestral confirmado).
  - `382620d` (Entrega completa): `git merge-base --is-ancestor 382620d HEAD` -> retorno 0 (ancestral confirmado).

---

## 2. Inventário Integrado na Branch de Poderes

1. **Coreografia Completa de Esgrima:**
   - `src/main/resources/assets/bleachmod/attack_animations/senbonzakura_return.json`
   - `src/main/resources/assets/bleachmod/attack_animations/senbonzakura_thrust.json`
   - `src/main/resources/assets/bleachmod/player_animation/senbonzakura_return.json`
   - `src/main/resources/assets/bleachmod/player_animation/senbonzakura_thrust.json`
   - Cadência unificada `8-20-30-2`, empunhadura a uma mão, mão livre no quadril esquerdo para contrabalanço nobre, transições anatômicas contínuas sem teleporte ou saltos entre os três golpes.
2. **Rastros Dinâmicos Ancorados à Lâmina e Remoção do ARC Legado:**
   - `src/main/java/com/bleachmod/client/model/SenbonCombatVisuals.java`: classe de amostragem contínua da espada (base, 1/3, 2/3 e ponta), com orçamento gráfico (12/6/3 partículas), atenuação em primeira pessoa (50%), culling por distância (48 blocos) e corte imediato de fita em teleporte (> 4 blocks).
   - `src/main/java/com/bleachmod/common/compat/BetterCombatCompat.java`: substituição do antigo pacote estático `SenbonPetalsS2C.ARC` na linha 43 pelo evento dinâmico leve `SenbonPetalsS2C.AURA` associado ao índice do combo.
   - `src/main/java/com/bleachmod/client/compat/BetterCombatClientCompat.java` e `src/main/java/com/bleachmod/client/particle/SenbonPetalEffects.java`: despacho seguro do evento visual no cliente.
3. **Evidências Fotográficas do Cliente Real (46 PNGs capturados):**
   - `Docs/poderes/qa/antigravity-senbon-combate/stage2/` (22 frames em alta resolução do combo completo nos 3 ângulos + `manifest.json`).
   - `Docs/poderes/qa/antigravity-senbon-combate/stage3/` (24 frames em alta resolução dos 5 momentos do Hakuteiken C nos 3 ângulos + `manifest.json`).
4. **Relatório Técnico da Entrega:**
   - `Docs/poderes/127-antigravity-senbon-entrega-combo-rastros-hakuteiken-2026-10-26.md`.

---

## 3. Preservação Rigorosa de Frentes e Conflitos

- **Conflitos de Merge:** 0 conflitos.
- **Trabalho do Codex Preservado:**  
  Arquivos em edição/untracked no checkout de destino permaneceram integralmente intocados:
  - `Docs/poderes/00-CONTINUIDADE-CODEX.md`
  - `Docs/poderes/72-revisao-geral-roadmap-gamedesign-2026-10-20.md`
  - Documentos conceituais e prompts 123, 124, 125, 126 e `qa/cycle123/`.
- **Outras Zanpakutōs:**  
  Ryūjin (transformação curta, áudio e turbilhão solar aprovados), Benihime (cinemática de abertura e esgrima aprovadas), Hyōrinmaru e Zaraki permaneceram inalteradas.

---

## 4. Validação em Ambiente Real Integrado

Executado no checkout principal `Bleach-Modpack`:

1. **Testes de Regressão Globais (`./gradlew regressionTest --offline`):**
   - **Resultado:** `BUILD SUCCESSFUL in 1m 15s`.
   - **Contagem real:** **148/148 cenários PASS (100% verde)**.
2. **Build Completo do Mod (`./gradlew check build --offline`):**
   - **Resultado:** `BUILD SUCCESSFUL in 37s`.
   - 14 tarefas acionáveis, classes, mixins, jar e reobfuscação gerados com sucesso e sem falhas.

---

## 5. Próximos Passos e Governança

- A branch `Feature-Poderes-bankais` contém agora todo o combo reconstruído, os rastros e o Hakuteiken demonstrados em cliente real.
- Nenhum push para repositório remoto ou modificação na branch `main` foi executado nesta tarefa.
