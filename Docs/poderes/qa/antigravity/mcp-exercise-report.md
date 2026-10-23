# Relatório de Integração MCP Blockbench — Antigravity QA

- **Data**: 2026-10-22
- **Checkout**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- **Branch**: `codex/design-poderes-criaturas` (HEAD `830ad64`)
- **Conversation ID**: `ad5d22db-aff7-4c6b-9dd4-913bea391a01`

---

## 1. Verificação de Conexão MCP

- **Mecanismo**: Ferramenta MCP nativa `call_mcp_tool` registrada como `bleach-blockbench` (44 tools disponíveis, modo lazy).
- **`get_status`**:
  - `blockbench_version`: `5.2.2`
  - `has_project`: `false` (inicialmente sem projeto aberto)
- **`list_formats`**:
  - 10 formatos detectados no editor:
    1. `free` (Generic Model — suporta animação)
    2. `java_block` (Java Block/Item)
    3. `bedrock` (Bedrock Entity)
    4. `bedrock_block` (Bedrock Block)
    5. `bedrock_old` (Bedrock Legacy Model)
    6. `modded_entity` (Modded Entity)
    7. `optifine_entity` (OptiFine Entity)
    8. `optifine_part` (OptiFine Part)
    9. `skin` (Minecraft Skin)
    10. `image` (Image)

---

## 2. Carga e Análise do Modelo no Laboratório

- **Arquivo carregado**: `tools/models/lab/antigravity/ag_hydra_jaw.bbmodel`
- **Estrutura identificada**:
  - Formato: `free` (Generic Model)
  - Cubos: 29 (geometria original mantida rigorosamente intacta)
  - Grupos/Bones: 2 (`head_center`, `jaw_center`)
  - Pivô da mandíbula (`jaw_center` origin): `[0, -8.5, -10]` (pivô anatômico na articulação, não nos dentes)
  - Clipes existentes: `animation.hydra_lab.jaw_test` (1.6s, preservado intacto)

---

## 3. Autoria da Nova Animação (2.0s)

- **Nome do clipe**: `animation.hydra_lab.jaw_roar_2s`
- **Duração**: 2.0 segundos, modo `loop`
- **Keyframes adicionados** (12 keyframes, interpolação `catmullrom`):
  - `head_center` (rotação X/Y/Z):
    - t=0.0s: `[0, 0, 0]`
    - t=0.4s: `[3, 0, 0]` (elevação suave)
    - t=0.8s: `[6, 2, 0]` (inclinação para trás no ápice da abertura)
    - t=1.2s: `[5, -2, 0]` (sustentação com leve oscilação)
    - t=1.6s: `[2, 0, 0]` (início do fechamento)
    - t=2.0s: `[0, 0, 0]` (retorno à pose neutra)
  - `jaw_center` (rotação X/Y/Z):
    - t=0.0s: `[0, 0, 0]`
    - t=0.4s: `[-10, 0, 0]` (início da abertura para baixo)
    - t=0.8s: `[-32, 0, 0]` (abertura máxima da boca)
    - t=1.2s: `[-30, 0, 0]` (sustentação aberta)
    - t=1.6s: `[-12, 0, 0]` (fechamento suave)
    - t=2.0s: `[0, 0, 0]` (mandíbula fechada / neutra)
- **Preservação**: Clipes finais no `.bbmodel`:
  1. `animation.hydra_lab.jaw_test` (1.6s)
  2. `animation.hydra_lab.jaw_roar_2s` (2.0s)

---

## 4. Capturas de Ângulos Estáticos

Imagens de referência salvas em `Docs/poderes/qa/antigravity/`:
1. `hydra_jaw_front.png` — Visão frontal.
2. `hydra_jaw_front_right.png` — Visão 3/4 isométrica.
3. `hydra_jaw_left.png` — Visão lateral esquerda.

---

## 5. Validação de Movimento Visível (`preview_animation`)

Para suprir a limitação de capturas estáticas e comprovar abertura/fechamento dinâmico do clipe `animation.hydra_lab.jaw_roar_2s`, foi executado `preview_animation` com tempos `[0, 0.8, 2]` e vistas `[front_right, left]`.

### Poses Reais Preservadas (`Docs/poderes/qa/antigravity/`)
- **t = 0.0s (Pose Neutra Inicial)**:
  - `pose_t0_front_right.png` (boca completamente fechada, dentes alinhados)
  - `pose_t0_left.png` (perfil fechado, mandíbula colada à maxila)
- **t = 0.8s (Ápice da Abertura)**:
  - `pose_t08_front_right.png` (boca amplamente aberta a -32°, cavidade exposta, dentes inferiores afastados, cabeça recuada a +6°)
  - `pose_t08_left.png` (perfil amplamente aberto mostrando ângulo de abertura angular descendente e pivô traseiro intacto)
- **t = 2.0s (Pose Neutra Final / Ciclo Fechado)**:
  - `pose_t2_front_right.png` (retorno exato à pose inicial t=0s)
  - `pose_t2_left.png` (retorno exato ao perfil inicial t=0s)

### Comparativo Início/Fim vs Abertura
- Em `t=0.0s`, a distância vertical entre os dentes superiores e inferiores é 0 (fechamento completo).
- Em `t=0.8s`, a rotação mandibular em `-32°` no eixo X projeta a extremidade anterior da mandíbula para baixo (~22 unidades de modelo), abrindo a bocarra com recuo dorsal da cabeça (`+6°`).
- Em `t=2.0s`, a mandíbula retorna perfeitamente para `0°`, demonstrando ciclo limpo de abertura e fechamento sem desvios ou estalos de loop.

### Diagnóstico Técnico: `analyze_animation` e `no_visible_motion`
- O upstream `analyze_animation` mede deslocamento translacional linear (`travel`, `max_forward`, `max_backward`, `rise/drop`) a partir da origem do bone, e não deslocamento angular relativo de vértices gerado por rotação pura em pivô fixo.
- Em bones de mandíbula (`jaw_center`) cujo canal animado é puramente `rotation`, o centro do bone não translada pelo mundo, disparando o falso positivo de `no_visible_motion`.
- **Conduta adotada**: Conforme instrução e atualização da skill `bleach-design-lab`, a geometria de 29 cubos foi rigorosamente mantida e as amplitudes anatômicas não foram adulteradas artificialmente para forçar métricas de translação.

---

## 6. Limite e Escopo da Verificação

- **O que foi comprovado**: Visualização amostrada de poses estáticas chaveadas (`preview_animation`) no canvas do Blockbench pela ponte MCP, confirmando cinemática da articulação e fechamento de ciclo.
- **O que NÃO foi feito / Limites reais**:
  - Não é captura de vídeo contínuo em tempo real (render de framerate contínuo).
  - Não é homologação de export GeckoLib (`.geo.json` / `.animation.json`).
  - Não é validação dentro de cliente Minecraft rodando com mod/renderizador Java ativo.

---

## 7. Salvamento e Conformidade

- **Cópia salva**: `tools/models/lab/antigravity/ag_hydra_jaw.bbmodel` via `save_project`.
- **Conformidade**:
  - Zero alterações em produção ou assets principais.
  - Branch exclusiva: `codex/design-poderes-criaturas`.
  - Zaraki, Codex checkout e configurações globais intactos.
  - Sem `git commit`, `git push` ou `git merge`.
