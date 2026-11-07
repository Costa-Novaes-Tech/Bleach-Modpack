"""Archive selected disposable QA evidence; never opens or copies personal saves."""
from __future__ import annotations
import argparse, hashlib, json, re, shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("source", type=Path)
    parser.add_argument("label")
    parser.add_argument("--log", type=Path)
    parser.add_argument("--ticks", default="0,40,50,70,84,112,144,175,190,202,220,300")
    args = parser.parse_args()
    if not re.fullmatch(r"[a-z0-9-]{1,64}", args.label):
        parser.error("Label must use lowercase letters, digits and dashes")
    try:
        ticks = sorted(set(int(value) for value in args.ticks.split(",")))
        if not ticks or any(value < 0 or value > 360 for value in ticks):
            raise ValueError()
    except ValueError:
        parser.error("Ticks must be a comma-separated list in 0..360")
    source = args.source.resolve()
    sandbox_roots = (ROOT / "run-benihime-qa", ROOT / "build/visual-lab/run-benihime-qa")
    if (not any(source.is_relative_to(root.resolve()) for root in sandbox_roots)
            or "ryujin-bankai-qa" not in source.parts):
        parser.error("Source must be an actual Ryujin QA capture directory")
    target = ROOT / "Docs/poderes/qa/codex" / args.label
    if target.exists():
        parser.error("Evidence label already exists; preserve it and choose a new label")
    files = []
    for name in ("frames.jsonl", "performance.json", "frame-times.csv"):
        p = source / name
        if p.is_file():
            files.append((p, Path(name)))
    for tick in ticks:
        rel = Path(f"screenshots/frame-t{tick:03d}.png")
        p = source / rel
        if p.is_file():
            files.append((p, rel))
    if args.log:
        log = args.log.resolve()
        log.relative_to((ROOT / "build/visual-lab").resolve())
        files.append((log, Path("client.log")))
    if not files:
        parser.error("No QA evidence found")
    manifest = {"source": str(source.relative_to(ROOT)), "files": []}
    for p, rel in files:
        out = target / rel
        out.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(p, out)
        manifest["files"].append({"path": rel.as_posix(), "bytes": out.stat().st_size,
                                  "sha256": hashlib.sha256(out.read_bytes()).hexdigest()})
    (target / "manifest.json").write_text(json.dumps(manifest, indent=2), encoding="utf-8")
    print(f"Archived {len(files)} files: {target}")


if __name__ == "__main__":
    main()
