# 94 — Preparação Antigravity / design

22/10/2026. Autorização humana: preparar Antigravity, conexões, prompts e exercício Blockbench em branch separada. Não é treinamento de pesos; são regras, skill, conexão e prática verificável.

## Estado e separação
Codex: Bleach-Modpack, Feature-Poderes-bankais, 14480c5 + ciclo93 não commitado. Antigravity: Bleach-Modpack-design, codex/design-poderes-criaturas, 830ad64. Divergência 7/1; preservar commit próprio de design, sem sincronização implícita. Portanto o cliente desta branch pode demonstrar versão anterior da hidra/áudio. Leia ciclo93 do checkout principal somente como referência; não copie produção unilateralmente.

## Recursos preparados
GEMINI.md e skill bleach-design-lab são específicos desta frente. As skills globais acervo-ia/eficiencia já existem no Antigravity. A skill bleach-mod-dev está no repositório; versões e comandos precisam ser conferidos localmente. Node22 e Java17 existentes preservados.

MCP stdio compila cópia de blockbench-mcp-main0.3.1 do acervo em ~/.gemini/config/mcp/blockbench-bleach. Fonte MIT/upstream https://github.com/sosadly/blockbench-mcp. Transporte HTTP exclusivamente 127.0.0.1:8787. Wrapper expõe subconjunto de autoria, sem execute_script/delete/install_plugin, gravações somente no laboratório do checkout de design e mutações só em projeto ag_. Isto reduz erros de edição do editor compartilhado, não substitui segurança de filesystem da conta Windows.

Config local .agents/mcp_config.json; App pode requerer recarregar conexões. Configuração declarada deve ser distinguida de handshake MCP e de uso comprovado pelo agente.

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

## Operação comprovada / limitações intermediárias

Instalação por cópia isolada + npm ci --ignore-scripts (95 dependências) e tsc aprovados. Suite upstream:73/73. Handshake SDK stdio44tools e recusa de save fora da lab aprovados. Antigravity App exibiu MCP Ready44; uma conversa já aberta ainda não via o catálogo. Abrir uma nova conversa no projeto resolveu: get_status e list_formats nativos executados pelo próprio Antigravity. Sem troca do Gemini3.8 Flash High. AgentAPI CLI fora do terminal do agente respondeu ANTIGRAVITY_LS_ADDRESS ausente; prompts foram executados pela interface nativa, sem presumir conexão CLI.

Registro global MCP tem backup mcp_config.json.pre-bleach-setup.bak; configuração local equivalente também existe. Novo projeto20efb9bd-3f25-4bb8-bc80-7d0edcba2ddb teve apenas defaultBranch corrigido de main para codex/design-poderes-criaturas, com backup. Projetos antigos preservados.
