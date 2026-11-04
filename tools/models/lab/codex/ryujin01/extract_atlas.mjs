// Extracts the exact atlas embedded by Blockbench in the saved .bbmodel.
import { readFile, writeFile } from 'node:fs/promises';
import { join } from 'node:path';

const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
for (const [name, texture] of [
  ['ryujin_sun_formation_lab', 'sun_core.png'],
  ['ryujin_bankai_pose_lab', 'pose_atlas.png'],
  ['ryujin_sun_vortex_lab', 'ryujin_sun_vortex_lab.png'],
  ['ryujin_sun_vortex_alpha_lab', 'ryujin_sun_vortex_lab.png'],
]) {
  const model = JSON.parse(await readFile(join(here, `${name}.bbmodel`), 'utf8'));
  const atlas = model.textures.find((entry) => entry.name === texture);
  if (!atlas?.source?.startsWith('data:image/png;base64,')) throw new Error(`${name}: Blockbench atlas PNG missing`);
  const png = Buffer.from(atlas.source.slice('data:image/png;base64,'.length), 'base64');
  if (png.toString('hex', 0, 8) !== '89504e470d0a1a0a') throw new Error(`${name}: Invalid PNG signature`);
  await writeFile(join(here, `${name}.png`), png);
  process.stdout.write(`${name}: ${png.length} PNG bytes\n`);
}
