"""Organic ice hydra: articulated chains, shared by entrance and combat poses.

Coordinates are Bedrock model units (16 per block). Segment pivots are relative
to one neck root. Runtime poses each link along the shared cubic curve, so there
are no rigid column-shaped necks or disconnected projectile heads.
"""
NAMES = ('center', 'left', 'right')
LINKS = 12


def box(origin, size, uv=(64, 32)):
    # Atlas islands fit inside 128x128, including teeth and eyes.
    return {'origin':origin, 'size':size, 'uv':{
        face:{'uv':list(uv), 'uv_size':[min(14,size[0] if face in ('north','south','up','down') else size[2]),
                                      min(14,size[2] if face in ('up','down') else size[1])]}
        for face in ('north','south','east','west','up','down')}}


def build_geo_json():
    bones=[{'name':'root','pivot':[0,0,0],'cubes':[]}]
    for side,name in enumerate(NAMES):
        x=(0,42,-42)[side]
        bones.append({'name':'root_'+name,'parent':'root','pivot':[0,0,0], 'cubes':[
            box([x-24,0,-16],[48,7,36]),box([x-14,3,-9],[28,12,22],(16,0))]})
        for i in range(LINKS):
            r=25.5-i*.75
            cubes=[box([-r,0,-r*.7],[r*2,16,r*1.4]),
                   box([-r*.7,0,-r],[r*1.4,16,r*2]),
                   box([-3,4,r-2],[6,11,8],(16,0)),
                   box([-r*.75,0,-r*.75],[r*1.5,3,r*1.5],(64,16))]
            bones.append({'name':f'neck_{name}_{i+1}','parent':'root_'+name,'pivot':[0,0,0],
                          'cubes':cubes})
        # Skull, long muzzle, brows and crown, with a separate movable jaw.
        cubes=[box([-16,-2,-14],[32,12,26],(64,32)),box([-17,0,-18],[34,21,30],(64,0)),box([-12,-8,-54],[24,14,39],(64,16)),
               box([-9,-3,-64],[18,8,14],(64,16))]
        for sign in (-1,1):
            cubes.extend([box([sign*16-2,9,-24],[4,5,15],(4,4)),
                          box([sign*15-3,14,-27],[6,5,21],(16,0)),
                          box([sign*12-3,18,0],[6,22,10],(16,0)),
                          box([sign*8-2,18,7],[4,28,7],(16,0))])
            for z in (-48,-39,-30,-21):cubes.append(box([sign*9-1.5,-14,z],[3,9,3],(16,0)))
        bones.append({'name':'head_'+name,'parent':'root_'+name,'pivot':[0,0,0],
                      'cubes':cubes})
        jaw=[box([-11,-20,-57],[22,7,42],(64,32))]
        for sign in (-1,1):
            for z in (-49,-40,-31,-22):jaw.append(box([sign*8-1.5,-16,z],[3,9,3],(16,0)))
        bones.append({'name':'jaw_'+name,'parent':'head_'+name,'pivot':[0,-9,-12],'cubes':jaw})
    return {'format_version':'1.12.0','minecraft:geometry':[{
        'description':{'identifier':'geometry.bleachmod.ice_hydra','texture_width':128,'texture_height':128,
                       'visible_bounds_width':100,'visible_bounds_height':48,'visible_bounds_offset':[0,12,0]},
        'bones':bones}]}


def build_anim_json():
    # Source animation is editable; the runtime overlays target-aware neck poses.
    idle={};emerge={}
    for index,name in enumerate(NAMES):
        for i in range(LINKS):
            y=8+i*13
            idle[f'neck_{name}_{i+1}']={'position':{'0.0':[index*18-18,y,0],'1.5':[index*18-18,y,3],'3.0':[index*18-18,y,0]}}
        emerge['head_'+name]={'position':{'0.0':[0,0,0],'2.5':[0,160,0],'7.6':[0,160,0]},
                              'scale':{'0.0':[.01,.01,.01],'2.5':[1,1,1],'7.6':[1,1,1]}}
        emerge['jaw_'+name]={'rotation':{'0.0':[0,0,0],'3.0':[6,0,0],'4.0':[36,0,0],'5.5':[32,0,0],'7.6':[0,0,0]}}
    return {'format_version':'1.8.0','animations':{
        'animation.ice_hydra.flight':{'loop':True,'animation_length':3,'bones':idle},
        'animation.ice_hydra.emerge':{'loop':False,'animation_length':7.6,'bones':emerge},
        'animation.ice_hydra.assault':{'loop':False,'animation_length':2.8,'bones':{}},
        'animation.ice_hydra.roar':{'loop':False,'animation_length':1,'bones':{
            'jaw_'+n:{'rotation':{'0.0':[0,0,0],'0.4':[40,0,0],'1.0':[0,0,0]}} for n in NAMES}}
    }}
