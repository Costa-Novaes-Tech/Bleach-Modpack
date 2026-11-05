// Ryujin sun V2: broken spiral bands and unequal flame tongues.
// Commands target only the isolated Blockbench bridge (port 8788).
import { writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const name = 'ryujin_sun_vortex_lab';
const round = n => Math.round(n * 100) / 100;
const rad = d => d * Math.PI / 180;
const point = (radius, angle) => [radius * Math.cos(rad(angle)), radius * Math.sin(rad(angle))];
const groups = [
  { name: 'sun_root', origin: [0, 0, 0] },
  { name: 'sun_core', origin: [0, 0, 0], parent: 'sun_root' },
  { name: 'inner_swirl', origin: [0, 0, -12], parent: 'sun_root' },
  { name: 'mid_swirl', origin: [0, 0, 0], parent: 'sun_root' },
  { name: 'outer_swirl', origin: [0, 0, 12], parent: 'sun_root' },
  { name: 'broken_rim', origin: [0, 0, -19], parent: 'sun_root' },
];
const cubes = [];
function beam(cubeName, parent, a, b, width, z, depth) {
  const cx = (a[0] + b[0]) / 2, cy = (a[1] + b[1]) / 2;
  const length = Math.hypot(b[0] - a[0], b[1] - a[1]);
  cubes.push({
    name: cubeName, parent,
    from: [round(cx - length / 2), round(cy - width / 2), round(z - depth / 2)],
    to: [round(cx + length / 2), round(cy + width / 2), round(z + depth / 2)],
    origin: [round(cx), round(cy), round(z)],
    rotation: [0, 0, round(Math.atan2(b[1] - a[1], b[0] - a[0]) * 180 / Math.PI)],
  });
}

// The hot center has depth, with staggered strips to keep coplanar faces apart.
for (const [layer, z, radius] of [[0, -18, 20], [1, -8, 33], [2, 4, 37], [3, 16, 28]]) {
  for (let strip = -3; strip <= 3; strip++) {
    const y = strip * radius / 3.6;
    const half = Math.sqrt(Math.max(0, radius * radius - y * y));
    const dz = strip * .19;
    cubes.push({ name: `heart_${layer}_${strip}_glow`, parent: 'sun_core',
      from: [round(-half), round(y - radius / 7.3), round(z - 4.5 + dz)],
      to: [round(half), round(y + radius / 7.3), round(z + 4.5 + dz)] });
  }
}

// Three incomplete spirals read as moving fire, rather than radial gear teeth.
const spirals = [
  { name: 'inner_swirl', start: -52, sweep: 295, r0: 39, r1: 58, count: 25, z: -12, width: 9.5 },
  { name: 'mid_swirl', start: 114, sweep: 273, r0: 61, r1: 80, count: 27, z: 0, width: 10.5 },
  { name: 'outer_swirl', start: -128, sweep: 248, r0: 85, r1: 107, count: 28, z: 12, width: 11 },
];
for (const band of spirals) {
  for (let i = 0; i < band.count; i++) {
    const f0 = i / band.count, f1 = (i + 1.11) / band.count;
    const makePoint = f => point(band.r0 + (band.r1 - band.r0) * f + 1.9 * Math.sin(i * 1.7), band.start + band.sweep * f);
    const w = band.width * (0.95 - .30 * f0) * (i % 6 === 0 ? .78 : 1);
    beam(`${band.name}_${String(i).padStart(2, '0')}`, band.name,
      makePoint(f0), makePoint(f1), w, band.z + .24 * (i % 3), 5.5);
  }
}

// A darker broken ring provides depth while leaving gaps for the spiral tails.
for (let i = 0; i < 15; i++) {
  if (i === 2 || i === 8 || i === 12) continue;
  const a = i * 24 - 39;
  beam(`rim_char_${i}`, 'broken_rim', point(81 + i % 3, a), point(83 + i % 3, a + 15),
    5 + i % 3, -19 + .3 * (i % 4), 4);
}

// Strong upper silhouette: staggered tongues curl sideways instead of forming spokes.
const flameAngles = [-31, -15, 5, 23, 43, 62, 82, 103, 121, 141, 163, 185, 205];
for (let i = 0; i < flameAngles.length; i++) {
  const angle = flameAngles[i], long = [44, 59, 49, 73, 52, 66, 84, 60, 74, 48, 64, 52, 43][i];
  const root = point(92 + i % 3 * 3, angle);
  const base = `flame_${String(i).padStart(2, '0')}`;
  groups.push({ name: base, parent: 'sun_root', origin: [round(root[0]), round(root[1]), 0] });
  const p1 = point(111 + long * .27, angle + 4);
  const p2 = point(115 + long * .68, angle + (i % 2 ? 11 : -7));
  const p3 = point(116 + long, angle + (i % 2 ? 20 : -15));
  const z = (i % 4 - 1.5) * 7;
  beam(`${base}_base`, base, root, p1, 14 + i % 3 * 2, z, 8);
  beam(`${base}_mid`, base, p1, p2, 8 + i % 4, z + .31, 6);
  beam(`${base}_tip`, base, p2, p3, 3.5 + i % 3, z + .63, 3.5);
}

// Tiny detached embers are visibly secondary to the modeled fire.
for (let i = 0; i < 12; i++) {
  const angle = 11 + i * 16.5;
  const radius = 150 + i % 4 * 12;
  const p = point(radius, angle);
  beam(`ember_${i}`, 'sun_root', p, [p[0] + (i % 2 ? -5 : 4), p[1] + 8 + i % 3 * 3],
    2 + i % 2, -12 + i % 4 * 8, 2);
}

const animation = 'animation.ryujin.sun_vortex_formation_lab';
const keyframes = [];
function track(bone, channel, values, interpolation = 'catmullrom') {
  for (const [time, value] of values) keyframes.push({ bone, channel, time, value, interpolation });
}
const scale = n => [n, n, n];
track('sun_core', 'scale', [[0,scale(.03)],[.62,scale(.03)],[.95,scale(.34)],[1.5,scale(1.04)],[2.4,scale(1)]]);
for (const [bone, delay, twist] of [['inner_swirl',.78,17],['mid_swirl',.91,-14],['outer_swirl',1.02,19],['broken_rim',1.07,-9]]) {
  track(bone, 'scale', [[0,scale(.03)],[delay,scale(.03)],[delay+.42,scale(.48)],[2.05,scale(1.04)],[2.4,scale(1)]]);
  track(bone, 'rotation', [[0,[0,0,-twist]],[delay,[0,0,-twist]],[1.65,[0,0,twist*.25]],[2.4,[0,0,0]]]);
}
for (let i = 0; i < flameAngles.length; i++) {
  const bone = `flame_${String(i).padStart(2, '0')}`;
  const flash = .34 + i % 4 * .045;
  track(bone, 'scale', [[0,scale(.03)],[.14,scale(.46)],[flash,scale(1.26 + i % 3 * .08)],[.86,scale(1.14)],[1.6,scale(1.04)],[2.4,scale(1)]]);
  track(bone, 'rotation', [[0,[0,0,0]],[flash,[0,0,i % 2 ? 8 : -7]],[1.15,[0,0,i % 2 ? -4 : 5]],[2.4,[0,0,0]]]);
}

const requests = [
  { name: 'new_project', arguments: { format: 'geckolib_model', name, geometry_name: `geometry.${name}`, texture_width: 256, texture_height: 4096 } },
  { name: 'create_texture', arguments: { name: `${name}.png`, width: 256, height: 4096, fill: '#54201a' } },
  { name: 'add_groups', arguments: { groups } },
  { name: 'add_cubes', arguments: { cubes } },
  { name: 'pack_uv', arguments: { padding: 1, auto_resize: true } },
  { name: 'detail_cubes', arguments: { texture: `${name}.png`, base: '#cb4718', noise: .035, blur: .48, top_light: .1,
    colors: [
      { match: 'heart_', color: '#ffdf78' },
      { match: 'inner_swirl', color: '#ffaf32' },
      { match: 'mid_swirl', color: '#ec6720' },
      { match: 'outer_swirl', color: '#b63319' },
      { match: 'rim_char', color: '#81251a' },
      { match: '_base$', color: '#f9781b' },
      { match: '_mid$', color: '#d74416' },
      { match: '_tip$', color: '#ad2916' },
      { match: 'ember_', color: '#ffcc4a' },
    ] } },
  { name: 'create_animation', arguments: { name: animation, loop: 'once', length: 2.4 } },
  { name: 'add_keyframes', arguments: { animation, keyframes, length: 2.4 } },
  { name: 'check_model' },
  { name: 'screenshot_views', arguments: { views: ['front','left','front_right'], width: 640, height: 640 } },
  { name: 'preview_animation', arguments: { animation, times: [.34,.95,1.55,2.4], views: ['front'], width: 640, height: 640 } },
  { name: 'save_project', arguments: { path: join(here, `${name}.bbmodel`) } },
  { name: 'export_model', arguments: { codec: 'bedrock', path: join(here, `${name}.geo.json`) } },
];
await writeFile(join(here, '12-vortex-plan.json'), JSON.stringify(requests, null, 2));
process.stdout.write(`Prepared ${groups.length} bones, ${cubes.length} cubes, ${keyframes.length} keyframes\n`);
