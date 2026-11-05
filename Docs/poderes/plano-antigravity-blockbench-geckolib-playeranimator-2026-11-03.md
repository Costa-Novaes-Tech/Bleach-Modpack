# Plano Antigravity — Blockbench, GeckoLib e PlayerAnimator (03/11/2026)

Branch de orquestração: `codex/conferencia-orquestracao`. **Somente documentação e preparação.** Nenhum modelo, código, branch nova ou teste de jogo foi criado/executado nesta rodada. Fonte do pedido: mensagem humana de 03/11; decisões abaixo marcadas "proposta" não estão aprovadas.

## 1. Pedido humano (preservado)

1. Kit Quincy Ishida precisa de validação e teste.
2. Garden (Senbonzakura N) precisa de teste.
3. Humano e Codex estão mexendo no Blockbench com a Ryūjin: o Antigravity **não** toca nessa frente.
4. Preparar o Antigravity para, usando Blockbench: **refatorar/refazer a habilidade C da Senbonzakura**; **melhorar o design das asas Quincy e dar-lhes animação/movimento**; e **testar GeckoLib + PlayerAnimator para modelar a movimentação do jogador e das asas**.

## 2. Estado real verificado (03/11)

| Item | Fato | Evidência |
|---|---|---|
| Ponte Blockbench | **Indisponível agora**: `get_status` e `list_plugins` falharam com "Cannot reach Blockbench on http://127.0.0.1:8787". Editor fechado ou servidor MCP não iniciado. Não é falha de ferramenta do agente; exige abrir Blockbench com o plugin ativo. | chamada MCP em 17:40 |
| Editor compartilhado | Humano/Codex usam Blockbench na Ryūjin. Regra GEMINI.md: confirmar projeto ativo, não alterar modelos de outra autoria, trabalhar em nova aba/cópia `ag_`, `execute_script` desligado. | GEMINI.md |
| GeckoLib | Dependência de runtime pinada (`maven.modrinth:geckolib:aC5KMoNg`; commit do rig cita 4.8.4). Editor Blockbench: plugin GeckoLib **não estava instalado** em 22/10; reconfirmar. | `build.gradle`, `bleach-design-lab` |
| PlayerAnimator + Better Combat | Dependências pinadas. Já existem `player_animation/*.json` (Benihime, Nozarashi, Senbonzakura ×3, `berserk_release`), `SenbonzakuraPlayerPose`, `HigashiBetterCombatMotion`, `RyujinBankaiEntranceClient`. | `build.gradle`, `src/main/resources/.../player_animation` |
| C da Senbonzakura | É **Shukei: Hakuteiken** (slot 4/C Bankai): windup 16 ticks, vetor travado, investida até 16 blocos, impacto e dissipação. Asas, halo e lâmina são **geometria procedural em Java** (`SenbonConstructGeometry.WINGS`), coreografia em `SignatureChoreography`, serviço em `SenbonzakuraTechniqueService`. Não é modelo Blockbench. | doc 109; `git grep` |
| Asas Quincy | Existe `quincy_manifestation` (11 bones, 34 cubos, 4 estrelas, 6 clipes: `emergence`, `idle`, `arsenal_left`, `arsenal_right`, `recoil`, `settle`) com 6 PASS na bancada GeckoLib. **Não está em produção**; a produção ainda desenha em `QuincyVollstandigVisuals`. | `results.txt`; `git grep` |
| Combo Senbon | Aceito pelo usuário (Better Combat, 3 golpes). Não restaurar clips antigos ao refatorar C. | `antigravity-frentes-2026-10-29.md` |

## 3. Frentes propostas (organização por feature)

| Frente | Dono | O que | Estado |
|---|---|---|---|
| F1 — Validação Quincy Ishida/Vollständig | Humano + Codex | Playtest do kit avançado; AG pode auditar evidências e propor roteiro de teste. | Aguardando teste humano |
| F2 — Teste do Garden N | Humano (+ AG audita frames) | Playtest; auditoria de `e1015da` ainda aberta. | Aguardando |
| F3 — Ryūjin no Blockbench | Humano + Codex | Em andamento fora do AG. | **Não tocar** |
| **AG-S — Hakuteiken (C Senbon) em Blockbench** | AG (design/assets) + Codex (runtime) | Refatorar/refazer o visual do C: asas, halo, lâmina, rastro e poses em assets editáveis; manter a mecânica (alcance, tempos, dano) salvo decisão. | A iniciar após aprovação |
| **AG-Q — Asas Quincy: design e animação** | AG (assets) + Codex (produção) | Melhorar o design de `quincy_manifestation` e seus clipes; provar no runtime GeckoLib; ligação à produção fica com Codex. | A iniciar após aprovação |
| **AG-P — Movimento do jogador (PlayerAnimator)** | AG | Poses/curvas de corpo sincronizadas com as asas/C; validar em cliente Forge real, primeira e terceira pessoa. | A iniciar após AG-S/AG-Q |

Rigor: AG-S e AG-Q podem rodar em paralelo apenas em **checkouts/branches distintos**; um cliente pesado por vez; nunca duas IAs no mesmo checkout.

## 4. Pipeline de cada frente de design

1. **Pré-voo (Git/ambiente):** `git status --short --branch`, `git worktree list`, ref atualizada (`git fetch origin`), conferir que a branch alvo contém `origin/Feature-Poderes-bankais`; confirmar Blockbench aberto + MCP `get_status`; confirmar quem usa o editor (Ryūjin).
2. **Baseline:** bones/pivôs/UV/escala atuais; para Hakuteiken, ler `SenbonConstructGeometry`, `SignatureChoreography`, `SenbonzakuraTechniqueService` (trecho Hakuteiken) e os GameTests existentes (`hakuteiken*` em `SenbonGameTests`) **antes** de propor assets; registrar o que é procedural e o que o servidor decide.
3. **Autoria no Blockbench:** aba/cópia `ag_` em `tools/models/lab/antigravity/`; nome de bone é contrato; rig com pivôs reais (escápula, juntas de pena); keyframes suaves; mais de uma vista (frente, perfil, costas); `check_model`, `check_rig`, `measure_model`; sem `execute_script`.
4. **Exportação GeckoLib:** só afirmar exportação se houver codec validado no editor; o runtime já carrega `geo.json`/`animation.json` (precedente `quincy_manifestation_pipeline.py`). Não renomear JSON genérico como `geo.json`.
5. **PlayerAnimator:** curvas Bedrock em `player_animation/*.json`, sem substituir o combo aceito; auditar sobreposição de controladores procedurais antes de culpar o clip.
6. **Prova real:** `check`, GameTests, e cliente Forge (um por vez) com sequência contínua, frente/perfil/costas, com e sem VFX; compilar não é validar aparência; preview do editor não é jogo.
7. **Entrega:** commit focado na branch da frente, relatório curto em `Docs/poderes`, `manifest` com SHA-256, limites declarados; sem merge de saída.

## 5. Critérios de aceite (propostos)

- **AG-S:** assets editáveis em Blockbench; mecânica preservada (testes `hakuteiken*` verdes sem relaxar asserts); cliente Forge mostra preparação, forma completa, investida, impacto e dissipação sem clarão cego e sem penetração arma/mão; aceite humano separado.
- **AG-Q:** clipes em runtime GeckoLib real, sem hitbox/voo/quinta estrela, asas não seguram o arco; ligação à produção não é feita pelo AG; aceite humano e playtest.
- **AG-P:** poses medidas por marcos (guarda, preparação, contato, passagem, recuperação, devolução de controle) comparadas no mesmo tempo efetivo; primeira pessoa e terceira testadas; combo aceito intacto.

## 6. Decisões humanas que destravam (perguntas abertas)

1. **Hakuteiken:** manter exatamente a mecânica atual (16 blocos, windup 16, dano) e refazer só o visual, ou mudar a habilidade? (proposta: só visual primeiro)
2. **Direção visual:** asas Senbon e Quincy devem ter identidades distintas? Alguma referência/arte aprovada além dos storyboards de 30/10?
3. **Produção:** AG-S e AG-Q entregam só assets + prova de bancada (proposta), ou também integração de renderer, que hoje é compartilhada com o Codex?
4. **Checkouts:** qual checkout/branch individual cada frente usa? Sugestão: `codex/senbon-hakuteiken-blockbench` e continuar `codex/quincy-rig-manifestacao`, ambos a partir da Feature atualizada. Nenhuma branch foi criada.
5. **Blockbench:** quando abrir o editor com o plugin ativo e reservar uma aba `ag_` sem conflito com a Ryūjin?

## 7. Limites e riscos

- Ponte indisponível hoje: sem reabrir o editor, nada é autorado; não substituir por HTTP ad hoc.
- Asas Senbon procedurais podem dominar rotações de clipes; investigar controladores antes de aumentar amplitudes.
- Quincy: contratos preservados — arco/mãos do jogador, asas/estrelas só visuais, H/N/B/C do Codex.
- Aceites existentes (Senbon combo, Ryūjin, Benihime) não são reabertos.
- Preview e telemetria heurística (`analyze_animation`) não provam movimento em jogo.
