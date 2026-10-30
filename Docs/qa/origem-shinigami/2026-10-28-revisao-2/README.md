# Revisão 2 — Rukia sentada, aproximação em pé e Fishbone lateral

28/10/2026 · `feat/Jigui/classes` · base `8df8a64d`. Correções pedidas pelo usuário,
incluindo o ajuste posterior que remove o agachamento do jogador. Evidências da
primeira entrega foram preservadas na pasta irmã `2026-10-28`.

## Resultado visual

- Rukia sentada no chão, costas junto ao poste, pernas projetadas para frente.
  Sua posição permanece fixa durante toda a transferência.
- O jogador caminha 1,1 bloco até a espada entre ticks 128 e 174. **Permanece em
  pé**, sem baixar o corpo ou fazer a passada ajoelhada da tentativa intermediária.
- Espada mais alta: braço de Rukia elevado para atingir o tronco do personagem
  em pé. Reação separada de tronco, cabeça e braços; sem inclinar o corpo inteiro
  como uma peça rígida. [Contato final](scene-174.png).
- Explosão com núcleo branco-azulado, casca de energia expansiva, raios, anel no
  chão, clarão pleno e impacto sonoro. Geometria fullbright funciona sem Photon
  e não depende somente da densidade de partículas. [Expansão](scene-198.png).
- Fishbone se aproxima pela esquerda de Rukia / direita do jogador. No referencial
  antigo da cena, isso corresponde a X negativo: o eixo X positivo representa a
  esquerda do jogador. Boss deixa de correr do fundo alinhado atrás de Rukia.
  O jogador vira para esse lado antes do corte e recebe controle de frente para
  a ameaça. [Aproximação](scene-216.png), [corte](scene-224.png).

## Áudio solicitado

Substituída a síntese SAPI pelo intervalo **1:27–1:30** do vídeo
[Ichigo Become a Shinigami — BleachTube HD](https://www.youtube.com/watch?v=73WQsPv-RK8).
O trecho foi recortado com reencodificação para remover o preroll da cópia de
pacotes, convertido para Ogg Vorbis mono 48000 Hz e limitado a 3,00 segundos.
Asset ativo: `assets/bleachmod/sounds/story/rukia_origin.ogg`. Entra no tick 38.
Fonte, intervalo e hash ficam em `proveniencia.json`.

Ferramentas locais: yt-dlp 2026.10.26, instalado somente em `build/audio-tools`,
e FFmpeg 7.1 já usado na primeira entrega. Sem mudança no PATH ou serviço cloud.

## Validação

- Baseline `check`: aprovado, 146 regressões. [Log](origin-r2-baseline.log).
- `check build runClient` final: aprovado; 146 regressões e `Origin QA COMPLETE`.
  [Log](origin-r2-client.log).
- GameTestServer: **231 testes obrigatórios aprovados** em mundo descartável `run-gametest/base`. O relatório
  final do comando está em [origin-r2-server.log](origin-r2-server.log).
- Acrescentado um GameTest para posição constante de Rukia, avanço do jogador,
  trajetória lateral do boss e estabilidade da marca de contato. A fixture de
  terreno acompanha o novo corredor lateral e restaura os blocos originais.
- Fixture de cliente mantém o mouse bloqueado, valida a marca de caminhada e a
  virada dirigida, restaura primeira pessoa e espada equipada e confirma o fluxo
  de vitória/livro. Mundo superflat separado; jogador invulnerável só na fixture.

## Evidências reais

Os PNGs são capturas do framebuffer do cliente Forge real. `frames.jsonl` inclui
posições de jogador, Rukia e boss, yaw, pose `SITTING` e estado do braço.

[Prévia corrigida](abertura-corrigida.mp4) montada com esses frames e seus ticks.
Não é gravação contínua: o mesmo áudio usado no jogo foi acrescentado no marco
da fala; efeitos sonoros do jogo não foram capturados no MP4.

Inspeção final: sentado/apoio 4/5, avanço em pé/contato 4/5, reação segmentada 3/5,
força visual da explosão 4/5, direção do Fishbone 4/5. São avaliações iniciais,
não aprovação artística definitiva. Pernas e mãos usam o rig Minecraft existente;
não há BendyLib, dedos novos ou braço destacado caindo.

Casa e modelo definitivo do poste permanecem pendentes. Dois clientes reais,
Photon, servidor dedicado com jogador conectado e medição de FPS não foram
homologados neste ciclo. Não houve commit, checkout, reset ou alteração da Zaraki.
