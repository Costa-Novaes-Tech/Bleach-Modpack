# M13 — Campanhas e sidequests

> **Alvo da abertura morto sem vitória válida — 30/10/2026:** em MAIN-SUB-01, se o Fishbone morrer sem validar a vitória do jogador proprietário, reiniciar somente a etapa do confronto. Preservar a introdução e permitir nova tentativa com Rukia/Journal. Não conceder conclusão por lava, morte por outro jogador ou ausência do alvo. Interagir com a mesma Rukia também permite recuperar uma tentativa antiga sem boss vivo carregado. A vitória legítima mantém a etapa de retorno.

> **Retomada de MAIN-SUB-01 após morte — 30/10/2026:** preservar as etapas já concluídas dessa missão. Morte no boss reinicia somente o confronto; morte no retorno mantém a vitória. Não exigir novo aceite ou repetir a introdução. Esta política é exclusiva da primeira quest da Rukia; as demais missões conservam suas próprias regras de falha.

> **Revisão de implementação solicitada em 30/10/2026 — MAIN-SUB-01:** aceitar a abertura pelo Mission Journal → localizar a primeira Rukia pela bússola → diálogo sobre Hollows/Shinigamis → confronto com Hollow Boss → retornar à mesma Rukia → diálogo de Shinigami temporário e entrega da Asauchi → conclusão e disponibilidade de MAIN-SUB-02. As etapas futuras ficam ocultas até concluir a anterior. Esta direção revisa somente a abertura; as fichas e decisões de 28/10 abaixo ficam como histórico no que divergirem. [Fluxo atual e migração](../planejamento/implementacao-campanha-shinigami-2026-10-29.md).

## 1. Estado do módulo

| Campo | Estado em 28/10/2026 |
|---|---|
| Estado | `EM ESPECIFICAÇÃO` |
| Foco autorizado | Somente a campanha principal, pelos acontecimentos principais da obra |
| Primeiro bloco | Shinigami Substituto e Soul Society: seleção/estrutura do roteiro concluídas; realização pendente |
| Adiado pelo usuário | Quests raciais/formação, fillers, demais histórias laterais e continuidade do detalhamento M11 |
| Contratos aprovados | 01A/02A: execução/início; 03A/B: abertura/conclusão; 04A/B: Orihime; 05A/B: Shrieker/Kon; 06A: Grand Fisher; 07A/B: Don Kanonji/Quincy; 08A: Renji; 08B: avaliação com Urahara; 08C: perseguição de Ururu; 09A/B: valores provisórios e retenção da etapa; 10A/B: travessia breve e Jidanbō/Gin; 11A/B: incluir Ganju e exercício/lançamento com Kūkaku; 12A/B: vitória de Ganju e sequência Ikkaku/Yumichika; 13A/B: Jirōbō e rota de Hanatarō sem escolta; 14A/B: Renji/interlúdio de Aizen e Shunsui jogável; 15A/B: contato na prisão/Byakuya em texto e Mayuri jogável; 16A/B: preparação própria e Tōsen/Komamura separados; 17A/B: circuito de Yoruichi e libertação de Rukia; 18A/B: Byakuya contínuo e Soi Fon jogável/Yamamoto em texto; 19A: ocorrências finais em contexto; 19B revisado: Aizen em contexto, Byakuya último boss |
| Conteúdo abaixo | 28 missões selecionadas/estruturadas nos dois primeiros arcos, com desfecho de Aizen em contexto. Ordem consolidada em 10.32; não são fichas executáveis nem conteúdo implementado |
| Continuidade | 19B aprovado com revisão: Aizen mantém superioridade/saída com Hōgyoku, sem boss aqui; Byakuya último boss. Fechamento em 10.32/10.33; próximo arco principal ainda por selecionar em rodada própria |

**Direção literal do usuário:** “Calma, vamos começar com a campanha principal, então as sidequests como as quests raciais, quests fillers que seriam as side quests reais e etc fica pra depois”. Isso muda a prioridade de trabalho; não reclassifica automaticamente toda missão racial como opcional ou apaga STR-02/08. Não voltar à formação para começar a discussão da campanha principal.

## 2. Objetivo

Transformar os acontecimentos principais de Bleach em missões que o jogador inicia e executa. Apresentação breve do NPC e contexto escrito sustentam objetivo/confronto; não exigir assistir à reencenação de encontros entre personagens antes de jogar. Parâmetros de itens e detalhes do sistema de quests continuam dependências posteriores da realização.

## 3. Fantasia do jogador

Conhecer personagens e conflitos reconhecíveis, enfrentar os inimigos com o próprio personagem/party e acompanhar a escalada da obra. O jogador permanece original; não passa a ser Ichigo nem herda sua família, origem ou revelações pessoais.

## 4. Base canônica e pesquisa

Herdar D05 e STR-01–08 de [M03](03-storyline-macro.md): mangá como base, complementos oficiais identificados, fillers opcionais. Consultas abaixo em 28/10/2026 são sinopses oficiais do anime, acessíveis; não equivalem à leitura integral dos capítulos ou episódios e não decidem sozinhas a classificação editorial de todo conteúdo de um episódio.

| Fonte | Uso nesta preparação | Limite |
|---|---|---|
| CAMP-S01 — [TV Tokyo, episódios 1–13](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes01/index.html) | Referência de encontro Ichigo/Rukia, caso do irmão de Orihime, Chad/Shrieker, Urahara/alma modificada, Grand Fisher e aproximação de Uryū | Resumos oficiais do anime; não importar todo detalhe adicional como mangá-base |
| CAMP-S02 — [TV Tokyo, episódios 14–25](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes02/index.html) | Escalada dos Hollows, chegada de Renji/Byakuya e preparação para buscar Rukia | Referência de sequência/contexto; detalhes das lutas e da adaptação exigem fichas |
| CAMP-S03 — [Pierrot, retrospectiva do arco Substituto, episódios 1–20](https://pierrot.jp/title/bleach/playback_01.html) | Resolução do irmão de Orihime; contexto do periquito e destino do Hollow ao Inferno por crimes em vida; origem de Kon e seu acolhimento; Grand Fisher escapa do primeiro confronto | Resumo oficial do anime consultado em 28/10/2026; não é capítulo integral nem comprovação do repertório completo do inimigo |
| CAMP-S04 — [Shueisha, sinopse editorial de BLEACH volume 4](https://www.shueisha.co.jp/books/items/contents_amp.html?isbn=4-08-873310-X&title=www.shueisha.co.jp%2Fbooks) | Trecho oficial indexado situa Don Kanonji em Karakura e a gravação no hospital abandonado; sustenta que o caso pertence ao mangá, em conjunto com CAMP-S01/episódio 10 para o contexto do anime | Consulta 28/10/2026: duas aberturas falharam; acesso apenas ao trecho indexado, sem páginas/capítulo integral. Não sustenta repertório completo ou desfecho detalhado |
| CAMP-S05 — [KLab, ficha oficial de Ururu Tsumugiya em BLEACH Brave Souls](https://www.bleach-bravesouls.com/character/ururu.html) | Identificação de Ururu e vínculo com a loja de Urahara | Complemento licenciado consultado em 28/10/2026; texto disponível no índice e identificação na página. Não importar mecânicas do jogo, capacidades posteriores ou alegar que a ficha comprova a cena de treino. CAMP-S02/18–20 confirma o contexto geral, sem descrever a perseguição em detalhe |
| CAMP-S06 — [Pierrot, retrospectiva da infiltração na Soul Society, episódios 21–41](https://pierrot.jp/title/bleach/playback_02.html) | Chegada ao Rukongai, vitória sobre Jidanbō, entrada fechada por Gin e busca de Kūkaku por Yoruichi | Sinopse oficial do anime consultada em 28/10/2026; confirma sequência e resultados, sem leitura integral do mangá nem repertório completo do guardião |
| CAMP-S07 — [TV Tokyo, episódios 26–38](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes03/index.html) | Entrada/separação do grupo e confrontos Ikkaku/Ichigo e Yumichika/Ganju, especialmente 26–28 | Sinopses oficiais do anime consultadas em 28/10/2026, não capítulo/episódio integral; não confirmam repertório completo. Ordem sequencial para um jogador é adaptação proposta, não ordem literal das lutas paralelas |
| CAMP-S08 — [TV Tokyo, episódios 39–51](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes04/index.html) | Complemento de continuidade: fim do confronto com Kenpachi e avanço à prisão; repertório posterior não é importado ao bloco atual | Sinopses oficiais do anime consultadas em 28/10/2026, sem capítulo/episódio integral. Cobertura das quests posteriores ainda aberta; leitura da página não aprova todas as suas cenas |
| CAMP-S09 — [Pierrot, retrospectiva do resgate na Soul Society, episódios 42–63](https://pierrot.jp/title/bleach/playback_03.html) | Interrupção do primeiro reencontro com Byakuya por Yoruichi; Mayuri/Uryū e recuo de Mayuri; localização posterior do confronto principal de Byakuya | Sinopse oficial do anime consultada em 28/10/2026, sem capítulo/episódio integral. Não aprova todo o elenco, técnicas ou eventos jogáveis do restante do arco |
| CAMP-S10 — [TV Tokyo, episódios 52–62](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes05/index.html) | Renji/Byakuya antes da execução, frente Kenpachi/Tōsen/Komamura e início da execução/resgate; horizonte dos confrontos finais | Sinopses oficiais do anime consultadas em 28/10/2026; não capítulos/episódios integrais. Tōsen usa Bankai no episódio 53; repertórios e resultados do mod exigem adaptação/ficha, sem aprovar automaticamente toda luta paralela |

O esqueleto abaixo seleciona acontecimentos, sem copiar diálogos ou supor que cada episódio corresponda a uma quest. Métodos de produção, poderes/estados exatos dos bosses e resultados particulares precisam de fonte apropriada quando forem detalhados.

## 5. Adaptação para Minecraft

Aplicar STR-03: jogador/party vence os confrontos canônicos adaptados, sem aliados canônicos lutando ou tomando a vitória. Preservar a sequência e os marcos necessários da obra, distinguindo o contexto original da execução jogável. Não impor derrota inevitável como se fosse boss vencível nem transferir relações familiares do elenco ao jogador. A solução de cada confronto ainda será escrita; a tabela não constitui aprovação de todas as cenas ou versões de inimigos.

Mundo aberto, construção e exploração continuam entre missões. O início da campanha não é uma nova votação de raça, poder ou tutorial de formação. Requisitos de entrada por personagem ficam identificados para integração futura, sem desenvolver as campanhas raciais nesta rodada.

## 6. Escopo

**Agora:** campanha principal; divisão em arcos, acontecimentos selecionados, participação do jogador, elenco, objetivos e sequência. Primeiro aprofundamento no arco Shinigami Substituto.

**Depois:** campanhas raciais e formação, fillers, side quests independentes, preços/receitas, recompensas numéricas, diálogos finais, sistema executável de quests, sincronização de era/party e implementação. M11 permanece incompleto e adiado por solicitação explícita, sem bloquear este trabalho de roteiro.

## 7. Modelo conceitual

`Arco principal → bloco de acontecimento → uma ou mais missões → objetivo jogável → resultado registrado → próximo acontecimento`.

Cada ficha indicará ID, categoria, procedência, era, NPC/fonte, modo de iniciar (NPC ou painel/HUD, conforme evento), local válido, objetivo/ação do jogador, confronto/estado do inimigo, conclusão, falha/retomada e efeitos persistentes. Categorias e autoridade de progresso continuam com M03/M12/M15; não criar outro registro paralelo de quests fora deste módulo por conveniência.

## 8. Regras principais

1. Trabalhar apenas a história principal nesta etapa.
2. Ordem macro herdada de M03: Shinigami Substituto → Soul Society → conflitos Arrancar/Hueco Mundo/Karakura → Agente Perdido → Guerra Sangrenta; pós-guerra e expansões mantêm os limites de STR-07.
3. “Shinigami Substituto” nomeia o período da obra, não uma raça/origem obrigatória do jogador.
4. Personagens canônicos contextualizam; o jogador/party conquista a vitória nos encontros adaptados.
5. Não conceder automaticamente skills, formas ou Mastery por diálogo/recompensa narrativa; preservar aquisição SP e requisitos próprios.
6. Não decidir filler, formação, perda de itens ou números de combate para resolver uma lacuna de roteiro nesta rodada.

### 8.1. M13-01A/M13-02A — Missão executada pelo jogador e início deliberado — DECIDIDOS

**Direção literal em 28/10/2026:** “Essa parte de história pode até ter escrito, mas não vejo sentido em mostrar eles se encontrando e tudo mais, podemos partir para as partes que jogador execute as missões na vera. O jogador encontra o npc ele se introduz e dai ele inicia a missão a partir dele. Ou se o evento puder ser iniciado de forma própria, com um botão por exemplo na área de missões no HUD o jogador mesmo da inicio e se for confronto o Hollow por exemplo é spawnado”. Substitui a recomendação anterior de mostrar o encontro de Ichigo/Rukia como abertura obrigatória. A história pode permanecer em texto; no contato, o NPC se apresenta brevemente e oferece a missão. O foco é a ação do jogador. Não aprova eliminar toda contextualização ou alterar os acontecimentos principais da obra.

**Dois modos de início, conforme a ficha:**

- **Pelo NPC:** encontrar o personagem, receber apresentação/contexto e iniciar a missão por sua interação. Abrir a conversa ou receber uma missão disponível não começa involuntariamente um combate.
- **Pelo painel de missões/HUD:** quando o evento admitir início autônomo e estiver disponível, o jogador usa a ação “Iniciar missão”. Não exigir voltar a um NPC para um evento definido como autônomo. Não liberar todos os eventos remotamente por associação.

**Confronto:** a ação explícita de iniciar cria a presença de combate prevista — por exemplo, o Hollow daquela missão — no local válido da ficha, no estado/versão correspondente. M03/STR-03 e M09-06A já aprovam autoria do jogador e início deliberado. “Iniciar missão” pode ser essa confirmação; não impor outro clique de confirmação universal ou uma cutscene. Aceitar/disponibilizar missão e efetivamente iniciar o confronto continuam resultados distintos.

O início revalida requisitos e situação, não cria outra cópia se a mesma tentativa já estiver ativa e informa o que impede começar. A ficha define área/posição, comportamento se o jogador está em local incompatível e conflito com encontro existente; botão no HUD não autoriza criar boss sobre qualquer construção ou teleportar/destruir terreno automaticamente. Não impor saída de combate ou instância privada universais. M09/M15 conservam continuidade, falha e participação; NPC permanente e ator do confronto são distintos. Repetibilidade e recompensa não são aprovadas apenas por existir um botão de início.

**Apresentação:** a interface deverá oferecer a área de missões e a ação de início para eventos autônomos; posição, tecla, layout e implementação ficam M12/M14/M16. Esta decisão não abre esses módulos inteiros nem muda o foco exclusivo na campanha principal.

### 8.2. M13-03A/B — Abertura e conclusão da campanha — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Vamos seguir como você recomendou”; 2. “Vamos seguir como você recomendou”. Respondem às duas perguntas de 12.4/12.5.

- **03A:** abrir com Rukia em Karakura e um confronto Hollow curto, antes do caso de Orihime. A estrutura de MAIN-SUB-01 está aprovada; identificação visual/nome do alvo, números, acesso por caminho e realização mantêm suas pendências.
- **03B:** concluir e registrar o progresso após o objetivo validamente cumprido; exigir retorno ao NPC quando houver entrega ou conversa necessária. Aplicar como contrato comum das fichas da campanha, sem nova votação a cada combate. Disponibilizar a próxima missão não a inicia automaticamente; participação, recompensa e repetição seguem contratos próprios.

Aprovação da abertura não fecha a tabela inteira de 10.1 ou concede Rukia como fonte universal de todas as raças. O desfecho narrativo de uma missão pode integrar o próprio local do acontecimento, sem impor deslocamento adicional ao NPC.

### 8.3. M13-04A/B — Caso de Orihime, fonte e objetivo — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Concordo”; 2. “Prefiro vencer o Hollow”. Respondem a 12.6/12.7.

Rukia encaminha a ocorrência envolvendo Orihime. O objetivo é vencer o irmão Hollow; Orihime participa do contexto e do desfecho, sem barra de defesa, escolta ou cronômetro de sobrevivência. Aplicar em MAIN-SUB-02. O alcance é esta missão, sem eliminar objetivos de defesa de toda a campanha por associação. Início e conclusão herdam 01A/02A/03B; a vitória do jogador precede a resolução narrativa, conforme STR-03 e CAMP-S03. Detalhes de capacidades, mapa, acesso/participação e recompensas continuam pendentes.

### 8.4. M13-05A/B — Shrieker direto e incidente de Kon jogável — DECIDIDOS

**Resposta literal em 28/10/2026:** “Concordo com as recomendações”. Aprova conjuntamente as recomendações de 12.8/12.9 apresentadas na rodada anterior.

- **05A:** Chad apresenta brevemente o caso do periquito; jogador inicia e enfrenta Shrieker diretamente. Não acrescentar etapa obrigatória de investigação, transporte do periquito no inventário ou escolta por associação. O desfecho é breve e a conclusão herda 03B.
- **05B:** incluir o incidente de Kon como missão curta: Urahara encaminha; no local indicado perto da escola, jogador inicia pelo painel/HUD e enfrenta o Hollow da ocorrência; contexto escrito encerra o caso e apresenta a continuidade de Kon. Não transformar Kon em alvo a matar nem apenas eliminar esse caso da cobertura jogável.

Aplicado em 10.5/10.6 e no catálogo 10.1. Não aprova repertórios/números dos adversários, mapa, requisitos de cada caminho, replay ou recompensas materiais; não concede Kon como pet/aliado/item. As duas recomendações estão respondidas; não reenviar.

### 8.5. M13-06A — Grand Fisher, vitória e retirada — DECIDIDO

**Resposta literal em 28/10/2026:** “Positivo, adotamos”. Aprova MAIN-SUB-05 de 10.7: Rukia oferece o confronto, jogador/party vence a representação inicial e conclui; texto breve informa a fuga, mantendo a continuidade de Grand Fisher. Não é falha ou derrota inevitável do jogador. A ligação com a família Kurosaki pertence ao contexto, não à biografia do personagem original. Perfil numérico, capacidades e transição/remoção do ator continuam por ficha/integração. O alcance é este encontro, não regra universal de retirada de bosses.

### 8.6. M13-07A/B — Ligação Don Kanonji e crise Quincy — DECIDIDOS

**Resposta literal em 28/10/2026:** “gostei da forma como propôs, muito bom, podemos seguir”. Aceita o pacote da rodada de 12.11/12.12, sem alternativa/correção indicada: Don Kanonji em texto curto e crise de Uryū em duas missões.

- **07A:** incidente de Don Kanonji fica no contexto escrito entre Grand Fisher e Uryū, sem missão obrigatória do hospital nesta sequência. Continua acontecimento do mangá, não filler reclassificado.
- **07B:** MAIN-SUB-06A contra os Hollows atraídos e MAIN-SUB-06B contra o Menos, com início próprio do segundo encontro pelo painel na área válida. Vitória do jogador seguida de recuo narrativo do Menos; concluir a primeira missão persiste se a tentativa do boss falhar.

Aplicado em 10.1/10.8/10.9/12.11/12.12. Sem NPCs canônicos aliados lutando; sem compra/forma/domínio/recompensa adicional por associação. Número de alvos/ondas, capacidades/medidas, cenário, motivação/acesso por caminho, participação e realização seguem pendentes. Não reenviar 07A/B como perguntas.

### 8.7. M13-08A — Renji vencível e prisão no desfecho — DECIDIDO

**Resposta literal em 28/10/2026 à pergunta 1:** “Concordo”. Aprova MAIN-SUB-07 de 10.10: aviso de Kon, início pelo painel no contexto/local indicado, vitória do jogador/party contra Renji e Byakuya/prisão de Rukia no desfecho escrito. A missão conclui pelo objetivo de vencer Renji; confronto jogável de Byakuya fica para a etapa pertinente da Soul Society. Não aplica morte/perda de poderes ao jogador por analogia a Ichigo. Detalhes de realização/participação/recompensas seguem pendentes; não reenviar.

**Histórico da pergunta 2:** o pedido de explicação não aprovou a prova naquele momento. Após o esclarecimento concreto, o usuário aceitou sua inclusão na resposta registrada em 8.8; 08B não está mais aberto.

### 8.8. M13-08B/C — Preparação com Urahara e perseguição de Ururu — DECIDIDOS NO ALCANCE ESTRUTURAL

**Resposta literal em 28/10/2026:** “Perfeito, podemos seguir assim, e também poderiamos incluir aquela parte que a menina pequena que esqueci o nome persegue o ichigo, poderiamos colocar aquilo e quando iniciasse ela começaria a perseguir o jogador e teria um tempo limite, se o jogador passar aquele tempo todo sem chegar a X% da vida ele passa. Fora o que você já propos”.

- **08B:** aceita a prova curta explicada anteriormente: iniciar deliberadamente no campo subterrâneo, usar repertório próprio e acertar um golpe válido na representação de Urahara; encerramento controlado e encaminhamento da expedição, sem viagem automática. Não refazer a recuperação racial de Ichigo nem conceder poderes. A opção de somente plano/viagem, sem avaliação, foi preterida.
- **08C:** acrescenta a etapa de perseguição pela menina identificada como Ururu Tsumugiya (CAMP-S05). Durante a tentativa iniciada, ela persegue/ataca o jogador; o objetivo é completar um intervalo de treino sem atingir um limiar percentual de Vida. É sobrevivência e movimentação, não matar Ururu. Inclusão e objetivo vêm da direção do usuário; tempo e percentual exatos não foram informados/aprovados.

Organização recomendada em 10.12: uma avaliação com duas etapas, Ururu antes de Urahara, seguida da expedição. Perfil de treino controlado, validação/encerramento e cenário exigem M09/M10/M12/M15. 09A/B foram posteriormente aprovados em 8.9: falha ao atingir 30% ou menos durante 60 segundos nominais, valores provisórios, e Ururu concluída preservada se o duelo seguinte falhar. Detalhes técnicos de Vida/formas, simulação, área e participação continuam por integração, sem fechamento implícito de todas as recomendações de 10.12.

### 8.9. M13-09A/B — Parâmetros provisórios e retenção da etapa de Ururu — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Pode ser isso mesmo, depois caso não, ajusto”; 2. “Mantemos concluida”.

- **09A:** adotar inicialmente 60 segundos nominais e falha ao atingir 30% da Vida máxima de referência ou menos em qualquer momento. Recuperação posterior não apaga a falha. Valores expressamente provisórios para balancear depois; não são cânone nem resultado testado. A aprovação não resolve automaticamente todos os efeitos de formas, custos de Vida ou absorção.
- **09B:** aprovação em Ururu permanece concluída se o jogador falhar depois contra Urahara; repetir apenas o duelo. É retenção específica desta avaliação, sem custo/recompensa adicional ou checkpoint universal.

Aplicado em 10.12/12.16/12.17. Não reenviar essas escolhas; retomar agora percurso e começo do arco Soul Society. Perfil/área de treino, encerramento controlado, elegibilidade e persistência ainda exigem M09/M10/M12/M15.

### 8.10. M13-10A/B — Travessia breve e abertura com Jidanbō — DECIDIDOS

**Resposta conjunta literal em 28/10/2026:** “Perfeito, podemos adotar isso sim”. Aceita as duas recomendações apresentadas em 12.18/12.19.

- **10A:** percurso curto jogável pelo Dangai até o Rukongai; Kōtotsu no contexto escrito, sem perseguição jogável/cronometrada adicional nesta missão. Não elimina o acontecimento da obra ou aprova riscos/teleporte automático.
- **10B:** Jidanbō apresenta o desafio; jogador inicia e vence. A missão conclui pela vitória; Gin fecha o acesso no desfecho escrito, e Yoruichi encaminha a busca de Kūkaku. Não criar derrota forçada adicional, matar Jidanbō definitivamente ou bloquear globalmente acessos legítimos de outros jogadores.

Aplicado em 10.11/10.13/12.18/12.19. Perfis, mapas, percurso executável, acesso/retorno, participação e recompensas continuam pendentes; não reenviar as escolhas. Próxima rodada: selecionar cobertura de Ganju e preparação da entrada pelo canhão.

### 8.11. M13-11A/B — Ganju jogável e preparo com Kūkaku — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Prefiro incluir”; 2. “Podemois seguir assim”.

- **11A:** escolher a alternativa de incluir uma luta com Ganju na campanha principal, entre Jidanbō e a preparação com Kūkaku. A recomendação de manter apenas o conflito no contexto foi preterida. Inclusão não escolhe automaticamente resultado/números da adaptação da briga originalmente interrompida; proposta específica em 12A/10.15.
- **11B:** aprovar MAIN-SS-02: exercício breve de ajustar a energia para manter marcador na faixa indicada e, depois, lançamento explícito pelo canhão de Kūkaku até o interior do Seireitei. Parâmetros/custos/tolerância, mapa, viagem e participação continuam pendentes, sem conceder skill/domínio ou canhão/dispositivo como recompensa.

Aplicado em 10.2/10.14/10.15/12.20/12.21. Não reenviar inclusão ou exercício. O jogador/party vence os confrontos; Ganju não se torna aliado canônico combatendo depois da sua missão.

**Direção de versionamento em 28/10/2026:** “Apenas uma informação quando acabarmos esse arco vamos subir no repositório, na main, mas antes vamos atualizar a branch mesmo”. Registrar publicação na main após concluir este arco da Soul Society e atualização prévia da branch de trabalho. Checkout conferido na main, HEAD 26b73b3742681bfeee694c23a6e92007fd7f44ba. Esclarecimento posterior do usuário: “Manter na main local e publicar somente ao concluir o arco”. Seguir na main local, sem criar branch separada; publicação somente após concluir Soul Society. Atualizações documentais não equivalem a commit/push executados.

### 8.12. M13-12A/B — Vitória contra Ganju e sequência Ikkaku/Yumichika — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Concordo”; 2. “seguimos assim”.

- **12A:** vencer completamente a representação de Ganju, que encerra o desafio e permanece vivo para a história. É adaptação assumida da briga originalmente interrompida, sem finalização de NPC ou interrupção que impeça o objetivo comunicado.
- **12B:** depois do canhão, duas missões separadas, Ikkaku e depois Yumichika; o jogador/party conquista ambas. Ikkaku concluído permanece se falhar contra Yumichika. Lutas paralelas da obra adaptadas em sequência, sem Ganju aliado combatente nem divisão forçada da party.

Aplicado em 10.15/10.16/12.22/12.23; não reenviar. Perfis/versões dos adversários, cenário, participação, recompensas/replay e realização continuam pendentes. Agora selecionar Jirōbō e a aproximação da prisão com Hanatarō. Manter main local, publicar só ao concluir Soul Society.

### 8.13. M13-13A/B — Jirōbō e caminho subterrâneo de Hanatarō — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Adotamos”; 2. “Seguimos assim”.

- **13A:** incluir Jirōbō como missão de combate do jogador/party, com contexto da ocorrência de Uryū/Orihime e cerco de soldados comuns em texto. Sem missão adicional de ondas, aliado canônico combatente ou defesa de Orihime.
- **13B:** Hanatarō se apresenta e indica passagem; jogador percorre rota curta nas galerias até a saída próxima à prisão. Sem escolta obrigatória/barra de defesa do NPC; chegada validada disponibiliza o segundo Renji, com início próprio.

Aplicado em 10.17/10.18/12.24/12.25; não reenviar. Autoria, objetivos e cobertura aprovados; perfis, mapas, posição/retomada, participação e recompensas continuam por integração. Rota não conclui o resgate de Rukia ou concede cura/skill automaticamente. Main local, publicação apenas ao concluir Soul Society.

### 8.14. M13-14A/B — Interlúdio de Aizen em texto e Shunsui jogável — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Concordo”; 2. “Adotamos”.

- **14A:** após a vitória contra o segundo Renji, aparente morte de Aizen e reações dos capitães no contexto escrito; sem missão obrigatória de investigação. Preservar o mistério/revelação posterior, sem consequências físicas pessoais de Ichigo impostas ao jogador.
- **14B:** incluir Shunsui como confronto vencível do jogador na adaptação da ocorrência de Chad, antes de Kenpachi. Capitão permanece vivo no desfecho. Sem obrigar a derrota do personagem para reproduzir o resultado de Chad nem transferir sua biografia/poderes.

Aplicado em 10.19/10.20/12.26/12.27; não reenviar. Ordem sequencial e autoria são adaptações aprovadas, não ordem literal de todas as frentes paralelas. Perfis/versões, mapas, elegibilidade e recompensas/replay continuam pendentes. Próximo: Kenpachi e primeira chegada à prisão; main local, publicação só ao concluir Soul Society.

### 8.15. M13-15A/B — Contato com Rukia e Mayuri jogável — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Adotamos”; 2. “Incluimos”.

- **15A:** após Kenpachi, missão de alcançar/interagir com Rukia na prisão; Byakuya e intervenção de Yoruichi no desfecho escrito. Guardar luta principal contra Byakuya para o resgate no local da execução, sem derrota/morte forçada ou teleporte automático do jogador.
- **15B:** incluir Mayuri como confronto vencível do jogador, da ocorrência de Uryū. Vitória completa e retirada narrativa posterior; capitão permanece vivo. Não impor Letzt Stil, perda de poderes ou história pessoal de Uryū ao personagem.

Aplicado em 10.22/10.23/12.28/12.29; não reenviar. Perfil/versão, efeitos, mapa, acesso/retorno, participação e recompensas continuam pendentes; aprovação não executa o resgate final de Rukia. Agora selecionar preparação final e frente Tōsen/Komamura. Main local, publicação só ao concluir Soul Society.

### 8.16. M13-16A/B — Preparação própria e capitães separados — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Prefiro uma preparação jogável própria”; 2. “Duas missões”.

- **16A:** escolher a alternativa de preparação jogável própria com Yoruichi, preterindo passar somente pelo plano/contexto sem prova. Na escolha 16A, conteúdo ainda não havia sido apresentado; posteriormente o circuito foi escolhido explicitamente em 17A/10.26. Parâmetros/mapa e integrações continuam pendentes, sem aprovação por associação.
- **16B:** duas missões, vencer Tōsen e depois Komamura, mantendo a primeira concluída se falhar na segunda. Sem Kenpachi aliado combatente, encontro simultâneo ou missão concluída só por assistir à luta dos NPCs.

Aplicado em 10.24/10.25/12.30/12.31. Não reenviar inclusão da preparação ou divisão dos capitães. Requisitos de capacidades, compras com SP e prática continuam próprios; treino jogável não implica Bankai grátis ou repetir a formação racial. Main local, publicar só ao concluir Soul Society.

### 8.17. M13-17A/B — Circuito de Yoruichi e resgate por interação — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Adotamos”; 2. “Seguimos asism”.

- **17A:** escolher o ensaio de resgate: pontos marcados em ordem, obstáculos/desníveis e interação com alvo final dentro do tempo limite. Percurso viável com movimentos disponíveis ao personagem apto; tempo, distâncias, tolerância e mapa ainda por ficha. Alternativa de duelo curto com Yoruichi preterida.
- **17B:** alcançar Rukia no local da execução e libertá-la por interação; interrupção do Sōkyoku no contexto escrito. Byakuya como luta própria seguinte, em início deliberado. Não acrescentar desafio/boss da arma da execução por associação.

Aplicado em 10.26/10.27/12.32/12.33; não reenviar. Preparação não compra/concede skill, forma, Mastery ou arma; resgate imediato não encerra a conspiração/arco. Main local, publicar só ao concluir Soul Society.

### 8.18. M13-18A/B — Byakuya contínuo e Soi Fon jogável — DECIDIDOS

**Respostas literais em 28/10/2026:** 1. “Concordo”; 2. “Adotamos”.

- **18A:** uma luta contínua de Byakuya, com progressão Shikai → Bankai → uso de Senkei dentro do Bankai, sem outra missão/tentativa entre esses momentos. Valores, gatilhos e transições ainda por ficha; Senkei não é terceira transformação racial.
- **18B:** Soi Fon como missão do jogador antes do resgate, adaptando a frente de Yoruichi; disputa Yamamoto/Shunsui/Ukitake no contexto escrito. Preservar sequência aprovada de libertação → Byakuya. Não incluir Yamamoto como boss obrigatório nesta seleção.

Aplicado em 10.28/10.29/12.34/12.35; não reenviar. Autoria/ordem são adaptações explícitas, sem NPC canônico aliado combatendo ou poder gratuito. Fichas executáveis/valores continuam pendentes. Agora fechar demais ocorrências e desfecho de Aizen, depois encerramento/retorno e revisão; main local até concluir Soul Society.

### 8.19. M13-19A aprovado; histórico do esclarecimento de M13-19B

**Respostas literais em 28/10/2026:** 1. “Concordo”; 2. “porque adaptar para uma vitória?”.

- **19A decidido:** Hitsugaya/Gin, Iba/Ikkaku, Hisagi/Yumichika e intervenção dos oficiais no resgate ficam no contexto escrito, sem novas missões obrigatórias desses combates. Aplicar cobertura de 10.30 no alcance mencionado; personagens/ações exatas ainda exigem confirmação para texto final.
- **19B continua aberto:** pergunta sobre a justificativa da adaptação, sem aprovação de vencer Aizen nesta etapa e sem escolha formal da alternativa. Não registrar como “Adotamos”, rejeição completa do jogo ou revisão automática de STR-03.

**Esclarecimento:** STR-03 determina autoria/vitória do jogador nos bosses selecionados e protege marcos da obra; não obriga que toda aparição de um antagonista seja convertida em boss vencível. O próprio exemplo de M03 associa derrota/selamento final de Aizen ao momento apropriado de Karakura. A recomendação anterior ampliou essa convenção mais do que o necessário para selecionar esta aparição da Soul Society.

**Recomendação atual do assistente, ainda não aprovada:** preservar superioridade de Aizen e obtenção do Hōgyoku neste trecho, com revelação/saída no contexto escrito e Byakuya como último boss deste arco; se houver objetivo final jogável, dar função de contato/retirada/apoio com sucesso próprio, sem anunciar “derrote Aizen” e exigir perder. Detalhe em 10.31.1; aguardar escolha antes de consolidar encerramento ou publicar. Main local.

### 8.20. M13-19B — Aizen em contexto e Byakuya último boss — DECIDIDO COM REVISÃO

**Resposta literal em 28/10/2026:** “Perfeito, aprovo”. Aprova a recomendação reformulada: superioridade de Aizen, revelação/Hōgyoku/partida em texto, sem vitória do jogador contra ele neste arco; Byakuya último boss da Soul Society. Confrontos vencíveis posteriores de Aizen continuam nos momentos apropriados sob STR-03.

A proposta anterior de boss Aizen/MAIN-SS-14 não foi adotada; preservada no histórico de 10.31.1. Aplicado em 10.31/12.37 e sequência/encerramento de 10.32/10.33. Não reenviar ou tratar o pedido anterior de justificativa como aceite da proposta antiga. Não é aprovação de detalhes executáveis/recompensas ou dos próximos arcos.

## 9. Integrações

| Responsável | Dependência para a realização futura |
|---|---|
| M03/M04 | Era, marcos protegidos, personagem original e condições de entrada |
| M06/M07/M09 | Ações disponíveis, perfil e estado correto de inimigos/confrontos |
| M08/M10 | Presença do elenco, lugares e rotas coerentes com o período |
| M11 | Itens de missão e recompensas materiais; pendências preservadas |
| M12/M14/M15/M16 | Objetivos/estados/retomada, apresentação, participação/era e persistência; ainda sem especificação executável completa |

## 10. Conteúdo e cobertura

### 10.1. Primeiro arco — Shinigami Substituto — seleção consolidada

Os sete blocos originais foram referências de acontecimentos, não sete quests aprovadas. Após 01A/02A, cada entrada só vira missão quando houver objetivo executável: encontrar um NPC, iniciar a atividade, enfrentar/interagir e concluir. Encontro entre personagens, memória ou conversa sem tarefa pode ficar no texto de contexto; não criar missão apenas para assistir. A formação racial permanece fora deste catálogo.

| Referência | Fonte/NPC candidato | Ação jogável a especificar | Início proposto |
|---|---|---|---|
| MAIN-SUB-01 — Um Hollow na área | Rukia; personagem se apresenta brevemente | Iniciar e vencer um confronto Hollow do começo do arco; identidade/versão exatas ainda por ficha | Pelo NPC; disponibilidade posterior no painel depende da ficha |
| MAIN-SUB-02 — Laços além da morte | Rukia — 04A aprovado | Vencer o irmão Hollow, sem objetivo de defesa de Orihime — 04B aprovado | Pelo NPC/contexto; ator do confronto criado no início explícito |
| MAIN-SUB-03 — A alma no periquito | Chad — 05A aprovado | Apresentação breve e confronto direto com Shrieker; desfecho do caso em texto | Pelo NPC; começo do confronto declarado |
| MAIN-SUB-04 — Uma alma fora do lugar | Urahara — 05B aprovado | Enfrentar o Hollow da ocorrência perto da escola e contextualizar Kon | Oferta por Urahara; início pelo painel no local indicado |
| MAIN-SUB-05 — Grand Fisher | Rukia — 06A aprovado | Vencer a representação inicial; retirada narrativa posterior e conclusão | Pelo NPC no contexto do cemitério |
| Ligação após MAIN-SUB-05 — Don Kanonji | Contexto escrito aprovado em 07A | Relatar o incidente do hospital; sem nova missão obrigatória na sequência atual | Sem início adicional |
| MAIN-SUB-06 — Orgulho Quincy | Uryū/contexto de incidentes Hollow — 07B aprovado | Duas missões: grupo de Hollows e confronto com Menos, encerrado com recuo narrativo | Primeiro pelo NPC/contexto; Menos pelo painel na área válida, em início próprio |
| MAIN-SUB-07 — Rukia é levada | Aviso de Kon/contexto da saída de Rukia — 08A aprovado | Vencer Renji; prisão de Rukia com Byakuya no desfecho escrito | Painel no local indicado, após apresentação da ocorrência |
| MAIN-SUB-PREP-01 — Preparação para o resgate | Urahara/Ururu — 08B/C aprovados no alcance estrutural | Sobreviver à perseguição por tempo definido sem atingir o limiar de Vida; depois acertar um golpe válido em Urahara | Pela interação no campo subterrâneo; início deliberado de cada etapa |
| MAIN-SUB-08 — Rumo à Soul Society | Urahara — continuidade da preparação | Iniciar expedição, atravessar passagem e chegar ao Rukongai | Pela interação/serviço contextual de Urahara, sem conceder skill de portal |

**Estado:** MAIN-SUB-01–07 (06 dividida em A/B) tiveram estruturas aprovadas em 03A/B, 04A/B, 05A/B, 06A, 07B e 08A; ligação Don Kanonji escolhida em 07A; preparação incluída em 08B/C, com duas etapas em 10.12. Percurso da expedição e demais detalhes de realização continuam rascunhos, sem aprovação do catálogo inteiro ou de sete inícios remotos. A direção NPC/painel/spawn está aprovada; cada missão escolhe seu modo conforme contexto. O nome do primeiro Hollow não foi tratado como confirmado por fonte primária nesta rodada.

### 10.2. Demais arcos

M03 organiza o horizonte completo. A abertura da Soul Society teve a primeira estrutura aprovada, sem fechar todo o arco:

| Referência | Conteúdo proposto | Estado |
|---|---|---|
| MAIN-SS-01 — O guardião do Seireitei | Jidanbō como primeiro confronto após a chegada ao Rukongai; Gin fecha a entrada no desfecho escrito; buscar outro caminho | M13-10B aprovado no alcance estrutural, ficha 10.13 |
| MAIN-SS-GANJU-01 — Desafio no Rukongai | Confronto com Ganju antes de Kūkaku | Inclusão/desfecho aprovados em 11A/12A, ficha 10.15 |
| MAIN-SS-02 — Entrada pelos céus | Preparo breve da esfera com Kūkaku e lançamento pelo canhão até o interior do Seireitei | M13-11B aprovado no alcance estrutural, ficha 10.14 |
| MAIN-SS-03A/B — Primeiros confrontos no Seireitei | Ikkaku e depois Yumichika, em duas missões vencidas pelo jogador | M13-12B aprovado, ficha 10.16 |
| MAIN-SS-04 — As lâminas de Jirōbō | Confronto do jogador contra o adversário da ocorrência de Uryū/Orihime | M13-13A aprovado, ficha 10.17 |
| MAIN-SS-05 — O caminho sob o Seireitei | Hanatarō apresenta a passagem; jogador percorre as galerias até a saída de aproximação da prisão | M13-13B aprovado, ficha 10.18 |
| MAIN-SS-06 — Renji, determinação na Soul Society | Segundo confronto de Renji; vitória e interlúdio escrito de reações/aparente morte de Aizen | M13-14A aprovado, ficha 10.19 |
| MAIN-SS-07 — O capitão da Oitava Divisão | Confronto adaptado da ocorrência de Chad com Shunsui | M13-14B aprovado, ficha 10.20 |
| MAIN-SS-08 — O capitão da Décima Primeira Divisão | Kenpachi como confronto completo da aproximação da prisão | Ficha preparada em 10.21, sob STR-03 e continuidade de 14B; realização em especificação |
| MAIN-SS-09 — Rukia na prisão | Alcançar/interagir com Rukia; Byakuya/Yoruichi no desfecho escrito desta primeira chegada | M13-15A aprovado, ficha 10.22 |
| MAIN-SS-10 — O capitão da Décima Segunda Divisão | Mayuri como vitória do jogador e recuo narrativo posterior | M13-15B aprovado, ficha 10.23 |
| MAIN-SS-PREP-01 — Ensaio de resgate | Preparação jogável própria com Yoruichi; circuito de aproximação e interação com alvo propostos | Inclusão/conteúdo aprovados em 16A/17A, blocos 10.24/10.26 |
| MAIN-SS-11A/B — Capitães em oposição | Tōsen e depois Komamura, adaptando a frente de Kenpachi para duas missões do jogador | M13-16B aprovado, ficha 10.25 |
| MAIN-SS-12 — Interromper a execução | Alcançar Rukia e libertá-la por interação; Sōkyoku no contexto, sem boss próprio | M13-17B aprovado, ficha 10.27 |
| MAIN-SS-13 — O orgulho de Byakuya | Confronto principal reservado desde 15A; progressão contínua Shikai/Bankai/Senkei proposta | M13-18A aprovado, ficha 10.28 |
| MAIN-SS-FRONT-01 — Soi Fon | Frente de Yoruichi adaptada ao jogador antes do resgate, se escolhida; Yamamoto/Shunsui/Ukitake propostos em texto | M13-18B aprovado, ficha 10.29; ordem é adaptação explícita |
| Outras ocorrências finais | Hitsugaya/Gin, Iba/Ikkaku, Hisagi/Yumichika, oficiais do resgate e passado/contexto pessoal em texto | M13-19A aprovado no alcance registrado, cobertura em 10.30 |
| Desfecho — A verdade por trás da execução | Revelação/Hōgyoku/partida de Aizen em contexto; nenhum confronto jogável contra ele neste arco | M13-19B aprovado com revisão; 10.31. ID MAIN-SS-14 da proposta retirada não é missão do catálogo vigente |
| Encerramento e retorno | Epílogo após os objetivos válidos, execução cancelada e Rukia na Soul Society; viagem do jogador pelos contratos de acesso/retorno | Consolidação dos contratos em 10.33; realização de rotas/participação continua pendente |
| Continuidade final | Resgate no local da execução, Byakuya e seleção das demais frentes/conspiração/encerramento | Próximos blocos; sem catálogo final aprovado |

Não tratar essas entradas como quests raciais ou filler. Os demais arcos e o restante da Soul Society ainda não possuem catálogo fechado neste módulo.

### 10.3. MAIN-SUB-01 — Um Hollow na área

**Estrutura aprovada — M13-03A/B, 28/10/2026.** As duas respostas “Vamos seguir como você recomendou” aprovam a abertura com Rukia e a conclusão após objetivo válido. Detalhes de realização e perfil de combate ainda pendentes não foram aprovados por associação; 01A/02A seguem decididos.

**Experiência aprovada:** encontrar Rukia em Karakura, receber uma apresentação breve, escolher “Iniciar missão” e derrotar um único Hollow do confronto inicial adaptado. Uma atividade de combate da campanha, sem coleta preparatória, escolta obrigatória ou encenação de Ichigo recebendo poderes. Não é uma caçada aleatória acrescentada como requisito da história.

| Campo | Estrutura aprovada e pendências indicadas |
|---|---|
| Categoria/era | Principal; abertura do arco Shinigami Substituto, antes do caso do irmão de Orihime |
| Procedência | Adaptação jogável do confronto Hollow da abertura; CAMP-S01, sinopses oficiais dos episódios 1–2 como complemento. A autoria do jogador segue STR-03; não afirmar reprodução integral do mangá |
| Fonte | Rukia Kuchiki, em presença de serviço compatível com o começo do arco, próxima da área designada em Karakura |
| Apresentação ilustrativa | “Sou Rukia Kuchiki. Um Hollow está ameaçando esta área. Preciso que você o detenha.” Texto provisório; sem transferência de poderes ou atribuição de parentesco ao personagem |
| Objetivo mostrado | “Derrote o Hollow na área indicada.” Um alvo desta tentativa; matar outro Hollow do mundo não substitui o objetivo |
| Acesso | Entrada na campanha e capacidade de participar do encontro compatíveis com os contratos do personagem. Requisitos concretos de cada caminho ficam na integração M03/M04/M12/M15; esta ficha não cria uma formação racial obrigatória para todos |
| Início | Ação “Iniciar missão” pelo NPC, já junto da área válida de confronto. Revalidar o contexto antes de criar o alvo; não acrescentar uma ida ao NPC e outra etapa de caminhada apenas para iniciar |
| Lugar | Trecho aberto designado de Karakura, adequado ao confronto e associado à oferta da missão. Coordenadas/mapa e condições de ocupação ficam M08/M09/M10/M12; não criar o alvo dentro de uma construção arbitrária do jogador |
| Adversário | Um Hollow do confronto de abertura, na versão inicial, sem importar evolução posterior. Nome próprio e identificação visual exata continuam pendentes de confirmação primária; não inventar nome nem trocar silenciosamente por um boss posterior |
| Perfil jogável proposto | Combate direto, com aproximação e golpes físicos legíveis; um único confronto, sem ondas extras ou segunda transformação. Este perfil é adaptação de gameplay, não descrição canônica comprovada de todos os ataques do Hollow. Capacidades finais, dano, Vida e alcance exigem ficha M09 |
| Ação do jogador | Enfrentar e vencer com os recursos que seu personagem já possui. Não exigir uma técnica exclusiva concedida pela própria missão |
| Papel de Rukia | Apresentar e encaminhar. Não lutar, reduzir a Vida do alvo ou finalizar o confronto pelo jogador |
| Vitória | Resultado de vitória do encontro reconhecido e elegibilidade de participação confirmada. A ficha não decide crédito por último golpe; M12/M15 avaliam o participante |
| Conclusão aprovada | Registrar a conclusão automaticamente após a vitória válida e mostrar confirmação breve. Não exigir voltar até Rukia apenas para apertar “Concluir” |
| Continuidade aprovada | Disponibilizar o próximo caso, MAIN-SUB-02 — Laços além da morte; seu início continua deliberado, sem criar automaticamente o próximo inimigo |
| Recompensa | Avanço da campanha definido acima; recompensa adicional ainda por especificar. Não prometer item, SP, skill, forma ou Mastery automaticamente |
| Falha/retomada | Herdar M09-06B/07B: derrota de todos encerra a tentativa; uma nova tentativa autorizada começa com o estado inicial do encontro. Sem vitória/recompensa na falha, sem restaurar terreno ou curar consequências persistentes |
| Repetição | Nova tentativa após falha não equivale a repetir uma missão já concluída para ganhar recompensa. Política de replay/recompensa permanece pendente |

**Fluxo aprovado:** Rukia → apresentação e objetivo → “Iniciar missão” → um Hollow criado na área válida → combate do jogador/party → vitória validada → conclusão e aviso do próximo caso. A conversa inicial não dispara ataque involuntário, e cliques repetidos não criam outra cópia da mesma tentativa, conforme 8.1.

**Limites do roteiro:** Rukia é a fonte aprovada desta missão, não uma mentora universal já aprovada para todas as raças/afiliações. Motivação e acesso de caminhos incompatíveis precisam de solução específica antes de disponibilizá-la a todos. Falha por abandono, reentrada, presença de terceiros e ocupação da área continuam integrações herdadas, não uma autorização implícita de instância privada ou reset de mundo. O botão remoto do HUD não foi proposto para esta primeira oferta; continua disponível a eventos autônomos de suas fichas.

**Conferência narrativa:** a sinopse oficial confirma Rukia e a ameaça Hollow no começo da obra e, no episódio 2, a referência ao confronto da noite anterior. Não fornece o nome próprio nem o repertório completo do adversário. Karakura como local jogável, posicionamento de Rukia, perfil simplificado, autoria e fluxo de conclusão são adaptações declaradas. Não importar o nome/visual da versão live-action como prova de equivalência ao mangá/anime.

**O que a rodada resolveu:** seleção da abertura e forma de concluir a primeira missão. Detalhamento de mapa, ficha de combate, acesso racial, participação e recompensas continua necessário antes de implementação. Aprovação da estrutura não declara esses detalhes especificados nem funcionamento observado em jogo.

### 10.4. MAIN-SUB-02 — Laços além da morte

**Estrutura aprovada — M13-04A/B, 28/10/2026.** Respostas: “Concordo” e “Prefiro vencer o Hollow”; Rukia encaminha e vencer o adversário é o objetivo, sem defesa de Orihime. Segunda atividade após MAIN-SUB-01. CAMP-S01/03 sustentam a ameaça do irmão Hollow contra Orihime e o desfecho do conflito. A oferta e o papel do jogador são adaptações declaradas.

| Campo | Estrutura aprovada e pendências |
|---|---|
| Categoria/era | Principal; começo do arco Substituto, após MAIN-SUB-01 e antes do caso do periquito |
| Fonte aprovada | Rukia encaminha o jogador à ocorrência envolvendo Orihime. Não fazer Orihime oferecer a caçada ao próprio irmão como se já compreendesse sua situação |
| Contexto breve | Uma presença Hollow ameaça a residência de Orihime. A identidade e o vínculo são revelados no contexto da ocorrência; não transferir esses laços ao personagem do jogador |
| Início | Interação “Iniciar missão” com Rukia no contexto/local de início indicado. Oferta pode apontar a residência; distância/posição concreta ainda exige mapa. Não supor retorno remoto universal ou combate ao apenas entrar na casa |
| Lugar | Área preparada associada à residência de Orihime em Karakura; volume de combate e colocação dos personagens adequados à versão do inimigo. Não destruir/reconstruir casas de jogadores para reproduzir a cena |
| Alvo | O irmão de Orihime em forma Hollow, na versão deste acontecimento. O nome próprio e o catálogo final de golpes exigem referência específica; não preencher com habilidades de outro boss ou jogo |
| Objetivo aprovado | “Interrompa o ataque e derrote o Hollow que ameaça Orihime.” Um confronto direto, vencido pelo jogador/party |
| Papel de Orihime | Pessoa envolvida e centro emocional da história. Aprovado em 04B: sem barra de defesa, cronômetro de sobrevivência ou escolta; não luta como aliada. Sua participação no desfecho não substitui a vitória do jogador |
| Combate | Usar a representação correta do irmão Hollow; capacidades/medidas exigem ficha M09. Não inventar fases ou poderes para diferenciar artificialmente a primeira luta |
| Vitória de gameplay | Superar o adversário deste encontro. Fim da Vida de combate representa derrota do encontro; não implica automaticamente destruição da alma por qualquer arma ou poder |
| Desfecho narrativo | Após a vitória validada, texto breve apresenta o reconhecimento entre irmãos e a resolução/partida do irmão, conforme CAMP-S03. Nenhum aliado canônico entra para vencer a luta; a resolução narrativa não reinicia o combate nem retira seu crédito |
| Coerência dos poderes | Não conceder purificação Shinigami ao ataque de qualquer raça. Ação com Zanpakutō descrita na obra fica no contexto protegido do desfecho; esta adaptação não exige uma finalização racial do jogador nem fornece uma arma emprestada automaticamente |
| Conclusão | M13-03B: registrar depois do objetivo e do breve desfecho local, sem volta administrativa ao NPC. A próxima missão fica disponível, mas seu confronto não começa sozinho |
| Continuidade | Proposta: MAIN-SUB-03, caso de Chad e do periquito |
| Recompensas | Marco da campanha; demais ganhos ainda por ficha. Não entregar os poderes de Orihime nem Mastery automática ao jogador |
| Falha/retomada | Contratos M09-06B/07B. Sem falha por esgotar Vida de Orihime: o objetivo de defesa foi explicitamente preterido em 04B. Isso não aprova imunidade universal para NPCs fora deste encontro |
| Pendências | Acesso/motivação por caminho, representação e ataques do alvo, cenário, participação/reentrada, recompensas e repetição. A ficha não é executável nem foi testada |

**Fluxo com fonte/objetivo aprovados:** Rukia comunica a ocorrência → início deliberado no contexto válido → confronto do jogador com o irmão Hollow → vitória validada → desfecho breve entre irmãos em texto → conclusão e próximo caso disponível.

**Distinção de adaptação:** derrotar o inimigo jogável e resolver seu destino na história são resultados separados, como STR-03 permite. Não apresentar a reconciliação como técnica exclusiva aprendida pelo jogador ou atribuir a Orihime os poderes que desenvolverá depois. A proposta mantém a história como contexto e dá o confronto ao jogador, conforme 01A/02A.

### 10.5. MAIN-SUB-03 — A alma no periquito

**Estrutura aprovada — M13-05A, 28/10/2026.** Resposta conjunta “Concordo com as recomendações”: Chad apresenta e jogador enfrenta Shrieker diretamente, sem etapa de investigação obrigatória. Próximo caso após Orihime. CAMP-S01 (sinopses 4–5) confirma Chad, o periquito e Shrieker; CAMP-S03 contextualiza a alma do menino e o destino do Hollow. Fonte, posição e execução pelo jogador são adaptações propostas.

| Campo | Estrutura aprovada e pendências |
|---|---|
| Categoria/era | Principal; arco Substituto, após MAIN-SUB-02 |
| Fonte | Chad, com o periquito sob seus cuidados no contexto do caso; apresenta o problema e pede ajuda. Não atribuir a ele visão espiritual ou poder de combate que ainda não possui nesse período |
| Contexto breve | O periquito abriga a alma de um menino, e a presença de Shrieker está ligada à perseguição. A explicação cabe na conversa; não criar busca por pistas aleatórias para revelar o confronto |
| Objetivo | “Derrote Shrieker e encerre a perseguição.” A situação do menino dá sentido ao objetivo; não exigir carregar o periquito no inventário ou escoltar Chad |
| Início | “Iniciar missão” na interação com Chad, no contexto/local válido do encontro. O alvo desta tentativa é criado pelo contrato 02A |
| Lugar | Área de confronto designada em Karakura, compatível com o caso e com as ações do inimigo. Espaço e posicionamento ainda exigem M08/M09/M10 |
| Adversário | Shrieker na versão inicial deste caso. As armadilhas são referidas em CAMP-S01, mas tipos, funcionamento e repertório final exigem fonte/ficha M09; não inventar detalhes, números ou ondas adicionais |
| Participação do elenco | Chad/periquito contextualizam e participam do desfecho. Chad e Rukia não combatem como aliados nem tomam a vitória do jogador; sem barra de defesa/escolta nesta proposta |
| Vitória | Jogador/party supera Shrieker; resultado válido pertence à tentativa e aos participantes reconhecidos. Não basta derrotar qualquer Hollow de mundo aberto |
| Desfecho | Texto breve informa a resolução da perseguição e o destino de Shrieker ao Inferno por seus crimes em vida, conforme CAMP-S03. Nenhum ataque de NPC finaliza a luta pelo jogador |
| Limite narrativo | O destino narrativo do Hollow não abre uma dimensão explorável, portal utilizável, nova campanha ou recurso de purificação universal. A resolução da alma do menino não depende de escolher uma raça para o personagem |
| Conclusão | Contrato 03B: registro após vitória e desfecho breve, sem retorno administrativo. Disponibilizar o próximo caso escolhido, sem início involuntário |
| Falha/recompensa | Herança M09-06B/07B; participação M12/M15. Ganhos adicionais/replay pendentes, sem recompensa de periquito, alma, poder ou domínio presumida |
| Pendências | Acesso/motivação por caminho, mapa, estado do alvo/capacidades, atores de contexto, participação e recompensas. Proposta documental, sem implementação/teste |

**Ritmo aprovado:** Chad explica o caso → início → confronto com Shrieker → desfecho breve. A alternativa de investigação foi preterida nesta escolha; pergunta original preservada em 12.8.

### 10.6. MAIN-SUB-04 — Uma alma fora do lugar

**Estrutura aprovada — M13-05B, 28/10/2026.** Resposta conjunta “Concordo com as recomendações”: incluir a missão curta do incidente da alma modificada, encaminhada por Urahara, com início pelo painel na área da escola, antes de Grand Fisher. CAMP-S01 (6–7) e CAMP-S03 confirmam o contexto de Urahara, a alma artificial no corpo de Ichigo, o perigo perto da escola e a posterior identidade de Kon. A conexão entre oferta, localização e luta abaixo é adaptação; não reencenar toda a perseguição da obra.

| Campo | Estrutura aprovada e pendências |
|---|---|
| Categoria/era | Principal; arco Substituto, após o caso de Chad e antes de Grand Fisher, conforme 05B aprovado |
| Fonte | Urahara; apresenta brevemente o incidente com uma alma artificial e indica a ocorrência. Sua oferta ao jogador é adaptação, não diálogo canônico verificado |
| Contexto | A alma artificial está envolvida em uma ocorrência perto da escola, onde existe ameaça Hollow. Contextualizar a ocupação do corpo de Ichigo sem fazer a alma possuir o corpo do jogador |
| Objetivo | “Derrote o Hollow que ameaça a área da escola.” Intervir na ocorrência ligada a Kon; não caçar Hollows aleatórios como etapa desconectada do incidente |
| Início | Urahara oferece/disponibiliza a missão; neste evento, “Iniciar missão” pelo painel/HUD quando o jogador estiver na área designada da escola. Criação do alvo só após esse início explícito válido; não exigir voltar à loja para disparar o confronto |
| Adversário | Hollow da ocorrência inicial ligada à alma modificada; identidade visual/nome e capacidades ainda por confirmar em fonte/ficha. Não transformar Kon em boss hostil que deve ser morto para completar |
| Participação | Jogador/party vence; Kon e os demais personagens ficam no contexto, sem aliados canônicos combatendo. Proposta sem escolta, cronômetro ou barra de defesa para as crianças |
| Desfecho | Texto curto apresenta a sobrevivência/acolhimento da alma artificial e sua continuidade como Kon. Não exigir que o jogador entregue um corpo ou fabrique um recipiente ainda sem ficha |
| Conclusão | Contrato 03B: vitória e desfecho local validam conclusão; disponibilizar Grand Fisher. Não obrigar retorno à loja somente para clicar em concluir |
| Recompensa | Marco de campanha; não conceder Kon como pet, item, aliado ou alma utilizável. Ganhos adicionais, participação e replay ainda por ficha |
| Falha/retomada | M09-06B/07B; área, ocupação, contexto de carregamento e participação exigem M09/M12/M15. Início no painel não cria alvo em outro lugar, teleporte, múltiplas cópias ou instância privada |
| Pendências | Mapa/posição, perfil do alvo, acesso por caminho, participação/recompensas e diálogo final. Inclusão e fluxo aprovados em 05B; sem implementação/teste |

**Escolha de cobertura aprovada:** incluir a missão curta, com confronto da ocorrência e continuidade de Kon no contexto. A alternativa de manter o caso apenas em texto foi preterida; preservada em 12.9. Não reclassificar o caso como filler ou conceder poderes/itens por sua inclusão.

### 10.7. MAIN-SUB-05 — Grand Fisher

**Estrutura aprovada — M13-06A, 28/10/2026.** Resposta “Positivo, adotamos”. Primeiro confronto com Grand Fisher, depois da missão de Kon. CAMP-S01 (8–9) e CAMP-S03 sustentam o contexto do cemitério, a isca com aparência humana e a fuga do adversário. A autoria do jogador segue STR-03; forma de vitória abaixo aprovada em 06A.

| Campo | Estrutura aprovada e pendências |
|---|---|
| Categoria/era | Principal; arco Substituto, primeiro confronto com Grand Fisher |
| Fonte | Rukia apresenta a ameaça e oferece o início no contexto da área do cemitério de Karakura. Encaminhamento ao jogador é adaptação; não transferir a ele a história da mãe de Ichigo |
| Contexto breve | Um Hollow usa uma isca com aparência humana para atrair vítimas. A ligação com a família Kurosaki pode constar do texto de história; não fazer dela parentesco ou memória pessoal do jogador |
| Objetivo comunicado | “Derrote Grand Fisher neste confronto e force sua retirada.” A missão deve declarar que vencer este encontro não é eliminar definitivamente o personagem da história |
| Início | “Iniciar missão” na interação com Rukia no contexto/local válido. Criar a representação de combate deste primeiro encontro; sem disparo por apenas conversar ou visitar o cemitério |
| Lugar | Área preparada associada ao cemitério, com espaço para os movimentos e a representação da isca. Mapa/volume/ocupação ainda exigem M08/M09/M10; sem exigir data do calendário real, visita a túmulo como tarefa ou destruição de construções |
| Versão do adversário | Grand Fisher Hollow deste período; não usar um estado posterior ou misturar capacidades de versões distintas. Isca pertence à representação do inimigo, não é um civil comum a matar |
| Perfil/contexto | A isca é elemento canônico confirmado; seu funcionamento jogável, interação com alvo e repertório exato exigem ficha M09. Não criar cópias de familiares do jogador, acessar dados pessoais ou inventar uma skill de ilusão universal |
| Ação do jogador | Enfrentar e vencer com seus próprios recursos; Rukia não combate como aliada nem encerra o confronto |
| Vitória aprovada | Superar completamente a representação de combate desta tentativa. A condição de derrota do boss gera vitória do encontro; o estado narrativo posterior é ferido/recuado, não morte definitiva |
| Retirada narrativa | Depois da vitória registrada, texto breve informa que Grand Fisher escapou. Não transformar a fuga em falha, exigir perseguição interminável ou iniciar outra luta sem confirmação |
| Limite de execução | M09/M12 devem distinguir derrota do encontro e remoção do ator de combate de morte narrativa. A retirada não depende de dano de NPC, temporizador que substitua a vitória, regeneração infinita ou cancelamento arbitrário de um golpe vencedor. Representação visual/transição e limpeza do ator continuam pendentes |
| Conclusão | Contrato 03B: concluir após vitória válida e desfecho breve, sem retorno administrativo. Liberar o próximo bloco definido para a campanha sem iniciá-lo sozinho |
| Falha/retomada | M09-06B/07B; derrota dos participantes é falha da tentativa, não vitória por cumprir tempo. Nova tentativa autorizada restaura o estado inicial previsto do adversário, sem curar consequências persistentes do jogador |
| Recompensa | Marco da campanha; recompensas adicionais/replay ainda por ficha. Não conceder por associação recompensa de morte definitiva, alma consumível, novo poder ou Mastery |
| Pendências | Mapa, perfil/medidas do adversário, transição pós-vitória, acesso/participação, recompensas e repetição. Estrutura aprovada em 06A; sem implementação/teste |

**Fluxo aprovado:** Rukia apresenta a ameaça → início deliberado → combate do jogador/party → vitória válida contra a representação inicial → texto breve de retirada → conclusão. A fuga é consequência narrativa posterior à conquista do encontro; não disfarça uma luta impossível de vencer.

**Comparação de adaptações:** recomendar vencer a representação inteira e depois registrar retirada, pois entrega uma vitória legível e preserva continuidade. Alternativa: encerrar por limiar de pressão/dano e fazer o adversário fugir antes do fim da luta; mantém a retirada, mas exige objetivo explícito e calibração próprios e pode parecer interrupção prematura. Matar Grand Fisher definitivamente neste encontro mudaria sua continuidade posterior e não é a solução recomendada. A escolha 06A aprovou esta solução para Grand Fisher; não uma regra universal de boss.

### 10.8. Ligação entre Grand Fisher e Uryū — Don Kanonji

**M13-07A APROVADO, 28/10/2026.** Resposta conjunta favorável em 8.6. O caso de Don Kanonji aparece no mangá: CAMP-S04 situa sua gravação no hospital abandonado; CAMP-S01/episódio 10 contextualiza a ocorrência espiritual. Não classificá-lo como filler nem abrir o catálogo de side quests para resolver esta seleção.

**Cobertura aprovada:** resumir o incidente em texto curto de ligação e seguir para a crise de Uryū. Isso mantém o contexto sem acrescentar outro confronto obrigatório ao trecho já composto pelos primeiros casos Hollow. O texto não vira quest para apenas assistir nem exige visita/coleta antes da missão seguinte.

**Alternativa:** incluir uma missão principal complementar no hospital, com apresentação breve e confronto da ocorrência. Se escolhida, receberá ficha/ID e verificação do alvo/objetivo; não foi criada ou aprovada automaticamente. A preferência por texto é uma seleção de cobertura da campanha, não remoção do acontecimento da obra ou autorização para deslocá-lo cronologicamente.

### 10.9. MAIN-SUB-06 — Orgulho Quincy

**Estrutura aprovada — M13-07B, 28/10/2026.** Resposta conjunta favorável em 8.6. CAMP-S01/episódios 11–13, CAMP-S02/14 e CAMP-S03 sustentam desafio com isca, acúmulo de Hollows e chegada do Menos. As duas missões entregam as vitórias ao jogador, sem NPC canônico combatendo como aliado.

| Campo | MAIN-SUB-06A — A isca Quincy | MAIN-SUB-06B — A chegada do Menos |
|---|---|---|
| Categoria/era | Principal; arco Substituto, após Grand Fisher e a ligação escolhida em 07A | Principal; sequência de 06A, antes da crise que leva Rukia de volta à Soul Society |
| Fonte/contexto | Uryū se apresenta como Quincy; o desafio entre ele e Ichigo e o uso da isca explicam a crise em texto. Oferta/intervenção do personagem original é adaptação | Missão disponibilizada ao concluir 06A; aviso breve no painel contextualiza a ameaça maior |
| Objetivo | “Derrote os Hollows atraídos nesta ocorrência.” Conjunto finito do encontro, com composição/quantidade ainda por ficha M09 | “Vença o confronto com o Menos Grande e force seu recuo.” Uma representação da ameaça deste acontecimento |
| Início | Pelo NPC Uryū no contexto/local indicado, com ação deliberada que cria o encontro previsto. Não exigir que o jogador fabrique/consuma uma isca ainda sem ficha | Pelo painel/HUD, com “Iniciar missão” na área válida indicada em Karakura. Não criar o boss imediatamente após concluir 06A nem exigir voltar ao NPC |
| Ação do jogador | Enfrentar os alvos do evento com seus recursos; derrotar outro mob do mundo não substitui o objetivo | Vencer a representação de combate usando seus recursos. Não exigir copiar a defesa/reflexão de Cero executada por Ichigo na obra |
| Participação do elenco | Uryū/Ichigo contextualizam a origem da ocorrência; não matam alvos em paralelo como aliados ou pontuação simulada obrigatória | Uryū/Ichigo não combatem como aliados nem finalizam o boss; jogador/party conquista a vitória conforme STR-03 |
| Escala proposta | Encontro de missão em área designada; não invasão infinita de toda Karakura ou de bases de jogadores. Quantidade/ondas/repertórios pendentes | Área compatível com o porte/ações do Menos, ainda por M09/M10; não destruir terreno ou converter qualquer modelo de chefe genérico em Gillian por tamanho |
| Vitória/conclusão | Todos os objetivos declarados do grupo cumpridos e participação validada; 03B registra 06A e disponibiliza 06B | Vitória do encontro seguida de texto breve sobre o recuo do Menos; concluir pelo contrato 03B. Recuo sustenta CAMP-S03 sem supor morte definitiva |
| Continuidade/falha | Conclusão de 06A persiste como missão concluída; falha posterior em 06B não obriga refazer este grupo | Se a tentativa falhar, tentar novamente apenas 06B autorizado, no estado inicial do boss, conforme M09-07B. Não cura ou repõe recursos/consequências do jogador |
| Recompensas | Marco pessoal; ganhos adicionais/replay por ficha | Marco da crise; ganhos adicionais/replay por ficha, sem recompensa presumida de morte definitiva |

**Herdado e ainda pendente:** início/conclusão seguem 01A/02A/03B; falha/participação e novas tentativas seguem M09/M12/M15. A divisão em duas missões foi aprovada em 07B; não é checkpoint universal de toda luta ou dois prêmios materiais aprovados. Valores, versões visuais, capacidades, ocupação/carregamento da área, acesso/motivação de cada caminho e repetição continuam por integração. Ser Quincy não é requisito automaticamente imposto à campanha comum.

**Adaptação narrativa:** a rivalidade Shinigami/Quincy pertence ao contexto dos personagens da obra, sem atribuir a todos os jogadores a raça ou história de Ichigo. Desenvolvimento de Chad/Orihime pode ser relatado no contexto do arco; não concede seus poderes, formas ou domínio ao jogador. A proposta não cria uma competição de abates contra Uryū nem exige atacar civis para reproduzir a atração da isca.

**Desfecho do Menos:** CAMP-S03 sustenta o afastamento da ameaça. A adaptação aprovada usa vitória jogável seguida de recuo narrativo, explicitamente para este confronto, sem converter 06A de Grand Fisher em regra universal por associação. Remoção/transição da representação ainda exige ficha, sem NPC finalizador ou fase de combate secreta.

**Efeito na campanha:** a presença do Menos e a reação da Soul Society contextualizam o próximo bloco, MAIN-SUB-07. Não iniciar automaticamente Renji/Byakuya, prender o personagem ou alterar a era global de todos ao concluir a missão; as consequências pertencem aos marcos e à integração M03/M12/M15.

**Alternativa de estrutura:** uma única missão contínua, grupo de Hollows seguido do Menos na mesma tentativa. Reduz a interrupção entre objetivos, mas torna a tentativa mais longa; retenção de progresso intermediário exigiria checkpoint explícito próprio. A divisão foi escolhida em 07B para que a primeira vitória permaneça concluída quando o jogador precisar retomar o boss.

### 10.10. MAIN-SUB-07 — Rukia é levada

**Estrutura aprovada — M13-08A, 28/10/2026.** Resposta “Concordo”. CAMP-S02 (16–18) e CAMP-S03 confirmam Renji/Byakuya buscando Rukia e sua condução à Soul Society. STR-03 já protege a prisão, dá a vitória jogável ao jogador nos confrontos adaptados e mantém sua identidade original. A seleção aprovada usa Renji como confronto deste trecho e Byakuya no contexto do desfecho.

| Campo | Estrutura aprovada e pendências |
|---|---|
| Categoria/era | Principal; fim do bloco Substituto, após MAIN-SUB-06B |
| Fonte/contexto | Kon informa que Rukia saiu; aviso breve encaminha à ocorrência. CAMP-S02/16 sustenta o aviso original a Ichigo; aplicá-lo ao jogador é adaptação. Não torna Kon um pet/aliado de combate |
| Objetivo comunicado | “Vença o confronto contra Renji.” Apresentação deixa claro que o resultado jogável não resolve ainda o resgate de Rukia |
| Início | Missão disponível após o bloco do Menos; jogador usa “Iniciar missão” pelo painel no local indicado em Karakura. Não iniciar luta ou prisão ao apenas concluir o Menos, aproximar-se do local ou abrir a conversa |
| Adversário | Renji na versão de seu primeiro confronto deste acontecimento; representação de boss distinta do NPC persistente. Repertório, estado da arma, capacidades e números exigem M09; não importar formas posteriores |
| Vitória | Jogador/party derrota a representação de combate de Renji, sem Rukia/Ichigo/Byakuya lutando como aliados ou finalizando por ele |
| Destino dos envolvidos | Vitória não significa morte definitiva de Renji. Texto breve apresenta a presença/autoridade de Byakuya e a ida de Rukia com os emissários à Soul Society, preservando o marco de prisão |
| Byakuya neste trecho | Aprovado: contexto escrito, sem boss invencível, golpe obrigatório que mate o jogador ou outra luta escondida após vencer Renji. Seu confronto jogável fica para a etapa pertinente do arco Soul Society, ainda por ficha |
| Coerência do objetivo | A missão conclui pela vitória sobre Renji. Não prometer “salve Rukia agora” e depois invalidar sucesso porque a prisão é protegida; o resgate motiva o arco seguinte |
| Consequências do jogador | Não aplicar automaticamente morte, perda de poderes/itens, dano à arma, troca de raça ou treino de recuperação por analogia com o que acontece a Ichigo |
| Conclusão/continuidade | Vitória e desfecho breve → conclusão registrada por 03B → MAIN-SUB-PREP-01 com Urahara/Ururu e, depois, MAIN-SUB-08. Sem retorno administrativo ou transição involuntária de dimensão |
| Falha | M09-06B/07B; se o jogador não vencer Renji, não receber o marco pela simples ocorrência de uma derrota. Não encerrar a tentativa por dano de um capitão impossível de vencer |
| Pendências | Motivação/acesso por caminho, cena/local e alvo em M08/M09/M10, participação e recompensas/replay em M12/M15. Estrutura aprovada em 08A, sem implementação |

**Adaptação explícita:** o resumo oficial descreve Ichigo ferido e derrotado por Byakuya. A adaptação aprovada não repete esse resultado como punição forçada ao personagem original: seleciona a luta vencível contra Renji e mantém prisão/Byakuya no contexto. Isso aplica a convenção de STR-03 sem afirmar que o jogador derrotou Byakuya aqui, impediu a prisão ou assumiu a trajetória de Ichigo.

**Alternativa de cobertura:** acrescentar um objetivo declarado de fuga/retirada perante Byakuya, com sucesso próprio e sem simular um boss eliminável. Exigiria uma segunda atividade e regras de saída; não foi detalhado/aprovado. Não recomendar uma luta apresentada como vencível cujo único resultado permitido seja perder.

### 10.11. MAIN-SUB-08 — Rumo à Soul Society

**Ligação após a preparação M13-08B/C, 28/10/2026.** A inclusão do treino foi aprovada; 10A aprova agora o recorte de travessia breve, com Kōtotsu no contexto. Detalhes de realização do percurso continuam pendentes. CAMP-S02 (18–21) e CAMP-S03 sustentam a preparação por Urahara e a expedição. M10b/9.5 admite fontes especiais na primeira chegada de campanha, distintas dos operadores da rota comum. Não é primeira visita universal à dimensão: um personagem já pode tê-la visitado por formação/acesso legítimo anterior.

| Campo | Estrutura aprovada e pendências |
|---|---|
| Categoria/era | Principal; ligação entre a prisão de Rukia e o arco Soul Society |
| Fonte | Urahara apresenta brevemente o plano e oferece a expedição contextual. Acesso/motivação por origem e lealdade permanecem integrações M03/M04/M08/M15, sem tornar o NPC fonte universal de toda viagem |
| Objetivo | “Atravesse a passagem da expedição e chegue ao Rukongai.” Concluir por deslocamento real do jogador, não por assistir a uma conversa |
| Pré-requisito narrativo | Concluir MAIN-SUB-07 e a preparação MAIN-SUB-PREP-01 no caminho compatível e receber a disponibilidade da expedição. Não impor morte/perda de poderes para disponibilizar o plano |
| Início | Pela interação explícita com Urahara, no contexto da passagem. Personagem confirma que vai partir; não teleportar ao abrir diálogo ou ao terminar Renji |
| Ação | Usar a passagem da expedição, percorrer trecho curto do Dangai e alcançar o destino definido do Rukongai. Kōtotsu no contexto escrito; não há perseguição jogável adicional nesta cobertura, conforme 10A. Representação/controles, chegada e riscos executáveis ainda exigem M10/M12, sem dano inevitável aprovado |
| Preparação de Ichigo | Contexto escrito pode relatar seu treino/recuperação de poderes. Não obrigar todos a refazer formação racial, cair em um poço com prazo de Hollowficação, perder/restaurar poderes ou adquirir Shikai por missão principal |
| Progressão do jogador | Permanecem requisitos próprios de capacidades e SP/prática. A preparação em 10.12 foi incluída neste trecho da campanha em 08B/C; não constitui formação racial universal. Aptidão por estágio/corpo/afiliação continua exigindo integração |
| Conclusão | Presença válida no destino da expedição registra chegada e disponibiliza o primeiro objetivo do arco Soul Society. Trocar de dimensão por outro meio não comprova sozinho a realização dessa missão |
| Recompensa/alcance | Marco da expedição; viajar como passageiro não ensina Senkaimon, concede skill, ingresso no Seireitei ou livre trânsito institucional. Recompensas adicionais/replay pendentes |
| Falha/retomada | Falha de viagem/posição tratada por M10; progresso da vitória em Renji permanece. Morte/abandono/desconexão no percurso não conclui por associação; ponto de retomada e participação exigem ficha de percurso/M12/M15 |
| Retorno e mundo compartilhado | Fonte/rota de volta efetiva e condições de viagem comuns seguem M10/M10b, inclusive restrição fora de combate onde aplicável. Não assumir que operadores institucionais aceitem automaticamente uma expedição de resgate irregular; validar esse contexto antes de implementar |
| Pendências | Percurso/risco/retomada, entrada/saída viáveis e autorizações por contexto/corpo, destino concreto, participação e recompensas. Não declara rota pronta, mapa ou portal implementado |

**Revisão de cobertura em 28/10/2026:** a recomendação inicial de somente plano/travessia foi substituída pela escolha de incluir preparação prática antes da expedição (08B/C). Recuperação de Ichigo continua no contexto; formação e progressão do jogador mantêm seus contratos. O treino não torna todo caminho automaticamente compatível com a expedição nem fecha os detalhes de viagem.

### 10.12. MAIN-SUB-PREP-01 — Preparação para o resgate

**Inclusão aprovada — M13-08B/C, 28/10/2026.** Usuário aceitou o duelo explicado e acrescentou perseguição cronometrada. Organização em duas etapas no campo subterrâneo da loja, entre Renji e a travessia ao Rukongai. 09A/B agora aprovados: 60 segundos nominais/30% de Vida como valores provisórios, e retenção da etapa de Ururu se o duelo seguinte falhar.

**Referência e adaptação:** CAMP-S02/18–20 sustenta preparação, campo subterrâneo e treino com Urahara; CAMP-S05 identifica Ururu e seu vínculo com a loja. A cena de perseguição foi lembrada pelo usuário; não houve acesso integral ao episódio/capítulo para confirmar todos os detalhes do exercício original. Cronômetro, percentual de Vida, validação do golpe e perfil controlado são adaptações de gameplay, não regras literais comprovadas do mangá. Não importar capacidades de versões posteriores de Ururu ou mecânicas de Brave Souls.

| Campo | Estrutura e limites |
|---|---|
| Categoria/era | Campanha principal; preparação da expedição de resgate, após MAIN-SUB-07 e antes de MAIN-SUB-08 |
| Fonte/lugar | Urahara apresenta a avaliação no campo subterrâneo da loja; Ururu participa da primeira etapa. Acesso físico, área válida e motivação por caminho exigem M08/M10/M12/M15 |
| Início | Entrar no campo e iniciar explicitamente a etapa disponível. Conversar não dispara perseguição. Criar/ativar apenas os atores da tentativa válida; cliques repetidos não duplicam o confronto |
| Etapa A — Ururu | Ururu persegue e ataca o jogador durante 60 segundos nominais. Jogador usa movimentação, esquiva/defesa e seu repertório atual para permanecer acima de 30% da Vida máxima de referência durante todo o intervalo; parâmetros provisórios aprovados em 09A. Não há objetivo de esgotar a Vida de Ururu |
| Objetivo mostrado — A | “Resista à perseguição de Ururu durante o tempo indicado sem atingir o limite de Vida.” Interface mostra tempo restante e limite antes do início e durante o teste; valores iniciais aprovados em 09A, calibração em jogo ainda pendente |
| Sucesso — A | Completar o intervalo válido sem ocorrência da condição de falha. Falhar ao atingir 30% ou menos em qualquer momento, conforme 09A; relógio e casos de integração abaixo; não basta estar acima dele somente ao fim |
| Etapa B — Urahara | Após aprovação em A, disponibilizar o duelo e exigir início deliberado. Representação de Urahara com perfil limitado e fixado para este teste, sem acompanhar automaticamente os atributos do jogador |
| Sucesso — B | Um golpe válido de participante elegível no alvo da tentativa ativa; não bater no NPC de serviço. Objetivo aceito em 08B; não exige esgotar a Vida completa de um capitão |
| Encerramento | Ao cumprir/falhar o objetivo, encerrar a ação ofensiva dos atores do treino. Interrupção controlada antes de finalização mortal foi aceita na proposta de Urahara; para Ururu, limiar exige encerramento próprio por ficha M09. Não é imunidade geral a dano externo, inventário protegido, reposição de Vida/recursos ou funcionalidade implementada |
| Conclusão | Aprovação nas duas etapas registra preparo e disponibiliza a expedição. Viagem segue sua própria ação explícita; não teleportar ao acertar Urahara |
| Falha e retomada | Nova tentativa autorizada começa no estado inicial definido para o teste; Renji continua concluído. 09B aprovado: retenção da etapa A ao falhar em B, repetindo somente o duelo com Urahara; não assumir checkpoint universal |
| Participação | Elegibilidade, teste individual ou coletivo, terceiros e crédito pessoal exigem M12/M15. Um participante não recebe aprovação por outro apenas ficar correndo; não definir automaticamente party de proximidade |
| Recompensa/limites | Marco de preparação; não concede Shikai, skill, forma ou Mastery automática, nem refaz formação. Ganhos adicionais/replay continuam por ficha |

**M13-09A — Parâmetros aprovados e integrações ainda necessárias:**

- Ponto de partida para balanceamento: **60 segundos nominais e falha ao atingir 30% da Vida máxima de referência ou menos**. Hipótese provisória do mod aprovada pelo usuário, ajustável depois; sem validação em jogo e sem pretensão de reproduzir duração canônica.
- Usar como referência a Vida máxima válida no início da etapa e exibir o limite correspondente; mudanças de forma/Vida máxima, dano absorvido, custos de Vida e recuperação precisam de integração M05/M07/M09 antes de implementar. Não recalcular silenciosamente o limite a cada troca de forma para permitir contorná-lo. Iniciar já no limite ou abaixo deve impedir o início e explicar a condição, sem cura automática.
- Atingiu o limite em qualquer momento: falha imediata; recuperar Vida depois não apaga o registro. Isso mede todo o intervalo proposto pelo usuário. Se dano e fim do cronômetro coincidirem na validação, não conceder sucesso a uma tentativa que já atingiu o limite.
- Contar somente tempo de tentativa ativa em simulação. Descarga/reinício seguem M09-08A (prova interrompida e retomada manual em nova tentativa); desconexão/saída não rende tempo nem aprovação. O detalhe de abandono/reentrada continua por ficha.
- Manter perseguição efetiva e área de treino viável: M09/M10 precisam tratar bloqueio do caminho, alterações de terreno, posições inalcançáveis, voo/teleporte e perda do alvo. Não aprovar simplesmente ficar escondido ou inacessível enquanto o relógio corre; também não conceder a Ururu detecção através de paredes, teleporte ou ataques inventados para contornar qualquer habilidade. Regras concretas ainda pendentes.

**M13-09B — Retenção aprovada:** após concluir A, guardar esse resultado; se o jogador falhar em B, repetir somente o duelo com Urahara. Evita repetir uma etapa de movimentação já demonstrada para cada nova tentativa do golpe. É checkpoint específico desta avaliação; política de repetição após conclusão, participação, recursos e recompensas continua nos módulos responsáveis. Alternativa preterida nesta escolha: avaliação contínua que reinicia A quando B falha.

**Fluxo recomendado:** Urahara apresenta → acesso ao campo → iniciar perseguição → resistir a Ururu → iniciar duelo → acertar Urahara → preparo registrado → expedição disponível. Sem poço de Hollowficação, recuperação obrigatória de poderes de Ichigo, aliado canônico vencendo luta pelo jogador ou troca de foco para quests raciais.

### 10.13. MAIN-SS-01 — O guardião do Seireitei

**Estrutura aprovada — M13-10B, 28/10/2026.** Resposta conjunta “Perfeito, podemos adotar isso sim”. Primeira ficha do arco Soul Society, após MAIN-SUB-08. CAMP-S02/21–22 e CAMP-S06 sustentam chegada ao Rukongai, impedimento pelo guardião Jidanbō, vitória de Ichigo na obra e fechamento da entrada por Gin. Seleção de objetivo, autoria e início abaixo são adaptações aprovadas no alcance de 10B sob STR-03; nenhuma luta de capitão invencível foi aprovada.

| Campo | Estrutura aprovada e pendências |
|---|---|
| Categoria/era | Principal; abertura da infiltração da Soul Society, expedição de resgate de Rukia |
| Pré-requisito | Chegada válida da expedição ao Rukongai registrada em MAIN-SUB-08; capacidade/contexto da rota compatível. Não chamar isso de primeira visita universal à dimensão |
| Fonte e apresentação | Jidanbō se apresenta brevemente como guardião no acesso ao Seireitei e declara o desafio. Yoruichi pode contextualizar a rota em texto, sem lutar pelo jogador. Falas exatas e presença por contexto ainda por M08 |
| Lugar | Área designada junto ao portão do Seireitei, do lado do Rukongai. Cenário/ocupação, tamanho do guardião e acesso físico exigem M09/M10; não alterar construções arbitrárias ou prender todos os usuários do portão |
| Início | Interação explícita “Iniciar missão” no contexto de Jidanbō junto do portão. Apresentação não dispara ataques; criar/ativar uma única representação de confronto da tentativa |
| Objetivo | “Vença o desafio de Jidanbō.” Explicar que superar o guardião é o objetivo atual; não prometer entrada garantida no Seireitei ou resgate imediato de Rukia |
| Adversário | Jidanbō na versão deste primeiro acontecimento; ator do encontro separado da presença persistente. Repertório, armas, Vida/dano/alcance e execução exigem fonte/ficha M09; sinopse não prova todos os golpes |
| Vitória | Jogador/party vence a representação de combate com seu repertório. Sem Ichigo/Yoruichi lutando como aliados, sem último golpe de NPC. Superar o guardião não significa matá-lo definitivamente |
| Desfecho aprovado | Texto breve: Jidanbō reconhece o resultado; Gin intervém e o acesso é fechado. Não iniciar outra luta, dano obrigatório ou derrota forçada do jogador; não é morte narrativa de Jidanbō |
| Conclusão | Vitória e desfecho local concluem por 03B. Intervenção de Gin não apaga o sucesso; Yoruichi encaminha a busca de outra entrada, por Kūkaku; próxima atividade proposta em MAIN-SS-02/10.14 |
| Mundo compartilhado | Fechamento no desfecho desta missão não aprova bloquear globalmente acessos legítimos de todos os jogadores nem impedir visitas anteriores. Representação narrativa/estado de acesso por progresso exige M10/M12/M15 |
| Falha e retomada | M09-06B/07B, participação M12/M15; falhar contra Jidanbō exige nova tentativa autorizada, sem refazer o treino ou a viagem já concluídos. Limpeza/posição de retorno e presença de terceiros ainda por ficha |
| Recompensas e pendências | Marco de campanha; ganhos adicionais/replay, mapa, elegibilidade/acesso por caminho, perfil e integração com serviços permanecem abertos. Não declara NPC/boss/quest implementado |

**Cobertura aprovada em 10B:** Jidanbō jogável e Gin no texto do desfecho. Entrega o primeiro confronto reconhecível da chegada e dá motivo à entrada alternativa, sem alongar o trecho com uma luta adicional. Alternativa preterida: acrescentar uma atividade própria de retirada perante Gin, com objetivo declarado e condição de sucesso; não foi desenvolvida. A mera presença de Gin não autoriza um boss aparentemente vencível cuja única resolução seja perder.

**M13-10A — Recorte aprovado do percurso anterior:** MAIN-SUB-08 tem travessia curta e controlada até o Rukongai, sem nova prova cronometrada; encontro com Kōtotsu no contexto escrito. Seleção de cobertura aceita, não exclusão do acontecimento da obra nem rota institucional comum liberada. A alternativa de perseguição jogável do Kōtotsu foi preterida nesta escolha. Perigos, controles, proteção de dano e duração exata ainda exigem integração M10/M12; não são aprovados por associação.

### 10.14. MAIN-SS-02 — Entrada pelos céus

**Estrutura aprovada em 11B, 28/10/2026; resposta “Podemois seguir assim”.** CAMP-S02/23 apresenta a briga de Ganju/Ichigo interrompida e busca de Kūkaku; /24–25 descreve formação e controle da esfera de energia para o canhão. CAMP-S06 descreve a entrada pelo lançamento e separação dos personagens. São sinopses oficiais do anime, não leitura integral dos capítulos. Exercício de interface foi aceito como adaptação em 11B; detalhes de validação continuam por integração; não afirmar que existiam comandos/barra específicos na obra.

**11A — Cobertura escolhida para Ganju:** incluir confronto jogável antes desta preparação, conforme “Prefiro incluir”. A proposta inicial de somente contexto foi preterida. Ficha MAIN-SS-GANJU-01 em 10.15; vitória completa posteriormente aprovada em 12A. Não transportar sua rivalidade pessoal com Ichigo para a biografia do jogador.

| Campo | Estrutura aprovada em 11B e pendências |
|---|---|
| Categoria/era | Principal; preparação da infiltração alternativa após o bloco Jidanbō/Ganju |
| Fonte/lugar | Yoruichi indica Kūkaku; ao chegar à residência/área de lançamento, Kūkaku se apresenta brevemente e explica seu método. A indicação é contexto de orientação, sem criar missão apenas para assistir ao encontro |
| Pré-requisito | Vitória contra Jidanbō e desfecho de acesso fechado registrados; incluir resolução da missão de Ganju escolhida em 11A, com condição proposta em 12A. Condições físicas/contexto por caminho e autorização de Kūkaku ainda por M03/M04/M08/M10/M15 |
| Objetivo mostrado | “Estabilize a esfera de energia e use o canhão de Kūkaku para entrar no Seireitei.” Exercício preparatório curto e deslocamento real, não simples conclusão por diálogo |
| Início | Interação explícita com Kūkaku na área de treino; jogador inicia a preparação quando estiver apto. Conversa não dispara exercício nem lançamento |
| Exercício aprovado | Usar o dispositivo de treino orientado por Kūkaku para formar/manter a esfera estável. Interface mostra um marcador e uma faixa-alvo; jogador ajusta a intensidade com comandos simples e mantém o marcador na faixa durante um intervalo breve. Não é apenas esperar parado por um cronômetro |
| Parâmetros abertos | Duração, tolerância, resposta dos comandos, custo e efeitos de errar ainda por ficha M05/M06/M12/M14; não fixar números sem necessidade ou teste. Dispositivo/forma de interação não foi confirmado como item de inventário ou recompensa |
| Sucesso e erro no exercício | Tempo válido de estabilidade conclui o objetivo de preparação. Sair da faixa exige nova tentativa do exercício, com regra de tolerância ainda por definir; não matar, perder poderes ou repetir Jidanbō por um erro de controle |
| Lançamento | Após estabilidade aprovada, Kūkaku disponibiliza a partida pelo canhão; jogador inicia a partida explicitamente no contexto válido. Lançamento leva à área de chegada da missão dentro do Seireitei. Trajeto/apresentação, posição segura e falhas de viagem exigem M10/M12; não pressupor veículo livre, voo permanente ou projétil que destrua construções |
| Conclusão | Objetivo de preparo reconhecido e chegada válida pelo lançamento concluem a missão por 03B. Um outro meio de visitar o Seireitei não comprova sozinho essa execução da expedição |
| Continuidade | Disponibilizar o primeiro confronto após a entrada, a detalhar com Ikkaku. A separação do elenco pode constar do contexto; não dividir automaticamente uma party de jogadores ou gerar NPC canônico aliado de combate |
| Progressão | Exercício contextual do método de entrada, não compra/desbloqueio automático de controle espiritual, skill de barreira, forma ou Mastery. Uso futuro autônomo de uma capacidade segue requisitos, SP e prática próprios |
| Falha/retomada | Treino, viagem e participação exigem M12/M15. Perda de conexão, ator/área indisponível e falha no lançamento não contam como chegada; retenção do exercício nesses casos ainda por integração, sem reset de Jidanbō já concluído ou teleporte arbitrário |
| Atores/recompensas | Kūkaku/Yoruichi/Ganju contextualizam e operam a entrada; não combatem/finalizam inimigos pelo jogador. Recompensa adicional/replay pendentes; não entregar canhão, dispositivo ou aliado por associação |

**Escolha aprovada em 11B:** uma missão curta de preparo e lançamento, com exercício ativo de controle; confronto com Ganju incluído anteriormente pela escolha 11A. Varia a ação antes de retomar os combates dentro do Seireitei. Alternativa preterida em 11B: Kūkaku explica o método e o jogador inicia diretamente a partida pelo canhão, sem exercício de estabilidade. Ambas exigem realização da viagem; nenhuma equivale a apenas assistir ao encontro dos personagens.

### 10.15. MAIN-SS-GANJU-01 — Desafio no Rukongai

**Inclusão/desfecho aprovados em M13-11A/12A, 28/10/2026.** Resposta “Concordo” aprova a vitória completa. A briga de Ganju com Ichigo é interrompida no resumo CAMP-S02/23; vitória concluída do personagem é adaptação assumida do mod, não resultado literal comprovado da briga original.

| Campo | Estrutura aprovada e pendências |
|---|---|
| Categoria/ordem | Principal; após Jidanbō/Gin e antes da preparação de Kūkaku, sem renumerar MAIN-SS-02 |
| Fonte/contexto | Ganju se apresenta no Rukongai, no contexto do encontro e desconfiança perante a expedição; breve interação oferece o desafio adaptado. Diálogo/motivo por origem precisam de M03/M04/M08, sem presumir que todo personagem é Shinigami/Ichigo |
| Início | Ação explícita de iniciar pelo NPC no local designado da ocorrência. Aproximar-se, abrir diálogo ou assistir ao contexto não dispara agressão |
| Objetivo aprovado | “Vença o desafio de Ganju.” Superar a representação de combate desta tentativa com o repertório atual; não transformar a atividade em assistir à interrupção por despertador |
| Adversário | Ganju na versão inicial do Rukongai, separado do NPC persistente. Repertório, arma, efeitos, Vida/dano/alcance e tamanho da área exigem fonte/ficha M09; não inventar ataques para completar o catálogo |
| Sucesso aprovado — 12A | Vitória integral do encontro pelo jogador/party; Ganju cede/encerra o desafio e permanece vivo para a continuidade. Texto breve encaminha a relação com Kūkaku, sem lhe atribuir vitória ou roubar a do jogador |
| Limites de adaptação | Não exigir combate desarmado, montagem em javali, noite real, despertador ou poder emprestado sem decisão própria. Não reproduzir uma interrupção inevitável que impeça o objetivo de vencer comunicado |
| Conclusão/continuidade | Vitória validada conclui por 03B e disponibiliza a preparação com Kūkaku; sem combate/lançamento automático. Resultado não concede Ganju como aliado combatente |
| Falha/pendências | M09/M12/M15 para tentativa/participação, M10 para área; ganhos/replay pendentes. Nova tentativa não repete Jidanbō já concluído. Inclusão/desfecho aprovados em 11A/12A; nenhuma realização testada |

**Escolha aprovada em 12A:** vitória completa e Ganju vivo no desfecho, para dar resultado claro ao confronto incluído. Alternativa preterida: objetivo explícito de provar capacidade por um limiar de pressão, encerrando o desafio antes da derrota completa; exigiria definir/comunicar outro objetivo. Não é adaptação obrigatória de todo encontro originalmente interrompido.

### 10.16. MAIN-SS-03 — Primeiros confrontos no Seireitei

**Estrutura aprovada em M13-12B, 28/10/2026; resposta “seguimos assim”.** CAMP-S06 e CAMP-S07/26–28 descrevem Ikkaku contra Ichigo e Yumichika contra Ganju após a entrada, com lutas paralelas. Seleção aprovada: adaptar os dois adversários em missões sequenciais para o jogador, sem dividir forçadamente uma party ou criar Ganju aliado combatente. A aprovação vem de 12B, não por associação ao exercício de Kūkaku.

| Campo | MAIN-SS-03A — Ikkaku | MAIN-SS-03B — Yumichika |
|---|---|---|
| Fonte/contexto | Ikkaku aparece no setor designado da chegada após o lançamento, identifica a incursão e apresenta o confronto em contexto breve | Após Ikkaku, contexto breve situa a continuação da reação da 11ª Divisão e o encontro com Yumichika; no original seu confronto é com Ganju |
| Pré-requisito | Chegada válida pelo lançamento de MAIN-SS-02 | Conclusão de MAIN-SS-03A; não exige que NPC Ganju tenha vencido luta fora de cena pelo jogador |
| Início | Painel/HUD no local válido do encontro, em ação explícita que cria/ativa o alvo da tentativa | Outro início explícito no painel/HUD no local válido; vencer Ikkaku não inicia Yumichika automaticamente |
| Objetivo | Vencer a representação de Ikkaku deste primeiro confronto | Vencer a representação de Yumichika deste período |
| Adversário/perfil | Ikkaku da entrada da Soul Society; estado da arma/repertório/números por fonte e ficha M09, sem importar Bankai ou capacidades de aparições posteriores | Yumichika deste encontro; repertório/estado da arma/números por fonte e ficha M09, sem importar revelações/capacidades posteriores |
| Vitória/desfecho | Vitória do jogador/party, sem NPC aliado/finalizador; Ikkaku permanece narrativamente vivo. Conclusão pelo contrato M13-03B disponibiliza MAIN-SS-03B | Vitória do jogador/party, Yumichika permanece narrativamente vivo. Conclusão pelo contrato M13-03B encaminha o avanço até a prisão de Rukia; próximas propostas Jirōbō/Hanatarō em 10.17/10.18 |
| Falha/retomada | M09/M12/M15; treino/lançamento já concluídos não são refeitos a cada derrota | Retenção aprovada em 12B: Ikkaku concluído permanece; falhar em Yumichika repete somente a segunda missão |

**Integrações abertas:** motivo/acesso por caminho, ocupação da chegada e estado compartilhado do mundo, participação/reentrada, capacidades dos adversários, mapas e recompensas/replay. Não introduzir ondas de soldados genéricos, corrida de fuga ou escolta de Ganju sem ficha própria.

**Escolha aprovada em 12B:** dois encontros em sequência, Ikkaku e depois Yumichika, preservando cada missão concluída. Isso adapta para o personagem original os dois confrontos reconhecíveis da chegada. Alternativa preterida: Ikkaku como única luta obrigatória e a ocorrência de Yumichika/Ganju no texto. Não exige copiar todas as batalhas paralelas da obra por uma regra universal.

### 10.17. MAIN-SS-04 — As lâminas de Jirōbō

**Estrutura aprovada — M13-13A, 28/10/2026; resposta “Adotamos”.** CAMP-S07/28–29 situa Jirōbō diante de Uryū/Orihime, com ataque de lâminas da Zanpakutō; CAMP-S06 também registra esse confronto paralelo. Depois das duas missões da 11ª Divisão, jogador executa este confronto da campanha conforme 13A. Não é filler ou quest racial Quincy. Autoria, ordem sequencial e ausência de defesa de NPC são adaptações aprovadas em 13A, não reprodução literal de quem luta na obra.

| Campo | Proposta |
|---|---|
| Categoria/era | Principal; avanço inicial no Seireitei após MAIN-SS-03B e antes do caminho subterrâneo de Hanatarō |
| Contexto | Ocorrência de Uryū/Orihime e a presença de Jirōbō apresentadas brevemente no painel. Ambos contextualizam; não entram como aliados para lutar ou finalizar o inimigo |
| Início | “Iniciar missão” no painel/HUD quando disponível e no setor válido do Seireitei. Validar contexto antes de criar o ator; sem duplicar tentativa ou surgir em base arbitrária |
| Objetivo | “Derrote Jirōbō.” Confronto direto do jogador/party; não adicionar escolta, cronômetro ou barra de defesa de Orihime para reproduzir sua ameaça na história |
| Adversário | Jirōbō deste primeiro encontro, em representação separada da presença persistente. CAMP-S07/29 confirma lâminas da Zanpakutō; funcionamento, estado exato, alcance, quantidade/dano e contrajogo exigem ficha M09, sem importar técnicas de outro jogo ou poder concedido ao jogador |
| Vitória/desfecho | Superar completamente a representação do encontro; narrativa preserva o adversário vivo, sem morte definitiva ou NPC finalizador. Ameaça da ocorrência encerrada pelo jogador |
| Conclusão | Vitória validada conclui por M13-03B e disponibiliza a aproximação com Hanatarō, sem início automático. Não exige ser Quincy, usar flechas ou receber repertório de Uryū |
| Ligação | Cerco de soldados genéricos referido em CAMP-S07/29 fica no contexto desta transição, sem nova missão de ondas obrigatórias. É seleção de cobertura aprovada no pacote 13A, não aprovação de todo confronto paralelo futuro |
| Falha/pendências | M09/M12/M15: nova tentativa não repete Ikkaku/Yumichika já concluídos. Acesso/motivação por caminho, cenário, perfil, participação/recompensas e replay continuam pendentes |

**Escolha aprovada em 13A:** incluir Jirōbō como próxima missão de combate, mantendo no contexto o cerco genérico para chegar ao novo caminho. Alternativa preterida: relato do confronto de Uryū/Orihime e ida direta a Hanatarō, sem Jirōbō jogável nesta sequência.

### 10.18. MAIN-SS-05 — O caminho sob o Seireitei

**Estrutura aprovada — M13-13B, 28/10/2026; resposta “Seguimos assim”.** CAMP-S07/30 e CAMP-S06 descrevem Hanatarō guiando pelas galerias subterrâneas em direção à prisão de Rukia, com Renji impedindo o avanço. Presença/indicação de Hanatarō e percurso são referências do anime oficial; execução sem escolta aprovada em 13B; mapa e marcações ainda por integração. Não ler uma sinopse como prova de todas as portas/perigos/condições da rota.

| Campo | Proposta |
|---|---|
| Categoria/ordem | Principal; após o bloco inicial de confrontos, antes de enfrentar Renji novamente na Soul Society. Jirōbō integra essa ordem conforme 13A aprovado |
| Fonte | Hanatarō se apresenta brevemente e explica que conhece a passagem e a situação de Rukia, de quem cuidou. Motivo/acesso por caminho exigem integração; não universalizar lealdade/afiliação do jogador |
| Início | Interação explícita com Hanatarō junto ao acesso designado às galerias. Conhecer o NPC ou abrir conversa não inicia deslocamento, combate ou cura |
| Objetivo | “Percorra a passagem subterrânea e alcance a saída indicada próxima à prisão de Rukia.” Ação real do jogador pelo caminho, sem missão somente para ouvir explicação |
| Percurso aprovado | Rota curta e legível pelas galerias, com orientação de Hanatarō e indicações de caminho. Pontos de passagem e chegada válidos comprovam o percurso; duração/distância/mapa e representação de orientação ainda por M10/M12/M14. Não inventar coleta de chaves, labirinto ou onda obrigatória de inimigos |
| Papel de Hanatarō | Fonte/orientação narrativa; sem barra de defesa, ritmo de caminhada a acompanhar ou escolta de NPC vulnerável nesta proposta. Não é aliado de combate, pet, mentor universal ou cura gratuita automática |
| Sucesso | Percurso e saída da missão validamente alcançados, com participação elegível; não basta visitar outro lugar da prisão por meio livre. Não exige primeira visita universal ao Seireitei |
| Conclusão/continuidade | M13-03B: registra aproximação e disponibiliza o segundo confronto de Renji. Este encontro terá início próprio e ficha proposta em 10.19; não spawnar Renji automaticamente ao cruzar a saída ou concluir esta missão |
| Limite de história | Chegar à saída não liberta Rukia, conclui resgate ou substitui os confrontos posteriores. Não obrigar o jogador a desmaiar/ser curado por analogia ao que ocorre com Ichigo depois de Renji |
| Mundo/acesso | Existência e uso desta rota da expedição não ensinam skill de viagem ou abrem toda a rede de galerias para qualquer origem/era. Acesso/retorno, ocupação, pontos válidos e proteção do trecho exigem M10/M12/M15 |
| Falha/retomada | Morte, abandono, desconexão e troca de dimensão não validam a chegada. Ponto de retomada/interrupção e participação exigem ficha; missões de combate concluídas permanecem, sem reset de mundo ou teleporte presumido |
| Recompensas/pendências | Marco de aproximação; ganhos adicionais/replay pendentes. Sem item, cura, skill ou Mastery automática. Perfil de percurso e objetivo ainda não implementados/testados |

**Escolha aprovada em 13B:** caminho jogável curto orientado por Hanatarō, sem escolta obrigatória. Alternativa preterida: levar Hanatarō como NPC de escolta com condição própria de defesa/ritmo/falha; não foi desenvolvida. Não transformar apenas conversar com o NPC em prova de ter percorrido a rota.

### 10.19. MAIN-SS-06 — Renji, determinação na Soul Society

**Estrutura/interlúdio aprovados — M13-14A, 28/10/2026; resposta “Concordo”.** O adversário já foi indicado na rota aprovada de 13B. CAMP-S07/30–32 e CAMP-S06 descrevem Renji impedindo o avanço e sua derrota no confronto de Ichigo; /34–35 contextualiza reações e aparente morte de Aizen. Esta ficha registra o recorte aprovado, com realização ainda por integrar, sob autoria comum STR-03; não é nova votação da regra geral de vitória do jogador.

| Campo | Proposta |
|---|---|
| Categoria/ordem | Principal; após a saída validada de MAIN-SS-05, antes de continuar a aproximação da prisão |
| Fonte/contexto | Renji surge como opositor na área designada; breve contexto situa o impedimento. Não transferir ao jogador as memórias, laços ou passado de Ichigo/Renji/Rukia |
| Início | Missão disponibilizada após a rota; ação explícita no painel/HUD quando no local válido cria/ativa a representação do confronto. Sem spawn automático ao sair da galeria |
| Objetivo | “Derrote Renji e supere este impedimento.” Confronto próprio, não replay administrativo da luta de Karakura nem resgate imediato de Rukia |
| Versão/perfil | Renji da Soul Society, com Zabimaru conforme CAMP-S07/31. A fonte distingue sua força da versão limitada no Mundo Humano, mas isso não aprova multiplicador automático de cinco vezes em todos os atributos. Estado de arma, repertório, alcance, dano/Vida e contrajogo exigem perfil M09 da versão; sem Bankai ou revelações posteriores |
| Ação e vitória | Jogador/party enfrenta e vence a representação com o repertório que já possui. Sem NPC aliado/finalizador, poder emprestado ou exigência de copiar uma técnica exclusiva de Ichigo |
| Desfecho aprovado | Renji permanece vivo e reconhece o empenho no resgate; breve contexto de seu vínculo com Rukia. Vitória encerra a luta, sem desmaio obrigatório, cura gratuita automática ou dano/perda de equipamento impostos ao jogador por analogia a Ichigo |
| Conclusão | Vitória e desfecho local concluem por M13-03B; narrativa situa a reação do Seireitei e disponibiliza o próximo bloco selecionado. Disponibilidade não inicia novo boss nem altera era global de todos |
| Falha/retomada | M09/M12/M15; nova tentativa desta luta não repete a rota de Hanatarō já concluída nem os bosses anteriores. Continuidade de recursos/posição, participação e recompensas/replay ainda por ficha |
| Pendências | Acesso/motivação por caminho, local adequado, perfil desta versão de Renji, integração de resultado/estado da campanha e recompensas. Nenhuma implementação/teste nesta proposta |

**Ligação narrativa aprovada em 14A:** reações à derrota de Renji e descoberta de Aizen aparentemente morto no texto do contexto da campanha. Não criar missão obrigatória para investigar o corpo ou reencenar as discussões entre capitães; apresentar apenas o que é conhecido nesse momento, preservando a revelação posterior da conspiração. Flashbacks da formação/passado de Renji/Rukia podem integrar contexto, sem reabrir formação racial. Conteúdo de Karakura intercalado pelo anime não é automaticamente missão principal ou declarado filler pela simples posição no episódio.

**Escolha aprovada em 14A:** segundo Renji como confronto próprio e interlúdio escrito para reações/aparente morte de Aizen, seguindo ao próximo impedimento jogável. Alternativa preterida de cobertura do interlúdio: missão própria de investigação com objetivo e descoberta realmente executáveis, ainda sem ficha; não substituir contexto por missão apenas para assistir.

### 10.20. MAIN-SS-07 — O capitão da Oitava Divisão

**Estrutura aprovada — M13-14B, 28/10/2026; resposta “Adotamos”.** CAMP-S07/37 situa Chad diante de Shunsui Kyōraku no setor da 8ª Divisão; /38 o descreve caído e sua Reiatsu desaparecendo da percepção de Ichigo. É ocorrência paralela ao avanço até Kenpachi; seleção aprovada adapta Shunsui como adversário vencível do jogador antes de Kenpachi. Não afirmar que Chad venceu na obra ou que o jogador herda sua história pessoal.

| Campo | Proposta |
|---|---|
| Categoria/ordem | Principal; após Renji/interlúdio de MAIN-SS-06 e antes do confronto de Kenpachi; sequência é adaptação das ocorrências paralelas |
| Contexto/motivo | Breve informação da incursão no setor e do impedimento por Shunsui, na ocorrência originalmente ligada a Chad. Coerência de caminho/lealdade exige M03/M04/M08/M15; não inventar parentesco ou vingança pessoal do jogador |
| Início | Missão disponível no painel; jogador inicia explicitamente no local válido, criando/ativando apenas o ator da tentativa. Não hostilizar todo prestador ou usuário aliado de outra era por aproximação |
| Objetivo | “Vença o confronto contra Shunsui.” Mostrar que é desafio vencível nesta adaptação, não aparente boss cuja única conclusão permitida é ser derrotado |
| Perfil | Shunsui na versão do confronto desse período. Perfil/repertório/estado da arma exigem referência específica e M09; não importar Bankai, capacidades posteriores ou escalonamento automático. As sinopses não fornecem o conjunto completo de golpes |
| Vitória/desfecho | Jogador/party vence a representação de combate; Shunsui permanece vivo e deixa de impedir o avanço neste acontecimento. Difere da trajetória de Chad: convenção de vitória STR-03 aplicada à seleção aprovada, não reprodução literal do resultado original |
| Conclusão/continuidade | M13-03B: vitória registrada, desfecho breve e próximo bloco de Kenpachi disponível, com início próprio. Não conceder poderes de Chad/Shunsui ou aliados canônicos combatentes |
| Falha/pendências | M09/M12/M15: nova tentativa não repete Renji concluído. Perfil, cenário, posição/retomada, participação, acesso e recompensas/replay continuam em aberto |

**Escolha aprovada em 14B:** incluir Shunsui como confronto adaptado da ocorrência de Chad, para que o jogador execute também esse encontro conhecido do arco. Alternativa preterida: deixar Chad/Shunsui no contexto escrito e seguir de Renji/interlúdio diretamente para Kenpachi. Não impor reprodução de todas as frentes paralelas por uma regra universal.

**Continuidade:** Kenpachi é o próximo confronto na aproximação da prisão, com ficha preparada em 10.21. Vitória segue STR-03; adaptação e dependências de execução permanecem explícitas, sem impor consequências pessoais de Ichigo ao personagem original.

### 10.21. MAIN-SS-08 — O capitão da Décima Primeira Divisão

**Ficha de continuidade preparada em 28/10/2026, com realização em especificação.** Kenpachi já é o próximo confronto na sequência aceita em 14B; autoria/vitória herdadas de STR-03. CAMP-S07/36–38, CAMP-S08/39–40 e CAMP-S06 sustentam encontro, escalada e sobrevivência de Kenpachi após a luta. Não significa aprovação de todas as consequências do confronto original, parâmetros/fases ou funcionamento testado.

| Campo | Estrutura herdada e pendências |
|---|---|
| Categoria/ordem | Principal; após Shunsui, na aproximação da prisão de Rukia. Ordem frente a Shunsui é adaptação sequencial já selecionada |
| Contexto | Kenpachi manifesta interesse no adversário e se apresenta como impedimento de combate. Breve contexto reconhecível, sem exigir assistir ao deslocamento dos personagens da obra |
| Início | Painel/HUD no local válido da ficha, por ação explícita; criar/ativar a representação da tentativa. Não iniciar automaticamente após Shunsui ou por simples proximidade |
| Objetivo | “Derrote Kenpachi neste confronto.” Usar repertório atual do jogador/party, sem NPC aliado/finalizador |
| Versão/perfil | Kenpachi do primeiro arco Soul Society. Repertório, pressão espiritual, estado da arma e progressão interna do encontro exigem fonte/ficha M09; não importar Shikai/Bankai de eras posteriores ou estatísticas que acompanham automaticamente o jogador |
| Vitória | Superar a representação completa do encontro. Kenpachi permanece vivo no desfecho; não resetar a luta para exigir intervenção de um NPC ou retirada que apague a vitória |
| Limites da adaptação | Momentos de Ichigo com Zangetsu, ferimento grave/desmaio e quebra de arma podem integrar contexto da história dele. Não obrigam o jogador a morrer, perder equipamento, entrar no mundo interior de Ichigo, sofrer fase invencível ou receber técnica/forma gratuita |
| Conclusão | Vitória validada e desfecho breve registram sucesso por M13-03B; próxima atividade de aproximação/contato com Rukia disponível em início próprio. Não impor cura automática, teleporte ou encontro seguinte sem ação deliberada |
| Falha/pendências | M09/M12/M15: nova tentativa não repete Shunsui concluído. Local, perfil, fases/transições se aplicáveis, acesso, elegibilidade, recursos, recompensas/replay continuam em aberto |

**Limite de maturidade:** esta ficha traduz o confronto já encadeado pelos contratos de campanha. Não foi fechada uma mecânica nova de duas fases, invulnerabilidade, golpe obrigatório, penalidade ou compra de poder; tais decisões não decorrem de a obra mostrar Ichigo sofrendo durante a luta.

### 10.22. MAIN-SS-09 — Rukia na prisão

**Estrutura aprovada — M13-15A, 28/10/2026; resposta “Adotamos”.** CAMP-S08/40–42, CAMP-S06 e CAMP-S09 descrevem chegada à prisão, presença de Byakuya e intervenção de Yoruichi antes do confronto principal posterior. Missão aprovada de alcançar/interagir com Rukia, com esse impedimento no texto; não é o resgate final nem uma luta vencível anunciada e cancelada.

| Campo | Proposta |
|---|---|
| Categoria/ordem | Principal; após Kenpachi, antes dos preparativos finais do resgate |
| Fonte/contexto | A orientação anterior situa o acesso à prisão. Rukia participa da chegada; história de Ganju/Kaien pode ser breve contexto, sem parentesco do jogador ou missão racial/filler |
| Objetivo mostrado | “Alcance Rukia e confirme sua situação na prisão.” Objetivo de contato/reconhecimento, não promessa de libertá-la nesta primeira chegada |
| Início/ação | Painel/HUD no ponto válido de aproximação. Jogador inicia, alcança o ponto de contato e interage com Rukia; presença/diálogo no local são a interação objetiva, sem cutscene obrigatória de reencontro de Ichigo |
| Desfecho aprovado | Breve texto apresenta o impedimento de Byakuya e orientação/intervenção de Yoruichi para adiar o confronto. Luta principal de Byakuya reservada ao resgate no local da execução, com ficha posterior |
| Autoria/controle | Não spawnar Yoruichi como aliada para vencer um boss pelo jogador, aplicar seu golpe de desmaio ao personagem, forçar derrota/morte/quebra de arma ou teleportar automaticamente por causa da cena original |
| Conclusão | Contato validado e desfecho breve concluem por M13-03B. Disponibilizar próxima atividade selecionada; deslocamento a eventual ponto de encontro de Yoruichi terá ação/rota próprias na integração, sem exigir retorno administrativo para concluir |
| Limite do marco | Rukia continua presa; a execução e o resgate permanecem para os próximos acontecimentos. Esta chegada não declara acesso livre universal, abre cela global ou remove NPC de outros jogadores |
| Falha/pendências | M10/M12/M15 para acesso físico, ponto de contato, elegibilidade e retomada; Ganju/Hanatarō não são NPCs de escolta vulneráveis nesta proposta. Missões concluídas não são repetidas na falha; mapa/roteiro/participação e recompensa/replay ainda por ficha |

**Escolha aprovada em 15A:** primeira presença de Byakuya/Yoruichi no contexto escrito da missão de contato com Rukia, guardando o confronto completo de Byakuya para o resgate no local da execução. Alternativa preterida: atividade própria e declarada de retirada diante de Byakuya nesta primeira chegada, com objetivo/falha/retomada a definir; não falso boss cuja resolução inevitável seja perder.

### 10.23. MAIN-SS-10 — O capitão da Décima Segunda Divisão

**Estrutura aprovada — M13-15B, 28/10/2026; resposta “Incluimos”.** CAMP-S08/43–44 contextualiza Uryū/Orihime perante Mayuri e o esforço de Uryū; CAMP-S09 confirma o Bankai de Mayuri e sua retirada diante da vitória de Uryū. Seleção aprovada dá autoria ao jogador e vitória do encontro seguida de recuo em texto, preservando o capitão para o restante da obra.

| Campo | Proposta |
|---|---|
| Categoria/ordem | Principal; após o primeiro contato na prisão e antes da preparação final para a execução. Sequência adapta a frente paralela de Uryū ao personagem original |
| Contexto | Incursão e confronto de Mayuri com Uryū/Orihime em breve apresentação. Crimes contra Sōken pertencem à história de Uryū; não atribuir ancestralidade, raça Quincy ou vingança pessoal ao jogador |
| Início | Painel/HUD no setor válido da ocorrência, em ação explícita; cria/ativa a representação do confronto sem duplicar a tentativa. NPC persistente/serviços não se tornam hostis globalmente |
| Objetivo | “Derrote Mayuri neste confronto e force sua retirada.” Comunicar desde o início que vitória não significa morte definitiva do capitão |
| Adversário | Mayuri dessa ocorrência da Soul Society. CAMP-S09 confirma seu uso de Bankai no confronto; perfil, fases, efeitos e contrajogo exigem M09/M05/M06. Presença/repertório de Nemu e outros atores exigem ficha própria, sem aprovação implícita de combate simultâneo adicional, aliado ou conjunto completo de técnicas |
| Vitória aprovada | Jogador/party supera completamente a representação do encontro, usando capacidades disponíveis. Não exigir Letzt Stil, perda de poderes Quincy, técnica de Uryū ou arma emprestada; requisitos/custos legítimos das próprias capacidades seguem M04/M06/M07 |
| Retirada/desfecho | Após vitória validada, texto breve apresenta retirada de Mayuri. Não substituir o fim da luta por fuga prematura que invalide sucesso, morte permanente ou perseguição obrigatória extra. Modo visual/técnico de retirada ainda por ficha; não inferir forma específica de fuga que a sinopse não detalhe |
| Conclusão | M13-03B: vitória, desfecho e missão concluída; próxima preparação disponível sem início automático. Ocorrências posteriores de Uryū não prendem/derrotam automaticamente o jogador que venceu |
| Falha/pendências | M09/M12/M15: nova tentativa não repete Kenpachi/contato já concluídos. Cenário, estado correto, perfil/medidas, efeitos persistentes, acesso por caminho, elegibilidade/recompensas/replay continuam pendentes |

**Escolha aprovada em 15B:** incluir Mayuri como vitória completa do jogador, com retirada narrativa posterior. Alternativa preterida: manter a ocorrência Uryū/Mayuri apenas no texto. Não conceder o resultado narrativo como compra de skill, Mastery, item ou antídoto automático; a ficha de efeitos especiais e recuperação permanece nos módulos responsáveis.

### 10.24. Preparação final com Yoruichi — DIREÇÃO REVISADA

**M13-16A decidido com revisão, 28/10/2026.** Resposta “Prefiro uma preparação jogável própria”: incluir atividade real do jogador, em vez de apenas plano/contexto sem prova. Conteúdo concreto posteriormente aprovado em 17A, MAIN-SS-PREP-01/10.26; não reapresentar inclusão ou seleção do circuito como pendentes. Parâmetros e mapa continuam abertos.

**Referência e limite:** CAMP-S08/42/45/47/51 e CAMP-S09 descrevem atuação de Yoruichi, treino de Bankai de Ichigo e urgência da execução. O episódio 42 dá contexto de sua mobilidade; não descreve o circuito proposto para o jogador. Treino e passado pessoais de Ichigo continuam no contexto e não são a origem racial do personagem original.

**Proposta inicial preservada como histórico:** Yoruichi apresentaria o plano e disponibilizaria a próxima operação, sem outra prova obrigatória. Essa seleção foi preterida em 16A. A nova atividade deve ter objetivo executável próprio e breve orientação, sem missão apenas para assistir ao treino de Ichigo ou concluir diálogo.

**Limites herdados:** não entregar Bankai, skill, forma, Mastery ou arma de Ichigo, nem exigir ser Shinigami por associação. Requisitos legítimos de acesso/capacidade seguem sistemas próprios. Prazo de três dias da obra não vira automaticamente prazo real/de servidor, execução de Rukia por ficar offline ou avanço de era global. Um cronômetro do exercício local, proposto em 17A, é distinto do prazo da história.

**Cobertura ainda a revisar:** duelo Renji/Byakuya, passado de Rukia/Kaien e outros conflitos paralelos podem integrar contexto, mas não considerar toda a cobertura final fechada por escolher o treino próprio. Formação racial/fillers seguem adiados; o confronto principal de Byakuya já foi reservado ao resgate em 15A.

### 10.25. MAIN-SS-11 — Capitães em oposição

**Estrutura aprovada — M13-16B, 28/10/2026; resposta “Duas missões”.** CAMP-S09 descreve Kenpachi frente a Tōsen/Komamura; CAMP-S08/51 e CAMP-S10/53 situam o impedimento e a luta perto da execução, inclusive Bankai de Tōsen. Seleção aprovada adapta essa frente em duas missões vencidas pelo jogador antes da missão do resgate, sem Kenpachi como aliado de combate. Ordem sequencial e vitória completa contra cada representação são adaptações aprovadas em 16B; sinopses não comprovam todos os resultados/movimentos das lutas integrais.

| Campo | MAIN-SS-11A — Tōsen | MAIN-SS-11B — Komamura |
|---|---|---|
| Categoria/ordem | Principal; após Mayuri e a preparação própria escolhida em 16A, antes do resgate no local da execução | Principal; após 11A, no mesmo bloco narrativo de oposição dos capitães |
| Contexto | Reação à incursão e tentativa de impedir o avanço dos envolvidos na operação. Breve explicação adapta ao jogador a ocorrência de Kenpachi; compatibilidade de caminho/lealdade por M03/M04/M08/M15 | Continuação da frente de oposição, sem exigir que NPC Kenpachi tenha vencido o primeiro alvo pelo jogador |
| Início | Ação explícita no painel/HUD no local válido; criar/ativar apenas o ator de Tōsen da tentativa | Outro início explícito no painel/HUD no local válido; vencer Tōsen não inicia Komamura automaticamente |
| Objetivo | “Vença o confronto contra Tōsen.” | “Vença o confronto contra Komamura.” |
| Versão/perfil | Tōsen da Soul Society; Bankai confirmado em CAMP-S10/53, mas campo/efeitos/contrajogo, transições, medidas e repertório exigem M09/M05/M06. Não importar evolução Hollow ou estado de outro arco | Komamura desse período; arma/repertório/estado, medidas e transições exigem fonte/ficha M09. Não importar transformações de arcos posteriores nem atribuir uma versão exata não confirmada apenas por associação a Tōsen |
| Vitória/desfecho | Jogador/party vence a representação completa; Tōsen permanece vivo e missão conclui por M13-03B, disponibilizando 11B | Jogador/party vence a representação completa; Komamura permanece vivo. Conclusão por M13-03B disponibiliza a frente de Soi Fon escolhida em 18B, que antecede o resgate, em início próprio |
| Autoria | Sem Kenpachi/companheiros canônicos combatendo ou finalizando pelo jogador | Sem Kenpachi/companheiros canônicos combatendo ou finalizando pelo jogador |
| Falha/retomada | Nova tentativa autorizada conforme M09/M12/M15, sem repetir Mayuri já concluído | Retenção aprovada em 16B: manter Tōsen concluído se falhar em Komamura; repetir apenas a segunda missão |

**Integrações e limites:** razão/acesso por caminho, cenário e relação espacial com o resgate, versões corretas, efeitos sensoriais se aplicáveis, elegibilidade/reentrada, recursos, recompensas e replay continuam pendentes. Vitória de uma representação não mata definitivamente os capitães, muda filiação de todos ou decide a revelação de Tōsen antes do momento pertinente da conspiração. Não conceder automaticamente imunidade/percepção para resolver o Bankai de Tōsen.

**Escolha aprovada em 16B:** dois confrontos sequenciais, Tōsen e depois Komamura, com retenção da primeira vitória. Alternativas preteridas: ambos ao mesmo tempo em um único encontro do jogador/party, ou manter a frente apenas no contexto e seguir ao resgate, sem essas duas missões; a presença da fonte não obriga incluir toda batalha paralela.

**Continuidade consolidada posteriormente:** libertação em 17B, Byakuya em 18A, cobertura de frentes em 18B/19A e Aizen em contexto em 19B. Sequência e encerramento em 10.32/10.33; realização de perfis/mapas/retorno e demais integrações continua pendente. Fechamento aqui é do roteiro, sem declarar gameplay pronto.

### 10.26. MAIN-SS-PREP-01 — Ensaio de resgate

**Conteúdo aprovado — M13-17A, 28/10/2026; resposta “Adotamos”.** A inclusão de preparo próprio já foi escolhida em 16A. Exercício aprovado de aproximação e execução de objetivo na área de treino ligada ao ponto de encontro de Yoruichi na Soul Society, após Mayuri e antes de Tōsen/Komamura. É adaptação original de gameplay ao papel orientador de Yoruichi, não prova literal do mangá/anime ou reprodução do treino de Bankai de Ichigo.

| Campo | Proposta |
|---|---|
| Fonte/contexto | Yoruichi explica que chegar ao ponto do resgate e agir a tempo exige preparo de deslocamento e precisão. Apresentação breve e objetivo visível antes do início |
| Lugar | Área de treino na Soul Society, em contexto apropriado ao encontro com Yoruichi. Local/mapa ainda por M08/M10; não confundir com o campo sob a loja de Urahara em Karakura |
| Início | Interação explícita de iniciar com Yoruichi junto da área válida. Abrir conversa não liga cronômetro; uma tentativa por execução, sem múltiplas cópias por cliques repetidos |
| Objetivo aprovado | Percorrer pontos marcados em ordem, atravessar obstáculos/desníveis e alcançar/acionar o alvo de treino no fim antes de esgotar o tempo. O alvo final representa a ação de alcançar um objetivo protegido, sem usar Rukia real como boneco ou exigir resgatar outro NPC |
| Ação do jogador | Escolher movimentos e trajeto entre os pontos válidos, executando deslocamento e interação final com seu repertório disponível. Não exigir acertar um golpe em Yoruichi ou apenas sobreviver a uma perseguição, objetivos já usados nos treinos anteriores |
| Compatibilidade | Perfil de percurso deve ser viável com movimentos disponíveis ao personagem apto, dimensões do corpo e contexto de acesso. Não exigir Shunpo/técnica exclusiva de uma raça nem emprestar desbloqueio. Distância, desníveis e obstáculos por ficha M10/M12; não alterar silenciosamente dificuldade pelos atributos do jogador |
| Sucesso | Sequência válida de pontos e interação com o alvo dentro do tempo da tentativa ativa. Pular pontos obrigatórios e só acionar o alvo não conclui o teste. Como técnicas de voo/teleporte contam dentro desse percurso exige ficha, sem proibição racial ou concessão automática |
| Parâmetros abertos | Tempo nominal, quantidade de pontos, extensão/desníveis, margem de interação e tolerância dependem do mapa/perfil do corpo. Não fixar números antes de conhecer o percurso nem reutilizar os 60 segundos/30% de Ururu por associação |
| Falha/retomada | Tempo esgotado sem objetivo completo ou abandono encerra o exercício sem aprovação. Nova tentativa autorizada reinicia os objetivos/relógio do exercício, sem refazer Mayuri ou outros bosses. Queda/saída, desconexão, chunk descarregado e retorno à área exigem M09/M10/M12/M15; não contam como tempo de sucesso ou teleporte gratuito |
| Papel de Yoruichi | Apresentar e avaliar, sem combater no lugar do jogador; não é NPC a escoltar. Segurança do percurso deve evitar tornar o treino uma punição de morte obrigatória; efeitos de dano externo e recursos continuam nos sistemas responsáveis |
| Conclusão | Preparo registrado por M13-03B e Tōsen disponível no contexto definido, em início próprio. Não lançar/teleportar o jogador automaticamente ao local da execução |
| Progressão/recompensa | Marco de preparação. Não concede Bankai, mobilidade racial, skill, forma ou Mastery automática; ganhos adicionais/replay por ficha. Uso/prática de capacidades existentes segue seus contratos |

**Escolha aprovada em 17A:** circuito curto de aproximação e interação final, com tempo limite próprio a calibrar. Treina uma ação diferente de Ururu/Urahara e antecipa o resgate. Alternativa preterida: duelo curto contra uma representação controlada de Yoruichi, com condição de vitória e perfil a especificar; 16A escolheu inclusão e 17A escolheu o circuito; não reenviar essas decisões.

### 10.27. MAIN-SS-12 — Interromper a execução

**Estrutura aprovada — M13-17B, 28/10/2026; resposta “Seguimos asism”.** CAMP-S10/54 situa a liberação do Sōkyoku e a ameaça a Rukia; CAMP-S09 descreve o resgate e Rukia entregue aos cuidados de Renji antes do confronto principal de Byakuya. Sinopses não foram usadas como leitura integral do mecanismo da execução. Ação aprovada: jogador alcança a posição de resgate e interage para libertar Rukia; liberação/interrupção do Sōkyoku no contexto escrito, sem transformar a arma da execução em boss próprio nesta seleção.

| Campo | Proposta |
|---|---|
| Categoria/ordem | Principal; após a preparação, Tōsen/Komamura e Soi Fon, antes do confronto principal com Byakuya |
| Início | Operação disponível no painel/HUD, início explícito na área válida de aproximação. Aproximar-se ou ficar offline não dispara prazo de execução real; disponibilidade não avança a era global |
| Objetivo mostrado | “Alcance Rukia no local da execução e liberte-a.” Deslocamento real até o ponto válido e interação com a personagem; não apenas assistir à chegada de Ichigo |
| Ação/validação | Jogador realiza aproximação e aciona a interação de resgate no contexto da tentativa. Tipo de contenção, alcance/tempo da ação e composição física do patíbulo exigem M10/M11/M12; não inventar chave/craft ou técnica racial obrigatórios |
| Sōkyoku | Contexto escrito de sua liberação/interrupção nesta adaptação; sem boss separado, duelo de NPCs tomando vitória ou técnica universal de quebrar qualquer arma de execução atribuída ao jogador. Apresentação da interrupção ainda por roteiro; não alegar que qualquer clique em um objeto reproduz literalmente a técnica original |
| Desfecho | Resgate efetuado pelo jogador, Rukia afastada do risco imediato e continuidade aos cuidados de Renji em texto. Renji não combate como aliado para resolver a missão, e Rukia não vira pet/NPC de escolta obrigatório |
| Conclusão | Resgate validado conclui por M13-03B e disponibiliza o confronto principal de Byakuya; início continua próprio. A revelação/conspiração ainda exige continuidade; não declarar arco encerrado por retirar Rukia do patíbulo |
| Falha/limites | M12/M15 para participação/retomada; morte/abandono não conclui, e tentativa nova não repete capitães concluídos. Sem cronômetro letal universal ou morte permanente de Rukia por falha/ausência do usuário; condições executáveis ainda por ficha |
| Mundo/recompensas | Representação do acontecimento por contexto/progresso; sem remover Rukia de todos os jogadores ou restaurar/destruir construções. Mapa/acesso, elegibilidade, recompensas/replay ainda pendentes; não conceder arma, skill ou forma automaticamente |

**Escolha aprovada em 17B:** ação de alcançar e libertar Rukia, Sōkyoku no contexto e Byakuya como confronto próprio seguinte. Alternativa preterida: acrescentar antes da libertação um desafio jogável de interrupção do Sōkyoku, a especificar com objetivo/falha/versão próprios; não é automaticamente um Hollow ou adversário racial comum. O escopo da libertação não deve ser confundido com aprovação desse desafio adicional.

### 10.28. MAIN-SS-13 — O orgulho de Byakuya

**Estrutura aprovada — M13-18A, 28/10/2026; resposta “Concordo”.** Inclusão do confronto principal após o resgate já reservada em 15A/17B; não é nova votação de sua presença ou da autoria STR-03. CAMP-S09 e CAMP-S10/55/57–59 descrevem Byakuya com Senbonzakura, Bankai Senbonzakura Kageyoshi e Senkei, além do reconhecimento da vitória de Ichigo. Escolha aprovada em 18A: organização em um confronto contínuo, sem transformar cada momento em outra missão/tentativa.

| Campo | Estrutura aprovada e pendências |
|---|---|
| Categoria/ordem | Principal; imediatamente após a missão de libertar Rukia, antes da revelação da conspiração. Frentes paralelas eventualmente escolhidas não devem ser inseridas entre a libertação e este confronto sem revisão explícita |
| Contexto | Byakuya se apresenta como impedimento à operação; breve contexto do dever e do conflito em torno de Rukia. Não atribuir relações familiares, promessas ou passado de Ichigo ao jogador |
| Início | Disponível após resgate validado; jogador inicia pelo painel/HUD no local válido próximo da execução. Não iniciar ataque ao apenas concluir a interação com Rukia ou aproximar-se do NPC |
| Objetivo | “Derrote Byakuya e supere sua oposição ao resgate.” Vitória do jogador/party com repertório próprio, sem aliado canônico/finalizador |
| Progressão aprovada | Uma única missão e tentativa com progressão reconhecível de Shikai para Bankai e depois o uso de Senkei dentro do Bankai. Senkei é técnica/apresentação desse estado, não terceira transformação ou missão racial. Transições, gatilhos e execução dos ataques exigem ficha M09/M06 |
| Limites de parâmetros | Número de barras de Vida, valores/limiares, dano/alcance e eventual custo/recuperação entre momentos não foram escolhidos. Não gerar reset/recompensa extra ou checkpoint entre fases apenas por mudar a apresentação |
| Versão | Byakuya do confronto principal da Soul Society; não importar evoluções de guerras posteriores. Fonte confirma os momentos citados, não fornece repertório jogável completo ou toda a parametrização |
| Vitória/desfecho | Superar a representação completa da tentativa. Byakuya reconhece o resultado e deixa de impedir o resgate; permanece vivo para a continuidade. Resolução pessoal da obra pode integrar texto, sem exigir que o jogador seja Ichigo |
| Trajetória do jogador | Não impor Bankai/Tensa Zangetsu, possessão pelo Hollow interior de Ichigo, perda de controle, ferimento obrigatório ou técnica exclusiva do protagonista. Ações/formas do jogador continuam com seus próprios requisitos/custos |
| Conclusão/falha | Vitória validada conclui por M13-03B e disponibiliza a revelação/continuidade em início próprio. Se perder esta tentativa contínua, retomar conforme M09/M12/M15, sem refazer libertação ou outros bosses concluídos; estado inicial do encontro sem dano acumulado de tentativa anterior |
| Pendências | Fases/versão/capacidades, cenário, condições de início/retomada, efeito de terceiros, participação e recompensas/replay. Nenhuma implementação/teste ou regra numérica aprovada nesta ficha |

**Escolha aprovada em 18A:** luta contínua com progressão Shikai → Bankai → uso de Senkei; preserva o confronto principal como uma vitória completa. Alternativa preterida: momentos com tentativas/missões separadas e retenção entre elas, que exigiriam outra organização explícita do objetivo e do ritmo. Não promover o número de fases/valores a cânone do mod só pela ordem da sinopse.

### 10.29. MAIN-SS-FRONT-01 — Soi Fon

**Estrutura/cobertura aprovada — M13-18B, 28/10/2026; resposta “Adotamos”.** CAMP-S09 e CAMP-S10/55–57 situam Yoruichi perante Soi Fon e Yamamoto perante Shunsui/Ukitake em frentes paralelas ao confronto de Byakuya. Seleção aprovada: Soi Fon como missão do jogador antes da libertação de Rukia e Yamamoto/Shunsui/Ukitake no contexto escrito. É adaptação declarada de autoria e ordem: não alegar que Soi Fon luta literalmente antes do resgate na sequência original.

| Campo | Estrutura aprovada de Soi Fon e pendências |
|---|---|
| Categoria/ordem | Principal; frente adaptada após Tōsen/Komamura e antes de MAIN-SS-12, conforme 18B aprovado. Preserva a sequência já aprovada de libertação → confronto de Byakuya |
| Contexto | Oposição na frente originalmente de Yoruichi. Breve apresentação mantém a relação pessoal das duas no contexto; motivação/acesso por caminho exige M03/M04/M08/M15, sem fazer do jogador antigo superior ou alvo pessoal da história de Soi Fon |
| Início | Painel/HUD no local válido da frente adaptada, em ação explícita que cria/ativa a representação da tentativa. Não agredir usuários/NPCs persistentes de outra era/afiliação por proximidade |
| Objetivo | “Vença o confronto contra Soi Fon.” Jogador/party enfrenta e vence com o próprio repertório; Yoruichi não combate/finaliza por ele |
| Perfil | Soi Fon desse período da Soul Society. Versão da arma, repertório/efeitos, alcance, dano e contrajogo exigem fonte/ficha M09/M06, sem importar Bankai, técnicas ou versões posteriores por associação ao catálogo geral |
| Vitória/desfecho | Vitória completa da representação, Soi Fon viva no desfecho e relação com Yoruichi no contexto escrito. Não conceder Shunkō ou outro poder de Yoruichi como recompensa para reproduzir a cena original |
| Conclusão/retomada | Vitória conclui por M13-03B e disponibiliza o resgate em início próprio. Falha segue M09/M12/M15 e não repete capitães já concluídos; participação/recompensas/replay ainda por ficha |

**Yamamoto na escolha aprovada 18B:** manter no contexto o conflito com Shunsui/Ukitake, sem missão obrigatória adicional contra ele. Preserva o papel do capitão-comandante nessa etapa e evita outro confronto de grande peso entre o avanço e o clímax de Byakuya. Não apagar a ocorrência da obra, tornar os dois aliados canônicos de combate do jogador ou importar Bankai posterior de Yamamoto. Alternativa preterida: incluir também Yamamoto como confronto adaptado e vencível do jogador. Sem ficha ou inclusão neste arco.

**Cobertura ainda a fechar:** disputas Hitsugaya/Gin, Iba/Ikkaku, Hisagi/Yumichika e oficiais que intervêm no resgate recebem proposta agrupada em 19A/10.30; não foram escolhidas automaticamente. Conspiração/Aizen proposta em 19B/10.31 e encerramento/retorno ainda exigem fechamento; não declarar publicação pronta nesta etapa.

### 10.30. Revisão das outras ocorrências do final do arco

**Cobertura aprovada — M13-19A, 28/10/2026; resposta “Concordo”.** Fechar seleção do roteiro por acontecimentos, sem prometer uma missão para cada episódio ou ampliar para sidequests. CAMP-S08/47–49, CAMP-S09 e CAMP-S10 fornecem contexto dos conflitos internos; não equivalem a assistir/ler todas as lutas ou confirmar todo o elenco/repertório do resgate.

| Ocorrência | Cobertura recomendada em 19A | Base/limite |
|---|---|---|
| Hitsugaya, Hinamori e Gin | Contexto escrito das suspeitas/manipulação e conflito, no ponto anterior à revelação; sem outra luta obrigatória contra Gin ou investigação que antecipe a conspiração | CAMP-S08/47–48 e CAMP-S09 descrevem suspeitas e confronto; não importar resolução posterior para o momento anterior |
| Iba/Ikkaku e Hisagi/Yumichika | Contexto da divisão interna dos Shinigami, sem missões adicionais desses embates | CAMP-S09 lista essas frentes; não afirmar resultados/golpes completos não descritos na fonte |
| Oficiais que intervêm no resgate | Participação de contexto, sem acrescentar uma sequência de bosses entre libertação e Byakuya | Elenco/ações exatas exigem confirmação primária antes de falas/cenas finais; não preencher nomes ou repertórios por lembrança nesta seleção |
| Renji contra Byakuya antes da execução | Contexto da decisão de Renji e vínculo com Rukia; não repetir Byakuya como outra luta obrigatória do jogador | CAMP-S10/52 e CAMP-S09; não conceder Bankai de Renji ao personagem original |
| Passado de Rukia/Kaien e reconciliação | Contexto pessoal necessário e resolução no encerramento, sem flashback jogável obrigatório | CAMP-S08/49 e CAMP-S09; não classificar como filler nem transferir vínculos familiares ao jogador |
| Uryū após Mayuri | Contexto da trajetória de Uryū, sem prender, retirar poderes ou derrotar automaticamente o jogador por causa da ocorrência dele | CAMP-S09 menciona Tōsen após a vitória de Uryū; vitória do jogador contra Mayuri não vira derrota obrigatória por associação |

**Já decidido, sem nova votação:** Yamamoto/Shunsui/Ukitake em texto (18B), Renji/Byakuya principal do jogador no momento reservado (15A/18A), circuito de Yoruichi (17A) e Sōkyoku em contexto (17B). Outros arcos, raças e fillers continuam adiados. A lista fecha cobertura do roteiro proposto, não preenche mapas, perfis, elenco exato ou implementação.

**Escolha aprovada em 19A:** demais disputas/oficiais mencionados no contexto, sem novas missões obrigatórias desses combates; seguir de Byakuya à revelação de Aizen, cujo formato permanece aberto em 19B. Alternativa preterida nesta seleção: selecionar alguma dessas ocorrências para missão própria, definindo qual antes de ampliar o catálogo. Não presumir que aprovação de texto proíbe conteúdo lateral futuro quando o usuário retomar esse escopo.

### 10.31. Desfecho — A verdade por trás da execução

**M13-19B decidido com revisão em 28/10/2026. Resposta literal:** “Perfeito, aprovo”. Aceita a recomendação reformulada após “porque adaptar para uma vitória?”: preservar a superioridade/apresentação de Aizen nesta etapa, com revelação, obtenção do Hōgyoku e partida com Gin/Tōsen no contexto escrito. **Byakuya é o último boss do arco da Soul Society; não há vitória jogável contra Aizen aqui.**

CAMP-S09 e CAMP-S10/60–62 sustentam esses acontecimentos. Isso preserva STR-03: bosses selecionados são vencidos pelo jogador, mas toda aparição de um antagonista não precisa virar boss. O exemplo de derrota/selamento de Aizen em M03 se refere a outro momento da campanha. Confrontos vencíveis posteriores dele mantêm a autoria do jogador/party, sem aliados canônicos finalizando.

| Elemento | Recorte vigente |
|---|---|
| Gatilho de continuidade | Objetivos do resgate e vitória contra Byakuya validamente concluídos; o desfecho escrito acompanha a conclusão, sem criar boss automaticamente |
| Revelação | Central 46, ordens falsas e responsabilidade de Aizen apresentados no contexto apropriado. Não obrigar o jogador a reencenar a investigação alheia ou antecipar a revelação em missões anteriores |
| Hōgyoku | Aizen o obtém de Rukia no contexto da obra. Não é recompensa/item recebido pelo jogador; não é preciso antecipar a extração apenas para contornar uma vitória inventada |
| Confronto nesta aparição | Não é boss selecionado neste arco. Não mostrar “derrote Aizen”, criar ataque obrigatório que mate o jogador ou cobrar uma derrota para registrar a revelação |
| Partida | Aizen, Gin e Tōsen deixam a Soul Society; ameaça preservada para os próximos acontecimentos. Não equivale a derrota/selamento definitivo ou cancelamento da vitória de Byakuya |
| Registro | Preservar a conclusão pessoal/participação validada nos objetivos anteriores; não mudar a era global ou remover NPCs de todos os jogadores por uma conclusão individual |
| Identificação retirada | MAIN-SS-14 era a proposta de boss não adotada. ID reservado no histórico, fora das 28 missões selecionadas; não criar JSON ou reaproveitá-lo silenciosamente como outra missão |

#### 10.31.1. Histórico da revisão de Aizen

A recomendação inicial aplicava a convenção de vitória dos bosses a essa aparição e antecipava a obtenção do Hōgyoku ao confronto. Usuário pediu justificativa; assistente reconsiderou porque a seleção não era obrigatória e enfraquecia a apresentação do antagonista. A nova recomendação foi aprovada, sem revogar [M03/STR-03](03-storyline-macro.md#str-03--quanto-os-acontecimentos-canônicos-podem-mudar).

[Proposta anterior e explicação integral — NÃO ADOTADA](historico/2026-10-28-proposta-m13-19b-aizen-vencivel-nao-adotada.md). Preservar como evidência histórica, sem executar a ficha antiga nem reenviar sua pergunta.

### 10.32. Sequência consolidada dos dois primeiros arcos

**Fechamento da seleção/estrutura do roteiro em 28/10/2026.** Tabela organiza decisões 01A–19B; não adiciona missões, novos requisitos, recompensas ou implantação. Cada início/conclusão conserva 01A/02A/03B e os limites de acesso/participação das fichas. São **10 missões no arco Substituto e 18 na Soul Society**, incluindo preparações; fases de treino/luta não contam como missões adicionais.

| Ordem | ID | Atividade aprovada |
|---:|---|---|
| 1 | MAIN-SUB-01 | Rukia: vencer o Hollow de abertura |
| 2 | MAIN-SUB-02 | Vencer o irmão Hollow de Orihime |
| 3 | MAIN-SUB-03 | Chad/periquito: vencer Shrieker |
| 4 | MAIN-SUB-04 | Urahara/Kon: vencer o Hollow da ocorrência da escola |
| 5 | MAIN-SUB-05 | Vencer Grand Fisher; retirada no desfecho |
| 6 | MAIN-SUB-06A | Vencer os Hollows atraídos pela crise Quincy |
| 7 | MAIN-SUB-06B | Vencer o Menos; retirada no desfecho |
| 8 | MAIN-SUB-07 | Vencer Renji em Karakura; prisão de Rukia em texto |
| 9 | MAIN-SUB-PREP-01 | Ururu: 60 segundos/30% provisórios; depois um golpe válido em Urahara |
| 10 | MAIN-SUB-08 | Travessia breve até o Rukongai; Kōtotsu em contexto |
| 11 | MAIN-SS-01 | Vencer Jidanbō; Gin impede a entrada no desfecho |
| 12 | MAIN-SS-GANJU-01 | Vencer o desafio de Ganju; ele permanece vivo |
| 13 | MAIN-SS-02 | Kūkaku: estabilizar esfera e iniciar lançamento pelo canhão |
| 14 | MAIN-SS-03A | Vencer Ikkaku |
| 15 | MAIN-SS-03B | Vencer Yumichika; Ikkaku concluído preservado na falha |
| 16 | MAIN-SS-04 | Vencer Jirōbō; cerco de soldados em contexto |
| 17 | MAIN-SS-05 | Percorrer as galerias indicadas por Hanatarō, sem escolta |
| 18 | MAIN-SS-06 | Vencer Renji da Soul Society; interlúdio de Aizen em texto |
| 19 | MAIN-SS-07 | Vencer Shunsui na adaptação da ocorrência de Chad |
| 20 | MAIN-SS-08 | Vencer Kenpachi |
| 21 | MAIN-SS-09 | Alcançar/interagir com Rukia na prisão; Byakuya/Yoruichi em texto |
| 22 | MAIN-SS-10 | Vencer Mayuri; retirada no desfecho |
| 23 | MAIN-SS-PREP-01 | Circuito de aproximação/interação final orientado por Yoruichi |
| 24 | MAIN-SS-11A | Vencer Tōsen |
| 25 | MAIN-SS-11B | Vencer Komamura; Tōsen concluído preservado na falha |
| 26 | MAIN-SS-FRONT-01 | Vencer Soi Fon; ordem adaptada antes do resgate |
| 27 | MAIN-SS-12 | Alcançar/libertar Rukia por interação; Sōkyoku em contexto |
| 28 | MAIN-SS-13 | Vencer Byakuya em confronto contínuo Shikai/Bankai/Senkei |

Após a última vitória válida, contexto da revelação/Hōgyoku/partida de Aizen (19B) e resolução abaixo. Don Kanonji e demais ocorrências escolhidas em texto permanecem nos pontos narrativos de suas fichas, sem IDs extras ou quests para assistir. A ordem sequencial adapta frentes paralelas, explicitamente inclusive Soi Fon; não representa uma lista literal de episódios.

### 10.33. Encerramento do arco e continuidade do personagem

**Consolidação de STR-03, M13-03B/19B e M10/M10b, sem nova missão ou sistema.** CAMP-S09 descreve a execução cancelada, Rukia permanecendo na Soul Society e a despedida do elenco. Esses resultados integram o epílogo escrito após o resgate/última vitória válidos, preservando o personagem original.

- **Fechamento pessoal:** objetivos selecionados concluídos/participação validada e desfecho registrado encerram este arco pelo contrato comum de conclusão. Não exigir boss Aizen, diálogo administrativo adicional ou novo combate só para marcar conclusão. Falhas/reentrada/replay continuam nos responsáveis, sem conclusão por derrota.
- **Destino de Rukia:** resgatada da execução, permanece na Soul Society no contexto do fim do arco. Não vira item/pet/aliada combatente ou acompanha automaticamente cada personagem em outro mundo.
- **Continuidade do jogador:** a despedida/retorno dos personagens da obra não obriga teleporte, mudança de raça/base/afiliação ou primeira visita universal. Retorno voluntário usa fonte/rota válidas e autorização por contexto/corpo já exigidas em M10/M10b, fora de combate onde aplicável. Não conceder Senkaimon, passe institucional universal ou acesso sem requisitos por concluir o resgate.
- **Pendência de realização do retorno:** a ficha da expedição precisa demonstrar ida/volta viáveis e fonte disponível na outra ponta antes da implementação, conforme M10-04A/M10b/9.4–9.5. Progresso não fica refém de outro jogador; não inventar botão de resgate ou rota concluída nesta rodada. Encerramento documental não prova essa integração em jogo.
- **Próxima etapa de coautoria:** desenvolver a campanha principal dos conflitos Arrancar conforme M03, em rodada posterior. Não iniciar boss/missão/era global ou campanha filler automaticamente pela conclusão; quests raciais e M11 permanecem adiados.

**Maturidade do recorte:** roteiro/seleção/fluxos estruturais dos dois arcos fechados no alcance desta discussão. M13 permanece `EM ESPECIFICAÇÃO`: cenários/rotas, perfis completos, acesso por caminho, objetivos executáveis, recompensas/replay, interface, participação/era e persistência continuam necessários antes da implementação. Não há build, teste de jogo ou aceite da experiência executada nesta rodada.

## 11. Visão completa

Campanha principal reconhecível do início à Guerra Sangrenta, com variação de encontros e objetivos e continuidade no mundo aberto. Volume de quests não equivale ao número de episódios; não prometer adaptar literalmente toda cena.

## 12. Decisões registradas e histórico

### 12.1. M13-01A — DECIDIDO COM REVISÃO — Execução direta em vez de abertura encenada

**Direção atual em 28/10/2026:** resposta integral preservada em 8.1; começar por missões executadas pelo jogador, com apresentação breve do NPC e contexto escrito. A encenação obrigatória do encontro Ichigo/Rukia não foi adotada. Não reenviar a pergunta anterior como se aguardasse escolha.

**Proposta original preservada, substituída no escopo da abertura:** abrir com uma apresentação curta dos acontecimentos da primeira noite de Ichigo/Rukia e daí situar a primeira missão do personagem. Conta o início, sem refazer a origem racial ou fazer o jogador receber de Rukia os poderes de Ichigo. A participação jogável exata será definida na ficha MAIN-SUB-01.

**Alternativa:** começar quando Ichigo já atua como Substituto, usando relato breve do encontro anterior. Vai mais rápido aos primeiros casos, mas reduz a presença jogável/narrada da abertura da obra.

**Pergunta 1:** A campanha principal começa mostrando brevemente o encontro de Ichigo com Rukia, como recomendo, ou já começamos com Ichigo atuando como Shinigami Substituto e contamos o início por relato?

**Resposta:** direção de execução direta registrada em 8.1; proposta de abertura substituída.

### 12.2. M13-01B — REFORMULAÇÃO APLICADA — Seleção por atividade jogável

**Estado atual:** a resposta não aprovou sete missões nem escolheu simplesmente condensar a história; mudou o critério para atividades executáveis. 10.1 foi reformulado por NPC/objetivo/início. A pergunta de quantidade anterior não é o próximo passo e não deve ser repetida. A cobertura/número destes dois primeiros arcos foram depois consolidados a partir das fichas escolhidas em 10.32; demais arcos continuam por especificar.

**Proposta original preservada:** desenvolver os sete blocos de 10.1 em missões enxutas, incluindo os acontecimentos relevantes de Orihime, Chad e Kon, além dos confrontos de maior escala. Não significa uma quest para cada conversa/episódio ou alongar tarefas de deslocamento.

**Alternativa:** condensar os primeiros casos em relatos e concentrar as missões sobretudo em Grand Fisher, crise Quincy/Hollow e prisão de Rukia. Reduz volume, mas oferece menos convivência e contexto do elenco.

**Pergunta 2:** Recomendo transformar os sete blocos em missões enxutas para conhecer o elenco e acompanhar a história. Você prefere isso ou condensar os primeiros casos e concentrar o gameplay nos grandes confrontos e na prisão de Rukia?

**Resposta:** sem aprovação de quantidade/catálogo; formulação anterior superada pela direção de execução direta de 8.1.

### 12.3. M13-02A — DECIDIDO — Início por NPC ou painel e criação do confronto

Direção literal em 8.1: NPC se apresenta e encaminha/inicia a missão por interação explícita; evento autônomo pode usar botão na área de missões do HUD; confronto cria o inimigo previsto. Não exige encenação dos encontros da obra, não autoriza início remoto de toda missão e não escolhe todas as fontes/atores. Contrato aplicado em 7/8.1/10.1/10.3, com integrações M09/M12/M14/M15/M16. Nenhuma pergunta de confirmação sobre esse formato é necessária.

### 12.4. M13-03A — DECIDIDO — Primeira missão com Rukia e um confronto Hollow

**Recomendação:** adotar MAIN-SUB-01 de 10.3: Rukia em Karakura oferece um confronto curto contra um Hollow da abertura, vencido pelo jogador/party. Contexto breve, sem coleta preparatória, transferência de poderes ou sequência para assistir. Identidade visual/nome exatos ficam em verificação; esta escolha não transforma Rukia em fonte universal de todas as raças.

**Alternativa:** começar diretamente pelo caso de Orihime e seu irmão, deixando toda a ameaça Hollow inicial no contexto escrito. Acelera a chegada ao primeiro conflito pessoal do elenco, mas elimina um encontro simples de apresentação da campanha.

**Pergunta 1:** Recomendo abrir com Rukia e esse primeiro confronto curto em Karakura, antes do caso de Orihime. Adotamos essa sequência ou começamos diretamente pelo caso de Orihime?

**Resposta literal em 28/10/2026:** “Vamos seguir como você recomendou”. Recomendação aprovada e aplicada em 8.2/10.3. A proposta e a alternativa acima ficam preservadas como histórico.

### 12.5. M13-03B — DECIDIDO — Conclusão após o objetivo, com retorno ao NPC quando necessário

**Recomendação:** nesta missão, vitória válida conclui e registra o progresso, informa o resultado e disponibiliza o próximo caso, sem iniciá-lo sozinho. Para as próximas fichas da campanha, recomendar conclusão após o objetivo sempre que não houver entrega ou conversa necessária; se o retorno tiver função na história/atividade, declará-lo como etapa. Não é conclusão antes de validar participação nem aprovação de recompensa/replay.

**Alternativa:** exigir retorno ao NPC após cada missão de combate para registrar o resultado, mesmo sem outra entrega. Mantém um ponto de reencontro, mas acrescenta deslocamento e uma interação administrativa após a vitória.

**Pergunta 2:** Recomendo concluir automaticamente após cumprir o objetivo e voltar ao NPC apenas quando houver entrega ou conversa necessária. Seguimos assim ou você prefere retornar ao NPC após todo confronto?

**Resposta literal em 28/10/2026:** “Vamos seguir como você recomendou”. Recomendação aprovada e aplicada em 8.2/10.3. A proposta e a alternativa acima ficam preservadas como histórico.

### 12.6. M13-04A — DECIDIDO — Encaminhamento do caso de Orihime

**Recomendação:** Rukia comunica a ocorrência e oferece o início da missão; o jogador descobre o vínculo do Hollow com Orihime no contexto do caso. Evita fazer Orihime contratar conscientemente uma caçada ao irmão antes de compreender o ocorrido. A oferta é adaptação da campanha, não diálogo copiado da obra.

**Alternativa:** Orihime oferece um pedido de ajuda por uma ocorrência estranha; a apresentação precisa manter seu conhecimento limitado naquele momento, sem identificar antecipadamente o irmão ou assumir um papel de operadora de missões.

**Pergunta 1:** Recomendo que Rukia encaminhe o jogador ao caso de Orihime. Seguimos assim ou prefere que Orihime faça um pedido de ajuda diretamente?

**Resposta literal em 28/10/2026:** “Concordo”. Aplicada em 8.3/10.4. Proposta, alternativa e pergunta preservadas como histórico; não reenviar.

### 12.7. M13-04B — DECIDIDO — Confronto direto ou objetivo de defesa

**Recomendação:** jogador/party vence o irmão Hollow; Orihime participa do contexto e do desfecho, sem barra de defesa, escolta ou tempo de sobrevivência. Mantém foco no confronto e evita falha por IA/dano em NPC enquanto ainda apresentamos a campanha. Vitória seguida de texto breve resolve o caso, pelo contrato 03B.

**Alternativa:** acrescentar defesa de Orihime com condição própria de falha se for derrotada durante o encontro. Diversifica o objetivo, mas exige Vida/ameaça/recuperação da representação de missão e retomada, sem matar o NPC permanente ou contradizer os marcos da história. Valores e regras não estão aprovados.

**Pergunta 2:** Recomendo focar em vencer o Hollow, com Orihime no contexto da missão. Prefere isso ou também ter de protegê-la com uma barra de Vida cuja perda faz a tentativa falhar?

**Resposta literal em 28/10/2026:** “Prefiro vencer o Hollow”. Aplicada em 8.3/10.4. Proposta, alternativa e pergunta preservadas como histórico; não reenviar.

### 12.8. M13-05A — DECIDIDO — Chad e confronto com Shrieker

**Recomendação:** adotar MAIN-SUB-03 de 10.5, com Chad apresentando o caso e o jogador iniciando/enfrentando Shrieker diretamente. Periquito/menino no contexto, sem inventário ou escolta obrigatórios; desfecho breve e conclusão pelo contrato 03B.

**Alternativa:** inserir uma etapa curta de investigação antes do confronto, a especificar por objetivo e pistas reais. Aumenta a participação no mistério, mas acrescenta uma etapa ao ritmo direto pedido pelo usuário.

**Pergunta 1:** Para Chad, recomendo apresentação breve do caso do periquito e início direto da luta contra Shrieker. Adotamos assim ou você quer uma etapa curta de investigação antes?

**Resposta conjunta literal em 28/10/2026:** “Concordo com as recomendações”. Recomendação aprovada e aplicada em 8.4/10.5/10.6 conforme seu alcance; pergunta e alternativa preservadas como histórico, não reenviar.

### 12.9. M13-05B — DECIDIDO — Incidente de Kon como missão curta

**Recomendação:** incluir MAIN-SUB-04 de 10.6: Urahara encaminha o incidente; no local da escola, início pelo painel/HUD e combate ao Hollow da ocorrência; texto breve introduz a continuidade de Kon. Ele não vira um inimigo a matar. Não reencenar toda a troca de corpos.

**Alternativa:** tratar o incidente de Kon somente como contexto escrito e seguir diretamente de Shrieker para Grand Fisher. Não o reclassifica como filler.

**Pergunta 2:** Recomendo incluir o caso de Kon como missão curta: Urahara encaminha, o jogador inicia no HUD ao chegar à escola e vence o Hollow da ocorrência. Incluímos assim ou deixamos esse caso em texto e seguimos para Grand Fisher?

**Resposta conjunta literal em 28/10/2026:** “Concordo com as recomendações”. Recomendação aprovada e aplicada em 8.4/10.5/10.6 conforme seu alcance; pergunta e alternativa preservadas como histórico, não reenviar.

### 12.10. M13-06A — DECIDIDO — Vitória contra Grand Fisher com retirada narrativa

**Recomendação:** adotar MAIN-SUB-05 de 10.7: Rukia oferece o confronto na área do cemitério; jogador/party vence a representação inicial de Grand Fisher; vitória registrada e missão concluída; texto breve informa a fuga, preservando a continuidade. O objetivo já comunica “derrotar e forçar retirada”, sem atribuir a história familiar de Ichigo ao jogador.

**Alternativa:** encerrar o encontro após atingir um limiar explícito de pressão/dano, com fuga antes de superar completamente a representação. Exige calibrar esse objetivo e comunicar o fim antecipado. Não é uma derrota inevitável do jogador.

**Pergunta:** Recomendo vencer a luta contra Grand Fisher e concluir a missão; no desfecho escrito ele escapa, preservando sua presença posterior na história. Adotamos esse formato?

**Resposta literal em 28/10/2026:** “Positivo, adotamos”. Aplicada em 8.5/10.7. Pergunta e alternativa preservadas como histórico; não reenviar.

### 12.11. M13-07A — DECIDIDO — Cobertura do incidente de Don Kanonji

**Recomendação:** contexto escrito curto entre Grand Fisher e a crise de Uryū, sem acrescentar uma missão obrigatória neste momento. CAMP-S04 confirma a presença do caso no mangá; não chamar de filler. Alternativa: desenvolver uma missão principal complementar do confronto no hospital.

**Pergunta 1:** O caso de Don Kanonji também está no mangá e ocorre nesse trecho. Recomendo resumi-lo em texto e seguir para Uryū, mantendo o ritmo da campanha. Concorda ou prefere uma missão jogável no hospital?

**Resposta conjunta literal em 28/10/2026:** “gostei da forma como propôs, muito bom, podemos seguir”. Pacote recomendado aceito e aplicado em 8.6/10.8/10.9, conforme o alcance de cada escolha. Não reenviar; proposta e alternativa preservadas como histórico.

### 12.12. M13-07B — DECIDIDO — Crise de Uryū em duas missões

**Recomendação:** MAIN-SUB-06A contra os Hollows atraídos, depois MAIN-SUB-06B contra o Menos, em início próprio pelo painel no local válido. Jogador vence o confronto e força o recuo narrativo do Menos; falhar no boss não exige refazer a missão de Hollows já concluída. Sem aliados canônicos em combate. Não aprova números, recompensas extras ou checkpoint universal.

**Alternativa:** uma única missão contínua de Hollows e Menos; condições de retomada/checkpoint exigiriam ficha específica. Mesma autoria do jogador e continuidade narrativa.

**Pergunta 2:** Recomendo duas missões: vencer os Hollows atraídos e, depois, vencer o Menos Grande e forçar seu recuo. Se falhar no Menos, não refaz a primeira missão. Seguimos assim ou prefere tudo em uma missão contínua?

**Resposta conjunta literal em 28/10/2026:** “gostei da forma como propôs, muito bom, podemos seguir”. Pacote recomendado aceito e aplicado em 8.6/10.8/10.9, conforme o alcance de cada escolha. Não reenviar; proposta e alternativa preservadas como histórico.

### 12.13. M13-08A — DECIDIDO — Renji jogável, Byakuya e prisão no desfecho

**Recomendação:** MAIN-SUB-07 de 10.10; aviso de Kon, início pelo painel no local indicado, vitória contra Renji e texto breve de Byakuya conduzindo Rukia à Soul Society. Sem derrota/morte/perda de poderes forçada do jogador; Byakuya não é boss desta primeira ocorrência. Prisão já é marco protegido STR-03, não nova votação.

**Alternativa:** atividade adicional de fuga/retirada perante Byakuya, explicitamente comunicada, a detalhar. Não transformar confronto aparentemente vencível em derrota inevitável.

**Pergunta 1:** Recomendo lutar e vencer Renji; Byakuya e a prisão de Rukia ficam no desfecho escrito. A missão conclui pela vitória, e o resgate segue para o próximo arco. Adotamos esse formato?

**Resposta literal em 28/10/2026:** “Concordo”. Aplicada em 8.7/10.10; pergunta e alternativa preservadas como histórico. Não reenviar.

### 12.14. M13-08B — DECIDIDO COM REVISÃO — Incluir avaliação antes da expedição

**Recomendação:** MAIN-SUB-08 de 10.11; Urahara apresenta o plano e oferece a passagem da expedição; jogador parte e conclui ao chegar ao Rukongai. Recuperação dos poderes de Ichigo permanece no contexto; não impor essa trajetória a todas as raças. Percurso e condições de acesso/retorno ainda exigem integração M10/M12/M15.

**Alternativa:** incluir uma prova prática principal antes da viagem, sem repetir formação racial. Exigiria selecionar uma tarefa jogável própria e seus requisitos; não foi aprovada ou detalhada nesta proposta.

**Pergunta 2:** Depois, recomendo Urahara apresentar brevemente o plano e encaminhar uma missão de atravessar a passagem até o Rukongai. O treino de recuperação de Ichigo fica em texto, e a progressão do jogador segue suas regras próprias. Seguimos assim ou você quer uma prova prática de preparação antes da viagem?

**Primeira resposta literal em 28/10/2026:** “Como seria uma prova prática de preparação? Seria o treinamento breve naquela sala na loja do urahara?”. Foi pedido de explicação, sem escolha naquele momento; o detalhamento deu origem à ficha 10.12.

**Esclarecimento proposto:** sim, uma avaliação curta no campo subterrâneo da loja; usar movimento/defesa e acertar um golpe válido em Urahara durante um duelo de treino. Esse objetivo é adaptação proposta, não tarefa literal confirmada pela sinopse. Recuperação de poderes de Ichigo permanece no contexto. O jogador mantém seu repertório e progressão próprios.

**Pergunta de esclarecimento agora respondida:** Você quer incluir essa prova curta no campo subterrâneo antes da expedição, ou manter apenas o plano e a travessia?

**Resposta posterior em 28/10/2026:** “Perfeito, podemos seguir assim”, seguida do acréscimo de perseguição preservado integralmente em 8.8. Aprova inclusão da prova explicada; proposta inicial sem treino preterida. Aplicada em 8.8/10.12; não reenviar. Detalhes da travessia continuam em 10.11.

### 12.15. M13-08C — DECIDIDO NO ALCANCE ESTRUTURAL — Perseguição de Ururu

Direção literal integral em 8.8: acrescentar perseguição, tempo limite e sucesso ao passar todo o intervalo sem atingir X% de Vida, além do duelo já aceito. Identificação de Ururu em CAMP-S05. Aplicado em 10.12; não voltar a perguntar se inclui. Valores e retenção foram posteriormente escolhidos em 09A/B; detalhes de integração continuam pendentes, sem aprovação por associação.

### 12.16. M13-09A — DECIDIDO COM VALORES PROVISÓRIOS — Duração e limite de Vida da perseguição

**Recomendação:** começar com 60 segundos nominais e limite de 30% da Vida máxima de referência no início; atingir esse valor ou menos em qualquer momento falha, mesmo se houver recuperação posterior. Proposta provisória para balancear, sem teste em jogo; tratamento de formas/custos e simulação ainda por integração conforme 10.12.

**Pergunta 1:** Para a perseguição, recomendo começar com 60 segundos e falha ao chegar a 30% da Vida ou menos, em qualquer momento. São valores provisórios para testar depois. Concorda ou prefere outro tempo/limite?

**Resposta literal em 28/10/2026:** “Pode ser isso mesmo, depois caso não, ajusto”. Aplicada em 8.9/10.12; não reenviar. Valores de 09A permanecem provisórios para balanceamento posterior.

### 12.17. M13-09B — DECIDIDO — Preservar a primeira etapa concluída

**Recomendação:** concluir Ururu mantém essa etapa; falhar depois contra Urahara exige repetir somente o duelo. Alternativa: reiniciar toda a avaliação desde a perseguição. Não amplia automaticamente as regras de checkpoint de outras missões.

**Pergunta 2:** Se falhar no duelo com Urahara, recomendo repetir só esse duelo, mantendo a etapa de Ururu concluída. Seguimos assim ou prefere reiniciar as duas etapas?

**Resposta literal em 28/10/2026:** “Mantemos concluida”. Aplicada em 8.9/10.12; não reenviar. Valores de 09A permanecem provisórios para balanceamento posterior.

### 12.18. M13-10A — DECIDIDO — Travessia breve até o Rukongai

**Recomendação:** MAIN-SUB-08 com percurso breve jogável e chegada registrada, sem nova corrida cronometrada. Kōtotsu pode permanecer no contexto escrito; alternativa é adaptá-lo como perseguição própria, com ficha de falha/retomada. Fonte CAMP-S02/21, limites e impactos em 10.11/10.13.

**Pergunta 1:** Na viagem, recomendo uma travessia curta até o Rukongai, deixando o Kōtotsu no contexto escrito. Seguimos assim ou prefere uma perseguição jogável dele?

**Resposta conjunta literal em 28/10/2026:** “Perfeito, podemos adotar isso sim”. Recomendação aceita e aplicada em 8.10/10.11/10.13 conforme o alcance. Não reenviar; alternativas preservadas como histórico.

### 12.19. M13-10B — DECIDIDO — Jidanbō jogável e Gin no desfecho

**Recomendação:** MAIN-SS-01 de 10.13: vencer Jidanbō; Gin fecha o acesso no desfecho escrito e Yoruichi indica procurar Kūkaku. A missão conclui pela vitória contra o guardião, sem luta invencível/derrota forçada adicional. Alternativa: desenvolver retirada jogável perante Gin, com objetivo próprio.

**Pergunta 2:** Na chegada, recomendo vencer Jidanbō; Gin fecha a entrada no desfecho escrito, e a próxima etapa busca o caminho de Kūkaku. Adotamos esse formato?

**Resposta conjunta literal em 28/10/2026:** “Perfeito, podemos adotar isso sim”. Recomendação aceita e aplicada em 8.10/10.11/10.13 conforme o alcance. Não reenviar; alternativas preservadas como histórico.

### 12.20. M13-11A — DECIDIDO COM REVISÃO — Incluir confronto com Ganju

**Recomendação:** apresentar Ganju e o conflito com Ichigo brevemente no contexto da busca de Kūkaku; sem nova luta obrigatória nesse trecho. Alternativa: desenvolver confronto adaptado do jogador com Ganju. CAMP-S02/23 confirma briga interrompida, sem aprovar condição de vitória do mod.

**Pergunta 1:** Recomendo apresentar Ganju no contexto e seguir para Kūkaku, sem duelo obrigatório. Concorda ou prefere incluir uma luta contra ele?

**Resposta literal em 28/10/2026:** “Prefiro incluir”. Aplicada em 8.11/10.14/10.15 conforme o alcance. 11A escolheu incluir o duelo, preterindo a recomendação de somente contexto; 11B aceitou o exercício e lançamento. Não reenviar.

### 12.21. M13-11B — DECIDIDO — Exercício breve da esfera e lançamento pelo canhão

**Recomendação:** MAIN-SS-02 de 10.14; jogador inicia exercício de ajustar/manter marcador de energia na faixa indicada, depois inicia a partida pelo canhão e conclui ao chegar ao interior do Seireitei. Objetivo inspirado na esfera da obra, interface adaptada; valores/custos/tolerância ainda por ficha.

**Pergunta 2:** Com Kūkaku, recomendo um exercício curto de manter a energia da esfera estável e, depois, iniciar o lançamento pelo canhão. Seguimos assim ou prefere ir direto ao lançamento?

**Resposta literal em 28/10/2026:** “Podemois seguir assim”. Aplicada em 8.11/10.14/10.15 conforme o alcance. 11A escolheu incluir o duelo, preterindo a recomendação de somente contexto; 11B aceitou o exercício e lançamento. Não reenviar.

### 12.22. M13-12A — DECIDIDO — Vitória completa contra Ganju

**Recomendação:** vencer a representação de Ganju e concluir o desafio; ele permanece vivo e segue no contexto de Kūkaku. A briga original é interrompida, e esse resultado completo é adaptação do mod. Alternativa: objetivo explícito de pressão/desempenho antes de encerrar, ainda sem limiar definido.

**Pergunta 1:** Para Ganju, recomendo vencer o confronto por completo; ele encerra o desafio e permanece vivo para a história. Adotamos esse resultado?

**Resposta literal em 28/10/2026:** “Concordo”. Aplicada em 8.12/10.15/10.16 conforme o alcance da escolha; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.23. M13-12B — DECIDIDO — Ikkaku e Yumichika em duas missões

**Recomendação:** após o canhão, Ikkaku e depois Yumichika, em missões separadas; falhar na segunda não repete a primeira. As lutas originalmente paralelas são adaptadas para o jogador, sem Ganju aliado de combate. Alternativa: Ikkaku jogável e ocorrência de Yumichika/Ganju no texto.

**Pergunta 2:** Depois da entrada, recomendo duas missões: vencer Ikkaku e depois Yumichika, mantendo Ikkaku concluído se falhar na segunda. Seguimos assim ou prefere somente Ikkaku jogável?

**Resposta literal em 28/10/2026:** “seguimos assim”. Aplicada em 8.12/10.15/10.16 conforme o alcance da escolha; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.24. M13-13A — DECIDIDO — Jirōbō jogável antes de Hanatarō

**Recomendação:** incluir MAIN-SS-04: jogador vence Jirōbō no confronto ligado a Uryū/Orihime; sem aliados canônicos, barra de defesa de Orihime ou missão extra de ondas para o cerco de soldados genéricos. Alternativa: caso de Jirōbō somente em texto e ida direta a Hanatarō. Referências e adaptação em 10.17.

**Pergunta 1:** Antes de Hanatarō, recomendo incluir a luta contra Jirōbō, que enfrenta Uryū e Orihime na obra, deixando o cerco de soldados comuns no contexto. Adotamos ou prefere esse confronto apenas em texto?

**Resposta literal em 28/10/2026:** “Adotamos”. Aplicada em 8.13/10.17/10.18 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.25. M13-13B — DECIDIDO — Travessia subterrânea sem escolta de Hanatarō

**Recomendação:** Hanatarō apresenta a passagem e jogador percorre uma rota curta até a saída próxima à prisão; conclusão disponibiliza Renji em início próprio. Sem escolta, barra de defesa, coleta ou cura automática. Alternativa: missão de acompanhar/proteger Hanatarō, com condições próprias ainda a definir.

**Pergunta 2:** Com Hanatarō, recomendo uma travessia curta pelas galerias até a saída próxima à prisão, sem precisar escoltá-lo. Seguimos assim ou prefere acompanhar e proteger o NPC pelo caminho?

**Resposta literal em 28/10/2026:** “Seguimos assim”. Aplicada em 8.13/10.17/10.18 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.26. M13-14A — DECIDIDO — Segundo Renji e interlúdio de Aizen em texto

**Recomendação:** MAIN-SS-06, Renji desta versão como confronto próprio; após a vitória, reação do Seireitei/aparente morte de Aizen em contexto escrito, sem investigação obrigatória ou consequências físicas de Ichigo impostas ao jogador. A regra geral de vitória é herdada de STR-03; a escolha nova é a cobertura da transição. Alternativa: atividade própria de investigação, ainda sem ficha.

**Pergunta 1:** Depois da vitória contra o segundo Renji, recomendo deixar a aparente morte de Aizen e as reações dos capitães no contexto escrito, sem missão obrigatória de investigação. Seguimos assim ou prefere investigar esse acontecimento em uma missão própria?

**Resposta literal em 28/10/2026:** “Concordo”. Aplicada em 8.14/10.19/10.20 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.27. M13-14B — DECIDIDO — Shunsui jogável antes de Kenpachi

**Recomendação:** MAIN-SS-07, jogador enfrenta e vence Shunsui na adaptação do confronto de Chad; capitão permanece vivo e próximo bloco é Kenpachi. Alternativa: ocorrência Chad/Shunsui no texto e avanço direto a Kenpachi. Não impor derrota do jogador para reproduzir a situação de Chad.

**Pergunta 2:** Antes de Kenpachi, recomendo incluir Shunsui como confronto vencível do jogador, adaptando a ocorrência de Chad. Adotamos ou prefere esse encontro somente em texto?

**Resposta literal em 28/10/2026:** “Adotamos”. Aplicada em 8.14/10.19/10.20 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.28. M13-15A — DECIDIDO — Contato com Rukia e primeiro Byakuya em texto

**Recomendação:** após vencer Kenpachi, alcançar/interagir com Rukia; Byakuya/Yoruichi no desfecho escrito, sem derrota forçada. Reservar confronto completo de Byakuya para o resgate no local da execução. Alternativa: atividade própria de retirada nesta primeira chegada, sem perfil ainda. Fichas em 10.21/10.22.

**Pergunta 1:** Depois de Kenpachi, recomendo chegar à prisão e falar com Rukia; Byakuya e a intervenção de Yoruichi ficam no desfecho escrito. Guardamos a luta principal contra Byakuya para o resgate no local da execução. Adotamos esse formato?

**Resposta literal em 28/10/2026:** “Adotamos”. Aplicada em 8.15/10.22/10.23 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.29. M13-15B — DECIDIDO — Mayuri jogável com retirada no desfecho

**Recomendação:** MAIN-SS-10, jogador vence Mayuri da ocorrência de Uryū; ele recua no desfecho, permanece vivo e missão conclui pela vitória. Não exigir Letzt Stil ou perda de poderes para reproduzir Uryū. Alternativa: ocorrência em texto, sem confronto jogável.

**Pergunta 2:** Depois, recomendo enfrentar e vencer Mayuri; ele escapa no desfecho escrito, mantendo a continuidade da história. Incluímos esse confronto jogável ou prefere deixá-lo em texto?

**Resposta literal em 28/10/2026:** “Incluimos”. Aplicada em 8.15/10.22/10.23 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.30. M13-16A — DECIDIDO COM REVISÃO — Incluir preparação jogável própria com Yoruichi

**Recomendação:** Yoruichi apresenta plano do resgate; treino de Bankai de Ichigo e duelo Renji/Byakuya no contexto escrito, sem nova prova obrigatória ou concessão de poder ao jogador. Sem prazo real automático ou missão somente de diálogo. Alternativa: exercício prático próprio, ainda sem tarefa selecionada. Bloco 10.24.

**Pergunta 1:** Recomendo Yoruichi apresentar o plano do resgate, deixando o treino de Bankai de Ichigo e o duelo Renji/Byakuya no contexto escrito, sem outra prova obrigatória. Seguimos assim ou quer uma preparação jogável própria nesse momento?

**Resposta literal em 28/10/2026:** “Prefiro uma preparação jogável própria”. Aplicada em 8.16/10.24/10.25 conforme o alcance. 16A escolhe preparação própria, conteúdo ainda em 17A; 16B aceita duas missões com retenção da primeira. Não reenviar.

### 12.31. M13-16B — DECIDIDO — Tōsen e Komamura em duas missões

**Recomendação:** adaptar a frente de Kenpachi para o jogador: vencer Tōsen e depois Komamura em missões separadas, preservando Tōsen concluído se falhar na segunda. Sem Kenpachi aliado. Alternativas: um único encontro simultâneo a especificar, ou frente só em contexto. Ficha 10.25.

**Pergunta 2:** Antes do resgate, recomendo duas missões: vencer Tōsen e depois Komamura, mantendo a primeira concluída se falhar na segunda. Seguimos assim, prefere enfrentar os dois juntos ou deixar essa frente em texto?

**Resposta literal em 28/10/2026:** “Duas missões”. Aplicada em 8.16/10.24/10.25 conforme o alcance. 16A escolhe preparação própria, conteúdo ainda em 17A; 16B aceita duas missões com retenção da primeira. Não reenviar.

### 12.32. M13-17A — DECIDIDO — Circuito de aproximação e alvo final

**Recomendação:** MAIN-SS-PREP-01 em 10.26, circuito curto orientado por Yoruichi com pontos em ordem, obstáculos/desníveis e interação com alvo final dentro de tempo próprio. Viável com movimentos do personagem apto; números/mapa ainda por ficha. Alternativa: duelo curto com Yoruichi, sem perfil definido. A inclusão do treino já foi escolhida; esta é somente a seleção de conteúdo.

**Pergunta 1:** Para Yoruichi, recomendo um ensaio de resgate: atravessar pontos marcados com obstáculos e desníveis e acionar um alvo de treino no final, dentro do tempo limite. Adotamos esse circuito ou prefere um duelo curto contra ela?

**Resposta literal em 28/10/2026:** “Adotamos”. Aplicada em 8.17/10.26/10.27 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.33. M13-17B — DECIDIDO — Libertação de Rukia como ação do jogador

**Recomendação:** MAIN-SS-12 em 10.27; jogador alcança/interage para libertar Rukia, Sōkyoku no contexto escrito e Byakuya como confronto seguinte em início próprio. Alternativa: desafio jogável separado para interromper Sōkyoku antes da libertação, ainda sem ficha.

**Pergunta 2:** No resgate, recomendo alcançar Rukia e libertá-la por interação, deixando a interrupção do Sōkyoku no contexto escrito; Byakuya vem como luta seguinte. Seguimos assim ou quer também um desafio jogável contra a arma da execução?

**Resposta literal em 28/10/2026:** “Seguimos asism”. Aplicada em 8.17/10.26/10.27 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.34. M13-18A — DECIDIDO — Byakuya em um confronto contínuo

**Recomendação:** MAIN-SS-13 em 10.28, uma única luta com progressão Shikai → Bankai → Senkei, sem missão/tentativa nova entre momentos. Vitória e inclusão já herdadas; parâmetros/gatilhos ainda por ficha. Alternativa: separar momentos em tentativas/missões com retenção, a especificar.

**Pergunta 1:** Para Byakuya, recomendo uma única luta contínua, passando por Shikai, Bankai e depois Senkei, sem reiniciar a missão entre esses momentos. Concorda ou prefere etapas com tentativas separadas?

**Resposta literal em 28/10/2026:** “Concordo”. Aplicada em 8.18/10.28/10.29 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.35. M13-18B — DECIDIDO — Soi Fon jogável e Yamamoto em contexto

**Recomendação:** incluir Soi Fon como missão do jogador antes do resgate, adaptando a frente de Yoruichi; Yamamoto/Shunsui/Ukitake no contexto escrito. Preserva libertação → Byakuya já aprovados e concentra o clímax do resgate. Alternativa: incluir também Yamamoto em confronto adaptado, ainda sem ficha.

**Pergunta 2:** Para as frentes paralelas, recomendo Soi Fon jogável antes do resgate e a disputa de Yamamoto com Shunsui/Ukitake em texto, mantendo Byakuya como o clímax do resgate. Adotamos ou prefere incluir também Yamamoto como luta do jogador?

**Resposta literal em 28/10/2026:** “Adotamos”. Aplicada em 8.18/10.28/10.29 conforme o alcance; pergunta/alternativa preservadas como histórico. Não reenviar.

### 12.36. M13-19A — DECIDIDO — Outras ocorrências finais em contexto

**Recomendação:** pacote de 10.30, Hitsugaya/Gin, Iba/Ikkaku, Hisagi/Yumichika, oficiais do resgate e histórias pessoais no contexto, sem novas lutas obrigatórias. Não reabrir escolhas já aprovadas ou impedir conteúdo lateral futuro. Alternativa: escolher alguma dessas ocorrências para missão própria.

**Pergunta 1:** Para fechar a cobertura, recomendo deixar Hitsugaya/Gin, Iba/Ikkaku, Hisagi/Yumichika e a intervenção dos oficiais do resgate no contexto escrito, sem mais missões de combate. Concorda ou quer algum desses encontros jogável?

**Resposta literal em 28/10/2026:** “Concordo”. Aplicada em 8.19/10.30; não reenviar a seleção dessas ocorrências.

### 12.37. M13-19B — DECIDIDO COM REVISÃO — Aizen em contexto neste arco

**Recomendação anterior preservada:** MAIN-SS-14, revelação e obtenção do Hōgyoku no contexto inicial; jogador vence Aizen desta versão e missão conclui; ele foge com Gin/Tōsen no desfecho. Adaptação explícita STR-03, sem morte definitiva/selamento ou Hōgyoku como recompensa. Alternativa: atividade distinta de retirada/proteção, sem falso boss vencível, ainda sem ficha.

**Pergunta 2:** Para Aizen, recomendo revelar a conspiração e a obtenção do Hōgyoku no contexto inicial; depois o jogador vence esse confronto, e ele foge com Gin e Tōsen no desfecho. A vitória é nossa adaptação, mantendo a fuga necessária aos próximos arcos. Adotamos?

**Resposta literal em 28/10/2026:** “porque adaptar para uma vitória?”. Pedido de justificativa, sem aprovação de vencer Aizen ou escolha de alternativa.

**Esclarecimento atual:** a proposta veio de aplicar STR-03 às representações dos bosses. Reavaliado em 10.31.1: esse contrato não obriga selecionar esta aparição como boss vencível. Recomenda-se agora superioridade de Aizen e revelação/Hōgyoku/fuga em contexto, mantendo Byakuya último boss deste arco e Aizen vencível nos momentos posteriores apropriados. A recomendação reconsiderada foi posteriormente aprovada na resposta abaixo; proposta antiga de vitória não foi adotada.

**Pergunta de continuidade:** Recomendo manter essa aparição de Aizen no contexto, preservando sua superioridade e saída com o Hōgyoku, sem vitória do jogador contra ele neste arco. Seguimos com esse recorte?

**Resposta posterior literal em 28/10/2026:** “Perfeito, aprovo”. Aplicada em 8.20/10.31. Byakuya último boss, Aizen em contexto; não reenviar. Proposta antiga retirada preservada em histórico, sem quest/boss MAIN-SS-14 no catálogo vigente.

## 13. Critérios de aceitação futuros

O jogador recebe contexto breve, reconhece quem oferece a missão e entende o objetivo; NPC/painel inicia somente atividade disponível e cria um confronto uma única vez no contexto válido; participa dos confrontos e reconhece o elenco sem assumir a biografia de Ichigo. Missão racial/filler não entra silenciosamente no catálogo principal. Falhas permitem retomada coerente e não destroem o mundo para repetir uma cena. Recompensas e participação precisam de contratos antes da implementação; nenhum critério foi testado em jogo nesta rodada.

## 14. Impacto técnico conhecido

Esta abertura organiza conteúdo de design. Arquivos de quests existentes foram localizados, sem nova auditoria ou alteração; sua presença não demonstra suporte completo a estas campanhas. Não foram produzidos JSONs, diálogos finais, mapas, encontros ou recompensas executáveis.

## 15. Próximos passos

Roteiro/seleção dos arcos Shinigami Substituto e Soul Society fechados no alcance das decisões 01A–19B em 28/10/2026. Sequência de 28 missões em 10.32; revelação de Aizen sem boss nesta aparição em 10.31 e epílogo/continuidade em 10.33. Preparar publicação documental autorizada na main após revisão de consistência/referências; confirmar publicação pelo histórico Git e resultado remoto, sem alegar execução antecipada. Próxima rodada: campanha principal Arrancar; não iniciar conteúdo novo nesta publicação. M13 permanece em especificação das integrações e dos próximos arcos; M11/racial/fillers/laterais adiados. Nenhum código, mapa/save, build ou teste de gameplay integra este fechamento.
