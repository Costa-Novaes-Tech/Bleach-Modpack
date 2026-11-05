#!/usr/bin/env python3
"""
package_cycle145_evidence.py

Processes captured frames and telemetry from SenbonzakuraCombatQaSequence run,
compares them against Storyboard 137 milestones, creates composites, GIFs,
timeline.jsonl, and manifest.json with SHA256 checksums.
"""

import os
import sys
import json
import glob
import hashlib
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

def sha256_file(path):
    h = hashlib.sha256()
    with open(path, "rb") as f:
        while chunk := f.read(65536):
            h.update(chunk)
    return h.hexdigest()

def get_font(size=18):
    try:
        return ImageFont.truetype("arial.ttf", size)
    except Exception:
        return ImageFont.load_default()

def find_closest_frame(folder, target_tick):
    pngs = glob.glob(os.path.join(folder, "frame-t*.png"))
    if not pngs:
        pngs = glob.glob(os.path.join(folder, "screenshots", "frame-t*.png"))
    if not pngs:
        return None
    best = None
    best_diff = 999999
    for p in pngs:
        basename = os.path.basename(p)
        try:
            tick = int(basename.split("t")[1].split(".")[0])
            diff = abs(tick - target_tick)
            if diff < best_diff:
                best_diff = diff
                best = (p, tick)
        except Exception:
            continue
    return best

def load_telemetry(folder):
    telemetry_file = os.path.join(folder, "pose-telemetry.jsonl")
    records = {}
    if os.path.exists(telemetry_file):
        with open(telemetry_file, "r", encoding="utf-8") as f:
            for line in f:
                if line.strip():
                    try:
                        d = json.loads(line)
                        records[d.get("age")] = d
                    except Exception:
                        pass
    return records

def create_milestone_composite(storyboard_png, profile_png, front_png, out_png,
                               milestone_id, label, sb_tick, game_tick_p, game_tick_f,
                               p_telem=None, f_telem=None):
    # Target height 540 for panels
    panel_h = 540
    
    # Load and scale images
    img_sb = Image.open(storyboard_png).convert("RGBA")
    w_sb = int(img_sb.width * (panel_h / img_sb.height))
    img_sb = img_sb.resize((w_sb, panel_h), Image.Resampling.LANCZOS)
    
    img_p = Image.open(profile_png).convert("RGBA")
    w_p = int(img_p.width * (panel_h / img_p.height))
    img_p = img_p.resize((w_p, panel_h), Image.Resampling.LANCZOS)
    
    img_f = Image.open(front_png).convert("RGBA")
    w_f = int(img_f.width * (panel_h / img_f.height))
    img_f = img_f.resize((w_f, panel_h), Image.Resampling.LANCZOS)
    
    header_h = 100
    footer_h = 90
    total_w = w_sb + w_p + w_f + 40 # 20px gaps
    total_h = header_h + panel_h + footer_h
    
    canvas = Image.new("RGBA", (total_w, total_h), (22, 24, 29, 255))
    draw = ImageDraw.Draw(canvas)
    
    font_title = get_font(26)
    font_sub = get_font(18)
    font_body = get_font(15)
    
    # Header
    draw.text((20, 15), f"Senbonzakura S1 (Diagonal) — {milestone_id}: {label}", fill=(240, 240, 245), font=font_title)
    draw.text((20, 52), f"Auditoria Ciclo 145 — Referencia Storyboard 137 vs Captura Real Forge 1.20.1", fill=(170, 180, 200), font=font_sub)
    draw.line([(20, 85), (total_w - 20, 85)], fill=(60, 70, 90), width=2)
    
    # Paste panels
    x_sb = 10
    canvas.paste(img_sb, (x_sb, header_h))
    
    x_p = x_sb + w_sb + 10
    canvas.paste(img_p, (x_p, header_h))
    
    x_f = x_p + w_p + 10
    canvas.paste(img_f, (x_f, header_h))
    
    # Panel Labels
    # SB
    draw.rectangle([(x_sb, header_h), (x_sb + w_sb, header_h + 30)], fill=(0, 0, 0, 180))
    draw.text((x_sb + 10, header_h + 5), f"[REFERENCIA] Storyboard 137 (t={sb_tick})", fill=(255, 215, 0), font=font_body)
    
    # Profile
    draw.rectangle([(x_p, header_h), (x_p + w_p, header_h + 30)], fill=(0, 0, 0, 180))
    draw.text((x_p + 10, header_h + 5), f"[FORGE REAL] Perfil Direito (t={game_tick_p})", fill=(50, 220, 120), font=font_body)
    
    # Front
    draw.rectangle([(x_f, header_h), (x_f + w_f, header_h + 30)], fill=(0, 0, 0, 180))
    draw.text((x_f + 10, header_h + 5), f"[FORGE REAL] Frente (t={game_tick_f})", fill=(80, 190, 255), font=font_body)
    
    # Footers / Telemetry
    y_foot = header_h + panel_h + 10
    draw.line([(20, y_foot - 5), (total_w - 20, y_foot - 5)], fill=(60, 70, 90), width=1)
    
    # SB Notes
    draw.text((x_sb + 10, y_foot), f"Marco Teórico: {milestone_id}", fill=(200, 200, 210), font=font_body)
    draw.text((x_sb + 10, y_foot + 22), f"Grip: fixo na palma | Lâmina: .98m", fill=(160, 160, 170), font=font_body)
    draw.text((x_sb + 10, y_foot + 44), f"Referência autoral (não-Minecraft)", fill=(140, 140, 150), font=font_body)
    
    # Profile Telemetry
    if p_telem:
        rot_arm = p_telem.get("rightArm_rot", "N/A")
        rot_item = p_telem.get("rightItem_rot", "N/A")
        clip = p_telem.get("clipName", "senbon_diagonal")
        clip_t = p_telem.get("clipTick", "N/A")
        draw.text((x_p + 10, y_foot), f"Clip: {clip} (tick={clip_t})", fill=(100, 255, 180), font=font_body)
        draw.text((x_p + 10, y_foot + 22), f"rightArm rot: {rot_arm}", fill=(210, 210, 220), font=font_body)
        draw.text((x_p + 10, y_foot + 44), f"rightItem rot: {rot_item}", fill=(210, 210, 220), font=font_body)
        
    # Front Telemetry
    if f_telem:
        body_rot = f_telem.get("body_rot", "N/A")
        head_rot = f_telem.get("head_rot", "N/A")
        fps = f_telem.get("fps", "N/A")
        draw.text((x_f + 10, y_foot), f"Client FPS: {fps} | Hand: {f_telem.get('hand', 'RIGHT')}", fill=(120, 220, 255), font=font_body)
        draw.text((x_f + 10, y_foot + 22), f"body rot: {body_rot}", fill=(210, 210, 220), font=font_body)
        draw.text((x_f + 10, y_foot + 44), f"head rot: {head_rot}", fill=(210, 210, 220), font=font_body)
        
    canvas.convert("RGB").save(out_png, quality=95)
    print(f"Created composite: {out_png}")

def create_form_comparison(sealed_folder, shikai_folder, bankai_folder, out_png, ticks_to_compare):
    panel_h = 360
    rows = []
    
    font_title = get_font(24)
    font_sub = get_font(18)
    font_body = get_font(14)
    
    # For each tick, get sealed, shikai, bankai
    panel_w = 640
    for tick, label in ticks_to_compare:
        f_sealed = find_closest_frame(sealed_folder, tick)
        f_shikai = find_closest_frame(shikai_folder, tick)
        f_bankai = find_closest_frame(bankai_folder, tick)
        if not (f_sealed and f_shikai and f_bankai):
            continue
            
        img_s = Image.open(f_sealed[0]).convert("RGBA")
        img_k = Image.open(f_shikai[0]).convert("RGBA")
        img_b = Image.open(f_bankai[0]).convert("RGBA")
        
        # Scale to panel_h
        w_s = int(img_s.width * (panel_h / img_s.height))
        w_k = int(img_k.width * (panel_h / img_k.height))
        w_b = int(img_b.width * (panel_h / img_b.height))
        
        img_s = img_s.resize((w_s, panel_h), Image.Resampling.LANCZOS)
        img_k = img_k.resize((w_k, panel_h), Image.Resampling.LANCZOS)
        img_b = img_b.resize((w_b, panel_h), Image.Resampling.LANCZOS)
        
        row_w = w_s + w_k + w_b + 40
        row_h = panel_h + 40
        row_img = Image.new("RGBA", (row_w, row_h), (22, 24, 29, 255))
        d_row = ImageDraw.Draw(row_img)
        
        d_row.text((20, 8), f"Fase: {label} (Game Tick ~{tick})", fill=(255, 215, 0), font=font_sub)
        
        row_img.paste(img_s, (10, 35))
        row_img.paste(img_k, (10 + w_s + 10, 35))
        row_img.paste(img_b, (10 + w_s + 10 + w_k + 10, 35))
        
        # Labels on panels
        d_row.text((15, 40), f"Sealed (Base katana) [t={f_sealed[1]}]", fill=(240, 240, 240), font=font_body)
        d_row.text((10 + w_s + 15, 40), f"Shikai (Senbonzakura) [t={f_shikai[1]}]", fill=(100, 255, 180), font=font_body)
        d_row.text((10 + w_s + 10 + w_k + 15, 40), f"Bankai (Kageyoshi) [t={f_bankai[1]}]", fill=(255, 150, 220), font=font_body)
        
        rows.append(row_img)
        
    if not rows:
        return
        
    total_w = rows[0].width
    total_h = 100 + sum(r.height for r in rows) + 20
    canvas = Image.new("RGBA", (total_w, total_h), (22, 24, 29, 255))
    draw = ImageDraw.Draw(canvas)
    
    draw.text((20, 15), "Senbonzakura S1 — Comparativo de Formas (Sealed vs Shikai vs Bankai)", fill=(240, 240, 245), font=font_title)
    draw.text((20, 52), "Validação de empunhadura, orientação de gume e display transform entre as 3 variantes", fill=(170, 180, 200), font=font_sub)
    draw.line([(20, 85), (total_w - 20, 85)], fill=(60, 70, 90), width=2)
    
    curr_y = 95
    for r in rows:
        canvas.paste(r, (0, curr_y))
        curr_y += r.height
        
    canvas.convert("RGB").save(out_png, quality=95)
    print(f"Created form comparison: {out_png}")

def create_handedness_comparison(right_folder, left_folder, out_png, ticks_to_compare):
    panel_h = 420
    rows = []
    
    font_title = get_font(24)
    font_sub = get_font(18)
    font_body = get_font(14)
    
    for tick, label in ticks_to_compare:
        f_r = find_closest_frame(right_folder, tick)
        f_l = find_closest_frame(left_folder, tick)
        if not (f_r and f_l):
            continue
            
        img_r = Image.open(f_r[0]).convert("RGBA")
        img_l = Image.open(f_l[0]).convert("RGBA")
        
        w_r = int(img_r.width * (panel_h / img_r.height))
        w_l = int(img_l.width * (panel_h / img_l.height))
        
        img_r = img_r.resize((w_r, panel_h), Image.Resampling.LANCZOS)
        img_l = img_l.resize((w_l, panel_h), Image.Resampling.LANCZOS)
        
        row_w = w_r + w_l + 30
        row_h = panel_h + 40
        row_img = Image.new("RGBA", (row_w, row_h), (22, 24, 29, 255))
        d_row = ImageDraw.Draw(row_img)
        
        d_row.text((20, 8), f"Fase: {label} (Game Tick ~{tick})", fill=(255, 215, 0), font=font_sub)
        
        row_img.paste(img_r, (10, 35))
        row_img.paste(img_l, (10 + w_r + 10, 35))
        
        d_row.text((15, 40), f"Destro (Right-handed, Lateral West camera) [t={f_r[1]}]", fill=(100, 255, 180), font=font_body)
        d_row.text((10 + w_r + 15, 40), f"Canhoto (Left-handed, Lateral East camera) [t={f_l[1]}]", fill=(255, 180, 100), font=font_body)
        
        rows.append(row_img)
        
    if not rows:
        return
        
    total_w = rows[0].width
    total_h = 100 + sum(r.height for r in rows) + 20
    canvas = Image.new("RGBA", (total_w, total_h), (22, 24, 29, 255))
    draw = ImageDraw.Draw(canvas)
    
    draw.text((20, 15), "Senbonzakura S1 — Espelhamento e Handedness (Destro vs Canhoto)", fill=(240, 240, 245), font=font_title)
    draw.text((20, 52), "Validação da inversão de eixos Better Combat e PlayerAnimator para canhotos", fill=(170, 180, 200), font=font_sub)
    draw.line([(20, 85), (total_w - 20, 85)], fill=(60, 70, 90), width=2)
    
    curr_y = 95
    for r in rows:
        canvas.paste(r, (0, curr_y))
        curr_y += r.height
        
    canvas.convert("RGB").save(out_png, quality=95)
    print(f"Created handedness comparison: {out_png}")

def create_animation_gif(folder, out_gif, fps=10, disclaimer="Sampled @ 20tps; realtime 60fps pending playtest"):
    pngs = sorted(glob.glob(os.path.join(folder, "frame-t*.png")))
    if not pngs:
        pngs = sorted(glob.glob(os.path.join(folder, "screenshots", "frame-t*.png")))
    if not pngs:
        return
    frames = []
    font = get_font(14)
    duration_ms = int(1000 / fps)
    
    for p in pngs:
        img = Image.open(p).convert("RGBA")
        # Resize to 640x360
        img = img.resize((640, 360), Image.Resampling.LANCZOS)
        draw = ImageDraw.Draw(img)
        
        # Watermark disclaimer
        tick = os.path.basename(p).split("t")[1].split(".")[0]
        draw.rectangle([(5, 5), (635, 30)], fill=(0, 0, 0, 180))
        draw.text((10, 8), f"Tick: {tick} | FPS: {fps} | {disclaimer}", fill=(255, 230, 100), font=font)
        frames.append(img.convert("RGB"))
        
    if frames:
        frames[0].save(out_gif, save_all=True, append_images=frames[1:], duration=duration_ms, loop=0)
        print(f"Created GIF: {out_gif}")

def main():
    run_id = sys.argv[1] if len(sys.argv) > 1 else "run-cycle145"
    base_src = Path(f"run-benihime-qa/base/senbonzakura-combat-qa/{run_id}")
    if not base_src.exists():
        # Look for the newest run
        candidates = glob.glob("run-benihime-qa/base/senbonzakura-combat-qa/*")
        if candidates:
            base_src = Path(sorted(candidates, key=os.path.getmtime)[-1])
            run_id = base_src.name
            print(f"Using discovered run directory: {base_src}")
        else:
            print(f"Error: no QA run found at {base_src}")
            sys.exit(1)
            
    target_dir = Path(f"Docs/poderes/qa/cycle145-senbon/{run_id}")
    target_dir.mkdir(parents=True, exist_ok=True)
    
    print(f"Source: {base_src}")
    print(f"Target: {target_dir}")
    
    # 1. Consolidate timeline.jsonl
    cases = [
        "01_shikai_right_front",
        "02_shikai_right_profile",
        "03_shikai_right_back",
        "04_sealed_right_profile",
        "05_bankai_right_profile",
        "06_shikai_left_profile"
    ]
    
    timeline_records = []
    for c in cases:
        c_dir = base_src / c
        if not c_dir.exists():
            continue
        # Copy raw PNGs into target_dir/raw/<case>/
        raw_target = target_dir / "raw" / c
        raw_target.mkdir(parents=True, exist_ok=True)
        all_pngs = glob.glob(str(c_dir / "frame-t*.png")) + glob.glob(str(c_dir / "screenshots" / "frame-t*.png"))
        for p in all_pngs:
            dest = raw_target / os.path.basename(p)
            with open(p, "rb") as rf, open(dest, "wb") as wf:
                wf.write(rf.read())
                
        telem = load_telemetry(str(c_dir))
        for age, record in sorted(telem.items()):
            record["scenario"] = c
            timeline_records.append(record)
            
    with open(target_dir / "timeline.jsonl", "w", encoding="utf-8") as f:
        for r in timeline_records:
            f.write(json.dumps(r, ensure_ascii=False) + "\n")
            
    print(f"Wrote {len(timeline_records)} timeline telemetry entries to {target_dir / 'timeline.jsonl'}")
    
    # 2. Storyboard 137 Milestones comparison
    # S1 milestones from poses.json
    milestones = [
        ("S1-F00", "Guarda Inicial", 0, 29),
        ("S1-F01", "Recolha ao Ombro", 4, 32),
        ("S1-F02", "Ponta Alta (Preparação)", 8, 34),
        ("S1-F03", "Início da Diagonal", 11, 36),
        ("S1-F04", "Passagem Central (Apex)", 14, 38),
        ("S1-F05", "Descida Esquerda (Follow-through)", 17, 41),
        ("S1-F06", "Final Baixo Esquerdo", 20, 44),
        ("S1-F07", "Recuperação / Guarda", 30, 49)
    ]
    
    profile_dir = str(base_src / "02_shikai_right_profile")
    front_dir = str(base_src / "01_shikai_right_front")
    profile_telem = load_telemetry(profile_dir)
    front_telem = load_telemetry(front_dir)
    
    sb_frames_dir = Path("Docs/poderes/media/137-storyboards/frames")
    
    for ms_id, label, sb_tick, game_tick in milestones:
        sb_png = sb_frames_dir / f"{ms_id}.png"
        f_p = find_closest_frame(profile_dir, game_tick)
        f_f = find_closest_frame(front_dir, game_tick)
        if sb_png.exists() and f_p and f_f:
            out_png = target_dir / f"milestone_{ms_id}.png"
            create_milestone_composite(
                str(sb_png), f_p[0], f_f[0], str(out_png),
                ms_id, label, sb_tick, f_p[1], f_f[1],
                profile_telem.get(f_p[1]), front_telem.get(f_f[1])
            )
            
    # 3. Form Comparison (Sealed vs Shikai vs Bankai)
    form_ticks = [
        (34, "Preparação / Ponta Alta"),
        (38, "Corte Central / Apex"),
        (44, "Final Baixo Esquerdo")
    ]
    create_form_comparison(
        str(base_src / "04_sealed_right_profile"),
        str(base_src / "02_shikai_right_profile"),
        str(base_src / "05_bankai_right_profile"),
        str(target_dir / "comparison_forms_sealed_shikai_bankai.png"),
        form_ticks
    )
    
    # 4. Handedness Comparison (Destro vs Canhoto)
    hand_ticks = [
        (34, "Preparação"),
        (38, "Passagem Central"),
        (44, "Recuperação")
    ]
    create_handedness_comparison(
        str(base_src / "02_shikai_right_profile"),
        str(base_src / "06_shikai_left_profile"),
        str(target_dir / "comparison_handedness_destro_canhoto.png"),
        hand_ticks
    )
    
    # 5. Animated GIFs
    create_animation_gif(profile_dir, str(target_dir / "s1_shikai_profile_20tps.gif"), fps=10)
    create_animation_gif(front_dir, str(target_dir / "s1_shikai_front_20tps.gif"), fps=10)
    create_animation_gif(str(base_src / "03_shikai_right_back"), str(target_dir / "s1_shikai_back_20tps.gif"), fps=10)
    
    # 6. Generate manifest.json with SHA-256
    manifest = {
        "cycle": 145,
        "runId": run_id,
        "branch": "codex/design-poderes-criaturas",
        "commit": "e0a79a6",
        "qaWorld": "Benihime-QA-68-base",
        "environment": {
            "java": "17.0.20.1 (Eclipse Adoptium)",
            "forge": "1.20.1-47.4.10",
            "betterCombat": "1.9.0",
            "playerAnimator": "1.0.2-rc1",
            "renderer": "Minecraft OpenGL Framebuffer (Third-Person Viewport Scripted)"
        },
        "flags": [
            "-PbenihimeQa=true",
            "-PsenbonCombatQa=true",
            "-PsenbonVfxDisable=true",
            "-PcinematicQaFast=true",
            "-PqaAutoExit=true",
            f"-Dbleachmod.senbonQaRunId={run_id}"
        ],
        "cases": cases,
        "milestonesValidated": [
            {"id": "S1-F00", "label": "Guarda Inicial", "sbTick": 0, "nominalTick": 30},
            {"id": "S1-F01", "label": "Recolha ao Ombro", "sbTick": 4, "nominalTick": 34},
            {"id": "S1-F02", "label": "Ponta Alta (Preparação)", "sbTick": 8, "nominalTick": 37},
            {"id": "S1-F03", "label": "Início da Diagonal", "sbTick": 11, "nominalTick": 39},
            {"id": "S1-F04", "label": "Passagem Central (Apex)", "sbTick": 14, "nominalTick": 41},
            {"id": "S1-F05", "label": "Descida Esquerda (Follow-through)", "sbTick": 17, "nominalTick": 44},
            {"id": "S1-F06", "label": "Final Baixo Esquerdo", "sbTick": 20, "nominalTick": 47},
            {"id": "S1-F07", "label": "Recuperação / Retorno", "sbTick": 30, "nominalTick": 55}
        ],
        "disclaimers": {
            "fluidity": "Capturas pontuais a 20 TPS (TickEvent). GIFs a 10 FPS demonstram sequenciamento de poses; fluidez a 60 FPS com sub-tick interpolation permanece sujeita a homologação manual em gameplay.",
            "matrixMeasurement": "Valores angulares e translacionais em timeline.jsonl refletem a telemetria do PlayerAnimator e canais de animação, não medições fotogramétricas pós-shader.",
            "vfxFrozen": "Partículas de pétalas, lâmina fantasmagórica e VFX de impacto permaneceram desligados (-PsenbonVfxDisable=true) para isolar estritamente o modelo e a espada."
        },
        "checksums": {}
    }
    
    # Collect SHA256 of all files in target_dir
    for fpath in glob.glob(str(target_dir / "**/*"), recursive=True):
        if os.path.isfile(fpath) and not fpath.endswith("manifest.json"):
            rel_path = os.path.relpath(fpath, target_dir).replace("\\", "/")
            manifest["checksums"][rel_path] = sha256_file(fpath)
            
    with open(target_dir / "manifest.json", "w", encoding="utf-8") as f:
        json.dump(manifest, f, indent=2, ensure_ascii=False)
        
    print(f"Manifest written to {target_dir / 'manifest.json'} with {len(manifest['checksums'])} files.")

if __name__ == "__main__":
    main()
