# Relatório de Avaliação e Iteração do Piloto da Hidra (Piloto 02) — Antigravity QA

- **Data**: 22/10/2026
- **Workspace**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- **Branch**: `codex/design-poderes-criaturas`
- **Commit HEAD**: `132c42d` (após retificação técnica do documento 96)
- **Ambiente de Teste**: Laboratório isolado via MCP `bleach-blockbench` (127.0.0.1:8787).
- **Preservação de Escopo**: Nenhuma execução de Gradle, cliente Minecraft, servidor ou MCP `bleach-design-runtime` foi realizada. Nenhum arquivo em `src/`, gameplay ou modelo de produção GeckoLib foi alterado. Nenhum comando git (`commit`, `push`, `merge`, `rebase`, `reset`, `stash`) foi executado.

---

## 1. Arquivos Alterados e Produzidos Nesta Rodada

### 1.1 Documentação Retificada
- [96-sincronizacao-e-correcao-cinematica-hyorinmaru-2026-10-22.md](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/96-sincronizacao-e-correcao-cinematica-hyorinmaru-2026-10-22.md):
  - Inserida a seção preliminar `0. Retificação Técnica e Auditoria Codex`.
  - Retificados os 5 pontos da auditoria independente:
    1. Demonstração de que na base anterior (`830ad64`) tanto cliente quanto servidor usavam `DURATION = 70` (sem conflito prévio com 400 ticks).
    2. Confirmação de que `animation.ice_dragon.flight` existia nos assets anteriores.
    3. Esclarecimento de que a ausência do áudio da trilha não causava congelamento do SoundEngine.
    4. Correção formal de procedência: alterações em `src/` pertencem à branch `Feature-Poderes-bankais` (autoria Codex, ciclos 86 a 93), tendo sido apenas sincronizadas via merge.
    5. Confirmação explícita de que os modelos de laboratório nunca foram integrados ao jogo.

### 1.2 Ativos do Laboratório de Design (Piloto 02)
- [ag_hydra_head_v3.bbmodel](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/lab/antigravity/piloto02/ag_hydra_head_v3.bbmodel):
  - Novo modelo experimental da cabeça e mandíbula dracônica da hidra.
  - 67 cubos, 2 ossos contratuais (`head_center`, `jaw_center`), textura customizada dedicada, clipe de rugido `animation.hydra_lab.jaw_roar_v3`.
- [ice_hydra_v3.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/lab/antigravity/piloto02/ice_hydra_v3.png):
  - Cópia e customização da textura de 128x128 com zoneamento tonal próprio para cavidade bucal escura, presas de gelo cristalino, facetas de chifre e olhos ciano luminescentes.

### 1.3 Evidências e Capturas de QA
- Diretório: [Docs/poderes/qa/antigravity/piloto02/](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/piloto02/)
  - Capturas estáticas do modelo neutro: `v3_front.png`, `v3_left.png`, `v3_front_right.png`.
  - Poses sequenciais da animação de rugido (16 imagens): `v3_pose_t00_*.png` a `v3_pose_t26_*.png` nas vistas frontal/direita e lateral esquerda.
  - Prévia contínua em formato animado: `v3_animation_preview_front_right.gif` e `v3_animation_preview_left.gif`.

---

## 2. Reavaliação Visual Concreta e Decisões Anatômicas

Com base na comparação direta entre o conceito original ([concept_bankai_hydra.jpg](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/media/concept/concept_bankai_hydra.jpg)) e a entrega preliminar ([piloto01/relatorio.md](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/piloto01/relatorio.md)), foram identificados os problemas específicos e aplicadas as seguintes soluções geométricas:

### 2.1 Proporção entre Crânio, Focinho e Mandíbula
- **Defeito no Piloto 01**:
  - O crânio era um bloco monolítico retangular de 28x18x30 unidades e o focinho uma extensão em degrau de 17x9.5x38 unidades sem conicidade no eixo Z.
  - *Efeito indesejado*: Parecia uma caixa de sapatos escalonada com perfil de canino/crocodilo genérico.
- **Mudança Geométrica Implementada no Piloto 02**:
  - O focinho foi fatiado em 3 seções trapezoidais afiladas:
    - Base posterior (`snout_posterior`): largura 17 un., altura 9.5 un.
    - Região medial (`snout_mid`): largura 14.4 un., altura 9 un.
    - Rostro anterior (`snout_rostrum_apex`): largura 11.6 un., altura 8.5 un.
  - Adicionada crista nasal em declive longitudinal (`snout_nasal_crest`, inclinada a -3°) e saliências caninas laterais (`snout_canine_flare_left/right`).
  - A mandíbula inferior segue exatamente a mesma redução de largura (de 17.6 un. na base para 11.6 un. no queixo), eliminando o aspecto de bloco reto.

### 2.2 Contorno e Continuidade dos Chifres
- **Defeito no Piloto 01**:
  - Chifres compostos por pares de cubos empilhados com pivôs arbitrários e ângulos de rotação desconexos (`[28, 14, -8]` na base e `[36, 16, -10]` na ponta com origens diferentes).
  - *Efeito indesejado*: Linha de contorno quebrada com degraus e vãos vazios no perfil; leitura de barras soltas sobrepostas em vez de lâminas de gelo.
- **Mudança Geométrica Implementada no Piloto 02**:
  - Construção em cadeia tangencial de 3 segmentos por chifre principal:
    - Base: `horn_prim_base_left/right` (tamanho 4x7x10, rotação `[26, 12, -6]`).
    - Medial: `horn_prim_mid_left/right` (tamanho 3x8.5x9, rotação `[34, 15, -8]`, face frontal perfeitamente conectada à face traseira da base).
    - Ponta: `horn_prim_tip_left/right` (tamanho 2x10.5x8, rotação `[42, 17, -10]`, afilamento em flecha cristalina).
  - Chifres secundários dispostos em leque lateral com angulações externas harmônicas (`[18, 22, -14]` e `[26, 26, -16]`), sem intersecções desordenadas.

### 2.3 Formato dos Olhos e Sobrancelhas
- **Defeito no Piloto 01**:
  - Olhos planos colados na lateral da caixa craniana, sob uma laje horizontal flutuante.
  - *Efeito indesejado*: Pareciam botões adesivos ou uma viseira suspensa sem encaixe anatômico.
- **Mudança Geométrica Implementada no Piloto 02**:
  - Criação de órbitas recuadas (`eye_socket_left/right`, 2x4x6 un.) encaixadas sob arcos supraciliares cristalinos (`brow_ridge_arch_left/right`) inclinados em cunha a `[-6, 10, -4]`.
  - A sobrancelha projeta sombra física sobre o olho. O olho foi mapeado para a nova zona de textura com íris ciano luminescente `(70, 215, 255)`, pupila em fenda e contorno escuro.

### 2.4 Transição Cabeça / Pescoço
- **Defeito no Piloto 01**:
  - Extremidade posterior terminava em corte reto vertical a 90° (`Z: 12`), com apenas duas aletas dorsais isoladas.
  - *Efeito indesejado*: Descontinuidade total com os anéis circulares do pescoço da criatura.
- **Mudança Geométrica Implementada no Piloto 02**:
  - Adicionado colarinho nucal (`neck_cervical_collar`) estendido até `Z: 18`.
  - Adicionadas duas placas dorsais imbricadas em escamas sobrepostas (`neck_dorsal_shield_1` a -12° e `neck_dorsal_shield_2` a -18° estendendo-se até `Z: 24`), acompanhadas de espículas laterais nucais. Essa geometria casa volumetricamente com o diâmetro do anel `neck_center_1` do modelo de produção (`r = 25.5`).

### 2.5 Dentes, Cavidade Bucal e Articulação
- **Defeito no Piloto 01**:
  - Todos os 16 dentes eram prismas retos verticais uniformes (`rot = [0,0,0]`). Palato e garganta usavam a textura externa clara. O escudo da junta ficava preso ao crânio, expondo um buraco na abertura.
  - *Efeito indesejado*: Dentes sem agressividade predatória, boca sem profundidade de sombra e articulação visualmente desmontada ao rugir.
- **Mudança Geométrica Implementada no Piloto 02**:
  - **Dentição**: Presas superiores maiores (`fang_upper_sabre_left/right`, comprimento 9.5 un.) com inclinação real para trás (`rotX = -8°`) e canting interno (`rotZ = 4°`). Dentes inferiores recuados em Z (`Z: -41` a `-37`), encaixando-se internamente em mordida em tesoura sem colisão quando fechados.
  - **Profundidade**: Palato abobadado (`palate_vault_roof`) e câmara de garganta profunda (`throat_vortex_chamber`) mapeados para o tom índigo abissal `(12, 22, 45)`. Assoalho da mandíbula rebaixado com núcleo de língua de gelo escuro.
  - **Articulação**: Escudos condilares (`jaw_condyle_shield_left/right`) vinculados à mandíbula (`jaw_center`), subindo até o pivô `[0, -8.5, -10]`. Conforme a mandíbula abre até -33.5°, os escudos deslizam sob as placas temporais do crânio, cobrindo o côndilo em todos os ângulos.

### 2.6 Materiais e Textura
- **Defeito no Piloto 01**:
  - Textura única original compartilhada, onde palato, garganta e dentes apontavam para coordenadas de azul ciano claro idêntico ao corpo exterior.
- **Mudança Implementada no Piloto 02**:
  - Criação da textura dedicada `tools/models/lab/antigravity/piloto02/ice_hydra_v3.png`.
  - Quatro zonas dedicadas preservando compatibilidade com o atlas 128x128:
    - Interior bucal / garganta: `uv=[0, 16, 16, 32]` (índigo escuro com gradiente de profundidade).
    - Língua: `uv=[16, 16, 32, 32]` (azul gélido fosco com borda esbranquiçada).
    - Presas: `uv=[16, 0, 32, 16]` (branco gelo translúcido no ápice a ciano na raiz).
    - Olhos: `uv=[0, 0, 16, 16]` (fenda brilhante com esclera azul profunda).
    - Chifres: `uv=[0, 32, 32, 48]` (estrias com arestas claras).

---

## 3. Análise da Animação de Rugido (`jaw_roar_v3`)

- **Identificador**: `animation.hydra_lab.jaw_roar_v3`
- **Duração**: 2.6 segundos | **Modo**: Loop | **Interpolação**: Catmull-Rom
- **Rig Contratual**: 2 bones (`head_center` em `[0,0,0]`, `jaw_center` em `[0, -8.5, -10]`).

### Fases e Dinâmica
1. `t = 0.00s` (**Repouso Neutro**): Boca fechada, presas encaixadas em tesoura sem atravessamento, cabeça em repouso.
2. `t = 0.35s` (**Antecipação / Compressão**): Cabeça desce levemente (`rotX = -3.5°`, `posY = -0.6`) e a mandíbula aperta firmemente para cima (`rotX = +1.8°`), transmitindo tensão prévia ao ataque.
3. `t = 0.70s` (**Abertura Agressiva**): Cabeça ergue-se rápido (`rotX = +6.0°`, `rotY = +1.5°`) e mandíbula cai a `-24.0°`.
4. `t = 1.15s` (**Ápice do Rugido**): Abertura máxima a `-33.5°`, cabeça recuada a `+8.5°`. A cavidade escura da garganta e as presas translúcidas ficam completamente expostas.
5. `t = 1.50s` (**Tremor Sustentado**): Mandíbula sustentada a `-31.5°` enquanto a cabeça realiza micro-oscilação contrária (`rotX = +7.5°`, `rotY = -1.5°`), simulando o esforço contínuo da baforada.
6. `t = 1.85s` (**Recuperação Ponderada**): Mandíbula fecha para `-12.0°` com cabeça amortecendo para `+2.5°`.
7. `t = 2.20s` (**Amortecimento / Overshoot**): Mandíbula atinge o neutro e a cabeça desce ligeiramente abaixo de zero (`rotX = -1.2°`) pelo peso da estrutura.
8. `t = 2.60s` (**Fechamento Perfeito**): Retorno contínuo a zero exato em todos os canais.

### Validação das Ferramentas MCP
- `analyze_animation`:
  - `total_keyframes`: 24 (16 em `head_center`, 8 em `jaw_center`).
  - `travel`: 1.44 un. | `max_forward`: 0.8 un. (no ápice 1.14s) | `max_backward`: 0.41 un. (na antecipação 0.33s).
  - `rise`: 1.2 un. | `drop`: -0.62 un.
  - `loop_gap`: **0** (fechamento perfeito).
  - `verdict`: *"The motion measures clean."*
- `check_model`:
  - `issue_count`: **0** (0 coplanar overlaps, 0 unpaired sides).
- `audit_complexity`:
  - `verdict`: *"acceptable"* (0% micro-cubos, 35 elementos rotacionados).

---

## 4. Comparação Visual e Evidências

### 4.1 Evidências Estáticas (Comparações Equivalentes)

| Vista | Piloto 01 (`ag_hydra_head_v2`) | Piloto 02 (`ag_hydra_head_v3`) | Avaliação Concreta da Diferença |
| :--- | :--- | :--- | :--- |
| **Frontal** | `before_front.png` | [v3_front.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/piloto02/v3_front.png) | No v2, o crânio era uma caixa reta e as narinas um bloco plano. No v3, silhueta em cunha trapezoidal, arcadas supraciliares sombreando olhos ciano, narinas anguladas e barbela afilada no queixo. |
| **Perfil Esquerdo** | `before_left.png` | [v3_left.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/piloto02/v3_left.png) | No v2, os chifres quebravam em duas barras desiguais e a nuca cortava reto a 90°. No v3, curvatura contínua dos chifres em 3 estágios, placas cervicais sobrepostas e presas sabre inclinadas para trás. |
| **3/4 Frontal Direita** | `before_front_right.png` | [v3_front_right.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/piloto02/v3_front_right.png) | No v2, aspecto de caixa retangular com dentes retos. No v3, relevo dracônico com cristas nasais, presas sabre salientes e chifres externos em leque. |

### 4.2 Prévia Contínua da Animação
Para atender ao requisito de prévia contínua sem depender exclusivamente de poses amostradas estáticas, foram sintetizados loops em GIF a partir dos quadros da renderização:
- [v3_animation_preview_front_right.gif](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/piloto02/v3_animation_preview_front_right.gif): Vista isométrica 3/4 demonstrando o ciclo completo (antecipação, escancaração, tremor, sustentação e amortecimento).
- [v3_animation_preview_left.gif](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/piloto02/v3_animation_preview_left.gif): Vista de perfil confirmando a manutenção da cobertura articular pela blindagem condilar e ausência de descolamento da mandíbula.

---

## 5. Verificação de Persistência (Salvar e Reabrir)

O modelo foi salvo via MCP `save_project` e reaberto do zero através de `load_project`:
- **Resultado do `load_project`**:
  - `name`: `ag_hydra_head_v3`
  - `format`: `free` (Generic Model)
  - `cubes`: 67 (preservados integralmente)
  - `groups`: 2 (`head_center`, `jaw_center`)
  - `textures`: 1 (`ice_hydra_v3.png` embutida em base64 e salva em disco)
  - `animations`: 1 (`animation.hydra_lab.jaw_roar_v3`, 24 keyframes íntegros)
  - `check_model`: `issue_count: 0`.

---

## 6. Julgamento Técnico e Artístico Honesto

### 6.1 O que Melhorou Claramente
1. **Linguagem Dracônica Coerente**: A cabeça deixou de parecer um caixote retangular com estacas e passou a ter perfil aerodinâmico em cunha, cristas nasais salientes e coroa de chifres cristalinos com continuidade tangencial.
2. **Profundidade Oral e Presas**: A boca aberta agora tem cavidade anatômica sombreada (índigo escuro) em vez do azul claro externo. As presas superiores têm inclinação real para trás e as inferiores encaixam por dentro em mordida em tesoura sem atravessamentos.
3. **Articulação da Mandíbula**: A inclusão dos escudos condilares (`jaw_condyle_shield_left/right`) elimina a fresta vazia atrás da mandíbula durante a rotação máxima de -33.5°.
4. **Qualidade do Rigging e Movimento**: A animação tem curva de peso convincente (compressão prévia antes da expansão, tremor de sustentação e desaceleração ponderada), com loop gap 0 medido pela ferramenta.

### 6.2 Limitações e Defeitos Restantes
1. **Facetamento Inerente ao Modelo de Caixas (Voxel Style)**: Como o padrão do Minecraft e do mod utiliza cubos rígidos sem superfícies curvas contínuas (subdivisão/mesh suave), as curvas dos chifres são formadas por 3 prismas rotacionados. Em ângulos muito rasantes, é possível notar pequenos ressaltos angulares nas juntas das facetas.
2. **Avaliação no Viewport vs Shaders de Jogo**: As capturas foram geradas sob a iluminação neutra do viewport do Blockbench. No Minecraft com shaders ativados (como Photon ou Iris), a iluminação direcional e oclusão de ambiente podem intensificar as sombras internas da boca ou clarear faces com normais invertidas.
3. **Isolamento da Cabeça**: O modelo foi trabalhado isoladamente no laboratório com a raiz cervical estendida até `Z: 24`. Embora as dimensões casem com a seção do pescoço de produção (`tools/models/hydra_organic.py`), a interação dinâmica com as 12 articulações vertebrais completas da criatura ainda não foi testada em conjunto.
4. **Não Integração em Produção**: Este modelo de laboratório **NÃO** foi exportado para o arquivo de jogo (`ice_hydra.geo.json`) nem homologado no cliente Minecraft nesta rodada.

---

## 7. Recomendação para Próximos Passos

1. **Aprovação do Design Base**: Submeter as capturas do Piloto 02 ([v3_front_right.png](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/piloto02/v3_front_right.png) e [v3_animation_preview_front_right.gif](file:///C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/piloto02/v3_animation_preview_front_right.gif)) à avaliação humana de estética antes de avançar para as cabeças laterais.
2. **Codec GeckoLib**: Quando a silhueta for aprovada, realizar ensaio controlado de codec para exportação em `.geo.json` do GeckoLib 4.0, garantindo que o modelo de 67 cubos mantenha mapeamento UV íntegro no pipeline Forge.
3. **Cabeças Laterais**: Ajustar as cabeças secundárias (esquerda e direita) utilizando a mesma geometria básica da cabeça central, aplicando espelhamento de chifres e ligeira redução de escala (escala 0.85x a 0.9x) para reforçar a hierarquia visual da cabeça central da hidra.
