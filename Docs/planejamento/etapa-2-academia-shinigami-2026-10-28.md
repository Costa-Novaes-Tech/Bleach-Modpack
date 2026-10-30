# Etapa 2 — Academia e começo jogável Shinigami

> Atualização de direção em 28/10: o usuário priorizou o início de história no
> Overworld com Rukia, primeiro Hollow e livro vivo. Ver
> [abertura Shinigami e livro](abertura-shinigami-e-livro-2026-10-28.md).
> Este dossiê continua como referência da formação futura, sem exigir passagem
> pela Academia antes do novo encontro solicitado.

Data: 28/10/2026. Preparação documental solicitada pelo usuário após a seleção
de classes. Base conferida: `feat/Jigui/classes`, `8df8a64d`, checkout limpo.
Este índice consolida contratos e dependências; não aprova novas missões,
personagens, preços ou alterações de runtime.

## Escopo

Conectar o personagem confirmado ao começo da formação: encaminhamento à
Soul Society, recepção na Academia, instrutor, equipamento de treino,
fundamentos, primeiro ensino selecionado e preparação da primeira atribuição.
Formação inicial e carreira no Gotei são acontecimentos distintos.

## Fontes de produto

| Documento | Blocos necessários e uso |
|---|---|
| [Handoff vigente](../game-design/00-handoff-atual.md) | Continuidade de 23/10 e dependências ainda abertas; o pedido atual seleciona o recorte Shinigami |
| [Mapa modular](../game-design/00-mapa-modular.md) | Responsáveis M04–M16 e distinção entre conceito, especificação e implementação |
| [Base de conhecimento](../game-design/00-base-conhecimento-minecraft-bleach.md) | Revisões vigentes e limites das referências históricas |
| [M04 — origens](../game-design/04-racas-e-origens.md) | RAC-07, RAC-12, formação inicial, shinai/Asauchi; RAC-26 e continuidade da criação |
| [M04a — presença](../game-design/04a-presenca-espiritual-e-interacao.md) | RAC-30A–D: visão, interação e compatibilidade espiritual do equipamento/alvo |
| [M05 — recursos](../game-design/05-atributos-e-recursos.md) | REV-SP-02/11.2, Stamina, Energia e prática; aquisição não entrega Mastery |
| [M06 — combate](../game-design/06-habilidades-e-combate.md) | Combate básico, defesa/Postura/Dodge; M06-07 quatro disciplinas; M06-11D/E Shunpo |
| [M06a — combate automático](../game-design/06a-estado-de-combate.md) | Exercício real versus diálogo; ameaça, perigo, saída e continuidade |
| [M08 — NPCs](../game-design/08-npcs-mentores-e-faccoes.md) | M08-01A/B proteção/continuidade; M08-02A compra na fonte; carreira separada |
| [M08a — catálogo](../game-design/08a-catalogo-funcional-de-npcs-e-faccoes.md) | 8.3/8.4 Rukia/Renji, 9.1 Academia, 9.31/9.32 instrutor e primeiro repertório, 9.36 SP/continuidade, 9.39/9.40 operadores e instrumentos |
| [M09 e revisão](../game-design/09b-revisao-m09-2026-10-23.md) | Participação, alvos, encontros de treino, falha/retomada e recompensas; fichas no catálogo M09a |
| [M10 — mundo](../game-design/10-dimensoes-locais-e-estruturas.md) | Acesso, viagem, retorno, lugares e proteção localizada |
| [M10b — percursos](../game-design/10b-catalogo-funcional-de-lugares-e-rotas.md) | LOC-SOUL-02/8.4 e ROUTE-HUM-SOUL-01/9.1: chegada, prática e volta |
| [M02 — trilhas](../game-design/02-trilhas-de-progressao.md) | Integração entre recompensa, aprendizado e desenvolvimento; aplicar revisões M05/M06 |
| [M03 — campanha](../game-design/03-storyline-macro.md) | Prólogo, jornada pessoal e era compartilhada; personagem novo não reinicia o servidor |
| [Revisão de cobertura](../game-design/00-revisao-cobertura-m01-a-m10-2026-10-23.md) | 12.1 e COV-03/04; sobrevivência, itens/SP e continuidade antes da entrega |

M11–M16 são responsáveis previstos no mapa; seus arquivos principais ainda
não existem neste checkout. Não confundir essa ausência documental com
ausência total de implementação de quests, UI ou multiplayer.

## Contratos já aprovados

- Começar no Mundo Humano e encaminhar à formação na Soul Society. O acontecimento
  exato inicial e a passagem narrativa ainda precisam de roteiro.
- Instrutor original da Academia; personagens canônicos aprofundam depois.
- Treino com shinai antes da Asauchi; a shinai não se transforma na Asauchi.
- Apresentar Zanjutsu, Hakuda, Hohō e Kidō sem conceder repertório completo.
- Praticar ataque, distância, guarda, Dodge e posicionamento já disponíveis.
- Primeiro ensino selecionado: Shunpo, após preparo, com compra SP na fonte.
- Não exigir Shunpo bloqueado para comprá-lo; prover atividade legítima para
  obter SP sem depender da própria habilidade que será adquirida.
- Comprar Shunpo não é requisito universal de graduação aprovado.
- Shunpo básico: impulso horizontal, Stamina no início aceito, colisão e
  gravidade preservadas, uso no chão/ar/água e saída para reação válida.
- Compatibilidade espiritual do instrumento e do alvo precisa ser declarada.
- Atendimento essencial protegido e continuidade coerente com a era; treino
  de combate tem representação própria, sem farm do atendente protegido.
- Não há anos escolares simulados, divisão/patente automática ou obrigação
  de maximizar quatro disciplinas antes de iniciar atuação.

## Preparação técnica

Consultar [quests](../arquitetura/03-sistema-quests.md),
[capabilities](../arquitetura/02-capability-system.md),
[persistência](../arquitetura/07-persistencia-nbt.md),
[rede](../arquitetura/06-rede-sincronizacao.md),
[NPCs/quests](plano-npcs-quests.md) e [mapas](../../history-maps/README.md).
Os trechos sobre Dragon Mine Z são referência histórica, não contrato de
funcionamento atual nem catálogo de objetivos automaticamente disponível.

Pontos de entrada locais: ConfirmCharacterC2S, PlayerData/CharacterData,
infraestrutura `common/quest`, NPCs de quest, Guard/Dodge e HistoryMapInstaller.
A inspeção dirigida de ConfirmCharacterC2S confirma entrega imediata da
Asauchi na criação. Isso precisa ser conciliado com a sequência de formação,
incluindo personagens e saves existentes, antes de mudar a entrega.
A busca nominal não localizou shinai ou Shunpo no recorte de código consultado;
não equivale a auditoria de toda mobilidade ou dos objetivos de treino.
Há regiões do mapa Soul Society versionadas; não foi confirmada visualmente
uma Academia adequada ou sua localização.

## Entregas para especificar agora

1. Ficha do acontecimento inicial, condutor/passagem, recepção e chegada.
2. Local real da Academia e percurso até instrutor, prática e saída/retorno.
3. Ficha do instrutor: identidade, competência, diálogos e cobertura por era.
4. Shinai: compatibilidade, obtenção, reposição e alvo de treino.
5. Exercícios: ações observadas, sucesso/falha, retomada e participação.
6. Fonte inicial de SP e ficha de acesso/compra/prática de Shunpo.
7. Critério da Asauchi e conclusão da formação; política para saves atuais.
8. Primeira atribuição: objetivo, envolvidos, acesso, recompensa e retorno.
9. Persistência e feedback de cada marco, sem repetir compras/recompensas.

Esses itens são fichas a desenvolver, não nove quests já escolhidas. Quantidade
de aulas, nomes, custos, técnicas de Kidō, coordenadas e condição de graduação
permanecem abertos. Recomenda-se começar pela chegada/recepção: ela determina
o estado do aluno e as ações disponíveis para os primeiros exercícios.

## Limites desta preparação

Somente um documento novo. Nenhum código, mapa/save, build, teste, commit ou
push foi produzido. As leituras deste turno reconferem os recortes da etapa 2
e usam a revisão aprofundada da conversa como contexto, sem declarar nova
leitura integral de todos os módulos ou auditoria completa de runtime.
