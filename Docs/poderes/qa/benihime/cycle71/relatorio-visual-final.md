# Ciclo 71 — relatório visual final para testes

20/10/2026. Branch `Feature-Poderes-bankais`, commit de referência `e967748d42e3e76a78d74889b70e7aadcb49bcfc`, com alterações locais coordenadas pelo agente principal. QA visual no cliente real Forge 1.20.1/47.4.10 e Java 17. Nenhuma alteração Zaraki, commit, push ou mundo pessoal realizado por esta frente de QA.

**Resultado:** marcos principais observados e correções de bloqueadores verificadas no perfil base; pronto para avaliação artística do usuário. A homologação completa da feature continua pendente dos limites descritos abaixo.

## Evidências que representam a entrega

- [Benihime — bancada plana, 286 PNGs](1792500668247/index.html).
- [Benihime — transformação real diurna, 284 PNGs](1792500703182/index.html).
- [Benihime — transformação real noturna, 282 PNGs](1792500735371/index.html).
- [Benihime — terreno irregular, 287 PNGs](1792500768530/index.html).
- **[Higashi final — dano normal letal acima de 7%, 62 PNGs](1792501538866-lethal/index.html).**
- **[Higashi final — execução a 7%, 66 PNGs](1792501546296-threshold/index.html).**

Cada galeria preserva PNGs originais, metadados, hashes e uma montagem de vídeo amostrada sem áudio. Os PNGs vieram de `Minecraft Screenshot.grab framebuffer`. A montagem usa os intervalos reais `wallMs`, sem interpolação; não é vídeo contínuo de todos os quadros renderizados. As pranchas `contact-sheet.jpg` da Higashi são derivados para consulta, não substituem os originais.

As [falhas da primeira sequência](revisao-visual-intermediaria.md) e o [reteste anterior ainda sem contato suficiente](reteste-higashi.md) permanecem arquivados. Não usar esses vídeos anteriores como representação do contato final da Higashi.

## Benihime

- **PASSOU nos marcos inspecionados — ocultação:** cortina opaca inteiramente fechada no tick 120, sem fresta revelando a figura. Mão aparece pela abertura central, com interior escuro.
- **PASSOU nos marcos inspecionados — proporções e apoios:** braços mais compactos que na versão anterior; cotovelos dobrados e palmas perto do gramado. Os marcos 280/320 mostram avanço perceptível do corpo com apoio. Não equivalem a simulação física completa de peso.
- **PASSOU no cenário controlado — terreno:** os pads de referência/+1/+2 blocos do save QA são acompanhados pelas mãos e pela adaptação do tronco no marco lateral 320. Não foram validadas todas as superfícies, buracos, encostas ou interiores.
- **PASSOU nos marcos inspecionados — postura final:** jogador de pé, espada para baixo, enquadramento frontal e manifestação com cotovelos baixos, evitando braços horizontais. FOLLOW registrado após deslocamento/giro.
- **PENDENTE — acabamento artístico:** mãos/rosto/roupas continuam procedurais e estilizados. Não declarar que o modelo já possui todo o refinamento de uma boneca real ou aprovação estética definitiva.

## Higashi — bloqueadores corrigidos e observados

Foram testados um Husk sem armadura com vida 10/máximo 20, morto pelo dano normal do golpe, e um Husk com vida 7/máximo 100 para a execução por limiar.

- **PASSOU — início próximo ao quadril:** preparação com lâmina baixa precede o corte.
- **PASSOU nos marcos finais — contato:** o jogador se aproxima, permanece junto ao chão e executa o corte diagonal ascendente. No tick de captura 34 do cenário letal, a lâmina penetra o corpo do Husk e o alvo reage vermelho ao impacto; no mesmo marco do cenário de limiar, o contato também é visível. A folga encontrada nas duas versões anteriores foi corrigida.
- **PASSOU nos marcos HOLD — espera:** corpo mantido em pé, sem fogo vanilla e sem poof branco de morte antecipados. Permanecem partículas do próprio corte, distintas da ignição do corpo.
- **PASSOU nos marcos inspecionados — câmera:** composição lateral com os dois participantes, mantida durante a pausa e a incineração.
- **PASSOU — incineração e retorno:** ignição seguida por desaparecimento do corpo e partículas de fogo/cinzas. Ambos retornam à primeira pessoa/HUD no tick de captura 112, após o início da incineração. Partículas ainda vivas após o retorno não significam câmera presa.

No cenário de limiar, as amostras de captura 36→76 (referência da pausa de 40 ticks) têm 1994 ms de intervalo real. No letal, a amostra mais próxima disponível foi 36→74, com 1975 ms; não afirmar que houve frame capturado no tick 76 dessa execução. O relógio textual da sequência começa no preparo QA, que agenda o cast 20 ticks depois; latência de recebimento do pacote pode deslocar os marcos gráficos em relação a esse rótulo.

## Desempenho e procedência

Ambiente gráfico registrado: Intel Iris Xe, resolução 1280×720, heap de 2 GiB e distância de renderização 6. Consultar os `environment.json` originais de cada galeria para o ambiente efetivo.

O FPS informado pelo Minecraft durante o primeiro cenário final letal variou de 2 a 54, mediana 26, com aquecimento/geração inicial e amostras ausentes no começo (primeiro tick disponível 9). Na execução de limiar seguinte, variou de 53 a 95, mediana 80. **Não é benchmark de frametimes**, nem prova de fluidez constante. [Resumo numérico das capturas](final-capture-summary.json).

O controle manual via `@oai/sky` travou por 522,4 s e foi interrompido, sem inputs enviados. Foi substituído por automação de desenvolvimento limitada a save novo `Benihime-QA-68-<timestamp>`. A tentativa de gravação contínua FFmpeg/gdigrab falhou porque o título da janela mudou; nenhum vídeo contínuo foi produzido. [Ambiente/limitações de captura](capture-environment.json).

## O que esta QA não aprovou

- Escuta e mixagem dos áudios escolhidos pelo usuário: não houve loopback de áudio disponível nem captura de microfone. Logs de cues não equivalem a avaliação humana de som.
- Dois clientes reais, observador remoto e sincronização em servidor dedicado com jogadores.
- Efeitos gráficos/editor Photon no cliente: os testes de servidor desse perfil pertencem ao relatório de build do agente principal, não a esta inspeção visual.
- Todas as condições de interiores, terreno sem suporte, skins classic/slim, canhoto e mão secundária ocupada.
- Cancelamentos e retomada de câmera em todas as condições do jogo, além do retorno observado ao terminar a cena.
- Julgamento artístico final ou benchmark controlado de desempenho.

A validação Gradle final foi executada sequencialmente pelo agente principal, após encerrar o cliente: base e Photon passaram com 127 regressões/151 GameTests (1m41s e 1m32s). Não reutilizar como resultado final a rodada que concorreu com compilação de cliente e falhou por falta de classes; consultar os logs finais da entrega.
