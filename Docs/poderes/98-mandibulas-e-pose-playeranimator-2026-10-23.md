# 98 — Correção das Mandíbulas da Hidra e Pose de Transformação com PlayerAnimator

- **Data**: 23/10/2026
- **Workspace**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- **Branch**: `codex/design-poderes-criaturas`
- **Commit Base**: `132c42d` (com alterações locais autorizadas)
- **Status de Verificação**:
  - `python tools/models/ice_hydra.py --check`: **MATCH (Exit 0)**
  - `Invoke-Design.ps1 -Mode Check`: **BUILD SUCCESSFUL (Exit 0)**
  - `Invoke-Design.ps1 -Mode GameTests`: **179/179 PASSADOS (Exit 0)**
  - `Invoke-Design.ps1 -Mode ClientQa`: **10 Cenários Aprovados em Jogo Real (Exit 0)**

---

## 1. Contexto e Objetivos da Rodada

Dando continuidade à integração da cabeça `v3` da hidra de gelo, esta rodada resolveu duas deficiências críticas identificadas na gravação de teste em jogo real:
1. **Mandíbulas da Hidra**: A mandíbula apresentava deformação/inversão de rotação e permanência estática ou mastigatória durante a cinemática de transformação e no ataque triplo (Slot 2).
2. **Pose de Transformação do Jogador**: O jogador permanecia na pose padrão estática de caminhada sem empunhadura adequada. Implementou-se a pose canônica bimanual no cabo da katana à frente do corpo com lâmina voltada para baixo, apoiado no solo, com curva dinâmica de preparação, tremor de Reiatsu e recuperação suave para combate via PlayerAnimator.

Em estrita conformidade com os limites de trabalho:
- O checkout principal do Codex (`Bleach-Modpack` / `Feature-Poderes-bankais`) e arquivos sob sua responsabilidade (`HyorinmaruCinematicClient.java`, `HyorinmaruEntranceAudio.java`, `HyorinmaruCameraPath.java`, `HyorinmaruTimeline.java`, `sounds.json`, áudios e importadores) **permaneceram 100% intocados**.
- A duração total (400 ticks), ponto de commit (240 ticks) e cues fundamentais (24, 216, 330) foram preservados sem alterações de balanceamento, dano ou weather.
- Nenhuma operação git de commit, push, merge, rebase, reset ou stash foi efetuada.

---

## 2. Correção das Mandíbulas da Hidra

### 2.1 Diagnóstico Técnico do Sistema de Rotação
Ao analisar a gravação de tela e inspecionar a cadeia de renderização GeckoLib 4.0:
- O GeckoLib executa `RenderUtil.rotateMatrixAroundBone(poseStack, bone)` rotacionando a matriz em torno do eixo `Axis.XP`.
- No sistema de coordenadas de modelos do Minecraft (onde $-Z$ é para a frente e $+Y$ é para cima), uma rotação **positiva** em torno de $X$ empurra a geometria frontal **para cima** (em direção ao interior do crânio), enquanto uma rotação **negativa** em $X$ abaixa a mandíbula para abrir a boca.
- Quando o GeckoLib lê clipes JSON em `BakedAnimationsAdapter`, ele aplica internamente uma negação de sinal (`dneg`). No entanto, o controle procedural em `IceDragonRenderer.setCustomAnimations` delegava o cálculo para `HydraMotion.jaw()`, que retornava valores positivos em graus (`+8.0` a `+36.0`). Ao converter esses graus em radianos e atribuir a `jaw.setRotX()`, a mandíbula era forçada contra o crânio e os dentes atravessavam o palato.

### 2.2 Reconciliação Procedural e Curva de Atuação
A governança da mandíbula foi unificada em `HydraMotion.java` através de convenção de sinal explícita (`(float)Math.toRadians(-angleDeg)`):

1. **Repouso Hermético (0°)**: Mandíbula perfeitamente fechada nos primeiros ticks, com os escudos condilares vedando a articulação.
2. **Tensão de Preparação (+0.5°)**: Tensão sutil antes do rugido entre os ticks 120 e 150.
3. **Abertura Progressiva (150 a 195 ticks)**: Transição cosenoidal suave elevando a abertura até 34° na cabeça central e 31.5° nas laterais.
4. **Sustentação Estável com Tremor de Reiatsu (195 a 290 ticks)**: Boca aberta estavelmente para o sopro gélido, com micro-oscilação de esforço (`0.75° * sin(...)`) sem mastigação artificial.
5. **Fechamento Gradual (290 a 340 ticks)**: Fechamento desacelerado suave.
6. **Pós-Sopro (340 a 400 ticks)**: Retorno ao repouso hermético completo.

### 2.3 Identidade Temporal Escalonada e Ataque Slot 2
- **Escalonamento**: As 3 cabeças possuem offsets temporais orgânicos (`head 0 = 0`, `head 1 = 2.5`, `head 2 = 4.0 ticks`), evitando movimento mecânico simultâneo em bloco.
- **Ataque Slot 2 (Bote e Mordida)**: Durante o avanço da hidra, a mandíbula abre em bote predatório atingindo até 35°, fechando com força instantânea de mordida no tick de impacto (`impactTick`), antes da dispersão das partículas de gelo.
- **Sincronia Mestre**: Os scripts `tools/models/hydra_organic.py` e `tools/models/ice_hydra.py` foram atualizados e sincronizados com os arquivos de runtime `ice_hydra.geo.json`, `ice_hydra.animation.json` e `ice_hydra.bbmodel`.

---

## 3. Implementação da Pose de Transformação com PlayerAnimator

### 3.1 Fundamentação Anatômica e Cinemática Direta
Para criar a postura solene da invocação da Bankai Daiguren Hyorinmaru, foi implementada a classe dedicada [HyorinmaruPlayerPose.java](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/common/visual/HyorinmaruPlayerPose.java), conectada ao pipeline de animação em [ZanpakutoReleaseAnimation.java](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/model/ZanpakutoReleaseAnimation.java):

```
       [Cabeça (Head)] leve inclinação frontal (-0.05 rad)
             |
       [Tronco (Body)] estabilizado verticalmente
        /          \
   [Braço Dir.]   [Braço Esq.]  (Ombros convergindo para frente)
        \          /
       [Mão Dir.] [Mão Esq.]    (Empunhadura bimanual vertical)
             |
       [Cabo Katana]            (Z = -8.42 pixels à frente do tórax)
             |
       [Tsuba Estrela]          (Y = 7.65)
             |
       [Lâmina Hyorinmaru]      (Direção vertical Y > 0.999999 para o solo)
             |
       [Solo Y = 24.0]          (Pés plantados firmes com espaçamento de base)
```

- **Afastamento Físico Seguro**: O cabo e a lâmina são posicionados em $Z = -8.422$ pixels, garantindo folga superior a 6 pixels do tórax e pernas, impedindo que a lâmina atravesse o modelo do jogador.
- **Geometria de Contato Bimanual**:
  - Mão principal (direita) fixada em $Y = 3.0$ pixels.
  - Mão de apoio (esquerda) posicionada em $Y = 6.0$ pixels.
  - O cabo encordoado da Hyorinmaru estende-se até $Y = 7.0$ pixels e a tsuba até $Y = 7.65$. Com a escala de $0.85$ de `ryujin_katana_handheld`, o ponto de preensão da mão de suporte localiza-se em $Y = 7.029$ pixels, repousando perfeitamente encostada na guarda sem invadir a lâmina ($Y = 7.7$).
- **Apoio no Solo e Compatibilidade**:
  - Pés e pernas ancorados na cota de solo ($Y = 24.0$), com leve abertura para estabilidade.
  - Suporte verificado tanto para o modelo padrão (Steve - braços de 4 pixels) quanto slim (Alex - braços de 3 pixels).
  - Em 1ª pessoa (`RenderHandEvent`), o pitch e translação da mão mantêm a coerência da espada apontada para baixo.

### 3.2 Curva Dinâmica ao Longo dos 400 Ticks
A pose não é estática, respondendo aos marcos visuais e de áudio da cinemática:
- `t = 0..24`: Inclinação de preparação e fechamento da pegada no cabo.
- `t = 24..216`: Ciclo de respiração controlada durante a acumulação de Reiatsu.
- `t = 88..112`: Leve recuo de absorção do impacto ao emergirem as primeiras serpentes de gelo.
- `t = 216..245`: Trava máxima de tensão contra a onda de choque e tremor de baixa frequência.
- `t = 240`: Cue central da Bankai (explosão congelante).
- `t = 330`: Cue da pronúncia do nome "Daiguren Hyorinmaru".
- `t = 340..400`: Transição e amortecimento gradual da pose de volta para a postura normal de combate.
- `t >= 400`: Peso atenuado a exatamente `0.0`, com limpeza total de registradores, garantindo **zero travas residuais** de movimento no jogador.

---

## 4. Testes e Evidências Automatizadas

### 4.1 Suíte de Testes Unitários e Físicos (`HyorinmaruStandingGripGameTests.java`)
Foi criada uma suíte automatizada com 10 testes dedicados validando matematicamente os contratos da pose e das mandíbulas:
1. `standingGripHasTwoHandsOnHilt`: Confirma que ambas as mãos seguram o cabo da katana dentro do intervalo anatômico de preensão.
2. `bladePointsStrictlyDownwards`: Confirma que o vetor diretor da lâmina aponta para baixo com componente vertical $Y > 0.999999$.
3. `feetRemainPlantedAtFloor`: Confirma que os pés permanecem no nível do solo ($Y = 24.0$).
4. `swordHasSafeClearanceInFrontOfBody`: Valida folga livre mínima de 6.0 pixels à frente do tronco e das pernas.
5. `supportHandRestsAgainstTsubaWithoutTouchingBlade`: Confirma que a mão secundária encosta na tsuba sem tocar na lâmina afiada.
6. `slimArmsReachGripWithoutDistortion`: Valida a geometria para o modelo de braço fino de 3 pixels.
7. `zeroResidualWeightAtTick400`: Garante liberação completa sem nenhuma trava residual após 400 ticks.
8. `dynamicCurvesFollowCinematicMilestones`: Confirma as intensidades nos marcos 24, 88, 216, 240 e 330 ticks.
9. `firstPersonPitchPointsBladeDown`: Valida a matriz de transformação da câmera em primeira pessoa.
10. `jawOpenAnglesMatchMotionPhases`: Confirma a física de rotação negativa das mandíbulas em repouso (0°), rugido (34°) e bote (35°).

### 4.2 Execuções e Resultados
- **Validação de Modelo**:
  ```powershell
  python tools/models/ice_hydra.py --check
  # Hydra source, hierarchy, animations, texture and runtime assets match. (Exit 0)
  ```
- **Check Task** (`2026-10-23T11-59-32-904Z-Check`):
  - Duração: 47 segundos.
  - Resultado: `BUILD SUCCESSFUL (Exit 0)`.
- **GameTests** (`2026-10-23T12-00-27-254Z-GameTests`):
  - Duração: 2 minutos e 44 segundos.
  - Resultado: **179 de 179 GameTests passaram (Exit 0)**, incluindo os 10 novos testes de pose.
- **ClientQa em Jogo Real** (`2026-10-23T12-03-13-153Z-ClientQa`):
  - Duração: 3 minutos e 27 segundos.
  - Executou automaticamente todos os 10 cenários Hyorinmaru em cliente Minecraft Forge real com saída limpa (`Exit 0`).

---

## 5. Acervo de Evidências Visuais e Telemetria

Os registros visuais e arquivos de telemetria foram arquivados em [Docs/poderes/qa/antigravity/pose-mandibulas/](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/):

| Arquivo de Evidência | Conteúdo Demonstrado |
| :--- | :--- |
| [trans_frame-t000.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t000.png) | Início da transformação: Jogador em pé, empunhadura bimanual à frente, lâmina para baixo. |
| [trans_frame-t024.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t024.png) | Cue 24: Tensão de preparação da pegada e postura aterrada. |
| [trans_frame-t088.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t088.png) | Erupção das serpentes de gelo com amortecimento corporal. |
| [trans_frame-t160.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t160.png) | Mandíbulas iniciando abertura gradual suave nas 3 cabeças. |
| [trans_frame-t200.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t200.png) | Mandíbulas totalmente abertas com escudos condilares vedados sem vãos. |
| [trans_frame-t240.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t240.png) | Cue 240 (Bankai commit): Tremor de Reiatsu na pose bimanual e sopro gélido em pleno fluxo. |
| [trans_frame-t280.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t280.png) | Sustentação do rugido sem oscilação mastigatória incorreta. |
| [trans_frame-t320.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t320.png) | Mandíbulas fechando suavemente após a passagem da frente fria. |
| [trans_frame-t360.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t360.png) | Recuperação postural suave do jogador em direção à guarda de combate. |
| [trans_frame-t400.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/trans_frame-t400.png) | Final da transformação (t=400): Transição completa, peso nulo, zero travas residuais. |
| [assault_frame-t024.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/assault_frame-t024.png) | Ataque Slot 2: Abertura agressiva da mandíbula durante a trajetória aérea. |
| [assault_frame-t068.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/assault_frame-t068.png) | Mordida no instante do impacto (`impactTick`) antes da explosão glacial. |
| [transformation_frames.jsonl](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/transformation_frames.jsonl) | Registro de telemetria quadro a quadro da cinemática de transformação. |
| [assault_frames.jsonl](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pose-mandibulas/assault_frames.jsonl) | Registro de telemetria quadro a quadro do bote triplo. |

---

## 6. Arquivos Alterados e Criados

- **Arquivos Criados**:
  - `src/main/java/com/bleachmod/common/visual/HyorinmaruPlayerPose.java`: Cinemática direta da pose bimanual, cálculo dinâmico de timeline, suporte Steve/Alex e 1ª/3ª pessoa.
  - `src/main/java/com/bleachmod/gametest/HyorinmaruStandingGripGameTests.java`: 10 GameTests automatizados para validação anatômica, física e de liberação da pose.
  - `Docs/poderes/98-mandibulas-e-pose-playeranimator-2026-10-23.md`: Documentação técnica e comprobatória da entrega.
- **Arquivos Modificados**:
  - `src/main/java/com/bleachmod/common/visual/HydraMotion.java`: Correção de sinal da mandíbula em radianos negativos, curvas de 6 fases, tremor sutil sem mastigação e snap de mordida no bote.
  - `src/main/java/com/bleachmod/client/model/IceDragonRenderer.java`: Aplicação procedural corrigida para `jaw_center`, `jaw_left` e `jaw_right`.
  - `src/main/java/com/bleachmod/client/model/ZanpakutoReleaseAnimation.java`: Integração de `HyorinmaruPlayerPose` nos canais do PlayerAnimator (3ª pessoa e 1ª pessoa).
  - `tools/models/hydra_organic.py` & `tools/models/ice_hydra.py`: Ajuste de consistência nos geradores e exportadores.
  - `src/main/resources/assets/bleachmod/geo/ice_hydra.geo.json`, `src/main/resources/assets/bleachmod/animations/ice_hydra.animation.json`, `src/main/resources/assets/bleachmod/textures/entity/ice_hydra.png` e `tools/models/source/ice_hydra.bbmodel`: Sincronizados com `--check`.
