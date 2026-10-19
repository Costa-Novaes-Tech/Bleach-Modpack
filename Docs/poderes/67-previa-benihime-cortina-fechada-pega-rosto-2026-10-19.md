# Ciclo 67 — cortina fechada, pega articulada e rosto sereno

19/10/2026. Branch `Feature-Poderes-bankais`, base `ca9a067`. Usuário comparou os prints da prévia 66 com o conceito e pediu refinar o gesto de abertura, ocultação inicial e aparência. Escopo permanece **somente prévia**, sem implementação no mod ou alterações do Zaraki. Documentos/arquivos anteriores, inclusive os já preparados no índice, foram preservados.

## Material desta revisão

- [Galeria 67](media/67-benihime-revelacao/index.html).
- [Entrada completa, 28 s, MP4 720p/24 fps](media/67-benihime-revelacao/03-entrada-surpresa.mp4).
- [Giro, pega e tração em detalhe, 10 s](media/67-benihime-revelacao/04-pega-cortina-detalhe.mp4).
- [Rosto e cabelo, 6 s](media/67-benihime-revelacao/05-rosto-cabelo.mp4).
- [GIF](media/67-benihime-revelacao/06-entrada-surpresa.gif), [storyboard do render](media/67-benihime-revelacao/01-storyboard-revisado.jpg), [rosto próximo](media/67-benihime-revelacao/02-rosto-cabelo.jpg) e [comparação 66/67](media/67-benihime-revelacao/08-comparacao-rosto.jpg).
- [Gerador](media/67-benihime-revelacao/gerar_previa.py), [timeline](media/67-benihime-revelacao/timeline.json), [verificação do braço/pega](media/67-benihime-revelacao/verificacao-rig.json) e [verificação de ocultação](media/67-benihime-revelacao/verificacao-oclusao.json).

Vídeos e imagens desta rodada são renders da geometria de estudo, não capturas do jogo. Não foi criado novo modelo/skin de runtime. As pranchas conceituais do ciclo 65 continuam referência de acabamento; a prévia 67 aproxima pose, expressão e penteado, sem afirmar que o acabamento ilustrado já foi reproduzido integralmente.

## Decisão que sucede a prévia 66

O pedido anterior de presença discretamente visível na fresta foi substituído pela orientação atual: **cortina completamente fechada para preservar a surpresa**. Não manter fresta nem silhueta visível durante sua montagem ou pausa inicial.

Sequência corrigida:

1. A cortina sobe sozinha, fechada, entre 3,6 e 5,1 s. A figura não participa desse crescimento.
2. A partir de 5,4 s a figura ocupa o interior, mais afastada, atrás do pano já montado. Nenhum corpo é mostrado antes da abertura.
3. As duas bordas têm sobreposição central de 0,09 unidade do estudo. A primeira separação começa somente a partir de 6,95 s, no gesto dos dedos.
4. A mão passa de lado pela abertura, gira e fecha os dedos sobre a bainha central; depois traciona o pano.
5. Só então a revelação permite ver a figura. O interior continua escuro; o corpo ganha luz conforme avança.

O posicionamento inicial da manifestação recua 2,5 unidades visuais em relação ao estudo anterior. Essa referência serve à encenação, não altera a distância de gameplay do kit nem hitboxes. Após sair, mantém a postura inclinada, pausa no chão e elevação para os fios já solicitadas.

## Gesto da mão e continuidade do braço

O novo gesto tem aproximação, rotação do punho, flexão escalonada dos dedos, oposição do polegar, pega sustentada, puxada e soltura. Os dedos longos dobram em três segmentos; a palma inteira gira, em vez de apenas mudar a posição dos dedos. A tomada próxima mostra o contato por um ângulo em que se pode ler a curvatura da pega.

Uma âncora comum define a borda agarrada, o punho e a dobra da cortina. Durante a tração, o punho mantém seu deslocamento relativo a essa borda; as pontas dos dedos retornam em gancho em torno dela. A dobra do tecido se projeta para a frente localmente junto à pega, dissipando a deformação para cima e para baixo. Abertura e mão não usam relógios independentes.

Os braços passam a usar solução de dois segmentos com comprimentos constantes. Ombro, cotovelo e punho se movem juntos; o cotovelo encontra uma posição contínua em função do alvo da mão. Perfis de espessura e juntas menores reduzem a aparência de peças cilíndricas desconectadas. Comprimentos são proporções visuais do estudo, não novos alcances de técnicas.

A verificação amostra 673 instantes e ambos os braços. O erro máximo de comprimento é da ordem de 10⁻¹⁴, e o erro de posição do punho relativo à âncora durante a pega sustentada é zero. Isso demonstra consistência da prévia; não é física de tecido/skinning final do jogo.

## Rosto e cabelo

- Cabeça com mandíbula/chin mais delicados e planos de rosto mais definidos, em lugar do volume oval único.
- Sobrancelhas suaves, olhos fechados e boca quase horizontal, sem sorriso marcado ou expressão de esforço.
- Nariz discreto, cílios menores e detalhes menos contrastados.
- Cabelo com cobertura posterior e superior, risca frontal, ondas laterais, mais volumes nos laços e cinco mechas em camadas por lado.
- Movimento secundário discreto preservado, sem balançar o rosto ou desfazer a pose de assinatura.

O plano de rosto usa iluminação de inspeção sem vinheta para permitir avaliar as formas. No vídeo completo, a revelação permanece sob a iluminação dramática. Texturas finais e detalhamento artístico ainda exigem a etapa de autoria para o jogo.

## Ritmo

- 0–3,6 s: ajoelhar, katana baixa e close/“Bankai”.
- 3,6–5,4 s: montagem independente da cortina fechada.
- 5,4–6,9 s: pausa fechada, sem figura visível.
- 6,9–8,3 s: aproximação dos dedos e giro do punho no centro.
- 8,3–8,8 s: fechamento da pega.
- 8,8–11,8 s: tração do pano, sustentando o contato.
- 11,8–13 s: rosto entre as bordas.
- 13–16 s: saída do corpo e apoio.
- 16–19 s: pausa com mãos no chão.
- 19–22,2 s: tronco se ergue.
- 22,2–26 s: fios e nome completo.
- 26–28 s: estabilização.

Mantido o escurecimento dirigido da revisão anterior. Vozes continuam adiadas. Os 28 s são duração da prévia detalhada, não nova duração jogável aprovada. Duração, commit/custo e restauração da câmera da build continuam fora desta rodada.

## Verificação visual e limites

Para detectar os vazamentos mostrados nos prints, foram renderizados os instantes **3,8 / 4,4 / 5,0 / 5,8 / 6,7 s**, com e sem a figura atrás. A diferença máxima por pixel foi **zero nos cinco pares**: não se vê a figura durante a subida nem na pausa fechada. Isso também evita que a altura da cortina arraste/escale o corpo visivelmente.

Quadros próximos e poses principais inspecionados; três MP4s decodificados integralmente sem erro (28/10/6 s, 720p/24 fps), GIF de 280 quadros e links locais conferidos. Galeria verificada com salto de capítulo e velocidade de 0,25×. Fontes e configurações do mod permanecem intactas; não há build/GameTest ou nova dependência de jogo nesta etapa. As prévias anteriores permanecem disponíveis para comparação.

Próximo foco permanece concluir a entrada Benihime. Higashi, dash/Rajada Ryūjin e kit Benihime continuam para depois; **Zaraki não foi alterado**.
