"""Revisao 67: tecido fechado, gesto de pega, IK do braco e rosto sereno.
Autoria de documentacao; nao e codigo do mod. Reutiliza o rasterizador do estudo 65.
python gerar_previa.py --stills | python gerar_previa.py
"""
from pathlib import Path
import types,sys,os,subprocess,json
from math import sin,cos,pi,tan,acos
import numpy as np
from PIL import Image,ImageDraw,ImageFont

OUT=Path(__file__).resolve().parent
source=OUT.parent/'65-benihime-entrada/gerar_previa.py'
base=types.ModuleType('previs65');base.__file__=str(source)
exec(compile(source.read_text(encoding='utf8'),str(source),'exec'),base.__dict__)
v,unit,smooth,p,mix=base.v,base.unit,base.smooth,base.p,base.mix
W,H,FPS,DURATION=1280,720,24,28
base.W=W;base.H=H
F=ImageFont.truetype('C:/Windows/Fonts/segoeui.ttf',23)
SMALL=ImageFont.truetype('C:/Windows/Fonts/segoeui.ttf',16)
BIG=ImageFont.truetype('C:/Windows/Fonts/segoeuib.ttf',32)
IVORY=(232,211,193); RED=(145,25,43); HAIR=(24,22,31)
CHAPTERS=[(0,'01 / Ajoelhar'),(2,'02 / Bankai'),(3.6,'03 / Cortina sobe fechada'),(5.4,'04 / Espera: surpresa preservada'),(6.9,'05 / Dedos e giro do punho'),(8.3,'06 / Pegar a borda central'),(8.8,'07 / Puxar o tecido'),(11.8,'08 / Rosto sereno'),(13,'09 / Corpo atravessa'),(16,'10 / Apoio e pausa'),(19,'11 / Erguer o tronco'),(22.2,'12 / Fios e nome completo'),(26,'13 / Estabilizacao')]

yy,xx=np.mgrid[0:H,0:W];rad=((xx-W*.5)/(W*.65))**2+((yy-H*.47)/(H*.75))**2
VIGNETTE=np.clip(rad,0,1.25)[...,None].astype(np.float32)

def keys(t,data):
 if t<=data[0][0]:return np.array(data[0][1],float)
 for (a,x),(b,y) in zip(data,data[1:]):
  if t<=b:return mix(x,y,p(t,a,b))
 return np.array(data[-1][1],float)

def grip(t):return p(t,7.9,8.7)*(1-p(t,11.8,12.8))

def gap(t,y):
 # Bordas sobrepostas enquanto fechadas. A pequena separacao so nasce com os dedos.
 crack=p(t,6.95,7.55)
 local=.55+.45*np.exp(-((y-4.6)/2.3)**2)
 g=-.045*(1-crack)+.36*crack+2.5*p(t,8.8,11.8)*local
 return g+(5.8-g)*p(t,11.8,16)

def edge(t):return v(-gap(t,4.6),4.6,12.1-.65*grip(t))

def hand_rotation(t,side):
 if side==1:return np.eye(3)
 roll=-pi/2*p(t,6.9,8.1)*(1-p(t,12,15.6))
 pitch=.08*sin(t*.8)*p(t,7,8)*(1-p(t,8.2,8.8))
 yaw=.10*p(t,7.2,7.8)*(1-p(t,8.2,8.7))
 rz=np.array([[cos(roll),-sin(roll),0],[sin(roll),cos(roll),0],[0,0,1.]])
 rx=np.array([[1.,0,0],[0,cos(pitch),-sin(pitch)],[0,sin(pitch),cos(pitch)]])
 ry=np.array([[cos(yaw),0,sin(yaw)],[0,1.,0],[-sin(yaw),0,cos(yaw)]])
 return rz@rx@ry

def solve_elbow(shoulder,wrist,side):
 # Dois comprimentos constantes: cotovelo nao desliza nem muda de tamanho durante a pega.
 upper,lower=3.9,4.05
 delta=wrist-shoulder;distance=float(np.linalg.norm(delta))
 if distance>=upper+lower-.005:raise AssertionError(('Alvo fora do alcance do braco',distance))
 direction=unit(delta);along=(upper*upper-lower*lower+distance*distance)/(2*distance)
 bend=v(side*.65,.20,.90);bend=unit(bend-direction*np.dot(bend,direction))
 height=max(0,upper*upper-along*along)**.5
 return shoulder+direction*along+bend*height

def joints(t,side):
 offset=8*(1-p(t,11.3,16));rise=p(t,19,22.2)
 shift=v(0,sin(t*1.1)*.018,offset)
 waist=v(0,2.25,11.1)+shift;chest=mix(v(0,4.9,9.8),v(0,7.45,10.6),rise)+shift
 neck=mix(v(0,5.75,8.6),v(0,8.62,10),rise)+shift
 head=mix(v(0,6.55,8.2),v(0,9.47,9.9),rise)+shift
 shoulder=chest+v(side*1.93,.2,0)
 wrist=mix(v(side*4.05,.53,5.25)+shift,v(side*3.4,6.6,3.2)+shift,rise)
 if side==-1 and t<16:
  approach=keys(t,[(0,(0,4.65,15.4)),(7.05,(0,4.65,15.4)),(7.9,(.14,4.65,13.05))])
  contact=edge(t)+v(.55,.05,.95)
  wrist=mix(approach,contact,p(t,7.9,8.65))
  wrist=mix(wrist,v(-4.05,.53,5.25),p(t,12,16))
 if side==1 and t<16:
  wrist=keys(t,[(0,(.2,3.4,18)),(11.3,(.2,3.4,18)),(12.5,(1,3.1,13.8)),(14.3,(2.65,1.6,9.6)),(16,(4.05,.53,5.25))])
 elbow=solve_elbow(shoulder,wrist,side)
 return waist,chest,neck,head,shoulder,elbow,wrist,rise

def construction_audit():
 worst=0;grip_error=0
 for t in np.linspace(0,28,673):
  for side in [-1,1]:
   *_,s,e,w,r=joints(t,side)
   worst=max(worst,abs(np.linalg.norm(e-s)-3.9),abs(np.linalg.norm(w-e)-4.05))
  if 8.7<=t<=11.8:
   wrist=joints(t,-1)[6];grip_error=max(grip_error,float(np.linalg.norm(wrist-(edge(t)+v(.55,.05,.95)))))
 assert worst<1e-8 and grip_error<1e-8
 assert all(gap(float(t),y)<0 for t in np.linspace(3.6,6.9,80) for y in [0,4.6,12])
 return {'arm_length_error':float(worst),'wrist_to_grip_anchor_error':grip_error,'closed_seam_overlap':.09,'hidden_until':5.4,'first_separation':6.95,'rig_samples':673}

class Scene(base.Scene):
 def __init__(self,t,view='main',clock=None,dramatic=True):
  self.t=t;self.faces=[];self.lines=[];self.hidden=False;self.subject=False;self.view=view;self.clock=t if clock is None else clock;self.dramatic=dramatic;self.hand_matrix=None;self.hand_origin=None;self.hide_giant=False
  if view=='observer':
   self.eye=v(0,1.65,-6)
   yaw=keys(self.clock,[(0,(-65,0,0)),(2,(-65,0,0)),(3.2,(0,0,0)),(5,(0,0,0)),(7,(65,0,0)),(8,(65,0,0))])[0]*pi/180
   self.target=self.eye+v(sin(yaw)*20,5.3,cos(yaw)*20);self.fov=57
  elif view=='hand':
   e=edge(t);self.eye=e+v(-2.3,.50,-3.0);self.target=e+v(.10,.05,0);self.fov=40
  elif view=='face':
   head=joints(t,-1)[3];self.eye=head+v(1.7,.10,-4.5);self.target=head+v(0,.10,-.15);self.fov=38
  elif t<2:
   self.eye=v(.35,1.7,-3.6);self.target=v(0,1.1,0);self.fov=47
  elif t<3.6:
   q=p(t,2,2.6);self.eye=mix(v(.35,1.7,-3.6),v(.05,1.32,-1.45),q);self.target=v(0,1.28,-.12);self.fov=43
  elif t<6.9:
   q=p(t,3.6,5.1);self.eye=mix(v(.05,1.32,-1.45),v(.6,4,-18),q);self.target=mix(v(0,1.28,-.12),v(0,4.5,11),q);self.fov=43
  elif t<11.8:
   e=edge(t);self.eye=e+v(-2.3,.55,-4.2);self.target=e+v(.10,.10,0);self.fov=42
  elif t<13:
   _,_,_,head,*_=joints(t,-1)
   self.eye=v(1,6.7,6.3);self.target=head+v(0,-.2,-.5);self.fov=43
  else:
   self.eye=v(.8,3.4,-16.4);self.target=mix(v(0,3.7,8),v(0,4.9,8),p(t,19,22.5));self.fov=45
  self.f=unit(self.target-self.eye);self.r=unit(np.cross(self.f,v(0,1,0)));self.u=unit(np.cross(self.r,self.f));self.focal=H/(2*tan(self.fov*pi/360))
  queen=v(0,6.5,12.5);dot=float(np.dot(self.f,unit(queen-self.eye)))
  facing=smooth((dot-.67)/.28)
  self.darkness=.40*p(t,4.3,6.2)*(1-p(t,26,28))*facing if dramatic else 0
 def poly(self,pts,color,shade=True):
  if self.hand_matrix is not None:pts=[self.hand_origin+self.hand_matrix@np.asarray(q) for q in pts]
  if self.subject:
   z=float(np.mean([q[2] for q in pts]));brightness=.105+.895*smooth((14.4-z)/2.7)
   color=tuple(int(c*brightness) for c in color)
  return super().poly(pts,color,shade)
 def darkness_space(self):
  if self.t<3.6:return
  # Camara preta atras da abertura: a figura existe nela durante toda a revelacao.
  self.poly([v(-8.3,0,21),v(8.3,0,21),v(8.3,12.4,21),v(-8.3,12.4,21)],(3,3,7),False)
  for side in [-1,1]:self.poly([v(side*8.1,0,12.2),v(side*8.1,0,21),v(side*8.1,12.4,21),v(side*8.1,12.4,12.2)],(5,5,10),False)
  self.poly([v(-8.1,12.35,12.2),v(8.1,12.35,12.2),v(8.1,12.35,21),v(-8.1,12.35,21)],(3,3,7),False)
 def curtain(self):
  if self.t<3.6:return
  grow=p(self.t,3.6,5.1);gone=p(self.t,23,26)
  if gone>=1:return
  opening=p(self.t,6.95,16)
  for side in [-1,1]:
   for j in range(22):
    for i in range(18):
     pts=[]
     for ii,jj in [(i,j),(i+1,j),(i+1,j+1),(i,j+1)]:
      h=jj/22;u=ii/18;y=h*12*grow;g=gap(self.t,y)
      x=side*(g+(8-g)*u)+side*gone*8
      # Ondulacao nasce na borda tocada e perde amplitude perto da fixacao externa.
      touch=grip(self.t)
      wave=.12*sin(u*8*pi+h*2)*sin(h*pi)+.32*touch*(1-u)*sin(u*8*pi+h*2-self.t*.5)*sin(h*pi)
      pinch=-.65*touch*np.exp(-((y-4.6)/.9)**2)*(1-u)**3
      pts.append(v(x,y,12.1+wave+pinch))
     col=int(20+7*sin(i*1.1+j*.16));self.poly(pts,(col,col-1,col+4))
   # Bainha central e costura continuam no pano; a mao permanece na area vazia.
   hem=[v(side*gap(self.t,y)+side*gone*8,y,12.1-.65*touch*np.exp(-((y-4.6)/.9)**2)) for y in np.linspace(.1,12*grow,42)]
   self.path(hem,.032,(53,41,49))
 def cloth_ring(self,center,rx,rz,phase,n=32):
  return [center+v(rx*cos(a)*(1+.025*sin(a*10+phase)),0,rz*sin(a)+.06*sin(a*8+phase)) for a in np.linspace(0,2*pi,n+1)]
 def hand(self,wrist,side,rise):
  t=self.t;R=hand_rotation(t,side);g=grip(t) if side==-1 else 0
  self.hand_matrix=R;self.hand_origin=wrist
  palm=v(0,-.20,-.42);self.ellipsoid(palm,(.48,.17,.56),IVORY,18,9)
  node=edge(t)
  def local(world):return R.T@(world-wrist)
  for i in range(4):
   x=(i-1.5)*.20;root=palm+v(x,-.01,-.33);length=[.88,.99,.93,.76][i]
   anticipation=.20*p(t,6.8+i*.035,7.7+i*.035)*(1-p(t,7.9,8.5)) if side==-1 else 0
   flex=rise*.5+anticipation
   a=root+v(0,-length*.40*sin(flex),-length*.40*cos(flex))
   b=a+v(0,-length*.34*sin(flex*1.7),-length*.34*cos(flex*1.7))
   tip=b+v(0,-length*.25*sin(flex*2.2),-length*.25*cos(flex*2.2))
   # O dedo abraça a MESMA borda que deforma a cortina; nao e uma mao aberta deslizando.
   finger_y=4.65-x
   targets=[node+v(.335,finger_y-node[1],.18),node+v(.22,finger_y-node[1],-.14),node+v(-.10,finger_y-node[1],-.28),node+v(.02,finger_y-node[1],.02)]
   weight=g*p(t,7.9+i*.04,8.6+i*.04)
   root,a,b,tip=[mix(q,local(target),weight) for q,target in zip([root,a,b,tip],targets)]
   for q1,q2,r1,r2 in [(root,a,.082,.073),(a,b,.073,.062),(b,tip,.062,.045)]:self.tube(q1,q2,r1,r2,IVORY,12)
   for q in [root,a,b]:self.ellipsoid(q,(.077,.060,.077),IVORY,12,6)
   self.tube(tip+v(0,.045,.01),mix(b,tip,.6)+v(0,.05,0),.041,.044,(241,222,209),8)
   self.path([v(x*.4,-.015,-.12),palm+v(x,.14,-.10),root+v(0,.05,.02)],.009,(186,151,137))
   self.path([a+v(-.038,.05,0),a+v(.038,.05,0)],.006,(173,137,126))
   if rise>.07:
    start=wrist+R@tip;end=v(x*2+side*.30,1.28,0)
    self.hand_matrix=None;self.thread([start,mix(start,end,.5)+v(.05*sin(t+i),0,0),end],(186,64,83),1);self.hand_matrix=R
  root=palm+v(side*.39,-.02,.13);a=root+v(side*.23,-.06,-.28);b=a+v(side*.10,-.08-rise*.30,-.24)
  if g>0:
   a=mix(a,local(node+v(.42,.30,.30)),g);b=mix(b,local(node+v(.12,.27,.13)),g)
  self.tube(root,a,.115,.09,IVORY,12);self.tube(a,b,.09,.06,IVORY,12);self.ellipsoid(a,(.10,.075,.10),(227,202,186),10,5)
  for frac in [-.07,.06]:
   self.path([v(-.22,.065,frac),v(.22,.065,frac)],.012,(133,92,94))
   for x in np.linspace(-.17,.17,5):self.path([v(x,.07,frac-.05),v(x+.01,.07,frac+.05)],.009,(202,174,158))
  self.hand_matrix=None;self.hand_origin=None

 def face_and_hair(self,head,chest,waist):
  # Mandibula delicada e testa coberta; nada de sobrancelhas franzidas ou sorriso.
  rows=[];n=32
  for y,rx,rz in [(-1.08,.14,.18),(-.90,.36,.36),(-.65,.54,.46),(-.32,.68,.54),(.04,.74,.59),(.38,.73,.59),(.70,.67,.55),(.96,.51,.43),(1.14,.15,.16)]:
   rows.append([head+v(rx*cos(a),y,rz*sin(a)) for a in np.linspace(0,2*pi,n+1)])
  for j in range(len(rows)-1):
   for i in range(n):self.poly([rows[j][i],rows[j][i+1],rows[j+1][i+1],rows[j+1][i]],IVORY)
  self.tube(head+v(0,.13,-.60),head+v(0,-.23,-.69),.055,.035,(212,182,163),10)
  for side in [-1,1]:
   self.path([head+v(side*.13,-.015,-.598),head+v(side*.34,-.04,-.55),head+v(side*.52,-.015,-.455)],.014,(62,40,49))
   self.path([head+v(side*.16,.27,-.58),head+v(side*.34,.29,-.545),head+v(side*.51,.27,-.46)],.014,(78,51,57))
   for i in range(2):self.path([head+v(side*(.40+i*.05),-.03,-.52+i*.03),head+v(side*(.43+i*.05),-.06,-.52+i*.03)],.009,(66,40,48))
  self.path([head+v(-.14,-.50,-.50),head+v(0,-.51,-.53),head+v(.14,-.50,-.50)],.015,(137,78,82))
  # Massa posterior e topo: cobre o cranio, com risca e ondas laterais.
  self.ellipsoid(head+v(0,.18,.54),(1.02,1.39,.65),HAIR,24,13)
  self.ellipsoid(head+v(0,1.02,.10),(.64,.55,.58),HAIR,24,12)
  self.poly([head+v(-.51,1.10,-.43),head+v(.51,1.10,-.43),head+v(.50,.78,-.48),head+v(0,.69,-.59),head+v(-.50,.78,-.48)],HAIR)
  for side in [-1,1]:
   sweep=[head+v(side*.06,1.09,-.15),head+v(side*.40,1.0,-.46),head+v(side*.73,.68,-.42),head+v(side*.86,.13,-.29),head+v(side*.85,-.66,-.12)]
   self.path(sweep,.22,HAIR)
   self.path([q+v(0,.035,-.16) for q in sweep[:4]],.014,(70,49,61))
   for coil in range(3):
    loop=[]
    for a in np.linspace(0,2*pi,24):
     loop.append(head+v(side*(.73+coil*.15+(.35+coil*.07)*cos(a)),1.05+coil*.14+(.42+coil*.06)*sin(a),.25+coil*.10+.16*cos(a)))
    self.path(loop,.19,HAIR);self.path([q+v(0,.04,-.13) for q in loop[2:9]],.012,(63,45,57))
   for lock in range(5):
    points=[head+v(side*(.86+.13*lock),.65,.02+.17*lock),head+v(side*(1.00+.13*lock),-.35,-.06),chest+v(side*(1.15+.18*lock),-.15,-.96),waist+v(side*(1.24+.21*lock)+sin(self.t*.75-lock*.4)*.10,-.78,-1.12)]
    self.path(points,.12+.020*lock,HAIR)
    if lock in [0,2]:self.path([q+v(0,.01,-.08) for q in points],.009,(67,47,58))

 def arm(self,a,b,ra,rb):
  # Perfil continuo: nao deixa ombro e punho como cilindros de espessura igual.
  for i in range(5):
   u=i/5;w=(i+1)/5
   r1=(ra*(1-u)+rb*u)*(1+.07*sin(pi*u));r2=(ra*(1-w)+rb*w)*(1+.07*sin(pi*w))
   self.tube(mix(a,b,u),mix(a,b,w),r1,r2,IVORY,16)

 def giant(self):
  if self.t<5.4 or self.hide_giant:return
  self.subject=True;t=self.t
  waist,chest,neck,head,*_=joints(t,-1)
  # Tres tecidos independentes e pregas sobrepostas.
  for layer,(rad,front,color) in enumerate([(4.9,.0,(91,16,34)),(4.55,-.13,RED),(4.15,-.23,(181,40,57))]):
   rows=[]
   for j in range(9):
    h=j/8;r=rad*(1-h)+1.32*h;center=mix(waist+v(0,-2.14,front),waist+v(0,.12,front),h)
    rows.append(self.cloth_ring(center,r,r*.73,t*.5-layer*.45))
   for j in range(8):
    for i in range(32):
     # Apenas paineis selecionados na camada externa deixam o forro visivel.
     if layer==2 and i%8 in [0,1]:continue
     c=tuple(int(x*(.82+.18*(i%3)/2)) for x in color)
     self.poly([rows[j][i],rows[j][i+1],rows[j+1][i+1],rows[j+1][i]],c)
   if layer>0:self.path(rows[0],.026,(200,85,91))
  rings=[]
  for frac,rx,rz in [(0,1.32,.87),(.22,1.25,.87),(.50,1.52,.95),(.80,1.95,1.08),(1,1.78,.86)]:rings.append(self.cloth_ring(mix(waist,chest,frac),rx,rz,t*.4))
  for j in range(4):
   for i in range(32):self.poly([rings[j][i],rings[j][i+1],rings[j+1][i+1],rings[j+1][i]],RED if i%4 else (173,37,54))
  self.tube(chest,neck,.64,.34,IVORY,14)
  for side in [-1,1]:
   collar=[neck+v(side*.35,.06,-.30),waist+v(side*.28,.65,-1.0),waist+v(side*.58,.3,-1.05),chest+v(side*1.28,.22,-.90)]
   self.poly(collar,(208,192,171))
   outer=[neck+v(side*.54,.10,-.28),waist+v(side*.68,.15,-1.09),waist+v(side*1.03,-.2,-1.02),chest+v(side*1.60,.20,-.82)]
   self.poly(outer,(184,40,58));self.path([outer[0],outer[1],outer[2]],.035,(211,89,96))
   # Costuras ornamentais na lapela, sem adicionar armadura.
   for u in np.linspace(.18,.9,8):
    c=mix(outer[0],outer[1],u);self.path([c+v(-.06,0,-.025),c+v(.06,.07,-.03)],.015,(211,133,129))
  self.ellipsoid(waist+v(0,.12,-.95),(1.37,.22,.16),(95,16,36),14,5)
  self.path([waist+v(-.4,.16,-1.14),waist+v(.4,.16,-1.14)],.055,(185,72,85))
  self.face_and_hair(head,chest,waist)
  for side in [-1,1]:
   _,_,_,_,shoulder,elbow,wrist,rise=joints(t,side)
   self.ellipsoid(shoulder,(.49,.57,.49),IVORY,18,9);self.arm(shoulder,elbow,.43,.29);self.ellipsoid(elbow,(.29,.31,.29),IVORY,16,8);self.arm(elbow,wrist,.29,.20)
   self.hand(wrist,side,rise)
  self.subject=False
 def render(self):
  self.floor();self.darkness_space();self.curtain();self.giant();self.player()
  im=Image.new('RGB',(W,H),(13,16,23));d=ImageDraw.Draw(im)
  items=[(z,0,xy,col,0) for z,xy,col in self.faces]+[(z,1,xy,col,width) for z,xy,col,width in self.lines]
  for _,kind,xy,col,width in sorted(items,key=lambda a:-a[0]):
   if kind:d.line(xy,fill=col,width=width)
   else:d.polygon(xy,fill=col)
  # Escurecimento tem peso angular: olhar para fora reduz o efeito. Centro segue legivel.
  if self.darkness>0:
   arr=np.asarray(im,dtype=np.float32);factor=1-self.darkness*(.20+.80*VIGNETTE)
   im=Image.fromarray(np.clip(arr*factor,0,255).astype(np.uint8))
  d=ImageDraw.Draw(im);d.rectangle((0,0,W,40),fill=(8,10,15));d.rectangle((0,H-70,W,H),fill=(8,10,15))
  title='BENIHIME / REVISAO 67 / PREVIA FORA DO JOGO'
  chapter=next(label for start,label in reversed(CHAPTERS) if self.t>=start)
  if self.view=='observer':chapter='OBSERVADOR / Olhar desvia, penumbra recua'
  if self.view=='face':chapter='DETALHE / Rosto sereno e cabelo em camadas'
  if self.view=='hand':chapter='DETALHE / Giro, pega e tracao da borda central'
  d.text((24,10),title,font=SMALL,fill=(224,215,205));d.text((W-275,10),f'Penumbra dirigida: {self.darkness*100:.0f}%',font=SMALL,fill=(188,153,160))
  d.text((24,H-63),chapter,font=F,fill=(241,226,210));d.text((24,H-33),'1280 x 720 / Geometria e articulacao de estudo / Sem audio / Sem alteracao no mod',font=SMALL,fill=(169,169,181))
  d.text((W-170,H-55),f'{self.clock:04.1f} s',font=SMALL,fill=(211,196,190))
  if self.view=='main':d.rectangle((0,H-4,W*self.t/DURATION,H),fill=(175,48,68))
  words='BANKAI' if 2.75<self.t<3.6 else 'KANNONBIRAKI BENIHIME ARATAME' if 23<self.t<26 else ''
  if words and self.view=='main':
   width=d.textbbox((0,0),words,font=BIG)[2];d.rectangle((W/2-width/2-16,H-129,W/2+width/2+16,H-77),fill=(10,11,16));d.text(((W-width)/2,H-123),words,font=BIG,fill=(242,220,202))
  return im

def encode(name,duration,sampler):
 sys.path.insert(0,os.environ.get('VIDEO_TOOLS',r'C:\Users\joaop\AppData\Local\Temp\bleach-video-tools'));import imageio_ffmpeg
 ff=imageio_ffmpeg.get_ffmpeg_exe()
 proc=subprocess.Popen([ff,'-v','error','-y','-f','rawvideo','-pix_fmt','rgb24','-s',f'{W}x{H}','-r',str(FPS),'-i','-','-an','-c:v','libx264','-crf','18','-preset','fast','-pix_fmt','yuv420p','-movflags','+faststart',str(OUT/name)],stdin=subprocess.PIPE)
 for i in range(int(FPS*duration)):
  im=sampler(i/FPS).render();proc.stdin.write(im.tobytes())
  if i%(FPS*2)==0:print(f'{name}: {i/FPS:.0f}/{duration}s',flush=True)
 proc.stdin.close();assert proc.wait()==0
 return ff

def main():
 audit=construction_audit();(OUT/'verificacao-rig.json').write_text(json.dumps(audit,indent=2),encoding='utf8');print(audit,flush=True)
 times=[4.3,6.1,7.7,8.6,10.3,12.4,16.8,20.4,24]
 board=Image.new('RGB',(W*3,H*3),(10,12,19))
 for i,t in enumerate(times):
  im=Scene(t).render();im.save(OUT/f'quadro-{i+1:02d}.jpg',quality=96);board.paste(im,(i%3*W,i//3*H))
 board.resize((2400,1350),Image.Resampling.LANCZOS).save(OUT/'01-storyboard-revisado.jpg',quality=95)
 for i,t in enumerate([7.6,8.6,9.8,11.5]):Scene(t,'hand',dramatic=False).render().save(OUT/f'detalhe-mao-{i+1:02d}.jpg',quality=96)
 Scene(17,'face',dramatic=False).render().save(OUT/'02-rosto-cabelo.jpg',quality=97)
 # Prova de oclusao: imagens com/sem figura sao identicas enquanto o pano sobe e espera.
 occ=[]
 for t in [3.8,4.4,5.0,5.8,6.7]:
  a=Scene(t,dramatic=False);b=Scene(t,dramatic=False);b.hide_giant=True
  delta=int(np.max(np.abs(np.asarray(a.render(),dtype=int)-np.asarray(b.render(),dtype=int))))
  occ.append({'time':t,'maximum_pixel_difference':delta});assert delta==0,('Figura visivel antes da abertura',t,delta)
 (OUT/'verificacao-oclusao.json').write_text(json.dumps(occ,indent=2),encoding='utf8')
 (OUT/'timeline.json').write_text(json.dumps({'duration':DURATION,'fps':FPS,'chapters':CHAPTERS,'type':'procedural_3d_previsualization_not_game_capture'},ensure_ascii=False,indent=2),encoding='utf8')
 if '--stills' in sys.argv:return
 ff=encode('03-entrada-surpresa.mp4',28,lambda t:Scene(t))
 encode('04-pega-cortina-detalhe.mp4',10,lambda t:Scene(6.6+t*.55,'hand',clock=t,dramatic=False))
 encode('05-rosto-cabelo.mp4',6,lambda t:Scene(16+t*.15,'face',clock=t,dramatic=False))
 subprocess.run([ff,'-v','error','-y','-i',str(OUT/'03-entrada-surpresa.mp4'),'-vf','fps=10,scale=768:-1:flags=lanczos,split[s0][s1];[s0]palettegen=max_colors=144[p];[s1][p]paletteuse=dither=bayer','-loop','0',str(OUT/'06-entrada-surpresa.gif')],check=True)
 print('Revisao 67 concluida.',flush=True)

if __name__=='__main__':main()
