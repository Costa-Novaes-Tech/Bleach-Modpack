"""Prepare isolated production Forge clients; never touches a personal launcher/save.

Dependencies live in build/visual-lab/python-deps. Downloads are pinned by
Modrinth version ID and checked against the publisher's SHA-512 metadata.
The test identity is for local development only, with no account credentials.
"""
from __future__ import annotations
import argparse, hashlib, json, shutil, subprocess, sys, urllib.parse, urllib.request, zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
LAB = ROOT / "build/visual-lab"
sys.path.insert(0, str(LAB / "python-deps"))
JAVA = Path("C:/Program Files/Eclipse Adoptium/jdk-17.0.20.101-hotspot/bin/java.exe")
PINNED = {
    "embeddium": "UTbfe5d1", "oculus": "iQ1SwGc3",
    "geckolib": "aC5KMoNg", "photon": "a7chTVuf", "ldlib": "ONxYJpRz",
    "playeranimator": "xe2EVE6q", "nea": "3YRHi2mE", "shimmer": "BeAfFYQQ",
}
SELECT = {
    "better-combat": ("better-combat", "1.9.0+1.20.1"),
    "cloth-config": ("cloth-config", "11.1.136+forge"),
    "skinlayers3d": ("3dskinlayers", "1.11.4"),
    "aaa": ("aaa-particles", "2.3.4"),
    "complementary": ("complementary-reimagined", None),
}
PROFILES = {
    "base": [], "render": ["embeddium"], "shimmer": ["shimmer"],
    "shimmer-render": ["shimmer","embeddium"],
    "shimmer-oculus": ["shimmer","embeddium","oculus"],
    "shimmer-shader": ["shimmer","embeddium","oculus","complementary"],
    "oculus-off": ["embeddium", "oculus"],
    "shader": ["embeddium", "oculus", "complementary"],
    "photon": ["photon", "ldlib"], "aaa": ["aaa"],
    "skin": ["skinlayers3d"], "nea": ["nea"],
    "cosmetics": ["skinlayers3d", "nea"],
}
CORE = ["geckolib", "better-combat", "playeranimator", "cloth-config"]

def api(endpoint):
    req = urllib.request.Request("https://api.modrinth.com/v2/" + endpoint,
                                 headers={"User-Agent": "BleachMod-LocalVisualLab/1.0"})
    with urllib.request.urlopen(req, timeout=60) as response:
        return json.load(response)

def fetch(key):
    if key == "aaa":
        url = "https://mediafilez.forgecdn.net/files/9239/318/aaa_particles-forge-1.20.1-2.3.4.jar"
        dest = LAB / "downloads/aaa_particles-forge-1.20.1-2.3.4.jar"
        dest.parent.mkdir(parents=True, exist_ok=True)
        if not dest.exists():
            with urllib.request.urlopen(url, timeout=120) as response:
                dest.write_bytes(response.read())
        digest = hashlib.sha256(dest.read_bytes()).hexdigest()
        if digest != "6dbb0f581a86a96690f9c03f19bf9ceaa090d272136cae4e4b6cef8f5b47b453":
            raise RuntimeError("AAA differs from the first official CDN download")
        return {"key": key, "version": "2.3.4", "path": str(dest.relative_to(ROOT)), "url": url,
                "source_page": "https://www.curseforge.com/minecraft/mc-mods/aaa-particles/files/9239318",
                "sha256": digest, "hash_note": "Local reproducibility digest, not an independent publisher digest",
                "dependencies": []}
    if key in PINNED:
        version = api("version/" + PINNED[key])
    else:
        slug, wanted = SELECT[key]
        params = {"game_versions": json.dumps(["1.20.1"])}
        if key != "complementary":
            params["loaders"] = json.dumps(["forge"])
        candidates = api("project/" + slug + "/version?" + urllib.parse.urlencode(params))
        candidates = [v for v in candidates if v["version_type"] in ("release", "beta")
                      and (wanted is None or wanted in v["version_number"])]
        if not candidates:
            raise RuntimeError("No matching official Forge release for " + key)
        version = candidates[0]
    if key != "complementary" and ("forge" not in version["loaders"] or "1.20.1" not in version["game_versions"]):
        raise RuntimeError("Wrong loader/game version: " + key)
    file = next((f for f in version["files"] if f["primary"]), version["files"][0])
    dest = LAB / "downloads" / file["filename"]
    dest.parent.mkdir(parents=True, exist_ok=True)
    if not dest.exists() or hashlib.sha512(dest.read_bytes()).hexdigest() != file["hashes"]["sha512"]:
        with urllib.request.urlopen(file["url"], timeout=120) as response:
            data = response.read()
        if hashlib.sha512(data).hexdigest() != file["hashes"]["sha512"]:
            raise RuntimeError("Publisher hash mismatch: " + key)
        dest.write_bytes(data)
    metadata = {"key": key, "version_id": version["id"], "version": version["version_number"],
                "path": str(dest.relative_to(ROOT)), "url": file["url"],
                "sha512": file["hashes"]["sha512"], "sha256": hashlib.sha256(dest.read_bytes()).hexdigest(),
                "dependencies": version["dependencies"]}
    with zipfile.ZipFile(dest) as zf:
        if "META-INF/mods.toml" in zf.namelist():
            (LAB / "downloads" / (key + "-mods.toml")).write_bytes(zf.read("META-INF/mods.toml"))
    return metadata

def download_mods():
    manifest = {}
    for key in [*PINNED, *SELECT]:
        try:
            manifest[key] = fetch(key)
            print(key, manifest[key]["version"], "verified", flush=True)
        except Exception as exc:
            manifest[key] = {"key": key, "status": "pending", "reason": str(exc)}
            print(key, "PENDING", str(exc), flush=True)
        (LAB / "artifacts.json").write_text(json.dumps(manifest, indent=2), encoding="utf-8")

def install_runtime():
    import minecraft_launcher_lib as mll
    runtime = LAB / "minecraft"
    runtime.mkdir(parents=True, exist_ok=True)
    version = "1.20.1-forge-47.4.10"
    if not (runtime / "versions" / version / (version + ".json")).exists():
        mll.forge.install_forge_version("1.20.1-47.4.10", runtime,
            callback={"setStatus": lambda value: print(value, flush=True)}, java=JAVA)
    print("Runtime ready", version, flush=True)

def profile(name):
    manifest = json.loads((LAB / "artifacts.json").read_text(encoding="utf-8"))
    folder = LAB / "profiles" / name
    folder.mkdir(parents=True, exist_ok=True)
    installed = []
    for key in CORE + PROFILES[name]:
        entry = manifest[key]
        if entry.get("status") == "pending":
            raise RuntimeError("Cannot prepare profile with unresolved dependency: " + key)
        category = "shaderpacks" if key == "complementary" else "mods"
        dest = folder / category / Path(entry["path"]).name
        dest.parent.mkdir(exist_ok=True)
        shutil.copy2(ROOT / entry["path"], dest)
        installed.append(entry)
    jar = ROOT / "build/libs/bleachmod-0.2.0.jar"
    shutil.copy2(jar, folder / "mods" / jar.name)
    options = folder / "options.txt"
    if not options.exists():
        options.write_text("enableVsync:false\nmaxFps:120\nrenderDistance:6\nsimulationDistance:5\n"
                           "pauseOnLostFocus:false\nguiScale:2\nlang:pt_br\n", encoding="utf-8")
    if name in ("shader", "oculus-off", "shimmer-oculus", "shimmer-shader"):
        config = folder / "config/oculus.properties"
        config.parent.mkdir(exist_ok=True)
        shader = next((e for e in installed if e["key"] == "complementary"), None)
        config.write_text("enableShaders=" + ("true" if shader else "false") + "\n" +
                          ("shaderPack=" + Path(shader["path"]).name + "\n" if shader else ""), encoding="utf-8")
    (folder / "profile-manifest.json").write_text(json.dumps({"name": name,
        "core_sha256": hashlib.sha256(jar.read_bytes()).hexdigest(), "artifacts": installed}, indent=2), encoding="utf-8")
    return folder

def launch(name):
    import minecraft_launcher_lib as mll
    folder = profile(name)
    options = mll.utils.generate_test_options()
    options.update({"username": "VisualLabQA", "uuid": "91854967b654458899875257ec7292bd",
        "gameDirectory": str(folder), "executablePath": str(JAVA), "customResolution": True,
        "resolutionWidth": "1280", "resolutionHeight": "720", "launcherName": "BleachVisualLab",
        "jvmArguments": ["-Xms512M", "-Xmx4G"]})
    if (folder / "saves/Visual-Lab-Flat-20261105/level.dat").exists():
        options["quickPlaySingleplayer"] = "Visual-Lab-Flat-20261105"
    elif (folder / "saves/Visual-Lab-20261105/level.dat").exists():
        options["quickPlaySingleplayer"] = "Visual-Lab-20261105"
    command = mll.command.get_minecraft_command("1.20.1-forge-47.4.10", LAB / "minecraft", options)
    with (folder / "console.log").open("w", encoding="utf-8") as log:
        process = subprocess.Popen(command, cwd=folder, stdout=log, stderr=subprocess.STDOUT,
                                   creationflags=subprocess.CREATE_NO_WINDOW)
    (folder / "process.json").write_text(json.dumps({"pid": process.pid, "profile": name}), encoding="utf-8")
    print("Launched", name, "PID", process.pid, flush=True)

def fixture(name):
    import nbtlib as n
    folder = profile(name)
    dest = folder / "saves/Visual-Lab-Flat-20261105"
    if dest.exists():
        raise RuntimeError("Fixture already exists; preserve it and select another profile")
    source = LAB / "profiles/base/saves/Visual-Lab-20261105"
    dest.mkdir(parents=True)
    level = n.load(source / "level.dat")
    data = level["Data"]
    data["LevelName"] = n.String("Visual-Lab-Flat-20261105")
    data["SpawnX"], data["SpawnY"], data["SpawnZ"] = n.Int(0), n.Int(64), n.Int(0)
    data["GameType"], data["allowCommands"] = n.Int(1), n.Byte(1)
    data["WorldGenSettings"]["dimensions"]["minecraft:overworld"]["generator"] = n.Compound({
        "type": n.String("minecraft:flat"), "settings": n.Compound({
            "biome": n.String("minecraft:plains"), "features": n.Byte(0), "lakes": n.Byte(0),
            "layers": n.List[n.Compound]([
                n.Compound({"height": n.Int(1), "block": n.String("minecraft:bedrock")}),
                n.Compound({"height": n.Int(63), "block": n.String("minecraft:stone")})]),
            "structure_overrides": n.List[n.String]([])})})
    data["DataPacks"]["Enabled"].append(n.String("file/visual_lab"))
    if "Player" in data:
        data["Player"]["Pos"] = n.List[n.Double]([0.5, 65.0, 0.5])
    level.save(dest / "level.dat")
    pack = dest / "datapacks/visual_lab"
    (pack / "data/minecraft/tags/functions").mkdir(parents=True)
    (pack / "data/visual_lab/functions").mkdir(parents=True)
    (pack / "pack.mcmeta").write_text('{"pack":{"pack_format":15,"description":"Disposable local QA"}}')
    (pack / "data/minecraft/tags/functions/tick.json").write_text('{"values":["visual_lab:tick"]}')
    (pack / "data/visual_lab/functions/tick.mcfunction").write_text(
        'execute as @a[tag=!visual_lab_ready] at @s run function visual_lab:setup\n')
    setup = source / "datapacks/visual_lab/data/visual_lab/functions/setup.mcfunction"
    (pack / "data/visual_lab/functions/setup.mcfunction").write_text(
        setup.read_text() + '\ntag @s add visual_lab_ready\n', encoding="utf-8")
    print("Created new flat fixture", dest)

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("action", choices=["download", "install-runtime", "profile", "fixture", "launch"])
    parser.add_argument("--profile", choices=PROFILES, default="base")
    args = parser.parse_args()
    LAB.mkdir(parents=True, exist_ok=True)
    if args.action == "download": download_mods()
    elif args.action == "install-runtime": install_runtime()
    elif args.action == "profile": print(profile(args.profile))
    elif args.action == "fixture": fixture(args.profile)
    elif args.action == "launch": launch(args.profile)

if __name__ == "__main__":
    main()
