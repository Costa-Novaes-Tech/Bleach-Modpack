# 146 — Relatório de Auditoria e QA: Senbonzakura Golpe 1 (S1 — Diagonal Descendente)

- **Data**: 27/10/2026
- **Branch**: `codex/design-poderes-criaturas`
- **Checkout**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- **Commit Base**: `e0a79a6` ("design(senbon): correcao da orientacao real do S1 (diagonal) baseada nos storyboards 137")
- **Destinatário da Auditoria**: Codex (encaminhamento pelo usuário)
- **Pacote de Evidências**: `Docs/poderes/qa/cycle145-senbon/run-cycle145/`

---

## 1. Resumo Executivo e Escopo Delimitado

Em resposta às diretrizes do Prompt 145 e às considerações da auditoria do Codex sobre o ciclo 142/commit `e0a79a6`:
1. **Foco Estrito no Golpe 1 (S1)**: S2 (horizontal), S3 (estocada) e VFX (partículas de pétalas, lâmina fantasmagórica) permaneceram estritamente congelados (`-PsenbonVfxDisable=true`), assim como as demais Zanpakutō (Ryūjin, Benihime, Hyōrinmaru, Zaraki).
2. **Sem Mesclagens ou Modificações Proibidas**: Nenhuma operação de `merge`, `rebase`, `push` ou alteração de branches alheias foi realizada.
3. **Comprovação com Telemetria e Captura Real**: O ciclo executou o cliente Forge 1.20.1 real (`run-benihime-qa/base`), simulando entrada autêntica de ataque melee único (`age >= 30 && age < 32`), coletando capturas em alta resolução (1280x720) e telemetria de ossos do PlayerAnimator e Better Combat frame a frame.
4. **Reserva Factual Declarada**:
   - As telemetrias registradas em `timeline.jsonl` decorrem da instrumentação em runtime da camada do PlayerAnimator (`rightArm`, `rightItem`, `body`, `head`) e do estado de ataque do Better Combat (`upswingTicks`, `swingProgress`). Não são rotuladas como matrizes pós-shader renderizadas.
   - Os GIFs foram gerados a 10 FPS (100 ms/frame) com base na amostragem nominal de 20 TPS. A fluidez final em 60 FPS com interpolação sub-tick permanece sujeita a homologação manual em gameplay pelo usuário.

---

## 2. Cenários de Teste Executados

Foram executados e registrados 6 cenários independentes:

| Cenário | Descrição | Ângulo de Câmera | Mão Dominante | Forma da Arma | Frames Capturados |
|---|---|---|---|---|---|
| `01_shikai_right_front` | S1 Frontal | Frontal (`z = +4.2`) | Destro (RIGHT) | Shikai (Senbonzakura) | 20 frames (`t025`–`t075`) |
| `02_shikai_right_profile` | S1 Perfil Direito | Perfil Oeste (`x = -4.2`) | Destro (RIGHT) | Shikai (Senbonzakura) | 31 frames (`t025`–`t075`) |
| `03_shikai_right_back` | S1 Costas | Costas (`z = -4.2`) | Destro (RIGHT) | Shikai (Senbonzakura) | 20 frames (`t025`–`t075`) |
| `04_sealed_right_profile` | S1 Perfil Selada | Perfil Oeste (`x = -4.2`) | Destro (RIGHT) | Sealed (Katana base) | 20 frames (`t025`–`t075`) |
| `05_bankai_right_profile` | S1 Perfil Bankai | Perfil Oeste (`x = -4.2`) | Destro (RIGHT) | Bankai (Kageyoshi) | 20 frames (`t025`–`t075`) |
| `06_shikai_left_profile` | S1 Perfil Canhoto | Perfil Leste (`x = +4.2`) | Canhoto (LEFT) | Shikai (Senbonzakura) | 20 frames (`t025`–`t075`) |

---

## 3. Confronto Objetivo com os Marcos do Storyboard 137

Comparação dos 8 marcos teóricos do Storyboard 137 (`poses.json`) contra os frames reais capturados no Forge:

### Tabela de Comparação dos Marcos S1

| Marco 137 | Tick SB | Tick Jogo | Fase / Intenção Autoral | Estado Observado no Forge | Rotação rightArm (Euler ZXY) | Rotação rightItem (Euler ZXY) | Status de Auditoria |
|---|---|---|---|---|---|---|---|
| **S1-F00** | t0 | t29 | Guarda Neutra | Empunhadura em repouso, lâmina segura à frente | `0.0, 0.0, 0.0` | `0.0, 0.0, 0.0` | **Alinhado** |
| **S1-F01** | t4 | t32 | Recolha ao Ombro | Início de upswing (`upswingTicks=2`), mão sobe ao ombro direito | `-1.454, 0.281, -0.272` | `-2.589, 0.947, -2.287` | **Alinhado** |
| **S1-F02** | t8 | t34 | Ponta Alta (Preparação) | **Ponta elevada afastada do rosto**, mão alta à direita, gume voltado à frente | `-1.650, 0.320, -0.300` | `-2.856, 0.103, -2.563` | **Corrigido (Ciclo 142)** |
| **S1-F03** | t11 | t36 | Início da Diagonal | Início do corte para frente e baixo (`swingProgress=0.40`) | `-1.305, 0.001, 0.045` | `-2.075, 0.644, -2.035` | **Alinhado** |
| **S1-F04** | t14 | t38 | Passagem Central (Apex) | Lâmina corta o centro do peito em diagonal descendente, tronco acompanha | `-0.960, -0.267, 0.209` | `-1.825, 0.396, -2.797` | **Alinhado** |
| **S1-F05** | t17 | t41 | Descida Esquerda | Follow-through descendente para esquerda baixa (`swingProgress=0.80`) | `-0.702, -0.448, 0.280` | `-1.489, 0.512, -3.040` | **Alinhado** |
| **S1-F06** | t20 | t44 | Final Baixo Esquerdo | Término do arco do golpe (`swingProgress=1.0`), ponta baixa à esquerda | `-0.578, -0.482, 0.289` | `-1.239, 0.467, -2.752` | **Alinhado** |
| **S1-F07** | t30 | t49 | Recuperação / Guarda | Desaceleração suave e retorno gradual à postura de repouso sem salto visual | `-0.021, -0.017, 0.010` | `-0.045, 0.017, -0.100` | **Alinhado** |

### Observações Físicas e Geométricas no Render Real:
1. **Contato do Cabo com a Palma**: A mão permanece unida ao cabo ao longo de toda a trajetória do golpe (verificado visualmente nos composites `milestone_S1-F00.png` a `milestone_S1-F07.png`).
2. **Ausência de Clipping no Corpo**: Em perfil, a ponta e a lâmina passam à frente do tronco e dos braços sem atravessar a cabeça ou o peito do jogador na subida (S1-F02) ou na descida (S1-F04).
3. **Dinâmica do Tronco (Body/Head)**: O tronco acompanha a diagonal de forma orgânica (`body_rot` varia suavemente de yaw `-0.30` na preparação até `+0.58` no final baixo esquerdo), enquanto a cabeça mantém o foco no alvo (`head_rot` compensa para manter a visão frontal).

---

## 4. Análise de Variantes: Formas e Handedness

### 4.1. Formas (Sealed vs Shikai vs Bankai)
- **Arquivo de Evidência**: [`comparison_forms_sealed_shikai_bankai.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/comparison_forms_sealed_shikai_bankai.png)
- **Resultado Observado**: As três formas compartilham o mesmo transform base de empunhadura (`ryujin_katana_handheld.json` e display context de terceira pessoa). A tsuba específica e os elementos de lâmina de cada variante acompanham rigorosamente a mesma orientação do gume na preparação (`t34`), no apex (`t38`) e na conclusão (`t44`). Não há inversão de display ou deslocamento relativo entre formas.

### 4.2. Handedness (Destro vs Canhoto)
- **Arquivo de Evidência**: [`comparison_handedness_destro_canhoto.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/comparison_handedness_destro_canhoto.png)
- **Resultado Observado**: Ao configurar a mão dominante para a esquerda (`HumanoidArm.LEFT`), o Better Combat e o PlayerAnimator realizam a inversão proporcional dos eixos. O corte diagonal que no destro vai do ombro direito para a esquerda baixa, no canhoto inicia no ombro esquerdo e desce para a direita baixa, com a câmera lateral oposta comprovando a simetria da preparação alta e do follow-through.

---

## 5. Índice de Arquivos e Checksums Criptográficos (SHA-256)

Diretório raiz das evidências:
`C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-design\Docs\poderes\qa\cycle145-senbon\run-cycle145\`

### 5.1. Painéis Comparativos e Animações
| Arquivo | Descrição | SHA-256 |
|---|---|---|
| [`manifest.json`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/manifest.json) | Manifesto completo com ambiente, flags e hashes de todos os 211 arquivos | `b499118e9ce7552554db22f254f1e58284687d7e366daae42c94cf3e43eece60` |
| [`timeline.jsonl`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/timeline.jsonl) | 197 registros de telemetria (worldTick, upswing, swingProgress, rotações e posições de todos os ossos) | `347eace564e736e61b302351be65d84d7c406ca7308e9ec904818eb2e4874fbe` |
| [`comparison_forms_sealed_shikai_bankai.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/comparison_forms_sealed_shikai_bankai.png) | Comparativo lado a lado Sealed vs Shikai vs Bankai nos 3 momentos críticos | `c5ac3f677ff8b1567597c2beaecaebb111c9754700e86dd7d8dbff5f943ee848` |
| [`comparison_handedness_destro_canhoto.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/comparison_handedness_destro_canhoto.png) | Comparativo lado a lado Destro vs Canhoto com inversão de eixos | `003bbf2fe04036b92275e0faf13a796a5c1c1a392b71e71659e527d1ef465d7c` |
| [`milestone_S1-F00.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/milestone_S1-F00.png) | Confronto Marco S1-F00 (Guarda) | `1e06c20c58ec0543cee6c68e1f7447df94d6a602b2c24bdbb7696ce9d8f3dd9b` |
| [`milestone_S1-F01.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/milestone_S1-F01.png) | Confronto Marco S1-F01 (Recolha ao Ombro) | `69d7f0a6bc112117b96f8388ed88bc61b7e754b91c72d25f0cafb9bc51c9b213` |
| [`milestone_S1-F02.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/milestone_S1-F02.png) | Confronto Marco S1-F02 (Ponta Alta / Preparação) | `a3c8e39cfd1ae62e9f2f34698c46ee684fa4887d4e5858ccd5ac09c9c9e37325` |
| [`milestone_S1-F03.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/milestone_S1-F03.png) | Confronto Marco S1-F03 (Início da Diagonal) | `c61761950acb07f243c5a6d4eadd5ebad5fd4533d0943f22c859579a55ea9735` |
| [`milestone_S1-F04.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/milestone_S1-F04.png) | Confronto Marco S1-F04 (Passagem Central / Apex) | `2b7b76c35b0793786d1cc09f8b32491707eb1b7ce6b684687a96dc9cbf719d7e` |
| [`milestone_S1-F05.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/milestone_S1-F05.png) | Confronto Marco S1-F05 (Descida Esquerda / Follow-through) | `802cba18de7bd137b90155dea4e46b1982faa45d6b356c525fd6097e670bdd7e` |
| [`milestone_S1-F06.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/milestone_S1-F06.png) | Confronto Marco S1-F06 (Final Baixo Esquerdo) | `cddee61d8cfe26f59526ec4460f8be98d1f02829c065c19d8c049acb658d5c04` |
| [`milestone_S1-F07.png`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/milestone_S1-F07.png) | Confronto Marco S1-F07 (Recuperação / Guarda) | `7a651de9f8841891da290a480ba3e984aa11d206ca310df7ffd026a88e2f7003` |
| [`s1_shikai_profile_20tps.gif`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/s1_shikai_profile_20tps.gif) | Animação sequencial em perfil (31 frames, 10 FPS amostrado de 20 TPS) | `5c9a89c8cc6d465a8633e0adaab5b006f9c267dc79e670e051658da74122b379` |
| [`s1_shikai_front_20tps.gif`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/s1_shikai_front_20tps.gif) | Animação sequencial frontal (20 frames, 10 FPS amostrado de 20 TPS) | `210b5ee28d4876fa3811326fc15355993afc4408656ddfbcf84dfb7ae4db3985` |
| [`s1_shikai_back_20tps.gif`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/cycle145-senbon/run-cycle145/s1_shikai_back_20tps.gif) | Animação sequencial de costas (20 frames, 10 FPS amostrado de 20 TPS) | `a0c5b9995156f469441c86fab376fd6c93c87b13ebaad0abd17c72308ebbefa3` |

---

## 6. Limitações Conhecidas e Próximos Passos

1. **Amostragem Discreta vs Tempo Real Contínuo**: As sequências em GIF representam a amostragem discreta da simulação do servidor local/cliente (20 TPS). Transições finas de 60 FPS com interpolação de sub-ticks (partial tick) devem ser apreciadas e homologadas em jogabilidade real.
2. **Golpes S2 e S3 Congelados**: O Golpe 2 (horizontal) e Golpe 3 (estocada) continuam aguardando a aprovação formal da anatomia e orientação do Golpe 1 antes de qualquer intervenção nas suas respectivas curvas de keyframe.
3. **Efeitos Melee (VFX)**: Os rastros de pétalas e lâmina de energia serão reativados e ajustados estritamente na fase designada para VFX, sem misturar alterações visuais da lâmina física com partículas.

Este pacote é submetido para auditoria direta do Codex e validação do usuário.
