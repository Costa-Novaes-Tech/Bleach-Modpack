"""Build a pitch-preserving 15 s music preview in one disposable QA profile.

Preserves the source WAV and previous 12 s packs. Does not install into run,
change SFX/voices or grant a distribution license.
"""
from __future__ import annotations
import argparse
import json
import re
import subprocess
import tempfile
from pathlib import Path
from install_candidate_music import REPO, SOURCE, EXPECTED_SOURCE_SHA256, ffmpeg_exe, sha256


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--profile", default="music15")
    args = parser.parse_args()
    if not re.fullmatch(r"[a-z0-9-]{1,40}", args.profile):
        parser.error("Invalid isolated QA profile name")
    if sha256(SOURCE.read_bytes()) != EXPECTED_SOURCE_SHA256:
        raise RuntimeError("Source music changed; preserve and audit it first")
    profile = REPO / ("run-benihime-qa/visual-" + args.profile)
    options = profile / "options.txt"
    if not options.exists():
        raise FileNotFoundError("Prepare this disposable profile's options first")
    pack_name = "RyujinCandidateMusic15Local"
    pack = profile / "resourcepacks" / pack_name
    sound = pack / "assets/bleachmod/sounds/ryujin_entrance_score.ogg"
    sound.parent.mkdir(parents=True, exist_ok=True)
    if sound.exists():
        raise FileExistsError("Preserve the existing preview; choose a new QA profile")
    filters = "atempo=0.8,apad,atrim=duration=15,afade=t=in:d=0.08,afade=t=out:st=14.4:d=0.6"
    with tempfile.TemporaryDirectory(prefix="music15-", dir=profile) as temporary:
        out = Path(temporary) / "candidate.ogg"
        subprocess.run([ffmpeg_exe(), "-nostdin", "-hide_banner", "-loglevel", "error",
                        "-i", str(SOURCE), "-map_metadata", "-1", "-vn", "-af", filters,
                        "-c:a", "libvorbis", "-q:a", "5", "-ar", "48000", "-ac", "2", str(out)], check=True)
        sound.write_bytes(out.read_bytes())
    (pack / "pack.mcmeta").write_text(json.dumps({"pack": {"pack_format": 15,
        "description": "Ryujin 15 s time-stretched music - local artistic A/B only"}}, indent=2))
    manifest = {"source_sha256": EXPECTED_SOURCE_SHA256, "output_sha256": sha256(sound.read_bytes()),
                "filter": filters, "duration_seconds": 15, "sample_rate": 48000,
                "author_statement": "User composed it and can license it to the mod",
                "distribution_license": "Undecided; local QA only", "artistic_acceptance": "Pending listening"}
    (pack / "provenance.json").write_text(json.dumps(manifest, indent=2))
    lines = options.read_text().splitlines()
    for i, line in enumerate(lines):
        if line.startswith("resourcePacks:"):
            packs = json.loads(line.split(":", 1)[1])
            packs = [p for p in packs if p != "file/RyujinCandidateMusicLocal"]
            packs.append("file/" + pack_name)
            lines[i] = "resourcePacks:" + json.dumps(packs)
            break
    else:
        lines.append("resourcePacks:" + json.dumps(["file/" + pack_name]))
    options.write_text("\n".join(lines) + "\n")
    print(sound)
    print(json.dumps(manifest, indent=2))


if __name__ == "__main__":
    main()
