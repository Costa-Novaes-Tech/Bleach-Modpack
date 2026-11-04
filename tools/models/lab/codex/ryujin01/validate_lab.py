"""Validate the exact Blockbench lab exports without launching Minecraft."""
from __future__ import annotations

import base64
import json
from pathlib import Path


ROOT = Path(__file__).resolve().parent


def read_json(name: str) -> dict:
    return json.loads((ROOT / name).read_text(encoding="utf-8"))


for name, expected_cubes in (
    ("ryujin_sun_formation_lab", 139),
    ("ryujin_bankai_pose_lab", 19),
    ("ryujin_sun_vortex_alpha_lab", 171),
):
    source = read_json(f"{name}.bbmodel")
    geo = read_json(f"{name}.geo.json")
    clips = read_json(f"{name}.animation.json")
    png = (ROOT / f"{name}.png").read_bytes()
    embedded = source["textures"][0]["source"].split("base64,", 1)[1]
    assert base64.b64decode(embedded) == png, f"{name}: PNG differs from Blockbench source"
    assert png[:8] == b"\x89PNG\r\n\x1a\n", f"{name}: invalid PNG"
    dimensions = (int.from_bytes(png[16:20], "big"), int.from_bytes(png[20:24], "big"))
    assert source["meta"]["model_format"] == "geckolib_model"
    assert source["name"] == name
    assert len(source["elements"]) == expected_cubes
    geometry = geo["minecraft:geometry"][0]
    assert geometry["description"]["identifier"] == f"geometry.{name}"
    assert sum(len(bone.get("cubes", [])) for bone in geometry["bones"]) == expected_cubes
    assert dimensions == (
        geometry["description"]["texture_width"],
        geometry["description"]["texture_height"],
    )
    bone_names = {bone["name"] for bone in geometry["bones"]}
    assert clips["format_version"] == "1.8.0"
    assert len(clips["animations"]) == 1
    animation = next(iter(clips["animations"].values()))
    assert set(animation["bones"]).issubset(bone_names)
    if name == "ryujin_sun_vortex_alpha_lab":
        flame_scales = animation["bones"]["flame_00"]["scale"]
        scale_values = [entry["post"]["vector"][0] for entry in flame_scales.values()]
        assert min(scale_values) <= 0.03 and max(scale_values) >= 1.2
        assert scale_values[-1] == 1
    print(f"{name}: {expected_cubes} cubes, {len(bone_names)} bones, PNG {dimensions[0]}x{dimensions[1]}, animation {animation['animation_length']}s")

sun_qa = read_json("qa/sun-refine-v3/results.json")
sun_check = next(item for item in sun_qa if item["name"] == "check_model")
assert json.loads(sun_check["text"][0])["issue_count"] == 0
pose_qa = read_json("qa/pose-v5/results.json")
pose_check = next(item for item in pose_qa if item["name"] == "check_model")
assert json.loads(pose_check["text"][0])["issue_count"] == 0
rig_check = next(item for item in pose_qa if item["name"] == "check_rig")
assert json.loads(rig_check["text"][0])["ready_to_animate"] is True
motion_check = next(item for item in pose_qa if item["name"] == "analyze_animation")
assert json.loads(motion_check["text"][0])["issue_count"] == 0
alpha_qa = read_json("qa/alpha-flames-v2/results.json")
alpha_check = next(item for item in alpha_qa if item["name"] == "check_model")
assert json.loads(alpha_check["text"][0])["issue_count"] == 0
print("Blockbench QA: original sun/model 0; alpha vortex/model 0; pose/model 0; rig ready; pose motion 0")
