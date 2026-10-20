# Ciclo 71 — revisão visual intermediária

20/10/2026. Inspeção de capturas reais do cliente Forge no perfil base. Esta revisão identifica falhas para a próxima correção; não declara a implementação final homologada.

## Execução e limites

Cliente iniciado com `-PbenihimeQa=true`. A tentativa de controle pela ferramenta `@oai/sky` ficou pendente por 522,4 segundos e foi interrompida pelo usuário; nenhum input ou comando de chat foi enviado. O agente principal acrescentou a sequência explícita `-PcinematicQa=true`, limitada ao ambiente de desenvolvimento e ao save descartável de QA.

O primeiro cliente foi encerrado por CTRL-C na sessão de QA. O segundo executou bancada, transformação diurna, noturna, terreno irregular e os dois cenários Higashi automaticamente. Não foram abertos mundos pessoais.

FFmpeg/gdigrab não encontrou o título da janela após sua alteração durante o carregamento singleplayer. Não há vídeo contínuo desta rodada. As galerias abaixo preservam os PNGs e metadados originais e incluem vídeo **amostrado**, com os intervalos reais `wallMs`, sem áudio e sem interpolação. Esses vídeos não comprovam a fluidez de cada quadro renderizado. Nenhum dispositivo de áudio de loopback estava disponível via DirectShow; não foi tentada captura de microfone ou webcam.

## Benihime — marcos observados

- [Bancada final, 286 PNGs](1792500668247/index.html): tick 120 com cortina inteiramente fechada, sem figura visível; tick 210 com mão na abertura central; ticks 280/320 mostrando avanço do tronco, braços mais compactos, cotovelos dobrados e palmas junto ao gramado.
- [Transformação real diurna, 284 PNGs](1792500703182/index.html): tick 480 com postura em pé do jogador, lâmina voltada ao chão, manifestação frontal e cotovelos baixos; preservação do acompanhamento após a cena.
- [Transformação real noturna, 282 PNGs](1792500735371/index.html): sequência repetida e acompanhamento registrados. A avaliação artística do áudio permanece dependente de escuta do usuário.
- [Terreno irregular, 287 PNGs](1792500768530/index.html): pads de pedra de referência/+1/+2 blocos no mundo descartável; tick 320 mostra as palmas acompanhando as duas alturas e o tronco adaptado. PASSOU neste marco controlado. Não generalizar para cavernas, bordas profundas, terrenos sem apoio, todas as orientações ou interiores.

Os braços têm aparência menos alongada e o apoio fica mais legível nesta inspeção. A mão ainda é uma construção procedural estilizada, com punho angular; o pedido de uma boneca mais refinada não deve ser declarado como acabamento artístico definitivo. A homologação da sequência completa pertence ao usuário.

## Higashi — falhas encontradas na primeira sequência automática

- [Dano normal letal acima de 7%, 66 PNGs](1792500804524-lethal/index.html): Husk de vida 10/máximo 20, sem armadura, três blocos à frente. Dano letal confirmado pelo servidor, câmera lateral e corpo de pé observados. **FALHOU a travessia da espada:** nos ticks de captura 34/40, a lâmina permanece próxima ao rosto/corpo do jogador e não alcança o alvo. **FALHOU a espera sem fogo:** o fogo vanilla já aparece no alvo durante CUT/HOLD, antes do marco de ignição.
- [Execução a 7%, 66 PNGs](1792500812528-threshold/index.html): Husk de vida 7/máximo 100. Câmera lateral, corpo de pé, efeito de ignição e retorno à primeira pessoa observados. **FALHOU a travessia** também neste cenário. Partículas brancas da morte vanilla aparecem durante a espera anterior à ignição. O fogo no chão herdado da execução anterior prejudica a leitura desta captura.
- Preparação próxima ao quadril é legível no tick de captura 28. A pausa e a incineração já possuem marcos separados, mas esses acertos não anulam as falhas acima.

Os JSONs Higashi desta execução conservaram os valores `FIRST_PERSON`/`THIRD_PERSON_BACK` no campo `view`, por isso foram empacotados por rotina derivada em `build/benihime-qa-71/package_higashi.py`, mantendo os JSONs originais intactos. O manifesto registra os hashes de cada PNG e a chamada FFmpeg. Não foram normalizados metadados de origem para aparentar compatibilidade com o empacotador histórico da Benihime.

## Próxima validação

Corrigir alcance/avanço visual do corte e supressão dos sinais de morte/fogo antecipados; repetir os dois cenários Higashi. Avaliar os novos frames antes de declarar a câmera e a execução prontas. Dois clientes, observador remoto, perfil gráfico Photon e escuta/mixagem real permanecem PENDENTES nesta revisão.
