import os, glob, subprocess, shutil
import imageio_ffmpeg
from PIL import Image, ImageDraw, ImageFont

ffmpeg_exe = imageio_ffmpeg.get_ffmpeg_exe()
art_dir = r'C:\Users\joaop\.gemini\antigravity\brain\12218445-ff89-468b-bf4f-ae3e24878798'
out_lab = 'tools/models/lab/video_qa/test_20261105_better_combat'
os.makedirs(out_lab, exist_ok=True)

s_dir = 'run-benihime-qa/base/senbonzakura-combat-qa/run-1793880170247/02_shikai_right_front/screenshots'
h_dir = 'run-benihime-qa/base/senbonzakura-combat-qa/run-1793880170247/07_hakuteiken_combo_front/screenshots'

s_frames = sorted(glob.glob(s_dir + '/*.png'))
h_frames = sorted(glob.glob(h_dir + '/*.png'))

print(f'Building videos and storyboards from {len(h_frames)} frames...')

# Helper to generate concat file
def write_concat(frames, path, fps=10):
    with open(path, 'w', encoding='utf-8') as f:
        duration = 1.0 / fps
        for frame in frames:
            abs_p = os.path.abspath(frame).replace('\\', '/')
            f.write(f"file '{abs_p}'\n")
            f.write(f"duration {duration:.3f}\n")
        # last frame duplicated for concat demuxer quirk
        if frames:
            abs_p = os.path.abspath(frames[-1]).replace('\\', '/')
            f.write(f"file '{abs_p}'\n")

# 1. Video for Hakuteiken
concat_h = os.path.join(out_lab, 'concat_haku.txt')
write_concat(h_frames, concat_h, fps=10)

video_haku_path = os.path.join(art_dir, 'video_combo_hakuteiken.mp4')
lab_video_haku = os.path.join(out_lab, 'video_combo_hakuteiken.mp4')

cmd_h = [
    ffmpeg_exe, '-y',
    '-f', 'concat', '-safe', '0',
    '-i', os.path.abspath(concat_h),
    '-c:v', 'libx264',
    '-pix_fmt', 'yuv420p',
    '-vf', 'scale=1280:720',
    video_haku_path
]
subprocess.run(cmd_h, check=True)
shutil.copy2(video_haku_path, lab_video_haku)
print('Generated Hakuteiken video:', video_haku_path, os.path.getsize(video_haku_path), 'bytes')

# 2. Side-by-side comparison frames
comp_frame_dir = os.path.join(out_lab, 'comp_frames')
os.makedirs(comp_frame_dir, exist_ok=True)

w, h = 640, 360
comp_frames = []
gif_frames = []

for i, (sf, hf) in enumerate(zip(s_frames, h_frames)):
    img_s = Image.open(sf).resize((w, h), Image.Resampling.LANCZOS)
    img_h = Image.open(hf).resize((w, h), Image.Resampling.LANCZOS)
    
    comp = Image.new('RGB', (w * 2 + 10, h + 45), (20, 20, 25))
    comp.paste(img_s, (0, 45))
    comp.paste(img_h, (w + 10, 45))
    
    tick = 28 + i * 2
    strike = 'Kamae Neutro' if tick < 32 else ('Golpe 1: Corte Direito' if tick <= 44 else ('Golpe 2: Retorno Esquerdo' if tick <= 56 else ('Golpe 3: Estocada Frontal' if tick <= 68 else ('Golpe 1: Ciclo 2' if tick <= 82 else ('Golpe 2: Ciclo 2' if tick <= 94 else 'Golpe 3: Ciclo 2')))))
    
    draw = ImageDraw.Draw(comp)
    draw.rectangle([(0, 0), (w, 44)], fill=(30, 35, 50))
    draw.rectangle([(w + 10, 0), (w * 2 + 10, 44)], fill=(20, 50, 35))
    
    draw.text((15, 12), f'SHIKAI ORIGINAL (t{tick:03d}) | {strike}', fill=(220, 230, 255))
    draw.text((w + 25, 12), f'HAKUTEIKEN ALADO (t{tick:03d}) | {strike}', fill=(140, 255, 180))
    
    out_f = os.path.join(comp_frame_dir, f'comp_{i:03d}.png')
    comp.save(out_f)
    comp_frames.append(out_f)
    
    # 15 fps gif sample
    if i % 2 == 0:
        gif_frames.append(comp.resize((645, 205), Image.Resampling.LANCZOS))

# Save animated GIF
gif_path = os.path.join(art_dir, 'animacao_combo_comparativo.gif')
gif_frames[0].save(gif_path, save_all=True, append_images=gif_frames[1:], duration=200, loop=0)
print('Generated GIF:', gif_path, os.path.getsize(gif_path), 'bytes')

# 3. Side-by-side comparison video
concat_comp = os.path.join(out_lab, 'concat_comp.txt')
write_concat(comp_frames, concat_comp, fps=10)

video_comp_path = os.path.join(art_dir, 'video_combo_comparativo_shikai_hakuteiken.mp4')
lab_video_comp = os.path.join(out_lab, 'video_combo_comparativo_shikai_hakuteiken.mp4')

cmd_c = [
    ffmpeg_exe, '-y',
    '-f', 'concat', '-safe', '0',
    '-i', os.path.abspath(concat_comp),
    '-vf', 'scale=1280:406',
    '-c:v', 'libx264',
    '-pix_fmt', 'yuv420p',
    video_comp_path
]
subprocess.run(cmd_c, check=True)
shutil.copy2(video_comp_path, lab_video_comp)
print('Generated Comparison Video:', video_comp_path, os.path.getsize(video_comp_path), 'bytes')

# 4. Multi-frame Storyboard Contact Sheet (12 keyframes across the 3 combo strikes)
# Key ticks: 28 (idle), 34 (strike 1 start), 38 (strike 1 apex), 42 (strike 1 recovery),
#            46 (strike 2 start), 50 (strike 2 apex), 54 (strike 2 recovery),
#            58 (strike 3 start), 62 (strike 3 apex), 66 (strike 3 recovery),
#            74 (loop strike 1), 86 (loop strike 2)
key_indices = [0, 3, 5, 7, 9, 11, 13, 15, 17, 19, 23, 29]
cols = 4
rows = 3
cell_w, cell_h = 320, 180

board = Image.new('RGB', (cols * cell_w + 10, rows * cell_h + 80), (16, 18, 24))
draw_b = ImageDraw.Draw(board)
draw_b.rectangle([(0, 0), (cols * cell_w + 10, 45)], fill=(28, 32, 45))
draw_b.text((20, 14), 'STORYBOARD: COMBO DE 3 GOLPES BETTER COMBAT EM SHUKEI: HAKUTEIKEN (V3)', fill=(255, 255, 255))

for idx, k in enumerate(key_indices):
    if k >= len(h_frames):
        continue
    c = idx % cols
    r = idx // cols
    x = c * cell_w + 5
    y = r * cell_h + 50
    
    img = Image.open(h_frames[k]).resize((cell_w - 6, cell_h - 26), Image.Resampling.LANCZOS)
    board.paste(img, (x + 3, y + 23))
    
    tick = 28 + k * 2
    strike_lbl = '1. Kamae Neutro' if tick < 32 else ('2. Golpe 1 (Slash R)' if tick <= 44 else ('3. Golpe 2 (Slash L)' if tick <= 56 else ('4. Golpe 3 (Stab)' if tick <= 68 else f'Ciclo 2 (t{tick})')))
    draw_b.rectangle([(x + 3, y), (x + cell_w - 3, y + 22)], fill=(32, 36, 52))
    draw_b.text((x + 8, y + 4), f't{tick:03d} | {strike_lbl}', fill=(130, 255, 160))

board_path = os.path.join(art_dir, 'storyboard_combo_3_golpes_better_combat.png')
board.save(board_path)
print('Generated Storyboard Contact Sheet:', board_path, os.path.getsize(board_path), 'bytes')
