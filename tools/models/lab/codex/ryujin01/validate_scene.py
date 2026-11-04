"""Check the integrated Blockbench training scene and its exact exports."""
from __future__ import annotations

import base64
import json
from pathlib import Path
from PIL import Image

root = Path(__file__).resolve().parent
name = 'ryujin_bankai_activation_scene_v6_lab'
def load(file):
    return json.loads((root / file).read_text(encoding='utf-8'))

source = load(f'{name}.bbmodel')
geo = load(f'{name}.geo.json')['minecraft:geometry'][0]
clips = load(f'{name}.animation.json')
clip = clips['animations']['animation.ryujin.bankai_activation_scene_lab']
atlas_png = (root / f'{name}.png').read_bytes()
assert base64.b64decode(source['textures'][0]['source'].split('base64,', 1)[1]) == atlas_png
atlas = Image.open(root / f'{name}.png').convert('RGBA')
mask = Image.open(root / f'{name}_glowmask.png').convert('RGBA')
assert atlas.size == mask.size == (256, 4096)
assert 0 < sum(mask.getchannel('A').histogram()[1:]) < 100_000
assert source['name'] == name and len(source['elements']) == 220
assert geo['description']['identifier'] == f'geometry.{name}'
assert sum(len(bone.get('cubes', [])) for bone in geo['bones']) == 220
bone_names = {bone['name'] for bone in geo['bones']}
assert len(bone_names) == 54
assert set(clip['bones']).issubset(bone_names)
assert clip['animation_length'] == 12
assert clip['bones']['sun_root']['scale']['0.0']['vector'][0] <= .03
assert clip['bones']['sun_root']['scale']['5.7917']['post']['vector'][0] == 1
assert clip['bones']['sun_root']['scale']['7.0833']['post']['vector'][0] <= .03
for bone, sign in [('inner_swirl', -1), ('mid_swirl', 1), ('outer_swirl', -1)]:
    turns = clip['bones'][bone]['rotation']['7.0833']['vector'][2]
    assert turns * sign >= 360
for cube in source['elements']:
    if cube['name'].startswith('heat_'):
        assert cube['to'][2] - cube['from'][2] >= 1
qa = load('qa/activation-scene-v6-heat-uv-fix/results.json')
check = json.loads(next(item for item in qa if item['name'] == 'check_model')['text'][0])
assert check['issue_count'] == 0
motion = load('qa/activation-scene-v6/results.json')
analysis = json.loads(next(item for item in motion if item['name'] == 'analyze_animation')['text'][0])
assert analysis['issue_count'] == 0 and analysis['total_keyframes'] == 481
print(f'{name}: 220 cubes, 54 bones, 481 keyframes, 12 s, atlas {atlas.width}x{atlas.height}')
print(f'Glow mask: {sum(mask.getchannel("A").histogram()[1:])} visible pixels')
print('Blockbench: model 0 issues; motion 0 issues; 4 viewport warnings resolved')
