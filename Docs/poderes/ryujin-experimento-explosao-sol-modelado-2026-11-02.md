# Ryūjin — experimento de explosão e sol modelado

02/11/2026 · `Feature-Poderes-bankais` · proposta visual para ensaio isolado, sem alteração de gameplay ou integração em runtime.

## Direção solicitada

O jogador firma os pés e empunha a katana com **uma mão, erguida à frente do corpo, ponta para cima**. Mantém essa pose durante a fala “Bankai”. Depois da palavra completa, uma explosão monumental de chamas surge **atrás** dele. O fogo se organiza em um sol com núcleo, manto e coroa tridimensionais modelados no Blockbench. Enquanto o sol se forma, a katana desce em arco contínuo até a posição da prancha: ao lado do jogador, lâmina baixa e voltada para fora do corpo. A composição deve ter a presença das grandes criaturas do Hyōrinmaru, manter jogador e katana legíveis e conservar a absorção posterior do sol pela lâmina. A explosão é cenográfica: sem dano, fogo em blocos ou alteração de alcance/custo.

Esta direção humana de 02/11 supera a restrição visual de "sem explosão" do plano 103 apenas na abertura; o selamento e os contratos de gameplay continuam como descritos ali.

[Prancha revisada: ponta para cima → explosão → descida → sol](media/ryujin-sol-experimento-2026-11-02/pose-vertical-explosao-sol-v3.png). Imagem gerada como alvo de silhueta e sequência; não é asset Blockbench, captura do cliente ou evidência de funcionamento. A [prancha anterior](media/ryujin-sol-experimento-2026-11-02/explosao-sol-absorcao-conceito.png) fica como estudo histórico do sol, não como referência de empunhadura.

## Primeiro ensaio

| Tempo real proposto | Corpo/katana | Fogo e cena |
| --- | --- | --- |
| 0–40 ticks | Preparação: braço armado sobe, uma mão na empunhadura, ponta para cima diante do tronco; outra mão junto ao corpo. Pés plantados. | Apenas prenúncio térmico atrás. Fala “Bankai” começa no tick 40 atual. |
| 40–68 ticks | Sustentar a katana vertical sem deriva perceptível de punho/item enquanto a palavra é pronunciada. | Fundo recua sob a voz. Confirmar fim audível do OGG antes de disparar a explosão. |
| 68–84 ticks | Manter pose firme; reação corporal curta à onda, sem deslocar a ponta para o rosto. | Explosão ampla atrás do jogador, primeiro expandindo para cima e lados. |
| 84–112 ticks | Katana desce em arco controlado para o lado da mão armada; ponta não cruza o corpo. | Labaredas se curvam para núcleo, manto e coroa; sol completo no fim da fase. |
| 112–140 ticks | Posição baixa e voltada para fora estabiliza; mão e lâmina viram âncoras dos feixes. | Selamento periférico e absorção progressiva até a katana. |
| 140–160 ticks | Mostrar lâmina carbonizada sem pose residual de mira. | Fogo externo cessa; aproximação moderada da câmera. |
| 160–240 ticks | Nome, pose baixa e retorno gradual ao controle. | Nome no cue vigente e restauração de câmera, HUD e VFX. |

Os ticks 68/84/112 são **alvos de ensaio**, não timings implementados. A leitura estrutural do OGG `ryujin_voice_bankai.ogg` confirmou 48 kHz/1,350 s: iniciado no tick real 40, termina nominalmente no 67, antes da explosão proposta no 68. Ainda falta escutar no cliente o começo/fim percebidos e o ducking. O código atual usa mapeamento `REAL_BEATS`/`STORY_BEATS` em `RyujinBankaiTimeline`; deslocar a explosão para depois da fala exige retimar juntos pose, câmera, VFX e testes. Preservar duração total de 240 ticks, cue nominal em 160 e os contratos de release/commit de gameplay.

O raio atual chega a 9 blocos, isto é, 18 blocos de diâmetro (`RyujinBankaiTimeline.sunRadius`). O protótipo visual testará **24 blocos de diâmetro**. Esse valor é hipótese de cena: a câmera ampla atual tem FOV 72° e posição frontal fixa; a coroa pode ser cortada e precisa de captura real antes de fixar escala. A comparação com a Hydra é de impacto visual, não de igualdade automática dos bounds do modelo.

## Rig e aparência a testar no Blockbench

- Ensaio de corpo/arma separado do sol: pose neutra → subida da katana → sustentação vertical → descida para fora → repouso. Usar o modelo efetivo do jogador e a geometria/display da Ryūjin; um bone de espada inventado não prova contato da mão no jogo. Conferir frente, perfil e terceira pessoa, destro/canhoto. A orientação final da lâmina deve coincidir com a prancha; a ponta não atravessa rosto, tronco ou mão livre.
- Ossos independentes para núcleo, manto, coroa e 12–16 línguas principais; pivôs no ponto de nascimento de cada língua. A explosão e a formação usam escala/rotação/translação desses ossos, com preparação, execução e repouso legíveis em clipes curtos.
- Núcleo amarelo claro pequeno, manto laranja com volume e sulcos escuros, coroa vermelho-escura irregular. Profundidade real em camadas; evitar esfera uniforme e sobreposição maciça de cartões voltados à câmera.
- Línguas grandes com comprimentos e ritmos diferentes; chamas menores e partículas só complementam o contorno. Preservar o orçamento atual como teto inicial: até 288 cartões e 12 partículas por tick, medindo custo no cliente antes de propor aumento.
- Exportar `.bbmodel`, `.geo.json`, textura e `.animation.json` de uma mesma fonte. Conferir bones, pivôs, UVs, clipes, continuidade nas transições e retorno ao estado neutro. Testar o asset exato em GeckoLib/Forge; preview do Blockbench sozinho não valida runtime.

## Linha de base e aceite do experimento

`regressionTest --offline --console=plain` passou com 158 cenários em 02/11: as dez curvas de gestos de fogo existentes são finitas, contínuas e retornam à animação base. Isso não valida o novo sol nem movimento visto no jogo. O GameTest Server padrão não foi executado nesta rodada porque `run-gametest/base` contém um mundo existente.

A [captura anterior do cliente](qa/cycle113/transformation-2.jpg) mostra o sol atual como uma massa brilhante de sprites que encobre parte do jogador. O código atual distribui 112 cartões internos e 20 de coroa em `RyujinSunVisual`, com sprites de fogo vanilla orientadas para a câmera. O ensaio deve comparar vídeo contínuo de preparo → fala completa → explosão → formação → descida da katana → absorção → retorno, vista frontal e perfil, dia/noite, destro/canhoto e câmera do observador. Registrar FPS/frametime somente quando medidos.

Em 02/11 o MCP Blockbench não estava exposto nesta sessão Codex e o bridge local não respondeu. O usuário autorizou explicitamente testes no Blockbench; a tentativa de abrir o aplicativo via Computer Use falhou antes da inicialização com erro de assets do kernel. Portanto, o modelo, a animação no editor e o teste visual Forge permanecem **pendentes**. A prancha e esta especificação são a entrada para a primeira sessão de modelagem; não representam homologação artística.
