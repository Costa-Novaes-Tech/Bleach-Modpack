# Ciclo 72 — revisão geral do projeto, game design e roadmap

> **Atualização93:** [ensaio real Blockbench/MCP e correção do rugido](93-blockbench-prova-controlada-rugido-hidras-2026-10-22.md). Ferramenta validada em cópia, sem substituir o modelo. Próxima prova de autoria: formato/export GeckoLib e integração do controlador. Mantida a ordem obrigatória e as pendências de aprovação artística/auditiva.

> **Atualização92 — 22/10/2026:** [92: áudio Hyōrinmaru, transformação20s e gelo temporário](92-audio-hyorinmaru-transformacao-gelo-temporario-2026-10-22.md). Nova mixagem com fala separada, transformação20s, baforada36blocos e piso de gelo visual temporário; clima global independente. Rede2.35. Não muda a ordem obrigatória abaixo; aceites artísticos/auditivos e multiplayer continuam pendentes. Resultados no relatório92.

**Implementação22/10 — ciclo91:** [hidra mais robusta, N com explosões sem retorno e weather desacoplado](91-hidra-robusta-explosoes-clima-local-2026-10-22.md). Câmera aproxima a revelação; modelo/geometria e resíduos locais atualizados; custo/recarga/passivas preservados. Base+Photon140 regressões/176 GameTests. A implementação do90 não encerra a homologação artística nem as pendências de mãos/combate, outras técnicas e multiplayer do89. Demais frentes desta lista continuam obrigatórias.

**Planejamento21/10 — ciclo90:** [refino da hidra e remoção do vínculo climático](90-planejamento-refino-hidra-impactos-sem-weather-2026-10-21.md). Acrescentar pescoços mais robustos/proporcionais; Bankai sem acionar weather global; ataqueN com três saídas por trás e impactos explosivos que espalham gelo/dano em área, sem retorno. Substitui a coreografia de recolhimento88–89. Apenas documentos; não implementado. Mantidas as demais pendências do89 e a lista obrigatória deste roadmap.

**Atualização21/10 — ciclo89:** [primeira implementação do novo kit/hidra Hyōrinmaru](89-implementacao-hyorinmaru-hidra-kit-2026-10-21.md). Novos slots88, transformação10s, baforada, asas/cauda, Sennen Shikai e Revestimento selado já implementados;139 regressões/173 GameTests nos perfis base/Photon. Ainda requer homologação visual e testes multiplayer em dois clientes. Não encerrar a fila obrigatória por esta entrega; clima/C, refinamento artístico, Benihime/Ryūjin e demais itens preservam suas pendências.

Revisão de 20/10/2026. Pedido do usuário: revisar a documentação e explicar o estágio do mod e o que falta, sem alterar agora os sons. Git inicial limpo em `Feature-Poderes-bankais`, HEAD `7621758` (`Feature: Adição de audio de Benihime`). Esta rodada altera somente documentação em `Docs/poderes`; não executa jogo, build, testes, instalação, commit, push ou merge.

## 1. Diagnóstico atual

O projeto está em **protótipo jogável avançado da frente Shinigami/combate**, com vários kits, ferramentas de autoria e QA, aparência e um protótipo de mundo História. Ainda não representa o RPG completo descrito no game design: campanhas, progressão racial completa, mentores, disciplinas, outras raças jogáveis e experiência multiplayer final continuam em estágios diferentes.

Não há base para dar uma porcentagem global de conclusão: o escopo completo ainda contém módulos sem especificação. Separar quatro estados: decisão conceitual, implementação, verificação técnica e homologação do usuário. Um documento aprovado, um NPC registrado ou um GameTest passando não comprovam os outros estados.

**Feedback de áudio vigente:** o usuário entende bem “Bankai”, mas a música ainda sobrepõe o nome completo “Kannonbiraki Benihime Aratame”, mesmo após o reforço do ciclo 71. A mixagem não está homologada. Correção adiada por pedido explícito; nenhum recurso sonoro foi modificado nesta revisão. Na retomada, avaliar primeiro a relação música/voz no trecho do nome, incluindo redução localizada da música, em vez de presumir que elevar novamente a fala resolverá sozinho.

## 2. Alcance e leitura das fontes

Inventário dos **124 arquivos Markdown em Docs**: 87 na frente poderes (incluindo evidências de QA), 13 em arquitetura, oito em game-design, oito em planejamento, cinco em arte, um em desenvolvimento, um em jogador e o README geral. O [inventário documental](revisoes/72-inventario-documental-2026-10-20.json) registra caminhos, títulos, seções, marcadores textuais de estado e hashes anteriores às alterações desta rodada. Esses marcadores são índices de leitura, não classificações automáticas de conclusão. O próprio relatório 72 e o inventário não pertencem à fotografia inicial.

A revisão cruza os documentos de design e suas revisões internas, planos técnicos, decisões de interface, relatórios recentes de entrega e aceites desta conversa. Verificações pontuais de código confirmam protocolo `2.28`, schema `9`, sete atributos atuais e as três telas que herdam de `LayoutScreen`. Não é uma auditoria integral de cada classe, uma revisão jurídica dos recursos ou uma nova sessão de testes.

Consultar também o [README dos mapas](../../history-maps/README.md). Os estudos de engenharia reversa de Dragon Mine Z em arquitetura documentam referências e sistemas de origem; não devem ser tratados integralmente como funcionalidades Bleach já implementadas.

## 3. O que já está entregue

### Combate e poderes

- **Homologados pelo usuário:** stamina, progressão de Resistência entregue, bainhas, esquiva Alt + direção, defesa X/Postura/Guard Break e kit Byakuya/Senbonzakura. Esquiva e guarda também funcionam no criativo nas condições previstas; esquiva nominal de 3,5 blocos.
- **Benihime — mecânicas homologadas:** kit, cura de aliados/regeneração da Bankai, Shibari de 20 blocos de diâmetro, captura/contração e ataque complementar sobre os inimigos da rede. O aceite de gameplay não fecha a revisão artística posterior da transformação e das técnicas.
- **Zaraki/Nozarashi/Berserker:** integrado e aprovado; preservar kit, modelos e animações. A etapa antiga “aguardar commit do Zaraki” já foi vencida.
- **Ryūjin e Hyōrinmaru:** repertórios refeitos e implementados; não estão aguardando uma primeira implementação. Hyōrinmaru foi considerada boa no retorno atual. Ryūjin ainda tem trabalho de apresentação e animações, sobretudo Higashi, Investida e Rajada.
- **Asauchi e combate básico:** animação aprovada, modelo 3D e poses de bloqueio entregues; a integração Better Combat física de Asauchi/Ryūjin/Hyōrinmaru existe. Senbon/Benihime não possuem automaticamente a mesma autoridade de hitboxes por terem gestos cosméticos.
- **Progressão atual:** vínculo de uma Zanpakutō, progresso separado por identidade, formas/técnicas, maestria por prática e migrações de dados. Isso ainda não implementa todo o modelo racial/disciplinar dos módulos de design.

### Transformação Benihime e execução Higashi

O [ciclo 71](71-benihime-apoios-audio-higashi-execucao-2026-10-20.md) já entregou braços menores, apoios adaptados ao terreno, postura do jogador em pé com duas mãos na espada e áudio fornecido pelo usuário. Cortina/revelação, avanço, elevação, pose dos fios e acompanhamento já existem no cliente. A manifestação ainda usa animação **procedural**; fonte editável Blockbench não significa migração integral para runtime GeckoLib concluída.

Higashi já contempla execução por limiar de 7% e incineração quando o dano normal confirma morte acima desse limiar, respeitando as regras próprias de cada gatilho. Preparação junto ao quadril, corte diagonal ascendente, pausa de dois segundos após a passagem e câmera lateral até as cinzas foram implementados e observados no laboratório. Falta aceite do usuário da apresentação final; não tratar essa cena como trabalho ainda não iniciado.

### Fundação, interface, NPCs e mundo

- Persistência, sincronização, missões JSON, objetivos/recompensas, diário, formas, HUD e comandos de desenvolvimento já existem. Versões atuais verificadas: rede **2.28**, schema persistente **9**. Os sete atributos de runtime continuam Zanjutsu, Hakuda, Vitalidade, Resistência, Kidō, Reserva e Controle.
- **Interface:** Diário, Personagem e seletor de formas já usam layouts JSON e artes tratadas. Há pipeline de conversão, fontes e prévias; não são mais apenas mockups. Persistem limites de conteúdo no diário (quatro linhas de descrição, três objetivos e duas recompensas), rolagem não arrastável e necessidade de QA em resoluções/escalas. Ver [decisões de telas](../arte/telas-decisoes.md) e [pipeline GUI](../arte/pipeline-gui.md).
- **Aparência:** editor, validação no servidor e composição de camadas integrados; plano registra 149 peças derivadas de 41 skins. Categorias atuais: base, traje, cabelo, olhos e boca. Cabelo 3D, acessórios/marcas e fases posteriores do catálogo continuam futuros. Preservar compatibilidade do modelo vanilla do jogador com animações, bainhas e armaduras especiais.
- **NPCs:** oito personagens originais com integração de quests e 33 adicionais sem missões atribuídas no plano atual. Existência de Uryū, Aizen ou Gin como NPC/skin não significa kit de poderes implementado nem altera a fila de poderes desta frente. NPC de quest também não equivale ao futuro sistema de Mentor.
- **Modo História:** instalação de mapas de mundo humano, Soul Society e Hueco Mundo/Las Noches para mundos novos no preset próprio; viagem provisória por três itens de TP sem custo. Não equivale a campanha pronta. Senkaimon/Garganta, custo, retorno e pouso seguro continuam pendentes; Wahrwelt não integra esse conjunto. Não presumir instalação retroativa em mundos normais.

## 4. Estado do game design — módulos 01 a 16

Os estados abaixo descrevem **design**, não percentual de código entregue.

1. **M01 — Visão e pilares: DECIDIDO.** Jogador original no universo Bleach, poderes canônicos e progressão contextual. Falta concretizar isso no conjunto final do jogo.
2. **M02 — Trilhas de progressão: DECIDIDO, com revisões posteriores.** Jornada, capacidades, técnicas, vínculo, Mastery, relações e exploração. As revisões de M05/M06 sucedem trechos antigos sobre ranks comprados e moeda de técnicas.
3. **M03 — Storyline macro: DECIDIDO.** Campanhas, marcos e separação entre estado pessoal, grupo e mundo. Falta detalhar e implementar arcos, quests, encontros e sincronização narrativa; mapas presentes não encerram essa tarefa.
4. **M04 — Raças e origens: ESPECIFICADO.** Shinigami, Hollow/Arrancar, Quincy e Fullbringer têm estruturas conceituais. O cabeçalho antigo “idealizado” foi superado pela conclusão do próprio arquivo. Custos, técnicas e condições exatas foram delegados a módulos posteriores. Outras raças jogáveis completas não estão entregues por esse documento.
5. **M05 — Atributos e recursos: ESPECIFICADO CONCEITUALMENTE.** Cinco atributos futuros: Potência, Vitalidade, Resistência, Reserva e Controle. Sem nível global. Prática e Spiritual Points alimentam um progresso de atributo; técnicas seguem acesso/aprendizado/Mastery, sem compra de Mastery. Calibração numérica e migração do modelo atual ainda faltam.
6. **M06 — Habilidades e combate: EM ESPECIFICAÇÃO.** Contratos gerais e **M06-07 Shinigami aprovados estruturalmente** no corpo mais recente. Próximo bloco de design: **M06-08 Hollow/Arrancar**, seguido de Quincy, Fullbringer e auditoria. Isso é diferente de M08, o módulo futuro de NPCs. O cabeçalho que ainda deixa M06-07 pendente está desatualizado.
7. **M07 — Transformações e domínio:** módulo dedicado ainda não escrito no conjunto atual. Precisará fechar vínculo, provas, requisitos raciais, domínio e relação dos grandes poderes com a progressão. Contratos pontuais das Bankais existentes já existem em poderes.
8. **M08 — NPCs, mentores e facções:** sem especificação modular dedicada; há protótipos técnicos e planos de NPCs/quests. Falta fechar ensino, relações, acesso e integração racial.
9. **M09 — Mobs, bosses e encontros:** sem especificação modular dedicada; mobs de teste não encerram repertório, IA, encontros e balanceamento.
10. **M10 — Dimensões e estruturas:** sem especificação modular dedicada; mapas/preset/TP atuais são fundação técnica, com viagens finais ainda abertas.
11. **M11 — Itens e economia:** sem especificação modular dedicada; faltam regras completas de obtenção, equipamentos, circulação e economia.
12. **M12 — Sistema de quests:** sem especificação modular dedicada; motor JSON já presente, integração ao design final e fluxo completo de campanhas ainda necessários.
13. **M13 — Campanhas e sidequests:** sem especificação modular dedicada; a macro de M03 ainda precisa virar conteúdo jogável.
14. **M14 — Interface e feedback:** sem especificação modular dedicada; implementação e documentos de arte já existem, mas falta consolidar a experiência dos sistemas futuros e seus critérios de aceite.
15. **M15 — Multiplayer e balanceamento:** sem especificação modular dedicada; rede existente e testes de servidor não substituem validação com jogadores, PvP e progressão conjunta.
16. **M16 — Integração, testes e roadmap:** sem especificação modular dedicada; há laboratório, regressões e relatórios operacionais, mas falta consolidar os critérios globais do produto.

### Diferenças importantes entre design e jogo

- **Sete atributos atuais → cinco futuros:** exige migração de saves, equivalência dos investimentos, fórmulas, interface, rede e balanceamento. Não remover Zanjutsu/Hakuda/Kidō dos dados sem esse contrato.
- **Proficiência disciplinar ≠ atributo ≠ Mastery individual:** M06-07 prevê desenvolvimento orgânico, sem outro rank individual comprado. Zanjutsu, Hakuda, Hohō e Kidō são disciplinas; Zanpakutō não é sinônimo de Zanjutsu. Esquiva não é Shunpo.
- **Repertório/lista substitui árvore obrigatória:** técnicas podem vir de fontes diversas, mestres e aprendizado autônomo; não presumir NPC específico para cada técnica básica. Kidō não exige universalmente Tap/Hold para toda magia.
- **Combat State atual ≠ Combat Mode/hotbar de combate completa:** os quatro slots de técnicas existentes não encerram o sistema proposto no design.
- **Vínculo atual com troca de identidade ≠ contrato racial final completo:** conciliar o comportamento de teste/progressão já entregue com exclusividade, descoberta e provas de M07 antes de mudar regras.
- **Mentor/Proficiências continuam adiados para confirmação da equipe**, conforme pedido do usuário. A aprovação estrutural mais recente de Shinigami reduz dúvidas de conceito, mas não revoga esse adiamento de implementação.

## 5. Roadmap vigente desta frente

A sequência abaixo foi reafirmada pelo usuário como **lista obrigatória de trabalhos**, sem deixar itens para trás. Preservar a ordem e acompanhar cada item até sua validação e aceite; implementação existente não encerra automaticamente a etapa. Qualquer reordenação depende de nova orientação do usuário. Mentor/Proficiências mantém a dependência de confirmação da equipe: uma espera não elimina o item do roadmap. As frentes globais da seção seguinte continuam como coordenação, não como autorização automática para implementá-las agora.

1. **Concluir apresentação da entrada Benihime.** Revisar pega central, ambos os braços, apoio/transferência de peso, saída, levantamento, rosto/cabelo/roupa e acompanhamento. Ajustes de proporção/terreno do ciclo 71 já estão entregues. Áudio do nome segue pendente e adiado nesta rodada. Fechar com escuta e aprovação visual do usuário.
2. **Fechar revisão da Higashi.** Avaliar a nova cena entregue, contato da lâmina, pausa, incineração, enquadramento lateral e retorno ao controle. Corrigir a partir de feedback concreto; preservar os dois gatilhos e as condições de morte confirmada.
3. **Ryūjin — Investida e Rajada.** Refinar deslocamento, gestos e identidade dos efeitos com partículas de chamas, conforme estudos aprovados. Não reiniciar o kit inteiro nem adicionar proteção Nishi rejeitada.
4. **Benihime — combate da Zanpakutō e demais técnicas.** Correção explicitamente solicitada: a animação de combate da espada está ruim. Revisar ataques básicos/combos, empunhadura, orientação da lâmina, transições e recuperação, em primeira e terceira pessoa, com contato visual coerente com o acerto e sem disputa entre controladores de animação. Não limitar esta etapa aos efeitos das habilidades. Melhorar também gestos e efeitos das técnicas preservando as mecânicas homologadas, inclusive alcance/captura da rede, detonação coletiva, cura e cargas de abertura de caminho. Mudanças na autoridade de hitboxes Better Combat exigem contrato próprio, não são consequência automática da correção visual.
5. **Uryū Ishida.** Próximo personagem novo da fila; fechar o contrato do kit/equipamento Quincy com a equipe antes de implementar. O bloco Quincy do design ainda não está fechado; a existência do NPC não entrega o kit.
6. **Mentor e Proficiências**, após confirmação da equipe; harmonizar com M05/M06/M07, sem duplicar níveis ou converter o NPC de quests em loja de habilidades por conveniência.
7. **Hyōrinmaru — revisão com Blockbench.** Incluir autoria de modelos, rig e animações dos dragões de gelo, além da transformação e animações do personagem. Planejar mandíbula/cabeça, corpo e movimento dos dragões com fontes editáveis; preservar os três dragões da Bankai e os alcances/regras aprovados salvo novo pedido. Blockbench serve à autoria; avaliar GeckoLib para animar os dragões no jogo e manter as animações do jogador na integração compatível com PlayerAnimator, sem substituir globalmente seu renderizador. Validar modelo, percurso, legibilidade, contato e desempenho no Minecraft real. O estado atual foi considerado bom: esta melhoria planejada permanece na etapa posterior, sem reabrir o kit imediatamente.

### Etapa 8 adicionada — eficiência e limpeza do projeto (25/10/2026)

Solicitação explícita do usuário após a integração das três branches: acrescentar **eficiência, velocidade de desenvolvimento e controle de espaço em disco** ao roadmap. Os sete itens anteriores permanecem; esta é uma frente técnica adicional, antes da expansão posterior para NPCs de combate e quests. Não autoriza apagar arquivos nesta rodada de integração.

Manter o trabalho paralelo das IAs em worktrees separados, conforme decisão posterior do usuário. Branches em um único checkout não isolam edições simultâneas. Não remover `Bleach-Modpack-design` nem migrar conversas por inferência.

- Inventariar tamanho lógico e, quando possível, espaço alocado de cada checkout, Git compartilhado, caches, mapas, saves QA, vídeos, screenshots, builds e fontes de autoria. Identificar duplicatas por hash e procedência; duplicação não prova inutilidade.
- Separar fontes/ativos necessários, evidências de homologação, arquivos pessoais, dependências reconstruíveis e resíduos realmente dispensáveis. Preservar fontes Blockbench, áudios originais, mapas autorizados e saves pessoais; não retirar documentação vigente para reduzir volume.
- Propor retenção/arquivamento dos testes e capturas antigos com índices, hashes e caminhos recuperáveis. Conferir se assets de produção ou documentação dependem deles antes de qualquer remoção.
- Medir tempo de build incremental, preparação de cliente, testes e autoria. Priorizar gargalos medidos, evitando `clean` indiscriminado e recompilações simultâneas competindo pelo mesmo cache/hardware. Manter coordenação de Minecraft/Blockbench entre agentes.
- Revisar arquivos rastreados/ignorados, logs e scripts com caminhos rígidos, sem alterar bibliotecas/modelos por conveniência ou quebrar reprodução em clone limpo.
- Entregar proposta concreta de limpeza, estimativa de economia e procedimento de recuperação; executar apenas o escopo aprovado e conferir build/QA pertinentes depois. Não usar remoção forçada de worktree para contornar arquivos não salvos.

Medição preliminar de25/10, antes da integração: checkout design com aproximadamente6,32GiB de tamanho lógico; run-benihime-qa3,72GiB, Docs1,84GiB. Não é medição de espaço físico recuperável nem autorização para apagar esses diretórios.

### Controle obrigatório de fechamento

**Revisão de21/10 — [ciclo88](88-revisao-kit-hyorinmaru-hidra-erupcao-storyboards-2026-10-21.md):** usuário reorganiza o repertório do item7: Ryūsenka em Bankai H; nova hidra ofensiva em N; Erupção Glacial em B; Sennen ampliada transferida para Shikai B. C Bankai permanece Hyōten. Dois vídeos analisados e três storyboards criados, sem implementar mudanças. Atualizar migração/maestrias junto dos slots quando houver código; os demais itens obrigatórios permanecem.

**Plano de21/10 — [ciclo87](87-plano-implementacao-hyorinmaru-transformacao-kit-2026-10-21.md):** detalhada a implementação futura do item7 e do repertório de quatro ações por forma, a partir da direção visual86 aceita. Ordem: contratos → pescoço/rig → transformação/aparência → baforada/estados → Bankai H/N/B/C → Shikai/selada → acabamento, perfis e observador. Documento distingue código atual e mudanças propostas; nada implementado nesta rodada e nenhum outro item eliminado.

**Discussão de21/10 — [ciclo86](86-prototipo-hyorinmaru-hidra-baforada-transformacao-2026-10-21.md):** item7 recebe nova proposta visual: três pescoços orgânicos se formam, baforada cobre o jogador e atinge inimigos à frente, neve revela asas/cauda novas. Apenas storyboard e estudo de contrato; duração/dano/fragilidade não implementados nem homologados. Usuário testa84; demais itens da lista obrigatória continuam preservados.

**Retorno de21/10 — [ciclo84](84-revisao-combate-benihime-hyorinmaru-2026-10-21.md):** usuário reabriu combate Benihime e pediu auditoria das tentativas Hyōrinmaru integradas na branch. Itens4 e7 em refinamento e validação; demais pendências e fila preservadas. Transformação140ticks, hidra cenográfica separada dos três projéteis e autoria Blockbench corrigidas; esgrima Benihime revisada no Better Combat. Não constitui aceite artístico automático.

**Retorno de21/10 — [ciclo81](81-benihime-selada-bettercombat-entrada-dash-ryujin-2026-10-21.md):** vídeo reabriu item1 por cancelamento ao andar e item3 pela corrida no dash. Revelação agora continua ao sair da câmera; dash com giro/horizontalidade retomados e subida verificada. Item4 inclui integração Better Combat com três animações próprias, quatro técnicas seladas, Hiasobi ampliada e suprema raio12/base40 com destruição limitada/protegida de terreno. Implementado para testes; não marcar homologado pelo agente. Nake/escudo com carga/parry continuam propostas separadas, sem aplicação automática.

**Retorno de21/10 — [ciclo80](80-benihime-fios-costura-final-reformulacao-2026-10-21.md):** item4 agora inclui reformulação autorizada: Hiasobi independente de fios sem armadilha + combo de rede; Dissecação em dois contatos; Caminho emB com três cargas; supremaC Costura Final e gestos próprios da manifestação. Base e Photon135 regressões/158 GameTests por perfil; cliente base13cenários/1144quadros. Homologação visual do usuário pendente. A proposta selada79 não foi aplicada automaticamente; demais itens permanecem obrigatórios.


**Retorno de21/10 — ciclos [78](78-higashi-preparacao-quadril-corte-ascendente-2026-10-21.md)/[79](79-proposta-reformulacao-benihime-selada-shikai-bankai-2026-10-21.md):** manejo Higashi reaberto por posicionamento/corte ascendente; correção de quadril/curva/rastro entregue para revalidação, sem voltar à prévia indiscriminada. Item4 ampliado a pedido do usuário: inventariar/reformular o kit Benihime e propor quatro ações para a forma selada, além de Shikai/Bankai. Proposta ainda sem implementação de novos poderes; entrada/manifestação mantidas. Demais itens da lista continuam obrigatórios.

**Retorno posterior — [ciclo 77](77-auditoria-video-higashi-combate-benihime-2026-10-20.md):** usuário considera Benihime boa; parecer visual favorável à entrada nas duas invocações noturnas. Preservar a cena/modelo/áudio; limites de escuta/multiplayer continuam registrados. Higashi reaberta por gatilho de prévia indiscriminada: correção exige confirmação de morte, sem cena longa no sobrevivente/sem alvo. Item 4 entregue para testes: três cortes básicos próprios Benihime e oito gestos sem alterar regras de dano/BC; doze cenários gráficos e suítes base/Photon aprovados tecnicamente. Aceite dos novos ataques ainda pendente.

**Atualização de execução — [ciclo 76](76-ryujin-investida-rajada-refinamento-2026-10-20.md):** o usuário declarou “a higashi está bom até o momento” e autorizou avançar antes da nova bateria. Higashi preservada; item 3 (Investida/Rajada) implementado e conferido no cliente base; 129 regressões/155 GameTests em base e Photon. Ainda sem aceite artístico do usuário. Item 1 continua com revisão final de áudio/visual pendente. A próxima etapa após o item 3 permanece combate da espada e técnicas Benihime.

**Histórico de testes — [ciclo 75](75-testes-benihime-higashi-proxima-etapa-2026-10-20.md):** laboratório aberto para revisão manual dos itens 1/2; Higashi testada com duas mortes confirmadas e um sobrevivente. Isso não dá baixa no aceite artístico/escuta. Próxima implementação continua Investida/Rajada, sem omitir as etapas seguintes.

**Entrega posterior do item 1 — [ciclo 74: palco, penumbra, câmera e áudio](74-benihime-palco-penumbra-camera-audio-2026-10-20.md):** usuário retomou e autorizou a correção de mixagem antes adiada. Piso visual temporário, área de luz preservada no jogador, orientação de palco menos obstruída e retorno orbital implementados; mix com trilha reduzida antes do nome e fala reforçada. Resultados/limites no relatório 74. Esta entrega não dá baixa automática no aceite artístico do item.

**Evidência posterior do item 1 — [vídeo do usuário, ciclo 73](73-analise-video-usuario-entrada-benihime-2026-10-20.md):** quatro invocações analisadas por quadros. Revelação/elevação legíveis no espaço aberto; close excessivo da cabeça no retorno da câmera e oclusão por paredão/árvore observados. Acompanhamento considerado bom pelo usuário em teste fora da gravação. Interrupções não testadas; áudio sem nova avaliação auditiva. Item permanece aberto, sem código nesta análise.

- [ ] 1. Entrada Benihime: caminhada reaberta/corrigida no ciclo81 e conferida no cliente; revalidação do usuário, escuta e multiplayer pendentes.
- [ ] 2. Higashi: reaberta no ciclo 77 por cena indevida em alvo sobrevivente; correção entregue e verificada (quatro cenários, 132 regressões/155 GameTests base e Photon); revalidação do usuário pendente.
- [ ] 3. Investida e Rajada Ryūjin: melhorias76 e giro do dash81 entregues; conferir aceite do usuário.
- [ ] 4. Benihime81: esgrima Better Combat, kit selado, Hiasobi e suprema destrutiva implementados para teste; validação artística e balanceamento pendentes.
- [ ] 5. Contrato e implementação do kit Uryū Ishida.
- [ ] 6. Mentor e Proficiências, após confirmação da equipe.
- [ ] 7. Hyōrinmaru: dragões, animações e transformação com apoio do Blockbench.
- [ ] 8. Eficiência e limpeza: inventário, retenção de evidências, gargalos de build/QA e limpeza revisada; preservar worktrees para colaboração paralela.

**Aceites posteriores de25/10:** entrada Benihime validada pelo usuário, incluindo o refinamento102; Investida/Rajada aprovadas. Higashi será validada depois. A substituição da boneca do kit selado Benihime foi solicitada, mas a alternativa ainda não foi fechada. Esses aceites atualizam os registros históricos acima; não significam homologação de todas as técnicas, áudio em observadores ou multiplayer. Integração e validações combinadas serão registradas no105.

Itens abertos representam o trabalho restante desta rodada de melhorias, não ausência de toda a implementação anterior. Dar baixa apenas com evidência da entrega pertinente e aceite correspondente, registrando dependências ainda abertas.

### Frente proposta após concluir a lista

O usuário informou que **a equipe já está desenvolvendo mais os Hollows**. Registrar como trabalho em andamento informado, sem presumir integração, estado da branch ou homologação: conferir commits, documentação e contratos quando chegar a hora, evitando duplicação de IA, entidades e conteúdo.

Após fechar os sete itens, a próxima direção proposta é **NPCs de combate e quests da lore**. Primeiro alinhar os módulos de NPCs/encontros/quests/campanhas e o conteúdo Hollow disponível; depois definir uma primeira sequência jogável com NPC combatente, comportamento/ataques e missão contextual com objetivo, recompensa e persistência. Preservar a decisão de M03 sobre ausência de ajudantes canônicos nos combates de bosses do jogador; NPC de combate não implica automaticamente companheiro de campanha. Planejamento posterior ainda deverá fechar quem luta, em quais condições e como as missões usam esses encontros. Esta direção não antecipa implementação agora nem substitui a lista obrigatória acima.

**Preservados:** Zaraki, Senbon/Byakuya e fundamentos homologados. **Aizen:** poderes adiados, não excluído do universo/campanha/NPCs. **Gin:** retirado da fila de poderes desta frente; isso não ordena remover skins ou referências narrativas existentes.

## 6. Trabalho global ainda necessário — coordenar com a equipe

- **Design:** continuar M06-08 e os blocos raciais seguintes; depois M07. Antes de migrar atributos/ensino, transformar as decisões em contratos de dados, custos, compatibilidade e uma primeira fatia jogável.
- **Progressão e campanha:** ligar criação de personagem, descoberta do poder, treinamento, provas, quests, mentores, recompensas e formas numa jornada coerente. Rever compra de forma atual frente ao modelo narrativo final; não trocar por uma regra ainda não fechada.
- **Conteúdo do mundo:** NPCs com papéis efetivos, encontros/mobs/bosses, objetivos nas localidades, viagens finais e coerência entre saves e instalação de mapas. Distribuição dos mapas depende das autorizações registradas no README; documentação de proveniência de skins, áudio e demais recursos precisa acompanhar a entrega.
- **Interface:** consolidar o que já foi feito no pipeline GUI; revisar espaço para quests maiores, escalas e navegação. Hotbar de combate, novos indicadores, feedback de treino/pressão e recursos de disciplinas devem seguir os contratos finais, sem assumir que todas as propostas de arte são obrigatórias agora.
- **Integração e multiplayer:** dois clientes reais/observador remoto, servidor dedicado, reconexão/morte/dimensão, cancelamento das cenas, sincronização e recuperação de câmera/sons. Testar progressão e missões compartilhadas; não limitar validação ao singleplayer de poderes.
- **Desempenho:** medir frametimes e custo de partículas/modelos/cenas com hardware e cenário registrados. Contadores ocasionais de FPS não constituem benchmark. Definir perfis/limites visuais a partir dos resultados.
- **Estabilização e distribuição:** fluxo completo em mundo novo/existente, compatibilidade dos pacotes, reprodução em clone limpo, migrações de dados e critérios explícitos de aceite. Reavaliar pendências antigas pela evidência atual, em vez de reexecutar cegamente todo checklist de setembro.

## 7. Ferramentas e dependências — situação real

- **Better Combat 1.9.0 + PlayerAnimator 1.0.2-rc1 + Cloth Config 11.1.136; GeckoLib 4.8.4:** integração existente no perfil base. Expandir conforme necessidade e contrato, preservando Zaraki e evitando dois controladores disputando a mesma pose/ataque.
- **Blockbench/plugin GeckoLib e FFmpeg:** já usados/preparados nos ciclos anteriores para fontes editáveis, análise e evidências. Caminhos em `build/` são cache/artefatos locais, não garantia de instalação permanente em qualquer clone. Fonte de modelo e runtime procedural continuam distintos.
- **Photon 1.1.17/LDLib 1.0.42:** piloto opcional; testes de servidor passaram no perfil experimental, mas faltam homologação gráfica/editor e comparação de desempenho. Não tornar obrigatório para encerrar a Bankai. Há avisos recuperados documentados no scanner/editor.
- **Pipeline GUI e aparência:** ferramentas próprias já existentes em `tools/gui` e `tools/appearance`; reutilizar em vez de tratar autoria de telas/skins como infraestrutura ainda ausente.
- **spark:** candidato à medição controlada; benchmark ainda pendente. **Audacity, Blender, Aseprite/LibreSprite:** opcionais conforme tarefa, sem instalação nova nesta rodada.
- **Curios, SmartBrainLib e TerraBlender:** possibilidades para equipamentos, IA e mundo, respectivamente; dependem do contrato e da equipe responsável. Não são um pacote de instalações já aprovado como requisito universal do mod.
- **WorldEdit:** apoio opt-in de validação, com JAR local documentado; não presumir presente em clone limpo nem transformá-lo em dependência obrigatória de produção.

## 8. Documentos que exigem reconciliação

Correções aqui registradas para a equipe: documentos externos a `Docs/poderes` foram consultados, mas permanecem intactos por restrição desta frente.

1. [Mapa modular](../game-design/00-mapa-modular.md) e [handoff de game design](../game-design/00-handoff-para-outro-chat.md): atualizar o ponto de retomada. O convite antigo para iniciar M04/bloco 3 não é a próxima tarefa atual; ler conclusões de M04, M05 e M06-07.
2. [M04](../game-design/04-racas-e-origens.md) e [M06](../game-design/06-habilidades-e-combate.md): alinhar cabeçalhos com as decisões posteriores no corpo. Preservar histórico, distinguindo claramente revisões superadas.
3. [M02](../game-design/02-trilhas-de-progressao.md): sinalizar as revisões REV-M02-01 a 05 de [M05](../game-design/05-atributos-e-recursos.md), além da Proficiência orgânica do M06-07. Compra de ranks técnicos/compra de Mastery não deve reaparecer por leitura isolada do texto antigo.
4. [README geral](../README.md), [arquitetura](../arquitetura/01-arquitetura-geral.md), [rede](../arquitetura/06-rede-sincronizacao.md), [persistência](../arquitetura/07-persistencia-nbt.md) e [habilidades](../arquitetura/05-sistema-habilidades.md): separar snapshot DMZ, MVP histórico e runtime atual. Valores 2.1/2.3/2.19 e schema 3 não descrevem o protocolo 2.28/schema 9 vigente.
5. [Checklist MVP](../planejamento/todo-mvp.md) e [plano MVP](../planejamento/plano-implementacao-mvp.md): reclassificar itens já superados por técnicas, mobs próprios, modelos 3D e novas telas. “Não existem técnicas ativas” não vale mais. Pendências de multiplayer/escala/fluxo continuam exigindo evidências atuais, não uma baixa automática.
6. [Roadmap visual](../arte/roadmap-visual.md) e [arquitetura UI](../arquitetura/08-ui-hud.md): reconciliar com `telas-decisoes.md`, layouts JSON e pipeline já implementados. Não considerar Diário/Personagem/seletor de formas apenas propostas de arte.
7. [Viagens](../planejamento/plano-viagens-soul-society.md): distinguir atualização de outubro do plano original “não implementado”; dimensões/preset/mapas/TP provisório já existem, portais definitivos ainda faltam.
8. [NPCs e quests](../planejamento/plano-npcs-quests.md): a abertura informa implementação de `quest_giver`, mas o plano original abaixo ainda diz que ele não existe. Marcar explicitamente esse trecho como histórico; ampliar referência aos NPCs adicionais sem quests.
9. [Manual geral do jogador](../jogador/manual-do-jogador.md): criação/aparência/NPCs recebeu atualizações, mas permanece afirmação de durabilidade da Asauchi incompatível com o contrato atual de indestrutibilidade. Consolidar também controles e kits com o [manual da frente poderes](manual-do-jogador.md).
10. [Inicialização](../desenvolvimento/manual-inicializacao.md) e [build/dependências](../arquitetura/10-build-dependencias.md): conferir instruções para clone limpo, perfis base/Photon e dependências locais; conservar separação entre ferramentas de autoria e mods exigidos do jogador.

Os READMEs e a continuidade desta frente passam a apontar para este ciclo. Os relatórios antigos continuam como registro datado, não como novas tarefas autorizadas.

## 9. Evidências e fechamento desta revisão

Referência técnica mais recente: **127 regressões e 151 GameTests**, base e Photon, registrados no ciclo 71; capturas reais Benihime dia/noite/apoios +1/+2 e os dois gatilhos da Higashi. São resultados históricos, não testes reexecutados agora. Vídeos amostrados/silenciosos não homologam escuta ou fluidez contínua. Áudio tem feedback negativo atual sobre inteligibilidade do nome; dois clientes reais e benchmark controlado continuam pendentes.

Nesta rodada: inventário documental, conferência de fontes e código pontual, revisão de links dos documentos novos e `git diff --check`. Sem mudança de gameplay, áudio, modelos ou bibliotecas. O próximo trabalho da frente continua **fechar a apresentação Benihime e revisar a Higashi entregue**, respeitando o adiamento atual da mixagem; em paralelo de planejamento, a equipe pode continuar **M06-08**. Nenhuma das pendências deste relatório dispara implementação automaticamente.
