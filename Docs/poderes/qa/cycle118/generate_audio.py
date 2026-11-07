"""Original 12-second music/fire stems. Python/numpy + existing ffmpeg; no voice synthesis.
Usage from repository root: python Docs/poderes/qa/cycle118/generate_audio.py
"""
from pathlib import Path
import hashlib,json,subprocess,wave
import numpy as np

root=Path.cwd(); out=root/'src/main/resources/assets/bleachmod/sounds'
work=root/'build/cycle118-audio';work.mkdir(parents=True,exist_ok=True)
sr=32000;t=np.arange(sr*12)/sr;rng=np.random.default_rng(118)
noise=rng.normal(0,1,len(t));low=np.convolve(noise,np.ones(100)/100,mode='same')
growth=np.clip(t/1.5,0,1)*(1-np.clip((t-4.5)/2.5,0,1))
music=np.zeros_like(t)
for hz,gain in [(73.416,.18),(110,.08),(146.832,.05),(220,.024)]:
 music+=gain*np.sin(2*np.pi*hz*t+.12*np.sin(t*1.8))*growth
for start in [.15,1.1,4.5]:
 x=np.maximum(t-start,0);env=np.where(t>=start,np.exp(-x*4),0)
 music+=.18*np.sin(2*np.pi*(48*x+8*(1-np.exp(-x*8))))*env
reveal=np.where(t>=7.3,np.exp(-np.maximum(t-7.3,0)*1.4),0)
music+=.13*np.sin(2*np.pi*293.665*t)*reveal
fire=(noise*.045+low*.9)*growth
for start in rng.uniform(.1,6.7,65):
 x=np.maximum(t-start,0);fire+=np.where(t>=start,np.exp(-x*130),0)*noise*.09
seal=np.clip((t-4.5)/1.8,0,1)*(1-np.clip((t-6.7)/.3,0,1))
fire+=low*1.6*seal
fire+=.08*noise*np.where(t>=7.3,np.exp(-np.maximum(t-7.3,0)*10),0)
# Pockets leave speech room even before dynamic runtime ducking, without speeding any voice.
pocket=np.ones_like(t)
for start,end in [(1.85,3.65),(7.85,10.6)]:
 env=np.minimum(np.clip((t-start)/.15,0,1),np.clip((end-t)/.25,0,1));pocket*=1-.5*env
fade=np.minimum(np.clip(t/.08,0,1),np.clip((12-t)/.8,0,1))
music*=pocket*fade;fire*=pocket*fade
ff=root/'build/video-review-tools/ffmpeg.exe';manifest={}
for name,data in [('ryujin_entrance_score',music),('ryujin_compact_score',music),('ryujin_solar_fire',fire)]:
 pcm=(np.clip(np.column_stack([data,data]),-1,1)*32767).astype('<i2')
 wav=work/(name+'.wav')
 with wave.open(str(wav),'wb') as w:w.setnchannels(2);w.setsampwidth(2);w.setframerate(sr);w.writeframes(pcm.tobytes())
 target=out/(name+'.ogg')
 subprocess.run([str(ff),'-v','error','-y','-i',str(wav),'-c:a','libvorbis','-q:a','5',str(target)],check=True)
 manifest[name]={'seconds':12,'rate':sr,'channels':2,'peak_linear':float(np.max(np.abs(data))),'sha256':hashlib.sha256(target.read_bytes()).hexdigest()}
Path(__file__).with_name('audio-manifest.json').write_text(json.dumps(manifest,indent=2),encoding='utf-8')
print(json.dumps(manifest,indent=2))
