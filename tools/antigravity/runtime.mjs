import {Server} from '@modelcontextprotocol/sdk/server/index.js';
import {StdioServerTransport} from '@modelcontextprotocol/sdk/server/stdio.js';
import {ListToolsRequestSchema,CallToolRequestSchema} from '@modelcontextprotocol/sdk/types.js';
import {spawn,execFileSync} from 'node:child_process';
import fs from 'node:fs';
import path from 'node:path';
const root=fs.realpathSync(process.env.BLEACH_DESIGN_ROOT);
const expected='C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design';
if(root.replaceAll('\\','/').toLowerCase()!==expected.toLowerCase())throw Error('Unexpected design root');
const out=path.join(root,'Docs/poderes/qa/antigravity/runtime-mcp');fs.mkdirSync(out,{recursive:true});
const tasks=new Set(['Check','GameTests','ClientQa']);
const jobs=new Map();
const server=new Server({name:'bleach-design-runtime',version:'1.0.0'},{capabilities:{tools:{}}});
const definitions=[{name:'start_design_task',description:'Run only the reviewed design launcher in its fixed checkout/branch. Check=check/build, GameTests=discardable server, ClientQa=visible existing Minecraft QA with auto exit. One task at a time; no arbitrary command.',inputSchema:{type:'object',properties:{mode:{type:'string',enum:[...tasks]}},required:['mode'],additionalProperties:false}},{name:'design_task_status',description:'Read status and last log lines of a job created by this server, without launching anything.',inputSchema:{type:'object',properties:{job_id:{type:'string'}},required:['job_id'],additionalProperties:false}}];
const reply=x=>({content:[{type:'text',text:JSON.stringify(x,null,2)}]});
server.setRequestHandler(ListToolsRequestSchema,async()=>({tools:definitions}));
server.setRequestHandler(CallToolRequestSchema,async req=>{
 try {
  const args=req.params.arguments??{};
  if(req.params.name==='start_design_task') {
   if(!tasks.has(args.mode))throw Error('Unsupported task');
   if([...jobs.values()].some(j=>j.state==='running'))throw Error('Another MCP task is active');
   const branch=execFileSync('git',['-C',root,'branch','--show-current'],{encoding:'utf8',windowsHide:true}).trim();
   if(branch!=='codex/design-poderes-criaturas' && branch!=='codex/uryu-arqueiro-tradicional')throw Error('Wrong branch');
   const id=new Date().toISOString().replaceAll(/[:.]/g,'-')+'-'+args.mode;
   const log=path.join(out,id+'.log');const fd=fs.openSync(log,'wx');
   const shell=path.join(process.env.SystemRoot,'System32/WindowsPowerShell/v1.0/powershell.exe');
   const child=spawn(shell,['-NoLogo','-NoProfile','-ExecutionPolicy','RemoteSigned','-File',path.join(root,'tools/antigravity/Invoke-Design.ps1'),'-Mode',args.mode],{cwd:root,windowsHide:args.mode!=='ClientQa',stdio:['ignore',fd,fd]});
   fs.closeSync(fd);
   const job={job_id:id,mode:args.mode,pid:child.pid,state:'running',started:new Date().toISOString(),log};jobs.set(id,job);
   const persist=()=>fs.writeFileSync(path.join(out,id+'.json'),JSON.stringify(job,null,2));persist();
   child.on('error',e=>{job.state='failed';job.error=e.message;persist();});
   child.on('exit',(code,signal)=>{job.state=code===0?'passed':'failed';job.exit_code=code;job.signal=signal;job.finished=new Date().toISOString();persist();});
   return reply(job);
  }
  if(req.params.name==='design_task_status') {
   const id=String(args.job_id);
   if(!/^[a-zA-Z0-9T-]+$/.test(id))throw Error('Invalid job id');
   const file=path.join(out,id+'.json');if(!fs.existsSync(file))throw Error('Unknown job');
   const job=JSON.parse(fs.readFileSync(file,'utf8'));
   const log=path.join(out,id+'.log');
   return reply({...job,tail:fs.existsSync(log)?fs.readFileSync(log,'utf8').split(/\r?\n/).slice(-25).join('\n'):''});
  }
  throw Error('Unknown tool');
 }catch(e){return {isError:true,content:[{type:'text',text:e.message}]};}
});
await server.connect(new StdioServerTransport());
