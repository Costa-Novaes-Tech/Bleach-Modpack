import json
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[2]
QA_DIR = ROOT / "Docs/poderes/qa/cycle142"
QA_DIR.mkdir(parents=True, exist_ok=True)

PROFILE_DIR = list((ROOT / "run-benihime-qa/base/senbonzakura-combat-qa").glob("*02_combo_profile"))[-1]
FRONT_DIR = list((ROOT / "run-benihime-qa/base/senbonzakura-combat-qa").glob("*01_combo_front"))[-1]
BACK_DIR = list((ROOT / "run-benihime-qa/base/senbonzakura-combat-qa").glob("*03_combo_back"))[-1]

CODEX_141_PROFILE = Path("C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/Docs/poderes/qa/cycle141/senbon/02_combo_profile")

STORYBOARD_DIR = ROOT / "Docs/poderes/media/137-storyboards/frames"

def get_font(size):
    try:
        return ImageFont.truetype("arial.ttf", size)
    except:
        return ImageFont.load_default()

def create_preparacao_composite():
    # 3-panel: S1-F02 reference, 141 failure, 142 fix
    im_ref = Image.open(STORYBOARD_DIR / "S1-F02.png").convert("RGB")
    im_141 = Image.open(CODEX_141_PROFILE / "frame-t033.png").convert("RGB")
    im_142 = Image.open(PROFILE_DIR / "screenshots/frame-t033.png").convert("RGB")

    # Crop player in game screenshots: (440, 80, 840, 680) -> 400x600
    crop_box = (440, 80, 840, 680)
    c_141 = im_141.crop(crop_box)
    c_142 = im_142.crop(crop_box)

    # Resize reference to match height 600
    w_ref = int(im_ref.width * (600 / im_ref.height))
    c_ref = im_ref.resize((w_ref, 600), Image.Resampling.LANCZOS)

    # Total width = w_ref + 400 + 400 + padding (20*4)
    canvas_w = w_ref + 800 + 60
    canvas_h = 700
    canvas = Image.new("RGB", (canvas_w, canvas_h), (24, 26, 32))
    draw = ImageDraw.Draw(canvas)
    font_title = get_font(20)
    font_sub = get_font(13)

    # Paste panels
    x = 15
    canvas.paste(c_ref, (x, 70))
    draw.text((x, 15), "1. Referência Autoral 137 (S1-F02)", fill=(240, 240, 240), font=font_title)
    draw.text((x, 42), "Ponta alta, fora do rosto, mão no ombro direito", fill=(180, 200, 220), font=font_sub)

    x += w_ref + 15
    canvas.paste(c_141, (x, 70))
    draw.rectangle([x, 70, x + 400, 670], outline=(220, 60, 60), width=3)
    draw.text((x, 15), "2. Ciclo 141 (REPROVADO)", fill=(255, 100, 100), font=font_title)
    draw.text((x, 42), "Lâmina baixa/trás (rot=[-0.15, 0.25, -1.55])", fill=(255, 160, 160), font=font_sub)

    x += 400 + 15
    canvas.paste(c_142, (x, 70))
    draw.rectangle([x, 70, x + 400, 670], outline=(60, 220, 120), width=3)
    draw.text((x, 15), "3. Ciclo 142 (CORRIGIDO)", fill=(100, 255, 140), font=font_title)
    draw.text((x, 42), "Ponta elevada (rot=[-2.86, 0.10, -2.56], erro=0.003)", fill=(160, 255, 180), font=font_sub)

    out_path = QA_DIR / "diagonal-preparacao-reference-vs-forge.png"
    canvas.save(out_path)
    print(f"Saved: {out_path}")

def get_closest_frame(dir_path, target_tick):
    screenshots_dir = dir_path / "screenshots"
    frames = list(screenshots_dir.glob("frame-t*.png"))
    if not frames:
        raise FileNotFoundError(f"No frames in {screenshots_dir}")
    def tick_from_path(p):
        name = p.stem # 'frame-t033'
        return int(name.replace("frame-t", ""))
    closest = min(frames, key=lambda p: abs(tick_from_path(p) - target_tick))
    return closest, tick_from_path(closest)

def create_travessia_composite():
    # S1-F04 reference vs Front & Profile frame around tick 38
    im_ref = Image.open(STORYBOARD_DIR / "S1-F04.png").convert("RGB")
    front_path, front_tick = get_closest_frame(FRONT_DIR, 38)
    prof_path, prof_tick = get_closest_frame(PROFILE_DIR, 38)
    im_front = Image.open(front_path).convert("RGB")
    im_prof = Image.open(prof_path).convert("RGB")

    crop_box = (440, 80, 840, 680)
    c_front = im_front.crop(crop_box)
    c_prof = im_prof.crop(crop_box)

    w_ref = int(im_ref.width * (600 / im_ref.height))
    c_ref = im_ref.resize((w_ref, 600), Image.Resampling.LANCZOS)

    canvas_w = w_ref + 800 + 60
    canvas_h = 700
    canvas = Image.new("RGB", (canvas_w, canvas_h), (24, 26, 32))
    draw = ImageDraw.Draw(canvas)
    font_title = get_font(20)
    font_sub = get_font(13)

    x = 15
    canvas.paste(c_ref, (x, 70))
    draw.text((x, 15), "1. Referência Autoral 137 (S1-F04)", fill=(240, 240, 240), font=font_title)
    draw.text((x, 42), "Travessia pelo centro, corte cruzando o tórax", fill=(180, 200, 220), font=font_sub)

    x += w_ref + 15
    canvas.paste(c_front, (x, 70))
    draw.rectangle([x, 70, x + 400, 670], outline=(60, 220, 120), width=3)
    draw.text((x, 15), f"2. Jogo Forge (Vista Frontal t={front_tick})", fill=(100, 255, 140), font=font_title)
    draw.text((x, 42), "Lâmina cruzando diagonalmente a linha média", fill=(160, 255, 180), font=font_sub)

    x += 400 + 15
    canvas.paste(c_prof, (x, 70))
    draw.rectangle([x, 70, x + 400, 670], outline=(60, 220, 120), width=3)
    draw.text((x, 15), f"3. Jogo Forge (Vista Perfil t={prof_tick})", fill=(100, 255, 140), font=font_title)
    draw.text((x, 42), "Extensão frontal no plano de ataque", fill=(160, 255, 180), font=font_sub)

    out_path = QA_DIR / "diagonal-travessia-centro-reference-vs-forge.png"
    canvas.save(out_path)
    print(f"Saved: {out_path}")

def create_final_composite():
    # S1-F06 reference vs Front & Profile frame around tick 42
    im_ref = Image.open(STORYBOARD_DIR / "S1-F06.png").convert("RGB")
    front_path, front_tick = get_closest_frame(FRONT_DIR, 42)
    prof_path, prof_tick = get_closest_frame(PROFILE_DIR, 42)
    im_front = Image.open(front_path).convert("RGB")
    im_prof = Image.open(prof_path).convert("RGB")

    crop_box = (440, 80, 840, 680)
    c_front = im_front.crop(crop_box)
    c_prof = im_prof.crop(crop_box)

    w_ref = int(im_ref.width * (600 / im_ref.height))
    c_ref = im_ref.resize((w_ref, 600), Image.Resampling.LANCZOS)

    canvas_w = w_ref + 800 + 60
    canvas_h = 700
    canvas = Image.new("RGB", (canvas_w, canvas_h), (24, 26, 32))
    draw = ImageDraw.Draw(canvas)
    font_title = get_font(20)
    font_sub = get_font(13)

    x = 15
    canvas.paste(c_ref, (x, 70))
    draw.text((x, 15), "1. Referência Autoral 137 (S1-F06)", fill=(240, 240, 240), font=font_title)
    draw.text((x, 42), "Final baixo esquerdo, lâmina recolhida abaixo", fill=(180, 200, 220), font=font_sub)

    x += w_ref + 15
    canvas.paste(c_front, (x, 70))
    draw.rectangle([x, 70, x + 400, 670], outline=(60, 220, 120), width=3)
    draw.text((x, 15), f"2. Jogo Forge (Vista Frontal t={front_tick})", fill=(100, 255, 140), font=font_title)
    draw.text((x, 42), "Espada baixa à esquerda anatômica", fill=(160, 255, 180), font=font_sub)

    x += 400 + 15
    canvas.paste(c_prof, (x, 70))
    draw.rectangle([x, 70, x + 400, 670], outline=(60, 220, 120), width=3)
    draw.text((x, 15), f"3. Jogo Forge (Vista Perfil t={prof_tick})", fill=(100, 255, 140), font=font_title)
    draw.text((x, 42), "Lâmina contida e pronta para retorno", fill=(160, 255, 180), font=font_sub)

    out_path = QA_DIR / "diagonal-final-esquerdo-reference-vs-forge.png"
    canvas.save(out_path)
    print(f"Saved: {out_path}")

def create_3views_composite():
    # Front, Profile, Back for S1-F02 (around tick 33)
    front_path, front_tick = get_closest_frame(FRONT_DIR, 33)
    prof_path, prof_tick = get_closest_frame(PROFILE_DIR, 33)
    back_path, back_tick = get_closest_frame(BACK_DIR, 33)

    im_front = Image.open(front_path).convert("RGB")
    im_prof = Image.open(prof_path).convert("RGB")
    im_back = Image.open(back_path).convert("RGB")

    crop_box = (440, 80, 840, 680)
    c_f = im_front.crop(crop_box)
    c_p = im_prof.crop(crop_box)
    c_b = im_back.crop(crop_box)

    canvas = Image.new("RGB", (1230, 700), (24, 26, 32))
    draw = ImageDraw.Draw(canvas)
    font_title = get_font(20)
    font_sub = get_font(13)

    for i, (img, label, sub) in enumerate([
        (c_f, f"Vista Frontal (t={front_tick})", "Mão no ombro direito, ponta alta e fora do rosto"),
        (c_p, f"Vista Perfil (t={prof_tick})", "Lâmina apontada para cima/frente, cabo firme na palma"),
        (c_b, f"Vista Posterior (t={back_tick})", "Postura ereta, cotovelo dobrado, mão livre equilibrando")
    ]):
        x = 10 + i * 410
        canvas.paste(img, (x, 70))
        draw.rectangle([x, 70, x + 400, 670], outline=(80, 140, 220), width=2)
        draw.text((x, 15), label, fill=(240, 240, 240), font=font_title)
        draw.text((x, 42), sub, fill=(180, 200, 220), font=font_sub)

    out_path = QA_DIR / "diagonal-3vistas-preparacao.png"
    canvas.save(out_path)
    print(f"Saved: {out_path}")

def create_gif():
    # Sequence of frames t028..t048
    frames = []
    ticks = range(28, 49)
    for t in ticks:
        p = PROFILE_DIR / f"screenshots/frame-t{t:03d}.png"
        if p.exists():
            im = Image.open(p).convert("RGB")
            crop = im.crop((440, 80, 840, 680))
            # draw tick label
            draw = ImageDraw.Draw(crop)
            draw.text((10, 10), f"Tick {t}", fill=(255, 255, 0), font=get_font(20))
            frames.append(crop)
    if frames:
        out_gif = QA_DIR / "diagonal-sampling-s1.gif"
        frames[0].save(out_gif, save_all=True, append_images=frames[1:], duration=100, loop=0)
        print(f"Saved: {out_gif} ({len(frames)} frames)")

def main():
    create_preparacao_composite()
    create_travessia_composite()
    create_final_composite()
    create_3views_composite()
    create_gif()

if __name__ == "__main__":
    main()
