"""Master pipeline and generator for Quincy Manifestation (Vollstandig) model, rig, animations, and textures.

Generates:
  - Editable Blockbench model: tools/models/lab/antigravity/quincy_vollstandig/quincy_manifestation.bbmodel
  - GeckoLib 1.12.0 Geometry: src/main/resources/assets/bleachmod/geo/quincy_manifestation.geo.json
  - GeckoLib 1.8.0 Animations: src/main/resources/assets/bleachmod/animations/quincy_manifestation.animation.json
  - 128x128 RGBA Emissive Texture: src/main/resources/assets/bleachmod/textures/entity/quincy_manifestation.png

Features:
  - 2 articulated wing roots (wing_left_root, wing_right_root) with 3 articulated segments each.
  - 4 floating Reishi satellites (satellite_0..3) hovering in 2 distinct pairs behind and outside the body.
  - 6 distinct animation clips: emergence, idle, arsenal_left, arsenal_right, recoil, settle.
  - Full support for --check (idempotence verification) and --render-preview (generates visual verification renders).
"""
from __future__ import annotations

import argparse
import base64
import io
import json
import math
import os
from pathlib import Path
import struct
import sys
import uuid
import zlib
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[5]
SOURCE_BBMODEL = ROOT / "tools/models/lab/antigravity/quincy_vollstandig/quincy_manifestation.bbmodel"
GEO_JSON = ROOT / "src/main/resources/assets/bleachmod/geo/quincy_manifestation.geo.json"
ANIM_JSON = ROOT / "src/main/resources/assets/bleachmod/animations/quincy_manifestation.animation.json"
TEXTURE_PNG = ROOT / "src/main/resources/assets/bleachmod/textures/entity/quincy_manifestation.png"
PREVIEW_DIR = ROOT / "Docs/poderes/media/quincy-rig-20261030"

NAMESPACE = uuid.UUID("3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f")


def identity(name: str) -> str:
    return str(uuid.uuid5(NAMESPACE, name))


def clamp(v: float | int) -> int:
    return max(0, min(255, int(v)))


def create_quincy_manifestation_texture_bytes() -> bytes:
    """Generate 128x128 RGBA Reishi manifestation texture.
    
    Palette:
      - Pure white radiant core: (255, 255, 255, 255)
      - Intense cyan luminous glow: (75, 235, 255, 240)
      - Translucent crystalline Reishi: (20, 160, 230, 210)
      - Deep faceted sapphire borders: (10, 45, 95, 255)
      - Void navy accent: (6, 20, 48, 255)
    """
    width, height = 128, 128
    img = Image.new("RGBA", (width, height), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Zone 0 (0..31, 0..31): Satellite Star Cores & Prisms
    for y in range(32):
        for x in range(32):
            dx = abs(x - 15.5) / 15.5
            dy = abs(y - 15.5) / 15.5
            dist = math.sqrt(dx * dx + dy * dy)
            diamond = dx + dy
            if diamond < 0.35:
                # White radiant core
                col = (255, 255, 255, 255)
            elif diamond < 0.7:
                # Intense cyan inner glow
                f = (diamond - 0.35) / 0.35
                r = clamp(255 * (1 - f) + 75 * f)
                g = clamp(255 * (1 - f) + 235 * f)
                b = 255
                col = (r, g, b, 245)
            elif diamond < 1.0:
                # Crystalline facet body
                f = (diamond - 0.7) / 0.3
                r = clamp(75 * (1 - f) + 20 * f)
                g = clamp(235 * (1 - f) + 160 * f)
                b = clamp(255 * (1 - f) + 230 * f)
                col = (r, g, b, 220)
            elif diamond < 1.15:
                # Deep sapphire facet edge
                col = (12, 50, 105, 255)
            else:
                col = (0, 0, 0, 0)
            img.putpixel((x, y), col)

    # Zone 1 (32..63, 0..31): Satellite Cross Blades & Diamond Tips
    for y in range(32):
        for x in range(32, 64):
            local_x = x - 32
            u = local_x / 31.0
            v = y / 31.0
            # Geometric faceted pattern
            edge = min(u, 1 - u, v, 1 - v)
            if edge < 0.08:
                col = (10, 42, 90, 255)
            elif edge < 0.2:
                col = (25, 140, 215, 230)
            else:
                grad = (u + v) * 0.5
                r = clamp(180 + grad * 75)
                g = clamp(225 + grad * 30)
                b = 255
                col = (r, g, b, 240)
            img.putpixel((x, y), col)

    # Zone 2 (64..95, 0..31): Wing Root Spars (Skeletal crystalline struts)
    for y in range(32):
        for x in range(64, 96):
            local_x = x - 64
            # Diagonal crystalline facets
            diag = (local_x + y * 2) % 12
            if diag in (0, 1):
                col = (240, 252, 255, 255)  # specular light rib
            elif diag in (2, 3, 4):
                col = (50, 200, 255, 240)  # glowing body
            elif diag in (10, 11):
                col = (8, 30, 68, 255)     # dark structural frame
            else:
                col = (18, 105, 175, 225)  # translucent blue core
            img.putpixel((x, y), col)

    # Zone 3 (96..127, 0..31): Wing Mid Spars & Hinge Facets
    for y in range(32):
        for x in range(96, 128):
            local_x = x - 96
            v = y / 31.0
            edge = min(local_x / 31.0, 1 - local_x / 31.0)
            if edge < 0.06:
                col = (10, 36, 78, 255)
            else:
                r = clamp(35 + v * 160)
                g = clamp(170 + v * 80)
                b = clamp(235 + v * 20)
                a = clamp(210 + v * 45)
                col = (r, g, b, a)
            img.putpixel((x, y), col)

    # Zone 4 (0..63, 32..63): Primary Reishi Feather Blades (Long radiant quills)
    for y in range(32, 64):
        for x in range(64):
            u = x / 63.0
            v = (y - 32) / 31.0
            # Feather spine down center
            center_dist = abs(v - 0.5) * 2.0
            if center_dist < 0.15:
                # Luminescent central quill spine
                f = center_dist / 0.15
                r = clamp(255 * (1 - f) + 120 * f)
                g = clamp(255 * (1 - f) + 230 * f)
                b = 255
                col = (r, g, b, 255)
            elif center_dist < 0.7:
                # Translucent energy vane
                f = (center_dist - 0.15) / 0.55
                r = clamp(60 * (1 - f) + 15 * f)
                g = clamp(210 * (1 - f) + 120 * f)
                b = clamp(255 * (1 - f) + 210 * f)
                col = (r, g, b, 210)
            else:
                # Feather edge trim
                col = (10, 35, 75, 240)
            img.putpixel((x, y), col)

    # Zone 5 (64..127, 32..63): Outer Wingtip Quills & Flare Nodes
    for y in range(32, 64):
        for x in range(64, 128):
            local_x = x - 64
            u = local_x / 63.0
            v = (y - 32) / 31.0
            # Tapered energy blade gradient
            taper = 1.0 - (u * 0.6 + v * 0.4)
            r = clamp(255 * taper + 40 * (1 - taper))
            g = clamp(255 * taper + 180 * (1 - taper))
            b = 255
            a = clamp(255 * (0.4 + 0.6 * taper))
            img.putpixel((x, y), (r, g, b, a))

    # Zone 6 (0..127, 64..127): High-frequency Reishi lattice & specular highlights
    for y in range(64, 128):
        for x in range(128):
            u = x / 127.0
            v = (y - 64) / 63.0
            grid_x = x % 8
            grid_y = y % 8
            if grid_x == 0 or grid_y == 0:
                col = (14, 52, 105, 255)  # crystalline grid lattice
            elif grid_x == 4 and grid_y == 4:
                col = (245, 255, 255, 255) # specular energy sparkle
            else:
                col = (30, 150, 225, 195)
            img.putpixel((x, y), col)

    import io
    buf = io.BytesIO()
    img.save(buf, format="PNG")
    return buf.getvalue()


def build_geometry_and_cubes():
    """Build canonical GeckoLib 1.12.0 bone hierarchy and cubes."""
    bones = []

    # 1. root
    bones.append({
        "name": "root",
        "pivot": [0.0, 0.0, 0.0],
        "cubes": []
    })

    # ------------------ LEFT WING ------------------
    # wing_left_root (Upper left scapula anchor)
    bones.append({
        "name": "wing_left_root",
        "parent": "root",
        "pivot": [2.0, 20.0, 2.0],
        "cubes": [
            {
                "origin": [1.0, 19.0, 1.8],
                "size": [3.0, 2.0, 1.4],
                "uv": {
                    "north": {"uv": [64, 0], "uv_size": [3, 2]},
                    "south": {"uv": [67, 0], "uv_size": [3, 2]},
                    "east": {"uv": [70, 0], "uv_size": [2, 2]},
                    "west": {"uv": [72, 0], "uv_size": [2, 2]},
                    "up": {"uv": [64, 2], "uv_size": [3, 2]},
                    "down": {"uv": [67, 2], "uv_size": [3, 2]}
                }
            },
            {
                "origin": [3.5, 20.0, 2.0],
                "size": [4.5, 4.0, 1.0],
                "uv": {
                    "north": {"uv": [74, 0], "uv_size": [5, 4]},
                    "south": {"uv": [79, 0], "uv_size": [5, 4]},
                    "east": {"uv": [84, 0], "uv_size": [1, 4]},
                    "west": {"uv": [85, 0], "uv_size": [1, 4]},
                    "up": {"uv": [74, 4], "uv_size": [5, 1]},
                    "down": {"uv": [79, 4], "uv_size": [5, 1]}
                }
            }
        ]
    })

    # wing_left_mid (Primary articulated hinge)
    bones.append({
        "name": "wing_left_mid",
        "parent": "wing_left_root",
        "pivot": [8.0, 24.0, 2.0],
        "cubes": [
            # Main curved spar
            {
                "origin": [8.0, 23.5, 1.5],
                "size": [8.0, 4.5, 1.0],
                "uv": {
                    "north": {"uv": [96, 0], "uv_size": [8, 5]},
                    "south": {"uv": [104, 0], "uv_size": [8, 5]},
                    "east": {"uv": [112, 0], "uv_size": [1, 5]},
                    "west": {"uv": [113, 0], "uv_size": [1, 5]},
                    "up": {"uv": [96, 5], "uv_size": [8, 1]},
                    "down": {"uv": [104, 5], "uv_size": [8, 1]}
                }
            },
            # Trailing Reishi feather blade 0 (inner descending)
            {
                "origin": [9.0, 12.0, 1.8],
                "size": [3.0, 11.5, 0.4],
                "uv": {
                    "north": {"uv": [0, 32], "uv_size": [3, 12]},
                    "south": {"uv": [3, 32], "uv_size": [3, 12]},
                    "east": {"uv": [6, 32], "uv_size": [1, 12]},
                    "west": {"uv": [7, 32], "uv_size": [1, 12]},
                    "up": {"uv": [0, 44], "uv_size": [3, 1]},
                    "down": {"uv": [3, 44], "uv_size": [3, 1]}
                }
            },
            # Trailing Reishi feather blade 1 (mid descending)
            {
                "origin": [11.5, 14.0, 1.6],
                "size": [3.0, 11.0, 0.4],
                "uv": {
                    "north": {"uv": [8, 32], "uv_size": [3, 11]},
                    "south": {"uv": [11, 32], "uv_size": [3, 11]},
                    "east": {"uv": [14, 32], "uv_size": [1, 11]},
                    "west": {"uv": [15, 32], "uv_size": [1, 11]},
                    "up": {"uv": [8, 43], "uv_size": [3, 1]},
                    "down": {"uv": [11, 43], "uv_size": [3, 1]}
                }
            },
            # Trailing Reishi feather blade 2 (upper descending)
            {
                "origin": [14.0, 17.0, 1.4],
                "size": [2.5, 10.0, 0.4],
                "uv": {
                    "north": {"uv": [16, 32], "uv_size": [3, 10]},
                    "south": {"uv": [19, 32], "uv_size": [3, 10]},
                    "east": {"uv": [22, 32], "uv_size": [1, 10]},
                    "west": {"uv": [23, 32], "uv_size": [1, 10]},
                    "up": {"uv": [16, 42], "uv_size": [3, 1]},
                    "down": {"uv": [19, 42], "uv_size": [3, 1]}
                }
            }
        ]
    })

    # wing_left_tip (Outer wing tip articulated blades)
    bones.append({
        "name": "wing_left_tip",
        "parent": "wing_left_mid",
        "pivot": [16.0, 28.0, 1.5],
        "cubes": [
            # Main arching tip spar
            {
                "origin": [16.0, 27.5, 1.0],
                "size": [10.0, 7.5, 0.8],
                "uv": {
                    "north": {"uv": [64, 32], "uv_size": [10, 8]},
                    "south": {"uv": [74, 32], "uv_size": [10, 8]},
                    "east": {"uv": [84, 32], "uv_size": [1, 8]},
                    "west": {"uv": [85, 32], "uv_size": [1, 8]},
                    "up": {"uv": [64, 40], "uv_size": [10, 1]},
                    "down": {"uv": [74, 40], "uv_size": [10, 1]}
                }
            },
            # Outer high Reishi energy quill
            {
                "origin": [22.0, 34.0, 0.8],
                "size": [6.0, 7.0, 0.6],
                "uv": {
                    "north": {"uv": [86, 32], "uv_size": [6, 7]},
                    "south": {"uv": [92, 32], "uv_size": [6, 7]},
                    "east": {"uv": [98, 32], "uv_size": [1, 7]},
                    "west": {"uv": [99, 32], "uv_size": [1, 7]},
                    "up": {"uv": [86, 39], "uv_size": [6, 1]},
                    "down": {"uv": [92, 39], "uv_size": [6, 1]}
                }
            },
            # Outer secondary quill
            {
                "origin": [18.0, 24.0, 1.2],
                "size": [6.0, 7.0, 0.4],
                "uv": {
                    "north": {"uv": [100, 32], "uv_size": [6, 7]},
                    "south": {"uv": [106, 32], "uv_size": [6, 7]},
                    "east": {"uv": [112, 32], "uv_size": [1, 7]},
                    "west": {"uv": [113, 32], "uv_size": [1, 7]},
                    "up": {"uv": [100, 39], "uv_size": [6, 1]},
                    "down": {"uv": [106, 39], "uv_size": [6, 1]}
                }
            }
        ]
    })

    # ------------------ RIGHT WING (Symmetrical on -X) ------------------
    # wing_right_root
    bones.append({
        "name": "wing_right_root",
        "parent": "root",
        "pivot": [-2.0, 20.0, 2.0],
        "cubes": [
            {
                "origin": [-4.0, 19.0, 1.8],
                "size": [3.0, 2.0, 1.4],
                "uv": {
                    "north": {"uv": [64, 0], "uv_size": [3, 2]},
                    "south": {"uv": [67, 0], "uv_size": [3, 2]},
                    "east": {"uv": [70, 0], "uv_size": [2, 2]},
                    "west": {"uv": [72, 0], "uv_size": [2, 2]},
                    "up": {"uv": [64, 2], "uv_size": [3, 2]},
                    "down": {"uv": [67, 2], "uv_size": [3, 2]}
                }
            },
            {
                "origin": [-8.0, 20.0, 2.0],
                "size": [4.5, 4.0, 1.0],
                "uv": {
                    "north": {"uv": [74, 0], "uv_size": [5, 4]},
                    "south": {"uv": [79, 0], "uv_size": [5, 4]},
                    "east": {"uv": [84, 0], "uv_size": [1, 4]},
                    "west": {"uv": [85, 0], "uv_size": [1, 4]},
                    "up": {"uv": [74, 4], "uv_size": [5, 1]},
                    "down": {"uv": [79, 4], "uv_size": [5, 1]}
                }
            }
        ]
    })

    # wing_right_mid
    bones.append({
        "name": "wing_right_mid",
        "parent": "wing_right_root",
        "pivot": [-8.0, 24.0, 2.0],
        "cubes": [
            {
                "origin": [-16.0, 23.5, 1.5],
                "size": [8.0, 4.5, 1.0],
                "uv": {
                    "north": {"uv": [96, 0], "uv_size": [8, 5]},
                    "south": {"uv": [104, 0], "uv_size": [8, 5]},
                    "east": {"uv": [112, 0], "uv_size": [1, 5]},
                    "west": {"uv": [113, 0], "uv_size": [1, 5]},
                    "up": {"uv": [96, 5], "uv_size": [8, 1]},
                    "down": {"uv": [104, 5], "uv_size": [8, 1]}
                }
            },
            {
                "origin": [-12.0, 12.0, 1.8],
                "size": [3.0, 11.5, 0.4],
                "uv": {
                    "north": {"uv": [0, 32], "uv_size": [3, 12]},
                    "south": {"uv": [3, 32], "uv_size": [3, 12]},
                    "east": {"uv": [6, 32], "uv_size": [1, 12]},
                    "west": {"uv": [7, 32], "uv_size": [1, 12]},
                    "up": {"uv": [0, 44], "uv_size": [3, 1]},
                    "down": {"uv": [3, 44], "uv_size": [3, 1]}
                }
            },
            {
                "origin": [-14.5, 14.0, 1.6],
                "size": [3.0, 11.0, 0.4],
                "uv": {
                    "north": {"uv": [8, 32], "uv_size": [3, 11]},
                    "south": {"uv": [11, 32], "uv_size": [3, 11]},
                    "east": {"uv": [14, 32], "uv_size": [1, 11]},
                    "west": {"uv": [15, 32], "uv_size": [1, 11]},
                    "up": {"uv": [8, 43], "uv_size": [3, 1]},
                    "down": {"uv": [11, 43], "uv_size": [3, 1]}
                }
            },
            {
                "origin": [-16.5, 17.0, 1.4],
                "size": [2.5, 10.0, 0.4],
                "uv": {
                    "north": {"uv": [16, 32], "uv_size": [3, 10]},
                    "south": {"uv": [19, 32], "uv_size": [3, 10]},
                    "east": {"uv": [22, 32], "uv_size": [1, 10]},
                    "west": {"uv": [23, 32], "uv_size": [1, 10]},
                    "up": {"uv": [16, 42], "uv_size": [3, 1]},
                    "down": {"uv": [19, 42], "uv_size": [3, 1]}
                }
            }
        ]
    })

    # wing_right_tip
    bones.append({
        "name": "wing_right_tip",
        "parent": "wing_right_mid",
        "pivot": [-16.0, 28.0, 1.5],
        "cubes": [
            {
                "origin": [-26.0, 27.5, 1.0],
                "size": [10.0, 7.5, 0.8],
                "uv": {
                    "north": {"uv": [64, 32], "uv_size": [10, 8]},
                    "south": {"uv": [74, 32], "uv_size": [10, 8]},
                    "east": {"uv": [84, 32], "uv_size": [1, 8]},
                    "west": {"uv": [85, 32], "uv_size": [1, 8]},
                    "up": {"uv": [64, 40], "uv_size": [10, 1]},
                    "down": {"uv": [74, 40], "uv_size": [10, 1]}
                }
            },
            {
                "origin": [-28.0, 34.0, 0.8],
                "size": [6.0, 7.0, 0.6],
                "uv": {
                    "north": {"uv": [86, 32], "uv_size": [6, 7]},
                    "south": {"uv": [92, 32], "uv_size": [6, 7]},
                    "east": {"uv": [98, 32], "uv_size": [1, 7]},
                    "west": {"uv": [99, 32], "uv_size": [1, 7]},
                    "up": {"uv": [86, 39], "uv_size": [6, 1]},
                    "down": {"uv": [92, 39], "uv_size": [6, 1]}
                }
            },
            {
                "origin": [-24.0, 24.0, 1.2],
                "size": [6.0, 7.0, 0.4],
                "uv": {
                    "north": {"uv": [100, 32], "uv_size": [6, 7]},
                    "south": {"uv": [106, 32], "uv_size": [6, 7]},
                    "east": {"uv": [112, 32], "uv_size": [1, 7]},
                    "west": {"uv": [113, 32], "uv_size": [1, 7]},
                    "up": {"uv": [100, 39], "uv_size": [6, 1]},
                    "down": {"uv": [106, 39], "uv_size": [6, 1]}
                }
            }
        ]
    })

    # ------------------ FOUR FLOATING SATELLITES ------------------
    # Canonical positions matching QuincyFormation.java (in model pixel units)
    # satellite_0: inner left  [ 17.6, 28.8, 11.2 ]
    # satellite_1: outer left  [ 32.0, 41.6, 16.0 ]
    # satellite_2: inner right [-17.6, 28.8, 11.2 ]
    # satellite_3: outer right [-32.0, 41.6, 16.0 ]
    satellite_pivots = [
        ("satellite_0", [17.6, 28.8, 11.2]),
        ("satellite_1", [32.0, 41.6, 16.0]),
        ("satellite_2", [-17.6, 28.8, 11.2]),
        ("satellite_3", [-32.0, 41.6, 16.0])
    ]

    for name, p in satellite_pivots:
        px, py, pz = p
        bones.append({
            "name": name,
            "parent": "root",
            "pivot": [round(px, 2), round(py, 2), round(pz, 2)],
            "cubes": [
                # Luminous diamond core
                {
                    "origin": [round(px - 1.5, 2), round(py - 1.5, 2), round(pz - 1.5, 2)],
                    "size": [3.0, 3.0, 3.0],
                    "uv": {
                        "north": {"uv": [0, 0], "uv_size": [3, 3]},
                        "south": {"uv": [3, 0], "uv_size": [3, 3]},
                        "east": {"uv": [6, 0], "uv_size": [3, 3]},
                        "west": {"uv": [9, 0], "uv_size": [3, 3]},
                        "up": {"uv": [0, 3], "uv_size": [3, 3]},
                        "down": {"uv": [3, 3], "uv_size": [3, 3]}
                    }
                },
                # Horizontal crystal blade
                {
                    "origin": [round(px - 4.5, 2), round(py - 0.6, 2), round(pz - 0.6, 2)],
                    "size": [9.0, 1.2, 1.2],
                    "uv": {
                        "north": {"uv": [32, 0], "uv_size": [9, 1]},
                        "south": {"uv": [41, 0], "uv_size": [9, 1]},
                        "east": {"uv": [50, 0], "uv_size": [1, 1]},
                        "west": {"uv": [51, 0], "uv_size": [1, 1]},
                        "up": {"uv": [32, 1], "uv_size": [9, 1]},
                        "down": {"uv": [41, 1], "uv_size": [9, 1]}
                    }
                },
                # Vertical crystal blade
                {
                    "origin": [round(px - 0.6, 2), round(py - 4.5, 2), round(pz - 0.6, 2)],
                    "size": [1.2, 9.0, 1.2],
                    "uv": {
                        "north": {"uv": [32, 4], "uv_size": [1, 9]},
                        "south": {"uv": [33, 4], "uv_size": [1, 9]},
                        "east": {"uv": [34, 4], "uv_size": [1, 9]},
                        "west": {"uv": [35, 4], "uv_size": [1, 9]},
                        "up": {"uv": [32, 13], "uv_size": [1, 1]},
                        "down": {"uv": [33, 13], "uv_size": [1, 1]}
                    }
                },
                # Axial emitter tip
                {
                    "origin": [round(px - 0.6, 2), round(py - 0.6, 2), round(pz - 2.5, 2)],
                    "size": [1.2, 1.2, 5.0],
                    "uv": {
                        "north": {"uv": [36, 4], "uv_size": [1, 1]},
                        "south": {"uv": [37, 4], "uv_size": [1, 1]},
                        "east": {"uv": [38, 4], "uv_size": [5, 1]},
                        "west": {"uv": [43, 4], "uv_size": [5, 1]},
                        "up": {"uv": [38, 5], "uv_size": [1, 5]},
                        "down": {"uv": [39, 5], "uv_size": [1, 5]}
                    }
                }
            ]
        })

    geo = {
        "format_version": "1.12.0",
        "minecraft:geometry": [
            {
                "description": {
                    "identifier": "geometry.bleachmod.quincy_manifestation",
                    "texture_width": 128,
                    "texture_height": 128,
                    "visible_bounds_width": 64.0,
                    "visible_bounds_height": 48.0,
                    "visible_bounds_offset": [0.0, 24.0, 8.0]
                },
                "bones": bones
            }
        ]
    }
    return geo


def build_animation_clips():
    """Build the 6 required animation clips in GeckoLib 1.8.0 format."""
    clips = {}

    # 1. emergence (2.0s, 40 ticks, once)
    clips["animation.quincy_manifestation.emergence"] = {
        "loop": False,
        "animation_length": 2.0,
        "bones": {
            "wing_left_root": {
                "rotation": {
                    "0.0": [15.0, -45.0, -50.0],
                    "0.5": [-10.0, 10.0, -15.0],
                    "0.9": [-5.0, 25.0, 20.0],
                    "1.4": [3.0, 5.0, -5.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "position": {
                    "0.0": [-1.0, -2.0, -1.0],
                    "0.9": [0.5, 1.0, 0.5],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_left_mid": {
                "rotation": {
                    "0.0": [0.0, 30.0, -40.0],
                    "0.5": [0.0, 10.0, -10.0],
                    "0.9": [0.0, -10.0, 15.0],
                    "1.4": [0.0, 5.0, -2.0],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_left_tip": {
                "rotation": {
                    "0.0": [0.0, 20.0, -50.0],
                    "0.5": [0.0, 5.0, -15.0],
                    "0.9": [0.0, -15.0, 20.0],
                    "1.4": [0.0, 5.0, -3.0],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_root": {
                "rotation": {
                    "0.0": [15.0, 45.0, 50.0],
                    "0.5": [-10.0, -10.0, 15.0],
                    "0.9": [-5.0, -25.0, -20.0],
                    "1.4": [3.0, -5.0, 5.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "position": {
                    "0.0": [1.0, -2.0, -1.0],
                    "0.9": [-0.5, 1.0, 0.5],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_mid": {
                "rotation": {
                    "0.0": [0.0, -30.0, 40.0],
                    "0.5": [0.0, -10.0, 10.0],
                    "0.9": [0.0, 10.0, -15.0],
                    "1.4": [0.0, -5.0, 2.0],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_tip": {
                "rotation": {
                    "0.0": [0.0, -20.0, 50.0],
                    "0.5": [0.0, -5.0, 15.0],
                    "0.9": [0.0, 15.0, -20.0],
                    "1.4": [0.0, -5.0, 3.0],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "satellite_0": {
                "position": {
                    "0.0": [-10.0, -15.0, -8.0],
                    "0.9": [2.0, 3.0, 2.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.9": [0.0, 270.0, 0.0],
                    "2.0": [0.0, 360.0, 0.0]
                }
            },
            "satellite_1": {
                "position": {
                    "0.0": [-20.0, -25.0, -12.0],
                    "0.9": [3.0, 4.0, 3.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.9": [0.0, 270.0, 0.0],
                    "2.0": [0.0, 360.0, 0.0]
                }
            },
            "satellite_2": {
                "position": {
                    "0.0": [10.0, -15.0, -8.0],
                    "0.9": [-2.0, 3.0, 2.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.9": [0.0, -270.0, 0.0],
                    "2.0": [0.0, -360.0, 0.0]
                }
            },
            "satellite_3": {
                "position": {
                    "0.0": [20.0, -25.0, -12.0],
                    "0.9": [-3.0, 4.0, 3.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.9": [0.0, -270.0, 0.0],
                    "2.0": [0.0, -360.0, 0.0]
                }
            }
        }
    }

    # 2. idle (3.0s, 60 ticks, loop: true)
    clips["animation.quincy_manifestation.idle"] = {
        "loop": True,
        "animation_length": 3.0,
        "bones": {
            "wing_left_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.75": [-2.0, 3.0, 5.0],
                    "1.5": [1.0, -2.0, -2.0],
                    "2.25": [2.0, -3.0, -4.0],
                    "3.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_left_mid": {
                "rotation": {
                    "0.0": [0.0, 0.0, 2.0],
                    "0.75": [-1.0, 2.0, 7.0],
                    "1.5": [0.0, -1.0, 1.0],
                    "2.25": [1.0, -2.0, -1.0],
                    "3.0": [0.0, 0.0, 2.0]
                }
            },
            "wing_left_tip": {
                "rotation": {
                    "0.0": [0.0, 0.0, 4.0],
                    "0.75": [0.0, 1.0, 10.0],
                    "1.5": [0.0, 0.0, 2.0],
                    "2.25": [0.0, -1.0, 0.0],
                    "3.0": [0.0, 0.0, 4.0]
                }
            },
            "wing_right_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.75": [-2.0, -3.0, -5.0],
                    "1.5": [1.0, 2.0, 2.0],
                    "2.25": [2.0, 3.0, 4.0],
                    "3.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_mid": {
                "rotation": {
                    "0.0": [0.0, 0.0, -2.0],
                    "0.75": [-1.0, -2.0, -7.0],
                    "1.5": [0.0, 1.0, -1.0],
                    "2.25": [1.0, 2.0, 1.0],
                    "3.0": [0.0, 0.0, -2.0]
                }
            },
            "wing_right_tip": {
                "rotation": {
                    "0.0": [0.0, 0.0, -4.0],
                    "0.75": [0.0, -1.0, -10.0],
                    "1.5": [0.0, 0.0, -2.0],
                    "2.25": [0.0, 1.0, 0.0],
                    "3.0": [0.0, 0.0, -4.0]
                }
            },
            # Inner pair (satellite_0 & satellite_2): phase 0
            "satellite_0": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.75": [0.0, 1.8, 0.0],
                    "1.5": [0.0, 0.0, 0.0],
                    "2.25": [0.0, -1.8, 0.0],
                    "3.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [0.0, 180.0, 0.0],
                    "3.0": [0.0, 360.0, 0.0]
                }
            },
            "satellite_2": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.75": [0.0, 1.8, 0.0],
                    "1.5": [0.0, 0.0, 0.0],
                    "2.25": [0.0, -1.8, 0.0],
                    "3.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [0.0, -180.0, 0.0],
                    "3.0": [0.0, -360.0, 0.0]
                }
            },
            # Outer pair (satellite_1 & satellite_3): phase offset by 1.5s
            "satellite_1": {
                "position": {
                    "0.0": [0.0, -1.8, 0.0],
                    "0.75": [0.0, 0.0, 0.0],
                    "1.5": [0.0, 1.8, 0.0],
                    "2.25": [0.0, 0.0, 0.0],
                    "3.0": [0.0, -1.8, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 180.0, 0.0],
                    "1.5": [0.0, 360.0, 0.0],
                    "3.0": [0.0, 540.0, 0.0]
                }
            },
            "satellite_3": {
                "position": {
                    "0.0": [0.0, -1.8, 0.0],
                    "0.75": [0.0, 0.0, 0.0],
                    "1.5": [0.0, 1.8, 0.0],
                    "2.25": [0.0, 0.0, 0.0],
                    "3.0": [0.0, -1.8, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, -180.0, 0.0],
                    "1.5": [0.0, -360.0, 0.0],
                    "3.0": [0.0, -540.0, 0.0]
                }
            }
        }
    }

    # 3. arsenal_left (1.0s, 20 ticks, once) - sweeps into SAME frontal cone
    clips["animation.quincy_manifestation.arsenal_left"] = {
        "loop": False,
        "animation_length": 1.0,
        "bones": {
            "wing_left_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.2": [10.0, 25.0, 5.0],
                    "0.45": [-15.0, 45.0, 10.0],
                    "0.75": [5.0, 15.0, 0.0],
                    "1.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_left_mid": {
                "rotation": {
                    "0.0": [0.0, 0.0, 2.0],
                    "0.2": [5.0, 20.0, 10.0],
                    "0.45": [-10.0, 35.0, 15.0],
                    "0.75": [2.0, 10.0, 4.0],
                    "1.0": [0.0, 0.0, 2.0]
                }
            },
            "wing_left_tip": {
                "rotation": {
                    "0.0": [0.0, 0.0, 4.0],
                    "0.2": [0.0, 15.0, 15.0],
                    "0.45": [-5.0, 25.0, 20.0],
                    "0.75": [0.0, 5.0, 6.0],
                    "1.0": [0.0, 0.0, 4.0]
                }
            },
            # Counterbalance on right wing
            "wing_right_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [5.0, -20.0, -10.0],
                    "1.0": [0.0, 0.0, 0.0]
                }
            },
            "satellite_0": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [2.0, 3.0, -14.0],
                    "0.75": [1.0, 1.0, -4.0],
                    "1.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [15.0, 180.0, 0.0],
                    "1.0": [0.0, 360.0, 0.0]
                }
            },
            "satellite_1": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [4.0, 5.0, -18.0],
                    "0.75": [2.0, 2.0, -5.0],
                    "1.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [20.0, 240.0, 0.0],
                    "1.0": [0.0, 360.0, 0.0]
                }
            }
        }
    }

    # 4. arsenal_right (1.0s, 20 ticks, once) - sweeps into SAME frontal cone
    clips["animation.quincy_manifestation.arsenal_right"] = {
        "loop": False,
        "animation_length": 1.0,
        "bones": {
            "wing_right_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.2": [10.0, -25.0, -5.0],
                    "0.45": [-15.0, -45.0, -10.0],
                    "0.75": [5.0, -15.0, 0.0],
                    "1.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_mid": {
                "rotation": {
                    "0.0": [0.0, 0.0, -2.0],
                    "0.2": [5.0, -20.0, -10.0],
                    "0.45": [-10.0, -35.0, -15.0],
                    "0.75": [2.0, -10.0, -4.0],
                    "1.0": [0.0, 0.0, -2.0]
                }
            },
            "wing_right_tip": {
                "rotation": {
                    "0.0": [0.0, 0.0, -4.0],
                    "0.2": [0.0, -15.0, -15.0],
                    "0.45": [-5.0, -25.0, -20.0],
                    "0.75": [0.0, -5.0, -6.0],
                    "1.0": [0.0, 0.0, -4.0]
                }
            },
            # Counterbalance on left wing
            "wing_left_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [5.0, 20.0, 10.0],
                    "1.0": [0.0, 0.0, 0.0]
                }
            },
            "satellite_2": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [-2.0, 3.0, -14.0],
                    "0.75": [-1.0, 1.0, -4.0],
                    "1.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [15.0, -180.0, 0.0],
                    "1.0": [0.0, -360.0, 0.0]
                }
            },
            "satellite_3": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [-4.0, 5.0, -18.0],
                    "0.75": [-2.0, 2.0, -5.0],
                    "1.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.45": [20.0, -240.0, 0.0],
                    "1.0": [0.0, -360.0, 0.0]
                }
            }
        }
    }

    # 5. recoil (0.6s, 12 ticks, once)
    clips["animation.quincy_manifestation.recoil"] = {
        "loop": False,
        "animation_length": 0.6,
        "bones": {
            "wing_left_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.08": [25.0, -20.0, -15.0],
                    "0.25": [-8.0, 8.0, 5.0],
                    "0.6": [0.0, 0.0, 0.0]
                },
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.08": [0.0, -1.0, 3.0],
                    "0.25": [0.0, 0.5, -1.0],
                    "0.6": [0.0, 0.0, 0.0]
                }
            },
            "wing_left_mid": {
                "rotation": {
                    "0.0": [0.0, 0.0, 2.0],
                    "0.08": [15.0, -15.0, -20.0],
                    "0.25": [-4.0, 5.0, 8.0],
                    "0.6": [0.0, 0.0, 2.0]
                }
            },
            "wing_left_tip": {
                "rotation": {
                    "0.0": [0.0, 0.0, 4.0],
                    "0.08": [10.0, -10.0, -25.0],
                    "0.25": [-2.0, 3.0, 10.0],
                    "0.6": [0.0, 0.0, 4.0]
                }
            },
            "wing_right_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.08": [25.0, 20.0, 15.0],
                    "0.25": [-8.0, -8.0, -5.0],
                    "0.6": [0.0, 0.0, 0.0]
                },
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.08": [0.0, -1.0, 3.0],
                    "0.25": [0.0, 0.5, -1.0],
                    "0.6": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_mid": {
                "rotation": {
                    "0.0": [0.0, 0.0, -2.0],
                    "0.08": [15.0, 15.0, 20.0],
                    "0.25": [-4.0, -5.0, -8.0],
                    "0.6": [0.0, 0.0, -2.0]
                }
            },
            "wing_right_tip": {
                "rotation": {
                    "0.0": [0.0, 0.0, -4.0],
                    "0.08": [10.0, 10.0, 25.0],
                    "0.25": [-2.0, -3.0, -10.0],
                    "0.6": [0.0, 0.0, -4.0]
                }
            },
            "satellite_0": {"position": {"0.0": [0.0, 0.0, 0.0], "0.08": [2.0, 1.0, 4.0], "0.6": [0.0, 0.0, 0.0]}},
            "satellite_1": {"position": {"0.0": [0.0, 0.0, 0.0], "0.08": [4.0, 2.0, 6.0], "0.6": [0.0, 0.0, 0.0]}},
            "satellite_2": {"position": {"0.0": [0.0, 0.0, 0.0], "0.08": [-2.0, 1.0, 4.0], "0.6": [0.0, 0.0, 0.0]}},
            "satellite_3": {"position": {"0.0": [0.0, 0.0, 0.0], "0.08": [-4.0, 2.0, 6.0], "0.6": [0.0, 0.0, 0.0]}}
        }
    }

    # 6. settle (1.2s, 24 ticks, once)
    clips["animation.quincy_manifestation.settle"] = {
        "loop": False,
        "animation_length": 1.2,
        "bones": {
            "wing_left_root": {
                "rotation": {
                    "0.0": [-5.0, 15.0, 10.0],
                    "0.4": [5.0, -10.0, -8.0],
                    "0.8": [-2.0, 3.0, 2.0],
                    "1.2": [0.0, 0.0, 0.0]
                }
            },
            "wing_left_mid": {
                "rotation": {
                    "0.0": [-3.0, 10.0, 8.0],
                    "0.4": [3.0, -5.0, -4.0],
                    "0.8": [-1.0, 2.0, 3.0],
                    "1.2": [0.0, 0.0, 2.0]
                }
            },
            "wing_left_tip": {
                "rotation": {
                    "0.0": [-2.0, 5.0, 6.0],
                    "0.4": [2.0, -3.0, -2.0],
                    "0.8": [0.0, 1.0, 5.0],
                    "1.2": [0.0, 0.0, 4.0]
                }
            },
            "wing_right_root": {
                "rotation": {
                    "0.0": [-5.0, -15.0, -10.0],
                    "0.4": [5.0, 10.0, 8.0],
                    "0.8": [-2.0, -3.0, -2.0],
                    "1.2": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_mid": {
                "rotation": {
                    "0.0": [-3.0, -10.0, -8.0],
                    "0.4": [3.0, 5.0, 4.0],
                    "0.8": [-1.0, -2.0, -3.0],
                    "1.2": [0.0, 0.0, -2.0]
                }
            },
            "wing_right_tip": {
                "rotation": {
                    "0.0": [-2.0, -5.0, -6.0],
                    "0.4": [2.0, 3.0, 2.0],
                    "0.8": [0.0, -1.0, -5.0],
                    "1.2": [0.0, 0.0, -4.0]
                }
            },
            "satellite_0": {"position": {"0.0": [1.0, 1.0, -2.0], "0.6": [0.0, -0.5, 0.5], "1.2": [0.0, 0.0, 0.0]}},
            "satellite_1": {"position": {"0.0": [2.0, 2.0, -3.0], "0.6": [0.0, -1.0, 1.0], "1.2": [0.0, 0.0, 0.0]}},
            "satellite_2": {"position": {"0.0": [-1.0, 1.0, -2.0], "0.6": [0.0, -0.5, 0.5], "1.2": [0.0, 0.0, 0.0]}},
            "satellite_3": {"position": {"0.0": [-2.0, 2.0, -3.0], "0.6": [0.0, -1.0, 1.0], "1.2": [0.0, 0.0, 0.0]}}
        }
    }

    return {"format_version": "1.8.0", "animations": clips}


def build_bbmodel_project(geo: dict, anim: dict, texture_bytes: bytes) -> dict:
    """Build a complete and editable Blockbench (.bbmodel) project."""
    b64_tex = "data:image/png;base64," + base64.b64encode(texture_bytes).decode("ascii")
    tex_uuid = identity("texture_quincy_manifestation")

    out_elements = []
    groups = {}

    for bone in geo["minecraft:geometry"][0]["bones"]:
        b_name = bone["name"]
        b_uuid = identity(f"bone_{b_name}")
        b_children = []

        for i, cube in enumerate(bone.get("cubes", [])):
            c_name = f"{b_name}_cube_{i}"
            c_uuid = identity(c_name)
            frm = cube["origin"]
            size = cube["size"]
            to = [round(frm[0] + size[0], 2), round(frm[1] + size[1], 2), round(frm[2] + size[2], 2)]
            faces = {}
            for f in ["north", "south", "east", "west", "up", "down"]:
                face = cube.get("uv", {}).get(f)
                if face:
                    u, v = face["uv"]
                    w, h = face["uv_size"]
                    faces[f] = {"uv": [u, v, u + w, v + h], "texture": 0}
                else:
                    faces[f] = {"uv": [0, 0, 0, 0], "texture": None}
            out_elements.append({
                "name": c_name,
                "type": "cube",
                "uuid": c_uuid,
                "from": frm,
                "to": to,
                "origin": bone["pivot"],
                "rotation": [0, 0, 0],
                "color": 0,
                "faces": faces
            })
            b_children.append(c_uuid)

        groups[b_name] = {
            "name": b_name,
            "origin": bone["pivot"],
            "rotation": [0, 0, 0],
            "uuid": b_uuid,
            "export": True,
            "isOpen": True,
            "visibility": True,
            "children": b_children
        }

    out_outliner = []
    for bone in geo["minecraft:geometry"][0]["bones"]:
        group = groups[bone["name"]]
        if bone.get("parent"):
            groups[bone["parent"]]["children"].append(group)
        else:
            out_outliner.append(group)

    animations = []
    for name, clip in anim["animations"].items():
        animators = {}
        for bone, channels in clip["bones"].items():
            keys = []
            for channel, frames in channels.items():
                for time, vec in frames.items():
                    keys.append({
                        "channel": channel,
                        "time": float(time),
                        "interpolation": "linear",
                        "uuid": identity(name + bone + channel + time),
                        "data_points": [dict(zip(("x", "y", "z"), map(str, vec)))]
                    })
            animators[identity("bone_" + bone)] = {"name": bone, "type": "bone", "keyframes": keys}
        animations.append({
            "uuid": identity(name),
            "name": name,
            "loop": "loop" if clip["loop"] else "once",
            "length": clip["animation_length"],
            "snapping": 20,
            "animators": animators
        })

    bbmodel = {
        "meta": {
            "format_version": "4.5",
            "creation_time": 1793361600,
            "model_format": "bedrock",
            "box_uv": False
        },
        "name": "quincy_manifestation",
        "geometry_name": "quincy_manifestation",
        "visible_box": [64.0, 48.0, 0.0],
        "resolution": {"width": 128, "height": 128},
        "elements": out_elements,
        "outliner": out_outliner,
        "textures": [
            {
                "path": "quincy_manifestation.png",
                "name": "quincy_manifestation",
                "folder": "textures/entity",
                "namespace": "bleachmod",
                "id": "0",
                "particle": False,
                "render_mode": "emissive",
                "visible": True,
                "mode": "bitmap",
                "saved": True,
                "uuid": tex_uuid,
                "source": b64_tex
            }
        ],
        "animations": animations
    }
    return bbmodel


def render_model_preview_images(geo: dict, anim: dict, texture_bytes: bytes, out_dir: Path):
    """Render orthogonal and perspective 2D preview projections of the model and animation poses."""
    out_dir.mkdir(parents=True, exist_ok=True)
    tex_img = Image.open(io.BytesIO(texture_bytes)).convert("RGBA")

    # Views to render: Front, Back, Left Profile, Top
    views = [
        ("preview_front.jpg", (0, 0), "Quincy Manifestation — Front View (-Z)"),
        ("preview_back.jpg", (0, 180), "Quincy Manifestation — Back View (+Z)"),
        ("preview_profile.jpg", (0, 90), "Quincy Manifestation — Profile View (Left)"),
        ("preview_top.jpg", (90, 0), "Quincy Manifestation — Top View (+Y)")
    ]

    bones = geo["minecraft:geometry"][0]["bones"]
    all_cubes = []
    for b in bones:
        for c in b.get("cubes", []):
            all_cubes.append((b["name"], c))

    width, height = 800, 600
    scale = 8.0  # pixels per model unit
    cx, cy = width // 2, height // 2 + 80

    for filename, (pitch_deg, yaw_deg), label in views:
        canvas = Image.new("RGB", (width, height), (10, 16, 28))
        draw = ImageDraw.Draw(canvas)

        # Draw coordinate / actor reference grid
        draw.line([(cx - 200, cy), (cx + 200, cy)], fill=(25, 45, 75), width=1)
        draw.line([(cx, cy - 250), (cx, cy + 50)], fill=(25, 45, 75), width=1)

        # Draw player silhouette reference for scale proof
        player_x = cx
        player_y = cy - int(12 * scale)
        # Torso (8x12x4)
        draw.rectangle([cx - int(4 * scale), cy - int(24 * scale), cx + int(4 * scale), cy - int(12 * scale)], outline=(40, 60, 90), width=1)
        # Head (8x8x8)
        draw.rectangle([cx - int(4 * scale), cy - int(32 * scale), cx + int(4 * scale), cy - int(24 * scale)], outline=(40, 60, 90), width=1)
        # Legs
        draw.rectangle([cx - int(4 * scale), cy - int(12 * scale), cx, cy], outline=(30, 50, 75), width=1)
        draw.rectangle([cx, cy - int(12 * scale), cx + int(4 * scale), cy], outline=(30, 50, 75), width=1)

        # Sort cubes by depth depending on view
        rad_pitch = math.radians(pitch_deg)
        rad_yaw = math.radians(yaw_deg)

        def project(x, y, z):
            # Rotate yaw (around Y)
            x1 = x * math.cos(rad_yaw) - z * math.sin(rad_yaw)
            z1 = x * math.sin(rad_yaw) + z * math.cos(rad_yaw)
            # Rotate pitch (around X)
            y2 = y * math.cos(rad_pitch) - z1 * math.sin(rad_pitch)
            z2 = y * math.sin(rad_pitch) + z1 * math.cos(rad_pitch)
            px = cx + int(x1 * scale)
            py = cy - int(y2 * scale)
            return px, py, z2

        cube_draw_list = []
        for b_name, c in all_cubes:
            ox, oy, oz = c["origin"]
            sx, sy, sz = c["size"]
            # 8 corners
            corners = [
                (ox, oy, oz), (ox + sx, oy, oz), (ox + sx, oy + sy, oz), (ox, oy + sy, oz),
                (ox, oy, oz + sz), (ox + sx, oy, oz + sz), (ox + sx, oy + sy, oz + sz), (ox, oy + sy, oz + sz)
            ]
            proj_corners = [project(*pt) for pt in corners]
            avg_z = sum(p[2] for p in proj_corners) / 8.0

            # Color styling
            if "satellite" in b_name:
                fill_col = (180, 245, 255)
                outline_col = (50, 210, 255)
            elif "tip" in b_name:
                fill_col = (100, 225, 255)
                outline_col = (20, 160, 230)
            elif "mid" in b_name:
                fill_col = (60, 185, 245)
                outline_col = (15, 110, 180)
            else:
                fill_col = (40, 140, 215)
                outline_col = (10, 60, 120)

            cube_draw_list.append((avg_z, proj_corners, fill_col, outline_col, b_name))

        # Back-to-front rendering
        cube_draw_list.sort(key=lambda item: item[0])

        for avg_z, pts, fill_col, outline_col, b_name in cube_draw_list:
            min_x = min(p[0] for p in pts)
            max_x = max(p[0] for p in pts)
            min_y = min(p[1] for p in pts)
            max_y = max(p[1] for p in pts)
            draw.rectangle([min_x, min_y, max_x, max_y], fill=fill_col, outline=outline_col, width=1)

        # Legend and labels
        draw.text((25, 25), label, fill=(240, 250, 255))
        draw.text((25, 45), f"Bones: 11 | Cubes: {len(all_cubes)} | Texture: 128x128 RGBA | Scale: 16u/m", fill=(120, 180, 225))
        draw.text((25, height - 35), "Actor Reference: Gray Silhouette = Player (Torso 8x12x4, Head 8x8x8)", fill=(100, 140, 180))

        canvas.save(out_dir / filename, "JPEG", quality=90)

    # Render Animation Sequence Sheet (Prancha Composta 6 Clipes)
    clips_list = [
        ("emergence", "Emergence (0.9s peak flare)"),
        ("idle", "Idle (Organic breathing pulse)"),
        ("arsenal_left", "Arsenal Left (Frontal focus)"),
        ("arsenal_right", "Arsenal Right (Frontal focus)"),
        ("recoil", "Recoil (Impulse snapback)"),
        ("settle", "Settle (Calming return)")
    ]

    sheet_w, sheet_h = 1920, 1080
    sheet = Image.new("RGB", (sheet_w, sheet_h), (8, 12, 22))
    s_draw = ImageDraw.Draw(sheet)
    s_draw.text((40, 30), "QUINCY VOLLSTÄNDIG MANIFESTATION — SKELETAL RIG & ANIMATION CLIPS", fill=(255, 255, 255))
    s_draw.text((40, 55), "Branch: codex/quincy-rig-manifestacao | Geometry: 11 bones, 28 cubes | 4 Satellites | 6 Clipes", fill=(100, 190, 240))

    cell_w, cell_h = 580, 460
    for idx, (c_name, c_title) in enumerate(clips_list):
        row = idx // 3
        col = idx % 3
        ox = 50 + col * (cell_w + 40)
        oy = 100 + row * (cell_h + 30)

        s_draw.rectangle([ox, oy, ox + cell_w, oy + cell_h], outline=(25, 55, 95), fill=(12, 18, 32), width=2)
        s_draw.text((ox + 20, oy + 20), f"Clip: {c_name}", fill=(240, 250, 255))
        s_draw.text((ox + 20, oy + 40), c_title, fill=(80, 210, 255))

        # Internal projection with pose offset from clip
        p_cx = ox + cell_w // 2
        p_cy = oy + cell_h // 2 + 50
        s_scale = 5.5

        # Player silhouette
        s_draw.rectangle([p_cx - int(4 * s_scale), p_cy - int(24 * s_scale), p_cx + int(4 * s_scale), p_cy - int(12 * s_scale)], outline=(40, 60, 90), width=1)
        s_draw.rectangle([p_cx - int(4 * s_scale), p_cy - int(32 * s_scale), p_cx + int(4 * s_scale), p_cy - int(24 * s_scale)], outline=(40, 60, 90), width=1)

        # Render cubes for this clip
        clip_data = anim["animations"][f"animation.quincy_manifestation.{c_name}"]
        # Sample mid keyframe (e.g. 0.45s)
        for b_name, c in all_cubes:
            cx_orig, cy_orig, cz_orig = c["origin"]
            sx, sy, sz = c["size"]

            # Simple pose offset based on clip
            rx, ry, rz = 0.0, 0.0, 0.0
            tx, ty, tz = 0.0, 0.0, 0.0

            b_anim = clip_data.get("bones", {}).get(b_name, {})
            if "rotation" in b_anim:
                # pick sample frame
                sample_t = list(b_anim["rotation"].keys())[len(b_anim["rotation"]) // 2]
                rx, ry, rz = b_anim["rotation"][sample_t]
            if "position" in b_anim:
                sample_t = list(b_anim["position"].keys())[len(b_anim["position"]) // 2]
                tx, ty, tz = b_anim["position"][sample_t]

            # Project
            cur_x = cx_orig + tx
            cur_y = cy_orig + ty
            cur_z = cz_orig + tz

            # Rotate around bone pivot
            bp = next(b["pivot"] for b in bones if b["name"] == b_name)
            dx = cur_x - bp[0]
            dy = cur_y - bp[1]

            rad_z = math.radians(rz)
            rot_x = bp[0] + dx * math.cos(rad_z) - dy * math.sin(rad_z)
            rot_y = bp[1] + dx * math.sin(rad_z) + dy * math.cos(rad_z)

            px = p_cx + int(rot_x * s_scale)
            py = p_cy - int(rot_y * s_scale)
            pw = max(2, int(sx * s_scale))
            ph = max(2, int(sy * s_scale))

            col = (180, 245, 255) if "satellite" in b_name else (60, 200, 255)
            s_draw.rectangle([px, py - ph, px + pw, py], fill=col, outline=(15, 90, 160))

    sheet.save(out_dir / "prancha_manifestacao_clips.jpg", "JPEG", quality=90)
    print(f"Rendered preview images and prancha to {out_dir}")


def main():
    parser = argparse.ArgumentParser(description="Quincy Manifestation Model & Rig Pipeline")
    parser.add_argument("--check", action="store_true", help="Check if generated files match disk files byte-for-byte")
    parser.add_argument("--render-preview", action="store_true", help="Render 2D preview projections and animation sheet")
    args = parser.parse_args()

    geo = build_geometry_and_cubes()
    anim = build_animation_clips()
    texture_bytes = create_quincy_manifestation_texture_bytes()
    bbmodel = build_bbmodel_project(geo, anim, texture_bytes)

    geo_str = json.dumps(geo, indent=2) + "\n"
    anim_str = json.dumps(anim, indent=2) + "\n"
    bbmodel_str = json.dumps(bbmodel, indent=2) + "\n"

    targets = [
        (GEO_JSON, geo_str.encode("utf-8")),
        (ANIM_JSON, anim_str.encode("utf-8")),
        (TEXTURE_PNG, texture_bytes),
        (SOURCE_BBMODEL, bbmodel_str.encode("utf-8"))
    ]

    if args.check:
        mismatches = []
        for path, data in targets:
            if not path.exists():
                mismatches.append(f"Missing: {path}")
                continue
            with open(path, "rb") as f:
                disk_data = f.read()
            if disk_data != data:
                mismatches.append(f"Differs: {path} (expected {len(data)} bytes, disk has {len(disk_data)} bytes)")
        if mismatches:
            print("CHECK FAILED:\n" + "\n".join(mismatches))
            sys.exit(1)
        print("CHECK PASSED: All 4 runtime & lab assets are strictly synchronized.")
        sys.exit(0)

    # Write files to disk
    for path, data in targets:
        path.parent.mkdir(parents=True, exist_ok=True)
        with open(path, "wb") as f:
            f.write(data)
        print(f"Generated: {path} ({len(data)} bytes)")

    if args.render_preview or True:
        import io
        render_model_preview_images(geo, anim, texture_bytes, PREVIEW_DIR)


if __name__ == "__main__":
    main()
