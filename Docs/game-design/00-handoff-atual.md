# Handoff vigente — Game Design do Bleach Mod

**Revisão vigente:** 22/10/2026. M08 especificado conceitualmente após M08-04C/D aprovados e revisão dirigida de integração; fichas/execução continuam pendentes. Próximo módulo principal M09 — Mobs, bosses e encontros. Checkout atualizado por fast-forward para `bb26be8`, preservando trabalho local; inventário e lista de assets reconciliados. SP compra skills/transformações; Mastery pela prática. Nenhuma implementação nova dos contratos GDD ou teste local nesta sessão; runtime recebido do upstream.

## Projeto e objetivo

Mod de Bleach para Minecraft Java: personagem original, poderes canônicos, campanha guiada em mundo aberto e persistente, exploração, sobrevivência, construção, treino e multiplayer. O GDD descreve o horizonte completo, com implementação por entregas coerentes.

Fontes de entrada:

- [Mapa modular](00-mapa-modular.md): ordem e responsabilidade dos 16 módulos, template e estados.
- [Base de conhecimento](00-base-conhecimento-minecraft-bleach.md): pesquisa datada, fontes oficiais e contratos revisados.
- [Auditoria do checkout](00-auditoria-estado-2026-10-20.md): relação entre design, implementação e evidência histórica.
- [Continuidade de engenharia e poderes](../poderes/00-CONTINUIDADE-CODEX.md): ciclos técnicos; seus caminhos e branches históricos precisam de confirmação local.

## Ponto exato de continuidade

### Rodada atual — M08 consolidado e implementação atualizada — 22/10/2026

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

Para cada bloco: pesquisar e ler o necessário, produzir análise revisável, apresentar as decisões ainda abertas, registrar respostas já aprovadas e revisar dependências. Não esperar confirmação para uma correção factual já autorizada nem marcar proposta como decisão por falta de resposta.

Ao fechar: apontar o que foi decidido, o que foi especificado, as dependências e o próximo bloco; distinguir qualquer evidência de implementação, teste e aceite. Os handoffs antigos permanecem históricos, sem substituir esta continuidade.
