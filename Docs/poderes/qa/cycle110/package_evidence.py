from pathlib import Path
import json,hashlib,shutil,subprocess,sys
from PIL import Image,ImageDraw
root=Path(__file__).resolve().parents[4];out=Path(__file__).resolve().parent
runs=root/'run-benihime-qa/base/ryujin-bankai-qa'
long=runs/'cycle110-1793014947864'/'01_day_back'
first=runs/'cycle110-1793014381666'
kit=sorted(runs.glob('cycle110-*'),key=lambda p:p.stat().st_mtime)[-1]/'09_army_and_supreme'
chosen=[long]+[first/n for n in ['02_night_front','03_first_return','04_left_front','05_near_wall','06_move_after_commit','07_cancel_before_commit','08_cancel_after_commit']]+[kit]
report=[]
for folder in chosen:
 records=[json.loads(s) for s in (folder/'frames.jsonl').read_text().splitlines()]
 dest=out/folder.name;dest.mkdir(exist_ok=True)
 shutil.copy2(folder/'frames.jsonl',dest/'frames-original.jsonl')
 ticks={0,100,200,290,400,490,580,690,790,798,800,820} if folder==long else {100,120,130,140,160,178,180,188,196,200,220} if folder==kit else {100,290,350,358,360,380}
 selected=[]
 for r in records:
  if r['tick'] not in ticks:continue
  source=folder/r['file'];target=dest/source.name;shutil.copy2(source,target)
  selected.append({'tick':r['tick'],'file':str(target.relative_to(out)),'source':str(source),'sha256':hashlib.sha256(source.read_bytes()).hexdigest()})
 report.append({'scenario':folder.name,'source':str(folder),'sampledFrames':len(records),'selected':selected})
(out/'captures.json').write_text(json.dumps(report,indent=2)+'\n')
# Sampled framebuffer montage; original wall-clock durations, not an interpolated render.
rows=[json.loads(s) for s in (long/'frames.jsonl').read_text().splitlines() if json.loads(s)['tick']<=800]
concat=[]
for i,r in enumerate(rows):
 path=(long/r['file']).as_posix()
 concat.append("file '"+path+"'")
 if i+1<len(rows):concat.append('duration '+str((rows[i+1]['wallMs']-r['wallMs'])/1000))
concat.append("file '"+(long/rows[-1]['file']).as_posix()+"'")
listing=root/'build/cycle110-preview.txt';listing.write_text('\n'.join(concat))
subprocess.run([sys.argv[1],'-hide_banner','-loglevel','error','-y','-f','concat','-safe','0','-i',str(listing),'-fps_mode','vfr','-c:v','libx264','-crf','24','-pix_fmt','yuv420p',str(out/'preview-sampled-long-no-audio.mp4')],check=True)
items=[(long,200),(long,490),(long,690),(long,800),(kit,130),(kit,178)]
canvas=Image.new('RGB',(1280,3*386),(17,19,23));draw=ImageDraw.Draw(canvas)
for i,(folder,tick) in enumerate(items):
 im=Image.open(folder/'screenshots'/('frame-t%03d.png'%tick)).convert('RGB');im.thumbnail((640,360));x=(i%2)*640;y=(i//2)*386;canvas.paste(im,(x,y));draw.text((x+10,y+362),folder.name+' / tick '+str(tick),fill='white')
canvas.save(out/'contact.jpg',quality=88)
print(json.dumps({'sources':[str(p) for p in chosen],'frames':sum(r['sampledFrames'] for r in report)},indent=2))
