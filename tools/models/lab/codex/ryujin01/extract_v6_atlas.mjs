import { readFile, writeFile } from 'node:fs/promises';
import { join } from 'node:path';
const here = 'C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/tools/models/lab/codex/ryujin01';
const name = 'ryujin_bankai_activation_scene_v6_lab';
const model = JSON.parse(await readFile(join(here, `${name}.bbmodel`), 'utf8'));
const source = model.textures[0].source;
if (!source.startsWith('data:image/png;base64,')) throw new Error('Embedded PNG missing');
await writeFile(join(here, `${name}.png`), Buffer.from(source.slice('data:image/png;base64,'.length), 'base64'));
