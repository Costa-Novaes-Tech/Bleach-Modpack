# M04a — Presença espiritual, visão e interação

## 1. Estado do bloco

| Campo | Estado em 23/10/2026 |
|---|---|
| Estado | `EM ESPECIFICAÇÃO` — retorno dirigido COV-05 |
| Decisões novas | RAC-30A/B/C/D `DECIDIDOS`; nenhuma resposta pendente deste bloco |
| Escopo | Presença das quatro origens atuais; visão direta, interação/dano e convivência com Minecraft |
| Herança | M04/RAC-01/04/06, origens e adiamentos; M06-11F/G/J; contratos M08/M09/M10 |
| Implementação/teste/aceite | Nenhum nesta rodada |

Entrada: [revisão de cobertura M01–M10](00-revisao-cobertura-m01-a-m10-2026-10-23.md). [M04](04-racas-e-origens.md) continua fonte de identidade/origens; este complemento não reabre híbridos, gigai ou Substituto.

## 2. Objetivo

Definir quem enxerga e interage com o personagem espiritual no recorte atual, antes de deixar essas escolhas ao renderer ou à IA. Manter a diferença de natureza em Bleach e a participação em construção, sobrevivência e cooperação de Minecraft.

## 3. Experiência do jogador

Compreender o que pode ver, por que alguém reage à sua presença e como um ataque ou interação pode acontecer. Conforme RAC-30A aprovado, o personagem não precisa comprar uma leitura avançada apenas para enxergar o inimigo que uma atividade inicial exige enfrentar. Conforme RAC-30B aprovado após reformulação, causar dano a um corpo espiritual exige um ataque/meio capaz de afetá-lo; visão adquirida não concede essa capacidade. RAC-30D aplica o mesmo requisito ao corpo espiritual do jogador, sem exceção automática para dano comum de mobs vanilla.

## 4. Base canônica e limites — pesquisa de 23/10/2026

| Fonte primária/oficial | Evidência usada | Limite |
|---|---|---|
| PRES-S01 — [VIZ, Bleach vol. 1](https://www.viz.com/manga-books/manga/bleach-volume-1/product/167/paperback), sinopse editorial | Ichigo já via espíritos antes de receber os poderes de Rukia | Consultada a apresentação do mangá, não lido o volume/capítulo integral nesta rodada |
| PRES-S02 — [Pierrot, resumo oficial episódios 1–20](https://pierrot.jp/title/bleach/playback_01.html), abertura do arco | Rukia se surpreende por Ichigo enxergá-la; o resumo diferencia essa capacidade da visão de uma pessoa comum | Anime oficial complementar; não define um limiar numérico ou a mesma percepção para todo humano |
| PRES-S03 — [TV Tokyo, episódio 5 — 見えない敵を殴れ！](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes01/index.html), sinopse | Chad luta em cooperação com Rukia contra Shrieker sem conseguir vê-lo naquele momento | Demonstra que falta de visão e impossibilidade de combater não são equivalentes. Não prova que qualquer pessoa/arma comum possa ferir qualquer espírito |
| PRES-S04 — [Shueisha/Shonen Jump, resumo oficial do arco do Fullbring](https://www.shonenjump.com/xmedia/bleach/story/index5.html), verbete 完現術 | Apresenta Fullbring como manipulação da alma da matéria e identifica o braço de Chad como uma dessas capacidades | Não descreve a causa exata de cada golpe contra Shrieker nem estabelece imbuimento automático de qualquer arma |

Nenhuma dessas fontes determina comportamento de Creeper, aldeão, espada de ferro, colisão de bloco ou progressão por SP. Regras de integração são adaptações identificadas. PRES-S03 não sustenta a proposta original de dano comum liberado por padrão; a recomendação foi retirada após a objeção do usuário. Chad não é uma base adequada para generalizar o comportamento de um humano ordinário. PRES-S04 tampouco comprova que aquele golpe específico foi um uso consciente de Fullbring.

Tentativa de conferir o capítulo 1 no leitor oficial VIZ em 23/10/2026: navegador redirecionou ao índice da série, informando que os capítulos disponíveis na localização devem ser acessados no parceiro MANGA Plus; extrator mostrou apenas a interface, sem os quadros. Nenhum capítulo/episódio integral foi conferido, e não se registra como comprovada uma lei universal de contato/dano ou uma fala não lida de Rukia.

## 5. Adaptação para Minecraft

Separar quatro perguntas: **enxergar o corpo**, **sentir a presença**, **poder selecionar/agir com informação válida** e **poder causar efeito no alvo**. Uma resposta não decide todas as outras. O mundo compartilhado e os sistemas vanilla precisam de uma política explícita, em vez de cada implementação escolher por omissão.

## 6. Escopo e adiamentos preservados

Inclui os corpos jogáveis atuais Shinigami, Hollow/Arrancar, Quincy e Fullbringer, seus observadores e interações ordinárias. Origem espiritual não concede voo, deslocamento livre entre mundos ou passagem através de paredes. Colisão/movimento de M06/M07 permanecem. RAC-30B deve resolver a elegibilidade de dano sem equipará-la à capacidade de construir ou comerciar.

Gigai, separação jogável de corpo/alma, Shinigami Substituto, híbridos e técnicas específicas de invisibilidade/intangibilidade ficam nas etapas já adiadas. Não simular um segundo corpo abandonado porque o personagem tem natureza espiritual. Morte comum continua derrota de gameplay, sem nova mudança de raça.

## 7. Modelo conceitual

| Informação | O que decide | O que não concede |
|---|---|---|
| Natureza/estado corporal | Presença atual e compatibilidades pertinentes à ficha | Invisibilidade, imunidade a todo ataque/ambiente e capacidade de atravessar blocos por associação |
| Visão espiritual ordinária | Enxergar presença espiritual exposta no campo de visão | Ler Reiatsu através de parede, ver técnica de ocultação ou saber identidade/poder exatos |
| Percepção Espiritual | Indício natural e leitura aprendida conforme M06-11F/G | Enxergar um alvo por si só, mira automática ou autorização de dano |
| Perfil do observador | Quais sentidos possui e a que informação pode reagir | Coordenadas de alvo que não percebe, hostilidade ou direito de atacar |
| Compatibilidade da ação | Se alcance/colisão/alvo/defesas permitem o efeito | Acerto automático porque alguém detectou uma presença |
| Política vanilla aprovada em RAC-30C | Preservar percepção/interação ordinária de Minecraft com corpos jogáveis | Tornar todo aldeão/personagem humano de Bleach espiritualmente sensível no cânone ou dispensar o requisito de dano de RAC-30B/D |

## 8. Perfis e estados das decisões

### 8.1. RAC-30A — Visão espiritual confiável antes do combate exigido — DECIDIDO

**Resposta em 23/10/2026:** “Visão no prólogo antes do combate exigido (recomendado)”.

Shinigami e Hollow/Arrancar têm visão ordinária de seres espirituais pela sua condição; Quincy/Fullbringer têm despertar estabelecido no próprio prólogo, **antes da primeira atividade de combate que exija enfrentar esses seres**. O prólogo pode mostrar indícios prévios e descoberta, sem impor um estágio obrigatório de lutar às cegas. Cada origem conserva seu acontecimento próprio.

Essa visão básica não é uma nova skill comprada, Proficiência ou Mastery; é condição de presença/despertar da origem. A leitura aprendida de Reiatsu continua aquisição própria, com SP e recursos conforme M05/M06. Energia atual zero e fechar a Combat Hotbar não apagam a visão ordinária adquirida. Supressão continua sem tornar o corpo invisível.

**Alternativa não adotada:** despertar visual mais tarde e permitir atividades iniciais contra entidades ainda não vistas, com linguagem de sinais/cooperação própria. A escolha aprovada garante legibilidade das quatro jornadas; não é uma regra universal de despertar de todo Quincy/Fullbringer da obra.

### 8.2. RAC-30B — Elegibilidade do dano contra corpos espirituais — DECIDIDO APÓS REFORMULAÇÃO

**Primeira resposta em 23/10/2026:** “mas no anime não é assim como você recomendou”. O usuário questionou a fidelidade da recomendação; naquela resposta não aprovou uma alternativa de dano. A pergunta original e a proposta permissiva permanecem no histórico, sem vigor como decisão. A recomendação de aceitar dano comum por padrão foi retirada. **Resposta à pergunta reformulada, na mesma data:** “Exigir capacidade espiritual do ataque (recomendado)”. Requisito espiritual **DECIDIDO**; não repetir essa pergunta.

**Regra aprovada:** para causar dano a corpos espirituais de Bleach, exigir que o ataque ou meio utilizado tenha capacidade de afetá-los. Zanpakutō, técnicas e ataques corporais espirituais compatíveis podem cumprir esse requisito; não exigir uma espada para todo combatente. Uma espada de ferro ou flecha vanilla comum não adquire essa capacidade apenas porque o usuário enxerga o alvo, possui Energia ou escolheu uma origem. Imbuimento ou preparação de objetos só existe quando uma capacidade/ficha o permitir, sem conceder uma skill universal agora.

Elegibilidade espiritual não garante dano suficiente, acerto, autorização de PvP ou ignorar armadura. Depois dessa validação, alcance, colisão, atributos e proteções compatíveis continuam com M06. Dano espiritual não ignora as defesas vanilla por associação; a regra de redução já aprovada continua independente.

Separar a interação com blocos, ferramentas, inventário e comércio dessa elegibilidade ofensiva. O comportamento de mobs/NPCs vanilla aprovado em RAC-30C permanece; D aplica o mesmo requisito ao **dano efetivo** desses ataques contra corpos espirituais jogáveis. Não transformar os golpes em espirituais por omissão nem ampliar a regra para imunidade a todo risco. Riscos ambientais exigem seu recorte; B/D não desligam gravidade, queda ou comportamentos ambientais já aprovados.

**Pergunta reformulada:** Para o dano contra corpos espirituais de Bleach, recomendo exigir um ataque ou arma capaz de afetá-los: Zanpakutō, técnica ou ataque corporal espiritual compatível. Uma espada de ferro ou flecha vanilla comum não ganha essa capacidade só porque o personagem enxerga espíritos; preparar um objeto exigiria uma capacidade própria. Construção e comércio ficam separados dessa regra. Adotamos esse requisito espiritual ou liberamos também armas comuns por adaptação?

**Opções:** “Exigir capacidade espiritual do ataque (recomendado)” / “Permitir também armas comuns por adaptação”. **Resposta em 23/10/2026:** “Exigir capacidade espiritual do ataque (recomendado)”. Alternativa permissiva não adotada. O requisito aprovado busca preservar a distinção de Bleach; fontes acessadas não comprovam todos os limites e exceções como uma lei universal da obra.

### 8.3. RAC-30C — Preservar comportamento vanilla; perfis sensoriais próprios em Bleach — DECIDIDO

**Resposta em 23/10/2026:** “Preservar interação vanilla (recomendado)”.

Mobs e NPCs vanilla continuam podendo perceber/interagir com os corpos jogáveis do mod conforme seu comportamento habitual. Hostis mantêm suas condições de alvo/ataque; passivos não ficam agressivos; aldeões mantêm suas regras de atendimento. Natureza espiritual, por si só, não desliga Creepers/zumbis nem impede comércio comum. A aprovação inicial de C não aprovou a proposta permissiva de dano, questionada na mesma resposta. B foi posteriormente decidido na direção do requisito espiritual; D confirmou o mesmo critério contra corpos espirituais jogáveis. Detectar, tentar atacar e efetivamente causar dano continuam distintos.

Personagens/NPCs e criaturas de Bleach usam seu perfil sensorial/contexto: um civil humano sem visão espiritual não passa a enxergar uma alma por causa dessa compatibilidade vanilla. Pode perceber consequências e sinais que sua ficha permitir; não recebe a posição exata da causa invisível. Uma fonte essencial precisa perceber/interagir com quem atende, por competência/condição adequada explicitada na ficha. A regra não concede assinatura espiritual detectável a todo mob vanilla.

**Alternativa não adotada:** aplicar também a todos os mobs/NPCs vanilla uma classificação de sensibilidade espiritual, alterando quem pode perceber/atender/atacar cada corpo. Mantida a compatibilidade ordinária com Minecraft e perfis específicos para conteúdo Bleach; é uma adaptação explícita, não alegação sobre espíritos na obra.

### 8.4. RAC-30D — Dano vanilla contra corpos espirituais jogáveis — DECIDIDO

**Resposta em 23/10/2026:** “Mesmo requisito espiritual para NPC e jogador (recomendado)”. Decisão de conciliação entre B e C; requisito espiritual de B e percepção/interação vanilla de C preservados. Não perguntar novamente se o jogador enxerga espíritos, se uma arma comum ganha capacidade espiritual ou se aldeões mantêm seu comércio.

**Regra aprovada:** aplicar o mesmo requisito espiritual a NPC e jogador. Um golpe comum de zumbi ou flecha comum de esqueleto não causa dano a um corpo espiritual por ser apenas um ataque material ordinário; um ataque que efetivamente tenha compatibilidade espiritual continua podendo causar dano, conforme sua ficha. Mobs mantêm detecção, escolha de alvo e tentativa de ataque conforme C, sem ganhar uma nova capacidade por isso. Quincy/Fullbringer **quando em corpo humano material** continuam sujeitos a dano comum; estado corporal real determina o caso, não só o nome da origem. Outros corpos/estados exigem sua ficha, sem reabrir gigai ou separação jogável.

A consequência aprovada é reduzir a ameaça ofensiva dos mobs vanilla sem capacidade espiritual contra personagens em corpo espiritual. Ameaças de Bleach e ataques espiritualmente compatíveis continuam pertinentes. Não ocultar essa mudança na sobrevivência nem prometer que o balanceamento atual já a comporte.

**Alternativa não adotada:** permitir que mobs vanilla causem dano normal também a corpos espirituais jogáveis, como exceção explícita de sobrevivência de Minecraft. Criaria uma diferença entre a regra do jogador usar arma comum contra um Hollow e a regra do mob vanilla ferir o jogador espiritual. Não introduzir essa exceção sem revisão expressa de D.

O recorte é ataque de mob vanilla contra corpo jogável. Queda, gravidade, colisão e regras ambientais já aprovadas não são desligadas por associação; explosões e componentes mistos precisam de classificação clara antes da implementação, conforme a escolha. Não usar essa pergunta para rever a utilidade das armaduras e reduções compatíveis de M06.

**Pergunta:** Para aplicar a regra aprovada também ao jogador, recomendo o mesmo critério para todos: golpes comuns de zumbis e flechas comuns de esqueletos não ferem um corpo espiritual Shinigami/Hollow; ataques espiritualmente compatíveis podem feri-lo. Quincy e Fullbringer, quando em corpo humano material, continuam vulneráveis ao dano comum. Os mobs ainda detectam e atacam, conforme aprovado. Adotamos essa consequência ou criamos uma exceção de sobrevivência para mobs vanilla causarem dano normal também a corpos espirituais jogáveis?

**Opções:** “Mesmo requisito espiritual para NPC e jogador (recomendado)” / “Exceção: mobs vanilla também ferem jogadores espirituais”. **Resposta em 23/10/2026:** “Mesmo requisito espiritual para NPC e jogador (recomendado)”. Adaptação/compatibilidade do mod, sem alegação de zumbis/esqueletos Minecraft na obra.

## 9. Integrações

M04 coordena presença e visão da origem; M06 interpreta alvo/efeito e conserva leitura de Reiatsu; M08 define perfil sensorial do prestador e acesso ao ensino/reset; M09 define percepção/informação da IA e reação; M10 fornece espaço/percurso/condição do mundo; M14 apresenta estados e resultados; M15/M16 aplicam a política no servidor e validam compatibilidade. As propostas não alteram PvP, filiação ou autorização só por visibilidade.

## 10. Matriz inicial de cobertura — A/B/C/D aprovados

| Observador/situação | Visão/percepção aprovada em A/C | Interação e fronteira |
|---|---|---|
| Jogador Shinigami ou Hollow/Arrancar | Vê presenças espirituais ordinárias expostas | Sem passagem por blocos; causar dano ao corpo espiritual exige compatibilidade, inclusive quando o alvo é jogador |
| Jogador Quincy/Fullbringer após despertar inicial | Vê antes do primeiro combate exigido contra esses seres | Não precisa adquirir a leitura ativa apenas para mirar; técnica de ocultação é outro caso |
| Humano de Bleach sem visão espiritual | Não enxerga diretamente a presença espiritual ordinária | Pode reagir a informação/sinal autorizado; não recebe conhecimento do alvo invisível |
| Fonte espiritual qualificada do mod | Perfil precisa permitir perceber o público atendido | Competência e motivação continuam próprias; requisito necessário ao serviço essencial |
| Mob/NPC vanilla | Mantém a percepção/interação ordinária com os corpos jogáveis | Sem nova agressividade/assinatura universal; ataque comum sem compatibilidade espiritual não causa dano ao corpo espiritual do jogador |
| Leitura de Reiatsu através de obstáculo | Entrega indício conforme M06 | Não mostra o corpo, não revela coordenadas exatas e não permite golpe através do bloco |

## 11. Visão completa

As fichas futuras podem tratar estados materializados, corpo separado e exceções de poder. Precisam apontar qual contrato alteram e como retornam ao estado comum. Esse horizonte não antecipa as escolhas adiadas de gigai/híbridos ou cria camadas corporais extras para todas as origens agora.

## 12. Decisões abertas

RAC-30A/B/C/D decididos em 23/10/2026; nenhuma resposta pendente deste bloco. B foi aprovado após objeção de fidelidade e retirada da recomendação original; [histórico integral](historico/2026-10-23-propostas-m04-rac30abc-presenca.md) preserva pergunta anterior, objeção, resposta à reformulação e conciliação D. Permanecem entregas de conteúdo: classificar componentes mistos/ambientais pertinentes, competências dos ataques e perfis de sentidos nas fichas M06/M08/M09/M11, antes da implementação de cada caso. Isso não reabre o requisito geral escolhido. Corpo/posição do reset decidido em M04/RAC-31A: recomeço no atendimento do NPC, com estado inicial próprio e validação antes da conclusão. Segue Combat State em M06.

## 13. Critérios futuros de validação

Prólogo não exige acertar um alvo sem os meios escolhidos; visão não depende de saldo de Energia ou leitura ativa; civil não escolhe alvo que não percebe; golpe cego não revela alvo; arma comum não ganha compatibilidade por visão/reserva/origem; dano de mob vanilla segue D sem remover queda e demais contratos ambientais aprovados; prestador atende as quatro origens previstas sem abrir uma interface impossível; leitura por obstáculo não entrega visão exata. Cenários documentais associados às escolhas; nenhum foi executado.

## 14. Impacto técnico conhecido

Somente documentação e pesquisa de fontes públicas nesta rodada. Não foram auditados renderer, colisão, filtros de dano/IA, pacotes ou interações atuais para certificar conformidade. Nenhuma alteração de código, mundo, API, build ou teste de jogo. A matriz registra decisões de produto aprovadas com fichas de componentes ainda necessárias, sem inventário de funcionalidades implementadas.

## 15. Continuidade

RAC-30A/B/C/D e corpo/local do reset RAC-31A aprovados; referências aplicadas nas integrações M04/M06/M08/M09/M10 sem repetir perguntas gerais. Seguir ao Combat State de M06 e complementos necessários. O commit/push continua autorizado após o bloco completo, conforme handoff; M10 e os demais recortes não se tornam executáveis por esta revisão documental.
