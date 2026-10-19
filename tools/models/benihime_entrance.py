"""Build the editable Blockbench *authoring scaffold* for Benihime's entrance.

Run ``python tools/models/benihime_entrance.py``; ``--check`` verifies reproducibility.
The .bbmodel is a local free-format rig source, NOT a GeckoLib/runtime export.
The playable vertical slice is BenihimeEntranceGeometry.java (procedural surfaces).
No external Python packages, borrowed textures, downloads or Blender are required.
"""
from __future__ import annotations

import argparse
import base64
import json
from pathlib import Path
import struct
import uuid
import zlib

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / "tools/models/source/benihime_entrance.bbmodel"
NAMESPACE = uuid.UUID("965c43c8-fcd0-4c6c-a673-a81b571d17ad")
COLORS = [(232, 211, 193), (24, 22, 31), (145, 25, 43), (91, 16, 34),
          (208, 192, 171), (21, 20, 25), (186, 64, 83), (137, 78, 82)]


def identity(name: str) -> str:
    return str(uuid.uuid5(NAMESPACE, name))


def units(point):
    return [round(value * 16, 5) for value in point]


def palette_png() -> str:
    """Original 128-square palette: 8 strips; consistent cube UVs stay in each strip."""
    def chunk(kind, data):
        return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data) & 0xffffffff)
    scanlines = b"".join(b"\0" + b"".join(bytes(COLORS[x // 16]) for x in range(128)) for _ in range(128))
    png = b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack(">IIBBBBB", 128, 128, 8, 2, 0, 0, 0))
    png += chunk(b"IDAT", zlib.compress(scanlines)) + chunk(b"IEND", b"")
    return "data:image/png;base64," + base64.b64encode(png).decode("ascii")


def build():
    elements = []

    def group(name, origin, parent=None):
        result = {"name": name, "origin": units(origin), "rotation": [0, 0, 0],
                  "uuid": identity(name), "export": True, "isOpen": False, "visibility": True, "children": []}
        if parent is not None:
            parent["children"].append(result)
        return result

    def box(name, frm, to, color, parent):
        uid = identity(name)
        uv = [color * 16 + 1, 1, color * 16 + 14, 14]
        elements.append({"name": name, "type": "cube", "uuid": uid, "from": units(frm), "to": units(to),
                         "origin": parent["origin"], "rotation": [0, 0, 0], "rescale": False,
                         "shade": True, "visibility": True, "export": True, "color": color,
                         "faces": {face: {"uv": uv, "texture": 0} for face in ["north", "south", "east", "west", "up", "down"]}})
        parent["children"].append(uid)

    root = group("benihime_authoring_root", (0, 0, 11.1))
    hips = group("pelvis", (0, 2.25, 11.1), root)
    dress = group("dress_layers", (0, .15, 11.1), hips)
    for layer, (width, depth, color) in enumerate([(4.9, 3.58, 3), (4.53, 3.31, 2), (4.16, 3.04, 2)]):
        # Individual flaps are intended to be reshaped into authored surfaces in the editor.
        for side in [-1, 1]:
            flap = group(f"robe_{layer}_{side}", (side * 1.3, 2.25, 11.1), dress)
            box(f"robe_panel_{layer}_{side}", (side * width if side < 0 else .2, .11 + layer * .04, 11.1 - depth),
                (-.2 if side < 0 else width, 2.37, 11.1 + depth), color, flap)
    chest = group("spine_chest", (0, 4.8, 11), hips)
    box("torso", (-1.65, 2.5, 10.2), (1.65, 7.45, 11.8), 2, chest)
    for side in [-1, 1]:
        lapel = group(f"lapel_{side}", (side * .4, 7.8, 10.4), chest)
        box(f"ivory_lapel_{side}", (-.5 if side < 0 else .1, 3.1, 9.95), (-.1 if side < 0 else .5, 7.9, 10.14), 4, lapel)
    neck = group("neck", (0, 8.2, 11), chest)
    box("neck_surface", (-.31, 7.5, 10.7), (.31, 8.6, 11.3), 0, neck)
    head = group("head", (0, 9.47, 10.9), neck)
    box("head_sculpt_base", (-.68, 8.6, 10.35), (.68, 10.4, 11.45), 0, head)
    box("jaw_sculpt_base", (-.43, 8.4, 10.49), (.43, 8.9, 11.31), 0, head)
    box("nose", (-.055, 9.22, 10.20), (.055, 9.60, 10.34), 0, head)
    for side in [-1, 1]:
        lo, hi = (-.48, -.13) if side == -1 else (.13, .48)
        box(f"closed_eye_{side}", (lo, 9.41, 10.29), (hi, 9.45, 10.34), 1, head)
    box("neutral_lips", (-.14, 8.95, 10.37), (.14, 8.98, 10.42), 7, head)
    hair = group("hair_mass", (0, 10.3, 11.2), head)
    box("hair_crown", (-.85, 10.1, 10.45), (.85, 10.85, 11.9), 1, hair)
    box("hair_back", (-1.03, 8.1, 11.35), (1.03, 10.7, 12.1), 1, hair)
    for side in [-1, 1]:
        for lock in range(5):
            x = side * (.9 + .15 * lock)
            strand = group(f"hair_lock_{side}_{lock}", (x, 10.1, 11), hair)
            box(f"hair_strand_{side}_{lock}", (x - .12, 2.1 + lock * .17, 10.0 + lock * .15),
                (x + .12, 10.3, 10.32 + lock * .15), 1, strand)
    for side in [-1, 1]:
        shoulder_x = side * 1.93
        shoulder = group(f"shoulder_{side}", (shoulder_x, 7.65, 11), chest)
        # Straight authoring pose makes all joints editable; runtime uses fixed-length IK.
        elbow_y, wrist_y = 3.75, -.30
        box(f"upper_arm_{side}", (shoulder_x - .40, elbow_y, 10.65), (shoulder_x + .40, 7.65, 11.35), 0, shoulder)
        elbow = group(f"elbow_{side}", (shoulder_x, elbow_y, 11), shoulder)
        box(f"forearm_{side}", (shoulder_x - .27, wrist_y, 10.75), (shoulder_x + .27, elbow_y, 11.25), 0, elbow)
        wrist = group(f"wrist_{side}", (shoulder_x, wrist_y, 11), elbow)
        box(f"palm_{side}", (shoulder_x - .46, wrist_y - .40, 10.45), (shoulder_x + .46, wrist_y, 11.45), 0, wrist)
        for finger in range(4):
            x = shoulder_x + (finger - 1.5) * .2
            parent, y = wrist, wrist_y - .35
            for segment, length in enumerate([.39, .33, .25]):
                joint = group(f"finger_{side}_{finger}_{segment}", (x, y, 10.60), parent)
                box(f"finger_surface_{side}_{finger}_{segment}", (x - .07, y - length, 10.53), (x + .07, y, 10.67), 0, joint)
                parent, y = joint, y - length
        thumb = group(f"thumb_{side}", (shoulder_x + side * .4, wrist_y - .20, 10.9), wrist)
        x = shoulder_x + side * .48
        box(f"thumb_surface_{side}", (x - .10, wrist_y - .75, 10.71), (x + .10, wrist_y - .15, 10.91), 0, thumb)
    curtain = group("curtain", (0, 12, 12.1))
    for side in [-1, 1]:
        panel = group(f"curtain_panel_{side}", (side * 8, 12, 12.1), curtain)
        box(f"opaque_curtain_{side}", (-8 if side < 0 else -.045, 0, 12.05),
            (.045 if side < 0 else 8, 12, 12.15), 5, panel)
        hem = group(f"central_hem_{side}", (side * -.045, 4.6, 12.1), panel)
        box(f"hem_contact_guide_{side}", (side * -.045 - .03, 4.55, 12.04),
            (side * -.045 + .03, 4.65, 12.16), 6, hem)
    return {"meta": {"format_version": "4.10", "model_format": "free", "box_uv": False},
            "name": "Benihime entrance — EDITABLE SCAFFOLD, runtime procedural",
            "model_identifier": "benihime_entrance_authoring", "visible_box": [0, 0, 0],
            "variable_placeholders": "", "resolution": {"width": 128, "height": 128},
            "elements": elements, "outliner": [root, curtain],
            "textures": [{"path": "benihime_original_palette.png", "name": "benihime_original_palette.png",
                          "folder": "", "namespace": "bleachmod", "id": "0", "particle": False,
                          "uuid": identity("palette"), "source": palette_png(), "mode": "bitmap", "saved": False}],
            "animations": []}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    text = json.dumps(build(), ensure_ascii=False, indent=2) + "\n"
    if args.check:
        if not OUT.exists() or OUT.read_text(encoding="utf-8") != text:
            raise SystemExit("Authoring source missing or differs from deterministic generator")
        print(f"PASS: {OUT.relative_to(ROOT)} matches generator; source only, no runtime export")
    else:
        OUT.parent.mkdir(parents=True, exist_ok=True)
        OUT.write_text(text, encoding="utf-8")
        print(f"Wrote {OUT.relative_to(ROOT)}; authoring scaffold, not a game/GeckoLib asset")


if __name__ == "__main__":
    main()
