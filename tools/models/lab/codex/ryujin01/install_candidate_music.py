"""Install the user's 12-second Ryujin score as an isolated local preview pack.

The pack overrides only the existing music sound file. It does not modify the
mod jar, the existing SFX pack, or the user's normal run/options.txt.
"""

from __future__ import annotations

import hashlib
import json
import re
import shutil
import subprocess
import tempfile
from pathlib import Path


REPO = Path(__file__).resolve().parents[5]
SOURCE = Path.home() / "Downloads/ryujin_jakka_bankai_music_12s_final.wav"
EXPECTED_SOURCE_SHA256 = "e647a0f503aac2c1959a872086cb8ec99c35975ebef4fa8d6a49efff981bcb90"
PACK_NAME = "RyujinCandidateMusicLocal"
PACKS = [
    REPO / "run/resourcepacks" / PACK_NAME,
    REPO / "run-benihime-qa/ryujin-v7/resourcepacks" / PACK_NAME,
]


def sha256(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def ffmpeg_exe() -> str:
    try:
        import imageio_ffmpeg

        return imageio_ffmpeg.get_ffmpeg_exe()
    except (ImportError, RuntimeError):
        path = shutil.which("ffmpeg")
        if path is None:
            raise RuntimeError("FFmpeg is required to encode the local OGG preview")
        return path


def enable_only_in_qa_options() -> None:
    options = REPO / "run-benihime-qa/ryujin-v7/options.txt"
    if not options.is_file():
        raise FileNotFoundError(f"QA options not found: {options}")
    lines = options.read_text(encoding="utf-8").splitlines()
    entry = f"file/{PACK_NAME}"
    for index, line in enumerate(lines):
        if line.startswith("resourcePacks:"):
            packs = json.loads(line.split(":", 1)[1])
            if entry not in packs:
                packs.append(entry)
                lines[index] = "resourcePacks:" + json.dumps(packs, separators=(",", ":"))
                options.write_text("\n".join(lines) + "\n", encoding="utf-8")
            return
    lines.append("resourcePacks:" + json.dumps([entry]))
    options.write_text("\n".join(lines) + "\n", encoding="utf-8")


def main() -> None:
    source_data = SOURCE.read_bytes()
    actual_sha = sha256(source_data)
    if actual_sha != EXPECTED_SOURCE_SHA256:
        raise RuntimeError(f"Source WAV changed; expected {EXPECTED_SOURCE_SHA256}, got {actual_sha}")
    ffmpeg = ffmpeg_exe()
    qa_root = REPO / "run-benihime-qa/ryujin-v7"
    with tempfile.TemporaryDirectory(prefix="ryujin-music-", dir=qa_root) as temporary:
        encoded = Path(temporary) / "ryujin_entrance_score.ogg"
        subprocess.run([
            ffmpeg, "-nostdin", "-hide_banner", "-loglevel", "error", "-i", str(SOURCE),
            "-map_metadata", "-1", "-vn", "-c:a", "libvorbis", "-q:a", "5",
            "-ar", "48000", "-ac", "2", "-y", str(encoded),
        ], check=True)
        sound_data = encoded.read_bytes()
        probe = subprocess.run([ffmpeg, "-nostdin", "-hide_banner", "-i", str(encoded)],
                               capture_output=True, text=True)
        if not re.search(r"Duration: 00:00:12\.00", probe.stderr) or "Audio: vorbis, 48000 Hz, stereo" not in probe.stderr:
            raise RuntimeError("Encoded candidate must be 12.00 s, stereo 48 kHz Vorbis")

    provenance = {
        "source": str(SOURCE),
        "source_sha256": actual_sha,
        "ogg_sha256": sha256(sound_data),
        "author_statement": "User states this music was composed by them and can be licensed to the mod",
        "status": "Local preview only; distribution license decision pending listening",
    }
    for pack in PACKS:
        sound_path = pack / "assets/bleachmod/sounds/ryujin_entrance_score.ogg"
        sound_path.parent.mkdir(parents=True, exist_ok=True)
        sound_path.write_bytes(sound_data)
        (pack / "pack.mcmeta").write_text(json.dumps({"pack": {
            "pack_format": 15,
            "description": "Ryujin 12-second candidate music - local listening preview"
        }}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        (pack / "provenance.json").write_text(json.dumps(provenance, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(pack)
    enable_only_in_qa_options()
    print(json.dumps(provenance, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
