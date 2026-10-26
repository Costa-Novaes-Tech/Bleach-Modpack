# 109 — Senbonzakura: Combate Próprio Better Combat e Redesenho do Shūkei: Hakuteiken

**Data**: 26/10/2026  
**Frente**: Antigravity Design Lab  
**Branch**: `codex/design-poderes-criaturas`  
**Checkout**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`  
**Status**: Implementado, verificado via Check (146 regressões), GameTests (224 aprovados) e Client QA automatizado com evidências visuais registradas.

---

## 1. Resumo Executivo

Neste ciclo, atendendo ao **Prompt 2 — Senbonzakura: combate próprio e redesenho do Hakuteiken**, a Senbonzakura recebeu sua integração completa ao **Better Combat** com identidade de esgrima nobre e controlada (estilo Byakuya Kuchiki) e a habilidade suprema **Shūkei: Hakuteiken** (Slot 4 / C da Bankai) foi totalmente redesenhada:

1. **Better Combat — Identidade de Esgrima Nobre**:
   - Senbonzakura habilitada no grupo piloto (`BetterCombatCompat.pilot`).
   - Perfil de movimentos dedicado (`weapon_attributes/senbonzakura.json`) com 3 ataques encadeados:
     - **Golpe 1 — Corte Diagonal Curto** (`senbonzakura_diagonal`): Preparação contida, ombro alinhado, corte rápido de cima para baixo cruzando a linha média, postura ereta.
     - **Golpe 2 — Corte Horizontal de Retorno** (`senbonzakura_return`): Varredura reversa precisa na altura do peito, acompanhada de faixa estreita de pétalas de cerejeira (*sakura ribbon*).
     - **Golpe 3 — Estocada Precisa** (`senbonzakura_thrust`): Conclusão do combo com lunge linear direto, lâmina perfeitamente alinhada e recuperação nobre imediata à guarda.
   - **Harmonização de Alcance da Bankai**: O bônus de alcance de Bankai (`+1.5` blocos via `ForgeMod.ENTITY_REACH`) foi integrado dinamicamente em `BetterCombatCompat.reachLimit`, permitindo que o Better Combat valide acertos de 3.05m (Selada/Shikai) até 4.55m (Bankai) sem perda de alcance e sem dupla contagem.
   - **Contratos Preservados**: Consumo único de stamina (4 por requisição de swing), proteção estrita de aliados (`TechniqueTargets.allowed`), rejeição física durante bloqueio/esquiva/recarga, ausência de dano duplicado com ataques vanilla.

2. **Redesenho do Shūkei: Hakuteiken (5 Momentos)**:
   - **Momento 1 — Preparação (Ticks 0–15)**: O jogador assume postura nobre de empunhadura e foco; pétalas convergem em direção à espada e às costas. **Mira Dinâmica autorizada**: O jogador pode ajustar a mira livremente durante toda a preparação; o vetor de ataque e a orientação das asas acompanham a câmera em tempo real.
   - **Momento 2 — Forma Completa (Tick 16)**: Ao atingir o ápice, manifestam-se asas brancas volumétricas com plumagem em camadas reais (5 coberturas superiores densas e 8 rêmiges primárias por asa), enraizadas na base das escápulas, disco de halo luminoso bem definido atrás do dorso e lâmina de energia branca pura. Eliminadas as pontas triangulares degeneradas.
   - **Momento 3 — Lançamento e Avanço (Ticks 16–25)**: Trava estrita de vetor no tick 16 — a investida segue estritamente em linha reta linear de até 16 blocos (1.6 blocos/tick). As asas se recolhem aerodinamicamente para trás em silhueta de flecha/dardo e o corpo mergulha na estocada.
   - **Momento 4 — Impacto**: Pulso branco nítido no ponto de contato e deslocamento radial de pétalas (`RADIAL` pattern 2, 112 partículas expandindo em anel). **Eliminado o clarão cego**: removida a saturação de partículas de `END_ROD` densas e ribbons sobrepostos na câmera que ofuscavam a tela.
   - **Momento 5 — Dissipação**: Ao colidir com parede, atingir o limite ou sofrer cancelamento, as asas se desfazem em pétalas esvoaçantes e a arma retorna suavemente ao estado normal.

---

## 2. Fontes e Recursos Editáveis Criados/Modificados

### 2.1 Better Combat Configurações e Animações
- [`senbonzakura.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/data/bleachmod/weapon_attributes/senbonzakura.json): Definição de atributos Better Combat com categoria `bleach_senbonzakura_fencing`, 3 ataques (hitbox vertical plane 70°, horizontal plane 85°, forward box 0°).
- [`attack_animations/senbonzakura_diagonal.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/attack_animations/senbonzakura_diagonal.json): Animação Bendable Emote Better Combat (corte diagonal).
- [`attack_animations/senbonzakura_return.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/attack_animations/senbonzakura_return.json): Animação Bendable Emote Better Combat (corte horizontal de retorno).
- [`attack_animations/senbonzakura_thrust.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/attack_animations/senbonzakura_thrust.json): Animação Bendable Emote Better Combat (estocada precisa).
- [`player_animation/senbonzakura_diagonal.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/player_animation/senbonzakura_diagonal.json): Curvas Bedrock PlayerAnimator.
- [`player_animation/senbonzakura_return.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/player_animation/senbonzakura_return.json): Curvas Bedrock PlayerAnimator.
- [`player_animation/senbonzakura_thrust.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/player_animation/senbonzakura_thrust.json): Curvas Bedrock PlayerAnimator.

### 2.2 Serviços e Lógica de Combate
- [`BetterCombatCompat.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/common/compat/BetterCombatCompat.java):
  - Adicionado `ModItems.SENBONZAKURA.get()` ao método `pilot(stack)`.
  - Emissão de faixa de pétalas em `comboChanged(player, 1)` sincronizada para todos os observadores na área.
  - Implementado `reachLimit(ServerPlayer p)` integrando `SenbonPolicy.REACH_BONUS` quando o modificador de Bankai está ativo.
- [`SenbonzakuraTechniqueService.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/common/technique/SenbonzakuraTechniqueService.java):
  - Mira dinâmica ativa na preparação (`age < HAKU_WINDUP` atualiza `a.forward = SenbonGeometry.forward(s.owner.getYRot())`).
  - Trava rígida de vetor no lançamento do dash (`tickHakuDash`).
  - Emissão de pulso e dispersão radial de pétalas na parada/impacto (`stopAttack`).
- [`SenbonConstructGeometry.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/common/visual/SenbonConstructGeometry.java):
  - Redesenho completo de `WINGS`: asas escapulares volumétricas com 2 níveis de plumagem estruturada (não triângulos degenerados), halo de 24 segmentos e lâmina branca com silhueta própria.
  - Voo aerodinâmico durante dash (`pattern == 1`).
  - Suavização de brilho na lança (`LANCE`) para eliminar ofuscamento.
- [`SignatureChoreography.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/common/visual/SignatureChoreography.java):
  - Postura de preparação nobre e foco linear durante a investida do Hakuteiken.
- [`SenbonPetalEffects.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/particle/SenbonPetalEffects.java):
  - Tonalidade balanceada de faíscas `END_ROD` sem saturação cega.

### 2.3 Suíte de Testes e Validação
- [`BetterCombatGameTests.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/gametest/BetterCombatGameTests.java):
  - Atualizado para testar e homologar os atributos Better Combat da Senbonzakura (3 ataques com animações `bleachmod:senbonzakura_` e dano base 1.0) enquanto preserva a rejeição de kits não revisados (como Nozarashi).
- [`SenbonzakuraCombatQaSequence.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/java/com/bleachmod/client/cinematic/SenbonzakuraCombatQaSequence.java):
  - Sequência automatizada executando 6 cenários de combate com captura de quadros em Frente, Perfil e Costas.

---

## 3. Resultados de Validação

### 3.1 Regressões Unitárias Offline
- **Comando**: `./gradlew regressionTest --offline`
- **Resultado**: `BUILD SUCCESSFUL in 53s`
- **Cenários**: **146 cenários de regressão aprovados**, incluindo geometrias de Senkei, Hakuteiken, bounds de asas, paletas de partículas, integridade de NBT e persistência de dados.

### 3.2 GameTests em Servidor Headless
- **Comando**: `./gradlew runGameTestServer --offline`
- **Resultado**: `BUILD SUCCESSFUL in 4m 4s`
- **Asserções**: **224 GameTests obrigatórios aprovados** (100% de sucesso), cobrindo:
  - `senbon_dash_sweep` (um acerto por alvo no corredor do dash, sem dano antecipado).
  - `senbon_dash_wall` (parada imediata antes da parede, sem dano através de blocos sólidos).
  - `senbon_dash_cancel` (parada imediata de deslocamento e cancelamento de dano tardio).
  - `hakuteikenChargesThenHitsAndRestoresBlade` (windup respeitado, aparência de lâmina branca, alcance de 16 blocos e retorno).
  - `pilotProfileLoadsWithoutEnablingOtherKitsOrDualWield` (perfil da Senbonzakura verificado no WeaponRegistry, dual wield bloqueado).
  - Todos os testes de escopo Better Combat, stamina, proteção de aliados e rejeição de alvos fora de alcance.

### 3.3 Prova Vertical no Cliente Gráfico Real
- **Comando**: `./gradlew runClient -PbenihimeQa=true -PsenbonCombatQa=true -PqaAutoExit=true --offline`
- **Resultado**: `BUILD SUCCESSFUL in 4m 53s`
- **Evidências Capturadas**: 45 screenshots gerados em `run-benihime-qa/base/senbonzakura-combat-qa/` cobrindo:
  - **Combo Better Combat**:
    - Golpe 1 (Corte diagonal curto): preparação contida e ângulo de corte disciplinado.
    - Golpe 2 (Corte horizontal de retorno): varredura acompanhada da faixa de pétalas de cerejeira.
    - Golpe 3 (Estocada precisa): lunge linear com extensão total de braço e recuperação rápida.
  - **Shūkei: Hakuteiken (5 Momentos)**:
    - Preparação: postura icônica com convergência de pétalas.
    - Forma Completa: asas brancas volumétricas com plumagem em camadas e halo circular.
    - Lançamento: silhueta aerodinâmica encolhida acompanhando a investida linear.
    - Impacto: anel radial de dispersão de pétalas sem clarão cego na visão do jogador.
    - Dissipação: dissolução suave das asas e restauração da lâmina normal.
  - Perspectivas validadas: Visão frontal (`THIRD_PERSON_FRONT`), perfil lateral e dorsal (`THIRD_PERSON_BACK`).

---

## 4. Limitações Materiais e Próximos Passos

1. **Aprovação Artística vs. Build Homologado**: Os testes técnicos, hitboxes e renderizadores de partículas passaram com 100% de conformidade. A aprovação artística da silhueta final e da fluidez do combo depende de playtest e avaliação do usuário.
2. **Ambiente Isolado**: Sem push, merge ou alteração em outras branches ou diretórios. O repositório permanece limpo e pronto para revisão humana.
