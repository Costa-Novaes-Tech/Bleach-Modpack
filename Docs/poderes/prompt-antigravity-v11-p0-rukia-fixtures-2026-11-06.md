# Prompt — Antigravity B: P0 pós-Rukia, persistência e preparação de lore

Audite a persistência e o fluxo integrado de MAIN-SUB-01 para liberar futuras features de progressão/mentoria. A missão **já está implementada e mesclada por `9eb6a05`**; não criar outra. **Você não está sozinho no código:** preserve alterações de outras conversas, sem revertê-las ou adaptar o projeto destruindo trabalho existente.

Leia continuidade, `Docs/poderes/p0-auditoria-dados-saves-2026-11-05.md`, `roadmap-revisao-progressao-lore-2026-11-05.md`, plano v11 de testes/divisão e handoff GDD. Consulte M05/REV-SP-02, M08 e M13 para os contratos específicos. Confirme branch/HEAD/status/worktrees. Trabalhe em branch/checkout próprio acordado, contendo Rukia; nunca main, reset/stash ou merge de árvore ocupada. Se a base/documentação não estiver disponível, reporte e avance só nas partes independentes.

## Responsabilidade exclusiva

- Relatórios e proposta de fatia futura: `Docs/poderes/antigravity-p0-rukia-*`.
- Fixtures sintéticas e scripts próprios: `tools/qa/fixtures/antigravity-p0/` e `tools/qa/antigravity_p0/`.
- Se necessário, **nova** classe de GameTests `src/main/java/com/bleachmod/gametest/P0RukiaPersistenceGameTests.java`, verificando antes colisão de nome. Não editar `MvpRegressionTest`, os testes Ryūjin compartilhados nem configurações de execução comuns.
- Código produtivo de quests/progressão/persistência, esquemas e regras de recompensa permanece somente leitura. Descobertas viram reprodução e proposta de patch separada para revisão. Não editar Ryūjin, VFX, luz, cosméticos ou outros poderes.

## Regras de produto a preservar

Sem nível global de personagem. SP compra atributos/skills/formas quando os requisitos disponibilizam a compra; Mastery vem de prática válida, não de compra. Cinco atributos universais no GDD, sete ranks legados no código: **a conversão ainda não está decidida**. Não converter XP vanilla em SP, zerar investimento ou inventar fonte/custo de pontos.

Rukia orienta e, no diálogo final implementado, entrega Asauchi e libera MAIN-SUB-02. Não acrescentar SP, Mastery, itens, mentoria universal ou formas. MAIN-SUB-02 constar disponível não significa que seu gameplay completo esteja pronto. As 28 missões do GDD não são conteúdo implementado.

## Auditoria e provas

1. Atualizar o mapa estático P0 para a base pós-Rukia: etapa da quest, vínculo de NPC/alvo/tentativa, falha, progresso por jogador, entrega/reivindicação e desbloqueio seguinte. Ligar cada invariante a código e campo persistido; separar dados salvos de snapshots transitórios.
2. Criar fixtures sintéticas de primeiro acesso, quest aceita, alvo ativo, vitória sem retorno, diálogo final/Asauchi já entregue e tentativa falhada. Escrever mundo/capability somente em diretório descartável do próprio checkout. Cópias de saves pessoais só se forem especificamente fornecidas/autorizadas; não acessar nem subir mundos reais para cloud.
3. Provar save/load e clone/morte sem perda de saldo/ranks/Mastery/claims. Provar que relog entre etapas e reinício de mundo não repetem recompensa nem esquecem identidade de tentativa. Alterar dados de fixture com descrição explícita não equivale a validar migração de um save real.
4. Provar claim repetido, inventário cheio, retomada depois de derrota/saída, troca de dimensão e NPC/alvo ausente. Em coop, dois jogadores e participação/crédito individual; não aceitar um teste fake como prova de duas conexões reais. Não inventar regra de party se o contrato não a decidir: relatar observado, contrato e diferença.
5. Reproduzir a suspeita do P0 sobre normalização Quincy/sealed após relog; separar concessões administrativas do kit de aquisição normal. Auditar usos reais de `TransformationReward`; não alterar recompensas/Mastery antes de decisão de produto.
6. Executar somente verificações adequadas. GameTests em mundo descartável; cliente com encerramento/reabertura para alegar relog real. Registrar comandos, JAR/assets, hashes, branch/HEAD, exit codes e arquivos brutos. Antes de afirmar teste real, conferir o diretório efetivo; init script pode não prevalecer sobre o ForgeGradle.

## Preparação de uma feature seguinte

Após o inventário, preparar **proposta documental**, em Docs/poderes, de uma fatia P1/P2: uma fonte contextual disponibiliza uma skill de fundamento, servidor debita SP uma vez, UI explica requisitos, aquisição persiste e Mastery cresce por uso válido. Instrutor original da Academia é candidato já discutido; não implementar mentor novo nesta auditoria. Listar escolhas ainda necessárias: skill, fonte, preço, conversão de ranks e tratamento das recompensas legadas. Não preencher essas decisões com números ficticiamente aprovados.

## Entrega e limites

Matriz caso/entrada/contrato/observado/evidência e riscos P0/P1. Relatar falhas reproduzíveis com prioridade e patch proposto, sem aplicar correções produtivas por este prompt. Commits locais somente dos seus testes/docs quando autorizados no encaminhamento; não incluir trabalho alheio. **Sem merge automático, push, alteração de schema, migração de saves, publicação ou mensagens externas.**

Cloud pode fazer leitura e executar testes compatíveis sobre fonte/fixtures sintéticas disponibilizadas. Não alegar MCP Blockbench, GPU, cliente gráfico Windows ou áudio local acessíveis sem verificar. Não iniciar outro cliente/Gradle pesado simultaneamente na máquina que estiver em uso pelo Codex. Ao terminar, registrar lições verificadas nas skills apropriadas, preservando as edições das outras frentes.
