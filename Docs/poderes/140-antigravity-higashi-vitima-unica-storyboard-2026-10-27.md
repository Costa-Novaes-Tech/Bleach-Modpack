# 140 — Higashi por Quadros: Vítima Única, Katana Vinculada e Storyboard HG-F00..F08

**Data:** 27/10/2026  
**Frente:** Antigravity (Execução Higashi — Storyboard & Visual Lifecycle)  
**Branch:** `codex/ryujin-bankai-sol-selamento` (checkout isolado `Bleach-Modpack-benihime`)  
**Base Integrada:** `fe239b7` (Feature-Poderes-bankais)  
**Status do Merge:** **SEM MERGE NESTA RODADA** (Trabalho restrito à branch de desenvolvimento para auditoria humana)

---

## 1. Contexto e Resolução das Falhas do Vídeo 092434

O teste do usuário no vídeo `092434` confirmou um cenário fundamental de gameplay: **todas as execuções foram decorrentes de dano normal letal** (nenhum alvo estava artificialmente abaixo de 7% de HP prévio). Nesse fluxo prioritário, duas anomalias visuais graves foram identificadas e corrigidas:

### 1.1 Duplicidade de Vítimas ("Cadáver Vermelho" + "Representação para Cinzas")
- **Causa Raiz Identificada:** No Minecraft Forge padrão, o evento `RenderLivingEvent.Pre` é disparado exclusivamente dentro de `LivingEntityRenderer`. Contudo, entidades de mods baseadas em GeckoLib (como o `HollowRenderer` do Bleach Mod) estendem `GeoEntityRenderer`, que por sua vez estende diretamente `EntityRenderer`, contornando totalmente o `RenderLivingEvent.Pre`. Com isso, a entidade original continuava sendo desenhada pelo despachante de renderização enquanto sofria animação de morte vanilla (piscando em vermelho e tombando), ao mesmo tempo em que a silhueta de apresentação era renderizada sobreposta na fase `AFTER_ENTITIES`.
- **Solução Definitiva:** Criação do mixin mestre `HigashiEntityRenderMixin`, injetado em `EntityRenderDispatcher.render(HEAD)` com cancelamento preventivo (`ci.cancel()`). Como o `EntityRenderDispatcher` é o despachante universal de todas as entidades do jogo (vanilla, custom e GeckoLib), a entidade original do mundo é completamente suprimida da tela (incluindo modelo, camadas, chamas vanilla, sombras e nametags) enquanto `HigashiExecutionVisuals.isExecuting(entityId)` for verdadeiro.

### 1.2 Regressão de Variante em Hollows Alados
- **Causa Raiz Identificada:** A criação dinâmica da réplica no cliente via `entity.getType().create(level)` instanciava o mob com os valores padrão de NBT (o Hollow bípede/brute terrestre), perdendo a variante alada/voadora que estava ativa no mundo.
- **Solução Definitiva:** Em `createPresentationSilhouette`, os dados sincronizados (`SynchedEntityData`) são copiados através de `getNonDefaultValues()` e aplicados à réplica (`assignValues`). Adicionalmente, é feita verificação de tipo explícita para `HollowEntity`, invocando `hReplica.setVariant(hLiving.getVariant())`. Dessa forma, o `GeoModel` carrega corretamente `hollow_flying.geo.json` e a textura alada correspondente.

### 1.3 Preservação de Texturas Multi-Layer no Fade-Out
- **Causa Raiz Identificada:** O redirecionamento cego de buffers para `RenderType.entityTranslucent(getTextureLocation(actor))` substituía a textura de todos os sub-renderers (como olhos e asas) pela textura base da pele.
- **Solução Definitiva:** Desenvolvido `AlphaTintVertexConsumer`, que atua como decorador do `VertexConsumer` original do próprio `RenderType` emitido por cada camada do modelo. Ele interpola a opacidade alfa ($1.0 \rightarrow 0.0$) e escurece as cores para simular carbonização (fator mínimo de 0.20), ativando o blend do OpenGL via `RenderSystem.enableBlend()` sem corromper nenhuma textura ou mapeamento UV.

### 1.4 Katana Baixa / Desalinhada do Braço
- **Causa Raiz Identificada:** No PlayerAnimator, aplicar `execPose.handRotation()` ao bone `"rightItem"` sobrescrevia a rotação nativa de empunhadura do item model (`rotation: [0, -90, 55]`), fazendo a lâmina inclinar para baixo em relação à mão. Além disso, `ElementalActionVisuals` mantinha um gesto residual ativo que competia contra o PlayerAnimator de Higashi.
- **Solução Definitiva:**
  1. Em `HigashiExecutionVisuals.Animation`, o canal de rotação de `"rightItem"` agora utiliza estritamente `execPose.item()` (que é `Vec3.ZERO`), e a posição utiliza `Vec3.ZERO`, mantendo o punho firmemente ancorado na palma da mão e permitindo que a katana acompanhe perfeitamente a elevação angular do braço direito.
  2. `ElementalActionVisuals.gesture(p)` agora cede imediatamente (`return null`) sempre que `HigashiExecutionVisuals.isExecuting(p.getId())` estiver ativo, e `accept()` purga qualquer visão residual pendente no mapa `VIEWS`.

---

## 2. Alinhamento Estrito ao Storyboard HG-F00..F08 / Ticks 000..068

Em conformidade com a direção autoral de `Docs/poderes/137-storyboards-direcao-katanas-senbon-higashi-2026-10-27.md` e os quadros navegáveis em `media/137-storyboards/`:

```
[T000..T010] Preparação no quadril  ──> [T010..T016] Corte diagonal ascendente
                                                         │
[T036] Ignição e som Blaze Death    <── [T016..T036] Pausa dramática de 1s (20t)
       │
[T036..T048] Dissolução em cinzas   ──> [T048..T068] Dispersão e devolução de controle
```

| Marco | Tick (Tempo) | Comportamento do Jogador e Katana | Comportamento da Vítima | Câmera e Render |
|---|---|---|---|---|
| **HG-F00** | `T000` (0,00s) | Confirmação autoritativa pelo servidor; início do replay visual no cliente. | Vítima única, serena e intacta. Supressão do modelo original via mixin. | Câmera lateral estabelecida sem clipping; HUD oculta. |
| **HG-F01** | `T005` (0,25s) | Katana recolhe junto ao quadril direito (`higashiHip`). Cotovelo flexionado. | Alvo imóvel e intacto; sem queda vanilla, sem flash vermelho. | Enquadramento lateral mantém agressor e vítima no campo. |
| **HG-F02** | `T010` (0,50s) | Mão baixa, lâmina em preparação para subida (`Y < 0.5m`). | Alvo sereno aguardando o golpe. | Início da emissão de faíscas discretas na lâmina. |
| **HG-F03** | `T012` (0,60s) | Corte diagonal ascendente cruzando o peito; katana rigidamente presa ao punho. | Lâmina transpassa o volume da vítima sem acionar animação vanilla. | Partículas de corte elétrico e chamas sutis na lâmina. |
| **HG-F04** | `T016` (0,80s) | Final do corte em guarda alta. Ponta elevada ($Y > 2.2m$, $Z > 0.6$). | Alvo transpassado, sem chamas de chão e sem tombamento. | Fim das faíscas da lâmina; câmera estável no plano lateral. |
| **HG-F05** | `T026` (1,30s) | Metade da pausa dramática (10/20 ticks); postura alta perfeitamente congelada. | Vítima congelada na pose de impacto, íntegra e sem sofrer nova agressão. | Sem inversão de 180° na câmera lateral. |
| **HG-F06** | `T036` (1,80s) | Conclusão exata dos 20 ticks (1s) de pausa. Início da incineração. | Som local `BLAZE_DEATH` e trigger `PhotonPilotBridge.incinerate`. | Início da dissolução gradual da silhueta via `AlphaTintVertexConsumer`. |
| **HG-F07** | `T048` (2,40s) | Espada inicia recuperação lenta; jogador começa retorno à guarda. | Silhueta atinge $alpha = 0.0$ e deixa de ser renderizada; apenas cinzas no ar. | Transição contínua sem que o cadáver original reapareça. |
| **HG-F08** | `T068` (3,40s) | Jogador recuperado em guarda neutra; entrega de controles e FOV. | Cinzas dispersas; limpeza completa de referências cliente no mapa `BURNS`. | HUD restaurada, câmera volta à perspectiva original do jogador. |

---

## 3. Matriz de Preservação e Isolamento

| Componente / Frente | Estado | Garantia Técnica |
|---|---|---|
| **Hyōrinmaru** | Aprovada / Intocada | Nenhum arquivo de modelo, rig, animação ou teste foi alterado. |
| **Ryūjin Bankai (12s)** | Aprovada / Intocada | Base solar, fumaça helicoidal, selamento e absorção de calor 100% preservados. |
| **Ryūjin Combate Geral** | Intocado | `RyujinActionVisuals.transform` preservado; ataques básicos, investida e rajada intactos. |
| **Senbonzakura** | Intocada | Nenhum conflito de arquivos com a frente paralela de Senbon. |
| **Benihime & Zaraki** | Intocadas | Animações e lógica preservadas. |
| **Git / Branch** | `codex/ryujin-bankai-sol-selamento` | **NENHUM MERGE REALIZADO**. Sem push, sem alterações na branch `main`. |

---

## 4. Validação e Limites Honestos de Verificação

### 4.1 Compilação e Testes Automatizados
- **Compilação:** `./gradlew compileJava --offline` concluído com sucesso (`BUILD SUCCESSFUL in 40s`).
- **Suíte de Testes:** `./gradlew test --offline` concluído com sucesso (`BUILD SUCCESSFUL in 45s`).
- **Cenários de Regressão:** Todos os 149 testes de `MvpRegressionTest` aprovados, incluindo a nova asserção que valida a invariância da empunhadura do item da katana ao longo de todos os ticks de execução ($0 \le t \le 68$).

### 4.2 Limites Honestos da Validação Automatizada
- Testes unitários e asserções matemáticas provam consistência vetorial, interpolação temporal, supressão de eventos e estabilidade de estado no heap.
- Contudo, **testes de código offline não substituem a percepção humana da renderização final no cliente Minecraft com display real**. A validação definitiva da sensação de impacto, transição da silhueta e ausência de glitches em modelos específicos depende do teste visual interativo no Forge.

---

## 5. Histórico de Commits desta Entrega

1. `5283777`: `fix(higashi): suprimir entidade original via EntityRenderDispatcherMixin e preservar variante alada`
2. `a48a796`: `feat(higashi): alinhar katana aos storyboards HG-F00..F04 com empunhadura fixa e corte ascendente`
3. Commit atual: `docs(higashi): registrar supressão de vítima única, storyboards HG-F00..F08 e validação`
