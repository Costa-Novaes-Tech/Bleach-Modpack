# Coordenação das frentes — 29/10

Autorização humana: três frentes Antigravity (proteção de ativação Bankai, projéteis Quincy, Garden N); Codex troca combo Senbon por animações prontas. Frentes de NPCs/lore preservadas para depois. Documentos de29/10 que propunham dano normal durante a cena ficam superados: usuário aprovou invencibilidade temporária.

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
