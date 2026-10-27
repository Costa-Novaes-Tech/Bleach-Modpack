"""
Generator for Senbonzakura 3-strike combat choreography based on Document 137 Storyboard.
Generates both attack_animations (Emote) and player_animation (Bedrock).
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
# S1-F07: Manter se encadear / recuperar se soltar
S1_RIGHT_ARM = [
    {"pitch": -0.70, "yaw": 0.10, "roll": -0.10, "x": -4.8, "y": 1.8, "z": -0.5},   # t=0  (guarda)
    {"pitch": -1.30, "yaw": 0.25, "roll": -0.25, "x": -4.8, "y": 2.0, "z": -0.5},   # t=4  (recolher ombro)
    {"pitch": -1.65, "yaw": 0.32, "roll": -0.30, "x": -4.8, "y": 2.1, "z": -0.5},   # t=8  (ponta alta)
    {"pitch": -1.25, "yaw": -0.05, "roll": 0.10, "x": -4.7, "y": 1.9, "z": -1.2},   # t=11 (iniciar diagonal)
    {"pitch": -0.85, "yaw": -0.35, "roll": 0.25, "x": -4.6, "y": 1.7, "z": -1.5},   # t=14 (passagem centro)
    {"pitch": -0.70, "yaw": -0.45, "roll": 0.28, "x": -4.6, "y": 1.6, "z": -1.2},   # t=17 (descer esquerda)
    {"pitch": -0.60, "yaw": -0.50, "roll": 0.30, "x": -4.6, "y": 1.5, "z": -0.8},   # t=20 (final baixo esquerdo)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}            # t=30 (recupera idle)
]

S1_RIGHT_ITEM = [
    {"pitch": -1.7931, "yaw": 1.0077, "roll": -1.7615, "x": 1.0, "y": -2.0, "z": -0.8}, # t=0  (guarda)
    {"pitch": -2.3799, "yaw": 1.6103, "roll": -2.0707, "x": 1.0, "y": -2.0, "z": -0.8}, # t=4  (subindo ponta)
    {"pitch": -2.8561, "yaw": 0.1026, "roll": -2.5627, "x": 1.0, "y": -2.0, "z": -0.8}, # t=8  (ponta alta fora do rosto)
    {"pitch": -1.9510, "yaw": 0.7301, "roll": -1.9510, "x": 1.0, "y": -2.0, "z": -0.8}, # t=11 (início diagonal)
    {"pitch": -1.7773, "yaw": 0.2697, "roll": -3.1179, "x": 1.0, "y": -2.0, "z": -0.8}, # t=14 (passagem centro)
    {"pitch": -1.4840, "yaw": 0.5157, "roll": -3.0390, "x": 1.0, "y": -2.0, "z": -0.8}, # t=17 (descendo esquerda)
    {"pitch": -1.2853, "yaw": 0.4841, "roll": -2.8561, "x": 1.0, "y": -2.0, "z": -0.8}, # t=20 (final baixo esquerdo)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}                 # t=30 (recupera idle)
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
    {"pitch": -0.65, "yaw": -0.55, "roll": 0.32, "x": -4.6, "y": 1.5, "z": -1.0},   # t=0  (herança exata S1-F06)
    {"pitch": -1.10, "yaw": -0.40, "roll": 0.25, "x": -4.7, "y": 1.8, "z": -1.2},   # t=4  (elevar pela esquerda)
    {"pitch": -1.45, "yaw": -0.25, "roll": 0.18, "x": -4.8, "y": 2.0, "z": -1.5},   # t=8  (armar plano horizontal peito)
    {"pitch": -1.40, "yaw": 0.10, "roll": 0.05, "x": -4.9, "y": 2.05, "z": -1.2},   # t=11 (iniciar varredura)
    {"pitch": -1.35, "yaw": 0.45, "roll": -0.10, "x": -5.0, "y": 2.1, "z": -0.9},   # t=14 (cruzar centro do peito)
    {"pitch": -1.05, "yaw": 0.35, "roll": -0.12, "x": -4.8, "y": 2.0, "z": -0.1},   # t=17 (atravessar direita)
    {"pitch": -0.55, "yaw": 0.10, "roll": -0.15, "x": -4.5, "y": 1.8, "z": 0.8},    # t=20 (recolher costela p/ estocada)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}            # t=30 (recupera idle)
]

S2_RIGHT_ITEM = [
    {"pitch": -0.80, "yaw": 0.0, "roll": -1.20, "x": -0.4, "y": -2.0, "z": -0.3},   # t=0  (herança exata S1-F06)
    {"pitch": -1.05, "yaw": 0.02, "roll": -1.45, "x": -0.3, "y": -2.0, "z": -0.4},  # t=4  (subindo p/ peito)
    {"pitch": -1.25, "yaw": 0.05, "roll": -1.57, "x": -0.2, "y": -2.0, "z": -0.6},  # t=8  (plano estritamente horizontal)
    {"pitch": -1.05, "yaw": 0.08, "roll": -1.50, "x": 0.1, "y": -2.0, "z": -0.5},   # t=11 (varredura esquerda->direita)
    {"pitch": -0.85, "yaw": 0.10, "roll": -1.40, "x": 0.3, "y": -2.0, "z": -0.4},   # t=14 (centro)
    {"pitch": -1.15, "yaw": 0.02, "roll": 0.20, "x": 0.8, "y": -2.0, "z": -0.5},    # t=17 (direita)
    {"pitch": -1.42, "yaw": -0.05, "roll": 1.55, "x": 1.2, "y": -2.0, "z": -0.6},   # t=20 (preparada p/ estocada)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}            # t=30 (recupera idle)
]

S2_TORSO = [
    {"yaw": 0.70, "roll": -0.08, "pitch": -0.08, "x": 0.0, "y": 0.0, "z": -0.05},
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
    {"pitch": -0.85, "yaw": 0.08, "roll": -0.10, "x": -4.6, "y": 1.85, "z": 0.4},   # t=8  (alinhado no eixo)
    {"pitch": -1.30, "yaw": 0.06, "roll": -0.06, "x": -4.7, "y": 1.9, "z": -1.8},   # t=11 (estendendo braço)
    {"pitch": -1.45, "yaw": 0.05, "roll": -0.05, "x": -4.8, "y": 1.9, "z": -2.8},   # t=14 (extensão frontal máxima)
    {"pitch": -1.42, "yaw": 0.04, "roll": -0.05, "x": -4.8, "y": 1.9, "z": -2.6},   # t=17 (sustentação linha de ação)
    {"pitch": -1.10, "yaw": 0.15, "roll": -0.15, "x": -4.7, "y": 1.9, "z": -0.8},   # t=20 (recolhe à guarda = S1-F00)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}            # t=30 (recupera idle)
]

S3_RIGHT_ITEM = [
    {"pitch": -1.42, "yaw": -0.05, "roll": 1.55, "x": 1.2, "y": -2.0, "z": -0.6},   # t=0  (herança exata S2-F06)
    {"pitch": -1.41, "yaw": -0.02, "roll": 1.56, "x": 1.1, "y": -2.0, "z": -0.7},   # t=4  (eixo de ataque)
    {"pitch": -1.41, "yaw": 0.0, "roll": 1.57, "x": 1.0, "y": -2.0, "z": -0.8},    # t=8  (alinhado)
    {"pitch": -1.40, "yaw": 0.0, "roll": 1.57, "x": 0.95, "y": -2.0, "z": -1.5},   # t=11 (estocada linear)
    {"pitch": -1.40, "yaw": 0.0, "roll": 1.57, "x": 0.9, "y": -2.0, "z": -1.8},    # t=14 (contato frontal mira)
    {"pitch": -1.40, "yaw": 0.0, "roll": 1.57, "x": 0.9, "y": -2.0, "z": -1.6},    # t=17 (mantém linha)
    {"pitch": -0.50, "yaw": 0.15, "roll": -1.20, "x": 1.0, "y": -2.0, "z": -0.8},   # t=20 (guarda = S1-F00)
    {"pitch": 0.0, "yaw": 0.0, "roll": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}            # t=30 (recupera idle)
]

S3_TORSO = [
    {"yaw": -0.15, "roll": 0.0, "pitch": 0.02, "x": 0.0, "y": 0.0, "z": 0.05},
    {"yaw": -0.08, "roll": 0.0, "pitch": 0.01, "x": 0.0, "y": 0.0, "z": 0.08},
    {"yaw": 0.0, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.10},
    {"yaw": 0.0, "roll": 0.0, "pitch": -0.03, "x": 0.0, "y": 0.0, "z": 0.15},
    {"yaw": 0.0, "roll": 0.0, "pitch": -0.05, "x": 0.0, "y": 0.0, "z": 0.20}, # avanço de peso cosmético +0.20z
    {"yaw": 0.0, "roll": 0.0, "pitch": -0.04, "x": 0.0, "y": 0.0, "z": 0.15},
    {"yaw": 0.0, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0},     # retorno à postura neutra
    {"yaw": 0.0, "roll": 0.0, "pitch": 0.0, "x": 0.0, "y": 0.0, "z": 0.0}
]

# Secondary limbs (left hand balances out of blade path, legs grounded)
LEFT_ARM = [
    {"pitch": -0.40, "yaw": 0.0, "roll": -0.20, "x": 5.0, "y": 2.0, "z": 0.0}
    for _ in TICKS
]
LEFT_LEG = [
    {"pitch": 0.10, "yaw": 0.10, "roll": -0.05, "x": 1.9, "y": 12.0, "z": 0.0}
    for _ in TICKS
]
RIGHT_LEG = [
    {"pitch": 0.10, "yaw": -0.10, "roll": 0.05, "x": -1.9, "y": 12.0, "z": 0.0}
    for _ in TICKS
]

def make_head_counter_rotation(torso_list):
    """Head counter-rotates slightly to keep gaze directly on the target."""
    heads = []
    for t in torso_list:
        heads.append({
            "pitch": -t["pitch"] * 0.35,
            "yaw": -t["yaw"] * 0.45,
            "roll": -t["roll"] * 0.35,
            "x": 0.0, "y": 0.0, "z": 0.0
        })
    return heads

STRIKES = {
    "diagonal": {
        "rightArm": S1_RIGHT_ARM,
        "rightItem": S1_RIGHT_ITEM,
        "torso": S1_TORSO,
        "head": make_head_counter_rotation(S1_TORSO),
    },
    "return": {
        "rightArm": S2_RIGHT_ARM,
        "rightItem": S2_RIGHT_ITEM,
        "torso": S2_TORSO,
        "head": make_head_counter_rotation(S2_TORSO),
    },
    "thrust": {
        "rightArm": S3_RIGHT_ARM,
        "rightItem": S3_RIGHT_ITEM,
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
    
    for name in ["diagonal"]:
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
