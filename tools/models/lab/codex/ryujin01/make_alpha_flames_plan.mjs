// Paint transparent flame silhouettes into the single packed GeckoLib atlas.
// The original vortex source stays intact; this saves a separate variant.
import { readFile, writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const base = JSON.parse(await readFile(join(here, 'ryujin_sun_vortex_lab.bbmodel'), 'utf8'));
const name = 'ryujin_sun_vortex_alpha_lab';
const ops = [];
const point = (x, y) => [Math.round(x), Math.round(y)];
for (const cube of base.elements.filter(e => /^flame_\d+_(base|mid|tip)$/.test(e.name))) {
  const part = cube.name.split('_').at(-1);
  for (const face of ['north', 'south']) {
    const [x1,y1,x2,y2] = cube.faces[face].uv;
    const x = Math.min(x1,x2), y = Math.min(y1,y2);
    const w = Math.abs(x2-x1), h = Math.abs(y2-y1);
    if (w < 3 || h < 2) continue;
    ops.push({ type: 'clear', x, y, width: w, height: h });
    const poly = [
      point(x, y+h*.28), point(x+w*.23, y+h*.05), point(x+w*.47, y+h*.19),
      point(x+w*.69, y+h*.31), point(x+w*.93, y+h*.5),
      point(x+w*.72, y+h*.61), point(x+w*.53, y+h*.92),
      point(x+w*.22, y+h*.78), point(x, y+h*.86),
    ];
    const outer = part === 'base' ? '#a12b15' : part === 'mid' ? '#b72d15' : '#e35b1b';
    ops.push({ type: 'polygon', points: poly, color: outer, fill: true });
    if (h >= 5) {
      ops.push({ type: 'polygon', points: [
        point(x+w*.04,y+h*.42), point(x+w*.23,y+h*.25), point(x+w*.48,y+h*.33),
        point(x+w*.72,y+h*.48), point(x+w*.49,y+h*.68), point(x+w*.21,y+h*.62),
      ], color: part === 'tip' ? '#ffad3c' : '#ff8d24', fill: true });
    }
  }
  // Side faces retain their UV assignments but do not expose opaque square caps.
  for (const face of ['east','west','up','down']) {
    const [u1,v1,u2,v2] = cube.faces[face].uv;
    ops.push({ type: 'clear', x: Math.min(u1,u2), y: Math.min(v1,v2),
      width: Math.abs(u2-u1), height: Math.abs(v2-v1) });
  }
}
const requests = [
  { name: 'get_status' },
  { name: 'set_project_meta', arguments: { name, geometry_name: name } },
  { name: 'paint_texture', arguments: { texture: 'ryujin_sun_vortex_lab.png', ops, edit_name: 'Sculpt alpha flame edges' } },
  { name: 'check_model' },
  { name: 'screenshot_views', arguments: { views: ['front','front_right','left'], width: 640, height: 640 } },
  { name: 'preview_animation', arguments: { animation: 'animation.ryujin.sun_vortex_formation_lab', times: [.34,.95,1.55,2.4], views: ['front'], width: 640, height: 640 } },
  { name: 'save_project', arguments: { path: join(here, `${name}.bbmodel`) } },
  { name: 'export_model', arguments: { codec: 'bedrock', path: join(here, `${name}.geo.json`) } },
];
await writeFile(join(here, '15-alpha-flames-plan.json'), JSON.stringify(requests,null,2));
process.stdout.write(`${ops.length} atlas paint operations prepared\n`);
