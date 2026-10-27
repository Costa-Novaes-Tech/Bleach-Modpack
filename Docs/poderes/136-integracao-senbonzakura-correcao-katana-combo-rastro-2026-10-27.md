# 136 — Integração: Correção da Katana Senbonzakura, Combo e Rastro Cinemático na Feature-Poderes-bankais

Data: 27/10/2026  
Autor: Antigravity Design Lab  
Branch de Origem: `codex/design-poderes-criaturas` (Commit `1c44af619e15b8a88cded1cb83a208996cf59e09`)  
Branch de Destino: `Feature-Poderes-bankais` (Merge Commit `90b6469`)  
Worktrees / Checkouts:
- Design: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- Principal: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack`

---

## 1. Resumo Executivo da Integração

Em atendimento ao [Prompt 133](133-prompt-correcao-senbon-katana-combo-2026-10-27.md) e à análise técnica [132](132-analise-video-regressoes-senbon-higashi-2026-10-27.md), foi realizada a integração completa da frente de reconstrução da esgrima da Senbonzakura e do alinhamento cinemático do rastro de combate na branch principal de poderes (`Feature-Poderes-bankais`).

O merge foi concluído no commit **`90b6469`**, preservando integralmente o trabalho paralelo do Codex e as entregas homologadas anteriores (Benihime, Ryūjin Jakka / Higashi, Hyōrinmaru e Zaraki).

---

## 2. Commits Integrados da Branch `codex/design-poderes-criaturas`

1. **`45c1207`** — `fix(senbon): reconstruct katana choreography, fix animation parser and palm grip`
   - **Correção Crítica no Parser PlayerAnimator:** Substituição de `"turn": false` por `"turn": 0` em `senbonzakura_return.json` e `senbonzakura_thrust.json`. Elimina `NumberFormatException` no Gson que causava descarte silencioso das animações no Better Combat e `NullPointerException` ao executar os golpes 2 e 3.
   - **Ajuste Anatômico de Grip:** Correção do canal `rightItem` em todos os quadros de `y: -0.3` para `y: -2.0` (escala 1/16, equivalente a -0.125 blocos), fixando firmemente o cabo da katana na palma do jogador.
   - **Reconstrução dos Clips (Emote e Bedrock):**
     - **Golpe 1 (`senbonzakura_diagonal`):** Corte nobre diagonal descendente do ombro direito até a esquerda baixa.
     - **Golpe 2 (`senbonzakura_return`):** Retorno horizontal na altura do peito, partindo exatamente da pose final do Golpe 1 ($\Delta = 0.00$).
     - **Golpe 3 (`senbonzakura_thrust`):** Estocada central linear partindo da recolha na costela ($\Delta = 0.00$), seguida de recolhimento nobre à guarda.
2. **`fd5a3e4`** — `feat(senbon): add vfx disable toggle and document combo reconstruction in doc 135`
   - Introdução do switch de sistema `bleachmod.senbonVfxDisable=true` (acessível via JVM args e `build.gradle`) para permitir validação anatômica limpa da esgrima sem interferência de partículas.
   - Documento detalhado de diagnóstico e especificação [135](135-antigravity-senbon-correcao-katana-combo-2026-10-27.md).
3. **`1c44af6`** — `fix(senbon): align melee trail to real animation applier and add seam regression test`
   - Atualização de `SenbonCombatVisuals.java` para amostrar a cinemática real do jogador via `AnimationApplier.get3DTransform` (ossos de braço e item), gerando o rastro de pétalas dinâmico diretamente na ponta e cabo da lâmina renderizada.
   - Fallback de cinemática direta perfeitamente contínuo para situações de rede/mixins desacoplados.
   - Inclusão de testes de regressão automatizados para deserialização dos 3 clips e validação de continuidade matemática de seams ($\Delta = 0.00$).

---

## 3. Resolução do Merge e Preservação de Contexto

- **Estratégia de Merge:**
  A integração foi realizada via `git merge 1c44af619e15b8a88cded1cb83a208996cf59e09` no checkout `Bleach-Modpack`.
- **Arquivos Preservados Sem Sobreposição:**
  - `Docs/poderes/00-CONTINUIDADE-CODEX.md` (trabalho ativo do Codex preservado).
  - `Docs/poderes/72-revisao-geral-roadmap-gamedesign-2026-10-20.md` (roadmap preservado).
  - `Docs/poderes/134-prompt-correcao-higashi-katana-morte-pausa1s-2026-10-27.md` (untracked preservado).
  - Logs de execução locais em `logs/`.
- **Incolumidade de Escopo:**
  - Nenhuma alteração em Ryūjin Jakka (Higashi e transformação intactas).
  - Nenhuma alteração em Benihime, Hyōrinmaru ou Zaraki.
  - Nenhuma alteração em balanceamento de dano, stamina, alcance ou física de movimento do jogador.
  - Nenhuma operação sobre a branch `main`.

---

## 4. Validação Técnica e Evidências

### 4.1 Testes de Regressão Automatizados (`regressionTest`)
Executados de forma offline e determinística:
- **Design (`codex/design-poderes-criaturas`):** 150/150 cenários aprovados.
- **Principal Integrado (`Feature-Poderes-bankais`):** 150/150 cenários aprovados.
- **Novos Testes Cobertos em `MvpRegressionTest.java`:**
  - Deserialização válida de `senbonzakura_diagonal`, `senbonzakura_return` e `senbonzakura_thrust` sem `NumberFormatException`.
  - Verificação de continuidade de rastro e seams: tolerância $\le 10^{-6}$ entre o término do Golpe 1 e início do Golpe 2, e entre o término do Golpe 2 e início do Golpe 3.

### 4.2 Compilação e Build Completo (`check build`)
- Executado via `./gradlew check build --offline` no checkout principal integrado (`Bleach-Modpack`).
- **Resultado:** **`BUILD SUCCESSFUL`** sem avisos críticos ou erros de compilação.

---

## 5. Instruções para Verificação no Cliente Forge (QA Manual)

Para validar a entrega no cliente do jogo:
1. **Verificação Sem Partículas (Opcional para foco estrito na katana):**
   - Executar o cliente Forge com `-Dbleachmod.senbonVfxDisable=true`.
   - Empunhar a Senbonzakura (selada, Shikai ou Bankai).
   - Desferir os três golpes sucessivos do combo:
     - **Golpe 1:** Corte diagonal descendente suave da direita superior para a esquerda inferior.
     - **Golpe 2:** A espada parte imediatamente da posição baixa esquerda e faz um corte horizontal na altura do peito, terminando recolhida na costela direita.
     - **Golpe 3:** Estocada reta e limpa na mira central, retornando à guarda nobre.
   - Observar que o cabo da espada permanece 100% encaixado na mão direita sem flutuação.
2. **Verificação Com Partículas (Padrão):**
   - Executar sem o argumento de desligamento.
   - Verificar que a esteira de pétalas acompanha a lâmina em tempo real nas três fases do golpe.
