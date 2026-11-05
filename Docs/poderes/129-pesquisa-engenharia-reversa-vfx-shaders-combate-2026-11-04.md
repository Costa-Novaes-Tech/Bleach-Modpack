# 129 — Pesquisa Aprofundada & Engenharia Reversa: VFX, Shaders, Modelagem 3D e Combate

**Data**: 04/11/2026  
**Contexto**: Estudo técnico e engenharia reversa de arquiteturas visuais consagradas no ecossistema Minecraft Java (Forge 1.20.1), analisando projetos como *Bleachcraft*, *Iron's Spells 'n Spellbooks*, *Mahou Tsukai*, *Cataclysm*, *Better Combat* e *PlayerAnimator*.  
**Objetivo**: Capacitar o pipeline do Antigravity para criar efeitos tridimensionais avançados, camadas luminosas/emissivas, shaders, trilhas de energia contínuas (ribbon trails) e poses de combate cinemáticas fluidas.

---

## 1. Engenharia Reversa: O Que Aprendemos dos Melhores Mods

### A. BleachCraft & Mods Anime (Getsuga, Cero, Asas e Formas Espirituais)
1. **Lâminas e Feixes de Energia (Cero e Getsuga Tensho)**:
   * Mods antigos utilizavam cubos simples ou entidades de projétil vanilla com textura estática. O resultado era truncado e sem dinamismo.
   * As versões modernas utilizam **malhas helicoidais ou de fita (ribbon strips)** geradas dinamicamente com coordenadas UV deslizantes (`UV animation`). O feixe parece "correr" para a frente porque a textura translúcida é deslocada ao longo do tempo (`uOffset = gameTime * speed`).
2. **Asas e Formas Espirituais (Bankai e Resurrección)**:
   * Em vez de geometrias sólidas e opacas que colidem estranhamente com a armadura do jogador, utiliza-se renderização em **múltiplas camadas translúcidas (Alpha Layering)** com `RenderType.entityTranslucent()` e mapeamento de brilho pleno (`LightTexture.FULL_BRIGHT = 0xF000F0`), fazendo com que o Reishi ou as chamas não escureçam mesmo à noite ou dentro de cavernas.

---

### B. Iron's Spells 'n Spellbooks (O Padrão Ouro de Magia e VFX no Forge 1.20.1)
*Iron's Spells* é referência mundial em efeitos visuais modernos no Minecraft 1.20.1. A engenharia reversa de seu pipeline gráfico revela três pilares essenciais:

1. **Camadas Emissivas com GeckoLib (`AutoGlowingGeoModel` / `GeoLayerRenderer`)**:
   * O mod separa a textura do modelo em duas:
     - `modelo.png`: Textura difusa básica com cor e sombras naturais.
     - `modelo_glow.png`: Textura contendo exclusivamente as partes de energia/luz (o resto é 100% transparente).
   * No ciclo de renderização do GeckoLib, o renderer desenha primeiro a base e, em seguida, aplica a camada `_glow` com `RenderType.eyes(texture)` ou `RenderType.entityTranslucentEmissive(texture)`.
   * **Resultado**: As asas, olhos ou símbolos arcanos brilham intensamente no escuro, mantendo a profundidade e a volumetria da base.
2. **Texturas Animadas de Fluxo (`Energy Swirl`)**:
   * Para escudos e auras concentradas, o Minecraft possui o `RenderType.energySwirl(ResourceLocation tex, float u, float v)`.
   * Esse shader nativo aplica uma camada de ruído que se desloca dinamicamente sobre a geometria do modelo, criando o clássico efeito de plasma/Reiatsu escorrendo pelo corpo do personagem.
3. **Core Shaders GLSL Customizados**:
   * O mod registra shaders próprios em `assets/<modid>/shaders/core/`.
   * Isso permite renderizar distorções de calor no ar (*heat haze*), desfoques de choque (*radial shockwave blur*) e feixes de partículas com blend aditivo (`glBlendFunc(GL_SRC_ALPHA, GL_ONE)`).

---

### C. Mahou Tsukai & Cataclysm (Círculos Mágicos, Espadas Projetadas e Asas de Anjo)
1. **Círculos Mágicos e Formações Geométricas**:
   * *Mahou Tsukai* não cria círculos mágicos usando centenas de blocos no mundo. Ele renderiza **discos e anéis bidimensionais orientados no espaço tridimensional** com texturas de runas de alta resolução (512x512) usando blend aditivo.
   * O círculo é desenhado no `RenderLevelStageEvent.Stage.AFTER_ENTITIES`, rotacionando continuamente em torno do seu eixo normal.
2. **Asas e Projeções Angélicas**:
   * No *Cataclysm* e *Mahou Tsukai*, as asas de energia são modeladas no **Blockbench** com subdivisões suaves e nós articulados (`root` -> `mid` -> `feathers`), em vez de cálculos angulares rígidos em código Java.
   * **Veredito sobre o nosso Vollständig atual**: Na análise do código [`QuincyVollstandigVisuals.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/cinematic/QuincyVollstandigVisuals.java), descobrimos que as asas da Imagem 5 foram geradas por um método matemático bruto `diamond()` desenhando 4 pirâmides rígidas de vértices com `RenderType.lightning()`. É por isso que ficaram duras e "quadradas"! A solução é migrá-las para um **modelo orgânico GeckoLib no Blockbench** com camadas luminosas.

---

### D. Better Combat & Epic Fight (Coreografias, Stances e Trilhas de Armas)
1. **Trilhas de Espada Contínuas (*Ribbon Trails*)**:
   * Quando uma katana golpeia no anime, ela não solta partículas quadradas soltas: ela deixa um arco de luz fluido.
   * Como é feito: Mantém-se uma fila circular (*ring buffer*) guardando a posição da base e da ponta da lâmina a cada frame (`PosBase`, `PosTip`).
   * No renderer, interliga-se os pontos consecutivos gerando uma fita contínua de triângulos (`TRIANGLE_STRIP` no `VertexConsumer`).
   * A fita recebe uma textura com gradiente de transparência (alfa 1.0 na lâmina decaindo suavemente para 0.0 na cauda da trilha).
2. **PlayerAnimator (KosmX) e o Segredo do *Bone Masking***:
   * O PlayerAnimator opera com uma pilha de camadas de animação (`ModifierLayer<IAnimation>`).
   * Para criar a **Postura de Combate (Stance)** perfeita sem travar o movimento do jogador:
     - **Bone Masking Parcial**: Bloqueia apenas o tronco (`body`), cabeça (`head`) e braços (`right_arm`, `left_arm`) para executar a pose samurai com a espada em guarda média e as asas estendidas.
     - As pernas continuam livres para responder ao teclado (andar, correr, agachar), combinando a pose épica com movimentação fluida.
     - Quando o jogador entra no modo de voo livre (`mayfly`), a máscara se expande para 100% do corpo, elevando as pernas em pose de suspensão aerodinâmica.

---

## 2. Diagnóstico Comparativo com o Nosso Projeto

| Efeito / Recurso | Como Está Hoje no Nosso Mod | Como Fazem os Mods de Referência | Plano de Ação para o Antigravity |
| :--- | :--- | :--- | :--- |
| **Asas do Vollständig (Quincy)** | Vértices matemáticos crus desenhados por `diamond()` em código Java (Imagem 5). Rígidos, planos e poligonais. | Modelo articulado no Blockbench com subdivisão curva e textura translúcida com brilho emissivo. | **Reconstruir em Blockbench** com 4 segmentos articulados por asa, gradiente ciano-celeste e satélites Sanrei dinâmicos. |
| **Asas do Hakuteiken (Senbon)** | Modelo GeckoLib em blocos planos com degraus de 90° e raiz deslocada à direita (Imagens 1 e 2). | Rig anatômico com volume tridimensional, penas em leque suave e ancoragem na espinha. | **Centralizar em X=0.00**, dar volume em camadas de pétalas e suavizar o recorte inferior das penas. |
| **Postura de Combate do Jogador** | Jogador em pé ereto na pose vanilla padrão segurando a espada para baixo (Imagem 3). | Stance dedicada via PlayerAnimator misturando guarda de espada com a pose das asas. | **Criar animação de stance** (Chudan-no-kamae) com lâmina erguida, corpo inclinado e asas em respiração sutil. |
| **Disparos do Quincy (Licht Regen)** | Partículas pontuais em diamante soltas no ar (Imagem 4). | Feixes de luz esticados (*lasers/energy streaks*) com iluminação plena e rastro contínuo. | **Implementar feixes de Reishi esticados** com textura emissiva e trilha de velocidade. |
| **Impacto no Solo (Hakuteiken C)** | Partículas instantâneas que somem rapidamente. | Mar persistente de energia/pétalas com decalque plano que decai ao longo de alguns segundos. | **Gerar anel de pétalas de 7m de raio** no chão com duração de 5s antes do desvanecimento suave. |

---

## 3. Próximos Passos de Implementação Técnica

1. **Frente 1 (Senbonzakura Hakuteiken)**:
   * Atualizar os parâmetros do ataque C:
     - Subida para **5.0 metros**.
     - Pausa angelical de **1.0 segundo (20 ticks)** no ar antes do mergulho.
     - Rasante veloz em direção ao chão/mira.
     - Impacto com dispersão de pétalas no chão em **raio de 7.0 metros**.
   * Travar a centralização das asas em `X = 0.00` absoluto no modelo e na renderização.
   * Criar a Stance frontal integrada no PlayerAnimator.

2. **Frente 2 (Quincy Vollständig & Reishi)**:
   * Abrir o Blockbench e esculpir as asas orgânicas de Reishi com curvatura suave, substituindo as pirâmides de `diamond()`.
   * Integrar o rig no GeckoLib com camada emissiva (`_glow`) para brilhar no escuro.
   * Refatorar os projéteis de Licht Regen para feixes luminosos contínuos.
