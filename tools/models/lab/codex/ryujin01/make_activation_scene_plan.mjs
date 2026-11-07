// Combined timing study: mannequin + reduced preview copy of the authored sun.
// Source sun remains full size; this scene uses 0.6 scale to judge composition.
import { readFile, writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const name = 'ryujin_bankai_activation_scene_v5_lab';
const pose = JSON.parse(await readFile(join(here, 'ryujin_bankai_pose_lab.bbmodel'), 'utf8'));
const sun = JSON.parse(await readFile(join(here, 'ryujin_sun_vortex_alpha_lab.bbmodel'), 'utf8'));
const poseClip = JSON.parse(await readFile(join(here, 'ryujin_bankai_pose_lab.animation.json'), 'utf8'));
const sunClip = JSON.parse(await readFile(join(here, 'ryujin_sun_vortex_alpha_lab.animation.json'), 'utf8'));
const scale = .6, offset = [0, 80, 50], round = n => Math.round(n * 100) / 100;
const moved = v => v.map((n,i) => round(n * scale + offset[i]));
const groups = [], cubes = [];

function appendModel(model, isSun) {
  const byGroup = new Map(model.groups.map(g => [g.uuid, g]));
  const byCube = new Map(model.elements.map(c => [c.uuid, c]));
  const seenGroups = new Set(), seenCubes = new Set();
  function visit(node, parent) {
    if (typeof node === 'string') {
      const c = byCube.get(node);
      if (!c) throw new Error(`Unresolved cube ${node}`);
      seenCubes.add(node);
      const cube = { name: c.name, parent, from: isSun ? moved(c.from) : c.from,
        to: isSun ? moved(c.to) : c.to, origin: isSun ? moved(c.origin) : c.origin,
        rotation: c.rotation || [0,0,0] };
      cubes.push(cube);
      return;
    }
    const g = byGroup.get(node.uuid);
    if (!g) throw new Error(`Unresolved group ${node.uuid}`);
    seenGroups.add(node.uuid);
    groups.push({ name: g.name, parent, origin: isSun ? moved(g.origin) : g.origin,
      rotation: g.rotation || [0,0,0] });
    for (const child of node.children || []) visit(child, g.name);
  }
  for (const root of model.outliner) visit(root, undefined);
  if (seenGroups.size !== model.groups.length || seenCubes.size !== model.elements.length)
    throw new Error('Outliner did not resolve complete source model');
}
appendModel(pose, false);
appendModel(sun, true);

// Four translucent atlas islands will act as a warm reflection on the mannequin.
// They preview lighting direction but do not illuminate Minecraft blocks.
groups.push({ name:'player_fire_light', parent:'root', origin:[0,18,-3] });
for (const [cubeName,from,to] of [
  ['heat_chest',[-3.7,15.2,-2.75],[3.7,22.8,-2.48]],
  ['heat_face',[-3.5,25.0,-4.34],[3.5,30.9,-4.12]],
  ['heat_arm_right',[4.25,17.1,-2.47],[7.75,22.3,-2.20]],
  ['heat_arm_left',[-7.75,17.1,-2.47],[-4.25,22.3,-2.20]],
]) cubes.push({name:cubeName,parent:'player_fire_light',from,to,origin:[(from[0]+to[0])/2,(from[1]+to[1])/2,(from[2]+to[2])/2],rotation:[0,0,0]});

// A brief modeled burst behind the player, before the spiral fills in.
groups.push({ name: 'flash_corona', parent: 'sun_root', origin: offset });
for (let i=0; i<14; i++) {
  const angle = i * 360/14 + (i%3-1)*4;
  const a = angle * Math.PI/180;
  const radius = 56 + i%3*3, length = 45 + i%4*12;
  const cx = radius*Math.cos(a), cy = radius*Math.sin(a);
  cubes.push({ name: `flash_ray_${String(i).padStart(2,'0')}`, parent: 'flash_corona',
    from: [round(cx-length/2),round(offset[1]+cy-3.8),48+i%3*.24],
    to: [round(cx+length/2),round(offset[1]+cy+3.8),52+i%3*.24],
    origin: [round(cx),round(offset[1]+cy),50+i%3*.24], rotation:[0,0,round(angle)] });
}

// Separate moving embers travel through 3D toward the blade during absorption.
const absorptionSparks=[];
for (let i=0; i<12; i++) {
  const angle=(i*30+11)*Math.PI/180, radius=54+(i%4)*8;
  const start=[round(radius*Math.cos(angle)),round(offset[1]+radius*Math.sin(angle)),round(50+(i%3-1)*7)];
  const target=[round(13+(i%3-1)*2),round(9+i%4*.7),round(-7+(i%3-1)*1.5)];
  const bone=`absorb_spark_${String(i).padStart(2,'0')}`;
  groups.push({name:bone,parent:'root',origin:start});
  const w=2.2+i%3*.55;
  cubes.push({name:`${bone}_ember`,parent:bone,
    from:[round(start[0]-w/2),round(start[1]-w/2),round(start[2]-w/2)],
    to:[round(start[0]+w/2),round(start[1]+w/2),round(start[2]+w/2)],
    origin:start,rotation:[0,0,i*19%47]});
  absorptionSparks.push({bone,start,target,delay:i%4*.07});
}

const animation = 'animation.ryujin.bankai_activation_scene_lab';
const keyframes = [];
function add(bone,channel,time,value,interpolation='catmullrom') {
  keyframes.push({ bone, channel, time:round(time), value:value.map(round), interpolation });
}
function importClip(clip, shift, omit = new Set(), holdTo12 = false) {
  const anim = nextAnimation(clip);
  for (const [bone,channels] of Object.entries(anim.bones)) {
    for (const [channel,keys] of Object.entries(channels)) {
      if (omit.has(`${bone}:${channel}`)) continue;
      let last;
      for (const [time,entry] of Object.entries(keys)) {
        const value = entry.post?.vector ?? entry.pre?.vector;
        if (!value) throw new Error(`No vector for ${bone}/${channel}/${time}`);
        add(bone,channel,Number(time)+shift,value,entry.lerp_mode || 'linear');
        last = value;
      }
      if (holdTo12 && last) add(bone,channel,12,last,'linear');
    }
  }
}
function nextAnimation(data) { return Object.values(data.animations)[0]; }
importClip(poseClip,0,new Set(),true);
importClip(sunClip,3.4,new Set([
  'inner_swirl:rotation','mid_swirl:rotation','outer_swirl:rotation','broken_rim:rotation',
]));

// Opposing, continuous ring rotations. Several intermediate keys make direction visible.
for (const [bone,sign,turns] of [
  ['inner_swirl',-1,1.0],['mid_swirl',1,1.25],['outer_swirl',-1,1.5],['broken_rim',1,.75],
]) {
  for (const [time,f] of [[0,0],[3.4,0],[4.2,.1],[4.8,.28],[5.5,.51],[6.2,.75],[7.1,1]])
    add(bone,'rotation',time,[0,0,round(sign*turns*360*f)],'linear');
}
// The whole sun converges toward the sword at the player's right side.
for (const [time,s,x,y,z] of [
  [0,.02,0,0,0],[3.34,.02,0,0,0],[3.40,.15,0,0,0],
  [3.55,1.15,0,0,0],[3.78,1,0,0,0],[5.8,1,0,0,0],
  [6.25,.8,3,-22,-12],[6.65,.38,9,-48,-31],[7.10,.025,18,-74,-56],[12,.025,18,-74,-56],
]) {
  add('sun_root','scale',time,[s,s,s],time<=3.4?'linear':'catmullrom');
  add('sun_root','position',time,[x,y,z],time<=3.4?'linear':'catmullrom');
}
// Fast warm flash, then decay; this is geometric brightness, not world lighting.
for (const [time,s] of [[0,.02],[3.35,.02],[3.52,1.36],[3.78,.93],[4.03,.36],[4.32,.02],[12,.02]])
  add('flash_corona','scale',time,[s,s,s],time===3.52?'linear':'catmullrom');
for (const [time,angle] of [[0,0],[3.35,0],[3.52,16],[3.9,28],[4.32,34],[12,34]])
  add('flash_corona','rotation',time,[0,0,angle],'linear');
for (const [time,s] of [[0,.02],[3.35,.02],[3.52,1.2],[4.15,.68],[5.05,.96],[5.80,.76],[6.55,.42],[7.20,.02],[12,.02]])
  add('player_fire_light','scale',time,[s,s,s],time===3.52?'linear':'catmullrom');
for (const {bone,start,target,delay} of absorptionSparks) {
  const delta=target.map((v,i)=>round(v-start[i]));
  for (const [time,f,s] of [[0,0,.02],[5.84,0,.02],[6.00+delay,.03,1],
    [6.25+delay,.31,.98],[6.55+delay,.66,.72],[6.88+delay,.91,.43],[7.24+delay,1,.02],[12,1,.02]]) {
    add(bone,'position',time,delta.map(n=>round(n*f)),'catmullrom');
    add(bone,'scale',time,[s,s,s],time===6.00+delay?'linear':'catmullrom');
  }
}
// A restrained pulse in the hot core while the rings keep moving.
for (const [time,s] of [[5.82,1],[6.08,1.07],[6.28,.96],[6.50,1.05],[6.80,.98],[7.10,1],[12,1]])
  add('sun_core','scale',time,[s,s,s],'catmullrom');

const requests = [
  { name:'new_project', arguments:{ format:'geckolib_model', name, geometry_name:name, texture_width:256, texture_height:4096 } },
  { name:'create_texture', arguments:{ name:`${name}.png`, width:256, height:4096, fill:'#241a1a' } },
  { name:'add_groups', arguments:{ groups } },
  { name:'add_cubes', arguments:{ cubes } },
  { name:'pack_uv', arguments:{ padding:1, auto_resize:true } },
  { name:'detail_cubes', arguments:{ texture:`${name}.png`, base:'#322527', blur:.50, noise:.025,
    colors:[
      {match:'^body_|^leg_|^foot_|^arm_',color:'#211c23'},
      {match:'^head_box',color:'#d3ad88'},
      {match:'^hand_',color:'#c99d77'},
      {match:'^katana_grip',color:'#694020'},
      {match:'^katana_guard',color:'#d99c2b'},
      {match:'^katana_blade|^katana_tip',color:'#e3e6e9'},
      {match:'^heart_',color:'#ffe59c'},
      {match:'^inner_swirl',color:'#ffb94d'},
      {match:'^mid_swirl',color:'#e56a24'},
      {match:'^outer_swirl',color:'#ad331a'},
      {match:'^rim_char',color:'#702119'},
      {match:'^flame_\\d+_base',color:'#f9761d'},
      {match:'^flame_\\d+_mid',color:'#d94b1d'},
      {match:'^flame_\\d+_tip',color:'#b92d18'},
      {match:'^ember_',color:'#ffd34f'},
      {match:'^flash_ray',color:'#ffe284'},
      {match:'^heat_',color:'#f99441'},
      {match:'^absorb_spark_',color:'#ffd359'},
    ] } },
  { name:'create_animation', arguments:{ name:animation, loop:'once', length:12 } },
  { name:'add_keyframes', arguments:{ animation, keyframes, length:12 } },
  { name:'check_model' },
  { name:'check_rig' },
  { name:'analyze_animation', arguments:{ animation, expect:'cast', samples:32 } },
  { name:'screenshot_views', arguments:{ views:['front','front_right','left'], width:720,height:720 } },
  { name:'preview_animation', arguments:{ animation,
    times:[0,2,3.3,3.55,4.4,5.8,6.6,7.2,8,12], views:['front'],width:720,height:720 } },
  { name:'preview_animation', arguments:{ animation,
    times:[2,3.55,5.8,6.6,7.2], views:['front_right'],width:720,height:720 } },
  { name:'save_project', arguments:{ path:join(here,`${name}.bbmodel`) } },
  { name:'export_model', arguments:{ codec:'bedrock',path:join(here,`${name}.geo.json`) } },
];
await writeFile(join(here,'24-activation-scene-v5-plan.json'),JSON.stringify(requests,null,2));
process.stdout.write(`Prepared scene: ${groups.length} bones, ${cubes.length} cubes, ${keyframes.length} keyframes\n`);
