"""Package real Minecraft framebuffer captures without altering original PNG bytes.

Only frames.jsonl emitted by BenihimeEntranceClient is accepted. This tool does
not render models, control Minecraft, or certify visual quality automatically.
"""
from __future__ import annotations

import argparse
from datetime import datetime, timezone
import hashlib
import html
import json
import math
from pathlib import Path
import statistics
import struct
import subprocess
import sys


SOURCE = "Minecraft Screenshot.grab framebuffer"
CRITICAL_TICKS = (0, 72, 108, 137, 150, 166, 176, 210, 236, 260,
                  290, 320, 340, 376, 410, 450, 490, 520, 558)
SCHEMA = {
    "input": "run-benihime-qa/base/benihime-qa/<runid>/frames.jsonl",
    "optionalEnvironment": "environment.json: object with captured GPU, heap, dayTime and renderDistance metadata; copied unchanged",
    "frame": {"file": "screenshots/<name>.png", "gameTick": "finite number",
              "wallMs": "integer Unix milliseconds, chronological",
              "phase": "nonempty string", "view": "front|side|hand",
              "fpsObserved": "nonnegative integer, Minecraft reported FPS",
              "width": "positive integer", "height": "positive integer",
              "origin": ["finite x", "finite y", "finite z"],
              "source": SOURCE},
    "provenance_limit": "Origin is checked against local capture metadata; it is not cryptographic attestation.",
    "video": "Optional sampled-frame evidence montage; wallMs preserves stalls; no frame interpolation.",
}


def finite(value):
    return isinstance(value, (int, float)) and not isinstance(value, bool) and math.isfinite(value)


def integer(value, minimum=0):
    return isinstance(value, int) and not isinstance(value, bool) and value >= minimum


def load_environment(run: Path):
    environment_path = run / "environment.json"
    if not environment_path.exists():
        return None, None
    data = environment_path.read_bytes()
    environment = json.loads(data.decode("utf-8-sig"))
    if not isinstance(environment, dict):
        raise ValueError("environment.json must contain a JSON object")
    return environment, data


def load_frames(run: Path):
    frames = []
    seen = set()
    previous = -1
    metadata = run / "frames.jsonl"
    for line_number, line in enumerate(metadata.read_text(encoding="utf-8-sig").splitlines(), 1):
        if not line.strip():
            continue
        frame = json.loads(line)
        prefix = f"frames.jsonl:{line_number}"
        if not isinstance(frame, dict):
            raise ValueError(f"{prefix}: object required")
        relative = Path(frame.get("file", ""))
        image = (run / relative).resolve()
        if relative.is_absolute() or not image.is_relative_to(run) or image.suffix.lower() != ".png":
            raise ValueError(f"{prefix}: PNG must be inside the capture directory")
        if image in seen:
            raise ValueError(f"{prefix}: duplicate screenshot path; possible overwritten capture")
        seen.add(image)
        if frame.get("source") != SOURCE:
            raise ValueError(f"{prefix}: unexpected capture source")
        if not finite(frame.get("gameTick")) or frame["gameTick"] < 0:
            raise ValueError(f"{prefix}: invalid gameTick")
        if not integer(frame.get("wallMs"), 1) or frame["wallMs"] < previous:
            raise ValueError(f"{prefix}: wallMs missing or not chronological")
        previous = frame["wallMs"]
        if not isinstance(frame.get("phase"), str) or not frame["phase"]:
            raise ValueError(f"{prefix}: phase required")
        if frame.get("view") not in ("front", "side", "hand"):
            raise ValueError(f"{prefix}: unknown view")
        if not integer(frame.get("fpsObserved")):
            raise ValueError(f"{prefix}: invalid fpsObserved")
        origin = frame.get("origin")
        if not isinstance(origin, list) or len(origin) != 3 or not all(map(finite, origin)):
            raise ValueError(f"{prefix}: finite scene origin required")
        if not all(integer(frame.get(key), 1) for key in ("width", "height")):
            raise ValueError(f"{prefix}: invalid framebuffer dimensions")
        data = image.read_bytes()
        if len(data) < 33 or data[:8] != b"\x89PNG\r\n\x1a\n" or data[12:16] != b"IHDR":
            raise ValueError(f"{prefix}: invalid PNG header")
        if struct.unpack(">II", data[16:24]) != (frame["width"], frame["height"]):
            raise ValueError(f"{prefix}: PNG dimensions disagree with capture metadata")
        frames.append({**frame, "sha256": hashlib.sha256(data).hexdigest(),
                       "sizeBytes": len(data), "_path": image})
    if not frames:
        raise ValueError("No actual Minecraft frames were found")
    return frames, metadata


def package(run: Path, destination: Path, frames, metadata: Path, ffmpeg: Path | None):
    environment, environment_bytes = load_environment(run)
    # Refuse replacement of any existing evidence package.
    destination.mkdir(parents=True, exist_ok=False)
    (destination / "screenshots").mkdir()
    exported = []
    for index, frame in enumerate(frames):
        relative = f"screenshots/frame-{index:06d}.png"
        data = frame["_path"].read_bytes()
        if hashlib.sha256(data).hexdigest() != frame["sha256"]:
            raise ValueError("Source screenshot changed during packaging")
        (destination / relative).write_bytes(data)
        exported.append({key: value for key, value in frame.items() if key != "_path"})
        exported[-1]["originalFile"] = exported[-1]["file"]
        exported[-1]["file"] = relative
    (destination / "frames-original.jsonl").write_bytes(metadata.read_bytes())
    if environment_bytes is not None:
        (destination / "environment.json").write_bytes(environment_bytes)
    selections = [{"requestedTick": tick,
                   "frameIndex": min(range(len(frames)), key=lambda i: abs(frames[i]["gameTick"] - tick))}
                  for tick in CRITICAL_TICKS]
    manifest = {
        "schemaVersion": 1, "createdUtc": datetime.now(timezone.utc).isoformat(),
        "sourceDirectory": str(run), "source": SOURCE,
        "metadataSha256": hashlib.sha256(metadata.read_bytes()).hexdigest(),
        "environment": environment,
        "environmentSha256": hashlib.sha256(environment_bytes).hexdigest() if environment_bytes is not None else None,
        "pngBytesUnmodified": True, "visualApproval": "PENDING HUMAN REVIEW",
        "provenanceLimit": SCHEMA["provenance_limit"],
        "frameCount": len(exported), "frames": exported, "criticalSelections": selections,
        "video": {"status": "not requested"},
    }
    if ffmpeg:
        deltas = [(b["wallMs"] - a["wallMs"]) / 1000 for a, b in zip(frames, frames[1:])]
        if not deltas or any(delta <= 0 for delta in deltas):
            raise ValueError("Video requires at least two frames with strictly increasing wallMs")
        if len({(f["width"], f["height"]) for f in frames}) != 1:
            raise ValueError("Video requires a consistent framebuffer resolution")
        tail = statistics.median(deltas)
        concat = []
        for frame, duration in zip(exported, deltas + [tail]):
            concat.extend([f"file '{frame['file']}'", "option framerate 1000", f"duration {duration:.6f}"])
        concat.extend([f"file '{exported[-1]['file']}'", "option framerate 1000"])
        (destination / "frames-concat.txt").write_text("\n".join(concat) + "\n", encoding="utf-8")
        command = [str(ffmpeg), "-hide_banner", "-nostdin", "-n", "-f", "concat", "-safe", "0",
                   "-i", "frames-concat.txt", "-an", "-c:v", "libx264", "-pix_fmt", "yuv420p",
                   "-vf", "pad=ceil(iw/2)*2:ceil(ih/2)*2", "-fps_mode", "vfr",
                   "-enc_time_base", "1:1000", "-video_track_timescale", "1000",
                   "-movflags", "+faststart", "minecraft-sampled-evidence.mp4"]
        result = subprocess.run(command, cwd=destination, capture_output=True, text=True)
        (destination / "ffmpeg.log").write_text(result.stdout + result.stderr, encoding="utf-8")
        manifest["video"] = {"status": "created" if result.returncode == 0 else "failed",
                             "exitCode": result.returncode, "command": command,
                             "timing": "Actual wallMs intervals; VFR; no motion interpolation",
                             "finalHoldSeconds": tail, "finalHoldMethod": "median captured interval",
                             "limitation": "Sampled screenshots do not measure full render FPS or prove smoothness"}
    (destination / "manifest.json").write_text(json.dumps(manifest, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    gallery = []
    unique = sorted({selection["frameIndex"] for selection in selections})
    for index in unique:
        frame = exported[index]
        caption = f"Tick {frame['gameTick']:.1f} · {frame['phase']} · {frame['view']} · reported FPS {frame['fpsObserved']}"
        gallery.append(f'<figure><a href="{frame["file"]}"><img loading="lazy" src="{frame["file"]}" alt="{html.escape(caption, quote=True)}"></a><figcaption>{html.escape(caption)}</figcaption></figure>')
    video = ('<video controls preload="metadata" src="minecraft-sampled-evidence.mp4"></video>'
             if manifest["video"]["status"] == "created" else "")
    page = ('<!doctype html><html lang="pt-BR"><meta charset="utf-8"><meta name="viewport" content="width=device-width">'
            '<title>Benihime — evidência Minecraft</title><style>body{background:#111;color:#ddd;font:16px sans-serif;margin:24px}'
            'main{display:grid;grid-template-columns:repeat(auto-fit,minmax(320px,1fr));gap:16px}figure{margin:0}img,video{width:100%}figcaption{padding:8px}</style>'
            '<h1>Capturas reais do cliente Minecraft</h1><p>Originais PNG copiados sem alterações. Aprovação visual pendente. '
            'A procedência foi conferida nos metadados locais; não é uma certificação criptográfica.</p>'
            '<p>O vídeo opcional é uma sequência de capturas com os intervalos reais de wallMs, incluindo pausas. '
            'Não interpola movimentos nem comprova a fluidez de todos os quadros renderizados.</p>'
            '<p><a href="manifest.json">Manifesto e hashes</a> · <a href="frames-original.jsonl">Metadados originais</a></p>'
            + video + '<main>' + ''.join(gallery) + '</main></html>')
    (destination / "index.html").write_text(page, encoding="utf-8")
    return manifest


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("run", nargs="?", type=Path, help="Actual Minecraft capture directory containing frames.jsonl")
    parser.add_argument("--schema", action="store_true", help="Print input schema; no files created")
    parser.add_argument("--validate-only", action="store_true", help="Validate captures without exporting")
    parser.add_argument("--output-root", type=Path, default=Path(__file__).resolve().parents[2] / "Docs/poderes/qa/benihime/cycle68")
    parser.add_argument("--ffmpeg", type=Path, help="Optional explicit FFmpeg executable path for wall-time montage")
    args = parser.parse_args()
    if args.schema:
        print(json.dumps(SCHEMA, indent=2, ensure_ascii=False))
        return 0
    if not args.run:
        parser.error("run directory is required unless --schema is used")
    run = args.run.resolve(strict=True)
    load_environment(run)
    frames, metadata = load_frames(run)
    if args.validate_only:
        print(json.dumps({"status": "metadata and PNG headers valid", "frames": len(frames)}))
        return 0
    ffmpeg = args.ffmpeg.resolve(strict=True) if args.ffmpeg else None
    destination = args.output_root.resolve() / run.name
    result = package(run, destination, frames, metadata, ffmpeg)
    print(json.dumps({"destination": str(destination), "frames": len(frames), "video": result["video"]["status"]}))
    return 1 if result["video"]["status"] == "failed" else 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except (OSError, ValueError, KeyError, TypeError) as failure:
        print(f"Evidence packaging failed: {failure}", file=sys.stderr)
        sys.exit(1)
