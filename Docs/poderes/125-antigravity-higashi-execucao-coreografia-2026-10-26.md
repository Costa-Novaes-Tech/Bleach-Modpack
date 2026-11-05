# 125 — Higashi: Coreografia de Execução Fluida, Câmera com Tolerância a Paredes e Autoridade de Servidor

**Data:** 26/10/2026  
**Autor:** Antigravity  
**Branch:** `codex/ryujin-bankai-sol-selamento` (checkout `Bleach-Modpack-benihime`)  
**Contexto:** Prompt 125 — Refino Hyōrinmaru e Higashi em fases (Fase 3)

---

## 1. Visão Geral da Fase 3

A Fase 3 aprimora a técnica e apresentação cinemática de execução do **Zanka no Tachi, Higashi: Kyokujitsujin**, removendo a hesitação estática de 4 ticks antes do golpe, dotando a câmera lateral de inteligência espacial contra paredes e corredores estreitos, e reafirmando a autoridade estrita do servidor sobre execuções letais.

---

## 2. Refinamento da Coreografia Corporal (`RyujinChoreography`)

### 2.1 Eliminação da Hesitação Pré-Golpe
- **Diagnóstico:** Anteriormente, tanto o ataque comum quanto a execução continham os keyframes `higashiHip(6)` e `higashiHip(10)` com valores de pose idênticos, criando um congelamento estático absoluto de 4 ticks (0.2s) na cintura antes do corte ascendente.
- **Correção:**
  - Em `RyujinChoreography.java`, a chave redundante no tick 6 foi removida tanto de `attack("higashi")` quanto de `execution()`.
  - A trajetória agora transita suavemente de `neutral(0)` direto para `higashiHip(10)`, realizando um movimento contínuo de desembainhar/recolher a lâmina no quadril sem qualquer estagnação intermediária.
  - O corte ascende dinamicamente no tick 12 (`higashiCross(12)`) e atinge a extensão máxima no tick 16 (`higashiEnd(16)`), apontando a ponta da lâmina em direção à vítima ($blade.z > 0.6, blade.y > 0.4$).

### 2.2 Linha do Tempo e Pausa Dramática
- **Preparação e Corte:** Ticks 0..10 (recolhimento fluido) $\rightarrow$ 10..16 (corte ascendente cortando o alvo).
- **Pausa Dramática (Freeze):** Ticks 16..56 (40 ticks exatos / 2.0 segundos de jogo). Durante este intervalo, o Shinigami permanece travado na pose de corte concluído (`higashiEnd(16)` com peso 1.0) e o alvo permanece retido em silhueta estática.
- **Ignição e Cinzas:** Tick 56 até 88 (32 ticks). A incineração por calor extremo consome o alvo em chamas e partículas de cinzas, com retorno progressivo da câmera e relaxamento da postura do atacante.

---

## 3. Câmera Lateral com Tolerância a Paredes (`HigashiCinematicPath` & `HigashiExecutionVisuals`)

### 3.1 Prevenção de Descarte Precoce em Corredores
- **Problema:** A câmera usava um vetor lateral fixo (`side = (-f.z, 0, f.x)`). Em ambientes fechados ou próximos a paredes, o raycast colidia com os blocos a menos de 1.5 blocos do ponto de foco (`focus`), invocando `dismiss()` imediatamente e cancelando a experiência cinemática.
- **Solução Implementada:**
  1. **Assinatura Flexível:** `HigashiCinematicPath.sample` recebeu suporte a `sideSign` ($\pm 1.0$), permitindo enquadramento tanto pelo flanco direito quanto pelo esquerdo, mantendo sobrecarga padrão para chamadas legadas.
  2. **Seleção Preventiva de Lado:** Ao inicializar a câmera cinemática, o sistema executa um raycast em ambos os lados (`eyeRight` e `eyeLeft`) contra o `mc.level`. O lado que apresentar maior distância livre em relação aos blocos de colisão é selecionado automaticamente.
  3. **Fallback Dinâmico:** Se durante a execução ocorrer obstrução súbita no lado ativo aproximando a câmera a menos de 1.5 blocos, o sistema tenta comutar instantaneamente para o lado oposto antes de abortar a cena. Apenas se ambos os lados estiverem bloqueados em espaço confinado é que o retorno suave à câmera do jogador ocorre.

---

## 4. Preservação da Autoridade do Servidor (`HigashiExecutionService`)

- Mantida inalterada a restrição matemática e de segurança:
  - Limiar de saúde $\le 7\%$ do HP máximo (`THRESHOLD = 0.07`).
  - Execução só comutada mediante evento de morte autoritativa do servidor (`!target.isAlive()`).
  - Imunidades vanilla (totens, invulnerabilidade criativa/operador, aliados) estritamente respeitadas.

---

## 5. Validação e Resultados
- **Suite de Regressão Unitária:** `./gradlew check --offline` executado com 148 testes aprovados (0 falhas).
- **GameTests:** `HigashiExecutionGameTests` validando:
  - Bypass de defesas exclusivamente em $\le 7\%$.
  - Alvo único por golpe.
  - Pausa dramática de 40 ticks exatos entre o término do corte e a ignição.
  - Retorno de câmera somente após o ciclo de cinzas.
