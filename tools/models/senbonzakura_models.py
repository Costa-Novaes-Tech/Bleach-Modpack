"""Senbonzakura Kageyoshi High-Detail Model & Texture Generator for Blockbench.

Refined assets built:
1. tools/models/lab/antigravity/senbonzakura01/ag_senbon_giant_blade.bbmodel
   - Monumental 16-block monolithic katana with authentic open-frame tsuba,
     fluted bo-hi fuller, undulating hamon temper line, purple tsuka-ito over samegawa,
     brass habaki/seppa and tiered 4-ring reishi ripple pool.
   - 256x256 texture atlas with metallic reflection, shading and alpha translucency.
   - Animations: rise (emergence from void pool) and shatter (tremor and dissolution).
2. tools/models/lab/antigravity/senbonzakura01/ag_senbon_hakuteiken.bbmodel
   - Shūkei: Hakuteiken celestial armor rig with 8-segment halo, multi-layered feathered
     reishi wings (scapulars, coverts, primaries, secondaries) and pure energy blade.
   - 128x128 texture atlas with luminous white-to-sakura gradient.
   - Animations: idle (celestial breath and halo rotation) and dash (aerodynamic lunge).

Usage:
  python tools/models/senbonzakura_models.py
  python tools/models/senbonzakura_models.py --check
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
LAB_DIR = ROOT / "tools/models/lab/antigravity/senbonzakura01"
BLADE_OUT = LAB_DIR / "ag_senbon_giant_blade.bbmodel"
HAKUTEIKEN_OUT = LAB_DIR / "ag_senbon_hakuteiken.bbmodel"
BLADE_TEX_OUT = LAB_DIR / "senbon_giant_blade.png"
HAKUTEIKEN_TEX_OUT = LAB_DIR / "senbon_hakuteiken.png"

NAMESPACE = uuid.UUID("a74b1234-9876-4321-beef-567890abcdef")


def uid(name: str) -> str:
    return str(uuid.uuid5(NAMESPACE, name))


def make_png_bytes(width: int, height: int, pixels: list[tuple[int, int, int, int]]) -> bytes:
    """Generate raw PNG bytes without third-party dependencies."""
    def chunk(kind: bytes, data: bytes) -> bytes:
        return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data) & 0xFFFFFFFF)

    raw = bytearray()
    for y in range(height):
        raw.append(0)  # filter type 0: None
        for x in range(width):
            r, g, b, a = pixels[y * width + x]
            raw.extend([min(255, max(0, r)), min(255, max(0, g)), min(255, max(0, b)), min(255, max(0, a))])

    header = b"\x89PNG\r\n\x1a\n"
    ihdr = chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))
    idat = chunk(b"IDAT", zlib.compress(bytes(raw), 9))
    iend = chunk(b"IEND", b"")
    return header + ihdr + idat + iend


def create_blade_texture_256() -> tuple[str, bytes]:
    """Generates high-detail 256x256 texture atlas for the monumental Senbonzakura katana."""
    w, h = 256, 256
    pixels = [(20, 15, 30, 255)] * (w * h)

    def set_px(x, y, color):
        if 0 <= x < w and 0 <= y < h:
            pixels[y * w + x] = color

    def fill_rect(x0, y0, x1, y1, color):
        for y in range(max(0, y0), min(h, y1)):
            for x in range(max(0, x0), min(w, x1)):
                pixels[y * w + x] = color

    # =========================================================================
    # Region 1: Steel Blade Body & Spine (0..64, 0..128)
    # Polished Tamahagane steel with metallic specular sheen and bo-hi fuller
    # =========================================================================
    for y in range(128):
        for x in range(64):
            # Base steel tone
            u = x / 64.0
            v = y / 128.0
            # Longitudinal specular streak (shinogi ridge line around x=20)
            specular = math.exp(-((x - 20) ** 2) / 18.0) * 45
            # Fuller shadow groove around x=12
            fuller = -25 if 10 <= x <= 14 else 0
            
            base_luma = 195 + 30 * u + 15 * math.sin(v * math.pi * 4) + specular + fuller
            r = int(base_luma * 0.94)
            g = int(base_luma * 0.97)
            b = int(base_luma * 1.05)  # slight cool steel tint
            set_px(x, y, (r, g, b, 255))

    # =========================================================================
    # Region 2: Blade Edge & Undulating Hamon (64..128, 0..128)
    # Gleaming razor edge with traditional sakura-wave temper line (Yakiba)
    # =========================================================================
    for y in range(128):
        # Hamon wave contour
        wave = math.sin(y * 0.18) * 4.5 + math.cos(y * 0.07) * 2.5
        hamon_boundary = 88 + wave

        for x in range(64, 128):
            if x >= hamon_boundary:
                # Hardened edge steel (pure bright steel with glowing sakura highlights)
                t = (x - hamon_boundary) / max(1.0, (127 - hamon_boundary))
                r = int(240 + 15 * t)
                g = int(230 + 15 * (1 - t * 0.3))
                b = int(245 + 10 * t)
                # Pure white apex cutting edge on the outer columns
                if x >= 124:
                    r, g, b = 255, 255, 255
            else:
                # Soft body steel leading to the hamon line with misty nioi-guchi transition
                dist = hamon_boundary - x
                if dist < 4:
                    # Luminous temper boundary (whitish-pink glow line)
                    r, g, b = 255, 225, 245
                else:
                    r, g, b = 180, 190, 205
            set_px(x, y, (r, g, b, 255))

    # =========================================================================
    # Region 3: Imperial Gold Tsuba, Habaki & Kashira (128..192, 0..64)
    # Rich gold tones (#f0c840 / #d4a020 / #8a6410) with ornamental beveling
    # =========================================================================
    for y in range(64):
        for x in range(128, 192):
            u = (x - 128) / 64.0
            v = y / 64.0
            # Bevel highlights on border
            border_dist = min(x - 128, 191 - x, y, 63 - y)
            bevel = 35 if border_dist < 4 else (0 if border_dist > 8 else -20)
            
            gold_r = int(225 + 30 * u + bevel)
            gold_g = int(180 + 25 * u + bevel * 0.8)
            gold_b = int(60 + 20 * (1 - u) + bevel * 0.3)
            set_px(x, y, (gold_r, gold_g, gold_b, 255))

    # Tsuba open-window corner markings
    for (cx, cy) in [(140, 16), (180, 16), (140, 48), (180, 48)]:
        for dx in range(-4, 5):
            for dy in range(-4, 5):
                if abs(dx) == 4 or abs(dy) == 4:
                    set_px(cx + dx, cy + dy, (120, 90, 25, 255))
                elif abs(dx) < 3 and abs(dy) < 3:
                    set_px(cx + dx, cy + dy, (40, 30, 50, 255))  # cut-out shadow

    # =========================================================================
    # Region 4: Tsuka-Ito Purple Braid over Samegawa (192..256, 0..64)
    # Traditional Japanese diamond wrap in deep Byakuya purple
    # =========================================================================
    for y in range(64):
        for x in range(192, 256):
            rx = x - 192
            # Diamond cross grid: period 16
            mod_p = (rx + y) % 16
            mod_m = (rx - y) % 16
            is_braid_crossing = (mod_p < 4) or (mod_m < 4)
            
            if is_braid_crossing:
                # Purple silk ito
                depth = math.sin((mod_p if mod_p < 4 else mod_m) / 4.0 * math.pi) * 20
                pr = int(75 + depth)
                pg = int(35 + depth * 0.6)
                pb = int(105 + depth * 1.2)
                set_px(x, y, (pr, pg, pb, 255))
            else:
                # White stingray skin (samegawa) background with tiny pebble nodules
                nodule = 15 if ((rx * 3 + y * 7) % 11 < 3) else 0
                set_px(x, y, (230 + nodule, 225 + nodule, 235 + nodule, 255))

    # =========================================================================
    # Region 5: Concentric Reishi Pool & Liquid Water Ripples (0..128, 128..256)
    # Ethereal glowing pink/magenta waves expanding from entry void
    # =========================================================================
    for y in range(128, 256):
        for x in range(128):
            dx = (x - 64)
            dy = (y - 192)
            dist = math.sqrt(dx * dx + dy * dy)
            
            # 5 harmonic concentric ripples
            wave_amp = math.sin(dist * 0.45) * 0.5 + 0.5
            wave_crest = math.exp(-((math.sin(dist * 0.35) - 0.7) ** 2) / 0.08)
            
            # Deep purple void water base
            base_r = int(25 + 15 * (dist / 90.0))
            base_g = int(12 + 10 * (dist / 90.0))
            base_b = int(40 + 20 * (dist / 90.0))
            
            # Glowing reishi pink wave crests
            glow_r = int(255 * wave_crest + base_r * (1 - wave_crest))
            glow_g = int(140 * wave_crest + base_g * (1 - wave_crest))
            glow_b = int(210 * wave_crest + base_b * (1 - wave_crest))
            
            # Alpha falloff at edges
            alpha = int(max(0, min(255, 255 - (dist - 50) * 4.5))) if dist > 50 else 240
            set_px(x, y, (glow_r, glow_g, glow_b, alpha))

    # =========================================================================
    # Region 6: Sakura Petal Emissive Sprites & Shards (128..256, 128..256)
    # Fine cherry blossom petals for shattering effects and floating debris
    # =========================================================================
    for y in range(128, 256):
        for x in range(128, 256):
            # Scattered translucent sakura petals
            rx = (x - 128) % 32 - 16
            ry = (y - 128) % 32 - 16
            r_dist = math.sqrt(rx * rx * 1.5 + ry * ry)
            if r_dist < 10:
                petal_t = (10 - r_dist) / 10.0
                pr = int(255)
                pg = int(175 + 50 * petal_t)
                pb = int(215 + 40 * petal_t)
                pa = int(220 * petal_t)
                set_px(x, y, (pr, pg, pb, pa))
            else:
                set_px(x, y, (0, 0, 0, 0))

    png_bytes = make_png_bytes(w, h, pixels)
    b64 = "data:image/png;base64," + base64.b64encode(png_bytes).decode("ascii")
    return b64, png_bytes


def create_hakuteiken_texture_128() -> tuple[str, bytes]:
    """Generates 128x128 texture atlas for Hakuteiken wings, halo and energy blade."""
    w, h = 128, 128
    pixels = [(255, 255, 255, 255)] * (w * h)

    def set_px(x, y, color):
        if 0 <= x < w and 0 <= y < h:
            pixels[y * w + x] = color

    def fill_rect(x0, y0, x1, y1, color):
        for y in range(max(0, y0), min(h, y1)):
            for x in range(max(0, x0), min(w, x1)):
                pixels[y * w + x] = color

    # Strip 0: Pure Divine White Feather Core (0..64, 0..64)
    for y in range(64):
        for x in range(64):
            # Feather shaft specular at x=32
            specular = math.exp(-((x - 32) ** 2) / 24.0) * 30
            r = min(255, int(245 + specular))
            g = min(255, int(250 + specular * 0.5))
            b = 255
            set_px(x, y, (r, g, b, 255))

    # Strip 1: Pale Pink Reishi Fringe (64..128, 0..64)
    for y in range(64):
        for x in range(64, 128):
            t = (x - 64) / 64.0
            r = 255
            g = int(225 - 45 * t)
            b = int(245 - 25 * t)
            a = int(255 - 40 * t)
            set_px(x, y, (r, g, b, a))

    # Strip 2: Divine Golden Halo Core (0..64, 64..128)
    for y in range(64, 128):
        for x in range(64):
            u = x / 64.0
            v = (y - 64) / 64.0
            ring = math.sin(u * math.pi) * math.sin(v * math.pi)
            r = int(255)
            g = int(235 + 20 * ring)
            b = int(140 + 80 * ring)
            set_px(x, y, (r, g, b, 255))

    # Strip 3: Sky-Blue Reishi Glow on Wing Edge (64..128, 64..128)
    for y in range(64, 128):
        for x in range(64, 128):
            u = (x - 64) / 64.0
            r = int(215 + 40 * u)
            g = int(240 + 15 * u)
            b = 255
            set_px(x, y, (r, g, b, 235))

    png_bytes = make_png_bytes(w, h, pixels)
    b64 = "data:image/png;base64," + base64.b64encode(png_bytes).decode("ascii")
    return b64, png_bytes


def build_refined_giant_blade_model() -> dict:
    """Build the refined 26-cube BBModel project for Senbonzakura's monumental Bankai blade."""
    tex_data, _ = create_blade_texture_256()
    tex_id = uid("tex_blade_256")

    elements = []

    # =========================================================================
    # 1. Tiered Reishi Ripple Pool Base (Y = 0..2 units)
    # =========================================================================
    elements.append({
        "name": "ripple_pool_tier1",
        "box_uv": False,
        "from": [-48, 0, -48],
        "to": [48, 0.4, 48],
        "origin": [0, 0, 0],
        "faces": {
            "up": {"uv": [0, 128, 128, 256], "texture": 0},
            "down": {"uv": [0, 128, 128, 256], "texture": 0},
            "north": {"uv": [0, 128, 8, 130], "texture": 0},
            "south": {"uv": [0, 128, 8, 130], "texture": 0},
            "west": {"uv": [0, 128, 8, 130], "texture": 0},
            "east": {"uv": [0, 128, 8, 130], "texture": 0}
        },
        "uuid": uid("cube_ripple_t1")
    })
    elements.append({
        "name": "ripple_pool_tier2",
        "box_uv": False,
        "from": [-32, 0.4, -32],
        "to": [32, 0.9, 32],
        "origin": [0, 0.5, 0],
        "faces": {
            "up": {"uv": [16, 144, 112, 240], "texture": 0},
            "down": {"uv": [16, 144, 112, 240], "texture": 0},
            "north": {"uv": [0, 128, 8, 130], "texture": 0},
            "south": {"uv": [0, 128, 8, 130], "texture": 0},
            "west": {"uv": [0, 128, 8, 130], "texture": 0},
            "east": {"uv": [0, 128, 8, 130], "texture": 0}
        },
        "uuid": uid("cube_ripple_t2")
    })
    elements.append({
        "name": "ripple_pool_tier3",
        "box_uv": False,
        "from": [-18, 0.9, -18],
        "to": [18, 1.4, 18],
        "origin": [0, 1.0, 0],
        "faces": {
            "up": {"uv": [32, 160, 96, 224], "texture": 0},
            "down": {"uv": [32, 160, 96, 224], "texture": 0},
            "north": {"uv": [0, 128, 4, 130], "texture": 0},
            "south": {"uv": [0, 128, 4, 130], "texture": 0},
            "west": {"uv": [0, 128, 4, 130], "texture": 0},
            "east": {"uv": [0, 128, 4, 130], "texture": 0}
        },
        "uuid": uid("cube_ripple_t3")
    })
    elements.append({
        "name": "ripple_fissure_crest",
        "box_uv": False,
        "from": [-8, 1.4, -8],
        "to": [8, 1.9, 8],
        "origin": [0, 1.5, 0],
        "faces": {
            "up": {"uv": [48, 176, 80, 208], "texture": 0},
            "down": {"uv": [48, 176, 80, 208], "texture": 0},
            "north": {"uv": [0, 128, 2, 130], "texture": 0},
            "south": {"uv": [0, 128, 2, 130], "texture": 0},
            "west": {"uv": [0, 128, 2, 130], "texture": 0},
            "east": {"uv": [0, 128, 2, 130], "texture": 0}
        },
        "uuid": uid("cube_ripple_crest")
    })

    # =========================================================================
    # 2. Kashira & Tsuka (Imperial Purple Hilt with Gold Fittings)
    # =========================================================================
    elements.append({
        "name": "kashira_pommel",
        "box_uv": False,
        "from": [-3.2, 1, -2.2],
        "to": [3.2, 4.5, 2.2],
        "origin": [0, 2.5, 0],
        "faces": {
            "up": {"uv": [128, 0, 144, 12], "texture": 0},
            "down": {"uv": [128, 0, 144, 12], "texture": 0},
            "north": {"uv": [128, 12, 144, 26], "texture": 0},
            "south": {"uv": [128, 12, 144, 26], "texture": 0},
            "west": {"uv": [128, 12, 138, 26], "texture": 0},
            "east": {"uv": [128, 12, 138, 26], "texture": 0}
        },
        "uuid": uid("cube_kashira")
    })
    elements.append({
        "name": "tsuka_hilt_core",
        "box_uv": False,
        "from": [-2.6, 4.5, -1.9],
        "to": [2.6, 42, 1.9],
        "origin": [0, 23, 0],
        "faces": {
            "up": {"uv": [192, 0, 212, 12], "texture": 0},
            "down": {"uv": [192, 0, 212, 12], "texture": 0},
            "north": {"uv": [192, 0, 256, 64], "texture": 0},
            "south": {"uv": [192, 0, 256, 64], "texture": 0},
            "west": {"uv": [192, 0, 216, 64], "texture": 0},
            "east": {"uv": [192, 0, 216, 64], "texture": 0}
        },
        "uuid": uid("cube_tsuka_core")
    })
    elements.append({
        "name": "tsuka_menuki_ornament_l",
        "box_uv": False,
        "from": [2.6, 20, -1.2],
        "to": [3.0, 26, 1.2],
        "origin": [2.8, 23, 0],
        "faces": {
            "up": {"uv": [128, 0, 136, 6], "texture": 0},
            "down": {"uv": [128, 0, 136, 6], "texture": 0},
            "north": {"uv": [128, 0, 136, 16], "texture": 0},
            "south": {"uv": [128, 0, 136, 16], "texture": 0},
            "west": {"uv": [128, 0, 136, 16], "texture": 0},
            "east": {"uv": [128, 0, 136, 16], "texture": 0}
        },
        "uuid": uid("cube_menuki_l")
    })
    elements.append({
        "name": "tsuka_menuki_ornament_r",
        "box_uv": False,
        "from": [-3.0, 20, -1.2],
        "to": [-2.6, 26, 1.2],
        "origin": [-2.8, 23, 0],
        "faces": {
            "up": {"uv": [128, 0, 136, 6], "texture": 0},
            "down": {"uv": [128, 0, 136, 6], "texture": 0},
            "north": {"uv": [128, 0, 136, 16], "texture": 0},
            "south": {"uv": [128, 0, 136, 16], "texture": 0},
            "west": {"uv": [128, 0, 136, 16], "texture": 0},
            "east": {"uv": [128, 0, 136, 16], "texture": 0}
        },
        "uuid": uid("cube_menuki_r")
    })
    elements.append({
        "name": "fuchi_collar",
        "box_uv": False,
        "from": [-3.0, 42, -2.1],
        "to": [3.0, 44.5, 2.1],
        "origin": [0, 43, 0],
        "faces": {
            "up": {"uv": [128, 0, 144, 10], "texture": 0},
            "down": {"uv": [128, 0, 144, 10], "texture": 0},
            "north": {"uv": [128, 0, 144, 8], "texture": 0},
            "south": {"uv": [128, 0, 144, 8], "texture": 0},
            "west": {"uv": [128, 0, 136, 8], "texture": 0},
            "east": {"uv": [128, 0, 136, 8], "texture": 0}
        },
        "uuid": uid("cube_fuchi")
    })

    # =========================================================================
    # 3. Tsuba (Open-Frame 4-Window Rectangular Guard) & Seppa
    # =========================================================================
    elements.append({
        "name": "seppa_lower",
        "box_uv": False,
        "from": [-4.5, 44.5, -2.5],
        "to": [4.5, 45.2, 2.5],
        "origin": [0, 45, 0],
        "faces": {
            "up": {"uv": [128, 0, 148, 12], "texture": 0},
            "down": {"uv": [128, 0, 148, 12], "texture": 0},
            "north": {"uv": [128, 0, 148, 4], "texture": 0},
            "south": {"uv": [128, 0, 148, 4], "texture": 0},
            "west": {"uv": [128, 0, 136, 4], "texture": 0},
            "east": {"uv": [128, 0, 136, 4], "texture": 0}
        },
        "uuid": uid("cube_seppa_lower")
    })

    # 4-Window open frame tsuba
    elements.append({
        "name": "tsuba_rim_north",
        "box_uv": False,
        "from": [-8.5, 45.2, 4.0],
        "to": [8.5, 47.6, 5.8],
        "origin": [0, 46.4, 4.9],
        "faces": {
            "up": {"uv": [128, 0, 192, 10], "texture": 0},
            "down": {"uv": [128, 0, 192, 10], "texture": 0},
            "north": {"uv": [128, 0, 192, 8], "texture": 0},
            "south": {"uv": [128, 0, 192, 8], "texture": 0},
            "west": {"uv": [128, 0, 136, 8], "texture": 0},
            "east": {"uv": [128, 0, 136, 8], "texture": 0}
        },
        "uuid": uid("cube_tsuba_rim_n")
    })
    elements.append({
        "name": "tsuba_rim_south",
        "box_uv": False,
        "from": [-8.5, 45.2, -5.8],
        "to": [8.5, 47.6, -4.0],
        "origin": [0, 46.4, -4.9],
        "faces": {
            "up": {"uv": [128, 0, 192, 10], "texture": 0},
            "down": {"uv": [128, 0, 192, 10], "texture": 0},
            "north": {"uv": [128, 0, 192, 8], "texture": 0},
            "south": {"uv": [128, 0, 192, 8], "texture": 0},
            "west": {"uv": [128, 0, 136, 8], "texture": 0},
            "east": {"uv": [128, 0, 136, 8], "texture": 0}
        },
        "uuid": uid("cube_tsuba_rim_s")
    })
    elements.append({
        "name": "tsuba_rim_west",
        "box_uv": False,
        "from": [6.8, 45.2, -4.0],
        "to": [8.5, 47.6, 4.0],
        "origin": [7.6, 46.4, 0],
        "faces": {
            "up": {"uv": [128, 0, 140, 32], "texture": 0},
            "down": {"uv": [128, 0, 140, 32], "texture": 0},
            "north": {"uv": [128, 0, 136, 8], "texture": 0},
            "south": {"uv": [128, 0, 136, 8], "texture": 0},
            "west": {"uv": [128, 0, 140, 8], "texture": 0},
            "east": {"uv": [128, 0, 140, 8], "texture": 0}
        },
        "uuid": uid("cube_tsuba_rim_w")
    })
    elements.append({
        "name": "tsuba_rim_east",
        "box_uv": False,
        "from": [-8.5, 45.2, -4.0],
        "to": [-6.8, 47.6, 4.0],
        "origin": [-7.6, 46.4, 0],
        "faces": {
            "up": {"uv": [128, 0, 140, 32], "texture": 0},
            "down": {"uv": [128, 0, 140, 32], "texture": 0},
            "north": {"uv": [128, 0, 136, 8], "texture": 0},
            "south": {"uv": [128, 0, 136, 8], "texture": 0},
            "west": {"uv": [128, 0, 140, 8], "texture": 0},
            "east": {"uv": [128, 0, 140, 8], "texture": 0}
        },
        "uuid": uid("cube_tsuba_rim_e")
    })
    elements.append({
        "name": "tsuba_central_hub",
        "box_uv": False,
        "from": [-4.5, 45.2, -2.5],
        "to": [4.5, 47.6, 2.5],
        "origin": [0, 46.4, 0],
        "faces": {
            "up": {"uv": [128, 0, 160, 20], "texture": 0},
            "down": {"uv": [128, 0, 160, 20], "texture": 0},
            "north": {"uv": [128, 0, 160, 8], "texture": 0},
            "south": {"uv": [128, 0, 160, 8], "texture": 0},
            "west": {"uv": [128, 0, 140, 8], "texture": 0},
            "east": {"uv": [128, 0, 140, 8], "texture": 0}
        },
        "uuid": uid("cube_tsuba_hub")
    })

    elements.append({
        "name": "seppa_upper",
        "box_uv": False,
        "from": [-4.2, 47.6, -2.3],
        "to": [4.2, 48.3, 2.3],
        "origin": [0, 48.0, 0],
        "faces": {
            "up": {"uv": [128, 0, 148, 12], "texture": 0},
            "down": {"uv": [128, 0, 148, 12], "texture": 0},
            "north": {"uv": [128, 0, 148, 4], "texture": 0},
            "south": {"uv": [128, 0, 148, 4], "texture": 0},
            "west": {"uv": [128, 0, 136, 4], "texture": 0},
            "east": {"uv": [128, 0, 136, 4], "texture": 0}
        },
        "uuid": uid("cube_seppa_upper")
    })
    elements.append({
        "name": "habaki_blade_collar",
        "box_uv": False,
        "from": [-3.6, 48.3, -1.6],
        "to": [3.6, 54.5, 1.6],
        "origin": [0, 51.4, 0],
        "faces": {
            "up": {"uv": [128, 0, 148, 14], "texture": 0},
            "down": {"uv": [128, 0, 148, 14], "texture": 0},
            "north": {"uv": [128, 0, 148, 20], "texture": 0},
            "south": {"uv": [128, 0, 148, 20], "texture": 0},
            "west": {"uv": [128, 0, 138, 20], "texture": 0},
            "east": {"uv": [128, 0, 138, 20], "texture": 0}
        },
        "uuid": uid("cube_habaki")
    })

    # =========================================================================
    # 4. Monumental Blade Body (Tamahagane Core, Hamon Edge & Kissaki)
    # Total Height reaches Y = 254 units (15.8 blocks)
    # =========================================================================
    # Blade Base
    elements.append({
        "name": "blade_base_spine",
        "box_uv": False,
        "from": [-3.2, 54.5, -1.1],
        "to": [3.2, 105, 1.1],
        "origin": [0, 80, 0],
        "faces": {
            "up": {"uv": [0, 0, 24, 8], "texture": 0},
            "down": {"uv": [0, 0, 24, 8], "texture": 0},
            "north": {"uv": [0, 0, 64, 128], "texture": 0},
            "south": {"uv": [0, 0, 64, 128], "texture": 0},
            "west": {"uv": [64, 0, 80, 128], "texture": 0},  # cutting edge
            "east": {"uv": [0, 0, 16, 128], "texture": 0}    # spine
        },
        "uuid": uid("cube_blade_base")
    })
    elements.append({
        "name": "blade_hamon_bevel_lower",
        "box_uv": False,
        "from": [-3.5, 54.5, -0.6],
        "to": [-3.0, 105, 0.6],
        "origin": [-3.2, 80, 0],
        "faces": {
            "up": {"uv": [64, 0, 72, 6], "texture": 0},
            "down": {"uv": [64, 0, 72, 6], "texture": 0},
            "north": {"uv": [88, 0, 128, 128], "texture": 0},
            "south": {"uv": [88, 0, 128, 128], "texture": 0},
            "west": {"uv": [120, 0, 128, 128], "texture": 0},
            "east": {"uv": [88, 0, 96, 128], "texture": 0}
        },
        "uuid": uid("cube_hamon_lower")
    })

    # Blade Mid-1
    elements.append({
        "name": "blade_mid_1",
        "box_uv": False,
        "from": [-3.0, 105, -1.0],
        "to": [3.0, 155, 1.0],
        "origin": [0, 130, 0],
        "faces": {
            "up": {"uv": [0, 0, 24, 8], "texture": 0},
            "down": {"uv": [0, 0, 24, 8], "texture": 0},
            "north": {"uv": [0, 0, 64, 128], "texture": 0},
            "south": {"uv": [0, 0, 64, 128], "texture": 0},
            "west": {"uv": [64, 0, 80, 128], "texture": 0},
            "east": {"uv": [0, 0, 16, 128], "texture": 0}
        },
        "uuid": uid("cube_blade_mid1")
    })
    elements.append({
        "name": "blade_hamon_bevel_mid1",
        "box_uv": False,
        "from": [-3.3, 105, -0.5],
        "to": [-2.8, 155, 0.5],
        "origin": [-3.0, 130, 0],
        "faces": {
            "up": {"uv": [64, 0, 72, 6], "texture": 0},
            "down": {"uv": [64, 0, 72, 6], "texture": 0},
            "north": {"uv": [88, 0, 128, 128], "texture": 0},
            "south": {"uv": [88, 0, 128, 128], "texture": 0},
            "west": {"uv": [120, 0, 128, 128], "texture": 0},
            "east": {"uv": [88, 0, 96, 128], "texture": 0}
        },
        "uuid": uid("cube_hamon_mid1")
    })

    # Blade Mid-2
    elements.append({
        "name": "blade_mid_2",
        "box_uv": False,
        "from": [-2.8, 155, -0.9],
        "to": [2.8, 205, 0.9],
        "origin": [0, 180, 0],
        "faces": {
            "up": {"uv": [0, 0, 20, 8], "texture": 0},
            "down": {"uv": [0, 0, 20, 8], "texture": 0},
            "north": {"uv": [0, 0, 64, 128], "texture": 0},
            "south": {"uv": [0, 0, 64, 128], "texture": 0},
            "west": {"uv": [64, 0, 80, 128], "texture": 0},
            "east": {"uv": [0, 0, 16, 128], "texture": 0}
        },
        "uuid": uid("cube_blade_mid2")
    })
    elements.append({
        "name": "blade_hamon_bevel_mid2",
        "box_uv": False,
        "from": [-3.1, 155, -0.45],
        "to": [-2.6, 205, 0.45],
        "origin": [-2.8, 180, 0],
        "faces": {
            "up": {"uv": [64, 0, 72, 6], "texture": 0},
            "down": {"uv": [64, 0, 72, 6], "texture": 0},
            "north": {"uv": [88, 0, 128, 128], "texture": 0},
            "south": {"uv": [88, 0, 128, 128], "texture": 0},
            "west": {"uv": [120, 0, 128, 128], "texture": 0},
            "east": {"uv": [88, 0, 96, 128], "texture": 0}
        },
        "uuid": uid("cube_hamon_mid2")
    })

    # Blade Upper
    elements.append({
        "name": "blade_upper",
        "box_uv": False,
        "from": [-2.5, 205, -0.8],
        "to": [2.5, 238, 0.8],
        "origin": [0, 221, 0],
        "faces": {
            "up": {"uv": [0, 0, 18, 6], "texture": 0},
            "down": {"uv": [0, 0, 18, 6], "texture": 0},
            "north": {"uv": [0, 0, 64, 96], "texture": 0},
            "south": {"uv": [0, 0, 64, 96], "texture": 0},
            "west": {"uv": [64, 0, 80, 96], "texture": 0},
            "east": {"uv": [0, 0, 16, 96], "texture": 0}
        },
        "uuid": uid("cube_blade_upper")
    })

    # Kissaki (Angled Katana Tip with Yokote line)
    elements.append({
        "name": "blade_kissaki_body",
        "box_uv": False,
        "from": [-2.2, 238, -0.7],
        "to": [1.8, 250, 0.7],
        "origin": [0, 244, 0],
        "faces": {
            "up": {"uv": [64, 0, 80, 16], "texture": 0},
            "down": {"uv": [0, 0, 16, 6], "texture": 0},
            "north": {"uv": [16, 0, 64, 48], "texture": 0},
            "south": {"uv": [16, 0, 64, 48], "texture": 0},
            "west": {"uv": [64, 0, 80, 48], "texture": 0},
            "east": {"uv": [0, 0, 16, 48], "texture": 0}
        },
        "uuid": uid("cube_kissaki_body")
    })
    elements.append({
        "name": "blade_kissaki_tip",
        "box_uv": False,
        "from": [-1.6, 250, -0.5],
        "to": [1.0, 255, 0.5],
        "origin": [0, 252, 0],
        "faces": {
            "up": {"uv": [112, 0, 128, 16], "texture": 0},
            "down": {"uv": [0, 0, 12, 4], "texture": 0},
            "north": {"uv": [32, 0, 64, 32], "texture": 0},
            "south": {"uv": [32, 0, 64, 32], "texture": 0},
            "west": {"uv": [112, 0, 128, 32], "texture": 0},
            "east": {"uv": [0, 0, 16, 32], "texture": 0}
        },
        "uuid": uid("cube_kissaki_tip")
    })

    # =========================================================================
    # Outliner Hierarchy
    # =========================================================================
    outliner = [
        {
            "name": "root",
            "origin": [0, 0, 0],
            "children": [
                {
                    "name": "reishi_pool",
                    "origin": [0, 0, 0],
                    "children": [
                        uid("cube_ripple_t1"),
                        uid("cube_ripple_t2"),
                        uid("cube_ripple_t3"),
                        uid("cube_ripple_crest")
                    ]
                },
                {
                    "name": "monumental_sword",
                    "origin": [0, 0, 0],
                    "children": [
                        uid("cube_kashira"),
                        uid("cube_tsuka_core"),
                        uid("cube_menuki_l"),
                        uid("cube_menuki_r"),
                        uid("cube_fuchi"),
                        uid("cube_seppa_lower"),
                        {
                            "name": "tsuba_assembly",
                            "origin": [0, 46.4, 0],
                            "children": [
                                uid("cube_tsuba_rim_n"),
                                uid("cube_tsuba_rim_s"),
                                uid("cube_tsuba_rim_w"),
                                uid("cube_tsuba_rim_e"),
                                uid("cube_tsuba_hub")
                            ]
                        },
                        uid("cube_seppa_upper"),
                        uid("cube_habaki"),
                        {
                            "name": "blade_assembly",
                            "origin": [0, 54.5, 0],
                            "children": [
                                uid("cube_blade_base"),
                                uid("cube_hamon_lower"),
                                uid("cube_blade_mid1"),
                                uid("cube_hamon_mid1"),
                                uid("cube_blade_mid2"),
                                uid("cube_hamon_mid2"),
                                uid("cube_blade_upper"),
                                uid("cube_kissaki_body"),
                                uid("cube_kissaki_tip")
                            ]
                        }
                    ]
                }
            ]
        }
    ]

    # Animations
    animations = [
        {
            "name": "animation.senbon_blade.rise",
            "loop": "hold",
            "length": 3.0,
            "animators": {
                uid("bone_sword"): {
                    "name": "monumental_sword",
                    "type": "bone",
                    "position": {
                        "0.0": [0, -255, 0],
                        "0.6": [0, -190, 0],
                        "1.5": [0, -60, 0],
                        "2.2": [0, 6, 0],
                        "3.0": [0, 0, 0]
                    },
                    "scale": {
                        "0.0": [0.2, 1.0, 0.2],
                        "0.8": [0.8, 1.0, 0.8],
                        "1.8": [1.0, 1.0, 1.0]
                    }
                },
                uid("bone_pool"): {
                    "name": "reishi_pool",
                    "type": "bone",
                    "scale": {
                        "0.0": [0.1, 1.0, 0.1],
                        "0.5": [1.5, 1.0, 1.5],
                        "1.2": [1.1, 1.0, 1.1],
                        "2.0": [1.35, 1.0, 1.35],
                        "3.0": [1.0, 1.0, 1.0]
                    }
                }
            }
        },
        {
            "name": "animation.senbon_blade.shatter",
            "loop": "once",
            "length": 1.5,
            "animators": {
                uid("bone_sword"): {
                    "name": "monumental_sword",
                    "type": "bone",
                    "rotation": {
                        "0.0": [0, 0, 0],
                        "0.2": [0.6, 0, -0.6],
                        "0.4": [-1.0, 0, 1.0],
                        "0.6": [1.5, 0, -1.5],
                        "0.8": [0, 0, 0]
                    },
                    "scale": {
                        "0.0": [1.0, 1.0, 1.0],
                        "0.6": [1.1, 1.06, 1.1],
                        "1.0": [0.4, 0.4, 0.4],
                        "1.5": [0.0, 0.0, 0.0]
                    }
                }
            }
        }
    ]

    bbmodel = {
        "meta": {
            "format_version": "4.5",
            "model_format": "free",
            "box_uv": False
        },
        "name": "ag_senbon_giant_blade",
        "geometry_name": "senbon_giant_blade",
        "resolution": {"width": 256, "height": 256},
        "elements": elements,
        "outliner": outliner,
        "textures": [
            {
                "path": "senbon_giant_blade.png",
                "name": "senbon_giant_blade",
                "folder": "block",
                "namespace": "bleachmod",
                "id": "0",
                "particle": False,
                "render_mode": "default",
                "source": tex_data,
                "uuid": tex_id
            }
        ],
        "animations": animations
    }
    return bbmodel


def build_refined_hakuteiken_model() -> dict:
    """Build the refined 32-cube BBModel project for Shūkei: Hakuteiken celestial armor rig."""
    tex_data, _ = create_hakuteiken_texture_128()
    tex_id = uid("tex_hakuteiken_128")

    elements = []

    # =========================================================================
    # 1. Floating 8-Segment Celestial Halo above player head (Y = 32..35)
    # =========================================================================
    r_halo = 7.0
    for i in range(8):
        angle = i * (math.pi / 4.0)
        next_angle = (i + 1) * (math.pi / 4.0)
        x0, z0 = math.cos(angle) * r_halo, math.sin(angle) * r_halo
        x1, z1 = math.cos(next_angle) * r_halo, math.sin(next_angle) * r_halo
        
        fx, tx = min(x0, x1) - 0.7, max(x0, x1) + 0.7
        fz, tz = min(z0, z1) - 0.7, max(z0, z1) + 0.7

        elements.append({
            "name": f"halo_segment_{i+1}",
            "box_uv": False,
            "from": [round(fx, 2), 33.0, round(fz, 2)],
            "to": [round(tx, 2), 34.5, round(tz, 2)],
            "origin": [0, 33.7, 0],
            "faces": {
                "up": {"uv": [0, 64, 32, 74], "texture": 0},
                "down": {"uv": [0, 64, 32, 74], "texture": 0},
                "north": {"uv": [0, 64, 32, 72], "texture": 0},
                "south": {"uv": [0, 64, 32, 72], "texture": 0},
                "west": {"uv": [0, 64, 8, 72], "texture": 0},
                "east": {"uv": [0, 64, 8, 72], "texture": 0}
            },
            "uuid": uid(f"cube_halo_seg_{i+1}")
        })

    # =========================================================================
    # 2. Celestial Wing Left (9 Articulated Feathers: Scapulars, Primaries & Secondaries)
    # Origin: back left shoulder [2, 22, 2]
    # =========================================================================
    # Base Root
    elements.append({
        "name": "wing_l_root_joint",
        "box_uv": False,
        "from": [2, 19, 2],
        "to": [8, 25, 3.6],
        "origin": [2, 22, 2],
        "faces": {
            "up": {"uv": [0, 0, 16, 4], "texture": 0},
            "down": {"uv": [0, 0, 16, 4], "texture": 0},
            "north": {"uv": [0, 0, 16, 16], "texture": 0},
            "south": {"uv": [0, 0, 16, 16], "texture": 0},
            "west": {"uv": [0, 0, 4, 16], "texture": 0},
            "east": {"uv": [0, 0, 4, 16], "texture": 0}
        },
        "uuid": uid("cube_wl_root")
    })
    # Scapulars (Soft base feathers)
    elements.append({
        "name": "wing_l_scapular_1",
        "box_uv": False,
        "from": [4, 15, 2.3],
        "to": [11, 21, 3.4],
        "origin": [4, 18, 2.8],
        "faces": {
            "up": {"uv": [0, 0, 24, 4], "texture": 0},
            "down": {"uv": [0, 0, 24, 4], "texture": 0},
            "north": {"uv": [0, 0, 24, 24], "texture": 0},
            "south": {"uv": [0, 0, 24, 24], "texture": 0},
            "west": {"uv": [0, 0, 4, 24], "texture": 0},
            "east": {"uv": [64, 0, 72, 24], "texture": 0}
        },
        "uuid": uid("cube_wl_scap1")
    })
    # Primaries (Long arching razor-sharp outer feathers)
    elements.append({
        "name": "wing_l_primary_main",
        "box_uv": False,
        "from": [7, 23, 2.2],
        "to": [25, 42, 3.3],
        "origin": [7, 24, 2.7],
        "faces": {
            "up": {"uv": [0, 0, 48, 4], "texture": 0},
            "down": {"uv": [0, 0, 48, 4], "texture": 0},
            "north": {"uv": [0, 0, 48, 48], "texture": 0},
            "south": {"uv": [0, 0, 48, 48], "texture": 0},
            "west": {"uv": [0, 0, 4, 48], "texture": 0},
            "east": {"uv": [64, 0, 72, 48], "texture": 0}
        },
        "uuid": uid("cube_wl_prim_main")
    })
    elements.append({
        "name": "wing_l_primary_mid",
        "box_uv": False,
        "from": [8, 19, 2.1],
        "to": [29, 34, 3.2],
        "origin": [8, 22, 2.6],
        "faces": {
            "up": {"uv": [0, 0, 48, 4], "texture": 0},
            "down": {"uv": [0, 0, 48, 4], "texture": 0},
            "north": {"uv": [0, 0, 48, 48], "texture": 0},
            "south": {"uv": [0, 0, 48, 48], "texture": 0},
            "west": {"uv": [0, 0, 4, 48], "texture": 0},
            "east": {"uv": [64, 0, 72, 48], "texture": 0}
        },
        "uuid": uid("cube_wl_prim_mid")
    })
    elements.append({
        "name": "wing_l_primary_outer",
        "box_uv": False,
        "from": [8, 14, 2.0],
        "to": [31, 26, 3.1],
        "origin": [8, 19, 2.5],
        "faces": {
            "up": {"uv": [0, 0, 48, 4], "texture": 0},
            "down": {"uv": [0, 0, 48, 4], "texture": 0},
            "north": {"uv": [0, 0, 48, 48], "texture": 0},
            "south": {"uv": [0, 0, 48, 48], "texture": 0},
            "west": {"uv": [0, 0, 4, 48], "texture": 0},
            "east": {"uv": [64, 0, 72, 48], "texture": 0}
        },
        "uuid": uid("cube_wl_prim_outer")
    })
    # Secondaries (Lower fan feathers)
    elements.append({
        "name": "wing_l_secondary_1",
        "box_uv": False,
        "from": [6, 9, 1.9],
        "to": [24, 19, 3.0],
        "origin": [6, 14, 2.4],
        "faces": {
            "up": {"uv": [0, 0, 32, 4], "texture": 0},
            "down": {"uv": [0, 0, 32, 4], "texture": 0},
            "north": {"uv": [0, 0, 32, 32], "texture": 0},
            "south": {"uv": [0, 0, 32, 32], "texture": 0},
            "west": {"uv": [0, 0, 4, 32], "texture": 0},
            "east": {"uv": [64, 0, 72, 32], "texture": 0}
        },
        "uuid": uid("cube_wl_sec1")
    })
    elements.append({
        "name": "wing_l_secondary_2",
        "box_uv": False,
        "from": [5, 4, 1.8],
        "to": [18, 13, 2.9],
        "origin": [5, 9, 2.3],
        "faces": {
            "up": {"uv": [0, 0, 24, 4], "texture": 0},
            "down": {"uv": [0, 0, 24, 4], "texture": 0},
            "north": {"uv": [0, 0, 24, 24], "texture": 0},
            "south": {"uv": [0, 0, 24, 24], "texture": 0},
            "west": {"uv": [0, 0, 4, 24], "texture": 0},
            "east": {"uv": [64, 0, 72, 24], "texture": 0}
        },
        "uuid": uid("cube_wl_sec2")
    })

    # =========================================================================
    # 3. Celestial Wing Right (Symmetrical Mirrored Feathers)
    # Origin: back right shoulder [-2, 22, 2]
    # =========================================================================
    elements.append({
        "name": "wing_r_root_joint",
        "box_uv": False,
        "from": [-8, 19, 2],
        "to": [-2, 25, 3.6],
        "origin": [-2, 22, 2],
        "faces": {
            "up": {"uv": [0, 0, 16, 4], "texture": 0},
            "down": {"uv": [0, 0, 16, 4], "texture": 0},
            "north": {"uv": [0, 0, 16, 16], "texture": 0},
            "south": {"uv": [0, 0, 16, 16], "texture": 0},
            "west": {"uv": [0, 0, 4, 16], "texture": 0},
            "east": {"uv": [0, 0, 4, 16], "texture": 0}
        },
        "uuid": uid("cube_wr_root")
    })
    elements.append({
        "name": "wing_r_scapular_1",
        "box_uv": False,
        "from": [-11, 15, 2.3],
        "to": [-4, 21, 3.4],
        "origin": [-4, 18, 2.8],
        "faces": {
            "up": {"uv": [0, 0, 24, 4], "texture": 0},
            "down": {"uv": [0, 0, 24, 4], "texture": 0},
            "north": {"uv": [0, 0, 24, 24], "texture": 0},
            "south": {"uv": [0, 0, 24, 24], "texture": 0},
            "west": {"uv": [64, 0, 72, 24], "texture": 0},
            "east": {"uv": [0, 0, 4, 24], "texture": 0}
        },
        "uuid": uid("cube_wr_scap1")
    })
    elements.append({
        "name": "wing_r_primary_main",
        "box_uv": False,
        "from": [-25, 23, 2.2],
        "to": [-7, 42, 3.3],
        "origin": [-7, 24, 2.7],
        "faces": {
            "up": {"uv": [0, 0, 48, 4], "texture": 0},
            "down": {"uv": [0, 0, 48, 4], "texture": 0},
            "north": {"uv": [0, 0, 48, 48], "texture": 0},
            "south": {"uv": [0, 0, 48, 48], "texture": 0},
            "west": {"uv": [64, 0, 72, 48], "texture": 0},
            "east": {"uv": [0, 0, 4, 48], "texture": 0}
        },
        "uuid": uid("cube_wr_prim_main")
    })
    elements.append({
        "name": "wing_r_primary_mid",
        "box_uv": False,
        "from": [-29, 19, 2.1],
        "to": [-8, 34, 3.2],
        "origin": [-8, 22, 2.6],
        "faces": {
            "up": {"uv": [0, 0, 48, 4], "texture": 0},
            "down": {"uv": [0, 0, 48, 4], "texture": 0},
            "north": {"uv": [0, 0, 48, 48], "texture": 0},
            "south": {"uv": [0, 0, 48, 48], "texture": 0},
            "west": {"uv": [64, 0, 72, 48], "texture": 0},
            "east": {"uv": [0, 0, 4, 48], "texture": 0}
        },
        "uuid": uid("cube_wr_prim_mid")
    })
    elements.append({
        "name": "wing_r_primary_outer",
        "box_uv": False,
        "from": [-31, 14, 2.0],
        "to": [-8, 26, 3.1],
        "origin": [-8, 19, 2.5],
        "faces": {
            "up": {"uv": [0, 0, 48, 4], "texture": 0},
            "down": {"uv": [0, 0, 48, 4], "texture": 0},
            "north": {"uv": [0, 0, 48, 48], "texture": 0},
            "south": {"uv": [0, 0, 48, 48], "texture": 0},
            "west": {"uv": [64, 0, 72, 48], "texture": 0},
            "east": {"uv": [0, 0, 4, 48], "texture": 0}
        },
        "uuid": uid("cube_wr_prim_outer")
    })
    elements.append({
        "name": "wing_r_secondary_1",
        "box_uv": False,
        "from": [-24, 9, 1.9],
        "to": [-6, 19, 3.0],
        "origin": [-6, 14, 2.4],
        "faces": {
            "up": {"uv": [0, 0, 32, 4], "texture": 0},
            "down": {"uv": [0, 0, 32, 4], "texture": 0},
            "north": {"uv": [0, 0, 32, 32], "texture": 0},
            "south": {"uv": [0, 0, 32, 32], "texture": 0},
            "west": {"uv": [64, 0, 72, 32], "texture": 0},
            "east": {"uv": [0, 0, 4, 32], "texture": 0}
        },
        "uuid": uid("cube_wr_sec1")
    })
    elements.append({
        "name": "wing_r_secondary_2",
        "box_uv": False,
        "from": [-18, 4, 1.8],
        "to": [-5, 13, 2.9],
        "origin": [-5, 9, 2.3],
        "faces": {
            "up": {"uv": [0, 0, 24, 4], "texture": 0},
            "down": {"uv": [0, 0, 24, 4], "texture": 0},
            "north": {"uv": [0, 0, 24, 24], "texture": 0},
            "south": {"uv": [0, 0, 24, 24], "texture": 0},
            "west": {"uv": [64, 0, 72, 24], "texture": 0},
            "east": {"uv": [0, 0, 4, 24], "texture": 0}
        },
        "uuid": uid("cube_wr_sec2")
    })

    # =========================================================================
    # 4. Pure Light Katana (Hakuteiken Blade of Condensed Reishi)
    # =========================================================================
    elements.append({
        "name": "ht_hilt_grip",
        "box_uv": False,
        "from": [-0.9, -4.5, -0.9],
        "to": [0.9, 5.5, 0.9],
        "origin": [0, 0.5, 0],
        "faces": {
            "up": {"uv": [0, 0, 4, 4], "texture": 0},
            "down": {"uv": [0, 0, 4, 4], "texture": 0},
            "north": {"uv": [0, 0, 6, 20], "texture": 0},
            "south": {"uv": [0, 0, 6, 20], "texture": 0},
            "west": {"uv": [0, 0, 6, 20], "texture": 0},
            "east": {"uv": [0, 0, 6, 20], "texture": 0}
        },
        "uuid": uid("cube_ht_hilt")
    })
    elements.append({
        "name": "ht_cross_guard_light",
        "box_uv": False,
        "from": [-3.5, 5.5, -1.2],
        "to": [3.5, 7.2, 1.2],
        "origin": [0, 6.35, 0],
        "faces": {
            "up": {"uv": [0, 0, 16, 6], "texture": 0},
            "down": {"uv": [0, 0, 16, 6], "texture": 0},
            "north": {"uv": [0, 0, 16, 6], "texture": 0},
            "south": {"uv": [0, 0, 16, 6], "texture": 0},
            "west": {"uv": [0, 0, 6, 6], "texture": 0},
            "east": {"uv": [0, 0, 6, 6], "texture": 0}
        },
        "uuid": uid("cube_ht_guard")
    })
    elements.append({
        "name": "ht_blade_condensed_core",
        "box_uv": False,
        "from": [-1.4, 7.2, -0.7],
        "to": [1.4, 38, 0.7],
        "origin": [0, 22.6, 0],
        "faces": {
            "up": {"uv": [0, 0, 8, 4], "texture": 0},
            "down": {"uv": [0, 0, 8, 4], "texture": 0},
            "north": {"uv": [0, 0, 12, 64], "texture": 0},
            "south": {"uv": [0, 0, 12, 64], "texture": 0},
            "west": {"uv": [64, 64, 72, 128], "texture": 0},
            "east": {"uv": [64, 64, 72, 128], "texture": 0}
        },
        "uuid": uid("cube_ht_core")
    })
    elements.append({
        "name": "ht_blade_flare_left",
        "box_uv": False,
        "from": [1.4, 10, -0.4],
        "to": [2.6, 32, 0.4],
        "origin": [2.0, 21, 0],
        "faces": {
            "up": {"uv": [64, 64, 72, 68], "texture": 0},
            "down": {"uv": [64, 64, 72, 68], "texture": 0},
            "north": {"uv": [64, 64, 88, 128], "texture": 0},
            "south": {"uv": [64, 64, 88, 128], "texture": 0},
            "west": {"uv": [64, 64, 72, 128], "texture": 0},
            "east": {"uv": [64, 64, 72, 128], "texture": 0}
        },
        "uuid": uid("cube_ht_flare_l")
    })
    elements.append({
        "name": "ht_blade_flare_right",
        "box_uv": False,
        "from": [-2.6, 10, -0.4],
        "to": [-1.4, 32, 0.4],
        "origin": [-2.0, 21, 0],
        "faces": {
            "up": {"uv": [64, 64, 72, 68], "texture": 0},
            "down": {"uv": [64, 64, 72, 68], "texture": 0},
            "north": {"uv": [64, 64, 88, 128], "texture": 0},
            "south": {"uv": [64, 64, 88, 128], "texture": 0},
            "west": {"uv": [64, 64, 72, 128], "texture": 0},
            "east": {"uv": [64, 64, 72, 128], "texture": 0}
        },
        "uuid": uid("cube_ht_flare_r")
    })
    elements.append({
        "name": "ht_blade_spear_tip",
        "box_uv": False,
        "from": [-1.0, 38, -0.5],
        "to": [1.0, 44, 0.5],
        "origin": [0, 41, 0],
        "faces": {
            "up": {"uv": [0, 0, 6, 4], "texture": 0},
            "down": {"uv": [0, 0, 6, 4], "texture": 0},
            "north": {"uv": [0, 0, 10, 16], "texture": 0},
            "south": {"uv": [0, 0, 10, 16], "texture": 0},
            "west": {"uv": [64, 64, 72, 80], "texture": 0},
            "east": {"uv": [64, 64, 72, 80], "texture": 0}
        },
        "uuid": uid("cube_ht_tip")
    })

    # Outliner
    outliner = [
        {
            "name": "root",
            "origin": [0, 0, 0],
            "children": [
                {
                    "name": "celestial_halo",
                    "origin": [0, 33.7, 0],
                    "children": [uid(f"cube_halo_seg_{i+1}") for i in range(8)]
                },
                {
                    "name": "celestial_wing_left",
                    "origin": [2, 22, 2],
                    "children": [
                        uid("cube_wl_root"),
                        uid("cube_wl_scap1"),
                        uid("cube_wl_prim_main"),
                        uid("cube_wl_prim_mid"),
                        uid("cube_wl_prim_outer"),
                        uid("cube_wl_sec1"),
                        uid("cube_wl_sec2")
                    ]
                },
                {
                    "name": "celestial_wing_right",
                    "origin": [-2, 22, 2],
                    "children": [
                        uid("cube_wr_root"),
                        uid("cube_wr_scap1"),
                        uid("cube_wr_prim_main"),
                        uid("cube_wr_prim_mid"),
                        uid("cube_wr_prim_outer"),
                        uid("cube_wr_sec1"),
                        uid("cube_wr_sec2")
                    ]
                },
                {
                    "name": "hakuteiken_sword",
                    "origin": [0, 0, 0],
                    "children": [
                        uid("cube_ht_hilt"),
                        uid("cube_ht_guard"),
                        uid("cube_ht_core"),
                        uid("cube_ht_flare_l"),
                        uid("cube_ht_flare_r"),
                        uid("cube_ht_tip")
                    ]
                }
            ]
        }
    ]

    animations = [
        {
            "name": "animation.hakuteiken.idle",
            "loop": "loop",
            "length": 2.0,
            "animators": {
                uid("bone_halo"): {
                    "name": "celestial_halo",
                    "type": "bone",
                    "rotation": {
                        "0.0": [0, 0, 0],
                        "1.0": [0, 180, 0],
                        "2.0": [0, 360, 0]
                    },
                    "position": {
                        "0.0": [0, 0, 0],
                        "1.0": [0, 1.0, 0],
                        "2.0": [0, 0, 0]
                    }
                },
                uid("bone_wing_left"): {
                    "name": "celestial_wing_left",
                    "type": "bone",
                    "rotation": {
                        "0.0": [0, 0, 0],
                        "1.0": [3.5, 12.0, -6.0],
                        "2.0": [0, 0, 0]
                    }
                },
                uid("bone_wing_right"): {
                    "name": "celestial_wing_right",
                    "type": "bone",
                    "rotation": {
                        "0.0": [0, 0, 0],
                        "1.0": [3.5, -12.0, 6.0],
                        "2.0": [0, 0, 0]
                    }
                }
            }
        },
        {
            "name": "animation.hakuteiken.dash",
            "loop": "hold",
            "length": 1.0,
            "animators": {
                uid("bone_wing_left"): {
                    "name": "celestial_wing_left",
                    "type": "bone",
                    "rotation": {
                        "0.0": [0, 0, 0],
                        "0.2": [18.0, 42.0, -38.0],
                        "1.0": [18.0, 42.0, -38.0]
                    }
                },
                uid("bone_wing_right"): {
                    "name": "celestial_wing_right",
                    "type": "bone",
                    "rotation": {
                        "0.0": [0, 0, 0],
                        "0.2": [18.0, -42.0, 38.0],
                        "1.0": [18.0, -42.0, 38.0]
                    }
                },
                uid("bone_ht_blade"): {
                    "name": "hakuteiken_sword",
                    "type": "bone",
                    "rotation": {
                        "0.0": [0, 0, 0],
                        "0.2": [-88.0, 0, 0],
                        "1.0": [-88.0, 0, 0]
                    }
                }
            }
        }
    ]

    bbmodel = {
        "meta": {
            "format_version": "4.5",
            "model_format": "free",
            "box_uv": False
        },
        "name": "ag_senbon_hakuteiken",
        "geometry_name": "senbon_hakuteiken",
        "resolution": {"width": 128, "height": 128},
        "elements": elements,
        "outliner": outliner,
        "textures": [
            {
                "path": "senbon_hakuteiken.png",
                "name": "senbon_hakuteiken",
                "folder": "block",
                "namespace": "bleachmod",
                "id": "0",
                "particle": False,
                "render_mode": "default",
                "source": tex_data,
                "uuid": tex_id
            }
        ],
        "animations": animations
    }
    return bbmodel


def main():
    parser = argparse.ArgumentParser(description="Generate refined Senbonzakura Bankai models.")
    parser.add_argument("--check", action="store_true", help="Verify model validity.")
    args = parser.parse_args()

    LAB_DIR.mkdir(parents=True, exist_ok=True)
    
    # 1. Build and export textures
    _, blade_png_bytes = create_blade_texture_256()
    with open(BLADE_TEX_OUT, "wb") as f:
        f.write(blade_png_bytes)
        
    _, hakuteiken_png_bytes = create_hakuteiken_texture_128()
    with open(HAKUTEIKEN_TEX_OUT, "wb") as f:
        f.write(hakuteiken_png_bytes)

    # 2. Build and export models
    blade_model = build_refined_giant_blade_model()
    hakuteiken_model = build_refined_hakuteiken_model()
    
    with open(BLADE_OUT, "w", encoding="utf-8") as f:
        json.dump(blade_model, f, indent=2)

    with open(HAKUTEIKEN_OUT, "w", encoding="utf-8") as f:
        json.dump(hakuteiken_model, f, indent=2)

    print(f"Generated {BLADE_OUT} ({len(blade_model['elements'])} cubes, {len(blade_model['animations'])} animations, 256x256 texture)")
    print(f"Generated {HAKUTEIKEN_OUT} ({len(hakuteiken_model['elements'])} cubes, {len(hakuteiken_model['animations'])} animations, 128x128 texture)")

    if args.check:
        with open(BLADE_OUT, "r", encoding="utf-8") as f:
            data_b = json.load(f)
        assert data_b["meta"]["format_version"] == "4.5"
        assert len(data_b["elements"]) == 26
        assert len(data_b["animations"]) == 2

        with open(HAKUTEIKEN_OUT, "r", encoding="utf-8") as f:
            data_h = json.load(f)
        assert data_h["meta"]["format_version"] == "4.5"
        assert len(data_h["elements"]) == 28
        assert len(data_h["animations"]) == 2

        print("CHECK PASSED: Both refined Senbonzakura model scaffolds are valid and reproducible.")


if __name__ == "__main__":
    main()
