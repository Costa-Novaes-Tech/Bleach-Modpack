"""Package real boss QA framebuffer captures, preserving game tick timing and provenance."""
import argparse
import json
from pathlib import Path
from PIL import Image


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('source', type=Path)
    parser.add_argument('output', type=Path)
    args = parser.parse_args()
    args.output.mkdir(parents=True, exist_ok=True)
    rows = [json.loads(line) for line in (args.source/'frames.jsonl').read_text(encoding='utf-8').splitlines()]
    for prefix in ['melee-', 'charge-', 'slam-', 'walk-']:
        frames = [r for r in rows if Path(r['file']).name.startswith(prefix)]
        if not frames:
            continue
        with Image.open(args.source/frames[0]['file']) as first:
            palette = first.convert('RGB').resize((768, 432), Image.Resampling.LANCZOS).quantize(colors=192)
        pictures, durations = [], []
        for i, row in enumerate(frames):
            with Image.open(args.source/row['file']) as frame:
                pictures.append(frame.convert('RGB').resize((768, 432), Image.Resampling.LANCZOS)
                                .quantize(palette=palette, dither=Image.Dither.NONE))
            gap = frames[i+1]['gameTick']-row['gameTick'] if i+1<len(frames) else 2
            durations.append(max(50, gap*50))
        destination = args.output/(prefix.rstrip('-')+'.gif')
        pictures[0].save(destination, save_all=True, append_images=pictures[1:], duration=durations,
                         loop=0, disposal=1, optimize=True)
        print(f'{destination}: {len(pictures)} framebuffer samples; {sum(durations)/1000:.2f}s')
    for row in rows:
        if Path(row['file']).name[:2] in ['01', '02', '03', '04']:
            (args.output/Path(row['file']).name).write_bytes((args.source/row['file']).read_bytes())
    for filename in ['frames.jsonl', 'combat.jsonl']:
        path = args.source/filename
        if path.exists():
            (args.output/filename).write_bytes(path.read_bytes())
    trace = args.source/'combat.jsonl'
    if trace.exists():
        combat = [json.loads(line) for line in trace.read_text(encoding='utf-8').splitlines()]
        summary = {}
        for attack in ['MELEE', 'CHARGE', 'SLAM']:
            samples = [r for r in combat if r['attack'] == attack]
            summary[attack] = {'samples': len(samples), 'minTick': min((r['attackTick'] for r in samples), default=None),
                              'maxTick': max((r['attackTick'] for r in samples), default=None),
                              'minTargetHealth': min((r.get('targetHealth', 500) for r in samples), default=None),
                              'distanceTravelledZ': max((r['z'] for r in samples), default=0)-min((r['z'] for r in samples), default=0)}
        summary['source'] = str(args.source.resolve())
        summary['observedFrameFps'] = {'min': min(r['fpsObserved'] for r in rows), 'max': max(r['fpsObserved'] for r in rows)}
        (args.output/'combat-summary.json').write_text(json.dumps(summary, indent=2)+'\n', encoding='utf-8')
        print(json.dumps(summary))

if __name__ == '__main__':
    main()
