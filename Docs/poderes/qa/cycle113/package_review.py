from pathlib import Path
import hashlib, json, shutil, subprocess, wave
import numpy as np

root=Path.cwd(); work=root/'build/review-113'; out=root/'Docs/poderes/qa/cycle113'
out.mkdir(parents=True,exist_ok=True)
source=next(Path('C:/Users/joaop/Downloads').glob('*/Gravando 2026-10-26 131228.mp4'))
names=['exact-overview-1.jpg','exact-overview-2.jpg','exact-overview-3.jpg','transformation-2.jpg','transformation-3.jpg','transformation-4.jpg','senbon_melee-frames-1.jpg','senbon_melee-frames-2.jpg','benihime_compare-frames-1.jpg','benihime_compare-frames-2.jpg','ryujin_supreme-frames-2.jpg','ryujin_supreme-frames-3.jpg','ryujin_supreme-frames-4.jpg']
for name in names: shutil.copy2(work/name,out/name)
sr=32000; t=np.arange(sr*12)/sr; rng=np.random.default_rng(113)
noise=rng.normal(0,1,len(t)); low=np.convolve(noise,np.ones(100)/100,mode='same')
grow=np.clip(t/1.5,0,1)*(1-np.clip((t-4.5)/2.5,0,1))
score=np.zeros_like(t)
for f,a in [(73.416,.09),(110,.04),(146.832,.025),(220,.012)]:
 score+=a*np.sin(2*np.pi*f*t+.12*np.sin(t*1.8))*grow
for start in [.15,1.1,4.5]:
 x=np.maximum(t-start,0); env=np.where(t>=start,np.exp(-x*4),0)
 score+=.09*np.sin(2*np.pi*(48*x+8*(1-np.exp(-x*8))))*env
fire=(noise*.022+low*.45)*grow
for start in rng.uniform(.1,6.7,65):
 x=np.maximum(t-start,0); fire+=np.where(t>=start,np.exp(-x*130),0)*noise*.045
seal=np.clip((t-4.5)/1.8,0,1)*(1-np.clip((t-6.7)/.3,0,1))
fire+=low*.8*seal
reveal=np.where(t>=7.3,np.exp(-np.maximum(t-7.3,0)*1.4),0)
score+=.065*np.sin(2*np.pi*293.665*t)*reveal
fire+=.04*noise*np.where(t>=7.3,np.exp(-np.maximum(t-7.3,0)*10),0)
duck=np.ones_like(t)
for start,end in [(1.85,4.45),(7.85,10.6)]:
 env=np.minimum(np.clip((t-start)/.15,0,1),np.clip((end-t)/.25,0,1))
 duck*=1-.86*env
fade=np.minimum(np.clip(t/.08,0,1),np.clip((12-t)/.8,0,1))
bed=(score+fire)*duck*fade
def save(path,data):
 stereo=np.column_stack([data,data]); pcm=(np.clip(stereo,-1,1)*32767).astype('<i2')
 with wave.open(str(path),'wb') as w: w.setnchannels(2);w.setsampwidth(2);w.setframerate(sr);w.writeframes(pcm.tobytes())
ff=str(root/'build/video-review-tools/ffmpeg.exe')
save(out/'ryujin-12s-original-bed.wav',bed)
mix=bed.copy(); voice_data=[]
for filename,start in [('ryujin_voice_bankai.ogg',2),('ryujin_voice_name.ogg',8)]:
 p=root/'src/main/resources/assets/bleachmod/sounds'/filename
 raw=subprocess.check_output([ff,'-v','error','-i',str(p),'-f','f32le','-ac','1','-ar',str(sr),'-'])
 voice=np.frombuffer(raw,dtype='<f4').astype(float); peak=np.max(np.abs(voice))
 if peak: voice*=.58/peak
 i=int(start*sr); count=min(len(voice),len(mix)-i);mix[i:i+count]+=voice[:count]
 voice_data.append({'file':filename,'start_s':start,'decoded_duration_s':len(voice)/sr})
save(out/'ryujin-12s-mix-preview.wav',mix)
for name in ['ryujin-12s-original-bed','ryujin-12s-mix-preview']:
 subprocess.run([ff,'-v','error','-y','-i',str(out/(name+'.wav')),'-codec:a','libmp3lame','-b:a','160k',str(out/(name+'.mp3'))],check=True)
 (out/(name+'.wav')).unlink()
manifest={'date':'2026-10-26','branch':'Feature-Poderes-bankais','base':'23b80e0','source':str(source),'source_bytes':source.stat().st_size,'source_sha256':hashlib.sha256(source.read_bytes()).hexdigest(),'duration_s':263.30,'source_fps':30,'inspection':'Overview at exact 8s steps; selected critical windows extracted at all 30fps. Not every frame of entire recording inspected.','frame_number':'zero-based, time_seconds * 30','audio':'Original procedural draft, not integrated. Technical amplitude checks only; subjective listening pending. Existing supplied voice assets reused without new imitation.','voices':voice_data,'mix_peak_linear':float(np.max(np.abs(mix))),'mix_rms_dbfs':float(20*np.log10(np.sqrt(np.mean(mix**2)))),'evidence':names}
(out/'manifest.json').write_text(json.dumps(manifest,indent=2,ensure_ascii=False),encoding='utf-8')
if Path(__file__).resolve() != (out/'package_review.py').resolve(): shutil.copy2(__file__,out/'package_review.py')
print(json.dumps({'source':str(source),'peak':manifest['mix_peak_linear'],'rms_dbfs':manifest['mix_rms_dbfs'],'voices':voice_data},ensure_ascii=False))
