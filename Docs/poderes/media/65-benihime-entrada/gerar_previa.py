"""Animatic 3D autoral de planejamento. Nao e codigo/asset do mod nem captura do Minecraft.
Python + numpy + Pillow; MP4 com imageio-ffmpeg (caminho opcional em VIDEO_TOOLS).
python gerar_previa.py --stills | python gerar_previa.py
"""
from pathlib import Path
from math import sin, cos, pi, tan
import os, sys, subprocess, json
import numpy as np
from PIL import Image, ImageDraw, ImageFont

OUT=Path(__file__).resolve().parent
W,H,FPS,DURATION=960,540,24,22
FONT=ImageFont.truetype('C:/Windows/Fonts/segoeui.ttf',19)
SMALL=ImageFont.truetype('C:/Windows/Fonts/segoeui.ttf',13)
BIG=ImageFont.truetype('C:/Windows/Fonts/segoeuib.ttf',27)
IVORY=(222,201,181); RED=(135,24,42); HAIR=(20,21,27)
CHAPTERS=[(0,'01 / Ajoelhar'),(2,'02 / Close: Bankai'),(3.6,'03 / Recuo e cortina'),(5.2,'04 / Primeira mao'),(8,'05 / Rosto entre as dobras'),(10.2,'06 / Saida do corpo'),(12.6,'07 / Maos no chao'),(15.5,'08 / Erguer o tronco'),(18.6,'09 / Fios e nome completo'),(21,'10 / Encerramento')]

def v(x,y,z):return np.array([x,y,z],float)
def unit(a):return a/max(1e-8,np.linalg.norm(a))
def smooth(a):a=max(0,min(1,a));return a*a*(3-2*a)
def p(t,a,b):return smooth((t-a)/(b-a))
def mix(a,b,t):return np.array(a)*(1-t)+np.array(b)*t
def tup(a):return tuple(float(x) for x in a)

class Scene:
 def __init__(self,t):
  self.t=t;self.faces=[];self.lines=[];self.hidden=False
  # Cenas independentes: enquadrar o gesto, nunca orbitar aleatoriamente.
  if t<2:
   self.eye=v(.35,1.7,-3.6);self.target=v(0,1.1,0);self.fov=47
  elif t<3.6:
   q=p(t,2,2.6);self.eye=mix(v(.35,1.7,-3.6),v(.05,1.32,-1.45),q);self.target=v(0,1.28,-.12);self.fov=43
  elif t<5.2:
   q=p(t,3.6,5.2);self.eye=mix(v(.05,1.32,-1.45),v(.6,4,-18),q);self.target=mix(v(0,1.28,-.12),v(0,4,9),q);self.fov=43
  elif t<8:
   self.eye=mix(v(.6,4,-18),v(1.5,3.1,-11),p(t,5.2,7.6));self.target=v(-1.5,3.5,9);self.fov=47
  elif t<10.2:
   self.eye=v(1.2,6.6,3.4);self.target=v(0,6.3,9.6);self.fov=41
  else:
   self.eye=v(.8,3.2,-19);self.target=mix(v(0,3.3,8),v(0,4.7,8),p(t,15.5,18.8));self.fov=44
  self.f=unit(self.target-self.eye);self.r=unit(np.cross(self.f,v(0,1,0)));self.u=unit(np.cross(self.r,self.f))
  self.focal=H/(2*tan(self.fov*pi/360))
 def project(self,a):
  d=a-self.eye;z=np.dot(d,self.f)
  if z<.08:return None
  return (W/2+np.dot(d,self.r)*self.focal/z,H/2-np.dot(d,self.u)*self.focal/z,z)
 def poly(self,pts,color,shade=True):
  if self.hidden:return
  pts=np.array(pts);projected=[self.project(x) for x in pts]
  if any(x is None for x in projected):return
  xy=[(x[0],x[1]) for x in projected]
  if max(x[0] for x in xy)<0 or min(x[0] for x in xy)>W or max(x[1] for x in xy)<25 or min(x[1] for x in xy)>H-30:return
  light=1
  if shade:
   n=unit(np.cross(pts[1]-pts[0],pts[2]-pts[0]));light=.43+.55*abs(np.dot(n,unit(v(-.45,.8,-.6))))
  c=tuple(max(0,min(255,int(x*light))) for x in color)
  depth=sum(x[2] for x in projected)/len(pts)
  # Uma nevoa moderada separa os planos sem esconder as maos.
  fog=min(.32,max(0,depth-20)*.006);c=tuple(int(x*(1-fog)+b*fog) for x,b in zip(c,(18,20,27)))
  self.faces.append((depth,xy,c))
 def box(self,c,size,col):
  pts=[c+v(dx*size[0]/2,dy*size[1]/2,dz*size[2]/2) for dx,dy,dz in [(-1,-1,-1),(1,-1,-1),(1,1,-1),(-1,1,-1),(-1,-1,1),(1,-1,1),(1,1,1),(-1,1,1)]]
  for ids in [(0,1,2,3),(4,7,6,5),(0,4,5,1),(3,2,6,7),(0,3,7,4),(1,5,6,2)]:self.poly([pts[i] for i in ids],col)
 def ellipsoid(self,c,r,col,n=12,m=7):
  rows=[]
  for j in range(m+1):
   lat=-pi/2+pi*j/m
   rows.append([c+v(r[0]*cos(lat)*cos(2*pi*i/n),r[1]*sin(lat),r[2]*cos(lat)*sin(2*pi*i/n)) for i in range(n)])
  for j in range(m):
   for i in range(n):
    pts=[rows[j][i],rows[j][(i+1)%n],rows[j+1][(i+1)%n],rows[j+1][i]]
    center=sum(pts)/4
    if np.dot(center-c,self.eye-center)>0:self.poly(pts,col)
 def tube(self,a,b,ra,rb,col,n=8):
  axis=unit(b-a);right=unit(np.cross(axis,v(0,0,1) if abs(axis[2])<.9 else v(0,1,0)));up=unit(np.cross(axis,right))
  row1=[a+(right*cos(2*pi*i/n)+up*sin(2*pi*i/n))*ra for i in range(n)]
  row2=[b+(right*cos(2*pi*i/n)+up*sin(2*pi*i/n))*rb for i in range(n)]
  for i in range(n):self.poly([row1[i],row1[(i+1)%n],row2[(i+1)%n],row2[i]],col)
  self.poly(row1,col);self.poly(row2,col)
 def path(self,pts,width,col):
  for a,b in zip(pts,pts[1:]):self.tube(a,b,width,width*.96,col,6)
 def thread(self,pts,col=(190,61,77),width=1):
  if self.hidden:return
  for a,b in zip(pts,pts[1:]):
   x=self.project(a);y=self.project(b)
   if x and y:self.lines.append(((x[2]+y[2])/2,[(x[0],x[1]),(y[0],y[1])],col,width))
 def floor(self):
  for x in range(-20,21,2):
   for z in range(-8,25,2):
    c=(35,38,47) if (x+z)%4 else (42,44,53)
    self.poly([v(x,0,z),v(x+1.98,0,z),v(x+1.98,0,z+1.98),v(x,0,z+1.98)],c,False)
  for x in [-10,10]:
   for z in [7,15,23]:self.box(v(x,4,z),(1.2,8,1.2),(47,47,56))
 def player(self):
  k=p(self.t,0,1.6);dy=-.45*k
  # Joelho real e espada baixa: os segmentos mudam, nao apenas o corpo inteiro.
  for sign in [-1,1]:
   hip=v(sign*.16,.93+dy,.03);knee=mix(v(sign*.16,.48,-.02),v(sign*.22,.19,-.38),k);ankle=mix(v(sign*.16,.1,.03),v(sign*.22,.09,.3),k)
   self.tube(hip,knee,.14,.14,(30,31,37));self.tube(knee,ankle,.14,.12,(25,26,31));self.box(ankle+v(0,-.02,-.08),(.29,.14,.36),(65,62,60))
  self.box(v(0,1.15+dy,-.03),(.6,.7,.35),(30,32,39));self.box(v(0,1.05+dy,-.215),(.08,.48,.02),(191,188,177));self.box(v(0,.88+dy,-.02),(.64,.1,.4),(202,196,177))
  head=v(0,1.73+dy,-.12);self.box(head,(.49,.49,.45),(205,173,148));self.box(head+v(0,.24,.02),(.52,.15,.49),(195,176,119))
  # Franja em placas e olhos legiveis no close.
  for i in range(5):self.box(head+v(-.21+i*.105,.15-.018*(i%2),-.24),(.12,.2+.04*(i%3),.05),(192-i*5,169-i*3,108))
  for sign in [-1,1]:self.box(head+v(sign*.105,.015,-.234),(.09,.025,.008),(40,51,58))
  self.box(head+v(0,-.11,-.233),(.085,.012,.01),(91,58,54))
  for sign in [-1,1]:
   shoulder=v(sign*.39,1.34+dy,0);elbow=v(sign*.53,.95+dy,-.11);hand=v(sign*.58,.62+dy,-.3)
   self.tube(shoulder,elbow,.16,.14,(30,31,38));self.tube(elbow,hand,.13,.08,(30,31,38));self.box(hand,(.16,.16,.15),(205,173,148))
   if sign==1:
    # Fio da espada voltado para fora, nunca atravessa o torso.
    tip=hand+v(1.4,-hand[1]+.08,-.2);self.tube(hand,tip,.025,.008,(168,178,184),5);self.tube(hand,hand+v(-.28,.03,.03),.055,.055,(186,149,73),7)
 def curtain(self):
  if self.t<3.6:return
  grow=p(self.t,3.6,4.8);opening=p(self.t,5.5,11.6);gone=p(self.t,16,19)
  if gone>=1:return
  for side in [-1,1]:
   for j in range(16):
    for i in range(16):
     pts=[]
     for ii,jj in [(i,j),(i+1,j),(i+1,j+1),(i,j+1)]:
      h=jj/16;u=ii/16;gap=opening*(3.2+2.4*sin(h*pi))
      x=side*(gap+(8-gap)*u)+side*gone*8;y=h*12*grow
      wave=(.15+.38*opening)*sin(u*7*pi+self.t*(.3+.4*opening)+h*2)*sin(h*pi)
      z=12.1+wave
      pts.append(v(x,y,z))
     c=int(17+5*sin(i*1.3+j*.12));self.poly(pts,(c,c,c+5))
 def giant(self):
  if self.t<5.2:return
  emerge=p(self.t,8.4,12.3);rise=p(self.t,15.5,18.5);breath=sin(self.t*1.15)*.045
  # Corpo atravessa o plano da cortina; a mao entra primeiro, sem escalar a figura.
  offset=(1-emerge)*4.1
  def point(a):return np.array(a)+v(0,breath,offset)
  waist=point((0,2.25,11.1));chest=point(mix(v(0,4.9,9.8),v(0,7.45,10.6),rise));neck=point(mix(v(0,5.75,8.6),v(0,8.62,10),rise));head=point(mix(v(0,6.55,8.2),v(0,9.47,9.9),rise))
  # Saia em paineis com dobras, tecido e atraso independente.
  self.hidden=self.t<8.1
  rows=[]
  for j in range(7):
   h=j/6;cy=.12+h*2.35;rad=4.7*(1-h)+1.4*h
   rows.append([point((cos(a)*rad,cy,11.2+sin(a)*rad*.74+.14*sin(a*5+self.t*.85)*(1-h))) for a in np.linspace(0,2*pi,25)])
  for j in range(6):
   for i in range(24):
    shade=.75+.25*(i%3)/2;self.poly([rows[j][i],rows[j][i+1],rows[j+1][i+1],rows[j+1][i]],tuple(int(c*shade) for c in RED))
  # Torso em aneis inclinados: volumes anatomicos sob vestes, nao um cubo.
  rings=[]
  for frac,rx,rz in [(0,1.4,.85),(.45,1.65,1),(.8,2,1.1),(1,1.8,.85)]:
   center=mix(waist,chest,frac);rings.append([center+v(rx*cos(a),0,rz*sin(a)) for a in np.linspace(0,2*pi,21)])
  for j in range(3):
   for i in range(20):self.poly([rings[j][i],rings[j][i+1],rings[j+1][i+1],rings[j+1][i]],RED if i%4 else (176,42,57))
  self.tube(chest,neck,.7,.36,IVORY,12)
  for side in [-1,1]:
   lapel=[chest+v(side*1.25,.15,-.88),waist+v(side*.45,.3,-.9),waist+v(side*.8,-.1,-.87),chest+v(side*1.5,.2,-.8)]
   self.poly(lapel,(183,50,63))
  # Cabelo posterior e face com mandibula/nariz em volume.
  self.hidden=self.t<8
  self.ellipsoid(head+v(0,.18,.28),(1.13,1.52,.9),HAIR,14,8)
  self.ellipsoid(head,(.84,1.19,.72),IVORY,14,9)
  
  self.tube(head+v(0,.05,-.70),head+v(0,-.32,-.84),.095,.06,(202,176,157),7)
  for side in [-1,1]:
   self.path([head+v(side*.13,.03,-.72),head+v(side*.38,-.01,-.69),head+v(side*.58,.08,-.59)],.022,(61,42,47))
   self.path([head+v(side*.13,.36,-.69),head+v(side*.43,.43,-.62)],.026,(54,40,43))
  self.path([head+v(-.2,-.54,-.65),head+v(0,-.58,-.70),head+v(.2,-.54,-.65)],.023,(125,63,69))
  # Mechas longas e lacos laterais. Atraso pequeno para evitar cabelo rigido.
  for side in [-1,1]:
   self.hidden=self.t<8
   loop=[]
   for a in np.linspace(0,2*pi,15):loop.append(head+v(side*(1.02+.52*cos(a)),1.0+.65*sin(a),.2+.19*cos(a)))
   self.path(loop,.24,HAIR)
   for lock in range(3):
    pts=[head+v(side*(.76+.19*lock),.85,-.28+.17*lock),head+v(side*(1.1+.12*lock),-.2,-.16),chest+v(side*(1.25+.2*lock),-.25,-.88),waist+v(side*(1.35+.25*lock)+sin(self.t*.9-lock)*.12,-.6,-1.1)]
    self.path(pts,.13+.035*lock,HAIR)
   # Ombro, cotovelo, punho e dedos independentes.
   self.hidden=self.t<10.2 and side==1
   shoulder=chest+v(side*1.93,.2,0)
   elbow=point(mix(v(side*3.2,2.7,7.25),v(side*3.9,6.55,7.4),rise))
   wrist=point(mix(v(side*4.05,.55,5.25),v(side*3.4,6.6,3.2),rise))
   if self.t<12.3 and side==-1:
    reach=p(self.t,5.2,7.8);reach_wrist=mix(v(-2.4,4.3,12.9),v(-3.6,3.5,9.2),reach);wrist=mix(reach_wrist,wrist,p(self.t,8.4,12.3));elbow=mix(shoulder,wrist,.55)+v(-.5,.7,0)
   self.ellipsoid(shoulder,(.55,.65,.55),IVORY,10,5);self.tube(shoulder,elbow,.48,.34,IVORY,10);self.ellipsoid(elbow,(.34,.37,.34),IVORY,10,5);self.tube(elbow,wrist,.34,.22,IVORY,10)
   palm=wrist+mix(v(0,-.23,-.43),v(0,-.24,-.3),rise)
   self.ellipsoid(palm,(.5,.19,.57),IVORY,10,5)
   for finger in range(5):
    fx=(finger-2)*.19;length=[.57,.88,1.02,.91,.72][finger]
    root=palm+v(fx,-.03,-.31);curl=rise*(.5+.06*sin(self.t*1.5+finger))
    mid=root+v(fx*.15,-.07-curl*.5,-length*.55);tip=mid+v(.02*sin(self.t+finger)*rise,-.02-curl*.65,-length*.42*(1-.5*rise))
    self.tube(root,mid,.09,.078,IVORY,6);self.tube(mid,tip,.078,.058,IVORY,6)
    if rise>.1:
     end=v(fx*2+side*.35,1.3,0)+v(.05*sin(self.t+finger),0,0)
     self.thread([tip,mix(tip,end,.45)+v(.05*sin(self.t+finger),0,.08),end],(int(180*rise),int(60*rise),int(77*rise)),1)
   for frac in [.48,.77]:
    c=mix(elbow,wrist,frac);self.path([c+v(-.2,0,-.25),c+v(.2,0,-.25)],.012,(99,68,68))
 def render(self):
  self.floor();self.curtain();self.giant();self.hidden=False;self.player()
  im=Image.new('RGB',(W,H),(15,18,25));d=ImageDraw.Draw(im)
  items=[(z,0,xy,col,0) for z,xy,col in self.faces]+[(z,1,xy,col,width) for z,xy,col,width in self.lines]
  for _,kind,xy,col,width in sorted(items,key=lambda a:-a[0]):
   if kind:d.line(xy,fill=col,width=width)
   else:d.polygon(xy,fill=col)
  d.rectangle((0,0,W,34),fill=(9,11,16));d.rectangle((0,H-62,W,H),fill=(9,11,16))
  chapter=next(label for start,label in reversed(CHAPTERS) if self.t>=start)
  d.text((20,7),'BENIHIME / ESTUDO DE ENTRADA',font=SMALL,fill=(211,208,198));d.text((W-215,7),'PREVIA 3D / FORA DO JOGO',font=SMALL,fill=(173,168,162))
  d.text((20,H-53),chapter,font=FONT,fill=(233,228,217));d.text((20,H-28),'Corpo, tecido e camera em movimento. Geometria de estudo; acabamento final no storyboard.',font=SMALL,fill=(163,166,179))
  d.text((W-110,H-48),f'{self.t:04.1f} / 22 s',font=SMALL,fill=(203,197,190))
  d.rectangle((0,H-4,int(W*self.t/DURATION),H),fill=(169,53,69))
  if 2.75<self.t<3.6:
   text='BANKAI';bw=d.textbbox((0,0),text,font=BIG)[2];d.text(((W-bw)/2,H-104),text,font=BIG,fill=(247,233,211))
  if 19<self.t<21.8:
   text='KANNONBIRAKI BENIHIME ARATAME';bw=d.textbbox((0,0),text,font=BIG)[2];d.rectangle((W/2-bw/2-16,H-112,W/2+bw/2+16,H-70),fill=(13,14,20));d.text(((W-bw)/2,H-107),text,font=BIG,fill=(236,213,190))
  return im

def main():
 times=[1.5,3.1,4.9,7.3,9.3,12.3,14.4,17.2,20.4]
 board=Image.new('RGB',(W*3,H*3),(14,17,23))
 for i,t in enumerate(times):
  im=Scene(t).render();im.save(OUT/f'quadro-{i+1:02d}.jpg',quality=92);board.paste(im,(i%3*W,i//3*H))
 board.resize((1920,1080),Image.Resampling.LANCZOS).save(OUT/'02-storyboard-tecnico.jpg',quality=93)
 (OUT/'timeline.json').write_text(json.dumps({'duration':DURATION,'fps':FPS,'chapters':CHAPTERS,'type':'procedural_3d_previsualization_not_game_capture'},ensure_ascii=False,indent=2),encoding='utf8')
 if '--stills' in sys.argv:return
 tools=os.environ.get('VIDEO_TOOLS',r'C:\Users\joaop\AppData\Local\Temp\bleach-video-tools');sys.path.insert(0,tools)
 import imageio_ffmpeg
 ff=imageio_ffmpeg.get_ffmpeg_exe()
 proc=subprocess.Popen([ff,'-v','error','-y','-f','rawvideo','-pix_fmt','rgb24','-s',f'{W}x{H}','-r',str(FPS),'-i','-','-an','-c:v','libx264','-crf','19','-preset','fast','-pix_fmt','yuv420p','-movflags','+faststart',str(OUT/'03-previa-animada.mp4')],stdin=subprocess.PIPE)
 for i in range(FPS*DURATION):
  im=Scene(i/FPS).render();proc.stdin.write(im.tobytes())
  if i%(FPS*2)==0:print(f'Render {i/FPS:.0f}/{DURATION}s',flush=True)
 proc.stdin.close();assert proc.wait()==0
 subprocess.run([ff,'-v','error','-y','-i',str(OUT/'03-previa-animada.mp4'),'-vf','fps=10,scale=640:-1:flags=lanczos,split[s0][s1];[s0]palettegen=max_colors=128[p];[s1][p]paletteuse=dither=bayer','-loop','0',str(OUT/'04-previa-animada.gif')],check=True)
 print('MP4, GIF e storyboard concluidos.',flush=True)

if __name__=='__main__':main()
