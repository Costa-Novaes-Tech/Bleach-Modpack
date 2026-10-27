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

## Medição ampliada — 27/10/2026

Inventário somente leitura dos quatro worktrees existentes; bytes lógicos por arquivo, sem seguir links simbólicos. Não equivale a espaço físico alocado/compactado nem a ganho garantido de exclusão. A pasta .git real foi contada somente no checkout principal; arquivos .git dos demais apontam ao repositório compartilhado.

- Principal Bleach-Modpack: **15,934 GiB**. run-benihime-qa 9,533; .git 2,307; Docs 2,196; run 1,102; history-maps 0,477; build 0,184; run-gametest 0,065 GiB.
- Bleach-Modpack-design: **7,595 GiB**. QA de execução 4,835; Docs 2,081; history-maps 0,477; build 0,092 GiB.
- Bleach-Modpack-benihime: **3,575 GiB**. Docs 2,050; QA de execução 0,839; history-maps 0,477; build 0,092 GiB.
- Worktree audio-source-main: **2,516 GiB**. Docs 1,839; history-maps 0,477; build 0,070 GiB.
- **Total: 29,620 GiB**. Execuções QA nos checkouts: aproximadamente **15,207 GiB**. Não apagar a pasta inteira: separar capturas/logs regeneráveis, saves/configurações e originais únicos antes.
- Docs/poderes possui **173 Markdown na raiz, 1.956.309 bytes (1,866 MiB)**. A consolidação reduz repetitividade/contexto, mas o ganho principal de disco estará nas evidências.
- Maior grupo QA versionado do principal: qa/benihime, 1.369,7 MiB/5.924 arquivos. cycle141 93,8 MiB; cycle102 80,6 MiB; cycle91 60,3 MiB; cycle92 55,0 MiB. Duplicatas entre originais e pacotes só devem ser removidas com hashes e manifesto; tamanho parecido não basta.
- Git: objetos soltos 713,40 MiB; packs 1,59 GiB; seis temporários apontados por git count-objects, somando 1,80 MiB. Não executar prune/gc concorrente com outras IAs nem reescrever histórico sem coordenação. Compactação precisa ser medida, não promessa de economia.
- Disco C: aproximadamente **161,62 GiB livres** no instante da leitura.

### RAM: limites da medição

Windows: **15,69 GiB utilizáveis**, **2,55 GiB disponíveis** no segundo instantâneo. Nenhum processo Java/Gradle foi encontrado; não é medição do pico do jogo/build nem prova de consumo do mod em execução. Antigravity tem processos próprios e seu working set não representa uso exclusivo deste projeto; somar working sets também pode contar páginas compartilhadas.

Configuração local: gradle.properties define daemon -Xmx2G; build.gradle define no perfil encontrado -Xms512m/-Xmx2G. Xmx é teto de heap, não consumo total/residente, e não limita todos os processos juntos. Medir uma sessão real com cliente e, separadamente, build/QA: RAM residente/privada, pico, número de processos, CPU e frametimes; não abrir múltiplos clientes apenas para medir. Limpar documentos não melhora diretamente heap/FPS do Minecraft.

### Consolidação documental proposta

Usar dossiês canônicos por assunto, sem simplesmente concatenar 173 relatórios e sem transformar planejamento antigo em implementação vigente:

1. Entrada única: README, continuidade curta e roadmap atual. A continuidade aponta para os dossiês; não repete toda a cronologia.
2. Ryūjin/Zanka: kit por forma, transformação curta aceita, áudio/câmera, Minami, C e Higashi. Higashi terá seção própria extensa para gatilhos, vítima única, pausa de1s, coreografia, falhas e negativos.
3. Benihime: kits, modelo/rig, cortina e apoios, áudio/câmera, seguimento e combate; substituição selada ainda pendente.
4. Hyōrinmaru: transformação/hidra, mandíbulas, alas/cauda, kit por forma, impactos e clima local; separar homologação de propostas antigas.
5. Senbonzakura: transformação, kit/jardim fixo, suprema e os três golpes; registrar somente S1 entregue para teste, sem homologar o combo inteiro.
6. Fundamentos e identidades de combate: Asauchi, esquiva, stamina, defesa/postura/guard break e bainhas, respeitando contratos e aceites já existentes.
7. Contratos comuns: arma/mão/efeitos118, ciclo de vida de cinematics, servidor/rede/multiplayer, integração Better Combat/PlayerAnimator/GeckoLib/Photon e pipeline Blockbench. Dossiês referenciam estes contratos em vez de copiá-los.
8. Validação e operação: comandos reproduzíveis, critérios de aceite, matriz mínima de cenários e índice dos pacotes canônicos; evitar um novo relatório integral para cada execução.

Template por dossiê: estado e data/commit; kit/contratos vigentes; decisões aprovadas e suas fontes; alternativas rejeitadas e por quê; erros conhecidos, causa confirmada versus hipótese, correção e regressões; evidências e limites; pendências e checklist de teste. Preservar números/unidades, incompatibilidades e decisões substituídas com data.

Execução em fases: mapear documentos e referências -> integrar um dossiê piloto -> conferir cada afirmação contra fonte/aceite atual -> criar mapa antigo/novo e ajustar links -> retirar os relatórios redundantes do checkout quando a informação estiver preservada. Renomear para uma pasta de arquivo não reduz bytes. O Git publicado preserva histórico textual, mas recuperação deve ter caminho/commit explícitos. Não mover GDD da equipe, nem alterar Zaraki ou apagar fontes de assets. O levantamento desta rodada não leu integralmente todos os173documentos; a consolidação de conteúdo exige essa conferência por assunto.

Nenhum arquivo excluído e nenhum processo encerrado nesta auditoria. Dados brutos locais em build/storage-audit.json (regenerável, não versionado).
