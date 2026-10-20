#!/usr/bin/env python3
"""Converte "pixel art" gerada por IA (imagem grande, borrada e com grade
irregular) na resolucao real da arte, um pixel de textura por pixel desenhado.

Uso basico:
    python tools/gui/pixelate.py entrada.png -o saida.png
    python tools/gui/pixelate.py entrada.png -o saida.png --grid 9 --colors 48
    python tools/gui/pixelate.py entrada.png -o saida.png --width 240

Como funciona:
  1. Mede, em cada eixo, onde as cores mudam (perfil de bordas).
  2. Descobre o periodo da grade (tamanho do "pixel" da IA) pelo espectro desse
     perfil, ou usa --grid / --width se informado.
  3. Posiciona os cortes seguindo as bordas reais, tolerando o desvio que a IA
     acumula ao longo da imagem (a grade nunca e perfeitamente regular).
  4. Cada celula vira um pixel com a cor dominante do seu miolo, sem misturar
     com as vizinhas, o que elimina o borrado.
  5. Opcional: reduz a paleta (--colors) e apara bordas transparentes (--trim).

Sempre grava tambem <saida>.preview.png ampliado 4x para conferir o resultado.
"""
from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path

import numpy as np
from PIL import Image


def edge_profile(rgba: np.ndarray, axis: int) -> np.ndarray:
    """Soma da diferenca de cor entre colunas (axis=1) ou linhas (axis=0) vizinhas."""
    premul = rgba[..., :3] * (rgba[..., 3:] / 255.0)
    feat = np.concatenate([premul, rgba[..., 3:]], axis=2)
    diff = np.abs(np.diff(feat, axis=axis)).sum(axis=2)
    return diff.sum(axis=0 if axis == 1 else 1)


def estimate_period(profile: np.ndarray, lo: float = 3.0, hi: float = 40.0) -> tuple[float, float]:
    """Retorna (periodo, fase) dominantes do perfil de bordas."""
    signal = profile - profile.mean()
    n = len(signal)
    idx = np.arange(n)
    hi = min(hi, n / 4)
    best = (0.0, lo, 0.0)
    for period in np.arange(lo, hi, 0.05):
        acc = (signal * np.exp(2j * np.pi * idx / period)).sum()
        score = abs(acc) / n
        if score > best[0]:
            best = (score, period, np.angle(acc))
    _, period, angle = best
    # A IA as vezes acentua cada meia celula; se o dobro do periodo tambem casa
    # bem com as bordas, a grade verdadeira e a maior.
    phase = (-angle / (2 * np.pi) * period) % period
    return float(period), float(phase)


def place_cuts(profile: np.ndarray, period: float, phase: float) -> list[int]:
    """Cortes da grade, cada um encaixado na borda mais forte perto do esperado."""
    n = len(profile) + 1
    window = max(1, int(round(period * 0.3)))
    # Abaixo disso a borda e ruido (area lisa): mantem o corte onde a grade manda.
    strong = np.percentile(profile, 70)
    cuts = [0]
    expected = phase if phase > period * 0.5 else phase + period
    while expected < n - period * 0.5:
        center = int(round(expected))
        lo, hi = max(cuts[-1] + 1, center - window), min(n - 1, center + window + 1)
        if lo >= hi:
            break
        local = profile[lo - 1:hi - 1]
        if local.max() > strong:
            snapped = lo + int(np.argmax(local))
            # Mistura o encaixe com o esperado para nao seguir ruido pontual.
            expected = (snapped * 0.6 + expected * 0.4) + period
        else:
            snapped = min(max(center, lo), hi - 1)
            expected += period
        cuts.append(snapped)
    cuts.append(n)
    # Remove celulas degeneradas (menores que meio periodo) nas pontas.
    cleaned = [cuts[0]]
    for c in cuts[1:]:
        if c - cleaned[-1] >= max(1, period * 0.5) or c == n:
            cleaned.append(c)
    if len(cleaned) > 2 and cleaned[-1] - cleaned[-2] < period * 0.5:
        cleaned.pop(-2)
    return cleaned


def uniform_cuts(length: int, cells: int) -> list[int]:
    return [int(round(i * length / cells)) for i in range(cells + 1)]


def cell_color(block: np.ndarray, alpha_cut: int, soft_alpha: bool) -> np.ndarray:
    h, w, _ = block.shape
    # Usa o miolo da celula: as bordas sao justamente onde a IA borra.
    my, mx = int(h * 0.2), int(w * 0.2)
    core = block[my:h - my or h, mx:w - mx or w].reshape(-1, 4)
    if core.size == 0:
        core = block.reshape(-1, 4)
    alpha = np.median(core[:, 3])
    if alpha < alpha_cut:
        return np.array([0, 0, 0, 0], dtype=np.uint8)
    opaque = core[core[:, 3] >= alpha_cut]
    keys = (opaque[:, :3] >> 3).astype(np.int32)
    packed = keys[:, 0] << 10 | keys[:, 1] << 5 | keys[:, 2]
    values, counts = np.unique(packed, return_counts=True)
    dominant = values[np.argmax(counts)]
    members = opaque[packed == dominant]
    rgb = members[:, :3].mean(axis=0)
    out_alpha = alpha if soft_alpha else 255
    return np.array([*np.round(rgb), out_alpha], dtype=np.uint8)


def pixelate(rgba: np.ndarray, xcuts: list[int], ycuts: list[int],
             alpha_cut: int, soft_alpha: bool) -> np.ndarray:
    out = np.zeros((len(ycuts) - 1, len(xcuts) - 1, 4), dtype=np.uint8)
    for j in range(len(ycuts) - 1):
        for i in range(len(xcuts) - 1):
            block = rgba[ycuts[j]:ycuts[j + 1], xcuts[i]:xcuts[i + 1]]
            out[j, i] = cell_color(block, alpha_cut, soft_alpha)
    return out


def reduce_palette(img: Image.Image, colors: int) -> Image.Image:
    alpha = img.getchannel("A")
    rgb = img.convert("RGB").quantize(colors=colors, method=Image.Quantize.MEDIANCUT,
                                      dither=Image.Dither.NONE).convert("RGB")
    rgb.putalpha(alpha)
    return rgb


def convert(src: Image.Image, grid: float | None = None, grid_y: float | None = None,
            width: int | None = None, height: int | None = None, colors: int = 0,
            alpha_cut: int = 110, soft_alpha: bool = False, trim: bool = False):
    """Converte uma imagem PIL; retorna (imagem, (grade_x, grade_y))."""
    rgba = np.asarray(src.convert("RGBA")).astype(np.int32)
    h, w = rgba.shape[:2]
    if width:
        cols = width
        rows = height or max(1, round(h * cols / w))
        xcuts, ycuts = uniform_cuts(w, cols), uniform_cuts(h, rows)
        px, py = w / cols, h / rows
    else:
        ex, ey = edge_profile(rgba, 1), edge_profile(rgba, 0)
        if grid:
            px, py = grid, grid_y or grid
            _, phx = estimate_period(ex, px - 0.01, px + 0.06)
            _, phy = estimate_period(ey, py - 0.01, py + 0.06)
        else:
            px, phx = estimate_period(ex)
            py, phy = estimate_period(ey)
            # Pixels da IA sao quadrados; se os eixos discordam muito, confia no
            # eixo mais longo (mais amostras) e reajusta so a fase do outro.
            if abs(px - py) / max(px, py) > 0.12:
                base = px if w >= h else py
                if w >= h:
                    _, phy = estimate_period(ey, base - 0.01, base + 0.06)
                    py = base
                else:
                    _, phx = estimate_period(ex, base - 0.01, base + 0.06)
                    px = base
        xcuts, ycuts = place_cuts(ex, px, phx), place_cuts(ey, py, phy)

    img = Image.fromarray(pixelate(rgba, xcuts, ycuts, alpha_cut, soft_alpha), "RGBA")
    if colors:
        img = reduce_palette(img, colors)
    if trim and img.getbbox():
        img = img.crop(img.getchannel("A").getbbox())
    return img, (px, py)


def main(argv: list[str] | None = None) -> int:
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("input", type=Path)
    ap.add_argument("-o", "--output", type=Path, required=True)
    ap.add_argument("--grid", type=float, help="tamanho do pixel da IA em px (pula a deteccao)")
    ap.add_argument("--grid-y", type=float, help="tamanho vertical, se diferente do horizontal")
    ap.add_argument("--width", type=int, help="forca a largura final em pixels (grade uniforme)")
    ap.add_argument("--height", type=int, help="forca a altura final (padrao: proporcional a --width)")
    ap.add_argument("--colors", type=int, default=0, help="limita a paleta (0 = sem limite)")
    ap.add_argument("--alpha-cut", type=int, default=110, help="alpha abaixo disso vira transparente")
    ap.add_argument("--soft-alpha", action="store_true", help="mantem alpha parcial (brilhos)")
    ap.add_argument("--crop", type=str, help="recorta a fonte antes: x0,y0,x1,y1 (px da imagem original)")
    ap.add_argument("--trim", action="store_true", help="apara bordas totalmente transparentes")
    ap.add_argument("--no-preview", action="store_true")
    args = ap.parse_args(argv)

    src = Image.open(args.input).convert("RGBA")
    if args.crop:
        src = src.crop(tuple(int(v) for v in args.crop.split(",")))
    img, (px, py) = convert(src, grid=args.grid, grid_y=args.grid_y, width=args.width, height=args.height,
                            colors=args.colors, alpha_cut=args.alpha_cut, soft_alpha=args.soft_alpha, trim=args.trim)
    w, h = src.size

    args.output.parent.mkdir(parents=True, exist_ok=True)
    img.save(args.output)
    if not args.no_preview:
        preview = args.output.with_suffix(".preview.png")
        img.resize((img.width * 4, img.height * 4), Image.NEAREST).save(preview)

    print(json.dumps({
        "input": str(args.input), "source_size": [w, h],
        "grid": [round(px, 2), round(py, 2)],
        "output": str(args.output), "size": [img.width, img.height],
    }))
    return 0


if __name__ == "__main__":
    sys.exit(main())
