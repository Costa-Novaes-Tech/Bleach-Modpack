# 135 — Correção Higashi: Morte Visual, Katana Vinculada e Pausa de 1s (Revisão Técnica Codex)

**Data:** 27/10/2026  
**Frente:** Antigravity (Higashi Execution Visuals & Choreography)  
**Branch:** `codex/ryujin-bankai-sol-selamento` (checkout `Bleach-Modpack-benihime`)  
**Base Integrada:** `fe239b7` (Feature-Poderes-bankais)  

---

## 1. Contexto, Auditoria do Vídeo 27/10 075349 e Revisão Codex

A auditoria do vídeo 27/10 075349 e a revisão técnica do Codex apontaram pontos cruciais que exigiram alinhamento rigoroso entre especificação, código e testes:

1. **Apresentação do Alvo e Fade-out Honesto:**
   - O relatório anterior mencionava fade-out gradual, porém a implementação apenas interrompia o renderer da silhueta aos 12 ticks após `IGNITE`.
   - Agora, foi implementada uma transição real: durante os 12 ticks pós-`IGNITE` (ticks 36 a 48), `AlphaTintVertexConsumer` intercepta os vértices do modelo, redirecionando o render para `RenderType.entityTranslucent(texture)` com interpolação contínua de alpha ($1.0 \rightarrow 0.0$) e escurecimento RGB (efeito de queima/carvão residual) até desaparecer por completo aos 48 ticks.
   - A silhueta foi desacoplada da entidade do mundo: cria-se uma réplica de renderização cliente (`living.getType().create(mc.level)`) com cópia de rotações, pitch, yaw e armaduras/itens empunhados. Como essa entidade nunca é inserida no mundo (`addFreshEntity`), ela não sofre ticks de rede, remoção prematura pelo servidor, queda lateral vanilla de 90° (`deathTime = 0`) nem billboard de chamas vanilla (`remainingFireTicks = 0`, `clearFire()`).
   - Entidades sem suporte a instanciação dinâmica no cliente (ex.: Players) contam com fallback seguro usando o ponteiro original com as mesmas travas de proteção de `deathTime` e `fireTicks`. Se a entidade for nula, o fallback omite a silhueta preservando áudio, partículas, câmera e controle.

2. **Preservação do Comportamento de Combate e Fogo:**
   - A supressão indevida de `setSecondsOnFire` para toda a técnica `higashi` em `ElementalTechniqueService.hit()` foi revertida.
   - Alvos sobreviventes a golpes de Higashi recebem normalmente seus 4 segundos de fogo vanilla (em Bankai).
   - Apenas alvos de execução confirmada (seja por $\le 7\%$ HP ou morte normal autoritativa confirmada por `!t.isAlive()`) têm o fogo omitido e transicionado exclusivamente pela cinemática autoral.
   - Golpes cancelados, aliados, alvos invulneráveis ou protegidos por Totem of Undying nunca autorizam execução.

3. **Restrição Estrita da Katana ao Contexto de Higashi:**
   - `RyujinActionVisuals.transform` foi restaurado integralmente ao seu comportamento original para não causar efeitos colaterais globais em outros consumidores (`RyujinBankaiEntranceClient`, `ElementalActionVisuals`, `SignatureActionVisuals`, `ZanpakutoReleaseAnimation`).
   - A orientação e translação da katana via `handRotation()` e `handPosition()` foram isoladas exclusivamente na classe interna `HigashiExecutionVisuals.Animation.get3DTransform`.
   - Trajetória autoral verificada em 4 estágios:
     - **Quadril (0..10 ticks):** Recolhimento ao quadril (`higashiHip`).
     - **Corte ascendente (10..16 ticks):** Movimento diagonal de baixo para cima (`higashiCross` $\rightarrow$ `higashiEnd`).
     - **Atravessamento (16 ticks):** Lâmina estendida e apontada à frente e para cima atravessando a posição do alvo.
     - **Pose sustentada (16..36 ticks):** Braço e katana retidos estáticos durante a pausa de exatamente 20 ticks (1,0s).

4. **Replay Completo e Câmera Lateral Estável:**
   - O relógio de apresentação inicia em `now`, garantindo visibilidade do preparo e do corte.
   - Faíscas e micro-chamas na lâmina ocorrem estritamente entre os ticks 10 e 16.
   - Câmera lateral avalia os flancos esquerdo/direito na inicialização via raycast e mantém o lado selecionado (`sideSign`) travado durante todo o corte, sem inversão de 180°.
   - Prevenção contra clipping em paredes: raycast aproxima a câmera e, se o espaço útil for $< 1.2$m, desativa a cinemática de forma segura sem prender o jogador.
   - Apenas o executor tem a câmera acionada (`mc.player.getId() == m.owner()`); observadores mantêm sua visão intacta.
   - Tolerância de 4 ticks para descartar teclas residuais mantidas no instante do clique de ataque.

5. **Pausa Aprovada de 1s (20 Ticks Nominais):**
   - Cronograma unificado:
     - `0..10`: Preparação e recuo ao quadril.
     - `10..16`: Corte diagonal com faíscas.
     - `16..36`: Pausa dramática exata de 20 ticks (1,0 segundo a 20 TPS), alvo congelado e espada transpassada.
     - `36`: Ignição / incineração com som Blaze Death e efeito Photon.
     - `36..48`: Dissolução gradual da silhueta em cinzas via `AlphaTintVertexConsumer`.
     - `48..68`: Dispersão de cinzas e fumaça; aos 68 ticks, restauração limpa da câmera, FOV, HUD e controles.

---

## 2. Matriz de Compatibilidade e Preservação

| Módulo / Frente | Estado | Garantia |
|---|---|---|
| **Hyōrinmaru** | Aprovada / Intocada | Modelos, rig, animações e geradores intocados. |
| **Ryūjin Bankai (12s)** | Aprovada / Intocada | Base solar, fumaça helicoidal, áudio e duração preservados. |
| **Senbonzakura** | Intocada | Nenhum arquivo compartilhado alterado; pronto para merge limpo. |
| **Benihime & Zaraki** | Intocadas | Lógica e assets preservados. |
| **Combate Geral Ryūjin** | Intocado | `RyujinActionVisuals.transform` restaurado; sem efeito em outros ataques. |
| **MvpRegressionTest** | 100% Passando | 149 cenários aprovados, 0 falhas. |

---

## 3. Validação Técnica e Evidências

- Compilação limpa (`./gradlew compileJava --offline`).
- Suíte completa de testes (`./gradlew check build --offline`).
- GameTests de Higashi (`HigashiExecutionGameTests`) aprovados.
- Teste manual em cliente Forge real: Declarado pendente para homologação visual interativa pelo usuário ou ambiente com display real, mantendo rigor técnico sobre evidências de renderização.
