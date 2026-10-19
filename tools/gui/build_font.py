#!/usr/bin/env python3
"""Gera uma fonte bitmap do Minecraft a partir de folhas de letras desenhadas pela IA.

    python tools/gui/build_font.py

Configuracao em tools/gui/fonts.json. Para cada folha:
  sheet   imagem em art-src/
  grid    tamanho do "pixel" da IA (a deteccao automatica confunde o espaco entre letras)
  rows    os caracteres de cada linha, na ordem em que aparecem

Saida: assets/<ns>/textures/font/<nome>.png + assets/<ns>/font/<nome>.json.
Cada texel da fonte vale 1 pixel de arte de uma tela com art_density 2, ou seja,
meia unidade de fonte: o "height" do provider e metade da altura da celula.
Caracteres que nao estao nas folhas (espaco, pontuacao) caem na fonte padrao.
"""
from __future__ import annotations

import json
import sys
from pathlib import Path

import numpy as np
from PIL import Image
from scipy import ndimage

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(Path(__file__).resolve().parent))
import pixelate  # noqa: E402


def runs(mask: np.ndarray) -> list[tuple[int, int]]:
    out, start = [], None
    for i, value in enumerate(mask):
        if value and start is None:
            start = i
        elif not value and start is not None:
            out.append((start, i))
            start = None
    if start is not None:
        out.append((start, len(mask)))
    return out


def glyphs_from_sheet(sheet: dict, sources: Path) -> list[tuple[str, Image.Image, int]]:
    """Retorna (caractere, imagem recortada, linha de base relativa ao topo do recorte)."""
    img, _ = pixelate.convert(Image.open(sources / sheet["sheet"]), grid=sheet["grid"])
    alpha = np.asarray(img)[..., 3] > 0
    # Linhas de texto: faixas separadas por pelo menos 1 linha vazia. Acentos podem
    # ficar separados da letra por 1 linha, entao juntamos faixas muito proximas.
    bands = runs(alpha.any(1))
    merged: list[list[int]] = []
    for top, bottom in bands:
        if merged and top - merged[-1][1] <= 2 and (bottom - top) < 6:
            merged[-1][1] = bottom
        elif merged and top - merged[-1][1] <= 2 and (merged[-1][1] - merged[-1][0]) < 6:
            merged[-1][1] = bottom
        else:
            merged.append([top, bottom])
    if len(merged) != len(sheet["rows"]):
        raise SystemExit(f"{sheet['sheet']}: {len(merged)} linhas encontradas, esperava {len(sheet['rows'])}: {merged}")

    result = []
    for (top, bottom), chars in zip(merged, sheet["rows"]):
        band = alpha[top:bottom]
        # Cada glifo e uma peca conectada; pecas pequenas (acentos, pingos) se juntam a
        # letra cujo centro horizontal estiver mais proximo.
        labels, count = ndimage.label(band, structure=np.ones((3, 3)))
        pieces = []
        for index, sl in enumerate(ndimage.find_objects(labels), start=1):
            area = int((labels[sl] == index).sum())
            pieces.append({"ids": [index], "x0": sl[1].start, "x1": sl[1].stop,
                           "y0": sl[0].start, "y1": sl[0].stop, "area": area})
        pieces.sort(key=lambda p: p["area"], reverse=True)
        letters = pieces[:len(chars)]
        for extra in pieces[len(chars):]:
            cx = (extra["x0"] + extra["x1"]) / 2
            target = min(letters, key=lambda p: abs((p["x0"] + p["x1"]) / 2 - cx))
            target["ids"] += extra["ids"]
            for k, f in (("x0", min), ("y0", min), ("x1", max), ("y1", max)):
                target[k] = f(target[k], extra[k])
        letters.sort(key=lambda p: p["x0"])
        if len(pieces) < len(chars):
            raise SystemExit(f"{sheet['sheet']} linha '{chars}': {len(pieces)} pecas, esperava {len(chars)}")
        cols = [(p["x0"], p["x1"]) for p in letters]
        masks = [np.isin(labels, p["ids"]) for p in letters]
        # Linha de base: fundo da maioria das letras (as com descendente ficam mais baixas).
        bottoms = []
        for mask in masks:
            bottoms.append(np.where(mask.any(1))[0].max())
        baseline = int(np.median(bottoms))
        rgba = np.asarray(img)[top:bottom]
        for (x0, x1), mask, ch in zip(cols, masks, chars):
            # Recorta so os pixels desta peca (letras italicas se sobrepoem na vertical).
            part = rgba[:, x0:x1].copy()
            part[~mask[:, x0:x1]] = 0
            result.append((ch, Image.fromarray(part, "RGBA"), baseline + 1))
    return result


def build(font: dict, sources: Path, assets: Path) -> None:
    glyphs = []
    for sheet in font["sheets"]:
        glyphs += glyphs_from_sheet(sheet, sources)
    above = max(base for _, _, base in glyphs)                    # maior altura acima da base
    below = max(g.height - base for _, g, base in glyphs)          # maior descendente
    # O Minecraft exige height/ascent inteiros; como cada unidade sao 2 texels, ambos pares.
    above += above % 2
    below += below % 2
    cell_h = above + below
    cell_w = max(g.width for _, g, _ in glyphs) + 1
    per_row = 16
    rows = [glyphs[i:i + per_row] for i in range(0, len(glyphs), per_row)]
    atlas = Image.new("RGBA", (cell_w * per_row, cell_h * len(rows)), (0, 0, 0, 0))
    chars = []
    for r, row in enumerate(rows):
        line = ""
        for c, (ch, glyph, base) in enumerate(row):
            atlas.alpha_composite(glyph, (c * cell_w, r * cell_h + above - base))
            line += ch
        # Completa a ultima linha com \0 (ignorado pelo Minecraft).
        chars.append(line + "\u0000" * (per_row - len(row)))

    name = font["name"]
    ns = font.get("namespace", "bleachmod")
    texture = assets / ns / "textures/font" / f"{name}.png"
    texture.parent.mkdir(parents=True, exist_ok=True)
    atlas.save(texture)
    definition = {"providers": [
        {"type": "bitmap", "file": f"{ns}:font/{name}.png",
         "height": cell_h // 2, "ascent": above // 2, "chars": chars},
        {"type": "reference", "id": "minecraft:include/space"},
        {"type": "reference", "id": "minecraft:include/default"},
    ]}
    out = assets / ns / "font" / f"{name}.json"
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps(definition, indent=2, ensure_ascii=True) + "\n", encoding="utf-8")
    preview = ROOT / "build/gui-preview" / f"font_{name}.preview.png"
    preview.parent.mkdir(parents=True, exist_ok=True)
    atlas.resize((atlas.width * 3, atlas.height * 3), Image.NEAREST).save(preview)
    print(f"font {ns}:{name}: {len(glyphs)} glifos, celula {cell_w}x{cell_h}, base {above}")


def main() -> int:
    config = json.loads((ROOT / "tools/gui/fonts.json").read_text(encoding="utf-8"))
    for font in config["fonts"]:
        build(font, ROOT / config["source_root"], ROOT / "src/main/resources/assets")
    return 0


if __name__ == "__main__":
    sys.exit(main())
