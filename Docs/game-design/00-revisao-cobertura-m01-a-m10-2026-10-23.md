# Revisão de cobertura e profundidade — M01–M10 — 23/10/2026

## 1. Estado da revisão

| Campo | Resultado |
|---|---|
| Pedido | Revisar M10 e identificar, desde M01, generalização excessiva, especificação excessiva e decisões indevidamente adiadas |
| Base local | `main`, HEAD `3a37ca761a95faa75163c3335536bb9e0c5a80f3`, com documentação local anterior preservada |
| Resultado | Cobertura funcional incompleta, sobretudo nos catálogos; algumas pendências são escolhas de produto, não apenas números ou programação |
| Maturidade | M10 permanece `EM ESPECIFICAÇÃO`; fechamentos anteriores conservam seus recortes conceituais, sem certificação do conteúdo completo |
| Decisões novas de gameplay | Nenhuma. Recomendações e campos ainda abertos não se tornam aprovações |
| Alterações | Critério de revisão, rastreabilidade, cobertura de lugares e conciliação de duas sínteses com REV-SP-02 |

**Parecer:** a direção central continua aproveitável. Não é adequado dizer que falta apenas balancear ou programar. Também não é necessário refazer as aprovações nem completar toda quest antes de planejar o próximo sistema. É preciso distinguir o fechamento das regras gerais da cobertura funcional do conteúdo que o jogo pretende oferecer.

## 2. Objetivo

Evitar dois erros: encerrar um domínio com regras abstratas e poucos exemplos; ou gastar rodadas repetindo contratos e definindo detalhes de execução antes de explicar a atividade que o jogador realizará. O caso de M08 demonstrou que separar sistema e conteúdo não basta: o conteúdo precisa ter um responsável e uma entrega verificável.

## 3. Pergunta que cada ficha deve responder

**O que o jogador faz aqui, com quem ou com o quê, por qual motivo, em quais condições, e qual resultado permite continuar?**

Um nome, uma skin, uma classe Java ou a frase “será definido por ficha” não respondem isso. Uma ficha funcional pode responder sem coordenadas finais, falas completas, preços definitivos ou implementação.

## 4. Evidência e alcance da leitura

Referências metodológicas: skill local `bleach-game-design`, revisão de contratos e coerência narrativa. Consultados mapa, continuidade vigente e organização/histórico da base datada; aproveitadas as revisões anteriores, identificadas abaixo. Busca foi seguida de leitura dos blocos pertinentes; não foi feita nova leitura integral das milhares de linhas de histórico de todos os módulos.

| Documentos | Recorte examinado nesta revisão |
|---|---|
| [M01](01-visao-e-pilares.md) | Visão, pilares, escopo, decisões, amostra e encaminhamento; sínteses sobre pontos |
| [M02](02-trilhas-de-progressao.md) | Estado, sete trilhas, aquisição, fontes, ritmo, integrações; leitura com precedência de M05/M06 |
| [M03](03-storyline-macro.md) | Escopo, cronologia, perspectivas raciais, participação, mundo aberto, estados, STR-06/07, pendências e critérios |
| [M04](04-racas-e-origens.md) | Síntese vigente, criação/reset e fronteiras; escopo/modelo corporal, RAC-04–06/11–20/22 e adiamentos |
| [M05](05-atributos-e-recursos.md) | REV-SP-02; Vida/Stamina/Energia/Combat State; responsabilidades, calibração, conclusão e contrato final |
| [M06](06-habilidades-e-combate.md) | Estado, filosofia, Combat State/Mode/Hotbar, consolidação de interrupções e fechamento/entregas; buscas dirigidas de interação corporal e espiritual |
| [M07](07-transformacoes-e-dominio.md) | Entregas vigentes de 12.2 e contratos de corpo/retorno referidos por M10; apoio da revisão anterior |
| [M08](08-npcs-mentores-e-faccoes.md), [M08a](08a-catalogo-funcional-de-npcs-e-faccoes.md), [M08b](08b-revisao-do-catalogo-2026-10-22.md) | Regras de função/ensino/continuidade; inventário e destino funcional do elenco; primeiras fichas, organizações e oito lacunas da revisão anterior |
| [M09](09-mobs-bosses-e-encontros.md), [M09a](09a-catalogo-funcional-de-adversarios-e-encontros.md), [M09b](09b-revisao-m09-2026-10-23.md) | Escopo/modelo e contratos sensoriais; leitura das quatro fichas e revisão/entregas |
| [M10](10-dimensoes-locais-e-estruturas.md), [M10a](10a-revisao-m10-2026-10-23.md), [M10b](10b-catalogo-funcional-de-lugares-e-rotas.md) | Regras funcionais de mundo/viagem/renascimento, decisões e limites, seis fichas iniciais, percursos, dependências e critérios |
| [Revisão M01–M07 de 22/10](00-revisao-modulos-01-a-07-2026-10-22.md), [reconciliação de assets](00-reconciliacao-implementacao-assets-2026-10-22.md) | Leitura dos pareceres e limites; preservar o que já foi corrigido e não promover a lista de assets a catálogo aprovado |

Esta é uma revisão de cobertura documental. Não revalida cânone, APIs, código, mapas, conteúdo remoto ou implantação. Fontes externas já citadas mantêm suas datas e limites; nenhuma nova pesquisa externa foi necessária para identificar estes problemas de documentação.

## 5. Critério de profundidade aplicado

| Camada | O que precisa explicar | O que pode esperar |
|---|---|---|
| Direção | Fantasia, princípios e limites | Catálogo e fórmulas; caso de M01 |
| Regras comuns | Estados, compatibilidades e consequências compartilhadas | Exceção de cada poder e parâmetros |
| Cobertura funcional | Elementos importantes conhecidos, função, relação com a jornada, estado e responsável | Modelagem, coordenadas e diálogos completos |
| Ficha de conteúdo | Interação, requisitos, resultado, falha/continuidade e diferenças próprias | Valores finais dependentes de calibração |
| Execução/entrega | Dados, parâmetros, integrações e evidência do recorte entregue | Somente pendências explicitamente não impeditivas |

**Uma amostra serve para validar o sistema; não prova a cobertura do jogo completo.** O inventário de cobertura também não aprova todo elemento citado. Candidato, decidido, adiado por escolha explícita e ainda sem definição precisam continuar distintos.

Delegar é adequado quando há **responsável principal, entrega concreta e momento de retorno**. Uma lista de muitos módulos, todos igualmente responsáveis, não substitui essa definição. O responsável principal coordena a resposta; os demais fornecem seus contratos.

## 6. Parecer desde o primeiro módulo

| Módulo | Avaliação | Complemento necessário, sem reabrir decisões já tomadas |
|---|---|---|
| M01 — Visão | Generalidade apropriada ao seu papel | Preservar D01–D06; não exigir NPCs ou mapas aqui. Corrigida síntese de pontos que ainda destacava atributos como destino principal, apesar de REV-SP-02 |
| M02 — Trilhas | Separação adequada; texto original preserva modelos superados | Demonstrar as sete trilhas em percursos reais, especialmente vínculo, relações e conhecimento. Não criar sete barras nem restaurar ranks comprados de disciplina. Conciliação executável de compra ainda é trabalho real |
| M03 — Storyline | Ordem dos arcos e autoria jogável definidas; perspectivas raciais ainda amplas | Antes das campanhas, matriz era × origem: motivo, participação e consequência; acontecimentos protegidos por arco. Quests completas continuam em M13; catch-up tem dono M15 |
| M04 — Raças | Identidade, evolução, convivência e política de personagem têm decisões | Explicitar presença corporal/espiritual e interação do recorte atual. Consciência, corpo, visibilidade e alvo não podem ser decididos apenas pelo renderer. Gigai/Substituto/híbridos mantêm seus adiamentos |
| M05 — Recursos | Arquitetura suficiente para orientar sistemas; fechamento não é calibração | Separar escolhas funcionais de números: eventos de combate, relação com sobrevivência vanilla e condições após morte não são apenas curvas. Corrigida síntese final de aquisição para incluir SP |
| M06 — Combate | Contratos comuns e diferenças raciais bem desenvolvidos | Fichas de fundamentos e currículo transferível precisam virar seleções concretas. Combat State e interação com proteções/itens ainda requerem resolução funcional; não delegar toda escolha a M15/M16 |
| M07 — Formas | Perfis distintos e contratos comuns preservam qualidade | Provas, benefícios e recuperações por poder continuam incompletos; são conteúdos, não só ajustes de duração. Grimmjow/artes e exceções futuras permanecem deliberadamente adiados |
| M08 — Habitantes | Ampliação corrigiu a ausência de papéis; cobertura ainda parcial | Os 36 personagens têm destino funcional de trabalho, mas muitos são candidatos. Currículos, compromissos institucionais, recusa e substituição por era ainda precisam de ficha. Papel aprovado não equivale a serviço detalhado |
| M09 — Ameaças | Regras e quatro amostras têm função distinta | Completar escolhas de sentidos/hostilidade/alvos da amostra e mapa de famílias por região/era. Não reduzir catálogo completo a três Hollows e chefe; não promover lista de assets a fauna aprovada |
| M10 — Mundo | Transporte/renascimento aprofundados; conteúdo espacial menos desenvolvido | Completar cobertura dos lugares exigidos por M03/M08, perfis ambientais, circulação/retorno, recursos e modos de mundo. Seis fichas de necessidade não fecham as três dimensões |

O parecer identifica o nível de detalhe ainda necessário; não revoga o estado conceitual de cada recorte nem transforma os módulos M11–M16 em documentos já iniciados.

## 7. Achados transversais

### COV-01 — O método favorecia encerrar cedo demais o conteúdo

**Confirmado e corrigido na orientação do mapa.** O exemplo da regra central impedia M08 de definir habilidades ensinadas; o template pedia somente a menor amostra. M08, porém, é responsável pelo repertório oferecido pelo mentor em integração com M06. A fronteira correta separa **quem oferece qual conteúdo** de **como cada técnica funciona** e de **qual quest encena o ensino**. Separar domínios não deve retirar a função de nenhum deles.

### COV-02 — Há duas sínteses de compra ainda ambíguas

**Correção documental aplicada, sem mudança econômica.** A nota de revisão no início de M01 ainda destacava atributos como destino direto principal. O contrato final M05/60 resumia técnicas como “aprendizado + Mastery”, omitindo a compra. REV-SP-02 já decide skills/formas por SP e Mastery por prática. As sínteses foram alinhadas; preços, exceções e fichas continuam pendentes. Não se alteraram respostas históricas.

### COV-03 — Trilha declarada não equivale a percurso demonstrado

M02 define sete trilhas e natureza dos registros, mas os catálogos iniciais ainda não demonstram a composição completa em uma jornada. M02 deve coordenar a correspondência entre trilhas e exemplos de M04–M13, aproveitando decisões posteriores. Não é necessário criar medidores, menus ou requisitos novos para preencher a tabela.

### COV-04 — Campanhas raciais precisam de cobertura em cada era

M03 promete quatro jornadas completas e oferece perspectivas gerais, mas ainda depende de uma distribuição concreta de participação e consequência. **M03 coordena a matriz macro; M13 escreve as missões.** Repetir “cada raça terá seu motivo” não demonstra que isso acontecerá. O pós-guerra detalhado e ramificações complexas permanecem adiados por STR-05/07.

### COV-05 — Presença espiritual ficou numa fronteira ampla demais

M04/modelo, RAC-04/06 distinguem corpo humano, alma separada, gigai e corpo Hollow. A representação futura é delegada genericamente; nos blocos e buscas desta revisão não foi localizada uma matriz funcional suficiente de **quem vê/interage/atinge quem e em quais estados do recorte atual**. M06 estabelece que Supressão não torna invisível, mas isso não resolve sozinho a regra de presença espiritual.

**M04 deve coordenar essa definição**, com M06 para alvos/interação, M08/M09 para sentidos e M10 para circulação. Primeiro delimitar o que já é necessário às origens atuais; o detalhamento de Gigai/Substituto/híbridos não deve ser antecipado por associação. Pesquisa e novas escolhas pertinentes serão necessárias antes de aprovar o comportamento. Não foi comprovado um bug de implementação por este achado.

### COV-06 — Combat State não é apenas um temporizador a calibrar

M05/24 e M06/5 usam exemplos de causar/receber dano e confronto relevante. M10/8.4 depende desse estado para permitir viagem; reset e evolução também usam restrições. Ainda é necessário fechar quais eventos **entram, renovam e encerram** o estado, como tratar dano ambiental, agressor sem solução de percurso e confronto encerrado. **M06 coordena a regra**, M05 a regeneração, M09 o encontro e M15 abuso/PvP. Duração final pode esperar calibração; significado do estado não pode ficar para programação por omissão.

### COV-07 — NPCs e carreiras ainda não estão detalhados integralmente

M08a/7.2 já cobre o elenco como aprovado/candidato/reservado; isso foi um avanço real. As lacunas REV-M08-02–08 da revisão anterior continuam pertinentes no escopo não resolvido. Precisam existir currículos selecionados, interlocutor/atividade de cada carreira oferecida, motivo de cooperação/recusa e substituições concretas. **M08 coordena esses conteúdos**, sem exigir que M13 escolha quem sabe ensinar cada técnica ou tornar todo NPC professor.

### COV-08 — Ameaças ainda têm campos funcionais sem escolha

M09a/8–9 registra sentidos, hostilidade e alvos como pendentes das quatro fichas, além de parâmetros. A seleção da amostra não os resolve. **M09 escolhe o perfil funcional**, M10 distribui por região, M11/M15 integram recompensas. Famílias Menos, indivíduos de campanha, fauna, civis e invocações não são todos o mesmo catálogo de inimigos; sua presença no horizonte precisa ser rastreada antes dos arcos que os utilizam.

## 8. Revisão específica de M10

| ID | Achado e efeito para o jogador | Tratamento |
|---|---|---|
| M10-COV-01 | M10b/7 contém seis necessidades iniciais, enquanto M03/M08 já exigem outros lugares | Acrescentada matriz de cobertura funcional em M10b/7.1; manter fichas e novas escolhas abertas |
| M10-COV-02 | Ambiente de Hueco Mundo tem decisões, mas perfil de Soul Society ainda não fecha céu/sono/clima; produção local continua abstrata | Próximo bloco de perfis ambientais e sobrevivência; separar regra ambiental M10 de materiais/receitas M11 |
| M10-COV-03 | “Acesso contextual” preserva liberdade, mas não diz como uma recusa, barreira ou serviço se aplica em cada lugar | M10 identifica o limite físico/rota; M08 determina autorização institucional e M13 evento/era. Não inventar uma credencial universal |
| M10-COV-04 | Gillian tem garantia geral de espaço, porém abrigo de spawn e áreas de recuperação ainda não têm interação/local funcional | Definir um perfil utilizável para corpo grande e cobertura nas três dimensões; coordenadas/modelos depois. Não encerrar isso como mera calibração |
| M10-COV-05 | Rota dos operadores prevê volta local, mas não resolve o retorno de toda passagem própria para uma base distante, convidado sem abertura ou fonte perdida | Ficha de cada via deve demonstrar retorno após encerramento. Sem aprovar NPC de resgate ou retorno gratuito por inferência |
| M10-COV-06 | Coexistem mapas de História e promessa de mundo aberto; experiência em save comum/existente não foi escolhida por esta revisão | M10 coordena o contrato de produto por modo; M16 coordena implantação. “Não sobrescrever construções” já é limite, não solução pronta |
| M10-COV-07 | Portal pago depende de ambas as regiões simuladas; viajar pode remover o último jogador de uma ponta | Verificar viabilidade do perfil cooperativo e carregamento em M16. Não presumir reserva de chunks, mudar a janela ou revogar 07E sem necessidade/decisão demonstradas |
| M10-COV-08 | M10/8.36 delega corretamente a missão exata, mas essa delegação não define geografia/serviços de todo Hueco Mundo | Preservar 09C. M13 escolhe missão/motivo; M10 continua responsável pelo lugar e percurso que sustentam essa missão |

**O que está adequado:** contratos comuns de passagem, destino conhecido/registro manual, abertura separada de travessia, fora de combate, validação por corpo, cama como spawn e limpeza de formas. Não precisam de nova votação por raça.

**Onde houve excesso de detalhe relativo:** várias rodadas sobre janela, encerramento, sessão e reações de passagem enquanto a cobertura dos lugares ainda era pequena. Essas decisões continuam úteis/aprovadas; a correção é mudar o foco da próxima rodada, não apagá-las ou fundir exceções de todos os serviços.

## 9. Percursos que ainda precisam ser demonstrados

| Caminho | Base existente | Elo ainda a concretizar |
|---|---|---|
| Shinigami | Encaminhamento à Academia, instrutor original, fundamentos/Shunpo | Fonte/passagem e recepção por contexto; equipamento/SP; atuação, retorno e continuidade institucional |
| Hollow/Arrancar | Veterano, caça, evoluções, operadores e missão de primeiro acesso | Primeira atividade/presa elegível, fontes especializadas, espaço dos estágios e volta após evolução |
| Quincy | Mentor independente, arco/Hirenkyaku, fontes avançadas como necessidade | Foco/SP iniciais, currículo avançado autorizado, acesso a instrumentos e recuperação antes de Letzt Stil |
| Fullbringer | Orientador, Bringer Light/salto, contatos e poder posterior | Práticas concretas da matéria, aquisição contextual, vetor quando pertinente e processo de reacesso antes de oferecer troca |

Esses são diagnósticos, não sequências de quests aprovadas. Não exigem comprar todas as técnicas para sair do prólogo nem concedem skill ou item provisórios para resolver um requisito circular.

## 10. Cobertura de conteúdo e entregas graduais

A [matriz M10b/7.1](10b-catalogo-funcional-de-lugares-e-rotas.md#71-cobertura-do-horizonte-conhecido--revisão-de-23102026) distingue as seis fichas da amostra dos demais lugares já requeridos pelos contratos. Abrir uma linha de necessidade não escolhe prédio, mapa, ponto de spawn, operador, presença em toda era ou nova dimensão.

Aplicar o mesmo princípio aos demais domínios: elenco e organizações em M08, famílias/ameaças em M09 e capacidades em M06/M07. **Completar o horizonte significa dar destino a seus elementos importantes, não escrever todos com a mesma profundidade de uma entrega inicial.**

## 11. Adiamentos apropriados

| Assunto | Por que pode esperar |
|---|---|
| Coordenadas, medidas finais, blocos e arquitetura de cada prédio | Dependem da composição espacial e dos mapas; função/acesso precisam vir primeiro |
| Falas, cenas, sequência e nome exatos da primeira missão de Hueco Mundo | M12/M13; 09C já resolveu o critério narrativo de acesso |
| Preços, curvas, taxas e números de dano/alcance | Dependem da ficha e de calibração M05/M11/M15; distinguir do funcionamento qualitativo |
| Catálogo integral de poderes, Grimmjow/artes, híbridos, múltiplos personagens e cargos únicos | Existem adiamentos explícitos; não reabertos por uma revisão de cobertura |
| Procedimento de recuperação de um poder ainda não oferecido | Pode acompanhar sua ficha, mas deve estar concluído antes de disponibilizar o risco correspondente |
| Engenharia, desempenho, migração e evidência executável | M16 integra contratos definidos; não deve inventar decisões de produto para preencher lacunas |

## 12. Registro de ações e momento de retorno

Prioridade abaixo organiza a revisão documental; não é ordem de implementação, lote de assets ou aprovação de conteúdo.

| Ação | Responsável principal | Momento de conclusão / retorno |
|---|---|---|
| COV-01/02 — método e sínteses | Mapa / M01 / M05 | Corrigidos nesta rodada; história e decisões preservadas |
| M10-COV-01 — cobertura dos lugares | M10 | Inventário ampliado nesta rodada; fichas funcionais ainda antes de fechar o conteúdo de M10 |
| M10-COV-02/03/04/05 — ambiente, função/acesso, corpos e retorno | M10 | Próxima revisão de conteúdo de M10, antes de apresentar o domínio como completo |
| M10-COV-06 — modo de mundo | M10 | Contrato de experiência antes do fechamento de M10; realização/migração M16 |
| COV-05 — presença espiritual | M04 | Retorno dirigido prioritário após organizar M10, antes de fechar alvos, populações e prólogos |
| COV-06 — Combat State | M06 | Retorno dirigido antes de especificar execução de viagens/reset/evolução; números depois |
| COV-07 — currículos e carreiras | M08 | Antes de fechar o conteúdo de cada serviço/carreira; recuperações antes do risco, com M07 |
| COV-08 — perfis e famílias | M09 | Antes de fechar população regional e encontros das campanhas; parâmetros depois |
| COV-03 — trilhas em percursos reais | M02 | Revisão de integração antes de fechar as primeiras aquisições/recompensas em M12/M13 |
| COV-04 — raça × era e marcos protegidos | M03 | Antes de detalhar cada campanha de M13; política compartilhada com M15 |
| Sobrevivência/economia inicial/itens e morte | M05 / M11, cada qual em seu domínio | Fazer retorno a M05 com M11: comida/fome/regeneração, recursos ao renascer e reposição de equipamentos não são decididos pelo spawn de M10 |
| M10-COV-07 e transações/implantação | M16 | Antes de implementar/entregar a respectiva rota; incompatibilidade real retorna a M10 |

Não se propõe resolver estes assuntos todos numa única rodada de perguntas. Cada bloco apresenta análise, herança, diferenças e decisões novas agrupadas. Resolver uma dependência não obriga reaprovar todo o módulo que a utiliza.

**Acompanhamento posterior à revisão inicial — 23/10/2026:** respostas do usuário decidiram frentes de atividade de Rukongai/refúgios (M10-10A/B), ambiente de Soul Society após pesquisa da condição e marco de abrigo (11A/B), abrangência em mundos novos/existentes compatíveis (12A), operadores institucionais e lugares abertos de recuperação/reset (12B/C). Perfis em M10/M10b e funções em M08a; conferência dos elos em M10/8.43. Isso resolve essas escolhas do complemento, sem executar mapas, finalizar os conteúdos individuais ou resolver toda contingência de retorno. COV-05 avançou em [M04a](04a-presenca-espiritual-e-interacao.md): RAC-30A/B/C/D decididos, com requisito espiritual de dano também contra o jogador e interação vanilla preservada. Corpo/local após reset decidido em M04/RAC-31A: recomeço no atendimento do NPC, com estado inicial próprio e validação antes de concluir. COV-06 consolidado conceitualmente em M06a/8.6 após CS-01A/B/C/D aprovados, sem fixar tempos ou certificar runtime. COV-08 segue em M09a/9.5–9.7: direção explícita de fidelidade à obra em 12A/B aplicada após pesquisa; 12C decidido sobre aldeões como civis vivos caçáveis, adaptação explícita. M08a/9.40 integra as fontes/serviços já escolhidos à presença/combate, mantendo as entregas de currículo/era e recursos antes de cada conteúdo. O diagnóstico inicial permanece como registro do que motivou o trabalho.

### 12.1. Conclusão do checkpoint autorizado — 23/10/2026

**Concluída a rodada de revisão e complementos funcionais combinada para publicação.** Isso não fecha o conteúdo completo de M08/M09/M10, nem o GDD inteiro. Os achados e diagnósticos iniciais acima ficam preservados como ponto de partida; o resultado atual é o da tabela abaixo.

| Parte do checkpoint | Resultado documental atual | Trabalho posterior com retorno definido |
|---|---|---|
| M10 — função dos lugares | Cobertura ampliada M10b/7.1; Rukongai com abastecimento/proteção/investigação local; refúgios com sobrevivência/trocas/acordos; instituições e rede de contatos ligadas às atividades aprovadas | M10 escolhe setores/lugares concretos e M08 completa prestadores/atividades antes da oferta correspondente; M13 encena a missão |
| M10 — ambiente e residência | Soul Society com dia/noite/clima regional, Hueco Mundo noturno com água transportada permitida; abastecimento local, camas e marco de abrigo, recuperação por corpo/lugar definidos | M11 materiais/obtenção/reposição; M10/M16 geografia e espaços implantados antes de entregar residência/renascimento |
| M10 — circulação/retorno | Operadores comuns nas duas pontas das duas rotas; áreas abertas de recuperação/reset; M10b/9.4.1 liga estadia → atendimento → mundo de volta, incluindo convidado sem skill e corpo evoluído | Ficha de cada percurso fecha caminho, acesso e contrapartida por passageiro/corpo/era. Não se declarou toda base arbitrária coberta ou criou resgate |
| M10 — modos de mundo | Campanha em mundos novos e existentes compatíveis; integração preserva regiões usadas, com posições reais por save | M16 realiza o procedimento de integração e verifica simulação das duas pontas; impedimento técnico real retorna a M10 antes da rota |
| M04 — presença e reset | RAC-30A/B/C/D e RAC-31A decididos: visão, interação, dano espiritualmente compatível e recomeço no atendimento com corpo inicial próprio | Itens/componentes concretos em M11/M06; Gigai/Substituto/híbridos mantêm adiamentos. Reset íntegro antes de implementar |
| M06 — Combat State | CS-01A/B/C/D aprovados e consolidados em M06a; eventos, perigo separado, saída e continuidade definidos | Tempos/classificações e runtime antes da entrega; não é teste ou correção executada |
| M08 — fontes dos percursos | Fontes/repertórios/operadores aprovados integrados a presença/competência/combate em M08a/9.40; atendimento essencial não vira presa | Currículo avançado, indivíduos/substitutos por era e compromissos específicos antes de oferecer cada serviço/carreira; não exigir todos para publicar a revisão |
| M09 — amostra e fidelidade | Três ritmos distintos e chefe opcional preparado preservados; sentidos/predação sem personalidade pela anatomia; M09-12C aprovado e aplicado: aldeões como civis vivos caçáveis | Regiões, números, componentes de ataque/recompensas e participação concreta do chefe antes da respectiva entrega. Fauna/golems não viram presas por associação |

**COV-03/04 continuam abertos:** M02 demonstra as trilhas em aquisições/recompensas antes de concluir os primeiros percursos M12/M13; M03 coordena a matriz origem × era antes de detalhar as campanhas. São trabalhos reais, sem novas barras ou roteiro inventado nesta revisão.

**Próximo bloco principal: M11 — itens, equipamentos e economia.** Retorno dirigido a M05 para sobrevivência e recursos ao renascer; depois focos, armamento de treino espiritualmente compatível, obtenção/reposição e compras com SP. Começar pelas funções e categorias que sustentam os percursos aprovados, sem fixar todos os preços nem transformar SP em moeda de qualquer comércio. M12–M16 recebem as entregas identificadas; nenhuma depende de autorização nova para registrar respostas já dadas.

## 13. Conferência documental

Conferência de 23/10/2026 nos nove documentos editados nesta rodada: 312 referências locais a arquivos com destino existente e cercas Markdown pareadas. Relatório e M10/M10a/M10b mantêm quinze seções numeradas. Comparação com cópias anteriores confirma a preservação integral dos blocos de perguntas/respostas de M10/12 e M10b/12; alterações de síntese/método foram examinadas separadamente. A seção de cobertura M10b/7.1 existe.

`git diff --check` não indicou problemas nas diferenças rastreadas. A conferência adicional encontrou seis linhas com espaços finais já existentes em M05; foram preservadas, sem novos espaços finais introduzidos pela rodada. Essas verificações não certificam todas as âncoras, toda a documentação histórica ou comportamento de jogo. Nenhum cenário de gameplay foi executado.

### 13.1. Conferência do lote acumulado para publicação — 23/10/2026

Lote explícito de 76 arquivos Markdown em `Docs/game-design/`, incluindo históricos: 548 referências locais a arquivos com destino existente, cercas Markdown pareadas e onze documentos de complemento/catálogo/revisão com as quinze seções esperadas. Conferidas também 75 referências com âncoras de títulos; corrigidos sete links de REV-SP-02 que omitiam o acento do título e um link histórico de M09-11B ainda apontando ao cabeçalho “PROPOSTO”. Links corrigidos não alteram as respostas.

O rascunho anterior não aprovado de M09-12A/B foi comparado ao backup desta rodada e permanece integral. M09-12C consta com a resposta literal em módulo, catálogo e histórico; os registros antigos da base datada conservam seu estado na época, com continuidade atual em 79. Conferência documental não substitui validação dos mapas/rotas, comportamento do mod ou leitura integral de toda fonte externa.

## 14. Limites e impacto

O lote autoral desta revisão contém somente documentação. Antes de publicar, o checkout recebeu 25 commits upstream por fast-forward até `18786a6`, sem diferenças upstream em `Docs/game-design/`; runtime/assets recebidos não foram reauditados ou testados. Na revisão inicial houve somente documentação: sem atualização remota, código, assets, dependências, mapas, instalação/migração de saves, build, teste de Minecraft, commit ou push. A publicação posterior autorizada do checkpoint não implementa essas funcionalidades; resultado Git registrado na entrega. Inventários técnicos anteriores continuam evidências datadas, não reauditoria atual. Sem declaração de ausência absoluta de outras lacunas.

## 15. Continuidade recomendada

**Atualização após os complementos:** checkpoint de revisão concluído no alcance de 12.1; preparar commit/push autorizados e seguir a M11 com retorno M05. M08/M09/M10 continuam em especificação de conteúdo. A sequência abaixo registra a recomendação inicial que originou esta rodada, não uma ordem para refazer os retornos concluídos.


1. **M10 agora:** usar a cobertura ampliada para especificar função/atividades/acesso dos lugares, perfis ambientais e circulação/retorno. Em seguida fechar a experiência por modo de mundo. Novas escolhas continuam propostas até resposta explícita.
2. **Retornos dirigidos:** M04/presença espiritual e M06/Combat State; depois conteúdo prioritário de M08/M09 que a cobertura regional exigir. Não recomeçar os módulos do zero nem adiantar conteúdos explicitamente adiados.
3. **Sequência modular:** prosseguir a M11 com essas entregas rastreadas; M12/M13 completam missões/campanhas. Antes de fechar cada arco, conferir M03 e as fontes/lugares/adversários necessários. M14–M16 não absorvem silenciosamente decisões de produto que pertencem aos domínios anteriores.

**A revisão não fecha M10.** O próximo trabalho deve explicar mais do jogo que acontece nos lugares; não repetir as regras já aprovadas de portais e camas.
