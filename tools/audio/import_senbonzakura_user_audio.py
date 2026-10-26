"""Prepare supplied Senbonzakura WAVs locally; preserve sources and keep dialogue/music separate."""
import argparse
import hashlib
import json
import subprocess
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]

def sha256_file(path: Path) -> str:
    h = hashlib.sha256()
    h.update(path.read_bytes())
    return h.hexdigest()

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--source-root', type=Path, default=Path(r'C:\Users\joaop\Downloads'))
    parser.add_argument('--ffmpeg', type=Path, default=ROOT / 'build/audio-tools/imageio_ffmpeg/binaries/ffmpeg-win-x86_64-v7.1.exe')
    args = parser.parse_args()

    target = ROOT / 'src/main/resources/assets/bleachmod/sounds'
    report = ROOT / 'build/cycle101-audio'
    report.mkdir(parents=True, exist_ok=True)
    target.mkdir(parents=True, exist_ok=True)

    # Dynamic ducking expression for 14.5s (290 ticks) scene:
    # 0..0.3s: 0.75 | 0.3..0.5s: duck to 0.28 | 0.5..3.8s: 0.28 (during "Bankai")
    # 3.8..4.3s: swell to 0.85 | 4.3..7.8s: 0.85 (during colossal blade rising)
    # 7.8..8.1s: duck to 0.28 | 8.1..11.4s: 0.28 (during "Senbonzakura Kageyoshi")
    # 11.4..11.8s: swell to 0.90 | 11.8..13.5s: 0.90 (during petal gale burst)
    # 13.5..14.5s: smooth natural fade out
    duck_expr = (
        'volume=\'if(lt(t,0.3),0.75,'
        'if(lt(t,0.5),0.75-0.47*(t-0.3)/0.2,'
        'if(lt(t,3.8),0.28,'
        'if(lt(t,4.3),0.28+0.57*(t-3.8)/0.5,'
        'if(lt(t,7.8),0.85,'
        'if(lt(t,8.1),0.85-0.57*(t-7.8)/0.3,'
        'if(lt(t,11.4),0.28,'
        'if(lt(t,11.8),0.28+0.62*(t-11.4)/0.4,'
        '0.90))))))))\':eval=frame'
    )

    specs = [
        ('Senbonzakura_Bankai_complete_voice.wav', 'senbonzakura_voice_bankai', 1,
         'aresample=44100,volume=1.0,afade=t=in:d=0.005', None),
        ('Senbonzakura_Kageyoshi_best_voice.wav', 'senbonzakura_voice_name', 1,
         'aresample=44100,volume=1.0,afade=t=in:d=0.005', None),
        ('Senbonzakura_music_only_extended30s.wav', 'senbonzakura_theme', 2,
         f'aresample=44100,{duck_expr},afade=t=in:d=0.2,afade=t=out:st=13.5:d=1.0', '14.5')
    ]

    records = []
    for name, stem, channels, filters, dur in specs:
        source = args.source_root / name
        out = target / (stem + '.ogg')
        cmd = [str(args.ffmpeg), '-v', 'error', '-nostdin', '-y', '-i', str(source)]
        if dur:
            cmd.extend(['-t', dur])
        cmd.extend(['-af', filters, '-ac', str(channels), '-ar', '44100', '-c:a', 'libvorbis', '-q:a', '6', '-map_metadata', '-1', str(out)])
        subprocess.run(cmd, check=True)
        records.append({
            'source': name,
            'sourceSha256': sha256_file(source),
            'output': str(out.relative_to(ROOT)),
            'sha256': sha256_file(out),
            'channels': channels,
            'filters': filters
        })

    # Offline mix preview
    preview_out = report / 'preview_mix.wav'
    cmd_mix = [
        str(args.ffmpeg), '-v', 'error', '-nostdin', '-y',
        '-i', str(target / 'senbonzakura_theme.ogg'),
        '-i', str(target / 'senbonzakura_voice_bankai.ogg'),
        '-i', str(target / 'senbonzakura_voice_name.ogg'),
        '-filter_complex',
        '[1:a]adelay=500:all=1[b];[2:a]adelay=8000:all=1[n];[0:a][b][n]amix=inputs=3:duration=first:normalize=0[mix]',
        '-map', '[mix]', '-c:a', 'pcm_s16le', str(preview_out)
    ]
    subprocess.run(cmd_mix, check=True)

    report_json = report / 'import.json'
    report_json.write_text(json.dumps(records, indent=2), encoding='utf-8')
    print(f'Wrote audio import report: {report_json}')

if __name__ == '__main__':
    main()
