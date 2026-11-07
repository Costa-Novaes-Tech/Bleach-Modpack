# 123 — Hyōrinmaru: Refinamento Anatômico, Rig e Autoridade de Controlador

**Data:** 26/10/2026  
**Autor:** Antigravity  
**Branch:** `codex/ryujin-bankai-sol-selamento` (checkout `Bleach-Modpack-benihime`)  
**Contexto:** Prompt 125 — Refino Hyōrinmaru e Higashi em fases (Fase 0 e Fase 1)

---

## 1. Inventário e Linha de Base (Fase 0)

### 1.1 Geradores e Fontes de Modelagem
- **Gerador Primário:** `tools/models/ice_hydra.py` e `tools/models/hydra_organic.py`.
- **Fonte Blockbench:** `tools/models/source/ice_hydra.bbmodel`.
- **Assets de Runtime:**
  - `src/main/resources/assets/bleachmod/geo/ice_hydra.geo.json`
  - `src/main/resources/assets/bleachmod/animations/ice_hydra.animation.json`
  - `src/main/resources/assets/bleachmod/textures/entity/ice_hydra.png`
- **Controladores Java:**
  - `com.bleach.client.renderer.entity.IceDragonRenderer`
  - `com.bleach.entity.IceDragonEntity`
  - `com.bleach.util.HydraMotion`
  - `com.bleach.visual.HyorinmaruBreathVisual`

### 1.2 Métricas Estruturais
- **Bones:** 46 bones organizados no rig da hidra (`root`, 3 cabeças completas compostas por `neck_center_0..7`, `neck_left_0..7`, `neck_right_0..7`, `head_center`, `head_left`, `head_right`, `jaw_center`, `jaw_left`, `jaw_right`).
- **Elementos:** 567 cubos anatômicos modelados parametricamente.
- **Pivô Condilar da Mandíbula:** `[0, -8.5, -10]` (articulado na base da mandíbula para rotação natural da boca).
- **Atlas UV:** 128x128 com zoneamento dedicado por anel cervical, crânio, chifres, dentes e boca interior, sem boxUV cego.

### 1.3 Diagnóstico de Disputa de Controladores
Na análise do código em `IceDragonRenderer.java`, observou-se que `setCustomAnimations()` chamava `super.setCustomAnimations(animatable, instanceId, animationState)` (que processava keyframes GeckoLib dos clipes `emerge` e `roar`) e, imediatamente a seguir, sobrescrevia `head.setRotX()`, `head.setRotY()`, `jaw.setRotX()` com base no `HydraMotion`.
Havendo keyframes de cabeça e mandíbula dentro dos arquivos JSON de animação da GeckoLib, havia interpolação redundante e potencial flicker entre os canais.
**Decisão:** Estabelecer o Java procedural (`IceDragonRenderer` / `HydraMotion`) como autoridade única sobre `neck_*`, `head_*` e `jaw_*`. Os clipes da GeckoLib mantêm controle das raízes e deslocamento, enquanto as tracks conflitantes de rotação da cabeça e mandíbula foram limpas dos arquivos de animação JSON.

---

## 2. Refinamento Anatômico (Fase 1)

### 2.1 Fechamento da Boca e Mordida Tesoura (Diastema)
- **Problema:** Quando a mandíbula fechava em repouso (ângulo 0°), os caninos superiores penetravam as paredes laterais da mandíbula inferior, criando artefatos visuais de colisão de malha.
- **Solução Implementada em `tools/models/hydra_organic.py`:**
  - Redimensionamento e reposicionamento do cubo lateral `Jaw 5`:
    - `origin: [-6.8, -16.5, -46]`
    - `size: [13.6, 7.0, 12]`
  - Criação de folga anatômica (diastema) que acolhe os caninos superiores (`Head 38` e `Head 39`, com $X \in [-8.8, -6.4]$ e $[6.4, 8.8]$) exteriormente à borda da mandíbula inferior em oclusão tipo tesoura.
  - Preservação da profundidade escura do interior da boca, palato e língua.

### 2.2 Escamas e Placas Vertebrais Sobrepostas (Scutes)
- **Problema:** Ao curvar o pescoço em ângulos elevados durante a perseguição e baforada, surgiam frestas e descontinuidades entre os elos cervicais (`neck_link_cubes`).
- **Solução Implementada em `tools/models/hydra_organic.py`:**
  - Aumento da altura do colar de escamas dorsal (`collar`) de `3.5` para `6.0` unidades (`origin: [-w_collar / 2, -2.5, -d_collar / 2]`, `size: [w_collar, 6.0, d_collar]`).
  - Esse transpasse de $2.5$ unidades estende as placas scute sobre o elo cervical subsequente, formando uma blindagem sobreposta semelhante à carapaça de serpentes/pangolins, eliminando frestas visíveis durante flexão acentuada do pescoço.

### 2.3 Higienização de Animação e Rig
- No método `build_anim_json()`, as faixas de rotação de `head_` e `jaw_` foram removidas dos clipes `emerge` e `roar`.
- O modelo `.bbmodel` e os arquivos de runtime `.geo.json` e `.animation.json` foram regenerados de forma 100% reproduzível.

---

## 3. Evidências de Validação

1. **Idempotência do Pipeline de Modelagem:**
   ```powershell
   python tools/models/ice_hydra.py --check
   # Saída:
   # Hydra source, hierarchy, animations, texture and runtime assets match.
   ```
2. **Build e Testes de Regressão:**
   ```powershell
   ./gradlew check --offline
   # Saída:
   # BUILD SUCCESSFUL in 1m 15s
   # 148 scenarios passed, 0 failures.
   ```
3. **Limitação Conhecida:**
   - Servidor MCP `bleach-blockbench` em `127.0.0.1:8787` não estava ativo nesta sessão (`Cannot reach Blockbench on http://127.0.0.1:8787`). Toda a validação geométrica e idempotência foi executada diretamente via gerador formal parametrizado e checagem de integridade de malha.

---

## 4. Próximos Passos
- Avançar para a **Etapa 2**: Integração da baforada no Java (`HyorinmaruBreathVisual`), alinhando o ponto de emissão com a rotação real da cabeça/mandíbula e validando a autoridade de movimento no runtime.
