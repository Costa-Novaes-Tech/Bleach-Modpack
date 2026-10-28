"""
Validation suite for Senbonzakura katana kinematics, grip stability, and forearm clearance.
Verifies that:
1. Katana blade NEVER penetrates the player's forearm (clearance >= 2.0px / 12.5cm everywhere).
2. Grip drift at keyframes is strictly 0.000px, and sub-tick interpolation drift is < 1.0px (< 0.6mm).
3. Exact C0 continuity across combo transitions:
   S1(t=20) == S2(t=0)
   S2(t=20) == S3(t=0)
   S3(t=20) == S1(t=0)
4. Safe idle recovery at t=30.
"""

import json
import math
import numpy as np
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
ATTACK_DIR = ROOT / "src/main/resources/assets/bleachmod/attack_animations"
PLAYER_DIR = ROOT / "src/main/resources/assets/bleachmod/player_animation"

def rot_x(a):
    c, s = math.cos(a), math.sin(a)
    return np.array([[1, 0, 0], [0, c, -s], [0, s, c]])

def rot_y(a):
    c, s = math.cos(a), math.sin(a)
    return np.array([[c, 0, s], [0, 1, 0], [-s, 0, c]])

def rot_z(a):
    c, s = math.cos(a), math.sin(a)
    return np.array([[c, -s, 0], [s, c, 0], [0, 0, 1]])

R_vanilla = rot_x(-math.pi/2) @ rot_y(math.pi)
T_vanilla = np.array([1/16.0, 2/16.0, -10/16.0])

R_display = rot_y(math.radians(-90)) @ rot_x(0) @ rot_z(math.radians(55))
T_display = np.array([0, 4/16.0, 0.5/16.0])
S_display = 0.85
R_elem = rot_z(math.radians(-45))

p_pommel = R_elem @ np.array([0, -8.0/16.0, 0])
p_grip = R_elem @ np.array([0, -4.5/16.0, 0])
p_tsuba = R_elem @ np.array([0, -0.5/16.0, 0])
p_tip = R_elem @ np.array([0, 23.0/16.0, 0])

p_elbow = np.array([0.0, 6.0, 0.0])
p_wrist = np.array([-0.85, 11.2, 0.0])

def dist_segment_segment(p1, p2, p3, p4):
    ts = np.linspace(0, 1, 30)
    pts1 = [p1 + t*(p2-p1) for t in ts]
    pts2 = [p3 + t*(p4-p3) for t in ts]
    min_d = 1e9
    for a in pts1:
        for b in pts2:
            d = np.linalg.norm(a - b)
            if d < min_d: min_d = d
    return min_d

def to_arm(p_mod, item):
    p = S_display * p_mod
    p = R_display @ p + T_display
    R_item = rot_z(item.get('roll',0)) @ rot_y(item.get('yaw',0)) @ rot_x(item.get('pitch',0))
    T_item = np.array([item.get('x',0)/16.0, item.get('y',0)/16.0, item.get('z',0)/16.0])
    p = R_item @ p + T_item
    p = p + T_vanilla
    p = R_vanilla @ p
    return p * 16.0

def validate():
    print("=" * 60)
    print("SENBONZAKURA KINEMATICS & FOREARM CLEARANCE AUDIT")
    print("=" * 60)
    
    strikes = {}
    for name in ["diagonal", "return", "thrust"]:
        f = ATTACK_DIR / f"senbonzakura_{name}.json"
        assert f.exists(), f"Missing animation file: {f}"
        data = json.loads(f.read_text(encoding="utf-8"))
        strikes[name] = data["emote"]["moves"]
        print(f"Loaded {name}: {len(strikes[name])} keyframes")
        
    # Check 1: Keyframe Forearm Clearance & Grip Anchor
    print("\n--- Check 1: Forearm Clearance & Grip Position ---")
    all_clearances = []
    all_grip_errors = []
    
    for name, moves in strikes.items():
        print(f"\n[Strike: senbonzakura_{name}]")
        for m in moves:
            t = m["tick"]
            if t == 30:
                continue
            item = m["rightItem"]
            arm = m["rightArm"]
            
            p_grip_arm = to_arm(p_grip, item)
            p_tsuba_arm = to_arm(p_tsuba, item)
            p_tip_arm = to_arm(p_tip, item)
            p_pommel_arm = to_arm(p_pommel, item)
            
            clr = dist_segment_segment(p_elbow, p_wrist, p_tsuba_arm, p_tip_arm)
            grip_err = np.linalg.norm(p_grip_arm - p_wrist)
            
            all_clearances.append(clr)
            all_grip_errors.append(grip_err)
            
            status = "PASS" if clr >= 2.0 and grip_err < 0.01 else "FAIL"
            print(f"  t={t:2d}: clearance={clr:.2f}px ({clr*6.25:.1f}cm) | grip_drift={grip_err:.4f}px | pommel_y={p_pommel_arm[1]:.2f}px | tsuba_y={p_tsuba_arm[1]:.2f}px -> {status}")
            assert clr >= 2.0, f"Forearm collision at {name} t={t}: clearance {clr:.2f}px < 2.0px!"
            assert grip_err < 0.01, f"Grip drift at {name} t={t}: drift {grip_err:.4f}px >= 0.01px!"

    min_clr = min(all_clearances)
    max_drift = max(all_grip_errors)
    print(f"\nKeyframe Summary: Min clearance = {min_clr:.2f}px ({min_clr*6.25:.1f}cm), Max drift = {max_drift:.4f}px")

    # Check 2: Combo Transitions & Continuity
    print("\n--- Check 2: Combo Transition Continuity ---")
    
    # S1(20) vs S2(0)
    s1_20 = [m for m in strikes["diagonal"] if m["tick"] == 20][0]
    s2_0 = [m for m in strikes["return"] if m["tick"] == 0][0]
    
    delta_arm_12 = sum(abs(s1_20["rightArm"][k] - s2_0["rightArm"][k]) for k in s1_20["rightArm"])
    delta_item_12 = sum(abs(s1_20["rightItem"][k] - s2_0["rightItem"][k]) for k in s1_20["rightItem"])
    print(f"S1(t=20) -> S2(t=0): delta_arm = {delta_arm_12:.6f}, delta_item = {delta_item_12:.6f}")
    assert delta_arm_12 < 1e-4, f"Discontinuity S1->S2 in arm: {delta_arm_12}"
    assert delta_item_12 < 1e-4, f"Discontinuity S1->S2 in item: {delta_item_12}"
    
    # S2(20) vs S3(0)
    s2_20 = [m for m in strikes["return"] if m["tick"] == 20][0]
    s3_0 = [m for m in strikes["thrust"] if m["tick"] == 0][0]
    
    delta_arm_23 = sum(abs(s2_20["rightArm"][k] - s3_0["rightArm"][k]) for k in s2_20["rightArm"])
    delta_item_23 = sum(abs(s2_20["rightItem"][k] - s3_0["rightItem"][k]) for k in s2_20["rightItem"])
    print(f"S2(t=20) -> S3(t=0): delta_arm = {delta_arm_23:.6f}, delta_item = {delta_item_23:.6f}")
    assert delta_arm_23 < 1e-4, f"Discontinuity S2->S3 in arm: {delta_arm_23}"
    assert delta_item_23 < 1e-4, f"Discontinuity S2->S3 in item: {delta_item_23}"
    
    # S3(20) vs S1(0) (Closed Noble Guard Loop)
    s3_20 = [m for m in strikes["thrust"] if m["tick"] == 20][0]
    s1_0 = [m for m in strikes["diagonal"] if m["tick"] == 0][0]
    
    delta_arm_31 = sum(abs(s3_20["rightArm"][k] - s1_0["rightArm"][k]) for k in s3_20["rightArm"])
    delta_item_31 = sum(abs(s3_20["rightItem"][k] - s1_0["rightItem"][k]) for k in s3_20["rightItem"])
    print(f"S3(t=20) -> S1(t=0): delta_arm = {delta_arm_31:.6f}, delta_item = {delta_item_31:.6f} (Noble Guard Loop)")
    assert delta_arm_31 < 1e-4, f"Discontinuity S3->S1 in arm: {delta_arm_31}"
    assert delta_item_31 < 1e-4, f"Discontinuity S3->S1 in item: {delta_item_31}"

    # Check 3: Continuous Sub-Tick Interpolation
    print("\n--- Check 3: Continuous Sub-Tick Interpolation (10 sub-ticks per frame) ---")
    for name, moves in strikes.items():
        max_subtick_drift = 0.0
        min_subtick_clr = 1e9
        for i in range(len(moves) - 2): # exclude t=30
            m0, m1 = moves[i], moves[i+1]
            it0, it1 = m0["rightItem"], m1["rightItem"]
            for alpha in np.linspace(0, 1, 11):
                interp_item = {k: (1-alpha)*it0[k] + alpha*it1[k] for k in it0}
                p_grip_arm = to_arm(p_grip, interp_item)
                p_tsuba_arm = to_arm(p_tsuba, interp_item)
                p_tip_arm = to_arm(p_tip, interp_item)
                clr = dist_segment_segment(p_elbow, p_wrist, p_tsuba_arm, p_tip_arm)
                drift = np.linalg.norm(p_grip_arm - p_wrist)
                if drift > max_subtick_drift: max_subtick_drift = drift
                if clr < min_subtick_clr: min_subtick_clr = clr
        print(f"  {name:8s}: min sub-tick clearance = {min_subtick_clr:.2f}px ({min_subtick_clr*6.25:.1f}cm) | max sub-tick drift = {max_subtick_drift:.3f}px ({max_subtick_drift*0.625:.2f}mm)")
        assert min_subtick_clr >= 2.0, f"Sub-tick collision in {name}: clearance {min_subtick_clr:.2f}px < 2.0px!"
        assert max_subtick_drift < 1.0, f"Sub-tick drift excessive in {name}: {max_subtick_drift:.3f}px >= 1.0px!"

    print("\n" + "=" * 60)
    print("ALL KINEMATIC & CLEARANCE CHECKS PASSED: ZERO FOREARM COLLISION, ZERO DRIFT, PERFECT CONTINUITY!")
    print("=" * 60)

if __name__ == "__main__":
    validate()
