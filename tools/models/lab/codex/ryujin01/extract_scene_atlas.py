"""Extract the exact atlas embedded in a saved Blockbench scene."""
from __future__ import annotations
import base64, json, sys
from pathlib import Path
root=Path(__file__).resolve().parent
name=sys.argv[1] if len(sys.argv)>1 else 'ryujin_bankai_activation_scene_v7_lab'
model=json.loads((root/f'{name}.bbmodel').read_text(encoding='utf-8'))
source=model['textures'][0]['source']
assert source.startswith('data:image/png;base64,')
image=base64.b64decode(source.split('base64,',1)[1])
assert image[:8]==b'\x89PNG\r\n\x1a\n'
(root/f'{name}.png').write_bytes(image)
print(name,len(image),'PNG bytes')
