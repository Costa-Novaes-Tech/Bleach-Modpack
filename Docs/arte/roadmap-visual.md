# Roadmap visual: o que dá para melhorar e quais artes criar

Inventário de toda a parte visual do mod (outubro/2026), com o estado atual, a ideia de melhoria e a lista de artes para cada item.
O fluxo para cada arte está em [pipeline-gui.md](pipeline-gui.md).

## Como ler as medidas

- Todas as medidas estão em **pixels da arte, já convertida**, com `art_density: 2` (o mesmo padrão do painel de status). Uma tela de 1920×1080 com GUI Scale 2 tem 960×540 unidades, ou seja, cabe uma arte de até ~1920×1080 px, mas uma janela típica fica em torno de **640×420 px**.
- Ao pedir a arte à IA, informe a **proporção** (ex.: "retangular 3:2"). O tamanho final quem define é o conversor.
- **Estados** de botão: `normal`, `hover` (mouse em cima), `pressed` (clicado) e `disabled` (bloqueado). Dá para pedir os 4 numa única imagem, lado a lado.
- Prioridade: **P1** = maior impacto visual, fazer primeiro · **P2** = importante · **P3** = polimento.

---

## 0. Kit de interface (base para todas as telas) · P1

Antes das telas, vale criar **um kit de peças reutilizáveis**. Com ele, cada tela nova vira montagem no JSON em vez de arte nova do zero, e tudo fica com a mesma identidade visual.

| Peça | Variações | Tamanho sugerido | Observação |
|---|---|---|---|
| Moldura de janela (nine-slice) | 1 | 96×96 (cantos de 24) | Estica para qualquer tamanho de tela. |
| Cabeçalho de janela | 1 | 320×40 | Faixa do título, com ornamento estilo Bleach. |
| Botão largo | normal / hover / pressed / disabled | 160×36 cada (nine-slice) | Texto vem do jogo, não da arte. |
| Botão quadrado / ícone | 4 estados | 36×36 | Para "+", "<", ">", "X". |
| Aba (tab) | ativa / inativa / hover | 120×32 | Para telas com seções (Status / Técnicas / Zanpakutō). |
| Barra de rolagem | trilho + alça (normal/hover) | 12×64 + 12×24 | Para listas de quests e diário. |
| Caixa de tooltip (nine-slice) | 1 | 48×48 | Substitui o tooltip roxo do Minecraft. |
| Divisória horizontal | 1 | 256×6 | Separador de seções. |
| Moldura de slot/retrato | normal / selecionado | 48×48 | Ícones de técnica, quest, item. |
| Campo de texto | normal / foco | nine-slice 48×24 | Se houver busca ou nome. |

> **Trabalho de código necessário:** o sistema de layout precisa de um elemento `button` (com os 4 estados, som e ação), `scroll_list` e `tooltip`. Isso estende o que já existe e serve de base para a etapa 3 (editor visual).

---

## 1. HUD em jogo

### 1.1 Painel de status (VIDA / REIATSU / TRANSFORMAÇÃO) · ✅ feito
Já está no sistema novo. Polimentos possíveis (P3):
- Animação de "dano recente": um trecho branco que some atrás do fill quando a vida cai.
- Pulso no fill de Reiatsu quando estiver cheio.
- Preencher o **círculo à esquerda** com o ícone da forma atual (selada / Shikai / Bankai). Artes: 3 ícones de 56×56.

### 1.2 Bloco de informações (canto inferior esquerdo) · P2
Hoje são 4 linhas de texto puro. Ideia: um mini-painel com ícone por linha.
- Arte: fundo do mini-painel (nine-slice 64×64) e ícones 14×14 de **Reiatsu**, **Forma/Alvo**, **Spirit Points** e **Recargas**. Já existem `icon_reiatsu.png` e `icon_tp.png` (16×16, sem uso).

### 1.3 Barra de técnicas (slots 1–4) · P1
Hoje as recargas aparecem como texto ("1:OK 2:OK"). Ideia: 4 slots com o ícone da técnica, a tecla e a recarga desenhada como um "relógio" escurecendo o ícone.
- Moldura de slot: normal / pronto (brilho) / em recarga / sem reiatsu, 40×40 cada.
- Ícone de cada técnica de cada Zanpakutō (Ryūjin Jakka, Hyōrinmaru, Senbonzakura, Benihime, Nozarashi…): 32×32 cada.
- Fundo da barra: 200×48.

### 1.4 Stamina · P2
Hoje é um retângulo verde chapado. Arte: moldura fina 240×10 + fill 236×6 (normal) e fill "exausto" (laranja/vermelho).

### 1.5 Indicador "Em combate" · P3
Ícone de espadas cruzadas 24×24 com brilho pulsante, no lugar do texto.

### 1.6 Guarda / postura · P2
O escudo hoje é desenhado em código, com retângulos. Artes: escudo 48×48 em 3 estados (cheio / rachado / quebrado) + fill de postura.

### 1.7 Números de dano · P2
Hoje aparece "DMG : 12" no centro da tela. Ideia: números estilizados que saltam e somem.
- Folha de dígitos 0–9 + "!" + "CRÍTICO": cada dígito 12×16, em 2 cores (normal / crítico).

### 1.8 Quest acompanhada (canto) · P2
Hoje é um retângulo escuro com texto. Já existe `quest_panel.png` (2156×655, sem uso), que pode ser convertido.
- Artes: painel (nine-slice), ícones de objetivo (matar / coletar / falar / ir até). `icon_objective_kill`, `icon_objective_item` e `icon_quest_track` já existem e só precisam de conversão.

### 1.9 Guia da Zanpakutō (painel direito) · P2
Hoje é um painel azul chapado. Artes: moldura vertical (nine-slice), **retrato de cada Zanpakutō** 64×64 (Asauchi, Ryūjin Jakka, Hyōrinmaru, Senbonzakura, Benihime, Nozarashi) e selos de forma "bloqueada / disponível".

### 1.10 Alvo / inimigo · P3
Ideia nova: barra de vida do inimigo mirado no topo da tela, com nome e nível.
- Arte: barra 320×28 + fill. Para chefes, uma versão maior com moldura ornamentada.

### 1.11 Efeitos de tela · P3
- Vinheta vermelha de **vida baixa** (overlay da tela inteira, com transparência).
- Flash/vinheta de **liberação de Shikai/Bankai** (uma por cor de Zanpakutō).
- Distorção de **pressão espiritual** perto de inimigos fortes.

---

## 2. Telas (menus)

### 2.1 Status / atributos (tecla K) · P1
Hoje: fundo chapado, botões padrão do Minecraft e 7 atributos em texto.

| Arte | Variações | Tamanho |
|---|---|---|
| Fundo da tela | **com** área do personagem / **sem** personagem | 640×420 |
| Cabeçalho (nome, SP, BP) | 1 | 640×48 |
| Linha de atributo | normal / hover | 280×32 |
| Ícone de cada atributo | Zanjutsu, Hakuda, Vitalidade, Resistência, Kidō, Reserva, Controle | 24×24 cada (7) |
| Botão "+" (comprar) | normal / hover / pressed / disabled | 24×24 |
| Medidor de nível do atributo | fundo + fill | 120×8 |
| Moldura do personagem | 1 | 160×240 |
| Pedestal / aura atrás do personagem | 1 | 160×80 |
| Card de maestria de forma | bloqueado / em progresso / liberado | 200×40 |

**Código:** mostrar o modelo do personagem girando (o editor de aparência já faz isso), mais tooltips com o efeito de cada atributo e uma animação no "+".

### 2.2 Diário de quests (tecla J) · P1
Hoje: retângulos chapados + botões padrão. Já existem `journal_bg.png`, `journal_header.png` e ícones de status (sem uso).
- Artes: livro/pergaminho aberto (2 páginas) 640×400, marcador de página, linha de quest (normal / selecionada / concluída), ícones de status (não iniciada / aceita / sucesso / falha) 16×16, carimbo "CONCLUÍDA", botões Iniciar / Resgatar / Acompanhar (4 estados).

### 2.3 Diálogo de NPC / quests do NPC · P2
- Artes: caixa de diálogo inferior (estilo visual novel) 640×140, moldura do retrato do NPC 96×96, retrato de cada NPC importante, botões de resposta (4 estados).

### 2.4 Roda de formas (radial) · P1
Hoje são botões padrão. Já existem ícones grandes `sealed` / `shikai` / `bankai` (sem uso).
- Artes: círculo da roda 256×256, fatia (normal / hover / bloqueada), ícone central, ícones das formas convertidos para 48×48.

### 2.5 Escolha de raça · P2
Já usa `confirm_bg.png` e `race_shinigami.png` (grandes, ainda não convertidos).
- Artes: converter os dois, card de raça (normal / hover / selecionado) e cards para raças futuras (Quincy, Hollow/Arrancar, Fullbring) com versão "bloqueada".

### 2.6 Editor de aparência · P2
Hoje usa botões padrão "<" ">".
- Artes: fundo com palco para o modelo 640×420, setas (4 estados), abas de categoria (cabelo, olhos, roupa…), paleta de cores (moldura de amostra normal / selecionada).

### 2.7 Toasts (avisos de história) · P2
Já existem `toast_bg.png` e ícones start/objective/complete/fail/claim (sem uso).
- Basta converter as artes e ligar no sistema de layout.

### 2.8 Telas de sistema · P3
- **Menu principal**: logo do modpack, panorama (Seireitei / Karakura), botões no kit.
- **Tela de morte**: "Sua alma se dispersou" com fundo próprio.
- **Tela de carregamento** do mundo.

---

## 3. Ordem sugerida

1. **Kit de interface** (seção 0) + elemento `button` no sistema de layout.
2. **Status/atributos** (2.1): é a tela onde o kit mais aparece.
3. **Barra de técnicas** (1.3): maior ganho visual no combate.
4. **Roda de formas** (2.4) e **Diário** (2.2): já têm arte parcial.
5. **Converter as artes que já existem sem uso**: toasts, quest panel, ícones de objetivo, journal (ganho rápido).
6. Etapa 3: **editor visual** para posicionar tudo isso arrastando.
7. Polimentos: efeitos de tela, números de dano, alvo/inimigo, telas de sistema.

## Artes que já existem e estão sem uso

`quest_panel.png`, `toast_bg.png` + 5 ícones de toast, `icon_objective_kill/item.png`, `icon_quest_track.png`, `sealed/shikai/bankai.png`, `journal_bg.png`, `journal_header.png`, `status_*.png`, `icon_reiatsu.png`, `icon_tp.png`, `reiatsu_frame/fill.png`, `charge_frame/fill.png`.
As que forem gigantes (feitas pela IA) precisam passar pelo conversor. As de 16×16 / 128×16 / 320×200 já estão em tamanho nativo.
