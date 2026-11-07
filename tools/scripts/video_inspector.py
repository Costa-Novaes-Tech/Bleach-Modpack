#!/usr/bin/env python3
"""Video Inspector & Frame Extraction Tool for Antigravity Bleach Mod Lab.

Extracts frames, generates timestamped contact sheets/storyboards, and inspects video
evidence (OBS recordings, Game Bar clips, user uploads) frame-by-frame.
Powered by imageio-ffmpeg / ffmpeg binary and PIL.
"""
from __future__ import annotations

import argparse
import json
import math
import os
import re
import subprocess
import sys
from pathlib import Path

try:
    import imageio_ffmpeg
    FFMPEG_EXE = imageio_ffmpeg.get_ffmpeg_exe()
except Exception:
    import shutil
    FFMPEG_EXE = shutil.which("ffmpeg") or "ffmpeg"

try:
    from PIL import Image, ImageDraw, ImageFont
    HAS_PIL = True
except ImportError:
    HAS_PIL = False


def probe_video(video_path: Path) -> dict:
    """Probes video metadata using ffmpeg."""
    cmd = [
        FFMPEG_EXE,
        "-i", str(video_path),
        "-hide_banner"
    ]
    proc = subprocess.run(cmd, stderr=subprocess.PIPE, stdout=subprocess.PIPE, text=True, encoding="utf-8", errors="replace")
    output = proc.stderr

    duration_match = re.search(r"Duration:\s*(\d+):(\d+):(\d+(?:\.\d+)?)", output)
    duration = 0.0
    if duration_match:
        h, m, s = duration_match.groups()
        duration = int(h) * 3600 + int(m) * 60 + float(s)

    video_match = re.search(r"Stream #\d+:\d+.*Video:\s*([^,\n]+),\s*([^,\n]+),\s*(\d+)x(\d+)", output)
    width, height, fps = 0, 0, 0.0
    codec = "unknown"
    if video_match:
        codec = video_match.group(1).strip()
        width = int(video_match.group(3))
        height = int(video_match.group(4))

    fps_match = re.search(r"(\d+(?:\.\d+)?)\s*fps", output)
    if fps_match:
        fps = float(fps_match.group(1))

    return {
        "path": str(video_path),
        "duration_seconds": duration,
        "width": width,
        "height": height,
        "fps": fps,
        "codec": codec
    }


def extract_frames(video_path: Path, out_dir: Path, fps: float = 2.0, max_frames: int = 60, start: float = 0.0, end: float | None = None) -> list[Path]:
    """Extracts timestamped PNG frames from a video."""
    out_dir.mkdir(parents=True, exist_ok=True)
    meta = probe_video(video_path)
    dur = meta["duration_seconds"]
    if end is None or end > dur:
        end = dur if dur > 0 else 30.0

    target_duration = max(0.1, end - start)
    # Clamp fps if frames exceed max_frames
    if target_duration * fps > max_frames:
        fps = max(0.2, max_frames / target_duration)

    pattern = str(out_dir / "frame_%04d.png")

    cmd = [FFMPEG_EXE, "-y"]
    if start > 0:
        cmd.extend(["-ss", f"{start:.3f}"])
    cmd.extend(["-i", str(video_path)])
    if end is not None and end > start:
        cmd.extend(["-t", f"{target_duration:.3f}"])
    cmd.extend([
        "-vf", f"fps={fps:.3f}",
        "-frame_pts", "1",
        pattern
    ])

    subprocess.run(cmd, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL, check=True)

    frames = sorted(out_dir.glob("frame_*.png"))

    # Rename with timestamp for crystal-clear readability
    renamed_frames = []
    for idx, f in enumerate(frames):
        ts = start + (idx / fps)
        new_name = out_dir / f"frame_{idx+1:03d}_{ts:05.2f}s.png"
        if f != new_name:
            if new_name.exists():
                new_name.unlink()
            f.rename(new_name)
        renamed_frames.append(new_name)

    return renamed_frames


def create_storyboard(frames: list[Path], out_image: Path, cols: int = 4, thumb_w: int = 320) -> Path:
    """Generates an annotated contact sheet grid of frames."""
    if not HAS_PIL or not frames:
        return out_image

    n = len(frames)
    cols = min(cols, n)
    rows = math.ceil(n / cols)

    sample_img = Image.open(frames[0])
    aspect = sample_img.height / sample_img.width
    thumb_h = int(thumb_w * aspect)

    margin = 8
    badge_h = 24
    cell_w = thumb_w
    cell_h = thumb_h + badge_h

    total_w = cols * cell_w + (cols + 1) * margin
    total_h = rows * cell_h + (rows + 1) * margin

    storyboard = Image.new("RGB", (total_w, total_h), (22, 22, 28))
    draw = ImageDraw.Draw(storyboard)

    for i, frame_path in enumerate(frames):
        r = i // cols
        c = i % cols
        x = margin + c * (cell_w + margin)
        y = margin + r * (cell_h + margin)

        with Image.open(frame_path) as im:
            im_resized = im.resize((thumb_w, thumb_h), Image.Resampling.LANCZOS)
            storyboard.paste(im_resized, (x, y))

        # Timestamp badge
        ts_match = re.search(r"(\d+\.\d+)s", frame_path.name)
        label = frame_path.stem
        if ts_match:
            label = f"T = {ts_match.group(1)}s ({frame_path.stem.split('_')[1]})"

        badge_box = [x, y + thumb_h, x + thumb_w, y + thumb_h + badge_h]
        draw.rectangle(badge_box, fill=(35, 35, 45))
        draw.text((x + 6, y + thumb_h + 4), label, fill=(240, 240, 245))

    out_image.parent.mkdir(parents=True, exist_ok=True)
    storyboard.save(out_image, format="PNG")
    return out_image


def main():
    parser = argparse.ArgumentParser(description="Antigravity Video Inspector & QA Tool")
    subparsers = parser.add_subparsers(dest="command", required=True)

    # Inspect
    p_info = subparsers.add_parser("info", help="Probe video metadata")
    p_info.add_argument("video", type=Path, help="Video file path")

    # Extract
    p_ext = subparsers.add_parser("extract", help="Extract frames from video")
    p_ext.add_argument("video", type=Path, help="Video file path")
    p_ext.add_argument("--out-dir", type=Path, default=Path("tools/models/lab/video_qa/frames"), help="Output directory")
    p_ext.add_argument("--fps", type=float, default=2.0, help="Frames per second")
    p_ext.add_argument("--max-frames", type=int, default=40, help="Maximum frame count")
    p_ext.add_argument("--start", type=float, default=0.0, help="Start time in seconds")
    p_ext.add_argument("--end", type=float, default=None, help="End time in seconds")
    p_ext.add_argument("--storyboard", type=Path, default=None, help="Path to generate contact sheet PNG")

    args = parser.parse_args()

    if args.command == "info":
        info = probe_video(args.video)
        print(json.dumps(info, indent=2))
    elif args.command == "extract":
        frames = extract_frames(args.video, args.out_dir, fps=args.fps, max_frames=args.max_frames, start=args.start, end=args.end)
        print(f"Extracted {len(frames)} frames into {args.out_dir}")
        if args.storyboard:
            sb = create_storyboard(frames, args.storyboard)
            print(f"Created storyboard at {sb}")


if __name__ == "__main__":
    main()
