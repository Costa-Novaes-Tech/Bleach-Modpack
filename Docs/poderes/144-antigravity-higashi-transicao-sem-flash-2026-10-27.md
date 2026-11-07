# 144 — Relatório Antigravity: Transição Sem Flash, Supressão Netty e Alvo Único

**Data**: 27/10/2026  
**Autor**: Antigravity  
**Branch**: `codex/ryujin-bankai-sol-selamento` (checkout `Bleach-Modpack-benihime`)  
**Base**: Ciclo 141 (`8f4115b`), Prompt 143  
**Status**: CONCLUÍDO (Passes: 233 GameTests, 149 Regressão, 5/5 QA Cliente Real) — **NO MERGE / NO PUSH**

---

## 1. Diagnóstico e Causa Raiz da Regressão (Ciclo 141)

Na rodada de evidências do Ciclo 141, o teste `repeatHusk` revelou que, no tick `elapsed 37`, a vítima exibiu um flash vermelho de dano vanilla em primeira pessoa antes de a câmera lateral assumir no tick `elapsed 39`.

A auditoria no ciclo de eventos do Minecraft Forge 1.20.1 identificou três causas interconectadas:

1. **Inversão na Ordem de Pacotes da Morte**:
   - Anteriormente, o dano letal de `hit()` chamava `MasteryPracticeService.hurt` $\rightarrow$ `LivingEntity.die` $\rightarrow$ `LivingEntity.actuallyHurt`.
   - O servidor transmitia os pacotes vanilla `ClientboundDamageEventPacket` e `broadcastEntityEvent(3)` (morte) **antes** de `HigashiExecutionService.confirmedDeath` disparar `HigashiExecutionS2C`.
   - Na fila do Netty e do renderizador cliente, a entidade recebia primeiro o evento de dano (ativando `hurtTime = 10` e o flash vermelho nos shaders de renderização) e o evento 2/3.
2. **Latência de Agendamento `enqueueWork`**:
   - `HigashiExecutionS2C.handle` delegava a supressão ao agendador do cliente via `ctx.get().enqueueWork(...)`. Durante a lacuna de 1 a 2 ticks/frames até o loop de tick do Minecraft processar a fila, `shouldSuppressEntity(id)` retornava falso, renderizando a entidade original com a textura vermelha de dano na câmera em primeira pessoa.
3. **Incompatibilidade de Indexação Caster vs Vítima em `ElementalActionVisuals`**:
   - Em `ElementalActionVisuals.gesture(p)`, a condição de cedência chamava `HigashiExecutionVisuals.isExecuting(p.getId())`.
   - O mapa `BURNS` armazena o ID da vítima (`m.target()`), enquanto o atacante fica no mapa `POSES` (`m.owner()`). Como resultado, `isExecuting(p.getId())` retornava falso para o atacante, gerando conflito de renderização e pose.

---

## 2. Correções Implementadas

### 2.1 Inversão de Prioridade no Servidor via `LivingDeathEvent`
- Em `com.bleachmod.common.technique.ElementalTechniqueService`:
  - Introduzido `ThreadLocal<Cast> ACTIVE_CAST` em `effect(Cast c)` para demarcar a execução de técnicas em andamento.
  - Subscrito o evento `higashiDeath(LivingDeathEvent e)`: no Forge 1.20.1, `LivingEntity.die()` invoca `ForgeHooks.onLivingDeath` logo na primeira linha, antes do envio de qualquer pacote de morte (`broadcastEntityEvent(3)`) ou dano vanilla.
  - Ao detectar morte letal causada por Higashi, dispara `HigashiExecutionService.confirmedDeath` imediatamente.
- Em `com.bleachmod.common.technique.HigashiExecutionService`:
  - Adicionada guarda persistente `target.getPersistentData().getBoolean("bleachmod_higashi_confirmed")` para garantir idempotência estrita (evitando broadcast duplicado caso múltiplos triggers de morte ocorram).

### 2.2 Supressão Instantânea na Thread Netty do Cliente
- Em `com.bleachmod.client.model.HigashiExecutionVisuals`:
  - Criado conjunto concorrente `PENDING_TARGETS` e métodos thread-safe `notePendingTarget(int id)`, `removePendingTarget(int id)`, `isPendingTarget(int id)`.
  - Atualizado `shouldSuppressEntity(id)` para suprimir entidades tanto se estiverem ativas em `BURNS` quanto pendentes em `PENDING_TARGETS`.
  - Exposto `isPerformingExecution(int entityId)` checando `POSES` (atacante) para desacoplar a verificação do atacante da verificação da vítima.
- Em `com.bleachmod.common.network.s2c/HigashiExecutionS2C`:
  - Na chegada do pacote pelo Netty (`handle`), chama imediatamente `HigashiExecutionVisuals.notePendingTarget(m.target())` **antes** de despachar `enqueueWork`, bloqueando qualquer frame intermediário de renderizar a entidade original em estado de dano.

### 2.3 Supressão Completa de Danos e Morte Vanilla via Mixin
- Em `com.bleachmod.mixin.client.HigashiDeathParticleMixin`:
  - Expandido `@Inject(method="handleEntityEvent")` para interceptar e cancelar os eventos `2` (hurt), `3` (death tilt) e `60` (fumaça vanilla de morte) sempre que `shouldSuppressEntity(id)` for verdadeiro.
  - Injetado em `handleDamageEvent(HEAD)` e `animateHurt(HEAD)` para cancelar `hurtTime = 10` e rotações de recuo de dano vanilla, eliminando completamente o tom avermelhado pré-câmera.

### 2.4 Alinhamento e Cedência de Poses do Atacante
- Em `com.bleachmod.client.model.ElementalActionVisuals`:
  - Atualizada a linha 48 para consultar `HigashiExecutionVisuals.isPerformingExecution(p.getId())`, garantindo que o gesto elemental do jogador ceda corretamente durante a animação de execução.

### 2.5 Refatoração do Harness de QA e Telemetria Real
- Em `com.bleachmod.client.cinematic.CinematicQaCommands`:
  - Limpeza automática de blocos residuais de fogo no raio de 8 blocos em `cleanTargets`.
  - Separação explícita dos comandos e cenários no dispatcher do cliente:
    - `/cinematicqa higashi lethal_husk`
    - `/cinematicqa higashi lethal_flying`
    - `/cinematicqa higashi threshold_real`
    - `/cinematicqa higashi survive`
    - `/cinematicqa higashi miss`
- Em `com.bleachmod.client.cinematic.CinematicQaSequence`:
  - Sequenciador automatizado executando os 5 cenários isolados.
  - Telemetria real injetada em cada frame de `frames.jsonl`: `burnAge`, `poseAge`, `targetId`, `originalPresent`, `originalSuppressed`.

---

## 3. Evidências de Validação

### 3.1 GameTests Autorizados
Execução: `.\gradlew.bat runGameTestServer --offline --console=plain`
- **Resultado**: `BUILD SUCCESSFUL in 4m 28s`
- **Total de Testes**: 233 GameTests executados
- **Veredito**: `All 233 required tests passed :)` (incluindo `onlyOneLowHealthVictimGetsExecutedPerHigashi`, `execution_threshold`, `berserk_terrain`, etc.).

### 3.2 Suíte de Regressão e Build
Execução: `.\gradlew.bat check build --offline --console=plain`
- **Resultado**: `BUILD SUCCESSFUL in 52s`
- **Total de Cenários**: 149 cenários de regressão (`MvpRegressionTest`)
- **Veredito**: `PASS: 149 regression scenarios`, `check` e `build` com sucesso integral.

### 3.3 Teste Gráfico em Cliente Real Forge (`runClient`)
Execução:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot'
.\gradlew.bat runClient -PbenihimeQa=true -PcinematicQa=true -PhigashiPoseQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain
```
- **Resultado**: `BUILD SUCCESSFUL in 2m 22s`
- **Resultados da Sequência Automatizada (Log Oficial)**:
  1. `Cinematic QA 71 prepared lethal_husk owner=1 victim=3 health=10.0/20.0`
     - `Cinematic QA finished lethal_husk victimPresent=false victimAlive=false expectedAlive=false passed=true`
     - `Cinematic QA 77 camera scenario=lethal_husk seen=true expected=true passed=true`
  2. `Cinematic QA 71 prepared lethal_flying owner=1 victim=12 health=10.0/20.0`
     - `Cinematic QA finished lethal_flying victimPresent=false victimAlive=false expectedAlive=false passed=true`
     - `Cinematic QA 77 camera scenario=lethal_flying seen=true expected=true passed=true`
  3. `Cinematic QA 71 prepared threshold_real owner=1 victim=21 health=7.0/100.0`
     - `Cinematic QA finished threshold_real victimPresent=false victimAlive=false expectedAlive=false passed=true`
     - `Cinematic QA 77 camera scenario=threshold_real seen=true expected=true passed=true`
  4. `Cinematic QA 71 prepared survive owner=1 victim=30 health=1000.0/1000.0`
     - `Cinematic QA finished survive victimPresent=true victimAlive=true expectedAlive=true passed=true`
     - `Cinematic QA 77 camera scenario=survive seen=false expected=false passed=true`
  5. `Cinematic QA 71 prepared miss owner=1 victim=32 health=1000.0/1000.0`
     - `Cinematic QA finished miss victimPresent=true victimAlive=true expectedAlive=true passed=true`
     - `Cinematic QA 77 camera scenario=miss seen=false expected=false passed=true`

### 3.4 Auditoria Fotométrica de Transição Pré-Câmera
- Inspeção pixel a pixel do quadro de transição imediata anterior ao DRAW (`frame-0015-t0032-WAIT_IMPACT.png` $\rightarrow$ `frame-0016-t0034-DRAW.png`):
  - **Pixels avermelhados de dano no bounding box do alvo**: `0` (Zero).
  - **Supressão do alvo original**: `supp=True` ativo continuamente desde a recepção do pacote Netty até o fim da execução.
  - **Transição de POV**: câmera em primeira pessoa preservada até o início de DRAW (`t=34`), alternando para a tomada lateral limpa sem entrada tardia do alvo ou rotação de morte vanilla.
  - **Variante Flying Hollow**: asas preservadas durante DRAW/CUT/HOLD com `origSuppressed: True`.

---

## 4. Veredito e Próximos Passos

| Requisito | Status | Observação |
| :--- | :---: | :--- |
| **Transição anterior à câmera** | **PASS** | Supressão instantânea via Netty + Mixin de eventos de dano; zero flash vermelho. |
| **Vítima única e supressão** | **PASS** | Suprimida no renderizador sem afetar outras entidades ou causar desaparecimento errôneo. |
| **Cenários de limiar real e controles** | **PASS** | `threshold_real` ($\le 7\%$), `survive` (1000 HP) e `miss` funcionando conforme especificação. |
| **Katana e rig visual HG-F01..F04** | **PENDENTE** | Aguarda congelamento dos storyboards/VFX da direção artística para ajustes finais da lâmina. |
| **Integração / Git** | **NO MERGE** | Commit focado delimitado apenas na branch local `codex/ryujin-bankai-sol-selamento`. Sem push, rebase ou merge para main/Feature. |
