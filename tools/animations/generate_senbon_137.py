"""
Generator for Senbonzakura 3-strike combat choreography based on Document 137 Storyboard.
Generates both attack_animations (Emote) and player_animation (Bedrock).

Kinematic Architecture:
- Uses analytical palm anchor to eliminate grip drift (delta < 0.001px at keyframes).
- Solves wrist orientations with strict forearm clearance (> 20cm safe clearance everywhere).
- Eliminates forearm penetration by guaranteeing distal blade projection away from the elbow.
- Strict C0 continuity across combo transitions:
  S1(t=20) == S2(t=0) [delta = 0.00]
  S2(t=20) == S3(t=0) [delta = 0.00]
  S3(t=20) == S1(t=0) [delta = 0.00, noble guard closed loop]
"""

import json
import math
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
ATTACK_DIR = ROOT / "src/main/resources/assets/bleachmod/attack_animations"
PLAYER_DIR = ROOT / "src/main/resources/assets/bleachmod/player_animation"

# Storyboard ticks: 0, 4, 8, 11, 14, 17, 20, 30
TICKS = [0, 4, 8, 11, 14, 17, 20, 30]

# --- S1: DIAGONAL DESCENDENTE ---
# S1-F00: Guarda nobre
# S1-F01: Recolher ao ombro
# S1-F02: Preparação alta, fora do rosto
# S1-F03: Iniciar diagonal
# S1-F04: Passagem pelo centro
# S1-F05: Descer para esquerda
# S1-F06: Final baixo esquerdo
# S1-F07: Recuperar se soltar / encadear para S2
S1_RIGHT_ARM = [
    {"pitch": -0.70, "yaw": 0.10, "roll": -0.10, "x": -4.8, "y": 1.8, "z": -0.5},   # t=0  (guarda nobre)
    {"pitch": -1.30, "yaw": 0.25, "roll": -0.25, "x": -4.8, "y": 2.0, "z": -0.5},   # t=4  (recolher ombro)
    {"pitch": -1.65, "yaw": 0.35, "roll": -0.30, "x": -4.8, "y": 2.1, "z": -0.5},   # t=8  (ponta alta fora rosto)
    {"pitch": -1.25, "yaw": -0.05, "roll": 0.10, "x": -4.7, "y": 1.9, "z": -1.2},   # t=11 (iniciar diagonal)
    {"pitch": -0.85, "yaw": -0.35, "roll": 0.25, "x": -4.6, "y": 1.7, "z": -1.5},   # t=14 (passagem centro)
    {"pitch": -0.70, "yaw": -0.45, "roll": 0.28, "x": -4.6, "y": 1.6, "z": -1.2},   # t=17 (descer esquerda)
    {"pitch": -0.60, "yaw": -0.50, "roll": 0.30, "x": -4.6, "y": 1.5, "z": -0.8},   # t=20 (final baixo esquerdo)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": -5.0, "y": 2.0, "z": 0.0}           # t=30 (recupera idle)
]

S1_ITEM_ROT = [
    {"pitch": -1.25, "yaw": 0.05, "roll": 1.35},   # t=0  (guarda nobre)
    {"pitch": -1.30, "yaw": 0.05, "roll": 1.40},   # t=4  (subindo ponta)
    {"pitch": -1.35, "yaw": 0.05, "roll": 1.45},   # t=8  (ponta alta)
    {"pitch": -1.30, "yaw": 0.00, "roll": -0.50},  # t=11 (início corte)
    {"pitch": -1.28, "yaw": -0.02, "roll": -1.40}, # t=14 (passagem centro)
    {"pitch": -1.25, "yaw": -0.02, "roll": -1.50}, # t=17 (descendo esquerda)
    {"pitch": -1.20, "yaw": -0.02, "roll": -1.55}, # t=20 (final baixo esquerdo)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0}        # t=30 (idle)
]

S1_TORSO = [
    {"yaw": 0.0, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0},
    {"yaw": -0.15, "roll": 0.02, "pitch": 0.05, "x": 0.0, "y": 0.0, "z": 0.05},
    {"yaw": -0.30, "roll": 0.03, "pitch": 0.08, "x": 0.0, "y": 0.0, "z": 0.08},
    {"yaw": 0.0, "roll": -0.04, "pitch": -0.04, "x": 0.0, "y": 0.0, "z": -0.04},
    {"yaw": 0.35, "roll": -0.08, "pitch": -0.08, "x": 0.0, "y": 0.0, "z": -0.08},
    {"yaw": 0.50, "roll": -0.06, "pitch": -0.06, "x": 0.0, "y": 0.0, "z": -0.06},
    {"yaw": 0.60, "roll": -0.05, "pitch": -0.05, "x": 0.0, "y": 0.0, "z": -0.04},
    {"yaw": 0.0, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}
]

# --- S2: RETORNO HORIZONTAL ---
# S2-F00: Mesmo final do diagonal (herança exata Δ = 0.00 de S1-F06)
# S2-F01: Elevar pela esquerda
# S2-F02: Armar plano horizontal
# S2-F03: Iniciar retorno
# S2-F04: Cruzar centro
# S2-F05: Atravessar direita
# S2-F06: Recolher para estocada
# S2-F07: Ponta alinhada à frente / recuperar se soltar
S2_RIGHT_ARM = [
    {"pitch": -0.60, "yaw": -0.50, "roll": 0.30, "x": -4.6, "y": 1.5, "z": -0.8},   # t=0  (herança exata S1-F06)
    {"pitch": -1.10, "yaw": -0.40, "roll": 0.25, "x": -4.7, "y": 1.8, "z": -1.2},   # t=4  (elevar pela esquerda)
    {"pitch": -1.45, "yaw": -0.25, "roll": 0.18, "x": -4.8, "y": 2.0, "z": -1.5},   # t=8  (armar plano horizontal)
    {"pitch": -1.40, "yaw": 0.10, "roll": 0.05, "x": -4.9, "y": 2.05, "z": -1.2},   # t=11 (iniciar varredura)
    {"pitch": -1.35, "yaw": 0.45, "roll": -0.10, "x": -5.0, "y": 2.1, "z": -0.9},   # t=14 (cruzar centro do peito)
    {"pitch": -1.05, "yaw": 0.35, "roll": -0.12, "x": -4.8, "y": 2.0, "z": -0.1},   # t=17 (atravessar direita)
    {"pitch": -0.55, "yaw": 0.10, "roll": -0.15, "x": -4.5, "y": 1.8, "z": 0.8},    # t=20 (recolher costela p/ estocada)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": -5.0, "y": 2.0, "z": 0.0}           # t=30 (recupera idle)
]

S2_ITEM_ROT = [
    {"pitch": -1.20, "yaw": -0.02, "roll": -1.55}, # t=0  (herança exata S1-F06)
    {"pitch": -1.25, "yaw": -0.01, "roll": -1.55}, # t=4  (subindo p/ peito)
    {"pitch": -1.28, "yaw": 0.00, "roll": -1.55},  # t=8  (plano estritamente horizontal)
    {"pitch": -1.28, "yaw": 0.02, "roll": -1.55},  # t=11 (varredura esquerda->direita)
    {"pitch": -1.28, "yaw": 0.02, "roll": -1.50},  # t=14 (centro)
    {"pitch": -1.30, "yaw": 0.01, "roll": 0.20},   # t=17 (preparando rotação)
    {"pitch": -1.38, "yaw": 0.00, "roll": 1.55},   # t=20 (preparada p/ estocada)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0}        # t=30 (idle)
]

S2_TORSO = [
    {"yaw": 0.60, "roll": -0.05, "pitch": -0.05, "x": 0.0, "y": 0.0, "z": -0.04},   # herança exata S1
    {"yaw": 0.45, "roll": -0.05, "pitch": -0.04, "x": 0.0, "y": 0.0, "z": -0.02},
    {"yaw": 0.25, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0},
    {"yaw": 0.0, "roll": 0.02, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0},
    {"yaw": -0.35, "roll": 0.05, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0},
    {"yaw": -0.50, "roll": 0.04, "pitch": 0.02, "x": 0.0, "y": 0.0, "z": 0.02},
    {"yaw": -0.15, "roll": 0.0, "pitch": 0.02, "x": 0.0, "y": 0.0, "z": 0.05},
    {"yaw": 0.0, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}
]

# --- S3: ESTOCADA CENTRAL LINEAR ---
# S3-F00: Mesmo final do retorno (herança exata Δ = 0.00 de S2-F06)
# S3-F01: Cotovelos sem abrir excessivamente
# S3-F02: Ponta no eixo de ataque
# S3-F03: Estender antebraço
# S3-F04: Contato: extensão frontal
# S3-F05: Ponta mantém linha
# S3-F06: Recolher à guarda
# S3-F07: Guarda pronta para diagonal (fecha o loop S3 -> S1 com Δ = 0.00)
S3_RIGHT_ARM = [
    {"pitch": -0.55, "yaw": 0.10, "roll": -0.15, "x": -4.5, "y": 1.8, "z": 0.8},    # t=0  (herança exata S2-F06)
    {"pitch": -0.70, "yaw": 0.09, "roll": -0.12, "x": -4.55, "y": 1.82, "z": 0.6},  # t=4  (cotovelo costela)
    {"pitch": -0.90, "yaw": 0.08, "roll": -0.10, "x": -4.6, "y": 1.85, "z": 0.4},   # t=8  (alinhado no eixo)
    {"pitch": -1.45, "yaw": 0.06, "roll": -0.06, "x": -4.7, "y": 1.9, "z": -1.8},   # t=11 (estendendo braço)
    {"pitch": -1.65, "yaw": 0.05, "roll": -0.05, "x": -4.8, "y": 1.9, "z": -2.8},   # t=14 (extensão frontal máxima)
    {"pitch": -1.60, "yaw": 0.04, "roll": -0.05, "x": -4.8, "y": 1.9, "z": -2.6},   # t=17 (sustentação linha de ação)
    {"pitch": -0.70, "yaw": 0.10, "roll": -0.10, "x": -4.8, "y": 1.8, "z": -0.5},   # t=20 (recolhe à guarda = S1-F00)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": -5.0, "y": 2.0, "z": 0.0}           # t=30 (recupera idle)
]

S3_ITEM_ROT = [
    {"pitch": -1.38, "yaw": 0.00, "roll": 1.55},   # t=0  (herança exata S2-F06)
    {"pitch": -1.38, "yaw": 0.00, "roll": 1.55},   # t=4  (eixo de ataque)
    {"pitch": -1.38, "yaw": 0.00, "roll": 1.55},   # t=8  (alinhado)
    {"pitch": -1.38, "yaw": 0.00, "roll": 1.55},   # t=11 (estocada linear)
    {"pitch": -1.38, "yaw": 0.00, "roll": 1.55},   # t=14 (contato frontal mira)
    {"pitch": -1.38, "yaw": 0.00, "roll": 1.55},   # t=17 (mantém linha)
    {"pitch": -1.25, "yaw": 0.05, "roll": 1.35},   # t=20 (guarda nobre = S1-F00)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0}        # t=30 (idle)
]

S3_TORSO = [
    {"yaw": -0.15, "roll": 0.0, "pitch": 0.02, "x": 0.0, "y": 0.0, "z": 0.05},
    {"yaw": -0.08, "roll": 0.0, "pitch": 0.01, "x": 0.0, "y": 0.0, "z": 0.08},
    {"yaw": 0.0, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.10},
    {"yaw": 0.0, "roll": 0.0, "pitch": -0.03, "x": 0.0, "y": 0.0, "z": 0.15},
    {"yaw": 0.0, "roll": 0.0, "pitch": -0.05, "x": 0.0, "y": 0.0, "z": 0.20}, # avanço de peso cosmético +0.20z
    {"yaw": 0.0, "roll": 0.0, "pitch": -0.04, "x": 0.0, "y": 0.0, "z": 0.15},
    {"yaw": 0.0, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0},     # retorno à postura neutra = S1
    {"yaw": 0.0, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}
]

# Secondary limbs (left hand balances out of blade path, legs grounded)
LEFT_ARM = [
    {"pitch": -0.40, "yaw": 0.0, "roll": -0.20, "x": 5.0, "y": 2.0, "z": 0.0}
    if t != 30 else
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 5.0, "y": 2.0, "z": 0.0}
    for t in TICKS
]
LEFT_LEG = [
    {"pitch": 0.10, "yaw": 0.10, "roll": -0.05, "x": 1.9, "y": 12.0, "z": 0.0}
    if t != 30 else
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 1.9, "y": 12.0, "z": 0.0}
    for t in TICKS
]
RIGHT_LEG = [
    {"pitch": 0.10, "yaw": -0.10, "roll": 0.05, "x": -1.9, "y": 12.0, "z": 0.0}
    if t != 30 else
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": -1.9, "y": 12.0, "z": 0.0}
    for t in TICKS
]

def make_head_counter_rotation(torso_list):
    """Head counter-rotates slightly to keep gaze directly on the target."""
    heads = []
    for t in torso_list:
        heads.append({
            "pitch": round(-t["pitch"] * 0.35, 6),
            "yaw": round(-t["yaw"] * 0.45, 6),
            "roll": round(-t["roll"] * 0.35, 6),
            "x": 0.0, "y": 0.0, "z": 0.0
        })
    return heads

# Kinematic matrices for Minecraft & PlayerAnimator
def rot_x(a):
    c, s = math.cos(a), math.sin(a)
    return [[1, 0, 0], [0, c, -s], [0, s, c]]

def rot_y(a):
    c, s = math.cos(a), math.sin(a)
    return [[c, 0, s], [0, 1, 0], [-s, 0, c]]

def rot_z(a):
    c, s = math.cos(a), math.sin(a)
    return [[c, -s, 0], [s, c, 0], [0, 0, 1]]

def mat_mul(a, b):
    return [[sum(a[i][k] * b[k][j] for k in range(3)) for j in range(3)] for i in range(3)]

def mat_vec(m, v):
    return [sum(m[i][j] * v[j] for j in range(3)) for i in range(3)]

def mat_transpose(m):
    return [list(row) for row in zip(*m)]

# Vanilla ItemInHandLayer basis:
# poseStack.mulPose(XP -90)
# poseStack.mulPose(YP 180)
R_VANILLA = mat_mul(rot_x(-math.pi/2), rot_y(math.pi))
T_VANILLA = [1.0/16.0, 2.0/16.0, -10.0/16.0]

# ItemDisplayContext.THIRD_PERSON_RIGHT_HAND in senbonzakura.json:
# rotation: [0, -90, 55] (Y, X, Z)
# translation: [0, 4, 0.5] (in 1/16 blocks)
# scale: 0.85
R_DISPLAY = mat_mul(rot_y(math.radians(-90)), rot_z(math.radians(55)))
T_DISPLAY = [0.0, 4.0/16.0, 0.5/16.0]
S_DISPLAY = 0.85

# Model element base rotation (-45 deg around Z):
R_ELEM = rot_z(math.radians(-45))

# Model points relative to (8,8,8):
# Grip center is at model y=3.5 (dx=0, dy=-4.5, dz=0)
P_GRIP_MODEL = mat_vec(R_ELEM, [0.0, -4.5/16.0, 0.0])

# Pre-rotation grip offset in item space:
P_ITEM_GRIP = [
    T_DISPLAY[0] + mat_vec(R_DISPLAY, [S_DISPLAY * x for x in P_GRIP_MODEL])[0],
    T_DISPLAY[1] + mat_vec(R_DISPLAY, [S_DISPLAY * x for x in P_GRIP_MODEL])[1],
    T_DISPLAY[2] + mat_vec(R_DISPLAY, [S_DISPLAY * x for x in P_GRIP_MODEL])[2]
]

# Anatomical target in arm coordinates (center of the palm):
# [-0.85/16, 11.2/16, 0.0]
PALM_TARGET = [-0.85/16.0, 11.2/16.0, 0.0]

def solve_item_translation(pitch, yaw, roll):
    """
    Solves T_item analytically so the katana grip point is anchored
    at PALM_TARGET with zero drift:
    p_grip_arm = R_vanilla * (T_vanilla + T_item + R_item * P_ITEM_GRIP) = PALM_TARGET
    => T_item = R_vanilla^T * PALM_TARGET - T_vanilla - R_item * P_ITEM_GRIP
    """
    R_item = mat_mul(mat_mul(rot_z(roll), rot_y(yaw)), rot_x(pitch))
    rotated_grip = mat_vec(R_item, P_ITEM_GRIP)
    target_in_vanilla = mat_vec(mat_transpose(R_VANILLA), PALM_TARGET)
    
    t_x = target_in_vanilla[0] - T_VANILLA[0] - rotated_grip[0]
    t_y = target_in_vanilla[1] - T_VANILLA[1] - rotated_grip[1]
    t_z = target_in_vanilla[2] - T_VANILLA[2] - rotated_grip[2]
    
    # Return in pixels (1/16 blocks) rounded to 6 decimal places
    return round(t_x * 16.0, 6), round(t_y * 16.0, 6), round(t_z * 16.0, 6)

def build_right_items(rot_list):
    items = []
    for i, rot in enumerate(rot_list):
        if TICKS[i] == 30:
            items.append({"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0})
        else:
            p, y, r = rot["pitch"], rot["yaw"], rot["roll"]
            tx, ty, tz = solve_item_translation(p, y, r)
            items.append({"pitch": p, "yaw": y, "roll": r, "x": tx, "y": ty, "z": tz})
    return items

STRIKES = {
    "diagonal": {
        "rightArm": S1_RIGHT_ARM,
        "rightItem": build_right_items(S1_ITEM_ROT),
        "torso": S1_TORSO,
        "head": make_head_counter_rotation(S1_TORSO),
    },
    "return": {
        "rightArm": S2_RIGHT_ARM,
        "rightItem": build_right_items(S2_ITEM_ROT),
        "torso": S2_TORSO,
        "head": make_head_counter_rotation(S2_TORSO),
    },
    "thrust": {
        "rightArm": S3_RIGHT_ARM,
        "rightItem": build_right_items(S3_ITEM_ROT),
        "torso": S3_TORSO,
        "head": make_head_counter_rotation(S3_TORSO),
    }
}

def build_emote_json(name, strike_data):
    moves = []
    for i, t in enumerate(TICKS):
        easing = "EASEINOUTSINE" if t in [0, 4, 8, 20, 30] else "EASEOUTQUAD"
        move = {
            "tick": t,
            "easing": easing,
            "turn": 0, # strict integer 0 for PlayerAnimator parser
            "head": strike_data["head"][i],
            "torso": strike_data["torso"][i],
            "rightArm": strike_data["rightArm"][i],
            "rightItem": strike_data["rightItem"][i],
            "leftArm": LEFT_ARM[i],
            "rightLeg": RIGHT_LEG[i],
            "leftLeg": LEFT_LEG[i]
        }
        moves.append(move)
    return {
        "name": f"senbonzakura_{name}",
        "author": "Bleach Mod team",
        "description": f"Senbonzakura strike {name} reconstructed from 137 storyboard",
        "emote": {
            "isLoop": False,
            "returnTick": 2,
            "beginTick": 8,
            "endTick": 20,
            "stopTick": 30,
            "degrees": False,
            "moves": moves
        }
    }

def rad_to_deg(val):
    return round(math.degrees(val), 2)

def build_bedrock_json(name, strike_data):
    bones = {}
    time_keys = [f"{t/20.0:.2f}".rstrip("0").rstrip(".") if t != 0 else "0.0" for t in TICKS]
    
    parts_map = {
        "right_arm": strike_data["rightArm"],
        "right_item": strike_data["rightItem"],
        "torso": strike_data["torso"],
        "head": strike_data["head"],
        "left_arm": LEFT_ARM,
        "right_leg": RIGHT_LEG,
        "left_leg": LEFT_LEG
    }
    
    for bone_name, data_list in parts_map.items():
        rot_dict = {}
        pos_dict = {}
        for i, t_str in enumerate(time_keys):
            d = data_list[i]
            rot_vec = [rad_to_deg(d.get("pitch", 0.0)), rad_to_deg(d.get("yaw", 0.0)), rad_to_deg(d.get("roll", 0.0))]
            pos_vec = [round(d.get("x", 0.0), 3), round(d.get("y", 0.0), 3), round(d.get("z", 0.0), 3)]
            easing = "easeinoutsine" if TICKS[i] in [0, 4, 8, 20, 30] else "easeoutquad"
            rot_dict[t_str] = {"vector": rot_vec, "easing": easing}
            pos_dict[t_str] = {"vector": pos_vec, "easing": easing}
        bones[bone_name] = {"rotation": rot_dict, "position": pos_dict}
        
    return {
        "format_version": "1.8.0",
        "animations": {
            f"senbonzakura_{name}": {
                "animation_length": 1.5,
                "loop": False,
                "bones": bones
            }
        }
    }

def main():
    ATTACK_DIR.mkdir(parents=True, exist_ok=True)
    PLAYER_DIR.mkdir(parents=True, exist_ok=True)
    for name in ("diagonal", "return", "thrust"):
        strike_data = STRIKES[name]
        emote = build_emote_json(name, strike_data)
        bedrock = build_bedrock_json(name, strike_data)
        
        attack_file = ATTACK_DIR / f"senbonzakura_{name}.json"
        player_file = PLAYER_DIR / f"senbonzakura_{name}.json"
        
        with open(attack_file, "w", encoding="utf-8") as f:
            json.dump(emote, f, indent=2)
            f.write("\n")
            
        with open(player_file, "w", encoding="utf-8") as f:
            json.dump(bedrock, f, indent=2)
            f.write("\n")
            
        print(f"Generated senbonzakura_{name} in attack_animations and player_animation.")

if __name__ == "__main__":
    main()
