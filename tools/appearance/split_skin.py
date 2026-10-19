#!/usr/bin/env python3
"""Split 64x64 Minecraft skins into appearance pieces for the character editor.

Each skin listed in npc_manifest.json becomes up to five pieces (hair, eyes, mouth,
outfit) at 128x128, plus the shared tintable body base. Tintable pixels are written
as grayscale "_tint" layers; the game multiplies them by the color the player picks.
The catalog consumed by the mod (data/bleachmod/appearance/catalog.json) is rewritten
from the manifest, so run this again after editing the manifest or the source skins.

    python3 tools/appearance/split_skin.py [--preview out.png]

New skins: drop 64x64 PNGs named after the character (kenpachi.png, renji.png) into
tools/appearance/skins/ and run the command again.
"""
import argparse
import colorsys
import json
import os
from PIL import Image

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
RES = os.path.join(ROOT, "src", "main", "resources")
TEX = os.path.join(RES, "assets", "bleachmod", "textures", "appearance")
CATALOG = os.path.join(RES, "data", "bleachmod", "appearance", "catalog.json")
MANIFEST = os.path.join(os.path.dirname(__file__), "npc_manifest.json")
# Drop-in folder: every <name>.png here becomes pieces too; an optional <name>.json beside it
# carries the source (author, license, link) and the same tuning keys as a manifest entry.
SKINS_DIR = os.path.join(os.path.dirname(__file__), "skins")
SCALE = 2  # 64x64 sources -> 128x128 pieces

# Box UVs of the vanilla player model: (u, v, width, height, depth).
HEAD = (0, 0, 8, 8, 8)
HAT = (32, 0, 8, 8, 8)
BASE_BOXES = [(16, 16, 8, 12, 4), (40, 16, 4, 12, 4), (32, 48, 4, 12, 4), (0, 16, 4, 12, 4), (16, 48, 4, 12, 4)]
OUTER_BOXES = [(16, 32, 8, 12, 4), (40, 32, 4, 12, 4), (48, 48, 4, 12, 4), (0, 32, 4, 12, 4), (0, 48, 4, 12, 4)]


def faces(box):
    """Named face rectangles (x, y, w, h) of a box in texture space."""
    u, v, w, h, d = box
    return {"top": (u + d, v, w, d), "bottom": (u + d + w, v, w, d), "right": (u, v + d, d, h),
            "front": (u + d, v + d, w, h), "left": (u + d + w, v + d, d, h), "back": (u + 2 * d + w, v + d, w, h)}


def pixels(rect):
    x, y, w, h = rect
    return [(x + i, y + j) for j in range(h) for i in range(w)]


def box_pixels(box):
    return [p for r in faces(box).values() for p in pixels(r)]


def lum(c):
    return 0.299 * c[0] + 0.587 * c[1] + 0.114 * c[2]


def dist(a, b):
    return sum((a[i] - b[i]) ** 2 for i in range(3)) ** 0.5


def warm(c):
    """Human skin tones: red >= green >= blue with moderate saturation (pale to dark)."""
    hi, lo = max(c), min(c)
    return hi > 60 and c[0] >= c[1] >= c[2] * 0.75 and 0.06 <= (hi - lo) / hi <= 0.75


def skin_palette(img, sample=None):
    """Skin reference colors: dominant warm color of the hands and cheeks plus its close shades."""
    pts = [(x, y) for x in range(48, 52) for y in range(16, 20)] + [(x, y) for x in range(40, 44) for y in range(48, 52)]
    pts += [(x, y) for x in range(9, 15) for y in range(12, 15)]
    if sample:
        pts = [tuple(sample)]
    counts = {}
    for p in pts:
        c = img.getpixel(p)
        if c[3] > 0:
            counts[c[:3]] = counts.get(c[:3], 0) + 1
    candidates = {c: n for c, n in counts.items() if warm(c)} or counts
    mode = max(candidates, key=candidates.get)
    return [c for c in candidates if dist(c, mode) < 35] or [mode]


def is_skin(c, palette, tolerance):
    return c[3] > 0 and min(dist(c[:3], p) for p in palette) < tolerance


def dominant(colors):
    """Most common color cluster (radius 40) of a list of RGB tuples."""
    best, best_n = None, -1
    for c in set(colors):
        n = sum(1 for o in colors if dist(c, o) < 40)
        if n > best_n:
            best, best_n = c, n
    return best


def hair_pixels(img, palette, tolerance):
    """Head base pixels that are hair: everything non-skin outside the face, plus fringe grown into it."""
    front = set(pixels(faces(HEAD)["front"]))
    head = [p for p in box_pixels(HEAD) if img.getpixel(p)[3] > 0 and not is_skin(img.getpixel(p), palette, tolerance)]
    outside = [p for p in head if p not in front]
    if not outside:
        return set(), None
    color = dominant([img.getpixel(p)[:3] for p in outside])
    hair = set(outside)
    fx, fy, fw, fh = faces(HEAD)["front"]
    # Fringe: hair-colored pixels reachable from the top row or the side columns of the face.
    seeds = [p for p in front if (p[1] == fy or p[0] in (fx, fx + fw - 1)) and p in head and dist(img.getpixel(p)[:3], color) < 70]
    stack = list(seeds)
    while stack:
        p = stack.pop()
        if p in hair:
            continue
        hair.add(p)
        for q in ((p[0] + 1, p[1]), (p[0] - 1, p[1]), (p[0], p[1] + 1), (p[0], p[1] - 1)):
            if q in front and q in head and q not in hair and dist(img.getpixel(q)[:3], color) < 70:
                stack.append(q)
    return hair, color


class Piece:
    def __init__(self):
        self.fixed = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
        self.tint = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
        self.tint_samples = []

    def put(self, p, c, tinted):
        (self.tint if tinted else self.fixed).putpixel(p, c)
        if tinted:
            self.tint_samples.append(c[:3])

    def empty(self):
        return self.fixed.getbbox() is None and self.tint.getbbox() is None

    def finish(self):
        """Turn tinted pixels into grayscale and return the color that reproduces the source."""
        if not self.tint_samples:
            return None
        top = max(lum(c) for c in self.tint_samples) or 1
        ref = dominant(self.tint_samples)
        for p in [(x, y) for y in range(64) for x in range(64)]:
            c = self.tint.getpixel(p)
            if c[3]:
                v = round(255 * lum(c) / top)
                self.tint.putpixel(p, (v, v, v, c[3]))
        k = top / max(lum(ref), 1)
        return "#%02X%02X%02X" % tuple(min(255, round(ch * k)) for ch in ref)


def saturated(c):
    h, s, v = colorsys.rgb_to_hsv(*(ch / 255 for ch in c[:3]))
    return s > 0.35 and v > 0.25


def split(img, entry):
    tolerance = entry.get("skinTolerance", 30)
    palette = skin_palette(img, entry.get("skinSample"))
    hair_set, hair_color = hair_pixels(img, palette, tolerance)
    tint_hair = entry.get("tintHair", True)
    pieces = {k: Piece() for k in ("hair", "eyes", "mouth", "outfit")}

    for p in box_pixels(HAT):  # the whole hat layer is hair/headwear
        c = img.getpixel(p)
        if c[3]:
            pieces["hair"].put(p, c, tint_hair and hair_color is not None and dist(c[:3], hair_color) < 70)
    for p in hair_set:
        c = img.getpixel(p)
        pieces["hair"].put(p, c, tint_hair and dist(c[:3], hair_color) < 70)

    fx, fy, fw, fh = faces(HEAD)["front"]
    mouth_row = fy + entry.get("mouthRow", 6)
    for p in pixels(faces(HEAD)["front"]):
        c = img.getpixel(p)
        if c[3] == 0 or p in hair_set or is_skin(c, palette, tolerance):
            continue
        target = "mouth" if p[1] >= mouth_row else "eyes"
        pieces[target].put(p, c, target == "eyes" and saturated(c))

    for box in BASE_BOXES:
        for p in box_pixels(box):
            c = img.getpixel(p)
            if c[3] and not is_skin(c, palette, tolerance):
                pieces["outfit"].put(p, c, False)
    for box in OUTER_BOXES:
        for p in box_pixels(box):
            c = img.getpixel(p)
            if c[3]:
                pieces["outfit"].put(p, c, False)
    skin = dominant(palette)
    return pieces, "#%02X%02X%02X" % skin


def body_base():
    """Shared tintable body: every base face filled, top faces lighter and bottom faces darker."""
    img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
    shade = {"top": 255, "front": 236, "back": 214, "left": 224, "right": 224, "bottom": 196}
    for box in [HEAD] + BASE_BOXES:
        for name, rect in faces(box).items():
            for p in pixels(rect):
                v = shade[name]
                img.putpixel(p, (v, v, v, 255))
    return img


def save(img, path):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.resize((64 * SCALE, 64 * SCALE), Image.NEAREST).save(path)


def dropped_skins(known):
    """Entries for PNGs in SKINS_DIR that the manifest does not list yet."""
    entries = []
    if not os.path.isdir(SKINS_DIR):
        return entries
    for file in sorted(os.listdir(SKINS_DIR)):
        name, ext = os.path.splitext(file)
        if ext.lower() != ".png":
            continue
        name = name.lower().replace("-", "_").replace(" ", "_")
        if not name.replace("_", "").isalnum() or not name.isascii():
            print(f"  skipped {file}: use letters, digits and _ in the file name")
            continue
        if name in known:
            continue
        path = os.path.join(SKINS_DIR, file)
        size = Image.open(path).size
        if size != (64, 64):
            print(f"  skipped {file}: {size[0]}x{size[1]}, only 64x64 skins are supported")
            continue
        entry = {"name": name, "file": os.path.relpath(path, ROOT),
                 "source": {"author": "unknown", "license": "unverified", "origin": "tools/appearance/skins"}}
        sidecar = os.path.join(SKINS_DIR, os.path.splitext(file)[0] + ".json")
        if os.path.exists(sidecar):
            with open(sidecar, encoding="utf-8") as f:
                extra = json.load(f)
            entry["source"].update(extra.pop("source", {}))
            entry.update(extra)
        entries.append(entry)
        known.add(name)
    return entries


def main():
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("--preview", help="write a contact sheet of every generated piece")
    args = parser.parse_args()
    with open(MANIFEST, encoding="utf-8") as f:
        manifest = json.load(f)
    manifest["skins"] += dropped_skins({npc["name"] for npc in manifest["skins"]})

    entries = [{"id": "base:default", "category": "base", "layers": [
        {"texture": "bleachmod:textures/appearance/base/default_tint.png", "tint": "skin"}],
        "source": {"author": "Bleach Mod", "license": "GPL-3.0", "origin": "generated by tools/appearance/split_skin.py"}}]
    save(body_base(), os.path.join(TEX, "base", "default_tint.png"))
    sheet = []
    for npc in manifest["skins"]:
        name = npc["name"]
        img = Image.open(os.path.join(ROOT, npc["file"])).convert("RGBA")
        pieces, skin = split(img, npc)
        for category, piece in pieces.items():
            if category in npc.get("skip", []) or piece.empty():
                continue
            default = piece.finish()
            layers = []
            if piece.fixed.getbbox():
                save(piece.fixed, os.path.join(TEX, category, name + ".png"))
                layers.append({"texture": f"bleachmod:textures/appearance/{category}/{name}.png", "tint": "none"})
            if piece.tint.getbbox():
                save(piece.tint, os.path.join(TEX, category, name + "_tint.png"))
                channel = {"hair": "hair", "eyes": "eye"}.get(category, "none")
                layers.append({"texture": f"bleachmod:textures/appearance/{category}/{name}_tint.png", "tint": channel})
            entry = {"id": f"{category}:{name}", "category": category, "inspiredBy": name, "layers": layers,
                     "source": npc["source"]}
            if default:
                entry["defaultColor"] = default
            entries.append(entry)
            sheet.append((f"{category}:{name}", piece))
        npc["_skin"] = skin

    catalog = {
        "_comment": "Generated by tools/appearance/split_skin.py from tools/appearance/npc_manifest.json; edit the manifest, not this file.",
        "skinColors": manifest["skinColors"], "hairColors": manifest["hairColors"], "eyeColors": manifest["eyeColors"],
        "defaults": manifest["defaults"], "pieces": entries}
    os.makedirs(os.path.dirname(CATALOG), exist_ok=True)
    with open(CATALOG, "w", encoding="utf-8") as f:
        json.dump(catalog, f, indent=2, ensure_ascii=False)
        f.write("\n")
    print(f"{len(entries)} pieces -> {os.path.relpath(CATALOG, ROOT)}")
    for npc in manifest["skins"]:
        print(f"  {npc['name']}: skin {npc['_skin']}")

    if args.preview:
        cols = 6
        rows = (len(sheet) + cols - 1) // cols
        out = Image.new("RGBA", (cols * 136, rows * 136), (50, 50, 60, 255))
        for i, (_, piece) in enumerate(sheet):
            tile = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
            tile.alpha_composite(piece.tint)
            tile.alpha_composite(piece.fixed)
            out.alpha_composite(tile.resize((128, 128), Image.NEAREST), ((i % cols) * 136 + 4, (i // cols) * 136 + 4))
        out.save(args.preview)


if __name__ == "__main__":
    main()
