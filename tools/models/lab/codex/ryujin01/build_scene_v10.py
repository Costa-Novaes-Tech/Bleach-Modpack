"""Insert a three-second full-sun hold; preserve v9 models and the original clip."""
from pathlib import Path
import copy, json, uuid

HERE=Path(__file__).resolve().parent
ROOT=HERE.parents[4]
CUT=5.6
HOLD=3.0
RINGS={'inner_swirl','mid_swirl','outer_swirl','broken_rim'}

def vector(entry):
    if isinstance(entry,list): return entry
    if 'vector' in entry: return entry['vector']
    return vector(entry.get('post',entry.get('pre')))

def bracket(points):
    ordered=sorted(points)
    lo=max((t for t in ordered if t<=CUT),default=ordered[0])
    hi=min((t for t in ordered if t>CUT),default=ordered[-1])
    return lo,hi

def retime(points, spin=False):
    lo,hi=bracket(points)
    a,b=vector(points[lo]),vector(points[hi])
    u=max(0,min(1,(CUT-lo)/(hi-lo))) if hi!=lo else 0
    at=[float(x)+(float(y)-float(x))*u for x,y in zip(a,b)]
    rate=(float(b[2])-float(a[2]))/(hi-lo) if spin and hi!=lo else 0
    out={}
    def shifted(value,amount):
        item=copy.deepcopy(value)
        if isinstance(item,list): item[2]=float(item[2])+amount
        elif 'vector' in item: item['vector'][2]=float(item['vector'][2])+amount
        else:
            for edge in ('pre','post'):
                if edge in item: item[edge]=shifted(item[edge],amount)
        return item
    for t,value in sorted(points.items()):
        out[t if t<=CUT else round(t+HOLD,5)]=shifted(value,rate*HOLD if t>CUT else 0)
    # An explicit pair prevents interpolation across the new scenic hold.
    out[CUT]={'vector':at}
    out[CUT+HOLD]={'vector':[at[0],at[1],at[2]+rate*HOLD]}
    return dict(sorted(out.items()))

def main():
    assets=ROOT/'src/main/resources/assets/bleachmod/animations'
    document=json.loads((assets/'ryujin_bankai_sun.animation.json').read_text())
    clip=document['animations']['animation.ryujin.bankai_sun']
    clip['animation_length']=15
    for bone,channels in clip['bones'].items():
        for channel,points in channels.items():
            channels[channel]={str(t):v for t,v in retime({float(k):v for k,v in points.items()},bone in RINGS and channel=='rotation').items()}
    (assets/'ryujin_bankai_sun_v10.animation.json').write_text(json.dumps(document,indent=2),encoding='utf-8')
    for suffix in ('','_pose'):
        source=HERE/f'ryujin_bankai_flame_wall_v9_lab{suffix}.bbmodel'
        model=json.loads(source.read_text())
        name=f'ryujin_bankai_sustained_sun_v10_lab{suffix}'
        model['name']=model['model_identifier']=name
        animation=model['animations'][0]
        animation['name']='animation.ryujin.sustained_sun_v10_lab'
        animation['length']=15
        for animator in animation['animators'].values():
            channels={}
            original=animator.get('keyframes',[])
            for key in original:
                values=[float(key['data_points'][0][axis]) for axis in ('x','y','z')]
                channels.setdefault(key['channel'],{})[float(key['time'])]={'vector':values}
            frames=[]
            for channel,points in channels.items():
                for t,value in retime(points,animator['name'] in RINGS and channel=='rotation').items():
                    frames.append({'channel':channel,'time':t,'uuid':str(uuid.uuid5(uuid.NAMESPACE_URL,f'{name}:{animator["name"]}:{channel}:{t}')),
                        'color':-1,'interpolation':original[0].get('interpolation','linear'),'data_points':[dict(zip(('x','y','z'),map(str,vector(value))))]})
            animator['keyframes']=frames
        (HERE/f'{name}.bbmodel').write_text(json.dumps(model,indent=2),encoding='utf-8')
    print('Wrote separate fifteen-second runtime clip and lab projects; v9 preserved.')

if __name__=='__main__': main()
