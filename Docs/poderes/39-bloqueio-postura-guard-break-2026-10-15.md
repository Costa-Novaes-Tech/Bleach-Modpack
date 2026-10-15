# Ciclo 39 — Bloqueio, Postura e Guard Break com escudo visível

Data: 15/10/2026. Branch: `Feature-Poderes-bankais`. Protótipo implementado para playtest, ainda sem homologação do usuário.

## Decisões e fila atualizada

O usuário descreveu guarda mantida com escudo visível acima/à frente do personagem, desgaste por impactos e quebra que abre a defesa. Bloqueio/Postura e Guard Break foram implementados juntos. Escolhida a posição **acima do jogador**, com indicador adicional para primeira pessoa. O escudo é sinalização de guarda: não é um item novo nem uma barreira que intercepta entidades.

Fila vigente, revisada pelo usuário após esta entrega: **defesa fundamental → Byakuya → Urahara → Mentor → Proficiências → revisão final de Hyōrinmaru/Tōshirō**. Gin foi retirado do planejamento. Usuário pesquisará os poderes antes de fechar os contratos; Mentor e Proficiências permanecem adiados até confirmação com a equipe. Aizen permanece reservado e adiado. Usuário testará a versão atual com esquiva e defesa; nenhum aceite novo foi presumido. Detalhes no [roadmap atualizado](38-roadmap-defesa-treinamento-gin-byakuya-2026-10-15.md).

## Contrato implementado

- **Segurar X**, remapeável em Controles, mantém guarda. F foi considerado, mas descartado por já trocar itens entre mãos no vanilla. Não substitui uso de itens ou escudo vanilla.
- Personagem criado, vivo, sobrevivência/aventura; não depende da identidade vinculada. Recusa criativo/espectador, sono, montaria, voo/elytra, uso de item, congelamento completo, carga de transformação, esquiva e Flame Dash. A guarda não exige chão: ao contrário da esquiva, pode defender no ar/água se as demais condições forem atendidas.
- Guarda ativa impede sprint, ataque manual e técnicas dos slots. Caminhada/pulo continuam disponíveis. Não aplica lentidão de movimento adicional nesta V1. A esquiva e o início de transformação são recusados enquanto a guarda estiver ativa; é necessário baixá-la primeiro. Reversão/descida de forma preserva a Postura.
- Cone frontal de **120 graus**: até 60 graus para cada lado do yaw do defensor, ignorando inclinação vertical da câmera. Atacante coincidente sem direção horizontal definida não recebe bloqueio.
- Bloqueia parcialmente apenas fontes diretas físicas `player_attack`, `mob_attack`, `mob_attack_no_aggro` e `sting`, atribuídas a uma entidade viva. Não bloqueia projéteis, explosões, ambiente, magia ou fontes que ignoram escudo/invulnerabilidade. Golpes pelas costas não gastam Postura nem recebem mitigação.
- O sistema classifica pelo tipo da fonte de dano. Uma técnica que reutiliza `player_attack` direto também segue essa regra física; não existe ainda metadado próprio de pressão/bloqueabilidade por técnica. Não afirmar suporte refinado a todo poder especial.
- Espada na mão principal (`SwordItem` ou tag de espadas) reduz **65%** do dano elegível; mão vazia/outros itens reduzem **35%**. Escudo vanilla em uso desativa esta guarda e mantém suas próprias regras. Knockback mantém regras vanilla.
- Postura máxima fixa em **100**. Pressão por golpe = `clamp(dano elegível × 4, 8, 50)`, calculada no evento de dano após as modificações anteriores do mod. Não é um atributo novo e não escala com Resistência nesta V1.
- Ao zerar, o golpe que causa a quebra ainda recebe a mitigação; os seguintes não. Guarda cai imediatamente, com **20 ticks (um segundo nominal)** de abertura. Guarda, esquiva, técnicas dos slots e início de transformação ficam impedidos nessa janela. Movimento e ataques básicos continuam permitidos: não é stun total.
- Após quebra/interrupção, manter X pressionado não rearma a guarda automaticamente: soltar e pressionar de novo. Também é preciso recuperar pelo menos **10 de Postura** para iniciar nova guarda.
- Recuperação de **20 Postura/s**, após **20 ticks sem pressão**, somente com guarda baixada. Segurar guarda não consome Postura ociosamente, mas impede sua recuperação. Após quebra a partir de zero, o mínimo 10 é alcançado aproximadamente meio segundo depois do fim da abertura, sem novos impactos.
- Não cobra stamina ou reiatsu para levantar/manter/bloquear. Recursos e maestrias anteriores mantêm seus contratos; nenhum treino/proficiência novo foi adicionado.
- Servidor decide guarda, direção, custo de Postura, quebra e recuperação. Cliente envia apenas estado pressionado/solto, com renovação a cada cinco ticks. Pedido de ativação limitado a um a cada dois ticks; soltura sempre aceita. Sem renovação por 12 ticks, guarda interrompe, exigindo novo pressionamento. Menus/perda de foco soltam o pedido; lease cobre desconexão/perda de sinal.

Todos os números são valores de protótipo para playtest, não balanceamento homologado.

## Visual e sincronização

- Escudo desenhado em código, sem novo asset raster, textura externa ou entidade. Preenchimento diminui conforme Postura; rachaduras aparecem abaixo de 75% e 40%; cor de alerta abaixo de 25%.
- Durante a quebra, símbolo vermelho dividido, som vanilla de escudo quebrado e oito partículas de crítico. Golpes bloqueados têm som de defesa vanilla.
- Observadores veem escudo acima do personagem, voltado para a câmera, até 32 blocos, oculto em morte/espectador/invisibilidade. Posicionado acima da região do nome; tamanho, altura e oclusão precisam de playtest.
- Primeira pessoa usa o mesmo desenho abaixo da mira, texto de Postura e mensagem de guarda quebrada. Mantém indicação de recuperação até encher. Não altera a bainha nem as texturas/modelos homologados.
- Snapshot público mínimo: Postura, guarda ativa e ticks de quebra. Integra a sincronização de aparência existente e o início de tracking. Observadores recebem mudanças em faixas de cinco pontos, com transições ativa/quebrada imediatas; dono também recebe snapshots periódicos de recursos. Não envia inventário ou progresso para desenhar o escudo.

## Persistência e compatibilidade

**Protocolo 2.11**, **schema 8**. Cliente e servidor devem usar a mesma build. O novo pacote de guarda foi acrescentado após os anteriores.

`GuardData` persiste saldo, pausa e tempo restante de quebra; ativo, tecla/lease e trava de novo pressionamento não são salvos. Morte/logout/dimensão limpam intenção. Reconectar/trocar dimensão não recarrega Postura nem apaga quebra; não há recuperação offline. Respawn restaura Postura máxima. A mudança de schema preserva atributos, identidade, maestria, formas, stamina e quests; saves anteriores sem `guard` começam com Postura cheia.

## Arquivos

Relativos a `src/main/java/com/bleachmod`:

| Frente | Arquivos |
| --- | --- |
| Política, estado e defesa | `common/GuardPolicy.java`, `common/GuardService.java`, `common/data/GuardData.java`, `common/data/PlayerData.java` |
| Entrada e rede | `client/input/ModKeybinds.java`, `client/BleachClient.java`, `client/events/ClientForgeEvents.java`, `common/network/c2s/GuardC2S.java`, `NetworkHandler.java`, `SyncHelper.java`, `client/network/ClientPacketHandler.java`, `Reference.java` |
| Visual | `client/hud/GuardVisuals.java`, `client/hud/ReiatsuHud.java` |
| Concorrência/ciclo de vida | `common/DodgeService.java`, `common/technique/TechniqueProgression.java`, `common/network/c2s/UpdateStatC2S.java`, `server/events/TickHandler.java`, `CapabilityEvents.java`, `FormModeHandler.java` |
| Integração | `gametest/GuardGameTests.java` |

Regressões em `src/test/java/com/bleachmod/MvpRegressionTest.java`; traduções pt_br/en_us. Documentação alterada somente em `Docs/poderes`.

## Validação

`check build runGameTestServer --offline --console=plain` aprovado: **80 regressões e 71 GameTests obrigatórios** (quatro regressões e quatro GameTests novos). Logs: `build/cycle39-validation.log` (build inicial) e `build/cycle39-integration.log` (validação final). JAR: `build/libs/bleachmod-0.2.0.jar`. Revisão de links locais e `git diff --check` concluídos sem erros.

Não foi iniciado cliente gráfico para homologar a aparência. GameTests usam FakePlayer e eventos reais de dano/ataque; não substituem teste visual, fluxo de teclado ou dois clientes com latência.

## Roteiro de homologação

1. Segurar/soltar X, remapear controle, testar F vanilla, menus e perda de foco. Escudo somente enquanto guardando; nenhuma guarda presa.
2. Terceira pessoa e segundo cliente: escudo acima do personagem, sem sobrepor nome; preenchimento, rachaduras, alerta e quebra legíveis. Testar skin slim/default, agachar, câmera, distância, invisibilidade e oclusão por paredes.
3. Golpes frontais, laterais e pelas costas; mão vazia e espada. Conferir perda de Postura, mitigação parcial e ausência de custo de stamina/reiatsu. Testar projéteis, explosão, queda, fogo, magia e escudo vanilla sem defesa dupla.
4. Zerar Postura: golpe final ainda mitigado, escudo quebra, hits seguintes normais; esquiva/técnicas/transformação recusadas durante abertura, ataques e deslocamento comuns disponíveis. Segurar X não rearma; soltar, recuperar e pressionar novamente.
5. Conferir recuperação apenas de guarda baixada, intervalo sem pressão e retorno a partir de 10. Testar sprint, ataque, uso de item, carga e Flame Dash concorrentes.
6. Reconectar/trocar dimensão durante guarda/quebra, morrer/respawnar e carregar save anterior ao schema 8. Não recarregar Postura em reconexão nem perder progresso antigo; respawn restaura.
7. Dois clientes na mesma build e latência: início/fim da guarda, impactos, quebra e recuperação coerentes. Registrar conforto de tecla, altura/tamanho do escudo e ritmo da abertura antes de homologar.

Próximo contrato após homologação: **Byakuya**, com pesquisa do usuário. Não iniciar Mentor/Proficiências enquanto a equipe não confirmar as informações.
