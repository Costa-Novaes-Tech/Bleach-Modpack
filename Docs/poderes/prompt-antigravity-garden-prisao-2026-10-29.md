# Antigravity — Garden N com prisão de pétalas

Regra de entrada obrigatória: após ler tudo, executar git fetch origin, comparar sua branch com a própria origin, origin/main e origin/Feature-Poderes-bankais e, com trabalho local preservado, executar git merge --no-ff origin/Feature-Poderes-bankais NA branch individual. Se já integrada, comprovar por ancestralidade. Implementar somente depois desse passo; nunca inverter o sentido do merge.

Leia integralmente este prompt, antigravity-frentes-2026-10-29.md e skills/bleach-antigravity-entrega/SKILL.md em Docs/poderes. Primeiro compare sua branch/origin/poderes/main e faça o merge de origin/Feature-Poderes-bankais na branch individual; só depois implemente. Branch exclusiva codex/senbon-garden-prisao, checkout separado existente. Não está sozinho: Codex está substituindo o combo melee por animações Better Combat prontas; NÃO editar weapon_attributes, clips, gerador do combo ou BetterCombatCompat.

## Contrato aprovado

Manter Garden N Bankai com centro fixo da primeira explosão, raio35blocos, primeira explosão e janela de reativação2–6s. A segunda explosão continua formando flor e elevando pétalas, mas agora aprisiona os inimigos na área: envolve cada alvo em pétalas, causa levitação e dano contínuo. Não exigir mirar diretamente em alvos. Não confundir prisão ampla com mover o campo junto do jogador.

## Fase1 — estado e comportamento

Leia SenbonzakuraTechniqueService.tickField/finishField/tickPrison, SenbonPolicy, FLORAL_BLOOM e geometria/VFX existentes. finishField hoje aplica14HP de uma vez e remove campo. Criar fase de prisão própria após segunda explosão, independente da janela de reativação. V0:3s, três pulsos espaçados1s; repartir orçamento atual14HP entre explosão/pulsos, sem aumentar silenciosamente dano total. Documentar valores e momento exato dos pulsos.

Capturar alvos válidos na explosão e acompanhar por UUID, sem admitir continuamente novos alvos não atingidos. Reavaliar morte/remoção/aliança/dimensão e contenção. Propor volume vertical explícito coerente com terreno e testar: esfera/coluna infinita não é aceitável. Sem dano através de parede. Não copiar cegamente owner.hasLineOfSight da Convergência, pois olhar/mover o conjurador não deve desligar prisão no centro fixo.

Levitação com lease curto e limite de altura/checagem de teto, sem teleporte através de blocos ou subida infinita. Usar mecanismo existente de efeitos de forma segura: não apagar/substituir permanentemente levitação de outra origem ao terminar. Sem puxar aliados, espectadores ou caster. Definir saída do volume, limite de alvos e custo por tick. Encerrar por fim/morte/logout/troca de dimensão/kit conforme lifecycle do serviço. Não aplicar dano novo ao limpar.

## Fase2 — visual

Manter tapete de pétalas no interior, bordas da flor e subida da explosão. Cada alvo recebe espiral/envoltório legível; não substituir todo campo por um anel vazio nem cobrir tela com densidade excessiva. Em multidão limitar emissão por distância/qualidade e não o contrato de dano sem declarar. Não reabrir cinematográfica ou melee.

## Fase3 — QA

Alvos centro/borda/fora, em cotas distintas, parede/teto, aliado/caster/espectador, múltiplos inimigos; dono olhando para outro lado e caminhando longe sem mover centro. Testar reativação antes2s/dentro2–6s/depois6s, repetição/spam, fim natural e interrupções. Verificar dano total e três pulsos sem burlar hurtTime, ausência de levitação residual e preservação de efeito externo mais forte.

Forge real: visão de longe do campo inteiro e perto do alvo, primeira explosão, flor e prisão de3s; mostrar que interior preserva pétalas. Contagem de testes não aprova estética. Entregar SHA base/merge, diff restrito, testes/exit codes e capturas compactas. Sem merge final na poderes/main e sem editar docs de continuidade compartilhadas em paralelo.

## Parecer sobre a especificação apresentada29/10

Correto respeitar os dois checkouts ocupados; não criar terceiro worktree. A frente ainda é planejamento, sem implementação comprovada. Mesmo com checkout Quincy limpo/commitado, confirmar liberação pela conversa responsável antes de trocar sua branch: Quincy tem correções de QA solicitadas pelo Codex. Aguardar sequencialmente enquanto essa frente o utilizar.

Orçamento5HP na flor +3pulsos de3HP é uma V0 coerente para14HP brutos, não garantia de14HP líquidos contra defesas/cancelamentos. Cuidados obrigatórios antes de aplicar:

- Pulso emt60 precisa acontecer ANTES da limpeza port>=60; testar t59/t60/t61 e exatamente três pulsos. Não perder o último pelo early return de expiração.
- Fase de prisão não pode morrer na expiração da janela2–6s: estado próprio, inclusive se a flor for reativada perto do sexto segundo. Preservar visual do piso durante essa fase.
- Lease8ticks continua acelerando a entidade depois de aplicado. Zerar velocidade apenas um tick no teto não impede a levitação no seguinte. Testar teto baixo e limite de altura durante toda a lease, sem remover levitação de outra fonte. Identificar efeitos de igual potência, duração maior ou origem externa; testar só amplifier>0 não resolve preservação de efeitos externos amplifier0.
- Medir altura relativa ao piso válido de cada alvo, não só Y do conjurador/centro. Centro fixo e clearLine devem funcionar em desnível, sem enterrar origem/fechar linha arbitrariamente. Declarar o volumevertical5blocos como parâmetro V0 e testá-lo, sem confundir raio35 horizontal com alcancevertical.
- Os limites MAX_TARGETS, custo de busca e orçamento VFX precisam ser explícitos. Distinguir alvos elegíveis não capturados por teto de desempenho dos atingidos; não prometer todos ilimitadamente.

Ao liberar checkout, atualizar refs e integrar origin/Feature-Poderes-bankais ANTES de implementar. Não apenas iniciar do antigo2b3c582: já há novas entregas Senbon/Higashi e revisão de roadmap. Manter os mesmos arquivos exclusivos e não reabrir combo da katana.
