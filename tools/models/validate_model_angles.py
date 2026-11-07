"""Validate all Minecraft block/item model JSON files for allowed rotation angles."""
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
MODELS_DIR = ROOT / "src/main/resources/assets/bleachmod/models/item"
VALID_ANGLES = {-45.0, -22.5, 0.0, 22.5, 45.0}

errors = []
checked_files = 0
checked_elements = 0

for json_file in MODELS_DIR.glob("*.json"):
    checked_files += 1
    try:
        data = json.loads(json_file.read_text(encoding="utf-8"))
    except Exception as e:
        errors.append(f"{json_file.name}: JSON parse error: {e}")
        continue

    elements = data.get("elements", [])
    for idx, elem in enumerate(elements):
        checked_elements += 1
        name = elem.get("name", f"elem_{idx}")
        if "rotation" in elem:
            rot = elem["rotation"]
            if "angle" in rot:
                angle = float(rot["angle"])
                if angle not in VALID_ANGLES:
                    errors.append(f"{json_file.name} [{name}]: invalid rotation angle {angle}. Must be one of {sorted(VALID_ANGLES)}")
            if "axis" in rot:
                axis = rot["axis"]
                if axis not in ("x", "y", "z"):
                    errors.append(f"{json_file.name} [{name}]: invalid axis {axis}")

print(f"Validation complete: checked {checked_files} model files, {checked_elements} elements.")
if errors:
    print(f"FAILED with {len(errors)} error(s):")
    for err in errors:
        print("  -", err)
    sys.exit(1)
else:
    print("SUCCESS: 100% of rotation angles are strictly valid in all item models!")
    sys.exit(0)
