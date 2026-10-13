> **Estado vigente — ciclo 33, 13/10/2026:** stamina máxima por Resistência com retornos decrescentes e sem recarga por compra; bainha da arma vinculada e saque/guarda cosméticos nativos. [Fórmula, arquivos, sincronização, limites e roteiro](33-progressao-stamina-bainhas-saque-2026-10-13.md). Schema 7; protocolo 2.9. Visual e balanceamento aguardam playtest; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 32, 13/10/2026:** ciclo 31 e estado de combate homologados pelo usuário. Golpes básicos de zanpakutō gastam até 4 stamina por tentativa; técnicas preservam custos de reiatsu. As três espadas são indestrutíveis por decisão do usuário. [Contrato, arquivos, lógica e testes](32-ataques-basicos-zanpakutos-indestrutiveis-2026-10-13.md). Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 31, 13/10/2026:** stamina de corrida com pausa, recuperação, exaustão e HUD. Valores provisórios; ciclo 30 segue em teste visual. [Contrato, arquivos, lógica e roteiro](31-stamina-corrida-2026-10-13.md). Schema 6; protocolo 2.8. Registros anteriores abaixo são históricos.

# Ciclo 30 — Estado automático de combate

Data: 12/10/2026. Branch: Feature-Poderes-bankais. Primeira fatia aprovada pelo usuário a partir dos contratos M05-02B e M06-01B/C.

## Entrega

Estado interno de combate automático no servidor e indicação discreta abaixo do painel principal de recursos. Não implementa Combat Mode manual ou Combat Hotbar; inventário e teclas permanecem. Não muda regeneração, stamina, atributos, custos, poderes ou maestria. M05/M06 deixam tempos e fórmulas abertos: 200 ticks (dez segundos nominais) é um valor de protótipo para esta entrega, não balanceamento definitivo.

## Arquivos e lógica, passo a passo

1. `src/main/java/com/bleachmod/common/CombatStateService.java`: observa LivingDamageEvent em prioridade LOWEST. Só considera quantidade finita positiva, evento não cancelado e fonte com entidade viva responsável na mesma dimensão. Ignora autoataque, aliados e ArmorStand. Dano direto ou projétil atribuído a entidade viva pode ativar o estado. Não depende de arma ou identidade Shinigami específica.
2. Marca atacante/vítima que forem ServerPlayer vivos, não espectadores e com personagem criado. PvP pode marcar ambos; PvE marca o jogador participante. Novo dano renova 200 ticks. Invocar uma técnica sem dano, controlar sem dano ou apenas aproximar-se de um inimigo não ativa esta primeira versão. Dano ambiental sem autor vivo (queda, lava, fome) não ativa; fogo residual sem autoria identificável não atribui combate artificialmente.
3. `src/main/java/com/bleachmod/common/data/StatusData.java`: contador transitório e snapshot independente para HUD. tickTransientState, já chamado pelo TickHandler, reduz o contador a cada tick válido até zero. clearTransientState e load limpam o estado; save não grava o contador. Sem mapa estático, jogador retido ou dados de adversário persistidos.
4. `techniqueHud()` inclui combatTicks, distribuído pelos PlayerSyncS2C/ResourceSyncS2C existentes. `readTechniqueHud` aplica snapshot limitado, sem escrever estado de combate do servidor. Envios imediatos ao receber dano e atualização periódica existente a cada quatro ticks permitem feedback de entrada e saída. O cliente não envia comando para marcar/desmarcar combate.
5. `src/main/java/com/bleachmod/client/hud/ReiatsuHud.java`: mostra “Em combate” sob o painel enquanto o snapshot indica estado ativo; ocultar HUD ou ausência de personagem mantém as regras anteriores. Traduções pt_br/en_us adicionadas; sem asset novo.
6. `src/main/java/com/bleachmod/Reference.java`: protocolo 2.7 para exigir builds compatíveis de cliente/servidor. Nenhum ID de pacote foi adicionado/reordenado. Schema persistido do jogador não muda.
7. Morte limpa e sincroniza pelo serviço. Clone/respawn/dimensão/logout/login aproveitam limpeza transitória existente. Reinício/reconexão não restauram um timer salvo. Encerrar mundo/servidor não deixa estado em cache global. Trocar forma ou zanpakuto durante combate não é um mecanismo de fuga desta primeira fatia.
8. `src/test/java/com/bleachmod/MvpRegressionTest.java`: expiração, renovação, snapshot, ausência do contador no save/load e limpeza transitória.
9. `src/main/java/com/bleachmod/gametest/CombatStateGameTests.java`: eventos de PvP/PvE, exclusão de eventos inválidos, morte, dimensão, logout/login e preservação de dados do personagem. FakePlayer não aceita dano vanilla normalmente; alguns cenários disparam o evento aceito diretamente para validar o contrato do observador.

O observador registra o estágio final do evento Forge de dano, antes da aplicação à vida. Não modifica valor ou cancelamento. Outra extensão que cancele dano depois desse observador deve ser considerada ao testar compatibilidade com mods externos; esta entrega valida a base local do projeto.

## Roteiro de homologação

1. Inicie com cliente/servidor atualizados; personagem criado. Antes de atacar, indicador deve estar ausente.
2. Ataque um zumbi com golpe que cause dano: indicador aparece. Pare a luta/afaste-se por dez segundos sem causar/receber dano: indicador desaparece, com atualização de HUD de até quatro ticks.
3. Receba ataque de um mob e repita; novos golpes antes do término devem renovar o intervalo. Golpe no vazio e dano de queda não devem iniciar combate em personagem fora de combate.
4. Com segundo jogador, teste PvP permitido: ambos veem o próprio estado. Teste equipes/aliados segundo as regras atuais; não deve criar estado pelo evento ignorado entre aliados.
5. Durante combate, morra/renasça, saia/reconecte e troque dimensão. Estado anterior deve estar limpo. Troque somente forma/zanpakuto: combate continua até ausência de conflito.
6. Confira posições do indicador em escalas/resoluções diferentes; HUD oculto não mostra o texto. Não deve mover itens, selecionar técnica ou trocar interface automaticamente.
7. Reconfirme reiatsu, recuperação, treino de maestria e habilidades: todos conservam comportamento anterior. Modo de teste gratuito não torna o jogador fora de combate; apenas conserva suas regras anteriores de custo/treino.

## Limites e continuidade

Esta fatia reconhece dano aceito, não aggro, aproximação ou controle sem dano. Não impede logout, viagem, inventário ou regen, nem aplica penalidade. Esses consumidores exigem uma próxima decisão explícita. Sem contador regressivo visível ou marcação de alvo no HUD. O timer usa ticks de servidor: com TPS baixo o tempo real pode aumentar.

Depois da homologação, especificar stamina e regeneração em pequenos incrementos, preservando a distinção entre Combat State automático e Combat Mode manual. A migração dos sete atributos atuais para os cinco do design continua separada. Aizen/narrativa/novos kits não são dependências desta entrega. Documentos fora de Docs/poderes permanecem intactos.

## Validação

check/build/runGameTestServer offline aprovados na versão final: 68 regressões e 57 GameTests obrigatórios. Log local: build/cycle30-validation.log. git diff --check aprovado. JAR atualizado: build/libs/bleachmod-0.2.0.jar. Documentação restrita a Docs/poderes; modelos/texturas e outros documentos intactos. Protocolo 2.7 em ambos os lados. Homologação de HUD/multiplayer real permanece com o usuário.
