"""Generate original Benihime entrance music and optional procedural sound cues.

No samples, external recordings, speech synthesis, network or third-party Python
packages. The deterministic source remains editable; FFmpeg exports Ogg Vorbis.
"""
from __future__ import annotations

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
DURATION = 28
SEED = 68019
TAU = 2 * math.pi


def smooth(value):
    value = max(0.0, min(1.0, value))
    return value * value * (3.0 - 2.0 * value)


def duck(t, start, end, level):
    return 1.0 - (1.0 - level) * smooth((t - start) / .35) * smooth((end - t) / .35)


def synthesize(path):
    rng = random.Random(SEED)
    phases = [rng.uniform(0, TAU) for _ in range(12)]
    samples = array("h")
    peak = 0
    square_sum = 0.0
    for index in range(RATE * DURATION):
        t = index / RATE
        fade = smooth(t / 1.8) * smooth((DURATION - t) / 2.4)
        voice_space = duck(t, 1.4, 3.2, .20) * duck(t, 22.8, 27.1, .24)
        reveal = smooth((t - 11.8) / 2.0)
        rise = smooth((t - 18.8) / 2.4)
        breath = .88 + .12 * math.sin(TAU * .105 * t)
        drone = (.040 * math.sin(TAU * 55 * t + phases[0])
                 + .021 * math.sin(TAU * 82.4069 * t + phases[1])
                 + .011 * math.sin(TAU * 110 * t + .12 * math.sin(TAU * .08 * t))) * breath
        # Gently beating upper tones suggest strings without imitating a recording.
        string_gain = .007 + .008 * reveal + .007 * rise
        left_strings = right_strings = 0.0
        for harmonic, frequency in enumerate((220.0, 261.6256, 329.6276)):
            drift = .25 * math.sin(TAU * (.13 + harmonic * .017) * t)
            envelope = .68 + .18 * math.sin(TAU * .071 * t + phases[harmonic + 2])
            left_strings += string_gain * envelope * math.sin(TAU * frequency * t + drift + phases[harmonic + 3])
            right_strings += string_gain * envelope * math.sin(TAU * (frequency + .19) * t - drift + phases[harmonic + 3])
        pulse = 0.0
        for event in (5.4, 8.8, 13.0, 16.5, 19.0, 22.0, 26.2):
            elapsed = t - event
            if 0 <= elapsed < 2.2:
                pulse += .025 * smooth(elapsed / .055) * math.exp(-elapsed * 2.8) * math.sin(TAU * (46 * elapsed + 3 * (1 - math.exp(-elapsed * 4))))
        # A sparse, breathy high halo accompanies the reveal and settles quickly.
        halo = .0035 * reveal * (.65 + .35 * rise) * math.sin(TAU * 659.255 * t + .3 * math.sin(TAU * .19 * t))
        gain = 3.5 * fade * voice_space
        for value in ((drone + left_strings + pulse + halo) * gain,
                      (drone + right_strings + pulse - .7 * halo) * gain):
            encoded = round(max(-1.0, min(1.0, value)) * 32767)
            samples.append(encoded)
            peak = max(peak, abs(encoded))
            square_sum += (encoded / 32767) ** 2
    if sys.byteorder != "little":
        samples.byteswap()
    with wave.open(str(path), "wb") as output:
        output.setnchannels(2)
        output.setsampwidth(2)
        output.setframerate(RATE)
        output.writeframes(samples.tobytes())
    return {"durationSeconds": DURATION, "sampleRate": RATE, "channels": 2,
            "seed": SEED, "pcmPeakDbfs": 20 * math.log10(peak / 32767),
            "pcmRmsDbfs": 10 * math.log10(square_sum / len(samples)),
            "voiceDucksSeconds": [[1.4, 3.2], [22.8, 27.1]],
            "gainComparedWithCycle68Db": 20 * math.log10(3.5),
            "source": "Original local mathematical synthesis; no samples or voices"}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--ffmpeg", type=Path, required=True)
    parser.add_argument("--output", type=Path, default=Path(__file__).resolve().parents[2] / "src/main/resources/assets/bleachmod/sounds/benihime_entrance.ogg")
    parser.add_argument("--report", type=Path, default=Path(__file__).resolve().parents[2] / "build/benihime-qa-69/benihime-audio.json")
    args = parser.parse_args()
    ffmpeg = args.ffmpeg.resolve(strict=True)
    destination = args.output.resolve()
    destination.parent.mkdir(parents=True, exist_ok=True)
    args.report.parent.mkdir(parents=True, exist_ok=True)
    with tempfile.TemporaryDirectory(prefix="original-audio-", dir=args.report.parent.resolve()) as temporary:
        wav_path = Path(temporary) / "original-cue.wav"
        report = synthesize(wav_path)
        command = [str(ffmpeg), "-hide_banner", "-nostdin", "-y", "-i", str(wav_path),
                   "-c:a", "libvorbis", "-q:a", "4", "-ar", str(RATE), "-ac", "2",
                   "-map_metadata", "-1", "-metadata", "title=Benihime Entrance - Original Atmospheric Cue",
                   "-metadata", "comment=Original deterministic local synthesis; no external recordings or voices",
                   str(destination)]
        conversion = subprocess.run(command, capture_output=True, text=True)
        report["encodeExitCode"] = conversion.returncode
        if conversion.returncode:
            raise RuntimeError(conversion.stderr)
    decode = subprocess.run([str(ffmpeg), "-hide_banner", "-i", str(destination), "-f", "null", "-"], capture_output=True, text=True)
    report.update({"file": str(destination), "bytes": destination.stat().st_size,
                   "sha256": hashlib.sha256(destination.read_bytes()).hexdigest(),
                   "decodeExitCode": decode.returncode,
                   "decodeLog": decode.stderr,
                   "listeningReview": "Pending in-game listening and mix validation"})
    args.report.parent.mkdir(parents=True, exist_ok=True)
    args.report.write_text(json.dumps(report, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(json.dumps({key: value for key, value in report.items() if key != "decodeLog"}, indent=2))
    if decode.returncode:
        raise RuntimeError("Generated OGG failed full FFmpeg decode")


if __name__ == "__main__":
    main()
