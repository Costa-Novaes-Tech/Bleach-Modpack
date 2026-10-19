#!/usr/bin/env python3
"""Desenha um layout de gui_layouts/ numa imagem, sem abrir o jogo.

    python tools/gui/preview_layout.py journal
    python tools/gui/preview_layout.py character --debug --scale 2

Os valores de exemplo (textos, flags, barras) vem de tools/gui/preview_samples/<id>.json.
O texto usa a fonte padrao do Minecraft (lida do jar do ForgeGradle) e as fontes do mod,
entao posicao e tamanho batem com o jogo. O modelo do jogador vira uma silhueta.
Saida: build/gui-preview/layout_<id>.png
"""
from __future__ import annotations

import argparse
import io
import json
import re
import zipfile
from pathlib import Path

import numpy as np
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1].parent
ASSETS = ROOT / "src/main/resources/assets"
JAR = Path.home() / ".gradle/caches/forge_gradle/minecraft_repo/versions/1.20.1/client-extra.jar"
PLACEHOLDER = re.compile(r"\{([a-zA-Z0-9_.]+)}")


# ------------------------------------------------------------------ fonts

class BitmapFont:
    """Um provider "bitmap" do Minecraft: grade de glifos, height e ascent em unidades de fonte."""

    def __init__(self, image: Image.Image, chars: list[str], height: float, ascent: float):
        self.image = image.convert("RGBA")
        rows, cols = len(chars), max(len(r) for r in chars)
        self.cw, self.ch = self.image.width // cols, self.image.height // rows
        self.unit = height / self.ch          # unidades de fonte por texel
        self.ascent = ascent
        self.glyphs = {}
        alpha = np.asarray(self.image)[..., 3]
        for r, line in enumerate(chars):
            for c, char in enumerate(line):
                if char == "\0":
                    continue
                cell = alpha[r * self.ch:(r + 1) * self.ch, c * self.cw:(c + 1) * self.cw]
                cols_used = np.where(cell.any(0))[0]
                width = int(cols_used.max()) + 1 if len(cols_used) else 0
                self.glyphs[char] = (c * self.cw, r * self.ch, width)

    def advance(self, char: str) -> float:
        _, _, width = self.glyphs[char]
        return int(0.5 + width * self.unit) + 1


class Font:
    def __init__(self, providers: list):
        self.providers = providers  # BitmapFont ou {"space": {...}}

    def find(self, char):
        for p in self.providers:
            if isinstance(p, dict) and char in p:
                return p
            if isinstance(p, BitmapFont) and char in p.glyphs:
                return p
        return None

    def width(self, text: str, bold=False) -> float:
        total = 0.0
        for char in text:
            p = self.find(char) or self.find("?")
            total += (p[char] if isinstance(p, dict) else p.advance(char if char in p.glyphs else "?")) + (1 if bold else 0)
        return total


def load_font(font_id: str, cache: dict) -> Font:
    if font_id in cache:
        return cache[font_id]
    ns, path = font_id.split(":")
    if ns == "minecraft":
        with zipfile.ZipFile(JAR) as jar:
            data = json.loads(jar.read(f"assets/minecraft/font/{path}.json"))
    else:
        data = json.loads((ASSETS / ns / "font" / f"{path}.json").read_text(encoding="utf-8"))
    providers = []
    for p in data["providers"]:
        if p["type"] == "reference":
            providers += load_font(p["id"], cache).providers
        elif p["type"] == "space":
            providers.append(p["advances"])
        elif p["type"] == "bitmap":
            pns, ppath = p["file"].split(":")
            if pns == "minecraft":
                with zipfile.ZipFile(JAR) as jar:
                    img = Image.open(io.BytesIO(jar.read(f"assets/minecraft/textures/{ppath}")))
            else:
                img = Image.open(ASSETS / pns / "textures" / ppath)
            providers.append(BitmapFont(img, p["chars"], p.get("height", 8), p["ascent"]))
    font = Font(providers)
    cache[font_id] = font
    return font


def draw_text(canvas: Image.Image, font: Font, text: str, x: float, y: float, scale: float,
              color: int, shadow: bool, bold: bool, spacing: float = 0) -> None:
    """scale = pixels de arte por unidade de fonte. (x, y) e o topo da linha, como no jogo."""
    def tinted(glyph: Image.Image, argb: int) -> Image.Image:
        r, g, b = (argb >> 16) & 255, (argb >> 8) & 255, argb & 255
        arr = np.asarray(glyph).astype(float)
        arr[..., 0] *= r / 255
        arr[..., 1] *= g / 255
        arr[..., 2] *= b / 255
        return Image.fromarray(arr.astype("uint8"), "RGBA")

    passes = [((color & 0xFCFCFC) >> 2, 1)] if shadow else []
    passes.append((color, 0))
    for argb, offset in passes:
        cx = x
        for char in text:
            p = font.find(char)
            if p is None:
                p, char = font.find("?"), "?"
            if isinstance(p, dict):
                cx += p[char] * scale
                continue
            gx, gy, width = p.glyphs[char]
            if width:
                glyph = p.image.crop((gx, gy, gx + width, gy + p.ch))
                k = p.unit * scale
                size = (max(1, round(width * k)), max(1, round(p.ch * k)))
                glyph = tinted(glyph.resize(size, Image.NEAREST), argb)
                top = y + (7 - p.ascent) * scale + offset * scale
                for b in ([0, 1] if bold else [0]):
                    canvas.alpha_composite(glyph, (round(cx + (offset + b) * scale), round(top)))
            cx += (p.advance(char) + (1 if bold else 0)) * scale + spacing


def split_lines(font: Font, text: str, max_units: float, bold: bool) -> list[str]:
    words, lines, line = text.split(" "), [], ""
    for word in words:
        trial = word if not line else line + " " + word
        if font.width(trial, bold) <= max_units or not line:
            line = trial
        else:
            lines.append(line)
            line = word
    if line:
        lines.append(line)
    return lines


# ------------------------------------------------------------------ layout

def expand(elements: list) -> list:
    out = []
    for raw in elements:
        if "repeat" not in raw:
            out.append(raw)
            continue
        spec = raw["repeat"]
        template = {k: v for k, v in raw.items() if k != "repeat"}
        text = json.dumps(template)
        for n in range(spec.get("count", 1)):
            copy = json.loads(text.replace("{i}", str(spec.get("start", 0) + n)))
            step = spec.get("step", [0, 0])
            copy["pos"] = [copy.get("pos", [0, 0])[0] + step[0] * n, copy.get("pos", [0, 0])[1] + step[1] * n]
            out.append(copy)
    return out


def color(value: str | None, default: int = 0xFFFFFFFF) -> int:
    if value is None:
        return default
    h = value.lstrip("#")
    return int(h, 16) | (0xFF000000 if len(h) == 6 else 0)


class Sample:
    def __init__(self, data: dict):
        self.flags = set(data.get("flags", []))
        self.texts = data.get("texts", {})
        self.values = data.get("values", {})

    def test(self, cond):
        if not cond:
            return True
        return cond[1:] not in self.flags if cond.startswith("!") else cond in self.flags

    def resolve(self, template: str) -> str:
        return PLACEHOLDER.sub(lambda m: str(self.texts.get(m.group(1), m.group(0))), template)

    def label(self, e: dict, lang: dict) -> str:
        if "translate" in e:
            text = lang.get(e["translate"], e["translate"])
            args = [self.resolve(a) for a in e.get("args", [])]
            text = re.sub(r"%(\d+)\$s", lambda m: args[int(m.group(1)) - 1], text)
            for a in args:
                text = text.replace("%s", a, 1)
        else:
            text = self.resolve(e["text"])
        return text.upper() if e.get("uppercase") else text


def texture(path: str) -> Image.Image:
    if ":" in path:
        ns, rest = path.split(":")
        return Image.open(ASSETS / ns / rest).convert("RGBA")
    return Image.open(ASSETS / "bleachmod/textures/gui" / path).convert("RGBA")


def cell(img: Image.Image, e: dict, sample: Sample, mouse=None) -> tuple:
    names = e.get("states", [])
    if "cells" in e:
        names = [f"cell{i}" for i in range(int(e["cells"]))]
    n = max(1, len(names))
    cw = img.width // n
    if "cells" in e:
        index = int(e["cell"])
    elif mouse is not None and not mouse:
        index = names.index("disabled") if "disabled" in names else names.index("locked") if "locked" in names else 0
    else:
        index = next((names.index(s) for s, c in e.get("state_if", {}).items() if sample.test(c)),
                     names.index("normal") if "normal" in names else 0)
    return index * cw, 0, cw, img.height


def nine_slice(img, w, h, s):
    out = Image.new("RGBA", (w, h))
    xs = [(0, s[0], 0, s[0]), (s[0], img.width - s[2], s[0], w - s[2]), (img.width - s[2], img.width, w - s[2], w)]
    ys = [(0, s[1], 0, s[1]), (s[1], img.height - s[3], s[1], h - s[3]), (img.height - s[3], img.height, h - s[3], h)]
    for sx0, sx1, dx0, dx1 in xs:
        for sy0, sy1, dy0, dy1 in ys:
            if dx1 > dx0 and dy1 > dy0:
                part = img.crop((sx0, sy0, sx1, sy1)).resize((dx1 - dx0, dy1 - dy0), Image.NEAREST)
                out.alpha_composite(part, (dx0, dy0))
    return out


def render(layout_id: str, debug: bool) -> Image.Image:
    layout = json.loads((ASSETS / "bleachmod/gui_layouts" / f"{layout_id}.json").read_text(encoding="utf-8"))
    sample_file = ROOT / "tools/gui/preview_samples" / f"{layout_id}.json"
    sample = Sample(json.loads(sample_file.read_text(encoding="utf-8")) if sample_file.exists() else {})
    lang = json.loads((ASSETS / "bleachmod/lang/en_us.json").read_text(encoding="utf-8"))
    density = layout.get("art_density", 1)
    w, h = layout["size"]
    canvas = Image.new("RGBA", (w, h), (34, 52, 30, 255))
    fonts: dict = {}
    draw = ImageDraw.Draw(canvas)

    for e in expand(layout["elements"]):
        if not sample.test(e.get("visible_if")):
            continue
        x, y = e.get("pos", [0, 0])
        t = e["type"]
        if t in ("image", "button"):
            img = texture(e["texture"])
            enabled = sample.test(e.get("enabled_if")) if t == "button" else None
            u, v, uw, vh = e.get("uv") or cell(img, e, sample, enabled)
            part = img.crop((u, v, u + uw, v + vh))
            size = e.get("size", [0, 0])
            tw, th = size[0] or uw, size[1] or vh
            if e.get("slide_value"):
                y += round(sample.values.get(e["slide_value"], 0) * e.get("slide_range", 0))
            part = nine_slice(part, tw, th, e["slice"]) if "slice" in e else part.resize((tw, th), Image.NEAREST)
            canvas.alpha_composite(part, (x, y))
            if t == "button" and ("text" in e or "translate" in e):
                font = load_font(e.get("font", "minecraft:default"), fonts)
                text = sample.label(e, lang)
                s = e.get("scale", 1) * density
                bold = e.get("bold", False)
                lp = e.get("label_pos")
                max_w = (tw - 8) if lp is None else (tw - lp[0] - 6)
                if font.width(text, bold) * s > max_w:
                    s = max_w / max(1, font.width(text, bold))
                tx = x + tw / 2 - font.width(text, bold) * s / 2 if lp is None else x + lp[0]
                ty = (y + (th / 2 if lp is None else lp[1])) - 4 * s
                col = color(e.get("color")) if enabled else color(e.get("disabled_color"), 0xFF8A8F99)
                draw_text(canvas, font, text, tx, ty, s, col, e.get("shadow", True), bold)
            bounds = (x, y, tw, th)
        elif t == "bar":
            fill = texture(e["texture"]) if "texture" in e else None
            bw, bh = e["size"]
            ratio = sample.values.get(e.get("value", e["id"]), 0)
            skew, skew_end = e.get("skew", 0), e.get("skew_end", e.get("skew", 0))
            for row in range(bh):
                tt = 0 if bh <= 1 else row / (bh - 1)
                left = round(skew * (1 - tt)) if skew >= 0 else round(-skew * tt)
                right = bw - (round(skew_end * tt) if skew_end >= 0 else round(-skew_end * (1 - tt)))
                cut = round((right - left) * ratio)
                if cut <= 0:
                    continue
                if fill is not None:
                    canvas.alpha_composite(fill.crop((left, row, left + cut, row + 1)), (x + left, y + row))
                else:
                    c = color(e.get("color"))
                    draw.rectangle((x + left, y + row, x + left + cut - 1, y + row), fill=((c >> 16) & 255, (c >> 8) & 255, c & 255, 255))
            bounds = (x, y, bw, bh)
        elif t == "text":
            font = load_font(e.get("font", "minecraft:default"), fonts)
            text = sample.label(e, lang)
            s = e.get("scale", 1) * density
            bold = e.get("bold", False)
            col = color(e.get("color"))
            if e.get("wrap") and e.get("max_width"):
                lines = split_lines(font, text, e["max_width"] / s, bold)[: e.get("max_lines") or None]
                step = e.get("line_height") or 9 * s
                for i, line in enumerate(lines):
                    lx = x - (font.width(line, bold) * s / 2 if e.get("align") == "center" else
                              font.width(line, bold) * s if e.get("align") == "right" else 0)
                    draw_text(canvas, font, line, lx, y + i * step, s, col, e.get("shadow", True), bold)
                bounds = (x, y, e["max_width"], round(len(lines) * step))
            else:
                sp = e.get("letter_spacing", 0)
                tw = font.width(text, bold) * s + sp * max(0, len(text) - 1)
                if e.get("max_width") and tw > e["max_width"]:
                    k = e["max_width"] / tw
                    s, sp, tw = s * k, sp * k, e["max_width"]
                tx = x - (tw / 2 if e.get("align") == "center" else tw if e.get("align") == "right" else 0)
                ty = y - (4 * s if e.get("valign") == "middle" else 8 * s if e.get("valign") == "bottom" else 0)
                draw_text(canvas, font, text, tx, ty, s, col, e.get("shadow", True), bold, sp)
                bounds = (round(tx), round(ty), round(tw), round(8 * s))
        elif t == "rect":
            c = color(e.get("color"))
            draw.rectangle((x, y, x + e["size"][0] - 1, y + e["size"][1] - 1), fill=((c >> 16) & 255, (c >> 8) & 255, c & 255, c >> 24))
            bounds = (x, y, *e["size"])
        elif t == "entity":
            ew, eh = e["size"]
            draw.rectangle((x + ew // 2 - 18, y + eh - 170, x + ew // 2 + 18, y + eh), fill=(15, 15, 25, 230))
            draw.rectangle((x + ew // 2 - 14, y + eh - 205, x + ew // 2 + 14, y + eh - 172), fill=(15, 15, 25, 230))
            bounds = (x, y, ew, eh)
        else:
            continue
        if debug:
            bx, by, bw2, bh2 = bounds
            draw.rectangle((bx, by, bx + bw2 - 1, by + bh2 - 1), outline=(0, 255, 255, 255))
            draw.text((bx + 1, by - 9), e.get("id", ""), fill=(0, 255, 255, 255))
    return canvas


def main() -> int:
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("layout")
    ap.add_argument("--debug", action="store_true")
    ap.add_argument("--scale", type=int, default=1)
    args = ap.parse_args()
    img = render(args.layout, args.debug)
    if args.scale > 1:
        img = img.resize((img.width * args.scale, img.height * args.scale), Image.NEAREST)
    out = ROOT / "build/gui-preview" / f"layout_{args.layout}.png"
    out.parent.mkdir(parents=True, exist_ok=True)
    img.save(out)
    print(out)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
