"""Original deterministic cloth, reveal and thread cues; no samples or network."""
import argparse
from array import array
import hashlib
import json
import math
from pathlib import Path
import random
import subprocess
import sys
import tempfile
import wave

RATE = 22050
TAU = 2 * math.pi
ROOT = Path(__file__).resolve().parents[2]
CUES = {"curtain": (2.8, 3.6, .7), "reveal": (2.4, 11.8, .8), "threads": (1.8, 22.5, .6)}


def generate(name, path, duration):
    rng = random.Random(69019 + list(CUES).index(name))
    samples = array("h")
    low = slower = 0.0
    peak = 0
    square_sum = 0.0
    for i in range(round(duration * RATE)):
        t = i / RATE
        noise = rng.uniform(-1, 1)
        low += .13 * (noise - low)
        slower += .018 * (noise - slower)
        fade = min(1, t / .08) * min(1, (duration - t) / .35)
        if name == "curtain":
            friction = .20 + .80 * math.sin(math.pi * t / duration) ** 2
            value = fade * friction * (.48 * (low - slower) + .055 * math.sin(TAU * 67 * t))
        elif name == "reveal":
            swell = (1 - math.exp(-t * 12)) * math.exp(-t * 1.4)
            value = fade * swell * (.25 * math.sin(TAU * 49 * t)
                                    + .10 * math.sin(TAU * 98 * t)
                                    + .045 * math.sin(TAU * 146.832 * t)
                                    + .16 * slower)
        else:
            pulse = math.exp(-t * 1.6) * (.7 + .3 * math.sin(TAU * 4 * t))
            value = fade * pulse * (.12 * math.sin(TAU * (440 * t + 27 * t * t))
                                    + .08 * math.sin(TAU * 659.255 * t) + .25 * (low - slower))
        sample = round(max(-1, min(1, value)) * 32767)
        samples.append(sample)
        peak = max(peak, abs(sample))
        square_sum += (sample / 32767) ** 2
    if sys.byteorder != "little":
        samples.byteswap()
    with wave.open(str(path), "wb") as wav:
        wav.setnchannels(1)
        wav.setsampwidth(2)
        wav.setframerate(RATE)
        wav.writeframes(samples.tobytes())
    return {"durationSeconds": duration, "channels": 1, "rate": RATE,
            "peakDbfs": 20 * math.log10(peak / 32767),
            "rmsDbfs": 10 * math.log10(square_sum / len(samples))}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--ffmpeg", type=Path, required=True)
    args = parser.parse_args()
    ffmpeg = args.ffmpeg.resolve(strict=True)
    report_dir = ROOT / "build/benihime-qa-69"
    report_dir.mkdir(parents=True, exist_ok=True)
    sounds = ROOT / "src/main/resources/assets/bleachmod/sounds"
    reports = {}
    with tempfile.TemporaryDirectory(prefix="effects-", dir=report_dir) as temporary:
        for name, (duration, cue, volume) in CUES.items():
            wav = Path(temporary) / f"{name}.wav"
            report = generate(name, wav, duration)
            target = sounds / f"benihime_{name}.ogg"
            encode = subprocess.run([str(ffmpeg), "-v", "error", "-nostdin", "-y", "-i", str(wav), "-c:a", "libvorbis", "-q:a", "5", "-map_metadata", "-1", str(target)], capture_output=True, text=True)
            if encode.returncode:
                raise RuntimeError(encode.stderr)
            decode = subprocess.run([str(ffmpeg), "-v", "error", "-i", str(target), "-f", "null", "-"], capture_output=True, text=True)
            report.update({"cueSeconds": cue, "proposedVolume": volume,
                           "sha256": hashlib.sha256(target.read_bytes()).hexdigest(),
                           "decodeExitCode": decode.returncode, "bytes": target.stat().st_size,
                           "origin": "Original deterministic mathematical synthesis; no samples"})
            if decode.returncode:
                raise RuntimeError(decode.stderr)
            reports[name] = report
    (report_dir / "benihime-effects.json").write_text(json.dumps(reports, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(reports, indent=2))


if __name__ == "__main__":
    main()
