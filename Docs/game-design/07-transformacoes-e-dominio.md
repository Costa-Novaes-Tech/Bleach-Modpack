# Transformações e Domínio

> **Integração de Combat State — 23/10/2026:** [M06a/CS-01A/B/C/D](06a-estado-de-combate.md) decidido. Evolução e Arrancarização que exigem estar fora de combate usam o estado automático, sem confundir Combat Mode. Risco/dano em curso incompatível também impede a operação, mesmo em estado OFF; revalidar os requisitos conforme sua execução aprovada. Isso não cria exigência universal de fora de combate para Shikai/Bankai/Resurrección/Vollständig ou outra forma permitida em luta. Limpeza de formas e custos/requisitos anteriores preservados; nenhum código ou teste.

> **Direção vigente REV-SP-02 — 22/10/2026:** Spiritual Points compram **skills e transformações**, conforme esclarecimento explícito do usuário, usando Dragon Mine Z como referência para a compra. **Mastery é desenvolvida pela prática e não é comprada.** Requisitos/contexto disponibilizam a aquisição; SP paga a compra/desbloqueio; usar a capacidade depois segue os custos próprios de Energia/Stamina. [Contrato comum em M05/11.2](05-atributos-e-recursos.md#112-rev-sp-02--compra-de-skills-e-transformações-com-spiritual-points). Preços e exceções concretas permanecem nas fichas; a compra não pode ser removida genericamente por interpretação de M05/M06.

> **Continuidade revisada — 22/10/2026:** M07 permanece especificado conceitualmente. Antes de iniciar M08, revisar e concluir a pendência de criação/continuidade encontrada em M04, conforme solicitação do usuário e [Revisão transversal de M01–M07](00-revisao-modulos-01-a-07-2026-10-22.md). Não reabre por si só as aprovações de transformações.

## 1. Estado do módulo

| Campo | Valor |
|---|---|
| Estado | `ESPECIFICADO CONCEITUALMENTE` |
| Responsável pela decisão | Equipe do Bleach Mod; decisões de produto pelo usuário |
| Abertura e última revisão | Abertura 21/10/2026; revisão vigente 22/10/2026 |
| Entradas | M04, M05 e M06; revisões vigentes prevalecem sobre descrições antigas |
| Blocos consolidados | Shikai M07-02A–J; Bankai M07-03A–N; evolução Hollow M07-04A–M; Arrancarização M07-05A–J; Resurrección M07-06A–Q; Vollständig M07-07A–R; Letzt Stil M07-08A–C; Fullbringer M07-09A–C; G01/G02 e revisão comum |
| Decisões aprovadas | M07-02A–J, M07-03A–N, M07-04A–M, M07-05A–J, M07-06A–Q e M07-07A–E em 21/10/2026; M07-07F–R, M07-08A–C, M07-09A–C e regras gerais M07-G01/G02 em 22/10/2026, por respostas explícitas |
| Perguntas pendentes dos blocos consolidados | Nenhuma; fichas, números, conteúdo e realização técnica continuam com responsáveis |
| Perguntas vigentes | Nenhuma pergunta geral pendente após C e revisão comum; fichas/efeitos/exceções futuros exigem seu próprio escopo |
| Ponto de continuidade | Fechamento conceitual na seção 15; avançar M08 — NPCs, mentores e facções |
| Próxima sequência | M08 → módulos seguintes do mapa; completar fichas e integrações nos responsáveis antes de qualquer entrega executável |
| Implementação, teste e aceite | Não avançados por esta documentação |

M06 está `ESPECIFICADO CONCEITUALMENTE`; sua [seção 48](06-habilidades-e-combate.md#48-fechamento-conceitual-de-m06--21102026) distribui as entregas restantes. Esta abertura não encerra essas pendências nem começa o catálogo de linhagens ou artes.

## 2. Objetivo

Definir o que o personagem conquista, quando pode manifestar esse poder, como desenvolve domínio e o que ocorre ao encerrar ou perder uma manifestação. Evolução, aquisição e uso precisam ter contratos distintos, sem tornar todas as raças uma sequência igual de formas.

## 3. Fantasia do jogador

Conquistar um novo estágio deve mudar as possibilidades do personagem de maneira reconhecível. Aprender a utilizá-lo faz parte da jornada; o jogador precisa entender a diferença entre ainda não ter conquistado o poder, estar sem condições de usá-lo agora e estar aprendendo a dominá-lo.

Construção, exploração e convivência no mundo continuam parte dessa experiência. Uma troca de interface não pode servir para apagar o compromisso de uma execução ou transformar um estágio permanente em estado temporário.

## 4. Base canônica

**Consulta: 21/10/2026.** Referência editorial oficial do mangá e fontes oficiais do anime, conforme D05; o mangá permanece a base. Textos japoneses interpretados; não houve leitura integral de capítulos, episódios ou novels nesta abertura.

| Fonte | Trecho efetivamente utilizado | Consequência e limite |
|---|---|---|
| [BL-12 — Pierrot, glossário sa](https://pierrot.jp/title/bleach/keyword_sa.html) | Entradas 始解 e 斬魄刀: primeira liberação, diálogo/sintonia e conhecimento do nome; vínculo com a espada | Fundamenta o aprendizado de Shikai. Não comprova prova, comandos ou custos do mod. O arquivo registra informações da época de exibição; sua descrição geral da origem da espada não substitui as revelações posteriores sobre Asauchi. |
| [BL-11 — Pierrot, glossário ha](https://pierrot.jp/title/bleach/keyword_ha.html) | Entrada 卍解: segunda liberação e relação com manifestação do espírito | Localiza uma conquista posterior, sem importar requisito de cargo universal ou decidir o processo jogável de Bankai. |
| [BL-03 — glossário oficial TYBW](https://bleach-anime.com/keyword/) | Entradas 刀剣解放, 聖文字, 滅却師完聖体 e 卍解 | Resurrección libera poder Hollow selado; Schrift é capacidade individual; Vollständig é distinto de Letzt Stil; Bankai tem manifestação e exigência espiritual próprias. Não demonstram acesso, custos ou coexistência universais do mod. |
| [BL-17 — Pierrot, retrospectiva 343–366](https://pierrot.jp/title/bleach/playback_16.html) | Trechos de aquisição, treinamento e completude do Fullbring de Ichigo | Sustenta separar conquistar e desenvolver. A experiência excepcional de Ichigo não determina o currículo ou as etapas de todos os Fullbringers. |

O glossário TYBW é mutável. Foram usadas somente as entradas indicadas; novidades promocionais, outras formas e exceções não foram importadas como regra do projeto. Sua referência a uma segunda liberação Arrancar não demonstra acesso universal a Segunda Etapa. Multiplicadores narrativos não substituem a decisão M05 de não adotar multiplicador universal de formas.

Pesquisa de evolução Menos, consequências específicas, poderes e técnicas será aprofundada antes dos respectivos blocos. As classificações abaixo consolidam decisões locais; não afirmam que a obra utilize essa arquitetura de dados.

### Aprofundamento para Bankai — 21/10/2026

Reconsultados BL-03, entrada `卍解`, e BL-11, mesma entrada: materialização e domínio sobre a manifestação do espírito. BL-03 também descreve exigência espiritual para manter Bankai e distingue a condição institucional de capitão. Essa referência não estabelece uma drenagem numérica, prova jogável ou regra universal de duração do mod.

Acrescentada [BL-23 — Pierrot, retrospectiva dos episódios 42–63](https://pierrot.jp/title/bleach/playback_03.html), trechos do treinamento de Ichigo com Zangetsu e da conquista de Renji. A sinopse mostra contexto de aprendizado; o prazo excepcional de Ichigo não será presumido como cronômetro universal. Consultada também a [sinopse TV Tokyo do episódio 52, republicada em 03/07/2024](https://www.tv-tokyo.co.jp/anime/bleach_bestbout/episodes/index_2.html): treino de Ichigo e uso por Renji. Data da republicação, não da estreia original.

São resumos oficiais do anime, interpretados do japonês; não foi lido capítulo ou episódio integral nesta rodada. Não foram incorporadas outras entradas promocionais do glossário. As propostas abaixo traduzem a relação com o espírito em progressão jogável; as fontes não comprovam seus requisitos, confiabilidade ou indicadores de domínio.

### Quebra de Bankai — pesquisa de 21/10/2026

Reconsultada BL-03, entrada `破壊された卍解`: descreve danos persistentes em Ryūmon Hōzukimaru/Hihiō Zabimaru e a recuperação particular de Kokujō Tengen Myō'ō ligada ao corpo de Komamura. O trecho não fundamenta reparo comum universal nem ausência absoluta de exceções. Seu “atualmente” pertence ao recorte da entrada, não a uma revisão exaustiva de todas as publicações até hoje.

[BL-24 — sinopse oficial TYBW 08, THE SHOOTING STAR PROJECT 〔ZERO MIX〕](https://bleach-anime.com/story/?id=8) situa a explicação de Mayuri após Tensa Zangetsu ser quebrada. É sinopse, não episódio integral; não demonstra um sistema universal de recuperação. Não foi lido o capítulo do mangá. Durabilidade vanilla, dissipação normal, fragmento visual e dano estrutural precisam de contratos distintos no mod. M/N são adaptações aprovadas do mod, não regras canônicas universais de reparo.

### Evolução Hollow — pesquisa de 21/10/2026

[BL-25 — Pierrot, glossário ma](https://pierrot.jp/title/bleach/keyword_ma.html), entradas `メノス`/`大虚`, relaciona a formação Menos à agregação de Hollows, a progressão à consciência dominante e descreve regressão Adjuchas. [BL-26 — glossário a](https://pierrot.jp/title/bleach/keyword_a.html), entradas `中級大虚`/`上級大虚`, distingue os estágios. Reconsultada a [sinopse oficial TV Tokyo do episódio 167](https://www.tv-tokyo.co.jp/anime/bleach_bestbout/episodes/), republicação de 14/08/2024: passado Adjuchas de Grimmjow e predação. As datas do arquivo não são as estreias originais.

São resumos oficiais do anime interpretados do japonês, relativos ao seu recorte de exibição; não capítulos ou episódios integrais. A descrição narrativa de raridade/aptidão não estabelece sorte na criação do personagem nem contagens de jogo. A ausência de regressão passiva e a agência Gillian são adaptações já aprovadas em M04/M06, não reprodução literal de todas as condições descritas. Esses arquivos não confirmam forma Vasto Lorde específica de Grimmjow. BL-03, entrada `虚圏`, distingue nutrição ambiental; respirar Reishi não demonstra evolução automática. A fonte também não determina consumo por interação, crédito de party ou início manual propostos nesta rodada.

### Arrancarização — pesquisa de 21/10/2026

[BL-27 — Shonen Jump, apresentação do arco Arrancar](https://www.shonenjump.com/xmedia/bleach/story/index4.html), verbete `破面`: retirada da máscara visando capacidades de Shinigami, Zanpakutō e remanescentes da máscara. É uma apresentação editorial oficial ligada ao mangá, não leitura de capítulo integral; a numeração citada pertence ao grupo reunido por Aizen, não a todo Arrancar independente.

Reconsultada [BL-26 — Pierrot, glossário a](https://pierrot.jp/title/bleach/keyword_a.html), entrada `破面`: relação com o Hōgyoku, núcleo das capacidades Hollow selado na arma e liberação da verdadeira capacidade/forma. Reconsultada [BL-11 — glossário ha](https://pierrot.jp/title/bleach/keyword_ha.html), entrada `崩玉`: contexto da intervenção sobre a fronteira entre Hollow e Shinigami. São verbetes oficiais do anime referentes à época de exibição; descrições antigas de funcionamento, escala ou multiplicador não viram fórmula do mod.

A busca também recuperou o texto indexado de BL-03/Arrancar, mas a abertura direta da página retornou 403 nesta rodada. Não foi usada como nova leitura integral nem como fundamento decisivo para o processo. As páginas acessíveis não descrevem um currículo jogável completo, nem demonstram que remover qualquer pedaço visual de máscara sempre basta ou que todo Arrancar necessariamente pertence a Aizen. Não se afirma, a partir delas, um procedimento autônomo universal ou a lista completa dos personagens que dispensaram Hōgyoku.

A/B registram a tradução jogável da conquista e do acesso, aprovada por respostas próprias abaixo; a via pessoal é adaptação explícita. C traduz a relação entre Arrancar e sua própria arma, sem importar aquisição de Asauchi, conceder todas as técnicas ou equiparar a transição à primeira Resurrección.

### Corpo-base e continuidade Hollow → Arrancar — pesquisa de 21/10/2026

Reconsultado BL-26, entrada `上級大虚`: o arquivo oficial relaciona a aparência Arrancar à evolução anterior e descreve traços Hollow remanescentes com maior frequência abaixo de Vasto Lorde. É uma generalização do anime em seu recorte de exibição, não uma regra atual exaustiva nem uma ficha corporal. Por isso, garantir uma base humanoide jogável para as três origens em D é uma adaptação explícita, sem alegar que todos os Arrancars canônicos tenham corpo humano idêntico.

Reconsultado BL-10, ficha de Aaroniero: origem Gillian e capacidade individual de Glotonería. Isso distingue origem, posição e poder; não demonstra que todo Arrancar de origem Gillian absorva técnicas, tenha o mesmo corpo ou possa acessar qualquer linhagem. BL-27 sustenta arma e fragmentos de máscara, sem determinar tamanho, colisão, equipamentos ou currículo de adaptação corporal.

A busca de BL-03 recuperou o trecho indexado sobre perda geral de regeneração rápida; a fonte já tinha limitação de abertura direta registrada acima. BL-14 também apareceu no índice com a capacidade particular de Ulquiorra, sem nova leitura integral da ficha nesta rodada. Esses trechos corroboram a cautela já aprovada em M06-08J: E preserva conhecimento/domínio, mas não garante a função de uma capacidade incompatível. Não foi localizado nos textos um procedimento universal de conversão de Proficiência/Mastery; esses indicadores pertencem ao mod.

### Consequências das origens Arrancar — pesquisa de 21/10/2026

Reconsultados [BL-26 — Pierrot](https://pierrot.jp/title/bleach/keyword_a.html), entradas `中級大虚`, `破面` e `上級大虚`, [BL-10 — TV Tokyo](https://www.tv-tokyo.co.jp/contents/bleach_test2/chara/index05.html), ficha de Aaroniero, e [BL-27 — Shonen Jump](https://www.shonenjump.com/xmedia/bleach/story/index4.html), verbete Arrancar. O glossário relaciona capacidade anterior do Hollow e força após Arrancarizar; distingue os estágios Menos. Não sustenta tratar todos os personagens como iguais no momento da passagem. Aaroniero demonstra a existência de um Arrancar de origem Gillian entre os Espada; sua absorção de poderes é capacidade individual, não compensação racial universal.

As fontes não fornecem tetos de atributos por origem, curvas de crescimento equivalentes ou uma tabela de vantagens exclusivas para cada classe. F é uma adaptação aprovada em resposta própria: preservar preparo e capacidades compatíveis, permitir desenvolvimento posterior e dar valor à escolha do momento, sem converter afirmações narrativas em multiplicadores ou punições permanentes. G também é adaptação aprovada em resposta própria: custo de recursos de jogo não é inferido desses verbetes. Fontes oficiais resumidas, japonesas e interpretadas, sem leitura de capítulo/episódio integral; os arquivos do anime mantêm seu recorte histórico. BL-27 não acrescenta regra de poder máximo.

### Primeira Resurrección — pesquisa de 21/10/2026

Reconsultado [BL-19 — Pierrot, glossário ra](https://pierrot.jp/title/bleach/keyword_ra.html), entrada `帰刃 / レスレクシオン`, linhas 57–61 da extração: retorno das capacidades ofensivas Hollow ao corpo do Arrancar. Reconsultado [BL-26 — glossário a](https://pierrot.jp/title/bleach/keyword_a.html), entrada `破面`, linhas 22–24: núcleo das capacidades selado na arma, com liberação da verdadeira capacidade/forma. São arquivos oficiais do anime que declaram informações relativas à época de exibição; texto japonês interpretado, sem episódio ou capítulo integral. Não importar o multiplicador narrativo do arquivo como fórmula de atributos.

A abertura direta de [BL-03 — glossário TYBW](https://bleach-anime.com/keyword/) retornou 403. A busca recuperou o texto indexado da entrada `刀剣解放`, que relaciona arma, capacidades Hollow e verdadeira forma. Isso é recuperação indexada dessa entrada, não nova leitura direta integral do site ou de uma obra. A semelhança de princípio com Bankai descrita ali não comprova currículo, custo, manutenção ou reversão iguais no jogo. A menção à segunda etapa não autoriza acesso universal a ela.

Essas referências sustentam a identidade da liberação, mas não estabelecem uma prova jogável, uma batalha com espírito independente, um prazo, uma exigência universal de posto ou uma Mastery numérica. A/B abaixo são adaptações de aquisição e domínio do mod, aprovadas por duas respostas próprias em 21/10/2026. A liberação não será tratada, por omissão, como retorno exato ao perfil Hollow que o personagem usava antes de Arrancarizar; formas e capacidades concretas exigem fonte e ficha próprias. Esta abertura não pesquisa artes ou seleciona uma linhagem nova.

### Ativação e recursos de Resurrección — pesquisa de 21/10/2026

Reconsultados [BL-19/Pierrot, entrada帰刃](https://pierrot.jp/title/bleach/keyword_ra.html) e [BL-26/Pierrot, entrada破面](https://pierrot.jp/title/bleach/keyword_a.html): capacidades Hollow liberadas no corpo e núcleo selado na arma. A busca também recuperou texto indexado de BL-03/刀剣解放; não houve nova abertura direta bem-sucedida do glossário TYBW nesta rodada. Esses trechos não estabelecem uma cobrança universal por ativação/manutenção, um tempo de duração de gameplay ou um comando de interface. A busca dirigida sobre gasto não acrescentou referência decisiva de custo; não apresentar ausência nesses verbetes como prova sobre todas as cenas da obra.

C/D são adaptações de operação e gerenciamento de recurso do mod, aprovadas por duas respostas próprias em 21/10/2026. A similaridade com regras Shinigami já aprovadas não as torna automaticamente aplicáveis. Arquivos oficiais do anime relativos à época de exibição e recuperação indexada limitada, textos japoneses interpretados; sem capítulos/episódios integrais. Não foram pesquisadas novas APIs ou artes. Exigência inicial de empunhadura é uma regra básica aprovada do mod; manifestações especiais da arma precisam de fonte e ficha do poder, sem afirmar que todo Arrancar canônico libera uma katana comum.

### Preparo e encerramento de Resurrección — pesquisa de 21/10/2026

Reconsultados [BL-19/Pierrot, entrada帰刃](https://pierrot.jp/title/bleach/keyword_ra.html), linhas 57–61, e [BL-26/Pierrot, entrada破面](https://pierrot.jp/title/bleach/keyword_a.html), linhas 22–24. Relacionam liberação, corpo e capacidades seladas na arma; não especificam as permissões de reação, o tempo de preparo ou a recuperação de gameplay propostos nesta rodada. São verbetes oficiais do anime com informação relativa à época de exibição, sem leitura de capítulo/episódio integral. E/F/G são adaptações aprovadas por três respostas próprias em 21/10/2026, não uma regra universal de reversão comprovada por esses verbetes. Nenhuma nova API, arte ou linhagem pesquisada.

### Corpo, arma e espaço de Resurrección — pesquisa de 21/10/2026

Reconsultados BL-19/帰刃 e BL-26/破面. Consulta dirigida de [BL-14 — Pierrot, fichas Espada](https://pierrot.jp/title/bleach/chara_06.html): Tiburón, linhas 84–90, descreve a arma cobrindo a mão direita; Gamuza, linhas 104–110, descreve corpo semelhante a centauro com arma semelhante a lança. Os exemplos sustentam diversidade entre corpo e armamento, sem escolher essas linhagens para a primeira entrega ou definir mãos/slots do mod. Arquivo oficial do anime relativo à época de exibição, texto japonês interpretado, sem episódio/capítulo integral.

A abertura de BL-05/Characters JP retornou 403 nesta rodada; não foi usada como evidência decisiva. Resultados de jogos licenciados e páginas secundárias não fundamentam as regras propostas. H/I/J são adaptações de inventário, interação e espaço aprovadas por três respostas próprias em 21/10/2026, não fatos canônicos. Não foram pesquisadas APIs, medidas finais ou artes.

### Meios e posse da arma em Resurrección — pesquisa de 21/10/2026

Reconsultados [BL-19/Pierrot, entrada帰刃](https://pierrot.jp/title/bleach/keyword_ra.html), linhas 57–61, e [BL-26/Pierrot, entrada破面](https://pierrot.jp/title/bleach/keyword_a.html), linhas 22–24. Consulta dirigida de [BL-14/Pierrot](https://pierrot.jp/title/bleach/chara_06.html), entradas Tiburón/Gamuza, linhas 84–110: preserva a distinção entre armamento corporal e separado. As fontes relacionam corpo, liberação e arma; não demonstram regras universais de apoio, inventário, transferência ou retorno de item do mod. São resumos oficiais do anime relativos à época de exibição, interpretados do japonês; sem capítulo/episódio integral. K/L/M são adaptações aprovadas por respostas próprias em 21/10/2026, não conclusões canônicas. Não foram consultadas novas APIs ou selecionadas artes/linhagens.

### Equipamento, presença e pagamentos de Resurrección — pesquisa de 21/10/2026

Reconsultados [BL-26/Pierrot, entradas破面 e鋼皮](https://pierrot.jp/title/bleach/keyword_a.html), linhas 22–33, e [BL-19/Pierrot, entrada帰刃](https://pierrot.jp/title/bleach/keyword_ra.html), linhas 57–61. Descrevem capacidades seladas/liberadas e Hierro como defesa corporal. Não estabelecem slots de armadura de Minecraft, supressão simultânea, custos ou ordem de pagamentos. N/O/P aprovados em 21/10/2026; N após explicação e escolha própria, O/P por duas respostas anteriores. São adaptações, não regras canônicas demonstradas pelos verbetes. Fontes oficiais do anime, relativas à época de exibição, interpretadas do japonês; sem capítulo/episódio integral. Sem consulta de APIs ou inclusão de poderes/artes novos.

### Ciclo de vida e efeitos de Resurrección — preparação de 21/10/2026

Reconsultados [BL-19/Pierrot, entrada帰刃](https://pierrot.jp/title/bleach/keyword_ra.html), linhas 57–61, e [BL-26/Pierrot, entrada破面](https://pierrot.jp/title/bleach/keyword_a.html), linhas 22–24: relação entre capacidades Hollow, corpo e arma. São arquivos oficiais do anime relativos à época de exibição; texto japonês interpretado, sem capítulo/episódio integral. Não descrevem login, reinício, descarregamento de região ou regras de inventário de Minecraft. Q é adaptação do mod aprovada por resposta própria em 21/10/2026; não equipara morte de gameplay ao destino canônico de uma alma.

M06-11U/V/W, M07-06F/G/H/L/M/O/P e M07-05J foram relidos nos contratos pertinentes. O comportamento geral dos disparos autônomos e efeitos dependentes já foi aprovado; a seção 12.1 o aplica ao encerramento da forma sem nova votação. Q aprova a política específica da manifestação no ciclo de vida na seção 8.68; o registro da resposta não exigiu nova consulta externa. Sem pesquisa de APIs, catálogo novo, implementação ou testes.

### Abertura Quincy — pesquisa de 21/10/2026

[BL-16 — Pierrot, glossário ka](https://pierrot.jp/title/bleach/keyword_ka.html), entrada `滅却師最終形態`, linhas 97–101: Letzt Stil relaciona treino com a luva Sanrei, retirada e perda posterior de poder; o texto também descreve a recuperação de Uryū. Reconsultado [BL-23 — retrospectiva 42–63](https://pierrot.jp/title/bleach/playback_03.html), trecho de Uryū/Mayuri: poder extremo com perda das capacidades Quincy. São resumos oficiais do anime relativos à época de exibição, não capítulos/episódios integrais. Prazos e procedimento descritos não viram duração de treino, alvo anatômico ou regra universal de recuperação do mod.

[BL-03 — glossário TYBW](https://bleach-anime.com/keyword/), entradas `滅却師完聖体` e `聖文字`, recuperadas pelo índice de busca após abertura direta retornar 403: Vollständig é distinto de Letzt Stil e potencializa capacidades; Schrift designa poder individual, ligado ao mecanismo de Yhwach, com exceções citadas no próprio glossário. Isso não estabelece o currículo do jogador ou uma relação universal de pré-requisitos entre forma, poder e facção. Consulta dirigida de [BL-28 — entrevista oficial com Kōichi Yamadera, voz de Quilge](https://bleach-anime.com/special/interview/04.html), passagem sobre a atuação: distingue Vollständig, Sklaverei e The Jail no mesmo personagem. Entrevista não substitui cena ou demonstra aprendizado universal. Textos japoneses interpretados; não incorporar todo o glossário mutável nem técnicas individuais por associação.

A sinopse oficial do episódio 3 foi consultada como contexto, mas não descreve um currículo de Vollständig; a abertura da sinopse do episódio 14 retornou 403 e não foi usada como base decisiva. Sem novos capítulos integrais, novels, artes ou APIs consultados. A/B são adaptações aprovadas por duas respostas próprias em 21/10/2026, seções 8.70/8.71; não são regras canônicas demonstradas sobre acesso ou custos. O registro das respostas não acrescentou consulta externa.

### Relação Vollständig/Schrift e acesso ao conhecimento — preparação de 21/10/2026

Reconsulta de [BL-03, entradas Vollständig/Schrift/Sternritter](https://bleach-anime.com/keyword/) pelo índice de busca, após abertura direta retornar 403. O texto relaciona Vollständig à ampliação das capacidades da Schrift e situa esses poderes na elite Quincy; isso não demonstra uma ordem universal de aprendizado ou a exigência de filiação permanente. C aprova a ordem do catálogo do mod, identificada como adaptação por resposta própria em 21/10/2026. A apresentação do mecanismo de Yhwach não autoriza tratar qualquer rival como transmissor literal de sua Schrift nem generalizar as exceções citadas pelo glossário.

Reconsultado [BL-28, entrevista de Quilge](https://bleach-anime.com/special/interview/04.html): o exemplo reúne forma e poder individual, sem fornecer um currículo. A sinopse oficial do episódio 14 foi recuperada no índice apenas como contexto institucional, sem evidência de ensino; a página de personagens em inglês retornou 403 e não fundamenta a proposta. Textos japoneses interpretados; sem capítulo/episódio integral. D, com fontes de conhecimento e ausência de vínculo permanente, é adaptação aprovada por resposta própria em 21/10/2026. Não foi confirmada uma via canônica universal de registros/mentores independentes de Vollständig; o conteúdo precisará justificar sua fonte. Sem nova pesquisa de APIs.

### Confiabilidade e domínio de Vollständig — preparação de 21/10/2026

Reconsulta dirigida de [BL-03 — glossário TYBW](https://bleach-anime.com/keyword/), entrada `滅却師完聖体`, recuperada pelo índice de busca; abertura direta retornou 403. A entrada distingue Vollständig de Letzt Stil e relaciona a forma ao poder individual. [BL-28 — entrevista de Quilge](https://bleach-anime.com/special/interview/04.html) teve abertura direta, mas a extração não recuperou nesta rodada o trecho específico; o índice de busca recuperou a passagem já registrada, distinguindo Vollständig, Sklaverei e The Jail. Não declarar nova leitura direta desse trecho ou de episódio/capítulo integral.

Esses materiais não fornecem uma regra universal de confiabilidade após treinamento, sorteio de falha ou Mastery de gameplay. E é proposta de adaptação, apoiada na separação local entre conquista e domínio; não uma conclusão canônica extraída da ausência de detalhes. Sem pesquisa nova de APIs, artes ou poderes concretos.

### Ativação e recursos de Vollständig — preparação de 21/10/2026

Reconsultado [BL-03 — glossário TYBW](https://bleach-anime.com/keyword/), entrada `滅却師完聖体`, pelo índice após abertura direta retornar 403: distingue a forma de Letzt Stil e relaciona a manifestação às capacidades individuais. [BL-28 — entrevista de Quilge](https://bleach-anime.com/special/interview/04.html) teve abertura direta; o trecho específico foi recuperado no índice de busca, distinguindo forma, Sklaverei e The Jail. Esses materiais não especificam comando de interface, cobrança por ativação/manutenção ou duração universal de gameplay. Não foram lidos capítulos/episódios integrais ou usadas páginas secundárias como fundamento.

F/G são propostas de adaptação operacional e de recursos. Não deduzir delas uma lei canônica de gasto pessoal por segundo nem equiparar as taxas a Bankai/Resurrección. Reishi ambiental conserva o contrato M05-03E/seções 41–42: propriedade do ambiente que pode influenciar eficiência/regeneração, sem segunda barra ou coleta manual contínua. Isso não define abastecimento ilimitado, Sklaverei automática ou consumo de blocos/entidades. Sem pesquisa nova de APIs ou artes.

### Equipamento e mãos em Vollständig — preparação de 22/10/2026

Reconsultado diretamente [BL-16 — Pierrot, glossário ka](https://pierrot.jp/title/bleach/keyword_ka.html), entradas `滅却十字` (linhas 86–90) e `滅却師最終形態` (97–101): identifica a Cruz Quincy e associa a luva Sanrei à técnica antiga e sua retirada. O arquivo informa refletir a época de exibição. Não transportar esse procedimento, seu prazo ou recuperação para Vollständig.

Reconsultado [BL-03 — glossário TYBW](https://bleach-anime.com/keyword/) pelo índice, entradas dirigidas de Vollständig e Sanrei; abertura direta retornou 403. O trecho pertinente distingue Vollständig de Letzt Stil. Não foi confirmada nessas fontes uma exigência instrumental universal para toda Vollständig; essa limitação não prova ausência de instrumentos ou gestos específicos em todas as cenas. H propõe uma regra geral de adaptação, com exceções justificadas por ficha, sem inventar um equipamento avançado obrigatório a partir da luva antiga. Não incorporar as demais entradas recentes da página por associação.

I propõe como a forma convive com itens de Minecraft; as fontes não determinam slots, duas mãos, ferramentas, comida ou sincronização do mod. O arco inicial mantém M06-09J/L. Sem leitura de capítulo/episódio integral, pesquisa nova de APIs, catálogo de armas ou seleção de artes.

### Preparo e encerramento de Vollständig — preparação de 22/10/2026

Reconsultado [BL-03 — glossário TYBW](https://bleach-anime.com/keyword/), entrada dirigida de Vollständig, pelo índice após abertura direta retornar 403. Distingue a forma da técnica antiga e a relaciona às capacidades individuais. [BL-28 — entrevista de Quilge](https://bleach-anime.com/special/interview/04.html), passagem recuperada pelo índice nesta rodada, distingue Vollständig, Sklaverei e The Jail; entrevista não é demonstração completa de execução.

Esses trechos não especificam caminhada, cancelamento para reação, recuperação de gameplay ou retorno voluntário universal. J/K/L são propostas de adaptação, sem transportar a perda de poder de Letzt Stil para o fim de Vollständig. Mangá segue a base de D05; não foi lido capítulo/episódio integral nesta rodada. A semelhança com Shikai/Bankai/Resurrección não aprova automaticamente as regras Quincy. Sem nova pesquisa de APIs ou artes.

### Vollständig, Blut e presença — preparação de 22/10/2026

Reconsultado [BL-03 — glossário TYBW](https://bleach-anime.com/keyword/) pelo índice em entradas dirigidas de `血装` e `滅却師完聖体`, após abertura direta retornar 403. Blut distingue aplicações ofensiva/defensiva e exclui seu uso simultâneo; Vollständig é uma manifestação distinta relacionada ao poder individual. Esses verbetes não fornecem contrato completo de coexistência, ocultação ou ordem de cobranças do mod. Não usar a enumeração das capacidades como prova de todas as combinações em qualquer situação.

Reconsultado diretamente [BL-28 — entrevista de Quilge](https://bleach-anime.com/special/interview/04.html), passagem da atuação na linha 76: distingue Vollständig, Sklaverei e The Jail. Não demonstra uma regra universal de simultaneidade com Blut ou Supressão. M/N/O são propostas de adaptação de compatibilidade e recursos; o recorte corporal de Arterie e os estados de presença seguem o design já aprovado. Textos japoneses interpretados; sem capítulo/episódio integral, novas APIs ou artes.

### Meios, espaço e armadura de Vollständig — preparação de 22/10/2026

Reconsultado [BL-03 — glossário TYBW](https://bleach-anime.com/keyword/), entrada dirigida `滅却師完聖体`, pelo índice após abertura direta retornar 403. O verbete descreve a manifestação comum com halo/asas e diferenças de forma entre usuários. Isso fundamenta não reduzir todo poder ao mesmo perfil visual; não fornece medidas, colisão, slots, condição de apoio ou regras de retorno de Minecraft. Não importar outras entradas recentes/promocionais da página. [BL-28 — entrevista de Quilge](https://bleach-anime.com/special/interview/04.html) teve nova abertura direta como referência de distinção das capacidades, sem ser evidência de física ou equipamento universais.

P/Q/R são propostas de adaptação: meios de manifestação, espaço para o perfil funcional e continuidade da armadura equipada. A aprovação de regras semelhantes em Resurrección não as transfere a Quincy. Transformar no ar e possuir voo são contratos distintos; voo/Sklaverei continuam no horizonte, com pesquisa e ficha próprias. Fonte oficial do anime interpretada do japonês; mangá continua base de D05. Não foram lidos capítulos/episódios integrais, pesquisadas novas APIs ou escolhidas artes. Plataforma local reconfirmada: Minecraft 1.20.1 / Forge 47.4.10 / Java 17.

### Letzt Stil — pesquisa e proposta de perfil — 22/10/2026

Reconsultados diretamente [BL-16 — glossário Pierrot](https://pierrot.jp/title/bleach/keyword_ka.html), entrada `滅却師最終形態`, linhas 97–101, e [BL-23 — retrospectiva dos episódios 42–63](https://pierrot.jp/title/bleach/playback_03.html), linhas 46–49. O primeiro associa treino com Sanrei, retirada da luva, poder extremo, perda posterior das capacidades Quincy e um procedimento de recuperação; o segundo contextualiza o uso de Uryū contra Mayuri. São textos oficiais resumidos do anime, com ressalva de época de exibição. Não houve leitura integral de capítulo/episódio. Não converter o prazo literal de treino ou o procedimento anatômico em exigência de gameplay.

[BL-03 — glossário TYBW](https://bleach-anime.com/keyword/): abertura direta retornou 403; a busca dirigida recuperou a entrada `滅却師完聖体`, distinguindo Vollständig de Letzt Stil. Não utilizar outras entradas mutáveis ou resultados secundários como fundamento. As fontes não demonstram compatibilidade universal com todas as Schrifts, repetibilidade após recuperação, janela de duração, custos, suspensão de todo repertório do jogador ou currículo independente universal.

M07-08A/B/C, seções 8.89–8.91, aprova essas escolhas como adaptações em 22/10/2026: via tradicional opcional com Sanrei; janela breve sem sobreposição a Vollständig, com consequência assumida apenas na conclusão válida; suspensão recuperável de capacidades Quincy sem apagar conquistas. Recuperação e repetibilidade exigem contrato próprio antes da entrega. Não atribuir essas políticas de acesso, economia ou persistência à obra.

### Fullbringer — natureza, desenvolvimento e completude — pesquisa de 22/10/2026

Reconsultado diretamente [BL-11 — Pierrot, glossário ha](https://pierrot.jp/title/bleach/keyword_ha.html), `完現術/完現術者`, linhas 52–62: atuação sobre a alma da matéria e afinidade com matéria familiar. [BL-17 — retrospectiva 343–366](https://pierrot.jp/title/bleach/playback_16.html), linhas 67, 86 e 98, resume descoberta, descontrole e completude no treinamento de Ichigo. Não generalizar sua condição de substituto Shinigami ou as etapas da sua manifestação.

Consultados diretamente [BL-29 — Shonen Jump, arco do desaparecimento dos poderes](https://www.shonenjump.com/xmedia/bleach/story/index5.html), verbete de Fullbring, e [BL-30 — TV Tokyo, sinopse do episódio 358](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes30/index.html), exibido em 31/01/2012, linhas 82–86. O texto editorial identifica o princípio e o caso corporal de Chad; a sinopse situa a conclusão do Fullbring de Ichigo. Nesta consulta, BL-29 forneceu texto legível; não revisar a alegação histórica de que a tentativa anterior retornou essencialmente imagens.

[BL-31 — fichas oficiais licenciadas de Brave Souls](https://www.bleach-bravesouls.com/en/character/ginjo.html), Ginjō, [Yukio](https://www.bleach-bravesouls.com/en/character/yukio.html), [Tsukishima](https://www.bleach-bravesouls.com/en/character/tsukishima.html) e [Chad](https://www.bleach-bravesouls.com/en/character/sado.html), abertas diretamente: descrições de identidade/vetor, sem importar habilidades jogáveis, raridade, economia ou balanceamento do outro jogo. Ginjō usa pingente, Yukio console e Tsukishima marcador; a ficha de Chad reconhece seu Fullbring. BL-05/Characters retornou 403 na abertura desta rodada, sem nova leitura direta do elenco por essa página.

Fontes oficiais resumidas/licenciadas; não houve leitura integral de mangá, episódios ou novels. Mangá continua base de D05; a pesquisa delimita o suporte acessível, sem provar um currículo, número universal de estágios ou custo comum. M07-09A/B, aprovado em 22/10/2026 nas seções 8.93/8.94, define marcos próprios, Mastery contínua e economia por manifestação/efeito como adaptações de gameplay. Não equiparar completude a uma transformação universal, transferência obrigatória de poder, roubo de outro jogador ou acesso automático a estágios posteriores. Conteúdo específico dos Fullbrings permanece para fichas, sem selecionar agora primeiro poder, arte ou campanha.

## 5. Adaptação para Minecraft

- Conquista pessoal, manifestação atual, equipamento e estado do mundo precisam ser consultáveis separadamente pelo servidor, inclusive no singleplayer.
- O mundo compartilhado continua acontecendo durante provas e liberações. Não presumir pausa global, invulnerabilidade ou teleporte para uma arena privada.
- A progressão não será resolvida apenas por portar um item, repetir ativações ou acumular abates. Requisitos e prática válida seguem M04/M05/M06.
- Corpos, mãos, armas e formas precisam caber e funcionar no terreno construído. Mudança de tamanho não autoriza quebrar blocos, atravessar parede ou criar espaço automaticamente; a solução concreta será definida nos blocos de transição.
- O modo de combate apresenta controles. Só o contrato de cada capacidade decide o efeito de entrar/sair desse modo; arco e Blut mantêm suas regras aprovadas em M06.

## 6. Escopo

**Pertence a M07:** evolução e transições raciais; acesso a liberações e estágios; ativação, manutenção, reversão, domínio e compatibilidades; alterações de corpo, repertório e capacidade; consequências de falha e ciclo de vida das formas.

**Fora deste fechamento:** quests e diálogos completos; elenco de mentores; modelos/artes finais; catálogo integral de poderes; fórmulas e calibração definitivas; implementação e evidências de gameplay. Integrações têm responsáveis na seção 9.

## 7. Modelo conceitual — M07-01, consolidação herdada

**Estado:** modelo conceitual herdado; contratos consolidados nas seções 7.1/7.2. G01/G02 aprovados em 22/10/2026 nas seções 7.3/7.4 ampliam os padrões nos escopos declarados.

| Registro | O que responde | Exemplo e ressalva |
|---|---|---|
| Evolução ou condição adquirida | O que o personagem se tornou? | Vasto Lorde é evolução permanente; Arrancarização é transição adquirida que preserva a origem e encerra a evolução Hollow normal naquele ponto. Não são botões para retornar livremente ao corpo anterior. |
| Poder/estágio conquistado | O que ele já aprendeu ou desbloqueou? | Possuir Zanpakutō, conquistar Shikai e conquistar Bankai são marcos distintos. A identidade Hollow pode começar antes da Arrancarização, mas não permite executar Resurrección em estado incompatível. |
| Manifestação ativa | O que está usando neste momento? | Shikai ou outra forma liberada, com requisitos próprios. Estar sem a manifestação não significa perder o aprendizado. |
| Domínio | Como sabe utilizar o que conquistou? | Mastery da capacidade/forma quando apropriado, distinta de aquisição, atributos e Proficiência geral. Granularidade de cada caminho ainda será detalhada. |

Esses registros coexistem: conquistar Shikai e manter Shikai ativo são informações distintas sobre o mesmo personagem. Não são três classes mutuamente exclusivas de poderes. Completar Fullbring registra um marco de desenvolvimento; não cria obrigatoriamente uma nova transformação alternável para todo Fullbring.

Supressão/Natural/Liberação de Reiatsu continuam sendo estados de manifestação da presença, definidos em M06. Liberação de Reiatsu não concede Shikai, Bankai, Resurrección ou Vollständig.

### 7.1. Contratos comuns — consolidação de 22/10/2026

**Mudança de método solicitada pelo usuário:** “estamos decidindo a mesma coisa várias vezes, sendo que poderiam ser regras gerais para adiantar aqui, mas sem perder a qualidade da documentação”. A partir desta revisão, a leitura principal é **contrato comum → perfil da forma → exceções justificadas**. A seção 8 preserva os IDs, respostas e datas das aprovações detalhadas; não deve ser usada como roteiro para repetir as mesmas perguntas.

**Natureza desta tabela:** consolidação documental de regras já aprovadas nos escopos indicados. Os códigos C identificam referências comuns, não novas votações ou expansão automática para qualquer capacidade. Para um novo bloco, apresentar de uma vez o perfil aplicável e suas diferenças; perguntar somente o que muda a experiência, introduz consequência nova, amplia o escopo ou conflita com uma decisão. Uma particularidade já resolvida em outro módulo é citada, sem nova votação.

| Referência | Contrato a citar nas fichas | Escopo já aprovado / origem |
|---|---|---|
| M07-C01 — conquista e manifestação | Separar condição permanente, conquista, manifestação ativa e domínio. Encerrar uso não apaga por si só aquisição/Proficiência/Mastery; não cria treino AFK, compra de domínio ou concessão de todo repertório | M07-01; conquistas e domínio dos blocos 02–09, com particularidades na seção 8. Privação efetiva e riscos especiais continuam contratos próprios |
| M07-C02 — validade e dependências | Servidor valida aprendizado, corpo, item, mãos, fase e recurso pertinentes. Perder requisito invalida as partes que dependem dele; dependência de uma técnica não se torna automaticamente dependência da forma inteira. Pedido recusado conserva o estado anterior apenas enquanto válido | M06 e M07-01; aplicações de arma/posse e preparo de Shikai/Bankai/Resurrección; Vollständig H/I/J/K; Letzt Stil B/C; Fullbringer A/B |
| M07-C03 — preparo das liberações | Preparo breve, caminhada lenta/olhar compatíveis; reação disponível cancela antes de começar, sem recuperação extra só por cancelar. Pedido indisponível conserva preparo ainda válido. Sem ataque/item/canal paralelo ou cancelamento gratuito da ação anterior | Shikai E, Bankai E, Resurrección E, Vollständig J; Letzt Stil B; Fullbringer B (manifestações básicas pertinentes). Evolução Hollow e Arrancarização têm preparo parado, apoiado e fora de combate; não herdam caminhada por associação |
| M07-C04 — cobrança | Nos perfis listados, ativação paga uma vez na conclusão válida; cancelar antes ou falhar nos requisitos da conclusão não cobra essa ativação. Manutenção, quando existir no perfil, paga no momento devido; falha encerra o benefício dependente, sem débito parcial/antecipação de períodos futuros | Shikai C, Bankai D/K, Resurrección D/P, Vollständig G/O; Letzt Stil B (janela limitada, sem manutenção básica). Fullbringer B aplica ativação às manifestações básicas pertinentes e custo próprio às demais aplicações. Valores e existência de manutenção não são universais |
| M07-C05 — custos e tempos preservados | Trocar forma, arma, interface ou sessão não devolve despesas legítimas, repõe recursos, repara/duplica itens ou apaga cooldown/Recovery. Tempos seguem o relógio do mundo, não treino/regeneração offline | M05, M06-11V e aplicações de M07. Conversão de máximos continua exigindo ficha |
| M07-C06 — interface | Os perfis listados são manuais e independentes de Combat Mode. Fechar a Combat Hotbar não encerra a forma nem suspende manutenção/restrições | Shikai B, Bankai C, Resurrección C, Vollständig F; Letzt Stil B; Fullbringer B (manifestações básicas pertinentes). Arco Quincy/Blut continuam desligando ao sair do modo por M06; evolução permanente não é uma interface |
| M07-C07 — transições | Concluir/encerrar não acrescenta recuperação ou taxa universal além do preparo/custo do perfil; respeita fase e ação disponível. Não cancela ataque comprometido nem permite ação sem requisito | Shikai H, Bankai G, Resurrección G, Vollständig L; Letzt Stil B; Fullbringer B (manifestações básicas pertinentes). Encerramento voluntário e perda forçada de requisito são eventos distintos |
| M07-C08 — presença | Os perfis listados podem coexistir com um estado válido de Reiatsu. Apenas manifestar não é ofensiva automática; iniciar ofensiva válida encerra Supressão. Sinais visuais permanecem e custos/exclusividades próprios continuam | Shikai J, Bankai J, Resurrección O, Vollständig N; Letzt Stil B; Fullbringer B (manifestações básicas pertinentes); M06. Manifestação que já execute ataque/área ofensiva precisa ser classificada como tal |
| M07-C09 — itens e empunhadura | Guardar no próprio inventário difere de perder posse. Ferramentas/consumíveis continuam dependendo de corpo, mãos e fase; ocupação de uma arma/técnica não some ao trocar interface. Incorporação não cria uma cópia utilizável da arma | Shikai F/G, Bankai H/I, Resurrección H/L/M, Vollständig H/I; Letzt Stil B; Fullbringer B. O perfil declara se exige empunhadura, posse ou nenhum instrumento; não universalizar a perda de espada de uma raça |
| M07-C10 — corpo e meio | Validar corpo funcional, não apenas aparência. Nos escopos já aprovados, conferir espaço no início/conclusão, posição real e sem reposicionamento automático; preservar queda/riscos e não conceder voo/respiração por aparência | Espaço: Hollow J, Arrancarização I, Resurrección I e Vollständig Q. Chão/ar/água: Resurrección K, Vollständig P e Letzt Stil B; este também aplica validação espacial. Fullbringer B aplica esse perfil às manifestações básicas pertinentes. G02 aprovado estende passagem compatível com corpo-base às formas temporárias que mudam corpo; permanentes mantêm seus contratos |
| M07-C11 — armadura | Quatro slots normais continuam funcionais, mesmos itens, encantamentos elegíveis e desgaste; apresentação adaptável, sem cópia/reparo ou proteção adicional pela malha. Remover/quebrar retira a contribuição | Resurrección N, Vollständig R e Letzt Stil B e Fullbringer B (manifestações básicas pertinentes). M06-11A/B rege elegibilidade/composição, sem conceder esses slots a todo corpo. Outros perfis precisam declarar compatibilidade no pacote, sem repetir a fórmula defensiva |
| M07-C12 — efeitos e ciclo de vida | Efeitos autônomos, dependentes e montagens seguem M06-11W; tempos seguem V. G01 encerra preparo/manifestação temporária no ciclo de vida, com retorno manual; estado-base é campo do perfil. Perder manifestação não recolhe todo disparo nem limpa toda condição independente | Herança M07-01/M06; Shikai I, Bankai L e Resurrección Q preservados. G01 aprovado inclui Vollständig/futuros perfis temporários pertinentes; condições permanentes e consequências especiais preservadas. Letzt Stil B/C registra compromisso/perda sem cura pelo encerramento |

**Qualidade da ficha:** referenciar o contrato comum e registrar somente parâmetros, diferenças, origem canônica, estados/dependências, feedback e casos observáveis próprios. A revisão verifica a aplicação dos contratos, em vez de criar uma pergunta para cada caso técnico. Um caso como inventário cheio pode exigir especificação M11/M16 sem representar nova escolha de produto. Pendência com responsável não equivale a regra implementada ou experiência validada.

### 7.2. Perfis vigentes — diferenças que continuam importando

| Perfil | Custo e execução | Instrumento / retorno / particularidade |
|---|---|---|
| Shikai | C03–C08; Energia ao concluir, sem manutenção básica | Zanpakutō compatível empunhada ao liberar; conservar ao guardar na própria posse, perda de posse encerra. Selar quando fase permitir. Ciclo de vida encerra; domínio próprio sem liberar Bankai. Fonte: M07-02A–J |
| Bankai | C03–C08; ativação + manutenção. Cobranças Bankai → Liberação → ação | Zanpakutō empunhada ao liberar; guardar na própria posse conserva manutenção. Encerramento voluntário permite Shikai/selado; falta de manutenção retorna a Shikai válido sem nova cobrança. Ciclo de vida retorna selado. Quebra estrutural e recuperação têm contrato exclusivo M/N; não generalizar. Fonte: M07-03A–N |
| Evolução Hollow | Permanente; predação/marcos/provas; mudança deliberada parada, apoiada e fora de combate, sem taxa/manutenção adicional | Conferir espaço; tentativa incompleta encerra nos eventos de ciclo de vida, estágio/marcos concluídos persistem. Gillian/Adjuchas/Vasto Lorde têm requisitos próprios. Não aplicar encerramento de forma temporária ao estágio. Fonte: M07-04A–M |
| Arrancarização | Condição permanente; preparo/acontecimento; execução parada, apoiada e fora de combate, sem taxa/manutenção adicional | Corpo-base humanoide, arma própria ao concluir, domínio compatível preservado, sem bônus/teto fixo por origem. Preparo interrompido termina; condição conquistada permanece. Fonte: M07-05A–J |
| Resurrección | C03–C08/C10/C11; ativação + manutenção. Cobranças forma → Liberação → ação | Arma própria empunhada para liberar, representação conforme poder; guardar arma separada na própria posse conserva forma e perder posse a encerra. Retorno a base Arrancar. Passagem inclui corpo-base; ciclo de vida encerra. Fonte: M07-06A–Q |
| Vollständig | C03–C08/C10/C11; ativação + manutenção. Cobranças forma → Blut → Liberação → ação | Schrift compatível antes, aprendizado próprio; sem instrumento universal. Arco básico continua exigindo foco e duas mãos; um Blut aprendido é compatível. Retorno Quincy sem a forma. Entrada/meios/armadura resolvidos; ciclo de vida encerra por G01 e passagem preserva base por G02. Consolidado estruturalmente; fichas/evidência técnica pendentes. Fonte: M07-07A–R + G01/G02 |
| Letzt Stil | M07-08A–C aprovado; Sanrei aprendida/preparada, Energia na conclusão, janela máxima breve sem manutenção básica; não sobrepor Vollständig | Concluir assume perda de acesso Quincy ao término, inclusive G01. Progresso preservado; recuperação própria remove suspensão, sem restaurar recursos/formas/prontidão Sanrei. Recorte estrutural consolidado, fichas pendentes |
| Fullbringer | M07-09A–C aprovado em 8.93–8.95: marcos próprios, Mastery contínua, custos por manifestação/técnica | Manifestação básica pertinente paga ao concluir, sem manutenção básica; efeitos próprios têm custos/condições. Vetor e fundamentos separados; troca contextual preserva histórico específico indisponível do antigo principal, sem cópia/sobreposição. Consolidado estruturalmente; fichas pendentes |

**Novo conteúdo:** apresentar um pacote contendo perfil/natureza, contratos C aplicáveis, diferenças reais e pendências. Regras já aprovadas para aquele escopo não voltam à votação. G01/G02 já aprovados são citados pelas fichas cobertas, sem nova votação por raça ou evento. Nova ampliação efetiva de escopo será decidida uma vez para a classe pertinente. Exceções precisam identificar o contrato alterado, motivo canônico ou de gameplay, consequência, recuperação quando houver e validação futura; não criar uma exceção silenciosa para cada poder nem exigir nova aprovação para toda linha herdada.

### 7.3. M07-G01 — DECIDIDO — Padrão de ciclo de vida para manifestações temporárias

**Aprovado em 22/10/2026.** Resposta explícita: **“Encerrar formas temporárias; retorno manual (recomendado)”**. Proposta preparada na mesma data.

**Regra aprovada:** adotar como padrão de M07: morte, desconexão, troca de dimensão ou reinício encerram preparo incompleto e manifestação temporária ativa, com retorno ao estado-base definido no perfil. Conquista, identidade, condição permanente, Proficiências e Masteries permanecem; nenhum preparo/forma religa sozinho e uma nova ativação válida paga normalmente. Não cobrar ativação incompleta, devolver gastos anteriores ou continuar cobrando manutenção de forma encerrada; tempos e efeitos seguem C05/C12. Detalhes de itens/respawn/local de chegada e ordenação de eventos ficam com M10/M11/M16.

**Abrangência aprovada:** consolidar Shikai I, Bankai L e Resurrección Q, já aprovados, e estender o mesmo padrão a Vollständig e aos futuros perfis classificados como manifestações temporárias de M07. Vollständig retorna a Quincy sem a forma, preservando Schrift/conquistas. A ficha cita G01 sem perguntar os mesmos eventos novamente. O retorno particular de cada perfil continua explícito: Bankai encerra selado nesse evento, mesmo que a saída por manutenção admita Shikai.

**Fronteiras:** não encerrar Gillian/Adjuchas/Vasto Lorde, condição Arrancar ou apagar Fullbring conquistado. Seu desenvolvimento permanente não é manifestação temporária. Letzt Stil, quebra estrutural e efeitos persistentes com consequência própria não são curados ao limpar a forma; não antecipar seu contrato de risco nem negar uma exceção explicitamente aprovada. Uma exceção futura precisa de regra/justificativa próprias antes de substituir o padrão. Não altera M06/Blut/presença nem concede retorno de item/keepInventory.

**Alternativa não escolhida:** manter as três decisões existentes e adotar restauração automática para Vollständig e novos perfis temporários quando os requisitos permitirem. Retoma a configuração anterior, mas exige definir cobrança, ambiente e dependências do retorno, podendo reativar consumo sem novo pedido. **Justificativa da escolha:** encerramento com retorno manual como padrão comum, preservadas exceções explícitas. É ampliação aprovada de adaptação do mod, não fato canônico novo. Vollständig e futuros perfis temporários pertinentes já estão cobertos; não repetir esta votação para cada um.

### 7.4. M07-G02 — DECIDIDO — Padrão de passagem com espaço para o retorno

**Aprovado em 22/10/2026.** Resposta explícita: **“Preservar espaço para o corpo-base (recomendado)”**. Proposta preparada na mesma data.

**Regra aprovada:** manifestações temporárias que mudam o corpo usam para ocupação/passagem também o espaço do corpo-base na postura compatível. Um corpo liberado maior exige mais espaço; um menor não entra só pelo tamanho em passagem onde o corpo-base não caiba. A validação de entrada consulta esse volume resultante. Assim, o retorno normal pode acontecer na posição atual sem teleporte ou manutenção gratuita. Se ambos mantêm o mesmo volume, não há restrição adicional só por apresentar asas/halo visuais.

**Abrangência aprovada:** preservar Resurrección J e estender esse padrão às mudanças corporais de Vollständig e futuros perfis temporários pertinentes. A postura precisa ser admitida por ambos os corpos; não criar rastejamento ou outra postura por omissão. Ocupação de terreno não amplia a região atingível de combate. A ficha cita G02 e declara medidas/poses, em vez de votar de novo a mesma solução espacial.

**Consequência real:** formas pequenas perdem a vantagem de entrar em túneis menores que o corpo-base. Preservar essa vantagem exige exceção explícita com solução de retorno segura. Não aplicar o padrão a evolução/Arrancarização permanentes: o corpo-base delas efetivamente muda. Voo/nado/respiração e armas seguem suas próprias capacidades, sem concessão por aparência.

**Limite:** pistões, novos blocos, teleporte/comandos, colisões forçadas, spawn e mudança de dimensão ainda precisam de tratamento de M10/M16. G02 não garante que o mundo nunca mude, nem aprova teleporte corretivo, apagamento de blocos, invulnerabilidade ou conservar benefícios sem pagamento. Esse tratamento e os volumes precisam existir antes de entregar um perfil afetado.

**Alternativa não escolhida:** permitir passagem pelo corpo liberado menor e escolher outra solução para retorno quando a base não couber. Preserva exploração por tamanho, mas exige decidir deslocamento/retorno em espaço incompatível e seus riscos. **Justificativa da escolha:** usar o padrão de passagem segura, com exceções justificadas quando o poder realmente depender de acesso por tamanho. A ampliação a Vollständig/futuros perfis pertinentes está aprovada; Resurrección J permanece preservada. A ficha aplica este padrão sem nova votação, salvo exceção justificada que realmente o altere.

## 8. Regras principais

Para a leitura corrente, começar pelos contratos comuns e perfis das seções 7.1/7.2. Os registros abaixo preservam o detalhe e a procedência das decisões; não exigem repetir a votação de cada linha herdada.

### 8.1. Herança que permanece vigente

1. Um poder canônico principal por vez; fundamentos raciais continuam próprios. Múltiplos jogadores com o mesmo poder são adaptação aprovada, sem transferir a biografia do personagem canônico.
2. Spiritual Points têm função direta principal nos atributos; não compram Mastery e não criam taxa universal para despertares. Aplicar REV-M02-02 a 05 de M05 sobre trechos antigos de M04/M02.
3. Nenhuma barra universal de transformação ou multiplicador fixo de atributos/Battle Power está aprovado. Forma pode alterar capacidades conforme seu contrato, sem conceder invencibilidade ou repor recursos automaticamente.
4. Mastery depende de prática apropriada. Não criar segundo rank comprado, Proficiência universal de transformações ou treino por permanecer AFK com forma ligada.
5. Vasto Lorde é conquista permanente, difícil e determinística; Arrancarização pode ocorrer em Gillian/Adjuchas/Vasto Lorde e encerra a evolução Hollow normal naquela etapa. Sem regressão passiva por ficar offline ou pausar para construir/explorar.
6. Nenhum acesso geral a Segunda Etapa, sustentação aérea, voo, regeneração especial ou combinação de formas decorre da existência de um estágio.
7. Morte de gameplay não troca raça automaticamente. Aplicar o ciclo de vida do perfil e a cobertura vigente de C12/G01; uma consequência especial exige decisão própria. G01 aprovado abrange manifestações temporárias, preservando condições permanentes e consequências especiais; não repetir a decisão em cada perfil coberto.
8. Recovery/cooldowns já iniciados seguem M06-11V. Ativar, encerrar ou trocar forma não autoriza reset desses tempos. M06-11W continua distinguindo efeitos autônomos de efeitos dependentes.

### 8.2. M07-02A — DECIDIDO — Primeiro Shikai por vínculo e prova

**Aprovado em 21/10/2026.** Resposta explícita: **“Vínculo com o espírito + prova de liberação (recomendado)”**. Despertar automático ao atingir requisitos de progresso não foi escolhido.

O personagem precisa ter conquistado a Zanpakutō canônica e estabelecido seu vínculo pessoal. Desenvolve a relação com o espírito, aprende a reconhecer seu nome no contexto dessa relação e realiza uma prova prática de liberação. Saber o nome como fã ou possuir uma espada de outro jogador não substitui o aprendizado do personagem.

- Orientação pode preparar o jogador; não é necessário instituir um novo mestre obrigatório para cada uso ou melhoria.
- A prova avalia a conquista da liberação; não se resume a alcançar Proficiência ou quantidade de abates. A prática anterior pode preparar o personagem, sem realizar o despertar sozinha.
- Conclusão e aprendizado são registrados no personagem. O uso posterior não exige repetir a prova ou visitar o mentor.
- O desafio precisa permitir a primeira demonstração no contexto de aprendizado; não exigir Mastery prévia de um Shikai que ainda não pode ser aprendido/usado.
- Conquistar o poder principal, conquistar Shikai e conquistar Bankai continuam separados. A prova não entrega automaticamente técnicas posteriores ou todo o repertório.

**Responsabilidade:** M07 define requisitos e resultado do despertar; M08 organiza orientação; M12/M13 constroem a prova, objetivos e retomada. Cenário, duração, falhas, exercícios e eventuais sessões interiores ainda serão definidos. Não foi aprovada arena instanciada, diálogo digitado ou minijogo obrigatório.

### 8.3. M07-02B — DECIDIDO — Shikai manual e independente da interface

**Aprovado em 21/10/2026.** Resposta explícita: **“Shikai manual e independente da interface (recomendado)”**. Restringir Shikai ao modo de combate não foi escolhido.

Depois de conquistado, Shikai é liberado e encerrado por solicitação manual com a Zanpakutō compatível empunhada. Pode funcionar dentro ou fora de Combat Mode; fechar a Combat Hotbar não sela a arma por si só.

- Validar aprendizado, vínculo, arma, condições da fase e recursos exigidos. B não define custo, duração, tempo de ativação ou limite de manutenção.
- Ativação/encerramento respeitam os compromissos das ações. Não criam cancelamento universal de ataque, emissão extra, restauração de carga ou reset de tempos.
- Fora do modo, mãos, fases, vetor do poder e restrições de uso de itens continuam valendo. Não é autorização para usar duas armas ou item incompatível simultaneamente, nem para executar toda técnica ofensiva sem seus controles/requisitos.
- Entrar no modo não libera Shikai automaticamente. A saída manual da forma é distinta da saída da interface.
- Essa regra é de Shikai. Arco Quincy e Blut continuam desligando ao sair do modo; M06-11X continua governando as capacidades de presença/percepção.

**Complementos posteriores:** E define preparo/reação; F/G resolvem empunhadura versus posse; H/I/J definem conclusão/selamento, ciclo de vida e presença para o Shikai básico. Recuperação do item, transições para Bankai, efeitos particulares e comando concreto permanecem pendentes.

### 8.4. M07-02C — DECIDIDO — Custo da liberação básica de Shikai

**Aprovado em 21/10/2026.** Resposta explícita: **“Custo ao liberar, sem manutenção básica (recomendado)”**. Consumo contínuo enquanto Shikai estiver ativo não foi escolhido.

Uma cobrança pequena de Energia Espiritual ao concluir a liberação, sem consumo contínuo universal apenas para manter a manifestação básica. Técnicas e efeitos sustentados pagam seus custos próprios; isso não torna gratuitas emissões, áreas ou efeitos só porque pertencem ao Shikai.

**Alternativa não escolhida:** manutenção contínua enquanto Shikai estiver ativo. Dá peso constante à forma, mas também cobra por carregar uma espada liberada durante exploração e torna o fundamento dependente de manutenção mesmo sem usar técnicas.

**Contrato estrutural de C:** validar recurso para iniciar e revalidar na conclusão; uma cobrança pelo estabelecimento bem-sucedido, sem cobrar ativação que não se concluiu. Sem recurso na conclusão, não estabelecer Shikai nem debitar parcialmente. Encerrar não devolve o custo; nova liberação paga de novo. Manter a manifestação básica sem manutenção não exige uma reserva artificial de Energia; ataques/técnicas continuam dependentes de seus requisitos. Encerramento não tem taxa adicional. Ainda não define preparo, interruptibilidade, Recovery, valor ou modificadores particulares.

**Justificativa:** permitir que Shikai seja uma ferramenta habitual conquistada, deixando o peso recorrente nas ações/efeitos que o exigem. A economia é adaptação aprovada do mod, não regra universal demonstrada nas fontes. Não se aplica a Bankai ou a outras raças.

### 8.5. M07-02D — DECIDIDO — Manifestação inicial confiável e domínio progressivo

**Aprovado em 21/10/2026.** Resposta explícita: **“Manifestação confiável + domínio progressivo (recomendado)”**. Ativação com chance de falha no início não foi escolhida.

Após a prova, o personagem já pode manifestar o Shikai básico de forma confiável se cumprir os requisitos. Mastery do Shikai desenvolve a execução/eficiência e aprofunda o uso da forma, sem entregar todas as técnicas; técnicas distintas conservam aprendizado e Mastery próprios conforme sua ficha.

**Alternativa não escolhida:** adicionar chance aleatória de falha às ativações com pouco domínio, mesmo com condições válidas. Representa instabilidade, mas permite perder uma reação por sorte e dificulta compreender se faltou recurso, requisito ou execução.

**Contrato estrutural de D:** não criar sorte por clique, progresso por liga/desliga ou ganho por tempo AFK. Prática precisa demonstrar uso apropriado da forma; a ficha deve distinguir evidência de domínio da forma de domínio de uma técnica, sem duplicar arbitrariamente o mesmo crédito. Não cobrar uma segunda aquisição depois da prova para começar a treinar. Mastery pode contribuir para marcos posteriores, mas esta decisão não concede Bankai por alcançar um número isolado nem define o domínio exigido para conquistá-lo. Nenhum percentual, curva ou nova barra está aprovado.

**Limite:** confiabilidade básica não remove vulnerabilidade, interrupção apropriada, requisitos ou riscos específicos de técnicas. Não elimina toda possibilidade de instabilidade narrativa em outros poderes; também não permite impor essa instabilidade ao primeiro Shikai sem decisão.

### 8.6. M07-02E — DECIDIDO — Preparo de liberação cancelável para reagir

**Aprovado em 21/10/2026.** Resposta explícita: **“Preparo breve cancelável para reagir (recomendado)”**. Ativação comprometida até terminar não foi escolhida.

A liberação básica possui preparo breve, com caminhada lenta e possibilidade de olhar ao redor. Não exige sustentar uma carga crescente nem concede a forma antes da conclusão válida. Duração nominal em ticks/segundos e apresentação permanecem para calibração/ficha e M14.

- Ataque, bloqueio, Dodge ou Shunpo disponíveis cancelam o preparo antes de começar a ação, sem recuperação adicional só por esse cancelamento. Requisitos, custo e tempos da reação continuam próprios.
- Validar a reação antes de abandonar o preparo ainda válido. Pedido indisponível gera feedback e o preserva, sem prolongar prazo ou enfileirar ação futura.
- Cancelamento antes da conclusão não cobra a Energia da liberação nem concede seus benefícios. Conclusão revalida e paga uma única vez conforme C. Custos anteriormente pagos não são devolvidos.
- Dano isolado não interrompe universalmente, conforme M06-02S; controle que invalide o preparo e perda de requisito aplicam a interrupção pertinente. A preparação não concede Super Armor ou invulnerabilidade.
- Caminhar/olhar não cria voo, apoio ou suspensão da queda. A ação de origem precisa permitir entrar na liberação: E não autoriza cancelar qualquer ataque anterior para liberar Shikai.

**Complemento posterior:** H define conclusão sem recuperação adicional e selamento direto quando permitido. Não copiar a recuperação de cancelamento de Cero nem as permissões de todo disparo para esta forma.

### 8.7. M07-02F — DECIDIDO — Conservar a liberação na própria posse

**Aprovado em 21/10/2026.** Resposta explícita: **“Conservar liberação; exigir empunhadura para usar (recomendado)”**. Selar ao deixar de empunhar não foi escolhido.

Depois de concluir a liberação, deixar de empunhar a Zanpakutō, mantendo-a no próprio inventário, conserva o Shikai básico. Técnicas e bônus dependentes de empunhadura ficam indisponíveis enquanto esse requisito não estiver satisfeito. Retornar à mesma arma permite usá-lo sem uma nova liberação ou cobrança, respeitando fases e recuperações.

- F complementa B: empunhadura é exigida para a solicitação de liberação, mas deixar de empunhar depois de concluída não sela automaticamente a manifestação conservada. Manter a conquista e conservar a liberação são registros distintos.
- Trocar para uma ferramenta não transforma os ataques dela em ataques da Zanpakutō. O moveset e as técnicas continuam verificando arma, vínculo, mãos e fase atuais.
- Conservar liberação não conserva preparo incompleto, carga ofensiva ou técnica cuja execução perdeu a arma exigida. Não é ativação automática ao equipar: a manifestação anterior ainda precisa existir e estar válida.
- Trocar/guardar só produz seus efeitos quando a fase permitir; não serve para encurtar execução comprometida ou apagar Recovery/cooldowns. Uma transferência efetiva de posse aplica G.
- Efeitos especiais, vetores desmaterializados e capacidades sustentadas exigem ficha própria: F não garante que toda área, lâmina dispersa ou bônus continue no mundo ao guardar a arma, nem suspende cobrança de efeito que ainda continua ativo legitimamente. M06-11W continua governando efeitos autônomos/dependentes.

**Experiência:** alternar entre ferramenta, combate corporal e arma sem pagar uma nova liberação por cada troca de empunhadura. Embainhar/guardar é uma condição funcional; representação visual de formas que não cabem em bainha comum continua própria do poder, sem obrigar uma bainha literal universal.

### 8.8. M07-02G — DECIDIDO — Perda de posse encerra manifestação básica

**Aprovado em 21/10/2026.** Resposta explícita: **“Encerrar manifestação ao perder a posse (recomendado)”**. Conservar liberação com a arma distante não foi escolhido.

Drop, depósito em baú ou transferência para outra pessoa encerram o Shikai básico conservado e as capacidades que dependem dessa posse. A arma continuar perto do dono não preserva o acesso. O servidor valida a mudança; a interface e o nome exibido do item não autorizam o poder.

- Vínculo, aquisição, aprendizado e Mastery permanecem no personagem. A perda física não concede automaticamente outra Zanpakutō nem transfere o poder para quem recolher o item, conforme M06-07V–Y.
- Recuperar a arma não restaura a liberação sozinha. Será necessário novo pedido válido, com o custo de C na conclusão. Custos anteriores não são devolvidos e os tempos iniciados permanecem.
- Se a posse for perdida antes da conclusão do preparo, não concluir nem cobrar a liberação. Não emitir depois ou restaurar um preparo abandonado quando a arma retornar.
- Capacidade dependente encerra ao perder o requisito; disparo autônomo já emitido segue M06-11W. G não recolhe projéteis, apaga impactos resolvidos ou cancela indistintamente toda condição já aplicada.
- O registro da manifestação e a exclusividade do vínculo precisam resistir à transferência/duplicação de itens; a simples cópia de um item não pode produzir segunda manifestação autorizada. Dados e migração pertencem a M11/M16.

**Pendências preservadas:** reposição/recuperação, regras de inventário e morte, slots adicionais/modificados e consequências de itens perdidos pertencem a M11/M15. G não aprova keepInventory, indestrutibilidade, retorno automático ou perda permanente do poder. Sair da interface continua diferente de perder posse.

### 8.9. M07-02H — DECIDIDO — Conclusão e selamento sem recuperação adicional

**Aprovado em 21/10/2026.** Resposta explícita: **“Transição sem recuperação adicional (recomendado)”**. Acrescentar recuperação própria após liberar/selar não foi escolhido.

Após concluir a liberação, permitir ações disponíveis sem espera adicional somente pela mudança de forma. Selar é direto quando a fase permitir, sem taxa ou recuperação extra.

O preparo de E continua existindo; H não concede liberação instantânea. A ação seguinte continua sujeita a suas mãos, requisitos, recursos e tempos. Selar não é reação que cancela automaticamente uma técnica nem atalho para interromper sua fase comprometida. O pedido recusado não é enfileirado e não encerra uma manifestação ainda válida. Aplicar a empunhadura exigida em B ao pedido manual; não confundir com a perda forçada de requisito em G.

Justificativa: o preparo cancelável e o pagamento de C já estabelecem o compromisso da liberação básica. Uma espera posterior adicional deve ter motivo de gameplay; não será copiada de Cero ou de todas as técnicas. O tempo restante de uma ação anterior continua valendo em qualquer forma.

### 8.10. M07-02I — DECIDIDO — Ciclo de vida do Shikai básico

**Aprovado em 21/10/2026.** Resposta explícita: **“Encerrar Shikai; retorno manual (recomendado)”**. Restaurar a manifestação automaticamente quando possível não foi escolhido.

Morte, logout, mudança de dimensão ou reinício encerram a manifestação básica. Vínculo, aprendizado e Mastery persistem; retorno sem liberação automática.

A decisão aplica a separação conquista/execução ao Shikai, sem presumir que M06-11U já a tivesse decidido para todas as formas. Não recolhe ataques autônomos emitidos, não limpa indiscriminadamente condições e não altera recursos do respawn, itens perdidos ou `keepInventory`. Cooldowns/recuperações seguem V, inclusive durante desconexão conforme o tempo simulado do mundo.

Se um evento ocorrer antes da conclusão da liberação, não completar ou cobrar o estabelecimento que não ocorreu. Depois da conclusão, o custo legítimo permanece; retornar exige nova liberação válida/pagamento normal. Conservar a forma ao trocar para ferramenta, em F, não representa persistência entre sessões.

### 8.11. M07-02J — DECIDIDO — Shikai e manifestação de Reiatsu

**Aprovado em 21/10/2026.** Resposta explícita: **“Shikai compatível com os estados de Reiatsu (recomendado)”**. Exigir Natural para manter o Shikai básico não foi escolhido.

Shikai básico pode coexistir com Natural, Supressão ou Liberação de Reiatsu. A manifestação da forma da arma, sem ataque associado, não é ofensiva automática.

- Supressão continua reduzindo presença conforme Controle/Mastery/condições; não torna arma, corpo ou efeitos visuais invisíveis, nem assegura ocultação apesar da manifestação. A leitura considera a presença realmente emitida, sem revelar poder exato.
- Início ofensivo válido, inclusive se a ficha combinar uma liberação com ataque, encerra Supressão conforme M06-11J. Rejeição anterior à ação não a rompe automaticamente.
- Liberação de Reiatsu conserva seu custo e efeito próprios, sem bônus de dano por associação. Manutenções realmente devidas precedem o pagamento da liberação do Shikai conforme M06-11T; não adicionar manutenção básica à forma em desacordo com C.
- Shikai não ativa Liberação de Reiatsu automaticamente, e selar a arma não é pedido automático de troca do estado da presença. Formas e presença continuam eixos distintos.
- Efeitos especiais, controles, Bankai e demais transformações exigem compatibilidade própria. J não concede imunidade a toda interrupção nem libera ocultação de qualquer área ofensiva sustentada.

H/I/J são adaptações aprovadas do mod; as fontes consultadas não os demonstram como regras canônicas universais. Compatibilidade não garante ocultação perfeita, dano adicional ou imunidade a controle.

### 8.12. M07-03A — DECIDIDO — Conquista própria de Bankai

**Aprovado em 21/10/2026.** Resposta explícita: **“Relação avançada + prova com o espírito (recomendado)”**.

O jogador já conquistou Shikai, demonstrou uso apropriado dele e desenvolveu sua relação com o espírito. O aprendizado de Bankai envolve materializar o espírito e superar uma prova que demonstre domínio sobre essa manifestação. A forma do desafio precisa respeitar a Zanpakutō; não obrigar toda prova a ser matar o espírito ou repetir o mesmo duelo.

**Efeito para o jogador:** a primeira liberação é uma conquista pessoal posterior ao Shikai. Não basta encher uma barra, acumular abates ou receber um item de um mentor. O registro da conquista pertence ao personagem, não a todos os membros da party.

**Contrato estrutural:** não exigir Shikai com Mastery máxima universal, Bankai já dominado ou todas as quatro disciplinas desenvolvidas para permitir tentar aprender. A preparação precisa ser verificável por uso/desafios apropriados e comunicada ao jogador; seus marcos e exigências concretas ainda precisam de especificação. Cargo de capitão, filiação específica, taxa universal de Spiritual Points e prazo real de anos não são requisitos gerais. Orientação pode ajudar; não pressupor um NPC obrigatório para cada melhoria. A conquista fica registrada, sem repetir a prova a cada ativação.

**Alternativa não escolhida:** liberar Bankai automaticamente ao atingir um marco numérico de desenvolvimento de Shikai. É mais simples de comunicar e produzir, mas reduz a relação com o espírito a uma barra e torna o despertar semelhante ao progresso comum. **Justificativa da escolha:** prova própria com preparação demonstrada, para dar significado ao marco sem exigir maximizar o estágio anterior.

**Responsáveis:** M07 valida os requisitos e registra a conquista; M08/M12/M13 detalham orientação, representação do espírito, exercícios, falha e retomada. Não foi escolhida arena privada, item obrigatório de materialização, prazo de três dias, mestre específico ou combate idêntico para todas as armas. Prova e primeiro uso precisam permitir demonstração de aprendizado sem requisito circular.

### 8.13. M07-03B — DECIDIDO — Manifestação básica e domínio de Bankai

**Aprovado em 21/10/2026.** Resposta explícita: **“Manifestação confiável + domínio próprio (recomendado)”**.

Depois da conquista, o jogador consegue manifestar o Bankai básico quando cumprir seus requisitos, sem chance aleatória de falhar a cada ativação. A Mastery própria desenvolve execução, eficiência e uso da forma; técnicas distintas mantêm aprendizado e Mastery próprios. Conquista inicial não entrega automaticamente todo o repertório ou versões posteriores.

**Efeito para o jogador:** ele já pode usar o poder que conquistou e aprende a aproveitá-lo melhor. Uma recusa deve apontar o requisito ausente; os limites concretos de uma forma recém-aprendida ficam em sua ficha. Domínio baixo não equivale automaticamente a roleta de ativação.

**Contrato estrutural:** Shikai e Bankai não compartilham um único valor de Mastery; conhecer o primeiro não entrega domínio do segundo. Não criar Proficiência universal de transformações, rank comprado ou crédito por permanecer AFK/alternar formas. Prática apropriada precisa distinguir domínio da forma e de sua técnica sem multiplicar arbitrariamente a mesma evidência. Confiabilidade não concede imunidade, execução instantânea, manutenção gratuita ou todas as evoluções canônicas por associação.

**Alternativa não escolhida:** manifestação inicial sujeita a falha aleatória até atingir domínio suficiente. Cria instabilidade, mas pode frustrar uma ativação válida sem uma decisão do jogador. **Justificativa da escolha:** confiabilidade básica, com desenvolvimento e limitações compreensíveis definidos por forma. Particularidades canônicas de controle exigem pesquisa/ficha; não decidir um estágio incompleto genérico para todo Bankai.

**Responsáveis:** M05/M07/M15 definem contribuição/curva e limites do domínio; M06/M07, repertório e efeitos por forma; M14, feedback. C–L complementam B com execução, economia, posse, presença e ciclo de vida. Valores e fichas particulares permanecem pendentes; M/N definem o contrato básico de quebra/consequências. B não copia as regras econômicas ou de transição de Shikai.

### 8.14. M07-03C — DECIDIDO — Ativação manual e direta de Bankai

**Aprovado em 21/10/2026.** Resposta explícita: **“Bankai manual e direto (recomendado)”**.

Solicitar Bankai manualmente com a própria Zanpakutō compatível empunhada, a partir do estado selado ou de Shikai. O pedido é permitido dentro ou fora de Combat Mode; fechar a Combat Hotbar não encerra Bankai por si só. Entrada na interface não o ativa automaticamente.

**Experiência:** depois de conquistar o estágio, o jogador escolhe quando utilizá-lo sem precisar executar duas liberações em sequência a cada uso. Ter aprendido Shikai continua requisito de conquista de A; estar com ele ativo não é requisito universal de cada ativação.

**Contrato estrutural:** validar vínculo, aprendizado, arma, estado/fase, recurso e condições da forma; a autorização do servidor permanece. A mudança não acumula simultaneamente os benefícios das duas formas nem concede benefícios antecipados durante o preparo. Não cancela por si só uma técnica anterior, apaga tempos ou recupera recursos. Partir do estado selado não executa uma liberação intermediária de Shikai e não cobra seu custo; Bankai paga a própria regra, sem desconto automático por vir de Shikai. E define preparo/cancelamento; F/G, retorno/conclusão; H/I, arma guardada/perdida. Efeitos particulares ainda precisam de ficha.

**Alternativa não escolhida:** exigir Shikai ativo antes de cada solicitação de Bankai. Comunica os estágios pela sequência, mas acrescenta comando, tempo e custo intermediário a todo uso. **Justificativa da escolha:** ativação direta, preservando conquista e condições próprias. É adaptação de controle/fluxo do mod; as fontes consultadas não fixam esse contrato universal.

**Responsáveis:** M07, acesso e transição; M06, fases e ações; M11, vínculo/mãos; M14, comando e feedback. F/H/J complementam retorno, perda de empunhadura e convivência com Reiatsu. Independência da interface não significa ataque sem requisitos ou manutenção suspensa.

### 8.15. M07-03D — DECIDIDO — Energia ao estabelecer e manter Bankai

**Aprovado em 21/10/2026.** Resposta explícita: **“Ativação + manutenção de Energia (recomendado)”**.

Cobrar Energia Espiritual uma vez ao concluir a liberação válida e consumo contínuo enquanto Bankai permanecer ativo, sem cronômetro universal de duração. Técnicas e efeitos próprios conservam custos adicionais definidos em ficha; não duplicar a mesma obrigação.

**Experiência:** o jogador decide quando o estágio compensa o gasto e por quanto tempo pode sustentá-lo. Reserva, Controle, domínio e uso de técnicas podem participar da eficiência conforme ficha/calibração; não estabelecem duração infinita ou redução universal de todos os custos. O primeiro uso precisa ser viável após a conquista, sem exigir domínio prévio de um Bankai ainda inacessível.

**Contrato estrutural:** validar Energia ao iniciar e revalidar ao concluir; preparo cancelado ou liberação recusada antes do estabelecimento não cobram ativação nem entregam Bankai. Concluir paga uma vez; sem recurso suficiente, não concluir nem debitar parcialmente. Manutenção realmente devida precisa ser paga para sustentar o período correspondente, sem intervalo gratuito renovável por alternância. Encerrar não devolve gastos; nova liberação válida paga novamente. Se faltar manutenção, Bankai termina sem reativação automática; o destino Shikai/selado segue F, com exceções de perda de posse/ciclo de vida em I/L. Nenhum reset de Recovery/cooldown, recarga de Vida/Energia ou progresso por AFK decorre da ativação/manutenção.

**Alternativa não escolhida:** pagar somente ao liberar e limitar cada uso por duração fixa, deixando o consumo recorrente para técnicas. Torna a duração previsível, mas cria um prazo adicional independente da reserva real e aproxima formas diferentes do mesmo temporizador. **Justificativa da escolha:** ativação + manutenção, sem duração fixa universal. Limites particulares de uma Zanpakutō exigem fonte e ficha próprias.

**Fonte e limite:** BL-03, entrada Bankai, reconsultada em 21/10/2026, descreve a exigência espiritual de sustentação. Não define taxa, débito ao concluir, prioridade entre manutenções ou cronômetro do mod. A economia aprovada é adaptação, não fórmula canônica. O custo básico de Shikai continua sem manutenção; não se aplica a Bankai por associação.

**Responsáveis e pendências:** M05/M07/M15 calibram débito inicial, manutenção, períodos e curvas, sem números arbitrários. K resolve a prioridade entre Bankai, Liberação e ação, preservando o recorte de Blut em M06-11T sem autorizar combinações raciais. E–L complementam fases, reação, retorno, posse e ciclo de vida; efeitos particulares e momentos executáveis de pagamento ainda precisam de ficha.

### 8.16. M07-03E — DECIDIDO — Preparo de Bankai cancelável para reagir

**Aprovado em 21/10/2026.** Resposta explícita: **“Preparo cancelável para reagir (recomendado)”**.

Preparo breve, com caminhada lenta e ajuste de direção/olhar. Ataque, bloqueio, Dodge ou Shunpo disponíveis cancelam o preparo antes de começar a ação, sem recuperação adicional só pelo cancelamento. A conclusão revalida e cobra D; nenhuma chance aleatória é acrescentada a B.

**Contrato estrutural:** validar a reação antes de abandonar o preparo ainda válido. Pedido indisponível gera aviso e mantém esse preparo, sem renovar duração ou enfileirar ação. Cancelar antes da conclusão não paga ativação nem concede benefícios de Bankai; custos e tempos anteriores continuam. Se o jogador começou em Shikai, conserva somente o Shikai anterior ainda válido; se começou selado, permanece selado. Não converter uma tentativa cancelada em liberação gratuita de Shikai.

Conservar a manifestação anterior não autoriza atacar simultaneamente durante o preparo nem preservar efeitos que perderam seus requisitos. Efeitos já existentes seguem sua ficha. Dano isolado não interrompe universalmente; controle ou perda de requisito aplicam a interrupção correspondente. Sem invulnerabilidade, Super Armor, pausa do mundo ou suspensão da queda. A fase anterior precisa permitir iniciar a liberação; não se pode cancelar qualquer ataque para entrar em Bankai.

**Alternativa não escolhida:** preparo comprometido que exige esperar a conclusão antes de reagir. Aumenta a exposição da liberação, mas reduz a possibilidade de responder a uma mudança do encontro. **Justificativa da escolha:** preparo cancelável, com exposição durante o preparo e custo legítimo quando a forma se estabelece. Duração continua para calibração; G complementa a recuperação após conclusão.

**Responsáveis:** M07/M06, fase e permissões; M05/M15, duração/ritmo; M14, sinal de preparo/recusa; M16, execução e sincronização. A semelhança com Shikai foi aprovada explicitamente para Bankai, sem copiar seu custo ou decidir por associação todas as liberações particulares.

### 8.17. M07-03F — DECIDIDO — Retorno de Bankai a Shikai ou selado

**Aprovado em 21/10/2026.** Resposta explícita: **“Retorno a Shikai sem nova cobrança (recomendado)”**.

No encerramento voluntário permitido, escolher retornar a Shikai ou selar a arma. Na falta de Energia para manutenção, retornar a Shikai se o vínculo, a arma e o estado ainda permitirem; caso contrário, não manter manifestação incompatível. Esse retorno de Bankai a Shikai não exige novo preparo ou custo de ativação de Shikai e não restaura Energia.

**Experiência:** o jogador pode reduzir o gasto e continuar usando o estágio anterior conquistado. Encerrar Bankai por falta de Energia não significa perder Shikai: sua manifestação básica não tem manutenção, conforme M07-02C. Técnicas que exigem Energia continuam indisponíveis sem recurso.

**Contrato estrutural:** a passagem encerra Bankai e seus benefícios dependentes, sem acumular bônus dos dois estágios. A ausência de nova cobrança/preparo refere-se a esse retorno, inclusive quando Bankai foi liberado diretamente do estado selado; não elimina o custo/preparo de liberar Shikai normalmente a partir da arma selada. Nova ativação de Bankai segue C/D, sem desconto, estorno ou retorno automático. Conquistas/Masteries permanecem distintas e recuperações/cooldowns anteriores continuam contando.

Pedido manual só ocorre quando a fase e os requisitos permitirem; não serve de cancelamento universal de técnica. Falta de manutenção remove a autorização de sustentar Bankai; não prolongá-lo gratuitamente para esperar uma animação terminar. A consequência sobre uma técnica dependente em andamento exige seu contrato M06/M07, sem transformar todo disparo autônomo em efeito dependente.

**Limite:** F trata encerramento voluntário e falha de manutenção com o personagem presente em condição válida. I/L complementam perda/transferência e ciclo de vida; M/N complementam quebra e recuperação, com detalhes por ficha ainda pendentes. Não restaurar Shikai depois de um evento que o encerrou em M07-02G/I. H/I complementam a condição de posse/empunhadura; F não a decidiu por associação.

**Alternativa não escolhida:** todo encerramento de Bankai por esses dois motivos retorna à arma selada, exigindo liberação normal de Shikai depois. Simplifica a saída, mas também impede a continuidade imediata em Shikai quando a Energia acabou. **Justificativa da escolha:** retorno a Shikai sem nova cobrança, com opção voluntária de selar e respeito aos requisitos.

**Responsáveis:** M07, estado resultante; M06, técnicas e efeitos dependentes; M11, arma/posse; M14, escolha/feedback; M16, transição validada. G/J/K complementam recuperação após liberação/encerramento, presença e ordem de manutenção.

### 8.18. M07-03G — DECIDIDO — Conclusão e encerramento sem recuperação adicional

**Aprovado em 21/10/2026.** Resposta explícita: **“Transições sem recuperação adicional (recomendado)”**.

Permitir as ações disponíveis após a conclusão válida de Bankai sem recuperação adicional apenas pela transformação. Encerramento voluntário para Shikai/selado é direto quando a fase permitir, sem custo adicional ou recuperação própria. O preparo de E e os custos de D continuam valendo.

**Contrato estrutural:** não apagar Recovery/cooldowns anteriores ou cancelar automaticamente ataque em andamento. A próxima ação respeita requisitos, mãos, recursos e fase. Pedido manual inválido não é enfileirado nem encerra Bankai ainda válido/pago. Ausência de espera após a transformação não permite executar duas ações incompatíveis simultaneamente. Falha de manutenção continua removendo Bankai conforme D/F; G não a transforma em solicitação voluntária que possa ser adiada.

**Alternativa não escolhida:** adicionar uma recuperação própria depois de liberar/encerrar. Dá uma janela adicional ao adversário, mas soma espera ao preparo e ao custo já existentes. **Justificativa da escolha:** sem recuperação extra no contrato básico; compromissos especiais precisam de ficha. M06/M07 definem permissões; M05/M15 calibram ritmo; M14 comunica disponibilidade. Não decide dano, bônus ou invulnerabilidade.

### 8.19. M07-03H — DECIDIDO — Bankai conservado na própria posse, com manutenção

**Aprovado em 21/10/2026.** Resposta explícita: **“Conservar Bankai com manutenção ativa (recomendado)”**.

Conservar Bankai já estabelecido ao trocar para ferramenta ou deixar de empunhar, mantendo a mesma Zanpakutō no próprio inventário. A manutenção continua sendo cobrada integralmente; técnicas e benefícios que exigem empunhadura ficam suspensos. Retomar a arma não exige nova liberação, desde que a manifestação ainda esteja válida.

**Contrato estrutural:** guardar não pausa cobranças, não restitui Energia, não conserva preparo incompleto ou técnica que perdeu requisito. Troca precisa respeitar a fase e não apaga tempos. Benefícios que não exigem a arma empunhada dependem da ficha; não atribuir todos os efeitos da forma à ferramenta. Se faltar manutenção enquanto guardada, aplica F: encerra Bankai e conserva somente Shikai quando válido, sem restauração automática de Bankai ao reequipar. O retorno a Shikai exige sua própria posse válida, conforme M07-02F/G.

**Alternativa não escolhida:** encerrar Bankai ao deixar de empunhar, retornando conforme F. Evita gastar durante uma troca para ferramenta, mas também faz toda troca exigir nova liberação de Bankai se o jogador quiser retomá-lo. **Justificativa da escolha:** conservar com custo e permitir que o jogador encerre voluntariamente para economizar.

**Limites e responsáveis:** empunhadura é condição funcional de equipamento; dispersão/desmaterialização da arma pela própria técnica não equivale automaticamente a guardar ou perder posse. Áreas, invocações, corpo, lâminas dispersas e bônus exigem ficha para continuidade/cobranças. M11 define slots e armazenamento; M06/M07 definem dependências; M14 informa manutenção mesmo fora da interface. H não garante persistência de todos os efeitos no mundo nem suspensão de seus custos próprios.

### 8.20. M07-03I — DECIDIDO — Perda de posse encerra Bankai

**Aprovado em 21/10/2026.** Resposta explícita: **“Encerrar Bankai ao perder posse (recomendado)”**.

Encerrar Bankai e capacidades dependentes ao soltar a arma, depositá-la em baú ou transferi-la. Vínculo, conquista e Mastery permanecem; quem recolher o item não recebe o poder. Recuperar a arma não restaura Bankai: nova liberação válida paga D.

**Contrato estrutural:** nessa perda de posse não se restaura Shikai por F, pois M07-02G já encerra sua manifestação no mesmo evento. Não devolver custos pagos, resetar tempos ou concluir uma preparação interrompida pela perda do item. Efeitos autônomos emitidos seguem M06-11W; os dependentes encerram quando perdem o requisito. Não recolher indistintamente projéteis ou desfazer impactos resolvidos.

**Alternativa não escolhida:** conservar Bankai vinculado ao dono com a arma fora da posse, mantendo seus custos. Permite uma separação entre usuário e arma, mas torna drop/baú/transferência formas de conservar poder sem o equipamento e exige contratos de distância/propriedade próprios. **Justificativa da escolha:** encerramento por perda de posse no modelo básico; exceções reais de vetor exigem ficha. Uma lâmina lançada/dispersa por uma técnica não é automaticamente item transferido.

**Responsáveis:** M11/M16, posse/vínculo/transferência e cópias de itens; M06/M07, dependências; M14, feedback. Não aprova arma indestrutível, reposição automática, keepInventory ou perda permanente de Bankai. L complementa morte/logout/dimensão/reinício. M/N complementam o modelo básico de quebra; causas/consequências particulares e armazenamento modificado ainda exigem ficha.

### 8.21. M07-03J — DECIDIDO — Bankai e estados de Reiatsu

**Aprovado em 21/10/2026.** Resposta explícita: **“Bankai compatível com os estados de Reiatsu (recomendado)”**.

Permitir Bankai básico com Natural, Supressão ou Liberação de Reiatsu. Manifestar somente a forma, sem ataque associado, não é início ofensivo automático. Uma ofensiva válida continua encerrando Supressão conforme M06-11J; pedido ofensivo recusado não a encerra.

**Contrato estrutural:** Supressão não reduz atributos da forma nem torna arma, corpo ou efeitos invisíveis; a leitura continua considerando presença realmente emitida, Controle, domínio e condições. Compatibilidade não garante ocultação de Bankai. Liberação mantém sua própria manutenção/opressão, sem bônus automático de dano; Bankai paga D. Ativar/encerrar a forma não ativa Liberação nem troca automaticamente a presença. A opressão básica continua exigindo Liberação deliberada/paga; não concedê-la gratuitamente a todo Bankai.

**Alternativa não escolhida:** exigir presença Natural enquanto Bankai estiver ativo. Simplifica combinações, mas impede usar as capacidades de presença já aprendidas junto da forma mesmo quando não há conflito de execução. **Justificativa da escolha:** convivência básica com sinais e custos próprios. Ofensivas sustentadas, áreas e particularidades que prejudiquem Supressão precisam de ficha; J não autoriza ocultar qualquer efeito ofensivo ativo. M06/M07 definem compatibilidade e M14 comunica sinal/estado. Nenhuma manutenção ou fidelidade de ocultação foi comprovada pelas fontes como regra universal; trata-se de adaptação aprovada.

### 8.22. M07-03K — DECIDIDO — Bankai antes de Liberação nas cobranças coincidentes

**Aprovado em 21/10/2026.** Resposta explícita: **“Bankai → Liberação → ação (recomendado)”**.

Quando Bankai e Liberação forem compatíveis, pagar primeiro a manutenção de Bankai realmente devida, depois a de Liberação, e só então o custo da ação solicitada no seu momento próprio. Preserva a forma escolhida antes da pressão exterior. Não cobrar períodos futuros nem transformar custos em um pacote indivisível.

**Contrato estrutural:** estado inválido deixa de gerar obrigações futuras, sem estorno de cobranças legítimas. Se não puder pagar Bankai, encerrá-lo e aplicar F quando válido, sem débito parcial ou benefício gratuito; ainda tentar Liberação com o saldo. Se Liberação não puder pagar, voltar a Natural sem desfazer Bankai já pago. A ação usa o recurso restante; falha própria não restitui manutenções. Nenhum estado reativa sozinho ao recuperar Energia. Valores e momentos de vencimento precisam de ficha/M05/M15/M16.

Custo inicial de Bankai é custo de uma ação na conclusão, não manutenção de uma forma ainda inexistente: manutenções já devidas precedem essa ativação conforme M06-11T. Stamina continua independente. A prioridade já aprovada de Blut antes de Liberação permanece no seu recorte; K não autoriza Bankai com Blut nem ordena combinações raciais ainda não permitidas. Formas não compatíveis sequer chegam à resolução conjunta.

**Alternativa não escolhida:** priorizar Liberação e usar o saldo para Bankai. Preserva pressão exterior, mas pode derrubar a forma enquanto mantém uma capacidade que não concede suas técnicas. **Justificativa da escolha:** Bankai antes de Liberação; o jogador pode encerrar voluntariamente um estado para evitar obrigações futuras, sem apagar cobrança já devida. Custos são adaptações do mod, sem atribuição de prioridade ao cânone.

### 8.23. M07-03L — DECIDIDO — Encerrar Bankai no ciclo de vida; retorno manual

**Aprovado em 21/10/2026.** Resposta explícita: **“Encerrar manifestações; retorno manual (recomendado)”**.

Morte, logout, troca de dimensão e reinício encerram Bankai e seu preparo, retornando à arma selada sem restaurar Shikai. Vínculo, conquista e Mastery persistem; retornar ao jogo exige nova liberação válida se o jogador a desejar, pagando seu custo normal.

**Contrato estrutural:** esse encerramento não é a falha de manutenção de F; não conservar/restaurar Shikai em desacordo com M07-02I. Custo de uma liberação não concluída não é cobrado; gastos legítimos anteriores não são devolvidos. Cooldowns/Recovery seguem M06-11V, sem reset ou treino/regeneração offline. Manutenção da forma encerrada não continua sendo cobrada offline e comandos/preparos não são retomados.

Efeitos autônomos emitidos seguem M06-11W; dependentes encerram quando perdem requisito, sem desfazer impactos ou limpar todas as condições no personagem. L não decide recursos de respawn, keepInventory, perda/reposição da arma, consequências de PvP ou dano permanente por quebra de Bankai. M07/M11/M15/M16 conservam essas responsabilidades.

**Alternativa não escolhida:** restaurar Bankai automaticamente ao retornar, quando recurso/arma permitirem. Retoma a configuração anterior, mas também reinicia poder e consumo sem um novo pedido. **Justificativa da escolha:** encerramento com retorno manual, coerente com a separação conquista/manifestação. M14 informa estado e M16 especifica persistência/sincronização; não há evidência de implementação ou testes dessas regras nesta rodada.

### 8.24. M07-03M — DECIDIDO — Quebra estrutural por causas explícitas

**Aprovado em 21/10/2026.** Resposta explícita: **“Quebra por condições específicas (recomendado)”**.

Uma quebra estrutural de Bankai só pode ocorrer por condição prevista na ficha do poder, técnica ou encontro. Receber dano comum, bloquear, perder Postura, ficar sem Energia ou morrer não causam essa quebra automaticamente. Não criar uma segunda barra universal de durabilidade para todos os Bankais.

**Contrato estrutural:** diferenciar fragmento visual, componente consumido pela própria técnica, fim normal da forma, dano corporal e quebra estrutural verdadeira. A ficha precisa indicar o componente atingível, a causa capaz de danificá-lo, a consequência, o feedback e a possibilidade de reação; o servidor valida o evento. Materializar uma espada ou invocação não as torna automaticamente itens com durabilidade vanilla. Dano ao item físico e perda/reposição da Zanpakutō continuam em M11, sem decidir indestrutibilidade por omissão.

**Alternativa não escolhida:** todo Bankai possui integridade própria reduzida por golpes até quebrar. Oferece uma regra comum de desgaste, mas exige dar uma integridade equivalente a vetores muito diferentes, incluindo armas, corpos, áreas e invocações. **Justificativa da escolha:** causas e componentes explícitos por ficha, preservando a identidade dos poderes. Não significa quebra somente por cena roteirizada ou ausência de reação; encontros que a permitam precisam de contrato jogável.

**Responsáveis:** M06/M07, condições e efeito; M09/M13, encontro/contexto; M11, distinção de item; M14, aviso/feedback; M15, elegibilidade e consequências multiplayer; M16, estado autorizado. M não aprova nenhum ataque/encontro concreto, destruição de base ou perda de progresso. N complementa a consequência persistente e recuperação, aprovado por resposta própria.

### 8.25. M07-03N — DECIDIDO — Consequência persistente com recuperação específica

**Aprovado em 21/10/2026.** Resposta explícita: **“Dano persistente com recuperação específica (recomendado)”**.

Se ocorrer a quebra estrutural válida de M, registrar dano que mantenha a parte/capacidade afetada limitada ou indisponível até uma recuperação específica. Vínculo, conquista e Mastery não são apagados. Não há perda irreversível automática do Bankai adquirido.

**Contrato estrutural:** a ficha declara alcance parcial/total da consequência e quais usos ainda são válidos. Não presumir que todo componente quebrado destrói o Bankai inteiro, que Shikai também foi perdido ou que uma forma quebrada pode continuar completa. A ativação confiável de B continua sujeita aos requisitos atuais. Uma nova liberação, selamento, retorno a Shikai, morte/respawn, logout, troca de dimensão ou reinício não apagam o dano registrado. L encerra manifestação, mas não repara dano persistente. Recuperar o item ou fabricar uma cópia não limpa o registro associado ao vínculo.

A recuperação precisa ser adequada ao poder e pode envolver orientação, prática, recurso ou jornada específica; não é reparo universal por bigorna, Mending ou espera offline. Exceções realmente ligadas ao corpo ou ao próprio poder exigem fonte e ficha, sem impor a mesma missão a todos. **Antes de introduzir um risco de quebra**, sua recuperação precisa estar disponível no recorte de conteúdo e explicada ao jogador. Não disponibilizar uma punição cujo caminho de recuperação ainda não existe. Método, custo, duração e conteúdo concreto pertencem às fichas/M08/M11/M12/M13/M15, sem inventá-los nesta decisão.

**Alternativa não escolhida:** dano irreversível como regra geral, salvo exceções canônicas específicas. Preserva a gravidade narrativa descrita na fonte, mas pode inutilizar uma conquista central de um personagem persistente sem caminho jogável de retorno. **Justificativa da escolha:** consequência persistente com recuperação específica, identificada como adaptação de continuidade do mod. Não afirmar que todo Bankai da obra pode ser reparado dessa maneira.

**Responsáveis:** M07 registra condição/conquista separadamente; M06 aplica limitações; M08/M11/M12/M13 definem e oferecem a recuperação; M14 comunica o que ficou comprometido e o caminho disponível; M15 regula acesso/contexto multiplayer; M16 garante persistência/migração. Ficha sem esses contratos não autoriza implementar a quebra. O evento não devolve Energia, reseta cooldowns ou concede Mastery por quebrar/reparar repetidamente.

### 8.26. M07-04 — Evolução Hollow: abertura e herança

**Abertura e consolidação estrutural: 21/10/2026. Estado do bloco: `DECIDIDO` em nível estrutural.** A–M aprovados por treze respostas explícitas, seções 8.27–8.39. Nenhuma pergunta básica deste bloco pendente; fichas, valores, conteúdo e realização técnica continuam delegados. M07 inteiro permanece `EM ESPECIFICAÇÃO`. A tabela abaixo consolida a herança; o fechamento ao final da seção 15 registra revisão e condições de retorno.

| Contrato herdado | Aplicação ao bloco |
|---|---|
| M04/H1–H5 e H7 | Sobrevivência corporal, predação para avanço e desenvolvimento antes da Arrancarização; sem Academia Hollow ou segunda barra permanente de fome |
| M04/H3/H9 e M06-08G | Repetição contribui, mas grandes marcos exigem preparo/contexto; manter agência do jogador Gillian |
| M04/H8/H9 | Arrancarização possível em Gillian/Adjuchas/Vasto Lorde encerra a evolução normal no estágio escolhido; Vasto Lorde permanente, difícil e determinístico |
| REV-H6-01 e M06-08G | Identidade principal pode começar antes da Arrancarização; acesso compatível também para Arrancar de origem Gillian, sem retorno à evolução encerrada |
| M06-08A–D/G | Três campos de Proficiência, Masteries individuais e perfis corporais funcionais; não criar quarta Proficiência racial ou copiar habilidades de cada presa |
| M06-08G e M07-01 | Construir, explorar ou ficar offline não provoca regressão passiva; G/M complementam estabilidade e retenção no percurso básico. Regressão extraordinária exigiria revisão explícita |

**Experiência a preservar:** um Gillian jogável continua sendo o personagem do jogador, com suas decisões e identidade; a consciência dominante dá contexto à jornada. Isso não resolve tamanho, câmera ou colisão. Tampouco concede Adjuchas, Arrancarização ou poder principal automaticamente. Não exigir sorte oculta de nascimento para permitir a rota Vasto Lorde já aprovada.

**Distinção de progresso:** consumir para evoluir não é receber automaticamente Proficiência, Mastery, atributos, técnicas da presa ou cura. Cada sistema conserva sua própria regra. A expressão histórica “Mastery racial elevada” de H9 deve ser concretizada pelo preparo e domínio pertinentes já existentes, sem criar um indicador racial comprado ou exigir Mastery do estágio ainda inacessível. D/E/F definem a organização dos marcos por transição; seus objetivos, critérios de domínio e valores concretos ainda precisam de ficha.

**Fronteira:** M07 define as operações e condições de evolução; M06, execução corporal e consumo; M09, presas/encontros; M08/M12/M13, orientação e acontecimentos; M10/M11, mundo/itens; M14, informação; M15, participação/calibração; M16, persistência/autoridade. Esta abertura não seleciona arte, mestre, prova ou técnica de linhagem.

### 8.27. M07-04A — DECIDIDO — Devorar deliberadamente uma presa derrotada

**Aprovado em 21/10/2026.** Resposta explícita: **“Devorar por interação após derrotar (recomendado)”**. A necessidade de predação já está aprovada. A resposta aprova a interação breve e o registro somente na conclusão; os complementos de representação, itens e execução abaixo ainda precisam de ficha.

**Regra aprovada:** depois de derrotar uma presa elegível, o Hollow solicita uma interação breve de devoração no local. Só a conclusão válida registra o consumo para evolução; matar ou estar perto não consome automaticamente. A disponibilidade inicial deve acompanhar os fundamentos Hollow, sem exigir Sonído, Arrancarização ou técnica conquistada com a própria predação.

**Desdobramentos de ficha ainda em elaboração:** o servidor verifica personagem, presa, acesso e conclusão; a mesma oportunidade não gera créditos repetidos por insistência, pacote duplicado ou duas solicitações simultâneas. O registro é de progresso pessoal, sem virar item de alma negociável, guardável em baú ou coletável por funil. A representação pode ser espiritual e não precisa de um cadáver físico persistente. Oportunidade de uma presa e divisão entre participantes ainda precisam da regra de B/M15; A não decide que a primeira pessoa a clicar rouba o crédito das demais.

Cancelar antes de concluir não registra consumo; consumir não copia técnicas, concede cura universal ou satisfaz sozinho um grande marco. H/I definem as reações/interrupções básicas e a ausência de custo adicional; M encerra a execução incompleta nos eventos de ciclo de vida. Alcance, duração, mãos/corpo, meios, representação, prazo da oportunidade e seu comportamento ao descarregar/reiniciar continuam requisitos da futura ficha. Não presumir consumo instantâneo, pausa do mundo, imunidade ou restauração de presas ao relogar.

**Alternativa não escolhida:** consumo automático quando uma derrota elegível for atribuída ao personagem. Reduz interações repetidas, mas aproxima predação de XP por abate e retira a escolha de se aproximar e consumir. **Justificativa da escolha:** dar uma ação reconhecível à fantasia Hollow; a execução deve permanecer breve e legível para não dominar todo o ritmo de caça.

### 8.28. M07-04B — DECIDIDO — Presas e participação para avanço

**Aprovado em 21/10/2026.** Resposta explícita: **“Presas elegíveis e participação efetiva (recomendado)”**. Complementa A; a rota natural continua viável em PvE. Participação, exclusões e relevância por estágio foram aprovadas na pergunta; critérios objetivos e distribuição de oportunidades continuam pendentes.

**Regra aprovada:** a fonte geral de predação evolutiva são Hollows NPCs inimigos elegíveis, incluindo classes Menos conforme o estágio/desafio. Exigir participação efetiva na caçada/encontro, sem exigir o golpe final. Jogadores, aliados, pets e mortes inteiramente automáticas não fornecem esse progresso. Presas muito abaixo do preparo do personagem não substituem as exigências dos estágios avançados.

**Desdobramentos de ficha ainda em elaboração:** participação e consumo são requisitos distintos; proximidade ou pertencer à party não bastam. A contribuição precisa considerar ações úteis apropriadas ao encontro, sem reduzir tudo ao maior dano. Os critérios objetivos e a distribuição de oportunidades entre participantes são pendências de M09/M15, necessárias antes da ficha executável. Não decidir nesta rodada crédito exclusivo, alma dividida, uma cópia para cada membro ou pesos numéricos.

Grind ativo é válido. Construir uma arena, usar terreno ou reencontrar uma espécie não invalida automaticamente a atividade; distinguir participação real de ganho AFK/sem ação. A configuração de presa informa sua contribuição e os limites por estágio. Arrancars, invocações, clones, representações repetíveis de personagens e casos de campanha exigem classificação específica; não tratá-los todos como presas comuns por compartilharem origem Hollow.

Purificação/destruição e o destino da alma seguem RAC-19 e os responsáveis por combate/encontros. A participação não faz reaparecer uma alma efetivamente removida nem aprova que qualquer método de derrota produza resto consumível; essa resolução precisa estar definida antes do conteúdo multirracial. Devoração evolutiva não é a absorção especial de capacidades de Glotonería.

**Alternativa não escolhida:** qualquer Hollow derrotado fornece progresso equivalente se puder ser consumido, independentemente de participação ou desafio. É simples, mas permite que presas fracas e produção automática resolvam a rota avançada. **Justificativa da escolha:** manter a caçada relevante e a cooperação possível, preservando grind ativo sem transformar último golpe ou PvP em pedágio.

### 8.29. M07-04C — DECIDIDO — Início deliberado da evolução preparada

**Aprovado em 21/10/2026.** Resposta explícita: **“Iniciar evolução deliberadamente (recomendado)”**. Aplica-se a Hollow → Gillian → Adjuchas → Vasto Lorde, sem decidir Arrancarização/Resurrección por associação.

**Regra aprovada:** cumprir predação, preparo e marco próprio torna a próxima evolução disponível; o jogador inicia deliberadamente a transição quando os requisitos forem válidos. O último consumo ou objetivo não muda o corpo automaticamente no meio de outra atividade. Adiar o pedido não provoca regressão passiva nem apaga requisitos já concluídos.

**Desdobramentos de ficha ainda em elaboração:** disponibilidade não equivale a evolução concluída. A operação revalida estágio/rota, condições de execução e espaço do corpo; pedido impossível mantém o estágio anterior e explica o impedimento, sem atravessar/apagar blocos ou encolher gratuitamente o perfil. C isoladamente não definiu custo, fases ou ciclo de vida: G/J/K/L/M complementam atualmente falha, espaço, economia, preparo fora de combate e encerramento de execuções incompletas. Não há aprovação de teleporte, forma compacta, invulnerabilidade ou animação específica. Duração, critérios concretos de apoio/meios e ficha do perfil continuam pendentes antes da execução.

Uma evolução concluída registra o novo estágio, sem voltar livremente ao anterior. Não repõe recursos, apaga tempos ou entrega automaticamente capacidades incompatíveis. A conquista de Vasto Lorde e o encerramento da evolução normal ao Arrancarizar continuam regidos por H8/H9; não se pode usar um pedido de evolução para contornar a rota adquirida. O servidor é o responsável pela operação.

**Alternativa não escolhida:** evolução automática assim que todos os requisitos forem cumpridos. Dá continuidade imediata ao acontecimento, mas pode alterar corpo/colisão numa base, passagem ou combate sem o jogador escolher o momento. **Justificativa da escolha:** permitir preparação do local e leitura da consequência, preservando o peso da conquista.

### 8.30. M07-04D — DECIDIDO — Formação Gillian por agregação de Hollows

**Aprovado em 21/10/2026.** Resposta explícita: **“Formação por agregação de Hollows (recomendado)”**. A/B/C aprovados continuam governando predação, participação e início deliberado.

**Regra aprovada:** o primeiro Gillian exige predação válida e preparo demonstrado nos fundamentos Hollow, culminando num acontecimento de agregação de Hollows NPCs. O personagem participa e estabelece sua consciência como dominante no conjunto. Esse acontecimento registra o marco de formação; consolidar o novo corpo Gillian exige o pedido deliberado de C, que pode acontecer ao concluir o marco ou depois, com requisitos e espaço válidos. Adiar não apaga o marco nem exige repeti-lo. Não há mudança automática ao último abate ou aquisição repetida a cada sessão.

**Experiência:** a mudança mostra que Gillian resulta de uma existência composta; não parece apenas crescer depois de encher uma barra. Consciência e controle do jogador permanecem, conforme a herança aprovada. Não exigir fusão, sacrifício ou perda de personagem de outro jogador. A contribuição narrativa de muitas almas não exige centenas de entidades simultâneas ou repetir literalmente uma contagem da obra.

**Detalhamento de ficha ainda pendente:** as presas/participação e o registro de predação seguem A/B. O conteúdo precisa verificar preparo que já possa ser adquirido como Hollow; não exigir Cero, Sonído, Mastery de Gillian ou três campos maximizados antes dessa conquista. O acontecimento pode ter ameaça, resistência ou disputa coerente, mas sua forma jogável ainda será construída por M09/M12/M13. Não foi escolhido boss obrigatório, comando de minijogo, perda de controle, local/dimensão, arena privada ou NPC mestre. A autoridade e a transição corporal seguem M07/M16; G/J/L/M já definem as regras básicas de falha, espaço, interrupção e ciclo de vida. O acontecimento concreto, seus pontos de retomada e a ficha corporal continuam pendentes. D não aprova perda de predação acumulada ou sorte de falha.

**Alternativa não escolhida:** a predação acumulada prepara uma assimilação pessoal e uma prova de formação, sem encontro específico de agregação; as almas já assimiladas dão o contexto Menos. Mantém preparo e marco, com menor custo de conteúdo, mas a formação coletiva fica menos visível. **Justificativa da escolha:** um acontecimento de agregação dá identidade própria ao primeiro grande marco Hollow. É adaptação de apresentação e desafio, não uma quest canônica existente.

**Responsáveis:** M07 define requisitos/resultado; M06/M09, ações e participantes; M10, local e espaço; M12/M13, acontecimento/retomada; M14, clareza e controle; M15, cooperação; M16, operação e persistência. Conteúdo concreto e números não estão aprovados por esta escolha.

### 8.31. M07-04E — DECIDIDO — Adjuchas por domínio próprio, sem linhagem obrigatória

**Aprovado em 21/10/2026.** Resposta explícita: **“Conquista racial sem linhagem obrigatória (recomendado)”**. A conquista racial não exige escolher o poder principal; agência Gillian e grandes marcos além de abates continuam vigentes. Conteúdo concreto da prova não foi escolhido.

**Regra aprovada:** depois de viver e desenvolver o estágio Gillian, combinar predação relevante, prática com seu corpo e uma prova própria de predominância da identidade. Isso habilita a evolução deliberada para Adjuchas sem obrigar a escolha definitiva de Pantera ou outra linhagem antes. Uma quest de linhagem pode incorporar essa mesma prova quando compatível, sem exigir um segundo desafio racial idêntico.

**Experiência:** o jogador conquista um corpo individualizado pelo desenvolvimento racial. Pode começar uma relação de linhagem no limiar de Adjuchas, conforme H6, ou escolher seu poder principal depois. Independência não significa duas vias com recompensas duplicadas; ambas solicitam e validam a mesma conquista racial. Este modelo também não bloqueia Arrancarização em Gillian enquanto ainda não seguiu para Adjuchas.

**Detalhamento de ficha ainda pendente:** a predominância não é recuperar controles que o jogador perdeu ao virar Gillian; é demonstrar desenvolvimento suficiente para o próximo estágio. Não exigir Mastery de Adjuchas antes da aquisição, maximizar todas as capacidades Gillian, cargo/facção, um mentor obrigatório ou genética aleatória. A prática usa indicadores e evidências pertinentes já existentes; não criar uma quarta Proficiência/medidor permanente de instinto. Cero pode ser parte de uma ficha apropriada, mas esta decisão não o estabelece como pedágio universal.

Prova, prática e predação precisam formar requisitos verificáveis e distintos; uma quantidade de abates sozinha continua insuficiente. M09/M12/M13 detalharão a demonstração, dificuldade, falha/retomada e eventuais alternativas compatíveis. Não é aprovação de duelo interior, boss, arena, QTE, regressão ativa ou punição irreversível. Estágio/identidade/perfil são registros distintos: virar Adjuchas sem linhagem escolhida precisa de perfil funcional coerente, a definir na ficha, sem obrigar a seleção de arte agora. Escolher a linhagem depois não concede troca corporal gratuita ou mudança de golpes/colisão fora dos contratos.

**Alternativa não escolhida:** exigir a quest de uma linhagem escolhida para conquistar Adjuchas. Conecta imediatamente evolução e identidade do poder, mas obriga compromisso com uma linhagem para continuar a rota natural e condiciona toda evolução ao catálogo disponível. **Justificativa da escolha:** manter a conquista racial acessível e integrar a linhagem quando o jogador já a estiver desenvolvendo, sem duplicar etapas ou tornar essa escolha um requisito universal. H6 permite a preparação antecipada, mas não a tornou obrigatória para todo Adjuchas.

**Responsáveis:** M07, requisitos e operação; M06, prática/corpo/capacidades; M08/M12/M13, fontes e prova; M14, requisitos/resultado; M15, contribuição e dificuldade; M16, registro pessoal. Compatibilidades de cada linhagem continuam para a etapa própria.

### 8.32. M07-04F — DECIDIDO — Rota Vasto Lorde com marcos registrados e prova final

**Aprovado em 21/10/2026.** Resposta explícita: **“Marcos registrados + prova final (recomendado)”**. H9 já aprovou evolução permanente, exigente e determinística. A resposta aprova a organização da jornada em marcos registrados e prova final; não escolhe presas, objetivos, valores ou toda sugestão de ficha.

**Regra aprovada:** permanecer como Adjuchas na evolução Hollow e cumprir uma rota avançada com marcos registrados de predação relevante, domínio demonstrado do próprio repertório e um desafio final de ascensão. O jogador pode dividir o percurso entre sessões; ao concluir um marco, não precisa refazê-lo apenas por adiar a próxima atividade. Concluir a rota habilita o pedido de evolução de C, sem transformação automática no último objetivo.

| Eixo aprovado | Resultado que a futura ficha precisa verificar |
|---|---|
| Predação avançada | Participar de caçadas/encontros de dificuldade adequada ao Adjuchas e concluir consumo elegível conforme A/B; presas fracas não substituem o eixo avançado |
| Domínio do estágio | Demonstrar execução e adaptação com fundamentos/capacidades já adquiridos no corpo Adjuchas; não exigir todas as técnicas, três campos máximos ou Mastery de Vasto Lorde antes da conquista |
| Ascensão | Superar um desafio final que avalie esse preparo, com critério de conclusão próprio; não basta repetir uma contagem de abates ou ter Battle Power alto |

**Detalhamento de ficha ainda pendente:** a composição precisa continuar viável enquanto Hollow; não exigir Resurrección, Sonído Arrancar, poder de Vasto Lorde, cargo Espada ou Arrancarização como pré-requisito circular. Não acrescentar moeda, quarta Proficiência, cronômetro de fome, fila de espera em tempo real ou sorte por tentativa. A escolha de uma linhagem pode orientar conteúdo compatível; uma linhagem canônica específica não fica aprovada como requisito universal por esta decisão. Fichas devem informar marcos/requisitos compreensíveis ao jogador; objetivos secretos concretos não foram definidos.

Dificuldade e variedade devem vir dos confrontos e demonstrações exigidos, sem transformar a rota numa repetição ilimitada de presas descartáveis. Quantidades, presas, locais, domínio necessário, objetivos, uso de cooperação e prova concreta continuam para M05/M06/M09/M10/M12/M13/M15. Não foi escolhido matar outro Vasto Lorde, derrotar um personagem canônico específico ou fazer tudo sozinho. Cada participante precisa da conquista pessoal; party não a entrega por presença. G define retenção de conquistas e falha limitada à tentativa. Conteúdo de retomada, transição e mudança de corpo ainda precisam de ficha própria.

**Alternativa não escolhida:** manter o preparo prévio de H9 e concentrar a comprovação numa única grande prova de ascensão, sem sequência própria de marcos intermediários registrados. Pode produzir um clímax mais concentrado, com menos variedade de atividades; exige fazer caber toda a avaliação nessa prova. **Justificativa da escolha:** uma rota em etapas comunica o progresso de uma conquista longa e permite conciliar a jornada com construção, exploração e sessões de durações diferentes. Exigência não depende de impedir pausas.

**Limites:** cumprir todos os requisitos precisa permitir a conquista, sem potencial secreto sorteado. Vasto Lorde não concede invencibilidade, multiplicador universal, Resurrección/Segunda Etapa ou restituição de recursos. H9 continua impedindo a evolução normal de um Arrancar de origem Adjuchas para Vasto Lorde; F não cria uma exceção a essa escolha de rota.

### 8.33. M07-04G — DECIDIDO — Estabilidade dos estágios e falha limitada à tentativa

**Aprovado em 21/10/2026.** Resposta explícita: **“Falha limitada à tentativa, sem regressão racial (recomendado)”**. A ausência de regressão passiva já é herança. Esta aprovação complementa o efeito de falha, dano ou morte comum sobre Gillian/Adjuchas consolidados e o progresso de ascensão. A permanência de Vasto Lorde em H9 continua vigente.

**Regra aprovada:** estágios Hollow já conquistados permanecem estáveis no percurso básico. Dano, morte comum ou falha numa prova não retiram Gillian/Adjuchas nem apagam predação e marcos pessoais já concluídos. Uma tentativa malsucedida não concede a evolução e pode exigir repetir a parte do desafio que ainda estava em andamento. Gastos e consequências normais do confronto permanecem; não há reembolso automático.

**Efeito para o jogador:** ele pode enfrentar uma prova difícil, falhar, preparar-se melhor e tentar novamente sem reconstruir toda a jornada racial. Isso não garante vencer, conservar a execução interrompida, recuperar itens ou desfazer o estado do mundo. O critério de dificuldade continua na prova e nos requisitos que ainda faltam.

**Detalhamento de ficha ainda pendente:** distinguir estágio consolidado, requisito concluído, progresso parcial da tentativa e execução transitória. Estágio consolidado e requisitos concluídos permanecem; progresso parcial da tentativa só é retomado quando a ficha oferecer esse ponto de retomada. Não restaurar carga, animação, alvo ou combate interrompido ao relogar. Prova falha não completa o marco; requisito já validado não é apagado para obrigar repetir caçadas. Encontro, recursos, itens, recompensas únicas e reinício de uma tentativa seguem seus contratos; preservar progresso pessoal não ressuscita presas consumidas, duplica loot ou concede crédito novamente por um marco concluído.

Não atribuir regressão ao jogador por um ataque qualquer, por ter parte do corpo visualmente danificada, por outra pessoa consumir uma presa ou por perder a guarda. Referências narrativas de regressão não viram automaticamente um efeito PvP. Condições extraordinárias capazes de alterar a evolução ou impedir definitivamente Vasto Lorde precisariam de revisão explícita; esta decisão não as autoriza por uma futura ficha isolada. Não decide danos especiais de Bankai, consequências de Resurrección, recuperação de itens ou penalidades gerais de PvP.

**Alternativa não escolhida:** permitir regressão temporária recuperável de Gillian/Adjuchas em falhas de provas específicas, com risco avisado e caminho de retorno existente. Dá peso adicional ao risco, mas exige revisitar corpos/estágios já conquistados e oferecer recuperação própria; não poderia causar perda irreversível, regressão passiva ou revogar H9 sem outra revisão. **Justificativa da escolha:** preservar os estágios e limitar a falha à tentativa, mantendo o desafio sem usar a repetição da jornada inteira como punição.

**Fonte e adaptação:** BL-25 e a sinopse oficial do episódio 167 descrevem regressão na condição Adjuchas, não uma política de falha em provas de jogo. Estabilidade e retenção de progresso são adaptações aprovadas do mod. M07 define os registros; M09/M12/M13 detalham falha/retomada; M11/M15 delimitam itens/consequências; M14 comunica; M16 persiste e sincroniza.

### 8.34. M07-04H — DECIDIDO — Devoração breve e parada, cancelável para reagir

**Aprovado em 21/10/2026.** Resposta explícita: **“Parado, com cancelamento para reagir (recomendado)”**. Complementa a interação de A, sem reabrir presas/participação de B.

**Regra aprovada:** executar a devoração básica parado e perto da oportunidade elegível, podendo olhar ao redor. Pode ser pedida dentro ou fora de Combat State, se a fase atual permitir. Caminhar, correr ou saltar cancela; ataque, guarda, Dodge ou mobilidade disponíveis cancelam antes de começar a reação. Não há recuperação adicional própria depois de concluir ou cancelar, e os tempos anteriores continuam valendo.

**Detalhamento de ficha ainda pendente:** validar reação antes de abandonar uma devoração ainda válida; pedido indisponível gera aviso e a preserva, sem renovar prazo/enfileirar ação. Apenas a conclusão válida consome a oportunidade atribuída e registra o progresso de A/B; cancelar não entrega crédito parcial nem gasta essa oportunidade por si só. Uma nova tentativa reinicia o preparo. Isso não congela o prazo de disponibilidade da presa nem decide sua duração.

Dano isolado não é interrupção universal, conforme o princípio de M06; controle ou deslocamento que invalide a execução, perda de alcance/oportunidade ou estado incompatível encerram a tentativa. A ação não concede invulnerabilidade, Super Armor ou cura para suportar os golpes. Ficar parado não suspende gravidade, queda ou riscos ambientais. O percurso racial permanece: devorar não ensina mobilidade nem autoriza cancelar qualquer técnica anterior.

**Alternativa não escolhida:** permitir caminhada lenta durante a devoração, mantendo alcance válido. Facilita reposicionar-se, mas reduz a exposição e torna o ato menos distinto das leituras/cargas móveis. **Justificativa da escolha:** interação breve e estacionária, com possibilidade de reagir, preservando a escolha de quando se expor para consumir. Duração, alcance, meios, mãos/controles, apresentação e compatibilidade com estados sustentados ainda precisam de ficha/M06/M14/M15. Não foi escolhido uso exclusivo fora de combate ou minijogo.

### 8.35. M07-04I — DECIDIDO — Devoração básica sem custo adicional de recurso

**Aprovado em 21/10/2026.** Resposta explícita: **“Sem custo adicional de Energia ou Stamina (recomendado)”**. Define somente a interação básica de predação evolutiva; poderes especiais de absorção conservam ficha própria.

**Regra aprovada:** não cobrar Energia Espiritual ou Stamina por iniciar, manter ou concluir a devoração básica. A ação exige oportunidade válida e o compromisso de execução de H, sem bloquear o avanço apenas porque a reserva atual está vazia. Continua sujeita a condições de vida, corpo, fase e acesso; Energia zero não elimina esses requisitos.

**Complemento nutricional aprovado em 23/10/2026 — M11-02E:** concluir a devoração evolutiva já elegível também repõe saciedade na barra de fome existente, conforme [M11/8.10](11-itens-e-economia.md#810-m11-02e--alimentação-comum-e-devoração-nutritiva--decidido). Cancelar/falhar não nutre; a quantidade fica para M05/M11. Sem ampliação de presas/concorrência ou evolução Arrancar.

Consumo evolutivo registra seu próprio progresso; não restaura automaticamente Vida, Energia ou Stamina e não ensina a técnica da presa. Regeneração, recuperação natural ou efeitos já aprendidos conservam suas próprias regras. Ausência de cobrança não concede prática/Mastery por clicar, cancelar ou repetir uma oportunidade já resolvida. O pagamento de outra manutenção ou ação continua normal.

**Alternativa não escolhida:** pequeno custo de Stamina ao concluir. Faz a interação participar do ciclo físico, mas pode acrescentar uma espera por recurso após uma caça já paga, sem ser a principal limitação do progresso. **Justificativa da escolha:** deixar a exigência na caçada, elegibilidade e exposição da interação, sem uma taxa adicional para registrar a predação. É adaptação do mod; nenhuma fonte consultada demonstra uma tabela canônica de custo. I não decidiu o custo de evoluir; K o define separadamente, sem taxa adicional. Custos de Arrancarização e Resurrección continuam próprios, sem aprovação por associação.

### 8.36. M07-04J — DECIDIDO — Novo corpo deve caber na posição escolhida

**Aprovado em 21/10/2026.** Resposta explícita: **“Exigir espaço na posição atual (recomendado)”**. Complementa C e os perfis funcionais; medidas/modelos específicos continuam para as fichas.

**Regra aprovada:** a evolução muda o corpo no local atual, preservando a referência dos pés e a direção. Validar o volume funcional do novo perfil antes de iniciar e novamente antes de concluir. Se não houver espaço válido, recusar/interromper a mudança, conservar o estágio anterior e os marcos conquistados e informar que o jogador precisa se reposicionar. K define separadamente evolução sem taxa adicional de Energia/Stamina; recusar ou interromper não devolve gastos anteriores nem repõe recursos.

**Detalhamento de ficha ainda pendente:** a verificação considera o corpo funcional, blocos e limites do local válido, sem usar somente a aparência. Não apagar construções, atravessar paredes, reduzir a colisão temporariamente ou procurar/teleportar para outro ponto de forma automática. O corpo anterior continua sendo o corpo válido até a conclusão; não receber antecipadamente golpes/alcance/benefícios do novo estágio. Revalidar impede concluir dentro de uma obstrução surgida durante o preparo. Região disponível, suporte/meios, limites do mundo e interação com outras entidades precisam da ficha/M06/M10/M15/M16, sem carregar terreno distante para buscar uma vaga.

A evolução já concluída não cria uma bolha de espaço livre ou proteção permanente contra blocos colocados depois. Dimensão de Gillian não está sendo fixada em escala literal, e J não cria uma forma compacta para entrar numa porta. Progresso pessoal retido segue G; a apresentação e os motivos de recusa precisam ser compreensíveis em M14. L/M definem o preparo e a interrupção básicos; duração, apoio/meios concretos e ficha corporal continuam pendentes. J não resolve por si só spawn/retorno de um estágio permanente.

**Alternativa não escolhida:** procurar automaticamente um ponto próximo válido e reposicionar o personagem ao concluir. Facilita evoluir perto de obstáculos, mas faz a troca de corpo também mover o jogador e exige regras adicionais de alcance, trajetos e destino. **Justificativa da escolha:** o jogador prepara o local e conserva o controle sobre onde a mudança acontece. É regra espacial aprovada do mod, sem alegar comportamento canônico de colisão.

### 8.37. M07-04K — DECIDIDO — Evolução permanente sem taxa adicional de recurso

**Aprovado em 21/10/2026.** Resposta explícita: **“Evolução sem taxa adicional de recurso (recomendado)”**. I decidiu a devoração; K aprova separadamente a ausência de taxa/manutenção adicional na mudança evolutiva permanente.

**Regra aprovada:** concluir a passagem Hollow → Gillian → Adjuchas → Vasto Lorde sem cobrar Energia Espiritual ou Stamina adicional pela mudança de estágio; sem manutenção desses recursos apenas por continuar no estágio conquistado. A exigência é cumprir os requisitos e executar uma transição válida. Recursos atuais vazios não impedem por si só a operação, sem dispensar os requisitos de desenvolvimento, corpo, fase ou local.

**Complementos de ficha ainda em elaboração:** evoluir não preenche Vida, Energia ou Stamina nem limpa condições/tempos. Conversão de eventuais limites/capacidades precisa de ficha/M05; K não aprova multiplicador ou novo máximo. Custos já pagos na caça, prova, técnica ou manutenção compatível permanecem, sem duplicá-los como taxa de evolução ou reembolsá-los ao cancelar. Spiritual Points continuam principalmente nos atributos, sem nova cobrança universal nesta operação. Isso não estabelece custo de Arrancarização, Resurrección ou efeitos especiais.

**Alternativa não escolhida:** cobrar Energia uma vez ao concluir a mudança permanente, sem manutenção do estágio. Acrescenta preparo de recurso imediato ao marco; pode exigir esperar recarga depois de já cumprir a conquista. **Justificativa da escolha:** reconhecer a preparação já demonstrada e preservar o novo estágio como condição adquirida, sem taxa adicional para consolidá-lo. Economia é adaptação do mod, não tabela canônica. Valores dos requisitos e conteúdo continuam com M05/M07/M09/M12/M13/M15.

### 8.38. M07-04L — DECIDIDO — Preparo apoiado no chão e fora de combate

**Aprovado em 21/10/2026.** Resposta explícita: **“Preparo apoiado e fora de combate (recomendado)”**. Trata a mudança evolutiva permanente; não copia o uso em combate da devoração ou das liberações temporárias.

**Regra aprovada:** um preparo breve, parado, com apoio válido no chão e fora de Combat State. Pode olhar ao redor. A entrada no estado de combate, perda de apoio ou deslocamento incompatível antes da conclusão cancela a mudança; também pode cancelar manualmente ou para executar reação disponível. Concluir ou cancelar não acrescenta recuperação própria. J revalida o espaço do novo corpo e G conserva estágio anterior/marcos se não concluir.

**Complementos de ficha ainda em elaboração:** validar requisitos no início e na conclusão; durante o preparo permanece o corpo anterior, sem receber capacidades antecipadas. Movimento voluntário para caminhar/correr/saltar encerra o preparo. Ataque, guarda, Dodge ou mobilidade disponíveis podem cancelá-lo antes de agir, respeitando suas próprias permissões. Pedido indisponível preserva preparo ainda válido, sem renovar duração ou enfileirar ação. Entrada efetiva em Combat State é perda de requisito, independentemente de comando; fechar a Combat Hotbar não a contorna.

O mundo continua acontecendo. Não há invulnerabilidade, Super Armor, suspensão da gravidade ou eliminação de risco ambiental. M06 distingue dano isolado de controle; se o dano iniciar Combat State ou invalidar outro requisito desta evolução, a interrupção decorre dessa condição. Não alterar a regra geral de dano de todas as técnicas. Uma nova tentativa começa o preparo novamente, sem repetir o marco de conquista já registrado nem reiniciar custos/recuperações anteriores.

A duração nominal, sinais, controles, critérios de apoio e comportamento preciso dos meios pertencem à ficha/M06/M10/M14/M15. Apoio não significa ganhar voo ou plataforma na água; condições ambientais continuam. Não foi escolhida duração numérica, casulo, arena privada ou animação que retire todos os controles.

**Alternativa não escolhida:** permitir o mesmo preparo parado e cancelável durante combate, preservando espaço e exposição. Permite uma evolução no decorrer do confronto, mas exige que a mudança de perfil e suas possibilidades participem do ritmo de combate. **Justificativa da escolha:** preparar a mudança permanente fora do confronto, com espaço e apoio compreensíveis; as técnicas/transformações de combate conservam seus próprios contratos. Não reabrir H, Shikai ou Bankai.

### 8.39. M07-04M — DECIDIDO — Encerrar transições incompletas no ciclo de vida

**Aprovado em 21/10/2026.** Resposta explícita: **“Encerrar preparos; preservar conquistas (recomendado)”**. G já protege estágio e progresso concluídos; M aprova encerrar as execuções incompletas e exigir novo pedido ao retornar.

**Regra aprovada:** morte, desconexão, troca de dimensão ou reinício encerram devoração e mudança evolutiva ainda em preparo, sem completá-las ou retomá-las automaticamente. Se a evolução já concluiu validamente antes do evento, o novo estágio permanece; caso contrário, permanece o estágio anterior. Predação registrada, marcos, aprendizados, Proficiências e Masteries persistem. Retornar requer novo pedido para a ação que ainda não foi concluída, com requisitos atuais válidos.

**Complementos de ficha ainda em elaboração:** o servidor determina se a conclusão ocorreu antes do evento, sem conceder crédito duas vezes. Consumo já registrado não é desfeito; consumo incompleto não gera progresso parcial por desconexão. Interromper a tentativa não restitui gastos anteriores, não zera cooldowns/Recovery e não concede treino/regeneração offline. Aplicar M06-11V aos tempos existentes. Oportunidade de presa não reaparece apenas para restaurar um preparo antigo; seu prazo, região ativa e política de persistência precisam de definição M09/M15/M16, sem aprovar cadáver ou estoque persistente.

Persistir estágio não equivale a forçar qualquer volume corporal em qualquer posição de retorno. M10/M15/M16 precisam garantir política de spawn/retorno compatível antes da entrega jogável, sem rebaixar o estágio ou criar um corpo compacto como contorno silencioso. J regula a mudança escolhida no local atual; não define sozinho os destinos de viagem/respawn. Perda/reposição de itens, antidesconexão de PvP e estado do encontro continuam próprios.

**Alternativa não escolhida:** salvar o andamento e retomar a execução interrompida quando possível no retorno. Evita recomeçar o preparo, mas exige reconstruir sua validade perante presa, local e mundo que podem ter mudado. **Justificativa da escolha:** preservar a conquista e exigir novo comando para a execução transitória, sem avanço automático durante ausência. É adaptação do mod; não altera o retorno manual das manifestações Shinigami nem transforma um estágio Hollow em forma selável.

### 8.40. M07-05 — Arrancarização: abertura e contratos herdados

**Abertura e consolidação estrutural: 21/10/2026. Estado do bloco: `DECIDIDO` em nível estrutural.** A–J aprovados por respostas próprias, seções 8.41–8.50; nenhuma dessas dez perguntas aguarda resposta. D teve o adiamento substituído por aprovação explícita. Fichas, viabilidade e realização técnica continuam pendentes; revisão e entregas na seção 15. M07 inteiro permanece `EM ESPECIFICAÇÃO`.

| Herança | Consequência para este bloco |
|---|---|
| RAC-04 e M07-01 | Arrancarização registra uma condição adquirida na raiz Hollow; origem e histórico evolutivo não são apagados. Não é forma temporária para alternar livremente |
| M04/H7/H8/H9 | Acesso possível como Gillian, Adjuchas ou Vasto Lorde após desenvolvimento Hollow; não como atalho do recém-formado. Arrancarizar encerra a evolução normal no ponto escolhido |
| H6/REV-H6-01 e M06-08G | Identidade pode começar antes da mudança; rota de origem Gillian exige acesso compatível sem voltar a Adjuchas. Não aprova qualquer linhagem para qualquer origem |
| M06-08A–G | Três Proficiências e Masteries próprias, aprendizado apropriado e perfis corporais funcionais. A mudança não entrega repertório completo nem vira uma quarta Proficiência |
| M07-04A–M | Caçadas, evolução e marcos já conquistados mantêm seus registros; custo/preparo de evoluir não decide automaticamente custo/preparo de Arrancarizar |
| M04/H8 e M15 | As três origens precisam permanecer escolhas viáveis. Não impor uma rota superior em todos os aspectos, teto permanente inventado ou evolução retroativa para corrigir o balanceamento |

**Fronteira:** M07 define aquisição, transição e compatibilidade; M06, repertório/corpo/prática; M08/M09/M12/M13, orientação e acontecimento; M10/M11, espaço/arma/equipamento; M14, decisão informada e execução; M15, cooperação e viabilidade das rotas; M16, dados/autoridade/persistência. A passagem permanente precisa comunicar que a evolução normal termina nesse ponto, conforme H9; controle/interface concretos ainda não foram escolhidos.

### 8.41. M07-05A — DECIDIDO — Preparo no estágio e acontecimento próprio

**Aprovado em 21/10/2026.** Resposta explícita: **“Preparo no estágio + acontecimento próprio (recomendado)”**.

**Regra aprovada:** como Gillian, Adjuchas ou Vasto Lorde, demonstrar preparo pertinente no estágio escolhido e realizar um acontecimento próprio de ruptura da máscara para conquistar Arrancarização. Tornar-se Gillian ou alcançar um número não conclui a mudança automaticamente. Uma quest compatível pode incorporar esse acontecimento, sem exigir uma cópia da mesma conquista em outra quest.

**Complementos de ficha em elaboração:** distinguir preparo, acontecimento concluído e transição efetivada; detalhar quais partes do acontecimento realizam a mudança e quais apenas a habilitam. Não inventar um segundo ritual obrigatório por separar esses registros. A preparação precisa ser possível antes da Arrancarização: não exigir Sonído Arrancar, Resurrección ou Mastery de uma capacidade ainda inacessível. Indicadores de Proficiência/prática podem compor requisitos, mas não substituir sozinhos o acontecimento aprovado. Não exigir todas as técnicas ou Masteries máximas por conveniência.

A ruptura é um marco controlado pelo sistema; dano visual à máscara, quebra de guarda, textura removida ou golpe de outro jogador não equivalem automaticamente à operação. H/I/J definem agora preparo, espaço e interrupção básicos; conteúdo, duração, condições concretas e retomada do desafio ainda precisam de ficha. Não foram escolhidos boss obrigatório, QTE, quantidade de abates, sacrifício de jogadores, falha aleatória ou perda de marcos.

**Alternativa não escolhida:** desbloquear somente por requisito numérico. É simples de comunicar, mas reduz a mudança a um limiar e enfraquece sua identidade própria. **Justificativa da escolha:** fazer a passagem resultar da experiência vivida no corpo atual e de um marco reconhecível; integrar uma quest existente quando apropriado evita repetir provas. O acontecimento jogável é adaptação, não uma quest extraída da obra.

### 8.42. M07-05B — DECIDIDO — Via pessoal sem facção ou Hōgyoku obrigatórios

**Aprovado em 21/10/2026.** Resposta explícita: **“Via pessoal sem facção ou Hōgyoku obrigatórios (recomendado)”**.

**Regra aprovada:** existir uma via pessoal de Arrancarização, desenvolvida por descoberta ou orientação e prática. Não exigir servir a Aizen, pertencer a uma facção ou usar o Hōgyoku. Essa via continua sujeita a A e às condições da futura ficha; autonomia não significa arrancar a máscara por um comando imediato desde o início da raça.

**Experiência e limite:** o personagem pode escolher a passagem sem ficar bloqueado pela lealdade a um NPC ou pelo momento da campanha compartilhada. Mestres/fontes de conhecimento podem ter função concreta; B não exige um mestre específico nem aprova qualquer NPC como capaz de realizar o processo. M08/M12/M13 devem definir fontes e acesso coerentes com a era e com o personagem original, preservando a descoberta prevista na opção.

**Complementos em elaboração:** processos assistidos poderão ser pesquisados e avaliados depois; não estão aprovados como segunda rota jogável, bônus superior, item de crafting, compra de poder ou serviço obrigatório. Hōgyoku continua objeto de lore; não concedê-lo, duplicá-lo no inventário ou atribuir-lhe custos a partir desta escolha. A implementação exata da via pessoal é adaptação declarada do mod, não alegação de um ritual autônomo universal comprovado para todos os estágios.

**Alternativa não escolhida:** exigir intervenção de alguém com Hōgyoku. Dá um ponto narrativo claro, mas vincula uma passagem racial obrigatoriamente a esse agente/recurso e à sua disponibilidade na cronologia compartilhada. **Justificativa da escolha:** manter uma via pessoal coerente com autonomia e com as três origens já aprovadas; métodos excepcionais podem ter avaliação própria sem bloquear a experiência básica.

### 8.43. M07-05C — DECIDIDO — Arma própria na conclusão da Arrancarização

**Aprovado em 21/10/2026.** Resposta explícita: **“Arma própria ao concluir Arrancarização (recomendado)”**.

**Regra aprovada:** a Zanpakutō Arrancar própria surge ao concluir a mudança, sem precisar conquistar Asauchi ou tomar a espada de outro personagem. Obter essa arma não concede Resurrección, técnicas exclusivas ou Mastery. A identidade canônica pode ser desenvolvida antes ou depois, conforme uma rota compatível; a arma não depende exclusivamente de ter conquistado Adjuchas.

**Complementos de ficha em elaboração:** representar a arma como expressão do poder próprio e separar sua existência do desbloqueio da primeira liberação. Caso a identidade principal ainda não esteja escolhida, a ficha precisa oferecer um estado-base utilizável e uma transição coerente ao poder canônico posterior; isso não aprova um segundo poder principal, uma Resurrección original provisória ou seleção livre de qualquer linhagem. A aparência, o tipo de arma e sua ligação com o perfil concreto continuam para as fichas, sem fixar katana universal ou modelo artístico agora.

M06 conserva o moveset orientado pela empunhadura e as técnicas compatíveis já aprendidas; obter uma arma não significa dominar Zanjutsu/Kidō ou receber repertório Shinigami. M11/M16 devem definir a criação vinculada sem duplicação, posse/slots, drop, perda, reposição e retorno; não importar automaticamente as políticas de Shikai/Bankai, de invocação do arco ou uma arma indestrutível. A operação precisa ser viável com inventário cheio sem destruir itens; a solução concreta ainda exige especificação.

**Alternativa não escolhida:** tornar-se Arrancar primeiro e conquistar a própria arma em etapa posterior. Cria outro marco, mas separa a mudança da expressão básica de seu poder e exige justificar a fase intermediária. **Justificativa da escolha:** entregar a arma própria na passagem e reservar o desenvolvimento posterior para o domínio e a liberação, preservando conquista separada de Resurrección. A existência da arma tem apoio nas fontes; seu momento exato de entrega e operação em inventário são adaptação do mod.

### 8.44. M07-05D — DECIDIDO — Corpo-base humanoide nas três origens

**Aprovado em 21/10/2026, após correção explícita.** O pedido inicial de adiar a escolha — “Vamos segurar essa decisão por enquanto, vou questionar meus amigos depois” — foi substituído pelo usuário: **“Eu entendi errado a primeira, concordo com a recomendação”**. Fica aprovada a opção **base humanoide nas três origens**. O modelo de perfis controlados de M06 permanece; D decide sua aplicação ao estado-base anterior à Resurrección. Não há adiamento pendente.

**Regra aprovada:** oferecer ao personagem um corpo-base humanoide jogável como Arrancar de origem Gillian, Adjuchas ou Vasto Lorde. Não obrigar a origem Gillian a conservar o volume gigante anterior. Cada corpo continua sendo um perfil do mod, com fragmentos/traços e diferenças coerentes; a escolha não fixa modelos, medidas ou um único corpo funcional idêntico para todos.

**Complementos de ficha ainda em elaboração:** ao concluir a Arrancarização, o perfil Arrancar passa a governar colisão, alcance e execução, mantendo referência espacial e validações que a futura regra de transição declarar. Não usar simultaneamente alcance de Gillian e volume humanoide nem transportar golpes de uma anatomia que já não está presente. D não concede mudança de corpo livre, voo, imunidade ambiental, atributos por tamanho, equipamento automático ou todas as interações vanilla sem contrato. Corpo humanoide também não resolve por si só inventário, armadura ou uso simultâneo de itens.

Definir perfil com/sem identidade principal já escolhida, câmera, mãos, golpes, arma e relação com terreno em M06/M07/M10/M11/M14/M15. Cosméticos mantêm a regra existente: não alteram propriedades funcionais. A origem evolutiva permanece registrada; humanoide não significa tornar-se Vasto Lorde, apagar sua história ou igualar numericamente as três rotas. Possíveis corpos de Resurrección pertencem ao bloco próprio, sem devolver o corpo anterior por associação. NPCs e exceções canônicas não são obrigados a adotar o perfil do jogador.

**Alternativa não escolhida:** corpos-base mais distintos conforme a origem, inclusive não humanoides ou com anatomia Hollow dominante. Reforça visualmente a procedência e permite experiências próprias, mas exige mais perfis, animações, interações e revisão de navegação/armas para manter todas as rotas viáveis. **Justificativa da escolha:** permitir combate armado e convivência com o mundo construído em qualquer origem, preservando identidade e diferenças pelo perfil e pelo desenvolvimento. É adaptação do mod, não afirmação de anatomia universal da obra. Escala exata e arte continuam adiadas.

### 8.45. M07-05E — DECIDIDO — Preservar desenvolvimento e executar capacidades compatíveis

**Aprovado em 21/10/2026.** Resposta explícita: **“Preservar domínio e usar o que for compatível (recomendado)”**. Complementa o tratamento da execução após mudar corpo/estado que M06-08B deixou para M07, sem reabrir a arquitetura de desenvolvimento.

**Regra aprovada:** conservar as Proficiências dos três campos e as Masteries já desenvolvidas. Técnicas aprendidas continuam utilizáveis sem um novo desbloqueio obrigatório quando o corpo/estado Arrancar e os demais requisitos forem compatíveis. Capacidades que dependem de anatomia/condição ausente ficam indisponíveis, sem apagar seu histórico; técnicas novas exigem aprendizado próprio.

| Aplicação do núcleo aprovado | Tratamento a especificar na ficha |
|---|---|
| Cero já aprendido e compatível | Conservar aprendizado/Mastery; adaptar apresentação e ponto de emissão ao perfil sem criar outra Mastery apenas por Arrancarizar |
| Golpe que exige membro ausente | Não executar no corpo atual nem converter automaticamente em outra técnica; histórico/domínio continuam registrados |
| Novo moveset básico corporal/armado | Seguir o perfil e a empunhadura de M06; Proficiência física não se torna domínio instantâneo de toda técnica armada |
| Sonído, Bala ou capacidade ainda não aprendida | O novo estado pode permitir o aprendizado, mas não substitui sua conquista nem entrega Mastery |
| Regeneração rápida ou capacidade particular | Verificar compatibilidade própria de corpo/poder; conhecimento registrado não garante manutenção da função |

**Complementos de ficha ainda em elaboração:** não reiniciar os três campos nem criar uma segunda trinca Arrancar; não duplicar prática, converter evolução em Proficiência ou repartir Mastery em cópias corporais por omissão. A técnica mantida conserva custos, fases e recuperação; trocar o corpo não restaura recursos nem limpa tempos, e a operação concreta da passagem ainda exige decisão própria. Não conservar uma carga/ação em curso apenas por preservar aprendizado.

Se uma capacidade realmente distinta for aprendida, terá o desenvolvimento pertinente de M06, sem herdar domínio máximo por similaridade. A ficha deve informar compatibilidade e motivo de bloqueio; isso não autoriza introduzir retrospectivamente uma perda racial ou atribuir regeneração a todos. Memória de técnica incompatível não é promessa de retorno ao corpo anterior, Resurrección automática ou execução mesmo sem os requisitos.

**Alternativa não escolhida:** preservar o histórico de domínio, mas exigir readaprender a execução de cada técnica geral no corpo Arrancar. Cria uma fase explícita de adaptação e demanda conteúdo/requisitos por técnica; também pode repetir aprendizagens que continuam funcionalmente equivalentes. **Justificativa da escolha:** reconhecer a prática anterior e concentrar o novo aprendizado nas capacidades novas ou realmente diferentes. Indicadores, preservação e currículo são adaptação do mod, não regras numéricas do cânone.

### 8.46. M07-05F — DECIDIDO — Origens como trajetórias de desenvolvimento

**Aprovado em 21/10/2026.** Resposta explícita: **“Trajetórias e preparo, sem bônus fixo por origem (recomendado)”**.

**Regra aprovada:** distinguir o momento de entrada no repertório Arrancar e o preparo efetivamente conquistado como Hollow. Não reiniciar ou nivelar artificialmente o personagem na passagem, nem criar bônus fixo, penalidade permanente ou teto menor de atributos apenas por seu rótulo de origem. As capacidades e alterações do estágio/perfil precisam de ficha própria; somente as compatíveis continuam funcionando, conforme E. A aprovação não preenche essas fichas nem atribui uma vantagem ainda inexistente.

| Origem escolhida | Vantagem da trajetória aprovada | Custo e consequência |
|---|---|---|
| Gillian | Acesso ao aprendizado Arrancar sem exigir primeiro Adjuchas/Vasto Lorde; pode investir antes em arma, Bala, Sonído e, após conquista própria, Resurrección | Renuncia à evolução Hollow normal posterior; precisa desenvolver efetivamente o novo repertório. Nenhuma dessas técnicas é entregue por Arrancarizar |
| Adjuchas | Chega após a experiência, prova e capacidades compatíveis conquistadas nesse estágio, sem exigir a jornada até Vasto Lorde | Investe mais no percurso Hollow antes da passagem e renuncia à ascensão normal a Vasto Lorde |
| Vasto Lorde | Completa a conquista Hollow avançada, com seu reconhecimento e o desenvolvimento/capacidades compatíveis que efetivamente obteve | Exige os marcos e prova de Vasto Lorde antes de começar sua etapa Arrancar; não recebe Mastery Arrancar ou vitória automática por concluir essa jornada |

“Antes” descreve os requisitos da trajetória, não prazo fixo ou garantia de que todo jogador Gillian completará o conteúdo antes de qualquer Vasto Lorde. Um Gillian pode ter treinado muito e um jogador pode cumprir conteúdos em ritmos diferentes. Não atribuir automaticamente mais atributos ou Mastery a alguém somente pela classe histórica.

**Crescimento posterior aprovado:** as três origens continuam desenvolvendo atributos, Proficiências e técnicas compatíveis segundo M05/M06. A origem Gillian não sofre uma redução própria do teto universal de atributos nem uma taxa extra de treino; isso não a transforma retroativamente em Adjuchas/Vasto Lorde. Não prometer desempenho final idêntico, todo poder principal para toda origem, mesmas passivas ou igual quantidade de conteúdo. H6 continua exigindo acesso viável a poderes compatíveis, e H9 continua encerrando a evolução normal.

**Complementos de ficha e viabilidade ainda em elaboração:** M06/M07/M15 devem comprovar uma jornada Arrancar completa e útil para cada origem, inclusive nos encontros avançados previstos, sem exigir refazer o personagem como Vasto Lorde. Comparar investimento e preparo anteriores, aprendizado posterior, função em combate e cooperação; somente chegar antes não basta para justificar uma origem que depois se torne inviável. A tabela define o eixo de comparação, não declara equilíbrio comprovado. Se fichas ou evidência futura mostrarem uma rota dominada, revisar o contrato afetado antes da entrega, com decisão explícita.

**Alternativa não escolhida:** adicionar vantagens exclusivas permanentes por origem, por exemplo regras distintas de eficiência ou especialização. Pode criar escolhas de build mais marcadas, mas exige justificar cada vantagem na identidade da rota, evitar simetria artificial e demonstrar que nenhuma origem domina. Nenhuma vantagem concreta dessa alternativa foi escolhida. Tetos raciais menores também exigiriam revisão explícita de M05; não são consequência automática de H8.

**Justificativa da escolha:** aproveitar o progresso que o jogador realmente viveu e manter valor no momento da passagem, sem inventar absorção universal para Gillian, um bônus obrigatório para Adjuchas ou um multiplicador geral para Vasto Lorde. A maior capacidade de Hollows anteriores descrita nas fontes é referência narrativa; não comprova a regra de crescimento aberto aprovada como adaptação do mod.

### 8.47. M07-05G — DECIDIDO — Passagem permanente sem taxa adicional de recurso

**Aprovado em 21/10/2026.** Resposta explícita: **“Sem taxa adicional pela Arrancarização (recomendado)”**.

**Regra aprovada:** não cobrar Energia Espiritual ou Stamina adicional para concluir a Arrancarização, nem consumo contínuo apenas por permanecer Arrancar. Os custos da jornada ficam no preparo, no acontecimento e nas ações que compõem os desafios; técnicas e efeitos posteriores conservam seus próprios custos. Uma prova pode conter combate com gastos normais, mas G não aprova consumível, material, taxa de pontos ou sacrifício novo.

Não recuperar Vida, Energia ou Stamina ao mudar; não limpar condições ou tempos anteriores.

**Complementos de ficha em elaboração:** se uma ficha alterar máximos, a conversão deve ser definida em M05/M07/M15 sem refill disfarçado; não está escolhida nesta rodada. A arma de C não cobra uma segunda taxa de manifestação só por surgir na conclusão. H/I/J definem preparo/espaço/interrupção básicos; fases, meios e demais complementos concretos continuam para a ficha.

**Alternativa não escolhida:** cobrar Energia uma vez ao concluir. Acrescenta um requisito de recurso no momento da passagem, mas exige decidir valor, reserva mínima e recusa, além da prova já cumprida. Não faria a condição racial depender de manutenção. **Justificativa da escolha:** tratar o custo principal como conquista da condição permanente; reservar o gerenciamento de Energia para as execuções que o jogador usa depois. Não herdar automaticamente a aprovação de custo da evolução Hollow ou das liberações Shinigami.

### 8.48. M07-05H — DECIDIDO — Escolha deliberada e preparo fora de combate

**Aprovado em 21/10/2026.** Resposta explícita: **“Preparo deliberado e fora de combate (recomendado)”**.

**Regra aprovada:** o jogador escolhe iniciar a passagem, cumpridos os requisitos, com preparo breve, parado, apoiado no chão e fora de Combat State. Mover-se, entrar em combate ou perder apoio cancela; uma reação disponível cancela o preparo antes de começar, sem recuperação extra só pelo cancelamento. A mudança pode integrar o acontecimento da máscara de A, sem um segundo ritual obrigatório.

Se a conquista incluir combate, essa parte conserva suas próprias regras; a mudança final aguarda as condições válidas. Antes de iniciar, comunicar a consequência já decidida em H9: encerrar a evolução Hollow normal no estágio escolhido. Não repetir um marco concluído apenas por separar os registros de conquista e mudança corporal.

**Complementos de execução para a ficha:** olhar ao redor pode acompanhar o preparo; um pedido de reação indisponível não deve cancelar um preparo ainda válido, renovar seu prazo ou ficar enfileirado. Conferir as fases e controles concretos com M06/M14. Até a conclusão, o corpo anterior governa a execução. Combat Mode aberto/fechado não substitui Combat State; sair da interface não torna o local seguro nem apaga tempos. O mundo permanece ativo; H não concede invulnerabilidade ou proteção contra controle.

Duração, apoios/meios concretos e compatibilidade com ações/efeitos sustentados continuam pendentes. O preparo de evolução Hollow foi comparado, mas H tem aprovação própria. G conserva a ausência de taxa adicional de Energia/Stamina, refill ou reset. J trata a interrupção no ciclo de vida.

**Alternativa não escolhida:** permitir esse preparo também em combate, preservando a escolha deliberada e as regras de interrupção. Facilitaria a passagem durante encontros, mas exigiria integrar a troca de corpo e repertório ao combate em andamento. **Justificativa da escolha:** preparar o local e compreender a consequência de uma mudança permanente. É adaptação do mod; não atribuir à obra exigência universal de chão ou ausência de combate.

### 8.49. M07-05I — DECIDIDO — Espaço válido na posição atual

**Aprovado em 21/10/2026.** Resposta explícita: **“Exigir espaço na posição atual (recomendado)”**.

**Regra aprovada:** manter a referência dos pés e a direção, verificando o espaço funcional do novo corpo antes de iniciar e ao concluir. Se não couber, recusar ou interromper com aviso, mantendo o corpo anterior e as conquistas. O jogador se reposiciona; nenhum bloco é removido nem há deslocamento automático para concluir.

**Complementos de ficha:** D garante uma base humanoide nas três origens, sem fixar medidas. Mesmo uma mudança para corpo menor exige validar colisão e apoio. O perfil anterior governa até a conclusão e o novo depois dela, sem combinar alcance antigo e colisão menor. A validação final considera obstruções surgidas durante o preparo. Medidas, desníveis, apoios/meios, entidades e limites do mundo exigem M06/M07/M10/M15/M16.

I trata a passagem escolhida; não decide respawn ou chegada entre dimensões. Criação da arma, inventário cheio, equipamento e posse continuam com M11/M16. A entrega precisa ser viável sem destruir itens ou deixar o personagem parcialmente convertido; a solução concreta não foi escolhida por I.

**Alternativa não escolhida:** buscar um ponto próximo válido e reposicionar automaticamente. Reduz recusas, mas acrescenta regras de distância, trajeto, perigos e permissões. **Justificativa da escolha:** conservar o controle do local e respeitar o terreno construído. É regra espacial do mod, não afirmação canônica de colisão.

### 8.50. M07-05J — DECIDIDO — Encerrar preparo e preservar conquistas

**Aprovado em 21/10/2026.** Resposta explícita: **“Encerrar preparo; preservar conquistas (recomendado)”**.

**Regra aprovada:** interromper a passagem incompleta conserva o estágio anterior e os marcos concluídos. Morte, desconexão, troca de dimensão ou reinício encerram o preparo incompleto, sem retomada automática; é necessário novo pedido em condições válidas. Se a Arrancarização já terminou, a condição permanece. Gastos anteriores não são devolvidos e tempos não são apagados.

A parte do desafio ainda em andamento pode precisar ser repetida; somente marcos realmente concluídos permanecem registrados. Origem histórica, desenvolvimento e capacidades compatíveis seguem E/F. Limpar execução transitória não devolve o corpo Hollow anterior nem reabre a evolução normal encerrada em H9. M06-11U continua encerrando cargas e estados deliberados próprios; preservar a condição Arrancar não restaura Supressão/Liberação ou ações interrompidas. M06-11V conserva os tempos já iniciados.

**Complementos de ficha/M16:** definir a ordem autorizada entre conclusão e interrupção, salvamento e sincronização; aplicar uma conclusão válida uma vez. Retorno não reaplica a conversão, recompensa ou arma própria de C. Arma/inventário precisam de contrato sem duplicação, ainda sem escolher drop, slot ou reposição. J não decide keepInventory, recursos de respawn, punição de PvP ou restauração do encontro. Sobreviver à interrupção como estágio anterior não é devolver gastos legítimos.

**Alternativa não escolhida:** guardar o andamento e retomar automaticamente o preparo quando possível. Poupa repetir a interação breve, mas pode executar uma escolha permanente em outro contexto e exige reconstruir sua validade. **Justificativa da escolha:** conservar conquistas sem executar sozinho o que ainda não terminou. É adaptação de continuidade do mod, sem alegar comportamento canônico de sessão.

### 8.51. M07-06 — Resurrección: abertura e contratos herdados

**Abertura e consolidação estrutural: 21/10/2026.** A–Q aprovados por respostas próprias, seções 8.52–8.68; dezessete decisões sem perguntas pendentes. Revisão dirigida e entregas na seção 15. Fichas executáveis e evidência de gameplay continuam pendentes; M07 inteiro segue `EM ESPECIFICAÇÃO`. Arrancarização M07-05A–J mantém seu fechamento estrutural.

| Herança | Consequência para Resurrección |
|---|---|
| RAC-20/RAC-23 e H6/REV-H6-01 | Um poder canônico principal, conquistado na questline pertinente; identidade pode começar antes de Arrancarizar. Executar a liberação exige condição compatível; não conceder poder ao recém-formado nem liberar qualquer linhagem para qualquer origem |
| M07-05C/D/E/F | Arma própria não entrega Resurrección; base humanoide não fixa corpo liberado; conservar desenvolvimento compatível sem conceder capacidades novas ou penalizar a origem por seu rótulo |
| M04/H8/H9 e M07-05J | Gillian/Adjuchas/Vasto Lorde têm rotas Arrancar compatíveis; não exigir voltar à evolução normal já encerrada para aprender a liberação |
| M05 e M06 | Atributos, Proficiência e Mastery permanecem distintos; sem compra de Mastery, quarta Proficiência racial ou multiplicador universal de forma |
| M07-01 e M06/fases/tempos | Condição Arrancar, liberação conquistada e manifestação atual são registros distintos. Custos, fases e retorno precisam de decisão própria; mudar forma não apaga tempos anteriores |

**Fronteira atual:** aquisição/domínio aprovados em A/B; ativação manual, arma inicial, independência da interface e modelo de recursos aprovados em C/D. Preparo cancelável, retorno básico e ausência de recuperação adicional aprovados em E/F/G. H/I/J aprovam representação da arma conforme o poder/itens compatíveis, validação espacial na posição atual e passagem que conserve espaço do corpo-base. K/L/M aprovam execução no chão/ar/água com queda/riscos preservados, conservação ao guardar a arma separada e encerramento ao perder sua posse. N/O/P aprovam armadura funcional, convivência com estados de Reiatsu e prioridade das manutenções. Complementos de ficha, medidas/poses, classificação concreta de ações/efeitos e item incorporado/inventário continuam em elaboração; Q aprova o ciclo de vida na seção 8.68. Nenhuma política de Shikai/Bankai ou da passagem permanente é transferida automaticamente. Mentoria existente permanece possível, sem escolher um mestre obrigatório ou converter todo personagem canônico em professor amigável.

**Herança consolidada, sem nova votação:** M06-08C/G já aprova perfis corporais funcionais por etapa e identidade canônica. Aplicar à Resurrección o perfil daquele poder, com golpes, alcance e colisão definidos na ficha, sem montar membros livremente ou recuperar obrigatoriamente o antigo corpo Hollow. M06-08F orienta o ataque fundamental pelos meios realmente disponíveis; uma arma incorporada não equivale a empunhar a espada selada. Esta consolidação não concede automaticamente técnicas, voo, regeneração ou valores. Aparência, medidas e repertório concreto continuam para conteúdo/fichas.

### 8.52. M07-06A — DECIDIDO — Preparo do próprio poder e demonstração de liberação

**Aprovado em 21/10/2026.** Resposta explícita: **“Preparo do próprio poder + demonstração (recomendado)”**.

**Regra aprovada:** exigir condição Arrancar, arma própria e identidade canônica já vinculada, preparo para liberar esse poder e uma demonstração prática de liberar o poder Hollow selado na arma. Conquista e demonstração ficam registradas; não se repetem a cada uso. A demonstração pode integrar a questline do poder, sem outra prova equivalente obrigatória depois.

A identidade pode ter começado antes da Arrancarização, conforme H6; a primeira execução de Resurrección precisa do estado compatível. Quem Arrancarizou sem identidade ainda escolhida mantém o estado-base utilizável previsto em M07-05C, mas não recebe uma Resurrección original provisória. A rota Gillian exige acesso ao aprendizado compatível sem se tornar Adjuchas/Vasto Lorde retroativamente.

**Complementos candidatos para a ficha:** o preparo deve ser verificável por prática/conteúdo possível antes da liberação, sem exigir Mastery de Resurrección, técnica exclusiva da forma ou todos os campos maximizados. A demonstração precisa permitir a primeira execução no contexto de aprendizado; não exigir que o personagem já saiba liberar para autorizá-lo a aprender. O preparo pode usar fundamentos e controle já disponíveis, sem exigir antecipadamente efeitos exclusivos da forma. Proficiência ou abates isolados não substituem a comprovação aprovada.

Orientação pode ajudar, preservando a questline associada ao personagem do poder e sua personalidade. Não impor conversar ou subjugar um espírito independente, materialização semelhante a Bankai, título de Espada, facção ou Hōgyoku como requisitos universais deste aprendizado. A relação, prova e exercícios concretos pertencem a M08/M12/M13; A não escolhe boss, minijogo, sacrifício, prazo ou uma nova taxa de pontos.

**Alternativa não escolhida:** conceder a liberação automaticamente quando o personagem Arrancar adquirir a identidade canônica compatível, sem demonstração própria. Reduz uma etapa e o conteúdo de aprendizado, mas torna a conquista da identidade suficiente para liberar sem demonstrar a execução. **Justificativa da escolha:** dar função ao aprendizado do próprio poder e aproveitar a quest existente, sem duplicar seus marcos.

### 8.53. M07-06B — DECIDIDO — Manifestação confiável e Mastery própria

**Aprovado em 21/10/2026.** Resposta explícita: **“Manifestação confiável + Mastery própria (recomendado)”**.

**Regra aprovada:** depois de conquistada, permitir a manifestação básica de Resurrección quando os requisitos forem cumpridos, sem chance aleatória de falhar a cada ativação. A Resurrección desse poder tem Mastery própria desenvolvida por prática apropriada; técnicas distintas e versões posteriores preservam aprendizado e domínio próprios.

Conquistar a forma permite começar a usá-la; não equivale a dominar todas as suas capacidades. O repertório inicial e o que muda no corpo precisam de ficha antes da entrega jogável, sem conceder toda técnica exclusiva apenas por transformar. Mastery não substitui a aquisição da forma e não é comprável.

**Complementos candidatos para a ficha:** diferenciar prática da forma, Proficiências físicas/emissão/mobilidade e prática de suas técnicas, sem duplicar arbitrariamente a mesma evidência. Mastery de Cero ou Sonído já desenvolvida continua própria; não se converte em domínio automático de Resurrección nem reinicia só pela forma. Tempo AFK ou alternância repetida não constituem treino válido.

Confiabilidade não concede execução instantânea, imunidade à interrupção, Energia gratuita, corpo anterior, regeneração especial ou manutenção ilimitada. C/D e E/F/G complementam custos, preparo e transições; condições específicas continuam na ficha. Não criar, por esta aprovação, falha aleatória, barra de instinto ou perda de controle universal; exceções de um poder exigem fonte e ficha próprias. B não concede Segunda Etapa nem todas as versões de um poder ao atingir um número isolado.

**Alternativa não escolhida:** após a conquista inicial, cada ativação ainda pode falhar aleatoriamente até desenvolver domínio suficiente. Comunica instabilidade por tentativas, mas pode recusar uma decisão válida do jogador e incentivar repetição de ativações. **Justificativa da escolha:** fazer o crescimento aparecer na execução e no uso do repertório, preservando a confiança no comando já aprendido.

### 8.54. M07-06C — DECIDIDO — Ativação manual com arma própria e independente da interface

**Aprovado em 21/10/2026.** Resposta explícita: **“Ativação manual com arma própria (recomendado)”**.

**Regra aprovada:** depois da conquista, ativar Resurrección por pedido manual, com a própria Zanpakutō compatível empunhada para realizar a liberação. Pode ser pedida em combate ou durante exploração, respeitando fases e requisitos; não depende de entrar em Combat Mode. Fechar a Combat Hotbar não encerra a manifestação por si só.

Validar conquista, condição Arrancar, identidade e arma vinculadas, corpo/local, fase e recursos da ficha. Portar arma alheia ou conhecer o nome do poder não substitui essas condições. C não fixa tecla, fala digitada, comando de voz, animação, duração do preparo ou cancelamentos. A operação continua autorizada pelo servidor; a interface apenas a apresenta.

**Complementos de ficha:** o requisito de arma para liberar não decide o que acontece depois de concluída a transformação. Cada ficha precisa representar corretamente a arma incorporada ao corpo, transformada ou ainda distinta, e resolver mãos, inventário, ferramenta, drop/perda e retorno. Não presumir espada duplicada, slot permanentemente ocupado ou necessidade de continuar empunhando uma arma que deixou de existir como peça separada. Manifestações especiais da arma exigem contrato específico; não padronizar toda Zanpakutō Arrancar como katana nem substituir um caso canônico por um item genérico sem decisão.

Estar em combate não torna qualquer fase cancelável. Pedir a liberação durante ataque/recuperação precisa obedecer às permissões de M06 e à futura ficha; C não concede troca que apague custos ou tempos. Também não importa o preparo fora de combate/parado/apoiado da Arrancarização permanente. A condição Arrancar continua adquirida quando a manifestação termina.

**Alternativa não escolhida:** ativar automaticamente ao iniciar um ataque compatível, com os mesmos requisitos de arma e conquista. Economiza um comando, mas pode iniciar transformação e gasto quando o jogador pretendia apenas usar o estado-base; exige outras regras de intenção e prevenção de repetição. **Justificativa da escolha:** o jogador escolhe quando exteriorizar o poder, inclusive fora da interface de combate.

### 8.55. M07-06D — DECIDIDO — Energia na conclusão e manutenção enquanto ativa

**Aprovado em 21/10/2026.** Resposta explícita: **“Ativação + manutenção de Energia (recomendado)”**.

**Regra aprovada:** cobrar Energia Espiritual uma vez ao concluir a liberação e consumir Energia enquanto Resurrección estiver ativa, sem duração fixa universal. Cancelar antes de concluir não cobra a ativação. Técnicas e efeitos com custos próprios conservam suas cobranças; não criar cobrança adicional automática para toda passiva. Sem recurso para a manutenção devida, a manifestação termina; F define retorno básico ao estado-base Arrancar. Transição corporal concreta, efeitos dependentes e espaço ainda precisam de ficha.

**Complementos de pagamento para a ficha:** conferir viabilidade inicial e revalidar na conclusão; cobrar a ativação uma vez, junto da manifestação válida. A nova manutenção começa com a forma ativa, sem cobrá-la durante um preparo que ainda não concluiu. Recurso insuficiente na conclusão impede a manifestação e sua cobrança, sem completar uma forma não financiada. Reserva/prepagamento de vários períodos não foi aprovado. Efeitos sustentados anteriores conservam seus contratos; P complementa a prioridade Resurrección → Liberação → ação nas cobranças coincidentes. Outros efeitos exigem contrato próprio.

Ativar, encerrar ou reativar não restaura Vida/Energia/Stamina nem apaga recuperações. Uma nova liberação concluída paga novamente; gastos anteriores não retornam. D não fixa taxa adicional de Stamina, Spiritual Points ou consumível; nenhum desses custos foi aprovado nesta rodada. A forma não ganha cura universal ou reposição por representar liberação de capacidades Hollow.

**Ainda para ficha/calibração:** valor da ativação, taxa/intervalo de manutenção, contribuição de Controle/Mastery, saldo, ritmo de combate e compatibilidade com outros estados. A manutenção precisa permitir uso real do repertório, sem tornar a forma inviável ao somar técnicas e outras cobranças; não escolher valores, multiplicadores ou prioridade a partir dessa intenção. Regeneração e demais capacidades só existem quando a ficha própria as autorizar. D não concede manutenção infinita nem um prazo obrigatório para toda forma do catálogo.

**Alternativa não escolhida:** cobrar Energia ao concluir e conceder uma duração fixa por uso, sem manutenção básica durante esse prazo. Facilita prever a janela, mas mantém a forma por um cronômetro e muda como ela disputa recursos com as técnicas. **Justificativa da escolha:** permitir que duração e decisão de manter dependam da Energia disponível e do uso efetivo do poder, com valores a calibrar.

### 8.56. M07-06E — DECIDIDO — Preparo breve cancelável para reagir

**Aprovado em 21/10/2026.** Resposta explícita: **“Preparo cancelável para reagir (recomendado)”**.

**Regra aprovada:** preparo breve permitindo caminhar devagar e olhar ao redor. Ataque, bloqueio, Dodge ou Sonído disponíveis cancelam o preparo antes de começar, sem recuperação adicional apenas pelo cancelamento. Pedido indisponível dá aviso e conserva o preparo ainda válido, sem renovar seu prazo ou enfileirar a reação. Cancelar antes da conclusão não cobra ativação, conforme D; custos e tempos anteriores permanecem.

**Efeito para o jogador:** há um momento reconhecível de liberação, mas ele pode desistir para se defender. A disponibilidade da reação inclui a fase e os tempos próprios: esta aprovação não permite transformar para apagar a recuperação de Cero ou começar outro ataque antecipadamente.

**Contratos herdados e complementos de ficha:** arma e demais requisitos precisam continuar válidos até a conclusão. Perda efetiva de requisito ou controle que invalide o preparo interrompe conforme M06; dano isolado não é interrupção universal e o preparo não concede invulnerabilidade. K complementa execução no chão/ar/água, sem apoio obrigatório e com queda/riscos preservados. Velocidade, duração, poses e volumes concretos ainda exigem ficha.

**Alternativa não escolhida:** preparo comprometido, sem cancelamento voluntário para reagir depois de iniciado. Cria uma janela de risco maior, mas retira uma reação que o jogador ainda poderia executar no estado-base. **Justificativa da escolha:** manter uma escolha tática sem tornar a liberação uma pausa segura.

### 8.57. M07-06F — DECIDIDO — Encerramento manual e retorno ao estado-base Arrancar

**Aprovado em 21/10/2026.** Resposta explícita: **“Encerrar manualmente e voltar ao estado-base (recomendado)”**.

**Regra aprovada:** permitir encerrar deliberadamente quando a fase atual autorizar; falta de Energia para a manutenção também encerra a manifestação, conforme D. O destino básico é o próprio estado-base Arrancar, sem liberação ativa. Condição, identidade, conquista e Mastery permanecem. Encerrar não devolve recursos; nova liberação concluída paga novamente.

**Limites e integrações:** isso não retorna ao corpo Gillian/Adjuchas/Vasto Lorde anterior à Arrancarização nem concede uma forma intermediária. O pedido voluntário não cancela um ataque comprometido. Encerramento forçado não conserva benefícios sem manutenção; efeitos e ações que realmente dependam da forma precisam declarar sua consequência, respeitando M06-11W para disparos já autônomos. Não apagar todo efeito emitido apenas pela mudança.

**Complementado por H/I/J:** arma conforme poder e itens compatíveis, validação na posição atual e passagem incluindo o corpo-base. **Fronteiras ainda abertas:** representação concreta de corpo/arma/equipamento na volta; intervenções do mundo que invalidem o volume e poses concretas; ações dependentes já em andamento. Q define o ciclo de vida básico; K define os meios básicos; L/M distinguem guardar e perder a arma separada. Antes da entrega jogável, M06/M07/M10/M11/M16 precisam resolver a transição segura, sem manter vantagens não pagas, atravessar paredes ou destruir blocos por omissão. F não aprova teleporte, encolhimento, duplicação de arma, retorno automático de estados espirituais ou restauração após logout.

**Alternativa não escolhida:** impedir o encerramento voluntário durante Combat State, mantendo a saída por falta de manutenção. Dá mais compromisso ao uso, mas obriga continuar consumindo Energia mesmo quando o jogador quer voltar ao repertório-base. **Justificativa da escolha:** permitir administrar o próprio recurso, respeitando o compromisso de cada ação.

### 8.58. M07-06G — DECIDIDO — Transições sem recuperação adicional

**Aprovado em 21/10/2026.** Resposta explícita: **“Transições sem recuperação adicional (recomendado)”**.

**Regra aprovada:** ao concluir a liberação, permitir ações disponíveis sem uma recuperação adicional apenas pela transformação. Encerrar voluntariamente também não acrescenta taxa ou recuperação própria, quando a fase e a transição corporal permitirem. O preparo de E continua existindo; todas as recuperações e cooldowns anteriores permanecem. D continua encerrando a forma sem manutenção financiada.

**Alternativa não escolhida:** acrescentar uma recuperação própria após liberar ou encerrar voluntariamente. Cria outra janela de exposição, mas soma espera ao preparo e exige calibrar uma penalidade universal para formas muito diferentes. **Justificativa da escolha:** o ritmo e as restrições específicas continuam na ficha de cada capacidade.

**Complementos ainda pendentes:** G não torna instantânea uma mudança corporal ainda inviável, não cancela ataques e não torna executável uma técnica que perdeu requisito. Consequências de perda forçada da forma e exceções de um poder permanecem pendentes de ficha/decisão própria; não foram aprovadas como recuperação gratuita por esta aprovação.

### 8.59. M07-06H — DECIDIDO — Arma conforme o poder e uso de itens compatíveis

**Aprovado em 21/10/2026.** Resposta explícita: **“Arma conforme o poder; itens compatíveis permitidos (recomendado)”**.

**Regra aprovada:** depois de liberar, representar a arma conforme a ficha do poder: incorporada ao corpo, transformada ou separada. Enquanto incorporada, a versão selada não fica disponível como uma segunda arma utilizável ou transferível. Conservar o mesmo vínculo e histórico do item; encerrar não fabrica outra arma nem repara dano persistente por omissão.

**Para o jogador:** ferramentas, blocos e outros itens podem ser usados quando o corpo, as mãos e a fase permitirem, sem encerrar Resurrección só por selecionar ou usar um item compatível. Manutenção continua; capacidades que dependem de armamento/mão indisponíveis não podem executar. Uma mão incorporada numa arma não fica livre só porque o jogador selecionou uma picareta. Não acrescentar slots vanilla ou ataques simultâneos pelo número de membros do modelo.

**Alternativa não escolhida:** reservar as mãos à forma e exigir encerrá-la para usar ferramentas/blocos. Simplifica animações e compatibilidades, mas interrompe exploração/construção e exige nova liberação paga ao retomar. **Justificativa da escolha:** integrar a forma às atividades de Minecraft, respeitando a anatomia e os compromissos de combate.

**Complementos ainda abertos:** representação do item incorporado, slot/reserva, inventário cheio, retorno sem duplicação, representação concreta de arma guardada/perdida conforme L/M, morte/drop/reposição e aplicação concreta de armadura/encantamentos conforme N. M11/M16 precisam de ficha antes da entrega jogável. H não torna a arma indestrutível, imune a perda ou automaticamente recuperável; essas políticas não são aprovadas por permitir ferramentas.

### 8.60. M07-06I — DECIDIDO — Validar espaço na posição atual ao liberar

**Aprovado em 21/10/2026.** Resposta explícita: **“Exigir espaço na posição atual (recomendado)”**.

**Regra aprovada:** preservar a referência dos pés e a direção, conferindo o espaço exigido pelo novo corpo antes do preparo e ao concluir. Se não couber, recusar/encerrar a tentativa com aviso, mantendo o corpo anterior, conquista e Mastery. Não cobrar a ativação não concluída, conforme D. O jogador escolhe outro local; a transformação não remove blocos nem o reposiciona automaticamente.

O personagem pode caminhar devagar durante E; a verificação final usa a posição atual, sem puxá-lo de volta ao ponto inicial. Fases, custos anteriores e cooldowns permanecem. O servidor valida o volume funcional, não somente a malha visível. K complementa os meios de execução, preservando gravidade, trajetória e queda; poses e volumes concretos ainda exigem ficha. Preservar referência dos pés não cria piso, voo ou imunidade à queda.

**Alternativa não escolhida:** procurar um ponto próximo e reposicionar automaticamente quando faltar espaço. Reduz recusas, mas acrescenta movimento involuntário e regras de trajeto/perigos/permissões. **Justificativa da escolha:** preservar controle do local e terreno construído. I define a entrada; o espaço de retorno é tratado separadamente em J.

### 8.61. M07-06J — DECIDIDO — Preservar espaço para o corpo-base durante a forma

**Aprovado em 21/10/2026.** Resposta explícita: **“Passagem preserva espaço do corpo-base (recomendado)”**.

**Regra aprovada:** para ocupação do terreno e passagem, o volume corporal liberado deve incluir também o espaço do corpo-base Arrancar na postura compatível. Formas maiores continuam exigindo mais espaço; formas menores não entram apenas por seu tamanho em um túnel onde o corpo-base, nessa postura, não caiba. O retorno de F pode ocorrer na posição atual sem teleporte ou prolongamento de benefícios sem manutenção.

**Consequência assumida:** limitar a vantagem de acesso de formas muito pequenas/baixas. Não é uma regra de anatomia de Bleach; é uma adaptação espacial do mod. Posturas permitidas precisam de correspondência explícita: agachar/nadar só podem reduzir esse volume quando ambos os perfis admitirem a postura. Não conceder rastejamento, novos meios ou alteração de hitbox de combate por esta regra de passagem. M06/M14/M16 precisam comunicar colisão e corpo de maneira coerente antes da entrega; não deixar uma barreira invisível sem feedback adequado.

**Alternativa não escolhida:** permitir passagens menores pelo corpo liberado e adotar uma regra especial quando o corpo-base não couber ao encerrar. Preserva acesso por tamanho, mas exige escolher deslocamento seguro, retorno adiado ou outra consequência; nenhuma foi aprovada, e manter poderes gratuitamente não é solução. **Justificativa da escolha:** como regra geral inicial, evitar essa dependência no retorno básico.

**Limites de ficha:** I valida o volume seguro resultante no início e na conclusão. Mundo alterado por pistão/comando/teleporte, surgimento de blocos, colisão com entidades e poses forçadas ainda exigem tratamento de M10/M16. J não promete invulnerabilidade, proteção de construção, teleporte corretivo ou apagamento de blocos; benefícios deixam de valer quando a manutenção termina. Movimento/atualização de volume não podem ser usados para atravessar o terreno. Exceção futura de um poder pequeno requer revisão explícita e contrato de retorno, sem exceção silenciosa.

### 8.62. M07-06K — DECIDIDO — Liberar e encerrar no chão, no ar e na água

**Aprovado em 21/10/2026.** Resposta explícita: **“Chão, ar e água, preservando queda e riscos (recomendado)”**.

**Regra aprovada:** permitir preparo/conclusão e retorno básico no chão, no ar e na água, quando espaço, postura, arma, fase e demais requisitos forem válidos. Não exigir apoio no chão só pela liberação. Caminhada/preparo de E continua limitado; o personagem pode olhar e realizar apenas o movimento compatível com o meio e sua fase. A gravidade, a trajetória já existente, a queda acumulada e os riscos ambientais continuam durante o preparo e a mudança. Transformar não freia a queda, não concede impulso, apoio espiritual, voo ou respiração extra.

**Herança de I/J e complementos para ficha:** validar o volume na posição real no início/conclusão; passagem e postura precisam admitir também o corpo-base. Uma transformação não cria a postura de nado ou agachamento ausente de um perfil. A ficha define as poses e o movimento compatível de ambos os corpos, inclusive retorno submerso; água não torna qualquer anatomia automaticamente incompatível nem concede capacidade aquática. Se faltar espaço/requisito, recusar sem cobrar a ativação não concluída. Benefícios específicos de voo/nado exigem sua própria ficha; não são aprovados nem proibidos universalmente por K.

**Alternativa não escolhida:** exigir apoio no chão para começar e concluir a liberação, cancelando ao perdê-lo. Facilita organizar a apresentação corporal, mas impede transformar durante queda ou nado e pode interromper o preparo ao descer um desnível. **Justificativa da escolha:** permitir uma escolha de combate/exploração sem usar a transformação para apagar o risco do ambiente.

**Ficha e evidência futura:** M06/M07/M10/M16 definem poses, volumes e continuidade de movimento. Conferir queda antes/depois da mudança, preparo submerso, perda de espaço durante o preparo e retorno no mesmo meio; não são testes executados. Alterações forçadas do mundo seguem pendentes de I/J, sem teleporte corretivo aprovado.

### 8.63. M07-06L — DECIDIDO — Guardar armamento separado conserva a forma válida

**Aprovado em 21/10/2026.** Resposta explícita: **“Guardar conserva a forma com manutenção (recomendado)”**.

**Regra aprovada:** se a ficha mantiver armamento separado, guardá-lo/trocar para uma ferramenta mantendo a arma no próprio inventário conserva Resurrección com manutenção integral. Suspender somente técnicas/bônus que realmente exijam essa arma empunhada; capacidades corporais e demais técnicas compatíveis continuam conforme suas fichas. Retomar o mesmo armamento não exige outra liberação enquanto a forma continuar válida. Troca respeita mãos, fases, Recovery e cooldowns, sem permitir usar a ferramenta como arma espiritual.

**Arma incorporada:** H já impede uma segunda espada utilizável/transferível. Não exigir empunhar uma peça que está incorporada nem considerar a incorporação, por si só, perda de posse. O vínculo/registro continua associado ao personagem e à mesma arma. A representação precisa impedir uso, depósito ou transferência simultâneos de uma cópia; isso não decide indestrutibilidade ou política de morte/drop. Partes corporais danificadas não são automaticamente um item que saiu do inventário; consequências particulares exigem ficha.

**Alternativa não escolhida:** para formas com armamento separado, encerrar toda Resurrección ao guardar essa arma. Simplifica alguns requisitos, mas faz a liberdade de ferramentas depender do tipo de armamento e cobra nova liberação ao retomar. **Justificativa da escolha:** preservar a exploração aprovada em H, mantendo as exigências reais das técnicas.

**Delegação:** M11/M16 especificam item incorporado, reserva de slot, inventário cheio e retorno do mesmo item sem cópia ou reparo gratuito; nenhuma solução de slot foi aprovada. Esse contrato precisa existir antes da entrega jogável. L não decide armadura ou encantamentos; M distingue depósito em baú de guardar no próprio inventário.

### 8.64. M07-06M — DECIDIDO — Perda efetiva da arma separada encerra a manifestação

**Aprovado em 21/10/2026.** Resposta explícita: **“Perda de posse encerra a manifestação (recomendado)”**.

**Regra aprovada:** se a arma vinculada permanecer separada e realmente sair da posse — drop, baú ou transferência —, encerrar Resurrección e benefícios dependentes, retornando ao estado-base conforme F/I/J. Conservar condição Arrancar, identidade, conquista e Mastery. Outra pessoa não recebe o poder; recuperar a arma não restaura a forma automaticamente, exigindo nova liberação válida e paga.

**Sem recolhimento gratuito:** encerrar não puxa a arma perdida de volta nem cria outra no inventário. O mesmo item/histórico fica onde a perda o deixou, com representação coerente com o encerramento; retorno e sincronização não podem duplicá-lo. Não aprovar arma substituta, reposição automática, imunidade à perda ou reparo por transferência. Se a ficha não possui arma separável durante a incorporação, não fabricar uma peça destacável para aplicar esta regra.

**Fases e efeitos:** pedir uma movimentação voluntária de item continua sujeito à fase; transferir não é um comando para cancelar ataque comprometido. Perda efetiva por evento válido precisa encerrar benefícios sem manutenção e invalidar ações realmente dependentes, com consequência definida na ficha. Disparos já autônomos seguem M06-11W; não desfazer impactos nem apagar todo efeito lançado. O tratamento de morte/logout/dimensão/reinício e de dano/destruição de componentes permanece para o contrato próprio, sem extensão automática de M.

**Alternativa não escolhida:** conservar a forma mesmo após perder/transferir a arma separada, exigindo posse só na próxima liberação. Permite continuar capacidades corporais, mas exige outro contrato para item distante, efeitos, retorno e acesso alheio. **Justificativa da escolha:** manter um vínculo de posse compreensível durante a manifestação e evitar deixar uma arma transferível liberada enquanto o dono retém seu poder sem contrato.

**Responsáveis e cenários futuros:** M07 valida a manifestação; M11 representa posse/item/local; M06 resolve ações/efeitos; M16 garante uma transição única no servidor. Distinguir guardar, depositar, recuperar e incorporação; verificar dois jogadores e inventário cheio sem duplicação. São cenários candidatos, não evidência de implementação ou testes.

### 8.65. M07-06O — DECIDIDO — Resurrección compatível com os estados de Reiatsu

**Aprovado em 21/10/2026.** Resposta explícita: **“Resurrección compatível com estados de Reiatsu (recomendado)”**.

**Regra aprovada:** permitir Resurrección básica com Natural, Supressão ou Liberação de Reiatsu, quando aprendidos e válidos. Manifestar apenas corpo/armamento, sem ataque associado, não encerra Supressão automaticamente; iniciar uma ofensiva válida continua encerrando-a em Natural, sem retorno automático depois. Pedido ofensivo recusado não a desliga. Supressão e Liberação continuam mutuamente exclusivas.

**Consequência:** Supressão não torna corpo, arma ou efeitos invisíveis nem garante esconder uma presença intensa; leitura continua por Controle, domínio, emissão e condições aprovadas em M06. Ela não reduz atributos ou suspende a manutenção de Resurrección. Liberação conserva sua manutenção e sua opressão elegível, sem bônus automático de dano ou opressão gratuita por transformar. Ativar/encerrar apenas a forma não solicita outro estado de Reiatsu; conserva somente o que ainda estiver válido, salvo os eventos de ciclo de vida de Q.

**Fronteira de efeitos:** uma forma com ataque, área ofensiva contínua ou efeito incompatível não ganha licença para continuar ofensiva oculta. A ficha precisa classificar ações/efeitos e sua interação com Supressão antes de entrar no catálogo. O não universaliza regeneração, voo, Hierro reforçado ou a combinação com Blut/Bankai e não libera híbridos. Técnicas anteriores compatíveis mantêm seus contratos.

**Alternativa não escolhida:** exigir presença Natural durante Resurrección. Reduz combinações, mas remove capacidades aprendidas de ocultação/pressão mesmo quando não há conflito funcional. **Justificativa da escolha:** permitir controle deliberado da presença com custos e sinais próprios, preservando os limites de M06-11J/K/L/R.

**Responsáveis:** M06/M07 classificam compatibilidade, início ofensivo e efeitos sustentados; M14 comunica presença/forma/custo; M16 mantém autoridade do servidor. Verificar liberação da forma sob Supressão, início ofensivo aceito/recusado, forma encerrada com presença ainda válida e efeito ofensivo contínuo com incompatibilidade declarada. São cenários candidatos; nenhum foi testado em Minecraft.

### 8.66. M07-06P — DECIDIDO — Resurrección antes de Liberação nas cobranças coincidentes

**Aprovado em 21/10/2026.** Resposta explícita: **“Resurrección → Liberação → ação (recomendado)”**.

**Regra aprovada:** quando as manutenções válidas coincidirem, pagar primeiro Resurrección, depois Liberação de Reiatsu; a ação usa o saldo no seu momento próprio. Prioriza o corpo/repertório escolhido antes da pressão exterior. Cobrar somente manutenções realmente devidas, sem reservar períodos futuros ou formar um pacote indivisível.

**Contrato estrutural:** tentar pagar Resurrección; se não puder, encerrar e voltar ao estado-base conforme F/I/J/K, sem débito parcial, restituição ou benefício não financiado. Ainda tentar Liberação com o saldo se continuar válida. Se Liberação falhar, retorna a Natural, sem desfazer Resurrección já paga. A ação posterior respeita os requisitos atuais: uma técnica exclusiva da forma não executa depois que ela terminou, mesmo se ainda houver Energia. Nenhum estado reativa sozinho ao regenerar.

**Momentos de cobrança herdados:** ativação inicial é custo de uma ação na conclusão, não manutenção de uma forma que ainda não existe. Manutenções já devidas precedem essa conclusão; sem Energia restante, não concluir nem cobrar a nova liberação. Uma nova Resurrección passa a gerar sua própria manutenção quando ativa, conforme D e sua futura ficha. A prioridade só resolve obrigações coincidentes, sem cobrança adicional por impacto, quadro visual ou consulta. Ações já emitidas/pagas não são desfeitas por vencimento posterior.

**Recursos e execução:** Stamina e demais recursos mantêm suas regras. Desligar voluntariamente evita períodos futuros, sem cancelar cobranças já devidas/resolvidas, devolver Energia ou apagar tempos. M05/M15/M16 detalham intervalos, saldo e ordem dos pedidos válidos; a interface não escolhe a ordem dos débitos. Pagamentos de outros efeitos sustentados exigem contrato próprio, sem permitir combinações raciais antes de decidir sua compatibilidade.

**Alternativa não escolhida:** Liberação antes de Resurrección, usando o saldo para a forma. Preserva a pressão exterior, mas pode retirar corpo/repertório enquanto mantém uma capacidade que não os substitui. **Justificativa da escolha:** priorizar a manifestação principal; o jogador continua podendo encerrá-la quando permitido para reservar recurso.

**Cenários candidatos:** saldo para ambos, somente para a forma, insuficiente para a forma mas suficiente para Liberação, ambos pagos sem saldo para ataque e ativação inicial concorrendo com manutenção anterior. Validar requisitos depois de um encerramento, sem tarifa parcial, reativação ou reset. Valores e evidência executável continuam pendentes; não são testes realizados.

### 8.67. M07-06N — DECIDIDO — Preservar os quatro slots normais de armadura durante Resurrección

**Aprovado em 21/10/2026 após esclarecimento.** Resposta explícita: **“A — Armadura continua funcionando (recomendado)”**. O pedido anterior de explicação não era aprovação/rejeição; esta resposta conclui a escolha.

**Herança:** M06-11A/B preserva proteções elegíveis e composição defensiva, mas delega compatibilidade de vestir/equipar a M07/M11. H permite itens conforme corpo/mãos/fase; ainda não aprova que toda armadura funcione em toda forma. N resolve especificamente Resurrección, sem estender a decisão aos corpos Hollow permanentes ou a outras raças.

**Regra aprovada:** manter acessíveis os quatro slots normais de armadura durante Resurrección. As peças legitimamente equipadas continuam protegendo e seus encantamentos compatíveis continuam atuando conforme suas próprias condições. A mudança corporal não obriga despir, soltar ou mover a armadura ao inventário; sua aparência pode ser adaptada ou ocultada conforme o perfil, com indicação clara do equipamento ainda ativo. A pele/placa corporal do modelo não conta como uma segunda cópia do item.

**Complementos para ficha:** conservar identidade, encantamentos e desgaste do mesmo equipamento; liberar/encerrar não repara, duplica ou reinicia suas condições. A proteção ativa continua sujeita ao desgaste legítimo do item, mesmo se sua malha não aparecer. Remover, quebrar ou trocar uma peça muda suas contribuições reais; aparência sozinha não conserva o bônus. Alterações voluntárias respeitam as restrições legítimas do item e da fase. Não criar slots extras pelo número de braços/pernas, novo conjunto gratuito, imunidade de armadura ou bônus universal de forma.

**Composição herdada:** Hierro e proteções próprias só existem quando compatíveis e declarados; não reaplicar uma mesma defesa como armadura do modelo mais Hierro mais cópia do equipamento. Reduções independentes seguem M06-11B, com limite/calibração ainda pendentes; pressão de Postura continua separada. Encantamentos condicionais continuam dependendo do evento que cobrem. Manter armadura não concede funções de outros equipamentos: escudo, ferramentas, armas e itens de mobilidade continuam consultando corpo, mãos, fase e ficha. Nenhum voo é concedido pelo aspecto alado da forma.

**Alternativa não escolhida:** cada perfil declara quais slots consegue vestir funcionalmente; peças incompatíveis permanecem armazenadas, com efeitos/desgaste suspensos enquanto a forma durar. Dá mais peso à anatomia e permite restrições próprias, mas exige comunicar conjuntos parcialmente inativos e compensar diferenças de equipamento entre poderes. **Justificativa da escolha:** preservar o valor da preparação em Minecraft e evitar que o formato corporal torne o mesmo equipamento inútil sem uma vantagem projetada que o justifique.

**Ficha e validação futura:** M11/M16 definem atualização de equipamento/desgaste sem duplicação; M06/M15 resolvem elegibilidade e calibração; M14 define apresentação do jogador e observadores, sem prometer um desenho final. Comparar peça equipada/removida/quebrada, transformação com inventário cheio, retorno e encantamento fora de sua condição. Esses cenários não foram executados. N não decide perda de itens na morte, encantamentos particulares, durabilidade de componentes do poder ou implementação da representação visual.

### 8.68. M07-06Q — DECIDIDO — Encerrar a manifestação no ciclo de vida; preservar conquistas

**Aprovado em 21/10/2026.** Resposta explícita: **“Encerrar forma; retorno manual (recomendado)”**.

**Regra aprovada:** morte, desconexão, mudança de dimensão ou reinício do mundo/servidor encerram o preparo incompleto e a Resurrección ativa. O personagem retorna ao estado-base Arrancar, sem restaurar automaticamente a forma ao voltar. Condição Arrancar, identidade, conquista da liberação, Proficiências e Masteries permanecem. Uma nova liberação válida é manual e paga normalmente.

**Exemplo:** você sai do servidor transformado. Ao entrar novamente, continua Arrancar e sabe usar sua Resurrección, mas precisa pedir uma nova liberação para voltar à forma. Se saiu durante o preparo, esse preparo não termina sozinho depois.

**Limites e integrações:** gastos legítimos não são devolvidos; cooldowns/recuperações seguem M06-11V. Forma encerrada não gera manutenção offline nem se religa quando recuperar Energia. Supressão/Liberação e demais estados transitórios seguem M06-11U, retornando a Natural; Q não restaura nenhum deles. A condição permanente de M07-05J não é desfeita. Não conceder recursos por limpar a manifestação nem decidir aqui os valores normais de respawn. Servidor precisa ordenar uma conclusão e um evento de saída sem cobrar ou aplicar a mesma operação duas vezes; detalhes de persistência/sincronização ficam em M16.

**Arma e equipamento:** encerrar representação corporal não concede recall, reposição ou segunda arma. H/L/M/N conservam identidade/posse/desgaste dos itens, sem equipar uma cópia ou reparar por relogar. M11/M16 ainda precisam resolver representação incorporada, retorno, inventário cheio e política de morte; Q não escolhe keepInventory, destino do drop, slot reservado ou recuperação de item. Guardar no próprio inventário e perder efetivamente a arma continuam eventos distintos. Spawn e chegada entre dimensões exigem local compatível em M10/M16; Q não aprova teleporte corretivo, remoção de blocos ou extensão gratuita da forma.

**Alternativa não escolhida:** restaurar automaticamente a Resurrección ao retornar, quando houver requisitos e Energia. Preserva a aparência anterior, mas exige decidir cobrança de retorno e pode reativar consumo/corpo em um lugar diferente. **Motivo da recomendação:** deixar o jogador escolher quando voltar a gastar e conservar a conquista entre sessões. Q tem aprovação própria; a comparação com Shikai/Bankai não substituiu a resposta.

### 8.69. M07-07 — Quincy: abertura, distinções e contratos herdados

**Abertura: 21/10/2026. Revisão: 22/10/2026. Estado: EM ESPECIFICAÇÃO.** A–E aprovados em 21/10/2026 e F–R em 22/10/2026, seções 8.70–8.87: dezoito decisões, sem perguntas pendentes desse conjunto. P/Q/R definem meios de manifestação, espaço na entrada e armadura; G01/G02 aprovados nas seções 7.3/7.4 resolvem padrões de ciclo de vida e passagem/retorno. Vollständig consolidado estruturalmente nesta revisão; Letzt Stil e demais particularidades Quincy continuam em elaboração. Resurrección A–Q mantém seu fechamento estrutural; demais contratos Quincy em elaboração.

| Camada | Distinção para o planejamento |
|---|---|
| Letzt Stil | Técnica antiga associada à luva Sanrei e a consequência sobre o poder. Não tratar como sinônimo de Vollständig ou como um buff comum sem consequência. Execução/recuperação complementadas por M07-08A–C, seções 8.89–8.91; fichas e valores continuam pendentes |
| Vollständig | Forma avançada distinta. Aquisição, manifestação, domínio e interação com o poder escolhido precisam de contrato próprio |
| Schrift | Poder individual, conforme RAC-20/RAC-23 e Q6/Q7 de M04; não é um nome genérico para a transformação nem uma segunda seleção cumulativa de poder |

| Herança vigente | Consequência neste bloco |
|---|---|
| RAC-09, M04/Q1–Q10 | Origem inicialmente independente; herança, afiliação e poder separados. Início independente não garante por si só acesso a todo ensinamento avançado |
| M04/Q7 e RAC-20/RAC-23 | Schrift exige contexto próprio; personagem associado pode ser rival ou adversário. Não escolher agora lealdade permanente, ritual universal ou poder aleatório |
| M05 e revisões de M06 | Spiritual Points principalmente nos atributos; domínio não é comprado. Aplicar revisões sobre a linguagem antiga de compra de técnicas de M04/Q11 |
| M06-09C e fechamento da seção 39 | Armamento, mobilidade e Gintō continuam com suas Proficiências; Blut/técnicas têm domínio próprio. Sem quarta Proficiência racial ou barra pessoal de Reishi |
| M06-09 e integração M06-11 | Arco/foco/mãos, custo na emissão, Blut exclusivo, provisões de Gintō e regras de mobilidade permanecem. Forma não concede automaticamente outro repertório nem altera esses contratos |
| M07-01 e M06-11U/V/W | Distinguir natureza, conquista, manifestação e domínio; fases, tempos e efeitos autônomos/dependentes mantêm seus contratos. Ciclo de vida específico da forma ainda exige decisão |

**Fronteiras:** C/D resolvem a ordem da conquista e a política de acesso/filiação. Fonte e conteúdo concretos, perfis, práticas/curvas concretas de domínio, valores de recursos, instrumentos específicos, valores/gestos do preparo, corpo/retorno concreto e consequências particulares ainda serão definidos. F/G resolvem ativação manual e o modelo básico de cobrança; H/I resolvem ausência de instrumento universal e mãos/itens durante a forma. Não criar uma forma original provisória, conceder toda manifestação do elenco ou exigir perda de poder por omissão. Habilidades específicas de voo, Sklaverei, absorção de entidades e alteração de blocos exigem pesquisa/ficha próprias; a aparência alada não concede automaticamente execução no Minecraft. M/N/O resolvem compatibilidade com Blut/Reiatsu e prioridade dos pagamentos por respostas próprias, sem herdar regras de Bankai/Resurrección. P/Q/R resolvem meios de manifestação, conferência de espaço na entrada e continuidade da armadura equipada. G01/G02 resolvem os padrões de ciclo de vida e passagem/retorno. Medidas/poses, instrumentos, intervenções do mundo e demais equipamentos específicos continuam exigindo ficha; exceção que altere um padrão exige decisão explícita. Outras formas e exceções continuam exigindo fonte/decisão.

**Sequência de análise:** A/B definem caminhos separados e modelo geral de conquista; C/D definem Schrift compatível antes da forma e fonte Quincy avançada sem vínculo permanente obrigatório. E define confiabilidade/Mastery própria; F/G definem ativação manual e cobrança. H/I definem instrumentos gerais e mãos/itens durante a forma; J/K/L definem preparo cancelável, retorno básico e transições. M/N/O definem compatibilidades de Blut/Reiatsu e manutenções; P/Q/R definem meios, espaço na entrada e armadura funcional. G01/G02 complementam passagem/retorno e ciclo de vida; C02/C09 e H/I governam dependências reais de armamento. Vollständig consolidado estruturalmente; seguir Letzt Stil com consequência/recuperação próprias e revisão Quincy. Essa ordem de documentação não define a ordem obrigatória das conquistas do personagem. Exceções de um poder precisam de fonte e decisão próprias.

### 8.70. M07-07A — DECIDIDO — Vollständig como forma avançada regular; Letzt Stil como caminho separado

**Aprovado em 21/10/2026.** Resposta explícita: **“Caminhos separados, sem Letzt Stil obrigatório (recomendado)”**.

**Regra aprovada:** organizar Vollständig como a forma avançada de uso regular para o repertório Quincy compatível e Letzt Stil como uma opção separada da tradição antiga, com aprendizado e consequências próprios. Aprender/usar Letzt Stil não é requisito universal para conquistar Vollständig. Não é uma sequência obrigatória “usar Letzt Stil → perder poder → recuperar → ganhar Vollständig”.

**Efeito para o jogador:** ele pode buscar sua forma avançada sem precisar sacrificar primeiro as capacidades que já desenvolveu. Quem escolhe estudar Letzt Stil terá uma decisão própria; a técnica não some do horizonte do projeto. Uso regular de Vollständig significa uma forma utilizável novamente sob seus requisitos, sem impor a ela a perda própria de Letzt Stil por associação; não significa manutenção gratuita ou repetição sem custo.

**Alternativa não escolhida:** fazer Letzt Stil e sua consequência uma etapa obrigatória anterior a Vollständig. Dá à passagem um arco de perda/recuperação comum, mas obriga todos os jogadores a experimentar essa consequência e acrescenta uma exigência não demonstrada pelas fontes consultadas. **Justificativa da escolha:** preservar a diferença das técnicas e a escolha da trajetória. A torna a relação entre elas uma adaptação explícita do mod.

**Limites:** A não decide a gravidade, duração, repetibilidade ou recuperação de Letzt Stil; não aprova perder conquistas/Mastery, mudar raça, esvaziar recursos, restaurar por relog ou tornar a técnica segura. Esse contrato precisa existir antes de introduzir o risco. Também não dispensa fonte apropriada, identidade/perfil compatível ou os futuros requisitos de Vollständig/Schrift. Não promete a mesma forma para todo poder, voo universal ou estágio superior automático.

### 8.71. M07-07B — DECIDIDO — Conquistar Vollständig por preparo, aprendizado e demonstração

**Aprovado em 21/10/2026.** Resposta explícita: **“Preparo, aprendizado e demonstração (recomendado)”**.

**Regra aprovada:** entre os requisitos de conquista, exigir preparo demonstrado nos fundamentos de Reishi, aprendizado específico de Vollständig por uma fonte apropriada e demonstração prática de manifestação/controle. Conclusão fica registrada; essa comprovação não se repete em toda ativação. A demonstração pode integrar uma quest compatível, sem cobrar outra prova equivalente depois.

**Efeito para o jogador:** evoluir com armas/técnicas o prepara, mas receber item, atingir um número ou ingressar em uma organização não manifesta a forma sozinho. A prática deve verificar o controle necessário ao uso, com exercícios/confrontos coerentes a definir em conteúdo; B não escolhe um mentor, boss ou minijogo.

**Sem requisito circular:** a primeira manifestação precisa ser possível no contexto de aprendizado. Não exigir Mastery prévia de uma forma indisponível, suas técnicas exclusivas ou todos os três campos maximizados. Gintō continua aprendizado separado, sem ser convertido em pré-requisito universal por esta aprovação. A prova não entrega domínio máximo ou todo o repertório do poder.

**Alternativa não escolhida:** desbloquear Vollständig automaticamente ao alcançar um marco numérico, satisfeitos os demais requisitos de acesso. Reduz conteúdo de aprendizagem, mas transforma a conquista em resultado automático do acúmulo de progresso. **Justificativa da escolha:** reconhecer aprendizado e controle específicos, sem copiar a relação com espírito de Zanpakutō.

**Complementado por C/D:** Schrift compatível conquistada antes da forma do mesmo poder e fonte Quincy avançada sem filiação permanente obrigatória. Instrutor/registro, era, obtenção e conteúdo concretos ainda precisam de ficha; B não garante acesso autônomo universal. E complementa a confiabilidade e a Mastery própria; F/G definem ativação manual e o modelo de Energia. H/I resolvem instrumentos gerais e mãos/itens durante a forma; instrumentos específicos e demais regras de execução continuam exigindo ficha/decisões próprias. M07 valida a conquista; M08/M12/M13 constroem acesso, orientação e demonstração; M16 registra uma conclusão válida uma vez.

### 8.72. M07-07C — DECIDIDO — Schrift compatível conquistada antes da Vollständig desse poder

**Aprovado em 21/10/2026.** Resposta explícita: **“Schrift compatível antes de Vollständig (recomendado)”**.

**Regra aprovada para o catálogo do mod:** exigir a conquista e o vínculo de uma Schrift canônica compatível antes da conquista da Vollständig associada ao mesmo poder principal. O aprendizado específico e a demonstração de B continuam necessários; conquistar Schrift não concede a forma automaticamente. A prática preparatória pode começar antes, sem registrar a liberação como aprendida ou permitir sua execução completa fora do contexto válido de aprendizagem.

**Para o jogador:** fundamentos Quincy → conquista da Schrift escolhida → aprendizado/demonstração de sua Vollständig. A identidade, a forma e o repertório ficam definidos no mesmo caminho. Continua um poder principal por vez; não se escolhe uma Schrift e depois uma Vollständig de outro personagem. A ordem é uma adaptação de progressão, não uma afirmação de que a obra provou esse requisito para todo Quincy.

**Alternativa não escolhida:** permitir conquistar Vollständig antes de Schrift. Amplia o percurso de fundamentos antes do poder individual, mas exige definir qual manifestação canônica e repertório estão disponíveis nesse momento, como se relacionam com a identidade posterior e quais técnicas ainda não existem. Não torna automaticamente legítima uma forma original provisória ou combinação livre. **Justificativa da escolha:** dar à liberação uma identidade reconhecível desde o primeiro uso, com um conjunto de capacidades coerente e menor dependência de uma forma genérica adicional.

**Limites e pendências:** compatível não significa que todo portador de Schrift tenha uma Vollständig confirmada ou que toda forma tenha os mesmos estágios. A ficha precisa de fonte e delimitação próprias antes de entrar no catálogo; não inventar manifestação por associação. C não exige Mastery máxima da Schrift, domínio de todas as técnicas, cargo de Sternritter ou uso prévio de Letzt Stil. Tampouco decide ritual, mestre ou mecanismo de aquisição da Schrift; M04/Q7 e a quest do personagem continuam exigindo contexto, sem transferência literal de poder por um rival qualquer. Exceções precisam de decisão própria.

C trata a ordem de conquista; não define que usar uma técnica ofensiva de Schrift seja necessário a cada ativação da forma. Perda/supressão de poder por um evento específico, troca do poder principal e consequências sobre aprendizado/manifestação continuam exigindo contratos próprios. Não apagar conquista nem garantir forma ativa nessas situações por omissão.

### 8.73. M07-07D — DECIDIDO — Fonte Quincy avançada, sem filiação permanente obrigatória

**Aprovado em 21/10/2026.** Resposta explícita: **“Fonte avançada, sem filiação permanente (recomendado)”**.

**Regra aprovada:** exigir acesso justificado ao conhecimento Quincy avançado pertinente à forma: instrução de alguém que a domine ou registro apropriado obtido e compreendido no conteúdo. O acesso deve ter relação com essa tradição avançada e respeitar era, personagem e condições da quest; não basta o professor dos fundamentos saber manipular Reishi. Não exigir filiação permanente ou lealdade contínua ao Wandenreich apenas para aprender e utilizar o que foi conquistado.

**Efeito para o jogador:** aproximar-se da organização pode abrir uma rota de ensino. Cooperação temporária ou acesso contextualizado a conhecimento também podem sustentá-la, sem obrigar todo personagem a servir para sempre. Obter um registro é uma fonte para estudo/prática de B, não um consumível que ensina ao clicar ou substitui a demonstração. Esses meios são adaptações aprovadas; nenhum NPC, registro, saque ou missão concreta foi selecionado.

**Afiliação e aprendizado:** entrar por si só não ensina a forma; sair/mudar de facção por si só não apaga a aprendizagem registrada. O acesso a serviços, locais e futuras oportunidades pode mudar conforme M08/M13/M15. Uso posterior ainda depende de poder compatível, recursos e fases que M07 especificará. A aprovação não impede consequências de um evento real de privação de poder; esse evento precisa de contrato próprio, em vez de ser presumido a partir de um marcador de facção.

**Alternativa não escolhida:** exigir filiação ativa e serviço ao Wandenreich como condição permanente de acesso/uso. Reforça a exclusividade institucional e o compromisso político, mas vincula a viabilidade da forma à permanência na organização e exige especificar a consequência de saída. **Justificativa da escolha:** dar valor ao acesso aos ensinamentos preservando escolha política e conquista pessoal, conforme a separação de M04/Q8. A independência inicial já aprovada não garantia acesso universal ao conteúdo avançado; D aprova essa política própria por resposta específica.

**Fronteira com Schrift:** D não cria via independente universal para receber Schrift, não dispensa o contexto de M04/Q7 e não remove o papel de Yhwach por uma frase sobre mentoria. A quest do poder precisa distinguir o personagem associado, o acontecimento de aquisição e a fonte de ensino da forma. Se usar o mesmo acontecimento para mais de um requisito, registrar cada conquista válida uma vez; não obrigar um ritual duplicado por separar sistemas. Condições concretas de acesso e retomada pertencem a M08/M12/M13, com disponibilidade multiplayer em M15.

### 8.74. M07-07E — DECIDIDO — Manifestação confiável e Mastery própria de Vollständig

**Aprovado em 21/10/2026.** Resposta explícita: **“Manifestação confiável + Mastery própria (recomendado)”**. Complementa a conquista de A–D, sem alterar sua ordem ou fonte de aprendizado.

**Regra aprovada:** depois da conquista válida de B/C/D, a manifestação básica funciona quando seus requisitos forem cumpridos, sem sorteio de falha por baixo domínio. Vollständig tem Mastery própria, desenvolvida por prática apropriada de manifestação e controle. O domínio pode melhorar execução e eficiência conforme a ficha; não entrega todas as técnicas ou versões posteriores, que continuam com aquisição e domínio próprios.

**Para o jogador:** a demonstração comprova que já consegue manifestar a forma; o treino posterior o torna melhor em utilizá-la. A primeira utilização conquistada precisa ser viável sem Mastery prévia da própria Vollständig. Ser iniciante não faz o comando falhar por azar, mas requisitos ausentes ou uma interrupção válida ainda podem impedir a execução.

**Integração de progressão:** preservar as três Proficiências Quincy de M06-09C e as Masteries já desenvolvidas de Blut, Hirenkyaku e demais técnicas. Nenhuma é zerada, substituída ou copiada para a Mastery da forma. Usar uma técnica compatível durante a transformação continua sujeito à prática dessa técnica; a ficha distingue o que também exercita o controle da forma, sem criar crédito duplicado para a mesma competência. Não surge uma quarta Proficiência, compra de Mastery, concessão por tempo AFK ou ganho por repetir liga/desliga sem prática pertinente.

**Alternativa não escolhida:** permitir falhas aleatórias nas ativações iniciais, reduzidas pelo domínio. Torna a inexperiência perceptível na própria transformação, mas acrescenta imprevisibilidade a uma capacidade cuja execução já foi demonstrada. **Justificativa da escolha:** concentrar o aprendizado nas decisões e no uso do poder, mantendo previsível o resultado de um pedido válido.

**Limites:** confiabilidade não significa ativação instantânea, proteção contra dano/interrupção ou uso sem recursos. F/G complementam a ativação manual e a cobrança de Energia, sem duração fixa universal. Preparo, valores dos custos, instrumentos, retorno e ciclo de vida ainda precisam de contrato. Não conceder voo, Sklaverei, novas técnicas de Schrift, estágios extras ou multiplicador universal. Práticas elegíveis, parâmetros afetados e curvas ficam com as fichas M06/M07 e a calibração M05/M15; M14 apresenta requisito/recusa/domínio e M16 implementa seu registro e autoridade no servidor. A conquista de B não exige repetir a prova a cada uso.

**Natureza da decisão:** adaptação de progressão aprovada para o mod. BL-03 e BL-28 identificam a forma e sua relação com poderes individuais, mas não estabelecem Mastery numérica ou probabilidade de falha por ativação. Não interpretar essa limitação como prova de que toda manifestação canônica é incondicional.

**Complementos posteriores:** F/G resolvem ativação manual e o modelo básico de recursos. Seguir instrumentos/mãos, preparo e reações, encerramento, compatibilidades e ciclo de vida. Letzt Stil mantém consequência e recuperação próprias antes da revisão Quincy.

### 8.75. M07-07F — DECIDIDO — Vollständig manual, independente da interface de combate

**Aprovado em 22/10/2026.** Resposta explícita: **“Ativação manual independente da interface (recomendado)”**. Proposta preparada em 21/10/2026. A–E continuam aprovados.

**Regra aprovada:** depois de conquistada, iniciar Vollständig por pedido manual, tanto em combate como explorando, dentro ou fora do modo de combate, quando requisitos e fases permitirem. Fechar a Combat Hotbar não encerra a forma por si só. Entrar em combate ou formar o arco não a ativa automaticamente.

**Para o jogador:** escolher o momento de transformar antes ou durante um encontro e continuar explorando transformado. A forma ativa continua com os custos de G e as restrições vigentes; fechar a interface não suspende manutenção, libera mãos ou apaga tempos de combate. J/K/L complementam preparo/reações, encerramento voluntário e recuperação das transições; ciclo de vida ainda exige decisão própria.

**Integração:** arco e Blut mantêm seus próprios controles e desligamento ao sair de Combat Mode, conforme M06-09L/Q e M06-11X/Y; continuar em Vollständig não conserva esses dois estados por omissão. Forma, arma espiritual e reforço corporal são estados distintos. H complementa a regra: nenhum desses instrumentos é requisito universal da forma; instrumentos específicos exigem fonte/ficha própria. A forma também não concede permissão universal para ferramentas, blocos ou ataques em fase incompatível.

**Alternativa não escolhida:** vincular a forma ao modo de combate, encerrando-a ao fechar a interface. Facilita limitar seu uso à luta, mas mistura escolha da manifestação com acesso à hotbar e restringe exploração. **Justificativa da escolha:** preservar o controle deliberado sobre uma capacidade conquistada e separar interface de estado do personagem. M14 definirá acesso legível ao comando fora da Combat Hotbar; nenhuma tecla, menu ou item foi escolhido. M16 valida o pedido no servidor, inclusive no singleplayer.

### 8.76. M07-07G — DECIDIDO — Energia ao concluir e manutenção enquanto ativa

**Aprovado em 22/10/2026.** Resposta explícita: **“Ativação + manutenção de Energia (recomendado)”**. Proposta preparada em 21/10/2026. Adaptação de recursos específica de Vollständig, sem importar valores de outras formas.

**Regra aprovada:** cobrar Energia Espiritual uma vez ao concluir a manifestação e consumir Energia enquanto Vollständig estiver ativa, sem duração fixa universal por uso. Cancelar antes de concluir não cobra a ativação; técnicas continuam com seus custos próprios. Se não houver Energia para a manutenção devida, a manifestação termina. K complementa o retorno básico a Quincy sem Vollständig, preservando Schrift/conquistas. Corpo/efeitos concretos e ciclo de vida ainda precisam de contrato, sem presumir perda da condição Quincy.

**Para o jogador:** pagar para entrar na forma e administrar a mesma reserva usada pelas técnicas para sustentá-la. O tempo útil depende dos custos e das condições vigentes, em vez de um contador universal igual para todos. Ao tentar concluir sem Energia suficiente, não aplicar a transformação nem sua cobrança. Energia para ativar não garante períodos futuros de uso; não reservar antecipadamente todo o consumo.

**Limites de recurso:** não acrescentar uma taxa geral de Stamina à transformação, barra de forma ou coleta manual de Reishi. Não recuperar Vida/Energia/Stamina ao transformar nem apagar cooldowns/Recovery. Cancelar não devolve despesas anteriores legítimas de outras capacidades. Técnicas e efeitos sustentados pagam conforme suas fichas; G não cria um custo adicional para toda passiva. Mudanças de máximos exigem conversão própria, sem refill por alternância.

**Reishi e calibração:** manter a influência ambiental já prevista em M05, sem garantir manutenção gratuita, drenar construções ou absorver seres por simplesmente ativar. Sklaverei exige contrato próprio. M05/M07/M15 precisam calibrar custo inicial, intervalos, eficiência e duração útil junto de tiros/Blut e demais gastos, preservando função jogável à forma recém-conquistada. M/N/O complementam a compatibilidade com Blut/estados de Reiatsu e a ordem das cobranças coincidentes, sem tratar todos os custos como um pacote indivisível.

**Alternativa não escolhida:** custo inicial com duração fixa por uso, sem manutenção básica durante esse intervalo. Torna o tempo disponível previsível, mas exige calibrar essa janela e suas regras de encerramento/reutilização. **Justificativa da escolha:** permitir administrar a duração pelo uso da reserva e pelas escolhas de técnica. Confiabilidade de E permanece: insuficiência de Energia é requisito ausente, sem sorteio aleatório de falha.

**Complementos posteriores:** H/I resolvem instrumentos gerais e mãos/itens durante a forma. Próximo: preparo/reações; encerramento/retorno; compatibilidades, prioridade das manutenções e ciclo de vida. Letzt Stil mantém contrato separado. Valores concretos, técnicas, conteúdo e artes continuam pendentes.

### 8.77. M07-07H — DECIDIDO — Forma sem instrumento universal; requisitos próprios por poder

**Aprovado em 22/10/2026.** Resposta explícita: **“Sem instrumento universal; requisitos por poder (recomendado)”**. A–G continuam aprovados.

**Regra aprovada:** para Vollständig já conquistada, não exigir universalmente foco/Cruz Quincy, luva Sanrei ou arco manifestado para iniciar e manter a forma. O aprendizado, a Schrift compatível e os requisitos de fase/recurso continuam necessários. Se uma manifestação específica exigir instrumento, sua ficha precisa justificar a exigência com referência e definir uso, posse/perda e acesso; não deduzir necessidade funcional de um adorno visual.

**Para o jogador:** possuir a Cruz não ensina Vollständig; perder a Cruz também não encerra automaticamente uma forma que não a exige. A Energia de G continua sendo cobrada normalmente. Isso não restaura equipamento nem permite usar técnicas que perderam seus próprios requisitos.

**Arco e forma separados:** o arco básico mantém o foco equipado de M06-09J. Sem esse foco, o arco/preparo seguem sua regra de perda, mesmo se Vollständig continuar válida. Não formar arma automaticamente ao transformar, criar outra Cruz, substituir um foco perdido ou dispensar os requisitos de uma arma individual. Uma aparência de armamento na transformação precisa ser classificada na ficha, sem duplicar item real ou conceder ataque por animação.

**Luva e tradição:** não criar uma luva avançada universal nem exigir Sanrei para toda Vollständig. A luva de Letzt Stil mantém análise e contrato próprios; H não decide como obtê-la, consumi-la, removê-la ou recuperar seus efeitos. Gestos/animação de ativação e possíveis restrições de mãos durante o preparo serão tratados na execução, sem conceder uso simultâneo de itens por esta dispensa instrumental.

**Alternativa não escolhida:** exigir foco físico equipado para iniciar/manter toda Vollständig, preservando a identidade material do equipamento Quincy. Cria dependência comum e uma consequência de perda que precisaria de reposição e regras próprias; não prova que Sanrei seja esse instrumento. **Justificativa da escolha:** manter a função de cada instrumento no poder que efetivamente o utiliza e separar a transformação conquistada da arma básica. A dispensa geral é adaptação aprovada, não afirmação universal sobre o cânone.

### 8.78. M07-07I — DECIDIDO — Mãos conforme arma/técnica; itens compatíveis durante a forma

**Aprovado em 22/10/2026.** Resposta explícita: **“Permitir itens compatíveis durante a forma (recomendado)”**. Trata a forma já ativa; não resolve as permissões do preparo.

**Regra aprovada:** Vollständig ativa não ocupa permanentemente as duas mãos apenas por estar ligada. Ferramentas, blocos e consumíveis podem ser usados quando o corpo/perfil, as mãos e a fase permitirem, sem encerrar a forma; a manutenção continua. Armas e técnicas definem sua ocupação e incompatibilidades próprias.

**Exemplo:** você está em Vollständig com o arco básico formado. Guarda o arco manualmente e passa à picareta; pode minerar mantendo a forma e pagando manutenção, quando a ação permitir. Não minera nem come ao mesmo tempo em que o arco ocupa as duas mãos. Voltar ao arco segue seu controle, foco e requisitos; não exige ativar novamente uma Vollständig que continuou válida.

**Integração:** M06-09L conserva arco básico de duas mãos, sem outro item simultâneo nessas mãos; M06-09T conserva guardar manualmente para Gintō que precise delas. Não guardar arma, reorganizar inventário ou equipar item automaticamente ao pedir uma ação incompatível. Troca e uso só começam quando a fase permitir; não cancelam ataques comprometidos ou apagam Recovery/cooldowns. Encerrar/guardar uma arma não encerra a forma por si só; a ficha declara se aquela arma é requisito da manifestação específica.

I não permite manter uma técnica de mãos ocupadas enquanto usa item incompatível, nem transforma picareta em arma do poder. Corpo/armamento especial pode restringir determinadas operações na ficha; não converter a aparência de asas em mãos adicionais, alcance de mineração ou voo. R complementa armadura/encantamentos por aprovação própria Quincy, mantendo quatro slots funcionais; Q resolve espaço na entrada, sem escolher medidas/poses. Outros benefícios e equipamentos específicos ainda exigem ficha, sem importação automática de Resurrección N. Materiais/consumíveis seguem seu gasto normal; transformar não duplica ou repara equipamentos.

**Alternativa não escolhida:** exigir encerrar Vollständig para usar ferramentas, blocos ou consumíveis. Simplifica essa separação durante a forma, mas acrescenta uma nova transformação paga para retomar o poder depois dessas ações. **Justificativa da escolha:** permitir integrar a forma à exploração de Minecraft com ocupação real das mãos e manutenção, sem tornar troca de item uma fonte de ações simultâneas. M11/M14/M16 detalham equipamento, feedback e autorização/sincronização; não foi escolhido slot ou biblioteca novos.

**Próximo:** preparo e reações; encerramento/retorno; compatibilidades/prioridade das manutenções, corpo/equipamentos restantes e ciclo de vida. Letzt Stil conserva contrato separado, antes da revisão Quincy. Fichas, valores, técnicas e artes permanecem pendentes.

### 8.79. M07-07J — DECIDIDO — Preparo breve cancelável para reagir

**Aprovado em 22/10/2026.** Resposta explícita: **“Preparo breve cancelável para reagir (recomendado)”**. A–I continuam aprovados.

**Regra aprovada:** preparo breve permitindo caminhar devagar e olhar ao redor. Ataque, bloqueio, Dodge ou Hirenkyaku disponíveis cancelam o preparo antes de começar, sem recuperação adicional apenas pelo cancelamento. Pedido indisponível dá aviso e conserva o preparo ainda válido, sem renovar seu prazo ou agendar reação futura. Cancelar antes da conclusão não cobra a ativação, conforme G; custos e tempos anteriores permanecem.

**Para o jogador:** pode desistir de transformar para reagir a uma ameaça. Isso não libera ataque ainda impedido pela recuperação de um tiro, não concede Hirenkyaku sem Stamina e não protege contra dano. M06-02S/T/U conserva a distinção entre dano, controle e interrupção: dano isolado não cancela automaticamente; controle apropriado ou perda de requisito pode invalidar o preparo. Não conceder Super Armor por aparência.

**Mãos e entrada:** não exigir mãos vazias universalmente só para iniciar o preparo básico. Segurar uma ferramenta ociosa ou manter o arco formado não impede por si só um pedido válido, mas não permite usar o item ou preparar/disparar flecha ao mesmo tempo. A ação anterior precisa permitir a transição; J não cancela automaticamente flecha preparada, canal, uso de item ou ataque comprometido para iniciar outra execução. Primeiro resolver essa ação conforme sua própria regra. Não guardar arma/mover inventário automaticamente, conservar duas cargas simultâneas ou apagar tempos. Gestos/animação e exceção instrumental de H precisam de ficha compatível, sem criar uma exigência universal adicional pela apresentação.

**Limites:** revalidar requisitos e Energia na conclusão; G cobra apenas se a manifestação concluir validamente. Duração e velocidade de caminhada ainda exigem valores. P permite chão/ar/água preservando queda/riscos; Q confere espaço na entrada. Medidas/poses e passagem/retorno ainda exigem contrato; J/P não concedem suspensão, voo ou respiração. M14 apresenta preparo, cancelamento e causa de recusa; M16 valida/ordena início, conclusão e interrupção no servidor.

**Alternativa não escolhida:** preparo comprometido, sem cancelamento voluntário para reagir depois de iniciado. Cria maior exposição deliberada, mas impede usar uma reação disponível até terminar ou sofrer interrupção. **Justificativa da escolha:** dar uma janela reconhecível de transformação mantendo uma escolha de reação, sem tornar o preparo um cancelamento universal de ações anteriores.

### 8.80. M07-07K — DECIDIDO — Encerramento manual e retorno a Quincy sem Vollständig

**Aprovado em 22/10/2026.** Resposta explícita: **“Encerrar manualmente e preservar conquistas (recomendado)”**. G já encerra a forma sem Energia para sua manutenção; K define a saída voluntária e o destino básico.

**Regra aprovada:** permitir encerrar Vollständig manualmente quando a fase atual autorizar, inclusive durante combate. Voltar ao estado Quincy sem essa manifestação, também quando faltar Energia para manutenção. Encerrar a forma por si só não remove a Schrift compatível conquistada, aprendizado, Proficiências ou Masteries. Não concede Letzt Stil ou outra transformação como retorno intermediário.

**Para o jogador:** pode parar de pagar a manutenção da forma quando não quiser mais utilizá-la. Recursos gastos não voltam; uma nova ativação concluída paga novamente conforme G. Recuperar Energia não reativa Vollständig sozinha. O estado-base continua sujeito aos requisitos das próprias capacidades; não recebe tiros ou técnicas de graça por estar sem a forma.

**Ações e efeitos:** o pedido voluntário respeita a fase, sem cancelar ataque comprometido ou apagar Recovery/cooldowns. Perda de manutenção encerra benefícios dependentes da forma sem conservá-los gratuitamente; técnicas e partes de efeito precisam declarar dependências. Disparos já autônomos e condições independentes seguem M06-11W, sem desfazer todos os efeitos emitidos só por encerrar. M/N/O complementam a prioridade de pagamentos simultâneos e a compatibilidade com Blut/Reiatsu.

**Fronteiras:** corpo, arma/armamento, inventário, espaço e efeitos concretos do retorno exigem fichas; não equipar/recriar item ou conservar técnica exclusiva da forma por omissão. A existência de arco básico independente não torna todo armamento de Vollständig independente. Privação efetiva de poder por evento específico é contrato distinto de encerrar uma manifestação; não importar perda de Letzt Stil ou apagar conquistas nessa saída. P complementa os meios de manifestação, preservando queda e riscos; G01/G02 aprovados complementam ciclo de vida e passagem/retorno, com fichas e intervenções forçadas do mundo ainda pendentes.

**Alternativa não escolhida:** impedir encerramento voluntário durante Combat State, mantendo a saída por falta de manutenção. Aumenta o compromisso de duração em confronto, mas obriga continuar consumindo Energia mesmo quando o jogador prefere o repertório-base. **Justificativa da escolha:** permitir administrar o próprio recurso sem remover o compromisso das ações em curso.

### 8.81. M07-07L — DECIDIDO — Transições sem recuperação adicional

**Aprovado em 22/10/2026.** Resposta explícita: **“Transições sem recuperação adicional (recomendado)”**. Complementa J/K, aprovados por respostas próprias.

**Regra aprovada:** ao concluir a manifestação, permitir ações disponíveis sem uma recuperação adicional apenas pela transformação. Encerrar voluntariamente também não acrescenta taxa ou recuperação própria, quando fase e retorno corporal permitirem. O preparo de J continua existindo; custos, cooldowns e recuperações anteriores permanecem. G continua encerrando a manifestação sem manutenção paga.

**Exemplo:** concluir a forma não libera um novo tiro se a recuperação ofensiva anterior ainda estiver contando. Se uma ação depender de corpo/arma ou técnica ainda indisponível, continua recusada. Encerrar não interrompe um ataque comprometido nem desfaz uma condição que já se aplica.

**Alternativa não escolhida:** acrescentar uma recuperação própria depois de manifestar ou encerrar. Cria uma janela adicional de vulnerabilidade, mas soma outra espera ao preparo e aos tempos herdados. **Justificativa da escolha:** deixar o ritmo nas fases e nas ações escolhidas; exceção de manifestação específica exige ficha e decisão explícitas. L não elimina requisitos de espaço, libera mãos ocupadas por uma técnica ou torna toda ação compatível.

**Próximo:** compatibilidades com Blut/Reiatsu e prioridade das manutenções; meios/corpo/espaço, equipamento e ciclo de vida. Letzt Stil conserva aquisição/consequência/recuperação próprias antes da revisão Quincy. Parâmetros, fichas, conteúdo e artes permanecem pendentes.

### 8.82. M07-07M — DECIDIDO — Vollständig com uma aplicação aprendida de Blut

**Aprovado em 22/10/2026.** Resposta explícita: **“Vollständig com uma aplicação de Blut (recomendado)”**. A–L continuam aprovados.

**Regra aprovada:** permitir Vollständig junto de uma aplicação de Blut aprendida e compatível: Vene ou Arterie, nunca ambas. Transformar não ativa Blut automaticamente, não ensina a aplicação nem elimina sua manutenção. Cada capacidade conserva requisitos, Mastery, fases e custos próprios. O define a prioridade das cobranças por aprovação própria.

**Para o jogador:** escolher entre proteção corporal de Vene e reforço corporal de Arterie durante a forma, pagando por essa combinação. Não ganha as duas aplicações pelo avanço da transformação. Trocar Blut conserva a transição breve sem nenhum dos dois reforços; a forma não remove essa janela.

**Limites herdados:** Vene cobre impactos elegíveis, sem cura, invulnerabilidade ou proteção residual automática contra queimadura/veneno. Arterie mantém golpes e componentes dependentes de força corporal; estar em Vollständig não estende automaticamente seu bônus a flechas, projéteis ou Gintō. Proteções compatíveis seguem M06-11A/B, sem aplicar a mesma redução duas vezes ou definir agora um multiplicador. Uma técnica específica precisa declarar seu dano e interações, sem prometer bônus universal por nome.

**Execução:** Blut pode permanecer durante preparo/forma enquanto válido e pago; isso não permite outra técnica em paralelo com o preparo de J. Pedidos de ativar/trocar Blut continuam sujeitos à fase. Sair de Combat Mode desliga Blut conforme M06 e F, mesmo que Vollständig permaneça válida. Encerrar Vollständig não encerra por si só um Blut independente ainda válido/pago; perda de requisito real segue sua regra. Sem religamento automático, cura, reposição de Energia ou concessão de Hierro/uma quarta Proficiência.

**Alternativa não escolhida:** exigir desligar Blut enquanto Vollständig estiver ativa. Simplifica os custos simultâneos e a composição defensiva, mas retira uma capacidade racial já aprendida durante a forma principal. **Justificativa da escolha:** preservar a função dos fundamentos e a escolha corporal, com custo e limites perceptíveis. A compatibilidade geral é adaptação aprovada; exceções justificadas do poder exigem ficha/decisão próprias.

### 8.83. M07-07N — DECIDIDO — Vollständig compatível com os estados de Reiatsu

**Aprovado em 22/10/2026.** Resposta explícita: **“Vollständig compatível com estados de Reiatsu (recomendado)”**. Estados Natural, Supressão e Liberação continuam mutuamente exclusivos conforme M06.

**Regra aprovada:** permitir Vollständig com Natural, Supressão ou Liberação de Reiatsu, quando aprendidos e válidos. Manifestar apenas a forma não conta automaticamente como ofensiva; iniciar uma ofensiva válida continua encerrando Supressão em Natural. Pedido ofensivo recusado não a encerra. Supressão não esconde corpo, asas, halo, arma ou sinais visuais; nem garante ocultação de uma presença difícil de controlar.

**Para o jogador:** pode manter a forma e tentar conter sua presença, ou pagar Liberação para exteriorizá-la. Não ganha invisibilidade, silêncio visual ou ocultação perfeita por acumular estados. A disputa entre Percepção/Supressão continua gradual pelas condições de M06, sem novo sorteio a cada leitura.

**Custos e limites:** Vollständig e Liberação pagam suas próprias manutenções. Supressão mantém seu contrato sem consumo de Energia; não reduz atributos automaticamente. Liberação não cria um bônus adicional automático de dano/Blut ou pressão passiva obrigatória no estado Natural. A presença da forma não torna a opressão paga gratuita. M permite a aplicação única de Blut também com esse estado de presença, preservando exclusividades e custos; N não concede nenhum deles só por conquistar Vollständig.

**Exceções por efeito:** se uma manifestação concreta já iniciar um ataque, área ofensiva ou emissão contínua ofensiva, sua ficha precisa classificá-la e aplicar a regra de quebra de Supressão; não proteger uma ofensiva só porque integra a transformação. N não autoriza trocar presença durante fase proibida ou cancelar técnicas para fazê-lo. Efeitos, leitura e observadores mantêm os limites de M06.

**Alternativa não escolhida:** exigir presença Natural durante Vollständig. Simplifica os estados simultâneos, mas impede usar controle de presença aprendido enquanto a forma está ativa. **Justificativa da escolha:** manter forma e manifestação da presença como escolhas distintas, com sinais observáveis e consequências próprias. Compatibilidade é adaptação aprovada, não regra universal extraída dos verbetes.

### 8.84. M07-07O — DECIDIDO — Vollständig → Blut → Liberação → ação

**Aprovado em 22/10/2026.** Resposta explícita: **“Vollständig → Blut → Liberação → ação (recomendado)”**. Aplica-se às combinações válidas de M/N, aprovadas por respostas próprias. M06-11T já define Blut antes de Liberação e manutenções antes da ação; O insere Vollständig antes dos dois quando a forma estiver ativa.

**Regra aprovada:** quando cobranças realmente coincidirem, pagar primeiro a manutenção de Vollständig, depois a da aplicação única de Blut, depois a de Liberação de Reiatsu. Resolver o custo da ação com a Energia restante, no momento próprio. Prioriza a forma principal escolhida e mantém Blut antes de Liberação. Sem Vollständig ativa/devida, a ordem anterior de M06-11T permanece.

**Contrato aprovado:**

1. Conferir estados válidos e somente as cobranças realmente devidas. Não reservar períodos futuros, antecipar manutenção da forma antes de ela existir ou cobrar por quadro/animação/consulta.
2. Tentar pagar Vollständig. Se não puder pagar, encerrar conforme G/K e retirar dependências; não gastar parcialmente ou conservar benefício sem pagamento.
3. Revalidar e tentar Blut, depois Liberação, com o saldo. Se um não puder pagar, ele termina e ainda se tenta o outro que continuar válido. Encerrar a forma não desliga automaticamente os demais estados independentes. Liberação encerrada retorna a Natural; Blut não troca de aplicação para tentar caber no saldo.
4. Revalidar a ação após possíveis mudanças de forma/recurso. Só executar se seus requisitos, fase e custo no momento próprio forem cumpridos. Não conceder uma técnica exclusiva de Vollständig depois que a forma terminou. Não devolver manutenções pagas por uma ação que acabou recusada.

**Ativação inicial e gastos independentes:** a ativação da forma paga na conclusão de G, como custo de ação, depois das manutenções já devidas dos estados então ativos. A ordem aprovada para sua manutenção começa quando Vollständig estiver ativa, sem antecipar cobrança durante o preparo. Flecha paga na emissão, leitura na conclusão; Stamina e conteúdo de Gintō mantêm os momentos próprios e não viram Energia pessoal. Supressão não entra como manutenção paga. Nenhum estado religa sozinho ao regenerar.

**Exemplo de escolha:** com pouca Energia, pagar a forma primeiro pode deixar Vene sem sua próxima manutenção. O jogador pode desligar capacidades quando a fase permitir para priorizar outro uso, sem estorno de despesas já devidas. Valores/intervalos precisam de calibração conjunta, sem tratar manter as três capacidades como obrigação de toda build.

**Alternativa não escolhida:** Blut → Vollständig → Liberação → ação. Favorece primeiro o reforço corporal e pode deixar a forma sem recurso antes, preservando a prioridade histórica do Blut também sobre a nova transformação. **Justificativa da escolha:** manter a transformação principal antes de reforços opcionais enquanto ela é escolhida e sustentada; não é uma ordem canônica nem consequência automática dos contratos de Bankai/Resurrección. O é ampliação explícita do caso com forma ativa, sem revisar Blut versus Liberação ou priorizar ação sobre manutenção.

**Complementado por P/Q/R:** meios de execução, espaço na entrada e armadura. **Complementado por G01/G02:** ciclo de vida e passagem/retorno; armas seguem dependências C02/C09 e H/I. **Próximo:** Letzt Stil e revisão Quincy. Exceções de poder, valores e apresentação continuam pendentes; linhagens/artes adiadas.

### 8.85. M07-07P — DECIDIDO — Manifestar/encerrar no chão, no ar e na água

**Aprovado em 22/10/2026.** Resposta explícita: **“Chão, ar e água, preservando queda e riscos (recomendado)”**. Proposta preparada na mesma data.

**Regra aprovada:** permitir iniciar/concluir a manifestação e encerrar Vollständig no chão, no ar e na água, quando corpo, espaço, fase e demais requisitos forem válidos. Não exigir apoio no chão como regra universal. No preparo, J mantém caminhada lenta onde aplicável e cancelamento para reação disponível; não se passa a caminhar no ar nem se interrompe a queda por transformar.

**Contrato aprovado:** conservar gravidade, trajetória e queda acumulada conforme o meio, colisões e riscos ambientais. A transformação em si não cria impulso, sustentação, plataforma ou respiração extra; não apaga dano de queda nem permite permanecer em um meio letal por aparência. Custos de G e ordem de O continuam; requisitos precisam continuar válidos na conclusão. Transformar durante uma queda continua sendo uma decisão de risco.

**Voo permanece no planejamento:** P trata onde transformar, não fecha como cada poder voa ou se sustenta. Capacidades específicas de voo, apoio espiritual e movimento precisam de pesquisa/ficha M06/M07, custos/limites M05/M15 e integração M10/M16. Se uma capacidade de sustentação já estiver legitimamente ativa, vale seu próprio contrato; não desligá-la nem concedê-la automaticamente por P. Sua perda precisa de regra de saída/queda antes de implementação. Aparência alada não substitui esse contrato.

**Alternativa não escolhida:** exigir apoio no chão para iniciar a manifestação. Simplifica a entrada, mas impede transformar enquanto cai ou nada, mesmo com os demais requisitos presentes. **Justificativa da escolha:** chão/ar/água para preservar a escolha durante exploração e combate, mantendo as consequências do meio. É adaptação aprovada, não regra universal demonstrada pela fonte.

### 8.86. M07-07Q — DECIDIDO — Conferir espaço do corpo na posição atual

**Aprovado em 22/10/2026.** Resposta explícita: **“Exigir espaço na posição atual (recomendado)”**. Proposta preparada na mesma data.

**Regra aprovada:** conferir o espaço do perfil corporal funcional de Vollständig antes de iniciar e ao concluir o preparo, na posição atual. Conservar referência dos pés e direção, sem reposicionamento automático. A caminhada permitida por J e o movimento/queda do meio usam a posição final real. Se não couber, recusar/encerrar a tentativa com aviso, conservando corpo anterior e conquistas, sem cobrar a ativação de G; custos legítimos de outras capacidades e tempos anteriores permanecem.

**Corpo e apresentação:** não padronizar todo poder como o mesmo humanoide com asas. A ficha distingue corpo funcional, partes que interagem com terreno/combate e adornos de apresentação. Um halo ou efeito apenas visual não aumenta a colisão de terreno nem concede alcance, dano, proteção ou área atingível por si só. Uma parte funcional não pode ser declarada cosmética para atravessar obstáculos ou ganhar alcance sem contrapartida. Volumes de ocupação e regiões de acerto são contratos distintos de M06/M07; não diminuir automaticamente o alvo de combate por ocultar a malha. Medidas e poses ainda não estão escolhidas.

**Limites:** não remover blocos, comprimir o corpo funcional, atravessar paredes, duplicar arma ou gerar outra forma para fazer o pedido caber. H/I preservam requisitos reais de armamento e mãos. Servidor é dono da validação, inclusive no singleplayer; M14 precisa comunicar a causa de recusa ao usuário e a manifestação aos observadores. Não foram escolhidas APIs ou bibliotecas.

**Retorno complementado por G02 aprovado:** Q resolve a entrada; G02 aplica a Vollständig e futuros perfis pertinentes a passagem compatível com o corpo-base, por aprovação geral própria. Medidas/poses e tratamento de intervenções forçadas do mundo ainda precisam de ficha antes da entrega. M10/M16 detalharão terreno alterado, pistões, teleporte, chegada e sincronização, sem teleporte corretivo, destruição ou manutenção gratuita aprovados por omissão.

**Alternativa não escolhida:** procurar uma posição próxima válida e mover automaticamente o personagem ao concluir. Facilita manifestar corpos grandes, mas transforma a ativação em reposicionamento e exige definir alcance, perigos e obstáculos. **Justificativa da escolha:** exigir espaço no local escolhido, para manter previsível a relação com construções e combate. É adaptação aprovada; não inferir dimensões ou colisão Minecraft da imagem canônica.

### 8.87. M07-07R — DECIDIDO — Manter os quatro slots normais de armadura funcionais

**Aprovado em 22/10/2026.** Resposta explícita: **“Armadura equipada continua funcionando (recomendado)”**. Proposta preparada na mesma data.

**Regra aprovada:** conservar os quatro slots normais de armadura funcionando durante Vollständig. Peças legitimamente equipadas continuam protegendo, com encantamentos compatíveis e desgaste normal; a aparência pode ser adaptada ou ocultada no modelo, sem apagar seus efeitos. Exemplo: um peitoral equipado continua protegendo mesmo quando não aparece sobre a forma. R tem aprovação Quincy própria; não foi transferida automaticamente de Resurrección N.

**Contrato aprovado:** são os mesmos itens, identidade, encantamentos e desgaste. Transformar/encerrar não equipa um conjunto novo, duplica, repara ou reinicia suas condições. Peça removida, quebrada ou trocada muda sua contribuição real. A aparência da forma não é outra cópia de armadura. Não criar slots extras por asas ou anatomia e não exigir despir/mover itens só para transformar. Mudança voluntária de equipamento continua sujeita a suas restrições e à fase.

**Composição:** seguir M06-11A/B, uma vez por defesa compatível, com limite conjunto e calibração ainda pendentes. Vollständig não concede Hierro nem uma segunda camada de proteção só por sua aparência. Vene continua separado conforme M/N/O, com elegibilidade e manutenção próprias; Arterie não se torna bônus para flechas. Encantamentos condicionais precisam de seu evento válido. Manter uma peça não concede uso simultâneo de ferramentas/escudo, mãos extras, voo ou respiração. Equipamentos de mobilidade e armas continuam com suas condições; R não aprova ocupação dupla de um slot, um item novo ou a interação de toda exceção.

**Alternativa não escolhida:** suspender os efeitos dos slots incompatíveis com cada anatomia, conservando as peças para voltar a funcionar ao retornar. Dá mais peso ao corpo, mas exige comunicar equipamento temporariamente inútil e equilibrar diferenças entre poderes. **Justificativa da escolha:** preservar a utilidade do equipamento preparado pelo jogador e adaptar sua apresentação, mantendo os mesmos itens e limites.

**Entregas:** M11/M16 detalham representação, inventário cheio, troca/desgaste e sincronização sem cópias; M06/M15 elegibilidade/cálculo; M14 apresentação legível ao jogador e observadores. Não decidir aqui política de drop na morte, reposição, modelo final ou durabilidade estrutural do poder. São pendências de ficha, sem evidência nova de gameplay.

### 8.88. M07-08 — Letzt Stil: perfil aprovado e fronteiras

**A/B/C aprovados em 22/10/2026**, por três respostas próprias registradas abaixo. M07-07A preserva o caminho separado/opcional, sem Letzt Stil obrigatório antes de Vollständig. O pacote articula aquisição, execução, consequência e recuperação; não apaga a identidade do personagem nem concede um segundo poder principal. Fonte e limites de pesquisa na seção 4. Contratos comuns aplicáveis estão declarados em B; G01/G02 não curam a consequência especial de C.

**Estado:** consolidado estruturalmente após revisão da seção 15. Ainda não é ficha executável, implementação, teste ou aceite. [Proposta original de 22/10/2026](historico/2026-10-22-proposta-letzt-stil.md) preservada para rastreabilidade.

**Experiência aprovada:** estudar a tradição Sanrei → preparar uma liberação extrema → escolher usá-la → ter uma janela breve de poder → perder acesso às capacidades Quincy → recuperar esse acesso por um procedimento aprendido. A consequência cria uma jornada recuperável, preservando a vida cotidiana de Minecraft e o desenvolvimento anterior. Letzt Stil não é uma segunda Schrift nem uma forma individual original provisória.

### 8.89. M07-08A — DECIDIDO — Aprendizado tradicional com Sanrei

**Aprovado em 22/10/2026.** Proposta preparada na mesma data. Resposta explícita: **“Aprendizado próprio com Sanrei (recomendado)”**.

**Pergunta apresentada:** “Para aprender Letzt Stil, recomendo preparo nos fundamentos de Reishi, orientação ou registro apropriado e prática com a luva Sanrei. Possuir a luva não ensina sozinho; Schrift, Vollständig e filiação permanente não seriam requisitos. O aprendizado já apresentaria o risco e a via de recuperação. Adotamos esse caminho?”

**Regra aprovada:** preparo nos fundamentos de Reishi, acesso contextualizado a orientação ou registro da tradição Sanrei e prática própria com a luva. Possuir/equipar o item ou atingir um número não ensina a técnica. Não exigir Schrift, Vollständig ou filiação permanente ao Wandenreich; tampouco prometer que qualquer professor básico domina esse ensinamento. A fonte concreta respeitará personagem, era e aquisição de M04/Q7 e M08/M12/M13.

O aprendizado deve explicar a perda e apresentar uma via de recuperação acessível antes de liberar o uso de risco. Primeira demonstração pode testar a preparação e o controle sem obrigar o aluno a consumir uma liberação completa e sofrer a perda apenas para aprender a habilidade. O uso completo continua com sua consequência normal; não criar uma primeira ativação gratuita/simulada que gere benefícios reais. Treino não exige permanecer conectado durante sete dias reais ou deixar o personagem AFK. Sanrei é instrumento de treino/liberação; não substitui automaticamente o foco exigido pelo arco básico.

**Alternativa não escolhida:** desbloqueio numérico ao alcançar um marco de Proficiência e possuir Sanrei, sem prática específica. Mais simples em conteúdo, mas reduz o entendimento e a preparação de uma capacidade com consequência relevante. **Justificativa da escolha:** o jogador conhece o risco antes de assumi-lo.

### 8.90. M07-08B — DECIDIDO — Janela breve com compromisso na liberação

**Aprovado em 22/10/2026.** Proposta preparada na mesma data. Resposta explícita: **“Janela breve com compromisso (recomendado)”**.

**Pergunta apresentada:** “Para usar Letzt Stil, recomendo uma janela breve com duração máxima, Energia cobrada ao concluir e sem manutenção básica; técnicas pagam seus custos. Concluir assume a perda posterior: encerrar cedo, morrer ou desconectar não a evita. Cancelar antes da conclusão não causa essa perda. Não pode coexistir com Vollständig. Adotamos esse perfil ou sustentação por manutenção de Energia, sem limite próprio de duração e com a mesma perda ao terminar?”

**Perfil aprovado:** pedido deliberado de liberar o preparo Sanrei, em combate ou explorando; não disparar Letzt Stil ao simplesmente tirar/trocar/perder a luva no inventário. Exigir Sanrei preparada no início e na conclusão válida. Aplicar a este perfil o preparo e as transições comuns C03/C07, sem novas votações de cada reação: caminhar devagar/olhar; reação disponível cancela antes da conclusão sem perda especial ou taxa; pedido indisponível mantém preparo ainda válido. A ação anterior precisa permitir começar; não executar flecha, item ou canal em paralelo.

| Diferença própria | Contrato aprovado |
|---|---|
| Duração e Energia | Janela com duração máxima breve a calibrar, sem prolongamento por regenerar Energia. Uma cobrança de Energia na conclusão válida, sem manutenção básica da forma; técnicas continuam pagando seus próprios custos. Não preencher Vida/Energia/Stamina, reparar itens ou apagar tempos. Valor, duração, eficiência e conversão de máximos: M05/M06/M07/M15 |
| Benefício | Potencializar aplicações Quincy de Reishi delimitadas na ficha, com foco no repertório já aprendido; não conceder todo repertório de Uryū, voo, Sklaverei, nova Schrift ou bônus universal a toda técnica. Nenhum multiplicador foi escolhido. Viabilidade e relação com Vollständig precisam de calibração, sem presumir superioridade universal |
| Momento de compromisso | Somente a conclusão válida cobra a ativação, inicia o benefício e registra uma vez a consequência de perda a efetivar ao término. Cancelar antes, falhar no espaço/recurso ou perder Sanrei antes disso não assume a perda. Uma ativação concluída não pode ser tratada depois como tentativa cancelada |
| Saída | Pode encerrar manualmente quando a fase permitir; atingir o limite ou perder validade também encerra. Toda saída após o compromisso efetiva a mesma perda de C. Encerrar cedo não reduz a consequência; não trocar para Vollständig para evitá-la |
| Ciclo de vida | G01 já cobre encerramento de preparo/forma por morte, desconexão, dimensão e reinício. Antes da conclusão, nenhuma perda de Letzt Stil; depois dela, a manifestação termina e a consequência de C permanece. Queda de conexão após ativar também encerra a oportunidade de uso. Sem restauração automática, extensão offline ou cura pelo evento |
| Relação com Vollständig | Não manter as duas simultaneamente. Ter aprendido Vollständig não apaga Letzt Stil; para iniciar esta, a primeira precisa estar encerrada por um pedido válido. Não combinar os benefícios nem usar transição como cancelamento gratuito. A perda aprovada em C impede Vollständig até a recuperação |
| Preparação para outro uso | Cada liberação completa esgota a prontidão Sanrei. Recuperar acesso às capacidades não deixa Letzt Stil rearmada; novo uso requer preparação apropriada. Não apagar aprendizado nem repetir toda a quest de conquista. Recondicionamento, integridade e eventual consumo do objeto precisam de ficha M07/M11 antes da entrega; não decidir receita, destruição ou reposição do item por omissão |

**Cobertura do pacote, sem novas perguntas genéricas:** C01/C02/C05/C09/C12 e G01/G02 continuam referências herdadas. O perfil aprovado adota C03/C04/C06/C07/C08/C10/C11 onde pertinentes, com as diferenças da tabela: ativação paga ao concluir e duração limitada substituem manutenção básica; a perda é uma consequência própria. Interface independente; chão/ar/água preservam queda e riscos, sem voo por aparência; espaço funcional e retorno seguem G02; armadura equipada conserva os mesmos quatro slots, efeitos elegíveis e desgaste. Sanrei deixa de ser requisito de manutenção depois de retirada na liberação; a ação não cria cópia, retorno automático ou reparo do item. Ferramentas e consumíveis continuam dependendo de corpo, mãos e fase. Arco básico mantém foco, manifestação manual e duas mãos; sair de Combat Mode ainda desliga arco e Blut, sem encerrar esta forma por si só.

Permitir também convivência com uma aplicação aprendida de Blut e um estado de Reiatsu válidos, preservando M06: nunca Vene e Arterie juntas, Arterie sem bônus automático a flechas/Gintō, ofensiva válida encerra Supressão e sinais visuais permanecem. Sem manutenção de Letzt Stil, a ordem de cobranças já devidas segue M06-11T: Blut → Liberação → ação; ativar é ação cobrada na conclusão, não manutenção antecipada. C encerra depois as capacidades dependentes da disponibilidade Quincy. Não são fatos canônicos universais; esta cobertura integra o perfil B aprovado, preservando os contratos e limites citados.

**Alternativa não escolhida:** sustentar o benefício enquanto conseguir pagar manutenção de Energia, mantendo a mesma consequência ao terminar. Facilita reaproveitar a economia de Vollständig, mas favorece duração prolongada e aproxima os papéis das duas formas. **Justificativa da escolha:** preservar o papel de recurso extremo com uma janela limitada. Sem duração numérica ou promessa de vencer automaticamente um encontro.

### 8.91. M07-08C — DECIDIDO — Perda de acesso recuperável, com progresso preservado

**Aprovado em 22/10/2026.** Proposta preparada na mesma data. Resposta explícita: **“Perda recuperável por procedimento próprio (recomendado)”**.

**Pergunta apresentada:** “Após Letzt Stil, recomendo suspender arco espiritual, Hirenkyaku, Blut, Gintō, Schrift e formas Quincy até concluir uma recuperação própria, com orientação e prática. Atributos, aprendizados, Proficiências e Masteries ficam preservados; atividades comuns de Minecraft continuam. Recuperar não exige técnicas suspensas nem outro jogador, e novo uso exige refazer o preparo Sanrei. Adotamos isso ou recuperação automática após um tempo?”

**Regra aprovada:** ao terminar uma liberação completa, registrar uma condição pessoal persistente de indisponibilidade Quincy. Não mudar raça/origem nem apagar atributos, Spiritual Points, vínculo da Schrift, aprendizados, Proficiências ou Masteries. Não é simplesmente estar com Energia zero: regenerar, trocar Cruz/luva, dormir, morrer, relogar, mudar de dimensão ou reiniciar não restaura o acesso.

| Durante a perda | Resultado para o jogador |
|---|---|
| Capacidades que dependem de acesso Quincy | Indisponíveis: formação/disparo do arco espiritual, Hirenkyaku, Blut, execução de Gintō, técnicas de Schrift, Vollständig e nova Letzt Stil. Desligar benefícios/canais dependentes e cancelar preparos ainda sem conclusão válida; não deixar emitir uma ação que perdeu o requisito |
| Provisões já preparadas | Conteúdo de Gintō e recipientes continuam itens reais; ter conteúdo não contorna a indisponibilidade de executar a fórmula. Não esvaziar/duplicar tubos, recolher itens transferidos ou apagar montagem persistente por associação; fichas próprias governam montagens |
| Minecraft e capacidades independentes | Andar, combate físico comum, ferramentas, construção, comida, armadura e sobrevivência continuam sob seus requisitos normais. Capacidade espiritual geral não é apagada por associação; sua disponibilidade precisa ser validada por dependências reais, sem converter BP/atributos preservados em licença para usar técnica Quincy bloqueada |
| Efeitos já emitidos | Aplicar C12/M06-11W: autônomos válidos não são recolhidos só pela perda, dependentes terminam, condições independentes não são curadas. Não classificar tudo como autônomo para contornar a suspensão |
| Jornada de recuperação | Procedimento próprio com orientação qualificada e prática adaptada, apresentado antes do risco, possível sem exigir as capacidades Quincy suspensas. Não exigir Schrift, Vollständig, outra Letzt Stil, outro jogador disponível ou filiação permanente para recuperar. M08/M12/M13 definirão instrutor/serviço, acesso e conteúdo coerentes; não foi escolhido um personagem ou ritual concreto |
| Falha e continuidade | Marcos concluídos da recuperação permanecem entre sessões; falhar pode exigir repetir a tentativa em andamento, sem apagar o histórico ou criar perda definitiva. Gastos normais permanecem. Evitar que cronologia/facção/NPC inacessível deixe o personagem sem rota de recuperação; M08/M10/M13/M15 devem demonstrar viabilidade no cenário de entrega |
| Recuperação concluída | Remover uma vez a condição e restituir acesso ao que já estava aprendido e continua compatível. Não restaurar forma/Blut/arco automaticamente, preencher recursos, conceder técnicas novas, Mastery ou uma Schrift substituta. O preparo Sanrei de B ainda precisa ser refeito para outro uso |

**Alternativa não escolhida:** retorno automático após um tempo determinado. Reduz a necessidade de conteúdo e o tempo dedicado à recuperação, mas transforma a perda principalmente numa espera. **Justificativa da escolha:** recuperação aprendida, com percurso proporcional e repetível, dá consequência narrativa preservando o investimento no personagem. Repetibilidade é adaptação aprovada; não foi comprovada como regra universal pela fonte.

**Proprietários e entregas antes de implementar:** M07 define estados, compromisso, saída e recuperação; M05/M06/M15 calibram custos, duração, benefícios, disponibilidade de capacidades comuns e viabilidade do período sem Quincy; M08/M12/M13 definem fonte, ensino e recuperação sem exigência circular; M11 define Sanrei/foco, slots, preparo/consumo/reposição e perdas de item; M10/M16 tratam corpo/chegada, persistência e eventos concorrentes; M14 comunica preparação, tempo restante, compromisso, capacidades bloqueadas e caminho de recuperação. Não entregar o risco antes da via de retorno. A revisão da seção 15 fecha o recorte estrutural, mantendo essas entregas como condições para uma ficha executável.

**Revisão e continuidade:** nenhuma pergunta A/B/C pendente. Revisão de Letzt Stil e do conjunto Quincy na seção 15; próximo Fullbringer por diferenças. Sanrei, benefícios, valores e recuperação concreta continuam exigindo fichas. Interface, armadura, meio e sessão estão cobertos pelo perfil aprovado; não voltar a votar esses detalhes. Conteúdo e calibração com responsável não são validação executada.


### 8.92. M07-09 — Fullbringer: desenvolvimento e manifestação

**A/B aprovados em 22/10/2026**, por duas respostas próprias. Fonte e limites na seção 4; [proposta original](historico/2026-10-22-proposta-fullbringer-ab.md) preservada. A registra completude/domínio; B define economia e cobertura operacional para as manifestações pertinentes. Não há transformação global obrigatória para todo Fullbring. C complementa o bloco em 8.95; Fullbringer consolidado estruturalmente e M07 fechado conceitualmente na seção 15. Fichas continuam pendentes; implementação, teste e aceite não avançados.

#### Herança aplicada — preservada

| Contrato vigente | Aplicação neste bloco |
|---|---|
| M04/F2/F3/F12/F13/RAC-23 | Fundamentos antes do poder principal; aquisição do Fullbring canônico em conteúdo contextual, um principal por vez. Sem objeto sentimental obrigatório no prólogo, poder original provisório ou combinação de repertórios. F12 prevalece sobre o diagrama antigo de RAC-10 |
| M04/F4 e M06-10/seção 16 | Vetor conforme o poder: objeto, equipamento, roupa, corpo ou outra representação apropriada. Não exigir item empunhado ou copiar foco/duas mãos Quincy para todos |
| M04/F6 + revisões M05/M06 | Domínio tem peso no desenvolvimento; Mastery não é comprada. Requisitos numéricos não substituem aprendizado. Não exigir Xcution/um NPC para cada melhoria ou uma reprodução da biografia de Ichigo |
| M06-10A–F | Uma Proficiência dos Fundamentos Fullbringer, Mastery das técnicas distintas e Bringer Light já aprovado. Não recriar essas decisões nem transformar técnica pessoal em fundamento geral |
| M07-C01/C02/C05/C09/C12 | Separar conquista, disponibilidade, manifestação e domínio; servidor valida requisitos reais, preserva gastos/tempos e classifica efeitos. Equipar/receber/guardar vetor não ensina nem transfere poder |
| G01/G02 | Manifestações temporárias encerram nos eventos aprovados; completude/conquista permanecem. Passagem/retorno corporal temporário pertinente preserva espaço da base. Não aplicar a limpeza à condição humana, ao aprendizado ou a toda condição já aplicada no mundo |

**Vetor indisponível:** C02 invalida capacidades que realmente dependem dele; fundamentos não dependentes não desaparecem. Um vetor corporal não é tratado como espada perdida do inventário. M04/RAC-10 já exige vínculo pessoal e recuperação do objeto: não votar de novo se o aprendizado passa ao destinatário ou se toda perda de item apaga o poder. Regra concreta de posse/empunhadura durante cada manifestação, destruição/reposição, slots/inventário cheio, morte e recuperação permanece em M07/M11/M16. Não conceder item indestrutível, recall, cópia, reparo automático ou keepInventory. A troca do Fullbring principal continua sendo processo especial pendente; perda de objeto não realiza essa troca.

### 8.93. M07-09A — DECIDIDO — Completude por marcos próprios e Mastery contínua

**Aprovado em 22/10/2026.** Proposta preparada na mesma data. Resposta explícita: **“Marcos próprios e Mastery contínua (recomendado)”**.

**Pergunta apresentada:** “Recomendo que cada Fullbring tenha seus próprios marcos de desenvolvimento. A completude reconhece domínio demonstrado, sem obrigar todos a terem três formas. A Mastery do poder continua entre os marcos, sem zerar ou criar uma barra para cada estágio; técnicas realmente distintas mantêm Mastery própria. Adotamos esse modelo ou Mastery separada para cada estágio?”

**Regra aprovada:** cada poder canônico tem um perfil de desenvolvimento coerente com sua referência: uso inicial útil, capacidades aprendíveis, marcos de domínio e eventual mudança real de manifestação. Não impor a todos a mesma sequência visual incompleto → intermediário → completo, nem inventar uma segunda forma de um poder apenas para preencher essa sequência. A aquisição por conteúdo já aprovada oferece o primeiro uso controlado cabível; não entrega toda a técnica, completude ou domínio máximo automaticamente.

**Completude:** marco pessoal registrado que reconhece domínio prático da manifestação correspondente. A ficha define quais aplicações demonstram esse controle e que preparo de Mastery contribui, sem exigir Mastery máxima universal, quantidade de abates sem relação ou uma prova equivalente repetida. A demonstração pode integrar treino/quest já existente; depois não se repete a cada uso. Se não houver nova aparência/forma sustentada pelo perfil canônico, a conquista é reconhecida pelo uso dominado do poder existente; não criar corpo/buff adicional. Completar tampouco concede todos os refinamentos, técnicas, poderes transferidos ou estágios posteriores. Cada conquista posterior tem seus requisitos quando cabíveis.

**Organização do domínio aprovada:** uma Mastery do Fullbring principal acompanha seu desenvolvimento; novos marcos desse mesmo poder não a zeram nem exigem uma nova barra para cada rótulo de estágio. Técnicas com execução distinta mantêm suas Masteries conforme M06. Não criar outra Proficiência do poder além dos Fundamentos Fullbringer. Uma manifestação realmente distinta só recebe domínio separado quando sua execução o justificar em ficha, não por ser chamada de completa. Critérios concretos de prática, contribuição e curvas permanecem M06/M07/M15, sem ganho por AFK, toggle vazio ou mero uso de itens vanilla.

**Confiabilidade:** após um aprendizado registrado, manifestação/execução básica válida funciona sem sorteio de falha por Mastery baixa. Ato inválido, interrupção, controle adversário ou perda de requisito ainda podem impedir o uso. Descontrole/perda efetiva como os episódios específicos da obra não vira rolagem universal ou punição automática; exigiria evento/ficha própria. Não exigir Mastery de uma forma inacessível para permitir seu primeiro aprendizado assistido.

**Alternativa não escolhida:** criar Mastery separada para cada estágio do mesmo poder, ainda respeitando seu vetor/referência. Torna o treino de cada etapa mais explícito, mas pode repetir o domínio da mesma execução e multiplicar indicadores sem função. **Justificativa da escolha:** continuidade do domínio, mantendo Mastery própria onde houver técnica realmente distinta. F6 já aprovou a importância de Mastery; a escolha nova é como representar seu avanço e a completude sem progressões redundantes.

### 8.94. M07-09B — DECIDIDO — Custos conforme manifestação e aplicação

**Aprovado em 22/10/2026.** Proposta preparada na mesma data. Resposta explícita: **“Custos por manifestação e técnica (recomendado)”**.

**Pergunta apresentada:** “Para a manifestação básica persistente de arma ou corpo, recomendo cobrar Energia uma vez ao concluir, sem manutenção só para conservar a forma. Técnicas, campos e benefícios sustentados têm custos próprios; poderes condicionais não recebem um botão global de transformação. Adotamos custos conforme manifestação e técnica ou manutenção universal para todo Fullbring ativo?”

**Regra aprovada:** cobrar pela manifestação/ação efetivamente executada. Completude aprendida não é um estado que paga manutenção por existir. O perfil distingue as situações abaixo; não impor uma transformação global e a mesma drenagem de Bankai a qualquer Fullbring.

| Tipo de uso | Contrato aprovado |
|---|---|
| Manifestação básica deliberada e persistente de arma/corpo | Energia uma vez na conclusão válida; sem manutenção básica apenas para conservar essa manifestação. Encerrar não cobra nem estorna; manifestar novamente paga normalmente. Técnicas, reforços e efeitos ativos mantêm custos próprios; manifestar não concede bônus universal de atributos ou proteção adicional pela aparência |
| Poder condicional ou aplicação sem forma persistente separada | Não inventar um botão/taxa global de transformar. A técnica declara comando, condições, pagamento e validade no seu evento real; isso não torna todos os passivos gratuitos nem permite efeito sem requisito |
| Canal, campo, espaço ou benefício sustentado | Ficha própria declara manutenção/condições e término do benefício dependente, quando houver; não recebe gratuidade por a manifestação básica não ter manutenção. Sem escolher agora duração, alcance, multiplicador ou uma fonte infinita de recurso |
| Recurso insuficiente | Não concluir/emitir ação cujo custo/requisito não foi atendido. Manifestação básica já válida e sem manutenção não se desfaz apenas por Energia zero; técnicas ainda precisam dos recursos próprios. Sem refill de Vida/Stamina/Energia ao manifestar, completar, guardar ou retomar |

**Perfil operacional aprovado em B, por referência aos contratos comuns:** para manifestações básicas que realmente tenham preparo/encerramento, adotar C03/C04/C06/C07/C08/C10/C11: pedido manual independente de Combat Mode, preparo breve com caminhada lenta/olhar, reação disponível cancela antes de começar sem taxa/recuperação extra pelo cancelamento, pedido inválido conserva preparo ainda válido, Energia só na conclusão válida, transições sem recuperação adicional respeitando fases e tempos anteriores. Não usar item/ataque/canal paralelo ou pedir manifestação para apagar compromisso de ação anterior. Chão/ar/água preservam espaço, colisão, gravidade/queda/riscos; G02 rege mudanças temporárias do corpo. Quatro slots normais de armadura preservam os mesmos itens, efeitos elegíveis e desgaste, com aparência adaptável e sem cópia/reparo. Com um estado válido de Reiatsu, apenas manifestar não conta como ataque; ofensiva válida encerra Supressão e sinais visuais permanecem. Essa cobertura integra B aprovado para as manifestações básicas pertinentes; não repetir votação de cada reação/slot/evento nem estendê-la automaticamente a toda técnica condicional.

**Limites de aplicação:** poderes sem manifestação persistente não ganham por associação essa fase, forma ou custo inicial. Mãos/empunhadura dependem de arma, vetor e capacidade real; ferramentas, comida e blocos só funcionam quando corpo, mãos e fase permitirem. Não exigir encerrar todo Fullbring para usar qualquer ferramenta. G01 encerra manifestação/preparo temporário sem apagar poder/completude/domínio ou restaurá-los automaticamente; não recolhe todo disparo nem desfaz efeito independente, conforme C12. Poder que encerre um espaço contendo participantes/itens precisa de saída segura explícita antes da entrega: G02 sobre corpo não resolve por si só esse caso de M10/M16. Nenhuma manutenção ou permanência gratuita de espaço foi aprovada aqui.

**Alternativa não escolhida:** exigir manutenção universal de Energia sempre que qualquer Fullbring estiver ativo, além dos custos das técnicas. Uniformiza a apresentação, mas cobra durante exploração mesmo de poderes cujo funcionamento é condicional e exige definir um estado global que nem todos possuem. **Justificativa da escolha:** custos por manifestação/efeito preservam diferenças e mantêm o custeio das aplicações sustentadas.

#### Entregas, limites e continuidade

| Responsável | O que ainda deve ser especificado |
|---|---|
| M06/M07 | Perfil canônico, uso inicial, marco de completude, Mastery/práticas e técnicas; distinguir manifestação, efeito condicional e benefício sustentado. Fonte precisa existir antes de criar estágio, execução ou exceção |
| M05/M06/M07/M15 | Custos/curvas, manutenção quando cabível, ritmo e utilidade dos perfis. Ordenação de cobranças e efeitos sustentados simultâneos entra na revisão comum de M07; B não escolhe prioridade universal para toda aplicação Fullbring |
| M07/M11/M16 | Disponibilidade do vetor, mãos/slots, guardar/perder/destruir/repor, vínculo e recuperação, dados e autoridade. C define o processo especial de troca e preservação do domínio específico; conteúdo, requisitos/materiais e realização ainda exigem ficha, com fundamentos preservados |
| M08/M12/M13 | Aquisição/ensino, práticas/demonstração e conteúdo de completude com era/identidade/acesso apropriados, sem quest duplicada por toda melhoria |
| M10/M14/M16 | Corpo/meio/retorno, espaços e participantes/itens, sinais de aprendizado/manifestação/custo, persistência e validação. Não presumir world rollback, editar bases ou apagar condições por fechar interface |

**Continuidade após aprovação:** A/B não têm perguntas pendentes. C foi aprovado em 8.95; revisão de vetor, fechamento estrutural de Fullbringer e conceitual de M07 na seção 15. Não recomeçar perguntas sobre fundamentos, item pessoal no prólogo, Proficiência ou mobilidade já aprovados. Linhagens/artes e campanhas específicas continuam adiadas.


### 8.95. M07-09C — DECIDIDO — Troca contextual com domínio preservado por poder

**Aprovado em 22/10/2026.** Proposta preparada na mesma data. Resposta explícita: **“Troca contextual, preservando domínio por poder (recomendado)”**.

**Pergunta apresentada:** “Para trocar de Fullbring, recomendo um processo próprio fora de combate, exigindo acesso e aquisição apropriados do destino. O anterior fica indisponível, com aprendizados, completude e Masteries guardados no histórico, sem copiá-los para o novo. Voltar ao antigo também exige um processo de reacesso; trocar o objeto não basta, mas não é preciso refazer toda a primeira campanha. Mantemos um principal por vez. Adotamos isso ou a troca deve apagar o domínio específico do poder abandonado?”

[Proposta original preservada](historico/2026-10-22-proposta-fullbringer-c.md).

**Regra aprovada:** permitir trocar o Fullbring principal mediante processo contextual próprio, deliberado e fora do estado de combate. Para um poder novo, cumprir a aquisição correspondente de M04/F3, respeitando personagem, era, vetor e um principal por vez. A quest de aquisição e o processo de troca podem integrar a mesma passagem, sem duas provas equivalentes. Não trocar pelo menu, por equipar outro objeto ou ao guardar/perder o vetor atual. A ficha definirá acesso, etapas, duração e eventuais despesas; nenhuma moeda, receita, taxa universal de Spiritual Points ou atalho gratuito foi escolhido.

| Momento / consequência | Contrato aprovado |
|---|---|
| Antes de concluir | O principal anterior continua registrado e só pode ser usado enquanto suas condições forem válidas. Iniciar o processo não dá capacidades do novo. Não conduzir técnica ofensiva/item/canal em paralelo com o preparo final de troca |
| Validação final | Servidor confere aquisição/acesso do destino, vetor, fase, espaço/itens quando pertinentes e ausência de combate antes de mudar o principal. Se um requisito falhar, não concluir a troca nem registrar dois principais; conservar apenas estado anterior ainda válido |
| Interrupção | Entrar em combate, cancelar, morrer, desconectar, trocar de dimensão ou reiniciar encerra a passagem incompleta, sem retomada automática. Principal/histórico anterior permanecem; manifestações ainda seguem G01 e gastos/etapas legítimos seguem suas fichas, sem restituição universal |
| Conclusão válida | O novo passa a ser o único principal. Encerrar manifestações, passivos e canais que dependam do principal anterior. Não restaurar recursos/itens, zerar cooldown/Recovery ou ativar automaticamente o novo poder. Não entregar domínio de um Fullbring ainda não praticado |
| Domínio do poder anterior | Preservar no histórico seus aprendizados, marcos/completude e Masteries, indisponíveis para uso enquanto aquele não for o principal. Não somar passivos, emprestar técnicas ou converter progresso de um poder no outro |
| Domínio do destino | Se novo, aprender/desenvolver conforme A e seu conteúdo; se já conhecido, recuperar o acesso ao próprio histórico compatível ao concluir o processo de retorno. Fundamentos, Mastery de Bringer Light, atributos e conquistas não específicos não são zerados pela troca |
| Retorno ao poder antigo | Exigir processo contextual de reacesso com requisitos próprios; possuir seu velho objeto ou seu histórico não basta. Não repetir automaticamente toda a primeira campanha já concluída nem dispensar preparo/acesso do processo. Conteúdo repetível deve ter esforço proporcional, sem virar loadout rápido antes de cada encontro |
| Efeitos já aplicados | C12/M06-11W preserva classificação: disparos autônomos finitos válidos não são recolhidos só pela troca; dependentes terminam. Condições/montagens persistentes têm ficha própria. A ficha de efeito longo precisa resolver coexistência/saída antes de permitir troca com ele; não classificar passivo/canal como independente para conservar vários principais |

**Justificativa:** respeitar a escolha de identidade sem destruir treino anterior ou copiar Mastery para um repertório diferente. O esforço fica no acesso/preparo e no desenvolvimento específico, preservando um principal utilizável por vez. Não é um sistema de híbridos, uma segunda raça ou fusão de Fullbrings. A preservação da Mastery entre marcos de A trata o mesmo poder; C trata sua indisponibilidade e retorno após mudar de principal, por isso foi registrado por resposta própria.

**Alternativa não escolhida:** manter o processo contextual, mas apagar/reduzir o domínio específico do principal abandonado. Dá peso maior à escolha e torna o retorno um novo investimento de treino; também pode punir experimentação e desencorajar aproveitar conteúdo. **Justificativa da escolha:** histórico preservado por poder, sem transferência ou acesso simultâneo. C não se estende automaticamente à troca de Zanpakutō, linhagem Hollow, Schrift ou natureza racial.

**Cânone e adaptação:** reconsultado diretamente BL-17 em 22/10/2026, trechos 63, 121 e 145 sobre eventos específicos de transferência/perda envolvendo Ichigo/Ginjō. Sinopse oficial do anime, não episódio/capítulo integral ou procedimento universal voluntário. C é política de gameplay aprovada em 22/10/2026 para concretizar a possibilidade especial deixada em M04/F13; não alegar que trocar objetos permitiria escolher outro Fullbring na obra. O catálogo e a aquisição canônica continuam obrigatórios.

**Entregas antes de implementar:** M07 define seleção/conclusão e história de domínio; M08/M12/M13 justificam acesso, aquisição/reacesso e repetição sem depender de um personagem indisponível na era; M11 trata vetor/slots/perda/reposição e eventuais materiais; M05/M15 calibra esforço e despesas sem zerar atributos ou converter Mastery entre poderes; M06/M07/M10 resolve efeitos e espaços persistentes; M14 mostra poder que sai/entra, capacidades indisponíveis e requisitos; M16 garante validação/persistência/ordem dos eventos, sem duplicação ou reset. Valores, rito, instrutor e possibilidade de reabrir todos os conteúdos de um poder não são aprovados por omissão.

**Revisão e continuidade:** C não tem pergunta pendente. Fullbringer consolidado estruturalmente com A–C; revisão comum e fechamento conceitual de M07 na seção 15. Custos e prioridade entre efeitos específicos, formas/híbridos excepcionais e fichas permanecem no escopo próprio; esse fechamento não torna todos os poderes executáveis. Não reabrir A/B ou perguntar de novo sobre cada reação/evento já coberto.


## 9. Integrações

| Responsável | Entrada/saída necessária |
|---|---|
| M04 | Natureza, condição evolutiva, aspectos adquiridos, vínculo e compatibilidade. H6 revisado e H9 permanecem vigentes. |
| M05 + M15 | Recursos, alterações de atributos/capacidades/Battle Power, intensidade, custos e calibração. Sem multiplicadores ou limiares inventados. |
| M06 | Técnicas permitidas por forma; fases, reações, controle, Recovery, mobilidade, passivas e manifestação da presença. M07 não concede todo repertório ao desbloquear uma forma. |
| M08 + M12/M13 | Orientação, provas, marcos narrativos e registro/retomada das conquistas; campanha solicita a operação, M07 valida o desbloqueio. |
| M09/M10 + M06 | Encontros e transições de corpo: ocupação do espaço, suporte, colisão, terreno e dimensões. |
| M11 | Zanpakutō/vetores, vínculo e posse, mãos/equipamento, perda, reposição e recuperação. |
| M14 | Controles e feedback de estado/requisito/custo/recusa dentro e fora do modo; apresentação para observadores. |
| M15 | Cooperação, permissões, consequências de derrota/saída e equilíbrio das diferentes rotas. |
| M16 | Persistência, sincronização, migração, fatias executáveis e evidência técnica de cada entrega. |

## 10. Conteúdo inicial

**Amostra de planejamento:** aquisição do primeiro Shikai e alternância entre espada selada e liberada, sem selecionar nova Zanpakutō ou ampliar seu catálogo. A–J estão aprovados; parâmetros, conteúdo da prova, efeitos particulares e ficha completa ainda impedem tratar a amostra como executável.

Uma prova futura deve demonstrar: aquisição uma vez, uso posterior válido, recusa compreensível sem requisitos e independência de Combat Mode sem burlar mãos/fases. Nenhum protótipo foi implementado ou testado nesta abertura.

## 11. Visão completa

Sequência estrutural percorrida: estrutura comum → Shinigami/Shikai e Bankai → Hollow/evoluções/Arrancarização/Resurrección → Quincy/formas avançadas e suas distinções → Fullbringer/completude/manifestações → revisão comum. O horizonte de conteúdo e a realização executável continuam nas entregas de 12.2, sem presumir catálogo integral concluído.

Híbridos, exceções de personagens e estágios adicionais precisam de fonte e decisão próprias. Não se presume o mesmo número de estágios, custo, benefício, desbloqueio ou regra de reversão nas quatro raças. Linhagens/Grimmjow e artes continuam para a etapa de conteúdo específico.

## 12. Decisões abertas

### 12.1. Decisões gerais concluídas — continuidade M08

**Fullbringer A/B/C aprovado**, seções 8.93–8.95; C: “Troca contextual, preservando domínio por poder (recomendado)”, em 22/10/2026. Processo contextual fora de combate, principal anterior indisponível com histórico próprio preservado, sem copiar domínio ou combinar repertórios; retorno exige reacesso apropriado. Nenhuma pergunta desses registros pendente.

**Revisão comum concluída no nível conceitual**, seção 15: perfis e C01–C12/G01/G02 cobrem a arquitetura de M07 sem conflito identificado no recorte examinado. Isso não resolve por omissão detalhes de fichas, custos, corpos, itens, provas, recuperação ou implementação. Nenhuma nova hierarquia universal de efeitos, exceção racial ou capacidade foi aprovada pela revisão. Não há pergunta geral atual impedindo avançar a M08 — NPCs, mentores e facções. Perguntas futuras de conteúdo/efeitos/exceções voltam no escopo correspondente.

**Estado de M07:** ESPECIFICADO CONCEITUALMENTE. Não implementado/testado/aceito por esta documentação. Entregas e critérios de reabertura nas seções 12.2/15; histórico preserva as antigas pendências e suas datas.

#### Formas Quincy — continuidade consolidada, preservada

**M07-08A/B/C aprovados em 22/10/2026**, seções 8.89–8.91. Respostas: “Aprendizado próprio com Sanrei (recomendado)”; “Janela breve com compromisso (recomendado)”; “Perda recuperável por procedimento próprio (recomendado)”. Nenhuma pergunta desses conjuntos pendente. Vollständig A–R + Letzt Stil A–C e G01/G02 estão consolidados estruturalmente, revisão na seção 15. Fullbringer e revisão comum foram concluídos depois no nível estrutural/conceitual, conforme seção 15; fichas/dependências delegadas permanecem.

**Continuidade atual:** Fullbringer A–C aprovado e consolidado estruturalmente; revisão comum de M07 concluída conceitualmente na seção 15. Próximo M08; fechamento Quincy preservado. Fichas de Sanrei, recuperação, benefícios, valores e realização técnica ficam nos módulos responsáveis, sem reabrir perguntas comuns já resolvidas.

#### Continuidade aprovada de Vollständig — preservada

**Método vigente desde 22/10/2026:** contratos já aprovados estão consolidados nas seções 7.1/7.2; não pedir novamente a mesma decisão para cada raça. G01/G02 aprovados em 22/10/2026 nas seções 7.3/7.4; nenhuma pergunta desses padrões pendente. A aplicação aos perfis cobertos dispensa nova votação. Letzt Stil tem seu pacote aprovado em 8.88–8.91; manter execução, consequência e recuperação distintas de Vollständig.

**A/B aprovados em 21/10/2026**, seções 8.70/8.71; nenhuma dessas perguntas pendente. A define Vollständig regular compatível e Letzt Stil separado/opcional, sem etapa obrigatória de perda; B define preparo de Reishi, aprendizado por fonte apropriada e demonstração registrada, integrável a quest sem duplicar prova. Abertura/herança na seção 8.69. Não recuperar compra de técnicas nem votar novamente nos fundamentos de M06.

**C/D aprovados em 21/10/2026**, seções 8.72/8.73. A–D mantêm suas quatro aprovações; E complementa o conjunto na seção 8.74. C exige Schrift compatível conquistada antes da Vollständig do mesmo poder; D exige fonte de conhecimento Quincy avançado sem filiação permanente obrigatória. Valores dos custos, práticas/curvas concretas de domínio e conteúdo continuam abertos; G define o modelo básico de cobrança.

**A–L aprovados nas seções 8.70–8.81:** conquista/domínio, ativação e Energia, instrumentos/mãos, preparo cancelável, encerramento manual e transições sem recuperação adicional. **M/N/O aprovados em 22/10/2026**, seções 8.82–8.84: uma aplicação de Blut e um estado de Reiatsu compatíveis com a forma; manutenções coincidentes Vollständig → Blut → Liberação → ação. P/Q/R aprovados em 22/10/2026 nas seções 8.85–8.87: chão/ar/água preservando queda/riscos; espaço do perfil funcional no início/conclusão sem reposicionamento automático; quatro slots normais de armadura funcionais, mesmos itens/desgaste e encantamentos elegíveis. A–R somam dezoito decisões aprovadas, sem perguntas pendentes. Datas anteriores preservadas. G01/G02 complementam o conjunto com ciclo de vida e passagem/retorno; dependências de armamento seguem C02/C09 e H/I. Vollständig consolidado estruturalmente na seção 15; Letzt Stil A–C também aprovado e revisado nessa seção. Próximo Fullbringer; linhagens/artes adiadas.

#### Resurrección — continuidade consolidada

**A–Q aprovados em 21/10/2026**, seções 8.52–8.68: dezessete decisões, sem perguntas pendentes desse conjunto. N preserva armadura funcional; O/P preservam convivência com Reiatsu e prioridade dos pagamentos. Complementos de ficha não foram aprovados em bloco.

**Q aprovado**, seção 8.68: encerrar preparo/manifestação no ciclo de vida, preservar conquistas e exigir retorno manual. Revisão estrutural concluída na seção 15; nenhuma pergunta A–Q pendente.

#### Efeitos e ações — consolidação herdada, sem nova votação

| Situação | Contrato já aprovado e limite |
|---|---|
| Disparo já emitido que a ficha classifica como autônomo | M06-11W: continua na região ativa até impacto/expiração ou invalidação própria; não é recolhido só porque a forma terminou. Descarregamento/reinício encerra os ataques efêmeros, sem reaparecer depois |
| Canal ou benefício que exige a forma ativa | M07-06F/M e M06-11W: termina quando perde esse requisito; não conserva corpo, bônus ou novas emissões sem a manifestação válida |
| Preparo/técnica ainda não concluídos | Revalidar corpo, arma, fase e recursos; não concluir execução que perdeu requisito. Custos por etapa e tempos já iniciados permanecem; ficha define a consequência concreta |
| Pedido voluntário de encerrar/guardar/transferir durante fase comprometida | F/G/L/M e M06: respeitar a fase. A troca de forma/item não é um cancelamento gratuito de ataque nem apaga Recovery |
| Impacto ou condição independente já aplicados | M06-11W: não desfazer impacto ou limpar condição só por encerrar a forma; duração, cura e persistência pertencem à ficha |
| Montagem persistente no mundo | Tem contrato próprio; não apagar, detonar, completar ou devolver provisões por confundir montagem com preparação na mão |

**Antes de entregar uma técnica:** a ficha precisa dizer quais partes exigem forma/corpo/arma e quais já são independentes. Não classificar todo Cero, área ou projétil como autônomo por aparência. Não foi criado um modelo universal de mutilação, quebra ou recuperação para Resurrección; casos particulares precisam de fonte e ficha, sem importar a quebra de Bankai ou conceder regeneração por alternar a forma.

**Revisão A–Q concluída:** seção 15 consolida situações, entregas e condições de retorno. As formas Quincy foram trabalhadas depois e estão consolidadas estruturalmente na seção 15. Este fechamento de Resurrección é estrutural; não transforma fichas delegadas em regras executáveis ou novas aprovações.

### 12.2. Entregas pendentes — fonte vigente após revisão comum

Não há pergunta geral atual para concluir a arquitetura dos perfis básicos. As entregas abaixo continuam obrigatórias antes de implementar/validar a capacidade correspondente. Nova escolha de produto em uma ficha será explicitada, sem usar o fechamento conceitual como aprovação automática.

| Entrega | Responsáveis e limite |
|---|---|
| Conteúdo canônico por poder: forma/repertório inicial, condições, estágios adicionais e exceções | M06/M07; fonte/continuidade, uma identidade principal e domínio apropriado. Catálogo integral e híbridos não estão especificados por este fechamento |
| Práticas, demonstrações, provas e progressão narrativas; acesso e recuperação | M07/M08/M09/M12/M13; sem requisito circular, prova duplicada ou risco sem via de recuperação. Inclui espírito da Zanpakutō, caçadas/marcos Hollow, máscara, Sanrei/Letzt Stil e Fullbring |
| Custos, duração/reuso, máximos e conversão de recursos, benefícios/mitigação e viabilidade | M05/M06/M07/M15; números/curvas não escolhidos, nenhuma reposição ou multiplicador universal. Fórmula de conversão deve respeitar a ausência de refill por alternar |
| Ordem entre efeitos específicos sustentados ainda sem ficha | M06/M07/M15/M16; registrar dependências/ordem determinística e demonstrar que não contraria prioridades já aprovadas. Não ordenar por nomes, menu ou chegada de pacote para inventar vantagem; nova prioridade que mude experiência exige decisão na ficha |
| Corpos, poses, colisões de terreno e acertos, meios, apoio e retorno | M06/M07/M10/M16; G02 cobre a passagem normal, não blocos/pistões/teleporte/chegada forçada. Espaços contendo participantes/itens precisam de saída própria; não entregar perfil afetado sem solução |
| Armas/vetores, vínculo, posse/empunhadura/slots, perda/recuperação/reposição e armadura | M07/M11/M16; distinguir objeto/equipamento/corpo, sem cópias/reparo/recall/keepInventory presumidos. Inclui Sanrei/prontidão e item incorporado/inventário cheio |
| Troca/reacesso de Fullbring e consequências específicas | M07/M08/M11/M12/M13/M15/M16; C já define a política, faltam processo/conteúdo/esforço. Não estender a outra raça ou fundir poderes sem decisão própria |
| Efeitos autônomos, canais, condições e montagens persistentes | M06/M07/M10/M16; cada ficha define dependências/término/saída sem apagar impactos independentes ou manter passivos de vários principais. Efeito longo precisa resolver coexistência antes de troca |
| Feedback, comandos, dados, persistência/migração e evidência | M14/M16; servidor autoriza, eventos não duplicam gasto/progresso, clocks seguem M06-11V. Há divergências históricas de runtime a conciliar; nenhum teste novo comprova implementação |

As revisões individuais da seção 15 continuam detalhando casos de cada caminho. Valores ou conteúdo novos não reabrem toda a aprovação geral; incompatibilidade/risco real volta ao contrato afetado. Próximo documento principal: M08.

### 12.3. O que falta no planejamento — panorama de 21/10/2026

> **Panorama histórico:** a linha de M07 abaixo antecede Vollständig/Letzt Stil/Fullbringer e a revisão comum, já concluídos conceitualmente em 22/10. A seção 12.2 e o fechamento da seção 15 governam M07; o relatório transversal de 22/10 registra os estados e a pendência de M04. Não retomar o próximo bloco a partir desta tabela antiga.

Este quadro separa a conclusão do sistema em discussão das entregas posteriores. Não é percentual de conclusão do jogo ou revisão integral da implementação.

| Etapa | Situação atual | Entrega que falta |
|---|---|---|
| M01–M04 — fundação, progressão macro e raças | Decisões de fundação e regras raciais registradas; revisões posteriores prevalecem | Aplicar contratos nas fichas e campanhas, sem reabrir por omissão o que já foi decidido |
| M05/M06 — atributos, recursos e combate | Especificados conceitualmente | Fórmulas, valores, fichas de técnicas, repertórios concretos e integração com código; pendências com responsáveis, inclusive M06/seção 48 |
| M07 — transformações | Shikai, Bankai, evolução Hollow e Arrancarização consolidados estruturalmente; Resurrección A–Q consolidada estruturalmente | Seguir formas avançadas Quincy com suas distinções, completude/manifestações Fullbringer e revisão comum de compatibilidades, corpos, custos, domínio e ciclo de vida. Híbridos/exceções só com fonte e decisão próprias |
| M08 — NPCs, mentores e facções | Documento próprio não iniciado | Funções, relações, acesso, serviços, ensino e disponibilidade por era; não pressupor todo personagem como professor amigável |
| M09 — mobs, bosses e encontros | Documento próprio não iniciado | Famílias, comportamento, desafios, fases, participação e recompensas |
| M10 — dimensões e estruturas | Documento próprio não iniciado | Acesso, travessias, locais, terreno, spawn/retorno e corpos grandes em mundo construído |
| M11 — itens e economia | Documento próprio não iniciado | Armas/equipamentos, criação, materiais, estoque, posse, perda, reposição e economia |
| M12/M13 — quests e campanhas | Documentos próprios não iniciados; storyline macro já existe | Primeiro regras de objetivos, grupos, falhas e recompensas; depois conteúdo das campanhas, provas e sidequests |
| M14/M15 — interface, multiplayer e equilíbrio | Documentos próprios não iniciados | Controles, informação, acessibilidade, party, PvE/PvP, encontros compartilhados, catch-up e calibração |
| M16 — integração, testes e roadmap | Documento próprio não iniciado | Escolher fatias jogáveis, dados/migrações, ordem técnica, cenários de teste e critérios de aceite |

**Depois dos sistemas gerais:** fichas das linhagens e técnicas, provas/quests, medidas e apresentações concretas. Grimmjow/Pantera continua a primeira linhagem quando essa etapa começar; artes ficam adiadas. Não é necessário catalogar todos os poderes antes de propor uma entrega jogável: M16 organizará entregas coerentes conforme as dependências.

**Para Resurrección em particular:** os custos numéricos, conteúdo de aprendizado, repertório inicial, poses/volumes, item incorporado/inventário cheio, efeitos particulares e apresentação ainda precisam de ficha. A regra de passagem cobre ocupação normal; pistões, teleporte, novos blocos e outras alterações forçadas precisam do contrato M10/M16 antes da entrega. Esses itens não são dezessete decisões a votar novamente.

**Leitura do progresso:** avançamos nas regras do jogo. Isso não demonstra que as quatro rotas já estejam implementadas, testadas ou aceitas. A auditoria de código existente continua tendo sua data e escopo próprios; esta rodada é documental.

## 13. Critérios de aceitação

**Critérios documentais vigentes de Shikai A–D:** obter a Zanpakutō não concede Shikai; o despertar exige vínculo/prova e fica registrado; o uso posterior é manual, independente da interface e sujeito a requisitos; Bankai/técnicas posteriores não são entregues por associação.

**Cenários para validação futura, ainda não executados:** tentar liberar sem vínculo/arma/aprendizado; cumprir a prova e usar depois; entrar/sair de Combat Mode com Shikai; pedir liberação durante fase incompatível; usar ferramenta/item incompatível; trocar/perder arma; morrer, sair, mudar de dimensão e reiniciar. E–J definem as expectativas básicas desses eventos; fichas precisam completar os efeitos particulares antes da validação executável.

Para Shikai E/F/G, verificar futuramente: reação válida cancela e reação recusada preserva preparo; cancelamento anterior não cobra; dano isolado não é interrupção universal; trocar para ferramenta conserva somente a manifestação válida e não permite ataques de espada pela ferramenta; guardar na própria posse não equivale a depositar em baú; perda de posse encerra dependentes sem recolher disparo autônomo; recuperar arma mantém a conquista, mas exige nova liberação. Nenhum desses cenários foi executado como teste nesta rodada.

Para Shikai C/D: cobrança única ao concluir, falha sem manifestação/custo indevido, técnicas com consumo próprio e prática sem liga/desliga/AFK. Confiabilidade da manifestação não é imunidade a interrupções ou dano. Esses critérios foram aprovados conceitualmente; sua execução ainda não foi validada.

**Bankai A–N — critérios documentais adicionais:** conquista e domínio próprios; ativação direta/manual com cobrança na conclusão e manutenção; preparo cancelável sem gerar Shikai gratuito; retorno válido a Shikai sem nova cobrança; conclusão/encerramento sem recuperação adicional; guardar conserva Bankai pago com requisitos de empunhadura; perda de posse encerra, sem transferir ou restaurar o poder. Reiatsu conserva sinais e custos; manutenção de Bankai precede Liberação e a ação; morte/logout/dimensão/reinício encerram manifestações sem restauração automática. M/N distinguem quebra estrutural de desgaste comum e preservam dano até recuperação específica, sem apagar conquista/Mastery. O risco só pode entrar com recuperação disponível. São critérios aprovados conceitualmente, sem execução em Minecraft.

**Critérios estruturais Hollow aprovados em A/B/C:** derrotar sem consumir não registra predação; conclusão inválida/duplicada não duplica crédito; cooperação útil não exige último golpe; ganho AFK não substitui caçada; cumprir requisitos não muda automaticamente o corpo; pedido sem espaço conserva o estágio anterior. A forma de verificar esses resultados depende das fichas, dos critérios de participação e dos detalhes operacionais das transições; G–M complementam suas regras básicas. Os núcleos foram aprovados; representação de consumo, distribuição de oportunidades e execução permanecem em elaboração. Não são testes executados.

**Critérios estruturais aprovados em D/E:** a formação Gillian representa agregação sem exigir personagem de outro jogador; cumprir o marco não força mudança de corpo; Gillian conserva agência; a conquista Adjuchas pode ser validada sem linhagem definitiva; uma quest compatível não cobra duas vezes a mesma prova. Detalhes e cenários de falha ainda dependem de conteúdo/ficha; nenhuma validação em Minecraft foi executada. F/G foram aprovados por respostas próprias, sem escolher conteúdo concreto ou números.

**Hollow F–M — critérios documentais adicionais e cenários futuros:** preservar marcos concluídos entre sessões; não remover estágio por dano/morte comum/falha; exigir novo preparo para tentativa interrompida; devorar parado, com reação válida cancelando e pedido recusado preservando execução ainda válida; não cobrar nem repor recursos pela devoração ou pela evolução. Rejeitar espaço insuficiente no início e na conclusão sem reposicionar ou apagar marcos. Evoluir somente fora de Combat State, parado e com apoio válido; entrar em combate, mover-se ou perder apoio cancela sem recuperação adicional. Morte/logout/dimensão/reinício encerram devoração/evolução incompletas; conclusão anterior válida mantém o novo estágio, sem comando automático ou crédito duplicado. Tempos anteriores seguem M06-11V. A representação da presa, atribuição do crédito, retorno em corpo grande e demais complementos exigem ficha própria antes da validação executável. Nenhum cenário foi executado em Minecraft.

**Arrancarização A/B/C — critérios documentais aprovados:** não conceder a condição apenas por número ou por concluir Gillian; permitir uma via pessoal sem facção/Hōgyoku obrigatórios; verificar preparo e acontecimento pertinentes no estágio escolhido, sem requisito circular ou duplicação do mesmo marco por quest compatível. A conclusão entrega a arma própria, sem Asauchi, Resurrección, técnicas exclusivas ou domínio automático. O personagem conserva a origem histórica e encerra a evolução normal conforme H9. Conteúdo, corpo, fases, custos, falhas e contrato de inventário ainda precisam de ficha; não são cenários executados em Minecraft.

**Arrancarização D/E — critérios documentais aprovados, com validação futura ainda não executada:** as três origens receberem perfil-base humanoide válido sem volume/alcance duplicados; conservar origem e desenvolvimento; técnica compatível já aprendida continuar sem novo desbloqueio; técnica corporal incompatível não executar; nova capacidade não aparecer dominada; regeneração rápida depender de compatibilidade específica. Validar depois espaço, inventário, custos, fases e sincronização conforme fichas e decisões de execução ainda abertas.

**Arrancarização F/G — critérios documentais aprovados, ainda sem validação em Minecraft:** conservar o desenvolvimento compatível sem nivelar personagens ou conceder técnicas novas; não aplicar bônus fixo, redução de teto ou taxa de treino apenas pelo rótulo da origem; conservar a origem e encerrar a evolução normal em H9. Não cobrar Energia/Stamina adicional pela passagem/manutenção da condição, nem restaurar recursos ou apagar tempos. Comparar futuramente trajetórias completas nas três origens e seus conteúdos compatíveis; viabilidade avançada, fichas e conversões continuam pendentes e não foram demonstradas pela aprovação.

**Arrancarização H/I/J — critérios documentais aprovados; cenários futuros ainda não executados:** iniciar deliberadamente após requisitos, parado/apoiado e fora de Combat State; cancelar por movimento, combate ou perda de apoio e para reação disponível, sem recuperação extra apenas pelo cancelamento. Validar espaço no início e na conclusão sem remover blocos ou reposicionar; recusa conserva corpo anterior e marcos. Interrupções e eventos de ciclo de vida encerram preparo incompleto, sem retomar sozinho; conclusão válida anterior mantém Arrancar. Custos/tempos e desenvolvimento seguem G/J/E/F. Repetir login ou pedido não deve duplicar arma/conquista; inventário, ordem de conclusão e persistência ainda dependem de ficha/M16 antes de validação executável.

**Resurrección — critérios documentais de A/B aprovados; validação futura ainda não executada:** possuir a arma não libera a forma; estado, identidade e conquista precisam ser verificados. Uma demonstração integrada à quest registra a mesma conquista uma vez. As três origens têm percurso compatível sem retorno à evolução normal; a primeira execução é possível sem Mastery prévia da forma. Após conquista, uma ativação válida deve ser confiável, sem entregar todas as técnicas ou Segunda Etapa. Prática deve distinguir forma/técnicas/Proficiências e não crescer por AFK ou liga/desliga. Fichas e decisões de execução ainda são necessárias; não são resultados de gameplay.

**Resurrección — critérios documentais de C/D aprovados; validação futura ainda não executada:** pedir manualmente com arma própria e conquista válida dentro/fora do modo; fechar interface sem encerrar por si só; recusar arma alheia/fase/local inválidos sem cobrança indevida. Cobrar ativação uma vez ao concluir e manutenção somente ativa; cancelamento anterior não paga, nova liberação paga novamente. Falta de manutenção encerra manifestação, sem apagar a conquista ou restituir recursos. E/F/G complementam preparo e retorno básico; representação concreta da arma/corpo, poses, volumes e valores continuam pendentes; H–P complementam espaço, meios, equipamentos e pagamentos. São critérios conceituais aprovados; complementos de ficha ainda precisam de especificação, sem evidência de gameplay.

**Resurrección — critérios documentais de E/F/G aprovados; validação futura ainda não executada:** reação válida cancela antes de começar e reação recusada conserva preparo válido; cancelar não cobra ativação; dano isolado não interrompe universalmente. Conclusão e encerramento voluntário não apagam tempos nem acrescentam recuperação universal. Retorno básico é Arrancar, sem desfazer a condição adquirida ou devolver recursos. Mudanças corporais e armas exigem ficha espacial/equipamento antes de validação executável; não se trata de teste realizado.

**Resurrección — critérios documentais de H/I/J aprovados; validação futura ainda não executada:** arma incorporada não permanece como cópia selada utilizável; itens compatíveis não encerram a forma nem suspendem manutenção, e mãos ocupadas continuam ocupadas. Espaço inválido no início/conclusão preserva corpo anterior e não cobra ativação; caminhada usa a posição final real. Volume de passagem inclui corpo-base na postura admitida; formas grandes continuam restringidas. Retorno não concede teleporte, destruição ou benefício não financiado. Inventário, poses, intervenções do mundo e feedback ainda dependem de ficha antes da validação executável. Nenhum desses cenários foi executado em Minecraft.

**Resurrección — critérios documentais de K/L/M aprovados; validação futura ainda não executada:** liberar/encerrar em chão, ar e água válidos sem apagar gravidade, trajetória, queda ou riscos. Guardar a arma separada no próprio inventário conserva a forma paga, suspendendo dependências de empunhadura; retomar não cobra nova liberação. Perder a posse encerra a manifestação sem transferir poder ou recolher/duplicar item; recuperar exige novo pedido válido e pago. Arma incorporada não deixa cópia transferível; dano corporal não vira perda de item por omissão. Fases, custos, efeitos autônomos e tempos anteriores permanecem. Poses, representação de inventário/perda/retorno e efeitos dependentes exigem ficha antes da validação executável.

**Resurrección — critérios documentais de O/P aprovados; validação futura ainda não executada:** apenas transformar não encerra Supressão nem esconde corpo/arma; início ofensivo válido encerra Supressão, pedido recusado a preserva se válida. Resurrección e Liberação mantêm custos próprios. Na coincidência, pagar forma → Liberação → ação; estado sem pagamento termina, e o outro ainda válido é tentado. Revalidar requisitos da ação depois de perder a forma; sem reativação automática ou benefício não pago. Custos iniciais continuam na conclusão e não viram manutenção antecipada. N complementa equipamento sem alterar a elegibilidade e prioridade aprovadas em O/P.

**Resurrección — critérios documentais de N aprovados; validação futura ainda não executada:** peças legitimamente equipadas nos quatro slots normais continuam protegendo com encantamentos elegíveis e desgaste legítimo, mesmo com aparência adaptada/oculta. Transformar não duplica/repara itens ou cria outra camada do mesmo equipamento. Remover/quebrar uma peça retira sua contribuição; Hierro e demais proteções seguem M06-11A/B. Fichas de itens, cálculos e apresentação ainda precisam de especificação e validação; nenhuma evidência de gameplay foi produzida.

**Q — critérios documentais aprovados; validação futura ainda não executada:** interrupção encerra preparo/forma sem retomada; conquista e condição permanecem; retorno exige liberação manual paga. Confrontar arma incorporada/separada, inventário cheio e itens na morte pelas futuras fichas M11/M16. Os efeitos da tabela da seção 12.1 já seguem a herança aprovada, mas cada técnica ainda precisa de classificação executável. Nenhum cenário foi executado.

**M07-07A/B — critérios documentais aprovados; validação futura ainda não executada:** caminho de Vollständig sem uso prévio de Letzt Stil; registros distintos das conquistas; número ou afiliação sem aprendizado não substituem demonstração; primeira execução possível na aprendizagem e conclusão não duplicada pela quest. Consequência de Letzt Stil, acesso institucional/Schrift e regras operacionais ainda não foram decididos. Nenhum cenário foi executado.

**M07-07C/D — critérios documentais aprovados; validação futura ainda não executada:** Schrift compatível conquistada não entrega forma automaticamente; primeira aprendizagem não combina identidades; ensino exige fonte pertinente e demonstração. Afiliação sozinha não concede nem apaga aprendizagem; uma mudança de serviço e uma privação efetiva de poder são eventos distintos. Fonte concreta, contextos de aquisição e requisitos operacionais ainda precisam de ficha. Nenhum cenário foi executado.

**M07-07E — critérios documentais aprovados:** uma forma conquistada e com requisitos válidos não falha por sorteio de baixo domínio; falta de requisito e interrupção continuam causas distintas. Primeira manifestação não exige Mastery anterior da forma; técnicas/Proficiências conservam seus históricos, sem cópia de progresso. Manter a forma AFK ou alterná-la sem prática pertinente não desenvolve domínio. Práticas, curvas e execução ainda precisam de ficha. Nenhum cenário foi executado.

**M07-07F/G — critérios documentais aprovados:** pedido manual válido pode iniciar dentro/fora de Combat Mode; fechar interface não encerra a forma ou suspende seus custos, mas arco/Blut seguem desligamento próprio. Cancelamento anterior à conclusão não cobra ativação; conclusão sem Energia suficiente não manifesta nem cobra; falta de manutenção encerra sem apagar conquistas. Não repor recursos ou resetar tempos ao alternar. Compatibilidades, ordem de pagamentos, instrumentos e retorno ainda precisam de contrato. Nenhum cenário foi executado.

**M07-07H/I — critérios documentais aprovados:** forma conquistada sem instrumento próprio obrigatório pode iniciar/continuar sem foco do arco, respeitados os demais requisitos; arco básico sem foco continua indisponível. Guardar arco manualmente permite item compatível durante a forma paga, quando a fase permitir; não usar item simultâneo com arco de duas mãos ou apagar tempos na troca. Exceção instrumental/corporal exige ficha própria; não conceder luva, arma, armadura funcional universal ou gesto gratuito por aparência. Nenhum cenário foi executado.

**M07-07J/K/L — critérios documentais aprovados:** reação disponível cancela preparo antes de começar e reação recusada conserva apenas preparo ainda válido; cancelamento anterior não cobra ativação, sem simultaneidade com tiro/item/canal. Encerrar voluntariamente respeita fase e conserva conquistas/Schrift; sem manutenção paga não conserva a forma. Conclusão/saída não acrescentam recuperação, mas preservam tempos anteriores. Corpo/arma/efeitos dependentes precisam de ficha, autônomos seguem M06-11W. Nenhum cenário foi executado.

**M07-07M/N/O — critérios documentais aprovados:** uma aplicação conhecida de Blut junto da forma, nunca Vene/Arterie simultâneos; presença escolhida conserva sinais/exclusividades e ofensiva válida encerra Supressão. Manutenções realmente devidas em Vollständig → Blut → Liberação → ação, sem antecipar/duplicar/pagar parcialmente. Falha de uma cobrança encerra esse estado e ainda permite tentar os demais válidos; ação revalidada após perda de forma. Ativação inicial paga como ação ao concluir, depois de obrigações já devidas. Nenhum cenário foi executado.

**M07-07P/Q/R — critérios documentais aprovados; validação futura ainda não executada:** manifestar/encerrar no chão/ar/água sem apagar gravidade, trajetória, queda e riscos; capacidade de voo segue sua ficha. Espaço inválido no início/conclusão conserva corpo anterior e não cobra ativação; adornos visuais não viram automaticamente colisão ou alcance. Peças equipadas continuam funcionais com desgaste/encantamentos elegíveis, sem cópia ou reparo; remover/quebrar retira a contribuição. G02 aprovado resolve a política geral de passagem/retorno; medidas/poses e tratamento das intervenções forçadas do mundo ainda precisam de ficha antes da entrega. Nenhum cenário foi executado em Minecraft.

**M07-G01/G02 — critérios documentais aprovados; validação futura ainda não executada:** um perfil temporário coberto encerra no ciclo de vida sem perder conquista ou reiniciar gastos/tempos; condição permanente permanece. Retorno usa o estado-base do perfil, distinguindo Bankai no ciclo de vida de Bankai sem manutenção. Passagem usa corpo liberado e base na postura comum, sem alterar hitbox de combate ou bloquear por halo visual. Alterações forçadas do mundo e armas/inventário continuam dependências explícitas. Conferência futura deve percorrer a matriz de perfis e exceções, sem duplicar a aprovação por raça. Nenhum cenário foi executado.

### Critérios conceituais de Letzt Stil — M07-08A/B/C aprovados

Nenhum cenário executado. A ficha deverá permitir observar os contratos aprovados: item/Proficiência isolados não ensinam; retirar luva normalmente não ativa; cancelamento ou invalidação anterior à conclusão não cobra nem assume perda; conclusão válida cobra e registra compromisso uma vez; duração não se renova por recurso/item/UI; não sobrepor Vollständig; toda saída posterior efetiva suspensão uma vez, inclusive eventos G01; Gintō preparado não contorna o bloqueio; conquistas e itens reais permanecem; sobrevivência vanilla continua; recuperação é acessível sem técnicas suspensas/outro jogador; falhas preservam marcos; completar recuperação não restaura recursos/formas ou prontidão Sanrei. Verificar colisões/armadura/tempos/efeitos por referência aos contratos, sem nova aprovação genérica. M16 detalhará ordem de evento e evidência antes da entrega.

### Critérios conceituais Fullbringer — M07-09A/B aprovados

Nenhum cenário executado. Para A/B aprovados: aquisição/prática sem requisito circular; poder canônico e fundamentos distintos; não inventar forma incompleta/intermediária para completar uma lista; completude registrada por demonstração/domínio, sem Mastery máxima universal ou barra nova por rótulo; técnica aprendida válida sem falha aleatória genérica; manifestação básica cobra uma vez ao concluir e não tem manutenção básica; ações/efeitos sustentados pagam custos próprios; poder condicional não vira forma global; toggles/guardar/sessão não concedem treino, recursos ou reset. Vetor corporal não cai como item; transferência de item não ensina poder; G01/G02 preservam conquistas e retorno pertinentes; espaço contendo participantes/itens exige tratamento próprio antes da entrega. C define a política de troca em 8.95; sua ficha e os casos de perda/reposição ainda são necessários antes de implementar. Não aprovar solução concreta por omissão.

**Critérios conceituais de C, ainda sem execução:** pedido por item/menu não troca principal; tentativa inválida/interrompida preserva o anterior ainda válido; conclusão autoriza exatamente um destino e desliga dependentes do antigo, sem refill/reset; histórico não dá acesso simultâneo nem Mastery ao novo; reacesso contextual recupera somente progresso próprio compatível; fundamentos permanecem; efeitos longos e recipientes/participantes não são apagados ou abandonados por omissão. A ficha executável precisa definir eventos concorrentes e gastos/etapas concretos antes da validação.

## 14. Impacto técnico conhecido

Checkout confirmado nesta abertura: `main`, HEAD `8ace6520e67d27441974b9ad92b443ddb33f9882`, com alterações documentais locais preservadas. Reconfirmados em `gradle.properties`/`build.gradle`: Minecraft 1.20.1, Forge 47.4.10 e toolchain Java 17. Não foi alterado o ambiente de execução.

A auditoria anterior, [registrada na base](00-base-conhecimento-minecraft-bleach.md), localizou formas/técnicas Shinigami e reset de estado/cooldowns, sem demonstrar M07 completo. O retorno selado encontrado no código após clone/respawn não é nova aprovação da política de formas. A ausência de persistência dos cooldowns identificada em M06 continua divergência de M06-11V.

Esta abertura realizou leitura documental dirigida e consulta de glossários/resumo oficiais. Não realizou auditoria integral de runtime, migração, build ou teste de Minecraft. Nenhum resultado histórico foi tratado como nova execução.

## 15. Próximos passos

1. Avançar para M08 — NPCs, mentores e facções, usando as necessidades de ensino/prova/acesso/recuperação de M04–M07 sem inventar que todo canônico é um professor disponível.
2. Manter os contratos comuns/perfis de 7.1/7.2, G01/G02 e decisões de 8 como fonte; novas fichas referenciam o aprovado e explicitam somente diferenças ou exceções reais.
3. Completar as entregas de 12.2 nos módulos responsáveis. Sanrei/recuperação, corpo/retorno, itens e ordenação de efeitos precisam de ficha antes da entrega pertinente; não reabrir perguntas gerais para preencher parâmetros.
4. M16 organizará fatias jogáveis e validação quando chegar sua etapa. Catálogo integral, Grimmjow/linhagens, artes, implementação/teste/aceite permanecem nas etapas próprias.

### Fechamento conceitual de M07 e revisão comum — 22/10/2026

**Estado:** ESPECIFICADO CONCEITUALMENTE. Os blocos básicos de Shikai, Bankai, evolução Hollow, Arrancarização, Resurrección, Vollständig, Letzt Stil e Fullbringer têm decisões estruturais registradas; C conclui o recorte Fullbringer. Revisão documental dirigida não identificou conflito impeditivo entre os contratos examinados. Nenhuma nova aprovação de produto é presumida, nenhum cenário foi executado e nenhuma implementação/teste/aceite foi avançado. O fechamento cobre a arquitetura dos perfis e suas relações; as entregas de 12.2 impedem tratá-la como especificação executável de todos os poderes.

| Eixo revisto | Resultado conceitual |
|---|---|
| Condição, conquista, forma e domínio | Estados distintos de C01/7; estágio Hollow/condição Arrancar permanecem, formas temporárias encerram por G01, completude Fullbring é marco. Perder disponibilidade não apaga história por omissão |
| Aquisição e treino | Conteúdo/prática coerentes, provas registradas sem repetir a cada uso; aprendizagem inicial possível sem Mastery inacessível. Fundamentos, técnicas e domínio não comprados por uma taxa universal; sem AFK/toggle como fonte de progresso |
| Ativar, reagir, usar interface e encerrar | C02–C09 e perfis mantêm condições/fases, reações válidas/recusadas e custos/tempos anteriores. Liberações básicas cobertas usam preparo cancelável; passagens permanentes mantêm preparo próprio parado/apoiado/fora de combate. Não copiar regra de uma para outra |
| Economia de cada perfil | Shikai e manifestação básica Fullbring pertinente: ativação sem manutenção básica. Bankai/Resurrección/Vollständig: ativação + manutenção. Letzt Stil: ativação + janela máxima e consequência. Evolução/Arrancarização: sem taxa/manutenção adicional; nenhum refill por mudança |
| Prioridades de cobranças já aprovadas | M06-11T: Blut → Liberação → ação; com Bankai/Resurrección, forma → Liberação → ação; Vollständig → Blut → Liberação → ação. Sem manutenção básica de Shikai/Fullbring/Letzt Stil, não inventar uma cobrança para preencher a fila. Cobranças realmente devidas antes da ação, sem prepagamento/débito parcial, revalidando dependências; estados independentes ainda são tentados quando outro termina |
| Efeitos específicos ainda futuros | Não foi aprovada nova ordem total universal ou compatibilidade de todas as técnicas. Cada ficha sustentada declara sua posição/dependências antes da entrega e respeita as relações já aprovadas. Alteração de experiência ou combinação não coberta requer decisão própria; pendência tem responsáveis em 12.2 |
| Coexistência | Perfis aprovados convivem com um estado de Reiatsu válido; apenas manifestar não é ofensiva automática e ofensiva válida encerra Supressão. Um Blut aprendido quando compatível, nunca Vene/Arterie juntos. Letzt Stil não sobrepõe Vollständig. Isso não autoriza híbridos, várias identidades principais ou toda combinação de formas |
| Corpo, vetor e equipamentos | Vetor conforme poder; guardar/empunhar/perder afeta dependências reais. G02 preserva retorno corporal normal; não cria voo/respiração ou resolve intervenções forçadas. Armadura tem cobertura declarada em C11, sem conceder por associação todos os slots a todo corpo permanente Hollow |
| Sessão, clocks e efeitos | G01/C12 e M06-11U/V/W preservam conquistas/tempos e distinguem autônomos/dependentes/persistentes. World clock não dá treino/regeneração offline. Evento de saída não é cura de consequência especial, coleta de projétil ou replay após reinício |
| Consequências e retorno | Bankai quebrado mantém limite até recuperação específica; Letzt Stil suspende acesso Quincy até procedimento próprio; falhas comuns não apagam evolução Hollow. Risco só entra com recuperação/retorno viável. Troca Fullbring preserva domínio por poder indisponível, sem transferir ou combinar |

**Critério de conclusão:** não resta pergunta geral identificada que impeça avançar a M08; conteúdo, números, detalhes de estado/itens/corpos e integração estão atribuídos em 12.2 e nas revisões por caminho. Isso não promete ausência absoluta de omissões, exame exaustivo do cânone/código ou que exceções futuras estejam prontas. Também não elimina técnicas de voo, Sklaverei, novas formas ou híbridos do horizonte: precisam de fonte, ficha e decisão próprias quando entrarem no escopo.

**Retornar a M07 se:** fonte pertinente contrariar o perfil; primeiro uso/recuperação ficar circular ou inviável; corpo/efeito/objeto exigir exceção com nova consequência; a ordem de cobranças mudar experiência não aprovada; retorno provocar prisão/duplicação/perda de progresso; ou validação revelar reset, exploração ou combinação indevida. Identificar o contrato e sua revisão; não repetir todas as decisões por raça.

### Fechamento estrutural Fullbringer A–C — 22/10/2026

**Resultado:** A/B/C decididos em 8.93–8.95, sem perguntas pendentes; Fullbringer consolidado estruturalmente. C: “Troca contextual, preservando domínio por poder (recomendado)”. Conferidos desenvolvimento, economia, vetor, domínio, principal único, cancelamento e retorno contra F2/F3/F4/F6/F12/F13, M06-10, C01–C12 e G01/G02. Não identificado conflito no recorte examinado; nenhum cenário executado.

Troca válida faz o destino ser o único principal, encerra benefícios dependentes do anterior e preserva seu histórico indisponível; não copia Mastery, restaura recursos ou permite trocar só por item/menu. Falha antes da conclusão preserva o principal anterior ainda válido; reacesso exige processo próprio, sem repetir automaticamente toda campanha inicial. Fundamentos/atributos permanecem. Nova aquisição, despesas/contexto, vetor/slots/perda/reposição, conteúdo de reacesso e efeitos persistentes continuam com as fichas de 12.2. A revisão anterior A/B abaixo é complementada por C, sem reabrir o que já foi decidido.

### Revisão Fullbringer A/B e do vetor — 22/10/2026

**Resultado:** A/B aprovados por respostas próprias, sem perguntas pendentes desses registros. Conferidos contra F2/F3/F4/F6/F12/F13, M06-10, C01–C12 e G01/G02 nos escopos declarados. Não identificado conflito no recorte examinado. C foi aprovado depois e a revisão abaixo consolida A–C/Fullbringer e o nível conceitual de M07; prioridades de efeitos sustentados e fichas concretas continuam com entregas próprias. Nenhum cenário executado nesta revisão documental.

| Tema revisado | Aplicação / fronteira |
|---|---|
| Completude e domínio | Marco do poder, não taxa/manutenção ou forma global. Mastery contínua no mesmo poder; técnicas distintas conservam domínio próprio. Não inventar estágio visual, comprar domínio ou exigir técnica inacessível para aprender |
| Economia e presença | Manifestação básica pertinente paga uma vez na conclusão, sem manutenção básica; ações/canais/benefícios sustentados têm ficha própria. Sem reset/refill, falha aleatória universal ou conversão da ausência de manutenção em efeitos ilimitados. Perfil operacional B adotado; presença e armadura seguem sua cobertura |
| Vetor portátil, equipamento ou roupa | A ficha declara disponibilidade: empunhado, equipado, na posse ou outra condição justificada. Guardar/trocar ferramenta não equivale a transferir posse. Revalidar apenas dependências reais; não manter técnica que exige empunhadura por segurar outro item |
| Vetor corporal | Não criar exigência de item-foco, retirada do braço pelo inventário ou perda de conhecimento por equipamento quebrado. Requisitos corporais/condições seguem a capacidade real, sem mutilação universal ou regeneração concedida por transformar |
| Perda/destruição/reposição | Vínculo/aprendizado não passa ao destinatário; dependentes ficam inválidos quando perdem requisito. Fundamentos independentes e domínio não são apagados. M04 já exige recuperação do objeto; M11/M16 devem entregar rota viável e tratamento de item único/inventário/slots, sem cópia, recall/reparo automático ou keepInventory presumido |
| Sessão, forma e efeitos | G01 encerra manifestações/preparos temporários e preserva poder/completude; efeitos persistentes seguem sua própria classificação. G02 trata corpo/retorno pertinente. Espaço contendo participantes/itens precisa de saída própria antes da entrega, sem reset de mundo ou cura de efeitos por fechar interface |
| Troca do principal | Não decorre de perder/equipar vetor; exige escolha de produto além de A/B. C aprovado em 8.95: processo contextual e histórico de domínio por poder, sem acesso simultâneo ou copiar Mastery; nenhuma pergunta pendente |

**Direção da revisão:** casos de guardar/perder/equipar não exigem repetir uma votação genérica por raça. M06/M07/M11 descrevem, em cada ficha, o requisito real e seu alcance; se houver exceção nova ou consequência de produto não coberta, ela volta com escopo explícito. Custos, materiais, rito/reposição, ordenação específica e efeito longo ainda precisam de fichas. C já define a política de troca; seu conteúdo/realização e o fechamento constam acima. Não usar esta revisão para declarar todos os vetores implementáveis.

### Revisão estrutural de Letzt Stil e das formas Quincy — 22/10/2026

**Resultado:** M07-08A/B/C aprovado por três respostas explícitas, sem perguntas pendentes. Confrontado com M07-07A–R, G01/G02, contratos C01–C12 e regras de M06 citadas nas fichas. Não identificado conflito no recorte examinado. Vollständig e Letzt Stil estão consolidados estruturalmente como formas Quincy de M07; isso não fecha todas as quests/técnicas/Schrifts, M07 inteiro ou a implementação. Nenhum cenário executado nesta revisão documental.

| Situação revisada | Resultado estrutural |
|---|---|
| Acesso e relação entre caminhos | Letzt Stil aprendido com Sanrei, sem Schrift/Vollständig/filiação permanente como requisito; não é etapa obrigatória de Vollständig. Vollständig continua exigindo Schrift compatível e aprendizado próprio. Não usar a via antiga para conceder outra identidade individual |
| Preparar e liberar | Sanrei preparada e requisitos válidos no início/conclusão; simples retirada no inventário não ativa. Preparo cancelado/inválido não cobra nem assume perda. Conclusão válida cobra Energia e inicia janela limitada/compromisso uma vez; sem manutenção básica ou reposição de recursos |
| Ações, reforços e forma alternativa | Contratos comuns cobrem reações, interface, mãos, armadura, espaço/meio e preservação de tempos. Uma aplicação de Blut/um estado de Reiatsu quando válidos, com seus próprios custos. Não coexistir com Vollständig; M06-11T governa manutenção devida durante Letzt Stil |
| Terminar ou sofrer evento G01 | Antes da conclusão: termina só a tentativa. Depois dela: qualquer término encerra benefício e efetiva perda de C uma vez. Morte, logout, dimensão/reinício não apagam o compromisso, curam a suspensão ou permitem nova forma. Conquista e capacidade disponível são registros distintos |
| Período sem capacidades Quincy | Bloquear execuções Quincy, inclusive Gintō com conteúdo prévio; preservar itens/progresso e vida cotidiana Minecraft. Canais dependentes terminam; efeitos autônomos/condições/montagens seguem C12/M06-11W. Capacidades comuns dependem de sua validade real, sem novos poderes ou bloqueio racial total por omissão |
| Recuperar e preparar outro uso | Via própria conhecida antes do risco, viável sem técnicas suspensas/outro jogador; marcos concluídos persistem, falha limitada à tentativa. Conclusão recupera acesso ao aprendido/compatível, sem refill, reativação automática ou domínio gratuito. Novo uso requer prontidão Sanrei refeita |

**Distinções preservadas:** Vollständig tem ativação + manutenção sem duração fixa universal e retorna Quincy sem forma; Letzt Stil tem ativação + janela máxima sem manutenção básica e retorna Quincy com acesso suspenso até recuperação. Não transportar perda de Letzt Stil para Vollständig nem manutenção de Vollständig para Letzt Stil. Energia zero e indisponibilidade Quincy são causas diferentes. G01 limpa manifestações, não consequências persistentes; G02 mantém seu escopo espacial. Nenhuma decisão de raça permanente, atributos, foco do arco ou multiplicidade do poder principal foi revogada.

| Entrega ainda necessária antes de usar esse risco em gameplay | Responsáveis / condição de entrega |
|---|---|
| Fonte, treino e primeira preparação Sanrei; via concreta de recuperação e repetição | M07/M08/M12/M13, com era/acesso e sem requisito circular. Recuperação disponível antes de autorizar uso de risco; M10/M15 verificam acesso e duração proporcional, inclusive solo/co-op |
| Objeto Sanrei: equipar, prontidão, recondicionamento/consumo, integridade, perda/reposição e slots junto do foco | M07/M11/M16. Recuperar não rearma o uso; não assumir destruição, receita, reparo, cópia ou retorno de item sem ficha. Resolver no domínio de itens, sem nova série de perguntas comuns |
| Duração máxima, custo inicial, benefícios e interação com recursos/máximos, práticas de domínio e capacidades comuns durante suspensão | M05/M06/M07/M15. Benefício suficiente para justificar o risco, sem superar toda Vollständig por definição, preencher recursos ou criar treino AFK. Nenhum valor/curva foi escolhido |
| Corpo/poses e partes funcionais; retorno em mudanças forçadas do mundo | M06/M07/M10/M16, sob C10/G02. Não conceder voo por aparência, teleporte corretivo ou remoção de blocos |
| Feedback de risco/tempo/perda/recuperação; dados e eventos concorrentes | M14/M16. Autoridade do servidor, conclusão/compromisso/saída/recuperação idempotentes; nova ativação bloqueada até cumprir requisitos. Timers, efeitos, saves antigos e reinício precisam de evidência futura |

**Condição de reabertura:** fonte pertinente contrariar o perfil, benefício/custo tornar a escolha inviável, recuperação ficar inacessível/circular, Sanrei/item contornar prontidão, ou evidência revelar perda de progresso/duplicação/cura indevida por sessão. Novos números, quests e representação técnica não reabrem automaticamente os contratos gerais. Próximo Fullbringer por diferenças; depois revisão comum de M07. A revisão é documental e dirigida, sem comprovação do comportamento do mod.

### Revisão estrutural de Vollständig — 22/10/2026

**Resultado:** subbloco Vollständig consolidado estruturalmente: dezoito decisões específicas M07-07A–R e adoção dos dois padrões gerais G01/G02, sem perguntas pendentes desse conjunto. A contagem não converte os padrões gerais em novas decisões raciais duplicadas. Letzt Stil foi complementado por A–C e revisado junto das formas Quincy na revisão acima; M07 ainda precisa do bloco Fullbringer e da revisão comum. Este fechamento não constitui ficha executável, implementação, teste ou aceite.

| Situação revisada | Contrato vigente / resultado da conferência documental |
|---|---|
| Conquista e primeiro uso | Schrift compatível e fonte avançada com preparo/demonstração; sem filiação permanente, Mastery prévia circular ou sorteio de falha. Letzt Stil não é pré-requisito. A–E e C01/C02 |
| Ativar, reagir e pagar | Pedido manual independente da interface; preparo cancelável sem ações paralelas; Energia na conclusão e manutenção. Vollständig → Blut → Liberação → ação nas cobranças coincidentes; revalidar a ação após perda de requisito. F/G/J/L/M/N/O e C03–C08 |
| Instrumentos e equipamentos | Sem foco universal da forma; arco mantém foco/duas mãos. Item ou arma específica declara quais partes dependem dele; guardar/perder um item não encerra uma forma que não o exige. Mesmos itens de armadura/desgaste, sem cópia/reparo. H/I/R e C02/C09/C11 |
| Corpo, espaço e saída | Chão/ar/água com gravidade/queda/riscos; espaço válido no início/conclusão e passagem compatível com base por G02. Encerramento voluntário ou sem manutenção retorna Quincy sem forma, respeitando fases e retirando benefícios dependentes. K/P/Q e C10 |
| Morte, logout, dimensão e reinício | G01 encerra preparo/forma, preserva condição Quincy, Schrift/conquistas/Proficiências/Masteries e exige retorno manual pago. Sem cobrança de preparo incompleto, manutenção após encerrar ou restauração automática. Itens/respawn seguem fichas próprias |
| Efeitos e tempos | C05/C12 e M06-11V/W: despesas/tempos preservados, ataques autônomos e efeitos dependentes classificados, sem apagar impactos/condições independentes. Não duplicar conclusão e evento de saída; M16 detalha a ordenação |

**Conferência:** não identificado conflito entre os contratos examinados. Os padrões gerais complementam a política, sem alterar A–R, exclusividade de Blut/presença, mão do arco ou a natureza permanente das evoluções. Não foi aprovada invulnerabilidade, voo universal, absorção de seres/blocos, novo poder individual, taxa numérica ou reposição de recurso/item.

| Entrega executável ainda pendente | Responsáveis e motivo |
|---|---|
| Fontes/quest/ensino e primeira demonstração concretos; forma e repertório inicial do mesmo poder | M07/M08/M12/M13, respeitando era/acesso e um poder principal. Práticas e Masteries das técnicas em M06/M07 |
| Valores de ativação/manutenção, intervalos, influência ambiental e conversão de máximos; viabilidade com arco/Blut | M05/M06/M07/M15, sem inventar números ou garantir abastecimento ilimitado |
| Perfis, medidas/poses, partes funcionais e acertos; retorno diante de mudanças forçadas do mundo | M06/M07/M10/M16. G02 resolve o padrão espacial normal; alterações externas precisam de tratamento antes da entrega |
| Instrumentos/armamento específico, perda/reposição, inventário cheio e morte; atualização real de armadura | M07/M11/M16, conservando identidade/posse/desgaste sem cópias e sem escolher keepInventory por omissão |
| Capacidades de voo, Sklaverei, versões e exceções canônicas | M06/M07 com pesquisa/ficha próprias; não são concedidas nem excluídas universalmente pelo fechamento básico |
| Controles e sinais para jogador/observadores; dados, migração e validação executável | M14/M16. Servidor é autoridade também no singleplayer; cenários ainda não executados |

**Retornar a este subbloco se:** uma fonte pertinente contrariar o perfil; um corpo/instrumento exigir exceção não coberta; a ficha tornar primeiro uso inviável; houver duplicação/reset de recursos/itens/tempos; ou a validação revelar problema de retorno/combate. Novos valores, conteúdo e realização técnica não reabrem por si só todas as perguntas gerais.


**Histórico de revisão:** os registros abaixo preservam o escopo de cada rodada. As consolidações posteriores governam seus respectivos blocos; pendências resolvidas depois não voltam a ser abertas por aparecerem no histórico.

### Revisão da abertura A–D — 21/10/2026

A–D foram confrontados com M04 (conquista separada da Zanpakutō/Shikai/Bankai), revisões de M05 (pontos, recursos e Mastery) e M06 (vínculo, fases, modo, custos e tempos). Não foi identificado conflito que exija revogar as decisões examinadas. B não revoga o desligamento de arco/Blut; C não torna técnicas gratuitas; D não entrega Bankai ou todas as técnicas. Nenhuma compatibilidade com Supressão/Liberação foi aprovada por associação.

A leitura foi dirigida aos contratos necessários à abertura; não é revisão integral dos módulos 04–06 ou de todo o cânone. A fonte sustenta a identidade do aprendizado, enquanto prova, controles e economia são adaptações aprovadas. Valores, fases, cancelamentos, arma, corpo, ciclo de vida e integração permanecem abertos. A–D não têm perguntas pendentes; M07 continua `EM ESPECIFICAÇÃO`.

### Revisão de E/F/G — 21/10/2026

E/F/G aprovados por três respostas próprias. Confrontados com A–D, M06-02S/T, M06-05, M06-07V–Y/HD–HF e M06-11V/W/X/Y. Não foi identificado conflito que exija revogar essas escolhas: F distingue empunhadura e posse, G preserva o vínculo, E preserva custo somente na conclusão e reação válida. F não amplia o repertório de técnicas desarmadas de cada poder.

Reconsultado BL-12 em 21/10/2026, entradas Shikai/Zanpakutō. Não define preparo, controles, armazenamento, conservação da manifestação no inventário ou encerramento por transferência; E/F/G são adaptações aprovadas de gameplay. Sem capítulo/episódio integral ou nova consulta de APIs. Sem implementação, build, migração ou teste de Minecraft. Duração, conclusão/selamento, presença e ciclo de vida continuam abertos.

### Revisão de H/I/J e consolidação do primeiro Shikai — 21/10/2026

H/I/J aprovados por três respostas explícitas. A–J estão decididos no recorte básico de M07-02. A revisão dirigida não identificou conflito que impeça avançar para Bankai. Isso não fecha M07 nem declara todas as Zanpakutō especificadas: o recorte consolidado é conquista/manifestação básica de Shikai, com aquisição, domínio, custo, preparo, reação, empunhadura/posse, conclusão/selamento, presença e ciclo de vida.

| Situação consolidada | Resultado conceitual |
|---|---|
| Ainda não conquistou Shikai | Arma/vínculo ou Proficiência não substituem prova de A |
| Liberação iniciada | Preparo de E, sem benefícios antecipados; recurso cobrado somente na conclusão válida |
| Reação recusada durante preparo | Preservar preparo ainda válido, sem renovação ou fila |
| Reação disponível durante preparo | Cancelar antes da reação, sem custo de liberação ou recuperação extra pelo cancelamento |
| Liberação concluída | Uma cobrança; manifestação básica sem manutenção; ações disponíveis conforme H |
| Ferramenta selecionada, espada na própria posse | Conservar manifestação; suspender capacidades que exigem empunhadura, sem transformar ferramenta em Zanpakutō |
| Arma sai da posse | Encerrar manifestação e dependentes; vínculo/domínio permanecem; efeitos autônomos seguem W |
| Arma recuperada após perda de posse | Sem restauração automática; nova liberação válida paga C |
| Selamento manual permitido | Encerramento direto, sem estorno ou recuperação extra; efeitos seguem dependências da própria ficha |
| Supressão/Liberação compatível | J preserva custos/sinal e início ofensivo de M06; forma da arma não esconde sua aparência |
| Morte/logout/dimensão/reinício | Encerrar Shikai; conquistas e tempos restantes persistem conforme seus contratos, sem retorno automático |

**Pendências e responsáveis:** M05/M15 calibram custo e duração do preparo/curvas de domínio; M06/M07 definem técnicas, efeitos, mudanças de capacidade e exceções por Zanpakutō; M08/M12/M13 detalham orientação/prova e retomada; M11 resolve armazenamento, slots, perda/reposição e representação de armas particulares; M14 define comandos/feedback; M16 especifica dados, migração, sincronização e execução validada. Esses itens são necessários antes de implementação/aceite da respectiva ficha, sem impedir a discussão do sistema de Bankai. Não criar bônus universal, recarga de Vida/Energia ou progressão por alternância para preencher as lacunas.

**Critérios futuros adicionais:** conclusão respeita recuperações antigas; selar não serve de cancelamento de ataque; mudança de interface não sela; morte/logout/dimensão/reinício não restauram Shikai; Supressão não torna arma invisível e uma ofensiva válida a encerra; manutenção de Reiatsu e custo de liberação respeitam a ordem de M06-11T. Nenhum desses cenários foi executado como teste nesta rodada.

Sem nova pesquisa canônica entre proposta e aprovação de H/I/J, nem consulta de APIs, alteração de runtime, build, dependências, saves, commit/push ou teste de Minecraft. Próximo: **M07-03 — Bankai**, preservando as dez decisões de Shikai.

### Abertura de M07-03 — Bankai — 21/10/2026

A/B preparados como propostas, sem aprovação presumida pela continuidade. Confrontados com aquisição separada em M04, aplicação Shinigami, revisões REV-M02-02–05 e Mastery em M05, e A–J do primeiro Shikai. Não foi identificado conflito nesses contratos que impeça apresentar as alternativas. Requisitos concretos de preparação e provas ainda precisam ser especificados; não foram inventados números, técnicas, benefícios ou conteúdo de campanha para preencher lacunas.

**Critérios candidatos, condicionados à aprovação:** obter Shikai ou maximizar um indicador sozinho não concede Bankai; a prova registra somente a conquista pessoal válida; não exigir Mastery prévia de Bankai para tentar aprendê-lo; depois da conquista, requisitos válidos permitem manifestação básica, sem entregar todas as técnicas. São critérios documentais propostos, não testes executados.

Revisão documental e pesquisa de resumos oficiais; sem mudança de runtime, dependências, saves, commit/push ou teste de Minecraft. Próximo: resolver M07-03A/B e então preparar ativação, economia e transições próprias de Bankai. A–J de Shikai permanecem vigentes.

### Aprovação de Bankai A/B e preparação de C/D — 21/10/2026

Duas respostas explícitas registradas nas seções 8.12/8.13: A — “Relação avançada + prova com o espírito (recomendado)”; B — “Manifestação confiável + domínio próprio (recomendado)”. A/B não têm perguntas pendentes. Preservadas conquistas separadas, prática apropriada e limites por Zanpakutō; não foi identificado conflito no confronto com M04/Shinigami, M05/Mastery/REV-M02 e Shikai A–J. O resultado não fecha Bankai nem M07.

**Critérios documentais aprovados:** Shikai ou indicador isolado não concedem Bankai; preparo e prova pessoal são necessários; não exigir Mastery de Bankai antes do aprendizado; conquista registrada permite manifestação básica com requisitos válidos, sem roleta ou concessão de todo o repertório. Critérios futuros: verificar preparação insuficiente, prova concluída, autorização pessoal em party, requisitos recusados e desenvolvimento separado de Shikai/Bankai. Nenhum cenário foi executado.

C/D preparados como propostas de ativação direta/manual e economia com ativação/manutenção. Reconsultada BL-03, entrada Bankai, sobre sustentação espiritual; sem capítulos/episódios integrais ou importação das demais entradas. Não é evidência de teclas, taxas ou compatibilidades. Sem mudança de runtime, build, dependências, saves, commit/push ou testes de Minecraft. Próximo: respostas C/D, depois fases/reação, reversão e demais integrações.

### Aprovação de Bankai C/D e preparação de E/F — 21/10/2026

Duas respostas explícitas: C — “Bankai manual e direto (recomendado)”; D — “Ativação + manutenção de Energia (recomendado)”. Registradas nas seções 8.14/8.15. A–D de Bankai estão decididos no escopo descrito, sem pergunta pendente. A ativação pode partir de selado/Shikai com arma compatível empunhada, dentro/fora da interface; a economia cobra conclusão válida e sustentação, sem duração fixa universal. Nenhuma transição interrompida, retorno a Shikai ou recuperação adicional foi decidida por associação.

Revisão dirigida de A–D e dos contratos vigentes de Shikai, custos, fases e efeitos não identificou conflito que exija revogação. Preparo/interrupção e estado resultante ainda requerem decisão: E/F são propostas próprias, não regras herdadas automaticamente. Critérios futuros de C/D: liberação direta sem cobrança intermediária de Shikai; fechar interface não encerra Bankai nem suspende manutenção; cancelamento anterior não paga ativação; falha de manutenção encerra Bankai sem reativar sozinho; não renovar período gratuito por alternância. Nenhum cenário foi executado.

Sem nova pesquisa canônica/API nesta atualização; as fontes anteriores conservam seus limites. E/F tratam adaptações de gameplay, sem alegar regra canônica universal. Sem alteração de runtime, build, dependências, saves, commit/push ou teste de Minecraft. Próximo: respostas E/F, depois conclusão/encerramento, posse, Reiatsu, concorrência e ciclo de vida.

### Aprovação de Bankai E/F e preparação de G/H/I — 21/10/2026

Duas respostas explícitas: E — “Preparo cancelável para reagir (recomendado)”; F — “Retorno a Shikai sem nova cobrança (recomendado)”. Registradas nas seções 8.16/8.17. Bankai A–F sem perguntas pendentes no recorte aprovado. Cancelar antes da conclusão preserva apenas o estado anterior válido; retornar após Bankai efetivamente estabelecido aplica F e não constitui liberação inicial gratuita de Shikai. A falta de Energia encerra Bankai sem invalidar por si só o Shikai básico, que não tem manutenção.

Revisão dirigida com Shikai C/E/F/G/I, Bankai A–D e os contratos de custo/fases/tempos não identificou conflito que exija revogação. F não restaura Shikai após perda de posse ou evento de ciclo de vida que o encerrou. Não autoriza conservar Bankai guardado, eliminar recuperação posterior ou combinar estados de Reiatsu sem decisões próprias.

**Critérios futuros de E/F:** reação disponível cancela preparo sem ativação; reação recusada preserva preparo válido sem renová-lo; tentativa a partir de selado cancelada não concede Shikai; saída válida de Bankai permite Shikai sem nova cobrança/preparo; falta de manutenção não permite benefício gratuito; custos e tempos antigos permanecem. Nenhum cenário foi executado. G/H/I são propostas próprias para conclusão/posse, aguardando respostas. Sem nova pesquisa canônica/API, alteração de runtime/build/dependências/saves, commit/push ou teste de Minecraft.

### Aprovação de Bankai G/H/I e preparação de J/K/L — 21/10/2026

Três respostas explícitas: G — “Transições sem recuperação adicional (recomendado)”; H — “Conservar Bankai com manutenção ativa (recomendado)”; I — “Encerrar Bankai ao perder posse (recomendado)”. Registradas nas seções 8.18–8.20. A–I de Bankai sem perguntas pendentes. H conserva a manifestação já válida, não um preparo ou efeito incompatível, e não pausa consumo; I não restaura Shikai sem posse e não transfere conquista ao portador do item.

Confrontados G/H/I com A–F, Shikai F/G e M06-11T/U/V/W no escopo necessário à integração. Não foi identificado conflito que exija revogação. Os contratos de M06 não decidem automaticamente presença, posição de Bankai nas manutenções ou sua reversão no ciclo de vida; J/K/L permanecem propostas próprias. Critérios futuros: ações disponíveis após conclusão sem reset de tempos; guardar com manutenção e suspensão dos requisitos de empunhadura; falta de manutenção guardado aplica F; perda/recuperação física não restaura ou transfere poder; efeitos autônomos seguem W. Nenhum cenário foi executado.

Sem nova pesquisa canônica/API ou capítulo/episódio integral nesta rodada; propostas de integração são adaptações identificadas. Sem mudança de runtime, build, dependências, saves, commit/push ou teste de Minecraft. Próximo: decidir J/K/L, revisar o conjunto e pesquisar quebra/consequências de Bankai antes de avaliar fechamento; não declarar o bloco completo por resolver somente ciclo de vida.

### Aprovação de Bankai J/K/L — 21/10/2026

Três respostas explícitas: J — “Bankai compatível com os estados de Reiatsu (recomendado)”; K — “Bankai → Liberação → ação (recomendado)”; L — “Encerrar manifestações; retorno manual (recomendado)”. Registradas nas seções 8.21–8.23. A–L sem perguntas pendentes. Encaminhamentos internos resolvidos por decisões posteriores foram atualizados, preservando o histórico e as aprovações.

Revisão dirigida distingue saída por manutenção (F, Shikai quando válido), perda de posse (I, sem manifestação) e ciclo de vida (L, sem restauração de Shikai). K prioriza manutenção devida de Bankai já ativo, não o custo inicial de uma forma inexistente; M06-11T continua precedendo a ação. Se uma cobrança não puder ser paga, a outra ainda é tentada; não há pacote indivisível. J não garante ocultação nem libera qualquer efeito ofensivo sustentado com Supressão.

**Critérios futuros adicionais:** manter Bankai sem ocultar sinais; ofensiva válida rompe Supressão; débito coincidente respeita K sem antecipar períodos; falta de uma manutenção não desfaz a outra já paga; evento de ciclo de vida não aciona o retorno de F; conquistas e tempos permanecem sem restauração automática. Nenhum cenário foi executado. Quebra/consequências ainda exigem pesquisa e decisão antes de avaliar fechamento. Sem implementação, build, migração ou teste de Minecraft.

### Revisão conjunta A–L e propostas de quebra M/N — 21/10/2026

**Registro histórico anterior à aprovação de M/N; a consolidação seguinte governa o estado atual.**

Releitura dirigida do conjunto Bankai A–L e dos contratos herdados aplicáveis; não é auditoria integral de todo M04/M05/M06, catálogo ou código. Não foi identificado conflito estrutural que exija revogar A–L. A tabela resume resultados atuais sem promover M/N a decisões.

| Situação | Regra vigente e limite |
|---|---|
| Conquistar Bankai | A/B: preparo, vínculo/prova pessoal e domínio próprio; sem cargo, Mastery máxima universal de Shikai ou roleta |
| Começar a liberar | C/E: manual, arma compatível empunhada, a partir de selado/Shikai; fase de origem precisa permitir |
| Reação recusada no preparo | E: preservar preparo válido sem renovar prazo ou enfileirar ação |
| Cancelar preparo com reação válida | D/E: sem ativação/benefício de Bankai; conservar somente forma anterior válida |
| Concluir | D/G: pagamento único, manutenção própria e ações disponíveis, sem recuperação adicional |
| Guardar na própria posse | H: conservar Bankai pago; suspender dependências de empunhadura; manutenção continua |
| Falhar manutenção de Bankai | D/F/K: encerrar e retornar a Shikai quando válido, sem cobrança/preparo novos; Liberação ainda pode ser tentada |
| Encerrar voluntariamente | F/G: escolher Shikai/selado quando permitido; não cancelar ataque ou apagar tempos |
| Perder posse | I: encerrar manifestação/dependentes sem conceder poder ao novo portador; recuperar arma não restaura |
| Usar estados de Reiatsu | J/K: presença independente, sinais/custos preservados; ofensiva rompe Supressão e manutenção de Bankai precede Liberação |
| Morrer/sair/viajar/reiniciar | L: encerrar manifestações sem restaurar Shikai; conquista/Mastery/tempos preservados |
| Quebra estrutural | Ainda não decidida: M/N propostos, distintos de fim da forma, falta de Energia e durabilidade vanilla |

**Pendências e responsáveis:** M05/M15, custo, manutenção, preparo, curvas e sustentabilidade; M06/M07, efeitos por forma, dependências, condições de controle, componentes e alterações de corpo; M08/M12/M13, preparação/prova/retomada e eventual recuperação; M09/M10, encontro, terreno e espaço válido para formas grandes; M11, slots, item, posse, perda/reposição; M14, comandos, sinais e recusas; M16, dados/persistência/migração/sincronização e execução. Formas que modificam corpo, espaço, mãos ou técnica precisam completar seus contratos; a base não concede destruição de blocos, voo ou transformação atravessando terreno.

Pesquisa de quebra: BL-03 e BL-24 nas partes indicadas na seção 4. Consulta complementar à sinopse TYBW 10 localizou o contexto de Ōetsu, sem demonstrar método universal de reparo; a tentativa de abrir a sinopse 13 falhou e não foi usada como evidência. Fontes resumidas, sem capítulos/episódios integrais. M/N aguardam respostas explícitas. Sem implementação, build, migração ou teste de Minecraft. A–L não fecham o bloco enquanto a consequência estrutural de quebra estiver em decisão; números/fichas continuam delegados, sem aceite técnico/artístico por associação.

### Aprovação de M/N e consolidação estrutural de Bankai — 21/10/2026

**M07-03M/N DECIDIDOS** por duas respostas explícitas: M — “Quebra por condições específicas (recomendado)”; N — “Dano persistente com recuperação específica (recomendado)”. Seções 8.24/8.25. M07-03A–N reúne **14 decisões aprovadas**; nenhuma pergunta de Bankai permanece pendente. A revisão anterior A–L é histórica no seu escopo; este fechamento incorpora M/N.

**Estado do bloco M07-03:** `DECIDIDO` em nível estrutural. Conquista, domínio, execução básica, recursos, reações, retorno, posse, presença, prioridade de manutenção, ciclo de vida e consequência de quebra têm contratos e responsáveis. Não foi identificado conflito estrutural que exija revogar decisões no conjunto examinado. Isso não torna M07 inteiro fechado, cada Bankai especificado para implementação ou a experiência implementada/testada/aceita.

| Distinção final | Consequência aprovada |
|---|---|
| Dano comum, bloqueio, perda de Postura, Energia zero ou morte | Não causam quebra estrutural automaticamente; não há barra universal de durabilidade de Bankai |
| Falta de manutenção, com requisitos válidos | Bankai encerra e retorna a Shikai conforme F; isso não repara dano persistente |
| Perda de posse ou evento de ciclo de vida | Encerrar manifestação conforme I/L, sem restaurar Shikai indevido; conquista e condição persistente são registros distintos |
| Quebra estrutural válida | Somente causa/componente/consequência definidos em ficha, com evento autorizado pelo servidor |
| Limitação parcial ou total | Depende do que a ficha declarou atingido; não apagar automaticamente Shikai, vínculo ou domínio |
| Recuperação | Caminho específico disponível antes de introduzir o risco, identificado como adaptação; relogar, selar, morrer ou copiar item não limpa o dano |

**Critérios futuros de M/N:** dano sem causa elegível não cria quebra; efeito visual não gera dano estrutural; evento válido aplica somente a limitação declarada; morte/logout/dimensão/reinício e nova liberação preservam o registro; recuperação válida remove apenas a consequência autorizada e não entrega nova conquista, reembolso ou Mastery por repetição. Esses cenários não foram executados.

| Entrega restante | Responsáveis e condição de conclusão |
|---|---|
| Custos, manutenção, preparo e domínio | M05/M07/M15: valores, períodos, curvas e sustentabilidade calibrados, sem limiares inventados |
| Ficha executável de cada forma/técnica | M06/M07: efeitos, condições de controle, mãos, corpo, espaço e dependências; sem conceder voo, destruição de blocos ou multiplicador universal |
| Preparação, prova e retomada | M08/M12/M13: conteúdo e marcos concretos, sem requisito circular ou mestre obrigatório por melhoria |
| Quebra e recuperação por poder | M06/M07/M09/M11/M12/M13/M15: causa, componente, consequência, reação possível e recuperação disponível; não entregar o risco isolado |
| Equipamento e mundo | M09/M10/M11: espaço válido, vetores, slots, armazenamento, perda/reposição e relação com terreno construído |
| Informação e controles | M14: comandos, avisos, custo/estado, recusa e leitura por observadores |
| Realização técnica | M16: dados, persistência/migração, autoridade/sincronização e evidências de execução; manter divergências do código explícitas |

**Condições de retorno ao bloco:** conflito demonstrado por ficha ou fonte, lacuna que impeça execução, incompatibilidade de corpo/vetor/controle ou falha observada na futura validação. Retornar ao contrato afetado com evidência e proposta explícita, sem reabrir todo A–N ou alterar decisões silenciosamente. A recuperação aprovada é adaptação do mod; não passa a ser fato canônico por constar no GDD.

**Próximo:** M07-04 — evolução Hollow. Primeiro esclarecer marcos e condições da evolução permanente; depois Arrancarização e Resurrección, mantendo rotas já aprovadas em Gillian/Adjuchas/Vasto Lorde, estabilidade sem punição offline e prioridade de sistemas gerais. Catálogo de linhagens/Grimmjow e artes ficam para a etapa de conteúdo. Esta passagem não cria propostas Hollow nem aprova requisitos novos.

Sem nova pesquisa canônica/API no registro de M/N e fechamento; as fontes e seus limites permanecem datados na seção 4. Sem alteração de runtime, build, dependências, saves, commit/push, migração ou teste de Minecraft. Conferência documental não equivale a evidência de gameplay.


### Abertura de M07-04 — evolução Hollow — 21/10/2026

Lidos os contratos Hollow de M04/H1–H9, incluindo REV-H6-01 e a resolução posterior de Vasto Lorde; M06-08G, reconciliação e continuidade vigentes. A antiga pendência de raridade em M04 não reabre H9. Predação, agência Gillian e ausência de regressão passiva são herança; A/B/C são propostas novas de operação, elegibilidade/participação e início deliberado. A evolução não se torna uma quarta Proficiência nem transfere técnicas de cada presa.

Pesquisa delimitada na seção 4 e na base: BL-25/26 e reconsulta à sinopse oficial do episódio 167/BL-03. Fontes resumidas; sem capítulos/episódios integrais ou pesquisa de artes. Não se importaram aptidão aleatória, multiplicadores narrativos, escalas literais ou regras de outros jogos. A/B/C aguardam três respostas explícitas; continuar depois pelos marcos de cada estágio, estabilidade e transições, antes de Arrancarização/Resurrección.

Revisão documental dirigida, sem implementação, build, alteração de dependências/saves ou teste de Minecraft. M07 continua `EM ESPECIFICAÇÃO`; Shikai e Bankai permanecem consolidados em seu escopo estrutural.


### Aprovação de M07-04A/B/C e preparação de D/E — 21/10/2026

Três respostas explícitas aprovam A — “Devorar por interação após derrotar (recomendado)”; B — “Presas elegíveis e participação efetiva (recomendado)”; C — “Iniciar evolução deliberadamente (recomendado)”. Registradas nas seções 8.27–8.29, sem pergunta pendente. A elaboração anterior é histórica; não pedir essas decisões novamente. Núcleos aprovados não tornam toda sugestão de representação/itens, crédito ou ficha automaticamente decidida.

Revisão dirigida preserva predação distinta de abate e de Mastery, participação sem último golpe obrigatório, grind ativo válido e evolução iniciada pelo jogador. Uma arena construída não invalida a atividade por si só. Critérios objetivos de contribuição, distribuição em party, purificação/destruição, consumo completo, espaço, falhas e persistência ainda precisam de ficha. Sem duplicação de crédito ou dispensa do servidor por usar a interface.

Reconsultados BL-25/26 nos verbetes Menos/Grande Hollow e Adjuchas/Vasto Lorde, em 21/10/2026. Agregação e consciência dominante sustentam a referência; acontecimento de formação e prova de predominância são propostas de adaptação. Não houve capítulo/episódio integral, nova fonte de mangá ou consulta de APIs. D/E aguardam respostas: formação Gillian com agregação de NPCs e evolução Adjuchas racial sem linhagem obrigatória. Não é nova votação da agência Gillian ou de H6/H9.

Próximo: D/E, depois Vasto Lorde e complementos de estabilidade/transição; Arrancarização/Resurrección na sequência. M07 permanece em especificação. Sem implementação, build, alteração de dependências/saves ou teste de Minecraft.


### Aprovação de M07-04D/E e preparação de F/G — 21/10/2026

D — “Formação por agregação de Hollows (recomendado)” e E — “Conquista racial sem linhagem obrigatória (recomendado)” aprovados por duas respostas próprias; seções 8.30/8.31. A–E têm cinco decisões aprovadas, sem pergunta pendente. D preserva o início deliberado de C após o marco; E permite incorporar a prova numa quest compatível, sem duplicação ou vínculo obrigatório de linhagem. Agência Gillian, H6/H8/H9 e as rotas Arrancar anteriores permanecem.

Revisão dirigida não identificou conflito que exija revogar A–E. Registros de conquista não substituem espaço válido, execução, corpos/perfis, falha ou persistência. Conteúdo de agregação/prova, valores e elenco ainda não foram escolhidos. O detalhamento candidato não foi promovido integralmente a aprovação por associação.

Reconsultados BL-25 e a sinopse TV Tokyo do episódio 167 nas entradas/trecho pertinentes, em 21/10/2026: dificuldade de ascensão e regressão são referências narrativas, sem comprovar organização em marcos ou punição por falha de prova. Resumos oficiais, sem capítulos/episódios integrais, novas fontes de mangá ou consulta de APIs. F/G são adaptações propostas: rota avançada por marcos + prova final e estabilidade sem regressão racial por falha comum.

**Critérios candidatos de F/G:** marcos concluídos podem ser retomados entre sessões; preparo não exige habilidades do estágio ainda inacessível ou Arrancarização; conclusão da rota habilita C; falha não entrega evolução e, na opção recomendada G, não retira estágio ou progresso pessoal concluído. Aplicação concreta depende das fichas; nenhuma execução em Minecraft ocorreu.

Próximo: F/G, complemento de devoração/transições/ciclo de vida e revisão da evolução; depois Arrancarização/Resurrección. M07 continua `EM ESPECIFICAÇÃO`, sem implementação, build, alterações de dependências/saves ou testes de gameplay.


### Aprovação de M07-04F/G e preparação de H/I/J — 21/10/2026

F — “Marcos registrados + prova final (recomendado)” e G — “Falha limitada à tentativa, sem regressão racial (recomendado)” aprovados por duas respostas próprias; seções 8.32/8.33. A–G têm sete decisões aprovadas, sem pergunta pendente. A rota Vasto Lorde registra marcos e termina numa prova; falha não retira estágio consolidado, predação ou marco concluído, e não devolve gastos legítimos. Progresso parcial da tentativa e execução transitória continuam distintos.

Revisão dirigida com A–E/H9 não identificou conflito que exija revogação: não há evolução automática, retorno de um Arrancar à linha normal, habilidade futura exigida antes de aquisição ou regressão comum como punição. Preservar conquistas não ressuscita presas, duplica recompensas ou restaura ação interrompida. O detalhamento ainda candidato de ficha não foi promovido integralmente a decisão.

**H/I/J PROPOSTOS:** devoração breve estacionária, dentro/fora de combate e cancelável para reagir sem recuperação extra; ausência de custo adicional de Energia/Stamina na interação básica; novo corpo precisa caber na posição atual, sem reposicionamento automático. São propostas de funcionamento em Minecraft, sem pesquisa canônica/API nova nesta rodada. As fontes e limites já registrados mantêm suas datas; não se atribuiu controle, custo ou colisão do mod à obra.

**Critérios futuros de F/G:** retomada dos marcos entre sessões, preservação de estágio e progresso concluído após falha/morte comum, ausência de aquisição ao falhar e manutenção dos custos legítimos. Critérios candidatos de H/I/J: cancelar não registra consumo; reação inválida preserva interação válida; Energia/Stamina zero não impede por recurso a devoração elegível; obstrução do corpo novo impede conclusão sem perder marcos ou deslocar o personagem. Fichas e critérios de validação concretos permanecem pendentes; nenhum cenário foi executado em Minecraft.

Próximo: H/I/J, complemento da mudança evolutiva/ciclo de vida e revisão; depois Arrancarização/Resurrección. M07 permanece em especificação, sem implementação, build, alteração de dependências/saves ou teste de gameplay.


### Aprovação de M07-04H/I/J e preparação de K/L/M — 21/10/2026

H — “Parado, com cancelamento para reagir (recomendado)”; I — “Sem custo adicional de Energia ou Stamina (recomendado)”; J — “Exigir espaço na posição atual (recomendado)” aprovados por três respostas próprias; seções 8.34–8.36. A–J, dez decisões Hollow aprovadas, sem perguntas pendentes. H permite devoração básica dentro/fora de combate, parada e cancelável, sem recuperação adicional; I não cobra nem repõe recursos pela devoração; J mantém o estágio anterior/marcos quando não houver espaço e não reposiciona automaticamente.

Revisão dirigida preserva fases/tempos anteriores, conclusão única do consumo e distinção entre devoração e mudança evolutiva. Os complementos de ficha, duração, meios, corpo, disponibilidade de oportunidade e participação/distribuição não foram promovidos integralmente a decisão. J não fixa tamanho literal de Gillian nem resolve retorno/respawn em volume corporal válido.

**K/L/M PROPOSTOS:** mudança evolutiva sem taxa/manutenção adicional de Energia/Stamina; preparo breve apoiado no chão e fora de combate, cancelável e sem recuperação extra; eventos de ciclo de vida encerram execuções incompletas sem apagar evolução/conquistas já concluídas ou retomar ação automaticamente. Propostas próprias, sem herdar por associação custo da devoração ou regras das formas Shinigami.

**Critérios futuros de H/I/J:** interrupção sem crédito de consumo; reação disponível cancela e recusada preserva preparo válido; reservas vazias não impedem por recurso a devoração; falta de espaço inicial/final não estabelece corpo novo nem remove marcos. **Critérios candidatos de K/L/M:** ausência de cobrança/refill pela evolução; entrada em combate ou perda de apoio cancela preparo na opção recomendada; interrupção de sessão preserva só a conquista concluída e exige novo pedido. Nenhum cenário foi executado; fichas e responsabilidades ainda impedem declarar implementação.

Sem nova pesquisa canônica/API nesta atualização de aprovações e contratos locais. Fontes anteriores conservam datas/limites; não se atribuiu custo, apoio, Combat State ou persistência de Minecraft à obra. Próximo: K/L/M, revisão estrutural e encaminhamentos; depois Arrancarização/Resurrección. Sem implementação, build, alterações de dependências/saves ou teste de gameplay.

### Aprovação de K/L/M e consolidação estrutural da evolução Hollow — 21/10/2026

**M07-04K/L/M DECIDIDOS** por três respostas explícitas: K — “Evolução sem taxa adicional de recurso (recomendado)”; L — “Preparo apoiado e fora de combate (recomendado)”; M — “Encerrar preparos; preservar conquistas (recomendado)”. Registradas nas seções 8.37–8.39. M07-04A–M reúne **13 decisões aprovadas**; nenhuma pergunta básica do bloco aguarda resposta. Os registros anteriores de propostas permanecem históricos, sem reabrir respostas recebidas.

**Estado do bloco M07-04:** `DECIDIDO` em nível estrutural. Revisão documental dirigida de A–M e das dependências aplicáveis de M04/H6–H9, M05/REV-M02 e M06/corpos, fases, modos e ciclo de vida não identificou conflito estrutural que exija revogar decisões examinadas. Foram atualizadas remissões internas já resolvidas. Isso não é auditoria integral do cânone/código; M07 inteiro segue `EM ESPECIFICAÇÃO`, sem ficha executável ou gameplay aceito.

| Situação | Resultado aprovado e limite |
|---|---|
| Derrotar sem devorar | A/B: não registra predação automaticamente; exige presa elegível, participação efetiva e conclusão válida |
| Caçar em cooperação ou arena construída | B: não exigir último golpe; construção/grind ativo não invalidam por si só. Critérios objetivos e distribuição ainda precisam de regra própria |
| Formar Gillian | D/C: agregação de NPCs registra o marco; agência permanece e a mudança do corpo é deliberada |
| Conquistar Adjuchas | E: prática e prova racial sem linhagem definitiva obrigatória; quest compatível pode integrar a mesma prova, sem duplicação |
| Buscar Vasto Lorde | F/H9: marcos registrados e prova final, difícil e determinístico; permanecer na rota Hollow, sem requisito circular ou sorte oculta |
| Falhar, sofrer dano ou morrer | G: não perder estágio ou marcos concluídos; tentativa incompleta pode ser repetida e consequências normais permanecem |
| Devorar e mudar de corpo | H/L: operações distintas. Devoração pode ocorrer em combate; evolução exige preparo parado, apoiado e fora de Combat State |
| Energia/Stamina vazias | I/K: ausência desses recursos não bloqueia por taxa adicional a devoração/evolução; demais requisitos continuam, sem cura/refill |
| Espaço inicial ou final insuficiente | J: conservar corpo anterior e marcos, avisar para reposicionar; não teleportar, encolher ou remover construções |
| Reagir ou perder requisito no preparo | H/L: aplicar cancelamento permitido sem recuperação extra; preservar tempos anteriores. Pedido recusado só mantém execução ainda válida |
| Morrer, desconectar, viajar ou reiniciar | M: encerrar execuções incompletas sem retomada automática; estágio já consolidado permanece e tempos seguem M06-11V |
| Escolher Arrancarização antes de Vasto Lorde | H8/H9: encerrar a evolução normal naquele ponto. A–M não concedem retorno livre, Resurrección ou futuras técnicas por associação |

**Lacuna prioritária antes de uma origem jogável:** J resolve o espaço da mudança escolhida, mas não resolve onde um corpo grande reaparece ao morrer, reconectar ou mudar de dimensão. Preservar permanentemente Gillian exige política de spawn/retorno compatível. M10/M15/M16 devem apresentar essa solução antes da entrega; não reduzir estágio, forçar colisão ou criar forma compacta silenciosamente. Nenhuma solução concreta de retorno foi aprovada nesta revisão.

| Entrega restante | Responsáveis e condição de conclusão |
|---|---|
| Perfil corporal por estágio | M06/M07/M10/M11/M14: medidas, colisão, câmera, golpes, alcance, mãos/equipamento/ferramentas e transição; incluir Adjuchas sem linhagem escolhida. Perfis não exigem escolher arte agora |
| Execução de devoração/evolução | M06/M07/M10/M14/M15: alcance, duração nominal, meios/apoio, controles, informação ao jogador/observadores e regras de ações concorrentes; respeitar os núcleos H–M |
| Presa e oportunidade de consumo | M09/M11/M15/M16: representação, validade, prazo, região/descarregamento/reinício e consumo único. Modelo de item/cadáver persistente e distribuição não foram decididos por A |
| Participação e resolução multirracial | M06/M09/M15: critérios objetivos de contribuição e atribuição; destino da alma/RAC-19 e derrotas que ainda permitem consumo. Não recriar alma removida nem conceder progresso AFK |
| Requisitos, marcos e provas concretos | M05/M06/M08/M09/M12/M13/M15: preparo pertinente já acessível, predadores/presas, agregação, identidade e ascensão, falha/retomada e cooperação. Não exigir mestre para cada melhoria nem duplicar a prova integrada à linhagem |
| Economia e crescimento | M05/M07/M15: calibrar requisitos, prática e alteração de capacidades/máximos sem cura/refill, multiplicador universal, quarta Proficiência ou Mastery comprada |
| Mundo e retorno em corpo permanente | M10/M15/M16: política de espaço/segurança para spawn, reconexão e viagem, com terreno construído e regiões ativas. Condição impeditiva da entrega jogável enquanto não resolvida |
| Presença e efeitos sustentados | M06/M07 e fichas: classificar devoração quanto ao início ofensivo/Supressão e verificar compatibilidades com preparo/troca de corpo. Não presumir ocultação, passivas futuras ou manutenções desligadas por associação |
| Realização técnica e evidência | M16: dados separados para estágio/marcos/execução, conclusão autorizada uma vez, persistência, migração, sincronização e validação futura. O reset de cooldowns anteriormente localizado continua divergência de M06-11V |

**Critérios futuros complementares:** cobrir pedido duplicado/concorrente de devoração, contribuição útil sem último golpe, conclusão versus evento de ciclo de vida, obstrução surgida durante o preparo, perda de apoio/entrada em combate e retomada em corpo grande. Verificar ausência de crédito/refill/reset indevido e preservação do progresso concluído. Os resultados esperados básicos vêm de A–M; completar as fichas e políticas pendentes antes de executar a validação. Não foram realizados testes de Minecraft nesta revisão.

**Condições de retorno:** incompatibilidade demonstrada por ficha, fonte ou futura execução; ausência de corpo/local/retorno viável; impossibilidade de crédito/prova coerente em cooperação; conflito com mãos, fases ou presença. Reabrir somente o contrato afetado com evidência e proposta explícita. Pendência técnica não autoriza alterar silenciosamente estabilidade, requisito ou escolha de rota já aprovados.

**Próximo: M07-05 — Arrancarização**, requisitos/processo/consequências entre origens; depois Resurrección. H6/H8/H9 continuam vigentes, sem obrigar Vasto Lorde ou voltar à evolução normal após Arrancarizar. Linhagens/Grimmjow e artes continuam para a etapa de conteúdo. A revisão não aprova custos ou métodos novos dessas próximas etapas.

Registro de aprovações e revisão de contratos locais, sem nova pesquisa canônica/API. Fontes anteriores conservam datas e limites; economia, preparo e persistência são adaptações do mod. Sem alteração de runtime, build, dependências ou saves, commit/push, migração ou teste de gameplay.


### Abertura de M07-05 e aprovação de A/B/C — 21/10/2026

Conferidos checkout/HEAD, plataforma local, handoff/mapa/base, RAC-04, H6/H8/H9, perfis/M06-08G e fechamento M07-04. Consultado BL-27, verbete Arrancar; reconsultados BL-26/Arrancar e BL-11/Hōgyoku. Textos oficiais japoneses interpretados, com limites na seção 4; não capítulos/episódios integrais. BL-03 recuperada apenas por índice nesta rodada, abertura direta 403, sem uso decisivo para requisitos.

Três respostas explícitas aprovaram A — “Preparo no estágio + acontecimento próprio (recomendado)”; B — “Via pessoal sem facção ou Hōgyoku obrigatórios (recomendado)”; C — “Arma própria ao concluir Arrancarização (recomendado)”. Seções 8.41–8.43; nenhuma dessas perguntas permanece pendente. As perguntas foram respondidas durante a preparação; não houve rodada A/B/C arquivada como se continuasse sem resposta.

Revisão dirigida preserva a origem, escolha entre as três classes Menos, encerramento da evolução normal, acesso compatível ao poder principal e a diferença entre tornar-se Arrancar, portar sua arma e conquistar Resurrección. Não identificado conflito com os contratos examinados. Não foram definidos método assistido, NPC/ritual concreto, perfil artístico, custos, valores ou novos poderes por associação. A rota pessoal é adaptação expressa, não procedimento canônico universal comprovado.

**Próximo:** corpo e continuidade do desenvolvimento, viabilidade/diferenças entre origens e execução da passagem; depois Resurrección. M07-05 ainda em especificação e M07 inteiro não está fechado. Atualizados handoff/mapa/base; sem implementação, build, alteração de dependências/saves, commit/push ou teste de Minecraft.


### Preparação de M07-05D/E — corpo e continuidade — 21/10/2026

Relidos A/B/C, perfis funcionais M06-08C, desenvolvimento M06-08A/B, linhagens M06-08G e compatibilidade de regeneração M06-08J. Reconsultados BL-26/Arrancar/Vasto Lorde, BL-27 e BL-10/Aaroniero; fontes e limites na seção 4. Recuperados apenas trechos indexados de BL-03/Arrancar e BL-14/Ulquiorra para a cautela com regeneração, sem nova leitura integral dessas páginas. Não houve capítulo/episódio integral nem confirmação de todas as anatomias/exceções.

**D/E PROPOSTOS:** corpo-base humanoide jogável nas três origens e preservação de desenvolvimento com execução das capacidades já aprendidas quando compatíveis. Duas perguntas enviadas, aguardando respostas próprias. Arquitetura de perfis e histórico preservado não são aprovação antecipada dessas escolhas de aplicação; capacidades novas, anatomia incompatível e regeneração conservam requisitos próprios.

Revisão dirigida não identificou necessidade de reabrir A/B/C ou H6/H8/H9. D é adaptação explícita da experiência corporal; E define a continuidade jogável sem novo indicador racial. Medidas, catálogo, arte, vantagens entre origens, custos, execução e ciclo de vida não foram definidos por associação. Próximo: registrar respostas e seguir consequências/execução antes de Resurrección. M07 permanece em especificação; sem alteração de runtime, build, dependências/saves ou testes de Minecraft.


### Aprovação de M07-05D/E e correção do adiamento de D — 21/10/2026

**D DECIDIDO:** a primeira resposta pediu adiar a escolha do corpo-base. O usuário corrigiu em seguida: “Eu entendi errado a primeira, concordo com a recomendação”. Essa mensagem substitui o adiamento e aprova a base humanoide jogável nas três origens, sem fixar medidas, traços, diferenças funcionais ou arte. Não é uma votação nova nem aprovação de todos os complementos candidatos.

**E DECIDIDO:** “Preservar domínio e usar o que for compatível (recomendado)”. Proficiências/Masteries conservadas; técnica já aprendida utilizável quando compatível; capacidades incompatíveis indisponíveis sem apagar histórico; novas capacidades com aprendizado próprio. Regeneração rápida continua dependente de compatibilidade, sem transferência universal.

Registrados nas seções 8.44/8.45. A–E, cinco decisões de Arrancarização aprovadas; nenhuma dessas perguntas pendente. Revisão documental preserva os três campos, perfis funcionais, histórico de origem, acesso compatível da origem Gillian e encerramento da evolução normal em H9. Não identificado conflito no conjunto examinado. O perfil concreto e a matriz de capacidades continuam necessários; D não torna todas as rotas numericamente iguais nem define todos os corpos canônicos como humanos.

Próximo: diferenças entre origens e execução/recursos/interrupção/falha/ciclo de vida; depois Resurrección. Arrancarização e M07 inteiro continuam em especificação. Registro das respostas e revisão de contratos locais, sem nova pesquisa canônica/API, alteração de runtime/build/dependências/saves, commit/push ou testes de Minecraft.


### Preparação de M07-05F/G — origens e custo da passagem — 21/10/2026

Conferidos Git/HEAD, continuidade vigente, H6/H7/H8/H9, M05-P03/escala universal e M07-05A–E. Reconsultados BL-26, BL-10 e BL-27; seção 4 registra fatos e limites. Não houve capítulo/episódio integral nem pesquisa de API. A hierarquia narrativa de Menos não foi convertida em fórmula ou teto racial de jogo; Aaroniero não concede sua capacidade individual aos demais Gillians.

**F/G PROPOSTOS**, sem aprovação: trajetórias diferenciadas pelo preparo e momento de acesso, crescimento compatível sem bônus fixo apenas pelo rótulo de origem; passagem permanente sem taxa adicional de Energia/Stamina nem manutenção da condição. Duas perguntas próprias; não registrar como decidido por autorização genérica de continuar. A–E permanecem aprovados, inclusive D após a correção do adiamento.

F explicita que a viabilidade avançada ainda exige fichas e comparação em M15; vantagem temporal isolada não aprova uma rota inviável no fim do jogo. G separa custo da condição e custos normais das ações/provas, sem refill ou reset. Não escolhidos bônus, perfis, poderes, números, redução de teto, custo de material ou novo ritual. Próximo após as respostas: execução, espaço/apoio, falha/interrupção e ciclo de vida; depois Resurrección. Atualizados mapa/handoff/base; sem implementação ou testes de Minecraft.


### Aprovação de M07-05F/G — origens e custo da passagem — 21/10/2026

Duas respostas explícitas: F — “Trajetórias e preparo, sem bônus fixo por origem (recomendado)”; G — “Sem taxa adicional pela Arrancarização (recomendado)”. Registradas como DECIDIDO nas seções 8.46/8.47. A–G, sete decisões aprovadas, sem perguntas pendentes. A preparação anterior de F/G permanece como histórico; não governa mais o estado atual.

F diferencia preparo acumulado e momento de acesso, com crescimento nas capacidades compatíveis. Não aprova bônus exclusivos por origem, menor teto de atributos, igualdade de desempenho, todo poder para toda origem ou evolução posterior a Arrancar. G não cobra Energia/Stamina adicional pela passagem nem manutenção da condição; gastos normais e recursos/tempos anteriores permanecem. Viabilidade de cada percurso, perfis, repertório concreto, conversão de máximos e arma/inventário continuam com responsáveis.

Revisão documental dirigida preserva H6/H8/H9, M05 e A–E. Não identificada necessidade de revisar essas decisões no recorte examinado. As duas respostas não aprovam todos os complementos candidatos nem encerram Arrancarização. Próximo: execução/espaço/apoio, interrupção/falha/ciclo de vida; depois Resurrección. Atualizados mapa/handoff/base. Registro de respostas e contratos locais, sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft.


### Preparação de M07-05H/I/J — execução da passagem — 21/10/2026

A–G continuam aprovados. H/I/J PROPOSTOS na seção 12.1, aguardando três respostas: escolha deliberada com preparo parado/apoiado fora de combate; espaço validado no local atual; interrupção da tentativa sem apagar marcos e limpeza de preparo no ciclo de vida, preservando a condição já concluída. Não registrar por associação às aprovações de evolução Hollow ou Shinigami.

Revisados os contratos de conquista/condição permanente, A–G, H8/H9, perfis e M06/interrupções, modos e ciclo de vida. A conclusão pode integrar o acontecimento da máscara, sem segundo ritual obrigatório. Corrigidas sínteses desatualizadas de M07/mapa que ainda paravam em A–E; as sete aprovações e seu histórico permanecem. Complementos de ficha, efeitos sustentados, inventário cheio/criação vinculada e retorno seguro continuam com responsáveis. Sem pesquisa canônica/API nova, implementação ou testes de Minecraft. Próximo: respostas e revisão, depois Resurrección.


### Aprovação de M07-05H/I/J e revisão estrutural de Arrancarização — 21/10/2026

Três respostas explícitas registradas nas seções 8.48–8.50:

- **H:** “Preparo deliberado e fora de combate (recomendado)”. Escolha deliberada; preparo breve parado/apoiado fora de combate, cancelável nas condições aprovadas sem recuperação extra só pelo cancelamento. Pode integrar o acontecimento da máscara, sem segundo ritual obrigatório.
- **I:** “Exigir espaço na posição atual (recomendado)”. Pés/direção preservados; validar espaço no início e na conclusão; recusa mantém corpo anterior/conquistas sem deslocar ou remover blocos.
- **J:** “Encerrar preparo; preservar conquistas (recomendado)”. Eventos de ciclo de vida encerram preparo incompleto, sem retorno automático; Arrancarização já concluída permanece. Gastos e tempos legítimos são preservados.

**Resultado documental:** M07-05A–J, dez decisões aprovadas; nenhuma pergunta pendente. Revisão dirigida de A–J e dos contratos examinados de M04/H6/H7/H8/H9, M05 e M06 não identificou conflito estrutural que exija revogá-los. Não é auditoria integral do cânone, catálogo ou runtime. As respostas aprovam os núcleos perguntados, sem converter todos os complementos de ficha em especificação executável.

| Situação revisada | Contrato vigente e limite |
|---|---|
| Gillian preparado escolhe Arrancarizar | A/B/H preservam a via pessoal e a escolha; H9 encerra evolução normal; não exigir Adjuchas ou Hōgyoku |
| Acontecimento integrado a quest compatível | A/H permitem concluir a passagem nele; não repetir o mesmo marco nem inventar segundo ritual obrigatório |
| Origem Gillian/Adjuchas/Vasto Lorde | D/E/F preservam base humanoide, histórico e desenvolvimento compatível; viabilidade avançada ainda precisa de evidência |
| Técnica nova ou anatomia incompatível | E não concede técnica/Mastery nova ou capacidade ausente; regeneração mantém compatibilidade própria |
| Mudança solicitada sem apoio, em combate ou sem espaço | H/I recusam ou interrompem a mudança; mantém estágio e marcos, sem benefício antecipado do novo perfil |
| Obstrução ou combate surge durante o preparo | Revalidar/cancelar conforme H/I; sem empurrar a mudança para outro lugar ou remover construções |
| Reação disponível durante o preparo | Cancela antes de reagir; custos/requisitos da reação e tempos anteriores permanecem |
| Passagem interrompida antes da conclusão | J mantém estágio anterior e marcos concluídos; não restaurar a parte incompleta nem devolver gastos normais |
| Morte/logout/dimensão/reinício antes ou depois da conclusão | Antes: encerra preparo, novo pedido. Depois: mantém condição Arrancar; estados transitórios próprios seguem M06-11U |
| Arma surge na conclusão | C não entrega Resurrección; inventário cheio/entrega vinculada sem duplicação ainda precisam de solução M11/M16 |
| Recursos e tempos após a passagem | G/J impedem taxa adicional, refill ou reset; conversão de máximos continua pendente |

**Consolidação estrutural:** aquisição, condição permanente, execução básica, continuidade do desenvolvimento e responsabilidades estão definidas para continuar o planejamento. Não declarar ficha pronta, equilíbrio demonstrado, origem jogável completa ou aceite. Compatibilidades e entregas abaixo continuam condições para implementação/validação.

| Entrega pendente | Responsáveis | Condição antes da entrega jogável |
|---|---|---|
| Preparo pertinente, acontecimento e partes repetíveis | M07 + M08/M09/M12/M13 | Conteúdo possível no estágio anterior, sem requisito circular, marcos duplicados ou dependência obrigatória de facção/Hōgyoku |
| Perfil-base e matriz de capacidades | M06/M07 + M10/M11/M14/M15 | Medidas, colisão, câmera, mãos/equipamento e execução sem mistura de corpos; perfil válido com/sem identidade já escolhida |
| Arma própria e inventário cheio | M11 + M07/M16 | Entrega utilizável, posse/perda/reposição e vínculo, sem destruir itens, duplicar recompensa ou deixar conversão parcial |
| Ficha da transição | M07 + M06/M10/M14/M16 | Duração, apoios/meios, fases, efeitos sustentados, feedback e ordem de conclusão/interrupção executáveis |
| Capacidades e crescimento nas três origens | M05/M06/M07 + M15 | Percursos completos úteis, sem bônus/teto menor apenas pela origem ou rota Gillian inviável no conteúdo avançado |
| Máximos, recursos e condições anteriores | M05/M07 + M15/M16 | Conversão explícita sem refill disfarçado, limpeza de condições ou reset de tempos |
| Persistência, observadores e retorno | M16 + M07/M10/M11/M15 | Conquista e origem preservadas, execução transitória limpa, arma não duplicada, retorno espacial compatível; morte/itens/recursos seguem suas políticas próprias |

**Condições de retorno ao bloco:** incompatibilidade de corpo/arma/identidade, impossibilidade de entrega com inventário cheio, requisito circular, perda/duplicação de progresso ou rota dominada exigem completar a ficha ou revisar a decisão afetada explicitamente antes da entrega. Não corrigir por regressão, refill, evolução retroativa ou bônus oculto.

**Próximo: M07-06 — Resurrección.** Abrir por pesquisa pertinente, conquista/domínio e vínculo entre arma própria e identidade compatível; obter decisões próprias para execução, custos, manutenção, reversão e consequências. Não inferir Segunda Etapa, corpo antigo ou políticas Shinigami por associação. Linhagens/Grimmjow e artes permanecem adiados.

Atualizados módulo, handoff, mapa e base. Registro das respostas e revisão de contratos locais; sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft. Fontes anteriores mantêm suas datas/limites. A documentação não demonstra gameplay validado.


### Abertura de M07-06 — Resurrección; A/B propostos — 21/10/2026

Conferidos Git/HEAD, continuidade e contratos pertinentes de M04/RAC-20/RAC-23/H6/H8/H9, M05, M06-08G e M07-05. Reconsultados BL-19/帰刃 e BL-26/Arrancar; abertura direta de BL-03 retornou 403 e busca recuperou a entrada indexada de刀剣解放. Fontes/limites na seção 4; não capítulos/episódios integrais. A identidade canônica permanece distinta da biografia do personagem; não usar similaridade com Bankai para copiar seu currículo ou custos.

**M07-06A/B PROPOSTOS**, seção 12.1: aprendizado do poder próprio com demonstração de liberação integrada à quest quando cabível; manifestação básica confiável após conquista e Mastery própria, com técnicas/versões posteriores separadas. Duas respostas necessárias. Aquisição da identidade e posse da arma não aprovam essas novas regras por associação.

A proposta preserva acesso compatível Gillian, três campos, domínio prévio e marcos existentes. Não escolhe ritual espiritual, personagem mestre, prova concreta, forma corporal, números, manutenção, perda/reposição ou Segunda Etapa. Próximo após respostas: manifestação/recursos, corpo/arma, encerramento e ciclo de vida. M07 segue em especificação; linhagens/artes adiadas. Atualizados mapa/handoff/base; sem consulta de API, implementação, build ou testes de Minecraft.


### M07-06A/B aprovados — conquista e domínio da primeira Resurrección — 21/10/2026

Duas respostas explícitas: A — “Preparo do próprio poder + demonstração (recomendado)”; B — “Manifestação confiável + Mastery própria (recomendado)”. Registradas como DECIDIDO nas seções 8.52/8.53. A/B sem perguntas pendentes; preparação anterior preservada como histórico.

A exige condição Arrancar, arma e identidade vinculadas, preparo e demonstração prática de liberação. A demonstração pode integrar a quest do poder sem prova equivalente duplicada; conquista registrada, sem repetição a cada uso. B torna a manifestação básica confiável com requisitos cumpridos, sem sorte por ativação; Mastery própria por prática, com técnicas e versões posteriores mantendo aprendizado/domínio próprios. Não concedem todo repertório, Segunda Etapa, corpo anterior ou política de custos por associação.

Revisão dirigida preserva RAC-20/RAC-23, H6, acesso compatível Gillian e M07-05C/E/F/J. Preparo não exige executar previamente uma capacidade exclusiva da forma nem Mastery de Resurrección ainda inacessível. A/B não introduzem ritual universal de espírito, compra de Mastery ou uma nova Proficiência. Não identificado conflito estrutural no recorte examinado. Conteúdo, marcos de prática e ficha inicial continuam com M06/M07/M08/M12/M13/M14/M15/M16; não estão executáveis por esta aprovação.

Próximo: manifestação, arma/empunhadura, modo e recursos; depois corpo/repertório, encerramento, compatibilidades e ciclo de vida. Linhagens/artes adiadas. Atualizados módulo/handoff/mapa/base. Registro das respostas sem pesquisa adicional além da abertura desta rodada; sem API, implementação, build ou testes de Minecraft. M07-06 e M07 inteiro permanecem em especificação.


### M07-06C/D propostos — ativação e recursos — 21/10/2026

A/B continuam aprovados; C/D PROPOSTOS na seção 12.1, aguardando duas respostas: ativação manual com arma própria empunhada para liberar, independente da interface; Energia na conclusão e manutenção enquanto ativa, sem prazo universal. Alternativas: ativação automática ao ataque e custo inicial com duração fixa. Custos e regras Shinigami não foram importados como aprovação.

Reconsultados BL-19/帰刃 e BL-26/Arrancar; recuperada a entrada indexada BL-03/刀剣解放. Não localizada nessas consultas regra universal de custo; fontes e limites na seção 4. São adaptações explícitas, sem capítulo/episódio integral ou nova API. M07-05 e M06/fases, recursos e interface foram preservados. O requisito inicial de arma não decide manutenção de empunhadura após integração corporal; formas especiais, mãos, inventário e posse/perda seguem para fichas.

Sem números, cura, Segunda Etapa ou prioridade de débitos aprovados. Próximo após respostas: preparo/interrupção e transições, corpo/arma, encerramento, compatibilidades/pagamentos e ciclo de vida. M07 permanece em especificação; linhagens/artes adiadas. Atualizados módulo/handoff/mapa/base; sem implementação, build ou testes de Minecraft.


### M07-06C/D aprovados — ativação e recursos de Resurrección — 21/10/2026

Duas respostas explícitas: C — “Ativação manual com arma própria (recomendado)”; D — “Ativação + manutenção de Energia (recomendado)”. Seções 8.54/8.55. A–D, quatro decisões aprovadas; nenhuma dessas perguntas pendente. Preparação anterior preservada como histórico.

C permite pedido manual com a arma própria compatível empunhada para liberar, em combate/exploração e dentro/fora de Combat Mode; fechar a Combat Hotbar não encerra a manifestação por si só. Fases e requisitos permanecem. D cobra Energia uma vez na conclusão e manutenção enquanto ativa, sem prazo fixo universal; cancelamento anterior não cobra ativação, técnicas conservam custos próprios e falta de recurso para manutenção encerra a manifestação. Conquista e condição Arrancar não são apagadas ao encerrar a forma.

Revisão documental dirigida preserva A/B, M07-05C/E/F/J e M06/fases, modos, recursos e tempos. Não identificado conflito no recorte examinado. A aprovação de C não impõe continuar empunhando uma espada separada depois de uma incorporação corporal; esse contrato ainda precisa da ficha. D não escolhe números, prioridade de cobranças, cura, retorno corporal ou Segunda Etapa. Cancelamento sem cobrança de ativação não devolve gastos normais de ações anteriores.

**Próximo:** preparo/interrupção e conclusão/encerramento; depois corpo/arma, posse/equipamentos, compatibilidades/pagamentos e ciclo de vida. Duração do preparo, meios/espaço, valores, forma de retorno, conteúdo e fichas continuam pendentes. Linhagens/artes adiadas. Atualizados módulo/handoff/mapa/base. Registro das respostas e revisão local, sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft. M07-06 e M07 inteiro seguem em especificação.


### M07-06E/F/G propostos — preparo e encerramento — 21/10/2026

A–D preservados. Três propostas na seção 12.1: preparo breve com caminhada limitada e cancelamento para reação disponível; encerramento manual e retorno ao estado-base Arrancar; conclusão/encerramento voluntário sem recuperação adicional só pela mudança. E/F/G aguardam respostas, sem aprovação presumida. Alternativas e consequências estão explicitadas.

Confrontados A–D, M07-01/M07-05 e M06-02S/T/U, recursos, fases, tempos e M06-11W/Y. Reconsultados BL-19/BL-26, com limites na seção 4; não fundamentam tempos de gameplay ou reversão universal. Corpo/arma, espaço inicial/final/retorno, meios, ações dependentes, posse, prioridades e ciclo de vida ainda exigem decisões/fichas. Corrigido o primeiro encaminhamento da seção 15, que ainda apontava Arrancarização A–E; histórico preservado. Atualizados handoff/mapa/base. Sem APIs, implementação, build ou testes de Minecraft; M07 segue em especificação.


### M07-06E/F/G aprovados — preparo e encerramento — 21/10/2026

Três respostas próprias registradas como DECIDIDO nas seções 8.56–8.58: E — “Preparo cancelável para reagir (recomendado)”; F — “Encerrar manualmente e voltar ao estado-base (recomendado)”; G — “Transições sem recuperação adicional (recomendado)”. A–G, sete decisões aprovadas; nenhuma dessas perguntas pendente. Preparação anterior preservada como histórico.

E permite caminhada limitada/olhar e cancelamento para reação disponível, sem recuperação extra ou cobrança de ativação; reação recusada conserva preparo ainda válido. F permite encerrar quando a fase autorizar e retorna ao estado-base Arrancar, também após falta de manutenção, conservando conquista/Mastery sem restituição de recursos. G não acrescenta custo/recuperação à conclusão ou ao encerramento voluntário; fases, recursos e tempos anteriores permanecem.

Revisão local confronta A–D, condição permanente de M07-05, dano/controle/fases, recursos, efeitos e tempos de M06. Não identificado conflito no recorte examinado. As respostas não aprovam todos os complementos de ficha, efeitos de perda forçada, meios, política espacial, mãos/posse ou ciclo de vida. Prioridade: corpo/arma e espaço da mudança/retorno, sem preservar vantagem não financiada nem atravessar parede por omissão. Depois compatibilidades/pagamentos e ciclo de vida. Atualizados módulo/handoff/mapa/base. Sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft; fontes anteriores mantêm datas/limites. M07 permanece em especificação; linhagens/artes adiadas.


### M07-06H/I/J propostos — corpo, arma e espaço — 21/10/2026

A–G preservados. Perfis funcionais e identidade de cada etapa consolidados como herança de M06-08C/F/G, sem nova votação ou seleção de artes. H/I/J PROPOSTOS na seção 12.1: arma conforme o poder e itens compatíveis sem encerrar a forma; espaço na posição atual antes/conclusão; volume de passagem incluindo corpo-base na postura compatível para permitir retorno. Três respostas necessárias; custos/tempos e retorno de A–G permanecem.

BL-14 consultado nas entradas Tiburón/Gamuza; BL-19/BL-26 reconsultados. BL-05 retornou 403. Fontes/limites na seção 4, sem capítulo/episódio integral. Propostas espaciais e de inventário são adaptações; J limita passagens de formas menores e não decide hitbox de combate ou pose nova. Inventário, mundo alterado, meios, arma perdida, armadura e ciclo de vida seguem pendentes. Atualizados módulo/handoff/mapa/base; sem APIs, implementação, build ou testes de Minecraft. M07 segue em especificação; linhagens/artes adiadas.


### M07-06H/I/J aprovados — arma, itens e espaço — 21/10/2026

Três respostas próprias registradas como DECIDIDO nas seções 8.59–8.61: H — “Arma conforme o poder; itens compatíveis permitidos (recomendado)”; I — “Exigir espaço na posição atual (recomendado)”; J — “Passagem preserva espaço do corpo-base (recomendado)”. A–J, dez decisões aprovadas; nenhuma dessas perguntas pendente. Preparação anterior preservada como histórico.

H representa arma incorporada/transformada/separada conforme poder, sem cópia selada utilizável; permite itens compatíveis mantendo mãos/fases/manutenção. I valida espaço antes e ao concluir na posição atual, sem reposicionamento ou destruição, preservando corpo anterior/conquistas sem cobrar ativação não concluída. J inclui o corpo-base na passagem, na postura compatível, limitando acesso por formas menores para permitir retorno básico na posição atual. Perfis funcionais por etapa permanecem herança de M06-08C/F/G, sem nova escolha de artes.

Revisão local confronta A–G, M07-05, M06/empunhadura, fases, ferramentas, custos/tempos e corpo funcional. Não identificado conflito no recorte examinado. H não resolve o item incorporado/slot/inventário cheio nem perda/morte/armadura; I/J não concedem poses/meios novos, hitbox de combate, invulnerabilidade ou reparação automática após intervenção do mundo. Complementos de ficha não foram aprovados em bloco. Próximo: meios, posse/perda, equipamentos restantes, compatibilidades/pagamentos e ciclo de vida. Atualizados módulo/handoff/mapa/base. Registro das respostas sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft; fontes mantêm datas/limites. M07 segue em especificação; linhagens/artes adiadas.


### M07-06K/L/M propostos — meios e posse da arma — 21/10/2026

A–J preservados. K propõe liberar/encerrar no chão, no ar e na água com requisitos válidos, preservando gravidade, queda e riscos; L propõe guardar armamento separado no próprio inventário mantendo forma paga e suspendendo dependências de empunhadura; M propõe encerrar por perda efetiva da arma separada, sem transferir poder, recolher o item ou restaurar a forma ao recuperá-lo. Três respostas pendentes; alternativas e limites na seção 12.1. Não são decisões aprovadas.

Revisão dirigida de A–J e M06/empunhadura, fases, itens, efeitos e tempos. Reconsultados BL-19/BL-26 e entradas Tiburón/Gamuza de BL-14; fontes e limites na seção 4. Não comprovam políticas universais de inventário/meios. Item incorporado, slots/inventário cheio, armadura, alterações forçadas do mundo, efeitos dependentes, pagamentos e ciclo de vida ainda exigem decisão/ficha. Sem novas APIs, implementação, build ou testes de Minecraft. M07 segue em especificação; linhagens/artes adiadas.


### M07-06K/L/M aprovados — meios e posse da arma — 21/10/2026

Três respostas próprias registradas como DECIDIDO nas seções 8.62–8.64: K — “Chão, ar e água, preservando queda e riscos (recomendado)”; L — “Guardar conserva a forma com manutenção (recomendado)”; M — “Perda de posse encerra a manifestação (recomendado)”. A–M, treze decisões aprovadas; nenhuma dessas perguntas pendente. Preparação anterior preservada como histórico.

K permite liberar/encerrar nos três meios válidos, preservando gravidade, trajetória, queda e riscos. L conserva a forma paga ao guardar a arma separada no próprio inventário, suspendendo dependências de empunhadura. M encerra por perda efetiva de posse, conservando conquistas sem transferir poder, recolher item ou restaurar a forma ao recuperá-lo; nova liberação paga normalmente. Incorporação não cria cópia nem equivale a perda de posse.

Revisão local confronta A–J, M07-05 e M06/empunhadura, corpo, fases, custos/tempos e efeitos autônomos. Não identificado conflito no recorte examinado. Complementos de ficha não foram aprovados em bloco: poses/volumes, intervenções do mundo, item incorporado/slots/inventário cheio, representação da perda/retorno e ações dependentes continuam pendentes. Próximo: equipamentos restantes, compatibilidades com Reiatsu, pagamentos, ciclo de vida e revisão. Atualizados módulo/handoff/mapa/base. Registro sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft; fontes mantêm datas/limites. M07 continua em especificação; linhagens/artes adiadas.


### M07-06N/O/P propostos — equipamento, Reiatsu e pagamentos — 21/10/2026

A–M preservados. N propõe quatro slots normais de armadura acessíveis/funcionais durante Resurrección, aparência adaptável e manutenção do mesmo item/desgaste; compatibilidade ainda não estava aprovada por M06-11A. O propõe coexistência básica com estados de Reiatsu, sinais/custos preservados e ofensiva válida encerrando Supressão. P propõe Resurrección → Liberação → ação para cobranças realmente coincidentes, tentando cada manutenção válida separadamente. Três respostas pendentes na seção 12.1; alternativas e complementos explicitados, sem aprovação presumida.

Revisão dirigida de A–M, M06-11A/B/J/K/L/R/S/T e comparação com Bankai J/K. Não importar combinações raciais ou regra de Bankai por associação. BL-26/Arrancar/Hierro e BL-19/Resurrección reconsultados; limites na seção 4. Não comprovam slots, Supressão ou prioridade de custos. Sem capítulos/episódios integrais, novas APIs, implementação, build ou testes de Minecraft. Próximo: respostas, ciclo de vida, efeitos dependentes e revisão estrutural; fichas, valores, inventário e apresentação continuam pendentes. M07 segue em especificação; linhagens/artes adiadas.


### M07-06O/P aprovados; N aguarda esclarecimento — 21/10/2026

O — “Resurrección compatível com estados de Reiatsu (recomendado)”; P — “Resurrección → Liberação → ação (recomendado)”. Duas respostas próprias registradas como DECIDIDO nas seções 8.65/8.66. A–M e O/P aprovados, quinze decisões; N não integra esse conjunto.

Para N, resposta explícita “Não entendi como funcionariam as duas alternativas”. Não é aprovação nem rejeição. Seção 12.1 mantém a proposta e explica com exemplo hipotético de peitoral: A conserva efeitos elegíveis independentemente da aparência corporal; B desativa a peça que o perfil não admite, preservando o item. Recomendação A mantida; uma escolha pendente, sem nova pergunta sobre O/P.

Revisão local não identificou conflito entre O/P, A–M e M06/estados, fases, custos/tempos no recorte examinado. Fontes anteriores mantêm datas/limites; sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft. Armadura, ciclo de vida, efeitos dependentes, fichas/inventário/valores e revisão estrutural continuam pendentes. Atualizados módulo/handoff/mapa/base; linhagens/artes adiadas.


### Esclarecimento anterior de M07-06N — registro histórico de 21/10/2026

O trecho abaixo documenta a explicação anterior à escolha. Seu estado pendente foi superado pela aprovação de N na seção 8.67.

**Resposta de 21/10/2026:** “Não entendi como funcionariam as duas alternativas”. N continua **PROPOSTO, aguardando esclarecimento e escolha**, sem aprovação nem rejeição presumidas. O/P já foram aprovados nas seções 8.65/8.66; não perguntar novamente.

**Explicação para a decisão:** os quatro espaços são cabeça, peito, pernas e pés. O exemplo abaixo é hipotético, sem selecionar uma linhagem ou definir a aparência final.

| Situação: o jogador veste um peitoral e libera uma forma cujo corpo não veste um peitoral humano comum | A — equipamento continua funcionando (recomendada) | B — funcionamento depende da anatomia |
|---|---|---|
| Item | Continua no espaço de armadura, sendo o mesmo peitoral | Continua guardado no espaço correspondente, sem virar outro item |
| Defesa e encantamentos elegíveis | Continuam contribuindo, mesmo que o desenho da armadura seja ocultado/adaptado | Ficam inativos se a ficha declarar o peito incompatível; peças compatíveis continuam funcionando |
| Ao encerrar a forma | O equipamento continua normal; não houve restauração ou cópia | A peça ainda presente volta a funcionar quando o corpo-base a admite; não foi apagada |
| Efeito na escolha do poder | O formato do corpo não retira a utilidade do equipamento | A ficha de cada forma precisa informar quais peças deixam de ajudar e equilibrar essa diferença |

**Por que A:** transformar preserva o investimento no equipamento sem exigir conjuntos próprios para cada anatomia. O equilíbrio com Hierro e defesas da forma continua pela composição/limite de M06-11B; não se escolheu um valor ou imunidade. B é viável se quisermos que vestir armadura faça parte das diferenças entre corpos, mas exige conteúdo e balanceamento específicos. Em ambas, transformação não cria/repara itens; aparência não é uma segunda armadura. Para esta alternativa B, “guardado” significa permanecer no espaço correspondente, com estado inativo comunicado, sem mover automaticamente para mochila ou chão. Esse esclarecimento também é proposta, sem decisão presumida.


### M07-06N aprovado após esclarecimento — 21/10/2026

Resposta explícita: “A — Armadura continua funcionando (recomendado)”. N registrado como DECIDIDO na seção 8.67. A–P somam dezesseis decisões aprovadas; nenhuma dessas perguntas pendente. O pedido anterior de explicação e a comparação de alternativas permanecem como histórico, superados pela escolha própria.

Os quatro slots normais de armadura continuam funcionais durante Resurrección; peças equipadas protegem com encantamentos elegíveis e desgaste legítimo, mesmo com aparência adaptada/oculta. Não há cópia, reparo automático ou camada duplicada do equipamento. Hierro e defesas compatíveis seguem a composição/limite já aprovados em M06-11A/B. Não identificado conflito no recorte local examinado; a decisão não estende quatro slots a todo corpo Hollow nem aprova desenho final ou cálculo executável.

Atualizados módulo/handoff/mapa/base. Próximo: ciclo de vida, efeitos dependentes e revisão estrutural de Resurrección. Itens/slots da arma incorporada/inventário cheio, poses, mundo alterado, valores e apresentação continuam com fichas pendentes. Sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft; fontes mantêm datas/limites. M07 segue em especificação; linhagens/artes adiadas.


### M07-06Q proposto; efeitos herdados e panorama do restante — 21/10/2026

Usuário pediu continuar e entender o que falta. A–P preservados; Q propõe encerrar preparo/manifestação em morte, desconexão, dimensão e reinício, preservando conquistas e exigindo novo pedido pago. Uma resposta própria pendente na seção 12.1; não é aprovação por continuidade. Efeitos autônomos/dependentes e tempos consolidados por M06-11U/V/W e M07-06F/G/H/L/M/O/P, sem nova votação redundante. Representação da arma e política de itens na morte permanecem M11/M16; nenhuma cópia/recall/keepInventory foi escolhida.

Reconsultados BL-19/帰刃 e BL-26/破面, com limites na seção 4. Não descrevem sessões de Minecraft; Q é adaptação. Panorama na seção 12.3 mostra M07 restante, pendências de M05/M06 e documentos M08–M16 ainda não iniciados. Corrigidos resumos vigentes que ainda citavam somente A–J, além de pendência de prioridade já resolvida por P; históricos preservados. Atualizados handoff/mapa/base. Revisão dirigida, sem auditoria integral de código, pesquisa de APIs, implementação, build ou testes de Minecraft. Próximo: resposta Q, revisão e depois Quincy; linhagens/artes adiadas.


### M07-06Q aprovado e Resurrección consolidada estruturalmente — 21/10/2026

**Resposta explícita:** “Encerrar forma; retorno manual (recomendado)”. Q registrado como DECIDIDO na seção 8.68. A–Q somam dezessete decisões aprovadas; nenhuma pergunta desse conjunto pendente. A preparação anterior fica preservada como histórico. Esta atualização registra a resposta e revisa contratos locais, sem nova pesquisa canônica/API; fontes mantêm suas datas e limites.

**Escopo da revisão:** releitura dirigida de A–Q e confronto com a condição permanente de M07-05J, recursos/domínio de M05, fases e M06-11U/V/W/X/Y. Não identificado conflito estrutural que exija revogar as escolhas examinadas. Não é revisão integral do cânone, de todo M04–M06, de cada poder ou do runtime. Encerramento da manifestação não desfaz Arrancarização, não restaura recursos, não decide itens na morte nem limpa efeitos independentes.

| Situação consolidada | Resultado pelas decisões aprovadas |
|---|---|
| Ainda não conquistou a liberação | Arma/identidade não substituem preparo e demonstração; a quest compatível pode integrar a mesma conquista |
| Conquista concluída | Manifestação válida confiável, Mastery própria por prática; técnicas/versões posteriores não são concedidas em bloco |
| Pedido de liberação | Manual, arma própria compatível empunhada, dentro/fora da interface; início e conclusão verificam requisitos/espaço |
| Preparo e reação | Caminhar devagar/olhar; reação disponível cancela antes de começar sem recuperação extra; reação recusada conserva preparo ainda válido |
| Conclusão e manutenção | Ativação paga uma vez na conclusão válida; manutenção somente ativa; técnicas conservam custos próprios |
| Encerrar voluntariamente ou faltar manutenção | Voltar à base Arrancar; conservar conquistas, sem refill/refund/reset ou cancelamento voluntário de fase comprometida |
| Corpo, passagem e meios | Perfil do poder; posição válida e passagem incluindo corpo-base na postura compatível; chão/ar/água preservam trajetória, gravidade e queda |
| Arma e ferramentas | Incorporada/transformada/separada conforme ficha, sem segunda espada; itens permitidos por corpo/mãos/fase; guardar arma separada conserva forma paga |
| Perda efetiva de posse da arma separada | Encerrar manifestação; conservar conquista, sem transferir poder, recolher arma ou restaurar forma ao recuperá-la |
| Armadura equipada | Quatro slots funcionais, encantamentos elegíveis e desgaste; aparência adaptável, sem cópia/reparo ou camada repetida da mesma proteção |
| Presença e cobranças | Estados compatíveis com sinais próprios; ofensiva válida encerra Supressão. Manutenções coincidentes: Resurrección → Liberação → ação; tentar separadamente os estados válidos |
| Morte/logout/dimensão/reinício | Encerrar preparo e forma, permanecer Arrancar; retorno manual pago quando válido. M06 limpa presença transitória em Natural; conquista/domínio e tempos legítimos permanecem |
| Efeitos após encerramento | Autônomos e condições independentes seguem ficha/M06-11W; dependentes perdem requisito. Ataques efêmeros encerram no descarregamento/reinício, sem replay |

**Entregas que ainda impedem declarar fichas executáveis:**

| Entrega | Responsáveis e limite |
|---|---|
| Repertório inicial e desenvolvimento do poder | M06/M07; classificar cada técnica, fase, parte autônoma/dependente e prática válida, sem domínio por AFK/liga-desliga |
| Conteúdo de preparo, demonstração e orientação | M08/M12/M13; primeira execução possível, sem requisito circular nem prova duplicada |
| Perfis, poses e transições espaciais | M06/M07/M10/M16; volumes de ambos os corpos, postura/meio, movimento e retorno. Pistões/teleportes/novos blocos/chegada exigem contrato próprio; J não resolve toda intervenção forçada |
| Arma incorporada, inventário cheio e itens | M11/M16; mesmo vínculo/histórico, representação e retorno únicos, perda/reposição e morte. Sem slot reservado, keepInventory, recall ou indestrutibilidade escolhidos por omissão |
| Custos, curvas e viabilidade | M05/M06/M07/M15; custos/intervalos/limites e defesa composta, alteração de máximos sem refill, uso real do repertório e trajetória útil das três origens compatíveis |
| Apresentação e controles | M14; requisitos, recusas, manutenção, corpo/passagem e equipamento ativo compreensíveis para jogador/observadores; arte final permanece adiada |
| Persistência, sincronização e evidência | M16; ordenar conclusão/interrupção, cobrar/aplicar uma vez, preservar tempos, migrar sem apagar conquistas e validar cenários com cliente/servidor. Implementação/testes/aceite continuam etapas distintas |

**Condições para retornar ao bloco:** fonte decisiva contradizer uma ficha; retorno espacial exigir atravessar parede, apagar blocos ou prolongar benefício sem pagamento; inventário duplicar/perder item indevidamente; poder exigir exceção de fase/presença/posse ainda não decidida; ou comparação das trajetórias revelar uma origem inviável. Apresentar evidência e regra afetada antes de propor revisão. Não antecipar exceção para toda a raça.

**Estado:** M07-06 consolidado estruturalmente. M07 inteiro permanece EM ESPECIFICAÇÃO; custos concretos, catálogo, corpo/itens e realização técnica continuam pendentes. Próximo: abrir o bloco Quincy, pesquisando separadamente formas avançadas e capacidade individual antes de propor suas regras; depois Fullbringer e revisão comum. Linhagens/Grimmjow e artes continuam adiados. Atualizados módulo/handoff/mapa/base; sem implementação, build ou testes de Minecraft.


### Abertura M07-07 — Quincy; A/B propostos — 21/10/2026

Continuidade autorizada pelo usuário. M04/RAC-09/Q1–Q11, revisões M05/M06, M06-09C e fechamento da seção 39 orientam a abertura; Resurrección A–Q preservado. Distinções e herança na seção 8.69. A/B PROPOSTOS na seção 12.1: Vollständig como forma avançada regular compatível, Letzt Stil opcional/separado sem ser requisito; conquista de Vollständig com preparo, fonte apropriada e demonstração registrada. Duas respostas próprias pendentes; aprovação de continuar não aprova essas regras.

BL-16/Letzt Stil reconsultado diretamente; BL-23/Uryū-Mayuri recuperado no índice; BL-03/Vollständig/Schrift recuperados no índice após abertura direta 403. BL-28/entrevista Quilge consultado diretamente; contexto não é currículo. Fontes/limites na seção 4. A sinopse do episódio 3 não demonstra aprendizado; episódio 14 inacessível e não decisivo. Sem capítulo/episódio integral, nova API ou arte. Propostas são adaptações de acesso/progressão; sem taxa, multiplicador, voo, Sklaverei, perda irreversível ou recuperação escolhidos por omissão.

Próximo após respostas: acesso aos ensinamentos e relação Vollständig/Schrift; depois domínio/execução, Letzt Stil/consequências e revisão. M07 segue EM ESPECIFICAÇÃO. Atualizados módulo/handoff/mapa/base; sem implementação, build ou testes de Minecraft. Grimmjow e artes continuam adiados.


### M07-07A/B aprovados — caminhos e conquista de Vollständig — 21/10/2026

Duas respostas próprias: A — “Caminhos separados, sem Letzt Stil obrigatório (recomendado)”; B — “Preparo, aprendizado e demonstração (recomendado)”. Registradas como DECIDIDO nas seções 8.70/8.71. Nenhuma dessas perguntas pendente; preparação anterior preservada como histórico.

A organiza Vollständig como forma avançada regular compatível e Letzt Stil como opção distinta com consequência própria, sem impor usar/perder/recuperar poder antes de buscar Vollständig. B exige preparo nos fundamentos de Reishi, aprendizado específico por fonte apropriada e demonstração de manifestação/controle; conquista fica registrada e pode integrar uma quest sem outra prova equivalente. Essas aprovações não fecham fonte, acesso institucional, requisito/ordem de Schrift, identidade exigida, domínio, custos ou consequências da técnica antiga.

**Revisão dirigida:** confrontadas A/B com M04/Q4/Q6/Q7/Q8/Q11 e revisões de M05/M06, M06-09C/fechamento da seção 39 e M07-01. Não identificado conflito que exija revogar as escolhas no recorte examinado. Preservados um poder principal, independência inicial, contexto apropriado de Schrift, ausência de quarta Proficiência/compra de Mastery e aquisição sem requisito circular. Um personagem associado pode orientar, desafiar ou se opor; não foi escolhido professor amigável nem cópia do treinamento com espírito de Zanpakutō.

**Pendências explícitas:** fonte dos ensinamentos, relação de Vollständig com Schrift e facção, perfil compatível e oportunidade da primeira execução; depois manifestação/domínio, ativação, recursos, instrumentos/mãos, compatibilidades/retorno/ciclo de vida. Letzt Stil ainda precisa de execução, consequência e recuperação antes de introduzir risco. Não impor perda de conquistas, reversão racial, voo ou absorção de entidades por omissão.

Atualizados módulo/handoff/mapa/base. Sem nova pesquisa canônica/API nesta atualização; referências mantêm datas/limites. Sem implementação, build ou testes de Minecraft. M07-07 e M07 inteiro seguem EM ESPECIFICAÇÃO. Próximo: acesso aos ensinamentos e relação Vollständig/Schrift, por pesquisa/propostas próprias; linhagens/artes adiadas.


### M07-07C/D propostos — Schrift e fonte dos ensinamentos — 21/10/2026

A/B preservados. C propõe Schrift canônica compatível conquistada antes de aprender a Vollständig do mesmo poder, com treinamento/demonstração próprios; ordem de gameplay explicitamente adaptada, não requisito universal comprovado. D propõe fonte de conhecimento Quincy avançado — instrutor ou registro contextualizado — sem filiação/lealdade permanente obrigatória; marcador de entrada/saída não concede/apaga aprendizado. Duas respostas próprias pendentes na seção 12.1.

Reconsultado BL-03 pelo índice após abertura direta 403; relação forma/Schrift e contexto de elite não comprovam ordem universal ou currículo independente. BL-28 reconsultado diretamente. Sinopse do episódio 14 recuperada pelo índice apenas como contexto, personagens EN com 403 sem fundamento decisivo. Fontes/limites na seção 4. Sem capítulo/episódio integral ou nova API. C/D são adaptações propostas.

Preservados M04/Q7/Q8, um poder principal e B; não delegar a qualquer rival a transmissão literal de Schrift, tornar registros genéricos suficientes, escolher ritual universal ou apagar conquistas por facção. Conteúdo/era e aquisição da Schrift exigem integração própria M08/M12/M13/M15. Sem professor, quest ou forma individual selecionados. Próximo após respostas: domínio/confiabilidade e operação, depois Letzt Stil. M07 segue EM ESPECIFICAÇÃO; sem implementação, build ou testes. Linhagens/artes adiadas.


### M07-07C/D aprovados — ordem da conquista e fonte dos ensinamentos — 21/10/2026

Duas respostas próprias: C — “Schrift compatível antes de Vollständig (recomendado)”; D — “Fonte avançada, sem filiação permanente (recomendado)”. Registradas como DECIDIDO nas seções 8.72/8.73. A–D somam quatro decisões aprovadas; nenhuma dessas perguntas pendente. Preparação anterior preservada como histórico.

C exige a Schrift canônica compatível conquistada/vinculada antes da Vollständig do mesmo poder principal, preservando o aprendizado e demonstração específicos de B. D exige conhecimento Quincy avançado contextualizado, por instrutor competente ou registro apropriado, sem filiação/lealdade permanente obrigatória. Entrar por si só não ensina e sair por si só não apaga a aprendizagem. Ordem de conquista e meios de acesso são adaptações aprovadas; fontes não demonstram currículo universal da obra.

**Revisão dirigida:** A/B, M04/Q6/Q7/Q8 e RAC-20/RAC-23, M05/revisões de pontos e M06-09C/fechamento da seção 39. Não identificado conflito no recorte examinado. Mantidos um poder principal, contexto apropriado de Schrift, aprendizado separado e demonstração possível sem requisito circular. Treino preparatório anterior não registra Vollständig conquistada. Um registro fornece conhecimento para prática; não é concessão por clique. Um rival associado ao poder não passa a ser transmissor literal universal da Schrift.

**Fronteiras:** C/D não escolhem personagem, era, registro, rito ou missão concretos; aquisição da Schrift mantém contexto próprio M04/Q7 e M08/M12/M13. M15 precisa de disponibilidade/retomada no mundo compartilhado. Mudança de afiliação não equivale a evento efetivo de privação de poder, que ainda exige contrato próprio. Não estão decididos confiabilidade/Mastery da forma, ativação/custos/instrumentos, técnica de voo, compatibilidades, retorno/ciclo de vida ou consequência/recuperação de Letzt Stil.

Atualizados módulo/handoff/mapa/base. Sem nova pesquisa canônica/API nesta atualização de respostas; referências mantêm datas e limites. Sem implementação, build ou testes de Minecraft. M07-07 e M07 inteiro seguem EM ESPECIFICAÇÃO. Próximo: domínio e confiabilidade da manifestação, depois ativação/recursos e execução. Linhagens/artes adiadas.


### M07-07E proposto — confiabilidade e domínio — 21/10/2026

A–D preservados. E PROPOSTO na seção 12.1, uma resposta própria pendente: manifestação básica confiável após conquista, sob requisitos válidos, e Mastery própria por prática apropriada. Técnicas/versões continuam com aprendizado/domínio próprios; três Proficiências e históricos existentes preservados. Sem compra, cópia de Mastery, ganho AFK ou requisito circular. Custos, fases, instrumentos e manifestação/ciclo de vida não foram escolhidos nesta proposta.

Reconsulta BL-03 pelo índice após 403 direto; BL-28 aberto, com passagem específica recuperada pelo índice, não pela extração direta desta rodada. Limites na seção 4. Fontes não determinam probabilidade de falha ou Mastery numérica; E é adaptação proposta. Confronto documental com A–D e herança M05/M06 não identificou conflito no recorte examinado. Critérios candidatos na seção 13; sem nova pesquisa de APIs, implementação, build ou testes de Minecraft. Próximo após resposta: ativação/recursos e execução; depois Letzt Stil e revisão Quincy. Linhagens/artes adiadas.


### M07-07E aprovado — confiabilidade e domínio — 21/10/2026

Resposta própria: “Manifestação confiável + Mastery própria (recomendado)”. DECIDIDO na seção 8.74. A–E somam cinco decisões aprovadas, sem perguntas pendentes. Manifestação básica conquistada funciona sob requisitos válidos, sem sorteio de falha por baixo domínio; Mastery própria se desenvolve por prática pertinente. Técnicas/versões conservam aquisição/domínio próprios e os históricos Quincy existentes são preservados.

**Revisão dirigida:** A–D, herança M05/M06 e os limites da proposta. Não identificado conflito no recorte examinado: primeira demonstração permanece possível sem Mastery prévia da forma; não há quarta Proficiência, compra/cópia de Mastery, treino AFK ou concessão por liga/desliga sem prática. Confiabilidade não evita requisito ausente, interrupção válida ou futuros custos; não concede voo/Sklaverei ou todo o repertório. Práticas/curvas concretas ainda exigem ficha.

Atualizados módulo/handoff/mapa/base e critérios documentais, preservando registros anteriores como históricos. Sem nova consulta canônica/API nesta atualização de resposta; fontes mantêm datas/limites. Sem implementação, build ou testes de Minecraft. M07 segue EM ESPECIFICAÇÃO. Próximo: ativação manual e recursos; depois instrumentos/mãos, preparo/reações, encerramento, compatibilidades/ciclo de vida, Letzt Stil e revisão Quincy. Linhagens/artes adiadas.


### M07-07F/G propostos — ativação e recursos — 21/10/2026

A–E preservados. F/G PROPOSTOS na seção 12.1, duas respostas próprias pendentes. F recomenda pedido manual dentro/fora de Combat Mode; interface não encerra forma, arco/Blut mantêm contratos próprios. G recomenda Energia ao concluir e manutenção enquanto ativa, sem duração fixa universal; cancelamento anterior não cobra ativação e falta de manutenção termina a manifestação. Instrumentos, fases, retorno, compatibilidades e prioridade de pagamentos seguem abertos.

Confrontados M07-01/A–E, M05/seções 21–23/41–42 e M06-09/fechamento 39 e integração herdada. Propostas preservam uma reserva pessoal, Reishi ambiental, fases/tempos e identidade do poder; não igualam custos entre raças nem concedem Sklaverei ou abastecimento ilimitado. Fontes BL-03 pelo índice após 403 e BL-28 com abertura direta/trecho pelo índice não determinam cobrança ou interface; adaptação explicitada na seção 4. Sem capítulos/episódios integrais, nova API, implementação, build ou testes. Após respostas: instrumentos/mãos e execução, compatibilidades/ciclo de vida; Letzt Stil e revisão depois. Linhagens/artes adiadas.


### M07-07F/G aprovados — ativação e recursos — 22/10/2026

Duas respostas próprias: F — “Ativação manual independente da interface (recomendado)”; G — “Ativação + manutenção de Energia (recomendado)”. DECIDIDOS nas seções 8.75/8.76. Propostas de 21/10 preservadas no histórico; aprovações datadas de 22/10. A–G somam sete decisões aprovadas, sem perguntas pendentes.

**Revisão dirigida:** A–E, M07-01, recursos/Reishi de M05 e contratos herdados de M06-09/39 e M06-11X/Y. Não identificado conflito no recorte examinado: forma independente da interface não conserva arco/Blut ao sair do modo; manutenção não é suspensa por fechar a hotbar. G cobra uma vez na conclusão válida, não cobra ativação cancelada antes e encerra a manifestação sem recurso para manutenção. Não cria duração fixa universal, nova barra, Stamina adicional, refill, reset de tempos, Sklaverei ou abastecimento ilimitado. E continua sem falha aleatória por baixo domínio.

F/G não decidem foco/luva/mãos, preparo/reações, retorno concreto, compatibilidades com Blut/Liberação ou prioridade dos pagamentos coincidentes. Valores/intervalos exigem calibração com tiros, Blut e condições ambientais. Sem nova consulta canônica/API ao registrar respostas; fontes mantêm datas/limites. Atualizados módulo/handoff/mapa/base e critérios documentais. Sem implementação, build ou testes de Minecraft. Próximo: instrumentos/foco e mãos; depois execução, compatibilidades/ciclo de vida, Letzt Stil e revisão. M07 em especificação; linhagens/artes adiadas.


### M07-07H/I propostos — instrumentos e mãos — 22/10/2026

A–G preservados. H/I PROPOSTOS na seção 12.1, duas respostas próprias pendentes. H recomenda dispensar foco/luva/arco como requisitos universais da forma, mantendo instrumentos próprios de cada poder e o foco do arco básico. I recomenda mãos conforme arma/técnica, permitindo itens compatíveis durante a forma paga, sem simultaneidade com arco de duas mãos, guarda automática ou reset de fases/tempos.

Reconsultado BL-16 diretamente: Cruz e Sanrei/Letzt Stil, arquivo da época de exibição; BL-03 por entradas dirigidas no índice após 403 direto. Não comprovado requisito instrumental universal para Vollständig, sem converter essa limitação em prova de ausência em todas as cenas. H/I são adaptações propostas. Preservados M06-09J/L/T e F/G; não selecionados equipamentos avançados, gestos de preparo, armadura universal ou instrumentos de linhagem. M11/M14/M16 completarão representação, feedback e realização técnica. Próximo após respostas: preparo/reações e retorno, compatibilidades/corpo/equipamentos/ciclo de vida, Letzt Stil e revisão. Sem capítulo/episódio integral, novas APIs, implementação, build ou testes. Linhagens/artes adiadas.


### M07-07H/I aprovados — instrumentos e mãos — 22/10/2026

Duas respostas próprias: H — “Sem instrumento universal; requisitos por poder (recomendado)”; I — “Permitir itens compatíveis durante a forma (recomendado)”. DECIDIDOS nas seções 8.77/8.78. A–I somam nove decisões aprovadas, sem perguntas pendentes. H dispensa foco/luva/arco como requisitos universais da forma, com exceções justificadas por ficha. I permite itens compatíveis durante a forma paga, respeitando corpo, mãos e fase.

**Revisão dirigida:** A–G e contratos herdados M06-09J/L/T. Não identificado conflito no recorte examinado: foco permanece exigido pelo arco básico, mesmo com Vollständig válida; perda desse foco não encerra automaticamente uma forma que não o exige. Arco ocupa duas mãos, guardar é manual e item incompatível não é usado simultaneamente. Troca não apaga tempos/ataques comprometidos, não duplica/repara itens nem suspende manutenção. Nenhuma arma é manifestada automaticamente por H/I. Requisito especial de instrumento/arma precisa de ficha e comportamento de perda próprios.

H/I não resolvem gestos/mãos durante o preparo, armadura/encantamentos de todos os perfis, voo/Sklaverei, encerramento/retorno, compatibilidades/prioridade dos pagamentos ou ciclo de vida. Letzt Stil mantém análise separada. Atualizados módulo/handoff/mapa/base e critérios documentais. Sem nova consulta canônica/API ao registrar respostas; fontes conservam datas/limites. Sem implementação, build ou testes de Minecraft. Próximo: preparo/reações e retorno; depois compatibilidades/corpo/equipamentos/ciclo de vida, Letzt Stil e revisão. M07 em especificação; linhagens/artes adiadas.


### M07-07J/K/L propostos — preparo, encerramento e transições — 22/10/2026

A–I preservados. J/K/L PROPOSTOS na seção 12.1, três respostas próprias pendentes. J recomenda preparo breve com caminhada lenta e cancelamento para reação disponível, sem recuperação extra; mãos vazias não são exigência universal, mas não há uso simultâneo de item/tiro/canal ou cancelamento gratuito da ação anterior. K recomenda encerramento voluntário quando a fase permitir, retorno Quincy sem Vollständig e preservação de Schrift/conquistas. L recomenda transições sem taxa/recuperação extra, preservando fases e tempos anteriores.

Reconsultados BL-03 pelo índice após 403 e BL-28/passagem pelo índice. Não determinam cancelamento, retorno voluntário ou Recovery universais; propostas são adaptações, sem importar consequência de Letzt Stil ou regras de outras raças por associação. Confrontados A–I, M06-02S/T/U, Hirenkyaku e efeitos autônomos herdados. Valores, meios/corpo/espaço, equipamento, compatibilidades/prioridade de pagamentos e ciclo de vida continuam abertos. Após respostas: esses contratos, Letzt Stil e revisão. Sem capítulo/episódio integral, novas APIs, implementação, build ou testes. Linhagens/artes adiadas.


### M07-07J/K/L aprovados — preparo, encerramento e transições — 22/10/2026

Três respostas próprias: J — “Preparo breve cancelável para reagir (recomendado)”; K — “Encerrar manualmente e preservar conquistas (recomendado)”; L — “Transições sem recuperação adicional (recomendado)”. DECIDIDOS nas seções 8.79–8.81. A–L somam doze decisões aprovadas, sem perguntas pendentes.

**Revisão dirigida:** A–I, M06-02S/T/U, permissões de Hirenkyaku e herança M06-11V/W. Não identificado conflito no recorte examinado. J mantém preparo breve/caminhada lenta, cancelamento para reação disponível sem recuperação extra e preservação do preparo válido diante de pedido recusado. Não exige mãos vazias universalmente, não mantém duas execuções ou cancela gratuitamente uma ação anterior; custos e fases prévios persistem. K permite encerramento manual quando a fase autoriza, volta a Quincy sem Vollständig e conserva Schrift/conquistas, sem refill ou retorno automático ao regenerar. L não acrescenta taxa/recuperação à transição, mantendo cobrança de G e tempos anteriores.

Corpo/espaço/arma e efeitos concretos do retorno, meios ambientais, compatibilidades com Blut/Reiatsu, prioridade das manutenções, demais equipamentos e ciclo de vida continuam abertos. Letzt Stil mantém consequência/recuperação separadas; K não importa perda de poder dessa técnica. Atualizados módulo/handoff/mapa/base e critérios documentais. Sem nova consulta canônica/API ao registrar respostas; fontes mantêm datas/limites. Sem implementação, build ou testes de Minecraft. Próximo: compatibilidades/prioridade das manutenções; depois corpo/equipamentos/ciclo de vida, Letzt Stil e revisão. M07 em especificação; linhagens/artes adiadas.


### M07-07M/N/O propostos — Blut, presença e pagamentos — 22/10/2026

A–L preservados. M/N/O PROPOSTOS na seção 12.1, três respostas próprias pendentes. M recomenda uma aplicação aprendida de Blut com Vollständig, custos/limites próprios e exclusão Vene/Arterie. N recomenda forma com Natural/Supressão/Liberação, sinais visuais preservados e ofensiva válida encerrando Supressão. O recomenda Vollständig → Blut → Liberação → ação para manutenções coincidentes, com revalidação e tentativa separada dos estados ainda válidos.

Reconsultado BL-03 por entradas dirigidas no índice após 403: Blut distingue aplicações exclusivas, forma é capacidade distinta; não prova todas as coexistências ou custos. BL-28 reconsultado diretamente, linha 76, sem regra universal de simultaneidade. M/N/O são adaptações propostas. M06-11S/T/R e M06-09/recorte de Arterie preservados; O insere forma ativa antes do Blut, sem alterar Blut antes de Liberação nem manutenção antes da ação. Ativação inicial continua custo de ação na conclusão, depois de obrigações devidas. Valores, exceções, meios/corpo/espaço, equipamentos e ciclo de vida abertos. Após respostas: esses contratos, Letzt Stil e revisão. Sem capítulo/episódio integral, nova API, implementação, build ou testes. Linhagens/artes adiadas.


### M07-07M/N/O aprovados — Blut, presença e pagamentos — 22/10/2026

Três respostas próprias: M — “Vollständig com uma aplicação de Blut (recomendado)”; N — “Vollständig compatível com estados de Reiatsu (recomendado)”; O — “Vollständig → Blut → Liberação → ação (recomendado)”. DECIDIDOS nas seções 8.82–8.84. A–O somam quinze decisões aprovadas, sem perguntas pendentes.

**Revisão dirigida:** A–L, M06-09/limites de Blut, M06-11R/S/T e herança de fases/recursos. Não identificado conflito no recorte examinado. M conserva apenas Vene ou Arterie aprendida, limites e manutenção próprios; sair do modo ainda desliga Blut. N conserva exclusividade dos estados de Reiatsu, sinais visuais e quebra de Supressão por ofensiva válida, sem invisibilidade ou ocultação garantida. Efeito ofensivo integrado à forma precisa de classificação própria.

O insere a manutenção de Vollständig ativa antes do Blut, conservando Blut antes de Liberação e manutenção antes da ação. Sem forma ativa/devida, M06-11T permanece. Cobrar somente obrigações devidas, tentar separadamente estados ainda válidos, sem pagamento parcial/antecipado ou reativação automática. Ação é revalidada após perda da forma. Ativação inicial de G continua custo de ação na conclusão, depois de manutenções já devidas; não cobra manutenção de uma forma ainda em preparo. Valores/intervalos exigem calibração.

Atualizados módulo/handoff/mapa/base e critérios documentais. Sem nova consulta canônica/API ao registrar respostas; fontes mantêm datas/limites. Meios/corpo/espaço/armamento/equipamentos e ciclo de vida continuam abertos, depois Letzt Stil e revisão. Sem implementação, build ou testes de Minecraft. M07 em especificação; linhagens/artes adiadas.


### M07-07P/Q/R propostos — meios, espaço e armadura — 22/10/2026

A–O preservados, quinze decisões aprovadas. P/Q/R PROPOSTOS na seção 12.1, três respostas próprias pendentes. P recomenda manifestar/encerrar no chão, ar e água válidos, preservando trajetória/gravidade/queda/riscos. Q recomenda espaço do perfil funcional antes do preparo e na conclusão, no local real, sem reposicionamento automático; distingue corpo e apresentação. R recomenda quatro slots normais de armadura funcionais, mesmos itens/desgaste e encantamentos elegíveis, apresentação adaptável. Adaptações próprias de Quincy; nenhuma aprovação por associação com Resurrección.

BL-03 reconsultado pelo índice após 403 direto, entrada pertinente de Vollständig sobre variação entre usuários; BL-28 com nova abertura direta. Fontes não fornecem colisão, meios ou armadura Minecraft. Plataforma local reconfirmada sem alteração; sem capítulos/episódios integrais, nova API ou artes. Confrontados A–O, M06-11A/B, mãos/arco e contratos do preparo: propostas não concedem voo, item duplicado, proteção adicional por malha ou nova taxa. Critérios candidatos na seção 13, sem execução. Passagem/retorno seguro continua pendência própria antes de entregar corpos alterados; depois dependências de armamento e ciclo de vida, Letzt Stil e revisão. M07 em especificação; nenhuma implementação, build ou teste. Atualizados módulo/handoff/mapa/base; linhagens/artes adiadas.


### M07-07P/Q/R aprovados — meios, espaço e armadura — 22/10/2026

Três respostas próprias: P — “Chão, ar e água, preservando queda e riscos (recomendado)”; Q — “Exigir espaço na posição atual (recomendado)”; R — “Armadura equipada continua funcionando (recomendado)”. DECIDIDOS nas seções 8.85–8.87. A–R somam dezoito decisões aprovadas, sem perguntas pendentes. Preparação anterior preservada como histórico, sem mudar a data/limite da pesquisa.

**Revisão dirigida:** A–O, preparo J, cobrança G/O, retorno K/L, mãos/equipamento H/I e composição defensiva M06-11A/B. Não identificado conflito no recorte examinado. P permite chão/ar/água sem transformar a manifestação em sustentação, voo, respiração ou limpeza da queda. Q confere espaço do perfil funcional no início/conclusão, usando posição real e conservando corpo anterior/conquistas sem cobrar ativação se inválido. Adornos apenas visuais não ampliam automaticamente colisão/alcance; corpo funcional e regiões de acerto exigem ficha. R conserva quatro slots e os mesmos itens/desgaste/encantamentos elegíveis, sem duplicação, reparo ou segunda proteção por aparência. Remover/quebrar equipamento muda a contribuição real; Vene e demais defesas mantêm seus contratos e calibração pendente.

**Pendências:** passagem/retorno seguro, medidas/poses, intervenções forçadas do mundo, dependências de armamento e ciclo de vida da forma. Q não aprova por associação a regra de passagem de Resurrección J; a solução precisa existir antes de entregar perfis com mudança de corpo. Voo/Sklaverei seguem no horizonte com pesquisa/fichas próprias; valores, fontes de aprendizagem e conteúdo permanecem nos módulos responsáveis. Atualizados módulo/handoff/mapa/base. Sem nova consulta canônica/API ao registrar respostas, sem implementação, build ou testes de Minecraft. M07 permanece EM ESPECIFICAÇÃO; próximo passagem/retorno, armamento/ciclo de vida, Letzt Stil e revisão. Linhagens/artes adiadas.


### Consolidação dos contratos comuns e mudança do método — 22/10/2026

O usuário apontou repetição de decisões que poderiam ser gerais e pediu avanço com a mesma qualidade documental. Adotado o método contrato comum → perfil → exceções justificadas nas seções 7.1/7.2 e no mapa/skill/handoff. Consolidadas as regras já aprovadas com cobertura e origem explícitas; preservados todos os registros de respostas, IDs, datas e histórico. A matriz mantém diferenças de custo, natureza permanente/temporária, arma, retorno e consequência. Não transformar o custo de Shikai em manutenção, o retorno de Bankai em base genérica, evolução em botão temporário ou armadura Resurrección/Quincy em concessão a toda anatomia.

Duas ampliações de produto propostas uma única vez: G01 ciclo de vida padrão para manifestações temporárias, incluindo Vollständig e futuros perfis pertinentes; G02 passagem compatível com corpo-base para mudanças temporárias de corpo. Não aprovadas pela solicitação de melhorar o método; aguardam respostas na seção 12.1. Dependências de armamento seguem C02/C09 e H/I, sem nova votação genérica: declarar qual capacidade exige o instrumento e invalidar só as dependências reais. Revisão documental não identificou conflito no recorte consolidado; exceções e cobertura continuam explícitas. Sem consulta canônica/API nova, pois esta rodada consolida adaptações e contratos locais; sem implementação, build ou testes. Próximo registrar as ampliações, revisar Vollständig e seguir as diferenças próprias de Letzt Stil/Fullbringer. Fichas/evidência técnica pendentes.


### G01/G02 aprovados; Vollständig consolidado estruturalmente — 22/10/2026

Respostas próprias: G01 — “Encerrar formas temporárias; retorno manual (recomendado)”; G02 — “Preservar espaço para o corpo-base (recomendado)”. DECIDIDOS nas seções 7.3/7.4. Padrões agora cobrem Vollständig e futuros perfis temporários pertinentes; não pedir novamente os mesmos eventos/solução de passagem. Aprovações específicas anteriores e registros históricos preservados.

G01 encerra preparo/manifestação temporária no ciclo de vida, preserva condições/conquistas e exige nova ativação manual paga. G02 preserva espaço da base durante passagem de mudança corporal temporária, com perda assumida de acesso a túneis menores. Evoluções permanentes permanecem; consequências especiais não são curadas por relogar. Medidas/poses, alterações forçadas do mundo, itens/respawn e exceções continuam com fichas/responsáveis.

Revisão estrutural de Vollständig na seção 15: A–R + G01/G02 cobrem aquisição/domínio, execução/custos, instrumentos/mãos, reações, presença/Blut, corpo/armadura, saída/ciclo de vida e herança de tempos/efeitos. Não identificado conflito no recorte examinado; nenhum cenário executado. Subbloco consolidado estruturalmente, com entregas e condições de retorno explícitas; todo o caminho Quincy ainda não está fechado, pois Letzt Stil tem contrato próprio pendente. Atualizados módulo/handoff/mapa/base, sem nova consulta canônica/API, implementação, build ou testes. Próximo Letzt Stil por diferenças, depois revisão Quincy, Fullbringer e revisão comum. Método de contratos comuns mantido.


### M07-08A/B/C propostos — Letzt Stil em um pacote — 22/10/2026

Pesquisados diretamente BL-16/entrada Letzt Stil e BL-23/retrospectiva Uryū–Mayuri; BL-03/entrada dirigida de Vollständig pelo índice após 403 direto. São resumos oficiais; nenhum capítulo/episódio integral lido. Sanrei/retirada/perda e recuperação têm suporte no recorte; acesso sem Schrift, duração/custos, coexistências, bloqueios e repetibilidade são adaptações propostas. Sem incorporar prazos literais, procedimento anatômico ou entradas mutáveis não pertinentes.

Seção 12.1 contém A aprendizado tradicional, B janela breve com consequência assumida só na conclusão válida e C perda de acesso Quincy recuperável sem apagar desenvolvimento. B inclui o perfil operacional por contratos, sem uma votação de cada detalhe já comum. G01/G02 preservados, sem curar consequência especial; Vollständig mantém fechamento estrutural. Três respostas pendentes; continuar não aprova o pacote. Critérios candidatos e responsáveis registrados. Fontes, prontidão/representação do objeto Sanrei, números, benefícios e recuperação concreta exigem fichas antes da entrega. Atualizados módulo/handoff/mapa/base. Sem implementação, build ou testes. Próximo registrar respostas e revisar apenas diferenças próprias restantes, depois Quincy/Fullbringer/revisão comum; linhagens/artes adiadas.


### M07-08A/B/C aprovados; revisão estrutural das formas Quincy — 22/10/2026

Respostas explícitas: “Aprendizado próprio com Sanrei (recomendado)”; “Janela breve com compromisso (recomendado)”; “Perda recuperável por procedimento próprio (recomendado)”. Registradas em 8.89–8.91, contexto 8.88. Proposta original preservada em historico/2026-10-22-proposta-letzt-stil.md. Cobertura comum de B adotada com diferenças de duração/compromisso; nenhuma pergunta A/B/C pendente. A mantém treino próprio e conhecimento prévio de recuperação; B cobra na conclusão e não mantém consumo básico, limita janela e impede coexistência com Vollständig; C suspende acesso às capacidades Quincy sem apagar desenvolvimento, exige recuperação e novo preparo Sanrei para repetir.

Revisão dirigida na seção 15 não identificou conflito entre A–C, Vollständig A–R, G01/G02 e os contratos comuns examinados. Formas Quincy de M07 consolidadas estruturalmente, com entregas de Sanrei/recuperação concreta, duração/custos/benefícios, práticas/compatibilidades, UI e dados/persistência explicitamente delegadas. Não entregar risco sem via de recuperação viável; nenhuma ficha/quest/valor foi inventado para fechar o bloco. Fontes conservam datas e limites da pesquisa anterior; sem nova consulta canônica/API ao registrar respostas. Nenhuma implementação, build ou teste executado. Próximo Fullbringer por diferenças, depois revisão comum. Atualizados módulo/handoff/mapa/base; linhagens/artes adiadas.


### M07-09A/B propostos — Fullbringer por diferenças — 22/10/2026

Reconsultados BL-11/Fullbring e BL-17/treinamento diretamente; novas fontes BL-29, apresentação editorial Shonen Jump com texto agora legível; BL-30, sinopse oficial TV Tokyo 358; BL-31, fichas licenciadas de vetores/identidade, sem importar gameplay de Brave Souls. BL-05 retornou 403. Sem capítulo/episódio/novel integral, catálogo de formas futuras ou nova API. Plataforma local reconfirmada. Preservadas datas/limites das consultas anteriores, inclusive a tentativa histórica de BL-29 que retornou essencialmente imagens.

Pacote em 12.1: A completude por marcos do poder com Mastery contínua; B custo por manifestação/aplicação, sem transformação/manutenção universal. Duas respostas pendentes. F4/F12/F13, M06-10A–F e C/G01/G02 são referências herdadas; parte operacional adicional de B está proposta, não aprovada pelo pedido de continuar. Não criar novo objeto de prólogo, poder pessoal original ou replicar jornada excepcional de Ichigo para todos. Vetor/perda/reposição, troca do principal, prioridades entre efeitos sustentados e fichas permanecem com responsáveis/continuidade indicada; Fullbringer/M07 não encerrados. Atualizados módulo/handoff/mapa/base. Sem implementação, build ou testes. Depois respostas, diferenças reais restantes e revisão comum; linhagens/artes adiadas.


### Fullbringer A/B aprovados; revisão de vetor e proposta C — 22/10/2026

Respostas explícitas “Marcos próprios e Mastery contínua (recomendado)” e “Custos por manifestação e técnica (recomendado)”, registradas em 8.93/8.94 com perguntas e data; contexto 8.92. Proposta original preservada em historico/2026-10-22-proposta-fullbringer-ab.md. Cobertura operacional pertinente de B registrada nos contratos comuns; não foi aprovada transformação/manutenção universal ou domínio novo por rótulo de estágio. Revisão dirigida de A/B/vetor na seção 15, sem conflito identificado no recorte e sem cenário executado. Vínculo, disponibilidade e domínio são campos distintos; fichas concretas de guardar/perder/repor permanecem com M07/M11/M16, sem nova pergunta genérica.

C PROPOSTO na seção 12.1: processo contextual fora de combate e histórico de domínio por poder, sem conversão/acesso simultâneo, sem troca por item/menu. Uma resposta pendente; Fullbringer/M07 não fechados. Reconsultado BL-17 diretamente para delimitar eventos específicos de transferência/perda; não demonstra troca universal voluntária. Proposta é adaptação do mod, não método canônico. Fontes anteriores conservam limites/datas; sem capítulo/episódio integral ou nova API. Atualizados módulo/handoff/mapa/base e critérios; nenhuma implementação, build ou teste. Próximo resposta C, diferenças reais restantes e revisão comum; linhagens/artes adiadas.


### Fullbringer C aprovado; fechamento estrutural e conceitual de M07 — 22/10/2026

Resposta explícita “Troca contextual, preservando domínio por poder (recomendado)”, registrada em 8.95 com pergunta/data. Proposta original arquivada em historico/2026-10-22-proposta-fullbringer-c.md. Fullbringer A–C consolidado estruturalmente, sem pergunta pendente: um principal por vez, domínio específico preservado/indisponível no histórico, sem cópia, acesso simultâneo, reset de recursos ou troca por item/menu; reacesso contextual. Regras de origem/fundamentos e perfil de cada vetor preservadas.

Revisão comum dirigida na seção 15 não identificou conflito impeditivo no recorte examinado. M07 passa a ESPECIFICADO CONCEITUALMENTE, sem pergunta geral atual; não é especificação executável do catálogo, implementação/teste/aceite. Consolidada lista de entregas em 12.2: valores/conversão de máximos, práticas/provas/recuperação, corpos/intervenções do mundo, objetos/vetores, efeitos persistentes/ordenação específica, UI/dados/migração/validação. Prioridades já aprovadas preservadas; nenhuma ordem universal inédita ou nova combinação de poderes aprovada por fechamento. Exceções/estágios/híbridos futuros permanecem no horizonte com fonte/decisão/ficha próprias. Próximo M08 — NPCs, mentores e facções. Sem nova pesquisa canônica/API, código, build ou teste; pesquisas mantêm datas/limites. Atualizados módulo/handoff/mapa/base; linhagens/artes adiadas.

#### Registro anterior de pendências executáveis — preservado para histórico

### 12.2. Pendências executáveis e de conteúdo

Evolução Hollow A–M mantém seu fechamento e as entregas da seção 15. Para Arrancarização ainda faltam conteúdo/fontes do acontecimento, perfis e matriz concreta de compatibilidade, viabilidade de cada origem, arma com inventário cheio/posse/perda/reposição, detalhamento espacial e de apoios/meios, compatibilidade com ações/efeitos sustentados, custos/valores das capacidades e desafios, conversão de máximos e realização técnica. G resolve a ausência de taxa/manutenção adicional da condição; H/I/J definem execução/espaço/interrupção e ciclo de vida básicos, sem preencher essas fichas. M07-06 tem A–Q aprovados e fechamento estrutural, com dezessete decisões sem perguntas pendentes. N preserva os quatro slots normais de armadura funcionais, com aparência adaptável e os mesmos itens/desgaste. E/F/G resolvem preparo cancelável, retorno básico e recuperação das transições; H/I/J resolvem arma/itens compatíveis e regras gerais de espaço na entrada/passagem. K/L/M resolvem meios básicos e guardar/perder a arma separada. Conteúdo de conquista/prática, repertório inicial, perfis/medidas/poses concretas, representação de item incorporado/slots/inventário cheio e da perda/retorno, equipamentos restantes, intervenções forçadas do mundo, efeitos/ações dependentes, valores/intervalos e execução técnica do ciclo de vida permanecem pendentes. Q resolve sua política básica. O/P aprovam convivência básica com estados de Reiatsu e prioridade Resurrección → Liberação → ação; demais efeitos sustentados exigem seus contratos. M07 inteiro segue `EM ESPECIFICAÇÃO`.

