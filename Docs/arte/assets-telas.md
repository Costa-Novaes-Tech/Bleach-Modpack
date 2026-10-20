# Assets das telas: Diário, Transformação e Personagem

Lista de cada peça de arte necessária para montar as três telas dos mockups, gerada a partir deles.

> **Status:** as três telas foram montadas. O que mudou em relação a esta lista (peças descartadas, unidas ou sem variação) está em [telas-decisoes.md](telas-decisoes.md).
Para converter e posicionar, siga o [Pipeline de GUI](pipeline-gui.md).

## Regras gerais (valem para todas as peças)

1. **Sem texto na arte.** Todo texto (títulos, nomes de quest, números, "100 SP", rótulos de botão) é desenhado pelo jogo. Assim ele muda conforme o idioma e os dados. A exceção opcional é a fonte de títulos (ver K-15).
2. **Sem personagem na arte.** O jogador da tela Personagem é o modelo 3D real, desenhado pelo jogo.
3. **Uma peça por imagem**, com **fundo transparente**. Se a peça tiver estados (normal, hover…), peça todos **na mesma imagem, lado a lado**, com o mesmo tamanho e alinhamento. Assim o estilo não muda entre eles.
4. **Use o mockup como referência** no ChatGPT: "extraia só este elemento, mesmo estilo, fundo transparente, sem texto".
5. **Tamanhos** estão em pixels da arte **depois da conversão** (`art_density: 2`). A tela inteira fica com **880×500 px** (440×250 unidades de GUI), e isso cabe até em GUI Scale 4 num monitor 1080p. A IA pode gerar maior, porque o conversor reduz. Só mantenha a **proporção**.
6. **Estados:**
   - `normal`: parado.
   - `hover`: mouse em cima.
   - `pressed`: clicando.
   - `selected`: o item escolhido de uma lista.
   - `disabled`: não pode usar (ex.: Claim sem quest completa).
   - `locked`: ainda não desbloqueado.
7. Marcado com 🤖 = **pode ser gerado pelo script** a partir de outra variação (ex.: versão cinza/bloqueada = colorida dessaturada e escurecida). Não precisa pedir à IA, só se quiser um resultado melhor.

Legenda de tipo:
- **fixa**: desenhada no tamanho exato.
- **9-slice**: os cantos ficam fixos e o meio estica. A arte precisa ter um miolo "liso" que aguente esticar.
- **adesivo**: decoração solta, posicionada por cima de outra peça.

---

## K. Kit compartilhado (as 3 telas usam)

| # | Arquivo sugerido | Peça | Tipo | Tamanho | Estados / variações |
|---|---|---|---|---|---|
| K-1 | `ui/window_frame.png` | Moldura azul da janela, com as faixas vermelhas nos cantos superiores, **sem** cabeçalho e **sem** chamas | 9-slice | 160×120 (cantos 40) | 1 |
| K-2 | `ui/window_fill.png` | Fundo escuro interno da janela (azul-marinho com textura sutil) | 9-slice / repetível | 64×64 | 1 |
| K-3 | `ui/header_plaque.png` | Placa do título: as duas esferas pretas + chamas azuis + faixa vermelha central, **vazia** | fixa | 420×64 | 1 |
| K-4 | `ui/corner_flame_left.png` / `_right.png` | Chamas azuis dos cantos inferiores | adesivo | 56×90 cada | 1 (a direita pode ser a esquerda espelhada 🤖) |
| K-5 | `ui/parchment.png` | Pergaminho **liso** (papel envelhecido + borda rasgada), sem desenhos | 9-slice | 128×128 (cantos 24) | 1 |
| K-6 | `ui/parchment_mask.png` | Desenho da máscara Hollow em tinta (canto superior direito do pergaminho) | adesivo | 120×150 | 1 |
| K-7 | `ui/parchment_castle.png` | Castelo/torres + nuvens em tinta (canto inferior direito) | adesivo | 110×110 | 1 |
| K-8 | `ui/parchment_splash.png` | Respingos vermelhos de tinta | adesivo | 48×48 | 2–3 variações |
| K-9 | `ui/title_brush.png` | Pincelada vermelha/preta atrás do título no pergaminho | fixa | 220×44 | 1 |
| K-10 | `ui/section_header.png` | Faixa de seção: círculo com chama azul + pincelada escura que some à direita (OBJECTIVES, REWARDS, ZANPAKUTO, PROGRESS…) | fixa | 264×28 | 1 |
| K-11 | `ui/button_wide.png` | Botão largo do rodapé (Start / Claim / Track / Character) | 9-slice | 180×44 | **normal, hover, pressed, disabled** |
| K-12 | `ui/footer_bar.png` | Barra do rodapé com as pontas vermelhas (texto de dica do Transformação / Personagem) | 9-slice | 770×48 | 1 |
| K-13 | `ui/scene_night.png` | Fundo noturno: lua crescente, torii, telhados, nuvens azuis. Usado atrás da roda, no quadro do personagem e no painel de informações | fixa | 480×280 | 1 |
| K-14 | `ui/icon_flame.png` | Chama azul pequena (ícone genérico usado em listas, seções e recompensas) | fixa | 24×24 | 1 |
| K-15 | `font/bleach_title.png` | *(Opcional)* Fonte de títulos (letras amarelas itálicas com contorno). Permite títulos como "MISSION JOURNAL" em qualquer idioma | folha de caracteres | A–Z, 0–9, acentos | 1 |

> **K-15:** sem essa fonte, os títulos usam a fonte do Minecraft (mais simples). A alternativa é colocar o título na arte, mas aí precisa de **uma versão por idioma** (pt_br e en_us).

---

## J. Diário de Missões (tecla J)

Usa: K-1, K-2, K-3, K-4, K-5, K-6, K-7, K-8, K-9, K-10, K-11 (Start, Claim, Track, Character).

| # | Arquivo | Peça | Tipo | Tamanho | Estados / variações |
|---|---|---|---|---|---|
| J-1 | `journal/list_panel.png` | Painel escuro da lista, com a silhueta de torii/lua/nuvens embaixo | fixa | 324×340 | 1 |
| J-2 | `journal/quest_row.png` | Linha de quest (fundo com faixa vermelha à direita) | fixa | 282×46 | **normal, hover, selected** (vermelho), **completed** (pronta para resgatar), **locked** 🤖 |
| J-3 | `journal/quest_icon.png` | Ícone circular à esquerda da linha | fixa | 28×28 | por status: **não iniciada, aceita, concluída, falhou**. Ou por tipo: história / secundária / diária |
| J-4 | `journal/scroll_track.png` | Trilho da barra de rolagem | 9-slice | 16×64 | 1 |
| J-5 | `journal/scroll_thumb.png` | Alça da barra de rolagem | 9-slice | 16×32 | normal, hover, pressed |
| J-6 | `journal/scroll_arrow.png` | Setas ▲ ▼ da rolagem | fixa | 16×16 | normal, hover, pressed, disabled (a ▼ pode ser a ▲ espelhada 🤖) |
| J-7 | `journal/page_button.png` | Botões « e » de página | fixa | 48×28 | normal, hover, pressed, disabled (» = « espelhado 🤖) |
| J-8 | `journal/objective_check.png` | Marcador de objetivo (círculo) | fixa | 14×14 | **pendente, concluído, falhou** |
| J-9 | `journal/reward_slot.png` | Moldura quadrada da recompensa | fixa | 36×36 | 1 |
| J-10 | `journal/reward_icons.png` | Ícones de recompensa: spirit points, reiatsu, maestria, item (o item usa o ícone do próprio Minecraft) | fixa | 24×24 cada | 1 por tipo |
| J-11 | `journal/stamp_completed.png` | *(Opcional)* Carimbo "CONCLUÍDA" sobre o pergaminho | adesivo | 96×48 | pt_br / en_us (tem texto, então é uma versão por idioma) |

**Texto desenhado pelo jogo:** título "MISSION JOURNAL", nomes das quests, título da quest no pergaminho, descrição, rótulos "OBJECTIVES" e "REWARDS", objetivos com contador `[0/6]`, recompensas e rótulos dos botões.

> No mockup aparecia `entity.bleachmod.hollow`: faltava a tradução da entidade Hollow no `lang`. Já corrigido.

---

## T. Selecionar Transformação (roda de formas)

Usa: K-1, K-2, K-3, K-4, K-5, K-6, K-7, K-8, K-9, K-10, K-12, K-13.

| # | Arquivo | Peça | Tipo | Tamanho | Estados / variações |
|---|---|---|---|---|---|
| T-1 | `forms/wheel_ring.png` | Anel da roda: círculo duplo azul/vermelho com os raios e conectores, **sem** medalhões e **sem** centro | fixa | 320×320 | 1 |
| T-2 | `forms/wheel_core.png` | Emblema central (tomoe azul) | fixa | 64×64 | 1 (o jogo pode girá-lo devagar) |
| T-3 | `forms/medal_sealed.png` | Medalhão Selada (chamas azuis + símbolo) | fixa | 148×148 | **normal, hover, active** (forma atual, mais brilho), **locked** 🤖 |
| T-4 | `forms/medal_shikai.png` | Medalhão Shikai (chamas vermelhas + katana) | fixa | 148×148 | normal, hover, active, locked 🤖 |
| T-5 | `forms/medal_bankai.png` | Medalhão Bankai **colorido** (o mockup só mostra o bloqueado; precisa da versão liberada, sugestão: dourado/roxo) | fixa | 148×148 | normal, hover, active, locked 🤖 |
| T-6 | `forms/lock.png` | Cadeado | adesivo | 24×28 | 1 |
| T-7 | `forms/name_plate.png` | Plaquinha do nome sob o medalhão | fixa | 136×32 | **normal** (azul), **shikai** (vermelha), **bankai**, **locked** (cinza) 🤖 |
| T-8 | `forms/progress_bar.png` | Moldura da barra de progresso de maestria | 9-slice | 196×16 | 1 |
| T-9 | `forms/progress_fill.png` | Preenchimento da barra | fixa (tamanho do poço) | ~188×8 | 1 por forma (azul / vermelho / bankai) |

**Texto desenhado pelo jogo:** título, nomes das formas, "Zanpakuto Guide", rótulos das seções, nome da Zanpakutō, forma atual, `0 / 100`, dica de treino, "Next unlock" e as duas linhas do rodapé.

**Observação de design:** cada Zanpakutō tem cores próprias (Ryūjin Jakka = fogo, Hyōrinmaru = gelo, Senbonzakura = rosa, Benihime = vermelho, Nozarashi = laranja/caos). Se quiser medalhões por Zanpakutō, multiplique T-4 e T-5 por arma. Sugestão: comece com os genéricos e depois faça os de cada Zanpakutō.

---

## C. Personagem / Atributos (tecla K)

Usa: K-1, K-2, K-3, K-4, K-12, K-13, K-14.

| # | Arquivo | Peça | Tipo | Tamanho | Estados / variações |
|---|---|---|---|---|---|
| C-1 | `character/portrait_frame.png` | Moldura do retrato com as faixas vermelhas e os kanji 魂, com o **centro transparente** | fixa | 244×322 | 1 |
| C-2 | `character/portrait_bg.png` | Cena atrás do personagem (noite, torii, chamas azuis laterais), **sem personagem** | fixa | 220×300 | 1 |
| C-3 | `character/pedestal.png` | Círculo rúnico brilhante sob os pés | fixa | 160×48 | 1 (ou 4 quadros para animar o brilho) |
| C-4 | `character/foreground_flames.png` | *(Opcional)* Chamas na frente das pernas do personagem, para profundidade | adesivo | 220×120 | 1 |
| C-5 | `character/info_panel.png` | Painel de informações (topo direito) com a cena noturna, **sem** texto | fixa | 488×128 | 1 |
| C-6 | `character/divider_flame.png` | Divisória horizontal com a chaminha no meio | fixa | 480×12 | 1 |
| C-7 | `character/attribute_card.png` | Cartão do atributo: hexágono do ícone à esquerda + faixa vermelha | fixa | 240×40 | **normal, hover, maxed** (nível máximo, borda dourada) |
| C-8 | `character/attribute_icons.png` | Ícones dos 7 atributos: **Zanjutsu** (espadas cruzadas), **Hakuda** (punho), **Vitalidade** (coração), **Resistência** (escudo), **Kidō** (chama), **Reserva** (espiral), **Controle** (mira) | fixa | 26×26 cada | 7 ícones numa folha |
| C-9 | `character/attribute_bar.png` | Barra segmentada de nível do atributo (moldura) | fixa | 104×10 | 1 |
| C-10 | `character/attribute_bar_fill.png` | Preenchimento da barra | fixa | ~100×6 | 1 |
| C-11 | `character/buy_button.png` | Botão de compra ("100 SP" é desenhado pelo jogo) | fixa | 68×26 | **normal, hover, pressed, disabled** (sem SP), **maxed** 🤖 |
| C-12 | `character/sp_icon.png` | *(Opcional)* Ícone de SP para ficar ao lado do "100" | fixa | 10×10 | 1 |
| C-13 | `character/bp_icon.png` | *(Opcional)* Ícone de BP (Battle Power) no topo | fixa | 14×14 | 1 |

**Texto desenhado pelo jogo:** título, Spirit Points, BP, Active/Target, as linhas de maestria, nome e nível de cada atributo, custo e a dica do rodapé.

**Desenhado pelo jogo, não é arte:** o modelo do jogador (com skin e aparência), girando com o mouse, como no editor de aparência.

---

## Contagem

| Grupo | Peças | Imagens para pedir à IA (descontando 🤖 e espelhos) |
|---|---|---|
| Kit compartilhado | 15 | ~14 |
| Diário | 11 | ~9 |
| Transformação | 9 | ~8 |
| Personagem | 13 | ~11 |
| **Total** | **48** | **~42** |

### Ordem sugerida para gerar

1. **Kit** K-1, K-2, K-3, K-4, K-11, K-12. Com isso as 3 telas já ganham "cara" de jogo, mesmo antes do resto.
2. **Personagem:** C-1, C-2, C-7, C-8, C-11.
3. **Pergaminho:** K-5 a K-10.
4. **Diário:** J-1, J-2, J-3, J-7.
5. **Transformação:** T-1, T-3, T-4, T-5, T-7.
6. Opcionais e polimento.

### Modelo de pedido para o ChatGPT

> Using the attached mockup as style reference, draw **only** the [PEÇA] as a separate game UI asset. 16-bit pixel art, crisp hard pixel edges, no anti-aliasing, no blur, flat colors, **transparent background**, **no text or letters**. [Se tiver estados:] Draw [N] versions side by side in one image, same size and alignment: normal, hover (slightly brighter glow), pressed (darker, shifted 1px down), disabled (desaturated gray). Aspect ratio [L:A].
