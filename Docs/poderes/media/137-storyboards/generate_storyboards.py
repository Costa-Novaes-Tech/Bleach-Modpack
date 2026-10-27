from pathlib import Path
import math,json
from PIL import Image,ImageDraw,ImageFont
R=Path(__file__).parent
# Anatomical right X, up Y, forward Z; dimensions are proposed art targets in blocks.
def v(x,y,z):return [x,y,z]
D=[
(0,'Guarda',v(.24,1.02,.20),v(.12,.18,1)),
(4,'Recolher ao ombro',v(.43,1.30,.12),v(.22,.80,.30)),
(8,'Ponta alta, fora do rosto',v(.43,1.44,.13),v(.32,.91,.20)),
(11,'Iniciar diagonal',v(.31,1.35,.38),v(.35,.50,.80)),
(14,'Passagem pelo centro',v(.03,1.16,.49),v(-.30,-.27,.92)),
(17,'Descer para esquerda',v(-.22,.98,.37),v(-.63,-.50,.59)),
(20,'Final baixo esquerdo',v(-.30,.91,.26),v(-.68,-.63,.38)),
(30,'Manter se encadear / recuperar se soltar',v(-.30,.91,.26),v(-.68,-.63,.38))]
H=[
(0,'Mesmo final do diagonal',D[-1][2],D[-1][3]),
(4,'Elevar pela esquerda',v(-.31,1.09,.30),v(-.85,-.10,.50)),
(8,'Armar plano horizontal',v(-.25,1.22,.35),v(-.92,0,.39)),
(11,'Iniciar retorno',v(-.12,1.22,.49),v(-.65,0,.76)),
(14,'Cruzar centro',v(.14,1.22,.53),v(.05,0,1)),
(17,'Atravessar direita',v(.40,1.22,.40),v(.78,0,.63)),
(20,'Recolher para estocada',v(.39,1.13,.12),v(.07,.02,1)),
(30,'Ponta alinhada a frente',v(.39,1.13,.12),v(.07,.02,1))]
T=[
(0,'Mesmo final do retorno',H[-1][2],H[-1][3]),
(4,'Cotovelos sem abrir excessivamente',v(.37,1.13,.10),v(0,0,1)),
(8,'Ponta no eixo de ataque',v(.31,1.16,.16),v(0,0,1)),
(11,'Estender antebraco',v(.23,1.17,.37),v(0,0,1)),
(14,'Contato: extensao frontal',v(.15,1.18,.57),v(0,0,1)),
(17,'Ponta mantem linha',v(.15,1.18,.57),v(0,0,1)),
(20,'Recolher a guarda',D[0][2],D[0][3]),
(30,'Guarda pronta para diagonal',D[0][2],D[0][3])]
E=[
(0,'Guarda / replay confirmado',v(.24,1.02,.20),v(.10,.10,1)),
(5,'Recolher ao proprio quadril',v(.35,.98,.08),v(-.10,-.30,.95)),
(10,'Preparacao baixa completa',v(.37,.94,.10),v(-.18,-.52,.83)),
(12,'Corte atravessa alvo',v(.13,1.16,.52),v(.05,.27,.96)),
(16,'Corte termina alto',v(.03,1.54,.42),v(-.15,.76,.63)),
(26,'Pausa: meio de 1 segundo',v(.03,1.54,.42),v(-.15,.76,.63)),
(36,'Ignicao apos 20 ticks',v(.03,1.54,.42),v(-.15,.76,.63)),
(48,'Alvo dissolvido / cinzas',v(.15,1.32,.32),v(.0,.45,.89)),
(68,'Retorno ao controle',D[0][2],D[0][3])]
acts=[]
for key,title,frames in [('S1','Senbon 1 - diagonal descendente',D),('S2','Senbon 2 - retorno horizontal',H),('S3','Senbon 3 - estocada central',T),('HG','Higashi - corte ascendente e incineracao',E)]:
 fs=[]
 for idx,(tick,label,w,di) in enumerate(frames):
  n=math.sqrt(sum(x*x for x in di));di=[x/n for x in di]
  fs.append(dict(id=f'{key}-F{idx:02}',tick=tick,label=label,wrist=w,direction=di,tip=[w[i]+di[i]*.98 for i in range(3)],pommel=[w[i]-di[i]*.18 for i in range(3)]))
 acts.append(dict(id=key,title=title,frames=fs))
data=dict(status='PROPOSTA DE AUTORIA, nao captura ou validacao do jogo',axes='X direita anatomica, Y cima, Z frente; origem nos pes; blocos',blade_length=.98,grip_back=.18,shoulder=v(.25,1.40,0),upper_arm=.40,forearm=.40,notes='Coordenadas de alvos artist icos, nao offsets diretos PlayerAnimator. Resolver IK, display e piv os no cliente.',actions=acts)
for act in acts:
 act['keyframes']=list(act['frames']);timeline=[]
 keys=act['keyframes']
 for tick in range(keys[-1]['tick']+1):
  lo=max((f for f in keys if f['tick']<=tick),key=lambda f:f['tick']);hi=min((f for f in keys if f['tick']>=tick),key=lambda f:f['tick'])
  t=0 if lo['tick']==hi['tick'] else (tick-lo['tick'])/(hi['tick']-lo['tick']);t=t*t*(3-2*t)
  w=[lo['wrist'][i]*(1-t)+hi['wrist'][i]*t for i in range(3)];di=[lo['direction'][i]*(1-t)+hi['direction'][i]*t for i in range(3)];n=math.sqrt(sum(x*x for x in di));di=[x/n for x in di]
  timeline.append(dict(id=f"{act['id']}-T{tick:03}",tick=tick,label=lo['label'],wrist=w,direction=di,tip=[w[i]+di[i]*.98 for i in range(3)],pommel=[w[i]-di[i]*.18 for i in range(3)]))
 act['timeline']=timeline
(R/'poses.json').write_text(json.dumps(data,ensure_ascii=False,indent=2),encoding='utf-8')
FONT='C:/Windows/Fonts/arial.ttf';font=ImageFont.truetype(FONT,16);small=ImageFont.truetype(FONT,13);big=ImageFont.truetype(FONT,23)
def add(a,b):return [x+y for x,y in zip(a,b)]
def sub(a,b):return [x-y for x,y in zip(a,b)]
def mul(a,k):return [x*k for x in a]
def dot(a,b):return sum(x*y for x,y in zip(a,b))
def norm(a):return math.sqrt(dot(a,a))
def elbow(w):
 s=data['shoulder'];d=sub(w,s);dist=norm(d);u=mul(d,1/dist);ref=[0,-1,0];b=sub(ref,mul(u,dot(ref,u)));b=mul(b,1/max(1e-6,norm(b)));height=math.sqrt(max(0,.40**2-(dist/2)**2));return add(add(s,mul(d,.5)),mul(b,height))
def project(p,mode,ox,oy,scale):return (ox+(-p[0] if mode=='front' else p[2])*scale,oy-p[1]*scale)
def render(frame,act):
 im=Image.new('RGB',(740,540),'#111820');d=ImageDraw.Draw(im)
 d.text((16,10),f"{frame['id']} | tick {frame['tick']} | {frame['label']}",font=font,fill='white')
 sh=data['shoulder'];el=elbow(frame['wrist']);svg=[]
 def line(a,b,mode,ox,width,color):
  pa=project(a,mode,ox,465,165);pb=project(b,mode,ox,465,165);d.line([pa,pb],fill=color,width=width);svg.append(f'<line x1="{pa[0]:.2f}" y1="{pa[1]:.2f}" x2="{pb[0]:.2f}" y2="{pb[1]:.2f}" stroke="{color}" stroke-width="{width}" stroke-linecap="round"/>')
 for mode,ox in [('front',205),('side',475)]:
  d.text((ox-65,45),'FRENTE' if mode=='front' else 'PERFIL > alvo',font=small,fill='#8c9eae')
  for y in [0,.5,1,1.5,2]:
   p1=project(v(-.8,y,-.4),mode,ox,465,165);d.line((ox-150,p1[1],ox+180,p1[1]),fill='#25313e',width=1)
  # torso and legs, free hand held clear of sword
  for a,b in [(v(-.22,1.4,0),v(.22,1.4,0)),(v(0,1.4,0),v(0,.88,0)),(v(0,.88,0),v(-.17,.48,.03)),(v(-.17,.48,.03),v(-.18,.03,.10)),(v(0,.88,0),v(.17,.48,-.03)),(v(.17,.48,-.03),v(.18,.03,-.08)),(v(-.25,1.4,0),v(-.40,1.12,-.02)),(v(-.40,1.12,-.02),v(-.33,.99,.10))]:line(a,b,mode,ox,13,'#687d91')
  c=project(v(0,1.70,0),mode,ox,465,165);d.rectangle((c[0]-22,c[1]-27,c[0]+22,c[1]+27),fill='#9dacb9');svg.append(f'<rect x="{c[0]-22}" y="{c[1]-27}" width="44" height="54" fill="#9dacb9"/>')
  line(sh,el,mode,ox,15,'#ce9d74');line(el,frame['wrist'],mode,ox,13,'#efbe8b')
  line(frame['pommel'],frame['wrist'],mode,ox,8,'#c79741');line(frame['wrist'],frame['tip'],mode,ox,5,'#eff9ff')
  wp=project(frame['wrist'],mode,ox,465,165);tp=project(frame['tip'],mode,ox,465,165)
  d.ellipse((wp[0]-5,wp[1]-5,wp[0]+5,wp[1]+5),fill='#51dcc0');d.ellipse((tp[0]-5,tp[1]-5,tp[0]+5,tp[1]+5),fill='#ff8395')
  # static non-gameplay target marker for Higashi side view
  if act['id']=='HG' and mode=='side':
   tx=ox+1.25*165
   if frame['tick']<48:
    col='#8d737c' if frame['tick']<36 else '#493d42'
    d.rectangle((tx-15,153,tx+15,187),outline=col,width=3);d.rectangle((tx-20,190,tx+20,330),outline=col,width=3)
    d.line((tx-12,330,tx-18,465),fill=col,width=5);d.line((tx+12,330,tx+18,465),fill=col,width=5)
    if frame['tick']>=36:
     for a in range(20):
      xx=tx-25+(a*17)%50;yy=170+(a*31)%270;d.line((xx,yy,xx+3,yy-10),fill='#f2ab5e',width=2)
   elif frame['tick']<68:
    for a in range(18):
     xx=tx-26+(a*17)%52;yy=160+(a*29)%300;d.ellipse((xx,yy,xx+3,yy+3),fill='#9c9691')
   d.text((tx-45,473),'alvo unico',font=small,fill='#ff8395')
 d.text((14,502),'Branco: lamina | dourado: cabo | verde: mao | rosa: ponta. Esquema de autoria.',font=small,fill='#b4c4d3')
 (R/'frames').mkdir(exist_ok=True);im.save(R/'frames'/f"{frame['id']}.png")
 text=f'<text x="16" y="28" fill="white" font-size="17">{frame["id"]} | tick {frame["tick"]}</text>'
 (R/'frames'/f"{frame['id']}.svg").write_text('<svg xmlns="http://www.w3.org/2000/svg" width="740" height="540" viewBox="0 0 740 540"><rect width="740" height="540" fill="#111820"/>'+text+''.join(svg)+'</svg>',encoding='utf-8')
 return im
for act in acts:
 ims=[render(f,act) for f in act['frames']];[render(f,act) for f in act['timeline']];sheet=Image.new('RGB',(1480,80+540*math.ceil(len(ims)/2)),'#111820');dr=ImageDraw.Draw(sheet);dr.text((16,20),act['title']+' | PROPOSTA - nao e Minecraft',font=big,fill='white')
 for i,im in enumerate(ims):sheet.paste(im,((i%2)*740,80+(i//2)*540))
 sheet.save(R/f"{act['id']}-prancha.png")
# simple interactive storyboard: exact frames, no misleading invented simulation
html='''<!doctype html><meta charset="utf-8"><title>137 - Storyboards de combate</title><style>body{margin:24px;background:#111820;color:#eef5fa;font:16px Arial}main{max-width:1200px;margin:auto}button,select,input{font:inherit;margin:8px;padding:8px}img{width:100%;max-width:900px}p{max-width:950px;line-height:1.5}code{color:#51dcc0}a{color:#92ceff}</style><main><h1>Katana primeiro: Senbon e Higashi</h1><p>Referência esquemática de autoria. Não é captura do mod nem preview de renderer. Frente e perfil descrevem o mesmo quadro. Coordenadas em poses.json são alvos artísticos em blocos; não copie como offsets PlayerAnimator. O braço usa IK simplificada para ilustrar contato; torso, dedos e expressão serão refinados no jogo.</p><select id="action"></select><button id="prev">Anterior</button><button id="next">Próximo</button><button id="play">Sequência de marcos</button><br><input id="frame" type="range" min="0" value="0"><h2 id="title"></h2><img id="pose"><p id="coords"></p><p>Senbon: uma mão e ponta alta → diagonal → retorno horizontal → estocada. Higashi: confirmação autoritativa → preparação/corte visual → 20 ticks de pausa após fim → ignição/fade → cinzas → controle. Durante tudo, só uma vítima visível.</p><a href="poses.json">Dados e coordenadas</a></main><script>const DATA=__DATA__;DATA.actions.forEach(x=>x.frames=x.timeline);let timer=null;const a=document.querySelector('#action'),f=document.querySelector('#frame');DATA.actions.forEach((x,i)=>a.add(new Option(x.title,i)));function draw(){const ac=DATA.actions[a.value],p=ac.frames[+f.value];f.max=ac.frames.length-1;document.querySelector('#pose').src='frames/'+p.id+'.png';document.querySelector('#title').textContent=p.id+' — tick '+p.tick+' — '+p.label;document.querySelector('#coords').textContent='Mão XYZ: '+p.wrist.map(x=>x.toFixed(3)).join(', ')+' | Ponta XYZ: '+p.tip.map(x=>x.toFixed(3)).join(', ')}a.onchange=()=>{f.value=0;draw()};f.oninput=draw;document.querySelector('#prev').onclick=()=>{f.value=Math.max(0,+f.value-1);draw()};document.querySelector('#next').onclick=()=>{f.value=Math.min(+f.max,+f.value+1);draw()};document.querySelector('#play').onclick=()=>{if(timer){clearInterval(timer);timer=null;return}timer=setInterval(()=>{f.value=(+f.value+1)%(+f.max+1);draw()},650)};draw()</script>'''
(R/'index.html').write_text(html.replace('__DATA__',json.dumps(data,ensure_ascii=False)),encoding='utf-8')
print('Generated',sum(len(a['frames']) for a in acts),'reference frames, front/profile PNG+SVG, 4 sheets, HTML and pose targets')
