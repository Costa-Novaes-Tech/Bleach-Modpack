# Handoff vigente — Game Design do Bleach Mod

> **Checkpoint de revisão concluído — 23/10/2026:** M09-12C aprovado e aplicado: aldeões como civis vivos caçáveis, preservando prestadores essenciais. Retornos RAC-30/RAC-31A e M06-CS-01A/B/C/D concluídos conceitualmente; fontes M08 e amostra M09 integradas. M10b/9.4.1 liga estadias aos serviços de volta já escolhidos. [Resultado e entregas em 12.1 do parecer](00-revisao-cobertura-m01-a-m10-2026-10-23.md#121-conclusão-do-checkpoint-autorizado--23102026). M08/M09/M10 seguem em especificação de conteúdo; próximo M11 com retorno M05.

> **Publicação do checkpoint:** autorizada pelo usuário para após essa revisão. Conferir e publicar os arquivos acumulados de documentação; resultado Git deve constar da entrega. Nenhum código, mapa/save, build ou teste de jogo integra este checkpoint. Os registros inferiores conservam a sequência histórica; não retomar perguntas respondidas.

**Síntese anterior preservada — 23/10/2026:** Bloco principal [M10 — Dimensões, locais e estruturas](10-dimensoes-locais-e-estruturas.md), `EM ESPECIFICAÇÃO`. M10-01A/B aprovados: núcleos planejados com áreas geradas e proteção pequena/sinalizada de trechos essenciais. M10-02A/B decididos: rotas/serviços inicialmente e skills de abertura sem exclusividade racial absoluta; passagens comuns fora de combate. 02C admite técnica como a de Urahara e dispositivo como o de Mayuri. 03A revisado por 03D: skills próprias escolhem locais conhecidos e acessíveis, inclusive locais do jogador. 03B/C decididos: busca próxima; sem solução, permanecer na origem. 04A decidido: retorno viável. 03D/E decididos: escolha de local próprio conhecido/acessível e visita com registro manual. 05A decidido: Rukongai com atividades próprias. 05B decidido: pequenos refúgios distribuídos em Hueco Mundo. 05C decidido: Karakura adaptada ao Minecraft. 06A decidido: bases espirituais viáveis com abastecimento local. 07A decidido: transição breve nas viagens comuns. 07B decidido: convidados da mesma party. 07C decidido: Energia ao formar e janela sem manutenção. 07D/E decididos: preparo parado/cancelável e autonomia da passagem paga em regiões ativas. 07F/G decididos: uma passagem ativa, fechamento manual e ida/volta. Bloco comum revisado/consolidado conceitualmente; 08A decidido condicionalmente à pesquisa, com água transportada funcional como adaptação; 08B decidido sobre ponto de renascimento no mundo escolhido; 08C decidido sobre recuperação na falha do ponto; 08D decidido sobre ausência de seleção e posição no retorno; bloco espacial consolidado; 08E decidido no recorte Hueco Mundo: dormir a qualquer horário possível; 08F decidido sobre passagem da noite por mundo; 08G decidido sobre criaturas por região; catálogo M10b aberto e 09A decidido sobre primeiros percursos; 09B decidido sobre operadores da primeira rota a Hueco Mundo; 09C decidido: primeiro acesso por missão e contexto da campanha, separado de abertura própria. M09-01–11 aprovados: regras gerais `ESPECIFICADO CONCEITUALMENTE`, catálogo M09a `EM ESPECIFICAÇÃO`, revisão 09b com entregas antes da implementação. M08 mantém suas pendências. Base `main`, `3a37ca7`, ampliações locais; sem runtime/build/testes/publicação. SP compra skills/formas; Mastery pela prática.

## Projeto e objetivo

Mod de Bleach para Minecraft Java: personagem original, poderes canônicos, campanha guiada em mundo aberto e persistente, exploração, sobrevivência, construção, treino e multiplayer. O GDD descreve o horizonte completo, com implementação por entregas coerentes.

Fontes de entrada:

- [Mapa modular](00-mapa-modular.md): ordem e responsabilidade dos 16 módulos, template e estados.
- [Base de conhecimento](00-base-conhecimento-minecraft-bleach.md): pesquisa datada, fontes oficiais e contratos revisados.
- [Auditoria do checkout](00-auditoria-estado-2026-10-20.md): relação entre design, implementação e evidência histórica.
- [Continuidade de engenharia e poderes](../poderes/00-CONTINUIDADE-CODEX.md): ciclos técnicos; seus caminhos e branches históricos precisam de confirmação local.

## Ponto exato de continuidade

### Publicação autorizada após concluir o próximo bloco — 23/10/2026

**Pedido do usuário:** “Perfeito quando completarmos tudo do que ficou para esse próximo passo quero subir o que foi feito no repositório”. Commit e push da documentação ficam autorizados para depois da conclusão do bloco indicado na revisão: conteúdo funcional de M10, ambiente, circulação/retorno e modos de mundo; retornos dirigidos a M04/presença espiritual e M06/Combat State; complementos de M08/M09 necessários a esses percursos.

Antes de publicar, conferir decisões e pendências do recorte, referências e diferenças; selecionar explicitamente os arquivos de documentação acumulados e confirmar o destino Git vigente. A autorização não transforma propostas em decisões aprovadas. Registrar o commit e o resultado do push na entrega. **Checkpoint concluído documentalmente em 23/10/2026 no alcance de 12.1 do parecer; lote de publicação desta rodada. Consultar o histórico Git e o resultado da sessão antes de repetir a subida. Conteúdo completo dos catálogos continua em especificação.**

**Sincronização para a entrega:** 25 commits upstream incorporados por fast-forward, de `3a37ca7` a `18786a6`, antes do commit documental. Nenhuma alteração upstream em `Docs/game-design/`; os 76 arquivos locais foram preservados. Código/assets de poderes vieram do repositório; não foram reauditados, testados nem instalados no launcher por esta rodada. Plataforma local reconferida: Minecraft 1.20.1 / Forge 47.4.10 / Java 17.

### Próximo bloco após a publicação — M11 com retorno M05

Abrir itens, equipamentos e economia a partir dos percursos aprovados: sobrevivência (comida/fome/regeneração e recursos ao renascer), instrumentos espiritualmente compatíveis de treino/combate, foco Quincy, obtenção/reposição e fronteira entre SP e comércio. Não presumir que SP compra qualquer mercadoria, que Mastery é comprável ou que uma arma vanilla é imbuída automaticamente. M11 seleciona o conteúdo e M05 os recursos; novos modelos permanecem propostos até resposta. COV-03 (M02) e COV-04 (M03) retornam antes das primeiras aquisições/campanhas M12/M13; demais entregas por conteúdo em 12.1 do parecer. Não reabrir os contratos concluídos de portais, camas, visão ou Combat State.

### Rodada concluída — Perfis Hollow pela referência da obra — 23/10/2026

**M06-CS-01A/B/C/D decididos:** “Envolvimento real, mesmo sem perder Vida (recomendado)”; “Perigo separado do estado de combate (recomendado)”; “Cessar conflito e ameaça; respeitar encontro (recomendado)”; “Preservar janela e reavaliar o contexto (recomendado)”. Aplicados em M06a, históricos e referências M04/M05/M06/M07/M08/M09/M10. Fechamento funcional COV-06 em M06a/8.6; nenhuma pergunta pendente do estado automático. D estende a janela ao relógio de simulação com reavaliação, sem restaurar formas, encontro interrompido ou reentrada em boss; U/V/W e M09-06/08 preservados. Parâmetros, classificação concreta de efeitos/ataques e implementação continuam por ficha.

**M09-12A/B, respostas integrais:** “Quero que siga como a obra” e “Quero que seja como na obra”. Direção de fidelidade aplicada; nenhuma alternativa original foi escolhida automaticamente. Rascunho bruto territorial/ágil agressivo/voador apenas reativo e exclusão padrão de aldeões retirados como recomendações; texto anterior integral preservado no histórico. Pesquisa ENC-S06–09 em M09a/4 usa Shueisha/Shonen Jump, Pierrot e TV Tokyo, com limites de sinopse/glossário e sem capítulo/episódio integral. Perfis M09a/9.5–9.6 usam referência predatória, sentidos limitados e distinção entre presa/oponente; não fixam personalidade pela anatomia, canibalismo universal, precisão ou número canônicos. Regras aprovadas de proteção essencial, busca/terreno e chefe opcional preparado continuam.

**M09-12C decidido em M09a/9.7 e M09/12.26**, com [pergunta enviada](historico/2026-10-23-propostas-m09-12ab-sentidos-hostilidade-e-alvos.md): tratar aldeões como civis vivos caçáveis por Hollows, ou restringir a caça a civis/Plus próprios de Bleach. Resposta em 23/10/2026: “Aldeões como civis vivos caçáveis (recomendado)”. Associação aprovada como adaptação; o cânone não decide a identidade de uma entidade Minecraft. Consequência sobre vilas expostas explicada; prestadores essenciais preservados. Não aprovar por associação dieta de pets/fauna/golems, dano incidental, entrada tardia ou imunidade de observadores.

M08a/9.40 integra presença/competência/combate nas fontes iniciais e serviços já escolhidos, sem ampliar currículo, atribuir Cero ao veterano ou conceder técnicas provisórias. Kidō nominal, fonte avançada de Cero, itens/SP, substituições por era e missões continuam entregas concretas antes do conteúdo correspondente, sem afirmar que os prólogos já sejam executáveis.

12C aplicado às fichas; integração conferida e ligações de retorno registradas em M10b/9.4.1. Checkpoint concluído no alcance do parecer/12.1; publicação autorizada segue após conferência documental. M10 permanece em especificação de conteúdo. Nenhum código/mundo/save, build ou teste de jogo nesta rodada.

### Registro anterior — Estado automático de combate — 23/10/2026

**RAC-31A decidido:** “No atendimento do NPC (recomendado)”. Aplicado em M04/retorno dirigido, histórico, prestador M08a/8.8 e lugar M10b/9.6. O reset conclui no atendimento no Mundo Humano, conservando pés/direção se o estado inicial próprio do novo prólogo for válido; validação antes de apagar a jornada, recusa integral se não couber, sem remoção de blocos/teleporte ou corpo avançado preservado. Nova origem confirmada antes do prólogo; preços/perdas/preservações já aprovados não reabertos. Recursos/itens vinculados, missões/recompensas e gravação continuam com responsáveis e critérios antes da implementação. Nenhuma pergunta pendente de RAC-30A/B/C/D ou RAC-31A.

Aberto [M06a](06a-estado-de-combate.md) para COV-06, após leitura dirigida de M05/24, M06/5, M09/sentidos/busca/sem solução e consumidores M04/M07/M08/M10. [Perguntas integrais](historico/2026-10-23-propostas-m06-cs01abc-estado-de-combate.md) enviadas. **M06-CS-01A/B/C propostos, três respostas pendentes:** A — envolvimento real mesmo sem perder Vida, incluindo apoio efetivo, sem party automática ou tentativa incompatível; B — ambiente sem origem hostil separado de combate, com impedimentos de perigo próprios da operação; C — saída após cessar conflito/ameaça efetiva e respeito ao encontro preparado. Alternativas e limites no complemento. Não fixar tempos, dar visão secreta, punir construções ou classificar tudo como ameaça por proximidade.

São propostas de produto, sem nova alegação de cânone ou API. Aplicar respostas antes de transmitir novas regras de ambiente/saída aos consumidores. Em seguida fechar ciclo de vida/efeitos relevantes e complementos necessários de M08/M09. M10 permanece em especificação de conteúdo; publicação aguarda todo o bloco autorizado. Nenhuma alteração de código/mundo/save, build, teste de jogo, commit ou push.

### Registro anterior — Corpo e local após reset — 23/10/2026

**RAC-30A/B/C/D decididos:** A — “Visão no prólogo antes do combate exigido (recomendado)”; C — “Preservar interação vanilla (recomendado)”. B recebeu primeiro a objeção “mas no anime não é assim como você recomendou”, sem aprovação de alternativa; recomendação permissiva retirada. Após reformulação, B recebeu “Exigir capacidade espiritual do ataque (recomendado)”. D conciliou dano vanilla com C por “Mesmo requisito espiritual para NPC e jogador (recomendado)”. Respostas e perguntas preservadas em M04a/8/10 e histórico. Nenhuma dessas perguntas está pendente.

A visão básica não depende da leitura aprendida ou do saldo de Energia. Ataque/arma comum não recebe compatibilidade espiritual só por visão, reserva ou origem; um corpo espiritual, inclusive o do jogador, não sofre dano de golpe comum sem essa compatibilidade. Mobs vanilla mantêm detecção/tentativa de ataque e aldeões mantêm comércio; corpos humanos materiais seguem sujeitos ao dano comum. Essa consequência reduz o perigo ofensivo vanilla contra corpos espirituais; não introduzir uma exceção de dano contra o jogador sem revisão. M06/M08/M09 receberam referência de integração; componentes mistos/ambientais e instrumentos de treino precisam de ficha própria, sem alterar gravidade/queda ou ignorar as proteções aprovadas.

PRES-S04, resumo oficial Shueisha/Shonen Jump, identifica Chad como Fullbringer e define manipulação da alma da matéria. Não prova a causa daquele golpe contra Shrieker; PRES-S03 confirma apenas que ele luta sem ver na sinopse. Leitor VIZ do capítulo 1 redirecionou ao índice na localização do navegador, e o extrator não entregou quadros; nenhum capítulo/episódio integral conferido. A matriz é política de produto aprovada com limites de pesquisa explícitos, não uma lei universal comprovada do cânone.

**Atual: RAC-31A proposto em M04/retorno dirigido**, com [pergunta integral](historico/2026-10-23-proposta-m04-rac31a-corpo-e-local-reset.md) enviada: recomeçar no atendimento do NPC, com estado inicial do novo prólogo e recusa integral se não couber, ou levar à área inicial humana. Uma resposta pendente. Preserva reset gratuito, confirmação, perdas/preservações e contexto da campanha; não inventa corpo humano universal, cura, deslocamento entre mundos ou continuidade de corpo avançado. Ficha do reset/lugar apontam a proposta, sem adotá-la.

Após resposta, aplicar corpo/local às fontes e percursos e seguir M06/Combat State e complementos necessários. M10 permanece em especificação; publicação só depois do bloco completo. Nenhum código, mapa/save, build, teste de jogo, commit ou push nesta rodada.

### Registro anterior — Visão/convivência aprovadas e revisão da elegibilidade de dano — 23/10/2026

RAC-30A decidido por “Visão no prólogo antes do combate exigido (recomendado)”; RAC-30C decidido por “Preservar interação vanilla (recomendado)”. Aplicados em M04a/8.1/8.3/10 e histórico. Fontes iniciais e inimigos de Bleach precisam respeitar seus perfis sensoriais; mobs/NPCs vanilla conservam percepção e interação ordinárias. Visão básica da origem não exige comprar leitura de Reiatsu; A/C não aprovam o dano questionado de B.

**RAC-30B, resposta integral:** “mas no anime não é assim como você recomendou”. Objeção registrada, sem escolher automaticamente uma alternativa. Recomendação anterior de dano comum liberado por padrão retirada. PRES-S04, resumo oficial Shueisha/Shonen Jump, identifica Chad como Fullbringer; PRES-S03 reconsultada só confirma combate sem visão naquela cena. Não comprova capacidade de toda arma/pessoa comum nem causa exata daquele golpe. Leitor oficial VIZ do capítulo 1 indisponível na navegação por redirecionamento regional ao índice; nenhum capítulo/episódio integral conferido.

Pergunta reformulada enviada em M04a/8.2 e [histórico](historico/2026-10-23-propostas-m04-rac30abc-presenca.md): exigir capacidade espiritual do ataque/meio, sem dar imbuimento a uma arma vanilla apenas porque o personagem vê espíritos, ou permitir armas comuns por adaptação. **Uma resposta pendente.** Elegibilidade ofensiva, interação com blocos/comércio, proteção vanilla e capacidade sensorial são decisões distintas. Dano efetivo de mobs vanilla contra corpos jogáveis espirituais precisa de conciliação explícita antes de fechar B; preservar C sem inventar imunidade ou converter todo golpe em espiritual.

Depois, completar corpo/posição do reset e M06/Combat State, além dos complementos necessários de M08/M09. M10 segue em especificação; sem código, mapa/save, build, teste de jogo, commit ou push nesta rodada.

### Registro anterior — Presença espiritual e interação — 23/10/2026

M10-12B/C decididos pelas respostas “Operadores institucionais nas duas pontas (recomendado)” e “Áreas abertas conectadas aos núcleos (recomendado)”. Aplicados em M10/8.42/12.38–12.39, M10b/9.5–9.6/12.6–12.7, M08a/8.8/9.39 e histórico. Operadores Shinigami originais autorizados perto de Karakura e no acesso externo de Seireitei pelo Rukongai; recuperação em áreas abertas na periferia de Karakura, junto a povoado do Rukongai e junto ao refúgio inicial de Hueco Mundo, comportando Gillian. Reset próximo à área humana; nomes, implantação e fichas individuais continuam com seus responsáveis.

M10/8.43 confere os percursos e distingue escolhas aprovadas de conteúdo ainda necessário. Retorno de convidado em base distante depois de fechar o portal precisa de meio efetivo na ficha; áreas de renascimento não são teleporte de resgate. M10 permanece em especificação, sem certificar mapas, rotas executáveis ou catálogo completo.

[M04a](04a-presenca-espiritual-e-interacao.md) aberto para COV-05; [perguntas integrais](historico/2026-10-23-propostas-m04-rac30abc-presenca.md) enviadas. RAC-30A propõe visão espiritual ordinária antes do combate inicial que a exija; RAC-30B propõe compatibilidade comum de interação/dano, com exceções por poder; RAC-30C propõe preservar comportamento vanilla e perfis sensoriais próprios do conteúdo Bleach. **Três respostas pendentes; matriz ainda proposta.** Visão direta, leitura de Reiatsu, informação de alvo e possibilidade de causar efeito são contratos distintos. PRES-S01–03: sinopses oficiais VIZ/Pierrot/TV Tokyo; Chad lutando sem ver Shrieker sustenta a distinção, sem provar compatibilidade universal de armas comuns no cânone. Nenhum capítulo/episódio integral conferido nesta rodada.

Após respostas, aplicar a matriz e completar corpo/posição do reset, preservando origem e adiamentos aprovados; depois M06/Combat State e complementos necessários de M08/M09. Sem código, mapa/save, build, teste de jogo ou publicação antecipada.

### Registro anterior — Fontes de retorno e lugares de recuperação — 23/10/2026

M10-12A decidido por “Campanha em mundos novos e existentes compatíveis (recomendado)”. Resposta aplicada em M10/8.41/12.37 e histórico; o horizonte de produto abrange esses perfis, com integração/validação próprias em M16, sem afirmar que o instalador atual aceite saves usados. Não sobrescrever regiões/construções ou reinstalar cidades por personagem.

M10b/9.4 organiza os elos de retorno de 04A, incluindo convidado sem skill em base distante, corpo que evoluiu e fonte indisponível. Critério de completude da ficha, sem novo bloqueio de interface ou resgate automático. M10-12B/C propostos em M10/8.42/12.38–12.39 e M10b/9.5–9.6/12.6–12.7; [perguntas integrais](historico/2026-10-23-propostas-m10-12bc-operadores-e-recuperacao.md) enviadas. 12B: operadores Shinigami institucionais nas pontas humana/Soul Society, ou rede Urahara como operação comum. 12C: áreas abertas de recuperação conectadas aos núcleos, com reset próximo ao ponto humano, ou centros dos núcleos. **Duas respostas pendentes até este registro.** Funções candidatas em M08a/9.39 e nota do reset em 8.8.

Após respostas, aplicar perfis também em M08, revisar os percursos/contingências e seguir retornos prioritários M04/presença/interação/reset e M06/Combat State. Não reabrir noite/clima, marco de abrigo, retorno viável ou gratuidade do reset. M10 continua em especificação; publicação condicionada ao bloco completo, sem código, mapa/save, build ou teste de jogo.

### Registro anterior — Perfil de mundo — 23/10/2026

11A recebeu resposta condicionada: “Como é na obra mesmo? eu lembro de chover e ficar de noite lá também, se for isso mesmo podemos seguir com o recomendado por você”. WORLD-S11 confirmou noite/manhã no contexto do Rukongai pela sinopse oficial TV Tokyo do episódio 23 e chuva no quadro da galeria oficial TYBW episódio 7, inspecionado no navegador. Condição atendida; perfil recomendado adotado. Não houve capítulo/episódio integral lido/assistido; duração e probabilidades de clima são adaptações. 11B aprovado diretamente: “Marco de abrigo em bloco próprio (recomendado)”. Perfis e respostas aplicados em M10/8.39–8.40/12.35–12.36 e histórico, sem nova pergunta dessas escolhas.

M10-12A proposto em 8.41/12.37; [pergunta/opções](historico/2026-10-23-proposta-m10-12a-perfis-de-mundo.md) enviada: planejar campanha em mundos novos e existentes compatíveis por integração própria, ou restringir campanha completa a novo mundo História. **Uma resposta pendente até este registro.** A recomendação não afirma que o instalador atual realize essa integração. Depois da escolha, continuar fichas de lugares/serviços e retorno/recuperação, fazer retornos prioritários M04/M06 e dependências necessárias. Publicação permanece autorizada após concluir todo esse bloco, sem commit/push antecipado.

### Registro anterior — Ambiente e abrigo — 23/10/2026

M10-10A/B aprovados por respostas explícitas: “Atividades com moradores e responsáveis locais (recomendado)” e “Sobrevivência e acordos locais (recomendado)”. Aplicados em módulo/catálogo/histórico, preservando perguntas e alternativas. Frentes de atividade decididas; indivíduos, povoado/refúgio, missões e recompensas ainda precisam de fichas. A composição espacial dos demais atendimentos está em M10b/8.8, com pendências concretas.

M10-11A/B propostos e perguntas enviadas: ciclo de dia/noite e clima regional para Soul Society, ou iluminação diurna permanente; marco de abrigo em bloco próprio para registrar renascimento de corpo grande, ou estrutura mínima. Contratos em M10/8.39–8.40 e 12.35–12.36; [registro integral](historico/2026-10-23-propostas-m10-11ab-ambiente-e-abrigo.md). **Duas respostas pendentes até este registro.** Adaptações do mod, sem nova afirmação de clima canônico ou API. Aplicar respostas antes de tratar perfis como decididos; continuar lugares/serviços, recuperação/retorno e modos de mundo, depois retornos M04/M06 e dependências antes da publicação autorizada. Nenhum código, mapa/save ou teste de jogo.

### Registro anterior — Conteúdo local de Rukongai e refúgios — 23/10/2026

M10-10A/B propostos em M10/8.38/12.33–12.34 e M10b/8.3.1/8.5.1/12.4–12.5. Duas perguntas enviadas sobre conteúdo local: moradores/responsáveis e três frentes no Rukongai; reconhecimento, provisões e acordos nos refúgios, com funções diferentes por grupo. **Sem respostas até este registro.** 05A/B e 09B/C permanecem decididos; a proposta não altera o primeiro acesso por missão/contexto nem concede ensino/recompensa por associação.

M10b/8.7 articula entrada/orientação, Academia, divisões, prática, instituições especializadas e saída conforme funções já aprovadas de M04/M08; não escolhe novas permissões globais ou treze campanhas. WORLD-S10 reconsulta glossários/resumo oficiais do anime para contexto e acesso; atividades novas são adaptações, sem capítulo/episódio integral lido. Após as respostas, aplicar escolhas e seguir lugares/serviços restantes, ambiente, circulação/retorno, recuperação e modos de mundo. Em seguida, retornos prioritários de M04/M06 e dependências necessárias antes do commit/push autorizado. Sem runtime, mapas, build ou testes de jogo.

### Registro anterior — Revisão de cobertura M01–M10 — 23/10/2026

Pedido do usuário: revisar M10 e identificar generalização/especificação excessiva e decisões que deveriam ter sido tomadas desde M01. [Relatório, alcance e registro de ações](00-revisao-cobertura-m01-a-m10-2026-10-23.md). Corrigidos o exemplo do mapa que restringia indevidamente o currículo em M08 e o template que confundia amostra com cobertura. Conciliadas sínteses de M01/M05 com REV-SP-02. Catálogo M10b/7.1 registra funções e pendências dos lugares conhecidos, sem aprovar novos prédios, dimensões ou missões.

M10 continua como bloco principal; completar conteúdo funcional dos lugares, ambiente, circulação/retorno, recuperação e modos de mundo. Coordenadas, diálogos e valores finais podem vir depois; escolhas de funcionamento não são automaticamente delegadas à engenharia. Retornos prioritários a M04/presença espiritual e M06/Combat State; demais lacunas e responsáveis constam no relatório. Preservar os adiamentos explícitos de poderes/artes, híbridos, múltiplos personagens e cargos únicos. Nenhuma pergunta de 09C pendente; novas escolhas serão apresentadas em blocos concretos, sem repetir aprovações.

### Registro anterior — Primeiro acesso orientado pela campanha — 23/10/2026

**09C DECIDIDO por resposta complementar:** “Vai ser com base nas missões mesmo, ele vai para lá quando necessário e quando fizer sentido no arco da obra que reflete diretamente na campanha do mod”. Primeira ida a Hueco Mundo determinada por missão e contexto do arco/era, sem escolher Hollow comum ou Gillian como requisito universal. Fonte competente viabiliza a passagem; atravessar não concede a skill de abertura. Rota comum 09B preservada quando pertinente à missão, sem substituir automaticamente os transportes do roteiro. [Registro integral](historico/2026-10-23-proposta-m10-09c-momento-do-acesso-hollow.md); M10/8.36/12.32 e M10b/9.3/12.3.

M13 define missão/momento/motivação; M12 registra requisitos e marcos; M08/M10 sustentam fonte/rota/corpo/retorno. Estágio específico pode existir se a missão o justificar, sem declará-lo mínimo canônico universal de abertura. M15 mantém separados era compartilhada e progresso pessoal. Não interpretar como exigência de missão ativa para toda visita futura, nem revogar exploração entre capítulos. Pesquisa WORLD-S09 preservada com limites; sem nova pesquisa nesta resposta. Próximo: revisar dependências das rotas e completar os perfis restantes de M10; missões concretas ficam em M12/M13. Nenhuma pergunta pendente de 09C. M10 em especificação; somente documentação.

### Registro anterior — Pesquisa e esclarecimento de abrir versus atravessar — 23/10/2026

**09C EM ESCLARECIMENTO:** usuário exige fidelidade à obra e reafirma que a primeira viagem deve ocorrer por outra fonte, incluindo Senkaimon nas rotas pertinentes. A rota de 09B já é assistida: NPC abre → jogador atravessa → fonte do destino atende a volta. Isso não concede a skill de abertura. Hollow comum versus Gillian para o primeiro acesso assistido ainda não escolhido. [Resposta integral e pesquisa](historico/2026-10-23-proposta-m10-09c-momento-do-acesso-hollow.md); M10/8.36/12.32 e M10b/9.3/12.3.

WORLD-S09: verbetes oficiais consultados e referências secundárias indexadas localizam Menos/Gillian usando Kūmon (mangá 45/p.2, anime 14) e Renji abrindo Senkaimon com Zanpakutō (mangá 56/pp.17–18, antes da conquista de Bankai). Wiki integral HTTP 402; VIZ exige assinatura; nenhum capítulo/episódio integral consultado. Não fixar estágio mínimo universal, exigência de Bankai/formatura ou competência de todo Hollow/aluno a partir desses exemplos. Abertura própria mantém aprendizagem/ficha e SP; fontes/permissões/contexto não são concedidos por atravessar. Próximo explicar os limites e só então retomar o acesso assistido. M10 em especificação; sem runtime/build/testes/publicação.

### Registro anterior — Operadores aprovados; primeiro acesso Hollow em proposta — 23/10/2026

**09B DECIDIDO**, resposta “Operadores independentes nos dois lados (recomendado)”. Rota entre entorno discreto de Karakura e refúgio de Hueco Mundo com operadores Arrancar originais nas duas pontas, incluindo atendimento de volta. [Resposta integral](historico/2026-10-23-proposta-m10-09b-primeira-rota-hueco-mundo.md); M10/8.35/12.31 e M10b/9.2/12.2. Funções individuais em [M08a/9.38](08a-catalogo-funcional-de-npcs-e-faccoes.md); nomes/motivos/era/preço/execução continuam por ficha.

**09C PROPOSTO**, uma decisão pendente: primeiro acesso da jornada Hollow após fundamentos ainda como Hollow comum, ou somente após Gillian. [Pergunta/opções](historico/2026-10-23-proposta-m10-09c-momento-do-acesso-hollow.md); M10/8.36/12.32 e M10b/9.3/12.3. Não escolher acesso de outras origens, preço, quest nominal ou fonte de Cero por associação. M10 em especificação; sem runtime, build, testes ou publicação.

### Registro anterior — Proximidade inicial aprovada; acesso a Hueco Mundo em proposta — 23/10/2026

**09A DECIDIDO**, resposta “Primeiros percursos próximos de Karakura (recomendado)”. Fontes humanas em locais distintos/coerentes no mesmo entorno; veterano Hollow afastado da circulação civil; encaminhamento à Academia continua levando à Soul Society. [Resposta integral](historico/2026-10-23-proposta-m10-09a-primeiros-percursos-humanos.md); M10/8.34/12.30 e M10b/8.1/12.1. Distâncias/posições não escolhidas.

M10b/9 agora elabora perfis de encaminhamento Shinigami e primeira rota comum a Hueco Mundo. **09B PROPOSTO**, uma decisão pendente: operadores Arrancar originais independentes em ambas as pontas, ou passagem fixa mantida por dispositivo. [Pergunta/opções](historico/2026-10-23-proposta-m10-09b-primeira-rota-hueco-mundo.md); M10/8.35/12.31 e M10b/9.2/12.2. Retorno com atendimento local, acesso por era/corpo e custos exigem ficha; não conceder abertura ao veterano inicial, serviço a toda origem ou lealdade obrigatória a Aizen. Sem runtime, build, testes ou publicação.

### Registro anterior — Criaturas regionais aprovadas; catálogo de lugares aberto — 23/10/2026

**08G DECIDIDO**, resposta “Criaturas por região e identidade do mundo (recomendado)”. Geração natural de Soul Society/Hueco Mundo por região; monstros vanilla comuns somente por ficha. Mundo Humano mantém base vanilla com Hollows regionais. [Resposta integral](historico/2026-10-23-proposta-m10-08g-criaturas-por-regiao.md); M10/8.33/12.29. Não apaga entidades nem fixa taxas/despawn/loot ou transporte.

Aberto [M10b — catálogo funcional de lugares/rotas](10b-catalogo-funcional-de-lugares-e-rotas.md), em especificação: seis fichas de necessidade herdadas de M08/M09/M10, sem aceite de mapa ou ordem de produção. **09A PROPOSTO**, uma decisão pendente: primeiros percursos humanos próximos no entorno de Karakura, com fontes em locais distintos/coerentes e encaminhamento Shinigami à Soul Society, ou regiões iniciais afastadas. [Pergunta/opções](historico/2026-10-23-proposta-m10-09a-primeiros-percursos-humanos.md); M10/8.34/12.30 e M10b/12. Perfis ambientais restantes, fontes/retorno por era e condições de implantação continuam explícitos antes da entrega. Sem runtime, build, testes ou publicação.

### Registro anterior — Passagem da noite aprovada; criaturas por região em proposta — 23/10/2026

**08F DECIDIDO**, resposta “Passagem da noite por mundo (recomendado)”. Descanso elegível adianta somente o ciclo do mundo dos participantes, com regra de sono local do servidor; Hueco Mundo segue noturno e cooldowns seguem simulação. [Resposta integral](historico/2026-10-23-proposta-m10-08f-passagem-da-noite-por-mundo.md); M10/8.32/12.28. Não declara implementação de relógios independentes nem fixa ciclo em toda dimensão.

**08G PROPOSTO**, uma decisão pendente: geração natural por região/identidade em Soul Society e Hueco Mundo, monstros vanilla somente por ficha; alternativa inclui geração comum desses monstros. Mundo Humano preserva base vanilla com aparições Hollow regionais. [Pergunta/opções](historico/2026-10-23-proposta-m10-08g-criaturas-por-regiao.md); M10/8.33/12.29. Sem apagar mobs existentes, fixar taxas, aprovar transporte de pets ou eliminar materiais sem obtenção compatível com 06A. Depois perfis/fichas de regiões, retorno e implantação. Sem runtime, build, testes ou publicação.

### Registro anterior — Sono de Hueco Mundo aprovado; passagem da noite em proposta — 23/10/2026

**08E DECIDIDO NO RECORTE HUECO MUNDO**, resposta “Então se é sempre noite no Hueco Mundo pode dormir a qualquer momento possivel”. Dormir pode ocorrer a qualquer horário possível, em cama/corpo compatível, com requisitos de segurança/espaço/fase. Cama marca spawn; descanso preserva noite permanente e não acrescenta cura espiritual/atalho de cooldown. Não desligar regenerações usuais elegíveis. [Resposta integral](historico/2026-10-23-proposta-m10-08e-descanso-nas-dimensoes-espirituais.md); M10/8.31/12.27. Soul Society não recebe aprovação integral de sono/céu por associação.

**08F PROPOSTO**, uma decisão pendente: passagem da noite por mundo com ciclo, ou compartilhada entre esses mundos. Hueco Mundo segue noturno e não depende de amanhecer para descansar. [Pergunta/opções](historico/2026-10-23-proposta-m10-08f-passagem-da-noite-por-mundo.md); M10/8.32/12.28. Não confundir ciclo visual com relógios de combate, pausar servidor ou afirmar independência técnica já implementada. Depois perfis ambientais/fichas pendentes. Sem runtime, build, testes ou publicação.

### Registro anterior — Esclarecimento de cama, spawn e recuperação — 23/10/2026

**08E EM ESCLARECIMENTO.** Usuário reafirma que camas devem marcar spawn e questiona a comunicação de “pontos de renascimento” e a diferença para Nether. Não aprovou dormir nas dimensões espirituais por essa resposta. [Resposta integral](historico/2026-10-23-proposta-m10-08e-descanso-nas-dimensoes-espirituais.md); M10/12.27.

M10/8.27/8.30 agora explicam: cama compatível marca o próprio local de retorno após morrer, inclusive na Soul Society/Hueco Mundo conforme 08B; não exige segundo cadastro num NPC. Registro de destino de portal é independente. Área de recuperação é alternativa automática de segurança de 08C/D. Alternativa de abrigo para corpo grande segue aprovada conceitualmente, mas objeto/interação ainda não definidos. Comparação vanilla verificada em artigo oficial Minecraft: cama no Overworld, âncora no Nether; não implica copiar essa restrição para os mundos Bleach nem alterar Nether/End. Próximo esclarecer em linguagem prática, depois retomar somente a escolha de descanso pendente. Sem runtime, build, testes ou publicação.

### Registro anterior — Posicionamento consolidado; descanso espiritual em proposta — 23/10/2026

**08D DECIDIDO**, resposta “Manter local válido e recuperar bloqueios (recomendado)”. Sem ponto pessoal, renascimento na área inicial segura do Mundo Humano. Retorno preserva posição válida; impedimento verificado gera busca próxima/recuperação já aprovada, sem mudar estágio/recursos. [Resposta integral](historico/2026-10-23-proposta-m10-08d-posicionamento-inicial-e-retorno.md); M10/8.29/12.26. **Bloco espacial consolidado conceitualmente em 8.30**, com áreas, tamanhos, UI e carregamento ainda por ficha; não repetir as regras gerais por raça/região.

**08E PROPOSTO**, uma resposta pendente: descanso em camas da Soul Society/Hueco Mundo ou somente registro de renascimento. Registrar o ponto independe de conseguir dormir; recomendação preserva noite permanente, relógios e ausência de cura espiritual extra. [Pergunta/opções](historico/2026-10-23-proposta-m10-08e-descanso-nas-dimensoes-espirituais.md); M10/8.31/12.27. M10 segue em especificação: perfis ambientais, fichas de regiões/rotas, abastecimento concreto e implantação continuam com responsáveis. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Recuperação aprovada; pacote final de posicionamento proposto — 23/10/2026

**08C DECIDIDO**, resposta “Busca próxima → recuperação local → Mundo Humano (recomendado)”. Na falha do ponto escolhido: busca próxima se referência/acesso válidos; sem solução, área de recuperação no mesmo mundo; indisponibilidade/acesso impedido leva à área segura do Mundo Humano. Todas precisam comportar o corpo-base permanente, inclusive Gillian. [Resposta integral](historico/2026-10-23-proposta-m10-08c-recuperacao-do-renascimento.md); M10/8.28/12.25. Áreas concretas e execução permanecem entregas.

**08D PROPOSTO**, uma resposta pendente: sem seleção pessoal, renascer na área inicial segura do Mundo Humano; ao retornar, manter posição salva válida e corrigir bloqueio por busca próxima/recuperação de 08C. Pacote explicita ampliação de escopo, sem nova votação do nascimento racial ou de cada evento/raça. [Pergunta/opções](historico/2026-10-23-proposta-m10-08d-posicionamento-inicial-e-retorno.md); M10/8.29/12.26. Nenhuma realocação só por queda/inimigo próximo nem cura/reset de recursos. Depois consolidação espacial e demais decisões ambientais, fichas e condições de entrega. M10 em especificação; somente documentação, sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Renascimento escolhido aprovado; recuperação em proposta — 23/10/2026

**08B DECIDIDO**, resposta “Ponto de renascimento no mundo escolhido (recomendado)”: Mundo Humano, Soul Society ou Hueco Mundo, com cama para corpo compatível ou abrigo apropriado a corpo grande. Acesso/espaço válidos; nascimento inicial no Overworld e contratos de morte/estados preservados. [Resposta integral](historico/2026-10-23-proposta-m10-08b-local-de-renascimento.md); M10/8.27/12.24.

**08C PROPOSTO**, uma resposta pendente: na falha de um ponto pessoal, buscar próximo se referência/acesso válidos, depois recuperação preparada no mesmo mundo e, se indisponível/sem acesso, recuperação no Mundo Humano; alternativa volta diretamente ao Mundo Humano na falha. Corpo-base permanente deve caber em todas as saídas; áreas concretas, primeiro spawn e reconexão ainda exigem ficha. [Pergunta/opções](historico/2026-10-23-proposta-m10-08c-recuperacao-do-renascimento.md); M10/8.28/12.25. M10 continua em especificação; 08A e passagens preservados. Somente documentação; sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Água decidida conforme pesquisa; renascimento em proposta — 23/10/2026

**08A DECIDIDO** por resposta condicionada à existência de restrição na obra. Reaberto WORLD-S08: descreve ausência de água, sem regra de evaporação/transporte; pesquisa dirigida não localizou proibição explícita verificável. Adotada água transportada funcional como adaptação, com paisagem noturna/areia branca sem rios/lagos naturais. Evidência primária em contrário exige revisão; não se afirma pesquisa exaustiva nem física canônica de baldes. [Resposta integral e limites](historico/2026-10-23-proposta-m10-08a-ambiente-e-agua-hueco-mundo.md); M10/8.26/12.23.

**08B PROPOSTO**, uma resposta pendente: escolher ponto de renascimento nas três dimensões principais por cama/corpo compatível ou abrigo para corpo grande, ou concentrar no Mundo Humano. [Pergunta/opções](historico/2026-10-23-proposta-m10-08b-local-de-renascimento.md); M10/8.27/12.24. Depois fallback seguro, pontos perdidos/bloqueados e demais entregas ambientais. Nascimento inicial, raça, estados e consequências da morte permanecem nos contratos anteriores; dormir/explosão de cama não aprovados por associação. M10 continua em especificação; passagens comuns consolidadas em 10a. Somente documentação; sem código, mapas/saves, build, testes ou publicação.

### Registro anterior — Passagens revisadas; ambiente de Hueco Mundo em proposta — 23/10/2026

**07F/G DECIDIDOS**, respostas “Uma passagem ativa, com fechamento manual (recomendado)” e “Ida e volta enquanto aberta (recomendado)”. Bloco comum 07A–G consolidado conceitualmente em M10/8.25 e [revisão dirigida](10a-revisao-m10-2026-10-23.md). Textos correntes antigos conciliados; módulo permanece em especificação, com fichas/entregas antes da implementação. Retorno viável após portal fechar, carregamento/transferência e corpos grandes são entregas explícitas.

**08A PROPOSTO**, uma resposta pendente: ambiente exterior noturno/areia branca sem rios/lagos naturais e água transportada funcional como adaptação, ou impedir água estável também trazida. WORLD-S08: verbete oficial Pierrot consultado, sem prova de evaporação. [Pergunta/opções](historico/2026-10-23-proposta-m10-08a-ambiente-e-agua-hueco-mundo.md); M10/8.26/12.23. Depois decisões ambientais necessárias e fichas/condições de entrega para fechar M10 e seguir M11. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Preparo e ciclo aprovados; limite e sentido em proposta — 23/10/2026

**07D/E DECIDIDOS**, respostas “Preparo breve parado e cancelável (recomendado)” e “Cumprir a janela já paga (recomendado)”. Preparo parado, cancelamento sem cobrança/recuperação extra; pedido indisponível mantém preparo válido. Objeto formado/pago mantém prazo em morte/desconexão do abridor com regiões ativas; descarga/reinício encerra sem restauração. Estados pessoais não são restaurados por essa autonomia. [M10/8.21/8.22 e 12.19/12.20](10-dimensoes-locais-e-estruturas.md); respostas no histórico.

**07F/G PROPOSTOS**, duas respostas pendentes: uma passagem própria comum ativa com fechamento manual ou várias; ida/volta durante janela ou apenas ida. [Perguntas/opções](historico/2026-10-23-propostas-m10-07fg-limite-fechamento-e-sentido.md); M10/8.23/8.24 e 12.21/12.22. Depois consolidar/revisar passagens e pendências ambientais/espaciais/implantação. Instrumentos, parâmetros, fases específicas e transações de travessia ainda exigem ficha. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Custo/janela aprovados; preparo e ciclo em proposta — 23/10/2026

**07C DECIDIDO**, resposta “Custo ao abrir + janela sem manutenção (recomendado)”. Energia cobrada ao formar passagem; janela curta sem consumo contínuo; abrir e não usar não reembolsa. Quem abre pode atravessar, preservando janela para convidados da mesma party. Valores, duração e limites continuam por ficha; serviços/dispositivos mantêm perfil próprio. [M10/8.20/12.18](10-dimensoes-locais-e-estruturas.md); resposta exata no histórico.

**07D/E PROPOSTOS**, duas respostas pendentes: preparo parado/cancelável ou imediato; autonomia do objeto pago até prazo com regiões ativas ou encerramento adicional por morte/desconexão do abridor. [Perguntas/opções](historico/2026-10-23-propostas-m10-07de-preparo-e-ciclo-da-passagem.md); M10/8.21/8.22 e 12.19/12.20. Não estender autonomia ao preparo/estado pessoal, nem aprovar carregamento forçado. Depois limites/encerramento/sentido e revisão ambiental/espacial/implantação. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Convidados da mesma party aprovados; custo/janela em proposta — 23/10/2026

**07B DECIDIDO com resposta própria:** “Compartilhar com convidados da mesma party”. Restringir convidados ao grupo de quem abre, com autorização e travessia voluntária; não ampliar a externos ou autorização automática de toda party. Passageiro mantém acesso individual, combate e validação do corpo, sem exigir aprender abertura/registrar destino previamente. Registro manual e retorno viável preservados. [M10/8.19/12.17](10-dimensoes-locais-e-estruturas.md); resposta exata no histórico.

**07C PROPOSTO**, uma resposta pendente: Energia ao concluir abertura e janela curta sem manutenção ou consumo contínuo. Travessia normal do abridor preserva janela para convidados, sem decidir morte/desconexão/descarga ou outros encerramentos. [Pergunta/opções](historico/2026-10-23-proposta-m10-07c-custo-e-janela-de-abertura.md); M10/8.20/12.18. Valores/preparo/limites e ciclo de vida ainda exigem ficha; serviços/dispositivos não herdam automaticamente esse custo. Depois preparo/encerramento e revisão ambiental/espacial/implantação. Sem runtime, convites reais, mapas/saves, build, testes ou publicação.

### Registro anterior — Transição breve aprovada; convidados nas passagens em proposta — 23/10/2026

**07A DECIDIDO**, resposta “Transição breve; percursos em situações próprias (recomendado)”. Viagens comuns têm transição breve; trechos jogáveis de Dangai/Garganta ficam para rotas/situações com função própria. Não aprova custos, duração numérica, dimensão intermediária ou imunidade. [M10/8.18/12.16](10-dimensoes-locais-e-estruturas.md); pergunta/resposta no histórico.

**07B PROPOSTO**, uma resposta pendente: compartilhar passagem própria com convidados autorizados ou uso pessoal por padrão. Cada pessoa decide atravessar, sem precisar aprender abertura/registrar destino antes, mantendo requisitos individuais. [Pergunta/opções](historico/2026-10-23-proposta-m10-07b-convidados-nas-passagens.md); M10/8.19/12.17. Combate, acesso, chegada por corpo, registro manual e retorno viável são herdados. Depois execução/custo/encerramento e revisão ambiental/espacial/implantação. Sem runtime, convites reais, mapas/saves, build, testes ou publicação.

### Registro anterior — Abastecimento local aprovado; experiência de travessia em proposta — 23/10/2026

**06A DECIDIDO**, resposta “Base viável com abastecimento local (recomendado)”. Soul Society e Hueco Mundo oferecem meios locais para sustentar rotina básica de bases, mantendo identidade e complementaridade. Não aprova itens/receitas, abundância igual, cura ou mudança de alimentação. Contrato [M10/8.17/12.15](10-dimensoes-locais-e-estruturas.md); pergunta/resposta no histórico.

**07A PROPOSTO**, uma resposta pendente: transição breve nas viagens comuns com percursos jogáveis em situações próprias, ou percurso também no uso habitual. [Pergunta/opções](historico/2026-10-23-proposta-m10-07a-experiencia-de-travessia.md); M10/8.18/12.16. Preserva combate, destinos, validação/retorno e ciclo de vida; não define custos, preparo, duração, passageiros ou dimensão intermediária. Depois execução/compartilhamento e revisão ambiental/espacial/implantação. M10 continua em especificação. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Karakura aprovada; recursos e bases em proposta — 23/10/2026

**05C DECIDIDO**, resposta “Karakura adaptada ao Minecraft (recomendado)”. Referência principal do núcleo urbano humano, com lugares/bairros ligados às atividades e contatos, integrada ao Overworld explorável/construível. Não exige escala real, não aprova o mapa instalado ou prédios individuais. [M10/8.16/12.14](10-dimensoes-locais-e-estruturas.md); pergunta/resposta no histórico.

**06A PROPOSTO**, uma resposta pendente: viabilidade de bases nas dimensões espirituais com fontes locais de materiais/abastecimento ou dependência principal de suprimentos do Mundo Humano. [Pergunta/opções](historico/2026-10-23-proposta-m10-06a-recursos-e-bases.md); M10/8.17/12.15. Não decide receitas, novos itens ou alimentação/regeneração; preserva M04/H2 e contratos M05/M06. Depois ambiente/riscos e execução das viagens, com recursos concretos em M11. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Refúgios em Hueco Mundo aprovados; núcleo humano em proposta — 23/10/2026

**05B DECIDIDO**, resposta “Pequenos refúgios distribuídos (recomendado)”. Apoio independente distribuído em lugares ligados a fontes/grupos com funções e acesso próprios; Las Noches mantém contexto de era. Rede é adaptação aprovada, sem locais/serviços individuais já escolhidos, cura automática ou imunidade territorial. [M10/8.15/12.13](10-dimensoes-locais-e-estruturas.md); pergunta/resposta no histórico.

**05C PROPOSTO**, uma resposta pendente: Karakura adaptada ao Minecraft ou cidade original com locais canônicos como identidade do núcleo urbano humano. WORLD-S07 registra fontes oficiais do anime e limites: resumo Pierrot integral; apresentação TYBW indexada, abertura HTTP 403. [Pergunta/opções](historico/2026-10-23-proposta-m10-05c-nucleo-urbano-humano.md); M10/8.16/12.14. Não aprova mapa instalado, escala, prédios ou migração. Depois ambiente/recursos e execução das viagens. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Rukongai aprovado; distribuição de apoio em Hueco Mundo em proposta — 23/10/2026

**05A DECIDIDO**, resposta “Rukongai com atividades próprias (recomendado)”. Rukongai com povoados, atividades locais e exploração; espaços institucionais da Soul Society com formação/serviços conforme acesso/era. Sem nova etapa obrigatória antes da Academia, acesso permanente ou proteção de cidade inteira. [M10/8.14/12.12](10-dimensoes-locais-e-estruturas.md); pergunta/resposta no histórico.

**05B PROPOSTO**, uma resposta pendente: pequenos refúgios distribuídos ou um povoado independente principal para apoiar a exploração de Hueco Mundo. Funções e autonomia já aprovadas em M08/M07 são herança; proposta nova é espacial. Rede de refúgios é adaptação do mod, não estrutura canônica demonstrada. [Pergunta/opções](historico/2026-10-23-proposta-m10-05b-apoio-em-hueco-mundo.md); M10/8.15/12.13. Depois Mundo Humano/ambiente e execução das viagens. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Registro manual aprovado; funções dos lugares em discussão — 23/10/2026

**03E DECIDIDO**, resposta “Visitar e registrar manualmente (recomendado)”. Destino próprio exige visita e registro deliberado com nome; não exige construir portal permanente. Rotas/fontes apropriadas continuam permitindo primeiras viagens a lugares desconhecidos. Registro não concede acesso ou propriedade; chegada revalidada no uso. Contrato [M10/8.13/12.11](10-dimensoes-locais-e-estruturas.md); histórico da pergunta preservado.

**05A PROPOSTO**, uma resposta pendente: Rukongai com povoados, atividades locais e exploração, articulado aos espaços de formação/instituições da Soul Society, ou principalmente cenário de passagem. Papéis já decididos de M08 e necessidades de M04/M09 organizados em M10/8.14 sem nova votação. [Pergunta/opções](historico/2026-10-23-proposta-m10-05a-funcoes-rukongai-seireitei.md); M10/12.12. Depois funções dos demais núcleos/ambiente e execução das viagens. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Destinos próprios aprovados; registro em proposta — 23/10/2026

**03D DECIDIDO**, resposta “Escolher local conhecido e acessível (recomendado)”. Skills próprias podem selecionar locais conhecidos/acessíveis no outro mundo, incluindo base/local do jogador, sem ficar limitadas às entradas do mod. Revisa 03A; correspondência geográfica pela posição de abertura não foi escolhida. Serviços iniciais preservam rotas de 02A; condições espaciais de 03B/C, combate de 02B e retorno de 04A permanecem.

**03E PROPOSTO**, uma resposta pendente: visitar e registrar deliberadamente um destino próprio, com nome e sem construir portal permanente, ou registro automático ao explorar. Primeiras viagens por serviços/fontes apropriadas evitam circularidade. [M10/8.13/12.11](10-dimensoes-locais-e-estruturas.md); [pergunta/opções](historico/2026-10-23-proposta-m10-03e-registro-de-destinos.md). Depois funções dos núcleos. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Falha de chegada decidida; seleção de destino em revisão — 23/10/2026

**03C DECIDIDO:** “Sem espaço próximo, permanecer na origem (recomendado)”. Não trocar automaticamente para outra entrada. **03A EM REVISÃO:** o usuário esclareceu a intenção de viajar como referência do Nether, sem chegada presa a um local fixo. Não insistir no catálogo de entradas como solução fechada, nem assumir fórmula espacial canônica.

**03D PROPOSTO**, uma resposta pendente: escolher local conhecido/acessível, incluindo locais do jogador, ou chegada por correspondência geográfica com a posição de abertura. Recomendação e limites em [M10/8.11/8.12/12.10](10-dimensoes-locais-e-estruturas.md); [pergunta e manifestação integral](historico/2026-10-23-revisao-m10-03a-e-proposta-03d-destinos.md). Preservar 02B, 03B/C e 04A; depois funções dos núcleos. Sem runtime, mapas/saves, build, testes ou publicação.

### Registro anterior — Retorno aprovado; destinos esclarecidos — 23/10/2026

**M10-04A DECIDIDO**, resposta explícita “Retorno viável nas viagens comuns (recomendado)”, contrato em [M10/8.10/12.9](10-dimensoes-locais-e-estruturas.md). Meio de retorno viável sem aprendizado novo para sair; não exige o mesmo portal aberto, retorno gratuito/imediato ou uso em combate.

**M10-03C permanece pendente:** o usuário perguntou se Senkaimon/Garganta podem chegar a qualquer lugar e como adaptar isso. Não aprovou fallback. WORLD-S06 confirma chegada inicial do grupo ao Rukongai, fora da Seireitei; WORLD-S04 registra conexões diferentes por Garganta, sem comprovar qualquer coordenada para qualquer usuário. Recomendação/análise em M10/8.11: múltiplas entradas habilitadas por mundo, distintas da procura de espaço perto da entrada escolhida; locais/número ainda por ficha. 03A/B não foram revisados por essa pergunta.

Pergunta reformulada de 03C em M10/12.8: se nem perto da entrada houver espaço, manter na origem ou tentar outra entrada habilitada. [Perguntas e respostas integrais](historico/2026-10-23-propostas-m10-03c-04a-busca-e-retorno.md). Uma decisão pendente; depois funções dos núcleos e condições concretas de viagem. Sem código/assets/saves, runtime/build/testes/publicação.

### Registro anterior — Busca próxima aprovada — 23/10/2026

**M10-03B DECIDIDO**, resposta explícita “Buscar posição próxima fora da área”. Segunda opção escolhida, diferente da recomendação; contrato em [M10/8.8/12.7](10-dimensoes-locais-e-estruturas.md). A recusa imediata por falta de posição dentro da área foi substituída pela busca próxima, preservando o ponto estabelecido de 03A como referência e a necessidade de validar a posição encontrada. [Pergunta/opções/resposta](historico/2026-10-23-proposta-m10-03b-chegada-invalida.md). Não aprova raio, busca ilimitada, novo destino ou acesso por barreira.

**M10-03C/04A PROPOSTOS**, duas respostas pendentes: limite/falha da busca (recusar após busca sem solução ou tentar outro ponto habilitado) e retorno viável em viagens comuns de exploração. [Perguntas/opções](historico/2026-10-23-propostas-m10-03c-04a-busca-e-retorno.md). Não presumir respostas, método de resgate ou retorno em combate. Próximo respostas e funções dos núcleos/condições concretas de viagem; perguntas continuam sem pedido adicional. Sem runtime/build/testes/publicação.

### Registro anterior — Pontos de chegada aprovados — 23/10/2026

**M10-03A DECIDIDO**, resposta explícita “Chegada em pontos estabelecidos (recomendado)”, contrato em [M10/8.7/12.6](10-dimensoes-locais-e-estruturas.md). As skills básicas abrem em local válido e chegam a pontos estabelecidos nos destinos já habilitados. Não aprova marcação de pontos próprios ou todas as coordenadas/dimensões. Pergunta, opções e resposta preservadas no histórico 03A.

**M10-03B PROPOSTO**, uma resposta pendente: validar a área antes da travessia e no momento de transferir; sem posição válida, recusar e manter na origem. Alternativa procura posição próxima fora da área. [Pergunta/opções](historico/2026-10-23-proposta-m10-03b-chegada-invalida.md). A proposta considera o corpo que chegará, sem restaurar formas ou retirar blocos. Apoio, perigos, concorrência, carregamento e recursos exigem ficha; não promete imunidade ou retorno automático. Próximo resposta, retorno e funções dos núcleos; perguntas seguem sem pedido adicional. Sem runtime/build/testes/publicação.

### Registro anterior — Rotas, skills e duas vias de abertura — 23/10/2026

M10-02A/B decididos em [M10/8.3–8.4 e 12.3–12.4](10-dimensoes-locais-e-estruturas.md). 02A recebeu resposta própria: rotas/serviços no contexto inicial de cada era, depois skills para abrir passagens; sem exclusividade racial absoluta. Não apagar aprendizados por mudança de era. 02B escolheu a segunda opção: passagens comuns somente fora de combate; a proposta de fuga e seu preparo não foram aprovados.

Complemento espontâneo 02C: adotar tanto técnica de abertura/estabilização como a de Urahara quanto dispositivo como o de Mayuri. Pesquisa oficial e limites em M10/4/8.6; página TYBW integral retornou 403, recorte indexado e sinopse oficial ep.196 consultados. Não inferir número de Kidō, receita, elegibilidade universal ou aprendizado automático. [Respostas integrais](historico/2026-10-23-propostas-m10-02ab-rotas-e-travessia.md).

**M10-03A PROPOSTO**, uma resposta pendente: pontos de chegada estabelecidos ou também pontos próprios registrados para as skills básicas. A restrição fora de combate de 02B acompanha a passagem comum, qualquer que seja sua fonte; não repetir a aprovação por skill/dispositivo. [Pergunta/opções](historico/2026-10-23-proposta-m10-03a-destinos-das-passagens.md). Depois, destinos/chegada/retorno e funções dos núcleos, sem novo pedido de avanço. M09/M08 conservam pendências; sem runtime/build/testes/commit/push.

### Registro anterior — M09 revisado; M10 aberto — 23/10/2026

11B/C aprovados e registrados. [Revisão M09](09b-revisao-m09-2026-10-23.md) fecha apenas regras gerais como `ESPECIFICADO CONCEITUALMENTE`; [M09a](09a-catalogo-funcional-de-adversarios-e-encontros.md) continua em especificação. Sensores/alvos/geometrias, valores, locais, despawn/retorno bloqueado, concorrência/reentrada/distribuição e integração/validação têm responsáveis e condições anteriores à entrega. Nenhuma pergunta geral de M09 pendente; 10B preserva a segunda opção, primeiro elegível a concluir consome, sem reserva/rodízio.

[M10](10-dimensoes-locais-e-estruturas.md) criado nas 15 seções. **M10-01A/B DECIDIDOS**, respostas “Núcleos planejados com áreas geradas ao redor (recomendado)” e “Proteção pequena e sinalizada em trechos essenciais (recomendado)”, recebidas antes do registro documental; [histórico](historico/2026-10-23-propostas-m10-01ab-geografia-e-protecao.md). Nascimento no Overworld já aprovado em M04, não votado novamente.

**M10-02A/B PROPOSTOS**, duas perguntas enviadas/respostas pendentes: acesso por rotas/serviços e travessia comum arriscada em combate. [Perguntas/opções](historico/2026-10-23-propostas-m10-02ab-rotas-e-travessia.md). Fontes concretas, técnica/item, fases, duração e chegada/retorno ainda pendentes. Próximo respostas, funções dos núcleos e chegada/retorno/implantação; perguntas seguem sem pedido adicional.

Conferência local integral de HistoryWorld, instalador, item provisório, manifesto/README/preset: mapas pré-construídos existem; não demonstram geração mista/proteção/viagem futura. Sem inspeção visual de mapas, execução do instalador, migração ou alteração de save. WORLD-S01/02 reconsulta oficial do anime (sa/ra); WORLD-S03 usa recorte ka já consultado nesta sequência. Sem capítulos/episódios integrais ou API nova. Nenhum runtime/build/teste/commit/push.

### Registro anterior — Funções e papel do chefe aprovados — 23/10/2026

Respostas explícitas “Funções distintas para bruto, ágil e voador (recomendado)” e “Confronto opcional preparado (recomendado)” aprovam **M09-11B/C**, em M09/12.22/12.23 e M09a/9.1–9.4. [Perguntas/opções/respostas](historico/2026-10-23-propostas-m09-11bc-funcoes-e-chefe.md) preservadas. Funções e tipo do encontro definidos; sentidos, alvos, geometrias exatas, números, local/era e recompensas ainda exigem fichas. Nenhuma pergunta pendente; próximo revisão de alcance/entregas M09. M09-01–10 preservados; sem runtime/build/testes/publicação.

### Registro anterior — Amostra aprovada, funções/papel propostos — 23/10/2026

M09-11A aprovado: “Detalhar bruto, ágil, voador e chefe existentes (recomendado)”, resposta recebida antes do registro documental. [Catálogo M09a](09a-catalogo-funcional-de-adversarios-e-encontros.md) criado nas 15 seções, fichas ENC-H01–04 em elaboração. [Pergunta/opções/resposta 11A](historico/2026-10-23-proposta-m09-11a-amostra-de-fichas.md). Seleção documental não é autorização de produção ou parâmetros/era/região.

**M09-11B/C PROPOSTOS**, perguntas em M09/12.22/12.23, duas respostas pendentes; [histórico](historico/2026-10-23-propostas-m09-11bc-funcoes-e-chefe.md). B propõe bruto lento/amplo, ágil de pressão curta/reposicionamento e voador com aproximação/janela próxima ao chão. C propõe chefe opcional preparado, sem requisito universal dos prólogos/primeiras skills; alternativa é ameaça espontânea aberta. Não aprovam sensores/alvos/geometrias/valores, quest ou local.

Próximo: respostas e revisão do alcance conceitual/entregas M09. M09-01–10 preservados, inclusive segunda opção de 10B: primeiro elegível a concluir consome, sem reserva/rodízio. Fichas executáveis e calibração seguem responsáveis. Sem pesquisa externa/API nova, runtime, build, testes ou publicação.

### Registro anterior — Consumo pela primeira conclusão aprovado — 23/10/2026

Resposta explícita “Primeiro elegível a concluir consome” decide **M09-10B**, segunda opção, diferente da recomendação inicial. [M09/8.29 e 12.20](09-mobs-bosses-e-encontros.md#829-primeiro-elegível-a-concluir-consome--m09-10b-decidido) e [pergunta/opções/resposta](historico/2026-10-23-proposta-m09-10b-prioridade-de-consumo.md) atualizados. Primeiro elegível a concluir validamente consome; iniciar não reserva, uma conclusão esgota a oportunidade. 10A conciliado: alternância de presas pode ser combinada pelos jogadores, sem rodízio garantido. Pode haver disputa entre elegíveis. Nenhuma prioridade temporária/fila aprovada.

Nenhuma pergunta pendente deste bloco; próximo amostra/fichas/revisão com perguntas sem pedido adicional. Participação, duração, expiração, ordenação simultânea e feedback exigem ficha M09/M15/M16. M09 em especificação; 01–09/10A e RAC-19 preservados. Sem pesquisa externa/API nova, runtime, build, testes ou publicação.

### Registro anterior — Prioridade de consumo proposta — 23/10/2026

M09-10A aprovado e registrado, resposta recebida antes do registro documental. [M09/8.29 e 12.20](09-mobs-bosses-e-encontros.md#829-primeiro-elegível-a-concluir-consome--m09-10b-decidido): **M09-10B PROPOSTO**, uma pergunta enviada/resposta pendente. [Pergunta/opções preservadas](historico/2026-10-23-proposta-m09-10b-prioridade-de-consumo.md).

Recomenda-se vez temporária comunicada a um elegível, considerando participação/revezamento; recusa, inelegibilidade ou prazo passam a vez enquanto a oportunidade geral for válida. Alternativa: primeiro elegível a concluir. Algoritmo, desempate, contexto do revezamento, prazos e ausência ficam com ficha M09/M15. Uma conclusão esgota a presa; não cria item de direito ou cópia aos demais.

Próximo: resposta e amostra/fichas/revisão, sem pedido adicional de avanço. RAC-19 aplicado em 8.27; ENC-S05 conserva limites das fontes. M09 em especificação; 01–09/10A preservados. Sem nova pesquisa externa/API nesta proposta, runtime, build, testes ou publicação.

### Registro anterior — Devoração por presa aprovada — 23/10/2026

Resposta explícita “Uma devoração por presa, com revezamento (recomendado)” aprova **M09-10A**, recebida antes do registro documental: [M09/8.27/8.28 e 12.19](09-mobs-bosses-e-encontros.md#828-uma-devoração-por-presa--m09-10a-decidido), [pergunta/opções/resposta](historico/2026-10-23-proposta-m09-10a-devoracao-por-presa.md). Uma única devoração concluída por participante elegível; reconhecimento/recompensa dos demais não vira consumo. Distribuição ainda não aprova primeiro clique, maior dano ou fila. Próximo prioridade de consumo, sem pedido de avanço.

**Conciliação:** RAC-19 já mantém diferença sistêmica Shinigami/Quincy em backlog; aplicar a adaptação sem punir automaticamente cooperação pela raça do último atacante. M09/8.26 conciliado com 8.27; nenhuma revisão racial aprovada. ENC-S05 reconsulta glossários oficiais do anime ha/ka/sa, conceitos gerais/históricos, não política cooperativa ou obra integral. M09 em especificação, 01–09 preservados; sem runtime/build/testes/publicação.

### Registro anterior — Contribuição e prêmios aprovados — 23/10/2026

Duas respostas explícitas aprovam **M09-09A**, “Reconhecer contribuição útil mesmo fora da party (recomendado)”, e **M09-09B**, “Prêmio principal único; recompensas repetíveis separadas (recomendado)”. [M09/8.24/8.25 e 12.17/12.18](09-mobs-bosses-e-encontros.md#824-contribuição-no-mundo-aberto-fora-da-party--m09-09a-decidido) e [perguntas/opções/respostas](historico/2026-10-23-propostas-m09-09ab-contribuicao-e-premios.md) registrados. Nenhuma pergunta pendente do bloco. Reconhecimento fora da party cobre encontro comum aberto; prêmios únicos e repetíveis separados não definem valores, distribuição, reset de jornada ou alma. Próximo resultado/consumo e amostra/fichas, sem pedido adicional de avanço. M09 em especificação; 01–08 preservados; sem runtime/build/testes/publicação.

### Registro anterior — Contribuição e prêmios propostos — 23/10/2026

M09-08A/B aprovados e registrados. Ciclo revisado em [M09/8.23–8.26 e 12.17/12.18](09-mobs-bosses-e-encontros.md#823-revisão-estrutural-do-ciclo--alcance-e-entregas); **M09-09A/B PROPOSTOS**, duas perguntas enviadas/respostas pendentes. [Perguntas/opções preservadas](historico/2026-10-23-propostas-m09-09ab-contribuicao-e-premios.md).

A recomenda reconhecer ajuda útil fora da mesma party em encontro comum aberto, sem distribuir automaticamente prêmio ou alma. Participação efetiva e último golpe já herdados, não votados novamente. B recomenda prêmio principal único por personagem e recompensas de atividades repetíveis separadas/configuradas. Reset de jornada/flags de prêmio/itens preservados, valores e divisão exigem responsáveis M04/M05/M11/M12/M15.

Ciclo segue com despawn, retorno bloqueado, resultados simultâneos, reentrada/terceiros e fichas executáveis pendentes. Predação/destino da alma ainda exigem resolução conjunta; não duplicar presa. Próximo: respostas, resultado/consumo e amostra/fichas, com perguntas sem pedido adicional. M09 em especificação, 01–08 preservados; sem pesquisa externa/API nova, runtime, build, testes ou publicação.

### Registro anterior — Interrupção e relógios aprovados — 23/10/2026

Duas respostas explícitas aprovam **M09-08A**, “Encerrar como interrompida; nova tentativa manual (recomendado)”, e **M09-08B**, “Pausar tempos fora de simulação (recomendado)”. [M09/8.21/8.22 e 12.15/12.16](09-mobs-bosses-e-encontros.md#821-interrupção-da-prova-por-descarregamentoreinício--m09-08a-decidido) e [perguntas/opções/respostas](historico/2026-10-23-propostas-m09-08ab-interrupcao-e-relogios.md) atualizados. Nenhuma pergunta pendente do bloco. M09-01–07 preservados; ciclo decidido no recorte estrutural, sem fechamento executável. Despawn/retorno bloqueado, concorrência de resultado, reentrada/terceiros, parâmetros e fichas permanecem entregas. Próximo: autoria/recompensas/consumo com perguntas sem pedido de avanço. Sem runtime/build/testes/publicação.

### Registro anterior — Interrupção e relógios propostos — 23/10/2026

M09-07A/B aprovados e registrados. [M09/8.21/8.22 e 12.15/12.16](09-mobs-bosses-e-encontros.md#821-interrupção-da-prova-por-descarregamentoreinício--m09-08a-decidido): **M09-08A/B PROPOSTOS**, duas perguntas enviadas/respostas pendentes. [Perguntas/opções preservadas](historico/2026-10-23-propostas-m09-08ab-interrupcao-e-relogios.md).

A recomenda encerrar tentativa dirigida ainda não resolvida ao descarregar/reiniciar, sem vitória/falha presumida, exigindo nova confirmação/estado inicial ou checkpoint. B recomenda tempo do próprio mob comum simulado para cooldowns/recuperação, preservando Vida, sem cura offline ou golpe antigo retomado. Tempos do jogador preservados. Não define despawn, limpeza indiscriminada, reentrada, escala/crédito ou carregamento forçado.

Próximo: respostas, consolidação estrutural do ciclo com pendências explícitas e autoria/recompensas/consumo, sem novo pedido de avanço. M09 em especificação; 01–07 preservados. Sem pesquisa externa/API nova, runtime, build, testes ou publicação.

### Registro anterior — Recuperação e nova tentativa aprovadas — 23/10/2026

Duas respostas explícitas aprovam **M09-07A**, “Recuperação gradual após período sem combate (recomendado)”, e **M09-07B**, “Nova tentativa com estado inicial; checkpoints explícitos (recomendado)”. Recebidas antes do registro documental deste bloco; [M09/8.19/8.20 e 12.13/12.14](09-mobs-bosses-e-encontros.md#819-recuperação-dos-inimigos-comuns--m09-07a-decidido) e [perguntas/opções/respostas](historico/2026-10-23-propostas-m09-07ab-recuperacao-e-nova-tentativa.md) registrados. Nenhuma pergunta pendente do bloco. Cura comum gradual após período sem combate/dano; nova tentativa restaura o estado inicial previsto do adversário, com checkpoint explícito. Sem cura de jogador, reembolso, reset de luta ativa ou restauração de blocos. Parâmetros e lifecycle permanecem nas fichas. Próximo: descarregamento/reinício e relógios, sem pedido de avanço. M09 em especificação, 01–06 preservados; sem runtime/build/testes/publicação.

### Registro anterior — Início e falha coletiva aprovados — 23/10/2026

Duas respostas explícitas aprovam **M09-06A**, “Confirmar o início das provas e confrontos preparados (recomendado)”, e **M09-06B**, “Continuar com participantes aptos; falha coletiva (recomendado)”. [M09/8.16/8.17 e 12.11/12.12](09-mobs-bosses-e-encontros.md#816-início-deliberado-de-provaconfronto-preparado--m09-06a-decidido) atualizado; [perguntas/opções/respostas preservadas](historico/2026-10-23-propostas-m09-06ab-inicio-e-falha-coletiva.md). Nenhuma pergunta pendente do bloco. 01–05 preservados. Exceções, parâmetros, reentrada/terceiros, recursos/retomada e lifecycle continuam nas fichas. Próximo: perguntas de recursos/retomada sem pedido de avanço; sem runtime, build, testes ou publicação.

### Registro anterior — Início e falha coletiva propostos — 23/10/2026

M09-05A aprovado e registrado; perguntas continuam sem novo pedido de avanço. [M09/8.15–8.18 e 12.11/12.12](09-mobs-bosses-e-encontros.md#815-ciclo-do-encontro--organização-conceitual): **M09-06A/B PROPOSTOS**, duas perguntas enviadas/respostas pendentes. [Perguntas/opções preservadas](historico/2026-10-23-propostas-m09-06ab-inicio-e-falha-coletiva.md).

A recomenda confirmação para início de provas/confrontos preparados, sem colocar menu diante de toda hostilidade ou impedir eventos automáticos declarados. B recomenda continuidade enquanto houver participante apto, falha se todos derrotados e término após prazo em abandono completo. Exceções de objetivo precisam de ficha. Sem reentrada, ressurreição, formação automática de party, recompensa individual, parâmetros, cura/reset ou restauração da arena por associação.

**Próximo:** respostas e recorte de recursos/retomada/ciclo de vida; depois autoria/recompensas/consumo e fichas. M09-01–05 preservados, módulo em especificação. Leitura dirigida de fronteiras M03/STR-03 e contratos existentes; sem nova pesquisa externa, runtime, build, testes, commit/push.

### Registro anterior — Compromisso direcional aprovado — 23/10/2026

Resposta explícita “Ajustar no início e fixar antes do golpe (recomendado)” aprova **M09-05A**, em [M09/8.14 e 12.10](09-mobs-bosses-e-encontros.md#814-momento-de-fixação-da-direção--m09-05a-decidido); [pergunta/opções/resposta preservadas](historico/2026-10-23-proposta-m09-05a-compromisso-direcional.md). Nenhuma pergunta pendente deste bloco; 01–04 e herança M06 preservados. Direção ajustável de modo limitado no começo e fixada na parte final antes da fase ativa; parâmetros, geometria, sinais e exceções exigem ficha. Sem mudança de código/teste/publicação. Próximo: ciclo dos encontros e novas perguntas sem aguardar pedido de avanço.

### Registro anterior — Compromisso direcional proposto — 23/10/2026

M09-04A/B aprovados e registrados antes desta rodada. Seguindo a orientação de continuar as perguntas, [M09/8.13/8.14 e 12.10](09-mobs-bosses-e-encontros.md#814-momento-de-fixação-da-direção--m09-05a-decidido) consolida herança de execução e apresenta **M09-05A PROPOSTO**, uma pergunta enviada/resposta pendente. [Pergunta/opções preservadas](historico/2026-10-23-proposta-m09-05a-compromisso-direcional.md).

Recomendação: ajuste direcional limitado no começo do preparo, fixação na parte final antes do golpe. Alternativa: fixar desde o início. Não repetir aprovação de legibilidade, dano/controle, Recovery ou troca de alvo; fichas definem fases/geometria e exceções. Código comum/boss usa mecanismos diferentes; sem implementação da proposta, cenário executado ou pesquisa externa nova. M09 em especificação; 01–04 preservados.

**Próximo:** registrar a resposta e apresentar o bloco de ciclo dos encontros, sem exigir novo pedido de avanço. Tempos, sinais, corpo, meios e retorno bloqueado continuam nas fichas; sem build, testes, commit/push.

### Registro anterior — Terreno e construções aprovados — 23/10/2026

Duas respostas explícitas aprovam **M09-04A**, “Sem alteração de blocos por padrão; exceções definidas (recomendado)”, e **M09-04B**, “Tentativas limitadas e recuo sem solução (recomendado)”. [M09/8.10–8.12 e 12.8/12.9](09-mobs-bosses-e-encontros.md#810-construções-e-alteração-de-blocos--m09-04a-decidido) e [histórico das perguntas/opções/respostas](historico/2026-10-23-propostas-m09-04ab-terreno-e-construcoes.md) atualizados. Nenhuma pergunta pendente deste bloco; M09-01–03 preservados.

Cobertura: mobs comuns sem alteração de blocos por padrão, exceções futuras declaradas; tentativa limitada diante de alvo percebido sem solução, sem cura/invulnerabilidade. Parâmetros, retomada/retorno bloqueado, capacidades em água e políticas de chefes permanecem nas fichas/ciclo. **Próximo:** execução/contrajogo; apresentar novas perguntas sem exigir pedido de continuação. Sem runtime, build, testes ou publicação; ampliações locais.

### Registro anterior — Terreno e construções propostos — 23/10/2026

Usuário pediu continuar com as perguntas sem precisar dizer para prosseguir. **Método vigente:** após cada resposta, registrar/aplicar as decisões autorizadas e apresentar a próxima rodada pertinente, sem novo pedido de avanço. Não presumir aprovação das novas escolhas nem repetir decisões comuns.

[M09/8.10–8.12 e 12.8/12.9](09-mobs-bosses-e-encontros.md#810-construções-e-alteração-de-blocos--m09-04a-decidido) elaborado; **M09-04A/B PROPOSTOS**, duas perguntas enviadas e respostas pendentes. [Perguntas/opções preservadas](historico/2026-10-23-propostas-m09-04ab-terreno-e-construcoes.md). A recomenda ausência de alteração de blocos por padrão, com exceções preparadas; B recomenda tentativas limitadas e recuo quando o alvo percebido não pode ser alcançado. Sem parâmetros, cura, invulnerabilidade, nova técnica, teleporte ou política universal de boss. Retorno bloqueado e meios concretos ainda exigem ficha/ciclo.

Conferência dirigida de navegação/colisão/investida locais; sem auditoria integral de motor, nova pesquisa externa, runtime, build, testes ou publicação. M09 permanece em especificação; 01–03 aprovados. Próximo: respostas, consolidação do recorte e novas perguntas de execução/contrajogo; depois ciclo dos encontros.

### Registro anterior — Hostilidade e escolha de alvos aprovadas — 23/10/2026

Duas respostas explícitas aprovam **M09-03A**, “Hostilidade agressiva, territorial ou reativa por perfil (recomendado)”, e **M09-03B**, “Alvo estável, com motivos definidos para trocar (recomendado)”. [M09/8.8/8.9 e 12.6/12.7](09-mobs-bosses-e-encontros.md#88-hostilidade-dos-mobs-comuns--m09-03a-decidido) registra os contratos; [perguntas/opções/respostas preservadas](historico/2026-10-23-propostas-m09-03ab-hostilidade-e-alvos.md). Nenhuma pergunta pendente. 01A/B, 02A/B, RAC-16/17 e M08-01A/04B preservados.

**Cobertura:** motivos de hostilidade diferentes para mobs comuns e manutenção do alvo com troca motivada, sem retarget obrigatório a cada golpe/proximidade. Categorias concretas, critérios/valores, coordenação, políticas de chefe e dano de área permanecem nas fichas; não há barra de ameaça ou provocação ativa concedida. A informação sensorial continua limitada e a troca não apaga compromissos de execução.

**Próximo:** terreno/construções/falta de caminho; depois execução e ciclo. M09 continua em especificação. Conferência documental, sem nova pesquisa externa, runtime, build, testes, commit/push. Alterações locais.

### Registro anterior — Hostilidade e escolha de alvos propostas — 23/10/2026

Continuação autorizada com “Excelente vamos seguir”. [M09/8.8/8.9 e 12.6/12.7](09-mobs-bosses-e-encontros.md#88-hostilidade-dos-mobs-comuns--m09-03a-decidido) elaborado; **M09-03A/B PROPOSTOS**, duas perguntas enviadas e respostas pendentes. [Perguntas/opções preservadas](historico/2026-10-23-propostas-m09-03ab-hostilidade-e-alvos.md). 01A/B, 02A/B, RAC-16/17 e proteção/advertências M08 preservados.

**A recomenda:** perfis comuns agressivos, territoriais ou reativos, cada qual com alvos e condições próprios. **B recomenda:** alvo estável com troca por motivo definido, sem alternância automática por proximidade ou cada golpe. Não escolhe listas concretas, parâmetros, barra de ameaça, provocação ativa, política de chefes, ajuda coletiva, dano de área ou recompensas. Conhecimento do servidor continua diferente de informação percebida pela criatura; troca de alvo não reorienta gratuitamente ataque comprometido.

**Próximo:** registrar respostas e desenvolver terreno/construções/falta de caminho; depois execução e ciclo dos encontros. Herança e fronteiras conferidas documentalmente. Nenhuma pesquisa canônica/API nova ou mudança de runtime, build, testes, commit/push nesta rodada. M09 segue em especificação; alterações locais.

### Registro anterior — Detecção e perseguição aprovadas — 23/10/2026

Duas respostas explícitas aprovam **M09-02A**, “Visão e percepção conforme o perfil (recomendado)”, e **M09-02B**, “Busca limitada e retorno à área (recomendado)”. [M09/8.4–8.7 e 12.4–12.5](09-mobs-bosses-e-encontros.md#84-detecção-por-perfil--m09-02a-decidido) atualizado; [perguntas/opções/respostas preservadas](historico/2026-10-23-propostas-m09-02ab-deteccao-e-perseguicao.md). Nenhuma pergunta desta rodada pendente; M09-01A/B e M06-11F/G/I/J/K preservados.

**Cobertura:** capacidade sensorial declarada por perfil; visão e indício espiritual aproximado distintos; Supressão não esconde o corpo nem apaga memória. Na perseguição comum, perda de sinais leva a busca limitada por informação válida e retorno, com limites próprios. Chefes exigem contrato próprio. Não aprova valores/capacidades concretos, hostilidade automática, seleção de alvos, audição global, compartilhamento sensorial, quebra de blocos ou cura/reset ao retornar.

**Próximo:** hostilidade, elegibilidade/prioridade de alvos e resposta ao terreno; depois execução e ciclo dos encontros. Revisão dirigida preservou a separação entre sensação, localização, caminho e acerto. M09 continua em especificação; ficha técnica e cenários futuros pendentes. Sem pesquisa externa nova ao registrar respostas; ENC-S04 conserva consulta/limites. Alterações documentais locais, sem runtime, build, testes, commit ou push.

### Registro anterior — Detecção e perseguição propostas — 23/10/2026

Continuação autorizada com “Perfeito, vamos seguir”. M09/8.4–8.7 e 12.4–12.5 elaborados sob M06-11F/G/I/J/K: informação aproximada, Supressão sem invisibilidade/perda automática de aggro e oposição gradual. **M09-02A/B PROPOSTOS**, duas perguntas enviadas; respostas pendentes. [Perguntas/opções preservadas](historico/2026-10-23-propostas-m09-02ab-deteccao-e-perseguicao.md).

**A recomenda:** visão e percepção espiritual conforme o perfil, sem conceder Pesquisa ou radar preciso a todos. **B recomenda:** após perder sinais atuais, busca limitada por última posição/pista e retorno da perseguição comum, com limites próprios; chefes exigem contrato próprio. Memória, sensação, localização, hostilidade, caminho e acerto permanecem distintos. Sem aprovação silenciosa de audição global, alvo automático por raça, partilha de informação, teleporte, destruição ou cura/reset ao retornar.

**Conferência:** seletores locais usam proximidade/visão e reação; voador usa posição atual em trajetos. Não demonstram busca por informação limitada ou integração com Supressão; tradução técnica pendente antes da implementação, sem auditoria integral de classes vanilla ou cenário executado. Reconsulta ENC-S04 a glossário Pesquisa e retrospectiva oficial 1–20: anime complementar/resumos, sem alcance/IA universal comprovados. Modelos de busca são adaptações propostas.

**Próximo:** registrar respostas, depois elegibilidade/prioridade de alvos e resposta ao terreno. Perfis sensoriais concretos, tempos/distâncias, ciclo do encontro e demais fichas permanecem pendentes. M09 em especificação, 01A/B preservados; ampliações locais, sem runtime, build, testes ou publicação.

### Registro anterior — Abertura de M09 e duas decisões aprovadas — 23/10/2026

Continuação autorizada com “Perfeito, vamos seguir”. [M09](09-mobs-bosses-e-encontros.md) criado com as 15 seções do mapa, herança de M03–M08, responsabilidades, proposta de amostra e critérios futuros. **M09-01A/B DECIDIDOS**, duas respostas explícitas em 23/10/2026; nenhuma pergunta desta rodada pendente. [Perguntas/opções/respostas preservadas](historico/2026-10-23-propostas-m09-01ab-perfis-e-forca.md).

**A**, “Separar natureza, combate e papel no encontro (recomendado)”: três camadas, sem confundir tamanho/estágio, modo de lutar e comum/elite/chefe; elite não significa só mais Vida. **B**, “Força própria por perfil e contexto (recomendado)”: inimigos de mundo aberto não aumentam automaticamente por crescimento do jogador. Versões distintas têm contexto próprio; party/dificuldade de desafios ainda exigem políticas explícitas de M09/M15.

**Conferência local:** HollowVariant, HollowEntity, HollowFlightCombatGoal, HollowBossEntity, registros, spawn e loot examinados; bruto/ágil/voador e boss são base de estudo, não novas espécies/estágios aprovados. Código de circulação/mergulho/retirada e três ataques do boss existe; dados numéricos atuais não são balanceamento aprovado. Spawn do Overworld e alvos atuais não viraram políticas finais. Sem auditoria integral de modificadores externos.

**Pesquisa ENC-S01–03:** glossários oficiais Pierrot ha/ma e retrospectiva 1–20 reconsultados em 23/10/2026; variedade Hollow, classes e exemplos de desfechos. Anime complementar e resumos, sem mangá/episódios integrais; não comprovam tiers, escala ou parâmetros de IA do mod.

**Próximo:** detecção, seleção/perda de alvo e perseguição; depois terreno/construções, execução e ciclo dos encontros, autoria/recompensas/predação e fichas. Amostra de três variantes e boss em M09/10 continua candidata, sem primeira leva aprovada. M08 conserva práticas/SP/itens/cobertura por era/serviços pendentes antes da entrega; M09 não declara os prólogos executáveis. Linhagens/artes e Visored/híbridos continuam adiados.

Estado de M09: em especificação. Alterações documentais locais; sem runtime, build, cliente, testes, commit ou push nesta rodada.

### Registro anterior — Fontes e repertório inicial aprovados — 22/10/2026

Autorização: “Perfeito, vamos seguir dessa forma então”, após revisão 08b. [Catálogo/9.31–9.37](08a-catalogo-funcional-de-npcs-e-faccoes.md#931-fontes-iniciais--bloco-conjunto-das-quatro-origens): fontes, práticas candidatas, pequeno repertório, recursos e cobertura essencial. **M08-12A/B DECIDIDOS**, catálogo/12.10–12.11, duas respostas explícitas em 22/10/2026; nenhuma pergunta pendente. [Perguntas/opções/respostas preservadas](historico/2026-10-22-propostas-m08-12ab-fontes-repertorio-inicial.md).

**A**, “Adotar essas quatro fontes iniciais (recomendado)”: instrutor original da Academia; Hollow veterano independente no Mundo Humano com interesse local; mentor Quincy independente sem parentesco obrigatório; orientador Fullbringer original. **B**, “Adotar o repertório inicial (recomendado)”: fundamentos e possibilidade de adquirir Shunpo; corpo/defesa/caça Hollow com Cero apenas a partir de Gillian por fonte qualificada; arco e Hirenkyaku separados; Bringer Light e depois seu salto. Não exigir comprar todas as técnicas para concluir a introdução.

**Limites:** nomes/visuais, motivos/atividades concretos, SP/preços, obtenção de shinai/foco e demais itens, primeira aplicação delimitada de matéria, primeiro Kidō nominal, fonte avançada de Cero, cobertura executável por era e runtime continuam nas fichas responsáveis. Aprovação do pequeno repertório não entrega Mastery, gratuidade, segunda compra de salto ou toda técnica do orientador. M08-01–11, contratos raciais e contatos canônicos posteriores preservados.

**Pesquisa CAT-S23:** glossários Pierrot ha/ka/sa e retrospectiva 343–366 reconsultados em 22/10/2026. Anime complementar, sem obra integral; identidades sustentadas no recorte, currículo/fontes originais são adaptações aprovadas. Não houve fonte suficiente para selecionar um Kidō nominal inicial nesta rodada. Não importar regras de outro jogo.

**Revisão do recorte em 9.37:** não identificado conflito impeditivo entre seleção e contratos examinados; documentação não demonstra quatro prólogos executáveis. **Próximo módulo de planejamento: M09 — Mobs, bosses e encontros**, usando as necessidades de prática/caça. Pendências M08 conservadas antes das entregas correspondentes; não exigir todas as campanhas/recuperações para planejar encontros. Visored/híbridos e linhagens/artes permanecem adiados, Grimmjow primeiro caso depois.

Catálogo continua em especificação; regras gerais especificadas conceitualmente. Base `main`, `3a37ca7`, com ampliações locais. Sem runtime, build, teste, commit/push nesta rodada.

### Registro anterior — Revisão do catálogo de M08 — 22/10/2026

Pedido: “Perfeito, vamos para a revisão”. [Revisão registrada em 08b](08b-revisao-do-catalogo-2026-10-22.md): leitura de regras gerais/catálogo e confronto dirigido com M03–M07. Nenhum conflito impeditivo identificado no recorte; não é revisão exaustiva do cânone ou runtime. M08-01–11 preservados; nenhuma nova resposta/decisão de produto nesta rodada.

**Resultado:** regras gerais continuam especificadas conceitualmente; catálogo continua em especificação. Oito lacunas com evidência/responsáveis: fontes iniciais, repertórios, procedimentos especiais, era, ficha do reset, percursos institucionais, amostra de aquisição/prática e perfis de presenças selecionadas. Corrigidas referências desatualizadas de status e formatação da tabela de fontes, sem alterar respostas.

**Próximo complemento recomendado:** fontes iniciais e repertório essencial das quatro origens, em um bloco conjunto, referenciando RAC-12, SP e Mastery. Concretizar fonte/situação, motivo, primeira prática possível, aquisição aplicável, encaminhamento e cobertura essencial por era. Não escolher automaticamente quatro novos NPCs ou skills gratuitas. Depois M09. Métodos especiais precisam estar prontos antes dos riscos/trocas correspondentes; não exigir todas as quests ou todo o catálogo antes do próximo módulo.

Nenhuma pergunta pendente. Linhagens/artes e Visored/híbridos permanecem adiados. Base `main`, `3a37ca7`, com ampliações locais; sem runtime, builds, testes, commit ou push nesta revisão. Verificação documental de diff, estrutura e referências locais registrada na entrega.

### Registro anterior — Rede Urahara e serviços — 22/10/2026

Continuação autorizada com “Vamos seguind”. [Catálogo/9.27–9.30](08a-catalogo-funcional-de-npcs-e-faccoes.md#927-loja-urahara--estabelecimento-e-rede-de-apoio) elaborado: loja/rede, divisão de atendimento, matriz de serviços e continuidade. M08-01–10 e contratos M04–M07 preservados. SP compra skills/formas, Mastery pela prática; estoque/moeda de produtos continuam para M11/M15.

**M08-11A DECIDIDO**, catálogo/12.9, resposta “Adotar essas funções (recomendado)” recebida em 22/10/2026: Urahara para suprimentos/investigação/orientação; Yoruichi para mobilidade/combate corporal; Tessai para Kidō/barreiras. Preparo/contexto/motivo de cooperação, sem filiação à loja ou concentração de todas as recuperações. [Pergunta, opções e resposta preservadas](historico/2026-10-22-proposta-m08-11a-rede-urahara-servicos.md). Resposta explícita recebida em 22/10/2026; funções aprovadas, sem promover todos os detalhes das fichas a decidido.

**Pesquisa CAT-S22:** fichas Pierrot de Karakura/Urahara/Tessai/funcionários e Soul Society/Yoruichi, sinopses 1–20, 42–63 e 206–212 reabertas em 22/10/2026; anime como complemento identificado, sem episódio/mangá integral. Sustentam comércio/invenção, competência em Kidō, Shunpo e contexto de orientação/confrontos. O recorte não comprova todo currículo de Hakuda/barreiras; atribuições docentes ao jogador são adaptações aprovadas neste recorte de papéis. Técnicas concretas continuam exigindo seleção e fonte apropriadas.

**Serviços:** matriz mantém reset original gratuito, Ryūken como referência após Letzt Stil, Bankai por método específico do poder e troca/reacesso Fullbring ainda sem fonte concreta. Urahara não virou reparador ou prestador universal. Suprimento não ensina skill; treinamento não entrega Mastery; local de treino não cria instância, cura ou pausa global automaticamente.

Registros/roster: Urahara (duas representações) e Yoruichi presentes; Tessai/Jinta/Ururu ausentes dos 41 IDs. Nenhuma entidade adicionada. Produtos, repertórios, acesso, motivo, métodos, preço e cobertura por era ainda pendentes. **Próximo:** revisar lacunas de M08 antes de abrir M09. Visored/híbridos, linhagens e artes continuam na etapa própria; Grimmjow primeiro caso depois. Alterações locais de documentação, sem runtime, builds, testes, commit ou push.

### Registro anterior — Relações Hollow/Arrancar e estrutura de Aizen/Espada — 22/10/2026

Continuação autorizada com “Excelente, vamos seguir”. [Catálogo/9.22–9.26](08a-catalogo-funcional-de-npcs-e-faccoes.md#922-fontes-hollow-independentes--sobrevivência-corpo-e-caça): orientação de sobrevivência, relações locais, regime e elite, com continuidade por era. Aplicados contratos raciais M04/H1–H9, M06-08 e M07-04/05/06; independência racial, SP/Mastery, predação e condição permanente não voltaram à votação.

**M08-10A DECIDIDO**, resposta “Adotar essas funções (recomendado)”: fontes independentes orientam sobrevivência/caça; Nel conserva orientação local; Harribel participa de proteção/cooperação; Grimmjow atua como rival e avaliador em combate. **M08-10B DECIDIDO**, resposta “Adotar essa organização (recomendado)”: regime de Aizen com operações/compromissos, Espada como autoridades/rivais/adversários conforme pessoa, ensino condicionado à competência e motivo. Duas respostas em 22/10/2026, sem pergunta pendente. [Perguntas e respostas](historico/2026-10-22-propostas-m08-10ab-hueco-mundo-aizen-espada.md).

**Alcance:** funções, sem currículo, atividade/recompensa concreta, ingresso/cargo, rotina de ausência, método Hōgyoku ou arte aprovados por associação. Rival de treino e aliado não viram presa automaticamente; M07 mantém critérios. M08-04D preserva chefias singulares para depois e M08-06B não foi estendido a todos os compromissos do regime. Fonte inicial concreta e ensino Arrancar ainda exigem fichas; independência não garante acesso imediato a todo conteúdo.

**Pesquisa CAT-S19–21:** retrospectiva oficial Shōnen Jump sobre Arrancar, fichas Pierrot de Aizen/Espada/Arrancars, sinopses 132–151/152–167 e índice oficial TYBW. Mangá como base, síntese oficial da editora e anime complementar identificados; nenhuma obra/episódio integral. Personagens TYBW retornou 403 diretamente; seus trechos atuais não foram importados para eras anteriores. Representantes existentes reconferidos por nome; Gin/Barragan/Lilynette/Pesche/Dondochakka ausentes dos 41 IDs. Não foram criadas entidades.

**Próximo:** rede Urahara/serviços, depois revisão das lacunas de M08 antes de M09. Concretizar fontes, repertórios, motivos, permissões e continuidade conforme cada ficha. Linhagens/artes permanecem adiadas, Grimmjow primeiro caso na etapa própria. Alterações locais de documentação, sem runtime, builds, testes, commit ou push.

### Registro anterior — Fontes Fullbringer e Xcution — 22/10/2026

Continuação autorizada com “Perfeito, pode seguir”. [Catálogo/9.17–9.21](08a-catalogo-funcional-de-npcs-e-faccoes.md#917-fontes-fullbringer--aprender-antes-de-escolher-um-poder): fonte inicial independente, Xcution, papéis do elenco, aquisição/completude/troca e requisitos de espaços/continuidade. Aplicados M04/F1–F4/F7/F12/F13, M06-10A–F, M07-09A–C e contratos gerais M08. Chad continua contato aprovado M08-05A; sem repetir votação de fundamentos, SP/Mastery ou troca de poder.

**M08-09A DECIDIDO**, catálogo/12.6, resposta “Adotar esses papéis (recomendado)” recebida em 22/10/2026: Ginjō avalia preparo e conduz acordos; Jackie acompanha exercícios de combate; Riruka/Yukio oferecem desafios de treino ligados aos espaços de seus poderes, com era/motivação apropriadas. Não concede os poderes aos alunos nem exige filiação para fundamentos. [Pergunta, opções e resposta preservadas](historico/2026-10-22-proposta-m08-09a-fontes-fullbringer-xcution.md). Nenhuma pergunta pendente; aprovação cobre papéis, sem currículo/quest/local/arquitetura ou produção de quatro NPCs aprovada por associação.

**Pesquisa CAT-S17/18:** fichas e sinopse Pierrot dos episódios 343–366, treino/conselho/conflito e mudanças de contexto; índice oficial TYBW de personagens e sinopse THE HOLY NEWBORN. Esta última exibe episódio 32 em URL com id=33; diferença registrada. Personagens TYBW retornou 403 ao abrir; não houve episódio, mangá ou novel integral. Ginjō/Tsukishima não viram serviços universais de troca/reparo. Registros/roster local: Chad/Ginjō presentes; Riruka/Yukio/Jackie/Tsukishima/Giriko/Shishigawara ausentes dos 41 IDs, sem novas entidades criadas.

**Entregas ainda necessárias:** fonte inicial concreta, técnicas transmissíveis, motivos/atividades, ingresso/permissões, aquisição/reacesso por poder e continuidade por era. Espaços de Riruka/Yukio exigem contratos de entrada, saída, itens, falha e interrupções em M09/M10/M12/M15/M16; não presumir instâncias, morte não letal, rollback ou pausa global. Poderes, custos e dados seguem seus módulos.

**Próximo:** relações Hollow/Arrancar independentes e estrutura Aizen/Las Noches/Espada; depois rede Urahara/serviços e revisão das lacunas de M08 antes de M09. Linhagens/artes continuam adiadas, Grimmjow primeiro caso na etapa própria. Catálogo em especificação, regras gerais conceitualmente especificadas. Alterações documentais locais, sem runtime, build, testes, novo commit ou push.

### Registro anterior — Fontes Quincy, Wandenreich/Sternritter e recuperação — 22/10/2026

Continuação autorizada com “Excelente, vamos seguir”. [Catálogo/9.11–9.16](08a-catalogo-funcional-de-npcs-e-faccoes.md#911-quincy-independentes--tradição-e-prática-sem-filiação-obrigatória): fontes independentes; matriz de conhecimentos e aquisições; Wandenreich; Sternritter; recuperação com Ryūken; continuidade e encaminhamentos. Regras de origem, SP/Mastery, Blut, Schrift, Vollständig e Letzt Stil preservadas, sem novas votações de custos/fases/ciclo de vida.

**M08-08A DECIDIDO**, resposta “Adotar essa distribuição (recomendado)”: fontes independentes preservam tradição/ensinam aplicações de Reishi; Wandenreich oferece formação militar/operações; Sternritter são especialistas, autoridades ou adversários nas campanhas dos poderes. **M08-08B DECIDIDO**, resposta “Ryūken como referência de recuperação (recomendado)”: primeira referência canônica para recuperação após Letzt Stil, com motivo e procedimento adaptados, continuidade por fonte qualificada quando a era exigir e viabilidade sem outro jogador/capacidades suspensas. Duas respostas recebidas em 22/10/2026, catálogo/12.4–12.5; [perguntas, opções e respostas](historico/2026-10-22-propostas-m08-08ab-fontes-quincy-recuperacao.md).

**Alcance:** funções e referência de atendimento. Currículo, instrutor avançado concreto, ingresso/cargos, operações, rito de Schrift, método de recuperação, locais, materiais/custos, recompensas e acesso tardio continuam com fichas próprias. M08-06B não foi estendido a toda obrigação imperial. Saída institucional não é privação automática; evento especial de retirada de poder exige contrato próprio. Nenhum personagem recebe toda a docência Quincy por conveniência.

**Pesquisa CAT-S14–16:** fichas/glossário Pierrot reabertos sobre Sōken/Ryūken e treino de recuperação; Aniplex 04/07/2023 confirma elite imperial; índice oficial TYBW sustenta relações/Schrift/comando; sinopse oficial TV Tokyo do episódio 14 contextualiza a nomeação de Uryū. Glossário/personagens TYBW retornaram 403 diretamente, sem leitura integral; nenhuma cena/mangá integral ou currículo universal comprovado. Elenco reconferido por nomes nos registros/roster: Uryū presente; Ryūken/Sōken/Yhwach/Haschwalth ausentes dos 41 IDs, sem novas entidades criadas.

**Próximo:** fontes Fullbringer e Xcution, distinguindo fundamentos, poder principal e filiação; depois relações Hollow/Arrancar e rede Urahara/serviços. M08-08A/B sem perguntas pendentes; catálogo segue em especificação, regras gerais conceitualmente especificadas. Plataforma confirmada 1.20.1/Forge 47.4.10. Alterações locais, sem runtime, builds, testes, commit ou push nesta continuação.

### Registro anterior — Organizações especializadas e funções aprovadas — 22/10/2026

Continuação autorizada com “Perfeito, vamos seguir”. [Catálogo/9.6–9.10](08a-catalogo-funcional-de-npcs-e-faccoes.md#96-instituições-especializadas--ensino-colaboração-e-carreira): fichas de Onmitsukidō, Corpo de Kidō e Pesquisa e Desenvolvimento, distinguindo ensino, colaboração delimitada e carreira. Requisitos/SP/Mastery e vínculos anteriores preservados. M08-06A/B permanecem aprovados; nenhuma disciplina fica exclusiva de divisão por esta elaboração.

**M08-07A DECIDIDO em 22/10/2026**, resposta “Adotar essas funções como base (recomendado)”, catálogo/12.3: reconhecimento, infiltração e resgate/extração para Onmitsukidō; contenção, barreiras e ensino especializado para Corpo de Kidō; investigação, análise de materiais e projetos técnicos para Pesquisa. Aprovação cobre as famílias de função; atividades, fontes e currículos específicos continuam candidatos. Alternativa apresentada: rever as funções antes de adotá-las. Resposta recebida, nenhuma pergunta pendente; [pergunta, opções e resposta](historico/2026-10-22-proposta-m08-07a-organizacoes-especializadas.md).

**Pesquisa dirigida CAT-S12/13:** fichas e sinopses oficiais Pierrot consultadas em 22/10/2026, URLs e trechos em catálogo/4. Vínculos históricos do Corpo, competências de Tessai/Hachigen, atribuições Onmitsukidō e exemplos técnicos confirmados no recorte. Não capítulos/episódios integrais ou dirigentes atuais de toda era. Glossário TYBW retornou 403 diretamente; não usado como leitura integral. Tessai, Hachigen e Akon ausentes do elenco registrado; são candidatos, não adições obrigatórias. Nenhuma campanha híbrida, técnica proibida, sistema global de pesquisa/furtividade ou serviço universal de reparação aprovado.

**Próximo:** fontes Quincy independentes e Wandenreich/Sternritter. M08-07A registrado e refletido nos três perfis, sem promover os demais candidatos a aprovados. Serviços da rede Urahara, fontes das demais origens, currículo preciso, ingresso formal, posições, acesso/era e recuperações continuam pendentes. M08-06B não foi estendido automaticamente de carreira Gotei a qualquer compromisso institucional. Perfis elaborados não demonstram conteúdo executável; sem runtime, builds, testes, novo commit ou push nesta continuação.

### Registro anterior — Academia/Gotei e carreira aprovada — 22/10/2026

Continuação autorizada com “Excelente, vamos seguir”. [Catálogo/9.1–9.5](08a-catalogo-funcional-de-npcs-e-faccoes.md#91-academia--formação-e-encaminhamento): ficha de formação e papéis de atendimento da Academia; ficha funcional do Gotei; mapa das 13 divisões distinguindo referência canônica de oportunidade proposta; rotina de serviço e fronteiras com ensino/organizações especializadas. Regras herdadas de M04/RAC-07/12 e M08-01–05 preservadas. Não são treze classes de combate, currículos aprovados ou chefias jogáveis.

**M08-06A DECIDIDO**, catálogo/12.1, resposta “Preferência do jogador + aceitação da divisão (recomendado)”: uma divisão principal por vez; preferência do jogador depois da formação entre opções disponíveis, com requisitos/aceitação; transferência contextual, sem restringir as quatro disciplinas à lotação. Alternativa: primeira divisão determinada pela campanha. **M08-06B DECIDIDO**, catálogo/12.2, resposta “Deveres por atividades aceitas, sem cota diária (recomendado)”: deveres de carreira por atividades aceitas, sem cota diária/perda de cargo só por ausência; missão assumida e escolhas de campanha conservam suas consequências próprias. Alternativa: obrigações periódicas de presença. Duas respostas recebidas em 22/10/2026, nenhuma pergunta pendente; [perguntas e respostas preservadas](historico/2026-10-22-propostas-m08-06ab-divisoes-deveres.md).

**Pesquisa:** reconsulta a fichas/glossários oficiais Pierrot; CAT-S10/11 no catálogo/4 registra deveres, pesquisa e exemplos de assistência/atuação no Mundo Humano. Conteúdo TYBW acessível pelo índice oficial da busca; abertura direta do glossário e episódio 8 retornou 403. Não capítulos/episódios integrais; não inventar missão exclusiva para toda divisão. Saída institucional regular e modelo de carreira são adaptações identificadas.

**Próximo:** fontes especializadas e demais organizações, partindo das fronteiras de catálogo/9.5. M08-06A/B consolidados; mapa das divisões, atividades, interlocutores e currículos continuam candidatos onde não houver resposta própria. Repertórios específicos, fontes originais das outras origens, serviços/recuperações, cargos concretos, locais e cobertura por era continuam pendentes. M09 permanece próximo módulo novo, sem antecipar artes/linhagens. Mudanças documentais locais, sem commit/push adicional, runtime, build ou testes.

### Registro anterior — Catálogo funcional de M08 e primeiros contatos aprovados — 22/10/2026

O usuário perguntou quando seriam detalhadas as funções de cada NPC/mentor/facção. Foi esclarecido que M08 fechou regras gerais, não fichas executáveis; recomendação de complementar o catálogo antes de M09 aceita com “Perfeito, vamos seguir então”. [Catálogo funcional](08a-catalogo-funcional-de-npcs-e-faccoes.md) criado, com 15 seções, como conteúdo do próprio M08. Não é novo módulo ou conclusão das campanhas.

**M08-05A DECIDIDO:** “Adotar esses contatos, respeitando contexto e era (recomendado)”. Rukia e Renji para atuação Shinigami, Uryū para tradição Quincy, Chad para prática Fullbringer e Nel para orientação em Hueco Mundo. Primeiros contatos canônicos após fundamentos; não monopolizam prólogos nem concedem todo repertório. Fichas 8.3–8.7 identificam função, entrada/saída, atendimento candidato, disponibilidade/recusa, continuidade e pendências. Nenhuma técnica específica foi aprovada só por escolher o contato.

**M08-05B DECIDIDO:** “Prestador original dedicado ao reset (recomendado)”. Ficha 8.8: serviço de reset aprovado M04/M08-03A, acesso viável às quatro origens, sem taxa adicional. Nome, apresentação, local, frequência, itens vinculados/recompensas repetidas, mudança corporal e operação segura continuam abertos; não atribuir esse serviço a Urahara por conveniência. Perguntas/respostas preservadas no histórico vinculado pelo catálogo.

**Inventário:** oito IDs explícitos + 33 da lista adicional = 41; agrupamento documental em 36 entradas narrativas (Ichigo três, Aizen três, Urahara duas). Todos aparecem em catálogo/7.2; demais funções são candidatas/reservadas. Skins/IDs não comprovam serviços completos. A tabela não modifica registros ou migra quests. Funções originais necessárias e lacunas em 7.3; organizações candidatas em 9.

**Pesquisa:** fichas/sinopses oficiais Pierrot de Karakura, Gotei, Soul Society, Hueco Mundo, Espada, Visored, Fullbringers e trechos de treino; URLs, entradas e limites em catálogo/4. Não capítulos/episódios integrais. TYBW personagem/glossário retornaram 403 ao abrir; entradas de instituições acessíveis pelo índice oficial foram identificadas como tal. Ensino ao jogador e saída regular institucional são adaptações; não alegar fidelidade literal de todo procedimento. Plataforma não alterada, sem novas APIs ou testes.

**Próximo:** permanecer no catálogo M08, detalhar organizações/deveres/acesso, começando por Academia/Gotei e fontes iniciais originais; depois repertórios, serviços e cobertura por era. M08-05A/B sem perguntas pendentes. Não promover todas as 36 atribuições a aprovadas por associação, nem antecipar híbridos, Grimmjow/Pantera ou artes. M09 é o próximo módulo novo depois do recorte de conteúdo solicitado; não é necessário concluir todos os diálogos/campanhas antes de abrir esse módulo.

### Registro anterior — M08 consolidado e implementação atualizada — 22/10/2026

**Respostas registradas:** M08-04C “Saída e reingresso contextuais (recomendado)” em M08/8.13; M08-04D “Carreira pessoal; cargos únicos depois (recomendado)” em 8.14. Propostas originais preservadas em historico/2026-10-22-propostas-m08-04cd-saida-cargos.md. Saída regular encerra cargo/permissões dependentes e preserva história/domínio compatível; reingresso depende da organização, sem recuperar cargo automaticamente. Carreira pessoal nesta etapa; chefias únicas futuras, sem excluir do horizonte.

**M08 ESPECIFICADO CONCEITUALMENTE**, revisão em §15 e entregas em §12.1. Ensino/SP, serviços/recuperação/reset, continuidade por era, camadas sociais, ingresso/saída/cargos e fronteiras pessoais/compartilhadas confrontados. Nenhum conflito impeditivo localizado no recorte; não significa fichas executáveis, 41 NPCs completos ou aceite de gameplay. Próximo M09; não repetir perguntas desses contratos.

**[Conferência atual e lista de conteúdo](00-reconciliacao-implementacao-assets-2026-10-22.md):** `main` atualizado de `8ace652` para `bb26be85fc3dac703f67e79e772fca5158eecdef`, sem conflito nos documentos locais. Backup em `/private/tmp/bleach-retomada-20261022-195247`; comparação por hash preservou os demais arquivos preexistentes após o avanço. Código agora contém três variantes Hollow e boss em GeckoLib/entrada pela Garganta, 41 NPCs com `PlayerModel`, seis itens-base de Zanpakutō e modelos adicionais. Mundo Humano é mapa no Overworld; Soul Society/Hueco Mundo são dimensões adicionais do preset de História. Existem mapas locais em `history-maps/`; apenas `empty.nbt` em estruturas não prova cenários vazios.

**Lista enviada pelo usuário:** banco de candidaturas com todos os grupos sintetizados no parecer, não fila aprovada de produção. Rever o existente antes de ampliar modelos; skin/modelo/forma/item/IA/campanha não se equivalem. Prioridades recebidas P1/P2/P3 abertas. “Morcego do Ulquiorra”, classificação/estágios, medidas e novas mecânicas requerem fonte/ficha. Camadas visuais não substituem perfis funcionais Hollow nem antecipam híbridos. Grimmjow/artes específicos continuam adiados.

**Próximo passo recomendado:** abrir M09 pelos papéis de inimigos e ciclo de encontro, usando variantes/boss atuais como exemplos. M10/M11 conferem mapas/viagem/itens; M12–M16 fecham campanha e fatia de entrega. Não implementar a lista inteira agora. Sem build, cliente, teste ou JAR novo; atualização de fontes não atualiza sozinha o mod instalado no launcher. Relatórios QA recebidos são históricos e têm limites próprios, inclusive falha de Shibari no ciclo de ajuste da Vida dos Hollows.

### Registro anterior — Vínculos/advertência aprovados; ingresso e cargos propostos — 22/10/2026

**M08-04A DECIDIDO:** “Permitir vínculos compatíveis (recomendado)”, M08/8.11. **M08-04B DECIDIDO:** “Advertência; consequências por insistência (recomendado)”, M08/8.12. Filiações dependem de compromissos/era/contexto; golpe isolado no prestador protegido adverte, insistência/gravidade pode ter efeitos próprios. Não liberar lealdades inimigas irrestritas, morte gratuita, culpa coletiva ou delitos por tick do mesmo ataque. Propostas arquivadas em historico/2026-10-22-propostas-m08-04ab-vinculos-advertencia.md.

**M08-04C PROPOSTO**, 12.1: ingresso contextual; saída regular fora de combate; cessam cargo e permissões dependentes, preservando histórico/aprendizados compatíveis; reingresso condicionado e sem cargo automático. Alternativa: ruptura definitiva por padrão. **M08-04D PROPOSTO**, 12.2: carreira/promoções/funções pessoais coexistentes nesta etapa, sem substituir elenco; chefias/cargos únicos por servidor em etapa própria. Alternativa: vagas únicas de chefia já nesta etapa, com sucessão/ausência/cânone a resolver. Não escolhe título concreto ou exclui cargos únicos do horizonte. Duas respostas pendentes; depois revisão de integração/serviços e maturidade. Sem pesquisa canônica/API nova, runtime, saves ou testes; todos os custos, SP/Mastery, personagem único e reset permanecem.

### Esclarecimento vigente — Skills e transformações compradas com SP — 22/10/2026

**REV-SP-02, M05/11.2:** o usuário esclareceu: “não é para a compra de mastery não, é para comprar skills, transformações do mesmo jeito que o dragon mine z por exemplo”. **SP compra skills e transformações; Mastery cresce pela prática.** A compra de aquisição é estrutural, não uma exceção eventual limitada a atributos. Contexto/requisitos disponibilizam a aquisição; não basta saldo para ignorar condição racial, vínculo/prova previstos.

Compra persistente e ativação são distintas: SP no desbloqueio, Energia/Stamina e fases no uso conforme os perfis. A passagem, manutenção ou repetição de uso não cria recompra automática. Conciliar a camada de aquisição nas fichas de M06/M07, inclusive onde textos antigos descartavam SP genericamente; preços/exceções ainda pendentes. Documentação local de engenharia reversa Dragon Mine Z consultada como referência histórica, sem auditar sua versão atual ou copiar números/AFK. Demais achados da revisão M04 permanecem em seu escopo; M08 ainda não iniciado. Sem implementação/testes.


### Correção vigente — Spiritual Points permanecem na base de compras — 22/10/2026

O usuário corrigiu a revisão: “Mas ainda sim vamos utilizar de pontos sim para comprar as coisas, spiritual points, isso está na base do jogo”. **Registro intermediário: REV-SP-01 em M05/11.1, complementado e qualificado por REV-SP-02 em M05/11.2 acima.** Compras por SP permanecem estruturais e não devem ser genericamente restringidas aos atributos ou removidas das técnicas/habilidades. Disponibilidade/aprendizado e compra podem coexistir; Mastery continua não comprável, sem segundo Level obrigatório ou taxa retroativa para cada forma/ação.

O achado REV-01 do relatório transversal foi corrigido. Próximo: conciliar o modelo de compras das fichas com acesso, aprendizado e domínio, preservando pontos e sem inventar preços. A revisão dos outros módulos/M04 continua; M08 não iniciado. Demais achados permanecem no seu escopo. Sem código ou testes.


### Rodada vigente — Revisão de M01–M07, prioridade M04 — 22/10/2026

O usuário pediu revisar os módulos anteriores antes de M08. [Relatório transversal](00-revisao-modulos-01-a-07-2026-10-22.md) separa achados, correções documentais e decisões ainda abertas. M01–M05 lidos integralmente; M06/M07 confrontados pelos blocos de contratos, revisões raciais e conclusões pertinentes, sem alegar leitura exaustiva de todo seu histórico/catálogo/código ou nova auditoria do cânone.

**M04:** origem/natureza/afiliação, quatro rotas, cooperação, poder principal e morte de gameplay continuam aprovados. Notas locais aplicam M05/M06 aos pontos, F2/F4/F12 ao início Fullbringer, H8/H9 às rotas Hollow, M07 à evolução/formas e M07-09C à troca Fullbring. A pendência antiga de Vasto Lorde está resolvida; as fichas/valores permanecem delegadas. Original integral preservado em historico/2026-10-22-m04-antes-revisao-transversal.md.

**Lacuna real:** criação/continuidade foi prometida em M04, sem decisões localizadas de confirmação final, eventual reinício ou múltiplos personagens. Estado documental geral corrigido para `EM ESPECIFICAÇÃO`, sem revogar decisões raciais e sem aprovar reset/slots/migração destrutiva. M04 define produto; M14 interface, M15 identidade/servidor, M16 persistência/migração. Próxima discussão é esse pacote, sem repetir regras de raça/pontos/formas.

**M01/M02/M03/M05:** sinalizadas revisões de pontos/técnicas, origem Fullbringer e opressão básica. M06/M07 continuam especificados conceitualmente com suas entregas. O antigo quadro de estados deste handoff foi atualizado; registros anteriores mantêm seu contexto histórico. Nenhuma pesquisa externa ou execução de Minecraft nesta revisão; não representa aceite ou implementação. M08 ainda não iniciado, linhagens/artes adiadas.

### Registro anterior — Fullbringer C aprovado; M07 fechado conceitualmente — 22/10/2026

**C DECIDIDO**, M07/8.95: “Troca contextual, preservando domínio por poder (recomendado)”. Fullbringer A–C consolidado estruturalmente. Processo fora de combate com aquisição/reacesso apropriados, um principal por vez e histórico específico indisponível preservado, sem cópia de Mastery ou troca por objeto/menu; fundamentos/atributos preservados. Nenhuma pergunta desses registros pendente. Proposta original em historico/2026-10-22-proposta-fullbringer-c.md.

**M07 ESPECIFICADO CONCEITUALMENTE**, revisão comum e critérios de reabertura na seção 15. Perfis de Shikai, Bankai, evolução Hollow, Arrancarização, Resurrección, Vollständig, Letzt Stil e Fullbringer e C01–C12/G01/G02 examinados em revisão dirigida; nenhum conflito impeditivo identificado no recorte. Não é auditoria exaustiva do cânone/código, ficha executável de todo poder, implementação/teste/aceite. Nenhuma hierarquia universal inédita de cobranças, combinação ou exceção foi aprovada pela revisão.

**Próximo M08 — NPCs, mentores e facções.** M07/12.2 centraliza entregas antes da capacidade correspondente entrar em gameplay: fichas/fontes/repertório, valores/conversão de máximos, ensino/provas/recuperação, corpos/retorno e intervenções do mundo, itens/vetores/slots, efeitos longos/ordenação específica, UI, persistência/migração/validação. Risco exige recuperação/retorno viável; preservadas prioridades aprovadas. Fichas futuras explicitam escolhas/exceções novas, sem repetir votação geral. Sem pesquisa externa/API nova ao registrar resposta/revisar contratos locais, sem código/build/testes; datas/limites anteriores preservados. Linhagens/artes e híbridos excepcionais seguem etapa própria, não descartados do horizonte. Este registro governa continuidade; anteriores são históricos.

### Registro anterior — Fullbringer A/B aprovado; C proposto — 22/10/2026

**A/B DECIDIDOS**, M07/seções 8.93/8.94: “Marcos próprios e Mastery contínua (recomendado)”; “Custos por manifestação e técnica (recomendado)”. Marco próprio e domínio contínuo; manifestação básica pertinente paga Energia ao concluir, sem manutenção básica, efeitos/técnicas com custos próprios. Perfil operacional comum adotado para esse escopo. Nenhuma pergunta A/B pendente; proposta original arquivada em historico/2026-10-22-proposta-fullbringer-ab.md.

**Revisão de vetor na seção 15:** fichas declaram requisito real e quais capacidades dependem dele; fundamentos/domínio não somem por falta do item e vínculo não é transferido ao destinatário. Guarda, perda/destruição/reposição e slots precisam de fichas, sem nova votação genérica. Corpo não vira item destacável por omissão; não conceder recall/cópia/reparo/keepInventory. Formas Quincy mantêm fechamento estrutural.

**C PROPOSTO**, M07/seção 12.1, uma resposta pendente: troca contextual fora de combate, novo principal adquirido pelo caminho apropriado, histórico específico preservado e indisponível enquanto antigo poder estiver inativo; não copiar Mastery ou combinar repertórios. Retorno requer processo próprio de reacesso, sem loadout rápido ou repetir automaticamente toda primeira campanha. Cancelamento/invalidação anterior à conclusão preserva o principal anterior ainda válido; despesas/tempos seguem contratos. Não estender a outras raças nem aprovar pelo pedido de continuar. BL-17 reconsultado diretamente apenas para delimitar transferência/perda específicas, sem procedimento universal; C é adaptação proposta. Sem nova API, implementação/build/testes. Próximo resposta C, diferenças reais restantes e revisão comum; Fullbringer/M07 não fechados, linhagens/artes adiadas. Este registro governa continuidade; anteriores são históricos.

### Registro anterior — Fullbringer M07-09A/B proposto — 22/10/2026

**Duas respostas pendentes**, pacote único em M07/seção 12.1. A: desenvolvimento conforme o poder, completude como marco de domínio demonstrado e Mastery do poder contínua, sem uma barra por rótulo de estágio; técnicas distintas mantêm domínio próprio. B: manifestação básica deliberada de arma/corpo cobra Energia ao concluir, sem manutenção básica; técnicas/benefícios sustentados têm custos próprios; poder condicional não recebe transformação global por associação. Perfil comum operacional de B está explicitamente proposto. Não aprovar A/B pelo pedido de seguir.

Herança preservada: um principal canônico, vetor conforme objeto/equipamento/corpo, nenhum objeto pessoal obrigatório no prólogo, fundamentos e Bringer Light de M06 independentes; G01/G02 para manifestações pertinentes. Fichas de vetor/posse/guardar/perda/recuperação e processo especial de troca ainda exigem especificação, assim como prioridades entre efeitos sustentados na revisão comum; não reabrir votação de fundamentos nem dizer que Fullbringer fechou só com A/B. Sem nova Proficiência geral, Mastery comprada ou treino AFK.

**Pesquisa 22/10:** BL-11/17 e fontes novas BL-29/30/31 diretas, com limites em M07/seção 4; BL-05 403. Textos resumidos/editoriais e fichas licenciadas; não capítulos/episódios/novels integrais, importação de gameplay de Brave Souls ou currículo universal. Plataforma local reconfirmada sem novas APIs. Formas Quincy mantêm fechamento estrutural. Próximo respostas, diferenças reais de vetor/troca e revisão comum. Sem implementação/build/testes; linhagens/artes adiadas. Este registro governa a continuidade; anteriores são históricos.

### Registro anterior — Letzt Stil aprovado; formas Quincy consolidadas estruturalmente — 22/10/2026

**M07-08A/B/C DECIDIDOS**, seções 8.89–8.91: “Aprendizado próprio com Sanrei (recomendado)”; “Janela breve com compromisso (recomendado)”; “Perda recuperável por procedimento próprio (recomendado)”. Nenhuma pergunta pendente. Via Sanrei própria, sem Schrift/Vollständig/filiação permanente como requisitos; janela máxima breve, Energia ao concluir/sem manutenção básica; conclusão assume perda após toda saída, sem coexistir com Vollständig; recuperação própria de acesso Quincy preservando atributos/conquistas/domínio, seguida de novo preparo Sanrei para repetir. Preparo cancelado antes de concluir não assume perda. Perfil comum de B aplicado sem novas perguntas genéricas; G01/G02 preservados.

**Revisão estrutural das formas Quincy em M07/seção 15:** Vollständig A–R + Letzt Stil A–C, contratos comuns e G01/G02 coerentes no recorte examinado. Estados de conquista, forma, compromisso de perda e disponibilidade distintos. Não identificado conflito; nenhum cenário executado. Sanrei/consumo/reposição, fonte/quest, recuperação concreta, custos/duração/benefícios, corpo/alterações do mundo, práticas/domínio, UI e dados/validação continuam com responsáveis antes de entregar o risco. Não confundir esse fechamento com todas as Schrifts, progressão racial executável ou M07 concluídos.

**Próximo:** Fullbringer por diferenças — natureza da manifestação/completude, objeto/poder canônico, aprendizado/domínio e dependências. Não aplicar botão de transformação universal por omissão. Depois revisão comum de M07. Sem nova pesquisa externa/API ao registrar respostas; fontes mantêm datas/limites. Sem implementação, build ou testes. Proposta original arquivada em historico/2026-10-22-proposta-letzt-stil.md. Linhagens/artes adiadas. Este registro governa a continuidade; anteriores são históricos.

### Registro anterior — Letzt Stil M07-08A/B/C proposto — 22/10/2026

**Três respostas pendentes**, pacote único em M07/seção 12.1: A aprendizado tradicional com Sanrei, sem Schrift/Vollständig/filiação permanente como requisitos; B pedido deliberado com janela máxima breve, Energia ao concluir/sem manutenção básica, compromisso com perda após qualquer saída e sem sobreposição a Vollständig; C suspensão persistente de acesso às capacidades Quincy, recuperável por procedimento aprendido, mantendo atributos/conquistas/Proficiências/Masteries. B apresenta o perfil comum aplicável sem perguntas repetidas. Retirar luva no inventário não ativa; preparo cancelado antes de concluir não assume a consequência. Sanrei requer nova preparação para outro uso; consumo/integridade/reposição do objeto ainda precisa de ficha.

Durante a perda proposta: arco, Hirenkyaku, Blut, Gintō, Schrift, Vollständig e Letzt Stil indisponíveis; vida cotidiana Minecraft preservada. Recuperação precisa ser apresentada antes do risco e viável sem capacidades suspensas, outro jogador ou lealdade permanente; não restaura recursos/formas/prontidão Sanrei automaticamente. Estes recortes são propostas, não decisões canônicas ou de produto aprovadas. G01 já encerra manifestação no ciclo de vida, mas não cura a consequência comprometida; G02 rege retorno corporal pertinente.

**Pesquisa 22/10:** BL-16 e BL-23 reabertos diretamente; BL-03/Vollständig pelo índice após 403. Resumos oficiais, sem capítulo/episódio integral; não importar prazos literais/procedimento anatômico ou compatibilidades universais. Plataforma local reconfirmada, sem consulta de APIs. Vollständig A–R + G01/G02 mantém fechamento estrutural. Próximo registrar respostas, conferir diferenças próprias restantes, revisar Quincy e seguir Fullbringer/revisão comum. Sem implementação/build/testes. Este registro governa a continuidade; anteriores são históricos.

### Registro anterior — G01/G02 aprovados; Vollständig consolidado — 22/10/2026

**G01/G02 DECIDIDOS**, seções 7.3/7.4 de M07. Respostas: “Encerrar formas temporárias; retorno manual (recomendado)”; “Preservar espaço para o corpo-base (recomendado)”. Aplicar a Vollständig e futuros perfis temporários pertinentes, sem novas votações por raça/evento. Condições permanentes e consequências especiais preservadas; padrões não concedem voo, reparo, teleporte, recursos ou cura de risco persistente.

**Vollständig consolidado estruturalmente**, dezoito decisões específicas A–R + dois padrões gerais, sem perguntas pendentes desse conjunto. Revisão dirigida, entregas/responsáveis e condições de retorno em M07/seção 15. Não identificado conflito entre contratos examinados; nenhuma implementação/teste/aceite. Corpo/poses, alterações externas do mundo, instrumentos/itens, fonte/quest concreta, repertório, valores, UI e dados/validação continuam pendentes. Letzt Stil ainda impede fechar o conjunto das formas Quincy.

**Próximo:** pesquisar e apresentar um pacote das diferenças de Letzt Stil — aquisição, execução, consequência e recuperação — sem repetir contratos comuns. Depois revisão Quincy, Fullbringer por diferenças e revisão comum. Sem nova pesquisa externa/API ao registrar respostas; fontes mantêm datas/limites. Método contrato comum → perfil → exceções mantido; linhagens/artes adiadas. Este registro governa a continuidade; anteriores são históricos.

### Registro anterior — contratos comuns; G01/G02 propostos — 22/10/2026

**Mudança de método solicitada pelo usuário e aplicada:** contrato comum → perfil da forma → exceções justificadas. Seções 7.1/7.2 de M07 consolidam regras já aprovadas e sua cobertura, com matriz das diferenças; seção 8 mantém IDs/respostas/datas. Não voltar ao roteiro de perguntas repetidas por raça. Próximo bloco apresenta um pacote e consulta só diferenças, nova consequência, ampliação de escopo ou conflito real.

**Duas ampliações reais ainda PROPOSTAS, seção 12.1:** G01 estende ciclo de vida padrão a Vollständig/futuros perfis temporários, sem apagar condições/conquistas ou tratar Letzt Stil/quebra como curados; G02 estende passagem compatível com corpo-base a mudanças temporárias de corpo, com perda assumida de acesso a túneis menores. Regras já aprovadas continuam; expansão não foi aprovada apenas pelo pedido de agilizar. Respostas dessas regras gerais dispensarão repeti-las para cada perfil coberto.

Armas seguem C02/C09 e Vollständig H/I: ficha declara dependências reais, sem foco universal ou perda automática da forma por qualquer item. Após respostas, revisar Vollständig, seguir Letzt Stil/consequências e Fullbringer por diferenças; completude não vira transformação alternável por omissão. Fichas, intervenções forçadas do mundo, fontes concretas, valores e evidência técnica continuam pendentes. Sem nova pesquisa externa/API, implementação, build ou testes. Este registro governa continuidade; anteriores são históricos.

### Registro anterior — M07-07P/Q/R aprovados — 22/10/2026

**P/Q/R DECIDIDOS**, seções 8.85–8.87 de M07. Respostas: “Chão, ar e água, preservando queda e riscos (recomendado)”; “Exigir espaço na posição atual (recomendado)”; “Armadura equipada continua funcionando (recomendado)”. A–R: dezoito decisões aprovadas, nenhuma pergunta pendente. P preserva gravidade/queda/riscos; Q exige espaço funcional no início/conclusão, sem reposicionamento automático e sem cobrar ativação inválida; R conserva os mesmos itens de armadura, encantamentos elegíveis e desgaste, com apresentação adaptável.

Revisão dirigida não identificou conflito com A–O, preparo/custos/mãos e M06-11A/B examinados. Transformar não concede voo nem limpa queda; aparência não cria colisão/alcance/proteção adicional. Próximo: passagem/retorno corporal seguro, dependências de armamento e ciclo de vida; Letzt Stil e revisão depois. Medidas/poses, intervenções do mundo e valores continuam pendentes; não adotar regra de passagem de Resurrección por associação. Sem nova consulta externa, implementação, build ou testes nesta atualização de respostas; fontes mantêm datas/limites. M07 em especificação; linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07P/Q/R propostos — 22/10/2026

**A–O aprovados; P/Q/R PROPOSTOS na seção 12.1 de M07, três respostas próprias pendentes.** P: manifestar/encerrar em chão/ar/água válidos, preservando gravidade, trajetória, queda e riscos, sem voo automático. Q: conferir espaço do perfil funcional no início/conclusão, posição real e sem reposicionamento automático; diferenciar corpo e adornos de apresentação. R: quatro slots normais de armadura funcionais, mesmos itens/desgaste e encantamentos elegíveis, apresentação adaptável. Não aprovar pelo pedido de continuar ou por semelhança com Resurrección.

BL-03 reconsultado pelo índice após 403 direto, entrada dirigida de Vollständig/variação; BL-28 com nova abertura direta. Não determinam física ou slots do mod. Propostas preservam A–O e M06-11A/B, mãos/arco e fases. Passagem/retorno corporal ainda exige contrato antes de entregar perfis que mudem corpo; depois armamento/ciclo de vida, Letzt Stil e revisão. Voo/Sklaverei permanecem no horizonte com pesquisa/fichas próprias. Plataforma reconfirmada, sem capítulos/episódios integrais, novas APIs, implementação, build ou testes. M07 em especificação; linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07M/N/O aprovados — 22/10/2026

**M/N/O DECIDIDOS**, seções 8.82–8.84 de M07. Respostas: “Vollständig com uma aplicação de Blut (recomendado)”; “Vollständig compatível com estados de Reiatsu (recomendado)”; “Vollständig → Blut → Liberação → ação (recomendado)”. A–O: quinze decisões aprovadas, nenhuma pergunta pendente. Uma aplicação aprendida de Blut, nunca Vene/Arterie simultâneas; Natural/Supressão/Liberação compatíveis, sinais preservados e ofensiva válida encerrando Supressão. Custos/limites próprios continuam.

Revisão dirigida não identificou conflito com A–L e herança examinada. O insere manutenção da forma ativa antes do Blut; conserva Blut antes de Liberação e manutenção antes da ação. Sem forma ativa/devida, M06-11T permanece. Tentativa separada de cada estado válido, sem pagamento parcial/antecipado ou religamento automático. Ativação inicial paga como ação na conclusão, depois das manutenções já devidas; ação revalidada após término da forma. Próximo: meios/corpo/espaço/armamento/equipamentos e ciclo de vida; Letzt Stil e revisão depois. Valores/intervalos continuam pendentes. Sem nova pesquisa canônica/API, implementação ou testes nesta atualização. M07 em especificação; linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07M/N/O propostos — 22/10/2026

**A–L aprovados; M/N/O PROPOSTOS na seção 12.1 de M07, três respostas pendentes.** M: Vollständig com uma aplicação aprendida de Blut, nunca Vene/Arterie juntas. N: forma com Natural/Supressão/Liberação, sinais preservados; ofensiva válida encerra Supressão, apenas manifestar forma não é ofensiva automática. O: manutenção coincidente Vollständig → Blut → Liberação → ação, tentando separadamente estados ainda válidos; sem pagamento parcial/antecipado ou reativação automática. Não aprovar pelo pedido de continuar.

M06-11S/T e limites raciais continuam. O propõe inserir forma ativa antes do Blut; não altera Blut antes de Liberação nem manutenção antes da ação. Ativação inicial continua custo de ação ao concluir depois de obrigações devidas. BL-03 reconsultado pelo índice após 403, BL-28 diretamente/linha 76; não provam todo contrato de coexistência ou cobrança. Adaptações propostas. Meios/corpo/espaço, equipamentos, valores, exceções e ciclo de vida abertos. Próximo após respostas: esses contratos, Letzt Stil e revisão. Sem capítulo/episódio integral, novas APIs, implementação ou testes. Linhagens/artes adiadas. Este registro governa a continuidade; anteriores são históricos.

### Registro anterior — M07-07J/K/L aprovados — 22/10/2026

**J/K/L DECIDIDOS**, seções 8.79–8.81 de M07. Respostas: “Preparo breve cancelável para reagir (recomendado)”; “Encerrar manualmente e preservar conquistas (recomendado)”; “Transições sem recuperação adicional (recomendado)”. A–L: doze decisões aprovadas, nenhuma pergunta pendente. Preparo breve/caminhada lenta, reação disponível cancela sem recuperação extra e pedido recusado conserva preparo ainda válido. Encerramento manual quando a fase permite, retorno Quincy sem Vollständig preservando Schrift/conquistas. Sem taxa/recuperação adicional por transição, com custos/tempos anteriores mantidos.

Revisão dirigida não identificou conflito com A–I e a herança examinada. Sem mãos vazias universais, execução paralela de item/flecha/canal ou cancelamento gratuito da ação anterior. G mantém cobrança ao concluir/manutenção; nova ativação paga normalmente, sem refill ou reativação automática por regenerar. Corpo/espaço/arma e efeitos concretos, meios, equipamentos, compatibilidades/prioridade das manutenções e ciclo de vida continuam abertos. Próximo: compatibilidades com Blut/Reiatsu e ordem de pagamentos; demais contratos Quincy, Letzt Stil e revisão depois. Sem nova pesquisa canônica/API, implementação ou testes nesta atualização. M07 em especificação; linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07J/K/L propostos — 22/10/2026

**A–I aprovados; J/K/L PROPOSTOS na seção 12.1 de M07, três respostas pendentes.** J: preparo breve/caminhada lenta, cancelar para reação disponível sem recuperação extra; pedido recusado preserva preparo válido, sem duas execuções simultâneas. K: encerramento manual quando a fase permitir e retorno Quincy sem Vollständig, preservando Schrift/conquistas. L: conclusão/encerramento sem taxa/recuperação adicional; custos/tempos anteriores permanecem. Não aprovar pelo pedido de continuar.

BL-03 reconsultado pelo índice após 403; BL-28/passagem pelo índice. Fontes não determinam essas regras de execução; adaptações propostas, sem importar perda de Letzt Stil. M06-02S/T/U, Hirenkyaku e efeitos herdados preservados. Mãos vazias não seriam exigência universal no preparo, mas ação anterior precisa permitir transição; não preservar flecha/canal/item em paralelo ou guardar inventário automaticamente. Valores, meios/corpo/espaço, equipamento, compatibilidades/prioridade dos pagamentos e ciclo de vida continuam abertos. Próximo após respostas: esses contratos, Letzt Stil e revisão. Sem capítulo/episódio integral, novas APIs, implementação ou testes. Linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07H/I aprovados — 22/10/2026

**H/I DECIDIDOS**, seções 8.77/8.78 de M07. Respostas: “Sem instrumento universal; requisitos por poder (recomendado)” e “Permitir itens compatíveis durante a forma (recomendado)”. A–I: nove decisões aprovadas, nenhuma pergunta pendente. Forma não exige foco/luva/arco universalmente; instrumento específico precisa de ficha. Itens compatíveis permitidos durante forma paga, quando corpo, mãos e fase permitirem.

Revisão dirigida não identificou conflito com A–G e M06-09J/L/T examinados. Arco básico conserva foco e duas mãos; guardar manualmente antes de item incompatível, sem apagar tempos. Perder foco impede arco, sem encerrar forma que não o exige. Não manifestar arma/cópia, reparar equipamento ou suspender manutenção por alternar itens. Preparo/gestos e suas mãos, corpo/armadura, retorno, compatibilidades/prioridade de pagamentos e ciclo de vida continuam abertos. Próximo: preparo/reações e encerramento; Letzt Stil e revisão depois. Sem nova pesquisa canônica/API, implementação ou testes nesta atualização. M07 em especificação; linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07H/I propostos — 22/10/2026

**A–G aprovados; H/I PROPOSTOS na seção 12.1 de M07, duas respostas pendentes.** H recomenda forma sem foco/luva/arco universais, com exceções instrumentais justificadas por ficha; foco continua obrigatório para arco básico, não para toda capacidade. I recomenda mãos conforme arma/técnica, com ferramentas/blocos/consumíveis compatíveis durante a forma paga. Guardar arco é manual; não usar item junto do arco de duas mãos ou apagar tempos. Não aprovar pelo pedido de continuar.

BL-16 reconsultado diretamente, Cruz e Sanrei/Letzt Stil; BL-03 pelo índice após 403 direto. Não confirmado requisito instrumental universal para Vollständig nas fontes consultadas; H/I são adaptações propostas, não afirmação sobre todas as cenas. Preparo/gestos, armadura/corpo, instrumentos específicos, retorno, compatibilidades/prioridade dos pagamentos e ciclo de vida continuam abertos. Próximo após respostas: preparo/reações e encerramento; Letzt Stil e revisão depois. Sem capítulo/episódio integral, novas APIs, implementação ou testes. Linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07F/G aprovados — 22/10/2026

**F/G DECIDIDOS**, seções 8.75/8.76 de M07. Respostas: “Ativação manual independente da interface (recomendado)” e “Ativação + manutenção de Energia (recomendado)”. Propostas de 21/10, aprovações de 22/10. A–G: sete decisões aprovadas, nenhuma pergunta pendente. Pedido manual dentro/fora de Combat Mode; fechar interface não encerra forma. Energia na conclusão válida e manutenção enquanto ativa, sem duração fixa universal; cancelar antes não cobra ativação, falta de manutenção termina manifestação.

Revisão dirigida não identificou conflito com A–E e a herança examinada. Arco/Blut conservam desligamento próprio ao sair do modo; forma não suspende cobrança, restaura recursos ou apaga tempos. Reishi continua ambiental, sem nova barra/Sklaverei ou abastecimento ilimitado. Próximo: instrumentos/foco e mãos; preparo/reações, retorno, compatibilidades/prioridade das manutenções e ciclo de vida ainda exigem decisões próprias. Valores/intervalos e conteúdo seguem pendentes; Letzt Stil e revisão depois. Sem nova pesquisa canônica/API, implementação ou testes nesta atualização. M07 em especificação; linhagens/artes adiadas. Este registro governa a continuidade; anteriores são históricos.

### Registro anterior — M07-07F/G propostos — 21/10/2026

**A–E aprovados; F/G PROPOSTOS na seção 12.1 de M07, duas respostas pendentes.** F: pedido manual dentro/fora de Combat Mode, sem encerrar a forma ao fechar interface; arco/Blut mantêm desligamento próprio. G: Energia ao concluir e manutenção enquanto ativa, sem duração fixa universal; cancelamento anterior não cobra ativação, falta de manutenção termina a manifestação. Não aprovar pelo pedido de continuar.

São adaptações de operação/recursos. BL-03 reconsultado pelo índice após 403 direto; BL-28 aberto, passagem específica pelo índice. Fontes não determinam interface/cobrança universal. M05 mantém Reishi ambiental e uma reserva pessoal; sem Sklaverei, coleta manual, drenagem de blocos ou abastecimento ilimitado por omissão. Custos/intervalos exigem calibração conjunta com tiros/Blut. Compatibilidades/prioridade de pagamentos, foco/luva/mãos, preparo, retorno e ciclo de vida ainda abertos. Próximo após respostas: instrumentos e execução; Letzt Stil e revisão depois. Sem nova API, implementação ou testes; linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07E aprovado — 21/10/2026

**E DECIDIDO**, seção 8.74 de M07. Resposta: “Manifestação confiável + Mastery própria (recomendado)”. A–E: cinco decisões aprovadas, nenhuma pergunta pendente. Forma conquistada funciona sob requisitos válidos, sem sorteio de falha por baixo domínio; Mastery própria por prática pertinente. Técnicas/versões mantêm aquisição/domínio próprios; Proficiências/Masteries existentes preservadas. Sem requisito circular, compra/cópia de progresso, ganho AFK ou liga/desliga como treino suficiente.

Revisão dirigida não identificou conflito com A–D e a herança examinada. Confiabilidade não significa execução instantânea, imunidade à interrupção, recursos gratuitos ou repertório completo. Valores/práticas concretos e demais contratos operacionais continuam abertos. Próximo: ativação manual e recursos; depois instrumentos/mãos, preparo/reações, encerramento, compatibilidades e ciclo de vida, Letzt Stil e revisão Quincy. Sem nova pesquisa canônica/API, implementação ou testes nesta atualização. M07 em especificação; linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07E proposto — 21/10/2026

**A–D aprovados; E PROPOSTO na seção 12.1 de M07, uma resposta pendente.** Recomenda manifestação básica confiável após conquista, cumpridos os requisitos, e Mastery própria por prática pertinente. Técnicas/versões mantêm aprendizado/domínio próprios; três Proficiências e Masteries existentes preservadas, sem cópia, compra, AFK ou liga/desliga como treino suficiente. Requisitos, interrupção e primeira demonstração continuam válidos. Não aprovar pelo pedido de continuar.

E é adaptação proposta. BL-03 recuperado pelo índice após 403 direto; BL-28 aberto, passagem específica disponível no índice, não na extração direta desta rodada. Limites em M07/seção 4 e base/seção 17. Sem capítulo/episódio integral, novas APIs, implementação ou testes. Próximo após resposta: ativação/recursos, instrumentos/mãos, preparo/reações, encerramento, compatibilidades e ciclo de vida; Letzt Stil/consequências e revisão depois. Fechamentos anteriores preservados; linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07C/D aprovados — 21/10/2026

**C/D DECIDIDOS**, seções 8.72/8.73 de M07, respostas: “Schrift compatível antes de Vollständig (recomendado)” e “Fonte avançada, sem filiação permanente (recomendado)”. A–D: quatro decisões aprovadas, sem perguntas pendentes. Primeiro conquistar Schrift compatível, depois aprender a Vollständig daquele poder; fonte Quincy avançada contextualizada, sem filiação/lealdade permanente obrigatória. B mantém treinamento/demonstração; entrar/sair por si só não concede/apaga aprendizado.

Revisão local não identificou conflito com a herança examinada; registro na seção 15. Ordem e meios de acesso são adaptações, não currículo canônico universal. Fonte, era, missão e aquisição da Schrift continuam exigindo conteúdo/contexto próprios; D não remove M04/Q7 ou o papel de Yhwach por omissão. Privação efetiva de poder é evento distinto de mudar facção e precisa de contrato próprio.

**Próximo:** domínio/confiabilidade da manifestação; depois ativação/recursos, instrumentos e execução/retorno, compatibilidades/ciclo de vida, Letzt Stil/consequências e revisão. Sem nova pesquisa externa nesta atualização; fontes anteriores mantêm limites. Sem implementação/testes. Resurrección A–Q e A/B Quincy preservados; M07 em especificação, linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07C/D propostos — 21/10/2026

**A/B aprovados; C/D PROPOSTOS na seção 12.1 de M07, duas respostas pendentes.** C: conquistar Schrift canônica compatível antes da Vollständig daquele poder, sem conceder a forma automaticamente. D: fonte de ensino Quincy avançado contextualizada, por instrutor ou registro apropriado, sem filiação permanente obrigatória; entrar/sair por si só não ensina/apaga aprendizagem.

São propostas de adaptação. BL-03 relaciona forma/Schrift, mas não comprova ordem universal ou currículo independente; abertura 403, entradas dirigidas pelo índice. BL-28 reconsultado diretamente; limites em M07/seção 4. Não importar capítulo/episódio integral, novas técnicas ou fontes de ensino de lembrança. Fonte, era, quest e aquisição da Schrift precisam de contrato próprio; D não retira o contexto de Q7 nem permite transferência literal por qualquer rival.

Próximo após respostas: domínio/confiabilidade, ativação/recursos e execução; Letzt Stil/consequências e revisão depois. Custos, instrumentos, poderes concretos e conteúdo ainda abertos. Resurrección A–Q e A/B Quincy preservados; sem implementação/testes, linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-07A/B aprovados — 21/10/2026

**A/B DECIDIDOS**, seções 8.70/8.71 de M07, por duas respostas próprias: “Caminhos separados, sem Letzt Stil obrigatório (recomendado)” e “Preparo, aprendizado e demonstração (recomendado)”. Sem perguntas pendentes desse conjunto. Vollständig regular compatível, Letzt Stil separado/opcional; conquista da forma por preparo, aprendizado apropriado e demonstração registrada, integrável à quest sem duplicar prova.

Revisão local não identificou conflito com a herança examinada; registro na seção 15. Fonte/filiação, relação ou ordem de Schrift, identidade exigida, domínio/execução/custos e consequência/recuperação de Letzt Stil continuam abertos. A/B não aprovam autonomia universal de acesso, professor obrigatório, voo ou perda de progresso. Próximo: acesso aos ensinamentos e relação Vollständig/Schrift por pesquisa/propostas próprias. Sem nova pesquisa canônica/API, implementação ou testes nesta atualização; fontes anteriores mantêm limites. Resurrección A–Q preservado; M07 segue EM ESPECIFICAÇÃO, linhagens/artes adiadas. Este registro governa a continuidade; anteriores são históricos.

### Registro anterior — M07-07 Quincy aberto; A/B propostos — 21/10/2026

**M07-07 EM ESPECIFICAÇÃO**, abertura/herança na seção 8.69 de M07. **A/B PROPOSTOS na seção 12.1; duas respostas pendentes:** A — Vollständig como forma avançada regular compatível e Letzt Stil opcional/separado, sem etapa de perda obrigatória para chegar à outra forma; B — preparo de Reishi, aprendizado por fonte apropriada e demonstração registrada de Vollständig, integrável a quest sem duplicar prova.

Não aprovar pelo pedido de continuar. A/B não decidem fonte/filiação, necessidade/ordem de Schrift, identidade exigida, domínio, custo, instrumentos, voo ou consequência/recuperação de Letzt Stil. Schrift continua poder individual com contexto próprio de M04/Q7, não outra transformação nem segunda seleção cumulativa de poder. Não importar custos/reversão de Bankai/Resurrección. M05/M06 superam a compra de técnicas de M04/Q11.

Fontes: BL-16 direto; BL-03/duas entradas pelo índice após 403; BL-23/trecho Uryū pelo índice; BL-28/entrevista Quilge direta. Limites em M07/seção 4 e base/seção 17. Próximo: respostas A/B, acesso aos ensinamentos e relação Vollständig/Schrift; depois execução e Letzt Stil antes da revisão. Resurrección A–Q mantém fechamento estrutural. Sem implementação/testes; linhagens/artes adiadas. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — Q aprovado; Resurrección consolidada estruturalmente — 21/10/2026

**Q DECIDIDO**, seção 8.68 de M07, resposta: “Encerrar forma; retorno manual (recomendado)”. Morte, desconexão, dimensão e reinício encerram preparo/manifestação; condição Arrancar, identidade, conquista, Proficiências e Masteries permanecem. Nova liberação manual paga normalmente; custos/tempos seguem seus contratos. A–Q: dezessete decisões aprovadas, nenhuma pergunta pendente.

**Revisão estrutural concluída**, seção 15 de M07: situações, entregas, responsáveis e condições de retorno. Não identificado conflito estrutural no recorte examinado. Efeitos autônomos/dependentes seguem M06-11W; sem nova votação ou limpeza universal. Arma/inventário cheio, poses/volumes e mundo alterado, custos/curvas, técnicas, prova e apresentação ainda exigem fichas. Não decidir keepInventory, recall ou recuperação de componentes por omissão. Fechamento estrutural não equivale a experiência executável/implementada/testada/aceita.

**Próximo:** abrir formas Quincy por pesquisa própria, distinguindo formas avançadas e poderes individuais antes das propostas; depois Fullbringer e revisão comum de M07. Panorama geral na seção 12.3; M08–M16 ainda não iniciados como documentos próprios. Sem nova pesquisa canônica/API, implementação ou testes ao registrar esta resposta. Linhagens/artes adiadas; Grimmjow continua primeiro conteúdo específico. Este registro governa a continuidade; os anteriores são históricos.

### Registro anterior — M07-06Q proposto; panorama do que falta — 21/10/2026

**A–P aprovados**, dezesseis decisões preservadas. **Q PROPOSTO**, seção 12.1 de M07, uma resposta pendente: encerrar preparo/manifestação na morte, desconexão, mudança de dimensão ou reinício; conservar Arrancar, identidade, conquista e domínio, sem restaurar automaticamente. Nova liberação manual e paga. Não aprovar pelo pedido de continuar.

Efeitos e tempos já aprovados consolidados por herança, sem nova votação: autônomos seguem M06-11W na região ativa; canais/benefícios perdem validade com seu requisito; fases/custos/tempos permanecem. Arma incorporada/retorno/inventário cheio e morte de itens continuam com fichas M11/M16, sem recall, cópia ou keepInventory escolhidos.

**O que falta:** responder Q e revisar Resurrección; depois formas avançadas Quincy, desenvolvimento Fullbringer e revisão comum de M07. M07/seção 12.3 contém panorama de M01–M16 e fichas pendentes; M08–M16 não iniciados como documentos próprios. M05/M06 conceituais ainda precisam de números/fichas. Corrigidas sínteses vigentes que citavam apenas A–J. Fontes BL-19/BL-26 reconsultadas; não definem política de sessão. Sem APIs, implementação ou testes. Linhagens/artes adiadas; Grimmjow permanece primeiro conteúdo específico. Este registro governa a continuidade; os seguintes são históricos.

### Registro anterior — M07-06N aprovado após esclarecimento — 21/10/2026

**N DECIDIDO**, seção 8.67 de M07. Resposta própria: “A — Armadura continua funcionando (recomendado)”. Os quatro slots normais permanecem funcionais durante Resurrección, com encantamentos elegíveis/desgaste legítimo e aparência adaptada/oculta. Transformar não duplica ou repara itens; composição defensiva segue M06-11A/B.

**A–P aprovados**, dezesseis decisões, sem perguntas pendentes desse conjunto. A dúvida anterior de N foi esclarecida e a escolha registrada; não perguntar novamente. Desenho final, cálculos e complementos de ficha não foram aprovados em bloco. **Próximo:** ciclo de vida de preparo/forma/arma, efeitos dependentes e revisão estrutural. Itens/inventário, poses, mundo alterado, valores e apresentação continuam pendentes de ficha. Sem nova pesquisa canônica/API, implementação ou testes. M07 segue em especificação; linhagens/artes adiadas. Este registro governa a continuidade; os seguintes preservam o histórico.

### Registro anterior — M07-06O/P aprovados; N aguarda esclarecimento — 21/10/2026

**O/P DECIDIDOS em 21/10/2026**, seções 8.65/8.66 de M07, por duas respostas próprias:

- O — “Resurrección compatível com estados de Reiatsu (recomendado)”. Sinais/custos próprios, ofensiva válida encerra Supressão; apenas transformar não é ofensiva automática.
- P — “Resurrección → Liberação → ação (recomendado)”. Manutenções realmente coincidentes, tentativa separada de cada estado ainda válido, sem reativar sozinho ou beneficiar estado sem pagamento.

**N ainda PROPOSTO.** Resposta: “Não entendi como funcionariam as duas alternativas”. Explicação com peitoral hipotético e tabela na seção 12.1: A mantém efeitos elegíveis mesmo com visual adaptado/oculto; B suspende peças incompatíveis com o corpo sem apagar o item. Recomendação A mantida, aguardando uma escolha. Não interpretar confusão como aprovação/rejeição nem perguntar novamente O/P.

A–M e O/P somam quinze decisões aprovadas. Próximo: esclarecer/escolher N; depois ciclo de vida, efeitos dependentes e revisão. Complementos de ficha continuam pendentes. Registro sem nova pesquisa canônica/API, implementação ou testes; fontes mantêm datas/limites. Linhagens/artes adiadas. Este registro governa a continuidade; os seguintes preservam o histórico.

### Preparação anterior — M07-06N/O/P propostos — 21/10/2026

A–M aprovados, sem nova votação. **N/O/P PROPOSTOS na seção 12.1 de M07; três respostas pendentes:**

- N: preservar quatro slots normais de armadura funcionais durante Resurrección, com aparência adaptada/oculta conforme perfil, mesmos itens/desgaste e encantamentos elegíveis. É escolha nova neste escopo; M06 não aprovou slots para todo corpo.
- O: coexistência com Natural/Supressão/Liberação; apenas transformar não é ofensiva automática, ataque válido encerra Supressão, sinais/custos permanecem. Efeitos ofensivos contínuos exigem ficha própria.
- P: Resurrección → Liberação → ação nas cobranças coincidentes; falha encerra só o estado sem pagamento e ainda se tenta o outro válido. Ativação inicial segue como ação na conclusão.

BL-26/Arrancar/Hierro e BL-19/Resurrección reconsultados; não fundamentam slots ou gestão de recurso do mod. Depois respostas, ciclo de vida, efeitos dependentes e revisão. Inventário, poses, intervenções do mundo, valores e apresentação exigem ficha. Sem APIs, implementação ou testes; linhagens/artes adiadas. Este registro governa a continuidade; os seguintes preservam o histórico.

### Registro anterior — M07-06K/L/M aprovados — 21/10/2026

**K/L/M DECIDIDOS em 21/10/2026**, por três respostas próprias, seções 8.62–8.64 de M07:

- K — “Chão, ar e água, preservando queda e riscos (recomendado)”. Liberação/retorno nos meios válidos sem apoio obrigatório ou apagar gravidade, trajetória e queda; não concede voo/respiração.
- L — “Guardar conserva a forma com manutenção (recomendado)”. Arma separada no próprio inventário conserva a manifestação paga; dependências de empunhadura ficam suspensas, sem nova liberação ao retomar.
- M — “Perda de posse encerra a manifestação (recomendado)”. Drop/baú/transferência da arma separada encerra dependentes, conserva conquistas, sem transferir poder ou recolher item; recuperar exige nova liberação paga.

A–M, treze decisões aprovadas; nenhuma dessas perguntas pendente. Revisão local não identificou conflito no recorte examinado. Poses/volumes, interferências do mundo, item incorporado/slots/inventário cheio, perda/retorno e ações dependentes ainda precisam de ficha; complementos não aprovados em bloco. **Próximo:** equipamentos restantes, convivência com Reiatsu, pagamentos, ciclo de vida e revisão de Resurrección. Sem nova pesquisa canônica/API, implementação ou testes; fontes mantêm datas/limites. M07 segue em especificação; linhagens/artes adiadas. Este registro governa a continuidade; os seguintes preservam o histórico.

### Preparação anterior — M07-06K/L/M propostos — 21/10/2026

A–J aprovados, sem nova votação. **K/L/M PROPOSTOS na seção 12.1 de M07; três respostas pendentes:**

- K: liberar/encerrar no chão, ar e água quando requisitos válidos, preservando gravidade, queda e riscos, sem apoio/voo/respiração extra.
- L: guardar arma separada no próprio inventário conserva forma paga; suspender dependências de empunhadura. Incorporação não exige espada separada nem deixa cópia utilizável.
- M: perda efetiva da arma separada encerra manifestação/dependentes; conserva conquistas, sem transferir poder, recolher arma ou restaurar forma automaticamente ao recuperá-la.

BL-19/BL-26 reconsultados e BL-14/Tiburón/Gamuza em consulta dirigida. Fontes não comprovam essas adaptações de inventário/meios; limites em M07/seção 4. Slots, inventário cheio, poses e interferências do mundo exigem ficha; depois respostas, equipamentos restantes, compatibilidades/pagamentos e ciclo de vida. Sem APIs, implementação ou testes. Linhagens/artes adiadas. Este registro governa a continuidade; os seguintes preservam o histórico.

### Registro anterior — M07-06H/I/J aprovados, arma/itens e espaço — 21/10/2026

**H/I/J DECIDIDOS em 21/10/2026**, por três respostas próprias, seções 8.59–8.61 de M07:

- H — “Arma conforme o poder; itens compatíveis permitidos (recomendado)”. Representação conforme ficha, sem cópia selada utilizável; ferramentas/blocos compatíveis não encerram a forma, mantendo mãos/fases/manutenção.
- I — “Exigir espaço na posição atual (recomendado)”. Conferência antes e na conclusão, preservando referência dos pés/direção; recusa conserva corpo anterior/conquistas, sem cobrar ativação não concluída ou reposicionar/remover blocos.
- J — “Passagem preserva espaço do corpo-base (recomendado)”. Passagem inclui corpo-base na postura compatível; formas maiores exigem mais espaço. Limita acesso das menores para permitir retorno básico na posição atual.

A–J, dez decisões aprovadas; nenhuma dessas perguntas pendente. Revisão local não identificou conflito no recorte examinado. Complementos de ficha não aprovados em bloco: item incorporado/slots/inventário cheio, poses/meios e alterações forçadas do mundo, posse/perda/morte, armadura e efeitos dependentes continuam abertos. **Próximo: meios, posse/perda, equipamentos restantes, compatibilidades/pagamentos e ciclo de vida**. Sem nova pesquisa canônica/API, implementação ou testes; fontes mantêm datas/limites. M07 segue em especificação, linhagens/artes adiadas. Este registro governa a continuidade; os seguintes preservam o histórico.

### Preparação anterior — M07-06H/I/J propostos, corpo/arma e espaço — 21/10/2026

A–G aprovados, sem nova votação. **H/I/J PROPOSTOS na seção 12.1 de M07**, aguardando três respostas:

- H: arma incorporada/transformada/separada conforme poder, sem cópia selada utilizável; itens compatíveis podem ser usados sem encerrar a forma, mantendo mãos/fases/manutenção.
- I: validar espaço na posição atual antes e ao concluir, preservando pés/direção; recusa sem cobrança da ativação, reposicionamento ou destruição.
- J: volume de passagem incluir o corpo-base na postura compatível; limita passagens de formas menores para viabilizar retorno na posição atual sem teleporte/manutenção gratuita.

Perfis funcionais por etapa são herança de M06-08C/F/G; não perguntar novamente nem selecionar artes. BL-14/Tiburón/Gamuza consultado; BL-19/BL-26 reconsultados; BL-05 retornou 403. Fontes/limites em M07/seção 4. H/I/J são adaptações, com alternativas e consequências explícitas. Inventário, poses/meios, intervenções do mundo, ações dependentes, posse/perda, armadura, compatibilidades/pagamentos e ciclo de vida continuam para fichas/decisões. Próximo: respostas; depois esses contratos restantes. Sem APIs, implementação ou testes. Linhagens/artes adiadas. Este registro governa a continuidade; os seguintes preservam o histórico.

### Registro anterior — M07-06E/F/G aprovados, preparo e encerramento — 21/10/2026

**E/F/G DECIDIDOS em 21/10/2026**, por três respostas próprias, seções 8.56–8.58 de M07:

- E — “Preparo cancelável para reagir (recomendado)”. Caminhar devagar/olhar, cancelar para reação disponível sem recuperação extra ou custo da ativação; pedido recusado mantém preparo válido.
- F — “Encerrar manualmente e voltar ao estado-base (recomendado)”. Encerramento voluntário quando a fase permitir; volta ao estado-base Arrancar, também após falta de manutenção. Conquista/Mastery preservadas, sem refill; nova liberação paga normalmente.
- G — “Transições sem recuperação adicional (recomendado)”. Sem espera/custo adicional só por concluir ou encerrar voluntariamente; ataques, requisitos e tempos anteriores permanecem.

A–G, sete decisões aprovadas; nenhuma dessas perguntas pendente. Não identificado conflito no recorte de revisão local. Complementos de ficha não foram aprovados em bloco. Corpo/arma, espaço/meios, ações dependentes, posse/equipamentos, compatibilidades/pagamentos e ciclo de vida ainda exigem detalhamento. **Próximo: corpo/arma e espaço da mudança/retorno**. Linhagens/artes adiadas. Registro das respostas sem nova pesquisa canônica/API, implementação ou testes de Minecraft; fontes anteriores mantêm datas/limites. Este registro governa a continuidade; os seguintes preservam o histórico.

### Preparação anterior — M07-06E/F/G propostos, preparo e encerramento — 21/10/2026

**A–D aprovados; E/F/G PROPOSTOS na seção 12.1 de M07**, aguardando três respostas próprias:

- E: preparo breve, caminhar devagar/olhar e cancelar para reação disponível, sem recuperação extra só pelo cancelamento; pedido recusado conserva preparo válido.
- F: encerramento manual quando a fase permitir e retorno ao estado-base Arrancar, também após falta de manutenção; conquistas preservadas e nova liberação paga normalmente.
- G: conclusão/encerramento voluntário sem recuperação adicional apenas pela mudança, preservando custos e tempos anteriores.

São propostas de adaptação. Espaço na mudança/retorno, corpo/arma, meios, ações dependentes, posse/equipamento, compatibilidades/pagamentos e ciclo de vida permanecem abertos. Não manter vantagens sem manutenção nem aprovar retorno através de parede por omissão. Não herdar políticas Shinigami automaticamente. BL-19/BL-26 reconsultados, sem capítulo/episódio integral; limites em M07/seção 4. Sem APIs, implementação ou testes de Minecraft. Próximo: respostas, depois corpo/arma e espaço. Linhagens/artes adiadas. Este registro governa a continuidade; os seguintes preservam o histórico.

### Registro anterior — M07-06C/D aprovados, ativação e recursos — 21/10/2026

**C/D DECIDIDOS**, por duas respostas próprias em 21/10/2026, seções 8.54/8.55 de M07:

- C — “Ativação manual com arma própria (recomendado)”. Arma própria compatível empunhada para liberar; pedido manual em combate/exploração, independente da interface. Fechar a Combat Hotbar não encerra por si só; fases e requisitos permanecem.
- D — “Ativação + manutenção de Energia (recomendado)”. Energia uma vez na conclusão e manutenção ativa, sem prazo universal. Cancelamento anterior não cobra ativação; técnicas têm custos próprios e falta de manutenção encerra a manifestação.

A–D, quatro decisões aprovadas, sem perguntas pendentes. A condição Arrancar permanece; empunhadura/representação da arma após concluir, números, retorno e prioridades de cobrança ainda exigem decisões/fichas. Não identificado conflito no recorte examinado; não aprovar complementos candidatos em bloco.

**Próximo:** preparo/interrupção e conclusão/encerramento; depois corpo/arma, posse/equipamentos, compatibilidades/pagamentos e ciclo de vida. Linhagens/artes adiadas. Registro das respostas e revisão local sem nova pesquisa canônica/API, implementação ou testes. Este registro governa a continuidade; o anterior preserva a preparação.

### Preparação anterior — M07-06C/D propostos, ativação e recursos — 21/10/2026

A/B aprovados, sem nova votação. **C/D PROPOSTOS na seção 12.1 de M07**, aguardando duas respostas:

- **C:** ativação manual com arma própria compatível empunhada para liberar, em combate/exploração e independente de Combat Mode; fechar a interface não encerra por si só. Representação da arma e empunhadura depois da conclusão ainda precisam de ficha.
- **D:** Energia uma vez na conclusão e manutenção enquanto ativa, sem prazo fixo universal; cancelamento anterior não cobra ativação, falta de manutenção encerra a manifestação. Retorno, valores e prioridade de cobranças ainda pendem.

A proposta não importa custos de Bankai nem empunhadura permanente de Shikai; fases e tempos anteriores permanecem. Reconsultados BL-19/BL-26; BL-03 recuperado como texto indexado. Esses trechos não determinam custo universal; limites em M07/seção 4. Sem capítulo/episódio integral, API, implementação ou testes. Próximo: respostas e preparo/transições, corpo/arma, encerramento, compatibilidades/pagamentos e ciclo de vida. Linhagens/artes adiadas. Este registro governa a continuidade; o anterior preserva as aprovações.

### Registro anterior — M07-06A/B aprovados, primeira Resurrección — 21/10/2026

**A/B DECIDIDOS**, por duas respostas explícitas em 21/10/2026, seções 8.52/8.53 de M07:

- A — “Preparo do próprio poder + demonstração (recomendado)”. Condição Arrancar, arma própria, identidade vinculada e demonstração prática; pode integrar a quest do poder sem outra prova equivalente. Conquista registrada, sem repetir a cada uso.
- B — “Manifestação confiável + Mastery própria (recomendado)”. Após conquistar, manifestação básica confiável quando válida; Mastery própria por prática. Técnicas distintas e versões posteriores mantêm aquisição/domínio próprios.

Nenhuma dessas duas perguntas pendente. Revisão dirigida preserva H6/RAC-20/RAC-23, acesso compatível Gillian, desenvolvimento anterior e M07-05. Preparo não pode exigir uma técnica exclusiva da forma ou Mastery ainda inacessível. Detalhes candidatos não foram aprovados em bloco. M07-06 continua em especificação.

**Próximo:** manifestação, arma/empunhadura, Combat Mode e recursos; depois corpo/repertório, encerramento/posse/perda, compatibilidades e ciclo de vida. Sem herdar políticas Shinigami ou conceder Segunda Etapa. Conteúdo e fichas permanecem pendentes; linhagens/artes adiadas. Pesquisa da abertura mantém datas/limites; registro das respostas sem pesquisa adicional, APIs, implementação ou testes. Este registro governa a continuidade; o seguinte preserva a preparação.

### Preparação anterior — M07-06A/B propostos, primeira Resurrección — 21/10/2026

**A/B PROPOSTOS**, seção 12.1 de M07, aguardando duas respostas próprias:

- **A:** condição Arrancar, arma própria, identidade canônica vinculada, preparo no próprio poder e demonstração prática de liberação; pode integrar a quest do poder sem duplicar prova. Conquista registrada, sem repetir a cada uso.
- **B:** manifestação básica confiável após conquista e requisitos cumpridos, sem falha aleatória por ativação; Mastery própria da Resurrección e aprendizado/domínio separados para técnicas distintas e versões posteriores.

M07-05A–J mantém seu fechamento estrutural. Preservar H6/RAC-20/RAC-23, acesso compatível Gillian e desenvolvimento anterior; não exigir batalha/materialização de espírito Shinigami, facção ou posto como regra universal. A/B são adaptações propostas, sem resposta ainda. Depois tratar manifestação, recursos, corpo/arma, encerramento e ciclo de vida; linhagens/artes adiadas.

Pesquisa de 21/10: BL-19/帰刃 e BL-26/Arrancar reconsultados; BL-03 teve abertura direta 403, com recuperação indexada da entrada刀剣解放. Sem capítulo/episódio integral; fontes e limites em M07/seção 4. Sem APIs, implementação ou testes de Minecraft. Este registro governa a continuidade; o anterior preserva a aprovação da Arrancarização.

### Registro anterior — M07-05H/I/J aprovados e revisão de Arrancarização — 21/10/2026

**H/I/J DECIDIDOS em 21/10/2026**, por três respostas próprias, seções 8.48–8.50 de M07:

- H — “Preparo deliberado e fora de combate (recomendado)”. Preparo breve parado/apoiado; pode integrar o acontecimento da máscara, sem segundo ritual. Reações disponíveis cancelam antes de começar, sem recuperação extra só pelo cancelamento.
- I — “Exigir espaço na posição atual (recomendado)”. Pés/direção mantidos, espaço conferido antes e ao concluir; recusa conserva corpo anterior/conquistas, sem reposicionar ou remover blocos.
- J — “Encerrar preparo; preservar conquistas (recomendado)”. Morte/logout/dimensão/reinício encerram preparo incompleto; condição Arrancar já concluída permanece. Sem retomada automática, refill ou reset de tempos.

**M07-05A–J consolidado estruturalmente**, dez decisões sem perguntas pendentes. Revisão/tabela de situações, entregas e condições de retorno na seção 15 de M07. Não identificado conflito estrutural no recorte examinado; fichas, meios/apoios, efeitos sustentados, perfis, inventário cheio/arma e viabilidade avançada continuam pendentes. Aprovações não concluem implementação ou gameplay.

**Próximo: M07-06 — Resurrección**, começando por pesquisa pertinente, conquista/domínio e relação com arma/identidade. Ainda não aberto; não copiar políticas de Shikai/Bankai ou conceder Segunda Etapa. M07 inteiro continua em especificação. Linhagens/artes adiadas. Registro/revisão sem pesquisa canônica/API nova ou testes de Minecraft. Este registro governa a continuidade; os seguintes preservam a preparação histórica.

### Preparação anterior — M07-05H/I/J propostos — 21/10/2026

A–G continuam aprovados, sem nova votação. **H/I/J PROPOSTOS**, seção 12.1 de M07, aguardando três respostas:

- **H:** escolha deliberada e preparo breve parado/apoiado fora de Combat State; movimento/combate/perda de apoio ou reação disponível cancelam, sem recuperação extra só pelo cancelamento. O preparo pode integrar o acontecimento da máscara, sem segundo ritual obrigatório.
- **I:** validar o novo corpo na posição atual no início e na conclusão; espaço inválido conserva corpo anterior/marcos, sem reposicionar ou alterar blocos.
- **J:** interrupção conserva estágio e marcos concluídos; morte/logout/dimensão/reinício encerram preparo incompleto sem retomada automática. Condição Arrancar já concluída permanece; arma/persistência sem duplicação ainda exigem ficha.

São propostas próprias, sem herança automática de evolução Hollow ou Shinigami. F/G mantêm trajetórias e ausência de taxa adicional/refill/reset. Corrigidas sínteses antigas do mapa e abertura do bloco para A–G. Sem nova pesquisa canônica/API, implementação ou testes. Próximo: registrar respostas e revisar Arrancarização; depois Resurrección. Fichas, inventário, perfis, efeitos sustentados e conteúdo continuam pendentes; linhagens/artes adiadas. Este registro governa a continuidade; os seguintes são históricos.

### Registro anterior — M07-05F/G aprovados — 21/10/2026

**F/G DECIDIDOS** por duas respostas explícitas, seções 8.46/8.47 de M07:

- **F:** “Trajetórias e preparo, sem bônus fixo por origem (recomendado)”. Diferenciar preparo acumulado e momento de acesso Arrancar; crescimento nas capacidades compatíveis, sem bônus fixo, penalidade de treino ou teto menor de atributos apenas pela origem. Não iguala personagens nem libera toda linhagem. Viabilidade avançada ainda exige fichas/M15.
- **G:** “Sem taxa adicional pela Arrancarização (recomendado)”. Sem taxa adicional de Energia/Stamina para a passagem nem manutenção da condição. Desafios/técnicas mantêm gastos próprios; não há refill ou reset de tempos.

A–G, sete decisões aprovadas; nenhuma dessas perguntas aguarda resposta. D continua aprovado após correção. Próximo: execução da mudança, espaço/apoio, interrupção/falha e ciclo de vida; depois Resurrección. Fichas, capacidades concretas, inventário/arma e conversão de máximos continuam pendentes. H6/H8/H9 e M05 preservados. Registro de respostas sem pesquisa canônica/API nova, implementação ou testes de Minecraft. Linhagens/artes adiadas. Esta rodada governa a continuidade; o registro de preparação abaixo permanece histórico.

### Preparação anterior — M07-05F/G propostos — 21/10/2026

A–E permanecem aprovados, incluindo a correção de D. **F/G aguardam duas respostas próprias**, na seção 12.1 de M07:

- **F, recomendado:** trajetórias diferenciadas por preparo acumulado e momento de acesso Arrancar, com crescimento posterior nas capacidades compatíveis; sem bônus fixo, penalidade de treino ou teto menor de atributos apenas pelo rótulo de origem. Alternativa: benefícios exclusivos permanentes por origem, ainda sem catálogo. Não iguala personagens nem libera toda linhagem; viabilidade avançada precisa de fichas/M15.
- **G, recomendado:** Arrancarização permanente sem taxa adicional de Energia/Stamina ou manutenção da condição; desafios/técnicas mantêm custos próprios, sem refill/reset. Alternativa: Energia uma vez ao concluir.

Reconsultados BL-26/estágios e capacidade anterior, BL-10/Aaroniero e BL-27/Arrancar; limites em M07, seção 4. Sem capítulos/episódios integrais, números ou testes de Minecraft. H6/H8/H9 e M05 preservados. Depois das respostas, tratar execução/espaço/apoio, falha/interrupção/ciclo de vida; depois Resurrección. Linhagens/artes adiadas. Esta rodada governa o ponto atual; o registro anterior de A–E abaixo é preservado.

### Registro anterior — M07-05A–E, Arrancarização — 21/10/2026

**A/B/C DECIDIDOS por três respostas próprias**, seções 8.41–8.43 de M07:

- **A:** “Preparo no estágio + acontecimento próprio (recomendado)”. Preparo pertinente como Gillian/Adjuchas/Vasto Lorde e acontecimento de ruptura da máscara; sem mudança automática por número e sem repetir marco integrado à quest compatível.
- **B:** “Via pessoal sem facção ou Hōgyoku obrigatórios (recomendado)”. Descoberta ou orientação e prática; sem exigir Aizen/facção/Hōgyoku. Via jogável é adaptação expressa; métodos assistidos ainda não foram aprovados.
- **C:** “Arma própria ao concluir Arrancarização (recomendado)”. Zanpakutō própria na conclusão, sem Asauchi ou espada alheia; não entrega Resurrección/técnicas/Mastery. Identidade antes ou depois conforme rota compatível.

**D/E DECIDIDOS em 21/10/2026**, seções 8.44/8.45 de M07. D: o pedido inicial de adiar foi substituído por “Eu entendi errado a primeira, concordo com a recomendação”; aprovada **base humanoide nas três origens**, sem fixar medidas/traços/diferenças funcionais. E: “Preservar domínio e usar o que for compatível (recomendado)”; conservar Proficiências/Masteries e técnicas já aprendidas compatíveis, sem reaprendizado obrigatório. Incompatíveis ficam indisponíveis sem apagar histórico; novas exigem aprendizado; regeneração não é garantida.

A–E, cinco decisões, sem perguntas pendentes. Próximo: diferenças entre origens e execução/custos/falha/ciclo de vida; então Resurrección. Inventário cheio, posse/perda/reposição da arma, perfis/matriz de capacidades, conteúdo/fontes de ensino e valores continuam pendentes. Permanecem H6/H8/H9 e o adiamento de linhagens/artes. Atualização de D/E sem pesquisa canônica/API nova; histórico da preparação abaixo preservado.

Pesquisa de 21/10/2026: BL-27/Shonen Jump, BL-26/Arrancar/Vasto Lorde e BL-10/Aaroniero, além da pesquisa anterior de BL-11/Hōgyoku. D é adaptação corporal explícita; não afirmar anatomia humana idêntica para todo Arrancar canônico. Sem capítulos/episódios integrais; BL-03 teve abertura direta 403 e apenas texto indexado recuperado, sem uso decisivo. A seção 4 de M07 registra limites. Sem implementação/teste de Minecraft. Esta rodada governa a continuidade; encaminhamentos anteriores abaixo são históricos.

### Bloco consolidado — evolução Hollow; continuidade para Arrancarização, 21/10/2026

**M07-04A/B/C DECIDIDOS em 21/10/2026**, seções 8.27–8.29: A — “Devorar por interação após derrotar (recomendado)”; B — “Presas elegíveis e participação efetiva (recomendado)”; C — “Iniciar evolução deliberadamente (recomendado)”. Nenhuma pergunta A/B/C pendente. Complementos de representação/itens, critérios objetivos e distribuição de oportunidades ainda precisam de ficha; G–M complementam atualmente falha, execução básica e ciclo de vida.

**M07-04D/E DECIDIDOS em 21/10/2026**, seções 8.30/8.31: D — “Formação por agregação de Hollows (recomendado)”; E — “Conquista racial sem linhagem obrigatória (recomendado)”. A–E aprovados; nenhuma pergunta dessas decisões pendente. Gillian forma-se por agregação de NPCs, com marco registrado e mudança deliberada de C; Adjuchas exige desenvolvimento/prova racial, que pode integrar quest de linhagem sem torná-la obrigatória ou duplicar prova.

**M07-04F/G DECIDIDOS em 21/10/2026**, seções 8.32/8.33: F — “Marcos registrados + prova final (recomendado)”; G — “Falha limitada à tentativa, sem regressão racial (recomendado)”. A–G aprovados; nenhuma pergunta dessas decisões pendente. Estágio consolidado, predação e marcos concluídos permanecem após dano/morte comum/falha; parte ainda em andamento pode ser repetida, e gastos legítimos permanecem. Não é devolução de itens, restauração de combate ou recompensa duplicada.

**M07-04H/I/J DECIDIDOS em 21/10/2026**, seções 8.34–8.36: H — “Parado, com cancelamento para reagir (recomendado)”; I — “Sem custo adicional de Energia ou Stamina (recomendado)”; J — “Exigir espaço na posição atual (recomendado)”. A–J, dez decisões, sem pergunta pendente. Devoração básica dentro/fora de combate, parada/cancelável sem recuperação extra; sem custo/refill de recurso; evolução recusa espaço insuficiente sem reposicionar ou apagar marcos.

**M07-04K/L/M DECIDIDOS em 21/10/2026**, seções 8.37–8.39: K — “Evolução sem taxa adicional de recurso (recomendado)”; L — “Preparo apoiado e fora de combate (recomendado)”; M — “Encerrar preparos; preservar conquistas (recomendado)”. Evolução sem taxa/manutenção adicional nem refill; preparo breve parado, apoiado e fora de Combat State; morte/logout/dimensão/reinício encerram devoração/evolução incompletas sem apagar conquistas ou retomar automaticamente. A–M, treze decisões, sem perguntas pendentes.

**Evolução Hollow consolidada estruturalmente:** revisão final/tabela de situações, entregas com responsáveis e condições de retorno na seção 15 de M07. Não identificado conflito estrutural no recorte examinado. Meios/apoios concretos, perfis, disponibilidade/distribuição da predação, spawn/retorno, fichas, valores, presença/efeitos sustentados e conteúdo continuam pendentes. Política de retorno compatível com corpos grandes é condição da futura entrega jogável, sem perda de estágio ou forma compacta por omissão. H6/H8/H9 e agência Gillian permanecem; nenhum mestre/prova/arte final escolhido. **Próximo: M07-05 — Arrancarização**, pesquisar e definir requisitos/processo/consequências entre origens; depois Resurrección. Grimmjow permanece primeiro caso futuro de conteúdo.

Registro de K/L/M e fechamento estrutural sem pesquisa canônica/API nova; fontes anteriores mantêm datas e limites. Sem implementação/teste de gameplay. Os registros seguintes preservam a trajetória anterior; esta rodada governa o ponto atual.

**Trabalhar [M07 — Transformações e Domínio](07-transformacoes-e-dominio.md), `EM ESPECIFICAÇÃO`.** Documento aberto em 21/10/2026, seguindo o template de 15 seções. M07-01 consolida evolução/condição adquirida, conquista e manifestação sem reabrir aprovações raciais. Primeiro bloco Shinigami, M07-02A–D, aprovado por quatro respostas explícitas:

- **A:** “Vínculo com o espírito + prova de liberação (recomendado)”. Zanpakutō vinculada, relação com o espírito e prova prática; conquista registrada, sem repetir a prova a cada uso ou despertar só por números/abates.
- **B:** “Shikai manual e independente da interface (recomendado)”. Liberar/encerrar com arma compatível empunhada, dentro/fora do modo; fechar a hotbar não sela por si só. Mãos, fases e requisitos permanecem.
- **C:** “Custo ao liberar, sem manutenção básica (recomendado)”. Energia uma vez na conclusão da liberação; sem drenagem apenas pela manifestação básica. Selar não cobra/reembolsa; nova liberação paga de novo. Técnicas/efeitos sustentados têm custos próprios.
- **D:** “Manifestação confiável + domínio progressivo (recomendado)”. Sem chance aleatória de falha na manifestação básica válida; Mastery melhora execução/eficiência e técnicas distintas preservam aprendizado/domínio próprios. Sem Bankai automático ou treino AFK/liga-desliga.

**A–D sem pergunta pendente. E/F/G aprovados depois por três respostas próprias, em 21/10/2026:**

- **E:** “Preparo breve cancelável para reagir (recomendado)”. Caminhada lenta e olhar durante liberação; ataque/guarda/Dodge/Shunpo disponíveis cancelam antes de começar, sem recuperação extra só pelo cancelamento. Pedido inválido preserva preparo válido; Energia na conclusão.
- **F:** “Conservar liberação; exigir empunhadura para usar (recomendado)”. Guardar na própria posse conserva Shikai básico; técnicas/bônus dependentes da empunhadura indisponíveis. Retomar a mesma arma não paga nova liberação, preservando fases/tempos.
- **G:** “Encerrar manifestação ao perder a posse (recomendado)”. Drop/baú/transferência encerram manifestação/capacidades dependentes; vínculo e domínio permanecem. Recuperação física não restaura liberação nem transfere poder a outra pessoa; efeitos emitidos seguem M06-11W.

**H/I/J aprovados em 21/10/2026 por três respostas próprias**, seções 8.9–8.11 de M07:

- **H:** “Transição sem recuperação adicional (recomendado)”. Conclusão libera ações disponíveis; selamento direto quando a fase permitir. Sem taxa/recuperação extra ao selar ou reset de tempos/cancelamento de ataque.
- **I:** “Encerrar Shikai; retorno manual (recomendado)”. Morte/logout/dimensão/reinício encerram manifestação, preservando vínculo/domínio. Retorno não restaura; nova liberação paga normalmente, sem estorno anterior.
- **J:** “Shikai compatível com os estados de Reiatsu (recomendado)”. Shikai básico com Natural/Supressão/Liberação. Manifestar apenas a arma não é ataque automático; ofensiva válida encerra Supressão. Aparência/sinal e manutenção próprios permanecem.

**A–J sem perguntas pendentes. Primeiro Shikai consolidado estruturalmente**, com revisão/tabela de situações e responsáveis ao final de M07. Não declarar fichas executáveis, números, prova/conteúdo final, implementação ou gameplay aceito. **Próximo: M07-03 — Bankai**, começando por acesso/conquista e domínio, depois custos/ativação/transições. Não herdar regras de Shikai automaticamente para Bankai ou todas as raças.

Reconsultados BL-03/11/12/17 em 21/10/2026 nas entradas indicadas no módulo: referências oficiais do anime, interpretadas do japonês, sem capítulos/episódios integrais. Revisão dirigida não identificou conflito com os contratos examinados de M04/M05/M06. Não houve implementação ou teste de gameplay; próximas raças, linhagens e artes ainda não foram detalhadas.

### Bloco consolidado — Bankai; continuidade para Hollow, 21/10/2026

**M07-03A/B DECIDIDOS em 21/10/2026**, por duas respostas explícitas: A — “Relação avançada + prova com o espírito (recomendado)”; B — “Manifestação confiável + domínio próprio (recomendado)”. Seções 8.12/8.13 de M07. Conquista pessoal após preparo em Shikai, relação/materialização/prova; manifestação básica confiável e Mastery própria, sem domínio máximo universal de Shikai, cargo obrigatório ou todas as técnicas concedidas. A/B sem pergunta pendente.

**M07-03C/D DECIDIDOS em 21/10/2026**, por duas respostas explícitas: C — “Bankai manual e direto (recomendado)”; D — “Ativação + manutenção de Energia (recomendado)”. Seções 8.14/8.15 de M07. Ativação manual com arma compatível empunhada a partir de selado/Shikai, dentro/fora da interface; Energia na conclusão + sustentação, sem prazo fixo universal. A–D sem perguntas pendentes.

**M07-03E/F DECIDIDOS em 21/10/2026**, por duas respostas explícitas: E — “Preparo cancelável para reagir (recomendado)”; F — “Retorno a Shikai sem nova cobrança (recomendado)”. Seções 8.16/8.17. Preparo com reação válida cancelando sem taxa; tentativa cancelada conserva somente a forma anterior válida. Saída voluntária escolhe Shikai/selado; falta de manutenção retorna a Shikai quando válido, sem novo preparo/custo. Bankai A–F sem perguntas pendentes.

**M07-03G/H/I DECIDIDOS em 21/10/2026**, por três respostas explícitas: G — “Transições sem recuperação adicional (recomendado)”; H — “Conservar Bankai com manutenção ativa (recomendado)”; I — “Encerrar Bankai ao perder posse (recomendado)”. Seções 8.18–8.20. Conclusão/encerramento preservam fases/tempos; guardar na própria posse conserva Bankai e cobrança, suspendendo requisitos de empunhadura; perda de posse encerra sem restaurar Shikai ou transferir poder. A–I sem perguntas pendentes.

**M07-03J/K/L DECIDIDOS em 21/10/2026**, por três respostas explícitas: J — “Bankai compatível com os estados de Reiatsu (recomendado)”; K — “Bankai → Liberação → ação (recomendado)”; L — “Encerrar manifestações; retorno manual (recomendado)”. Seções 8.21–8.23. Presença mantém sinais/custos; Bankai tem prioridade de manutenção antes de Liberação e ação; morte/logout/dimensão/reinício encerram Bankai/preparo sem restaurar Shikai. A–L sem perguntas pendentes.

**M07-03M/N DECIDIDOS em 21/10/2026**, por duas respostas explícitas: M — “Quebra por condições específicas (recomendado)”; N — “Dano persistente com recuperação específica (recomendado)”. Seções 8.24/8.25. Quebra estrutural exige causa/ficha própria; não resulta automaticamente de dano comum, Postura, Energia zero ou morte. Limitação persiste até recuperação específica disponível, sem apagar vínculo/conquista/Mastery. É adaptação do mod, sem alegar reparabilidade canônica universal.

**Bankai consolidado estruturalmente:** M07-03A–N, 14 decisões; nenhuma pergunta pendente. Fechamento final de M07 registra resultados, critérios futuros, entregas por responsável e condições de retorno. Não identificado conflito estrutural no conjunto examinado. Fichas, valores, prova e recuperação concretas, controle/corpo/espaço, itens e realização técnica ainda impedem declarar qualquer Bankai completamente executável ou aceito.

**Encaminhamento atendido na abertura acima: M07-04 — evolução Hollow**, começando por marcos/condições da evolução permanente; depois Arrancarização e Resurrección. A–M aprovados e consolidados depois; a rodada vigente acima encaminha para M07-05 — Arrancarização. Preservar H6/H9, acesso Arrancar em Gillian/Adjuchas/Vasto Lorde e estabilidade sem punição offline; não reabrir aprovações por pendência histórica. Sistemas gerais primeiro; Grimmjow e artes continuam adiados. M07 permanece `EM ESPECIFICAÇÃO`.

Pesquisa de quebra anterior em M07/seção 4 e base: BL-03/Bankai destruído e BL-24/TYBW 08, com limites registrados. Sem pesquisa nova no registro de M/N; sem implementação ou teste de Minecraft. A sinopse 13 inacessível não foi usada. Regras aprovadas, especificação executável, implementação, teste e aceite continuam estados separados.

### Passagem de M06 para M07 — histórico do fechamento

**M06 — Habilidades e Combate está `ESPECIFICADO CONCEITUALMENTE`.** Os quatro caminhos raciais e a integração conceitual foram consolidados; [seção 48 do módulo](06-habilidades-e-combate.md#48-fechamento-conceitual-de-m06--21102026) registra escopo, pendências e passagem a M07. Isso não declara fichas executáveis, implementação ou gameplay aceito. Encaminhamentos anteriores abaixo são históricos; a conclusão final e a tabela vigente governam a continuidade.

### Histórico da integração — M06-11

Primeiro confronto documental: formação/Proficiência/Mastery, aquisição, recursos, vínculos/mãos, fases/reação, movimento, Postura/controle, percepção, modos e persistência. Não foi identificado conflito que exija revogar as aprovações raciais examinadas. Isso não é auditoria integral de todo código, catálogo ou cânone.

Correções de leitura: nota de superação junto a M06-02Y, lista inicial incluindo fichas Shinigami pendentes, sínteses de continuidade e redação do mapa sobre pontos/repertórios alinhadas às revisões já aprovadas. Histórico preservado, sem nova decisão de produto nessas correções.

**A/B/C aprovados por três respostas explícitas em 20/10/2026; registrados em M06-11, seções 5–7, e revisados na seção 10:**

- **M06-11A — DECIDIDO:** armadura, encantamentos/efeitos compatíveis e Resistência úteis contra ataques diretos físicos/espirituais; exceções por componente/técnica, sem bypass total pelo rótulo espiritual.
- **M06-11B — DECIDIDO:** reduções independentes sucessivas sobre o dano restante, com limite conjunto a calibrar; Postura separada, sem repetir a mesma proteção. Ordem técnica completa e números permanecem pendentes.
- **M06-11C — DECIDIDO:** Vene mitiga impacto elegível, sem proteção adicional automática contra queimadura/veneno residual; emissão/área com novos impactos conserva classificação própria. Não resolve toda cobertura de Hierro/Resistência por associação.

**D/E também aprovados por duas respostas próprias, na mesma data; seções 12–13 e revisão na 15:**

- **M06-11D — DECIDIDO:** “Horizontal no chão, ar e água (recomendado)”. Shunpo/Sonído básicos horizontais, Stamina no início aceito, colisão/gravidade/queda preservadas. Sem voo/apoio/respiração concedidos; desempenho e domínio próprios.
- **M06-11E — DECIDIDO:** “Parar para reagir, preservando custos e tempos (recomendado)”. Ação disponível encerra impulso sem recuperação extra só pela parada; pedido inválido preserva movimento ainda válido. Não elimina tempos anteriores nem a recuperação de cancelamento do Cero.

As respostas A/B/C exatas estão em cada decisão: “Proteções compatíveis; exceções por técnica (recomendado)”; “Reduções sucessivas com limite conjunto (recomendado)”; “Vene no impacto; condições separadas (recomendado)”. Não houve aprovação por associação. Nenhuma pergunta A–E está pendente.

Pesquisa de apoio anterior: MC-11, notas oficiais Java 1.19.4, Damage Types; build/toolchain e CombatEvents.java. Na preparação D/E, reconsultado BL-03, entradas Hirenkyaku/Arrancar: sustenta identidade de movimento rápido, sem fixar meios, controles ou custos do mod. Textos japoneses interpretados, sem capítulos/episódios integrais. Não houve alteração de runtime, teste de Minecraft ou aprovação de implementação.

**Registro de continuidade das rodadas:** Percepção, Supressão, Liberação, alcance/alvos, concorrência e ciclo de vida foram trabalhados nas etapas abaixo. X/Y e a revisão final encerram o recorte conceitual; a próxima abertura é M07. Fichas e dependências têm responsáveis na seção 48 de M06.

### Percepção Espiritual F/G/H aprovada e revisada — 20/10/2026

**DECIDIDOS por três respostas próprias**, em M06-11, seções 16–20; revisão na seção 21:

- **F:** percepção natural sem custo e leitura breve aprendida com a mesma Mastery; pequeno custo de Energia na conclusão válida, mesmo sem detecção; cancelamento anterior sem custo/resultado.
- **G:** leitura aproximada através de obstáculos, conforme domínio, sinal e Supressão; assinatura familiar pode ser reconhecida, sem localização/identidade/poder total exatos ou rastreamento contínuo.
- **H:** caminhada lenta e reação disponível cancelam a concentração; pedido indisponível mantém leitura ainda válida. Preservar tempos/custos anteriores e permissões da ação de origem.

Respostas exatas: F — “Percepção natural + leitura breve aprendida (recomendado)”; G — “Leitura aproximada através de obstáculos (recomendado)”; H — “Caminhar e cancelar para reagir (recomendado)”. Alternativas não escolhidas preservadas no M06; nenhuma pergunta F/G/H pendente. Pesquisa mantém aquisição racial e Mastery comum; nenhuma nova Proficiência sensorial. Reconsultado BL-11/Pesquisa e consultado BL-19/Reiraku, resumos oficiais japoneses interpretados, sem capítulos/episódios integrais. Controles/custos/informação são adaptações aprovadas do mod, não regras canônicas demonstradas. Sem alteração de gameplay ou teste novo.

**Encaminhamento após F/G/H, já trabalhado abaixo:** Supressão e oposição à percepção; depois Liberação e estados/ciclo de vida. Revisão na seção 21 não identificou conflito que exija revogar F/G/H; números, fichas e implementação continuam pendentes. Não voltar a votar A–H; M06 continua em especificação. O detalhamento das formas/linhagens e as artes seguem adiados.

### Supressão de Reiatsu I/J/K aprovada e revisada — 20/10/2026

**DECIDIDOS por três respostas próprias**, em M06-11, seções 23–25; critérios na 26 e revisão na 27:

- **I:** Supressão aprendida/manual, sem custo inicial ou manutenção de Energia; Controle/Mastery e vazamento determinam eficácia, sem invisibilidade ou redução de atributos.
- **J:** deslocamento, Dodge, mobilidades, guarda básica e leitura espiritual compatíveis preservam o estado; início ofensivo aceito volta a Natural, sem retorno automático. Pedido recusado preserva ocultação válida; preparar Cero/arco já conta como início ofensivo.
- **K:** oposição gradual pela condição real, domínio, Controle, sinal, distância e familiaridade; repetir nas mesmas condições não cria nova chance aleatória. Resultados individuais permanecem dentro dos limites de G.

Respostas exatas: I — “Supressão manual sem consumo de Energia (recomendado)”; J — “Ofensiva encerra Supressão; retorno manual (recomendado)”; K — “Resultado gradual conforme as condições (recomendado)”. Alternativas não escolhidas preservadas no M06; nenhuma pergunta I/J/K pendente. BL-20 distingue restrição por selo de ocultação voluntária; BL-21 complementa familiaridade/variação de presença, sem comprovar custo ou regra de rompimento. Resumos oficiais interpretados, sem episódios/capítulos integrais. Não houve alteração de gameplay ou teste.

**Encaminhamento após I/J/K, já trabalhado abaixo:** Liberação e depois estados/concorrência/ciclo de vida. Revisão da seção 27 não identificou conflito que exija revogar F–K. A–K não têm perguntas pendentes. Não presumir compatibilidade de todos os efeitos sustentados, retorno automático ou fórmula numérica de oposição.

### Liberação de Reiatsu L/M/N aprovada e revisada — 20/10/2026

Três respostas explícitas, registradas em M06-11, seções 29–31; revisão na 32:

- **L:** “Liberação manual com manutenção de Energia (recomendado)”. Aprendida, manual, sem taxa adicional de ativação; falta de manutenção volta a Natural, sem retorno automático. Não cria poder/atributos ou transformação.
- **M:** “Manter durante ações compatíveis (recomendado)”. Ataque/guarda/Dodge/mobilidade/leitura disponíveis preservam estado pago e compatível; custos, fases e exclusão com Supressão permanecem. Sem bônus automático de dano.
- **N:** “Opressão limitada com grande diferença espiritual (recomendado)”. Prejuízo limitado de movimento comum contra alvo elegível muito inferior; semelhantes só recebem pressão percebida. Sem dano, paralisia, perda de comandos ou redução automática da mobilidade técnica.

BL-22, sinopse do episódio 308, relata opressão intensa em situação narrativa particular; não estabelece custos, alcance, elegibilidade ou o efeito limitado aprovado para a versão básica. Resumo japonês interpretado, sem episódio/capítulo integral. Não houve alteração de gameplay ou teste novo.

**Revisão:** 21/10/2026. M06 `ESPECIFICADO CONCEITUALMENTE`; M06-11 concluído no recorte conceitual, A–Y aprovados. Pendências executáveis e revisão na seção 48. Próximo: M07 — Transformações e Domínio. Linhagens/artes continuam adiadas.

**Encaminhamento após L/M/N, já trabalhado em O/P/Q abaixo:** alcance, obstáculos/barreiras, alvos/aliados/party/pets da pressão e relação com manifestação Natural; depois transições, concorrência/pagamentos e ciclo de vida, antes de avaliar fechamento conceitual de M06. Liberação e M06 não estão totalmente especificados. Números, fichas e dependências continuam com responsáveis; M07 permanece na sequência, com linhagens/artes adiadas.

### Alcance, alvos e manifestação O/P/Q aprovados — 20/10/2026

Três respostas explícitas registradas em M06-11, seções 33–35; revisão e critérios futuros na seção 36:

- **O:** “Área limitada que atravessa paredes comuns (recomendado)”. Área em todas as direções, distância tridimensional e pressão menor ao se afastar; barreira só protege conforme contrato. Sair da área válida encerra a penalidade dessa fonte, sem radar ou alteração de terreno.
- **P:** “Opressão sobre adversários elegíveis (recomendado)”. Inimigos elegíveis e jogadores adversários com combate permitido; aliados/party e seus pets protegidos. Animais/NPCs neutros não entram automaticamente; percepção permanece independente. Proteção é adaptação de cooperação, sem decidir dano amigo de todas as técnicas.
- **Q:** “Opressão básica exige Liberação deliberada (recomendado)”. Natural/Suprimida podem ser percebidas, sem penalidade básica gratuita; Liberação válida/paga autoriza N/O/P. Exceções futuras de formas/personagens exigem regras próprias.

Reconsultadas BL-21/22: não fixam paredes, raio, aliados ou funcionamento Natural. O/P/Q são adaptações aprovadas; sem episódios/capítulos integrais, nova API, implementação ou teste. Revisão dirigida não identificou conflito que exija revogar F–N. Nenhuma pergunta A–Q pendente.

**Encaminhamento após O/P/Q, trocas/Blut/pagamentos trabalhados em R/S/T abaixo:** transições Natural/Suprimida/Liberada, concorrência com Blut/passivas/ações, pagamentos simultâneos e interrupção/ciclo de vida. Depois avaliar fechamento conceitual de M06 e avanço a M07. Números/fichas, classificação concreta de alvos, barreiras e política de party/PvP permanecem com responsáveis; linhagens/artes continuam adiadas.

### Trocas, Blut e pagamentos R/S/T aprovados — 21/10/2026

Três respostas explícitas registradas em M06-11, seções 37–39; revisão dirigida e critérios futuros na seção 40:

- **R:** “Troca direta validada (recomendado)”. Um pedido para o estado pretendido; validar antes de abandonar o atual ainda válido. Sem sobreposição, espera universal extra, cancelamento de ataque, reset de tempos ou execução posterior de pedido recusado.
- **S:** “Blut compatível com os estados (recomendado)”. Uma aplicação conhecida de Blut pode coexistir com Natural/Suprimida/Liberada. Sinais visuais permanecem; ofensiva encerra Supressão; Vene/Arterie e Supressão/Liberação continuam exclusivos entre si. Sem conceder poderes raciais ou todas as formas por associação.
- **T:** “Manutenção primeiro; Blut antes de Liberação (recomendado)”. Resolver cobranças realmente devidas nessa ordem, depois custo da ação no momento próprio. Estado sem recurso termina; sem pacote indivisível, cobrança antecipada, estorno ou retorno automático. Stamina e Gintō preparado preservam contratos próprios.

Reconsultada BL-03, entrada 血装, em 21/10/2026: reforço corporal e exclusão das duas aplicações. Não comprova coexistência com manifestação ou prioridade de débitos. R/S/T são adaptações aprovadas; sem episódio/capítulo integral, nova API, implementação ou teste. Revisão não identificou conflito que exija revogar A–Q ou Blut. Nenhuma pergunta A–T pendente.

**Encaminhamento após R/S/T, ciclo de vida e efeitos trabalhados em U/V/W abaixo:** interrupção/ciclo de vida e modo de combate — morte, desconexão, dimensão, reinício, efeitos já emitidos, custos/tempos/conquistas. Depois avaliar fechamento conceitual de M06 e avanço a M07. Números, fichas, formas e conteúdo seguem com responsáveis; linhagens/artes adiadas.

### Ciclo de vida, tempos e efeitos U/V/W aprovados — 21/10/2026

Três respostas próprias registradas em M06-11, seções 41–43; revisão dirigida e critérios futuros na seção 44:

- **U:** “Encerrar estados; retorno manual (recomendado)”. Morte/logout/dimensão/reinício encerram cargas, leituras, propulsão, Blut e manifestação deliberada; Natural, sem apagar conquistas ou reativar sozinho. Custos pagos permanecem; não decide itens/recursos do respawn/formas por associação.
- **V:** “Tempo do mundo, preservado entre sessões (recomendado)”. Cooldowns/recuperações preservados; tempo de simulação conta inclusive offline. Mundo parado pausa; reinício não zera nem desconta período parado. Sem treino/regen offline concedidos.
- **W:** “Disparos autônomos continuam na região ativa (recomendado)”. Continuam na origem válida até impacto/expiração mesmo sem emissor; dependentes de presença/manutenção encerram. Ataques efêmeros terminam no descarregamento/reinício, sem replay; montagens persistentes exigem ficha própria.

MC-05 reconsultada sobre cópia/sincronização. Leitura de CapabilityEvents/PlayerData/TickHandler e trechos de StatusData identifica reset/não persistência dos cooldowns no código atual: divergência a adequar futuramente, sem alteração de runtime. U/V/W são regras aprovadas do mod, sem evidência de implementação/teste; nenhuma pergunta A–W pendente.

**Encaminhamento após U/V/W, trabalhado em X/Y e na revisão final abaixo:** saída de Combat Mode para Supressão/Liberação/leitura, consolidação de interrupções e revisão de maturidade de M06. Combat State, antidesconexão/PvP, formas, montagens e políticas de respawn mantêm fronteiras próprias. Depois avaliar fechamento conceitual e avanço a M07; linhagens/artes adiadas.

### X/Y aprovados e fechamento conceitual de M06 — 21/10/2026

- **X:** “Capacidades espirituais independentes do modo (recomendado)”. Supressão/Liberação/leitura podem funcionar fora do modo; fechar a hotbar não as encerra. Arco/Blut mantêm desligamento próprio; custos e Combat State permanecem.
- **Y:** “Sair da interface preservando a execução (recomendado)”. Interface pode fechar; preparos canceláveis seguem suas consequências e fases não canceláveis concluem. Mãos/fases continuam limitando itens, sem reset de tempos.

Seções 45–46 registram as respostas; seção 47 consolida interrupções herdadas, sem nova votação; seção 48 fecha M06 como `ESPECIFICADO CONCEITUALMENTE`. Revisão dirigida não identificou impedimento estrutural neste recorte para iniciar M07. Sem nova pesquisa canônica/API ou teste nesta rodada; A–Y sem perguntas pendentes.

**O que continua aberto:** fichas completas, categorias/ordem técnica de dano, números, catálogo/ensino, formas, corpos/vetores/equipamento, montagens persistentes, controles concretos, políticas de PvP/party/antidesconexão/respawn e implementação/migração. Responsáveis e condições de retorno na seção 48; reset de cooldown do código continua divergência, sem alteração executada.

**Encaminhamento do fechamento, atendido na abertura registrada no início deste handoff:** M07 — Transformações e Domínio. A distinção conceitual foi consolidada; o documento próprio e as decisões atuais não alteram o histórico de M06. Preservar decisões raciais, pontos/Mastery e o adiamento de linhagens/artes.

### Fullbringer aprovado e revisado — M06-10

Em 20/10/2026, respostas explícitas aprovaram A–F:

- **A:** “Uma Proficiência dos fundamentos + Masteries por técnica (recomendado)”. Fundamentos Fullbringer com Proficiência; poder principal com desenvolvimento próprio.
- **B:** “Aprendizado próprio com prática (recomendado)”. Bringer Light aprendido após fundamentos, possível antes do poder individual, sem exigir Xcution/objeto pessoal.
- **C:** “Impulso curto direcional (recomendado)”. Comando dedicado, Stamina uma vez ao iniciar, sem Energia adicional/item; colisões dentro e fora de combate.
- **D:** “Incluir salto impulsionado aprendido (recomendado)”. Aplicação aprendida a partir de apoio no chão, mesma Mastery de Bringer Light, uso deliberado e custo próprio. Salto vanilla preservado.
- **E:** “Horizontal no chão, ar e água; salto com apoio (recomendado)”. Física/queda do meio preservadas; sem relançamento vertical no ar, piso aquático, respiração extra ou voo concedido.
- **F:** “Reagir preservando custos, tempos e queda (recomendado)”. Ação disponível encerra propulsão horizontal sem recuperação extra só pela parada; pedido inválido mantém impulso válido. Salto lançado permite ações aéreas disponíveis sem apagar trajetória/queda, custo ou tempos anteriores.

**Revisão/fechamento:** seções 15–17. Não foi identificado conflito que exija revogar escolhas. Vetor, exclusividade do poder, aquisição, domínio e relação com itens foram consolidados a partir de M04/M05/M06, sem aprovar uma manifestação/custo universal para todos os Fullbrings. Nenhum G, primeiro poder do catálogo ou mestre obrigatório foi escolhido. A–F não exigem nova votação; não há pergunta pendente.

**Pendências:** fichas de C/D e suas prioridades/reuso, meios/apoios/queda concretos, ciclo de vida, dano/vanilla, vetores/mãos/perda/reposição, Fullbrings específicos/formas, fontes de ensino, catálogo e calibração. Responsáveis em M06, M06-11, M07–M16. Não conceder edição livre de blocos, radar, bônus universal de equipamento ou capacidades pessoais pela Proficiência. A existência de C/D não elimina futuras técnicas compartilhadas com referência e ficha próprias.

Pesquisa da preparação: F1–F13 de M04 e contratos relevantes M05/M06; BL-11 (Fullbring/Fullbringer), BL-05 (vetores), BL-17/18 (treino/mobilidade em resumos oficiais). Sem episódios/capítulos integrais. E/F são adaptações aprovadas; a atualização/revisão não acrescentou evidência canônica nem nova consulta de APIs. Não houve mudança de runtime/build/dependências/saves ou teste de gameplay.

**Encaminhamento após Fullbringer, já iniciado:** M06-11 confronta base universal e quatro raças. A rodada vigente está descrita acima; preservar revisões identificadas e dependências com responsáveis. Depois seguir a ordem modular. Fichas de linhagem/Grimmjow e artes continuam adiadas.

### Histórico aprovado e revisão Quincy

M06-09A/B foram aprovados explicitamente em 20/10/2026:

- **A:** “Energia por disparo, sem custo para manter o arco (recomendado)”. Disparo básico consome Energia, com custo/cadência adequados à arma principal; manter o arco formado não drena continuamente; ambiente pode influenciar eficiência sem coleta manual ou segunda barra.
- **B:** “Segurar para preparar e soltar para disparar (recomendado)”. Preparação curta e limitada; máximo permite manter a mira sem ganho infinito de força ou disparo automático. Não foi escolhido tiro imediato por toque.

**M06-09C aprovado em 20/10/2026:** resposta explícita “Três campos + Masteries específicas (recomendado)”. Armamento espiritual, mobilidade e Gintō, este aprendido separadamente, possuem Proficiência; técnicas distintas têm Mastery. Tiro fundamental desenvolve armamento sem Mastery própria redundante; Vene e Arterie têm Masteries próprias, sem outra Proficiência de Blut. Uma Proficiência geral de Reishi não foi escolhida. Não reabrir a organização.

Foram comparadas alternativas e explicitados prática, aquisição, mestres, instrumentos e critérios de revisão. Gintō não é pré-requisito universal nem classe exclusiva. Conhecer/possuir um instrumento não transfere técnica; não conceder todos os campos por gastar Energia. Controle continua atributo, Reishi ambiental não vira recurso adicional e percepção mantém seus indicadores existentes.

**M06-09D aprovado em 20/10/2026:** resposta explícita “Cancelar a flecha para reagir (recomendado)”. Durante preparo do arco básico, caminhar devagar e mirar são permitidos; bloquear, Dodge ou Hirenkyaku cancelam flecha/carga. Antes do disparo, não há recuperação adicional por esse cancelamento; a reação mantém requisitos, custos e recuperação próprios. Depois de disparar, a recuperação do tiro continua. D não revisa K de Cero, que conserva sua recuperação de cancelamento. Seções 12–13 registram decisão e revisão documental; não houve teste de gameplay.

**M06-09E/F/G aprovados em 20/10/2026 por três respostas explícitas:**

- **E:** “Cobrar somente ao emitir (recomendado)”. Uma cobrança por flecha emitida; preparar/cancelar antes disso não paga o tiro, falta de Energia impede emissão e erro/colisão posterior não devolve custo.
- **F:** “Permitir flecha incompleta após mínimo curto (recomendado)”. Antes do mínimo, soltar cancela; depois, dano reduzido cresce com preparo até o máximo. Mesmo custo de Energia e velocidade nas mesmas condições.
- **G:** “Linha reta com tempo de viagem (recomendado)”. Mira manual sem perseguição/queda; primeiro impacto válido encerra; obstáculos bloqueiam; sem destruir blocos ou gerar flecha coletável.

**M06-09H/I aprovados em 20/10/2026 por duas respostas explícitas:**

- **H:** “Manter preparo válido se a reação falhar (recomendado)”. Reação indisponível é rejeitada com aviso, preservando preparo ainda válido. Guardar/perder a arma, morrer, desconectar ou trocar de dimensão limpa a carga, sem custo de tiro ou perda de conquistas. Não reseta disparo/recuperação já realizados.
- **I:** “Permitir reação mantendo recuperação ofensiva (recomendado)”. Após emitir, movimento, bloqueio, Dodge e Hirenkyaku disponíveis podem começar sem espera adicional do tiro. A recuperação ofensiva continua contando; outro tiro ou técnica ofensiva aguarda tanto seu término quanto a ação atual permitir atacar. Permissão específica do tiro básico, sem generalizar para todas as técnicas.

**M06-09J/K/L aprovados em 20/10/2026 por três respostas explícitas:**

- **J:** “Foco físico equipado (recomendado)”. Instrumento físico do arco inicial, com Cruz Quincy como referência; formação aprendida separadamente; foco não é consumido por disparo. Slot, obtenção e reposição concreta permanecem para M11.
- **K:** “Sem custo adicional de manifestação (recomendado)”. Formar/guardar sem Energia adicional, visual possível com Energia insuficiente para tiro; recurso continua exigido ao preparar/disparar. Alternar não recupera recursos nem apaga tempos.
- **L:** “Manifestação manual com duas mãos (recomendado)”. Separada de entrar no modo; duas mãos sem outro item simultâneo, com bloqueio/reação disponíveis. Sair do modo dissipa arco/preparo, preservando inventário e recuperação.

**Encaminhamento histórico após o arco:** Hirenkyaku, depois Blut/repertórios. Seção 22 consolida estruturalmente o arco e distribui bloqueadores de ficha/calibração: equipamento e perda/reposição do foco, mãos/comandos, números, alvos/escudos/meios, concorrência/ciclo de vida e runtime. A estrutura não exige aguardar todo M11 para discutir a mobilidade. Não tratar o arco como completamente especificado, implementado ou testado. A–L não precisam de nova votação. D/I aprovam permissões de Hirenkyaku em relação ao arco, não sua direção, distância ou aquisição. A rodada seguinte decidiu M/N/O sobre Hirenkyaku por respostas explícitas; ver abaixo.

**Rodada atual — Hirenkyaku, 20/10/2026:** seções 23–27 registram **M06-09M/N/O DECIDIDOS** por três respostas explícitas, sem reabrir A–L:

- **M:** “Aprendizado próprio de Hirenkyaku (recomendado)”. Após fundamentos iniciais de Reishi, com fonte apropriada e prática; não conceder automaticamente pelo arco, nem exigir Schrift/Wandenreich.
- **N:** “Impulso curto direcional (recomendado)”. Comando dedicado, uma cobrança de Stamina ao iniciar, percurso/colisão completos, dentro e fora de combate; sem foco/arco ou Energia adicional. Deslocamento mantido não foi escolhido.
- **O:** “Parar para agir, permitindo interrupção (recomendado)”. Ataque, bloqueio ou Dodge disponível podem interromper o impulso sem recuperação extra pela parada. Custo já pago e tempos anteriores permanecem; pedido indisponível preserva movimento ainda válido. Não há tiro/guarda simultâneos ao impulso.

Reconsultadas BL-03/BL-11 na preparação: fluxo de Reishi nos pés, sem estabelecer controles/custos do mod. Não houve nova pesquisa canônica na atualização das aprovações. A revisão documental não identificou conflito com A–L: carga descartada não retorna, flecha emitida e recuperação ofensiva permanecem; próximo preparo exige ação válida. Números, ensino concreto e integração continuam delegados, sem teste em jogo ou implementação nova.

**Encaminhamento histórico após M/N/O:** ar, verticalidade, desníveis, quedas e água, depois Blut; esse bloco foi decidido em P/Q/R. Esses meios não foram decididos por M/N/O, nem se concedeu voo por omissão. M06-09 continua em especificação. Não votar M/N/O novamente; a rodada conjunta seguinte prepara P/Q/R, descrita abaixo.

**Rodada conjunta atual:** M06-09P/Q/R **DECIDIDOS** por três respostas explícitas em 20/10/2026, nas seções 28–32:

- **P:** “Horizontal no chão, ar e água (recomendado)”. Impulso horizontal com gravidade, queda acumulada, colisão e riscos ambientais; sem subir pela mira, piso aquático, voo ou respiração concedidos.
- **Q:** “Seleção manual com manutenção de Energia (recomendado)”. Aprendizado específico, desligado/Vene/Arterie, Energia de manutenção sem taxa fixa adicional, troca breve sem sobreposição e desligamento ao sair do modo de combate. Vene permite ataques comuns; Arterie permite guarda comum, sem reforço da outra aplicação.
- **R:** “Arterie em golpes de força corporal (recomendado)”. Reforça componentes corporais de golpes/ataques armados; sem bônus automático a flechas/projéteis/Gintō. Vene mitiga dano de ataques elegíveis, sem cura ou invulnerabilidade. O recorte é adaptação explícita, sem alegar uma proibição canônica universal de reforçar projéteis.

A revisão documental não identificou conflito que exija rever A–O. Manutenção de Blut e custo de tiro são obrigações diferentes sobre Energia: tiro sem recurso falha sem cobrança de flecha, preservando o que já foi pago por Blut. Stamina continua disponível para Hirenkyaku quando válido. Nenhuma aplicação elimina as restrições de ataque/guarda durante o impulso. Sem nova pesquisa externa na atualização das aprovações, implementação ou teste novo.

**Encaminhamento após P/Q/R:** seção 33 consolida Hirenkyaku e Blut estruturalmente. Repertórios/Gintō foram decididos depois em S/T/U e a revisão geral está na seção 39. Números, ensino concreto, classificação/ordem de dano e integrações têm responsáveis nas seções 27/32/33. M06-09 continua em especificação, sem pergunta pendente desta rodada. A–R não precisam de nova votação.

**S/T/U aprovados e agora registrados:** a falta de espaço impediu a gravação na rodada anterior. Após o usuário liberar espaço, respostas e contratos foram salvos nas seções 34–38, sem nova votação:

- **S:** “Tubos recarregáveis com conteúdo preparado (recomendado)”. Preparar com Energia fora de Combat State; fórmula consome conteúdo e preserva recipiente vazio; sem cobrar novamente pelo mesmo conteúdo. Provisão pode alimentar fórmula básica com Energia pessoal atual zero.
- **T:** “Guardar arco manualmente para Gintō (recomendado)”. Fórmula pela Combat Hotbar com tubos acessíveis; mãos compatíveis e arco guardado quando necessário; pedido incompatível falha sem consumo ou perda de preparo válido.
- **U:** “Execução e consumo conforme cada fórmula (recomendado)”. Preparo direto ou montagem no mundo conforme a técnica; consumir conteúdo por etapa na liberação, sem reembolso de uso realizado e sem minijogo/encantamento digitado obrigatório.

**Revisão geral concluída:** seção 39 consolida A–U, relações com M04/M05/M06, pendências e riscos a validar. Não foi identificado conflito que exija revogar escolhas. Atenção futura: estoque/recarga de Gintō, utilidade corporal de Arterie, custo de Blut junto ao arco, transição manual de mãos e preparações em mundo construído. Esses pontos têm responsáveis e não são defeitos demonstrados nem justificativa para mudar decisões silenciosamente. Reuso/recarga e recortes de Arterie continuam adaptações identificadas. Nenhuma pergunta desta revisão está pendente.

**Situação após o fechamento Quincy (histórica):** estrutura decidida; ainda faltavam fichas, números, catálogo, ensino concreto, inventário, dano, estados persistentes e implementação/validação. Fullbringer foi concluído estruturalmente depois; M06-11 é o bloco vigente. Não foi alterado runtime nem executado teste de Minecraft. A conferência documental de links/estados não equivale a gameplay validado.

Leituras de entrada: M04, RAC-09 e Q1–Q11; M05, recursos/Reishi e revisões de pontos; M06 universal e M06-07. Q11 em M04 mantém linguagem histórica de compra de técnicas: aplicar M05, REV-M02-02/04/05, e M06-07; não recuperar a loja de habilidades. Fontes reconsultadas no início de Quincy: glossário TYBW, entradas Quincy/Blut/Schrift/Vollständig, e Pierrot, Hirenkyaku. Blut ofensivo e defensivo não simultâneos são referência canônica; Q/R decidiram a execução estrutural do mod, com parâmetros e integração ainda dependentes de ficha. Nenhum mestre específico foi escolhido.

M06-01–06, os quatro caminhos M06-07/08/09/10 e M06-11 estão consolidados conceitualmente; a seção 48 governa a continuidade. Seguir M07–M16 conforme o mapa, preservando as entregas pendentes e os critérios de maturidade.

## Histórico consolidado de M06-08 — Hollow / Arrancar

**Fechamento em 20/10/2026:** seção 36 de M06 reúne as aprovações e encaminha fichas de combate, compatibilidade, evolução, corpos grandes, mobilidade aérea, travessias, mentores, inventário, apresentação e calibração. O fechamento é estrutural; não declara origem jogável implementada nem resolve essas pendências por omissão. Garganta/viagem não foi concedida por Sonído, e sustentação aérea não foi aprovada nesta rodada.

Leituras utilizadas para abrir M06-08: contratos relevantes de M04/M05/M06, incluindo revisões, aplicações Hollow e fechamento do M06-07. A base inicial não representa pesquisa completa de todas as exceções de lore; futuras fichas exigem suas fontes específicas.

Primeiro bloco de M06-08 concluído: herança, fontes oficiais, implicações de Minecraft, alternativas e critérios de revisão. **M06-08A/M06-08B estão `DECIDIDOS` em 20/10/2026**: três campos funcionais (combate físico, emissão espiritual e mobilidade), com Proficiência por campo e Mastery individual. A equipe escolheu explicitamente “Três campos com Proficiência + Mastery por técnica (recomendado)”. Proficiência Hollow única e ausência de Proficiência foram alternativas não escolhidas.

Segunda rodada concluída em **M06-08C — perfis corporais com diferenças funcionais controladas**, `DECIDIDO` em 20/10/2026. Resposta explícita da equipe: “Perfis corporais com diferenças funcionais controladas (recomendado)”. Cada perfil pode ter golpes, alcance e colisão próprios, com controles fundamentais comuns; variações cosméticas não alteram essas propriedades. Corpo funcional único e montagem livre de membros/proporções não foram escolhidos. Fonte nova: glossário oficial Pierrot, entrada Hollow, sobre diversidade de formas/capacidades.

Terceira rodada concluída nas seções 17–21: **M06-08D — incorporação conforme a complexidade da capacidade**, `DECIDIDO` em 20/10/2026. Resposta explícita da equipe: “Modelo misto, conforme a complexidade da capacidade (recomendado)”. Refinamentos simples podem entrar naturalmente com preparo; técnicas gerais com execução distinta ficam desenvolvíveis e são adquiridas por prática apropriada; especializadas preservam suas fontes/condições. A aquisição automática de todas as gerais e a prática obrigatória para cada ampliação não foram escolhidas.

Quarta rodada preparada nas seções 22–26. A equipe pediu julgamento de direção criativa e análise do valor de mestres. **M06-08E consolida diretrizes herdadas**: guia inicial, mestre de repertório, rival avaliador e personagem associado ao poder podem ensinar/orientar sem virar NPC obrigatório para cada melhoria. A existência de mentores e ensino de repertórios já está aprovada; não perguntar isso de novo. Autonomia não significa aprender tudo sem fonte externa.

Recomendações de conteúdo ainda candidatas: veterano original para sobrevivência, Neliel para estudar uma relação cooperativa, Grimmjow como rival avaliador e Harribel em contexto de facção/era apropriados. As fichas oficiais sustentam identidade, não o ensino dessas capacidades ao jogador. Elenco, exercícios, localização, disponibilidade e quests finais pertencem a M08/M12/M13; não estão aprovados por esta análise.

**M06-08F está `DECIDIDO` em 20/10/2026**. Resposta explícita da equipe: “Moveset orientado pela empunhadura (recomendado)”. Arma empunhada orienta ataques fundamentais armados e arma guardada orienta corpo, permitindo técnicas corporais compatíveis enquanto armado. Não foi escolhida postura física separada da empunhadura. Alternância respeita execução/Recovery e não concede conhecimento ou resets.

**Entrada posterior da mesma data:** o usuário forneceu um handoff de outra sessão e pediu análise antes de continuar. A [reconciliação do histórico Hollow](00-reconciliacao-handoff-hollow-2026-10-20.md) preserva a cópia integral, o parecer e a aprovação posterior. **M06-08G está decidido:** trajetórias por linhagem com perfis controlados, identidade anterior à Arrancarização em contexto compatível, rota Arrancar em Gillian preservada e Grimmjow/Pantera como primeira linhagem. M04/H6 recebeu REV-H6-01. Predação e agência Gillian foram preservadas; não adotar regressão passiva por ficar offline ou pausar a caça para construir/explorar. Condições concretas continuam para M07. Os IDs do anexo não substituem os atuais; o G atual tem significado próprio. Não reabrir H8/H9 por pendências históricas.

**Referências visuais aprovadas como método:** usar formas oficiais quando disponíveis e pesquisar fan arts para orientar as etapas faltantes. Identificar autor/publicação e separar oficial, fan art e adaptação final. Nenhuma arte específica foi escolhida. Grimmjow possui referências fortes de identidade em Adjuchas, Arrancar e Pantera; não foi confirmada nas fontes conferidas uma forma Vasto Lorde específica dele. A etapa Vasto Lorde do jogador requer referência adicional ou adaptação, sem apresentá-la como forma canônica confirmada.

**Correção de prioridade pelo usuário, em 20/10/2026:** continuar definindo as regras gerais e o funcionamento do jogo conforme a ordem dos módulos. A ficha específica de Grimmjow, a seleção de artes e as aparências ficam para uma etapa posterior. Grimmjow permanece a primeira linhagem quando esse detalhamento começar; essa escolha não muda a ordem de especificação dos sistemas.

**Rodada solicitada depois dessa correção:** o usuário pediu desenvolver os três primeiros assuntos e fazer a revisão depois. H/I/J foram preparados, apresentados e **aprovados explicitamente em 20/10/2026**. Respostas: “Adotar Cero carregável e Bala rápida (recomendado)”; “Sonído direcional (recomendado)”; “Aprovar essa organização (recomendado)”. As seções 28–32 registram pesquisa, contratos e aprovações. Não perguntar isso novamente.

- **H — emissão, decidido:** Cero desenvolvível a partir de Gillian, com carga limitada; Bala geral Arrancar com disparo breve; Masteries separadas e Proficiência de emissão. Cero com preparação fixa não foi escolhido. Custo espiritual, colisão e sem variantes avançadas automáticas; parâmetros e composição de efeitos continuam em especificação.
- **I — mobilidade, decidido:** Sonído desenvolvível como Arrancar, direcional, com trajeto/colisão válidos; preserva Stamina, Mastery e Proficiência de mobilidade. Destino pela mira não foi escolhido. Não concede voo ou invulnerabilidade.
- **J — capacidades, decidido:** Hierro passivo, com Mastery defensiva já prevista em M05; Pesquisa como leitura focada usando Percepção Espiritual, sem outra barra redundante; regeneração rápida restrita a capacidade/corpo compatível, separada da recuperação natural. Execução específica da regeneração fica na ficha apropriada, sem detalhar linhagens agora.

**Revisão conjunta realizada**, seção 33: confronto documental com recursos, aprendizado, interrupção, defesa, percepção e compatibilidade dos estados. Não foi encontrada contradição que exija revogar H/I/J. Foram consolidados o aprendizado inicial sem requisito circular, o crédito único da mesma leitura espiritual, a separação de Hierro/guarda/controle e o tratamento da regeneração especial. Sem teste de gameplay, números calibrados ou aceite de implementação.

**K também decidido**, seção 34: resposta explícita “Cancelar a carga para reagir (recomendado)”. Caminhar de forma limitada e mirar durante carga são permitidos; bloquear, usar Dodge ou Sonído exige cancelar a carga e respeitar a recuperação. A carga cancelada não é preservada para a próxima tentativa. Hierro passivo continua protegendo enquanto compatível. Não reabrir essa combinação.

O fechamento geral foi conferido e registrado na seção 36; a continuidade avançou para Quincy. Fichas específicas e artes permanecem adiadas. Não tratar o fechamento como nova votação das aprovações existentes. M07 trata evolução, Arrancarização e liberações; os demais módulos continuam conforme o mapa. Desenhos finais, técnicas, números, quests e compatibilidades concretas permanecem abertos; isso não equivale a M06 inteiro concluído.

**Comunicação:** o usuário pediu mais clareza. Explicar primeiro o efeito para o jogador, com frases diretas e exemplos; reservar IDs e termos técnicos para rastreabilidade, sem depender deles para a compreensão.

Catálogo/quantidade de corpos, seleção inicial, medidas e transições continuam abertos; perfis aprovados não implicam escala literal, forma compacta livre ou perda de Mastery.

Não criar catálogo completo, quests ou números apenas para adiantar etapas. O glossário TYBW alerta que Arrancars geralmente perderam a regeneração rápida; capacidades e exceções precisam de fichas próprias. A pesquisa ainda não confirmou cenas/capítulos com todos os requisitos Menos ou de técnicas avançadas.

## Estado documental

| Módulo | Estado e ressalva |
|---|---|
| M01 | `DECIDIDO`; visão, pilares e política canônica |
| M02 | `DECIDIDO` arquiteturalmente; partes revisadas por M05/M06 |
| M03 | `DECIDIDO` arquiteturalmente; campanhas concretas e catch-up delegados |
| M04 | `EM ESPECIFICAÇÃO` no escopo completo; identidades raciais consolidadas, criação/continuidade pendentes conforme revisão de 22/10 |
| M05 | `ESPECIFICADO CONCEITUALMENTE`; fórmulas, calibração e tradução para o código pendentes |
| M06 | `ESPECIFICADO CONCEITUALMENTE`; base universal, quatro caminhos e integração consolidados; entregas na seção 48. M07 concluído conceitualmente; sem aceite de implementação |
| M07 | `ESPECIFICADO CONCEITUALMENTE`; perfis básicos, C01–C12 e G01/G02 consolidados. Entregas na seção 12.2 e revisão comum na 15. Sem pergunta geral pendente de transformações; antes de M08, concluir a pendência identificada em M04 |
| M08–M16 | Documentos próprios ainda não iniciados; dependências e implementações parciais não os tornam concluídos |

## Revisões que evitam regressões de design

1. **M05, REV-M02-01:** progresso integrado dos atributos; o modelo anterior de rank e XP prática separados foi superado.
2. **M05, REV-M02-02/04:** técnicas seguem aquisição apropriada e Mastery; Spiritual Points têm como função direta principal os atributos. Não presumir compra de todas as técnicas por pontos.
3. **M05, REV-M02-03/05:** Mastery não se compra; pontos não são automaticamente obrigatórios para toda grande transformação.
4. **M06-07:** Proficiência de disciplina, repertório, técnica e Mastery individual são distintos; não criar outro rank obrigatório da disciplina.
5. **M06-07:** lista/repertório substitui skill tree como estrutura principal. Formação e desenvolvimento autônomo existem; um NPC por técnica foi descartado.
6. **M05:** cinco atributos já decididos — Potência, Vitalidade, Resistência, Reserva e Controle. O código ainda possui sete categorias; especificar migração/compatibilidade sem reabrir automaticamente o design ou apagar saves.
7. **M04, RAC-23:** um poder canônico principal por vez; desenvolver o repertório racial não concede todos os poderes individuais da obra.
8. **M04/M05/M06:** natureza, evolução, cargo e forma são distintos; Progressão, Proficiência e Mastery também. Duplicação de poderes entre jogadores é adaptação aceita.

O fato de uma implementação existir ou um texto ser mais recente não prova aprovação de uma revisão. Em conflito real, mostrar as decisões e o contrato implementado, recomendar uma solução e obter a decisão necessária.

## Entrada Hollow já decidida

Ler os blocos completos de M04 antes de usar este resumo:

- corpo como primeira ferramenta de combate;
- fome/instinto sem segunda barra permanente de manutenção;
- grind válido, mas insuficiente sozinho para grandes marcos;
- aprendizado por sobrevivência e contexto, sem Academia Hollow artificial;
- começo no Mundo Humano; acesso posterior a Hueco Mundo como marco;
- evolução Hollow antes da Arrancarização, com ramificações em estágios compatíveis;
- poder Arrancar exige natureza compatível;
- Vasto Lorde como estado evolutivo permanente, difícil e determinístico, conforme o fechamento posterior do módulo;
- diferenças raciais sem simetria artificial e conteúdo multirracial nos territórios.

Uma pendência antiga sobre raridade de Vasto Lorde não deve ser recuperada sem ler sua resolução posterior. Condições concretas de evolução e transformação pertencem a M07.

## Pesquisa e contexto técnico

Na revisão de 20/10/2026, o projeto declara Minecraft Java 1.20.1, Forge 47.4.10, Java 17 e Gradle 8.8. Revalidar esses dados no build quando houver mudança técnica.

O preset Modo História declara Soul Society e Hueco Mundo e instala mapas pré-construídos; não presumir as dimensões espirituais em um mundo comum. O preset também preserva Nether e End. Conferir o tipo de mundo em decisões que dependam desses locais.

A lógica decisiva pertence ao servidor, inclusive no singleplayer. Técnicas devem considerar terreno construído, recursos, inventário, farms, morte/respawn, dimensões, chunks e observadores. Dados JSON de quest no save e datapacks são mecanismos diferentes no código atual.

Para Bleach, seguir D05 e registrar capítulo/episódio/seção, continuidade e data. Material anunciado ou promocional não prova toda uma capacidade; verificar a parte efetivamente publicada quando ela for necessária.

## Skill e aproveitamento dos materiais externos

Skill do projeto: [bleach-game-design](../../.agents/skills/bleach-game-design/SKILL.md), com metadados do Codex e referências de revisão de contratos/coerência narrativa. Ativação: `$bleach-game-design`.

Em 20/10/2026 foi preparada em `.agents/skills/bleach-game-design/` e registrada por vínculo no diretório de skills do usuário. A pasta do repositório é a fonte mantida. Em outra máquina, conferir a descoberta/registro local; o vínculo pessoal não faz parte do Git.

`game-design-architect`, de ncdlek, orientou a revisão técnica; a coerência narrativa de fcsouza foi consultada e adaptada aos documentos locais. A [nota de adoção](../../.agents/skills/bleach-game-design/references/origem-e-adocao.md) registra as fontes e os limites.

O plugin completo fcsouza/game-dev não foi instalado e seus comandos/hooks não são declarados ativos. A skill local cobre o trabalho documental necessário. `bleach-mod-dev` continua sendo a referência separada para execução e QA quando essa tarefa for solicitada.

## Estado local e verificação desta preparação

Snapshot: `main` no commit `8ace6520e67d27441974b9ad92b443ddb33f9882`; remoto `Costa-Novaes-Tech/Bleach-Modpack`. Conferir Git novamente antes de editar. O antigo remoto e a branch dos handoffs históricos não governam este checkout.

Já existiam uma alteração em `.DS_Store`, a auditoria/base de conhecimento locais e a inclusão da base no mapa. Preservar esse trabalho. Esta preparação adiciona a skill, este handoff e correções de continuidade nos documentos; não inclui commit ou push.

O validador oficial da skill aprovou sua estrutura. Conferir também os links e o diff ao encerrar a edição. Validação de estrutura não demonstra que todas as decisões futuras serão corretas; o fluxo precisa de revisão crítica durante seu uso.

Não foram alterados sistemas de gameplay ou executados novos testes de Minecraft nesta preparação. Resultados automatizados dos ciclos técnicos continuam evidência histórica das execuções documentadas.

## Método para continuar

Conversar em português claro; explicar trade-offs e recomendar. Trabalhar um bloco por vez, respeitar decisões anteriores e registrar cada mudança com data e estado. Aprovação do método/skill não aprova novas mecânicas por antecipação.

A partir de 23/10/2026, após cada resposta, registrar a decisão e apresentar as próximas perguntas pertinentes sem esperar “pode seguir”. Escolhas novas continuam pendentes até resposta explícita.

Para cada bloco: pesquisar e ler o necessário, produzir análise revisável, apresentar as decisões ainda abertas, registrar respostas já aprovadas e revisar dependências. Não esperar confirmação para uma correção factual já autorizada nem marcar proposta como decisão por falta de resposta.

Ao fechar: apontar o que foi decidido, o que foi especificado, as dependências e o próximo bloco; distinguir qualquer evidência de implementação, teste e aceite. Os handoffs antigos permanecem históricos, sem substituir esta continuidade.
