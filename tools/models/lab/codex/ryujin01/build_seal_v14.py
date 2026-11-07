"""Reproducible alpha material for the v14 seal ribbons; no external source art."""
from pathlib import Path
import math,hashlib,json,base64,io,uuid
from PIL import Image

ROOT=Path(__file__).resolve().parents[5]
HERE=Path(__file__).resolve().parent

def smooth(t):
    t=max(0,min(1,t));return t*t*(3-2*t)
def phase(t):
    real=[0,40,70,84,112,176,202,220,270,300];story=[0,12,20,24,30,32,52,60,112,140]
    for i in range(1,len(real)):
        if t<=real[i]:return story[i-1]+(story[i]-story[i-1])*(t-real[i-1])/(real[i]-real[i-1])
    return 140
def mix(a,b,t):return [x+(y-x)*t for x,y in zip(a,b)]
def radius(p):
    if p<20:return 0
    if p<30:return 9*smooth((p-20)/10)
    if p<32:return 9+math.sin(p*.4)*.15
    return 9*(1-smooth((p-32)/20)) if p<52 else 0
def blade():
    # Held side pose from RyujinChoreography's arm/display anchor, in model-facing -Z space.
    hand=[(.3125-.625*math.sin(.10))*16,(1.375-.625*math.cos(.10))*16,0]
    return hand,[hand[0]+1.35*math.cos(math.radians(10)+.10)*16,hand[1]-1.35*math.sin(math.radians(10)+.10)*16,0]
def flow(t,stream,i):
    p=phase(t);envelope=smooth((p-32)/3)*(1-smooth((p-50)/2)) if 32<p<52 else 0
    hand,tip=blade();end=mix(hand,tip,.25+.18*stream)
    centre=mix([0,96,128],mix(hand,tip,.55),smooth((p-32)/20));r=max(.15,radius(p)*.7)*16
    a=stream*math.pi*.5+t*.045
    start=[centre[0]+math.cos(a)*r,centre[1]+math.sin(a)*r,centre[2]]
    control=mix(start,end,.48);control[0]+=math.sin(a+.8)*r*.65;control[1]+=.55*16+r*.18;control[2]-=.45*16
    def point(u):return [(1-u)**2*x+2*(1-u)*u*y+u*u*z for x,y,z in zip(start,control,end)]
    a0,a1=point(i/8),point((i+1)/8);d=[y-x for x,y in zip(a0,a1)];length=math.sqrt(sum(x*x for x in d))
    mid=mix(a0,a1,.5);u=(i+.5)/8
    width=(.36+.12*stream)*envelope*(.55+.45*math.sin(math.pi*u))*(1-.72*u)*16
    rot=[math.degrees(math.asin(d[2]/max(.0001,length))),0,-math.degrees(math.atan2(d[0],d[1]))]
    return mid,rot,width,length,envelope

def lab(material,eye):
    """Animation study, not a replacement for Java sampler/renderer validation."""
    model=json.loads((HERE/'ryujin_bankai_continuous_sun_v13_lab_scene.bbmodel').read_text())
    model['name']=model['model_identifier']='ryujin_seal_v14_scene_lab'
    atlas=Image.open(io.BytesIO(base64.b64decode(model['textures'][0]['source'].split(',',1)[1]))).convert('RGBA')
    atlas.paste(material,(0,3328));atlas.paste(eye,(64,3328));buf=io.BytesIO();atlas.save(buf,format='PNG')
    model['textures'][0]['source']='data:image/png;base64,'+base64.b64encode(buf.getvalue()).decode()
    anim=model['animations'][0];anim['name']='animation.ryujin.seal_v14_lab'
    def key(channel,time,values):
        return {'channel':channel,'time':round(time,4),'interpolation':'linear','data_points':[dict(zip(('x','y','z'),map(str,values)))],'uuid':str(uuid.uuid5(uuid.NAMESPACE_URL,f'v14:{channel}:{time}:{values}'))}
    groups={g['name']:g for g in model['groups']}
    for name,group in groups.items():
        if name.startswith('absorb_spark_'):
            anim['animators'][group['uuid']]={'name':name,'type':'bone','keyframes':[key('scale',0,[0,0,0]),key('scale',15,[0,0,0])]}
    root=model['outliner'][0]
    def find(node,uid):
        if node['uuid']==uid:return node
        for child in node.get('children',[]):
            if isinstance(child,dict):
                found=find(child,uid)
                if found:return found
    head=find(root,groups['head']['uuid'])
    # Native head is Y-up, anatomical right +X; runtime PlayerModel is inverted.
    # Staging plane sits outside heat_face. It is a study, not the in-game skin layer.
    def eye_pose(tick,i):
        power=smooth((tick-20)/12)*(1-smooth((tick-52)/16)) if 20<tick<68 else 0
        def tail(t):return [(.125+.28*t)*16,24+(.245+.043*t+.008*math.sin(t*4-tick*.31)*t)*16,-4.60-.008*t*16]
        if i<4:
            t=i*.035 if i<2 else (i-1)/3
            life=.5+.5*math.sin(tick*.62-i*1.7);size=(.026+.012*life)*(1-.68*t)*power*16
            at=tail(t);at[2]-=i*.12
            return at,[0,0,0],[size,size*(1.3+.5*life),1] if power>.005 else [0,0,0]
        a,b=tail((i-4)/4),tail((i-3)/4);a[2]=b[2]=-5.8
        d=[y-x for x,y in zip(a,b)];length=math.sqrt(sum(v*v for v in d))
        return mix(a,b,.5),[0,0,-math.degrees(math.atan2(d[0],d[1]))],[.012*power*(1-.72*(i-4)/4)*16,length,1] if power>.005 else [0,0,0]
    for i in range(8):
        name=f'right_eye_{i}';gid=str(uuid.uuid5(uuid.NAMESPACE_URL,name));eid=str(uuid.uuid5(uuid.NAMESPACE_URL,name+':cube'));rest=eye_pose(39,i)[0]
        model['groups'].append({'name':name,'uuid':gid,'origin':rest,'rotation':[0,0,0],'export':True,'color':3})
        head['children'].append({'uuid':gid,'children':[eid],'isOpen':False})
        uv=[64,3328,80,3360] if i<4 else [0,3328,64,3456]
        faces={f:{'uv':uv[:],'texture':0} for f in ['north','south','east','west','up','down']}
        for f in ['east','west','up','down']:faces[f]['uv']=[64,3328,65,3329]
        model['elements'].append({'name':name,'uuid':eid,'type':'cube','box_uv':False,'from':[rest[0]-.5,rest[1]-.5,rest[2]-.05],'to':[rest[0]+.5,rest[1]+.5,rest[2]+.05],'origin':rest,'rotation':[0,0,0],'faces':faces,'autouv':0,'export':True})
        keys=[]
        for tick in [0,*range(20,69,2),300]:
            mid,rot,scale=eye_pose(tick,i)
            keys.extend([key('position',tick/20,[v-b for v,b in zip(mid,rest)]),key('rotation',tick/20,rot),key('scale',tick/20,scale)])
        anim['animators'][gid]={'name':name,'type':'bone','keyframes':keys}
    for s in range(4):
        for i in range(8):
            name=f'seal_{s}_{i}';gid=str(uuid.uuid5(uuid.NAMESPACE_URL,name));eid=str(uuid.uuid5(uuid.NAMESPACE_URL,name+':cube'))
            rest=flow(188,s,i)[0]
            model['groups'].append({'name':name,'uuid':gid,'origin':rest,'rotation':[0,0,0],'export':True,'color':3})
            root['children'].append({'uuid':gid,'children':[eid],'isOpen':False})
            faces={f:{'uv':[0,3328,64,3456],'texture':0} for f in ['north','south','east','west','up','down']}
            for f in ['east','west','up','down']:faces[f]['uv']=[0,3328,1,3329]
            model['elements'].append({'name':name,'uuid':eid,'type':'cube','box_uv':False,'from':[rest[0]-.5,rest[1]-.5,rest[2]-.05],'to':[rest[0]+.5,rest[1]+.5,rest[2]+.05],'origin':rest,'rotation':[0,0,0],'faces':faces,'autouv':0,'export':True})
            keys=[]
            for tick in [0,170,*range(176,203),210,300]:
                mid,rot,width,length,envelope=flow(tick,s,i)
                keys.extend([key('position',tick/20,[v-b for v,b in zip(mid,rest)]),key('rotation',tick/20,rot),key('scale',tick/20,[width,length,1] if envelope>.001 else [0,0,0])])
            anim['animators'][gid]={'name':name,'type':'bone','keyframes':keys}
    # Match the pilot's shared solar centre and scale; native origin already lies at its pivot.
    solar=anim['animators'][groups['sun_root']['uuid']]
    solar['keyframes']=[k for k in solar['keyframes'] if k['channel'] not in ['position','scale']]
    for tick in [0,40,70,84,100,112,144,176,*range(178,203,2),220,300]:
        p=phase(tick);hand,tip=blade();centre=mix([0,96,128],mix(hand,tip,.55),smooth((p-32)/20));scale=radius(p)/9*1.35
        solar['keyframes'].extend([key('position',tick/20,[centre[0],centre[1]-80,centre[2]-50]),key('scale',tick/20,[scale]*3)])
    target=HERE/'ryujin_seal_v14_scene_lab.bbmodel';target.write_text(json.dumps(model,indent=2),encoding='utf-8')
    print('Lab study:',target.name,'cubes',len(model['elements']),'bones',len(model['groups']))
def main():
    image=Image.new('RGBA',(64,128))
    for y in range(128):
        for x in range(64):
            u=(x-31.5)/31.5
            grain=.84+.16*math.sin(y*.23+u*5)
            alpha=int(255*max(0,1-abs(u))**.65*grain)
            hot=max(0,1-abs(u)*3)
            image.putpixel((x,y),(255,int(140+115*hot),int(28+227*hot),alpha))
    target=ROOT/'src/main/resources/assets/bleachmod/textures/effect/ryujin_seal_flow.png'
    target.parent.mkdir(parents=True,exist_ok=True);image.save(target)
    print(json.dumps({'texture':str(target.relative_to(ROOT)),'sha256':hashlib.sha256(target.read_bytes()).hexdigest()}))
    eye=Image.new('RGBA',(16,32))
    for y in range(32):
        rise=1-y/31
        centre=7.5+1.8*math.sin(rise*5)*rise
        spread=1+5*(1-rise)**.7
        for x in range(16):
            hot=max(0,1-abs(x-centre)/spread)
            alpha=int(255*hot*min(1,(1-rise)*9)*min(1,(y+1)/3))
            eye.putpixel((x,y),(255,int(125+130*hot),int(12+225*hot*hot),alpha))
    eye.save(target.with_name('ryujin_eye_flame.png'))
    fire=Image.new('RGBA',(128,512))
    for frame in range(8):
        for variant in range(2):
            turn=frame*math.tau/8+variant*1.4
            for y in range(64):
                rise=1-y/63
                centre=31.5+math.sin(rise*6+turn)*6*rise+math.sin(rise*13-turn)*2*rise
                spread=1.2+26*(1-rise)**.70
                for x in range(64):
                    body=max(0,1-abs(x-centre)/spread)
                    lobe=max(0,1-abs(x-(centre-11+4*math.sin(turn)))/(1+12*(1-rise)**1.4))*.68
                    second=max(0,1-abs(x-(centre+12-3*math.cos(turn)))/(1+10*(1-rise)**1.8))*.58
                    density=max(body,lobe,second)
                    edge=max(0,min(1,y/4,(63-y)/3))
                    alpha=int(245*density**.72*edge)
                    hot=body**2*(1-rise*.65)
                    fire.putpixel((variant*64+x,frame*64+y),(255,int(85+170*hot),int(8+237*hot**2),alpha))
    fire.save(target.with_name('ryujin_fire_sheet.png'))
    lab(image,eye)
if __name__=='__main__':main()
