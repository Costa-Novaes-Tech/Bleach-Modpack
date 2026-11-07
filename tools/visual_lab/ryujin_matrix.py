"""Run a packaged Ryujin fixture with pinned mods and local audiovisual packs."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import time
from zipfile import ZipFile

from prepare_lab import CORE, JAVA, LAB, PROFILES, ROOT
import minecraft_launcher_lib as mll

CONTROL = ROOT / "run-benihime-qa/visual-v15-flame-finish-final-b"
VERSION = "1.20.1-forge-47.4.10"


def shader_settings(shader_zip, preset):
    with ZipFile(shader_zip) as archive:
        props = archive.read("shaders/shaders.properties").decode("utf-8")
    profiles = dict(re.findall(r"(?m)^\s*profile\.(\w+)\s*=([^\r\n]+)", props))
    settings = {}

    def apply(name):
        for token in profiles[name].split():
            if token.startswith("profile."):
                apply(token.removeprefix("profile."))
            elif "=" in token:
                key, value = token.split("=", 1)
                settings[key] = value
            elif token.startswith("!"):
                settings[token[1:]] = "false"
            else:
                settings[token] = "true"

    apply(preset)
    return "".join(f"{key}={value}\n" for key, value in settings.items())


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("label")
    parser.add_argument("--profile", choices=PROFILES, default="base")
    parser.add_argument("--case", type=int, choices=range(-1, 15), default=1)
    parser.add_argument("--detail", choices=["LOW", "MEDIUM", "HIGH"], default="HIGH")
    parser.add_argument("--solar", choices=["control", "bands", "full", "continuous"], default="full")
    jars = parser.add_mutually_exclusive_group()
    jars.add_argument("--latest-jar", action="store_true", help="Use the standard build JAR instead of the matrix snapshot")
    jars.add_argument("--jar", type=Path, help="Explicit candidate JAR under build/libs")
    parser.add_argument("--shader-zip", type=Path, help="Alternative shader ZIP from the lab downloads folder")
    parser.add_argument("--preset", choices=["LOW", "POTATO"], default="LOW")
    parser.add_argument("--solar-tonemap", action="store_true", help="Use the softer Complementary exposure candidate")
    parser.add_argument("--repeats", type=int, choices=range(1, 6), default=1)
    for switch in ("no-smoke", "no-screens", "reload", "music-probe", "mute-music", "light-off", "light-probe",
                   "record", "wave", "bloom", "heat", "shader-composite"):
        parser.add_argument("--" + switch, action="store_true")
    args = parser.parse_args()
    if not re.fullmatch(r"v16-[a-z0-9-]{1,44}", args.label):
        parser.error("Use a new label beginning with v16-")
    folder = LAB / "run-benihime-qa" / args.label
    if folder.exists():
        parser.error("QA run already exists")
    mods = json.loads((LAB / "artifacts.json").read_text(encoding="utf-8"))
    folder.mkdir(parents=True)
    installed = []
    shader = None
    for key in [*CORE, *PROFILES[args.profile]]:
        entry = mods[key]
        source = ROOT / entry["path"]
        if key == "complementary" and args.shader_zip:
            source = args.shader_zip.resolve()
            source.relative_to((LAB / "downloads").resolve())
        digest = hashlib.sha256(source.read_bytes()).hexdigest()
        if not (key == "complementary" and args.shader_zip) and digest != entry["sha256"]:
            raise ValueError(f"Artifact changed: {key}")
        if key == "complementary":
            shader = source.name
        dest = folder / ("shaderpacks" if key == "complementary" else "mods") / source.name
        dest.parent.mkdir(exist_ok=True)
        shutil.copy2(source, dest)
        installed.append({"key": key, "name": dest.name, "sha256": digest})
    release = LAB / "v16-release-matrix.jar"
    if args.jar:
        release = args.jar.resolve()
        release.relative_to((ROOT / "build/libs").resolve())
    elif args.latest_jar or not release.exists():
        release = ROOT / "build/libs/bleachmod-0.2.0.jar"
    shutil.copy2(release, folder / "mods/bleachmod-0.2.0.jar")
    if "oculus" in PROFILES[args.profile]:
        (folder / "config").mkdir()
        (folder / "config/oculus.properties").write_text(
            f"enableShaders={str(bool(shader)).lower()}\n" + (f"shaderPack={shader}\n" if shader else ""))
    if shader:
        shader_dir = folder / "shaderpacks"
        settings = shader_settings(shader_dir / shader,args.preset)
        if args.solar_tonemap:
            settings += "BLOOM_STRENGTH=0.036\nT_EXPOSURE=0.95\n"
        (shader_dir / (shader + ".txt")).write_text(settings)
    resources = folder / "resourcepacks"
    shutil.copytree(CONTROL / "resourcepacks", resources)
    names = ["RyujinSunV13Local", "RyujinAudio15Local.zip", "RyujinAudio15HeavyMusicLocal.zip"]
    if args.solar != "control":
        name = "RyujinSunV16" + ("Full" if args.solar == "continuous" else args.solar.title())
        shutil.copytree(LAB / "ryujin-v16-packs" / name, resources / name)
        names.append(name)
    (folder / "options.txt").write_text("enableVsync:false\nmaxFps:120\nrenderDistance:6\nsimulationDistance:5\n"
        "pauseOnLostFocus:false\nguiScale:2\nfov:0.0\n"
        f"soundCategory_music:{0.0 if args.mute_music else 1.0}\n"
        f"resourcePacks:{json.dumps(['file/'+n for n in names])}\n", encoding="utf-8")
    flags = {
        "packagedRyujinQa": "true", "ryujinBankaiQa": "true", "benihimeQa": "true",
        "cinematicQaFast": "true", "qaAutoExit": "true", "ryujinV14Pilot": "true",
        "ryujinBankaiQaCase": args.case, "ryujinQaRepeats": args.repeats, "ryujinFireDetail": args.detail,
        "qaNoScreenshots": args.no_screens, "ryujinQaNoBackdropSmoke": args.no_smoke,
        "ryujinQaMusicProbe": args.music_probe, "ryujinQaReload": args.reload,
        "ryujinLightOff": args.light_off, "ryujinLightProbe": args.light_probe, "ryujinQaVideo": args.record,
        "ryujinContinuousSolar": args.solar == "continuous",
        "ryujinSolarBloom": args.bloom, "ryujinHeatRefraction": args.heat,
        "ryujinShaderComposite": args.shader_composite,
    }
    env = os.environ.copy()
    if args.record:
        from imageio_ffmpeg import get_ffmpeg_exe
        flags["qaFfmpeg"] = get_ffmpeg_exe()
    if args.wave:
        env.update(ALSOFT_DRIVERS="wave", ALSOFT_WAVE_FILE=str(folder / "engine.wav"))
    jvm = ["-Xms512M", "-Xmx4G", *(f"-Dbleachmod.{k}={str(v).lower() if isinstance(v,bool) else v}" for k, v in flags.items())]
    options = mll.utils.generate_test_options()
    options.update({"username":"VisualLabQA", "uuid":"91854967b654458899875257ec7292bd",
        "gameDirectory":str(folder), "executablePath":str(JAVA), "customResolution":True,
        "resolutionWidth":"1280", "resolutionHeight":"720", "launcherName":"BleachVisualLab", "jvmArguments":jvm})
    command = mll.command.get_minecraft_command(VERSION, LAB / "minecraft", options)
    manifest = {"profile": args.profile, "case": args.case, "flags": flags, "solar": args.solar,
                "shader_preset": args.preset if shader else None, "solar_tonemap": bool(shader and args.solar_tonemap),
                "core_sha256": hashlib.sha256(release.read_bytes()).hexdigest(), "artifacts": installed}
    total_cases = 14 if release.name == "v16-release-matrix.jar" else 15
    expected = (total_cases if args.case < 0 else 1) * args.repeats
    path = folder / "profile-manifest.json"
    path.write_text(json.dumps(manifest, indent=2), encoding="utf-8")
    print(f"Launching packaged Forge: {folder}", flush=True)
    start = time.monotonic()
    try:
        with (folder / "console.log").open("w", encoding="utf-8") as log:
            result = subprocess.run(command, cwd=folder, env=env, stdout=log, stderr=subprocess.STDOUT,
                                    creationflags=subprocess.CREATE_NO_WINDOW, timeout=100+expected*42)
        manifest["exit"] = result.returncode
    except subprocess.TimeoutExpired:
        manifest["exit"] = "timeout"
    manifest["seconds"] = round(time.monotonic()-start, 1)
    log = (folder / "console.log").read_text(encoding="utf-8", errors="replace")
    manifest["cases_completed"] = log.count("Ryujin Bankai QA completed")
    checks = []
    if args.music_probe:
        probes = re.findall(r"Ryujin QA vanilla music \S+ (started=.+sliderPreserved=\w+)",log)
        expected_probes = (total_cases-4 if args.case<0 else int(args.case not in range(8,12))) * args.repeats
        accepted = ("started=false blocked=false released=true canResume=false sliderPreserved=true"
                    if args.mute_music else "started=true blocked=true released=true canResume=true sliderPreserved=true")
        manifest["music_probes"] = probes
        checks.append(len(probes)==expected_probes and all(value==accepted for value in probes))
    if args.bloom or args.heat:
        marker = f"Ryujin solar post applied bloom={str(args.bloom).lower()} refraction={str(args.heat).lower()}"
        manifest["post_effect_applied"] = marker in log
        checks.append(manifest["post_effect_applied"] and "Ryujin solar post unavailable" not in log)
    if args.shader_composite:
        measurements = list(folder.glob("ryujin-bankai-qa/*/*/performance.json"))
        manifest["composite_verified"] = len(measurements)==expected and all(
            json.loads(path.read_text(encoding="utf-8"))["solar_shader"]=="shader-pack-post" for path in measurements)
        checks.append(manifest["composite_verified"])
    manifest["accepted"] = manifest["exit"]==0 and manifest["cases_completed"]==expected and all(checks)
    path.write_text(json.dumps(manifest, indent=2), encoding="utf-8")
    print(f"Exit {manifest['exit']} · {manifest['cases_completed']} QA cases · {manifest['seconds']} s", flush=True)
    return 0 if manifest["accepted"] else 1


if __name__ == "__main__":
    raise SystemExit(main())
