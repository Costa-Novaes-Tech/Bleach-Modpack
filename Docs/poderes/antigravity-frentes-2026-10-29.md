# Coordenação das frentes — 29/10

Autorização humana: três frentes Antigravity (proteção de ativação Bankai, projéteis Quincy, Garden N); Codex troca combo Senbon por animações prontas. Frentes de NPCs/lore preservadas para depois. Documentos de29/10 que propunham dano normal durante a cena ficam superados: usuário aprovou invencibilidade temporária.

## Organização vigente30/10 — três frentes, substitui a fila anterior

- **Codex / Feature-Poderes-bankais:** controles e estados Quincy, conciliação M07, preparação/reação, ações H/N/B/C, snapshots e integração final dos assets. Autoridade exclusiva sobre common/quincy, pacotes/SyncHelper, ClientForgeEvents, QuincyVollstandigVisuals, HUD/commands e conexões compartilhadas. Nesta rodada só organização/auditoria; transformação intermediária Ginrei e parâmetros/destruição C continuam propostas para discutir, não implementação aprovada por esta divisão.
- **Antigravity A / codex/quincy-rig-manifestacao:** fontes/modelo/rig/texturas/animações de asas/quatro estrelas e prova em bancada Forge. [Prompt específico](prompt-antigravity-quincy-rig-2026-10-30.md). Não tocar gameplay/rede/arquivos cliente compartilhados. Usar checkout individual existente liberado; o checkout benihime é candidato, não disponibilidade presumida.
- **Antigravity B / codex/senbon-garden-prisao:** corrigir a entrega7750815 e produzir QA real. [Retorno corretivo no prompt Garden](prompt-antigravity-garden-prisao-2026-10-29.md). Checkout design já está nessa branch; sem alternar para Quincy em outra conversa simultânea. Não tocar combo aceito, Quincy ou docs compartilhados.

Antes de qualquer implementação, cada AG lê tudo, atualiza/compara refs e integra poderes NA SUA branch conforme regra abaixo. Mesmo Garden precisa trazer a documentação posterior a2eb3898. Nenhuma frente faz merge de retorno automaticamente. Sem novo diretório/worktree, sem duas IAs no mesmo checkout e sem dois clientes pesados simultâneos.

Auditoria7750815: seis GameTests novos e283PASS confirmados no log da branch, mas escopo dos testes é menor que o relatório. Falta revalidar aliados durante prisão, assegurar limite de altura durante lease inteira, renovar prazo do piso sem resetar e provar destinatários/deltas por fase. Testes intitulados aliados não criam aliado; teste tardio pode passar antes do terceiro pulso. Imagem/clip Forge Garden ausentes do pacote apresentado. Não integrado/homologado nesta revisão.

## Regra obrigatória de início, comum aos três prompts

Depois de ler integralmente seu prompt e o contrato Docs/poderes/skills/bleach-antigravity-entrega/SKILL.md, antes de implementar:

1. Confirmar caminho do checkout individual existente, branch, HEAD, upstream, git status, diff e worktrees. Não operar no checkout Codex. Não alternar branch usada por outra conversa.
2. Atualizar refs com git fetch origin. Comparar branch individual com sua origin correspondente, origin/Feature-Poderes-bankais e origin/main. Informar ahead/behind e arquivos divergentes. Comparação com main é informativa: a integração exigida é poderes→branch individual, não main→branch individual.
3. Preservar trabalho local: inspecionar e separar mudanças próprias das alheias. Commitar somente entrega própria revisada quando necessário à integração. Não fazer git add ., reset --hard, clean, checkout de arquivo alheio, force-push ou stash genérico. Se autoria/estado impedir merge seguro, explicar o impedimento específico e não sobrescrever.
4. Na branch individual, integrar primeiro eventual avanço de sua própria origin sem descartar commits; depois executar git merge --no-ff origin/Feature-Poderes-bankais. Se já ancestral, registrar que está atualizada. Não dar merge reverso na poderes.
5. Resolver conflitos semanticamente, preservando as duas entregas pertinentes. Nunca escolher ours/theirs em bloco. Se conflito envolver decisão de outra frente, consultar responsável em vez de inventar contrato. Confirmar ausência de unmerged paths e git diff --check.
6. Registrar SHAs pré/pós-merge e verificar que origin/Feature-Poderes-bankais é ancestral do HEAD com git merge-base --is-ancestor. Revisar o diff resultante e rodar baseline adequado antes de iniciar a implementação; falha preexistente deve ser diferenciada da nova.

Comandos de leitura, substituindo o nome real da branch em vez de copiar placeholder:

```powershell
git status --short --branch
git branch --show-current
git worktree list
git fetch origin
git log -5 --oneline
git rev-list --left-right --count HEAD...origin/Feature-Poderes-bankais
git diff --stat HEAD...origin/Feature-Poderes-bankais
git rev-list --left-right --count HEAD...origin/main
git ls-files -u
```

Usar branch exclusiva por frente, não reutilizar a mesma branch em duas conversas. Para nova branch, usar codex/ryujin-protecao-ativacao, codex/quincy-projeteis-reishi ou codex/senbon-garden-prisao, conforme frente, da poderes atualizada; somente no checkout individual disponível. Não criar outro diretório/worktree. Se os checkouts disponíveis estiverem ocupados, executar sequencialmente; não dividir um diretório ativo entre duas IAs. Não publicar nem mesclar na poderes/main automaticamente ao concluir; entregar commit e evidência para auditoria. Preservar autorizações humanas posteriores.

## Responsabilidade exclusiva

- AG proteção: ciclo da transformação e proteção server-side; clientes Ryūjin, testes correspondentes. Não editar combo/modelos, projéteis ou Garden. Rede compartilhada somente se indispensável, declarar protocolo e coordenar antes.
- AG Quincy: QuincyArrowEntity/Renderer, recursos próprios do projétil, QuincyBowItem e testes pertinentes. Não reescrever comandos, HUD, progressão ou modelo já calibrado do arco.
- AG Garden: SenbonzakuraTechniqueService/Policy no trecho Garden, VFX/partículas do Garden, HUD apenas se novo estado exigir. Não editar weapon_attributes, clips, gerador de combo ou BetterCombatCompat.
- Codex: weapon_attributes/senbonzakura.json, QA do combo e documentação de aceite; não tocar runtime das três frentes.

Cada frente atualiza seu próprio prompt com resultado curto ou um único documento do assunto. Não disputar 00-CONTINUIDADE-CODEX.md nem roadmap126 em paralelo; Codex consolida. Nenhuma frente deve restaurar clips personalizados como combo ativo para resolver um conflito futuro.

## Portões de entrega

Baseline após integração; alteração pequena; teste pertinente; Forge real; revisão das imagens/resultado; só então commit focado. Rodar um cliente pesado por vez. Evidência declara HEAD/diff, comando/exit code, cenário, tempos e origem dos arquivos. Não usar teste que injeta o resultado como prova do fluxo normal. Compilação não é homologação visual. Fazer uma primeira entrega funcional antes do polimento, sem afirmar conclusão integral se falta arte/multiplayer.

Prompts: [proteção](prompt-antigravity-bankai-protecao-2026-10-29.md), [Quincy](prompt-antigravity-quincy-projeteis-2026-10-29.md), [Garden](prompt-antigravity-garden-prisao-2026-10-29.md). Arquivos de apoio não significam configuração automática do aplicativo: leitura explícita exigida em cada conversa.


## Integração solicitada pelo usuário —29/10

Autorização explícita para mesclar as duas entregas na nossa branch e resolver conflitos. Proteção a3b4357 integrada em f392f79; projéteis Quincy ed722ae integrados em cc5ec61. Os dois merges foram feitos com --no-ff na Feature-Poderes-bankais, sem conflitos manuais/unmerged paths. Main e checkouts individuais não foram alterados. Combo pronto Senbon, adaptação Higashi e comandos próprios preservados por diff com04705fe.

Check/build integrados exit0,158regressões; JAR build/libs/bleachmod-0.2.0.jar gerado. GameTests integrados: primeiro ciclo terminou com1falha em dissectionDealsDelayedDamageAndExposureExpires (Benihime), log build/integracao-frentes-20261029-gametests.log; repetição sem mudança de runtime/testes reproduziu a falha no log build/integracao-frentes-20261029-gametests-recheck.log. Diagnóstico posterior e correção do fixture descritos abaixo. Não houve novo ciclo cliente nesta integração; usuário fará playtest. Resultados das branches isoladas não substituem o teste do conjunto.

Limites preservados no aceite: proteção ainda precisa conciliar saída voluntária de câmera e fim no servidor, e seu evento knockback não prova bloqueio de toda colisão entre entidades. QA Quincy de impacto ainda não demonstra acertar o bloco (obstáculo à altura dos pés, flecha à altura dos olhos; PASS não exige colisão confirmada). Essas pendências não foram ocultadas nem corrigidas incidentalmente para fazer merge. Esta integração é entrega para teste, não homologação final.

Garden permanece especificação sem implementação/commit de gameplay. Não há merge dessa frente. Ao liberar checkout individual, sincronizar com a poderes que agora contém as duas integrações e preservar ajustes compartilhados. Os bloqueios de QA e futuras correções devem ser auditados antes de declarar tudo concluído.

Diagnóstico da falha de teste: alvo vivo/rastreado em(457.5,286,18.5), conjurador em(457.5,286,2.5), HP1000/bonus0 e hasLineOfSight=false. O fixture de céu fica fora do pequeno template e não assegurava corredor aberto. Correção restrita a BenihimeGameTests: limpar volume estreito do corredor no mundo descartável, exigir linha de visão e sucesso do cast antes de verificar dano/exposição; asserções de dano atrasado/expiração mantidas e diagnóstico ampliado. Runtime Benihime não alterado. Última suíte em build/integracao-frentes-20261029-gametests-final.log.

**Resultado integrado final:** todos270GameTests requeridos aprovados, exit0, em build/integracao-frentes-20261029-gametests-final.log. As tentativas anteriores falharam e foram preservadas; a aprovação final só ocorreu após corrigir a preparação do cenário, sem enfraquecer asserções nem mudar gameplay Benihime. [Registro compacto](qa/integracao-frentes-20261029.json).
