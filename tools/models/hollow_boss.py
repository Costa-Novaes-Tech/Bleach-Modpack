"""First Hollow boss: authored world-scale GeckoLib rig and editable Blockbench source.
Run python tools/models/hollow_boss.py [--check]. No downloaded artwork.
"""
from pathlib import Path
import argparse
import json
import math
from hollow_variants import Rig, texture, bbmodel, uid

ROOT = Path(__file__).resolve().parents[2]
ASSETS = ROOT / 'src/main/resources/assets/bleachmod'
COLORS = [(91, 93, 110), (121, 124, 137), (76, 80, 79), (242, 240, 226),
          (108, 112, 109), (16, 16, 23), (251, 224, 54), (100, 33, 38)]


def rig():
    r = Rig('boss')
    r.bone('root', (0, 0, 0), None)
    body = r.bone('body', (0, 34, 2))
    r.mass(body, 'pelvis', (0, 34, 3), (22, 13, 13))
    r.mass(body, 'lower_abdomen', (0, 42, 1), (20, 12, 11))
    chest = r.bone('chest', (0, 56, 2), 'body')
    # Ring and separated ribs: the Hollow opening genuinely crosses the whole body.
    r.mass(chest, 'upper_thorax', (0, 67, 3), (38, 19, 19))
    for s in [-1, 1]:
        r.mass(chest, 'ribcage', (s * 12.5, 53, 1), (10.5, 24, 13))
        r.mass(chest, 'pectoral', (s * 10, 66, -5), (17, 12, 7))
        for y, x in [(69, 14), (60, 11), (42, 7), (34, 8)]:
            r.box(chest if y > 45 else body, 'wine_body_mark', (s*x - 1.8, y-3, -9 if y>45 else -5.6),
                  (3.6, 8, .35), 7, [0, 0, -s*17], [s*x, y, -8 if y>45 else -5.4])
    for i in range(16):
        a = math.tau * i / 16
        x, y = math.cos(a)*7.65, 52+math.sin(a)*7.65
        r.box(chest, f'hole_ring_{i}', (x-1.7, y-1.25, -6.5), (3.4, 2.5, 13), 2,
              [0, 0, math.degrees(a)+90], [x, y, 0])
    # High rounded back, face buried between shoulders: no exposed neck.
    r.mass(chest, 'hunched_back', (0, 70, 9), (31, 19, 19), 0)
    for s in [-1, 1]:
        for j in range(3):
            r.box(chest, 'mantle_red_flame', (s*(9+j*5)-1.8, 73-j*.7, -5+j*2.5),
                  (3.6, 6.5, 1.1), 7, [-18, 0, -s*20], [s*(9+j*5), 76-j*.7, -4+j*2.5])
    # Continuous thin dorsal sail; stepped trailing edge and dark burgundy rim.
    fin = r.bone('dorsal_fin', (0, 64, 15), 'chest')
    for i, (y, depth) in enumerate([(47, 5), (53, 8), (59, 12), (65, 15), (71, 13), (77, 9), (82, 4)]):
        r.box(fin, f'dorsal_membrane_{i}', (-.65, y, 14), (1.3, 6 if i<6 else 2.8, depth), 0)
        r.box(fin, f'dorsal_rim_{i}', (-.85, y, 13+depth), (1.7, 6 if i<6 else 2.8, 1.4), 7)
    head = r.bone('head', (0, 74, -8), 'chest')
    r.mass(head, 'round_white_skull', (0, 77.4, -12.5), (17, 18, 13), 3)
    r.box(head, 'forehead_crown', (-5.7, 84, -17), (11.4, 2.4, 7), 3)
    r.box(head, 'bone_face', (-7.3, 73, -20), (14.6, 9, 2.1), 3)
    # Wide yellow oval eyes; curved black cheek markings and a broad toothy grin.
    for s in [-1, 1]:
        x = s*4.3
        r.mass(head, 'eye_socket', (x, 77.5, -20.5), (3.7, 5.1, 1), 5)
        r.mass(head, 'yellow_eye', (x, 77.6, -21.08), (2.25, 3.4, .35), 6)
        r.box(head, 'pupil_highlight', (x-.4, 77.7, -21.3), (.65, 1.4, .15), 3)
        r.box(head, 'brow_engraving', (x-1.8, 79.8, -20.6), (3.6, .7, .55), 5,
              [0, 0, -s*15], [x, 80, -20.3])
        r.box(head, 'mask_cheek_curve', (s*6.1-2, 73.7, -20.7), (4, .8, .55), 5,
              [0, 0, s*32], [s*6.1, 74, -20.3])
        r.box(head, 'mask_cheek_tip', (s*7.1-.4, 74.7, -20.6), (.8, 2.5, .6), 5,
              [0, 0, s*18], [s*7.1, 76, -20.3])
        r.box(head, 'temple_red_stripe', (s*6.5-1, 81.5, -18.4), (2, 4.2, 2), 7,
              [0, 0, -s*20], [s*6.5, 84, -16.4])
    r.box(head, 'nasal_cleft', (-.55, 74, -21.1), (1.1, 1.9, .5), 5)
    r.box(head, 'central_mask_crack', (-.17, 81.3, -20.2), (.34, 3.2, .35), 7)
    jaw = r.bone('jaw', (0, 72, -13), 'head')
    r.mass(jaw, 'wide_jawbone', (0, 68.5, -15), (13, 6.8, 8), 3)
    r.box(jaw, 'smile_dark_gap', (-5.8, 68.2, -20.7), (11.6, 4, 1.1), 5)
    for i in range(9):
        x = -5.2 + i*1.16
        curve = abs(i-4)*.18
        r.box(jaw, f'upper_tooth_{i}', (x, 70.2+curve, -21), (.9, 1.8, .65), 3,
              [0, 0, (i-4)*3], [x+.45, 71, -20.6])
        r.box(jaw, f'lower_tooth_{i}', (x, 68.3+curve, -20.98), (.9, 1.55, .65), 3)
    r.box(jaw, 'pointed_chin', (-2.3, 64.8, -18.3), (4.6, 2.7, 3.5), 3)
    for s, side in [(-1, 'right'), (1, 'left')]:
        x = s*21
        arm = r.bone(side+'_arm', (x, 69, 1), 'chest')
        r.mass(arm, 'massive_deltoid', (x, 67, 1), (14, 17, 15), 2)
        r.mass(arm, 'long_upper_arm', (s*23, 54, -1.5), (10, 28, 11), 2)
        r.box(arm, 'triceps_ridge', (s*23-2.4, 46, -7.1), (4.8, 17, 1.6), 4,
              [-9, 0, s*6], [s*23, 54, -6])
        forearm = r.bone(side+'_arm_lower', (s*24, 41, -3), side+'_arm')
        r.mass(forearm, 'elbow', (s*24, 40, -3), (9, 9, 10), 2)
        r.mass(forearm, 'long_forearm', (s*25, 27, -6.5), (9.5, 27, 11), 2)
        r.box(forearm, 'forearm_tendon', (s*25-2, 17, -12.1), (4, 22, 1.2), 4,
              [-10, 0, 0], [s*25, 28, -11])
        hand = r.bone(side+'_hand', (s*25, 14, -9), side+'_arm_lower')
        r.mass(hand, 'great_palm', (s*25, 10, -10), (13, 10, 10), 2)
        for i in range(4):
            fx = s*25-4.9+i*3.1
            r.mass(hand, f'knuckle_{i}', (fx, 6.2, -13), (2.9, 4.2, 4), 4)
            finger = r.bone(f'{side}_finger_{i}', (fx, 6, -12), side+'_hand')
            r.box(finger, 'support_finger', (fx-1.25, .7, -14.3), (2.5, 5.8, 4.3), 2)
            r.box(finger, 'fingertip', (fx-1.15, .15, -15.5), (2.3, 2.4, 3), 4)
        r.bone(side+'_fist_contact', (s*25, 4.5, -14), side+'_hand')
        thumb = r.bone(side+'_thumb', (s*20, 11, -10), side+'_hand')
        r.box(thumb, 'hooked_thumb', (s*18-1.8, 5.6, -12), (3.6, 7, 4.5), 2,
              [0, 0, -s*28], [s*20, 11, -10])
        leg = r.bone(side+'_leg', (s*8, 34, 3), 'body')
        r.mass(leg, 'thigh', (s*10, 26, .5), (10, 19, 11))
        r.box(leg, 'thigh_wine_flame', (s*10-1.8, 21, -5.3), (3.6, 13, .4), 7,
              [0, 0, -s*12], [s*10, 28, -5])
        shin = r.bone(side+'_leg_lower', (s*12, 17, -1), side+'_leg')
        r.mass(shin, 'bent_shin', (s*12, 10, 2), (7, 17, 8))
        foot = r.bone(side+'_foot', (s*12, 3, 1), side+'_leg_lower')
        r.mass(foot, 'long_foot', (s*12, 2.1, -3), (9, 4, 13), 2)
        for i in range(3):
            r.box(foot, f'pointed_toe_{i}', (s*12-3.5+i*2.8, .1, -12+(i==1)*-1.8), (2.2, 2, 6), 4)
    return r


def keys(times, values):
    return {str(t): {'post': v, 'lerp_mode': 'catmullrom'} for t,v in zip(times, values)}


def animations():
    clips = {}
    def add(name, length, bones, loop=False):
        clips['animation.boss.'+name] = {'loop': loop, 'animation_length': length, 'bones': bones}
    def wave(amplitude, phase=0):
        return f'math.sin(query.anim_time * 180 + {phase}) * {amplitude}'
    add('idle', 2, {'chest': {'rotation': [wave(.6), 0, 0]},
                    'head': {'rotation': [wave(-.5), 0, 0]}, 'jaw': {'rotation': [wave(.6), 0, 0]}}, True)
    walk = {'body': {'position': [0, f'math.abs(math.sin(query.anim_time * 180)) * .9', 0],
                     'rotation': [0, wave(2), wave(1.5)]},
            'chest': {'rotation': [wave(1.2), wave(-2), 0]}, 'head': {'rotation': [wave(-1), wave(1), 0]}}
    for side, phase in [('right', 0), ('left', 180)]:
        walk[side+'_arm'] = {'rotation': [wave(11, phase), 0, 0]}
        walk[side+'_arm_lower'] = {'rotation': [wave(7, phase+35), 0, 0]}
        walk[side+'_hand'] = {'rotation': [wave(-8, phase), 0, 0]}
        walk[side+'_leg'] = {'rotation': [wave(-19, phase), 0, 0]}
        walk[side+'_leg_lower'] = {'rotation': [wave(10, phase+35), 0, 0]}
        walk[side+'_foot'] = {'rotation': [wave(-6, phase), 0, 0]}
    add('walk', 2, walk, True)
    t = [0, .38, .6, .78, 1.3]
    melee = {'chest': {'rotation': keys(t, [[0,0,0], [-5,-14,0], [9,15,0], [4,7,0], [0,0,0]])},
             'right_arm': {'rotation': keys(t, [[0,0,0], [-95,-22,-14], [-55,22,12], [-20,15,8], [0,0,0]])},
             'right_arm_lower': {'rotation': keys(t, [[0,0,0], [35,0,0], [-15,0,0], [0,0,0], [0,0,0]])},
             'left_arm': {'rotation': keys(t, [[0,0,0], [-8,0,0], [10,0,0], [6,0,0], [0,0,0]])}}
    add('melee', 1.3, melee)
    t = [0, .5, .8, 1.05, 1.6, 1.8, 2.1]
    charge = {'chest': {'rotation': keys(t, [[0,0,0], [8,0,0], [10,0,0], [14,0,0], [14,0,0], [8,0,0], [0,0,0]])},
              'head': {'rotation': keys(t, [[0,0,0], [-8,0,0], [-10,0,0], [-12,0,0], [-12,0,0], [-6,0,0], [0,0,0]])}}
    for side, sign in [('right', -1), ('left', 1)]:
        charge[side+'_arm'] = {'rotation': keys(t, [[0,0,0], [-16,0,sign*6], [-25,0,sign*7], [-35,0,sign*7], [-30,0,sign*7], [-12,0,0], [0,0,0]])}
        charge[side+'_leg'] = {'rotation': [f'math.sin(query.anim_time * 900 + {0 if sign<0 else 180}) * 22 * math.clamp((query.anim_time - 0.8) * 8, 0, 1) * math.clamp((2.1 - query.anim_time) * 6, 0, 1)',0,0]}
    add('charge', 2.1, charge)
    # At 1.2 s / tick 24 both fists return to ground, 2.5 blocks ahead.
    t = [0, .35, .85, 1.0, 1.15, 1.4, 1.75, 2.1]
    slam = {'body': {'position': keys(t, [[0,0,0], [0,1,0], [0,2,0], [0,2,0], [0,-18.8,0], [0,-18.8,0], [0,-4,0], [0,0,0]])},
            'chest': {'rotation': keys(t, [[0,0,0], [-8,0,0], [-12,0,0], [-12,0,0], [0,0,0], [0,0,0], [0,0,0], [0,0,0]])},
            'jaw': {'rotation': keys(t, [[0,0,0], [3,0,0], [10,0,0], [13,0,0], [18,0,0], [10,0,0], [4,0,0], [0,0,0]])}}
    for side, sign in [('right', -1), ('left', 1)]:
        slam[side+'_arm'] = {'rotation': keys(t, [[0,0,0], [-65,0,sign*-10], [-155,0,sign*-14], [-165,0,sign*-14], [-35,0,sign*-8], [-35,0,sign*-8], [-18,0,sign*-4], [0,0,0]])}
        slam[side+'_arm_lower'] = {'rotation': keys(t, [[0,0,0], [20,0,0], [34,0,0], [25,0,0], [8,0,0], [8,0,0], [0,0,0], [0,0,0]])}
        slam[side+'_hand'] = {'rotation': keys(t, [[0,0,0], [-12,0,0], [-25,0,0], [-25,0,0], [27,0,0], [27,0,0], [0,0,0], [0,0,0]])}
        # Solve the two leg segments for a deep crouch while the feet keep their support.
        for part, angle, recovery in [('leg', -49.4, -20), ('leg_lower', 114.1, 46), ('foot', -64.7, -26)]:
            slam[side+'_'+part] = {'rotation': keys(t, [[0,0,0], [0,0,0], [0,0,0], [0,0,0],
                                                      [angle,0,0], [angle,0,0], [recovery,0,0], [0,0,0]])}
        for i in range(4):
            slam[f'{side}_finger_{i}'] = {'rotation': keys(t, [[0,0,0], [30,0,0], [70,0,0], [70,0,0], [65,0,0], [65,0,0], [25,0,0], [0,0,0]])}
    add('slam', 2.1, slam)
    return {'format_version': '1.8.0', 'animations': clips}


def editable_source(r, png):
    source = bbmodel(r, png)
    for name, clip in animations()['animations'].items():
        animators = {}
        for bone, channels in clip['bones'].items():
            frames = []
            for channel, track in channels.items():
                samples = {'0': {'post': track}} if isinstance(track, list) else track
                for time, key in samples.items():
                    frames.append({'channel': channel, 'time': float(time),
                                   'interpolation': key.get('lerp_mode', 'linear'),
                                   'data_points': [dict(zip(('x', 'y', 'z'), map(str, key['post'])))]})
            animators[uid('boss'+bone)] = {'name': bone, 'type': 'bone', 'keyframes': frames}
        source['animations'].append({'uuid': uid(name), 'name': name, 'length': clip['animation_length'],
                                     'loop': 'loop' if clip['loop'] else 'once', 'snapping': 20,
                                     'animators': animators})
    return source


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--check', action='store_true')
    args = parser.parse_args()
    r = rig()
    png = texture(COLORS)
    geometry = {'format_version': '1.12.0', 'minecraft:geometry': [{
        'description': {'identifier': 'geometry.bleachmod.hollow_boss', 'texture_width': 128, 'texture_height': 32,
                        'visible_bounds_width': 12, 'visible_bounds_height': 12, 'visible_bounds_offset': [0, 3.5, 0]}, 'bones': r.bones}]}
    files = {ASSETS/'geo/hollow_boss.geo.json': geometry,
             ASSETS/'animations/hollow_boss.animation.json': animations(),
             ROOT/'tools/models/source/hollow_boss.bbmodel': editable_source(r, png)}
    blobs = {p: (json.dumps(d, indent=2)+'\n').encode() for p,d in files.items()}
    blobs[ASSETS/'textures/entity/hollow_boss.png'] = png
    names = {b['name'] for b in r.bones}
    assert len(names) == len(r.bones)
    assert all(b.get('parent', 'root') in names for b in r.bones)
    for b in r.bones:
        for c in b['cubes']:
            assert all(n > 0 for n in c['size'])
            for face in c['uv'].values():
                assert 0 <= face['uv'][0] < face['uv'][0]+face['uv_size'][0] <= 128
                assert 0 <= face['uv'][1] < face['uv'][1]+face['uv_size'][1] <= 32
    for clip in animations()['animations'].values():
        assert set(clip['bones']) <= names
        for channels in clip['bones'].values():
            for channel, track in channels.items():
                if isinstance(track, dict):
                    assert min(map(float, track)) == 0
                    assert max(map(float, track)) == clip['animation_length']
                    assert next(iter(track.values()))['post'] == list(track.values())[-1]['post'], 'Recovery pose missing'
                else:
                    assert len(track) == 3
    assert max(c['origin'][1]+c['size'][1] for b in r.bones for c in b['cubes']) == 86.4
    for path, data in blobs.items():
        if args.check:
            assert path.read_bytes() == data, f'Outdated asset: {path}'
        else:
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_bytes(data)
    print(f'Boss: {len(names)} bones, {len(r.parts)} cubes, 5 clips, 5.4 blocks; source/runtime '+('verified' if args.check else 'written'))

if __name__ == '__main__':
    main()
