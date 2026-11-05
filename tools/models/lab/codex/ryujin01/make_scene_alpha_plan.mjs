// Transparent atlas silhouettes and a warm-light preview for the combined scene.
import { readFile, writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const baseName = 'ryujin_bankai_activation_scene_v5_lab';
const name = 'ryujin_bankai_activation_scene_v6_lab';
const source = JSON.parse(await readFile(join(here, `${baseName}.bbmodel`), 'utf8'));
const ops = [];
const pt = (x,y) => [Math.round(x),Math.round(y)];
function rect(face) {
  const [u1,v1,u2,v2] = face.uv;
  return {x:Math.min(u1,u2),y:Math.min(v1,v2),w:Math.abs(u2-u1),h:Math.abs(v2-v1)};
}
function clearFace(face) {
  const {x,y,w,h}=rect(face);
  if (w>0&&h>0) ops.push({type:'clear',x,y,width:w,height:h});
}
for (const cube of source.elements) {
  const flame = /^flame_\d+_(base|mid|tip)$/.exec(cube.name);
  const flash = cube.name.startsWith('flash_ray_');
  const heat = cube.name.startsWith('heat_');
  if (!flame&&!flash&&!heat) continue;
  for (const faceName of ['north','south','east','west','up','down'])
    clearFace(cube.faces[faceName]);
  for (const faceName of heat?['north']:['north','south']) {
    const {x,y,w,h}=rect(cube.faces[faceName]);
    if (w<3||h<2) continue;
    if (heat) {
      ops.push({type:'ellipse',x:x+.5,y:y+.5,width:w-1,height:h-1,
        color:'rgba(255,122,37,0.48)',fill:true});
      if (w>6&&h>4) ops.push({type:'ellipse',x:x+w*.20,y:y+h*.22,width:w*.60,height:h*.56,
        color:'rgba(255,217,99,0.28)',fill:true});
      continue;
    }
    if (flash) {
      ops.push({type:'polygon',points:[
        pt(x,y+h*.5),pt(x+w*.17,y+h*.12),pt(x+w*.55,y+h*.24),
        pt(x+w*.98,y+h*.5),pt(x+w*.57,y+h*.76),pt(x+w*.19,y+h*.9),
      ],color:'#f58b2a',fill:true});
      ops.push({type:'polygon',points:[
        pt(x+w*.10,y+h*.5),pt(x+w*.28,y+h*.33),pt(x+w*.69,y+h*.41),
        pt(x+w*.91,y+h*.5),pt(x+w*.66,y+h*.6),pt(x+w*.27,y+h*.66),
      ],color:'#fff2a6',fill:true});
      continue;
    }
    const part=flame[1];
    ops.push({type:'polygon',points:[
      pt(x,y+h*.28),pt(x+w*.23,y+h*.05),pt(x+w*.47,y+h*.19),
      pt(x+w*.69,y+h*.31),pt(x+w*.93,y+h*.5),
      pt(x+w*.72,y+h*.61),pt(x+w*.53,y+h*.92),
      pt(x+w*.22,y+h*.78),pt(x,y+h*.86),
    ],color:part==='base'?'#a12b15':part==='mid'?'#b72d15':'#e35b1b',fill:true});
    if (h>=5) ops.push({type:'polygon',points:[
      pt(x+w*.04,y+h*.42),pt(x+w*.23,y+h*.25),pt(x+w*.48,y+h*.33),
      pt(x+w*.72,y+h*.48),pt(x+w*.49,y+h*.68),pt(x+w*.21,y+h*.62),
    ],color:part==='tip'?'#ffad3c':'#ff8d24',fill:true});
  }
}
const animation='animation.ryujin.bankai_activation_scene_lab';
const requests=[
  {name:'get_status',expect_project:{name:baseName,cubes:220,textures:1}},
  {name:'set_project_meta',arguments:{name,geometry_name:name}},
  {name:'paint_texture',arguments:{texture:`${baseName}.png`,ops,edit_name:'Sculpt flame silhouettes and warm player reflections'}},
  {name:'check_model'},
  {name:'analyze_animation',arguments:{animation,expect:'cast',samples:40}},
  {name:'screenshot_views',arguments:{views:['front','front_right','left'],width:900,height:900}},
  {name:'preview_animation',arguments:{animation,times:[2,3.30,3.55,4.40,5.8,6.6,7.2,8,12],views:['front'],width:900,height:900}},
  {name:'preview_animation',arguments:{animation,times:[2,3.55,5.8,6.6,7.2],views:['front_right'],width:900,height:900}},
  {name:'save_project',arguments:{path:join(here,`${name}.bbmodel`)}},
  {name:'export_model',arguments:{codec:'bedrock',path:join(here,`${name}.geo.json`)}},
];
await writeFile(join(here,'25-scene-alpha-v6-plan.json'),JSON.stringify(requests,null,2));
process.stdout.write(`${ops.length} texture operations for ${name}\n`);
