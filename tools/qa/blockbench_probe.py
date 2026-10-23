"""Small localhost client for the installed Blockbench MCP bridge; no script escape.

This checks the actual editor bridge, not a configured Codex stdio MCP session.
Each request is a JSON object with action and params. Media is saved, never printed.
"""
import argparse
import base64
import json
from pathlib import Path
import urllib.request

ALLOWED = {'get_status', 'list_outliner', 'load_project', 'edit_element', 'create_animation',
           'add_keyframes', 'preview_animation', 'list_animations', 'screenshot',
           'set_camera_angle', 'save_project', 'check_model', 'list_formats'}
ROOT = Path(__file__).resolve().parents[2]

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('requests', type=Path)
    parser.add_argument('--out', type=Path, required=True)
    args = parser.parse_args()
    out = args.out.resolve()
    if not out.is_relative_to(ROOT):
        raise ValueError('Evidence must stay in the workspace')
    out.mkdir(parents=True, exist_ok=True)
    records = []
    def clean(value, label):
        if isinstance(value, dict):
            data = value.get('data_url')
            result = {k: clean(v, label+'-'+k) for k, v in value.items() if k not in ('base64', 'data_url')}
            if data and data.startswith('data:image/png;base64,'):
                name = label+'.png'
                (out/name).write_bytes(base64.b64decode(data.split(',', 1)[1]))
                result['capture'] = name
            return result
        if isinstance(value, list):
            return [clean(v, label+'-'+str(i)) for i, v in enumerate(value)]
        return value
    for i, call in enumerate(json.loads(args.requests.read_text(encoding='utf-8-sig'))):
        if call['action'] not in ALLOWED:
            raise ValueError('Command outside the test allowlist')
        if call['action'] in ('load_project', 'save_project'):
            path = Path(call['params']['path']).resolve()
            if not path.is_relative_to(ROOT/'tools/models/lab'):
                raise ValueError('Only laboratory models may be loaded/saved by this probe')
        request = urllib.request.Request('http://127.0.0.1:8787/command',
            data=json.dumps(call).encode(), headers={'Content-Type': 'application/json'}, method='POST')
        with urllib.request.urlopen(request, timeout=45) as response:
            answer = json.load(response)
        if not answer.get('ok'):
            raise RuntimeError(answer.get('error', 'Bridge error'))
        record = {'request': call, 'response': clean(answer, f'{i:02d}-{call["action"]}')}
        records.append(record)
        (out/'commands.json').write_text(json.dumps(records, indent=2), encoding='utf-8')
        print(call['action'], 'OK')

if __name__ == '__main__':
    main()
