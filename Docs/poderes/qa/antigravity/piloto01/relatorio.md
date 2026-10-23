# Relatório do Piloto de Modelagem e Animação — Antigravity QA

- **Data**: 2026-10-22
- **Checkout exclusivo**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- **Branch**: `codex/design-poderes-criaturas`
- **Commit HEAD**: `830ad643644d7c95e13866b05afb045b4ef31e8b`
- **Ambiente de Teste**: Laboratório isolado via MCP `bleach-blockbench` (porta 127.0.0.1:8787).
- **Preservação de Sessão**: Nenhuma execução de Gradle, cliente Minecraft adicional, servidor ou MCP `bleach-design-runtime` foi realizada; a sessão paralela de testes do usuário no Minecraft foi 100% preservada. Nenhum arquivo em `src/`, gameplay ou checkout principal do Codex (`Bleach-Modpack`) foi alterado.

---

## 1. Ferramentas e Arquivos Envolvidos

### Arquivos de Referência e Base
1. `tools/models/lab/antigravity/ag_hydra_jaw.bbmodel` (modelo baseline anterior preservado intacto com 29 cubos).
2. `Docs/poderes/media/concept/concept_bankai_hydra.jpg` (arte conceitual da Hidra de Gelo de Hyōrinmaru inspecionada visualmente).
3. `Docs/poderes/qa/antigravity/mcp-exercise-report.md` (evidências da rodada preliminar).

### Arquivo Produzido no Piloto
- `tools/models/lab/antigravity/piloto01/ag_hydra_head_v2.bbmodel` (novo projeto `ag_hydra_head_v2`, 62 cubos, 2 bones contratuais, 3 clipes salvos).

### Ferramentas MCP Nativas Utilizadas
- `bleach-blockbench`: `get_status`, `list_formats`, `list_plugins`, `load_project`, `set_project_meta`, `check_model`, `check_rig`, `get_rig`, `measure_model`, `audit_complexity`, `analyze_animation`, `screenshot`, `preview_animation`, `save_project`.

---

## 2. Diagnóstico Inicial do Modelo Anterior (`ag_hydra_jaw`)

A partir da inspeção direta das capturas estáticas (`Docs/poderes/qa/antigravity/hydra_jaw_*.png`) e poses dinâmicas (`Docs/poderes/qa/antigravity/pose_t08_*.png`), foram identificados os seguintes defeitos visuais e anatômicos:

1. **Silhueta e Chifres em Bloco Rígido**:
   - Os 4 chifres superiores eram colunas prismáticas 100% verticais (`rotation: [0, 0, 0]`), sem angulação, inclinação ou afilamento, assemelhando-se a pilares de alvenaria em vez de cristais dracônicos de gelo.
   - O crânio e focinho eram compostos por caixas retangulares retas, conferindo aspecto de focinho de crocodilo/canino genérico.
2. **Ausência de Transição Cabeça/Pescoço**:
   - A extremidade posterior da cabeça (`Z: 12`) terminava em corte reto vertical (`90°`), sem escamas dorsais, placas espinhais ou prolongamento cervical para conexão harmônica com o pescoço da hidra.
3. **Cavidade Bucal Inexistente (Maciço Superior)**:
   - O bloco do focinho superior preenchia todo o volume vertical até `Y: -8`. Ao abrir a boca, não havia palato abobadado nem garganta; visualizava-se apenas uma parede inferior plana azulada com dentes suspensos no ar.
4. **Mandíbula Plana e Desconexão do Pivô Articular**:
   - A mandíbula inferior resumia-se a uma fina laje retangular (`size: [22, 7, 42]`). Ao girar para `-32°`, abria-se um grande vão vazio atrás da articulação, dando a impressão de peça flutuante descolada do crânio.
5. **Dentes como Estacas de Cerca**:
   - 8 dentes superiores e 8 dentes inferiores idênticos (`3x9x3`), orientados verticalmente sem dentes-de-sabre, serrilhado ou conicidade.

---

## 3. Justificativa das Melhorias Implementadas (`ag_hydra_head_v2`)

Mantendo rigorosamente a estética Minecraft (volumes cúbicos limpos, texturização facetada coerente e sem micro-cubos desordenados), foram implementadas as seguintes soluções:

1. **Coroa Dracônica de Chifres de Gelo (*Sweeping Horns*)**:
   - Chifres primários angulados para trás e para fora (`rot: [28, 14, -8]` na base e `[36, 16, -10]` na ponta afunilada), simulando a formação cristalina da Bankai Hyōrinmaru vista no concept.
   - Chifres secundários internos (`rot: [22, 6, -4]`) e espícula dorsal central no topo da calota craniana.
   - Espículas temporais nas laterais do crânio (`cheek_spike_l/r`).
2. **Transição Nape/Pescoço**:
   - Adicionado toco cervical de inserção (`neck_base_stump` de `Z: 10` a `Z: 22`).
   - Adicionadas cristas espinhais dorsais sobrepostas (`neck_dorsal_crest_1/2`, anguladas a `-12°` e `-18°`) e espinhas cervicais laterais, eliminando o corte reto a `Z: 12`.
3. **Cavidade Bucal Abobadada e Garganta Profunda**:
   - Abertura da arcada maxilar superior com palato rebaixado (`palate_vault_roof`), paredes maxilares laterais (`maxilla_wall_l/r`) e câmara escura de garganta (`throat_cavity_interior`).
   - Assoalho da boca rebaixado (`mandible_floor_recess`) com crista/núcleo de língua cristalina (`mandible_tongue_core`).
4. **Blindagem Articular e Mandíbula Menestrel**:
   - Escudos temporais da articulação (`hinge_shield_l/r`) que cobrem o côndilo articular mesmo na abertura máxima de `-34°`, eliminando a descontinuidade visual.
   - Queixo afilado (`mandible_chin_taper`) com esporão/barbela cristalina ventral (`mandible_chin_barb`, angulada para frente e para baixo).
5. **Dentição Predatória Escalonada**:
   - Grandes caninos sabre frontais (`fang_upper_major_l/r` com comprimento de 9.5 unidades) e caninos mandibulares complementares que se encaixam logo atrás sem colisão direta ou cruzamento defeituoso.
   - Dentes intermediários escalonados e incisivos anteriores na ponta do focinho.
6. **Controle de Complexidade**:
   - Total de 62 cubos (45 em `head_center`, 17 em `jaw_center`).
   - Resultado em `audit_complexity`: **verdict: "acceptable"**, 0% de micro-cubos, mantendo fidelidade ao estilo do mod.

---

## 4. Autoria da Nova Animação de Rugido (`jaw_roar_v2`)

- **Nome do clipe**: `animation.hydra_lab.jaw_roar_v2`
- **Duração**: 2.4 segundos, modo `loop`, interpolação `catmullrom`.
- **Justificativa da Duração e Fases**:
  Um rugido dracônico de criatura de grande porte necessita de antecipação e assentamento para transmitir peso:
  - `t = 0.0s`: Pose neutra em repouso (boca fechada, dentes alinhados).
  - `t = 0.4s` (*Antecipação/Inalação*): Cabeça abaixa levemente (`rotX: -3°`), mandíbula contrai (`rotX: +1.5°`).
  - `t = 0.8s` (*Abertura Agressiva*): Mandíbula desce rápido para `-22°`, cabeça sobe para `+6°`.
  - `t = 1.2s` (*Ápice do Rugido*): Abertura máxima a `-34°`, cabeça recuada a `+8°`, boca totalmente exposta.
  - `t = 1.6s` (*Tremor Sustentado*): Mandíbula sustentada a `-32°` com oscilação orgânica da cabeça (`rotY: -2°, rotZ: +1°`).
  - `t = 2.0s` (*Recuperação e Fechamento Rápido*): Mandíbula fecha para `-4°`, cabeça desce através de neutro com leve overshoot (`rotX: -1.5°`).
  - `t = 2.4s` (*Assentamento Neutro*): Retorno a `[0, 0, 0]` em todos os eixos, permitindo loop contínuo e suave sem pulo.

---

## 5. Comparação Visual e Evidências

Todas as capturas foram renderizadas na mesma orientação e salvas em `Docs/poderes/qa/antigravity/piloto01/`.

### 5.1 Capturas Estáticas (Antes vs Depois)

| Vista | Antes (`ag_hydra_jaw`) | Depois (`ag_hydra_head_v2`) | Melhoria Observada |
| :--- | :--- | :--- | :--- |
| **Frontal** | `before_front.png` | `after_front.png` | Coroa de chifres aberta em leque; queixo afilado com barbela; olhos rebaixados sob arcadas; dentição completa. |
| **Perfil Esquerdo** | `before_left.png` | `after_left.png` | Chifres sweeping inclinados para trás; cristas dorsais no pescoço; focinho contínuo sem frestas; blindagem da mandíbula. |
| **3/4 Isométrica** | `before_front_right.png` | `after_front_right.png` | Silhueta dracônica tridimensional; relevo de narinas e caninos; espículas de bochecha; profundidade visual. |

### 5.2 Poses Amostradas da Animação (`preview_animation`)

| Tempo | Vista 3/4 (`front_right`) | Vista Perfil (`left`) | Análise Visual da Pose |
| :--- | :--- | :--- | :--- |
| **t = 0.0s** | `after_pose_t0_front_right.png` | `after_pose_t0_left.png` | Pose neutra: mandíbula selada, dentes superiores encaixados externamente aos inferiores. |
| **t = 0.4s** | `after_pose_t04_front_right.png` | `after_pose_t04_left.png` | Inalação/contração: cabeça abaixada, compressão antes do rugido. |
| **t = 0.8s** | `after_pose_t08_front_right.png` | `after_pose_t08_left.png` | Expansão: mandíbula abre a -22°, dentes se separam, cabeça projeta-se para cima. |
| **t = 1.2s** | `after_pose_t12_front_right.png` | `after_pose_t12_left.png` | **Ápice do Rugido**: mandíbula a -34°, cabeça a +8°, garganta e palato expostos; pivô permanece protegido. |
| **t = 1.6s** | `after_pose_t16_front_right.png` | `after_pose_t16_left.png` | Tremor sustentado: boca aberta ampla com leve torção cervical. |
| **t = 2.0s** | `after_pose_t2_front_right.png` | `after_pose_t2_left.png` | Fechamento: mandíbula quase fechada (-4°), cabeça em transição de retorno. |
| **t = 2.4s** | `after_pose_t24_front_right.png` | `after_pose_t24_left.png` | **Retorno Completo**: coincide perfeitamente com a pose neutra de t=0s. |

---

## 6. Confronto Técnico de Ferramentas e Diagnósticos

### 6.1 `analyze_animation` e o Alerta de `no_visible_motion`
- **Fato observado**: A ferramenta retornou `issue: "no_visible_motion"`, alegando que nada se move mais que meia unidade.
- **Causa real**: A heurística mede apenas translação linear (`travel` / deslocamento de pivô dos bones). No nosso rig, `head_center` e `jaw_center` possuem rotação pura (`rotation` channel) em torno de seus pivôs fixos contratuais.
- **Evidência visual**: A rotação de -34° na mandíbula desloca a ponta do queixo por cerca de 25 unidades de arco descendente no espaço de modelo. O alerta é uma limitação da métrica de translação do upstream, e não uma imobilidade real do modelo. Conforme instruído nas regras de colaboração, a amplitude anatômica não foi adulterada para satisfazer cegamente o validador.

### 6.2 `check_rig` e Alertas de Membros Faltantes
- **Fato observado**: `check_rig` acusou `no_root_bone`, `no_spine`, `missing_limb: right arm`.
- **Causa real**: O validador testa heurísticas para bípedes humanoides. Este modelo é um componente específico e isolado de cabeça e mandíbula (`head_center` e `jaw_center`) para uma hidra. Criar ossos de braços ou pernas humanos corromperia o contrato do mod.

### 6.3 `check_model` e Coplanaridade
- Ajustados os volumes internos de palato, fangs e paredes laterais para manter penetrações e offsets de 0.2 a 0.3 unidades, prevenindo cintilação (*z-fighting*) no renderizador do Blockbench.

---

## 7. Verificação de Persistência da Cópia Reaberta

Após o salvamento por MCP `save_project` no caminho:
`C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/lab/antigravity/piloto01/ag_hydra_head_v2.bbmodel`,
a cópia foi descarregada e reaberta via MCP `load_project`, apresentando:
- `name`: `ag_hydra_head_v2`
- `format`: `free` (Generic Model)
- `cubes`: 62 (todos persistidos com seus nós de outliner e UVs intactos)
- `groups`: 2 (`head_center` e `jaw_center`, com pivôs preservados em `[0,0,0]` e `[0,-8.5,-10]`)
- `textures`: 1 (`ice_hydra.png`, 128x128)
- `animations`: 3 (`animation.hydra_lab.jaw_test`, `animation.hydra_lab.jaw_roar_2s`, `animation.hydra_lab.jaw_roar_v2`)

---

## 8. Defeitos Restantes, Limitações e Próximos Passos

### O que foi observado vs O que é hipótese
- **Comprovado**: Autoria, refinamento de geometria, rigging anatômico, preservação de textura, clipe de 2.4s com fechamento limpo e persistência no arquivo `.bbmodel` reaberto pelo Blockbench MCP.
- **Hipótese a validar**: Como a rotação de `-34°` da mandíbula se comporta visualmente quando a hidra estiver acoplada aos outros 2 pescoços secundários e ao corpo completo, e se controladores Java de pose procedural (`applyRotations`) interagem com o canal autorado.

### Limitações Materiais
- As poses amostradas estáticas comprovam cinemática chaveada, mas não constituem renderização a 60 FPS contínua.
- O formato GeckoLib nativo permanece pendente de instalação oficial do plugin no Blockbench; o modelo foi trabalhado no formato Generic Model (`free`), em total conformidade com a autorização desta rodada.
- Este modelo não foi exportado nem integrado aos diretórios de produção `src/main/resources/assets/bleachmod/`.

### Próximo Teste Específico Recomendado
- Ensaio de clipe de transição suave entre o estado de repouso respiratório (*idle breathe*) e o rugido `jaw_roar_v2` em laboratório, verificando blend de poses antes de qualquer tentativa de integração ao corpo principal.
