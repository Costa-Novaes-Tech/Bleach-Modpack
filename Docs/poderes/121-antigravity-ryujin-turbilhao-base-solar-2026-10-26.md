# 121 — Ryūjin: turbilhão de calor e base solar

26/10/2026. Branch `codex/ryujin-bankai-sol-selamento`, checkout isolado `Bleach-Modpack-benihime`. Implementação autorizada pelo Prompt 2 de efeitos da Ryūjin. Base sincronizada com `Feature-Poderes-bankais` (commit `10b0fe3`). Nenhuma alteração em branch alheia (`Bleach-Modpack` ou `Bleach-Modpack-design`). Sem commit, push ou merge não autorizados.

## Entrega implementada

Implementação de apresentação visual pura em `com.bleachmod.client.model.RyujinSunVisual`:

1. **0–2s (0..40 ticks / beats 0..12) — Ignição e Base Solar**:
   - Criação da base solar em anel elíptico ao redor dos pés (32 cards de chama, raio de até 3m).
   - Apoio e pés do jogador mantidos 100% legíveis: no setor frontal (`cos > 0.15`), a altura das chamas é reduzida para até 0,38m e a opacidade moderada a 72%, concentrando o volume visual nas laterais e na retaguarda conectada ao sol nascente.
   - Emissão de partículas de brasas e lava ascendentes pelos pés, sem criar parede de fogo diante do rosto.

2. **2–4,5s (40..90 ticks / beats 12..30) — Sol Pleno e Turbilhão Helicoidal**:
   - Sol monumental sustentado ao fundo com 112 cards de distribuição golden-angle com profundidade volumétrica e 20 línguas na coroa solar.
   - Turbilhão helicoidal largo de calor e fumaça: duas faixas helicoidais entrelaçadas (28 cards totais) cobrindo altura de 0,35m a 3,95m em raio amplo (2,3m a 2,7m), deixando cavidade central aberta para visualização integral do corpo, rosto e katana.
   - Emissão de partículas tangenciais de calor, fumaça (`SMOKE`), brasas (`ASH`) e chamas (`FLAME`) com vetor de rotação angular e ascensão térmica.

3. **4,5–7s (90..140 ticks / beats 30..52) — Selamento e Convergência**:
   - O turbilhão e a base sob os pés se contraem progressivamente conforme a absorção avança (`1.0 - progress`).
   - 4 feixes de chamas concentradas (56 cards) desprendem-se da periferia solar e percorrem trajetórias curvas fechando-se sobre a lâmina da katana.
   - Partículas convergem diretamente para as coordenadas da lâmina (`blade.hand()`), evidenciando a concentração de calor na espada.

4. **7–8s (140..160 ticks / beats 52..60) — Lâmina Carbonizada**:
   - Volume de chamas extinto instantaneamente: 0 cards de billboard ativos (sol, base e turbilhão zerados).
   - Fios sutis de fumaça e cinzas (máximo 3 partículas/tick) emanando diretamente do contato da mão com a lâmina carbonizada, garantindo visibilidade total durante a aproximação de câmera nos detalhes de guarda, mão e lâmina.

5. **8–10,5s (160..210 ticks / beats 60..112) — Nome da Bankai**:
   - Zero cards de billboard.
   - Cinzas residuais flutuando lentamente pelo ar (máximo 2 partículas/tick), preservando a legibilidade total de rosto, espada e legenda de tela ("Zanka no Tachi").

6. **10,5–12s (210..240 ticks / beats 112..140) — Devolução ao Combate**:
   - Emissores de partículas e cards completamente desligados a partir do beat 112.
   - Nenhum tornado persistente, fumaça residual acumulada ou atraso na devolução ao controle do jogador.

## Orçamento e Contratos

- **Orçamento de Cards**: Teto rígido em `MAX_FIRE_CARDS = 288`. O pico simulado atingiu 248 cards no tick 93 (durante transição sol pleno/início do selamento), zerando completamente aos 7 segundos (tick 140).
- **Orçamento de Partículas**: Teto rígido em `MAX_PARTICLES_PER_SCENE_TICK = 12`, respeitando as preferências de Minecraft (`MINIMAL` limita a 3, `DECREASED` a 6). Nos marcos finais (7–12s), o limite é reduzido para 3, 2 e finalmente 0 partículas.
- **Contratos de Apresentação e Gameplay**:
  - Duração de 240 ticks / 12s, câmera frontal, poses de jogador, áudio e sincronização mantidos intocados.
  - Regras de gameplay inalteradas: `release 60` / `commit 40`, cancelamento pré/pós commit, sem dano, sem deslocamento forçado e sem modificação de blocos ou clima.

## Validação Real e Evidências

- **Compilação**: `./gradlew compileJava --offline` → Exit 0 (1m48s).
- **Testes Unitários**: `./gradlew test --offline` → Exit 0 (1m13s).
- **GameTestServer**: `./gradlew runGameTestServer --offline` → Exit 0 (4m19s, 233 de 233 GameTests obrigatórios aprovados).
- **Regressões Globais**: `./gradlew check --offline` → Exit 0 (35s, 148 de 148 cenários aprovados).
- **Simulação Matemática**: `python tools/qa/ryujin_vortex_evidence.py` → Exit 0. Resultados registrados em `Docs/poderes/qa/cycle121/ryujin-vortex-budget.json`.

## O que testar no Minecraft real

1. Executar a Bankai da Ryūjin Jakka parado em terceira pessoa:
   - Observar as chamas surgindo aos pés nos primeiros 2s com pés visíveis.
   - Observar o turbilhão helicoidal largo de calor e fumaça subindo ao redor do corpo entre 2s e 4,5s sem tampar o tronco e rosto.
   - Observar o encolhimento do turbilhão e a convergência das línguas de fogo para a lâmina entre 4,5s e 7s.
   - Conferir o foco aproximado na lâmina carbonizada aos 7s com apenas tênues fios de fumaça.
   - Conferir a limpeza visual do rosto e legenda aos 8s durante a fala "Zanka no Tachi".
   - Conferir a entrega limpa aos 12s sem fogo ou partículas sobrando.
