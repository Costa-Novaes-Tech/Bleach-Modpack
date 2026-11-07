# Antigravity B — fixtures P0 e persistência real da missão da Rukia

Trabalhe no Bleach-Modpack, no checkout `Bleach-Modpack-design`, branch `codex/senbon-hakuteiken-blockbench`. Use `8e221002` como referência da entrega anterior e confirme o HEAD real. A missão `MAIN-SUB-01` já está integrada à branch principal de trabalho. Seu escopo é testar a implementação existente. Restrinja mudanças a fixtures, testes, scripts e ao seu relatório em `Docs/poderes`. Use cópias descartáveis dos mundos. Qualquer merge depende de autorização.

## Comece aqui

1. Mostre checkout, branch, HEAD e alterações preexistentes com `git status --short --branch`, `git rev-parse HEAD` e `git worktree list`. Na nuvem, confirme acesso ao commit e às fixtures. Preserve o trabalho local de outras frentes.
2. Consulte as seções de P0 e Rukia de `Docs/poderes/00-CONTINUIDADE-CODEX.md`, `Docs/poderes/p0-auditoria-dados-saves-2026-11-05.md` e `Docs/poderes/skills/bleach-antigravity-entrega/SKILL.md`. As autorizações históricas desses arquivos valem para seus respectivos ciclos.
3. Identifique o validador, uma fixture conhecida e o caminho para reproduzir a entrega final da missão. Registre os estados esperados e os arquivos que pretende alterar.

## B1 — provar que o validador rejeita entradas inválidas

Faça o validador falhar para fixture incompleta e schema incompatível. Rode em cópias descartáveis uma fixture válida e casos inválidos. Registre o comando, o código de saída e o campo ou contrato apontado em cada falha. Derive a contagem dos arquivos realmente verificados a partir da execução.

## B2 — provar persistência em conexão e reinício reais

Após B1, abra uma cópia de uma fixture de `MAIN-SUB-01`, faça login, conclua a entrega, saia, feche a sessão e reabra a mesma cópia. Compare UUID, progresso da missão, checkpoint, saldo, inventário e Asauchi no chão antes e depois. Repita o diálogo final e confira a contagem exata de recompensas. Faça também o caso com inventário cheio. Se houver ambiente para duas conexões, confira que progresso e recompensa são individuais. Registre separadamente migração de ranks, recompensas de Maestria e normalização racial como decisões de produto fora desta rodada.

## Entrega

Entregue SHA, mudanças, comandos, códigos de saída, identificação do JAR, logs e estados antes e depois. Diferencie GameTest, reconexão à mesma sessão e reinício que reabre a cópia no disco. Quando faltar infraestrutura, registre o caso como pendente e explique o que as provas disponíveis sustentam. Inclua um defeito relevante que as provas atuais ainda poderiam deixar passar.
