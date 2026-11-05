"""Build a selective GeckoLib glow mask from the exact Blockbench atlas UVs."""
from __future__ import annotations

import json
import sys
from math import floor, ceil
from pathlib import Path
from PIL import Image

root = Path(__file__).resolve().parent
name = sys.argv[1] if len(sys.argv) > 1 else 'ryujin_bankai_activation_scene_v6_lab'
source = json.loads((root / f'{name}.bbmodel').read_text(encoding='utf-8'))
atlas = Image.open(root / f'{name}.png').convert('RGBA')
mask = Image.new('RGBA', atlas.size, (0, 0, 0, 0))
assert atlas.size == (256, 4096)
count = 0
for cube in source['elements']:
    if not cube['name'].startswith(('heart_', 'inner_', 'flame_', 'flash_', 'ember_', 'absorb_')):
        continue
    for face in cube['faces'].values():
        u1, v1, u2, v2 = face['uv']
        box = (max(0, floor(min(u1, u2))), max(0, floor(min(v1, v2))),
               min(atlas.width, ceil(max(u1, u2))), min(atlas.height, ceil(max(v1, v2))))
        if box[0] >= box[2] or box[1] >= box[3]:
            continue
        patch = atlas.crop(box)
        mask.paste(patch, box)
        count += 1
mask.save(root / f'{name}_glowmask.png')
print(f'{count} warm UV faces, {sum(mask.getchannel("A").histogram()[1:]):,} visible glow pixels')
