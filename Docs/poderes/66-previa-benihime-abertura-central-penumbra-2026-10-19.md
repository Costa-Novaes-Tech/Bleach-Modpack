# Ciclo 66 — prévia Benihime: abertura central, penumbra e detalhes

> Sucedido pela [revisão 67](67-previa-benihime-cortina-fechada-pega-rosto-2026-10-19.md): usuário pediu cortina totalmente fechada antes do gesto, pega articulada e novo refinamento do rosto/cabelo. A fresta e silhueta visível iniciais abaixo são históricas; não implementar essa orientação antiga.

19/10/2026. Branch `Feature-Poderes-bankais`, HEAD `ca9a067`. Revisão solicitada sobre a [prévia 65](65-storyboard-entrada-benihime-cortina-2026-10-19.md). Git já continha documentação preparada/em elaboração dos ciclos 64/65; preservada. Escopo: somente a prévia da entrada Benihime, sem tocar código do mod, Zaraki, técnicas, dano, cura ou dependências de jogo.

## Entrega visual

- [Galeria revisada](media/66-benihime-revisao/index.html), com seleção de vídeo, capítulos, câmera lenta até 0,25× e pranchas.
- [Entrada completa, 26 s, MP4 1280×720 / 24 fps](media/66-benihime-revisao/03-entrada-revisada.mp4).
- [Plano dedicado da mão, 8 s](media/66-benihime-revisao/04-detalhe-mao.mp4), com iluminação de inspeção e movimento desacelerado.
- [Olhar do observador e penumbra, 8 s](media/66-benihime-revisao/05-observador-penumbra.mp4).
- [GIF da entrada](media/66-benihime-revisao/06-entrada-revisada.gif), [quadros técnicos](media/66-benihime-revisao/01-storyboard-revisado.jpg) e [comparação do escurecimento](media/66-benihime-revisao/02-comparacao-penumbra.jpg).
- [Nova prancha conceitual](media/66-benihime-revisao/07-conceito-abertura-central.png) e [prompt integral](media/66-benihime-revisao/prompt-imagegen.md).
- [Gerador](media/66-benihime-revisao/gerar_previa.py), [timeline](media/66-benihime-revisao/timeline.json) e [verificação da passagem](media/66-benihime-revisao/verificacao-abertura.json).

Os vídeos são renders 3D procedurais, não capturas do Minecraft. A geometria recebe detalhes reais nesta revisão, mas continua sendo modelo de estudo, sem arquivo final de skin/rig exportado ao jogo. A ilustração é direção artística, separada do render. Não tratar a qualidade da prancha como qualidade já obtida no runtime.

## Correção principal: mão atravessa o espaço central

A cortina é formada por dois panos, com bordas internas próprias. Uma fresta inicial permite perceber a presença atrás. A abertura aumenta antes de os dedos cruzarem seu plano. A mão fica centralizada durante a travessia; só se afasta lateralmente quando a palma já está à frente e a abertura comporta o antebraço. Ombro e cotovelo permanecem conectados ao corpo na penumbra.

Não foi criado um furo num painel nem escondido o atravessamento com partículas. Dobras, bordas e bainhas pertencem aos lados da abertura; existe espaço vazio para a trajetória. A cortina mantém pregas estáticas antes do contato. O movimento próximo à borda cresce durante a aproximação e se dissipa depois da passagem.

A geometria foi conferida em 385 instantes da passagem, verificando pontos dos dois segmentos de braço dentro da faixa de profundidade das pregas, com espessura e margem, além de amostras da palma/dedos. Foram **13.411 verificações**, com margem mínima aproximada de **0,169 bloco** para os volumes amostrados. Isso verifica a trajetória modelada na prévia, não substitui inspeção final de toda a malha/skinning no futuro modelo do mod.

## Interior e presença contínua

Existe um espaço preto atrás da abertura, com fundo, laterais e teto. A manifestação completa está nele desde que a cortina entra no enquadramento. Não há mais ativação súbita de cabeça/roupa/braço por troca de visibilidade em instantes diferentes. O corpo avança continuamente para fora.

A iluminação das superfícies depende da profundidade em relação à cortina. Dentro, pele, vestes e cabelo recebem luz muito reduzida; uma silhueta aparece na fresta antes da mão. Ao cruzar o plano frontal, cada parte recebe progressivamente mais luz. O contraste entre rosto/mão claros e interior escuro produz a revelação, sem flash de spawn.

## Novas camadas e detalhes

- Vestes com três camadas, forro escuro, painéis sobrepostos, pregas, bordas de bainha e pequenas costuras.
- Gola clara e lapelas carmesins separadas, costuras ornamentais e faixa de cintura.
- Mãos com palma, polegar lateral/opositor, três segmentos em cada dedo longo, volumes de juntas, unhas, tendões e vincos.
- Punhos com duas linhas de costura e pontos transversais; dedos respondem à passagem e à postura dos fios.
- Rosto com mais subdivisões, olhos fechados/cílios, nariz discreto e lábios; cabelo posterior afastado da face para reduzir as interseções do estudo anterior.
- Mechas e tecidos têm movimento secundário contínuo, mantendo leitura das poses.

O modelo ainda usa materiais de cor, não uma pintura final de textura. Estes detalhes são geometria/linhas de estudo. A próxima etapa de autoria deverá traduzir o acabamento para o formato de modelo e texturas suportado pelo renderizador escolhido.

## Escurecimento dirigido pela visão

Proposta visual demonstrada: redução gradual de luminosidade, mais forte na periferia, enquanto o observador encara a Bankai. A região central permanece legível. A intensidade considera o alinhamento entre a câmera e a manifestação; ao desviar o olhar, o efeito recua. Há entrada e saída suaves na timeline da ativação.

Os [três quadros do observador](media/66-benihime-revisao/08-olhar-penumbra-quadros.jpg) complementam o vídeo. O clipe do observador mantém a mesma manifestação no cenário e muda a direção de visão: lateral → Bankai → lateral. O indicador registra o peso do efeito; não equivale a reduzir uniformemente a imagem inteira em 40%. O centro recebe atenuação menor que as bordas. O clipe de detalhe da mão desliga essa vinheta para permitir inspeção.

Nada disso foi aplicado aos jogadores reais. Na futura implementação, usar pós-processamento/overlay cliente específico da apresentação; não conceder Cegueira/Escuridão vanilla ou assumir câmeras de espectadores. Distância, cobertura e configuração gráfica ainda devem ser fechadas no contrato de integração. A intenção pedida é imponência visual ao olhar para a ativação, sem modificar regras de combate.

## Ritmo desta revisão

- 0–3,6 s: ajoelhar, katana baixa, close e “Bankai”.
- 3,6–5,4 s: câmera recua; cortina e presença na penumbra.
- 5,4–7 s: abertura central prepara a passagem.
- 7–10 s: plano próximo da mão cruzando o meio.
- 10–12 s: rosto, já presente, ganha leitura entre as bordas.
- 12–15 s: saída contínua do corpo e apoio das mãos.
- 15–18 s: pausa inclinada com mãos no chão.
- 18–21,2 s: elevação do tronco e das mãos.
- 21,2–24 s: fios e nome completo.
- 24–26 s: estabilização e recuo do escurecimento.

Os 26 segundos servem à revisão detalhada e não alteram a duração jogável de três segundos/commit no tick 40. Sem vozes nesta rodada. A restituição exata da perspectiva real do jogador e os cancelamentos serão implementados quando a entrada for levada ao mod.

## Verificação

Trajetória e quadros principais inspecionados; versões 65 preservadas em sua pasta para comparação. Três MP4s decodificados integralmente sem erro; entrada em 720p/26 s e dois clipes de 8 s, todos a 24 fps. GIF de 260 quadros. Galeria conferida com seleção de clipe, navegação e velocidade 0,25×; links e imagens locais válidos. Sem build/GameTest por não haver alteração de runtime. Nenhum commit/push adicional integra esta rodada de estudos.

Próximo foco permanece a entrada Benihime. Revisão de Higashi, dash/Rajada Ryūjin e kit Benihime fica para depois; Zaraki continua intacto.
