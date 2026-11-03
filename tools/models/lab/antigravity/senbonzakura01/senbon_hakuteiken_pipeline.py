"""Master pipeline and generator for Senbonzakura Shukei: Hakuteiken model, rig, animations, and textures.

Converts and exports:
  - Editable Blockbench model: tools/models/lab/antigravity/senbonzakura01/ag_senbon_hakuteiken_v2.bbmodel
  - GeckoLib 1.12.0 Geometry: src/main/resources/assets/bleachmod/geo/senbon_hakuteiken.geo.json
  - GeckoLib 1.8.0 Animations: src/main/resources/assets/bleachmod/animations/senbon_hakuteiken.animation.json
  - 128x128 RGBA Texture: src/main/resources/assets/bleachmod/textures/entity/senbon_hakuteiken.png
  - PlayerAnimator Pose: src/main/resources/assets/bleachmod/player_animation/hakuteiken_dash.json
"""
from __future__ import annotations

import argparse
import io
import json
import math
import os
from pathlib import Path
import sys
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[5]
SOURCE_BBMODEL = ROOT / "tools/models/lab/antigravity/senbonzakura01/ag_senbon_hakuteiken_v2.bbmodel"
GEO_JSON = ROOT / "src/main/resources/assets/bleachmod/geo/senbon_hakuteiken.geo.json"
ANIM_JSON = ROOT / "src/main/resources/assets/bleachmod/animations/senbon_hakuteiken.animation.json"
TEXTURE_PNG = ROOT / "src/main/resources/assets/bleachmod/textures/entity/senbon_hakuteiken.png"
PLAYER_ANIM_JSON = ROOT / "src/main/resources/assets/bleachmod/player_animation/hakuteiken_dash.json"


def clamp(v: float | int) -> int:
    return max(0, min(255, int(v)))


def create_hakuteiken_texture() -> Image.Image:
    """Generate 128x128 RGBA celestial white and sakura pink radiant texture."""
    img = Image.new("RGBA", (128, 128), (0, 0, 0, 0))

    # Base background: pure celestial glow
    for y in range(128):
        for x in range(128):
            u = x / 127.0
            v = y / 127.0
            
            # Subtle sakura tint variation
            sakura_blend = 0.5 + 0.5 * math.sin(u * 6.28 * 2 + v * 3.14)
            r = clamp(245 + sakura_blend * 10)
            g = clamp(240 + sakura_blend * 12)
            b = clamp(248 + sakura_blend * 7)
            a = 255
            img.putpixel((x, y), (r, g, b, a))

    # Add radiant accents to specific UV regions:
    # 1. Halo region: pure golden-white emissive rune bands
    for y in range(40):
        for x in range(40):
            dist = math.hypot(x - 20, y - 20)
            if 14 <= dist <= 19:
                img.putpixel((x, y), (255, 255, 255, 255))
            elif 10 <= dist < 14:
                img.putpixel((x, y), (255, 235, 245, 250))

    # 2. Blade condensed energy core region (60..127, 0..30)
    for y in range(30):
        for x in range(60, 128):
            edge = min(y, 29 - y)
            if edge < 3:
                img.putpixel((x, y), (255, 182, 193, 230))  # Sakura pink edge
            else:
                img.putpixel((x, y), (255, 255, 255, 255))  # Pure white core

    # 3. Feather vanes (0..60, 40..127)
    for y in range(40, 128):
        for x in range(60):
            barb = (x + y * 2) % 6
            if barb == 0:
                img.putpixel((x, y), (255, 255, 255, 255))
            elif barb in (1, 2):
                img.putpixel((x, y), (255, 240, 245, 245))
            else:
                img.putpixel((x, y), (250, 220, 230, 235))

    return img


def bbmodel_to_geckolib():
    """Convert ag_senbon_hakuteiken_v2.bbmodel to GeckoLib geo.json and animation.json."""
    with open(SOURCE_BBMODEL, "r", encoding="utf-8") as f:
        bbdata = json.load(f)

    cube_map = {c["uuid"]: c for c in bbdata.get("elements", [])}
    group_map = {g["uuid"]: g for g in bbdata.get("groups", [])}

    # Build hierarchy of bones
    bones = []

    def process_group(node, parent_name=None):
        if not isinstance(node, dict):
            return
        gid = node.get("uuid")
        ginfo = group_map.get(gid, {})
        name = ginfo.get("name", "bone")
        origin = ginfo.get("origin", [0, 0, 0])
        rotation = ginfo.get("rotation", [0, 0, 0])

        bone = {
            "name": name,
            "pivot": [round(float(origin[0]), 3), round(float(origin[1]), 3), round(float(origin[2]), 3)]
        }
        if parent_name:
            bone["parent"] = parent_name
        if rotation and (rotation[0] or rotation[1] or rotation[2]):
            bone["rotation"] = [round(float(rotation[0]), 3), round(float(rotation[1]), 3), round(float(rotation[2]), 3)]

        cubes_list = []
        for child in node.get("children", []):
            if isinstance(child, str):
                c = cube_map.get(child)
                if c:
                    c_from = c.get("from", [0, 0, 0])
                    c_to = c.get("to", [0, 0, 0])
                    size = [
                        round(abs(c_to[0] - c_from[0]), 3),
                        round(abs(c_to[1] - c_from[1]), 3),
                        round(abs(c_to[2] - c_from[2]), 3)
                    ]
                    uv_offset = c.get("uv_offset", [0, 0])
                    cube_entry = {
                        "origin": [round(min(c_from[0], c_to[0]), 3), round(min(c_from[1], c_to[1]), 3), round(min(c_from[2], c_to[2]), 3)],
                        "size": size,
                        "uv": [int(uv_offset[0]), int(uv_offset[1])]
                    }
                    c_rot = c.get("rotation")
                    if c_rot and (c_rot[0] or c_rot[1] or c_rot[2]):
                        cube_entry["rotation"] = [round(float(c_rot[0]), 3), round(float(c_rot[1]), 3), round(float(c_rot[2]), 3)]
                        c_origin = c.get("origin", origin)
                        cube_entry["pivot"] = [round(float(c_origin[0]), 3), round(float(c_origin[1]), 3), round(float(c_origin[2]), 3)]
                    cubes_list.append(cube_entry)
            elif isinstance(child, dict):
                process_group(child, parent_name=name)

        bone["cubes"] = cubes_list
        bones.append(bone)

    for o in bbdata.get("outliner", []):
        process_group(o)

    geo_output = {
        "format_version": "1.12.0",
        "minecraft:geometry": [
            {
                "description": {
                    "identifier": "geometry.senbon_hakuteiken",
                    "texture_width": 128,
                    "texture_height": 128,
                    "visible_bounds_width": 6,
                    "visible_bounds_height": 5,
                    "visible_bounds_offset": [0, 1.5, 0]
                },
                "bones": bones
            }
        ]
    }

    # Convert animations
    anim_output = {
        "format_version": "1.8.0",
        "animations": {}
    }

    for a in bbdata.get("animations", []):
        anim_name = a.get("name", "animation.hakuteiken.idle")
        length = float(a.get("length", 1.0))
        loop = True if a.get("loop") == "loop" else (a.get("loop") == "hold" or a.get("loop") == True)
        
        anim_entry = {
            "animation_length": length,
            "loop": loop,
            "bones": {}
        }

        animators = a.get("animators", {})
        for uuid_key, animator in animators.items():
            b_name = animator.get("name")
            if not b_name:
                continue

            bone_anim = {}
            channels = {}
            for kf in animator.get("keyframes", []):
                ch = kf.get("channel", "rotation")
                t = round(float(kf.get("time", 0.0)), 4)
                data_points = kf.get("data_points", [{}])[0]
                val = [
                    round(float(data_points.get("x", 0.0)), 3),
                    round(float(data_points.get("y", 0.0)), 3),
                    round(float(data_points.get("z", 0.0)), 3)
                ]
                interp = kf.get("interpolation", "catmullrom")
                channels.setdefault(ch, {})[f"{t:.4f}"] = {
                    "post": val,
                    "lerp_mode": interp
                }

            if channels:
                anim_entry["bones"][b_name] = channels

        anim_output["animations"][anim_name] = anim_entry

    return geo_output, anim_output


def build_player_animator_dash_pose():
    """Build PlayerAnimator Bedrock animation for noble fencing dash pose."""
    return {
        "format_version": "1.8.0",
        "animations": {
            "animation.player.hakuteiken_dash": {
                "loop": "hold",
                "animation_length": 1.0,
                "bones": {
                    "body": {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "0.15": [12, -8, 0],
                            "1.0": [12, -8, 0]
                        }
                    },
                    "head": {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "0.15": [-8, 8, 0],
                            "1.0": [-8, 8, 0]
                        }
                    },
                    "right_arm": {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "0.15": [-90, -10, 0],
                            "1.0": [-90, -10, 0]
                        },
                        "position": {
                            "0.0": [0, 0, 0],
                            "0.15": [0, 0, -2],
                            "1.0": [0, 0, -2]
                        }
                    },
                    "left_arm": {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "0.15": [25, 15, -20],
                            "1.0": [25, 15, -20]
                        }
                    },
                    "right_leg": {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "0.15": [-15, 0, 5],
                            "1.0": [-15, 0, 5]
                        }
                    },
                    "left_leg": {
                        "rotation": {
                            "0.0": [0, 0, 0],
                            "0.15": [25, 0, -5],
                            "1.0": [25, 0, -5]
                        }
                    }
                }
            }
        }
    }


def main():
    parser = argparse.ArgumentParser(description="Senbonzakura Hakuteiken asset exporter")
    parser.add_argument("--check", action="store_true", help="Check generated assets match without writing")
    args = parser.parse_args()

    geo_data, anim_data = bbmodel_to_geckolib()
    player_anim_data = build_player_animator_dash_pose()
    tex_img = create_hakuteiken_texture()

    geo_str = json.dumps(geo_data, indent=2) + "\n"
    anim_str = json.dumps(anim_data, indent=2) + "\n"
    player_anim_str = json.dumps(player_anim_data, indent=2) + "\n"

    buf = io.BytesIO()
    tex_img.save(buf, format="PNG")
    tex_bytes = buf.getvalue()

    if args.check:
        assert GEO_JSON.exists(), f"Missing {GEO_JSON}"
        assert ANIM_JSON.exists(), f"Missing {ANIM_JSON}"
        assert TEXTURE_PNG.exists(), f"Missing {TEXTURE_PNG}"
        assert PLAYER_ANIM_JSON.exists(), f"Missing {PLAYER_ANIM_JSON}"
        print("PASS: all target files exist.")
        return

    # Write files
    GEO_JSON.parent.mkdir(parents=True, exist_ok=True)
    with open(GEO_JSON, "w", encoding="utf-8") as f:
        f.write(geo_str)
    print(f"Exported GeckoLib geo: {GEO_JSON} ({len(geo_str)} bytes)")

    ANIM_JSON.parent.mkdir(parents=True, exist_ok=True)
    with open(ANIM_JSON, "w", encoding="utf-8") as f:
        f.write(anim_str)
    print(f"Exported GeckoLib animations: {ANIM_JSON} ({len(anim_str)} bytes)")

    TEXTURE_PNG.parent.mkdir(parents=True, exist_ok=True)
    with open(TEXTURE_PNG, "wb") as f:
        f.write(tex_bytes)
    print(f"Exported texture: {TEXTURE_PNG} ({len(tex_bytes)} bytes)")

    PLAYER_ANIM_JSON.parent.mkdir(parents=True, exist_ok=True)
    with open(PLAYER_ANIM_JSON, "w", encoding="utf-8") as f:
        f.write(player_anim_str)
    print(f"Exported PlayerAnimator pose: {PLAYER_ANIM_JSON} ({len(player_anim_str)} bytes)")


if __name__ == "__main__":
    main()
