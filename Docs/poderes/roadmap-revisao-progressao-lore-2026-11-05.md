# Roadmap revisto — progressão, mentores e lore

**Atualização após merge `6df769a`, 05/11:** [revisão somente de leitura e plano P0 de saves](ryujin-senbon-merge-revisao-roadmap-saves-2026-11-05.md) prevalece nos estados novos. O GDD `origin/main` `9906486` entrou pelo merge da branch de design; a missão de Rukia foi relatada como pronta em outra branch e aguarda auditoria/integração pelo usuário, sem duplicação por esta frente. P0 continua o próximo trabalho próprio de progressão. Hakuteiken v3 requer revisão dos riscos de piso compartilhado, interrupção/voo e prova de combo com asas.

**05/11/2026.** Revisão da branch `Feature-Poderes-bankais` após o commit `617892b`. Este documento separa estado verificado, contratos de design e proposta de sequência; não aprova novas regras, preços, recompensas ou implementação. Nenhum merge de `main`, push ou mudança de gameplay nesta revisão.

## Estado conferido

| Frente | Estado e próximo gate |
|---|---|
| Poderes aceitos | Benihime, Hyōrinmaru, fundamentos, Investida/Rajada Ryūjin e combate Senbon têm aceites registrados no [roadmap 72](72-revisao-geral-roadmap-gamedesign-2026-10-20.md). Preservar as mecânicas aceitas. |
| Ryūjin Bankai | Câmera v8 e sol com efeitos têm [QA técnico](ryujin-camera-v8-qa-2026-11-05.md); a [música candidata](ryujin-musica-candidata-qa-2026-11-05.md) está em pack local ignorado. Escuta, aceite artístico final, licença expressa da música e multiplayer ainda pendentes. |
| Quincy | Vollständig, kit avançado e rig GeckoLib constam da branch; merge do rig `18909a7` em 03/11. Refinamento visual/equilíbrio, storyboard de H/N/B/C, aquisição narrativa e prova de dois clientes continuam separados de implementação básica. Ginrei e destruição do C não estão aprovados. |
| Senbon Garden | Merge `e051e6a` em 03/11 incorporou as correções da frente Garden. O checklist antigo que o chama de não integrado está superado nesse ponto; aceite visual e avaliação integrada seguem sujeitos a evidência específica. |
| Higashi | Aceite específico da coreografia ainda não consta como encerrado no roadmap. Revisar com vídeo/retorno humano antes de reabrir a mecânica. |
| Quest/NPC | Há `QuestService`, recompensas persistentes e `QuestNpcEntity`; isto não prova campanha, mentoria, diálogo e encontro vinculados de ponta a ponta. [Roadmap 126](126-roadmap-personagens-npcs-lore-proximas-entregas-2026-10-26.md) propõe auditoria N0 e a primeira missão MAIN-SUB-01. |
| Eficiência/QA | Continua transversal: retenção de evidências, velocidade de build, saves e testes de multiplayer/dedicado. Não apagar dados por suposição. |

O GDD em `origin/main` (`9906486`, 28/10) consolidou **10 missões do Substituto e 18 da Soul Society**, com Byakuya como último boss deste arco. O checkout atual de poderes contém a versão mais antiga de `Docs/game-design/`; ler o ref remoto para planejar não equivale a mesclar a `main`. Essas 28 estruturas não são quests implementadas. O GDD prevê Arrancar como próximo arco documental, enquanto M11, formação racial, fillers e laterais permanecem adiados até nova direção.

## Contratos de progressão já decididos

- **Sem nível global de personagem.** Jornada, atributos, disciplina/proficiência, vínculo, Mastery, relações/posição e conhecimento são trilhas distintas ([M02](../game-design/02-trilhas-de-progressao.md)). XP vanilla do Minecraft não deve ser confundido com SP ou prática espiritual.
- **Spiritual Points** compram atributos, skills e transformações depois que requisitos/contexto disponibilizam a aquisição. Compra persistente é diferente de custo de uso. **Mastery cresce por prática válida e não é comprável** ([M05/REV-SP-02](../game-design/05-atributos-e-recursos.md)). Preços, fontes, exceções e respec permanecem por ficha.
- Mentor é fonte contextual de repertório/prática, não condição universal para toda técnica; fundamentos autônomos permanecem possíveis ([M08](../game-design/08-npcs-mentores-e-faccoes.md)). Rukia guia o começo de MAIN-SUB-01, sem por isso virar mentora universal ou conceder poder ao falar.
- Abertura MAIN-SUB-01 “Um Hollow na área”: Rukia orienta e o jogador/party vence o encontro vinculado; não foi aprovada recompensa adicional de SP, item, Mastery ou forma ([roadmap 126](126-roadmap-personagens-npcs-lore-proximas-entregas-2026-10-26.md)).

## Distância entre código e design

1. `ResourcesData.trainingPoints` e o HUD já guardam/exibem uma moeda de pontos. `AttributeData` tem **sete IDs compráveis**, enquanto M05 define **cinco atributos universais**; Zanjutsu/Hakuda/Kidō pertencem à discussão de disciplinas. A migração exige mapa de equivalência, save antigo e teste de preservação de investimento antes de alterar campos.
2. `ProgressionService.purchaseSkill` só aceita `zanpakuto`; `SkillsData` tem custos fixos de protótipo. A regra geral de compra de skills/formas do GDD ainda não é um catálogo executável. Não estender preços provisórios a todas as raças.
3. `MasteryPracticeService` já concede prática por ação válida com limite de repetição para Shinigami/Quincy. `TransformationReward` ainda pode elevar Mastery e descobrir forma diretamente; auditar conteúdo que usa essa recompensa antes de aplicar REV-SP-02. `FormData` mantém um campo chamado `passiveMasteryEveryFiveSeconds`, mas a busca de chamadas não mostrou concessão por esse campo; não afirmar ganho AFK sem reprodução.
4. `QuestService` separa aceitar, completar e resgatar recompensas. Falta provar no cliente e em save recarregado o contrato específico de tentativa, alvo, participação e recompensa única de MAIN-SUB-01. A existência de oito NPCs registrados e um painel não prova mentores ou lore jogável.

## Sequência proposta para discussão

**P0 — reconciliação técnica curta.** Auditar dados salvos de pontos, sete atributos, compras, Mastery e quest/NPC, comparando com M02/M05/M06/M07/M08 e o GDD atualizado de `origin/main`. Entregar matriz campo atual → regra desejada → migração → GameTest. Não alterar o save ainda.

**P1 — primeira fatia de progressão.** Implementar apenas uma aquisição demonstrável: fonte/contexto disponibiliza uma skill, SP debita uma vez no servidor, UI mostra requisito/saldo, a capacidade persiste após relog e Mastery aumenta só por prática válida. Tratar atributos legados com migração explícita, sem nível global novo nem conversão automática de XP vanilla. Ficha de skill, custo e fonte ainda precisam de escolha.

**P2 — mentor piloto com função real.** Usar um instrutor compatível já aprovado conceitualmente em M08, com diálogo curto, recusa explicada, exercício verificável e oferta da skill P1. Preferência inicial: instrutor original da Academia para fundamentos Shinigami; preservar a opção de mentor Quincy independente em sua trilha. Não fazer de Rukia uma loja universal nem exigir NPC para cada fundamento.

**P3 — primeira missão de lore.** Auditar N0 e entregar MAIN-SUB-01 com Rukia e Hollow vinculado, retomada após morte/logout, crédito correto ao jogador/party e conclusão/recompensa sem duplicação. Qualquer SP ou Mastery como prêmio precisa de decisão própria; usar a missão para explicar a progressão, sem conceder domínio de forma automática.

**P4 — expansão controlada.** Após a fatia aceita, estender repertórios, origens, facções e capítulos. M11/economia, M15/party e M16/migrações entram quando as fichas precisarem; não começar pelas 28 missões ou por uma cidade inteira.

**Decisão pendente:** priorizar P1–P2 como próximo pacote jogável, com MAIN-SUB-01 logo em seguida, ou fazer N0/MAIN-SUB-01 primeiro e usar a progressão existente provisoriamente. Recomendo P0 → P1/P2 → P3 para que o primeiro mentor e a missão tenham recompensa e domínio coerentes, mantendo o conteúdo narrativo aprovado intacto. A escolha de custo/fonte/primeira skill deve vir de ficha, não deste roadmap.
