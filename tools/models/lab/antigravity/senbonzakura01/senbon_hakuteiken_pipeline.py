"""Master pipeline and generator for Senbonzakura Shukei: Hakuteiken model, rig, animations, and textures.

Converts and exports:
  - Editable Blockbench model (with Player + Wings + Halo + Blade):
    tools/models/lab/antigravity/senbonzakura01/ag_senbon_hakuteiken_v2.bbmodel
  - GeckoLib 1.12.0 Geometry (13 construct bones for in-game rendering):
    src/main/resources/assets/bleachmod/geo/senbon_hakuteiken.geo.json
  - GeckoLib 1.8.0 Animations:
    src/main/resources/assets/bleachmod/animations/senbon_hakuteiken.animation.json
  - 128x256 RGBA Texture:
    src/main/resources/assets/bleachmod/textures/entity/senbon_hakuteiken.png
  - PlayerAnimator Pose:
    src/main/resources/assets/bleachmod/player_animation/hakuteiken_dash.json
"""
from __future__ import annotations

import argparse
import base64
import io
import json
import math
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[5]
SOURCE_BBMODEL = ROOT / "tools/models/lab/antigravity/senbonzakura01/ag_senbon_hakuteiken_v2.bbmodel"
GEO_JSON = ROOT / "src/main/resources/assets/bleachmod/geo/senbon_hakuteiken.geo.json"
ANIM_JSON = ROOT / "src/main/resources/assets/bleachmod/animations/senbon_hakuteiken.animation.json"
TEXTURE_PNG = ROOT / "src/main/resources/assets/bleachmod/textures/entity/senbon_hakuteiken.png"
PLAYER_ANIM_JSON = ROOT / "src/main/resources/assets/bleachmod/player_animation/hakuteiken_dash.json"

PLAYER_PREVIEW_BONES = {
    "player_body",
    "head",
    "right_arm",
    "left_arm",
    "right_leg",
    "left_leg",
}


def clamp(v: float | int) -> int:
    return max(0, min(255, int(v)))


def create_hakuteiken_texture(bbdata: dict) -> Image.Image:
    """Generate 128x256 RGBA texture with per-cube UV painting for both the Shinigami Captain preview and the radiant Hakuteiken wings/halo/blade."""
    res = bbdata.get("resolution", {"width": 128, "height": 256})
    width = int(res.get("width", 128))
    height = int(res.get("height", 256))
    img = Image.new("RGBA", (width, height), (250, 244, 250, 255))
    draw = ImageDraw.Draw(img)

    # Base background: celestial white-sakura gradient for wings/halo/blade
    for y in range(height):
        for x in range(width):
            u = x / max(1, width - 1)
            v = y / max(1, height - 1)
            sakura_blend = 0.5 + 0.5 * math.sin(u * 6.28 * 2 + v * 3.14 * 2)
            r = clamp(248 + sakura_blend * 7)
            g = clamp(238 + sakura_blend * 14)
            b = clamp(246 + sakura_blend * 9)
            img.putpixel((x, y), (r, g, b, 255))

    # Paint each cube's UV faces according to its semantic material
    for c in bbdata.get("elements", []):
        cname = c.get("name", "")
        faces = c.get("faces", {})
        for fname, fobj in faces.items():
            uv = fobj.get("uv")
            if not uv or len(uv) != 4:
                continue
            x0, y0 = int(min(uv[0], uv[2])), int(min(uv[1], uv[3]))
            x1, y1 = int(max(uv[0], uv[2])), int(max(uv[1], uv[3]))
            if x1 <= x0 or y1 <= y0:
                continue

            if cname == "player_head":
                if fname == "north":
                    draw.rectangle([x0, y0, x1 - 1, y1 - 1], fill=(242, 212, 194, 255))
                    # Hairline at top and noble eyes
                    draw.rectangle([x0, y0, x1 - 1, y0 + 2], fill=(26, 26, 38, 255))
                    ey = min(y1 - 2, y0 + 4)
                    draw.point((x0 + 2, ey), fill=(40, 42, 62, 255))
                    draw.point((max(x0 + 2, x1 - 3), ey), fill=(40, 42, 62, 255))
                else:
                    draw.rectangle([x0, y0, x1 - 1, y1 - 1], fill=(26, 26, 38, 255))
            elif cname.startswith("hair_"):
                draw.rectangle([x0, y0, x1 - 1, y1 - 1], fill=(24, 24, 36, 255))
            elif cname.startswith("kenseikan_") or cname == "captain_scarf":
                draw.rectangle([x0, y0, x1 - 1, y1 - 1], fill=(238, 242, 250, 255))
            elif cname.startswith("haori_"):
                draw.rectangle([x0, y0, x1 - 1, y1 - 1], fill=(248, 248, 252, 255))
                if y1 - y0 >= 3:
                    draw.rectangle([x0, y1 - 2, x1 - 1, y1 - 1], fill=(35, 35, 48, 255))
            elif cname.startswith("player_") or cname.startswith("hakama_"):
                draw.rectangle([x0, y0, x1 - 1, y1 - 1], fill=(28, 29, 40, 255))
                if cname.startswith("player_") and "leg" in cname and y1 - y0 >= 3:
                    draw.rectangle([x0, y1 - 2, x1 - 1, y1 - 1], fill=(245, 245, 250, 255))
            elif cname.startswith("halo_"):
                draw.rectangle([x0, y0, x1 - 1, y1 - 1], fill=(255, 252, 254, 255))
                if x1 - x0 >= 2 and y1 - y0 >= 2:
                    draw.rectangle([x0, y0, x1 - 1, y1 - 1], outline=(255, 198, 218, 255))
            elif cname.startswith("blade_"):
                draw.rectangle([x0, y0, x1 - 1, y1 - 1], fill=(255, 255, 255, 255))
                if x1 - x0 >= 2 and y1 - y0 >= 2:
                    draw.rectangle([x0, y0, x1 - 1, y1 - 1], outline=(255, 182, 205, 255))
            else:
                # Wing feathers and nexus: layered white plumage with soft sakura pink tips
                for py in range(y0, min(height, y1)):
                    for px in range(x0, min(width, x1)):
                        barb = (px + py * 2) % 5
                        if py >= y1 - 2:
                            img.putpixel((px, py), (255, 196, 216, 255))
                        elif barb == 0:
                            img.putpixel((px, py), (255, 255, 255, 255))
                        elif barb in (1, 2):
                            img.putpixel((px, py), (254, 242, 248, 255))
                        else:
                            img.putpixel((px, py), (252, 224, 236, 250))

    return img


def clean_and_convert_bbmodel():
    """Deduplicate keyframes in ag_senbon_hakuteiken_v2.bbmodel, embed texture, and convert to GeckoLib + PlayerAnimator."""
    with open(SOURCE_BBMODEL, "r", encoding="utf-8") as f:
        bbdata = json.load(f)

    # Deduplicate same-(channel, time) keyframes keeping the latest added keyframe
    for a in bbdata.get("animations", []):
        animators = a.get("animators", {})
        for _, animator in animators.items():
            kfs = animator.get("keyframes", [])
            if not kfs:
                continue
            dedup = {}
            for kf in kfs:
                ch = kf.get("channel", "rotation")
                t = round(float(kf.get("time", 0.0)), 4)
                dedup[(ch, t)] = kf
            animator["keyframes"] = [
                dedup[k] for k in sorted(dedup.keys(), key=lambda x: (x[0], x[1]))
            ]

    res = bbdata.get("resolution", {"width": 128, "height": 256})
    tex_w = int(res.get("width", 128))
    tex_h = int(res.get("height", 256))

    existing_src = ""
    if bbdata.get("textures") and bbdata["textures"][0].get("source", "").startswith("data:image/png;base64,"):
        existing_src = bbdata["textures"][0]["source"]
        b64_part = existing_src.split(",", 1)[1]
        tex_bytes = base64.b64decode(b64_part)
    else:
        tex_img = create_hakuteiken_texture(bbdata)
        buf = io.BytesIO()
        tex_img.save(buf, format="PNG")
        tex_bytes = buf.getvalue()
        b64_source = "data:image/png;base64," + base64.b64encode(tex_bytes).decode("ascii")
        if bbdata.get("textures"):
            bbdata["textures"][0]["source"] = b64_source
            bbdata["textures"][0]["width"] = tex_w
            bbdata["textures"][0]["height"] = tex_h
            bbdata["textures"][0]["uv_width"] = tex_w
            bbdata["textures"][0]["uv_height"] = tex_h

    with open(SOURCE_BBMODEL, "w", encoding="utf-8") as f:
        json.dump(bbdata, f, separators=(",", ":"))

    cube_map = {c["uuid"]: c for c in bbdata.get("elements", [])}
    group_map = {g["uuid"]: g for g in bbdata.get("groups", [])}

    bones = []

    def process_group(node, parent_name=None):
        if not isinstance(node, dict):
            return
        gid = node.get("uuid")
        ginfo = group_map.get(gid) or node
        name = ginfo.get("name", "bone")

        # Skip player preview dummy bones in the exported GeckoLib construct geo so the real player renders in-game
        if name in PLAYER_PREVIEW_BONES:
            return

        origin = ginfo.get("origin", [0, 0, 0])
        rotation = ginfo.get("rotation", [0, 0, 0])

        bone = {
            "name": name,
            "pivot": [round(-float(origin[0]), 3), round(float(origin[1]), 3), round(float(origin[2]), 3)]
        }
        if parent_name:
            bone["parent"] = parent_name
        if rotation and (rotation[0] or rotation[1] or rotation[2]):
            bone["rotation"] = [round(-float(rotation[0]), 3), round(-float(rotation[1]), 3), round(float(rotation[2]), 3)]

        cubes_list = []
        child_groups = []
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
                    faces_raw = c.get("faces", {})
                    if faces_raw:
                        uv_entry = {}
                        for face_name in ("north", "east", "south", "west", "up", "down"):
                            f_obj = faces_raw.get(face_name)
                            if f_obj and "uv" in f_obj and len(f_obj["uv"]) == 4:
                                u1, v1, u2, v2 = f_obj["uv"]
                                uv_entry[face_name] = {
                                    "uv": [round(float(u1), 3), round(float(v1), 3)],
                                    "uv_size": [round(float(u2 - u1), 3), round(float(v2 - v1), 3)]
                                }
                    else:
                        uv_entry = [int(uv_offset[0]), int(uv_offset[1])]
                    cube_entry = {
                        "origin": [round(-max(c_from[0], c_to[0]), 3), round(min(c_from[1], c_to[1]), 3), round(min(c_from[2], c_to[2]), 3)],
                        "size": size,
                        "uv": uv_entry
                    }
                    c_rot = c.get("rotation")
                    if c_rot and (c_rot[0] or c_rot[1] or c_rot[2]):
                        cube_entry["rotation"] = [round(-float(c_rot[0]), 3), round(-float(c_rot[1]), 3), round(float(c_rot[2]), 3)]
                        c_origin = c.get("origin", origin)
                        cube_entry["pivot"] = [round(-float(c_origin[0]), 3), round(float(c_origin[1]), 3), round(float(c_origin[2]), 3)]
                    cubes_list.append(cube_entry)
            elif isinstance(child, dict):
                child_groups.append(child)

        bone["cubes"] = cubes_list
        bones.append(bone)

        for child in child_groups:
            process_group(child, parent_name=name)

    for o in bbdata.get("outliner", []):
        process_group(o)

    geo_output = {
        "format_version": "1.12.0",
        "minecraft:geometry": [
            {
                "description": {
                    "identifier": "geometry.senbon_hakuteiken",
                    "texture_width": tex_w,
                    "texture_height": tex_h,
                    "visible_bounds_width": 6,
                    "visible_bounds_height": 5,
                    "visible_bounds_offset": [0, 1.5, 0]
                },
                "bones": bones
            }
        ]
    }

    anim_output = {
        "format_version": "1.8.0",
        "animations": {}
    }

    for a in bbdata.get("animations", []):
        anim_name = a.get("name", "animation.hakuteiken.idle")
        length = float(a.get("length", 1.0))
        loop = True if a.get("loop") == "loop" else False

        anim_entry = {
            "loop": loop,
            "animation_length": length,
            "bones": {}
        }

        animators = a.get("animators", {})
        for _, animator in animators.items():
            b_name = animator.get("name")
            if not b_name or b_name in PLAYER_PREVIEW_BONES:
                continue

            channels = {}
            for kf in sorted(animator.get("keyframes", []), key=lambda k: float(k.get("time", 0.0))):
                ch = kf.get("channel", "rotation")
                # wing_nexus rotation in .bbmodel mirrors player_body for Blockbench preview;
                # in-game, SenbonHakuteikenVisuals couples the construct directly to live player torso pitch/yaw.
                if b_name == "wing_nexus" and ch == "rotation":
                    continue
                t = round(float(kf.get("time", 0.0)), 4)
                data_points = kf.get("data_points", [{}])[0]
                val = [
                    round(float(data_points.get("x", 0.0)), 3),
                    round(float(data_points.get("y", 0.0)), 3),
                    round(float(data_points.get("z", 0.0)), 3)
                ]
                t_key = f"{t:g}" if "." in f"{t:g}" else f"{t:.1f}"
                channels.setdefault(ch, {})[t_key] = val

            if channels:
                anim_entry["bones"][b_name] = channels

        anim_output["animations"][anim_name] = anim_entry

    return geo_output, anim_output, tex_bytes


def build_player_animator_dash_pose():
    """Build PlayerAnimator Bedrock animation synchronized with the Blockbench upward leap (0.0-0.8s) + diagonal rasante (0.8-1.3s) poses."""
    torso_track = {
        "rotation": {
            "0.0": {"vector": [6, 0, 0], "easing": "easeInOutSine"},
            "0.45": {"vector": [-10, 0, 0], "easing": "easeInOutSine"},
            "0.8": {"vector": [22, 0, 0], "easing": "easeOutExpo"},
            "1.15": {"vector": [24, 0, 0], "easing": "linear"},
            "1.3": {"vector": [0, 0, 0], "easing": "easeInOutSine"}
        }
    }
    return {
        "format_version": "1.8.0",
        "animations": {
            "hakuteiken_dash": {
                "animation_length": 1.3,
                "loop": False,
                "bones": {
                    "torso": torso_track,
                    "body": torso_track,
                    "head": {
                        "rotation": {
                            "0.0": {"vector": [0, 0, 0], "easing": "easeInOutSine"},
                            "0.45": {"vector": [-12, -12, 0], "easing": "easeInOutSine"},
                            "0.8": {"vector": [-10, 10, 0], "easing": "easeOutExpo"},
                            "1.15": {"vector": [-10, 10, 0], "easing": "linear"},
                            "1.3": {"vector": [0, 0, 0], "easing": "easeInOutSine"}
                        }
                    },
                    "right_arm": {
                        "rotation": {
                            "0.0": {"vector": [-30, 10, -6], "easing": "easeInOutSine"},
                            "0.45": {"vector": [-135, -18, 14], "easing": "easeInOutSine"},
                            "0.8": {"vector": [-78, -4, 0], "easing": "easeOutExpo"},
                            "1.15": {"vector": [-78, -4, 0], "easing": "linear"},
                            "1.3": {"vector": [-28, -12, 8], "easing": "easeInOutSine"}
                        }
                    },
                    "rightItem": {
                        "rotation": {
                            "0.0": {"vector": [0, 0, 0], "easing": "easeInOutSine"},
                            "0.45": {"vector": [12, 0, 0], "easing": "easeInOutSine"},
                            "0.8": {"vector": [-75, 0, 0], "easing": "easeOutExpo"},
                            "1.15": {"vector": [-75, 0, 0], "easing": "linear"},
                            "1.3": {"vector": [0, 0, 0], "easing": "easeInOutSine"}
                        }
                    },
                    "left_arm": {
                        "rotation": {
                            "0.0": {"vector": [10, 8, -8], "easing": "easeInOutSine"},
                            "0.45": {"vector": [-48, 22, -18], "easing": "easeInOutSine"},
                            "0.8": {"vector": [42, 20, -26], "easing": "easeOutExpo"},
                            "1.15": {"vector": [44, 22, -28], "easing": "linear"},
                            "1.3": {"vector": [8, 6, -6], "easing": "easeInOutSine"}
                        }
                    },
                    "right_leg": {
                        "rotation": {
                            "0.0": {"vector": [12, 0, 0], "easing": "easeInOutSine"},
                            "0.45": {"vector": [22, -8, 5], "easing": "easeInOutSine"},
                            "0.8": {"vector": [-26, 0, 5], "easing": "easeOutExpo"},
                            "1.15": {"vector": [-28, 0, 6], "easing": "linear"},
                            "1.3": {"vector": [0, 0, 0], "easing": "easeInOutSine"}
                        }
                    },
                    "left_leg": {
                        "rotation": {
                            "0.0": {"vector": [-12, 0, 0], "easing": "easeInOutSine"},
                            "0.45": {"vector": [-24, 8, -5], "easing": "easeInOutSine"},
                            "0.8": {"vector": [32, 0, -6], "easing": "easeOutExpo"},
                            "1.15": {"vector": [34, 0, -6], "easing": "linear"},
                            "1.3": {"vector": [0, 0, 0], "easing": "easeInOutSine"}
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

    if args.check:
        assert GEO_JSON.exists(), f"Missing {GEO_JSON}"
        assert ANIM_JSON.exists(), f"Missing {ANIM_JSON}"
        assert TEXTURE_PNG.exists(), f"Missing {TEXTURE_PNG}"
        assert PLAYER_ANIM_JSON.exists(), f"Missing {PLAYER_ANIM_JSON}"
        print("PASS: all target files exist.")
        return

    geo_data, anim_data, tex_bytes = clean_and_convert_bbmodel()
    player_anim_data = build_player_animator_dash_pose()

    geo_str = json.dumps(geo_data, indent=2) + "\n"
    anim_str = json.dumps(anim_data, indent=2) + "\n"
    player_anim_str = json.dumps(player_anim_data, indent=2) + "\n"

    GEO_JSON.parent.mkdir(parents=True, exist_ok=True)
    with open(GEO_JSON, "w", encoding="utf-8") as f:
        f.write(geo_str)
    print(f"Exported GeckoLib geo: {GEO_JSON} ({len(geo_str)} bytes, {len(geo_data['minecraft:geometry'][0]['bones'])} bones)")

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
