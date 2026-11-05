from pathlib import Path
import json,hashlib,shutil,subprocess,sys
from PIL import Image,ImageDraw
root=Path(__file__).resolve().parents[4];out=Path(__file__).resolve().parent
base=root/'run-benihime-qa/base/ryujin-bankai-qa'
run=base/'cycle111-1793017327636'
left=sorted(base.glob('cycle111-*/12_supreme_left_profile/frames.jsonl'),key=lambda p:p.stat().st_mtime)[-1].parent
folders=[run/'09_army_and_supreme',run/'10_supreme_front',run/'11_supreme_profile',left]
senbon=root/'run-benihime-qa/base/senbonzakura-combat-qa'
folders += [senbon/n for n in ['1793016921486-01_combo_front','1793016928491-02_combo_profile','1793016935486-03_combo_back','1793016942484-04_hakuteiken_front','1793016949485-05_hakuteiken_profile','1793016956486-06_hakuteiken_back']]
report=[]
for folder in folders:
 records=[json.loads(line) for line in (folder/'frames.jsonl').read_text().splitlines()]
 dest=out/folder.name;dest.mkdir(exist_ok=True);shutil.copy2(folder/'frames.jsonl',dest/'frames-original.jsonl')
 selected=[];ticks={150,160,170,174,178,180,184,188,194,198} if folder in folders[:4] else {38,52,68} if 'combo' in folder.name else {44,56,64,78,88}
 for r in records:
  if r['tick'] not in ticks:continue
  source=folder/r['file']
  if not source.exists():source=folder/'screenshots'/r['file']
  target=dest/source.name;shutil.copy2(source,target)
  selected.append({'tick':r['tick'],'file':str(target.relative_to(out)),'source':str(source),'sha256':hashlib.sha256(source.read_bytes()).hexdigest()})
 report.append({'scenario':folder.name,'source':str(folder),'sampledFrames':len(records),'selected':selected})
(out/'captures.json').write_text(json.dumps(report,indent=2)+'\n')
# Authenticated sampled video, original wall-clock intervals. No audio or generated in-betweens.
concat=[]
for folder in [run/'10_supreme_front',run/'11_supreme_profile']:
 records=[json.loads(line) for line in (folder/'frames.jsonl').read_text().splitlines()]
 records=[r for r in records if 140<=r['tick']<=198]
 for i,r in enumerate(records):
  concat.append("file '"+(folder/r['file']).as_posix()+"'")
  if i+1<len(records):concat.append('duration '+str((records[i+1]['wallMs']-r['wallMs'])/1000))
listing=root/'build/cycle111-preview.txt';listing.write_text('\n'.join(concat))
subprocess.run([sys.argv[1],'-hide_banner','-loglevel','error','-y','-f','concat','-safe','0','-i',str(listing),'-fps_mode','vfr','-c:v','libx264','-crf','23','-pix_fmt','yuv420p',str(out/'c-vertical-sampled-no-audio.mp4')],check=True)
items=[(run/'10_supreme_front',160),(run/'10_supreme_front',174),(run/'11_supreme_profile',184),(left,184),(senbon/'1793016928491-02_combo_profile',52),(senbon/'1793016949485-05_hakuteiken_profile',56)]
canvas=Image.new('RGB',(1280,3*384),(18,20,23));draw=ImageDraw.Draw(canvas)
for i,(folder,tick) in enumerate(items):
 im=Image.open(folder/'screenshots'/('frame-t%03d.png'%tick)).convert('RGB');im.thumbnail((640,360));x=i%2*640;y=i//2*384;canvas.paste(im,(x,y));draw.text((x+8,y+362),folder.name+' / tick '+str(tick),fill='white')
canvas.save(out/'contact.jpg',quality=88)
print(json.dumps({'cases':len(report),'sourceFrames':sum(x['sampledFrames'] for x in report)},indent=2))
