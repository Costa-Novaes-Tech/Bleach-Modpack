"""Estudos autorais de movimento, NÃO captura de Minecraft nem animações do runtime.
Render ortográfico procedural com Pillow; execução: python gerar_previs.py.
"""
from pathlib import Path
from math import sin, cos, pi
import random
from PIL import Image, ImageDraw, ImageFont, ImageFilter

OUT = Path(__file__).resolve().parent
W, H, S = 640, 400, 27
CAMERA_SHIFT=0
FONT = ImageFont.truetype('C:/Windows/Fonts/segoeui.ttf', 17)
SMALL = ImageFont.truetype('C:/Windows/Fonts/segoeui.ttf', 13)
TITLE = ImageFont.truetype('C:/Windows/Fonts/segoeuib.ttf', 21)
CLIPS = [
 ('01-selo-bankai', 'Bankai · selo e pose breve', 'Transformação', 4.0),
 ('02-corte-frontal', 'H · Corte frontal', 'Selada', 2.8),
 ('03-varredura', 'N · Varredura', 'Selada', 2.8),
 ('04-rajada', 'H · Rajada de Chamas', 'Shikai', 3.2),
 ('05-leque', 'N · Leque de Fogo', 'Shikai', 3.2),
 ('06-tornado', 'B · Tornado de Fogo', 'Shikai', 3.6),
 ('07-jokaku', 'C · Jōkaku Enjō', 'Shikai', 3.6),
 ('08-investida', 'H · Investida de Chamas', 'Bankai', 3.0),
 ('09-higashi', 'N · Higashi', 'Bankai', 3.2),
 ('10-minami', 'B · Minami', 'Bankai', 3.8),
 ('11-vapor', 'C · Corte de Vapor', 'Bankai', 3.6),
]

def clamp(v): return max(0, min(1, v))
def smooth(v): v=clamp(v); return v*v*(3-2*v)
def mix(a,b,t): return tuple(x+(y-x)*t for x,y in zip(a,b))
def add(a,b): return tuple(x+y for x,y in zip(a,b))
def sub(a,b): return tuple(x-y for x,y in zip(a,b))
def scale(a,t): return tuple(x*t for x in a)
def project(p):
    x,y,z=p
    return (W*.43+(x+CAMERA_SHIFT)*S+z*S*.48, H*.74-y*S+z*S*.24)
def depth(p): return p[2]-p[0]*.35

def frame(index,t):
    global S,CAMERA_SHIFT
    S=43 if index in (0,1,2,8) else 27
    CAMERA_SHIFT=2.4 if S==43 else 0
    rng=random.Random(1000+int(t*120)+index*10000)
    im=Image.new('RGB',(W,H),(15,20,28)); draw=ImageDraw.Draw(im)
    for k in range(-12,16,2):
        draw.line([project((k,0,-7)),project((k,0,9))],fill=(31,39,48))
    for k in range(-6,11,2):
        draw.line([project((-12,0,k)),project((16,0,k))],fill=(31,39,48))
    faces=[]; particles=[]
    def box(center,size,color):
        x,y,z=center; a,b,c=(v/2 for v in size)
        verts=[(x+dx*a,y+dy*b,z+dz*c) for dx,dy,dz in [(-1,-1,-1),(1,-1,-1),(1,1,-1),(-1,1,-1),(-1,-1,1),(1,-1,1),(1,1,1),(-1,1,1)]]
        for ids,light in [([0,1,2,3],.75),([4,5,6,7],1),([0,4,7,3],.62),([1,5,6,2],.90),([3,2,6,7],1.2)]:
            pts=[verts[i] for i in ids]
            faces.append((sum(depth(p) for p in pts)/4,pts,tuple(min(255,int(v*light)) for v in color)))
    def rod(a,b,width,color):
        # Render a bevelled solid beam in projected space, not a screen-wide translucent ribbon.
        pa,pb=project(a),project(b); dx,dy=pb[0]-pa[0],pb[1]-pa[1];length=max(1,(dx*dx+dy*dy)**.5)
        side=(-dy/length*width*S/2,dx/length*width*S/2)
        pts=[(pa[0]+side[0],pa[1]+side[1]),(pb[0]+side[0],pb[1]+side[1]),(pb[0]-side[0],pb[1]-side[1]),(pa[0]-side[0],pa[1]-side[1])]
        faces.append(((depth(a)+depth(b))/2,pts,color,'screen'))
    def flame(p,n=1,size=1,white=False):
        for _ in range(n):
            q=add(p,(rng.uniform(-.16,.16),rng.uniform(0,.45),rng.uniform(-.16,.16)))
            particles.append((q,rng.uniform(2,5)*size,white,rng.random()))
    def figure(base,hand=None,tip=None,lean=0,yaw=0,step=0,ash=False):
        def tr(q):
            x,y,z=q; return add(base,(x*cos(yaw)+z*sin(yaw)+lean*y,y,-x*sin(yaw)+z*cos(yaw)))
        cloth=(55,53,52) if ash else (29,31,36)
        skin=(95,90,83) if ash else (213,176,143)
        box(tr((0,1.8,0)),(.8,1.05,.65),cloth)
        if not ash:
            box(tr((-.17,1.95,0)),(.65,1.05,.74),(206,204,188))
            box(tr((.25,1.46,0)),(.16,.14,.67),(210,208,188))
        box(tr((0,2.65,0)),(.64,.65,.63),skin)
        if not ash: box(tr((-.07,2.97,0)),(.66,.12,.65),(214,215,209))
        for z,phase in [(-.23,1),(.23,-1)]:
            hip=tr((0,1.3,z)); knee=tr((phase*step,.65,z)); foot=tr((-phase*step,0.18,z))
            rod(hip,knee,.32,cloth);rod(knee,foot,.31,cloth);box(foot,(.48,.25,.37),(68,65,58))
        shoulder=tr((0,2.15,-.46))
        hand=hand or tr((.43,1.38,-.49))
        elbow=add(mix(shoulder,hand,.50),(-.2,-.1,-.12))
        rod(shoulder,elbow,.28,cloth);rod(elbow,hand,.25,skin)
        otherS=tr((0,2.15,.43));otherH=tr((.15,1.32,.52))
        rod(otherS,otherH,.28,cloth)
        if tip is not None:
            v=sub(tip,hand)
            rod(add(hand,scale(v,-.2)),hand,.13,(88,62,41))
            rod(add(hand,(0,0,-.18)),add(hand,(0,0,.18)),.09,(208,161,75))
            rod(hand,tip,.09,(75,62,51) if index==0 and t>.52 or index>=7 else (208,221,224))
        return hand
    # 0.3–0.56 is impact; anticipation and follow-through remain visible.
    strike=smooth((t-.27)/.28); recover=smooth((t-.64)/.20)
    load=sin(clamp(t/.30)*pi/2)*(1-recover)
    bx=-4.6
    if index==7: bx+=smooth((t-.27)/.32)*5
    if index==8: bx+=smooth((t-.32)/.15)*.65 # proposed visual step; no automatic gameplay dash
    base=(bx,0,-.5)
    neutral=add(base,(.48,1.4,-.5)); neutralTip=add(neutral,(1.0,-1.3,0))
    hand=neutral; tip=neutralTip; lean=0; yaw=0; step=0
    if index==0:
        q=smooth(t/.18); hand=mix(neutral,add(base,(.45,1.55,-.65)),q)
        tip=add(hand,(1.95,.02,-.30)) # outward, never through the torso
        r=smooth((t-.78)/.15);hand=mix(hand,neutral,r);tip=mix(tip,neutralTip,r)
    elif index in (1,3,10):
        angle=(-1.20+2.55*strike)
        prepared=add(base,(.2,2.1,-.55)); hand=mix(neutral,prepared,load)
        tip=add(hand,(1.9*sin(angle),1.9*cos(angle),-.12))
        tip=mix(tip,neutralTip,recover);hand=mix(hand,neutral,recover)
        lean=.11*sin(strike*pi)*(1-recover);step=.3*load
    elif index==8:
        # Higashi is coiled at the hip, unlike Rajada's overhead preparation.
        hand=mix(add(base,(-.05,1.35,-.65)),add(base,(.9,1.75,-.65)),strike)
        angle=-2.9+3.1*strike
        tip=add(hand,(2.0*cos(angle),.9*sin(angle),-.30))
        hand=mix(hand,neutral,recover);tip=mix(tip,neutralTip,recover)
        yaw=(-.4+.7*strike)*load;lean=.1*sin(strike*pi)*(1-recover);step=.3*load
    elif index in (2,4):
        angle=-1.2+2.4*strike
        hand=add(base,(.5,1.75,-.5));tip=add(hand,(1.7*cos(angle),.1,1.7*sin(angle)))
        hand=mix(hand,neutral,recover);tip=mix(tip,neutralTip,recover);yaw=(-.25+.5*strike)*load;step=.25*load
    elif index==5:
        hand=add(base,(.3,2.0,-.3));a=t*pi*6;tip=add(hand,(1.7*cos(a),.4,1.7*sin(a)))
        hand=mix(hand,neutral,recover);tip=mix(tip,neutralTip,recover);yaw=.35*sin(t*pi*4)*(1-recover)
    elif index in (6,9):
        hand=mix(neutral,add(base,(.9,1.8,-.4)),load)
        tip=mix(add(hand,(.4,1.9,0)),add(base,(1.4,.1,-.35)),strike)
        tip=mix(tip,neutralTip,recover);lean=.15*sin(strike*pi)*(1-recover)
    elif index==7:
        hand=mix(add(base,(.0,1.65,-.5)),add(base,(.8,1.65,-.5)),strike)
        tip=add(hand,(2,.12,0));lean=.18*(1-recover);step=.35*sin(t*pi*10)*(1-recover)
        hand=mix(hand,neutral,recover);tip=mix(tip,neutralTip,recover)
    figure(base,hand,tip,lean,yaw,step)
    # Training target, intentionally no damage numbers: this is a motion study.
    if index not in (0,5,9): figure((base[0]+3 if index in (1,2,8) else 3.5,0,.1),ash=True)
    if index==0 and .10<t<.56:
        r=4.4*(1-smooth((t-.1)/.46))+.25
        for i in range(100):
            a=rng.random()*2*pi;flame(add(base,(cos(a)*r, .2+rng.random()*2.5,sin(a)*r)),size=.85)
    if index==0 and .54<t<.80:
        for _ in range(28): flame(mix(hand,tip,rng.random()),size=.32)
    if index in (3,4,5,6,7,8,9,10) and .12<t<.76:
        for _ in range(24):flame(mix(hand,tip,rng.random()),size=.5,white=index==8)
    if index==3 and .29<t<.68:
        front=-3.5+smooth((t-.29)/.39)*8
        for _ in range(150):flame((front+rng.uniform(-.25,.25),rng.random()*2.2,rng.uniform(-.75,.75)),size=.9)
    if index==4 and .29<t<.68:
        a=-1.2+2.4*smooth((t-.29)/.39)
        for _ in range(150):
            r=rng.uniform(1,8);flame((base[0]+cos(a)*r,.4+rng.random(),base[2]+sin(a)*r),size=.9)
    if index==5 and .3<t<.84:
        for i in range(240):
            y=rng.random()*5;r=2+y*.6;a=y*2+t*20+(i%2)*pi
            flame(add(base,(cos(a)*r,y,sin(a)*r)),size=.75)
    if index==6 and .3<t<.88:
        grow=smooth((t-.3)/.2)
        for _ in range(260):
            a=rng.random()*2*pi;y=rng.random()*4.5*grow;r=3.1*(1-.07*y)
            flame((3+cos(a)*r,y,sin(a)*r),size=.85)
    if index==7 and .3<t<.67:
        for _ in range(110):flame((rng.uniform(-4.6,bx),rng.random()*.7,-.5+rng.uniform(-.35,.35)),size=.6)
    if index==8 and .41<t<.61:
        for _ in range(140):
            p=mix(hand,tip,rng.random());flame(add(p,(rng.random()*1.4,-rng.random()*.35,rng.uniform(-.15,.15))),size=.55,white=True)
    if index==9 and .32<t<.93:
        grow=smooth((t-.32)/.22)
        for i in range(6):
            x=-1+i*.8;sbase=(x, -3*(1-grow),2.0+(i%2)*1.0)
            figure(sbase,ash=True)
            for _ in range(14):flame((x,.1,2+(i%2)),size=.55)
    if index==10 and .40<t<.72:
        reach=smooth((t-.40)/.18)*12
        for _ in range(220):flame((base[0]+rng.random()*reach,rng.random()*2.3,-.5+rng.uniform(-.30,.30)),size=.8)
    for face in sorted(faces,key=lambda f:f[0],reverse=True):
        pts=face[1] if len(face)>3 else [project(p) for p in face[1]]
        draw.polygon(pts,fill=face[2])
    glow=Image.new('RGB',(W,H));gd=ImageDraw.Draw(glow)
    for p,r,white,jitter in particles:
        x,y=project(p);gd.ellipse((x-r*2,y-r*2,x+r*2,y+r*2),fill=(160,70,14))
    from PIL import ImageChops
    im=ImageChops.add(im,glow.filter(ImageFilter.GaussianBlur(4)));draw=ImageDraw.Draw(im)
    for p,r,white,jitter in sorted(particles,key=lambda v:depth(v[0]),reverse=True):
        x,y=project(p);c=(255,242,179) if white or jitter>.75 else (255,166,36) if jitter>.35 else (233,77,16)
        draw.rectangle((x-r*.55,y-r,x+r*.55,y+r*.5),fill=c)
        if r>2:draw.rectangle((x-r*.28,y-r*.6,x+r*.28,y+r*.4),fill=(255,231,128))
    slug,label,form,duration=CLIPS[index]
    draw.rectangle((0,0,W,68),fill=(15,20,28));draw.text((22,12),label,font=TITLE,fill=(248,236,216))
    draw.text((22,42),form+'  /  estudo de movimento · fora do jogo',font=SMALL,fill=(167,176,190))
    if index==0:
        phase='Recolher chamas' if t<.54 else 'Lâmina para FORA · pose breve' if t<.78 else 'Retorno à postura normal'
    else:phase='Preparação' if t<.27 else 'Execução e efeito' if t<.64 else 'Recuperação' if t<.84 else 'Postura normal'
    draw.rectangle((0,H-51,W,H),fill=(15,20,28));draw.text((22,H-43),phase,font=FONT,fill=(249,189,91))
    draw.line((22,H-12,W-22,H-12),fill=(44,52,65),width=3);draw.line((22,H-12,22+(W-44)*t,H-12),fill=(251,161,60),width=3)
    draw.text((W-90,H-40),f'{duration*t:.1f} s',font=SMALL,fill=(177,188,202))
    return im

if __name__=='__main__':
    samples=[]
    for i,(slug,label,form,duration) in enumerate(CLIPS):
        count=round(duration*15);frames=[frame(i,k/(count-1)) for k in range(count)]
        frames[0].save(OUT/(slug+'.gif'),save_all=True,append_images=frames[1:],duration=round(1000/15),loop=0,optimize=False)
        frame(i,.50 if i!=0 else .65).save(OUT/(slug+'.png'))
        sheet=Image.new('RGB',(W*3,H))
        for j,t in enumerate((.18,.50,.89)):sheet.paste(frame(i,t),(j*W,0))
        sheet.save(OUT/(slug+'-quadros.png'))
        samples.append(frame(i,.50 if i!=0 else .65).resize((480,300)))
        print(slug,flush=True)
    board=Image.new('RGB',(480*3,300*4),(15,20,28))
    for i,im in enumerate(samples):board.paste(im,((i%3)*480,(i//3)*300))
    d=ImageDraw.Draw(board);d.text((990,960),'RYŪJIN · PREVIS 57',font=TITLE,fill=(248,236,216));d.text((990,1000),'11 estudos · não é gameplay',font=SMALL,fill=(177,188,202))
    board.save(OUT/'00-painel-movimentos.png')
