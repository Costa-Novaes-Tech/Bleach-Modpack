# 146 — Antigravity Higashi: segurança da confirmação e QA para Codex

27/10/2026. Encaminhar à conversa Higashi existente, codex/ryujin-bankai-sol-selamento, checkout Bleach-Modpack-benihime. Executar testes/capturas e disponibilizar arquivos locais para Codex. SEM MERGE/PUSH/MAIN. Você não está sozinho; preserve alterações Codex e outra frente. Não enviar mensagem a outro chat por conta própria.

## O que a auditoria aceita e o que falta

Commit0f3a1e1 separou casos e registrou cliente/GameTests/check. A remoção de conflito indexando executor emPOSES está coerente com a pendência anterior. Contudo, os cinco casos básicos e dois quadros sem vermelho NÃO demonstram segurança da arquitetura nem ausência de flash em todos os frames. Katana permanece pendente; não atribuir isso a falta de aprovação de VFX/storyboards: os marcos137 já existem e partículas não são a correção do gesto.

Codex encontrou no diff:
1. confirmedDeath é chamado dentro de LivingDeathEvent, com prioridade padrão, antes de terminar a distribuição do evento. `health<=0`/`!isAlive()` naquele instante não prova que um listener posterior não cancelará/recuperará a entidade. Seu hook não comprova confirmação FINAL e pode marcar `bleachmod_higashi_confirmed` e transmitir morte de um alvo que sobreviverá.
2. ACTIVE_CAST delimita o contexto do cast, mas não necessariamente a vítima/causa de toda morte aninhada. Conferir dano refletido, reentrada e mortes secundárias para não atribuir execução à entidade errada.
3. PENDING_TARGETS é set concorrente de IDs, sem expiração/limite/geração do mundo. Há remoção na aceitação normal/reset, mas isso não cobre todos retornos antecipados, troca de mundo e IDreutilizado. Não tratar thread-safe como lifecycle-safe.

Antes de expandir cinematografia, reproduzir esses casos e corrigir somente riscos comprovados por arquitetura/teste. Nenhum patch pode voltar a esconder original+réplica de forma errada ou anunciar morte antecipada.

## Fase1 — testes autoritativos negativos

Adicionar GameTests focados conforme infra existente:
- Higashi dano normal letal, mas LivingDeathEvent cancelado por listener posterior: entidade recuperada/viva, ZERO evento confirmed/execução efetiva, nenhuma flag persistente impedindo outra execução válida, câmera não abre e vítima não fica invisível. Testar ordem/prioridade explicitamente; não escrever teste que apenas cancela antes do seu listener e deixa o problema descoberto.
- Totem e dano cancelado: vivo, nenhuma execução. Sobrevivente/miss continuam válidos.
- Morte verdadeira por dano normal acima7% e execução≤7%: confirmação única, drop/XP sem duplicar.
- Dano refletido ou morte secundária dentro do contextoACTIVE_CAST: apenas vítima elegível realmente atingida pela Higashi pode apresentar execução; caster/aliado/outroalvo não recebe tag indevida.

Usar contagem observável de confirmação/evento, não só verificar morto/vivo. Listener de teste deve ser removido ao fim para não contaminar suítes. Não desativar Forgeevents nem modificar expectativas para aceitar falso positivo.

Resolver ordenação sem trocar segurança por cosmética. Se for necessário separar hint visual pendente de confirmação final, hint não abre câmera, não incinera, não cria cadáver e tem rollback/timeout. Proibir “confirmed” antes da decisão final. Não presumir que priorizar o mesmo hook é suficiente para todos mods; documentar alcance e mecanismo.

## Fase2 — pendências cliente e lifecycle

Implementar marcação pendente limitada/expirável e vinculada à sessão/mundo correto, sem acessar mundo/render fora da thread apropriada. HandlerNetty pode publicar dados mínimos em estrutura segura, não manipular entidade/render nessa thread.

Testar: pacote duplicado; aceitação abortada/indisponível; troca de dimensão/desconexão entre nota e enqueue; ID reciclado; alvo não existe ainda/já removido. Nada pode ficar oculto indefinidamente ou ocultar entidade diferente. Limpar pending em todos os caminhos. Guardas persistentes da vítima precisam de comportamento definido diante de morte cancelada/recuperação; não deixar flag eterna bloquear execução real posterior.

Manter esquema/protocolo coerente se houver alteração. Não ampliar patches a kits alheios. Ler137/141/143 na raiz principalBleach-Modpack, preservandoQA local.

## Fase3 — captura gráfica correta

Somente após os negativos acima, repetir lethal_husk10/20, lethal_flying10/20, threshold_real7/100, survive1000, miss. MundoQA limpo visualmente, sem fogo de cenário anterior ao lado da vítima. Câmera própria do renderer, captura player+alvo em frente/perfil, variante/asa preservada.

Cobrir desde ANTES do dano até volta do controle. Registrar em cada frame: worldTick, renderFrameID/partialtick, vítima/executor, recepção hint/confirmed, idade realPOSES/BURNS, originalPresent/originalSuppressed e réplica visível. Não nomear fase por elapsed-20. Capturar vídeo contínuo se ferramenta instalada permitir; screenshots com gaps são limite declarado. “Zero vermelho” em dois quadros não aprova toda a transição. Se usar análise de pixels, documentar ROI/máscara e limiar, inclusive falsos positivos por roupa/fogo/fundo; inspecionar visualmente.

Pausa20ticks após fim do corte e limpeza precisam de régua real. Não chamar avaliação de cores de verificação da coreografia. HG-F01..F04 permanecem separados e pendentes até evidência real da katana, preferencialmente sem efeitos. Não adicionarVFX para concluir o corte artificialmente.

## Comandos (PowerShell)

```powershell
Set-Location -LiteralPath 'C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-benihime'
git status --short --branch
git log -4 --oneline
git diff --check
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot'
.\gradlew.bat check build --offline --console=plain
# Conferir antes o runDirectory descartável configurado para GameTests:
.\gradlew.bat runGameTestServer --offline --console=plain
# Com testes/cliente anteriores encerrados:
.\gradlew.bat runClient -PbenihimeQa=true -PcinematicQa=true -PhigashiPoseQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain
```

Rodar sequencialmente, não em paralelo com outro cliente da máquina. Contagens reais por comando; não misturar149/150 de branches diferentes. GameTests não aprovam imagem e check não roda servidorGameTest automaticamente.

## Entrega direta para auditoria

Pasta Docs/poderes/qa/cycle146-higashi/<run-id>/ com manifest de commit/diff, comandos/exits, ambiente, hashes e caminhos absolutos; logs resumidos dos casos negativos e positivos; timeline.jsonl; PNGs originais da janela de morte; vídeo ou sequência com gaps/cadência explícitos; comparativos HG quando feitos. Preservar material anterior como baseline, não sobrescrever evidências.

Relatório responde separadamente: confirmação segura; pending limpo; flash; vítima única; katana. Não chamar tudoPASS se um item não foi testado. O usuário encaminhará esse relatório ao Codex, que poderá abrir os arquivos locais diretamente. Não fazer merge; entregar correções focadas e limitações para revisão.
