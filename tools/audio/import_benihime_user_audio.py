"""Import user-supplied dialogue and ordered invocation mix without altering originals.

The ordered mix already contains both voices. Local playback must not layer dialogue
over this file. The original instrumental remains the voice-disabled fallback.
"""
import argparse
import math
import hashlib
import json
from pathlib import Path
import subprocess

ROOT = Path(__file__).resolve().parents[2]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--source-root', type=Path, required=True)
    parser.add_argument('--ffmpeg', type=Path, required=True)
    parser.add_argument('--only-name', action='store_true', help='Leave Bankai and other assets untouched')
    args = parser.parse_args()
    output = ROOT / 'src/main/resources/assets/bleachmod/sounds'
    report = []
    conversions = [
        ('Bankai_voice_clean.wav', 'benihime_voice_bankai', 'aresample=44100,afade=t=in:d=0.01,afade=t=out:st=0.86:d=0.04', 1),
        ('Kannonbiraki_Benihime_Aratame_voice_clean.wav', 'benihime_voice_name', 'aresample=44100,volume=4dB,afade=t=in:d=0.01,afade=t=out:st=4.08:d=0.043,alimiter=limit=0.97:level=false:latency=true', 1),
        ('bankai_final_ordered_30s.wav', 'benihime_invocation_mix',
         'atrim=start=1.2:end=29.2,asetpts=PTS-STARTPTS,aresample=44100,volume=0.84,afade=t=in:d=0.10,afade=t=out:st=27.4:d=0.6', 2),
    ]
    for source_name, target_name, filters, channels in conversions:
        if args.only_name and target_name == 'benihime_voice_bankai':
            continue
        source = args.source_root / source_name
        target = output / (target_name + '.ogg')
        if target_name == 'benihime_invocation_mix':
            # Reinforce only the supplied clean dialogue; do not boost the music window.
            # Embedded name gain/offset measured in cycle71:1.1303867 at24.2651875s,
            # hence23.0651875s after the existing1.2s trim. Use sample-accurate delay.
            # Earlier music fade reaches -14.9 dB before the complete name, without
            # moving either voice. Retain the sample-aligned embedded dialogue and
            # restore only the clean name to +4 dB relative to its original level.
            extra_gain = .84 * 1.1303867 * (math.pow(10, 4 / 20) - .18)
            duck = "volume='if(lt(t,20.8),1,if(lt(t,22.8),1-0.82*(t-20.8)/2,0.18))':eval=frame"
            graph = (f'[0:a]{filters},{duck}[bed];'
                     f'[1:a]aresample=44100,volume={extra_gain:.9f},adelay=1017175S:all=1[extra];'
                     '[bed][extra]amix=inputs=2:duration=first:normalize=0,'
                     "alimiter=limit=0.70:level=false:latency=true:enable='gte(t,20.8)'[final]")
            command = [str(args.ffmpeg), '-v', 'error', '-nostdin', '-y', '-i', str(source),
                       '-i', str(args.source_root / 'Kannonbiraki_Benihime_Aratame_voice_clean.wav'),
                       '-filter_complex', graph, '-map', '[final]']
        else:
            command = [str(args.ffmpeg), '-v', 'error', '-nostdin', '-y', '-i', str(source), '-af', filters]
        command += ['-ac', str(channels), '-c:a', 'libvorbis', '-q:a', '6', '-map_metadata', '-1', str(target)]
        subprocess.run(command, check=True)
        subprocess.run([str(args.ffmpeg), '-v', 'error', '-i', str(target), '-f', 'null', '-'], check=True)
        report.append({'source': source_name, 'sourceSha256': hashlib.sha256(source.read_bytes()).hexdigest(),
                       'target': str(target.relative_to(ROOT)), 'sha256': hashlib.sha256(target.read_bytes()).hexdigest(),
                       'filters': graph if target_name == 'benihime_invocation_mix' else filters, 'channels': channels})
    destination = ROOT / 'build/benihime-stage-74/audio-import.json'
    destination.parent.mkdir(parents=True, exist_ok=True)
    destination.write_text(json.dumps(report, indent=2), encoding='utf-8')
    print(destination)


if __name__ == '__main__':
    main()
