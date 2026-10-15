# Ciclo 44 — Pétalas ao vento, prisão, Senkei e Hakuteiken

Data: 15/10/2026. Branch `Feature-Poderes-bankais`. Sucede o protótipo do [ciclo 43](43-senbonzakura-kit-completo-prototipo-2026-10-15.md), após playtest do usuário. **Esquiva e defesa homologadas**: consideradas concluídas, sem alterar seus parâmetros nesta entrega. Aizen adiado; roadmap posterior preservado.

## Pedido e resultado

O usuário aprovou a direção das pétalas, mas pediu menos densidade/mais vento, +2 blocos nos alcances, jardim fixo de raio 40, convergência com prisão/levitação/dano contínuo, arsenal também na forma selada e lâmina fantasmagórica nas liberações. As duas imagens anexadas orientaram o desenho das espadas luminosas da Senkei e a preservação do cabo/tsuba de Senbonzakura; são referências visuais, não texturas copiadas para o mod.

| Técnica ou parâmetro | Anterior | Atual |
| --- | --- | --- |
| Corte selada/Shikai e cinco cortes Bankai | 6 blocos | **8 blocos** |
| Corrente guiada | 12 blocos | **14 blocos** |
| Região apontada da convergência | 9 blocos | **11 blocos** |
| Raio de captura da convergência | 3 blocos, explosão única | **5 blocos**, prisão com pulsos |
| Guarda de pétalas | Visual de raio 1,5 | Visual de raio **3,5**; defesa continua individual/frontal, redução de 45%, sem proteger aliados próximos |
| Senkei | Círculo de raio 5, 16 colunas de partículas | Raio **7**, **32 espadas luminosas em duas fileiras** |
| Ikka Senjinka | 12 blocos | **14 blocos** |
| Hakuteiken | 10 blocos | **12 blocos** |
| Jardim fixo | Raio 10 | **Raio 40 / diâmetro 80**, conforme pedido específico, em vez de aplicar apenas +2 |

Custos, cooldowns, dano bruto dos demais ataques, janela de reativação e bônus passivo Bankai de alcance corpo a corpo +1,5 permanecem. Alcance passivo não é o alcance de uma técnica; não altera mineração. Limites nominais de estocadas agora também verificam a projeção do centro do alvo para não ganhar distância adicional pela tolerância lateral da caixa de colisão. Cobertura e chunks carregados continuam exigidos.

## Pétalas mais leves

- Corte: **84 → 32** emissões por quadro. Aura: **16 → 6**, frequência **4 → 8 ticks**. Fluxo, guarda, convergência e explosões: **96 → 36**; chão/contorno floral: **96 → 64**, distribuídos sobre o campo maior. Senkei/Hakuteiken: apenas 12 pétalas por quadro como acompanhamento da geometria.
- Limite global de novas emissões por cliente/tick: **256 → 128**; orçamento por dono no servidor permanece 192, compartilhado entre camadas. Menos partículas não muda colisão/dano.
- Pétalas menores: tamanho base .045–.085, opacidade inicial .75, vida 24–34 ticks, rotação lenta, deriva horizontal variável e leve oscilação. Corrente com distribuição lateral/vertical mais solta; guarda/aura com variação de raio e altura. Tapete flutua suavemente acima da superfície.
- Mesmo conjunto de doze sprites vanilla de cerejeira. Geometria luminosa e voo das pétalas são recursos distintos: opções gráficas podem reduzir partículas, mas espadas/asas continuam desenhadas.

## Convergência: prisão de pétalas

Selada e Shikai, slot 3/B. Região apontada até 11 blocos, preparação de 12 ticks. Captura uma lista de alvos elegíveis no raio horizontal de 5/altura inicial de 3, limitada pelas regras de alvo/linha de visão existentes. Não captura novos inimigos continuamente.

Depois da preparação, a prisão dura **60 ticks / três segundos**, com **três pulsos de dano bruto 3**, espaçados em 20 ticks. Substitui o antigo dano bruto 9 de uma vez. Aplica levitação vanilla de shulker, nível I, e lentidão V; uma atração horizontal suave tenta manter o alvo perto da região. Não é teleporte nem prisão absoluta contra qualquer deslocamento: fuga do volume, cobertura, morte e invalidação do alvo encerram sua participação. Volume de manutenção: raio 6/altura 6, para acomodar a subida e a movimentação.

O lançador fica ocupado apenas na preparação de 12 ticks; depois pode se mover/usar outras ações. Espirais de pétalas acompanham os capturados; no máximo três alvos recebem quadro visual por atualização, mantendo o orçamento, enquanto o dano obedece ao limite de seleção de 32.

Efeitos de controle usam concessões de **oito ticks**, renovadas a cada quatro. Cancelar a técnica, perder forma/arma/vínculo ou sair do mundo interrompe dano/renovação; a última concessão termina em até oito ticks. Não remover à força efeitos de levitação/lentidão de outra fonte: uma levitação externa mais forte é preservada. Respeitam-se efeitos/imunidades vanilla. HUD informa o tempo da prisão. A duração estendida de controle não concede maestria ociosa; apenas prática válida passa pelo serviço comum.

## Hakuteiken: princípio e leitura em jogo

Hakuteiken inspira-se na concentração das lâminas em uma espada, com energia branca em forma de asas e halo; o anime não precisa ser reproduzido como uma área de pétalas. Para a inspiração, ver a descrição de [Senbonzakura Kageyoshi](https://bleachyashiki.com/kageyoshi/) e da [forma de espada/asas brancas](https://nan-blog.com/41509). Nossa adaptação mantém o slot 4 Bankai como um finalizador frontal, não como voo/dash/teleporte.

Sequência implementada:

1. Ao apertar C, concentração por **16 ticks / 0,8 s**, lâmina da mão branca e um contorno luminoso de espada apontando na direção do lançamento.
2. **Duas asas brancas de penas triangulares**, abrindo conforme a carga, e **halo circular** atrás do personagem. Poucas pétalas acompanham; a silhueta vem de geometria própria, não de uma nuvem aleatória.
3. Estocada de energia branca até **12 blocos**, dano bruto 20; traço branco de núcleo estreito mostra a mesma direção e comprimento liberado pela colisão com blocos. Não gira automaticamente após a mira inicial. O jogador pode andar durante a carga, mas o golpe permanece ancorado à posição/direção do início para manter previsibilidade.
4. As construções visuais recentes expiram em até sete ticks sem novo quadro; lâmina da mão volta ao desenho espectral normal. HUD mostra “Concentrando → estocada frontal”.

Asas são cosméticas e não concedem voo. O alcance visual das asas não aumenta a largura do dano. Ikka usa traço semelhante com tonalidade rosa; Hakuteiken tem núcleo/espada/asas brancos.

## Senkei e modelos

Senkei desenha **16 espadas por fileira, duas fileiras**, distribuídas ao redor do jogador. Cada espada possui cabo violeta, guarda transversal, corpo rosado luminoso, núcleo branco estreito e ponta definida; já não depende de seis partículas em coluna para sugerir uma lâmina. A referência visual recebida orienta esse anel de espadas. Reativação Ikka reúne o círculo antes da estocada; custo/espera/cooldown do ciclo 43 preservados.

Shikai e Bankai agora mantêm uma **lâmina espectral completa**, com texturas vanilla de vidro tingido rosa e branco, `render_type: minecraft:translucent`, cabo/tsuba existentes preservados. A espada temporária de Hakuteiken tem lâmina branca ligeiramente mais larga. As outras zanpakutōs e a bainha homologada não foram remodeladas.

As construções usam `SenbonConstructGeometry` compartilhável para testes/prévia e `SenbonConstructRenderer` no evento Forge AFTER_PARTICLES, com material próprio sem culling, depth test e composição no alvo de partículas, sem entidades persistentes. Cache máximo de 32 construções, chave por dono/tipo, substituição por quadro novo e expiração de sete ticks; logout/unload limpam cache e referências do emissor. Senkei utiliza 160 quadriláteros; asas/halo/espada, menos de 128. Coordenadas renderizadas relativas à câmera.

Prévia estática gerada da mesma geometria Java e inspecionada: [Senkei e Hakuteiken](media/44-senkei-hakuteiken-geometria.png). É projeção do código com cores planas, **não captura nem homologação no Minecraft**.

## Forma selada e compatibilidade

Forma selada agora possui os **mesmos quatro slots da Shikai**: corte, corrente, prisão, guarda. Mesmos custos/cooldowns; maestria por forma continua independente, requisitos 0/20/40/60 e descobertas persistentes. Não copiar automaticamente a maestria de Shikai para selada. Para liberar tudo rapidamente no playtest:

```mcfunction
/bleachdev mastery set @s zanpakuto sealed 100
```

Setup de vínculo/item/transformação/mode gratuito permanece no [ciclo 43](43-senbonzakura-kit-completo-prototipo-2026-10-15.md). Catálogo geral: **36 definições**, 12 delas Senbonzakura (quatro por forma), mantendo oito técnicas funcionais distintas e duas continuações Bankai. Asauchi continua sem slots elementais. Protocolo **2.13**, incluindo dono no pacote das construções e raio visual válido até 48; audiência até 96 blocos do centro, para contemplar o jardim maior. Não carregar chunks novos para desenhar ou atingir alvos. Schema **8** preservado, sem migração/reset de progresso.

## Validação e próximo playtest

`check build runGameTestServer --offline --console=plain` aprovados na versão final: **89 regressões e 89 GameTests obrigatórios**. Log local `build/cycle44-final-validation.log`; JAR `build/libs/bleachmod-0.2.0.jar`. Cinco cenários de integração novos cobrem prisão com três pulsos/levitação/expiração, cancelamento sem apagar efeito externo, cobertura sólida, alcance ampliado na selada e primeiro pulso da corrente quando o processamento começa dois ticks após o lançamento. Cenários anteriores foram atualizados para raio 40, arsenal selado e alcance Hakuteiken 12, com alvo fora desse alcance preservado.

A corrente agora registra os intervalos de pulso processados: uma chegada do pedido depois do tick do jogador não perde o primeiro pulso nem repete dano a cada tick da mesma janela. Correção identificada durante a repetição da suíte, com teste específico reproduzindo o atraso inicial.

Regressões adicionais verificam roundtrip do pacote de raio 40/dono e rejeição acima de 48, limites/estrutura da geometria luminosa e parâmetros de emissão/alcance. Recursos conferidos: JSONs dos cinco modelos, coordenadas/aliases/texturas vanilla, render type translúcido das lâminas espectrais e traduções; classes/material/modelos presentes no JAR final. Prévia PNG derivada da geometria real inspecionada e vinculada acima. Diff e links locais revisados. Avisos existentes de depreciação permanecem; teste automatizado não equivale a homologação visual/FPS.

Conferir em jogo: leveza/vento das pétalas e visibilidade do arco; arsenal selado após maestria adequada; transparência da lâmina em primeira/terceira pessoa; prisão contra mobs e jogador, cobertura, fuga e cancelamento; jardim plano/irregular de raio 40 com reativação no centro original; Senkei em duas fileiras; Hakuteiken com carga, asas, halo, estocada e parede bloqueando o feixe. Dois clientes com a mesma build devem conferir observador, expiração das construções e opções gráficas. Não houve medição de FPS ou sessão multiplayer real nesta entrega.

Arquivos principais alterados: `SenbonPolicy`, `SenbonzakuraTechniqueService`, `TechniqueDefinitions`, `SenbonPetalsS2C`, `SenbonConstructGeometry`, `SenbonConstructRenderer`, `SenbonPetalEffects`, `SenbonPetalParticle`, HUD/snapshot/traduções/modelos próprios e testes Senbon/regressões. Esquiva/defesa mantêm a implementação homologada.
