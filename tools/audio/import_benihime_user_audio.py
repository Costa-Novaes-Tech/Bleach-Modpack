"""Import user-supplied dialogue and ordered invocation mix without altering originals.

The ordered mix already contains both voices. Local playback must not layer dialogue
over this file. The original instrumental remains the voice-disabled fallback.
"""
import argparse
import hashlib
import json
from pathlib import Path
import subprocess

ROOT = Path(__file__).resolve().parents[2]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--source-root', type=Path, required=True)
    parser.add_argument('--ffmpeg', type=Path, required=True)
    args = parser.parse_args()
    output = ROOT / 'src/main/resources/assets/bleachmod/sounds'
    report = []
    conversions = [
        ('Bankai_voice_clean.wav', 'benihime_voice_bankai', 'aresample=44100,afade=t=in:d=0.01,afade=t=out:st=0.86:d=0.04', 1),
        ('Kannonbiraki_Benihime_Aratame_voice_clean.wav', 'benihime_voice_name', 'aresample=44100,afade=t=in:d=0.01,afade=t=out:st=4.08:d=0.043', 1),
        ('bankai_final_ordered_30s.wav', 'benihime_invocation_mix',
         'atrim=start=1.2:end=29.2,asetpts=PTS-STARTPTS,aresample=44100,volume=0.84,afade=t=in:d=0.10,afade=t=out:st=27.4:d=0.6', 2),
    ]
    for source_name, target_name, filters, channels in conversions:
        source = args.source_root / source_name
        target = output / (target_name + '.ogg')
        command = [str(args.ffmpeg), '-v', 'error', '-nostdin', '-y', '-i', str(source),
                   '-af', filters, '-ac', str(channels), '-c:a', 'libvorbis', '-q:a', '6',
                   '-map_metadata', '-1', str(target)]
        subprocess.run(command, check=True)
        subprocess.run([str(args.ffmpeg), '-v', 'error', '-i', str(target), '-f', 'null', '-'], check=True)
        report.append({'source': source_name, 'sourceSha256': hashlib.sha256(source.read_bytes()).hexdigest(),
                       'target': str(target.relative_to(ROOT)), 'sha256': hashlib.sha256(target.read_bytes()).hexdigest(),
                       'filters': filters, 'channels': channels})
    destination = ROOT / 'build/benihime-qa-71/audio-import.json'
    destination.parent.mkdir(parents=True, exist_ok=True)
    destination.write_text(json.dumps(report, indent=2), encoding='utf-8')
    print(destination)


if __name__ == '__main__':
    main()
