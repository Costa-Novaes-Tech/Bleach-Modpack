#!/usr/bin/env python3
"""
Builds the redesigned Shukei: Hakuteiken v2 Blockbench model (ag_senbon_hakuteiken_v2.bbmodel):
- True Angelic Wings (Asas de Anjo) with high scapular arch, curved carpal bend,
  3 overlapping rows of curved plumage (upper coverts, middle secondaries, outer primaries,
  and inner trailing feathers), and 3D floating sakura petals detaching from the feathers.
- Removes the Vollstandig-like radial starburst halo and removes the duplicate construct sword bone.
- Includes the Shinigami Captain (Byakuya Kuchiki) preview mannequin holding the existing
  in-game Zanpakuto reference (senbonzakura_hakuteiken.json window-guard katana) in right_arm
  (filtered out automatically on GeckoLib export so only the 11 Angel Wing + Petal bones export).
"""
import base64
import io
import json
import math
import uuid
from pathlib import Path
from PIL import Image, ImageDraw

HERE = Path(__file__).resolve().parent
BBMODEL_PATH = HERE / "ag_senbon_hakuteiken_v2.bbmodel"


def make_uuid(seed_str: str) -> str:
    return str(uuid.uuid5(uuid.NAMESPACE_DNS, f"bleachmod.hakuteiken.angel.v3.{seed_str}"))


def generate_angel_wing_texture() -> str:
    """Generates a 128x256 RGBA texture with soft sakura-white angel feather gradients,
    delicate feather rachis/barbs, glowing sakura petals, and Byakuya preview mannequin regions."""
    w, h = 128, 256
    img = Image.new("RGBA", (w, h), (0, 0, 0, 0))
    px = img.load()

    # 1. Top half (y=0..127): Angelic Feather & Sakura Petal Atlas
    # Region A (x=0..63, y=0..63): Pure Imperial White & Soft Sakura Tip Primary Feathers
    for y in range(0, 64):
        t = y / 63.0
        for x in range(0, 64):
            u = x / 63.0
            # Soft feather barb herringbone pattern toward center shaft
            barb = math.sin((u * 8.0 + t * 14.0) * math.pi) * 0.04
            # Gradient from pure glowing white at base/shaft to soft sakura blush at feather tip
            blush = max(0.0, (t - 0.35) / 0.65) + barb
            blush = max(0.0, min(1.0, blush))
            r = int(255)
            g = int(255 - 42 * blush)
            b = int(255 - 24 * blush)
            px[x, y] = (r, g, b, 250)

    # Region B (x=64..127, y=0..63): Sakura-Pink Coverts & Inner Angel Plumage (#ffd1e3 -> #ffffff)
    for y in range(0, 64):
        t = y / 63.0
        for x in range(64, 128):
            u = (x - 64) / 63.0
            wave = math.cos((u * 6.0 - t * 10.0) * math.pi) * 0.05
            s = max(0.0, min(1.0, 0.75 - 0.55 * t + wave))
            r = 255
            g = int(255 - 58 * s)
            b = int(255 - 32 * s)
            px[x, y] = (r, g, b, 248)

    # Region C (x=0..63, y=64..127): Individual Glowing Sakura Petals (#ff7aa8 -> #ffd0e0)
    for y in range(64, 128):
        t = (y - 64) / 63.0
        for x in range(0, 64):
            u = x / 63.0
            dist = math.hypot(u - 0.5, t - 0.5) * 1.4
            s = max(0.0, min(1.0, 1.0 - dist * 0.7))
            r = 255
            g = int(130 + 85 * (1.0 - s))
            b = int(175 + 58 * (1.0 - s))
            px[x, y] = (r, g, b, 252)

    # Region D (x=64..127, y=64..127): Luminous Reishi Core & Scapular Mantle (#ffffff -> #ffe6f0)
    for y in range(64, 128):
        t = (y - 64) / 63.0
        for x in range(64, 128):
            r = 255
            g = int(242 + 13 * (1.0 - t))
            b = int(248 + 7 * (1.0 - t))
            px[x, y] = (r, g, b, 252)

    # 2. Bottom half (y=128..255): Captain Byakuya Kuchiki Preview Mannequin + Existing Zanpakuto Reference
    draw = ImageDraw.Draw(img)
    # Skin tone (x=0..31, y=128..159)
    draw.rectangle([0, 128, 31, 159], fill=(235, 198, 172, 255))
    # Dark hair & Byakuya eyes (x=32..63, y=128..159)
    draw.rectangle([32, 128, 63, 159], fill=(26, 22, 34, 255))
    # White Kenseikan hairpieces & Captain Haori (x=64..95, y=128..159)
    draw.rectangle([64, 128, 95, 159], fill=(246, 246, 250, 255))
    # Seafoam/Silver Scarf Ginpakukazahana no Uso (x=96..127, y=128..159)
    draw.rectangle([96, 128, 127, 159], fill=(216, 232, 230, 255))
    # Black Shihakusho robes (x=0..63, y=160..223)
    draw.rectangle([0, 160, 63, 223], fill=(20, 20, 26, 255))
    # Haori black diamond hem pattern (x=64..127, y=160..191)
    draw.rectangle([64, 160, 127, 191], fill=(242, 242, 247, 255))
    for dx in range(66, 124, 8):
        draw.polygon([(dx, 184), (dx + 3, 179), (dx + 6, 184), (dx + 3, 189)], fill=(25, 25, 32, 255))
    # Existing Senbonzakura Zanpakuto reference colors (x=64..127, y=192..255)
    # White Hakuteiken quartz blade & tsuba (x=64..95, y=192..255)
    draw.rectangle([64, 192, 95, 255], fill=(252, 248, 254, 255))
    # Classic Senbonzakura gold tsuba & lavender/magenta tsuka wrap (x=96..127, y=192..255)
    draw.rectangle([96, 192, 127, 223], fill=(218, 178, 72, 255))
    draw.rectangle([96, 224, 127, 255], fill=(168, 74, 140, 255))

    buf = io.BytesIO()
    img.save(buf, format="PNG")
    b64 = base64.b64encode(buf.getvalue()).decode("ascii")
    return f"data:image/png;base64,{b64}"


def make_cube(name: str, frm, to, origin, rotation, uv_box):
    """Creates a Blockbench cube with explicit per-face UVs inside uv_box = (u0, v0, u1, v1)."""
    u0, v0, u1, v1 = uv_box
    uid = make_uuid(f"cube.{name}.{frm}.{to}.{origin}.{rotation}")
    faces = {}
    for face in ("north", "east", "south", "west", "up", "down"):
        faces[face] = {"uv": [u0, v0, u1, v1], "texture": 0}
    return {
        "name": name,
        "box_uv": False,
        "rescale": False,
        "locked": False,
        "render_order": "default",
        "allow_mirror_modeling": True,
        "from": [round(float(x), 3) for x in frm],
        "to": [round(float(x), 3) for x in to],
        "autouv": 0,
        "color": 0,
        "origin": [round(float(x), 3) for x in origin],
        "rotation": [round(float(x), 3) for x in rotation],
        "faces": faces,
        "type": "cube",
        "uuid": uid
    }


def build_elements_and_outliner():
    elements = []
    groups = {}

    def add_to_group(g_name, cube):
        elements.append(cube)
        groups.setdefault(g_name, []).append(cube["uuid"])

    # UV Regions on 128x256 texture
    UV_PRIMARY = [4, 4, 60, 60]       # White -> soft pink tip feather
    UV_COVERT = [68, 4, 124, 60]      # Sakura pink -> white covert feather
    UV_PETAL = [8, 72, 56, 120]       # Glowing sakura petal
    UV_CORE = [72, 72, 120, 120]      # Pure luminous white-pink core
    UV_SKIN = [4, 132, 28, 156]
    UV_HAIR = [36, 132, 60, 156]
    UV_HAORI = [68, 132, 92, 156]
    UV_SCARF = [100, 132, 124, 156]
    UV_ROBE = [8, 168, 56, 216]
    UV_HEM = [68, 164, 124, 188]
    UV_ZAN_WHITE = [68, 196, 92, 250]

    # =========================================================================
    # 1. DORSAL SAKURA MANTLE / NEXUS (wing_nexus)
    # Embedded directly into the Captain's shoulder blades (z = 1.1..2.65, overlapping
    # torso back at z = 2.0 and Haori at z = 2.35) so there is ZERO air gap!
    # =========================================================================
    add_to_group("wing_nexus", make_cube(
        "nexus_core_crest", [-2.6, 17.5, 1.1], [2.6, 23.8, 2.65],
        [0, 21.5, 1.85], [2, 0, 0], UV_CORE
    ))
    add_to_group("wing_nexus", make_cube(
        "nexus_left_calyx", [-4.4, 18.2, 1.25], [-0.8, 23.9, 2.75],
        [-2.2, 21.5, 1.95], [2, 8, -14], UV_COVERT
    ))
    add_to_group("wing_nexus", make_cube(
        "nexus_right_calyx", [0.8, 18.2, 1.25], [4.4, 23.9, 2.75],
        [2.2, 21.5, 1.95], [2, -8, 14], UV_COVERT
    ))
    add_to_group("wing_nexus", make_cube(
        "nexus_upper_arch_l", [-3.8, 22.2, 1.35], [-0.4, 25.8, 2.65],
        [-2.0, 23.2, 1.95], [-4, 6, -24], UV_COVERT
    ))
    add_to_group("wing_nexus", make_cube(
        "nexus_upper_arch_r", [0.4, 22.2, 1.35], [3.8, 25.8, 2.65],
        [2.0, 23.2, 1.95], [-4, -6, 24], UV_COVERT
    ))

    # =========================================================================
    # 2. ANGEL WINGS (Symmetric Left [-X] and Right [+X])
    # Physically fused to the back of the torso (z = 1.95..2.85):
    # - Starts at x = ±2.2, y = 21.8, z = 1.95 (embedded inside the shoulder blades!)
    # - High angelic arch rises to y = 32.6, curving gently to z = 2.80 at the wrist
    # - Feathers hang flush with the back (rx = 0..1.5 deg) so they never detach!
    # =========================================================================
    def arch_curve(u: float):
        """Parametric upper wing arch for u in [0..1] (x_abs from 2.2 to 22.2):
        Starts embedded in the shoulder blade (x=2.2, y=21.8, z=1.95), rises to
        the high angelic crest (x=12.8, y=32.6, z=2.38), and glides to the wrist (x=22.2, y=29.8, z=2.80)."""
        x_abs = 2.2 + 20.0 * u
        y = 21.8 + 10.8 * math.sin(u * math.pi * 0.68) + 1.2 * (u ** 2)
        z = 1.95 + 0.85 * u
        return x_abs, y, z

    for side_name, s in (("left", -1.0), ("right", 1.0)):
        b_root = f"wing_{side_name}_root"
        b_mid = f"wing_{side_name}_mid"
        b_tip = f"wing_{side_name}_tip"
        b_feathers = f"wing_{side_name}_feathers"

        def box_top_pivot(px, py, pz, wx, wy, wz):
            """Creates a feather box whose top-center shaft is tucked at (px, py, pz) and hangs down by wy."""
            return [px - wx / 2.0, py - wy, pz - wz / 2.0], [px + wx / 2.0, py, pz + wz / 2.0]

        # --- A. SCAPULAR SHOULDER & RISING ARCH (wing_*_root, pivot [±2.2, 21.5, 1.95]) ---
        root_arch_steps = [
            ("arch_seg_1", 0.00, 0.17, 1.70, 1.55),
            ("arch_seg_2", 0.15, 0.33, 1.62, 1.48),
            ("arch_seg_3", 0.31, 0.48, 1.55, 1.42),
        ]
        for lbl, u0, u1, thick_y, thick_z in root_arch_steps:
            x0, y0, z0 = arch_curve(u0)
            x1, y1, z1 = arch_curve(u1)
            cx, cy, cz = 0.5 * (x0 + x1) * s, 0.5 * (y0 + y1), 0.5 * (z0 + z1)
            dx, dy = (x1 - x0), (y1 - y0)
            seg_len = math.hypot(dx, dy) + 0.95
            tilt_deg = math.degrees(math.atan2(dy, dx))
            frm = [cx - seg_len / 2.0, cy - thick_y / 2.0, cz - thick_z / 2.0]
            to = [cx + seg_len / 2.0, cy + thick_y / 2.0, cz + thick_z / 2.0]
            add_to_group(b_root, make_cube(
                f"{b_root}_{lbl}", frm, to, [cx, cy, cz], [-1.5, -3.5 * s, +tilt_deg * s], UV_CORE
            ))

        # 6 Upper Sakura Coverts along the rising scapular arch (u = 0.03..0.45), flush with back
        for i in range(6):
            u = 0.03 + i * 0.084
            x_abs, py, pz = arch_curve(u)
            px = x_abs * s
            wy = 6.8 + i * 0.7
            wx = 2.40
            wz = 0.95
            rz = (9.0 + i * 4.5) * s
            rx = 1.0
            ry = -4.0 * s
            frm, to = box_top_pivot(px, py, pz + 0.04, wx, wy, wz)
            add_to_group(b_root, make_cube(
                f"{b_root}_covert_{i+1}", frm, to, [px, py, pz + 0.04], [rx, ry, rz], UV_COVERT
            ))

        # --- B. CARPAL CREST, MEDIAN COVERTS & SECONDARIES (wing_*_mid, pivot [±11.5, 31.5, 2.38]) ---
        mid_arch_steps = [
            ("carpal_seg_1", 0.46, 0.73, 1.50, 1.35),
            ("carpal_seg_2", 0.71, 0.99, 1.40, 1.25),
        ]
        for lbl, u0, u1, thick_y, thick_z in mid_arch_steps:
            x0, y0, z0 = arch_curve(u0)
            x1, y1, z1 = arch_curve(u1)
            cx, cy, cz = 0.5 * (x0 + x1) * s, 0.5 * (y0 + y1), 0.5 * (z0 + z1)
            dx, dy = (x1 - x0), (y1 - y0)
            seg_len = math.hypot(dx, dy) + 0.9
            tilt_deg = math.degrees(math.atan2(dy, dx))
            frm = [cx - seg_len / 2.0, cy - thick_y / 2.0, cz - thick_z / 2.0]
            to = [cx + seg_len / 2.0, cy + thick_y / 2.0, cz + thick_z / 2.0]
            add_to_group(b_mid, make_cube(
                f"{b_mid}_{lbl}", frm, to, [cx, cy, cz], [-1.0, -3.5 * s, +tilt_deg * s], UV_CORE
            ))

        # 6 Median Covert Feathers (sakura-blush layer along u = 0.49..0.95)
        for i in range(6):
            u = 0.49 + i * 0.09
            x_abs, py, pz = arch_curve(u)
            px = x_abs * s
            wy = 9.2 - i * 0.2
            wx = 2.45
            wz = 0.88
            rz = (26.0 + i * 5.2) * s
            frm, to = box_top_pivot(px, py - 0.2, pz + 0.08, wx, wy, wz)
            add_to_group(b_mid, make_cube(
                f"{b_mid}_med_covert_{i+1}", frm, to, [px, py - 0.2, pz + 0.08], [1.2, -3.5 * s, rz], UV_COVERT
            ))

        # 7 Long Secondary Flight Feathers (overlapping softly with wx=2.55)
        for i in range(7):
            u = 0.42 + i * 0.092
            x_abs, py, pz = arch_curve(u)
            px = x_abs * s
            wy = 14.0 + math.sin(i / 6.0 * math.pi) * 2.4
            wx = 2.55
            wz = 0.72
            rz = (18.0 + i * 5.2) * s
            frm, to = box_top_pivot(px, py - 2.0, pz + 0.16, wx, wy, wz)
            add_to_group(b_mid, make_cube(
                f"{b_mid}_secondary_{i+1}", frm, to, [px, py - 2.0, pz + 0.16], [1.0, -3.0 * s, rz], UV_PRIMARY
            ))

        # --- C. OUTER PRIMARY FLIGHT FEATHERS (wing_*_tip, pivot [±20.5, 29.8, 2.80]) ---
        primaries = [
            # (lbl, px_abs, py, pz, wx, wy, wz, rx, ry_abs, rz_abs)
            ("prim_alula", 21.2, 30.1, 2.75, 1.85,  9.5, 0.72, 1.0, -3.0, 74.0),
            ("prim_1",     22.0, 29.8, 2.80, 2.25, 17.5, 0.68, 1.2, -3.5, 78.0),
            ("prim_2",     21.8, 29.0, 2.84, 2.35, 18.2, 0.68, 1.2, -3.5, 70.0),
            ("prim_3",     21.5, 27.9, 2.88, 2.35, 17.8, 0.68, 1.2, -3.0, 62.0),
            ("prim_4",     21.0, 26.7, 2.92, 2.35, 17.0, 0.68, 1.0, -3.0, 54.0),
            ("prim_5",     20.3, 25.4, 2.96, 2.30, 16.0, 0.68, 1.0, -2.5, 46.0),
            ("prim_6",     19.5, 24.1, 3.00, 2.25, 15.0, 0.68, 1.0, -2.5, 39.0),
            ("prim_7",     18.5, 22.8, 3.04, 2.20, 14.0, 0.68, 0.8, -2.0, 32.0),
            ("prim_8",     17.4, 21.6, 3.08, 2.15, 13.0, 0.68, 0.8, -2.0, 25.0),
        ]
        for lbl, px_abs, py, pz, wx, wy, wz, rx, ry_abs, rz_abs in primaries:
            px = px_abs * s
            frm, to = box_top_pivot(px, py, pz, wx, wy, wz)
            add_to_group(b_tip, make_cube(
                f"{b_tip}_{lbl}", frm, to, [px, py, pz], [rx, ry_abs * s, rz_abs * s], UV_PRIMARY
            ))

        # --- D. INNER TRAILING ANGEL PLUMAGE (wing_*_feathers, pivot [±6.5, 21.0, 2.05]) ---
        # 6 Soft overlapping inner feathers hugging the back of the Haori (z = 1.95..2.25, rx = 0.5 deg)
        for i in range(6):
            u = 0.04 + i * 0.068
            x_abs, py, pz = arch_curve(u)
            px = x_abs * s
            wy = 12.4 + i * 0.65
            wx = 2.50
            wz = 0.85
            rz = (4.0 + i * 2.8) * s
            frm, to = box_top_pivot(px, py - 1.5, pz + 0.05, wx, wy, wz)
            add_to_group(b_feathers, make_cube(
                f"{b_feathers}_trail_{i+1}", frm, to, [px, py - 1.5, pz + 0.05], [0.5, -2.5 * s, rz], UV_PRIMARY
            ))

    # =========================================================================
    # 3. 3D SAKURA PETAL PARTICLES ON THE WINGS (petal_streams, pivot [0, 22.0, 2.8])
    # 24 Visible diamond-tilted sakura petals swirling closely around the angel wings
    # =========================================================================
    petal_defs = [
        # Left wing (-X) sakura petals closely hugging the wing arch and feather tips (z = 1.4..3.8)
        ("petal_l1",  -6.5, 28.0, 1.5, 1.7, 1.15, 0.22,  18,  15, -38),
        ("petal_l2", -12.5, 34.5, 2.2, 1.8, 1.20, 0.22, -22,  18, -45),
        ("petal_l3", -18.5, 33.8, 1.8, 1.7, 1.15, 0.22,  20, -16, -52),
        ("petal_l4", -26.5, 31.0, 2.6, 1.9, 1.25, 0.22, -18,  22, -40),
        ("petal_l5", -35.5, 28.5, 3.1, 1.9, 1.25, 0.22,  25, -14, -48),
        ("petal_l6", -39.5, 23.5, 3.3, 1.8, 1.20, 0.22, -20,  20, -35),
        ("petal_l7", -36.5, 17.5, 3.4, 1.8, 1.20, 0.22,  18, -18, -28),
        ("petal_l8", -30.0, 12.8, 3.5, 1.7, 1.15, 0.22, -24,  15, -22),
        ("petal_l9", -22.5, 10.2, 3.4, 1.7, 1.15, 0.22,  22, -14, -18),
        ("petal_l10", -14.5, 9.0, 3.2, 1.6, 1.10, 0.22, -20,  12, -14),
        ("petal_l11", -9.5, 19.5, 1.4, 1.6, 1.10, 0.22,  15, -12, -42),
        ("petal_l12", -19.5, 22.0, 1.6, 1.7, 1.15, 0.22, -16,  14, -36),
        # Right wing (+X) sakura petals (mirror symmetric)
        ("petal_r1",   6.5, 28.0, 1.5, 1.7, 1.15, 0.22,  18, -15,  38),
        ("petal_r2",  12.5, 34.5, 2.2, 1.8, 1.20, 0.22, -22, -18,  45),
        ("petal_r3",  18.5, 33.8, 1.8, 1.7, 1.15, 0.22,  20,  16,  52),
        ("petal_r4",  26.5, 31.0, 2.6, 1.9, 1.25, 0.22, -18, -22,  40),
        ("petal_r5",  35.5, 28.5, 3.1, 1.9, 1.25, 0.22,  25,  14,  48),
        ("petal_r6",  39.5, 23.5, 3.3, 1.8, 1.20, 0.22, -20, -20,  35),
        ("petal_r7",  36.5, 17.5, 3.4, 1.8, 1.20, 0.22,  18,  18,  28),
        ("petal_r8",  30.0, 12.8, 3.5, 1.7, 1.15, 0.22, -24, -15,  22),
        ("petal_r9",  22.5, 10.2, 3.4, 1.7, 1.15, 0.22,  22,  14,  18),
        ("petal_r10", 14.5,  9.0, 3.2, 1.6, 1.10, 0.22, -20, -12,  14),
        ("petal_r11",  9.5, 19.5, 1.4, 1.6, 1.10, 0.22,  15,  12,  42),
        ("petal_r12", 19.5, 22.0, 1.6, 1.7, 1.15, 0.22, -16, -14,  36),
    ]
    for lbl, cx, cy, cz, wx, wy, wz, rx, ry, rz in petal_defs:
        frm = [cx - wx / 2.0, cy - wy / 2.0, cz - wz / 2.0]
        to = [cx + wx / 2.0, cy + wy / 2.0, cz + wz / 2.0]
        add_to_group("petal_streams", make_cube(lbl, frm, to, [cx, cy, cz], [rx, ry, rz], UV_PETAL))

    # =========================================================================
    # 4. SHINIGAMI CAPTAIN PREVIEW MANNEQUIN + EXISTING ZANPAKUTO REFERENCE
    # (Filtered out on GeckoLib export; present in Blockbench for frame-by-frame pose review)
    # =========================================================================
    # Torso + Captain Haori + Scarf
    add_to_group("player_body", make_cube("torso_shihakusho", [-4, 12, -2], [4, 24, 2], [0, 24, 0], [0, 0, 0], UV_ROBE))
    add_to_group("player_body", make_cube("captain_haori", [-4.35, 12.2, -2.25], [4.35, 24.1, 2.35], [0, 24, 0], [0, 0, 0], UV_HAORI))
    add_to_group("player_body", make_cube("haori_diamond_hem", [-4.4, 10.0, -2.3], [4.4, 12.4, 2.4], [0, 24, 0], [0, 0, 0], UV_HEM))
    add_to_group("player_body", make_cube("byakuya_scarf", [-4.5, 22.2, -2.5], [4.5, 24.6, 2.5], [0, 24, 0], [0, 0, 0], UV_SCARF))

    # Head + Kenseikan hairpieces
    add_to_group("head", make_cube("captain_head", [-4, 24, -4], [4, 32, 4], [0, 24, 0], [0, 0, 0], UV_SKIN))
    add_to_group("head", make_cube("captain_hair", [-4.25, 24.5, -4.25], [4.25, 32.3, 4.25], [0, 24, 0], [0, 0, 0], UV_HAIR))
    add_to_group("head", make_cube("kenseikan_top", [-1.5, 32.0, -2.5], [1.5, 33.6, 0.5], [0, 24, 0], [0, 0, 0], UV_HAORI))
    add_to_group("head", make_cube("kenseikan_side_r", [-4.6, 26.0, -3.8], [-3.8, 30.5, -2.2], [0, 24, 0], [0, 0, 0], UV_HAORI))

    # Right Arm (+X is model left in Blockbench? Wait: in Blockbench [-4..-8] is Right Arm!)
    # Plus exact reference of the existing in-game Zanpakuto (senbonzakura_hakuteiken.json: white handle, window guard tsuba, white blade)
    add_to_group("right_arm", make_cube("right_arm_mesh", [-8, 12, -2], [-4, 24, 2], [-5, 22, 0], [0, 0, 0], UV_ROBE))
    add_to_group("right_arm", make_cube("right_haori_sleeve", [-8.3, 17, -2.3], [-3.9, 24.2, 2.3], [-5, 22, 0], [0, 0, 0], UV_HAORI))
    # Existing in-game Zanpakuto (senbonzakura_hakuteiken.json) held in right hand at [-6, 12.5, -1.0]
    # Aligned with the arm axis (-Y) so when right_arm thrusts forward (rx = -88 deg), the katana points straight horizontally forward (-Z)!
    add_to_group("right_arm", make_cube("zanpakuto_existing_tsuka", [-6.45, 10.5, -1.45], [-5.55, 16.5, -0.55], [-6, 12.5, -1.0], [10, 0, 0], UV_ZAN_WHITE))
    add_to_group("right_arm", make_cube("zanpakuto_existing_tsuba_frame", [-7.1, 10.0, -2.2], [-4.9, 10.6, 0.2], [-6, 12.5, -1.0], [10, 0, 0], UV_ZAN_WHITE))
    add_to_group("right_arm", make_cube("zanpakuto_existing_blade", [-6.25, -9.0, -1.35], [-5.75, 10.0, -0.65], [-6, 12.5, -1.0], [10, 0, 0], UV_ZAN_WHITE))

    # Left Arm ([4..8])
    add_to_group("left_arm", make_cube("left_arm_mesh", [4, 12, -2], [8, 24, 2], [5, 22, 0], [0, 0, 0], UV_ROBE))
    add_to_group("left_arm", make_cube("left_haori_sleeve", [3.9, 17, -2.3], [8.3, 24.2, 2.3], [5, 22, 0], [0, 0, 0], UV_HAORI))

    # Right & Left Legs
    add_to_group("right_leg", make_cube("right_leg_hakama", [-4.1, 0, -2.1], [0.0, 12, 2.1], [-2, 12, 0], [0, 0, 0], UV_ROBE))
    add_to_group("left_leg", make_cube("left_leg_hakama", [0.0, 0, -2.1], [4.1, 12, 2.1], [2, 12, 0], [0, 0, 0], UV_ROBE))

    # =========================================================================
    # 5. OUTLINER HIERARCHY (11 Construct Bones + 6 Player Preview Bones = 17 Bones)
    # =========================================================================
    def make_group(name, origin, children):
        return {
            "name": name,
            "origin": origin,
            "rotation": [0, 0, 0],
            "color": 0,
            "uuid": make_uuid(f"group.{name}"),
            "export": True,
            "mirror_uv": False,
            "isOpen": True,
            "locked": False,
            "visibility": True,
            "autouv": 0,
            "children": children
        }

    outliner = [
        make_group("root", [0, 0, 0], [
            make_group("wing_nexus", [0, 21.5, 1.85], groups.get("wing_nexus", []) + [
                make_group("wing_left_root", [-2.2, 21.5, 1.95], groups.get("wing_left_root", []) + [
                    make_group("wing_left_mid", [-11.5, 31.5, 2.38], groups.get("wing_left_mid", []) + [
                        make_group("wing_left_tip", [-20.5, 29.8, 2.80], groups.get("wing_left_tip", []))
                    ]),
                    make_group("wing_left_feathers", [-6.5, 21.0, 2.05], groups.get("wing_left_feathers", []))
                ]),
                make_group("wing_right_root", [2.2, 21.5, 1.95], groups.get("wing_right_root", []) + [
                    make_group("wing_right_mid", [11.5, 31.5, 2.38], groups.get("wing_right_mid", []) + [
                        make_group("wing_right_tip", [20.5, 29.8, 2.80], groups.get("wing_right_tip", []))
                    ]),
                    make_group("wing_right_feathers", [6.5, 21.0, 2.05], groups.get("wing_right_feathers", []))
                ]),
                make_group("petal_streams", [0, 22.0, 2.6], groups.get("petal_streams", []))
            ]),
            make_group("player_body", [0, 21.5, 1.85], groups.get("player_body", []) + [
                make_group("head", [0, 24, 0], groups.get("head", [])),
                make_group("right_arm", [-5, 22, 0], groups.get("right_arm", [])),
                make_group("left_arm", [5, 22, 0], groups.get("left_arm", [])),
                make_group("right_leg", [-2, 12, 0], groups.get("right_leg", [])),
                make_group("left_leg", [2, 12, 0], groups.get("left_leg", []))
            ])
        ])
    ]

    return elements, outliner


def make_kf(channel, time_s, x, y, z, interp="catmullrom"):
    return {
        "channel": channel,
        "data_points": [{"x": str(round(x, 3)), "y": str(round(y, 3)), "z": str(round(z, 3))}],
        "uuid": make_uuid(f"kf.{channel}.{time_s}.{x}.{y}.{z}.{uid_counter()}"),
        "time": round(float(time_s), 4),
        "color": -1,
        "interpolation": interp
    }


_COUNTER = 0
def uid_counter():
    global _COUNTER
    _COUNTER += 1
    return _COUNTER


def build_animations():
    """Builds 3 fluid, weightless angelic animations with overlapping feather-wave motion and floating petals:
    - animation.hakuteiken.idle (2.0s loop): Active 90s Hakuteiken winged transformation wingbeat
    - animation.hakuteiken.windup (0.8s hold): Upward wing-propelled leap before diagonal rasante
    - animation.hakuteiken.dash (1.0s hold): Diagonal aerial rasante dive -> ground petal sea explosion flare
    """
    def anim_bone(b_name, kfs):
        return make_uuid(f"group.{b_name}"), {
            "name": b_name,
            "type": "bone",
            "keyframes": kfs
        }

    # -------------------------------------------------------------------------
    # 1. IDLE (2.0s loop): Active Hakuteiken transformation wingbeat (zero gap)
    # -------------------------------------------------------------------------
    idle_animators = dict([
        anim_bone("wing_nexus", [
            make_kf("position", 0.0, 0, 0.0, 0),
            make_kf("position", 1.0, 0, 0.25, 0),
            make_kf("position", 2.0, 0, 0.0, 0),
        ]),
        anim_bone("wing_left_root", [
            make_kf("rotation", 0.0, 3, 8, -4),
            make_kf("rotation", 0.5, 0, -6, 6),
            make_kf("rotation", 1.0, -3, -14, 10),
            make_kf("rotation", 1.5, 1, -2, 2),
            make_kf("rotation", 2.0, 3, 8, -4),
        ]),
        anim_bone("wing_right_root", [
            make_kf("rotation", 0.0, 3, -8, 4),
            make_kf("rotation", 0.5, 0, 6, -6),
            make_kf("rotation", 1.0, -3, 14, -10),
            make_kf("rotation", 1.5, 1, 2, -2),
            make_kf("rotation", 2.0, 3, -8, 4),
        ]),
        # Mid-wing lags root by 0.25s for fluid feather wave
        anim_bone("wing_left_mid", [
            make_kf("rotation", 0.0, 2, 12, -8),
            make_kf("rotation", 0.5, 4, 4, -2),
            make_kf("rotation", 1.0, -2, -12, 8),
            make_kf("rotation", 1.5, -3, -4, 4),
            make_kf("rotation", 2.0, 2, 12, -8),
        ]),
        anim_bone("wing_right_mid", [
            make_kf("rotation", 0.0, 2, -12, 8),
            make_kf("rotation", 0.5, 4, -4, 2),
            make_kf("rotation", 1.0, -2, 12, -8),
            make_kf("rotation", 1.5, -3, 4, -4),
            make_kf("rotation", 2.0, 2, -12, 8),
        ]),
        # Outer primaries lag mid-wing by another 0.25s (whip/feather follow-through)
        anim_bone("wing_left_tip", [
            make_kf("rotation", 0.0, -2, 16, -10),
            make_kf("rotation", 0.5, 3, 10, -4),
            make_kf("rotation", 1.0, 2, -8, 6),
            make_kf("rotation", 1.5, -2, -14, 10),
            make_kf("rotation", 2.0, -2, 16, -10),
        ]),
        anim_bone("wing_right_tip", [
            make_kf("rotation", 0.0, -2, -16, 10),
            make_kf("rotation", 0.5, 3, -10, 4),
            make_kf("rotation", 1.0, 2, 8, -6),
            make_kf("rotation", 1.5, -2, 14, -10),
            make_kf("rotation", 2.0, -2, -16, 10),
        ]),
        anim_bone("wing_left_feathers", [
            make_kf("rotation", 0.0, 2, 6, -4),
            make_kf("rotation", 1.0, -2, -8, 6),
            make_kf("rotation", 2.0, 2, 6, -4),
        ]),
        anim_bone("wing_right_feathers", [
            make_kf("rotation", 0.0, 2, -6, 4),
            make_kf("rotation", 1.0, -2, 8, -6),
            make_kf("rotation", 2.0, 2, -6, 4),
        ]),
        # Sakura petals gently float and shimmer around the angel wings
        anim_bone("petal_streams", [
            make_kf("position", 0.0, 0, -0.8, 0),
            make_kf("position", 1.0, 0, 1.4, -0.4),
            make_kf("position", 2.0, 0, -0.8, 0),
            make_kf("rotation", 0.0, -3, -4, 0),
            make_kf("rotation", 1.0, 4, 4, 0),
            make_kf("rotation", 2.0, -3, -4, 0),
        ]),
        anim_bone("right_arm", [
            make_kf("rotation", 0.0, -28, -12, 8),
            make_kf("rotation", 1.0, -32, -10, 6),
            make_kf("rotation", 2.0, -28, -12, 8),
        ]),
        anim_bone("left_arm", [
            make_kf("rotation", 0.0, 8, 6, -6),
            make_kf("rotation", 1.0, 12, 8, -8),
            make_kf("rotation", 2.0, 8, 6, -6),
        ]),
    ])

    # -------------------------------------------------------------------------
    # 2. WINDUP (0.8s hold): Powerful upward wingbeat leap -> aiming diagonal rasante
    # -------------------------------------------------------------------------
    windup_animators = dict([
        anim_bone("wing_nexus", [
            make_kf("scale", 0.0, 0.72, 0.72, 0.72),
            make_kf("scale", 0.28, 1.04, 1.08, 1.02),
            make_kf("scale", 0.58, 1.08, 1.12, 1.05),
            make_kf("scale", 0.8, 1.0, 1.0, 1.0),
            make_kf("position", 0.0, 0, 0.0, 0.0),
            make_kf("position", 0.8, 0, 0.0, 0.0),
            make_kf("rotation", 0.0, 6, 0, 0),
            make_kf("rotation", 0.45, -10, 14, 0),
            make_kf("rotation", 0.8, 18, -8, 0),
        ]),
        anim_bone("wing_left_root", [
            # 0.0s: Wings gather forward then beat down to propel the upward leap
            make_kf("rotation", 0.0, 8, -36, 14),
            make_kf("rotation", 0.25, 4, 18, -12),
            # 0.58s: High angelic arch unfurled at the apex of the leap
            make_kf("rotation", 0.58, -8, 22, -16),
            make_kf("rotation", 0.8, -4, 12, -10),
        ]),
        anim_bone("wing_right_root", [
            make_kf("rotation", 0.0, 8, 36, -14),
            make_kf("rotation", 0.25, 4, -18, 12),
            make_kf("rotation", 0.58, -8, -22, 16),
            make_kf("rotation", 0.8, -4, -12, 10),
        ]),
        anim_bone("wing_left_mid", [
            make_kf("rotation", 0.0, 6, -42, 18),
            make_kf("rotation", 0.28, 2, 14, -8),
            make_kf("rotation", 0.62, -6, 18, -14),
            make_kf("rotation", 0.8, -3, 10, -8),
        ]),
        anim_bone("wing_right_mid", [
            make_kf("rotation", 0.0, 6, 42, -18),
            make_kf("rotation", 0.28, 2, -14, 8),
            make_kf("rotation", 0.62, -6, -18, 14),
            make_kf("rotation", 0.8, -3, -10, 8),
        ]),
        anim_bone("wing_left_tip", [
            make_kf("rotation", 0.0, 4, -38, 16),
            make_kf("rotation", 0.32, 4, 10, -6),
            make_kf("rotation", 0.66, -4, 22, -14),
            make_kf("rotation", 0.8, -2, 12, -8),
        ]),
        anim_bone("wing_right_tip", [
            make_kf("rotation", 0.0, 4, 38, -16),
            make_kf("rotation", 0.32, 4, -10, 6),
            make_kf("rotation", 0.66, -4, -22, 14),
            make_kf("rotation", 0.8, -2, -12, 8),
        ]),
        anim_bone("wing_left_feathers", [
            make_kf("rotation", 0.0, 6, -22, 10),
            make_kf("rotation", 0.58, -4, 12, -8),
            make_kf("rotation", 0.8, -2, 6, -4),
        ]),
        anim_bone("wing_right_feathers", [
            make_kf("rotation", 0.0, 6, 22, -10),
            make_kf("rotation", 0.58, -4, -12, 8),
            make_kf("rotation", 0.8, -2, -6, 4),
        ]),
        anim_bone("petal_streams", [
            make_kf("scale", 0.0, 0.6, 0.6, 0.6),
            make_kf("scale", 0.55, 1.18, 1.18, 1.18),
            make_kf("scale", 0.8, 1.05, 1.05, 1.05),
            make_kf("position", 0.0, 0, -2.0, -0.5),
            make_kf("position", 0.55, 0, 1.6, 0.4),
            make_kf("position", 0.8, 0, 0.5, 0.0),
        ]),
        # Captain preview pose during upward leap -> aiming diagonal rasante
        anim_bone("player_body", [
            make_kf("rotation", 0.0, 6, 0, 0),
            make_kf("rotation", 0.45, -10, 14, 0),
            make_kf("rotation", 0.8, 18, -8, 0),
        ]),
        anim_bone("head", [
            make_kf("rotation", 0.0, 0, 0, 0),
            make_kf("rotation", 0.45, -12, -12, 0),
            make_kf("rotation", 0.8, 12, 8, 0),
        ]),
        anim_bone("right_arm", [
            make_kf("rotation", 0.0, -30, 10, -6),
            make_kf("rotation", 0.45, -135, -18, 14),
            make_kf("rotation", 0.8, -72, 10, -6),
        ]),
        anim_bone("left_arm", [
            make_kf("rotation", 0.0, 10, 8, -8),
            make_kf("rotation", 0.45, -48, 22, -18),
            make_kf("rotation", 0.8, 28, 16, -20),
        ]),
        anim_bone("right_leg", [
            make_kf("rotation", 0.0, 12, 0, 0),
            make_kf("rotation", 0.45, 22, -8, 5),
            make_kf("rotation", 0.8, -18, 0, 4),
        ]),
        anim_bone("left_leg", [
            make_kf("rotation", 0.0, -12, 0, 0),
            make_kf("rotation", 0.45, -24, 8, -5),
            make_kf("rotation", 0.8, 22, 0, -4),
        ]),
    ])

    # -------------------------------------------------------------------------
    # 3. DASH (1.0s hold): Diagonal aerial rasante dive (0..0.5s) -> Sea of Petals impact flare (0.5..1.0s)
    # -------------------------------------------------------------------------
    dash_animators = dict([
        anim_bone("wing_nexus", [
            make_kf("position", 0.0, 0, 0.0, 0.0),
            make_kf("position", 1.0, 0, 0.0, 0.0),
            make_kf("scale", 0.0, 1.0, 1.0, 1.0),
            make_kf("scale", 0.5, 1.05, 1.05, 1.05),
            make_kf("scale", 0.65, 1.16, 1.14, 1.12),
            make_kf("scale", 1.0, 1.0, 1.0, 1.0),
            make_kf("rotation", 0.0, 18, -8, 0),
            make_kf("rotation", 0.2, 24, -10, 0),
            make_kf("rotation", 0.45, 24, -10, 0),
            make_kf("rotation", 0.62, 10, 8, 0),
            make_kf("rotation", 0.85, 0, 0, 0),
        ]),
        anim_bone("wing_left_root", [
            # Swept back in aerodynamic V-dive during diagonal rasante (0.0..0.48s)
            make_kf("rotation", 0.0, -4, 12, -10),
            make_kf("rotation", 0.16, -8, 28, -16),
            make_kf("rotation", 0.42, -6, 24, -14),
            # Ground impact flare (0.55..0.75s): wings burst wide open!
            make_kf("rotation", 0.60, 4, -22, 14),
            make_kf("rotation", 1.0, 2, 6, -4),
        ]),
        anim_bone("wing_right_root", [
            make_kf("rotation", 0.0, -4, -12, 10),
            make_kf("rotation", 0.16, -8, -28, 16),
            make_kf("rotation", 0.42, -6, -24, 14),
            make_kf("rotation", 0.60, 4, 22, -14),
            make_kf("rotation", 1.0, 2, -6, 4),
        ]),
        anim_bone("wing_left_mid", [
            make_kf("rotation", 0.0, -3, 10, -8),
            make_kf("rotation", 0.18, -6, 22, -12),
            make_kf("rotation", 0.44, -4, 18, -10),
            make_kf("rotation", 0.64, 4, -18, 12),
            make_kf("rotation", 1.0, 2, 8, -6),
        ]),
        anim_bone("wing_right_mid", [
            make_kf("rotation", 0.0, -3, -10, 8),
            make_kf("rotation", 0.18, -6, -22, 12),
            make_kf("rotation", 0.44, -4, -18, 10),
            make_kf("rotation", 0.64, 4, 18, -12),
            make_kf("rotation", 1.0, 2, -8, 6),
        ]),
        anim_bone("wing_left_tip", [
            make_kf("rotation", 0.0, -2, 12, -8),
            make_kf("rotation", 0.2, -4, 18, -10),
            make_kf("rotation", 0.46, -2, 16, -8),
            make_kf("rotation", 0.68, 3, -16, 10),
            make_kf("rotation", 1.0, 0, 10, -6),
        ]),
        anim_bone("wing_right_tip", [
            make_kf("rotation", 0.0, -2, -12, 8),
            make_kf("rotation", 0.2, -4, -18, 10),
            make_kf("rotation", 0.46, -2, -16, 8),
            make_kf("rotation", 0.68, 3, 16, -10),
            make_kf("rotation", 1.0, 0, -10, 6),
        ]),
        anim_bone("wing_left_feathers", [
            make_kf("rotation", 0.0, -2, 6, -4),
            make_kf("rotation", 0.18, -4, 14, -8),
            make_kf("rotation", 0.60, 4, -10, 6),
            make_kf("rotation", 1.0, 2, 4, -2),
        ]),
        anim_bone("wing_right_feathers", [
            make_kf("rotation", 0.0, -2, -6, 4),
            make_kf("rotation", 0.18, -4, -14, 8),
            make_kf("rotation", 0.60, 4, 10, -6),
            make_kf("rotation", 1.0, 2, -4, 2),
        ]),
        anim_bone("petal_streams", [
            make_kf("position", 0.0, 0, 0.5, 0.0),
            make_kf("position", 0.25, 0, 1.4, 3.5),
            make_kf("position", 0.58, 0, -2.5, -1.5),
            make_kf("position", 1.0, 0, 0.0, 0.0),
            make_kf("scale", 0.0, 1.0, 1.0, 1.0),
            make_kf("scale", 0.58, 1.45, 1.25, 1.45),
            make_kf("scale", 1.0, 1.0, 1.0, 1.0),
        ]),
        # Captain preview pose during diagonal rasante dive -> impact slash
        anim_bone("player_body", [
            make_kf("rotation", 0.0, 18, -8, 0),
            make_kf("rotation", 0.2, 24, -10, 0),
            make_kf("rotation", 0.45, 24, -10, 0),
            make_kf("rotation", 0.62, 10, 8, 0),
            make_kf("rotation", 0.85, 0, 0, 0),
        ]),
        anim_bone("head", [
            make_kf("rotation", 0.0, 12, 8, 0),
            make_kf("rotation", 0.2, -10, 10, 0),
            make_kf("rotation", 0.45, -10, 10, 0),
            make_kf("rotation", 0.85, 0, 0, 0),
        ]),
        anim_bone("right_arm", [
            make_kf("rotation", 0.0, -72, 10, -6),
            make_kf("rotation", 0.2, -78, -4, 0),
            make_kf("rotation", 0.45, -78, -4, 0),
            make_kf("rotation", 0.62, -42, -24, 16),
            make_kf("rotation", 0.85, -28, -12, 8),
        ]),
        anim_bone("left_arm", [
            make_kf("rotation", 0.0, 28, 16, -20),
            make_kf("rotation", 0.2, 42, 20, -26),
            make_kf("rotation", 0.45, 44, 22, -28),
            make_kf("rotation", 0.85, 8, 6, -6),
        ]),
        anim_bone("right_leg", [
            make_kf("rotation", 0.0, -18, 0, 4),
            make_kf("rotation", 0.2, -26, 0, 5),
            make_kf("rotation", 0.45, -28, 0, 6),
            make_kf("rotation", 0.85, 0, 0, 0),
        ]),
        anim_bone("left_leg", [
            make_kf("rotation", 0.0, 22, 0, -4),
            make_kf("rotation", 0.2, 32, 0, -6),
            make_kf("rotation", 0.45, 34, 0, -6),
            make_kf("rotation", 0.85, 0, 0, 0),
        ]),
    ])

    return [
        {
            "uuid": make_uuid("anim.idle"),
            "name": "animation.hakuteiken.idle",
            "loop": "loop",
            "override": False,
            "length": 2.0,
            "snapping": 20,
            "selected": False,
            "animators": idle_animators
        },
        {
            "uuid": make_uuid("anim.windup"),
            "name": "animation.hakuteiken.windup",
            "loop": "hold",
            "override": False,
            "length": 0.8,
            "snapping": 20,
            "selected": False,
            "animators": windup_animators
        },
        {
            "uuid": make_uuid("anim.dash"),
            "name": "animation.hakuteiken.dash",
            "loop": "hold",
            "override": False,
            "length": 1.0,
            "snapping": 20,
            "selected": True,
            "animators": dash_animators
        }
    ]


def main():
    elements, outliner = build_elements_and_outliner()
    animations = build_animations()
    tex_data_url = generate_angel_wing_texture()

    bbmodel = {
        "meta": {
            "format_version": "4.10",
            "model_format": "geckolib_model",
            "box_uv": False
        },
        "name": "ag_senbon_hakuteiken_v2",
        "model_identifier": "senbon_hakuteiken",
        "visible_box": [3, 3, 0],
        "variable_placeholders": "",
        "variable_placeholder_buttons": [],
        "timeline_setups": [],
        "unhandled_root_fields": {},
        "resolution": {"width": 128, "height": 256},
        "elements": elements,
        "outliner": outliner,
        "textures": [
            {
                "path": "",
                "name": "senbon_hakuteiken.png",
                "folder": "entity",
                "namespace": "bleachmod",
                "id": "0",
                "particle": False,
                "render_mode": "default",
                "render_sides": "auto",
                "frame_time": 1,
                "frame_order_type": "loop",
                "frame_order": "",
                "frame_interpolate": False,
                "visible": True,
                "internal": True,
                "saved": True,
                "uuid": make_uuid("tex.main"),
                "relative_path": "",
                "source": tex_data_url
            }
        ],
        "animations": animations
    }

    BBMODEL_PATH.write_text(json.dumps(bbmodel, indent=2), encoding="utf-8")
    print(f"Wrote {BBMODEL_PATH} ({len(elements)} cubes, {len(animations)} animations)")


if __name__ == "__main__":
    main()
