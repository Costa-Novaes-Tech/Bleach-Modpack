// A disposable movement study, not the runtime player/item rig.
import { writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const anim = 'animation.ryujin.bankai_pose_lab';
const keyframes = [];
function channel(bone, values, kind = 'rotation') {
  for (const [time, value] of values) keyframes.push({ bone, channel: kind, time, value, interpolation: 'catmullrom' });
}
channel('arm_upper_right', [[0,[0,0,0]],[.6,[-8,0,-5]],[1.6,[58,0,-9]],[2,[65,0,-8]],[3.4,[65,0,-8]],[4,[53,0,-8]],[5.2,[15,0,-5]],[6.2,[0,0,0]],[8,[0,0,0]]]);
channel('arm_lower_right', [[0,[0,0,0]],[.6,[-8,0,0]],[1.6,[40,0,0]],[2,[45,0,0]],[3.4,[45,0,0]],[4,[30,0,0]],[5.2,[8,0,0]],[6.2,[0,0,0]],[8,[0,0,0]]]);
channel('hand_right', [[0,[0,0,0]],[.6,[8,0,0]],[1.6,[-30,0,0]],[2,[-35,0,0]],[3.4,[-35,0,0]],[4,[-30,0,0]],[5.2,[-10,0,0]],[6.2,[0,0,0]],[8,[0,0,0]]]);
channel('katana', [[0,[0,0,-110]],[.6,[0,0,-105]],[1.6,[-68,0,-18]],[2,[-75,0,0]],[3.4,[-75,0,0]],[4,[-53,0,-28]],[5.2,[-13,0,-105]],[6.2,[0,0,-110]],[8,[0,0,-110]]]);
channel('chest', [[0,[0,0,0]],[1.6,[-5,0,0]],[2,[-5,0,0]],[3.4,[-5,0,0]],[3.7,[4,0,0]],[4.4,[1,0,0]],[5.8,[0,0,0]],[8,[0,0,0]]]);
channel('head', [[0,[0,0,0]],[1.6,[3,0,0]],[2,[2,0,0]],[3.4,[2,0,0]],[3.7,[-4,0,0]],[4.4,[0,0,0]],[8,[0,0,0]]]);

const requests = [
  { name: 'new_project', arguments: { format:'geckolib_model', name:'ryujin_bankai_pose_lab', geometry_name:'ryujin_bankai_pose_lab', texture_width:128, texture_height:128 } },
  { name: 'create_texture', arguments: { name:'pose_atlas.png', width:128, height:128, fill:'#42485a' } },
  { name: 'create_rig', arguments: { type:'humanoid', height:32, placeholder_cubes:true, arm_segments:3, leg_segments:3, spine_segments:2 } },
  { name: 'edit_element', arguments: { element:'body_lower', from:[-4,12,-2.3], to:[4,18.6,2.3] } },
  { name: 'add_group', arguments: { name:'katana', origin:[6,11,-5], parent:'hand_right' } },
  { name: 'add_cubes', arguments: { cubes:[
    { name:'katana_grip', parent:'katana', from:[5.1,9.3,-5.8], to:[6.9,14,-4.2] },
    { name:'katana_guard', parent:'katana', from:[3.6,13.4,-6.1], to:[8.4,14.6,-3.9] },
    { name:'katana_blade', parent:'katana', from:[5.5,14.3,-5.7], to:[6.5,38,-4.6] },
    { name:'katana_tip', parent:'katana', from:[5.5,38,-5.8], to:[6.5,40,-4.6] },
  ] } },
  { name: 'pack_uv', arguments: { cubes:'all', padding:1, auto_resize:true } },
  { name: 'detail_cubes', arguments: { cubes:'all', texture:'pose_atlas.png', base:'#444b5d', colors:[
    {match:'^head',color:'#cdb59b'}, {match:'^hand',color:'#cdb59b'},
    {match:'^katana_blade|^katana_tip',color:'#d8dce0'}, {match:'^katana_grip',color:'#54362b'},
    {match:'^katana_guard',color:'#8d6c3b'},
  ], noise:.025, blur:.1 } },
  { name: 'check_rig' },
  { name: 'create_animation', arguments: { name:anim, loop:'once', length:8 } },
  { name: 'add_keyframes', arguments: { animation:anim, keyframes, length:8 } },
  { name: 'analyze_animation', arguments: { animation:anim, samples:25 } },
  { name: 'preview_animation', arguments: { animation:anim, times:[0,.6,2,3.4,4.3,5.8,8], views:['front','left'], width:520, height:620 } },
  { name: 'check_model' },
  { name: 'save_project', arguments: { path:join(here,'ryujin_bankai_pose_lab.bbmodel') } },
];
await writeFile(join(here,'06-pose-plan.json'),JSON.stringify(requests,null,2));
process.stdout.write(`Prepared pose: ${keyframes.length} keyframes\n`);
