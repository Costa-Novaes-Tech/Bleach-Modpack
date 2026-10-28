# Base de conhecimento — Bleach dentro de Minecraft

> **Direção vigente REV-SP-02 — 22/10/2026:** Spiritual Points compram **skills e transformações**, conforme esclarecimento explícito do usuário, usando Dragon Mine Z como referência para a compra. **Mastery é desenvolvida pela prática e não é comprada.** Requisitos/contexto disponibilizam a aquisição; SP paga a compra/desbloqueio; usar a capacidade depois segue os custos próprios de Energia/Stamina. [Contrato comum em M05/11.2](05-atributos-e-recursos.md#112-rev-sp-02--compra-de-skills-e-transformações-com-spiritual-points). Preços e exceções concretas permanecem nas fichas; a compra não pode ser removida genericamente por interpretação de M05/M06.

| Campo | Valor |
|---|---|
| Estado | Base inicial pesquisada; aprofundamento por módulo |
| Data de consulta | 20/10/2026 |
| Referência original da pesquisa | `main`, `8ace6520e67d27441974b9ad92b443ddb33f9882`, em 20/10/2026 |
| Checkout atualizado | `main`, `bb26be85fc3dac703f67e79e772fca5158eecdef`, conferido/atualizado em 22/10/2026; inventário dirigido no parecer de reconciliação |
| Plataforma do projeto | Minecraft Java Edition 1.20.1 / Forge 47.4.10 / Java 17 |
| Finalidade | Orientar a continuação do game design e a adaptação das skills ao mod |
| Autoridade | Referência de pesquisa; não aprova novas mecânicas nem substitui decisões da equipe |

**Continuidade e conferência documental:** 23/10/2026. Consultas e aprovações mantêm suas datas próprias abaixo. Estados/encaminhamentos nas rodadas são históricos; os registros posteriores de cada módulo governam sua continuidade vigente.

> **Continuidade da rodada anterior — 23/10/2026:** checkpoint de revisão M01–M10 e complementos funcionais concluído no alcance de [12.1 do parecer](00-revisao-cobertura-m01-a-m10-2026-10-23.md#121-conclusão-do-checkpoint-autorizado--23102026). RAC-30/RAC-31A, M06-CS-01A/B/C/D e M09-12C decididos; M10b/9.4.1 explicita as ligações de retorno. M08/M09/M10 continuam em especificação de conteúdo. Próximo M11 com retorno M05; publicação documental autorizada, sem implementação/teste de jogo. Registros abaixo conservam as datas e pendências de cada rodada histórica.

> **Continuidade vigente — 28/10/2026:** por solicitação explícita, foco no [M13](13-campanhas-e-sidequests.md), somente campanha principal. M11 adiado com aprovações até 07A/B preservadas; formação/quests raciais, fillers e demais histórias laterais ficam para depois. Primeiro arco em rascunho: Shinigami Substituto; 01A/02A decididos para execução direta por NPC/painel e criação do confronto; 01B reformulado, sem catálogo/quantidade aprovados. Registros inferiores preservam a cronologia anterior, não indicam perguntas atuais já respondidas.

## 1. Entendimento do projeto

O Bleach Mod acrescenta uma jornada de ação e progressão espiritual a Minecraft Java. O jogador cria um personagem próprio, desenvolve poderes canônicos e participa de campanhas dentro de um mundo que continua permitindo exploração, preparação, sobrevivência, construção e cooperação.

As decisões de produto estão em [M01](01-visao-e-pilares.md), [M02](02-trilhas-de-progressao.md), [M03](03-storyline-macro.md), [M04](04-racas-e-origens.md), [M05](05-atributos-e-recursos.md) e [M06](06-habilidades-e-combate.md). O [mapa modular](00-mapa-modular.md) governa a divisão de responsabilidades.

Contratos que precisam orientar qualquer pesquisa ou skill:

1. O personagem original preserva sua identidade mesmo utilizando poderes do elenco canônico.
2. O catálogo é de poderes canônicos. Repetição do mesmo poder entre personagens é uma adaptação aprovada.
3. A jornada combina campanha principal, campanhas raciais, atividades laterais e mundo aberto.
4. As quatro rotas planejadas são Shinigami, Hollow/Arrancar, Quincy e Fullbringer. A implementação atual não comprova a completude dessas rotas.
5. Origem, natureza espiritual, evolução, poder principal, forma ativa, afiliação e cargo possuem significados diferentes.
6. Morte comum/respawn não muda automaticamente a natureza espiritual do personagem — RAC-06.
7. O mundo possui uma cronologia compartilhada; a participação principal e racial é registrada por personagem — STR-06. Catch-up e regras concretas permanecem para M15.
8. O jogador ou a party pode vencer confrontos canônicos adaptados; isso não transfere a biografia do personagem original — STR-03.
9. Construções e atividades de Minecraft fazem parte da experiência e precisam ser consideradas no desenho de poderes, encontros e campanhas.
10. O GDD descreve o horizonte completo. Cada entrega implementa e valida uma parte coerente, sem transformar todo conteúdo futuro em compromisso imediato.

### Revisões que prevalecem sobre descrições antigas

Ler somente o começo dos documentos ou os handoffs antigos pode recuperar modelos superados.

| Assunto | Contrato mais recente localizado | Consequência para a continuidade |
|---|---|---|
| Atributos | M05: Potência, Vitalidade, Resistência, Reserva e Controle | Cinco atributos já são a direção conceitual decidida. As sete categorias do código são uma divergência de implementação a conciliar. |
| Crescimento dos atributos | M05, REV-M02-01 | Spiritual Points e prática válida alimentam um progresso integrado; o modelo anterior de rank comprado e XP prática separados foi superado. |
| Spiritual Points | REV-SP-02, esclarecimento do usuário em 22/10/2026, M05/11.2 | Compra de skills e transformações, além de atributos; contexto/requisitos abrem acesso. Mastery pela prática, sem compra. Débito de aquisição separado de Energia/Stamina por uso; catálogo/preços/exceções pendentes. |
| Disciplina e técnica | M06-07B.1 e consolidação final | Proficiência representa formação geral; Mastery representa domínio individual de uma capacidade. Não criar um segundo rank obrigatório da disciplina. |
| Aquisição | M06-03C/03D refinados e M06-07 | Formação, Proficiência, desenvolvimento autônomo e mestres podem participar; não exigir um NPC para cada técnica. |
| Apresentação da progressão | M06-07L e seção de decisões superadas | Repertório/lista por disciplina; skill tree foi descartada como estrutura principal. |
| Limite de desenvolvimento | M05-P03 | A build expressa o que o jogador desenvolve primeiro. Isso não autoriza acumular todos os poderes canônicos principais. |
| Ponto de continuidade | M10 EM ESPECIFICAÇÃO; 01A/B decididos | 02A/B propostos sobre rotas/travessia, duas respostas pendentes. M09 geral especificado conceitualmente, catálogo em especificação; M08 e demais módulos mantêm entregas antes das funcionalidades. |

Uma revisão explícita substitui o trecho anterior no seu escopo. Um texto mais novo ou uma implementação diferente, isoladamente, não prova nova aprovação de produto. Conflitos restantes devem ser apresentados com as referências de ambos os lados.

A tabela reflete a continuidade vigente após o fechamento de M08. Os registros anteriores conservam o fechamento de M07 e a revisão posterior de criação/continuidade de M04; não reabrem seus perfis aprovados.

## 2. Como relacionar as fontes

Cada tema precisa de três registros:

| Registro | Pergunta | Evidência esperada |
|---|---|---|
| Referência de Bleach | O que a obra realmente apresenta e em qual continuidade? | Capítulo/volume, episódio/cena ou publicação oficial identificável |
| Adaptação de design | O que foi escolhido para funcionar no mod? | ID de decisão, justificativa, limites e estado de aprovação |
| Realização em Minecraft | Como isso interage com o jogo e com o código existente? | Versão, sistema responsável, persistência, rede, recursos, mundo e validação |

Exemplo de raciocínio: a referência de uma liberação informa sua identidade; o mod decide requisitos, custos, alcance e contrajogo; Minecraft exige definir colisão com blocos, alvos elegíveis, autoridade do servidor e comportamento ao morrer ou trocar de dimensão. Esses números e regras não se tornam fatos canônicos.

### Política de cânone herdada de D05

1. Mangá principal como base de mundo e acontecimentos.
2. Anime oficial, especialmente TYBW, como referência de apresentação e expansões, com procedência registrada.
3. Novels licenciadas como complemento, com obra e trecho identificados.
4. Fillers, filmes e material adicional classificados conforme sua origem e a política do projeto.
5. Mecânicas de outros jogos de Bleach não definem automaticamente o funcionamento canônico de um poder.

Uma fonte oficial pode ser promocional ou resumida. Ela só sustenta o que realmente informa. Página de venda ou índice confirma publicação; não comprova que seu conteúdo integral foi lido. Wiki pode ajudar a localizar uma cena, mas uma decisão dependente de lore deve procurar a evidência primária.

## 3. Minecraft: base confirmada e implicações

### Plataforma efetivamente declarada

Conferido em [gradle.properties](../../gradle.properties), [build.gradle](../../build.gradle), [mods.toml](../../src/main/resources/META-INF/mods.toml) e no [wrapper Gradle](../../gradle/wrapper/gradle-wrapper.properties):

| Componente | Declaração local |
|---|---|
| Minecraft | `1.20.1`, mappings oficiais `1.20.1` |
| Forge / ForgeGradle | `47.4.10` / `6.0.54` |
| Java / Gradle | toolchain Java `17` / wrapper `8.8` |
| Better Combat | `1.9.0+1.20.1-forge` |
| PlayerAnimator | `1.0.2-rc1+1.20-forge` |
| Cloth Config | `11.1.136+forge` |
| GeckoLib | artefato Modrinth `aC5KMoNg`; faixa exigida `[4.8.4,4.9)` |
| Photon / LDLib | perfil opcional; faixas declaradas `[1.1.17,1.2)` e `[1.0.42,1.1)` |
| Protocolo Bleach | `2.28` em [Reference.java](../../src/main/java/com/bleachmod/Reference.java) |

Faixas aceitas em metadados não constituem prova de compatibilidade em todas as versões incluídas. A pesquisa técnica deve começar pelo artefato e pelo loader utilizados. A recomendação geral de uma documentação para instalar a versão mais recente não autoriza migrar este projeto.

Exemplo concreto: Item Stack Components foram introduzidos em Minecraft 1.20.5. Não importar esse formato para comandos/itens de 1.20.1 por seguir um tutorial recente. Fonte: [notas oficiais de 1.20.5](https://www.minecraft.net/pt-pt/article/minecraft-java-edition-1-20-5).

### Fundamentos e consequências de design

As implicações abaixo são pontos de análise do mod; não aprovações de funcionalidades novas.

| Base confirmada | Consequência a tratar no game design |
|---|---|
| Minecraft permite explorar, construir, coletar, fabricar e sobreviver; Survival administra vida e fome. [MC-01] | Explicar como progressão espiritual convive com comida, abrigo, mineração, equipamento e construção. |
| O servidor lógico executa as regras também no singleplayer, dentro do cliente físico. [MC-03] | Um resultado no cliente local não comprova servidor dedicado nem sincronização com observadores. |
| Classes de renderização pertencem ao cliente físico. [MC-03] | Uma capacidade pode ter efeitos visuais locais sem fazer sua autorização depender desses efeitos. |
| A lógica usa ticks; a referência normal é 20 por segundo, podendo atrasar sob carga. [MC-08] | Duração em ticks precisa indicar segundos nominais. FPS, quadros de animação e ticks são medidas diferentes. |
| Pacotes do cliente precisam de validação; acessar posições sem verificar chunks pode carregar/gerar terreno indevidamente. [MC-04] | Definir alcance, elegibilidade e destino seguro no servidor. Áreas e mobilidade não podem presumir todo o mapa carregado. |
| Capability não sincroniza automaticamente nem preserva dados na morte sem tratamento explícito. [MC-05] | Para cada progresso, declarar o que persiste, o que é limpo e o que observadores recebem. |
| `SavedData` pode guardar estado associado ao nível; alterações precisam ser marcadas para salvar. [MC-06] | A era compartilhada e o progresso pessoal precisam de donos explícitos; a opção de armazenamento não aprova a política de avanço. |
| Datapacks permitem modificar dados de servidor, como receitas, loot, tags e estruturas. [MC-07] | Separar definições de conteúdo, configuração administrativa e estado persistido. JSON próprio de quest não vira datapack automaticamente. |

### Particularidades já existentes neste mod

- Soul Society e Hueco Mundo são declaradas pelo preset `history_mode`; [BleachDimensions.java](../../src/main/java/com/bleachmod/server/world/BleachDimensions.java) distingue esse mundo de um mundo comum.
- O [preset](../../src/main/resources/data/bleachmod/worldgen/world_preset/history_mode.json) mantém Nether e End. O Mundo Humano usa `minecraft:overworld`; os dois mundos espirituais usam IDs próprios.
- Há mapas pré-construídos com manifesto, instalação e spawns. O [README dos mapas](../../history-maps/README.md) descreve a cópia para mundos novos e o manifesto por save. Não equivale a geração procedural completa do universo de Bleach.
- [QuestRegistry.java](../../src/main/java/com/bleachmod/common/quest/QuestRegistry.java) carrega JSONs sob `<save>/bleachmod/`, separa snapshots lógicos e valida definições. Atualmente limita o conjunto a 128 quests; o horizonte de campanhas exigirá revisar esse limite antes de prometer volume de conteúdo.
- O projeto usa capability do jogador e eventos de ciclo de vida. [CapabilityEvents.java](../../src/main/java/com/bleachmod/server/events/CapabilityEvents.java) contém tratamento de clone, login, mudança de dimensão e observação. Isso localiza a infraestrutura; não atesta todos os cenários de persistência.
- Better Combat integra ataques físicos selecionados; serviços Bleach mantêm técnicas e progressão. O [ciclo 51](../poderes/51-roadmap-better-combat-ferramentas-refatoracao-2026-10-16.md) documenta a necessidade de evitar dano/custo duplicados e já registra o uso anterior de `game-design-architect`.

### Perguntas obrigatórias ao adaptar uma capacidade

1. Como interage com blocos colocados, mineração, construção e arenas preparadas pelo jogador?
2. Causa dano a terreno ou somente efeito visual? Qual regra já existe e qual decisão ainda falta?
3. Como interage com armadura, encantamentos, escudos, efeitos, projéteis e dano vanilla?
4. Uma farm, redstone, mob preso, pet ou jogador aliado pode gerar ganho indevido?
5. O poder depende de item, vínculo do personagem ou ambos? Troca, drop, baú, morte e perda do item permitem contornar requisitos?
6. Como se comporta em terreno irregular, água, corredores, limites de mundo e chunks descarregados?
7. O mundo continua acontecendo durante menu, diálogo e cena? Como isso afeta os demais jogadores?
8. Quais regras valem para Survival, Creative, Spectator e ferramentas administrativas?
9. O que ocorre ao desconectar, morrer, trocar de dimensão, recarregar definições ou reiniciar o servidor?
10. Qual parte é progresso pessoal, estado da party, encontro ou estado do mundo?

Essas perguntas devem ser respondidas no módulo responsável. Não pressupõem imunidade, pausa global, proteção territorial, keepInventory ou novo sistema de instâncias.

## 4. Bleach: fatos conferidos nesta pesquisa

Esta é uma base seletiva para o mod, sem transcrição de episódios, mangá ou guias.

| Tema | Informação sustentada pela fonte oficial | Uso no projeto |
|---|---|---|
| Shinigami | Conduzem almas e purificam Hollows; sua formação inclui espada, combate corporal, movimentação e Kidō. [BL-03] | Preservar funções de mundo e disciplinas distintas. |
| Arrancar | São Hollows com poderes Shinigami; Resurrección libera a natureza/poder selado na arma. [BL-03] | Não tratar como cargo nem como Bankai renomeada. |
| Quincy | Utilizam Reishi em armas e técnicas; há capacidades ofensivas, defensivas e de movimento. [BL-03] | A identidade racial ultrapassa o papel de arqueiro. |
| Kidō | Hadō e Bakudō possuem funções diferentes; execução/encantamento participa do resultado. [BL-03] | Preservar propósito e forma de execução ao adaptar. |
| Mundos | A apresentação da guerra relaciona Mundo Humano, Soul Society, Hollows e Wandenreich. [BL-02] | Região, instituição e estado da campanha precisam de registros próprios. |
| Cargo e capacidade | Renji é tenente com Bankai; Ikkaku permanece terceiro assento apesar de dominar Bankai. [BL-04] | Cargo, forma e prontidão de combate não são uma única escala. |
| Fullbringer | O elenco oficial japonês descreve poderes e vetores diferentes, incluindo Ginjō, Tsukishima, Yukio e Riruka. [BL-05] | Cada poder precisa de referência própria; não impor um único vetor genérico. |

As descrições japonesas foram interpretadas para esta síntese. Grafia de nomes, comandos e condições decisivas deve ser conferida na publicação/cena pertinente antes de fechar uma ficha de conteúdo.

### Atualidade editorial e audiovisual

- O catálogo oficial da VIZ apresenta a série principal até o volume 74, marcado como final. O índice serve para localizar volumes; não representa leitura integral nesta pesquisa. [BL-01]
- Em 20/10/2026, a página japonesa de exibição lista o episódio 49 para 19/10 às 24h e o 50 para 26/10 às 24h, no horário de programação japonês. Portanto, ainda há exibição futura em relação à data desta base. [BL-06]
- A página de notícias e o glossário são mutáveis e podem mostrar conteúdo promocional. Um anúncio, título ou imagem não comprova toda a mecânica da cena. [BL-03, BL-07]
- O índice consultado da VIZ identifica o especial de 09/08/2021 como one-shot. Isso não fundamenta uma campanha nova completa. [BL-08]
- A apresentação de *Can't Fear Your Own World* pela VIZ localiza a novel no pós-guerra e identifica sua autoria. É uma referência complementar; o artigo não substitui a leitura do trecho necessário. [BL-09]

Cada referência de TYBW usada no futuro deve registrar episódio, cena e estado de publicação na data da consulta. Não incorporar automaticamente todas as entradas recentes do site à continuidade-base do mangá.

## 5. Vocabulário operacional do projeto

Este quadro resume contratos locais, sobretudo M05/M06. Não afirma que Bleach tenha uma ficha universal com esses campos numéricos.

| Termo | Uso no design vigente |
|---|---|
| Potência / Vitalidade / Resistência / Reserva / Controle | Cinco atributos fundamentais |
| Vida / Stamina / Energia Espiritual | Recursos universais de combate |
| Reiryoku | Representado no gameplay pela energia espiritual disponível; não cria outra barra |
| Reiatsu | Manifestação/pressão exterior do poder; não é sinônimo de barra consumível ou Battle Power |
| Reishi | Propriedade/matéria espiritual ambiental; não implica coleta manual constante |
| Spiritual Points | Principalmente desenvolvimento de atributos; não compram Mastery |
| Proficiência | Desenvolvimento geral de uma disciplina, adquirido organicamente |
| Técnica | Capacidade específica adquirida de maneira apropriada |
| Mastery | Domínio individual da capacidade/forma; não moeda |
| Battle Power | Resumo informativo, sem autorizar conteúdo ou determinar sozinho o vencedor |
| Postura | Sistema de defesa/Guard Break, separado de Stamina |
| Dodge / mobilidade espiritual | Esquiva fundamental e técnica racial de deslocamento são capacidades diferentes |

O código ainda pode utilizar nomes legados como `reiatsu` e `trainingPoints`. Registrar o significado de cada campo antes de propor renomeação ou migração.

## 6. Pesquisa necessária antes de fechar os próximos módulos

| Módulo/bloco | Pesquisa de Bleach a aprofundar | Verificação de Minecraft/mod correspondente |
|---|---|---|
| M06-08 — Hollow/Arrancar | Anatomia, capacidades gerais, movimento, evolução e exceções por personagem | Combate corporal, hitboxes, tamanho, colisão, alvo, terreno e aprendizado |
| M06-09 — Quincy | Formação de armas, Reishi, Blut, mobilidade, Schrift e condições específicas | Projéteis, munição/recurso, dano, alcance e compatibilidade com equipamentos |
| M06-10 — Fullbringer | Manipulação da matéria, vetores, mobilidade e limitações por poder | Blocos mutáveis, corpo/equipamento, itens vinculados e exploração de construções |
| M06-11 / M07 | Compatibilidade entre repertórios, naturezas e liberações | Ordem de estados, cancelamento, persistência e transição segura |
| M08 | Funções, instituições, relações e disponibilidade do elenco em cada era | NPC permanente, representação de encontro, serviços e acesso |
| M09 | Comportamentos e fases reconhecíveis dos inimigos | IA, navegação em mundo construído, spawn, loot e propriedade do encontro |
| M10 | Mundos, travessias e geografia | Preset História, mapas existentes, geração, chunks, spawns e retorno |
| M11 | Armas, instrumentos e materiais pertinentes | Inventário, crafting, troca, perda, encantamento e fontes de recursos |
| M12–13 | Eventos e disponibilidade dos personagens por era | Estados de quest, recompensas únicas, retomada e repetição |
| M14–16 | Clareza dos conceitos e identidade visual/auditiva | Controles de Minecraft, feedback, acessibilidade, servidor dedicado e critérios de aceite |

Essa agenda não aprova novos poderes, técnicas ou capítulos. Valores finais permanecem sujeitos à especificação e validação nos módulos correspondentes.

## 7. Registro de fontes externas

Todas consultadas em 20/10/2026. “Consultada” descreve a página acessível, não a leitura de obras completas ou de conteúdo restrito.

| ID | Fonte | Escopo conferido |
|---|---|---|
| MC-01 | [Minecraft — What is Minecraft?](https://www.minecraft.net/en-us/article/what-minecraft) | Sandbox, blocos, exploração, Survival, vida/fome e Creative |
| MC-02 | [Minecraft — Java Edition 1.20.1](https://www.minecraft.net/sv-se/article/minecraft--java-edition-1-20-1) | Release de 12/06/2023; versão histórica utilizada pelo projeto |
| MC-03 | [Forge 1.20.x — Sides](https://docs.minecraftforge.net/en/1.20.x/concepts/sides/) | Cliente/servidor lógico e físico; singleplayer e dedicado |
| MC-04 | [Forge 1.20.x — SimpleImpl](https://docs.minecraftforge.net/en/1.20.x/networking/simpleimpl/) | Validação, execução na thread principal e cuidado com chunks |
| MC-05 | [Forge 1.20.1 — Capabilities](https://docs.minecraftforge.net/en/1.20.1/datastorage/capabilities/) | Persistência, sincronização e clone do jogador |
| MC-06 | [Forge 1.20.x — Saved Data](https://docs.minecraftforge.net/en/1.20.x/datastorage/saveddata/) | Estado por nível, NBT e marcação de alterações |
| MC-07 | [Forge 1.20.x — Datapacks](https://docs.minecraftforge.net/en/1.20.x/resources/server/) | Dados modificáveis de servidor e diretório `data` |
| MC-08 | [Forge 1.20.x — Block Entities](https://docs.minecraftforge.net/en/1.20.x/blockentities/) | Ticking e custo de atualização/sincronização |
| MC-09 | [Forge 1.20.x — Getting Started](https://docs.minecraftforge.net/en/1.20.x/gettingstarted/) | Java 17 e distinção entre execução local e dedicado |
| MC-10 | [Minecraft — Java Edition 1.20.5](https://www.minecraft.net/pt-pt/article/minecraft-java-edition-1-20-5) | Mudança posterior para Item Stack Components; comparação de versões |
| MC-11 | [Minecraft — Java Edition 1.19.4](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-19-4) | Seção Damage Types: registro extensível por dados e interpretação por tags; referência histórica anterior ao alvo 1.20.1, sem fórmula/compatibilidade integral do mod |
| BL-01 | [VIZ — Catálogo Bleach](https://www.viz.com/manga-books/manga/bleach/all) | Localizador oficial de volumes |
| BL-02 | [BLEACH TYBW — Story](https://bleach-anime.com/en/story/story.html) | Premissa oficial da guerra |
| BL-03 | [BLEACH TYBW — Key Word](https://bleach-anime.com/keyword/) | Entradas oficiais selecionadas; fonte mutável e japonesa |
| BL-04 | [BLEACH TYBW — Characters EN](https://bleach-anime.com/en/character/) | Patentes, vínculos e capacidades nas fichas consultadas |
| BL-05 | [BLEACH TYBW — Characters JP](https://bleach-anime.com/character/) | Elenco e poderes, incluindo Fullbringers |
| BL-06 | [BLEACH TYBW — On Air](https://bleach-anime.com/onair/) | Calendário japonês vigente na consulta |
| BL-07 | [BLEACH TYBW — News](https://bleach-anime.com/news/) | Localizador de anúncios recentes |
| BL-08 | [VIZ — Special One-Shot](https://www.viz.com/shonenjump/chapters/bleach-special-one-shot) | Identificação e data do especial; conteúdo marcado como restrito |
| BL-09 | [VIZ — New Bleach Novels](https://www.viz.com/blog/posts/new-bleach-novels) | Apresentação editorial de CFYOW |
| BL-10 | [TV Tokyo — Elenco de Hueco Mundo](https://www.tv-tokyo.co.jp/contents/bleach_test2/chara/index05.html) | Arquivo oficial do anime; ficha de Aaroniero distingue origem Gillian, posto Espada e poder individual |
| BL-11 | [Pierrot — Glossário Bleach, grupo ha](https://pierrot.jp/title/bleach/keyword_ha.html) | Entradas Hollow, Bala, Pesquisa, Hirenkyaku e Fullbring, em consultas identificadas nas rodadas; arquivo relativo à época de exibição |
| BL-12 | [Pierrot — Glossário Bleach, grupo sa](https://pierrot.jp/title/bleach/keyword_sa.html) | Cero na rodada Hollow; Sprenger/Seele Schneider na rodada de formação Quincy. Entradas relativas à época de exibição, sem custos/receitas do mod |
| BL-13 | [Pierrot — Retrospectiva do arco inicial](https://pierrot.jp/title/bleach/playback_01.html) | Trecho do Menos disparando Cero contra Ichigo/Uryū; resumo, não episódio integral |
| BL-14 | [Pierrot — Espada](https://pierrot.jp/title/bleach/chara_06.html) | Ficha de Ulquiorra/Murciélago inclui regeneração rápida. Consulta dirigida de 21/10/2026 a Tiburón/Gamuza: diversidade de corpo e armamento; não regras de inventário ou colisão do mod. Arquivo relativo à época de exibição |
| BL-15 | [Brave Souls — Ulquiorra](https://www.bleach-bravesouls.com/character/ulquiorra.html) | Descrição biográfica licenciada de regeneração e limites; não importar mecânicas do jogo |

| BL-16 | [Pierrot — Glossário Bleach, grupo ka](https://pierrot.jp/title/bleach/keyword_ka.html) | Quincy Cross e Ginrei Kojaku na rodada J/K/L; arquivo da época de exibição, sem comprovar exigência universal ou regras de equipamento do mod. Reconsulta em 21/10: entrada Letzt Stil, luva/perda/recuperação descrita; não importar treino literal ou procedimento como regra universal do mod. |
| BL-17 | [Pierrot — Retrospectiva do arco do desaparecimento dos poderes](https://pierrot.jp/title/bleach/playback_16.html) | Episódios 343–366, resumos de treinamento e desenvolvimento de Fullbring; não episódios integrais nem currículo universal |
| BL-18 | [TV Tokyo — Sinopses dos episódios 342–354](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes29/index.html) | Episódios 346/352: alma da matéria e movimento rápido desenvolvido em treino; não especifica controles, salto, custos ou meios de Bringer Light |

| BL-19 | [Pierrot — Glossário Bleach, grupo ra](https://pierrot.jp/title/bleach/keyword_ra.html) | Entrada Reiraku (`霊絡`), consulta de 20/10: manifestação visual particular, sem percepção universal. Reconsulta em 21/10 de `帰刃 / レスレクシオン`: liberação das capacidades Hollow no corpo, sem currículo ou Mastery numérica. Arquivo relativo à época de exibição |

| BL-20 | [TV Tokyo — Sinopses dos episódios 121–131](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes11/index.html) | Episódio 121, 11/04/2007: restrição de poder no Mundo Humano e liberação autorizada; não demonstra custo da Supressão voluntária ou multiplicador universal |
| BL-21 | [BS TV Tokyo — TYBW episódio 21](https://www.tv-tokyo.co.jp/broad_bstvtokyo4k/program/detail/202402/23289_202402252330.html) | THE HEADLESS STAR, página da exibição de 25/02/2024: mudança de presença e assinatura familiar; não estreia original, alcance ou fórmula de detecção |

| BL-22 | [TV Tokyo — Sinopses dos episódios 303–315](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes26/index.html) | Episódio 308, 08/02/2011: opressão de Aizen sobre humanos; apenas esse trecho, sem conceder o desmaio, custos ou alcance à Liberação básica |

| BL-23 | [Pierrot — Retrospectiva do resgate na Soul Society](https://pierrot.jp/title/bleach/playback_03.html) | Consulta 21/10/2026; resumo dos episódios 42–63, trechos de treinamento de Ichigo com Zangetsu e conquista de Renji. Não episódios integrais nem método/prazo universal de aprendizado de Bankai. Reconsulta dirigida pelo índice em 21/10: trecho Uryū/Mayuri e perda do poder, sem currículo universal. |

| BL-24 | [TYBW — Sinopse oficial do episódio 08](https://bleach-anime.com/story/?id=8) | Consulta 21/10/2026; THE SHOOTING STAR PROJECT 〔ZERO MIX〕, explicação de Mayuri após a quebra de Tensa Zangetsu. Sinopse, não episódio integral ou método universal de recuperação |

| BL-25 | [Pierrot — Glossário ma](https://pierrot.jp/title/bleach/keyword_ma.html) | Consulta 21/10/2026; entradas Menos/Grande Hollow: agregação, consciência dominante e regressão. Arquivo do anime, sem capítulo integral, contagem de jogo ou condições universais atualizadas |
| BL-26 | [Pierrot — Glossário a](https://pierrot.jp/title/bleach/keyword_a.html) | Consulta 21/10/2026; entradas Adjuchas/Vasto Lorde e Arrancar: classes, máscara, Hōgyoku, núcleo selado na arma e generalização histórica sobre aparência após a mudança. Não importar escala literal, multiplicador ou raridade narrativa como chance de desbloqueio |
| BL-27 | [Shonen Jump — Apresentação do arco Arrancar](https://www.shonenjump.com/xmedia/bleach/story/index4.html) | Consulta 21/10/2026; verbete editorial oficial de Arrancar: retirada da máscara visando capacidades de Shinigami, arma e remanescentes da máscara. Não capítulo integral nem procedimento jogável; numeração refere-se ao grupo de Aizen |
| BL-28 | [TYBW — Entrevista com Kōichi Yamadera, voz de Quilge](https://bleach-anime.com/special/interview/04.html) | Consulta 21/10/2026; trecho da atuação distingue Vollständig, Sklaverei e The Jail em Quilge. Entrevista oficial, não cena integral nem currículo universal |



Documentação `1.20.x` pode cobrir diferenças entre patches. Conferir assinatura e comportamento no artefato 1.20.1 antes de aplicar um exemplo. Guias Bedrock, NeoForge, Fabric e versões posteriores exigem identificação explícita e não substituem a referência Forge deste projeto.

| BL-29 | [Shonen Jump — Apresentação do arco do desaparecimento dos poderes](https://www.shonenjump.com/xmedia/bleach/story/index5.html) | Consulta direta 22/10/2026: texto editorial e verbete Fullbring, inclusive caso corporal de Chad. Agora legível; a tentativa anterior retornou essencialmente imagens. Não capítulo integral ou regra universal de estágios |
| BL-30 | [TV Tokyo — Sinopses 355–366](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes30/index.html) | Consulta direta 22/10/2026: episódio 358, exibido 31/01/2012, situa completude do Fullbring de Ichigo. Somente trecho pertinente; sem episódio integral ou currículo universal |
| BL-31 | [Brave Souls — Ginjō](https://www.bleach-bravesouls.com/en/character/ginjo.html), [Chad](https://www.bleach-bravesouls.com/en/character/sado.html), [Yukio](https://www.bleach-bravesouls.com/en/character/yukio.html), [Tsukishima](https://www.bleach-bravesouls.com/en/character/tsukishima.html) | Fichas oficiais licenciadas, reconsultadas diretamente em 22/10/2026 para identidade/vetores. Não importar economia, habilidades jogáveis ou progressão desse jogo |

## 8. Uso e atualização desta base

Antes de cada bloco: ler integralmente os documentos relevantes; localizar revisões que superam decisões antigas; verificar a fonte primária necessária; registrar como a referência será adaptada; conferir as interfaces existentes no mod; então apresentar alternativas e recomendação para as decisões ainda abertas.

Para cada afirmação nova relevante, registrar: assunto, fonte exata, capítulo/episódio/seção quando disponível, data, continuidade/versão, classificação, consequência para o módulo e pendências de confirmação. Se a evidência não estiver acessível, manter o fato pendente e avançar apenas no que não depende dele.

Atualizar quando uma versão/dependência do projeto mudar, quando uma publicação oficial alterar um ponto utilizado ou quando uma decisão aprovada revisar um contrato. A data desta página não significa monitoramento automático nem dispensa nova consulta de informações mutáveis.

### Cobertura e limites desta rodada

- Conferidos integralmente M01–M03; nos demais documentos, leitura dirigida dos contratos, revisões e conclusões pertinentes a esta preparação.
- Conferidos configuração de build, preset/manifesto de História e pontos do código responsáveis por dimensões, quests e persistência.
- Pesquisadas fontes oficiais de Minecraft/Forge e publicações/páginas oficiais de Bleach listadas acima.
- Capítulos completos, novels e todos os episódios não foram lidos/assistidos nesta rodada. Não declarar conhecimento exaustivo do cânone nem auditoria integral dos módulos 04–06.
- As referências individuais Gillian/Adjuchas/Vasto Lorde, exceções raciais e condições detalhadas de Fullbring devem ser aprofundadas antes das respectivas decisões. Cabeçalhos ou descrições antigas do projeto não bastam para validar essas regras.
- Nenhum código de gameplay, dependência, versão ou decisão de produto foi alterado nesta preparação. Não houve nova execução de build ou teste em jogo.

Aplicação preparada em 20/10/2026: [skill bleach-game-design](../../.agents/skills/bleach-game-design/SKILL.md), com revisão de contratos e coerência narrativa, e [handoff vigente](00-handoff-atual.md). Usar esta base no fechamento do M06, começando por pesquisa específica de Hollow/Arrancar e pelas decisões que continuam abertas. A preparação da skill não aprova essas novas decisões.

## 9. Aprofundamento M06-08 — 20/10/2026

Pesquisa específica e implicações registradas no bloco [M06-08 de Habilidades e Combate](06-habilidades-e-combate.md#m06-08--hollow--arrancar), seção 3. Reconsultados BL-03, MC-03 e MC-04; acrescentada a [apresentação de Ulquiorra pela Bandai Namco](https://bleach-ros.bn-ent.net/character/?chara=ulquiorra-cifer), identificada como fonte de jogo licenciado, sem importar sua economia de combate.

A rodada verificou o resumo oficial de capacidades Arrancar e seus limites. BL-10 acrescenta evidência oficial para o exemplo de Aaroniero, antes apoiado nesta preparação apenas por uma página licenciada cujo texto detalhado não estava acessível. Isso não comprova todos os requisitos de evolução Menos.

Ainda não houve conferência integral das cenas/capítulos de evolução Menos, variantes de emissão, regeneração ou demais exceções. Os três campos e sua Proficiência em M06-08A/B foram aprovados explicitamente pela equipe em 20/10/2026 como adaptação de design, sem implementação nesta rodada.

### Segunda rodada — Corpo e combate físico

BL-11 consultado em 20/10/2026 para fundamentar a diversidade corporal. O texto da página foi lido integralmente; apenas a entrada Hollow sustenta a proposta desta rodada. Seus resumos de outros assuntos não foram importados como contratos do mod.

[M06-08C](06-habilidades-e-combate.md#12-m06-08c--decidido--perfis-corporais-com-diferenças-funcionais-controladas) compara perfis funcionais definidos, corpo único e montagem livre. A equipe aprovou explicitamente os perfis funcionais controlados em 20/10/2026 como adaptação de design. Catálogo, medidas e regras de obtenção/transição continuam abertos. Reconsultado MC-04; conferidos dados de aparência e exemplos locais de alcance, sem executar gameplay.

### Terceira rodada — Desenvolvimento físico

Em 20/10/2026, a equipe aprovou explicitamente o modelo misto de aquisição física de [M06-08D](06-habilidades-e-combate.md#18-m06-08d--decidido--incorporação-conforme-a-complexidade-da-capacidade). O modelo é adaptação de design dos contratos existentes, sem novas técnicas ou afirmações canônicas. A referência corporal BL-11 permanece a leitura da segunda rodada; a tentativa de reconsulta nesta rodada falhou, sem sustentar informação nova.

MC-05 reconsultado para sincronização/cópia de dados de capabilities. Lidos integralmente `MasteryPracticeService.java`, `TechniqueProgression.java` e `SkillsData.java`: localizam prática/unlocks Shinigami e compra legada de níveis, sem demonstrar o modelo físico Hollow. Não houve alteração ou teste de gameplay. O modelo aprovado trata fundamentos incorporados, técnica desenvolvível, aquisição e Mastery, com treino apropriado sem requisito circular ou quest obrigatória para cada melhoria.

### Quarta rodada — Mestres e integração física Arrancar

Em 20/10/2026, a equipe pediu análise de mestres e julgamento próprio de direção criativa. [M06-08E](06-habilidades-e-combate.md#23-m06-08e--diretriz-herdada--mestres-repertórios-e-provas) consolida a mentoria já prevista em RAC-12/RAC-20 e M06-03/M06-07; oferece candidatos de conteúdo explicitamente propostos. [M06-08F](06-habilidades-e-combate.md#24-m06-08f--decidido--alternância-corporal-e-armada-por-estado-de-empunhadura) foi aprovado explicitamente: empunhadura orienta o moveset físico, sem outra Proficiência.

Reconsultados BL-03/04/05/10: as fichas japonesas de Neliel, Grimmjow e Harribel identificam condições e contextos que precisam ser respeitados pela mentoria. Atribuir ensino ao jogador é adaptação candidata; as fichas não demonstram currículo, técnica transferível ou treinamento canônico desses personagens. Não aplicar a ficha atual automaticamente a toda era de M03.

Lidos integralmente o plano histórico `Docs/planejamento/plano-npcs-quests.md`, `QuestNpcEntity.java` e `QuestActionC2S.java`; conferido o default de teste Ulquiorra em `QuestDefaults.java`. A interação e validação de quests existem, mas não demonstram demonstração/sparring de mestre. As tarefas genéricas de teste não são aceite de conteúdo. Nenhuma implementação, treinamento em jogo ou teste novo nesta rodada.

### Reconciliação do histórico externo — Linhagens e evolução

Em 20/10/2026, o usuário forneceu um handoff de outra sessão, lido integralmente e preservado com [análise própria](00-reconciliacao-handoff-hollow-2026-10-20.md). Seus IDs e pendências não substituem automaticamente as decisões atuais. As descrições de jogos de Roblox vieram sem fontes/versões reproduzíveis e permanecem pesquisa histórica relatada, não verificada nesta rodada.

Reconsultados BL-03/10; consultada a [sinopse oficial TV Tokyo do episódio 167](https://www.tv-tokyo.co.jp/anime/bleach_bestbout/episodes/), republicação de 14/08/2024. O resumo descreve passado Adjuchas de Grimmjow, predação e risco de regressão. Não foi assistido o episódio integral nem conferido o capítulo do mangá; a sinopse não valida forma Vasto Lorde de Grimmjow, timer de regressão offline ou método numérico de evolução. BL-03 distingue a nutrição possível pelo Reishi ambiental em Hueco Mundo, sem estabelecer equivalência automática com evolução. BL-10 identifica Glotonería como absorção especial de capacidades, sem generalizá-la para toda predação.

Recomendações novas — trajetórias por linhagem com perfis controlados, preparação de identidade anterior à Arrancarização e tratamento de estabilidade — continuam `PROPOSTAS`. O conflito de acesso com H6 e a rota Arrancar em Gillian estão explicitados; a pesquisa não os decide pela equipe. Nenhuma mudança de código ou teste novo.

**Atualização após resposta do usuário, na mesma data:** a direção foi aprovada em M06-08G, H6 recebeu REV-H6-01 e Grimmjow/Pantera foi escolhido como primeira linhagem. O usuário autorizou fan arts como base visual para formas faltantes; a pesquisa e a escolha de imagens específicas ainda serão feitas, registrando autor/publicação e separando referência oficial, fan art e adaptação do mod. Reconsultadas as duas páginas TV Tokyo acima: sustentam passado Adjuchas e Pantera, sem confirmar uma forma Vasto Lorde específica de Grimmjow. Essa etapa da trajetória do jogador permanece dependente de referência adicional ou adaptação. Modelos finais, capacidades por etapa e condições concretas de estabilidade continuam abertos.

### Rodada dos três sistemas gerais — Emissão, mobilidade e capacidades

O usuário pediu trabalhar os três blocos gerais antes de fazer sua revisão conjunta. Fichas específicas de linhagens e artes continuam adiadas. Na preparação, **M06-08H/I/J eram propostas**. Foram aprovados depois por três respostas explícitas, registradas no documento de combate; não houve aprovação automática por pedir continuidade.

BL-11 reconsultado com sucesso: foram conferidas também as entradas Bala (mais rápida e menos potente que Cero) e Pesquisa (leitura espiritual). BL-12/13 sustentam Cero antes da Arrancarização. BL-03 reconsultado para Hierro/Sonído e a perda geral de regeneração rápida em Arrancars. BL-14/15 sustentam Ulquiorra como exemplo de regeneração especial; a afirmação de exclusividade na ficha licenciada não foi generalizada para todas as obras/continuidades. Foram consultados resumos/entradas, não capítulos ou episódios integrais.

Carga regulável, recorte de acesso a partir de Gillian, Bala/Sonído na formação Arrancar, Hierro passivo, Pesquisa focada ligada à Mastery universal de percepção e contratos de custo são propostas de game design, não afirmações de que o cânone tenha esse sistema. Capítulos de variantes avançadas e todas as exceções individuais continuam fora desta pesquisa. A página Bandai de Ulquiorra foi reconsultada, mas a cura de seu medidor pertence àquele jogo e não sustenta a regeneração de recursos deste mod.

Lidos os contratos de M05 sobre recuperação, Stamina/Energia, Mastery defensiva e percepção, e M06-05/06 sobre execução, interrupção, defesa, Dodge e mobilidade. Na preparação das propostas ainda não havia revisão conjunta final, implementação ou teste de gameplay novo.

**Atualização após respostas, em 20/10/2026:** H/I/J aprovados estruturalmente e revisão conjunta documental registrada em M06-08, seção 33. O confronto com M05 e M06-02/03/05/06/07/08 identificou uma escolha pendente de execução, submetida ao usuário e aprovada em K: cancelar a carga de Cero para bloquear, usar Dodge ou Sonído, respeitando recuperação. A proteção passiva de Hierro continua aplicável enquanto compatível. Não foi feita nova pesquisa canônica nesta revisão de contratos locais, nem execução de gameplay. Fórmulas, parâmetros, alvos e fichas individuais continuam delegados.

### Fechamento geral de Hollow/Arrancar

Em 20/10/2026, relido integralmente M06-08 e registrada a revisão de fechamento na seção 36. Estrutura decidida, com responsabilidades de fichas, estados, aprendizado, corpos grandes, movimento aéreo, travessias, inventário e integração explicitadas. Essa revisão não acrescentou capacidades canônicas, não resolveu pendências por omissão e não declarou implementação. A continuidade avançou para Quincy; fichas de linhagem e artes permanecem adiadas.

## 10. Abertura M06-09 — Quincy — 20/10/2026

Lidos os contratos Quincy de M04, incluindo RAC-09 e Q1–Q11, e os trechos pertinentes de M05/M06. Q11 mantém a descrição histórica de compra de técnicas: o bloco Quincy registra a aplicação das revisões vigentes de M05, REV-M02-02/04/05, e M06-07. Spiritual Points não voltam a ser moeda presumida de toda técnica ou forma.

**Pesquisa:** BL-03 reconsultado nas entradas `滅却師`, `血装`, `聖文字` e `滅却師完聖体`; BL-11 reconsultado em `飛簾脚`. São entradas oficiais do anime, com texto japonês interpretado, não capítulos/episódios integrais. Sustentam repertório diversificado, distinção dos dois usos de Blut, movimento sobre fluxo de Reishi e separação entre formação, poder individual e forma. A síntese e os limites estão em [M06-09](06-habilidades-e-combate.md#m06-09--quincy). Não foi feita pesquisa exaustiva de exceções, ferramentas ou técnicas avançadas, nem importado conteúdo promocional recente como regra de toda era.

**Duas respostas explícitas:** M06-09A aprova Energia por disparo básico, sem consumo contínuo para manter o arco; M06-09B aprova segurar para preparar e soltar para disparar, com preparo curto/limitado e mira mantida no máximo. Custos pessoais por flecha e comando são adaptações do mod. Não foram atribuídos à obra como valores ou controles canônicos. Detalhes candidatos de execução permanecem abertos e não receberam aprovação integral por associação.

Relido `ConfirmCharacterC2S.java`, ainda restrito a criação Shinigami. Não houve mudança de gameplay, dependências ou saves, nem build/teste em jogo. Essa leitura não é busca exaustiva de todo código experimental. Próximo: desenvolvimento do repertório Quincy, sem impor simetria de campos e sem criar antecipadamente Mastery para cada tiro fundamental.

### Formação, fundamentos e domínio — M06-09C

Em 20/10/2026, reconsultados BL-03, entradas Blut/Hirenkyaku/Seele Schneider, e BL-12, entradas Sprenger/Seele Schneider. Fontes oficiais do anime, lidas nas partes indicadas, com interpretação do japonês; não houve leitura integral de capítulos/episódios. A combinação de ferramentas e preparação em Sprenger fundamenta estudar fórmulas como formação específica; não demonstra sozinho todo o catálogo de Gintō nem autoriza dano a terreno. O arquivo antigo descreve a arma de lâmina no recorte de exibição; não universalizar essa exclusividade para toda a obra.

**Proposta originalmente submetida ao usuário:** três campos com Proficiência — armamento espiritual, mobilidade e Gintō — e Mastery para técnicas distintas. Tiro fundamental sem Mastery paralela; Vene/Arterie com Masteries próprias; Reishi/Controle/percepção sem indicadores duplicados. As seções 7–11 de M06-09 registram alternativas, limites e critérios. Na preparação, a organização era proposta; foi aprovada depois por resposta explícita, registrada abaixo. Fontes oficiais sustentam as referências, não o número de indicadores do mod.

Aplicados contratos de aquisição e granularidade de M06-03/M06-07 e mentoria/recursos de M05. Sem novo mestre, catálogo, valores, alteração de código ou teste de gameplay. Fichas de Gintō e ferramentas ainda exigem pesquisa específica.

**Atualização após resposta em 20/10/2026:** M06-09C aprovado — “Três campos + Masteries específicas (recomendado)”. A organização estrutural passou a decidida, preservando conteúdo e parâmetros abertos.

### Reação durante preparação do arco — M06-09D

Em 20/10/2026, resposta explícita “Cancelar a flecha para reagir (recomendado)”: caminhada lenta/mira durante preparação; bloquear, Dodge ou Hirenkyaku descartam flecha/carga, sem recuperação adicional de cancelamento antes de emitir. Requisitos/custos da reação e recuperação após disparar permanecem. São permissões de design do arco básico, não regras atribuídas à obra ou a toda técnica.

Confrontado com M06-05M/R e M06-06I e com K de Cero; a permissão própria do arco não revisa a recuperação de cancelamento de Cero. Sem nova pesquisa canônica nesta revisão de contratos locais, implementação ou teste em jogo. Próximo: consumo, soltura precoce, preparo/efeito, trajetória e falhas antes dos blocos de mobilidade/Blut.

### Preparação complementar do arco — M06-09E/F/G

Em 20/10/2026, preparadas propostas de consumo na emissão, preparo parcial com dano limitado e projétil reto com tempo de viagem. Na preparação, A–D estavam aprovados e E/F/G aguardavam resposta explícita; a atualização abaixo registra as aprovações posteriores. Reconsultado BL-03: trecho de `散霊刻片（ライデンシェルベン）` menciona reunir Reishi e formar arco/flechas. Utilizado somente esse fundamento, sem importar o objeto ou as demais entradas. A fonte não define pagamento pessoal por tiro, curva de dano ou trajetória universal; essas propostas são adaptações do mod. Não foi lido capítulo/episódio integral.

Relidos M06-05 e M06-06I para pagamento individual, recuperação e permissões. A revisão candidata separa emissão de acerto, prevê colisão/terreno e explicita falhas e pendências. Recomendações complementares de reação inválida/ciclo de vida não foram aprovadas por associação. Sem mudança de código, build ou teste de gameplay.

**Atualização após respostas em 20/10/2026:** E aprovado — “Cobrar somente ao emitir (recomendado)”; F aprovado — “Permitir flecha incompleta após mínimo curto (recomendado)”; G aprovado — “Linha reta com tempo de viagem (recomendado)”. Registrados escopos e alternativas nas seções 15–17. A revisão documental preservou A–D e separou as duas recomendações complementares ainda abertas. Sem nova pesquisa canônica, implementação ou teste de gameplay nesta atualização.

### Falhas e recuperação do arco — preparação M06-09H/I

Em 20/10/2026, preparados H/I como propostas, preservando A–G. H recomenda preservar preparo ainda válido se uma reação falhar e limpar carga quando perder arma/estado, morrer, sair ou trocar de dimensão. I recomenda permitir movimento/reação após emissão sem apagar recuperação ofensiva: novo ataque aguarda tanto o tempo do tiro quanto a ação atual permitir. Não foram aprovados por associação.

Revisão dos contratos locais M06-05M/R/O, M06-06A/B/E/F/G/I e M06-09D–G. São propostas de execução específicas do mod; sem nova pesquisa canônica ou consulta de APIs. Encaminhadas manifestação/foco, inventário, meios, alvos e ciclo de projéteis aos responsáveis, sem decidir custo/item por omissão. Sem mudança de código, build ou teste de gameplay.

**Atualização após respostas em 20/10/2026:** H aprovado — “Manter preparo válido se a reação falhar (recomendado)”; I aprovado — “Permitir reação mantendo recuperação ofensiva (recomendado)”. Registrados escopos, alternativas não escolhidas e síntese do ciclo nas seções 18–19. A revisão documental preserva A–G e restringe a permissão após emissão ao tiro básico. Respostas resolvem as recomendações complementares da seção 16; manifestação/foco e integrações continuam pendentes. Sem nova pesquisa canônica, implementação, build ou teste de gameplay nesta atualização.

### Manifestação e foco — preparação M06-09J/K/L

Em 20/10/2026, consultado [Pierrot — Glossário ka](https://pierrot.jp/title/bleach/keyword_ka.html), entrada `滅却十字 / クインシー・クロス`: identifica uma cruz especial Quincy. A entrada `銀嶺弧雀` identifica arma específica de Uryū. A página explicita o recorte da época de exibição; os resumos não comprovam exigência universal de foco, slot, custo, vínculo ou uso de mãos. Não houve leitura integral de capítulo/episódio. Nenhuma mecânica de item foi atribuída ao cânone sem essa ressalva.

Na preparação, J/K/L eram propostas locais para o arco básico: foco físico equipado; formar/guardar sem custo adicional, com visual possível sem Energia para tiro; manifestação manual e ocupação de duas mãos. Preservam A–I e M06-01B–F: modo manual, hotbar de técnicas e inventário real. Item não transfere aprendizado; slot/receita/drop/reposição permanecem para os módulos responsáveis. Naquele momento aguardavam resposta explícita; atualização abaixo registra as escolhas posteriores. Sem alteração de código, build ou teste de gameplay.

**Atualização após respostas em 20/10/2026:** J aprovado — “Foco físico equipado (recomendado)”; K aprovado — “Sem custo adicional de manifestação (recomendado)”; L aprovado — “Manifestação manual com duas mãos (recomendado)”. Escopos registrados nas seções 20–21, com alternativas não escolhidas preservadas. A seção 22 consolida o arco estruturalmente e mantém os bloqueadores de ficha com seus responsáveis. A revisão documental não identificou conflito que exija rever A–I; próximo bloco Hirenkyaku, depois Blut. Não houve nova pesquisa canônica, implementação, build ou teste de gameplay nesta atualização.


### Hirenkyaku — preparação M06-09M/N/O

Em 20/10/2026, reconsultadas BL-11, entrada `飛簾脚`, e BL-03, entrada `飛廉脚`: movimento rápido sobre fluxo de Reishi nos pés, classificado como arte avançada de movimentação. São resumos oficiais do anime, interpretados do japonês; não houve leitura de capítulo/episódio integral. O exemplo de dispositivo de Mayuri não foi generalizado como equipamento do jogador. As fontes não decidem comandos, consumo, duração ou invulnerabilidade.

As seções 23–27 de M06-09 comparam impulso curto, deslocamento mantido e destino pela mira. **Na preparação, M/N/O eram PROPOSTOS:** aprendizado próprio após os fundamentos; impulso curto direcional com cobrança de Stamina no início; parar para atacar/defender, com interrupção voluntária por ação válida e sem recuperação adicional apenas pela parada. A recomendação é de design, sem alegar comprovação em jogo. Preservados M05 e A–L, inclusive perda de carga, recuperação ofensiva e foco restrito ao arco. Ar/verticalidade/quedas/água e parâmetros permanecem explicitamente delegados para fechamento posterior. Sem mudança de código ou testes de gameplay; naquele momento as respostas ainda estavam pendentes.


**Atualização após respostas em 20/10/2026:** M aprovado — “Aprendizado próprio de Hirenkyaku (recomendado)”; N aprovado — “Impulso curto direcional (recomendado)”; O aprovado — “Parar para agir, permitindo interrupção (recomendado)”. As seções 24–27 registram escopo, alternativas não escolhidas e revisão documental com A–L. Não foi identificado conflito que exija rever as decisões anteriores. Ar, verticalidade, desníveis, quedas e água continuam para o próximo bloco; a técnica não está totalmente especificada por essa aprovação. Sem nova consulta canônica nesta atualização, implementação ou teste em jogo.


### Rodada conjunta — Hirenkyaku em meios e Blut — 20/10/2026

Por pedido do usuário, as partes restantes de Hirenkyaku e a proposta de Blut foram desenvolvidas na mesma rodada. Reconsultados BL-11 (`飛簾脚`) e BL-03 (`血装`, com a referência racial `滅却師` também localizada na busca oficial). O resumo de Blut sustenta reforço corporal ofensivo/defensivo, exclusividade simultânea e possibilidade de aprender por treino. Não define teclas, manutenção, transição ou todos os tipos de dano; não foi conferido capítulo/episódio integral. Resultados secundários da busca não foram utilizados como evidência de regras canônicas.

**Na preparação, M06-09P/Q/R eram PROPOSTOS**, seções 28–32: impulso horizontal em chão/ar/água preservando gravidade e riscos; Blut aprendido com seleção manual e Energia de manutenção; Vene em dano elegível e Arterie em componentes corporais, sem bônus geral de flechas/Gintō. Movimento aquático/aéreo limitado, cobrança contínua e recorte de efeitos são adaptações candidatas. A fonte resumida não demonstra uma proibição canônica universal de reforçar projéteis; a proposta evita extrapolar o reforço corporal a toda ofensiva. M05 e A–O foram preservados. Números, ordem de dano e integração permanecem com responsáveis. Na preparação, as propostas aguardavam respostas; a aprovação posterior está registrada abaixo. Sem alteração de runtime ou testes em jogo.


**Atualização após respostas em 20/10/2026:** P aprovado — “Horizontal no chão, ar e água (recomendado)”; Q aprovado — “Seleção manual com manutenção de Energia (recomendado)”; R aprovado — “Arterie em golpes de força corporal (recomendado)”. A seção 32 revisa a integração com A–O e a seção 33 consolida Hirenkyaku/Blut estruturalmente, mantendo números, tabela de dano e implementação delegados. Preservada a distinção entre referência canônica e adaptação aprovada. Próximo: repertórios/ferramentas Quincy e Gintō; sem nova pesquisa externa, alteração de código ou testes em jogo nesta atualização.


### Gintō registrado e revisão geral Quincy — 20/10/2026

As propostas S/T/U da rodada anterior foram aprovadas por respostas explícitas, mas não haviam sido gravadas por falta de espaço. Depois da liberação informada pelo usuário, as seções 34–38 de M06-09 recuperam respostas e contratos: “Tubos recarregáveis com conteúdo preparado (recomendado)”; “Guardar arco manualmente para Gintō (recomendado)”; “Execução e consumo conforme cada fórmula (recomendado)”. A pesquisa da preparação utilizou BL-12/Sprenger e BL-16/Quincy Zeichen, sem leitura integral de capítulos, episódios ou guia MASKED. Recarga, reuso e controles permanecem adaptações do mod.

A seção 39 registra revisão documental de A–U com os contratos de origem, recursos, aprendizagem, execução e mundo Minecraft. Não foi identificado conflito que exija revogar escolhas. Habilidades com conteúdo preparado, manutenção de Blut, foco do arco e deslocamento com Stamina conservam requisitos distintos. Estoque/recarga de Gintō, função corporal de Arterie, ritmo de Blut/arco e transições/preparações são condições de design a observar na especificação e validação futura, sem ensaio de gameplay nesta rodada.

**Conclusão:** M06-09 decidido em nível estrutural, com fichas, catálogo, ensino, números e integração delegados. M06 permanece em especificação; próximo M06-10 — Fullbringer. A revisão não trouxe novos fatos canônicos, consulta de APIs ou auditoria exaustiva de código; não houve mudança de runtime, build, dependências, saves ou execução de testes em jogo.


## 11. Abertura M06-10 — Fullbringer — 20/10/2026

Lida integralmente a aplicação Fullbringer de M04, F1–F13, e os contratos relevantes de M05/M06 sobre recursos, aquisição, práticas, granularidade, Proficiência, Mastery e poder principal. F5/F6 e seus diagramas devem ser lidos com as revisões vigentes: Spiritual Points principalmente nos atributos; nenhuma compra de Mastery ou preço universal para técnica/completude. A reação do objeto, citada como possibilidade em M05, não impõe objeto pessoal no prólogo em desacordo com F12.

**Fontes consultadas:** BL-11, entradas `完現術`/`完現術者`, para alma da matéria e afinidade com objetos; BL-05, fichas Ginjō/Tsukishima/Yukio/Riruka, para diversidade de vetores/efeitos; BL-17, trechos de treino do arco, para aprendizado contextual. Textos japoneses interpretados, sem leitura integral de capítulos, episódios ou novels. A página Shonen Jump do arco foi localizada, mas a abertura exibiu essencialmente imagens; não foi usada como confirmação textual adicional. A busca de Bringer Light trouxe resultados secundários, sem descrição primária suficiente para fechar sua execução específica nesta rodada. Meios, apoio, verticalidade e efeitos continuam a pesquisar ou identificar como adaptações, sem alegar confirmação exaustiva.

**Na abertura, M06-10A era PROPOSTO:** uma Proficiência de Fundamentos Fullbringer com Mastery por técnica distinta, preservando desenvolvimento próprio do poder canônico. Alternativa: duas Proficiências para matéria e mobilidade. A recomendação parte do princípio comum e da dimensão do repertório geral, sem impor os três campos de outras raças. A fonte oficial não estabelece quantos indicadores o mod deve ter. A pergunta foi enviada ao usuário e nenhuma resposta foi presumida.

Registrados herança, alternativas, contrato candidato, ensino e critérios de revisão. F1 já exclui edição livre de blocos; não foram aprovadas novas aplicações concretas, radar, voo ou capacidade de usar qualquer objeto como poder. Próximos assuntos: matéria, Bringer Light, combate/vetor e revisão conjunta. Números, itens, formas, conteúdo e integrações conservam seus responsáveis. Sem alteração de código, build, dependências ou saves; sem teste em jogo nesta abertura.


### Aprovação de A e preparação de B/C/D — 20/10/2026

**M06-10A aprovado por resposta explícita:** “Uma Proficiência dos fundamentos + Masteries por técnica (recomendado)”. Registrado contrato estrutural na seção 4 do bloco Fullbringer; duas Proficiências separadas não foram escolhidas. Nenhum comando, custo, variante ou condição de meios foi aprovado automaticamente por essa organização.

Pesquisa complementar: BL-18, sinopse do episódio 346 (exibição 01/11/2011) sobre extrair/potencializar a alma da matéria; episódio 352 (13/12/2011), sobre treino com Jackie e desenvolvimento de movimento rápido por Ichigo. Resumos oficiais japoneses interpretados, sem episódio integral. A fonte dá contexto de aprendizado e mobilidade; não comprova a execução exata de Bringer Light nem apoios, salto, gravidade, custos ou controles do mod. BL-11 reconsultado para o princípio da alma da matéria. Não houve consulta de APIs ou nova auditoria de implementação.

**Na preparação, B/C/D eram PROPOSTOS:** aprendizado próprio com prática; impulso curto direcional com Stamina por início, sem Energia adicional/item; salto impulsionado aprendido a partir do chão, compartilhando Mastery de Bringer Light. A recomendação usa mobilidade como aplicação inicial útil da matéria, preservando F1/F2/F12, A e M05. Alternativas, contratos candidatos e critérios estão nas seções 8–12 de M06-10. Três perguntas enviadas; respostas não presumidas. Ar/água, queda e reações ficam para complemento, sem impor a solução Quincy.

Sem mudança de código, build, dependências ou saves; sem execução de testes em jogo. A conferência documental não demonstra jogabilidade ou calibração. Próximo: registrar B/C/D quando respondidos e complementar a execução antes de vetores/revisão geral.


### Aprovação de B/C/D/E/F e fechamento estrutural Fullbringer — 20/10/2026

Respostas explícitas registradas em M06-10: B — “Aprendizado próprio com prática (recomendado)”; C — “Impulso curto direcional (recomendado)”; D — “Incluir salto impulsionado aprendido (recomendado)”. Na mesma rodada, perguntas próprias de complemento foram respondidas: E — “Horizontal no chão, ar e água; salto com apoio (recomendado)”; F — “Reagir preservando custos, tempos e queda (recomendado)”. Não foram aprovadas por associação a A nem necessitam nova votação.

E permite impulso horizontal aéreo/aquático preservando o meio, sem relançamento vertical no ar ou voo. F permite saída para ação disponível, sem devolver custo/apagar tempos, e ação aérea após salto sem congelar queda. São adaptações de execução do mod; a revisão de contratos locais não acrescentou pesquisa canônica, consulta de APIs ou episódio/capítulo integral. As fontes da preparação continuam com seus limites registrados.

Seções 15–17 consolidam mobilidade, herança de vetor/poder e fechamento **DECIDIDO em nível estrutural**. Não foi identificado conflito que exija rever A–F. Vetores e manifestação permanecem próprios de cada poder; item não transfere aprendizado, foco Quincy não vira exigência de todo Fullbringer e Proficiência não concede técnicas pessoais. Fichas, conteúdo, domínio/formas, itens/reposição, dano, números, apoios/física/estados e integração têm responsáveis explícitos. Não foi criado novo poder para preencher repertório nem descartado o catálogo futuro de técnicas compartilhadas.

**Continuidade:** M06-11 — Auditoria e Integração. M06 continua em especificação; fechamento dos quatro caminhos não equivale a fichas prontas, implementação, teste ou aceite. Sem alteração de runtime, build, dependências ou saves, nem teste em jogo nesta rodada.


## 12. Abertura M06-11 — Auditoria e Integração — 20/10/2026

Confrontados contratos universais de M06, revisões/conclusões raciais e dependências pertinentes de M05. A leitura foi dirigida aos contratos da integração; não é nova revisão integral de todos os módulos/código ou de cada exceção canônica. Não foi identificado conflito que exija revogar escolhas raciais examinadas. Notas de superação/continuidade foram corrigidas sem alterar aprovações; no mapa, a redação sobre pontos/repertórios passou a refletir revisões já existentes.

**Pesquisa técnica nova:** MC-11, seção Damage Types da publicação oficial de Java 1.19.4, consultada em 20/10/2026. Confirma o registro por dados e o papel das tags; não decide como ataques espirituais, armadura, Resistência ou Vene funcionarão no mod. O [snapshot 23w06a](https://www.minecraft.net/en-us/article/minecraft-snapshot-23w06a) foi consultado complementarmente, usando a publicação final como referência principal. Nenhuma migração de Minecraft foi proposta.

Conferidas versões em gradle.properties e a toolchain Java em build.gradle. Lido integralmente CombatEvents.java: o ramo atual de Resistência identifica golpes físicos diretos; isso não demonstra uma integração geral de dano espiritual nem revisa o conceito de M05. A leitura não foi auditoria de todos os emissores, tags, eventos e efeitos do projeto.

**Na abertura, A/B/C eram PROPOSTOS, aguardando respostas explícitas:** proteções compatíveis contra ataques diretos inclusive espirituais, com exceções por técnica; composição sucessiva com limite conjunto a calibrar e Postura separada; Vene no impacto elegível, sem cobertura adicional automática de condição residual. Nenhuma dessas propostas virou aprovação por pedir continuidade. São adaptações do mod, sem novas afirmações canônicas sobre todas as interações de Blut.

Identificadas próximas entregas de M06-11: meios/fases de Shunpo/Sonído, Percepção/Supressão/Liberação universais e estados/concorrência/ciclo de vida. Equipamento, alvos/party, números e conteúdo continuam nas fronteiras dos módulos próprios. Sem alteração de runtime, dependências ou saves e sem execução de build ou teste de Minecraft.


### Aprovação A–E e revisão de dano/mobilidades — 20/10/2026

Três respostas explícitas aprovaram A/B/C: proteções compatíveis contra ataques diretos inclusive espirituais, com exceções específicas; reduções sucessivas com limite conjunto a calibrar; Vene no impacto elegível, sem cobertura adicional automática de condições residuais. Seção 10 de M06-11 registra revisão de Resistência, Postura, defesas, ataques compostos e pipeline vanilla. Não foi identificado conflito que exija revogar essas escolhas; fórmulas, ordem técnica completa e tabelas permanecem pendentes.

Para os complementos Shunpo/Sonído, reconsultado BL-03 em Hirenkyaku (`飛廉脚`) e Arrancar (`破面`). O glossário oficial identifica os métodos de movimento rápido, sem determinar ar/água, controle, gravidade, pagamento ou reações. Texto japonês interpretado; sem capítulo/episódio integral. A busca na página sa do arquivo Pierrot não localizou a entrada Shunpo, portanto não foi usada como comprovação adicional.

D/E aprovados depois por duas respostas próprias: “Horizontal no chão, ar e água (recomendado)” e “Parar para reagir, preservando custos e tempos (recomendado)”. Impulsos básicos horizontais, Stamina no início, percurso e riscos preservados; ação disponível encerra avanço sem recuperação extra só pela parada. E não revisa a saída de Cero nem apaga tempos anteriores. São adaptações aprovadas para essas versões; a semelhança da execução não iguala desempenho/ensino/domínio das quatro mobilidades. Seção 15 registra revisão conjunta.

**Continuidade vigente:** Percepção Espiritual, Supressão e Liberação de Reiatsu, já existentes como fundamentos treináveis de M05; depois estados/concorrência/ciclo de vida. A–E não têm perguntas pendentes. M06 continua em especificação; sem alteração de runtime/build/dependências/saves ou teste de Minecraft nesta atualização. Pesquisa oficial resumida não é validação do comportamento implementado.


### Preparação de Percepção Espiritual — M06-11F/G/H — 20/10/2026

Lidos M05, seções 30–40/51 e delegação numérica na 57, M06-01, Pesquisa em M06-08J e os contratos pertinentes de M06-11. Não foi feita nova leitura integral de todos os módulos. A existência dos fundamentos e o uso da Mastery de Percepção por Pesquisa já estavam aprovados.

Reconsultado BL-11, entrada `探査神経 / ペスキス`, sobre leitura de Reiatsu/características da alma. Consultado BL-19, entrada `霊絡`, como exemplo de manifestação visual sensorial particular, sem concedê-la ao repertório comum. O glossário TYBW foi aberto, mas outras entradas recentes não sustentam novas regras nesta rodada. Resumos oficiais japoneses interpretados, sem capítulos/episódios integrais ou conferência de todas as exceções.

**F/G/H PROPOSTOS, aguardando três respostas próprias:** percepção natural gratuita com leitura breve aprendida e Energia na conclusão; informação aproximada através de obstáculos; caminhada limitada e cancelamento para reação disponível. Métodos raciais/aprendizado e Mastery comum preservados; nenhum número final ou novo catálogo aprovado. As perguntas, alternativas, contratos candidatos e critérios futuros estão em M06-11, seções 16–20. Supressão/Liberação e oposição completa ainda vêm depois.

A busca local por nomes sensoriais não demonstra ausência exaustiva de implementação equivalente. Nenhum código, build, dependência ou save alterado; nenhum teste de gameplay executado. Preparar uma proposta e conferir documentos não equivale a decisão ou aceite do comportamento.


### Aprovação de Percepção Espiritual F/G/H — 20/10/2026

Três respostas próprias aprovaram: F — “Percepção natural + leitura breve aprendida (recomendado)”; G — “Leitura aproximada através de obstáculos (recomendado)”; H — “Caminhar e cancelar para reagir (recomendado)”. Contratos registrados como decididos e revisão conjunta na seção 21 de M06-11. Não foi identificado conflito com M05, Pesquisa ou recuperações anteriores que exija revogação. Preparação anterior preservada como histórico; não há pergunta F/G/H pendente.

Esta atualização não acrescenta pesquisa canônica ou consulta técnica. Permanecem válidos os limites de BL-11/BL-19, sem leitura de capítulos/episódios integrais. Custos, informação e reação são adaptações aprovadas. Números, alvos, fichas, ensino, ciclo de vida e implementação ainda pendem; nenhum gameplay/build/dependência/save alterado ou teste novo. Próximo: Supressão e oposição à percepção, depois Liberação e estados/ciclo de vida.


### Preparação de Supressão e oposição — M06-11I/J/K — 20/10/2026

Herança de M05, seções 32–40/51, e Percepção F/G/H aprovada/revisada. BL-20, sinopse do episódio 121, distingue restrição por selo de capacidade voluntária de ocultação; não importar o fator da cena para o sistema universal. BL-21, sinopse de TYBW 21, complementa reconhecimento familiar e percepção de mudanças. Também lido o resumo Pierrot 266–316 sobre o retorno de Ichigo, sem convertê-lo em prova de Supressão voluntária ou indetectabilidade por Battle Power. Resumos japoneses interpretados, sem episódios/capítulos integrais; buscas não confirmaram uma regra universal de manutenção, rompimento ofensivo ou probabilidade de detecção.

**I/J/K PROPOSTOS**, em três perguntas próprias: Supressão manual sem Energia; início ofensivo aceito encerra ocultação e volta a Natural, com retorno manual; oposição gradual pelas condições, sem nova sorte a cada clique. Compatibilidades, alternativas e critérios candidatos em M06-11, seções 22–26. Não há aprovação automática desses modelos nem compatibilidade universal com formas/efeitos sustentados. Próximo: respostas I/J/K, revisão e Liberação de Reiatsu; depois estados/ciclo de vida.

Sem código, build, dependências ou saves alterados, e sem testes de Minecraft. Pesquisa primária complementa a análise, sem constituir demonstração do funcionamento implementado.


### Aprovação de Supressão I/J/K e revisão — 20/10/2026

Três respostas próprias aprovaram I — “Supressão manual sem consumo de Energia (recomendado)”; J — “Ofensiva encerra Supressão; retorno manual (recomendado)”; K — “Resultado gradual conforme as condições (recomendado)”. Seções 23–27 de M06-11 registram decisões, critérios e revisão com F/G/H, Pesquisa e regras de início/cancelamento ofensivo. Não foi identificado conflito que exija revogar F–K; nenhuma pergunta I/J/K pendente. As propostas anteriores permanecem histórico, sem alterar o escopo das respostas.

Atualização de aprovações sem pesquisa canônica nova, alteração de runtime/build/dependências/saves ou teste de gameplay. Números, fichas, compatibilidades de estados e implementação continuam pendentes; próximo Liberação e estados/concorrência/ciclo de vida.


### Liberação L/M/N aprovada e revisão dos fundamentos — 20/10/2026

Consultada BL-22, sinopse oficial do episódio 308, exibição de 08/02/2011: descreve pessoas perdendo consciência sob a pressão de Aizen. Usado somente esse trecho da página, sem episódio/capítulo integral ou importação das outras sinopses. O caso sustenta opressão narrativa intensa, não custo contínuo, fórmula, alcance ou efeito idêntico na capacidade básica do mod.

Três respostas próprias aprovaram L — “Liberação manual com manutenção de Energia (recomendado)”; M — “Manter durante ações compatíveis (recomendado)”; N — “Opressão limitada com grande diferença espiritual (recomendado)”. Seções 28–32 de M06-11 registram fonte, adaptações, contratos e revisão com F–K. Capacidade aprendida/manual, manutenção paga sem taxa adicional, término em Natural por falta de recurso; combate compatível mantém custos/fases; efeito básico limita movimento comum de alvo elegível muito inferior, sem dano, paralisia ou redução automática da mobilidade técnica.

Não foi identificado conflito que exija revogar F–N. A–N sem perguntas pendentes. Próximo: alcance/alvos/barreiras da pressão e relação com manifestação Natural; depois transições/concorrência/pagamentos/ciclo de vida. M06 continua em especificação, sem números, fichas, conteúdo ou implementação concluídos. Nenhum runtime/build/dependência/save alterado e nenhum teste de Minecraft executado.


### Alcance/alvos e manifestação O/P/Q aprovados — 20/10/2026

Reconsultadas BL-21/22 nas sinopses já identificadas: presença familiar/variações e opressão intensa. Não demonstram raio, regra de blocos/barreiras, proteção de aliados ou opressão Natural universal. Textos japoneses interpretados, sem episódios/capítulos integrais ou novas APIs. As decisões são adaptações explícitas do mod.

Três respostas próprias aprovaram O — “Área limitada que atravessa paredes comuns (recomendado)”; P — “Opressão sobre adversários elegíveis (recomendado)”; Q — “Opressão básica exige Liberação deliberada (recomendado)”. Seções 33–36 de M06-11 registram contratos, alternativas e revisão: área tridimensional finita com intensidade por distância; barreiras com cobertura expressa; proteção de aliados/party/pets e neutralidade sem elegibilidade automática; presença Natural/Suprimida sem dificuldade de movimento básica gratuita. Percepção permanece separada da penalidade e não revela lista de afetados.

Não foi identificado conflito que exija revogar F–N na revisão dirigida. A–Q sem perguntas pendentes; próximo transições/concorrência/pagamentos/ciclo de vida. Fichas, parâmetros, relações concretas de combate e formas continuam com responsáveis. Sem alteração de runtime/build/dependências/saves e sem teste de Minecraft; M06 continua em especificação.


### Trocas, Blut e pagamentos R/S/T aprovados — 21/10/2026

Reconsultada BL-03, entrada 血装: reforço corporal, distinção entre Vene/Arterie e exclusão simultânea. Texto japonês interpretado, sem episódio/capítulo integral; demais entradas recentes não incorporadas. A fonte não determina compatibilidade com Supressão/Liberação, manutenção ou prioridade de pagamentos. Não houve nova consulta de APIs.

Três respostas próprias aprovaram R — “Troca direta validada (recomendado)”; S — “Blut compatível com os estados (recomendado)”; T — “Manutenção primeiro; Blut antes de Liberação (recomendado)”. Seções 37–40 de M06-11 registram adaptações e revisão: um pedido validado sem sobreposição/reset de tempos; Blut com manifestação mantendo exclusividades e sinais; manutenções realmente devidas antes da ação, Blut antes de Liberação, respeitando os momentos próprios de custo e a independência de Stamina/Gintō preparado.

Não foi identificado conflito que exija revogar A–Q ou os contratos de Blut examinados. A–T sem perguntas pendentes; próximo interrupção/ciclo de vida e modo de combate, antes de avaliar fechamento conceitual de M06 e avanço a M07. Fichas/números, formas e conteúdo continuam com responsáveis. Sem alteração de runtime/build/dependências/saves ou teste de Minecraft.


### Ciclo de vida, tempos e efeitos U/V/W aprovados — 21/10/2026

Reconsultada MC-05, Forge 1.20.1 Capabilities, nas seções de sincronização/cópia após morte. A documentação técnica exige tratamento explícito; não escolhe a política de progressão/efeitos ou a contagem dos cooldowns. Sem nova fonte canônica, capítulo/episódio ou integração de APIs nesta rodada.

Três respostas próprias aprovaram U — “Encerrar estados; retorno manual (recomendado)”; V — “Tempo do mundo, preservado entre sessões (recomendado)”; W — “Disparos autônomos continuam na região ativa (recomendado)”. Seções 41–44 de M06-11 registram escopos e revisão: limpar execução deliberada sem apagar conquistas/custos; preservar cooldowns/recuperações pelo tempo de simulação, inclusive offline, pausando com mundo parado; separar disparos autônomos, canais dependentes e montagens persistentes. Ataques efêmeros não retomam após descarregamento/reinício.

CapabilityEvents.java, PlayerData.java e TickHandler.java lidos integralmente; trechos de StatusData.java localizam reset/não persistência dos cooldowns e contagem por tick do jogador. Isso diverge do relógio aprovado e exige adequação futura M16, sem auditoria de todo temporizador. Reposição de recursos no respawn e forma selada encontradas no código não são aprovações novas de design.

A–W sem perguntas pendentes. Próximo modo de combate/interrupções e revisão de maturidade para avaliar fechamento conceitual de M06 e passagem a M07; fichas, números, antidesconexão, formas, instrumentos e conteúdo permanecem com responsáveis. Sem alteração de runtime/build/dependências/saves ou teste de Minecraft.


### X/Y e fechamento conceitual de M06 — 21/10/2026

Duas respostas próprias aprovaram X — “Capacidades espirituais independentes do modo (recomendado)” e Y — “Sair da interface preservando a execução (recomendado)”. Seções 45–46 de M06-11 registram a independência de Supressão/Liberação/leitura e a mudança de interface sem bypass de mãos/fases/cancelamento/custos. Seção 47 consolida contratos herdados de interrupção. São adaptações de operação do mod; não houve nova consulta canônica/API, capítulo/episódio, código ou teste nesta rodada.

Seção 48 registra revisão dirigida das conclusões e contratos examinados, com pendências/condições de retorno: M06 passa a `ESPECIFICADO CONCEITUALMENTE`, sem declarar fichas executáveis, catálogo completo, implementação, teste ou aceite. A–Y sem perguntas pendentes. Fontes anteriores conservam seus limites e datas; a conclusão não alega pesquisa exaustiva do cânone ou auditoria integral de código.

Próximo: abrir M07 — Transformações e Domínio, começando por evolução permanente, despertar e forma temporária, preservando aprovações raciais, pontos/Mastery e dependências da seção 48. Linhagens/artes continuam adiadas. M07 ainda não tem documento próprio neste fechamento; reset de cooldown, números, compatibilidades e conteúdo continuam com responsáveis.

## 13. Abertura M07 e primeiro Shikai — 21/10/2026

Criado [M07 — Transformações e Domínio](07-transformacoes-e-dominio.md), com o template de 15 seções. Lidos os contratos pertinentes de M04 (identidade, RAC-20/23/25, Shinigami, H6–H9, Q6/Q7, F2–F6), M05 (formas, recursos, Mastery e REV-M02) e fechamento de M06. A leitura foi dirigida, não integral de todos os módulos 04–06. A classificação de evolução, conquista e manifestação consolida decisões existentes sem tratá-las como categorias exclusivas.

**Pesquisa reconsultada:** BL-12, entradas Shikai/Zanpakutō, sobre nome, diálogo/sintonia e vínculo; BL-11, Bankai; BL-17, trechos de aquisição/treino/completude no resumo de 343–366. BL-03: Resurrección como liberação do poder selado, Schrift como capacidade individual, Vollständig distinto de Letzt Stil e exigência espiritual de Bankai. Fontes oficiais do anime interpretadas do japonês, sem capítulos/episódios integrais. Arquivos Pierrot refletem sua época; entradas promocionais recentes de BL-03 não foram incorporadas. Não comprovam controles, prova, custos, acesso universal a Segunda Etapa ou equivalência entre todos os Fullbrings.

**Quatro respostas explícitas:** M07-02A — “Vínculo com o espírito + prova de liberação (recomendado)”; B — “Shikai manual e independente da interface (recomendado)”; C — “Custo ao liberar, sem manutenção básica (recomendado)”; D — “Manifestação confiável + domínio progressivo (recomendado)”. A/B/C/D registrados como decididos, com alternativas não escolhidas e limites. Prova, controles, economia e confiabilidade são adaptações aprovadas; não foram atribuídos como regras universais da obra.

Revisão dirigida preserva conquista separada de arma/Shikai/Bankai, pontos nos atributos, prática válida, vínculos, fases e regras próprias de arco/Blut. Próximo: fases, ações compatíveis, arma guardada/ausente, depois Bankai. Nenhuma pergunta A–D pendente; valores, compatibilidades, ciclo de vida, demais caminhos e fichas continuam abertos. Reconfirmados Minecraft 1.20.1/Forge 47.4.10/Java 17 nos arquivos de build. Sem alteração de runtime/build/dependências/saves, commit/push ou teste de Minecraft.

### Execução, empunhadura e posse — M07-02E/F/G — 21/10/2026

Três respostas próprias aprovaram E — “Preparo breve cancelável para reagir (recomendado)”; F — “Conservar liberação; exigir empunhadura para usar (recomendado)”; G — “Encerrar manifestação ao perder a posse (recomendado)”. Registrados em M07, seções 8.6–8.8. E mantém pagamento somente na conclusão; F diferencia guardar no próprio inventário e perder posse; G preserva vínculo/domínio e não restaura manifestação ao recuperar item. Efeitos especiais e reposição permanecem com responsáveis.

Reconsultado BL-12 nas entradas Shikai/Zanpakutō. Não define duração, reações, inventário, conservação ou transferência do mod. São adaptações aprovadas; sem nova leitura integral de capítulo/episódio. Contratos pertinentes de M06-02S/T, M06-05, M06-07V–Y/HD–HF e M06-11V/W/X/Y confrontados com A–G; sem conflito identificado que exija revogação. Corrigido o quadro de estados do handoff que ainda agrupava M07 entre os documentos não iniciados; correção factual, sem nova decisão de produto.

H/I/J preparados como propostas: conclusão/selamento sem espera adicional, ciclo de vida e convivência com presença. Aguardam respostas explícitas. Nenhuma API, código, build, dependência ou save foi alterado e nenhum teste de Minecraft foi executado nesta rodada.

### H/I/J aprovados e consolidação estrutural do primeiro Shikai — 21/10/2026

Três respostas próprias: H — “Transição sem recuperação adicional (recomendado)”; I — “Encerrar Shikai; retorno manual (recomendado)”; J — “Shikai compatível com os estados de Reiatsu (recomendado)”. Registrados em M07, seções 8.9–8.11; propostas anteriores são histórico, não perguntas pendentes. Conclusão/selamento preservam tempos e fases; ciclo de vida encerra manifestação sem apagar conquista; forma e presença coexistem mantendo visibilidade, oposição, custos e regra de ofensiva da Supressão.

Revisão dirigida de A–J não identificou conflito que impeça avançar para Bankai. Consolidação final de M07 registra tabela de situações, pendências com responsáveis e critérios futuros. Primeiro Shikai decidido estruturalmente; M07 continua em especificação. Próximo: M07-03 — Bankai, conquista/domínio antes de execução/transições. Fichas, números, prova, exceções e integração permanecem abertos. Sem nova pesquisa canônica/API na atualização de H/I/J, capítulo/episódio integral, implementação ou teste de Minecraft.

### Abertura de Bankai — M07-03A/B propostos — 21/10/2026

Reconsultados BL-03/11, entrada `卍解`: materialização/domínio do espírito; BL-03 descreve exigência espiritual para manutenção. Consultada BL-23, retrospectiva 42–63, nos trechos de treino de Ichigo e conquista de Renji, e a [sinopse oficial do episódio 52](https://www.tv-tokyo.co.jp/anime/bleach_bestbout/episodes/index_2.html), republicação de 03/07/2024. Fontes do anime interpretadas do japonês; sem capítulo/episódio integral ou importação de entradas promocionais recentes. O prazo excepcional de Ichigo não decide um cronômetro universal. Custos e manutenção do mod seguem abertos.

M07-03A propõe preparação demonstrada em Shikai, relação avançada e prova própria com materialização do espírito, sem Mastery máxima universal/cargo. B propõe manifestação básica confiável e Mastery própria de Bankai, com técnicas e versões posteriores separadas. **Ambos PROPOSTOS, aguardando respostas explícitas.** Revisão dirigida de M04/Shinigami e M05/Mastery/REV-M02 preserva a separação de conquistas e proíbe compra de domínio; A–J de Shikai não mudam. Não se copiam seus custos, posse ou transições para Bankai. Sem implementação/teste de Minecraft; próxima sequência: decidir A/B e preparar execução/economia/transições.

### Bankai A/B aprovados; C/D preparados — 21/10/2026

A — “Relação avançada + prova com o espírito (recomendado)” e B — “Manifestação confiável + domínio próprio (recomendado)” aprovados por respostas explícitas; registrados nas seções 8.12/8.13 de M07. A preparação anterior permanece histórica. Revisão dirigida preserva conquista pessoal, ausência de requisito circular, domínio próprio e técnicas/versões posteriores separadas. Não fecha M07 nem as regras de execução de Bankai.

C propõe ativação manual direta a partir do estado selado ou Shikai, com arma compatível empunhada e independência da interface. D propõe custo na conclusão + manutenção paga, sem cronômetro universal. **C/D PROPOSTOS, aguardando respostas próprias.** BL-03 reconsultado na entrada Bankai para exigência de sustentação espiritual; não comprova taxas, cobrança inicial, prioridade, retorno ou controles. Sem capítulo/episódio integral ou nova consulta de APIs. Fases, reações, reversão, concorrência de recursos, posse, Reiatsu e ciclo de vida permanecem para complemento. Sem alteração ou teste de gameplay.

### Bankai C/D aprovados; E/F preparados — 21/10/2026

C — “Bankai manual e direto (recomendado)” e D — “Ativação + manutenção de Energia (recomendado)” aprovados por duas respostas próprias; registrados nas seções 8.14/8.15 de M07. A–D sem pergunta pendente. Ativação direta a partir de selado/Shikai, independente da interface, com custo na conclusão e manutenção. Valores, concorrência e efeitos permanecem dependentes de ficha/calibração.

**E/F PROPOSTOS, aguardando respostas:** preparo breve cancelável para reação disponível e retorno de Bankai a Shikai/selado. E preserva a forma de origem ainda válida sem criar Shikai gratuitamente numa tentativa cancelada. F propõe retorno sem nova ativação/cobrança de Shikai na saída voluntária ou falta de manutenção, sem decidir morte, logout, dimensão, posse ou quebra por associação. Revisão documental dirigida; sem nova pesquisa canônica/API, capítulos/episódios integrais ou alteração/teste de gameplay. Próximo: decidir E/F e seguir conclusão/encerramento, posse, presença, pagamentos e ciclo de vida.

### Bankai E/F aprovados; G/H/I preparados — 21/10/2026

E — “Preparo cancelável para reagir (recomendado)” e F — “Retorno a Shikai sem nova cobrança (recomendado)” aprovados por duas respostas explícitas; seções 8.16/8.17 de M07. A–F sem perguntas pendentes. Cancelamento anterior conserva apenas a forma de origem válida; saída voluntária de Bankai permite Shikai/selado e falha de manutenção retorna a Shikai quando válido, sem novo preparo/custo. Não restaura manifestação depois de perda de posse ou eventos que encerram Shikai.

**G/H/I PROPOSTOS**, aguardando três respostas: conclusão/encerramento sem recuperação adicional; conservação de Bankai na própria posse com manutenção ativa e restrições de empunhadura; perda de posse encerra a manifestação sem transferir poder ou restaurá-la ao recuperar o item. Fichas particulares, slots, morte/logout/dimensão/reinício, Reiatsu e concorrência continuam abertos. Revisão documental dirigida; sem pesquisa canônica/API nova, capítulos/episódios integrais ou alteração/teste de gameplay.

### Bankai G/H/I aprovados; J/K/L preparados — 21/10/2026

G — “Transições sem recuperação adicional (recomendado)”; H — “Conservar Bankai com manutenção ativa (recomendado)”; I — “Encerrar Bankai ao perder posse (recomendado)” aprovados por três respostas explícitas; seções 8.18–8.20 de M07. A–I sem perguntas pendentes. Guardar não suspende manutenção; transferência física encerra manifestação sem transferir conquista ou restaurar Shikai indevido. Fichas particulares e inventário modificado permanecem com responsáveis.

**J/K/L PROPOSTOS**, aguardando respostas: convivência com estados de Reiatsu, prioridade Bankai→Liberação→ação nas obrigações devidas, e encerramento no ciclo de vida sem restauração automática. Não autoriza combinações raciais por definir prioridade de custos. Revisão dirigida de M06-11T/U/V/W, sem nova pesquisa canônica/API ou capítulo/episódio integral. Sem alteração/teste de gameplay. Depois revisar e pesquisar quebra/consequências antes de avaliar fechamento de Bankai.

### Bankai J/K/L aprovados — 21/10/2026

J — “Bankai compatível com os estados de Reiatsu (recomendado)”; K — “Bankai → Liberação → ação (recomendado)”; L — “Encerrar manifestações; retorno manual (recomendado)” aprovados por três respostas explícitas, seções 8.21–8.23 de M07. A–L sem perguntas pendentes. Revisão diferencia falta de manutenção, perda de posse e ciclo de vida, e separa custo inicial de Bankai da manutenção de uma forma já ativa. Atualizados encaminhamentos internos já resolvidos, sem reabrir decisões.

Próximo: pesquisar quebra/consequências e concluir revisão antes de avaliar fechamento. Nenhuma perda permanente, método de reparo ou barra de durabilidade de Bankai foi aprovada nesta atualização. Sem nova pesquisa canônica/API no registro das respostas; sem alteração de runtime/build/dependências/saves ou teste de Minecraft.

### Revisão A–L de Bankai e pesquisa de quebra — 21/10/2026

Releitura dirigida do conjunto A–L em M07; tabela de situações e responsáveis registrada ao final do módulo. Sem conflito estrutural identificado no recorte. Encaminhamentos internos já resolvidos foram atualizados; a revisão não é auditoria integral do catálogo, de todos os módulos/código ou aceite de gameplay.

Pesquisa nova delimitada em M07/seção 4: reconsultada BL-03, entrada `破壊された卍解`, e consultada BL-24. Consulta complementar à sinopse TYBW 10 localizou Ōetsu; não demonstra reparo universal. A sinopse 13 não abriu e não foi usada. Resumos oficiais interpretados do japonês, sem episódios/capítulos integrais. A política de recuperação proposta não foi atribuída como regra canônica.

**M/N PROPOSTOS:** causas explícitas para quebra estrutural, distinta de durabilidade vanilla/fim normal, e consequência persistente com recuperação específica disponível sem apagar aquisição/Mastery. Duas respostas ainda necessárias; não foi aprovada perda irreversível, caminho concreto de reparo ou nova barra. Conteúdo, fórmulas, fichas e implementação continuam com responsáveis; sem alteração de runtime/build/dependências/saves, commit/push ou teste de Minecraft.

### M/N aprovados e consolidação estrutural de Bankai — 21/10/2026

M — “Quebra por condições específicas (recomendado)” e N — “Dano persistente com recuperação específica (recomendado)” aprovados por duas respostas explícitas; seções 8.24/8.25 de M07. A–N, 14 decisões de Bankai, sem perguntas pendentes. Quebra exige causa/componentes de ficha; consequência persiste até recuperação própria disponível antes do risco, preservando aquisição/Mastery. Não se aprovou barra universal, perda irreversível automática ou reparo universal como fato canônico.

Bankai `DECIDIDO` em nível estrutural, com revisão final, critérios futuros, tabela de entregas/responsáveis e condições de retorno. Não identificado conflito estrutural no recorte examinado. Valores, fichas de formas/efeitos/corpos/controles, prova e recuperação concretas, inventário, UI e execução persistente ainda pendem; M07 permanece em especificação. Próximo M07-04 — evolução Hollow, preservando contratos raciais e pesquisando antes de novas propostas. Linhagens/artes adiadas. Sem nova pesquisa canônica/API nesta consolidação, alteração de runtime/build/dependências/saves, commit/push ou testes de Minecraft.


## 14. Abertura de M07-04 — evolução Hollow — 21/10/2026

Conferidos M04/H1–H9, REV-H6-01, M06-08G e a reconciliação aprovada. Mantidos predação para avanço, agência Gillian, evolução permanente e ausência de regressão passiva. H9 governa a raridade determinística; não retomar a pendência histórica anterior como decisão aberta.

Consultados BL-25/26 nos verbetes indicados. Reconsultada a sinopse oficial TV Tokyo do episódio 167, republicada em 14/08/2024, sobre predação no passado Adjuchas de Grimmjow. BL-03, entrada Hueco Mundo, distingue nutrição ambiental de evolução. Referências oficiais do anime, interpretadas do japonês, sem capítulos/episódios integrais. Não confirmam forma Vasto Lorde de Grimmjow, requisitos numéricos, genética aleatória do jogador ou economia de consumo.

**M07-04A/B/C PROPOSTOS**, aguardando respostas próprias: interação de devoração após derrota; presas NPCs inimigas e participação útil sem último golpe; início manual da evolução preparada. São adaptações de operação, cooperação e mundo construído, não regras comprovadas da obra. Fichas, marcos por estágio, estabilidade, critérios de crédito, espaço, falhas e persistência permanecem abertos com responsáveis. Arrancarização/Resurrección e arte de linhagem vêm depois.

Sem nova consulta de APIs, implementação, build, alteração de dependências/saves ou teste de Minecraft. O código não foi usado como prova de uma origem Hollow completa; os critérios desta abertura são candidatos, não evidência de gameplay.


### M07-04A/B/C aprovados e marcos Gillian/Adjuchas propostos — 21/10/2026

Respostas próprias aprovaram A — “Devorar por interação após derrotar (recomendado)”; B — “Presas elegíveis e participação efetiva (recomendado)”; C — “Iniciar evolução deliberadamente (recomendado)”. Seções 8.27–8.29 de M07; sem pergunta pendente. Complementos de ficha, itens/representação e distribuição de oportunidades continuam em elaboração, sem aprovação integral por associação.

Reconsultados BL-25/26 nos verbetes já registrados: agregação Menos e predominância individual, distinção Adjuchas/Vasto Lorde. Resumos oficiais do anime relativos à sua época, interpretados do japonês; não capítulos/episódios integrais. Nenhuma nova fonte ou leitura de mangá nesta rodada. A referência não determina acontecimento jogável, prova, escolha de linhagem ou número de participantes do mod.

**D/E PROPOSTOS:** formação Gillian com acontecimento de agregação de NPCs, preservando agência; conquista Adjuchas por desenvolvimento e prova racial sem linhagem obrigatória, com integração possível à quest de uma linhagem compatível. Não concede o próximo estágio automaticamente nem reabre a preparação de identidade permitida por H6. Sem falha aleatória, regressão ativa ou conteúdo de prova aprovado por associação. Próximo: respostas D/E e rota Vasto Lorde/complementos, depois Arrancarização/Resurrección. Sem implementação ou teste de Minecraft.


### M07-04D/E aprovados; rota Vasto Lorde e estabilidade propostas — 21/10/2026

D — “Formação por agregação de Hollows (recomendado)” e E — “Conquista racial sem linhagem obrigatória (recomendado)” aprovados por duas respostas próprias; seções 8.30/8.31 de M07. A–E sem pergunta pendente. A formação registra um marco e respeita o início deliberado; a conquista Adjuchas pode integrar uma quest de linhagem sem torná-la obrigatória ou repetir a prova. Fichas, conteúdos e números continuam pendentes.

Reconsultados BL-25 e a sinopse oficial TV Tokyo do episódio 167 (republicação de 14/08/2024), no trecho sobre condição Adjuchas. A primeira referência descreve ascensão difícil; ambas descrevem regressão narrativa. Resumos oficiais do anime, interpretados do japonês, sem capítulos/episódios integrais. Não comprovam uma organização de objetivos de jogo ou regra de falha de prova. A ausência de regressão passiva e H9 continuam aprovações anteriores do mod.

**F/G PROPOSTOS, aguardando respostas:** rota avançada com marcos de predação/domínio e prova final; estágios consolidados estáveis, sem regressão por falha/dano/morte comum, limitando a perda ao andamento da tentativa ainda não concluída e preservando gastos normais. São adaptações de progressão/continuidade. Não revogam o Vasto Lorde permanente nem aprovam objetivos, presas, critérios de domínio, penalidades gerais de PvP ou recuperação de itens. Próximo: responder F/G e completar devoração/corpo/ciclo de vida antes da revisão e de Arrancarização/Resurrección. Sem consulta nova de APIs, implementação ou teste de Minecraft.


### M07-04F/G aprovados; devoração e espaço corporal em elaboração — 21/10/2026

F — “Marcos registrados + prova final (recomendado)” e G — “Falha limitada à tentativa, sem regressão racial (recomendado)” aprovados por duas respostas próprias; seções 8.32/8.33 de M07. A–G, sete decisões, sem pergunta pendente. Preservação de estágio, predação e marcos concluídos não implica restaurar tentativa interrompida, itens, custos ou o estado do encontro. Valores, marcos e prova concretos continuam para fichas/conteúdo.

**H/I/J PROPOSTOS, aguardando respostas:** devoração breve parada e cancelável para reagir; nenhum custo adicional de Energia/Stamina na interação básica; evolução corporal na posição atual, se o novo perfil couber, sem reposicionamento automático. Regras candidatas de execução, recurso e mundo construído, sem atribuição ao cânone. Preparo/custo da evolução, meios, ciclo de vida e distribuição/disponibilidade da predação permanecem pendentes.

Sem pesquisa canônica/API nova nesta atualização de aprovações e contratos locais; fontes anteriores mantêm suas datas/limites. Não houve alteração de implementação, build, dependências ou saves nem teste de Minecraft. Próximo: H/I/J e complementos antes da revisão estrutural da evolução Hollow; depois Arrancarização/Resurrección. Linhagens e artes adiadas.


### M07-04H/I/J aprovados; mudança evolutiva e ciclo de vida propostos — 21/10/2026

H — “Parado, com cancelamento para reagir (recomendado)”; I — “Sem custo adicional de Energia ou Stamina (recomendado)”; J — “Exigir espaço na posição atual (recomendado)” aprovados por três respostas próprias; seções 8.34–8.36 de M07. A–J, dez decisões, sem perguntas pendentes. Sem custo/refill por devoração; espaço revalidado e sem reposicionamento automático para evoluir. Valores, medidas, meios e ficha concreta não foram definidos por essas respostas.

**K/L/M PROPOSTOS:** evolução permanente sem taxa/manutenção adicional de recurso; preparo breve parado, apoiado e fora de combate; eventos de ciclo de vida cancelam devoração/evolução incompletas sem apagar conquistas concluídas ou retomar execução automaticamente. Aguardando três respostas; não foram aprovados por associação à devoração, a Shikai/Bankai ou a M06. Spawn/retorno compatível com o corpo exige contrato M10/M15/M16 antes da origem jogável; J não o decide sozinho.

Atualização de aprovações e contratos locais, sem pesquisa canônica/API nova. Fontes anteriores mantêm datas/limites. Não houve implementação, build, alteração de dependências/saves ou teste de Minecraft. Próximo: K/L/M, revisão e pendências da evolução, depois Arrancarização/Resurrección. Linhagens/artes adiadas.


### M07-04K/L/M aprovados e evolução Hollow consolidada estruturalmente — 21/10/2026

K — “Evolução sem taxa adicional de recurso (recomendado)”; L — “Preparo apoiado e fora de combate (recomendado)”; M — “Encerrar preparos; preservar conquistas (recomendado)” aprovados por três respostas próprias, seções 8.37–8.39 de M07. A–M reúne treze decisões, sem pergunta básica pendente. Sem taxa/manutenção adicional de Energia/Stamina nem refill pela evolução; preparo breve parado, apoiado e fora de Combat State; ciclo de vida encerra devoração/evolução incompletas, conserva conquistas concluídas e exige novo pedido.

Revisão documental dirigida de A–M, contratos raciais aplicáveis, revisões de pontos/Mastery e M06/fases, modos e ciclo de vida não identificou conflito estrutural que exija revogar as decisões examinadas. Devoração em combate e evolução fora dele são operações distintas. Foram atualizadas remissões já resolvidas, mantendo histórico e complementos de ficha em elaboração. Não houve aprovação integral dos detalhes candidatos por associação.

**M07-04 consolidado estruturalmente**, com tabela de situações, responsáveis e condições de retorno no fechamento da seção 15. Permanecem fichas, valores, perfis, presas/participação/distribuição, provas/conteúdo, compatibilidade com presença/efeitos sustentados e realização técnica. Prioridade antes da origem jogável: política de spawn/retorno compatível com corpos permanentes grandes, sem regressão de estágio ou forma compacta silenciosa. M07 inteiro continua `EM ESPECIFICAÇÃO`; próximo **M07-05 — Arrancarização**, depois Resurrección. H6/H8/H9 e o adiamento de linhagens/artes permanecem.

Registro de respostas e revisão de contratos locais, sem nova pesquisa canônica/API; fontes anteriores mantêm datas e limites. Sem implementação, build, alteração de dependências/saves, commit/push ou testes de Minecraft. A conferência documental não é evidência de gameplay.


## 15. Abertura de M07-05 — Arrancarização — 21/10/2026

Consultado BL-27, apresentação editorial oficial ligada ao mangá, verbete `破面`. Reconsultados BL-26/Arrancar e BL-11/Hōgyoku, arquivos oficiais do anime referentes à sua época. Máscara, arma e intervenção do Hōgyoku têm apoio nos textos acessíveis; preparo, acontecimento e via pessoal são traduções jogáveis declaradas. Não foi lido capítulo/episódio integral nem comprovado procedimento autônomo universal para todas as classes. A busca recuperou texto indexado de BL-03/Arrancar; sua abertura direta retornou 403 nesta rodada, sem uso decisivo para o processo.

**M07-05A/B/C aprovados por três respostas próprias:** A — “Preparo no estágio + acontecimento próprio (recomendado)”; B — “Via pessoal sem facção ou Hōgyoku obrigatórios (recomendado)”; C — “Arma própria ao concluir Arrancarização (recomendado)”. Seções 8.41–8.43 de M07. Não há pergunta dessas decisões ou nova proposta aguardando resposta nesta atualização. A via pessoal inclui descoberta ou orientação e prática, sem exigir Aizen/facção/Hōgyoku; arma própria não entrega Resurrección, técnicas exclusivas ou domínio.

Confrontados RAC-04, H6/H8/H9, M06/perfis/linhagens e M07-04: preservar origem, rotas Gillian/Adjuchas/Vasto Lorde e encerramento da evolução normal; conquista Arrancar, arma e liberação permanecem distintas. Sem conflito identificado no recorte examinado. A preparação pode integrar quest compatível sem duplicar o mesmo acontecimento. Não foram aprovados métodos assistidos, NPCs concretos, números, custos ou modelo artístico.

Próximo: corpo-base e continuidade de desenvolvimento/capacidades, diferenças entre origens e execução/recursos/falha/ciclo de vida; depois Resurrección. Ficha da arma deve resolver identidade ainda não escolhida, inventário cheio, posse/perda/reposição e criação vinculada sem duplicação. M07-05 e M07 inteiro seguem em especificação. Plataforma local reconfirmada em gradle.properties/build.gradle: Minecraft 1.20.1 / Forge 47.4.10 / Java 17. Sem nova consulta de APIs, implementação, build, alteração de dependências/saves, commit/push ou testes de Minecraft.


### M07-05D/E — propostas de corpo-base e continuidade — 21/10/2026

Conferidos contratos de perfis, Proficiência/Mastery, linhagens, estado-base/arma e regeneração de M06/M07. Reconsultados BL-26/Arrancar/Vasto Lorde, BL-27 e BL-10/Aaroniero. O glossário histórico descreve variação de aparência conforme evolução anterior; não é ficha universal nem demonstra que todas as origens produzam corpos humanos idênticos. O caso de Aaroniero separa origem Gillian, posto e poder particular; não generalizar Glotonería ou anatomia para a raça. BL-03/Arrancar e BL-14/Ulquiorra recuperadas apenas como trechos indexados, sem nova leitura integral; preservada cautela de regeneração já aprovada em M06-08J. Sem capítulos/episódios integrais ou novas APIs.

**D/E PROPOSTOS, aguardando respostas próprias:** D — corpo-base humanoide jogável nas três origens, adaptação explícita sem fixar medidas/artes; E — Proficiências/Masteries preservadas e técnicas aprendidas utilizáveis quando compatíveis, com bloqueio das incompatíveis e aprendizado próprio das novas. Não transferir automaticamente regeneração, golpes de membros ausentes ou domínio de armas/técnicas novas. A alternativa de E exige readaprender execução conservando histórico; a de D admite corpos-base distintos, inclusive não humanoides.

A/B/C continuam aprovados. As propostas não mudam H6/H8/H9, encerramento da evolução normal ou acesso compatível da origem Gillian. Depois diferenças entre origens e execução/recursos/falha/ciclo de vida; então Resurrección. M07 segue em especificação, sem implementação, build, alteração de dependências/saves ou testes de Minecraft.


### M07-05D/E aprovados; correção da resposta sobre corpo-base — 21/10/2026

D inicialmente recebeu pedido de adiamento; o usuário o substituiu por “Eu entendi errado a primeira, concordo com a recomendação”. Aprovada base humanoide jogável nas três origens, mantendo medidas, traços e diferenças funcionais para detalhamento; não há adiamento pendente. E aprovado — “Preservar domínio e usar o que for compatível (recomendado)”: Proficiências/Masteries preservadas e técnicas aprendidas utilizáveis quando compatíveis, sem transferir automaticamente regeneração ou execução dependente de anatomia ausente; capacidades novas exigem aprendizado.

Seções 8.44/8.45 de M07; A–E, cinco decisões de Arrancarização, sem perguntas pendentes. Preservados perfis funcionais, origem, três campos, acesso compatível Gillian e H9. Próximo: diferenças entre origens e execução/recursos/interrupção/falha/ciclo de vida; depois Resurrección. Perfis/matriz de capacidades, conteúdo, itens e números ainda exigem ficha. M07 continua em especificação. Registro das respostas e revisão documental, sem nova pesquisa canônica/API, implementação, build, alteração de dependências/saves, commit/push ou testes de Minecraft.


### M07-05F/G propostos — consequências das origens e custo — 21/10/2026

Reconsultados BL-26, entradas Adjuchas/Arrancar/Vasto Lorde; BL-10, Aaroniero; e BL-27, verbete Arrancar. O glossário relaciona força anterior e capacidade após a mudança, sem fornecer tetos ou curvas de crescimento por origem. Aaroniero é exemplo Gillian entre os Espada; absorção é seu poder individual. Fontes resumidas oficiais, sem capítulo/episódio integral e sem importação de multiplicador narrativo. Os limites históricos dos arquivos permanecem.

**F/G PROPOSTOS**, seção 12.1 de M07, aguardando duas respostas próprias: diferenciar trajetórias pelo preparo e momento de acesso, sem bônus fixo por rótulo de origem, mantendo crescimento compatível; não cobrar Energia/Stamina adicional pela passagem ou manutenção da condição. São adaptações de design. A–E continuam aprovados; D não voltou a ser adiado. H6/H8/H9 e M05 não foram revisados. Igualdade de desempenho final, acesso a toda linhagem, novas passivas e números não foram aprovados. Viabilidade das três origens exige fichas e avaliação M15; acesso antecipado não justifica inviabilidade posterior. Próximo: respostas, execução/espaço/falha/ciclo de vida; depois Resurrección. Sem alteração de runtime ou testes de Minecraft.


### M07-05F/G aprovados — origens e custo — 21/10/2026

Respostas explícitas: F — “Trajetórias e preparo, sem bônus fixo por origem (recomendado)”; G — “Sem taxa adicional pela Arrancarização (recomendado)”. Seções 8.46/8.47 de M07. A–G, sete decisões, sem perguntas pendentes. Preparação anterior mantida como histórico, sem marcar F/G ainda abertos.

Trajetórias diferem pelo preparo e momento de acesso, com crescimento compatível; sem bônus fixo/teto menor/taxa extra de treino apenas pela origem. Não iguala resultados, concede toda linhagem ou permite retomar evolução normal. Passagem e manutenção da condição não cobram Energia/Stamina adicional, nem restauram recursos/tempos; desafios/técnicas têm custos próprios. Viabilidade avançada, capacidades/perfis concretos, máximos e arma/inventário permanecem em elaboração. H6/H8/H9 e M05 preservados; nenhuma nova fonte ou fórmula adotada. Próximo: execução/espaço/apoio, interrupção/falha/ciclo de vida; depois Resurrección. Sem implementação ou testes de Minecraft.


### M07-05H/I/J propostos — execução, espaço e interrupção — 21/10/2026

A–G permanecem aprovados. Preparadas três propostas próprias, seção 12.1 de M07: H — início deliberado com preparo parado/apoiado fora de combate, cancelável para reação disponível; I — validar espaço na posição atual antes e ao concluir, sem reposicionar automaticamente; J — encerrar preparo incompleto no ciclo de vida sem apagar marcos, preservando Arrancarização já concluída. Aguardando três respostas; nenhuma dessas propostas é aprovação herdada de M07-04 ou Shikai/Bankai.

Análise de contratos locais e referências de revisão da skill, sem pesquisa canônica/API nova. Fontes anteriores conservam datas e limites; regras operacionais são adaptações propostas. Não atribuir à obra exigência de chão, preparo fora de combate ou limpeza de sessão. A escolha final pode integrar o acontecimento de A, sem segundo ritual obrigatório. Arma/inventário, perfis, efeitos sustentados e viabilidade continuam para fichas. Corrigidas sínteses atuais ainda em A–E para refletir F/G já aprovados. Próximo: respostas/revisão de Arrancarização, depois Resurrección; sem implementação ou testes de Minecraft.


### M07-05H/I/J aprovados e Arrancarização consolidada estruturalmente — 21/10/2026

Respostas próprias: H — “Preparo deliberado e fora de combate (recomendado)”; I — “Exigir espaço na posição atual (recomendado)”; J — “Encerrar preparo; preservar conquistas (recomendado)”. Seções 8.48–8.50 de M07. A–J, dez decisões aprovadas, sem perguntas pendentes. Preparação anterior permanece histórica; custos, perfis e poderes não foram herdados por associação.

Revisão dirigida de contratos locais não identificou conflito estrutural no recorte examinado. M07-05 consolidado estruturalmente, com situações, entregas/responsáveis e condições de retorno na seção 15. Perfis, acontecimento, apoios/meios, efeitos sustentados, inventário cheio/arma, conversão de máximos, viabilidade avançada e persistência ainda exigem fichas e evidência; não se declara experiência executável/aceita. Próximo M07-06 — Resurrección, a abrir por pesquisa pertinente e decisões próprias; linhagens/artes adiadas. Sem pesquisa canônica/API nova, implementação, build ou testes de Minecraft. Fontes existentes mantêm suas datas e limites; a revisão é documental.


## 16. Abertura de M07-06 — Resurrección — 21/10/2026

Reconsultados BL-19, entrada帰刃/レスレクシオン, e BL-26, entrada破面: liberação de capacidades Hollow no corpo e núcleo selado na arma. Abertura direta de BL-03 retornou 403; busca recuperou texto indexado de刀剣解放. Esse trecho não equivale a leitura direta integral do site nem de capítulo/episódio. A comparação de princípio com Bankai não decide currículo, manutenção ou reversão do mod. A menção à segunda etapa não estabelece acesso universal. Arquivos Pierrot mantêm informação relativa à época de exibição; textos japoneses interpretados, sem multiplicadores importados.

**M07-06A/B PROPOSTOS**, aguardando duas respostas: condição Arrancar, arma/identidade vinculadas, preparo e demonstração prática de liberação que pode integrar a quest; manifestação básica confiável depois da conquista, com Mastery própria e técnicas/versões posteriores separadas. São adaptações, não currículo universal comprovado pelas fontes. Confrontados RAC-20/RAC-23, H6/H8/H9, três campos e M07-05; não identificada necessidade de rever as aprovações para apresentar as opções.

Sem ritual de espírito, posto/facção ou corpo antigo concedidos por associação; via compatível Gillian preservada, linhagens/artes adiadas. Manifestação, recursos, repertório inicial, corpos, mãos/arma, encerramento e ciclo de vida continuam para as rodadas/fichas próprias. Próximo: respostas A/B e execução. Sem consulta de APIs, implementação, build ou testes de Minecraft.


### M07-06A/B aprovados — conquista e domínio — 21/10/2026

Respostas próprias: A — “Preparo do próprio poder + demonstração (recomendado)”; B — “Manifestação confiável + Mastery própria (recomendado)”. Seções 8.52/8.53 de M07. A/B sem perguntas pendentes; preparação acima preservada como histórico. Condição Arrancar, arma/identidade e demonstração integrada à quest quando cabível; uso posterior confiável com requisitos cumpridos e Mastery própria. Técnicas/versões posteriores seguem aprendizado próprio.

Revisão dirigida não identificou conflito com os contratos examinados de RAC-20/RAC-23/H6/M07-05. A primeira demonstração deve ser possível sem exigir técnica exclusiva ou Mastery da forma ainda inacessível. As respostas não aprovam todo detalhamento candidato, recursos ou Segunda Etapa. Conteúdo e fichas permanecem pendentes. Próximo: manifestação, arma/modo e recursos, depois demais contratos da forma. Sem pesquisa adicional à abertura desta rodada, APIs, implementação, build ou testes de Minecraft; referências mantêm datas/limites. Linhagens/artes adiadas.


### M07-06C/D propostos — ativação/arma/modo e recursos — 21/10/2026

Reconsultados BL-19/帰刃 e BL-26/Arrancar; busca recuperou a entrada indexada de BL-03/刀剣解放, sem nova abertura direta bem-sucedida. São verbetes oficiais do anime/texto indexado, com os limites históricos registrados; não capítulos/episódios integrais. Confirmam relação entre liberação, corpo e capacidades seladas na arma, sem estabelecer custo universal de ativação/manutenção ou interface. Busca dirigida de gasto não acrescentou fonte decisiva; não se afirma ausência de custo em toda a obra.

A/B permanecem aprovados. **C/D PROPOSTOS**, aguardando duas respostas: ativação manual com arma própria empunhada para liberar, independente de Combat Mode; Energia na conclusão e manutenção ativa sem prazo fixo universal. São adaptações de operação/recursos, sem herança automática de regras Shinigami. Empunhadura depois da transformação, manifestação especial da arma, valores, retorno e prioridade de cobranças ainda exigem ficha/decisões. Próximo: respostas e preparo/transições, corpo/arma, encerramento, compatibilidades/pagamentos e ciclo de vida. Linhagens/artes adiadas; sem APIs, implementação, build ou testes de Minecraft.


### M07-06C/D aprovados — ativação e recursos — 21/10/2026

Respostas próprias: C — “Ativação manual com arma própria (recomendado)”; D — “Ativação + manutenção de Energia (recomendado)”. Seções 8.54/8.55 de M07. A–D aprovados, sem perguntas pendentes. Pedido manual com arma própria para liberar, independente da interface; Energia na conclusão e manutenção ativa sem prazo universal, técnicas com custos próprios. Cancelamento anterior não cobra ativação; manutenção não financiada encerra a manifestação.

Revisão local preserva aquisição/domínio, condição Arrancar, fases e tempos. A empunhadura inicial não decide como a arma permanece depois da liberação. Valores, retorno corporal, prioridades de cobrança e demais complementos ainda precisam de ficha/decisão; não foram aprovados em bloco. Próximo: preparo/interrupção e transições, depois corpo/arma, posse/equipamentos, compatibilidades/pagamentos e ciclo de vida. Registro das respostas sem nova pesquisa canônica/API, implementação, build ou testes; fontes anteriores mantêm datas/limites. Linhagens/artes adiadas.


### M07-06E/F/G propostos — preparo e encerramento — 21/10/2026

Reconsultados BL-19/帰刃, linhas 57–61, e BL-26/破面, linhas 22–24: relação de liberação, corpo e capacidades seladas na arma. São verbetes oficiais do anime relativos à época de exibição, sem capítulo/episódio integral. Não estabelecem permissões de reação, duração de preparo, recuperação de gameplay ou regra universal de reversão. Não houve nova consulta a BL-03 ou APIs nesta rodada.

A–D aprovados. E/F/G PROPOSTOS na seção 12.1 de M07, aguardando três respostas: preparo breve cancelável para reagir; encerramento manual/retorno ao estado-base Arrancar; transições sem recuperação adicional só pela mudança. Alternativas e consequências registradas, como adaptações. Preservados condição/conquista, custos, fases e tempos de M06/M07. Corpo/arma, espaço e meios, ações dependentes, posse, prioridade de pagamentos e ciclo de vida continuam abertos. Nenhuma passagem por parede, conservação de benefício não financiado ou arma duplicada aprovada. Próximo: respostas e corpo/arma/espaço. Linhagens/artes adiadas; sem implementação, build ou testes de Minecraft.


### M07-06E/F/G aprovados — preparo e encerramento — 21/10/2026

Três respostas próprias: E — “Preparo cancelável para reagir (recomendado)”; F — “Encerrar manualmente e voltar ao estado-base (recomendado)”; G — “Transições sem recuperação adicional (recomendado)”. Seções 8.56–8.58 de M07; A–G aprovados, sete decisões sem perguntas pendentes. Preparação anterior preservada como histórico.

Preparo permite caminhar devagar/olhar e cancelar para reação disponível, sem recuperação extra ou custo de ativação; reação recusada conserva preparo válido. Encerramento voluntário respeita a fase e volta ao estado-base Arrancar, também após falta de manutenção. Conquista/Mastery e tempos permanecem, sem recursos devolvidos; nova liberação paga normalmente. Concluir/encerrar voluntariamente não acrescenta custo/recuperação só pela mudança. Revisão local não identificou conflito no recorte examinado; não aprova todos os complementos de ficha. Próximo: corpo/arma, espaço/meios e ações dependentes, depois posse, compatibilidades/pagamentos e ciclo de vida. Registro sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft; fontes mantêm datas/limites. Linhagens/artes adiadas.


### M07-06H/I/J propostos — corpo, arma e espaço — 21/10/2026

Consulta dirigida de BL-14/Pierrot, entradas Tiburón (linhas 84–90) e Gamuza (104–110), sobre arma cobrindo a mão direita e corpo semelhante a centauro com arma semelhante a lança. Sustenta diversidade, não mãos/slots de Minecraft ou escolha dessas linhagens para a primeira entrega. BL-19/BL-26 reconsultados; BL-05/Characters JP retornou 403. Verbetes oficiais do anime relativos à época de exibição; textos japoneses interpretados, sem capítulo/episódio integral. Nenhuma regra de outro jogo ou página secundária foi adotada.

A–G aprovados. M06-08C/F/G já define perfis funcionais por etapa; consolidação sem nova votação. H/I/J PROPOSTOS: arma conforme poder e uso de itens compatíveis mantendo forma; validar espaço na posição atual ao iniciar/concluir; passagem exigir também espaço do corpo-base na postura compatível. J limita vantagem de acesso das formas pequenas para permitir retorno básico sem teleporte ou manutenção gratuita; hitbox de combate e medidas não são definidos. Alternativas registradas; três respostas pendentes. Inventário, poses/meios, intervenções do mundo, posse/perda, armadura, compatibilidades/pagamentos e ciclo de vida continuam abertos. Próximo: respostas, depois contratos restantes; linhagens/artes adiadas. Sem APIs, implementação, build ou testes de Minecraft.


### M07-06H/I/J aprovados — arma, itens e espaço — 21/10/2026

Três respostas próprias: H — “Arma conforme o poder; itens compatíveis permitidos (recomendado)”; I — “Exigir espaço na posição atual (recomendado)”; J — “Passagem preserva espaço do corpo-base (recomendado)”. Seções 8.59–8.61 de M07; A–J, dez decisões aprovadas, sem perguntas pendentes. Preparação anterior preservada como histórico.

Arma segue a ficha; incorporação não deixa cópia selada utilizável. Ferramentas/blocos compatíveis mantêm forma e manutenção, respeitando mãos/fases. Espaço é conferido antes/conclusão na posição atual; recusa conserva corpo/conquistas sem cobrar ativação, reposicionar ou destruir blocos. Passagem inclui corpo-base na postura compatível, limitando acesso de formas menores. Revisão local não identificou conflito no recorte examinado. Item incorporado/slots/inventário cheio, medidas/poses/meios, alterações forçadas do mundo, posse/perda/morte, armadura e efeitos dependentes ainda exigem ficha/decisão; não aprovados em bloco. Próximo: meios, posse/perda, equipamentos restantes, compatibilidades/pagamentos e ciclo de vida. Registro sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft; fontes mantêm datas/limites. Linhagens/artes adiadas.


### M07-06K/L/M propostos — meios e posse da arma — 21/10/2026

A–J preservados. K propõe liberar/encerrar no chão, no ar e na água com requisitos válidos, preservando gravidade, queda e riscos; L propõe guardar armamento separado no próprio inventário mantendo forma paga e suspendendo dependências de empunhadura; M propõe encerrar por perda efetiva da arma separada, sem transferir poder, recolher o item ou restaurar a forma ao recuperá-lo. Três respostas pendentes; alternativas e limites na seção 12.1. Não são decisões aprovadas.

Revisão dirigida de A–J e M06/empunhadura, fases, itens, efeitos e tempos. Reconsultados BL-19/BL-26 e entradas Tiburón/Gamuza de BL-14; fontes e limites na seção 4. Não comprovam políticas universais de inventário/meios. Item incorporado, slots/inventário cheio, armadura, alterações forçadas do mundo, efeitos dependentes, pagamentos e ciclo de vida ainda exigem decisão/ficha. Sem novas APIs, implementação, build ou testes de Minecraft. M07 segue em especificação; linhagens/artes adiadas.

Pesquisa desta rodada: entradas帰刃 de BL-19 (linhas 57–61), 破面 de BL-26 (22–24) e Tiburón/Gamuza de BL-14 (84–110), resumos oficiais do anime relativos à época de exibição. Sem capítulo/episódio integral; não determinam regras universais de apoio, posse, transferência ou recuperação de itens. As propostas são adaptações locais, não regras inferidas de outros jogos.


### M07-06K/L/M aprovados — meios e posse da arma — 21/10/2026

Três respostas próprias registradas como DECIDIDO nas seções 8.62–8.64: K — “Chão, ar e água, preservando queda e riscos (recomendado)”; L — “Guardar conserva a forma com manutenção (recomendado)”; M — “Perda de posse encerra a manifestação (recomendado)”. A–M, treze decisões aprovadas; nenhuma dessas perguntas pendente. Preparação anterior preservada como histórico.

K permite liberar/encerrar nos três meios válidos, preservando gravidade, trajetória, queda e riscos. L conserva a forma paga ao guardar a arma separada no próprio inventário, suspendendo dependências de empunhadura. M encerra por perda efetiva de posse, conservando conquistas sem transferir poder, recolher item ou restaurar a forma ao recuperá-lo; nova liberação paga normalmente. Incorporação não cria cópia nem equivale a perda de posse.

Revisão local confronta A–J, M07-05 e M06/empunhadura, corpo, fases, custos/tempos e efeitos autônomos. Não identificado conflito no recorte examinado. Complementos de ficha não foram aprovados em bloco: poses/volumes, intervenções do mundo, item incorporado/slots/inventário cheio, representação da perda/retorno e ações dependentes continuam pendentes. Próximo: equipamentos restantes, compatibilidades com Reiatsu, pagamentos, ciclo de vida e revisão. Atualizados módulo/handoff/mapa/base. Registro sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft; fontes mantêm datas/limites. M07 continua em especificação; linhagens/artes adiadas.


### M07-06N/O/P propostos — equipamento, Reiatsu e pagamentos — 21/10/2026

A–M preservados. N propõe quatro slots normais de armadura acessíveis/funcionais durante Resurrección, aparência adaptável e manutenção do mesmo item/desgaste; compatibilidade ainda não estava aprovada por M06-11A. O propõe coexistência básica com estados de Reiatsu, sinais/custos preservados e ofensiva válida encerrando Supressão. P propõe Resurrección → Liberação → ação para cobranças realmente coincidentes, tentando cada manutenção válida separadamente. Três respostas pendentes na seção 12.1; alternativas e complementos explicitados, sem aprovação presumida.

Revisão dirigida de A–M, M06-11A/B/J/K/L/R/S/T e comparação com Bankai J/K. Não importar combinações raciais ou regra de Bankai por associação. BL-26/Arrancar/Hierro e BL-19/Resurrección reconsultados; limites na seção 4. Não comprovam slots, Supressão ou prioridade de custos. Sem capítulos/episódios integrais, novas APIs, implementação, build ou testes de Minecraft. Próximo: respostas, ciclo de vida, efeitos dependentes e revisão estrutural; fichas, valores, inventário e apresentação continuam pendentes. M07 segue em especificação; linhagens/artes adiadas.

Pesquisa dirigida: BL-26/Pierrot, entradas破面/鋼皮 (linhas 22–33), e BL-19/帰刃 (57–61). Relação entre corpo, capacidades e defesa corporal; não determina os quatro slots de Minecraft, Supressão simultânea ou prioridade de manutenção. Resumos oficiais do anime relativos à época de exibição, interpretados do japonês; propostas rotuladas como adaptações.


### M07-06O/P aprovados; N aguarda esclarecimento — 21/10/2026

O — “Resurrección compatível com estados de Reiatsu (recomendado)”; P — “Resurrección → Liberação → ação (recomendado)”. Duas respostas próprias registradas como DECIDIDO nas seções 8.65/8.66. A–M e O/P aprovados, quinze decisões; N não integra esse conjunto.

Para N, resposta explícita “Não entendi como funcionariam as duas alternativas”. Não é aprovação nem rejeição. Seção 12.1 mantém a proposta e explica com exemplo hipotético de peitoral: A conserva efeitos elegíveis independentemente da aparência corporal; B desativa a peça que o perfil não admite, preservando o item. Recomendação A mantida; uma escolha pendente, sem nova pergunta sobre O/P.

Revisão local não identificou conflito entre O/P, A–M e M06/estados, fases, custos/tempos no recorte examinado. Fontes anteriores mantêm datas/limites; sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft. Armadura, ciclo de vida, efeitos dependentes, fichas/inventário/valores e revisão estrutural continuam pendentes. Atualizados módulo/handoff/mapa/base; linhagens/artes adiadas.


### M07-06N aprovado após esclarecimento — 21/10/2026

Resposta explícita: “A — Armadura continua funcionando (recomendado)”. N registrado como DECIDIDO na seção 8.67. A–P somam dezesseis decisões aprovadas; nenhuma dessas perguntas pendente. O pedido anterior de explicação e a comparação de alternativas permanecem como histórico, superados pela escolha própria.

Os quatro slots normais de armadura continuam funcionais durante Resurrección; peças equipadas protegem com encantamentos elegíveis e desgaste legítimo, mesmo com aparência adaptada/oculta. Não há cópia, reparo automático ou camada duplicada do equipamento. Hierro e defesas compatíveis seguem a composição/limite já aprovados em M06-11A/B. Não identificado conflito no recorte local examinado; a decisão não estende quatro slots a todo corpo Hollow nem aprova desenho final ou cálculo executável.

Atualizados módulo/handoff/mapa/base. Próximo: ciclo de vida, efeitos dependentes e revisão estrutural de Resurrección. Itens/slots da arma incorporada/inventário cheio, poses, mundo alterado, valores e apresentação continuam com fichas pendentes. Sem nova pesquisa canônica/API, implementação, build ou testes de Minecraft; fontes mantêm datas/limites. M07 segue em especificação; linhagens/artes adiadas.


### M07-06Q proposto; efeitos herdados e panorama das pendências — 21/10/2026

Reconsultados BL-19/Pierrot, entrada帰刃/レスレクシオン, linhas 57–61, e BL-26/Pierrot, entrada破面, linhas 22–24: liberação no corpo e capacidades seladas na arma. Arquivos oficiais do anime relativos à época de exibição, japonês interpretado, sem capítulo/episódio integral. Não estabelecem morte de gameplay, login, dimensão, reinício ou inventário do mod. Sem pesquisa de APIs.

A–P aprovados preservados. Q PROPOSTO em M07/seção 12.1, uma resposta pendente: encerrar preparo/forma no ciclo de vida, conservar condição/conquista/domínio e exigir nova liberação manual paga. Efeitos autônomos/dependentes e tempos consolidados pelos contratos já aprovados M06-11U/V/W e M07-06F/G/H/L/M/O/P, sem nova votação. Item incorporado/retorno/inventário cheio e política de morte continuam delegados; não aprovar keepInventory, recall ou cópia por encerrar representação.

M07/seção 12.3 registra o panorama solicitado: concluir/revisar Resurrección, formas Quincy, desenvolvimento Fullbringer e revisão comum; depois M08–M16, com pendências de fichas/valores de M05/M06. Corrigidas sínteses vigentes desatualizadas sem apagar históricos. Não é porcentagem do jogo pronto nem auditoria de implementação. Próximo: resposta Q e revisão estrutural, ainda sem declarar fechamento. Sem implementação, build ou testes; linhagens/artes adiadas.


### Q aprovado e Resurrección consolidada estruturalmente — 21/10/2026

Resposta própria: “Encerrar forma; retorno manual (recomendado)”. Q DECIDIDO em M07/seção 8.68: limpar preparo/forma no ciclo de vida, manter condição/conquistas/domínio e exigir nova liberação manual paga. A–Q: dezessete decisões aprovadas, sem perguntas pendentes. Revisão dirigida de contratos locais e M06-11U/V/W/X/Y não identificou conflito estrutural no recorte examinado; não é auditoria integral de lore/código.

M07/seção 15 consolida situações, entregas/responsáveis e condições de retorno. Fichas, custos/curvas, corpo/poses, mundo alterado, item incorporado/inventário cheio, técnicas, prova e apresentação continuam pendentes. Efeitos seguem suas dependências aprovadas; não importar keepInventory, recall ou regeneração universal. M07 segue EM ESPECIFICAÇÃO. Próximo: formas Quincy por pesquisa própria, depois Fullbringer e revisão comum. Sem nova consulta canônica/API nesta atualização de resposta; fontes mantêm datas/limites. Sem implementação, build ou testes; linhagens/artes adiadas.


## 17. Abertura M07-07 — Quincy — 21/10/2026

BL-16/ka reconsultado diretamente, entrada Letzt Stil, linhas 97–101; BL-23 pelo índice, trecho Uryū/Mayuri. BL-03 recuperado pelo índice nas entradas Vollständig/Schrift após abertura direta 403. BL-28/entrevista Quilge acessível diretamente. São resumos/verbetes/entrevista oficiais do anime, interpretados do japonês; não capítulos/episódios integrais. Distinguem técnica antiga, forma avançada e poder individual; não fornecem currículo ou ordem universal de conquista no mod. A sinopse do episódio 3 serviu só de contexto; episódio 14 retornou 403 e não fundamenta proposta. M07/seção 4 detalha os limites. Material secundário de busca não foi usado como fundamento.

M04/Q1–Q11 e M06-09C/fechamento da seção 39 orientam a herança, com as revisões de M05/M06 sobre técnicas/pontos. M07-07A/B PROPOSTOS na seção 12.1: caminhos separados, Vollständig regular compatível sem uso obrigatório de Letzt Stil; conquista por preparo, fonte apropriada e demonstração registrada. Duas respostas próprias pendentes. Acesso/filiação, relação com Schrift, domínio/execução e consequência/recuperação de Letzt Stil permanecem abertos; não importar custo, voo, absorção de entidades ou mudança de terreno de outra forma.

Próximo: respostas e acesso aos ensinamentos/relação Vollständig-Schrift antes da execução. Resurrección A–Q preservado; M07 segue EM ESPECIFICAÇÃO. Plataforma local reconfirmada: Minecraft 1.20.1, Forge 47.4.10, Java 17. Sem nova pesquisa de APIs, implementação, build ou testes de Minecraft; linhagens/artes adiadas.


### M07-07A/B aprovados — caminhos e conquista — 21/10/2026

Respostas próprias: “Caminhos separados, sem Letzt Stil obrigatório (recomendado)” e “Preparo, aprendizado e demonstração (recomendado)”. DECIDIDOS em M07/seções 8.70/8.71: Vollständig regular compatível e Letzt Stil separado/opcional; conquista de Vollständig por preparo, fonte apropriada e demonstração registrada, integrável a quest sem duplicar prova. Nenhuma dessas perguntas pendente.

Revisão dirigida preserva M04/Q4/Q6/Q7/Q8, revisões sobre Q11, um poder principal, fundamentos/M06-09C e ausência de requisito circular. Não identificado conflito no recorte examinado. Fonte/instituição, relação com Schrift, domínio/execução e consequências de Letzt Stil continuam abertos. Sem nova consulta externa, capítulo/episódio ou API nesta atualização; fontes anteriores mantêm datas/limites. Próximo: acesso aos ensinamentos e relação Vollständig-Schrift. Sem implementação, build ou testes; M07 segue EM ESPECIFICAÇÃO e linhagens/artes adiadas.


### M07-07C/D propostos — ordem e fonte de aprendizagem — 21/10/2026

BL-03 reconsultado nas entradas Vollständig/Schrift/Sternritter pelo índice após 403 direto; BL-28 reconsultado diretamente. Relação forma/poder e presença na elite Quincy não demonstram ordem universal, lealdade permanente ou currículo autônomo. Não confirmada via canônica universal de registros/mentores independentes. Sinopse do episódio 14 pelo índice somente como contexto; personagens EN com 403 não decisivo. Textos japoneses interpretados; sem capítulo/episódio integral ou API nova. Limites em M07/seção 4.

A/B preservados. C/D PROPOSTOS na seção 12.1, duas respostas pendentes: Schrift compatível antes da Vollständig daquele poder, sem liberação automática; fonte Quincy avançada contextualizada, sem filiação permanente ou apagamento da aprendizagem só por mudar facção. São adaptações propostas. Conteúdo/fonte/era, aquisição da Schrift e eventos efetivos de privação de poder exigem contratos próprios; não removem M04/Q7 por omissão. Depois domínio/confiabilidade e operação, Letzt Stil/consequências e revisão. Sem implementação, build ou testes; linhagens/artes adiadas.


### M07-07C/D aprovados — ordem e acesso — 21/10/2026

Respostas próprias: “Schrift compatível antes de Vollständig (recomendado)” e “Fonte avançada, sem filiação permanente (recomendado)”. DECIDIDOS em M07/seções 8.72/8.73: Schrift compatível conquistada/vinculada antes da forma do mesmo poder, treinamento próprio e fonte Quincy avançada contextualizada, sem filiação permanente. A–D: quatro decisões aprovadas, nenhuma pergunta pendente. Ordem e meios de acesso são adaptações; fonte/era/conteúdo e aquisição da Schrift ainda exigem seus contratos.

Revisão dirigida não identificou conflito com A/B, M04/Q6/Q7/Q8/RAC-20/23 e M05/M06 examinados. Um marcador de facção não concede/apaga aprendizado; evento efetivo de privação de poder segue pendente de contrato próprio. Sem nova consulta canônica/API ou capítulos/episódios nesta atualização; fontes mantêm datas/limites. Próximo domínio/confiabilidade, depois ativação/recursos e execução. Sem implementação, build ou testes; M07 em especificação e linhagens/artes adiadas.


### M07-07E proposto — confiabilidade e domínio — 21/10/2026

BL-03 reconsultado pelo índice, entrada Vollständig; abertura direta 403. BL-28 teve abertura direta, mas o trecho específico foi recuperado pelo índice, não pela extração direta desta rodada. Mantidos os limites: glossário/entrevista oficiais do anime, sem capítulo/episódio integral. Identificam a forma e distinguem capacidades; não determinam Mastery numérica ou falha por ativação. M07/seção 4 registra a pesquisa. Plataforma local reconfirmada em gradle.properties/build.gradle: Minecraft 1.20.1 / Forge 47.4.10 / Java 17, sem mudança de ambiente ou pesquisa nova de APIs.

A–D preservados. E PROPOSTO na seção 12.1, uma resposta pendente: manifestação básica confiável após conquista, sob requisitos válidos; Mastery própria por prática pertinente; técnicas/versões com aprendizado/domínio próprios. Três Proficiências e históricos existentes conservados, sem compra/cópia/AFK ou ganho por alternância sem prática. É adaptação proposta, sem conceder voo, Sklaverei ou todo o repertório. Depois ativação/recursos e execução, Letzt Stil/consequências e revisão. M07 segue EM ESPECIFICAÇÃO; sem implementação, build ou testes de Minecraft. Linhagens/artes adiadas.


### M07-07E aprovado — confiabilidade e domínio — 21/10/2026

Resposta própria: “Manifestação confiável + Mastery própria (recomendado)”. DECIDIDO em M07/seção 8.74: manifestação básica conquistada confiável sob requisitos válidos, sem falha aleatória por baixo domínio; Mastery própria por prática pertinente. Técnicas/versões com aquisição/domínio próprios; três Proficiências e históricos existentes preservados. A–E: cinco decisões aprovadas, nenhuma pergunta pendente. É adaptação aprovada, sem exigir Mastery anterior à primeira demonstração, comprar/copiar domínio ou conceder repertório completo.

Revisão dirigida não identificou conflito com A–D e a herança examinada. Sem nova consulta externa nesta atualização da resposta; fontes anteriores mantêm datas/limites. Ativação/recursos, instrumentos/mãos, preparo/reações, retorno, compatibilidades/ciclo de vida e Letzt Stil permanecem para as próximas rodadas. Práticas/curvas concretas exigem fichas. M07 em especificação; sem implementação, build ou testes de Minecraft. Linhagens/artes adiadas.


### M07-07F/G propostos — ativação e recursos — 21/10/2026

BL-03 reconsultado pelo índice, entrada Vollständig, após abertura direta 403; BL-28 com abertura direta e passagem específica recuperada no índice. Distinguem forma, tradição antiga e capacidades, sem determinar interface ou cobrança universal. Não foram lidos capítulos/episódios integrais; fontes secundárias não fundamentam as propostas. Limites em M07/seção 4. Sem nova pesquisa de APIs ou artes.

A–E preservados. F/G PROPOSTOS na seção 12.1, duas respostas pendentes: ativação manual independente da interface; Energia ao concluir e manutenção enquanto ativa, sem duração fixa universal. São adaptações propostas; custos não são copiados de Bankai/Resurrección. M05 mantém Reishi ambiental e uma reserva pessoal, sem abastecimento ilimitado/Sklaverei ou coleta manual por omissão. Valores/intervalos junto de tiros/Blut, foco/mãos, fases, retorno, compatibilidades e prioridade de pagamentos ainda precisam de contrato. Depois instrumentos/execução, Letzt Stil e revisão. Sem implementação, build ou testes; linhagens/artes adiadas.


### M07-07F/G aprovados — ativação e recursos — 22/10/2026

Respostas próprias: “Ativação manual independente da interface (recomendado)” e “Ativação + manutenção de Energia (recomendado)”. DECIDIDOS em M07/seções 8.75/8.76. Propostas preparadas em 21/10, aprovadas em 22/10. Pedido manual dentro/fora de Combat Mode; fechar interface não encerra forma. Energia uma vez na conclusão válida e manutenção enquanto ativa, sem duração fixa universal. Cancelamento anterior não cobra ativação; falta de manutenção encerra manifestação. A–G: sete decisões aprovadas, nenhuma pergunta pendente.

Revisão dirigida não identificou conflito com A–E e a herança M05/M06 examinada. Arco/Blut continuam desligando ao sair do modo; Reishi ambiental não vira segunda reserva, coleta manual, Sklaverei ou abastecimento ilimitado. Instrumentos/mãos, preparo/reações, retorno, compatibilidades/prioridade das manutenções e ciclo de vida ainda precisam de contrato, com valores/intervalos calibrados junto de tiros/Blut. Sem nova consulta externa ao registrar respostas; fontes mantêm datas/limites. Próximo instrumentos/foco e mãos. Sem implementação, build ou testes; M07 em especificação e linhagens/artes adiadas.


### M07-07H/I propostos — instrumentos e mãos — 22/10/2026

BL-16 reconsultado diretamente, entradas Cruz (86–90) e Letzt Stil/Sanrei (97–101); arquivo oficial do anime com recorte da época de exibição. BL-03 reconsultado pelo índice nas entradas pertinentes após 403 direto, distinguindo Vollständig da técnica antiga. Não confirmado nesses materiais um requisito instrumental universal da forma; não afirmar ausência de instrumentos/gestos em todas as cenas. Sem capítulo/episódio integral, novas APIs ou seleção de artes.

A–G preservados. H/I PROPOSTOS na seção 12.1, duas respostas pendentes. H recomenda sem foco/luva/arco universais para a forma, preservando instrumentos exigidos por poder e foco do arco básico. I recomenda mãos conforme arma/técnica, com itens compatíveis durante a forma paga; arco de duas mãos e guarda manual preservados. São adaptações propostas. Corpo/armadura, gestos/preparo, retorno, compatibilidades/prioridade das manutenções e ciclo de vida continuam abertos. Depois execução, Letzt Stil e revisão. Sem implementação, build ou testes; linhagens/artes adiadas.


### M07-07H/I aprovados — instrumentos e mãos — 22/10/2026

Respostas próprias: “Sem instrumento universal; requisitos por poder (recomendado)” e “Permitir itens compatíveis durante a forma (recomendado)”. DECIDIDOS em M07/seções 8.77/8.78. Forma sem foco/luva/arco como requisito universal, com instrumentos específicos por ficha; ferramentas/blocos/consumíveis compatíveis permitidos durante manifestação paga, respeitando corpo, mãos e fase. A–I: nove decisões aprovadas, nenhuma pergunta pendente.

Revisão dirigida não identificou conflito com A–G e M06-09J/L/T examinados. Arco básico continua exigindo foco e duas mãos; guardar é manual, sem simultaneidade com item incompatível ou reset de tempos. Perda de foco não encerra forma que não o exige, sem conceder arco gratuito ou reposição/cópia. Preparo/gestos, corpo/armadura, retorno, compatibilidades/prioridade de pagamentos e ciclo de vida continuam pendentes. São adaptações aprovadas; sem nova consulta externa nesta atualização, fontes mantêm datas/limites. Próximo preparo/reações e encerramento; Letzt Stil e revisão depois. Sem implementação, build ou testes; M07 em especificação e linhagens/artes adiadas.


### M07-07J/K/L propostos — preparo e encerramento — 22/10/2026

BL-03 reconsultado pelo índice na entrada dirigida de Vollständig, após 403 direto; BL-28/passagem Quilge recuperada pelo índice. Distinguem capacidades e técnica antiga, sem especificar cancelamento, recuperação ou retorno voluntário universais. Sem capítulo/episódio integral, novas APIs ou artes. Limites em M07/seção 4; J/K/L são adaptações propostas.

A–I preservados. J/K/L PROPOSTOS na seção 12.1, três respostas pendentes: preparo breve com caminhada e cancelamento para reação disponível; encerramento manual quando a fase permitir, retorno Quincy sem Vollständig preservando Schrift/conquistas; transições sem recuperação adicional. Não exigir mãos vazias universalmente nem permitir item/flecha/canal em paralelo ou cancelar gratuitamente uma ação anterior. G mantém cobrança na conclusão e manutenção; fases, tempos e efeitos autônomos continuam. Corpo/espaço, equipamento, compatibilidades/prioridade das manutenções e ciclo de vida ainda abertos. Depois esses contratos, Letzt Stil e revisão. Sem implementação, build ou testes; linhagens/artes adiadas.


### M07-07J/K/L aprovados — preparo e encerramento — 22/10/2026

Respostas próprias: “Preparo breve cancelável para reagir (recomendado)”; “Encerrar manualmente e preservar conquistas (recomendado)”; “Transições sem recuperação adicional (recomendado)”. DECIDIDOS em M07/seções 8.79–8.81. Preparo breve/caminhada lenta e cancelamento para reação disponível sem recuperação extra; pedido recusado preserva preparo válido. Encerramento manual quando fase permite, retorno Quincy sem Vollständig preservando Schrift/conquistas. Conclusão/saída sem taxa/recuperação adicional, mantendo custos de G e tempos anteriores. A–L: doze decisões aprovadas, nenhuma pergunta pendente.

Revisão dirigida não identificou conflito com A–I e M06 examinados. Não exigir mãos vazias universalmente nem preservar execução paralela ou cancelar gratuitamente ação anterior. Não repor recursos, importar perda de Letzt Stil ou reativar forma ao regenerar. Compatibilidades com Blut/Reiatsu, prioridade das manutenções, meios/corpo/espaço, equipamento, efeitos concretos e ciclo de vida ainda precisam de contrato. São adaptações aprovadas; sem nova consulta externa nesta atualização, fontes mantêm datas/limites. Próximo compatibilidades/prioridade das manutenções; depois demais contratos Quincy, Letzt Stil e revisão. Sem implementação, build ou testes; M07 em especificação e linhagens/artes adiadas.


### M07-07M/N/O propostos — Blut, presença e pagamentos — 22/10/2026

BL-03 reconsultado nas entradas dirigidas Blut/Vollständig pelo índice após 403 direto. Distingue aplicações exclusivas de Blut e a forma, sem comprovar todo contrato de simultaneidade ou cobrança. BL-28 reconsultado diretamente, passagem na linha 76 sobre capacidades de Quilge, sem currículo/regra universal de coexistência. Sem capítulo/episódio integral, novas APIs ou artes. M/N/O são adaptações propostas.

A–L preservados. M/N/O PROPOSTOS na seção 12.1, três respostas pendentes: uma aplicação aprendida de Blut junto da forma; Natural/Supressão/Liberação compatíveis, sem invisibilidade e com quebra por ofensiva válida; prioridade Vollständig → Blut → Liberação → ação para cobranças coincidentes. M06-11S/T e recorte de Arterie conservados; O propõe inserir forma ativa antes do Blut, não mudar Blut antes de Liberação. Inicial da forma paga como ação ao concluir, depois de obrigações devidas; sem pacote indivisível, cobrança parcial/antecipada ou religamento automático. Depois meios/corpo/espaço/equipamentos/ciclo de vida, Letzt Stil e revisão. Sem implementação, build ou testes; linhagens/artes adiadas.


### M07-07M/N/O aprovados — Blut, presença e pagamentos — 22/10/2026

Respostas próprias: “Vollständig com uma aplicação de Blut (recomendado)”; “Vollständig compatível com estados de Reiatsu (recomendado)”; “Vollständig → Blut → Liberação → ação (recomendado)”. DECIDIDOS em M07/seções 8.82–8.84. A–O: quinze decisões aprovadas, nenhuma pergunta pendente. M conserva uma aplicação conhecida de Blut e limites/custo próprios; N conserva estados exclusivos de Reiatsu, sinais e ofensiva válida encerrando Supressão. Sem ocultação garantida ou concessão automática de capacidades.

Revisão dirigida não identificou conflito com A–L e M06 examinados. O insere manutenção da forma ativa antes do Blut; conserva Blut antes de Liberação e manutenção antes da ação. Sem forma ativa/devida, M06-11T continua. Ativação inicial é custo de ação ao concluir, depois de obrigações já devidas, sem antecipar manutenção no preparo. Estado sem pagamento termina e ainda se tentam os demais válidos; revalidar ação após perda de forma. Sem nova consulta externa nesta atualização; fontes mantêm datas/limites. Meios/corpo/espaço/armamento/equipamentos, valores e ciclo de vida ainda pendentes; depois Letzt Stil e revisão. Sem implementação, build ou testes; M07 em especificação e linhagens/artes adiadas.


### M07-07P/Q/R propostos — meios, espaço e armadura — 22/10/2026

BL-03 reconsultado pelo índice, entrada dirigida de Vollständig: halo/asas são manifestação comum, com variação entre usuários. Abertura direta retornou 403; não foram incorporadas outras entradas recentes/promocionais. BL-28 teve nova abertura direta como referência da distinção das capacidades. Esses materiais não determinam medidas, colisão, condição de apoio ou armadura Minecraft. Mangá continua base; fontes oficiais do anime interpretadas, sem capítulo/episódio integral. Plataforma local reconfirmada: Minecraft 1.20.1 / Forge 47.4.10 / Java 17; sem pesquisa nova de APIs ou artes.

A–O preservados. P/Q/R PROPOSTOS em M07/seção 12.1, três respostas pendentes: chão/ar/água com queda/riscos mantidos; espaço do perfil funcional no início/conclusão, posição atual sem reposicionamento automático; quatro slots normais de armadura funcionais, mesmos itens/desgaste/encantamentos elegíveis e apresentação adaptável. Nenhuma aprovação importada de Resurrección. Critérios candidatos documentados, sem teste. Voo/Sklaverei precisam de capacidades/fichas próprias; não foram concedidos ou excluídos para todo poder. Após respostas: passagem/retorno seguro, dependências de armamento e ciclo de vida; Letzt Stil e revisão depois. Sem implementação ou build; M07 em especificação, linhagens/artes adiadas.


### M07-07P/Q/R aprovados — meios, espaço e armadura — 22/10/2026

Respostas próprias: “Chão, ar e água, preservando queda e riscos (recomendado)”; “Exigir espaço na posição atual (recomendado)”; “Armadura equipada continua funcionando (recomendado)”. DECIDIDOS em M07/seções 8.85–8.87. A–R: dezoito decisões aprovadas, nenhuma pergunta pendente. Permitir manifestação/encerramento nos três meios válidos, preservando gravidade/queda/riscos; conferir espaço funcional no início/conclusão sem reposicionamento automático ou cobrança de ativação inválida; conservar quatro slots normais de armadura com mesmos itens/desgaste/encantamentos elegíveis e aparência adaptável.

Revisão dirigida não identificou conflito com A–O, preparo/custos/mãos e M06-11A/B examinados. Aprovações são adaptações próprias do mod, sem universalização canônica ou transferência automática de Resurrección. Sem voo por aparência, limpeza da queda, cópia/reparo de item ou segunda proteção pela malha. Passagem/retorno seguro e intervenções do mundo continuam pendentes antes da entrega de perfis alterados; depois armamento e ciclo de vida, Letzt Stil e revisão. Voo/Sklaverei mantêm necessidade de pesquisa/ficha próprias. Nenhuma consulta externa nova ao registrar respostas; fontes mantêm datas/limites. Sem implementação, build ou testes; M07 em especificação, linhagens/artes adiadas.


### Contratos comuns e mudança de método — 22/10/2026

Solicitação do usuário: evitar decisões repetidas, adotando regras gerais sem perder qualidade. Aplicada consolidação contrato comum → perfil → exceções em M07/seções 7.1/7.2, mapa, handoff e skill. Mantidos registros detalhados, aprovações, datas e cobertura; consolidação documental não cria nova lei canônica nem comprova implementação. Custos/retornos, natureza permanente, instrumentos e equipamentos distintos permanecem na matriz. Futuras rodadas apresentam pacote e perguntam apenas diferenças reais/expansões/consequências/conflitos.

G01/G02 PROPOSTOS em M07/seção 12.1, duas respostas gerais pendentes. G01 estende padrão de ciclo de vida às manifestações temporárias pertinentes, incluindo Vollständig, preservando condições/conquistas e consequências especiais. G02 estende passagem com espaço para corpo-base às mudanças temporárias de corpo; formas pequenas perdem acesso a passagens incompatíveis com a base. Não aprovados por mudar o método. Após respostas, perfis cobertos referenciam as decisões, sem nova votação de cada evento/raça. Dependências de armamento já seguem contratos comuns C02/C09 e H/I. Nenhuma consulta externa/API nova nesta consolidação; pesquisas mantêm datas/limites. Sem implementação, build ou testes; fichas/evidência técnica continuam pendentes.


### G01/G02 aprovados; Vollständig consolidado estruturalmente — 22/10/2026

Respostas próprias: “Encerrar formas temporárias; retorno manual (recomendado)”; “Preservar espaço para o corpo-base (recomendado)”. DECIDIDOS em M07/seções 7.3/7.4. Padrões de ciclo de vida e passagem/retorno agora cobrem Vollständig e futuros perfis temporários pertinentes; fichas os referenciam sem nova votação. Condições permanentes e consequências especiais preservadas; não curar Letzt Stil/quebra por limpar manifestação nem conceder recall/reparo de item.

Revisão estrutural de Vollständig em M07/seção 15: dezoito decisões específicas A–R + padrões G01/G02, sem perguntas pendentes desse conjunto ou conflito identificado no recorte examinado. Fichas de fonte/quest, repertório, corpo/poses/alterações do mundo, instrumentos/itens, custos e integração continuam com responsáveis; nenhum cenário executado. Letzt Stil ainda precisa de aquisição/execução/consequência/recuperação antes de fechar as formas Quincy. Próximo seu pacote de diferenças, depois revisão Quincy e Fullbringer. Sem nova consulta externa/API ao registrar respostas; fontes mantêm datas/limites. Sem implementação, build, teste ou aceite; M07 em especificação.


### Letzt Stil — pesquisa dirigida e propostas M07-08A/B/C — 22/10/2026

Reabertos diretamente BL-16 (https://pierrot.jp/title/bleach/keyword_ka.html), entrada 滅却師最終形態, linhas 97–101, e BL-23 (https://pierrot.jp/title/bleach/playback_03.html), retrospectiva 42–63, linhas 46–49. Sanrei/retirada, uso extremo, perda e recuperação são o suporte factual deste recorte oficial do anime, marcado pela época de exibição. Nenhum capítulo/episódio integral lido. Prazo literal e procedimento anatômico não viram exigências de gameplay. BL-03 (https://bleach-anime.com/keyword/) retornou 403; índice dirigido recuperou distinção Vollständig/Letzt Stil. Não usar resultados secundários, demais entradas mutáveis ou silêncio de uma sinopse como prova universal.

M07/seção 12.1 apresenta A aprendizado Sanrei; B janela máxima breve com Energia ao concluir/sem manutenção básica, compromisso só na conclusão e sem sobrepor Vollständig; C perda persistente de acesso Quincy com recuperação própria, sem apagar atributos ou domínio. Três respostas pendentes. Acesso sem Schrift/facção permanente, custos/duração, coexistências, bloqueio de técnicas e repetibilidade são adaptações PROPOSTAS. Retirada comum da luva não ativa; nova liberação exige recuperar acesso e refazer prontidão, sem repetir toda conquista. Material/consumo/reposição de Sanrei, valores, benefícios e recuperação concreta permanecem com responsáveis antes da entrega.

G01/G02 já aprovados são herdados sem nova votação; limpar a manifestação não cura a consequência especial. Vollständig mantém fechamento estrutural, conjunto Quincy/M07 aberto. Critérios candidatos registrados, nenhuma execução. Plataforma local reconfirmada em gradle.properties/build.gradle; sem novas APIs, implementação, build ou testes. Próximo respostas e revisão das diferenças próprias restantes, depois Quincy/Fullbringer/revisão comum.


### Letzt Stil A/B/C aprovado; revisão das formas Quincy — 22/10/2026

Respostas explícitas: “Aprendizado próprio com Sanrei (recomendado)”; “Janela breve com compromisso (recomendado)”; “Perda recuperável por procedimento próprio (recomendado)”. M07/seções 8.89–8.91, com contexto 8.88; proposta original preservada em historico/2026-10-22-proposta-letzt-stil.md. Agora são adaptações aprovadas de gameplay, não novos fatos universais sobre Bleach. Aprendizado próprio; Energia na conclusão/janela limitada sem manutenção básica; perda de acesso Quincy ao término de ativação concluída, sem sobreposição a Vollständig; desenvolvimento preservado e recuperação específica, com preparo Sanrei próprio para repetir.

M07/seção 15 revisa A–C junto de Vollständig A–R, G01/G02 e contratos comuns examinados: nenhuma incompatibilidade estrutural identificada, nenhuma pergunta desses conjuntos pendente. Conquista não equivale a acesso disponível durante a suspensão; limpeza de manifestação não cura consequência. Formas Quincy de M07 consolidadas estruturalmente, com Sanrei/recuperação concreta, benefícios/valores, práticas/compatibilidades, corpo, UI e persistência ainda exigindo entregas e evidência. Risco não entra sem recuperação viável. Próximo Fullbringer por diferenças e revisão comum de M07. Sem nova pesquisa canônica/API, implementação, build ou testes ao registrar respostas; fontes anteriores mantêm datas e limites.


### Fullbringer — M07-09A/B proposto — 22/10/2026

Pesquisa direta: BL-11/entradas Fullbring e BL-17/treinamento/completude de Ichigo; BL-29/texto editorial Shonen Jump agora legível, sem revisar o acesso histórico malsucedido; BL-30/sinopse do episódio 358, exibido 31/01/2012; BL-31/fichas licenciadas de identidade/vetor. BL-05 retornou 403 nesta abertura. Referências sustentam princípio de matéria/afinidade, diversidade de vetores e desenvolvimento contextual; não número fixo de estágios, custo, Mastery de gameplay ou currículo universal. Não capítulos/episódios/novels integrais, nem importação de gameplay de Brave Souls. Trechos pertinentes e links em M07/seção 4; não generalizar o caso híbrido/extraordinário de Ichigo.

M07/seção 12.1: A marcos próprios de completude com Mastery contínua; B custo conforme manifestação e técnica, sem manutenção/transformação universal. PROPOSTOS, duas respostas pendentes; perfil operacional adicional de B incluído no pacote, sem perguntas repetidas. Herança F2/F3/F4/F6/F12/F13, M06-10A–F e C/G01/G02 preservada. Vetor não é sempre item; fundamentos não exigem objeto pessoal no prólogo; domínio não é comprado; transferir item não transfere poder. Fichas de perda/reposição e troca do principal e prioridades entre efeitos sustentados continuam abertas, com responsáveis; não fechar Fullbringer/M07 por associação. Depois respostas, diferenças reais restantes e revisão comum. Plataforma local reconfirmada sem novas APIs; nenhuma implementação, build ou teste. Formas Quincy mantêm fechamento estrutural; linhagens/artes adiadas.


### Fullbringer A/B aprovado; vetor revisado e troca C proposta — 22/10/2026

Respostas “Marcos próprios e Mastery contínua (recomendado)” e “Custos por manifestação e técnica (recomendado)”, M07/8.93–8.94. A/B passam a adaptações aprovadas, com cobertura operacional de B restrita às manifestações pertinentes. Proposta original preservada em historico/2026-10-22-proposta-fullbringer-ab.md. Revisão de vetor/contratos na seção 15: requisitos reais, vínculo, conhecimento e disponibilidade distintos; nenhuma incompatibilidade identificada no recorte examinado. Casos de item/equipamento/corpo e recuperação permanecem com fichas M07/M11/M16, sem nova votação genérica ou concessão de recall/indestrutibilidade.

M07-09C PROPOSTO em 12.1: troca contextual fora de combate, histórico de domínio separado por poder, sem cópia/sobreposição ou troca por item; uma resposta pendente. Reconsultado diretamente BL-17 (https://pierrot.jp/title/bleach/playback_16.html), passagens 63/121/145 sobre eventos específicos de transferência/perda; não procedimento universal de troca voluntária. Sem capítulo/episódio integral. C concretiza uma possibilidade de M04/F13 como adaptação proposta, não afirmação canônica ou regra já aprovada. Prioridades de cobranças, efeitos longos, fichas de vetor e troca ainda têm entregas; Fullbringer/M07 não fechados. Próximo resposta C e revisão comum após diferenças reais restantes. Sem novas APIs, implementação, build ou testes; pesquisas anteriores mantêm datas/limites, formas Quincy preservadas.


### Fullbringer C aprovado e revisão comum de M07 — 22/10/2026

Resposta “Troca contextual, preservando domínio por poder (recomendado)”, M07/8.95. Fullbringer A–C consolidado estruturalmente: principal único, história de domínio própria indisponível quando antigo poder inativo, sem cópia/sobreposição; aquisição/reacesso contextual, fundamentos preservados. Proposta original em historico/2026-10-22-proposta-fullbringer-c.md. C passa a adaptação aprovada, sem tornar troca universal canônica ou estendê-la a outras raças.

M07/seção 15 registra revisão comum documental dirigida, fechamento ESPECIFICADO CONCEITUALMENTE e critérios de reabertura. Sem pergunta geral atual ou conflito impeditivo identificado no recorte examinado; nenhum cenário executado. Não confirma catálogo integral, detalhes de fichas, exceções, nova ordem universal de cobranças, implementação/teste/aceite. 12.2 centraliza entregas de fontes/repertórios, valores/conversão de máximos, provas/recuperação, corpos/retorno, itens/vetores, ordenação específica/efeitos persistentes, UI e dados/validação nos módulos responsáveis. Novas fichas não herdam aprovação de uma escolha de produto inédita. Próximo M08 — NPCs, mentores e facções. Sem nova consulta canônica/API ao registrar resposta e revisar contratos locais; fontes conservam datas/limites. Sem código/build/testes; linhagens/artes e exceções permanecem em etapa própria.


### Viabilidade de múltiplos personagens e revisão de prioridade — 22/10/2026

Consultada diretamente a documentação oficial [Forge 1.20.1 — Capabilities](https://docs.minecraftforge.net/en/1.20.1/datastorage/capabilities/), seções de anexação, persistência e sincronização: dados próprios podem ser anexados à entidade, serializados e sincronizados pelo mod; persistência em clone/respawn requer tratamento. Ela não fornece um sistema pronto de vários personagens. Context7 retornou a documentação oficial do ramo 1.21.x; utilizada somente para localizar conceitos, sem importar API ou migrar o projeto. A página primária 1.20.1 e os arquivos locais sustentam o recorte aplicável.

Leitura dirigida de PlayerData/PlayerProvider/CharacterData/CapabilityEvents/ConfirmCharacterC2S: um conjunto de progressão ligado ao jogador nos arquivos consultados; não gerenciador de perfis independentes demonstrado. Construir múltiplos personagens é tecnicamente viável por análise de arquitetura, com trabalho para inventários, posições, vínculos, quests, rede e integração com outros mods. Não é implementação nem teste, nem promessa de compatibilidade automática.

Usuário preferiu reset e recomeço por enquanto; REV-RAC27-01 adia múltiplos personagens. Resposta de posição própria preservada para a expansão em RAC-29B; RAC-29A adiado. Sem novas afirmações canônicas de Bleach.


## 18. Abertura de M08 e fechamento da revisão M04 — 22/10/2026

M04 consolidado conceitualmente: personagem único nesta etapa, confirmação antes do prólogo e reset NPC com alcance aprovado. REV-03 resolvido quanto à política; itens vinculados, corpo/local após reset, preços, retomada por era e migração concreta continuam com responsáveis e exigências antes da entrega. Não houve nova aprovação de teleporte, regra de item ou conversão automática.

M08 criado com 15 seções. RAC-12/PRG-06/08 e REV-SP-02 orientam guias, mentores, serviços, facções e aprendizado. M08-01A PROPOSTO: proteção dos NPCs essenciais de atendimento, sem estender a todo ator ou encontro; uma resposta pendente. Morte comum de um prestador, disponibilidade narrativa e representação de boss são assuntos separados.

**Pesquisa dirigida:** [Pierrot playback_01](https://pierrot.jp/title/bleach/playback_01.html), linhas 85–86 e 149: loja e apoio de Urahara; [playback_03](https://pierrot.jp/title/bleach/playback_03.html), linhas 45–50/83: orientação de Yoruichi e prática com Zangetsu, além de Renji; [playback_16](https://pierrot.jp/title/bleach/playback_16.html), linhas 63–67/98: treino Fullbring com aplicações e orientação. Consultados em 22/10/2026; sinopses oficiais do anime à época da exibição, japonês interpretado, sem leitura de mangá/episódio integral. Sustentam exemplos, não um currículo universal, professor obrigatório para toda skill, nem acesso permanente/proteção do mod. A contagem literal de dias e a trajetória excepcional de Ichigo não se tornam regras gerais. BL-23 e BL-17 conservam seus IDs; playback_01 identificado pela URL exata nesta rodada.

Plano histórico Docs/planejamento/plano-npcs-quests.md (191 linhas) e QuestNpcEntity.java lidos integralmente; a entidade protege contra dano, é imóvel e abre painel. Não demonstra mentor, reputação, reset ou recuperação completos. Catálogo e testes históricos não são validação atual; Ulquiorra de teste não é professor universal aprovado. Nenhum runtime, save, build ou teste alterado/executado.


### Proteção M08-01A aprovada; continuidade/compra propostas — 22/10/2026

Resposta explícita: “Proteger NPCs essenciais de atendimento (recomendado)”. M08/8.4 registra cobertura e fronteiras; proposta original arquivada. Não é prova de atendimento completo no runtime; código atual não aprova por si só toda a regra. M08 permanece em especificação.

M08/12.1 propõe M08-01B: fontes alternativas coerentes para funções essenciais, preservando aquisição canônica/contexto/marcos, sem escolher representação de conteúdo passado. M08/12.2 propõe M08-02A: aquisição SP de skills ensinadas por mestre na interface da fonte autorizada; prática/domínio autônomos; alternativa de compra no repertório pessoal depois do ensino. Duas decisões pendentes. São adaptações de produto a contratos locais já aprovados, sem nova afirmação canônica, pesquisa externa/API, código, saves ou testes nesta rodada. SP e Mastery mantêm REV-SP-02; serviços de risco/recuperação respeitam M07.


### M08 — Continuidade e compra aprovadas; contrato de ensino — 22/10/2026

Respostas “Fontes coerentes com a era (recomendado)” e “Comprar na interface do mestre (recomendado)” registradas em M08/8.5–8.6; propostas anteriores arquivadas. Cobertura e limites mantidos: fontes autorizadas/contexto de campanha, skills ensinadas por mestre, SP no desbloqueio e prática/Mastery autônomas. Marcos vigentes reconhecidos por outra fonte; não restaurar progresso apagado pelo reset.

M08/8.7 organiza os contratos de repertório/ensino de M06-03A–E/M06-08E, sem nova regra universal de treino/prova ou quest por técnica; 8.8 distingue serviços. M08-03A PROPOSTO em 12.1: gratuidade do serviço de reset, separado da perda de SP da jornada e da aquisição de skills/formas. Alternativa taxa em item/moeda comum; uma resposta pendente. Frequência, itens/recompensas repetidas e fonte/local concretos seguem pendentes. Sem nova consulta canônica/API ou mudança de runtime/saves/testes; é consolidação e proposta de adaptação sobre contratos locais.


### M08 — Gratuidade aprovada e abertura de relações/facções — 22/10/2026

Resposta “Reset sem taxa adicional (recomendado)” registrada em M08/8.9; proposta anterior arquivada. Gratuidade do serviço não altera perdas de M04, compras SP, outros serviços ou define frequência/retorno/itens. Camadas sociais de M02/PRG-08 e M04/RAC-16/17/18 consolidadas em 8.10, sem nova promoção, escala numérica ou sistema completo de crimes.

M08-04A PROPOSTO: vínculos institucionais compatíveis por grupo/era, sem filiação adversária irrestrita; alternativa de filiação única. M08-04B PROPOSTO: advertência para golpe isolado em NPC essencial protegido, consequências por insistência/gravidade; alternativa de efeito já no primeiro golpe. Duas respostas pendentes. A regra não se estende a todas as vítimas, mortes ou ações de quest; critérios, autoria e impactos de canal precisam de ficha antes da execução. São propostas de adaptação de contratos locais, sem nova afirmação ou pesquisa canônica/API, runtime, saves ou testes.


### M08 — Vínculos e advertência aprovados; ingresso/cargos em proposta — 22/10/2026

Respostas “Permitir vínculos compatíveis (recomendado)” e “Advertência; consequências por insistência (recomendado)” registradas em M08/8.11–8.12; propostas originais arquivadas. Cobertura/limites mantidos, sem assumir espionagem, dupla lealdade irrestrita, política de morte gratuita ou crimes por tick.

M08-04C PROPOSTO: saída regular contextual com possibilidade de reingresso condicionado, preservando história/domínio e encerrando cargo/permissões dependentes. M08-04D PROPOSTO: carreira pessoal coexistente nesta etapa e chefias/posições únicas futuras, com responsabilidades reais e sem substituição automática do elenco. São escolhas de adaptação/prioridade, não afirmações canônicas de que cargos singulares possam ser ilimitados. Duas respostas pendentes; títulos/matriz de facções, sucessão e NPCs concretos continuam próprios. Sem consulta canônica/API nova, runtime, saves ou testes.


### M08 concluído conceitualmente; retomada do checkout e análise de conteúdo — 22/10/2026

M08-04C/D aprovados: “Saída e reingresso contextuais (recomendado)” e “Carreira pessoal; cargos únicos depois (recomendado)”. Registrados em M08/8.13–8.14 com propostas preservadas; revisão dirigida de integração em §15, entregas pendentes em §12.1. M08 ESPECIFICADO CONCEITUALMENTE; próximo M09. Não é conclusão das fichas/catálogo/execução.

Checkout atualizado de 8ace652 para bb26be85fc3dac703f67e79e772fca5158eecdef. Conferência de código/assets/manifest mostrou três variantes Hollow e boss em GeckoLib, 41 IDs NPC com `PlayerModel`, seis itens-base de Zanpakutō e modelos adicionais, dois mundos customizados e Mundo Humano no Overworld do preset de História. `history-maps/` possui regiões de três mapas; não foi feita inspeção visual deles. Inventário antigo permanece histórico. Lista completa recebida foi analisada por grupos no [parecer](00-reconciliacao-implementacao-assets-2026-10-22.md), incluindo armas, habitantes, props, cosméticos, cenários, VFX e dicas técnicas; candidatos e prioridades não aprovados automaticamente.

Pesquisa complementar de 22/10: sinopse oficial Pierrot dos episódios 1–20, fichas licenciadas Brave Souls de Kon, Nnoitora e Orihime; fontes/trechos/limites no parecer. Contexto narrativo não vira spawn genérico; ficha resumida não resolve toda classificação/anatomia. Página TYBW retornou 403 e não foi tratada como lida. Sem nova API/dependência, implementação de mecânica, build/cliente/teste ou atualização de JAR instalado; runtime incorporado é upstream. Relatórios QA recebidos conservam seus cenários, datas e falhas próprias.

## 19. Conteúdo de M08 — catálogo funcional e primeiros contatos — 22/10/2026

Solicitação posterior do usuário: detalhar o que cada NPC, mentor e organização faz antes de abrir M09. O fechamento conceitual anterior permanece válido nas regras gerais; conteúdo individual em especificação no [catálogo de M08](08a-catalogo-funcional-de-npcs-e-faccoes.md). Conferidos integralmente ModEntities, NpcRoster, roster.json e o plano histórico: 41 IDs, agrupados documentalmente em 36 personagens/entradas narrativas de trabalho, sem alterar registros ou dados. Todos os IDs aparecem no catálogo; atribuições fora das respostas explícitas continuam candidatas/reservadas.

**M08-05A aprovado:** Rukia e Renji para atuação Shinigami, Uryū para tradição Quincy, Chad para prática Fullbringer e Nel para orientação em Hueco Mundo, como primeiros contatos canônicos após fundamentos e com contexto/era. **M08-05B aprovado:** prestador original dedicado ao reset, acesso viável às quatro origens, mantendo M04/M08-03A. As seis fichas iniciais não aprovam automaticamente repertório, quest, localização, frequência ou solução de perda de itens/retorno corporal. Perguntas/respostas exatas preservadas no histórico vinculado pelo catálogo.

Pesquisa dirigida CAT-S01–S09 em catálogo/4: fichas e sinopses oficiais Pierrot de Karakura, Gotei, Soul Society, Hueco Mundo, Espada, Visored, Fullbringers e trechos de treino/recuperação. Fontes complementares do anime, japonês interpretado, sem capítulos/episódios integrais. Site TYBW de personagens/glossário retornou 403 diretamente; entradas institucionais recuperadas pelo índice oficial de busca estão rotuladas com esse limite. Competência e papel na obra não provam disponibilidade docente universal. Saída institucional regular aprovada em M08 é adaptação de gameplay, sem alegar reprodução literal da disciplina do Gotei.

Próximo bloco: fichas de organizações/deveres/acesso e fontes iniciais originais; depois repertórios, serviços e continuidade por era. Não reabrir SP/Mastery ou demais contratos gerais. Plataforma não mudou; sem APIs/dependências novas, runtime, builds, cliente ou testes nesta rodada. Documentação anterior publicada em 3a37ca7; esta ampliação ainda é trabalho local. M09 permanece próximo módulo novo após o recorte de conteúdo solicitado; não é preciso antecipar todas as falas/campanhas de M13.


## 20. Conteúdo de M08 — Academia/Gotei e carreira aprovada — 22/10/2026

Catálogo/9.1–9.5 amplia a formação já aprovada de M04/RAC-07/12: papéis de atendimento da Academia, funcionamento institucional proposto do Gotei, mapa das 13 divisões, atividades e relações com fontes especializadas. Funções comprovadas e oportunidades de gameplay estão separadas; não atribuir uma especialidade exclusiva a cada unidade ou limitar as quatro disciplinas por lotação. As seis fichas anteriores e M08-05A/B permanecem.

**M08-06A DECIDIDO**, resposta “Preferência do jogador + aceitação da divisão (recomendado)”: divisão principal por vez, preferência do jogador após formação entre opções disponíveis, com requisitos/aceitação e transferência contextual. Alternativa: primeira lotação determinada pela campanha. **M08-06B DECIDIDO**, resposta “Deveres por atividades aceitas, sem cota diária (recomendado)”: carreira por atividades aceitas, sem cota diária/perda de cargo apenas por ausência. Alternativa: presença periódica obrigatória. Falhas de atividades assumidas, compromissos informados e escolhas de campanha mantêm regras próprias; não pausar o mundo no logout. Duas respostas recebidas em 22/10/2026; perguntas/opções/respostas preservadas no histórico vinculado pelo catálogo. Não estender o pacote a todas as organizações por omissão.

**Pesquisa complementar dirigida:** CAT-S02/03 e CAT-S09 reconsultados; CAT-S10/11 acrescentados em catálogo/4 com URLs e trechos. Glossários Pierrot sustentam deveres Shinigami e origem do Departamento de Pesquisa e Desenvolvimento; fichas exemplificam vínculos e especialidades. Índice oficial TYBW traz estrutura/transferências do Gotei, cultura da 11ª, assistência no episódio 8 e atribuição de Ryūnosuke/Shino a Karakura. Abertura direta de glossário/episódio retornou 403; não tratar como leitura integral. Anime como complemento identificado do mangá; processos jogáveis são adaptações, sem novo currículo universal.

Próximo: fontes especializadas/demais organizações; M08-06A/B consolidados. O restante das atividades/divisões/fontes candidatas não foi aprovado por associação. Ensino preciso, representantes por era, cargos, permissões, atividades, fontes das demais origens e serviços ainda precisam de fichas. Nenhuma mudança de plataforma/API, runtime, builds, testes ou nova publicação nesta continuação.


## 21. Conteúdo de M08 — organizações especializadas — 22/10/2026

Catálogo/9.6–9.10 detalha perfis propostos de Onmitsukidō, Corpo de Kidō e Pesquisa e Desenvolvimento, com fontes, entrada, atividades, resultados, limites e pendências. Ensino com especialista, colaboração delimitada e carreira formal não se confundem; antigo dirigente não concede automaticamente autorização atual. SP/requisitos/Mastery e contratos gerais anteriores preservados. M08-06A/B continuam aprovados; o escopo de deveres sem cota diária não foi estendido automaticamente a toda instituição ou compromisso.

**M08-07A DECIDIDO em 22/10/2026**, resposta “Adotar essas funções como base (recomendado)”: reconhecimento/infiltração/resgate para Onmitsukidō; contenção/barreiras/ensino especializado para Corpo de Kidō; investigação/análise/projetos técnicos para Pesquisa. Resposta recebida; nenhuma pergunta pendente. Pergunta/opções/resposta preservadas no histórico vinculado pelo catálogo. A aprovação cobre funções, sem promover cada atividade, professor, técnica, risco, preço, recompensa ou regra de acesso a decidido.

**Pesquisa CAT-S12/13:** Pierrot, episódios 206–212, trechos da criação da organização sob a 12ª e cargos históricos do Corpo; fichas Karakura/Tessai, Visored/Hachigen e Soul Society/Yoruichi/Akon/Rin; glossário か行, entradas Kanritai/Keigun/Pesquisa. Consulta em 22/10/2026, japonês interpretado, anime como complemento, sem obras/episódios integrais. Glossário TYBW abriu com 403; não tratado como lido integralmente. Competência em barreiras/cura não define todo currículo; Akon com Gigai não aprova sistema de fabricação. Nenhum dirigente atual do Corpo de Kidō foi confirmado para todas as eras.

Elenco local continua com os mesmos IDs: Tessai, Hachigen e Akon são candidatos ausentes, sem obrigar adicionar todos. Técnica híbrida, Shunkō, poderes pessoais, prisão/PvP e sistemas universais de furtividade/pesquisa não são concedidos por essas fichas. Kidō conserva Proficiência única e Masteries por técnica; Kaidō exige formação especializada, conforme M06. Próximo: fontes Quincy independentes/Wandenreich/Sternritter; M08-07A registrado nos três perfis. Plataforma reconfirmada 1.20.1/Forge 47.4.10/Java 17; sem APIs/dependências novas, runtime, builds, testes ou publicação nesta continuação.


## 22. Conteúdo de M08 — fontes Quincy e recuperação — 22/10/2026

Catálogo/9.11–9.16 separa tradição independente, fonte avançada, aquisição de poder principal, carreira imperial, elite e serviço de recuperação. M04/Q5–Q8, M06, M07-07B/C/D e M07-08A–C preservados; não reabrir SP/Mastery, independência inicial, aprendizado de Vollständig sem filiação permanente ou as consequências de Letzt Stil.

**M08-08A DECIDIDO**, resposta “Adotar essa distribuição (recomendado)”: fontes independentes preservam tradição/ensinam aplicações de Reishi; Wandenreich oferece formação militar/operações; Sternritter participam como especialistas, autoridades ou adversários nas campanhas dos poderes. **M08-08B DECIDIDO**, resposta “Ryūken como referência de recuperação (recomendado)”: primeira referência canônica após Letzt Stil, com motivo para cooperar, procedimento adaptado e continuidade por fonte qualificada quando a era exigir; recuperação solo sem capacidades suspensas. Duas respostas em 22/10/2026; perguntas/opções/respostas no histórico vinculado pelo catálogo/12.4–12.5. Currículo, ingresso, cargos, rito, método, custos e locais não aprovados por associação.

**Pesquisa CAT-S14–16:** Pierrot/Karakura, entradas Sōken/Ryūken; glossário か行, hospital e Ginrei Kojaku; Aniplex/visual de 04/07/2023, elite do Wandenreich; personagens/glossário TYBW pelo índice oficial, entradas Haschwalth/Wandenreich/Sternritter/Schrift; TV Tokyo/sinopse do episódio 14. Consultados em 22/10/2026, anime complementar ao mangá, sem cenas ou obras integrais. TYBW personagens/glossário retornaram 403 diretamente. Sōken falecido não se torna mestre vivo; Ryūken ter recuperado Uryū não prova serviço público ou currículo completo. Relação de Schrift com Yhwach não decide um rito universal, concessor substituto ou acesso tardio no mod.

Conferência dirigida por nomes nos registros/roster local: Uryū presente; Ryūken/Sōken/Yhwach/Haschwalth ausentes dos 41 IDs. Aprovação de papel não adiciona entidade ou impõe produzir todo o elenco imperial. Fonte avançada para Blut/Vollständig e alternativa concreta de recuperação permanecem pendentes. Não estender M08-06B a todo compromisso imperial; não criar retirada automática de poder por facção, PvP racial ou nova Proficiência. Próximo: fontes Fullbringer/Xcution; depois relações Hollow/Arrancar e rede Urahara/serviços. Plataforma confirmada 1.20.1/Forge 47.4.10; sem APIs novas, runtime, builds, testes ou publicação nesta continuação.


## 23. Conteúdo de M08 — fontes Fullbringer e Xcution — 22/10/2026

Catálogo/9.17–9.21 aplica fundamentos antes do poder principal, independência racial e aquisição/completude/troca de M04/F1–F4/F7/F12/F13, M06-10A–F e M07-09A–C. Fonte inicial concreta ainda pendente; Chad permanece contato aprovado. Xcution possui interesses e conflitos por era; não escola neutra obrigatória, nova classe ou currículo universal.

**M08-09A DECIDIDO**, resposta “Adotar esses papéis (recomendado)” em 22/10/2026: Ginjō para avaliar preparo e conduzir acordos; Jackie para exercícios de combate; Riruka/Yukio para desafios de treino em espaços dos próprios poderes. Condições de cooperação/era mantidas; participar não concede seus Fullbrings. A aprovação cobre papéis, não quatro quests, todo currículo, métodos de troca ou produção imediata de quatro NPCs. Pergunta/opções/resposta no histórico vinculado por catálogo/12.6; nenhuma pergunta pendente.

**Pesquisa CAT-S17/18:** Pierrot/Fullbringers e retrospectiva 343–366 reabertos, trechos de orientação/treino/conflito; TYBW personagens e sinopse THE HOLY NEWBORN pelo índice oficial. Sinopse exibe episódio 32 em URL id=33; registrar o trecho efetivamente retornado. Personagens com 403 direto; nenhuma obra/episódio/novel integral consultado. Competência/acontecimento não comprova atendimento amistoso ou currículo para toda era. Não atribuir a Ginjō/Tsukishima troca livre de poderes ou recuperação universal.

Chad/Ginjō presentes nos registros/roster; demais nomes do bloco ausentes dos 41 IDs, sem novas entidades. Reacesso mantém histórico próprio e um principal por vez conforme M07, mas fonte/procedimento concretos continuam pendentes. Treino em espaço não concede seu poder ao aluno; requer saída válida, itens, participantes e interrupções antes da entrega, sem escolher instâncias/rollback/morte não letal por omissão. Próximo: relações Hollow/Arrancar e estrutura Aizen/Las Noches/Espada; depois rede Urahara/serviços. Sem novas APIs/dependências, runtime, builds, testes, commit ou publicação nesta continuação.


## 24. Conteúdo de M08 — relações Hollow/Arrancar e regime de Aizen — 22/10/2026

Catálogo/9.22–9.26 aplica sobrevivência/predação, orientação independente e relações locais, separando Las Noches, regime, elite Espada e condição racial. Evolução/Arrancarização/Resurrección mantêm M04/H1–H9, M06-08 e M07-04/05/06; não há evolução por ingresso, cópia das habilidades da presa ou nova votação de SP/Mastery.

**M08-10A DECIDIDO**, resposta “Adotar essas funções (recomendado)”: fontes independentes orientam sobrevivência/caça; Nel conserva orientação local; Harribel participa de proteção/cooperação; Grimmjow atua como rival e avaliador. **M08-10B DECIDIDO**, resposta “Adotar essa organização (recomendado)”: operações/compromissos do regime e Espada como autoridades/rivais/adversários, ensino somente com competência/motivo. Respostas explícitas em 22/10/2026, catálogo/12.7–12.8 e histórico vinculado; nenhuma pergunta pendente. Não aprova todas as atribuições candidatas ou procedimentos, cargos, prazos e recompensas.

**Pesquisa CAT-S19–21:** [Shōnen Jump — retrospectiva Arrancar](https://www.shonenjump.com/xmedia/bleach/story/index4-3.html), parágrafos de conflito e notas Hueco Mundo/Espada; Pierrot/fichas Aizen, Espada e Arrancars, sinopses 132–151/152–167; TYBW/personagens pelo índice oficial. Consultados em 22/10/2026. Síntese oficial da editora não equivale ao mangá integral; anime complementar com estados temporais próprios. TYBW retornou 403 diretamente. Recorte sustenta diversidade das relações e mudanças de disponibilidade, não currículos ou cooperação universal. Situações posteriores do site não antecipam automaticamente o conteúdo dessas eras no mod.

Busca dirigida dos registros/roster preserva os 41 IDs e confirma os representantes já listados. Gin, Barragan, Lilynette, Pesche e Dondochakka ausentes, sem inclusão automática de assets. Predação exige classificação própria, inclusive para rival/treino/boss repetível; M08-06B não foi estendido a todo compromisso do regime. Fonte inicial e repertórios ainda pendentes. Próximo: rede Urahara/serviços e revisão de lacunas antes de M09. Linhagens/artes adiadas, Grimmjow primeiro caso depois. Sem APIs/dependências novas, runtime, builds, testes, commit ou publicação nesta continuação.


## 25. Conteúdo de M08 — rede Urahara e serviços — 22/10/2026

Catálogo/9.27–9.30 separa estabelecimento/rede, fontes de atendimento, matriz de serviços e disponibilidade por era. **M08-11A DECIDIDO**, resposta “Adotar essas funções (recomendado)” em 22/10/2026: Urahara para suprimentos/investigação/orientação, Yoruichi para mobilidade/combate corporal e Tessai para Kidō/barreiras. Pergunta/opções no histórico vinculado por catálogo/12.9. A resposta aprova as funções; currículos, produtos, procedimentos e preços não foram aprovados por associação.

**Pesquisa CAT-S22 em 22/10/2026:** [Pierrot — Karakura](https://pierrot.jp/title/bleach/chara.html), Urahara/Tessai/Jinta/Ururu; [Soul Society](https://pierrot.jp/title/bleach/chara_03.html), Yoruichi; sinopses 1–20 (loja/apoio), 42–63 (orientação/confronto) e 206–212 (cargos/exílio). Anime complementar identificado, japonês interpretado, sem leitura de mangá ou episódios integrais. Comércio/invenção e competências sustentam candidatos; todo currículo corporal/barreiras e disponibilidade para ensinar ao jogador não são fatos comprovados por essas sínteses.

Serviços herdados preservados: reset original gratuito, recuperação Letzt Stil com referência Ryūken, dano estrutural de Bankai por método próprio e reacesso Fullbring ainda com fonte/procedimento pendentes. A loja não recebe todos esses serviços automaticamente. SP para skills/formas não decide moeda ou estoque de produtos. Treino usa capacidades disponíveis e local com regras próprias; sem cura, instância ou recuperação de recursos presumidas. Representações Urahara/Yoruichi confirmadas; Tessai/Jinta/Ururu ausentes dos 41 IDs, sem registros novos. Plataforma continua 1.20.1/Forge 47.4.10/Java 17. Próximo: revisar lacunas M08 antes de M09; sem APIs/dependências novas, runtime, builds, testes, commit ou publicação nesta continuação.


## 26. Revisão do catálogo de M08 — 22/10/2026

[Relatório da revisão](08b-revisao-do-catalogo-2026-10-22.md): regras gerais e catálogo confrontados com contratos dirigidos de M03–M07. Sem conflito impeditivo identificado nesse recorte; não equivale a revisão exaustiva do cânone ou a rotas executáveis. M08-01–11 permanecem aprovados apenas em seus alcances; nenhum novo pacote de produto foi aprovado.

Oito lacunas: fontes iniciais concretas, repertórios, procedimentos especiais, cobertura por era, ficha operacional do reset, percursos de organizações, amostra de aquisição/prática e perfil de cada presença selecionada. Recomenda-se complementar fontes e repertório inicial das quatro origens antes de M09. Recuperações especiais e reacesso continuam condições anteriores ao risco/troca correspondente; diálogos completos, locais, preços finais, campanhas e realização técnica permanecem nos módulos responsáveis.

Corrigidas referências desatualizadas de status e interrupções na tabela de fontes do catálogo; histórico e respostas preservados. Não houve pesquisa externa nova nesta revisão; CAT-S01–22 conservam seus limites. Catálogo em especificação; alterações documentais locais, sem runtime, build, teste ou publicação nesta rodada.


## 27. Fontes iniciais e pequeno repertório de M08 — 22/10/2026

Catálogo/9.31–9.37 elaborado após autorização do complemento recomendado na revisão 08b. **M08-12A DECIDIDO**, resposta “Adotar essas quatro fontes iniciais (recomendado)”: instrutor original da Academia, Hollow veterano independente no Mundo Humano por interesse local, mentor Quincy independente sem parentesco obrigatório e orientador Fullbringer original. **M08-12B DECIDIDO**, resposta “Adotar o repertório inicial (recomendado)”: fundamentos/Shunpo; corpo/defesa/caça e Cero apenas a partir de Gillian por fonte qualificada; formação de arco/Hirenkyaku separados; Bringer Light e depois seu salto. Não exige comprar todo o conjunto para concluir a introdução. Perguntas/opções/respostas no histórico vinculado por catálogo/12.10–12.11; nenhuma pergunta pendente.

**CAT-S23:** reconsulta em 22/10/2026 a glossários Pierrot ha (Hirenkyaku/Fullbring), ka (Cruz) e sa (deveres), e retrospectiva 343–366. Anime complementar identificado, japonês interpretado, sem capítulos/episódios integrais. Fontes não comprovam esses professores originais ou esse currículo universal; são adaptações aprovadas. A pesquisa não bastou para selecionar um primeiro Kidō nominal; manter sua ficha pendente, sem atribuir técnica apenas pela numeração.

Revisão do recorte em catálogo/9.37: SP/aquisição/uso/Mastery e estágios preservados; não identificado conflito impeditivo no alcance examinado. Práticas concretas, SP/equipamento, primeira aplicação delimitada de matéria, cobertura por era, fonte avançada de Cero e execução permanecem entregas antes do conteúdo correspondente. Não presumir preço, gratuidade, segunda compra de salto ou domínio recebido. Próximo módulo de planejamento: M09, sem obrigar currículo avançado inteiro antes dos encontros. Catálogo permanece em especificação; sem runtime, build, teste, commit ou publicação nesta rodada.


## 28. Abertura de M09 — perfis e força no mundo aberto — 23/10/2026

[M09](09-mobs-bosses-e-encontros.md) criado no template de 15 seções. **M09-01A DECIDIDO**, resposta “Separar natureza, combate e papel no encontro (recomendado)”; **M09-01B DECIDIDO**, resposta “Força própria por perfil e contexto (recomendado)”. Classificação de gameplay e política do mundo aberto são adaptações aprovadas, sem criar novos estágios raciais, valores, escala cooperativa ou primeira leva de produção. Perguntas/opções/respostas no histórico vinculado por M09/1; nenhuma pergunta desta rodada pendente.

**Pesquisa ENC-S01–03**, consultada em 23/10/2026: [Pierrot — glossário ha](https://pierrot.jp/title/bleach/keyword_ha.html), Hollow/Plus; [retrospectiva 1–20](https://pierrot.jp/title/bleach/playback_01.html), confrontos, fuga/afastamento e Menos; [glossário ma](https://pierrot.jp/title/bleach/keyword_ma.html), classes/evolução na descrição histórica. Anime oficial complementar, resumos e japonês interpretado; não capítulos/episódios integrais. Não fornece tiers, parâmetros de IA, política de força ou revisão dos contratos de evolução já aprovados. Mantidos IDs anteriores BL-11/13/25; ENC identifica o recorte dentro de M09, não nova autoridade canônica.

**Conferência local:** lidos HollowVariant, HollowEntity, HollowFlightCombatGoal e HollowBossEntity, registros, JSON de spawn e loot. Três variantes no tipo Hollow, comportamento aéreo por circulação/aproximação/ataque/retirada e boss com golpe/investida/impacto. Vida atual 20/6/14/100 HP é evidência de código, não balanceamento aprovado. Spawn Overworld peso 80/grupo 2–6 e alvos jogador/aldeão/golem ainda não são políticas finais de design; loot examinado vazio não prova ausência de todas as recompensas por outros sistemas. Nos arquivos de perfil lidos não localizada escala por BP; não auditoria de todo modificador externo. Plataforma declarada permanece 1.20.1/Forge 47.4.10/Java 17.

Próximo: detecção, escolha/perda de alvo e perseguição; depois terreno, ciclo e resultados dos encontros. Amostra de variantes/boss é candidata e dependências M08/M10–M16 seguem explícitas. Sem nova API/dependência, alteração de runtime/save, build, cliente, testes, commit ou push nesta rodada.


## 29. M09 — detecção e perseguição propostas — 23/10/2026

M09/8.4–8.7 e 12.4–12.5 elaborados: M09-02A recomenda visão/percepção por capacidade do perfil, 02B busca limitada pela última informação válida e retorno na perseguição comum. **PROPOSTOS, duas respostas pendentes**; 01A/B e M06-11F/G/I/J/K preservados. Supressão já não concede invisibilidade/perda automática de aggro; aplicação aos sentidos/reações concretas dos inimigos exige este bloco e suas fichas. Hostilidade, caminho, posição percebida e autorização de acerto são responsabilidades distintas; sem audição universal, seleção de todos os alvos ou cura/reset do encontro por associação.

**ENC-S04, consulta em 23/10/2026:** [Pierrot ha](https://pierrot.jp/title/bleach/keyword_ha.html), Pesquisa como leitura de Reiatsu/características da alma; [retrospectiva 1–20](https://pierrot.jp/title/bleach/playback_01.html), atração de Hollows pela capacidade espiritual de Ichigo. Reconsulta de ENC-S01/02, anime complementar/resumos, não episódios/mangá integrais. Não comprova percepção exata, alcance, custo ou comportamento universal de perseguição; modelo proposto é adaptação.

Conferência dirigida de seleção/alcance/acerto em HollowEntity/HollowBossEntity e retenção/uso de posição em HollowFlightCombatGoal: código não demonstra a oposição sensorial ou busca por pistas propostas. Leitura das classes locais, sem inspeção integral dos objetivos vanilla herdados, execução de teste ou alteração de navegação. Próximo: respostas e alvos/terreno. Tempos, sentidos concretos, parâmetros, comunicação, lifecycle e recompensas continuam pendentes; sem nova API, runtime, build ou publicação.


### M09-02A/B aprovados — 23/10/2026

Respostas explícitas “Visão e percepção conforme o perfil (recomendado)” e “Busca limitada e retorno à área (recomendado)”. M09/8.4–8.7 e 12.4–12.5 passam a contratos aprovados, com perguntas/opções/respostas no histórico próprio. Nenhuma pergunta desta rodada pendente. Detecção, pista aproximada, memória, hostilidade e autorização de ataque continuam distintos; perseguição comum recebe busca/retorno limitado, sem aprovar reset ou cura. Chefes, sentidos e parâmetros concretos, alvos e terreno exigem fichas próprias. Próximo: elegibilidade/prioridade de alvos e terreno. Conferência documental dirigida; sem nova pesquisa de cânone/API, runtime, build, testes ou publicação. M09 continua em especificação.


## 30. M09 — hostilidade e escolha de alvos propostas — 23/10/2026

[M09/8.8/8.9 e 12.6/12.7](09-mobs-bosses-e-encontros.md#88-hostilidade-dos-mobs-comuns--m09-03a-decidido) elaborado sob RAC-16/17, M08-01A/04B e M09-02A/B. **M09-03A/B PROPOSTOS**, duas respostas pendentes. A recomenda comportamentos comuns agressivo, territorial ou reativo; B recomenda alvo estável com motivos explícitos para troca. Perguntas/opções no histórico vinculado pelo módulo. Relações e percepção são entradas distintas; dados administrativos não entregam posição secreta e troca de alvo não apaga compromissos de execução.

São recomendações de adaptação, sem pesquisa externa nova ou nova atribuição canônica. ENC-S01–04 conservam datas/limites. Categorias concretas de alvos, parâmetros, políticas de chefes, coordenação e dano de área continuam pendentes. Próximo: respostas e terreno/construções, depois execução/ciclo. Sem API/dependência, runtime, build, testes ou publicação; M09 em especificação e ampliações locais.


### M09-03A/B aprovados — 23/10/2026

Respostas explícitas “Hostilidade agressiva, territorial ou reativa por perfil (recomendado)” e “Alvo estável, com motivos definidos para trocar (recomendado)”. M09/8.8/8.9 e 12.6/12.7 passam a contratos aprovados, com perguntas/opções/respostas no histórico próprio. Nenhuma pergunta pendente. Cobertura: motivos variados de hostilidade e seleção estável com troca motivada para mobs comuns. Relações, percepção, alvos, execução e recompensa permanecem distintos; não aprova lista concreta de alvos, parâmetros, coordenação, barra de ameaça ou política universal de chefe. Próximo: terreno/construções e falta de caminho. Sem pesquisa externa/API nova, runtime, build, teste ou publicação; M09 permanece em especificação.


## 31. M09 — terreno e construções propostos — 23/10/2026

[M09/8.10–8.12 e 12.8/12.9](09-mobs-bosses-e-encontros.md#810-construções-e-alteração-de-blocos--m09-04a-decidido) elaborado: **M09-04A/B PROPOSTOS**, duas respostas pendentes. A recomenda não alterar blocos por padrão nos mobs comuns, com exceções em técnicas/encontros definidos; B recomenda tentativas limitadas e recuo sem solução, mesmo com percepção do alvo. Preservados 01–03, construção/grind ativo, colisão e separação entre informação/caminho/acerto. Não aprova técnica destrutiva, proteção de terreno, parâmetros, cura, invulnerabilidade ou boss universal.

Relidos trechos locais de aproximação/circulação/segmento do voador, correção terrestre e investida do boss: checagens de colisão/espaço/apoio não demonstram política completa de terreno/retorno. Sem API ou pesquisa canônica nova; fontes anteriores preservadas. Sem runtime, build, testes ou publicação. Orientação do usuário: registrar respostas e continuar com próximas perguntas sem aguardar novo “prossiga”; escolhas novas não são aprovadas por essa autorização. Próximo: respostas, execução/contrajogo e ciclo, preservando fichas de meios/retorno bloqueado e demais dependências.


### M09-04A/B aprovados — 23/10/2026

Respostas explícitas “Sem alteração de blocos por padrão; exceções definidas (recomendado)” e “Tentativas limitadas e recuo sem solução (recomendado)”. Contratos aprovados em M09/8.10–8.12 e 12.8/12.9; perguntas/opções/respostas preservadas no histórico. Nenhuma pergunta pendente do bloco. Cobertura comum, sem técnica destrutiva, parâmetros, recuperação automática ou política universal de boss. Próximo execução/contrajogo, com perguntas após respostas sem novo pedido de avanço. Sem pesquisa externa/API nova, runtime, build, testes ou publicação; módulo em especificação.


## 32. M09 — execução e compromisso direcional propostos — 23/10/2026

[M09/8.13/8.14 e 12.10](09-mobs-bosses-e-encontros.md#814-momento-de-fixação-da-direção--m09-05a-decidido): herança de execução consolidada, **M09-05A PROPOSTO**, uma resposta pendente. Recomenda ajuste limitado no início do preparo e fixação na parte final antes da fase ativa; alternativa fixa desde o início. Sem nova votação de dano/controle, Recovery ou troca de alvo, sem parâmetros ou poder concreto. Proposta de adaptação, não afirmação canônica universal.

Conferência dirigida: golpe comum guarda/revalida alvo, alcance e visão; boss registra direção/ponto no início. Mecanismos locais distintos não comprovam uma política comum. Nenhuma implementação/teste/aceite da proposta, pesquisa externa/API nova, build ou publicação. M09-01–04 preservados. Próximo: resposta e perguntas de ciclo dos encontros sem novo pedido de continuação; fichas concretas continuam pendentes.


### M09-05A aprovado — 23/10/2026

Resposta explícita “Ajustar no início e fixar antes do golpe (recomendado)”. Contrato aprovado em M09/8.14 e 12.10; pergunta/opções/resposta no histórico. Nenhuma pergunta pendente do bloco. Direção com ajuste inicial limitado e fixação antes da fase ativa, sem parâmetros, geometria ou técnica concreta por associação. M09-01–04 preservados; próximo ciclo dos encontros com perguntas contínuas. Sem nova pesquisa externa/API, runtime, build, testes ou publicação.


## 33. M09 — início e falha coletiva propostos — 23/10/2026

[M09/8.15–8.18 e 12.11/12.12](09-mobs-bosses-e-encontros.md#815-ciclo-do-encontro--organização-conceitual): **M09-06A/B PROPOSTOS**, duas respostas pendentes. A recomenda início deliberado de provas/confrontos preparados; B continuidade com participante apto, falha coletiva e prazo para abandono completo. Não generaliza a emboscadas/eventos nem a toda agressão comum; objetivos especiais podem declarar outra condição. Reentrada, party/terceiros, parâmetros, relógio, resultado simultâneo, recursos/retomada e lifecycle continuam pendentes.

Herança de M03/STR-03: vitória jogável sem aliado canônico e representação de combate distinta do prestador; conquistas e despesas continuam com seus contratos, sem recompensa ou reset presumido. Leitura dirigida documental; sem nova pesquisa externa/API, runtime, build, testes ou publicação. M09-01–05 preservados; perguntas continuam automaticamente após respostas, com próximas escolhas ainda pendentes.


### M09-06A/B aprovados — 23/10/2026

Respostas explícitas “Confirmar o início das provas e confrontos preparados (recomendado)” e “Continuar com participantes aptos; falha coletiva (recomendado)”. Contratos aprovados em M09/8.16/8.17 e 12.11/12.12; perguntas/opções/respostas no histórico. Nenhuma pergunta pendente deste bloco. Início deliberado para confronto preparado e continuidade/falha coletiva no seu alcance; sem reentrada, parâmetros, escala, reset de recursos ou resultado individual presumidos. M09-01–05 preservados; próximas perguntas de recursos/retomada. Sem pesquisa externa/API nova, runtime, build, testes ou publicação.


## 34. M09 — recuperação e nova tentativa aprovadas — 23/10/2026

Duas perguntas enviadas e respostas explícitas recebidas antes do registro documental: “Recuperação gradual após período sem combate (recomendado)” e “Nova tentativa com estado inicial; checkpoints explícitos (recomendado)”. **M09-07A/B DECIDIDOS**, em 8.19/8.20 e 12.13/12.14; histórico próprio preserva os textos. Recuperação natural de inimigos comuns é ampliação de gameplay sob M05/17/24; não importa números ou cura especial. Estado inicial da nova tentativa não redefine confronto ativo, cura jogador ou restaura terreno. Parâmetros, recursos concretos e lifecycle/relógios continuam pendentes. Próximas perguntas: descarregamento/reinício e relógios. Sem nova pesquisa externa/API, runtime, build, testes ou publicação; M09 em especificação e 01–06 preservados.


## 35. M09 — interrupção e relógios propostos — 23/10/2026

[M09/8.21/8.22 e 12.15/12.16](09-mobs-bosses-e-encontros.md#821-interrupção-da-prova-por-descarregamentoreinício--m09-08a-decidido): **M09-08A/B PROPOSTOS**, duas respostas pendentes. A recomenda encerrar prova ainda não resolvida ao descarregar/reiniciar e iniciar outra manualmente por 06A/07B; B recomenda tempo de simulação para mobs comuns salvos, sem cura offline/retomada de golpe. Relógios de personagem M06 preservados; não presumir persistência eterna, despawn, limpeza de terceiros, reentrada ou carregamento forçado. Recortes de gameplay, sem nova alegação canônica/API ou auditoria técnica integral. Sem runtime, build, testes ou publicação; M09-01–07 preservados. Próximo: respostas, consolidação estrutural e autoria/recompensas/consumo.


### M09-08A/B aprovados — 23/10/2026

Respostas explícitas “Encerrar como interrompida; nova tentativa manual (recomendado)” e “Pausar tempos fora de simulação (recomendado)”. Contratos aprovados em M09/8.21/8.22 e 12.15/12.16; perguntas/opções/respostas no histórico. Nenhuma pergunta pendente deste bloco. Interrupção da tentativa dirigida ainda não resolvida exige novo início; tempos do mob comum salvo pausam fora de simulação, sem alterar relógio do personagem. Ciclo estrutural não resolve despawn, retorno bloqueado, resultados simultâneos ou reentrada/terceiros. Próximo autoria/recompensas/consumo. Sem nova pesquisa externa/API, runtime, build, testes ou publicação; M09-01–07 preservados.


## 36. M09 — contribuição e prêmios propostos — 23/10/2026

[M09/8.23–8.26 e 12.17/12.18](09-mobs-bosses-e-encontros.md#823-revisão-estrutural-do-ciclo--alcance-e-entregas): revisão estrutural do ciclo preserva entregas; **M09-09A/B PROPOSTOS**, duas respostas pendentes. A recomenda reconhecimento de contribuição útil fora da party em encontro comum aberto; B prêmio principal único por personagem e fontes repetíveis separadas/configuradas. Não redefine último golpe, compra por SP, Mastery prática ou grind ativo; valores/distribuição e oportunidade/destino de alma não estão aprovados. Reset de jornada e flags de prêmio exigem integração M04/M11/M12 para preservar viabilidade e evitar multiplicação de itens comuns.

Leitura dirigida de M02, M05 e M07-04A/B; referências de produto e revisões posteriores prevalecem, sem importar políticas antigas de grind ainda não aprovadas. Nenhuma pesquisa externa/API nova ou mudança de runtime, build, testes/publicação. M09-01–08 preservados; próximas respostas seguidas de resultado/consumo e amostra/fichas, sem novo pedido de avanço.


### M09-09A/B aprovados — 23/10/2026

Respostas explícitas “Reconhecer contribuição útil mesmo fora da party (recomendado)” e “Prêmio principal único; recompensas repetíveis separadas (recomendado)”. Contratos aprovados em M09/8.24/8.25 e 12.17/12.18; perguntas/opções/respostas no histórico. Nenhuma pergunta pendente do bloco. Reconhecimento aberto não distribui prêmio ou alma; prêmio principal único e repetível separado mantém fontes de grind, com economia e reset da jornada pendentes de integração. Próximo resultado/consumo e amostra/fichas. Sem pesquisa externa/API nova ao registrar respostas, runtime, build, testes ou publicação; M09-01–08 preservados.


## 37. M09 — uma devoração por presa aprovada — 23/10/2026

Resposta explícita “Uma devoração por presa, com revezamento (recomendado)”, recebida antes do registro documental, aprova M09-10A em 8.28/12.19. Pergunta/opções/resposta no histórico; uma conclusão de consumo por presa elegível, prioridade ainda pendente. Reconhecimento/recompensa dos demais não duplica predação. M09/8.27 confere RAC-19: diferença sistêmica entre derrota por Shinigami e Quincy continua em backlog, sem revisão por associação. M09/8.26 conciliado com a herança.

**ENC-S05, consulta em 23/10/2026:** [Pierrot ha](https://pierrot.jp/title/bleach/keyword_ha.html), Hollow/alimentação; [ka](https://pierrot.jp/title/bleach/keyword_ka.html), eliminação Quincy; [sa](https://pierrot.jp/title/bleach/keyword_sa.html), funções Shinigami. Fontes oficiais do anime complementar/arquivo da época, não capítulos/episódios integrais. Não sustentam prioridade, cópia de oportunidade, divisão de progresso ou representação consumível do mod. A escolha é de gameplay. Próximo: prioridade de consumo e amostra/fichas; M09 em especificação, sem runtime/build/testes/publicação.


## 38. M09 — prioridade de consumo proposta — 23/10/2026

[M09/8.29 e 12.20](09-mobs-bosses-e-encontros.md#829-primeiro-elegível-a-concluir-consome--m09-10b-decidido): **M09-10B PROPOSTO**, uma resposta pendente. Recomenda prioridade temporária comunicada, com passagem a outro elegível por recusa, inelegibilidade ou prazo, enquanto a oportunidade geral ainda for válida. Critérios de participação/revezamento e parâmetros exigem ficha M09/M15; alternativa é primeiro elegível a concluir. Consumo único de 10A e RAC-19 preservados. Sem nova pesquisa externa/API nesta proposta, runtime, build, testes ou publicação. Próximo: resposta e amostra/fichas/revisão, com perguntas contínuas.


### M09-10B decidido — primeira conclusão elegível — 23/10/2026

Resposta explícita “Primeiro elegível a concluir consome”. Segunda opção escolhida, diferente da recomendação inicial; contrato em M09/8.29/12.20 e pergunta/opções/resposta no histórico. Não há vez reservada, fila ou rodízio automático; uma conclusão válida encerra a presa. 10A conciliado para alternância voluntária, sem reabrir o consumidor único. Participação, parâmetros, expiração, concorrência e feedback exigem ficha. Nenhuma pergunta pendente do bloco; próximo amostra/fichas/revisão. Sem nova pesquisa externa/API, runtime, build, testes ou publicação; RAC-19 preservado.


## 39. M09 — amostra aprovada e funções/papel propostos — 23/10/2026

M09-11A aprovado por “Detalhar bruto, ágil, voador e chefe existentes (recomendado)”, resposta recebida antes do registro documental. [M09a](09a-catalogo-funcional-de-adversarios-e-encontros.md) criado com quatro fichas em elaboração e 15 seções. 11A seleciona conteúdo para detalhar, sem aprovar produção, região/era ou valores. **11B/C PROPOSTOS**, duas respostas pendentes em M09/12.22/12.23: funções distintas dos comuns e papel opcional preparado do chefe. Demais campos permanecem explícitos como pendentes.

Reconsulta local de HollowVariant, mecanismos do voador e boss confirma pontos de partida diferentes, sem demonstrar todos os contratos/funções propostos. Não classificar boss como Gillian pelo tamanho; não declarar combate calibrado. M09-01–10 preservados; perguntas/opções/respostas nos históricos próprios. Próximo: respostas e revisão de alcance/entregas do módulo. Sem pesquisa externa/API nova, runtime, build, testes ou publicação.


### M09-11B/C aprovados — 23/10/2026

Respostas explícitas “Funções distintas para bruto, ágil e voador (recomendado)” e “Confronto opcional preparado (recomendado)”. Funções/papel aprovados em M09/12.22/12.23 e M09a/9.1–9.4; histórico preservado. Nenhuma pergunta pendente. Seleção e funções da amostra não definem sensores/alvos/valores ou localização, quest e prêmios. Próximo revisão de alcance/entregas do módulo. M09-01–10 preservados; sem pesquisa externa/API nova, runtime, build, testes ou publicação.


## 40. M09 revisado e M10 aberto — 23/10/2026

[09b](09b-revisao-m09-2026-10-23.md) registra revisão documental dirigida de M09-01–11: regras gerais especificadas conceitualmente, catálogo M09a ainda em especificação. Não é auditoria exaustiva ou aceite; sensores/alvos, parâmetros, lugares, lifecycle específico, concorrência/multiplayer/economia e dados/validação continuam entregas antes da funcionalidade. Nenhuma nova escolha nessa revisão.

[M10](10-dimensoes-locais-e-estruturas.md) criado nas 15 seções; M10-01A/B aprovados por respostas explícitas recebidas antes do registro: núcleos planejados com áreas geradas e proteção localizada/sinalizada. 02A/B propostos sobre rotas/serviços e fuga em travessia comum; duas respostas pendentes. Overworld como nascimento físico já era decisão de M04, não repetida.

WORLD-S01/02: reconsulta em 23/10 a [Pierrot sa](https://pierrot.jp/title/bleach/keyword_sa.html), Seireitei/Senkaimon, e [ra](https://pierrot.jp/title/bleach/keyword_ra.html), Rukongai/Las Noches. WORLD-S03 usa recorte [ka](https://pierrot.jp/title/bleach/keyword_ka.html), Garganta, já consultado nesta sequência. Anime oficial complementar/arquivo da época, sem obra integral; não sustenta geração, escala, proteções ou permissões de viagem do mod.

Leitura integral local de HistoryWorld, HistoryMapInstaller, WorldTravelItem, manifesto/README/preset identifica instalação inicial de mapas de terceiros, destinos e transporte provisório. Não demonstra geração mista, mapa canonicamente fiel, proteção de trechos ou travessia futura. Não foram inspecionados visualmente mapas, executados instalador/cliente, migrados saves ou alterados código/assets. Sem API/dependência nova, runtime/build/testes/publicação. Próximo respostas e funções dos núcleos/chegada/retorno/implantação, com perguntas contínuas.


## 41. M10 — Rotas, autonomia de abertura e restrição de combate — 23/10/2026

02A decidido com resposta própria: rotas/serviços inicialmente no contexto de cada era e skills posteriores para abrir Senkaimon/Garganta, sem exclusividade racial absoluta. 02B segunda opção: passagens comuns somente fora de combate; fuga arriscada/preparo da recomendação anterior não aprovados. 02C complemento espontâneo do usuário admite técnica como a de Urahara e dispositivo como o de Mayuri. [Perguntas e respostas integrais](historico/2026-10-23-propostas-m10-02ab-rotas-e-travessia.md); contratos em M10/8.3/8.4/8.6.

WORLD-S04 — [glossário oficial TYBW, verbete 黒腔](https://bleach-anime.com/keyword/): resultado indexado consultado em 23/10/2026 descreve técnica com encantamento de Urahara e dispositivo de Mayuri; abertura integral retornou HTTP 403. WORLD-S05 — [TV Tokyo, sinopse ep.196](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes17/index.html) confirma estabilização para passagem dos capitães. Anime oficial complementar; sem capítulos/episódios integrais. Não prova habilidade universal, número de Kidō, currículo, receita ou consumo. Não se estabeleceu um Hollow abrindo Senkaimon naturalmente.

M10-03A PROPOSTO, uma resposta pendente: chegada em pontos estabelecidos ou também pontos próprios registrados para as skills básicas. A restrição fora de combate acompanha a passagem comum, independentemente da fonte; exceção especial exige ficha e decisão próprias, sem repetir a aprovação geral. Acesso de passageiro e competência de abrir permanecem distintos; cada via exige ficha. Mudança de era não foi autorizada a apagar aprendizados. Sem código/assets/save, runtime, build, testes ou publicação. Próximo resposta e destinos/chegada/retorno/funções dos núcleos.


## 42. M10 — Pontos de chegada aprovados — 23/10/2026

M10-03A decidido por resposta explícita “Chegada em pontos estabelecidos (recomendado)”. Skills básicas abrem em local válido e chegam a pontos estabelecidos nos destinos habilitados; marcação de pontos próprios não escolhida. Contrato em M10/8.7/12.6, pergunta/opções/resposta no histórico. Coordenadas, critérios de habilitação e expansão posterior continuam com fichas.

M10-03B PROPOSTO, uma resposta pendente: validar chegada antes da travessia e no momento de transferir; se inválida dentro da área, recusar mantendo na origem. Alternativa busca posição próxima fora da área. Corpo efetivo de chegada respeita a limpeza de formas em M06/M07; validação não implica invulnerabilidade. Carregamento, perigos, concorrência, recursos e retorno exigem ficha M10/M16. Contrato comum evita repetir decisões por raça/fonte. Sem pesquisa canônica/API nova, código, mapas, saves, build, testes ou publicação. Próximo resposta, retorno e funções dos núcleos.


## 43. M10 — Busca próxima fora da área aprovada — 23/10/2026

M10-03B decidido por resposta explícita “Buscar posição próxima fora da área”. Segunda opção, diferente da recomendação inicial; contrato em M10/8.8/12.7 e resposta no histórico. Busca próxima substitui recusa imediata quando falta posição válida dentro da área, preservando ponto estabelecido como referência e validação da posição encontrada. Não aprova raio, varredura ilimitada, destino alternativo ou acesso através de barreira/permissão.

03C/04A PROPOSTOS, duas respostas pendentes: limites/falha da busca e retorno viável em viagens comuns de exploração. [Perguntas/opções](historico/2026-10-23-propostas-m10-03c-04a-busca-e-retorno.md). Custos, perigos, concorrência, carregamento e meios de retorno/resgate concretos exigem ficha. Sem pesquisa canônica/API nova, código, mapas, saves, build, testes ou publicação. Próximo respostas e funções dos núcleos/condições concretas de viagem, com perguntas contínuas.


## 44. M10 — Retorno aprovado e alcance dos destinos esclarecido — 23/10/2026

M10-04A decidido por “Retorno viável nas viagens comuns (recomendado)”, contrato em M10/8.10/12.9. 03C recebeu pergunta sobre destinos canônicos de Senkaimon/Garganta e adaptação ao mod, sem escolha de fallback; pergunta reformulada em 12.8 e histórico. 03A/B preservados; uma decisão pendente.

WORLD-S06: [Pierrot, arco 21–41](https://pierrot.jp/title/bleach/playback_02.html), abertura do resumo, confirma chegada inicial do grupo ao Rukongai, fora da Seireitei. [Sinopse licenciada Hulu, ep.21](https://www.hulu.jp/watch/100044758?t=0), resultado indexado (abertura integral indisponível), identifica entrada pelo Senkaimon. WORLD-S04, glossário oficial TYBW/黒腔, descreve conexões Mundo Humano/Hueco Mundo e Soul Society/Hueco Mundo pelo dispositivo de Mayuri. Nenhuma fonte consultada nesta rodada estabelece qualquer coordenada para qualquer usuário; sem capítulos/episódios integrais.

M10/8.11 distingue entradas habilitadas por mundo de busca espacial no entorno de uma entrada. Múltiplas entradas são recomendação de catálogo coerente com 03A, não locais/quantidades já aprovados. Precisão livre futura exigiria expansão explícita. Sem mudança de código, mapas/saves, API, build, testes ou publicação. Próximo decisão de 03C e funções dos núcleos/condições concretas de viagem, com perguntas contínuas.

## 45. M10 — Falha de chegada aprovada e seleção de destino em revisão — 23/10/2026

03C decidido pela resposta “Sem espaço próximo, permanecer na origem (recomendado)”. Sem solução próxima válida, recusar viagem e avisar; não tentar automaticamente outra entrada. Custos e parâmetros continuam por ficha.

Manifestação posterior: “A ideia é ser a mesma do nether do minecraft por exemplo e a mesma da obra pelo o que entendi, já que também não parece ter um lugar fixo para chegar pelo senkaimon ou garganta”. Reabre a restrição de 03A, sem escolher ainda correspondência geográfica ou seleção deliberada de local. Fontes WORLD-S04/S06 não comprovam conversão universal entre coordenadas dos mundos de Bleach. A referência ao Nether não aprova escala, moldura de portal ou alteração de terreno.

03D proposto em M10/8.12/12.10 e [histórico](historico/2026-10-23-revisao-m10-03a-e-proposta-03d-destinos.md): recomendar seleção de local conhecido/acessível, inclusive local do jogador; alternativa região correspondente à posição de abertura. Uma resposta pendente. 02B, 03B/C e 04A preservados. Sem mudança de runtime, mapas/saves, testes, build ou publicação.

## 46. M10 — Destinos próprios aprovados — 23/10/2026

03D decidido por “Escolher local conhecido e acessível (recomendado)”. Skills próprias de Senkaimon/Garganta podem escolher local conhecido/acessível no outro mundo, inclusive local do jogador, revisando a restrição anterior de 03A às entradas cadastradas pelo mod. Correspondência geográfica pela posição de abertura não foi escolhida. Pergunta e resposta exatas no histórico de 03D; contrato vigente em M10/8.12/12.10. Rotas/serviços iniciais de 02A e contratos 02B, 03B/C e 04A preservados.

03E proposto em M10/8.13/12.11 e [histórico](historico/2026-10-23-proposta-m10-03e-registro-de-destinos.md): visita e registro deliberado com nome, sem portal permanente, ou registro automático ao explorar. Serviços/fontes apropriadas possibilitam primeira viagem a lugares desconhecidos; não criar exigência circular. Uma resposta pendente. Registro não concede acesso/propriedade ou compra de skill; limites, custos, UI e compartilhamento continuam por especificar. Sem nova pesquisa canônica/API, código, mapas/saves, build, testes ou publicação.

## 47. M10 — Registro manual aprovado e funções dos lugares — 23/10/2026

03E decidido por “Visitar e registrar manualmente (recomendado)”. Destino próprio exige visita e registro com nome; não exige portal permanente. Primeiras viagens mantêm serviços/fontes apropriadas. Contrato M10/8.13/12.11 e histórico preservam pergunta/resposta; 03D e demais contratos de acesso, combate, chegada e retorno permanecem. Registro não concede acesso ou propriedade; parâmetros, UI, quantidade e compartilhamento ainda exigem ficha.

M10/8.14 organiza necessidades dos lugares herdadas de M04/M08/M09, sem votar novamente papéis de NPCs. 05A proposto: Rukongai como região de povoados, atividades locais e exploração junto dos espaços institucionais da Soul Society; alternativa principalmente cenário de passagem. [Pergunta/opções](historico/2026-10-23-proposta-m10-05a-funcoes-rukongai-seireitei.md); uma resposta pendente. Não cria etapa obrigatória antes da Academia, acesso permanente ou proteção de toda cidade. Sem nova pesquisa canônica/API ou mudança de runtime, mapas/saves, build, testes ou publicação.

## 48. M10 — Rukongai aprovado; distribuição do apoio em Hueco Mundo — 23/10/2026

05A decidido por “Rukongai com atividades próprias (recomendado)”. Rukongai recebe função jogável com povoados, atividades locais e exploração, articulada aos espaços institucionais da Soul Society e aos requisitos de acesso/era. Contrato M10/8.14/12.12; pergunta/resposta preservadas no histórico. Não acrescenta etapa obrigatória antes da Academia ou catálogo completo de atividades.

05B proposto em M10/8.15/12.13 e [histórico](historico/2026-10-23-proposta-m10-05b-apoio-em-hueco-mundo.md): pequenos refúgios distribuídos ou um povoado independente principal para apoio em Hueco Mundo. Proposta de adaptação espacial; autonomia e papéis de M07/M08 não são votados novamente. Uma resposta pendente; locais, fontes, serviços e condições de cada apoio ainda exigem ficha. Sem nova pesquisa canônica/API ou mudança de runtime, mapas/saves, build, testes ou publicação.

## 49. M10 — Refúgios distribuídos aprovados e identidade urbana humana — 23/10/2026

05B decidido por “Pequenos refúgios distribuídos (recomendado)”. Apoio independente em Hueco Mundo por lugares ligados a fontes/grupos e com funções/acesso próprios, mantendo contexto de Las Noches. Rede é adaptação aprovada; locais, serviços, currículo, perigos e corpos exigem fichas. Contrato M10/8.15/12.13 e histórico preservam resposta.

05C proposto em M10/8.16/12.14 e [histórico](historico/2026-10-23-proposta-m10-05c-nucleo-urbano-humano.md): Karakura adaptada ao Minecraft ou cidade original com locais canônicos. Uma resposta pendente; não aprova mapa local, escala ou prédios individuais. WORLD-S07: [Pierrot, resumo do arco 1–20](https://pierrot.jp/title/bleach/playback_01.html), página lida integralmente, situa Rukia, ameaças Hollow e loja Urahara no contexto de Karakura; [apresentação TYBW](https://bleach-anime.com/story/story-1st.html), resultado indexado, abertura integral HTTP 403, situa Ichigo/novas ocorrências na cidade. Fontes complementares do anime consultadas em 23/10/2026, sem capítulos/episódios integrais ou planta completa. Sem mudança de runtime, mapas/saves, build, testes ou publicação.

## 50. M10 — Karakura aprovada e abastecimento de bases em discussão — 23/10/2026

05C decidido por “Karakura adaptada ao Minecraft (recomendado)”. Identidade do núcleo urbano humano articulada ao Overworld explorável/construível; recorte e avaliação do mapa ainda necessários, sem aprovação de escala, prédio ou implantação. Contrato M10/8.16/12.14 e histórico preservam resposta; WORLD-S07 sustenta contexto narrativo, não planta completa.

06A proposto em M10/8.17/12.15 e [histórico](historico/2026-10-23-proposta-m10-06a-recursos-e-bases.md): fontes locais para sustentar rotina básica de bases espirituais ou dependência principal do Mundo Humano. Uma resposta pendente. M01/M10 preservam exploração/preparação/construção; não duplicar todos os recursos por omissão. M11 completa catálogo/produção/economia; M05/M06 e M04/H2 mantêm recursos e limites de fome. Sem nova pesquisa canônica/API, mudança de runtime, mapas/saves, build, testes ou publicação.

## 51. M10 — Bases com abastecimento local aprovadas — 23/10/2026

06A decidido por “Base viável com abastecimento local (recomendado)”. Soul Society/Hueco Mundo permitem sustentar rotina básica por fontes locais, sem replicar obrigatoriamente todos os recursos do Overworld ou definir oferta gratuita. Contrato M10/8.17/12.15 e histórico preservam resposta; M11 completa itens/produção/economia, M05/M06 mantêm alimentação/regeneração e M04/H2 não cria segunda barra Hollow.

07A proposto em M10/8.18/12.16 e [histórico](historico/2026-10-23-proposta-m10-07a-experiencia-de-travessia.md): transição breve nas viagens comuns e percursos intermediários em situações próprias, ou percurso jogável também no uso habitual. Uma resposta pendente. Preservar passagens comuns fora de combate, validação/retorno e lifecycle; não aprovar custos, duração, passageiros, dimensão ou riscos novos por omissão. Revisão ambiental/espacial/implantação segue pendente. Sem nova pesquisa canônica/API, mudança de runtime, mapas/saves, build, testes ou publicação.

## 52. M10 — Transição breve aprovada; convidados nas passagens — 23/10/2026

07A decidido por “Transição breve; percursos em situações próprias (recomendado)”. Viagem comum tem apresentação/transição breve, sem corredor jogável obrigatório a cada uso; percursos de Dangai/Garganta ficam para conteúdos próprios. Contrato M10/8.18/12.16 e histórico preservam resposta. Custos, preparo, duração, dimensão intermediária e imunidade não foram definidos por essa escolha.

07B proposto em M10/8.19/12.17 e [histórico](historico/2026-10-23-proposta-m10-07b-convidados-nas-passagens.md): convidados autorizados em passagens próprias ou uso pessoal por padrão. Uma resposta pendente. Passageiro escolhe atravessar sem aprender abertura ou registrar previamente o destino, mantendo acesso individual, fora de combate e validação do corpo; registro manual e retorno viável preservados. Sem novo cânone/API, convite real, mudança de runtime, mapas/saves, build, testes ou publicação.

## 53. M10 — Passagens com convidados da mesma party — 23/10/2026

07B decidido pela resposta própria “Compartilhar com convidados da mesma party”. Restringe compartilhamento a convidados do mesmo grupo de quem abre, com autorização e decisão individual de atravessar. Não aprova convidados externos ou ingresso automático de todos os membros. Contrato M10/8.19/12.17 e histórico preservam resposta; acesso, combate, validação do corpo, registro manual e retorno viável permanecem.

07C proposto em M10/8.20/12.18 e [histórico](historico/2026-10-23-proposta-m10-07c-custo-e-janela-de-abertura.md): custo em Energia ao formar e janela curta sem manutenção, com travessia normal do abridor permitida, ou consumo contínuo. Uma resposta pendente. Não aprova valores, tarifa por passageiro, preparo ou ciclo de vida; serviços/dispositivos/rotas especiais exigem ficha própria e aquisição de skill mantém SP. Sem nova pesquisa canônica/API, convite real, mudança de runtime, mapas/saves, build, testes ou publicação.

## 54. M10 — Custo e janela aprovados; preparo e ciclo propostos — 23/10/2026

07C decidido por “Custo ao abrir + janela sem manutenção (recomendado)”. Energia ao formar, janela curta sem manutenção, sem reembolso por não utilizar passagem formada. Abridor pode atravessar preservando janela para convidados da mesma party. Contrato M10/8.20/12.18 e histórico preservam resposta; valores/limites e serviços/dispositivos continuam por ficha.

07D/E propostos em M10/8.21/8.22 e 12.19/12.20 e [histórico](historico/2026-10-23-propostas-m10-07de-preparo-e-ciclo-da-passagem.md): preparo parado/cancelável ou imediato; objeto formado/pago autônomo até prazo em regiões ativas, encerrando na descarga/reinício, ou término adicional por morte/desconexão. Duas respostas pendentes. Preservar estado pessoal de M06/M07, acesso/party e resultado válido da travessia; não aprovar chunk forçado, imunidade ou reembolso por omissão. Sem nova pesquisa canônica/API, mudança de runtime, mapas/saves, build, testes ou publicação.

## 55. M10 — Preparo e autonomia aprovados; limite e sentido — 23/10/2026

07D/E decididos pelas respostas “Preparo breve parado e cancelável (recomendado)” e “Cumprir a janela já paga (recomendado)”. Preparo cancelável sem débito/recuperação extra, pedido indisponível mantém válido; passagem paga cumpre tempo com morte/desconexão em regiões ativas, encerrando na descarga/reinício sem restaurar. Contratos M10/8.21/8.22 e 12.19/12.20; respostas no histórico. Autonomia do objeto não restaura estados pessoais de M06/M07.

07F/G propostos em M10/8.23/8.24 e 12.21/12.22 e [histórico](historico/2026-10-23-propostas-m10-07fg-limite-fechamento-e-sentido.md): uma passagem ativa com fechamento manual ou várias; ida/volta durante prazo ou apenas ida. Duas respostas pendentes. Preservar tempo pago sem renovação, acesso/combate/espaço/party por viajante e retorno viável; não criar rede persistente ou substituir portal anterior por omissão. Depois revisão/consolidação das passagens e lacunas ambientais/espaciais/implantação. Sem nova pesquisa canônica/API, mudança de runtime, mapas/saves, build, testes ou publicação.

## 56. M10 — Limite/ida e volta aprovados e revisão dirigida — 23/10/2026

07F/G decididos por “Uma passagem ativa, com fechamento manual (recomendado)” e “Ida e volta enquanto aberta (recomendado)”. Bloco comum de passagens consolidado conceitualmente em M10/8.25 e [revisão](10a-revisao-m10-2026-10-23.md); módulo permanece em especificação. Estados correntes obsoletos conciliados sem apagar perguntas/histórico. Entregas de retorno de convidados após fechar, corpos grandes, carregamento/transferência e implantação estão identificadas antes de implementar.

08A proposto em M10/8.26/12.23 e [histórico](historico/2026-10-23-proposta-m10-08a-ambiente-e-agua-hueco-mundo.md): perfil noturno/areia branca sem rios/lagos naturais, permitindo água transportada funcional como adaptação, ou impedindo água estável também trazida. WORLD-S08: [Pierrot, verbete 虚圏/ウェコムンド](https://pierrot.jp/title/bleach/keyword_a.html), trecho integral consultado em 23/10/2026, descreve noite permanente, solo semelhante a areia branca, árvores de aspecto de quartzo e ausência de água. Fonte oficial histórica do anime complementar, sem capítulo/episódio integral. Não demonstra evaporação, física de balde, agricultura ou regras de nutrição do jogador. Uma resposta pendente. Sem mudança de runtime, mapas/saves, build, testes ou publicação.

## 57. M10 — Água conforme evidência disponível; proposta de renascimento — 23/10/2026

08A decidido por resposta condicionada à pesquisa: “Se na obra tiver alguma referência a isso e for impedir água no Hueco Mundo então que assim seja, mas se não vamos com sua recomendação”. WORLD-S08 reaberto; descreve noite/areia branca/ausência de água, sem abordar transporte, evaporação ou baldes. Pesquisa dirigida não localizou proibição primária explícita verificável. Recomendação adotada como adaptação: água trazida funcional, paisagem sem rios/lagos naturais. Não se afirma autorização canônica universal nem investigação exaustiva; fonte primária contrária exige revisão registrada. Referência: [Pierrot](https://pierrot.jp/title/bleach/keyword_a.html), verbete integral, complemento histórico oficial do anime; nenhum capítulo/episódio integral. Fanfic, especulação e espelhos não autorizados não fundamentam a decisão. Contrato/resposta em M10/8.26/12.23 e histórico.

08B proposto em M10/8.27/12.24 e [histórico](historico/2026-10-23-proposta-m10-08b-local-de-renascimento.md): ponto escolhido nas três dimensões, cama compatível ou abrigo para corpos grandes, ou concentração no Mundo Humano. Uma resposta pendente. Adaptação nova, sem repetir nascimento inicial no Overworld nem alterar raça/estados/penalidades de morte. Fallback, primeiro spawn/reconexão de corpo grande e dormir/explosão continuam sem aprovação. M10 em especificação; 10a recebeu complemento datado preservando escopo da revisão anterior. Sem runtime, mapas/saves, build, testes ou publicação.

## 58. M10 — Renascimento no mundo escolhido; recuperação em proposta — 23/10/2026

08B decidido por “Ponto de renascimento no mundo escolhido (recomendado)”. M10/8.27/12.24 e histórico registram aprovação: cama para corpo compatível ou abrigo para corpo grande, com acesso/espaço válidos, mantendo nascimento inicial, identidade e contratos de morte. Não decide quantidade de registros, interface, construção/serviço concreto, dormir/explosão de cama ou recursos após morte.

08C proposto em M10/8.28/12.25 e [histórico](historico/2026-10-23-proposta-m10-08c-recuperacao-do-renascimento.md): na falha de ponto pessoal, busca próxima se referência/acesso válidos, recuperação preparada no mesmo mundo e alternativa no Mundo Humano se necessário; alternativa concentra a recuperação no Mundo Humano. Uma resposta pendente. Áreas devem comportar o corpo-base permanente e respeitar acesso, sem escavar, reduzir estágio ou exigir skill/jogador para renascer. Segurança final e carregamento são entregas M10/M15/M16 antes da funcionalidade; primeiro spawn, ausência de seleção e reconexão não ganham teleporte automático por esta proposta. Adaptação de produto, sem nova pesquisa canônica/API. M10 em especificação; somente documentação, sem runtime, mapas/saves, build, testes ou publicação.

## 59. M10 — Recuperação aprovada; posicionamento restante em pacote — 23/10/2026

08C decidido por “Busca próxima → recuperação local → Mundo Humano (recomendado)”. M10/8.28/12.25 e histórico registram busca próxima válida, área de recuperação no mesmo mundo e alternativa no Mundo Humano. Todas comportam o corpo-base permanente, preservam permissões e não exigem nova skill/outro jogador. Não há redução automática de estágio, remoção de terreno ou garantia de área final já implementada; fichas/carregamento continuam entregas antes da funcionalidade.

08D proposto em M10/8.29/12.26 e [histórico](historico/2026-10-23-proposta-m10-08d-posicionamento-inicial-e-retorno.md): sem ponto pessoal escolhido, renascer na área segura inicial do Mundo Humano; ao retornar, manter posição válida e corrigir apenas impedimento por busca próxima/recuperação. Ampliação explícita de escopo para os casos restantes em um pacote. Nascimento inicial de M04 permanece; posição no ar/água não é automaticamente inválida e ameaça/queda não ativam resgate gratuito. Inventário, recursos, conquistas e tempos não são resetados pela correção. Uma resposta pendente; sem nova pesquisa canônica/API, runtime, mapas/saves, build, testes ou publicação. M10 em especificação.

## 60. M10 — Posicionamento consolidado; descanso espiritual em proposta — 23/10/2026

08D decidido por “Manter local válido e recuperar bloqueios (recomendado)”. M10/8.29/12.26 e histórico registram aprovação. 8.30 consolida primeiro nascimento herdado de M04, ponto escolhido, falha/ausência de ponto e retorno válido/bloqueado, com corpo-base permanente, permissões e recursos preservados. Sem nova aprovação de números, local físico, UI, salvamento ou carregamento; essas fichas continuam condições de entrega. Não repetir decisões gerais por raça ou região.

08E proposto em M10/8.31/12.27 e [histórico](historico/2026-10-23-proposta-m10-08e-descanso-nas-dimensoes-espirituais.md): descanso em cama nas dimensões espirituais ou somente registro de renascimento. Recomendação separa registrar de conseguir dormir, preserva noite permanente de Hueco Mundo e relógios de simulação, sem cura espiritual extra. Uma resposta pendente. Alternativa mantém o renascimento já aprovado; não reabre 08B. Condições/efeitos/tempos de descanso por ficha M10/M11/M14/M15/M16; nenhuma alegação canônica/API nova. M10 em especificação; apenas documentação, sem runtime, mapas/saves, build, testes ou publicação.

## 61. M10 — Esclarecimento de cama/spawn e recuperação — 23/10/2026

Resposta integral registrada em 08E/12.27 e histórico. Usuário reafirma cama marcando spawn, mas pede clareza e comparação com Nether; não escolheu a opção de descanso. Estado de 08E atualizado para EM ESCLARECIMENTO, mantendo o que 08B/C/D já aprovou. M10/8.27/8.30 explica cama = ponto pessoal de retorno após morte; portal/destino registrado é outra função; recuperação automática não exige atendimento para marcar cama. Abrigo de Gillian é alternativa conceitual com objeto/interação ainda pendentes, não spawn automático em construção arbitrária.

Fonte oficial Minecraft consultada em 23/10/2026: [Spawning and dying](https://www.minecraft.net/en-us/article/spawning-and-dying), seção de alterar respawn: cama no Overworld e âncora no Nether. É descrição vanilla, sem alegar compatibilidade técnica já implementada; camas nos mundos Bleach são adaptação aprovada para marcar spawn, com descanso ainda pendente. Não altera Nether/End por associação. Próximo explicar o fluxo com exemplo e retomar depois a escolha de sono; sem nova aprovação inferida, runtime, build, testes ou publicação.

## 62. M10 — Sono em Hueco Mundo aprovado; passagem da noite em proposta — 23/10/2026

08E decidido no recorte Hueco Mundo pela resposta “Então se é sempre noite no Hueco Mundo pode dormir a qualquer momento possivel”. Sem restrição de horário para dormir em cama compatível, mantendo segurança/espaço/fase; cama marca spawn e descanso não cria amanhecer. Não acrescenta cura espiritual/treino/atalho artificial de cooldown nem desliga regenerações usuais elegíveis. Duração, saída e efeitos vanilla compatíveis exigem ficha. Demais perfis, como céu/sono de Soul Society, não aprovados por associação. Histórico preserva o esclarecimento anterior e a resposta complementar; M10/8.31/12.27.

08F proposto em M10/8.32/12.28 e [histórico](historico/2026-10-23-proposta-m10-08f-passagem-da-noite-por-mundo.md): passagem da noite independente por mundo com ciclo ou compartilhada. Uma decisão pendente. Não aprova ciclo em toda dimensão nem independência técnica existente; M10/M15/M16 detalham condições/multiplayer/relógio/salvamento. Sem nova pesquisa canônica/API, runtime, build, testes ou publicação.

## 63. M10 — Passagem da noite por mundo aprovada; população regional proposta — 23/10/2026

08F decidido por “Passagem da noite por mundo (recomendado)”. M10/8.32/12.28 e histórico registram avanço local do ciclo, participação do mundo conforme regra do servidor, Hueco Mundo permanentemente noturno e relógios de combate por simulação. Não fixa percentual, perfil de céu em toda dimensão ou implementação de independência temporal. 08E e renascimento/retorno preservados.

08G proposto em M10/8.33/12.29 e [histórico](historico/2026-10-23-proposta-m10-08g-criaturas-por-regiao.md): geração natural regional em Soul Society/Hueco Mundo com monstros vanilla só por ficha, ou geração comum desses monstros também nas dimensões espirituais. Mundo Humano mantém base vanilla com aparições Hollow regionais. Uma decisão pendente. Política de produto, sem alegação canônica/API nova; não apaga entidades, decide transporte/pets ou fixa taxas/despawn/recompensas. M09 mantém seus perfis; M11/06A exigem abastecimento compatível. M10 em especificação; sem runtime, build, testes ou publicação.

## 64. M10 — Criaturas regionais aprovadas; catálogo funcional de lugares — 23/10/2026

08G decidido por “Criaturas por região e identidade do mundo (recomendado)”. M10/8.33/12.29 e histórico registram geração natural regional em Soul Society/Hueco Mundo, monstros vanilla somente por ficha e base vanilla humana com aparições Hollow regionais. Não resolve taxas/despawn/loot, entidades existentes ou transporte. M09 e abastecimento 06A/M11 preservados.

Aberto [M10b](10b-catalogo-funcional-de-lugares-e-rotas.md), em especificação, com seis fichas de necessidade e matriz de percursos/entregas. Leitura dirigida de M08a/9.31–9.37 e fronteiras M09/M10; sem nova pesquisa canônica/API. Funções herdadas distinguidas de geografia nova. 09A proposto em M10/8.34/12.30 e M10b/12: proximidade dos percursos humanos em Karakura, com fontes distintas/coerentes, ou regiões iniciais afastadas. Uma decisão pendente; Academia permanece na Soul Society, quatro origens começam fisicamente no Mundo Humano conforme M04. Sem novas skills/fontes concedidas, coordenadas, ordem de produção ou aceite de mapas. M10 permanece em especificação; perfis ambientais restantes, retorno, abrigos/áreas concretas e implantação continuam condições de entrega. Sem runtime, build, testes ou publicação.

## 65. M10 — Percursos iniciais próximos aprovados; fonte da rota a Hueco Mundo — 23/10/2026

09A decidido por “Primeiros percursos próximos de Karakura (recomendado)”. M10/8.34/12.30 e M10b/8.1/12.1 aplicam proximidade com fontes distintas/coerentes, sem fixar distâncias/coords ou levar Academia ao Mundo Humano. Releitura dirigida de M04/RAC-07 e catálogo M10b: ingresso institucional já aprovado; Plus/Konsō/estado narrativo continuam para prólogo, e passagem por Rukongai era exemplo, não nova obrigação.

M10b/9 elabora perfis funcionais de encaminhamento Shinigami e primeira rota comum a Hueco Mundo. 09B proposto em M10/8.35/12.31 e M10b/9.2/12.2: operadores Arrancar originais independentes nas duas pontas ou passagem fixa mantida por dispositivo. Uma decisão pendente. Veterano pode indicar fonte sem ganhar abertura/Cero; serviço de volta independe da janela de ida; acesso/era, fontes qualificadas, corpos, preço e continuidade exigem ficha antes da entrega. Adaptação de produto, sem nova pesquisa canônica/API; nenhuma via de 02C excluída do catálogo completo. M10 em especificação; sem runtime, build, testes ou publicação.

## 66. M10 — Operadores aprovados e primeiro acesso Hollow em proposta — 23/10/2026

09B decidido por “Operadores independentes nos dois lados (recomendado)”. M10/8.35/12.31 e M10b/9.2/12.2 registram serviço com operadores Arrancar originais nas duas pontas. M08a/9.38 acrescenta funções documentais individuais, atendimento essencial/continuidade herdados, com nomes, motivos, origem do conhecimento, acesso/era, preço e execução pendentes. Não atribui abertura ao veterano, ensino de técnica ao passageiro ou serviço irrestrito a toda origem. Dispositivo continua possível no catálogo completo.

09C proposto em M10/8.36/12.32 e M10b/9.3/12.3 e [histórico](historico/2026-10-23-proposta-m10-09c-momento-do-acesso-hollow.md): primeiro acesso após fundamentos iniciais ainda como Hollow comum, ou somente após Gillian. Uma decisão pendente. Preparo reconhecido pode integrar a introdução sem duplicar prova; proposta não exige Cero, todo repertório ou chefe opcional e não cria escala automática dos inimigos. Acesso de outras origens, pagamento, disponibilidade por era e etapas concretas continuam com responsáveis. Adaptação de produto, sem nova pesquisa canônica/API, runtime, build, testes ou publicação.

## 67. M10 — Abertura própria versus viagem assistida; pesquisa dirigida — 23/10/2026

09C recebeu pedido de fidelidade à obra e esclarecimento de estágio de Garganta/Senkaimon, com reafirmação de primeiras viagens assistidas. Resposta integral em histórico/M10/12.32/M10b/12.3. NPC da rota 09B abre e jogador atravessa; chegada permite volta por operador local. Não há skill pessoal concedida por serviço. Primeiro acesso assistido ainda como Hollow comum versus após Gillian não foi escolhido; 09C EM ESCLARECIMENTO.

WORLD-S09: verbetes oficiais [Garganta](https://pierrot.jp/title/bleach/keyword_ka.html) e [Senkaimon](https://pierrot.jp/title/bleach/keyword_sa.html) reabertos, sem marco universal de desbloqueio. Referências secundárias indexadas [Kūmon](https://bleach.fandom.com/wiki/K%C5%ABmon) (mangá 45/p.2, anime 14; Menos/Gillian) e [Senkaimon](https://bleach.fandom.com/wiki/Senkaimon) (mangá 56/pp.17–18; Renji/Zanpakutō, antes de sua conquista de Bankai). Aberturas integrais das páginas gerais da wiki: HTTP 402. Índice oficial VIZ do capítulo 56 exige assinatura; nenhum capítulo/episódio integral conferido nem scans não autorizados utilizados. Gillian é exemplo localizado, não mínimo universal comprovado; Renji não prova que todo aluno abra. Requisitos pessoais seguem por ficha/pesquisa primária e adaptações identificadas, preservando compra SP/Mastery pela prática. Não criar regra automática só de raça, Shikai, Bankai ou formatura. Sem runtime/build/testes/publicação.

## 68. M10 — Primeiro acesso determinado pela campanha — 23/10/2026

**Resposta complementar em 23/10/2026:** “Vai ser com base nas missões mesmo, ele vai para lá quando necessário e quando fizer sentido no arco da obra que reflete diretamente na campanha do mod”. **09C DECIDIDO — acesso por missão e contexto da campanha.** A primeira ida a Hueco Mundo acontece quando uma missão pertinente a exigir ou a disponibilizar no arco/era adequado. Não se adota um estágio racial universal para essa viagem: o roteiro da missão define os requisitos coerentes, inclusive estágio se for necessário naquele caso. A escolha não aprova automaticamente o acesso como Hollow comum nem uma trava geral em Gillian. Viajar como passageiro continua separado de adquirir a skill de abertura própria. Missão, marco, fonte e condições concretas serão especificados em M12/M13 com M08/M10; não são uma decisão binária ainda pendente entre Hollow comum e Gillian.

Aplicado em M10/8.36/12.32, M10b/9.3/12.3, M08a/9.38, revisão e continuidade. M03 preserva cronologia, campanha pessoal e mundo aberto entre capítulos; a decisão não exige missão ativa para toda visita futura nem antecipa catch-up de M15. 09B mantém operadores da rota comum quando pertinente, sem substituir automaticamente os transportes de missões. M13 define missão/arco/motivo e fonte; M12 registra condições/marcos; M08/M10 oferecem operação, corpo, chegada e retorno. Primeira viagem e abertura própria continuam separadas, com SP na aquisição e Mastery pela prática. Proposta anterior de acesso após fundamentos e alternativa somente Gillian não foram adotadas. Registros anteriores de pendência são históricos, superados por esta resposta. Pesquisa WORLD-S09 preservada com seus limites; sem nova pesquisa, implementação, build, testes, commit ou publicação nesta atualização.

## 69. Revisão de cobertura e profundidade — M01–M10 — 23/10/2026

**Pedido do usuário:** “Perfeito, vamos revisar a M10 então, uma coisa que precisa de atenção também é a generalização excessiva ou a especificação excessiva, pois como no módulo 8 que ainda você não ia falar de cada NPC ou dar designação se eu não tivesse falado nada. então vamos tomar cuidado e também tentar identificar casos como esse de decisões em outros módulos que eram para ter sido tomadas, desde o primeiro”.

[Relatório de cobertura](00-revisao-cobertura-m01-a-m10-2026-10-23.md) registra o alcance das leituras dirigidas, achados, responsáveis principais e momentos de retorno. Regras comuns, cobertura do horizonte, fichas de conteúdo e execução são níveis diferentes. Uma amostra mínima não certifica o jogo completo; coordenadas/falas/números podem esperar, enquanto função e escolhas de funcionamento precisam de dono e entrega.

Corrigida orientação do mapa que restringia M08 quanto ao repertório oferecido; selecioná-lo pertence a M08 em integração com execução M06 e quests M13. Conciliadas duas sínteses de M01/M05 com a compra de skills/formas/atributos por SP e Mastery pela prática, sem alterar respostas históricas. M10b/7.1 amplia a cobertura dos lugares requeridos por contratos anteriores, distinguindo necessidade, candidato e decisão aprovada.

M10 permanece em especificação: próximo foco em lugares/atividades, ambiente, acesso, circulação/retorno, recuperação e modos de mundo. Retornos dirigidos prioritários a M04/presença espiritual e M06/Combat State; M02/M03/M08/M09 mantêm entregas concretas de integração/conteúdo. Missões e marco exatos de 09C continuam em M12/M13, sem nova regra de estágio racial. Não reabrir adiamentos explícitos para preencher tabelas. Nenhuma nova escolha de gameplay foi aprovada pelo relatório. Somente documentação; sem nova pesquisa externa, inspeção de mapas, código, build, teste de jogo, commit ou push.

## 70. M10 — Conteúdo local e composição institucional — 23/10/2026

Após pedido de continuidade, desenvolvidos perfis de atividade em M10b/8.3.1 e 8.5.1: M10-10A propõe abastecimento, proteção e apuração junto a moradores/responsáveis do Rukongai; M10-10B propõe reconhecimento, provisões/trocas e acordos locais nos refúgios independentes. Alternativas e perguntas integrais no [registro do bloco](historico/2026-10-23-propostas-m10-10ab-atividades-locais.md). **Ambos PROPOSTOS, duas respostas pendentes.** Não reaprovam função/distribuição de 05A/B nem mudam operadores 09B ou acesso narrativo 09C. Missões, indivíduos e recompensas concretos continuam com os responsáveis; fichas mostram ações e percursos, sem declarar sua execução pronta.

M10b/8.7 organiza requisitos espaciais da entrada, formação, divisões, prática, atendimento especializado e saída em Seireitei, a partir de M04/M08. Não concede credencial global, nova chefia ou poder por filiação. WORLD-S10: reconsultados [Rukongai/Las Noches](https://pierrot.jp/title/bleach/keyword_ra.html), [Seireitei/Soul Society](https://pierrot.jp/title/bleach/keyword_sa.html) e [resumo oficial do arco 21–41](https://pierrot.jp/title/bleach/playback_02.html); contexto histórico do anime complementar, sem capítulos/episódios integrais. Atividades propostas e refúgios originais são adaptações do mod, não instituições canônicas demonstradas por essas fontes.

Publicação autorizada pelo usuário após concluir o bloco da revisão: M10, retornos dirigidos a M04/M06 e complementos necessários; condição e texto integral no handoff. Não houve commit/push antecipado. M10 permanece em especificação; sem código, mapa/save, build ou teste de jogo.

## 71. M10 — Atividades locais aprovadas; ambiente e abrigo propostos — 23/10/2026

M10-10A/B decididos por “Atividades com moradores e responsáveis locais (recomendado)” e “Sobrevivência e acordos locais (recomendado)”. Aplicados em M10/8.38/12.33–12.34 e M10b/8.3.1/8.5.1/12.4–12.5, com perguntas/alternativas no histórico. Resultado é seleção de frentes de atividade e interlocutores por função; povoado/refúgio individual, pessoas, missões e recompensas concretos continuam por ficha, sem conceder compra, Mastery ou evolução automaticamente. Seireitei e demais atendimentos em M10b/8.7/8.8 articulam os contratos de M08 sem aprovar novos currículos ou permissões globais.

M10-11A/B propostos em M10/8.39–8.40/12.35–12.36 e [histórico](historico/2026-10-23-propostas-m10-11ab-ambiente-e-abrigo.md): ambiente com dia/noite/clima regional em Soul Society, ou iluminação diurna permanente; marco de abrigo em bloco próprio para corpo grande, ou estrutura mínima. **Duas respostas pendentes.** Propostas de adaptação para Minecraft, sem nova afirmação de clima canônico ou verificação de API. Heranças 08B/C/D/E/F preservadas: mesmo ponto da cama, recuperação, noite permanente de Hueco Mundo e passagem da noite por mundo. Obtenção/receita, dimensões e realização permanecem com responsáveis. M10 em especificação; sem código, mapa/save, build, testes ou publicação.

## 72. M10 — Noite/chuva conferidas, ambiente e marco aprovados; perfil de mundo proposto — 23/10/2026

**11A, resposta integral:** “Como é na obra mesmo? eu lembro de chover e ficar de noite lá também, se for isso mesmo podemos seguir com o recomendado por você”. **WORLD-S11:** a [sinopse oficial TV Tokyo do episódio 23](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes02/index.html), no contexto do Rukongai, menciona noite anterior e manhã seguinte; a [galeria oficial TYBW episódio 7](https://bleach-anime.com/story/1st/?id=07), inspecionada visualmente no navegador, mostra quadro de Byakuya sob chuva em Seireitei. Extrator retornou 403 nessa página; navegador acessou a página pública normalmente. Não houve capítulo/episódio integral lido/assistido. Condição da resposta atendida; **11A DECIDIDO**, perfil recomendado adotado. Duração do ciclo, probabilidade de clima e regras de cama são adaptações para Minecraft, não medições canônicas. **11B DECIDIDO** por “Marco de abrigo em bloco próprio (recomendado)”. Respostas/perfis em M10/8.39–8.40/12.35–12.36 e histórico; tempos, obtenção/receita, geometria e realização mantêm seus responsáveis.

**M10-12A PROPOSTO:** campanha em mundos novos/existentes compatíveis por integração própria, ou completa somente em novo mundo História. Pergunta em M10/8.41/12.37 e [histórico](historico/2026-10-23-proposta-m10-12a-perfis-de-mundo.md); uma resposta pendente. README de mapas relido e leitura dirigida de HistoryWorld/HistoryMapInstaller: fluxo atual de História e recusa a sobrescrever regiões não comprovam integração de núcleos em saves usados. Proposta escolhe horizonte de produto; implantação/validação em M16. M10 em especificação; lugares/serviços e retorno ainda precisam de fichas, seguidos dos retornos M04/M06 e dependências do bloco autorizado para publicação. Sem código, mapa/save, build, teste, commit ou push.

## 73. M10 — Perfis de mundo aprovados; operadores e áreas em proposta — 23/10/2026

12A decidido por “Campanha em mundos novos e existentes compatíveis (recomendado)”. Aplicado em M10/8.41/12.37 e histórico, com pergunta/alternativa preservadas. Aprova o horizonte de experiência; integração e validação próprias em M16, sem promover o instalador atual a suporte comprovado de saves usados. Nenhuma região, construção ou save alterado.

M10b/9.4 organiza retorno viável conforme 04A: fonte/meio efetivo após fechar a passagem, convidado sem skill, corpo permanente novo e fonte indisponível. Área de recuperação de renascimento é outro contrato, sem teleporte de resgate. Contingências concretas ainda precisam de ficha. 12B/C propostos em M10/8.42/12.38–12.39, M10b/9.5–9.6/12.6–12.7 e [histórico](historico/2026-10-23-propostas-m10-12bc-operadores-e-recuperacao.md); **duas respostas pendentes**. 12B propõe operadores institucionais Shinigami originais nos dois lados da rota comum Humano/Soul Society; alternativa concentra a operação na rede Urahara. 12C propõe áreas abertas conectadas à periferia de Karakura, povoado do Rukongai e refúgio inicial de Hueco Mundo, com reset perto do ponto humano; alternativa instala nos centros dos núcleos. Papéis individuais candidatos em M08a/9.39 e nota de localização do reset em 8.8.

Propostas de adaptação e localização do mod, sem nova pesquisa externa/API ou alegação de postos canônicos. Não alteram transporte das primeiras missões, papéis canônicos, raça, gratuidade/perdas do reset ou regras de cama/marco. M10/M08 especificam lugares/fontes; retornos dirigidos M04/M06 precisam completar presença/interação/reset e Combat State. M10 em especificação; sem código, mapa/save, build, testes, commit ou push.

## 74. M10 — Operadores e recuperação aprovados; retorno dirigido à presença espiritual — 23/10/2026

12B/C decididos por “Operadores institucionais nas duas pontas (recomendado)” e “Áreas abertas conectadas aos núcleos (recomendado)”. Respostas/perfis aplicados em M10/8.42/12.38–12.39, M10b/9.5–9.6/12.6–12.7, M08a/8.8/9.39 e histórico. Operadores Shinigami originais autorizados perto de Karakura e na entrada externa de Seireitei pelo lado do Rukongai. Áreas de recuperação abertas na periferia humana, junto a povoado do Rukongai e junto ao refúgio inicial de Hueco Mundo, todas compatíveis com Gillian; reset próximo à área humana. Não são postos canônicos comprovados nem destinos livres de teleporte. Nomes, arquitetura, coordenadas e implantação continuam por ficha.

M10/8.43 confere os percursos e responsáveis. Escolhas do complemento atual consolidadas; conteúdo e execução de M10 continuam em especificação. Convidado numa base distante após fechar o portal precisa de retorno efetivo, sem substituir esse requisito por renascimento ou resgate gratuito. Missão do primeiro acesso segue M12/M13 e não é decidida pelo operador comum.

Aberto [M04a](04a-presenca-espiritual-e-interacao.md) para COV-05. PRES-S01: [VIZ, sinopse do volume 1](https://www.viz.com/manga-books/manga/bleach-volume-1/product/167/paperback), Ichigo já vê espíritos antes da transferência de Rukia. PRES-S02: [Pierrot, resumo oficial dos episódios 1–20](https://pierrot.jp/title/bleach/playback_01.html), surpresa de Rukia por ser vista e distinção da visão humana comum. PRES-S03: [TV Tokyo, sinopse do episódio 5](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes01/index.html), Chad combate Shrieker com Rukia sem enxergá-lo. Consultadas apresentações/sinopses oficiais, não capítulos ou episódios integrais. A distinção entre ver e conseguir participar do combate não demonstra que toda pessoa ou arma comum fira todo espírito; comportamento de criaturas vanilla é adaptação.

RAC-30A/B/C **PROPOSTOS**, [perguntas enviadas](historico/2026-10-23-propostas-m04-rac30abc-presenca.md), três respostas pendentes: visão espiritual ordinária no prólogo antes do combate exigido; compatibilidade comum de dano/interação com exceções específicas; comportamento vanilla preservado com perfis sensoriais próprios em Bleach. Separar visão, leitura de Reiatsu, informação do alvo e compatibilidade de efeito; não criar visão por leitura através de parede. Não promover a matriz a aprovada. Próximos: aplicar respostas, completar corpo/posição do reset e seguir M06/Combat State e dependências. Publicação autorizada somente ao concluir o bloco completo; sem alteração de código/mapas/saves, build, testes, commit ou push.

## 75. M04 — Visão/interação aprovadas, dano reformulado e conciliação escolhida — 23/10/2026

**Respostas iniciais:** RAC-30A — “Visão no prólogo antes do combate exigido (recomendado)”; RAC-30B — “mas no anime não é assim como você recomendou”; RAC-30C — “Preservar interação vanilla (recomendado)”. A/C aprovados. B permaneceu em esclarecimento naquela resposta, sem assumir uma alternativa; recomendação de dano comum liberado por padrão retirada. A ressalva anterior de que isso era adaptação não bastava para sustentar essa recomendação diante da prioridade de fidelidade do usuário.

**Pesquisa PRES-S04:** [Shueisha/Shonen Jump, resumo oficial do arco Fullbring](https://www.shonenjump.com/xmedia/bleach/story/index5.html), verbete 完現術, identifica o poder do braço de Chad como Fullbring e define manipulação da alma da matéria. Reconsulta da [sinopse TV Tokyo do episódio 5](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes01/index.html) confirma combate sem visão, sem explicar uma regra universal de dano ou a causa daquele golpe específico. Chad não é base adequada para generalizar uma pessoa/arma ordinária. Não afirmar que se verificou imbuimento consciente nessa cena. Leitor VIZ do [capítulo 1](https://www.viz.com/shonenjump/bleach-chapter-1/chapter/5829) redirecionou no navegador ao índice da série, que encaminha capítulos disponíveis na localização ao MANGA Plus; extrator retornou interface sem quadros. Nenhum capítulo/episódio integral conferido nem fala não lida de Rukia registrada como prova.

**RAC-30B DECIDIDO após reformulação:** “Exigir capacidade espiritual do ataque (recomendado)”. Para ferir um corpo espiritual, o ataque/arma precisa de capacidade compatível; Zanpakutō, técnicas e golpes corporais espirituais próprios podem atender. Ver o alvo, possuir Energia ou escolher uma origem não espiritualiza automaticamente uma espada de ferro/flecha vanilla. Preparação de item requer capacidade/ficha apropriada; nenhuma skill universal de imbuimento concedida. Elegibilidade não garante acerto, dano suficiente ou ignorar proteções vanilla; estas seguem M06.

**RAC-30D DECIDIDO:** “Mesmo requisito espiritual para NPC e jogador (recomendado)”. O mesmo requisito vale para dano de mobs vanilla contra corpos espirituais jogáveis; exceção de dano normal contra o jogador não adotada. C preserva percepção, tentativa de ataque e serviços, sem garantir que um golpe incompatível cause dano. Corpos humanos materiais continuam vulneráveis ao dano comum; estado corporal atual rege a distinção. Isso reduz o perigo ofensivo de mobs comuns contra corpos espirituais, exigindo perfis de ameaça coerentes; não significa apagar gravidade/queda ou declarar imunidade a todo ambiente. Meios de treino, componentes mistos e riscos pertinentes precisam de ficha M06/M08/M09/M11 antes da entrega.

Respostas, perguntas originais, objeção, reformulação e D preservados em [histórico](historico/2026-10-23-propostas-m04-rac30abc-presenca.md); matriz aprovada em M04a/8/10 e referências aplicadas em M04/M06/M08/M09. Nenhuma pergunta pendente de RAC-30A/B/C/D. Regras do mod identificadas como produto aprovado, sem declarar todos os limites/exceções uma lei universal comprovada do cânone.

**Atual: RAC-31A PROPOSTO**, [pergunta enviada](historico/2026-10-23-proposta-m04-rac31a-corpo-e-local-reset.md): recomeçar no atendimento do NPC, usando estado inicial do novo prólogo e recusando integralmente se não couber, ou ir à área inicial humana. Uma resposta pendente. Heranças relidas em M04/criação/RAC-26–29/fechamento e perfis RAC-03/07–10, M08a/8.8 e M10b/9.6. Todas as origens iniciam no Mundo Humano em estados próprios; isso não cria um corpo humano inicial universal nem preserva o corpo avançado antigo. Gratuidade, confirmação, perdas/preservações, era e recompensas/itens por responsáveis mantidos; não aprovar cura ou teleporte por omissão. Próximo: aplicar corpo/local e seguir M06/Combat State e complementos necessários. Apenas documentação e pesquisa pública; sem código, mapas/saves, build, teste de jogo, commit ou push.


## 76. M04 — Reset no atendimento aprovado; retorno M06/Combat State aberto — 23/10/2026

**RAC-31A DECIDIDO** por “No atendimento do NPC (recomendado)”. Aplicado em M04/retorno dirigido, histórico, prestador M08a/8.8 e lugar M10b/9.6. Após informar perdas e confirmar a nova origem, concluir reset no atendimento no Mundo Humano, com estado inicial próprio do novo prólogo, conservando pés/direção se válidos. Validar espaço/acesso antes de apagar a jornada; se inválidos, recusar integralmente e preservar estado anterior. Não manter corpo avançado, teleportar à área inicial/Academia/Hueco Mundo ou remover blocos por omissão. Seleção antes do prólogo, serviço gratuito, perdas/preservações e era compartilhada continuam como aprovados. Recursos/itens vinculados, recompensas e operação íntegra continuam por ficha nos responsáveis, sem aprovar cura ou cópia de itens. Nenhuma pergunta pendente deste retorno M04; RAC-30A/B/C/D preservados.

Aberto [M06a — Estado automático de combate](06a-estado-de-combate.md) para COV-06, a partir de leituras dirigidas de M05/24, M06/5, M09/8.4–8.5/8.11 e consumidores de viagem/reset/evolução. O estado automático já é aprovado e separado da interface manual; faltam seus eventos funcionais, não apenas um número de duração. Herança RAC-30B/D exige compatibilidade do ataque contra corpo espiritual; detecção/tentativa da IA não prova dano ou ameaça efetiva.

**M06-CS-01A/B/C PROPOSTOS**, [perguntas integrais enviadas](historico/2026-10-23-propostas-m06-cs01abc-estado-de-combate.md), três respostas pendentes. A: envolvimento real, incluindo ataque evitado e apoio efetivo; não tag automática da party ou tentativa incapaz de afetar. B: ambiente sem origem hostil separado do combate, mas perigo incompatível impede operações pertinentes mesmo em estado OFF. C: janela sem conflito e sem ameaça efetiva, com compromisso de encontro preparado; mob sem rota/ataque viável não trava indefinidamente. Alternativas em M06a/8; nenhuma regra nova de entrada/ambiente/saída aprovada pela abertura, nenhum número final escolhido. Ciclo de vida/efeitos relevantes ainda precisa de integração funcional depois das respostas. M05 mantém regeneração, M09 perfis/encontros, M10 condições de viagem, M15/M16 autoridade/abuso/execução.

Regras de produto do mod, sem nova afirmação canônica, pesquisa externa ou escolha de API. Não foram auditados eventos de runtime, não alterados código, mapa ou save e não executados build/teste de jogo. M10 mantém fichas de conteúdo/implantação pendentes; publicação autorizada somente depois de concluir o bloco completo, sem commit/push antecipado.


## 77. M06 — Estado automático e continuidade aprovados — 23/10/2026

M06-CS-01A/B/C decididos por “Envolvimento real, mesmo sem perder Vida (recomendado)”, “Perigo separado do estado de combate (recomendado)” e “Cessar conflito e ameaça; respeitar encontro (recomendado)”. Aplicados em M06a/8.1–8.3/10 e histórico, preservando as perguntas. Referências nos consumidores M04/M05/M06/M07/M08/M09/M10: interface/party/presença não bastam; ataque/tentativa real compatível e apoio efetivo envolvem; ambiente sem origem hostil não ativa combate por si, mas perigo incompatível impede a operação; saída respeita janela sem conflito/ameaça e compromisso de encontro. Não alterar taxas de regen, proteção do corpo ou punições por essas escolhas.

M06-CS-01D decidido por “Preservar janela e reavaliar o contexto (recomendado)”. Janela restante preservada após morte/logout/transição válida/reinício pelo tempo de simulação, com reavaliação atual e sem rastrear agressor por memória ou restaurar tentativa interrompida. Sem portal comum em luta, reentrada presumida, multa adicional, comando restaurado, regen/treino offline ou reembolso de recursos. U/V/W e M09-06/08 preservados. Efeitos reais/autoria tratados em M06a/8.5, sem copiar resultados de jornada apagada, dar crédito ou inventar dano. O relógio dos mobs descarregados continua próprio de M09.

Fechamento funcional COV-06 em M06a/8.6: especificado conceitualmente no recorte, nenhuma pergunta pendente; números, classificação das fichas, UI, dados e implementação continuam entregas antes das funcionalidades. M08a/9.40 integra papéis/competências já escolhidos com presença e combate, sem inventar currículo ou eliminar dependências de SP/equipamento/era. Somente documentação; nenhuma nova pesquisa externa/API, auditoria de código, mundo/save, build ou teste de jogo para estas decisões. Publicação ainda não realizada.

## 78. M09 — Direção de fidelidade à obra e revisão das propostas — 23/10/2026

M09-12A recebeu “Quero que siga como a obra”; M09-12B recebeu “Quero que seja como na obra”. Não foi escolhida uma alternativa original. Registrada a direção de fidelidade e retiradas recomendações baseadas no formato do corpo ou na exclusão automática de aldeões. Texto integral dos rascunhos, perguntas e respostas preservados no histórico; perfis corrigidos em M09a/9.5–9.6. Funções de ataque de 11B e chefe opcional preparado de 11C permanecem aprovados, sem reinterpretar o pedido como revogação de toda adaptação anterior.

Pesquisa oficial ENC-S06–09 registrada em M09a/4: [Shueisha/Shonen Jump, arco inicial](https://www.shonenjump.com/xmedia/bleach/story/), verbete Hollow; [Pierrot, Hollow](https://pierrot.jp/title/bleach/keyword_ha.html); [TV Tokyo, episódio 12](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes01/index.html); [Pierrot, Menos Grande](https://pierrot.jp/title/bleach/keyword_ma.html). Limites de resumo editorial/glossário/sinopse; nenhum capítulo/episódio integral conferido. Referência predatória, interesse espiritual e variação individual não determinam temperamento pela anatomia, precisão numérica ou canibalismo obrigatório. Demais regras históricas dos verbetes não revisam M07 automaticamente.

Atual M09-12C proposto em M09a/9.7 e M09/12.26: aldeões como civis vivos caçáveis, ou restringir a caça a civis/Plus próprios de Bleach. Uma resposta pendente, pergunta enviada. É associação necessária entre universos, sem dizer que aldeões existem na obra. Consequências sobre vilas expostas explicitadas; proteção de atendimento essencial e padrão sem destruição de blocos preservados. Não aprova dieta de fauna/pets/golems, dano incidental, imunidade de observadores, entrada tardia ou instância privada por associação. Após a resposta, conferir perfis, fontes, circulação/retorno e o checkpoint autorizado antes da publicação. Nenhum código, mapa/save, build, teste de jogo, commit ou push nesta revisão.

## 79. M09-12C aprovado e conclusão do checkpoint de revisão — 23/10/2026

Resposta explícita: “Aldeões como civis vivos caçáveis (recomendado)”. Aplicada em M09/12.26, M09a/9.7 e fichas comuns, com pergunta/opções/rascunhos preservados no histórico. Aldeões representam civis vivos para a caça Hollow, adaptação aprovada; não é fato canônico sobre entidades Minecraft. Vilas expostas podem sofrer perdas. Permanecem proteção de prestadores essenciais, sentidos legítimos, padrão sem alteração de blocos e requisito espiritual de dano. Ser presa não concede visão/arma espiritual; fauna, pets e golems não recebem a mesma classificação por associação.

M10b/9.4.1 explicita o retorno pelos operadores já aprovados em 09B/12B: estadia na Soul Society → acesso externo pelo Rukongai → operador institucional → Karakura; estadia em Hueco Mundo → refúgio da rota comum → operador independente → Karakura. Corpo, acesso e contrapartida precisam ser demonstrados em cada ficha, inclusive convidado sem skill após encerrar a janela. Não há novo resgate, teleporte ou declaração de que qualquer base arbitrária já satisfaça o percurso. Substituto por era e implantação física precisam existir antes da oferta dependente.

O parecer M01–M10/12.1 conclui o checkpoint autorizado de revisão/complementos, preservando os catálogos em especificação e suas entregas. Próximo bloco principal M11, integrado ao retorno M05 sobre sobrevivência/recursos/itens; COV-03 e COV-04 mantêm responsáveis M02/M03 antes dos percursos/campanhas M12/M13. Publicação documental segue a autorização já registrada; nenhuma funcionalidade, mapa, save, asset, build ou teste de jogo foi produzido nesta conclusão.

### 79.1. Sincronização anterior à publicação — 23/10/2026

`git fetch origin main` encontrou 25 commits após a base documental `3a37ca7`; `git merge --ff-only --no-stat origin/main` avançou o checkout para `18786a622fd4ebf2ca3fb42c54986b8858fdabb4`, preservando os 76 arquivos locais de documentação. A diferença upstream não alterava `Docs/game-design/`. Os avanços existentes de código/assets/poderes foram incorporados do repositório, sem autoria nova de runtime nesta rodada ou revalidação de suas evidências. Minecraft 1.20.1, Forge 47.4.10 e Java 17 reconferidos em `gradle.properties`/`build.gradle`. Não houve build, teste ou atualização do JAR do launcher. O commit desta publicação acrescenta somente o lote explícito de game design sobre essa base; o histórico Git identifica o resultado.

## 80. M11 — Comércio material aprovado e abertura do catálogo — 23/10/2026

M11-01A recebeu “Comércio material; SP para progressão (recomendado)”. Comida, materiais e reposições negociados por comércio comum usam itens/moeda material; SP permanece em atributos/skills/formas, Mastery pela prática. Aplicado em M11/8.1, M05/11.3 e catálogo M08. Moedas, preços, transferência/conversão e estoques não foram escolhidos. Primeiro fornecimento gratuito também não decorre dessa resposta.

M11 aberto no template de quinze seções, com cobertura de instrumentos/equipamentos, consumíveis/abastecimento, materiais/drops, meios de troca, viagem, abrigo e itens de missão. M11-01B propõe primeira shinai pela Academia e primeiro foco pelo mentor Quincy, na orientação e sem cobrança material inicial; alternativa compra/crafting antes do treino, uma resposta pendente. Aprendizado/compra SP continuam separados, Asauchi fica posterior à formação. Sobrevivência/fome/regeneração e recursos ao renascer mantêm retorno M05 antes de fechar consumíveis.

Leitura dirigida em `26b73b3`: ModItems principal registra armas/viagem/ovos; outro ModItems contém ovo Hollow; ZanpakutoItem sem desgaste/reparo convencional; receita de Asauchi com ferro/papel/graveto; loot Hollow com `pools` vazio. Isso não aprova receita definitiva, proteção contra toda perda ou ausência de recompensas em outros mecanismos. Nenhuma mudança de código/receita ou execução de teste.

ITEM-S01/S02 em M11/4 usam trechos oficiais indexados do personagem Nimaiya e sinopse TYBW 13. Abertura direta das páginas retornou 403; sem leitura integral de página/capítulo/episódio. Evidência limitada à criação da Zanpakutō e prova de Asauchi no caso de Ichigo; não comprova receita, método completo de fabricação ou trajetória obrigatória de todo aluno. Próxima conciliação de obtenção precisa preservar esses limites e o caminho de M04.

## 81. M11 — Entrega inicial e crafting coexistentes — 23/10/2026

**M11-01B decidido com ajuste. Resposta literal:** “Sua recomendação não implica em poder também compra/crafting eles coexistem, mas concordo que os primeiros instrumentos serão fornecidos na introdução, mas quando quebrar o jogador terá que craftar”. Registrada em M11/12.2 e histórico; perfis materiais em 10.1/10.2 e fontes M08 atualizados. A pergunta original opunha caminhos compatíveis; fornecimento na introdução não exclui compra/fabricação. Primeira shinai/foco fornecidos no contexto aprovado; após quebra, crafting repõe o exemplar. Não é reposição gratuita ilimitada pelo mentor, recompra SP da skill ou fabricação de qualquer arma espiritual. Materiais/receitas/posse/perda/reparo/reset continuam por ficha.

**M11-01C proposto:** foco Quincy sem desgaste por flecha, preservando possibilidade de perda/destruição conforme a futura ficha, ou desgaste por flecha emitida. Uma resposta pendente. A possibilidade de quebra mencionada em B não seleciona automaticamente dano por tiro nem torna o foco imune a todo dano. Shinai, Asauchi/Zanpakutō e dano estrutural de Bankai mantêm recortes distintos. Proposta de gameplay, sem nova alegação de cânone/API ou pesquisa externa. Código ZanpakutoItem relido: ausência de desgaste/reparo comum no runtime não decide o funcionamento de shinai/foco ainda por realizar.

Retorno M05 sobre alimentação/regeneração/recursos ao renascer permanece antes de fechar provisões; sem avanço de implementação ou publicação nesta rodada.

## 82. M11 — Arco de Reishi e Cruz física: esclarecimento — 23/10/2026

**Resposta recebida a M11-01C:** “Mas o arco quincy é material ou é criado por eles? Se for criado por reishi então não faz sentido ter desgaste mas se for o material então concordo que deveria desgastar”. Pergunta de esclarecimento com critério condicional, não seleção automática de durabilidade para a Cruz. M06-09J relido integralmente: exige foco físico equipado, Cruz como referência, e distingue-o do arco temporário de Reishi. Crafting da reposição de 01B aplica-se ao instrumento material. Não inventar arco básico físico ou exigir fabricar a manifestação.

ITEM-S03/S04 registrados em M11/4: glossário oficial TYBW, trecho indexado de Sanrei sobre reunir Reishi para formar arco/flechas, com abertura 403; glossário oficial Pierrot ka acessível, verbetes do arco espiritual e Cruz Quincy. Material do anime complementar; sem leitura integral de capítulos/episódios e sem fonte de uma taxa de desgaste da Cruz por disparo. Não importar regras de jogos licenciados ou traduções de fandom como cânone.

M11-01C reformulado explicitamente para a Cruz física: não desgastar por flecha (recomendação) ou desgastar por flecha. Uma resposta pendente; posse, perda/destruição, slot, receita e demais regras mantêm suas fichas. A/B preservados, sem mudança de código, receita, build, teste ou publicação.

## 83. M11 — Cruz sem desgaste aprovada; retorno M05/02A aberto — 23/10/2026

**M11-01C decidido. Resposta:** “Cruz sem desgaste por disparo (recomendado)”. Aplicado em M11/8.5/10.2/12.4, histórico e M08. Mantém arco como manifestação de Reishi, foco físico equipado e Energia por tiro; não declara Cruz indestrutível ou resolve perda/destruição. Custo material por quebra e obtenção inicial mantêm M11-01B, sem recompra SP de skill persistente.

**M11-02A proposto:** comida comum compatível repõe alimentação/saciedade, sem recarga direta de Vida/Stamina/Energia; recuperação mantém regras de M05/Combat State, sem duplicação de cura automática. Alternativa permite acelerar Energia fora de combate. Uma resposta pendente, em M11/12.5, M05/17.1 e histórico próprio. A proposta trata efeito de alimento compatível, não dieta, fome obrigatória, dano de inanição ou limiar nutricional de todas as origens. Comidas especiais/curativos/poções mantêm fichas; não remover maçã dourada nem tornar toda comida equivalente a ela. Predação de M07 permanece separada de cura e regressão.

ITEM-S05: [guia oficial Minecraft sobre saúde](https://www.minecraft.net/en-us/article/health-minecraft), seções de fome/alimentação, associa comida, fome e regeneração; ITEM-S06: [Taking Inventory: Apple](https://www.minecraft.net/en-us/article/taking-inventory-apple), distingue maçãs e efeitos da maçã dourada. Consultados em 23/10/2026; guias gerais não são especificação Java 1.20.1/Forge nem dieta canônica de Bleach. Pesquisa inicial de alimentação espiritual não foi suficiente para escolher toda dieta racial; não sustentar uma regra geral com uma refeição especial ou conteúdo secundário.

Leitura dirigida de TickHandler e trechos pertinentes de ResourcesData: orquestração de Stamina/manutenção/recuperação de Energia e registros próprios; não foi encontrada uma integração de comida nesses blocos. Isso não prova ausência de toda interferência de alimentação, pois comportamento vanilla herdado/eventos e demais serviços precisam de revisão técnica própria. Sem alteração de código, taxas, receitas, gamerules, save, build ou teste nesta rodada.

## 84. M11 — Comida comum aprovada; perfil humano em proposta — 23/10/2026

**M11-02A decidido. Resposta:** “Comida comum sem recarga de recursos (recomendado)”. Aplicada em M11/8.6/12.5, M05/17.1 e histórico. Repõe alimentação/saciedade quando compatível, sem recarga direta de Vida/Stamina/Energia ou acelerador comum de Energia fora de combate. Recuperações naturais mantêm os critérios próprios; alimentos especiais/curativos/poções têm fichas distintas.

**M11-02B proposto:** manter alimentos/barra de fome vanilla para Quincy/Fullbringer em corpo humano material, separados de Stamina/Energia e sem duplicar cura; alternativa alimentação opcional sem fome obrigatória nesses corpos. Uma resposta pendente. Isso não escolhe alimentação Shinigami/Hollow/Arrancar, fome de civis/Plus, limiares, inanição ou recursos ao renascer. Perfil humano proposto a partir de M04; demais perfis precisam de referência/adaptação explícita.

Pesquisa de 23/10/2026 em M11/4, ITEM-S07–09: [Shonen Jump, história/verbete Hollow](https://www.shonenjump.com/xmedia/bleach/story/) e [Pierrot, Hollow](https://pierrot.jp/title/bleach/keyword_ha.html) sustentam consumo de almas; [sinopse TV Tokyo 32](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes03/index.html) contextualiza Rukia/Renji, sem reproduzir a explicação completa de fome/poder. Trecho indexado do [glossário TYBW](https://bleach-anime.com/keyword/) descreve refeição espiritual especial, sem provar efeito de qualquer alimento. Resumos/glossários não equivalem a capítulos/episódios integrais; nem ausência de detalhe numa sinopse demonstra inexistência na obra. Dieta e frequência espiritual seguem abertas, sem importar fanon ou transformar exemplo excepcional em regra universal.

Sem código, receitas, gamerules, save, build, teste ou publicação novos. Documentação local após `26b73b3`.

## 85. M11 — Perfil humano aprovado e proposta Shinigami — 23/10/2026

**M11-02B decidido. Resposta:** “Preservar alimentação e fome nos corpos humanos (recomendado)”. Aplicada em M11/8.7/12.6, M05/17.1 e histórico próprio. Quincy/Fullbringer em corpo humano material mantêm alimentos/barra de fome vanilla, separados de Stamina/Energia e com recuperação conforme M05/02A. Não estende dieta humana a outros corpos ou cria consumo de fome por cada técnica espiritual.

**M11-02C proposto:** representar fome Shinigami na barra existente com alimentos espirituais comuns próprios, produção/comércio em Soul Society e acesso apropriado no Mundo Humano; alternativa aceita também comida vanilla comum. Uma resposta pendente, M11/12.7, M05 e histórico. A restrição alimentar não foi comprovada como exigência canônica universal pelas referências disponíveis; é escolha explícita de adaptação, com consequências diferentes para abastecimento e cooperação. Antes da oferta, concretizar produto/fonte/percurso sem exigir um suprimento inacessível; nenhuma entrega grátis, transformação automática de itens no portal ou fonte única universal inferida.

Hollow/Arrancar, Gigai/híbridos e fome de civis/Plus não decididos por este perfil. Recuperação/penalidades nutricionais e recursos ao renascer seguem M05/M11; sem regressão racial, cura da predação ou nova barra espiritual por associação. Nenhuma implementação, teste, mapa/save, build ou publicação novos.

## 86. M11 — Comida vanilla Shinigami e funções especiais — 23/10/2026

**M11-02C decidido com ajuste. Resposta literal:** “Acredito que a parte de comida para encher apenas a fome possa ser feitos tanto com as comidas do vanilla mas que as outras formas especificas de cada raça tenham outras funções além e também de encher a fome, e como funcionam na obra também”. Aplicada em M11/8.8/12.7, catálogo 10.3 e retorno M05. Aceita comida vanilla para nutrir Shinigami espirituais, sem exclusividade alimentar espiritual e mantendo 02A. Alimentos próprios podem nutrir e oferecer efeitos específicos; não inventar um bônus ou item obrigatório por raça. Efeitos e obtenção concretos continuam por ficha.

**Pesquisa ITEM-S08/S10:** o [glossário oficial TYBW](https://bleach-anime.com/keyword/), verbetes 臥豚殿 e 虚圏, descreve respectivamente a refeição especial de Hikifune incorporando outra Reiatsu e elevando o patamar de poder, e a nutrição suficiente pela respiração de Hollows comuns no Reishi atmosférico de Hueco Mundo. Consulta indexada em 23/10/2026; abertura direta 403, sem leitura integral de capítulo/episódio. Não generalizar para todo alimento, estágio ou recurso.

**Atual M11-02D proposto:** sustento ambiental para o perfil inicial Hollow comum em Hueco Mundo, sem cura, reposição de Stamina/Energia ou predação evolutiva. Uma resposta pendente; associar o caso geral da fonte ao estágio jogável é adaptação explícita. Demais perfis e alimentação fora desse ambiente continuam por definir, com estabilidade racial preservada. Nenhuma implementação, teste ou publicação novos.

## 87. M11 — Sustento ambiental aprovado e pacote alimentar Hollow — 23/10/2026

**M11-02D decidido. Resposta:** “Nutrição ambiental em Hueco Mundo (recomendado)”. Aplicada em M11/8.9/12.8, M05/17.1 e histórico. Sustento ambiental apenas do perfil inicial aprovado; sem cura direta, Stamina/Energia, ganho offline ou evolução. Mantém a distinção nutricional de ITEM-S10 e não estende a suficiência aos demais estágios por inferência.

**Atual M11-02E proposto:** barra de fome existente/comida vanilla para os perfis restantes Hollow/Arrancar; concluir devoração já elegível também nutre, sem refill de recursos ou nova lista de presas. Alternativa mantém a alimentação comum, deixando a devoração somente evolutiva. Uma resposta pendente. M04/H2 não recebe segunda barra ou cota obrigatória de almas. M07/8.33 preserva estágios, mesmo sem caça recorrente.

Consulta à [publicação oficial do episódio 167](https://bleach-anime.com/news/?id=60931), sinopse indexada, confirma predação e risco de regressão Adjuchas na referência do anime. Abertura direta 403; sem episódio/capítulo integral. Esse contexto já era tratado por BL-25/26 de M07; não determina dieta vanilla, saciedade do mod ou revogação da estabilidade aprovada. Demais regras nutricionais e efeitos especiais seguem nas fichas M05/M11. Sem código, build, teste ou publicação novos.

## 88. M11 — Alimentação Hollow aprovada e consequências nutricionais — 23/10/2026

**M11-02E decidido. Resposta literal:** “Comida comum + devoração nutritiva (recomendado)”. Aplicada em M11/8.10/12.9 e nos contratos M04/H2, M05/17.1 e M07/8.35. Barra de fome existente e comida vanilla para os perfis restantes; concluir devoração já elegível também sacia. Sem ampliação de presas, cura/refill de recursos, nova evolução Arrancar ou regressão racial. 02D não se estende automaticamente aos demais estágios.

**Atual M11-03A/B propostos:** A, nutrição suficiente como condição da recuperação natural lenta de Vida, sem bloquear recuperação própria de Stamina/Energia só por fome; B, corrida comum impedida por fome baixa e inanição por dificuldade, ou o mesmo bloqueio sem dano. Duas respostas pendentes em M11/12.10/12.11 e retorno M05/17.2. Sustento ambiental aprovado conta no seu perfil; alimento comum não cria cura rápida adicional. Valores, integração técnica e recursos ao renascer continuam por fechar.

Apoio geral ITEM-S05 sobre alimentação/fome/recuperação de Minecraft; novas buscas não certificaram regras completas Java 1.20.1. Não usar posts comunitários, Bedrock ou notas de versões posteriores como especificação do projeto. Propostas de produto, sem nova regra canônica de Bleach ou comportamento integrado afirmado como testado. Nenhum código, build, teste ou publicação novos.

## 89. M11 — Recuperação/fome aprovadas; recursos ao renascer em proposta — 28/10/2026

M11-03A/B, preparados em 23/10/2026, receberam hoje as respostas literais “1. Sim / 2. Concordo com a recomendação”, na reapresentação das perguntas. [Registro de aprovação](historico/2026-10-28-decisoes-m11-03ab-recuperacao-e-inanicao.md). A: nutrição suficiente permite recuperação natural lenta de Vida, sem cura rápida somada ou bloqueio da recuperação própria de Stamina/Energia apenas por fome. B: fome baixa limita corrida comum e inanição segue dificuldade, podendo matar no Difícil; demais ações conservam regras/recursos e estágios raciais permanecem. Aplicado em M11/8.11/8.12/12.10/12.11, M05/17.2 e referência M04/H2. Fontes alimentares e elegibilidade da devoração permanecem aprovadas; nenhuma nova pesquisa externa ou regra canônica nesta rodada.

M11-04A propõe recursos ao renascer após morte comum: Vida/Stamina/Energia cheias nos limites válidos do corpo-base permanente e barra de fome cheia, com saciedade oculta inicial por ficha. Alternativa: Vida/fome cheias e Stamina/Energia parciais. Uma resposta pendente em M11/12.12 e M05/17.3; [pergunta/limites](historico/2026-10-28-proposta-m11-04a-recursos-ao-renascer.md). Não inclui login, sono, viagem ou correção de posição; preserva formas encerradas, tempos e consequências especiais. Itens/XP permanecem por decidir.

Leitura dirigida em `26b73b3` de `CapabilityEvents.clone`/`respawn`: cópia e limpeza transitória; evento de retorno preenche Stamina e o recurso legado `currentReiatsu`, sem distinção explícita de causa no método. Isso não demonstra a política completa de morte, Vida/fome, efeitos, consequências ou sincronização observada em jogo. Nenhum código/receita/gamerule/save, build, teste de jogo ou publicação alterado/executado. Registros 80–88 conservam suas datas e estados históricos; a aprovação atual não retroage a 23/10.

## 90. M11 — Recursos ao renascer aprovados; instrumentos básicos em proposta — 28/10/2026

M11-04A recebeu “Vida, Stamina, Energia e fome cheias (recomendado)”. Aplicado em M11/8.13/12.12 e M05/17.3; [registro integral](historico/2026-10-28-proposta-m11-04a-recursos-ao-renascer.md). Ao renascer após morte comum, recursos cheios nos limites válidos do corpo-base permanente e barra de fome cheia; saciedade oculta inicial por ficha, sem saturação máxima presumida. Não estende reposição a login/viagem/sono, não restaura formas ou consequências especiais, não zera tempos e não decide inventário/XP/efeitos universais. A evidência de código de 89 continua uma leitura dirigida, sem teste ou aprovação por existência.

M11-05A propõe posse/perda/reposição de shinai e Cruz básicas: transferíveis, inventário na morte conforme regras do mundo e ampliação do crafting para perda, além da quebra já aprovada. Alternativa: vínculo pessoal, sem transferência e com preservação na morte. Uma resposta pendente em M11/12.13; [pergunta e limites](historico/2026-10-28-proposta-m11-05a-posse-instrumentos-basicos.md). Não inclui Asauchi/Zanpakutō, armas Arrancar ou vetores Fullbring e não reabre primeira entrega/crafting por quebra ou desgaste por tiro. Próximas fichas fecham receitas, desgaste, reposição e conciliação da Asauchi; parâmetros de sobrevivência mantêm entrega M05 antes da realização.

Somente documentação local; sem nova pesquisa externa, alteração de código/receitas/gamerules/saves, build, teste de jogo ou publicação. Proposta de produto não equivale a cânone ou funcionalidade implementada.


## 91. M11 — Vínculo conforme a obra; consequências separadas — 28/10/2026

Resposta literal a M11-05A: “Se na obra são objetos vinculados ao usuário então deveria ser vinculados, se não então podemos adotar isso de guardar em báu e etc”. Direção registrada em M11/8.14/12.13 e fichas 10.1/10.2; [pergunta original e aplicação](historico/2026-10-28-proposta-m11-05a-posse-instrumentos-basicos.md). Não é escolha integral das alternativas anteriores. A shinai inicial definida no projeto como instrumento de treino admite guarda/empréstimo; vínculo de poder, posse física e capacidade de uso são propriedades distintas. Proteção na morte, perda normal e crafting por perda continuam sem aprovação nesta resposta; 01B/01C permanecem.

Pesquisa ITEM-S11–14, M11/4: [Pierrot](https://pierrot.jp/title/bleach/keyword_ka.html), página acessível, confirma a Cruz especial carregada pelos Quincy, sem resolver exclusividade individual. [Bandai](https://www.bandai.co.jp/catalog/item.php?jan_cd=4543112355577000), descrição de produto licenciado em trecho indexado, associa-a ao sucessor legítimo; abertura em manutenção. [VIZ, capítulo 186](https://www.viz.com/shonenjump/bleach-chapter-186/chapter/14407), conteúdo por assinatura, não lido; herança referida em fontes secundárias é pista de pesquisa, não confirmação primária. [TV Tokyo, episódio 46](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes04/index.html), sinopse acessível, contextualiza a formação, sem detalhar vínculo da shinai. Essas fontes não provam ausência universal de vínculo; classificação concreta da Cruz continua em verificação. Não importar mecânicas de acessórios de outros jogos como cânone.

Próximo: completar classificação canônica e separar a política de morte/reposição do uso; depois desgaste/receitas e conciliação da Asauchi. Não repetir a pergunta binária de 05A. Somente documentação local; nenhuma alteração de código, receitas, gamerules ou save; sem build, teste de jogo ou publicação.


## 92. M11 — Perguntas agrupadas de instrumentos e Asauchi — 28/10/2026

Pedido de continuidade com várias perguntas por rodada e referência à obra; não é aprovação das propostas. M11/12.14–12.17: 05B, perda conforme inventário do mundo e crafting após perda para instrumentos comuns sem vínculo; 05C, desgaste da shinai por golpe acertado/bloqueio efetivo; 05D, espaço próprio de equipamento para foco real, sem mudar o arco de duas mãos; 06A, recebimento de Asauchi na formação sem receita comum. [Quatro perguntas, alternativas e limites](historico/2026-10-28-propostas-m11-05bcd-06a-instrumentos-e-asauchi.md), quatro respostas pendentes. 05A permanece com vínculo conforme a obra; a exclusividade individual da Cruz continua em verificação.

ITEM-S15: nova busca do [glossário oficial TYBW](https://bleach-anime.com/keyword/) atribui a forja das Asauchi a Ōetsu Nimaiya. [Sinopse oficial do episódio 13](https://bleach-anime.com/en/story/?id=13) contextualiza a prova de Ichigo. Ambas indexadas, abertura direta 403; sem capítulo/episódio integral, prova de fabricação por jogador ou rito universal obrigatório. A recomendação de retirar crafting comum é adaptação fundamentada nessa origem, preservando a rota M04; nenhum recebimento, reposição ou uso específico se torna cânone por associação. Receita local de ferro/papel/graveto conferida e mantida intacta.

Perguntas de sobrevivência, desgaste e interface distinguem adaptação de conteúdo da obra. Sem APIs novas, alteração de código, receita, gamerule ou save; sem build, teste de jogo ou publicação. Próximo: registrar respostas e detalhar materiais/receitas/reposição, sem repetir decisões herdadas.


## 93. M11 — Respostas dos instrumentos e Asauchi; STR-08 explícito — 28/10/2026

[Respostas e perguntas originais](historico/2026-10-28-propostas-m11-05bcd-06a-instrumentos-e-asauchi.md): 05B “Vamos seguir com o recomendado”, perda comum conforme mundo e crafting por perda; 05C “Desgaste”, confirmação parcial que ainda não distingue acertos de bloqueios; 05D “Espaço próprio”, equipamento real de foco separado das mãos/slots de técnicas; 06A decidido com ajuste de missão repetível com Ōetsu após a formação, uma Asauchi por nova conclusão válida. Sem receita comum no design futuro; arquivo existente intacto. Não é farm por reenviar a mesma conclusão nem nova compra de poder.

Resposta posterior “Perfeito, recomendação aprovada” refere-se à recomendação comunicada de intermediação inicial pela Academia e encontro pessoal com Ōetsu quando a história permitir: 06B aplicado em M11/8.19/12.19 e fichas M04/M08. [Registro e perguntas restantes](historico/2026-10-28-propostas-m11-desgaste-acesso-oetsu-e-repeticao.md); 05C e 06C ainda aguardam duas respostas, sobre gatilhos de desgaste e materiais/prova por execução. Não aplicar a aprovação de acesso a perguntas não apresentadas.

Esclarecimento narrativo do usuário registrado como STR-08 em M03 e aplicado em M04/M08: formação Shinigami pertence à campanha racial; campanha principal segue acontecimentos principais da obra; fillers são side quests opcionais. Preserva D05/STR-02 e adaptações de ligação, sem tornar formação um evento principal canônico ou filler obrigatório. A obtenção repetível de Asauchi segue caminho racial, não reencena a história principal a cada exemplar.

ITEM-S16: [perfil oficial de Ōetsu](https://bleach-anime.com/en/character/?chara=58) o identifica na Divisão Zero; [episódio 9](https://bleach-anime.com/en/story/?id=09) contextualiza condução de Ichigo ao Palácio Real. Trechos oficiais indexados, abertura direta 403; sem episódio integral ou prova de proibição universal de contato com recém-formados. A intermediação da Academia é adaptação aprovada, não procedimento canônico confirmado. M10 ainda precisa de ficha do acesso pessoal apropriado. Sem código, receita, gamerule ou save alterado; sem build, teste de jogo ou publicação.


## 94. M11 — Desgaste e requisitos de Asauchi aprovados; arma pessoal em proposta — 28/10/2026

À rodada de duas perguntas, respostas literais 1. “Concordo com o recomendado”; 2. “Concordo com o recomendado”. [Registro](historico/2026-10-28-propostas-m11-desgaste-acesso-oetsu-e-repeticao.md). 05C decidido: shinai desgasta por golpe acertado/bloqueio efetivo, não por bater no ar ou manter guarda. 06C decidido: materiais apropriados e tarefa prática por execução da missão de Asauchi, sem refazer formação. Aplicados em M11/8.16/8.20/10.1/10.4/12.18/12.20 e retornos M04/M08; quantidades/produtos/fontes/prova ainda por ficha. Conjunto anterior sem perguntas pendentes; preservar STR-08 e a intermediação de 06B.

Novas propostas 07A/B, [duas perguntas](historico/2026-10-28-propostas-m11-07ab-zanpakuto-morte-e-desgaste.md), M11/12.21/12.22: preservar na morte comum o exemplar pessoal Shinigami que estava com o dono e não aplicar desgaste comum na forma selada/Shikai. Nenhuma resposta ainda. Não concedem recall de item já fora da posse nem reparo de Bankai e não se estendem a outros tipos de arma. ITEM-S17 registra referência/limites; M07 conserva as regras aprovadas de manifestações/perda de posse/quebra estrutural. Respawn e durabilidade Minecraft são escolhas de adaptação, não fatos canônicos.

Próximo: registrar respostas e continuar recuperação concreta/perfis de armas e fichas materiais. Sem alterações de código, receita, gamerule ou save; sem build, teste de jogo ou publicação.

## 95. Prioridade alterada — somente campanha principal — 28/10/2026

M11-07A/B aprovados por “1. Concordo / 2. concordo”, aplicados em M11/8.21/8.22/10.5/12.21/12.22: preservação da arma pessoal na morte comum e ausência de desgaste comum selada/Shikai, sem limpar histórico de dano ou consequências. Na sequência, o usuário pediu adiar o restante do módulo e avançar para quests/campanha, esclarecendo depois foco exclusivo na campanha principal. Formação/quests raciais, fillers e demais histórias laterais ficam para depois. Não usar o rascunho de formação que havia sido apenas anunciado como próxima tarefa.

[M13](13-campanhas-e-sidequests.md) aberto em especificação, com rascunho de acontecimentos do arco Shinigami Substituto e M13-01A/B propostos para abertura/cobertura. Ordem macro, personagem original e vitória jogável preservam M03; M12 ainda sem documento próprio, com dependências de realização registradas. A autorização de antecipar roteiro não fecha sistemas ou pendências de itens. Fontes/limites constam em M13/4. Nenhuma implementação, build, teste de jogo ou publicação novos.

## 96. M13 — Missões executadas pelo jogador, NPC ou painel — 28/10/2026

O usuário substituiu a abertura encenada por história escrita/contexto breve, apresentação do NPC e ação do jogador na missão. Evento autônomo pode iniciar por botão na área de missões do HUD; confronto cria o Hollow/inimigo previsto. Direção literal registrada em M13/8.1, 01A revisado e 02A decididos, retorno em M03. M09-06A já prevê começo deliberado; a ação “Iniciar missão” é compatível sem confirmação repetida universal. Contexto/requisitos e ator da tentativa precisam de validação e não se aprova início remoto de todo evento.

Catálogo inicial de M13/10.1 reformulado por fonte, objetivo e início; 10.3 oferece exemplo revisável com Rukia/confronto Hollow. 01B não aprova sete missões e a pergunta antiga não deve ser repetida. Próximo: definir primeira missão principal por ação/ator/conclusão; formação racial, fillers, laterais e M11 adiados. Pesquisa adicional do nome do primeiro Hollow não obteve confirmação primária no recorte mangá/anime; referência do filme foi distinguida e não usada para fechar identidade/versão. Sem alteração de runtime, quest JSON, receita ou save; sem build, teste de jogo ou publicação.


## 97. M13 — Primeira ficha jogável e duas escolhas novas — 28/10/2026

Após “Perfeito, pode seguir”, MAIN-SUB-01 foi desenvolvida em M13/10.3 como proposta: Rukia, área designada de Karakura, início deliberado pelo NPC, um Hollow da abertura, vitória do jogador/party e registro do próximo caso. Não exige coleta ou nova formação; não concede habilidade ou assume raça universal. M13-03A/B, em 12.4/12.5, aguardam escolha da abertura e da conclusão após objetivo ou retorno ao NPC. Autorizar continuação não aprovou automaticamente essas regras.

[TV Tokyo, episódios 1–13](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes01/index.html), sinopses dos episódios 1–2 reconferidas: ameaça Hollow e Rukia no começo, com referência à luta da noite anterior. Nome próprio e repertório exato do alvo não foram confirmados nesse conteúdo; perfil direto simplificado e fluxo da quest são adaptações propostas. M09 mantém contratos de tentativa/falha; M12/M15 recebem resultado/participação, sem recompensa ou replay inventados. Depois das respostas, seguir para o conflito do irmão de Orihime. Somente campanha principal e documentação local; sem código, build, teste de jogo ou publicação.


## 98. M13 — Abertura/conclusão aprovadas; proposta do caso de Orihime — 28/10/2026

Duas respostas “Vamos seguir como você recomendou” aprovam M13-03A/B: primeiro confronto com Rukia em Karakura antes do caso de Orihime; concluir após objetivo validado e exigir retorno ao NPC quando houver entrega/conversa necessária. Aplicado em M13/8.2/10.3/12.4/12.5; não repetir essas perguntas. Detalhes ainda pendentes e demais entradas do arco não foram aprovados por associação.

CAMP-S03 — [Pierrot, retrospectiva oficial Substituto, episódios 1–20](https://pierrot.jp/title/bleach/playback_01.html), acessível em 28/10/2026, complementa CAMP-S01 para o irmão de Orihime: retorno à lucidez e partida voluntária com Zanpakutō. Não se consultou capítulo integral. MAIN-SUB-02 propõe a vitória do jogador seguida de desfecho breve em texto, preservando contexto de purificação sem atribuí-la a qualquer arma/poder. Não importar os poderes futuros de Orihime para esta etapa.

Duas escolhas novas M13-04A/B em 12.6/12.7: encaminhamento por Rukia ou pedido direto de Orihime; confronto direto ou defesa da personagem com condição própria de falha. Recomendados Rukia e confronto direto. Depois, caso de Chad/periquito. Somente campanha principal; M11/racial/lateral adiados, sem runtime/build/teste/publicação.


## 99. M13 — Caso de Orihime aprovado; Chad e Kon propostos — 28/10/2026

Respostas literais “Concordo” e “Prefiro vencer o Hollow” aprovam 04A/B: Rukia encaminha, jogador vence o irmão Hollow, sem defesa de Orihime como objetivo. Aplicado em M13/8.3/10.4/12.6/12.7; não repetir perguntas nem transformar escolha local em proibição universal de defesa de NPCs.

CAMP-S01/03 reconferidos: episódios 4–5 dão Chad/periquito/Shrieker e referência a armadilhas; retrospectiva Pierrot descreve o destino ao Inferno por crimes em vida. Episódios 6–7 e retrospectiva contextualizam a alma modificada, ocorrência perto da escola e Kon. Fonte oficial complementar do anime, sem capítulo integral. Não inferir capacidades exatas, acesso explorável ao Inferno ou recompensa de alma/pet.

MAIN-SUB-03/04 em M13/10.5/10.6 e M13-05A/B em 12.8/12.9 são propostas: recomendar Chad → Shrieker direto (alternativa investigação curta); recomendar incidente de Kon jogável, encaminhado por Urahara com início no painel na área da escola e confronto com o Hollow daquela ocorrência (alternativa só contexto). Duas respostas pendentes; depois Grand Fisher. M11/racial/lateral adiados; sem implementação/teste/publicação.


## 100. M13 — Shrieker/Kon aprovados; proposta Grand Fisher — 28/10/2026

Resposta conjunta “Concordo com as recomendações” aprova 05A/B: Chad apresenta e jogador enfrenta Shrieker diretamente; incluir missão curta de Kon por Urahara, com início no painel na área da escola e confronto com o Hollow da ocorrência. Aplicado em M13/8.4/10.5/10.6/12.8/12.9, sem aprovar perfis completos, números, mapas, acesso/participação ou recompensas.

CAMP-S01 (8–9) e CAMP-S03 reconferidos em 28/10/2026: isca com aparência humana, contexto da família Kurosaki/cemitério e Grand Fisher escapando do primeiro confronto. Sinopses/retrospectiva oficiais do anime, não capítulos integrais. M13-06A, em 10.7/12.10, propõe vencer a representação inicial e depois registrar retirada narrativa em texto, preservando vitória e continuidade; uma resposta pendente. Sem parentesco do jogador com a família Kurosaki ou mistura com forma posterior.

Após essa escolha, selecionar a ligação até a crise de Uryū e detalhar os objetivos seguintes; não assumir cobertura de cada episódio nem exclusão automática de casos intermediários. Somente campanha principal; M11/racial/lateral adiados; sem implementação/teste/publicação.


## 101. M13 — Grand Fisher aprovado; cobertura Don Kanonji e crise de Uryū — 28/10/2026

“Positivo, adotamos” aprova 06A: vitória contra Grand Fisher e fuga no desfecho; aplicado em M13/8.5/10.7/12.10, sem morte definitiva ou transferência da biografia de Ichigo. Detalhes de realização pendentes; não reenviar essa escolha.

CAMP-S04 — [Shueisha, volume 4](https://www.shueisha.co.jp/books/items/contents_amp.html?isbn=4-08-873310-X&title=www.shueisha.co.jp%2Fbooks): trecho oficial indexado situa Don Kanonji em Karakura/gravação no hospital. Duas aberturas falharam; não houve acesso ao capítulo ou página integral. Sustenta que o caso é do mangá, sem classificá-lo como filler. 07A propõe contexto escrito de ligação; alternativa é missão do hospital, ainda sem ficha aprovada.

CAMP-S01 (11–13), CAMP-S02 (14) e CAMP-S03 reconferidos: desafio/isca, crise Hollow e Menos afastado. 07B propõe duas missões com vitórias do jogador: grupo de Hollows e Menos; início local do boss pelo painel; conclusão da primeira persiste se a segunda falhar. NPCs canônicos não entram como aliados em combate; recuo do Menos é desfecho proposto específico, não regra universal. Quantidades/ondas, ficha do Menos, mapa, participação e recompensas continuam pendentes. Duas respostas em M13/12.11/12.12, depois bloco de Rukia. Somente campanha principal; demais trabalhos adiados, sem implementação/teste/publicação.


## 102. M13 — Ligação/Quincy aprovados; Renji e expedição propostos — 28/10/2026

“gostei da forma como propôs, muito bom, podemos seguir” aceita o pacote 07A/B: Don Kanonji em texto, crise de Uryū em duas missões Hollows/Menos, conclusão da primeira mantida quando o boss falha. Aplicado em M13/8.6/10.8/10.9/12.11/12.12; não reenviar. Números/capacidades/mapa/participação/recompensas continuam pendentes.

CAMP-S02 (16–21) e CAMP-S03 reconferidos em 28/10/2026 sustentam aviso de Kon sobre saída de Rukia, emissários, prisão e preparação/expedição de Urahara. Não são capítulos integrais. 08A propõe vitória do jogador contra Renji, Byakuya/prisão em texto e nenhuma derrota/perda de poderes forçada por analogia a Ichigo; escolha de cobertura jogável, sem votar novamente o marco de prisão. 08B propõe missão de travessia/chegada ao Rukongai, treino pessoal de Ichigo no contexto e capacidades do jogador em seus sistemas próprios.

M10b/9.5 permite fonte contextual de campanha distinta dos operadores comuns, mas acesso/retorno, percurso/retomada e participação precisam de fichas. Não conceder Senkaimon, primeira visita universal ou autorização institucional por atravessar. Duas escolhas em M13/12.13/12.14; depois sequência Substituto e início Soul Society. Só campanha principal; M11/racial/lateral adiados; sem implementação/teste/publicação.


## 103. M13 — Renji aprovado; alternativa de treino explicada — 28/10/2026

“Concordo” à pergunta 1 aprova 08A, aplicado em 8.7/10.10/12.13: vitória sobre Renji e prisão de Rukia/Byakuya no desfecho escrito. À pergunta 2, usuário pediu explicação sobre prova breve na loja; não escolheu a inclusão do treino nem a opção sem ele.

CAMP-S02/19–20 reconferido: sinopse oficial explicita campo de treinamento subterrâneo da loja e treino de Ichigo com Urahara. Não confirma o objetivo proposto do mod. M13/10.12 detalha alternativa: acessar área, iniciar duelo de avaliação, acertar um golpe válido com recursos próprios, encerrar e disponibilizar expedição; perfil controlado, derrota/encerramento seguro e integração ainda por ficha M09/M12/M15. Não concede poderes, repete formação ou impõe prazo/poço de Hollowficação.

08B continua em discussão em 12.14, pergunta sobre incluir a prova ou seguir com plano/travessia. Inclusão/obrigatoriedade não aprovadas; não confundir pergunta com aceite. Depois consolidar a sequência e abrir Soul Society; só campanha principal, M11/racial/lateral adiados; sem implementação/teste/publicação.


## 104. M13 — Treino aprovado e perseguição de Ururu incluída — 28/10/2026

“Perfeito, podemos seguir assim” aceita 08B, prova explicada de acertar um golpe válido em Urahara; mesma resposta pede acrescentar perseguição com tempo e limite percentual de Vida (08C). Literal integral em M13/8.8. MAIN-SUB-PREP-01, em 10.12, organiza Ururu antes de Urahara e encaminha a expedição após aprovação, sem viagem automática ou habilidades gratuitas.

CAMP-S05 — [KLab, ficha oficial licenciada de Ururu](https://www.bleach-bravesouls.com/character/ururu.html), consultada em 28/10/2026: identificação e vínculo com a loja, texto disponível no índice/identificação na página. Não importar mecânicas de Brave Souls ou capacidades posteriores. CAMP-S02/18–20 reconferido confirma contexto/local/treino com Urahara; não detalha a perseguição lembrada pelo usuário. Não houve leitura integral do episódio/capítulo. Tempo/percentual são adaptação solicitada, não reprodução literal verificada do exercício original.

09A/B em M13/12.16/12.17 são propostas novas: 60 segundos e falha ao atingir 30% da Vida máxima inicial de referência ou menos em qualquer momento; guardar etapa de Ururu ao falhar no duelo seguinte. Nenhum valor/retomada aprovado ainda. Perfil controlado/encerramento, efeitos de formas/Vida, obstáculos/mobilidade, acesso e participação ainda exigem integração. Só campanha principal; M11/racial/fillers/laterais adiados; sem runtime/build/teste/publicação.


## 105. M13 — Parâmetros provisórios/retomada aceitos e começo da Soul Society — 28/10/2026

“Pode ser isso mesmo, depois caso não, ajusto” e “Mantemos concluida” aprovam 09A/B. Aplicado em M13/8.9/10.12/12.16/12.17: perseguição por 60 segundos nominais sem atingir 30% ou menos, valores provisórios; Ururu permanece concluída se o duelo seguinte falhar. Não reenviar as perguntas, declarar teste observado ou promover todos os detalhes técnicos a decisões.

CAMP-S06 — [Pierrot, retrospectiva de infiltração Soul Society, episódios 21–41](https://pierrot.jp/title/bleach/playback_02.html), consultada em 28/10/2026, confirma Rukongai, Jidanbō vencido, Gin fechando acesso e procura de Kūkaku por Yoruichi. Complemento oficial do anime; sem leitura de capítulo integral ou confirmação de repertórios. CAMP-S02/21–22 também reconferido para percurso/Kōtotsu/chegada e portão.

M13/10.13 contém MAIN-SS-01 como proposta: desafio do guardião, vitória do jogador, desfecho escrito com Gin e próximo caminho. 10A/B em 12.18/12.19 perguntam recorte breve da travessia com Kōtotsu no texto versus perseguição própria, e seleção Jidanbō jogável/Gin no desfecho. Ainda sem respostas/aprovação; não bloquear globalmente rotas legítimas, conceder skills ou declarar mapas/bosses prontos. Depois Kūkaku/Ganju. Só campanha principal, sem M11/racial/fillers/laterais nem implementação/teste/publicação.


## 106. M13 — Chegada/Jidanbō aprovados e preparo com Kūkaku proposto — 28/10/2026

“Perfeito, podemos adotar isso sim” aprova pacote 10A/B: travessia breve com Kōtotsu no contexto; vitória contra Jidanbō e Gin fechando acesso no desfecho escrito. Aplicado em M13/8.10/10.11/10.13/12.18/12.19. Não reenviar ou inferir bloqueio global de rotas, derrota forçada ou ficha de combate pronta.

CAMP-S02 — [TV Tokyo, episódios 23–25](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes02/index.html) e CAMP-S06 — [Pierrot, infiltração](https://pierrot.jp/title/bleach/playback_02.html), reconferidos em 28/10/2026, dão briga interrompida de Ganju/Ichigo, vínculo com Kūkaku, treino de controle da esfera, canhão e separação do elenco. Complemento oficial do anime, sem leitura integral de capítulo/episódio. Não inferir custos, duração, comandos ou separação obrigatória da party.

11A/B em 12.20/12.21 propõem Ganju no contexto e MAIN-SS-02 em 10.14: exercício de ajustar/manter marcador na faixa, depois lançamento explícito até o interior do Seireitei. Mecânica de interface é adaptação proposta; alternativa é lançamento direto sem exercício. Não concede skill/domínio, canhão/dispositivo como recompensa ou aliado de combate. Duas escolhas pendentes; depois Ikkaku. Somente campanha principal/documentação local; M11/racial/fillers/laterais adiados; sem runtime/build/teste/publicação.


## 107. M13 — Ganju incluído, Kūkaku aceito e direção de branch/main — 28/10/2026

“Prefiro incluir” aceita alternativa de Ganju jogável em 11A; “Podemois seguir assim” aprova exercício/lançamento em 11B. Aplicado em M13/8.11/10.14/10.15/12.20/12.21. Inclusão não resolve sozinha o desfecho da briga interrompida da obra: 12A propõe vitória completa do jogador, Ganju vivo no contexto.

CAMP-S07 — [TV Tokyo, episódios 26–38](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes03/index.html), consultado em 28/10/2026, especialmente 26–28, descreve confrontos paralelos Ikkaku/Ichigo e Yumichika/Ganju após o lançamento. Complemento oficial do anime, sem episódio/capítulo integral nem repertório completo. 12B propõe duas missões sequenciais do jogador Ikkaku/Yumichika, preservando a primeira se falhar na segunda. Ainda sem resposta, NPC aliado ou versão posterior de poder.

Usuário orientou atualizar branch antes e subir na main ao acabar este arco. Checkout confirmado na main, HEAD 26b73b3742681bfeee694c23a6e92007fd7f44ba; pedido assíncrono para esclarecer branch separada versus main local. Não declarar commit/push sem execução e verificar mudanças existentes antes de versionar. Campanha principal somente; M11/racial/fillers/laterais adiados, sem implementação/build/teste de jogo.

**Esclarecimento de versionamento recebido nesta rodada:** “Manter na main local e publicar somente ao concluir o arco”. Seguir na main local; não criar branch separada. Publicação só após terminar Soul Society. Nenhum commit/push realizado nesta rodada.


## 108. M13 — Vitória de Ganju e sequência da 11ª Divisão aceitas — 28/10/2026

“Concordo” e “seguimos assim” aprovam 12A/B: vitória completa do desafio de Ganju com sobrevivência narrativa; missões Ikkaku e depois Yumichika, primeira concluída preservada se a segunda falhar. Aplicado em M13/8.12/10.15/10.16/12.22/12.23. Não reenviar ou declarar versões/mapas/recompensas já especificados.

CAMP-S07 — [TV Tokyo, especialmente 28–31](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes03/index.html) e CAMP-S06 — [Pierrot, infiltração](https://pierrot.jp/title/bleach/playback_02.html), reconferidos em 28/10/2026: Jirōbō diante de Uryū/Orihime, cerco de soldados, Hanatarō/guias subterrâneas e Renji bloqueando aproximação da prisão. Complemento oficial do anime; sem capítulo/episódio integral ou repertório completo.

13A/B em 12.24/12.25 propõem MAIN-SS-04/05: vencer Jirōbō com cerco genérico no contexto; Hanatarō apresenta rota curta que jogador percorre sem escolta até saída próxima à prisão. Sem aliados de combate, defesa de Orihime/Hanatarō ou cura automática. Duas escolhas pendentes; depois ficha do segundo Renji. Main local, publicar só ao concluir Soul Society; sem branch/commit/push novos ou runtime/build/teste. M11/racial/fillers/laterais adiados.


## 109. M13 — Jirōbō/rota de Hanatarō aceitos e transição até Kenpachi — 28/10/2026

“Adotamos” e “Seguimos assim” aprovam 13A/B: Jirōbō jogável, cerco de soldados no contexto; Hanatarō indica galerias para travessia curta sem escolta, com chegada validada e Renji disponível em início próprio. Aplicado em M13/8.13/10.17/10.18/12.24/12.25; não reenviar ou declarar implementação.

CAMP-S07 — [TV Tokyo, episódios 30–38](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes03/index.html) e CAMP-S06 — [Pierrot, infiltração](https://pierrot.jp/title/bleach/playback_02.html), reconferidos em 28/10/2026, sustentam Renji/Zabimaru, vitória, reações/aparente morte de Aizen, ocorrência Chad/Shunsui e Kenpachi. CAMP-S08 — [TV Tokyo, episódios 39–51](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes04/index.html), também consultado para continuidade, sem aprovar suas quests/cenas automaticamente. Sinopses oficiais do anime, não conteúdo integral/repertório. Referência de força de Renji não aprova estatísticas cinco vezes maiores.

MAIN-SS-06/07 em 10.19/10.20 e escolhas 14A/B em 12.26/12.27 propõem Renji como confronto próprio, interlúdio de Aizen em texto sem investigação, e Shunsui adaptado como vitória do jogador antes de Kenpachi. Opções em aberto, sem transferir história de Chad/Ichigo, impor derrota, dano/quebra de arma ou conceder poder. Depois Kenpachi/prisão. Main local, publicação só ao concluir Soul Society; sem commit/push/branch nova ou runtime/build/testes. M11/racial/fillers/laterais adiados.


## 110. M13 — Interlúdio/Shunsui aceitos e próximo bloco da prisão — 28/10/2026

“Concordo” e “Adotamos” aprovam 14A/B: interlúdio escrito de Aizen após segundo Renji; confronto vencível de Shunsui antes de Kenpachi. Aplicado em M13/8.14/10.19/10.20/12.26/12.27. Não reenviar ou impor derrota/biografia/poder de Chad ao personagem.

CAMP-S06/07/08 reconferidos; CAMP-S09 — [Pierrot, resgate Soul Society, episódios 42–63](https://pierrot.jp/title/bleach/playback_03.html), consultado em 28/10/2026, descreve interrupção do primeiro Byakuya por Yoruichi, Bankai/recuo de Mayuri e confronto principal posterior de Byakuya. Sinopses oficiais complementares do anime, sem capítulo/episódio integral ou repertório completo; não comprova modo visual específico da fuga de Mayuri.

M13/10.21 prepara ficha de Kenpachi da continuidade escolhida, sob STR-03, sem fases novas aprovadas, morte/desmaio/quebra de arma forçados ou mundo interior de Ichigo imposto. 15A/B em 12.28/12.29 propõem contato com Rukia e Byakuya/Yoruichi em texto, principal Byakuya na execução, e Mayuri jogável com recuo após vitória completa. Não exige Letzt Stil/perda racial ou concede skill/antídoto automático. Duas escolhas pendentes; depois preparação final/execução. Main local, publicar só ao terminar Soul Society; sem branch/commit/push novos ou implementação/testes. M11/racial/fillers/laterais adiados.


## 111. M13 — Prisão/Mayuri aceitos e preparação final proposta — 28/10/2026

“Adotamos” e “Incluimos” aprovam 15A/B: contato jogável com Rukia, primeiro Byakuya/Yoruichi em texto e principal Byakuya reservado à execução; Mayuri vencido com retirada depois da vitória. Aplicado em M13/8.15/10.22/10.23/12.28/12.29. Não reenviar ou importar Letzt Stil/perda de poderes e biografia de Uryū.

CAMP-S09 reconferido; CAMP-S10 — [TV Tokyo, episódios 52–62](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes05/index.html), consultado em 28/10/2026, sustenta Renji/Byakuya antes da execução, frente de Kenpachi/Tōsen/Komamura e execução. Bankai de Tōsen referido no 53, sem conjunto completo de efeitos/medidas. Complemento oficial do anime, não episódio/capítulo integral; não aprova toda frente paralela nem números do mod.

16A/B em M13/12.30/12.31 propõem plano de Yoruichi e treino de Ichigo/duelo Renji em texto, sem nova prova racial, e duas missões Tōsen/Komamura com retenção da primeira, sem Kenpachi aliado. Alternativas de preparação própria e encontro simultâneo/contexto preservadas. Depois resgate/Byakuya, outras frentes/conspiração e encerramento, revisão antes de publicar. Main local, publicação só ao concluir Soul Society; sem branch/commit/push novos, runtime/build/testes. M11/racial/fillers/laterais adiados.


## 112. M13 — Preparação própria escolhida e dois capitães aceitos — 28/10/2026

“Prefiro uma preparação jogável própria” revisa 16A para incluir atividade do jogador; não aprova conteúdo ainda não apresentado. “Duas missões” aprova 16B, Tōsen depois Komamura, mantendo a primeira concluída se falhar depois. Aplicado em M13/8.16/10.24/10.25/12.30/12.31. Não reenviar inclusão ou divisão.

CAMP-S08/42/45 e CAMP-S09 reconferidos em 28/10/2026 para mobilidade/orientação de Yoruichi e treino pessoal de Ichigo. Sinopses oficiais do anime, não exercício literal de circuito nem leitura integral da obra. MAIN-SS-PREP-01 em 10.26 propõe ensaio de resgate com pontos/obstáculos/desníveis e interação final dentro de tempo próprio; viabilidade por corpo/repertório e números dependem de ficha. Não copiar 60 segundos/30% de Ururu ou desbloquear Shunpo/Bankai.

MAIN-SS-12 em 10.27 propõe alcance/libertação de Rukia por interação, Sōkyoku no contexto e Byakuya em luta própria seguinte. CAMP-S10/54 e CAMP-S09 sustentam arma/resgate, sem comprovar quebra universal por interação. 17A/B em 12.32/12.33 aguardam circuito versus duelo e Sōkyoku em texto versus desafio adicional. Depois Byakuya, cobertura restante/conspiração/encerramento. Main local, publicação só ao concluir Soul Society; sem branch/commit/push novos ou runtime/build/testes. M11/racial/fillers/laterais adiados.


## 113. M13 — Circuito e resgate aceitos; Byakuya e frentes em discussão — 28/10/2026

“Adotamos” e “Seguimos asism” aprovam 17A/B: circuito de aproximação/interação final com Yoruichi e resgate por alcançar/libertar Rukia; Sōkyoku em texto, Byakuya seguinte em início próprio. Aplicado em M13/8.17/10.26/10.27/12.32/12.33. Não reenviar, importar números de Ururu ou promover mapa/parâmetros a especificação pronta.

CAMP-S09 — [Pierrot, resgate](https://pierrot.jp/title/bleach/playback_03.html) e CAMP-S10 — [TV Tokyo, 55–59](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes05/index.html), reconferidos em 28/10/2026, sustentam Byakuya Shikai/Bankai/Senkei e vitória reconhecida, frentes Soi Fon/Yoruichi e Yamamoto/Shunsui/Ukitake. Só sinopses oficiais do anime, não conteúdo integral/repertório/parametrização. Senkei não é terceira transformação racial.

18A/B em M13/12.34/12.35 propõem uma luta contínua de Byakuya e Soi Fon jogável antes do resgate, Yamamoto em texto. Autoria/ordem de Soi Fon são adaptações; não interromper libertação → Byakuya já aprovados. Alternativas de tentativas separadas e Yamamoto jogável preservadas, ainda sem resposta. Depois demais frentes/oficiais, conspiração/Aizen, encerramento/retorno e revisão antes de publicar. Main local; só publicar ao concluir Soul Society; sem branch/commit/push novos, runtime/build/testes. M11/racial/fillers/laterais adiados.


## 114. M13 — Byakuya/Soi Fon aceitos e confronto de Aizen proposto — 28/10/2026

“Concordo” e “Adotamos” aprovam 18A/B: Byakuya em luta contínua com Shikai/Bankai/Senkei; Soi Fon antes do resgate, Yamamoto/Shunsui/Ukitake em contexto. Aplicado em M13/8.18/10.28/10.29/12.34/12.35. Não reenviar ou deslocar novas missões para entre libertação e Byakuya.

CAMP-S09 — [Pierrot, resgate 42–63](https://pierrot.jp/title/bleach/playback_03.html) e CAMP-S10 — [TV Tokyo, especialmente 60–62](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes05/index.html), reconferidos em 28/10/2026, sustentam ordens falsas/Aizen/Hōgyoku/fuga e ocorrências internas. Página seguinte TV Tokyo foi aberta para localizar encerramento, sem importar Bount/fillers. Só sinopses oficiais, sem conteúdo integral ou repertório.

19A em M13/10.30/12.36 propõe cobertura restante em contexto; 19B em 10.31/12.37 propõe revelação/obtenção do Hōgyoku antes da luta, vitória do jogador contra Aizen dessa era e fuga com Gin/Tōsen no desfecho. É adaptação de autoria/ordem STR-03, não vitória literal de Ichigo na obra, selamento ou recuperação do artefato. Duas escolhas pendentes; depois cancelamento da execução, destino de Rukia, retorno/continuidade e revisão antes de publicar. Main local, publicação só ao concluir Soul Society; sem branch/commit/push novos, runtime/build/testes. Pendências de realização e M11/racial/fillers/laterais preservadas.


## 115. M13 — Cobertura restante aceita e justificativa de Aizen em discussão — 28/10/2026

“Concordo” aprova 19A: demais disputas/oficiais mencionados em contexto; aplicado em M13/8.19/10.30/12.36. “porque adaptar para uma vitória?” questiona 19B e pede justificativa, sem aprovar boss/vitória nem escolher alternativa. Não inventar aceite, rejeição global ou revisão do contrato.

M03/STR-03 reconferido: autoria/vitória nos bosses selecionados; derrota/selamento de Aizen no exemplo do momento de Karakura. Não obriga converter toda aparição em boss. CAMP-S09 — [Pierrot, resgate](https://pierrot.jp/title/bleach/playback_03.html), reconferido em 28/10/2026, descreve superioridade de Aizen/obtenção do Hōgyoku/saída; só sinopse oficial, sem conteúdo integral. Assistente reconsiderou recomendação: preservar essa aparição no contexto, Byakuya último boss deste arco e Aizen vencível nas etapas apropriadas posteriores. Avaliação de design ainda não aprovada; M03 não foi alterado.

Proposta anterior preservada como histórico não aprovado em M13/10.31; explicação e nova recomendação em 10.31.1/12.37. Aguardar escolha antes de concluir encerramento/retorno/revisão e publicar. Main local, publicação só ao concluir Soul Society; sem branch/commit/push novos ou runtime/build/testes. Outras pendências e adiamentos preservados.


## 116. M13 — Revisão de Aizen aprovada e roteiro dos dois primeiros arcos consolidado — 28/10/2026

“Perfeito, aprovo” aceita o recorte reformulado de 19B: Aizen em contexto, sem vitória contra ele neste arco; Byakuya último boss. Aplicado em M13/8.20/10.31/12.37 e retorno explicativo em M03/STR-03. Proposta de boss MAIN-SS-14 retirada do catálogo e preservada em histórico não adotado; não confundir com as 28 missões atuais nem revogar Aizen jogável nos momentos posteriores adequados.

M13/10.32 consolida 10 missões Substituto e 18 Soul Society; 10.33 aplica contratos já aprovados de conclusão/continuidade. CAMP-S09, [retrospectiva oficial do resgate](https://pierrot.jp/title/bleach/playback_03.html), reconferido em 28/10/2026, sustenta cancelamento da execução/Rukia na Soul Society e despedida; não é capítulo integral. Não impor teleporte/raça/afiliação ao personagem original ou criar quest apenas administrativa. Retorno real e todas as integrações precisam ser demonstrados antes de implementar.

Fechamento da seleção/estrutura, não de gameplay: M13 permanece em especificação; perfis/mapas, acesso por caminho, custos, recompensas/replay, UI/participação/era/persistência pendentes. Publicação documental autorizada na main ao concluir o arco, com comprovação Git; próximo Arrancar em rodada posterior. M11/racial/fillers/laterais adiados, sem código/build/testes de jogo. [Relatório desta revisão](00-fechamento-roteiro-substituto-soul-society-2026-10-28.md).
