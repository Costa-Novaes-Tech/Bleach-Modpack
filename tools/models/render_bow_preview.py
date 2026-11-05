"""Render 3D preview images of the repaired Quincy Kojaku bow from its JSON block models."""
import json
import math
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[2]
MODELS_DIR = ROOT / "src/main/resources/assets/bleachmod/models/item"
TEX_DIR = ROOT / "src/main/resources/assets/bleachmod/textures/item"
QA_DIR = ROOT / "Docs/poderes/qa/quincy-correcao-20261028"
QA_DIR.mkdir(parents=True, exist_ok=True)

def rotate_point(pt, origin, axis, angle_deg):
    rad = math.radians(angle_deg)
    cos_a = math.cos(rad)
    sin_a = math.sin(rad)
    x, y, z = pt[0] - origin[0], pt[1] - origin[1], pt[2] - origin[2]
    if axis == 'x':
        ny = y * cos_a - z * sin_a
        nz = y * sin_a + z * cos_a
        return (origin[0] + x, origin[1] + ny, origin[2] + nz)
    elif axis == 'y':
        nx = x * cos_a + z * sin_a
        nz = -x * sin_a + z * cos_a
        return (origin[0] + nx, origin[1] + y, origin[2] + nz)
    elif axis == 'z':
        nx = x * cos_a - y * sin_a
        ny = x * sin_a + y * cos_a
        return (origin[0] + nx, origin[1] + ny, origin[2] + z)
    return pt

def get_cube_vertices(elem):
    f = elem["from"]
    t = elem["to"]
    verts = [
        (f[0], f[1], f[2]), # 0
        (t[0], f[1], f[2]), # 1
        (t[0], t[1], f[2]), # 2
        (f[0], t[1], f[2]), # 3
        (f[0], f[1], t[2]), # 4
        (t[0], f[1], t[2]), # 5
        (t[0], t[1], t[2]), # 6
        (f[0], t[1], t[2]), # 7
    ]
    if "rotation" in elem:
        rot = elem["rotation"]
        orig = rot["origin"]
        ax = rot["axis"]
        ang = rot["angle"]
        verts = [rotate_point(v, orig, ax, ang) for v in verts]
    return verts

def render_model(model_path, out_path, yaw=35, pitch=-20, width=512, height=512, zoom=12.0):
    data = json.loads(model_path.read_text(encoding="utf-8"))
    elements = data.get("elements", [])

    img = Image.new("RGBA", (width, height), (26, 30, 42, 255))
    draw = ImageDraw.Draw(img)

    yaw_rad = math.radians(yaw)
    pitch_rad = math.radians(pitch)

    faces = [
        (0, 1, 2, 3, (120, 220, 255, 230)), # North (-Z)
        (5, 4, 7, 6, (90, 180, 240, 230)),  # South (+Z)
        (4, 0, 3, 7, (70, 150, 220, 230)),  # West (-X)
        (1, 5, 6, 2, (140, 240, 255, 240)), # East (+X)
        (3, 2, 6, 7, (200, 245, 255, 255)), # Up (+Y)
        (4, 5, 1, 0, (60, 130, 200, 230)),  # Down (-Y)
    ]

    all_polys = []

    for elem in elements:
        verts = get_cube_vertices(elem)
        name = elem.get("name", "")

        # Distinct colors for grip, cross, arrow, and string
        color_override = None
        if "cross" in name:
            color_override = (255, 255, 255, 255)
        elif "grip" in name:
            color_override = (50, 90, 160, 255)
        elif "arrow" in name:
            color_override = (230, 255, 255, 255)
        elif "string" in name:
            color_override = (160, 240, 255, 200)

        for v0, v1, v2, v3, face_col in faces:
            poly3d = [verts[v0], verts[v1], verts[v2], verts[v3]]
            # Transform by yaw and pitch
            t_poly = []
            avg_z = 0
            for vx, vy, vz in poly3d:
                # Center around (8, 8, 8)
                cx, cy, cz = vx - 8.0, vy - 8.0, vz - 8.0
                # Yaw (around Y)
                rx = cx * math.cos(yaw_rad) + cz * math.sin(yaw_rad)
                rz = -cx * math.sin(yaw_rad) + cz * math.cos(yaw_rad)
                # Pitch (around X)
                ry = cy * math.cos(pitch_rad) - rz * math.sin(pitch_rad)
                final_z = cy * math.sin(pitch_rad) + rz * math.cos(pitch_rad)
                avg_z += final_z
                sx = width / 2 + rx * zoom
                sy = height / 2 - ry * zoom
                t_poly.append((sx, sy))
            avg_z /= 4.0
            col = color_override if color_override else face_col
            all_polys.append((avg_z, t_poly, col))

    # Painter's algorithm
    all_polys.sort(key=lambda item: item[0])

    for _, poly2d, col in all_polys:
        draw.polygon(poly2d, fill=col, outline=(30, 60, 110, 200))

    img.save(out_path)
    print(f"Rendered {out_path.name}")

def make_comparison_sheet():
    idle_path = QA_DIR / "quincy_bow_idle_preview.png"
    pull_path = QA_DIR / "quincy_bow_pulling_preview.png"
    render_model(MODELS_DIR / "quincy_bow.json", idle_path, yaw=30, pitch=-15, zoom=11.0)
    render_model(MODELS_DIR / "quincy_bow_pulling_2.json", pull_path, yaw=30, pitch=-15, zoom=11.0)

    # Make side-by-side comparison sheet
    w, h = 1024, 512
    sheet = Image.new("RGBA", (w, h), (18, 22, 32, 255))
    draw = ImageDraw.Draw(sheet)

    idle_img = Image.open(idle_path)
    pull_img = Image.open(pull_path)

    sheet.paste(idle_img, (0, 0))
    sheet.paste(pull_img, (512, 0))

    draw.text((30, 25), "Quincy Kojaku Spirit Bow - Idle (Valid -22.5/22.5 Angles)", fill=(255, 255, 255, 255))
    draw.text((542, 25), "Quincy Kojaku Spirit Bow - Pull Stage 2 (Nocked Arrow)", fill=(255, 255, 255, 255))

    comp_path = QA_DIR / "quincy_bow_model_preview.png"
    sheet.save(comp_path)
    print(f"Created comparison sheet {comp_path}")

if __name__ == "__main__":
    make_comparison_sheet()
