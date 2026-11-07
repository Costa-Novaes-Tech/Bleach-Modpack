# Antigravity — tarefas do próximo ciclo

Envie uma frente por vez a cada conversa. A e B preservam os checkouts atuais. C entra depois como bancada independente, após a primeira entrega verificável de A e B. Os prompts são para encaminhamento humano.

## Instruções comuns para copiar

> Leia o [contrato de entrega](skills/bleach-antigravity-entrega/SKILL.md), o documento técnico da tarefa e a seção pertinente da continuidade do projeto. Mostre checkout, branch, HEAD e alterações preexistentes antes de escrever. Se estiver na nuvem, confirme que o commit base e os assets chegaram ao ambiente. Quando um cliente gráfico ou MCP local estiver indisponível, entregue a parte verificável e deixe a execução gráfica identificada como pendente.
>
> Antes do patch, registre o comportamento esperado, um controle que falharia diante do defeito e o escopo de arquivos. Ao entregar, informe commit, comando, exit code, versão do JAR e artefatos do mesmo ciclo. Diferencie regressão Java, GameTest, cliente Forge e inspeção de vídeo. Leia os resultados finais no disco. A resposta deve citar um defeito relevante que a sua prova ainda deixaria passar. Entregue o patch na branch individual para revisão. A integração em `Feature-Poderes-bankais` depende da autorização do usuário.

## A — Quincy. Fechar o QA visual e provar uma ação no jogo

> Continue no worktree `Bleach-Modpack-quincy-refino-v11`, branch `codex/quincy-vollstandig-refino-v11`. Preserve o refinamento existente. Use o guia local `Docs/poderes/guia-transformacoes-modelagem-vfx-qa-2026-11-06.md` como método quando estiver disponível. Sua responsabilidade cobre os assets, a bancada e a apresentação visual Quincy.
>
> **Entrega A1.** Corrija a captura para gerar os endpoints reais da abertura e do retorno. Compare os marcos com a mesma câmera, luz, tempo de clipe e mão. Prove que o empacotador retorna falha quando faltar uma imagem obrigatória. Rode novamente com o pacote completo. Inclua uma captura contínua e declare sua duração e FPS efetivos. Confira a transição em frente e perfil no Forge.
>
> **Entrega A2, após A1.** Exercite a ativação do Vollständig e uma ação produtiva do kit, começando pela flecha do arco. Mostre nos mesmos marcos corpo, mão, arco, soltura e retorno, primeiro com o VFX reduzido e depois com a apresentação completa. Use o ponto de ancoragem real e inspecione mão dominante e vista lateral. Se houver desalinhamento, localize a primeira etapa em que ele surge antes de corrigir. Rode `gradlew check` e a prova visual pertinente. Dano, alcance, custos e Ginrei ficam fora deste ciclo.

## B — P0/Rukia. Validar fixture ruim e persistência com reinício real

> Continue no checkout da frente B, branch `codex/senbon-hakuteiken-blockbench`, com o trabalho de `8e221002` como referência. Restrinja a escrita a fixtures, testes, scripts e ao relatório próprio. MAIN-SUB-01 já está integrada. O objetivo é testar a missão existente e a segurança de dados.
>
> **Entrega B1.** Faça o validador rejeitar fixture incompleta ou com schema incompatível. Prove o exit code da negativa e do controle válido em cópias descartáveis. O relatório deve derivar a contagem dos arquivos realmente verificados e apontar qual campo ou contrato falhou.
>
> **Entrega B2, após B1.** Escolha uma fixture de MAIN-SUB-01 e exercite login, entrega final, saída e reabertura real da mesma cópia. Compare UUID, missão e checkpoint, saldo, inventário e Asauchi no chão antes e depois. Repita o diálogo final e verifique a contagem exata, inclusive com inventário cheio. Diferencie GameTest de conexão real. Se o ambiente permitir, execute a prova com dois jogadores e confirme que o progresso e a recompensa são individuais. Registre migração de ranks, recompensas de Mastery e normalização racial como decisões separadas de produto. Preserve os saves pessoais.

## C — laboratório. Comparar um efeito original em Photon e AAA

> Quando A1 e B1 estiverem em revisão, proponha um worktree descartável para uma comparação de VFX. Use uma labareda curta original com âncora, duração, escala, câmera e paleta iguais nas três provas. Compare renderer atual, Photon 1.1.17 com LDLib 1.0.42 e AAA Particles 2.3.4 com autoria em Effekseer, em perfis Forge 1.20.1 separados. Mantenha a cena e o renderer produtivos da Ryūjin sob responsabilidade do Codex.
>
> Entregue fontes editáveis do efeito, captura contínua por perfil e uma prova de expiração ou cancelamento. Em execução separada, sem gravador, repita três vezes e compare p95 e p99 com resolução e preset fixos. Mostre o que cada backend melhora visualmente e o custo medido. Um carregamento bem-sucedido do mod conta como prova de inicialização. A comparação exige que a labareda equivalente seja reproduzida no jogo.

## Como avaliar a evolução

Compare o primeiro relatório com o resultado de uma segunda tarefa equivalente. Dê um ponto por controle negativo real, reprodução por outra pessoa, inspeção correta dos artefatos e preservação do escopo. Registre a intervenção humana que foi necessária em cada tentativa. Mantenha os casos incompletos identificados até que tenham sua própria prova.
