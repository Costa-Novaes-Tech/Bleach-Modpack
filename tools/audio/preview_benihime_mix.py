"""Offline audio-only mix preview. This is not Minecraft playback evidence."""
import argparse
from array import array
import hashlib
import json
import math
from pathlib import Path
import subprocess
import sys
import wave

ROOT = Path(__file__).resolve().parents[2]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--ffmpeg", type=Path, required=True)
    args = parser.parse_args()
    ffmpeg = args.ffmpeg.resolve(strict=True)
    destination = ROOT / "build/benihime-qa-69"
    destination.mkdir(parents=True, exist_ok=True)
    sounds = ROOT / "src/main/resources/assets/bleachmod/sounds"
    inputs = [sounds / f"benihime_{name}.ogg" for name in
              ("entrance", "voice_bankai", "voice_name", "curtain", "reveal", "threads")]
    cues = [
        {"asset": "entrance", "tick": 0, "volume": .8, "pitch": 1},
        {"asset": "voice_bankai", "tick": 28, "volume": 1, "pitch": 1},
        {"asset": "voice_name", "tick": 460, "volume": 1, "pitch": 1},
        {"asset": "curtain", "tick": 72, "volume": .7, "pitch": 1},
        {"asset": "curtain", "tick": 176, "volume": .35, "pitch": 1.2},
        {"asset": "reveal", "tick": 236, "volume": .8, "pitch": 1},
        {"asset": "threads", "tick": 450, "volume": .6, "pitch": 1},
    ]
    filters = (
        "[0:a]aformat=sample_rates=22050:channel_layouts=stereo,volume=0.8[music];"
        "[1:a]pan=stereo|c0=c0|c1=c0,aformat=sample_rates=22050,adelay=1400|1400[bankai];"
        "[2:a]pan=stereo|c0=c0|c1=c0,aformat=sample_rates=22050,adelay=23000|23000[name];"
        "[3:a]asplit=2[cloth1][cloth2];"
        "[cloth1]pan=stereo|c0=c0|c1=c0,aformat=sample_rates=22050,volume=0.7,adelay=3600|3600[curtain];"
        "[cloth2]asetrate=26460,aresample=22050,pan=stereo|c0=c0|c1=c0,volume=0.35,adelay=8800|8800[grip];"
        "[4:a]pan=stereo|c0=c0|c1=c0,aformat=sample_rates=22050,volume=0.8,adelay=11800|11800[reveal];"
        "[5:a]pan=stereo|c0=c0|c1=c0,aformat=sample_rates=22050,volume=0.6,adelay=22500|22500[threads];"
        "[music][bankai][name][curtain][grip][reveal][threads]amix=inputs=7:duration=longest:normalize=0,apad=whole_dur=28,atrim=duration=28[mix]"
    )
    wav = destination / "mix-preview-OFFLINE-NOT-MINECRAFT.wav"
    command = [str(ffmpeg), "-v", "error", "-nostdin", "-y"]
    for source in inputs:
        command.extend(["-i", str(source)])
    command.extend(["-filter_complex", filters, "-map", "[mix]", "-c:a", "pcm_s24le", str(wav)])
    mix = subprocess.run(command, capture_output=True, text=True)
    if mix.returncode:
        raise RuntimeError(mix.stderr)
    # Decode to floating-point PCM for exact numerical peak and RMS measurement.
    pcm = subprocess.run([str(ffmpeg), "-v", "error", "-i", str(wav), "-f", "f32le", "-acodec", "pcm_f32le", "-"], capture_output=True)
    if pcm.returncode:
        raise RuntimeError(pcm.stderr.decode(errors="replace"))
    samples = array("f")
    samples.frombytes(pcm.stdout)
    if sys.byteorder != "little":
        samples.byteswap()
    peak = max(map(abs, samples))
    rms = math.sqrt(sum(value * value for value in samples) / len(samples))
    ogg = destination / "mix-preview.ogg"
    conversion = subprocess.run([str(ffmpeg), "-v", "error", "-y", "-i", str(wav), "-c:a", "libvorbis", "-q:a", "6",
                                 "-metadata", "title=OFFLINE MIX PREVIEW - NOT MINECRAFT EVIDENCE", str(ogg)], capture_output=True, text=True)
    if conversion.returncode:
        raise RuntimeError(conversion.stderr)
    check = subprocess.run([str(ffmpeg), "-v", "error", "-i", str(ogg), "-f", "null", "-"], capture_output=True, text=True)
    report = {"kind": "Offline audio-only mix preview, not in-game evidence", "durationSeconds": len(samples) / (22050 * 2),
              "channels": 2, "sampleRate": 22050, "peakDbfs": 20 * math.log10(peak), "rmsDbfs": 20 * math.log10(rms),
              "clippedPcmSamples": sum(abs(sample) >= 1 for sample in samples), "cues": cues, "command": command,
              "inputHashes": {path.name: hashlib.sha256(path.read_bytes()).hexdigest() for path in inputs},
              "wavSha256": hashlib.sha256(wav.read_bytes()).hexdigest(), "oggSha256": hashlib.sha256(ogg.read_bytes()).hexdigest(),
              "mixExitCode": mix.returncode, "decodeExitCode": check.returncode,
              "limitations": "No Minecraft category sliders, distance attenuation, occlusion, device response or real game scheduling represented; human listening pending",
              "recommendation": "Keep requested starting volumes if measured headroom remains; confirm VOICE versus MUSIC/SFX in Minecraft before further gain increases"}
    (destination / "mix-preview-report.json").write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({key: value for key, value in report.items() if key not in ("command", "inputHashes")}, indent=2))
    if check.returncode:
        raise RuntimeError(check.stderr)


if __name__ == "__main__":
    main()
