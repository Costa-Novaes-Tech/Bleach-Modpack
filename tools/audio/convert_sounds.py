"""Converts source voice lines (tools/audio/source/*.mp3) into Minecraft-ready OGG Vorbis.

Run from the repository root:  python tools/audio/convert_sounds.py   (needs: pip install soundfile)
Minecraft only plays OGG Vorbis, and only MONO files are positional (fade with distance), so every
file is mixed down to mono and peak-normalized before being written to assets/bleachmod/sounds.
"""
from pathlib import Path
import numpy as np
import soundfile as sf

ROOT = Path(__file__).resolve().parents[2]
SOURCE = ROOT / "tools/audio/source"
OUT = ROOT / "src/main/resources/assets/bleachmod/sounds"
PEAK = .9

for mp3 in sorted(SOURCE.glob("*.mp3")):
    data, rate = sf.read(mp3, always_2d=True)
    mono = data.mean(axis=1)
    mono *= PEAK / max(1e-9, float(np.abs(mono).max()))
    target = OUT / (mp3.stem.lower().replace(" ", "_") + ".ogg")
    OUT.mkdir(parents=True, exist_ok=True)
    sf.write(target, mono, rate, format="OGG", subtype="VORBIS")
    print(f"{mp3.name} -> {target.relative_to(ROOT)} ({len(mono) / rate:.2f} s, mono, {rate} Hz)")
