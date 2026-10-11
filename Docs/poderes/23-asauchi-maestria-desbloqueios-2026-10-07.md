> **Estado vigente — ciclo 26, 11/10/2026:** aura e liberação da Hyōrinmaru integradas ao emissor comum com audiência próxima e orçamentos explícitos, preservando a aparência aprovada. Bancada continua privada. [Arquivos, lógica, limites e roteiro](26-integracao-gelo-emissor-comum-2026-10-11.md). Os ciclos anteriores abaixo são históricos; Aizen e narrativa permanecem futuros.

> **Estado vigente — ciclo 25, 11/10/2026:** biblioteca comum com paletas fire/ice/reiatsu e formas aura/cut/beam/release; camadas compartilham orçamento. Bancada anterior preservada; nenhuma mudança nos kits homologados. [API, arquivos, limites e comparação](25-biblioteca-paletas-camadas-2026-10-11.md). Escolha visual e integração em produção são próximos gates; Aizen e narrativa continuam futuros.

> **Estado vigente — ciclo 24, 11/10/2026:** Asauchi/maestria do ciclo 23 homologadas pelo usuário. Catálogo vanilla e bancada administrativa de seis presets, três intensidades e duração limitada, sem alterações nos kits ou terreno. [Recursos, arquivos, lógica, limites e roteiro](24-catalogo-vanilla-bancada-visual-2026-10-11.md). Aizen e aquisição narrativa continuam futuros.

# Ciclo 23 — Asauchi independente e prática de maestria

Data: 07/10/2026. Branch exclusiva: Feature-Poderes-bankais.

## 1. Decisões e limites

O usuário homologou a última correção de clima e o dragão da Hyōrinmaru do ciclo 22. Ryujin e Hyōrinmaru estão encerradas quanto àquele escopo. Este ciclo implementa uma nova base de progressão, sujeita a homologação no cliente.

Asauchi passa a ser a identidade inicial de novos Shinigamis. Conforme a correção expressa do usuário, possui selada, Shikai e Bankai, sem habilidades especiais: suas liberações aumentam apenas o dano dos golpes da espada, aproveitando os modelos existentes. Nenhuma textura, geometria, receita ou asset da Asauchi foi alterado. Aizen permanece trabalho futuro; não foi adicionado código, conteúdo nem dependência para ele.

A identidade pertence ao personagem. Equipar uma espada não troca vínculo nem importa maestria. A troca de teste continua restrita ao operador: /bleachdev zanpakuto bind <jogador> asauchi|ryujin_jakka|hyorinmaru. Volta à selada, encerra efeitos e conserva o progresso separado de cada identidade.

## 2. Contrato operacional provisório

| Elemento | Regra desta entrega |
| --- | --- |
| Maestria por forma | 0–100, independente por identidade e selada/Shikai/Bankai |
| Maestria por técnica | 0–100, independente por identidade, forma e slot |
| Prática aceita | +0,5 na forma e, quando aplicável, +1 na técnica responsável |
| Descoberta de Shikai | 20 de maestria na selada |
| Descoberta de Bankai | Shikai descoberta e 25 de maestria na Shikai |
| Aquisição/uso da forma | Também exige nível da skill zanpakuto e requisitos do registro de formas |
| Compra dos níveis | Mecânica anterior preservada: 200 TP para nível 1; 500 TP para nível 2 |
| Slots H/N/B/C dos kits elementais | Requisitos de maestria da forma 0/20/40/60 |
| Asauchi | Sem slots especiais em nenhuma forma |
| Dano da Asauchi | Bônus existente: selada +0%; Shikai +20%; Bankai +50% |

Os valores são uma primeira configuração revisável, não uma definição final de história ou balanceamento. Descobrir uma forma não ativa nem compra automaticamente sua transformação. O nível geral da skill zanpakuto continua compartilhado como infraestrutura de transformação; maestria, descobertas e técnicas são arquivadas por identidade. Esta entrega não implementa uma campanha nova de provas ou aquisição de zanpakutos.

As recompensas narrativas existentes continuam explicitamente relacionadas à Ryujin. Não concedem gelo ou identidade Asauchi por equipar outra espada. Os marcos práticos acima permitem testar a progressão enquanto a equipe desenvolve história e provas. Técnicas já conquistadas permanecem adquiridas mesmo após redução administrativa da maestria; definir maestria pelo comando de operador não é uma rotina de treino nem um reset do repertório.

O bônus de forma da Asauchi usa a fórmula anterior de combate, incluindo Zanjutsu: dano × (1 + 0,1 × rank + bônus de forma). Não é um multiplicador adicional sobre outro bônus. Formas continuam com carga, seleção e consumo de reiatsu anteriores; “apenas dano” se refere aos benefícios de combate, sem chamas, neve, clima, resistência ou técnicas elementais.

## 3. Implementação, passo a passo e arquivos

1. **Persistência e identidade — CharacterData.java / PlayerData.java.** Acrescentada a identidade asauchi como padrão de criação. Chaves de formas da Ryujin são mantidas por compatibilidade; Asauchi e Hyōrinmaru usam prefixo próprio. Acrescentados unlockedTechniques e techniqueMasteries, com cap 100 e saneamento de números não finitos no carregamento. Snapshots de aparência não apagam essas coleções.
2. **Migração — PlayerData.load / CharacterData.preserveLegacyTechniques.** Schema passa de 4 para 5. Personagens anteriores sem identidade explícita continuam Ryujin. Identidades antigas que já possuíam o repertório recebem a descoberta persistente dos slots, preservando a disponibilidade anterior após adquirir as respectivas formas. Valores de maestria, skills, inventário e quests não são copiados para Asauchi nem outra identidade. Novos saves de schema 5 não recebem essa concessão de compatibilidade.
3. **Acesso comum — TechniqueProgression.java / TransformationsHelper.java.** Servidor verifica personagem criado, vivo, raça, vínculo, arma, forma desbloqueada e slot antes de custo/cooldown. Asauchi recusa todos os slots; os kits existentes continuam em seus próprios executores. Requisitos da forma e da técnica são distintos. Os marcos de treino descobrem formas; o sistema anterior compra/seleciona/ativa.
4. **Prática — MasteryPracticeService.java.** Ganho deriva de eventos de dano aceito, controle efetivamente aplicado e dano recebido sob armadura temporária ativa. Considera inimigos hostis e jogadores adversários quando PvP é permitido; recusa aliados, NPCs, animais comuns, invocações e alvos invulneráveis. Requer a arma correspondente ao vínculo.
5. **Limites de prática.** Um crédito no máximo a cada 20 ticks por jogador; o mesmo alvo só permite outro após 100 ticks. Lista temporária limitada a 128 alvos por jogador. Técnicas persistentes só creditam enquanto houver intenção recente de uso, até 100 ticks após solicitação válida. Golpe manual aceito fornece sua própria intenção. Não há ganho somente por estar transformado, emitir partículas ou manter clima.
6. **Separação entre golpe e técnica — CombatEvents.java e executores.** Um contexto temporário identifica o slot responsável pelo dano. Técnica não recebe também crédito como golpe comum; dano e controle no mesmo alvo obedecem à mesma limitação. O contexto é restaurado em finally. Incêndio ambiental da SpiritFlame é marcado como passivo e não concede prática. Dano negativo, zero, cancelado e sem consequência não concede crédito por dano.
7. **Integração dos kits — TechniqueService.java / RyujinTechniqueService.java / HyorinmaruTechniqueService.java / IceControlService.java / SpiritFlameService.java.** Dano dos slots recebe origem de prática; barreira/controle só creditam lentidão aceita. Defesa ativa usa o slot B da armadura selada. Ignição H da Ryujin treina pelos golpes aceitos enquanto ativa, nunca pelo simples toggle. Armadura passiva das formas, fumaça, chamas e criatura visual não geram prática sozinhas. Parâmetros de alcance, partículas, dano, duração e custo dos kits homologados permanecem.
8. **Remoção de AFK — TickHandler.java.** Retirado o bloco que acrescentava maestria a cada 100 ticks transformado. Recursos, dreno, carga e atualizações de cooldown continuam.
9. **Ciclo de vida — MasteryPracticeService.** Troca de vínculo, forma, dimensão e morte encerram a intenção de prática. Logout remove registro temporário; encerramento do servidor limpa todo runtime. Dados persistentes de maestria continuam.
10. **Visual e combate da Asauchi — BleachClient.java / CombatEvents.java.** A propriedade de modelo exige identidade Asauchi para ativar seus overrides preexistentes. Bônus de dano exige arma compatível e forma desbloqueada. Empunhar Asauchi com vínculo Ryujin não transforma sua aparência nem herda o bônus da liberação Ryujin.
11. **HUD — ZanpakutoHud.java.** Mostra a identidade atual, domínio da forma, acesso aos slots e domínio individual conquistado. Asauchi mostra treino de espada em vez de nomes de habilidades da Ryujin. Atalhos vêm das configurações reais. Requisitos individuais aparecem nos slots bloqueados.
12. **Comandos e protocolo — BleachCommands.java / Reference.java.** Suggestions de bind incluem Asauchi; comandos de maestria reconhecem marcos elegíveis. Protocolo 2.6: cliente e servidor devem atualizar juntos para os novos dados.
13. **Localização — assets/bleachmod/lang/pt_br.json e en_us.json.** Mensagens para ausência de habilidades Asauchi, requisito individual e orientação de treino.
14. **Testes — MasteryGameTests.java / MvpRegressionTest.java.** Novos cenários de fronteiras, migração, persistência, prática real, controle, dano das liberações e limites. Fixtures antigas dos kits passam a declarar sua identidade e concessões de compatibilidade explicitamente; não se altera o padrão do jogador novo para fazer testes antigos passarem.

Todos os arquivos Java citados pertencem a src/main/java/com/bleachmod, exceto a suíte de regressões em src/test/java/com/bleachmod. Documentação modificada exclusivamente em Docs/poderes. Os documentos-base fora dessa pasta permanecem intactos.

## 4. Testes e homologação

Validação final: gradlew check build runGameTestServer --offline --console=plain aprovado; 60 cenários de regressão e 42 GameTests obrigatórios passaram. Log local: build/cycle23-validation.log. git diff --check aprovado; os seis assets existentes da Asauchi não possuem alterações e os documentos fora de Docs/poderes permaneceram intactos. JAR atualizado: build/libs/bleachmod-0.2.0.jar. Testes usam Java 17, Forge 47.4.10 e Minecraft 1.20.1. A defesa é verificada despachando o fluxo de eventos Forge de dano reduzido, pois FakePlayer rejeita dano vanilla; confirmar dano real ao jogador na homologação manual. Este relatório não afirma homologação visual desta nova progressão.

### Percurso de jogador novo

1. Use um mundo/personagem de teste novo e conclua a criação Shinigami. Confirme Asauchi no HUD e maestria selada zero.
2. Empunhe Asauchi. H/N/B/C não devem consumir reiatsu nem produzir habilidades elementais.
3. Mantenha custos e cooldowns normais. Ataque zumbis ou outros inimigos hostis. Cada crédito válido dá 0,5 de maestria, limitado por alvo e intervalo global; golpear repetidamente sem esperar não acumula.
4. Confirme ausência de ganho contra animais comuns, aliados, alvo invulnerável e ao ficar parado. Em reiatsu free ou cooldowns disable, confirme ausência de ganho mesmo com golpes aceitos.
5. Ao atingir 20 na selada, confirme descoberta de Shikai e compre nível 1 de zanpakuto com 200 TP pela interface existente. Se faltar TP, complete treinos/quests existentes.
6. Selecione e ative Shikai pelo fluxo de transformação atual. Verifique o modelo anterior, dano maior e ausência de chamas, neve ou habilidades.
7. Treine na Shikai até 25; confirme descoberta de Bankai. Compre nível 2 por 500 TP e transforme conforme a cadeia existente. Verifique modelo anterior e aumento de dano.
8. Saia e entre novamente. Confirme persistência da identidade, maestria e descobertas.

### Kits elementais e compatibilidade

1. Em personagem novo, o operador pode vincular hyorinmaru ou ryujin_jakka. Empunhe a espada correspondente; a troca sempre volta à selada e não importa maestria da Asauchi.
2. H começa acessível; N/B/C exigem 20/40/60 na forma corrente. Tentativa bloqueada não debita recurso nem estabelece cooldown.
3. Para alcançar fronteiras sem grind, use /bleachdev mastery set @s zanpakuto sealed 19.5 e depois 20; tente N em cada caso. Repita 39.5/40 para B e 59.5/60 para C em um perfil ainda sem essas descobertas.
4. Não espere relock de técnica conquistada ao reduzir a maestria: descoberta é persistente. Para testar bloqueios iniciais, use outro personagem novo.
5. Controle de duelo e barreira aceitos treinam seus slots; armadura temporária treina B quando reduz um golpe real. Dano persistente sem entrada recente não continua concedendo domínio.
6. Troque de vínculo, volte e reinicie: cada identidade conserva suas próprias formas e técnicas. Apenas trocar o item não modifica o vínculo.
7. Em personagem do ciclo 22, confirme que nenhum repertório anterior fica bloqueado indevidamente. Reduções administrativas futuras não equivalem a reset de técnicas.
8. Homologue também a legibilidade do HUD nas escalas GUI habituais e as três aparências preexistentes da Asauchi.

Para treinamento normal após testes: /bleachdev reiatsu restore @s e /bleachdev cooldowns restore @s. Os comandos fill/set continuam ferramentas administrativas. Aizen, narrativa completa de aquisição, provas pessoais, benefícios numéricos da maestria individual e migração de atributos ficam fora deste ciclo.
