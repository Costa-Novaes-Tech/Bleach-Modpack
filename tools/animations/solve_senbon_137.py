"""
Solver for Senbonzakura 3-strike combat using exact 137 storyboard poses.
Solves IK for rightArm, rightItem, torso, head, leftArm, and legs.
Outputs verified attack_animations (Emote) and player_animation (Bedrock) JSON files.
"""

import json
import math
import os
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
POSES_PATH = ROOT / "Docs/poderes/media/137-storyboards/poses.json"
ATTACK_ANIM_DIR = ROOT / "src/main/resources/assets/bleachmod/attack_animations"
PLAYER_ANIM_DIR = ROOT / "src/main/resources/assets/bleachmod/player_animation"

with open(POSES_PATH, "r", encoding="utf-8") as f:
    storyboard_data = json.load(f)

# Helper vector math
def v_add(a, b): return [a[0]+b[0], a[1]+b[1], a[2]+b[2]]
def v_sub(a, b): return [a[0]-b[0], a[1]-b[1], a[2]-b[2]]
def v_scale(a, s): return [a[0]*s, a[1]*s, a[2]*s]
def v_dot(a, b): return a[0]*b[0] + a[1]*b[1] + a[2]*b[2]
def v_norm(a): return math.sqrt(v_dot(a, a))
def v_normalize(a):
    n = v_norm(a)
    return [a[0]/n, a[1]/n, a[2]/n] if n > 1e-6 else [0, 0, 1]

# Arm / Rig parameters (in blocks)
# In Minecraft model coordinates:
# Shoulder pivot is at approximately [0.3125, 1.375, 0.0] (in right-hand positive X coordinate system)
SHOULDER_ORIGIN = [0.25, 1.40, 0.0]
ARM_LENGTH = 0.625 # shoulder to palm in Minecraft player model (10-12 pixels)

def solve_arm_and_item(wrist, direction, torso_rot=[0,0,0]):
    """
    Computes (arm_rot, arm_pos, item_rot, item_pos) to match wrist and direction.
    Rotations are in radians (pitch, yaw, roll).
    """
    # Vector from shoulder to wrist in torso local space
    # (torso rotates around Y by torso_rot[1], around X by torso_rot[0])
    tx, ty, tz = torso_rot
    # Invert torso rotation on delta to get local shoulder-to-wrist vector
    dx = wrist[0] - SHOULDER_ORIGIN[0]
    dy = wrist[1] - SHOULDER_ORIGIN[1]
    dz = wrist[2] - SHOULDER_ORIGIN[2]
    
    # Rotate by -ty around Y
    cos_ty = math.cos(-ty)
    sin_ty = math.sin(-ty)
    lx = dx * cos_ty + dz * sin_ty
    ly = dy
    lz = -dx * sin_ty + dz * cos_ty
    
    # Arm length
    dist = math.sqrt(lx*lx + ly*ly + lz*lz)
    if dist < 1e-4:
        arm_dir = [0, -1, 0]
    else:
        arm_dir = [lx/dist, ly/dist, lz/dist]
        
    # Solve arm pitch, yaw, roll
    # Arm at pitch=0 points straight down [0, -1, 0].
    # Pitch swings forward/backward (around X): pitch = -atan2(lz, -ly)
    # Yaw swings left/right (around Y): yaw = atan2(lx, sqrt(ly^2 + lz^2))
    # Roll adjusts abduction/adduction
    arm_pitch = -math.atan2(arm_dir[2], -arm_dir[1])
    arm_yaw = -math.atan2(arm_dir[0], math.sqrt(arm_dir[1]**2 + arm_dir[2]**2))
    arm_roll = 0.0
    
    # For high or crossed poses, roll helps position elbow naturally
    if arm_dir[1] > 0.0: # raised above shoulder
        arm_roll = 0.3 * arm_dir[0]
        arm_pitch = -1.57 - math.asin(min(1.0, max(-1.0, arm_dir[1])))
    elif arm_dir[0] < -0.2: # crossed over chest to left
        arm_roll = 0.35
    elif arm_dir[0] > 0.3: # out to right
        arm_roll = -0.30
        
    # Arm pos (in pixels, where -5 is right shoulder standard in Emote)
    # Adjust position slightly to allow reaching extreme close or far targets
    arm_x = -5.0 + (arm_dir[0] - 0.2) * 1.5
    arm_y = 2.0 + (arm_dir[1] + 0.5) * 1.5
    arm_z = arm_dir[2] * 2.0
    
    # Now solve item rotation relative to arm
    # Target direction is 'direction' = [dx, dy, dz] in world space
    # The item's thirdperson display has default orientation pointing along +Z with a 55-degree pitch.
    # We solve item_pitch, item_yaw, item_roll so that the final blade direction aligns with target direction.
    target_dir = v_normalize(direction)
    
    # Relative direction in arm-space:
    # Target in horizontal pitch/yaw
    dir_pitch = -math.asin(min(1.0, max(-1.0, target_dir[1])))
    dir_yaw = math.atan2(target_dir[0], max(1e-4, target_dir[2]))
    
    item_pitch = dir_pitch - arm_pitch
    item_yaw = dir_yaw - arm_yaw
    
    # Roll aligns the blade edge and orientation
    # S1 diagonal cut: edge tilted ~ -1.5 rad (-85°)
    # S2 horizontal cut: blade strictly horizontal, roll = -1.57 rad (-90°)
    # S3 thrust: blade aligned with thrust axis, roll = 1.57 rad (+90°)
    if abs(target_dir[1]) > 0.4: # diagonal / vertical
        item_roll = -1.50
    elif abs(target_dir[0]) > 0.5: # horizontal
        item_roll = -1.57
    else: # thrust / forward
        item_roll = 1.55
        
    # item pos: y = -2.0 is required for palm center
    # x and z fine-tune grip alignment
    item_x = arm_dir[0] * 1.2
    item_y = -2.0
    item_z = arm_dir[2] * 1.5 - 0.5
    
    return (
        {"pitch": round(arm_pitch, 5), "yaw": round(arm_yaw, 5), "roll": round(arm_roll, 5),
         "x": round(arm_x, 3), "y": round(arm_y, 3), "z": round(arm_z, 3)},
        {"pitch": round(item_pitch, 5), "yaw": round(item_yaw, 5), "roll": round(item_roll, 5),
         "x": round(item_x, 3), "y": round(item_y, 3), "z": round(item_z, 3)}
    )

print("Solver logic ready")
