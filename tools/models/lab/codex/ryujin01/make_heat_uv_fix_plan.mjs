import { readFile, writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const oldModel = JSON.parse(await readFile(join(here, 'ryujin_bankai_activation_scene_v5_lab.bbmodel'), 'utf8'));
const model = JSON.parse(await readFile(join(here, 'ryujin_bankai_activation_scene_v6_lab.bbmodel'), 'utf8'));
const ops = [];
const oldByName = new Map(oldModel.elements.map(cube => [cube.name, cube]));
function rect(cube) {
  const [u1,v1,u2,v2] = cube.faces.north.uv;
  return {x:Math.min(u1,u2), y:Math.min(v1,v2), width:Math.abs(u2-u1), height:Math.abs(v2-v1)};
}
for (const cube of model.elements.filter(cube => cube.name.startsWith('heat_'))) {
  const old = rect(oldByName.get(cube.name));
  const now = rect(cube);
  ops.push({type:'clear',...old});
  ops.push({type:'clear',...now});
  ops.push({type:'ellipse',x:now.x+.5,y:now.y+.5,width:now.width-1,height:now.height-1,
    color:'rgba(255,122,37,0.48)',fill:true});
  if (now.width>6 && now.height>4) ops.push({type:'ellipse',
    x:now.x+now.width*.20,y:now.y+now.height*.22,
    width:now.width*.60,height:now.height*.56,
    color:'rgba(255,217,99,0.28)',fill:true});
}
const name = 'ryujin_bankai_activation_scene_v6_lab';
const requests = [
  {name:'get_status',expect_project:{name,cubes:220,textures:1}},
  {name:'paint_texture',arguments:{texture:'ryujin_bankai_activation_scene_v5_lab.png',ops,
    edit_name:'Align warm reflection to corrected Box UV'}},
  {name:'check_model'},
  {name:'preview_animation',arguments:{animation:'animation.ryujin.bankai_activation_scene_lab',
    times:[3.55,5.8,6.6,7.2],views:['front'],width:900,height:900}},
  {name:'save_project',arguments:{path:join(here,`${name}.bbmodel`)}},
  {name:'export_model',arguments:{codec:'bedrock',path:join(here,`${name}.geo.json`)}},
];
await writeFile(join(here,'27-scene-heat-uv-fix.json'),JSON.stringify(requests,null,2));
