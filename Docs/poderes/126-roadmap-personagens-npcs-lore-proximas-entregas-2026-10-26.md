# 126 — Roadmap futuro: personagens, NPCs, encontros e lore

26/10/2026. Branch Feature-Poderes-bankais. Revisão documental solicitada pelo usuário; não implementa gameplay, não executa Minecraft e não altera main. Mudanças documentais anteriores123–125 preservadas.

## 1. Estado e fontes

A lista obrigatória do123 continua;124 e125 são as frentes paralelas autorizadas. Esta revisão prepara a etapa de conteúdo do mundo, sem declarar aprovadas novas missões, personagens por era ou recompensas.

Fontes locais consultadas: Mapa modular e handoff GDD; M03 Storyline macro; M08 e catálogo08a; cabeçalhos/contratos vigentes de M09 e M10; plano-npcs-quests e arquitetura03. Conferência dirigida do runtime: QuestNpcEntity, QuestRegistry, QuestParser, NpcQuestScreen, QuestActionC2S, ModEntities e entidades Hollow listadas. Não é auditoria integral do motor de quests nem teste funcional.

Há oito registros de NPCs de quests em ModEntities: Sado, Ichigo, Orihime, Uryū, Rukia, Byakuya, Urahara e Ulquiorra. Identidades registradas não provam skins/modelos adequados, diálogo narrativo, combate, rotina ou disponibilidade por era. O plano de03/10 confirma a fundação e mantém a proposta original abaixo como histórico. Arquitetura03 contém extensa referência Dragon Mine Z: não importar suas regras, exemplos, dailies, aliases, TPS ou comportamento de party como implementação atual Bleach sem conferir código.

M08–M10 possuem regras conceituais e catálogos ainda em especificação. Próximo módulo GDD continua M11 com retorno M05; M12 quests, M13 narrativa, M14 interface, M15 multiplayer e M16 dados precisam de especificação no alcance necessário. Não trocar essa sequência por anúncio de campanha completa.

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

### N1 — uma pequena sequência narrativa completa

Proposta de piloto, ainda sujeita à escolha de origem/era com a equipe: contato espiritual no Mundo Humano, orientação contextual por fonte apropriada, investigação de uma ocorrência, encontro com Hollow já existente e retorno ao contato para reconhecer consequência e indicar próximo destino. Pode usar Rukia ou Urahara se a ficha da era justificar; escolher um responsável principal, não colocar todo o elenco na abertura.

A origem precisa ser compatível com percepção e acesso: um piloto Shinigami/alma não vira automaticamente prólogo Quincy, Hollow ou Fullbringer. Começar por uma origem valida a infraestrutura; o horizonte continua multirracial.

Missões propostas, IDs apenas editoriais: PILOTO-01 conversar/entender situação; PILOTO-02 investigar local e reconhecer indícios; PILOTO-03 resolver encontro e retornar. Sem número de inimigos, recompensa, preço ou unlock inventado. Usar a capacidade existente; objetivo novo só quando indispensável. Não transformar toda missão em matar uma quantidade de mobs.

Cada ficha define: era/raça, ator, motivação, objetivo, local, requisitos, início, progresso, falha, retomada, efeito persistente, recompensa solicitada e feedback. Falta de item/vida, logout, morte e afastamento não podem tornar a jornada irrecuperável.

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

## 6. Próxima decisão útil

Recomendação: definir um piloto de três missões com um contato principal e um encontro Hollow, reaproveitando a base. Antes de codar: escolher origem jogável e era/local, validar qual NPC atende nessa situação e qual consequência a missão deixa. Economia/recompensas finais dependem M11; Mentor/Proficiências não são reabertos por este piloto.

Planejamento pode avançar enquanto124/125 são implementados. A implementação futura de NPCs/lore continua após a lista obrigatória vigente, salvo priorização explícita do usuário. Nenhum framework, download ou nova dependência foi necessário nesta revisão.
