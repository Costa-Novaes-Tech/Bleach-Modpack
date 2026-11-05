"""Local, read-only media inspection for Minecraft cinematic QA.

Requires Pillow for video contact sheets, NumPy for audio levels, and either
imageio-ffmpeg or an ffmpeg executable on PATH. No network calls or ASR.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import math
import re
import shutil
import subprocess
from pathlib import Path


def ffmpeg_path() -> str:
    try:
        import imageio_ffmpeg

        return imageio_ffmpeg.get_ffmpeg_exe()
    except (ImportError, RuntimeError):
        executable = shutil.which("ffmpeg")
        if executable:
            return executable
        raise RuntimeError("FFmpeg unavailable: install imageio-ffmpeg or provide ffmpeg on PATH")


def digest(path: Path) -> str:
    sha = hashlib.sha256()
    with path.open("rb") as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b""):
            sha.update(chunk)
    return sha.hexdigest()


def probe(executable: str, source: Path) -> dict:
    result = subprocess.run(
        [executable, "-nostdin", "-hide_banner", "-i", str(source)],
        capture_output=True, text=True, check=False,
    )
    details = result.stderr
    duration = re.search(r"Duration: (\d+):(\d+):(\d+(?:\.\d+)?)", details)
    streams = [line.strip() for line in details.splitlines() if "Stream #" in line]
    return {
        "path": str(source),
        "sha256": digest(source),
        "duration_seconds": (
            int(duration[1]) * 3600 + int(duration[2]) * 60 + float(duration[3])
            if duration else None
        ),
        "streams": streams,
        "has_video": any("Video:" in line for line in streams),
        "has_audio": any("Audio:" in line for line in streams),
    }


def contact_sheet(executable: str, source: Path, output: Path, seconds: list[float]) -> dict:
    from PIL import Image, ImageDraw

    output.mkdir(parents=True, exist_ok=True)
    frames = []
    for index, second in enumerate(seconds):
        target = output / f"frame-{index:02d}-{second:.2f}s.png"
        command = [
            executable, "-nostdin", "-hide_banner", "-loglevel", "error",
            "-i", str(source), "-ss", str(second), "-frames:v", "1", "-y", str(target),
        ]
        result = subprocess.run(command, capture_output=True, check=False)
        if result.returncode or not target.is_file():
            raise RuntimeError(f"Could not decode frame at {second}s: {result.stderr.decode(errors='replace')[:300]}")
        frames.append((second, target))

    columns, width, height = 3, 400, 250
    rows = math.ceil(len(frames) / columns)
    sheet = Image.new("RGB", (columns * width, rows * height), "#161616")
    label = ImageDraw.Draw(sheet)
    for index, (second, path) in enumerate(frames):
        image = Image.open(path).convert("RGB")
        image.thumbnail((width - 12, height - 32))
        x, y = index % columns * width, index // columns * height
        sheet.paste(image, (x + (width - image.width) // 2, y + 25))
        label.text((x + 8, y + 5), f"{second:.2f}s", fill="white")
    target = output / "contact-sheet.png"
    sheet.save(target)
    return {"seconds": seconds, "frames": len(frames), "contact_sheet": str(target)}


def audio_levels(executable: str, source: Path) -> dict:
    import numpy as np

    result = subprocess.run(
        [executable, "-nostdin", "-hide_banner", "-loglevel", "error", "-i", str(source),
         "-ac", "1", "-ar", "48000", "-f", "f32le", "pipe:1"],
        capture_output=True, check=False,
    )
    if result.returncode:
        raise RuntimeError(f"Could not decode audio: {result.stderr.decode(errors='replace')[:300]}")
    samples = np.frombuffer(result.stdout, dtype="<f4")
    if not len(samples):
        raise RuntimeError("Decoded audio is empty")
    peak = float(np.max(np.abs(samples)))
    rms = float(np.sqrt(np.mean(samples.astype(np.float64) ** 2)))
    return {
        "path": str(source),
        "sha256": digest(source),
        "duration_seconds": round(len(samples) / 48000, 3),
        "sample_rate_hz": 48000,
        "peak_dbfs": round(20 * math.log10(max(peak, 1e-12)), 1),
        "rms_dbfs": round(20 * math.log10(max(rms, 1e-12)), 1),
        "note": "Isolated-file level only; not perceived quality or the in-game mix",
    }


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--video", type=Path, help="Existing local video to inspect")
    parser.add_argument("--audio", type=Path, action="append", default=[], help="Existing local audio; repeat as needed")
    parser.add_argument("--out", type=Path, help="Directory for contact sheet and JSON")
    parser.add_argument("--seconds", default="0,2,4,6,8,10,12", help="Comma-separated video timestamps, max 24")
    args = parser.parse_args()
    if not args.video and not args.audio:
        parser.error("provide --video or --audio")
    if args.video and not args.out:
        parser.error("--out is required for video contact sheets")
    executable = ffmpeg_path()
    report: dict = {"ffmpeg": executable, "video": None, "audio": []}
    if args.video:
        video = args.video.resolve(strict=True)
        seconds = [float(value) for value in args.seconds.split(",")]
        if not seconds or len(seconds) > 24 or any(not math.isfinite(t) or t < 0 for t in seconds):
            parser.error("--seconds must contain 1-24 finite, nonnegative timestamps")
        info = probe(executable, video)
        if not info["has_video"]:
            parser.error("video source has no video stream")
        if info["duration_seconds"] is not None and max(seconds) >= info["duration_seconds"]:
            parser.error("sample timestamp reaches or exceeds video duration")
        info.update(contact_sheet(executable, video, args.out.resolve(), seconds))
        report["video"] = info
    for source in args.audio:
        report["audio"].append(audio_levels(executable, source.resolve(strict=True)))
    if args.out:
        target = args.out.resolve() / "inspection.json"
        target.write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(report, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
