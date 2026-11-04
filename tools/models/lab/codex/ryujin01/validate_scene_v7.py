"""Validate the revised Blockbench scene and exported animation."""
from __future__ import annotations
import base64, json
from pathlib import Path
from PIL import Image
root=Path(__file__).resolve().parent
name='ryujin_bankai_activation_scene_v7_lab'
def read(path): return json.loads((root/path).read_text(encoding='utf-8'))
source=read(f'{name}.bbmodel')
geo=read(f'{name}.geo.json')['minecraft:geometry'][0]
clips=read(f'{name}.animation.json')
clip=clips['animations'][f'animation.ryujin.bankai_activation_scene_v7_lab']
png=(root/f'{name}.png').read_bytes()
assert base64.b64decode(source['textures'][0]['source'].split('base64,',1)[1])==png
atlas=Image.open(root/f'{name}.png').convert('RGBA')
glow=Image.open(root/f'{name}_glowmask.png').convert('RGBA')
assert atlas.size==glow.size==(256,4096)
assert atlas.getpixel((17,11))[:3]==(46,23,19)
assert source['name']==source['model_identifier']==name
assert len(source['elements'])==220 and len(source['groups'])==54
assert sum(len(animator.get('keyframes',[])) for animator in source['animations'][0]['animators'].values())==629
assert geo['description']['identifier']==f'geometry.{name}'
assert sum(len(b.get('cubes',[])) for b in geo['bones'])==220
assert clip['animation_length']==12
assert set(clip['bones']).issubset({b['name'] for b in geo['bones']})
sun=clip['bones']['sun_root']
assert sun['position']['5.7917']['vector']==[0,18,18]
assert sun['position']['7.0833']['vector']==[-18,-74,-56]
scales=[sun['scale'][t]['vector'][0] for t in ('5.7917','6.0','6.2083','6.4167','6.5833','6.7917','7.0','7.0833')]
assert scales==sorted(scales,reverse=True) and scales[0]==1 and scales[-1]<=.03
for bone,direction in [('inner_swirl',-1),('mid_swirl',1),('outer_swirl',-1)]:
    turns=clip['bones'][bone]['rotation']
    samples=[(float(time),entry.get('post',entry)['vector'][2]) for time,entry in sorted(turns.items(),key=lambda pair:float(pair[0])) if float(time)>=5.79]
    values=[angle for _,angle in samples]
    assert all((b-a)*direction>=0 for a,b in zip(values,values[1:]))
    assert abs(values[-1])>700
    speeds=[abs((b[1]-a[1])/(b[0]-a[0])) for a,b in zip(samples,samples[1:])]
    assert speeds[-1]>speeds[0]*2
qa=read('qa/activation-scene-v7/results.json')
for tool in ('check_model','analyze_animation'):
    result=json.loads(next(item for item in qa if item['name']==tool)['text'][0])
    assert result['issue_count']==0
    if tool=='analyze_animation': assert result['total_keyframes']==629
orientation=json.loads(next(item for item in qa if item['name']=='get_orientation')['text'][0])
assert orientation['front_axis']=='-z' and orientation['back_axis']=='+z'
print(f'{name}: {len(source["elements"])} cubes, {len(source["groups"])} bones, 12 s')
print('Sun depth/height offset verified; shrinking monotonic; opposing rings accelerate')
print('Blockbench model/motion QA: 0 issues; embedded atlas and glowmask verified')
