"""Author a continuous solar volume variant; preserve all v10/runtime originals.

One shared projected material crosses core rows, so row UV islands cannot create
different coloured stripes. GeckoLib's installed format supports optional_box_uv.
Outputs are laboratory assets until editor and Forge A/B validation.
"""
from __future__ import annotations
import base64, copy, hashlib, io, json, math, uuid
from pathlib import Path
from PIL import Image

HERE = Path(__file__).resolve().parent
REPO = HERE.parents[4]
NAME = "ryujin_bankai_continuous_sun_v13_lab"
CENTER = (0, 80, 50)
SOLAR = {"sun_root", "sun_core", "inner_swirl", "mid_swirl", "outer_swirl", "broken_rim",
         *(f"flame_{i:02d}" for i in range(13))}


def uid(label):
    return str(uuid.uuid5(uuid.NAMESPACE_URL, NAME + ":" + label))


def rounded(vector):
    return [round(v, 5) for v in vector]


def bake_volume_faces(image, cubes, polished=False):
    """Evaluate one 3D colour field on every face, with padded independent UVs.

    A radial 2D gradient projected anew onto X/Y/Z gives different temperatures
    at shared corners. This field uses the actual position instead of face axes.
    """
    cursor_x,cursor_y,shelf_height=0,640,0
    def colour(x,y,z):
        u,v,w=x/19,(y-80)/19,(z-50)/14
        n=.5+.18*math.sin(u+2.2*math.sin(v))+.14*math.sin(1.7*v+1.3*math.cos(w))+.1*math.cos(w*2.1-u)
        n=max(0,min(1,n))
        if polished:
            radial = min(1, math.hypot(x / 75, (y - 80) / 75, (z - 50) / 80))
            return (255, int(227 + 25*n + 11*(1-radial)), int(144 + 76*n + 32*(1-radial)), 255)
        return (255,int(215+37*n),int(133+112*n),255)
    def point(cube,face,s,t):
        x0,y0,z0=cube['from'];x1,y1,z1=cube['to'];dx,dy,dz=x1-x0,y1-y0,z1-z0
        if face=='north':return (x1-s*dx,y1-t*dy,z0)
        if face=='south':return (x0+s*dx,y1-t*dy,z1)
        if face=='east':return (x1,y1-t*dy,z1-s*dz)
        if face=='west':return (x0,y1-t*dy,z0+s*dz)
        if face=='up':return (x0+s*dx,y1,z0+t*dz)
        return (x0+s*dx,y0,z1-t*dz)
    pending=[]
    for cube in cubes:
        dx,dy,dz=[b-a for a,b in zip(cube['from'],cube['to'])]
        for face,(width,height) in {'north':(dx,dy),'south':(dx,dy),'east':(dz,dy),'west':(dz,dy),'up':(dx,dz),'down':(dx,dz)}.items():
            # Continuous low-frequency material needs no extra atlas height when
            # the silhouette voxel grid is refined. Padding remains one pixel.
            pending.append((max(2,math.ceil(height*.75)),max(2,math.ceil(width*.75)),cube,face))
    # Tall faces first avoids wasting a tall shelf on subsequent tiny faces.
    for height,width,cube,face in sorted(pending,key=lambda item:(-item[0],-item[1],item[2]['name'],item[3])):
            if cursor_x+width+2>256:
                cursor_x=0;cursor_y+=shelf_height;shelf_height=0
            assert cursor_y+height+2<2624,'Keep the ribbon region intact'
            ox,oy=cursor_x+1,cursor_y+1
            for py in range(-1,height+1):
                for px in range(-1,width+1):
                    s=max(0,min(1,px/(width-1)));t=max(0,min(1,py/(height-1)))
                    image.putpixel((ox+px,oy+py),colour(*point(cube,face,s,t)))
            cube['faces'][face]={'uv':[ox,oy,ox+width,oy+height],'texture':0}
            cursor_x+=width+2;shelf_height=max(shelf_height,height+2)
    return cursor_y+shelf_height


def texture(base):
    img = Image.open(io.BytesIO(base64.b64decode(base.split(",", 1)[1]))).convert("RGBA")
    # These dedicated regions are outside the original packed UV footprint.
    for kind, top in (("core", 2048), ("mantle", 2336), ("filament", 2624), ("plume",2912)):
        for y in range(256):
            for x in range(256):
                u, v = (x - 127.5) / 127.5, (127.5 - y) / 127.5
                r = math.hypot(u, v)
                a = math.atan2(v, u)
                convection = .5 + .22 * math.sin(17*u + 5*math.sin(6*v)) + .15 * math.sin(23*v-4*u) + .09*math.cos(19*a+11*r)
                if kind == "core":
                    edge = min(1, r)
                    rgb = (255, int(238-100*edge*edge+22*convection), int(218-190*edge*edge+18*convection))
                    alpha = 255
                elif kind == "mantle":
                    heat = max(0, 1-r)
                    rgb = (255, int(130+63*heat+27*convection), int(15+44*heat))
                    contour=1+.035*math.sin(7*a)+.045*math.sin(13*a+.5)+.02*math.sin(21*a)
                    alpha = int(255*max(0, min(1, (contour-r)*9)))
                else:
                    # Narrow hot flow within a broad orange ribbon; no white rectangular base.
                    hot = max(0, 1-abs(u)*1.7)
                    flow = .82+.18*math.sin(13*v+3*u)
                    rgb = (255, int(153+79*hot*flow), int(25+82*hot*flow))
                    fade_end=max(0,min(1,(y-8)/28,(248-y)/28))
                    alpha = int(178*max(0,min(1,(1-abs(u))*3))*fade_end)
                    if kind=="plume":
                        rise=1-y/255
                        centre=.19*math.sin(rise*5.3)
                        width=.90*(1-rise)**.68+.015
                        alpha=int(255*max(0,min(1,(1-abs((u-centre)/width))*5))*(1-rise**10))
                img.putpixel((x, top+y), (*[max(0, min(255, c)) for c in rgb], alpha))
    out = io.BytesIO(); img.save(out, format="PNG")
    return img, out.getvalue()


def cube(name, centre, size, *, material="filament", rotation=0, projection_radius=None):
    start = [c-s*.5 for c, s in zip(centre, size)]
    end = [c+s*.5 for c, s in zip(centre, size)]
    top = {"core": 2048, "mantle": 2336, "filament": 2624,"plume":2912}[material]
    if projection_radius:
        def u(x): return 128 + 126*x/projection_radius
        def v(y): return top+128-126*(y-80)/projection_radius
        front = [u(start[0]), v(end[1]), u(end[0]), v(start[1])]
        faces = {face: {"uv": rounded(front), "texture": 0} for face in ("north", "south")}
        faces["north"]["uv"]=rounded([u(end[0]),v(end[1]),u(start[0]),v(start[1])])
        def depth_u(z):return 128+126*(z-50)/40
        for face in ("east","west"):
            faces[face]={"uv":rounded([depth_u(start[2]),v(end[1]),depth_u(end[2]),v(start[1])]),"texture":0}
        faces["east"]["uv"]=rounded([depth_u(end[2]),v(end[1]),depth_u(start[2]),v(start[1])])
        for face in ("up","down"):
            faces[face]={"uv":rounded([u(start[0]),top+depth_u(start[2]),u(end[0]),top+depth_u(end[2])]),"texture":0}
    else:
        faces = {face: {"uv": [18, top+10, 238, top+246], "texture": 0}
                 for face in ("north", "south", "east", "west", "up", "down")}
        for face in ("east","west","up","down"):
            faces[face]["uv"]=[0,top,1,top+1] # Transparent border: no metallic sidewalls on an energy ribbon.
    return {"name": name, "uuid": uid(name), "type": "cube", "box_uv": False,
            "from": rounded(start), "to": rounded(end), "origin": rounded(centre),
            "rotation": [0, 0, round(rotation, 5)], "faces": faces,
            "autouv": 0, "export": True, "color": 5, "render_order": "default", "locked": False}


def core():
    # A true filled voxel ellipsoid, greedily merged into cuboids. No latitude plates.
    step=5
    xy_steps=math.ceil(72/step);z_steps=math.ceil(40/step)
    occupied={(x,y,z) for x in range(-xy_steps,xy_steps) for y in range(-xy_steps,xy_steps) for z in range(-z_steps,z_steps)
              if ((x+.5)*step/72)**2+((y+.5)*step/72)**2+((z+.5)*step/40)**2<=1}
    cubes=[]
    while occupied:
        x,y,z=min(occupied,key=lambda p:(p[2],p[1],p[0]))
        nx=1
        while (x+nx,y,z) in occupied:nx+=1
        ny=1
        while all((xx,y+ny,z) in occupied for xx in range(x,x+nx)):ny+=1
        nz=1
        while all((xx,yy,z+nz) in occupied for xx in range(x,x+nx) for yy in range(y,y+ny)):nz+=1
        occupied.difference_update((xx,yy,zz) for xx in range(x,x+nx) for yy in range(y,y+ny) for zz in range(z,z+nz))
        c=((x+nx*.5)*step,80+(y+ny*.5)*step,50+(z+nz*.5)*step)
        cubes.append(cube(f"solar_volume_{len(cubes):03d}",c,(nx*step,ny*step,nz*step),
                          material="core",projection_radius=72.1))
    return cubes


def ribbon(name, radius, depth, count, phase, turns, *, finer=False):
    cubes = []
    for i in range(count):
        t = (i+.5)/count
        a = phase+t*math.tau*turns
        r = radius*(.55+.53*t) + 2.8*math.sin(2*a+.3) + 1.2*math.sin(5*a)
        width = (8.5+4.8*(.5+.5*math.sin(a*3+.8)))*(.60+.40*math.sin(math.pi*t)**.4)
        length = radius*math.tau*turns/count*1.23
        if finer:
            width *= .74
            length *= 1.13
        z = 50-40*math.sqrt(max(0,1-(r/72)**2))-6.0+.3*math.sin(a*2.3)+i*.007
        e=cube(f"{name}_{i:02d}", (math.cos(a)*r,80+math.sin(a)*r,z),
               (width,length,1.2), rotation=math.degrees(a))
        for face in ("north","south"):
            e["faces"][face]["uv"]=[18,2624+8+i*240/count,238,2624+8+(i+1)*240/count]
        cubes.append(e)
    return cubes


def plumes(groups):
    result = {}
    for i in range(13):
        name = f"flame_{i:02d}"
        # Preserve the source pivots/controllers, curve the actual tongue around each joint.
        x,y,z = groups[name]["origin"]
        a = math.atan2(y-80,x)
        cubes = []
        for segment in range(4):
            t = segment/3
            distance = 13+segment*7
            bent = a + .42*t*t + .08*math.sin(i*1.7)
            radius = math.hypot(x,y-80)+distance
            c = (math.cos(bent)*radius,80+math.sin(bent)*radius,z+.4*segment)
            cubes.append(cube(f"plume_{i:02d}_{segment}",c,
                              (7.5*(1-.72*t),16*(1-.25*t),2.6), material="plume",
                              rotation=math.degrees(bent)-90))
        result[name] = cubes
    return result


def encode_model(model, png, name):
    model["name"] = model["model_identifier"] = name
    model["textures"][0].update({"name":NAME+".png", "path":"", "saved":False,
                                    "source":"data:image/png;base64,"+base64.b64encode(png).decode()})
    (HERE/(name+".bbmodel")).write_text(json.dumps(model,indent=2),encoding="utf-8")


def main():
    model = json.loads((HERE/"ryujin_bankai_sustained_sun_v10_lab.bbmodel").read_text())
    groups = {g["name"]:g for g in model["groups"]}
    image, png = texture(model["textures"][0]["source"])
    changes = {"sun_root":[],"flash_corona":[], "sun_core":core(),
               "inner_swirl":ribbon("inner_flow",49,23,28,.1,1.07),
               "mid_swirl":ribbon("middle_flow",66,26,32,1.9,1.02),
               "outer_swirl":ribbon("outer_flow",76,30,36,3.5,1.01),
               "broken_rim":[],
               **{f"flame_{i:02d}":[] for i in range(13)},
               **{f"absorb_spark_{i:02d}":[] for i in range(12)}}
    baked_bottom=bake_volume_faces(image,changes['sun_core'])
    image_bytes=io.BytesIO();image.save(image_bytes,format='PNG');png=image_bytes.getvalue()
    by_uuid = {g["uuid"]:name for name,g in groups.items()}
    removed = set()
    def replace(node):
        if isinstance(node,str): return
        name=by_uuid[node["uuid"]]
        for child in node.get("children",[]):
            if not isinstance(child,str): replace(child)
        if name in changes:
            removed.update(c for c in node.get("children",[]) if isinstance(c,str))
            node["children"]=[c for c in node.get("children",[]) if not isinstance(c,str)]+[e["uuid"] for e in changes[name]]
    for node in model["outliner"]: replace(node)
    model["elements"]=[e for e in model["elements"] if e["uuid"] not in removed]+[e for c in changes.values() for e in c]
    # Keep the exact authored clock and existing public bones for this first geometry A/B.
    model["animations"][0]["name"]="animation.ryujin.continuous_sun_v13_lab"
    encode_model(model,png,NAME+"_scene")
    runtime=json.loads((REPO/"src/main/resources/assets/bleachmod/geo/ryujin_bankai_sun.geo.json").read_text())
    names={b["name"] for b in runtime["minecraft:geometry"][0]["bones"]}
    keep={groups[name]["uuid"] for name in names}
    def prune(node):
        if isinstance(node,str):return node
        if node["uuid"] not in keep:return None
        return {**node,"children":[v for c in node.get("children",[]) if (v:=prune(c)) is not None]}
    model["outliner"]=[v for n in model["outliner"] if (v:=prune(n)) is not None]
    def elements(node):
        if isinstance(node,str):return {node}
        return set().union(*(elements(c) for c in node.get("children",[]))) if node.get("children") else set()
    used=set().union(*(elements(n) for n in model["outliner"]))
    model["elements"]=[e for e in model["elements"] if e["uuid"] in used]
    model["groups"]=[g for g in model["groups"] if g["uuid"] in keep]
    model["animations"][0]["animators"]={k:v for k,v in model["animations"][0]["animators"].items() if k in keep}
    encode_model(model,png,NAME)
    (HERE/(NAME+".png")).write_bytes(png)
    # The geometry is VFX only; source actor texture regions are never referenced by it.
    glow=Image.new("RGBA",image.size)
    glow.paste(image.crop((0,640,256,3200)),(0,640))
    glow.save(HERE/(NAME+"_glowmask.png"))
    # Native editor export is authoritative; this manifest is structural, not visual approval.
    report={"source":"ryujin_bankai_sustained_sun_v10_lab.bbmodel","name":NAME,
            "cubes":len(model["elements"]),"bones":len(model["groups"]),"clip_seconds":15,
            "texture_size":image.size,"texture_sha256":hashlib.sha256(png).hexdigest(),
            "world_position_bake_bottom":baked_bottom,
            "runtime_adopted":False,"pending":["native UV/model checks","exact export","Forge comparison","human acceptance"]}
    assert report["cubes"]<=300
    assert len({e["uuid"] for e in model["elements"]})==len(model["elements"])
    (HERE/(NAME+".json")).write_text(json.dumps(report,indent=2))
    print(json.dumps(report,indent=2))


if __name__=="__main__":main()
