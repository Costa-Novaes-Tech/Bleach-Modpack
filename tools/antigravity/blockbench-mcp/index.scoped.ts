#!/usr/bin/env node
/**
 * BlockbenchMCP — Model Context Protocol server.
 *
 * Exposes Blockbench (3D model / texture / animation editor) to an AI client.
 * It forwards each tool call to the companion bridge plugin running inside
 * Blockbench over local HTTP. Start Blockbench, make sure the BlockbenchMCP
 * plugin is installed and its server is running, then point your MCP client
 * at this process (stdio transport).
 */
import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import {
  CallToolRequestSchema,
  ListToolsRequestSchema,
} from "@modelcontextprotocol/sdk/types.js";
import { tools } from "./tools.js";
import { BASE_URL, callBlockbench } from "./client.js";
import path from "node:path";
import fs from "node:fs";
const allowed = new Set("get_status get_guide get_orientation which_side check_sides list_formats measure_model list_outliner get_element list_textures get_texture list_animations list_plugins check_model audit_complexity check_rig get_rig analyze_animation screenshot screenshot_views new_project load_project save_project export_project export_model set_project_meta add_group add_cube add_groups add_cubes add_plane edit_element mirror_element set_cube_uv apply_texture create_texture paint_texture pack_uv create_animation add_keyframe add_keyframes preview_animation set_camera_angle create_rig".split(" "));
const readOnly = new Set("get_status get_guide get_orientation which_side check_sides list_formats measure_model list_outliner get_element list_textures get_texture list_animations list_plugins check_model audit_complexity check_rig get_rig analyze_animation screenshot screenshot_views".split(" "));
const root = fs.realpathSync(process.env.BLEACH_DESIGN_ROOT!);
function safePath(value: unknown) {
  if (typeof value !== "string" || !path.isAbsolute(value)) throw new Error("Explicit absolute lab path required");
  const candidate = path.resolve(value);
  const lab = path.join(root, "tools", "models", "lab");
  const rel = path.relative(lab, candidate);
  if (!rel || rel.startsWith("..") || path.isAbsolute(rel)) throw new Error("Path must be inside design tools/models/lab");
  let existing = candidate;
  while (!fs.existsSync(existing)) existing = path.dirname(existing);
  const realRel = path.relative(root, fs.realpathSync(existing));
  if (realRel.startsWith("..") || path.isAbsolute(realRel)) throw new Error("Symlink escapes design root");
}
async function guard(name: string, args: Record<string, any>) {
  if (["load_project", "save_project", "export_project", "export_model"].includes(name)) safePath(args.path);
  if (name === "new_project" || name === "set_project_meta") {
    if (args.name && !args.name.startsWith("ag_")) throw new Error("Use ag_ project prefix");
    if (name === "new_project" && !args.name) throw new Error("Use explicit ag_ name");
  }
  if (!readOnly.has(name) && !["new_project", "load_project"].includes(name)) {
    const state = await callBlockbench("get_status", {}) as any;
    if (!state?.project?.name?.startsWith("ag_")) throw new Error("Active model belongs to another author. Open an ag_ lab copy first.");
  }
}
const designTools = tools.filter(t => allowed.has(t.name));

const server = new Server(
  { name: "blockbench-mcp", version: "0.1.0" },
  { capabilities: { tools: {} } }
);

const toolMap = new Map(designTools.map((t) => [t.name, t]));

server.setRequestHandler(ListToolsRequestSchema, async () => ({
  tools: designTools.map((t) => ({
    name: t.name,
    description: t.description,
    inputSchema: t.inputSchema,
  })),
}));

server.setRequestHandler(CallToolRequestSchema, async (request) => {
  const tool = toolMap.get(request.params.name);
  if (!tool) {
    return {
      isError: true,
      content: [{ type: "text", text: `Unknown tool: ${request.params.name}` }],
    };
  }
  try {
    await guard(request.params.name, (request.params.arguments ?? {}) as Record<string, any>);
    const content = await tool.handler((request.params.arguments ?? {}) as Record<string, any>);
    return { content };
  } catch (err: any) {
    return {
      isError: true,
      content: [{ type: "text", text: `Error: ${err?.message ?? String(err)}` }],
    };
  }
});

async function main() {
  const transport = new StdioServerTransport();
  await server.connect(transport);
  // Logs go to stderr so they never corrupt the stdio protocol on stdout.
  console.error(`BlockbenchMCP server ready. Bridging to Blockbench at ${BASE_URL}`);
}

main().catch((err) => {
  console.error("Fatal error starting BlockbenchMCP server:", err);
  process.exit(1);
});
