# 122 — Integração Senbonzakura Etapa 1: Reconstrução do Golpe Diagonal na Branch Feature-Poderes-bankais

**Data:** 26/10/2026  
**Frente:** Antigravity 1 — Senbonzakura (checkout isolado `Bleach-Modpack-design`)  
**Branch de Origem:** `codex/design-poderes-criaturas` (commit `c7c2b9f`)  
**Branch de Destino:** `Feature-Poderes-bankais` (checkout `Bleach-Modpack`)  
**Status da Integração:** Concluída, validada e compilada offline com 148/148 cenários verdes.

---

## 1. Identificação de Commits e Rastreabilidade

- **Commit de Origem (Etapa 1 na branch de design):**  
  `c7c2b9f` — `feat(senbonzakura): stage 1 reconstruction of diagonal strike with 8-20-30 cadence and client QA`
- **Base de Destino em `Feature-Poderes-bankais` antes do merge:**  
  `0ae6730` — `docs: preserve main roadmap review before Antigravity integration`
- **Merge da Etapa 1 em `Feature-Poderes-bankais`:**  
  `2127c26` — `merge: integrate Antigravity Senbonzakura stage 1 diagonal strike into powers`
- **HEAD Consolidado de `Feature-Poderes-bankais`:**  
  `6a2da98` — `merge: integrate Antigravity Ryujin heat vortex and solar base into powers` (integração concomitante da frente Ryūjin)
- **Confirmação de Ancestralidade:**  
  `git merge-base --is-ancestor c7c2b9f HEAD` retorna exit code 0 (`c7c2b9f` é ancestral estrito do HEAD consolidado `6a2da98`).

---

## 2. Inventário de Arquivos Integrados

A integração limitou-se estritamente aos ativos do Golpe 1 (diagonal) e suas evidências, sem invadir arquivos de gameplay, controladores compartilhados ou outras Zanpakutōs:

1. `src/main/resources/assets/bleachmod/attack_animations/senbonzakura_diagonal.json`
   - Reconstrução no formato Emote com cadência Better Combat (`beginTick: 8, endTick: 20, stopTick: 30, returnTick: 2`).
   - Empunhadura estrita a uma mão (`rightArm` e `rightItem` ativos; `leftArm` livre e relaxado no flanco/quadril esquerdo com `pitch: -0.42 rad`, `roll: -0.22 rad`).
   - Corte diagonal descendente (kesagiri) cruzando o peito com flexão de punho (`pitch: -0.65 rad`, `roll: -0.52 rad`, ponta em `z: -1.2`).
2. `src/main/resources/assets/bleachmod/player_animation/senbonzakura_diagonal.json`
   - Reconstrução correspondente no formato Bedrock para o PlayerAnimator, sincronizada em 1,5 segundos (30 ticks) com fade-in e retorno neutro.
3. `src/main/java/com/bleachmod/client/cinematic/SenbonzakuraCombatQaSequence.java`
   - Ajuste delimitado de amostragem de captura automática no tick ativo do golpe diagonal.
4. `Docs/poderes/121-antigravity-senbon-reconstrucao-golpe-diagonal-2026-10-26.md`
   - Documento técnico descritivo da entrega da Etapa 1.
5. `Docs/poderes/qa/antigravity-senbon-combate/stage1/`
   - 22 capturas PNG em alta resolução do cliente Minecraft real em 3 ângulos (frontal, perfil e costas) e respectivo `manifest.json`.

---

## 3. Previsão e Resolução de Conflitos

- **Previsão via `git merge-tree`:** 0 conflitos detectados contra `0ae6730`.
- **Estratégia de Merge:** `ort` executada de forma limpa e automática.
- **Isolamento de Documentação:**  
  O documento `121-antigravity-senbon-reconstrucao-golpe-diagonal-2026-10-26.md` não colidiu com o documento do Codex `121-revisao-main-planejamento-futuro-2026-10-26.md` nem com `121-antigravity-ryujin-turbilhao-base-solar-2026-10-26.md`.
- **Preservação de Outras Frentes:**  
  Benihime, Ryūjin curta, Hyōrinmaru, Zaraki e o roadmap do Codex permaneceram integralmente intactos e funcionais.

---

## 4. Validação em Ambiente Real e Compilação

Executados no conjunto integrado em `Bleach-Modpack`:

1. **Testes de Regressão Automatizados (`./gradlew regressionTest --offline`):**
   - **Resultado:** `BUILD SUCCESSFUL in 53s / 41s`
   - **Contagem real:** **148/148 cenários PASS** (100% verde).
   - A contagem de 148 reflete com precisão os cenários consolidados da branch de destino (incluindo testes de instalação do History Mode e novos testes da suíte global).
2. **Build Completo do Mod (`./gradlew check build --offline`):**
   - **Resultado:** `BUILD SUCCESSFUL in 38s` (14 tarefas acionáveis, 9 executadas, 5 up-to-date, Mixins e Reobf gerados com sucesso).
3. **Carga e Integridade Estrutural dos JSONs:**
   - Parse validado via script Python em ambos os formatos (`attack_animations` e `player_animation`).

---

## 5. Limites e Próximos Passos

- **Escopo Delimitado:** Somente o Golpe 1 (diagonal) foi integrado.
- **Pendências Mapeadas:**
  - **Etapa 2:** Reconstrução dos Golpes 2 (`senbonzakura_return`, corte horizontal partindo da pose baixa) e 3 (`senbonzakura_thrust`, estocada com cotovelo recolhido às costelas e avanço corporal cosmético).
  - **Etapa 3:** Implementação dos rastros dinâmicos finos de pétalas na lâmina e coordenação com Codex para remoção do emissor `SenbonPetalsS2C.ARC` em `BetterCombatCompat.java` (linhas 43–47).
- **Governança:** Nenhum push para repositório remoto ou alteração na branch `main` foi realizado nesta operação.
