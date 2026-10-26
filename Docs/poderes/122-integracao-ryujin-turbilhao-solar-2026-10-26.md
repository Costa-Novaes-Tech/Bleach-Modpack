# 122 — Integração Ryūjin: turbilhão de calor, base solar e validação cliente

26/10/2026. Branch `Feature-Poderes-bankais`. Integração autorizada pelo prompt de revisão e integração do Prompt 2 da Ryūjin.
Precedida pela preservação documental do Codex (`0ae6730`) e pela integração da Etapa 1 do Senbonzakura (`2127c26`).
Main e branches remotas preservadas; sem push remoto nesta etapa.

---

## 1. Commits e Rastreabilidade

- **Origem**: `codex/ryujin-bankai-sol-selamento` (checkout `Bleach-Modpack-benihime`)
  - Commit `b7b3f70`: `feat(ryujin): turbilhao de calor, base solar e absorcao progressiva na Bankai`
- **Destino**: `Feature-Poderes-bankais` (checkout `Bleach-Modpack`)
  - Merge commit `6a2da98`: `merge: integrate Antigravity Ryujin heat vortex and solar base into powers`
  - Sem conflitos textuais ou sobreposição de arquivos não commitados.

---

## 2. Escopo Entregue e Contratos Preservados

- **Apresentação Visual Pura** em [`RyujinSunVisual.java`](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/src/main/java/com/bleachmod/client/model/RyujinSunVisual.java):
  - **0–2s (ticks 0..40 / beats 0..12)**: Base solar em elipse ao redor dos pés (32 cards de chama). Altura atenuada (até 0,38m) no setor frontal (`cos > 0.15`) e opacidade em 72%, mantendo apoio e pés legíveis. Emissão de brasas e faíscas ascendentes sem criar parede de fogo em frente ao rosto.
  - **2–4,5s (ticks 40..90 / beats 12..30)**: Sol pleno sustentado ao fundo (112 cards em distribuição golden-angle com profundidade volumétrica + 20 línguas na coroa). Turbilhão helicoidal largo de calor e fumaça em duas faixas entrelaçadas (28 cards) de 0,35m a 3,95m de altura e raio amplo ($R \approx 2,3\text{m}$ a $2,7\text{m}$), deixando a cavidade central desobstruída para leitura do tronco, rosto e katana. Partículas tangenciais de calor (`SMOKE`, `ASH`, `FLAME`).
  - **4,5–7s (ticks 90..140 / beats 30..52)**: Contraque progressiva do turbilhão e da base dos pés acompanhando a absorção. 4 feixes curvos (56 cards) convergem da periferia do sol diretamente para a lâmina da espada (`blade.hand()`).
  - **7–8s (ticks 140..160 / beats 52..60)**: Chamas zeradas instantaneamente (0 cards de billboard ativos). Apenas tênues fios de fumaça e cinzas (máximo 3 partículas/tick) emanando do contato da mão com a lâmina carbonizada durante a aproximação física da câmera.
  - **8–10,5s (ticks 160..210 / beats 60..112)**: Zero cards ativos. Cinzas flutuando suavemente pelo ar (máximo 2 partículas/tick); rosto, espada e legenda ("Zanka no Tachi") 100% visíveis e desobstruídos.
  - **10,5–12s (ticks 210..240 / beats 112..140)**: Emissores completamente desligados a partir do beat 112 (tick 210). Transição suave de câmera e devolução limpa ao controle sem tornados residuais.

- **Contratos e Orçamentos Estritos**:
  - `MAX_FIRE_CARDS = 288` (pico medido de 248 cards no tick 93, zerando aos 7s).
  - Partículas limitadas a 12/tick em normal, com redução automática pelas opções vanilla (`MINIMAL` $\le 3$, `DECREASED` $\le 6$).
  - Duração de 240 ticks / 12s, áudios com ducking, falas em 2s e 8s, poses do jogador, release 60 / commit 40, cancelamentos pré e pós commit mantidos intactos.
  - Nenhuma alteração em regras de gameplay, dano, weather, blocos ou movimentação do jogador.
  - Benihime, Senbonzakura (inclusive Etapa 1 recém-integrada), Zaraki, Higashi, Minami e dash preservados.
  - `MvpRegressionTest.java` não sofreu alterações.

---

## 3. Validação Real e Evidências

1. **Compilação e Regressões**:
   - Comando: `./gradlew check build --offline`
   - Resultado: **Exit 0** em 1m19s.
   - Contagem: **148 de 148 cenários de regressão aprovados**.

2. **GameTestServer**:
   - Comando: `./gradlew runGameTestServer --offline`
   - Resultado: **Exit 0** em 2m16s.
   - Contagem: **233 de 233 GameTests obrigatórios aprovados** (`All 233 required tests passed :)`).

3. **Simulação Matemática**:
   - Comando: `python tools/qa/ryujin_vortex_evidence.py`
   - Resultado: **Exit 0**. 241 ticks verificados (orçamento e fases). Arquivo de auditoria: [`Docs/poderes/qa/cycle121/ryujin-vortex-budget.json`](qa/cycle121/ryujin-vortex-budget.json).

4. **Cliente Minecraft Forge Real (Laboratório Descartável)**:
   - Comando: `./gradlew runClient -PbenihimeQa=true -PryujinBankaiQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline`
   - Resultado: **Exit 0** em 5m06s. Mundo descartável: `Benihime-QA-68-1793054027038`.
   - 12 cenários completados com sucesso:
     - `01_day_back`: `releaseBusy=false scene={} form=bankai` (retorno em terceira pessoa de costas)
     - `02_night_front`: `releaseBusy=false scene={} form=bankai` (visão frontal noturna, progressão completa capturada)
     - `03_first_return`: `releaseBusy=false scene={} form=bankai` (retorno suave contornando o corpo e transição limpa para `FIRST_PERSON` no tick 240)
     - `04_left_front`: `releaseBusy=false scene={} form=bankai` (canhoto com espelhamento da espada e chamas)
     - `05_near_wall`: `releaseBusy=false scene={} form=bankai` (proximidade de obstáculo)
     - `06_move_after_commit`: `releaseBusy=false scene={} form=bankai` (deslocamento pós-commit mantém Bankai)
     - `07_cancel_before_commit`: `releaseBusy=false scene={} form=shikai` (reversão limpa para Shikai ao cancelar antes do tick 40)
     - `08_cancel_after_commit`: `releaseBusy=false scene={} form=bankai` (cancelamento após commit mantém Bankai)
     - `09_army_and_supreme`, `10_supreme_front`, `11_supreme_profile`, `12_supreme_left_profile`: suíte de combate e execução com corte supremo.
   - Capturas de tela e telemetria salvas em: `run-benihime-qa/base/ryujin-bankai-qa/cycle118-1793054027038/`.

---

## 4. Artefato Final

- **Arquivo JAR Gerado**: `build/libs/bleachmod-0.2.0.jar`
- **SHA-256**: `cfd9673bd0b6e7f81ced38b2590ecbce8a60f90e328981d94865042328942b7a`

---

## 5. Pendências

- **Avaliação Artística e Escuta Subjetiva**: Avaliação em jogo real pelo usuário das novas camadas de calor, turbilhão e transições da Ryūjin.
- **Pausa de Destruição do Corte C**: Preservada intocada no escopo do item 8 de eficiência de terreno do roadmap geral (não alterada nesta entrega visual).
