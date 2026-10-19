"""Revisao 66: abertura fisica central, presenca continua, detalhe e penumbra dirigida.
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
W,H,FPS,DURATION=1280,720,24,26
base.W=W;base.H=H
F=ImageFont.truetype('C:/Windows/Fonts/segoeui.ttf',23)
SMALL=ImageFont.truetype('C:/Windows/Fonts/segoeui.ttf',16)
BIG=ImageFont.truetype('C:/Windows/Fonts/segoeuib.ttf',32)
IVORY=(232,211,193); RED=(145,25,43); HAIR=(24,22,31)
CHAPTERS=[(0,'01 / Ajoelhar'),(2,'02 / Bankai'),(3.6,'03 / Presenca na penumbra'),(5.4,'04 / Abertura central'),(7,'05 / Mao e dobra do tecido'),(10,'06 / Rosto ja presente'),(12,'07 / Saida continua'),(15,'08 / Apoio e pausa'),(18,'09 / Erguer o tronco'),(21.2,'10 / Fios e nome completo'),(24,'11 / Estabilizacao')]
yy,xx=np.mgrid[0:H,0:W];rad=((xx-W*.5)/(W*.65))**2+((yy-H*.47)/(H*.75))**2
VIGNETTE=np.clip(rad,0,1.25)[...,None].astype(np.float32)

def keys(t,data):
 if t<=data[0][0]:return np.array(data[0][1],float)
 for (a,x),(b,y) in zip(data,data[1:]):
  if t<=b:return mix(x,y,p(t,a,b))
 return np.array(data[-1][1],float)

def gap(t,y):
 """Meia abertura livre no plano z=12.1; nunca um buraco no proprio tecido."""
 return .22+1.13*p(t,5.4,6.7)+4.4*p(t,8,14.8)*(.86+.14*sin(pi*y/12))

def joints(t,side):
 offset=5.5*(1-p(t,9.7,14.8));rise=p(t,18,21.2)
 shift=v(0,sin(t*1.1)*.028,offset)
 waist=v(0,2.25,11.1)+shift;chest=mix(v(0,4.9,9.8),v(0,7.45,10.6),rise)+shift
 neck=mix(v(0,5.75,8.6),v(0,8.62,10),rise)+shift
 head=mix(v(0,6.55,8.2),v(0,9.47,9.9),rise)+shift
 shoulder=chest+v(side*1.93,.2,0)
 low_wrist=v(side*4.05,.53,5.25)+shift
 wrist=mix(low_wrist,v(side*3.4,6.6,3.2)+shift,rise)
 elbow=mix(v(side*3.2,2.7,7.25)+shift,v(side*3.9,6.55,7.4)+shift,rise)
 if side==-1 and t<15:
  wrist=keys(t,[(3.6,(0,4.5,15.2)),(6.6,(0,4.5,15.2)),(7.5,(0,4.4,12.65)),(8.5,(0,4.2,11.15)),(10,(-1.9,4.05,10.4)),(12,(-3,2.8,8.8)),(15,(-4.05,.53,5.25))])
  elbow=keys(t,[(3.6,(-.25,4.9,15.1)),(6.7,(-.18,5.0,14.1)),(8,(-.15,5.0,13.1)),(10,(-.9,4.9,12.6)),(12,(-2,4.4,10.7)),(15,(-3.2,2.7,7.25))])
 if side==1 and t<15:
  wrist=keys(t,[(3.6,(.2,3.4,16.8)),(9.7,(.25,3.4,16.8)),(11.5,(1,3.1,12.8)),(13.1,(2.65,1.6,9.6)),(15,(4.05,.53,5.25))])
  elbow=keys(t,[(3.6,(1.7,4.7,17.1)),(9.7,(1.7,4.7,17.1)),(11.5,(1.45,4.4,14.2)),(13.1,(2.6,3.6,11)),(15,(3.2,2.7,7.25))])
 return waist,chest,neck,head,shoulder,elbow,wrist,rise

def clearance_audit():
 checks=0;worst=99
 for t in np.linspace(5.4,15,385):
  for side in [-1,1]:
   _,_,_,_,s,e,w,_=joints(t,side)
   for a,b,r in [(s,e,.52),(e,w,.36)]:
    # Todos os pontos de um segmento dentro da espessura maxima das pregas.
    for u in np.linspace(0,1,51):
     q=mix(a,b,u)
     if 11.55-r<q[2]<12.65+r:
      margin=gap(t,q[1])-abs(q[0])-r-.08
      worst=min(worst,margin);checks+=1
      if margin<-.01:raise AssertionError(('Braco cruza tecido',float(t),side,q.tolist(),margin))
   # Palma e pontas dos dedos na passagem inicial usam o mesmo corredor central.
   if side==-1 and t<8:
    for dz in [-1.6,-1.0,-.5,0]:
     q=w+v(0,-.2,dz)
     if 11.3<q[2]<12.9:
      margin=gap(t,q[1])-.85;worst=min(worst,margin);checks+=1
      if margin<0:raise AssertionError(('Mao fora da abertura',float(t),margin))
 return {'samples':checks,'minimum_margin_blocks':round(float(worst),4),'curtain_center_z':12.1}

class Scene(base.Scene):
 def __init__(self,t,view='main',clock=None,dramatic=True):
  self.t=t;self.faces=[];self.lines=[];self.hidden=False;self.subject=False;self.view=view;self.clock=t if clock is None else clock;self.dramatic=dramatic
  if view=='observer':
   self.eye=v(0,1.65,-6)
   yaw=keys(self.clock,[(0,(-65,0,0)),(2,(-65,0,0)),(3.2,(0,0,0)),(5,(0,0,0)),(7,(65,0,0)),(8,(65,0,0))])[0]*pi/180
   self.target=self.eye+v(sin(yaw)*20,5.3,cos(yaw)*20);self.fov=57
  elif view=='hand':
   self.eye=v(2.0,4.35,6.1);self.target=v(-.35,4.1,12.1);self.fov=46
  elif t<2:
   self.eye=v(.35,1.7,-3.6);self.target=v(0,1.1,0);self.fov=47
  elif t<3.6:
   q=p(t,2,2.6);self.eye=mix(v(.35,1.7,-3.6),v(.05,1.32,-1.45),q);self.target=v(0,1.28,-.12);self.fov=43
  elif t<5.4:
   q=p(t,3.6,5.1);self.eye=mix(v(.05,1.32,-1.45),v(.6,4,-18),q);self.target=mix(v(0,1.28,-.12),v(0,4.5,11),q);self.fov=43
  elif t<7:
   self.eye=v(.6,4,-18);self.target=v(0,4.3,11.7);self.fov=43
  elif t<10:
   self.eye=v(2.2,4.5,6.2);self.target=v(-.35,4.15,12.1);self.fov=46
  elif t<12:
   _,_,_,head,*_=joints(t,-1)
   self.eye=v(1,6.7,6.3);self.target=head+v(0,-.2,-.5);self.fov=43
  else:
   self.eye=v(.8,3.4,-16.4);self.target=mix(v(0,3.7,8),v(0,4.9,8),p(t,18,21.5));self.fov=45
  self.f=unit(self.target-self.eye);self.r=unit(np.cross(self.f,v(0,1,0)));self.u=unit(np.cross(self.r,self.f));self.focal=H/(2*tan(self.fov*pi/360))
  queen=v(0,6.5,12.5);dot=float(np.dot(self.f,unit(queen-self.eye)))
  facing=smooth((dot-.67)/.28)
  self.darkness=.40*p(t,4.3,6.2)*(1-p(t,24,26))*facing if dramatic else 0
 def poly(self,pts,color,shade=True):
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
  grow=p(self.t,3.6,4.6);gone=p(self.t,21.5,24)
  if gone>=1:return
  opening=p(self.t,5.4,14.8)
  for side in [-1,1]:
   for j in range(22):
    for i in range(18):
     pts=[]
     for ii,jj in [(i,j),(i+1,j),(i+1,j+1),(i,j+1)]:
      h=jj/22;u=ii/18;y=h*12*grow;g=gap(self.t,y)
      x=side*(g+(8-g)*u)+side*gone*8
      # Ondulacao nasce na borda tocada e perde amplitude perto da fixacao externa.
      touch=p(self.t,6.1,7.8)*(1-p(self.t,14.4,17))
      wave=.12*sin(u*8*pi+h*2)*sin(h*pi)+.32*touch*(1-u)*sin(u*8*pi+h*2-self.t*.5)*sin(h*pi)
      pts.append(v(x,y,12.1+wave))
     col=int(20+7*sin(i*1.1+j*.16));self.poly(pts,(col,col-1,col+4))
   # Bainha central e costura continuam no pano; a mao permanece na area vazia.
   hem=[v(side*gap(self.t,y)+side*gone*8,y,11.98) for y in np.linspace(.1,12*grow,26)]
   self.path(hem,.032,(53,41,49))
 def cloth_ring(self,center,rx,rz,phase,n=32):
  return [center+v(rx*cos(a)*(1+.025*sin(a*10+phase)),0,rz*sin(a)+.06*sin(a*8+phase)) for a in np.linspace(0,2*pi,n+1)]
 def hand(self,wrist,side,rise):
  t=self.t;palm=wrist+v(0,-.20,-.42)
  self.ellipsoid(palm,(.52,.18,.60),IVORY,14,7)
  # Dorso, tendao, unhas, costuras e tres segmentos de dedo.
  for i in range(4):
   x=(i-1.5)*.205;root=palm+v(x,-.015,-.35);length=[.91,1.03,.96,.78][i]
   curl=rise*(.44+.045*sin(t*1.35+i))+.10*p(t,6.6,9.2)*(1-p(t,10,14))
   a=root+v(x*.12,-.035-curl*.30,-length*.40)
   b=a+v(x*.04,-.015-curl*.40,-length*.34)
   tip=b+v(.025*sin(t*.9+i)*rise,-.01-curl*.40,-length*.25*(1-.3*rise))
   for q1,q2,r1,r2 in [(root,a,.105,.09),(a,b,.09,.077),(b,tip,.077,.057)]:self.tube(q1,q2,r1,r2,IVORY,8)
   for q in [root,a,b]:self.ellipsoid(q,(.105,.075,.10),(214,185,170),8,4)
   self.tube(tip+v(0,.05,.02),mix(b,tip,.55)+v(0,.055,0),.045,.052,(242,225,213),6)
   self.path([wrist+v(x*.4,.035,-.12),palm+v(x,.16,-.13),root+v(0,.06,.02)],.012,(161,123,113))
   self.path([a+v(-.055,.066,0),a+v(.055,.066,0)],.011,(99,66,70))
   if rise>.07:
    end=v(x*2+side*.30,1.28,0)
    self.thread([tip,mix(tip,end,.5)+v(.06*sin(t+i),0,0),end],(186,64,83),1)
  # Polegar opositor: origina na lateral da palma, nao cinco dedos paralelos.
  root=palm+v(side*.40,-.02,.13);a=root+v(side*.26,-.06,-.28);b=a+v(side*.10,-.08-rise*.30,-.24)
  self.tube(root,a,.13,.10,IVORY,8);self.tube(a,b,.10,.07,IVORY,8);self.ellipsoid(a,(.11,.08,.12),(214,185,170),8,4)
  for frac in [-.07,.06]:
   a=wrist+v(-.23,.07,frac);b=wrist+v(.23,.07,frac)
   self.path([a,b],.018,(115,76,82))
   for x in np.linspace(-.18,.18,5):self.path([wrist+v(x,.075,frac-.055),wrist+v(x+.015,.075,frac+.055)],.014,(189,163,149))
 def giant(self):
  if self.t<3.6:return
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
  # Cabelo fica realmente atras da face: evita as intersecoes quadriculadas do estudo 65.
  self.ellipsoid(head+v(0,.19,.62),(1.06,1.45,.67),HAIR,18,10)
  self.ellipsoid(head,(.82,1.14,.66),IVORY,22,13)
  self.tube(head+v(0,.12,-.64),head+v(0,-.29,-.77),.075,.045,(203,171,154),8)
  for side in [-1,1]:
   self.path([head+v(side*.12,.03,-.654),head+v(side*.34,-.015,-.63),head+v(side*.54,.06,-.53)],.021,(54,34,43))
   self.path([head+v(side*.13,.35,-.62),head+v(side*.39,.40,-.56)],.021,(56,36,44))
   for i in range(3):self.path([head+v(side*(.37+i*.06),-.005,-.62+i*.025),head+v(side*(.41+i*.06),-.06,-.64+i*.025)],.012,(57,36,45))
   loop=[head+v(side*(1.0+.47*cos(a)),1.0+.61*sin(a),.25+.13*cos(a)) for a in np.linspace(0,2*pi,21)]
   self.path(loop,.235,HAIR)
   self.path([q+v(0,.04,-.16) for q in loop[2:9]],.016,(75,58,68))
   for lock in range(4):
    pts=[head+v(side*(.84+.16*lock),.82,-.12+.20*lock),head+v(side*(1.10+.14*lock),-.2,-.10),chest+v(side*(1.31+.18*lock),-.25,-.91),waist+v(side*(1.38+.23*lock)+sin(t*.8-lock*.5)*.12,-.74,-1.12)]
    self.path(pts,.12+.028*lock,HAIR)
    if lock==1:self.path([q+v(0,.01,-.09) for q in pts],.012,(67,46,59))
  self.path([head+v(-.18,-.50,-.60),head+v(0,-.54,-.65),head+v(.18,-.50,-.60)],.024,(140,70,78))
  for side in [-1,1]:
   _,_,_,_,s,e,w,rise=joints(t,side)
   self.ellipsoid(s,(.54,.61,.53),IVORY,14,7);self.tube(s,e,.47,.31,IVORY,12);self.ellipsoid(e,(.33,.36,.32),IVORY,12,6);self.tube(e,w,.32,.22,IVORY,12)
   self.path([mix(e,w,.1)+v(0,.03,-.29),mix(e,w,.8)+v(0,.02,-.23)],.012,(166,127,118))
   self.hand(w,side,rise)
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
  title='BENIHIME / REVISAO 66 / PREVIA FORA DO JOGO'
  chapter=next(label for start,label in reversed(CHAPTERS) if self.t>=start)
  if self.view=='observer':chapter='OBSERVADOR / Olhar desvia, penumbra recua'
  if self.view=='hand':chapter='DETALHE / Mao atravessa a abertura central'
  d.text((24,10),title,font=SMALL,fill=(224,215,205));d.text((W-275,10),f'Penumbra dirigida: {self.darkness*100:.0f}%',font=SMALL,fill=(188,153,160))
  d.text((24,H-63),chapter,font=F,fill=(241,226,210));d.text((24,H-33),'1280 x 720 / Geometria e articulacao de estudo / Sem audio / Sem alteracao no mod',font=SMALL,fill=(169,169,181))
  d.text((W-170,H-55),f'{self.clock:04.1f} s',font=SMALL,fill=(211,196,190))
  if self.view=='main':d.rectangle((0,H-4,W*self.t/DURATION,H),fill=(175,48,68))
  words='BANKAI' if 2.75<self.t<3.6 else 'KANNONBIRAKI BENIHIME ARATAME' if 21.8<self.t<24.5 else ''
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
 audit=clearance_audit();(OUT/'verificacao-abertura.json').write_text(json.dumps(audit,indent=2),encoding='utf8');print(audit,flush=True)
 times=[4.9,6.5,7.6,9.0,10.8,14.5,16.5,20.4,23.2]
 board=Image.new('RGB',(W*3,H*3),(10,12,19))
 for i,t in enumerate(times):
  im=Scene(t).render();im.save(OUT/f'quadro-{i+1:02d}.jpg',quality=96);board.paste(im,(i%3*W,i//3*H))
 board.resize((2400,1350),Image.Resampling.LANCZOS).save(OUT/'01-storyboard-revisado.jpg',quality=95)
 # Planos dedicados maiores para conferir textura, articulacoes e abertura.
 for i,t in enumerate([6.8,7.7,8.7,9.6]):Scene(t,'hand',dramatic=False).render().save(OUT/f'detalhe-mao-{i+1:02d}.jpg',quality=96)
 before=Scene(4.9,dramatic=False).render();after=Scene(4.9).render();comp=Image.new('RGB',(W*2,H));comp.paste(before,(0,0));comp.paste(after,(W,0));comp.resize((1920,540)).save(OUT/'02-comparacao-penumbra.jpg',quality=94)
 (OUT/'timeline.json').write_text(json.dumps({'duration':DURATION,'fps':FPS,'chapters':CHAPTERS,'type':'procedural_3d_previsualization_not_game_capture','clearance':audit},ensure_ascii=False,indent=2),encoding='utf8')
 if '--stills' in sys.argv:return
 ff=encode('03-entrada-revisada.mp4',26,lambda t:Scene(t))
 encode('04-detalhe-mao.mp4',8,lambda t:Scene(5.4+t*.57,'hand',clock=t,dramatic=False))
 encode('05-observador-penumbra.mp4',8,lambda t:Scene(14+t*.10,'observer',clock=t))
 subprocess.run([ff,'-v','error','-y','-i',str(OUT/'03-entrada-revisada.mp4'),'-vf','fps=10,scale=768:-1:flags=lanczos,split[s0][s1];[s0]palettegen=max_colors=144[p];[s1][p]paletteuse=dither=bayer','-loop','0',str(OUT/'06-entrada-revisada.gif')],check=True)
 print('Revisao 66 concluida.',flush=True)

if __name__=='__main__':main()
