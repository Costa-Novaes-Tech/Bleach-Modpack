import { readFile, writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const model = JSON.parse(await readFile(join(here, 'ryujin_sun_formation_lab.bbmodel'), 'utf8'));
const edits = [];
for (const element of model.elements) {
  const core = /^core_(\d+)_(-?\d+)_glow$/.exec(element.name);
  if (core) {
    const strip = Number(core[2]);
    const shift = strip * 0.17;
    edits.push({ name: 'edit_element', arguments: {
      element: element.name,
      from: [element.from[0], element.from[1], Math.round((element.from[2] + shift) * 100) / 100],
      to: [element.to[0], element.to[1], Math.round((element.to[2] + shift) * 100) / 100],
    } });
  }
  const tongue = /^flame_(\d+)_(middle|tip|ember)$/.exec(element.name);
  if (tongue) {
    const i = Number(tongue[1]);
    const sign = i % 2 ? 1 : -1;
    const extra = { middle: 7, tip: 13, ember: 19 }[tongue[2]];
    edits.push({ name: 'edit_element', arguments: {
      element: element.name,
      rotation: [0, 0, element.rotation[2] + sign * extra],
    } });
  }
}
const requests = [
  { name: 'set_project_meta', arguments: { geometry_name: 'ryujin_sun_formation_lab' } },
  ...edits,
  { name: 'check_model' },
  { name: 'screenshot_views', arguments: { views: ['front', 'left', 'front_right'], width: 640, height: 640 } },
  { name: 'preview_animation', arguments: { animation: 'animation.ryujin.sun_formation_lab', times: [0.03, .52, 1.20, 2.20], views: ['front'], width: 640, height: 640 } },
  { name: 'save_project', arguments: { path: join(here, 'ryujin_sun_formation_lab.bbmodel') } },
  { name: 'export_model', arguments: { codec: 'bedrock', path: join(here, 'ryujin_sun_formation_lab.geo.json') } },
];
await writeFile(join(here, '05-refine-plan.json'), JSON.stringify(requests, null, 2));
process.stdout.write(`Prepared ${edits.length} geometry edits\n`);
