# 150 — Prioridade de eficiência, espaço e retenção de QA

Data: 27/10/2026. Pedido do usuário: publicar os commits locais e priorizar revisão de eficiência, lixo documental e necessidade de manter tantas evidências até o fim.

## Decisão e posição no roadmap

Antecipar o item 8 como prioridade transversal, em paralelo à homologação atual e antes de ampliar a produção de NPCs/quests. Não remove os demais itens obrigatórios. Esta rodada registra o plano; não elimina arquivos, worktrees ou histórico.

## Inventário inicial medido

Somente Docs/poderes, tamanhos lógicos dos arquivos no checkout (não tamanho físico nem histórico Git):

- qa: 8.616 arquivos, 2.098,99 MiB, aproximadamente 2,05 GiB.
- media fora de qa: 677 arquivos, 106,61 MiB.
- demais arquivos: 181 arquivos, 2,40 MiB.

A prioridade de espaço são evidências volumosas e duplicadas, não apagar documentação textual indiscriminadamente. Logs rotativos rastreados também exigem revisão; não incluir suas alterações geradas pelos testes no commit documental.

## Fases e critérios de entrega

1. Inventariar checkout, Git, builds, caches, saves, logs e worktrees separadamente; classificar rastreados/ignorados, tamanho lógico e duplicatas por hash. Medir também tempo de build/QA e o gargalo de terreno do C da Ryūjin já registrado no119.
2. Consolidar documentos vigentes: índice curto de decisões, contratos, roadmap e instruções reproduzíveis. Marcar relatórios históricos como superados e localizar referências antes de remover ou arquivar. Preservar GDD da equipe, proveniência/licenças, fontes de modelos/animações, saves e configurações.
3. Aplicar retenção por feature. Enquanto houver regressão aberta, manter original relevante, log integral, manifesto, versão/commit, passos reproduzíveis e marcos suficientes para demonstrar o defeito e a correção. Não apagar agora as provas Senbon/Higashi só porque os testes automatizados passaram.
4. Depois do aceite visual, manter um pacote canônico mínimo por feature: referência antes/depois, evidência contínua quando necessária para fluidez, quadros-chave, logs e instruções de reprodução. Propor eliminação de cópias idênticas, capturas intermediárias redundantes, GIFs derivados regeneráveis, pacotes repetidos e logs transitórios. Não há necessidade de manter todas as capturas brutas para sempre, mas a seleção deve preservar a prova do comportamento aprovado.
5. Produzir manifesto de candidatos com caminho, bytes recuperáveis, motivo, dependências e destino/retensão. Executar limpeza delimitada, validar links e reprodução, comparar bytes antes/depois e verificar builds quando houver mudanças de configuração. Não apagar worktree em uso por outra IA.
6. Rever .gitignore e política para não versionar continuamente logs, caches e capturas massivas. Apagar arquivos do HEAD não remove blobs antigos do histórico: medir histórico separadamente. Reescrita de histórico/force-push exige decisão específica coordenada com a equipe; não está autorizada por este plano.

## Estado desta rodada

Sem mudança de gameplay, assets ou QA. Build anterior do149: 150 regressões e 237 GameTests aprovados; teste visual do usuário pendente. Publicação dos commits será verificada no Git após o push e comunicada no chat.
