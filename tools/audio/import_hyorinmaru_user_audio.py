"""Prepare supplied Hyorinmaru WAVs locally; preserve sources and keep dialogue/music separate."""
import argparse
import hashlib
import json
import subprocess
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--source-root', type=Path, required=True)
    parser.add_argument('--ffmpeg', type=Path, required=True)
    args = parser.parse_args()
    target = ROOT / 'src/main/resources/assets/bleachmod/sounds'
    report = ROOT / 'build/cycle92-audio'
    report.mkdir(parents=True, exist_ok=True)
    # The source music is already softer than speech. Reduce another 9.1 dB,
    # then duck it by 14.9 dB during both voice windows (1.2s and16.5s).
    duck = "volume='if(lt(t,0.9),1,if(lt(t,1.15),1-0.82*(t-0.9)/0.25,if(lt(t,2.35),0.18,if(lt(t,2.8),0.18+0.82*(t-2.35)/0.45,if(lt(t,15.6),1,if(lt(t,16.3),1-0.82*(t-15.6)/0.7,0.18))))))':eval=frame"
    specs = [
        ('Daiguren_Bankai_voice_clean.wav', 'hyorinmaru_voice_bankai', 1,
         'aresample=44100,volume=1.344,afade=t=in:d=0.005,afade=t=out:st=1.02:d=0.03'),
        ('Daiguren_Hyorinmaru_voice_clean.wav', 'hyorinmaru_voice_name', 1,
         'aresample=44100,volume=1.093,afade=t=in:d=0.005,afade=t=out:st=2.47:d=0.03'),
        ('daiguren_hyorinmaru_ice_theme_adapted_20s.wav', 'hyorinmaru_theme', 2,
         'aresample=44100,volume=0.35,' + duck + ',afade=t=in:d=0.3,afade=t=out:st=19:d=1'),
    ]
    records = []
    for name, stem, channels, filters in specs:
        source = args.source_root / name
        out = target / (stem + '.ogg')
        subprocess.run([str(args.ffmpeg), '-v', 'error', '-nostdin', '-y', '-i', str(source),
                        '-af', filters, '-ac', str(channels), '-ar', '44100', '-c:a', 'libvorbis',
                        '-q:a', '6', '-map_metadata', '-1', str(out)], check=True)
        subprocess.run([str(args.ffmpeg), '-v', 'error', '-i', str(out), '-f', 'null', '-'], check=True)
        records.append(dict(source=name, sourceSha256=hashlib.sha256(source.read_bytes()).hexdigest(),
                            output=str(out.relative_to(ROOT)), sha256=hashlib.sha256(out.read_bytes()).hexdigest(),
                            channels=channels, filters=filters))
    # Offline listening preview at equal MUSIC/VOICE slider levels. Roar stays owned by the server.
    subprocess.run([str(args.ffmpeg), '-v', 'error', '-nostdin', '-y',
        '-i', str(target/'hyorinmaru_theme.ogg'), '-i', str(target/'hyorinmaru_voice_bankai.ogg'),
        '-i', str(target/'hyorinmaru_voice_name.ogg'), '-filter_complex',
        '[1:a]adelay=1200:all=1[b];[2:a]adelay=16500:all=1[n];[0:a][b][n]amix=inputs=3:duration=first:normalize=0[a]',
        '-map', '[a]', '-c:a', 'pcm_s16le', str(report/'preview.wav')], check=True)
    (report/'import.json').write_text(json.dumps(records, indent=2), encoding='utf-8')
    print(report/'import.json')

if __name__ == '__main__':
    main()
