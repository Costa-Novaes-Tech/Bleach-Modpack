# 124 — Prompt Antigravity: Senbonzakura, combo, rastros e kit visual

## Pedido para copiar à conversa Antigravity

Continue a reconstrução do combate básico da Senbonzakura na sua branch existente `codex/design-poderes-criaturas`, no checkout separado já utilizado. Você não está sozinho no projeto: preserve mudanças de outras conversas e não reverta arquivos alheios. Não trabalhar na main, não trocar a branch de outro checkout e não criar nova pasta.

Antes de editar, confira status, branch, log e divergência com Feature-Poderes-bankais. A etapa1 já foi integrada em2127c26; a referência da revisão Codex é ef64c45. Verifique os hashes reais, sem presumir que o checkout continua nesse estado. Se precisar sincronizar, preserve trabalho local e documente conflitos; não use reset destrutivo nem sobrescreva conteúdo. Commit/push/merge somente conforme autorização vigente da conversa, sem assumir que este texto autoriza alterar main.

Leia Docs/poderes/00-CONTINUIDADE-CODEX.md, contrato118, relatório121 da reconstrução diagonal, integração122 e análise123. Use as skills e instruções locais pertinentes. Documentação nova somente em Docs/poderes.

O usuário aprovou avançar a partir do diagonal. Preserve esse golpe como referência; se um ajuste de transição nele for indispensável, mostre o antes/depois e a razão. Ryūjin e Benihime foram aprovadas: não modificar suas poses, câmera, áudio, efeitos ou habilidades. Zaraki intocada.

### Entrega de agora: coreografia sem partículas

Reconstrua os movimentos abaixo; não apenas adicione canais JSON ou copie o mesmo corte com outra rotação.

1. Golpe2 — retorno horizontal, uma mão. Parta da posição final baixa à esquerda do diagonal, sem teletransportar a lâmina para o ombro e sem voltar ao idle entre ataques encadeados. Conduza a ponta para a altura do peito e varra da esquerda para a direita do próprio jogador. Ombro, cotovelo, punho e rotação moderada do tronco devem manter a lâmina no plano horizontal. A mão livre contrabalança naturalmente; não segura a espada. Desacelere no fim e recolha o braço para preparar a estocada. Não girar o corpo inteiro nem fazer dash.
2. Golpe3 — estocada central, uma mão. Parta da recolha do retorno: cotovelo próximo às costelas e ponta mirando à frente. Pequena antecipação corporal, extensão clara do braço dominante e avanço linear da ponta, com transferência de peso apenas visual. Evitar que a lâmina gire como outro corte ou atravesse a cabeça. Depois recolher o braço suavemente à guarda, permitindo reiniciar o diagonal sem salto.
3. Transições: mostre 1→2, 2→3 e 3→1, além de cada golpe isolado e ataque interrompido. Ao parar de clicar, recuperar idle normalmente. Não esconder pausas ou clipping com pétalas.

Autorias espelhadas para canhoto devem seguir o espelhamento real do Better Combat, sem dupla inversão. Não transformar o combo básico em golpes de duas mãos.

### Arquivos e responsabilidade

Sua responsabilidade são `src/main/resources/assets/bleachmod/attack_animations/senbonzakura_return.json` e `senbonzakura_thrust.json`, respectivos arquivos em `player_animation/`, e ajustes exclusivos do QA Senbon necessários às evidências. Manter os dois formatos coerentes e os canais de item coerentes com a empunhadura. Perfil exclusivo Senbon pode ser ajustado apenas com justificativa, preservando mecânica.

O diagonal possui cadência autoral beginTick8/endTick20/stopTick30/returnTick2. Verifique a velocidade real/upswing aplicada pelo Better Combat: ticks do arquivo não são automaticamente tempo final em jogo. Compare marcos de preparação, contato e recuperação. Não altere dano, alcance, cooldown, stamina, hitbox ou deslocamento real para disfarçar animação. Canais de item são parte da solução; renderer, transformação de display, bend e camada ativa também precisam ser considerados.

Arquivos comuns exigem alteração mínima e restrita à Senbon. A autorização desta revisão inclui substituir a emissão ARC legada no ramo Senbon de BetterCombatCompat, na mesma entrega do novo rastro. Não refatorar mixins globais, prioridades ou comportamento de outras armas. Se precisar desses ajustes amplos, registrar reprodução e proposta separada. Não aplicar offset universal baseado na Ryūjin.

### Evidências e validação

Primeiro trabalhe o retorno e registre prova real; depois a estocada e por fim o combo. Use o cliente Forge e QA existente. Não declarar aprovação visual com apenas build ou preview Blockbench. Apresente vídeo contínuo e frames de preparação/contato/recuperação, com tick, câmera, forma, branch/commit e perfil de render. Compare antes/depois na mesma posição.

Capturas sem partículas para ler a lâmina: frente, perfil e costas; primeira e terceira pessoa; destro/canhoto; selada/Shikai/Bankai; offhand livre/ocupada; atacar→parar→guardar→técnica→combate. Registre separadamente o que foi observado, falhou ou está pendente. Multiplayer observador não está validado por singleplayer.

Execute as verificações estruturais pertinentes e a regressão/check existentes. Não alterar expectativas para forçar sucesso. Se houver mudança funcional, rode também GameTests relacionados. Informe comandos, resultado e limitações; contagens antigas não são novos testes.

Critério visual: espada permanece solidária à mão; nenhum salto de pose; retorno horizontal identificável e estocada claramente linear; mão livre coerente; recuperação limpa; diagonal aprovado preservado. Entregue diff focado, relatório e evidências em Docs/poderes. O usuário autorizou executar também as fases visuais abaixo nesta mesma entrega. Use validação técnica e evidência por fase para avançar, sem exigir nova confirmação entre elas; o aceite artístico final permanece do usuário.

## Atualização de escopo autorizada pelo usuário

Esta seção substitui a antiga restrição de aguardar aprovação para construir o rastro. Execute as fases em sequência, mantendo pontos de comparação e entregas separáveis. Não parar apenas no laboratório: integrar no mod e entregar para testes. Não mascarar falha de pose com partículas. Não há autorização para mudar balanceamento, adicionar destruição de blocos ou redesenhar a transformação Senbon já aprovada.

### Fase0 — diagnóstico e mapa do kit

Confirme no código slots e formas: Bankai H=five_cuts, N=floral_garden, B=senkei, C=hakuteiken; selada/Shikai petal_cut, petal_stream, petal_convergence e petal_guard. C é Hakuteiken, não Jardim. Registre valores atuais a partir de SenbonPolicy/TechniqueDefinitions e serviços, incluindo tempos reais do dash, colisões e instantes de dano. Faça capturas de referência sob as mesmas condições que o resultado. Reutilize partículas, constructos e QA existentes. Registre qual camada controla corpo, arma e efeitos.

### Fase1 — completar esgrima

Execute a coreografia dos golpes2/3 especificada acima; mantenha primeiro passe sem VFX. Compare arma/mão na preparação, contato e retorno. O diagonal aprovado não deve regredir. Somente após esse controle técnico ficar correto, ligar os rastros. Se não conseguir executar QA gráfico, declare a limitação e entregue o resultado como pendente, sem alegar aprovação.

### Fase2 — rastro de pétalas solidário à lâmina

Implemente agora o rastro dos três golpes, sem a emissão ARC fixa concorrente. Autoria por identidade:

- Diagonal: fita descontínua de pétalas acompanha a lâmina de cima/direita para baixo/esquerda do jogador; ponta com pequeno destaque branco-rosa, sem cone genérico.
- Retorno: faixa horizontal curta acompanha esquerda→direita na altura do peito. Pétalas periféricas escapam do plano e se dispersam com vento suave.
- Estocada: filete estreito segue a ponta para frente; breve dispersão longitudinal no contato/alcance visual real, sem outro arco circular nem explosão de suprema.

A emissão deve derivar de amostras consecutivas da lâmina animada, considerando mão dominante, corpo, yaw/pitch, posição interpolada e transformação do item. Distribua alguns pontos entre base/meio/ponta e interpole segmentos para não quebrar a fita em baixa taxa de frames. Se o renderer não expõe matriz final de maneira segura, use amostrador específico Senbon baseado na mesma coreografia e documente aproximação; não chamar de ancoragem exata sem medir. Não copiar bladeHand da Ryūjin como solução universal.

Gerar partículas no cliente a partir de estado/evento de ataque identificado, com deduplicação por atacante/ataque. Não enviar posições de cada pétala pela rede a cada frame. Usar relógio do ataque real, evitando mudanças de quantidade conforme FPS. Cortar segmentos ao teleportar ou saltar posição; não conectar ataques distintos por uma linha longa. Esconder/atenuar em primeira pessoa de forma coerente, sem perder sincronismo para observadores. Espelhar uma única vez no canhoto.

Sugestão inicial de orçamento, a medir e ajustar: até12 novas pétalas por tick por atacante em qualidade normal durante a janela ativa,6 em reduzida,3 em mínima, vida8–16ticks; limite compartilhado de partículas vivas e culling por distância. Evitar emissão infinita ao segurar clique/pausar. Não sobrepor um segundo glint/mesh/rastro se já existir representação equivalente. Basic melee deve ser leve: preservar silhueta, rosto e leitura da arma. Provar lado a lado com VFX ligado/desligado e captura de baixa qualidade de partículas.

Remover exclusivamente o ARC legado do melee Senbon no mesmo patch em que o substituto funciona. Não afetar ARC das técnicas que ainda o usam nem a rede de outras identidades. Se mudar pacote/schema, justificar e preservar compatibilidade explicitamente; prefira evitar necessidade de protocolo novo. Valide emissor e observador, interrupção por defesa, troca de item/forma, morte, dimensão e logout.

### Fase3 — Hakuteiken C: suprema de concentração e perfuração

Objetivo: reconhecer C como a concentração final de Senbonzakura numa lâmina branca e num avanço perfurante, diferente do Jardim e do Senkei. Não basta aumentar contagem de pétalas ou colocar esfera rosa opaca. Preserve o dash, alcance, velocidade, dano por alvo, parede, custo e cooldown atuais.

Roteiro adaptado à linha do tempo já existente, sem alongar arbitrariamente gameplay:

1. Antecipação: pétalas próximas convergem em espirais estreitas para guarda/lâmina; reduzir a dispersão livre perto da arma para mostrar concentração. Mão dominante firma a arma à frente; mão livre equilibra o corpo ou apoia a pose apenas se a coreografia especial for autorada de modo convincente. Não reutilizar três golpes básicos sobre a técnica.
2. Formação: lâmina fantasmagórica branca com borda rosa clara torna-se nítida; formar duas silhuetas de asas de energia atrás dos ombros, com filamentos de pétalas e vazios entre penas. Asas são efeito temporário da técnica, não novo equipamento persistente. Cabeça e espada permanecem legíveis.
3. Arranque: pequena contração de asas e impulso do corpo na direção real do dash. Ponta branca lidera; cauda alongada branco-rosa fica atrás, nunca na frente do ponto de colisão. Efeito deve acompanhar cada passo real do avanço. Não teleportar o corpo visual para um destino ainda não alcançado.
4. Travessia: corte de ar estreito, pétalas puxadas lateralmente pelo deslocamento e contatos pontuais nos alvos realmente atingidos. Não sinalizar dano fora da trajetória ou através de paredes. Contra parede, parar também a ponta/cauda e dissipar; não desenhar explosão no destino bloqueado.
5. Conclusão: expansão breve em leque atrás do fim da estocada, fragmentos brancos se tornam pétalas rosas e caem. Evitar outra explosão de área que sugira dano inexistente. Recuperar postura e item, remover asas temporárias e liberar controles/câmera normalmente.

Preferir câmera de combate livre: nenhuma nova cinemática obrigatória para cada C, nenhum giro ou zoom que esconda colisão. Pode haver tremor leve/local configurável no impacto confirmado, nunca deslocamento que prejudique mira ou observadores. Primeiro sem áudio novo, reutilizando sons existentes adequados; não instalar serviços musicais ou baixar mídia sem necessidade. Áudio/VFX não podem multiplicar por alvo de forma ensurdecedora.

### Fase4 — coerência visual do restante do kit

Refinar os efeitos preservando mecânica e identidade aprovada:

- H Bankai: cinco cortes com direções distinguíveis, fases sucessivas e espaço entre faixas; não cinco nuvens idênticas. Trajetória visual segue a autoria de cada corte, sem virar rastro de ataque básico.
- N Jardim: manter centro fixo, raio35 e janela de reativação2–6s conforme contrato. Piso interno com pétalas distribuídas desde a primeira ativação. Na segunda, parte organiza a flor na borda enquanto o interior permanece preenchido; em seguida colunas/correntes sobem por toda a área. Nada de esvaziar o centro para criar apenas um anel. Usar piso visual/instâncias e partículas moderadas, sem spawnar milhares de entidades. Medir custo em qualidade mínima e quando vários jardins aparecem.
- B Senkei: lâminas espectrais com silhueta de espada, guarda, ponta e brilho delimitado, não só traços de partículas. Distribuição coerente com arena existente e reativação radial, sem sugerir que apenas o alvo da mira sofre efeito. Não mudar raio, duração ou dano nesta revisão.
- Shikai/selada: corte de pétalas legível; fluxo com direção e vazios; convergência identificável como prisão/levitação com pulsos contínuos, não uma explosão única; guarda visual discreta preservando a defesa. Não modificar acesso selado ao kit nem remover lâmina fantasmagórica.

Faça passes focados por habilidade; preserve versões já boas. Rastro básico suave e pequeno; técnica de corte intermediária; Hakuteiken concentrado e forte; Jardim/Senkei dominam o espaço. A distinção deve vir de forma, direção e ritmo, não apenas densidade. Não aumentar tudo ao máximo.

### Fase5 — integração, desempenho e entrega

Revalidar combo com técnicas e defesa, primeira/terceira pessoa, canhoto, offhand, observador, ataques contra chão/parede, cancelamentos e retorno normal. Rode check/regressionTest e GameTests pertinentes Senbon, inclusive dash sem acertar antecipadamente, colisão e único dano por alvo; nenhum snapshot alterado para ocultar falhas. Testar base sem Photon; perfil Photon separado se realmente alterado. Não tornar Photon obrigatório.

Use contagens/telemetria de emissão e frametime se disponível, com cenário/hardware descritos; sem afirmar FPS por impressão. QA visual real contém comparação antes/depois de cada fase, vídeo contínuo do combo e C, e frames por marco. Se multiplayer não for executado, deixar pendente.

Entregar relatório único com mapa de arquivos, mudanças por fase, resultados, limites e próximos testes. Commits separados sugeridos: combo; rastro/substituição ARC; Hakuteiken; restante do kit. Trabalhar até concluir as fases autorizadas ou documentar bloqueio real, sem pedir aprovação de rotina a cada fase. Aprovação artística pertence ao usuário. Não fazer merge automático na main nem alterar trabalho da outra conversa.
