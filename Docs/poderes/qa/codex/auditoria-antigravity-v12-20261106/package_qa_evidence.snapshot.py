import os
import sys
import json
import hashlib
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

def sha256_file(p: Path) -> str:
    h = hashlib.sha256()
    with open(p, "rb") as f:
        while chunk := f.read(65536):
            h.update(chunk)
    return h.hexdigest()

def main():
    root = Path(__file__).resolve().parents[6]
    qa_dir = root / "Docs" / "poderes" / "qa" / "antigravity" / "quincy-v11"
    media_dir = root / "Docs" / "poderes" / "media" / "antigravity-quincy-v11"

    print(f"Packaging QA evidence in {qa_dir}...")
    stages = [
        ("emergence_transition", [
            "emergence-arc-t00.png", "emergence-arc-t08.png", "emergence-arc-t16.png",
            "emergence-arc-t24.png", "emergence-arc-t32.png", "emergence-arc-t40.png"
        ]),
        ("settle_transition", [
            "settle-arc-t00.png", "settle-arc-t04.png", "settle-arc-t08.png",
            "settle-arc-t12.png", "settle-arc-t16.png"
        ]),
    ]

    # Create animated GIFs (explicitly documented as sampled transition frames, not 60fps video)
    for name, frames in stages:
        imgs = []
        for f in frames:
            p = qa_dir / f
            if p.exists():
                imgs.append(Image.open(p).convert("RGB"))
        if imgs:
            gif_path = qa_dir / f"{name}.gif"
            imgs[0].save(gif_path, save_all=True, append_images=imgs[1:], duration=180, loop=0)
            print(f"Created {gif_path.name} ({len(imgs)} frames, sampled arc)")

    # Build primary composite contact sheet (3 columns x 2 rows: Emergence Arc + Layer Isolation Diagnostic)
    primary_shots = [
        ("1. Emergence t=0.0s (Folded)", qa_dir / "emergence-arc-t00.png"),
        ("2. Emergence t=1.2s (Mid-Spread)", qa_dir / "emergence-arc-t24.png"),
        ("3. Emergence t=2.0s (Full Wingspan)", qa_dir / "emergence-arc-t40.png"),
        ("4. Layer Isolation: Diffuse Only", qa_dir / "iso-diffuse-only.png"),
        ("5. Layer Isolation: Emissive Only", qa_dir / "iso-glow-only.png"),
        ("6. Layer Isolation: Dual-Layer Baseline", qa_dir / "iso-dual-layer.png"),
    ]

    thumb_w, thumb_h = 640, 360
    cols, rows = 3, 2
    header_h = 80
    footer_h = 40
    pad = 12

    total_w = cols * thumb_w + (cols + 1) * pad
    total_h = header_h + rows * (thumb_h + 30) + (rows + 1) * pad + footer_h

    sheet = Image.new("RGB", (total_w, total_h), (12, 18, 28))
    draw = ImageDraw.Draw(sheet)

    # Header
    draw.rectangle([0, 0, total_w, header_h], fill=(8, 12, 20))
    draw.text((pad + 10, 16), "QUINCY VOLLSTANDIG V11 - RUNTIME QA CAPTURES (GECKOLIB 4.8.4)", fill=(200, 240, 255))
    draw.text((pad + 10, 44), "Cycle 1 Refinement: Emergence Arc (40t/2.0s) + Layer Isolation Diagnostic | 11 Bones | 52 Cubes | Client Forge 1.20.1", fill=(140, 180, 210))

    for idx, (label, img_path) in enumerate(primary_shots):
        r = idx // cols
        c = idx % cols
        x = pad + c * (thumb_w + pad)
        y = header_h + pad + r * (thumb_h + 30 + pad)

        if img_path.exists():
            im = Image.open(img_path).resize((thumb_w, thumb_h), Image.Resampling.LANCZOS)
            sheet.paste(im, (x, y))

        draw.rectangle([x, y + thumb_h, x + thumb_w, y + thumb_h + 26], fill=(16, 24, 38))
        draw.text((x + 8, y + thumb_h + 6), f"{label} [Sampled Capture - Visual Review Pending]", fill=(220, 250, 255))

    # Footer
    draw.rectangle([0, total_h - footer_h, total_w, total_h], fill=(8, 12, 20))
    draw.text((pad + 10, total_h - 28), "Automated Client Testbench: QuincyManifestationQa | BleachMod Forge 1.20.1 | Branch: codex/quincy-vollstandig-refino-v11", fill=(110, 150, 180))

    contact_path = qa_dir / "quincy_v11_qa_contact_sheet.png"
    sheet.save(contact_path)
    print(f"Created contact sheet: {contact_path.name}")

    # Generate QA manifest.json
    qa_manifest = {}
    for f in sorted(qa_dir.iterdir()):
        if f.is_file() and f.name != "manifest.json":
            qa_manifest[f.name] = {
                "bytes": f.stat().st_size,
                "sha256": sha256_file(f)
            }
    with open(qa_dir / "manifest.json", "w", encoding="utf-8") as out_f:
        json.dump(qa_manifest, out_f, indent=2)
    print(f"Created {qa_dir / 'manifest.json'} ({len(qa_manifest)} entries)")

    # Generate Media manifest.json
    if media_dir.exists():
        media_manifest = {}
        for f in sorted(media_dir.iterdir()):
            if f.is_file() and f.name != "manifest.json":
                media_manifest[f.name] = {
                    "bytes": f.stat().st_size,
                    "sha256": sha256_file(f)
                }
        with open(media_dir / "manifest.json", "w", encoding="utf-8") as out_f:
            json.dump(media_manifest, out_f, indent=2)
        print(f"Created {media_dir / 'manifest.json'} ({len(media_manifest)} entries)")

if __name__ == "__main__":
    main()
