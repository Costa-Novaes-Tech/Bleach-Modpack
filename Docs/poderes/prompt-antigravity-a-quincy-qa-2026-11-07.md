# Antigravity A — QA do Vollständig e soltura da flecha Quincy

Trabalhe no Bleach-Modpack, no checkout `Bleach-Modpack-quincy-refino-v11`, branch `codex/quincy-vollstandig-refino-v11`. Preserve o refinamento local existente. Sua responsabilidade nesta rodada cobre assets, apresentação e bancada de QA Quincy. Mantenha dano, alcance, custos, Ginrei e outras frentes fora do escopo. Registre o resultado em `Docs/poderes`. A entrega é para revisão na branch individual. Qualquer merge depende de autorização.

## Comece aqui

1. Mostre o caminho do checkout, branch, HEAD e alterações preexistentes com `git status --short --branch`, `git rev-parse HEAD` e `git worktree list`. Na nuvem, confirme o commit e os assets disponíveis. Se o refinamento local estiver ausente, identifique os arquivos que faltam antes de editar.
2. Consulte `Docs/poderes/00-CONTINUIDADE-CODEX.md`, `Docs/poderes/skills/bleach-antigravity-entrega/SKILL.md` e `Docs/poderes/guia-transformacoes-modelagem-vfx-qa-2026-11-06.md`, se disponíveis. Limite a leitura às seções da tarefa. As autorizações históricas desses arquivos valem para seus respectivos ciclos.
3. Localize o capturador e o empacotador existentes. Registre o cenário, o tempo efetivo do clipe, os quadros obrigatórios e os arquivos que pretende alterar.

## A1 — fechar a captura da transformação

- Investigue o histórico de endpoints ausentes na abertura e no retorno, registrados como t40 e t16 no relatório anterior. Confirme o significado desses valores na bancada atual e capture os dois marcos a partir do relógio do clipe.
- Em uma cópia descartável dos artefatos, retire um quadro obrigatório. O empacotador deve retornar código diferente de zero e indicar o quadro ausente. Repita com o conjunto completo e registre a saída de sucesso.
- Compare frente e perfil com a mesma iluminação, mão dominante e tempo de clipe. Entregue uma captura contínua com duração e FPS efetivos. Indique quais marcos foram observados no cliente Forge e quais foram examinados apenas por quadro ou no editor.

## A2 — provar uma ação do kit no jogo

Após A1, ative o Vollständig e execute a ação de flecha com o arco real do kit. Registre preparação, mão e arco, soltura e retorno. Faça a primeira passagem com VFX reduzido para expor o contato e repita com a apresentação completa. Verifique a âncora real da flecha, a mão dominante e a vista lateral. Se encontrar desalinhamento, localize a primeira etapa em que aparece antes de corrigir.

## Entrega

Rode `gradlew check` e o cenário pertinente no Forge. Entregue SHA, lista de arquivos alterados, comando e código de saída de cada prova, identificação do JAR e caminhos para logs, quadros brutos e captura do mesmo ciclo. Distinga regressão, GameTest, preview e cliente real. Se o cliente gráfico ou o Blockbench estiver indisponível, entregue o que pôde comprovar e marque a parte visual como pendente. Inclua um defeito que as provas atuais ainda poderiam deixar passar.
