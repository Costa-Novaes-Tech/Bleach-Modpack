---
name: bleach-model-pipeline
description: Pipeline completo de modelagem, rig, texturização procedural, teste em laboratório Blockbench e integração GeckoLib/Java no Bleach Mod.
---

# Pipeline Completo de Modelagem, Laboratório e Integração — Bleach Mod

Este documento sintetiza o método consolidado de desenvolvimento de entidades e habilidades do Bleach Modpack no checkout de design (`Bleach-Modpack-design`, branch `codex/design-poderes-criaturas`), estabelecendo o percurso obrigatório da concepção até a validação em jogo.

---

## 1. Ciclo de Vida do Asset

```
  [1. Concepção & Referência]
             │ (Análise visual de proporção, silhueta e pivôs)
             ▼
  [2. Laboratório Blockbench (MCP)]
             │ (Cópia ag_ isolada em tools/models/lab/antigravity/)
             ▼
  [3. Pipeline Mestre de Assets]
             │ (tools/models/hydra_organic.py + ice_hydra.py)
             ▼
  [4. Texturização & UV Atlas Dedicado]
             │ (Mapeamento per-face explícito em quadrantes dedicados)
             ▼
  [5. Integração com GeckoLib & Java Procedural]
             │ (IceDragonRenderer, HydraMotion, ZanpakutoReleaseAnimation)
             ▼
  [6. Validação em Três Níveis: Check -> GameTests -> ClientQa]
             │ (Evidências visuais e telemetria em Docs/poderes/qa/antigravity/)
             ▼
  [7. Documentação Técnica em Docs/poderes/]
```

---

## 2. Protocolo de Laboratório Blockbench (MCP)

1. **Editor Compartilhado**:
   - Nunca altere modelos abertos pertencentes a outros autores.
   - Sempre utilize `load_project` ou crie uma nova aba salvando cópias com prefixo `ag_` na pasta `tools/models/lab/antigravity/<etapa>/`.
   - O comando `execute_script` permanece estritamente desativado.
2. **Pivôs e Hierarquia Canônica**:
   - Nomes de bones representam contratos públicos com o código Java (`root`, `head_center`, `jaw_center`, `neck_{name}_{1..12}`).
   - Pivôs anatômicos devem ser cravados no ponto real de articulação física (ex: `jaw` em `[0, -8.5, -10]` na base condilar, nunca na ponta dos dentes).

---

## 3. Arquitetura de Geração e Consistência Mestra

Para que modificações não desapareçam quando os geradores do projeto forem executados novamente, os assets não devem ser editados apenas como arquivos estáticos, mas integrados na raiz do pipeline:

1. **Definição Anatômica**:
   - `tools/models/hydra_organic.py`: Estruturação da geometria tridimensional, cálculo de raios dinâmicos, subdivisão de segmentos e animações base GeckoLib.
2. **Gerador e Exportador Mestre**:
   - `tools/models/ice_hydra.py`: Síntese procedural de texturas RGBA, compilação de geometrias `.geo.json`, clipes `.animation.json` e projeto `.bbmodel`.
3. **Checagem Idempotente Obrigatória**:
   - `python tools/models/ice_hydra.py --check`
   - O comando deve retornar `Exit 0` confirmando que todos os arquivos de runtime (`geo.json`, `animation.json`, `texture.png`, `.bbmodel`) correspondem estritamente à fonte.

---

## 4. Texturização Procedural e Mapeamento UV Atlas

1. **Zonas UV Dedicadas**:
   - Evitar mapeamento de caixa cego (`box UV` a partir de `0, 0`), que causa sobreposição desastrosa em paletas sensíveis (como dentes, olhos ou boca).
   - Estruturar quadrantes específicos no atlas (ex: 128x128):
     - `Zone 0`: Olhos (`0..15, 0..15`)
     - `Zone 1`: Presas e Dentes (`16..31, 0..15`)
     - `Zone 2`: Garganta / Cavidade Oral Profunda (`0..15, 16..31`)
     - `Zone 3`: Língua e Palato (`16..31, 16..31`)
     - `Zone 4`: Chifres e Estrias Glaciais (`0..31, 32..47`)
     - `Zone 5`: Penas e Asas Translúcidas (`32..63, 0..31`)
     - `Zone 6`: Placas Ventrais Gulares / Gastrosteges (`0..31, 48..79`)
     - `Zone 7`: Espinhos Glaciais e Cristas Dorsais (`32..63, 48..79`)
     - `Zone 8`: Matriz de Escamas em Diamante (`64..95, 48..79`)
     - `Zone 9`: Colarinhos e Aros Articulares (`96..127, 48..79`)
     - `Zone 10`: Facetas Prismáticas de Núcleo (`0..63, 80..111`)
2. **Mapeamento Per-Face Explícito**:
   - Cada face exposta (`north`, `south`, `east`, `west`, `up`, `down`) recebe suas coordenadas $[U, V, U+W, V+H]$ exatas, garantindo arestas geadas (`RGB(240, 252, 255)`), sulcos sombreados e destaques especulares.

---

## 5. Coordenação entre GeckoLib e Controle Procedural Java

1. **Convenção de Rotação e Sinais**:
   - O GeckoLib 4.0 aplica rotações de bone em torno de `Axis.XP`.
   - No sistema de coordenadas de modelos Minecraft (onde $-Z$ é frontal e $+Y$ é superior), uma rotação **positiva** em $X$ eleva a geometria para cima (empurrando a mandíbula para dentro do crânio), enquanto uma rotação **negativa** em $X$ abaixa a mandíbula abrindo a boca.
   - Qualquer cálculo procedural em Java deve aplicar `(float)Math.toRadians(-angleDeg)`.
2. **Curvas de Atuação Orgânica em 6 Fases**:
   - `Fase 1 (Repouso)`: $0^\circ$, hermeticamente selado com escudos condilares.
   - `Fase 2 (Tensão/Aviso)`: $+0.5^\circ$, leve contração antes do rugido.
   - `Fase 3 (Abertura)`: Rampa suave cosenoidal até abertura máxima ($34^\circ$).
   - `Fase 4 (Sustentação Estável)`: Abertura firme durante o fluxo/sopro, com micro-tremor de Reiatsu ($0.75^\circ \cdot \sin$), eliminando oscilações mastigatórias artificiais.
   - `Fase 5 (Fechamento)`: Desaceleração suave.
   - `Fase 6 (Snap de Mordida em Bote)`: Fechamento instantâneo no instante exato do impacto (`impactTick`).
3. **Escalonamento Temporal de Criaturas com Múltiplas Cabeças**:
   - Aplicar offsets orgânicos entre as cabeças (ex: $0.0$, $2.5$ e $4.0$ ticks) para evitar movimento mecânico síncrono em bloco.

---

## 6. Poses do Jogador com PlayerAnimator

1. **Cinemática Direta e Ancoragem**:
   - Ancorar os pés rigidamente no plano de solo ($Y = 24.0$).
   - Manter afastamento frontal seguro do cabo e da lâmina ($Z \le -6.0$ pixels, tipicamente $Z = -8.422$) para evitar interseção poligonal com o tórax ou pernas.
   - Empunhadura bimanual precisa: mão primária em $Y = 3.0$ e secundária em $Y = 6.0$, repousando contra a tsuba sem tocar na lâmina afiada.
   - Direção da espada estritamente descendente ($Y > 0.999999$).
2. **Dinâmica Temporal e Liberação Limpa**:
   - Interpolação acompanhando marcos cinemáticos (preparação, acúmulo, erupção, explosão, recuperação).
   - No tick final da transformação/habilidade, atenuar o peso (`weight`) exatamente a `0.0`, com limpeza integral de variáveis para garantir **zero travas residuais** de movimento no jogador.
3. **Compatibilidade Dupla**:
   - Suporte verificado tanto para modelos padrão (Steve - 4px) quanto slim (Alex - 3px).
   - Suporte a 1ª pessoa (`RenderHandEvent` com pitch e translação correspondentes) e 3ª pessoa.

---

## 7. Roteiro Obrigatório de Validação e Evidências

Antes de concluir qualquer entrega de design ou modelo:
1. `python tools/models/ice_hydra.py --check` $\to$ Consistência total.
2. `./tools/antigravity/Invoke-Design.ps1 -Mode Check` $\to$ Compilação limpa (`Exit 0`).
3. `./tools/antigravity/Invoke-Design.ps1 -Mode GameTests` $\to$ Suíte de testes headless aprovada com 100% de sucesso (`Exit 0`).
4. `./tools/antigravity/Invoke-Design.ps1 -Mode ClientQa` $\to$ Cliente Minecraft Forge real executando a suíte de cenários com auto-exit (`Exit 0`), coletando FPS e telemetria.
5. **Arquivamento de Evidências**:
   - Extrair capturas e arquivos `.jsonl` em `Docs/poderes/qa/antigravity/<subpasta>/`.
   - Registrar relatório formal detalhado em `Docs/poderes/<num>-<descricao>.md`.
