// Data for a first, isolated GeckoLib sun experiment; no Blockbench script execution.
import { writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const colors = {
  core: '#ffed9a', mantle: '#ff9d29', ridge: '#b83b16',
  corona: '#8d2215', flame: '#ef5d19', tip: '#ffd259',
};
const faces = (texture) => Object.fromEntries(
  ['north', 'south', 'east', 'west', 'up', 'down'].map((side) => [side, { texture }]),
);
const round = (n) => Math.round(n * 100) / 100;
const cubes = [];
const groups = [
  { name: 'sun_root', origin: [0, 0, 0] },
  { name: 'sun_core', origin: [0, 0, 0], parent: 'sun_root' },
  { name: 'sun_mantle', origin: [0, 0, 0], parent: 'sun_root' },
  { name: 'sun_corona', origin: [0, 0, 0], parent: 'sun_root' },
];

// Five stepped depth slices make the luminous heart a real 3D volume.
for (const [layer, z, radius] of [[0,-21,26],[1,-11,38],[2,0,46],[3,11,38],[4,21,26]]) {
  for (let strip = -3; strip <= 3; strip++) {
    const y = strip * radius / 3.55;
    const halfWidth = Math.sqrt(Math.max(0, radius * radius - y * y));
    cubes.push({
      name: `core_${layer}_${strip}_glow`, parent: 'sun_core',
      from: [round(-halfWidth), round(y - radius / 7), z - 5],
      to: [round(halfWidth), round(y + radius / 7), z + 5],
      faces: faces('sun_core.png'),
    });
  }
}

function radialCube(name, angle, radius, length, width, z, depth, parent, texture) {
  const a = angle * Math.PI / 180;
  const cx = round(radius * Math.cos(a));
  const cy = round(radius * Math.sin(a));
  cubes.push({
    name, parent,
    from: [round(cx - length / 2), round(cy - width / 2), round(z - depth / 2)],
    to: [round(cx + length / 2), round(cy + width / 2), round(z + depth / 2)],
    origin: [cx, cy, z], rotation: [0, 0, angle],
    faces: faces(texture),
  });
}

for (let i = 0; i < 20; i++) {
  const angle = i * 18;
  radialCube(`mantle_${i}`, angle, 54, 32, 21, (i % 3 - 1) * 11, 14, 'sun_mantle', 'sun_mantle.png');
  radialCube(`sulcus_${i}`, angle + 4, 68, 15, 4, -20 + (i % 2) * 40, 3, 'sun_mantle', 'sun_ridge.png');
}
for (let i = 0; i < 16; i++) {
  radialCube(`corona_${i}`, i * 22.5, 75, 18, 12, (i % 4 - 1.5) * 7, 10,
    'sun_corona', i % 3 === 0 ? 'sun_ridge.png' : 'sun_corona.png');
}

for (let i = 0; i < 12; i++) {
  const angle = i * 30 + (i % 2 ? 5 : -4);
  const a = angle * Math.PI / 180;
  const name = `flame_${String(i).padStart(2, '0')}`;
  groups.push({ name, origin: [round(76 * Math.cos(a)), round(76 * Math.sin(a)), 0], parent: 'sun_root' });
  radialCube(`${name}_root`, angle, 85, 28, 15, (i % 3 - 1) * 5, 12, name, 'sun_flame.png');
  radialCube(`${name}_middle`, angle + (i % 2 ? 5 : -5), 102, 22, 10, (i % 3 - 1) * 7, 8, name, 'sun_mantle.png');
  radialCube(`${name}_tip`, angle + (i % 2 ? 9 : -8), 118, 19, 5, (i % 3 - 1) * 8, 5, name, 'sun_tip.png');
  radialCube(`${name}_ember`, angle + (i % 2 ? 13 : -12), 127, 6, 3, (i % 3 - 1) * 9, 3, name, 'sun_tip.png');
}

const animation = 'animation.ryujin.sun_formation_lab';
const keyframes = [];
function scale(bone, keys) {
  for (const [time, amount] of keys) keyframes.push({ bone, channel: 'scale', time, value: [amount, amount, amount], interpolation: 'linear' });
}
scale('sun_core', [[0,.03],[.60,.03],[.90,.22],[1.20,.63],[1.70,1.03],[2.20,1]]);
scale('sun_mantle', [[0,.03],[.72,.03],[1.05,.25],[1.42,.80],[1.90,1.04],[2.20,1]]);
scale('sun_corona', [[0,.03],[.90,.03],[1.20,.20],[1.60,.72],[2.00,1.05],[2.20,1]]);
for (let i = 0; i < 12; i++) {
  const name = `flame_${String(i).padStart(2, '0')}`;
  const peak = 1.35 + (i % 4) * .08;
  scale(name, [[0,.04],[.18,.60],[.52,peak],[.82,1.20],[1.25,1.12],[1.80,1.02],[2.20,1]]);
  for (const [time, angle] of [[0,0],[.52,(i%2?1:-1)*7],[1.05,(i%2?-1:1)*4],[1.70,(i%2?1:-1)*2],[2.20,0]]) {
    keyframes.push({ bone:name, channel:'rotation', time, value:[0,0,angle], interpolation:'catmullrom' });
  }
}

const requests = [
  { name: 'new_project', arguments: { format: 'geckolib_model', name: 'ryujin_sun_formation_lab', geometry_name: 'geometry.ryujin_sun_formation_lab', texture_width: 256, texture_height: 256 } },
  ...Object.entries(colors).map(([name, fill]) => ({ name: 'create_texture', arguments: { name: `sun_${name}.png`, width: 256, height: 256, fill } })),
  { name: 'add_groups', arguments: { groups } },
  { name: 'add_cubes', arguments: { cubes } },
  { name: 'create_animation', arguments: { name: animation, loop: 'once', length: 2.2 } },
  { name: 'add_keyframes', arguments: { animation, keyframes, length: 2.2 } },
  { name: 'list_outliner' },
  { name: 'list_animations' },
  { name: 'check_model' },
  { name: 'screenshot_views', arguments: { views: ['front', 'left', 'front_right'], width: 640, height: 640 } },
  { name: 'preview_animation', arguments: { animation, times: [0.03, .52, 1.20, 2.20], views: ['front'], width: 640, height: 640 } },
  { name: 'save_project', arguments: { path: join(here, 'ryujin_sun_formation_lab.bbmodel') } },
  { name: 'export_project', arguments: { path: join(here, 'ryujin_sun_formation_lab.geo.json') } },
];
await writeFile(join(here, '01-sun-plan.json'), JSON.stringify(requests, null, 2));
process.stdout.write(`Prepared ${groups.length} bones, ${cubes.length} cubes, ${keyframes.length} keyframes\n`);
