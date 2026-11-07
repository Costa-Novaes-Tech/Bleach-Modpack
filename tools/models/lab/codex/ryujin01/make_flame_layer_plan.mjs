import { writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const name = 'ryujin_sun_vortex_flame_lab';
const animation = 'animation.ryujin.sun_vortex_formation_lab';
const requests = [
  { name: 'load_project', arguments: { path: join(here, 'ryujin_sun_vortex_lab.bbmodel') } },
  { name: 'set_project_meta', arguments: { name, geometry_name: name } },
  { name: 'add_groups', arguments: { groups: [
    { name: 'flame_backdrop', origin: [0, -100, 45], parent: 'sun_root' },
  ] } },
  { name: 'create_vfx_texture', arguments: { name: 'vortex_flame_sheet.png', style: 'flame', preset: 'fire', width: 64, height: 96, frames: 1, seed: 803, soft_edge: false, render_mode: 'additive', render_sides: 'double' } },
];
const sheet = 'vortex_flame_sheet.png';
const positions = [
  [-160,-87,88,143,35],[-117,-96,94,169,38],[-74,-91,76,136,41],[-33,-92,100,186,44],
  [17,-92,91,152,47],[57,-95,96,184,50],[103,-88,89,140,53],
  [-122,-74,74,133,58],[-64,-84,77,162,61],[1,-83,88,173,64],[72,-80,81,146,67],
];
positions.forEach(([x,y,w,h,z],i) => requests.push({ name: 'add_plane', arguments: {
  name: `flame_sheet_${String(i).padStart(2,'0')}`,
  parent: 'flame_backdrop', from: [x,y,z], width: w, height: h,
  origin: [x+w/2,y,z], rotation: [0, (i%3-1)*12, 0], texture: sheet,
} }));
requests.push(
  { name: 'add_keyframes', arguments: { animation, length: 2.4, keyframes: [
    { bone: 'flame_backdrop', channel: 'scale', time: 0, value: [.02,.02,.02] },
    { bone: 'flame_backdrop', channel: 'scale', time: .12, value: [.45,.60,1] },
    { bone: 'flame_backdrop', channel: 'scale', time: .40, value: [1.2,1.35,1] },
    { bone: 'flame_backdrop', channel: 'scale', time: .80, value: [1.10,1.12,1] },
    { bone: 'flame_backdrop', channel: 'scale', time: 1.55, value: [1.02,1.02,1] },
    { bone: 'flame_backdrop', channel: 'scale', time: 2.4, value: [1,1,1] },
  ] } },
  { name: 'check_model' },
  { name: 'screenshot_views', arguments: { views: ['front','front_right','left'], width: 640, height: 640 } },
  { name: 'preview_animation', arguments: { animation, times: [.34,.95,1.55,2.4], views: ['front'], width: 640, height: 640 } },
  { name: 'save_project', arguments: { path: join(here, `${name}.bbmodel`) } },
  { name: 'export_model', arguments: { codec: 'bedrock', path: join(here, `${name}.geo.json`) } },
);
await writeFile(join(here, '13-flame-layer-plan.json'), JSON.stringify(requests,null,2));
process.stdout.write(`${positions.length} layered flame planes planned\n`);
