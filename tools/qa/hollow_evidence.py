"""Package real Minecraft framebuffer sequences as animated review GIFs.

python tools/qa/hollow_evidence.py <hollow-evidence/world> <output-folder>
Requires Pillow, already available in this workstation. Original PNGs are untouched.
Playback durations follow captured game ticks; missing frames are held, not interpolated.
"""
from pathlib import Path
import argparse
import json
from PIL import Image


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("source", type=Path)
    parser.add_argument("output", type=Path)
    args = parser.parse_args()
    args.output.mkdir(parents=True, exist_ok=True)
    metadata = [json.loads(line) for line in (args.source / "frames.jsonl").read_text().splitlines()]
    for prefix in ["tank-", "flight-", "swift-", "gait-side-", "gait-front-", "gait-rear-"]:
        frames = [row for row in metadata if Path(row["file"]).name.startswith(prefix)]
        if not frames:
            continue
        first = Image.open(args.source / frames[0]["file"]).convert("RGB").resize((768, 432), Image.Resampling.LANCZOS)
        palette = first.quantize(colors=128)
        pictures = []
        durations = []
        for index, row in enumerate(frames):
            with Image.open(args.source / row["file"]) as image:
                pictures.append(image.convert("RGB").resize((768, 432), Image.Resampling.LANCZOS)
                                .quantize(palette=palette, dither=Image.Dither.NONE))
            ticks = frames[index + 1]["tick"] - row["tick"] if index + 1 < len(frames) else 2
            durations.append(max(50, ticks * 50))
        path = args.output / (prefix.rstrip("-") + ".gif")
        pictures[0].save(path, save_all=True, append_images=pictures[1:], duration=durations, loop=0,
                         optimize=True, disposal=1)
        print(f"{path}: {len(pictures)} real frames; {sum(durations) / 1000:.2f}s; {path.stat().st_size} bytes")
    trace = args.source / "combat.jsonl"
    if trace.exists():
        rows = [json.loads(line) for line in trace.read_text().splitlines()]
        report = {}
        for variant in ["brute", "swift", "flying"]:
            samples = [r for r in rows if r["variant"] == variant]
            starts = list(dict.fromkeys(r["lastStart"] for r in samples if r["lastStart"] >= 0))
            gaps = [b - a for a, b in zip(starts, starts[1:])]
            entry = {"starts": starts, "intervalsTicks": gaps,
                     "successfulHits": max((r["hits"] for r in samples), default=0)}
            if variant == "flying":
                transitions = []
                previous = None
                for row in samples:
                    if row["phase"] != previous:
                        transitions.append({k: row[k] for k in ["entityTick", "phase", "hits", "passes", "lastBurstAttempts"]})
                        previous = row["phase"]
                entry["transitions"] = transitions
                entry["passes"] = max((r["passes"] for r in samples), default=0)
                entry["retreats"] = []
                previous_hits = 0
                for count in range(1, entry["passes"] + 1):
                    retreat = [r for r in samples if r["phase"] == "RETREAT" and r["passes"] == count]
                    if not retreat:
                        continue
                    first, last = retreat[0], retreat[-1]
                    complete = any(r["entityTick"] > last["entityTick"] and r["phase"] != "RETREAT" for r in samples)
                    entry["retreats"].append({"pass": count, "attempts": first["lastBurstAttempts"],
                        "landedHits": first["hits"] - previous_hits,
                        "observedTicks": last["entityTick"] - first["entityTick"] + 2, "complete": complete,
                        "hitsDuringRetreat": last["hits"] - first["hits"], "lastDistance": last.get("distance"),
                        "maxHeightAboveTarget": max(r["y"] - r.get("targetY", r["y"]) for r in retreat)})
                    previous_hits = first["hits"]
            report[variant] = entry
        (args.output / "combat-summary.json").write_text(json.dumps(report, indent=2) + "\n")
        print(json.dumps(report))


if __name__ == "__main__":
    main()
