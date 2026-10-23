# Referência local — 22/10/2026

Leia somente ao retomar a preparação pessoal já feita neste computador. Revalide caminhos, Git, processos e ferramentas; não trate estes dados como estado atual garantido ou permissão para atuar em outros projetos.

## Acervo e descoberta

- Biblioteca: `C:/Users/joaop/Documents/Skills para IA`.
- Skills compartilhadas: `02-Skills-Compartilhadas/acervo-ia/SKILL.md` e `02-Skills-Compartilhadas/eficiencia-memoria-ia/SKILL.md` dentro da biblioteca.
- Catálogo: `02-Skills-Compartilhadas/acervo-ia/references/catalogo.json`.
- Memória central: `03-Documentacao/acervo-ia-memoria.md`.
- Na preparação anterior, não havia roteamento OmniRoute operacional comprovado. Preferência por OmniRoute não autoriza trocar o modelo ou simular roteamento.

## Antigravity e checkout de design

- Executável observado: `C:/Users/joaop/AppData/Local/Programs/antigravity/Antigravity.exe`.
- Modelo preservado na preparação: Gemini 3.8 Flash High. Não é uma recomendação automática para todas as tarefas.
- Novo projeto App: **Bleach-Modpack-design**, ID `20efb9bd-3f25-4bb8-bc80-7d0edcba2ddb`.
- Checkout: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`.
- Branch: `codex/design-poderes-criaturas`, HEAD observado `830ad64`.
- Checkout do Codex: pasta irmã `Bleach-Modpack`, branch `Feature-Poderes-bankais`, base `14480c5` com trabalho não commitado do ciclo93. Nunca editar esse checkout a partir da frente de design por conveniência.
- Divergência observada:7 commits exclusivos Feature /1 design; nenhuma sincronização automática foi feita.

Instruções da frente: GEMINI.md e `.agents/skills/bleach-design-lab/SKILL.md` no checkout design. Elas não substituem a verificação do estado atual.

## Conexões comprovadas

1. **bleach-blockbench**:44 ferramentas de autoria/leitura, conectadas ao Blockbench5.2.2 pelo bridge local127.0.0.1:8787. `execute_script` não exposto; gravação restrita ao laboratório de design e mutações em modelo `ag_`. Arquivo/configuração não equivale a conexão em outra conversa.
2. **bleach-design-runtime**:2 ferramentas, `start_design_task(mode=Check|GameTests|ClientQa)` e `design_task_status(job_id)`. Comandos fixos, branch/raiz verificadas e logs em Docs/poderes/qa/antigravity. Não é terminal arbitrário.

Instalação observada: `C:/Users/joaop/.gemini/config/mcp/blockbench-bleach`. Config global: `C:/Users/joaop/.gemini/config/mcp_config.json`; equivalente local `.agents/mcp_config.json`. Backups preservados. Permissões dos dois servidores limitadas ao projeto design; não ampliar a todos os MCPs ou projetos.

Uma conversa antiga não via o catálogo novo, apesar de Ready. Nova conversa passou a chamar o MCP nativo. O AgentAPI usado fora do terminal do agente falhou por ausência de ANTIGRAVITY_LS_ADDRESS; a preparação enviou prompts pela interface nativa, explicitamente autorizada pelo usuário. Não inventar endpoints internos.

## Evidências e lições verificadas

- Fonte Blockbench MCP copiada do acervo, dependências instaladas com npm ci --ignore-scripts;73 testes upstream passaram. Rejeição de save fora do laboratório e de tarefa runtime arbitrária verificada.
- O Antigravity criou clipe2s com12keyframes e6poses reais. Geometria29cubos/outliner e dados do clipe anterior preservados, exceto seleção de editor. Poses início/fim idênticas na região do modelo; mandíbula aberta visível. Não é vídeo contínuo ou integração de modelo novo ao jogo.
- Check/build da branch design:137 regressões aprovadas. Não confundir com141 regressões da Feature em outra rodada.
- Duas tentativas pelo terminal/background task carregaram Forge sem janela utilizável; causas específicas não comprovadas. Lançamento pelo MCP runtime expôs janela e mundo real. Primeiro Continue da acessibilidade foi feito por Codex via controle nativo; depois o bootstrap procedeu automaticamente.
- Runtime MCP: exit0,6cenários Hyōrinmaru com75PNG distribuídos19/11/12/13/13/7, Benihime diurna/noturna e acompanhamento, total919PNG, autoexit. Isso verifica execução da versão antiga da branch, não aprova todo seu design nem integra o modelo de laboratório.
- Launcher `tools/antigravity/Invoke-Design.ps1`: Java17 existente, logs stdout/stderr separados, clientes visíveis, build oculto. Inicialização mínima de opções para perfil QA novo implementada depois da prova; esse caminho novo não foi reexecutado.
- `analyze_animation` acusava deslocamento zero em mandíbula rotacional. Poses demonstraram abertura; não deformar o rig para satisfazer uma heurística que não mede esse fenômeno.

## Fonte de continuidade e pendências

Documento94 em `Docs/poderes/94-antigravity-design-onboarding-2026-10-22.md` nos dois checkouts. Evidências canônicas do design em Docs/poderes/qa/antigravity; auditoria do Codex em Docs/poderes/qa/antigravity-host no checkout principal.

Pendentes naquela rodada: formato GeckoLib no editor, exportação/integração de asset real, sincronização revisada das branches, Photon separado e dedicado/observador. GameTests não executados nessa preparação. Zaraki preservada; Aizen adiado, Gin removido, Mentor/Proficiências por último aguardando equipe. Estas são decisões desse projeto, não regras globais para o Antigravity pessoal.
