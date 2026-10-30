# Antigravity — Senbonzakura: Garden N como prisão de pétalas

Prompt executivo atualizado29/10 após a entrega Quincy4185558. Esta versão substitui a espera e as instruções antigas deste arquivo. Copie a seção seguinte ou mande a IA ler o arquivo integralmente.

## Retorno Codex30/10 ao commit7750815 — correção antes de integrar

Esta revisão é mais recente que o resultado relatado na branch. Ainda NÃO houve merge para a poderes. Preserve seu trabalho; leia este bloco da origin/Feature-Poderes-bankais atualizada antes de implementar. O log run-gametest/base/logs/latest.log do checkout design confirma All283requiredtestspassed em30/10/2026 14:00:54; não foi uma nova execução do Codex nem prova de cliente real. Fonte7750815 revisada por diff. Base2eb3898 já está atrás da documentação5fb4c9a/844d02d; faça novamente o merge de entrada da poderes remota atualizada na SUA branch, preservando Garden e o resultado documentado.

**Acertos a preservar:** estado gardenPrison independente de B, commit tardio antes de expirar janela, pulsos20/40/60 antes da limpeza, posição fixa,14HP base repartidos. Os21HP no fixture vêm do multiplicador1,5 da Bankai sem defesa naquele cenário: não chamar de dano líquido garantido em qualquer alvo. Não mexer no combo/transformação/Quincy.

**Correções obrigatórias desta rodada:**

1. Em tickGardenPrison revalidar TechniqueTargets.allowed a cada atualização, inclusive mudança de aliança/PvP/espectador/NPC protegido depois da captura. O laço atual só remove mortos/ausentes/fora de volume/sem linha; um capturado que se torne aliado pode continuar recebendo controle/dano.
2. Controle de altura: checagem a cada4ticks e Vy=0 apenas nessa checagem não demonstram limite2,5 durante a lease8. Impedir aplicação/continuação do controle próprio quando colisão/limite ocorrer, mantendo o limite durante TODOS os ticks pertinentes e distinguindo a origem do efeito. Não truncar/remover efeito externo nem amortecer sua velocidade como se pertencesse à prisão. Testar subida efetiva, teto que surge durante lease, transição para limite, término/cancelamento e efeito externo forte/igual longo.
3. FIELD do piso: accept começa com f.until=now+10 e só depois max(now+15); isso apaga o prazo75 de RISE. Como finished continua true, o fade de20ticks pode oscilar a cada renovação. Preservar extensão monotônica adequada enquanto prisão existir, separar piso persistente de fade final e provar visualmente duração real. Não assumir que max após um reset preserva a duração anterior.
4. Destinatários: pulseGarden dá5HP a todos os elegíveis ANTES de escolher16 para os pulsos. Isso não é o mesmo conjunto recebendo5+3+3+3. Na segunda fase V0, capture primeiro os16 válidos, então aplique burst e pulsos a esse conjunto; primeira explosão6HP mantém sua regra atual. Se propor burst para mais alvos, declarar como mudança de contrato em vez de afirmar orçamento completo para todos.
5. floorY hoje é o Y inicial do alvo, não piso terreno consultado. Declare a referência corretamente e teste capturados já elevados/relevo; se implementar busca de piso, limite-a, respeite chunks/colisão e não teletransporte o alvo para o chão.

**Os seis testes atuais NÃO cobrem todo o contrato anunciado:**

- Cap: health<1000 pode ser satisfeita apenas pela primeira explosão; a comparação dos distantes com alvo15 não prova exatamente16capturados/praticando pulsos. Registre saúde logo antes do bloom, deltas por fase, conjunto de UUIDs e exclusões exatas. Incluir casos de empate e volume/parede próximos antes do teto.
- AlliesOrCaster não cria aliado. Crie aliado real/team, NPC protegido e espectador quando aplicável; teste também inimigo que vira aliado DEPOIS da captura. Não renomear teste para esconder ausência de cobertura.
- Levitação/teto observa só tick75, no início da prisão. Isso não prova limite2,5 ao longo da subida ou preservação da lease completa. Observe posição, colisão, Vy e efeitos a cada tick até depois da expiração. Não fixar posição/velocidade manualmente para fabricar sucesso.
- Late5,8s aceita health<=980: primeira explosão +burst+um pulso já podem satisfazer esse limiar. Meça os três deltas20/40/60 e t59/t60/t61, referenciados ao início REAL da prisão. Não usar só atraso global do cenário.
- Inclua ciclo de vida: sair/morrer/dimensão/trocar kit/fim da forma e prisão B independente. Não apagar levitação externa ao limpar.

**QA visual ainda ausente:** executar cliente Forge em mundo descartável, com primeira explosão/flor/prisão/fade reais, perfil distante e alvo próximo, teto baixo e reativação tardia. Inspecionar o tapete interior e envoltórios, não somente log de emissão. Retenção compacta: origem/hash/tempos e poucas capturas, idealmente trecho curto contínuo para demonstrar transição; só quadros não comprovam fluidez. Nenhum cliente pesado em paralelo com o outro checkout. Informe reserva de uso antes de iniciar.

Entregue correção focada e relato honesto: problemas corrigidos, testes realmente executados, falhas, contagens verificadas e limitações. Não afirmar cobertura de aliado/altura/todos os critérios por nome do teste. Commit somente sua branch codex/senbon-garden-prisao; sem merge de retorno na poderes/main nesta rodada.

## Missão e resultado esperado

Implemente o Garden N da Bankai como prisão ampla após a segunda explosão: pétalas continuam cobrindo o interior, a flor se forma, inimigos capturados levitam de maneira limitada, envolvidos em pétalas, e recebem pulsos de dano. O usuário já aprovou o combo atual da Senbon: preserve arma, empunhadura, animações prontas Better Combat, rastro de combate e transformação. Não reabra os antigos JSONs para tentar melhorar uma pendência já aceita.

Você não está sozinho no projeto. Não reverta alterações de outras conversas. Responsabilidade exclusiva: estado/colisão/lifecycle do Garden, efeitos visuais do Garden e testes correspondentes. Preserve Quincy/Vollständig, proteção Bankai/Ryūjin, Benihime, Hyōrinmaru, Higashi e Zaraki. Não altere as regras da prisão B por compartilhar um nome ou tipo de estado.

## Fase0 — contexto, sincronização e baseline obrigatórios

1. Leia integralmente este prompt, Docs/poderes/00-CONTINUIDADE-CODEX.md, antigravity-frentes-2026-10-29.md e skills/bleach-antigravity-entrega/SKILL.md. Use as skills locais bleach-mod-dev e bleach-game-design pertinentes, depois de localizar suas instruções reais. Ferramentas disponíveis não significam ferramentas testadas.
2. Confira caminho, git status/diff, branch, HEAD, upstream e git worktree list. Use um checkout individual existente, liberado pela conversa responsável. Não opere em Bleach-Modpack, não crie outra pasta/worktree e não troque a branch de uma conversa ativa. Git limpo não prova liberação do checkout.
3. Depois de ler e analisar todo o prompt, execute git fetch origin e compare sua branch com sua respectiva origin, origin/Feature-Poderes-bankais e origin/main: SHAs, ahead/behind e arquivos divergentes. Main serve de comparação, sem merge automático dela.
4. Preserve alterações locais/autoria. Integre eventual avanço da sua própria origin e faça o merge de origin/Feature-Poderes-bankais NA SUA branch individual antes de implementar. Para uma frente nova, crie codex/senbon-garden-prisao a partir da origin/Feature-Poderes-bankais atualizada. Se já existir, inspecione-a e integre a poderes; não recrie/reset a branch. Prove ancestralidade com git merge-base --is-ancestor. Base mínima de referência4185558, mas busque a versão remota mais recente, incluindo este prompt.
5. Resolva conflitos semanticamente, preservando as duas entregas. Não use ours/theirs em bloco, git add ., reset --hard, clean, force-push ou stash genérico. Confira git ls-files -u e git diff --check. Não remova arquivos porque não pertencem ao Garden.
6. Execute baseline adequado antes da mudança. Referência da poderes:158regressões e277GameTests aprovados na repetição final. Houve uma falha isolada anterior no teste legado tankNaturalAiAttacksEveryFortyEightTicks; repetição sem mudança passou. Registre ocorrências reais, sem alterar testes de Hollow para obter verde.

## Fase1 — investigação e prova mínima de comportamento

Reconstrua a cadeia N → reativação → animação da flor → finishField → dano/VFX/limpeza. Leia os arquivos vigentes, sem presumir que correspondem a um relatório antigo:

- common/technique/SenbonzakuraTechniqueService.java, SenbonPolicy.java e TechniqueDefinitions.java;
- common/visual/SenbonGardenGeometry.java e SenbonGeometry.java;
- client/particle/SenbonGardenFloor.java, SenbonPetalEffects.java e SenbonPetalsS2C;
- GameTests do Garden e lifecycle do serviço.

Observado pelo Codex em4185558: finishField ainda usa FIELD_BLOOM_DAMAGE=14 numa aplicação e limpa s.field. tickPrison/s.prison pertencem à Convergência B, usam raio pequeno, linha de visão do dono e expiração anterior ao tick60. Portanto não são uma implementação pronta da prisão do Garden. Não ligue N a esse estado sem separar os contratos. tickField também verifica expiração da janela antes de processar uma flor já aceita: teste a reativação tardia.

Antes de editar, registre uma síntese curta: comportamento observado, causa sustentada, hipótese, teste que a refuta, arquivos e critérios. Prove primeiro em um alvo, depois em múltiplos, sem depender de partículas para concluir que houve captura/dano.

## Contrato V0 a implementar

- Centro fixo da primeira explosão, raio horizontal35blocos, dano inicial6HP e reativação entre2 e6s preservados. Não mover campo com o jogador nem exigir mirar em cada inimigo. Preservar custos e cooldown atuais.
- Uma reativação válida compromete a segunda fase. A animação aceita e a prisão não podem desaparecer ao alcançar o antigo prazo de6s. Estado próprio da prisão:60ticks completos a partir da segunda explosão efetiva, mesmo com reativação em5,8s.
- Segunda fase totaliza14HP brutos por alvo elegível:5HP na flor +3HP em t20 +3HP em t40 +3HP em t60. Não acrescentar isso por cima dos14HP antigos. Vida perdida depende de defesas/cancelamentos. Não zerar hurtTime/invulnerableTime para forçar o dano.
- Execute o terceiro pulso ANTES de descartar o estado; testar t59/t60/t61. Não causar pulso em t0 nem dano extra na limpeza.
- Capture até16alvos elegíveis por UUID na segunda explosão, priorizando os mais próximos ao centro de maneira estável. Declare o teto; não prometa quantidade ilimitada. Não admitir novos alvos depois. Estado e alvos independentes de s.prison/B: conjurar N não deve substituir a prisão B nem herdar seu raio3,5.
- Volume inicial explícito: raio35 horizontal, alcance vertical ±5blocos em relação ao plano central, como V0. Linha livre a partir do centro do campo, com origem elevada coerente; não usar owner.hasLineOfSight como vínculo da prisão. Olhar para outro lado ou andar dentro das regras existentes não cancela alvos por depender da posição atual do dono.
- Ancore o limite de levitação no piso/posição inicial válida de CADA alvo. Reavalie colisão de cabeça/teto e limite de elevação2,5blocos, sem atravessar blocos, teletransportar ou subir indefinidamente. Saída do volume horizontal/vertical de contenção deve ser tratada explicitamente; não transportar alvos de outra dimensão de volta ao campo.
- Reavalie morte/remoção, dimensão, aliança, espectador e política TechniqueTargets. Não afetar dono, aliados e NPCs protegidos. Limpeza em morte/logout/dimensão/troca de kit/fim da forma, sem dano novo; confira também ausência de VFX órfãos.
- Lease curta de controle8ticks. Preservar levitação externa mais forte OU de igual potência com duração maior. Não chamar removeEffect(LEVITATION) cegamente ao terminar. Se a lease não permitir garantir teto/altura durante os8ticks, escolha outra aplicação de controle local segura, explique a decisão e prove o comportamento; não aceite uma verificação que só amortece um tick e deixa subir nos sete seguintes.
- Uma busca espacial na captura, depois atualização O(N) sobre os UUIDs capturados. Sem varredura global a cada tick, sem carregar chunks distantes.

## Fase2 — direção visual do Garden

Desenhe a sequência temporal em texto antes do VFX: expansão da primeira explosão → tapete interior → flor se organiza → colunas/pétalas ascendentes → envoltórios individuais → dissipação. Reutilize recursos existentes; nenhuma biblioteca nova é necessária para esta prova.

Preserve pétalas NO INTERIOR durante a flor e durante toda a prisão. A borda não pode ficar bonita às custas de esvaziar o piso. O renderer atual mantém parte das âncoras no chão: preserve esse princípio, adequando tempo de vida ao novo estado. Renove o piso até o término real da prisão, inclusive na reativação tardia, e faça fade final legível.

Cada alvo deve ter espiral/envoltório leve acompanhando seu corpo e uma subida clara. Mantenha visíveis silhueta e movimento do mob. VFX individuais limitados inicialmente a4alvos por tick, em rodízio entre capturados, com culling e qualidade de partículas; isso NÃO reduz o conjunto que recebe dano. Não crie jato constante que cubra a tela. Preserve desenho da flor, tapete, cor rosa/branco e densidade suave fora dos momentos de impacto.

Não editar weapon_attributes/senbonzakura.json, attack_animations, player_animation, geradores do combo, BetterCombatCompat ou cinematográfica de transformação. C/Hakuteiken não está incluído nesta implementação: reporte uma pendência objetiva se realmente observada, sem refazer habilidade por histórico superado.

## Fase3 — testes que podem reprovar a entrega

GameTests significativos, com cenário controlado e fluxo real de cast/reativação/ticks, não apenas callback direto:

- Reativar antes2s, em2s, em5,8s e após6s; spam não repete captura/custo/dano. A janela expirada sem reativação encerra normalmente.
- Confirmar5 +3 +3 +3 nos ticks definidos, inclusive terceiro pulso e nenhuma repetição após60/61. Separar orçamento solicitado de HP real contra armadura; usar alvo controlado e evidenciar ambos.
- Centro, borda, fora, terreno irregular, cota diferente, parede, teto baixo e piso individual. Alvos capturados saindo do volume, morte/remoção e dimensão.
- Dono olhando para trás e movendo-se sem deslocar o campo; preservar as condições preexistentes de reativação do dono, não removê-las silenciosamente.
- Mais de16inimigos, aliados/caster/espectador/NPC protegido; somente capturados elegíveis recebem pulsos. Rodízio VFX não altera dano.
- Levitação externa forte, externa de potência0 mas longa, teto durante uma lease inteira, fim natural/interrompido e ausência de controle próprio residual. Não confundir efeito externo preservado com vazamento da habilidade.
- Prisão B continua independente e funcional. Kit-base, combo Better Combat e dados novos Quincy continuam preservados.

Depois dos testes focados: check/build e suíte pertinente; ampliar regressões quando integração justificar. Execute um cliente Forge pesado por vez. Para QA real use mundo descartável, fase realmente acionada pelo servidor, não visual preview forçado. Capture longe (campo completo), perto (alvo preso e teto) e a sequência inteira da flor/prisão/fade. Inspecione as capturas: log de emissão não comprova que o tapete permaneceu visível; quadros isolados não comprovam fluidez contínua.

## Fase4 — entrega auditável, sem regressão de coordenação

Atualize este documento com bloco curto de resultado, sem apagar o contrato. Documentação só em Docs/poderes; não editar continuidade/roadmap compartilhados em paralelo. Retenha poucos frames representativos e um manifesto com origem/tempos/SHAs, evitando milhares de capturas ou vídeos no Git.

Entregue: branch e caminho, SHA base e merge de entrada, diff exclusivo, causa demonstrada, comportamento implementado, comandos/exit codes, falhas anteriores e resultado final, imagens reais e limitações. Distinga implementado, compilado, testado, visualmente observado e aceitação humana pendente. Build verde não aprova estética.

Commit focado da sua entrega na branch individual, excluindo logs e arquivos alheios. Não mergear/pushar poderes ou main. Entregar SHA para revisão Codex e teste humano; se houver autorização humana posterior específica, respeitá-la. Não declarar Garden concluído enquanto houver somente especificação ou VFX sem comportamento.
