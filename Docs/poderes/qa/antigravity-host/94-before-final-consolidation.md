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
- Cliente QA isolado iniciado pelo Antigravity, com flags existentes `-PbenihimeQa=true -PhyorinmaruQa=true -PqaAutoExit=true`; resultado final será acrescentado após concluir. Não integra o modelo experimental ao jogo.

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
