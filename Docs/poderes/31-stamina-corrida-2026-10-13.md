> **Estado vigente — ciclo 32, 13/10/2026:** ciclo 31 e estado de combate homologados pelo usuário. Golpes básicos de zanpakutō gastam até 4 stamina por tentativa; técnicas preservam custos de reiatsu. As três espadas são indestrutíveis por decisão do usuário. [Contrato, arquivos, lógica e testes](32-ataques-basicos-zanpakutos-indestrutiveis-2026-10-13.md). Registros anteriores abaixo são históricos.

# Ciclo 31 — Stamina para corrida

Data: 13/10/2026. Branch: Feature-Poderes-bankais. Próxima fatia recomendada e implementada com autorização do usuário. O ciclo 30 de estado de combate está em teste visual pelo usuário.

## Origem e contrato

Base: Docs/game-design/05-atributos-e-recursos.md, seções de stamina, recuperação e exaustão; 06-habilidades-e-combate.md, separação entre recursos e ações futuras. Primeiro consumidor: sprint com deslocamento horizontal. Valores provisórios para playtest a 20 TPS: máximo 100, gasto 5/s, recuperação 20/s após 20 ticks sem esforço. Reserva cheia sustenta aproximadamente 20 segundos de corrida contínua. Ao chegar a zero, sprint é interrompido e só liberado novamente ao recuperar 10 pontos: cerca de 1,5 segundo contando a pausa. Caminhar continua disponível. Fome e regras vanilla de corrida permanecem aplicáveis.

Máximo fixo nesta primeira etapa; derivação de Resistência exige conciliar os atributos existentes com o contrato futuro. Esquiva, bloqueio, postura, Shunpo e hotbar continuam pendentes. Reiatsu, maestria, custos das técnicas e regeneração de reiatsu conservam as regras anteriores. Stamina recupera também em combate; ainda não há modificador por estado de combate.

## Arquivos e modificações, passo a passo

1. `common/StaminaPolicy.java`: centraliza máximo, gasto, recuperação, pausa e limiar de retomada. Tempos medidos em ticks; TPS baixo prolonga o tempo real.
2. `common/data/ResourcesData.java`: reserva, pausa restante e trava de exaustão; máquina de gasto/recuperação com limites. Grava e lê esses três campos NBT; sanitiza valores não finitos, negativos e fora do máximo. Nenhum gasto de reiatsu ou ganho de treino é feito aqui. O comando de custo gratuito de reiatsu não desativa stamina.
3. `common/data/StatusData.java`: amostra transitória da posição anterior, sem mapa global ou referências a jogadores. Detecta movimento horizontal maior que 0,01 bloco por tick; primeira amostra não cobra. Limpeza transitória e load descartam a posição, conservando o recurso persistido.
4. `common/StaminaService.java`: servidor calcula esforço somente com sprint e movimento; exclui criativo, voo e passageiros. Mortos/espectadores/personagens não criados não são processados. Exaustão encerra sprint no servidor.
5. `server/events/TickHandler.java`: chama o serviço no tick END válido; snapshots de recursos já existentes enviam stamina a cada quatro ticks. Não cria pacote novo.
6. `client/events/ClientForgeEvents.java`: usa snapshot de exaustão para interromper sprint local. Cliente não calcula nem altera a reserva autorizada do servidor.
7. `client/hud/ReiatsuHud.java` e traduções `pt_br.json`/`en_us.json`: indicador numérico e barra compacta abaixo do painel; exaustão muda a cor. Aviso de combate fica abaixo da barra. Nenhuma textura alterada.
8. `common/data/PlayerData.java`: schema 6 aditivo, novo personagem inicia cheio. Recursos antigos sem stamina iniciam com o padrão 100 em instância nova; preservação de técnicas legadas continua restrita a schema anterior a 5.
9. `server/events/CapabilityEvents.java`: respawn restaura 100. Clone/reconexão/dimensão preservam reserva e pausa, evitando recarga por reconectar; nenhum progresso offline. Formas não restauram stamina.
10. `Reference.java`: protocolo 2.8 para cliente/servidor compatíveis. `MvpRegressionTest.java` e `gametest/StaminaGameTests.java`: consumo até zero, pausa, limiar, recuperação limitada, NBT, legado, movimento real simulado, caminhada/voo, reconexão e respawn.

## Teste manual

1. Atualize cliente e servidor para a mesma build. Em sobrevivência com personagem criado e fome suficiente, confira Stamina 100/100 abaixo do HUD.
2. Caminhe e fique parado: reserva cheia não diminui. Corra continuamente em terreno plano por aproximadamente 20 segundos: barra diminui e sprint termina ao zerar.
3. Continue andando. Após um segundo inicia a recuperação; ao chegar a 10 pode correr novamente. Sem correr, retorna a 100. Segurar a tecla de sprint deve permitir nova corrida após o limiar.
4. Durante exaustão, confira caminhada e habilidades disponíveis conforme reiatsu/cooldown/maestria. Stamina não deve consumir reiatsu ou gerar maestria por correr.
5. Gaste parte da reserva, reconecte/troque dimensão: não ganha recarga completa. Morra e renasça: volta a 100. Reinicie o mundo para verificar persistência.
6. Teste criativo/voo, corrida parado contra parede e escalas de HUD. Ocultar HUD deve ocultar barra e indicador de combate. Em multiplayer, cada jogador tem sua própria reserva.
7. Reconfirme combate automático e poderes Ryujin/Hyōrinmaru/Asauchi. Homologação visual, movimento com latência e balanceamento dependem desse playtest.

## Validação e próximos passos

Validação: `gradlew check build runGameTestServer --offline --console=plain` concluído com sucesso; 70 cenários de regressão e todos os 59 GameTests obrigatórios aprovados. Log local: `build/cycle31-validation.log`. JAR: `build/libs/bleachmod-0.2.0.jar`. Nenhuma homologação visual presumida. Próxima decisão após testes: ajustar consumo/pausa/retomada, depois implementar esquiva física com consumo explícito; bloqueio/postura e migração de atributos em etapas separadas. Aizen permanece futuro.