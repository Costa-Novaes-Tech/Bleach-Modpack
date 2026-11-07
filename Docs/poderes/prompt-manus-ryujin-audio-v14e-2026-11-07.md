# Prompt para o Manus · áudio da Ryūjin Jakka

Você é o sound designer da transformação **Ryūjin Jakka / Zanka no Tachi**, uma cena de 15 segundos em Minecraft Forge. Crie áudio novo e editável para teste pessoal. A cena começa no rosto com uma chama no olho direito. A fala “Bankai” antecede uma explosão de fogo que revela um sol e um paredão ao fundo. As chamas convergem para a katana, o calor se encerra no selo e a câmera mantém a espada em close enquanto o personagem diz “Zanka no Tachi”.

Use minhas gravações `Fala-Bankai.m4a` e `Fala Zanka no Tachi.m4a`. Preserve minha interpretação, pronúncia e timbre. Entregue uma versão limpa das duas vozes e, se fizer sentido, uma segunda versão com saturação e reverberação curtas, cada uma em seu próprio arquivo. Faça a limpeza e o ganho com moderação. Mantenha uma versão seca para que o jogo possa controlar distância e mixagem.

## Pesquisa de referências

Pesquise de três a cinco cenas acessíveis e anote links diretos, minutagem e o que se ouve. Procure uma ativação de poder com fala e impacto separado, uma propagação de fogo em escala crescente, um fogo sustentado com estalos de materiais e um exemplo de colapso seguido de silêncio. Você pode usar o áudio original da cena como referência de ritmo, tensão e contraste para este uso pessoal. Crie stems novos a partir do que aprender. Registre os trechos de referência em uma lista separada e descreva como você traduziu cada recurso para esta cena. Distinga os sons que você de fato ouviu dos que você está propondo.

## Relógio e desenho de som

Os tempos abaixo são do início da cena. Alinhe os ataques aos marcos e registre o primeiro som audível de cada stem. Use a prévia para acertar as entradas e manter os sons jogáveis individualmente.

| Tempo | O que acontece | O que produzir |
|---|---|---|
| 0–2,00 s | Aproximação e chama no olho direito a partir de 1,00 s | `preheat` como ar aquecendo, poucos estalos secos e uma ignição curta e localizada no olho. A chama cresce de perto antes de ganhar escala. |
| 2,00 s | “Bankai” | A voz entra em primeiro plano. Um acento breve de pressão e brasa responde à palavra. Deixe a consoante inicial e o fim da fala audíveis. |
| 3,50 s | Impacto e abertura do sol | `flame_explosion` com ataque seco, pressão grave curta e uma abertura clara. Em `flame_spread`, crie uma segunda camada de fogo que avança e se alarga, com rajadas em sucessão e estalos próximos que ficam mais distantes. Faça as duas camadas funcionarem juntas ou separadas. |
| 3,60–8,80 s | Sol sustentado e paredão | `solar_roar_loop` com uma massa de combustão grave e irregular, movimento lento de ar e espaço entre os picos. Entregue `ember_crackle` separado, com pequenos estalos espaçados, alguns maiores e variação de material. O rugido dá escala e os estalos dão textura. |
| 8,80–10,10 s | Absorção para a lâmina | `flame_absorption` estreita o campo sonoro, puxa as camadas em direção à katana e desacelera perto do fechamento. Rugido e propagação perdem força durante essa janela e terminam no selo. |
| 10,10 s | Selo da lâmina | `blade_seal` com fecho metálico curto, um último estalo e um respiro. Uma pista opcional `seal_ash` pode sugerir cinzas e ar residual em volume baixo. |
| 11,00–15,00 s | “Zanka no Tachi”, close e retorno | Deixe a voz dominar. Dissipe música e resíduos até o fim. O retorno começa a 13,50 s. Preserve a pausa e o peso da lâmina selada. |

A gravação de “Bankai” dura aproximadamente **1,81 s**. Colocada a 2,00 s, ela chega a cerca de 3,81 s e cruza o marco da explosão. Identifique por escuta onde termina a última sílaba. Em uma versão, mantenha a explosão marcada a 3,50 s e atenue o que mascara a voz. Na outra, use um ataque mais filtrado no marco e abra o corpo do impacto logo após a sílaba final. Use a mesma gravação e o mesmo relógio nas duas. “Zanka no Tachi” dura aproximadamente **1,87 s**, de 11,00 s a 12,87 s.

## Música, instrumentos e fogo com ritmo

- **Peso e tensão.** Experimente um tambor grave de pele, como um taiko, em batidas espaçadas. Acrescente cordas graves em nota sustentada ou tremolo lento para construir pressão. Reserve a batida de maior peso para a abertura.
- **Calor e detalhe.** Experimente sopro curto e textural, metal ressonante bem discreto ou um arco raspado como cor do arranjo. Coloque essas respostas nos espaços deixados pelas falas.
- **Fogo como música.** Faça uma versão em que a propagação e os estalos entram na cadência da cena. Comece com poucos estalos isolados, deixe a combustão ganhar corpo com o sol e desmonte esse ritmo na absorção. Entregue a textura rítmica numa pista separada para comparar com a combustão natural.
- **Duas propostas.** A primeira é mais contida e prioriza o fogo e a voz. A segunda tem mais peso instrumental, com ataque grave e sustentação das cordas. Em ambas, reduza a música e o fundo durante as falas. Use as mesmas vozes e os mesmos marcos para a comparação.

## Entrega

1. Duas prévias estéreo de **15 segundos** com voz, efeitos e música sincronizados. Inclua uma prévia só com voz e efeitos para avaliarmos a leitura do fogo.
2. WAV PCM **48 kHz, 24 bits** por stem. Entregue efeitos localizáveis em mono para teste posicional e as trilhas e prévias em estéreo. Mantenha os ataques dos sons avulsos fáceis de localizar. Para `solar_roar_loop`, indique a duração e os pontos de emenda e confira a repetição.
3. Uma tabela ou manifesto com nome, função, tempo de entrada, duração, canais, nível de pico e, quando houver, início e fim do loop. Identifique os efeitos e vozes originais, as ferramentas usadas e a origem de qualquer material incorporado. Registre separadamente as permissões de uso conhecidas.
4. Uma nota curta comparando as duas versões nos pontos que importam para o teste: clareza de cada fala, peso da explosão, leitura do fogo se alastrando, naturalidade dos estalos, emenda do rugido e contraste do selo. Confira a mixagem em estéreo e em mono, com margem de pico e sem clipping. Se algum item ficar como proposta em vez de arquivo produzido, identifique isso na entrega.
