"""Static integration evidence, not artistic approval. Run from repository root."""
from pathlib import Path
import hashlib,json,math,subprocess

root=Path.cwd();out=Path(__file__).resolve().parent;assets=root/'src/main/resources/assets/bleachmod'
names=['benihime_thrust','benihime_thrust_2','benihime_slash','senbonzakura_diagonal','senbonzakura_return','senbonzakura_thrust']
summary=[]
def finite(value):
 if isinstance(value,float):assert math.isfinite(value)
 elif isinstance(value,dict):
  for item in value.values():finite(item)
 elif isinstance(value,list):
  for item in value:finite(item)
for name in names:
 emote=assets/'attack_animations'/(name+'.json');bedrock=assets/'player_animation'/(name+'.json')
 data=json.loads(emote.read_text(encoding='utf-8'));finite(data)
 mirrored=json.loads(bedrock.read_text(encoding='utf-8'));finite(mirrored)
 e=data['emote'];item_moves=[m for m in e['moves'] if 'rightItem' in m]
 assert item_moves,(name,'missing rightItem')
 summary.append({'animation':name,'beginTick':e['beginTick'],'endTick':e['endTick'],'stopTick':e['stopTick'],
  'rightItem_keyframes':len(item_moves),'emote_sha256':hashlib.sha256(emote.read_bytes()).hexdigest(),
  'bedrock_sha256':hashlib.sha256(bedrock.read_bytes()).hexdigest()})
profile=json.loads((root/'src/main/resources/data/bleachmod/weapon_attributes/benihime.json').read_text())
assert [a['animation'] for a in profile['attributes']['attacks']]==['bleachmod:benihime_thrust','bleachmod:benihime_thrust_2','bleachmod:benihime_slash']
assert all(a['damage_multiplier']==1 for a in profile['attributes']['attacks'])
jar=root/'build/libs/bleachmod-0.2.0.jar'
result={'date':'2026-10-26','branch':subprocess.check_output(['git','branch','--show-current'],text=True).strip(),
 'merged_head':subprocess.check_output(['git','rev-parse','HEAD'],text=True).strip(),'animations':summary,
 'benihime_profile':profile,'jar_sha256':hashlib.sha256(jar.read_bytes()).hexdigest(),
 'status':'finite assets and channels verified; Java parser and runtime checked separately'}
(out/'assets.json').write_text(json.dumps(result,indent=2,ensure_ascii=False),encoding='utf-8')
print(json.dumps({'animations':len(summary),'jar_sha256':result['jar_sha256']},indent=2))
