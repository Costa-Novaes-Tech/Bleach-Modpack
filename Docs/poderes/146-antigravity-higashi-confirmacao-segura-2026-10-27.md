# 146 — Relatório Antigravity: Confirmação Segura, Ciclo de Vida de Pendentes e QA para Auditoria Codex

**Data**: 27/10/2026  
**Autor**: Antigravity  
**Branch**: `codex/ryujin-bankai-sol-selamento` (checkout `Bleach-Modpack-benihime`)  
**Base**: Ciclo 144 (`0f3a1e1`), Prompt 146  
**Status**: CONCLUÍDO (Passes: 237 GameTests, 149 Regressão, 5/5 QA Cliente Real) — **SEM MERGE / SEM PUSH / MAIN INTOCADA**  
**Pacote de Auditoria Local para Codex**: [`Docs/poderes/qa/cycle146-higashi/run-1793128459/`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime/Docs/poderes/qa/cycle146-higashi/run-1793128459/)

---

## 1. Respostas aos Apontamentos da Auditoria Codex

### 1.1 Confirmação Segura (Prevenção de Execução Antecipada ou Cancelada)
- **Problema apontado**: No ciclo 144, `confirmedDeath` era chamado dentro de `LivingDeathEvent` com prioridade normal, antes do término da distribuição do evento. Se um listener posterior cancelasse o evento ou revivesse a entidade, a execução já havia sido transmitida e a tag `bleachmod_higashi_confirmed` travava execuções futuras.
- **Correção arquitetural**:
  1. **Remoção de Confirmação Precoce**: O listener `higashiDeath` em `LivingDeathEvent` foi **completamente removido**. Nenhuma confirmação de morte ocorre durante a distribuição do evento.
  2. **Separação entre Hint Visual e Confirmação Final**:
     - Antes do dano, o servidor emite um pacote de aviso prévio (`HigashiExecutionService.sendHint`) com `confirmed = false`.
     - O hint informa aos observadores para suprimir o flash vermelho vanilla no cliente sem abrir câmera, sem criar réplica e sem iniciar incineração.
     - O hint possui timeout de expiração automática (15 ticks) e rollback explícito (`cancelHint`).
  3. **Decisão Autoritativa Final**:
     - Apenas **após** o retorno de `hit(c, t, 16, true)` ou `execute(p, t)` — quando o pipeline do Minecraft (`actuallyHurt` $\rightarrow$ `die`) terminou por completo —, o servidor verifica `dealt && !t.isAlive() && !isDeathCancelled(t)`.
     - Se um listener cancelar o evento (detectado em `EventPriority.LOWEST` com `receiveCanceled = true`), `isDeathCancelled` retorna verdadeiro, `confirmedDeath` **não é chamado**, e `cancelHint` é transmitido imediatamente.
     - A flag persistente `bleachmod_higashi_confirmed` só é gravada no momento da confirmação final e irrevogável, e é limpa em `cancelHint` caso a entidade sobreviva.

### 1.2 Delimitação de Vítima contra Dano Refletido e Reentrância
- **Problema apontado**: `ACTIVE_CAST` demarcava todo o contexto do cast, podendo associar a morte de um terceiro (ou do próprio conjurador via Espinhos/Thorns) ao ataque de Higashi.
- **Correção arquitetural**:
  - `ACTIVE_CAST` foi removido em favor da vinculação estrita de alvo por golpe: cada iteração de `cutVictims` opera individualmente sobre a entidade `t` em escopo fechado.
  - Qualquer dano secundário, reflexão ou evento disparado durante o processamento do dano em `t` que atinja o caster, aliados ou mobs secundários é sumariamente ignorado.
  - Validado via GameTest negativo com `SecondaryKillListener`: a morte de uma entidade secundária durante o cast resulta em `confirmedCountFor == 0`, mantendo a execução estritamente restrita à vítima primária.

### 1.3 Ciclo de Vida e Limpeza de Pendentes no Cliente (`PENDING_TARGETS`)
- **Problema apontado**: `PENDING_TARGETS` era um `Set<Integer>` concorrente sem expiração, sem vínculo de mundo/sessão e sem tratamento de retornos antecipados.
- **Correção arquitetural**:
  - `PENDING_TARGETS` agora armazena instâncias de `PendingTarget(int entityId, long expiryGameTime, int worldHash, boolean confirmed)`.
  - **Thread-Safety sem Contaminação de Estado**: A thread Netty apenas grava o registro imutável com timestamp e hash da sessão atual, sem interagir com entidades ou renderizadores.
  - **Expiração Automática**: Em `isPendingTarget` e a cada `ClientTickEvent`, entradas com `expiryGameTime < now` ou pertencentes a instâncias anteriores de mundo são automaticamente purgadas.
  - **Limpeza em Todos os Caminhos**: `accept(m)` envolve seu processamento em bloco `try-finally`, garantindo que `removePendingTarget` seja invocado mesmo em retornos antecipados.
  - **Limpeza em Desconexão/Reset**: `reset()`, `logout()` e troca de dimensão limpam integralmente o mapa.

---

## 2. Testes Autoritativos Negativos (GameTests)

Foram introduzidos 4 novos GameTests em [`HigashiExecutionGameTests.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime/src/main/java/com/bleachmod/gametest/HigashiExecutionGameTests.java):

1. **`normalLethalDamageCancelledByLaterListenerLeavesEntityAliveAndZeroConfirmed`**:
   - Alvo com 10/20 HP recebe dano letal de Higashi (16 de dano).
   - Um listener em `EventPriority.LOW` cancela `LivingDeathEvent` e restaura a vida para 10 HP.
   - **Asserções Verificadas**:
     - `t.isAlive() == true` e `t.getHealth() == 10.0F`.
     - `HigashiExecutionService.confirmedCountFor(t.getUUID()) == 0` (zero confirmações emitidas).
     - `!t.getPersistentData().getBoolean("bleachmod_higashi_confirmed")` (sem trava persistente).
   - Listener desregistrado; segundo cast de Higashi desferido contra o mesmo alvo.
   - **Asserções Verificadas**:
     - `t.isAlive() == false` (morte autoritativa concluída).
     - `HigashiExecutionService.confirmedCountFor(t.getUUID()) == 1` (exatamente 1 confirmação emitida).

2. **`totemOfUndyingPreventsHigashiExecutionOnNormalLethalDamage`**:
   - Alvo com 10/20 HP portando Totem da Imortalidade na mão secundária.
   - Higashi desfere 16 de dano letal; o Totem salva a criatura.
   - **Asserções Verificadas**:
     - `t.isAlive() == true`.
     - `HigashiExecutionService.confirmedCountFor(t.getUUID()) == 0`.
     - Nenhuma tag persistente gravada.

3. **`reflectedDamageAndSecondaryDeathCannotTriggerHigashiExecution`**:
   - Alvo primário (10/20 HP) e entidade secundária (1 HP) adjacente.
   - Listener simula morte secundária/dano refletido durante o processamento do golpe.
   - **Asserções Verificadas**:
     - Entidade secundária morre, mas `confirmedCountFor(secondary) == 0`.
     - Caster `confirmedCountFor(caster) == 0`.
     - Alvo primário morre com `confirmedCountFor(target) == 1`.

4. **`singleConfirmationForNormalLethalAndThresholdExecution`**:
   - Execução por limiar ($\le 7\%$) e dano letal normal.
   - **Asserções Verificadas**:
     - Confirmação estritamente unitária (`confirmedCountFor == 1`, `totalConfirmedCount == 1`).
     - Zero duplicação de drops ou XP.

---

## 3. Evidências de Execução

### 3.1 Servidor GameTest
- **Comando**: `.\gradlew.bat runGameTestServer --offline --console=plain`
- **Resultado**: `BUILD SUCCESSFUL in 4m 28s`
- **Placar**: `All 237 required tests passed :)` (237/237 testes com sucesso integral).

### 3.2 Suíte de Regressão e Build
- **Comando**: `.\gradlew.bat check build --offline --console=plain`
- **Resultado**: `BUILD SUCCESSFUL in 53s`
- **Placar**: `PASS: 149 regression scenarios` (149/149 cenários de `MvpRegressionTest` aprovados).

### 3.3 Cliente QA Real Forge
- **Comando**: `.\gradlew.bat runClient -PbenihimeQa=true -PcinematicQa=true -PhigashiPoseQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain`
- **Resultado**: `BUILD SUCCESSFUL in 2m 29s`
- **Resultados dos 5 Cenários**:
  1. `lethal_husk`: `seen=true expected=true passed=true`
  2. `lethal_flying`: `seen=true expected=true passed=true`
  3. `threshold_real`: `seen=true expected=true passed=true`
  4. `survive`: `seen=false expected=false passed=true`
  5. `miss`: `seen=false expected=false passed=true`

### 3.4 Auditoria Fotométrica da Janela de Morte
- **ROI**: Caixa de 300x250 pixels centralizada no alvo (75.000 pixels).
- **Critério de Hurt Flash**: $R > 180, G < 80, B < 80$.
- **Resultados**:
  - `t=26` (WAIT_IMPACT, primeira pessoa): 0 pixels vermelhos.
  - `t=30` (WAIT_IMPACT, primeira pessoa): 0 pixels vermelhos.
  - `t=32` (WAIT_IMPACT, transição pré-câmera): 23 pixels vermelhos concentrados em uma única partícula de faísca em $y=133$ (0,03% da ROI). Regiões da cabeça (top 50px) e corpo/pernas (bottom 50px): **0 pixels vermelhos**. Zero flash avermelhado vanilla na malha do mob.
  - `t=36` (DRAW, câmera lateral): Transição suave para a réplica cliente (`replicaVisible = true`), sem rotação de morte vanilla ou desvio de pose.

---

## 4. Pacote de Entrega para a Auditoria Codex

Os arquivos brutos, manifests, PNGs da janela de morte e telemetria estão disponíveis no repositório local:
- **Diretório**: `Docs/poderes/qa/cycle146-higashi/run-1793128459/`
- **Arquivos incluídos**:
  - [`manifest.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime/Docs/poderes/qa/cycle146-higashi/run-1793128459/manifest.json): SHA-256 de todos os arquivos modificados, ambiente, comandos executados e contagens.
  - [`timeline.jsonl`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime/Docs/poderes/qa/cycle146-higashi/run-1793128459/timeline.jsonl): 71 quadros amostrados com telemetria completa (`worldTick`, `gameTick`, `targetId`, `hintActive`, `confirmedActive`, `originalSuppressed`, `replicaVisible`).
  - [`summary_logs.txt`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime/Docs/poderes/qa/cycle146-higashi/run-1793128459/summary_logs.txt): Logs completos de saída dos GameTests e do cliente Forge.
  - `screenshots/`: 71 imagens PNG originais cobrindo a transição e a janela de morte de todos os cenários.

---

## 5. Veredito Separado por Requisito

| Requisito | Status | Justificativa |
| :--- | :---: | :--- |
| **Confirmação Segura** | **PASS** | `confirmedDeath` ocorre estritamente após a conclusão autoritativa do dano (`hit`/`execute`), com verificação de não-cancelamento e zero confirmações espúrias em caso de cancelamento/totem. |
| **Pending Limpo / Lifecycle** | **PASS** | `PENDING_TARGETS` vinculado à instância do mundo e com timeout de 15 ticks, expiração por tick e limpeza em `accept(m)` (`try-finally`), `reset()` e rollback. |
| **Ausência de Flash Vermelho** | **PASS** | Pre-strike hint suprime `hurtTime = 10` e evento 2 antes da chegada do pacote de dano vanilla, com prova fotométrica de 0 pixels vermelhos na malha do mob. |
| **Vítima Única** | **PASS** | Execução restrita estritamente ao alvo primário do golpe; danos refletidos e mortes secundárias não acionam confirmação nem ocultam outras entidades. |
| **Katana (HG-F01..F04)** | **PENDENTE** | Mantida intocada e delimitada como pendente de revisão artística do corte / gesto, sem aplicação artificial de partículas para mascarar a geometria. |
| **Integração Git** | **SEM MERGE** | Trabalho mantido isolado na branch `codex/ryujin-bankai-sol-selamento`, sem push, rebase ou merge para a branch principal. |
