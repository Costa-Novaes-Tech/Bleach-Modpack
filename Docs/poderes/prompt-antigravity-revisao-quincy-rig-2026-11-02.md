# Revisão corretiva Antigravity — rig Quincy Vollständig — 02/11/2026

## Objetivo

Revisar `codex/quincy-rig-manifestacao` (`8228ab1`) à luz da auditoria Codex de 02/11. Provar os assets exportados com runtime GeckoLib real, não com a aproximação desenhada pela bancada atual. Corrigir apenas lacunas demonstradas, executar QA reproduzível e, **depois do Garden estar integrado**, integrar Quincy na `Feature-Poderes-bankais`. O usuário autorizou explicitamente os merges e a resolução semântica de conflitos. **Nunca integrar na `main`.**

## Ambiente, sequência, nuvem e preservação

1. Execute esta revisão depois da revisão Garden e do merge Garden para `Feature-Poderes-bankais`. Confira o SHA atualizado da branch alvo e incorpore-o à branch Quincy, preservando os commits originais. Não iniciar merge Quincy antes do Garden.
2. Verifique se o Antigravity oferece agente/checkout em nuvem conectado ao repositório Git correto. Se houver, prefira tarefa isolada em nuvem e confirme que pode ler a branch Quincy, sincronizar a branch alvo atualizada, rodar Forge e registrar/mesclar resultados. A presença de botão ou modelo não prova que a nuvem está operacional.
3. Se nuvem não estiver disponível, relate o limite e use apenas o checkout individual Quincy depois de confirmar que está livre. Não crie worktree novo, não dispute checkout ativo e não encerre processo de outra conversa.
4. Confira status, branch, HEAD, upstream, worktrees, diff e árvore de commits. Preserve trabalho local/terceiro. Não use `git add .`, reset, clean, stash genérico, `ours/theirs` em bloco ou force-push. Se conflito semântico não puder ser resolvido sem inventar contrato, pare e relate-o.
5. As notas locais não comitadas `Docs/poderes/00-CONTINUIDADE-CODEX.md` e `Docs/poderes/continuidade-quincy-senbon-xerj-2026-10-31.md` pertencem ao checkout Codex e talvez não existam no clone em nuvem. Não tente recriá-las, substituí-las ou apagá-las; mantenha sua documentação da revisão em arquivos próprios de `Docs/poderes`.

## Lacunas que motivaram a revisão

- O commit contém `.bbmodel`, `geo.json`, `animation.json`, textura e `QuincyManifestationQa`. A classe atual desenha asas/estrelas e calcula movimentos com `renderCube`, `applyWingAnimation` e `applySatelliteAnimation` próprios; não lê nem reproduz o modelo/clipes exportados. Os seis PASS e a prancha demonstram apresentação de bancada Forge, não execução dos assets GeckoLib.
- O manifesto lista hashes de PNGs brutos sob `run-benihime-qa` de outro checkout, mas esses PNGs não estão no pacote versionado disponível ao auditor. A prancha JPG é visível, porém tem HUD/overlay e marcos estáticos; não comprova continuidade dos clipes.
- `pipeline.py --check` confere sincronização de arquivos, não substitui parsing e reprodução no runtime GeckoLib.

## Escopo permitido e preservado

Pode ajustar modelo/rig, pipeline, exports próprios `quincy_manifestation` e uma bancada isolada que carregue exatamente esses exports via GeckoLib. Não tocar gameplay, serviços Quincy, rede, `BleachClient`, `QuincyVollstandigVisuals` compartilhado, HUD, comandos, bow, flechas ou danos. Não editar Garden nem decisões Quincy do GDD. Se um controlador de produção compartilhado for necessário, documentar a interface mínima e deixar a integração ao Codex.

Preservar 11 bones, segmentos independentes, quatro estrelas, seis nomes de clipes, unidades/pivôs documentados, frente `-Z` e responsabilidade do jogador pelo arco/mãos. Manifestação é visual: sem hitboxes, voo, item/armadura duplicados ou quinta estrela. H/N/B/C em produção continuam responsabilidade Codex; não inventar API produtiva.

## Trabalho solicitado

1. Criar prova isolada que carregue `geo.json`, textura e clipes GeckoLib efetivamente exportados, usando renderer/controller compatível com as dependências já configuradas. Não desenhar geometria equivalente manualmente como substituto. Se faltar contrato de produção, capturar o erro real e descrever a interface mínima sem tocar módulos compartilhados.
2. Executar sequência contínua de `emergence`, `idle`, `arsenal_left`, `arsenal_right`, `recoil` e `settle`; mostrar frente, perfil e costas nos marcos pertinentes. Confirmar movimento segundo os JSONs, ambas origens no mesmo setor frontal e arco/silhueta legíveis. Capturar vídeo contínuo e imagens nos marcos; imagens isoladas não provam interpolação.
3. Registrar duração/ticks, pivôs, câmera, configuração gráfica, versões Forge/GeckoLib, comando, branch/HEAD, exit code e logs brutos. Inspecionar imagens e incluir SHA-256 e relação com captura original. Não afirmar teste dedicado/multiplayer sem executá-lo.
4. Revalidar `pipeline.py --check` e rodar `check`, GameTests e cliente Forge em sequência, registrando cada resultado. Diferenciar compilação, carregamento GeckoLib e QA visual contínuo.
5. Atualizar manifesto para apontar captura contínua e arquivos brutos acessíveis no pacote auditável. Manter entrega como **PARCIAL** até haver integração de produção pelo Codex.

## Integração autorizada

Depois de Garden integrado e da prova dos exports no runtime real, integrar a branch Quincy atualizada na `Feature-Poderes-bankais`, sem `main`. Preferir nuvem se houver checkout isolado conectado e integração segura; caso contrário, usar checkout dedicado sem deslocar branch ativa em outro worktree. Resolver conflitos semanticamente e preservar Garden e Quincy. Conferir `git diff --check`, ausência de unmerged paths, ancestralidade e histórico. Não force-push.

Se a branch alvo estiver ativa em outro worktree local e não houver integração segura em nuvem, não tente trocar essa branch a partir do checkout Quincy. Entregue commits/evidências e informe que o merge ficou impedido pela concorrência do checkout.

## Relatório de entrega

Informe disponibilidade e uso real de nuvem; ordem de integração Garden→Quincy; checkout/branch/base/commits; conflitos e resolução; arquivos; resultados/exit codes; vídeo/imagens/manifests e hashes; estado final da `Feature-Poderes-bankais`; lacunas e pendências. Não aprovar assets a partir de uma bancada que os desenha à parte.
