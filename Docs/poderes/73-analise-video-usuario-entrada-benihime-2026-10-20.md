# Ciclo 73 — análise do vídeo de teste da entrada Benihime

20/10/2026. Pedido: analisar o vídeo do usuário, ainda sem codar. Branch `Feature-Poderes-bankais`, com alterações documentais do ciclo 72 já presentes e preservadas. Sem abrir o jogo, controlar a máquina, alterar runtime/áudio, executar Gradle ou realizar commit/push/merge.

## Fonte e limites

Arquivo original: `C:\Users\joaop\Downloads\Gravando 2026-10-20 201016.mp4`, 484.725.596 bytes, duração aproximada de 7m41s, 1920×1020, 30 FPS, com faixa AAC estéreo. Original preservado; não duplicado no repositório. Hash e método em [origem.json](qa/benihime/cycle73/origem.json).

Leitura visual por panorama amostrado e extração a um quadro por segundo nos quatro trechos de invocação. Pranchas são derivados, não prévias simuladas. Permitem analisar composição, oclusão e poses, mas não homologar todos os quadros, microgestos ou fluidez de 30 FPS. Não houve avaliação auditiva da faixa: presença de áudio e legendas não comprovam inteligibilidade. O problema de mixagem relatado anteriormente permanece aberto, sem nova conclusão de escuta.

O usuário informou que a manifestação **segue bem o jogador**, testado fora da gravação. Registrar como retorno positivo do usuário, separado de evidência gravada. Interrupções não foram tentadas; continuam pendentes. Mais ângulos de uma mesma sessão não equivalem a observador remoto/multiplayer.

## O que o vídeo confirma visualmente

- **Aproximadamente 3:34–3:40:** close do jogador de pé, preparação com espada baixa, recuo e cortina levantada fechada. Nos quadros inspecionados dessa preparação não aparece o rosto antecipadamente pela cortina.
- **3:41–3:47:** mão emerge pela abertura central, pano se abre e figura aparece progressivamente no interior escuro. O mecanismo de revelação está legível no cenário aberto; refinamento de pega/dedos ainda exige inspeção próxima e maior frequência de quadros. [Abertura em detalhe](qa/benihime/cycle73/0344-abertura.jpg).
- **3:47–3:54:** ambos os braços participam de poses de apoio e há mudança de posição do corpo. A leitura de avanço existe; não aprovar por isso toda a transferência de peso ou a firmeza dos contatos em cada quadro. [Pose de apoio](qa/benihime/cycle73/0350-apoio.jpg).
- **3:54–4:00:** elevação do tronco e chegada à pose das mãos/fios; cotovelos dobrados e sem a antiga aparência de braços inteiramente horizontais. Sequência: [preparo](qa/benihime/cycle73/A-sheet-0.jpg), [saída e apoios](qa/benihime/cycle73/A-sheet-1.jpg), [elevação e retorno](qa/benihime/cycle73/A-sheet-2.jpg).
- Há retorno à primeira pessoa/HUD após as cenas observadas. Isso comprova término normal nesses exemplos, não recuperação após cancelamento.

## Problemas e prioridades de revisão

### 1. Câmera no encerramento — problema visual observado

Em torno de **4:01** e mais claramente **6:34**, o rosto/cabeça do jogador ocupa quase toda a tela antes do retorno à primeira pessoa. O enquadramento perde a manifestação justamente no fim da apresentação. [Quadro de 6:34](qa/benihime/cycle73/0634-camera.jpg).

**Recomendação:** revisar a trajetória de retorno, distância do modelo e transição entre câmera dirigida e perspectiva anterior, evitando atravessar/encostar no volume da cabeça. Não atribuir a causa a um método específico sem inspeção do código e reprodução. Manter um plano legível até a devolução do controle.

### 2. Obstáculos ocultam a manifestação — limitação observada

Na invocação lateral próxima ao paredão (**aproximadamente 5:30–5:55**), terreno encobre quase todo rosto/torso e grande parte dos braços. A mão isolada permanece visível, mas a saída/apoios perdem leitura. Ver [trecho lateral](qa/benihime/cycle73/B-sheet-1.jpg) e [pose final lateral](qa/benihime/cycle73/B-sheet-2.jpg).

Na vista mais frontal entre **6:11–6:32**, a borda do terreno continua escondendo parte de um braço e da roupa. [Detalhe por volta de 6:24](qa/benihime/cycle73/0624-oclusao.jpg), [preparação](qa/benihime/cycle73/C-sheet-0.jpg), [saída](qa/benihime/cycle73/C-sheet-1.jpg) e [final](qa/benihime/cycle73/C-sheet-2.jpg).

No teste da árvore (**7:18–7:32**), tronco/folhagem encobrem rosto e parte do corpo. [Saída junto à árvore](qa/benihime/cycle73/D-sheet-1.jpg), [final](qa/benihime/cycle73/D-sheet-2.jpg).

Isso demonstra **oclusão real e composição inadequada nesses locais**, não prova por si só erro no renderizador, nem permite determinar exatamente quais volumes atravessam cada bloco. Adaptar a altura das mãos ao chão não resolve espaço livre para todo o corpo/cortina/câmera.

**Recomendação:** avaliar separadamente posicionamento da manifestação e escolha do enquadramento. Procurar posições próximas com espaço suficiente, mantendo a identidade de estar atrás do jogador; prever alternativa quando não houver área livre, sem mover o jogador ou remover blocos automaticamente. Regras de deslocamento/cancelamento fora do espaço disponível precisam de contrato antes da implementação.

### 3. Locomoção, roupa e modelo — acabamento ainda aberto

Nos quadros abertos a sequência é compreensível, mas ainda há leitura geométrica e rígida em braços, vestido e rosto. Isso é avaliação artística, não um novo bug de mecânica comprovado. A alternância de poses das mãos existe; contato sem patinar, antecipação do peso e continuidade do engatinhar não estão homologados por amostragem de um segundo.

**Recomendação:** preservar a sequência e revisar um avanço completo em close lateral, incluindo ombro/cotovelo/punho, permanência da mão no apoio e movimento do quadril. Não aumentar simplesmente a duração ou quantidade de quadros-chave sem resolver os contatos. Avaliar necessidade de rig/autoria mais detalhados conforme a limitação concreta, sem declarar migração integral para GeckoLib obrigatória ou concluída.

## Estado do primeiro item obrigatório do roadmap

**Em revisão; não encerrado.** Há evidência adicional da revelação, apoios/elevação e término normal; acompanhamento recebeu retorno positivo do usuário. Prioridades propostas: retorno da câmera → espaço/enquadramento perto de obstáculos → contato/peso e acabamento → mixagem quando retomada. Preservar proporções já corrigidas e Zaraki.

Continuam pendentes: escuta da mixagem final; interrupções/morte/dimensão/desconexão; skins classic/slim/canhoto e mão secundária; observador remoto/dois clientes; benchmark controlado. Cenários com encostas, paredes e árvore agora têm evidência concreta de problemas de legibilidade, não devem voltar a ser descritos apenas como “ainda não testados”.

## Ferramentas e verificação documental

FFmpeg 7.1 preparado localmente em `build/video-review-tools`, via `imageio-ffmpeg==0.6.0` em cache. Tentativa restrita não resolveu PyPI; execução com acesso ao cache concluiu. Sem instalação global ou mod novo. Derivados selecionados em `Docs/poderes/qa/benihime/cycle73`; extrações intermediárias em `build/video-review-73`. Nenhum áudio enviado a serviço externo.

Verificar links locais e `git diff --check`. Nenhum teste de gameplay executado nesta análise. Próximo passo continua revisão do item 1; correções dependem da próxima orientação, sem iniciar código a partir deste relatório.
