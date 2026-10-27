import json
import numpy as np

def rx(rad): return np.array([[1,0,0],[0,np.cos(rad),-np.sin(rad)],[0,np.sin(rad),np.cos(rad)]])
def ry(rad): return np.array([[np.cos(rad),0,np.sin(rad)],[0,1,0],[-np.sin(rad),0,np.cos(rad)]])
def rz(rad): return np.array([[np.cos(rad),-np.sin(rad),0],[np.sin(rad),np.cos(rad),0],[0,0,1]])

M_disp = rx(0) @ ry(np.radians(-90)) @ rz(np.radians(55))
M_elem = rz(np.radians(-45))
v_blade = np.array([0, 1, 0])
v_in = M_disp @ M_elem @ v_blade
M_layer = rx(np.radians(-90)) @ ry(np.radians(180))

with open('tools/animations/s1_solved_item.json') as f:
    item_data = json.load(f)
with open('Docs/poderes/media/137-storyboards/poses.json') as f:
    d = json.load(f)
kfs = [a['keyframes'] for a in d['actions'] if a['id'] == 'S1'][0]

arm_data = [
    {'pitch': -0.70, 'yaw': 0.10, 'roll': -0.10},
    {'pitch': -1.30, 'yaw': 0.25, 'roll': -0.25},
    {'pitch': -1.65, 'yaw': 0.32, 'roll': -0.30},
    {'pitch': -1.25, 'yaw': -0.05, 'roll': 0.10},
    {'pitch': -0.85, 'yaw': -0.35, 'roll': 0.25},
    {'pitch': -0.70, 'yaw': -0.45, 'roll': 0.28},
    {'pitch': -0.60, 'yaw': -0.50, 'roll': 0.30}
]

torso_data = [
    {'yaw': 0.0, 'pitch': 0.0, 'roll': 0.0},
    {'yaw': -0.15, 'pitch': 0.05, 'roll': 0.02},
    {'yaw': -0.30, 'pitch': 0.08, 'roll': 0.03},
    {'yaw': 0.0, 'pitch': -0.04, 'roll': -0.04},
    {'yaw': 0.35, 'pitch': -0.08, 'roll': -0.08},
    {'yaw': 0.50, 'pitch': -0.06, 'roll': -0.06},
    {'yaw': 0.60, 'pitch': -0.05, 'roll': -0.05}
]

print("FRAME  | TARGET (X, Y_down, Z_back) | ACTUAL (X, Y_down, Z_back) | ERROR")
print("-" * 65)
for i in range(7):
    kf = kfs[i]
    dp = kf['direction']
    target_mc = np.array([dp[0], -dp[1], -dp[2]])
    target_mc = target_mc / np.linalg.norm(target_mc)

    M_torso = rz(torso_data[i]['roll']) @ ry(torso_data[i]['yaw']) @ rx(torso_data[i]['pitch'])
    M_arm = rz(arm_data[i]['roll']) @ ry(arm_data[i]['yaw']) @ rx(arm_data[i]['pitch'])
    it = item_data[i]
    M_held = rz(it['roll']) @ ry(it['yaw']) @ rx(it['pitch'])

    v_out = M_torso @ M_arm @ M_layer @ M_held @ v_in
    err = np.linalg.norm(v_out - target_mc)
    t_str = f"[{target_mc[0]:+.3f}, {target_mc[1]:+.3f}, {target_mc[2]:+.3f}]"
    a_str = f"[{v_out[0]:+.3f}, {v_out[1]:+.3f}, {v_out[2]:+.3f}]"
    print(f"{kf['id']:6s} | {t_str} | {a_str} | {err:.4f}")
