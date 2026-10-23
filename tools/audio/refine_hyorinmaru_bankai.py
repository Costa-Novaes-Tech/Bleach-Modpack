"""Local dialogue finishing. Never infer missing syllables from an already processed source."""
import argparse
import hashlib
import json
import subprocess
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]


def main():
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument('--ffmpeg', type=Path, required=True)
    p.add_argument('--source', type=Path, required=True)
    p.add_argument('--processed-source', action='store_true', help='Source is the previously packaged OGG, not original speech')
    p.add_argument('--report', type=Path, required=True)
    args = p.parse_args()
    target = ROOT / 'src/main/resources/assets/bleachmod/sounds/hyorinmaru_voice_bankai.ogg'
    if args.source.resolve() == target.resolve():
        raise SystemExit('Preserve a separate source; do not process the output in place.')
    # Keep all source speech. Add a small room decay after the last phoneme rather than
    # fading the utterance at a hardcoded timestamp. This cannot recover previously lost audio.
    filters = 'aresample=44100,' + ('' if args.processed_source else 'volume=1.344,afade=t=in:d=0.005,')
    filters += 'aecho=1:1:55|105:0.08|0.025,apad=pad_dur=0.12,alimiter=limit=0.89:level=false'
    subprocess.run([str(args.ffmpeg), '-v', 'error', '-nostdin', '-y', '-i', str(args.source),
                    '-af', filters, '-ac', '1', '-ar', '44100', '-c:a', 'libvorbis', '-q:a', '6',
                    '-map_metadata', '-1', str(target)], check=True)
    subprocess.run([str(args.ffmpeg), '-v', 'error', '-i', str(target), '-f', 'null', '-'], check=True)
    args.report.parent.mkdir(parents=True, exist_ok=True)
    args.report.write_text(json.dumps(dict(source=str(args.source), processedSource=args.processed_source,
        sourceSHA256=hashlib.sha256(args.source.read_bytes()).hexdigest(), filters=filters,
        outputSHA256=hashlib.sha256(target.read_bytes()).hexdigest(),
        limitation='Previously faded/truncated phonemes cannot be recovered from a processed OGG.' if args.processed_source else None), indent=2), encoding='utf8')


if __name__ == '__main__':
    main()
