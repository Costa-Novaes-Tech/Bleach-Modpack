import { writeFile } from 'node:fs/promises';
import { join } from 'node:path';
const here='C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const name='ryujin_bankai_activation_scene_v7_lab';
const animation='animation.ryujin.bankai_activation_scene_v7_lab';
const requests=[
  {name:'load_project',arguments:{path:join(here,`${name}.bbmodel`)}},
  {name:'get_status',expect_project:{name,cubes:220,textures:1}},
  {name:'get_orientation'},
  {name:'paint_texture',arguments:{texture:'ryujin_bankai_activation_scene_v5_lab.png',edit_name:'Mark mannequin front for camera orientation',ops:[
    {type:'rect',x:17,y:11,width:1,height:2,color:'#2e1713',fill:true},
    {type:'rect',x:20,y:11,width:1,height:2,color:'#2e1713',fill:true},
    {type:'rect',x:18,y:14,width:2,height:1,color:'#74412a',fill:true},
    {type:'rect',x:229,y:261,width:2,height:2,color:'#dd7b2a',fill:true}
  ]}},
  {name:'check_model'},
  {name:'check_rig'},
  {name:'analyze_animation',arguments:{animation,expect:'cast',samples:48}},
  {name:'screenshot_views',arguments:{views:['front','front_right','back_right','back'],width:900,height:900}},
  {name:'preview_animation',arguments:{animation,times:[2,3.55,5.8,6.15,6.45,6.8,7.1,7.3],views:['front'],width:900,height:900}},
  {name:'preview_animation',arguments:{animation,times:[2,5.8,6.45,7.3],views:['front_right','back_right'],width:900,height:900}},
  {name:'save_project',arguments:{path:join(here,`${name}.bbmodel`)}},
  {name:'export_model',arguments:{codec:'bedrock',path:join(here,`${name}.geo.json`)}}
];
await writeFile(join(here,'29-scene-v7-review-plan.json'),JSON.stringify(requests,null,2));
