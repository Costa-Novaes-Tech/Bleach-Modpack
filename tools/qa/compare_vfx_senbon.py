"""
Generates comparison images comparing Without VFX vs With VFX.
"""

from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[2]
QA_DIR = ROOT / "Docs/poderes/qa/cycle138"
QA_DIR.mkdir(parents=True, exist_ok=True)

RUN_DIR = ROOT / "run-benihime-qa/base/senbonzakura-combat-qa"

# No VFX run folders
no_vfx_front = RUN_DIR / "1793106885634-01_combo_front/screenshots"
no_vfx_prof = RUN_DIR / "1793106892588-02_combo_profile/screenshots"

# With VFX run folders
vfx_front = RUN_DIR / "1793107180901-01_combo_front/screenshots"
vfx_prof = RUN_DIR / "1793107187892-02_combo_profile/screenshots"

MOMENTS = [
    ("frame-t038.png", "Golpe 1 (Diagonal Descendente) - Preparacao Alta"),
    ("frame-t042.png", "Golpe 1 (Diagonal Descendente) - Corte Travessia"),
    ("frame-t050.png", "Golpe 2 (Retorno Horizontal) - Varredura Peito"),
    ("frame-t056.png", "Golpe 2 (Retorno) - Recolha Costela / Pre-Estocada"),
    ("frame-t064.png", "Golpe 3 (Estocada Central) - Extensao Linear"),
    ("frame-t070.png", "Golpe 3 (Estocada Central) - Contato Mira")
]

FONT_PATH = "C:/Windows/Fonts/arial.ttf"
font_title = ImageFont.truetype(FONT_PATH, 20)
font_label = ImageFont.truetype(FONT_PATH, 16)

for fname, title in MOMENTS:
    f_novfx = no_vfx_front / fname
    f_vfx = vfx_front / fname
    p_vfx = vfx_prof / fname
    
    if not (f_novfx.exists() and f_vfx.exists() and p_vfx.exists()):
        continue
        
    im1 = Image.open(f_novfx).convert("RGB")
    im2 = Image.open(f_vfx).convert("RGB")
    im3 = Image.open(p_vfx).convert("RGB")
    
    target_h = 480
    w = int(im1.width * (target_h / im1.height))
    
    im1_r = im1.resize((w, target_h), Image.Resampling.LANCZOS)
    im2_r = im2.resize((w, target_h), Image.Resampling.LANCZOS)
    im3_r = im3.resize((w, target_h), Image.Resampling.LANCZOS)
    
    total_w = w * 3
    total_h = target_h + 50
    canvas = Image.new("RGB", (total_w, total_h), "#0f141c")
    draw = ImageDraw.Draw(canvas)
    
    draw.text((20, 12), f"VALIDAÇÃO VFX: {title} ({fname})", font=font_title, fill="#ffffff")
    
    canvas.paste(im1_r, (0, 50))
    canvas.paste(im2_r, (w, 50))
    canvas.paste(im3_r, (w*2, 50))
    
    draw.rectangle([0, 50, w, 80], fill=(0,0,0,180))
    draw.text((10, 55), "Frente (Sem VFX / Arma Limpa)", font=font_label, fill="#ffa657")
    
    draw.rectangle([w, 50, w*2, 80], fill=(0,0,0,180))
    draw.text((w + 10, 55), "Frente (Com VFX / Rastro Alinhado)", font=font_label, fill="#7ee787")
    
    draw.rectangle([w*2, 50, total_w, 80], fill=(0,0,0,180))
    draw.text((w*2 + 10, 55), "Perfil (Com VFX / Rastro)", font=font_label, fill="#d2a8ff")
    
    out_name = QA_DIR / f"vfx-validacao-{fname}"
    canvas.save(out_name)
    print(f"Saved: {out_name.name}")

print("VFX comparisons completed.")
