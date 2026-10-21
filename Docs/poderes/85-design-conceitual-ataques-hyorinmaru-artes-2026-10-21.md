# Design Conceitual, Artes e Plano de Execução — Daiguren Hyōrinmaru

**Data:** 21/10/2026  
**Branch:** `codex/design-poderes-criaturas`  
**Autor:** Antigravity & Equipe Bleach Mod  
**Status:** Planejamento e Artes Conceituais Homologadas  

---

## 1. Visão Geral e Identidade Visual

A **Daiguren Hyōrinmaru** (大紅蓮 氷輪丸) é a Zanpakutō de gelo mais poderosa de toda a Soul Society. Seu design transcende a estética de gelo comum do Minecraft:
- **Materialidade Glacial:** Gelo cristalino facetado, translúcido e luminescente, emitindo névoa ártica contínua.
- **Paleta de Cores:**
  - Azul Noturno Profundo (`#0B1B3D`) para o manto e sombras.
  - Ciano Glacial / Turquesa Brilhante (`#00F0FF` / `#50D8E8`) para o corpo da Hidra e asas.
  - Branco Neve Puro (`#F0FAFF`) para as arestas afiadas dos cristais.
  - Vermelho Carmesim Emissivo (`#FF1E27`) para os 6 olhos das 3 cabeças da Hidra.

---

## 2. A Transformação da Bankai — O Despertar da Hidra de 3 Cabeças

### Artes e Referências Visuais:
- **Arte Conceitual:** `Docs/poderes/media/concept/concept_bankai_hydra.jpg`
- **Validação In-Game:** `Docs/poderes/media/hyorinmaru-qa/01_bankai_transformation/screenshots/frame-t036.png`

### Passo a Passo de Execução Cinematográfica (70 Ticks / 3.5s):
1. **Ticks 0 a 15 (Convocação Solene):**
   - O Shinigami firma a base e ergue a Hyōrinmaru com ambas as mãos à frente do peito (PlayerAnimator).
   - A câmera afasta suavemente para terceira pessoa lateral/traseira.
   - Aos 12 ticks, ecoa o chamado solene: *"Bankai..."* (`voice.hyorinmaru_bankai`).
   - O chão abaixo do jogador começa a congelar em círculos concêntricos de geada.
2. **Ticks 16 a 35 (Erupção e Ascensão da Hidra):**
   - No tick 25, o solo estala em som de vidro temperado quebrando (`GLASS_BREAK`).
   - O modelo Blockbench da **Hidra Glacial de Três Cabeças** (`ice_hydra.geo.json`) irrompe do solo diretamente atrás do jogador, subindo 2 blocos no ar com as 3 cabeças serpentinas ondulando.
3. **Ticks 36 a 55 (Nomeação e Rugido Triplo):**
   - No tick 40, soa a proclamação do nome: *"Daiguren Hyōrinmaru!"* (`voice.hyorinmaru_name`) acompanhada pelo rugido abafado do dragão.
   - As asas colossais de cristal da Hidra desabrocham.
   - No tick 55, as 3 mandíbulas móveis abrem-se simultaneamente no rugido definitivo (`hyorinmaru.roar`), gerando uma onda de choque de partículas `SNOWFLAKE`.
4. **Ticks 56 a 70 (Cristalização do Manto e Liberação):**
   - As asas de gelo, cauda segmentada e as **12 pétalas de lótus flutuantes** (3 flores de 4 pétalas) cristalizam-se no corpo do jogador (`IceArmorLayer`).
   - A câmera retorna suavemente para a visão de combate em primeira pessoa (ou terceira pessoa livre).

---

## 3. Slot 1 — Ryūsen Hyōketsu (Ataque Triplo dos Dragões / Sopro da Hidra)

### Artes e Referências Visuais:
- **Arte Conceitual:** `Docs/poderes/media/concept/concept_slot1_ryusen_hyoketsu.jpg`
- **Validação In-Game:** `Docs/poderes/media/hyorinmaru-qa/03_slot1_ice_dragons/screenshots/frame-t020.png`

### Conceito & Fantasia:
Diferente da Shikai (que dispara um único dragão), a Bankai canaliza a força da Hidra de três cabeças para disparar uma **revoada tripla em leque** de dragões de gelo colossais.

### Passo a Passo de Execução:
1. **Gesto do Jogador:** Corte horizontal rápido da direita para a esquerda, liberando uma rajada de vento glacial.
2. **Disparo das Entidades:**
   - Três dragões colossais (`IceDragonEntity`) surgem em formação de leque nas direções `0°` (central), `-25°` (flanco esquerdo) e `+25°` (flanco direito).
   - Cada dragão possui 5.25x de escala visual, voando a 0.8 blocos/tick por até 24 blocos de distância.
   - Deixam um rastro denso de geada, gelo triturado e partículas árticas.
3. **Impacto no Alvo:**
   - Causa 12 pontos de dano espiritual direto.
   - Aplica congelamento profundo (Nível II de Lentidão + Efeito de Gelo com congelamento de tela por 60 ticks).

---

## 4. Slot 2 — Ryūsenka (Flor de Gelo do Dragão - Investida Perfurante)

### Artes e Referências Visuais:
- **Arte Conceitual:** `Docs/poderes/media/concept/concept_slot2_ryusenka.jpg`
- **Validação In-Game:** `Docs/poderes/media/hyorinmaru-qa/04_slot2_ryusenka/screenshots/frame-t016.png`

### Conceito & Fantasia:
O golpe de estocada mais veloz e letal de Hitsugaya. Uma investida em linha reta que transpassa o oponente, aprisionando-o em um casulo gigantesco em forma de cruz/estrela de cristal de gelo antes de estilhaçá-lo.

### Passo a Passo de Execução:
1. **Preparação (0–4 Ticks):**
   - O Shinigami agacha levemente e estende a espada à frente; as asas de gelo nas costas batem fortemente para trás, criando um impulso de vento comprimido.
2. **Investida Supersônica (Ticks 5–12):**
   - O jogador projeta-se até 14 blocos para a frente em velocidade máxima, deixando um rastro de geada suspensa no ar.
   - Qualquer inimigo na trajetória é atravessado pela estocada de gelo.
3. **Florescimento da Cruz de Gelo (Ticks 13–35):**
   - Ao redor do inimigo perfurado, uma gigantesca escultura de gelo em forma de **estrela de 4 pontas / cruz floral** brota instantaneamente, paralisando-o completamente.
4. **Detonação Final:**
   - O casulo floral explode em dezenas de estilhaços cristalinos pontiagudos, aplicando dano em área secundário aos inimigos próximos.

---

## 5. Slot 3 — Sennen Hyōrō (Prisão Glacial das Mil Flores de Gelo)

### Artes e Referências Visuais:
- **Arte Conceitual:** `Docs/poderes/media/concept/concept_slot3_sennen_hyoro.jpg`
- **Validação In-Game:** `Docs/poderes/media/hyorinmaru-qa/05_slot3_sennen_barrier/screenshots/frame-t024.png`

### Conceito & Fantasia:
Uma das técnicas defensivas e de controle de multidão mais icônicas. Dezenas de pilares hexagonais de gelo que se erguem do solo até o céu, fechando-se em uma gaiola impenetrável que congela e esmaga tudo em seu interior.

### Passo a Passo de Execução:
1. **Invocação:** O Shinigami crava a ponta da Zanpakutō no solo; runas de geada luminosa irradiam em círculo até o alvo selecionado.
2. **Erupção dos Pilares (Ticks 0–20):**
   - Dezenas de monólitos de gelo maciço erguem-se verticalmente em formato circular ao redor do alvo (raio de 4 a 6 blocos).
   - O som de estalos de gelo e rochas subindo ressoa no campo.
3. **Convergência e Esmagamento (Ticks 21–60):**
   - Os pilares inclinam-se para dentro, unindo suas pontas e formando uma cúpula hermética.
   - Qualquer criatura no interior fica completamente aprisionada, recebendo dano contínuo de frio extremo e impedida de se teletransportar ou atacar para fora.
4. **Dissipação:** Ao término da duração (3 a 5 segundos), a prisão racha e se dissolve em neve fofa.

---

## 6. Slot 4 — Hyōten Hyakkasō (Funeral das Cem Flores do Céu Congelado)

### Artes e Referências Visuais:
- **Arte Conceitual:** `Docs/poderes/media/concept/concept_slot4_hyoten_hyakkaso.jpg`
- **Validação In-Game:** `Docs/poderes/media/hyorinmaru-qa/06_slot4_blizzard_zone/screenshots/frame-t024.png`

### Conceito & Fantasia:
O ápice da Daiguren Hyōrinmaru. O clima se transforma: um buraco negro se abre no manto de nuvens do céu, de onde caem flocos de neve cristalinos brilhantes como flores. Onde cada floco toca, uma flor de gelo brota; quando a centésima flor desabrocha, a vida do oponente chega ao fim.

### Passo a Passo de Execução:
1. **Invocação Celestial (Ticks 0–20):**
   - O jogador ergue a lâmina ao céu. Um vórtice de nuvens escuras surge no zênite com um olho circular luminescente no centro.
2. **A Chuva de Flores de Neve (Ticks 20–120):**
   - Flocos de neve hexagonais gigantes e brilhantes caem suavemente cobrindo uma área de 30 blocos de raio.
   - O vento ártico cria uma nevasca contínua sem depender da chuva vanilla do Minecraft.
3. **Germinação das Flores de Gelo:**
   - Cada inimigo dentro do raio recebe acúmulos de "Pétala Congelada".
   - Conforme os acúmulos sobem (1, 10, 50, 100), pequenas esculturas de flores de lótus de gelo vão brotando sobre a cabeça, braços e pernas do inimigo, reduzindo sua velocidade a zero.
4. **Petrificação Glacial:**
   - Ao atingir o ápice, o inimigo se transforma em uma estátua permanente de gelo cristalino, sofrendo dano crítico massivo.

---

## 7. Próximos Passos e Melhorias no Mod

1. **Mecânica das 12 Pétalas Decadentes:**
   - Integrar um contador no HUD e na camada `IceArmorLayer`: a cada 20 segundos de Bankai ativa, uma pétala de lótus murcha e cai em partículas.
2. **Shader Emissivo nos 6 Olhos da Hidra:**
   - Configurar o canal emissivo do GeckoLib para que os olhos vermelhos das 3 cabeças brilhem no escuro e durante tempestades.
3. **Áudio Mono OGG Oficial:**
   - Gravação e inserção das falas originais em japonês do dublador oficial do Hitsugaya para *"Bankai... Daiguren Hyōrinmaru"*, ajustadas em curva de atenuação 3D estrita.
