# Plano de poses e pranchas de modelagem — Ryūjin

**05/11/2026 · `Feature-Poderes-bankais` · planejamento.** Complementa o [plano de animação, VFX, câmera e shaders](plano-stack-visual-animacao-camera-shaders-2026-11-05.md). A cena Ryūjin serve como primeiro ensaio de corpo, arma, sol e explosão. Este documento não muda o runtime nem homologa uma imagem gerada. As poses devem ser verificadas com o jogador e o item reais no cliente Forge.

## Contrato temporal existente

`RyujinBankaiTimeline` dura **240 ticks reais / 12 s** e mapeia esse tempo para **0–140 beats autorais**. `RyujinChoreography.bankaiEntrance` controla braço armado, braço livre, tronco, pernas, item e punho; `RyujinBankaiCamera` mantém tomada frontal. O roteiro visual segue estes marcos, sem alterar o contrato de gameplay de release 60/commit 40:

| Ticks reais | Beats autorais | Marco visível |
| --- | --- | --- |
| 0–40 | 0–12 | Katana sobe diante do jogador; aproximação frontal. |
| 40–70 | 12–20 | Sustentação vertical durante “Bankai”; fala inicia no tick 40. |
| 70–112 | 20–30 | Explosão **atrás** do jogador, recuo de câmera e formação do sol. |
| 112–116 | 30–32 | Sol completo brevemente legível. |
| 116–142 | 32–52 | Absorção: giro acelera, chamas mascaram a redução gradual e convergem à espada. |
| 142–160 | 52–60 | Lâmina carbonizada e pausa de leitura. |
| 160–210 | 60–112 | Nome “Zanka no Tachi”, espada no flanco e close frontal. |
| 210–240 | 112–140 | Saída da pose e restauração da câmera/controle. |

Esses intervalos descrevem o **estado atual do código**, não duração nova aprovada. A voz, o primeiro quadro da explosão e a passagem da lâmina precisam ser inspecionados juntos em vídeo contínuo.

## Construção pose por pose

| ID | Referência corporal e da espada | Transição e checagem |
| --- | --- | --- |
| P0 — base, beat 0 | Pés no chão, eixo do peito e rosto voltados à frente, ombros sem torção; mão armada segura o cabo. Mostrar a geometria real da katana e a orientação do fio. | Registrar pivôs de ombro, cotovelo, punho, quadril e item. A posição neutra é o ponto de retorno, sem deslocamento da entidade. |
| P1 — preparação, beats 0–8 | A mão armada eleva a katana por um arco curto diante do esterno. Mão livre acompanha apenas para equilibrar a silhueta; quadril firme e joelhos com flexão discreta. | Conferir em perfil que punho/guarda não atravessam peito ou rosto; ponta não varre a câmera. A subida deve ter antecipação e desaceleração ao chegar. |
| P2 — invocação, beats 8–20 | **Uma mão** sustenta katana **à frente, com ponta para cima**. Ombros assentados, olhar à frente, mão livre separada do punho armado. Espada permanece legível durante a palavra “Bankai”. | Fixar posição relativa de mão, guarda e ponta; evitar deriva do item e giro involuntário por transformação local da mão. A câmera frontal aproxima e sustenta o rosto e a arma. |
| P3 — explosão, beats 20–30 | Corpo absorve a onda com compressão breve de tronco/joelhos, sem dar passo e sem retirar prematuramente a espada da frente. A reação é menor que a ação do fogo. | O volume nasce no espaço **atrás** do jogador e sobe/abre para os lados; uma faixa clara deixa cabeça, mão e espada visíveis. Checar frente e perfil com sol, jogador e explosão na mesma cena. |
| P4 — descida, beats 20–52 | Após a fala, cotovelo e punho conduzem a lâmina em arco contínuo rumo ao lado da mão armada. Ombro relaxa e tronco retorna ao eixo. A ponta termina baixa, com lâmina **voltada para fora** do corpo. | Usar amostras intermediárias nos beats 30, 44 e 52; impedir espada de cruzar rosto/peito e impedir snap entre keyframes. O sol se completa por volta de 30 e a câmera panorâmica deve mostrá-lo inteiro. |
| P5 — absorção/selamento, beats 32–52 | Corpo sustenta a pose lateral já alcançada progressivamente; punho é a âncora das chamas que entram na espada. A mão livre não cobre o ponto de chegada. | Sol preserva volume no começo, aros aceleram em sentidos alternados, escala reduz progressivamente **sob fluxo de chamas**. O último quadro deve ter destino visível na lâmina, não desaparecimento súbito. |
| P6 — revelação, beats 52–112 | Katana carbonizada ao lado do jogador; pés plantados, peito aberto, braço armado em posição baixa estável. A arma não volta a apontar para o observador. | Fazer pausa curta para ler a espada e a fala do nome; aproximar câmera sem cortar rosto/arma. Corpo pode respirar discretamente, sem pose residual de mira. |
| P7 — saída, beats 112–140 | Reduzir peso da camada de pose até a animação normal; mão e espada se ajustam sem salto visível. | Conferir último frame, cancelamento, mão esquerda espelhada, primeira/terceira pessoa e câmera retornada. Nenhum osso ou efeito permanece preso ao jogador. |

P3 e P4 se sobrepõem intencionalmente: a reação começa no instante da explosão e a descida da espada ocorre durante a formação do sol. Os ângulos exatos de ossos, dimensões e curvas são medidas a ajustar no Blockbench e no cliente; a tabela não autoriza inventar números a partir de uma imagem.

## Regra de produção para imagens de storyboard e referência

Toda nova prancha destinada a **modelo ou animação** deve ser uma folha de referência utilizável pelo Blockbench, e não apenas uma ilustração de atmosfera. Para cada personagem/prop e pose aprovada:

1. Produzir **duas versões correspondentes** da mesma pose e proporção: **limpa/lisa**, com volumes e materiais simplificados; e **em blocos**, decompondo volumes em cubos/planos compatíveis com a modelagem Blockbench. Manter mesmo enquadramento, escala e orientação entre as duas, para comparar contorno e volumes diretamente.
2. Mostrar **frente, costas, perfil esquerdo, perfil direito e 3/4** em escala consistente. Para relação de profundidade, acrescentar vista superior esquemática: jogador, direção da frente, plano do sol e âncoras de fogo. As vistas ortográficas são a fonte de medida; 3/4 serve para verificar leitura espacial.
3. Usar fundo neutro, luz uniforme, grade e escala indicadas. Marcar frente/costas, lado armado, mão dominante, ponta/fio da espada, pivôs e separação do sol em relação ao corpo. Na versão em blocos, identificar hierarquia de bones e pontos de articulação. Não atribuir dimensões exatas sem medição no editor.
4. **Remover detalhes imaginativos e desnecessários**: cenário, poses adicionais, ornamentos inventados, textura de anime, partículas, bloom, sombras dramáticas, fumaça que esconda a silhueta e elementos ausentes do asset aprovado. Efeitos ficam em **folha separada** de volumes, emissão e timing; jamais substituem vista limpa da peça.
5. Gerar primeiro a pose neutra e as poses extremas P2/P6, comparar as versões limpa/em blocos e corrigir proporções. Em seguida produzir P1/P3/P4/P5/P7 e amostras de transição. Quando uma saída de IA contradizer outra vista, registrar a incerteza e corrigir a prancha antes de converter em cubos.
6. Nomear cada arquivo com `asset_pose_vista_variante_versao` e registrar fonte aprovada, beat/tick, orientação, escala, mão dominante, pivôs e revisão humana. Storyboard de sequência pode reunir quadros com tempo, mas cada quadro precisa apontar para sua folha de modelagem; não usar o storyboard como única referência de geometria.

Para este ensaio, fixar **frente local do manequim em `-Z` e sol em `+Z`**, atrás dele, e documentar o eixo da câmera frontal. As vistas traseiras podem projetar o sol sobre o jogador; por isso, validar visualmente a sobreposição, além das coordenadas. Construir variante espelhada para mão esquerda após aprovar a direita, verificando fio, empunhadura e posição final.

## Gates de execução

1. Aprovar a silhueta limpa e os eixos da prancha P0/P2/P6; em seguida conferir que a versão em blocos preserva proporção e empunhadura.
2. Importar as referências no Blockbench em projeto isolado, criar pivôs/bones e animar P0→P7. Conferir editor e JSON exportado, sem tratar preview como prova de runtime.
3. Reproduzir os assets exatos no Forge com corpo PlayerAnimator, item real, sol GeckoLib, VFX e câmera sincronizados pelo mesmo relógio. Capturar vídeo contínuo de frente, 3/4 e lado; inspecionar fala, profundidade, absorção, retorno e duas mãos.
4. Comparar o protótipo base e os backends VFX previstos no plano visual; medir legibilidade e custo antes de escolher tecnologia. Guardar evidências e limitações em `Docs/poderes`.
