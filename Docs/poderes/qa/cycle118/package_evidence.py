"""Package selected real-client screenshots. Preserve original captures and saves."""
from pathlib import Path
import hashlib,json,math,shutil,subprocess
from PIL import Image,ImageDraw

root=Path.cwd();out=Path(__file__).resolve().parent
qa=max((root/'run-benihime-qa/base/ryujin-bankai-qa').glob('cycle118-*'),key=lambda p:p.stat().st_mtime)
selected=[];tiles=[];scenarios=[]
selection={'01_day_back':[40,110,150,170], '02_night_front':[60,170],
 '03_first_return':[220,236,242], '04_left_front':[170], '05_near_wall':[170],
 '09_army_and_supreme':[170,178,182,192], '10_supreme_front':[168,180],
 '11_supreme_profile':[180], '12_supreme_left_profile':[180]}
for folder in sorted(qa.iterdir()):
 if not folder.is_dir():continue
 rows=[json.loads(line) for line in (folder/'frames.jsonl').read_text().splitlines()]
 gaps=[{'ms':b['wallMs']-a['wallMs'],'from_tick':a['tick'],'to_tick':b['tick']} for a,b in zip(rows,rows[1:]) if 170<=a['tick']<=190]
 scenarios.append({'case':folder.name,'samples':len(rows),'impact_window_sample_gaps':gaps,'last_sample':rows[-1]})
 for target in selection.get(folder.name,[]):
  row=min(rows,key=lambda r:abs(r['tick']-target));src=folder/row['file']
  name=f"{folder.name}-t{row['tick']:03d}.png";shutil.copy2(src,out/name)
  selected.append({'case':folder.name,'tick':row['tick'],'source':str(src),'file':name,'sha256':hashlib.sha256(src.read_bytes()).hexdigest()})
  with Image.open(src) as im:
   tile=Image.new('RGB',(400,250),(16,16,20));tile.paste(im.convert('RGB').resize((400,225)),(0,25))
   ImageDraw.Draw(tile).text((6,6),f"{folder.name} / tick {row['tick']}",fill='white');tiles.append(tile)
contact=Image.new('RGB',(1600,250*math.ceil(len(tiles)/4)),(16,16,20))
for i,tile in enumerate(tiles):contact.paste(tile,((i%4)*400,(i//4)*250))
contact.save(out/'contact.jpg',quality=86)
for name in ['check','gametest','client']:
 text=(root/f'build/cycle118-{name}.log').read_text(encoding='utf-8',errors='replace')
 lines=[line for line in text.splitlines() if any(key in line for key in ['PASS:', 'All 233 required','BUILD SUCCESSFUL','Ryujin Bankai QA completed','Ryujin Bankai QA result','steam cut terrain','FAIL','FAILED','ERROR'])]
 (out/(name+'-summary.txt')).write_text('\n'.join(lines)+'\n',encoding='utf-8')
manifest={'base':'58e3e4e','source_qa_directory':str(qa),'scenarios':scenarios,'selected_real_screenshots':selected,
 'warning':'Sampled framebuffer captures, not continuous video or a controlled benchmark. Gaps include capture overhead. Audio not subjectively auditioned.',
 'runtime_sounds':{},'code':{}}
for path in (root/'src/main/resources/assets/bleachmod/sounds').glob('ryujin*.ogg'):
 manifest['runtime_sounds'][path.name]=hashlib.sha256(path.read_bytes()).hexdigest()
for name in ['RyujinBankaiTimeline','RyujinBankaiCamera']:
 path=root/f'src/main/java/com/bleachmod/common/visual/{name}.java';manifest['code'][name]=hashlib.sha256(path.read_bytes()).hexdigest()
(out/'captures.json').write_text(json.dumps(manifest,indent=2,ensure_ascii=False),encoding='utf-8')
# Explicitly silent sampled preview; not continuous motion capture.
temp=root/'build/cycle118-preview';temp.mkdir(exist_ok=True)
for i,tick in enumerate(range(0,241,10)):
 shutil.copy2(qa/'01_day_back/screenshots'/f'frame-t{tick:03d}.png',temp/f'{i:03d}.png')
subprocess.run([str(root/'build/video-review-tools/ffmpeg.exe'),'-v','error','-y','-framerate','2','-i',str(temp/'%03d.png'),'-vf','scale=960:-2','-c:v','libx264','-pix_fmt','yuv420p','-crf','24','-an',str(out/'ryujin-short-sampled-no-audio.mp4')],check=True)
print(json.dumps({'qa':str(qa),'samples':sum(s['samples'] for s in scenarios),'selected':len(selected)},indent=2))
