"""
Composes side-by-side comparison images between 137 Storyboard Schematics and real Minecraft Forge captures.
"""

import json
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[2]
STORYBOARD_FRAMES = ROOT / "Docs/poderes/media/137-storyboards/frames"
QA_DIR = ROOT / "Docs/poderes/qa/cycle138"
QA_DIR.mkdir(parents=True, exist_ok=True)

# Latest capture folders in run-benihime-qa
RUN_DIR = ROOT / "run-benihime-qa/base/senbonzakura-combat-qa"
candidates = sorted([d for d in RUN_DIR.iterdir() if d.is_dir() and "combo" in d.name], key=lambda d: d.stat().st_mtime, reverse=True)

front_dir = next(d for d in candidates if "01_combo_front" in d.name) / "screenshots"
profile_dir = next(d for d in candidates if "02_combo_profile" in d.name) / "screenshots"
back_dir = next(d for d in candidates if "03_combo_back" in d.name) / "screenshots"

print(f"Using front: {front_dir}")
print(f"Using profile: {profile_dir}")
print(f"Using back: {back_dir}")

MAPPING = [
    {"stage": "S1", "frame_id": "S1-F02", "tick_file": "frame-t038.png", "title": "S1-F02: Preparacao Alta (Ombro Direito / Fora do Rosto)"},
    {"stage": "S1", "frame_id": "S1-F04", "tick_file": "frame-t042.png", "title": "S1-F04: Travessia Diagonal pelo Centro"},
    {"stage": "S1", "frame_id": "S1-F06", "tick_file": "frame-t044.png", "title": "S1-F06 / S2-F00: Final Baixo Esquerdo (Seam 1->2)"},
    {"stage": "S2", "frame_id": "S2-F04", "tick_file": "frame-t050.png", "title": "S2-F04: Varredura Horizontal no Plano do Peito"},
    {"stage": "S2", "frame_id": "S2-F06", "tick_file": "frame-t056.png", "title": "S2-F06 / S3-F00: Recolha na Costela Direita (Seam 2->3)"},
    {"stage": "S3", "frame_id": "S3-F03", "tick_file": "frame-t064.png", "title": "S3-F03: Extensao Linear Frontal da Estocada"},
    {"stage": "S3", "frame_id": "S3-F04", "tick_file": "frame-t070.png", "title": "S3-F04: Contato Frontal na Mira Central"},
    {"stage": "S3", "frame_id": "S3-F06", "tick_file": "frame-t076.png", "title": "S3-F06 / S1-F00: Retorno a Guarda Nobre (Loop Fechado)"}
]

FONT_PATH = "C:/Windows/Fonts/arial.ttf"
font_title = ImageFont.truetype(FONT_PATH, 20)
font_label = ImageFont.truetype(FONT_PATH, 16)

comparisons_data = []

for item in MAPPING:
    schematic_path = STORYBOARD_FRAMES / f"{item['frame_id']}.png"
    front_path = front_dir / item['tick_file']
    profile_path = profile_dir / item['tick_file']
    
    if not schematic_path.exists() or not front_path.exists() or not profile_path.exists():
        print(f"Skipping {item['frame_id']}: missing files")
        continue
        
    im_schem = Image.open(schematic_path).convert("RGB")
    im_front = Image.open(front_path).convert("RGB")
    im_prof = Image.open(profile_path).convert("RGB")
    
    # Target height 540
    target_h = 540
    # schematic is 740x540
    # captures are 1280x720 -> resize to fit h=540
    scale_c = target_h / im_front.height
    w_cap = int(im_front.width * scale_c)
    
    im_front_res = im_front.resize((w_cap, target_h), Image.Resampling.LANCZOS)
    im_prof_res = im_prof.resize((w_cap, target_h), Image.Resampling.LANCZOS)
    
    # Combined canvas: Header 50px + 3 columns: Schematic (740) + Front (w_cap) + Profile (w_cap)
    total_w = im_schem.width + w_cap * 2
    total_h = target_h + 60
    
    canvas = Image.new("RGB", (total_w, total_h), "#0d1117")
    draw = ImageDraw.Draw(canvas)
    
    draw.text((20, 15), f"COMPARAÇÃO: {item['title']}", font=font_title, fill="#ffffff")
    draw.text((total_w - 450, 18), "Referência Storyboard 137 vs Captura Forge Real (VFX off)", font=font_label, fill="#58a6ff")
    
    # Paste
    x_offset = 0
    canvas.paste(im_schem, (x_offset, 60))
    x_offset += im_schem.width
    canvas.paste(im_front_res, (x_offset, 60))
    x_offset += w_cap
    canvas.paste(im_prof_res, (x_offset, 60))
    
    # Column labels
    draw.rectangle([0, 60, im_schem.width, 90], fill=(0, 0, 0, 180))
    draw.text((10, 65), f"Storyboard 137: {item['frame_id']} (Meta Autoral)", font=font_label, fill="#7ee787")
    
    draw.rectangle([im_schem.width, 60, im_schem.width + w_cap, 90], fill=(0, 0, 0, 180))
    draw.text((im_schem.width + 10, 65), f"Forge Real: Frente ({item['tick_file']})", font=font_label, fill="#ffa657")
    
    draw.rectangle([im_schem.width + w_cap, 60, total_w, 90], fill=(0, 0, 0, 180))
    draw.text((im_schem.width + w_cap + 10, 65), f"Forge Real: Perfil ({item['tick_file']})", font=font_label, fill="#d2a8ff")
    
    out_file = QA_DIR / f"comparativo-{item['frame_id']}.png"
    canvas.save(out_file)
    print(f"Saved comparison composite: {out_file.name}")
    
    comparisons_data.append({
        "frame_id": item['frame_id'],
        "title": item['title'],
        "tick_file": item['tick_file'],
        "composite_png": str(out_file.relative_to(ROOT)).replace("\\", "/")
    })

(QA_DIR / "manifest.json").write_text(json.dumps(comparisons_data, indent=2), encoding="utf-8")
print("Manifest saved in cycle138.")
