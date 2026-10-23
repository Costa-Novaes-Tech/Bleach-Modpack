"""Original Hollow rigs: editable Blockbench sources + GeckoLib runtime exports.

python tools/models/hollow_variants.py [--check]
Uses only the standard library. No external art, image editing or downloads.
The open chest is built around a real gap; no black decal closes the hole.
"""
from pathlib import Path
import argparse
import base64
import json
import math
import struct
import uuid
import zlib
from types import SimpleNamespace

ROOT = Path(__file__).resolve().parents[2]
ASSETS = ROOT / "src/main/resources/assets/bleachmod"
PALETTES = {
    "brute": [(61, 43, 77), (132, 91, 147), (26, 20, 36), (245, 232, 196), (176, 149, 121), (10, 9, 17), (255, 164, 35), (108, 40, 57)],
    "swift": [(39, 65, 58), (94, 134, 109), (17, 32, 29), (239, 233, 202), (144, 171, 144), (9, 16, 15), (255, 191, 44), (87, 44, 56)],
    "flying": [(37, 34, 56), (115, 99, 144), (16, 16, 28), (243, 232, 213), (173, 149, 158), (8, 9, 17), (255, 149, 41), (154, 48, 74)],
}
FACES = ["north", "south", "east", "west", "up", "down"]
NS = uuid.UUID("191c26a6-0428-49ed-99ae-32709e5b8b1a")


def uid(name):
    return str(uuid.uuid5(NS, name))


def texture(colors):
    def chunk(kind, data):
        return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data) & 0xffffffff)
    rows = []
    for y in range(32):
        row = bytearray(b"\0")
        for x in range(128):
            # Each 16px swatch has a restrained, original pixel texture.
            # Broad material shading with fine grain; a brighter top and dark edges
            # make muscles, bone and membranes readable without emissive lighting.
            u = x % 16
            shade = round(10 * math.cos((u - 7.5) * math.pi / 16) - 8 * y / 31)
            shade += ((x * 13 + y * 7) % 5 - 2)
            row.extend(max(0, min(255, c + shade)) for c in colors[x // 16])
        rows.append(bytes(row))
    return b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack(">IIBBBBB", 128, 32, 8, 2, 0, 0, 0)) + chunk(b"IDAT", zlib.compress(b"".join(rows))) + chunk(b"IEND", b"")


class Rig:
    def __init__(self, variant):
        self.variant = variant
        self.bones = []
        self.parts = []

    def bone(self, name, pivot, parent="root", rotation=None):
        bone = {"name": name, "pivot": list(pivot)}
        if parent:
            bone["parent"] = parent
        if rotation:
            bone["rotation"] = rotation
        bone["cubes"] = []
        self.bones.append(bone)
        return bone

    def box(self, bone, name, origin, size, color=0, rotation=None, pivot=None):
        uv = {f: {"uv": [color * 16 + 1, 1], "uv_size": [12, 12]} for f in FACES}
        cube = {"origin": list(origin), "size": list(size), "uv": uv}
        if rotation:
            cube.update(rotation=rotation, pivot=pivot or bone["pivot"])
        bone["cubes"].append(cube)
        self.parts.append((bone["name"], name, cube, color))

    def mass(self, bone, name, center, size, color=0):
        """Overlapping bevel volumes break the silhouette of a straight rectangular limb."""
        x, y, z = center
        w, h, d = size
        for i, (sx, sy, sz) in enumerate([(1, .72, .72), (.76, 1, .76), (.76, .78, 1)]):
            self.box(bone, f"{name}_{i}", (x - w * sx / 2, y - h * sy / 2, z - d * sz / 2),
                     (w * sx, h * sy, d * sz), color)

    def skull(self, center_y, width, height, z, horns):
        head = self.bone("head", (0, center_y, z + 2), "body")
        w = width / 2
        self.box(head, "cranium", (-w, center_y - height / 2, z), (width, height, 4), 3)
        self.box(head, "brow", (-w - .5, center_y + .6, z - .5), (width + 1, 1.2, 1), 4)
        for s in [-1, 1]:
            x = s * width * .24
            self.box(head, f"socket_{s}", (x - 1.1, center_y - .8, z - .65), (2.2, 1.7, .55), 5,
                     [0, 0, s * -12], [x, center_y, z])
            self.box(head, f"eye_{s}", (x - .35, center_y - .3, z - .72), (.7, .5, .2), 6)
            self.box(head, f"cheek_{s}", (s * (w - .6) - .6, center_y - height / 2, z - .35), (1.2, height * .45, .8), 4)
            if horns:
                self.box(head, f"horn_{s}", (s * (w - .5) - .6, center_y + height / 2 - .7, z + .6), (1.2, horns, 1.4), 3,
                         [0, 0, -s * 24], [s * (w - .5), center_y + height / 2 - .7, z + 1])
        self.box(head, "nose_void", (-.45, center_y - 1.8, z - .7), (.9, 1.3, .3), 5)
        jaw = self.bone("jaw", (0, center_y - height / 2 + .6, z + 1.5), "head")
        mouth_y = center_y - height / 2 - .2
        self.box(jaw, "mouth", (-w + .7, mouth_y, z - .45), (width - 1.4, 1.8, .5), 5)
        self.box(jaw, "jawbone", (-w + .5, mouth_y - .6, z), (width - 1, .7, 2.6), 3)
        for i in range(max(3, int(width - 2))):
            x = -w + 1 + i * (width - 2) / max(3, int(width - 2))
            self.box(jaw, f"tooth_{i}", (x, mouth_y + .85, z - .6), (.55, .85, .4), 3)
        return head

    def chest(self, y, halfwidth, height, depth, hole):
        body = self.bone("body", (0, y, 0))
        # Four separate volumes leave the opening visible all the way through the torso.
        self.box(body, "chest_top", (-halfwidth, y + hole, -depth / 2), (halfwidth * 2, height - hole, depth), 0)
        self.box(body, "abdomen", (-halfwidth * .72, y - height * .72, -depth * .43), (halfwidth * 1.44, height * .72 - hole, depth * .86), 0)
        for s in [-1, 1]:
            x = -halfwidth if s < 0 else hole
            self.box(body, f"ribcage_{s}", (x, y - hole, -depth / 2), (halfwidth - hole, hole * 2, depth), 0)
            self.box(body, f"pectoral_{s}", (s * (halfwidth * .5) - halfwidth * .36, y + hole + .4, -depth / 2 - .5), (halfwidth * .72, height * .32, .8), 1)
            self.box(body, f"hole_rim_side_{s}", (s * hole - .25, y - hole, -depth / 2 - .15), (.5, hole * 2, .4), 2)
        for dy in [-hole, hole]:
            self.box(body, f"hole_rim_{dy}", (-hole, y + dy - .2, -depth / 2 - .15), (hole * 2, .4, .4), 2)
        return body

    def limb(self, name, pivot, length, width, depth, hand=False):
        bone = self.bone(name, pivot, "body")
        x, y, z = pivot
        self.box(bone, name + "_upper", (x - width / 2, y - length * .53, z - depth / 2), (width, length * .53, depth), 0)
        lower = self.bone(name + "_lower", (x, y - length * .53, z), name)
        self.box(lower, name + "_lower", (x - width * .43, y - length, z - depth * .45), (width * .86, length * .49, depth * .9), 1)
        self.box(lower, "knuckles" if hand else "foot", (x - width * .55, y - length, z - depth * .8), (width * 1.1, 1.7, depth * 1.4), 2)
        for i in range(3):
            self.box(lower, f"claw_{i}", (x - width * .45 + i * width * .32, y - length - (.9 if hand else 0), z - depth * .85), (.7, 2 if hand else .8, 1.3), 3)
        return bone


def build_rig(variant):
    r = Rig(variant)
    r.bone("root", (0, 0, 0), None)
    if variant == "brute":
        # Beast silhouette: low pelvis, bent legs, forward neck, vaulted back and
        # uneven bony shoulders. Chest/hips articulate independently of the arms.
        body = r.bone("body", (0, 18, 1))
        r.mass(body, "pelvis", (0, 18.7, 1), (13, 7, 9), 2)
        chest = r.bone("chest", (0, 24, 1), "body", [11, 0, 0])
        r.mass(chest, "abdomen", (0, 23.5, 0), (12, 8, 8), 0)
        # Split thorax leaves a genuine through-hole; bevels round its silhouette.
        r.mass(chest, "upper_thorax", (0, 35.5, .3), (24, 9, 12), 0)
        for s, side in [(-1, "right"), (1, "left")]:
            r.mass(chest, side + "_ribs", (s * 6.4, 29.5, 0), (7.8, 7, 10), 0)
            r.mass(chest, side + "_pectoral", (s * 6, 35.3, -5.1), (10, 5, 3), 1)
            r.box(chest, side + "_collar", (s * 6 - 4, 37.3, -5.7), (8, 1.2, 2), 4,
                  [0, 0, s * 12], [s * 6, 38, -4.7])
            r.box(chest, side + "_hole_edge", (s * 2.55 - .3, 27, -5), (.6, 5, .8), 4)
        for y in [26.7, 32]:
            r.box(chest, "hole_lip", (-2.7, y, -5), (5.4, .6, .8), 4)
        r.mass(chest, "hunched_mantle", (0, 38.6, 3), (18, 10, 12), 2)
        for i in range(4):
            r.box(chest, f"dorsal_ridge_{i}", (-.8, 27 + i * 3.7, 5.2), (1.6, 2.6, 4.2), 4,
                  [-24, 0, 0], [0, 28 + i * 3.7, 5.2])
        # Elongated predator mask, a split crest and swept horns replace the golem face.
        head = r.bone("head", (0, 40, -3.5), "chest", [-10, 0, 0])
        r.mass(head, "mask_skull", (0, 42.3, -5.4), (8.4, 10, 6.4), 3)
        for s in [-1, 1]:
            r.box(head, f"mask_cheek_{s}", (s * 3.4 - .8, 37, -9.1), (1.6, 7, 2.8), 4,
                  [0, 0, s * 15], [s * 3.4, 41, -7.7])
            r.box(head, f"predator_socket_{s}", (s * 2 - 1.3, 41.2, -9), (2.6, 1.1, .6), 5,
                  [0, 0, -s * 24], [s * 2, 42, -8.7])
            r.box(head, f"ember_eye_{s}", (s * 2 - .6, 41.5, -9.25), (1.2, .4, .4), 6)
            horn = r.bone(f"horn_{s}", (s * 3.3, 46, -4.4), "head", [-20, 0, -s * 28])
            r.box(horn, "swept_horn", (s * 3.3 - .9, 45.7, -5.2), (1.8, 6.5 if s < 0 else 5.6, 1.8), 3)
            r.box(horn, "horn_tip", (s * 3.3 - .45, 51.4 if s < 0 else 50.5, -4.8), (.9, 2, 1), 4,
                  [-22, 0, 0], [s * 3.3, 51.4 if s < 0 else 50.5, -4.3])
        r.box(head, "mask_center_ridge", (-.7, 39.3, -9.3), (1.4, 7.2, 1.1), 3)
        r.box(head, "nasal_cleft", (-.45, 38.7, -9.6), (.9, 1.7, .5), 5)
        jaw = r.bone("jaw", (0, 38.2, -5.6), "head")
        r.box(jaw, "snarl", (-2.8, 36.7, -9.2), (5.6, 2, 2), 5)
        r.mass(jaw, "jaw_bone", (0, 36.4, -7.2), (6.4, 2, 4.4), 3)
        for i in range(6):
            r.box(jaw, f"fang_{i}", (-2.6 + i * .9, 37.2 - (i in [0, 5]) * .4, -9.6),
                  (.6, 1.6 if i in [0, 5] else 1, .7), 3)
        for s, side in [(-1, "right"), (1, "left")]:
            x = s * 12
            arm = r.bone(side + "_arm", (x, 36, .5), "chest", [-18, 0, s * 14])
            r.mass(arm, "deltoid", (x, 34.4, .5), (10, 10, 10), 0)
            r.mass(arm, "upper_arm", (x, 28.8, .5), (7, 12, 7), 1)
            r.box(arm, "shoulder_scute", (x - 4, 37, -2.7), (8, 2.1, 6), 4,
                  [0, 0, s * 16], [x, 38, .3])
            lower = r.bone(side + "_arm_lower", (x, 23, .5), side + "_arm", [25, 0, 0])
            r.mass(lower, "heavy_forearm", (x, 18.5, .5), (8.4 if s < 0 else 7.8, 10.7, 8), 0)
            r.box(lower, "forearm_tendon", (x - 1.5, 15, -4.1), (3, 7, 1.2), 1)
            hand = r.bone(side + "_hand", (x, 13, .5), side + "_arm_lower", [-10, 0, 0])
            r.mass(hand, "palm", (x, 11.4, .2), (8, 4.5, 6), 2)
            for finger in range(3):
                fx = x - 2.6 + finger * 2.2
                digit = r.bone(f"{side}_finger_{finger}", (fx, 10.5, -1.3), side + "_hand", [12, 0, 0])
                r.box(digit, "curved_finger", (fx - .65, 7.6, -2), (1.3, 3.4, 1.8), 0)
                r.box(digit, "ivory_claw", (fx - .45, 6.7, -2.3), (.9, 1.8, 1.2), 3, [-25, 0, 0], [fx, 8, -1.5])
            leg = r.bone(side + "_leg", (s * 5, 18.5, 1), "body", [-13, 0, s * 5])
            r.mass(leg, "haunch", (s * 5, 14.6, 1), (7.2, 10, 8), 0)
            shin = r.bone(side + "_leg_lower", (s * 5, 10, 1), side + "_leg", [24, 0, 0])
            r.mass(shin, "shin", (s * 5, 6, 1), (4.7, 9, 5.4), 1)
            foot = r.bone(side + "_foot", (s * 5, 2, 1), side + "_leg_lower", [-11, 0, 0])
            r.mass(foot, "wide_paw", (s * 5, 1.4, -1), (7.4, 3, 8), 2)
            for i in range(3):
                r.box(foot, f"toe_{i}", (s * 5 - 2.8 + i * 2, .4, -6), (1.2, 1.4, 2.2), 3)
    elif variant == "swift":
        body = r.chest(12, 4.2, 4.7, 5, 1.1)
        r.box(body, "hunched_back", (-3.5, 15, .2), (7, 3.5, 4.3), 2)
        r.skull(18.2, 5.8, 5.4, -5, 1.4)
        for s, side in [(-1, "right"), (1, "left")]:
            r.limb(side + "_arm", (s * 4.6, 15.5, -.6), 11.5, 2.3, 2.7, True)
            r.limb(side + "_leg", (s * 2.7, 8.8, .7), 8.8, 2.8, 3)
        # Every child starts at its parent's unrotated endpoint. Curvature lives on
        # the bones, so both the next segment and its pivot inherit the same bend.
        z, parent = 2.5, "body"
        for i, (name, length, width, bend) in enumerate([
                ("tail", 3.8, 1.9, 18), ("tail_1", 3.5, 1.5, 15),
                ("tail_2", 3.2, 1.1, 18), ("tail_tip", 2.8, .7, 12)]):
            tail = r.bone(name, (0, 9, z), parent, [bend, 0, 0])
            r.box(tail, "tail_segment", (-width / 2, 9 - width / 2, z - .15), (width, width, length + .3), 0 if i < 2 else 1)
            r.mass(tail, "tail_joint", (0, 9, z), (width * 1.1, width * 1.1, width * 1.1), 0)
            if i == 3:
                r.box(tail, "tail_barb", (-.7, 8.3, z + length - 1), (1.4, 1.4, 1.8), 4)
            z += length
            parent = name
    else:
        # A lean aerial predator: vaulted chest, swept skull and tucked talons.
        # Independent shoulder, elbow and wrist spars replace the flat wing board.
        body = r.bone("body", (0, 14, 1))
        r.mass(body, "pelvis", (0, 13.8, 1), (7.4, 5.2, 5.4), 2)
        chest = r.bone("chest", (0, 18, .5), "body", [12, 0, 0])
        r.mass(chest, "upper_thorax", (0, 24.2, .5), (12, 6, 7), 0)
        r.mass(chest, "abdomen", (0, 16.6, .5), (6.8, 4.2, 5), 1)
        for s in [-1, 1]:
            r.mass(chest, f"ribs_{s}", (s * 3.6, 20.2, .5), (4, 5.2, 6.2), 0)
            r.box(chest, f"collar_{s}", (s * 3 - 2.2, 24.5, -3.5), (4.4, 1.1, 1.5), 4,
                  [0, 0, s * 18], [s * 3, 25, -2.5])
            r.box(chest, f"hole_side_{s}", (s * 1.6 - .2, 18.3, -2.9), (.4, 3.4, .6), 4)
        for y in [18.1, 21.7]:
            r.box(chest, "hole_rim", (-1.8, y, -2.9), (3.6, .45, .6), 4)
        for i in range(3):
            r.box(chest, f"back_crest_{i}", (-.5, 20 + i * 2, 3.3), (1, 1.8, 2.7), 4,
                  [-28, 0, 0], [0, 20 + i * 2, 3.3])
        head = r.bone("head", (0, 27, -1.5), "chest", [-12, 0, 0])
        r.mass(head, "mask_crown", (0, 29.3, -3.6), (6.7, 6.6, 5.5), 3)
        r.box(head, "central_mask_keel", (-.65, 25.8, -7.3), (1.3, 6.3, 2.1), 3,
              [10, 0, 0], [0, 29, -6.2])
        r.box(head, "beak_tip", (-.4, 25.1, -8.2), (.8, 2.8, 1.4), 4,
              [-20, 0, 0], [0, 27, -7.1])
        for s in [-1, 1]:
            r.box(head, f"socket_{s}", (s * 1.85 - 1.05, 28.4, -6.7), (2.1, 1.9, .7), 5,
                  [0, 0, -s * 22], [s * 1.85, 29.5, -6.3])
            r.box(head, f"eye_{s}", (s * 1.85 - .5, 29.1, -7), (1, .45, .4), 6)
            r.box(head, f"cheek_blade_{s}", (s * 2.6 - .6, 25.4, -6.4), (1.2, 5, 1.9), 4,
                  [0, 0, s * 15], [s * 2.6, 28.5, -5.5])
            r.box(head, f"swept_horn_{s}", (s * 2.3 - .5, 31, -3.6), (1, 3.5, 1.2), 3,
                  [-40, 0, -s * 20], [s * 2.3, 31, -3])
        jaw = r.bone("jaw", (0, 26.2, -3.5), "head")
        r.box(jaw, "mouth", (-1.7, 24.6, -6.4), (3.4, 1.5, 2), 5)
        r.box(jaw, "jawbone", (-1.9, 24.2, -6.5), (3.8, .6, 3), 3)
        for i in range(4):
            r.box(jaw, f"fang_{i}", (-1.5 + i * .85, 25.1, -6.8), (.5, 1.1, .5), 3)
        for s, side in [(-1, "right"), (1, "left")]:
            x = s * 6
            arm = r.bone(side + "_arm", (x, 24, -.3), "chest", [-12, 0, s * 9])
            r.mass(arm, "deltoid", (x, 23, -.3), (4.4, 5, 4.2), 1)
            r.mass(arm, "upper_arm", (x, 20.5, -.3), (2.6, 6, 3), 0)
            lower = r.bone(side + "_arm_lower", (x, 18, -.3), side + "_arm", [16, 0, 0])
            r.mass(lower, "forearm", (x, 15.2, -.3), (3.3, 6.5, 3.5), 1)
            hand = r.bone(side + "_hand", (x, 12.2, -.3), side + "_arm_lower", [-12, 0, 0])
            r.mass(hand, "hand", (x, 11.5, -.6), (3.8, 2.6, 3.4), 2)
            for i in range(3):
                fx = x - 1.2 + i * 1.1
                r.box(hand, f"claw_{i}", (fx - .3, 9.2, -1.6), (.6, 2.3, .9), 3,
                      [18, 0, 0], [fx, 11.4, -1.1])
            leg = r.bone(side + "_leg", (s * 2.6, 14.8, 1), "body", [-12, 0, s * 4])
            r.mass(leg, "haunch", (s * 2.6, 12.5, 1), (4, 6, 4.8), 0)
            shin = r.bone(side + "_leg_lower", (s * 2.6, 9.8, 1), side + "_leg", [22, 0, 0])
            r.mass(shin, "shin", (s * 2.6, 6.8, 1), (2.4, 7, 3), 1)
            foot = r.bone(side + "_foot", (s * 2.6, 3, 1), side + "_leg_lower", [-10, 0, 0])
            r.mass(foot, "talon_pad", (s * 2.6, 2, -.1), (3.7, 2.5, 4), 2)
            for i in range(3):
                r.box(foot, f"talon_{i}", (s * 2.6 - 1.5 + i * 1.2, .7, -3.6), (.7, 1.5, 3), 3,
                      [-12, 0, 0], [s * 2.6, 2, -1.2])
            wing = r.bone(side + "_wing", (s * 4, 24, 2), "chest")
            outer = r.bone(side + "_wing_outer", (s * 11.5, 26, 2), side + "_wing")
            tip = r.bone(side + "_wing_tip", (s * 20, 26, 2), side + "_wing_outer")
            for i, (cx, low) in enumerate([(6.8, 19), (10.5, 16.5), (14.5, 18), (18.5, 20), (22.5, 22), (26, 24)]):
                x = s * cx
                owner = wing if i < 2 else outer if i < 4 else tip
                height = 27 - low
                r.box(owner, f"web_{i}", (x - 2.4, low, 2.5), (4.8, height, .6), 7)
                r.box(owner, f"finger_spar_{i}", (x - .3, low - .5, 2.2), (.6, height + .7, 1), 4)
                r.box(owner, f"web_tension_{i}", (x - 1.6, low + .8, 2.1), (.4, height * .75, .4), 1,
                      [0, 0, s * 12], [x, 26, 2.4])
            for owner, a, b in [(wing, 4, 11.5), (outer, 11.5, 20), (tip, 20, 27)]:
                r.box(owner, "leading_spar", (min(s * a, s * b) - .3, 25.8, 1.8), (b - a + .6, 1.2, 1.6), 0)
                r.mass(owner, "wing_joint", (s * a, 26, 2.5), (2.2, 2.2, 2.2), 4)
            r.box(tip, "tip_hook", (s * 27 - .4, 23.5, 1.8), (.8, 4, 1.2), 3,
                  [0, 0, -s * 18], [s * 27, 26, 2.4])
        tail = r.bone("tail", (0, 14, 2.5), "body", [18, 0, 0])
        r.box(tail, "tail_base", (-.8, 13.2, 2.3), (1.6, 1.6, 4.5), 0)
        end = r.bone("tail_tip", (0, 14, 6.5), "tail", [12, 0, 0])
        for s in [-1, 1]:
            r.box(end, "forked_streamer", (s * .65 - .3, 13.6, 6.3), (.6, .8, 5), 1,
                  [0, s * 12, 0], [s * .65, 14, 6.5])
    return r


def animations():
    def sine(speed, amplitude, phase=0, offset=0):
        return f"{offset} + math.sin(query.anim_time * {speed} + {phase}) * {amplitude}"

    def keys(times, poses):
        return {str(t): {"post": pose, "lerp_mode": "catmullrom"} for t, pose in zip(times, poses)}

    anims = {}
    def add(variant, action, duration, bones, loop=True):
        anims[f"animation.hollow.{variant}.{action}"] = {"loop": loop, "animation_length": duration, "bones": bones}

    for variant, speed, breath in [("brute", 100, .23), ("swift", 170, .12), ("flying", 135, .16)]:
        idle = {"body": {"position": [0, sine(speed, breath), 0], "rotation": [0, sine(speed, .8), sine(speed, .65, 40)]},
                "head": {"rotation": [sine(speed, 1.6, -35), sine(speed, 1, -50), 0]},
                "jaw": {"rotation": [sine(speed, 1.5, -20, 1.5), 0, 0]}}
        for side, sign in [("left", 1), ("right", -1)]:
            idle[side + "_arm"] = {"rotation": [sine(speed, 1.6, -25), 0, sine(speed, .8, -40) if sign > 0 else sine(speed, -.8, -40)]}
            idle[side + "_arm_lower"] = {"rotation": [sine(speed, 2, -55), 0, 0]}
        if variant == "brute":
            idle["chest"] = {"rotation": [sine(speed, 1.1), 0, 0], "scale": [sine(speed, .012, 0, 1), sine(speed, .007, 0, 1), sine(speed, .012, 0, 1)]}
            for side in ["left", "right"]:
                idle[side + "_hand"] = {"rotation": [sine(speed, 2, -70), 0, 0]}
        if variant == "swift":
            for i, bone in enumerate(["tail", "tail_1", "tail_2", "tail_tip"]):
                idle[bone] = {"rotation": [sine(speed, 2, -i * 30), sine(speed, 5, -i * 30), 0]}
        if variant == "flying":
            idle["chest"] = {"rotation": [sine(speed, 1.2), 0, 0]}
            idle["tail"] = {"rotation": [sine(speed, 3, -30), sine(speed, 5, -35), 0]}
            idle["tail_tip"] = {"rotation": [sine(speed, 4, -55), sine(speed, 7, -60), 0]}
        add(variant, "idle", 3.6 if variant == "brute" else 360 / speed, idle)

    # A low, rolling beast gait: hip shift precedes the planted leg, shoulder mass
    # counter-rotates, and elbows/wrists trail rather than swinging as rigid rods.
    speed = 288  # 1.25s cycle, slower than the agile variant.
    walk = {
        "body": {"position": [sine(speed, .7, 90), "-0.1 - math.abs(math.sin(query.anim_time * 288)) * 0.55", 0],
                 "rotation": [sine(speed * 2, 1.5), sine(speed, 3.5), sine(speed, 3.3, 90)]},
        "chest": {"rotation": [sine(speed * 2, 2.1, -25), sine(speed, -6, -20), sine(speed, -2.5, 60)]},
        "head": {"rotation": [sine(speed * 2, -2, -35), sine(speed, 2, 20), sine(speed, -1, 65)]},
        "jaw": {"rotation": [sine(speed * 2, 1.5, -50, 1.5), 0, 0]}}
    for side, phase, sign in [("left", 0, 1), ("right", 180, -1)]:
        walk[side + "_leg"] = {"rotation": [sine(speed, 19, phase), sine(speed, 2, phase), sign * 1.5]}
        walk[side + "_leg_lower"] = {"rotation": [f"math.max(0, math.sin(query.anim_time * {speed} + {phase + 35})) * 22 - 5", 0, 0]}
        walk[side + "_foot"] = {"rotation": [sine(speed, -13, phase + 15), 0, 0]}
        walk[side + "_arm"] = {"rotation": [sine(speed, -10, phase - 22), sine(speed, 4, phase), sine(speed, sign * 2, phase - 30)]}
        walk[side + "_arm_lower"] = {"rotation": [sine(speed, 7, phase - 58), 0, 0]}
        walk[side + "_hand"] = {"rotation": [sine(speed, -6, phase - 85), 0, sine(speed, 3, phase - 60)]}
        for i in range(3):
            walk[f"{side}_finger_{i}"] = {"rotation": [sine(speed, 3, phase - 90 - i * 8), 0, 0]}
    add("brute", "walk", 1.25, walk)

    swift = {"body": {"position": [0, sine(1200, .18), 0], "rotation": [sine(1200, 2), sine(600, 3), sine(600, 2)]},
             "head": {"rotation": [sine(1200, -2, -30), sine(600, -2), 0]}}
    for side, phase in [("left", 0), ("right", 180)]:
        swift[side + "_leg"] = {"rotation": [sine(600, 30, phase), 0, 0]}
        swift[side + "_leg_lower"] = {"rotation": [f"math.max(0, math.sin(query.anim_time * 600 + {phase + 30})) * 34 - 5", 0, 0]}
        swift[side + "_arm"] = {"rotation": [sine(600, -22, phase - 15), 0, sine(600, 3, phase)]}
        swift[side + "_arm_lower"] = {"rotation": [sine(600, 15, phase - 45, 12), 0, 0]}
    for i, bone in enumerate(["tail", "tail_1", "tail_2", "tail_tip"]):
        swift[bone] = {"rotation": [sine(600, 3, -i * 24), sine(300, 8, -i * 28), sine(300, 2, -i * 28)]}
    add("swift", "walk", 1.2, swift)

    # Flight stroke: elbow folds late on recovery; head stabilises while body,
    # arms and dangling calves follow at different phases.
    def wingbeat(speed, effort, pitch):
        flight = {"body": {"position": [0, sine(speed, .55, -35), 0], "rotation": [sine(speed, 2.5, -20, pitch), sine(speed / 2, 1), sine(speed / 2, 1.5)]},
                  "chest": {"rotation": [sine(speed, -1.8, -30), 0, sine(speed / 2, -1.5)]},
                  "head": {"rotation": [sine(speed, -2.5, -35, -pitch * .6), sine(speed / 2, -1), 0]},
                  "jaw": {"rotation": [sine(speed, 1.6, -50, 2), 0, 0]},
                  "tail": {"rotation": [sine(speed, 3, -55), sine(speed / 2, 5, -50), 0]},
                  "tail_tip": {"rotation": [sine(speed, 5, -80), sine(speed / 2, 7, -80), 0]}}
        for side, sign in [("left", 1), ("right", -1)]:
            flight[side + "_wing"] = {"rotation": [sine(speed, 6, -15), sine(speed, sign * 6, -20), sine(speed, sign * effort, 0, sign * 2)]}
            flight[side + "_wing_outer"] = {"rotation": [sine(speed, 4, -45), sine(speed, sign * 8, -50), sine(speed, sign * 12, -55, sign * 4)]}
            flight[side + "_wing_tip"] = {"rotation": [sine(speed, 4, -65), sine(speed, sign * 8, -70), sine(speed, sign * 14, -80, sign * 3)]}
            flight[side + "_leg"] = {"rotation": [sine(speed, 4, -65, 17), 0, sign * -5]}
            flight[side + "_leg_lower"] = {"rotation": [sine(speed, 7, -100, -18), 0, 0]}
            flight[side + "_foot"] = {"rotation": [sine(speed, 6, -120, 4), 0, 0]}
            flight[side + "_arm"] = {"rotation": [sine(speed, 4, -65, -12), sine(speed / 2, 2), sign * -8]}
            flight[side + "_arm_lower"] = {"rotation": [sine(speed, 7, -100, -5), 0, 0]}
            flight[side + "_hand"] = {"rotation": [sine(speed, 5, -120), 0, 0]}
        return flight
    flight = wingbeat(450, 31, 5)
    add("flying", "flight", 1.6, flight)
    add("flying", "walk", 1.6, flight)
    add("flying", "retreat", 1.2, wingbeat(600, 43, -8))
    dive = {"body": {"rotation": [sine(360, 2, 0, 20), 0, sine(360, 2)]},
            "chest": {"rotation": [4, 0, 0]}, "head": {"rotation": [-15, 0, 0]},
            "tail": {"rotation": [sine(360, 2, -25), sine(360, 3, -20), 0]},
            "tail_tip": {"rotation": [sine(360, 3, -55), sine(360, 5, -50), 0]}}
    for side, sign in [("left", 1), ("right", -1)]:
        dive[side + "_wing"] = {"rotation": [3, sign * 15, sine(360, sign * 4, 0, sign * 8)]}
        dive[side + "_wing_outer"] = {"rotation": [0, sign * 24, sign * 18]}
        dive[side + "_wing_tip"] = {"rotation": [0, sign * 20, sign * 16]}
        dive[side + "_arm"] = {"rotation": [-32, 0, sign * -10]}
        dive[side + "_arm_lower"] = {"rotation": [6, 0, 0]}
        dive[side + "_leg"] = {"rotation": [26, 0, sign * -6]}
        dive[side + "_leg_lower"] = {"rotation": [-28, 0, 0]}
        dive[side + "_foot"] = {"rotation": [12, 0, 0]}
    add("flying", "dive", 1, dive)

    # Contact frames match the server windups: brute 8 ticks, swift 3, flying 5.
    t = [0, .24, .4, .53, .72, .95]
    brute = {
        "body": {"rotation": keys(t, [[0, 0, 0], [-3, -8, -2], [5, 9, 3], [3, 6, 1], [-1, -2, 0], [0, 0, 0]])},
        "chest": {"rotation": keys(t, [[0, 0, 0], [-8, -17, -5], [12, 20, 6], [9, 13, 4], [-2, -4, -1], [0, 0, 0]])},
        "head": {"rotation": keys(t, [[0, 0, 0], [-6, 13, 0], [8, -14, -3], [5, -8, 0], [-2, 2, 0], [0, 0, 0]])},
        "right_arm": {"rotation": keys(t, [[0, 0, 0], [-102, -25, -20], [-65, 24, 22], [-15, 35, 27], [12, -3, -4], [0, 0, 0]])},
        "right_arm_lower": {"rotation": keys(t, [[0, 0, 0], [35, -5, 0], [-20, 0, 0], [-14, 0, 0], [9, 0, 0], [0, 0, 0]])},
        "right_hand": {"rotation": keys(t, [[0, 0, 0], [-14, 0, 0], [18, 0, 0], [26, 0, 0], [-6, 0, 0], [0, 0, 0]])},
        "left_arm": {"rotation": keys(t, [[0, 0, 0], [-17, 14, 12], [15, -8, -9], [10, -4, -5], [-4, 0, 0], [0, 0, 0]])},
        "left_leg_lower": {"rotation": keys(t, [[0, 0, 0], [8, 0, 0], [-5, 0, 0], [-3, 0, 0], [2, 0, 0], [0, 0, 0]])},
        "jaw": {"rotation": keys(t, [[0, 0, 0], [6, 0, 0], [24, 0, 0], [17, 0, 0], [3, 0, 0], [0, 0, 0]])}}
    add("brute", "attack", .95, brute, False)
    t = [0, .08, .15, .23, .34, .45]
    swift_attack = {
        "body": {"rotation": keys(t, [[0, 0, 0], [-6, -13, -3], [11, 15, 4], [5, 8, 2], [-3, -3, 0], [0, 0, 0]])},
        "right_arm": {"rotation": keys(t, [[0, 0, 0], [-95, -25, -18], [-50, 30, 30], [5, 25, 12], [12, -5, 0], [0, 0, 0]])},
        "right_arm_lower": {"rotation": keys(t, [[0, 0, 0], [30, 0, 0], [-18, 0, 0], [-7, 0, 0], [6, 0, 0], [0, 0, 0]])},
        "head": {"rotation": keys(t, [[0, 0, 0], [-9, 10, 0], [8, -13, 0], [4, -6, 0], [-3, 2, 0], [0, 0, 0]])}}
    add("swift", "attack", .45, swift_attack, False)
    t = [0, .15, .25, .35, .48, .65]
    flying_attack = {"body": {"rotation": keys(t, [[0, 0, 0], [-9, 0, 0], [18, 0, 0], [10, 0, 0], [-3, 0, 0], [0, 0, 0]])},
                     "chest": {"rotation": keys(t, [[0, 0, 0], [-5, 0, 0], [8, 0, 0], [4, 0, 0], [-2, 0, 0], [0, 0, 0]])},
                     "head": {"rotation": keys(t, [[0, 0, 0], [-5, 0, 0], [7, 0, 0], [4, 0, 0], [-2, 0, 0], [0, 0, 0]])},
                     "jaw": {"rotation": keys(t, [[0, 0, 0], [6, 0, 0], [20, 0, 0], [11, 0, 0], [3, 0, 0], [0, 0, 0]])}}
    for side, sign in [("left", 1), ("right", -1)]:
        flying_attack[side + "_arm"] = {"rotation": keys(t, [[0, 0, 0], [-70, sign * 10, sign * -14], [-40, sign * -12, sign * 8], [15, 0, sign * 6], [8, 0, 0], [0, 0, 0]])}
        flying_attack[side + "_arm_lower"] = {"rotation": keys(t, [[0, 0, 0], [25, 0, 0], [-12, 0, 0], [-8, 0, 0], [4, 0, 0], [0, 0, 0]])}
        flying_attack[side + "_hand"] = {"rotation": keys(t, [[0, 0, 0], [-8, 0, 0], [16, 0, 0], [8, 0, 0], [-3, 0, 0], [0, 0, 0]])}
        flying_attack[side + "_leg"] = {"rotation": keys(t, [[0, 0, 0], [9, 0, 0], [-12, 0, 0], [-5, 0, 0], [4, 0, 0], [0, 0, 0]])}
        flying_attack[side + "_foot"] = {"rotation": keys(t, [[0, 0, 0], [-6, 0, 0], [12, 0, 0], [6, 0, 0], [-2, 0, 0], [0, 0, 0]])}
    add("flying", "attack", .65, flying_attack, False)
    return {"format_version": "1.8.0", "animations": anims}


def bbmodel(rig, png):
    groups = {}
    for b in rig.bones:
        groups[b["name"]] = {"name": b["name"], "uuid": uid(rig.variant + b["name"]), "origin": b["pivot"],
                             "rotation": b.get("rotation", [0, 0, 0]), "export": True, "visibility": True, "children": []}
    elements = []
    for index, (parent, name, c, color) in enumerate(rig.parts):
        cube_id = uid(f"{rig.variant}:{index}:{name}")
        elements.append({"type": "cube", "uuid": cube_id, "name": name, "from": c["origin"],
                         "to": [round(a + b, 5) for a, b in zip(c["origin"], c["size"])],
                         "origin": c.get("pivot", groups[parent]["origin"]), "rotation": c.get("rotation", [0, 0, 0]),
                         "box_uv": False, "rescale": False, "export": True, "visibility": True,
                         "faces": {f: {"uv": [color * 16 + 1, 1, color * 16 + 13, 13], "texture": 0} for f in FACES}})
        groups[parent]["children"].append(cube_id)
    for b in rig.bones:
        if "parent" in b:
            groups[b["parent"]]["children"].append(groups[b["name"]])
    return {"meta": {"format_version": "4.10", "model_format": "bedrock", "box_uv": False},
            "name": "hollow_" + rig.variant, "model_identifier": "geometry.bleachmod.hollow_" + rig.variant,
            "resolution": {"width": 128, "height": 32}, "elements": elements, "outliner": [groups["root"]],
            "textures": [{"name": "hollow_" + rig.variant + ".png", "id": "0", "uuid": uid(rig.variant + "texture"),
                          "source": "data:image/png;base64," + base64.b64encode(png).decode(), "width": 128, "height": 32}],
            "animations": []}


def files():
    result = {}
    def js(path, data):
        result[path] = (json.dumps(data, indent=2) + "\n").encode()
    for variant in PALETTES:
        rig = build_rig(variant)
        png = texture(PALETTES[variant])
        result[ASSETS / f"textures/entity/hollow_{variant}.png"] = png
        js(ASSETS / f"geo/hollow_{variant}.geo.json", {"format_version": "1.12.0", "minecraft:geometry": [{
            "description": {"identifier": "geometry.bleachmod.hollow_" + variant, "texture_width": 128, "texture_height": 32,
                            "visible_bounds_width": 8, "visible_bounds_height": 6, "visible_bounds_offset": [0, 2, 0]}, "bones": rig.bones}]})
        js(ROOT / f"tools/models/source/hollow_{variant}.bbmodel", bbmodel(rig, png))
    js(ASSETS / "animations/hollow.animation.json", animations())
    return result


def main():
    check = argparse.ArgumentParser()
    check.add_argument("--check", action="store_true")
    args = check.parse_args()
    for path, data in files().items():
        if args.check:
            assert path.read_bytes() == data, f"Outdated: {path}"
        else:
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(data)
    # Structural contract: unique bones, valid parent links and UV rectangles in bounds.
    for variant in PALETTES:
        rig = build_rig(variant)
        names = {b["name"] for b in rig.bones}
        assert len(names) == len(rig.bones)
        assert all(b.get("parent", "root") in names for b in rig.bones)
        for b in rig.bones:
            for c in b["cubes"]:
                assert all(s > 0 for s in c["size"])
                assert all(0 <= f["uv"][0] < f["uv"][0] + f["uv_size"][0] <= 128 for f in c["uv"].values())
        print(f"{variant}: {len(rig.bones)} bones, {len(rig.parts)} cubes; assets {'verified' if args.check else 'written'}")
    validate_motion()


def validate_motion():
    """Cross-check authored channels against the actual rigs, and catch loop/tail seams."""
    molang_math = SimpleNamespace(sin=lambda degrees: math.sin(math.radians(degrees)), abs=abs, max=max)
    def value(expression, t):
        if not isinstance(expression, str):
            return expression
        # Expressions originate exclusively in animations() above, never in input files.
        return eval(expression, {"__builtins__": {}}, {"math": molang_math, "query": SimpleNamespace(anim_time=t)})

    for name, clip in animations()["animations"].items():
        variant = name.split(".")[2]
        available = {bone["name"] for bone in build_rig(variant).bones}
        assert set(clip["bones"]).issubset(available), f"Missing animated bones: {name}"
        for bone, channels in clip["bones"].items():
            for channel, track in channels.items():
                if isinstance(track, list):
                    samples = [value(v, t) for t in [0, clip["animation_length"]] for v in track]
                    assert all(math.isfinite(v) for v in samples)
                    if clip["loop"]:
                        assert all(abs(value(v, 0) - value(v, clip["animation_length"])) < 1e-5 for v in track), f"Loop pop: {name}/{bone}/{channel}"
                else:
                    times = [float(t) for t in track]
                    assert min(times) == 0 and max(times) == clip["animation_length"]
                    assert track[str(min(times)) if str(min(times)) in track else "0"]["post"] == track[str(max(times))]["post"], f"Attack does not recover: {name}/{bone}"

    swift = build_rig("swift")
    bones = {b["name"]: b for b in swift.bones}
    for parent, child in zip(["tail", "tail_1", "tail_2"], ["tail_1", "tail_2", "tail_tip"]):
        segment = next(c for owner, name, c, _ in swift.parts if owner == parent and name == "tail_segment")
        end_z = segment["origin"][2] + segment["size"][2] - .15
        assert abs(end_z - bones[child]["pivot"][2]) < 1e-6, "Tail endpoint/pivot detached"
        assert bones[child]["parent"] == parent and "rotation" not in segment, "Tail curve must propagate through the bone chain"
    print(f"Motion: {len(animations()['animations'])} clips, loop seams, recovery poses, rig references and continuous tail joints verified")


if __name__ == "__main__":
    main()
