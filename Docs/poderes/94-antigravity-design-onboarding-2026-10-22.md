# 94 — Preparação Antigravity / design

22/10/2026. Autorização humana: preparar Antigravity, conexões, prompts e exercício Blockbench em branch separada. Não é treinamento de pesos; são regras, skill, conexão e prática verificável.

## Estado e separação
Codex: Bleach-Modpack, Feature-Poderes-bankais, 14480c5 + ciclo93 não commitado. Antigravity: Bleach-Modpack-design, codex/design-poderes-criaturas, 830ad64. Divergência 7/1; preservar commit próprio de design, sem sincronização implícita. Portanto o cliente desta branch pode demonstrar versão anterior da hidra/áudio. Leia ciclo93 do checkout principal somente como referência; não copie produção unilateralmente.

## Recursos preparados
GEMINI.md e skill bleach-design-lab são específicos desta frente. As skills globais acervo-ia/eficiencia já existem no Antigravity. A skill bleach-mod-dev está no repositório; versões e comandos precisam ser conferidos localmente. Node22 e Java17 existentes preservados.

MCP stdio compila cópia de blockbench-mcp-main0.3.1 do acervo em ~/.gemini/config/mcp/blockbench-bleach. Fonte MIT/upstream https://github.com/sosadly/blockbench-mcp. Transporte HTTP exclusivamente 127.0.0.1:8787. Wrapper expõe subconjunto de autoria, sem execute_script/delete/install_plugin, gravações somente no laboratório do checkout de design e mutações só em projeto ag_. Isto reduz erros de edição do editor compartilhado, não substitui segurança de filesystem da conta Windows.

Config local .agents/mcp_config.json; App pode requerer recarregar conexões. Configuração declarada deve ser distinguida de handshake MCP e de uso comprovado pelo agente.

Também registrado no MCP global do App, preservando backup `~/.gemini/config/mcp_config.json.pre-bleach-setup.bak`. Permissão `mcp(bleach-blockbench/*)` limitada ao novo projeto; sem autorização global de todos os MCPs. Novo projeto App `20efb9bd-3f25-4bb8-bc80-7d0edcba2ddb`, defaultBranch corrigida para design, projetos anteriores intactos.

## Resultados da preparação

- Servidor instalado como cópia do acervo original: `npm ci --ignore-scripts --no-audit --no-fund`,95 dependências; TypeScript compilado,73 testes upstream aprovados. Handshake stdio44tools, leitura do editor e recusa de save fora do laboratório comprovados.
- A conversa inicial mantinha catálogo antigo; nova conversa `MCP Integration Test Bleach Design` (`ad5d22db-aff7-4c6b-9dd4-913bea391a01`) passou a executar MCP nativo. Gemini3.8 Flash High preservado. AgentAPI externo não teve `ANTIGRAVITY_LS_ADDRESS`; prompts executados pela interface nativa.
- Antigravity carregou a cópia `ag_hydra_jaw`, criou `animation.hydra_lab.jaw_roar_2s`,12 keyframes Catmull–Rom, preservou geometria29cubos/outliner e dados do clipe1,6s. Somente metadado `selected` do clipe anterior mudou. Arquivo salvo pelo MCP. Três ângulos e seis poses0/0,8/2s gravados no QA da branch design.
- Auditoria independente Codex: poses inicial/final têm diferença média0 na região do modelo; boca aberta difere visualmente. São poses amostradas reais do Blockbench, não vídeo contínuo. `analyze_animation` acusa deslocamento0 em clipes rotacionais: heurística não mede adequadamente esta mandíbula; não aumentar amplitude só para satisfazê-la.
- Launcher `tools/antigravity/Invoke-Design.ps1` na branch design verifica raiz/branch/Java17, separa logs stdout/stderr e oferece Check/GameTests/Client/ClientQa/DryRun. Corrigido erro do primeiro ensaio em que stderr nativo interrompia PowerShell; tentativa interrompida preservada. Nova execução do próprio Antigravity: **check/build aprovados,137 regressões,45s**. Isto é a versão830ad64, não a Feature com141 regressões.
- Cliente real lançado pelo próprio Antigravity através do novo MCP `bleach-design-runtime`: job `2026-10-22T17-57-02-533Z-ClientQa`, exit0, Gradle5m25s,6 cenários Hyōrinmaru concluídos e75 PNGs distribuídos19/11/12/13/13/7; Benihime diurna/noturna e acompanhamento concluídos. Total919 PNGs no ambiente descartável. Não integra o modelo experimental ao jogo. Primeira tela de acessibilidade concluída por Codex via controle nativo; o restante foi automatizado.

Relatórios e prompts completos ficam em `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/94-antigravity-design-onboarding-2026-10-22.md` e `Docs/poderes/qa/antigravity` daquele checkout. A fonte do wrapper está arquivada em `tools/antigravity/blockbench-mcp/index.scoped.ts`.

## Continuidade da frente

Retomar com GEMINI.md → skill bleach-design-lab → contratos relevantes de Docs/poderes → git status/diff → fonte editável/rig/UV → codec exportação → controlador runtime → cliente base/Photon separado → observador dedicado. Não tratar catálogo de ferramentas como ferramentas todas instaladas. GeckoLib no editor continua ausente; instalação oficial/exportação e integração ainda são etapas próprias. Acervo global e configurações existentes preservados; sem troca de modelo/provedor, sem main/Zaraki, sem commit/push/merge. A autorização de design é específica do novo checkout e não muda a branch do Codex. Roadmap obrigatório72 preservado.

## Exercício de aceitação
1. Ler contexto, confirmar cwd/branch/HEAD, relatar divergência e acesso ao acervo sem carregar tudo.
2. MCP get_status/list_formats; abrir cópia ag_hydra_jaw.bbmodel. Listar bones, verificar mandíbula, adicionar novo clipe não destrutivo de 2s com abertura suave e retorno à pose neutra. Capturar frente/3 quartos; salvar só laboratório. Evidência em Docs/poderes/qa/antigravity.
3. Rodar check/build do checkout design e registrar exit/log; não mascarar falhas. Abrir cliente real com runClient e validar laboratório existente descoberto no código. Não alegar modelo novo no Minecraft: não houve exportação/integracao desse ensaio.
4. Avaliar GeckoLib oficial e integração em etapa posterior, sem alterar Zaraki ou gameplay agora. Futuro asset: fonte editável + rig/UV + export correto + controlador + teste base/Photon + observador dedicado.

## Fontes
https://antigravity.google/docs/mcp
https://www.antigravity.google/docs/commands/learn
Docs/poderes/82-nova-frente-design-poderes-criaturas-blockbench-2026-10-21.md
Ciclo93 no checkout Codex. Resultado efetivo de cada etapa será registrado ao final desta preparação.

## Launcher e prompts reutilizáveis

`tools/antigravity/Invoke-Design.ps1 -Mode Check|GameTests|Client` valida raiz/branch e Java17, recusa cliente já ativo, serializa suas próprias tarefas e grava logs nesta frente. `-DryRun` já verificou caminho, branch e JDK sem executar build. Não substitui a revisão do cenário de QA. Não use o launcher do checkout Codex.

### Retomada de design
Leia GEMINI.md, skill bleach-design-lab e continuidade pertinente; confirme branch/cwd/HEAD/status e diff dos arquivos alvo. Identifique o contrato de bones/UV/escala e o controlador runtime. Consulte somente acervo relacionado. Entregue fonte editável e evidências com origem, sem alterar gameplay ou outros autores incidentalmente. Se a ferramenta falhar duas vezes pelo mesmo motivo, informe o erro concreto e faça diagnóstico direcionado, evitando exploração repetitiva.

### Prova de autoria
Use MCP bleach-blockbench nativo; get_status/list_formats, load_project na cópia ag_ autorizada, list_outliner/list_animations. Preserve clipe existente, acrescente clipe curto suave com pivô correto, capture frente/perfil/3 quartos, salve lab. Registre medidas, nomes de bones, keyframes e caminhos. Não use execute_script nem edite a produção nesse ensaio.

### Build e cliente
Confirme cwd/branch, então execute Invoke-Design.ps1 -Mode Check e registre log/exit/regressões. Descubra comandos QA no código antes de usar runClient. Mundo descartável; não abra saves pessoais. Captura real do cliente com versão/hash e diferença entre modelo atual e laboratório. Feche o cliente ao concluir. Base e Photon são avaliações separadas; não declare dedicado/multiplayer sem observar dois clientes.

### Handoff para Codex
Liste branch/HEAD, arquivos alterados, fonte/export/rig/textura/clipes, testes executados e pendentes, screenshots editor/jogo separados e risco de controladores sobrescreverem animação. Não faça commit/push/merge automático. O roadmap obrigatório72 permanece na mesma ordem; esta preparação não homologa redesign ou muda prioridade.


## Fechamento — runtime MCP e limites

Além da ponte Blockbench44tools, criado MCP local `bleach-design-runtime` com apenas2tools: `start_design_task(mode=Check|GameTests|ClientQa)` e `design_task_status(job_id)`. Fonte em tools/antigravity/runtime.mjs, instalação ao lado da ponte. Sem comando arbitrário; raiz/branch fixas, execução serializada pelo launcher, logs/job JSON persistentes. Handshake e rejeição de tarefa arbitrária verificados. Permissão mcp(bleach-design-runtime/*) concedida somente ao projeto design, com configurações antigas preservadas.

Duas tentativas pelo terminal/background task carregaram Forge sem janela utilizável e foram encerradas por Codex após verificar PID/path/flags. Mudar Hidden para Normal não resolveu nesse fluxo. Não foi demonstrada a causa de baixo nível; não atribuir certeza a GLFW ou swapchain. Nova rota MCP exibiu janela real id789308 no desktop. Captura de tela inicial e de mundo real preservadas. Primeiro Continue da acessibilidade foi feito por Codex, não pelo Antigravity. Launcher agora cria options mínimo somente quando ausente no perfil QA descartável (onboardAccessibility:false e pauseOnLostFocus:false); preferências existentes/pessoais preservadas. Esse caminho de perfil novo não foi reexecutado, pois o perfil desta prova já foi inicializado.

O próprio Antigravity consultou design_task_status e confirmou passed/exit0; logs independentes comprovam conclusão dos6cenários/autoexit. Código Java/recursos do mod não foram alterados nesta preparação. Logs rastreados gerados foram arquivados em QA e restaurados à base limpa inicial, com filtros de finais de linha Git; testes temporários da raiz arquivados. Sem processo Minecraft/Gradle deixado rodando pela prova.

Evidências da frente design: Docs/poderes/qa/antigravity/{mcp-exercise-report.md,baseline-runtime.md,runtime-mcp-report.md,codex-pose-audit.json,final-verification.json}, além dos logs de Check/ClientQa e captures em run-benihime-qa/base. Snapshot de auditoria do host Codex em Docs/poderes/qa/antigravity-host. Não são vídeos contínuos ou aceite artístico integral; é prova da infraestrutura/editor/execução. Quantidade de arquivos não substitui revisão visual.

Pendências específicas: instalar e validar formato GeckoLib no editor pela origem oficial; exportar e integrar um asset real respeitando bones/UV/controladores; revisar sincronização Feature/design (7/1 + ciclo93 não commitado), sem merge automático; QA dedicado/observador e Photon separado não executados nesta frente. GameTests do design não foram executados nesta preparação. O roadmap obrigatório72 permanece na mesma ordem.
