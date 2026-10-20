# Telas Diário, Transformação e Personagem: decisões

Registro das decisões tomadas ao montar as três telas a partir dos mockups e das artes
em `art-src/gui/hud/`. Cada item diz **o que foi decidido**, **por quê** e **onde mudar**.

- Lista original de peças: [assets-telas.md](assets-telas.md).
- Como o sistema funciona: [pipeline-gui.md](pipeline-gui.md).

## Arquivos

| Tela | Layout | Código (valores e ações) | Abrir no jogo |
|---|---|---|---|
| Diário de Missões | `gui_layouts/journal.json` | `client/gui/JournalScreen.java` | tecla J |
| Selecionar Transformação | `gui_layouts/form_wheel.json` | `client/gui/FormWheelScreen.java` | tecla da roda de formas |
| Personagem | `gui_layouts/character.json` | `client/gui/StatusScreen.java` | tecla K, ou botão "Character" no Diário |

As três herdam de `client/gui/layout/LayoutScreen.java`.

## Decisões de conteúdo (pedidas pelo Gabriel)

| # | Decisão | Onde |
|---|---|---|
| D1 | O ícone de quest é o mesmo `icon_flame` (não existe `quest_icon` próprio). | `journal.json` → `row{i}_icon` |
| D2 | Não há `reward_icons`. Toda recompensa usa a chama pequena (`icon_flame_small`) por enquanto. | `journal.json` → `rew{i}_icon` |
| D3 | A arte `quest_icon_variations.png` são os **marcadores de objetivo**: pendente, concluído, falhou. | `assets.json` → `journal/objective_marker.png` |
| D4 | O estado bloqueado do medalhão Bankai **já tem o cadeado** na arte. O cadeado solto (`lock.png`) só aparece sobre Selada e Shikai. | `FormWheelScreen` → flag `<forma>_show_lock` |
| D5 | Os medalhões já trazem o nome do estágio ("Sealed", "Shikai", "Bankai"). **`name_plate.png` não é usado** (continua em `art-src`). O nome específico da Zanpakutō aparece no guia. | — |
| D6 | C-2 e C-3 viraram **uma imagem só**: `portrait_bg` (cena + círculo rúnico). | `character.json` → `portrait_bg` |
| D7 | **C-4** (chamas na frente do personagem) não será usado. | — |
| D8 | O cartão de atributo (C-7) **não tem variações** por enquanto (sem estado de nível máximo). | `assets.json` → `character/attribute_card.png` |
| D9 | **Sem barra de nível de atributo** por enquanto (C-9 e C-10). O nível aparece no texto ("Zanjutsu  3"). | `StatusScreen` → `attr{i}_label` |
| D10 | **Sem ícones de SP e BP** (C-12 e C-13). | — |

## Decisões técnicas

### Tamanho e escala
- **Canvas comum:** 920×518 pixels de arte com `art_density: 2`, ou seja, 460×259 unidades de GUI. Corresponde ao mockup (1672×941) multiplicado por **0,55**. Cabe em GUI Scale 4 num monitor 1080p (480×270).
  - Em telas menores, `max_width_fraction` e `max_height_fraction` (0,98) encolhem o conjunto, que nunca cresce além do tamanho nativo.
- **Texto do corpo** usa escala 0,75–0,9 da fonte normal. Em GUI Scale 4 isso dá 3 px de tela por pixel de fonte, sem distorção.

### Artes que precisaram de tratamento (`tools/gui/assets.json`)
- **`window_fill_1`:** tinha o "xadrez de transparência" **desenhado** no lugar do fundo. `remove_checker` apaga esse xadrez, e só o miolo (`crop`) é usado. A moldura é a `window_frame`, por cima. **`window_fill_2` não é usado.**
- **`window_frame`:** é desenhada em **nine-slice** com `slice: [200, 40, 190, 40]`. Os cantos incluem os entalhes decorativos, e só o trecho liso entre eles estica.
- **`medal_sealed`:**
  - Tinha uma sombra escura **opaca** emendando os quatro medalhões. `isolate` mantém só a parte clara e um contorno fino.
  - As palavras "NORMAL/HOVER/ACTIVE/LOCKED" escritas na imagem ficam fora das caixas de recorte (`cells` manuais).
- **`scroll_arrow_variations`:** os rótulos escritos ficam fora do recorte. A linha de cima vira `scroll_up`, a de baixo `scroll_down`.
- **Folhas de variação** (`*_variations`, medalhões, ícones de atributo): cada variação é recortada, convertida no mesmo tamanho e enfileirada da esquerda para a direita. A ordem dos estados está no campo `note` de cada entrada.
- **Estados gerados por script** (`derive`), porque a arte só tinha o normal:
  - `button_wide`: hover, pressed e disabled;
  - `buy_button`: hover, pressed e disabled;
  - `footer_character`: hover e pressed.
- **Pergaminho, seção e pincelada:** gerados em **dois tamanhos exatos** (Diário e Guia), em vez de esticar a mesma imagem. Assim os pixels não ficam irregulares.
- **`corner_flame_left`:** é a da direita espelhada (`mirror`).

### Fonte dos títulos
- `bleach_title_1.png` (maiúsculas, números, acentos) e `bleach_title_2.png` (minúsculas, acentos) viram a fonte **`bleachmod:title`**.
  - Gerador: `python tools/gui/build_font.py`, configurado em `tools/gui/fonts.json`.
- Caracteres que não estão nas folhas (espaço, pontuação) caem na fonte padrão do Minecraft.
- As letras são itálicas e a caixa de cada uma inclui a inclinação, então os títulos usam `letter_spacing` negativo (-3 a -4) para não ficarem espaçados.

### Comportamento
- **Roda de formas:**
  - Clicar num medalhão disponível escolhe a forma-alvo e **fecha a tela**, como antes. O R transforma.
  - O estado "active" do medalhão é a **forma ativa no momento**.
  - Medalhões não selecionáveis mostram o estado bloqueado, com tooltip explicando o motivo.
- **Guia da Zanpakutō:** "Próximo desbloqueio" usa o mesmo cálculo do painel lateral do HUD (`ZanpakutoHud.nextUnlock`).
- **Rodapé do Personagem:**
  - É o **botão de comprar nível da Zanpakutō** (o texto "Discover the form…" era a mensagem desse botão).
  - Quando não dá para comprar, mantém a arte normal e só o texto fica cinza (estado `dim`), porque nesse caso ele funciona mais como aviso.
- **Personagem:** o jogador é o modelo 3D real. Ele gira ao **clicar e arrastar**, como no editor de aparência (`EntityPreview`).
- **Diário:**
  - Mostra 5 quests por vez. As setas ▲▼ e a roda do mouse andam 1 linha, e « » andam 5.
  - A alça da rolagem **só indica** a posição: não é arrastável.
  - Limites do pergaminho: descrição com até **4 linhas**, até **3 objetivos** e até **2 recompensas**. Mais que isso não aparece, e deve ser revisto se surgir uma quest maior.
  - O carimbo CONCLUÍDA/COMPLETED aparece quando a quest está concluída, na versão do idioma do jogo.

## Como ajustar

1. Abra o jogo pelo `Iniciar-Teste.bat`, depois a tela.
2. Edite o JSON da tela e salve. A mudança aparece na hora.
3. Use `"debug": true` para ver o contorno e a posição de cada elemento.
4. Sem abrir o jogo: `python tools/gui/preview_layout.py journal --debug` gera `build/gui-preview/layout_journal.png`, com os valores de exemplo de `tools/gui/preview_samples/journal.json`.
