# 133 — Prompt de correção: Senbon, katana e combo antes dos efeitos

## Copiar para a conversa Antigravity Senbon

Corrija a regressão visual apontada pelo usuário no vídeo27/10 075349. Leia Docs/poderes/132 e contatos qa/cycle132/senbon-1.jpg, contrato118 e prompt124. O usuário esclareceu: O PRINCIPAL DEFEITO É A KATANA NÃO REALIZAR OS MOVIMENTOS ORGANIZADOS. Alinhar pétalas ou adicionar mais brilho não resolve a entrega.

Trabalhe na branch existente codex/design-poderes-criaturas e seu checkout já usado. Você não está sozinho; não sobrescreva alterações de outras conversas. Confira Git/HEAD e sincronização com Feature-Poderes-bankais antes de editar, sem reset destrutivo, checkout de outro worktree ou merge sobre mudanças alheias. Correções Codex438628e (evento melee exclusivo/protocolo2.36, sem início local concorrente, sinal canhoto) precisam estar preservadas. Não reintegrar commits já ancestrais, não editar main nem instalar ferramentas desnecessárias.

## Fase1 — reprodução limpa e diagnóstico da arma

Antes de editar partículas, desligue somente VFX do QA e reproduza cada golpe isolado e combo completo no cliente Forge. A espada deve continuar visível nas três formas, inclusive lâmina fantasmagórica. Filme frente, perfil e costas com índice do golpe e tempo real ativo. Descubra o que está sendo renderizado: qual clip, velocidade, begin/end/return, camada/prioridade, bone do braço e item, transform de display/grip, renderer e mixin. Compare matriz/transform final observável da arma com o movimento do punho.

Verifique PlayerAnimatorHeldItemMixin, canais rightItem/leftItem efetivos, transformação de posição em1/16, rotação e bend, além da pilha de item. Não afirmar causa única pela presença/ausência de canal JSON. Não corrigir toda arma com offset global. Veja se outro controlador fixa o item ou se braços e item usam relógios distintos. Registrar causa reproduzida e prova antes/depois.

## Fase2 — executar exatamente os três golpes

Direções são anatômicas do jogador destro; espelhar uma vez para canhoto.

Golpe1 diagonal descendente: recolher katana junto ao próprio ombro direito com ponta alta e afastada do rosto. Punho/antebraço conduzem a lâmina da direita alta à esquerda baixa, cruzando à frente do peito. Ao fim, ponta e mão ficam baixas à esquerda, prontos para retorno; não espada parada na cintura enquanto braços giram. A referência antes aprovada deve ser recuperada visualmente, não presumida intacta pelo hash do arquivo.

Golpe2 retorno horizontal: partir do final do1 sem reset instantâneo; elevar pela transição para a altura do peito e cortar esquerda→direita em plano horizontal legível. Lâmina gira/avança com punho e cotovelo, tronco acompanha discretamente. Final desacelera e recolhe a arma para preparar estocada. Não repetir diagonal, não manter item orientado para o chão durante o corte.

Golpe3 estocada central: recolher cotovelo junto às costelas, alinhar ponta à frente e estender braço/katana linearmente, com pequeno ajuste visual de peso. Ponta avança, não varre em arco. Recolher à guarda sem salto e permitir3→1. Nada de dash físico novo.

Uma mão empunha no combo básico. Outra livre contrabalança sem cruzar a lâmina ou agarrar o punho como golpe de duas mãos. Sem clipping no rosto/torso, mão deslizando pelo cabo ou arma solta. Interromper cliques deve voltar ao idle, guarda deve assumir prioridade e técnica não pode deixar a espada presa na pose anterior.

Atualizar pares attack_animations/player_animation e perfis exclusivos apenas onde necessário. Preservar dano, alcance, cadência funcional e stamina. Tempo autoral não é tempo real: medir velocidade Better Combat e instante de contato, sem forçar gameplay para encaixar o vídeo.

## Fase3 — prova obrigatória antes do rastro

Critério técnico para avançar: com partículas DESLIGADAS, os três golpes são identificáveis pela katana em vídeo contínuo, arma mantém contato com mão e não há salto1→2→3→1. Comparar contra Asauchi/Ryūjin apenas para fluxo de item, sem copiar sua identidade. Build verde não é aprovação visual. Se falhar, continuar a esgrima; não produzir mais VFX para esconder.

Esta é ordem de trabalho interna, não pedido de nova aprovação de rotina ao usuário entre fases. Aceite final segue humano. Se não puder executar cliente, declarar pendência e não alegar conclusão visual.

## Fase4 — só então o rastro

Vincular trajetória/tempo do rastro à arma realmente animada, não usar linhas locais inventadas como prova de movimento da katana. Preferir mesma curva/transform da arma; aproximação específica só se comparada nos marcos e declarada. Rastro deve seguir diagonal, horizontal e estocada respectivas, com yaw/pitch, destro/canhoto e velocidade real. Sem reaproveitar AURA passiva para início do golpe, sem dupla emissão/reinício por previsão+servidor.

Menos brilho branco contínuo: pétalas leves com vazios, destaque discreto de ponta. Primeiro pessoa não pode virar barra rosa sobre o rosto. Preservar orçamento12/6/3 por tick e deduplicação; medir qualidade mínima e pausa/teleporte/saída. Não alterar Garden/Senkei ou Hakuteiken durante esta correção. O refino C e restante124 continuam backlog autorizado, mas prioridade é fechar regressão básica e entregar patch focado.

## Fase5 — entrega

Responsabilidade: assets/perfil Senbon, SenbonCombatVisuals e QA Senbon. Mudança em compat/mixin compartilhado só mínima e isolada, com justificativa e regressão nas outras armas; coordenar com Codex se amplo. Outra conversa trabalha Higashi: não tocar RyujinChoreography, câmera ou morte. Hyōrinmaru, Benihime e transformação Ryūjin congeladas.

Executar check/regressionTest e testes pertinentes ao código alterado. Prova real: isolados+combo em três vistas, primeira pessoa, canhoto, offhand, clique rápido/interrompido, defesa e técnica→retorno. Anotar não testados. Relatório em Docs/poderes com causa, diff, commit, condições de captura e vídeo sem/com VFX. Não relatar concluído apenas porque canais existem. Commit correção da arma separado do rastro; não fazer merge automático na main.
