# 121 — Revisão da main: futuro do projeto e integração com poderes

26/10/2026. Pedido: revisar a documentação atualizada da equipe, suas propostas para o futuro e melhorias, enquanto Antigravity trabalha nas duas frentes visuais. Somente revisão documental; sem mudança de runtime, merge, commit, publicação ou interferência nos checkouts dos agentes.

## 1. Fonte efetivamente encontrada e alcance

Após `git fetch origin --prune`, `origin/main` continua em **26b73b3**, consolidação de game design M01–M10 de23/10. Esse commit já é ancestral da nossa branch; não apareceu nova revisão remota exclusiva de GDD nesta consulta. A `main` local está em **84738ae**, um commit à frente, com fontes WAV/importadores de áudio; checkout limpo. O HEAD de poderes na abertura desta revisão é **c6b9ec5**, sincronizado com origin. Nenhuma dessas referências foi movida por checkout/merge.

Inventário integral de arquivos rastreados em Docs na main remota: **7666 arquivos**, dos quais **501 textos estruturados**, incluindo **266 Markdown**; os demais são principalmente evidências e mídia. Textos por domínio:96 game-design,333 poderes,43 QA,13 arquitetura,8 planejamento,5 arte,1 desenvolvimento,1 jogador e1 README. Isso conta arquivos, não funcionalidades concluídas.

[Inventário com hashes, linhas e títulos](qa/cycle121/main-document-inventory.json). Cobertura: inventário de todos os textos, leitura funcional dirigida dos contratos vigentes M01–M10, revisões, catálogos e dependências, com comparação a documentos técnicos/planejamento. Históricos, milhares de quadros QA e fontes externas não foram relidos individualmente como novas decisões. Não é auditoria integral do cânone, do código ou teste atual de gameplay. O snapshot textual de apoio está no cache local build/review-main-docs; não foi duplicado inteiro na documentação.

As fontes que governam o planejamento são o [handoff vigente](../game-design/00-handoff-atual.md), [mapa modular](../game-design/00-mapa-modular.md) e [checkpoint de cobertura](../game-design/00-revisao-cobertura-m01-a-m10-2026-10-23.md), sobretudo12.1 e a continuidade que aponta M11 com retorno M05. Parágrafos inferiores preservam etapas antigas; não reabrir M04/presença ou Combat State já respondidos por ler um encaminhamento histórico.

## 2. Estágio real do planejamento

- **M01–M03:** visão, trilhas e storyline macro decididas. Personagem original, poderes canônicos, campanha guiada em mundo aberto persistente e quatro jornadas raciais. Era compartilhada do mundo e marcos pessoais são distintos; o jogador/party enfrenta os bosses adaptados, preservando os marcos necessários à história.
- **M04–M07:** raças, atributos/recursos, habilidades/combate e formas com fechamento conceitual no alcance declarado. Conteúdo, fichas executáveis, fórmulas, recuperação de riscos e realização técnica continuam com responsáveis. Fechar o conceito não entrega automaticamente quatro sistemas raciais completos.
- **M08–M10:** regras gerais têm decisões consolidadas, mas catálogos de NPCs, adversários e lugares continuam em especificação. Uma skin, classe Java ou nome de região não demonstra a atividade que o jogador fará ali.
- **M11–M16:** previstos no mapa; não há arquivos dos seis módulos publicados na main examinada. Próximo principal explicitamente indicado: **M11 — itens, equipamentos e economia**, com retorno dirigido a M05. Depois vêm quests, campanhas, interface, multiplayer/balanceamento e integração/testes.

Na engenharia de poderes há um recorte Shinigami avançado e testado, acompanhado de frameworks de quests/NPCs e protótipos de mundo registrados anteriormente. Esta revisão não reexecutou esse inventário de runtime nem promoveu as quatro origens a implementadas.

## 3. Propostas e decisões mais relevantes para o futuro

### Progressão e aquisição

[M02](../game-design/02-trilhas-de-progressao.md), [M05](../game-design/05-atributos-e-recursos.md) e revisões REV-SP-02: SP compra skills, transformações e investimento em atributos; requisitos/contexto disponibilizam a aquisição; Mastery cresce pela prática. Aquisição persistente, ativação e domínio são três operações diferentes. O projeto não tem um nível global que substitua todas as trilhas.

O modelo de design possui cinco atributos fundamentais: Potência, Vitalidade, Resistência, Reserva e Controle; recursos universais Vida, Stamina e Energia Espiritual. Proficiências/repertórios, vínculo, relações, conhecimento, jornada e cargo têm funções distintas. M11 precisa demonstrar como o iniciante consegue equipamento e SP com ações já acessíveis, sem exigir a técnica bloqueada para cumprir sua própria aquisição. SP não foi aprovado como moeda de qualquer comércio.

**Impacto:** nossa futura implementação de Mentor/Proficiências precisa aplicar essa separação. A pendência com a equipe continua nas fichas e integração; não revogar a compra de skills/formas nem transformar Mastery em compra por simplificação.

### Quatro origens com começo próprio e presença espiritual

[M04](../game-design/04-racas-e-origens.md) e [M04a](../game-design/04a-presenca-espiritual-e-interacao.md): personagem único por conta em cada mundo/servidor nesta etapa; múltiplos personagens adiados. Reset via NPC original, gratuito no recorte aprovado, confirmado e seguro, recomeça a jornada Bleach e preserva itens comuns/XP vanilla conforme contrato. Não é migração já implementada nem desbloqueio de todas as formas após reset.

As quatro origens iniciam no Mundo Humano em estados próprios. Quincy/Fullbringer recebem visão espiritual básica no prólogo antes do primeiro combate que a exija; isso não compra leitura avançada de Reiatsu. Ver, detectar, poder atingir e ter permissão de dano são verificações distintas.

RAC-30B/D exige capacidade espiritual do ataque contra corpos espirituais, inclusive jogadores. Arma de ferro/flecha comum não ganha essa capacidade apenas pela origem ou visão do usuário. Vanilla mantém percepção/interação ordinárias; isso não autoriza automaticamente seu dano comum contra um corpo espiritual. Quincy/Fullbringer em corpo humano material continuam sujeitos a dano comum. Queda, lava, explosões/componentes mistos e demais riscos exigem seus recortes; não generalizar imunidade ambiental.

**Impacto:** futuras armas de treino, ataques naturais Hollow e NPCs precisam de classificação coerente. Esse requisito muda a ameaça de mobs vanilla e exige auditoria/balanceamento próprios antes de declarar sobrevivência integrada. Nenhum filtro de dano foi modificado nesta revisão.

### Combat State com significado de luta

[M06a](../game-design/06a-estado-de-combate.md): estado automático autorizado pelo servidor, separado da Combat Hotbar. Ataque válido bloqueado/esquivado, controle hostil e suporte efetivo podem envolver o participante sem perda de Vida. Estar na party ou abrir a interface não inicia confronto por si. Golpe incompatível com o corpo não vira ameaça só pela animação.

Perigo ambiental sem origem hostil é separado do combate e ainda pode impedir viagem/reset/evolução. Sair exige janela sem conflito, efeitos e ameaça efetiva, respeitando compromisso de encontro. A janela restante persiste em morte/logout/transição legítima pelo tempo de simulação; mundo parado não a avança. Isso não restaura ataques, formas ou tentativas de boss interrompidas.

**Impacto:** guarda/esquiva homologadas precisam ser integradas ao contrato futuro sem alterar seus valores agora. Portais, atendimento e recuperação dependerão desse estado; um simples temporizador de último dano não representa toda a regra. A conformidade completa do runtime não foi auditada aqui.

### NPCs, ensino e instituições

[M08a](../game-design/08a-catalogo-funcional-de-npcs-e-faccoes.md),9.31–9.40: aprovadas quatro fontes originais para fundamentos, com repertório inicial selecionado. Nomes/visuais/exercícios e parte da operação ainda por ficha:

- Instrutor Shinigami: formação, prática básica e Shunpo por preparo/compra; shinai antes de Asauchi, sem concessão automática de Shikai.
- Veterano Hollow independente: corpo, risco, caça e devoração; não ensina Cero por omissão. Cero aparece a partir de Gillian sob sua aquisição própria.
- Mentor Quincy independente: formação do arco e depois Hirenkyaku; foco físico necessário ao uso, sem exigir filiação ao império ou conceder Schrift/Vollständig.
- Orientador Fullbringer: fundamentos e Bringer Light horizontal, seguido de salto apoiado na sequência didática; não concede o poder principal do professor.

Rukia/Renji, Uryū, Chad e Nel entram como contatos contextualizados. Gotei/divisões, Onmitsukidō, Corpo de Kidō, Pesquisa e Desenvolvimento, redes Quincy, Xcution e rede Urahara têm papéis diferentes; não formam uma escola única de todos os poderes. Facção/cargo não equivale a BP, origem ou autorização de PvP.

Atendimento essencial protegido e representação de combate são distintos. Disponibilidade por era e substituição funcional precisam de ficha; não manter todo canônico vivo e disponível em todas as fases. O inventário narrativo documentado de36 entradas/41 IDs é evidência datada do catálogo, não recontagem técnica atual nem36 serviços prontos.

**Impacto:** a frente futura de NPCs precisa entregar atividade, acesso, recusa e continuidade, além de skin e diálogo. A presença de Urahara/Aizen no planejamento institucional não reabre kits fora da nossa prioridade: Aizen poder permanece adiado.

### Hollows e encontros

[M09](../game-design/09-mobs-bosses-e-encontros.md) e [M09a](../game-design/09a-catalogo-funcional-de-adversarios-e-encontros.md): amostra funcional de bruto lento/amplo, ágil com pressão curta/reposicionamento, voador com janela de resposta próxima ao solo e chefe opcional preparado. Identidade, natureza e estágio não são definidos pelo tamanho do modelo ou rótulo boss.

Ataques têm antecipação, compromisso direcional e recuperação; detecção não concede informação secreta, navegação através de obstáculos ou acerto através de parede. Não alterar blocos por padrão. Predação/sentidos não recebem personalidade pela anatomia. Aldeões como civis vivos caçáveis foram aprovados como adaptação, preservando atendimento essencial; isso não classifica pets/fauna/golems como presas automaticamente.

Contribuição útil, recompensa e devoração têm contratos separados. Uma presa tem um consumidor elegível a concluir, sem reserva/rodízio garantidos. Party não distribui automaticamente alma, loot ou SP. Reinício/descarga interrompe encontro dirigido conforme a ficha; não presumir restauração completa do boss ou prêmio.

**Impacto:** a equipe dos Hollows precisa fechar sensores/alvos/geometrias e oportunidades de resposta antes de chamar os três ritmos de IA concluída. A [revisão09b](../game-design/09b-revisao-m09-2026-10-23.md) mantém parâmetros, regiões, recompensa e conteúdo do encontro como entregas reais, não apenas detalhes cosméticos.

### Mundos que sustentam atividades

[M10](../game-design/10-dimensoes-locais-e-estruturas.md) e [M10b](../game-design/10b-catalogo-funcional-de-lugares-e-rotas.md): entorno de Karakura, prática/caça humana, Rukongai, Academia, refúgios e regiões de caça em Hueco Mundo constituem a amostra de seis necessidades funcionais; não fecham todo o horizonte geográfico.

Rukongai tem abastecimento, proteção de pessoas/percursos e apuração de ocorrências com moradores. Academia e instituições oferecem formação/serviços; refúgios de Hueco Mundo têm apoio, abastecimento e retorno. São razões para circular e voltar, com conteúdo concreto ainda por escolher. Las Noches não é hub neutro universal. Menção a Palácio Real/WahrWelt não aprova nova dimensão ou construção imediata.

Viagem comum é breve e fora de combate; fontes/operadores iniciais e skills próprias são caminhos distintos. Primeiro acesso a Hueco Mundo segue missão/contexto, sem um estágio racial universal imposto por esta revisão. Destino próprio exige conhecimento/registro e acesso válido; chegada, corpo grande, convidados autorizados e retorno depois de fechar a passagem precisam ser demonstrados.

Hueco Mundo mantém noite permanente, água transportada funcional como adaptação e sono compatível a qualquer horário no recorte aprovado. Renascimento considera cama/abrigo corporal, busca próxima e alternativas de recuperação. Ambiente restante de Soul Society, espaços concretos e implantação em mundos novos/existentes continuam entregas. O protótipo History Mode/mapas/itens TP não comprova a implementação do contrato completo de passagem.

### Itens, quests e campanha: próximo elo

O [handoff](../game-design/00-handoff-atual.md) manda abrir M11 com retorno M05: comida/fome/regeneração e recursos ao renascer; armamento/instrumentos espiritualmente compatíveis; foco Quincy; obtenção/reposição; fronteira SP/comércio. Não há catálogo de receitas/preços finais aprovado nesta consulta.

Depois, M12 define objetivos, estados, falhas e recompensas; M13 transforma isso em campanhas e sidequests; M14 apresenta requisitos/feedback; M15 resolve cooperação/escala/catch-up; M16 organiza migração, operação, testes e entregas. A arquitetura data-driven existente pode ser ponto de partida, mas a existência do framework não preenche as campanhas raciais.

## 4. Melhoria prática recomendada, preservando a ordem vigente

**Enquanto o polimento de poderes continua:** a equipe pode fechar a especificação M11/M05 e converter as primeiras rotas já aprovadas em fichas funcionais. Cada ficha deve responder quem oferece, o que o jogador faz, quais requisitos consegue cumprir, o que recebe e como continua. Isso é preparação documental, sem puxar NPCs/quests para antes da nossa lista obrigatória.

**Depois da lista obrigatória:** recomendo entregar um percurso jogável coerente, começando pelo recorte Shinigami já mais maduro: obtenção de instrumento compatível, fonte de formação, uma atividade contra ameaça legível, aquisição com SP e prática posterior, acesso/retorno e feedback. Esta é recomendação de produção, não uma nova quest, recompensa, preço ou sequência narrativa aprovada. As outras origens precisam de percursos próprios sem serem versões simétricas dessa rota.

Critérios de qualidade futuros: iniciante não precisa da skill bloqueada para comprá-la; um mentor indisponível tem continuidade funcional; armas/ataques declaram compatibilidade; encontro oferece resposta/retirada; recompensa não duplica em morte/logout; viagem tem volta viável; personagem/saves existentes conservam conquistas na migração. Fórmulas e conteúdo compatíveis não precisam de nova votação dos contratos gerais.

## 5. Ajustes documentais e técnicos identificados

1. **Fonte de verdade e navegação:** iniciar pelo handoff atual/checkpoint12.1. Auditoria20/10 e encaminhamentos anteriores conservam estados históricos superados. Produzir sínteses vigentes curtas e links por decisão é mais útil que reler todas as perguntas. Preservar IDs/respostas/histórico.
2. **Planejamento versus runtime de atributos:** M05 tem cinco atributos fundamentais; `AttributeData.IDS` no checkout atual ainda declara sete categorias: zanjutsu, hakuda, vitality, resistance, kidou, reserve, control. A migração e suas relações com proficiências precisam de contrato explícito. Não apagar campos ou reabrir stamina/Resistência homologadas por uma revisão documental.
3. **Cabeçalhos técnicos datados:** arquitetura01 ainda apresenta atualização03/10 com protocolo2.3/schema3; fonte atual `Reference.NETWORK_PROTOCOL` é2.35 e `PlayerData.save` grava schema9. A série também contém engenharia reversa DMZ deliberadamente histórica. Corrigir a síntese de runtime sem tratar a referência DMZ inteira como errada.
4. **Links portáveis:** varredura dos Markdown GDD ativos encontrou cinco referências absolutas para `/Users/tarsopassos/...` em `00-handoff-para-outro-chat.md`, linhas40–44. É handoff antigo; o atual deve prevalecer. Verificação de destinos não valida todas as âncoras Markdown. Correção futura pode usar caminhos relativos sem reescrever decisões.
5. **Maturidade dos catálogos:** “proposta”, “decidido”, “especificado conceitualmente”, “implementado”, “testado” e “aceito” não são sinônimos. A próxima revisão precisa fechar função/acesso/consequência e apontar responsáveis, sem declarar que só faltam números ou modelos.
6. **Eficiência/limpeza:** o volume documental inclui milhares de imagens QA. Planejar organização e retenção de evidências a partir de inventário, sem deletar históricos, WAVs ou worktrees por tamanho. Nossa pausa do C em terreno continua prioridade medida do item8, independentemente desta revisão.

## 6. Commit local de áudio da main

84738ae versiona as fontes WAV selecionadas em `tools/audio/source`, manifesto/verificador e caminhos relativos nos importadores Benihime/Hyorin. São11 fontes atuais selecionadas (68.665.666bytes no documento), incluindo uma já existente, sem copiar vídeos ou MP3s correspondentes. O jogo continua usando OGGs; os importadores não foram reexecutados para sobrescrevê-los.

Fonte da leitura: `main:Docs/poderes/audios-fontes-versionadas-2026-10-26.md` e diff84738ae. Trata-se de portabilidade/preservação de autoria, não atualização automática de música, gameplay ou integração das features recentes na main. Commit permanece local no checkout main nesta consulta. Nenhum áudio original foi eliminado ou importado nesta rodada.

## 7. Nossa lista obrigatória, com o feedback mais recente

1. Entrada Benihime: validada; preservar.
2. Higashi: revisão final do usuário continua registrada como pendente.
3. Ryūjin: dash/rajada aprovados; usuário aprovou a transformação curta como base. Antigravity prepara acréscimos de cinzas/fumaça em espiral e fogo sob os pés, preservando12s, câmera e áudio.
4. Benihime combate: usuário aprovou a movimentação atual. Kit/alternativa à boneca selada e revisão transversal112 não se encerram por essa aprovação parcial. Senbon combate foi reprovado na sensação; Antigravity recebeu direção de uma mão, diagonal descendente → retorno horizontal → estocada, com prova por golpe. Vínculo/rastro permanece alvo de integração.
5. Uryū Ishida: contrato Quincy com a equipe antes de implementar. M08/M11 ajudam a separar mentor, formação do arco, foco e kit canônico.
6. Mentor/Proficiências: implementação continua adiada para a equipe; aproveitar os contratos já decididos sem repetir perguntas gerais.
7. Hyorin: melhorias de modelo/rig/transformação no Blockbench conforme planejamento, sem reabrir agora o recorte aprovado.
8. Eficiência/limpeza: mantida, com inventário seguro e pendência de desempenho do C.

Após a lista: NPCs de combate e quests de lore, agora apoiados pelos contratos M08–M13. Aizen poder adiado, Gin fora do nosso planejamento de poderes, Zaraki preservada. Citar Aizen/Gin como personagens/contexto narrativo no GDD da equipe não reativa automaticamente seus kits nesta frente.

## 8. Encerramento e limites

Sem runtime, builds, GameTests, abertura do Minecraft, instalação, merge ou publicação nesta revisão. Somente documentação própria em Docs/poderes e cache de leitura em build. Os documentos fonte da main e o trabalho Antigravity ficaram intactos. Recomendações de ordem/fatia/migração são recomendações, não decisões novas do usuário. Se a equipe publicar novos commits depois deste snapshot, atualizar a comparação antes de tratá-los como revisados.
