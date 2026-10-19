# Ciclo 37 — Berserk no máximo: intensidade, aura em espiral e golpe de martelo

Data: 15/10/2026. Branch: `bankai-zaraki`. Base: `3c74bef` (modelos refeitos e aprovados pelo usuário).

## Pedidos do usuário

1. Mais poder de destruição: mais partículas na transformação e nos ataques; animação mais longa, com mais ódio e rugido de raiva ("pode botar pra torar").
2. Aura vermelha em espiral em volta do corpo durante todo o Berserk Mode.
3. Zaraki carrega a espada apoiada no ombro e ataca martelando o inimigo.

## Decisão de orçamento visual

O contrato limita cada técnica a `VisualFrame.TECHNIQUE_LIMIT = 1056` e proíbe abrir frames extras para contornar o limite. Por pedido explícito do usuário, foi criado `VisualFrame.overdrive` (até **4000** partículas por frame, espalhamento até 10), usado **só** por `BerserkVisuals`. Os outros kits seguem com os limites anteriores. Audiência, alcance de 32 blocos e checagem de chunk não mudaram.

## O que mudou

| Área | Arquivo | Mudança |
| --- | --- | --- |
| Visuais | `BerserkVisuals` | Cerca de 2,5× mais partículas; destroços do bloco do chão (`BlockParticleOption`), explosões ao longo das fendas, pilar com 3 hélices e `EXPLOSION_EMITTER`, novo `impact` (cratera). Nenhum bloco é alterado |
| Cinemática (servidor) | `NozarashiReleaseEffects` | 140 ticks (7 s): tremor e batimento acelerando, 3 socos no chão (ticks 24/32/40), erupção com rugido em camadas (45), segundo rugido (68), pegada da espada (112) e golpe de martelo no chão com fenda de 18 blocos (124). Empurrão em raio 12 |
| Rugido | `NozarashiReleaseEffects.roar` | Warden + Ravager + Ender Dragon + Hoglin, com tom mais grave |
| Tremor de tela | `ReleaseCinematicS2C` tipo `impact` + `CinematicDirector.shake` | Qualquer jogador a até 40 blocos sente impactos fortes; a intensidade cai com a distância |
| Cinemática (cliente) | `CinematicDirector` | Tomadas retemporizadas para 7 s; pulso vermelho a cada soco, flash no martelo final, título no impacto |
| Animação | `tools/animations/berserk_release.py` | Socos no chão, dois rugidos com a cabeça para trás e as mãos em garra, golpe final, terminando na pose do ombro |
| Técnicas | `NozarashiTechniqueService` | Todas com golpe de cima para baixo (`swing`), impactos, mais anéis e rugidos; tremor proporcional à força |
| Golpe comum | `NozarashiTechniqueService.meleeHammer` | Todo golpe com a Nozarashi abre uma cratera no alvo (força por forma); não se repete em acertos de técnica |
| Aura em espiral | `client/berserk/BerserkAura` | 3 fios vermelhos subindo em hélice a cada tick, brasas, fumaça e anel nos pés. Partículas locais, sem rede, visíveis a até 64 blocos. Substitui a aura do servidor no Berserk |
| Espada no ombro | `client/berserk/NozarashiStance` + `nozarashi_shoulder.json` | Camada PlayerAnimator (prioridade 500) em loop, só no braço direito e no item, para que as pernas continuem andando normalmente |
| Martelada | `nozarashi_hammer.json` | Camada de prioridade 600 tocada a cada swing (golpe comum ou técnica), também visível para outros jogadores |

A cinemática (prioridade 1000) sobrepõe as camadas do ombro e do martelo enquanto toca.

## Validação

- `compileJava` e `regressionTest`. O teste novo lê as três animações com o próprio `AnimationSerializing` do PlayerAnimator e confere duração, loop e braço direito.
- `runGameTestServer`: 63/63 em duas execuções. Na primeira, o teste de clima da Hyōrinmaru falhou de novo de forma intermitente (estado global de clima compartilhado; não tem relação com este ciclo).
- **Não verificado no cliente:** sinais e ângulos das poses (convenção Bedrock), o encaixe da lâmina sobre o ombro, a densidade real de partículas e o FPS com vários jogadores.

## Ajustes rápidos

- Pose do ombro: `REST_ARM` e `REST_ITEM` em `tools/animations/nozarashi_stance.py`, depois rode o script.
- Ritmo da cinemática: constantes em `NozarashiReleaseEffects` e em `tools/animations/berserk_release.py`; os dois precisam continuar sincronizados (o teste confere a duração).
- Densidade: contagens em `BerserkVisuals`; aura em `BerserkAura.STRANDS/STEPS`.

## Limite conhecido

A pose do ombro e a martelada só aparecem em terceira pessoa; em primeira pessoa a mão segue o padrão do jogo.
