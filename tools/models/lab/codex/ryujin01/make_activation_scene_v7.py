"""Derive a reviewable v7 Blockbench scene from v6 without touching other projects."""
from __future__ import annotations

import json
from copy import deepcopy
from pathlib import Path
from uuid import uuid4

root = Path(__file__).resolve().parent
old = 'ryujin_bankai_activation_scene_v6_lab'
new = 'ryujin_bankai_activation_scene_v7_lab'
source = json.loads((root / f'{old}.bbmodel').read_text(encoding='utf-8'))
scene = deepcopy(source)
scene['name'] = new
scene['model_identifier'] = new
scene['geckolib_filepath_cache'] = {}
scene['animations'][0]['saved'] = False
scene['animations'][0]['path'] = ''
scene['animations'][0]['name'] = 'animation.ryujin.bankai_activation_scene_v7_lab'
groups = {group['name']: group for group in scene['groups']}
animators = scene['animations'][0]['animators']

def channel(name: str, kind: str):
    return animators[groups[name]['uuid']]['keyframes']

def value(key):
    point = key['data_points'][0]
    return [float(point[axis]) for axis in 'xyz']

def make_key(kind, time, vector, interpolation='linear'):
    return {'channel': kind,
            'data_points': [dict(zip('xyz', (f'{v:.5f}'.rstrip('0').rstrip('.') for v in vector)))],
            'uuid': str(uuid4()), 'time': round(time, 5), 'color': -1,
            'interpolation': interpolation, **({'uniform': True} if kind == 'scale' else {})}

def replace(name, kind, points, keep_before=None):
    keys = channel(name, kind)
    retained = [key for key in keys if key['channel'] != kind or
                (keep_before is not None and key['time'] < keep_before)]
    retained += [make_key(kind, time, vector, interpolation)
                 for time, vector, interpolation in points]
    animators[groups[name]['uuid']]['keyframes'] = retained

# The modeled sun is already behind the actor (+Z); lift it clear of the head
# and separate it further in depth for the front-camera cinematic composition.
lift = (0, 18, 18)
path = [
    (0,(0,18,18)), (3.34,(0,18,18)), (3.4,(0,18,18)),
    (3.55,(0,18,18)), (3.78,(0,18,18)), (5.8,(0,18,18)),
    (6.0,(1,18,18)), (6.2,(3,15,13)), (6.4,(6,7,4)),
    (6.6,(9,-7,-11)), (6.8,(12,-32,-30)),
    (7.0,(16,-60,-49)), (7.1,(18,-74,-56)), (12,(18,-74,-56)),
]
replace('sun_root','position',[(t,v,'linear') for t,v in path])
scale = [
    (0,.02),(3.34,.02),(3.4,.15),(3.55,1.15),(3.78,1),
    (5.8,1),(6.0,.98),(6.2,.93),(6.4,.83),(6.6,.67),
    (6.8,.45),(7.0,.18),(7.1,.025),(12,.025),
]
replace('sun_root','scale',[(t,(s,s,s),'linear') for t,s in scale])

# Keep the formation keys. Accelerate the counter-rotating rings during
# absorption; 0.13 s samples keep each angular step below a half turn.
def sample_rotation(name, time):
    keys = sorted((k for k in channel(name,'rotation') if k['channel']=='rotation'),key=lambda k:k['time'])
    before = max((k for k in keys if k['time'] <= time),key=lambda k:k['time'])
    after = min((k for k in keys if k['time'] >= time),key=lambda k:k['time'])
    if before['time'] == after['time']:
        return value(before)
    f = (time-before['time'])/(after['time']-before['time'])
    return [a+(b-a)*f for a,b in zip(value(before),value(after))]
for name, turn in [('inner_swirl',-540),('mid_swirl',620),('outer_swirl',-760),('broken_rim',400)]:
    start = sample_rotation(name,5.8)
    times = [5.8,5.93,6.06,6.19,6.32,6.45,6.58,6.71,6.84,6.97,7.1]
    points=[]
    for time in times:
        u=(time-5.8)/1.3
        # Increasing angular velocity, with small enough per-key steps.
        f=.18*u+.82*u*u
        points.append((time,(start[0],start[1],start[2]+turn*f),'linear'))
    replace(name,'rotation',points,keep_before=5.8)

# Pull flame tongues around the moving rings while the core contracts.
for i in range(13):
    name=f'flame_{i:02d}'
    old_rotation = sample_rotation(name,5.82)
    direction = -1 if i%2 else 1
    points=[(5.82,old_rotation,'linear')]
    for time,angle in [(6.2,22),(6.55,57),(6.85,96),(7.1,132)]:
        points.append((time,(old_rotation[0],old_rotation[1],old_rotation[2]+direction*angle),'linear'))
    replace(name,'rotation',points,keep_before=5.82)
    replace(name,'scale',[(time,(s,s,s),'linear') for time,s in
                          [(5.82,1),(6.2,1.03),(6.55,.92),(6.85,.70),(7.1,.4)]],
            keep_before=5.82)

# Existing spark trajectories ended at the blade. Shift their starting geometry
# with the sun, then subtract the same offset over travel to keep that endpoint.
for group in scene['groups']:
    if group['name'].startswith('absorb_spark_'):
        group['origin'] = [round(v+d,5) for v,d in zip(group['origin'],lift)]
for cube in scene['elements']:
    if cube['name'].startswith('absorb_spark_'):
        for prop in ('from','to','origin'):
            cube[prop] = [round(v+d,5) for v,d in zip(cube[prop],lift)]
for i in range(12):
    name=f'absorb_spark_{i:02d}'
    keys=channel(name,'position')
    pos=[k for k in keys if k['channel']=='position']
    end=value(max(pos,key=lambda k:k['time'] if k['time']<8 else -1))
    norm=sum(v*v for v in end)
    assert norm>0
    for key in pos:
        v=value(key)
        fraction=max(0,min(1,sum(a*b for a,b in zip(v,end))/norm))
        shifted=[round(a-d*fraction,5) for a,d in zip(v,lift)]
        key['data_points'][0]=dict(zip('xyz',(str(a) for a in shifted)))

# Source remains an editable Blockbench project. The texture is still the exact
# embedded v6 atlas; the front-facing marker is painted in the isolated editor.
out = root / f'{new}.bbmodel'
out.write_text(json.dumps(scene,separators=(',',':')),encoding='utf-8')
print(out.name, len(scene['elements']), len(scene['groups']),
      sum(len(a.get('keyframes',[])) for a in animators.values()), 'keyframes')
