"""Generates the three Nozarashi item models (sealed katana, Shikai cleaver, Berserk Mode cleaver).

Run from the repository root:  python tools/models/nozarashi_models.py
The swords follow the Hyorinmaru convention: blade along +Y, every element rotated -45 degrees on Z around
(8, 8, 8), vanilla block textures only. Vanilla limits element coordinates to [-16, 32], so the cleaver's
size comes from the display transforms instead of longer elements.
"""
import json
from pathlib import Path

OUT = Path(__file__).resolve().parents[2] / "src/main/resources/assets/bleachmod/models/item"
GLOW = {"block_light": 15, "sky_light": 15, "ambient_occlusion": False}


def box(name, frm, to, tex, glow=False):
    frm = [round(v, 3) for v in frm]
    to = [round(v, 3) for v in to]
    sx, sy, sz = (min(16, max(.1, to[i] - frm[i])) for i in range(3))
    uv = {"north": [0, 0, sx, sy], "south": [0, 0, sx, sy], "east": [0, 0, sz, sy],
          "west": [0, 0, sz, sy], "up": [0, 0, sx, sz], "down": [0, 0, sx, sz]}
    element = {"name": name, "from": frm, "to": to,
               "rotation": {"angle": -45, "axis": "z", "origin": [8, 8, 8]},
               "faces": {face: {"uv": [round(v, 3) for v in coords], "texture": "#" + tex} for face, coords in uv.items()}}
    if glow:
        element["forge_data"] = GLOW
    return element


def handle(top, wrap="wrap", binding="binding", width=1.3):
    """Long two-hand grip with diamond-like binding bands and a heavy pommel."""
    lo, hi = 8 - width / 2, 8 + width / 2
    parts = [box("grip", [lo, 0, lo], [hi, top, hi], wrap),
             box("pommel", [lo - .2, -0.6, lo - .2], [hi + .2, 0.2, hi + .2], "guard")]
    y = 0.9
    while y + .3 < top - .4:
        parts.append(box("binding", [lo - .06, y, lo - .06], [hi + .06, y + .28, hi + .06], binding))
        y += 1.15
    return parts


def sealed():
    """Zaraki's sealed blade: long, plain and chipped all along its edge from countless fights."""
    parts = handle(7.2, width=1.25)
    parts += [box("worn_tsuba", [6.3, 7.2, 7.1], [9.7, 7.75, 8.9], "guard"),
              box("habaki", [7.35, 7.75, 7.65], [8.65, 8.4, 8.35], "guard")]
    # Blade body in segments that drift toward the spine; the edge strip has gaps where it was chipped.
    segments = [(8.4, 14, 0), (14, 20, .12), (20, 26, .3), (26, 29.4, .55)]
    for y0, y1, shift in segments:
        parts.append(box("blade", [7.45 + shift, y0, 7.8], [8.45 + shift, y1, 8.2], "blade"))
        parts.append(box("spine", [8.43 + shift, y0, 7.74], [8.58 + shift, y1, 8.26], "spine"))
    chips = [(9.6, 10.3), (12.4, 12.8), (16.1, 17.0), (19.2, 19.6), (22.8, 23.7), (25.3, 25.6)]
    edge_y, cuts = 8.4, []
    for c0, c1 in chips:
        cuts.append((edge_y, c0)); edge_y = c1
    cuts.append((edge_y, 29.4))
    for y0, y1 in cuts:
        shift = next(s for a, b, s in segments if a <= y0 < b)
        parts.append(box("chipped_edge", [7.31 + shift, y0, 7.96], [7.46 + shift, y1, 8.04], "edge"))
    for c0, c1 in chips:  # darker bites cut into the blade surface
        shift = next(s for a, b, s in segments if a <= c0 < b)
        parts.append(box("notch", [7.42 + shift, c0, 7.78], [7.75 + shift, c1, 8.22], "notch"))
    parts += [box("kissaki", [8.05, 29.4, 7.8], [9.0, 30.3, 8.2], "blade"),
              box("kissaki_tip", [8.6, 30.3, 7.85], [9.0, 31.0, 8.15], "blade")]
    return parts


def cleaver(bankai):
    """Nozarashi as in the anime: a long wrapped pole topped by a gold ferrule with green tassels, carrying a
    huge crescent blade that sweeps out from the top and hangs down to a point. Berserk Mode darkens the steel
    and splits it with glowing red cracks."""
    import math
    t = .3 if bankai else 0  # extra thickness in Berserk Mode
    parts = [box("pole", [7.3, -0.5, 7.3], [8.7, 25.0, 8.7], "wrap"),
             box("pommel", [7.1, -1.4, 7.1], [8.9, -0.5, 8.9], "guard")]
    y = 0.2
    while y < 23.8:  # dark diagonal-looking stripes of the wrap
        parts.append(box("wrap_band", [7.24, y, 7.24], [8.76, y + .45, 8.76], "binding")); y += 1.3
    # Gold ferrule clamping the blade, with a dark rivet hole as in the manga.
    parts += [box("ferrule", [6.2, 24.0, 6.9 - t], [10.6, 31.4, 9.1 + t], "gold"),
              box("ferrule_lip", [5.9, 30.6, 6.7 - t], [10.9, 31.8, 9.3 + t], "gold"),
              box("ferrule_hole", [8.4, 26.0, 6.8 - t], [9.8, 27.6, 9.2 + t], "notch")]
    for i, (x, length) in enumerate([(9.4, 6.5), (9.9, 8.0), (10.3, 5.5), (8.9, 7.2), (10.6, 4.4)]):
        z = 7.6 + (i % 3) * .3
        parts.append(box("tassel", [x, 24.0 - length, z], [x + .35, 24.0, z + .35], "tassel"))
    # Crescent: vertical slices from the ferrule (u=0) to the hanging tip (u=1).
    x0, x1, step = 6.2, -15.6, .9
    n = int((x0 - x1) / step)
    for i in range(n):
        u = i / (n - 1)
        top = 31.6 - 1.5 * u - 10.5 * u ** 3
        height = 9.4 * (1 - u) ** .85 + .45
        bottom = top - height
        xa, xb = x0 - (i + 1) * step, x0 - i * step
        thin = .32 * u
        z0, z1 = 7.55 - t + thin, 8.45 + t - thin
        if height < 1.2:  # the hanging point is pure cutting edge
            parts.append(box("crescent_tip", [xa, bottom, 7.9 - t / 2], [xb, top, 8.1 + t / 2], "edge"))
            continue
        parts.append(box("crescent", [xa, bottom + .55, z0], [xb, top - .45, z1], "blade"))
        parts.append(box("crescent_spine", [xa, top - .45, z0 - .05], [xb, top, z1 + .05], "spine"))
        parts.append(box("crescent_edge", [xa, bottom, 7.9 - t / 2], [xb, bottom + .55, 8.1 + t / 2], "edge"))
        if bankai and i % 3 == 1 and u < .85:  # glowing red veins running across the blade
            mid = bottom + height * (.35 + .25 * math.sin(i))
            parts.append(box("berserk_crack", [xa - .1, mid, z0 - .06], [xb + .1, mid + .7, z1 + .06], "crack", glow=True))
        if bankai:
            parts.append(box("berserk_edge_glow", [xa, bottom - .15, 7.88 - t / 2], [xb, bottom + .1, 8.12 + t / 2], "crack", glow=True))
    # Notches bitten into the edge near the base, like the anime close-ups.
    for xa, xb, depth in [(4.0, 5.0, 1.4), (1.6, 2.6, 1.1)]:
        u = (x0 - xb) / (x0 - x1)
        bottom = 31.6 - 1.5 * u - 10.5 * u ** 3 - (9.4 * (1 - u) ** .85 + .45)
        parts.append(box("edge_notch", [xa, bottom - .02, 7.4 - t], [xb, bottom + depth, 8.6 + t], "notch"))
    return parts


SEALED_TEXTURES = {"wrap": "minecraft:block/black_wool", "binding": "minecraft:block/light_gray_wool",
                   "guard": "minecraft:block/anvil", "blade": "minecraft:block/iron_block",
                   "spine": "minecraft:block/light_gray_concrete", "edge": "minecraft:block/quartz_block_side",
                   "notch": "minecraft:block/gray_concrete", "particle": "minecraft:block/iron_block"}
SHIKAI_TEXTURES = dict(SEALED_TEXTURES, wrap="minecraft:block/white_wool", binding="minecraft:block/brown_wool",
                       blade="minecraft:block/iron_block", spine="minecraft:block/gray_concrete",
                       gold="minecraft:block/gold_block", tassel="minecraft:block/green_wool",
                       notch="minecraft:block/black_concrete")
BANKAI_TEXTURES = dict(SHIKAI_TEXTURES, blade="minecraft:block/polished_blackstone",
                       spine="minecraft:block/blackstone", edge="minecraft:block/red_concrete",
                       binding="minecraft:block/black_wool", crack="minecraft:block/redstone_block",
                       particle="minecraft:block/red_concrete")


def display(hand, first, gui):
    return {
        "thirdperson_righthand": {"rotation": [0, -90, 55], "scale": [hand] * 3, "translation": [0, 4 + (hand - .85) * 6, .5]},
        "thirdperson_lefthand": {"rotation": [0, 90, -55], "scale": [hand] * 3, "translation": [0, 4 + (hand - .85) * 6, .5]},
        "firstperson_righthand": {"rotation": [0, -90, 25], "scale": [first] * 3, "translation": [1.13, 3.2, 1.13]},
        "firstperson_lefthand": {"rotation": [0, 90, -25], "scale": [first] * 3, "translation": [1.13, 3.2, 1.13]},
        "gui": {"rotation": [0, 0, 0], "scale": [gui] * 3, "translation": [-3, -3, 0]},
        "fixed": {"rotation": [0, 0, 0], "scale": [gui] * 3, "translation": [-3, -3, 0]},
        "ground": {"scale": [.4] * 3, "translation": [0, 3, 0]},
    }


def write(name, textures, elements, disp, overrides=None):
    model = {"gui_light": "front", "textures": textures,
             "display": disp, "elements": elements}
    if overrides:
        model["overrides"] = overrides
    (OUT / f"{name}.json").write_text(json.dumps(model, indent=1) + "\n", encoding="utf-8")


if __name__ == "__main__":
    write("nozarashi", SEALED_TEXTURES, sealed(), display(1.0, .72, .6), [
        {"predicate": {"bleachmod:form": 1}, "model": "bleachmod:item/nozarashi_shikai"},
        {"predicate": {"bleachmod:form": 2}, "model": "bleachmod:item/nozarashi_bankai"}])
    # Nozarashi towers over its wielder: about twice the katanas in hand.
    write("nozarashi_shikai", SHIKAI_TEXTURES, cleaver(False), display(1.7, .85, .42))
    write("nozarashi_bankai", BANKAI_TEXTURES, cleaver(True), display(1.85, .9, .42))
    print("Nozarashi models written to", OUT)
