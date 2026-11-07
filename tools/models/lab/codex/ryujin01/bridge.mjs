// Runs a short, explicit MCP command list against Codex's isolated Blockbench profile.
// Never points at Antigravity's port 8787.
import { readFile, writeFile, mkdir } from 'node:fs/promises';
import { join } from 'node:path';
import { pathToFileURL } from 'node:url';

const source = 'C:/Users/joaop/Documents/Skills para IA/01-Repositorios-GitHub/05-modelagem-nicho/blockbench-mcp-main';
const sdk = join(source, 'node_modules/@modelcontextprotocol/sdk/dist/esm');
const { Client } = await import(pathToFileURL(join(sdk, 'client/index.js')).href);
const { StdioClientTransport } = await import(pathToFileURL(join(sdk, 'client/stdio.js')).href);

const [requestFile, outputDir] = process.argv.slice(2);
if (!requestFile || !outputDir) throw new Error('Usage: node bridge.mjs requests.json output-dir');
const requests = JSON.parse(await readFile(requestFile, 'utf8'));
if (!Array.isArray(requests)) throw new Error('Requests must be an array');
const forbidden = new Set(['execute_script', 'install_plugin', 'uninstall_plugin', 'remove_animation', 'close_project']);
if (requests.some((request) => forbidden.has(request.name))) throw new Error('Disallowed MCP operation');
await mkdir(outputDir, { recursive: true });

const client = new Client({ name: 'bleach-ryujin-isolated-lab', version: '1.0.0' });
const transport = new StdioClientTransport({
  command: 'node',
  args: [join(source, 'dist/index.js')],
  env: { ...process.env, BLOCKBENCH_MCP_PORT: '8788' },
});
await client.connect(transport);
try {
  const results = [];
  for (let i = 0; i < requests.length; i++) {
    const request = requests[i];
    // Large GeckoLib geometry exports can exceed the SDK's 60 s default.
    const result = await client.callTool(
      { name: request.name, arguments: request.arguments ?? {} },
      undefined,
      { timeout: 180000 },
    );
    const summary = { index: i, name: request.name, isError: Boolean(result.isError), text: [], images: [] };
    for (let j = 0; j < (result.content ?? []).length; j++) {
      const item = result.content[j];
      if (item.type === 'text') summary.text.push(item.text);
      else if (item.type === 'image') {
        const path = join(outputDir, `${String(i).padStart(2, '0')}-${request.name}-${j}.png`);
        await writeFile(path, Buffer.from(item.data, 'base64'));
        summary.images.push(path);
      }
    }
    if (request.expect_project) {
      const actual = JSON.parse(summary.text[0] || '{}').project;
      const wanted = request.expect_project;
      if (!actual || actual.name !== wanted.name || actual.cubes !== wanted.cubes
          || actual.textures !== wanted.textures) {
        throw new Error(`Active Blockbench project mismatch: expected ${JSON.stringify(wanted)}, got ${JSON.stringify(actual)}`);
      }
    }
    results.push(summary);
    await writeFile(join(outputDir, 'results.json'), JSON.stringify(results, null, 2));
    process.stdout.write(`${i} ${request.name}: ${summary.isError ? 'ERROR' : 'OK'} ${summary.images.length} image(s)\n`);
    if (summary.isError) break;
  }
} finally {
  await client.close();
}
