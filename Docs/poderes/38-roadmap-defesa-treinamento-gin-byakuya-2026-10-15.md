# Ciclo 38 — Ordem escolhida: defesa, treinamento e novos kits

Data: 15/10/2026. Branch: `Feature-Poderes-bankais`. Entrega de planejamento, sem alterações de código.

## Decisão do usuário

Sequência escolhida após a pesquisa de personagens:

1. Bloqueio/Postura.
2. Guard Break.
3. Mentor.
4. Proficiências.
5. Gin Ichimaru.
6. Byakuya Kuchiki.

Bloqueio/Postura e Guard Break serão tratados como uma mesma frente, entregue em etapas: a barra precisa ter consequência funcional. Não considerar defesa concluída enquanto sua quebra ainda não existe. A esquiva do ciclo 37 continua aguardando o teste do usuário; não presumir aceite pelo avanço deste planejamento. Aizen permanece reservado e adiado, inclusive qualquer Bankai original. Os demais personagens pesquisados não entraram na fila escolhida.

## Entregas e critérios

| Ordem | Entrega proposta | Critério para encerrar a etapa |
| --- | --- | --- |
| 1 | Bloqueio frontal universal; Postura própria; indicação de guarda e pressão no HUD. Eficiência pode variar entre mão vazia e arma. | Direção, equipamento e tipo de ataque corretamente validados no servidor; ataques pelas costas e dano ambiental não entram por acidente na defesa. |
| 2 | Postura zero derruba a guarda e abre uma janela curta de vulnerabilidade; recuperação natural após cessar pressão. | Quebra previsível, recuperação legível, sem ciclos de interrupção infinita e sem cobrança automática dupla de stamina/Postura. |
| 3 | Mentor com uma cadeia curta: apresentar fundamento, executar treino verificável e receber recompensa uma vez. | Objetivos autoritativos, progresso salvo, retomada após reconectar e recompensa sem duplicação. Não depende de ter todas as disciplinas implementadas. |
| 4 | Estrutura persistida de Zanjutsu/Hakuda/Hohō/Kidō, progresso por prática relevante e apresentação na interface. | Isolamento entre disciplinas, anti-farm, migração sem apagar saves e distinção de atributos/maestria. Disciplinas sem conteúdo não ganham fontes artificiais de XP. |
| 5 | Kit de Gin: Shinsō e Kamishini no Yari, com alcance, precisão e veneno adaptados. | Contrato das formas/quatro slots, vínculo e maestria próprios; alvos/colisões corretos e contrajogo legível. |
| 6 | Kit de Byakuya: Senbonzakura e Senbonzakura Kageyoshi, com lâminas em padrões ofensivos/defensivos. | Mesmos critérios de identidade e progressão, orçamento visual controlado e nenhuma entidade criada para cada pétala. |

Cada etapa terá contrato específico, check/build, testes pertinentes e playtest. A ordem foi escolhida; números, teclas, recompensas e tabelas de habilidades abaixo ainda são propostas de escopo, não decisões já homologadas.

## Frente imediata — Bloqueio/Postura e Guard Break

Base: [M06-06A/B/C e subdivisões](../game-design/06-habilidades-e-combate.md), especialmente seções 48–55.

- Ação de segurar um controle remapeável para manter guarda frontal. Sugestão inicial: F, a conferir com controles do projeto e experiência em jogo antes da implementação. Não substituir automaticamente o uso vanilla de itens/escudos.
- Guarda universal, sem depender de qual zanpakutō foi vinculada. Mão vazia e arma podem ter eficiências distintas, sem transformar um novo kit em requisito para defesa básica.
- Servidor decide se a direção e a natureza do ataque permitem bloqueio. Definir tratamento de projéteis, explosões, fontes sem posição e ataques marcados como não bloqueáveis. Não aplicar uma redução genérica a todo dano recebido.
- Postura representa capacidade restante da guarda. Ataques bloqueados a reduzem; ausência de pressão permite recuperação. Não é atributo, XP, moeda, recurso de esquiva ou custo de técnicas.
- Não debitar stamina automaticamente a cada golpe bloqueado. Stamina segue corrida/esquiva/ataques físicos conforme seus contratos.
- Ao chegar a zero: interromper guarda, sinalizar a quebra e impedir nova guarda por uma abertura curta. Definir separadamente quais ações ficam disponíveis nessa janela; evitar stun total como efeito implícito.
- A integração deve decidir concorrência com esquiva, sprint, ataques, uso de itens, carga de transformação e técnicas ativas. Guard Break precisa ser considerado pela esquiva; não permitir que uma nova ação ignore uma restrição de controle sem decisão explícita.
- Parry não integra gratuitamente esta V1. Pode ser uma recompensa futura de treinamento, com contrato próprio.
- Antes do código: fechar tecla, cone frontal, mitigação, Postura máxima, pressão por tipo de golpe, pausa/taxa de recuperação, duração da abertura, efeitos sobre movimento e persistência em reconexão. Nenhum desses números está imposto pelo design-base.

## Mentor antes de Proficiências

A ordem é viável se o primeiro mentor ensinar fundamentos já existentes. Proposta: uma sequência curta de combate com espada, bloqueio e esquiva, usando objetivos que comprovem ações válidas, sem exigir número de proficiência ainda inexistente.

Reaproveitar NPCs/quests/recompensas existentes após inspecionar integrações da equipe; não criar NPC ou história duplicada. Nome do mentor, local, narrativa e recompensa precisam de definição própria. Não inventar aquisição narrativa das Bankais nesta etapa.

Na etapa seguinte, conectar o mentor às Proficiências: desafios e técnicas aprendidas podem exigir marcos apropriados. Não guardar créditos ocultos para conceder retroativamente XP sem contrato explícito.

## Proficiências sem reiniciar o personagem

Base: M06-07B/B.1/G/H/N/O/P e demais regras de disciplina no [M06](../game-design/06-habilidades-e-combate.md).

- Atributos indicam capacidade fundamental; proficiência indica experiência em uma disciplina; maestria indica domínio da técnica/forma. Não fundir as três progressões.
- Começar com infraestrutura das quatro disciplinas e fontes reais disponíveis. Zanjutsu/Hakuda podem usar combate válido; Hohō e Kidō dependem de práticas e conteúdo apropriados.
- Não conceder Hohō por segurar caminhada, nem Kidō por lançar poder elemental de zanpakutō. Esquiva universal não equivale automaticamente a Shunpo aprendido.
- Prática com técnica específica de uma zanpakutō não vira técnica geral de Zanjutsu só por utilizar espada. Categorizar créditos com critério explícito.
- Sem nível global, moeda de compra ou skill tree obrigatória. Sem XP ocioso, por apertar tecla ou por atacar indefinidamente um alvo irrelevante.
- Preservar atributos, vínculos, formas descobertas, maestria e quests existentes. Escala, fontes, limites, marcos e eventual migração de schema precisam de contrato e testes próprios.

## Gin e depois Byakuya

Base operacional: [contrato de novos poderes](contrato-criacao-novos-poderes.md). A escolha da fila não aprova antecipadamente toda técnica nem libera criação de Bankai original.

**Gin — proposta de adaptação:** focar na diferença entre espada curta e estocadas de alcance, com trajetória clara, colisões e oportunidades de errar. Bankai amplia esse repertório e pode permitir marca venenosa com ativação posterior. Veneno terá duração/dano/condições limitados; morte instantânea e alcance literal de quilômetros não são requisitos. Os quatro slots por forma serão definidos antes de implementar, separando referências canônicas de adaptações do mod.

**Byakuya — proposta de adaptação:** ondas de pequenas lâminas, cerco e padrões de defesa/ataque. Representar o conjunto por volumes de efeito e visuais com orçamento; não simular cada lâmina individualmente. Definir limites de alvos, frequência de dano e sobreposição. A defesa específica da Senbonzakura terá contrato próprio e não substituirá implicitamente o bloqueio universal.

Referências consultadas na pesquisa precedente: [Gin](https://bleach.fandom.com/wiki/Gin_Ichimaru), [Byakuya](https://bleach.fandom.com/wiki/Byakuya_Kuchiki). Essas fontes descrevem os poderes; as escolhas de adaptação e arquitetura acima são propostas para o projeto.

## Validação desta entrega

Somente documentação em `Docs/poderes`, com revisão de diff e links locais. Sem nova build ou execução de testes de código nesta etapa. Validação funcional mais recente continua sendo a do ciclo 37: 76 regressões e 67 GameTests obrigatórios aprovados; esquiva ainda sem homologação do usuário.
