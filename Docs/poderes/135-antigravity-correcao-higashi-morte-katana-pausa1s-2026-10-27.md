# 135 — Correção Higashi: Morte Visual, Katana Vinculada e Pausa de 1s

**Data:** 27/10/2026  
**Frente:** Antigravity (Higashi Execution Visuals & Choreography)  
**Branch:** `codex/ryujin-bankai-sol-selamento` (checkout `Bleach-Modpack-benihime`)  
**Base Integrada:** `fe239b7` (Feature-Poderes-bankais)  

---

## 1. Contexto e Diagnóstico do Vídeo 27/10 075349

O teste em vídeo revelou anomalias na apresentação visual da execução de **Higashi: Kyokujitsujin**:
1. **Entrada de câmera tardia / corte invisível:** O relógio da pose em `HigashiExecutionVisuals.accept()` subtraía `HigashiCinematicPath.IMPACT` (12 ticks), fazendo a cena iniciar já no corte final, suprimindo a preparação (ticks 0 a 10) e a aproximação visível.
2. **Katana estática ("braço mexendo com espada parada"):** Em `RyujinActionVisuals.transform()`, a rotação do osso `"rightItem"` verificava apenas `pose.item()`. Como as poses autorais de Higashi (`higashiHip`, `higashiCross`, `higashiEnd`) definiam `item = Vec3.ZERO` e orientavam a espada via rotação do punho (`pose.handRotation()`), o PlayerAnimator forçava a katana à rotação base (0, 0, 0), desvinculando-a do movimento da mão.
3. **Fogo vanilla antecipado:** Em `ElementalTechniqueService.hit()`, chamava-se `target.setSecondsOnFire(4)` no início do impacto, gerando o billboard vertical de chamas de Minecraft antes da incineração do efeito.
4. **Morte vanilla (queda lateral de 90° e puff de fumaça branca):** Ao morrer, o servidor enviava o status de entidade 3 (`handleEntityEvent(3)`), que incrementava `deathTime`, disparando a rotação de 90° (`Axis.ZP`) típica da morte vanilla. O evento 60 disparava o puff padrão de fumaça branca de morte de mob.
5. **Sumiço abrupto do alvo ("pop"):** O renderer alternativo `silhouette` cortava bruscamente a malha aos 6 ticks após `IGNITE`, fazendo o corpo desaparecer subitamente enquanto partículas ainda estavam ativas.

---

## 2. Soluções Implementadas em 3 Fases / Commits

### Commit 1 (`72d3315`): Apresentação Estável do Alvo e Supressão de Morte Vanilla
- **`ElementalTechniqueService.java`:**
  - Suprimido o `setSecondsOnFire(4)` para a técnica `higashi`, evitando chamas verticais vanilla antes ou durante a execução.
- **`HigashiDeathParticleMixin.java`:**
  - Interceptação de eventos do cliente expandida: cancela tanto o evento 60 (puff de fumaça de morte) quanto o evento 3 (status de morte vanilla que deita a entidade lateralmente) enquanto `HigashiExecutionVisuals.isExecuting(id)` estiver ativo.
- **`HigashiExecutionVisuals.java`:**
  - `isExecuting(id)` cobre todo o ciclo de vida da execução (`< END`).
  - `hideIncinerated()` suprime a renderização padrão de `LivingEntityRenderer` e limpa continuamente o fogo do alvo.
  - `silhouette()` preserva a malha congelada e ereta na pose exata do impacto até o momento da incineração (`IGNITE`), aplicando um fade out gradual de alpha (1.0 -> 0.0) durante 12 ticks após `IGNITE` em perfeita sincronia com a dispersão das cinzas do Photon/partículas.

### Commit 2 (`fc8a76a`): Katana Vinculada, Replay Autoral e Câmera Lateral Estável
- **`RyujinActionVisuals.java`:**
  - Vinculado o canal de item (`"rightItem"`) para assumir `pose.handRotation()` e `pose.handPosition()` quando `pose.item()` for neutro. Assim, a katana acompanha fidedignamente o punho, corte e bainha autorais em todo o arco.
  - Método `world()` tornado package-private para cálculo de coordenadas de partículas na lâmina.
- **`HigashiExecutionVisuals.java`:**
  - Relógio de apresentação em `accept()` ajustado para iniciar em `now`, garantindo replay visual completo da preparação, corte ascendente e passagem da lâmina.
  - Efeito de faíscas e micro-chamas (`ELECTRIC_SPARK` e `SMALL_FLAME`) emitido ao longo da lâmina durante a passagem do corte (ticks 10 a 16).
  - Câmera lateral travada: a seleção de flanco esquerdo/direito é realizada uma única vez em `accept()` avaliando desobstrução de colisão por raycast, evitando inversões bruscas de 180° durante o golpe.
  - Proteção contra toque involuntário de teclado: descarte por teclas de movimento/ataque só é habilitado após `age > 4`.

### Commit 3: Timing da Pausa Dramática de 1s (20 Ticks), Cinemática e Testes
- **`HigashiCinematicPath.java`:**
  - `IGNITE = SLASH_END + 20` (36 ticks nominais: exatamente 1,0 segundo a 20 TPS pós-corte).
  - `END = IGNITE + 32` (68 ticks nominais: dispersão completa de cinzas e liberação limpa de controle).
- **`RyujinChoreography.java`:**
  - A coreografia autoral referencia dinamicamente `HigashiCinematicPath.IGNITE`, retendo a pose estática do corpo e da espada transversal exatamente durante esse 1,0 segundo (ticks 16 a 36), antes de retornar a neutral aos 68 ticks.
- **`HigashiExecutionGameTests.java`:**
  - Atualizado o teste de timing para `cinematicHoldsForOneSecondAfterBladePassesAndReturnsAfterAsh`, verificando `IGNITE - SLASH_END == 20` e retenção da pose autoral até tick 35.

---

## 3. Matriz de Preservação e Compatibilidade

| Componente | Estado | Verificação |
|---|---|---|
| **Hyōrinmaru** | Aprovada / Intocada | Modelos, rig, animações e gerador mantidos idênticos à entrega homologada. |
| **Ryūjin Bankai (12s)** | Aprovada / Intocada | Base solar, fumaça/cinzas helicoidais, áudio e duração preservados. |
| **Senbonzakura** | Intocada | Sem alterações de assets ou classes exclusivas da branch Senbon. |
| **Benihime & Zaraki** | Intocadas | Nenhuma alteração de identidade ou lógica de combate. |
| **MvpRegressionTest** | 100% Passando | 149 cenários de regressão executados com sucesso (0 falhas). |

---

## 4. Evidências de Validação

- `git status --short --branch`: limpo em `codex/ryujin-bankai-sol-selamento`.
- `./gradlew check build --offline`: Sucesso com todas as suites de testes aprovadas e JAR gerado.
