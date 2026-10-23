"""Audit actual Forge bone traces: stationary supports must not follow the moving torso."""
import argparse
import json
import math
from pathlib import Path


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('source', type=Path)
    parser.add_argument('output', type=Path)
    args = parser.parse_args()
    rows = [json.loads(line) for line in (args.source/'frames.jsonl').read_text(encoding='utf-8').splitlines()]
    report = {'source': str(args.source.resolve()), 'positionSource': 'GeckoLib tracked bones + outer entrance translation', 'views': {}}
    for name, prefix in [('front', 'entrance-'), ('oblique', 'entrance-side-')]:
        view = [r for r in rows if Path(r['file']).stem.startswith(prefix)
                and Path(r['file']).stem[len(prefix):].isdigit()]
        result = {}
        for key, start, end in [('rightWrist', 78, 92), ('leftWrist', 98, 110),
                                ('rightAnkle', 112, 136), ('leftAnkle', 124, 136)]:
            samples = [r for r in view if start <= r['entranceAge'] <= end]
            if len(samples) < 3:
                raise AssertionError(f'{name}/{key}: insufficient real client samples')
            points = [[r[key+a] for a in 'XYZ'] for r in samples]
            drift = max(math.dist(points[0], point) for point in points)
            y_offset = .875 if 'Wrist' in key else .1875
            ground_error = max(abs(r[key+'Y']-r['bossY']-y_offset-.01) for r in samples)
            result[key] = {'samples': len(samples), 'ageWindow': [start, end],
                           'maxDriftBlocks': drift, 'maxSupportHeightErrorBlocks': ground_error}
        for side in ['right', 'left']:
            heights = [r[side+'AnkleY']-r['bossY']-.1975 for r in view]
            result[side+'FootLiftBlocks'] = max(heights)
        report['views'][name] = result
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(report, indent=2)+'\n', encoding='utf-8')
    print(json.dumps(report, indent=2))
    for name, view in report['views'].items():
        for key, value in view.items():
            if isinstance(value, dict):
                assert value['maxDriftBlocks'] < .05, f'{name}/{key} slides while planted'
                assert value['maxSupportHeightErrorBlocks'] < .05, f'{name}/{key} misses the floor'


if __name__ == '__main__':
    main()
