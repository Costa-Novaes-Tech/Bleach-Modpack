"""Generate the Blockbench source model and GeckoLib runtime assets for the Three-Headed Ice Hydra.

Outputs:
  - tools/models/source/ice_hydra.bbmodel
  - src/main/resources/assets/bleachmod/geo/ice_hydra.geo.json
  - src/main/resources/assets/bleachmod/animations/ice_hydra.animation.json
  - src/main/resources/assets/bleachmod/textures/entity/ice_hydra.png
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
SOURCE_BBMODEL = ROOT / "tools/models/source/ice_hydra.bbmodel"
GEO_JSON = ROOT / "src/main/resources/assets/bleachmod/geo/ice_hydra.geo.json"
ANIM_JSON = ROOT / "src/main/resources/assets/bleachmod/animations/ice_hydra.animation.json"
TEXTURE_PNG = ROOT / "src/main/resources/assets/bleachmod/textures/entity/ice_hydra.png"

NAMESPACE = uuid.UUID("74a8120d-83f1-4df2-a39c-5bc484fe7301")


def identity(name: str) -> str:
    return str(uuid.uuid5(NAMESPACE, name))


def clamp(v: float | int) -> int:
    return max(0, min(255, int(v)))


def generate_hydra_texture_bytes() -> bytes:
    """Generate a 128x128 RGBA texture with crystalline ice gradients, dark oral depth and cyan luminous eyes."""
    width, height = 128, 128
    raw_pixels = bytearray(width * height * 4)

    for y in range(height):
        for x in range(width):
            idx = (y * width + x) * 4

            # Zone 0: Eyes palette (x: 0..15, y: 0..15) -> Piercing Draconic Icy Cyan Eye with deep frame
            if x < 16 and y < 16:
                if x in (0, 1, 14, 15) or y in (0, 1, 14, 15):
                    raw_pixels[idx : idx + 4] = bytes([15, 38, 68, 255])
                elif x in (6, 7, 8, 9) and 4 <= y <= 11:
                    raw_pixels[idx : idx + 4] = bytes([235, 255, 255, 255])  # glowing slit
                elif 3 <= x <= 12 and 3 <= y <= 12:
                    raw_pixels[idx : idx + 4] = bytes([70, 215, 255, 255])  # intense ice cyan
                else:
                    raw_pixels[idx : idx + 4] = bytes([25, 65, 110, 255])
                continue

            # Zone 1: Teeth & Fangs (x: 16..31, y: 0..15) -> Pure Crystalline Ice Fangs (white to icy cyan)
            if 16 <= x < 32 and y < 16:
                grad = y / 15.0
                r = clamp(245 - grad * 60 + ((x + y) % 2) * 8)
                g = clamp(252 - grad * 35 + ((x % 3) == 0) * 4)
                b = clamp(255 - grad * 15)
                raw_pixels[idx : idx + 4] = bytes([r, g, b, 245])
                continue

            # Zone 2: Oral Cavity & Throat (x: 0..15, y: 16..31) -> Deep dark indigo abyss
            if x < 16 and 16 <= y < 32:
                depth_factor = (y - 16) / 16.0
                r = clamp(12 + depth_factor * 8 + (x % 3) * 2)
                g = clamp(22 + depth_factor * 12 + ((x + y) % 2) * 3)
                b = clamp(45 + depth_factor * 20 + (y % 4) * 4)
                raw_pixels[idx : idx + 4] = bytes([r, g, b, 255])
                continue

            # Zone 3: Tongue & Palate (x: 16..31, y: 16..31) -> Dark frozen blue with frost ridges
            if 16 <= x < 32 and 16 <= y < 32:
                dist_center = abs(x - 23.5) / 8.0
                r = clamp(24 + dist_center * 15 + ((x * y) % 3) * 3)
                g = clamp(55 + dist_center * 30 + ((x + y) % 2) * 4)
                b = clamp(95 + dist_center * 45 + (y % 3) * 5)
                raw_pixels[idx : idx + 4] = bytes([r, g, b, 255])
                continue

            # Zone 4: Horn Crystal Striations (x: 0..31, y: 32..47) -> Longitudinal facet bands
            if x < 32 and 32 <= y < 48:
                band = (x + (y // 4)) % 4
                if band == 0:
                    raw_pixels[idx : idx + 4] = bytes([185, 235, 255, 255])
                elif band == 1:
                    raw_pixels[idx : idx + 4] = bytes([110, 195, 245, 255])
                elif band == 2:
                    raw_pixels[idx : idx + 4] = bytes([65, 160, 225, 255])
                else:
                    raw_pixels[idx : idx + 4] = bytes([140, 210, 250, 255])
                continue

            # Zone 5: Wings / Crystal Feathers (x: 32..63, y: 0..31) -> Translucent Ice Gradient
            if 32 <= x < 64 and y < 32:
                t = (x - 32 + y) / 64.0
                r = clamp(140 + 70 * t)
                g = clamp(205 + 45 * t)
                b = clamp(245 + 10 * t)
                alpha = clamp(200 + 40 * math.sin(x * 0.4))
                raw_pixels[idx : idx + 4] = bytes([r, g, b, alpha])
                continue

            # Zone 6: Ventral Throat Plates / Gastrosteges (x: 0..31, y: 48..79)
            # Detailed ribbed reptilian belly plating with frosty highlights
            if x < 32 and 48 <= y < 80:
                local_y = (y - 48) % 8
                local_x = abs(x - 15.5) / 16.0
                if local_y == 0:
                    r = clamp(220 - local_x * 40)
                    g = clamp(245 - local_x * 25)
                    b = clamp(255 - local_x * 10)
                elif local_y == 7:
                    r = clamp(18 + local_x * 10)
                    g = clamp(42 + local_x * 15)
                    b = clamp(85 + local_x * 20)
                else:
                    plate_t = local_y / 7.0
                    r = clamp(45 + (1.0 - plate_t) * 90 - local_x * 25)
                    g = clamp(130 + (1.0 - plate_t) * 75 - local_x * 20)
                    b = clamp(205 + (1.0 - plate_t) * 45)
                raw_pixels[idx : idx + 4] = bytes([r, g, b, 255])
                continue

            # Zone 7: Dorsal Glacial Spines & Crests (x: 32..63, y: 48..79)
            # Sharp crystalline facets, white edges, translucent ice core
            if 32 <= x < 64 and 48 <= y < 80:
                lx = (x - 32)
                ly = (y - 48)
                facet = (lx * 2 + ly) % 8
                edge = (lx == 0 or lx == 31 or ly == 0 or ly == 31 or facet == 0)
                if edge:
                    r, g, b = 242, 252, 255
                elif facet in (1, 2):
                    r, g, b = 175, 230, 255
                elif facet in (3, 4):
                    r, g, b = 105, 195, 245
                else:
                    r, g, b = 48, 125, 195
                raw_pixels[idx : idx + 4] = bytes([r, g, b, 250])
                continue

            # Zone 8: Lateral Dragon Scales - Diamond / Hexagonal Matrix (x: 64..95, y: 48..79)
            # Staggered diamond scales with chiseled apex and shaded borders
            if 64 <= x < 96 and 48 <= y < 80:
                lx = x - 64
                ly = y - 48
                row = ly // 4
                offset = (row % 2) * 4
                cx = (lx + offset) % 8 - 4
                cy = ly % 4 - 2
                dist = abs(cx) + abs(cy) * 1.5
                if dist > 3.2:
                    r, g, b = 28, 75, 130
                elif dist < 1.2:
                    r, g, b = 205, 242, 255
                else:
                    scale_grad = (dist - 1.2) / 2.0
                    r = clamp(70 - scale_grad * 35)
                    g = clamp(170 - scale_grad * 60)
                    b = clamp(235 - scale_grad * 50)
                raw_pixels[idx : idx + 4] = bytes([r, g, b, 255])
                continue

            # Zone 9: Articulated Interlocking Collars / Scute Rims (x: 96..127, y: 48..79)
            if 96 <= x < 128 and 48 <= y < 80:
                lx = x - 96
                ly = y - 48
                rim_t = ly / 31.0
                r = clamp(55 + rim_t * 60 + ((lx % 4) == 0) * 15)
                g = clamp(155 + rim_t * 50 + ((lx % 4) == 0) * 15)
                b = clamp(225 + rim_t * 25)
                raw_pixels[idx : idx + 4] = bytes([r, g, b, 255])
                continue

            # Zone 10: Neck Core Lateral Facets (x: 0..63, y: 80..111)
            if x < 64 and 80 <= y < 112:
                lx = x
                ly = y - 80
                band = math.sin(lx * 0.45) * 0.5 + math.cos(ly * 0.4) * 0.5
                r = clamp(60 + band * 25 + (ly // 8) * 4)
                g = clamp(165 + band * 25 + (ly // 8) * 3)
                b = clamp(230 + band * 15)
                raw_pixels[idx : idx + 4] = bytes([r, g, b, 248])
                continue

            # Base Body / Scales / Cranium: Smooth, broad glacial facets without high-frequency zebra stripes
            nx = x / float(width)
            ny = y / float(height)
            facet = math.sin(nx * 3.5) * 0.4 + math.cos(ny * 4.0) * 0.3 + math.sin((nx - ny) * 2.5) * 0.3
            base_r = clamp(68 + 22 * facet + 15 * ny)
            base_g = clamp(175 + 20 * facet + 12 * ny)
            base_b = clamp(238 + 12 * facet)
            alpha = 245
            raw_pixels[idx : idx + 4] = bytes([base_r, base_g, base_b, alpha])

    def chunk(tag: bytes, data: bytes) -> bytes:
        return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    scanlines = bytearray()
    for row in range(height):
        scanlines.append(0)
        scanlines.extend(raw_pixels[row * width * 4 : (row + 1) * width * 4])

    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))
    png += chunk(b"IDAT", zlib.compress(bytes(scanlines)))
    png += chunk(b"IEND", b"")
    return png


from hydra_organic import build_geo_json, build_anim_json


def build_bbmodel(geo: dict, anim: dict, texture_png: bytes) -> dict:
    """Build .bbmodel Blockbench project file for interactive editing and authoring."""
    b64_tex = "data:image/png;base64," + base64.b64encode(texture_png).decode("ascii")
    tex_uuid = identity("texture_main")
    
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
                "origin": cube.get("pivot", bone["pivot"]),
                "rotation": cube.get("rotation", [0, 0, 0]),
                "color": 0,
                "faces": faces
            })
            b_children.append(c_uuid)
        
        groups[b_name] = {
            "name": b_name,
            "origin": bone["pivot"],
            "rotation": bone.get("rotation", [0, 0, 0]),
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

    return {
        "meta": {
            "format_version": "4.8",
            "model_format": "geckolib_model",
            "box_uv": False
        },
        "name": "ice_hydra",
        "geometry_name": "geometry.bleachmod.ice_hydra",
        "resolution": {"width": 128, "height": 128},
        "elements": out_elements,
        "outliner": out_outliner,
        "animations": animations,
        "textures": [
            {
                "path": str(TEXTURE_PNG),
                "name": "ice_hydra.png",
                "folder": "entity",
                "namespace": "bleachmod",
                "id": "0",
                "uuid": tex_uuid,
                "source": b64_tex
            }
        ]
    }


def main():
    parser = argparse.ArgumentParser(description="Generate Three-Headed Ice Hydra assets")
    parser.add_argument("--check", action="store_true", help="Verify outputs match")
    args = parser.parse_args()

    if args.check:
        tex = generate_hydra_texture_bytes()
        geo = build_geo_json()
        anim = build_anim_json()
        outputs = {
            TEXTURE_PNG: tex,
            GEO_JSON: json.dumps(geo, indent=2).encode("utf-8"),
            ANIM_JSON: json.dumps(anim, indent=2).encode("utf-8"),
            SOURCE_BBMODEL: json.dumps(build_bbmodel(geo, anim, tex), indent=2).encode("utf-8")
        }
        stale = [
            str(p.relative_to(ROOT))
            for p, data in outputs.items()
            if not p.exists() or (p.read_bytes() if p.suffix == ".png" else p.read_text(encoding="utf-8").encode("utf-8")) != data
        ]
        if stale:
            raise SystemExit("Stale generated assets: " + ", ".join(stale))
        print("Hydra source, hierarchy, animations, texture and runtime assets match.")
        return

    print("[1/4] Generating Ice Hydra texture PNG...")
    tex_bytes = generate_hydra_texture_bytes()
    TEXTURE_PNG.parent.mkdir(parents=True, exist_ok=True)
    TEXTURE_PNG.write_bytes(tex_bytes)

    print("[2/4] Generating GeckoLib geometry JSON...")
    geo_data = build_geo_json()
    GEO_JSON.parent.mkdir(parents=True, exist_ok=True)
    GEO_JSON.write_text(json.dumps(geo_data, indent=2), encoding="utf-8")

    print("[3/4] Generating GeckoLib animation JSON...")
    anim_data = build_anim_json()
    ANIM_JSON.parent.mkdir(parents=True, exist_ok=True)
    ANIM_JSON.write_text(json.dumps(anim_data, indent=2), encoding="utf-8")

    print("[4/4] Generating Blockbench .bbmodel source project...")
    bbmodel_data = build_bbmodel(geo_data, anim_data, tex_bytes)
    SOURCE_BBMODEL.parent.mkdir(parents=True, exist_ok=True)
    SOURCE_BBMODEL.write_text(json.dumps(bbmodel_data, indent=2), encoding="utf-8")

    print(f"Done! Created:\n  - {TEXTURE_PNG}\n  - {GEO_JSON}\n  - {ANIM_JSON}\n  - {SOURCE_BBMODEL}")


if __name__ == "__main__":
    main()
