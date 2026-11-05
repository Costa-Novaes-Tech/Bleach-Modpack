"""Export the user-supplied Blockbench 5 GeckoLib project without executing project code.

Source is preserved byte-for-byte in source/acidwire.bbmodel. Run with --check for
reproducibility. Coordinate/UV conventions follow Blockbench's Bedrock codecs:
https://github.com/JannisX11/blockbench/tree/master/js/formats/bedrock
Only numeric keyframes and the cube/face features actually used here are accepted.
"""
import argparse
import base64
import hashlib
import json
import math
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
SOURCE = ROOT / "tools/models/source/acidwire.bbmodel"
ASSETS = ROOT / "src/main/resources/assets/bleachmod"


def vector(value):
    result = [float(v) for v in value]
    assert len(result) == 3 and all(math.isfinite(v) for v in result)
    return result


def mirrored(value, rotation=False):
    x, y, z = vector(value)
    return [-x, -y if rotation else y, z]


def export():
    model = json.loads(SOURCE.read_text(encoding="utf-8-sig"))
    assert model["meta"]["model_format"] == "geckolib_model"
    groups = {g["uuid"]: g for g in model["groups"]}
    elements = {e["uuid"]: e for e in model["elements"]}
    assert len(groups) == len(model["groups"]) and len(elements) == len(model["elements"])
    names = {g["name"] for g in groups.values()}
    assert len(names) == len(groups)
    bones, seen = [], set()
    width, height = model["resolution"]["width"], model["resolution"]["height"]

    def cube(element):
        assert element.get("type", "cube") == "cube" and not element.get("box_uv")
        low, high = vector(element["from"]), vector(element["to"])
        size = [round(b-a, 6) for a, b in zip(low, high)]
        assert all(v > 0 for v in size)
        result = {"origin": [-high[0], low[1], low[2]], "size": size, "uv": {}}
        if any(element.get("rotation", [0, 0, 0])):
            result.update(pivot=mirrored(element["origin"]), rotation=mirrored(element["rotation"], True))
        if element.get("inflate"):
            result["inflate"] = element["inflate"]
        for face, data in element["faces"].items():
            if data.get("texture") is None:
                continue
            assert data["texture"] == 0 and not data.get("rotation", 0)
            u, v, u2, v2 = data["uv"]
            assert 0 <= min(u, u2) <= max(u, u2) <= width
            assert 0 <= min(v, v2) <= max(v, v2) <= height
            result["uv"][face] = {"uv": [u2, v2] if face in ("up", "down") else [u, v],
                                  "uv_size": [u-u2, v-v2] if face in ("up", "down") else [u2-u, v2-v]}
        return result

    def walk(node, parent=None):
        identifier = node["uuid"] if isinstance(node, dict) else node
        assert identifier not in seen, "Duplicate or cyclic hierarchy"
        seen.add(identifier)
        if identifier in elements:
            assert parent is not None
            if elements[identifier].get("export", True):
                parent["cubes"].append(cube(elements[identifier]))
            return
        group = groups[identifier]
        assert group.get("export", True)
        bone = {"name": group["name"], "pivot": mirrored(group["origin"]), "cubes": []}
        if parent is not None:
            bone["parent"] = parent["name"]
        if any(group.get("rotation", [0, 0, 0])):
            bone["rotation"] = mirrored(group["rotation"], True)
        bones.append(bone)
        for child in node.get("children", []):
            walk(child, bone)

    for node in model["outliner"]:
        walk(node)
    assert seen == set(groups) | set(elements), "Unbound source geometry"
    animations = {}
    for animation in model["animations"]:
        assert animation["loop"] in ("loop", "once")
        clip = {"loop": animation["loop"] == "loop", "animation_length": animation["length"], "bones": {}}
        for identifier, animator in animation["animators"].items():
            assert animator["type"] == "bone" and identifier in groups
            channels = {}
            for key in animator.get("keyframes", []):
                channel = key["channel"]
                assert channel in ("rotation", "position", "scale")
                assert len(key["data_points"]) == 1 and key["interpolation"] in ("catmullrom", "linear")
                assert 0 <= key["time"] <= animation["length"]
                point = key["data_points"][0]
                values = [point[axis] for axis in "xyz"]
                values = vector(values) if channel == "scale" else mirrored(values, channel == "rotation")
                channels.setdefault(channel, {})[str(float(key["time"]))] = {
                    "post": values, "lerp_mode": key["interpolation"]}
            if channels:
                clip["bones"][groups[identifier]["name"]] = channels
        animations[animation["name"]] = clip
    assert set(animations) == {"animation.acidwire."+a for a in ("idle", "walk", "attack")}
    assert animations["animation.acidwire.attack"]["animation_length"] == 1
    texture = model["textures"][0]
    assert len(model["textures"]) == 1 and texture["source"].startswith("data:image/png;base64,")
    png = base64.b64decode(texture["source"].split(",", 1)[1], validate=True)
    assert png[:8] == b"\x89PNG\r\n\x1a\n" and int.from_bytes(png[16:20], "big") == width and int.from_bytes(png[20:24], "big") == height
    geometry = {"format_version": "1.12.0", "minecraft:geometry": [{"description": {
        "identifier": "geometry.bleachmod.acidwire", "texture_width": width, "texture_height": height,
        "visible_bounds_width": 10, "visible_bounds_height": 6, "visible_bounds_offset": [0, 1.75, 0]}, "bones": bones}]}
    dump = lambda obj: (json.dumps(obj, ensure_ascii=False, indent=2)+"\n").encode("utf-8")
    print(f"Acidwire: {len(bones)} bones, {sum(len(b['cubes']) for b in bones)} cubes, 3 clips, {width}x{height}; source SHA256 {hashlib.sha256(SOURCE.read_bytes()).hexdigest()}")
    return {ASSETS / "geo/acidwire.geo.json": dump(geometry),
            ASSETS / "animations/acidwire.animation.json": dump({"format_version": "1.8.0", "animations": animations}),
            ASSETS / "textures/entity/acidwire.png": png}


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    for path, contents in export().items():
        if args.check:
            assert path.read_bytes() == contents, f"Stale asset: {path}"
        else:
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(contents)
