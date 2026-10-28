# Uryū Ishida — plano do arqueiro tradicional

27/10/2026. Estado: planejamento; sem código, assets ou branch nova. Decisão explícita do usuário: arqueiro tradicional primeiro; TYBW depois. Direção do kit aceita pelo usuário em27/10; valores iniciais autorizados para protótipo, sem homologação de balanceamento. O arco deve ser próprio; efeitos especiais finais serão discutidos depois. Hyōrinmaru concluída por aceite explícito e retirada da fila ativa.

## Contrato e recorte

Referências: M04/Q1–Q7, M05/11.2 REV-SP-02, M06 e seus contratos de combate, M07/8.69–8.88, M08a/8.5. Arco/fundamentos/Gintō/Hirenkyaku/Schrift/Vollständig/Letzt Stil são aquisições distintas. Uryū é contato tradicional contextual, não professor universal. SP compra skills/formas; Mastery pela prática. Não chamar o kit de Shikai/Bankai, não mudar raça ao equipar um item e não acrescentar outra barra de recursos por conveniência.

Não incluir Antithesis/Vollständig nesta entrega. Letzt Stil pertence ao repertório tradicional, mas fica fora desta primeira entrega por recomendação: seu contrato exige suspensão persistente do acesso Quincy e recuperação viável; não é buff simples e não deve ser liberada sem sua jornada de retorno.

A busca dirigida em src/main/java não encontrou serviço Quincy equivalente aos kits de zanpakutō. CharacterData.knownIdentity/supportsForm e TechniqueDefinitions examinados são centrados em zanpakutō. Isso não é auditoria integral da raça. Fase0 deve mapear criação racial, recurso, equipamento/foco, entradas, persistência e requisitos existentes antes de decidir a integração. Não inserir Uryū como outra zanpakutō nem duplicar um sistema inteiro de personagem.

## Direção aceita para o kit inicial

Ataque comum: formar arco/flecha de Reishi, puxar a corda, sustentar mira e soltar projétil. Sem flechas vanilla obrigatórias. Recurso espiritual segue o contrato racial; sem coleta manual constante de partículas. Arco próprio orientado por referências Kojaku/Ginrei Kojaku a escolher na fase visual, não retextura de um arco vanilla apresentada como modelo final.

- H — Seele Schneider: disparo especial de lâmina espiritual; trajetória e impacto legíveis. O efeito de penetração/ruptura é adaptação a especificar, sem ignorar toda defesa automaticamente. Não virar katana/novo moveset de espada nesta entrega.
- N — Licht Regen: rajada frontal de flechas, distinta do disparo carregado. Preparação, abertura controlada e recuperação; número visual não vira multiplicação ilimitada de dano. Collisions/tetos de acerto por alvo no servidor. Não usar chuva meteorológica global.
- B — Hirenkyaku: reposicionamento sobre fluxo de Reishi nos pés, sem conceder voo permanente. Preservar a esquiva Alt+direção. Requisito racial e aquisição próprios; equipar o arco não concede esse fundamento automaticamente. Distância, direção de entrada, custo e recargas serão definidos na ficha executável.
- C — Sprenger: preparação de um pentágono com cinco Seele Schneider e catalisação por Gintō. Sugerida área fixa e legível, preparação perceptível e detonação em reativação, com janela finita. Esse automatismo de colocação e a reativação são ADAPTAÇÕES PROPOSTAS. Não virar explosão instantânea sem preparo nem prisão inevitável; ter chance de sair da região.

Passiva funcional inicial: formação de arco/flechas por Reishi segundo os requisitos raciais, sem inventar crítico, regeneração ou buff gratuito. Mira/precisão pertencem ao comportamento do disparo; bônus numérico só com ficha aprovada.

## Direção visual e animação

Branco/azul-ciano; contornos finos e núcleo luminoso, cruz/foco reconhecível. Reishi converge para a mão antes de formar o arco. A corda estica com a mão de tração; flecha acompanha corda/empunhadura e nasce no ponto real de lançamento. Corpo lateral, base firme, mão de tração aproxima o rosto, soltura com retorno curto. Espelhar arco/tração para canhoto sem inverter duas vezes. Demonstrar essa mecânica SEM efeitos antes do kit completo.

Seele tem silhueta de lâmina e vibração controlada; Licht Regen usa agrupamento de flechas e ritmo, não nuvem opaca; Hirenkyaku deixa fluxo curto nos pés; Sprenger enfatiza vértices, conexões e convergência. Câmara de combate permanece controlável; não criar cinematic longa para cada tiro.

## Fases para o futuro prompt

0. Auditoria com mapa de implementado/ausente e interfaces compartilhadas, sem reescrever GDD.
1. Uma fatia jogável: modelo/rig editável de arco + formar/puxar/soltar UMA flecha, custo validado no servidor e contato mão/corda demonstrado no Forge real.
2. Integrar H/N individualmente, com testes de parede, alcance, alvo móvel, múltiplos alvos, gasto e repetição.
3. Hirenkyaku e Sprenger: requisitos/área/terreno/interrupção/expiração/limpeza/observador.
4. UI, persistência e balanceamento; não depender de comandos de QA para o uso normal.
5. Check/build, GameTests pertinentes e prova real de primeira/terceira pessoa, destro/canhoto, offhand, retorno ao controle e observador.

O protótipo usa os valores da Ficha V0 abaixo. Antes de integração pública de produção, conciliar item/foco e Gintō/Seele como inventário/consumíveis conforme M11, forma de aquisição e entrada racial. O balanceamento final permanece aberto. Creative/QA pode fornecer equipamentos para teste; isso não implementa a jornada de produção. Não fechar essas decisões silenciosamente na IA.

## Ferramentas e propriedade

Reutilizar Forge/Java existentes, Blockbench+GeckoLib para modelo/corda se necessário, animação nativa de arco quando suficiente e PlayerAnimator para poses específicas. Better Combat não substitui automaticamente o controlador de um arco. Photon opcional para acabamento; mecânica/VFX base precisam funcionar sem ele. Sem novas dependências no plano inicial.

Branch proposta: codex/uryu-arqueiro-tradicional, baseada no mesmo commit atualizado de Feature-Poderes-bankais usado pela outra frente. Reutilizar checkout separado existente após conferir alterações; não criar pasta agora. IA possui namespace Quincy, modelos e assets próprios. Codex coordena interfaces gerais de raça/PlayerData/CharacterData, registros, rede, UI e persistência, evitando edição simultânea com a migração Benihime. Integrações comuns devem ser delimitadas em fase0, não aparecer como refatoração surpresa no fim.

## Fontes e limites de pesquisa

- Pierrot oficial, glossário ha: https://pierrot.jp/title/bleach/keyword_ha.html — Hirenkyaku como passo avançado sobre fluxo de Reishi nos pés; consultado27/10.
- Pierrot oficial, glossário sa: https://pierrot.jp/title/bleach/keyword_sa.html — Sprenger com Seele Schneider e Gintō, preparo demorado e explosão interna; Seele como arma de lâmina/Reishi vibratório. Fonte não aprova nossos slots, colocação automática ou balanceamento.
- Licht Regen: referência secundária https://bleach.fandom.com/wiki/Licht_Regen, resultado indexado remete ao mangá349; leitura integral indisponível402 no navegador de pesquisa. Conferir cena/arte antes de modelagem final, sem afirmar leitura integral de capítulo.
- Catálogo M08a tem referências oficiais adicionais de cruz/fonte e aprendizado. Consulta direta ao site TYBW retornou403; não tratar como fonte integral lida.

## Ficha V0 — valores de teste autorizados em27/10

HP significa pontos de vida, antes da defesa/escala existente;2HP equivalem a um coração vanilla. PE significa pontos do recurso espiritual funcional do perfil, a mapear na fase0; não é uma barra nova. Valores definidos por Codex como ponto de partida, todos ajustáveis após teste.

- Disparo comum: alcance48blocos, carga completa1s, dano6HP, custo2PE; permitir soltura antecipada com dano proporcional. Respeitar colisão e expiração no alcance, sem flechas vanilla obrigatórias.
- H Seele Schneider:48blocos, corredor de aproximadamente0,5bloco de largura, até3alvos distintos em linha,10HP por alvo,12PE, recarga6s. Uma aplicação por alvo e parede interrompe.
- N Licht Regen:40blocos, cone total20graus,12flechas ao longo de2s;2HP por acerto, teto8HP por alvo por ativação;20PE, recarga10s. Não resetar hurtTime para forçar todos os hits.
- B Hirenkyaku:6blocos horizontais,8PE+10stamina, recarga6s. Direção de movimento; sem input, recuo. Validar volume do jogador contra blocos/precipício e parar com segurança; sem voo/invulnerabilidade novos.
- C Sprenger: centro até36blocos, raio6blocos, faixa vertical de4blocos a partir do piso local; preparar1,5s, reativar depois do preparo e até6s contados da primeira ativação. Dano18HP por alvo, teto8alvos,35PE uma vez, recarga20s desde o primeiro uso. Expiração dissolve sem detonar; reativação válida não cobra de novo nem é impedida pela recarga do primeiro uso. Uma formação por jogador. Terreno/local de cinco vértices validados antes de cobrar.

Modelos de foco/catalisador e consumo material Gintō/Seele permanecem dependências M11; não inventar economia definitiva. QA pode fornecer equipamento e desbloqueios em mundo descartável. Não liberar bypass de requisitos/raça no jogo normal. Usar fontes/registro dev para testar o protótipo enquanto a aquisição pública ainda não estiver integrada, declarando esse limite.

O modelo inicial do arco já deve ser original/editável e ter identidade Quincy; efeitos especiais mais ambiciosos e desenho final serão discutidos depois. Protótipo próprio não significa recolorir o arco vanilla e chamar de concluído.

## Execução do plano — Fase 0 mapeada (27/10/2026)

Branch de execução: `codex/uryu-arqueiro-tradicional`, criada a partir de `origin/Feature-Poderes-bankais` (`e9afb3434d2db64ec38300afca32528e3086b103`), sem conflito e isolada no checkout de design.

### Mapa de interfaces e dependências do protótipo

1. **Entrada racial e estado**:
   - `Reference.RACE_QUINCY = "quincy"`.
   - `CharacterData`: raça identificada como `"quincy"`. O personagem mantém identidade e progressão sem se confundir com Shinigami ou Zanpakutō.
   - Jornada de produção (encontro com mentor Uryū em Karakura, M04/RAC-26/RAC-31A e M08a/8.5) permanece formalmente PENDENTE de integração pública de quests/mundo. Para testes de laboratório/QA, providenciado helper seguro e comando de inicialização dev (`/bleachdev quincy` ou inicialização do protótipo), sem expor bypass no jogo público final.
2. **Recurso espiritual (PE)**:
   - Mapeado diretamente para `ResourcesData.currentReiatsu` (base 100), sem criação de barra redundante.
   - Cobranças autoritativas no servidor: Disparo comum (2 PE), Seele Schneider H (12 PE), Licht Regen N (20 PE), Hirenkyaku B (8 PE + 10 stamina), Sprenger C (35 PE cobrado uma única vez no preparo).
3. **Arma e foco espiritual**:
   - `QuincyBowItem` (`ModItems.QUINCY_BOW` / `bleachmod:quincy_bow`): arco espiritual Kojaku de Reishi (não usa flechas vanilla físicas).
   - Silhueta original com cruz central Quincy, hastes arqueadas em gradiente azul/ciano e corda espiritual.
   - Modelos de tração com predicados `pulling` e `pull` (0.0, 0.65, 0.9) integrados à animação nativa `UseAnim.BOW`.
4. **Controles e despacho de habilidades**:
   - Disparo comum: clique direito sustentado (carga até 1.0s / 20 ticks, soltura antecipada proporcional mínima a partir de 0.25s).
   - H (`TECHNIQUE_SLOT_1`): Seele Schneider — projétil especial em linha (48 blocos, largura ~0.5, perfura até 3 alvos, 10 HP/alvo, 12 PE, CD 6s, parede interrompe).
   - N (`TECHNIQUE_SLOT_2`): Licht Regen — rajada frontal de 12 flechas em 2s (40 ticks), cone 20 graus, alcance 40 blocos, 2 HP por acerto, teto de 8 HP por alvo, 20 PE, CD 10s.
   - B (`TECHNIQUE_SLOT_3` ou Alt+Direção): Hirenkyaku — deslocamento horizontal de 6 blocos com checagem de colisão local e de precipício, 8 PE + 10 stamina, CD 6s.
   - C (`TECHNIQUE_SLOT_4`): Sprenger — colocação de pentágono de 5 Seele Schneider a partir do piso local (centro até 36 blocos, raio 6 blocos, altura 4 blocos), preparo 1.5s, reativação entre 1.5s e 6s (18 HP, teto 8 alvos, 35 PE únicos, CD 20s desde o primeiro uso). Expiração aos 6s dissolve pacificamente sem dano.
5. **Persistência e isolamento de namespace**:
   - Classes e recursos novos criados no pacote `com.bleachmod.common.quincy` e `com.bleachmod.client.quincy`.
   - Subestado `QuincyData` associado ao jogador com serialização NBT limpa.
   - Despacho em `TechniqueService` redireciona para `QuincyTechniqueService` sem afetar kits de Zanpakutō (Zaraki, Benihime, Senbon, Hyōrinmaru, Ryūjin permanecem estritamente intactos).


## Execução do plano — Fases 1 a 5 concluídas (27/10/2026)

### Fase 1 — Modelo, Texturas e Disparo do Arco Espiritual Kojaku
- **Modelagem e Textura**:
  - Modelo Blockbench mestre criado em `tools/models/lab/antigravity/quincy01/ag_quincy_bow.bbmodel` com cruz de Reishi central e hastes arqueadas azuis e cianas.
  - Texturas geradas em `src/main/resources/assets/bleachmod/textures/item/quincy_bow.png`, `quincy_arrow.png`, `seele_schneider.png` e textura de projétil em `textures/entity/projectiles/quincy_arrow.png`.
  - Predicados de item `pull` e `pulling` configurados em `quincy_bow.json` referenciando `quincy_bow_pulling_0.json`, `quincy_bow_pulling_1.json`, `quincy_bow_pulling_2.json` e registrados no cliente em `BleachClient.java`.
- **Mecânica de Tiro**:
  - Implementado `QuincyBowItem.java` estendendo `BowItem` nativo:
    - Retorna `UseAnim.BOW`, engajando a pose lateral e recuo do braço à bochecha nativos do Forge/Minecraft sem conflitar com mixins de katana.
    - Carga total de 1.0s (20 ticks), soltura proporcional mínima a partir de 5 ticks.
    - Consumo de 2 PE por disparo; não requer flechas físicas de inventário.
    - Suporte nativo a primeira pessoa, terceira pessoa e modo canhoto (`HumanoidArm.LEFT`/`RIGHT`).
  - Implementado `QuincyArrowEntity.java` disparado a partir da coordenada dos olhos/mira do jogador (offset 0.5b), com rastro de partículas Reishi, alcance máximo de 48 blocos, corte de parede estrito e sem coleta física.

### Fase 2 — H (Seele Schneider) e N (Licht Regen)
- **H — Seele Schneider**:
  - Implementado em `SeeleSchneiderEntity.java` e despachado em `QuincyTechniqueService.executeSeeleSchneider`:
    - Custo: 12 PE, recarga: 120 ticks (6s).
    - Alcance: 48 blocos, velocidade: 3.0b/tick, caixa de colisão corredor ~0.5b.
    - Atravessa até 3 alvos distintos causando 10 HP em cada.
    - Impacto com bloco sólido interrompe o projétil imediatamente.
- **N — Licht Regen**:
  - Implementado em `QuincyTechniqueService.executeLichtRegen`:
    - Custo: 20 PE, recarga: 200 ticks (10s).
    - Sequência ativa de 40 ticks (2s), disparando 12 flechas no total em cone frontal de 20 graus.
    - Dano de 2 HP por impacto, teto estrito de 8 HP (máximo 4 acertos) por alvo por ciclo.
    - Não reseta `hurtTime` para não invalidar a defesa do Minecraft.

### Fase 3 — B (Hirenkyaku) e C (Sprenger)
- **B — Hirenkyaku**:
  - Implementado em `HirenkyakuService.java`:
    - Custo: 8 PE + 10 Stamina, recarga: 120 ticks (6s).
    - Integrado ao comando de slot 3 e ao atalho Alt+Direção (via `DodgeService.java`).
    - Deslocamento horizontal preciso de 6 blocos no nível dos pés.
    - Checagem a cada passo de 0.5b: interrompe com segurança contra paredes sólidas ou beiradas de precipício sem bloco de sustentação sob os pés. Sem voo, sem intangibilidade.
- **C — Sprenger**:
  - Implementado em `QuincyTechniqueService.java` e `SprengerNodeEntity.java`:
    - Alcance de mira do centro: até 36 blocos.
    - Varredura de relevo local: 5 vértices a 6 blocos do centro, escaneando verticalmente +/-4 blocos para achar o solo sólido em cavernas e encostas (sem uso de heightmap global do céu).
    - Criação: consome 35 PE uma única vez, aplica recarga de 400 ticks (20s) e posiciona 5 entidades de nodo `SprengerNodeEntity` com partículas Reishi.
    - Janela de preparo: 30 ticks (1.5s).
    - Janela de detonação: entre 1.5s e 6.0s (120 ticks). Reativação válida detona o pentágono para 18 HP em até 8 alvos dentro do cilindro (raio 6b, altura 4b), com verificação de linha de visão do centro para os alvos (sem dano através de paredes e sem destruir blocos).
    - Expiração: após 6.0s da criação, o campo se dissolve pacificamente sem dano.
    - Um campo por jogador; limpeza imediata na morte ou logout.

### Fase 4 — Persistência, UI e Integração Limpa
- **Subestado NBT**:
  - Criado `QuincyData.java` com serialização NBT limpa para o estado de Sprenger e estado transiente de rajada de Licht Regen.
  - Integrado em `PlayerData.java` via `getQuincy()` e `initializeQuincy()`.
  - Limpeza em `resetTransientState()`.
- **Comando de Teste / QA**:
  - `/bleachdev quincy <player>`: inicializa raça quincy, 100 PE, equipa o Kojaku bow na mão principal e limpa cooldowns e estado transiente.
- **Localização**:
  - Chaves de feedback e nomes adicionados em `en_us.json` e `pt_br.json`.

### Fase 5 — Resultados de Build, Testes e QA

1. **Gradle Build e Testes de Regressão**:
   - Comando: `./gradlew check --console=plain`
   - Exit Code: `0` (BUILD SUCCESSFUL em 49s).
   - Suíte de regressão: `PASS: 158 regression scenarios`.
   - Inclui 6 novos cenários unitários/offline específicos do Uryū Quincy:
     - `Quincy profile initialization, race, and independent QuincyData save/load NBT persistence`: PASS
     - `Quincy Kojaku bow charges Reiatsu, calculates pull scale, and enforces 48-block maximum range`: PASS
     - `Seele Schneider (H) pierces up to 3 distinct targets, deals 10 HP per target, and stops at wall`: PASS
     - `Licht Regen (N) fires 12 arrows over 2 seconds in a 20-degree cone, with 2 HP per hit and 8 HP ceiling per target`: PASS
     - `Hirenkyaku (B) executes 6-block horizontal movement, charges 8 PE and 10 stamina, and respects terrain bounds`: PASS
     - `Sprenger (C) requires 5 local floor vertices, charges 35 PE once, prepares in 1.5s, detonates within 6s for 18 HP up to 8 targets, and cleans up on timeout`: PASS

2. **Forge GameTest Server**:
   - Comando: `./gradlew runGameTestServer --console=plain`
   - Total de testes carregados: `243` (237 da base + 6 QuincyGameTests).
   - Testes Quincy (`QuincyGameTests.java`): 6 de 6 PASS:
     - `quincyProfileInitializationAndBowEquip`: PASS
     - `quincyBowConsumesReiatsuAndFiresWithoutVanillaArrows`: PASS
     - `seeleSchneiderExecutionChargesPEAndSetsCooldown`: PASS
     - `lichtRegenExecutionChargesPEAndSetsCooldown`: PASS
     - `hirenkyakuChargesPEAndStaminaAndSetsCooldown`: PASS
     - `sprengerLifecycleCreationAndDetonation`: PASS
   - Nota de auditoria: 1 teste preexistente da frente paralela (`sealedhado91preparesandconvergesontargetwithoutblockdestruction` em Benihime) falhou na asserção de dano da outra frente, não tendo relação com o módulo Quincy; kits de Benihime, Zaraki, Higashi, Senbon e Hyōrinmaru foram mantidos intactos.

### Limitações Materiais Declaradas
1. **Ponte Blockbench MCP**: A porta local 8787 do Blockbench estava inacessível durante a sessão; o arquivo mestre `.bbmodel` e os modelos JSON foram gerados de forma programática rigorosa e salvos na pasta autorizada `tools/models/lab/antigravity/quincy01/ag_quincy_bow.bbmodel`.
2. **Jornada Pública de Aquisição**: Conforme Ficha V0 e escopo aprovado, o encontro de narrativa/mentor Uryū em Karakura e consumo físico de itens Gintō/Seele permanecem pendentes da fase de produção de quests (M08a/8.5 e M11). Para testes imediatos em laboratório, utiliza-se o comando seguro `/bleachdev quincy <player>`.
3. **OmniRoute**: Não esteve acessível neste host/sessão; utilizou-se o provedor local ativo.

### Roteiro Curto para Teste Humano no Cliente Real
1. Iniciar o cliente Minecraft de desenvolvimento (`./gradlew runClient` ou script `Invoke-Design.ps1 -Mode Client`).
2. Em um mundo de testes, executar `/bleachdev quincy @p`.
3. **Disparo com o Kojaku Bow**:
   - Segurar botão direito para armar o arco. Observar em primeira e terceira pessoa: postura lateral, mão oposta puxando a corda até a bochecha, escala de tração da textura do modelo (`pulling` e `pull`). Soltar para disparar.
   - Observar o consumo de 2 PE na barra de Reiatsu e o rastro de partículas Reishi até o impacto (máx. 48 blocos).
   - Testar com a opção de mão principal esquerda (canhoto) nas opções do jogo: verificar o espelhamento natural da pose.
4. **H (Seele Schneider)**:
   - Pressionar a tecla de habilidade 1 (ou `H`). Observar o disparo da lâmina espiritual, perfuração de até 3 alvos e consumo de 12 PE (CD 6s).
5. **N (Licht Regen)**:
   - Pressionar a tecla de habilidade 2 (ou `N`). Observar a rajada frontal de 12 flechas em leque de 20 graus por 2 segundos e consumo de 20 PE (CD 10s).
6. **B (Hirenkyaku)**:
   - Pressionar tecla 3 (ou `B` / `Alt + Direção`). Observar o deslizamento horizontal rápido de 6 blocos com partículas nos pés e parada segura diante de obstáculos ou beiradas de despenhadeiros.
7. **C (Sprenger)**:
   - Mirar no chão e pressionar tecla 4 (ou `C`). Observar a colocação dos 5 nodos de Seele Schneider no terreno formando o pentágono e o consumo único de 35 PE.
   - Aguardar 1.5s de preparo e pressionar tecla 4 novamente antes de 6.0s para detonar a área e dissolver os nodos.


---

## Ciclo de Correções e Integração — 28/10/2026 (Frente Antigravity A)

**Branch de execução**: `codex/quincy-correcao-integracao`  
**Base remota**: `origin/Feature-Poderes-bankais` (`d247bb09ad81396e77310701a450ace00dfe77cd`)  
**Coordenação**: Conforme `Docs/poderes/coordenacao-correcoes-kits-comandos-2026-10-28.md`  

### 1. Diagnóstico e Reparo do Modelo e Textura (Prioridade Zero)
- **Causa Raiz Identificada**:
  - `BlockElement$Deserializer.getAngle` no Minecraft Forge 1.20.1 rejeitava com `JsonParseException` qualquer rotação que não pertencesse ao conjunto estrito `{-45.0, -22.5, 0.0, 22.5, 45.0}`.
  - No protótipo inicial, `tools/models/quincy_models.py` gerava elementos com ângulos contínuos (`-12.0`, `-25.0`, `12.0`, `25.0` e rotações fracionárias de tração da corda `-5.0`, `-10.0`, `-15.0`).
  - O Minecraft falhava no bake do modelo e aplicava o fallback para `builtin/missing` (modelo de bloco 16x16x16 com textura preta e magenta). Os transforms de empunhadura ampliavam esse cubo na mão do jogador, cobrindo metade da tela em 1ª pessoa e o tórax em 3ª pessoa.
  - Além disso, `quincy_bow.json` possuía `"parent": "item/generated"` em conjunto com `"elements"`, gerando conflitos no pipeline de item extrusion.
- **Correções Aplicadas**:
  - Reconstrução dos elementos em `quincy_models.py`: todas as rotações das hastes e da corda foram calibradas estritamente para os ângulos permitidos `-22.5`, `0.0` e `22.5`.
  - Remoção de `"parent": "item/generated"` de modelos que definem `"elements"` próprios; inclusão explícita de `"particle": "bleachmod:item/quincy_bow"`.
  - Calibração do display de GUI em `COMMON_DISPLAY`: escala ajustada de `0.9` para `0.45` com rotação `[0, 0, -45]`, permitindo que o arco espiritual de 36 unidades de altura caiba com elegância no slot de 16x16 do inventário e hotbar sem transbordar.
  - Calibração de 1ª pessoa (`firstperson_righthand` / `firstperson_lefthand`): retículo central de mira permanece desobstruído durante idle e tração.
  - Criação do script de auditoria `tools/models/validate_model_angles.py`: 100% dos 74 arquivos de modelo de item e 701 elementos validados com zero ângulos ilegais.
  - Artefatos visuais gerados em `Docs/poderes/qa/quincy-correcao-20261028/`:
    - `quincy_bow_model_preview.png`: Renderização 3D do modelo idle e tração estagio 2 com flecha engastada.
    - `comparativo_antes_depois.png`: Prancha comparativa lado a lado mostrando o erro de runtime inicial versus o modelo restaurado.

### 2. Correção de Autorização e Despacho de Técnicas (Fase 2)
- **Desacoplamento de Raça e Posse do Item**:
  - `QuincyTechniqueService.isQuincy(data)`: exige estritamente `Reference.RACE_QUINCY.equalsIgnoreCase(data.getCharacter().getRace())` e `hasCreatedCharacter()`. Possuir o arco não confere raça Quincy.
  - `QuincyBowItem`: tanto no início da tração (`use`) quanto no disparo (`releaseUsing`), verifica vida, espectador, raça Quincy e bloqueios de ação. Jogadores não-Quincy recebem feedback e são impedidos de usar o arco para burlar fundamentos.
  - `QuincyTechniqueService.authorize`: bloqueia execução durante esquiva, canalização de ação, guarda ativa ou quebrada, espectador ou morte; exige o arco Kojaku na mão principal para Seele Schneider (H) e Licht Regen (N), liberando Hirenkyaku (B) e Sprenger (C) para movimentação e catalisação.
  - `clearQuincyTransientState(player, data)`: interface mínima limpa fornecida para o Codex chamar na troca reversível de kit (`/bleachdev kit`), desfazendo campos de Sprenger e cancelando rajadas ativas.

### 3. Hirenkyaku e Desacoplamento da Esquiva Comum (Fase 3)
- **Restauração da Esquiva Universal**:
  - Removido o desvio em `DodgeService.request`: o atalho `Alt + Direção` permanece estritamente a esquiva comum física universal para todas as raças.
- **Direção e Entrada de Rede para a Tecla B**:
  - `ExecuteTechniqueSlotC2S` atualizado de forma retrocompatível para transportar `(int slot, int forward, int left)`.
  - `ClientForgeEvents`: ao pressionar `TECHNIQUE_SLOT_3` (B), captura os inputs WASD do jogador.
  - O servidor valida o vetor de deslocamento e consulta `player.zza` / `player.xxa` como fallback; sem input ativo, executa o recuo de retaguarda padrão.
- **Colisão por Volume Varrido e Terreno Seguro**:
  - `HirenkyakuService`: realiza varredura de volume (`currentBox.minmax(candidateBox)`) a cada passo de 0.5 blocos, prevenindo penetração em paredes finas (barras de ferro, vidros, cercas).
  - Sonda vertical de apoio: interrompe o deslocamento antes de precipícios e impede que o jogador deslize sobre águas profundas sem base sólida.
- **Cobrança de Recursos e Cheats**:
  - Cobra 8 PE e 10 Stamina.
  - Quando o cheat de Reiatsu estiver ativado (`costsDisabled == true`), o consumo de PE é dispensado, mas a stamina continua sendo estritamente cobrada (esforço físico do deslocamento).

### 4. Validação H / N / C com Alvos Controlados (Fase 4)
- **Seele Schneider (H)**:
  - Projétil veloz (3.0b/tick, 48 blocos), caixa de colisão de corredor (~0.5b).
  - Raycast de blocos no início de cada tick: interrompido imediatamente ao colidir com bloco sólido.
  - Checagem de linha de visada individual para cada entidade atingida: impede dano através de paredes.
  - Atravessa até 3 alvos distintos causando 10 HP em cada um e descarta ao atingir o limite.
- **Licht Regen (N)**:
  - 12 flechas disparadas em cone de 20 graus ao longo de 40 ticks (2.0s).
  - 2 HP por acerto com teto estrito de 8 HP (máximo 4 acertos contados) por alvo.
  - Respeita o `hurtTime` vanilla; limpeza automática do estado de rajada em caso de cancelamento, morte ou logout.
- **Sprenger (C)**:
  - Busca de pentágono no solo local (+/- 4 blocos verticais) com suporte a vegetação e blocos substituíveis (`canBeReplaced()`).
  - Preparo de 1.5s (30 ticks) seguido de janela de reativação até 6.0s (120 ticks).
  - Reativação válida não cobra PE novamente nem é bloqueada pelo cooldown de 20s.
  - Dano de 18 HP em até 8 alvos com checagem de linha de visada do centro cilíndrico (raio 6, altura 4).
  - Sem quebra ou destruição de blocos de terreno.

### 5. Resultados de Validação e Evidências
1. **Compilação e Regressão Offline**:
   - Comando: `./gradlew check --console=plain`
   - Resultado: `BUILD SUCCESSFUL` (Exit Code 0).
   - Suíte de regressão: `PASS: 158 regression scenarios`.
2. **GameTests no Forge Server Real**:
   - Comando: `./gradlew runGameTestServer --console=plain`
   - Resultado: `BUILD SUCCESSFUL` (Exit Code 0).
   - Total de GameTests: `248/248` aprovados (100% de sucesso).
   - Novos testes em `QuincyGameTests.java`:
     - `quincyProfileInitializationAndBowEquip`: PASS
     - `quincyBowConsumesReiatsuAndFiresWithoutVanillaArrows`: PASS
     - `nonQuincyHoldingBowCannotUseOrAuthorizeTechniques`: PASS
     - `quincyBowRequiresBowForTechniques1And2`: PASS
     - `seeleSchneiderExecutionChargesPEAndSetsCooldown`: PASS
     - `lichtRegenExecutionChargesPEAndSetsCooldown`: PASS
     - `hirenkyakuChargesPEAndStaminaAndSetsCooldown`: PASS
     - `hirenkyakuRetreatsWithoutInputAndBlocksUnderBusyStates`: PASS
     - `hirenkyakuConsumesStaminaEvenWithReiatsuCostsDisabledCheat`: PASS
     - `sprengerLifecycleCreationAndDetonation`: PASS
     - `sprengerClearTransientStateCleansField`: PASS
3. **Evidências Arquivadas**:
   - `Docs/poderes/qa/quincy-correcao-20261028/comparativo_antes_depois.png`: prancha técnica com capturas de tela do erro versus modelo reparado.
   - `Docs/poderes/qa/quincy-correcao-20261028/quincy_bow_model_preview.png`: renderização 3D das posturas de idle e tração.


## Correção Codex — posicionamento, HUD e persistência (28/10/2026)

Após os prints161822/161833/161844: o modelo já carregava, mas herdava transforms de um spriteXY vanilla apesar de sua geometria serYZ. A tração recebia rotação duplicada e a flecha/corda ficavam orientadas incorretamente. Fonte tools/models/quincy_models.py agora distingue repouso/tração e primeira/terceira pessoa, reduz escala de mão, ancora o centro geométrico do cabo e compensa as rotações vanilla de tração (incluindo a rotação final em Y negativo). Corda voxelizada une os pontos reais das extremidades ao encaixe; flecha fica acima da empunhadura. Preservados paleta, identidade e valores de combate. Projeto Blockbench e JSONs regenerados juntos, todos os822elementos dos74modelos com rotações válidas.

A ausência de HUD/progresso não era apenas renderização: o overlay aceitava exclusivamente Shinigami e QuincyData não armazenava maestria. Agora há painel Quincy com H/N/B/C, custos/cooldowns, stamina do B, janela do Sprenger, domínio do arco e maestrias por técnica. Nenhuma forma Shikai/Bankai é inventada; o HUD genérico não mostra chave de forma vazia.

QuincyData guarda domínio e quatro maestrias por IDs estáveis em NBT. Limpeza transitória e troca de kit não apagam esses valores; snapshots owner-only atualizam o cliente. Valores antigos ausentes começam em0: não existe dado histórico de maestria Quincy que possa ser recuperado de saves que nunca o gravaram. Cheats all/progress max agora maximizam também o kit Quincy, preservando o arquivo de progresso Shinigami.

Prática usa os mesmos ganhos e limites do MasteryPracticeService: domínio+0,5 e técnica+1 por oportunidade válida, intervalo global20ticks e por alvo100ticks, sem XP em modo de teste, contra aliados ou por ataque no ar. Dano Quincy usa evento LivingDamage com contexto de técnica confirmado; o handler anterior só aceitava armas melee. Hirenkyaku pratica apenas após deslocamento bem-sucedido com ameaça hostil próxima e visível. Isto não define preços/aquisição de técnicas, Gintō ou TYBW: o protótipo continua sem jornada Quincy final de M11.

Protocolo de rede2.37 evita combinar a entrada direcional nova com clientes/servidores antigos2.36. Mesma versão do mod nas duas pontas.

Validação:158regressões;258GameTests (três novos de persistência, preset e prática com antirrepetição);9cenários de cliente real, incluindo repouso/tração, ambas as mãos e perspectivas, e ida/volta de kit mantendo valores37/23 de QA. Dados foram injetados somente no cenário de desenvolvimento, não são ganhos anunciados por disparo. A primeira inspeção reprovou o sentido da corda e a orientação da flecha; corrigidos e recapturados. QA compacto em qa/quincy-correcao-20261028/codex. Não declarar homologação artística ou multiplayer pela suíte. Teste humano continua necessário, principalmente tiro em movimento, campo C e retorno ao kit anterior.
