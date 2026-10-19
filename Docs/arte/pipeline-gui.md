# Pipeline de GUI: da arte da IA até o HUD no jogo

Este é o fluxo para colocar artes de interface no jogo sem brigar com escala e posição.
Ele tem duas partes:

1. **Converter a arte**: a imagem gerada pela IA vira pixel art de verdade, um pixel de textura para cada "pixel" desenhado.
2. **Layout em JSON**: posições, tamanhos, cores e textos ficam num arquivo que você edita. Com o jogo de teste aberto, a mudança aparece na hora.

---

## 1. Converter a arte

### Por que converter

O ChatGPT gera imagens enormes que só *imitam* pixel art. O painel de status original tinha 2169×725, mas cada "pixel" dele ocupava uns 9 px reais, e a grade nem era regular. Desenhar isso reduzido no jogo deixa tudo borrado, e as coordenadas viram números como `705, 2110`, que ninguém acerta.

O conversor descobre o tamanho da grade, segue as bordas reais (que desviam um pouco ao longo da imagem) e pega a cor dominante de cada célula. O resultado é nítido e pequeno: o painel virou 480×118.

### Passo a passo

1. Salve a imagem original da IA em `art-src/gui/<pasta>/<nome>.png`. Essa pasta é o backup das originais.
2. Adicione uma entrada em `tools/gui/assets.json`:
   ```json
   { "source": "hud/minha_arte.png", "output": "hud/minha_arte.png", "trim": true }
   ```
3. Rode:
   ```bash
   python tools/gui/build_assets.py
   ```
   Para converter só uma: `python tools/gui/build_assets.py minha_arte`.
4. Confira a prévia ampliada 4× em `build/gui-preview/<nome>.x4.png`.

### Opções de conversão (cada uma também é um campo no `assets.json`)

| Campo | Para que serve |
|---|---|
| *(nenhum)* | Detecta a grade sozinho. Funciona na maioria das artes. |
| `grid` | Força o tamanho do "pixel" da IA em px (ex.: `9`). Use quando a detecção errar. **Meia grade** (ex.: `4.47` em vez de `8.95`) preserva textos pequenos desenhados mais finos que o resto. |
| `width` / `height` | Força o tamanho final exato. Use para fills de barra: eles precisam ter **exatamente** o tamanho do poço. |
| `crop` | `[x0, y0, x1, y1]`: recorta a original antes de converter (ex.: tirar a moldura de um fill). |
| `colors` | Limita a paleta (ex.: `32`) para limpar ruído de cor. |
| `trim` | Apara as bordas transparentes. |
| `soft_alpha` | Mantém transparência parcial (brilhos). Por padrão cada pixel fica opaco ou transparente. |
| `autocrop` | Recorta as margens transparentes da original **antes** de converter. Assim `width` vale para a arte, não para a margem. |
| `remove_checker` | Apaga o "xadrez de transparência" que a IA às vezes **desenha** no lugar do fundo transparente. |
| `isolate` | `{"min_luma": 75, "close": 4, "grow": 2}`: separa a arte de uma sombra ou de um brilho escuro **opaco** em volta dela. |
| `cells` | Folha de variações (estados). Use `{"auto": 4}` para achar as 4 maiores peças em ordem de leitura. Use uma lista de caixas `[[x0,y0,x1,y1], ...]` quando a imagem tiver rótulos escritos ou peças encostadas. Cada variação é convertida no mesmo tamanho e enfileirada da esquerda para a direita. |
| `cell_width` / `cell_height` | Tamanho final de cada variação. Basta um; o outro segue a proporção. |
| `derive` | `["hover", "pressed", "disabled"]`: gera esses estados por script a partir da primeira variação (mais claro; mais escuro e 1 px abaixo; cinza). |
| `mirror` | Espelha na horizontal (ex.: a chama da esquerda é a da direita espelhada). |
| `note` | Comentário livre. Use para registrar a ordem dos estados da folha. |

Também dá para rodar direto: `python tools/gui/pixelate.py entrada.png -o saida.png --grid 9`.

### Fontes

Folhas de letras desenhadas pela IA viram uma fonte do Minecraft com `python tools/gui/build_font.py`. A configuração fica em `tools/gui/fonts.json`: a folha, o tamanho do pixel e os caracteres de cada linha, na ordem.

Hoje existe a `bleachmod:title`, a fonte amarela dos títulos. Para usá-la num texto, coloque `"font": "bleachmod:title"`.

### Dicas para pedir arte à IA

- **Não coloque texto que muda na arte** (números, porcentagens, nomes). O jogo desenha esses textos por cima com a fonte do Minecraft. Rótulos fixos ("VIDA") podem ficar na arte, mas saem melhor desenhados grandes.
- Peça **fundo transparente**, **cores chapadas** e **sem anti-aliasing / sem desfoque**.
- Peça cada peça separada: painel vazio, fill de cada barra, ícones. Assim cada uma pode ser posicionada no JSON.
- Para fills de barra, peça uma faixa longa e reta, que depois é convertida no tamanho do poço com `width`/`height`.

---

## 2. Layouts em JSON

Ficam em `src/main/resources/assets/bleachmod/gui_layouts/<id>.json`. Hoje existem:

- `hud_status.json`: painel VIDA/REIATSU/TRANSFORMAÇÃO + stamina, no alto à esquerda.
- `hud_info.json`: linhas de reiatsu, forma, SP e recargas no canto inferior esquerdo.
- `journal.json`, `form_wheel.json`, `character.json`: as telas do Diário, da roda de formas e do Personagem. As decisões estão em [telas-decisoes.md](telas-decisoes.md).

### Ver um layout sem abrir o jogo

```bash
python tools/gui/preview_layout.py journal --debug
```

Gera `build/gui-preview/layout_journal.png` usando a fonte real do Minecraft. Os textos e estados de exemplo ficam em `tools/gui/preview_samples/<id>.json`.

### Edição ao vivo

Com o jogo aberto pelo `Iniciar-Teste.bat`, **salve o JSON e a mudança aparece em menos de 1 segundo**. O mesmo vale para os PNGs em `src/main/resources/.../textures/gui`: rode o `build_assets.py` e a textura nova entra sozinha. Fora do ambiente de teste, F3+T recarrega.

Se o JSON tiver um erro, o jogo **não fecha**. Ele continua mostrando a última versão boa e escreve o erro em vermelho no canto da tela, dizendo o elemento e o campo (ex.: `vida: falta "size"`).

### Coordenadas: pixels da arte

Todas as posições e tamanhos estão em **pixels da arte**, os mesmos que você vê ao passar o mouse sobre o PNG em qualquer editor (Paint, Aseprite, Photopea…). Para posicionar algo, abra o PNG, ache o pixel e copie o número.

`art_density` diz quantos pixels da arte cabem em uma unidade de GUI do Minecraft. O painel tem `art_density: 2`: os 480 px da textura ocupam 240 unidades na tela, e com GUI Scale 2 cada pixel da arte vira exatamente 1 pixel do monitor.

### Depuração visual

Coloque `"debug": true` no layout e salve. Aparece um contorno em cada elemento com o `id` e a posição, mais o contorno rosa do layout inteiro. Volte para `false` quando terminar.

### Campos do layout

| Campo | Exemplo | Significado |
|---|---|---|
| `anchor` | `"top_left"` | Ponto da **tela** onde o layout se prende: `top_left`, `top_center`, `top_right`, `center_left`, `center`, `center_right`, `bottom_left`, `bottom_center`, `bottom_right`. |
| `pivot` | `"bottom_right"` | Ponto do **layout** que encosta no anchor (padrão: igual ao anchor). |
| `offset` | `[8, 6]` | Deslocamento em unidades de GUI depois de ancorar. |
| `size` | `[480, 118]` | Tamanho do layout em pixels da arte (usado para ancorar e reduzir). |
| `art_density` | `2` | Pixels da arte por unidade de GUI (padrão 1). |
| `scale` | `1` | Escala extra (padrão 1). |
| `max_width_fraction` | `0.6` | Se o layout passar dessa fração da largura da tela, ele **encolhe**. Nunca cresce além do tamanho nativo, para não borrar. |
| `max_height_fraction` | `0.98` | O mesmo, para a altura. |
| `responsive` | ver `hud_info.json` | Troca `anchor`/`pivot`/`offset` quando a tela tem até `max_screen_width` unidades de largura. |
| `debug` | `true` | Contornos de depuração. |
| `elements` | `[...]` | Os elementos, desenhados na ordem da lista (o primeiro fica por baixo). |

### Elementos

Todos aceitam `id` (nome que aparece no debug e nas mensagens de erro), `pos` (`[x, y]`) e `visible_if` (nome de um flag; `"!flag"` inverte).

**`repeat`** vale para qualquer elemento:
- `"repeat": {"count": 5, "step": [0, 49], "start": 0}` cria 5 cópias, cada uma `step` pixels depois da anterior.
- Em todos os textos do elemento, `{i}` vira o número da cópia (`start`, `start+1`, ...). Exemplo: `"id": "row{i}"`, `"text": "{row{i}_name}"`.

**`image`**: uma textura.
```json
{ "id": "panel", "type": "image", "texture": "hud/status_panel.png", "pos": [0, 0] }
```
- `texture`: caminho dentro de `textures/gui/` (ou um id completo `mod:caminho`).
- `size`: opcional; o padrão é o tamanho do PNG.
- `uv`: `[u, v, largura, altura]` para recortar um pedaço de uma folha de sprites.
- `slice`: `[esquerda, topo, direita, baixo]` para **nine-slice**. Os cantos ficam fixos e o meio estica, o que serve para caixas que mudam de tamanho.
- `states` + `state_if`: para texturas com várias variações lado a lado.
  - `"states": ["pending", "done", "failed"]` dá nome às células, da esquerda para a direita.
  - `"state_if": {"done": "obj0_done"}` liga um estado quando o flag é verdadeiro. O primeiro que bater vence; se nenhum bater, usa `normal` ou a 1ª célula.
- `cells` + `cell`: atalho para escolher uma célula fixa de uma folha (ex.: o ícone do atributo usa `"cells": 7, "cell": "{i}"`).
- `tint`: `"#B3FFFFFF"` multiplica cor e transparência (ex.: deixar uma decoração mais apagada).
- `slide_value` + `slide_range`: desce a imagem `valor × range` pixels (é a alça da barra de rolagem).

**`bar`**: barra que enche.
```json
{ "id": "health", "type": "bar", "pos": [143, 31], "size": [319, 13], "skew": 12, "skew_end": 13,
  "texture": "hud/status_fill_health.png" }
```
- `value`: qual valor mostrar (padrão: o próprio `id`).
- `texture`: fill desenhado 1:1. O PNG deve ter o mesmo tamanho que `size`, e o teste de regressão confere isso. Sem textura, usa `color`.
- `skew`: para poços inclinados (paralelogramo), quantos pixels a linha de cima começa à direita da de baixo.
- `skew_end`: quantos pixels a linha de baixo termina antes da borda direita. Valores negativos inclinam para o outro lado.
- `direction`: `right` (padrão), `left`, `up`, `down`.
- `color`, `color_if`, `background`: cor do fill (tinge a textura, se houver), cores condicionais e fundo do poço.

**`text`**: texto com a fonte do Minecraft.
```json
{ "id": "health_pct", "type": "text", "text": "{health}%", "pos": [446, 38],
  "align": "right", "valign": "middle", "scale": 0.75 }
```
- `text`: texto com marcadores `{nome}`. Ou `translate` + `args` para usar uma chave do `lang/*.json`.
- `align`: `left` / `center` / `right`, em relação a `pos`. `valign`: `top` / `middle` / `bottom`.
- `scale`: tamanho relativo à fonte normal da GUI (1 = normal).
- `max_width`: largura máxima em pixels da arte; textos maiores encolhem para caber.
- `color`, `color_if`, `shadow` (padrão `true`).
- `font`: outra fonte (ex.: `"bleachmod:title"`). `bold`: negrito. `uppercase`: tudo em maiúsculas.
- `letter_spacing`: espaço extra entre letras, em pixels da arte. Negativo aproxima as letras, o que ajuda em fontes itálicas.
- `wrap`: `true` quebra o texto em várias linhas dentro de `max_width`. `line_height` (pixels da arte) e `max_lines` controlam as linhas.

**`button`**: textura clicável.
```json
{ "id": "start", "type": "button", "texture": "ui/button_wide.png", "pos": [58, 442],
  "states": ["normal", "hover", "pressed", "disabled"], "enabled_if": "can_start",
  "action": "start", "translate": "screen.bleachmod.journal.start" }
```
- A textura é uma folha de estados (veja `states` em `image`). O estado é escolhido nesta ordem:
  1. desabilitado: `disabled`, ou `locked` se não houver `disabled`;
  2. clicando: `pressed`;
  3. `state_if`;
  4. mouse em cima: `hover`;
  5. `normal`.
- `enabled_if`: flag que habilita o botão. Sem ele, o botão fica sempre habilitado.
- `action`: o texto que a tela recebe no clique (ex.: `"buy:{i}"`).
- Rótulo opcional com `text`/`translate`, com os mesmos campos de texto (`scale`, `color`, `font`...).
  - `label_pos` fixa o rótulo, alinhado à esquerda. Sem ele, o rótulo fica centralizado.
  - `disabled_color` é a cor do rótulo quando o botão está desabilitado.
- `tooltip`: texto (ou `{marcador}`) mostrado quando o mouse está em cima.

**`entity`**: o modelo 3D do jogador, com os pés no fundo da caixa definida por `pos`/`size`. Campos: `source` (`player`), `scale` e `yaw_value` (rotação vinda da tela).

**`rect`**: retângulo de cor sólida (`size`, `color`, `color_if`).

**Cores** são `"#RRGGBB"` ou `"#AARRGGBB"` (com transparência). `color_if` é `{ "flag": "#cor" }`, e a primeira condição verdadeira vence.

### Valores disponíveis no HUD de status

Fornecidos por `ReiatsuHud`:

| Tipo | Nomes |
|---|---|
| Barras (0–1; como texto viram 0–100) | `health`, `reiatsu`, `transform`, `stamina` |
| Textos | `stamina_now`, `stamina_max`, `reiatsu_now`, `reiatsu_max`, `form`, `tp`, `cooldowns` |
| Flags | `stamina_exhausted`, `in_combat` |

Para expor um valor novo, adicione uma linha `.value(...)`, `.text(...)` ou `.flag(...)` em `ReiatsuHud.renderPanel`. A partir daí ele pode ser usado em qualquer layout.

### Criar um layout novo (outra tela/overlay)

1. Crie `gui_layouts/meu_layout.json`.
2. No código do overlay:
   ```java
   LayoutContext ctx = new LayoutContext().value("algo", ratio).text("nome", texto);
   LayoutRenderer.render(graphics, mc.font, "meu_layout", ctx, screenWidth, screenHeight);
   ```

3. Para uma **tela** com botões, herde de `LayoutScreen` e implemente:
   - `context()`: os valores de cada quadro;
   - `onAction(String)`: o que fazer no clique.

   Exemplos: `JournalScreen`, `FormWheelScreen` e `StatusScreen`.

Código: `com.bleachmod.client.gui.layout` (`GuiLayoutParser`, `LayoutRenderer`, `LayoutGeometry`, `GuiLayouts`, `GuiTextures`, `LayoutScreen`, `LayoutInteraction`).
