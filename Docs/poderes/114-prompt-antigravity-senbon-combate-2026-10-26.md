# Prompt — Antigravity 1: Senbonzakura, arma e combate

## Atribuição atualizada pelo coordenador

Trabalhe na branch existente `codex/design-poderes-criaturas`, no checkout `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`. A sincronização desta entrega leva a base de poderes e estes prompts para essa branch. Confira HEAD e status antes de começar. A sugestão anterior de criar outra branch fica substituída por esta atribuição. Não alterne para a branch da outra conversa. O Codex mantém a infraestrutura comum; solicite o contrato de âncoras antes de depender de mudanças ainda não integradas. Pode iniciar diagnóstico e autoria exclusiva do primeiro golpe em paralelo.

Você realizará uma tarefa delimitada no Bleach Mod Forge1.20.1. Outros agentes trabalham em paralelo: não reverta alterações deles. Sua responsabilidade é o combate básico customizado do Senbonzakura e sua ligação visual arma/punho/rastro. Não altere Zaraki.

## Preparação e fontes obrigatórias

Raiz de referência: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack`. Leia AGENTS.md, skills pertinentes, `Docs/poderes/00-CONTINUIDADE-CODEX.md`, documentos111,112 e `113-auditoria-video-ryujin-combate-tres-frentes-2026-10-26.md`. Leia o documento109 específico de Senbon, não apenas o109 de Ryūjin. Evidências acessíveis sem receber vídeo: `Docs/poderes/qa/cycle113/senbon_melee-frames-1.jpg` e `senbon_melee-frames-2.jpg`, manifest e overview. Não afirme ter visto o vídeo inteiro usando somente essas imagens.

Confira git status, branch, HEAD, upstream e worktrees antes de editar. Base técnica auditada23b80e0; documentos113–115 podem ainda estar locais. Não faça reset/stash/checkout sobre alterações. Utilize apenas o checkout isolado combinado com o coordenador; sugestão de branch `codex/senbon-combate-vinculo-arma`. Não trabalhe na main ou no checkout ocupado pelo Codex. Se ainda não houver checkout próprio, solicite sua atribuição em vez de trocar a branch do diretório compartilhado. Commit/push/merge dependem da autorização vigente da sua conversa.

## Diagnóstico que deve orientar o trabalho

No vídeo43–45s a lâmina se move, mas trajetória e emissão de pétalas não têm leitura coesa. Não prometa corrigir “arma totalmente imóvel” como fato universal. Ausência de canais rightItem/leftItem nos assets customizados é diferença confirmada; herança do braço significa que não é causa única demonstrada. Compare com o nativo Better Combat efetivamente instalado e Asauchi/Ryūjin/Hyorin preservadas. Reproduza com identidade Senbon e arma Senbon corretas, sem confundir HUD de uma técnica com item de outra.

## Propriedade de arquivos e fronteiras

Você pode alterar `src/main/resources/assets/bleachmod/attack_animations/senbonzakura_*.json`, correspondentes `player_animation/senbonzakura_*.json`, perfil `src/main/resources/data/bleachmod/weapon_attributes/senbonzakura.json` e recursos exclusivos Senbon necessários à prova. Inspecione os caminhos reais antes de escrever. Relatório e evidências próprios em `Docs/poderes/qa/antigravity-senbon-combate/` e documento de entrega exclusivo.

BetterCombatClientCompat, BetterCombatCompat, SignatureActionVisuals, ElementalActionVisuals, mixins, controladores compartilhados, registry, rede, build/dependências e documentos globais pertencem ao Codex. Se a causa estiver ali, entregue reprodução/contrato e aguarde integração da correção comum; não duplique compensação local ou remova controladores globais. Não edite Benihime/Ryūjin/Hyorin/Zaraki. Não atualize continuidade/roadmap simultaneamente ao coordenador.

## Execução e identidade

Primeiro prove um único corte completo: preparação → passagem da lâmina → contato → recuperação. Punho e guarda devem manter contato; lâmina precisa varrer arco legível, sem escorregar pela mão, travar vertical ou retornar com salto. Depois complete os três golpes com corte elegante diagonal, retorno e estocada, mantendo o perfil aprovado. Examine pivô/display, item rotation/position, bend e espelhamento; não copie keyframes nativos sem entender sua composição. Sincronize duplicatas de assets quando o carregamento exigir, comprovando qual recurso está sendo usado.

Rastro nasce no ponto correto durante a janela ativa e partículas emitidas podem permanecer no mundo naturalmente. Não manter emissor deslocado fixo quando deveria seguir a lâmina. Não faça Jardim ou outras áreas fixas passarem a seguir a mão. Hakuteiken: preserve dash, alcance/hitbox, custos e dano; refine somente leitura de empunhadura/entrada/recuperação e excesso visual comprovado. A cinematográfica/palco já aprovados ficam intactos.

## Validação e entrega

Use ferramentas existentes: JSON/Better Combat/PlayerAnimator e cliente Forge real; Blockbench somente se necessário. Não instalar novas bibliotecas por conveniência. Demonstre em primeira e terceira pessoa, destro/canhoto, selada/Shikai/Bankai, mão secundária livre/ocupada e transições combo→defesa→técnica. Grave antes/depois do mesmo ângulo com arma inteira, sem partículas escondendo a empunhadura; registre marcos e recurso carregado. Teste comportamento sem alvo e com alvo. Preserve stamina, custo, dano, cooldown, aliados e autorização do servidor.

Execute checks apropriados já existentes, sem desativar testes para obter verde. Separar resultado técnico, observação no cliente, observador multiplayer e aprovação artística do usuário. Sem acesso ao jogo, declarar validação visual pendente. Entregue causa comprovada, diff por arquivo, evidências reais, comandos/exit codes, commit se autorizado e limitações. Não chamar build verde de homologação. Só expandir os três golpes depois que o primeiro comprovar vínculo arma/punho.
