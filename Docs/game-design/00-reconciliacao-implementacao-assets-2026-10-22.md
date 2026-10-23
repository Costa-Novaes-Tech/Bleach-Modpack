# Reconciliação — implementação e lista de conteúdo

## Estado e escopo — 22/10/2026

**Estado: conferência dirigida do checkout e análise de propostas.** Subsídio aos módulos, sem aprovar automaticamente catálogo, prioridades ou implementação. A lista enviada pelo usuário foi lida integralmente, incluindo animais/atores, armas, props, cosméticos, estruturas, VFX, primeira leva e dicas técnicas. Os grupos abaixo preservam suas candidaturas; a síntese não é transcrição literal.

A cópia estava em `8ace6520e67d27441974b9ad92b443ddb33f9882`. Consulta ao GitHub e fetch localizaram `bb26be85fc3dac703f67e79e772fca5158eecdef` em `origin/main`; atualização local por **fast-forward**, sem commit local/push. Dez commits alcançados, incluindo merges; isso não equivale a dez funcionalidades independentes. Fontes remotas agora incorporadas contêm variantes/remodelagem Hollow, boss/cinemática e correções técnicas.

Antes da atualização, os 35 arquivos locais alterados ou não rastreados foram copiados para `/private/tmp/bleach-retomada-20261022-195247`, com manifesto SHA-256 e patch. Os únicos conflitos de caminho eram `.DS_Store` e `Docs/.DS_Store`, removidos/ignorados pelo upstream e preservados no backup. Conferência por hash logo após o avanço não encontrou diferenças nos demais arquivos locais preexistentes. Nenhum save foi aberto ou migrado nesta sessão.

**Plataforma reconferida:** Minecraft Java `1.20.1`, Forge `47.4.10`, Java `17`, declarados em [gradle.properties](../../gradle.properties) e [build.gradle](../../build.gradle). Não foi executado build, cliente, teste ou instalação de dependências. Atualizar código-fonte não recompila nem substitui automaticamente um JAR instalado no launcher.

A [auditoria de 20/10](00-auditoria-estado-2026-10-20.md) continua como fotografia histórica. Este documento atualiza o recorte abaixo; não reaudita todas as skills, rede, saves ou campanhas.

## 1. O que o código e os arquivos mostram hoje

| Informação recebida | Conferência atual | Consequência para o planejamento |
|---|---|---|
| Sete Zanpakutō/modelos | [Registro de itens](../../src/main/java/com/bleachmod/init/ModItems.java) contém **seis itens-base**: Asauchi, Benihime, Hyōrinmaru, Nozarashi, Ryūjin Jakka e Senbonzakura. Há modelos adicionais de formas. `senbonzakura_hakuteiken.json` é uma seleção do modelo de Senbonzakura, não um sétimo item-base | Separar arma/identidade, forma e arquivo de modelo. A localização chama a capacidade **Shūkei: Hakuteiken**; “Kenseikan Hakuteiken” não é o identificador local. Não criar nova arma independente a partir dessa contagem |
| Cerca de 40 NPCs, com skin de jogador | **41 IDs**: oito registros diretos e 33 do roster, sem sobreposição. Todas as texturas correspondentes existem; os renderers usam `PlayerModel` | Não são 41 modelos anatômicos próprios nem 41 mestres/IA de combate completos. Um modelo de jogador já é 3D; a lacuna é geometria/rig próprio e função jogável onde necessários |
| Um Hollow genérico | `HollowEntity` agora possui **brute, swift e flying**, com geometria, textura, animação, atributos e dimensões próprios | São três variantes do mesmo tipo `bleachmod:hollow`; não três estágios raciais ou três espécies canônicas aprovadas |
| Só o dragão em GeckoLib | `HollowRenderer`, `HollowBossRenderer` e `IceDragonRenderer` usam o caminho GeckoLib. Há cinco `.geo.json`: três variantes, boss e dragão | A infraestrutura já suporta esse recorte. Reaproveitar primeiro, sem tornar GeckoLib obrigatório para todo item, bloco, acessório e efeito |
| Boss Hollow com modelo | Boss atualizado com modelo próprio e entrada articulada pela Garganta; código de combate e sincronização presentes | Portal da apresentação do boss não comprova viagem utilizável, destino ou progressão de Garganta para jogadores |
| Dragão de gelo como mob | [IceDragonEntity](../../src/main/java/com/bleachmod/entity/IceDragonEntity.java) se define e opera como **projétil transitório de técnica**, sem IA de fauna e sem persistência normal | Não contabilizar como criatura ambiente pronta ou pet; visual semelhante pode servir a funções diferentes |
| Três dimensões: Humano/Soul Society/Hueco Mundo | O preset de História declara duas dimensões adicionais, Soul Society/Hueco Mundo. O mapa `human_world` aponta para `minecraft:overworld` | Três destinos de história não equivalem a três dimensões adicionais. Mundos comuns não recebem automaticamente o mesmo arranjo |
| Estruturas: só `empty.nbt` | Confirmado no recorte de `src/main/resources/data/`. Porém existem regiões de **três mapas em `history-maps/`**, manifest e instalador | Ausência de templates de estrutura não prova ausência de cidades/construções nos mapas. Inspeção visual e inventário de conteúdo desses mapas ainda pendentes |
| Viagem `travel_*` | Itens e `WorldTravelItem` existem como transporte provisório ao spawn de destino do History Mode, resolvido no servidor | Não aprovar chaves permanentes, viagem sem requisitos ou sistema final de Senkaimon/Garganta a partir do placeholder |

### Fontes locais do inventário

- NPCs: [ModEntities](../../src/main/java/com/bleachmod/registry/ModEntities.java), [NpcRoster](../../src/main/java/com/bleachmod/registry/NpcRoster.java), [roster JSON](../../src/main/resources/data/bleachmod/npcs/roster.json), [renderers](../../src/main/java/com/bleachmod/client/events/ModEntityRenderers.java).
- Hollows: [HollowVariant](../../src/main/java/com/bleachmod/entity/HollowVariant.java), [HollowRenderer](../../src/main/java/com/bleachmod/client/model/HollowRenderer.java), [HollowBossRenderer](../../src/main/java/com/bleachmod/client/model/HollowBossRenderer.java). Conferência dirigida; não auditoria integral de todas as ações da IA.
- Armas: [Senbonzakura e overrides](../../src/main/resources/assets/bleachmod/models/item/senbonzakura.json), [Hakuteiken](../../src/main/resources/assets/bleachmod/models/item/senbonzakura_hakuteiken.json), [localização](../../src/main/resources/assets/bleachmod/lang/pt_br.json).
- Mundo: [HistoryWorld](../../src/main/java/com/bleachmod/server/world/HistoryWorld.java), [preset](../../src/main/resources/data/bleachmod/worldgen/world_preset/history_mode.json), [manifest](../../history-maps/maps.json), [README dos mapas](../../history-maps/README.md), [instalador](../../src/main/java/com/bleachmod/server/world/HistoryMapInstaller.java), [viagem provisória](../../src/main/java/com/bleachmod/common/item/WorldTravelItem.java). Pastas locais de mapas não equivalem a conteúdo já instalado em todo save ou incluído automaticamente em todo pacote distribuído.

## 2. Revisão integral da lista recebida

**Todas as prioridades abaixo são as recebidas ou candidatas, sem aprovação de uma primeira leva.** Existência de skin/modelo não concede kit ao jogador, papel narrativo, estágio racial ou cargo. Fichas concretas ainda precisam de fonte canônica própria e contratos de gameplay.

### 2.1. Hollows e habitantes

| Candidatura recebida | Parecer e destino |
|---|---|
| Fishbone D — inicial no Mundo Humano, andar/garra/rugido, P1 | Bom candidato a confronto inicial identificável. Distinguir indivíduo da história e família reutilizável; conferir aparência, repertório e papel em M09/M13 antes de fixar spawn |
| Acidwire — “lagarto”, rastejar/cuspe, P2 | Nome, anatomia e técnica não estão validados por esta lista. Tratar como personagem associado ao contexto da história, não converter automaticamente em espécie comum; fonte e ficha M09/M13 |
| Shrieker — parasitas/“mini-Hollows”, minichefe P2 | Definir criaturas/efeitos da própria capacidade e sua autoria; não transformar parasita em Hollow independente com loot/XP por omissão. Pesquisa e encontro M09/M13 |
| Grand Fisher — chefe de história/isca/garras, P1 | Manter encontro narrativo separado de spawn genérico. Era, alvo, condição de vitória/fuga e consequências precisam respeitar M03; M09/M13 |
| Gillian/Menos Grande — gigante de aproximadamente seis blocos, Cero bucal, P1 | Recomendável para contraste de escala e ameaça; **seis blocos é proposta de escala**, não medida canônica nem perfil racial já aprovado. Colisão, câmera, telegráfo, terreno, custo de presença e frequência em M09/M10/M15 |
| Adjuchas — pantera/lagarto/ave, elites P2 | Estágio e anatomia não se deduzem apenas da forma do modelo. Pantera ligada a identidade específica não vira corpo de todo Adjuchas; distinguir catálogo de inimigos e evolução do jogador. M04/M07/M09 |
| Aranha/voador — variedade P3; primeira leva de três–quatro variantes | Voador e duas variantes terrestres já existem. Avaliar seus papéis antes de encomendar o mesmo recorte novamente; aranha ou quarta variante só se acrescentar situação de combate distinta |
| Várias máscaras por textura no mesmo modelo | Boa técnica candidata para variação visual compatível. Máscara que muda silhueta precisa de geometria; cosmético não muda alcance/colisão sem perfil funcional declarado |
| Jigokuchō — ambiente e guia de Senkaimon P1 | Candidata pertinente; distinguir ambiente, orientação e viagem. “Barata” exige estimativa real de animação/IA/integração, não só tamanho pequeno; M09/M10 |
| Plus/Corrente do Destino/Konsō — P1 | Não é apenas decoração: envolve estado da alma, interação, destino, autoria e recompensas. Definir função M04/M09/M12 antes do modelo; não criar farm universal por respawn |
| Kon — pet ou decoração P3 | Separar réplica decorativa do personagem consciente. IA de pet é adaptação nova, não consequência automática de estar numa pelúcia; M08/M09/M11/M13 |
| “Morcego do Ulquiorra” — ambiente Las Noches P3 | **Referência pendente.** Aparência/tema de um poder não comprova fauna canônica. Se criatura original do mod, rotular e propor; não preencher bioma como fato da obra |

### 2.2. Invocações, armas e poderes

| Candidatura recebida | Parecer e destino |
|---|---|
| Zanka no Tachi, Minami/esqueletos — P2 | Já há definição `minami` e rotina `army` em [ElementalTechniqueService](../../src/main/java/com/bleachmod/common/technique/ElementalTechniqueService.java). Conferir/revisar representação existente antes de tratar como funcionalidade ausente; visual, invocação, persistência e alvos são partes distintas |
| Hihiō Zabimaru segmentado P2; Kokujō Tengen Myō'ō P2; Konjiki Ashisogi Jizō P3 | Não são simples props: requerem ficha do poder, controle/autoria, colisão/alvos, sustentação/retorno e performance. Geometria segmentada ou um braço emergindo não define o kit inteiro; M06/M07/M09/M11 |
| Daiguren Hyōrinmaru — revisar asas/cauda P2 | Concordar com reaproveitamento: [IceArmorLayer](../../src/main/java/com/bleachmod/client/model/IceArmorLayer.java) já tem placas, asas e cauda. Visual de asa não aprova voo ou altera proteção por si só |
| Zangetsu/Tensa Zangetsu, Zabimaru e Sode no Shirayuki — P1 | Relevantes ao elenco, mas distinguir **NPC equipado**, item visual e poder jogável. Uma espada para Ichigo não exige entregar imediatamente toda a progressão de Zangetsu ao jogador. Prioridade deve seguir a fatia de campanha, sem mudar agora o primeiro caso de linhagem já escolhido |
| Hōzukimaru, Katen Kyōkotsu, Suzumebachi/Jakuhō Raikōben — P2 | Fichas separam estado selado, Shikai, Bankai e suas mãos/poses/alcances; não um simples trio obrigatório de modelos por identidade |
| Kyōka Suigetsu, Tobiume, Kazeshini, Haineko, Sōgyo no Kotowari, Fuji Kujaku, Minazuki e Tenken — P2–P3 | Preservados como candidatos de catálogo. Forma exata, nome por estágio, técnicas e era exigem pesquisa própria; lista não valida todos os nomes/estágios nem aprova novos kits |
| Bainha própria para cada arma | [ZanpakutoSheathLayer](../../src/main/java/com/bleachmod/client/model/ZanpakutoSheathLayer.java) já trata identidades com ramificações específicas, inclusive bengala e Nozarashi sem bainha. **Não é encaixe automático de qualquer modelo**; transporte, poses, mãos e anatomia precisam de integração |
| Pantera; Murciélago/Lanza del Relámpago; Santa Teresa; Tiburón; pistolas de Los Lobos | Não agrupar forma corporal, arma selada e técnica como um único tipo de item. Santa Teresa exige conferir corpo/armamento liberados, não somente a lâmina selada. Lanza exige versão/condição própria. M06/M07/M11, linhagens depois |
| Ginrei Kojaku, Seele Schneider e Cruz Quincy como pingente | Candidatos de armamento/instrumento. Arco inicial mantém foco aprovado; aparência de pingente não decide slot de equipamento. Nome de arco específico não define arma universal de todos os Quincy |
| Cross of Scaffold; Brazo Derecha del Gigante/Brazo Izquierda del Diablo; Shun Shun Rikka/seis espíritos | Candidatos de vetores/efeitos de poderes distintos. Não confundir braço/campo/espírito com arma de inventário, pet ou cosmético. Classificação e condições de Orihime exigem fonte apropriada; ficha promocional resumida não resolve sozinha essa classificação |

### 2.3. Itens e props

| Candidatura recebida | Parecer e destino |
|---|---|
| Hōgyoku — história/chefe Aizen | Evento, disponibilidade e efeito próprio; não vira item livre de Arrancarização universal. A via pessoal aprovada não exige Hōgyoku |
| Distintivo de Shinigami Substituto | Requer condição narrativa/institucional apropriada; não basta possuí-lo para mudar raça/origem. M04/M08/M11/M13 |
| Gikongan e dispenser de pato | Separar objeto, efeito de separação corpo/alma e contexto; não ativar nova arquitetura de corpo/respawn pela produção do prop. M04/M11 |
| Bengala de Urahara/Benihime selada | Já há caminho visual de bengala na camada de transporte de Benihime; avaliar modelo na mão e o conjunto antes de encomendar do zero. Não afirmar que toda representação desejada já esteja aceita |
| Denreishinki abrindo diário | Boa associação candidata, mas falta decidir acesso ao diário sem o item, perda/reposição e outras origens. Não substituir globalmente a UI aprovada por omissão. M11/M14 |
| Kikanshinki e limitador de Reiatsu | Um pode envolver efeito narrativo sobre memória; outro altera acesso/força se funcional. Não são meros cosméticos sem regras; M05/M08/M11/M12/M13 |
| Fragmento de máscara, osso, cristal de Reishi — drops/crafting P2 | Materiais candidatos do mod, sem origem/receita comprovada pela sugestão. Economia, alvos elegíveis e repetição em M09/M11/M12/M15; não criar outra barra de Reishi |
| `travel_*` convertidos em chaves de Senkaimon/Garganta | Preservar como possibilidade, sem impor chaves universais ou confundir portal visual do boss com transporte. Regras de viagem pertencem a M10/M11 |

### 2.4. Cosméticos e anatomia

- **Haori de capitão P1**, braçadeiras de tenente e números de divisão: visual, cargo e permissão são registros distintos. Vestir não promove; acesso e reprodução visual exigem ficha própria.
- **Máscaras de Ichigo, Shinji, Hiyori, Kensei e Love P1:** cosmético não concede Hollowficação. Sistemas híbridos permanecem na etapa futura já definida, sem antecipação automática por uma referência a RAC-04.
- **Restos de máscara Arrancar** — mandíbula de Grimmjow, capacete de Ulquiorra, tapa-olho de Nnoitra e colar de Starrk: conferir desenho, corpo/forma e vínculo ao poder, distinguindo aspecto de anatomia funcional.
- **Kenseikan/cachecol de Byakuya; sinos/tapa-olho de Kenpachi; chapéu de Urahara; adorno de Mayuri:** candidatos a camadas compatíveis, com cabelo, equipamento e transformação considerados. Acessório visual não entrega poder ou limita visão/dano por omissão.
- **Cabelos 3D P3:** as “70 texturas” são informação recebida, não contagem auditada aqui. Volumes adicionais exigem compatibilidade com estilos/chapéus; não há evidência para afirmar que todo estilo fica melhor obrigatoriamente em 3D.

**Recomendação:** camadas são adequadas a muitos acessórios humanoides. Não tornar isso proibição de outro corpo: perfis Hollow e transformações anatômicas já aprovados precisam permitir geometria e colisão correspondentes. Não substituir o planejamento racial pelo limite atual das skins.

### 2.5. Mundo, blocos e estruturas

| Candidatura recebida | Parecer e destino |
|---|---|
| Senkaimon animado P1; Garganta animada P1 | Apropriados para a identidade visual da viagem; destino, validação, acesso, queda, party e interrupção dependem de M10/M15. Reaproveitamento visual do portal do boss não fecha esses contratos |
| Sōkyoku/alabarda/morro/fênix P2; canhão de Kūkaku/braços/faixa P3 | Conjuntos narrativos com variantes de apresentação/ação, não decoração intercambiável. Fichas de era, escala e evento M10/M13 |
| Portões/kanji, muros, lanternas, tatame, shōji e bandeiras da Soul Society | Primeiro inventariar o mapa existente e o que precisa funcionar como bloco novo. Construção com blocos existentes, template `.nbt` e asset de bloco são entregas diferentes |
| Árvores brancas/cristalinas de “quartzo/Reishi” P1 | Visual e material devem ser conferidos por fonte/cena. Usar quartzo como material Minecraft é adaptação, não prova de composição canônica. Não regenerar biomas ou sobrescrever construções sem contrato M10 |
| Las Noches/colunas/cúpula/trono P2 | Há mapa de Las Noches no manifest. Conferir o conteúdo instalado antes de declarar tudo ausente ou reconstruir “ruínas” sem era/contexto; não houve inspeção visual do mapa nesta sessão |
| Loja Urahara/balcão/prateleiras/letreiro P2 e treino subterrâneo/céu pintado | Identificar presença no mapa, função de atendimento/treino e acesso; não definir localização ou disponibilidade por modelagem isolada |
| Dangai/Kōtotsu P2, perseguição durante viagem | É sistema de viagem/encontro novo, não pequeno detalhe de portal. Regra “persegue quem fica parado” exige pesquisa e proposta própria; não aprovada como fato canônico ou regra do mod |

### 2.6. VFX e recomendações técnicas recebidas

- **Getsuga Tenshō azul/preto P1:** variante/cor dependem da forma e referência; modelo não entrega técnica sem aquisição/requisitos.
- **Cero/Gran Rey Cero — feixe e carga P1:** Cero básico já tem contrato M06. Gran Rey Cero é capacidade distinta a pesquisar/especificar, não melhoria cosmética automática da mesma técnica.
- **Shakkahō, Byakurai, Rikujōkōrō, Dankū e Kurohitsugi P2:** cada um precisa de efeito mecânico próprio. Barras/caixão/parede visíveis não definem imobilização, colisão, bypass ou destruição de blocos.
- **Senkei/anel de “mil espadas”:** há modelo de item e representação existente a revisar; a quantidade da apresentação não deve ser traduzida automaticamente em mil entidades com IA/colisão. Orçamento e legibilidade por M14/M15/M16.
- **Estética Blockbench/cubos/pixel art:** direção candidata coerente para avaliar junto aos assets atuais; “16 px por bloco” não é contrato técnico universal de toda textura/UV, nem necessidade de reautorar o que funciona.
- **JSON vanilla e rotações de elementos:** dica recebida deve ser validada no formato efetivamente usado em 1.20.1. Não confundir rotação do elemento com transformações de exibição/renderer. Lâmina curva não obriga migrar todo item para GeckoLib; escolher formato por necessidade.
- **GeckoLib `.geo.json`/`.animation.json`:** já existe nos Hollows e dragão; reutilizar organização e rig quando fizer sentido, sem nova instalação ou troca global de renderers nesta etapa.
- **Texturas em potências de dois/mipmaps:** recomendação de produção candidata; o upstream contém correção Ryūjin. Tamanho, atlas, filtros e densidade de pixels precisam de compatibilidade, não somente resolução em potência de dois.
- **Gigantes/bones/cubos:** limitar trabalho por tela é necessário, mas não há orçamento numérico aprovado. Medir animação, efeitos, IA, rede e simultaneidade antes de fixar limites; “é pequeno/barato” ou “é só mesh” não estimam uma feature inteira.

## 3. Fontes canônicas conferidas e limites

Consultadas em **22/10/2026**, sem leitura de mangá/episódios integrais:

| Fonte | Uso nesta revisão | Limite |
|---|---|---|
| [Pierrot — sinopse dos episódios 1–20](https://pierrot.jp/title/bleach/playback_01.html), trechos do irmão de Orihime, Shibata, Kon e Grand Fisher | Confirma contexto pessoal/narrativo de confrontos iniciais e Kon recebendo um corpo de pelúcia | Não é catálogo completo de anatomia, animações, escala, taxa de spawn ou sistemas de Konsō |
| [Brave Souls oficial — Kon](https://www.bleach-bravesouls.com/en/character/kon.html) | Personagem é alma modificada, com personalidade; pet genérico não é conclusão canônica | Jogo licenciado como complemento; não aprova sua mecânica no mod |
| [Brave Souls oficial — Nnoitora](https://www.bleach-bravesouls.com/en/character/nnoitora.html) | Ficha distingue liberação com mudança corporal, além da arma | Não detalha todas as fases/armas/anatomia ou um kit para o mod |
| [Brave Souls oficial — Orihime](https://www.bleach-bravesouls.com/en/character/orihime.html) | Ficha curta refere apoio no treino Fullbring de Ichigo | Não basta para fechar origem/classificação universal de seu poder |

Página de personagens TYBW retornou 403 na abertura; resultado de busca não foi tratado como leitura integral. Wiki de modelos apareceu na busca, mas não foi adotada como evidência de API/limites de formato. As demais sugestões continuam exigindo fonte específica quando entrarem nas fichas; esta análise não confirma integralmente toda a lore enviada.

## 4. Evidência de engenharia recebida

Há relatórios e capturas históricos incorporados pelo upstream, com ambientes e escopos próprios. Não foram reexecutados nesta máquina:

- [Vida dos Hollows — 20/10](../qa/hollows/2026-10-20-health/README.md): build/regressões e cliente relatados como aprovados; **uma falha repetida da Shibari/Benihime entre 185 GameTests**, com testes Hollow aprovados. Não declarar essa bateria inteira verde.
- [Entrada articulada do boss — 20/10](../qa/boss/2026-10-20-garganta-movimento/README.md): ciclo posterior relata build com 129 regressões e 188 GameTests obrigatórios aprovados, mais captura/medições no cliente base Windows. Evidência daquele ciclo, não novo teste local nem aceite de todos os sistemas.
- O relatório do boss mantém limites de terreno irregular, espaços estreitos, dois clientes reais, Photon/LDLib e frametime. Presença de GIF/log não elimina esses limites.

## 5. Recomendação e ponto de continuidade

**A lista é útil como banco de conteúdo. Não recomendo transformá-la diretamente numa fila de implementação.** O cenário mudou e ela mistura modelo visual, item, técnica, forma, criatura, viagem e campanha. P1/P2/P3 permanecem sugestões recebidas, sem prazo ou aprovação de produção.

1. **Concluir M08 conceitualmente:** registrar saída/reingresso e prioridade de carreira já escolhidos, revisar ensino/serviços/relações. Fechamento em M08/15.
2. **Seguir M09:** aproveitar bruto/ágil/voador e boss como casos existentes para definir ameaça, percepção, alvo/perseguição, ataque/telegráfo, começo/fim/falha do encontro e encaminhamento de recompensa. Não dar uma IA por skin nem confundir variante com estágio racial.
3. **M10 e M11:** conferir cenários reais, viagem, itens e reaproveitamento antes de afirmar lacunas; mapear os candidatos às suas funções.
4. **M12–M16:** quests/campanhas, UI, multiplayer e entrega fixam a primeira fatia jogável. Só então ordenar a primeira leva por dependências, valor para o jogador e evidência de custo.

**Amostra recomendada para discussão, ainda não aprovada como entrega:** uma caçada com funções distintas entre os Hollows existentes, um encontro especial reaproveitando o boss e um ciclo claro de vitória/falha/retorno. Gillian ou outro novo modelo só entra primeiro se um papel necessário dessa fatia não puder ser atendido pelo catálogo atual. Plus/Konsō e Jigokuchō merecem fichas próprias, sem classificá-los como decoração pronta ou adicionar silenciosamente dois sistemas à amostra.

Próxima discussão principal: **M09 — Mobs, bosses e encontros**. Grimmjow continua o primeiro caso de linhagem no momento apropriado; listar Zangetsu como asset não altera essa escolha. Nenhum asset, nova IA ou nova mecânica foi criado nesta sessão; as mudanças de runtime vieram da atualização upstream.
