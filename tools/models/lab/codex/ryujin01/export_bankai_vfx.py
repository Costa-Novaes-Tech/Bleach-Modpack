"""Derive the production GeckoLib VFX from the editable v7 Blockbench scene.

The source remains untouched. Run this after changing the v7 model or exports.
"""
import copy
import json
from pathlib import Path
import shutil

ROOT = Path(__file__).resolve().parent
SOURCE = ROOT / "ryujin_bankai_activation_scene_v7_lab"
DEST = Path(__file__).resolve().parents[5] / "src/main/resources/assets/bleachmod"
NAME = "ryujin_bankai_sun"


def read(path):
    return json.loads(path.read_text(encoding="utf-8"))


def write(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")


def descendants(node):
    yield node
    for child in node.get("children", []):
        if isinstance(child, dict):
            yield from descendants(child)
        else:
            yield child


def main():
    source_geo = read(SOURCE.with_suffix(".geo.json"))
    source_anim = read(SOURCE.with_suffix(".animation.json"))
    source_bb = read(SOURCE.with_suffix(".bbmodel"))
    all_bones = source_geo["minecraft:geometry"][0]["bones"]
    chosen = {"root", "sun_root"}
    chosen.update(b["name"] for b in all_bones if b["name"].startswith("absorb_spark_"))
    chosen.update(b["name"] for b in all_bones if b.get("parent") == "sun_root")
    assert len(chosen) == 33, f"unexpected bone count: {len(chosen)}"
    assert sum(len(b.get("cubes", [])) for b in all_bones if b["name"] in chosen) == 197

    geo = copy.deepcopy(source_geo)
    geometry = geo["minecraft:geometry"][0]
    geometry["bones"] = [b for b in geometry["bones"] if b["name"] in chosen]
    geometry["description"]["identifier"] = "geometry." + NAME
    assert all(b.get("parent") in chosen for b in geometry["bones"] if b.get("parent"))
    write(DEST / "geo" / f"{NAME}.geo.json", geo)

    anim = copy.deepcopy(source_anim)
    clip = next(iter(anim["animations"].values()))
    clip["bones"] = {k: v for k, v in clip["bones"].items() if k in chosen}
    # The runtime sun sits 2.5 blocks lower. Lift only its sealing travel so
    # the final small core still converges near the real blade instead of ground.
    for timestamp, keyframe in clip["bones"]["sun_root"]["position"].items():
        t = float(timestamp)
        lift = 30 * max(0, min(1, (t - 5.8) / (7.0833 - 5.8)))
        keyframe["vector"][1] += round(lift, 4)
    anim["animations"] = {"animation.ryujin.bankai_sun": clip}
    write(DEST / "animations" / f"{NAME}.animation.json", anim)

    groups = {g["name"]: g["uuid"] for g in source_bb["groups"]}
    uuids = {groups[name] for name in chosen}
    outliner = []
    for root in source_bb["outliner"]:
        if root["uuid"] == groups["sun_root"]:
            outliner.append(root)
        elif root["uuid"] == groups["root"]:
            copy_root = copy.deepcopy(root)
            copy_root["children"] = [x for x in root["children"]
                                     if isinstance(x, dict) and x["uuid"] in uuids]
            outliner.insert(0, copy_root)
    selected = {x if isinstance(x, str) else x["uuid"]
                for root in outliner for x in descendants(root)}
    bb = copy.deepcopy(source_bb)
    bb["name"] = NAME
    bb["model_identifier"] = NAME
    bb["groups"] = [g for g in bb["groups"] if g["uuid"] in uuids]
    bb["elements"] = [e for e in bb["elements"] if e["uuid"] in selected]
    bb["outliner"] = outliner
    for clip_bb in bb["animations"]:
        clip_bb["name"] = "animation.ryujin.bankai_sun"
        clip_bb["animators"] = {k: v for k, v in clip_bb["animators"].items() if k in uuids}
        for keyframe in clip_bb["animators"][groups["sun_root"]]["keyframes"]:
            if keyframe["channel"] != "position":
                continue
            t = float(keyframe["time"])
            lift = 30 * max(0, min(1, (t - 5.8) / (7.0833 - 5.8)))
            for point in keyframe["data_points"]:
                point["y"] = str(round(float(point["y"]) + lift, 4))
    assert len(bb["elements"]) == 197
    write(ROOT / f"{NAME}.bbmodel", bb)

    for suffix in (".png", "_glowmask.png"):
        src = ROOT / f"{SOURCE.name}{suffix}"
        dst = DEST / "textures/entity" / f"{NAME}{suffix}"
        dst.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(src, dst)
    print(f"{NAME}: 33 bones, 197 cubes, 12s animation; source unchanged")


if __name__ == "__main__":
    main()
