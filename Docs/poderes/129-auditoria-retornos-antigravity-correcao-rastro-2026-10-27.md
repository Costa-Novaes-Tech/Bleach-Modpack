# 129 — Auditoria dos retornos Antigravity e correção Senbon

27/10/2026. Branch Feature-Poderes-bankais, HEAD inicial f4342ba, seis commits à frente do origin local. Sem fetch nesta rodada: isso não comprova estado remoto atualizado. Documentos123–126 e alterações de continuidade já existentes preservados; logs modificados por outras execuções não foram revertidos. Nenhum merge/commit/push realizado nesta auditoria.

## Entregas conferidas no Git

Senbon: 9bfec03/157323a/382620d já ancestrais do HEAD f4342ba; não repetir merge. A branch design está em36dd739, com registro documental equivalente de integração. Os diffs implementam golpes2/3 e SenbonCombatVisuals, integração do emissor e capturas stage2/stage3. Há46 PNGs e manifests; presença de capturas não prova toda a matriz ou continuidade do movimento.

Não há alteração de Hakuteiken/constructos/geometria/política nos commits entregues. Os cinco momentos descritos pelo relatório127 são apresentação/captura do efeito existente, não uma nova implementação demonstrada dessa fase. Também não há mudanças específicas de Jardim, Senkei ou demais habilidades do passe visual pedido124. Portanto a entrega não encerra124 por inteiro.

Hyōrinmaru/Higashi: 99fbcc0/1e7aefc/0675778 estão na branch codex/ryujin-bankai-sol-selamento, ainda não integrados na Feature nesta revisão. Modelo ajusta mandíbula/scutes e remove canais concorrentes de cabeça/mandíbula nos clips; sopro recebe offset e direção tridimensional; Higashi muda recolha e escolha de lado da câmera. Isso não demonstra novo modelo completo, asas/cauda refinadas ou QA visual real. Blockbench MCP indisponível declarado corretamente, com uso dos geradores. Relatórios citam check/build, mas check/build não equivale a runGameTestServer nem prova câmera no cliente: solicitar logs/comando/exits de GameTests e evidências visuais antes de homologar.

## Defeitos identificados e correção local

1. AURA passiva Bankai com pattern0 era emitida periodicamente pelo serviço; SenbonPetalEffects tratava toda AURA pattern0..2 como melee. Consequência pelo fluxo: aura suprimida e rastros iniciados em idle. Separado shape MELEE_TRAIL12, preservando AURA10 e demais formas; validação restringe combos a0..2. Protocolo2.36 exige clientes/servidor correspondentes, pois a semântica visual mudou.
2. Dois disparos do mesmo melee: ATTACK_START local e evento servidor, deduplicados apenas no mesmo tick. Removido início local concorrente: servidor confirma o evento para emissor e observadores. Validar latência na prática; isso não resolve sozinho a sincronização fina da animação.
3. Canhoto: sinal aplicado no eixo e novamente nas coordenadas locais anulava espelhamento lateral. Eixo anatômico agora é estável e o sinal aplicado nas coordenadas uma vez.
4. Rastro ainda é aproximação manual em coordenadas, com janela fixa14ticks e progressão independente da velocidade real Better Combat, sem pitch no vetor frontal. Não é matriz final da lâmina nem amostragem da mesma curva dos assets. Correção de sincronismo/ancoragem permanece próxima tarefa; não homologar visualmente por este patch.

Regressão nova verifica separação aura/melee, combos válidos e roundtrip dos três eventos. Caso antigo de shape inválido ajustado de12 para13 pela ampliação explícita do enum; validação não foi relaxada para esconder falha. Build/check iniciado nesta rodada; resultado registrado abaixo quando concluído. Nenhum cliente iniciado nesta rodada.

## Próximas fases

1. Validar correção isolada de idle/aura/rastro; sincronizar relógio e aproximação com a coreografia Better Combat. Comparar vídeo frente/perfil/costas e canhoto, ligado/desligado.
2. Implementar realmente o refino C Hakuteiken do124 (concentração, asas temporárias, arranque/travessia/dissipação) sem mudar mecânica ou câmera livre. Depois passe visual das demais técnicas autorizado124.
3. Revisar/importar Hyōrinmaru/Higashi em entrega isolada após coordenação Git, preservando esta correção e documentos; obter mandíbula/sopro e execução em cliente real. Possível corte de180graus ao trocar flanco da Higashi precisa observação em corredor, não assumir retorno suave apenas pelo relatório.
4. Homologação do usuário antes de encerrar pendências. Transformação Ryūjin aprovada e Benihime preservadas.
5. Após fechamentos obrigatórios, seguir126: escolher origem/era, contato principal, três missões e encontro Hollow; reconciliar infraestrutura existente primeiro. Não iniciar campanha completa ou regras de recompensas ainda abertas em M11/M12.

## Validação final desta correção

`./gradlew.bat check --offline --console=plain`: exit0, BUILD SUCCESSFUL em1m58s,149 cenários de regressão aprovados. Java17.0.20.1; cache Gradle externo exigiu execução com acesso ampliado após bloqueio de arquivo .lck no sandbox. Sem download solicitado (modo offline), sem cliente gráfico, GameTests ou build de distribuição novo nesta rodada. Avisos de APIs deprecated permanecem. `git diff --check` sem erros. Novo evento/espelhamento ainda exigem teste visual; não declarar homologação.

[Complemento130 para as duas conversas](130-complemento-antigravity-entregas124-125-2026-10-27.md) detalha os próximos passos e lacunas de evidência.
