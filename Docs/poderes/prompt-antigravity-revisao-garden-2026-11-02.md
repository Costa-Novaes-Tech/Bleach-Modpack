# Revisão corretiva Antigravity — Garden Senbon — 02/11/2026

## Objetivo

Revisar `codex/senbon-garden-prisao` (`8543d6d`) à luz da auditoria Codex de 02/11. Corrigir somente lacunas demonstradas, executar QA reproduzível e, se todos os portões passarem, integrar Garden na `Feature-Poderes-bankais`. O usuário autorizou explicitamente esse merge e a resolução semântica de conflitos. **Nunca integrar na `main`.**

## Ambiente, nuvem e preservação

1. Verifique se o Antigravity oferece nesta sessão um agente/checkout em nuvem conectado ao repositório Git correto. Se houver, prefira tarefa isolada em nuvem para evitar disputar checkout local. Confirme branch remota, HEAD e capacidade real de commit/merge; botão ou modelo disponível não prova que o modo em nuvem está operacional.
2. Se nuvem não estiver disponível, registre o limite e use apenas o checkout individual já atribuído a esta frente, depois de confirmar que está livre. Não crie worktree novo, não compartilhe checkout ativo e não encerre processos de outra conversa.
3. `Feature-Poderes-bankais` está em `04ef0d1` no checkout local do Codex, com notas documentais locais pendentes. Preserve-as e preserve trabalho de outras conversas. No alvo remoto, atualize refs e inspecione estado antes de operar.
4. Confira status, branch, HEAD, upstream, worktrees, histórico e diff. Não use `git add .`, reset, clean, stash genérico, `ours/theirs` em bloco ou force-push. Se houver conflito ambíguo com trabalho alheio, pare e relate o conflito.
5. As notas locais não comitadas `Docs/poderes/00-CONTINUIDADE-CODEX.md` e `Docs/poderes/continuidade-quincy-senbon-xerj-2026-10-31.md` pertencem ao checkout Codex e talvez não existam no clone em nuvem. Não tente recriá-las, substituí-las ou apagá-las; mantenha sua documentação da revisão em arquivos próprios de `Docs/poderes`.

## Lacunas que motivaram a revisão

- O relatório de `8543d6d` diz que o teste do teto de 16 prova delta exato de 21 HP nos alvos selecionados. No código versionado, `gardenPrisonCapAndTargetSetStrictlySixteenWithExactDeltas` só exige `delta > 0` nos primeiros 16 e zero nos demais/ocluídos. Ajuste teste e relato para medir baseline e validar, em cada alvo capturado, o total esperado do fixture; mantenha zero para alvos fora do teto e atrás da parede. Explicite HP base (`5+3+3+3`) e multiplicador Bankai do fixture; não confunda dano solicitado com HP líquido se defesa/invulnerabilidade afetarem o resultado.
- Em `tickGardenPrison`, alvos com Levitação externa forte (`amplifier > 0`) ou longa não recebem contenção vertical naquele bloco. O teste preserva o efeito externo, mas não monitora a altura desses mesmos alvos. Desenhe e prove solução que preserve o efeito e não deixe a prisão perder silenciosamente o limite `floorY + 2.5` nem permita atravessar teto. Não remova, reduza ou substitua o efeito externo. Teste amplificador maior, duração longa e teto; se o contrato for incompatível, apresente reprodução e pare para decisão em vez de relaxar o limite.
- Falta evidência visual do Garden no cliente Forge no pacote versionado. Teste o fluxo iniciado normalmente pelo servidor, não callback/preview que injete o resultado.

## Escopo preservado

Manter prisão independente de `s.prison`/B; centro fixo; volume e elegibilidade; máximo 16 alvos estáveis; ordem de 5+3+3+3; pulsos t20/t40/t60 relativos à prisão; terceiro pulso antes da limpeza; aliados/NPCs revalidados; piso monotônico visível durante a prisão; limite por piso/teto; efeitos externos protegidos; uma busca espacial na captura e atualizações limitadas aos UUIDs capturados. Não editar combo Senbon, `weapon_attributes`, clipes ou Better Combat. Não ampliar C/Hakuteiken.

## Validação obrigatória

1. Criar/ajustar GameTests de fluxo para total exato por alvo capturado; zero fora do teto/parede; mudança para aliado; t59/t60/t61; elevação sob piso/teto; Levitação externa forte/longa sob o limite; cancelamento/fim sem apagar efeitos externos; independência da prisão B.
2. Rodar no checkout isolado, em sequência, `compileJava`, `check` e `runGameTestServer`. Registrar comando, branch/HEAD, duração, exit code e log bruto de cada etapa. Não reportar números históricos como execução nova.
3. Rodar um cliente Forge real por vez. Ativar Garden pelo fluxo normal e capturar vídeo contínuo da flor, tapete, envoltório, subida, pulsos e fade; incluir quadro geral e close do alvo/teto. Inspecionar capturas e registrar SHA-256, origem, ticks/timestamps, perfil gráfico e limitações.
4. Documentar achados, diff, testes, evidências e falhas em `Docs/poderes`. Atualizar o prompt de Garden com resultado factual; não editar `00-CONTINUIDADE-CODEX.md` em paralelo.

## Integração autorizada

Somente depois dos portões acima passarem, integrar primeiro Garden na `Feature-Poderes-bankais`, sem `main`. Preferir nuvem se houver checkout isolado conectado e integração segura; caso contrário, use o checkout dedicado sem deslocar branch ativa em outro worktree. Resolver conflitos semanticamente, preservando ambas as mudanças; conferir `git diff --check`, ausência de unmerged paths, ancestralidade e histórico final. Não dar push forçado.

Se a integração segura exigir operar no checkout `Feature-Poderes-bankais` já ativo em outro worktree, não tente trocar essa branch a partir de um segundo checkout local. Entregue commit/evidências e reporte o impedimento para o Codex concluir a integração. A autorização de merge não autoriza sobrescrever trabalho concorrente.

## Relatório de entrega

Informe se nuvem estava realmente disponível e foi usada; checkout/branch/base/commits; conflitos e resolução; arquivos; comandos/exit codes; evidências e hashes; estado final da `Feature-Poderes-bankais`; pendências visuais/humanas. Não declarar homologação visual sem sequência contínua observada.
