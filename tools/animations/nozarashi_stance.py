"""Writes Zaraki's Nozarashi stance animations (GeckoLib/Blockbench format read by PlayerAnimator).

Run from the repository root:  python tools/animations/nozarashi_stance.py

- nozarashi_shoulder: looping idle, sword-arm raised so the blade rests over the shoulder (only the
  right arm and held item are animated, so legs keep vanilla walking).
- nozarashi_hammer: 0.5 s overhead smash played on every swing (plain attacks and techniques).

Bedrock convention: negative X raises an arm forward/up; positive X tilts head/body forward.
"""
import json
from pathlib import Path

DIR = Path(__file__).resolve().parents[2] / "src/main/resources/assets/bleachmod/player_animation"
E = "easeInOutSine"
# Resting pose: hand by the head, blade laid back over the shoulder. Tune here.
REST_ARM = (-145, -20, 12)
REST_ITEM = (0, 0, 0)


def track(*keys):
    return {f"{t:.2f}": {"vector": list(v), "easing": e} for t, v, e in keys}


def write(name, length, loop, bones):
    data = {"format_version": "1.8.0", "animations": {name: {"animation_length": length, "loop": loop, "bones": bones}}}
    path = DIR / f"{name}.json"
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=1) + "\n", encoding="utf-8")
    print("written", path)


breath = (REST_ARM[0] - 4, REST_ARM[1], REST_ARM[2])
write("nozarashi_shoulder", 2.0, True, {
    "right_arm": {"rotation": track((0, REST_ARM, E), (1.0, breath, E), (2.0, REST_ARM, E))},
    "right_item": {"rotation": track((0, REST_ITEM, E), (2.0, REST_ITEM, E))},
})

write("nozarashi_hammer", 0.5, False, {
    # Lift further behind the head, then bring the whole weight straight down.
    "right_arm": {"rotation": track((0, REST_ARM, E), (.12, (-190, -10, 6), "easeOutQuad"),
                                    (.22, (-25, 0, 0), "easeInExpo"), (.32, (-30, 0, 0), E), (.5, REST_ARM, E))},
    "left_arm": {"rotation": track((0, (0, 0, 0), E), (.12, (-20, 0, -10), E), (.22, (30, 0, -15), "easeInExpo"),
                                   (.5, (0, 0, 0), E))},
    "body": {"rotation": track((0, (0, 0, 0), E), (.12, (-8, 0, 0), "easeOutQuad"), (.22, (16, 0, 0), "easeInExpo"),
                               (.32, (14, 0, 0), E), (.5, (0, 0, 0), E)),
             "position": track((0, (0, 0, 0), E), (.22, (0, -1.5, 0), "easeInExpo"), (.5, (0, 0, 0), E))},
    "head": {"rotation": track((0, (0, 0, 0), E), (.12, (-12, 0, 0), E), (.22, (12, 0, 0), "easeInExpo"), (.5, (0, 0, 0), E))},
})
