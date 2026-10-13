> **Estado vigente — ciclo 33, 13/10/2026:** stamina máxima por Resistência com retornos decrescentes e sem recarga por compra; bainha da arma vinculada e saque/guarda cosméticos nativos. [Fórmula, arquivos, sincronização, limites e roteiro](33-progressao-stamina-bainhas-saque-2026-10-13.md). Schema 7; protocolo 2.9. Visual e balanceamento aguardam playtest; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 32, 13/10/2026:** ciclo 31 e estado de combate homologados pelo usuário. Golpes básicos de zanpakutō gastam até 4 stamina por tentativa; técnicas preservam custos de reiatsu. As três espadas são indestrutíveis por decisão do usuário. [Contrato, arquivos, lógica e testes](32-ataques-basicos-zanpakutos-indestrutiveis-2026-10-13.md). Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 31, 13/10/2026:** stamina de corrida com pausa, recuperação, exaustão e HUD. Valores provisórios; ciclo 30 segue em teste visual. [Contrato, arquivos, lógica e roteiro](31-stamina-corrida-2026-10-13.md). Schema 6; protocolo 2.8. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 30, 12/10/2026:** primeira fatia de Combat State automático, indicador no HUD e limpeza transitória, sem mudar regeneração/stamina/atributos. [Lógica, arquivos, timeout provisório e testes](30-estado-combate-2026-10-12.md). Protocolo 2.7; schema 5 preservado. Combat Mode/hotbar seguem futuros; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 29, 12/10/2026:** ciclos anteriores homologados pelo usuário. Definições comuns dos 24 slots/formas de Ryujin e Hyōrinmaru consultadas por execução, acesso e HUD; valores preservados. [Arquivos, lógica e validação](29-definicoes-compartilhadas-2026-10-12.md) · [Contrato para novos poderes](contrato-criacao-novos-poderes.md). Registros anteriores abaixo são históricos; nova feature será discutida antes de outro kit.

> **Estado vigente — ciclo 28, 11/10/2026:** feedback de maestria sem arredondar limiares, avisos de novas liberações pelo treino, distinção de descoberta/compra/disponibilidade e indicação de treino suspenso em modo teste. [Arquivos, lógica e roteiros de visuais/maestria](28-feedback-maestria-roteiro-2026-10-11.md). Ganhos e requisitos preservados; ciclos abaixo são históricos.

> **Estado vigente — ciclo 27, 11/10/2026:** cortes, barreira, Zona Glacial, impacto de controle e trilha do dragão da Hyōrinmaru usam o emissor comum, mantendo aparência e combate. [Arquivos, lógica, valores e testes](27-migracao-slots-gelo-2026-10-11.md). Clima e terreno conservam sua lógica; registros abaixo são históricos.

> **Estado vigente — ciclo 26, 11/10/2026:** aura e liberação da Hyōrinmaru integradas ao emissor comum com audiência próxima e orçamentos explícitos, preservando a aparência aprovada. Bancada continua privada. [Arquivos, lógica, limites e roteiro](26-integracao-gelo-emissor-comum-2026-10-11.md). Os ciclos anteriores abaixo são históricos; Aizen e narrativa permanecem futuros.

> **Estado vigente — ciclo 25, 11/10/2026:** biblioteca comum com paletas fire/ice/reiatsu e formas aura/cut/beam/release; camadas compartilham orçamento. Bancada anterior preservada; nenhuma mudança nos kits homologados. [API, arquivos, limites e comparação](25-biblioteca-paletas-camadas-2026-10-11.md). Escolha visual e integração em produção são próximos gates; Aizen e narrativa continuam futuros.

> **Estado vigente — ciclo 24, 11/10/2026:** Asauchi/maestria do ciclo 23 homologadas pelo usuário. Catálogo vanilla e bancada administrativa de seis presets, três intensidades e duração limitada, sem alterações nos kits ou terreno. [Recursos, arquivos, lógica, limites e roteiro](24-catalogo-vanilla-bancada-visual-2026-10-11.md). Aizen e aquisição narrativa continuam futuros.

> **Estado vigente — ciclo 23, 07/10/2026:** ciclo 22 homologado pelo usuário. Asauchi independente com selada/Shikai/Bankai, mantendo todas as texturas/modelos e apenas bônus de dano nas liberações; treino válido por forma/técnica e desbloqueios graduais dos kits. Personagens antigos preservam repertório. Aizen permanece futuro. [Contrato operacional, lógica, arquivos, migração e roteiro de testes](23-asauchi-maestria-desbloqueios-2026-10-07.md). As seções abaixo registram os ciclos anteriores; propostas de história continuam abertas.
# Ciclo 22 — sincronização do clima e dragão Bankai ampliado

Data: 07/10/2026. Branch exclusiva: `Feature-Poderes-bankais`. Base: `47d9271`.

O usuário homologou os outros pontos do ciclo 21, mas relatou que, após encerrar Bankai, a neve parava e a chuva continuava no cliente, mesmo usando `/weather clear`. Pediu também aumentar o modelo e o alcance do Ice Dragon da Bankai. Este ciclo atende esses dois ajustes finais; aguarda confirmação visual no cliente antes de declarar encerramento definitivo da Hyōrinmaru.

## 1. Causa da chuva persistente

A revisão anterior verificava flags e temporizadores no servidor. Não verificava os pacotes enviados ao cliente. Em `Level.setRainLevel` e `setThunderLevel`, o Minecraft escreve simultaneamente a intensidade atual e a anterior. `ServerLevel.advanceWeatherCycle` só envia os pacotes usuais quando detecta diferença nessas intensidades ou mudança de estado observável durante seu tick. Ao restaurar tudo diretamente, esse tick podia encontrar o servidor já limpo sem detectar uma transição. O cliente ficava com sua última intensidade de chuva; outro comando clear também podia não produzir transição, porque o servidor já estava seco.

Essa causa foi confirmada pela inspeção do bytecode das classes locais da versão 1.20.1. O controle de proprietários, restauração de snapshot e prioridade para comandos do ciclo 21 continuam vigentes; a correção faltante é a sincronização explícita do estado visual.

## 2. Correção e lógica do clima

1. `src/main/java/com/bleachmod/common/technique/BlizzardWeatherData.java`: após restaurar snapshot, `syncWeather` transmite três pacotes vanilla para os jogadores da mesma dimensão: `START_RAINING` ou `STOP_RAINING`, `RAIN_LEVEL_CHANGE` e `THUNDER_LEVEL_CHANGE` com os valores restaurados.
2. O evento de comando marca `commandSyncPending`, e o tick da dimensão aplica a sincronização depois da execução do comando. Se a chuva foi desligada, as intensidades de chuva/trovão são zeradas antes de transmitir os pacotes. Assim, clear também recupera um cliente que ficou com chuva visual presa em builds anteriores, mesmo sem tempestade gerenciada ativa.
3. O parser percorre contextos filhos para reconhecer tanto `/weather clear` direto quanto `/execute ... run weather clear`; também admite a raiz `minecraft:weather`. Mantém validação de permissão e parse completo antes de entregar o controle.
4. Não reaplica Bankai por cima do comando manual: os donos ativos continuam registrados até sair. Uma nova ativação após encerrar todas as concessões inicia um novo ciclo normalmente.
5. A intensidade de trovão é convertida do getter ponderado pela chuva para o valor bruto esperado pelos setters/pacotes vanilla. Isso preserva também uma tempestade parcialmente intensa anterior à Bankai, sem ponderação duplicada no cliente. O snapshot inclui `rawThunder`; o carregamento converte snapshots antigos para esse formato.
6. Não altera gamerules nem temporizadores de comandos além do comportamento vanilla. Preserva sobreposição de jogadores, recuperação de snapshot, restrições de dimensão/bioma e neve local anterior. A neve temporária da Zona Glacial continua independente.

## 3. Ice Dragon da Bankai

1. `src/main/java/com/bleachmod/common/technique/HyorinmaruTechniqueService.java`: alcance do corte/dragão frontal H da Bankai passa de 14 para 24 blocos. Dano base 12, abertura de 15 graus, custo 20, recarga 100 ticks, controle de frio e política de alvos permanecem.
2. `src/main/java/com/bleachmod/entity/IceDragonEntity.java`: parâmetro vanilla de tamanho do Phantom passa de quatro para doze exclusivamente na Bankai. O renderer existente usa esse parâmetro para ampliar o modelo e mantém a textura de gelo.
3. Duração visual Bankai passa de 20 para 30 ticks; velocidade passa de 0,7 para 0,8 bloco por tick. Com a origem a 1,5 bloco à frente, a animação percorre aproximadamente a distância do ataque ampliado antes de desaparecer. O dano continua resolvido no lançamento; a entidade é visual, não aplica outro dano ao atravessar o alvo.
4. Shikai mantém tamanho dois, duração 14 ticks, velocidade 0,7 e alcance dez. Modelo continua sem IA, gravidade, colisão, persistência ou possibilidade de seleção; troca de item/forma inválida, morte, dimensão e chunk descarregado continuam encerrando-o.

## 4. Testes de regressão específicos

`src/main/java/com/bleachmod/gametest/IceRefinementGameTests.java` recebe dois cenários:

- `restoringWeatherAndClearCommandSynchronizeConnectedClients`: insere temporariamente um jogador de teste na lista de destinatários e captura os pacotes enviados por sua conexão. Exige STOP_RAINING e intensidade zero de chuva/trovão após restauração, após clear em servidor já limpo e após clear dentro de execute. A lista pública de jogadores é somente leitura; apenas a montagem desse teste acessa a lista interna via reflexão. Remove o jogador e restaura sua conexão no fim. Também verifica a restauração de uma tempestade prévia com intensidades parciais e os valores brutos transmitidos. Isso cobre a falha que os testes anteriores de estado do servidor não detectavam.
- `bankaiDragonIsLargerReachesTwentyFourBlocksAndExpires`: alvo a 22 blocos atingido, alvo a 26 preservado, tamanho do modelo doze, avanço visual além de 21 blocos e desaparecimento ao final. Usa lote próprio para não interferir em outros testes de habilidades.

Validação final aprovada: `check build runGameTestServer --offline --console=plain`, 57 cenários de regressão e 35 GameTests obrigatórios. Artefato atualizado: `build/libs/bleachmod-0.2.0.jar` (11.913.134 bytes). Log local ignorado: `build/weather-client-dragon-validation-final.log`. Revisão de documentos: 266 links locais válidos. A aprovação automatizada cobre rede/estado e alcance; a aparência final continua sendo homologada pelo usuário no cliente.

## 5. Roteiro final no cliente

1. Feche e reinicie usando `Iniciar-Teste` para carregar a versão nova. No mundo de teste, use `/weather clear 100` uma vez; isso deve limpar inclusive chuva visual presa anteriormente.
2. Com tempo limpo, ative a Bankai da Hyōrinmaru, aguarde a tempestade e encerre com V. Conferir que neve, chuva, som de chuva e nuvens voltam ao estado anterior.
3. Ative novamente e execute `/weather clear 100` com a Bankai ainda ativa. A chuva deve limpar e não reiniciar automaticamente. Repetir com `/execute run weather clear 100` se desejar testar o caminho indireto.
4. Se havia chuva antes da transformação, é esperado restaurar aquela chuva ao encerrar; usar clear deve limpá-la normalmente.
5. Em Bankai, use H mirando mobs a aproximadamente 22 blocos e compare o dragão com Shikai: modelo maior e percurso mais longo. Um mob a 26 blocos não deve ser atingido pelo ataque inicial. Conferir desaparecimento automático do modelo.

Nenhum outro documento fora de `Docs/poderes`, modelo de espada, habilidade homologada, HUD ou arquivo do inicializador foi modificado por este ciclo.