"""Build the local Ryujin audio packs from the supplied Manus ZIP."""

import argparse
import hashlib
import io
import json
from pathlib import Path, PurePosixPath
from zipfile import ZipFile, ZipInfo


LEGACY_EFFECTS = ("preheat", "flame_explosion", "solar_roar_loop", "flame_absorption", "blade_seal")
NEW_EFFECTS = ("flame_spread", "ember_crackle", "seal_ash")
NAMESPACE = "assets/bleachmod/"


def sha(data):
    return hashlib.sha256(data).hexdigest()


def json_bytes(data):
    return (json.dumps(data, indent=2, ensure_ascii=False) + "\n").encode("utf-8")


def verified_sources(source):
    with ZipFile(source) as archive:
        manifest = json.loads(archive.read("final_15s/MANIFEST.json"))
        verified = {}
        for entry in manifest["files"]:
            name = entry["file"]
            if PurePosixPath(name).name != name or name in verified:
                raise ValueError(f"Invalid or duplicated manifest entry: {name}")
            member = archive.getinfo(f"final_15s/{name}")
            if member.file_size != entry["size_bytes"] or member.file_size > 8_000_000:
                raise ValueError(f"Invalid source size: {name}")
            data = archive.read(member)
            if sha(data) != entry["sha256"]:
                raise ValueError(f"Invalid source hash: {name}")
            verified[name] = data
    return verified


def write_pack(destination, description, files, source_names, sources, source_zip):
    content = {**files, "pack.mcmeta": json_bytes({
        "pack": {"pack_format": 15, "description": description}
    })}
    content["provenance.json"] = json_bytes({
        "source_zip_sha256": source_zip,
        "usage": "Personal local testing. Distribution terms for generated media are unresolved.",
        "audio": {path: {"file": name, "sha256": sha(sources[name])}
                  for path, name in sorted(source_names.items())},
    })
    buffer = io.BytesIO()
    with ZipFile(buffer, "w") as archive:
        for path, data in sorted(content.items()):
            archive.writestr(ZipInfo(path), data)
    result = buffer.getvalue()
    if destination.exists():
        if destination.read_bytes() != result:
            raise FileExistsError(f"Pack already exists with different content: {destination}")
    else:
        with destination.open("xb") as out:
            out.write(result)
    print(f"{destination}  {sha(result)}")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("package", type=Path)
    parser.add_argument("output", type=Path)
    args = parser.parse_args()
    sources = verified_sources(args.package)
    source_zip = sha(args.package.read_bytes())
    audio_a = {
        f"{NAMESPACE}sounds/ryujin_voice_bankai.ogg": "voice_bankai_clean.ogg",
        f"{NAMESPACE}sounds/ryujin_voice_name.ogg": "voice_zanka_no_tachi_clean.ogg",
        f"{NAMESPACE}sounds/ryujin_entrance_score.ogg": "music_A_contained_15s.ogg",
    }
    sounds = {}
    for version, effects in (("v7", LEGACY_EFFECTS), ("v15", NEW_EFFECTS)):
        for effect in effects:
            key = f"ryujin_{version}_{effect}"
            audio_a[f"{NAMESPACE}sounds/{key}.ogg"] = f"{effect}.ogg"
            sounds[f"effect.{key}"] = {
                "replace": True,
                "sounds": [{"name": f"bleachmod:{key}", "stream": effect == "solar_roar_loop"}],
            }
    audio_b = {f"{NAMESPACE}sounds/ryujin_entrance_score.ogg": "music_B_heavy_15s.ogg"}
    assets_a = {path: sources[name] for path, name in audio_a.items()}
    assets_a[f"{NAMESPACE}sounds.json"] = json_bytes(sounds)
    assets_a[f"{NAMESPACE}ryujin_audio_15s.json"] = json_bytes({"mix": "manus-15s"})
    assets_b = {path: sources[name] for path, name in audio_b.items()}
    args.output.mkdir(parents=True, exist_ok=True)
    write_pack(args.output / "RyujinAudio15Local.zip", "Ryujin 15 s · mix A · teste pessoal",
               assets_a, audio_a, sources, source_zip)
    write_pack(args.output / "RyujinAudio15HeavyMusicLocal.zip", "Ryujin 15 s · musica B · sobrepor ao mix A",
               assets_b, audio_b, sources, source_zip)


if __name__ == "__main__":
    main()
