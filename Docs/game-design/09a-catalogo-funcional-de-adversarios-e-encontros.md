# M09a — Catálogo funcional de adversários e encontros

## 1. Estado do catálogo

| Campo | Estado em 23/10/2026 |
|---|---|
| Estado documental | `EM ESPECIFICAÇÃO` |
| Seleção da amostra | M09-11A `DECIDIDO`: bruto, ágil, voador e chefe já existentes |
| Funções dos comuns | M09-11B `DECIDIDO`, funções distintas aprovadas |
| Papel do chefe na amostra | M09-11C `DECIDIDO`, confronto opcional preparado |
| Complemento funcional atual | M09-12A/B: diretriz de fidelidade à obra registrada; M09-12C decidido: aldeões como civis vivos caçáveis, adaptação explícita |
| Implementação/teste/aceite | Há código/modelos anteriores; nenhuma implementação, teste ou aceite dos contratos desta ficha |

[M09](09-mobs-bosses-e-encontros.md) mantém os contratos e decisões. O catálogo detalha sua amostra sem substituir a fonte das aprovações. As quatro fichas estão em elaboração; selecionar a amostra não aprova todos os campos.

## 2. Objetivo

Transformar os contratos gerais em adversários com função compreensível e pendências rastreáveis. Começar pelo conteúdo existente, mantendo espaço para o catálogo canônico completo.

## 3. Fantasia do jogador

Reconhecer ritmos e trajetórias, escolher quando defender/recuar/atacar e perceber crescimento ao reencontrar o mesmo perfil. Preparar terreno permanece útil pelos limites de M09-04A/B.

## 4. Base canônica

Aplicar M09/4 e ENC-S01–05. Hollows têm variedade de corpo/capacidades; nenhuma fonte atribui os nomes bruto/ágil/voador a espécies canônicas obrigatórias. Esses nomes são perfis de gameplay. O chefe atual não é Gillian por tamanho. Uma identidade canônica posterior exige fonte/ficha próprias; nenhuma foi atribuída aqui.

### Pesquisa dirigida ENC-S06–09 — 23/10/2026

| Fonte oficial | Evidência usada | Limite |
|---|---|---|
| ENC-S06 — [Shueisha/Shonen Jump, arco inicial](https://www.shonenjump.com/xmedia/bleach/story/), verbete 虚 | Hollows atacam vivos e mortos e se alimentam de almas | Resumo editorial do mangá, não leitura integral de capítulo |
| ENC-S07 — [Pierrot, verbete Hollow](https://pierrot.jp/title/bleach/keyword_ha.html) | Descreve formas/capacidades variadas e natureza perigosa | Anime complementar histórico; não atribui temperamento pelo formato corporal |
| ENC-S08 — [TV Tokyo, episódio 12](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes01/index.html) | Registra atração/prioridade por humanos de maior poder espiritual | Sinopse; não determina precisão, alcance, troca automática de alvo ou hostilidade universal |
| ENC-S09 — [Pierrot, verbete Menos Grande](https://pierrot.jp/title/bleach/keyword_ma.html) | Alguns Hollows passam a buscar almas de outros Hollows | Não estabelece canibalismo obrigatório para todo indivíduo |

Nenhum capítulo/episódio integral conferido nesta revisão. Não importar outras regras desses verbetes históricos como revisões de M07: ausência de regressão passiva e conquistas já aprovadas permanecem. Vilas Minecraft, entidades artificiais, proteções de serviço e confirmação de encontro exigem tradução identificada, não uma citação como se existissem literalmente na obra.

## 5. Adaptação para Minecraft

A amostra usa corpos e mecanismos já encontrados no código, com funções/papel aprovados em 11B/C e parâmetros ainda pendentes. Não copiar números atuais como balanceamento. Servidor valida informação percebida, caminho, acerto, resultado e consumo; um modelo/animação existente não comprova a integração.

## 6. Escopo

Quatro fichas funcionais, seus contratos herdados e entregas para conteúdo/validação. Regiões/spawn pertencem a M10, recursos/economia a M05/M11, objetivos específicos a M12/M13 e participação/escala a M15. Linhagens/artes continuam posteriores, Grimmjow primeiro caso quando essa etapa chegar.

## 7. Identificadores e cobertura

| ID documental | Código de referência | Natureza/perfil/papel |
|---|---|---|
| ENC-H01 | `HollowEntity` / `brute` | Hollow; terrestre pesado; ficha de adversário comum em elaboração |
| ENC-H02 | `HollowEntity` / `swift` | Hollow; terrestre ágil; ficha de adversário comum em elaboração |
| ENC-H03 | `HollowEntity` / `flying` | Hollow; aéreo; ficha de adversário comum em elaboração |
| ENC-H04 | `HollowBossEntity` | Hollow de identidade/estágio específico ainda não atribuídos; chefe de encontro |

IDs documentais não criam registro de entidade, espécie, classe racial ou item. Comum não é dificuldade numérica e chefe não é estágio Menos.

## 8. Contrato comum das fichas

Aplicar M09-01–10: força por perfil/contexto; sentidos declarados sem informação oculta; hostilidade e alvo separados; busca/retorno limitado; sem alteração de blocos por padrão; compromisso direcional; execução/controle de M06; recuperação, novas tentativas e lifecycle conforme o tipo de encontro; contribuição, prêmios e consumo separados.

**Complemento M09-12A/B, 23/10/2026:** usuário exigiu seguir a obra; perfis corrigidos em 9.5–9.6, com pesquisa e sem adotar as alternativas anteriores. M09-12C em 9.7 aprovado sobre a representação dos aldeões. As funções de ataque de 11B/C e a seleção de 11A não estão em votação novamente. Aplicar [M04a/RAC-30](04a-presenca-espiritual-e-interacao.md) para visão/interação/dano e [M06a/CS-01A/B/C/D](06a-estado-de-combate.md) para envolvimento/ameaça/continuidade; natureza espiritual, percepção e meios de ataque continuam distintos.

**Pendências compartilhadas:** calibrar parâmetros dos perfis corrigidos; geometria/controles/tempos de cada ataque; condições em água/desnível/teto/retorno bloqueado; região/era/criação; recursos/Vida/SP/loot/predação e presença dos participantes. Os seletores atuais não aprovam categorias por si só; aldeões foram selecionados explicitamente em 12C, enquanto golems não receberam essa classificação. Não conceder Cero, Pesquisa, regeneração rápida especial ou Super Armor apenas pelo perfil.

## 9. Fichas funcionais em elaboração

### 9.1. ENC-H01 — Bruto terrestre

**Função aprovada em 11B:** ameaça de curta distância com preparo reconhecível e golpe amplo/lento. O jogador observa o compromisso, evita a área ou usa defesa válida e pode responder na recuperação, inclusive após erro. A diferença pretendida está na área/ritmo, além de corpo ou dano.

**Sequência funcional candidata:** aproximação por caminho válido → preparação → direção comprometida por 05A → golpe com volume declarado → recuperação. Não transformar um giro de animação em acerto sobre qualquer pessoa no raio. Movimento e largura do golpe ainda exigem ficha.

**Código existente:** variante própria, corpo maior, atributos/cadência e antecipação; o ataque comum revalida alvo/alcance/visão. Isso não demonstra a varredura proposta ou uma janela calibrada de resposta.

**Enfrentamento pretendido:** repertório básico pertinente à etapa, sem exigir mobilidade racial aprendida. Passagem estreita pode impedir aproximação; aplicar 04B sem encolher ou quebrar o abrigo. Base sensorial/predatória em 12A/B, aldeões como civis vivos por 12C aprovado; meios, parâmetros e geometrias ainda precisam de ficha.

### 9.2. ENC-H02 — Ágil terrestre

**Função aprovada em 11B:** pressão em sequências curtas, seguida de reposicionamento/oportunidade de resposta. A ameaça exige escolher o momento de agir, sem sequência interminável que prenda o jogador. Menor Vida não é sua única característica.

**Sequência funcional candidata:** aproximação → preparo/compromisso do golpe → sequência limitada declarada → recuperação/reposicionamento. Novo golpe pode ter novo preparo conforme ficha; não seguir livremente o alvo no meio de contato comprometido. Quantidade de golpes, intervalos e permissões de interrupção ainda pendentes.

**Código existente:** variante menor, velocidade/cadência próprias e ataque comum com antecipação. Não demonstra a sequência/reposicionamento propostos como pacote fechado; números atuais não são aprovados para iniciantes.

**Enfrentamento pretendido:** defesa, distância e resposta entre pressões. Deslocamento rápido respeita corpo/colisão; não ganha dash, teleporte ou Shunpo por associação. Base sensorial/predatória em 12A/B e aldeões como civis vivos por 12C aprovado; água, parâmetros e geometria exigem ficha.

### 9.3. ENC-H03 — Voador

**Função aprovada em 11B:** aproximação aérea legível e ataque próximo do chão, com uma janela em que o jogador pode enfrentá-lo usando recursos básicos da etapa antes de ele se afastar. Combate aéreo não deve exigir voo ou arma à distância para toda origem.

**Sequência funcional candidata:** circulação → aproximação → ataque próximo e permanência/recuperação alcançável → afastamento. Tempo de alcance e quantidade de contatos precisam de calibração; não prometer imobilidade gratuita ou vulnerabilidade além do contrato de dano/controle.

**Código existente:** `HollowFlightCombatGoal` já alterna circulação, mergulho, contatos e retirada; verifica espaço/colisão em trajetos. Usa posição da entidade-alvo em trechos de navegação, que precisam ser conciliados com o contrato sensorial de M09. Não é aceite de alcance, câmera ou ritmo em terreno real.

**Enfrentamento pretendido:** reconhecer descida e responder na janela próxima. Teto/parede continuam limites; confinamento não autoriza travessia, dano remoto ou ataque novo. Base sensorial/predatória em 12A/B e aldeões como civis vivos por 12C aprovado; parâmetros, água e retorno ainda exigem ficha.

### 9.4. ENC-H04 — Chefe Hollow atual

**Papel aprovado em 11C:** confronto opcional preparado, com confirmação de início. Reúne leitura de golpe próximo, investida e ataque de área; não é requisito universal dos quatro prólogos ou das primeiras compras. A alternativa de ameaça espontânea não foi escolhida para esta primeira ficha; pode ter ficha própria no catálogo futuro.

**Código existente:** `MELEE`, `CHARGE`, `SLAM`, antecipações, recursos de apresentação e cooldowns; entrada visual articulada pela Garganta. A apresentação não concede viagem pela Garganta ou prova uma campanha de boss.

**Ficha de encontro a completar:** fonte/contexto/era/local; pré-requisitos acessíveis; condições de início e participantes; três geometrias/sinais; fases/limites; vitória/falha/interrupção e nova tentativa; predabilidade e prêmios; interação com terreno. Não presumir bloqueio de construção, destruição de blocos, ajuda canônica, imunidade ou mudança de fase por porcentagem de Vida.

Com 11C aprovado, herdar 06A/B, 07B e 08A no alcance do encontro preparado. Isso não declara instância privada, local definido ou boss pronto. Não transformar morte do ator em morte de NPC canônico permanente.

### 9.5. M09-12A — Comportamento pela referência de Bleach — DIRETRIZ DECIDIDA

**Resposta em 23/10/2026:** “Quero que siga como a obra”. Nenhuma das duas alternativas de temperamento foi escolhida. O pacote bruto territorial/ágil agressivo/voador apenas reativo foi retirado como recomendação. Seu texto integral permanece no histórico, sem vigência.

**Aplicação da pesquisa ENC-S06–09:** a base dos Hollows comuns é a procura de almas/presas e o comportamento perigoso de referência. Os três podem iniciar caça/confronto contra presas elegíveis detectadas; não exigir que o jogador bata primeiro só porque o corpo é voador. Atração por pessoas com maior poder espiritual pode orientar interesse, respeitando os sentidos, informação atual, prioridade estável e limites de perseguição aprovados. Não transformar isso num radar exato ou troca obrigatória de alvo a cada diferença de Reiatsu.

| Ficha | Base de comportamento | Diferença já aprovada e preservada |
|---|---|---|
| ENC-H01 — Bruto | Predação/hostilidade pertinentes ao indivíduo/contexto; não territorial obrigatório pela anatomia | Aproximação terrestre, preparo reconhecível e golpe amplo/lento, com oportunidade de resposta |
| ENC-H02 — Ágil | Mesma referência predatória; velocidade não determina personalidade ou inteligência | Sequências curtas e reposicionamento, sem pressão ininterrupta |
| ENC-H03 — Voador | Pode caçar e atacar sem provocação; não passivo por ser aéreo | Aproximação aérea e janela próxima enfrentável com recursos básicos antes de se afastar |

Os perfis usam visão e percepção espiritual natural limitada na tradução para o mod, sem Pesquisa concedida por classe, coordenadas ocultas ou sentidos adicionais não declarados. O alcance/sensibilidade e a resposta aos sinais precisam de ficha/calibração, conforme M06/M09. O cânone sustenta a referência de interesse/detecção; não determina blocos, ticks ou uma precisão igual para todos.

Uma personalidade, motivo ou capacidade individual pode justificar emboscada, defesa de área, negociação ou reação particular quando sua própria referência/ficha sustentar. M09-03A continua permitindo diversidade por perfil; esta revisão impede atribuí-la arbitrariamente ao formato dos três corpos. O veterano mentor já aprovado é um indivíduo com motivo de cooperação, sem fazer a espécie inteira pacífica.

### 9.6. M09-12B — Presas, oponentes e contexto — DIRETRIZ DECIDIDA; ADAPTAÇÕES DELIMITADAS

**Resposta em 23/10/2026:** “Quero que seja como na obra”. As alternativas originais não foram selecionadas. Retirada a recomendação de excluir aldeões por padrão apenas para restringir o recorte; a representação deles como civis vivos foi aprovada em 12C. Não limitar o universo ficcional a alvos que sejam jogadores.

**Presas de referência:** pessoas vivas e almas como Plus podem estar sujeitas à caça, conforme sinais detectáveis e condições reais do encontro. Shinigami, Quincy, Fullbringer e outros personagens espiritualmente relevantes podem entrar no conflito por sua condição e ações; origem não dá imunidade ou aliança automática. Um civil sem visão espiritual continua podendo ser vítima, sem receber informação visual que não possui.

**Hollows entre si:** ENC-S09 registra que alguns passam a buscar almas de outros Hollows. Não tornar todo Hollow automaticamente aliado de um jogador Hollow nem atribuir canibalismo obrigatório a todos. Competição, agressão, predação específica e relações legítimas vêm da ficha/contexto; presas evolutivas e participação do jogador mantêm M07/M09. Caça a NPC elegível não precisa que a presa já estivesse atacando antes.

Preservar proteções e acordos aprovados: atendimento essencial protegido, relações reconhecidas e permissões de dano continuam. Ser presa possível não garante acerto, morte, devoração, crédito ou recompensa. Golpes naturais compatíveis do corpo Hollow espiritual não exigem espada; ainda seguem alcance/colisão e defesas de M04/M06, sem ganhar Cero ou técnica nova.

**Criaturas vanilla:** aldeões são civis vivos caçáveis por 12C aprovado. Não inventar uma dieta canônica específica para vacas, pets, golems ou monstros Minecraft a partir de uma categoria genérica. Alimento, alvo tático, agressor e dano incidental são coisas diferentes; suas fichas devem explicitar a relação antes de oferecer esse comportamento. Não é declaração de imunidade física ou espiritual dessas criaturas. A regra vanilla de interagir com corpos jogáveis não decide a dieta de um Hollow NPC.

**Chefe ENC-H04:** permanece confronto opcional preparado, com início confirmado por 11C/06A; isso é adaptação já aprovada, sem dizer que todo chefe da obra espera uma interface. Não conceder Pesquisa/omniconsciência por ser chefe. Adversários, sentidos e ataques seguem compatibilidade e informação legítima durante a tentativa. A restrição nova e absoluta de selecionar somente participantes, proposta antes da resposta, não foi aprovada automaticamente. Entrada tardia, ajuda externa e efeitos sobre não participantes precisam da ficha/M15 antes da entrega; não criar imunidade de observadores, início compulsório ao passar perto ou instância privada por omissão.

### 9.7. M09-12C — Aldeões representam civis vivos — DECIDIDO

**Decisão de 23/10/2026:** tratar aldeões vanilla como civis vivos para essa relação de mundo, permitindo que Hollows os cacem quando detectados/expostos e conforme seu comportamento. Isso mantém a ameaça às pessoas do Mundo Humano em vez de limitar a ficção ao jogador. Aldeões não existem em Bleach: a associação é adaptação explícita do mod, não regra canônica demonstrada.

**Consequência concreta:** vilas expostas podem sofrer ataques e perdas; proteção de trajetos, abrigo, defesa e atendimento passam a importar também ali. M09-04A preserva o padrão sem alteração de blocos pelos mobs comuns do mod: habilitar aldeão como alvo não autoriza destruir casas para alcançá-lo. Fontes essenciais de ensino/serviço continuam protegidas pelo papel, sem proteger automaticamente todos os habitantes ou toda a cidade. Visibilidade do aldeão e possibilidade de se defender seguem a presença/compatibilidade já aprovadas; ser atacável não concede visão espiritual ou arma espiritual ao aldeão.

**Alternativa não adotada:** deixar a caça a civis restrita aos NPCs humanos/Plus de Bleach, sem aldeões como presas de alimentação nesta etapa. Preserva o cotidiano das vilas vanilla, mas separa moradores visualmente semelhantes nessa relação. Não implica imunidade geral a qualquer impacto ou controle; esses componentes precisam de ficha.

**Pergunta:** As fontes descrevem Hollows caçando almas de vivos e mortos. Para traduzir isso ao Minecraft, recomendo tratar aldeões como civis vivos, permitindo que Hollows os cacem quando expostos. Prestadores essenciais continuam protegidos, como aprovado. Aldeões não existem em Bleach, então essa associação é uma adaptação do mod. Adotamos essa representação ou deixamos a caça a civis restrita aos NPCs humanos/Plus criados para Bleach?

**Opções:** “Aldeões como civis vivos caçáveis (recomendado)” / “Somente civis/Plus próprios de Bleach”. **Resposta em 23/10/2026:** “Aldeões como civis vivos caçáveis (recomendado)”. Primeira opção aprovada; alternativa restrita aos civis/Plus próprios de Bleach não adotada. A decisão não classifica automaticamente toda fauna, pet, golem ou inimigo vanilla como presa.

## 10. Conteúdo inicial

A seleção destas quatro fichas foi aprovada por 11A. Produção, implantação, região e ordem de desbloqueio seguem decisões e dependências próprias. Não exigir criar novos modelos para elaborar as fichas nem excluir criaturas canônicas futuras por usar a amostra atual.

## 11. Visão completa

Expandir depois para famílias/regiões, classes Menos, indivíduos canônicos e ameaças por era, com fichas próprias e pesquisa. A lista de assets recebida continua banco de conteúdo, não lote de produção aprovado.

## 12. Decisões e entregas

11A, 11B e 11C aprovados, com perguntas/opções/respostas em [M09/12](09-mobs-bosses-e-encontros.md#1222-m09-11b--decidido--funções-dos-três-hollows-comuns). **M09-12A/B receberam diretriz explícita de seguir a obra**, com perfis corrigidos em 9.5–9.6, referências em 4 e [respostas/histórico preservados](historico/2026-10-23-propostas-m09-12ab-sentidos-hostilidade-e-alvos.md). **M09-12C decidido em 9.7**: “Aldeões como civis vivos caçáveis (recomendado)”. Associação aprovada como adaptação; não apresentar como fato canônico. Demais campos pendentes em 8/9; parâmetros, regiões, instrumentos/recursos e encontros completos mantêm seus responsáveis.

## 13. Critérios de aceitação futuros

1. Os três comuns exigem decisões de combate diferentes conforme 11B aprovado.
2. A janela do voador é utilizável com repertório básico da etapa, em terreno real.
3. Corpo, direção e geometria de contato correspondem aos sinais; erro não vira acerto por referência ao alvo.
4. A função do chefe corresponde ao tipo de encontro escolhido, sem tornar sua luta requisito universal por omissão.
5. Cada ficha executável resolve seus sentidos, alvos, meios, números e resultados antes da entrega.

Nenhum cenário executado nesta rodada.

## 14. Impacto técnico conhecido

Referências: [HollowVariant](../../src/main/java/com/bleachmod/entity/HollowVariant.java), [HollowEntity](../../src/main/java/com/bleachmod/entity/HollowEntity.java), [HollowFlightCombatGoal](../../src/main/java/com/bleachmod/entity/HollowFlightCombatGoal.java), [HollowBossEntity](../../src/main/java/com/bleachmod/entity/HollowBossEntity.java). Conferência dirigida dos mecanismos descritos em 23/10/2026; não auditoria integral, build ou gameplay. Código não foi alterado e valores não foram recalibrados.

## 15. Próximos passos

11B/C registrados e [revisão conceitual de M09](09b-revisao-m09-2026-10-23.md) preservada como alcance anterior. Retorno dirigido atual COV-08: M09-12A/B corrigidos conforme a direção de fidelidade; M09-12C decidido e aplicado às três fichas comuns: aldeões como civis vivos caçáveis. Coerência conferida com sentidos/corpos/Combat State e proteção essencial; revisão do checkpoint autorizado em M10b/9.4 e no parecer de cobertura, sem exigir catálogo completo para publicar este recorte. Catálogo permanece em especificação de conteúdo e calibração, sem alegar implementação, teste ou aceite.
