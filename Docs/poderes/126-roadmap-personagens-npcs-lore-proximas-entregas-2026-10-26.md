# 126 — Roadmap futuro: personagens, NPCs, encontros e lore

**29/10 — adiamento explícito:** usuário decidiu deixar a quest Rukia para depois e antecipar expansão Ishida/TYBW. Preservar planejamento narrativo abaixo, sem iniciar campanha nesta rodada.

26/10/2026. Branch Feature-Poderes-bankais. Revisão documental solicitada pelo usuário; não implementa gameplay, não executa Minecraft e não altera main. Mudanças documentais anteriores123–125 preservadas.

## 1. Estado e fontes

A lista obrigatória do123 continua;124 e125 são as frentes paralelas autorizadas. Esta revisão prepara a etapa de conteúdo do mundo, sem declarar aprovadas novas missões, personagens por era ou recompensas.

Fontes locais consultadas: Mapa modular e handoff GDD; M03 Storyline macro; M08 e catálogo08a; cabeçalhos/contratos vigentes de M09 e M10; plano-npcs-quests e arquitetura03. Conferência dirigida do runtime: QuestNpcEntity, QuestRegistry, QuestParser, NpcQuestScreen, QuestActionC2S, ModEntities e entidades Hollow listadas. Não é auditoria integral do motor de quests nem teste funcional.

Há oito registros de NPCs de quests em ModEntities: Sado, Ichigo, Orihime, Uryū, Rukia, Byakuya, Urahara e Ulquiorra. Identidades registradas não provam skins/modelos adequados, diálogo narrativo, combate, rotina ou disponibilidade por era. O plano de03/10 confirma a fundação e mantém a proposta original abaixo como histórico. Arquitetura03 contém extensa referência Dragon Mine Z: não importar suas regras, exemplos, dailies, aliases, TPS ou comportamento de party como implementação atual Bleach sem conferir código.

Atualização de 28/10: fontes autoritativas consultadas diretamente em origin/main, commit 9906486, especialmente 00-handoff-atual.md, 00-fechamento-roteiro-substituto-soul-society-2026-10-28.md e M13. A campanha principal passou a ser a prioridade documental; aprofundamento M11, formação racial e conteúdo secundário estão adiados. A seleção/estrutura aprovada contém 10 missões Substituto e 18 Soul Society, mas M13 continua em especificação: roteiro aprovado não significa runtime implementado. O próximo arco a detalhar no GDD é Arrancar. A antiga indicação de começar por M11 foi superada.

## 2. Contratos que o roadmap preserva

- Jogador original; não reproduzir a biografia de Ichigo como identidade obrigatória. Mundo compartilhado, era do mundo e progresso pessoal/racial distintos.
- NPC de atendimento, mentor, autoridade e oponente são papéis separados. Derrotar representação de combate não remove automaticamente prestador indispensável.
- Fontes de aprendizado não exigem um NPC por técnica; fundamentos autônomos continuam possíveis. Mentor/Proficiências runtime aguardam confirmações já registradas.
- Spiritual Points compram skills/formas conforme REV-SP-02; Mastery vem de prática. A recompensa narrativa não concede domínio automaticamente. Preços/fórmulas não definidos permanecem abertos.
- Visão espiritual, leitura de Reiatsu e capacidade de dano são distintas. Civil não vê alma por associação. Não corrigir isso dando a todo NPC sentidos/dano espirituais.
- Provas e bosses canônicos são vencidos pelo jogador/party conforme STR-03, sem aliados canônicos combatendo para entregar a vitória. Representações narrativas/diálogo não autorizam companheiros invencíveis.
- Geografia, era e acesso seguem M10 e tipo de mundo; não presumir mapas espirituais instalados em todo mundo comum. Proteções de construções e estado de combate não podem ser ignorados por quest/portal.
- Aizen como kit de poderes segue adiado; sua existência no horizonte narrativo não autoriza implementar esse kit. Gin permanece fora do planejamento de poderes; Zaraki preservada.

## 3. Sequência recomendada de entregas futuras

### N0 — reconciliação do que já existe

Antes de novo NPC, testar um registro existente de ponta a ponta: spawn/persistência, identificação/skin, interação, lista filtrada de quests, aceitação autorizada, objetivo, entrega e recompensa única. Conferir distância, origem, dimensão e ID no servidor; não confiar no painel cliente. Mapear saves/JSON de mundo versus datapacks e migrações realmente suportadas. Checar trabalho Hollow da equipe para reaproveitar entidades existentes sem criar duplicatas.

Saída: lista implementado/testado/ausente e uma sessão reproduzível. Não usar contagem de classes como aceite.

### N1 — abertura aprovada: MAIN-SUB-01, “Um Hollow na área”

Substitui a proposta editorial PILOTO-01/02/03. Seguir M13/10.3 e 10.32: Rukia próxima da área válida, introdução breve e botão deliberado de iniciar. Um Hollow específico vinculado à tentativa; matar um Hollow aleatório não substitui o objetivo. Rukia orienta, sem combater ou entregar vitória. Conclusão automática após vitória válida, sem retorno obrigatório para receber prêmio; disponibilizar MAIN-SUB-02 sem gerar automaticamente outro encontro. Nenhuma recompensa adicional de SP, item, maestria ou Bankai foi autorizada por essa abertura.

Antes de implementar: fechar acesso racial/contexto, área e ocupação, identidade/perfil do inimigo, contribuição em grupo, afastamento/abandono, morte/logout e retomada. Nome e visual exatos do primeiro Hollow não estão confirmados: entidade existente pode servir de placeholder explicitamente identificado, sem inventar fidelidade canônica. Wipe encerra tentativa; repetição autorizada cria encontro novo sem duplicação por cliques, reset indevido de terreno ou cura de consequências persistentes.

Conferência dirigida do runtime em 28/10: QuestService já separa conclusão e claim de recompensas; QuestEvents.onEntityKill parte do killer. Isso não comprova vínculo único de encontro nem participação de party. Auditar objetivos, persistência e autorização antes de ampliar o motor; não substituir a infraestrutura existente por outro framework.

### N2 — NPCs com presença e função

Refinar primeiro os NPCs realmente usados pelo piloto: silhueta, roupa em camadas, cabelo/acessórios, postura e animações curtas de atenção, fala, apontar e despedida. Blockbench/GeckoLib quando o modelo justificar; renderer vanilla existente pode servir à primeira prova. Não exigir novo framework de diálogo ou IA para apenas abrir conversa.

Diálogos por situação: primeira visita, missão ativa, retorno, recusa com motivo e próximo passo. Traduções/legendas separadas; voz é melhoria posterior, nunca única fonte da informação. Não repetir cinematográfica longa para atendimento recorrente. Textos completos são trabalho M13, opções de UI M14.

Separar comerciante/guia estável de entidade de combate. Rotina de olhar, gestos e deslocamentos limitados pode vir antes de patrulhas complexas; navegação não deve tirar o prestador do local obrigatório ou perder sua identidade após reload.

### N3 — adversários e provas de combate

Aproveitar Hollows da equipe: percepção apropriada, seleção de alvo, aviso de ataque, compromisso direcional, recuperação e limite de perseguição. Ataques legíveis têm preparação/contato/recuperação, sem acertar através de parede por seguir o jogador instantaneamente.

Progressão recomendada: um inimigo básico bem validado; variante de função distinta; um encontro de prova; só depois boss elaborado. Cada um precisa de objetivo de combate distinto, não apenas mais HP. Fichas e balanceamento M09 precedem importar todo elenco como chefes. Duelo de treino com NPC original pode validar sistemas antes de adaptar capitão canônico, conforme aprovação da ficha.

Despawn, limite da arena, wipe, reset, crédito, loot e retomada precisam de contrato. Solo não valida party. Não adicionar destruição do cenário sem regra de proteção específica.

### N4 — núcleo de mundo funcional

Conectar NPCs e quests a um núcleo pequeno de Karakura/Mundo Humano ou local já instalado e aprovado, com pontos de orientação e rota de ida/retorno segura. Expandir Rukongai/Seireitei/Hueco Mundo quando acesso e presença por origem estiverem especificados.

Locais precisam de função: atendimento, recuperação, investigação, treino, encontro, passagem. Evitar cidade gigante vazia antes de uma rua jogável completa. Não substituir construções do usuário, forçar chunks sempre ativos ou assumir sistema de instâncias não aprovado.

### N5 — capítulos e facções

Após piloto aprovado, fichar pequenos capítulos encadeados: conflito local, relação com instituição, dever/prova, nova rota e encontro marcante. Papéis candidatos para conteúdo futuro: Rukia orientação/contexto; Urahara atendimento e investigação; Byakuya autoridade/prova em contexto adequado; Uryū conteúdo Quincy conforme contrato próprio; contatos originais para continuidade onde elenco canônico estiver indisponível. Não conceder a esses candidatos currículo universal, disponibilidade permanente ou quest específica sem ficha.

Relações/facções devem alterar acesso, dever ou diálogo com efeito compreensível. Evitar barra de reputação sem função. Cargo não é raça nem domínio de técnica. Expandir caminhos Hollow/Quincy/Fullbringer com experiências próprias, sem Academia Hollow artificial ou repetição do prólogo Shinigami com nomes trocados.

### N6 — narrativa compartilhada e produção contínua

Tratar catch-up, jogadores em fases distintas, join/leave de party, contribuição real, recompensas pessoais, repetição de encontro e avanço da era com M15/M16. Um jogador finalizar arco não deve sobrescrever silenciosamente a missão do outro ou duplicar prêmios.

Depois, ferramentas de autoria/validação: IDs estáveis, schema estreito versionado, verificação de referências e pré-requisitos cíclicos, localização, testes de migração e comando de reload apenas se suportado. Evitar editor complexo antes de provar necessidade com conteúdo real.

## 4. Distribuição possível das conversas, depois das frentes atuais

- Codex: contratos de quests/progresso/segurança, integração e sequência narrativa piloto; conciliação dos módulos da equipe.
- Antigravity visual: um NPC do piloto, modelo editável, texturas e gestos, com evidência em jogo.
- Outra conversa Antigravity: um Hollow/encontro existente, telegraph e animação de ataque, preservando trabalho da equipe.

Não atribuir simultaneamente a ambas os mesmos registries, rede ou modelo. Só abrir implementação ao escolher piloto e fronteiras de arquivos; esta distribuição é proposta, não despacho automático para outras conversas.

## 5. Critérios para considerar uma entrega pronta

Missão completa em save novo e recarregado; UI e diálogo indicam próximo passo; NPC continua disponível; objetivo/claim validados no servidor; recompensa não duplica; retomada após morte/logout; terreno preservado; ausência de bloqueio por NPC desaparecido. Legibilidade de modelos/animações observada no Forge real. Multiplayer, dedicado e perfis gráficos têm resultados separados, nunca presumidos pelo teste solo.

## 6. Próxima decisão útil — revisão de 28/10

Recomendação: preparar uma entrega vertical de MAIN-SUB-01 após o aceite das correções de poderes. Não inventar outra sequência introdutória. Primeiro produzir a ficha executável e conferir quais contratos o runtime já atende; depois implementar apenas as lacunas da missão.

Divisão proposta, ainda sem despacho ou autorização de implementação nesta revisão:

- Codex: reconciliação controlada da main, contrato de tentativa/participação/persistência e integração. Preservar comandos, troca de kits e progressão Quincy atuais.
- Antigravity A: Rukia de atendimento existente, postura, olhar, fala e gesto de indicação. Sem combate aliado, mentor universal ou novo sistema de diálogo. Modelo editável e capturas Forge reais.
- Antigravity B: auditoria do encontro com Hollow existente da equipe; depois vínculo tentativa/inimigo, vitória, falha, repetição e limpeza conforme ficha aprovada. Sem duplicar modelos Hollows ou impor balanceamento definitivo.

Cada frente deve partir de origin/Feature-Poderes-bankais atualizada, em checkout separado já existente, com propriedade de arquivos explícita. Rede, registries e progresso compartilhados ficam com uma única frente. Entrega por fases, commits focados e auditoria antes de merge; nenhuma mensagem foi enviada a outras conversas nesta revisão.

Novidades upstream: 8df8a64 adiciona interface de seleção racial, mas ConfirmRaceScreen libera apenas Shinigami; não comprova abertura das quatro raças. c8d2229 corrige cotovelo do Hollow tanque. Nenhum desses commits foi integrado nesta revisão. Integração futura exige atenção a build.gradle, traduções e criação/troca de perfil.

M11 registra Cruz Quincy física em slot dedicado e arco como manifestação manual de Reishi com duas mãos. Nosso kit de testes não equivale à integração final desse equipamento/economia; registrar a diferença sem reabrir M11 agora. Asauchi por percurso de formação/Ōetsu e preservação da Zanpakutō pessoal na morte também são contratos futuros, não implementações presumidas.

Preservar: Hyōrinmaru encerrada; Zaraki intacta; Aizen como kit adiado; Gin fora da fila de poderes. Byakuya encerra Soul Society em combate contínuo Shikai/Bankai/Senkei; Aizen permanece contexto desse encerramento. Mentor/Proficiências continuam aguardando equipe. Eficiência e retenção de QA seguem tarefa transversal, sem nova exclusão de evidências aqui.

Sem gameplay, merge, instalação, download ou nova dependência nesta revisão. Evidência deste registro: git show de origin/main em 9906486 e leitura dirigida de QuestService/QuestEvents locais; não é validação funcional de campanha.

### Preparação anterior às novas frentes — 29/10

A pedido do usuário, manter as propostas acima e priorizar [capacitação do Antigravity](plano-capacitacao-antigravity.md): contrato curto do executor, auditoria pequena, feedback e teste de transferência antes de features extensas. Não iniciar NPCs/campanha automaticamente. Nenhum exercício foi executado nem capacidade adicional comprovada nesta preparação.

**Retorno dos testes29/10:** antes de novas missões, tratar [interrupção Ryūjin, projéteis Quincy e diagnóstico de coreografia/Garden Senbon](revisao-testes-2026-10-29.md). Benihime aceita. Frentes sugeridas Codex/Ryūjin, AG-A/Quincy e AG-B/Senbon em fases, ainda sem despacho ou implementação nesta revisão.

**Divisão aprovada29/10:** [quatro frentes](antigravity-frentes-2026-10-29.md): três AG para proteção temporária de ativação Bankai, projéteis Ishida e Garden N; Codex para combo Senbon com animações prontas. Invencibilidade durante ativação agora aprovada pelo usuário; não limitar correção a manter dano normal. Sincronização poderes→branches individuais obrigatória antes de implementação; sem merge automático de retorno. NPCs/lore continuam preservados para depois.
