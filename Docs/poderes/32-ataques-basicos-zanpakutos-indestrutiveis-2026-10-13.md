> **Estado vigente — ciclo 33, 13/10/2026:** stamina máxima por Resistência com retornos decrescentes e sem recarga por compra; bainha da arma vinculada e saque/guarda cosméticos nativos. [Fórmula, arquivos, sincronização, limites e roteiro](33-progressao-stamina-bainhas-saque-2026-10-13.md). Schema 7; protocolo 2.9. Visual e balanceamento aguardam playtest; registros anteriores abaixo são históricos.

# Ciclo 32 — Ataques básicos e zanpakutōs indestrutíveis

Data: 13/10/2026. Branch: Feature-Poderes-bankais. Usuário homologou stamina, estado automático de combate e limpeza; pediu gasto físico nos golpes de espada, técnicas somente com reiatsu e escolheu retirar durabilidade das zanpakutōs.

## Decisões e escopo

Asauchi, Ryujin Jakka e Hyōrinmaru são indestrutíveis em todas as formas. Texturas, modelos, identidade, encantamentos e atributos de ataque não mudam. NPC de reparo não é implementado; uma eventual manutenção narrativa fica para discussão futura.

Golpe básico manual contra alvo vivo custa até 4 pontos de stamina disponíveis, uma vez por tentativa. Valor provisório de balanceamento, definido para este playtest; reserva cheia comporta 25 tentativas sem recuperação. Consome esforço mesmo se a vítima bloquear, for invulnerável ou não receber dano por intervalo vanilla. Golpe no ar, mineração, mão vazia e armas vanilla não são novos consumidores nesta etapa. Criativo, espectador, evento cancelado, alvo morto, próprio jogador e aliados são excluídos. Nenhuma técnica especial recebe custo adicional de stamina.

Ataques básicos continuam possíveis a zero, mantendo o princípio de combate básico disponível na exaustão. Não muda dano, velocidade de ataque, fome, varredura ou cooldown vanilla. A restrição de sprint permanece até recuperar 10 pontos. Cada tentativa reinicia a pausa de 20 ticks antes da recuperação; insistir em atacar pode manter a corrida indisponível. Gastar menos de 4 restantes zera o recurso sem saldo negativo. O modo administrativo de reiatsu gratuito não isenta esforço físico.

Esta solicitação específica do usuário acrescenta custo aos golpes de zanpakutō nesta versão; o contrato geral M06-02I permite custo inexistente/desprezível em ataques triviais e deixa valores abertos. Não foi alterado o documento-base de game design.

## Arquivos, lógica e modificações

1. `common/StaminaPolicy.java`: acrescenta BASIC_SWORD_ATTACK = 4, mantendo máximo, corrida, pausa e recuperação homologados.
2. `common/data/ResourcesData.java`: spendBasicAttackStamina reduz a reserva com limite zero, renova a pausa e marca exaustão ao esgotar. Não recusa ataque nem modifica reiatsu/maestria. Reutiliza os campos persistidos no schema 6.
3. `common/BasicAttackStamina.java`: observador Forge AttackEntityEvent, prioridade LOWEST, servidor, somente três zanpakutōs na mão principal de personagem criado. Cobra tentativa manual uma vez e sincroniza recursos imediatamente. Interrompe sprint ao esgotar. Varredura de múltiplas vítimas não multiplica custo porque não observa dano de cada vítima.
4. A separação usa o ponto de entrada manual: técnicas de fogo/gelo frequentemente usam PLAYER_ATTACK, portanto cobrar LivingHurtEvent/LivingDamageEvent confundiria técnica com golpe básico. Essas técnicas executam hurt diretamente e não passam pelo novo observador de tentativa manual.
5. `common/item/ZanpakutoItem.java`: subclasse compartilhada de SwordItem com os mesmos atributos Tiers.IRON, bônus 3 e velocidade -2.4. Capacidade de dano zero, item não danificável, barra oculta e reparo por material desativado. A rotina vanilla de desgaste passa a ignorar essas espadas. IDs de registro permanecem.
6. `init/ModItems.java`: os três registros usam ZanpakutoItem; demais itens conservam implementação. Itens antigos carregam pelo mesmo ID e passam a ser indestrutíveis sem varrer inventários ou apagar tags de encantamento/modelo. Tag Damage antiga pode continuar no NBT, mas não representa desgaste ativo nem exibe barra.
7. `MvpRegressionTest.java`: gasto repetido, limite zero, esforço com reiatsu gratuito, pausa e recuperação independente de reiatsu.
8. `gametest/SwordEffortGameTests.java`: evento manual nas três armas, fonte PLAYER_ATTACK especial sem cobrança, básica disponível a zero, exclusões e itens antigos serializados submetidos a centenas de usos e desgaste forçado sem quebra. Espada de ferro vanilla continua danificável.

Não adiciona pacote, altera formato de rede ou schema. Protocolo continua 2.8, schema 6. Atualize cliente e servidor com a mesma build para comportamento e visual consistentes.

## Propósito do estado de combate

O ciclo 30 registra conflito no servidor e informa o HUD; não é um botão de Combat Mode. Dano atribuído a entidade viva marca participantes; dez segundos sem novo dano encerram. A limpeza já homologada evita manter estado antigo após morte/reconexão/dimensão. Atualmente não penaliza logout, inventário ou recuperação, nem bloqueia técnicas. Serve como base para regras futuras, por exemplo recuperação de reiatsu durante combate, postura e condições de técnicas. Essas regras ainda devem ser decididas e balanceadas; não foram ativadas por esta mudança. Stamina recupera também durante o estado de combate, após sua pausa de esforço.

## Roteiro de teste

1. Atualize a build e use sobrevivência com personagem criado. Teste Asauchi, Ryujin e Hyōrinmaru seladas, Shikai e Bankai: nenhum item deve mostrar barra de durabilidade.
2. Utilize uma espada antiga que já tinha desgaste. Golpeie mobs e quebre blocos repetidamente: item não quebra. Confira nome, encantamentos e aparência. Armas vanilla devem manter desgaste.
3. Ataque um mob com zanpakutō: stamina cai 4 por tentativa manual, sem gasto novo de reiatsu. Varredura/crit não multiplicam custo. Tente ataque bloqueado ou repetido no intervalo de dano: continua sendo esforço. Atacar no ar não cobra nesta etapa.
4. Gaste até zero: ataque básico continua permitido; sprint fica indisponível até recuperar 10. Pare de atacar/correr, confira pausa de um segundo e recuperação de 20/s.
5. Use cada slot de Ryujin e Hyōrinmaru nas formas liberadas: reserva de stamina não cai por ativação, impactos, criatura ou dano contínuo. Reiatsu, manutenção de forma e cooldowns continuam seus contratos anteriores. Evite correr/atacar manualmente durante a comparação para isolar o recurso.
6. Confira criativo, mãos vazias e espada vanilla sem cobrança nova. Confira que atacar com zanpakutō no modo de reiatsu gratuito ainda consome stamina.
7. Reconecte com stamina parcialmente gasta, confirme persistência e estado de combate limpo conforme ciclo anterior. Teste PvP e indicadores individuais em segundo cliente.

## Validação e continuidade

Validação: `gradlew check build runGameTestServer --offline --console=plain` concluído com sucesso. 71 cenários de regressão e todos os 62 GameTests obrigatórios aprovados; log local `build/cycle32-validation.log`. Build gerada em `build/libs/bleachmod-0.2.0.jar`. Homologação visual e balanceamento dependem do teste do usuário. Após essa validação, a recomendação continua esquiva curta com consumo de stamina e colisão real; bloqueio/postura em etapa separada. Aizen, reparador e novas bankais continuam futuros.