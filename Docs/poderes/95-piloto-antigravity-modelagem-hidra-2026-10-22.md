# 95 — Piloto de capacidade do Antigravity: cabeça da hidra

22/10/2026. Pedido: preparar um prompt e orientar a execução enquanto o usuário testa o mod. Esta rodada cria somente este documento; não envia o prompt nem inicia o exercício.

## Prontidão e objetivo

Já foram comprovados leitura de contexto, autoria simples Blockbench/MCP, salvamento de clipe/poses, check/build137 regressões e lançamento do cliente real pelo MCP de runtime. Isso demonstra utilidade para tarefas delimitadas, ainda sem comprovar autonomia de produção, qualidade artística consistente ou integração GeckoLib de um asset novo.

O piloto deve entregar uma melhoria visual revisável, preservando a versão testada pelo usuário. Avaliar julgamento e resultado; quantidade de ferramentas ou de cubos não mede qualidade.

## Passo a passo

1. Abrir **Bleach-Modpack-design** no Antigravity, evitando o projeto antigo Bleach-Modpack.
2. Criar uma conversa nova nesse projeto; a sessão antiga pode manter catálogo MCP desatualizado.
3. Manter o modelo selecionado e abrir o Blockbench com a ponte local disponível. O agente verificará a conexão.
4. Colar o texto da seção seguinte. A conferência de branch não exige pausa para aprovação quando estiver correta.
5. Enquanto o usuário testa Minecraft, permitir somente laboratório/editor e documentação desta frente. Nenhum Minecraft/Gradle adicional ou integração em produção.
6. Trazer relatório e caminhos das evidências ao Codex para auditoria independente.

## Prompt para copiar no Antigravity

Execute um piloto de modelagem e animação para avaliarmos sua capacidade de colaborar no Bleach Mod. O usuário está testando Minecraft em paralelo; preserve essa sessão.

Seu checkout exclusivo é C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design, branch codex/design-poderes-criaturas. Leia GEMINI.md, a skill .agents/skills/bleach-design-lab/SKILL.md e as partes pertinentes do documento94 em Docs/poderes. Confira cwd, branch, HEAD e status/diff. Se raiz ou branch estiverem diferentes, informe a divergência sem trocar automaticamente. Não está sozinho: não escreva no checkout Bleach-Modpack do Codex nem desfaça mudanças de outros.

Objetivo: melhorar VISIVELMENTE a cabeça central da hidra e sua animação de mandíbula. Analise o modelo tools/models/lab/antigravity/ag_hydra_jaw.bbmodel e a arte Docs/poderes/media/concept/concept_bankai_hydra.jpg; observe as imagens antes de concluir algo sobre aparência. Use também as evidências em Docs/poderes/qa/antigravity/mcp-exercise-report.md.

Trabalhe somente em uma NOVA cópia dentro de tools/models/lab/antigravity/piloto01/, com projeto ag_hydra_head_v2 e arquivo ag_hydra_head_v2.bbmodel. Preserve o modelo anterior e seus clipes. Evidências e relatório somente em Docs/poderes/qa/antigravity/piloto01/.

Identifique os principais defeitos do modelo anterior e implemente as melhorias prioritárias na silhueta, transição cabeça/pescoço, mandíbula, cavidade da boca, dentes e leitura de gelo/cristais. Respeite o estilo Minecraft; não refaça a hidra inteira. Justifique complexidade e escolhas. Não basta repetir o clipe anterior ou entregar um plano.

Apresente uma recomendação breve e continue as decisões reversíveis dentro deste escopo. Discorde de sugestões que não ajudem, explicando o motivo e uma alternativa; não invente objeções. Pergunte apenas se faltar informação essencial. Nos demais detalhes, escolha uma hipótese razoável e registre-a.

Use MCP nativo bleach-blockbench: get_status/list_formats e conferência do projeto ativo antes de mutações; abra nova aba/cópia, sem alterar a aba de outro autor. execute_script permanece desligado/proibido; não contorne por scripts dentro do editor ou HTTP ad hoc. Se GeckoLib estiver ausente, use o formato de laboratório disponível e informe o limite, sem fingir exportação válida. Não instale plugins nesta rodada.

NÃO execute Gradle, Minecraft, servidores ou o MCP bleach-design-runtime. Não altere src/, modelos de produção, gameplay, Zaraki, áudio, dependências, configurações globais, conta ou modelo/provedor. Não envie arquivos a serviços externos. Sem commit, push, merge, rebase, reset ou stash.

Crie um clipe curto de preparação, abertura para rugido e recuperação, com pivôs coerentes e interpolação suave. Escolha a duração e explique-a. Compare antes/depois com orientação, projeção e enquadramento equivalentes. Capture frente, perfil e3/4; depois preview_animation de pelo menos5 instantes relevantes em duas vistas, incluindo abertura máxima e retorno. Inspecione as imagens: dentes não devem atravessar a boca, mandíbula não deve descolar, e a recuperação precisa ser coerente. Confronte avisos automáticos com o que realmente medem; não deforme um rig correto apenas para apagar um aviso.

Salve pelo MCP e reabra sua cópia para confirmar persistência de geometria, textura, rig e clipe. Poses amostradas não comprovam fluidez contínua; não afirme ter assistido a vídeo ou testado no Minecraft.

Entregue a fonte editável, capturas reais e Docs/poderes/qa/antigravity/piloto01/relatorio.md contendo branch/HEAD, arquivos e ferramentas usados, diagnóstico inicial, justificativas, comparação visual com tempos/caminhos, verificação da cópia reaberta, defeitos restantes e próximo teste. Separe o que foi observado do que é hipótese. Se algo falhar, preserve a entrega parcial e explique o erro exato. Não marque aprovado pelo usuário nem pronto para produção. Termine esta rodada sem iniciar outras tarefas.

## Como avaliaremos

- Preservação dos originais, produção, branch e teste do usuário.
- Diagnóstico coerente com o modelo/referência efetivamente observados.
- Escolhas fundamentadas, perguntas úteis e independência sem oposição automática.
- Melhoria visível em comparação equivalente, não apenas mais detalhes.
- Pivôs, contato e recuperação legíveis nas poses, com limites de amostragem declarados.
- Cópia reaberta conservando a autoria.
- Relatório honesto, com defeitos e próximo teste específico.

Ainda não há resultado deste piloto. Não atribuir notas antes de revisar a entrega. Depois de aprovado, testar outra pequena parte para avaliar transferência; exportação/integração são etapas próprias. A fila obrigatória do roadmap72 permanece preservada.
