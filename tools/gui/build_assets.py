#!/usr/bin/env python3
"""Regenera todas as texturas de GUI a partir das artes originais em art-src/.

    python tools/gui/build_assets.py            # converte tudo
    python tools/gui/build_assets.py journal    # so entradas cujo caminho contem "journal"

Cada entrada de tools/gui/assets.json descreve uma textura. Campos (todos opcionais
exceto source/output), aplicados nesta ordem:

  crop            [x0,y0,x1,y1] recorte da original
  remove_checker  true: apaga o "xadrez de transparencia" desenhado pela IA
  isolate         {"min_luma":45,"close":8,"grow":3}: separa a arte de um brilho/sombra opaco
  autocrop        true: recorta as margens transparentes antes de converter
  cells           folha de variacoes (estados):
                    "auto": N        detecta as N maiores pecas, em ordem de leitura
                    [[x0,y0,x1,y1]]  caixas manuais
                  cada variacao e convertida para o mesmo tamanho e empacotada lado a lado
  cell_width / cell_height   tamanho final de cada variacao (um deles basta)
  grid, width, height, colors, alpha_cut, soft_alpha, trim   -> pixelate.convert
  derive          ["hover","pressed","disabled"]: estados gerados a partir da 1a variacao
  mirror          true: espelha horizontalmente o resultado final
  note            comentario livre

As previas ampliadas vao para build/gui-preview/, fora do mod.
"""
from __future__ import annotations

import json
import sys
from pathlib import Path

import numpy as np
from PIL import Image, ImageEnhance, ImageOps
from scipy import ndimage

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(Path(__file__).resolve().parent))
import pixelate  # noqa: E402

CONVERT_KEYS = {"grid", "grid_y", "width", "height", "colors", "alpha_cut", "soft_alpha", "trim"}


def remove_checker(img: Image.Image) -> Image.Image:
    """Torna transparente o xadrez branco/cinza ligado as bordas da imagem."""
    a = np.asarray(img.convert("RGBA")).copy()
    rgb = a[..., :3].astype(int)
    light = (rgb.min(2) > 170) & (rgb.max(2) - rgb.min(2) < 18)
    labels, _ = ndimage.label(light)
    border = set(np.unique(np.concatenate([labels[0], labels[-1], labels[:, 0], labels[:, -1]]))) - {0}
    mask = np.isin(labels, list(border))
    a[mask, 3] = 0
    return Image.fromarray(a, "RGBA")


def isolate(img: Image.Image, min_luma: int = 45, close: int = 8, grow: int = 3) -> Image.Image:
    """Mantem so a regiao clara/saturada (a arte) e um contorno fino; apaga sombras opacas."""
    a = np.asarray(img.convert("RGBA")).copy()
    luma = a[..., :3].max(2)
    core = (luma >= min_luma) & (a[..., 3] > 128)
    core = ndimage.binary_closing(core, iterations=close)
    core = ndimage.binary_fill_holes(core)
    core = ndimage.binary_dilation(core, iterations=grow)
    a[~core, 3] = 0
    return Image.fromarray(a, "RGBA")


def auto_cells(img: Image.Image, count: int) -> list[tuple[int, int, int, int]]:
    """As N maiores pecas opacas, em ordem de leitura, todas com a caixa do mesmo tamanho."""
    a = np.asarray(img.convert("RGBA"))
    mask = ndimage.binary_dilation(a[..., 3] > 140, iterations=6)
    labels, _ = ndimage.label(mask)
    boxes = []
    for sl in ndimage.find_objects(labels):
        y0, y1, x0, x1 = sl[0].start, sl[0].stop, sl[1].start, sl[1].stop
        boxes.append((x0, y0, x1, y1))
    boxes.sort(key=lambda b: (b[2] - b[0]) * (b[3] - b[1]), reverse=True)
    boxes = boxes[:count]
    if len(boxes) < count:
        raise SystemExit(f"esperava {count} variacoes, achei {len(boxes)}")
    row_h = max(b[3] - b[1] for b in boxes) / 2
    boxes.sort(key=lambda b: (round(((b[1] + b[3]) / 2) / row_h), b[0]))
    w = max(b[2] - b[0] for b in boxes)
    h = max(b[3] - b[1] for b in boxes)
    out = []
    for x0, y0, x1, y1 in boxes:
        cx, cy = (x0 + x1) // 2, (y0 + y1) // 2
        out.append((cx - w // 2, cy - h // 2, cx - w // 2 + w, cy - h // 2 + h))
    return out


def trim_cells(img: Image.Image, boxes: list) -> list:
    """Encolhe todas as caixas para a uniao do conteudo delas (mesmo deslocamento em todas)."""
    union = None
    for x0, y0, x1, y1 in boxes:
        bbox = img.crop((x0, y0, x1, y1)).getchannel("A").point(lambda v: 255 if v > 20 else 0).getbbox()
        if bbox:
            union = bbox if union is None else (min(union[0], bbox[0]), min(union[1], bbox[1]),
                                                max(union[2], bbox[2]), max(union[3], bbox[3]))
    if union is None:
        return boxes
    return [(x0 + union[0], y0 + union[1], x0 + union[2], y0 + union[3]) for x0, y0, _, _ in boxes]


def derive(base: Image.Image, kind: str) -> Image.Image:
    rgba = base.convert("RGBA")
    alpha = rgba.getchannel("A")
    rgb = rgba.convert("RGB")
    if kind == "hover":
        rgb = ImageEnhance.Brightness(rgb).enhance(1.22)
        rgb = ImageEnhance.Color(rgb).enhance(1.15)
    elif kind == "pressed":
        rgb = ImageEnhance.Brightness(rgb).enhance(0.78)
        out = Image.new("RGBA", rgba.size, (0, 0, 0, 0))
        moved = rgb.copy()
        moved.putalpha(alpha)
        out.alpha_composite(moved, (0, 1))
        return out
    elif kind in ("disabled", "locked", "gray"):
        gray = ImageOps.grayscale(rgb)
        rgb = ImageEnhance.Brightness(gray.convert("RGB")).enhance(0.7)
    else:
        raise SystemExit(f"derive desconhecido: {kind}")
    rgb.putalpha(alpha)
    return rgb


def build(entry: dict, sources: Path, textures: Path) -> Image.Image:
    img = Image.open(sources / entry["source"]).convert("RGBA")
    if "crop" in entry:
        img = img.crop(tuple(entry["crop"]))
    if entry.get("remove_checker"):
        img = remove_checker(img)
    if "isolate" in entry:
        img = isolate(img, **entry["isolate"])
    if entry.get("autocrop"):
        box = img.getchannel("A").point(lambda v: 255 if v > 20 else 0).getbbox()
        if box:
            img = img.crop(box)
    opts = {k: v for k, v in entry.items() if k in CONVERT_KEYS}

    cells = entry.get("cells")
    if cells is None:
        result, _ = pixelate.convert(img, **opts)
    else:
        boxes = auto_cells(img, cells["auto"]) if isinstance(cells, dict) else [tuple(c) for c in cells]
        boxes = trim_cells(img, boxes)
        bw, bh = boxes[0][2] - boxes[0][0], boxes[0][3] - boxes[0][1]
        cw = entry.get("cell_width") or round(entry["cell_height"] * bw / bh)
        ch = entry.get("cell_height") or round(cw * bh / bw)
        opts.update(width=cw, height=ch)
        parts = [pixelate.convert(img.crop(b), **opts)[0] for b in boxes]
        parts += [derive(parts[0], kind) for kind in entry.get("derive", [])]
        result = Image.new("RGBA", (cw * len(parts), ch), (0, 0, 0, 0))
        for i, part in enumerate(parts):
            result.alpha_composite(part, (i * cw, 0))
    if cells is None and entry.get("derive"):
        parts = [result] + [derive(result, kind) for kind in entry["derive"]]
        sheet = Image.new("RGBA", (result.width * len(parts), result.height), (0, 0, 0, 0))
        for i, part in enumerate(parts):
            sheet.alpha_composite(part, (i * result.width, 0))
        result = sheet
    if entry.get("mirror"):
        result = ImageOps.mirror(result)
    return result


def main(argv: list[str]) -> int:
    manifest = json.loads((ROOT / "tools/gui/assets.json").read_text(encoding="utf-8"))
    textures = ROOT / manifest["texture_root"]
    sources = ROOT / manifest["source_root"]
    preview_dir = ROOT / "build/gui-preview"
    preview_dir.mkdir(parents=True, exist_ok=True)
    filters = argv[1:]
    for entry in manifest["assets"]:
        if filters and not any(f in entry["output"] or f in entry["source"] for f in filters):
            continue
        out = textures / entry["output"]
        result = build(entry, sources, textures)
        out.parent.mkdir(parents=True, exist_ok=True)
        result.save(out)
        scale = 4 if result.width < 400 else 2
        result.resize((result.width * scale, result.height * scale), Image.NEAREST).save(
            preview_dir / (entry["output"].replace("/", "__").removesuffix(".png") + ".preview.png"))
        print(f"{entry['output']:42s} {result.width}x{result.height}")
    return 0


if __name__ == "__main__":
    sys.exit(main(sys.argv))
