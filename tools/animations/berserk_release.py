"""Writes the Berserk Mode release body animation (GeckoLib/Blockbench format read by PlayerAnimator).

Run from the repository root:  python tools/animations/berserk_release.py
7 s = 140 ticks, synced with NozarashiReleaseEffects: fists pound the ground at 1.2/1.6/2.0 s, eruption and
first roar at 2.25 s, second roar at 3.4 s, he rises, grabs the blade at 5.6 s and hammers the ground at
6.2 s, ending in the shoulder stance. Rotations in degrees, positions in pixels. Bedrock convention:
negative X raises an arm forward, positive X tilts head/body forward. Editable in Blockbench.
"""
import json
from pathlib import Path

OUT = Path(__file__).resolve().parents[2] / "src/main/resources/assets/bleachmod/player_animation/berserk_release.json"
E = "easeInOutSine"
POUNDS = (1.2, 1.6, 2.0)
ERUPTION, ROAR2, RISE, GRAB, SLAM, END = 2.25, 3.4, 3.9, 5.6, 6.2, 7.0
REST_ARM = (-145, -20, 12)  # ends in the shoulder stance (tools/animations/nozarashi_stance.py)


def track(*keys):
    keys = sorted(keys, key=lambda k: k[0])
    return {f"{t:.2f}": {"vector": list(v), "easing": e} for t, v, e in keys}


# Trembling with rage before the collapse.
tremble = [(t / 10, (4, 0, 3 if i % 2 else -3), "linear") for i, t in enumerate(range(1, 10))]
pound_arm = []
for p in POUNDS:
    pound_arm += [(p - .12, (-160, 0, -12), "easeOutQuad"), (p, (-68, 0, -10), "easeInExpo")]

bones = {
    "body": {
        "position": track((0, (0, 0, 0), E), (1.0, (0, 0, 0), E), (1.15, (0, -9, 2), "easeInQuad"),
                          (RISE, (0, -9, 2), E), (5.3, (0, 0, 0), "easeOutCubic"), (SLAM - .1, (0, 0, 0), E),
                          (SLAM, (0, -2, 0), "easeInExpo"), (END, (0, 0, 0), E)),
        "rotation": track((0, (0, 0, 0), E), *tremble, (1.0, (6, 0, 0), E), (1.15, (40, 0, 0), "easeInQuad"),
                          *[(p, (44, 0, 0), "easeInExpo") for p in POUNDS],
                          (ERUPTION, (18, 0, 0), "easeOutBack"), (2.9, (22, 0, 0), E), (3.2, (36, 0, 0), E),
                          (ROAR2, (14, 0, 0), "easeOutBack"), (RISE, (36, 0, 0), E), (5.3, (0, 0, 0), "easeOutCubic"),
                          (GRAB, (10, 0, 0), E), (SLAM - .1, (-8, 0, 0), E), (SLAM, (26, 0, 0), "easeInExpo"), (END, (0, 0, 0), E)),
    },
    "head": {
        "rotation": track((0, (10, 0, 0), E), (1.0, (25, 0, 0), E), (1.15, (35, 0, 0), E),
                          *[(p, (42, 0, 0), "easeInExpo") for p in POUNDS],
                          (ERUPTION, (-60, 0, 0), "easeOutBack"), (2.9, (-50, 0, 0), E), (3.2, (25, 0, 0), E),
                          (ROAR2, (-55, 0, 0), "easeOutBack"), (RISE, (20, 0, 0), E), (5.3, (0, 0, 0), E),
                          (SLAM, (18, 0, 0), "easeInExpo"), (END, (0, 0, 0), E)),
    },
    # Free fist: hammers the ground three times, then claws outward with each roar.
    "left_arm": {
        "rotation": track((0, (0, 0, 0), E), (1.0, (-20, 0, -10), E), (1.08, (-75, 0, -10), "easeInQuad"), *pound_arm,
                          (ERUPTION, (-55, 0, -60), "easeOutBack"), (2.9, (-72, 0, -14), E),
                          (ROAR2, (-95, 0, -65), "easeOutBack"), (RISE, (-72, 0, -12), E),
                          (5.3, (-15, 0, -8), E), (SLAM, (30, 0, -15), "easeInExpo"), (END, (0, 0, 0), E)),
    },
    # Sword hand: drags the blade, braces, reaches down, rips it overhead and smashes it into the ground.
    "right_arm": {
        "rotation": track((0, (0, 0, 0), E), (1.0, (-15, 0, 10), E), (1.15, (-60, 0, 20), "easeInQuad"),
                          (ERUPTION, (-50, 0, 50), "easeOutBack"), (2.9, (-55, 0, 22), E),
                          (ROAR2, (-60, 0, 55), "easeOutBack"), (RISE, (-55, 0, 18), E), (5.3, (-15, 0, 15), E),
                          (GRAB, (-25, 0, 35), E), (5.95, (-190, 0, -5), "easeOutBack"),
                          (SLAM, (-25, 0, 0), "easeInExpo"), (6.5, (-30, 0, 0), E), (END, REST_ARM, E)),
    },
    "right_leg": {
        "rotation": track((0, (0, 0, 0), E), (1.0, (0, 0, 0), E), (1.15, (-70, 0, 6), "easeInQuad"),
                          (RISE, (-70, 0, 6), E), (5.3, (0, 0, 0), "easeOutCubic"), (END, (0, 0, 0), E)),
    },
    "left_leg": {
        "rotation": track((0, (0, 0, 0), E), (1.0, (0, 0, 0), E), (1.15, (-25, 0, -6), "easeInQuad"),
                          (RISE, (-25, 0, -6), E), (5.3, (0, 0, 0), "easeOutCubic"), (SLAM, (-20, 0, 0), E), (END, (0, 0, 0), E)),
    },
}
animation = {"format_version": "1.8.0", "animations": {"berserk_release": {
    "animation_length": END, "loop": False, "bones": bones}}}
OUT.parent.mkdir(parents=True, exist_ok=True)
OUT.write_text(json.dumps(animation, indent=1) + "\n", encoding="utf-8")
print("written", OUT)
