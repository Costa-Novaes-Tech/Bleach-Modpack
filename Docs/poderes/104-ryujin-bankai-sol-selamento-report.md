# 104 — Relatório de Implementação: Entrada Cinematográfica da Bankai de Ryūjin Jakka / Zanka no Tachi

**Data:** 25/10/2026  
**Checkout:** `Bleach-Modpack` (checkout principal)  
**Branch:** `codex/ryujin-bankai-sol-selamento` (criada a partir de `Feature-Poderes-bankais`, commit base `c4db442`)  
**Documento de Especificação:** [103-ryujin-bankai-sol-selamento-plano-prompt-2026-10-25.md](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/Docs/poderes/103-ryujin-bankai-sol-selamento-plano-prompt-2026-10-25.md)  
**Prancha de Referência Visual:** [103-ryujin-referencia-frames-2026-10-25.jpg](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/Docs/poderes/103-ryujin-referencia-frames-2026-10-25.jpg)  

---

## 1. Escopo e Diretrizes

A implementação executou integralmente o plano arquitetural e visual estabelecido no documento 103:
- **Cenografia e Iluminação:** Construção do monumental "Sol de Chamas" cênico posicionado atrás do jogador/Yamamoto (~9 blocos de raio / 18 de diâmetro), com absorção curva do fogo pela lâmina da katana (ticks 30..52) e lâmina carbonizada escura com fios de brasas e tênue fumaça ascendente (ticks 52..124).
- **Coreografia e Áudio:** Sincronização dos marcos vocais autênticos extraídos dos stems WAV locais: cue 1 ("Bankai") no tick 12 e cue 2 ("Zanka no Tachi") no tick 60, com ducking do estrondo das chamas durante as falas e chiado de extinção de fogo no tick 52.
- **Desacoplamento de Gameplay:** Preservação estrita do contrato 60/40 do mod (`ReleaseAnimationService.busy` ativo de 0 a 60 ticks; commit da forma no tick 40 debitando Reiatsu uma única vez). A apresentação estendida de 140 ticks é governada por uma camada independente de exibição e câmera (`RyujinBankaiEntranceService` / `RyujinBankaiEntranceClient`).
- **Isolamento de Branches:** Trabalho isolado no checkout principal existente na branch `codex/ryujin-bankai-sol-selamento`. O checkout `Bleach-Modpack-design` e a branch `Senbonzakura` permaneceram intactos; nenhum commit, push ou merge foi executado. Os kits de Benihime, Hyōrinmaru, Senbonzakura e Zaraki (congelado) permanecem inalterados.

---

## 2. Artefatos de Áudio e Registro

1. **Processamento dos WAVs Locais:**
   - Utilizado o binário local FFmpeg 7.1 para decodificação e transcodificação estrita dos arquivos em `C:/Users/joaop/Downloads/`:
     - `Zanka_no_Tachi_Bankai_fixed.wav` $\rightarrow$ `src/main/resources/assets/bleachmod/sounds/ryujin_voice_bankai.ogg` (48 kHz mono Vorbis, ~1,35 s, dinâmica de amplitude preservada).
     - `Zanka_no_Tachi_name_fixed.wav` $\rightarrow$ `src/main/resources/assets/bleachmod/sounds/ryujin_voice_name.ogg` (48 kHz mono Vorbis, ~3,05 s, dinâmica de amplitude preservada).
2. **Registro em Código e Metadados:**
   - `ModSounds.java`: Registrados os `RegistryObject<SoundEvent>` `RYUJIN_VOICE_BANKAI` (`bleachmod:voice.ryujin_bankai`) e `RYUJIN_VOICE_NAME` (`bleachmod:voice.ryujin_name`).
   - `sounds.json`: Registrados ambos os eventos com categoria `voice`, atenuação linear de 64 blocos para observadores remotos e `preload: true`.
   - `en_us.json` e `pt_br.json`: Registradas as legendas `subtitles.bleachmod.voice.ryujin_bankai` ("Bankai.") e `subtitles.bleachmod.voice.ryujin_name` ("Zanka no Tachi.").

---

## 3. Implementação Técnica

### 3.1. Relógio e Curvas (`RyujinBankaiTimeline.java`)
- Duração nominal de 140 ticks (7,0 segundos), estruturada em 6 fases:
  1. `GROWTH` (ticks 0..12): Ignição rápida e expansão do sol de fogo cênico.
  2. `SUSTAINED_SUN` (ticks 12..30): Sol atinge escala monumental (~9 blocos de raio) com turbulência na coroa e fala "Bankai" (tick 12).
  3. `ABSORPTION` (ticks 30..52): Chamas convergem em curvas da coroa do sol diretamente para o fio da lâmina; o sol colapsa seu raio de 9 para 0.
  4. `CARBONIZATION_HOLD` (ticks 52..60): Sol se extingue por completo com som de extinção/chiado no tick 52; lâmina adquire textura negra carbonizada com micro-emissão de fumaça.
  5. `NAME_PRONUNCIATION` (ticks 60..124): Liberação do gameplay no tick 60, fala "Zanka no Tachi", plano médio mantido com respiração e postura solene de Yamamoto.
  6. `RETURN` (ticks 124..140): Transição suave da câmera e da pose para a posição normal de combate.

### 3.2. Câmera Dirigida (`RyujinBankaiCamera.java`)
- **Plano Amplo (Ticks 0..52):** Posicionamento frontal 3/4 (distância dos olhos de ~13,2 blocos, elevação +3,2 blocos, FOV 70°), enquadrando a silhueta escura do jogador contra o disco monumental do sol de chamas ao fundo.
- **Dolly de Transição (Ticks 52..60):** Aproximação suave da câmera em direção ao jogador ao término da absorção do fogo.
- **Plano Médio 3/4 (Ticks 60..124):** Câmera a ~4,8 blocos com elevação de 1,35 blocos e FOV 52°, enquadrando peito, semblante e a katana carbonizada apontada para baixo/fora no flanco de empunhadura, com deriva de respiração senoidal de amplitude $\pm 0,08$ blocos.
- **Retorno (Ticks 124..140):** Interpolação Hermite da posição e FOV de volta à visão de gameplay do jogador.

### 3.3. Volume do Sol, Fitas de Absorção e Lâmina Carbonizada (`RyujinSunVisual.java`)
- Renderização em lote no estágio `RenderLevelStageEvent.Stage.AFTER_PARTICLES` usando render type `POSITION_COLOR` translúcido sem cull.
- **Volume do Sol:** 4 camadas esféricas concêntricas com deformação de profundidade 3D (núcleo branco-amarelo, corpo dourado/laranja e línguas turbulentas carmesim/vermelhas com pulsação senoidal).
- **Fitas de Absorção:** 7 fitas tridimensionais helicoidais partindo do perímetro do sol em direção a diferentes segmentos da lâmina da katana (do cabo à ponta), ativas exclusivamente durante os ticks 30..52.
- **Lâmina Carbonizada:** Sobreposição escura cinza-chumbo na lâmina com fio fino alaranjado de brasa incandescente e partículas orçadas de fumaça ascendente (`SMOKE`) durante os ticks 52..124.

### 3.4. Áudio Dinâmico e Ducking (`RyujinBankaiAudio.java`)
- Disparo do cue 1 no tick 12 e cue 2 no tick 60 via `SimpleSoundInstance`.
- Ambiência de chamas com redução de ganho (ducking para 35% de volume) durante as falas vocais, restaurando ganho após o término de cada fala.
- Disparo do efeito sonoro `FIRE_EXTINGUISH` no tick 52 ao término da absorção.
- Suporte a áudio sem atenuação (2D) para o jogador local e posicional 3D atenuado linearmente para observadores até 64 blocos.

### 3.5. Postura Yamamoto e PlayerAnimator (`RyujinChoreography.java`)
- Método autoral `bankaiEntrance(float age, float sign)`:
  - Base firme, peito ereto e katana empunhada baixa apontando para fora do corpo no lado correspondente à mão dominante (invertida corretamente para canhotos via `sign`).
  - Em conformidade com o Ciclo 57/58, a espada não cruza o tronco nem aponta para dentro do corpo.
  - Camada de corpo registrada na prioridade 4500 (`Reference.id("ryujin_bankai_entrance")`), cedendo gradualmente à postura neutra entre os ticks 120 e 140.
  - Em `ZanpakutoReleaseAnimation.java`, a camada legada de 60 ticks foi configurada para retornar `value` inalterado quando `v.bankai && v.identity.equals("ryujin_jakka")`, evitando conflito de poses com o `RyujinBankaiEntranceClient`.

### 3.6. Serviços Autoritativo e de Sincronização
- `RyujinBankaiEntranceService.java`: Rastreia o início, âncora tridimensional, orientação e dimensão do jogador. Cancelamento automático caso o jogador receba dano, sofra logout, morra, mude de dimensão ou se afaste mais de 512 blocos.
- `ReleaseAnimationService.java`: Ao atingir o tick 60 (`age >= r.duration`), preserva a entrada cênica ativa de Ryūjin Bankai (análogo a Senbonzakura Bankai), encerrando apenas o estado `busy` de jogabilidade.
- `SyncHelper.java` e `ClientPacketHandler.java`: Snapshot NBT `"ryujinBankaiEntrance"` propagado confiavelmente em `appearance` e `appearanceTo`.
- `CinematicQaCommands.java`: Registrado o comando `/cinematicqa ryujin_bankai` para disparo determinístico em mundo de teste.

---

## 4. Validação e Testes Automatizados

### 4.1. Suíte de GameTests (`RyujinBankaiGameTests.java`)
Cinco cenários formais foram implementados e executados no servidor de teste real do Forge:
1. `presentationOutlivesReleaseWithoutChangingCommitOrChargingTwice`:
   - Verifica que a liberação de jogabilidade tem duração 60 e a apresentação tem duração 140.
   - No tick 39: forma permanece "sealed" e Reiatsu em 100.
   - No tick 40: commit da Bankai com débito exato de 12 de Reiatsu (saldo 88).
   - No tick 65: `ReleaseAnimationService.busy` é falso (jogador liberado para jogar), enquanto `RyujinBankaiEntranceService` permanece ativo (140 ticks) sem cobrar Reiatsu adicional.
2. `walkingAfterCommitPreservesBankaiFormAndPresentation`:
   - O jogador se desloca 5 blocos no tick 70.
   - A forma permanece Bankai, o sol mantém sua âncora fixa no mundo e a apresentação continua ativa até os 140 ticks.
3. `preCommitCancellationCleansPresentationAndPreservesSealedState`:
   - Testa 5 vetores de interrupção antes do commit (cancelamento direto, desarmamento, distância > 512, mudança de dimensão e morte).
   - Todos limpam imediatamente a apresentação, preservando a forma "sealed" e a Reiatsu intacta (100).
4. `postCommitDamageCancelsPresentationWithoutRevertingBankai`:
   - Aplicação de dano no tick 70 (após commit).
   - A apresentação cinematográfica é limpa imediatamente, mas a forma Bankai conquistada é preservada.
5. `timelineAndCameraCurvesComplyWithContract`:
   - Valida numericamente a progressão das 6 fases, a expansão do sol (> 8 blocos), o colapso para 0 no tick 52, a curva de carbonização (1.0 no tick 54), as distâncias e FOV da câmera (amplo > 12 blocos / FOV 70°; médio < 6 blocos / FOV 52°) e os pesos de pose do PlayerAnimator (0 inicial, > 0.9 na sustentação, 0 ao término).

### 4.2. Evidências de Execução
- **`compileJava --offline`:** Concluído com código 0 (tempo: 32s).
- **`test --offline`:** Concluído com código 0 (tempo: 25s).
- **`runGameTestServer --offline`:** Concluído com código 0 (tempo: 1m 46s).
  - Resultado: **`228 GAME TESTS COMPLETE: All 228 required tests passed :)`**.
  - Todos os 5 lotes de Ryūjin Bankai (`ryujin_bankai_cleanup`, `ryujin_bankai_damage`, `ryujin_bankai_walk`, `ryujin_bankai_commit`, `ryujin_bankai_curves`) aprovados com sucesso.
- **`check build --offline`:** Concluído com código 0 (tempo: 24s).
  - Resultado: **`PASS: 147 regression scenarios`**, JAR gerado em `build/libs/bleachmod-0.2.0.jar`.

---

## 5. Limitações e Orientações para Playtest Humano

- **Audibilidade dos Arquivos de Áudio:** Os arquivos de voz OGG foram gerados a partir dos stems autênticos e sincronizados conforme as especificações; a verificação da audibilidade e do balanço sonoro subjetivo cabe ao usuário durante o teste no cliente Minecraft.
- **Aparência e FPS:** Os testes automatizados comprovam rigorosamente as asserções de servidor, contratos de estado, transições de fases e geometria de câmera. A aprovação da sensação visual contínua, do volume do sol em diferentes biomas e da performance em hardware local fica aberta para validação no cliente pelo usuário com o comando `/cinematicqa ryujin_bankai`.
