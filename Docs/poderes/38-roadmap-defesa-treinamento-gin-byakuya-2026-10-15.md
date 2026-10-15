# Ciclo 38 — Ordem escolhida: defesa, treinamento e novos kits

Data: 15/10/2026. Branch: `Feature-Poderes-bankais`. Entrega de planejamento, sem alterações de código.

## Decisão do usuário

Sequência revisada pelo usuário após a pesquisa de personagens (atualização no ciclo 39):

1. Bloqueio/Postura e Guard Break juntos, com escudo visível que perde integridade e quebra.
2. Byakuya Kuchiki — próximo kit; usuário pesquisará os poderes antes de fechar o contrato.
3. Gin Ichimaru — permanece na fila, depois de Byakuya.
4. Mentor — por último, aguardando confirmação da equipe.
5. Proficiências — por último, aguardando confirmação da equipe.

Bloqueio/Postura e Guard Break foram implementados juntos no [ciclo 39](39-bloqueio-postura-guard-break-2026-10-15.md), para playtest. A esquiva do ciclo 37 também continua aguardando o teste do usuário; não presumir aceite pelo avanço deste planejamento. Aizen permanece reservado e adiado, inclusive qualquer Bankai original. Os demais personagens pesquisados não entraram na fila escolhida. Mentor e Proficiências não devem começar enquanto a confirmação da equipe estiver pendente.

## Entregas e critérios

| Ordem | Entrega proposta | Critério para encerrar a etapa |
| --- | --- | --- |
| 1 | Bloqueio frontal universal, Postura, escudo visível e quebra funcional. | Direção/tipo de ataque corretos, abertura previsível e sem cobrança dupla de stamina/Postura. Playtest pendente no ciclo 39. |
| 2 | Byakuya: Senbonzakura e Senbonzakura Kageyoshi. | Contrato por forma/slot após pesquisa do usuário; orçamento visual controlado e sem entidade para cada pétala. |
| 3 | Gin: Shinsō e Kamishini no Yari. | Contrato de alcance, precisão e veneno; vínculo/maestria próprios e contrajogo legível. |
| 4 | Mentor com cadeia curta de treino. | Confirmação da equipe antes de implementar; objetivos persistidos e recompensa sem duplicação. |
| 5 | Proficiências de Zanjutsu/Hakuda/Hohō/Kidō. | Confirmação da equipe antes de implementar; anti-farm e migração sem apagar saves. |

Cada etapa terá contrato específico, check/build, testes pertinentes e playtest. A ordem foi escolhida; números, teclas, recompensas e tabelas de habilidades abaixo ainda são propostas de escopo, não decisões já homologadas.

## Frente imediata — Bloqueio/Postura e Guard Break

Base: [M06-06A/B/C e subdivisões](../game-design/06-habilidades-e-combate.md), especialmente seções 48–55.

- Ação de segurar um controle remapeável para manter guarda frontal. Controle efetivo do ciclo 39: **X**; F foi descartado porque já troca itens entre mãos no vanilla. Não substituir o uso vanilla de itens/escudos.
- Escudo acima do personagem durante a guarda, com preenchimento/rachaduras conforme Postura diminui. Ao zerar, escudo vermelho partido, som e partículas de quebra; indicador em primeira pessoa. Visual ainda precisa de homologação em jogo.
- Guarda universal, sem depender de qual zanpakutō foi vinculada. Mão vazia e arma podem ter eficiências distintas, sem transformar um novo kit em requisito para defesa básica.
- Servidor decide se a direção e a natureza do ataque permitem bloqueio. Definir tratamento de projéteis, explosões, fontes sem posição e ataques marcados como não bloqueáveis. Não aplicar uma redução genérica a todo dano recebido.
- Postura representa capacidade restante da guarda. Ataques bloqueados a reduzem; ausência de pressão permite recuperação. Não é atributo, XP, moeda, recurso de esquiva ou custo de técnicas.
- Não debitar stamina automaticamente a cada golpe bloqueado. Stamina segue corrida/esquiva/ataques físicos conforme seus contratos.
- Ao chegar a zero: interromper guarda, sinalizar a quebra e impedir nova guarda por uma abertura curta. Definir separadamente quais ações ficam disponíveis nessa janela; evitar stun total como efeito implícito.
- A integração deve decidir concorrência com esquiva, sprint, ataques, uso de itens, carga de transformação e técnicas ativas. Guard Break precisa ser considerado pela esquiva; não permitir que uma nova ação ignore uma restrição de controle sem decisão explícita.
- Parry não integra gratuitamente esta V1. Pode ser uma recompensa futura de treinamento, com contrato próprio.
- Valores implementados como protótipo estão no relatório 39, sem tratá-los como balanceamento homologado.

## Mentor antes de Proficiências

**Adiado para o fim da fila: depende de confirmação do usuário com a equipe.** As ideias abaixo são propostas preservadas, não instrução para implementar agora.

A ordem é viável se o primeiro mentor ensinar fundamentos já existentes. Proposta: uma sequência curta de combate com espada, bloqueio e esquiva, usando objetivos que comprovem ações válidas, sem exigir número de proficiência ainda inexistente.

Reaproveitar NPCs/quests/recompensas existentes após inspecionar integrações da equipe; não criar NPC ou história duplicada. Nome do mentor, local, narrativa e recompensa precisam de definição própria. Não inventar aquisição narrativa das Bankais nesta etapa.

Na etapa seguinte, conectar o mentor às Proficiências: desafios e técnicas aprendidas podem exigir marcos apropriados. Não guardar créditos ocultos para conceder retroativamente XP sem contrato explícito.

## Proficiências sem reiniciar o personagem

**Adiado para o fim da fila: depende de confirmação do usuário com a equipe.**

Base: M06-07B/B.1/G/H/N/O/P e demais regras de disciplina no [M06](../game-design/06-habilidades-e-combate.md).

- Atributos indicam capacidade fundamental; proficiência indica experiência em uma disciplina; maestria indica domínio da técnica/forma. Não fundir as três progressões.
- Começar com infraestrutura das quatro disciplinas e fontes reais disponíveis. Zanjutsu/Hakuda podem usar combate válido; Hohō e Kidō dependem de práticas e conteúdo apropriados.
- Não conceder Hohō por segurar caminhada, nem Kidō por lançar poder elemental de zanpakutō. Esquiva universal não equivale automaticamente a Shunpo aprendido.
- Prática com técnica específica de uma zanpakutō não vira técnica geral de Zanjutsu só por utilizar espada. Categorizar créditos com critério explícito.
- Sem nível global, moeda de compra ou skill tree obrigatória. Sem XP ocioso, por apertar tecla ou por atacar indefinidamente um alvo irrelevante.
- Preservar atributos, vínculos, formas descobertas, maestria e quests existentes. Escala, fontes, limites, marcos e eventual migração de schema precisam de contrato e testes próprios.

## Byakuya e depois Gin

Base operacional: [contrato de novos poderes](contrato-criacao-novos-poderes.md). A escolha da fila não aprova antecipadamente toda técnica nem libera criação de Bankai original.

**Gin — proposta de adaptação:** focar na diferença entre espada curta e estocadas de alcance, com trajetória clara, colisões e oportunidades de errar. Bankai amplia esse repertório e pode permitir marca venenosa com ativação posterior. Veneno terá duração/dano/condições limitados; morte instantânea e alcance literal de quilômetros não são requisitos. Os quatro slots por forma serão definidos antes de implementar, separando referências canônicas de adaptações do mod.

**Byakuya — proposta de adaptação:** ondas de pequenas lâminas, cerco e padrões de defesa/ataque. Representar o conjunto por volumes de efeito e visuais com orçamento; não simular cada lâmina individualmente. Definir limites de alvos, frequência de dano e sobreposição. A defesa específica da Senbonzakura terá contrato próprio e não substituirá implicitamente o bloqueio universal.

Referências consultadas na pesquisa precedente: [Gin](https://bleach.fandom.com/wiki/Gin_Ichimaru), [Byakuya](https://bleach.fandom.com/wiki/Byakuya_Kuchiki). Essas fontes descrevem os poderes; as escolhas de adaptação e arquitetura acima são propostas para o projeto.

## Validação desta entrega

A entrega inicial do ciclo 38 foi somente documental. A revisão deste roadmap no ciclo 39 acompanha a implementação da defesa; estado funcional, validações e limites atuais no [relatório 39](39-bloqueio-postura-guard-break-2026-10-15.md). Esquiva e defesa continuam sem homologação do usuário.
