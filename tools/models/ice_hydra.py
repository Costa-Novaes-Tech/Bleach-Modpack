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


def build_geo_json() -> dict:
    """Build GeckoLib 4.0 compatible geometry for the Three-Headed Ice Hydra."""
    bones = [
        {"name": "root", "pivot": [0, 0, 0], "cubes": []},
        {
            "name": "base_spine",
            "parent": "root",
            "pivot": [0, 10, 8],
            "cubes": [
                {
                    "origin": [-7, 4, 2],
                    "size": [14, 12, 16],
                    "uv": {"north": {"uv": [0, 32, 14, 44]}, "south": {"uv": [14, 32, 28, 44]},
                           "east": {"uv": [28, 32, 44, 44]}, "west": {"uv": [44, 32, 60, 44]},
                           "up": {"uv": [0, 44, 14, 60]}, "down": {"uv": [14, 44, 28, 60]}}
                }
            ]
        },
        # Central Neck and Head
        {
            "name": "neck_center_1",
            "parent": "base_spine",
            "pivot": [0, 14, 4],
            "rotation": [-10, 0, 0],
            "cubes": [
                {"origin": [-4, 14, 0], "size": [8, 10, 8],
                 "uv": {"north": {"uv": [0, 64, 8, 74]}, "south": {"uv": [8, 64, 16, 74]},
                        "east": {"uv": [16, 64, 24, 74]}, "west": {"uv": [24, 64, 32, 74]},
                        "up": {"uv": [0, 74, 8, 82]}, "down": {"uv": [8, 74, 16, 82]}}}
            ]
        },
        {
            "name": "neck_center_2",
            "parent": "neck_center_1",
            "pivot": [0, 24, 0],
            "rotation": [-15, 0, 0],
            "cubes": [
                {"origin": [-3.5, 24, -4], "size": [7, 10, 7],
                 "uv": {"north": {"uv": [32, 64, 39, 74]}, "south": {"uv": [39, 64, 46, 74]},
                        "east": {"uv": [46, 64, 53, 74]}, "west": {"uv": [53, 64, 60, 74]},
                        "up": {"uv": [32, 74, 39, 81]}, "down": {"uv": [39, 74, 46, 81]}}}
            ]
        },
        {
            "name": "head_center",
            "parent": "neck_center_2",
            "pivot": [0, 34, -4],
            "cubes": [
                # Skull
                {"origin": [-6, 32, -16], "size": [12, 8, 14],
                 "uv": {"north": {"uv": [64, 0, 76, 8]}, "south": {"uv": [76, 0, 88, 8]},
                        "east": {"uv": [88, 0, 102, 8]}, "west": {"uv": [102, 0, 116, 8]},
                        "up": {"uv": [64, 8, 76, 22]}, "down": {"uv": [76, 8, 88, 22]}}},
                # Eyes (Emissive Red UV 0..8, 0..8)
                {"origin": [-6.2, 35, -12], "size": [0.4, 2.5, 4],
                 "uv": {"west": {"uv": [0, 0, 4, 2.5]}}},
                {"origin": [5.8, 35, -12], "size": [0.4, 2.5, 4],
                 "uv": {"east": {"uv": [0, 0, 4, 2.5]}}},
                # Upper Horns
                {"origin": [-4, 40, -10], "size": [2.5, 6, 8],
                 "uv": {"north": {"uv": [16, 0, 18.5, 6]}, "south": {"uv": [18.5, 0, 21, 6]},
                        "east": {"uv": [21, 0, 29, 6]}, "west": {"uv": [21, 0, 29, 6]},
                        "up": {"uv": [16, 6, 18.5, 14]}, "down": {"uv": [18.5, 6, 21, 14]}}},
                {"origin": [1.5, 40, -10], "size": [2.5, 6, 8],
                 "uv": {"north": {"uv": [16, 0, 18.5, 6]}, "south": {"uv": [18.5, 0, 21, 6]},
                        "east": {"uv": [21, 0, 29, 6]}, "west": {"uv": [21, 0, 29, 6]},
                        "up": {"uv": [16, 6, 18.5, 14]}, "down": {"uv": [18.5, 6, 21, 14]}}}
            ]
        },
        {
            "name": "jaw_center",
            "parent": "head_center",
            "pivot": [0, 32, -6],
            "cubes": [
                {"origin": [-5, 28, -15], "size": [10, 4, 11],
                 "uv": {"north": {"uv": [64, 24, 74, 28]}, "south": {"uv": [74, 24, 84, 28]},
                        "east": {"uv": [84, 24, 95, 28]}, "west": {"uv": [95, 24, 106, 28]},
                        "up": {"uv": [64, 28, 74, 39]}, "down": {"uv": [74, 28, 84, 39]}}}
            ]
        },
        # Left Neck and Head
        {
            "name": "neck_left_1",
            "parent": "base_spine",
            "pivot": [6, 12, 6],
            "rotation": [-5, 25, 12],
            "cubes": [
                {"origin": [3, 12, 3], "size": [6, 9, 6],
                 "uv": {"north": {"uv": [0, 84, 6, 93]}, "south": {"uv": [6, 84, 12, 93]},
                        "east": {"uv": [12, 84, 18, 93]}, "west": {"uv": [18, 84, 24, 93]},
                        "up": {"uv": [0, 93, 6, 99]}, "down": {"uv": [6, 93, 12, 99]}}}
            ]
        },
        {
            "name": "neck_left_2",
            "parent": "neck_left_1",
            "pivot": [10, 20, 4],
            "rotation": [-8, 15, 8],
            "cubes": [
                {"origin": [8, 20, 1], "size": [5.5, 9, 5.5],
                 "uv": {"north": {"uv": [24, 84, 29.5, 93]}, "south": {"uv": [29.5, 84, 35, 93]},
                        "east": {"uv": [35, 84, 40.5, 93]}, "west": {"uv": [40.5, 84, 46, 93]},
                        "up": {"uv": [24, 93, 29.5, 98.5]}, "down": {"uv": [29.5, 93, 35, 98.5]}}}
            ]
        },
        {
            "name": "head_left",
            "parent": "neck_left_2",
            "pivot": [14, 28, 0],
            "rotation": [0, -10, 0],
            "cubes": [
                {"origin": [9, 26, -11], "size": [10, 7, 12],
                 "uv": {"north": {"uv": [64, 40, 74, 47]}, "south": {"uv": [74, 40, 84, 47]},
                        "east": {"uv": [84, 40, 96, 47]}, "west": {"uv": [96, 40, 108, 47]},
                        "up": {"uv": [64, 47, 74, 59]}, "down": {"uv": [74, 47, 84, 59]}}},
                # Left Head Eyes
                {"origin": [8.8, 29, -8], "size": [0.4, 2, 3], "uv": {"west": {"uv": [0, 0, 3, 2]}}},
                {"origin": [18.8, 29, -8], "size": [0.4, 2, 3], "uv": {"east": {"uv": [0, 0, 3, 2]}}}
            ]
        },
        {
            "name": "jaw_left",
            "parent": "head_left",
            "pivot": [14, 26, -2],
            "cubes": [
                {"origin": [10, 22, -10], "size": [8, 4, 9],
                 "uv": {"north": {"uv": [64, 60, 72, 64]}, "south": {"uv": [72, 60, 80, 64]},
                        "east": {"uv": [80, 60, 89, 64]}, "west": {"uv": [89, 60, 98, 64]},
                        "up": {"uv": [64, 64, 72, 73]}, "down": {"uv": [72, 64, 80, 73]}}}
            ]
        },
        # Right Neck and Head
        {
            "name": "neck_right_1",
            "parent": "base_spine",
            "pivot": [-6, 12, 6],
            "rotation": [-5, -25, -12],
            "cubes": [
                {"origin": [-9, 12, 3], "size": [6, 9, 6],
                 "uv": {"north": {"uv": [0, 84, 6, 93]}, "south": {"uv": [6, 84, 12, 93]},
                        "east": {"uv": [12, 84, 18, 93]}, "west": {"uv": [18, 84, 24, 93]},
                        "up": {"uv": [0, 93, 6, 99]}, "down": {"uv": [6, 93, 12, 99]}}}
            ]
        },
        {
            "name": "neck_right_2",
            "parent": "neck_right_1",
            "pivot": [-10, 20, 4],
            "rotation": [-8, -15, -8],
            "cubes": [
                {"origin": [-13.5, 20, 1], "size": [5.5, 9, 5.5],
                 "uv": {"north": {"uv": [24, 84, 29.5, 93]}, "south": {"uv": [29.5, 84, 35, 93]},
                        "east": {"uv": [35, 84, 40.5, 93]}, "west": {"uv": [40.5, 84, 46, 93]},
                        "up": {"uv": [24, 93, 29.5, 98.5]}, "down": {"uv": [29.5, 93, 35, 98.5]}}}
            ]
        },
        {
            "name": "head_right",
            "parent": "neck_right_2",
            "pivot": [-14, 28, 0],
            "rotation": [0, 10, 0],
            "cubes": [
                {"origin": [-19, 26, -11], "size": [10, 7, 12],
                 "uv": {"north": {"uv": [64, 40, 74, 47]}, "south": {"uv": [74, 40, 84, 47]},
                        "east": {"uv": [84, 40, 96, 47]}, "west": {"uv": [96, 40, 108, 47]},
                        "up": {"uv": [64, 47, 74, 59]}, "down": {"uv": [74, 47, 84, 59]}}},
                # Right Head Eyes
                {"origin": [-19.2, 29, -8], "size": [0.4, 2, 3], "uv": {"west": {"uv": [0, 0, 3, 2]}}},
                {"origin": [-9.2, 29, -8], "size": [0.4, 2, 3], "uv": {"east": {"uv": [0, 0, 3, 2]}}}
            ]
        },
        {
            "name": "jaw_right",
            "parent": "head_right",
            "pivot": [-14, 26, -2],
            "cubes": [
                {"origin": [-18, 22, -10], "size": [8, 4, 9],
                 "uv": {"north": {"uv": [64, 60, 72, 64]}, "south": {"uv": [72, 60, 80, 64]},
                        "east": {"uv": [80, 60, 89, 64]}, "west": {"uv": [89, 60, 98, 64]},
                        "up": {"uv": [64, 64, 72, 73]}, "down": {"uv": [72, 64, 80, 73]}}}
            ]
        },
        # Colossal Glacial Wings
        {
            "name": "wing_left",
            "parent": "base_spine",
            "pivot": [7, 14, 10],
            "rotation": [12, 28, 15],
            "cubes": [
                {"origin": [7, 10, 8], "size": [28, 22, 2],
                 "uv": {"north": {"uv": [32, 0, 60, 22]}, "south": {"uv": [32, 0, 60, 22]},
                        "east": {"uv": [32, 0, 34, 22]}, "west": {"uv": [32, 0, 34, 22]},
                        "up": {"uv": [32, 0, 60, 2]}, "down": {"uv": [32, 0, 60, 2]}}}
            ]
        },
        {
            "name": "wing_right",
            "parent": "base_spine",
            "pivot": [-7, 14, 10],
            "rotation": [12, -28, -15],
            "cubes": [
                {"origin": [-35, 10, 8], "size": [28, 22, 2],
                 "uv": {"north": {"uv": [32, 0, 60, 22]}, "south": {"uv": [32, 0, 60, 22]},
                        "east": {"uv": [32, 0, 34, 22]}, "west": {"uv": [32, 0, 34, 22]},
                        "up": {"uv": [32, 0, 60, 2]}, "down": {"uv": [32, 0, 60, 2]}}}
            ]
        }
    ]

    return {
        "format_version": "1.12.0",
        "minecraft:geometry": [
            {
                "description": {
                    "identifier": "geometry.bleachmod.ice_hydra",
                    "texture_width": 128,
                    "texture_height": 128,
                    "visible_bounds_width": 48,
                    "visible_bounds_height": 28,
                    "visible_bounds_offset": [0, 8, 0]
                },
                "bones": bones
            }
        ]
    }


def build_anim_json() -> dict:
    """Build GeckoLib animations for the Ice Hydra."""
    flight_anim = {
        "loop": True,
        "animation_length": 3.0,
        "bones": {
            "neck_center_1": {
                "rotation": {
                    "0.0": [-10, 0, 0],
                    "1.5": [-16, 0, 0],
                    "3.0": [-10, 0, 0]
                }
            },
            "neck_left_1": {
                "rotation": {
                    "0.0": [-5, 25, 12],
                    "1.5": [-2, 32, 16],
                    "3.0": [-5, 25, 12]
                }
            },
            "neck_right_1": {
                "rotation": {
                    "0.0": [-5, -25, -12],
                    "1.5": [-2, -32, -16],
                    "3.0": [-5, -25, -12]
                }
            },
            "wing_left": {
                "rotation": {
                    "0.0": [12, 28, 15],
                    "0.75": [8, 42, 28],
                    "1.5": [15, 18, 5],
                    "2.25": [8, 42, 28],
                    "3.0": [12, 28, 15]
                }
            },
            "wing_right": {
                "rotation": {
                    "0.0": [12, -28, -15],
                    "0.75": [8, -42, -28],
                    "1.5": [15, -18, -5],
                    "2.25": [8, -42, -28],
                    "3.0": [12, -28, -15]
                }
            },
            "jaw_center": {
                "rotation": {
                    "0.0": [5, 0, 0],
                    "1.5": [18, 0, 0],
                    "3.0": [5, 0, 0]
                }
            }
        }
    }
    return {
        "format_version": "1.8.0",
        "animations": {
            "animation.ice_hydra.flight": flight_anim,
            "animation.ice_dragon.flight": flight_anim,
            "animation.ice_hydra.roar": {
                "loop": False,
                "animation_length": 2.5,
                "bones": {
                    "neck_center_1": {
                        "rotation": {
                            "0.0": [-10, 0, 0],
                            "0.5": [-25, 0, 0],
                            "1.0": [5, 0, 0],
                            "2.0": [0, 0, 0],
                            "2.5": [-10, 0, 0]
                        }
                    },
                    "jaw_center": {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "0.5": [10, 0, 0],
                            "1.0": [42, 0, 0],
                            "2.0": [35, 0, 0],
                            "2.5": [0, 0, 0]
                        }
                    },
                    "jaw_left": {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "0.6": [38, 0, 0],
                            "2.0": [30, 0, 0],
                            "2.5": [0, 0, 0]
                        }
                    },
                    "jaw_right": {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "0.6": [38, 0, 0],
                            "2.0": [30, 0, 0],
                            "2.5": [0, 0, 0]
                        }
                    },
                    "wing_left": {
                        "rotation": {
                            "0.0": [12, 28, 15],
                            "0.8": [-5, 55, 35],
                            "2.0": [-5, 55, 35],
                            "2.5": [12, 28, 15]
                        }
                    },
                    "wing_right": {
                        "rotation": {
                            "0.0": [12, -28, -15],
                            "0.8": [-5, -55, -35],
                            "2.0": [-5, -55, -35],
                            "2.5": [12, -28, -15]
                        }
                    }
                }
            }
        }
    }


def build_bbmodel(geo: dict, anim: dict, texture_png: bytes) -> dict:
    """Build .bbmodel Blockbench project file for interactive editing and authoring."""
    b64_tex = "data:image/png;base64," + base64.b64encode(texture_png).decode("ascii")
    tex_uuid = identity("texture_main")
    
    out_elements = []
    out_outliner = []

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
                faces[f] = {"uv": cube.get("uv", {}).get(f, {}).get("uv", [0, 0, 16, 16]), "texture": 0}
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
        
        out_outliner.append({
            "name": b_name,
            "origin": bone["pivot"],
            "rotation": bone.get("rotation", [0, 0, 0]),
            "uuid": b_uuid,
            "export": True,
            "isOpen": True,
            "visibility": True,
            "children": b_children
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
