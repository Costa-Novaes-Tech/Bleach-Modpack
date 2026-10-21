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


def generate_hydra_texture_bytes() -> bytes:
    """Generate a 128x128 RGBA texture with crystalline ice gradients and red emissive eyes."""
    width, height = 128, 128
    raw_pixels = bytearray(width * height * 4)

    for y in range(height):
        for x in range(width):
            idx = (y * width + x) * 4

            # Zone 0: Eyes palette (x: 0..15, y: 0..15) -> Vivid Crimson Red with bright core
            if x < 16 and y < 16:
                dist = math.hypot(x - 8, y - 8)
                if dist < 4:
                    raw_pixels[idx : idx + 4] = bytes([255, 60, 80, 255])
                elif dist < 7:
                    raw_pixels[idx : idx + 4] = bytes([220, 20, 40, 240])
                else:
                    raw_pixels[idx : idx + 4] = bytes([160, 10, 25, 220])
                continue

            # Zone 1: Horns & Teeth (x: 16..31, y: 0..15) -> Pure Bright Glacier White
            if 16 <= x < 32 and y < 16:
                frost = int(230 + 25 * math.sin((x + y) * 0.5))
                raw_pixels[idx : idx + 4] = bytes([frost, 245, 255, 230])
                continue

            # Zone 2: Wings / Crystal Feathers (x: 32..63, y: 0..31) -> Translucent Ice Gradient
            if 32 <= x < 64 and y < 32:
                t = (x - 32 + y) / 64.0
                r = int(120 + 80 * t)
                g = int(190 + 55 * t)
                b = int(240 + 15 * t)
                alpha = int(180 + 60 * math.sin(x * 0.8))
                raw_pixels[idx : idx + 4] = bytes([r, g, b, alpha])
                continue

            # Base Body / Scales / Heads -> Layered Glacial Blue with Crystalline Facets
            nx = x / float(width)
            ny = y / float(height)
            pattern = math.sin(nx * 18.0) * math.cos(ny * 18.0) + math.sin((nx + ny) * 24.0) * 0.5
            base_r = int(max(40, min(140, 60 + 40 * pattern + 30 * ny)))
            base_g = int(max(140, min(230, 170 + 35 * pattern + 20 * ny)))
            base_b = int(max(200, min(255, 235 + 20 * pattern)))
            alpha = int(max(200, min(245, 225 + 20 * pattern)))
            raw_pixels[idx : idx + 4] = bytes([base_r, base_g, base_b, alpha])

    # Convert to PNG format with IHDR, IDAT, IEND chunks
    def chunk(tag: bytes, data: bytes) -> bytes:
        return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    scanlines = bytearray()
    for row in range(height):
        scanlines.append(0)  # filter type 0
        scanlines.extend(raw_pixels[row * width * 4 : (row + 1) * width * 4])

    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))  # RGBA
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
            to = [frm[0] + size[0], frm[1] + size[1], frm[2] + size[2]]
            faces = {}
            for f in ["north", "south", "east", "west", "up", "down"]:
                face=cube.get('uv', {}).get(f)
                if face:
                    u,v=face['uv'];w,h=face['uv_size'];faces[f]={'uv':[u,v,u+w,v+h],'texture':0}
                else:faces[f]={'uv':[0,0,0,0],'texture':None}
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
        
        groups[b_name]={
            "name": b_name,
            "origin": bone["pivot"],
            "rotation": bone.get("rotation", [0, 0, 0]),
            "uuid": b_uuid,
            "export": True,
            "isOpen": True,
            "visibility": True,
            "children": b_children
        }

    out_outliner=[]
    for bone in geo['minecraft:geometry'][0]['bones']:
        group=groups[bone['name']]
        if bone.get('parent'):groups[bone['parent']]['children'].append(group)
        else:out_outliner.append(group)
    animations=[]
    for name,clip in anim['animations'].items():
        animators={}
        for bone,channels in clip['bones'].items():
            keys=[]
            for channel,frames in channels.items():
                for time,vec in frames.items():
                    keys.append({'channel':channel,'time':float(time),'interpolation':'linear','uuid':identity(name+bone+channel+time),'data_points':[dict(zip(('x','y','z'),map(str,vec)))]})
            animators[identity('bone_'+bone)]={'name':bone,'type':'bone','keyframes':keys}
        animations.append({'uuid':identity(name),'name':name,'loop':'loop' if clip['loop'] else 'once','length':clip['animation_length'],'snapping':20,'animators':animators})

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
        tex=generate_hydra_texture_bytes();geo=build_geo_json();anim=build_anim_json()
        outputs={TEXTURE_PNG:tex,GEO_JSON:json.dumps(geo,indent=2).encode('utf-8'),ANIM_JSON:json.dumps(anim,indent=2).encode('utf-8'),SOURCE_BBMODEL:json.dumps(build_bbmodel(geo,anim,tex),indent=2).encode('utf-8')}
        stale=[str(p.relative_to(ROOT)) for p,data in outputs.items() if not p.exists() or (p.read_bytes() if p.suffix=='.png' else p.read_text(encoding='utf-8').encode('utf-8'))!=data]
        if stale:raise SystemExit('Stale generated assets: '+', '.join(stale))
        print('Hydra source, hierarchy, animations, texture and runtime assets match.')
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
