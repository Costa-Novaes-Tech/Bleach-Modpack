# Inventário de pendências e ordem de execução — 03/11/2026

Branch de conferência/orquestração: `codex/conferencia-orquestracao` (origem `Feature-Poderes-bankais` @ `18909a7`, mais merge `origin/main` @ `9906486` em `edd6605`). Documento de organização: nenhuma implementação, merge de saída, push ou aceite humano é declarado aqui. Antes de agir, reconferir Git: refs mudam.

## 1. Estado verificado nesta data

| Fonte | Fato verificado (comando) |
|---|---|
| Garden `codex/senbon-garden-prisao` @ `e1015da` | Ancestral de `Feature-Poderes-bankais` (merge `e051e6a`) e desta branch (`git merge-base --is-ancestor`, exit 0). |
| Quincy rig `codex/quincy-rig-manifestacao` @ `7664737` | Ancestral de Feature (merge `18909a7`) e desta branch (exit 0). |
| `origin/main` @ `9906486` | Mesclado nesta branch; conflitos só em README de poderes e `logs/debug-*.log.gz` (mantido o lado local). |
| Testes após o merge com a main | `./gradlew check build` exit 0 (158 regressões); `runGameTestServer` exit 0, 285 requeridos aprovados (`build/merge-main-gametests.log`). Sem cliente gráfico. |
| Checkout Codex `Bleach-Modpack` | Tem notas **não commitadas**: `00-CONTINUIDADE-CODEX.md`, `continuidade-quincy-senbon-xerj-2026-10-31.md`, `storyboards-vollstandig-2026-11-02.md`, `prompt-antigravity-revisao-*-2026-11-02.md`, `ryujin-experimento-explosao-sol-modelado-2026-11-02.md`, `plano-uryu-arqueiro-tradicional.md` modificado. Lidos só para leitura; não estão nesta branch e não foram tocados. |

Limites: relatório do Garden (`revisao-corretiva-garden-senbon-2026-11-02.md`) é autodeclarado (284 GameTests, 12 frames Forge); não reauditei os frames nem os asserts. Rig Quincy: `results.txt` lista 6 PASS com `runtime=geckolib`, mas `quincy_manifestation` só é referenciado por `QuincyManifestationQa` e um GameTest — **não há uso em produção** (`git grep`).

## 2. Inventário por frente

Legenda de dono: **Codex** = gameplay/rede/HUD/comandos/integração e consolidação de continuidade; **AG** = design/modelo/rig/QA em checkout individual; **Humano** = playtest/aceite/decisão.

| # | Frente | O que falta | Dono | Critério de aceite |
|---|---|---|---|---|
| 1 | Auditoria do Garden N integrado | Conferir se `e1015da` fecha as lacunas da auditoria de 30/10: revalidação de aliados, altura/lease a cada tick, prazo do piso, teto de 16 alvos e três pulsos; revisar os frames reais. | AG (esta branch, só leitura) → Codex | Cada lacuna ligada a teste que falha sem a correção; frames inspecionados; playtest humano do Garden. |
| 2 | Quincy: rig em produção | Ligar `quincy_manifestation` (11 bones, 4 estrelas, 6 clipes) a controlador/renderer de produção e a eventos H/N/B/C; hoje só bancada. Interface mínima já exigida: sem hitbox, voo ou quinta estrela. | Codex (integração) com AG (ajuste de assets) | Cliente Forge mostra os clipes dos assets exportados disparados por estado de servidor; sem regressão em Kojaku/arco; multiplayer observado ou declarado pendente. |
| 3 | Quincy Vollständig: acabamento do kit | Estados/poses por técnica H/N/B/C, participação articulada das asas, falas/áudios que o usuário fornecer. Ginrei (estágio intermediário), destruição do C e valores são **propostas** abertas. | Humano decide; Codex implementa | Decisão humana registrada antes de código; kit testado no cliente; contratos M07 preservados (hurtTime, reações, descarte de flechas). |
| 4 | Quincy: lacunas de QA conhecidas | Impacto da flecha em bloco ainda não provado; fluxo G vs CHARGE/INSTANT e estado real a auditar. | Codex | Teste que exige colisão confirmada, não só PASS. |
| 5 | Proteção de ativação Bankai | Saída voluntária da câmera × fim no servidor; knockback não prova bloqueio de colisão entre entidades. | Codex | Teste de regressão por caso; sem reabrir o roteiro Ryūjin aceito. |
| 6 | Higashi | Aceite visual humano (uppercut Better Combat, pausa 1 s, vítima/gatilhos). | Humano | Aceite explícito. |
| 7 | Pendências artísticas/técnicas transversais | Primeira pessoa, dedicado/observador multiplayer, Photon gráfico, escuta de áudio: tratadas como coberturas, não defeitos confirmados. | Humano + Codex | Execução real declarada; ouvido humano para áudio. |
| 8 | Eficiência/retenção de QA | Inventário e consolidação de ~2 GiB / ~8,6 mil arquivos; medir build/QA e pausas de destruição extensa antes de otimizar. Sem apagar prova única de regressão aberta. | Codex + Humano | Plano aprovado e medição antes/depois com o indicador realmente observado. |
| 9 | Ryūjin experimento do sol modelado | Nota local do Codex (02/11); estado depende de leitura do documento no checkout dele. | Codex | A confirmar; não presumir. |
| 10 | Campanha M13 (Substituto + Soul Society) | Roteiro consolidado pela main (28 missões, 10 + 18); falta ficha de realização (mapas/acesso, custos, recompensas, UI, persistência). Próximo arco Arrancar pós-aprovação. Divergência: nosso roadmap 126 ainda cita MAIN-SUB-01 com Rukia como piloto de 3 missões — a main supera isso. | Humano + game design | Reconciliar 126 com M13/10.32–10.33 (feito pelo Codex, que consolida 126). |
| 11 | Projeto externo XERJ | Resposta ao contato Ivan/licenciamento (GPL-3.0). Fora do gameplay. | Humano | Decisão e envio por conta do usuário; este agente não envia nada. |
| 12 | Adiados por decisão humana | Aizen (kit), Gin (removido), Mentor/Proficiências (aguardam equipe), M11/formação racial, Rukia/Quests laterais. | Humano | Não reabrir sem pedido. |

## 3. Ordem de execução sugerida

1. **Fechar auditoria** (1) e conferir se a branch é segura para teste (feito em parte: check/GameTests após merge verdes).
2. **Decisões humanas** que destravam código: escopo Quincy (3), prioridade entre Quincy-produção, Garden playtest e Higashi.
3. **Quincy produção** (2, 4) pelo Codex, com assets AG; um cliente pesado por vez.
4. **Lacunas pequenas de QA** (5) em paralelo, arquivos distintos.
5. **Playtest humano** (1, 6, 7) e aceites.
6. **Eficiência de QA** (8) após os aceites, para não perder prova de regressão aberta.
7. **Campanha** (10) como planejamento paralelo; implementação só após a lista de poderes e a ficha aprovada.

## 4. O que esta branch pode fazer sem autorização nova

- Auditorias de leitura, comparação de branches e inventário (este documento).
- Rodar `check`/GameTests em branch própria, um Gradle por vez.
- Atualizar este inventário e o bloco inicial do roadmap `72`.

Não faz: editar `00-CONTINUIDADE-CODEX.md` ou roadmap `126` (Codex consolida), mexer em runtime compartilhado, push, merge para Feature/main, nem conta/modelo/provedor.
