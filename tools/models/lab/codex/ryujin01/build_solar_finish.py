"""Generate geometry-only and full-material Ryujin sun A/B resource packs."""
from copy import deepcopy
from pathlib import Path
import json
import math

from PIL import Image

from build_sun_v13 import HERE, REPO, NAME, bake_volume_faces, ribbon, rounded


def geo_cube(element):
    x, y, z = element["from"]
    tx, ty, tz = element["to"]
    px, py, pz = element["origin"]
    uv = {}
    for name, face in element["faces"].items():
        u0, v0, u1, v1 = face["uv"]
        if name in ("up", "down"):
            u0, v0, u1, v1 = u1, v1, u0, v0
        uv[name] = {"uv": rounded([u0, v0]), "uv_size": rounded([u1-u0, v1-v0])}
    return {"origin": rounded([-tx, y, z]), "size": rounded([tx-x, ty-y, tz-z]),
            "pivot": rounded([-px, py, pz]), "rotation": element["rotation"], "uv": uv}


def soften_bands(atlas):
    for y in range(8, 249):
        for x in range(18, 239):
            u = (x - 128) / 110
            flow = .85 + .15 * math.sin(y / 16 + 3*u)
            envelope = max(0, min(1, (y-8)/27, (249-y)/28))
            edge = max(0, 1-abs(u)) ** .76
            hot = edge ** 1.7 * flow
            atlas.putpixel((x, 2624+y), (255, int(129+98*hot), int(13+99*hot),
                                       int(166*edge*envelope)))


def write_pack(root, geometry, atlas):
    root.mkdir(parents=True, exist_ok=True)
    (root / "pack.mcmeta").write_text(json.dumps({"pack": {
        "pack_format": 15, "description": root.name}}), encoding="utf-8")
    assets = root / "assets/bleachmod"
    (assets / "geo").mkdir(parents=True, exist_ok=True)
    (assets / "textures/entity").mkdir(parents=True, exist_ok=True)
    (assets / "geo/ryujin_bankai_sun.geo.json").write_text(
        json.dumps(geometry, separators=(",", ":")), encoding="utf-8")
    atlas.save(assets / "textures/entity/ryujin_bankai_sun.png")
    (assets / "ryujin_solar_atlas_v16.json").write_text('{"atlas":"v16"}', encoding="utf-8")


def main():
    geometry = json.loads((HERE / (NAME+".geo.json")).read_text(encoding="utf-8"))
    model = json.loads((HERE / (NAME+".bbmodel")).read_text(encoding="utf-8"))
    atlas = Image.open(HERE / (NAME+".png")).convert("RGBA")
    assert atlas.size == (256, 4096)
    bones = {b["name"]: b for b in geometry["minecraft:geometry"][0]["bones"]}
    for bone, name, radius, depth, count, phase, turns in (
        ("inner_swirl","inner_flow",49,23,38,.1,1.07),
        ("mid_swirl","middle_flow",66,26,44,1.9,1.02),
        ("outer_swirl","outer_flow",76,30,50,3.5,1.01),
    ):
        bones[bone]["cubes"] = [geo_cube(e) for e in ribbon(name,radius,depth,count,phase,turns,finer=True)]
    soften_bands(atlas)
    packs = REPO / "build/visual-lab/ryujin-v16-packs"
    write_pack(packs / "RyujinSunV16Bands", geometry, atlas)
    core = [deepcopy(e) for e in model["elements"] if e["name"].startswith("solar_volume_")]
    assert len(core) == len(bones["sun_core"]["cubes"]) == 184
    bake_volume_faces(atlas, core, polished=True)
    assert all(geo_cube(e)["uv"] == cube["uv"] for e, cube in zip(core, bones["sun_core"]["cubes"]))
    write_pack(packs / "RyujinSunV16Full", geometry, atlas)
    print("Both candidates: 184 core cuboids, 132 smaller band segments.")


if __name__ == "__main__":
    main()
