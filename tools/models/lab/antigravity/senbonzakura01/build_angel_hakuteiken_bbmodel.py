"""Master Blockbench model generator for Senbonzakura Shūkei: Hakuteiken.
Refinements:
1. Elytra-style Scapular Harness & Mantle:
   - Central spinal plate flush on dorsal spine (Z = 1.95..2.35).
   - Left and right shoulder straps wrap over the shoulders (Y = 23.5..24.5, Z = -2.35..2.35)
     clasping the clavicle in front and scapula in back, physically unifying the wings with the body like an Elytra.
   - Inner plumage hugs the Haori at Z = 2.10..2.35 with ZERO air gap.
2. Organic Curved Feathers:
   - 2-tier articulated feather curves (upper vane + parabolic sweeping tip) eliminating rigid bicycle spokes.
   - Feather tips curve outward/downward smoothly with continuous taper (no 90-deg Lego steps).
3. 3-Tier Volumetric 3D Depth in Z:
   - Layer 1 (Coverts & Mantle): Z = 1.85..2.35 (flush with body)
   - Layer 2 (Flight Arch & Secondaries): Z = 2.20..3.35 (curved middle body)
   - Layer 3 (Primaries & Alula): Z = 2.60..3.90 (outer trailing crest)
4. Strict Bilateral Mirror Symmetry:
   - X = 0.00 exact centerline symmetry for all nexus and harness elements.
"""
from __future__ import annotations

import base64
import io
import json
import math
import uuid
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[5]
BBMODEL_PATH = ROOT / "tools/models/lab/antigravity/senbonzakura01/ag_senbon_hakuteiken_v2.bbmodel"


def make_uuid(seed: str) -> str:
    return str(uuid.uuid5(uuid.NAMESPACE_DNS, seed))


def generate_angel_wing_texture() -> str:
    width, height = 128, 256
    img = Image.new("RGBA", (width, height), (255, 255, 255, 0))
    px = img.load()

    # Region A (x=0..63, y=0..63): Primary Flight Feathers & Halo
    # Luminous celestial white core (#ffffff) with delicate crystalline sakura tip at the bottom 25%
    for y in range(0, 64):
        t = y / 63.0  # 0.0 at root/top, 1.0 at feather tip
        for x in range(0, 64):
            u = x / 63.0
            edge = 1.0 - math.sin(u * math.pi) * 0.06
            # Pure white for upper 75%, soft ethereal sakura glow for bottom 25%
            tip_pink = max(0.0, (t - 0.70) / 0.30)
            r = 255
            g = int((255 - 42 * (tip_pink ** 1.3)) * edge)
            b = int((255 - 24 * (tip_pink ** 1.3)) * edge)
            px[x, y] = (r, g, b, 255)

    # Region B (x=64..127, y=0..63): Covert Plumage, Scapular Harness & Celestial Halo
    # Brilliant radiant white reishi (#ffffff -> #fff8fb) with spiritual luminescence
    for y in range(0, 64):
        t = y / 63.0
        for x in range(64, 128):
            u = (x - 64) / 63.0
            s = math.sin(u * math.pi) * (1.0 - t * 0.20)
            r = 255
            g = int(248 + 7 * (1.0 - s))
            b = int(252 + 3 * (1.0 - s))
            px[x, y] = (r, g, b, 255)

    # Region C (x=0..63, y=64..127): 3D Glowing Sakura Petals (#ff75a4 -> #ffd8e6)
    for y in range(64, 128):
        t = (y - 64) / 63.0
        for x in range(0, 64):
            u = x / 63.0
            dist = math.hypot(u - 0.5, t - 0.5) * 1.4
            s = max(0.0, min(1.0, 1.0 - dist * 0.65))
            r = 255
            g = int(145 + 85 * (1.0 - s))
            b = int(185 + 55 * (1.0 - s))
            px[x, y] = (r, g, b, 254)

    # Region D (x=64..127, y=64..127): Pure Luminous Reishi Core (#ffffff)
    for y in range(64, 128):
        for x in range(64, 128):
            px[x, y] = (255, 255, 255, 255)

    # Captain Mannequin preview colors (y=128..255)
    draw = ImageDraw.Draw(img)
    draw.rectangle([0, 128, 31, 159], fill=(235, 198, 172, 255))      # Skin
    draw.rectangle([32, 128, 63, 159], fill=(26, 22, 34, 255))        # Hair
    draw.rectangle([64, 128, 95, 159], fill=(246, 246, 250, 255))     # Captain Haori white
    draw.rectangle([96, 128, 127, 159], fill=(216, 232, 230, 255))    # Scarf
    draw.rectangle([0, 160, 63, 223], fill=(20, 20, 26, 255))         # Shihakusho black
    draw.rectangle([64, 160, 127, 191], fill=(242, 242, 247, 255))    # Haori hem
    for dx in range(66, 124, 8):
        draw.polygon([(dx, 184), (dx + 3, 179), (dx + 6, 184), (dx + 3, 189)], fill=(25, 25, 32, 255))
    draw.rectangle([64, 192, 95, 255], fill=(252, 248, 254, 255))     # Hakuteiken blade/tsuba white
    draw.rectangle([96, 192, 127, 223], fill=(218, 178, 72, 255))     # Gold accents
    draw.rectangle([96, 224, 127, 255], fill=(168, 74, 140, 255))     # Magenta wrap

    buf = io.BytesIO()
    img.save(buf, format="PNG")
    b64 = base64.b64encode(buf.getvalue()).decode("ascii")
    return f"data:image/png;base64,{b64}"

def make_cube(name: str, frm, to, origin, rotation, uv_box):
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

    UV_PRIMARY = [4, 4, 60, 60]       # White -> soft pink tip
    UV_COVERT = [68, 4, 124, 60]      # Sakura pink -> white covert
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
    # 1. CELESTIAL REISHI HALO & ELYTRA-STYLE SCAPULAR HARNESS (wing_nexus)
    # Physically unified with the player's dorsal spine (Z = 2.02..2.28):
    # - Iconic 12-segment Celestial Reishi Halo (radius 6.8 units) hovering behind upper spine/neck!
    # - Central sleek spine plate flush against back of torso with ZERO air gap.
    # - Scapular mantle plates embracing the shoulder blades, completely closing the center void!
    # - Dorsal mantle plumage blanket covering the entire back down to the waist.
    # =========================================================================

    # A. 12-Segment Radiant Celestial Halo centered at [0, 23.5, 2.25]
    halo_radius = 6.8
    halo_thick_r = 1.15
    halo_depth_z = 0.55
    for k in range(12):
        theta = k * (2.0 * math.pi / 12.0)
        cx = halo_radius * math.sin(theta)
        cy = 23.5 + halo_radius * math.cos(theta)
        cz = 2.25
        seg_len = 2.0 * halo_radius * math.sin(math.pi / 12.0) + 0.65
        seg_deg = -math.degrees(theta)
        frm = [cx - seg_len / 2.0, cy - halo_thick_r / 2.0, cz - halo_depth_z / 2.0]
        to = [cx + seg_len / 2.0, cy + halo_thick_r / 2.0, cz + halo_depth_z / 2.0]
        add_to_group("wing_nexus", make_cube(
            f"halo_seg_{k+1}", frm, to, [cx, cy, cz], [0, 0, seg_deg], UV_CORE
        ))

    # B. Central Sleek Spine Plate (flush against the back at Z = 2.02..2.16, Y = 13.0..24.0)
    add_to_group("wing_nexus", make_cube(
        "nexus_spinal_crest", [-1.2, 13.0, 2.02], [1.2, 23.8, 2.16],
        [0, 21.0, 2.05], [0, 0, 0], UV_CORE
    ))

    # C. Scapular Harness Plates embracing the shoulder blades (X = ±1.0..±3.8, Y = 16.5..23.2)
    add_to_group("wing_nexus", make_cube(
        "nexus_scapula_l", [-3.8, 16.5, 2.04], [-1.0, 23.2, 2.16],
        [-2.4, 21.0, 2.05], [0, 0, -3.5], UV_COVERT
    ))
    add_to_group("wing_nexus", make_cube(
        "nexus_scapula_r", [1.0, 16.5, 2.04], [3.8, 23.2, 2.16],
        [2.4, 21.0, 2.05], [0, 0, 3.5], UV_COVERT
    ))

    # D. Dorsal Mantle Plumage filling the back space below the halo down to waist (Y = 12.0..18.0)
    # This completely eliminates any empty void behind the player!
    add_to_group("wing_nexus", make_cube(
        "nexus_mantle_center", [-2.8, 12.0, 2.06], [2.8, 17.5, 2.18],
        [0, 15.0, 2.10], [0, 0, 0], UV_PRIMARY
    ))

    # =========================================================================
    # 2. VOLUMETRIC ANGELIC WINGS (Symmetric Left [-X] and Right [+X])
    # 3D Depth Curve in Z:
    # - Starts at scapula: x = 1.8, y = 21.5, z = 2.05 (FLUSH on back of torso!)
    # - Arches up and curves back: crest at x = 14.5, y = 34.0, z = 3.35 (3D volume)
    # - Glides outward and sweeps gracefully down: tip at x = 23.0, y = 30.2, z = 2.68
    # Strict bone boundaries:
    #   root: u in [0.00, 0.38] (X: 1.8..9.85,  pivot [±1.8, 21.5, 2.05])
    #   mid:  u in [0.38, 0.68] (X: 9.85..16.2, pivot [±9.85, 32.7, 3.24])
    #   tip:  u in [0.68, 1.00] (X: 16.2..23.0, pivot [±16.2, 35.4, 3.41])
    # =========================================================================
    def arch_curve(u: float):
        x_abs = 1.8 + 21.2 * u
        y = 21.5 + 15.5 * math.sin(u * math.pi * 0.72) - 3.2 * (u ** 1.8)
        z = 2.05 + 1.40 * math.sin(u * math.pi * 0.85)
        return x_abs, y, z

    def add_curved_feather(group_name, lbl, px, py, pz, wx, total_len, wz, rz_base, curve_extra, s, z_stagger=0.0):
        """Creates an organic, curved feather with 2 articulated planar blade segments:
        - Upper shaft/vane: length = 60% of total, rotated at rz_base
        - Sweeping tip: length = 40% of total, curved outward by curve_extra, with tapered width!
        - Z-staggering creates dense overlapping plumage with ZERO air gaps and no z-fighting!"""
        len1 = total_len * 0.60
        len2 = total_len * 0.40
        rz1 = rz_base * s
        rz2 = (rz_base + curve_extra) * s
        actual_pz = pz + z_stagger

        # Upper vane: wide planar reishi blade
        frm1 = [px - wx / 2.0, py - len1, actual_pz - wz / 2.0]
        to1 = [px + wx / 2.0, py, actual_pz + wz / 2.0]
        add_to_group(group_name, make_cube(f"{lbl}_vane", frm1, to1, [px, py, actual_pz], [0.0, 0.0, rz1], UV_PRIMARY))

        # Tip pivot is at the bottom of the upper vane:
        rad1 = math.radians(rz1)
        tip_px = px + len1 * math.sin(rad1)
        tip_py = py - len1 * math.cos(rad1)
        tip_wx = wx * 0.72
        tip_wz = wz * 0.80
        frm2 = [tip_px - tip_wx / 2.0, tip_py - len2, actual_pz - tip_wz / 2.0]
        to2 = [tip_px + tip_wx / 2.0, tip_py, actual_pz + tip_wz / 2.0]
        add_to_group(group_name, make_cube(f"{lbl}_tip", frm2, to2, [tip_px, tip_py, actual_pz], [0.0, 0.0, rz2], UV_PRIMARY))

    for side_name, s in (("left", -1.0), ("right", 1.0)):
        b_root = f"wing_{side_name}_root"
        b_mid = f"wing_{side_name}_mid"
        b_tip = f"wing_{side_name}_tip"
        b_feathers = f"wing_{side_name}_feathers"

        # --- A. SCAPULAR SHOULDER & RISING ARCH (wing_*_root, u: 0.00..0.38, pivot [±1.8, 21.5, 2.05]) ---
        root_arch_steps = [
            ("arch_seg_1", 0.00, 0.18, 1.70, 1.45),
            ("arch_seg_2", 0.16, 0.38, 1.60, 1.35),
        ]
        for lbl, u0, u1, thick_y, thick_z in root_arch_steps:
            x0, y0, z0 = arch_curve(u0)
            x1, y1, z1 = arch_curve(u1)
            cx, cy, cz = 0.5 * (x0 + x1) * s, 0.5 * (y0 + y1), 0.5 * (z0 + z1)
            dx, dy = (x1 - x0), (y1 - y0)
            seg_len = math.hypot(dx, dy) + 1.1
            tilt_deg = math.degrees(math.atan2(dy, dx))
            frm = [cx - seg_len / 2.0, cy - thick_y / 2.0, cz - thick_z / 2.0]
            to = [cx + seg_len / 2.0, cy + thick_y / 2.0, cz + thick_z / 2.0]
            add_to_group(b_root, make_cube(
                f"{b_root}_{lbl}", frm, to, [cx, cy, cz], [0.0, 0.0, tilt_deg * s], UV_CORE
            ))

        # 6 Upper Coverts along the rising arch (Zone: X = 2.6..9.4, Layer 1)
        for i in range(6):
            u = 0.04 + i * (0.32 / 5.0)
            x_abs, py, pz = arch_curve(u)
            px = x_abs * s
            total_len = 8.0 + i * 0.90
            wx = 4.60
            wz = 0.58
            rz_base = 10.0 + i * 2.2
            z_stagger = ((i % 2) * 2 - 1) * 0.12
            add_curved_feather(b_root, f"{b_root}_covert_{i+1}", px, py - 0.08, pz - 0.08, wx, total_len, wz, rz_base, 5.0, s, z_stagger=z_stagger)

        # --- B. CARPAL CREST & SECONDARIES (wing_*_mid, u: 0.38..0.68, pivot [±9.85, 32.7, 3.24]) ---
        mid_arch_steps = [
            ("carpal_seg_1", 0.38, 0.54, 1.55, 1.30),
            ("carpal_seg_2", 0.52, 0.68, 1.45, 1.20),
        ]
        for lbl, u0, u1, thick_y, thick_z in mid_arch_steps:
            x0, y0, z0 = arch_curve(u0)
            x1, y1, z1 = arch_curve(u1)
            cx, cy, cz = 0.5 * (x0 + x1) * s, 0.5 * (y0 + y1), 0.5 * (z0 + z1)
            dx, dy = (x1 - x0), (y1 - y0)
            seg_len = math.hypot(dx, dy) + 1.1
            tilt_deg = math.degrees(math.atan2(dy, dx))
            frm = [cx - seg_len / 2.0, cy - thick_y / 2.0, cz - thick_z / 2.0]
            to = [cx + seg_len / 2.0, cy + thick_y / 2.0, cz + thick_z / 2.0]
            add_to_group(b_mid, make_cube(
                f"{b_mid}_{lbl}", frm, to, [cx, cy, cz], [0.0, 0.0, tilt_deg * s], UV_CORE
            ))

        # 4 Median Coverts shielding secondary roots (Zone: X = 9.85..15.0)
        for i in range(4):
            u = 0.40 + i * (0.24 / 3.0)
            x_abs, py, pz = arch_curve(u)
            px = x_abs * s
            total_len = 10.5 - i * 0.30
            wx = 4.80
            wz = 0.56
            rz_base = 20.0 + i * 2.8
            z_stagger = ((i % 2) * 2 - 1) * 0.14
            add_curved_feather(b_mid, f"{b_mid}_med_covert_{i+1}", px, py - 0.2, pz, wx, total_len, wz, rz_base, 6.0, s, z_stagger=z_stagger)

        # 6 Long Secondary Flight Feathers forming dense solid wing body (Zone: X = 9.85..15.7)
        for i in range(6):
            u = 0.38 + i * (0.28 / 5.0)
            x_abs, py, pz = arch_curve(u)
            px = x_abs * s
            total_len = 15.5 + math.sin(i / 5.0 * math.pi) * 3.0
            wx = 5.20
            wz = 0.55
            rz_base = 22.0 + i * 2.6
            z_stagger = ((i % 2) * 2 - 1) * 0.15
            add_curved_feather(b_mid, f"{b_mid}_secondary_{i+1}", px, py - 1.2, pz + 0.08, wx, total_len, wz, rz_base, 7.0, s, z_stagger=z_stagger)

        # --- C. OUTER PRIMARY FLIGHT FEATHERS (wing_*_tip, u: 0.68..1.00, pivot [±16.2, 35.4, 3.41]) ---
        tip_arch_steps = [
            ("tip_seg_1", 0.68, 0.84, 1.40, 1.15),
            ("tip_seg_2", 0.82, 1.00, 1.30, 1.05),
        ]
        for lbl, u0, u1, thick_y, thick_z in tip_arch_steps:
            x0, y0, z0 = arch_curve(u0)
            x1, y1, z1 = arch_curve(u1)
            cx, cy, cz = 0.5 * (x0 + x1) * s, 0.5 * (y0 + y1), 0.5 * (z0 + z1)
            dx, dy = (x1 - x0), (y1 - y0)
            seg_len = math.hypot(dx, dy) + 1.1
            tilt_deg = math.degrees(math.atan2(dy, dx))
            frm = [cx - seg_len / 2.0, cy - thick_y / 2.0, cz - thick_z / 2.0]
            to = [cx + seg_len / 2.0, cy + thick_y / 2.0, cz + thick_z / 2.0]
            add_to_group(b_tip, make_cube(
                f"{b_tip}_{lbl}", frm, to, [cx, cy, cz], [0.0, 0.0, tilt_deg * s], UV_CORE
            ))

        # 7 Sweeping Primary Feathers fanning gracefully from X = 16.2 to 23.0
        # Angles capped at 46 degrees (sweeping down and back, never pointing up/horizontal!)
        for i in range(7):
            u = 0.68 + i * (0.32 / 6.0)
            x_abs, py, pz = arch_curve(u)
            px = x_abs * s
            total_len = 16.5 + math.sin(i / 6.0 * math.pi) * 4.2
            wx = 5.00
            wz = 0.55
            rz_base = 34.0 + i * 2.0
            z_stagger = ((i % 2) * 2 - 1) * 0.16
            add_curved_feather(b_tip, f"{b_tip}_prim_{i+1}", px, py, pz + 0.12, wx, total_len, wz, rz_base, 6.0, s, z_stagger=z_stagger)

        # --- D. INNER TRAILING ANGEL PLUMAGE (wing_*_feathers, pivot [±4.5, 20.8, 2.10]) ---
        # Sealing dorsal back space behind the player (Zone: X = 1.2..7.5)
        for i in range(5):
            u = 0.04 + i * (0.28 / 4.0)
            x_abs, py, _ = arch_curve(u)
            px = x_abs * s
            total_len = 12.5 + i * 0.70
            wx = 4.20
            wz = 0.55
            rz_base = 12.0 + i * 2.2
            z_stagger = ((i % 2) * 2 - 1) * 0.10
            add_curved_feather(b_feathers, f"{b_feathers}_trail_{i+1}", px, py - 0.8, 2.10, wx, total_len, wz, rz_base, 6.0, s, z_stagger=z_stagger)
    # =========================================================================
    # 3. 3D SAKURA PETAL PARTICLES ON THE WINGS (petal_streams, pivot [0, 22.0, 2.4])
    # 24 Luminous petals swirling in 3D around the angel wings
    # =========================================================================
    petal_defs = [
        # Left wing (-X) petals
        ("petal_l1",  -6.5, 28.0, 2.1, 1.7, 1.15, 0.22,  18,  0, -38),
        ("petal_l2", -12.5, 34.5, 3.1, 1.8, 1.20, 0.22, -22,  0, -45),
        ("petal_l3", -18.5, 33.8, 2.8, 1.7, 1.15, 0.22,  20,  0, -52),
        ("petal_l4", -26.5, 31.0, 2.5, 1.9, 1.25, 0.22, -18,  0, -40),
        ("petal_l5", -35.5, 28.5, 2.7, 1.9, 1.25, 0.22,  25,  0, -48),
        ("petal_l6", -39.5, 23.5, 2.8, 1.8, 1.20, 0.22, -20,  0, -35),
        ("petal_l7", -36.5, 17.5, 2.9, 1.8, 1.20, 0.22,  18,  0, -28),
        ("petal_l8", -30.0, 12.8, 2.8, 1.7, 1.15, 0.22, -24,  0, -22),
        ("petal_l9", -22.5, 10.2, 2.7, 1.7, 1.15, 0.22,  22,  0, -18),
        ("petal_l10", -14.5, 9.0, 2.5, 1.6, 1.10, 0.22, -20,  0, -14),
        ("petal_l11", -9.5, 19.5, 2.1, 1.6, 1.10, 0.22,  15,  0, -42),
        ("petal_l12", -19.5, 22.0, 2.4, 1.7, 1.15, 0.22, -16,  0, -36),
        # Right wing (+X) petals (mirror symmetric)
        ("petal_r1",   6.5, 28.0, 2.1, 1.7, 1.15, 0.22,  18,  0,  38),
        ("petal_r2",  12.5, 34.5, 3.1, 1.8, 1.20, 0.22, -22,  0,  45),
        ("petal_r3",  18.5, 33.8, 2.8, 1.7, 1.15, 0.22,  20,  0,  52),
        ("petal_r4",  26.5, 31.0, 2.5, 1.9, 1.25, 0.22, -18,  0,  40),
        ("petal_r5",  35.5, 28.5, 2.7, 1.9, 1.25, 0.22,  25,  0,  48),
        ("petal_r6",  39.5, 23.5, 2.8, 1.8, 1.20, 0.22, -20,  0,  35),
        ("petal_r7",  36.5, 17.5, 2.9, 1.8, 1.20, 0.22,  18,  0,  28),
        ("petal_r8",  30.0, 12.8, 2.8, 1.7, 1.15, 0.22, -24,  0,  22),
        ("petal_r9",  22.5, 10.2, 2.7, 1.7, 1.15, 0.22,  22,  0,  18),
        ("petal_r10", 14.5,  9.0, 2.5, 1.6, 1.10, 0.22, -20,  0,  14),
        ("petal_r11",  9.5, 19.5, 2.1, 1.6, 1.10, 0.22,  15,  0,  42),
        ("petal_r12", 19.5, 22.0, 2.4, 1.7, 1.15, 0.22, -16,  0,  36),
    ]
    for lbl, cx, cy, cz, wx, wy, wz, rx, ry, rz in petal_defs:
        frm = [cx - wx / 2.0, cy - wy / 2.0, cz - wz / 2.0]
        to = [cx + wx / 2.0, cy + wy / 2.0, cz + wz / 2.0]
        add_to_group("petal_streams", make_cube(lbl, frm, to, [cx, cy, cz], [rx, ry, rz], UV_PETAL))

    # =========================================================================
    # 4. SHINIGAMI CAPTAIN PREVIEW MANNEQUIN WITH AUTHENTIC CHŪDAN-NO-KAMAE
    # =========================================================================
    add_to_group("player_body", make_cube("torso_shihakusho", [-4, 12, -2], [4, 24, 2], [0, 24, 0], [0, 0, 0], UV_ROBE))
    add_to_group("player_body", make_cube("captain_haori", [-4.35, 12.2, -2.25], [4.35, 24.1, 2.35], [0, 24, 0], [0, 0, 0], UV_HAORI))
    add_to_group("player_body", make_cube("haori_diamond_hem", [-4.4, 10.0, -2.3], [4.4, 12.4, 2.4], [0, 24, 0], [0, 0, 0], UV_HEM))
    add_to_group("player_body", make_cube("byakuya_scarf", [-4.5, 22.2, -2.5], [4.5, 24.6, 2.5], [0, 24, 0], [0, 0, 0], UV_SCARF))

    # Head + Kenseikan
    add_to_group("head", make_cube("captain_head", [-4, 24, -4], [4, 32, 4], [0, 24, 0], [0, 0, 0], UV_SKIN))
    add_to_group("head", make_cube("captain_hair", [-4.25, 24.5, -4.25], [4.25, 32.3, 4.25], [0, 24, 0], [0, 0, 0], UV_HAIR))
    add_to_group("head", make_cube("kenseikan_top", [-1.5, 32.0, -2.5], [1.5, 33.6, 0.5], [0, 24, 0], [0, 0, 0], UV_HAORI))
    add_to_group("head", make_cube("kenseikan_side_r", [-4.6, 26.0, -3.8], [-3.8, 30.5, -2.2], [0, 24, 0], [0, 0, 0], UV_HAORI))

    # Arms and Zanpakuto
    add_to_group("right_arm", make_cube("right_arm_mesh", [-8, 12, -2], [-4, 24, 2], [-5, 22, 0], [0, 0, 0], UV_ROBE))
    add_to_group("right_arm", make_cube("right_haori_sleeve", [-8.3, 17, -2.3], [-3.9, 24.2, 2.3], [-5, 22, 0], [0, 0, 0], UV_HAORI))
    add_to_group("right_arm", make_cube("zanpakuto_existing_tsuka", [-6.45, 10.5, -1.45], [-5.55, 16.5, -0.55], [-6, 12.5, -1.0], [10, 0, 0], UV_ZAN_WHITE))
    add_to_group("right_arm", make_cube("zanpakuto_existing_tsuba_frame", [-7.1, 10.0, -2.2], [-4.9, 10.6, 0.2], [-6, 12.5, -1.0], [10, 0, 0], UV_ZAN_WHITE))
    add_to_group("right_arm", make_cube("zanpakuto_existing_blade", [-6.25, -9.0, -1.35], [-5.75, 10.0, -0.65], [-6, 12.5, -1.0], [10, 0, 0], UV_ZAN_WHITE))

    add_to_group("left_arm", make_cube("left_arm_mesh", [4, 12, -2], [8, 24, 2], [5, 22, 0], [0, 0, 0], UV_ROBE))
    add_to_group("left_arm", make_cube("left_haori_sleeve", [3.9, 17, -2.3], [8.3, 24.2, 2.3], [5, 22, 0], [0, 0, 0], UV_HAORI))

    # Legs
    add_to_group("right_leg", make_cube("right_leg_hakama", [-4.1, 0, -2.1], [0.0, 12, 2.1], [-2, 12, 0], [0, 0, 0], UV_ROBE))
    add_to_group("left_leg", make_cube("left_leg_hakama", [0.0, 0, -2.1], [4.1, 12, 2.1], [2, 12, 0], [0, 0, 0], UV_ROBE))

    # =========================================================================
    # 5. OUTLINER HIERARCHY
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
        make_group("root", [0, 24, 0], [
            make_group("wing_nexus", [0, 21.0, 2.05], groups.get("wing_nexus", []) + [
                make_group("wing_left_root", [-1.8, 21.5, 2.05], groups.get("wing_left_root", []) + [
                    make_group("wing_left_mid", [-9.85, 32.7, 3.24], groups.get("wing_left_mid", []) + [
                        make_group("wing_left_tip", [-16.2, 35.4, 3.41], groups.get("wing_left_tip", []))
                    ]),
                    make_group("wing_left_feathers", [-4.5, 20.8, 2.10], groups.get("wing_left_feathers", []))
                ]),
                make_group("wing_right_root", [1.8, 21.5, 2.05], groups.get("wing_right_root", []) + [
                    make_group("wing_right_mid", [9.85, 32.7, 3.24], groups.get("wing_right_mid", []) + [
                        make_group("wing_right_tip", [16.2, 35.4, 3.41], groups.get("wing_right_tip", []))
                    ]),
                    make_group("wing_right_feathers", [4.5, 20.8, 2.10], groups.get("wing_right_feathers", []))
                ]),
                make_group("petal_streams", [0, 22.0, 2.4], groups.get("petal_streams", []))
            ]),
            make_group("player_body", [0, 21.0, 2.05], groups.get("player_body", []) + [
                make_group("head", [0, 24, 0], groups.get("head", [])),
                make_group("right_arm", [-5, 22, 0], groups.get("right_arm", [])),
                make_group("left_arm", [5, 22, 0], groups.get("left_arm", [])),
                make_group("right_leg", [-2, 12, 0], groups.get("right_leg", [])),
                make_group("left_leg", [2, 12, 0], groups.get("left_leg", []))
            ])
        ])
    ]

    return elements, outliner


_COUNTER = 0
def uid_counter():
    global _COUNTER
    _COUNTER += 1
    return _COUNTER


def make_kf(channel, time_s, x, y, z, interp="catmullrom"):
    return {
        "channel": channel,
        "data_points": [{"x": str(round(x, 3)), "y": str(round(y, 3)), "z": str(round(z, 3))}],
        "uuid": make_uuid(f"kf.{channel}.{time_s}.{x}.{y}.{z}.{uid_counter()}"),
        "time": round(float(time_s), 4),
        "color": -1,
        "interpolation": interp
    }


def build_animations():
    """Builds animations with strictly locked yaw (ry = 0.0) on root and nexus bones.
    Eliminates backward swinging in Z and diagonal yaw drift!"""
    def anim_bone(b_name, kfs):
        return make_uuid(f"group.{b_name}"), {
            "name": b_name,
            "type": "bone",
            "keyframes": kfs
        }

    # -------------------------------------------------------------------------
    # 1. IDLE (2.0s loop): Active Hakuteiken winged transformation wingbeat
    # -------------------------------------------------------------------------
    idle_animators = dict([
        anim_bone("wing_nexus", [
            make_kf("position", 0.0, 0, 0.0, 0),
            make_kf("position", 1.0, 0, 0.15, 0),
            make_kf("position", 2.0, 0, 0.0, 0),
            make_kf("rotation", 0.0, 0, 0, 0),
            make_kf("rotation", 2.0, 0, 0, 0),
        ]),
        anim_bone("wing_left_root", [
            make_kf("rotation", 0.0, 1.2, 0.0, -5.0),
            make_kf("rotation", 0.5, -0.8, 0.0, 3.5),
            make_kf("rotation", 1.0, -2.0, 0.0, 7.0),
            make_kf("rotation", 1.5, 0.4, 0.0, 1.5),
            make_kf("rotation", 2.0, 1.2, 0.0, -5.0),
        ]),
        anim_bone("wing_right_root", [
            make_kf("rotation", 0.0, 1.2, 0.0, 5.0),
            make_kf("rotation", 0.5, -0.8, 0.0, -3.5),
            make_kf("rotation", 1.0, -2.0, 0.0, -7.0),
            make_kf("rotation", 1.5, 0.4, 0.0, -1.5),
            make_kf("rotation", 2.0, 1.2, 0.0, 5.0),
        ]),
        anim_bone("wing_left_mid", [
            make_kf("rotation", 0.0, 0.8, 0.0, -8.0),
            make_kf("rotation", 0.5, 1.5, 0.0, -1.5),
            make_kf("rotation", 1.0, -1.2, 0.0, 10.0),
            make_kf("rotation", 1.5, -1.5, 0.0, 3.0),
            make_kf("rotation", 2.0, 0.8, 0.0, -8.0),
        ]),
        anim_bone("wing_right_mid", [
            make_kf("rotation", 0.0, 0.8, 0.0, 8.0),
            make_kf("rotation", 0.5, 1.5, 0.0, 1.5),
            make_kf("rotation", 1.0, -1.2, 0.0, -10.0),
            make_kf("rotation", 1.5, -1.5, 0.0, -3.0),
            make_kf("rotation", 2.0, 0.8, 0.0, 8.0),
        ]),
        anim_bone("wing_left_tip", [
            make_kf("rotation", 0.0, -0.8, 0.0, -12.0),
            make_kf("rotation", 0.5, 1.2, 0.0, -3.0),
            make_kf("rotation", 1.0, 0.8, 0.0, 12.0),
            make_kf("rotation", 1.5, -1.2, 0.0, 6.0),
            make_kf("rotation", 2.0, -0.8, 0.0, -12.0),
        ]),
        anim_bone("wing_right_tip", [
            make_kf("rotation", 0.0, -0.8, 0.0, 12.0),
            make_kf("rotation", 0.5, 1.2, 0.0, 3.0),
            make_kf("rotation", 1.0, 0.8, 0.0, -12.0),
            make_kf("rotation", 1.5, -1.2, 0.0, -6.0),
            make_kf("rotation", 2.0, -0.8, 0.0, 12.0),
        ]),
        anim_bone("wing_left_feathers", [
            make_kf("rotation", 0.0, 0.8, 0.0, -3.5),
            make_kf("rotation", 1.0, -0.8, 0.0, 4.0),
            make_kf("rotation", 2.0, 0.8, 0.0, -3.5),
        ]),
        anim_bone("wing_right_feathers", [
            make_kf("rotation", 0.0, 0.8, 0.0, 3.5),
            make_kf("rotation", 1.0, -0.8, 0.0, -4.0),
            make_kf("rotation", 2.0, 0.8, 0.0, 3.5),
        ]),
        anim_bone("petal_streams", [
            make_kf("position", 0.0, 0, -0.5, 0),
            make_kf("position", 1.0, 0, 0.8, -0.15),
            make_kf("position", 2.0, 0, -0.5, 0),
            make_kf("rotation", 0.0, -2, 0, 0),
            make_kf("rotation", 1.0, 2, 0, 0),
            make_kf("rotation", 2.0, -2, 0, 0),
        ]),
    ])

    # -------------------------------------------------------------------------
    # 2. WINDUP (1.5s total): 0..0.5s Leap Ascent -> 0.5..1.5s Angel Hover at 5m
    # -------------------------------------------------------------------------
    windup_animators = dict([
        anim_bone("wing_nexus", [
            make_kf("scale", 0.0, 0.90, 0.90, 0.90),
            make_kf("scale", 0.45, 1.04, 1.04, 1.04),
            make_kf("scale", 1.0, 1.06, 1.06, 1.06),
            make_kf("scale", 1.5, 1.0, 1.0, 1.0),
            make_kf("rotation", 0.0, 3, 0, 0),
            make_kf("rotation", 0.5, -6, 0, 0),
            make_kf("rotation", 1.0, -2, 0, 0),
            make_kf("rotation", 1.5, 10, 0, 0),
        ]),
        anim_bone("wing_left_root", [
            make_kf("rotation", 0.0, 4, 0.0, 8),
            make_kf("rotation", 0.25, 3, 0.0, -14),
            make_kf("rotation", 0.5, -3, 0.0, 22),
            make_kf("rotation", 1.0, -2, 0.0, 26),
            make_kf("rotation", 1.5, 0, 0.0, 12),
        ]),
        anim_bone("wing_right_root", [
            make_kf("rotation", 0.0, 4, 0.0, -8),
            make_kf("rotation", 0.25, 3, 0.0, 14),
            make_kf("rotation", 0.5, -3, 0.0, -22),
            make_kf("rotation", 1.0, -2, 0.0, -26),
            make_kf("rotation", 1.5, 0, 0.0, -12),
        ]),
        anim_bone("wing_left_mid", [
            make_kf("rotation", 0.0, 3, 0.0, 12),
            make_kf("rotation", 0.28, 2, 0.0, -10),
            make_kf("rotation", 0.55, -2, 0.0, 18),
            make_kf("rotation", 1.0, -1, 0.0, 22),
            make_kf("rotation", 1.5, 1, 0.0, 10),
        ]),
        anim_bone("wing_right_mid", [
            make_kf("rotation", 0.0, 3, 0.0, -12),
            make_kf("rotation", 0.28, 2, 0.0, 10),
            make_kf("rotation", 0.55, -2, 0.0, -18),
            make_kf("rotation", 1.0, -1, 0.0, -22),
            make_kf("rotation", 1.5, 1, 0.0, -10),
        ]),
        anim_bone("wing_left_tip", [
            make_kf("rotation", 0.0, 2, 0.0, 10),
            make_kf("rotation", 0.32, 1, 0.0, -8),
            make_kf("rotation", 0.6, -1, 0.0, 20),
            make_kf("rotation", 1.0, 0, 0.0, 24),
            make_kf("rotation", 1.5, 2, 0.0, 10),
        ]),
        anim_bone("wing_right_tip", [
            make_kf("rotation", 0.0, 2, 0.0, -10),
            make_kf("rotation", 0.32, 1, 0.0, 8),
            make_kf("rotation", 0.6, -1, 0.0, -20),
            make_kf("rotation", 1.0, 0, 0.0, -24),
            make_kf("rotation", 1.5, 2, 0.0, -10),
        ]),
        anim_bone("wing_left_feathers", [
            make_kf("rotation", 0.0, 2, 0.0, 5),
            make_kf("rotation", 0.5, -2, 0.0, 10),
            make_kf("rotation", 1.0, -1, 0.0, 12),
            make_kf("rotation", 1.5, 1, 0.0, 5),
        ]),
        anim_bone("wing_right_feathers", [
            make_kf("rotation", 0.0, 2, 0.0, -5),
            make_kf("rotation", 0.5, -2, 0.0, -10),
            make_kf("rotation", 1.0, -1, 0.0, -12),
            make_kf("rotation", 1.5, 1, 0.0, -5),
        ]),
        anim_bone("petal_streams", [
            make_kf("scale", 0.0, 0.75, 0.75, 0.75),
            make_kf("scale", 0.5, 1.20, 1.20, 1.20),
            make_kf("scale", 1.0, 1.30, 1.30, 1.30),
            make_kf("scale", 1.5, 1.02, 1.02, 1.02),
            make_kf("position", 0.0, 0, -1.2, -0.2),
            make_kf("position", 0.5, 0, 1.2, 0.2),
            make_kf("position", 1.0, 0, 1.5, 0.3),
            make_kf("position", 1.5, 0, 0.4, 0.0),
        ]),
    ])

    # -------------------------------------------------------------------------
    # 3. DASH (1.0s hold): Aerodynamic swept-back V-dive (0..0.45s) -> Ground flare
    # Wings pull BACKWARD and up like a diving falcon, NEVER covering the head!
    # -------------------------------------------------------------------------
    dash_animators = dict([
        anim_bone("wing_nexus", [
            make_kf("rotation", 0.0, -8, 0, 0),
            make_kf("rotation", 0.2, -12, 0, 0),
            make_kf("rotation", 0.45, -12, 0, 0),
            make_kf("rotation", 0.62, 6, 0, 0),
            make_kf("rotation", 0.85, 0, 0, 0),
        ]),
        anim_bone("wing_left_root", [
            make_kf("rotation", 0.0, -6, 0.0, -14),
            make_kf("rotation", 0.16, -10, 0.0, -26),
            make_kf("rotation", 0.42, -8, 0.0, -22),
            make_kf("rotation", 0.60, 4, 0.0, 16),
            make_kf("rotation", 1.0, 2, 0.0, 4),
        ]),
        anim_bone("wing_right_root", [
            make_kf("rotation", 0.0, -6, 0.0, 14),
            make_kf("rotation", 0.16, -10, 0.0, 26),
            make_kf("rotation", 0.42, -8, 0.0, 22),
            make_kf("rotation", 0.60, 4, 0.0, -16),
            make_kf("rotation", 1.0, 2, 0.0, -4),
        ]),
        anim_bone("wing_left_mid", [
            make_kf("rotation", 0.0, -4, 0.0, -12),
            make_kf("rotation", 0.18, -8, 0.0, -20),
            make_kf("rotation", 0.44, -6, 0.0, -16),
            make_kf("rotation", 0.64, 4, 0.0, 12),
            make_kf("rotation", 1.0, 2, 0.0, 6),
        ]),
        anim_bone("wing_right_mid", [
            make_kf("rotation", 0.0, -4, 0.0, 12),
            make_kf("rotation", 0.18, -8, 0.0, 20),
            make_kf("rotation", 0.44, -6, 0.0, 16),
            make_kf("rotation", 0.64, 4, 0.0, -12),
            make_kf("rotation", 1.0, 2, 0.0, -6),
        ]),
        anim_bone("wing_left_tip", [
            make_kf("rotation", 0.0, -3, 0.0, -12),
            make_kf("rotation", 0.2, -6, 0.0, -16),
            make_kf("rotation", 0.46, -4, 0.0, -14),
            make_kf("rotation", 0.68, 3, 0.0, 10),
            make_kf("rotation", 1.0, 0, 0.0, 6),
        ]),
        anim_bone("wing_right_tip", [
            make_kf("rotation", 0.0, -3, 0.0, 12),
            make_kf("rotation", 0.2, -6, 0.0, 16),
            make_kf("rotation", 0.46, -4, 0.0, 14),
            make_kf("rotation", 0.68, 3, 0.0, -10),
            make_kf("rotation", 1.0, 0, 0.0, -6),
        ]),
        anim_bone("wing_left_feathers", [
            make_kf("rotation", 0.0, -3, 0.0, -8),
            make_kf("rotation", 0.42, -5, 0.0, -10),
            make_kf("rotation", 0.64, 3, 0.0, 8),
            make_kf("rotation", 1.0, 1, 0.0, 3),
        ]),
        anim_bone("wing_right_feathers", [
            make_kf("rotation", 0.0, -3, 0.0, 8),
            make_kf("rotation", 0.42, -5, 0.0, 10),
            make_kf("rotation", 0.64, 3, 0.0, -8),
            make_kf("rotation", 1.0, 1, 0.0, -3),
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
            "length": 1.5,
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