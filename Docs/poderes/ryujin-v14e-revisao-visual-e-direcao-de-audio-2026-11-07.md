# Ryūjin v14e · revisão visual e direção de áudio

07/11/2026. Revisão do candidato local `Feature-Poderes-bankais`, base `9eb6a057`. O usuário aprovou o enquadramento frontal do v14e. Esta rodada registra decisões e propostas de acabamento. O frame 112 enviado pelo usuário tem o mesmo SHA-256 da captura noturna arquivada e serve como controle do novo fundo. [Implementação e provas do v14e](ryujin-v14e-olho-frontal-cinzas-2026-11-07.md).

## O que já funciona e o que falta

| Frente | Estado atual | Próxima revisão |
|---|---|---|
| Câmera, pose e lâmina | Enquadramento frontal baixo aprovado. O close final mantém rosto e katana legíveis até começar o retorno. | Preservar o plano aprovado ao comparar os efeitos. Na parede, os pés ainda tangenciam a barra inferior. Falta ampliar a validação da perspectiva de retorno e dos ângulos da pose. |
| Olho direito | A cobertura da região padrão já funciona no close. O emissor acompanha a cabeça e tem rastro lateral. | Trocar o contorno retangular por uma chama contínua, assimétrica e afilada. Validar com outras skins e oclusão de cabelo e capacete. |
| Sol e fogo | O núcleo e a corona já compõem a abertura. Há fogo na base, uma cortina atrás do jogador e luz solar opcional. | Criar profundidade atrás do sol e reduzir a repetição dos cones. Refinar a corona e o material do sol em ensaios próprios, depois de julgar a nova composição. A onda baixa de pressão segue como proposta separada. |
| Absorção e selo | Quatro correntes convergem na arma. A fumaça e as cinzas permanecem no close até depois do tempo nominal da fala. O QA confirma limpeza ao fim. | Observar movimento contínuo, leitura da katana no escuro e dissolução do novo fundo durante a absorção. |
| Áudio | Cues, redução de ganho durante as falas, fade do rugido e parada dos sons quentes já existem. O v14d tem uma captura real de áudio do motor. O v14e validou o novo quadro em perfil silenciado. | Escutar voz e mixagem em jogo. Produzir e comparar novos stems por função, com o vídeo e os marcos da v14e como guia. |
| Validação e integração | 184 regressões e ensaios Forge noturno e junto à parede no v14e. O candidato é ativado por flag. | Testar as duas mudanças visuais em movimento, medir sem captura após aquecimento e ampliar para observador, servidor dedicado e compatibilidade. Calor com refração, bloom, backends e cosméticos continuam pilotos separados. |

## Leitura das cinco imagens

1. **Primeiro close do olho.** A massa luminosa cobre a órbita e cresce para cima em uma ponta larga, inclinada e irregular. O calor se concentra no olho direito e dá assimetria ao rosto. A referência é útil para o pico de energia.
2. **Segundo close do olho.** A forma é mais legível. A base abraça o olho, afina acima da sobrancelha e abre em uma língua sinuosa, com pequenos recortes no contorno. O centro claro tem margem dourada e laranja. É a melhor referência para a silhueta principal. O olho esquerdo e a expressão seguem legíveis.
3. **Frame 112 do v14e.** O personagem, a katana e o sol já têm a escala desejada. O horizonte escuro continua visível nas laterais e entre as chamas do plano intermediário. As bases triangulares também se repetem, sobretudo no chão e no contorno solar.
4. **Figura com anel solar.** O fundo atravessa o quadro com camadas horizontais de calor e faixas verticais de fogo. Trechos escuros separam a corona das áreas de maior intensidade. Essa diferença de profundidade ajuda a destacar o personagem e a preservar o anel.
5. **Figura diante do paredão.** Línguas altas com bordas irregulares ocupam o fundo. As laterais são mais densas e a região atrás da cabeça tem contraste. A lâmina, os pés e parte do chão continuam reconhecíveis. É a principal referência para a ocupação do fundo.

## Próximos dois estudos visuais

**Olho.** Preservar a âncora que cobre os dois pixels das skins padrão. Usar uma máscara animada com a base cobrindo a órbita em todos os quadros de pico. Uma ponta dominante sobe em curva e duas pontas menores variam de altura, com núcleo claro e borda quente. O rastro lateral sai da própria chama, afina e se desfaz em poucas faíscas. Começar redistribuindo as camadas atuais. Comparar os ticks 32, 39 e 40 em frente e em três quartos. A forma deve cobrir o olho direito por completo, manter a expressão legível e acompanhar a cabeça sem parecer um retângulo colado.

**Paredão.** A cortina atual começa a cerca de 3 blocos atrás do jogador e se estende por três profundidades. O centro do sol está a cerca de 8 blocos atrás. O novo plano ocupa o fundo além da geometria solar, com alcance lateral maior que a corona. Propor uma base profunda em vermelho e âmbar para quebrar a linha do horizonte, línguas longas com larguras, inclinações e fases variadas, e picos mais quentes em alguns trechos. Deixar aberturas escuras e reservar o maior brilho para o núcleo solar. Manter chão suficiente para dar apoio aos pés e à pose.

O fogo existente antecipa a explosão no close inicial. O paredão cresce a partir do impacto no tick 70, ganha massa na sustentação e enfraquece durante a absorção. O fundo quente termina no tick 202. O close da katana fica com fumaça e cinzas. No primeiro A/B, usar a câmera e o sol do v14e e redistribuir parte dos sprites repetidos dentro do orçamento atual. Conferir separadamente o ganho de profundidade, o custo em tela e a legibilidade no dia, à noite e junto à parede.

## Insumos para o briefing de áudio

Pedir ao Manus ou à ferramenta escolhida uma demonstração guiada pela cena de 15 s e arquivos separados de voz, efeitos e música para comparar dentro do Forge. A captura do v14d serve de referência para o que já toca. O vídeo com a composição aprovada deve guiar intensidade e sincronismo. As novas falas foram inspecionadas e têm cerca de 1,81 s e 1,87 s, respectivamente. O briefing inclui duas soluções para o cruzamento de “Bankai” com a explosão e compara fogo natural com fogo usado como textura musical. [Prompt copiável e entregáveis](prompt-manus-ryujin-audio-v14e-2026-11-07.md).

| Marco no cliente | Direção para os novos efeitos |
|---|---|
| Olho acende entre ticks 20–40 (1,00–2,00 s) | Ignição curta e focada, com ar quente e uma subida discreta. |
| “Bankai” no tick 40 (2,00 s) | Voz em primeiro plano, com acento curto. Deixar espaço entre a palavra e o impacto principal. |
| Explosão no tick 70 (3,50 s) | Ataque definido, pressão grave curta e abertura do fogo nas laterais. |
| Rugido a partir do tick 72 (3,60 s) | Loop de combustão com textura e variação lenta, sustentando o volume do sol e a profundidade do fundo. |
| Absorção e selo entre ticks 176–202 (8,80–10,10 s) | Som se estreita e converge. Fecho curto e seco no selo, seguido por um respiro. |
| Nome no tick 220 (11,00 s) e retorno a partir do tick 270 (13,50 s) | Dar espaço ao nome e ao close da lâmina. Testar cinzas com textura quase silenciosa e deixar o fundo dissipar antes do fim. |

Solicitar WAV 48 kHz, pontos de entrada e saída em segundos, um loop contínuo com pontos de emenda, e versões de intensidade para comparação A/B. Fazer a escolha final com escuta no jogo, verificando clareza da fala, sincronismo, emendas, equilíbrio e encerramento. Registrar a origem e as permissões de cada arquivo antes de decidir a distribuição.
