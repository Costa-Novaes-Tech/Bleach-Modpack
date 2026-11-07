# P0 — auditoria estática de dados e saves

> **Atualização de contexto, 05/11:** o merge Rukia `9eb6a05` entrou após esta auditoria. A criação Shinigami agora inicia sem espada; MAIN-SUB-01 entrega a Asauchi ao confirmar o diálogo final, conforme M13 revisado em 30/10. O fluxo integrado passou em 295 GameTests e no cliente automatizado, mas o inventário de NBT abaixo foi elaborado em `a2868b3` e precisa de complemento específico para a persistência das etapas/tentativa da nova quest antes de P1. Nenhum save pessoal foi aberto.

**05/11/2026.** Auditoria somente de leitura na `Feature-Poderes-bankais`, HEAD `a2868b3`, árvore limpa no início e ao fim. Nenhum save pessoal foi aberto ou alterado; não houve build, GameTest, migração, merge ou commit nesta etapa. Este inventário consolida o código lido e define o que deverá ser provado com cópias de fixtures.

## Mapa de persistência

O Forge grava a capability `bleachmod:player_data` (`PlayerProvider`), cujo `PlayerData.save()` escreve `schemaVersion=9` e os subcompostos abaixo. `SyncHelper.full()` envia o mesmo payload ao próprio jogador com snapshots adicionais de HUD; `resources()` e `progression()` enviam subconjuntos ao dono; `appearance()` envia visual aos observadores. Snapshots de HUD/cinemática não são a fonte persistida.

| Caminho NBT | Estado persistido | Legado, normalização e próximo contrato |
|---|---|---|
| `guard` | Postura e tempos de recuperação | Save antigo sem guarda inicia com postura cheia. Entrada mantida, ataque segurado não deve reaparecer. |
| `attributes` | Sete ranks: `zanjutsu`, `hakuda`, `vitality`, `resistance`, `kidou`, `reserve`, `control` | `power` antigo vira `zanjutsu` se não houver esse campo. M05 decide cinco atributos universais; Zanjutsu, Hakuda e Kidō não têm conversão aprovada para disciplinas. Preservar investimento antes de remover qualquer ID. |
| `resources` | Stamina, Reiatsu, `actionCharge`, saldo `trainingPoints` (`float`) | Saldo limitado a 0–1.000.000 e valores não finitos saneados. Stamina máxima derivada de Resistência no load; Reiatsu máximo recalculado por `normalize()` no login. SP do GDD corresponde à moeda atual, mas nome/migração ainda exigem decisão. |
| `skills.skillsList` | Nome, rank comprado, estado `active`, máximo | Compra do protótipo `zanpakuto` custa 200/500/1000 por rank e exige forma descoberta; não é catálogo geral de skills aprovado. |
| `character` | Raça, identidade da Zanpakutō, forma selecionada/ativa, Mastery, formas e técnicas descobertas | `techniqueRevision=5`. Há migrações de chaves elementais, Benihime e Hyōrinmaru; saves sem identidade são atribuídos à Ryūjin. Mastery permanece por identidade, sem mover progresso entre Zanpakutōs. |
| `appearance` | Skin/camadas/cor e escolhas visuais | Ausente antes do schema 9; mantém skin Minecraft do jogador. Poses/cinemáticas transitórias não entram no save. |
| `quincy` | Mastery de perfil, técnicas e Vollständig, desbloqueios e alguns estados ativos | IDs de técnicas são semânticos; `vollstandigActive` e arena Sprenger podem ser serializados, enquanto serviço de Vollständig usa sessão em memória. Reentrada após salvamento durante forma ativa requer prova específica. |
| `status` | `hasCreatedCharacter`, `actionCharging` | Cooldowns e efeitos temporários não são restaurados. `normalize()` limpa transformação/carga após login. |
| `playerQuestData` | Quests, desbloqueios de saga, quest acompanhada | Cada `QuestProgress` grava ID, definição estrutural, status, falhas, objetivos/requisitos e índices de recompensas reivindicadas. A definição bloqueia mudança estrutural sem migração; texto pode mudar. |

XP vanilla é estado do Minecraft, separado de `trainingPoints` e de Mastery obtida por prática. A prática autoritativa atual passa por `MasteryPracticeService`, com limites de repetição e exclusão de cheats.

## Rotas e invariantes

| Operação | Caminho verificado | Invariante a testar em fixture |
|---|---|---|
| Criação | `ConfirmCharacterC2S` só confirma Shinigami uma vez; no estado pós-`9eb6a05`, `initializeShinigami()` marca criação sem entregar Asauchi. `RukiaOpeningQuest` dá a espada ao final da missão. | Uma confirmação sem item precoce; entrega única após retorno à Rukia, inclusive com inventário cheio e relog. Quincy inicializável por kit de laboratório não equivale a seleção racial comum. |
| Compra | `PurchaseAttributeC2S` e `UpdateSkillC2S` debitam no servidor | Débito único; saldo e rank coerentes após relog/clone; falha não consome SP. |
| Troca de kit de teste | `DevKitService` arquiva identidade/progresso e limpa combate; `all` concede pontos/Mastery administrativamente | Troca de raça/identidade não expõe forma da anterior nem destrói investimento; distinguir concessão administrativa de aquisição normal. |
| Quest e recompensa | `QuestService` vincula definição, conclui e marca cada claim após `give()`; o merge `9eb6a05` acrescentou etapas/identidade da tentativa Rukia e entrega de Asauchi no retorno. | Recompensa uma vez, inclusive após reload; mudança de definição exige migração explícita. Conferir fixture pós-merge da missão Rukia. |
| Morte/clone | `CapabilityEvents.clone()` copia `ForgeCaps` e limpa estado temporário | SP, ranks, Mastery por identidade, formas e claims preservados; combate/forma ativos cancelados. |
| Dimensão/relog | Reset de estado ligado ao nível; login normaliza, vincula definições e sincroniza | Estado persistente preservado; efeitos e sessão não reaparecem incoerentemente. `ProgressionService.normalize()` atualmente pode preencher `zanpakuto/sealed` num Quincy depois de relog, apesar de o kit Quincy limpar a forma: descompasso de código a reproduzir. |

## Priorização antes de P1

1. **Decisão de produto:** mapa explícito dos sete ranks antigos para os cinco atributos M05 e eventuais proficiências, mantendo saldo e poder investido. Não aplicar conversão silenciosa.
2. **Decisão de produto:** tratamento de `TransformationReward`, que ainda pode elevar Mastery e descobrir forma diretamente, frente a REV-SP-02 (SP compra skill/forma; Mastery vem da prática). Auditar recompensas existentes antes de mudar contrato.
3. **Compatibilidade:** fixture antiga e atual em cópia; roundtrip, clone, relog, dimensão, quest alterada, claim repetido e Quincy ativo. Cobertura estática existente em `MvpRegressionTest`/GameTests não equivale a execução nesta rodada ou prova em save real.
4. **Rukia pós-merge:** MAIN-SUB-01 já está em `9eb6a05`. Complementar matriz de save com etapa, UUID de Rukia, identidade da tentativa, falha, claim de Asauchi e desbloqueio de MAIN-SUB-02. Usar cópias de fixture para relog, morte, dimensão e dois jogadores; não duplicar a quest.

Fontes principais: `src/main/java/com/bleachmod/common/data/{PlayerData,PlayerProvider,AttributeData,ResourcesData,SkillsData,CharacterData,StatusData}.java`, `common/quincy/QuincyData.java`, `common/quest/{PlayerQuestData,QuestProgress,QuestService}.java`, `server/events/CapabilityEvents.java`, `common/ProgressionService.java`, `server/commands/DevKitService.java`, `src/test/java/com/bleachmod/MvpRegressionTest.java`, `Docs/game-design/05-atributos-e-recursos.md` (REV-SP-02).
