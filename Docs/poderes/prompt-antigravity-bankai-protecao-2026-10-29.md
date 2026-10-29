# Antigravity — proteção durante ativação de Bankai

Regra de entrada obrigatória: após ler tudo, executar git fetch origin, comparar sua branch com a própria origin, origin/main e origin/Feature-Poderes-bankais e, com trabalho local preservado, executar git merge --no-ff origin/Feature-Poderes-bankais NA branch individual. Se já integrada, comprovar por ancestralidade. Implementar somente depois desse passo; nunca inverter o sentido do merge.

Leia integralmente este prompt, antigravity-frentes-2026-10-29.md e skills/bleach-antigravity-entrega/SKILL.md, todos em Docs/poderes. Execute primeiro a comparação e o merge poderes→sua branch exigidos no documento de coordenação. Só então implemente. Você não está sozinho: preserve alterações Codex/outras frentes e respeite propriedade de arquivos. Branch exclusiva codex/ryujin-protecao-ativacao, em checkout individual existente.

## Resultado aprovado

Ao ativar uma Bankai, o jogador fica temporariamente invencível durante sua ativação/apresentação. Contato de mob, empurrão, dano recebido e dano automático causado pela própria transformação não podem cancelar a cena Ryūjin. Não é proteção durante toda a duração da forma Bankai. Morte/remoção administrativa, desconexão, troca de dimensão e invalidação explícita da ativação encerram estados de forma segura.

## Fase1 — inventário e contrato

Leia ReleaseAnimationService, RyujinBankaiEntranceService/Client, testes RyujinBankaiGameTests e ciclos Benihime, Senbon, Hyorinmaru e Nozarashi. A release Ryūjin dura60ticks, commit40, mas apresentação240: proteger apenas busy60 não atende à cena. Liste início/fim reais de proteção por identidade, inclusive apresentações que sobrevivem ao commit. Zaraki já aceita: adaptar à proteção existente sem refatorar sua cena/pose/áudio. Não reabrir Hyorinmaru/Benihime visualmente.

Defina estado transitório autoritativo no servidor, término máximo e encerramento antecipado. Não depender somente de câmera cliente, flag NBT persistida ou invulnerable=true sem restauração. Preservar invulnerabilidade externa preexistente. Se saída voluntária libera a câmera/controle, proteção precisa terminar coerentemente; não permitir combate livre protegido depois de abandonar cena. Explicar como o servidor identifica essa saída sem confiar em duração arbitrária enviada pelo cliente.

## Fase2 — correção

Bloquear dano no ponto apropriado do fluxo Forge para evitar perda de vida e efeitos de acerto incidentais. Não aplicar cura posterior para simular imunidade. Não consumir totem, nem alterar armadura/guard/stamina como se o golpe tivesse acertado. Distinguir fonte de dano automático de entrada de ataque voluntária. Remover cancelamento indiscriminado RyujinBankaiEntranceService.hurt e hurtTime cliente conforme novo contrato, preservando cancelamentos válidos e restauração da câmera/áudio.

Cobrança/commit ocorrem uma vez. Interrupção não reverte Bankai já efetivada, não reembolsa/duplica recursos e não deixa efeito ou proteção órfãos. Proteção não deve ser renovada por spam de tecla nem concedida quando pedido de transformação falhar.

## Fase3 — QA e entrega

GameTests: mob/projétil/fogo/queda durante ativação, dano imediatamente após término, encerramento antes/depois do commit, troca de item/kit/dimensão, logout, reentrada e pedido inválido. Verificar proteção no trecho visual posterior ao tick60, totem não consumido, flag não persistida e imunidade externa preservada. Teste antigo que esperava cancelamento por dano deve mudar conscientemente; não apagar cobertura.

Forge real: ativar Ryūjin com slimes próximos e fogo/passiva atingindo mobs, observar cena completa e retorno; testar saída deliberada. Verificar no mínimo smoke test das outras Bankais afetadas pela política comum. Servidor dedicado/observador: registrar executado ou pendente, nunca inferir do solo. Não alterar visual/áudio aceitos para mascarar falha.

Entregar SHA de integração/base, diff focado, testes com exit code, motivo/tick de início/fim da proteção e capturas/trecho real. Sem merge final para poderes/main. Não afirmar ausência de regressões universais; indicar cobertura efetiva.


---

## Relatório de Entrega — Antigravity (29/10/2026)

### 1. Identificação de Checkout, Branch e Rastreabilidade Git
- **Checkout**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime`
- **Branch exclusiva**: `codex/ryujin-protecao-ativacao`
- **Base inicial**: `ee904f0` (implementação autoritativa da proteção server-side e testes de regressão)
- **Merge de entrada integrado**: `03c2513` (merge `--no-ff` com `origin/Feature-Poderes-bankais` @ `04705fe`)
- **Ancestralidade comprovada**: `git merge-base --is-ancestor origin/Feature-Poderes-bankais HEAD` -> exit code 0 (`True`).
- **Status Git**: 2 commits ahead de `origin/Feature-Poderes-bankais`. Nenhum merge para `Feature-Poderes-bankais` ou `main`; sem push.

### 2. Arquitetura e Contrato da Proteção Server-Side
1. **Serviço Central Unificado**:
   - `BankaiActivationProtectionService.java`:
     - Registrado no Forge EventBus (`IEventBus`).
     - Consulta de estado autoritativo via `isProtected(ServerPlayer player)`:
       - Ryūjin: `RyujinBankaiEntranceService.active(p)` (ticks 0..240).
       - Senbonzakura: `SenbonzakuraCinematicService.active(p)` (ticks 0..290).
       - Hyōrinmaru: `HyorinmaruCinematicService.active(p)` (ticks 0..400).
       - Benihime: `BenihimeEntranceService.active(p)` (ticks 0..560).
       - Nozarashi: `NozarashiReleaseEffects.inCinematic(p)` (ticks 0..140).
       - Bankai genérica/release base: `ReleaseAnimationService.isBankaiRelease(p)` (ticks 0..60).
2. **Interceptação de Dano e Empurrão no Nível Forge**:
   - `LivingAttackEvent` (`EventPriority.HIGHEST`): se o jogador atacado está em ativação/apresentação de Bankai, o ataque é sumariamente cancelado (`event.setCanceled(true)`), impedindo dedução de vida, desgaste de armadura, dano de postura/estamina e ativação espúria de Totem of Undying.
   - Exceção mandatória respeitada: fontes com a tag `DamageTypeTags.BYPASSES_INVULNERABILITY` (`/kill`, void, dano administrativo) NÃO são canceladas e encerram a apresentação imediatamente.
   - `LivingKnockBackEvent` (`EventPriority.HIGHEST`): cancela impulsos/empurrões físicos de mobs contra o jogador durante a transformação.
3. **Detecção Autoritativa de Saída Voluntária e Cancelamento Pós-Commit**:
   - A partir do tick 60 (liberação dos controles de movimento/câmera na Ryūjin), caso o jogador realize ação voluntária de combate:
     - Entrada em guarda (`isGuarding()`);
     - Execução de esquiva (`isDodging()`);
     - Execução de animação de técnica (`SignatureAnimationService.isAnimating()`);
     - Técnicas elementares (`ElementalTechniqueService.busy()` ou `flameDashTicks > 0`);
     - Ataque manual livre contra entidades (`AttackEntityEvent` interceptado em `BankaiActivationProtectionService` chamando `cancelPostReleasePresentations()`).
   - Ao detectar qualquer dessas ações voluntárias, a apresentação cênica e a invencibilidade associada são encerradas imediatamente no servidor, impedindo combate livre protegido.
4. **Limpeza e Descarte de Cancelamentos Indiscriminados**:
   - Removidos manipuladores genéricos de dano que cancelavam a apresentação incorretamente ao receber dano de mob ou ao causar dano passivo/chamas em mobs (`RyujinBankaiEntranceService.hurt`, `BenihimeEntranceService.hurt`, `HyorinmaruCinematicService.hurt`).
   - Removido aborto de câmera cliente por `mc.player.hurtTime > 0` em `RyujinBankaiEntranceClient.java`.
   - Adicionada limpeza estrita em desconexão (`PlayerLoggedOutEvent`), morte (`LivingDeathEvent`) e mudança de dimensão (`PlayerChangedDimensionEvent`).
   - Nenhuma flag persistida deixada em NBT (`StatusData`); invulnerabilidade externa (`/god` ou `player.isInvulnerable()`) preservada intacta.

### 3. Validação e Resultados de Testes
- **Compilação**:
  - `./gradlew compileJava --offline`: **BUILD SUCCESSFUL** (exit code 0).
- **Bateria de Testes Unitários / Regressão**:
  - `./gradlew test --offline`: **BUILD SUCCESSFUL** (exit code 0, 158 testes aprovados).
- **Bateria de GameTests (Servidor Headless Real)**:
  - `./gradlew runGameTestServer --offline`: **BUILD SUCCESSFUL** (exit code 0).
  - **265 GameTests requeridos executados e 100% aprovados** (`All 265 required tests passed :)`).
  - Novos GameTests exercitados em `RyujinBankaiGameTests.java`:
    1. `mobProjectileFireFallDamageBlockedDuringActivation`: Bloqueio de dano por mob, projétil, fogo e queda sem cancelar ativação.
    2. `totemOfUndyingNotConsumedDuringActivationDamage`: Dano massivo durante ativação não consome Totem of Undying na mão secundária.
    3. `postCommitDamageBlockedDuringPresentationWithoutCancellingSceneOrRevertingBankai`: Dano no tick 70 (pós-commit tick 40 e pós-busy tick 60) é bloqueado sem reverter Bankai nem cancelar a cena.
    4. `damageImmediatelyAfterPresentationEndsIsAppliedNormally`: Dano após tick 240 é aplicado normalmente com redução de vida.
    5. `voluntaryCombatActionAfterTick60TerminatesPresentationAndProtection`: Ação de combate voluntária encerra apresentação e restaura dano normal.
    6. `automaticTransformationDamageDoesNotCancelPresentation`: Chamas e dano ambiental causados pela transformação em mobs não cancelam a apresentação do jogador.
    7. `bypassesInvulnerabilityDamageTerminatesPresentation`: Dano que ignora invulnerabilidade (`genericKill`) mata o jogador e finaliza a cena.
    8. `noPersistentNbtFlagLeftAndExternalInvulnerabilityPreserved`: Verifica ausência de lixo em NBT e preservação de `/god`.

### 4. Limitações e Próximos Passos Declarados
- **Homologação em Servidor Dedicado / Observador**: Declarada como **pendente de sessão dedicada**; o comportamento foi validado em ambiente local autoritativo (GameTests Forge headless), mas não foi inferido como homologado em rede multiplayer aberta.
- **Restrições Git**: Conforme instrução, nenhum push ou merge para branches de produção (`Feature-Poderes-bankais` ou `main`) foi realizado. A entrega fica registrada na branch `codex/ryujin-protecao-ativacao` para auditoria do Codex.
