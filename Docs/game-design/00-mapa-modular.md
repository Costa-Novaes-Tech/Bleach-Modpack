# Mapa modular do game design

> **Direção vigente REV-SP-02 — 22/10/2026:** Spiritual Points compram **skills e transformações**, conforme esclarecimento explícito do usuário, usando Dragon Mine Z como referência para a compra. **Mastery é desenvolvida pela prática e não é comprada.** Requisitos/contexto disponibilizam a aquisição; SP paga a compra/desbloqueio; usar a capacidade depois segue os custos próprios de Energia/Stamina. [Contrato comum em M05/11.2](05-atributos-e-recursos.md#112-rev-sp-02--compra-de-skills-e-transformacoes-com-spiritual-points). Preços e exceções concretas permanecem nas fichas; a compra não pode ser removida genericamente por interpretação de M05/M06.

**Última revisão de continuidade:** 22/10/2026. Revisão de M01–M07 concluída no recorte documental descrito no [relatório transversal](00-revisao-modulos-01-a-07-2026-10-22.md); M04 agora consolidado conceitualmente, com criação/continuidade e dependências explícitas. M08 consolidado conceitualmente. Checkout atualizado; nenhuma implementação nova dos contratos GDD ou teste/aceite nesta revisão.

**Discussão atual:** M08 consolidado conceitualmente; próximo M09 — Mobs, bosses e encontros. M08-04C/D aprovados, fechamento e fronteiras em [M08/15](08-npcs-mentores-e-faccoes.md#15-próximos-passos). [Implementação e lista de assets reconciliadas](00-reconciliacao-implementacao-assets-2026-10-22.md) após atualização para `bb26be8`; conteúdo candidato não altera prioridades por omissão. Sem novos testes locais.

## Propósito deste documento

Este documento organiza o planejamento do Bleach Mod em módulos independentes. Ele não especifica todos os sistemas e não declara nenhuma ideia como implementada. Sua função é definir:

- quais assuntos terão documentos próprios;
- o que pertence e o que não pertence a cada módulo;
- quais decisões precisam ser tomadas antes de outras;
- como impedir que storyline, quests, NPCs e progressão se tornem um único sistema acoplado;
- como avançar um módulo por vez sem perder a visão do jogo completo.

Os documentos de `Docs/arquitetura/` continuam descrevendo principalmente a engenharia reversa do Dragon Mine Z e a implementação técnica atual. Os documentos de `Docs/game-design/` descreverão a experiência desejada para o Bleach Mod.

Referência de preparação: [Base de conhecimento — Bleach dentro de Minecraft](00-base-conhecimento-minecraft-bleach.md), pesquisada em 20/10/2026, reúne fontes oficiais, versões do projeto e revisões entre módulos. Ela complementa este mapa e não aprova novas decisões de produto.

Para retomar, usar o [handoff vigente](00-handoff-atual.md). A skill do projeto é [bleach-game-design](../../.agents/skills/bleach-game-design/SKILL.md); as referências externas foram adaptadas ao método deste mapa.

Entrada adicional de 20/10/2026: [reconciliação do handoff externo Hollow](00-reconciliacao-handoff-hollow-2026-10-20.md). Recupera discussões de outra sessão sem sobrescrever IDs. Após o parecer, o usuário aprovou a direção em M06-08G e escolheu Grimmjow como primeira linhagem; fan arts podem orientar formas faltantes, com procedência identificada. Requisitos concretos de evolução permanecem em M07.

O usuário esclareceu depois que o detalhamento específico das linhagens e das artes fica para uma etapa posterior. Continuar os sistemas gerais e a sequência modular; Grimmjow será o primeiro caso quando chegar a etapa de conteúdo específico.

## Regra central de trabalho

Cada etapa deve tratar apenas um módulo principal. Outros módulos podem ser citados como dependências, mas suas decisões não serão fechadas antecipadamente.

Exemplo: o documento de NPCs pode decidir que mentores oferecem treinamento e quests. Ele não deve definir todas as quests, fórmulas de atributos ou habilidades ensinadas. Esses detalhes pertencem aos módulos correspondentes.

## Estados permitidos

Cada documento de módulo deve mostrar um destes estados no início:

| Estado | Significado |
|---|---|
| `NÃO INICIADO` | O módulo está apenas listado neste mapa. |
| `IDEALIZADO` | Existem possibilidades e referências, mas as decisões continuam abertas. |
| `DECIDIDO` | As principais decisões de produto foram aceitas. |
| `EM ESPECIFICAÇÃO` | Há blocos em elaboração e decisões ainda abertas, identificados no documento. |
| `ESPECIFICADO CONCEITUALMENTE` | A arquitetura está definida; fórmulas, calibração ou tradução para implementação continuam explicitamente delegadas. |
| `ESPECIFICADO` | Regras, dados, integrações e critérios estão definidos para implementação. |
| `IMPLEMENTADO` | O código ou conteúdo correspondente existe. Não significa que foi testado. |
| `TESTADO` | Há evidência técnica de funcionamento nos cenários definidos. |
| `AUDITADO` | Houve revisão com escopo e resultados registrados; não substitui teste ou aceite. |
| `ACEITO` | O resultado foi avaliado e aprovado como experiência de jogo. |
| `PENDENTE` | Existe uma dependência ou decisão conhecida impedindo o próximo estado. |

Um módulo pode ter partes em estados diferentes. Nesse caso, o documento deve separar claramente cada parte. Implementação, teste e aceitação nunca avançam automaticamente juntos.

## Separação entre sistema e conteúdo

Para evitar acoplamento, cada domínio será analisado em duas camadas:

| Camada | Pergunta |
|---|---|
| Sistema | Quais regras e ferramentas permitem criar esse tipo de conteúdo? |
| Conteúdo | Quais personagens, inimigos, lugares, técnicas e missões existirão? |

Exemplos:

- O sistema de NPCs define diálogo, rotina, interação e treinamento. Rukia e Urahara são conteúdo de NPC.
- O sistema de mobs define IA, atributos, fases e recompensas. Hollow comum e Menos Grande são conteúdo de mob.
- O sistema de quests define objetivos, pré-requisitos e estados. “O Nome que Faltava” é conteúdo de campanha.
- O sistema de dimensões define viagem, geração e persistência. Soul Society e Hueco Mundo são conteúdo de mundo.
- O sistema de transformações define desbloqueio, ativação, custo e domínio. Shikai e Bankai são conteúdo Shinigami.

## Módulos de game design

### Fundação

| Ordem | Documento futuro | Responsabilidade | Não decide |
|---:|---|---|---|
| 01 | [`01-visao-e-pilares.md`](01-visao-e-pilares.md) | Fantasia do jogador, identidade do projeto e princípios de design | Números, quests ou catálogo de conteúdo |
| 02 | [`02-trilhas-de-progressao.md`](02-trilhas-de-progressao.md) | Todas as formas de progresso e a relação entre elas | Fórmulas detalhadas de cada sistema |
| 03 | [`03-storyline-macro.md`](03-storyline-macro.md) | Ordem dos grandes arcos e papel do personagem original no cânone | Quests individuais e diálogos completos |

### Personagem e poder

| Ordem | Documento futuro | Responsabilidade | Não decide |
|---:|---|---|---|
| 04 | [`04-racas-e-origens.md`](04-racas-e-origens.md) | Shinigami, Hollow, Quincy, Fullbringer, híbridos e origem do personagem | Todas as transformações de cada raça |
| 05 | `05-atributos-e-recursos.md` | Atributos, reiatsu, vida, stamina, crescimento e Battle Power | Técnicas e formas específicas |
| 06 | `06-habilidades-e-combate.md` | Zanjutsu, Hakuda, Hohō, Kidō, técnicas ativas, passivas e combate | Quando a storyline concede cada habilidade |
| 07 | [`07-transformacoes-e-dominio.md`](07-transformacoes-e-dominio.md) | Estágios, despertar, mastery, ativação, custo, manutenção e reversão | Campanhas completas de desbloqueio |

### Habitantes e ameaças

| Ordem | Documento futuro | Responsabilidade | Não decide |
|---:|---|---|---|
| 08 | [`08-npcs-mentores-e-faccoes.md`](08-npcs-mentores-e-faccoes.md) | NPCs, diálogo, serviços, mentores, reputação, esquadrões e facções | Objetivos completos das campanhas |
| 09 | `09-mobs-bosses-e-encontros.md` | Famílias de inimigos, IA, tiers, bosses, fases e encontros | História de cada boss dentro da campanha |

### Mundo

| Ordem | Documento futuro | Responsabilidade | Não decide |
|---:|---|---|---|
| 10 | `10-dimensoes-locais-e-estruturas.md` | Mundo Humano, Soul Society, Hueco Mundo, Garganta, Senkaimon e estruturas | Lista completa de quests de exploração |
| 11 | `11-itens-e-economia.md` | Equipamentos, materiais, crafting, recompensas, moedas e recuperação de itens | Progressão narrativa e balanceamento final de combate |

### Orquestração da experiência

| Ordem | Documento futuro | Responsabilidade | Não decide |
|---:|---|---|---|
| 12 | `12-sistema-de-quests.md` | Tipos de objetivo, estados, falhas, grupos, NPCs, encontros e recompensas | Enredo e texto de campanhas específicas |
| 13 | `13-campanhas-e-sidequests.md` | Prólogo, Soul Society e demais campanhas transformadas em quests | Regras internas dos sistemas utilizados |
| 14 | `14-interface-e-feedback.md` | Diário, HUD, mapa, diálogos, tutoriais e comunicação de progresso | Autoridade do servidor e persistência técnica |
| 15 | `15-multiplayer-e-balanceamento.md` | Party, escala, propriedade de encontros, PvE/PvP e cooperação narrativa | Infraestrutura externa sem necessidade demonstrada |

### Entrega e evolução

| Ordem | Documento futuro | Responsabilidade | Não decide |
|---:|---|---|---|
| 16 | `16-integracao-testes-e-roadmap.md` | Dependências finais, fatias jogáveis, testes, migrações e ordem de implementação | Novas funcionalidades fora das decisões aprovadas |

## Relações entre os módulos

```mermaid
flowchart TD
    V[Visão e pilares] --> P[Trilhas de progressão]
    V --> S[Storyline macro]

    P --> R[Raças e origens]
    P --> A[Atributos e recursos]
    R --> H[Habilidades e combate]
    A --> H
    R --> T[Transformações e domínio]
    A --> T
    H --> T

    S --> N[NPCs, mentores e facções]
    S --> M[Mobs, bosses e encontros]
    S --> D[Dimensões, locais e estruturas]

    H --> Q[Sistema de quests]
    T --> Q
    N --> Q
    M --> Q
    D --> Q
    E[Itens e economia] --> Q

    S --> C[Campanhas e sidequests]
    Q --> C
    C --> U[Interface e feedback]
    C --> MP[Multiplayer e balanceamento]
    U --> I[Integração, testes e roadmap]
    MP --> I
```

O diagrama representa dependências de decisão, não uma ordem rígida de programação. A storyline macro aparece cedo para dizer quais experiências serão necessárias. As campanhas detalhadas aparecem somente depois que os sistemas capazes de sustentá-las estiverem definidos.

## Contratos de fronteira

Estas regras devem permanecer válidas durante todo o planejamento:

1. **Storyline não concede poder diretamente.** Ela registra acontecimentos e autorizações; transformações e habilidades aplicam as regras de desbloqueio.
2. **Quests não calculam combate.** Elas observam eventos e pedem resultados ao sistema responsável.
3. **NPCs não alteram dados livremente.** Mentores solicitam operações aos serviços de progressão, habilidades e quests.
4. **Mobs não conhecem campanhas específicas.** Encontros aplicam configurações fornecidas pelo sistema de quests ou pelo mundo.
5. **Raça não é transformação.** Raça define natureza, compatibilidades e repertórios disponíveis; cada transformação tem seu próprio contrato. Isso não recupera skill tree como modelo principal, superado em M06-07.
6. **Dimensão não é storyline.** Lugares podem existir fora de uma campanha; a storyline apenas controla quando e por que o jogador os acessa.
7. **Aquisição, requisitos e domínio têm funções distintas.** REV-SP-02/M05-11.2: Spiritual Points compram skills e transformações, além dos atributos. Requisitos de acesso, vínculo e provas disponibilizam a compra; SP não os ignora. Mastery cresce pela prática. Compra persistente não se repete por ativação; Energia/Stamina custeiam o uso conforme a capacidade. Preços e exceções concretas exigem ficha.
8. **Mastery representa uso e domínio.** Não deve ser apenas outra moeda comprável.
9. **UI informa, não autoriza.** Regras decisivas continuam no servidor.
10. **Conteúdo canônico e adaptação Minecraft devem ser identificados.** Quando uma mecânica não vier diretamente da obra, o documento deverá marcá-la como adaptação de game design.

## Template obrigatório para cada módulo

Cada novo documento deverá usar esta estrutura:

1. **Estado do módulo** — estado atual, responsável pela decisão e última revisão.
2. **Objetivo** — qual problema do jogo este módulo resolve.
3. **Fantasia do jogador** — o que o jogador deve sentir e conseguir fazer.
4. **Base canônica** — conceitos de Bleach usados e fontes consultadas.
5. **Adaptação para Minecraft** — mudanças necessárias para funcionar como jogo aberto e multiplayer.
6. **Escopo** — o que pertence e não pertence ao módulo.
7. **Modelo conceitual** — entidades, estados e relações sem exigir código.
8. **Regras principais** — comportamentos e invariantes.
9. **Integrações** — entradas e saídas para outros módulos.
10. **Conteúdo inicial** — somente a menor amostra necessária para validar o sistema.
11. **Visão completa** — possibilidades futuras sem tratá-las como compromisso imediato.
12. **Decisões abertas** — alternativas, custos, riscos e recomendação.
13. **Critérios de aceitação** — como saber se a experiência ficou correta.
14. **Impacto técnico conhecido** — relação com o código e a documentação atuais.
15. **Próximos passos** — o que pode começar depois da aprovação.

## Método para trabalhar módulo por módulo

Para cada módulo:

1. Ler integralmente as fontes atuais relacionadas.
2. Pesquisar o material canônico relevante.
3. Registrar fatos, adaptações e ideias em seções separadas.
4. Apresentar as decisões que realmente mudam o produto.
5. Comparar alternativas e fazer uma recomendação explícita.
6. Obter a decisão antes de escrever uma especificação fechada.
7. Definir a menor fatia jogável do módulo.
8. Mapear dependências e riscos técnicos.
9. Só então autorizar implementação.
10. Manter implementação, teste e aceitação como gates separados.

### Contratos comuns, perfis e exceções — método vigente em 22/10/2026

Por solicitação do usuário, consolidar contratos repetidos em uma fonte comum, com cobertura e aprovações rastreáveis. Cada novo bloco apresenta um pacote: referências herdadas, perfil/natureza, diferenças reais, exceções justificadas, pendências com responsáveis e critérios. Perguntar somente novas escolhas de produto, ampliações de abrangência, consequências ou conflitos; não votar novamente a mesma regra por raça, evento de sessão ou detalhe de implementação. Exemplo vigente em M07/seções 7.1/7.2.

Preservar registros de respostas/IDs/datas e diferenças aprovadas. Generalizar a redação não aprova automaticamente nova mecânica: expansão realmente necessária é proposta uma vez para a classe de perfis cobertos; após aprovação, suas fichas a referenciam. Fichas técnicas e pesquisa canônica seguem exigidas onde pertinentes, sem inventar parâmetros ou uniformizar custos/conquistas/retornos. Exceção identifica qual contrato altera, motivo e consequência antes de ser adotada. Implementação, teste e aceite continuam gates separados.

## Ordem recomendada para começar

O primeiro módulo deve ser **Visão e pilares** (`01-visao-e-pilares.md`). Ele fechará a fantasia central do jogador, sua relação com os personagens canônicos e os princípios que diferenciam este projeto de uma coleção de conteúdo de Bleach.

Em seguida vem **Trilhas de progressão** (`02-trilhas-de-progressao.md`), porque NPCs e quests precisam saber o que ensinam, desbloqueiam e avaliam. Nenhum dos dois documentos definirá números finais, catálogo completo de NPCs ou campanhas inteiras.

Depois das trilhas, a sequência recomendada é:

1. Visão e pilares.
2. Trilhas de progressão.
3. Storyline macro, apenas no nível dos grandes arcos.
4. Raças e origens.
5. Atributos e recursos.
6. Habilidades e combate.
7. Transformações e domínio.
8. NPCs, mobs e dimensões, um documento por vez.
9. Sistema de quests.
10. Campanhas detalhadas.
11. Interface, multiplayer e roadmap de integração.

## Estado deste mapa e dos módulos — atualização em 22/10/2026

O mapa permanece `DECIDIDO` quanto à divisão modular e ao método. A leitura deve considerar as revisões explícitas entre os documentos.

| Módulo | Estado documental vigente | Continuidade |
|---|---|---|
| 01 — Visão e pilares | `DECIDIDO` | Preservar D01–D06 e observar as revisões explícitas posteriores sobre progressão. |
| 02 — Trilhas de progressão | `DECIDIDO` arquiteturalmente | Aplicar `REV-M02-01` a `REV-M02-05` de M05 e os refinamentos de M06. |
| 03 — Storyline macro | `DECIDIDO` arquiteturalmente | Campanhas concretas e políticas detalhadas de multiplayer continuam nos módulos próprios. |
| 04 — Raças e origens | `ESPECIFICADO CONCEITUALMENTE` nesta etapa | RAC-26, REV-RAC27-01 e RAC-28A/B consolidam criação/continuidade: personagem único e reset NPC; múltiplos personagens futuros. Fronteiras e condições de reabertura no quarto bloco; conteúdo e migração executável pendentes. |
| 05 — Atributos e recursos | `ESPECIFICADO CONCEITUALMENTE` | Calibração, fórmulas e conciliação com o código continuam pendentes. |
| 06 — Habilidades e combate | `ESPECIFICADO CONCEITUALMENTE` | Base universal, quatro caminhos e integração consolidados; A–Y aprovados. Seção 48 registra fichas/integrações pendentes e condições de retorno; M07 também concluído conceitualmente, continuidade principal em M08. |
| 07 — Transformações e domínio | `ESPECIFICADO CONCEITUALMENTE` | Fullbringer M07-09A–C aprovado, C em 8.95; caminho consolidado estruturalmente. Revisão comum e fechamento conceitual de M07 na seção 15, sem perguntas gerais pendentes. M07 ESPECIFICADO CONCEITUALMENTE; próximo M08 — NPCs, mentores e facções. Valores, fichas de poder/corpo/item/prova/recuperação, efeitos e integração/validação permanecem com responsáveis; nenhuma implementação/teste/aceite. |
| 08 — NPCs, mentores e facções | `ESPECIFICADO CONCEITUALMENTE` | M08-01A/B, M08-02A, M08-03A e M08-04A–D decididos; revisão de integração em §15, entregas em §12.1. Fichas/serviços/regras de execução pendentes. Próximo M09, partindo das variantes/boss atuais. |
| 09–16 | `NÃO INICIADOS` como documentos próprios | Dependências e código relacionado não equivalem a especificação ou aceite. |

**Continuidade vigente:** M04 e M08 consolidados conceitualmente; próximo M09, ainda sem documento próprio. A sequência abaixo registra o fechamento anterior de M07 e conserva seu contexto histórico; a informação de que M08 não foi iniciado naquele momento está superada pelo registro vigente acima.

[M07 — Transformações e Domínio](07-transformacoes-e-dominio.md) está especificado conceitualmente; o próximo documento principal é M08 — NPCs, mentores e facções. Shikai, Bankai, evolução Hollow e Arrancarização mantêm seu fechamento estrutural. Resurrección A–Q consolidada estruturalmente em 21/10/2026: dezessete decisões aprovadas, nenhuma pergunta pendente. Q encerra preparo/manifestação no ciclo de vida, conservando condição e conquistas, com retorno manual; efeitos e tempos seguem os contratos herdados. Revisão, entregas/responsáveis e condições de retorno na seção 15; panorama geral na seção 12.3. Fullbringer A–C consolidado estruturalmente e revisão comum de M07 concluída no nível conceitual; fichas e realização executável continuam pendentes. Fichas, valores, conteúdo, corpos/poses, intervenções do mundo, arma/inventário e evidência técnica continuam pendentes; não herdar regras de outra raça por associação. M05/M06 mantêm pendências conceituais; M08–M16 ainda não iniciados como documentos próprios. Aprovações não equivalem a implementação/teste/aceite; Grimmjow e artes continuam adiados. **Continuidade vigente de M07:** Fullbringer M07-09A–C aprovado, C em 8.95; caminho consolidado estruturalmente. Revisão comum e fechamento conceitual de M07 na seção 15, sem perguntas gerais pendentes. M07 ESPECIFICADO CONCEITUALMENTE; próximo M08 — NPCs, mentores e facções. Valores, fichas de poder/corpo/item/prova/recuperação, efeitos e integração/validação permanecem com responsáveis; nenhuma implementação/teste/aceite. Aprovações gerais de 22/10/2026 abrangem ciclo de vida e passagem/retorno. Evoluções permanentes e consequências especiais preservadas. O fechamento das formas Quincy é estrutural; não declara todo o caminho racial ou suas fichas concluídos.
