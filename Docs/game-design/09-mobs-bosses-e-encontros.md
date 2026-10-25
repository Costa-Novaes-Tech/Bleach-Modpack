# M09 — Mobs, bosses e encontros

> **Retorno dirigido COV-08 — 23/10/2026:** M09-12A/B receberam a direção explícita de seguir a obra; [M09a/4/9.5–9.6](09a-catalogo-funcional-de-adversarios-e-encontros.md) registra pesquisa e aplicação. Retirados temperamento determinado pelo corpo e exclusão automática de aldeões do rascunho anterior. M09-12C decidido: aldeões representam civis vivos caçáveis, adaptação explícita aprovada; não atribuir essa associação ao cânone. M06-CS-01A/B/C/D já aprovados; nenhuma pergunta pendente desse retorno de sistema.

> **Integração de Combat State — 23/10/2026:** [M06a/CS-01A/B/C/D](06a-estado-de-combate.md) decidido. Envolvimento real, ataque evitado e apoio efetivo podem marcar participantes pertinentes; presença/party/UUID não bastam. Perfil informa ameaça atual com meios válidos e encontro ainda ativo. Agressor sem rota/ataque viável não renova bloqueio indefinidamente; a própria ofensiva válida do jogador ainda pode renovar. A janela do personagem segue relógio de simulação/reavaliação, sem mudar o relógio dos mobs descarregados ou restaurar tentativa interrompida por M09-08A. Não concede reentrada, crédito ou exceção de dano espiritual.

> **Integração de presença — 23/10/2026:** [M04a/RAC-30A/B/C/D](04a-presenca-espiritual-e-interacao.md) decidido. Visão, leitura de Reiatsu e capacidade de causar dano são distintas. Perfil de Bleach declara sentidos e ataques compatíveis; alvo percebido não garante dano espiritual. Mobs vanilla mantêm percepção/tentativa de ataque, mas golpes comuns sem capacidade espiritual não ferem corpos espirituais, inclusive os jogáveis. Corpos humanos materiais seguem sujeitos ao dano comum. Refletir essa diferença nas ameaças das regiões e instrumentos de treino; não criar dano espiritual por omissão, imunidade a todo ambiente ou exceção especial contra o jogador.

## 1. Estado do módulo

| Campo | Valor |
|---|---|
| Estado documental | Regras gerais `ESPECIFICADO CONCEITUALMENTE`; catálogo M09a `EM ESPECIFICAÇÃO` |
| Abertura e última revisão | 23/10/2026 |
| Responsável pelas decisões de produto | Usuário, com análise e recomendações de design |
| Decisões | M09-01A/B e M09-02A/B `DECIDIDOS`; M09-03A/B `DECIDIDOS`; M09-04A/B `DECIDIDOS`; M09-05A `DECIDIDO`; M09-06A/B `DECIDIDOS`; M09-07A/B `DECIDIDOS`; M09-08A/B `DECIDIDOS`; M09-09A/B `DECIDIDOS`; M09-10A `DECIDIDO`; M09-10B `DECIDIDO`; M09-11A `DECIDIDO`; M09-11B/C `DECIDIDOS`; M09-12A/B com direção de fidelidade registrada e M09-12C `DECIDIDO`: aldeões como civis vivos caçáveis |
| Implementação examinada | Conferência dirigida em `main`, `3a37ca761a95faa75163c3335536bb9e0c5a80f3`; código de Hollows já existente, sem mudança nesta rodada |
| Teste e aceite | Não realizados nesta rodada; os critérios abaixo são trabalho futuro |

Este módulo define os adversários e os contratos dos encontros dentro de Minecraft. M08 mantém suas regras gerais conceituais e o catálogo em especificação; aprovar fontes iniciais não tornou os quatro prólogos executáveis. A entrada é o [repertório inicial de M08](08a-catalogo-funcional-de-npcs-e-faccoes.md#931-fontes-iniciais--bloco-conjunto-das-quatro-origens).

[Perguntas e respostas da abertura](historico/2026-10-23-propostas-m09-01ab-perfis-e-forca.md); [perguntas e respostas de detecção/perseguição](historico/2026-10-23-propostas-m09-02ab-deteccao-e-perseguicao.md). [Perguntas e respostas de hostilidade/alvos](historico/2026-10-23-propostas-m09-03ab-hostilidade-e-alvos.md). [Perguntas e respostas de terreno/construções](historico/2026-10-23-propostas-m09-04ab-terreno-e-construcoes.md). [Pergunta e resposta de compromisso direcional](historico/2026-10-23-proposta-m09-05a-compromisso-direcional.md). [Perguntas e respostas de início/falha coletiva](historico/2026-10-23-propostas-m09-06ab-inicio-e-falha-coletiva.md). [Perguntas e respostas de recuperação/nova tentativa](historico/2026-10-23-propostas-m09-07ab-recuperacao-e-nova-tentativa.md). [Perguntas e respostas de interrupção/relógios](historico/2026-10-23-propostas-m09-08ab-interrupcao-e-relogios.md). [Perguntas e respostas de contribuição/prêmios](historico/2026-10-23-propostas-m09-09ab-contribuicao-e-premios.md). [Pergunta e resposta de devoração por presa](historico/2026-10-23-proposta-m09-10a-devoracao-por-presa.md). [Pergunta e resposta de disputa de consumo](historico/2026-10-23-proposta-m09-10b-prioridade-de-consumo.md). [Seleção da amostra aprovada](historico/2026-10-23-proposta-m09-11a-amostra-de-fichas.md); [funções/papel aprovados](historico/2026-10-23-propostas-m09-11bc-funcoes-e-chefe.md). O restante deste documento distingue herança, decisões e elaboração pendente. Ter um campo no modelo não significa ter uma regra aprovada para preenchê-lo.

## 2. Objetivo

Criar ameaças reconhecíveis que exijam observar, posicionar-se, defender, atacar e escolher quando recuar. A variedade deve mudar as decisões do jogador, além da aparência e da quantidade de Vida.

Separar criatura, representação de combate e encontro permite reutilizar os assets existentes sem transformar cada skin em uma IA inteira, cada Hollow grande em Gillian ou cada confronto em uma nova morte narrativa.

## 3. Fantasia do jogador

O jogador aprende a reconhecer como uma ameaça se comporta e usa o próprio repertório para enfrentá-la. Um corpo pesado, um adversário veloz e uma criatura aérea criam problemas diferentes. Ao retornar mais preparado, enfrenta com maior segurança perigos que antes exigiam recuo.

Exploração e construção continuam relevantes. Um abrigo, uma passagem estreita ou uma arena preparada precisam ter consequências coerentes; a resposta dos inimigos ao terreno ainda será definida no bloco responsável. Não presumir destruição de bases, teleporte corretivo ou uma arena imutável em todo confronto.

## 4. Base canônica

Política herdada de D05: mangá como base; anime oficial como complemento identificado. Nesta abertura foram consultados **resumos/glossários oficiais do anime**, em japonês, em **23/10/2026**. Não houve leitura de capítulos ou episódios integrais.

| ID | Fonte e recorte conferido | Uso e limite |
|---|---|---|
| ENC-S01 | [Pierrot — glossário ha](https://pierrot.jp/title/bleach/keyword_ha.html), verbetes Hollow e Plus | Hollows têm formas/capacidades variadas; Plus são almas geralmente inofensivas. Não fornece tiers, atributos, spawn ou relação automática com cada jogador |
| ENC-S02 | [Pierrot — retrospectiva 1–20](https://pierrot.jp/title/bleach/playback_01.html), confrontos iniciais, Grand Fisher e Menos | Exemplifica ameaças e desfechos distintos, incluindo fuga e afastamento. Não define condição de vitória para o mod nem valida transformar indivíduos da história em espécies de spawn comum |
| ENC-S03 | [Pierrot — glossário ma](https://pierrot.jp/title/bleach/keyword_ma.html), Menos/Grande Hollow | Distingue classes e evolução na descrição histórica. Não converte tamanho em estágio, raridade em chance de desbloqueio ou essa descrição antiga em revisão dos contratos raciais aprovados |

O próprio arquivo informa que retrata a época da exibição. Generalizações históricas não substituem as exceções e adaptações já registradas em M04/M07. Comum, elite e chefe, seus números e a política de força são **organização de gameplay aprovada**, não uma taxonomia canônica universal.

Para cada inimigo nomeado, a ficha ainda precisa conferir identidade, aparência, capacidades e era. Fishbone D, Acidwire, Shrieker e Grand Fisher não foram aprovados como população genérica nesta abertura. [Banco de conteúdo recebido e parecer](00-reconciliacao-implementacao-assets-2026-10-22.md#21-hollows-e-habitantes).

## 5. Adaptação para Minecraft

**M09-01A aprovado:** natureza/estágio, perfil de combate e papel no encontro são registros distintos. **M09-01B aprovado:** no mundo aberto, a força pertence ao perfil e ao contexto, sem acompanhar automaticamente o crescimento do jogador. Versões mais difíceis e ajustes para party precisam de regras explícitas.

Adaptações herdadas que limitam as próximas escolhas:

- Personagem original e catálogo de poderes seguem M01/M04; raça não determina inimizade ou PvP automaticamente.
- M03/STR-03 entrega a vitória dos bosses canônicos ao jogador/party, sem aliado canônico combatendo ou finalizando por ele. Representação de encontro não é morte do prestador permanente.
- Mundo e construções persistentes fazem parte do jogo. Uma regra de destruição ou restauração ainda precisa ser decidida; nenhum rollback é presumido.
- Combate, recursos, defesa, percepção e supressão seguem M05/M06. Um inimigo não recebe detecção exata através de paredes apenas porque existe Percepção Espiritual.
- Predação segue M07-04A/B: presa Hollow NPC inimiga elegível, participação efetiva e devoração concluída; último golpe não é requisito universal. Arena construída e grind ativo não são automaticamente inválidos.
- Aquisição usa SP conforme REV-SP-02; Mastery vem da prática. Derrotar um inimigo não compra uma skill ou entrega domínio automaticamente.
- O servidor autoriza ações e resultados também no singleplayer. A persistência de cada estado de encontro ainda exige contrato próprio; a limpeza de formas do jogador não resolve sozinha a persistência dos mobs.

## 6. Escopo

| Pertence a M09 | Depende de outro responsável |
|---|---|
| Famílias de adversários, perfis de luta, comum/elite/chefe | Natureza e evolução do jogador: M04/M07 |
| Detecção, escolha de alvo, perseguição, retorno e comportamento sem caminho | Geografia, geração e locais: M10; interação com construções também exige M09 |
| Ataques, preparação visível, acerto, recuperação e fases de encontro | Fórmulas e recursos: M05/M06; calibração e escala de party: M15 |
| Início, vitória, derrota, desistência e interrupção do encontro | Objetivos de quests: M12; contexto e acontecimentos: M03/M13 |
| Evidência de participação e resultado para sistemas consumidores | Distribuição cooperativa: M15; itens/economia: M11; recompensas de quest: M12 |
| Compatibilidade entre ator de combate e atendimento | Função/serviço/relação do NPC: M08 |
| Necessidades de legibilidade e desempenho da amostra | Apresentação/controles: M14; implementação e validação: M16 |

Não fecha aqui todas as fichas do elenco, a primeira leva de produção, as campanhas de linhagens ou o sistema de híbridos/Visored. Grimmjow continua primeiro caso de linhagem quando chegar sua etapa.

## 7. Modelo conceitual

### 7.1. Criatura, perfil e papel — M09-01A DECIDIDO

| Camada | Pergunta | Exemplo e limite |
|---|---|---|
| Natureza e estágio | O que é esse ser? | Hollow, Gillian, Arrancar etc., conforme evidência da ficha; corpo grande ou branco não basta para classificar |
| Perfil de combate | Como ele enfrenta o jogador? | Bruto terrestre, ágil de curta distância, aéreo de aproximação e retirada; capacidades e anatomia precisam corresponder |
| Papel no encontro | Como essa presença organiza o desafio? | Comum, elite ou chefe; o mesmo tipo de criatura pode ocupar papéis diferentes em configurações explícitas |

**Comum:** adversário do repertório regular, com comportamento compreensível e utilidade própria. **Elite:** ameaça especial cuja diferença precisa aparecer no comportamento ou na situação, além de poder ter outros números. **Chefe:** centro de um confronto com identidade e condições de resolução próprias. Essas descrições não aprovam novas técnicas, imunidades, fases obrigatórias ou um limite numérico de inimigos.

Natureza não é slot de dificuldade. Um Gillian não precisa sempre ser chefe, um Hollow menor pode sustentar um encontro especial e o chefe atual não se torna Gillian por medir mais que os demais. “Elite” não é outro estágio de evolução do jogador. Textura cosmética não altera alcance/colisão sem perfil funcional declarado.

### 7.2. Representação e encontro — elaboração para as próximas fichas

| Registro necessário | Informação a separar | Dono funcional proposto |
|---|---|---|
| Definição do adversário | Identidade, corpo, repertório, parâmetros e contextos permitidos | Catálogo M09; corpo/poder compartilhado consulta M06/M07 |
| Representação presente | Vida atual, posição, alvo, execução e vínculo ao encontro quando houver | Ator de combate, autorizado pelo servidor |
| Encontro | Motivo de criação, participantes, objetivos, limites, resultado e retomada | Sistema de encontros, em integração com mundo/quest |
| Progresso pessoal | Crédito válido, requisitos e marcos reconhecidos | Progressão/quest/evolução responsáveis; não campo livre no mob |
| Consequência compartilhada | Marco de mundo autorizado pela história | M03/M12/M13/M15 |

São responsabilidades conceituais, sem impor novas classes, banco externo ou instâncias privadas. Um encontro pode ocorrer no mundo compartilhado. Fechamento/retomada e autoria têm contratos em 06–10; admissão de terceiros e regras executáveis/distributivas ainda exigem fichas M09/M12/M15.

### 7.3. Ficha mínima de adversário/encontro

Identidade e fonte; natureza/estágio; perfil e papel; contexto/era/local; corpo/alcance; repertório; percepção/alvos; comportamento no terreno; força/versão; criação; início; fases; vitória/falha; retorno/interrupções; participação; eventos de recompensa/predação; comunicação; limites de custo técnico. Campos ainda sem regra ficam pendentes, com responsável.

## 8. Regras principais

### 8.1. Força própria por perfil e contexto — M09-01B DECIDIDO

O adversário de mundo aberto usa a força definida para sua versão, região e situação. Ganhar atributos, Mastery ou novas formas não aumenta automaticamente a força daquele perfil para acompanhar o jogador. Voltar a uma ameaça conhecida pode demonstrar crescimento real; regiões e situações também podem conter ameaças que recomendem recuar.

**Não significa uma cópia idêntica em todo lugar:** versões distintas podem existir se forem configuradas e comunicadas. Uma mudança prevista na era ou na situação pode introduzir outro perfil; não é aumento oculto por ler o BP de quem se aproxima. A ficha informa o contexto e a versão, sem reajuste contínuo durante a luta para anular vantagem conquistada.

A decisão cobre o **mundo aberto**. Não define fórmula de boss narrativo, ajustes de party, dificuldade do servidor, desafios opcionais, entrada tardia de participantes ou recompensas de inimigos superados. Essas políticas precisam de M09/M15 antes de serem oferecidas. Não cria nível obrigatório, trava de acesso por BP ou indicação exata de atributos do inimigo.

### 8.2. Contratos herdados de combate e história

| Contrato vigente | Consequência para M09 |
|---|---|
| M06: execução, colisões, custos e recuperação | VFX não autoriza acerto; definição do ataque precisa declarar área, momento e reação possível |
| M06: armadura/proteções compatíveis | Ataque espiritual não ignora armadura automaticamente; exceção exige ficha |
| M06: Percepção/Supressão graduais | IA não ganha localização perfeita nem nova tentativa aleatória a cada tick por associação; seu uso real desses sentidos ainda exige decisão |
| M03/STR-03 | Derrota jogável e continuidade narrativa distintas; elenco pode aparecer no contexto, sem tomar o combate canônico do jogador |
| M08-01A e M08-12A | Prestador essencial protegido não vira alvo de farm, tanque móvel ou presa pela aparência; treino usa representação própria |
| M07-04A/B | Abate, participação e consumo são resultados diferentes; automação integral, aliados, pets e jogadores não geram predação evolutiva |
| M03/STR-06 | Vitória individual não muda livremente a era do servidor; encontros fornecem resultados aos responsáveis |

### 8.3. Questões operacionais ainda abertas

M09-02A/B aprovam detecção/busca; 03A/B, hostilidade e troca de alvo; 04A/B, alteração de blocos por exceção e tentativa limitada sem solução; 05A, compromisso direcional. Capacidades, alvos e parâmetros concretos ainda exigem fichas. M09-07A/B definem recuperação gradual comum e estado inicial da nova tentativa. Ataque a aldeões foi decidido em 12C; ainda não estão decididos técnicas destrutivas concretas, despawn, persistência de boss, morte não letal ou divisão de crédito. M09-06A/B aprovam início de prova e continuidade/falha coletiva; seus parâmetros e demais regras de ciclo continuam pendentes. O comportamento atual do código localiza uma implementação a avaliar, sem aprovar essas políticas para o jogo completo.

Não herdar automaticamente dos projéteis efêmeros de M06 a regra de todo mob desaparecer ao descarregar um chunk. Não tratar sair do modo de combate como sair do encontro; são sistemas diferentes.

### 8.4. Detecção por perfil — M09-02A DECIDIDO

**Aprovado em 23/10/2026.** Resposta: “Visão e percepção conforme o perfil (recomendado)”. Herança de M06-11F/G/I/J/K; ampliação aprovada para a reação dos inimigos. Não muda a aquisição, os custos ou os limites das capacidades do jogador. A ficha do adversário declara os sentidos que realmente possui, sua capacidade e seus parâmetros; ser elite ou chefe não concede automaticamente todos os sentidos.

| Informação disponível | O que pode orientar | O que não se deduz dela |
|---|---|---|
| Visão direta válida | Localizar o corpo visível e atualizar a posição observada | Supressão não esconde esse corpo; aparência, campo visual e obstáculos precisam corresponder à ficha |
| Presença espiritual detectável | Alerta ou direção/distância aproximadas, conforme capacidade e condições | Coordenadas exatas, mira através de blocos, identidade desconhecida ou confirmação automática de hostilidade |
| Última posição observada | Busca naquele local e arredores pelo prazo do perfil | Posição atual de quem já se deslocou sem ser percebido |
| Último indício espiritual | Investigar a área aproximada indicada enquanto a informação for pertinente | Acompanhamento contínuo por conservar uma leitura antiga |

**Regra aprovada:** combinar visão e percepção espiritual somente conforme o perfil. Um inimigo pode investigar um sinal sem ainda localizar o alvo para executar um ataque dirigido. A decisão de hostilidade é separada: sentir uma presença não ordena atacar qualquer ser próximo.

Supressão influencia o sinal espiritual, sem apagar visão, informação anterior ou evidências sensoriais legítimas. Domínio, Controle, presença exteriorizada, distância e familiaridade seguem M06; não oferecer uma nova rolagem de detecção por tick. Aproximar-se, perder a barreira visual ou mudar de emissão pode alterar legitimamente o resultado. Nem toda parede garante fuga; nem toda presença percebida revela a posição real.

**Percepção natural e técnica ativa:** sensibilidade natural não exige uma leitura paga a cada atualização. Isso não concede Pesquisa a todos. Se a ficha der ao inimigo uma leitura deliberada, ela precisa declarar execução, frequência, custo/recurso e informação produzida, respeitando os limites correspondentes de M06; não transformar uma técnica finita em radar gratuito permanente. Valores de NPC e sua tradução técnica continuam pendentes, sem inventar uma nova barra sensorial.

**Audição e outros sentidos:** M06 já diz que Supressão não silencia ações. Isso não comprova uma IA que escuta todos os sons do cliente. Som, contato ou capacidade especializada só orientam a reação quando sua ficha define evento, alcance e informação disponível; não está aprovado aqui um sistema global de audição, cheiro ou pegadas. Dano com autoria conhecida pelo servidor não entrega automaticamente à criatura a posição oculta do atirador.

**Cobertura do contrato:** contrato para os perfis de inimigos, começando pelos Hollows existentes. Bruto, ágil, voador e boss ainda precisam da seleção concreta de capacidade sensorial; a escolha do modelo não faz todos possuírem a mesma sensibilidade, Pesquisa ou campo visual. Não aprovar uma tabela numérica sem ficha/calibração M05/M06/M15.

### 8.5. Perda de contato e busca — M09-02B DECIDIDO

**Aprovado em 23/10/2026.** Resposta: “Busca limitada e retorno à área (recomendado)”. **Cobertura:** perseguição comum no mundo aberto. Chefes, provas e encontros dirigidos precisam declarar seus limites próprios; não recebem reset, fuga garantida ou retirada de participantes por esta decisão.

| Situação | Comportamento pelo contrato aprovado |
|---|---|
| Alvo localizado por informação atual válida | Perseguir por um caminho permitido, mantendo limites do perfil e as condições próprias de cada ataque |
| Visão perdida, presença ainda perceptível | Investigar a direção/área percebida; não usar a coordenada oculta real como destino exato |
| Sem sinais atuais suficientes | Procurar perto da última posição conhecida ou da área indicada pela última pista, por período limitado |
| Novo sinal suficiente durante a busca | Atualizar a informação e a ação correspondente; um sinal vago pode orientar investigação, sem virar localização precisa |
| Busca sem reencontro ou limite de perseguição atingido | Abandonar aquela perseguição e retornar à área de origem/atividade definida pelo perfil |

O prazo e a área de busca, alcance máximo da perseguição, condição de reencontro e área de retorno precisam ser definidos por perfil em unidades de tempo/distância. Esta decisão não fixa valores universais, não cria uma barra de detecção e não promete que toda criatura percorra o mesmo caminho de volta.

**Memória sem atualização oculta:** ir à última posição vista não permite acompanhá-la invisivelmente até o lugar novo. Um sinal atual pode renovar a investigação dentro dos limites do perfil; uma pista antiga, reconsulta vazia ou conhecimento administrativo do UUID não renovam a busca por si só. Ligar Supressão não elimina a lembrança nem assegura fuga se ainda houver informação suficiente.

**Perder o alvo e não alcançá-lo são situações diferentes:** ver alguém atrás de uma passagem estreita pode conservar a localização, mas não autoriza atravessar paredes, teleportar, quebrar blocos ou acertar fora do alcance. A resposta concreta a falta de caminho/retorno bloqueado pertence ao próximo bloco de terreno. O sistema não pode depender de teleporte presumido para cumprir o retorno.

**Voltar não significa curar ou encerrar todos os sistemas:** Vida, recuperação de recursos, cooldowns, crédito de participação, oportunidade de predação, quest e despawn exigem regras do ciclo do encontro. Não atribuir cura automática nem persistência ilimitada de dano por omissão. O alvo pode escapar da criatura sem que isso seja automaticamente uma vitória de quest.

### 8.6. Informação, alvos e autoridade — desdobramentos do bloco

Organização conceitual do contrato: **atividade normal → alerta/investigação → perseguição/combate → busca → retorno**. Um sinal pode iniciar investigação; localizar e decidir atacar exigem os critérios pertinentes. Estados não precisam ser uma cadeia obrigatória: visão de um alvo hostil pode dispensar investigação intermediária, e uma pista perdida não confirma a presença de um inimigo.

O servidor valida a elegibilidade e conserva separadas a posição real da entidade e a informação que a criatura tem direito a usar. Compartilhar o mesmo servidor não justifica navegar até a posição secreta, orientar a cabeça continuamente para ela ou iniciar um golpe que revele o alvo oculto. Alcance/colisão de acerto continuam sendo requisitos independentes.

**Alvos:** 03A/B aprovam motivos de hostilidade e estabilidade/troca em 8.8/8.9. Categorias da amostra foram complementadas em M09a/9.6–9.7, incluindo aldeões como civis vivos caçáveis por 12C. Golems, critérios concretos de prioridade e comunicação entre criaturas permanecem nas fichas. O código atual de seleção por proximidade/reação não decide essas políticas. Não compartilhar automaticamente a descoberta com todos os mobs, não tornar facção/raça uma ordem universal de ataque e não tratar party como informação sensorial global.

Morte, logout, mudança de dimensão ou descarregamento invalidam informações conforme o ciclo responsável; não procurar o jogador em outra dimensão por identidade conhecida. Se a região não estiver simulada, não forçar seu carregamento para concluir uma busca. Persistência dos estados, retomada, temporizadores e destino da criatura ainda precisam do bloco de ciclo de vida; a aprovação não garante simulação offline ou retorno instantâneo.

### 8.7. Casos de revisão do bloco sensorial — critérios futuros

| Caso | Resultado futuro a verificar sob 02A/B aprovados |
|---|---|
| Jogador suprimido passa diante do inimigo que pode vê-lo | Visão continua válida; não há invisibilidade espiritual automática |
| Jogador se oculta atrás de parede e seu sinal não é detectável | Busca pela informação passada, sem seguir a posição atual escondida |
| Jogador atrás de parede com sinal ainda detectável | Possível investigação aproximada, sem acerto através do bloco ou coordenada exata gratuita |
| Duas criaturas com sensibilidades diferentes | Resultados podem diferir; nenhuma revelação global automática |
| Mesmas condições repetidas durante a busca | Não ganhar informação por uma nova rolagem a cada tick |
| Pista expira, sem reencontro | Terminar a perseguição comum conforme os limites e solicitar retorno válido; Vida/recompensa não são resolvidas por esse evento sozinho |
| Alvo visto, mas sem passagem para o corpo | Tratar falta de caminho no contrato de terreno, sem fingir perda de visão ou conceder travessia de blocos |

**Pesquisa dirigida ENC-S04, 23/10/2026:** reconsulta a [ENC-S01/Pierrot ha](https://pierrot.jp/title/bleach/keyword_ha.html), verbete Pesquisa, e [ENC-S02/retrospectiva 1–20](https://pierrot.jp/title/bleach/playback_01.html), atração de Hollows pela capacidade espiritual de Ichigo. O recorte sustenta exemplos sensoriais, sem provar precisão, alcance, docência, custo, memória ou perseguição de todo Hollow. Anime complementar e resumo, sem capítulos/episódios integrais. 02A/B são adaptações aprovadas sob os limites já decididos de M06; a fonte não comprova uma IA universal da obra.

### 8.8. Hostilidade dos mobs comuns — M09-03A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Hostilidade agressiva, territorial ou reativa por perfil (recomendado)”. Cobertura: comportamento comum de mundo aberto. A herança RAC-16/17 já separa natureza, relações e PvP; não está sendo votada novamente. A escolha nova é oferecer diferentes motivos para iniciar combate, configurados por perfil e contexto.

| Comportamento aprovado | Motivo para iniciar | Informação que a ficha precisa fechar |
|---|---|---|
| Agressivo | Procura e enfrenta alvos elegíveis quando os identifica pelas informações permitidas | Categorias/relações visadas, contexto e condições de abandono |
| Territorial | Reage à presença elegível numa área que defende | Limites reconhecíveis, quem pode entrar e condição de reação; advertência não é universal nem já aprovada |
| Reativo | Responde a provocação reconhecida pelas regras do perfil | Quais eventos provocam, autoria, informação disponível e limites da resposta |

São padrões de gameplay aprovados, não classes canônicas nem atribuições já aprovadas para bruto, ágil, voador ou todo Hollow. A ficha precisa escolher seu comportamento e eventuais condições particulares. Agressivo não significa atacar tudo; territorial não significa perseguir sem limite; reativo não significa conhecer a posição oculta do autor de um projétil. Não se está criando um sistema universal de provocação por som, presença ou contato.

**Heranças e fronteiras:** relações consideram contexto, era e histórico conforme M04/M08; mesma natureza não garante amizade. Um Hollow inimigo pode ter outros Hollows como alvos, com aplicação posterior em M09a/9.6–9.7: jogadores elegíveis, humanos/Plus e aldeões como civis vivos por 12C. Golems, fauna e relações específicas entre rivais ainda precisam de ficha, sem canibalismo universal. Esta aprovação não ratifica os seletores atuais do código. Um Plus geralmente inofensivo não se torna adversário por ser um possível alvo de outra criatura.

Prestadores essenciais mantêm proteção e limites de M08-01A/04B. O padrão reativo não transforma um clique no mestre em combate automático nem estende a tolerância do prestador a todo mob neutro. Guardas, civis, escoltas e representações de prova precisam das próprias fichas. Seleção de alvo, dano de área, bloqueio físico por prestador e uso como escudo são problemas distintos; proteção não concede tanque ao jogador.

**Elegibilidade antes da prioridade:** a criatura só considera candidatos válidos para seu perfil, dimensão e informações de 02A/B. Relação aliada reconhecida não vira hostilidade só por proximidade. Ser membro da party de um alvo não transfere automaticamente culpa ou informação; participação e relação precisam de motivo próprio. PvP permitido/proibido continua responsabilidade de M15 e não é uma chave global que liga/desliga ataques PvE.

### 8.9. Escolha e troca do alvo comum — M09-03B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Alvo estável, com motivos definidos para trocar (recomendado)”. Regra: conservar o alvo enquanto sua perseguição ainda for válida, com troca motivada. A primeira escolha compara apenas candidatos elegíveis e informação permitida. Ordem de preferências, desempates, frequência de avaliação e critérios de ameaça relevante exigem ficha; nenhum peso ou fórmula está aprovado.

| Situação | Tratamento pelo contrato aprovado |
|---|---|
| Outro jogador passa mais perto ou acerta um golpe isolado | Não força troca por esse evento sozinho; avaliar as condições do perfil |
| O alvo se esconde | Aplicar busca/memória de 02B; perder visão não é invalidar imediatamente o alvo |
| O alvo morre, sai ou deixa de ser elegível | Encerrar sua seleção; considerar outro candidato válido ou a busca/retorno pertinente, sem cura/reset presumido |
| Uma ameaça percebida mais relevante ou objetivo explícito justifica troca | Permitir troca conforme critérios declarados e informação disponível |
| Alvo continua visível, mas o corpo não consegue alcançá-lo | Não fingir perda de contato; a resposta a falta de caminho é o próximo bloco de terreno |

O contrato evita alternância a cada dano/proximidade e permite cooperação previsível. Não garante fixar o inimigo num jogador. Não cria profissão de tanque, comando de provocação, invulnerabilidade para os demais ou barra universal de ameaça. O alvo principal também não garante ser o único atingido: áreas, ataques secundários e fogo aliado dependem das fichas de execução/dano.

**Trocar alvo não reinicia a técnica:** alvo escolhido pela IA e alvo/área já comprometidos por uma execução são estados distintos. A política não autoriza girar um golpe já lançado, zerar Recovery/cooldown ou cancelar um ataque gratuitamente; cada execução conserva seu contrato. Autoria conhecida pelo servidor não equivale a localizar o agressor escondido.

**Cobertura e pendências:** 03B cobre inimigos comuns; chefes, provas, defesa de objetivos e comportamentos especializados terão política explícita, sem copiar este contrato como aprovação universal. Coordenação, pedido de ajuda, transferência de pistas e reação coletiva permanecem pendentes: um mob ser atingido não comunica automaticamente o alvo a todos os aliados. A seleção não distribui SP, participação, predação ou recompensa.

**Casos para revisão futura:** dupla alternando golpes; jogador ferido fugindo e aliado intervindo; tiro de origem oculta; alvo suprimido ainda em busca; encontro entre Hollows não aliados; invasão de território por visitante autorizado; mestre protegido junto do combate. Antes de implementação, cada ficha precisa produzir resultados observáveis, respeitar 02A/B e definir as informações que justificam a escolha. Nenhum cenário foi executado.

### 8.10. Construções e alteração de blocos — M09-04A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Sem alteração de blocos por padrão; exceções definidas (recomendado)”. Regra: mobs comuns do mod não quebram, substituem ou colocam blocos por padrão, ao navegar ou atacar. O padrão alcança terreno natural e construído; não depende de adivinhar quem colocou cada bloco. Abrigos, muros e arenas preparados pelo jogador continuam estratégias válidas. Esta regra é uma adaptação aprovada para Minecraft, não uma limitação canônica do poder destrutivo em Bleach.

Destruição permanece possível em técnica/encontro que declare uma exceção: ação que altera blocos, materiais atingidos, área/limites, contexto e aviso, interação com regras do servidor e consequências persistentes. Criar fogo, liberar líquidos, provocar explosões ou substituir terreno também exige tratar sua alteração direta/indireta; não contornar o padrão trocando a categoria do efeito. A decisão não aprova uma técnica destrutiva concreta, alcance ou resistência de material.

**Fronteira:** não desliga comportamentos vanilla ou de outros mods. Não cria claims, propriedade de terreno, restauração automática, drops de bloco ou invulnerabilidade da base. Configuração e suporte real às proteções presentes exigem M10/M11/M15/M16; não presumir que uma integração já existe. Um evento destrutivo futuro precisa especificar essas interações antes de ser oferecido.

A alternativa permite destruição de passagem para perfis capazes na perseguição comum. Dá pressão contra fortificações, mas exige desde o início critérios de material, frequência, limite territorial e proteção das construções. Ser grande, elite ou chefe não concede essa capacidade por classificação. A política de cada boss ainda exige ficha.

### 8.11. Alvo percebido sem solução de alcance — M09-04B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Tentativas limitadas e recuo sem solução (recomendado)”. Cobertura: inimigos comuns do mundo aberto. Regra: tentativa limitada quando o alvo é percebido, mas não há caminho/ataque viável; esta é uma situação diferente da perda de informação tratada em 02B.

| Situação | Resposta pelo contrato aprovado |
|---|---|
| Parede, teto, passagem ou desnível bloqueia a aproximação | Tentar caminho alternativo permitido para o corpo e capacidades reais do perfil, usando informação autorizada |
| Existe ataque do repertório que pode alcançar legitimamente | Usá-lo se requisitos, fase, alcance e obstáculos permitirem; não ganhar Cero, voo ou artilharia ao ficar preso |
| Sem solução pelo prazo de tentativa | Encerrar aquela tentativa e procurar retorno à área de atividade, mesmo mantendo visão; o evento não apaga a informação observada |
| A rota se torna viável durante a tentativa | Reavaliar a possibilidade real, respeitando alvos e compromissos já aprovados |
| Caminho de retorno também está bloqueado | Não autoriza teleporte, atravessar blocos ou apagar a entidade; destino alternativo, espera/retentativa e descarregamento precisam do contrato de ciclo |

A ficha precisa definir o que é progresso real, o prazo sem solução, periodicidade de avaliação e quando uma nova tentativa pode começar. Ver o mesmo alvo outra vez a cada tick não deve, por si só, reiniciar o prazo e impedir o recuo aprovado. Agressão nova, deslocamento, abertura de rota e interrupção do retorno precisam de critérios concretos antes da implementação; a aprovação não escolhe esses intervalos ou gatilhos.

Recuar não significa tornar-se invulnerável, curar, recuperar recursos, limpar cooldowns, apagar participação ou conceder vitória de quest. Dano válido continua sujeito ao combate, inclusive quando o mob não alcança o jogador. Construir uma posição favorável não invalida automaticamente grind/participação: M07 permite arena construída e grind ativo; autoria, automação e recompensa serão resolvidas no bloco próprio. Não oferecer uma punição por usar o terreno como compensação presumida para a IA.

A alternativa mantém pressão enquanto existir percepção, podendo aguardar, contornar ou atacar quando viável; continua sem conceder travessia ou dano através de obstáculos. Exige tratar imobilização prolongada, concentração de entidades e encerramento. Chefes, provas e encontros dirigidos ficam fora do padrão comum até sua ficha declarar a resposta ao terreno.

### 8.12. Corpo, meios e cenários de terreno — elaboração e pendências

Herança de colisão/execução: tamanho funcional não é somente visual; detectar não significa alcançar e efeitos visuais não concedem acerto. Teto e túneis precisam caber no corpo real. Um voador não ganha travessia de parede; uma criatura grande não encolhe automaticamente para alcançar o jogador. Adaptações de postura, escalada, salto, natação ou transformação exigem capacidade declarada, sem impor os controles de mobilidade do jogador aos NPCs.

**Ainda não decidido:** quais perfis nadam/mergulham, evitam riscos, toleram queda, abrem portas ou interagem com objetos; comportamento quando pistão, líquido, bloco colocado ou teleporte externo confina o corpo; retorno sem rota; alteração de arena por jogadores e exceções de boss. Não presumir que todo inimigo sabe evitar todos os perigos ou possui os movimentos que a anatomia apenas sugere. M10/M15 precisam conciliar construção em encontro e proteções; este bloco não cria arena indestrutível ou restrição universal de construção.

**Revisão futura:** abrigo de dois blocos contra corpo maior; muro com passagem alternativa; jogador em pilar; teto baixo contra voador; água e borda com queda; mob atingido de posição inacessível; retorno bloqueado por bloco novo; exceção destrutiva recusada por regra do servidor. Cada ficha precisará resolver seus casos sem inventar capacidades ou localização secreta. Critérios documentais para trabalho futuro, nenhum cenário executado.

### 8.13. Execução legível — consolidação dos contratos herdados

Este bloco aplica a herança de M06-02S/T/U, M06-06I, M06/47 e M09-03B ao trabalho das fichas; não pede nova aprovação de dano, interrupção, Recovery ou imunidades. Nenhum perfil recebe Super Armor por ser grande/elite/chefe.

| Parte da execução | Exigência de ficha sob os contratos existentes |
|---|---|
| Preparação | Sinal coerente com o golpe, duração, movimento permitido, mira e eventuais cancelamentos; antecipação precisa ajudar a reconhecer a ameaça |
| Compromisso/parte ativa | Momento e geometria real de acerto, direção/área, alcance, obstáculos e alvos elegíveis; animação sozinha não causa dano |
| Recovery e cooldown | Declarar a fase de recuperação e o intervalo de reutilização separadamente; ações disponíveis precisam corresponder à ficha |
| Controle/interrupção | Dano não equivale a stagger ou cancelamento; efeito de controle só invalida as fases/requisitos pertinentes e resistência é propriedade declarada |

Errar, trocar alvo ou perder uma rota não apaga tempos já assumidos por associação. Interrupção real e cancelamento autorizado precisam declarar sua consequência; não criar recuperação universal fixa nem imunidade temporária para remendar IA. Defesa, alcance e obstáculos continuam validados no servidor; visual e hitbox devem corresponder. Compatibilidade com escudo vanilla e defesa espiritual mantém M06/M11, sem tratar todo ataque como bloqueável ou todo golpe de boss como inevitável.

**Pendências por ficha:** duração das fases em ticks, forma do volume de acerto, alvos/cobertura, canal visual/sonoro, tolerâncias técnicas, ritmo de sequências e oportunidade real de resposta para o repertório exigido. M14 define apresentação/acessibilidade; M15/M16 calibram e validam latência/desempenho. Não exigir Shunpo/Cero/voo a todo iniciante para resolver a amostra. Não oferecer números apenas para preencher a tabela.

### 8.14. Momento de fixação da direção — M09-05A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Ajustar no início e fixar antes do golpe (recomendado)”. Cobertura: padrão dos golpes direcionais comuns dos inimigos. Regra: permitir giro limitado durante o começo do preparo e encerrar esse ajuste na parte final, antes da fase ativa. O trecho final deve comunicar o compromisso e oferecer oportunidade real de reação conforme alcance/velocidade e repertório exigido; durações e limite angular ainda precisam da ficha.

**Experiência aprovada:** esquivar muito cedo pode permitir correção ainda autorizada; reposicionar-se no momento comprometido pode tirar o jogador da trajetória. Isso não garante que qualquer direção de esquiva evite área/alcance do ataque. Fixar orientação não transforma uma varredura em raio sem largura, paralisa automaticamente todo deslocamento ou elimina a trajetória legítima de uma investida. Corpo, movimento e geometria exigem definição própria.

Depois do compromisso, uma nova posição do alvo ou troca de seleção não reorienta o golpe gratuitamente. A direção autorizada e a informação percebida seguem M09-02/03; não usar coordenadas ocultas para ajustar a mira. A escolha não permite transferir dano ao alvo selecionado se ele já não estiver no volume válido, nem garante acertar quem permaneceu apenas no alcance radial.

Ataque que segue o alvo durante execução, projétil guiado, canal ajustável, sequência com nova mira ou golpe de área centrado em outro ponto precisa declarar sua diferença e sinais. Não transformar este padrão em regra universal de todo poder ou de boss. Cada novo golpe de uma sequência pode ter novo preparo conforme ficha; a decisão não autoriza redirecionar no meio de um contato já comprometido.

**Alternativa:** fixar desde o começo do preparo. Torna o compromisso mais cedo e simples de ler, mas facilita evitar golpes lentos com deslocamento antecipado; pode ser adequada a uma técnica específica. O padrão aprovado permite variedade sem converter toda execução em perseguição automática.

**Critérios futuros:** esquiva cedo/tarde; alvo que cruza para trás; jogador saindo do alcance mas ainda visível; ocultação durante preparo; troca de alvo antes/depois do compromisso; parede nova; golpe largo/investida versus animação; dois observadores no servidor. Nenhum cenário executado; sinais, números e ficha concreta continuam pendentes.

### 8.15. Ciclo do encontro — organização conceitual

M09 distingue a existência de um inimigo no mundo, sua agressão e a tentativa organizada de uma prova/quest. O ciclo abaixo organiza a especificação; não implementa uma máquina de estados nem atribui uma missão a todo mob.

| Situação funcional | Informação/resultado que exige contrato |
|---|---|
| Disponível | Perfil, contexto/era, local, requisitos e fonte que oferece a atividade |
| Preparado | Participantes elegíveis e condições de início; aceitar quest, confirmar tentativa e criar ator são atos distintos |
| Ativo | Objetivos, participantes aptos, limites, atores e execuções pertencentes à tentativa |
| Resolvido | Vitória, falha por condição própria ou abandono reconhecido; resultado sem duplicação para seus consumidores |
| Interrompido sem resultado confirmado | Unload, reinício ou falha de continuidade exigem regra de retomada; não são vitória/falha presumidas |
| Nova tentativa | Requisitos, estado dos atores, preservação de conquistas e despesas; reset/recompensas ainda pendentes |

Entradas e resultados são autorizados pelo servidor; M12 solicita/observa o encontro, M15 define participação e compartilhamento, M03/M13 autorizam consequências narrativas. O inimigo não decide sozinho a era, compra ou recompensa. M03/STR-03 mantém vitória jogável sem aliado canônico que finalize; representação de combate não mata o prestador permanente de M08. Progresso/conquistas persistentes seguem seus contratos; falhar não concede por associação um reset da jornada.

### 8.16. Início deliberado de prova/confronto preparado — M09-06A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Confirmar o início das provas e confrontos preparados (recomendado)”. Regra: confirmação de início após apresentação de objetivo, riscos e requisitos, para provas e confrontos preparados por quest. Aceitar a quest pode disponibilizar a atividade, mas não dispara a tentativa sozinho. A ficha declara o local e condições válidas, sem exigir universalmente sair de combate, viajar, teleportar ou abrir outra interface. Confirmar não pausa o mundo nem concede proteção.

**Cobertura aprovada:** atividade deliberadamente oferecida como prova/confronto preparado. Emboscadas, invasões, gatilhos de história e eventos automáticos podem usar outro início quando a ficha os identificar. Mobs comuns continuam reagindo por 03A; o contrato não coloca confirmação antes de cada ataque ou transforma todo boss em serviço de menu.

A confirmação precisa revalidar requisitos/contexto e evitar tentativa/ator duplicado por repetição do pedido. Uma confirmação não inscreve silenciosamente todos os jogadores próximos: consentimento, composição, líder/party, terceiros e disputa de espaço exigem M15/M12. Área de acesso, conflito com encontro já ativo e possibilidade de cancelamento antes do início real precisam de ficha; esta aprovação não cria instância privada ou reserva de região automaticamente.

**Alternativa:** iniciar ao entrar na área da prova. Reduz uma interação e serve a certos eventos, mas exige que aproximar-se, atravessar ou construir perto não dispare tentativas indevidas. A escolha de um padrão não remove a necessidade de gatilho e contexto explícitos para exceções.

### 8.17. Continuidade coletiva e falha — M09-06B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Continuar com participantes aptos; falha coletiva (recomendado)”. Padrão para confrontos cooperativos de combate: a tentativa continua enquanto houver participante apto no encontro; morte/saída individual não a encerra para os demais. Se todos forem derrotados, reconhecer falha; se todos abandonarem, encerrar após prazo declarado na ficha. Condições próprias de objetivo, como falhar uma proteção ou esgotar um prazo de missão, podem encerrar antes disso quando previstas.

**Participante apto** é condição funcional: participante reconhecido, vivo e capaz de continuar naquela situação, conforme limites e regras do encontro. Um observador, espectador ou prestador protegido não mantém a tentativa ativa. Identificação concreta de área/distância, ausência, desconexão e elegibilidade ainda exige M09/M15; não usar a mera existência do UUID para manter uma tentativa indefinidamente. A aprovação não cria estado de incapacitado, ressurreição ou vidas extras.

| Situação | Tratamento pelo contrato aprovado |
|---|---|
| Um participante morre ou abandona; outro permanece apto | Continuar a tentativa dos presentes; a condição de quem saiu é avaliada separadamente |
| Todos são derrotados | Falha daquela tentativa, sem reconhecer vitória ou entregar conquista pelo simples fim |
| Não resta participante porque todos abandonaram | Prazo de abandono e encerramento conforme ficha; não decidir segundos/relógio nesta aprovação |
| Prova exige sobrevivência de todos ou objetivo específico | Declarar a exceção e sua consequência; não aplicar a todas as lutas por omissão |

Continuar para os presentes não autoriza automaticamente quem morreu a voltar, receber crédito como vencedor ou chamar substituto. Reentrada, respawn, entrada tardia, ajuda externa, escala e distribuição de resultado exigem M15 e a ficha. Alternativa: encerrar ao perder qualquer integrante; favorece provas coletivas rígidas, mas faz uma morte/saída encerrar a luta dos demais. Não é o padrão recomendado.

**Fronteiras do resultado:** declarar falha/abandono não cura o boss, remove atores, restaura construções, limpa cooldowns ou devolve gastos automaticamente. Não define a recuperação dos inimigos comuns de 02B/04B. Morte de gameplay conserva a natureza/conquistas pelos contratos pertinentes; consequências especiais e objetivos não são apagados por esta regra. Simultaneidade entre último golpe, morte de todos, término do objetivo e efeitos já emitidos precisa de ordenação antes de implementação; não inventar um vencedor por omissão.

### 8.18. Entregas restantes do ciclo e critérios futuros

Recursos/Vida e nova tentativa foram decididos em 07A/B, seções 8.19/8.20. Restante do ciclo: desistência explícita e retorno sem caminho; unload/reinício e retomada; relógios; resultados concorrentes e consumo único por sistemas responsáveis. Não herdar automaticamente relógios do jogador ou desaparecimento de projéteis efêmeros para todos os mobs/encontros. Região não simulada não é abandono detectado por magia nem exige carregamento forçado.

**Casos para revisão futura:** aceitar quest sem iniciar; atravessar a área; dupla confirma ao mesmo tempo; outra pessoa está no local; um participante morre e outro continua; todos derrotados; todos saem e retornam durante/depois do prazo; objetivo falha com alguém vivo; logout do último presente; unload/reinício; último projétil derrota o inimigo após morte do emissor. Casos e contratos pendentes, sem cenários executados.

### 8.19. Recuperação dos inimigos comuns — M09-07A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Recuperação gradual após período sem combate (recomendado)”. Cobertura: inimigos comuns do mundo aberto. Recuperam Vida lentamente após período sem combate nem dano; sofrer dano ou voltar à luta interrompe a recuperação. Espera, ritmo, condições objetivas de combate e eventuais diferenças de perfil exigem ficha/calibração, sem números presumidos.

A mudança para busca, recuo ou retorno de 02B/04B não restaura Vida por si só. Uma criatura que recua sob ataques não ganha cura instantânea, invulnerabilidade ou limpeza de recursos/tempos. O limite é a Vida máxima válida do perfil. M05/17/24 rege a recuperação do personagem; esta decisão amplia explicitamente a recuperação natural para os inimigos comuns no recorte aprovado, sem copiar valores do jogador.

**Recuperação natural e poder especial:** este padrão não concede regeneração rápida Hollow a todo corpo, cura durante a luta, regeneração de membros ou recuperação de quebra estrutural. Capacidades especiais mantêm suas condições/custos e exceções. Stamina/Energia de NPC, quando realmente usadas pelo repertório, precisam da ficha; não inventar barras ou reposição automática de todos os recursos por esta aprovação.

**Alternativa não escolhida:** nenhuma recuperação natural, apenas mecanismos específicos. Preservaria todo dano entre reencontros do mesmo ator, mas favoreceria desgaste indefinido por ataques espaçados. A decisão não resolve despawn, simulação descarregada ou cura offline; ciclo/relógios continuam no próximo bloco. Não altera elegibilidade de grind, predação ou recompensa por a criatura ter recuperado Vida.

### 8.20. Nova tentativa de prova/boss — M09-07B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Nova tentativa com estado inicial; checkpoints explícitos (recomendado)”. Cobertura: nova tentativa de prova/boss depois de encerrada a anterior. O adversário começa no estado inicial previsto pelo encontro, incluindo Vida, recursos de combate e fases daquela tentativa. Checkpoint só existe quando declarado na ficha. O padrão evita vencer por acumular dano de tentativas já encerradas.

**Tentativa não é alvo:** morte/saída individual com participante apto restante mantém o confronto ativo por 06B; não restaura o boss. Perder visão, mudar de alvo ou retornar à área também não inicia uma nova tentativa. Não aplicar reset de prova a um ator comum de mundo aberto só porque outro jogador se aproximou. Criatura compartilhada, outra tentativa ativa, efeitos anteriores e uso do local exigem validação antes de preparar o estado inicial; não sobrescrever uma luta em andamento.

Estado inicial pertence à versão/contexto do encontro, respeitando 01B e a escala ainda pendente; não é buff para acompanhar BP. As fichas precisam declarar Vida/recursos iniciais, disponibilidade inicial de técnicas, fases e checkpoint autorizado. Não presumir apagar consequências persistentes que pertencem ao poder, quest ou mundo. Gastos e tempos do jogador seguem seus contratos, sem cura, reembolso ou limpeza gratuita para tentar de novo.

**Mundo e conquistas:** reiniciar o adversário não reconstrói blocos, desfaz preparações do jogador ou restaura a arena. Conquistas/marcos persistentes permanecem com seus responsáveis; progresso parcial da tentativa só sobrevive quando o checkpoint o autoriza. Repetição não duplica recompensa, alma ou resultado; autoria/consumo ficam no bloco próprio. Alternativa não escolhida: preservar dano/fases parciais entre tentativas, favorecendo desgaste acumulado em vez de superar uma tentativa completa.

**Critérios futuros do bloco:** comum recuando sob dano; espera e cura gradual; novo combate interrompendo cura; prova em que um jogador morreu e outro continua; nova tentativa autorizada; checkpoint declarado; encontro anterior ainda ativo; mundo construído preservado. Nenhum cenário executado. Recuperação/despawn/offline e limpeza de efeitos continuam pendentes.

### 8.21. Interrupção da prova por descarregamento/reinício — M09-08A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Encerrar como interrompida; nova tentativa manual (recomendado)”. Cobertura: tentativa dirigida de prova/confronto preparado. Se sua região deixar de ser simulada ou o servidor reiniciar, encerrar como interrompida quando ainda não houver resultado confirmado. Conquistas e gastos registrados permanecem; nenhuma vitória, derrota de combate ou recompensa nasce só do descarregamento. Retomar a atividade exige confirmação de nova tentativa por 06A, com estado inicial/checkpoint previsto por 07B.

**Resultado confirmado não é reclassificado:** vitória, falha ou abandono já reconhecidos não viram interrupção neutra ao descarregar. Perdas e consequências especiais persistentes não são apagadas. Se apenas um participante sai enquanto os demais continuam aptos e a região segue simulada, 06B mantém a luta; o contrato não a encerra por todo logout individual. Abandono reconhecido e ausência ainda em avaliação precisam conservar autoria/resultado válidos.

Não restaurar golpes/cargas transitórios da tentativa antiga. Encerrar uma tentativa exige distinguir seus atores/efeitos próprios de fauna, projéteis e entidades de terceiros; a política concreta de limpeza precisa de ficha, sem apagar todos os seres próximos ou remover construções. Efeitos autônomos seguem seus contratos M06/M11; decidir a tentativa não revoga suas regras por associação. Atores residuais não podem virar uma segunda tentativa ativa ou uma fonte duplicada de recompensa.

**Limites:** não cria checkpoint automático, instanciação privada, carregamento forçado, teleporte, recuperação de itens ou reembolso. Relogar para explorar o resultado não deve reabrir um resultado já confirmado. Desconexão, reentrada, punições por abandono e justiça em servidor continuam com M15; classificar a interrupção não promete fuga segura ou absolvição de acontecimentos anteriores.

**Alternativa:** salvar a tentativa para retomar do ponto exato. Preserva andamento, mas exige representar participantes ausentes, tempos, fases, efeitos e arena alterada no mundo compartilhado. Não é o padrão recomendado nesta etapa; fichas com necessidade real de persistência podem propor exceção própria antes de implementá-la.

### 8.22. Relógio dos mobs comuns — M09-08B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Pausar tempos fora de simulação (recomendado)”. Cobertura: cooldowns e espera/recuperação natural de mobs comuns que continuem salvos. Regra: avançar somente durante sua simulação ativa. Região descarregada ou mundo/servidor parado pausa esses tempos; preservar Vida e tempo restante válido, sem completar cura por tempo fora de simulação. Ao retornar, a recuperação só continua se 07A e o estado atual ainda a permitirem.

Golpes incompletos não retomam como contato atrasado ao carregar. Isso não reembolsa gastos nem zera o cooldown já assumido. Identidade/perfil e Vida válida do mesmo ator não são sorteados novamente por carregar; não restaurar uma criatura morta ou fabricar um ator novo usando o estado de outro. Limpeza de alvo/memória e reavaliação sensorial precisam respeitar 02A/B, sem usar a posição secreta de um alvo distante para retomar navegação.

**Persistir não é tornar permanente:** esta aprovação não cancela despawn legítimo, salva todo mob para sempre ou define condições de remoção/criação. Primeiro se decide se o ator ainda existe; os valores preservados governam o mesmo ator quando ele é restaurado. Critérios de persistência/despawn, mobs nomeados, encontros especiais e animais invocados exigem seus responsáveis/fichas. Relógios de busca/retorno e prazos da tentativa precisam de definição compatível; não inserir valores por omissão.

| Dono/situação | Regra vigente |
|---|---|
| Personagem: cooldowns/Recovery já iniciados | M06 mantém relógio do mundo enquanto simulado, inclusive personagem offline; pausa com mundo/servidor parado |
| Mob comum salvo: cooldown/espera/recuperação | 08B determina tempo de simulação do próprio ator, sem progressão enquanto descarregado |
| Prova/confronto preparado descarregado ou reiniciado | 08A determina encerrar a tentativa não resolvida; confirmação inicia outra pelo perfil 07B |
| Reentrada individual, escala e crédito | Dependências M15; nenhum dos dois modelos concede esses resultados automaticamente |

**Alternativa de 08B:** contar os tempos pelo relógio do mundo mesmo sem simular a criatura. Exigiria definir quais efeitos são recalculados ao retornar e impedir cura/resultado incoerentes por salto de tempo; também não significa tempo real com servidor desligado. A escolha é nova para os mobs, não uma revisão do relógio do jogador.

**Critérios futuros:** mesma criatura antes/depois de unload; reinício durante ataque; cooldown parcialmente cumprido; cura interrompida por dano; passagem longa sem simulação; despawn real versus restauração; dois participantes e logout de um; falha confirmada antes de unload; checkpoint explícito e nova confirmação. Nenhum cenário executado. A aprovação não afirma que os saves atuais já cobrem todos esses dados/efeitos.

### 8.23. Revisão estrutural do ciclo — alcance e entregas

M09-06–08 definem início/falha coletiva, recuperação/nova tentativa e interrupção/relógios nos seus recortes. Não tornam todos os encontros executáveis. Esta revisão organiza as dependências sem promover propostas ou fixar números.

| Contrato já decidido | Entrega que continua necessária antes da respectiva implementação |
|---|---|
| 06A: confirmar prova preparada | Gatilho, requisitos, local, concorrência de tentativas e participantes autorizados — M09/M12/M15 |
| 06B: participantes aptos e falha coletiva | Área/ausência, prazo de abandono, objetivo, reentrada e simultaneidade de resultados — M09/M12/M15 |
| 07A: recuperação comum gradual | Espera, ritmo, critérios de combate e diferenças de perfil — M05/M09/M15 |
| 07B: estado inicial por nova tentativa | Versão, fases/recursos, checkpoint declarado e efeitos anteriores — M09/M12/M13 |
| 08A: interromper prova descarregada/reiniciada | Resultado persistente, limpeza dos atores próprios e reconciliação sem duplicação — M09/M12/M16 |
| 08B: tempos do mob salvo por simulação | Dados efetivamente preservados, critérios de persistência/despawn e reconciliação ao carregar — M09/M10/M16 |
| 02B/04B: busca/retorno e tentativa sem solução | Parâmetros e comportamento com retorno bloqueado — M09/M10/M15 |

Consolidação documental, sem revisão de código adicional ou cenário executado. Fichas e integração precisam resolver as entregas do seu recorte, inclusive proteção de construções e ajuda externa. O próximo bloco trata evidência de contribuição e política de prêmios; não é aceite da IA ou encerramento de M09.

### 8.24. Contribuição no mundo aberto fora da party — M09-09A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Reconhecer contribuição útil mesmo fora da party (recomendado)”. Regra: reconhecer contribuição útil no encontro comum do mundo aberto mesmo sem party compartilhada. A escolha nova é o acesso ao reconhecimento para cooperação espontânea; participação efetiva, ausência de pedágio pelo último golpe e grind ativo já vêm dos contratos anteriores, sem nova votação.

Contribuições avaliáveis: dano ao adversário; controle que de fato interfere no confronto; proteção/mitigação útil; cura de dano real ligada à situação; objetivo pertinente ao encontro. Ação vazia, cura excedente sem efeito, proximidade, vínculo de party ou um toque simbólico não garantem elegibilidade. Critérios de relevância, registros e limiares ainda pertencem às fichas/M15. Não reduzir suporte a uma equivalência numérica inventada de dano nem exigir uma classe fixa de curador/tanque.

**Reconhecimento não é distribuição:** M09 produz evidências de ação, autoria, contexto e resultado; os consumidores avaliam o direito concreto. A aprovação não dá prêmio integral a cada presente, partilha igual, drop pessoal, fração de alma, Mastery comprada ou crédito de campanha fora dos próprios requisitos. A morte/saída de um participante mantém a regra 06B; seu crédito final e exigência de presença ainda precisam de M15. Origem/party não cria PvP automático nem nova amizade institucional.

**Cobertura:** encontros comuns do mundo aberto. Provas e bosses preparados conservam suas condições de admissão/terceiros ainda a detalhar; a aprovação não permite entrar livremente numa tentativa alheia. Dano ao próprio aliado/pet, cura artificial ou repetição sem situação real não deve fabricar contribuição. Arena construída continua válida; avaliar sua contribuição exige contexto, sem premiar automaticamente qualquer dono de bloco próximo.

Alternativa: reservar o crédito a quem iniciou o combate e à sua party. Ajuda a reservar uma caça, mas exige definir primeiro contato, concorrência e participação externa; pode desestimular ajuda espontânea. Nenhuma opção autoriza roubar uma oportunidade já reconhecida ou decidir sozinho o destino de uma alma.

### 8.25. Prêmio principal e recompensas repetíveis — M09-09B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Prêmio principal único; recompensas repetíveis separadas (recomendado)”. Para quests de campanha e provas de conquista, regra de prêmio principal único por personagem; caçadas, treinos e atividades declaradas repetíveis podem ter SP/loot próprios. Uma repetição válida do combate não concede outra cópia do prêmio único. Não tornar toda atividade repetível nem toda criatura fonte de SP/loot por esta escolha.

| Resultado/registro | Tratamento pelo contrato aprovado |
|---|---|
| Primeira conclusão elegível de campanha/conquista | Reconhecer marco e prêmio principal conforme requisitos pessoais, sem depender de ser o primeiro jogador do servidor |
| Repetição ou ajuda a outro jogador | Não pagar de novo o prêmio principal já reconhecido; eventuais recompensas repetíveis exigem ficha |
| Caçada/treino declarado repetível | Avaliar participação, ameaça/contexto e resultado para recompensa própria; grind ativo continua possível |
| Reenvio de evento ou pedido da mesma conclusão | Processar sem duplicar concessão; não é uma nova atividade válida |

Esta é a fronteira aprovada entre resultado do encontro e configuração de recompensa. Valores, chances, quantidade, calendário, periodicidade, estoque e divisão pertencem a M11/M12/M15, com fontes/calibração de SP em M05. Não cria limite diário, retorno decrescente obrigatório, taxa de repetição ou recompensa por todo golpe. Repetir a mesma espécie ou usar uma arena não invalida automaticamente a fonte aprovada; automação integral e exploração de eventos seguem os limites herdados.

Aquisição por SP e prática seguem REV-SP-02: ter vitória/marco disponibiliza a aquisição conforme os requisitos, sem substituir compra por concessão automática de skill/forma. Prêmio e saldo não são Mastery. As fontes iniciais precisam continuar viáveis antes da primeira compra, sem depender de uma recompensa que exija a própria skill ainda bloqueada.

**Continuidade/reset:** cada personagem tem seus próprios requisitos e registros; prêmio de campanha não vira prêmio único global de servidor. M04/RAC-28 reseta campanha e progresso Bleach, preservando itens comuns; o tratamento das flags de prêmio na nova jornada e a prevenção de multiplicação de itens preservados exigem M04/M11/M12 antes do serviço executável. Não escolher silenciosamente uma trava eterna por UUID nem permitir uma rota de geração ilimitada de itens por reset. Migração e repetição por era/checkpoint exigem ficha.

Alternativa: pagar o prêmio principal novamente a cada conclusão válida de uma atividade refazível. É simples de apresentar, mas pode concentrar toda a progressão em reabrir a prova de maior retorno e exige balancear suas concessões repetidas. A separação recomendada preserva fontes repetíveis sem confundir conquista persistente com renda recorrente.

### 8.26. Consumidores de resultado e predação — fronteiras pendentes

Participação, vitória, recompensa, progresso de quest e consumo evolutivo são saídas distintas. A oportunidade de devoração continua exigindo presa Hollow NPC inimiga elegível, participação efetiva e interação concluída por M07-04A/B. O crédito aberto aprovado não duplica alma nem decide exclusividade, cópia para cada participante ou elegibilidade de qualquer modo de derrota.

Antes da ficha multirracial, aplicar a adaptação vigente M04/RAC-19 e especificar resolução definitiva/oportunidade com M06/M07/M09/M15, sem introduzir diferença automática pela raça do atacante. Conferência em 8.27; consumo único aprovado em 8.28. Invocações, clones, aliados, pets e representações repetíveis de personagens precisam de classificação; natureza Hollow por si só não basta. Recompensa de combate não depende automaticamente de existir algo devorável, nem devorar compra Mastery.

**Critérios futuros:** dois desconhecidos ajudam; suporte útil sem maior dano; cura vazia; toque no fim; pessoa chegou sem requisitos da campanha; repetição para ajudar iniciante; mesmo evento entregue duas vezes; caça ativa versus morte automática; reset de jornada com itens comuns preservados; derrota por métodos raciais diferentes. Nenhum cenário executado; critérios e distribuição ainda precisam de contrato.

### 8.27. Destino da alma — aplicar RAC-19 vigente

**Conferência em 23/10/2026:** [M04/RAC-19](04-racas-e-origens.md#rac-19--purificação-shinigami-e-destruição-quincy) mantém em backlog a diferenciação sistêmica entre purificação Shinigami e destruição Quincy. A decisão vigente impede consequências diferentes na derrota comum apenas pela raça do atacante; não foi reaberta nem revisada nesta rodada. A elegibilidade da predação não deve virar punição automática para um Hollow cooperar com Shinigami ou Quincy por causa do golpe final.

As referências anteriores de M09 a resolver destino da alma são entregas de ficha/integração, não aprovação para sobrepor RAC-19. Presa inimiga elegível, participação efetiva e consumo concluído continuam necessários. Uma ação/cena que declare resolução definitiva precisa tratar suas consequências antes de produzir oportunidade de consumo; uma alma efetivamente removida não é recriada para dar progresso. Não se escolheu aqui uma nova técnica de purificação, interação racial ou consequência global de equilíbrio dos mundos.

**ENC-S05, consulta em 23/10/2026:** reconsultados os glossários oficiais Pierrot [ha](https://pierrot.jp/title/bleach/keyword_ha.html), Hollow/alimentação de almas; [ka](https://pierrot.jp/title/bleach/keyword_ka.html), Quincy/eliminar Hollows; e [sa](https://pierrot.jp/title/bleach/keyword_sa.html), funções espirituais dos Shinigami. Anime oficial complementar e arquivo da época de exibição, não capítulos/episódios integrais. Confirmam conceitos gerais distintos; não especificam predação cooperativa, prioridade, cadáver, divisão ou temporizador do mod. Não importar outras generalizações históricas da página como revisão de todo o cânone.

### 8.28. Uma devoração por presa — M09-10A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Uma devoração por presa, com revezamento (recomendado)”. Cada presa elegível permite uma única devoração concluída por um Hollow participante elegível. A oportunidade se esgota ao consumo válido; outros participantes mantêm reconhecimento e recompensas que suas regras autorizarem, sem receber automaticamente progresso por esse consumo. Caçar em grupo exige revezar presas para desenvolver a predação de vários Hollows.

**Escolha de gameplay:** este contrato define uma oportunidade, sem afirmar que toda presa contém literalmente uma só alma ou uma quantidade canônica fixa. Não divide uma presa em porções de progresso nem cria uma cópia para cada participante. M09-09A abre reconhecimento de contribuição útil; não multiplica a oportunidade de M07. SP/loot, prática/Mastery e progresso evolutivo por consumo continuam distintos.

Quem participou pode ser avaliado para a oportunidade; não exigir último golpe, mas também não permitir entregar predação a espectador/iniciante sem participação efetiva. Devorar exige a interação própria já aprovada, sem consumo automático, item de alma transferível ou recurso extra. Só concluir registra consumo; cancelar não concede progresso nem marca conclusão bem-sucedida. Representação, prazo e validade da presa ainda exigem ficha.

**Distribuição definida posteriormente por M09-10B:** primeiro participante elegível a concluir validamente consome, conforme 8.29. Não há prioridade temporária, fila ou rodízio automático. “Revezamento” de 10A representa alternar presas por cooperação/acordo, sem garantia imposta pelo sistema. Critérios concretos de elegibilidade, duração/expiração, empate técnico e comunicação continuam nas fichas M09/M15/M16. O servidor precisa garantir uma única conclusão válida, sem duas concessões simultâneas.

**Alternativa não escolhida:** dividir conteúdo finito entre participantes, cada um concluindo a própria interação. Permitiria avanço em grupo na mesma presa, mas exigiria porções, mínimos de contribuição, valor total e marcos compatíveis. Nenhum desses sistemas foi aprovado. A regra escolhida não decide taxas/quantidades por estágio, cura ao comer ou aquisição de poderes de outra criatura.

**Critérios futuros:** dois Hollows elegíveis; ajudante fora de party; um participante sem requisitos; consumo simultâneo; cancelamento antes da conclusão; oportunidade expirada; presa com várias contribuições; cooperação entre raças sob RAC-19; invocação/clone versus presa real. Nenhum cenário executado; elegibilidade, concorrência, parâmetros e fichas permanecem condições de implementação.

### 8.29. Primeiro elegível a concluir consome — M09-10B DECIDIDO

**Decidido em 23/10/2026. Resposta:** “Primeiro elegível a concluir consome”. O usuário escolheu a segunda opção, diferente da recomendação inicial de prioridade temporária. Qualquer participante elegível pode iniciar a interação enquanto a oportunidade for válida; o primeiro a concluir validamente consome e encerra a oportunidade por 10A. Iniciar/clicar não reserva a presa nem bloqueia outro elegível.

| Situação | Tratamento pelo contrato escolhido |
|---|---|
| Vários elegíveis iniciam | Interações podem disputar a mesma oportunidade; nenhuma vez é reservada |
| Primeira conclusão válida | Servidor registra um único consumo e encerra a oportunidade para todos |
| Outra interação termina depois de consumo já reconhecido | Recusar concessão e informar que a presa já foi consumida; não duplicar progresso |
| Interação cancelada ou participante fica inelegível | Não registra consumo; outros elegíveis ainda podem concluir enquanto houver oportunidade válida |
| Nenhum elegível conclui antes da expiração geral | Não registrar consumo; duração e encerramento precisam de ficha |

Elegibilidade continua exigindo Hollow compatível, presa classificada, participação efetiva e requisitos válidos; a opção não autoriza espectador que só chegou para clicar. O último golpe não garante consumo, e maior dano não reserva direito. Reconhecimento/recompensas de outros participantes por 09A/B permanecem independentes do consumidor único.

**Consequência da escolha:** pode haver disputa entre participantes elegíveis; tempo de início, execução e conexão podem influenciar o resultado. Não prometer revezamento garantido. Jogadores podem combinar alternância voluntária, mas não há fila, designação automática ou passagem de prioridade pelo sistema. Não criar PvP obrigatório para resolver a disputa.

O servidor valida começo, duração e conclusão; um timestamp alegado pelo cliente não prova término. Consumo já reconhecido não é revertido por outro pacote atrasado. Conclusões no mesmo tick precisam de ordenação consistente e uma única concessão, com regra técnica/feedback definidos antes de implementar. Não presumir empate compartilhado, sorteio ou segunda cópia. Ficha deve declarar duração, interrupções, expiração geral e alcance; não fixar números nesta decisão.

**Proposta não escolhida, preservada no histórico:** prioridade temporária a um elegível com passagem por recusa/inelegibilidade/prazo. Não implementar reserva ou rodízio como se tivessem sido aprovados. Perda de validade da oportunidade e lifecycle seguem os contratos pertinentes, sem recriar presa por carregar, morrer ou reenviar pedido.

**Critérios futuros:** dois elegíveis iniciam em momentos distintos; iniciador cancela; outro conclui primeiro; apoio fora da party; pessoa sem participação tenta consumir; duas conclusões no mesmo tick; pacote duplicado/atrasado; presa expira; cooperação por alternância voluntária. Nenhum cenário executado; integração e parâmetros permanecem pendentes.

## 9. Integrações

| Entrada | Resposta necessária / saída | Limite |
|---|---|---|
| M08: orientação e treino | Situação de prática com ações já acessíveis e representação adequada | Não exigir a skill bloqueada como único meio de obtê-la; não aprovar treino não letal por omissão |
| M07: predação | Identidade da presa, resultado e evidência de participação | Destino da alma/purificação/destruição e oportunidade de consumo ainda precisam de resolução conjunta; não duplicar alma ou crédito |
| M12: pedido de encontro | Validar perfil/contexto e produzir resultado reconhecível | Quest observa e solicita; não calcula combate ou edita atributos diretamente |
| M10: região/terreno | Criação e movimentação compatíveis com espaço e mundo | Sem carregar todo o mapa ou remover blocos para fazer o corpo caber por presunção |
| M11/M12/M15: recompensa | Evento de conclusão/participação com autoria | Não pressupor drop, valor de SP, recompensa por invocação ou ganho repetido por reabrir diálogo |
| M03/M13: estado narrativo | Representação correspondente à era/aparição | Forma posterior não fica disponível porque há skin/modelo; repetição não mata o NPC permanente |
| M14: apresentação | Explicar ameaça, preparo do golpe, acerto, falha e encerramento | Canal visual/sonoro/UI e números finais ainda exigem ficha |

## 10. Conteúdo inicial

**M09-11A aprovado em 23/10/2026:** seleção das três variantes Hollow existentes e do boss atual como amostra inicial de fichas documentais. [Catálogo funcional M09a](09a-catalogo-funcional-de-adversarios-e-encontros.md) criado, com funções/papel de 11B/C aprovados. A seleção não aprova produção, regiões, ordem de desbloqueio ou todos os campos executáveis.

| Ficha da amostra | O que exercita | O que falta demonstrar |
|---|---|---|
| [ENC-H01 — bruto](09a-catalogo-funcional-de-adversarios-e-encontros.md#91-enc-h01--bruto-terrestre) | Distância, antecipação, guarda/esquiva e punição após golpe | Cadência/alcance legíveis em terreno real; ameaça apropriada ao contexto |
| [ENC-H02 — ágil](09a-catalogo-funcional-de-adversarios-e-encontros.md#92-enc-h02--ágil-terrestre) | Ritmo curto, reposicionamento e escolha do momento de atacar | Não virar sequência inevitável de acertos nem depender só de Vida baixa |
| [ENC-H03 — voador](09a-catalogo-funcional-de-adversarios-e-encontros.md#93-enc-h03--voador) | Leitura da aproximação, janela próxima do chão e retirada | Solução com repertório inicial pertinente, teto/parede e terreno irregular; não exigir arco/Cero/voo a toda origem |
| [ENC-H04 — chefe](09a-catalogo-funcional-de-adversarios-e-encontros.md#94-enc-h04--chefe-hollow-atual) | Golpe próximo, investida e impacto de área com preparação | Início/fim/falha, arena, recuperação da tentativa, participantes e recompensa; modelo existente não prova esses contratos |

**Necessidades das quatro origens:** Shinigami precisa de prática básica antes de Shunpo; Quincy de tarefa acessível antes do arco e de foco disponível antes de usá-lo; Fullbringer de exercício alcançável antes de Bringer Light; Hollow de caça real elegível, sem o veterano vencer por ele. Nem todas precisam cumprir a mesma missão. Uma amostra de combate compartilhado não comprova os quatro prólogos ou a oportunidade de devoração multirracial.

Não está decidido que o primeiro inimigo do jogador será o bruto atual, que todas as variantes aparecerão no primeiro local ou que o boss será o encerramento obrigatório do prólogo. Dados atuais não foram calibrados para esse objetivo.

## 11. Visão completa

Hollows de comportamentos distintos; classes Menos; Arrancars; adversários institucionais e canônicos por era; treinos, provas, patrulhas e confrontos especiais. Conteúdo inclui também seres que não são adversários: Plus e Jigokuchō exigem suas próprias funções/interações, sem serem incluídos por padrão no catálogo de inimigos ou loot.

Invocações, clones e criaturas de técnicas não se tornam fauna ou presas elegíveis automaticamente. Uma representação nomeada requer identidade narrativa; uma família recorrente requer classificação própria. A lista recebida de assets continua banco de candidaturas, com prioridades a decidir pela fatia jogável e por suas dependências.

## 12. Decisões e entregas abertas

### 12.1. M09-01A — DECIDIDO — Natureza, perfil e papel separados

**23/10/2026. Resposta:** “Separar natureza, combate e papel no encontro (recomendado)”. Cobertura em 7.1: natureza/estágio, modo de lutar e papel comum/elite/chefe distintos; elite não é apenas mais Vida. Não aprova uma classificação racial nova, ficha concreta, fase obrigatória ou asset.

**Alternativa não escolhida:** classificação única de inimigos. Facilitaria uma lista inicial, mas misturaria estágio, modo de lutar e importância; reduziria o reaproveitamento e confundiria evolução do personagem com ameaça do encontro.

### 12.2. M09-01B — DECIDIDO — Força do mundo aberto por perfil/contexto

**23/10/2026. Resposta:** “Força própria por perfil e contexto (recomendado)”. Cobertura em 8.1. Ameaças não aumentam automaticamente por crescimento do jogador; voltar permite sentir evolução e encontrar perigo superior permite recuar. Versões difíceis e party exigem políticas explícitas.

**Alternativa não escolhida:** adaptar automaticamente a força ao jogador. Manteria pressão mais uniforme, mas reduziria a sensação de superar ameaças conhecidas e exigiria resolver de qual jogador vem a escala num mundo compartilhado. A decisão não escolhe fórmula de escala cooperativa.

### 12.3. Sequência dos próximos blocos

| Bloco | Escolhas/entregas necessárias | Responsáveis |
|---|---|---|
| Detecção e perseguição | Sentidos utilizados, aquisição/perda do alvo, memória da última posição, limites e retorno; separar ver, sentir e poder alcançar | M09 com M06/M08/M15 |
| Hostilidade e alvos | M09-03A/B aprovados em 8.8/8.9 e 12.6/12.7; critérios e categorias concretas por perfil continuam pendentes | M09 com M04/M08/M15 |
| Terreno e construções | M09-04A/B aprovados em 8.10–8.12 e 12.8/12.9. Meios/capacidades concretos e retorno bloqueado ainda exigem ficha | M09/M10/M15 |
| Execução e contrajogo | Herança consolidada em 8.13; M09-05A aprovado sobre momento de fixação da direção em 8.14/12.10. Fases/geometria e parâmetros concretos exigem fichas | M09/M06/M14/M15 |
| Ciclo do encontro | 06–08 aprovados: início/falha coletiva, recuperação/nova tentativa e interrupção/relógios. Despawn, retorno bloqueado, parâmetros, efeitos residuais e concorrência ainda exigem fichas | M09/M12/M15/M16 |
| Autoria, recompensa e consumo | 09A/B aprovados em 8.24/8.25 e 12.17/12.18: reconhecimento fora da party e prêmio único/repetível. Consumo único/primeira conclusão elegível decididos em 10A/B; representação/validade, resultado definitivo, valores e distribuição de SP/loot ainda exigem fichas | M09/M04/M07/M11/M12/M15 |
| Fichas e amostra | 11A/B/C aprovados: seleção/funções/papel em M09a. Regiões/era, parâmetros, pré-requisitos e critérios executáveis continuam pendentes | M09 com módulos de conteúdo e M16 |

M09-01A/B, 02A/B e 03A/B estão respondidos, em 12.1–12.7; 04A/B estão respondidos em 12.8/12.9. M09-05A está respondido em 12.10. M09-06A/B estão respondidos em 12.11/12.12; 07A/B, em 12.13/12.14. M09-08A/B estão respondidos em 12.15/12.16. M09-09A/B estão respondidos em 12.17/12.18; 10A está respondido em 12.19. M09-10B está respondido em 12.20; 11A, em 12.21. M09-11B/C estão respondidos em 12.22/12.23; não há pergunta pendente desses blocos. M09-12A/B receberam diretrizes de fidelidade em 12.24/12.25; M09-12C está decidido em 12.26; nenhuma resposta pendente deste pacote. A tabela orienta a sequência; não aprova suas soluções.

### 12.4. M09-02A — DECIDIDO — Sentidos declarados por perfil

**Pergunta enviada em 23/10/2026:** Para detectar o jogador, recomendo visão direta e percepção espiritual conforme o perfil do inimigo. Quem consegue sentir Reiatsu pode investigar uma direção aproximada mesmo atrás de paredes, respeitando Supressão, distância e domínio; isso não entrega a posição exata para atacar. Supressão também não esconde um corpo que o inimigo está vendo. Adotamos esse modelo ou começamos todos os inimigos apenas com visão direta?

**Opções:** “Visão e percepção conforme o perfil (recomendado)” / “Somente visão direta nesta etapa”. **Resposta em 23/10/2026:** “Visão e percepção conforme o perfil (recomendado)”. Primeira opção aprovada, com cobertura/limites de 8.4/8.6. Mantém a utilidade de ocultação e sentidos espirituais; exige capacidade sensorial e informações autorizadas por perfil. A alternativa simplifica a primeira entrega, mas adia a reação dos inimigos ao sinal espiritual.

### 12.5. M09-02B — DECIDIDO — Busca limitada e retorno

**Pergunta enviada em 23/10/2026:** Para a perseguição comum no mundo aberto, recomendo: ao perder sinais atuais do alvo, o inimigo procura por um período limitado perto da última posição conhecida e depois volta à sua área se não reencontrá-lo. Um sinal espiritual ainda detectável pode orientar a busca; ficar atrás de uma parede ou ligar Supressão não apaga a perseguição imediatamente. Limites variam por perfil, e chefes terão condições próprias. Adotamos isso ou a perseguição continua até matar o alvo ou ele sair da região carregada?

**Opções:** “Busca limitada e retorno à área (recomendado)” / “Perseguição contínua enquanto possível”. **Resposta em 23/10/2026:** “Busca limitada e retorno à área (recomendado)”. Primeira opção aprovada, com cobertura/limites de 8.5/8.6. Permite fugir ao romper contato e impede caça interminável baseada em informação oculta. Exige parâmetros e retorno válidos. A alternativa mantém pressão prolongada, mas pode arrastar ameaças por grandes distâncias e reduzir o valor de despistar.

### 12.6. M09-03A — DECIDIDO — Motivos de hostilidade por perfil

**Pergunta enviada em 23/10/2026:** Para os mobs comuns do mundo aberto, recomendo três comportamentos de hostilidade conforme o perfil: agressivo, que procura alvos elegíveis; territorial, que reage à invasão de uma área definida; e reativo, que luta quando provocado. Cada ficha define contra quem e em quais condições isso vale, respeitando relações e proteções já aprovadas. Não seria a mesma regra para todos os Hollows. Adotamos essa variedade ou começamos apenas com inimigos que atacam ao detectar um alvo elegível?

**Opções:** “Hostilidade agressiva, territorial ou reativa por perfil (recomendado)” / “Somente agressão ao detectar nesta etapa”. **Resposta em 23/10/2026:** “Hostilidade agressiva, territorial ou reativa por perfil (recomendado)”. Primeira opção aprovada, com cobertura/limites em 8.8. A variedade permite caça, defesa de território e encontros evitáveis por comportamento; exige condições e relações explícitas em cada ficha. A alternativa reduz o escopo inicial, mas deixa todos os inimigos comuns oferecidos com o mesmo motivo básico para atacar. Não escolhe a lista de presas/alvos de cada criatura.

### 12.7. M09-03B — DECIDIDO — Estabilidade e troca motivada de alvo

**Pergunta enviada em 23/10/2026:** Para escolher entre vários alvos, recomendo que o inimigo comum mantenha o alvo atual enquanto houver motivo e informação válidos para persegui-lo. Pode trocar por perda do alvo, uma ameaça mais relevante ou um objetivo do perfil; aproximar-se ou acertar um golpe não força a troca sozinho. Isso evita alternância constante, sem criar uma barra universal de ameaça. Adotamos esse modelo ou priorizamos sempre o último agressor detectado?

**Opções:** “Alvo estável, com motivos definidos para trocar (recomendado)” / “Priorizar o último agressor detectado”. **Resposta em 23/10/2026:** “Alvo estável, com motivos definidos para trocar (recomendado)”. Primeira opção aprovada, com cobertura/limites em 8.9. Alvo estável favorece leitura e cooperação, mas exige critérios de troca e não pode tornar a IA incapaz de reagir a outra ameaça. Priorizar o último agressor detectado é simples de entender, mas facilita alternar golpes para fazer a criatura girar entre jogadores. Não decide barra de ameaça, habilidade de provocação ou política de boss.

### 12.8. M09-04A — DECIDIDO — Alteração de blocos por exceção

**Pergunta enviada em 23/10/2026:** Para proteger o valor das construções, recomendo que mobs comuns não quebrem nem coloquem blocos por padrão, inclusive ao atacar. Destruição pode existir em técnicas ou encontros específicos, definidos e comunicados, respeitando as regras do servidor. Isso permite usar muros e abrigos, sem excluir combates destrutivos do jogo completo. Adotamos esse padrão ou permitimos que inimigos capazes abram caminho destruindo blocos durante a perseguição comum?

**Opções:** “Sem alteração de blocos por padrão; exceções definidas (recomendado)” / “Destruição de passagem conforme capacidade do inimigo”. **Resposta em 23/10/2026:** “Sem alteração de blocos por padrão; exceções definidas (recomendado)”. Primeira opção aprovada, com cobertura/limites em 8.10: construção útil no mundo comum, mantendo possibilidade de destruição explicitamente preparada. Exige definir exceções e integração com regras do servidor antes de disponibilizá-las; não concede restauração automática.

### 12.9. M09-04B — DECIDIDO — Tentativa limitada sem solução

**Pergunta enviada em 23/10/2026:** Se enxergar o jogador, mas não conseguir alcançá-lo, recomendo que o inimigo comum tente contornar ou usar ataques que já possui, respeitando alcance e obstáculos. Se continuar sem solução por um período limitado, abandona aquela tentativa e procura retornar à sua área, mesmo ainda vendo o alvo. Não ganha teleporte, ataque novo, cura ou invulnerabilidade. Adotamos isso ou ele deve permanecer pressionando enquanto perceber o jogador?

**Opções:** “Tentativas limitadas e recuo sem solução (recomendado)” / “Manter pressão enquanto perceber o alvo”. **Resposta em 23/10/2026:** “Tentativas limitadas e recuo sem solução (recomendado)”. Primeira opção aprovada, com cobertura/limites em 8.11: resposta limitada à falta de solução mesmo com percepção atual, distinta da busca por perda de contato. Mantém dano válido e compromissos; tempos, retomada, retorno bloqueado e ciclo permanecem por especificar.

### 12.10. M09-05A — DECIDIDO — Ajuste inicial e compromisso direcional

**Pergunta enviada em 23/10/2026:** Para os golpes direcionais comuns dos inimigos, recomendo permitir ajuste limitado da direção no começo do preparo e fixá-la na parte final, antes do golpe. Assim, uma esquiva muito cedo ainda pode ser acompanhada, mas o inimigo não gira livremente para acertar depois de se comprometer. Ataques que continuam seguindo o alvo precisam de regra e sinal próprios. Adotamos esse modelo ou fixamos a direção desde o início do preparo?

**Opções:** “Ajustar no início e fixar antes do golpe (recomendado)” / “Fixar desde o início do preparo”. **Resposta em 23/10/2026:** “Ajustar no início e fixar antes do golpe (recomendado)”. Primeira opção aprovada, com cobertura/limites em 8.14. A escolha trata do momento de fixação, não repete a decisão de que trocar alvo não apaga compromissos. Não aprova parâmetros, golpe concreto, perseguição ativa universal ou nova regra de interrupção/Recovery.

### 12.11. M09-06A — DECIDIDO — Confirmar início do confronto preparado

**Pergunta enviada em 23/10/2026:** Para provas e confrontos preparados por quest, recomendo um início deliberado: o jogador conhece objetivos e riscos, cumpre os requisitos e confirma quando começar. Aceitar a quest ou passar perto não inicia essa tentativa sozinho. Emboscadas e eventos automáticos continuam possíveis quando definidos como tais; inimigos do mundo aberto seguem suas regras de hostilidade. Adotamos esse padrão ou iniciamos essas provas automaticamente ao entrar na área?

**Opções:** “Confirmar o início das provas e confrontos preparados (recomendado)” / “Iniciar ao entrar na área da prova”. **Resposta em 23/10/2026:** “Confirmar o início das provas e confrontos preparados (recomendado)”. Primeira opção aprovada, com cobertura/limites em 8.16. Não aprova menu, teleporte, instância, composição de party ou confirmação para todo encontro hostil.

### 12.12. M09-06B — DECIDIDO — Continuidade e falha coletiva

**Pergunta enviada em 23/10/2026:** Nos confrontos cooperativos de combate, recomendo continuar enquanto houver participante apto no encontro. Morrer ou sair não encerra a luta dos demais; se todos forem derrotados, a tentativa falha. Se todos abandonarem, ela termina após um prazo definido na ficha. Objetivos e provas especiais podem declarar outras condições de falha. Adotamos esse padrão ou perder qualquer integrante já encerra a tentativa coletiva?

**Opções:** “Continuar com participantes aptos; falha coletiva (recomendado)” / “Encerrar ao perder qualquer integrante”. **Resposta em 23/10/2026:** “Continuar com participantes aptos; falha coletiva (recomendado)”. Primeira opção aprovada, com cobertura/limites em 8.17. Não aprova retorno de mortos, entrada tardia, recompensa individual, parâmetros, reset de recursos ou restauração de arena.

### 12.13. M09-07A — DECIDIDO — Recuperação gradual fora de combate

**Pergunta enviada em 23/10/2026:** Para inimigos comuns do mundo aberto, recomendo recuperar Vida lentamente após um período sem combate nem dano, com ritmo definido pelo perfil. Voltar à área de origem não cura instantaneamente; sofrer dano ou voltar à luta interrompe essa recuperação. Regenerações especiais continuam com regras próprias. Adotamos esse padrão ou a Vida perdida só volta por uma capacidade ou intervenção específica, sem recuperação natural dos mobs?

**Opções:** “Recuperação gradual após período sem combate (recomendado)” / “Sem recuperação natural; somente mecanismos específicos”. **Resposta em 23/10/2026:** “Recuperação gradual após período sem combate (recomendado)”. Primeira opção aprovada, cobertura e alternativa em 8.19. Não define tempos/ritmos, cura especial ou simulação offline.

### 12.14. M09-07B — DECIDIDO — Estado inicial por nova tentativa

**Pergunta enviada em 23/10/2026:** Ao começar uma nova tentativa de prova ou boss, depois de encerrar a anterior, recomendo restaurar o inimigo ao estado inicial previsto: Vida, recursos de combate e fases daquela tentativa. Checkpoints só existem quando declarados na ficha; conquistas persistentes permanecem. Isso não cura o jogador, devolve gastos ou restaura construções. Adotamos esse padrão ou mantemos o dano e as fases parciais do inimigo entre tentativas?

**Opções:** “Nova tentativa com estado inicial; checkpoints explícitos (recomendado)” / “Preservar dano e fases parciais entre tentativas”. **Resposta em 23/10/2026:** “Nova tentativa com estado inicial; checkpoints explícitos (recomendado)”. Primeira opção aprovada, cobertura e alternativa em 8.20. Não redefine tentativa ainda ativa, cura jogador ou restaura construções.

### 12.15. M09-08A — DECIDIDO — Interrupção e nova tentativa manual

**Pergunta enviada em 23/10/2026:** Se a região de uma prova ou confronto preparado deixar de ser simulada, ou o servidor reiniciar, recomendo encerrar a tentativa como interrompida, sem presumir vitória ou derrota em combate. Conquistas já registradas e gastos permanecem; voltar exige confirmar uma nova tentativa, usando o estado inicial ou checkpoint previsto. Não se retoma um golpe antigo ao carregar. Adotamos isso ou salvamos a tentativa para continuar exatamente de onde parou?

**Opções:** “Encerrar como interrompida; nova tentativa manual (recomendado)” / “Salvar e retomar a tentativa em andamento”. **Resposta em 23/10/2026:** “Encerrar como interrompida; nova tentativa manual (recomendado)”. Primeira opção aprovada, cobertura/limites em 8.21; não reclassifica resultado confirmado, concede fuga segura ou escolhe limpeza indiscriminada de atores/arena.

### 12.16. M09-08B — DECIDIDO — Tempos dos mobs por simulação

**Pergunta enviada em 23/10/2026:** Para os tempos próprios dos mobs comuns — cooldowns e espera/recuperação natural — recomendo contar apenas enquanto a criatura estiver sendo simulada na região ativa. Descarregar a região ou parar o servidor pausa esses tempos e preserva a Vida salva, sem cura offline; golpes incompletos não retomam ao carregar. Isso não torna todo mob permanente nem muda o relógio já aprovado do jogador. Adotamos esse modelo ou contamos esses tempos pelo relógio do mundo mesmo com a criatura descarregada?

**Opções:** “Pausar tempos fora de simulação (recomendado)” / “Contar tempos pelo relógio do mundo”. **Resposta em 23/10/2026:** “Pausar tempos fora de simulação (recomendado)”. Primeira opção aprovada, cobertura/limites em 8.22; relógio do jogador preservado, despawn/persistência e demais tempos ainda exigem fichas.

### 12.17. M09-09A — DECIDIDO — Cooperação espontânea no mundo aberto

**Pergunta enviada em 23/10/2026:** Nos encontros comuns do mundo aberto, recomendo reconhecer contribuição útil mesmo entre jogadores fora da mesma party: dano, controle, proteção, cura ou objetivo que realmente ajude naquele confronto. Isso dá elegibilidade para avaliação, sem prometer prêmio igual, duplicar drops ou dividir uma alma automaticamente. Provas e bosses preparados mantêm suas regras próprias de entrada. Adotamos essa cooperação espontânea ou reservamos o crédito ao jogador ou party que iniciou o combate?

**Opções:** “Reconhecer contribuição útil mesmo fora da party (recomendado)” / “Reservar crédito a quem iniciou e à sua party”. **Resposta em 23/10/2026:** “Reconhecer contribuição útil mesmo fora da party (recomendado)”. Primeira opção aprovada, cobertura/limites em 8.24; reconhecimento não define valor/distribuição, admissão em prova ou destino da alma.

### 12.18. M09-09B — DECIDIDO — Prêmio único e recompensa repetível separados

**Pergunta enviada em 23/10/2026:** Para quests de campanha e provas de conquista, recomendo entregar o prêmio principal uma vez por personagem. Caçadas, treinos e outras atividades declaradas repetíveis podem continuar rendendo SP ou loot conforme suas fichas; repetir uma luta não paga novamente o prêmio único. Isso mantém fontes de grind sem duplicar conquistas. Adotamos essa separação ou repetimos também o prêmio principal sempre que a atividade puder ser refeita?

**Opções:** “Prêmio principal único; recompensas repetíveis separadas (recomendado)” / “Repetir o prêmio principal a cada conclusão válida”. **Resposta em 23/10/2026:** “Prêmio principal único; recompensas repetíveis separadas (recomendado)”. Primeira opção aprovada, cobertura/limites em 8.25; configurações, resets de jornada e economia ainda exigem integração M04/M05/M11/M12/M15.

### 12.19. M09-10A — DECIDIDO — Uma devoração por presa elegível

**Pergunta enviada em 23/10/2026:** Quando vários Hollows participam de uma caçada, recomendo que cada presa elegível permita uma única devoração, por um deles. Os demais mantêm o reconhecimento da participação e as recompensas aplicáveis, mas o avanço por consumo exige revezar presas. A alternativa é dividir uma quantidade finita de alimento/progresso da presa, com cada participante concluindo sua interação. Qual modelo adotamos? A prioridade de quem pode devorar será definida depois.

**Opções:** “Uma devoração por presa, com revezamento (recomendado)” / “Dividir conteúdo finito entre participantes”. **Resposta em 23/10/2026:** “Uma devoração por presa, com revezamento (recomendado)”. Primeira opção aprovada, cobertura/limites em 8.28; distribuição posteriormente decidida em 10B/8.29 como primeira conclusão elegível, sem reserva ou revezamento automático. RAC-19 preservado conforme 8.27.

### 12.20. M09-10B — DECIDIDO — Primeiro elegível a concluir consome

**Pergunta enviada em 23/10/2026:** Para evitar uma corrida de cliques, recomendo dar prioridade temporária a um Hollow elegível, informando os envolvidos. A escolha considera participação e revezamento, com critérios definidos na ficha; só ele pode concluir a devoração durante sua vez. Se recusar, perder a elegibilidade ou deixar o prazo acabar, a oportunidade passa a outro elegível. Adotamos esse modelo ou qualquer participante elegível pode consumir, vencendo quem concluir primeiro?

**Opções:** “Prioridade temporária com passagem a outro elegível (recomendado)” / “Primeiro elegível a concluir consome”. **Resposta em 23/10/2026:** “Primeiro elegível a concluir consome”. Segunda opção escolhida, diferente da recomendação. Contrato em 8.29; prioridade temporária e rodízio não aprovados. Consumo único de 10A preservado; elegibilidade, duração, expiração, concorrência e feedback exigem ficha M09/M15/M16.

### 12.21. M09-11A — DECIDIDO — Amostra inicial das fichas

**Pergunta enviada em 23/10/2026:** Para as primeiras fichas de M09, recomendo detalhar os três perfis Hollow existentes — bruto terrestre, ágil e voador — e um encontro com o chefe atual. Vamos definir função, comportamento, alvos, ataques e requisitos, sem chamar o chefe de Gillian só pelo tamanho. Essa é a amostra inicial de documentação; catálogo completo, regiões e ordem de produção serão tratados nas etapas próprias. Adotamos esse conjunto ou começamos as fichas por novas criaturas canônicas?

**Opções:** “Detalhar bruto, ágil, voador e chefe existentes (recomendado)” / “Começar por novas criaturas canônicas”. **Resposta em 23/10/2026:** “Detalhar bruto, ágil, voador e chefe existentes (recomendado)”. Primeira opção aprovada; seleção na seção 10 e fichas no catálogo M09a. Não aprova funções/papel propostos em 11B/C ou parâmetros/produção/região.

### 12.22. M09-11B — DECIDIDO — Funções dos três Hollows comuns

**Pergunta enviada em 23/10/2026:** Para diferenciar os três Hollows comuns, recomendo: bruto com golpe lento e amplo, abrindo uma janela após errar; ágil com sequências curtas e reposicionamento, sem pressão ininterrupta; voador com aproximação aérea, ataque perto do chão e uma janela em que pode ser enfrentado com recursos básicos da etapa, antes de se afastar. Adotamos essas funções ou mantemos os três com o mesmo padrão de ataque, variando apenas corpo e cadência?

**Opções:** “Funções distintas para bruto, ágil e voador (recomendado)” / “Mesmo padrão básico, variando corpo e cadência”. **Resposta em 23/10/2026:** “Funções distintas para bruto, ágil e voador (recomendado)”. Primeira opção aprovada em M09a/9.1–9.3; funções distintas com resposta/recuperação pertinentes, sem números, sensores, lista de alvos ou geometrias exatas aprovados por associação.

### 12.23. M09-11C — DECIDIDO — Papel do chefe na amostra

**Pergunta enviada em 23/10/2026:** Para a primeira ficha do chefe Hollow, recomendo um confronto opcional preparado, com início confirmado, que reúna leitura de golpe próximo, investida e ataque de área. Ele não seria obrigação universal para concluir os prólogos ou adquirir as primeiras skills. A alternativa é detalhá-lo primeiro como ameaça espontânea do mundo aberto. Qual papel adotamos para essa amostra?

**Opções:** “Confronto opcional preparado (recomendado)” / “Ameaça espontânea do mundo aberto”. **Resposta em 23/10/2026:** “Confronto opcional preparado (recomendado)”. Primeira opção aprovada em M09a/9.4. A escolha do tipo de encontro não aprova uma quest, local, recompensa, instância ou fase nova.

### 12.24. M09-12A — DIRETRIZ DECIDIDA — Comportamento segundo a obra

**Pergunta original enviada em 23/10/2026:** Para os três Hollows comuns desta amostra, recomendo: bruto territorial, ágil agressivo e voador reativo, que só entra na luta quando provocado. Os três usam visão e sensibilidade espiritual natural limitada, sem Pesquisa nem saber posições ocultas. Isso cria ameaças e escolhas diferentes na exploração. Adotamos esses perfis ou fazemos os três agressivos, mantendo as diferenças de ataque já aprovadas?

**Resposta:** “Quero que siga como a obra”. Nenhuma alternativa original selecionada automaticamente. Direção canônica pesquisada e aplicada em M09a/4/9.5–9.6; rascunho anterior preservado no histórico. A representação de aldeões foi depois aprovada como adaptação em 12C.

### 12.25. M09-12B — DIRETRIZ DECIDIDA — Alvos segundo a obra

**Pergunta original enviada em 23/10/2026:** Para os alvos da amostra, recomendo que os comuns possam enfrentar jogadores de qualquer origem e Plus vulneráveis, quando seu perfil de hostilidade e a relação permitirem. Não caçam aliados reconhecidos, prestadores protegidos, aldeões, pets ou fauna vanilla por padrão. O chefe preparado escolhe alvos entre seus participantes válidos, sem atacar quem apenas passa perto. Adotamos esse recorte ou já ampliamos os comuns para atacar aldeões e outras criaturas vanilla elegíveis?

**Resposta:** “Quero que seja como na obra”. Nenhuma alternativa original selecionada automaticamente. Direção canônica pesquisada e aplicada em M09a/4/9.5–9.6; rascunho anterior preservado no histórico. A representação de aldeões foi depois aprovada como adaptação em 12C.

### 12.26. M09-12C — DECIDIDO — Aldeões como civis vivos

**Pergunta enviada em 23/10/2026:** As fontes descrevem Hollows caçando almas de vivos e mortos. Para traduzir isso ao Minecraft, recomendo tratar aldeões como civis vivos, permitindo que Hollows os cacem quando expostos. Prestadores essenciais continuam protegidos, como aprovado. Aldeões não existem em Bleach, então essa associação é uma adaptação do mod. Adotamos essa representação ou deixamos a caça a civis restrita aos NPCs humanos/Plus criados para Bleach?

**Opções:** “Aldeões como civis vivos caçáveis (recomendado)” / “Somente civis/Plus próprios de Bleach”. **Resposta em 23/10/2026:** “Aldeões como civis vivos caçáveis (recomendado)”. Primeira opção aprovada; alternativa restrita aos civis/Plus próprios de Bleach não adotada. Perfil M09a/9.7; questão de representação de Minecraft, sem atribuí-la ao cânone.

## 13. Critérios de aceitação

Critérios para futura especificação/validação, sem cenários executados nesta rodada:

1. A ficha separa natureza/estágio, perfil de luta e papel; tamanho/modelo sozinho não muda classe racial.
2. Um inimigo de mundo aberto do mesmo perfil/contexto não ganha força porque o jogador aumentou atributos ou trocou de forma; versões diferentes têm contexto identificável.
3. O jogador reconhece a diferença dos perfis pelas decisões de combate, com sinais coerentes com o acerto e meios de reação apropriados ao repertório exigido.
4. A ficha demonstra como o adversário lida com paredes, teto, água, desníveis e construções; não depende de teleporte/destruição não aprovados para funcionar.
5. Vitória, falha, saída e interrupções produzem um resultado definido e não duplicam criação, crédito ou recompensa; políticas ainda precisam ser fechadas antes dessa validação.
6. Um confronto canônico preserva STR-03; não usa o prestador protegido como boss ou aliado que decide a vitória.
7. Na caça Hollow, participação, derrota elegível e consumo são distinguíveis; um cenário cooperativo precisa resolver o destino da presa antes de alegar predação funcional.
8. A amostra futura distingue execução solo, servidor dedicado e observação por outros jogadores, sem tratar evidência histórica como teste novo.

## 14. Impacto técnico conhecido

Conferência local em **23/10/2026**, sem build/cliente/testes. Plataforma declarada: Java Edition **1.20.1**, Forge **47.4.10**, Java **17**. Fontes lidas: [HollowVariant](../../src/main/java/com/bleachmod/entity/HollowVariant.java), [HollowEntity](../../src/main/java/com/bleachmod/entity/HollowEntity.java), [HollowFlightCombatGoal](../../src/main/java/com/bleachmod/entity/HollowFlightCombatGoal.java), [HollowBossEntity](../../src/main/java/com/bleachmod/entity/HollowBossEntity.java), [registro de entidades](../../src/main/java/com/bleachmod/registry/ModEntities.java), [modificador de spawn](../../src/main/resources/data/bleachmod/forge/biome_modifier/hollow_spawn.json) e [loot do Hollow](../../src/main/resources/data/bleachmod/loot_tables/entities/hollow.json).

| Evidência no código | Consequência e limite |
|---|---|
| `brute`, `swift`, `flying` no mesmo tipo `bleachmod:hollow`, com dimensões/atributos próprios | Base existente para três perfis; não prova três espécies canônicas ou estágios |
| Vida máxima declarada: bruto 20 HP, ágil 6 HP, voador 14 HP; boss 100 HP | Valores de implementação atuais, **não balanceamento aprovado** para M09 ou prólogos |
| Antecipação/cadência dos ataques comuns e revalidação de alcance/visão no contato | Ponto de partida para legibilidade/contrajogo; qualidade em gameplay não reavaliada nesta rodada |
| Voador alterna circulação, aproximação, ataques e retirada, com navegação e verificações de espaço | Há comportamento aéreo real no código; terreno irregular, confinamento e experiência multirracial continuam a validar |
| Boss tem entrada, golpe próximo, investida e impacto; colisão/raycast em trechos de dano | Não apenas modelo estático. Ataques e estados não equivalem a um encontro completo ou a todas as políticas futuras |
| Atributos próprios nos perfis lidos | Não foi localizada escala por BP nesses arquivos; **não é auditoria de todo modificador externo** ou de toda configuração do servidor |
| Alvos incluem jogador, aldeão e golem; reação a dano também existe | Política atual a confrontar com o próximo bloco; não aprovar todos esses alvos por herança do código nem presumir detecção espiritual |
| Modificador adiciona Hollow a `#minecraft:is_overworld`, peso 80, grupo 2–6 | Configuração existente, não distribuição final aprovada para regiões/eras; registro não prova frequência observada em todos os tipos de mundo |
| Tabela de loot Hollow examinada tem `pools` vazio | Não comprova ausência de toda recompensa por eventos/quests, nem autoriza novo drop ou valor de SP |
| Variante e alguns cooldowns/entrada são salvos; ataque do boss é cancelado ao ler dados | Persistência parcial demonstrada no código, sem contrato universal de retomada. Não generalizar relógio desses mobs a partir do contrato do jogador |

**Confronto dirigido do bloco 02:** os seletores locais de Hollow/boss usam proximidade com verificação de visão e reação a dano; isso não demonstra oposição a Supressão ou busca por pista. O objetivo aéreo recebe a entidade-alvo e calcula trajetos com sua posição atual (`orbit`/`approach`). Antes de implementar 02A/B, será necessário separar informação percebida de posição real, inclusive navegação/orientação e retenção do alvo. Trata-se de uma lacuna frente ao contrato aprovado, não prova de erro nas regras antigas. Não houve execução de cenário ou inspeção integral das classes vanilla herdadas; alvo/retenção do motor exigem revisão técnica quando a implementação for autorizada. Não substituir a navegação existente nesta etapa documental.

**Conferência dirigida de terreno — 23/10/2026:** relidos os trechos de aproximação/circulação e segmento local do voador, correção curta terrestre e investida do boss. Há checagens de espaço, colisão e apoio nesses trechos; isso não prova um contrato completo de obstáculos/retorno ou configurações de destruição. Não foram executados cenários, auditadas todas as classes vanilla herdadas ou alterada a navegação. Implementar 04A/B aprovados exigirá conciliar seleção de destino, informação sensorial, progresso/temporizadores e regras de blocos na implementação futura.

**Conferência dirigida de compromisso — 23/10/2026:** em `HollowEntity`, o golpe comum guarda a entidade-alvo e revalida alcance/visão até o contato; esse trecho não demonstra um volume angular fixado correspondente à orientação. Em `HollowBossEntity.beginAttack`, a direção e o ponto de impacto são registrados no começo; `tickAttack` usa a direção registrada. São implementações diferentes, sem uma política única inferida. O contrato 05A aprovado não foi aplicado ao código; sua implementação precisará conciliar geometria, navegação, direção e sinais. Sem observação de gameplay ou nova auditoria integral.

Os relatórios históricos de QA preservam escopos e limites no [parecer de reconciliação](00-reconciliacao-implementacao-assets-2026-10-22.md#4-evidência-de-engenharia-recebida). Não foram reexecutados. Nenhuma migração de saves, biblioteca nova, modelo ou IA foi criada por este documento.

## 15. Próximos passos

**Continuidade atual:** M06-CS-01A/B/C/D consolidados conceitualmente; M09-12A/B corrigidos pela direção de seguir a obra, com fontes em M09a. M09-12C decidido e aplicado à amostra: aldeões como civis vivos caçáveis. Conferência de integração e do checkpoint no parecer de cobertura/M10b; os parâmetros, regiões, encontros e restante do catálogo completo continuam por entrega.

**Revisão em 23/10/2026:** [relatório de alcance e entregas](09b-revisao-m09-2026-10-23.md). M09-01–11 decididos, sem pergunta geral pendente. Regras gerais `ESPECIFICADO CONCEITUALMENTE`; [catálogo M09a](09a-catalogo-funcional-de-adversarios-e-encontros.md) permanece `EM ESPECIFICAÇÃO`. Funções da amostra não substituem sensores/alvos/valores, locais, distribuição, concorrência, migração ou validação. Condições de retorno e responsáveis no relatório/9/12.

**Encaminhamento anterior, preservado como histórico:** M10 — Dimensões, locais e estruturas, preservando as decisões de origem, mundo compartilhado e construções. Continuar as perguntas sem pedido adicional. Entregas M08/M09 continuam condições antes das funcionalidades correspondentes; não exigir todo o catálogo para planejar os locais. Nenhum runtime, build, teste, publicação ou produção foi autorizado por esse fechamento.
