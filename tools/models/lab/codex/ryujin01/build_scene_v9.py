"""Derive a separate editable flame-wall laboratory scene; preserve v7 sources.

This model is a staging mannequin, not a replacement player skin. The Java
runtime keeps the player/item and the existing sun separate from this scene.
"""
from __future__ import annotations
import copy, json, math, uuid
from pathlib import Path

ROOT = Path(__file__).resolve().parent
NAME = "ryujin_bankai_flame_wall_v9_lab"

def uid(label): return str(uuid.uuid5(uuid.NAMESPACE_URL, NAME + ":" + label))
def key(channel, time, vector, label):
    return {"channel":channel,"time":time,"uuid":uid(label),"color":-1,
            "interpolation":"linear","data_points":[dict(zip(("x","y","z"),map(str,vector)))]}

def main():
    model=json.loads((ROOT/"ryujin_bankai_activation_scene_v7_lab.bbmodel").read_text())
    model["name"]=model["model_identifier"]=NAME
    groups={g["name"]:g for g in model["groups"]}
    katana=groups["katana"]
    shift=[-1,2,5]
    katana["origin"]=[a+b for a,b in zip(katana["origin"],shift)]
    katana["rotation"]=[0,0,-105]
    for e in model["elements"]:
        if e["name"].startswith("katana_"):
            for field in ("from","to","origin"):
                e[field]=[a+b for a,b in zip(e[field],shift)]
    animation=model["animations"][0]
    animation["name"]="animation.ryujin.flame_wall_v9_lab"
    stable={"chest","head","arm_upper_right","arm_lower_right","hand_right","katana",
            "arm_upper_left","arm_lower_left","hand_left"}
    for a in animation["animators"].values():
        if a["name"] in stable:
            preparation={"chest":[3,0,0],"head":[-4,0,0],
                         "arm_upper_right":[18,0,0],"arm_lower_right":[-12,0,0],
                         "arm_upper_left":[-10,0,0]}.get(a["name"],[0,0,0])
            a["keyframes"]=[key("rotation",t,v,a["name"]+str(t)) for t,v in
                ((0,preparation),(.8,[0,0,0]),(5.6,[0,0,0]),(8,[0,0,0]),
                 (10.5,[0,0,0]),(12,preparation))]
            for frame in a["keyframes"]: frame["interpolation"]="catmullrom"
    root_node=next(node for node in model["outliner"] if isinstance(node,dict) and node["uuid"]==groups["root"]["uuid"])
    # Reuse existing painted flame surfaces; no new raster material is fabricated.
    template=next(e for e in model["elements"] if "flame" in e["name"].lower())
    for i in range(12):
        x=-24+i*48/11
        g=copy.deepcopy(groups["sun_root"])
        g.update({"name":f"rear_flame_{i:02d}","uuid":uid(f"flame-{i}"),
                  "origin":[x,0,18+(i%3)*3],"rotation":[0,0,0]})
        model["groups"].append(g)
        children=[]
        for j in range(2):
            e=copy.deepcopy(template)
            w=2.8-j*.8; h=12+(i*7%13)+j*6
            e.update({"name":f"rear_flame_{i:02d}_{j}","uuid":uid(f"flame-{i}-{j}"),
                      "from":[x-w/2,0,g["origin"][2]-1+j*.4],
                      "to":[x+w/2,h,g["origin"][2]+1+j*.4],"origin":g["origin"],
                      "rotation":[0,0,(-1 if i%2 else 1)*(5+j*7)]})
            model["elements"].append(e);children.append(e["uuid"])
        root_node["children"].append({"uuid":g["uuid"],"isOpen":False,"children":children})
        frames=[]
        for t in (0,.8,1.4,2,2.8,3.5,4.2,5,5.6,5.8,6.2,6.6,7.1,12):
            ignition=max(0,min(1,(t-.8)/1.2))
            drain=1-max(0,min(1,(t-5.8)/1.3))
            scale=max(.001,ignition*drain)
            frames.append(key("scale",t,[scale,scale*(.85+.15*math.sin(t*4+i)),scale],f"s-{i}-{t}"))
            frames.append(key("rotation",t,[2*math.sin(t*3+i),8*math.sin(t*2+i),6*math.sin(t*4+i)],f"r-{i}-{t}"))
        animation["animators"][g["uuid"]]={"name":g["name"],"type":"bone","keyframes":frames}
    path=ROOT/(NAME+".bbmodel")
    path.write_text(json.dumps(model,ensure_ascii=False,indent=2),encoding="utf-8")
    manifest={"name":NAME,"source":"ryujin_bankai_activation_scene_v7_lab.bbmodel",
              "cubes":len(model["elements"]),"bones":len(model["groups"]),"duration_seconds":12,
              "front":"-Z","rear_flames":"+Z","actor_right":"+X in this source mannequin",
              "purpose":"editable staging model, not runtime export or player skin",
              "pending":"runtime adoption and artistic QA; replace staging fire with runtime particles/materials"}
    (ROOT/(NAME+".json")).write_text(json.dumps(manifest,indent=2),encoding="utf-8")
    assert len({e["uuid"] for e in model["elements"]})==len(model["elements"])
    assert len({g["uuid"] for g in model["groups"]})==len(model["groups"])
    assert len(animation["animators"])==len(model["groups"])
    # A separate body-only project makes profile/hand inspection readable,
    # without the sun's much larger framing bounds hiding the mannequin.
    pose=copy.deepcopy(model)
    kept={g["uuid"] for g in model["groups"][:21]}
    def prune(node):
        if isinstance(node,str): return node
        if node["uuid"] not in kept: return None
        return {**node,"children":[v for ch in node.get("children",[]) if (v:=prune(ch)) is not None]}
    pose["outliner"]=[v for node in pose["outliner"] if (v:=prune(node)) is not None]
    def cubes(node):
        if isinstance(node,str): return {node}
        return set().union(*(cubes(ch) for ch in node.get("children",[])))
    used=set().union(*(cubes(node) for node in pose["outliner"]))
    pose["elements"]=[e for e in pose["elements"] if e["uuid"] in used]
    pose["groups"]=[g for g in pose["groups"] if g["uuid"] in kept]
    pose["animations"][0]["animators"]={k:v for k,v in pose["animations"][0]["animators"].items() if k in kept}
    pose["name"]=pose["model_identifier"]=NAME+"_pose"
    (ROOT/(NAME+"_pose.bbmodel")).write_text(json.dumps(pose,indent=2),encoding="utf-8")
    print(json.dumps(manifest,indent=2))

if __name__=="__main__": main()
