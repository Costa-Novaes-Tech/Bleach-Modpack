"""Original procedural score and UV material. No external audio or vanilla texture copy.
Run with Python/numpy/Pillow; ffmpeg path argument. Seeded and reproducible.
"""
from pathlib import Path
import numpy as np
from PIL import Image
import random, wave, subprocess, sys
root=Path(__file__).resolve().parents[4]
assets=root/'src/main/resources/assets/bleachmod'
work=root/'build/cycle110-audio';work.mkdir(parents=True,exist_ok=True)
sr=32000
for seconds,name in [(40,'ryujin_entrance_score'),(18,'ryujin_compact_score')]:
 n=sr*seconds;t=np.arange(n)/sr;story=t*40/seconds
 rng=np.random.default_rng(110)
 # D minor suspended fifths and slow dissonant upper partial, original synthesis.
 grow=np.clip((story-1)/12,0,1)*(1-np.clip((story-28)/2,0,1))
 music=np.zeros(n)
 for f,a in [(73.416,.12),(110,.065),(146.832,.045),(164.814,.018),(220,.022)]:
  music+=a*np.sin(2*np.pi*f*t+.10*np.sin(t*1.3))*grow
 noise=rng.normal(0,1,n)
 low=np.convolve(noise,np.ones(160)/160,mode='same')
 fire=(noise*.025+low*.23)*grow*(.7+.3*np.sin(t*2.1)**2)
 music+=fire
 for beat in [5,9,13,18,22,26]:
  x=t-beat*seconds/40;env=np.where(x>=0,np.exp(-np.maximum(x,0)*4),0)
  music+=.16*np.sin(2*np.pi*(48*x+14*(1-np.exp(-np.maximum(x,0)*7))))*env
 # Suction toward the blade; soft final harmonic with space under the name.
 seal=np.clip((story-20)/6,0,1)*(1-np.clip((story-28.5)/.5,0,1))
 music+=low*.6*seal
 reveal=np.where(story>=30,np.exp(-np.maximum(story-30,0)*.7),0)
 music+=.09*np.sin(2*np.pi*293.665*t)*reveal
 fade=np.clip(t/.8,0,1)*np.clip((seconds-t)/1.5,0,1)
 music*=fade
 # Intentional quiet voice pockets; runtime also ducks for clear speech.
 for start,length in [(14,2.9),(34,4.5)]:
  a=start*seconds/40;b=a+length
  pocket=np.clip((t-a+.25)/.25,0,1)*(1-np.clip((t-b)/.35,0,1))
  music*=1-.82*pocket
 music=np.tanh(music)*.85
 stereo=np.column_stack([music,music*.98+np.roll(low,180)*grow*.015])
 wav=work/(name+'.wav')
 with wave.open(str(wav),'wb') as w:
  w.setnchannels(2);w.setsampwidth(2);w.setframerate(sr);w.writeframes((np.clip(stereo,-1,1)*32767).astype('<i2').tobytes())
 subprocess.run([sys.argv[1],'-hide_banner','-loglevel','error','-y','-i',str(wav),'-c:a','libvorbis','-q:a','4',str(assets/'sounds'/ (name+'.ogg'))],check=True)
# Charcoal UV material; emissive cracks are a separate sparse mask.
rng=random.Random(110)
base=Image.new('RGBA',(64,32));glow=Image.new('RGBA',(64,32),(0,0,0,0))
for y in range(32):
 for x in range(64):
  v=rng.randrange(29,60);base.putpixel((x,y),(v+7,v+2,v,255))
for x0,y0,length in [(9,9,5),(15,7,4),(21,18,8),(25,20,5),(42,18,9),(46,17,7),(3,18,11),(7,20,8)]:
 x=x0
 for i in range(length):
  y=y0+i
  if y>=32:break
  x=max(0,min(63,x+rng.choice([-1,0,0,1])))
  base.putpixel((x,y),(142,47,14,255));glow.putpixel((x,y),(255,112+rng.randrange(50),21,255))
# Skull front UV 8..15 / 8..15: ember eyes, expressionless dark jaw.
for x,y in [(10,11),(11,11),(13,11),(14,11)]:
 base.putpixel((x,y),(255,162,36,255));glow.putpixel((x,y),(255,185,60,255))
for x,y in [(12,12),(11,14),(12,14),(13,14)]:base.putpixel((x,y),(12,10,9,255));glow.putpixel((x,y),(0,0,0,0))
folder=assets/'textures/entity';folder.mkdir(parents=True,exist_ok=True)
base.save(folder/'ash_soldier.png');glow.save(folder/'ash_soldier_embers.png')
print('Generated 40s/18s original scores and charcoal/magma UV materials.')
