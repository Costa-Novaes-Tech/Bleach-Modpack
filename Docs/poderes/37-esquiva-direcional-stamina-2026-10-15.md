# Ciclo 37 — Esquiva direcional com stamina

Data: 15/10/2026. Branch: `Feature-Poderes-bankais`.

## Escolha e contrato revisado

O usuário escolheu a esquiva como próxima feature e, após discutir dois toques nas teclas de movimento, preferiu **Alt + direção**. Dois toques não foram implementados; corrida vanilla permanece disponível pelos controles anteriores. Esta ação fundamental segue M06-06E/F e não exige arma, identidade ou aprendizado de Shunpo. Stamina/Resistência e bainhas anteriormente homologadas permanecem; Aizen segue adiado.

Contrato da V1:

- Alt esquerdo, remapeável no menu de controles, pressionado enquanto uma direção está segurada. Usa os controles de movimento configurados no Minecraft, não letras fixas. Sem direção, recuo. Teclas opostas se anulam; diagonais são normalizadas.
- Cada novo pressionamento de Alt faz uma tentativa. Segurar Alt não repete nem fica aguardando stamina/cooldown para disparar depois. Menus e janela sem foco não enviam pedidos.
- Custo completo de **15 stamina**, cinco ticks de impulso horizontal a **0,5 bloco/tick**, intervalo de **12 ticks a partir da aceitação**. Distância nominal de 2,5 blocos; física vanilla, colisões e rede influenciam a distância efetiva. Não é teleporte nem garantia de distância exata em cliente real. Valores de protótipo, sujeitos a playtest.
- Direção calculada no servidor pelo yaw do jogador na aceitação, sem componente vertical; olhar para cima não lança o jogador. Direção permanece fixa durante a ação.
- Personagem criado, vivo, no chão, em sobrevivência/aventura. Recusa espectador/criativo, voo/planagem, montaria, água/bolhas/lava, escadas, sono, uso de item, congelamento completo e qualquer efeito de lentidão. A restrição de lentidão é conservadora: a V1 não oferece esquiva reduzida durante esse efeito. Atributo de movimento zero também recusa.
- Recusa durante carga de transformação ou Flame Dash. Técnicas dos slots não iniciam durante a esquiva. Efeitos já existentes que não prendem o movimento continuam; não há cancelamento universal de técnicas. Não foi criado um sistema genérico futuro de stun/root.
- Servidor aceita somente dois eixos inteiros entre -1 e 1, nunca destino/velocidade/custo enviados pelo cliente. Pacote usa também o limitador comum de ações. Sem reserva completa, intervalo ou condição válida, não cobra.
- Valida volume varrido de cada passo, borda do mundo e chunks já carregados. Parede no primeiro passo recusa sem custo; obstáculo surgido depois interrompe, sem reembolso. A checagem é conservadora e pode parar antes de encostar na parede. Física vanilla continua resolvendo colisões.
- Não acrescenta velocidade vertical, não altera `fallDistance`, não oferece esquiva aérea ou invulnerabilidade. Ao perder chão, o impulso horizontal da esquiva é encerrado; gravidade/dano de queda continuam vanilla. Controles do Minecraft, física e rede ainda exigem avaliação em jogo nas bordas.
- Sprint é interrompido durante a esquiva para evitar cobrança sobreposta. Ataques básicos continuam possíveis e conservam seu custo. Esforço da esquiva mantém a pausa de recuperação; custo gratuito de reiatsu não isenta stamina.
- Duração, direção e intervalo são transitórios, limpos pelos fluxos existentes de morte/logout/dimensão/carregamento. Mudança de forma cancela a duração, preservando o intervalo. Saldo de stamina mantém a persistência existente; reconectar não reabastece, respawn restaura máximo.
- Som vanilla curto e duas partículas de nuvem por tick ativo. Sem alterações de modelos, texturas, bainhas ou kits homologados.

## Arquivos e integração

Arquivos Java relativos a `src/main/java/com/bleachmod`:

| Frente | Arquivos |
| --- | --- |
| Política e execução | `common/DodgePolicy.java`, `common/DodgeService.java` |
| Estado e custo | `common/data/StatusData.java`, `ResourcesData.java`, `common/StaminaService.java` |
| Entrada | `client/input/ModKeybinds.java`, `client/BleachClient.java`, `client/events/ClientForgeEvents.java` |
| Rede | `common/network/c2s/DodgeC2S.java`, `common/network/NetworkHandler.java`, `Reference.java` |
| Tick e concorrência | `server/events/TickHandler.java`, `common/technique/TechniqueProgression.java` |
| Integração automatizada | `gametest/DodgeGameTests.java` |

Nome do controle traduzido em pt_br/en_us. Regressões puras adicionadas em `src/test/java/com/bleachmod/MvpRegressionTest.java`.

Protocolo passa de **2.9 para 2.10**, com pacote novo acrescentado após os anteriores. Cliente e servidor precisam da mesma build. **Schema 7 preservado**: nenhum novo campo persistido, sem migração de saves.

## Validação e limites

Primeira execução de `check build` aprovada com **76 regressões**. A compilação inicial identificou chamadas de `LazyOptional.orElseThrow` sem supplier nos novos GameTests; corrigidas antes da build aprovada. Log: `build/cycle37-validation.log`.

Validação final: `check build runGameTestServer --offline --console=plain` aprovada, com **76 regressões e 67 GameTests obrigatórios** (63 anteriores + quatro novos). Log: `build/cycle37-integration.log`. `git diff --check` sem erros. JAR atualizado: `build/libs/bleachmod-0.2.0.jar`. Implementação entregue para playtest; ainda não homologada pelo usuário.

Os GameTests usam FakePlayer e, no teste de deslocamento, aplicam manualmente cinco passos pela física de colisão vanilla. Isso valida direção/custo/estado e integração local, não reproduz interpolação, processamento de teclado ou tráfego de dois clientes reais. Não afirmar homologação visual, de latência ou FPS com esses testes.

## Roteiro de homologação em jogo

1. Personagem criado no chão: testar Alt + frente/trás/esquerda/direita, diagonais e Alt sem direção; repetir com yaw diferente e olhando para cima/baixo. Remapear o controle e uma tecla de movimento.
2. Segurar Alt, abrir chat/inventário/tela K, trocar foco e voltar: nenhuma repetição automática. Dois toques em W mantêm corrida vanilla.
3. Conferir débito único de 15, rejeição abaixo de 15 sem custo, pausa da recuperação e intervalo entre ativações. Testar sprint e ataques concorrentes; somente os ataques mantêm seu custo separado.
4. Testar parede fina, quina, porta, slab, escada de blocos, teto baixo e borda. Não atravessar obstáculos, ganhar altura ou prolongar queda. Verificar que a parada conservadora continua confortável.
5. Testar água/lava, escada de mão, barco/montaria, voo/elytra, lentidão, gelo, uso de item, criativo/espectador, carga de transformação e Flame Dash. Não permitir contornar restrições.
6. Testar morte/respawn, troca de dimensão/forma e reconexão durante a ação; nenhum impulso sustentado ou recarga indevida. Testar ataques recebidos: dano normal durante esquiva.
7. Dois clientes com a mesma build: direção/posição observadas, som/partículas, correções de posição e latência. Ajustar velocidade/duração somente após medir a resposta real. Homologação depende do usuário.

## Próximas implementações para escolher depois desta feature

1. **Bloqueio frontal, Postura e Guard Break:** sequência recomendada de combate fundamental. Definir Postura separada de stamina; evitar cobrança dupla automática. Parry exige aprendizado próprio.
2. **Mentor com treinamento curto:** primeiro ciclo de aquisição, objetivo verificável e recompensa; narrativa depende da equipe.
3. **Hohō/Shunpo aprendido:** mobilidade espiritual própria, com aquisição, custo, recovery e maestria; não substituir a esquiva universal.
4. **Pequeno repertório de Kidō:** poucas técnicas com papéis distintos, catálogo e contrato por técnica.
5. **Proficiência das disciplinas:** Zanjutsu/Hakuda/Hohō/Kidō separadas de atributos e maestria individual; definir fontes válidas de prática.
6. **Encontro Hollow ou boss:** validar integrações das outras branches antes de criar conteúdo duplicado.

Migração de atributos, Combat Mode/hotbar, dimensões/viagens e cooperação são etapas maiores e precisam de contratos próprios. A lista não autoriza implementação automática da próxima feature nem retoma Aizen.
