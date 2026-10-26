"""Read-only verification of versioned audio originals. No FFmpeg or third-party packages."""
import argparse
import hashlib
import json
import sys
import wave
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--manifest', type=Path, default=ROOT / 'tools/audio/source/manifest.json')
    args = parser.parse_args()
    manifest = json.loads(args.manifest.read_text(encoding='utf-8'))
    records = manifest['files']
    if not records:
        raise ValueError('Empty source manifest')
    seen = set()
    for record in records:
        path = (ROOT / record['path']).resolve()
        if not path.is_relative_to((ROOT / 'tools/audio/source').resolve()) or path.suffix.lower() != '.wav':
            raise ValueError('Unexpected source path: ' + record['path'])
        if path in seen:
            raise ValueError('Duplicate source path: ' + record['path'])
        seen.add(path)
        if path.stat().st_size != record['bytes']:
            raise ValueError('Size mismatch: ' + record['path'])
        if hashlib.sha256(path.read_bytes()).hexdigest() != record['sha256']:
            raise ValueError('SHA-256 mismatch: ' + record['path'])
        with wave.open(str(path), 'rb') as audio:
            expected = record['audio']
            if (audio.getframerate(), audio.getnchannels(), audio.getsampwidth(), audio.getnframes()) != (
                    expected['sample_rate'], expected['channels'], expected['sample_width_bytes'], expected['frames']):
                raise ValueError('WAV format mismatch: ' + record['path'])
        print('PASS ' + record['path'])
    print(str(len(records)) + ' versioned WAV sources verified; no files modified.')

if __name__ == '__main__':
    try:
        main()
    except (OSError, ValueError, KeyError, wave.Error) as exc:
        print('FAIL: ' + str(exc), file=sys.stderr)
        sys.exit(1)
