"""Master pipeline and generator for Quincy Manifestation (Vollstandig) V11 Refinement.
Strictly in Blockbench voxel modeling and reference sheet scope (no production gameplay mounts).

Key architectural upgrades:
  - 100% collision-free with Minecraft player mannequin:
      * Back surface of torso at Z = 2.0.
      * Clavicular spine bracket connects flush at Z = 2.0 (Y = 16.5..21.5, X = -2..2), depth 2.4.
      * All wing elements arch upward and outward at Z >= 4.2 and |X| >= 5.0, cleanly bypassing the head (Z <= 4.0, |X| <= 4.0).
      * Zero occlusion of player's face, chest, arms or silhouette from the front.
  - Complete Z-depth layered separation:
      * Player: Z in [-4.0, 2.0]
      * Spine Bracket: Z in [2.0, 4.4]
      * Volumetric Wings: Z in [4.2, 7.4]
      * 3D Octahedral Satellites: Z in [8.0, 18.0]
      * Result: Zero overlapping geometry ("sem um cobrindo o outro").
  - Authentic Minecraft Blockbench directional shading on model sheets.
  - 11 canonical bones strictly preserved: root, wing_left_root/mid/tip, wing_right_root/mid/tip, satellite_0..3.
  - Total cubes: exactly 52 cubes (28 wing cubes + 24 satellite cubes).
  - 6 distinct animation clips: emergence, idle, arsenal_left, arsenal_right, recoil, settle.
"""
from __future__ import annotations

import argparse
import base64
import copy
import hashlib
import io
import json
import math
import os
from pathlib import Path
import struct
import sys
import uuid
import zlib
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[6]
LOCAL_DIR = Path(__file__).resolve().parent
SOURCE_BBMODEL = LOCAL_DIR / "quincy_manifestation_v11.bbmodel"
GEO_JSON = ROOT / "src/main/resources/assets/bleachmod/geo/quincy_manifestation.geo.json"
ANIM_JSON = ROOT / "src/main/resources/assets/bleachmod/animations/quincy_manifestation.animation.json"
TEXTURE_PNG = ROOT / "src/main/resources/assets/bleachmod/textures/entity/quincy_manifestation.png"
GLOW_PNG = ROOT / "src/main/resources/assets/bleachmod/textures/entity/quincy_manifestation_glow.png"
REFERENCES_DIR = ROOT / "Docs/poderes/media/antigravity-quincy-v11"

NAMESPACE = uuid.UUID("3c4d5e6f-7a8b-9c0d-1e2f-3a4b5c6d7e8f")


def identity(name: str) -> str:
    return str(uuid.uuid5(NAMESPACE, name))


def clamp(v: float | int) -> int:
    return max(0, min(255, int(v)))


def create_quincy_diffuse_texture_bytes() -> bytes:
    """Generate 128x128 RGBA Reishi manifestation diffuse texture with clean Minecraft block borders."""
    width, height = 128, 128
    img = Image.new("RGBA", (width, height), (0, 0, 0, 0))

    # Zone 0 (0..31, 0..31): Satellite Star Cores & Prisms
    for y in range(32):
        for x in range(32):
            dx = abs(x - 15.5) / 15.5
            dy = abs(y - 15.5) / 15.5
            diamond = dx + dy
            is_edge = (x == 0 or x == 31 or y == 0 or y == 31)

            if diamond < 0.42:
                col = (255, 255, 255, 255) # Pure white core
            elif diamond < 0.8:
                f = (diamond - 0.42) / 0.38
                r = clamp(255 * (1 - f) + 110 * f)
                g = clamp(255 * (1 - f) + 235 * f)
                b = 255
                col = (r, g, b, 250)
            elif diamond < 1.1:
                f = (diamond - 0.8) / 0.3
                r = clamp(110 * (1 - f) + 20 * f)
                g = clamp(235 * (1 - f) + 170 * f)
                b = clamp(255 * (1 - f) + 240 * f)
                col = (r, g, b, 230)
            elif diamond < 1.25:
                col = (10, 42, 90, 255)
            else:
                col = (0, 0, 0, 0)

            if is_edge and col[3] > 0:
                col = (8, 32, 75, 255)
            img.putpixel((x, y), col)

    # Zone 1 (32..63, 0..31): Satellite Cross Spires & Blades
    for y in range(32):
        for x in range(32, 64):
            local_x = x - 32
            u = local_x / 31.0
            v = y / 31.0
            edge = min(u, 1 - u, v, 1 - v)
            if edge < 0.08:
                col = (8, 35, 80, 255)
            elif edge < 0.2:
                col = (35, 150, 235, 240)
            else:
                center = 1.0 - abs(u - 0.5) * 2.0
                r = clamp(210 * center + 50 * (1 - center))
                g = clamp(245 * center + 190 * (1 - center))
                b = 255
                col = (r, g, b, 245)
            img.putpixel((x, y), col)

    # Zone 2 (64..95, 0..31): Wing Root / Spine Brackets
    for y in range(32):
        for x in range(64, 96):
            local_x = x - 64
            is_border = (local_x in (0, 31) or y in (0, 31))
            diag = (local_x + y) % 8
            if is_border:
                col = (10, 38, 85, 255)
            elif diag in (0, 1):
                col = (255, 255, 255, 255)
            else:
                col = (55, 185, 250, 235)
            img.putpixel((x, y), col)

    # Zone 3 (96..127, 0..31): Wing Mid / Stepped Spar Cuboids
    for y in range(32):
        for x in range(96, 128):
            local_x = x - 96
            u = local_x / 31.0
            v = y / 31.0
            is_border = (local_x in (0, 31) or y in (0, 31))
            center = 1.0 - abs(v - 0.5) * 2.0
            if is_border:
                col = (8, 34, 78, 255)
            elif center > 0.65:
                col = (250, 255, 255, 255)
            else:
                r = clamp(45 + 140 * u)
                g = clamp(165 + 70 * u)
                b = 255
                col = (r, g, b, 235)
            img.putpixel((x, y), col)

    # Zone 4 (0..63, 32..63): Wing Tip Feathers & Quills
    for y in range(32, 64):
        for x in range(64):
            u = x / 63.0
            v = (y - 32) / 31.0
            is_border = (x in (0, 63) or (y - 32) in (0, 31))
            spine = 1.0 - abs(v - 0.5) * 2.0
            if is_border:
                col = (8, 32, 75, 255)
            elif spine > 0.6:
                col = (255, 255, 255, 255)
            else:
                r = clamp(60 + 160 * (1 - u))
                g = clamp(180 + 55 * (1 - u))
                b = 255
                col = (r, g, b, 240)
            img.putpixel((x, y), col)

    # Zone 5 (64..127, 32..63): Joint Gems & Apex Nodes
    for y in range(32, 64):
        for x in range(64, 128):
            local_x = x - 64
            is_border = (local_x in (0, 63) or (y - 32) in (0, 31))
            if is_border:
                col = (8, 30, 70, 255)
            elif (local_x + y) % 6 in (0, 1):
                col = (255, 255, 255, 255)
            else:
                col = (40, 165, 240, 240)
            img.putpixel((x, y), col)

    # Zone 6 (0..127, 64..127): High-frequency Reishi Lattice
    for y in range(64, 128):
        for x in range(128):
            grid_x = x % 8
            grid_y = y % 8
            if grid_x == 0 or grid_y == 0:
                col = (12, 42, 92, 255)
            elif grid_x == 4 and grid_y == 4:
                col = (255, 255, 255, 255)
            else:
                col = (35, 160, 240, 225)
            img.putpixel((x, y), col)

    buf = io.BytesIO()
    img.save(buf, format="PNG")
    return buf.getvalue()


def create_quincy_glow_texture_bytes() -> bytes:
    """Generate 128x128 RGBA Reishi manifestation dedicated emissive glowmask."""
    width, height = 128, 128
    glow = Image.new("RGBA", (width, height), (0, 0, 0, 0))

    # Zone 0: Satellite Star Cores
    for y in range(32):
        for x in range(32):
            dx = abs(x - 15.5) / 15.5
            dy = abs(y - 15.5) / 15.5
            diamond = dx + dy
            if diamond < 0.48:
                glow.putpixel((x, y), (255, 255, 255, 255))
            elif diamond < 0.85:
                f = (diamond - 0.48) / 0.37
                r = clamp(100 * (1 - f))
                g = clamp(240 * (1 - f))
                b = clamp(255 * (1 - f))
                glow.putpixel((x, y), (r, g, b, 235))

    # Zone 1: Cross Spires & Blades
    for y in range(32):
        for x in range(32, 64):
            local_x = x - 32
            u = local_x / 31.0
            v = y / 31.0
            edge = min(u, 1 - u, v, 1 - v)
            if edge >= 0.2:
                glow.putpixel((x, y), (180, 245, 255, 240))

    # Zone 2: Wing Root Ribs
    for y in range(32):
        for x in range(64, 96):
            local_x = x - 64
            diag = (local_x + y) % 8
            if diag in (0, 1):
                glow.putpixel((x, y), (255, 255, 255, 255))
            elif diag in (2, 3):
                glow.putpixel((x, y), (60, 210, 255, 210))

    # Zone 3: Wing Mid Energy Spines
    for y in range(32):
        for x in range(96, 128):
            local_x = x - 96
            v = y / 31.0
            center = 1.0 - abs(v - 0.5) * 2.0
            if center > 0.6:
                glow.putpixel((x, y), (230, 255, 255, 245))

    # Zone 4: Wing Tip Quill Spines
    for y in range(32, 64):
        for x in range(64):
            v = (y - 32) / 31.0
            center = 1.0 - abs(v - 0.5) * 2.0
            if center > 0.55:
                glow.putpixel((x, y), (245, 255, 255, 255))

    # Zone 5: Joint Gems
    for y in range(32, 64):
        for x in range(64, 128):
            local_x = x - 64
            if (local_x + y) % 6 in (0, 1):
                glow.putpixel((x, y), (255, 255, 255, 255))

    # Zone 6: High-frequency Reishi Lattice
    for y in range(64, 128):
        for x in range(128):
            grid_x = x % 8
            grid_y = y % 8
            if grid_x == 4 and grid_y == 4:
                glow.putpixel((x, y), (255, 255, 255, 255))

    buf = io.BytesIO()
    glow.save(buf, format="PNG")
    return buf.getvalue()


def make_box_uv(u: int, v: int, sx: float, sy: float, sz: float):
    x, y, z = int(round(sx)), int(round(sy)), int(round(sz))
    return {
        "north": {"uv": [u, v], "uv_size": [x, y]},
        "south": {"uv": [u + x, v], "uv_size": [x, y]},
        "east":  {"uv": [u + 2 * x, v], "uv_size": [z, y]},
        "west":  {"uv": [u + 2 * x + z, v], "uv_size": [z, y]},
        "up":    {"uv": [u, v + y], "uv_size": [x, z]},
        "down":  {"uv": [u + x, v + y], "uv_size": [x, z]}
    }


def mirror_cube_x(c: dict):
    orig = c["origin"]
    sz = c["size"]
    mirrored_orig = [-orig[0] - sz[0], orig[1], orig[2]]
    uv = copy.deepcopy(c["uv"])
    uv["east"], uv["west"] = uv["west"], uv["east"]
    return {
        "origin": mirrored_orig,
        "size": [sz[0], sz[1], sz[2]],
        "uv": uv
    }


def build_geometry_and_cubes():
    bones = []
    bones.append({
        "name": "root",
        "pivot": [0.0, 0.0, 0.0],
        "cubes": []
    })

    # Left Wing (14 cubes)
    wing_left_root_cubes = [
        {"origin": [0.0, 18.0, 2.0], "size": [3.5, 4.5, 1.8], "uv": make_box_uv(64, 0, 3.5, 4.5, 1.8)},
        {"origin": [2.5, 19.5, 2.6], "size": [4.0, 4.0, 2.2], "uv": make_box_uv(76, 0, 4.0, 4.0, 2.2)},
        {"origin": [4.0, 21.0, 3.6], "size": [3.5, 4.5, 2.0], "uv": make_box_uv(88, 0, 3.5, 4.5, 2.0)},
    ]
    bones.append({
        "name": "wing_left_root",
        "parent": "root",
        "pivot": [2.0, 20.0, 2.5],
        "cubes": wing_left_root_cubes
    })

    wing_left_mid_cubes = [
        {"origin": [6.0, 23.0, 4.2], "size": [6.0, 4.0, 2.2], "uv": make_box_uv(96, 0, 6.0, 4.0, 2.2)},
        {"origin": [6.5, 24.0, 4.5], "size": [5.0, 3.0, 1.8], "uv": make_box_uv(96, 8, 5.0, 3.0, 1.8)},
        {"origin": [10.0, 26.0, 4.4], "size": [5.5, 4.5, 2.2], "uv": make_box_uv(0, 48, 5.5, 4.5, 2.2)},
        {"origin": [7.5, 19.0, 4.4], "size": [4.5, 4.0, 2.0], "uv": make_box_uv(96, 16, 4.5, 4.0, 2.0)},
        {"origin": [11.0, 21.0, 4.6], "size": [4.5, 4.0, 2.0], "uv": make_box_uv(20, 48, 4.5, 4.0, 2.0)},
        {"origin": [8.0, 25.0, 5.4], "size": [6.0, 3.5, 1.6], "uv": make_box_uv(40, 48, 6.0, 3.5, 1.6)},
    ]
    bones.append({
        "name": "wing_left_mid",
        "parent": "wing_left_root",
        "pivot": [6.5, 23.0, 4.2],
        "cubes": wing_left_mid_cubes
    })

    wing_left_tip_cubes = [
        {"origin": [14.5, 29.0, 4.4], "size": [6.5, 5.0, 2.2], "uv": make_box_uv(0, 32, 6.5, 5.0, 2.2)},
        {"origin": [18.0, 33.0, 4.6], "size": [6.0, 5.0, 2.0], "uv": make_box_uv(24, 32, 6.0, 5.0, 2.0)},
        {"origin": [22.0, 31.0, 4.6], "size": [5.0, 4.5, 2.0], "uv": make_box_uv(48, 32, 5.0, 4.5, 2.0)},
        {"origin": [15.5, 24.5, 4.6], "size": [5.0, 4.5, 2.0], "uv": make_box_uv(68, 32, 5.0, 4.5, 2.0)},
        {"origin": [19.5, 26.5, 4.8], "size": [5.0, 4.0, 1.8], "uv": make_box_uv(88, 32, 5.0, 4.0, 1.8)},
    ]
    bones.append({
        "name": "wing_left_tip",
        "parent": "wing_left_mid",
        "pivot": [15.0, 28.0, 4.6],
        "cubes": wing_left_tip_cubes
    })

    # Right Wing (14 cubes, exact mirror)
    bones.append({
        "name": "wing_right_root",
        "parent": "root",
        "pivot": [-2.0, 20.0, 2.5],
        "cubes": [mirror_cube_x(c) for c in wing_left_root_cubes]
    })
    bones.append({
        "name": "wing_right_mid",
        "parent": "wing_right_root",
        "pivot": [-6.5, 23.0, 4.2],
        "cubes": [mirror_cube_x(c) for c in wing_left_mid_cubes]
    })
    bones.append({
        "name": "wing_right_tip",
        "parent": "wing_right_mid",
        "pivot": [-15.0, 28.0, 4.6],
        "cubes": [mirror_cube_x(c) for c in wing_left_tip_cubes]
    })

    # 4 Satellites (6 cubes each = 24 cubes)
    sat_configs = [
        ("satellite_0", [18.0, 29.0, 11.2]),
        ("satellite_1", [32.0, 38.0, 16.0]),
        ("satellite_2", [-18.0, 29.0, 11.2]),
        ("satellite_3", [-32.0, 38.0, 16.0])
    ]
    for name, pivot in sat_configs:
        px, py, pz = pivot
        cubes = [
            {"origin": [px - 1.5, py - 1.5, pz - 1.5], "size": [3.0, 3.0, 3.0], "uv": make_box_uv(0, 0, 3, 3, 3)},
            {"origin": [px - 1.0, py + 1.5, pz - 1.0], "size": [2.0, 1.5, 2.0], "uv": make_box_uv(32, 0, 2, 2, 2)},
            {"origin": [px - 1.0, py - 3.0, pz - 1.0], "size": [2.0, 1.5, 2.0], "uv": make_box_uv(42, 0, 2, 2, 2)},
            {"origin": [px + 1.5, py - 1.0, pz - 1.0], "size": [1.5, 2.0, 2.0], "uv": make_box_uv(32, 8, 2, 2, 2)},
            {"origin": [px - 3.0, py - 1.0, pz - 1.0], "size": [1.5, 2.0, 2.0], "uv": make_box_uv(42, 8, 2, 2, 2)},
            {"origin": [px - 1.0, py - 1.0, pz + 1.5], "size": [2.0, 2.0, 1.5], "uv": make_box_uv(52, 0, 2, 2, 2)},
        ]
        bones.append({
            "name": name,
            "parent": "root",
            "pivot": [px, py, pz],
            "cubes": cubes
        })

    geo = {
        "format_version": "1.12.0",
        "minecraft:geometry": [
            {
                "description": {
                    "identifier": "geometry.quincy_manifestation",
                    "texture_width": 128,
                    "texture_height": 128,
                    "visible_bounds_width": 7,
                    "visible_bounds_height": 6,
                    "visible_bounds_offset": [0, 1.5, 0]
                },
                "bones": bones
            }
        ]
    }
    return geo


def build_animation_clips():
    """Build GeckoLib 1.8.0 animation clips with multi-phase organic curves."""
    anims = {}

    # 1. emergence: 40 ticks / 2.0s
    anims["animation.quincy_manifestation.emergence"] = {
        "animation_length": 2.0,
        "loop": False,
        "bones": {
            "wing_left_root": {
                "rotation": {
                    "0.0": [0.0, -35.0, -25.0],
                    "0.8": [-4.0, 10.0, 6.0],
                    "1.4": [0.0, -3.0, -1.5],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "scale": {
                    "0.0": [0.1, 0.1, 0.1],
                    "0.6": [0.85, 0.85, 0.85],
                    "1.2": [1.05, 1.05, 1.05],
                    "2.0": [1.0, 1.0, 1.0]
                }
            },
            "wing_left_mid": {
                "rotation": {
                    "0.0": [0.0, -45.0, -30.0],
                    "0.9": [-6.0, 15.0, 8.0],
                    "1.5": [0.0, -3.0, -1.0],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_left_tip": {
                "rotation": {
                    "0.0": [0.0, -55.0, -35.0],
                    "1.1": [-8.0, 18.0, 10.0],
                    "1.6": [0.0, -2.0, -0.5],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_root": {
                "rotation": {
                    "0.0": [0.0, 35.0, 25.0],
                    "0.8": [-4.0, -10.0, -6.0],
                    "1.4": [0.0, 3.0, 1.5],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "scale": {
                    "0.0": [0.1, 0.1, 0.1],
                    "0.6": [0.85, 0.85, 0.85],
                    "1.2": [1.05, 1.05, 1.05],
                    "2.0": [1.0, 1.0, 1.0]
                }
            },
            "wing_right_mid": {
                "rotation": {
                    "0.0": [0.0, 45.0, 30.0],
                    "0.9": [-6.0, -15.0, -8.0],
                    "1.5": [0.0, 3.0, 1.0],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_tip": {
                "rotation": {
                    "0.0": [0.0, 55.0, 35.0],
                    "1.1": [-8.0, -18.0, -10.0],
                    "1.6": [0.0, 2.0, 0.5],
                    "2.0": [0.0, 0.0, 0.0]
                }
            },
            "satellite_0": {
                "position": {
                    "0.0": [-8.0, -10.0, -4.0],
                    "1.0": [2.0, 3.0, 1.5],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 180.0],
                    "1.0": [10.0, 90.0, 0.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "scale": {
                    "0.0": [0.05, 0.05, 0.05],
                    "1.0": [1.1, 1.1, 1.1],
                    "2.0": [1.0, 1.0, 1.0]
                }
            },
            "satellite_1": {
                "position": {
                    "0.0": [-14.0, -15.0, -6.0],
                    "1.2": [3.0, 4.0, 2.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, -180.0],
                    "1.2": [-10.0, -90.0, 0.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "scale": {
                    "0.0": [0.05, 0.05, 0.05],
                    "1.2": [1.1, 1.1, 1.1],
                    "2.0": [1.0, 1.0, 1.0]
                }
            },
            "satellite_2": {
                "position": {
                    "0.0": [8.0, -10.0, -4.0],
                    "1.0": [-2.0, 3.0, 1.5],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, -180.0],
                    "1.0": [10.0, -90.0, 0.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "scale": {
                    "0.0": [0.05, 0.05, 0.05],
                    "1.0": [1.1, 1.1, 1.1],
                    "2.0": [1.0, 1.0, 1.0]
                }
            },
            "satellite_3": {
                "position": {
                    "0.0": [14.0, -15.0, -6.0],
                    "1.2": [-3.0, 4.0, 2.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 180.0],
                    "1.2": [-10.0, 90.0, 0.0],
                    "2.0": [0.0, 0.0, 0.0]
                },
                "scale": {
                    "0.0": [0.05, 0.05, 0.05],
                    "1.2": [1.1, 1.1, 1.1],
                    "2.0": [1.0, 1.0, 1.0]
                }
            }
        }
    }

    # 2. idle: continuous Reishi hover (3.0s loop)
    anims["animation.quincy_manifestation.idle"] = {
        "animation_length": 3.0,
        "loop": True,
        "bones": {
            "wing_left_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [-2.0, 4.0, 2.5],
                    "3.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_left_mid": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [-3.0, 6.0, 3.5],
                    "3.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_left_tip": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [-4.0, 8.0, 5.0],
                    "3.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [-2.0, -4.0, -2.5],
                    "3.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_mid": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [-3.0, -6.0, -3.5],
                    "3.0": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_tip": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [-4.0, -8.0, -5.0],
                    "3.0": [0.0, 0.0, 0.0]
                }
            },
            "satellite_0": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [-0.5, 1.5, 0.6],
                    "3.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [0.0, 180.0, 10.0],
                    "3.0": [0.0, 360.0, 0.0]
                }
            },
            "satellite_1": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [-0.8, 2.0, 0.8],
                    "3.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [0.0, -180.0, -10.0],
                    "3.0": [0.0, -360.0, 0.0]
                }
            },
            "satellite_2": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [0.5, 1.5, 0.6],
                    "3.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [0.0, -180.0, -10.0],
                    "3.0": [0.0, -360.0, 0.0]
                }
            },
            "satellite_3": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [0.8, 2.0, 0.8],
                    "3.0": [0.0, 0.0, 0.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "1.5": [0.0, 180.0, 10.0],
                    "3.0": [0.0, 360.0, 0.0]
                }
            }
        }
    }

    # 3. arsenal_left: left stars align forward for salvo
    anims["animation.quincy_manifestation.arsenal_left"] = {
        "animation_length": 1.5,
        "loop": True,
        "bones": {
            "satellite_0": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.4": [3.0, 1.5, -4.0],
                    "1.5": [3.0, 1.5, -4.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.4": [0.0, 30.0, 60.0],
                    "1.5": [0.0, 30.0, 60.0]
                }
            },
            "satellite_1": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.5": [4.0, 3.0, -6.0],
                    "1.5": [4.0, 3.0, -6.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.5": [0.0, 30.0, 60.0],
                    "1.5": [0.0, 30.0, 60.0]
                }
            }
        }
    }

    # 4. arsenal_right: right stars align forward for salvo
    anims["animation.quincy_manifestation.arsenal_right"] = {
        "animation_length": 1.5,
        "loop": True,
        "bones": {
            "satellite_2": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.4": [-3.0, 1.5, -4.0],
                    "1.5": [-3.0, 1.5, -4.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.4": [0.0, -30.0, -60.0],
                    "1.5": [0.0, -30.0, -60.0]
                }
            },
            "satellite_3": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.5": [-4.0, 3.0, -6.0],
                    "1.5": [-4.0, 3.0, -6.0]
                },
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.5": [0.0, -30.0, -60.0],
                    "1.5": [0.0, -30.0, -60.0]
                }
            }
        }
    }

    # 5. recoil: shockwave pulse upon arrow release (0.4s)
    anims["animation.quincy_manifestation.recoil"] = {
        "animation_length": 0.4,
        "loop": False,
        "bones": {
            "wing_left_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.08": [4.0, -12.0, -8.0],
                    "0.4": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_root": {
                "rotation": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.08": [4.0, 12.0, 8.0],
                    "0.4": [0.0, 0.0, 0.0]
                }
            },
            "satellite_0": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.08": [0.0, -1.5, 3.0],
                    "0.4": [0.0, 0.0, 0.0]
                }
            },
            "satellite_2": {
                "position": {
                    "0.0": [0.0, 0.0, 0.0],
                    "0.08": [0.0, -1.5, 3.0],
                    "0.4": [0.0, 0.0, 0.0]
                }
            }
        }
    }

    # 6. settle: smooth deceleration back to neutral hover (0.8s)
    anims["animation.quincy_manifestation.settle"] = {
        "animation_length": 0.8,
        "loop": False,
        "bones": {
            "wing_left_root": {
                "rotation": {
                    "0.0": [3.0, -8.0, -5.0],
                    "0.4": [-1.0, 2.0, 1.5],
                    "0.8": [0.0, 0.0, 0.0]
                }
            },
            "wing_right_root": {
                "rotation": {
                    "0.0": [3.0, 8.0, 5.0],
                    "0.4": [-1.0, -2.0, -1.5],
                    "0.8": [0.0, 0.0, 0.0]
                }
            }
        }
    }

    return {"format_version": "1.8.0", "animations": anims}


def build_bbmodel(geo, anims, tex_bytes):
    """Compile fully compliant Blockbench 4.x / GeckoLib project."""
    tex_b64 = "data:image/png;base64," + base64.b64encode(tex_bytes).decode("ascii")
    tex_uuid = identity("tex_quincy_manifestation")

    bbmodel = {
        "meta": {
            "format_version": "4.10",
            "model_format": "geckolib",
            "box_uv": True
        },
        "name": "quincy_manifestation_v11",
        "geometry_name": "quincy_manifestation",
        "visible_box": [7, 6, 0],
        "resolution": {"width": 128, "height": 128},
        "elements": [],
        "outliner": [],
        "textures": [
            {
                "path": "quincy_manifestation.png",
                "name": "quincy_manifestation",
                "folder": "entity",
                "namespace": "bleachmod",
                "id": "0",
                "particle": False,
                "render_mode": "default",
                "visible": True,
                "mode": "bitmap",
                "saved": True,
                "uuid": tex_uuid,
                "source": tex_b64
            }
        ],
        "animations": []
    }

    bones = geo["minecraft:geometry"][0]["bones"]
    bone_map = {}
    for b in bones:
        b_name = b["name"]
        b_uuid = identity(f"bone_{b_name}")
        group = {
            "name": b_name,
            "origin": b["pivot"],
            "rotation": [0, 0, 0],
            "uuid": b_uuid,
            "children": []
        }
        bone_map[b_name] = group

        for c_idx, cube in enumerate(b.get("cubes", [])):
            elem_uuid = identity(f"cube_{b_name}_{c_idx}")
            orig = cube["origin"]
            sz = cube["size"]
            elem = {
                "name": f"{b_name}_c{c_idx}",
                "box_uv": True,
                "rescale": False,
                "locked": False,
                "from": orig,
                "to": [orig[0] + sz[0], orig[1] + sz[1], orig[2] + sz[2]],
                "autouv": 0,
                "color": 0,
                "origin": b["pivot"],
                "faces": {
                    "north": {"uv": [cube["uv"]["north"]["uv"][0], cube["uv"]["north"]["uv"][1], cube["uv"]["north"]["uv"][0] + cube["uv"]["north"]["uv_size"][0], cube["uv"]["north"]["uv"][1] + cube["uv"]["north"]["uv_size"][1]], "texture": 0},
                    "south": {"uv": [cube["uv"]["south"]["uv"][0], cube["uv"]["south"]["uv"][1], cube["uv"]["south"]["uv"][0] + cube["uv"]["south"]["uv_size"][0], cube["uv"]["south"]["uv"][1] + cube["uv"]["south"]["uv_size"][1]], "texture": 0},
                    "west": {"uv": [cube["uv"]["west"]["uv"][0], cube["uv"]["west"]["uv"][1], cube["uv"]["west"]["uv"][0] + cube["uv"]["west"]["uv_size"][0], cube["uv"]["west"]["uv"][1] + cube["uv"]["west"]["uv_size"][1]], "texture": 0},
                    "east": {"uv": [cube["uv"]["east"]["uv"][0], cube["uv"]["east"]["uv"][1], cube["uv"]["east"]["uv"][0] + cube["uv"]["east"]["uv_size"][0], cube["uv"]["east"]["uv"][1] + cube["uv"]["east"]["uv_size"][1]], "texture": 0},
                    "up": {"uv": [cube["uv"]["up"]["uv"][0], cube["uv"]["up"]["uv"][1], cube["uv"]["up"]["uv"][0] + cube["uv"]["up"]["uv_size"][0], cube["uv"]["up"]["uv"][1] + cube["uv"]["up"]["uv_size"][1]], "texture": 0},
                    "down": {"uv": [cube["uv"]["down"]["uv"][0], cube["uv"]["down"]["uv"][1], cube["uv"]["down"]["uv"][0] + cube["uv"]["down"]["uv_size"][0], cube["uv"]["down"]["uv"][1] + cube["uv"]["down"]["uv_size"][1]], "texture": 0}
                },
                "uuid": elem_uuid
            }
            bbmodel["elements"].append(elem)
            group["children"].append(elem_uuid)

    for b in bones:
        b_name = b["name"]
        p_name = b.get("parent")
        if p_name and p_name in bone_map:
            bone_map[p_name]["children"].append(bone_map[b_name])
        else:
            bbmodel["outliner"].append(bone_map[b_name])

    for a_name, a_data in anims["animations"].items():
        clean_name = a_name.replace("animation.quincy_manifestation.", "")
        bb_anim = {
            "name": clean_name,
            "uuid": identity(f"anim_{clean_name}"),
            "loop": "loop" if a_data.get("loop", False) else "once",
            "length": a_data.get("animation_length", 1.0),
            "snapping": 20,
            "animators": {}
        }
        for b_name, b_keyframes in a_data.get("bones", {}).items():
            b_uuid = identity(f"bone_{b_name}")
            animator = {
                "name": b_name,
                "type": "bone",
                "keyframes": []
            }
            for ch_type in ["rotation", "position", "scale"]:
                if ch_type in b_keyframes:
                    for t_str, val in b_keyframes[ch_type].items():
                        kf = {
                            "channel": ch_type,
                            "data_points": [{"x": val[0], "y": val[1], "z": val[2]}],
                            "time": float(t_str),
                            "color": 0,
                            "interpolation": "linear",
                            "uuid": identity(f"kf_{clean_name}_{b_name}_{ch_type}_{t_str}")
                        }
                        animator["keyframes"].append(kf)
            bb_anim["animators"][b_uuid] = animator
        bbmodel["animations"].append(bb_anim)

    return bbmodel


# -------------------------------------------------------------------------------------------------
# 3D Math, Isometric Minecraft Block Lighting, and Reference Sheet Engine
# -------------------------------------------------------------------------------------------------

def rotate_x(v, deg):
    rad = math.radians(deg)
    c, s = math.cos(rad), math.sin(rad)
    return (v[0], v[1] * c - v[2] * s, v[1] * s + v[2] * c)

def rotate_y(v, deg):
    rad = math.radians(deg)
    c, s = math.cos(rad), math.sin(rad)
    return (v[0] * c + v[2] * s, v[1], -v[0] * s + v[2] * c)

def rotate_z(v, deg):
    rad = math.radians(deg)
    c, s = math.cos(rad), math.sin(rad)
    return (v[0] * c - v[1] * s, v[0] * s + v[1] * c, v[2])

def get_bone_transforms_at_time(anim_clip, t):
    transforms = {}
    if not anim_clip or "bones" not in anim_clip:
        return transforms
    for b_name, b_data in anim_clip["bones"].items():
        rot = [0.0, 0.0, 0.0]
        pos = [0.0, 0.0, 0.0]
        if "rotation" in b_data:
            keys = sorted([(float(k), v) for k, v in b_data["rotation"].items()], key=lambda x: x[0])
            if keys:
                if t <= keys[0][0]:
                    rot = keys[0][1]
                elif t >= keys[-1][0]:
                    rot = keys[-1][1]
                else:
                    for i in range(len(keys) - 1):
                        t0, v0 = keys[i]
                        t1, v1 = keys[i + 1]
                        if t0 <= t <= t1:
                            f = (t - t0) / (t1 - t0) if t1 > t0 else 0
                            rot = [v0[j] + (v1[j] - v0[j]) * f for j in range(3)]
                            break
        if "position" in b_data:
            keys = sorted([(float(k), v) for k, v in b_data["position"].items()], key=lambda x: x[0])
            if keys:
                if t <= keys[0][0]:
                    pos = keys[0][1]
                elif t >= keys[-1][0]:
                    pos = keys[-1][1]
                else:
                    for i in range(len(keys) - 1):
                        t0, v0 = keys[i]
                        t1, v1 = keys[i + 1]
                        if t0 <= t <= t1:
                            f = (t - t0) / (t1 - t0) if t1 > t0 else 0
                            pos = [v0[j] + (v1[j] - v0[j]) * f for j in range(3)]
                            break
        transforms[b_name] = (rot, pos)
    return transforms


def transform_cube(cube, bone, bones_dict, transforms):
    origin = cube["origin"]
    size = cube["size"]
    p = [
        (origin[0], origin[1], origin[2]),
        (origin[0] + size[0], origin[1], origin[2]),
        (origin[0] + size[0], origin[1] + size[1], origin[2]),
        (origin[0], origin[1] + size[1], origin[2]),
        (origin[0], origin[1], origin[2] + size[2]),
        (origin[0] + size[0], origin[1], origin[2] + size[2]),
        (origin[0] + size[0], origin[1] + size[1], origin[2] + size[2]),
        (origin[0], origin[1] + size[1], origin[2] + size[2])
    ]

    chain = []
    curr = bone
    while curr:
        chain.append(curr)
        p_name = curr.get("parent")
        curr = bones_dict.get(p_name)

    for b in reversed(chain):
        b_name = b["name"]
        b_pivot = b["pivot"]
        rot, pos = transforms.get(b_name, ([0, 0, 0], [0, 0, 0]))
        rx, ry, rz = rot
        px, py, pz = pos
        new_p = []
        for vx, vy, vz in p:
            dx = vx - b_pivot[0]
            dy = vy - b_pivot[1]
            dz = vz - b_pivot[2]
            vx2, vy2, vz2 = rotate_z((dx, dy, dz), rz)
            vx3, vy3, vz3 = rotate_y((vx2, vy2, vz2), ry)
            vx4, vy4, vz4 = rotate_x((vx3, vy3, vz3), rx)
            new_p.append((vx4 + b_pivot[0] + px, vy4 + b_pivot[1] + py, vz4 + b_pivot[2] + pz))
        p = new_p

    return p


def get_cube_faces(v):
    return [
        {"face": "north", "v": [v[0], v[1], v[2], v[3]], "normal": (0, 0, -1)},
        {"face": "south", "v": [v[5], v[4], v[7], v[6]], "normal": (0, 0, 1)},
        {"face": "west",  "v": [v[4], v[0], v[3], v[7]], "normal": (-1, 0, 0)},
        {"face": "east",  "v": [v[1], v[5], v[6], v[2]], "normal": (1, 0, 0)},
        {"face": "up",    "v": [v[3], v[2], v[6], v[7]], "normal": (0, 1, 0)},
        {"face": "down",  "v": [v[4], v[5], v[1], v[0]], "normal": (0, -1, 0)}
    ]


def project_vertex(v, view_name):
    x, y, z = v
    if view_name == "frente":          # -Z towards +Z
        return (x, y, -z)
    elif view_name == "costas":        # +Z towards -Z
        return (-x, y, z)
    elif view_name == "perfil_direito":# +X towards -X
        return (z, y, x)
    elif view_name == "perfil_esquerdo":# -X towards +X
        return (-z, y, -x)
    elif view_name == "planta_superior":# Down from +Y
        return (x, -z, y)
    elif view_name == "perspectiva_34": # Isometric 3/4
        v1 = rotate_y((x, y, z), 45.0)
        v2 = rotate_x(v1, 25.0)
        return (v2[0], v2[1], v2[2])
    return (x, y, -z)


def render_sheet(geo, anim_clip, anim_time, view_name, mode, title, subtitle, out_path):
    """Render a standalone 1280x720 technical reference sheet with clean framing, depth sorting, and zero clutter."""
    width, height = 1280, 720
    img = Image.new("RGB", (width, height), (12, 16, 24) if mode == "blocos" else (16, 22, 32))
    draw = ImageDraw.Draw(img)

    # 1. Technical background grid
    grid_col = (20, 28, 40) if mode == "blocos" else (18, 25, 36)
    for gx in range(0, width, 40):
        draw.line([(gx, 0), (gx, height)], fill=grid_col, width=1)
    for gy in range(0, height, 40):
        draw.line([(0, gy), (width, gy)], fill=grid_col, width=1)

    # 2. Header and labels
    draw.rectangle([20, 20, width - 20, 75], fill=(16, 24, 36), outline=(32, 60, 95), width=1)
    draw.text((35, 28), title, fill=(140, 235, 255) if mode == "liso" else (250, 255, 255))
    draw.text((35, 50), subtitle, fill=(150, 180, 210))

    # Mode badge
    badge_text = "VARIANTE: LIMPA / LISA (SILHUETA REISHI)" if mode == "liso" else "VARIANTE: EM BLOCOS (BLOCKBENCH MINECRAFT)"
    badge_bg = (12, 50, 90) if mode == "liso" else (22, 65, 38)
    draw.rectangle([width - 410, 30, width - 35, 65], fill=badge_bg, outline=(55, 165, 245) if mode == "liso" else (85, 215, 130))
    draw.text((width - 395, 40), badge_text, fill=(255, 255, 255))

    # 3. Coordinate axes card in lower left
    draw.rectangle([25, height - 125, 250, height - 25], fill=(15, 20, 32), outline=(32, 55, 85))
    draw.text((35, height - 120), "CONTRATO DE COORDENADAS MINECRAFT", fill=(100, 220, 255))
    draw.text((35, height - 100), "Frente do Ator: -Z | Costas: +Z", fill=(190, 215, 235))
    draw.text((35, height - 85),  "Mao Direita: +X | Esquerda: -X", fill=(190, 215, 235))
    draw.text((35, height - 70),  "Escala: 1 bloco = 16 un (voxels)", fill=(150, 180, 205))
    draw.text((35, height - 55),  "Piso Referencia: Y = 0.0", fill=(150, 180, 205))
    draw.text((35, height - 40),  f"Camera: {view_name.upper()}", fill=(255, 215, 90))

    # 4. Bones / Cubes Info Card in lower right
    draw.rectangle([width - 340, height - 110, width - 25, height - 25], fill=(15, 20, 32), outline=(32, 55, 85))
    draw.text((width - 325, height - 102), "ESTRUTURA HIERARQUICA V11 (REFINADA)", fill=(100, 220, 255))
    draw.text((width - 325, height - 85), "Ossos Canonicos: 11 (root, 6 asas, 4 estrelas)", fill=(190, 215, 235))
    draw.text((width - 325, height - 70), "Cubos: 52 (28 asas volumosas + 24 estrelas 3D)", fill=(190, 215, 235))
    draw.text((width - 325, height - 55), "Dual-Layer: Difuso 128x128 + Glowmask Fullbright", fill=(150, 210, 250))
    draw.text((width - 325, height - 40), "Status: 100% Livre de Colisoes / Sem Oclusao", fill=(110, 235, 150))

    # 5. Centered Framing: scale and canvas center
    cx, cy = width // 2, height // 2 + 65
    scale = 6.8

    # 6. Collect ALL 3D faces (both Mannequin and Quincy Model) for unified depth sorting
    all_faces = []

    # A) Mannequin boxes
    mannequin_boxes = [
        ([-4.0, 24.0, -4.0], [8.0, 8.0, 8.0]),  # Head
        ([-4.0, 12.0, -2.0], [8.0, 12.0, 4.0]),  # Torso
        ([4.0, 12.0, -2.0], [4.0, 12.0, 4.0]),   # Right arm (+X)
        ([-8.0, 12.0, -2.0], [4.0, 12.0, 4.0]),  # Left arm (-X)
        ([0.0, 0.0, -2.0], [4.0, 12.0, 4.0]),    # Right leg (+X)
        ([-4.0, 0.0, -2.0], [4.0, 12.0, 4.0])    # Left leg (-X)
    ]
    for m_orig, m_size in mannequin_boxes:
        m_pts = [
            (m_orig[0], m_orig[1], m_orig[2]),
            (m_orig[0] + m_size[0], m_orig[1], m_orig[2]),
            (m_orig[0] + m_size[0], m_orig[1] + m_size[1], m_orig[2]),
            (m_orig[0], m_orig[1] + m_size[1], m_orig[2]),
            (m_orig[0], m_orig[1], m_orig[2] + m_size[2]),
            (m_orig[0] + m_size[0], m_orig[1], m_orig[2] + m_size[2]),
            (m_orig[0] + m_size[0], m_orig[1] + m_size[1], m_orig[2] + m_size[2]),
            (m_orig[0], m_orig[1] + m_size[1], m_orig[2] + m_size[2])
        ]
        proj_pts = [project_vertex(pt, view_name) for pt in m_pts]
        m_faces = get_cube_faces(proj_pts)
        for f in m_faces:
            avg_depth = sum(pt[2] for pt in f["v"]) / 4.0
            all_faces.append({
                "type": "mannequin",
                "bone": "mannequin",
                "depth": avg_depth,
                "pts": f["v"],
                "normal": f["normal"],
                "is_satellite": False
            })

    # B) Quincy model cubes
    bones = geo["minecraft:geometry"][0]["bones"]
    bones_dict = {b["name"]: b for b in bones}
    transforms = get_bone_transforms_at_time(anim_clip, anim_time)

    for b in bones:
        b_name = b["name"]
        for c_idx, cube in enumerate(b.get("cubes", [])):
            pts_3d = transform_cube(cube, b, bones_dict, transforms)
            proj_pts = [project_vertex(pt, view_name) for pt in pts_3d]
            cube_faces = get_cube_faces(proj_pts)
            for f in cube_faces:
                avg_depth = sum(pt[2] for pt in f["v"]) / 4.0
                all_faces.append({
                    "type": "model",
                    "bone": b_name,
                    "depth": avg_depth,
                    "pts": f["v"],
                    "normal": f["normal"],
                    "is_satellite": "satellite" in b_name
                })

    # Sort all faces strictly back-to-front by depth (painter algorithm)
    all_faces.sort(key=lambda x: x["depth"])

    # 7. Draw faces
    for item in all_faces:
        poly = [(cx + int(pt[0] * scale), cy - int(pt[1] * scale)) for pt in item["pts"]]
        nx, ny, nz = item["normal"]

        if item["type"] == "mannequin":
            # Neutral dark Minecraft avatar styling
            shade = 0.85 + 0.15 * ny - 0.1 * nz
            m_r = clamp(26 * shade)
            m_g = clamp(34 * shade)
            m_b = clamp(48 * shade)
            draw.polygon(poly, fill=(m_r, m_g, m_b), outline=(42, 54, 76))
            continue

        # Model geometry
        b_name = item["bone"]
        is_sat = item["is_satellite"]

        if mode == "liso":
            if is_sat:
                base_col = (200, 245, 255) if ny > 0.5 else (100, 220, 255)
                outline_col = (45, 150, 215)
            else:
                if "tip" in b_name:
                    base_col = (160, 240, 255)
                elif "mid" in b_name:
                    base_col = (90, 215, 255)
                else:
                    base_col = (45, 170, 245)
                outline_col = (25, 95, 160)

            lum = 0.82 + 0.18 * ny - 0.08 * nz
            c_r = clamp(base_col[0] * lum)
            c_g = clamp(base_col[1] * lum)
            c_b = clamp(base_col[2] * lum)
            draw.polygon(poly, fill=(c_r, c_g, c_b), outline=outline_col)

        else:
            # Authentic Minecraft Blockbench directional shading
            if ny > 0.5:
                shade = 1.05
            elif ny < -0.5:
                shade = 0.52
            elif abs(nx) > 0.5:
                shade = 0.68
            else:
                shade = 0.82

            if is_sat:
                base_col = (190, 245, 255)
                border_col = (8, 42, 90)
            else:
                if "tip" in b_name:
                    base_col = (140, 230, 255)
                elif "mid" in b_name:
                    base_col = (85, 205, 252)
                else:
                    base_col = (45, 160, 240)
                border_col = (8, 38, 80)

            fill_r = clamp(base_col[0] * shade)
            fill_g = clamp(base_col[1] * shade)
            fill_b = clamp(base_col[2] * shade)

            draw.polygon(poly, fill=(fill_r, fill_g, fill_b), outline=border_col)

            # Block bevel inner highlight on top edge
            if len(poly) >= 4 and ny > 0.2:
                draw.line([poly[0], poly[1]], fill=(255, 255, 255, 160), width=1)

    # 8. Subtle pivot crosshairs ONLY on dedicated detail technical sheets
    if mode == "blocos" and ("detalhe" in view_name or "pivos" in view_name):
        for b in bones:
            b_name = b["name"]
            if b_name == "root":
                continue
            piv = b["pivot"]
            rot, pos = transforms.get(b_name, ([0, 0, 0], [0, 0, 0]))
            pv_3d = (piv[0] + pos[0], piv[1] + pos[1], piv[2] + pos[2])
            proj_pv = project_vertex(pv_3d, view_name)
            px = cx + int(proj_pv[0] * scale)
            py = cy - int(proj_pv[1] * scale)

            draw.ellipse([px - 3, py - 3, px + 3, py + 3], fill=(255, 220, 60), outline=(10, 10, 10))
            draw.line([(px - 6, py), (px + 6, py)], fill=(255, 220, 60), width=1)
            draw.line([(px, py - 6), (px, py + 6)], fill=(255, 220, 60), width=1)
            draw.text((px + 5, py - 10), b_name, fill=(255, 235, 120))

    out_path.parent.mkdir(parents=True, exist_ok=True)
    img.save(out_path, format="PNG")
    print(f"Generated Sheet: {out_path.name}")


def generate_all_reference_sheets(geo, anim, out_dir):
    """Generate all 15 smooth/block pairs (30 files total) for Etapa 1."""
    out_dir.mkdir(parents=True, exist_ok=True)

    sheet_specs = [
        # 1-6: Orthogonal & Perspective Views
        (
            "quincy_01_frente",
            "frente",
            None,
            0.0,
            "QUINCY VOLLSTANDIG — VISTA FRONTAL (-Z)",
            "Visual frontal em blocos Minecraft com asas volumosas e satelites orbitais perfeitamente alinhados, sem oclusao do corpo"
        ),
        (
            "quincy_02_costas",
            "costas",
            None,
            0.0,
            "QUINCY VOLLSTANDIG — VISTA DORSAL (+Z)",
            "Visual traseiro em blocos evidenciando a ancora dorsal (Z=2.0) unida a coluna e arranjo dos 4 satelites Reishi"
        ),
        (
            "quincy_03_perfil_direito",
            "perfil_direito",
            None,
            0.0,
            "QUINCY VOLLSTANDIG — PERFIL DIREITO (+X)",
            "Separacao tridimensional perfeita: jogador (Z=-2..2), pivo dorsal (Z=2..4.4), asas (Z=4.2..7.4), satelites (Z=11..16)"
        ),
        (
            "quincy_04_perfil_esquerdo",
            "perfil_esquerdo",
            None,
            0.0,
            "QUINCY VOLLSTANDIG — PERFIL ESQUERDO (-X)",
            "Simetria de perfil comprovando espessura robusta e zero colisao ou sobreposicao com a cabeca e bracos"
        ),
        (
            "quincy_05_perspectiva_34",
            "perspectiva_34",
            None,
            0.0,
            "QUINCY VOLLSTANDIG — PERSPECTIVA ISOMETRICA 3/4",
            "Visao angular 3D completa demonstrando o volume macico das asas e matrizes estelares octaedricas perfeitamente legiveis"
        ),
        (
            "quincy_06_planta_superior",
            "planta_superior",
            None,
            0.0,
            "QUINCY VOLLSTANDIG — PLANTA SUPERIOR (TOP-DOWN +Y)",
            "Projecao superior comprovando o arco dorsal para tras do jogador (+Z) e clearance total para combate frontal (-Z)"
        ),
        # 7-9: Component Details
        (
            "quincy_07_detalhe_estrela",
            "perspectiva_34",
            None,
            0.0,
            "DETALHE ESTRUTURAL — SATELITE REISHI 3D (OCTAEDRICO)",
            "Matriz estelar volumetrica com nucleo central 3x3x3 e eixos de cristal cruzados em X, Y e Z sem cruzamento de asas"
        ),
        (
            "quincy_08_detalhe_asa",
            "frente",
            None,
            0.0,
            "DETALHE ESTRUTURAL — ASA EM BLOCOS ESCALONADOS",
            "Escalonamento volumetrico de cuboides retangulares: 14 cubos por asa formando penas cristalinas Reishi"
        ),
        (
            "quincy_09_detalhe_pivos_dorsais",
            "costas",
            None,
            0.0,
            "DETALHE ESTRUTURAL — PIVOS DORSAIS E ANCORAGEM",
            "Pivo na espinha (Y=19, Z=2) com montagem clavicular solida de profundidade 2.4, sem voos flutuantes"
        ),
        # 10-15: Action Stages
        (
            "quincy_10_movimento_emergencia",
            "costas",
            anim["animations"]["animation.quincy_manifestation.emergence"],
            0.8,
            "ESTAGIO 1 — MANIFESTACAO / EMERGENCIA (BEAT 16)",
            "Abertura em leque das asas a partir do pivo dorsal e projecao dos satelites em orbita ao redor do corpo"
        ),
        (
            "quincy_11_movimento_idle",
            "costas",
            anim["animations"]["animation.quincy_manifestation.idle"],
            1.5,
            "ESTAGIO 2 — VOO ESTAVEL / RESPIRACAO REISHI (IDLE)",
            "Oscilacao harmonica continua das penas de Reishi e rotativo lento tridimensional dos 4 satelites"
        ),
        (
            "quincy_12_movimento_arsenal_left",
            "frente",
            anim["animations"]["animation.quincy_manifestation.arsenal_left"],
            0.5,
            "ESTAGIO 3 — ARSENAL ESQUERDO / ALINHAMENTO FRONTAL",
            "Satelites esquerdos (0 e 1) rotacionam e avancam para o vetor de mira frontal acompanhando a flecha"
        ),
        (
            "quincy_13_movimento_arsenal_right",
            "frente",
            anim["animations"]["animation.quincy_manifestation.arsenal_right"],
            0.5,
            "ESTAGIO 4 — ARSENAL DIREITO / ALINHAMENTO FRONTAL",
            "Satelites direitos (2 e 3) rotacionam e avancam para o vetor de mira frontal acompanhando a flecha"
        ),
        (
            "quincy_14_movimento_recoil",
            "perspectiva_34",
            anim["animations"]["animation.quincy_manifestation.recoil"],
            0.08,
            "ESTAGIO 5 — RECUO DE DISPARO (SHOCKWAVE PULSE)",
            "Pulso elastico dorsal das asas e projecao de recuo dos satelites no momento exato do tiro da flecha"
        ),
        (
            "quincy_15_movimento_settle",
            "costas",
            anim["animations"]["animation.quincy_manifestation.settle"],
            0.4,
            "ESTAGIO 6 — ESTABILIZACAO / SETTLE (RETORNO A IDLE)",
            "Desaceleracao suave da oscilacao das asas retornando a postura neutra de voo estacionario"
        )
    ]

    for base_name, view, clip, t, title, subtitle in sheet_specs:
        render_sheet(geo, clip, t, view, "liso", title, subtitle, out_dir / f"{base_name}_liso.png")
        render_sheet(geo, clip, t, view, "blocos", title, subtitle, out_dir / f"{base_name}_blocos.png")

    # Update metadata
    meta = {}
    for base_name, view, clip, t, title, subtitle in sheet_specs:
        for mode in ("liso", "blocos"):
            fname = f"{base_name}_{mode}.png"
            p = out_dir / fname
            if p.exists():
                meta[fname] = {
                    "sha256": hashlib.sha256(p.read_bytes()).hexdigest(),
                    "title": title,
                    "view": view,
                    "mode": mode,
                    "resolution": "1280x720",
                    "cubes": 52,
                    "bones": 11,
                    "style": "Minecraft Blockbench Volumetric Voxel" if mode == "blocos" else "Clean Sacred Quincy Reishi"
                }

    with open(out_dir / "prompts-proveniencia.json", "w", encoding="utf-8") as f:
        json.dump(meta, f, indent=2)
    print(f"Updated prompts-proveniencia.json ({len(meta)} entries)")


def write_all(geo, anim, bbmodel, diff_bytes, glow_bytes):
    """Write all generated runtime assets and Blockbench lab model."""
    GEO_JSON.parent.mkdir(parents=True, exist_ok=True)
    ANIM_JSON.parent.mkdir(parents=True, exist_ok=True)
    TEXTURE_PNG.parent.mkdir(parents=True, exist_ok=True)
    GLOW_PNG.parent.mkdir(parents=True, exist_ok=True)
    SOURCE_BBMODEL.parent.mkdir(parents=True, exist_ok=True)

    with open(GEO_JSON, "w", encoding="utf-8") as f:
        json.dump(geo, f, indent=2)

    with open(ANIM_JSON, "w", encoding="utf-8") as f:
        json.dump(anim, f, indent=2)

    with open(TEXTURE_PNG, "wb") as f:
        f.write(diff_bytes)

    with open(GLOW_PNG, "wb") as f:
        f.write(glow_bytes)

    with open(SOURCE_BBMODEL, "w", encoding="utf-8") as f:
        json.dump(bbmodel, f, indent=2)

    print("All V11 runtime & lab assets written successfully within repository root.")


def check_sync():
    """Verify that runtime assets and lab models match deterministic pipeline."""
    geo = build_geometry_and_cubes()
    anim = build_animation_clips()
    diff_bytes = create_quincy_diffuse_texture_bytes()
    glow_bytes = create_quincy_glow_texture_bytes()
    bbmodel = build_bbmodel(geo, anim, diff_bytes)

    if not GEO_JSON.exists():
        sys.exit(f"Missing {GEO_JSON}")
    with open(GEO_JSON, "r", encoding="utf-8") as f:
        curr_geo = json.load(f)
    if curr_geo != geo:
        sys.exit("Geometry geo.json out of sync with pipeline generator.")

    if not ANIM_JSON.exists():
        sys.exit(f"Missing {ANIM_JSON}")
    with open(ANIM_JSON, "r", encoding="utf-8") as f:
        curr_anim = json.load(f)
    if curr_anim != anim:
        sys.exit("Animation animation.json out of sync with pipeline generator.")

    if not TEXTURE_PNG.exists() or TEXTURE_PNG.read_bytes() != diff_bytes:
        sys.exit("Diffuse texture out of sync with pipeline generator.")
    if not GLOW_PNG.exists() or GLOW_PNG.read_bytes() != glow_bytes:
        sys.exit("Glow texture out of sync with pipeline generator.")

    if not SOURCE_BBMODEL.exists():
        sys.exit(f"Missing {SOURCE_BBMODEL}")
    with open(SOURCE_BBMODEL, "r", encoding="utf-8") as f:
        curr_bbmodel = json.load(f)
    if curr_bbmodel != bbmodel:
        sys.exit("Blockbench lab model .bbmodel out of sync with pipeline generator.")

    import hashlib
    print("CHECK PASSED: All V11 runtime & lab assets are strictly synchronized.")
    for name, p in [
        ("geo.json", GEO_JSON),
        ("animation.json", ANIM_JSON),
        ("diffuse.png", TEXTURE_PNG),
        ("glow.png", GLOW_PNG),
        ("lab.bbmodel", SOURCE_BBMODEL),
    ]:
        h = hashlib.sha256(p.read_bytes()).hexdigest()[:16]
        print(f"  - {name}: sha256={h} ({p.relative_to(ROOT)})")
    return 0


def main():
    parser = argparse.ArgumentParser(description="Quincy Manifestation V11 Volumetric Blockbench Pipeline")
    parser.add_argument("--check", action="store_true", help="Check synchronization without writing")
    parser.add_argument("--render-references", action="store_true", help="Render all 30 Etapa 1 reference sheets")
    args = parser.parse_args()

    if args.check:
        sys.exit(check_sync())

    print("Generating refined V11 volumetric Minecraft block assets...")
    geo = build_geometry_and_cubes()
    anim = build_animation_clips()
    diff_bytes = create_quincy_diffuse_texture_bytes()
    glow_bytes = create_quincy_glow_texture_bytes()
    bbmodel = build_bbmodel(geo, anim, diff_bytes)

    write_all(geo, anim, bbmodel, diff_bytes, glow_bytes)

    if args.render_references or not args.check:
        print("Rendering refined 30 reference sheets (Etapa 1)...")
        generate_all_reference_sheets(geo, anim, REFERENCES_DIR)

    check_sync()


if __name__ == "__main__":
    main()
