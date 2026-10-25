# M10 — Dimensões, locais e estruturas

> **Integração de Combat State — 23/10/2026:** [M06a/CS-01A/B/C/D](06a-estado-de-combate.md) decidido. Passagens comuns continuam fora do estado automático de combate, mesmo com a interface fechada. Perigo/dano atual incompatível também impede abertura/travessia, conforme a ficha, ainda que o indicador esteja OFF. Validar origem, destino, corpo/acesso e fases nas etapas próprias; não usar o portal para apagar conflito. D preserva a janela quando uma mudança de dimensão legítima acontecer, sem autorizar viagens comuns em luta. Números, perigos/componentes concretos e execução continuam por ficha; nenhum mapa/save alterado.

## 1. Estado do módulo

| Campo | Estado em 23/10/2026 |
|---|---|
| Estado documental | `EM ESPECIFICAÇÃO` |
| Decisões | M10-01A/B e 02A/B `DECIDIDOS`; 02C direção complementar registrada; 03A `REVISADO` por 03D; 03B/C/D/E e 04A `DECIDIDOS`; 05A/B/C `DECIDIDOS`; 06A `DECIDIDO`; 07A `DECIDIDO`; 07B `DECIDIDO` com restrição à mesma party; 07C `DECIDIDO`; 07D/E `DECIDIDOS`; 07F/G `DECIDIDOS`; bloco comum de passagens consolidado conceitualmente; 08A `DECIDIDO` por aprovação condicionada à pesquisa; 08B `DECIDIDO`; 08C `DECIDIDO`; 08D `DECIDIDO`; bloco de renascimento/retorno consolidado conceitualmente; 08E `DECIDIDO NO RECORTE HUECO MUNDO`; 08F `DECIDIDO`; 08G `DECIDIDO`; catálogo funcional de lugares aberto; 09A `DECIDIDO`; 09B `DECIDIDO`; 09C `DECIDIDO`: primeiro acesso por missão/contexto da campanha |
| Base examinada | `main`, `3a37ca761a95faa75163c3335536bb9e0c5a80f3`, com ampliações documentais locais |
| Implementação | Preset de História, mapas/manifesto, instalador e transporte de desenvolvimento já existentes; sem alteração nesta rodada |
| Teste/aceite | Não realizados nesta rodada |

Entrada: [revisão de M09](09b-revisao-m09-2026-10-23.md). Regras gerais M09 especificadas conceitualmente; [catálogo M09a](09a-catalogo-funcional-de-adversarios-e-encontros.md) continua em especificação. M10 define geografia e acesso, sem declarar os prólogos ou a amostra executáveis. [Perguntas/respostas 01A/B](historico/2026-10-23-propostas-m10-01ab-geografia-e-protecao.md); [perguntas/respostas 02A/B e complemento 02C](historico/2026-10-23-propostas-m10-02ab-rotas-e-travessia.md); [pergunta/resposta 03A](historico/2026-10-23-proposta-m10-03a-destinos-das-passagens.md); [pergunta/resposta 03B](historico/2026-10-23-proposta-m10-03b-chegada-invalida.md); [pergunta/retorno 03C e resposta 04A](historico/2026-10-23-propostas-m10-03c-04a-busca-e-retorno.md).

## 2. Objetivo

Criar lugares de Bleach reconhecíveis dentro de um mundo persistente de Minecraft, com orientação, viagem, exploração, coleta e construção. O local precisa oferecer funções e percursos, além de cenário.

## 3. Fantasia do jogador

Ter um lugar para viver/construir, descobrir rotas e alcançar territórios espirituais com contexto. Um núcleo reconhecível ajuda a se orientar; sair dele oferece exploração e oportunidades compatíveis com o mundo. Personagens de origens diferentes continuam tendo motivos para visitar os mesmos destinos.

## 4. Base canônica e limites

Mangá como base conforme D05; nesta abertura foram reconsultados glossários oficiais do anime, complemento histórico em japonês, em **23/10/2026**. Não houve leitura de capítulos/episódios integrais.

| Fonte | Recorte utilizado | Limite |
|---|---|---|
| WORLD-S01 — [Pierrot sa](https://pierrot.jp/title/bleach/keyword_sa.html) | Seireitei como núcleo da Soul Society; Senkaimon como passagem para Soul Society | Não determina escala em blocos, coordenadas, terrenos gerados ou permissões de construção |
| WORLD-S02 — [Pierrot ra](https://pierrot.jp/title/bleach/keyword_ra.html) | Rukongai e Las Noches no contexto descrito pela fonte | Não fixa disponibilidade de instituições/elenco em toda era nem reproduz mapas do mod |
| WORLD-S03 — [Pierrot ka](https://pierrot.jp/title/bleach/keyword_ka.html), recorte já reconsultado nesta sequência em 23/10 | Garganta como espaço/passagem associado aos Arrancar | Não comprova viagem universal gratuita, travessia instantânea, destino seguro ou capacidade de todo jogador abrir uma |

**Aprofundamento de 23/10/2026 — WORLD-S04/S05:** o [glossário oficial de TYBW, verbete 黒腔](https://bleach-anime.com/keyword/), em resultado indexado consultado nesta data, descreve Urahara ativando uma técnica com encantamento para abrir, ajustar e estabilizar a Garganta; descreve também o dispositivo desenvolvido por Mayuri após analisar os dados de Szayelaporro. A abertura integral dessa página retornou HTTP 403; não se declara leitura integral. A [sinopse oficial da TV Tokyo, episódio 196](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes17/index.html) confirma a estabilização por Urahara para a passagem dos capitães. São fontes oficiais do anime complementar; não houve acesso aos capítulos ou episódios integrais.

Isso sustenta casos de abertura por conhecimento/técnica e tecnologia fora da capacidade natural Hollow, sem provar que qualquer Shinigami possa fazê-lo. Não foi estabelecido neste recorte um Hollow abrindo Senkaimon naturalmente. Número/classificação de Kidō, receita, componentes, consumo, ensino ao jogador e elegibilidade de todas as origens não foram demonstrados; não os preencher por associação.

**WORLD-S06 — esclarecimento de destinos em 23/10/2026:** o [resumo oficial Pierrot do arco 21–41](https://pierrot.jp/title/bleach/playback_02.html), abertura do arco, situa a chegada do grupo no Rukongai, fora da Seireitei. O [resumo licenciado Hulu do episódio 21](https://www.hulu.jp/watch/100044758?t=0), consultado em resultado indexado (abertura integral indisponível), identifica a entrada pelo Senkaimon. Isso refuta tratar Senkaimon como chegada necessariamente dentro da Seireitei; não demonstra alcance irrestrito a qualquer coordenada. WORLD-S04 descreve o dispositivo de Mayuri conectando Soul Society e Hueco Mundo, além da ligação usual Mundo Humano/Hueco Mundo. A variedade de conexões também não comprova precisão ilimitada de todo usuário. Fontes complementares do anime; nenhum episódio/capítulo integral consultado nesta rodada.

**WORLD-S07 — identidade do núcleo humano, 23/10/2026:** o [resumo oficial Pierrot do arco 1–20](https://pierrot.jp/title/bleach/playback_01.html), lido integralmente como página de resumo, situa Rukia, ocorrências Hollow e a loja Urahara no contexto de Karakura. A [apresentação oficial da primeira parte de TYBW](https://bleach-anime.com/story/story-1st.html), consultada em resultado indexado (abertura integral HTTP 403), identifica Karakura como a cidade onde Ichigo vive e onde surgem novas ocorrências. São fontes complementares do anime, sem leitura de capítulos ou episódios integrais. Não fornecem planta completa, escala em blocos, posições de spawn ou aprovação do mapa local; adaptar Karakura como núcleo foi aprovado como direção de produto em 05C; a fonte não aprova sua representação local.

**WORLD-S08 — ambiente de Hueco Mundo, consulta de 23/10/2026:** o [glossário oficial Pierrot, verbete 虚圏/ウェコムンド](https://pierrot.jp/title/bleach/keyword_a.html), trecho do verbete acessível integralmente, descreve noite permanente, solo semelhante a areia branca, árvores de aspecto semelhante a quartzo e ausência de água. Fonte histórica do anime complementar; não houve leitura de capítulo/episódio integral. Não demonstra evaporação de água transportada, regras de balde, agricultura ou física de blocos. Ausência de rios/lagos naturais é o recorte adotado para geração em 08A; permitir água trazida é adaptação aprovada condicionalmente pelo usuário, aplicada após pesquisa dirigida sem localizar uma proibição explícita de transporte/estabilidade. Não é fato canônico demonstrado nem pesquisa exaustiva de toda a obra. A informação de nutrição Hollow por Reishi encontrada em resultado indexado não é convertida nesta rodada em cura, fome vanilla removida ou regeneração gratuita.

**WORLD-S09 — consulta dirigida em 23/10/2026:** reabertos os verbetes oficiais Pierrot Garganta/Senkaimon, sem estágio mínimo ou marco de formação universal. Referências secundárias indexadas: [Kūmon](https://bleach.fandom.com/wiki/K%C5%ABmon) aponta Menos/Gillian e mangá 45/p.2, anime 14; [Senkaimon](https://bleach.fandom.com/wiki/Senkaimon) aponta abertura com Zanpakutō/Renji e mangá 56/pp.17–18. Páginas integrais gerais da wiki retornaram HTTP 402; o [capítulo oficial VIZ 56](https://www.viz.com/shonenjump/bleach-chapter-56/chapter/14273) foi localizado em índice, exigindo assinatura. Não houve leitura integral de capítulo/episódio nem verificação visual dessas páginas. Gillian é exemplo localizado, não mínimo universal comprovado; Renji abre antes da conquista de Bankai, sem demonstrar que qualquer aluno saiba fazê-lo. Abertura própria precisa de ficha e evidência; primeira travessia por operador não concede essa capacidade. [Registro detalhado e limites](historico/2026-10-23-proposta-m10-09c-momento-do-acesso-hollow.md).

**WORLD-S10 — conteúdo local e acesso institucional, 23/10/2026:** reconsultados os verbetes [Rukongai/Las Noches](https://pierrot.jp/title/bleach/keyword_ra.html), [Seireitei/Soul Society](https://pierrot.jp/title/bleach/keyword_sa.html) e o [resumo oficial do arco 21–41](https://pierrot.jp/title/bleach/playback_02.html). O verbete de Rukongai descreve vida nos distritos e diferenças de segurança; Seireitei tem acesso restrito, e o resumo mostra a entrada sendo impedida por autoridades. Las Noches é situada no contexto de Aizen e subordinados. Páginas históricas do anime complementar acessíveis, sem leitura de capítulos/episódios integrais. Não sustentam currículo, economia, autorização de todo visitante ou refúgios cooperativos originais; atividades e representação espacial do mod são adaptações com decisão própria.

**WORLD-S11 — noite e chuva em Soul Society, 23/10/2026:** a [sinopse oficial TV Tokyo do episódio 23](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes02/index.html) situa o grupo no Rukongai e menciona explicitamente a noite anterior e a manhã seguinte. A [página oficial TYBW, episódio 7 — BORN IN THE DARK](https://bleach-anime.com/story/1st/?id=07) situa os acontecimentos em Seireitei; sua galeria foi inspecionada visualmente no navegador, com quadro de Byakuya sob chuva visível. O extrator dessa página retornou 403, mas a página pública abriu normalmente no navegador; não se declara episódio integral assistido. São evidências do anime oficial complementar, suficientes para confirmar os fenômenos lembrados pelo usuário, sem fixar física astronômica, duração de dia/noite ou probabilidade de tempestade. Não usar o título EVERYTHING BUT THE RAIN como prova de chuva em Soul Society: seu contexto é outro. A resposta condicionada de 11A foi atendida; tempos, clima regional e regras de cama permanecem adaptações de gameplay.

Geração mista, proteções e tempos/condições de viagem são adaptações de gameplay; a fonte não aprova essas regras. Os nomes dos mapas locais e sua aparência não provam fidelidade canônica. Núcleo urbano humano não foi identificado automaticamente como uma reprodução pronta de Karakura.

## 5. Adaptação para Minecraft — herança

- M04 já aprovou nascimento físico no Overworld/Mundo Humano, com introdução racial própria; a passagem inicial Shinigami leva à Soul Society/Academia. Não é nova votação de dimensão de nascimento.
- M03 mantém mundo/era compartilhados e progresso pessoal; mudança narrativa não autoriza sobrescrever construções ou reabrir toda campanha de outro jogador.
- M04/RAC-18 mantém acesso contextual por era, relação e requisitos, sem trava absoluta de raça. Acesso físico, aceitação institucional e aquisição de poder são coisas diferentes.
- M08 exige acesso viável às fontes essenciais e coerência com a era. Proteger blocos não substitui disponibilidade de mentor ou continuidade de serviço.
- M09 rege hostilidade, percepção, busca, terreno e encontros; sair de uma região/interface não cura ou apaga tempos. Proteção de atendimento não é tanque do jogador.
- M06/M07 mantêm colisão, corpos, queda, recursos e limpeza de estados nas mudanças de dimensão. Viajar não reativa automaticamente forma ou devolve gastos.

## 6. Escopo

| M10 define | Integração necessária |
|---|---|
| Dimensões, regiões, núcleos, estruturas e rotas | Narrativa/era: M03/M13; atividades: M12 |
| Geografia, continuidade do terreno e implantação de conteúdo | Implementação/migração: M16 |
| Proteção localizada e convivência com construção/exploração | Terreno/combate: M09; itens: M11; servidor: M15 |
| Acesso, travessia, destino/retorno e condições espaciais | Capacidades: M06/M07; party/PvP: M15 |
| Requisitos de lugares para ensino e caçada | Fontes: M08; adversários: M09 |

Não completa aqui cada campanha, cidade, receita, custo de SP, técnica de portal ou asset. Direitos/procedência de mapas e adequação ao produto exigem conferência antes de uma distribuição de conteúdo; a documentação não redistribui mapas nem os substitui.

## 7. Modelo conceitual

| Registro funcional | Informação necessária |
|---|---|
| Dimensão | Identidade, regras físicas/ambientais, geração e destinos presentes |
| Região/núcleo | Função, contexto/era, limites, identidade e implantação no save |
| Estrutura | Presença física, acesso, uso e blocos essenciais protegidos quando definidos |
| Rota/passagem | Origem, destino, fonte, condições de acesso, execução e sentido de retorno |
| Área de chegada | Validade espacial para corpo, trajetória/queda e contingência |
| Estado persistente | Conteúdo instalado, construções, acontecimentos autorizados e alterações próprias do save |

São responsabilidades, não novas classes, banco externo ou promessa de instâncias privadas. Identidade de local é estável no save, sem escolher coordenadas globais ou tamanho literal da obra.

## 8. Regras e propostas atuais

### 8.1. Geografia mista — M10-01A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Núcleos planejados com áreas geradas ao redor (recomendado)”. Combinar núcleos planejados/estáveis em cada save com áreas geradas ao redor para exploração, recursos e construção, respeitando a identidade da dimensão. Mapas atuais são ponto de partida a avaliar; não foram aprovados integralmente como versão final.

Escala, limites dos núcleos, posições, geração das bordas, terreno/biomas/recursos e combinação com os arquivos existentes exigem ficha. O preset atual usa geradores planos/vazios em trechos; isso não demonstra o novo modelo misto. Implementação futura precisa tratar costuras e chunks existentes sem sobrescrever o mundo. Não executar instalação, conversão ou regeneração nesta etapa documental.

Alternativa não escolhida: mundos inteiramente pré-construídos. Dá controle de composição, mas exige produzir/manter toda a extensão e o conteúdo de exploração. A escolha mista não obriga gerar estruturas canônicas repetidas nem reserva toda a dimensão à campanha.

### 8.2. Proteção localizada — M10-01B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Proteção pequena e sinalizada em trechos essenciais (recomendado)”. Proteger e sinalizar trechos pequenos de blocos essenciais de acessos, serviços e pontos de viagem; o restante conserva construção/mineração conforme regras do local. Não proteger automaticamente toda cidade, todos os blocos decorativos ou o corpo de todos os personagens.

Cada ficha delimita o trecho, motivo, ações bloqueadas, intervenções autorizadas e feedback. Remoção/substituição, explosões, fogo, líquidos e mecanismos indiretos precisam ser tratados para que a proteção real corresponda ao propósito; critérios de blocos/mecanismos e integração com servidor exigem implementação própria. Não presumir plugin de claims já instalado. Mudanças narrativas autorizadas precisam respeitar construções/progresso e as regras de era.

Proteção física não concede acesso social, desliga combate, cura jogador, impede todo inimigo de entrar ou corrige uma passagem bloqueada por outro meio. Efeitos destrutivos excepcionais de M09 continuam sujeitos às regras do lugar/servidor. A alternativa de tudo editável com reposição de acessos/serviços não foi escolhida; não prometer reconstrução automática.

### 8.3. Rotas iniciais e autonomia aprendida — M10-02A DECIDIDO

**Aprovado com direção própria em 23/10/2026.** Resposta integral em 12.3 e no histórico. Acesso inicial por rotas/serviços coerentes com o contexto de cada era; posteriormente, skills permitem abrir passagens próprias. Senkaimon para o caminho Shinigami e Garganta para o caminho Hollow/Arrancar são as referências apresentadas pelo usuário, sem exclusividade racial absoluta. Abrir e atravessar são capacidades distintas; um passageiro não aprende a abrir por ter viajado.

Condição racial, estágio, fonte qualificada, conhecimento, instrumentos e contexto precisam ser especificados por via. Não conceder Garganta automaticamente a todo estágio Hollow, Senkaimon a todo Shinigami ou ambos a todas as origens. O complemento 02C em 8.6 admite técnica de abertura/estabilização e dispositivo. Skills seguem aquisição contextual e compra com SP, Mastery pela prática; custos de uso continuam nas fichas. Dispositivo depende de M11, sem receita/moeda escolhida.

A introdução Shinigami mantém rota viável à Soul Society/Academia antes dessas skills. A expressão do usuário “no inicio de cada era” orienta a disponibilidade contextual de rotas/serviços; não autoriza apagar ou suspender aprendizados a cada mudança de era. Eventuais restrições narrativas precisam de regra própria em M03/M13, preservando os contratos de conquistas.

Não foi escolhido um item pessoal universal para todos os destinos. Os itens `travel_*` existentes permanecem transporte de desenvolvimento. Abertura própria não ignora acesso, barreiras, era ou validação de destino. 07A–G definiram o perfil das skills comuns e convidados da mesma party, incluindo ida/volta. Locais, requisitos de aquisição, instrumentos e parâmetros de cada via continuam por ficha; serviços/dispositivos não herdam automaticamente todo o perfil das skills.

### 8.4. Passagens comuns fora de combate — M10-02B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Passagens comuns somente fora de combate”. Segunda opção escolhida, diferente da recomendação inicial. Passagens comuns não funcionam como fuga durante combate. A restrição usa o Combat State automático de M06/5; fechar a interface/Combat Mode não a contorna. A condição acompanha a classe “passagem comum”, seja por serviço, skill ou dispositivo; a fonte não cria sozinha uma exceção. Técnicas especiais de fuga exigem uma regra própria antes de existir, sem presumir sua aprovação.

A proposta de travessia arriscada durante combate não foi escolhida. O seu preparo comprometido e suas condições de interrupção não se tornam regras aprovadas pela escolha da outra opção. 07C/D definiram o custo de abertura sem manutenção e o preparo cancelável das skills comuns; parâmetros, validações e perfis de serviços/dispositivos ainda exigem ficha. Uma viagem concluída preserva cooldowns/consequências e aplica os contratos de mudança de dimensão de M06/M07.

Antes de implementar, M06/M09/M15/M16 precisam definir entrada/saída do Combat State e o tratamento de combate residual, adversário sem solução de percurso e feedback da recusa. Não inventar um prazo de saída, limpar o combate pelo portal ou permitir bloqueio permanente por mera proximidade. Portais vanilla/outros mods não são alterados por essa aprovação; encontros e passagens especiais exigem seus contratos próprios.

### 8.5. Entregas abertas após consolidação

Decisões estruturais de destinos, registro, convidados e ida/volta estão aprovadas em 03D/E e 07A–G. A lista seguinte trata das fichas e lacunas restantes, sem reabrir essas escolhas: geografia concreta/bordas; ambiente, recursos e riscos; fontes/rotas por origem/era; espaço de formação e chegada, corpos grandes, queda/água; retorno quando a passagem termina; simultaneidade e transações; outros tipos de entidade; perda de instrumento quando exigido; instalação em modos/saves novos ou existentes e intervenção por era. 03B/C definem busca próxima e permanência na origem sem solução; raio/ordem e segurança concreta continuam por ficha. [Revisão dirigida e responsáveis](10a-revisao-m10-2026-10-23.md) organizam as entregas antes de implementar. Nenhum valor, destino alternativo ou substituição de chunks foi escolhido por omissão.

### 8.6. Técnica de estabilização e dispositivo — M10-02C DIREÇÃO APROVADA

**Complemento explícito do usuário em 23/10/2026:** “Então vamos seguir a obra, podemos ter tanto o dispositivo quanto o que faz o Urahara abrir e estabilizar, como ele faz?”. Adotar ambas as vias como conteúdo a especificar: técnica de abertura/estabilização referenciada em Urahara e dispositivo referenciado em Mayuri. Há respaldo em WORLD-S04/S05 para esses casos; não são uma permissão universal para qualquer personagem abrir qualquer passagem.

A ficha deve diferenciar abertura natural Hollow/Arrancar, abertura por técnica/conhecimento e abertura por dispositivo. O requisito de cada via não é decidido apenas pela origem; outras origens precisam de acesso compatível demonstrado ou de adaptação identificada. Ensino por Urahara, obtenção de um dispositivo de Mayuri e exigência de filiação não foram aprovados automaticamente. Fontes/era: M08/M13; skills: M06, com compra SP; dispositivo: M11; destinos e travessia: M10; execução/persistência: M16.

Não atribuir à técnica um número de Kidō, nome definitivo, componentes ou domínio instantâneo sem evidência. A animação/encantamento de apresentação e o modo de comando serão tratados na ficha; não foi escolhido exigir digitação ou recitação pelo jogador.

### 8.7. Destinos das skills básicas — M10-03A REVISADO POR 03D

**Aprovação original de 23/10/2026:** “Chegada em pontos estabelecidos (recomendado)”. A restrição das skills próprias ao catálogo de entradas do mod foi substituída pela aprovação posterior de 03D, em 8.12. A pergunta e a resposta originais permanecem em 12.6 e no histórico. Rotas/serviços iniciais seguem 02A; a revisão permite destinos próprios conhecidos e acessíveis, preservando validação, acesso e limites da técnica.

### 8.8. Busca de chegada próxima — M10-03B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Buscar posição próxima fora da área”. Segunda opção escolhida, diferente da recomendação inicial. Se não houver posição válida na área de chegada prevista, buscar automaticamente uma posição próxima fora dela. A alternativa de recusar imediatamente por sair da área não foi escolhida. A busca de espaço não escolhe por si só um novo destino. 03D revisou a restrição de 03A: a busca próxima se aplica também ao local próprio conhecido e acessível escolhido pelo jogador.

A validação anterior à travessia e a confirmação antes de transferir integram o enunciado da escolha; o comportamento em caso de bloqueio passa a ser a busca próxima. A posição encontrada também precisa ser válida para o corpo efetivo de chegada, respeitando o encerramento de formas temporárias na troca de dimensão de M06/M07. A fonte da viagem não cria exceção de acesso, colisão ou remoção de blocos.

Alcance, ordem de procura, apoio/perigos, verticalidade, permissões, carregamento, timeout e concorrência exigem ficha. 03C decidiu permanecer na origem se a busca próxima não encontrar posição válida; não há busca ilimitada ou troca automática para outro destino. Estar inicialmente descarregada não equivale por si só a região inválida; M16 precisa resolver sua disponibilidade para validação. 07C define custo apenas ao formar a passagem, sem reembolso por não usá-la depois; custos de serviços/dispositivos e tratamento de falhas de infraestrutura ainda exigem ficha. Imunidade/queda não são concedidas por buscar espaço.

### 8.9. Limites e falha da busca — M10-03C DECIDIDO

**Aprovado em 23/10/2026 após esclarecimento. Resposta:** “Sem espaço próximo, permanecer na origem (recomendado)”. Procurar nas proximidades da referência de chegada, respeitando corpo, terreno, barreiras e acesso. Sem posição válida, recusar a viagem, manter o jogador na origem e avisar. Não tentar automaticamente outra entrada. O exemplo da Seireitei não aprova um portão concreto nem limita os destinos próprios aprovados posteriormente em 03D.

O limite em blocos, ordem de busca, ocupação/perigos e carregamento exigem ficha M10/M16. 03C não definiu pagamento/reembolso por si só; a decisão posterior 07C resolve o custo de formação das skills comuns, sem taxa por uma travessia recusada aprovada aqui. 03B/C resolvem espaço e falha; não exigem que todo destino seja um ponto criado pelo mod.

### 8.10. Meio de retorno em exploração comum — M10-04A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Retorno viável nas viagens comuns (recomendado)”. Viagens comuns de exploração devem ter meio de retorno viável por passagem, serviço ou capacidade já disponível, sem exigir aprendizado novo para sair. O retorno não precisa ser o mesmo portal mantido aberto, um comando de resgate ou teleporte gratuito. Viagens sem retorno imediato ficam restritas a situações especiais informadas antes de começar. A alternativa de descobrir somente no destino como voltar não foi escolhida.

A decisão não elimina distância, risco de exploração, combate ou requisitos já válidos. Viável não significa retorno imediato em combate, restauração da forma encerrada ou imunidade a perder um item. A ficha precisa contemplar corpos permitidos, fontes por era, perda de recurso/dispositivo e bloqueio de rota; recuperação de contingência ainda não foi escolhida. Nenhuma viagem especial, prisão, portal permanente ou método de retorno específico é aprovado por essa escolha.

### 8.11. Nova direção sobre destinos — revisão de M10-03A

**Manifestação posterior do usuário, 23/10/2026:** “A ideia é ser a mesma do nether do minecraft por exemplo e a mesma da obra pelo o que entendi, já que também não parece ter um lugar fixo para chegar pelo senkaimon ou garganta”. Isso reabre a restrição de 03A a entradas estabelecidas pelo mod. A recomendação anterior de várias entradas cadastradas não resolve inteiramente a intenção apresentada. A sequência foi resolvida pela aprovação de 03D em 8.12: seleção de local conhecido e acessível. Preservar a aprovação original como histórico; a analogia não aprovou uma fórmula espacial.

Separar duas experiências: correspondência geográfica entre origem/destino, como referência do Nether, e escolha deliberada de um local no outro mundo. Não são equivalentes. WORLD-S04/S06 sustentam conexões/casos de chegada e limites de precisão; não demonstram conversão geográfica universal entre os mundos de Bleach ou escolha irrestrita de qualquer coordenada por qualquer usuário. A comparação com o Nether não aprova escala numérica, reaproveitamento de portal, geração de moldura, remoção de blocos ou travessia do Dangai como mecânica pronta.

A decisão de segurança 03C permanece: sem espaço próximo válido, ficar na origem. 02B mantém passagens comuns fora de combate; 04A mantém retorno viável. A revisão da seleção do destino não revisa esses contratos.

### 8.12. Escolha da chegada nas skills próprias — M10-03D DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Escolher local conhecido e acessível (recomendado)”. Em revisão explícita de 03A, skills próprias podem escolher um local conhecido e acessível no outro mundo, inclusive local do jogador; deixam de depender exclusivamente do catálogo de entradas criado pelo mod. Serviços iniciais podem continuar com suas rotas estabelecidas de 02A. A escolha favorece viagens deliberadas entre bases, lugares de treino e locais narrativos, sem uma relação matemática obrigatória entre mapas. Não se apresenta conhecer/registrar um local como exigência universal comprovada da obra.

**Alternativa não escolhida:** a posição onde a passagem é aberta determina uma região correspondente no outro mundo, seguindo a ideia espacial do Nether. Favorece exploração geográfica e redes de passagem; exige definir correspondência e escala entre os mapas, sem permitir escolher qualquer destino ao abrir do mesmo ponto. Nenhuma escala ou método já foi aprovado.

Preservar 03B/C: validar chegada para o corpo efetivo, buscar perto da referência quando necessário e manter na origem sem solução. Conhecimento/registro foi decidido em 03E/8.13; parâmetros, permissões, barreiras espirituais, fontes/eras e eventual precisão diferenciada por técnica ainda exigem ficha. Não impor visita prévia a todas as primeiras viagens, criar uma exigência circular ou conferir acesso institucional por conhecer o lugar. Pergunta em 12.10 e [histórico da revisão](historico/2026-10-23-revisao-m10-03a-e-proposta-03d-destinos.md).

### 8.13. Conhecimento e registro de destinos próprios — M10-03E DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Visitar e registrar manualmente (recomendado)”. Para criar uma referência própria de chegada, visitar o local e registrá-lo deliberadamente com um nome; não exigir construir um portal permanente no destino. Serviços/fontes apropriadas de 02A continuam permitindo primeiras viagens a lugares ainda desconhecidos. Assim, conhecer um novo mundo não depende de já ter estado nele. Alternativa não escolhida: gerar registros automaticamente durante a exploração. A visita por si só não registra cada lugar como destino próprio.

O registro manual organiza a escolha pessoal e não concede acesso, propriedade do terreno, proteção de construção ou aprendizado de skill. Usar um ponto continua dependendo de acesso válido e da capacidade de viagem. 03E complementa 03D quanto à criação de referência pessoal; não reabre a seleção de destinos próprios. Não exigir aparelho, receita, taxa ou consumível sem ficha M11; não conceder compartilhamento de pontos da party por omissão.

Identificador, atualização/remoção, quantidade, tratamento de referências antigas e UI dependem de M10/M14/M16; acesso/servidor de M15. A chegada é validada no uso, preservando 03B/C. A pergunta não escolhe custo, preparo, duração de portal ou passageiros. [Pergunta/opções](historico/2026-10-23-proposta-m10-03e-registro-de-destinos.md); 12.11 preserva a pergunta e a resposta.

### 8.14. Funções dos lugares — herança e M10-05A DECIDIDO

Os papéis já aprovados em M04/M08/M09 orientam o uso do espaço; esta organização não reabre currículos, carreiras, hostilidade, progressão ou funções dos canônicos. Função documental não fixa ainda coordenadas, tamanho, construções ou sequência de produção.

| Lugar/contexto | Necessidade herdada | Conteúdo espacial ainda aberto |
|---|---|---|
| Mundo Humano/Overworld | Nascimento das quatro origens, fontes iniciais pertinentes, atuação de campo e rede Urahara conforme contexto | Local de cada fonte, relação entre núcleo urbano e terreno explorável, áreas de treino/ameaças e serviços. Não reservar o mundo a uma origem ou identificar o mapa instalado como Karakura já aceita |
| Soul Society — formação e instituições | Percurso viável à Academia, carreira/divisões e fontes especializadas de M08 | Distribuição/localização dos atendimentos e acesso por era; não assegurar que Seireitei esteja sempre aberta ou que toda fonte necessária esteja dentro dela |
| Hueco Mundo — sobrevivência e relações | Caça/progressão e fontes independentes; Nel, Harribel e Grimmjow com papéis aprovados; Las Noches com contexto próprio de autoridade/conflito | Regiões de caça, lugares de cooperação, continuidade dos serviços e percurso até o núcleo. Não tornar Las Noches uma cidade neutra universal nem ingresso obrigatório para evoluir |

**M10-05A DECIDIDO — Soul Society, 23/10/2026. Resposta:** “Rukongai com atividades próprias (recomendado)”. Além dos espaços institucionais, dar ao Rukongai função de região jogável própria, com povoados, atividades locais e exploração. A Seireitei concentra formação/instituições e serviços pertinentes, com disponibilidade/acesso por contexto. As fontes WORLD-S01/S02/S06 identificam lugares e casos narrativos; a direção de atividades próprias foi aprovada para o mod; o catálogo concreto continua por especificar, sem declará-lo uma reprodução integral de atividades canônicas.

A escolha cria motivos para permanecer e retornar ao Rukongai; não exige completar suas atividades antes da Academia, não move por aprovação toda formação para dentro da Seireitei e não acrescenta uma nova filiação racial. A alternativa não escolhida concentra o Rukongai principalmente no papel de cenário/percurso até a Seireitei, com menor escopo de atividades próprias. M10-01A/B continua regendo geração mista e proteção pequena/sinalizada; esta proposta não protege toda cidade ou libera qualquer intervenção.

Povoados, atividades, elenco, recompensas, recursos e perigos concretos continuam nas fichas M08/M09/M10/M11/M12/M13. A dimensão conserva os perfis de força de M09, sem adaptação automática ao jogador. Pergunta e resposta em 12.12 e no [histórico](historico/2026-10-23-proposta-m10-05a-funcoes-rukongai-seireitei.md).

### 8.15. Distribuição de apoio em Hueco Mundo — M10-05B DECIDIDO

**Herança:** M08-10A/B já aprovou funções de fontes Hollow independentes, orientação local de Nel, compromissos de Harribel, rivalidade de Grimmjow e o contexto da estrutura de Aizen/Las Noches. M07 permite a trajetória racial sem servir a Aizen; M10 não submete novamente essa autonomia a votação. A questão nova é a distribuição espacial do apoio independente.

**Aprovado em 23/10/2026. Resposta:** “Pequenos refúgios distribuídos (recomendado)”. Pequenos refúgios em lugares diferentes, ligados a fontes ou grupos com motivo para cooperar. Cada lugar oferece uma função pertinente — por exemplo, orientação de sobrevivência, encaminhamento para treino ou preparação de uma atividade local — com disponibilidade por contexto/era. Esses exemplos ainda não escolhem um serviço, currículo ou nova facção. A distribuição de refúgios é adaptação aprovada do mod, sem afirmar que a obra apresenta uma rede equivalente pronta.

A distribuição favorece percursos e descoberta em regiões distintas, em vez de concentrar todos os atendimentos num só lugar. **Alternativa não escolhida:** um povoado independente principal concentra esses contatos, facilitando orientação e retorno, mas centralizando os deslocamentos de apoio. Ambas preservam fontes essenciais e o contexto de Las Noches; nenhuma converte a fortaleza em cidade neutra para todas as origens/eras.

Cada lugar precisa de ficha de localização, função, interlocutores, acesso/era, circulação dos corpos e ameaças próximas. Não presumir cura gratuita, imunidade territorial, todos os serviços em cada refúgio ou entrega de técnicas ao visitar. Proteção pequena/sinalizada segue 01B onde houver trechos essenciais declarados; proteção do prestador segue M08, sem proteger toda a região. Regiões de caça e recursos continuam com M09/M10/M11; apoios não fornecem presas derrotadas infinitas. Quantidade, nomes, construções, distâncias e ordem de produção permanecem pendentes.

Pergunta e resposta em 12.13 e no [histórico](historico/2026-10-23-proposta-m10-05b-apoio-em-hueco-mundo.md). Não há mudança nos mapas instalados nem novo assentamento já implementado.

### 8.16. Identidade do núcleo urbano humano — M10-05C DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Karakura adaptada ao Minecraft (recomendado)”. Karakura adaptada ao Minecraft como referência principal do núcleo urbano humano, com bairros/lugares reconhecíveis ligados a atividades e contatos. WORLD-S07 sustenta sua função narrativa, sem definir um mapa completo. A identidade espacial foi aprovada como adaptação do mod; o recorte concreto continua por ficha e não exige reprodução literal de cada prédio ou escala real.

O núcleo integra o Overworld explorável/construível aprovado em 01A, onde nascem as quatro origens conforme M04. Não limita a residência ou a base do jogador à cidade, não torna todo jogador colega de Ichigo nem exige passagem diária por escola/loja. Introduções, fontes e serviços de M08 mantêm contexto próprio; seus locais exatos ainda exigem ficha. Não concentrar automaticamente toda atividade ou todo NPC do Mundo Humano no mesmo bairro.

**Alternativa não escolhida:** uma cidade original com locais canônicos inseridos, oferecendo mais liberdade de composição, mas uma identidade urbana menos diretamente associada a Karakura. A escolha não exclui outras cidades/locais futuros do Mundo Humano e não escolhe a ordem de produção. O mapa urbano instalado continua ponto de partida a avaliar, sem aprovação visual, fidelidade ou compatibilidade espacial presumidas.

Bairros, percursos, bordas com terreno gerado, circulação de corpos grandes, distribuição de ameaças e adaptação dos saves existentes precisam de ficha M10/M16. Serviços/era dependem de M08/M13; atividades de M12; materiais de M11; proteção localizada conserva 01B, sem proteger a cidade inteira. A aprovação da identidade não aprova individualmente prédio, quest, asset ou mapa. Pergunta e resposta em 12.14 e no [histórico](historico/2026-10-23-proposta-m10-05c-nucleo-urbano-humano.md).

### 8.17. Recursos e permanência nas dimensões espirituais — M10-06A DECIDIDO

**Herança:** M01 preserva exploração, coleta, preparação e construção fora da campanha; 01A combina núcleos estáveis e arredores gerados com identidade própria. Isso não decidiu o abastecimento de bases em cada dimensão. M04/H2 rejeita uma segunda barra obrigatória de fome Hollow; M05 mantém os recursos de combate, e 06A não revisa alimentação, regeneração ou consumo racial.

**Aprovado em 23/10/2026. Resposta:** “Base viável com abastecimento local (recomendado)”. Soul Society e Hueco Mundo devem oferecer meios locais para sustentar a rotina básica de uma base, por materiais e abastecimento adequados ao mundo, sem exigir voltar sempre ao Mundo Humano apenas para continuar vivendo ali. Fontes concretas podem ser coleta, produção compatível ou troca/serviço contextual, ainda por especificar. Não é aprovação de uma plantação, vendedor, receita ou recurso novo específico. A direção envolve planejamento e acesso às fontes; não significa recursos gratuitos, abundância igual ou atendimento em todo refúgio.

Manter identidades e complementaridade: não replicar obrigatoriamente todos os biomas, minérios, animais e cultivos do Overworld em cada dimensão. Recursos especiais, condições locais e trajetos podem continuar motivando exploração/comércio/viagens. A direção aprovada não escolhe exclusividades ou bloqueios concretos de itens, nem uma equivalência de Reishi com madeira/pedra/comida. O ambiente de Hueco Mundo continua distinto sem inventar aqui uma fisiologia ou uma regra de agricultura canônica.

**Alternativa não escolhida:** bases espirituais dependem principalmente de abastecimento trazido do Mundo Humano, enfatizando expedições e logística entre dimensões. Aumenta o papel do transporte/importação, mas restringe a permanência independente. Ambas preservam construção autorizada e retorno viável de 04A; não mudar a disponibilidade de viagem para resolver a economia por omissão.

M10 define onde se obtêm recursos e a viabilidade espacial; M11 define materiais, obtenção/produção, receitas, equipamentos e economia; M05/M06 mantêm Vida/Stamina/Energia e interações com comida/regeneração; M15/M16 tratam oferta em servidor, renovação, exploração de chunks e preservação do save. Relação com blocos/itens vanilla e de outros mods precisa ser especificada antes de implementar, sem converter/apagar inventários. Pergunta e resposta em 12.15 e no [histórico](historico/2026-10-23-proposta-m10-06a-recursos-e-bases.md). É adaptação de gameplay aprovada, sem nova afirmação canônica ou pesquisa de API nesta rodada.

### 8.18. Experiência das travessias comuns — M10-07A DECIDIDO

**Questão resolvida:** 07A define o ritmo da viagem comum, preservando abertura, acesso, destino e retorno de 02A/B/C, 03B/C/D/E e 04A.

**Aprovado em 23/10/2026. Resposta:** “Transição breve; percursos em situações próprias (recomendado)”. Nas viagens comuns de Senkaimon/Garganta, apresentação breve da passagem e chegada ao destino validado, sem corredor jogável obrigatório a cada uso. A intenção é manter o acesso frequente a bases, fontes e regiões sem repetir um trajeto sem objetivo. Isso preserva abertura, requisitos, custo e preparo: 07C/D já definem o perfil comum; valores, fases específicas e requisitos de cada via continuam por ficha. A transição não é uma nova técnica universal de teleporte instantâneo.

Trechos jogáveis de Dangai/Garganta permanecem como conteúdo próprio de rotas/situações, com objetivo, entrada/saída, riscos, interrupção e recuperação definidos em M09/M10/M12/M13/M16. Não aprovar por associação Kōtotsu, treino temporal, controle de Reishi obrigatório ou uma instância particular. **Alternativa não escolhida:** percurso jogável também nas viagens comuns, reforçando a presença espacial da travessia, com custo de tempo recorrente e necessidade de variar ou justificar a repetição.

A escolha compara experiência comum e conteúdo especial; não define uma implementação de carregamento, quantidade de dimensões, duração numérica ou câmera bloqueada. Não concede invulnerabilidade, pausa do servidor ou limpeza extra de gastos/tempos. Mudança de dimensão segue M06/M07; o fluxo precisa evitar cobrança/execução duplicadas por etapas técnicas intermediárias. M15/M16 ainda precisam resolver concorrência, falha/desconexão e recuperação antes da implementação.

02B continua exigindo fora de combate para passagens comuns, qualquer que seja a fonte. Percurso especial não vira fuga em combate por essa aprovação; sua classificação/condições exigem ficha. 03B/C ainda validam chegada, buscam perto e preservam origem sem solução; 04A mantém retorno viável. Pergunta e resposta em 12.16 e no [histórico](historico/2026-10-23-proposta-m10-07a-experiencia-de-travessia.md). Adaptação aprovada, sem nova afirmação canônica/API ou mudança de runtime.

### 8.19. Convidados nas passagens próprias — M10-07B DECIDIDO

**Aprovado com restrição própria em 23/10/2026. Resposta:** “Compartilhar com convidados da mesma party”. Quem abre uma passagem própria compatível pode autorizar convidados da mesma party; não generalizar a convidados externos ao grupo. A autorização não se estende automaticamente a todos os membros por pertencerem à party. Cada pessoa decide atravessar; abrir não transporta automaticamente uma party nem força outro jogador. Passageiro não precisa ter aprendido a abertura ou registrado previamente o destino do trajeto, mas continua sujeito ao seu acesso individual, à era e às condições da passagem. Isso permite cooperação e uma primeira visita acompanhada sem transferir a skill do abridor.

Ser da mesma party é condição adicional de compartilhamento, conferida na travessia; mudanças de grupo durante uma transferência em andamento exigem contrato M15/M16, sem teletransportar à força quem já chegou. A seleção do destino usa as condições válidas de quem abre conforme 03D/E; acesso de um personagem não se estende por convite aos demais. Conhecer alguém, entrar numa party ou receber convite não concede credencial institucional, marco de campanha ou elegibilidade de outra origem. 02B mantém cada viajante fora de combate nas passagens comuns; 03B/C validam chegada para o corpo efetivo de cada pessoa, com busca próxima e recusa individual sem solução. Não inventar um cancelamento coletivo automático da passagem por uma pessoa recusada.

Chegar como convidado não registra automaticamente um destino próprio: o registro manual de 03E permanece. 04A exige retorno viável nas viagens comuns também para quem não sabe abrir; não considerar a capacidade do anfitrião um meio garantido de retorno se ele já não estiver disponível. Contingências e serviços concretos exigem ficha, sem aprovar resgate instantâneo.

**Alternativa não escolhida:** passagem própria de uso pessoal por padrão, com transporte coletivo dependente de serviço ou capacidade específica futura. Simplifica a autorização inicial, mas restringe o uso cooperativo da habilidade básica. 07C–G complementam custo, manutenção, ciclo de vida, limite e sentido da passagem comum. Quantidade de convidados, valores de custo/duração e método/alcance do convite continuam por ficha M10/M14/M15/M16. Animais, pets, NPCs, montarias, veículos, itens soltos e projéteis não são passageiros aprovados por omissão nesta pergunta sobre pessoas jogadoras.

Pergunta e resposta em 12.17 e no [histórico](historico/2026-10-23-proposta-m10-07b-convidados-nas-passagens.md). É adaptação aprovada com restrição de party, sem nova afirmação canônica/API, envio real de convites ou mudança de runtime.

### 8.20. Custo e janela das skills comuns de abertura — M10-07C DECIDIDO

**Escopo:** skills comuns aprendidas para abrir Senkaimon/Garganta. Compra da skill mantém SP conforme REV-SP-02; 07C trata do uso com Energia Espiritual, não uma nova compra por passagem. Serviços, dispositivos e rotas especiais mantêm ficha própria; não herdam automaticamente o mesmo débito ou duração.

**Aprovado em 23/10/2026. Resposta:** “Custo ao abrir + janela sem manutenção (recomendado)”. Cobrar Energia uma vez quando a formação da passagem concluir e disponibilizá-la por uma janela curta sem manutenção contínua. Cancelar antes da conclusão não cobra a abertura. Depois de formada, não utilizar a passagem não devolve esse custo. Quem abre pode atravessar sem fechar só por esse ato a janela disponível aos convidados da mesma party, desde que as demais condições continuem válidas. Nova abertura concluída é um novo uso; não existe portal permanente gratuito por essa aprovação.

Valores de Energia e duração variam conforme ficha/calibração, sem escolher blocos, segundos ou ticks agora. A janela deve permitir o uso cooperativo previsto sem criar uma conexão indefinida; número de usuários e falhas individuais exigem ficha M10/M15/M16. Essa aprovação não inclui tarifa por passageiro, débito de SP de uso, recurso especial novo ou Stamina adicional. **Alternativa não escolhida:** consumo contínuo de Energia enquanto a passagem permanecer aberta, vinculando disponibilidade à sustentação e exigindo resolver como essa sustentação acompanha o abridor.

07D definiu preparo e cancelamento; 07E definiu a autonomia da passagem formada em morte/desconexão e seu encerramento na descarga/reinício. Perda de instrumento exigido, combate posterior, transferências em andamento e parâmetros ainda exigem ficha. 07F/G definiram fechamento manual, uma passagem ativa por personagem e ida/volta durante a janela. Não usar a janela para ignorar 02B, acesso individual, validação por corpo ou retorno viável.

Pergunta e resposta em 12.18 e no [histórico](historico/2026-10-23-proposta-m10-07c-custo-e-janela-de-abertura.md). Adaptação aprovada, sem nova afirmação canônica/API ou mudança de runtime.

### 8.21. Preparo das skills comuns de abertura — M10-07D DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Preparo breve parado e cancelável (recomendado)”. Preparo breve parado, com direção do olhar livre. Movimento voluntário ou início de ação disponível cancela o preparo, sem débito da abertura nem recuperação adicional só por cancelar. Pedido de ação indisponível é recusado e preserva o preparo ainda válido; não contorna custos ou fases anteriores. Entrada no Combat State impede concluir a abertura comum, herdando 02B, sem nova votação da restrição de combate. 07C cobra apenas na formação concluída; nenhum custo deve ser debitado duas vezes por apresentação/carregamento.

O perfil aprovado aplica à classe das skills comuns, sem repetir pergunta por Senkaimon/Garganta ou origem. Não aprova abertura junto a ataque, consumo de item ou outro preparo incompatível. **Alternativa não escolhida:** formação imediata após validar requisitos, sem intervalo próprio de preparo, ainda com custo e demais condições. O preparo dá uma ação perceptível e uma oportunidade de desistir; a alternativa prioriza rapidez de uso fora de combate.

Duração, comando/interface, interrupções por controle/dano, deslocamento imposto, espaço de abertura, chão/ar/água e eventuais instrumentos exigem ficha. Essa aprovação não concede apoio/voo, imunidade ou passagem através de blocos. Não atribuir ao dispositivo de M11 ou aos serviços esse perfil automaticamente. Pergunta e resposta em 12.19 e no [histórico](historico/2026-10-23-propostas-m10-07de-preparo-e-ciclo-da-passagem.md).

### 8.22. Passagem formada e ciclo de vida — M10-07E DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Cumprir a janela já paga (recomendado)”. Depois de formada e paga, a passagem cumpre a janela restante mesmo com morte/desconexão do abridor, desde que entrada e chegada continuem em regiões ativamente simuladas e as demais condições permaneçam válidas. Não se exige sustentação de Energia. A travessia normal do abridor já preserva essa janela por 07C; não é novamente submetida a aprovação. Autorizações, party e acesso individual continuam sujeitos a validação; não congelar uma autorização para ex-integrantes por interpretar autonomia como permissão permanente.

Descarregar uma das regiões necessárias ou reiniciar encerra a passagem efêmera, sem reaparecer ao carregar/entrar depois. A aprovação não exige manter chunks carregados indefinidamente, restaurar uma janela em outro local ou devolver o gasto. A nova abertura segue o uso normal de 07C. **Alternativa não escolhida:** também encerrar a passagem quando seu abridor morrer/desconectar, tornando o uso da party dependente da continuidade dele.

A autonomia aprovada pertence ao objeto de passagem já formado, não a um preparo, forma temporária ou estado pessoal restaurado após logout/morte. Os contratos de limpeza de M06/M07 permanecem para o personagem; 07E define explicitamente o caso do objeto externo. Preparo incompleto não se torna passagem paga/autônoma por essa regra. Serviços/portais permanentes/dispositivos ainda exigem ficha própria.

M15/M16 precisam definir o ponto de confirmação da travessia, validações e encerramento simultâneo: impedir novas entradas quando a passagem terminar e concluir ou recusar transferências conforme o estado válido, sem prender o jogador em apresentação intermediária nem duplicá-lo. Não garantir chegada a área inválida; 03B/C e 04A preservam cobertura. Perda de instrumento específico e combate posterior do abridor ainda exigem ficha. Encerramento manual, limite de aberturas e sentido de retorno foram definidos em 07F/G. Pergunta e resposta em 12.20 e no [histórico](historico/2026-10-23-propostas-m10-07de-preparo-e-ciclo-da-passagem.md). 07D/E são adaptações aprovadas de gameplay, sem nova afirmação canônica/API.

### 8.23. Limite e fechamento manual — M10-07F DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Uma passagem ativa, com fechamento manual (recomendado)”. Uma passagem própria comum ativa por personagem, com comando de fechamento manual e sem reembolso. Enquanto existir uma ativa, novo pedido de abertura é recusado; o personagem precisa encerrá-la ou aguardar o término. Evita criar uma rede simultânea com a mesma skill básica e dá controle explícito sobre a interrupção do acesso da party, em vez de substituir a anterior automaticamente. A classe inclui as skills comuns cobertas por 07C/D/E; não permitir uma por tipo só porque os nomes diferem.

**Alternativa não escolhida:** várias passagens simultâneas, com limite e custos próprios a calibrar; amplia a organização de rotas e exige gestão de autorizações, fechamento e recursos. Uma passagem envolve suas duas pontas, não duas vagas distintas. Serviços, portais permanentes e dispositivos continuam com ficha própria; não são desativados silenciosamente pela contagem pessoal aprovada para skills comuns.

Fechamento manual exige comando válido de quem abriu e fase compatível, sem ignorar a resolução de travessias em andamento de 07E/M15/M16. Interface, alcance do comando, tratamento de indisponibilidade e feedback à party exigem ficha; não presumir reembolso, retorno forçado ou retirada de quem já chegou. A regra de término por prazo/descarga/reinício de 07E permanece. Pergunta e resposta em 12.21 e no [histórico](historico/2026-10-23-propostas-m10-07fg-limite-fechamento-e-sentido.md).

### 8.24. Sentido da passagem — M10-07G DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Ida e volta enquanto aberta (recomendado)”. As duas pontas da passagem comum permitem ida e volta ao abridor e aos convidados autorizados da mesma party durante a janela já paga, sem nova abertura a cada travessia. Transitar não renova o prazo, não muda os pontos conectados nem concede um novo registro pessoal. O retorno também valida party/autorização, acesso, Combat State e espaço para o corpo efetivo; transformação/evolução ou alteração do terreno podem exigir a busca próxima já aprovada em 03B/C.

**Alternativa não escolhida:** uma abertura permite somente ida; retornar depende de nova abertura ou serviço. Exige planejar retornos separados inclusive para quem não domina a skill. Em ambas, 04A garante meio de retorno viável nas viagens comuns, sem obrigar manter este portal aberto indefinidamente. Não garantir retorno por uma passagem já encerrada, chegada inválida, imunidade ou nova reserva de Energia.

A regra bidirecional aprovada é de funcionamento do mod para skills comuns; não descreve capacidade universal de toda passagem da obra, serviço ou dispositivo. Posições exatas das pontas, espaço de formação, entradas simultâneas e confirmação da travessia dependem de ficha M10/M15/M16. O custo aprovado em 07C é de abertura; a pergunta não cria tarifa por passagem individual. Pergunta e resposta em 12.22 e no [histórico](historico/2026-10-23-propostas-m10-07fg-limite-fechamento-e-sentido.md). Sem nova afirmação ou pesquisa canônica/API nesta rodada.

### 8.25. Consolidação das passagens — revisão dirigida em 23/10/2026

**Resultado:** contratos comuns de 02A/B/C, 03B/C/D/E, 04A e 07A–G coerentes no recorte revisto; o bloco das skills comuns está consolidado conceitualmente, com fichas executáveis ainda necessárias. M10 permanece `EM ESPECIFICAÇÃO`. [Relatório de revisão e entregas](10a-revisao-m10-2026-10-23.md).

| Etapa | Regra vigente | Complemento necessário |
|---|---|---|
| Conhecer/escolher | Local próprio visitado e registrado manualmente; primeira viagem por via contextual ou acompanhada válida | Catálogo/fontes por era, UI/registro e acesso |
| Preparar | Uma passagem ativa por personagem; fora de combate; preparo parado/cancelável | Geometria da abertura, meios, instrumentos e fases específicas |
| Formar | Energia uma vez ao concluir; janela curta sem manutenção | Valores, temporizador e validação/carregamento inicial |
| Viajar | Abridor/convidados da mesma party; adesão voluntária; validar acesso, combate e corpo por pessoa | Confirmação/concorrência, alteração de party e apresentação breve |
| Resolver chegada | Buscar perto se necessário; sem solução, permanecer na origem da travessia atual | Limites de busca, terreno/perigos e transação com limpeza de formas |
| Retornar/encerrar | Ida/volta sem renovar prazo; manual/expiração/descarga/reinício; morte/logout do abridor não encerra só por si | Retorno viável após fechamento, comando e transferências em andamento |

**Conciliações:** região inicialmente descarregada pode precisar ser preparada para uma primeira viagem; 07E encerra uma passagem já formada se perder simulação, sem proibir todo destino inicialmente descarregado. M06/M07 limpam manifestações pessoais na mudança de dimensão efetiva, enquanto 07E governa o objeto externo. Corpo de chegada precisa considerar esse retorno ao estado-base; travessia recusada não deve ser confundida com uma mudança de dimensão já concluída. A confirmação e ordenação exatas são entregas M15/M16, sem alegar implementação.

Registro e convite não dão propriedade, credencial ou progresso. Ida/volta durante janela não satisfaz sozinha 04A para um convidado sem habilidade de abertura após o portal fechar. O meio concreto de retorno é entrega obrigatória da rota antes de disponibilizá-la, sem aprovar serviço, NPC ou resgate por omissão. Essas conciliações aplicam regras já aprovadas; não são novas opções de produto.

### 8.26. Ambiente e água em Hueco Mundo — M10-08A DECIDIDO

**Referência WORLD-S08:** noite permanente e paisagem de areia branca, sem rios/lagos naturais na geração proposta. **Adaptação adotada conforme a condição da resposta:** permitir colocar e usar normalmente água trazida pelo jogador, compatível com manter uma base conforme 06A. Não implica gerar água natural, conceder um balde inicial ou copiar os biomas do Overworld. **Alternativa não adotada pela evidência disponível:** impedir água estável também quando transportada, exigindo outro desenho de abastecimento e produção. A fonte descreve ausência ambiental, sem estabelecer evaporação/proibição de água importada. Revisar se houver evidência primária explícita em contrário; não alegar prova universal de inexistência dessa regra.

Noite permanente descreve a iluminação/céu, sem congelar ticks, cooldowns ou prazos. Renascimento/retorno seguem o contrato consolidado em 8.30; descanso em Hueco Mundo está aprovado em 08E, sem restrição de horário e sem criar amanhecer. Clima, agricultura, geração de fontes renováveis, iluminação de áreas construídas e surgimento de mobs continuam por ficha M10/M11/M15/M16. O perfil exterior não decide iluminação interna especial de Las Noches. Referência visual não concede visão noturna, respiração, voo ou dano ambiental universal, nem altera todo mob vanilla automaticamente.

A aprovação de 06A exige fontes locais viáveis; 08A trata da água transportada e não aprova importação obrigatória recorrente para sobreviver. Fontes concretas, obtenção e compatibilidade com itens/condições raciais ainda dependem de M11/M05/M06. Pergunta/resposta em 12.23 e [histórico](historico/2026-10-23-proposta-m10-08a-ambiente-e-agua-hueco-mundo.md); decisão condicionada à pesquisa, aplicada em 23/10/2026.

### 8.27. Local de renascimento e corpos compatíveis — M10-08B DECIDIDO

Aprovado em 23/10/2026: permitir escolher ponto de renascimento no Mundo Humano, na Soul Society ou em Hueco Mundo, por cama quando o corpo permitir, ou abrigo apropriado a corpos permanentes grandes. É adaptação de gameplay, apoiada na viabilidade de bases de 06A e na derrota de gameplay de M04; não é ressurreição canônica nem muda o nascimento inicial no Overworld.

**Em termos de jogo:** para corpos compatíveis, a cama é o ponto pessoal de renascimento. Interagir com uma cama válida marca o local onde o personagem voltará após morrer. Exemplo: o jogador marca uma cama na Soul Society, viaja ao Mundo Humano e morre; se a cama continuar válida e acessível, renasce junto dela na Soul Society. Não há um segundo cadastro obrigatório num NPC ou uma seleção de coordenadas além da interação com o objeto. Selecionar/registrar destino de portal em 03E é outra ação, com finalidade de viagem.

Para um Gillian, a alternativa de abrigo representa a mesma função de marcar um local de retorno adequado ao corpo. O objeto/interação e os requisitos concretos ainda não foram definidos; construir um cômodo qualquer não o torna spawn automaticamente.

Escolha exige acesso e espaço válidos para o corpo-base efetivo após encerramento de formas temporárias. Não reduzir Gillian automaticamente para caber numa cama humana. O abrigo é a alternativa funcional aprovada para corpos grandes; dimensões, construção/serviço e método de vincular o ponto exigem ficha. O ponto pertence ao progresso pessoal e sua validade é conferida pelo servidor; quantidade de registros e interface continuam por especificar. Local de renascimento não decide penalidade de morte, reposição de itens, recursos ou cura. Aprendizados/evoluções e encerramento de estados seguem contratos já aprovados.

Alternativa não adotada: concentrar todo renascimento no Mundo Humano, com viagem para retomar a base espiritual. Perda/bloqueio do ponto segue a sequência aprovada em 08C; ausência de seleção e reconexão foram aprovadas em 08D. O primeiro spawn continua no Mundo Humano conforme M04, exigindo espaço válido para o corpo; o renascimento não pode simplesmente ser recusado como uma viagem voluntária. Descanso a qualquer horário em Hueco Mundo foi aprovado em 08E; demais perfis têm ficha própria; relógios de combate mantêm os contratos anteriores. 12.24 e histórico conservam pergunta/opções/resposta aprovada. A recuperação por perda/bloqueio do ponto foi aprovada separadamente em 08C.

### 8.28. Recuperação quando o ponto de renascimento falha — M10-08C DECIDIDO

Aprovada em 23/10/2026 a sequência abaixo para um ponto pessoal escolhido em 08B que não possa ser usado:

1. Se a referência ainda existe e o acesso é válido, buscar posição segura nas proximidades, com limite e critérios próprios de renascimento. Não usar a posição antiga de uma cama/abrigo destruído como se o vínculo ainda fosse válido.
2. Se a referência foi perdida ou a busca não encontrar espaço, usar área de recuperação previamente preparada no mesmo mundo, se houver acesso permitido.
3. Se o mundo/área de recuperação não estiver disponível ou o acesso não for permitido, usar a área de recuperação prevista no Mundo Humano.

Todas as alternativas devem comportar o corpo-base efetivo após a limpeza de formas temporárias, inclusive um Gillian permanente, com apoio/terreno e riscos imediatos avaliados. Não reduzir estágio, escavar construções, atravessar barreiras ou restaurar uma transformação para fazer caber. O jogador recebe aviso do local usado e do motivo da substituição. Retorno ao jogo não exige aprender uma skill de viagem nem depender de outro jogador.

A área de recuperação é infraestrutura de acesso essencial, com proteção localizada conforme 01B e espaço apropriado antes da entrega da origem/estágio. M10/M15/M16 precisam garantir e validar a alternativa final, inclusive em mapas existentes; esta decisão não aprova coordenadas, regeneração de terreno ou fallback em posição sabidamente inválida. Se dados/região não puderem ser preparados, a falha técnica exige tratamento recuperável de carregamento, sem declarar um renascimento concluído em colisão. Não é uma condição normal de gameplay para manter o jogador morto indefinidamente.

Alternativa não adotada: falha do ponto pessoal leva diretamente à recuperação no Mundo Humano, simplificando infraestrutura nas dimensões espirituais, mas afastando o jogador da base escolhida. Em ambas, demais consequências da morte permanecem conforme seus contratos. Esta pergunta cobre falha de um ponto pessoal já escolhido; primeiro spawn, ausência de qualquer seleção e reconexão em posição bloqueada não ganham teleporte automático por associação. Parâmetros da busca, localização/era das áreas e ordenação do salvamento exigem fichas. É adaptação de produto, sem nova alegação canônica/API. 12.25 e histórico registram pergunta/opções/resposta aprovada. Ampliação para ausência de seleção e reconexão foi aprovada em 08D.

### 8.29. Ausência de ponto pessoal e posição no retorno — M10-08D DECIDIDO

Pacote aprovado em 23/10/2026 para concluir os casos gerais de posicionamento:

- **Nascimento inicial:** manter o Mundo Humano já aprovado em M04; a posição inicial precisa comportar o corpo e ser válida. Isso é requisito de viabilidade, não nova escolha racial/dimensional.
- **Morte sem ponto pessoal escolhido:** usar a área inicial/recuperação segura do Mundo Humano, validada para o corpo-base efetivo. Não escolher automaticamente a dimensão da última morte como nova residência.
- **Reconexão ou retorno após reinício:** conservar posição/dimensão salvas quando válidas, considerando o corpo-base após encerramento dos estados temporários conforme M06/M07. Não reaplicar vitória, cura ou transformações automaticamente.
- **Posição salva bloqueada ou com acesso inválido:** se acesso local permitir, buscar espaço próximo com limite e critérios próprios; sem solução ou sem acesso permitido, usar a recuperação local/Mundo Humano de 08C, sem ultrapassar permissões. Corrigir apenas o impedimento verificado pelo servidor e informar o deslocamento.

Posição válida no ar/água continua possível: queda, ameaça próxima ou presença de inimigos não bastam para pedir realocação de segurança. O ajuste não é comando de teleporte/fuga, não escolhe a base por conveniência e não remove terreno. Preservar estágio permanente, aprendizados, inventário, recursos e tempos legítimos; nenhuma cura/regeneração offline ou reset de consequência é concedido. O tratamento da trajetória/queda e dos danos durante o carregamento deve ser conciliado em M06/M15/M16, sem prometer invulnerabilidade ou apagar queda acumulada.

Alternativa não adotada: todo login retorna ao ponto pessoal de renascimento ou sua recuperação válida, facilitando reencontro com a base, mas interrompendo viagens/exploração entre sessões e permitindo reposicionamento por logout. Não é recomendada para este mundo persistente. A aprovação do pacote não especifica raio, coordenadas, duração de carregamento, limite de pontos guardados ou interface; M10/M14/M15/M16 detalham esses parâmetros e a garantia da alternativa final antes da funcionalidade. 12.26 e histórico conservam pergunta/opções/resposta aprovada; consolidação em 8.30. Nenhuma nova afirmação canônica/API.

### 8.30. Contrato comum de renascimento e retorno — consolidação de 08B/C/D

Consolidação das aprovações existentes, sem nova escolha de produto:

**Vocabulário:** “spawn marcado pela cama” e “ponto pessoal de renascimento” designam o mesmo local. “Área de recuperação” é a alternativa automática de segurança quando as regras abaixo a exigirem, não um serviço obrigatório para marcar cama. Registrar um destino de Senkaimon/Garganta é um sistema de viagem separado.

| Evento | Resultado aprovado | Referência |
|---|---|---|
| Primeiro nascimento | Mundo Humano, posição compatível com corpo e introdução racial | M04; 08D |
| Escolher residência para renascimento | Ponto pessoal nas três dimensões principais; cama compatível ou abrigo para corpo grande | 08B |
| Morte com ponto válido | Renascer no ponto escolhido, sujeito à validação de corpo/acesso | 08B |
| Ponto pessoal bloqueado/perdido | Busca próxima quando referência/acesso válidos; depois recuperação no mesmo mundo; Mundo Humano como alternativa final | 08C |
| Morte sem ponto pessoal escolhido | Área inicial/recuperação segura do Mundo Humano | 08D |
| Reconexão/reinício, posição válida | Conservar posição/dimensão salvas, após limpeza dos estados temporários | 08D; M06/M07 |
| Reconexão/reinício, posição inválida | Corrigir por busca próxima permitida e sequência de recuperação de 08C, informando o motivo | 08D |

Corpo-base efetivo inclui evolução permanente; não se reduz Gillian para fazer caber. Queda/inimigo próximo não ativam resgate. Posição válida no ar/água pode ser mantida; estados/recursos/tempos seguem seus contratos, sem cura offline pela correção. O servidor valida, preservando progresso pessoal e permissões. Recusa de viagem voluntária de 03C é outro evento: não substitui o tratamento de um renascimento obrigatório.

**Condição de entrega:** ficha das áreas finais e abrigos, dimensões corporais, busca, acesso por era, carregamento/transferência, salvamento e integração com mapas/saves existentes; responsáveis M04/M07/M10/M14/M15/M16. Garantia de alternativa final deve existir antes da respectiva origem/estágio. Regra conceitualmente consolidada não é implementação nem aceite. Não repetir essas aprovações por raça, técnica ou região; apenas exceções/conflitos reais voltam à decisão.

### 8.31. Descanso nas dimensões espirituais — M10-08E DECIDIDO NO RECORTE HUECO MUNDO

**Decisão complementar de 23/10/2026:** em Hueco Mundo, o jogador pode dormir a qualquer horário em cama compatível, cumpridos os requisitos de segurança, espaço e fase. A noite permanente elimina a restrição de horário; não elimina os demais impedimentos. A cama marca spawn conforme 08B e permite descanso, sem explosão exclusivamente por estar em Hueco Mundo. Descansar não cria amanhecer.

Dormir não acrescenta cura espiritual, reposição extra de Energia/Stamina, treino ou atalho artificial de cooldown. Regenerações usuais continuam conforme suas regras, e os relógios de combate seguem a simulação. Acomodação/descanso de corpos grandes exige ficha compatível; não encaixar Gillian em cama humana. Duração, saída do descanso e efeitos vanilla compatíveis continuam com M10/M11/M14/M15/M16.

A resposta reafirma a distinção entre marcar spawn e passar a noite, mas decide expressamente o caso de Hueco Mundo. A recomendação anterior sobre descanso em Soul Society permanece identificada no histórico, sem aprovação por associação; perfil de céu/sono e condições próprias serão fechados na ficha ambiental. Não reabrir a cama marcando spawn já aprovada nas três dimensões. A alternativa de somente registrar spawn, sem permitir dormir em Hueco Mundo, não foi adotada. Pergunta, primeiro esclarecimento e resposta complementar preservados em 12.27/histórico. É adaptação do mod; não afirmação de cânone ou API implementada.

### 8.32. Passagem da noite nos mundos com ciclo — M10-08F DECIDIDO

Aprovado em 23/10/2026: dormir, quando elegível para passar a noite, altera somente o ciclo do mundo onde o grupo está descansando. Participação e condições são avaliadas para os jogadores desse mundo conforme regra de sono do servidor; não exigir que alguém em Hueco Mundo durma para os demais passarem a noite em outro lugar. Quantidade/percentual de participantes e detalhes da votação não foram fixados.

Hueco Mundo conserva noite permanente e descanso a qualquer horário conforme 08E. Dormir nele não adianta o céu de outro mundo. Mudança de horário visual não antecipa cooldowns, manutenções, prazos de combate ou treino; relógios de simulação aprovados em M06 permanecem. Não pausar o servidor, mudar era/campanha ou conceder crescimento offline.

Alternativa não adotada compartilha a passagem da noite entre os mundos que adotarem um ciclo: simplifica a referência de horário, mas faz o descanso de um grupo mudar a experiência de outro. O caso de Hueco Mundo continua visualmente noturno em ambas. A decisão não aprova automaticamente dia/noite em toda dimensão, não altera Nether/End nem a compatibilidade com outros mods por omissão. M10/M15/M16 detalham ciclo por dimensão, elegibilidade, sincronização, salvamento e integração; é alvo de produto, sem afirmar que o relógio atual já é independente. Pergunta/opções/resposta aprovada em 12.28 e histórico; a execução continua por ficha.

### 8.33. Geração natural de criaturas por mundo e região — M10-08G DECIDIDO

Aprovado em 23/10/2026: definir a população natural de Soul Society e Hueco Mundo em fichas de região, coerentes com a identidade de Bleach e com a função local. Escuridão não basta para importar automaticamente a geração comum de zumbis, esqueletos e creepers do Overworld para esses mundos. Monstros vanilla podem existir quando a ficha os justificar como conteúdo/adaptação explícita. É política de geração aprovada para o mod, não uma alegação de que o cânone descreve entidades vanilla.

Hueco Mundo prioriza a presença Hollow e os perfis de ameaça previstos para suas regiões. Soul Society tem moradores, fauna e perigos próprios conforme seus lugares/eras, sem transformar toda a dimensão em zona pacífica ou hostil. O Mundo Humano preserva criaturas vanilla como base e recebe Hollows pelas regras locais de aparição; Karakura, serviços essenciais e áreas protegidas continuam sujeitos às fichas e contratos M08/M09/M10. Não equivale a gerar monstros em qualquer rua ou dentro de todo serviço.

Alternativa não adotada mantém também a geração comum de monstros vanilla nas dimensões espirituais, aumentando a variedade e reaproveitando conteúdo, mas fazendo a escuridão e a noite permanente trazerem ameaças menos ligadas à identidade desses mundos. Nenhuma opção define densidade, distâncias, níveis/atributos, luz necessária, limites de população, fases de era, despawn ou taxas de loot/SP. Força/perfil e comportamento seguem M09; personagens canônicos nomeados, chefes de prova e invocações não viram fauna recorrente por associação.

**Escopo da regra:** população natural prevista na geração do mod. Não apaga entidades já existentes nem decide transporte, reprodução, criação por item/comando, estruturas com geradores, pets ou integrações de outros mods. Fauna e obtenção de recursos precisam ser compatíveis com a base viável de 06A; ausência de monstros vanilla comuns não elimina materiais sem prever sua obtenção. M09/M10/M11/M15/M16 detalham fichas de população, abastecimento e implantação em saves novos/existentes. Pergunta/opções/resposta aprovada em 12.29 e histórico; fichas de região continuam em especificação. Sem aprovação de conteúdo/modelos ou compatibilidade técnica por omissão.

### 8.34. Catálogo funcional de lugares e primeiros percursos — M10-09A DECIDIDO

Aberto o [catálogo funcional M10b](10b-catalogo-funcional-de-lugares-e-rotas.md), em especificação. A amostra documental organiza necessidades já aprovadas: entorno inicial humano, prática/caça, Rukongai, Academia, refúgio e caça em Hueco Mundo. Não seleciona a primeira versão pública nem declara mapa/rota prontos. Funções de NPCs e adversários continuam em M08/M09; M10 demonstra onde e como alcançá-las.

**09A aprovado em 23/10/2026:** os primeiros percursos humanos das quatro origens devem ser alcançáveis num mesmo entorno de Karakura, com locais distintos e coerentes para cada fonte. Quincy/Fullbringer mantêm seus contatos originais; Hollow encontra o veterano independente em área afastada da circulação civil; Shinigami alcança um encaminhamento/rota autorizada para a formação na Soul Society, onde permanece o instrutor da Academia. Não transferir a Academia para o Mundo Humano nem entregar a skill de abrir portal para viabilizar a introdução.

Essa proximidade facilita orientação e reduz deslocamento obrigatório antes dos fundamentos, sem pôr todos os mentores numa praça ou criar amizade automática. Alternativa não adotada inicia os percursos raciais em regiões humanas bem afastadas: reforça isolamento e descoberta, mas aumenta orientação, distâncias e necessidades de continuidade antes do primeiro ensino. Nenhuma fixa o mesmo bloco de nascimento para todos, revela fontes automaticamente, muda motivos para cooperar ou concede todo atendimento a toda origem. Distâncias, acesso inicial, meios de descoberta e prática continuam em M04/M08/M10/M12/M13/M14/M15/M16. Pergunta/opções/resposta aprovada em 12.30 e M10b/12.1; locais concretos continuam por ficha.

### 8.35. Fonte da primeira rota comum a Hueco Mundo — M10-09B DECIDIDO

O [perfil M10b/9.2](10b-catalogo-funcional-de-lugares-e-rotas.md#92-route-hum-hueco-01--primeira-rota-comum-a-hueco-mundo--m10-09b-decidido) registra a aprovação de operadores Arrancar originais independentes em ambas as pontas de uma rota entre entorno discreto de Karakura e refúgio de Hueco Mundo. O veterano inicial pode encaminhar, sem receber automaticamente conhecimento de abertura. Atendimento próprio de volta concretiza a necessidade de 04A sem manter o mesmo portal aberto.

Alternativa não adotada para esta rota: passagem fixa mantida por dispositivo, com infraestrutura e continuidade próprias. Esta escolha não elimina a outra via do catálogo completo de 02C. Nomes, motivos/acordos, acesso por origem/preparo/era, preço, instrumento, capacidade corporal e parâmetros continuam por ficha M08/M10/M11/M12/M13/M15/M16. Ser operador Arrancar não significa exclusividade racial de passageiros nem exige lealdade a Aizen. Serviço comum mantém fora de combate e validações; custos/party das skills pessoais não se estendem automaticamente. Pergunta/opções/resposta aprovada em 12.31, M10b/12.2 e histórico. [M08a/9.38](08a-catalogo-funcional-de-npcs-e-faccoes.md) detalha a função de cada operador, sem aprovar os dados ainda pendentes.

### 8.36. Primeiro acesso a Hueco Mundo por missão e contexto — M10-09C DECIDIDO

**Decisão de 23/10/2026:** a primeira ida a Hueco Mundo será conduzida pelas missões, quando houver necessidade e sentido no arco da obra adaptado à campanha do mod. O momento não será determinado por uma regra geral de “após os fundamentos” ou “somente Gillian”. Se uma missão exigir preparo ou estágio específico, sua ficha deve justificar esse requisito no contexto, sem apresentá-lo como um mínimo canônico universal de abertura de Garganta.

**Viagem assistida:** a missão identifica uma fonte competente e disponível na era para viabilizar a passagem. A rota comum com operadores originais aprovada em 09B permanece uma opção funcional, quando pertinente à missão; não substitui automaticamente todo transporte de campanha nem transforma seus operadores em personagens canônicos. O jogador atravessa como passageiro. As demais origens também recebem transporte compatível com sua missão/contexto conforme 02A; não precisam aprender Senkaimon/Garganta antes da primeira viagem.

**Aquisição separada:** atravessar ou concluir uma missão de viagem não concede automaticamente a skill de abertura. Fonte, aprendizado, requisitos e compra com SP seguem ficha própria; Mastery vem da prática. WORLD-S09 conserva os limites da pesquisa: exemplos conhecidos não demonstram um estágio mínimo universal. As duas alternativas da pergunta original ficam superadas pela direção de acesso narrativo dada pelo usuário.

**Responsabilidades:** M13 define qual missão/arco apresenta a viagem, sua motivação, participantes e transporte; M12 registra condições e marcos; M08 fornece fonte competente e continuidade; M10 fornece rota, espaço, chegada e retorno. O estágio/corpo efetivamente previsto pela missão precisa ser atendido na ida e na volta. Operadores, serviços e passagens comuns mantêm os contratos já aprovados, inclusive fora de combate e retorno viável; uma travessia especial exige ficha própria. M15 resolve era compartilhada versus progresso pessoal/party sem conceder a um passageiro o marco narrativo dos demais.

Essa decisão define o primeiro acesso no contexto de campanha. Não determina que toda visita posterior exija missão ativa, não concede entrada irrestrita em todos os lugares e não revoga o mundo aberto entre capítulos de M03. Acesso posterior e exceções contextuais serão expressos nas fichas, usando as regras gerais vigentes. Missão nominal, momento exato, fonte, possíveis tarifas e áreas concretas continuam entregas de conteúdo; não há nova quest, capacidade, recompensa ou implementação aprovada por associação.

[Pergunta e respostas em 12.32](#1232-m10-09c--decidido--momento-do-primeiro-acesso-hollow), [perfil M10b/9.3](10b-catalogo-funcional-de-lugares-e-rotas.md) e [histórico preservado](historico/2026-10-23-proposta-m10-09c-momento-do-acesso-hollow.md).

### 8.37. Revisão de cobertura e profundidade — 23/10/2026

Por solicitação do usuário, a [revisão M01–M10](00-revisao-cobertura-m01-a-m10-2026-10-23.md) examinou também a adequação do detalhamento. As regras de passagem/renascimento estão mais desenvolvidas que o conteúdo espacial. **M10 permanece em especificação:** contratos comuns consolidados e seis fichas iniciais não equivalem à cobertura das três dimensões e de seus lugares.

A [matriz M10b/7.1](10b-catalogo-funcional-de-lugares-e-rotas.md#71-cobertura-do-horizonte-conhecido--revisão-de-23102026) registra necessidades provenientes de M03/M08, sua função e as entregas restantes. O próximo bloco deve concretizar atividades, ambiente, acesso, circulação e retorno, incluindo corpos grandes e serviços por era. Também permanece necessário escolher a experiência de instalação/uso por modo de mundo; M16 realizará esse contrato sem inventá-lo por omissão.

Missão exata continua em M13 conforme 09C; geografia e percursos continuam em M10. Coordenadas e construção detalhada podem acompanhar a produção posterior. Referências a lugares na matriz não aprovam novos mapas, dimensões, NPCs ou quests. Achados em M04/presença espiritual e M06/Combat State têm retornos dirigidos, sem recomeçar módulos nem repetir contratos aprovados.

### 8.38. Conteúdo local e espaços institucionais — M10-10A/B DECIDIDOS

O catálogo passa das necessidades gerais para propostas de atividade: [Rukongai/M10b-8.3.1](10b-catalogo-funcional-de-lugares-e-rotas.md#831-m10-10a--atividades-com-moradores-e-responsáveis-locais--decidido), com abastecimento, proteção e apuração junto aos habitantes; e [refúgios/M10b-8.5.1](10b-catalogo-funcional-de-lugares-e-rotas.md#851-m10-10b--sobrevivência-e-acordos-locais-nos-refúgios--decidido), com reconhecimento, provisões e acordos locais. **10A/B aprovados em 23/10/2026:** “Atividades com moradores e responsáveis locais (recomendado)” e “Sobrevivência e acordos locais (recomendado)”. Perguntas e alternativas em 12.33/12.34. Estas respostas selecionam as frentes concretas, complementando função/distribuição de 05A/B.

Seireitei/M10b-8.7 compõe pontos de entrada, formação, atendimento de divisão, prática, atendimento especializado e saída a partir de M04/M08. Não concede credencial global nem determina nova barreira. M10 define os espaços e percursos necessários; M08 seleciona os interlocutores e o conteúdo oferecido, M09 perfis de ameaça, M11 provisões e M12/M13 missões/contratos. WORLD-S10 apoia o contexto dos lugares; os perfis novos são adaptações do mod. Não alteram o acesso narrativo de 09C.

### 8.39. Perfil ambiental da Soul Society — M10-11A DECIDIDO APÓS PESQUISA

**Resposta condicionada em 23/10/2026:** “Como é na obra mesmo? eu lembro de chover e ficar de noite lá também, se for isso mesmo podemos seguir com o recomendado por você”. WORLD-S11 confirma noite/manhã e chuva por fontes oficiais do anime, atendendo à condição. **Perfil recomendado adotado:** dia/noite e clima semelhante ao Overworld, com chuva/tempestades conforme a região; duração/frequência, sono e operação dos relógios são adaptações do mod. Não é afirmação de um relógio ou frequência universal comprovados do mangá. 08E continua próprio de Hueco Mundo.

| Aspecto proposto | Funcionamento |
|---|---|
| Céu e clima | Alternância de dia/noite; perfil regional pode limitar precipitação, sem uniformizar cada lugar |
| Cama | Marca o mesmo ponto pessoal já aprovado; permite sono nas condições normais de horário e segurança, sem explosão pela dimensão |
| Passagem da noite | M10-08F: somente o ciclo deste mundo, conforme regra de sono do servidor; não altera Hueco Mundo nem relógios de combate |
| Riscos e criaturas | Noite/chuva não substituem M09/M10-08G: população por região e identidade; não acrescentam monstros vanilla só pela escuridão |
| Sobrevivência | Base localmente viável mantém 06A; materiais/obtenção M11 e fome/regeneração M05, sem nova reposição espiritual por dormir |

**Alternativa:** iluminação diurna permanente, com perfil de descanso a completar se escolhida. Simplifica o céu e preserva iluminação constante, mas reduz a variação temporal da rotina. Recomendo o ciclo para integrar exploração, construção e descanso. Duração, calendário, eventos de clima e realização dos relógios ficam por ficha; perfil aprovado não exigirá nova votação a cada bairro, somente exceções pertinentes.

**Estado:** decidido após a pesquisa que satisfez a condição da resposta; 12.35 e histórico. A regra não altera Nether/End ou o sono de Hueco Mundo. Não confundir decisão ambiental com aceite dos mapas existentes.

### 8.40. Registro de renascimento para corpos grandes — M10-11B DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Marco de abrigo em bloco próprio (recomendado)”. 08B já permite abrigo para corpo que não cabe em cama; agora o registro usa um **bloco próprio de marco de abrigo**, colocado pelo jogador e acionado para registrar o ponto pessoal, sem exigir construção mínima para habilitá-lo. Nome é funcional/provisório, sem item implementado ou origem canônica alegada.

| Momento | Perfil aprovado |
|---|---|
| Colocar | Bloco comum de referência para o abrigo, respeitando construção e permissões do local; obtenção/receita em M11 antes de oferecer a necessidade |
| Registrar | Interagir, com acesso e posição de renascimento compatíveis com o corpo efetivo. Falha informa o motivo e conserva o ponto pessoal anterior ainda válido |
| Referência pessoal | Mesma escolha de spawn usada pela cama; registrar um substitui o outro, sem criar lista extra de destinos ou registro de portal |
| Renascer | Validar apoio/espaço/acesso para o corpo-base efetivo conforme 08B/C/D; não reduzir estágio, escavar ou restaurar forma temporária |
| Bloquear/remover a referência | Aplicar busca próxima quando referência válida e depois recuperação local/Mundo Humano conforme 08C; não tratar um bloco destruído como destino ainda válido |
| Mudar de estágio | Conservar a conquista; validar o corpo efetivo no uso. Construir o marco não garante espaço para um corpo maior adquirido depois |

**Experiência:** Gillian pode preparar sua residência e marcar onde voltará sem ser comprimido numa cama humana ou depender de um NPC para cada marcação. O bloco registra renascimento; não é teleporte, passagem, proteção territorial ou concessão automática de descanso para qualquer corpo. Descanso compatível mantém ficha própria.

**Alternativa:** detectar uma estrutura mínima de abrigo antes de permitir registro. Valoriza construir uma acomodação específica, mas acrescenta materiais, regra de montagem e risco de invalidar o spawn por alterar a estrutura. Recomendo o marco simples; o jogador continua podendo construir sua base e precisa reservar espaço real para o corpo.

**Dependências:** M10 coordena interação/retorno; M04/M07 perfis de corpo, M11 obtenção/bloco, M14 apresentação e M15/M16 posição, validação e registro por personagem. Não foi escolhido tamanho numérico, receita, acabamento visual ou exclusividade de uso do bloco entre jogadores. **Estado:** decidido; 12.36 e histórico.

### 8.41. Campanha por tipo de mundo — M10-12A DECIDIDO

**Aprovado em 23/10/2026. Resposta:** “Campanha em mundos novos e existentes compatíveis (recomendado)”. Planejar a campanha completa tanto para mundos novos quanto para saves existentes compatíveis, mediante integração validada. O preset História organiza a implantação desde a criação; um mundo comum/usado exige localizar áreas ainda não geradas e adequadas aos núcleos, mantendo construções e chunks existentes. É direção de produto aprovada, não compatibilidade já demonstrada pelo instalador atual.

| Perfil aprovado | Experiência a entregar | Condição necessária |
|---|---|---|
| Mundo novo no preset História | Núcleos planejados e percursos preparados desde o início, com arredores gerados conforme 01A | Aceitar geografia/conteúdo e validar instalação; mapas existentes continuam sujeitos à avaliação, não aprovados por seu nome |
| Mundo novo comum | Minecraft gerado normalmente com núcleos de Bleach integrados em regiões apropriadas | Distribuição e orientação viáveis sem exigir coordenadas fixas universais; mesma função de lugares e campanha |
| Mundo já existente compatível | Preservar a vida/construções do save e incorporar campanha/lugares através de integração própria | Avaliar terreno/regiões disponíveis, registrar posições reais e validar fontes, acessos e retorno antes de oferecer as etapas dependentes |
| Servidor | Uma geografia e era compartilhadas, com progresso pessoal conforme M03 | Integração conduzida pelo responsável pelo mundo; novo personagem não reinstala cidades, muda a era ou recebe marcos dos demais |

**Limites:** a integração não copia mapas sobre regiões usadas, recria a cidade por jogador ou converte silenciosamente um save. Caso não exista região adequada, a campanha correspondente não pode ser apresentada como disponível sem resolver a implantação. Escolher este horizonte exige um procedimento explícito de integração/recuperação em M16; não garante aceitar qualquer save ou combinação de mods sem validação. Início do personagem, atendimento na era vigente e catch-up mantêm M04/M15, sem reset de progresso por associação.

**Alternativa não adotada:** reservar a campanha completa para mundos novos criados no preset História; mundos comuns/usados recebem somente mecânicas e conteúdos cuja instalação seja compatível. Reduz os perfis de implantação da campanha, mas exige uma nova jornada de mundo para quem já construiu sua base. Recomendo abrangência validada em ambos por coerência com exploração, construção e persistência de Minecraft, reconhecendo o trabalho adicional de integração.

**Evidência técnica local e limite:** README de mapas relido e leitura dirigida de `HistoryWorld`/`HistoryMapInstaller` em 23/10/2026 mostram o fluxo existente de História e a recusa a sobrescrever regiões existentes em instalação nova. Isso não demonstra instalação de núcleos em áreas novas de qualquer save. A opção recomendada exige realização própria; não executar o instalador atual como conversor. Nenhum arquivo de mundo foi alterado.

**Responsável:** M10 coordena a experiência por perfil; M16 implantação/validação; M03/M13 continuidade de cenário, M04 criação e M15 mundo/era/personagem. **Estado:** decidido; pergunta/resposta em 12.37 e histórico. Esta decisão não fecha os locais, o retorno nem a revisão inteira.

### 8.42. Fontes de volta e áreas de recuperação — M10-12B/C DECIDIDOS

[M10b/9.4](10b-catalogo-funcional-de-lugares-e-rotas.md#94-demonstrar-a-volta-de-cada-percurso--aplicação-de-m10-04a) organiza a comprovação de retorno exigida por 04A, incluindo convidado sem skill em base distante. Não cria teleporte de resgate ou transforma a área de renascimento em rota de viagem. Cada percurso ainda precisa de fonte/meio efetivo e contingências pertinentes; a janela bidirecional não basta como única saída posterior.

**12B aprovado em 23/10/2026 por “Operadores institucionais nas duas pontas (recomendado)”:** dois operadores Shinigami originais autorizados para a rota comum Humano ↔ Soul Society, com atendimento perto de Karakura e no acesso externo de Seireitei pelo lado do Rukongai. A primeira chegada de campanha continua podendo usar outra fonte pertinente; a operação comum não decide o acontecimento racial inicial. Alternativa: concentrar serviço comum na rede Urahara. Perfil M10b/9.5 e M08a/9.39.

**12C aprovado em 23/10/2026 por “Áreas abertas conectadas aos núcleos (recomendado)”:** áreas abertas de recuperação na periferia de Karakura, junto a povoado do Rukongai fora de Seireitei e junto ao refúgio inicial de Hueco Mundo. Próximo ao ponto humano ficaria o prestador original de reset. Todos precisam comportar os corpos contemplados, inclusive Gillian, com acesso e saída adequados. Alternativa: instalar esses atendimentos nos centros/interiores dos núcleos e adaptar a arquitetura. Perfil M10b/9.6.

São escolhas de conteúdo/localização para aplicar contratos aprovados; não fatos canônicos comprovados de postos da obra, novas classes de poderes ou implementação. Perguntas/opções/respostas em 12.38–12.39; nenhuma pendente dessas duas escolhas. M04 precisa fechar presença/interação e posição/corpo do reset; M06 fecha Combat State; M08 interlocutores/era e M16 implantação. Escolher lugares não resolve esses contratos por associação.

### 8.43. Conferência dos percursos e retorno dirigido a M04/M06 — 23/10/2026

12B/C aprovados e aplicados em M08/M10b; não há pergunta pendente dessas escolhas. Conferência documental do complemento funcional de M10, sem execução de mapa/percurso:

| Percurso / função | Base agora definida | Entrega que ainda precisa ser concretizada |
|---|---|---|
| Karakura → formação Shinigami → retorno | Origem/encaminhamento M04; operador institucional comum nas duas pontas 12B; atendimento externo não exige ingresso no interior apenas para voltar | M13: acontecimento do primeiro acesso; M08: indivíduos/era; M11: equipamento/SP; M10/M16: posição e caminho implantados |
| Primeiro acesso a Hueco Mundo → apoio/caça → volta | Momento por missão 09C; operadores 09B; frentes de atividade 10B e recuperação local 12C | M13: missão/contexto concreto; M08: pessoas; M09: ameaça e sentidos do perfil; M11: provisões; implantação/percurso reais |
| Ponto pessoal de renascimento → recuperação | Cama/marco 08B/11B; busca/local/Humano 08C/D; locais abertos 12C | Espaço dos perfis corporais e áreas implantadas/validadas; não basta criar um nome de lugar |
| Consultar reset | Prestador original gratuito, local próximo à recuperação humana e espaço às quatro origens; RAC-30 e RAC-31A já decidem interação, corpo inicial próprio e reset no atendimento | M11/M12/M15/M16: itens vinculados, recompensas e operação íntegra; implantação do acesso corporal |
| Visitar base distante por skill própria/convite | Destino conhecido/registrado 03D/E; participação/janela 07B–G; retorno 04A ligado aos operadores de 09B/12B em M10b/9.4.1 | Cada ficha demonstra caminho, acesso e contrapartida para o passageiro/corpo/era atendidos. Nomear um operador distante ou local de recuperação não prova esse percurso |

As escolhas de ambiente, frentes locais, perfis de mundo, operação comum e localização funcional de recuperação foram concretizadas no recorte de 10A/B, 11A/B e 12A/B/C. **M10 continua em especificação de conteúdo**, com diferenças entre escolha de produto e ficha/implantação explicitadas; não foi fechado todo catálogo ou certificada jogabilidade. Os retornos dirigidos foram concluídos conceitualmente em M04a/RAC-30, M04/RAC-31A e M06a/CS-01A/B/C/D. M08a/9.40 e M09a/9.5–9.7 aplicam esses contratos às fontes e à amostra de ameaças; aldeões como civis vivos caçáveis aprovados em M09-12C. M10b/9.4.1 explicita as ligações de retorno, com os limites de entrega. Exceção de retorno ou conteúdo novo necessário volta ao responsável, sem converter viagens comuns em situações sem saída por omissão.

## 9. Integrações

| Entrada/necessidade | Saída/limite |
|---|---|
| Origem M04 e fontes M08 | Percurso inicial viável, sem exigir habilidade ainda bloqueada ou raça exclusiva de todo território |
| Pedido de travessia | Servidor valida origem, acesso, execução e chegada; UI/item não autorizam por conta própria |
| Corpo/forma M06/M07 | Espaço e estados compatíveis; não remover blocos ou restaurar forma para fazer caber |
| Encontro M09 | Local, limites e consequências consistentes; não resetar boss ou crédito por atravessar interface |
| Era/quest M03/M12/M13 | Pedidos de mudança local autorizada, sem regeneração destrutiva de todo o save |
| Economia/itens M11 | Recursos e serviços com ficha; nome canônico não define receita/moeda |
| Multiplayer M15/M16 | Concorrência, autoridade, persistência, migração e proteção observáveis |

## 10. Conteúdo inicial para elaborar

Mundo Humano/Overworld, Soul Society e Hueco Mundo, já presentes no manifesto do modo História. Primeiras necessidades: local das quatro introduções/fontes iniciais, transição Shinigami, espaço de caça/confrontos e uma rota de ida/retorno com chegada válida. Essa seleção de necessidades não aprova ordem de produção, coordenadas ou todos os portais. O [catálogo funcional M10b](10b-catalogo-funcional-de-lugares-e-rotas.md) reúne as primeiras fichas de necessidade, em especificação; 09A aprovou a proximidade dos primeiros percursos humanos; M10b/9 elabora os perfis de rota.

## 11. Visão completa

Núcleos institucionais, regiões exploráveis e locais de campanha/treino, rotas Senkaimon/Garganta e conteúdos próprios de viagem. Dangai e outros espaços exigem função/continuidade/ficha; não assumidos como dimensão obrigatória, minijogo obrigatório ou corredor já implementado. Nether/End existem no preset atual, mas isso não cria enredo Bleach ou novas exigências de progressão nesses destinos.

## 12. Decisões e perguntas

### 12.1. M10-01A — DECIDIDO — Núcleos e áreas geradas

**Pergunta enviada em 23/10/2026:** Para os mundos principais, recomendo combinar núcleos planejados e estáveis em cada save — como a região urbana humana, Seireitei e Las Noches — com áreas geradas ao redor para exploração, recursos e construção, respeitando a identidade de cada dimensão. Os mapas atuais seriam avaliados como ponto de partida. Adotamos esse modelo misto ou planejamos cada mundo inteiro como um mapa pré-construído?

**Opções:** “Núcleos planejados com áreas geradas ao redor (recomendado)” / “Mundos inteiramente pré-construídos”. **Resposta em 23/10/2026:** “Núcleos planejados com áreas geradas ao redor (recomendado)”. Cobertura/alternativa em 8.1.

### 12.2. M10-01B — DECIDIDO — Proteção de trechos essenciais

**Pergunta enviada em 23/10/2026:** Para evitar que uma construção indispensável seja removida e bloqueie a jornada, recomendo proteção localizada e sinalizada nos blocos de acessos, serviços e pontos de viagem essenciais. O restante mantém construção e mineração conforme as regras do local; isso não protege automaticamente cidades inteiras nem torna personagens invulneráveis. Adotamos esse padrão ou deixamos tudo editável, exigindo meios de repor acessos e serviços?

**Opções:** “Proteção pequena e sinalizada em trechos essenciais (recomendado)” / “Tudo editável, com reposição de acessos e serviços”. **Resposta em 23/10/2026:** “Proteção pequena e sinalizada em trechos essenciais (recomendado)”. Cobertura/alternativa em 8.2.

### 12.3. M10-02A — DECIDIDO — Rotas iniciais e skills de abertura

**Pergunta enviada em 23/10/2026:** Para viagens entre mundos, recomendo acesso por passagens e serviços estabelecidos no contexto, como rotas de Senkaimon e Garganta. Abrir uma passagem própria seria uma capacidade específica com requisitos; a introdução Shinigami teria uma rota viável sem exigir esse aprendizado avançado. Adotamos esse acesso por rotas ou usamos itens pessoais de viagem para destinos já liberados?

**Opções:** “Rotas e serviços de passagem (recomendado)” / “Itens pessoais para destinos liberados”. **Resposta em 23/10/2026:** “Acredito que no inicio de cada era deveria ser por rotas e serviços de passagem, mas que houvesse uma skill para que o jogador pudesse abrir um Senkaimon se for um shinigami ou uma garganta se for um arrancar/hollow, não lembro se shinigamis são capazes de abrir gargantas e vice versa, mas assim como no anime que certos personagens conseguem abrir Senkaimons ou gargantas para cada caso, mas sem deixar exclusivo para shinigamis e hollows”. Direção própria: rotas/serviços inicialmente e autonomia por skills, sem exclusividade racial absoluta. Cobertura em 8.3; complemento posterior em 8.6; requisitos e capacidades concretas ainda exigem ficha.

### 12.4. M10-02B — DECIDIDO — Passagens comuns fora de combate

**Pergunta enviada em 23/10/2026:** Nas passagens comuns entre mundos, recomendo permitir uma tentativa de fuga em combate, com preparo de travessia perceptível e comprometido, respeitando as fases das ações. Sair da posição exigida ou sofrer controle compatível pode interromper; dano sozinho não cancela automaticamente. A viagem não apaga cooldowns ou consequências, e encontros especiais têm seus limites. Adotamos isso ou exigimos estar fora de combate para usar toda passagem comum?

**Opções:** “Travessia em combate com preparo e riscos (recomendado)” / “Passagens comuns somente fora de combate”. **Resposta em 23/10/2026:** “Passagens comuns somente fora de combate”. Segunda opção escolhida; a fuga em combate recomendada não foi aprovada. Cobertura em 8.4; condição comum preservada independentemente da fonte. Não há nova votação da mesma restrição por instrumento ou origem.

### 12.5. M10-02C — DIREÇÃO APROVADA — Técnica e dispositivo

**Manifestação espontânea em 23/10/2026:** “Então vamos seguir a obra, podemos ter tanto o dispositivo quanto o que faz o Urahara abrir e estabilizar, como ele faz?”. Não houve pergunta de múltipla escolha. Aprova a coexistência das duas vias como direção de conteúdo, com pesquisa e limites em 4/8.6. Mecânica executável, fonte de aprendizado/obtenção e requisitos continuam nas fichas responsáveis.

### 12.6. M10-03A — REVISADO POR 03D — Destinos das skills básicas

**Pergunta de 23/10/2026:** Para as skills básicas de viagem, recomendo permitir abrir a passagem em um local válido e chegar a pontos de entrada estabelecidos nos destinos já habilitados ao personagem. Assim, aprender a abrir não permite escolher qualquer coordenada. A alternativa permite também registrar pontos próprios em lugares visitados e autorizados, exigindo regras de marcação e proteção. Qual modelo adotamos para a versão básica?

**Opções:** “Chegada em pontos estabelecidos (recomendado)” / “Também permitir pontos próprios registrados”. **Resposta em 23/10/2026:** “Chegada em pontos estabelecidos (recomendado)”. Aprovação original preservada como histórico; a restrição a entradas estabelecidas foi revisada pela resposta posterior de 03D em 12.10, contrato vigente em 8.12.

### 12.7. M10-03B — DECIDIDO — Busca próxima para chegada inválida

**Pergunta enviada em 23/10/2026:** Como padrão das viagens do mod, recomendo validar a área de chegada antes da travessia e confirmar o espaço no momento de transferir. Se não houver posição válida nessa área para o corpo que chegará, a viagem é recusada e o jogador permanece na origem, com aviso. Não remove blocos nem procura outro local fora da área. Adotamos isso ou buscamos automaticamente uma posição próxima fora da área de chegada?

**Opções:** “Recusar se a área de chegada estiver inválida (recomendado)” / “Buscar posição próxima fora da área”. **Resposta em 23/10/2026:** “Buscar posição próxima fora da área”. Segunda opção escolhida, diferente da recomendação; contrato em 8.8. Falha da busca decidida em 03C; parâmetros continuam por ficha, sem decidir cobrança ou chegada imune.

### 12.8. M10-03C — DECIDIDO — Limites e falha da busca

**Pergunta de 23/10/2026:** Para fechar a busca de chegada que você escolheu, recomendo procurar apenas nas proximidades do ponto, dentro de um limite definido para a rota, respeitando espaço do corpo, terreno e permissões de acesso. Se não encontrar posição válida, a viagem é recusada e você permanece na origem. A alternativa tenta outro ponto de chegada habilitado no mesmo destino, avisando antes de transferir. Qual fallback adotamos?

**Opções:** “Busca próxima limitada; sem solução, ficar na origem (recomendado)” / “Tentar outro ponto de chegada habilitado”. **Retorno do usuário em 23/10/2026:** “A ideia é que o jogador por exemplo, utilizou um Senkaimon, então ele chega em um ponto da Seireitei, o Senkaimon pode chegar em qualquer lugar da sociedade das almas? Uma garganta pode chegar em qualquer lugar? E como podemos fazer?”. Retorno inicial foi pedido de esclarecimento, sem escolha de fallback; foi respondido posteriormente na pergunta reformulada abaixo. Pesquisa em 4; nova revisão de destinos em 8.11 não apaga a resposta posterior de 03C.

**Pergunta reformulada após esclarecimento, em 23/10/2026:** Exemplo: você escolhe uma entrada habilitada na Seireitei. Se a chegada estiver bloqueada, o jogo procura espaço perto dessa mesma entrada, respeitando as barreiras e o acesso. Se não encontrar, recomendo cancelar a viagem e manter você na origem, com aviso. A alternativa tenta outra entrada já habilitada na Soul Society. Qual comportamento adotamos?

**Opções reformuladas:** “Sem espaço próximo, permanecer na origem (recomendado)” / “Tentar outra entrada habilitada”. **Resposta em 23/10/2026:** “Sem espaço próximo, permanecer na origem (recomendado)”. Primeira opção aprovada; contrato em 8.9. O exemplo não aprova local, portão ou condição de acesso específica.

### 12.9. M10-04A — DECIDIDO — Retorno em exploração comum

**Pergunta de 23/10/2026:** Nas viagens comuns de exploração, recomendo garantir um meio de retorno viável por passagem, serviço ou capacidade já disponível, sem exigir aprender uma skill nova para conseguir sair. Isso não mantém necessariamente o mesmo portal aberto. Viagens sem retorno imediato ficam reservadas a situações especiais avisadas antes de começar. Adotamos esse padrão ou permitimos descobrir somente no destino como voltar?

**Opções:** “Retorno viável nas viagens comuns (recomendado)” / “Descobrir o retorno apenas no destino”. **Resposta em 23/10/2026:** “Retorno viável nas viagens comuns (recomendado)”. Primeira opção aprovada; contrato em 8.10, meios concretos e contingências exigem ficha.

### 12.10. M10-03D — DECIDIDO — Revisão da seleção de destino

**Direção recebida em 23/10/2026:** resposta integral em 8.11 e no histórico. Reabre 03A; não define por si só fórmula de coordenadas ou método de marcação.

**Pergunta:** Para as skills próprias de Senkaimon/Garganta, como escolher a chegada? Recomendo permitir selecionar um local conhecido e acessível no outro mundo, incluindo locais do jogador, sem limitar às entradas criadas pelo mod; a forma de conhecer/registrar será detalhada depois. A alternativa segue a lógica geográfica do Nether: a posição de abertura determina a região correspondente do outro mundo, sem escolher livremente o destino. Qual modelo prefere?

**Opções:** “Escolher local conhecido e acessível (recomendado)” / “Região correspondente à posição de abertura”. **Resposta em 23/10/2026:** “Escolher local conhecido e acessível (recomendado)”. Primeira opção aprovada; revisa a restrição de 03A. Contrato/alternativa em 8.12; 03B/C, 02B e 04A preservados.

### 12.11. M10-03E — DECIDIDO — Registro dos destinos próprios

**Pergunta de 23/10/2026:** Para guardar um destino próprio de viagem, recomendo visitar o local e escolher “Registrar destino”, dando um nome, sem precisar construir um portal permanente ali. Serviços e fontes apropriadas continuam permitindo a primeira viagem a lugares ainda desconhecidos. A alternativa registra automaticamente pontos ao explorar. Qual modelo adotamos?

**Opções:** “Visitar e registrar manualmente (recomendado)” / “Registrar automaticamente ao explorar”. **Resposta em 23/10/2026:** “Visitar e registrar manualmente (recomendado)”. Primeira opção aprovada; contrato e dependências em 8.13. Não reabre escolha de destino, busca próxima ou restrição de combate já aprovadas.

### 12.12. M10-05A — DECIDIDO — Rukongai e Seireitei

**Pergunta de 23/10/2026:** Para a Soul Society, recomendo que o Rukongai seja uma região jogável com povoados, atividades locais e exploração, enquanto a Seireitei concentra formação, instituições e serviços ligados ao seu contexto. O acesso continua dependendo da situação e da era. Adotamos essa divisão de funções ou deixamos o Rukongai principalmente como cenário de passagem até a Seireitei?

**Opções:** “Rukongai com atividades próprias (recomendado)” / “Rukongai principalmente como passagem”. **Resposta em 23/10/2026:** “Rukongai com atividades próprias (recomendado)”. Primeira opção aprovada; herança e contrato em 8.14; não altera as condições já aprovadas de acesso, formação ou mundo persistente.

### 12.13. M10-05B — DECIDIDO — Pontos de apoio independentes em Hueco Mundo

**Pergunta de 23/10/2026:** A orientação Hollow independente já foi aprovada. Para distribuir os pontos de apoio em Hueco Mundo, recomendo pequenos refúgios em locais diferentes, ligados a fontes ou grupos com motivos para cooperar; cada um teria funções e acesso próprios. Las Noches mantém seu papel conforme a era. Esses refúgios seriam conteúdo criado para o mod. Preferimos essa distribuição ou concentrar o apoio independente em um povoado principal?

**Opções:** “Pequenos refúgios distribuídos (recomendado)” / “Um povoado independente principal”. **Resposta em 23/10/2026:** “Pequenos refúgios distribuídos (recomendado)”. Primeira opção aprovada; distribuição em 8.15; funções e autonomia já aprovadas em M08/M07 são herança, sem nova votação.

### 12.14. M10-05C — DECIDIDO — Núcleo urbano do Mundo Humano

**Pergunta de 23/10/2026:** Para o núcleo urbano do Mundo Humano, recomendo Karakura adaptada ao Minecraft: bairros e lugares reconhecíveis ligados às atividades e aos contatos, integrada ao restante do Overworld explorável e construível, sem exigir uma reprodução em escala real. O mapa atual ainda precisará ser avaliado. Adotamos essa identidade ou preferimos uma cidade original com alguns locais canônicos inseridos?

**Opções:** “Karakura adaptada ao Minecraft (recomendado)” / “Cidade original com locais canônicos”. **Resposta em 23/10/2026:** “Karakura adaptada ao Minecraft (recomendado)”. Primeira opção aprovada; contrato/alternativa em 8.16; fonte e limite em WORLD-S07. Não substitui avaliação do mapa existente ou os contratos de geração/preservação do save.

### 12.15. M10-06A — DECIDIDO — Recursos e bases nas dimensões espirituais

**Pergunta de 23/10/2026:** Para Soul Society e Hueco Mundo, recomendo permitir manter uma base com fontes locais de materiais e abastecimento adequadas a cada mundo. Não seria necessário voltar ao Mundo Humano apenas para sustentar a rotina básica, mas recursos especiais e diferenças locais continuariam dando motivos para viajar. A alternativa faz essas bases dependerem principalmente de suprimentos trazidos do Mundo Humano. Qual direção adotamos?

**Opções:** “Base viável com abastecimento local (recomendado)” / “Base dependente de suprimentos do Mundo Humano”. **Resposta em 23/10/2026:** “Base viável com abastecimento local (recomendado)”. Primeira opção aprovada; contrato/alternativa e responsáveis em 8.17; não decide catálogo de recursos, alimentação ou receitas antes de M11.

### 12.16. M10-07A — DECIDIDO — Experiência da travessia comum

**Pergunta de 23/10/2026:** Nas viagens comuns de Senkaimon/Garganta, recomendo uma transição breve com a apresentação da passagem, sem exigir percorrer um corredor toda vez. Trechos jogáveis de Dangai/Garganta ficam para rotas ou situações próprias, com objetivos e riscos definidos. A alternativa exige uma travessia jogável também nas viagens comuns. Qual experiência adotamos?

**Opções:** “Transição breve; percursos em situações próprias (recomendado)” / “Percurso jogável também nas viagens comuns”. **Resposta em 23/10/2026:** “Transição breve; percursos em situações próprias (recomendado)”. Primeira opção aprovada; contrato/alternativa em 8.18; não decide custos, preparo, passageiros, duração numérica ou uma implementação de espaço intermediário.

### 12.17. M10-07B — DECIDIDO — Compartilhamento de passagens próprias

**Pergunta de 23/10/2026:** Para passagens próprias de Senkaimon/Garganta, recomendo permitir convidados autorizados por quem abriu. Cada pessoa escolhe atravessar; não precisa dominar a skill de abertura nem ter o destino registrado, mas precisa cumprir seus próprios requisitos de acesso. A alternativa mantém a passagem de uso pessoal por padrão. Qual adotamos?

**Opções:** “Compartilhar com convidados autorizados (recomendado)” / “Passagem de uso pessoal por padrão”. **Resposta em 23/10/2026:** “Compartilhar com convidados da mesma party”. Resposta própria restringe os convidados ao grupo de quem abre; não é aprovação irrestrita de convidados externos nem travessia automática da party. Contrato/alternativa em 8.19; condições de combate, chegada e registro são herdadas, sem nova votação.

### 12.18. M10-07C — DECIDIDO — Recursos e janela de abertura

**Pergunta de 23/10/2026:** Para as skills comuns de abertura, recomendo cobrar Energia uma vez, quando a passagem se formar, e deixá-la aberta por uma janela curta sem consumo contínuo. Quem abriu pode atravessar enquanto ela permanece disponível para os convidados da mesma party. Abrir e não usar não devolve o custo; valores e duração serão calibrados depois. A alternativa consome Energia continuamente enquanto a passagem fica aberta. Qual modelo adotamos?

**Opções:** “Custo ao abrir + janela sem manutenção (recomendado)” / “Consumo contínuo enquanto aberta”. **Resposta em 23/10/2026:** “Custo ao abrir + janela sem manutenção (recomendado)”. Primeira opção aprovada; contrato/alternativa em 8.20. Não escolhe valores, número de passageiros, tarifa individual, fases ou ciclo de vida da passagem.

### 12.19. M10-07D — DECIDIDO — Preparo de abertura

**Pergunta de 23/10/2026:** Para abrir a passagem, recomendo um preparo breve parado, podendo olhar ao redor. Mover-se ou iniciar uma ação disponível cancela, sem cobrar Energia nem acrescentar recuperação; entrar em combate também cancela, conforme a restrição já aprovada. Pedido de ação indisponível mantém o preparo ainda válido. Adotamos esse preparo ou abertura imediata após validar os requisitos?

**Opções:** “Preparo breve parado e cancelável (recomendado)” / “Abertura imediata após validação”. **Resposta em 23/10/2026:** “Preparo breve parado e cancelável (recomendado)”. Primeira opção aprovada; contrato em 8.21; condição fora de combate e cobrança ao formar são herança de 02B/07C.

### 12.20. M10-07E — DECIDIDO — Ciclo da passagem formada

**Pergunta de 23/10/2026:** Depois de formada, recomendo que a passagem continue pelo tempo já pago mesmo se quem abriu morrer ou desconectar, enquanto os locais de entrada e chegada estiverem sendo simulados. Descarregar um desses locais ou reiniciar encerra a passagem, sem restaurá-la depois. A alternativa também a encerra quando o abridor morre ou desconecta. Qual regra adotamos?

**Opções:** “Cumprir a janela já paga (recomendado)” / “Encerrar também com morte ou desconexão do abridor”. **Resposta em 23/10/2026:** “Cumprir a janela já paga (recomendado)”. Primeira opção aprovada; contrato em 8.22; travessia normal do abridor já é coberta por 07C.

### 12.21. M10-07F — DECIDIDO — Limite e fechamento

**Pergunta de 23/10/2026:** Para as skills comuns, recomendo uma passagem própria ativa por personagem, com opção de encerrá-la manualmente sem reembolso. Para abrir outra, é preciso fechar a anterior ou esperar seu término. A alternativa permite várias passagens simultâneas, com limite e custos a calibrar. Qual modelo adotamos?

**Opções:** “Uma passagem ativa, com fechamento manual (recomendado)” / “Várias passagens simultâneas com limite”. **Resposta em 23/10/2026:** “Uma passagem ativa, com fechamento manual (recomendado)”. Primeira opção aprovada; contrato em 8.23.

### 12.22. M10-07G — DECIDIDO — Ida e volta

**Pergunta de 23/10/2026:** Enquanto estiver aberta, recomendo que a passagem permita ida e volta entre as duas pontas para o abridor e os convidados autorizados da mesma party, sem nova abertura. Atravessar não renova o prazo, e acesso, combate e espaço continuam sendo validados em cada viagem. A alternativa permite somente ida; voltar exige outra abertura ou serviço. Qual adotamos?

**Opções:** “Ida e volta enquanto aberta (recomendado)” / “Somente ida por abertura”. **Resposta em 23/10/2026:** “Ida e volta enquanto aberta (recomendado)”. Primeira opção aprovada; contrato em 8.24.

### 12.23. M10-08A — DECIDIDO — Ambiente e água em Hueco Mundo

**Pergunta de 23/10/2026:** Para Hueco Mundo, recomendo noite permanente e paisagem de areia branca, sem rios ou lagos naturais, seguindo a referência da obra. Como adaptação para Minecraft, água trazida pelo jogador pode ser colocada e usada normalmente, ajudando a manter bases. Adotamos esse perfil ou impedimos água estável também quando trazida de fora?

**Opções:** “Paisagem canônica + água trazida funcional (recomendado)” / “Impedir água estável, inclusive trazida de fora”. **Resposta em 23/10/2026:** “Se na obra tiver alguma referência a isso e for impedir água no Hueco Mundo então que assim seja, mas se não vamos com sua recomendação”. Aprovação condicionada: sem proibição explícita localizada nas fontes consultadas, aplica-se a recomendação como adaptação. WORLD-S08/8.26 e histórico registram evidência, limites e revisão se surgir fonte contrária. Não se afirma autorização física universal no cânone.

### 12.24. M10-08B — DECIDIDO — Local de renascimento

**Pergunta de 23/10/2026:** Para o renascimento, recomendo permitir escolher um ponto nas três dimensões principais: cama para corpos compatíveis ou abrigo próprio para corpos grandes, como Gillian. Isso permite morar e renascer na Soul Society ou em Hueco Mundo. A escolha exige acesso e espaço válidos; o que fazer se o ponto for perdido ou bloqueado será definido em seguida. Adotamos esse modelo ou concentramos o renascimento no Mundo Humano?

**Opções:** “Ponto de renascimento no mundo escolhido (recomendado)” / “Renascimento concentrado no Mundo Humano”. **Resposta em 23/10/2026:** “Ponto de renascimento no mundo escolhido (recomendado)”. Recomendação aprovada; contrato/alternativa em 8.27 e [histórico](historico/2026-10-23-proposta-m10-08b-local-de-renascimento.md).

### 12.25. M10-08C — DECIDIDO — Recuperação após falha do ponto pessoal

**Pergunta de 23/10/2026:** Se o ponto escolhido estiver bloqueado, recomendo buscar espaço seguro perto dele enquanto a referência e o acesso forem válidos. Se foi destruído ou não houver espaço, usar uma área de recuperação preparada no mesmo mundo; se esse mundo estiver indisponível ou sem acesso permitido, recorrer a uma área segura no Mundo Humano. Todas devem comportar o corpo atual, inclusive Gillian. Adotamos essa sequência ou voltamos direto ao Mundo Humano quando o ponto pessoal falhar?

**Opções:** “Busca próxima → recuperação local → Mundo Humano (recomendado)” / “Falha do ponto → Mundo Humano”. **Resposta em 23/10/2026:** “Busca próxima → recuperação local → Mundo Humano (recomendado)”. Recomendação aprovada; contrato/alternativa em 8.28 e [histórico](historico/2026-10-23-proposta-m10-08c-recuperacao-do-renascimento.md).

### 12.26. M10-08D — DECIDIDO — Ausência de ponto pessoal e reconexão

**Pergunta de 23/10/2026:** Para fechar esse bloco, recomendo que quem ainda não escolheu um ponto renasça na área inicial segura do Mundo Humano. Ao reconectar, mantém a posição salva se ela comportar o corpo; se ficou bloqueada, tenta espaço perto e depois usa a recuperação que acabamos de aprovar. Esse ajuste só corrige o impedimento, preservando raça, estágio e recursos. Adotamos esse padrão?

**Opções:** “Manter local válido e recuperar bloqueios (recomendado)” / “Login sempre no ponto de renascimento”. **Resposta em 23/10/2026:** “Manter local válido e recuperar bloqueios (recomendado)”. Pacote recomendado aprovado; contrato/alternativa em 8.29 e [histórico](historico/2026-10-23-proposta-m10-08d-posicionamento-inicial-e-retorno.md).

### 12.27. M10-08E — DECIDIDO NO RECORTE HUECO MUNDO — Descanso nas dimensões espirituais

**Pergunta de 23/10/2026:** Para concluir a integração das camas, recomendo permitir descanso na Soul Society e em Hueco Mundo, sem explosão por estar nessas dimensões. Registrar o renascimento independe de poder dormir no momento. Em Hueco Mundo, descansar preserva a noite permanente; nenhuma cama concede cura espiritual extra ou adianta cooldowns. Adotamos esse padrão ou usamos camas nessas dimensões apenas para registrar renascimento?

**Opções:** “Descanso e renascimento compatíveis (recomendado)” / “Somente registro de renascimento”. **Resposta em 23/10/2026:** “Acredito que as camas devam sim setar spawn, e ainda não entendi como seriam isso de pontos de renascimento, está mal comunicado como isso vai funcionar pois é diferente do nether certo?”. O usuário reafirma cama marcando spawn e pede explicação; não escolhe entre permitir descanso e somente registrar renascimento. **Descanso permanece em esclarecimento, sem aprovação inferida.** **Resposta complementar em 23/10/2026:** “Então se é sempre noite no Hueco Mundo pode dormir a qualquer momento possivel”. Aprovado dormir em Hueco Mundo sem restrição de horário, cumpridos requisitos de segurança/corpo; descanso não cria amanhecer. Perfil de Soul Society não aprovado por associação. Contrato em 8.31 e [histórico](historico/2026-10-23-proposta-m10-08e-descanso-nas-dimensoes-espirituais.md).

### 12.28. M10-08F — DECIDIDO — Passagem da noite por mundo

**Pergunta de 23/10/2026:** Para os mundos com dia e noite, recomendo que dormir avance apenas o ciclo daquele mundo, conforme as regras de sono do servidor para os jogadores presentes ali. Dormir em um mundo não muda o horário dos outros; Hueco Mundo continua sempre noturno. Cooldowns seguem o tempo de simulação. Adotamos esse funcionamento ou a passagem da noite deve ser compartilhada entre os mundos com ciclo?

**Opções:** “Passagem da noite por mundo (recomendado)” / “Passagem da noite compartilhada”. **Resposta em 23/10/2026:** “Passagem da noite por mundo (recomendado)”. Recomendação aprovada; contrato/alternativa em 8.32 e [histórico](historico/2026-10-23-proposta-m10-08f-passagem-da-noite-por-mundo.md).

### 12.29. M10-08G — DECIDIDO — Criaturas por região

**Pergunta de 23/10/2026:** Para Soul Society e Hueco Mundo, recomendo geração natural de criaturas definida por região e pela identidade de Bleach. Zumbis, esqueletos e creepers só apareceriam quando previstos numa ficha específica, em vez de surgirem automaticamente no escuro. O Mundo Humano preserva as criaturas vanilla e recebe Hollows conforme suas regiões. Adotamos esse padrão ou mantemos também a geração comum de monstros vanilla nas dimensões espirituais?

**Opções:** “Criaturas por região e identidade do mundo (recomendado)” / “Monstros vanilla também nas dimensões espirituais”. **Resposta em 23/10/2026:** “Criaturas por região e identidade do mundo (recomendado)”. Recomendação aprovada; contrato/alternativa em 8.33 e [histórico](historico/2026-10-23-proposta-m10-08g-criaturas-por-regiao.md).

### 12.30. M10-09A — DECIDIDO — Proximidade dos primeiros percursos

**Pergunta de 23/10/2026:** Para a introdução, recomendo concentrar os primeiros percursos no entorno de Karakura, com locais distintos e coerentes para as fontes Quincy, Fullbringer e Hollow, além do acesso à formação Shinigami. O veterano Hollow ficaria numa área afastada da circulação civil. Isso mantém os primeiros objetivos próximos; os demais contatos preservam seus locais e requisitos próprios. Adotamos essa proximidade inicial ou começamos cada origem em regiões bem afastadas?

**Opções:** “Primeiros percursos próximos de Karakura (recomendado)” / “Regiões iniciais afastadas por origem”. **Resposta em 23/10/2026:** “Primeiros percursos próximos de Karakura (recomendado)”. Proximidade inicial aprovada; contrato/alternativa em 8.34, [catálogo M10b](10b-catalogo-funcional-de-lugares-e-rotas.md) e [histórico](historico/2026-10-23-proposta-m10-09a-primeiros-percursos-humanos.md).

### 12.31. M10-09B — DECIDIDO — Fonte da primeira rota comum a Hueco Mundo

**Pergunta em 23/10/2026:** Para a primeira rota comum até Hueco Mundo, recomendo um serviço independente com operadores Arrancar originais nos dois lados, ligando um ponto discreto perto de Karakura a um refúgio. O veterano Hollow pode indicar esse contato. O acesso depende do preparo, do contexto e da era; a volta tem atendimento próprio no refúgio. Preferimos esses operadores ou uma passagem fixa mantida por dispositivo?

**Opções:** “Operadores independentes nos dois lados (recomendado)” / “Passagem fixa mantida por dispositivo”. **Resposta em 23/10/2026:** “Operadores independentes nos dois lados (recomendado)”. Operadores independentes nas duas pontas aprovados; contrato/alternativa em 8.35, M10b/9.2/12.2, fichas de função M08a/9.38 e [histórico](historico/2026-10-23-proposta-m10-09b-primeira-rota-hueco-mundo.md).

### 12.32. M10-09C — DECIDIDO — Momento do primeiro acesso Hollow

**Pergunta em 23/10/2026:** Para a jornada Hollow, recomendo permitir essa primeira viagem após os fundamentos iniciais de sobrevivência e caça, ainda como Hollow comum. Hueco Mundo teria uma área de chegada adequada a essa etapa, com ameaças mais fortes em outras regiões. Cero, evolução Gillian e derrota de chefe não seriam requisitos dessa viagem. Preferimos esse acesso inicial ou liberar somente após virar Gillian?

**Opções:** “Acesso após fundamentos, como Hollow comum (recomendado)” / “Primeiro acesso somente como Gillian”. **Resposta em 23/10/2026:** “Precisa ser como na obra, a partir de que estágio é possivel abrir uma garganta por exemplo? Se ele for para mundo hollow precisa ser de outra forma e não com ele mesmo abrindo a garganta, o mesmo para as outras raças e o senkaimon também, não lembro a partir de quando o shinigami poderia abrir um”. Usuário exige referência da obra e reafirma primeira viagem assistida, separada da capacidade de abrir. Não escolheu entre Hollow comum e Gillian como momento de acesso assistido. **Estado naquele esclarecimento:** estágio não escolhido.

**Resposta complementar em 23/10/2026:** “Vai ser com base nas missões mesmo, ele vai para lá quando necessário e quando fizer sentido no arco da obra que reflete diretamente na campanha do mod”. **09C DECIDIDO — acesso por missão e contexto da campanha.** A primeira ida a Hueco Mundo acontece quando uma missão pertinente a exigir ou a disponibilizar no arco/era adequado. Não se adota um estágio racial universal para essa viagem: o roteiro da missão define os requisitos coerentes, inclusive estágio se for necessário naquele caso. A escolha não aprova automaticamente o acesso como Hollow comum nem uma trava geral em Gillian. Viajar como passageiro continua separado de adquirir a skill de abertura própria. Missão, marco, fonte e condições concretas serão especificados em M12/M13 com M08/M10; não são uma decisão binária ainda pendente entre Hollow comum e Gillian. Perfil vigente em 8.36, M10b/9.3/12.3 e [histórico](historico/2026-10-23-proposta-m10-09c-momento-do-acesso-hollow.md).

### 12.33. M10-10A — DECIDIDO — Conteúdo local do Rukongai

Três frentes junto a moradores/responsáveis locais — abastecimento, proteção e apuração — ou oferta concentrada em encaminhamento institucional. **Recomendação:** atividades locais que sustentem a vida própria aprovada em 05A. **Resposta em 23/10/2026:** “Atividades com moradores e responsáveis locais (recomendado)”. [Pergunta/opções integrais e alternativas](historico/2026-10-23-propostas-m10-10ab-atividades-locais.md); M10b/8.3.1/12.4. Não concede recompensas, distrito escolhido ou etapa obrigatória antes da Academia.

### 12.34. M10-10B — DECIDIDO — Conteúdo dos refúgios independentes

Reconhecimento territorial, provisões/trocas e acordos de caça/proteção, ou foco em orientação/transporte. **Recomendação:** sobrevivência e acordos locais, com funções diferentes conforme o grupo. **Resposta em 23/10/2026:** “Sobrevivência e acordos locais (recomendado)”. [Pergunta/opções integrais e alternativas](historico/2026-10-23-propostas-m10-10ab-atividades-locais.md); M10b/8.5.1/12.5. Preserva 05B, 09B/C e retorno 04A; não cria facção única, cura ou evolução automática.

### 12.35. M10-11A — DECIDIDO APÓS PESQUISA — Ambiente da Soul Society

**Pergunta em 23/10/2026:** Para o ambiente da Soul Society, recomendo ciclo de dia e noite e clima semelhante ao Overworld, com chuva e tempestades onde o perfil da região permitir. Camas marcam spawn e permitem dormir nas condições normais de horário e segurança, sem explodir; passar a noite afeta só esse mundo, como já aprovado. Isso é uma adaptação para Minecraft. Adotamos esse perfil ou mantemos iluminação diurna permanente?

**Opções:** “Ciclo de dia/noite e clima regional (recomendado)” / “Iluminação diurna permanente”. **Resposta em 23/10/2026:** “Como é na obra mesmo? eu lembro de chover e ficar de noite lá também, se for isso mesmo podemos seguir com o recomendado por você”. Condição atendida pela pesquisa WORLD-S11; recomendado adotado. Contrato/alternativa em 8.39 e [histórico](historico/2026-10-23-propostas-m10-11ab-ambiente-e-abrigo.md).

### 12.36. M10-11B — DECIDIDO — Marco de abrigo para corpo grande

**Pergunta em 23/10/2026:** Para Gillian e outros corpos incompatíveis com camas, recomendo um bloco próprio de marco de abrigo: o jogador coloca na base e interage para marcar seu renascimento, se houver espaço para o corpo. Ele ocupa a mesma escolha pessoal da cama; não teleporta nem muda o corpo, e destruí-lo aciona a recuperação já aprovada. A alternativa exige montar uma estrutura mínima de abrigo para habilitar esse registro. Qual adotamos?

**Opções:** “Marco de abrigo em bloco próprio (recomendado)” / “Estrutura mínima de abrigo”. **Resposta em 23/10/2026:** “Marco de abrigo em bloco próprio (recomendado)”. Contrato/alternativa em 8.40 e [histórico](historico/2026-10-23-propostas-m10-11ab-ambiente-e-abrigo.md).

### 12.37. M10-12A — DECIDIDO — Campanha por tipo de mundo

**Pergunta em 23/10/2026:** Sobre onde jogar a campanha completa, recomendo planejá-la tanto para mundos novos quanto para mundos existentes compatíveis. Os núcleos de Bleach seriam integrados em regiões ainda não geradas e validadas, preservando construções; o preset História organizaria a geografia desde a criação. Em saves usados, isso exige um processo próprio de integração, ainda a desenvolver. Preferimos essa abrangência ou campanha completa apenas em mundo novo criado no preset História, deixando os demais com conteúdo compatível?

**Opções:** “Campanha em mundos novos e existentes compatíveis (recomendado)” / “Campanha completa só em novo mundo História”. **Resposta em 23/10/2026:** “Campanha em mundos novos e existentes compatíveis (recomendado)”. Perfil/limites em 8.41 e [histórico](historico/2026-10-23-proposta-m10-12a-perfis-de-mundo.md).

### 12.38. M10-12B — DECIDIDO — Operadores da rota comum à Soul Society

Operadores Shinigami originais autorizados nas duas pontas da rota comum, ou serviço concentrado na rede Urahara. **Recomendação:** operadores institucionais, com competência e atendimento próprios. **Resposta em 23/10/2026:** “Operadores institucionais nas duas pontas (recomendado)”. [Pergunta e opções integrais](historico/2026-10-23-propostas-m10-12bc-operadores-e-recuperacao.md); M10b/9.5/12.6. Não decide o transporte de toda primeira missão ou concede abertura a todo Shinigami.

### 12.39. M10-12C — DECIDIDO — Áreas de recuperação e local do reset

Áreas abertas conectadas a Karakura/Rukongai/refúgio inicial, com reset próximo ao ponto humano, ou áreas nos centros dos núcleos. **Recomendação:** trechos abertos compatíveis com corpos grandes. **Resposta em 23/10/2026:** “Áreas abertas conectadas aos núcleos (recomendado)”. [Pergunta e opções integrais](historico/2026-10-23-propostas-m10-12bc-operadores-e-recuperacao.md); M10b/9.6/12.7. Aplica 08C/D sem criar viagem voluntária às áreas ou mudar as perdas/preservações do reset.

## 13. Critérios de aceitação futuros

1. Origem e primeira formação têm caminho viável sem circularidade de habilidades/itens.
2. Núcleos são estáveis no save; bordas e geração preservam construção/chunks existentes.
3. Proteção é delimitada e comunicada; bloqueia as intervenções previstas sem expandir silenciosamente à cidade inteira.
4. Acesso físico não entrega cargo, poder ou progresso narrativo de outro jogador.
5. Travessia revalida condições e espaço, sem duplicação de viajante, itens, estados ou resultado.
6. Mundo novo, mundo existente e servidor têm contratos distintos de instalação/migração antes da entrega.

Nenhum cenário executado; critérios dependem das escolhas/fichas pertinentes.

## 14. Estado técnico examinado

Lidos integralmente em 23/10/2026: [HistoryWorld](../../src/main/java/com/bleachmod/server/world/HistoryWorld.java), [HistoryMapInstaller](../../src/main/java/com/bleachmod/server/world/HistoryMapInstaller.java), [WorldTravelItem](../../src/main/java/com/bleachmod/common/item/WorldTravelItem.java), [manifesto dos mapas](../../history-maps/maps.json), [README](../../history-maps/README.md) e [preset](../../src/main/resources/data/bleachmod/worldgen/world_preset/history_mode.json). Plataforma herdada Java 1.20.1/Forge 47.4.10/Java 17; sem alteração de dependências.

- `human_world` aponta para `minecraft:overworld`; Soul Society/Hueco Mundo têm destinos próprios. O manifesto declara mapas de terceiros com posições de spawn; isso não prova fidelidade, extensão utilizável ou segurança de chegada.
- Preset de História inclui Nether/End e geradores planos/vazios para partes dos destinos. Não comprova os arredores gerados aprovados em 01A.
- Instalador usa manifesto salvo e cópia inicial, recusa chunks existentes na instalação nova e tem retomada marcada que limpa pastas sob seu controle. Esse fluxo não é prova de migração segura de construções em saves usados; nenhum caminho foi executado nesta rodada.
- Transporte provisório resolve destino no servidor, recusa alguns estados, redefine queda e teleporta ao spawn. Não demonstra os contratos propostos de acesso, travessia comprometida ou validação espacial; comportamento de desenvolvimento não aprova item final ou imunidade de queda.

Não foi aberta uma sessão de Minecraft nem feita inspeção visual dos mapas. Nenhum mapa, save, preset, item, código ou licença foi alterado; documentos não são evidência de teste/aceite.

## 15. Próximos passos

**Resultado atual — 23/10/2026:** checkpoint de revisão/complementos concluído no alcance de 12.1 do parecer de cobertura, com M09-12C decidido, integração M08/M09 e ligações de volta em M10b/9.4.1. M10 permanece `EM ESPECIFICAÇÃO` de conteúdo; próxima etapa principal M11 com retorno M05, após publicação documental autorizada. Os encaminhamentos abaixo preservam a sequência anterior; presença/Combat State, ambiente de Soul Society e representação de aldeões já receberam as decisões posteriores e não devem ser propostos novamente.


**Continuidade após 12B/C:** escolhas aplicadas em M10b/M08a; percursos conferidos documentalmente em 8.43. Retorno dirigido de [M04a/presença e interação](04a-presenca-espiritual-e-interacao.md): RAC-30A/B/C/D decididos. Corpo/local do reset em M04/RAC-31A decidido: recomeço no atendimento do NPC, com estado inicial próprio e validação antes de apagar a jornada. Combat State em M06a consolidado conceitualmente com CS-01A/B/C/D aprovados. Atual: complementos M08/M09, com direção canônica M09-12A/B aplicada e representação de aldeões M09-12C proposta, antes de revisar/publicar o bloco. M10 mantém fichas/implantação e contingências explícitas; nenhuma promessa de percurso executável.

**Escolhas do complemento:** 12A/B/C decididos; respostas aplicadas em fichas e M08. Revisar os elos de retorno de M10b/9.4 com os retornos prioritários M04/presença espiritual e M06/Combat State antes do fechamento do bloco e da publicação. Contingências de viagem, acesso/era e conteúdo individual continuam nas respectivas fichas; não declarar completude executável.

**Perfil de mundo:** M10-12A decidido sobre abrangência da campanha por tipo de mundo, 8.41/12.37; sem pergunta pendente desse perfil. 10A/B e 11A/B decididos, com condição de 11A satisfeita por WORLD-S11. Continuar fichas de lugares/serviços, retorno e recuperação e os retornos dirigidos; não considerar toda M10 concluída pela seleção destes perfis.

**Ambiente e abrigo:** M10-11A decidido após pesquisa da condição e 11B decidido por resposta direta, em 8.39/8.40 e 12.35/12.36; sem pergunta pendente dessas duas escolhas. 10A/B decididos; M10b/8.7/8.8 compõe funções de instituições e atendimentos sem encerrar as respectivas fichas individuais. Com os perfis adotados, continuar locais/serviços, recuperação/retorno e modos de mundo, antes dos retornos de M04/M06 e publicação.

**Atividades locais:** M10-10A/B decididos, sem pergunta pendente; respostas em M10b e histórico. Frentes aprovadas orientam as fichas de conteúdo; seguir com as demais entregas do bloco de revisão/publicação. A composição institucional de M10b/8.7 deriva das funções aprovadas, com conteúdo individual ainda a completar em M08.

**Prioridade após a revisão de cobertura — 23/10/2026:** completar funções/atividades/acesso dos lugares a partir de M10b/7.1, perfis ambientais, circulação/retorno e recuperação; depois fechar o contrato de experiência por modo de mundo. [Achados, responsáveis e momentos de retorno](00-revisao-cobertura-m01-a-m10-2026-10-23.md). A revisão não fecha M10 nem altera aprovações anteriores. M12/M13 continuam responsáveis pelas missões concretas.

07F/G decididos: uma passagem própria comum ativa com fechamento manual; ida e volta durante a janela sem renová-la. Bloco comum de passagens consolidado conceitualmente em 8.25 e [revisão](10a-revisao-m10-2026-10-23.md); requisitos de implementação identificados, sem declarar ficha executável completa. M10 permanece em especificação. 08A decidido por aprovação condicionada à pesquisa: água transportada funcional como adaptação. 08B decidido: ponto de renascimento no mundo escolhido, com cama ou abrigo compatível. 08C decidido: busca próxima válida, recuperação local e alternativa no Mundo Humano na falha do ponto. 08D decidido: morte sem ponto escolhido e preservação/correção da posição no retorno. Bloco espacial consolidado conceitualmente em 8.30. 08E decidido no recorte Hueco Mundo: dormir a qualquer horário quando os demais requisitos permitirem, preservando noite permanente e spawn marcado pela cama. 08F decidido: passagem da noite por mundo com ciclo, preservando os relógios de combate. 08G decidido: criaturas por região/identidade, com monstros vanilla espirituais somente por ficha. Catálogo M10b aberto em especificação; 09A decidiu proximidade dos primeiros percursos no entorno de Karakura. M10b/9 elabora encaminhamento institucional e primeira rota comum a Hueco Mundo; 09B decidiu operadores independentes nas duas pontas, com funções em M08a/9.38. 09C decidido por resposta complementar: primeira ida orientada por missão e contexto do arco/campanha; as alternativas de estágio universal não foram adotadas. Pesquisa WORLD-S09 preserva os limites sobre abertura própria. Perfis ambientais restantes, inclusive céu/sono de Soul Society, permanecem por ficha sem aprovação inferida. Próximo: revisar as fichas funcionais de lugares/rotas com o acesso narrativo de 09C e demonstrar chegada/retorno; missões concretas e seus marcos ficam em M12/M13, sem nova votação de estágio universal. Perfis ambientais restantes seguem como entregas explícitas; abrir catálogo não os aprova nem encerra M10. Áreas de recuperação, geometria, parâmetros de busca, UI e carregamento são entregas com responsáveis; não exigem repetir as aprovações gerais. 07A–E, 06A, 05A/B/C e demais decisões preservados; 03A revisado por 03D. Perguntas seguem sem novo pedido de avanço. Nenhuma mudança de runtime, mapa/save, build, teste ou publicação.
