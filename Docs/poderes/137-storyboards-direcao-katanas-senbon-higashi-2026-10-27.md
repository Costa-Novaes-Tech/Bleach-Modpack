# 137 — Direção por quadro: katana Senbon e execução Higashi

27/10/2026. Pedido: storyboards e prompts precisos, sem merge. Trabalho exclusivamente documental/visual sob Docs/poderes; não há alteração de assets/runtime do mod, execução de cliente ou homologação. Personagens esquemáticos são referências de autoria, não Minecraft renderizado. Hyōrinmaru e transformações aprovadas preservadas.

## Correção factual do teste

O usuário confirmou que TODAS as execuções do vídeo092434 vieram de dano normal letal, não de alvos abaixo de7%. Esse caso é o teste prioritário da regressão. Não inferir limiar pela câmera. Há relato e evidência visual de duas representações do inimigo: cadáver vermelho e representação usada para cinzas. O problema Senbon permanece a katana não cumprir os três gestos definidos, não densidade de pétalas.

## Como usar os materiais

[Abrir storyboard navegável](media/137-storyboards/index.html). Escolher ação e percorrer quadro a quadro. O botão de sequência usa650ms por marco por conveniência de leitura; NÃO é reprodução do tempo real e não prova fluidez. Slider percorre todos os ticks de referência.

33 poses-chave em PNG/SVG;162 referências por tick (31por golpe Senbon e69Higashi), todas em frente+perfil, e quatro pranchas. Coordenadas exatas de alvos em [poses.json](media/137-storyboards/poses.json), gerador reproduzível na mesma pasta. Cada S1/S2/S3-Fxx/HG-Fxx é pose-chave; Txxx é tick interpolado por smoothstep, não quadro originalmente desenhado à mão nem captura de60fps. A60fps, a renderização final requer interpolação sub-tick. Não prometer162 testes visuais do mod.

Sistema de coordenadas: origem nos pés; X direita anatômica do destro, Y cima, Z frente; valores em blocos de estudo, não unidades/bones PlayerAnimator. Comprimento visual de lâmina .98 e cabo .18 são proporções do desenho, NÃO aumento de alcance nem obrigação de escalar o item existente. O braço usa IK simplificada e corpo-base estático: não é rig final, simulação muscular ou modelo de jogador. A animação final deve adicionar tronco/apoios conforme roteiro, preservando comprimento real dos membros.

Legenda: branco lâmina, dourado cabo, verde ponto de empunhadura, rosa ponta. Frente espelha a direita anatômica na tela; perfil mostra avanço à direita da imagem. Sobreposição aparente em frente precisa ser julgada em perfil: espada deve passar à frente, não dentro do rosto. Representação da vítima Higashi é esquema; o mod deve conservar o modelo original/variante, inclusive asas e equipamento.

## Senbon: roteiro comum

Uma mão dominante empunha, mão livre contrabalança fora da trajetória. Cabo fixo na palma, sem deslizar; antebraço e punho conduzem o item, tronco acompanha moderadamente e pés mantêm apoio. Não mover hitbox ou criar dash. Não alterar dano/cadência funcional para copiar a duração nominal da prancha. Ticks0/4/8/11/14/17/20/30 indicam fases autorais; mapear ao tempo efetivo do Better Combat e documentar velocidade/upswing.

As junções S1-F06/F07→S2-F00 e S2-F06/F07→S3-F00 coincidem em mão/direção no desenho. Isso só vale para ENCADEAMENTO. Se clique terminar, recuperar para idle em vez de congelar baixo até o próximo ataque isolado. S3 retorna à guarda deS1-F00, não diretamente à preparação alta deS1-F02. Provar também comportamento da camada entre clips, pois igualdade de arquivos não evita blend incorreto.

### S1 — diagonal descendente

[Prancha S1](media/137-storyboards/S1-prancha.png). F00guarda; F01recolha ao ombro; F02preparação com mão alta à direita e ponta elevada afastada do rosto; F03início do corte à frente; F04passagem central; F05descida à esquerda; F06final baixo esquerdo; F07encadeamento/recuperação contextual.

Corpo: peso começa equilibrado, pequena rotação de ombros na preparação, acompanha a diagonal sem giro completo; cabeça mantém alvo. O centro visual do ataque é a lâmina, não apenas braço. Não deixar ponta baixa enquanto mão arma o ombro.

### S2 — retorno horizontal

[Prancha S2](media/137-storyboards/S2-prancha.png). F00herda final doS1; F01eleva pela esquerda; F02define plano de peito; F03corte esquerda→direita; F04ponta atravessa eixo frontal; F05continuação à direita; F06recolhe cotovelo para costela; F07ponta preparada à frente.

Tronco desfaz rotação do diagonal com amplitude contida; ombro/cotovelo/punho mantêm plano, não torsão de punho para compensar braço errado. A subida F00→F02 é transição, não um segundo corte diagonal. A lâmina horizontal deve ser reconhecível também em perfil, sem girar em hélice.

### S3 — estocada central

[Prancha S3](media/137-storyboards/S3-prancha.png). F00herda recolha; F01cotovelo junto às costelas; F02ponta alinhada; F03extensão; F04contato; F05sustentação curtíssima; F06recolha; F07guarda.

Corpo faz pequena transferência de peso à frente e retorna, pés não dão dash. A ponta avança em linha; não varrer como outro corte nem baixar no contato. Projeção frontal pode encurtar espada por perspectiva: validar avanço em perfil. Testar braço não esticado além do limite e sem duas mãos no combo básico.

## Higashi: roteiro especial

[Prancha HG](media/137-storyboards/HG-prancha.png). Tempo de apresentação0..68ticks:

- HG-F00/t0: confirmação autoritativa já ocorreu; câmera lateral estabelecida e apenas UMA vítima visível, na mesma posição/aparência. Começo do replay visual, sem segundo dano.
- F01/t5: katana recolhe junto ao próprio quadril, cotovelo dobra e tronco prepara a subida; alvo sem queda/flash/fogo vanilla.
- F02/t10: mão baixa e gume pronto para diagonal ascendente. Ponta mostra preparação sem atravessar coxa/solo.
- F03/t12: lâmina passa baixo→alto à frente e através do volume visual do alvo. Peso transfere, ombro acompanha; mão e item solidários. Faíscas discretas opcionais só depois de validar sem VFX.
- F04/t16: espada terminou alta do outro lado do corte; não começar pausa antes disso. Ombros concluem, cotovelo não trava em hiperextensão, pés apoiados.
- F05/t26: metade da pausa; mesma pose final e vítima única, serena visualmente, sem fogo antecipado. Sem novo swing.
- F06/t36:1segundo/20ticks após fim; ignição controlada, começa escurecimento e fade da MESMA representação. Não revelar cadáver oculto ao iniciar dissolução.
- F07/t48: corpo dissolvido; cinzas ocupam volume coerente, sem virar painel retangular nem corpo reaparecer. Espada inicia recuperação.
- F08/t68: partículas residuais discretas, jogador em guarda e câmera/HUD/FOV/controles devolvidos. Sem entidade visual órfã.

Controles de segurança podem encerrar antes conforme contrato; demonstrar limpeza. Replay não muda dano, morte/drop/XP do servidor ou posição real. Para alvo aéreo, não reposicionar o jogador de forma fictícia: escolher composição que mostre posições verdadeiras ou fallback suportado. Não deformar um Hollow alado para virar boneco humanoide durante replay.

## Aceite e método

Primeiro arma sem VFX. Comparar marcos com mesmo yaw, câmera, handedness e escala. IA deve entregar lado a lado REFERÊNCIA ESQUEMÁTICA versus CAPTURA FORGE REAL, sem rotular desenho como evidência do jogo. Observar mão/cabo, direção da lâmina, ponta, tronco, apoio e recuperação. Use tolerâncias artísticas justificadas por proporções, não exigir igualdade de pixels entre modelo esquemático e Minecraft.

Bloqueios: qualquer katana parada/baixo na preparação alta; estocada virando corte; cabo descolado; blend reiniciando idle no combo; duplicação de vítima; morte vermelha/queda/fogo antes da ignição confirmada. Partículas e números de regressões não compensam falha nesses itens.

Validação dos arquivos de referência:162poses finitas, direções normalizadas, comprimento de lâmina constante e junções1→2/2→3 conferidas por script; NÃO validação de animação runtime. Proposta de poses para revisão do usuário, não canon nem nova mecânica.
