# Prompt — Antigravity 2: Benihime, identidade de combate

**Contrato de integração após a retomada Codex:** leia também `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/Docs/poderes/118-contrato-comum-arma-maos-efeitos-2026-10-26.md`. Os canais de item são locais, POSITION passa por escala1/16 no renderer e o bend/display participam do resultado. Não adicionar um segundo controlador de melee nem transportar as âncoras aproximadas da Ryūjin para Benihime sem prova. O118 pode estar local no checkout principal até o próximo commit; consulte esse arquivo existente antes de redefinir a interface comum.

## Atribuição atualizada pelo coordenador

A segunda branch existente do Antigravity é `codex/ryujin-bankai-sol-selamento`; o nome é histórico, e nesta divisão sua responsabilidade passa a ser Benihime. Ela recebe a mesma base atualizada de poderes e estes prompts. Não retome mudanças de Ryūjin: essa frente pertence ao Codex. Esta atribuição substitui a sugestão anterior de criar outra branch. Confirme um checkout isolado disponível antes de editar; atualmente o checkout principal pertence ao Codex e o checkout `Bleach-Modpack-design` pertence à frente Senbon. Se não houver um terceiro checkout atribuído, alinhe seu uso antes de executar: não troque branches em diretório ocupado e não trabalhe simultaneamente no checkout Senbon. Pode ler e planejar enquanto essa atribuição física é resolvida.

Sua tarefa é melhorar o combate básico da Benihime com ligação limpa entre mãos, espada e efeitos. Outros agentes trabalham em paralelo: preserve suas mudanças e não reverta arquivos alheios. Zaraki e entrada cinematográfica da Benihime estão fora do escopo.

## Fontes e checkout

Raiz de referência: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack`. Leia AGENTS.md, skills pertinentes, `Docs/poderes/00-CONTINUIDADE-CODEX.md`,111,112 e `113-auditoria-video-ryujin-combate-tres-frentes-2026-10-26.md`. Consulte o contrato vigente do kit Benihime antes de editar. Referências visuais extraídas: `Docs/poderes/qa/cycle113/benihime_compare-frames-1.jpg`, `benihime_compare-frames-2.jpg` e overview. São trechos comparativos com troca de item: reproduza identidade e arma Benihime juntas antes de atribuir falha. Não afirmar ausência universal de movimento da espada: o vídeo mostra movimentos, mas a qualidade e sincronização precisam melhorar.

Antes de editar, registre git status, branch, HEAD, upstream e worktrees. Base auditada23b80e0; documentos novos podem ainda estar locais. Sugestão de branch própria `codex/benihime-combate-vinculo-arma`, somente em checkout isolado atribuído pelo coordenador. Branch diferente no mesmo diretório não permite trabalho simultâneo seguro. Se faltou checkout, solicite atribuição em vez de alternar o diretório ocupado. Sem reset, stash, edição na main ou descarte de mudanças. Commit/push/merge somente com autorização vigente nessa conversa.

## Propriedade e diagnóstico

Seus arquivos: `src/main/resources/assets/bleachmod/attack_animations/benihime_*.json`, equivalentes `player_animation/benihime_*.json`, `src/main/resources/data/bleachmod/weapon_attributes/benihime.json` e recursos exclusivos do combate Benihime necessários. Confirme os nomes reais. `BenihimeChoreography.java` só pode ser alterado se comprovadamente necessário a um golpe deste escopo, sem modificar entrada/locomoção/pose da manifestação aprovada. Relatório e capturas próprios em `Docs/poderes/qa/antigravity-benihime-combate/`.

Controladores/renderers comuns, BetterCombatClientCompat/BetterCombatCompat, SignatureActionVisuals, ElementalActionVisuals, mixins, rede, build/registry e documentos globais pertencem ao Codex. Não os edite em paralelo. Se o defeito depender da composição comum, entregue reprodução e contrato ao coordenador; não crie segunda correção concorrente. Não altere assets Senbon nem Ryūjin/Hyorin/Zaraki.

Os assets customizados não têm rightItem/leftItem; os nativos de referência têm. Isso não basta para concluir causa: o item pode herdar a transformação do braço. Verifique renderer de primeira pessoa, pivô/display, rotations, bend, handedness e qual camada escreve por último. Referências funcionais: Asauchi, Ryūjin e Hyorin, sem substituir seus movimentos aprovados. Não resolver somente com um offset global que conserta um golpe e quebra os demais.

## Direção do combate

Criar leitura de esgrima técnica e econômica própria da Benihime: diagonal controlada, retorno distinto e estocada precisa; manter identidade/modelo singular da arma, sem transformá-la em katana genérica. Comece por UM golpe completo com preparação, passagem, contato e recuperação. Mostrar guarda aderida ao punho, mão secundária coerente e movimento real da lâmina; depois completar os três golpes. Canais de item e braço devem trabalhar juntos, sem espada parada enquanto o braço gira ou lâmina atravessando a palma. Transições para defesa, técnicas e transformação precisam limpar seus offsets.

Efeitos presos à espada devem seguir sua âncora durante a janela ativa. Projéteis saem do ponto e instante corretos; efeitos já emitidos podem permanecer no mundo. Rede Shibari permanece fixa conforme contrato. Não reautorizar o kit selado, boneca substituta, Hiasobi, dano supremo ou a entrada da rainha nesta tarefa: essas decisões têm escopo próprio. Não modificar custo/stamina/dano/alcance/cooldown/aliados nem regras de cancelamento.

## Ferramentas, testes e entrega

Priorize Better Combat/PlayerAnimator e recursos do projeto; não instalar dependências novas. Blockbench se houver necessidade real de autoria de rig/item; não basta uma prévia de editor. Prove no Minecraft Forge real com arma inteira enquadrada, primeiro sem partículas e depois com efeitos, antes/depois no mesmo ângulo. Cobrir primeira/terceira pessoa, destro/canhoto, selada/Shikai/Bankai, mão secundária livre/ocupada, sem/com alvo e combo→defesa→técnica→retorno.

Execute checks existentes pertinentes, registre comandos/exit codes e não desative testes. Entregue causa comprovada, arquivos alterados, recurso efetivamente carregado, quadros/vídeo de marcos, status de observador multiplayer separado e limitações. Não declarar aprovado visualmente por compilar; usuário faz aceite artístico. Se não conseguir cliente real, marque pendente e entregue a reprodução, sem capturas simuladas. Expanda os demais golpes somente depois da prova vertical completa.
