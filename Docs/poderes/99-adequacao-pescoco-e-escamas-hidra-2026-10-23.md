# 99 — Adequação e Melhoria dos Pescoços e Escamas da Hidra de Gelo

- **Data**: 23/10/2026
- **Workspace**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- **Branch**: `codex/design-poderes-criaturas`
- **Commit Base**: `132c42d` (com alterações locais autorizadas)
- **Status da Validação**:
  - `python tools/models/ice_hydra.py --check`: **MATCH (Exit 0)**
  - `Invoke-Design.ps1 -Mode Check`: **BUILD SUCCESSFUL (Exit 0)** em 28s
  - `Invoke-Design.ps1 -Mode GameTests`: **179/179 PASSADOS (Exit 0)** em 1m 25s
  - `Invoke-Design.ps1 -Mode ClientQa`: **10 Cenários Aprovados em Jogo Real (Exit 0)** em 3m 33s (116–119 FPS)

---

## 1. Contexto e Objetivos

Com a cabeça dracônica `v3` integrada e as mandíbulas sincronizadas ao GeckoLib/PlayerAnimator, o modelo dos pescoços ainda apresentava a geometria rudimentar de protótipo: dois blocos retangulares cruzados em ângulo reto por elo, espinhos dorsais rombudos sem continuidade com a nuca, ausência total de placas ventrais (gulares) na garganta e mapeamento de textura genérico que amostrava a paleta de olhos e dentes.

O objetivo desta etapa foi:
1. **Adequar a Proporção e Encaixe Cervical**: Fazer o diâmetro dos 12 anéis afilar suavemente da base maciça até o colar nucal da cabeça `v3`, eliminando a protuberância e degraus abruptos.
2. **Refinar a Anatomia Dracônica Tridimensional**: Construir 10 cubos especializados por anel (coluna prismática octogonal, cristas e espinhos dorsais pontiagudos com raízes de reforço, placas ventrais gulares e colarinho articular imbricado para vedação de curvas).
3. **Escamas e Texturas Detalhadas**: Criar zonas dedicadas no atlas 128x128 para placas gulares com sulcos horizontais, espinhos cristalinos, matriz de escamas em diamante e colarinho articulado, mapeando cada face via UV per-face explícito.

---

## 2. Anatomia e Geometria dos Pescoços Refinados

### 2.1 Curva de Afilamento e Conexão com a Cabeça
Anteriormente, o pescoço utilizava $r = 25.5 - i \times 0.75$, resultando em $r = 17.25$ no 12º anel (largura de 34.5 pixels), enquanto o colarinho nucal da cabeça `v3` (`neck_cervical_collar`) media apenas 21.0 pixels de largura ($X \in [-10.5, 10.5]$). Essa discrepância criava um degrau visual onde o topo do pescoço vazava pelas laterais do crânio.

A nova equação de raio dinâmico foi calibrada para:
$$r(i) = 11.0 + 11.0 \times \left(\frac{11 - i}{11}\right)^{1.1}$$

- **Base ($i = 0$)**: $r = 22.0$ (largura do núcleo de 35.2 pixels), conferindo ancoragem muscular e sólida na raiz congelada.
- **Transição Média ($i = 6$)**: $r = 15.9$, mantendo volume proporcional à curvatura sinuosa da hidra.
- **Topo sob a Cabeça ($i = 11$)**: $r = 11.0$ (largura do núcleo de 17.6 pixels; com flancos laterais atinge exatamente 22.0 pixels), encaixando-se no colarinho nucal `[-10.5, 3, 6]` a `[10.5, 14.5, 18]`.

### 2.2 Estrutura de 10 Cubos por Anel Articulado
Cada um dos 12 anéis dos 3 pescoços (total de 360 cubos de pescoço) agora é composto por:

1. **Núcleo Central Prismático (`neck_core_center`)**: Bloco estrutural com altura $Y = 16.5$ (leve sobreposição de 0.5 pixel para eliminar frestas durante curvas acentuadas).
2. **Flancos Laterais Biselados (`neck_flank_left` e `neck_flank_right`)**: Criam uma seção transversal octogonal que quebra os reflexos de luz em facetas cristalinas.
3. **Placas Ventrais Gulares (`neck_ventral_plate`)**: Localizadas em $-Z$, cobrindo o peito e a garganta com degrau saliente de 1.0 a 15.5 pixels de altura. É a região mais visível quando a hidra ergue as cabeças para rugir.
4. **Espinho Glacial Dorsal (`neck_dorsal_spine`)**: Lâmina cristalina saliente em $+Z$ com comprimento longitudinal progressivo ($5.5$ pixels na base até $11.5$ pixels no topo), alinhando-se com os escudos nucais `neck_dorsal_shield_1` e `neck_dorsal_shield_2` da cabeça.
5. **Raízes Cristalinas Laterais (`neck_spine_root_left` e `neck_spine_root_right`)**: Facetas de ancoragem na base de cada espinho dorsal, garantindo aspecto chiseled tridimensional.
6. **Cristas de Escamas Laterais (`neck_scale_ridge_left` e `neck_scale_ridge_right`)**: Armadura lateral em quilha que quebra a silhueta cilíndrica.
7. **Colarinho Imbricado de Articulação (`neck_collar_ring`)**: Extensão de vedação na base ($Y = -0.75$ a $2.75$) com diâmetro ligeiramente superior ($1.04\times$), cobrindo a junta articular quando a spline se curva até $35^\circ$.

---

## 3. Matriz de Texturas e Escamas Detalhadas

O gerador `tools/models/ice_hydra.py` foi expandido para estruturar o quadrante livre do atlas 128x128 ($V \ge 48$):

| Zona | Região UV ($U, V$) | Descrição e Efeito Visual no Jogo |
| :--- | :--- | :--- |
| **Zona 6** | `x: 0..31, y: 48..79` | **Placas Ventrais (Gastrosteges)**: Placas gulares com gradiente de ciano gélido a índigo profundo, sulcos de sombra entre placas e ápices com geada branca (`RGB(220, 245, 255)`). |
| **Zona 7** | `x: 32..63, y: 48..79` | **Espinhos Dorsais Glaciais**: Facetas angulares com arestas brancas puras (`RGB(242, 252, 255)`), brilho especular e núcleo de refração translúcido. |
| **Zona 8** | `x: 64..95, y: 48..79` | **Escamas em Diamante / Matriz Reptiliana**: Escamas entrelaçadas em padrão losangular com bordas profundas e ápices destacados. |
| **Zona 9** | `x: 96..127, y: 48..79` | **Colarinhos Articulados e Aros de Vedação**: Textura de aro metálico-glacial biselado. |
| **Zona 10** | `x: 0..63, y: 80..111` | **Facetas Prismáticas de Gelo**: Bandas de cristais suaves sem zebrados repetitivos. |

Cada face de cada cubo nos 12 anéis possui mapeamento per-face explícito, garantindo que as escamas laterais, espinhos dorsais e placas ventrais recebam a respectiva textura sem distorção.

---

## 4. Pipeline e Coerência de Arquivos

A atualização foi integrada na fonte mestra de geração para preservar a reproducibilidade do projeto:
- `tools/models/hydra_organic.py`: Função `build_neck_link_cubes(i)` e integração em `build_geo_json()`.
- `tools/models/ice_hydra.py`: Implementação dos novos blocos procedurais na função `generate_hydra_texture_bytes()`.
- **Sincronização**: Os arquivos de produção [ice_hydra.geo.json](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/geo/ice_hydra.geo.json), [ice_hydra.animation.json](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/animations/ice_hydra.animation.json), [ice_hydra.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/src/main/resources/assets/bleachmod/textures/entity/ice_hydra.png) e [ice_hydra.bbmodel](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/source/ice_hydra.bbmodel) foram regenerados e validados com `--check`.
- **Cópia de Laboratório**: Salva em [tools/models/lab/antigravity/pescoco01/ag_hydra_neck_v1.bbmodel](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/lab/antigravity/pescoco01/ag_hydra_neck_v1.bbmodel).

---

## 5. Validação Automatizada e Evidências em Jogo

### 5.1 Testes Executados
1. **Validação de Consistência**:
   - `python tools/models/ice_hydra.py --check` $\rightarrow$ **MATCH (Exit 0)**.
2. **Compilação e Verificação (`Check`)**:
   - Job `2026-10-23T12-21-15-844Z-Check` $\rightarrow$ **BUILD SUCCESSFUL (Exit 0)** em 28s.
3. **Suíte Completa de GameTests**:
   - Job `2026-10-23T12-22-22-397Z-GameTests` $\rightarrow$ **179 de 179 GameTests aprovados (Exit 0)** em 1m 25s.
4. **Validação Visual no Minecraft Real (`ClientQa`)**:
   - Job `2026-10-23T12-24-47-327Z-ClientQa` $\rightarrow$ **10 Cenários Aprovados (Exit 0)** em 3m 33s.
   - Taxa de quadros no cliente: **116 a 119 FPS constantes**, demonstrando que os 360 cubos anatômicos possuem custo de renderização insignificante.

### 5.2 Acervo de Evidências Visuais e Telemetria
Os arquivos capturados pelo cliente real de teste foram organizados em [Docs/poderes/qa/antigravity/pescoco-escamas/](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pescoco-escamas/):

| Arquivo de Evidência | Detalhe em Destaque |
| :--- | :--- |
| [trans_frame-t112.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pescoco-escamas/trans_frame-t112.png) | Emergência das hidras: Espinhos dorsais escalonados e placas ventrais em plena elevação. |
| [trans_frame-t176.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pescoco-escamas/trans_frame-t176.png) | Curvatura intermediária: Colarinhos imbricados vedando as juntas entre elos. |
| [trans_frame-t224.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pescoco-escamas/trans_frame-t224.png) | Encaixe perfeito no colarinho nucal da cabeça v3 sem degraus ou vazamentos. |
| [trans_frame-t264.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pescoco-escamas/trans_frame-t264.png) | Vista lateral de alta curvatura: Leitura clara das escamas em diamante e espinhos cristalinos. |
| [trans_frame-t312.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pescoco-escamas/trans_frame-t312.png) | Visão inferior gular: Placas ventrais com sulcos escuros e ápices geados. |
| [assault_frame-t028.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pescoco-escamas/assault_frame-t028.png) | Ataque Slot 2: Trajetória rasante das serpentes com silhueta afilada contínua. |
| [assault_frame-t060.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pescoco-escamas/assault_frame-t060.png) | Instante de bote no ataque triplo com articulação cervical íntegra. |
| [trans_frames.jsonl](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/pescoco-escamas/trans_frames.jsonl) | Telemetria completa quadro a quadro comprovando 116–119 FPS estáveis. |
