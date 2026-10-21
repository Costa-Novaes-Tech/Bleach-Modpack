"""Original fencing curves shared by the Better Combat runtime and editable reference.

Run from any directory. No third-party animations are copied. Angles are degrees
here; the runtime format uses radians, the Bedrock reference uses degrees.
"""
import json
import math
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2] / 'src/main/resources/assets/bleachmod'
TIMES = [0, 2, 4, 6, 8, 10, 12, 17, 20]
# Preparation -> acceleration -> blade crossing -> follow-through -> recovery.
CURVES = {
    'diagonal': {
        'rightArm': [[-32,12,-10],[-70,30,-26],[-128,32,-40],[-145,24,-36],[-90,-12,18],[-48,-40,38],[-35,-46,32],[-38,-12,2],[-32,12,-10]],
        'leftArm': [[-28,-12,-15],[-38,-18,-20],[-46,-25,-24],[-42,-22,-20],[-32,12,-12],[-22,28,-8],[-20,30,-12],[-26,0,-14],[-28,-12,-15]],
        'torso': [[3,-8,0],[5,-18,-2],[6,-30,-5],[4,-32,-4],[1,6,2],[-3,30,6],[-2,34,4],[2,5,0],[3,-8,0]],
    },
    'return': {
        'rightArm': [[-38,-28,24],[-55,-46,40],[-78,-58,62],[-88,-54,70],[-91,10,-20],[-60,42,-40],[-42,48,-36],[-35,10,-4],[-32,12,-10]],
        'leftArm': [[-22,24,-10],[-30,30,-15],[-38,36,-18],[-36,28,-22],[-40,-12,-18],[-32,-26,-12],[-22,-28,-12],[-26,-12,-14],[-28,-12,-15]],
        'torso': [[3,10,0],[4,24,2],[5,32,4],[3,34,4],[1,-6,-2],[-2,-30,-5],[-1,-34,-4],[2,-14,-1],[3,-8,0]],
    },
    'thrust': {
        'rightArm': [[-32,12,-10],[-54,24,-20],[-70,32,-28],[-74,30,-26],[-94,-4,-4],[-96,-8,0],[-90,-6,-3],[-54,12,-12],[-32,12,-10]],
        'leftArm': [[-28,-12,-15],[-32,-20,-22],[-40,-28,-30],[-42,-28,-30],[-24,20,-30],[-18,28,-34],[-20,22,-28],[-26,0,-18],[-28,-12,-15]],
        'torso': [[3,-8,0],[4,-16,-2],[5,-24,-3],[5,-24,-3],[-9,12,0],[-11,18,2],[-7,16,1],[1,-2,0],[3,-8,0]],
    },
}


def main():
    for name, curves in CURVES.items():
        # Counter-rotation keeps the gaze on the opponent, rather than twisting
        # the head and torso together. BC disables leg channels while walking.
        curves['head'] = [[-p*.3, -y*.45, -r*.3] for p,y,r in curves['torso']]
        curves['rightLeg'] = [[v,0,2] for v in [0,3,7,8,-6,-9,-7,-2,0]]
        curves['leftLeg'] = [[-v,0,-2] for v in [0,3,7,8,-6,-9,-7,-2,0]]
        moves=[]
        bones={}
        for part, keys in curves.items():
            for tick, angles in zip(TIMES, keys):
                moves.append({'tick':tick,'easing':'EASEINOUTSINE' if tick < 6 or tick > 12 else 'EASEOUTQUAD','turn':0,
                              part:dict(zip(['pitch','yaw','roll'],map(math.radians,angles)))})
            bone={'rightArm':'right_arm','leftArm':'left_arm','rightLeg':'right_leg','leftLeg':'left_leg','torso':'body'}.get(part,part)
            bones[bone]={'rotation':{f'{tick/20:.2f}':{'vector':angles,'easing':'easeInOutSine'} for tick,angles in zip(TIMES,keys)}}
        stem='benihime_'+name
        runtime={'name':stem,'author':'Bleach Mod team','emote':{'isLoop':False,'beginTick':0,'endTick':12,'stopTick':20,'returnTick':0,'degrees':False,'moves':moves}}
        reference={'format_version':'1.8.0','animations':{stem:{'animation_length':1,'loop':False,'bones':bones}}}
        for folder,data in [('attack_animations',runtime),('player_animation',reference)]:
            (ROOT/folder/(stem+'.json')).write_text(json.dumps(data,indent=2)+'\n',encoding='utf-8')


if __name__ == '__main__':
    main()
