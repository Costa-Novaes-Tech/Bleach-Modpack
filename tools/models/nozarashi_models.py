"""Generate Nozarashi's sealed, Shikai and red-cracked Bankai item models.

Run from the repository root: ``python tools/models/nozarashi_models.py``.
The three forms intentionally have different silhouettes. All parts share the
project's -45 degree item rotation and only use vanilla Minecraft textures.
"""
import json
from pathlib import Path

OUT = Path(__file__).resolve().parents[2] / "src/main/resources/assets/bleachmod/models/item"
ROTATION = {"angle": -45, "axis": "z", "origin": [8, 8, 8]}
GLOW = {"block_light": 15, "sky_light": 15, "ambient_occlusion": False}


def box(name, frm, to, texture, glow=False):
    """Create one cuboid accepted by the vanilla item-model loader."""
    frm = [round(value, 3) for value in frm]
    to = [round(value, 3) for value in to]
    sx, sy, sz = (min(16, max(.1, to[i] - frm[i])) for i in range(3))
    uv = {
        "north": [0, 0, sx, sy], "south": [0, 0, sx, sy],
        "east": [0, 0, sz, sy], "west": [0, 0, sz, sy],
        "up": [0, 0, sx, sz], "down": [0, 0, sx, sz],
    }
    element = {
        "name": name, "from": frm, "to": to, "rotation": ROTATION,
        "faces": {
            face: {"uv": [round(value, 3) for value in coords], "texture": f"#{texture}"}
            for face, coords in uv.items()
        },
    }
    if glow:
        element["forge_data"] = GLOW
    return element


def sample(points, position):
    """Linearly sample a normalized profile described by (position, value)."""
    for (p0, value0), (p1, value1) in zip(points, points[1:]):
        if position <= p1:
            amount = (position - p0) / (p1 - p0)
            return value0 + (value1 - value0) * amount
    return points[-1][1]


def wrapped_handle(top, width, wrap, binding, band_step=1.25):
    lo, hi = 8 - width / 2, 8 + width / 2
    parts = [box("grip", [lo, -.7, lo], [hi, top, hi], wrap)]
    y, band = -.15, 0
    while y < top - .5:
        inset = .08 if band % 2 else 0
        parts.append(box("grip_binding", [lo - .07 + inset, y, lo - .07],
                         [hi + .07, min(top, y + .32), hi + .07 - inset], binding))
        y += band_step
        band += 1
    return parts


def sealed():
    """The battered, oversized katana Zaraki carries before calling Nozarashi."""
    parts = wrapped_handle(7.6, 1.35, "sealed_wrap", "sealed_binding", 1.05)
    parts += [
        box("worn_pommel", [7.05, -1.35, 7.05], [8.95, -.65, 8.95], "worn_gold"),
        box("worn_tsuba", [5.9, 7.5, 7.05], [10.1, 8.2, 8.95], "worn_gold"),
        box("habaki", [7.15, 8.15, 7.45], [8.85, 9.15, 8.55], "dark_steel"),
    ]
    blade_segments = [(9.1, 14.2, 0), (14.2, 19.2, .12), (19.2, 24.0, .32),
                      (24.0, 28.2, .6), (28.2, 30.6, .95)]
    chips = [(10.7, 11.35), (13.1, 13.55), (16.0, 16.8), (18.3, 18.8),
             (21.5, 22.25), (25.0, 25.85), (27.35, 27.9)]
    for y0, y1, shift in blade_segments:
        parts.append(box("scarred_blade", [7.35 + shift, y0, 7.55],
                         [8.55 + shift, y1, 8.45], "dark_steel"))
        parts.append(box("heavy_spine", [8.48 + shift, y0, 7.45],
                         [8.7 + shift, y1, 8.55], "spine"))
    edge_y = 9.1
    for chip_start, chip_end in chips + [(30.6, 30.6)]:
        if edge_y < chip_start:
            shift = next(value for start, end, value in blade_segments if start <= edge_y < end)
            parts.append(box("broken_edge", [7.13 + shift, edge_y, 7.82],
                             [7.39 + shift, chip_start, 8.18], "bright_edge"))
        edge_y = chip_end
    for chip_start, chip_end in chips:
        shift = next(value for start, end, value in blade_segments if start <= chip_start < end)
        parts.append(box("edge_bite", [7.32 + shift, chip_start, 7.48],
                         [7.78 + shift, chip_end, 8.52], "notch"))
    parts += [
        box("broken_kissaki", [8.15, 28.9, 7.6], [9.45, 30.75, 8.4], "dark_steel"),
        box("broken_tip", [8.85, 30.55, 7.72], [9.45, 31.45, 8.28], "bright_edge"),
        box("blade_scar", [7.75, 17.4, 7.43], [8.45, 18.0, 8.57], "notch"),
        box("blade_scar", [8.05, 23.1, 7.43], [8.7, 23.65, 8.57], "notch"),
    ]
    return parts


def released_handle(bankai):
    """A long grip on the same longitudinal axis as the released blade."""
    wrap = "bankai_wrap" if bankai else "shikai_wrap"
    binding = "bankai_binding" if bankai else "shikai_binding"
    pommel = "bankai_spine" if bankai else "worn_gold"
    lo, hi = 7.2, 8.8
    parts = [box("released_grip", [lo, -15.0, lo], [hi, 7.0, hi], wrap),
             box("released_pommel", [6.95, -15.7, 6.95], [9.05, -14.9, 9.05], pommel)]
    y, band = -14.55, 0
    while y < 6.45:
        inset = .08 if band % 2 else 0
        parts.append(box("grip_binding", [lo - .08 + inset, y, lo - .08],
                         [hi + .08, y + .34, hi + .08 - inset], binding))
        y += 1.16
        band += 1
    return parts


def released(bankai=False):
    """Collinear released Nozarashi; Bankai reuses the silhouette with red damage effects."""
    blade = "bankai_blade" if bankai else "shikai_blade"
    spine = "bankai_spine" if bankai else "shikai_spine"
    edge = "bankai_edge" if bankai else "bright_edge"
    parts = released_handle(bankai)
    parts += [
        box("gold_ferrule", [4.7, 5.9, 6.55], [11.35, 12.6, 9.45], "gold"),
        box("ferrule_cap", [4.4, 11.85, 6.4], [11.65, 13.0, 9.6], "gold"),
        box("ferrule_shadow", [4.55, 5.8, 6.45], [5.4, 12.0, 9.55], "worn_gold"),
        box("ferrule_rivet", [8.8, 6.7, 6.35], [10.45, 8.35, 9.65], "notch"),
    ]
    for index, (x, length) in enumerate([(9.5, 5.4), (10.0, 7.0), (10.5, 6.1),
                                         (11.0, 7.7), (11.5, 4.8)]):
        z = 7.3 + (index % 3) * .45
        parts.append(box("green_tassel", [x, 6.6 - length, z],
                         [x + .34, 6.75, z + .34], "tassel"))
        if index % 2:
            parts.append(box("tassel_knot", [x - .12, 5.95, z - .1],
                             [x + .48, 6.8, z + .44], "tassel_dark"))

    # Longitudinal blade: the grip continues into its base at y=9 and the
    # weapon grows toward +Y.  Width is kept through 90% of the head; only the
    # short kissaki closes, preserving the massive cleaver silhouette.
    y_base, y_tip, step = 9.0, 31.6, .72
    left_profile = [(0, -.8), (.18, -1.8), (.5, -3.25), (.78, -4.0),
                    (.9, -3.5), (.97, 2.0), (1, 7.15)]
    right_profile = [(0, 11.0), (.2, 11.35), (.5, 11.15), (.78, 10.4),
                     (.92, 9.6), (1, 8.65)]
    count = round((y_tip - y_base) / step)
    crack_anchors = {8: 1.7, 15: 3.5, 22: .4}
    for index in range(count):
        u = (index + .5) / count
        ya, yb = y_base + index * step, y_base + (index + 1) * step
        left, right = sample(left_profile, u), sample(right_profile, u)
        thickness = 2.55 - .55 * u
        z0, z1 = 8 - thickness / 2, 8 + thickness / 2
        edge_width = min(1.0, max(.45, (right - left) * .12))
        parts.append(box("cleaver_body", [left + edge_width - .08, ya, z0],
                         [right - .58, yb, z1], blade))
        parts.append(box("cutting_edge", [left, ya, 7.32],
                         [left + edge_width, yb, 8.68], edge))
        parts.append(box("heavy_spine", [right - .65, ya, z0 - .14],
                         [right, yb, z1 + .14], spine))
        if bankai and index in crack_anchors:
            start = max(left + edge_width + .25, crack_anchors[index])
            end = min(right - .8, start + 3.8)
            parts.append(box("glowing_crack", [start, ya + .18, z0 - .08],
                             [end, ya + .46, z1 + .08], "crack", glow=True))
            if end + .2 < right - .45:
                parts.append(box("glowing_crack_branch", [end - .15, ya + .42, z0 - .08],
                                 [min(right - .45, end + 1.25), ya + .68, z1 + .08], "crack", glow=True))
    # Small heel damage from the reference, without changing the weapon axis.
    parts += [
        box("heel_notch", [-.65, 10.0, 6.95], [.35, 11.25, 9.05], "notch"),
        box("heel_notch", [-1.45, 12.15, 7.05], [-.45, 13.1, 8.95], "notch"),
    ]
    return parts


def shikai():
    return released(False)


def bankai():
    return released(True)


SEALED_TEXTURES = {
    "sealed_wrap": "minecraft:block/brown_wool", "sealed_binding": "minecraft:block/light_gray_wool",
    "worn_gold": "minecraft:block/raw_gold_block", "dark_steel": "minecraft:block/polished_deepslate",
    "spine": "minecraft:block/deepslate_tiles", "bright_edge": "minecraft:block/quartz_block_side",
    "notch": "minecraft:block/black_concrete", "particle": "minecraft:block/polished_deepslate",
}
SHAIKAI_TEXTURES = {
    "shikai_wrap": "minecraft:block/light_gray_wool", "shikai_binding": "minecraft:block/brown_wool",
    "worn_gold": "minecraft:block/raw_gold_block", "gold": "minecraft:block/gold_block",
    "tassel": "minecraft:block/green_wool", "tassel_dark": "minecraft:block/green_concrete",
    "shikai_blade": "minecraft:block/black_concrete", "shikai_spine": "minecraft:block/polished_blackstone",
    "bright_edge": "minecraft:block/quartz_block_side", "notch": "minecraft:block/blackstone",
    "particle": "minecraft:block/black_concrete",
}
BANKAI_TEXTURES = {
    "bankai_wrap": "minecraft:block/white_wool", "bankai_binding": "minecraft:block/light_gray_concrete",
    "bankai_blade": "minecraft:block/polished_blackstone", "bankai_spine": "minecraft:block/obsidian",
    "bankai_edge": "minecraft:block/red_concrete", "crack": "minecraft:block/redstone_block",
    "gold": "minecraft:block/gold_block", "worn_gold": "minecraft:block/raw_gold_block",
    "tassel": "minecraft:block/green_wool", "tassel_dark": "minecraft:block/green_concrete",
    "notch": "minecraft:block/black_concrete", "particle": "minecraft:block/red_concrete",
}


def display(hand, first_person, gui, translation=None):
    translation = translation if translation is not None else 4 + (hand - .85) * 6
    return {
        "thirdperson_righthand": {"rotation": [0, -90, 55], "scale": [hand] * 3,
                                  "translation": [0, translation, .5]},
        "thirdperson_lefthand": {"rotation": [0, 90, -55], "scale": [hand] * 3,
                                 "translation": [0, translation, .5]},
        "firstperson_righthand": {"rotation": [0, -90, 25], "scale": [first_person] * 3,
                                  "translation": [1.13, 3.2, 1.13]},
        "firstperson_lefthand": {"rotation": [0, 90, -25], "scale": [first_person] * 3,
                                 "translation": [1.13, 3.2, 1.13]},
        "gui": {"rotation": [0, 0, 0], "scale": [gui] * 3, "translation": [-3, -3, 0]},
        "fixed": {"rotation": [0, 0, 0], "scale": [gui] * 3, "translation": [-3, -3, 0]},
        "ground": {"scale": [.4] * 3, "translation": [0, 3, 0]},
    }


def write(name, textures, elements, model_display, overrides=None):
    model = {"gui_light": "front", "textures": textures, "display": model_display, "elements": elements}
    if overrides:
        model["overrides"] = overrides
    (OUT / f"{name}.json").write_text(json.dumps(model, indent=1) + "\n", encoding="utf-8")


if __name__ == "__main__":
    write("nozarashi", SEALED_TEXTURES, sealed(), display(1.15, .78, .58), [
        {"predicate": {"bleachmod:form": 1}, "model": "bleachmod:item/nozarashi_shikai"},
        {"predicate": {"bleachmod:form": 2}, "model": "bleachmod:item/nozarashi_bankai"},
    ])
    write("nozarashi_shikai", SHAIKAI_TEXTURES, shikai(), display(2.05, .9, .39, 10.7))
    write("nozarashi_bankai", BANKAI_TEXTURES, bankai(), display(1.78, .86, .42, 9.25))
    print("Nozarashi models written to", OUT)
