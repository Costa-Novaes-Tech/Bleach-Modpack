"""Opt-in offline Japanese placeholder voice, using isolated OpenJTalk/HTS Mei.

No actor cloning, online inference, speech recordings or automatic downloads.
Requires pyopenjtalk-plus installed into --packages (project-local recommended).
HTS Voice Mei: MMDAgent Project Team, (c) 2009-2013 Nagoya Institute of
Technology, Department of Computer Science. CC Attribution 3.0:
https://creativecommons.org/licenses/by/3.0/
https://github.com/tsukumijima/pyopenjtalk-plus/blob/master/pyopenjtalk/htsvoice/LICENSE_mei_normal.htsvoice
Preserve attribution and license when redistributing this derivative voice.
"""
import argparse
import hashlib
import json
from pathlib import Path
import subprocess
import sys
import wave

ROOT = Path(__file__).resolve().parents[2]
LINES = {
    "bankai": {"display": "卍解", "kana": "バンカイ", "cueTick": 28, "speed": .86, "maximumSeconds": 1.8},
    "name": {"display": "観音開紅姫改メ", "kana": "カンノンビラキ ベニヒメ アラタメ", "cueTick": 460, "speed": .86, "maximumSeconds": 3.5},
}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--packages", type=Path, required=True, help="Explicit isolated pyopenjtalk-plus package directory")
    parser.add_argument("--ffmpeg", type=Path, required=True)
    parser.add_argument("--generate", action="store_true", help="Explicitly opt in to local placeholder voice generation")
    parser.add_argument("--half-tone", type=float, default=-7, help="Generic pitch adjustment; never matches a real actor")
    parser.add_argument("--output", type=Path, default=ROOT / "src/main/resources/assets/bleachmod/sounds")
    args = parser.parse_args()
    if not args.generate:
        parser.error("Pass --generate to explicitly synthesize the provisional Japanese voice")
    sys.path.insert(0, str(args.packages.resolve(strict=True)))
    import numpy as np
    import pyopenjtalk

    ffmpeg = args.ffmpeg.resolve(strict=True)
    reports = ROOT / "build/benihime-qa-69/tts"
    reports.mkdir(parents=True, exist_ok=True)
    args.output.mkdir(parents=True, exist_ok=True)
    result = {"engine": "pyopenjtalk-plus", "version": pyopenjtalk.__version__,
              "voice": "HTS Mei normal, generic pitch-adjusted Japanese placeholder",
              "license": "CC-BY-3.0", "attribution": "HTS Voice Mei, released by MMDAgent Project Team; Copyright (c) 2009-2013 Nagoya Institute of Technology, Department of Computer Science",
              "source": "https://github.com/tsukumijima/pyopenjtalk-plus/blob/master/pyopenjtalk/htsvoice/LICENSE_mei_normal.htsvoice",
              "processing": "Local CPU only; kana input; original and lowered-pitch WAVs kept for comparison; no actor imitation",
              "quality": "Provisional synthetic voice; Japanese pronunciation/intonation and game mix require human listening",
              "phrases": {}}
    for name, line in LINES.items():
        labels = pyopenjtalk.extract_fullcontext(line["kana"], use_vanilla=True, predict_nani=False, use_sudachi_kanji_yomi=False)
        phonemes = pyopenjtalk.g2p(line["kana"], use_vanilla=True, predict_nani=False, use_sudachi_kanji_yomi=False)
        for variant, pitch in (("original", 0), ("lowered", args.half_tone)):
            # Fresh engine avoids accumulating pitch shifts across phrases.
            engine = pyopenjtalk.HTSEngine(pyopenjtalk.DEFAULT_HTS_VOICE)
            rate = engine.get_sampling_frequency()
            engine.set_speed(line["speed"])
            engine.add_half_tone(pitch)
            samples = np.asarray(engine.synthesize(labels), dtype=np.float64)
            peak = float(np.max(np.abs(samples)))
            pcm = np.round(samples / max(peak, 1) * .82 * 32767).astype("<i2")
            wav_path = reports / f"{name}-{variant}.wav"
            with wave.open(str(wav_path), "wb") as wav:
                wav.setnchannels(1)
                wav.setsampwidth(2)
                wav.setframerate(rate)
                wav.writeframes(pcm.tobytes())
            if variant == "lowered":
                duration = len(pcm) / rate
                tempo = max(1.0, duration / line["maximumSeconds"])
                target = args.output / f"benihime_voice_{name}.ogg"
                filters = f"highpass=f=80,lowpass=f=8000,atempo={tempo:.7f},loudnorm=I=-17:TP=-2:LRA=5"
                encode = subprocess.run([str(ffmpeg), "-v", "error", "-nostdin", "-y", "-i", str(wav_path), "-af", filters,
                                         "-ar", "22050", "-ac", "1", "-c:a", "libvorbis", "-q:a", "5", "-map_metadata", "-1", str(target)], capture_output=True, text=True)
                if encode.returncode:
                    raise RuntimeError(encode.stderr)
                decoded = reports / f"{name}-final-decoded.wav"
                check = subprocess.run([str(ffmpeg), "-v", "error", "-y", "-i", str(target), "-c:a", "pcm_s16le", str(decoded)], capture_output=True, text=True)
                if check.returncode:
                    raise RuntimeError(check.stderr)
                with wave.open(str(decoded), "rb") as wav:
                    final_duration = wav.getnframes() / wav.getframerate()
                result["phrases"][name] = {**line, "phonemes": phonemes, "halfTone": pitch,
                    "sourceDurationSeconds": duration, "finalDurationSeconds": final_duration,
                    "tempoFactor": tempo, "file": str(target), "bytes": target.stat().st_size,
                    "sha256": hashlib.sha256(target.read_bytes()).hexdigest(),
                    "encodeExitCode": encode.returncode, "decodeExitCode": check.returncode}
    (reports / "voice-report.json").write_text(json.dumps(result, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(json.dumps(result, indent=2, ensure_ascii=True))


if __name__ == "__main__":
    main()
