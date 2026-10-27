import json
import math
import numpy as np

def rx(rad):
    return np.array([[1, 0, 0], [0, np.cos(rad), -np.sin(rad)], [0, np.sin(rad), np.cos(rad)]])

def ry(rad):
    return np.array([[np.cos(rad), 0, np.sin(rad)], [0, 1, 0], [-np.sin(rad), 0, np.cos(rad)]])

def rz(rad):
    return np.array([[np.cos(rad), -np.sin(rad), 0], [np.sin(rad), np.cos(rad), 0], [0, 0, 1]])

M_disp = rx(np.radians(0)) @ ry(np.radians(-90)) @ rz(np.radians(55))
M_elem = rz(np.radians(-45))
v_blade = np.array([0, 1, 0])
v_in = M_disp @ M_elem @ v_blade
v_in = v_in / np.linalg.norm(v_in)

M_layer = rx(np.radians(-90)) @ ry(np.radians(180))

with open('Docs/poderes/media/137-storyboards/poses.json') as f:
    d = json.load(f)

s1_keyframes = [a['keyframes'] for a in d['actions'] if a['id'] == 'S1'][0]

torso_data = [
    {"yaw": 0.0, "pitch": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0},       # t=0
    {"yaw": -0.15, "pitch": 0.05, "roll": 0.02, "x": 0.0, "y": 0.0, "z": 0.05},  # t=4
    {"yaw": -0.30, "pitch": 0.08, "roll": 0.03, "x": 0.0, "y": 0.0, "z": 0.08},  # t=8
    {"yaw": 0.0, "pitch": -0.04, "roll": -0.04, "x": 0.0, "y": 0.0, "z": -0.04}, # t=11
    {"yaw": 0.35, "pitch": -0.08, "roll": -0.08, "x": 0.0, "y": 0.0, "z": -0.08},# t=14
    {"yaw": 0.50, "pitch": -0.06, "roll": -0.06, "x": 0.0, "y": 0.0, "z": -0.06},# t=17
    {"yaw": 0.60, "pitch": -0.05, "roll": -0.05, "x": 0.0, "y": 0.0, "z": -0.04},# t=20
    {"yaw": 0.0, "pitch": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}        # t=30
]

arm_data = [
    {"pitch": -0.70, "yaw": 0.10, "roll": -0.10, "x": -4.8, "y": 1.8, "z": -0.5},
    {"pitch": -1.30, "yaw": 0.25, "roll": -0.25, "x": -4.8, "y": 2.0, "z": -0.5},
    {"pitch": -1.65, "yaw": 0.32, "roll": -0.30, "x": -4.8, "y": 2.1, "z": -0.5},
    {"pitch": -1.25, "yaw": -0.05, "roll": 0.10, "x": -4.7, "y": 1.9, "z": -1.2},
    {"pitch": -0.85, "yaw": -0.35, "roll": 0.25, "x": -4.6, "y": 1.7, "z": -1.5},
    {"pitch": -0.70, "yaw": -0.45, "roll": 0.28, "x": -4.6, "y": 1.6, "z": -1.2},
    {"pitch": -0.60, "yaw": -0.50, "roll": 0.30, "x": -4.6, "y": 1.5, "z": -0.8},
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}
]

# For each keyframe, collect ALL good candidate triples (pitch, yaw, roll) with error < 0.02
candidates_per_frame = []

for i in range(7): # 0..6
    kf = s1_keyframes[i]
    dir_poses = kf['direction']
    target_mc = np.array([dir_poses[0], -dir_poses[1], -dir_poses[2]])
    target_mc = target_mc / np.linalg.norm(target_mc)

    M_torso = rz(torso_data[i]["roll"]) @ ry(torso_data[i]["yaw"]) @ rx(torso_data[i]["pitch"])
    M_arm = rz(arm_data[i]["roll"]) @ ry(arm_data[i]["yaw"]) @ rx(arm_data[i]["pitch"])
    M_before = M_torso @ M_arm @ M_layer
    v_hand_target = np.linalg.inv(M_before) @ target_mc
    v_hand_target = v_hand_target / np.linalg.norm(v_hand_target)

    frame_cands = []
    # Dense sample
    for r in np.linspace(-np.pi, np.pi, 25):
        for y in np.linspace(-np.pi, np.pi, 25):
            for p in np.linspace(-np.pi, np.pi, 25):
                M = rz(r) @ ry(y) @ rx(p)
                err = np.linalg.norm(M @ v_in - v_hand_target)
                if err < 0.06:
                    frame_cands.append((p, y, r, err))
    candidates_per_frame.append((frame_cands, v_hand_target))
    print(f"Frame {i} has {len(frame_cands)} candidates")

# Now find a smooth sequence through candidates minimizing total angular distance
# Start from F02 (index 2) which is the anchor:
# We know S1-F02 has a natural item pose near (p=-0.23, y=-1.30, r=0.96) or (p=0.27, y=-2.62, r=0.69)
# Let's search best path using dynamic programming / Viterbi
dp = {}
# Initialize frame 0
for idx, (p, y, r, err) in enumerate(candidates_per_frame[0][0]):
    dp[(0, idx)] = (err * 10, [idx])

for f in range(1, 7):
    new_dp = {}
    prev_cands = candidates_per_frame[f-1][0]
    curr_cands = candidates_per_frame[f][0]
    for c_idx, (cp, cy, cr, cerr) in enumerate(curr_cands):
        best_cost = 1e9
        best_path = None
        for p_idx, (pp, py, pr, perr) in enumerate(prev_cands):
            # Angular distance
            dp_val = min(abs(cp - pp), 2*np.pi - abs(cp - pp))
            dy_val = min(abs(cy - py), 2*np.pi - abs(cy - py))
            dr_val = min(abs(cr - pr), 2*np.pi - abs(cr - pr))
            step_dist = dp_val**2 + dy_val**2 + dr_val**2
            cost = dp[(f-1, p_idx)][0] + step_dist + cerr * 10
            if cost < best_cost:
                best_cost = cost
                best_path = dp[(f-1, p_idx)][1] + [c_idx]
        new_dp[(f, c_idx)] = (best_cost, best_path)
    dp = new_dp

best_overall = min(dp.values(), key=lambda x: x[0])
print(f"Optimal smooth path cost: {best_overall[0]:.4f}")
path_indices = best_overall[1]

smooth_angles = []
for f, idx in enumerate(path_indices):
    cand = candidates_per_frame[f][0][idx]
    # Refine locally
    p0, y0, r0, _ = cand
    v_hand_target = candidates_per_frame[f][1]
    best_p, best_y, best_r, best_err = p0, y0, r0, 999.0
    for r in np.linspace(r0 - 0.15, r0 + 0.15, 20):
        for y in np.linspace(y0 - 0.15, y0 + 0.15, 20):
            for p in np.linspace(p0 - 0.15, p0 + 0.15, 20):
                M = rz(r) @ ry(y) @ rx(p)
                err = np.linalg.norm(M @ v_in - v_hand_target)
                if err < best_err:
                    best_err = err
                    best_p, best_y, best_r = p, y, r
    smooth_angles.append((best_p, best_y, best_r, best_err))

print("=== REFINED SMOOTH PATH ===")
final_right_item = []
for f, (p, y, r, err) in enumerate(smooth_angles):
    tick = s1_keyframes[f]['tick']
    print(f"t={tick:2d} ({s1_keyframes[f]['id']}): pitch={p:+.4f}, yaw={y:+.4f}, roll={r:+.4f} (err={err:.4f})")
    final_right_item.append({
        "pitch": round(p, 4),
        "yaw": round(y, 4),
        "roll": round(r, 4),
        "x": 1.0,
        "y": -2.0,
        "z": -0.8
    })

# Add tick 30 recovery
final_right_item.append({
    "pitch": 0.0, "yaw": 0.0, "roll": 0.0,
    "x": 0.0, "y": 0.0, "z": 0.0
})

with open("tools/animations/s1_solved_item.json", "w") as f:
    json.dump(final_right_item, f, indent=2)
print("Saved to tools/animations/s1_solved_item.json")
