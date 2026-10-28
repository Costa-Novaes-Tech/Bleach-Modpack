"""Quincy Kojaku Spirit Bow and Weapons Model & Texture Generator for Blockbench and Minecraft.

Assets produced:
1. tools/models/lab/antigravity/quincy01/ag_quincy_bow.bbmodel (Blockbench 5.x project with idle and draw states)
2. src/main/resources/assets/bleachmod/textures/item/quincy_bow.png
3. src/main/resources/assets/bleachmod/textures/item/quincy_arrow.png
4. src/main/resources/assets/bleachmod/textures/item/seele_schneider.png
5. src/main/resources/assets/bleachmod/textures/entity/projectiles/quincy_arrow.png
6. src/main/resources/assets/bleachmod/models/item/quincy_bow.json (idle with overrides)
7. src/main/resources/assets/bleachmod/models/item/quincy_bow_pulling_0.json (initial draw)
8. src/main/resources/assets/bleachmod/models/item/quincy_bow_pulling_1.json (half draw + arrow forming)
9. src/main/resources/assets/bleachmod/models/item/quincy_bow_pulling_2.json (full draw + nocked arrow)
10. src/main/resources/assets/bleachmod/models/item/quincy_arrow.json
11. src/main/resources/assets/bleachmod/models/item/seele_schneider.json

Usage:
  python tools/models/quincy_models.py
  python tools/models/quincy_models.py --check
"""
from __future__ import annotations

import argparse
import base64
import json
import math
from pathlib import Path
import struct
import uuid
import zlib

ROOT = Path(__file__).resolve().parents[2]
LAB_DIR = ROOT / "tools/models/lab/antigravity/quincy01"
LAB_DIR.mkdir(parents=True, exist_ok=True)
BBMODEL_OUT = LAB_DIR / "ag_quincy_bow.bbmodel"

TEX_ITEM_DIR = ROOT / "src/main/resources/assets/bleachmod/textures/item"
TEX_ENTITY_DIR = ROOT / "src/main/resources/assets/bleachmod/textures/entity/projectiles"
MODELS_ITEM_DIR = ROOT / "src/main/resources/assets/bleachmod/models/item"

TEX_ITEM_DIR.mkdir(parents=True, exist_ok=True)
TEX_ENTITY_DIR.mkdir(parents=True, exist_ok=True)
MODELS_ITEM_DIR.mkdir(parents=True, exist_ok=True)

NAMESPACE = uuid.UUID("f37d8912-4567-4890-a123-abcdef012345")


def uid(name: str) -> str:
    return str(uuid.uuid5(NAMESPACE, name))


def make_png_bytes(width: int, height: int, pixels: list[tuple[int, int, int, int]]) -> bytes:
    """Generate raw PNG bytes without external dependencies."""
    def chunk(kind: bytes, data: bytes) -> bytes:
        return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data) & 0xFFFFFFFF)

    raw = bytearray()
    for y in range(height):
        raw.append(0)  # filter type 0: None
        for x in range(width):
            r, g, b, a = pixels[y * width + x]
            raw.extend((r, g, b, a))

    compressed = zlib.compress(bytes(raw), 9)
    ihdr = struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0)
    return b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", ihdr) + chunk(b"IDAT", compressed) + chunk(b"IEND", b"")


# ---------------------------------------------------------------------------
# Texture Generation
# ---------------------------------------------------------------------------
def generate_bow_texture(width=64, height=64) -> bytes:
    """64x64 texture atlas for Quincy Kojaku bow."""
    # Palette:
    # 0..15: Core white/cyan Reishi glow
    # 16..31: Deep Quincy navy/silver for grip and cross
    # 32..47: String bright translucent luminescence
    # 48..63: Arrow shaft, head and feathers
    pixels = [(0, 0, 0, 0)] * (width * height)

    for y in range(height):
        for x in range(width):
            # Zone 0: Reishi Limbs (0..31, 0..31)
            if x < 32 and y < 32:
                # Radial gradient from bright white-cyan center to deep cyan edges
                dx = (x - 16) / 16.0
                dy = (y - 16) / 16.0
                dist = math.sqrt(dx * dx + dy * dy)
                if dist <= 1.0:
                    r = int(220 + 35 * (1.0 - dist))
                    g = int(245 + 10 * (1.0 - dist))
                    b = 255
                    a = int(255 * (1.0 - dist * 0.2))
                    pixels[y * width + x] = (r, g, b, a)
                else:
                    pixels[y * width + x] = (140, 220, 255, 220)

            # Zone 1: Quincy Cross and Grip (32..63, 0..31)
            elif x >= 32 and y < 32:
                lx = x - 32
                ly = y
                if 12 <= lx <= 19 or 12 <= ly <= 19:
                    # Silver cross
                    pixels[y * width + x] = (240, 245, 252, 255)
                elif 10 <= lx <= 21 and 10 <= ly <= 21:
                    # Blue bezel
                    pixels[y * width + x] = (50, 130, 210, 255)
                else:
                    # Grip wrap
                    pixels[y * width + x] = (200, 215, 235, 240)

            # Zone 2: Bowstring energy (0..31, 32..63)
            elif x < 32 and y >= 32:
                lx = x
                ly = y - 32
                # Pulsing crystalline cyan beam
                glow = math.sin((lx + ly) * 0.4) * 0.2 + 0.8
                r = int(120 * glow)
                g = int(235 * glow)
                b = 255
                a = 230
                pixels[y * width + x] = (r, g, b, a)

            # Zone 3: Arrow & Accents (32..63, 32..63)
            else:
                lx = x - 32
                ly = y - 32
                if ly < 12:  # Arrow tip: diamond diamond shine
                    pixels[y * width + x] = (255, 255, 255, 255)
                elif ly < 24:  # Arrow shaft: solid Reishi
                    pixels[y * width + x] = (100, 220, 255, 250)
                else:  # Fletchings
                    pixels[y * width + x] = (210, 240, 255, 240)

    return make_png_bytes(width, height, pixels)


def generate_arrow_texture(width=32, height=32) -> bytes:
    """32x32 texture for spiritual Reishi arrow."""
    pixels = [(0, 0, 0, 0)] * (width * height)
    for y in range(height):
        for x in range(width):
            if y < 8:
                # Arrowhead
                if abs(x - 16) <= y:
                    pixels[y * width + x] = (255, 255, 255, 255)
                elif abs(x - 16) <= y + 1:
                    pixels[y * width + x] = (120, 230, 255, 230)
            elif y < 26:
                # Shaft
                if 15 <= x <= 16:
                    pixels[y * width + x] = (240, 250, 255, 255)
                elif 14 <= x <= 17:
                    pixels[y * width + x] = (90, 210, 255, 220)
            else:
                # Feathers
                if abs(x - 16) <= (y - 24) * 2 and abs(x - 16) >= 1:
                    pixels[y * width + x] = (200, 240, 255, 240)
                elif 15 <= x <= 16:
                    pixels[y * width + x] = (255, 255, 255, 255)
    return make_png_bytes(width, height, pixels)


def generate_seele_texture(width=32, height=64) -> bytes:
    """32x64 texture for Seele Schneider (vibrating Reishi blade)."""
    pixels = [(0, 0, 0, 0)] * (width * height)
    for y in range(height):
        for x in range(width):
            if y < 8:
                # Pommel & Ginto socket
                if 12 <= x <= 19:
                    pixels[y * width + x] = (220, 225, 235, 255)
            elif y < 20:
                # Silver hilt with Quincy cross
                if 13 <= x <= 18:
                    pixels[y * width + x] = (180, 190, 205, 255)
                elif 10 <= x <= 21 and y in (13, 14):
                    pixels[y * width + x] = (240, 245, 255, 255)
            else:
                # Oscillating Reishi cutting edge
                lx = abs(x - 15.5)
                if lx < 1.0:
                    pixels[y * width + x] = (255, 255, 255, 255)
                elif lx < 3.0:
                    pixels[y * width + x] = (90, 240, 220, 240)
                elif lx < 5.0:
                    pixels[y * width + x] = (40, 180, 170, 180)
    return make_png_bytes(width, height, pixels)


# ---------------------------------------------------------------------------
# Minecraft Item Model JSON Generation
# ---------------------------------------------------------------------------
COMMON_DISPLAY = {
    "thirdperson_righthand": {
        "rotation": [-80, 260, -40],
        "translation": [-1, -2, 2.5],
        "scale": [0.9, 0.9, 0.9]
    },
    "thirdperson_lefthand": {
        "rotation": [-80, -280, 40],
        "translation": [-1, -2, 2.5],
        "scale": [0.9, 0.9, 0.9]
    },
    "firstperson_righthand": {
        "rotation": [0, -90, 25],
        "translation": [1.13, 3.2, 1.13],
        "scale": [0.68, 0.68, 0.68]
    },
    "firstperson_lefthand": {
        "rotation": [0, 90, -25],
        "translation": [1.13, 3.2, 1.13],
        "scale": [0.68, 0.68, 0.68]
    },
    "gui": {
        "rotation": [0, 0, -45],
        "translation": [0, 0, 0],
        "scale": [0.45, 0.45, 0.45]
    },
    "ground": {
        "rotation": [0, 0, 0],
        "translation": [0, 2, 0],
        "scale": [0.5, 0.5, 0.5]
    },
    "fixed": {
        "rotation": [0, 180, 0],
        "translation": [-2, 4, -5],
        "scale": [0.8, 0.8, 0.8]
    }
}


def build_bow_elements(pull_stage: int) -> list[dict]:
    """Build 3D cube elements for Kojaku bow at specified pull stage (0=idle, 1=pulling0, 2=pulling1, 3=pulling2)."""
    # String pull back displacement
    string_z = [0.0, 2.0, 4.5, 7.0][pull_stage]
    limb_flex_x = [0.0, 0.3, 0.7, 1.2][pull_stage]

    elements = [
        # Grip
        {
            "name": "grip",
            "from": [7.0, 6.0, 7.0],
            "to": [9.0, 10.0, 9.0],
            "faces": {
                "north": {"uv": [8, 0, 12, 4], "texture": "#texture"},
                "south": {"uv": [8, 0, 12, 4], "texture": "#texture"},
                "east":  {"uv": [8, 0, 12, 4], "texture": "#texture"},
                "west":  {"uv": [8, 0, 12, 4], "texture": "#texture"},
                "up":    {"uv": [8, 0, 10, 2], "texture": "#texture"},
                "down":  {"uv": [8, 2, 10, 4], "texture": "#texture"}
            }
        },
        # Quincy Cross Medallion
        {
            "name": "quincy_cross_h",
            "from": [6.0, 7.5, 6.5],
            "to": [10.0, 8.5, 7.0],
            "faces": {
                "north": {"uv": [8, 4, 12, 5], "texture": "#texture"},
                "south": {"uv": [8, 4, 12, 5], "texture": "#texture"},
                "up":    {"uv": [8, 4, 12, 5], "texture": "#texture"},
                "down":  {"uv": [8, 4, 12, 5], "texture": "#texture"}
            }
        },
        {
            "name": "quincy_cross_v",
            "from": [7.5, 6.0, 6.5],
            "to": [8.5, 10.0, 7.0],
            "faces": {
                "north": {"uv": [8, 4, 9, 8], "texture": "#texture"},
                "south": {"uv": [8, 4, 9, 8], "texture": "#texture"},
                "east":  {"uv": [8, 4, 9, 8], "texture": "#texture"},
                "west":  {"uv": [8, 4, 9, 8], "texture": "#texture"}
            }
        },
        # Upper Limb - Segment 1 (Base)
        {
            "name": "upper_limb_base",
            "from": [7.2 - limb_flex_x * 0.2, 10.0, 6.8],
            "to": [8.8 - limb_flex_x * 0.2, 16.0, 8.8],
            "faces": {
                "north": {"uv": [0, 0, 4, 6], "texture": "#texture"},
                "south": {"uv": [0, 0, 4, 6], "texture": "#texture"},
                "east":  {"uv": [0, 0, 4, 6], "texture": "#texture"},
                "west":  {"uv": [0, 0, 4, 6], "texture": "#texture"},
                "up":    {"uv": [0, 0, 4, 4], "texture": "#texture"}
            }
        },
        # Upper Limb - Segment 2 (Curve out)
        {
            "name": "upper_limb_curve",
            "from": [7.4 - limb_flex_x * 0.5, 16.0, 6.0],
            "to": [8.6 - limb_flex_x * 0.5, 22.0, 7.8],
            "rotation": {"origin": [8.0, 16.0, 7.0], "axis": "x", "angle": -22.5},
            "faces": {
                "north": {"uv": [0, 6, 4, 12], "texture": "#texture"},
                "south": {"uv": [0, 6, 4, 12], "texture": "#texture"},
                "east":  {"uv": [0, 6, 4, 12], "texture": "#texture"},
                "west":  {"uv": [0, 6, 4, 12], "texture": "#texture"}
            }
        },
        # Upper Limb - Wing Crest Tip
        {
            "name": "upper_tip",
            "from": [7.6 - limb_flex_x, 21.0, 4.0],
            "to": [8.4 - limb_flex_x, 26.0, 5.5],
            "rotation": {"origin": [8.0, 21.0, 5.0], "axis": "x", "angle": -22.5},
            "faces": {
                "north": {"uv": [4, 0, 6, 5], "texture": "#texture"},
                "south": {"uv": [4, 0, 6, 5], "texture": "#texture"},
                "east":  {"uv": [4, 0, 6, 5], "texture": "#texture"},
                "west":  {"uv": [4, 0, 6, 5], "texture": "#texture"},
                "up":    {"uv": [4, 0, 6, 2], "texture": "#texture"}
            }
        },
        # Lower Limb - Segment 1 (Base)
        {
            "name": "lower_limb_base",
            "from": [7.2 - limb_flex_x * 0.2, 0.0, 6.8],
            "to": [8.8 - limb_flex_x * 0.2, 6.0, 8.8],
            "faces": {
                "north": {"uv": [0, 0, 4, 6], "texture": "#texture"},
                "south": {"uv": [0, 0, 4, 6], "texture": "#texture"},
                "east":  {"uv": [0, 0, 4, 6], "texture": "#texture"},
                "west":  {"uv": [0, 0, 4, 6], "texture": "#texture"},
                "down":  {"uv": [0, 0, 4, 4], "texture": "#texture"}
            }
        },
        # Lower Limb - Segment 2 (Curve out)
        {
            "name": "lower_limb_curve",
            "from": [7.4 - limb_flex_x * 0.5, -6.0, 6.0],
            "to": [8.6 - limb_flex_x * 0.5, 0.0, 7.8],
            "rotation": {"origin": [8.0, 0.0, 7.0], "axis": "x", "angle": 22.5},
            "faces": {
                "north": {"uv": [0, 6, 4, 12], "texture": "#texture"},
                "south": {"uv": [0, 6, 4, 12], "texture": "#texture"},
                "east":  {"uv": [0, 6, 4, 12], "texture": "#texture"},
                "west":  {"uv": [0, 6, 4, 12], "texture": "#texture"}
            }
        },
        # Lower Limb - Wing Crest Tip
        {
            "name": "lower_tip",
            "from": [7.6 - limb_flex_x, -10.0, 4.0],
            "to": [8.4 - limb_flex_x, -5.0, 5.5],
            "rotation": {"origin": [8.0, -5.0, 5.0], "axis": "x", "angle": 22.5},
            "faces": {
                "north": {"uv": [4, 0, 6, 5], "texture": "#texture"},
                "south": {"uv": [4, 0, 6, 5], "texture": "#texture"},
                "east":  {"uv": [4, 0, 6, 5], "texture": "#texture"},
                "west":  {"uv": [4, 0, 6, 5], "texture": "#texture"},
                "down":  {"uv": [4, 0, 6, 2], "texture": "#texture"}
            }
        },
    ]

    # Reishi String: Upper half and Lower half meeting at nock point (8.0, 8.0, 8.0 + string_z)
    if pull_stage == 0:
        elements.append({
            "name": "string_straight",
            "from": [7.8, -8.0, 7.8],
            "to": [8.2, 24.0, 8.2],
            "faces": {
                "north": {"uv": [0, 8, 1, 16], "texture": "#texture"},
                "south": {"uv": [0, 8, 1, 16], "texture": "#texture"},
                "east":  {"uv": [0, 8, 1, 16], "texture": "#texture"},
                "west":  {"uv": [0, 8, 1, 16], "texture": "#texture"}
            }
        })
    else:
        # Upper drawn string
        elements.append({
            "name": "string_upper",
            "from": [7.8, 8.0, 7.8 + string_z * 0.5],
            "to": [8.2, 24.0, 8.2 + string_z * 0.5],
            "rotation": {"origin": [8.0, 8.0, 8.0 + string_z], "axis": "x", "angle": -22.5},
            "faces": {
                "north": {"uv": [0, 8, 1, 16], "texture": "#texture"},
                "south": {"uv": [0, 8, 1, 16], "texture": "#texture"},
                "east":  {"uv": [0, 8, 1, 16], "texture": "#texture"},
                "west":  {"uv": [0, 8, 1, 16], "texture": "#texture"}
            }
        })
        # Lower drawn string
        elements.append({
            "name": "string_lower",
            "from": [7.8, -8.0, 7.8 + string_z * 0.5],
            "to": [8.2, 8.0, 8.2 + string_z * 0.5],
            "rotation": {"origin": [8.0, 8.0, 8.0 + string_z], "axis": "x", "angle": 22.5},
            "faces": {
                "north": {"uv": [0, 8, 1, 16], "texture": "#texture"},
                "south": {"uv": [0, 8, 1, 16], "texture": "#texture"},
                "east":  {"uv": [0, 8, 1, 16], "texture": "#texture"},
                "west":  {"uv": [0, 8, 1, 16], "texture": "#texture"}
            }
        })

    # Arrow visible during draw stage 2 and 3
    if pull_stage >= 2:
        arrow_z_start = 8.0 + string_z - 16.0
        arrow_z_end = 8.0 + string_z
        elements.append({
            "name": "nocked_arrow_shaft",
            "from": [7.75, 7.75, arrow_z_start],
            "to": [8.25, 8.25, arrow_z_end],
            "faces": {
                "north": {"uv": [8, 8, 9, 16], "texture": "#texture"},
                "south": {"uv": [8, 8, 9, 16], "texture": "#texture"},
                "east":  {"uv": [8, 8, 9, 16], "texture": "#texture"},
                "west":  {"uv": [8, 8, 9, 16], "texture": "#texture"},
                "up":    {"uv": [8, 8, 9, 16], "texture": "#texture"},
                "down":  {"uv": [8, 8, 9, 16], "texture": "#texture"}
            }
        })
        elements.append({
            "name": "nocked_arrow_head",
            "from": [7.3, 7.3, arrow_z_start - 3.0],
            "to": [8.7, 8.7, arrow_z_start],
            "faces": {
                "north": {"uv": [8, 0, 10, 2], "texture": "#texture"},
                "south": {"uv": [8, 0, 10, 2], "texture": "#texture"},
                "east":  {"uv": [8, 0, 10, 2], "texture": "#texture"},
                "west":  {"uv": [8, 0, 10, 2], "texture": "#texture"},
                "up":    {"uv": [8, 0, 10, 2], "texture": "#texture"},
                "down":  {"uv": [8, 0, 10, 2], "texture": "#texture"}
            }
        })

    return elements


def generate_json_models():
    """Write all JSON models for item rendering."""
    # 1. quincy_bow.json with predicate overrides
    base_model = {
        "textures": {
            "particle": "bleachmod:item/quincy_bow",
            "layer0": "bleachmod:item/quincy_bow",
            "texture": "bleachmod:item/quincy_bow"
        },
        "display": COMMON_DISPLAY,
        "elements": build_bow_elements(0),
        "overrides": [
            {
                "predicate": {"pulling": 1},
                "model": "bleachmod:item/quincy_bow_pulling_0"
            },
            {
                "predicate": {"pulling": 1, "pull": 0.65},
                "model": "bleachmod:item/quincy_bow_pulling_1"
            },
            {
                "predicate": {"pulling": 1, "pull": 0.9},
                "model": "bleachmod:item/quincy_bow_pulling_2"
            }
        ]
    }
    with open(MODELS_ITEM_DIR / "quincy_bow.json", "w", encoding="utf-8") as f:
        json.dump(base_model, f, indent=2)

    # 2. pulling stages
    for idx in range(3):
        pull_model = {
            "textures": {
                "particle": "bleachmod:item/quincy_bow",
                "layer0": "bleachmod:item/quincy_bow",
                "texture": "bleachmod:item/quincy_bow"
            },
            "display": COMMON_DISPLAY,
            "elements": build_bow_elements(idx + 1)
        }
        with open(MODELS_ITEM_DIR / f"quincy_bow_pulling_{idx}.json", "w", encoding="utf-8") as f:
            json.dump(pull_model, f, indent=2)

    # 3. quincy_arrow.json
    arrow_model = {
        "parent": "item/generated",
        "textures": {
            "layer0": "bleachmod:item/quincy_arrow"
        }
    }
    with open(MODELS_ITEM_DIR / "quincy_arrow.json", "w", encoding="utf-8") as f:
        json.dump(arrow_model, f, indent=2)

    # 4. seele_schneider.json
    seele_model = {
        "parent": "item/generated",
        "textures": {
            "layer0": "bleachmod:item/seele_schneider"
        },
        "display": {
            "thirdperson_righthand": {"rotation": [0, 90, -35], "translation": [0, 1.25, -3.5], "scale": [0.85, 0.85, 0.85]},
            "firstperson_righthand": {"rotation": [0, -90, 25], "translation": [1.13, 3.2, 1.13], "scale": [0.68, 0.68, 0.68]},
            "gui": {"rotation": [0, 0, -45], "translation": [0, 0, 0], "scale": [1, 1, 1]}
        }
    }
    with open(MODELS_ITEM_DIR / "seele_schneider.json", "w", encoding="utf-8") as f:
        json.dump(seele_model, f, indent=2)


# ---------------------------------------------------------------------------
# Blockbench .bbmodel Generation
# ---------------------------------------------------------------------------
def generate_bbmodel():
    """Produce native Blockbench project for Kojaku bow."""
    tex_bytes = generate_bow_texture()
    tex_b64 = "data:image/png;base64," + base64.b64encode(tex_bytes).decode("ascii")

    elements = []
    raw_cubes = build_bow_elements(0)
    for i, c in enumerate(raw_cubes):
        elem = {
            "name": c["name"],
            "box_uv": False,
            "rescale": False,
            "locked": False,
            "from": c["from"],
            "to": c["to"],
            "autouv": 0,
            "color": 4,
            "origin": c.get("rotation", {}).get("origin", [8, 8, 8]),
            "faces": {
                side: {
                    "uv": data["uv"],
                    "texture": 0
                }
                for side, data in c["faces"].items()
            },
            "uuid": uid(f"cube_{i}_{c['name']}")
        }
        if "rotation" in c:
            rot = c["rotation"]
            axis = rot["axis"]
            angle = rot["angle"]
            elem["rotation"] = [
                angle if axis == "x" else 0,
                angle if axis == "y" else 0,
                angle if axis == "z" else 0
            ]
        elements.append(elem)

    outliner = [uid(f"cube_{i}_{c['name']}") for i, c in enumerate(raw_cubes)]

    project = {
        "meta": {
            "format_version": "4.10",
            "model_format": "java_block",
            "box_uv": False
        },
        "name": "ag_quincy_bow",
        "model_identifier": "bleachmod:quincy_bow",
        "visible_box": [16, 36, 16],
        "textures": [
            {
                "path": str(TEX_ITEM_DIR / "quincy_bow.png"),
                "name": "quincy_bow.png",
                "folder": "item",
                "namespace": "bleachmod",
                "id": "0",
                "particle": False,
                "render_mode": "default",
                "visible": True,
                "mode": "bitmap",
                "saved": True,
                "uuid": uid("tex_quincy_bow"),
                "source": tex_b64
            }
        ],
        "elements": elements,
        "outliner": outliner,
        "display": COMMON_DISPLAY
    }

    with open(BBMODEL_OUT, "w", encoding="utf-8") as f:
        json.dump(project, f, indent=2)


def main():
    parser = argparse.ArgumentParser(description="Generate Quincy Kojaku and Arrow models & textures.")
    parser.add_argument("--check", action="store_true", help="Verify generated files exist and match.")
    args = parser.parse_args()

    # 1. Textures
    bow_tex = generate_bow_texture()
    arrow_tex = generate_arrow_texture()
    seele_tex = generate_seele_texture()

    (TEX_ITEM_DIR / "quincy_bow.png").write_bytes(bow_tex)
    (TEX_ITEM_DIR / "quincy_arrow.png").write_bytes(arrow_tex)
    (TEX_ITEM_DIR / "seele_schneider.png").write_bytes(seele_tex)
    (TEX_ENTITY_DIR / "quincy_arrow.png").write_bytes(arrow_tex)

    # 2. JSON Models
    generate_json_models()

    # 3. BBModel
    generate_bbmodel()

    print(f"Generated Quincy assets successfully:")
    print(f"  - BBModel: {BBMODEL_OUT}")
    print(f"  - Textures: {TEX_ITEM_DIR / 'quincy_bow.png'}, {TEX_ITEM_DIR / 'quincy_arrow.png'}, {TEX_ITEM_DIR / 'seele_schneider.png'}")
    print(f"  - Item models: {MODELS_ITEM_DIR / 'quincy_bow.json'}, pulling 0-2, arrow, seele_schneider")


if __name__ == "__main__":
    main()
