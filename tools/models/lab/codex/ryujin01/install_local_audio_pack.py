"""Install supplied ElevenLabs SFX into an ignored, personal-use resource pack.

The source license does not establish GPL-compatible redistribution rights.
The generated pack lives under run/ and is never added to the mod jar.
"""
import hashlib
import json
from pathlib import Path
import shutil

REPO = Path(__file__).resolve().parents[5]
SOURCE = Path.home() / "Downloads"
TARGETS = [REPO / "run/resourcepacks/RyujinV7LocalAudio",
           REPO / "run-benihime-qa/ryujin-v7/resourcepacks/RyujinV7LocalAudio"]
FILES = {
    "preheat": "preheat.ogg",
    "flame_explosion": "flame_explosion.ogg",
    "solar_roar_loop": "solar_roar_loop.ogg",
    "flame_absorption": "flame_absorption.ogg",
    "blade_seal": "blade_seal.ogg",
}


def main():
    manifest = json.loads((SOURCE / "MANIFEST.json").read_text(encoding="utf-8"))
    expected = {entry["file"]: entry["sha256"] for entry in manifest["files"]}
    sounds = json.loads((REPO / "src/main/resources/assets/bleachmod/sounds.json").read_text(encoding="utf-8"))
    for event, filename in FILES.items():
        data = (SOURCE / filename).read_bytes()
        assert hashlib.sha256(data).hexdigest() == expected[filename], filename
        sounds["effect.ryujin_v7_" + event] = {
            "sounds": [{"name": "bleachmod:ryujin_v7_" + event, "stream": False}]
        }
    for target in TARGETS:
        dest = target / "assets/bleachmod/sounds"
        dest.mkdir(parents=True, exist_ok=True)
        for filename in FILES.values():
            shutil.copyfile(SOURCE / filename, dest / ("ryujin_v7_" + filename))
        (target / "assets/bleachmod/sounds.json").write_text(
            json.dumps(sounds, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        (target / "pack.mcmeta").write_text(json.dumps({"pack": {
            "pack_format": 15,
            "description": "Ryujin v7 audio - local personal-use test only; ElevenLabs via Manus"
        }}, indent=2) + "\n", encoding="utf-8")
        (target / "LICENSE_AND_SOURCE.txt").write_bytes((SOURCE / "LICENSE_AND_SOURCE.txt").read_bytes())
        print(target)
    qa_options = TARGETS[1].parents[1] / "options.txt"
    if not qa_options.exists():
        qa_options.write_text('resourcePacks:["file/RyujinV7LocalAudio"]\n', encoding="utf-8")


if __name__ == "__main__":
    main()
