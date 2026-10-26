"""Select evidence from this integration's two actual client runs; preserve originals."""
from pathlib import Path
import json,re,hashlib,shutil,math
from PIL import Image,ImageDraw

root=Path.cwd();out=Path(__file__).resolve().parent;tiles=[];manifest=[]
groups=[('senbon','senbonzakura-combat-qa',['01_combo_front','02_combo_profile','03_combo_back','04_hakuteiken_front','05_hakuteiken_profile','06_hakuteiken_back']),
        ('benihime','benihime-combat-qa',['sealed-melee','shikai-melee','bankai-melee','shikai-first-person'])]
for group,directory,cases in groups:
 log=(root/f'build/cycle120-client-{group}.log').read_text(encoding='utf-8',errors='replace')
 world_ids=[int(v) for v in re.findall(r'Benihime-QA-68-(\d+)',log)]
 assert world_ids,('world provenance missing',group)
 world_id=max(world_ids)
 for case in cases:
  folders=[p for p in (root/'run-benihime-qa/base'/directory).glob('*-'+case) if int(p.name.split('-')[0])>=world_id]
  assert folders,('current run capture missing',group,case)
  folder=max(folders,key=lambda p:p.stat().st_mtime)
  rows=[json.loads(line) for line in (folder/'frames.jsonl').read_text().splitlines()]
  assert rows
  manifest.append({'group':group,'case':case,'world_id':world_id,'source_directory':str(folder),'samples':len(rows),'selected':[]})
  targets=[44,64,88] if 'hakuteiken' in case else [32,46,68]
  for target in targets:
   row=min(rows,key=lambda r:abs(r['tick']-target));src=folder/row['file']
   if not src.is_file():src=folder/'screenshots'/Path(row['file']).name
   assert src.is_file(),str(src)
   name=f"{group}-{case}-t{row['tick']:03d}.png";shutil.copy2(src,out/name)
   manifest[-1]['selected'].append({'tick':row['tick'],'file':name,'source':str(src),'sha256':hashlib.sha256(src.read_bytes()).hexdigest()})
   with Image.open(src) as im:
    tile=Image.new('RGB',(400,250),(16,16,20));tile.paste(im.convert('RGB').resize((400,225)),(0,25))
    ImageDraw.Draw(tile).text((5,6),f"{group}: {case} / t{row['tick']}",fill='white');tiles.append(tile)
for page in range(math.ceil(len(tiles)/12)):
 subset=tiles[page*12:(page+1)*12];canvas=Image.new('RGB',(1600,250*math.ceil(len(subset)/4)),(16,16,20))
 for i,tile in enumerate(subset):canvas.paste(tile,((i%4)*400,(i//4)*250))
 canvas.save(out/f'client-contact-{page+1}.jpg',quality=87)
(out/'client-captures.json').write_text(json.dumps(manifest,indent=2,ensure_ascii=False),encoding='utf-8')
for name in ['check','gametest','client-senbon','client-benihime']:
 text=(root/f'build/cycle120-{name}.log').read_text(encoding='utf-8',errors='replace')
 keys=['PASS:','required tests passed','BUILD SUCCESSFUL','BUILD FAILED','ERROR','loaded attacks:','QA prepared','QA result','Validation complete','sequence COMPLETE','combat QA 84 complete','cast Hakuteiken']
 lines=[line for line in text.splitlines() if any(key in line for key in keys)]
 (out/f'{name}-summary.txt').write_text('\n'.join(lines)+'\n',encoding='utf-8')
print(json.dumps({'scenarios':len(manifest),'samples':sum(item['samples'] for item in manifest),'selected_images':len(tiles)},indent=2))
