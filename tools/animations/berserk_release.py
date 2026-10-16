"""Writes the Berserk Mode release body animation (GeckoLib/Blockbench format read by PlayerAnimator).

Run from the repository root:  python tools/animations/berserk_release.py
Times in seconds (5 s = 100 ticks, synced with NozarashiReleaseEffects), rotations in degrees, positions in
pixels. Bedrock convention: negative X raises an arm forward, positive X tilts head/body forward.
The output can be opened and refined in Blockbench.
"""
import json
from pathlib import Path

OUT = Path(__file__).resolve().parents[2] / "src/main/resources/assets/bleachmod/player_animation/berserk_release.json"
E = "easeInOutSine"

def track(*keys):
    return {f"{t:.2f}": {"vector": list(v), "easing": e} for t, v, e in keys}

bones = {
    # Whole body: sink to the ground (on all fours, as in the anime), hold, then rise.
    "body": {
        "position": track((0, (0, 0, 0), E), (.5, (0, 0, 0), E), (1.2, (0, -9, 2), "easeInQuad"), (2.75, (0, -9, 2), E),
                          (4.0, (0, 0, 0), "easeOutCubic"), (5.0, (0, 0, 0), E)),
        "rotation": track((0, (0, 0, 0), E), (.5, (6, 0, 0), E), (1.2, (40, 0, 0), "easeInQuad"), (1.6, (34, 0, 0), E),
                          (2.75, (36, 0, 0), E), (4.0, (0, 0, 0), "easeOutCubic"), (5.0, (0, 0, 0), E)),
    },
    "head": {
        "rotation": track((0, (0, 0, 0), E), (.5, (20, 0, 0), E), (1.2, (35, 0, 0), E), (1.55, (-45, 0, 0), "easeOutBack"),
                          (2.4, (-35, 0, 0), E), (2.9, (25, 0, 0), E), (4.0, (5, 0, 0), E), (4.6, (-10, 0, 0), E), (5.0, (0, 0, 0), E)),
    },
    # Free hand slams the ground, then pushes the body up.
    "left_arm": {
        "rotation": track((0, (0, 0, 0), E), (.5, (-10, 0, -5), E), (1.2, (-75, 0, -10), "easeInQuad"), (1.6, (-70, 0, -35), E),
                          (2.75, (-72, 0, -12), E), (3.6, (-20, 0, -8), E), (5.0, (0, 0, 0), E)),
    },
    # Sword hand: drags the blade, then reaches down, rips it up overhead and brings it to guard.
    "right_arm": {
        "rotation": track((0, (0, 0, 0), E), (.5, (-10, 0, 5), E), (1.2, (-60, 0, 20), "easeInQuad"), (1.6, (-50, 0, 45), E),
                          (2.75, (-55, 0, 18), E), (3.8, (-15, 0, 15), E), (4.1, (-25, 0, 35), E),
                          (4.55, (-165, 0, -5), "easeOutBack"), (5.0, (-30, 0, 5), E)),
    },
    "right_leg": {
        "rotation": track((0, (0, 0, 0), E), (.5, (0, 0, 0), E), (1.2, (-70, 0, 6), "easeInQuad"), (2.75, (-70, 0, 6), E),
                          (4.0, (0, 0, 0), "easeOutCubic"), (5.0, (0, 0, 0), E)),
    },
    "left_leg": {
        "rotation": track((0, (0, 0, 0), E), (.5, (0, 0, 0), E), (1.2, (-25, 0, -6), "easeInQuad"), (2.75, (-25, 0, -6), E),
                          (4.0, (0, 0, 0), "easeOutCubic"), (5.0, (0, 0, 0), E)),
    },
}
animation = {"format_version": "1.8.0", "animations": {"berserk_release": {
    "animation_length": 5.0, "loop": False, "bones": bones}}}
OUT.parent.mkdir(parents=True, exist_ok=True)
OUT.write_text(json.dumps(animation, indent=1) + "\n", encoding="utf-8")
print("written", OUT)
