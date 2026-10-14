# Ciclo 36 — Consolidação, homologação e próximos passos

Data: 13/10/2026. Branch: Feature-Poderes-bankais. Entrega documental, sem alteração de código, assets ou documentos fora de Docs/poderes. O usuário confirmou que a proporção/inclinação final da bainha está perfeita e pediu documentação consolidada e revisão da próxima etapa.

## Estado confirmado

Stamina e estado automático de combate foram testados pelo usuário; progressão da stamina também foi aprovada. O visual final de bainha foi homologado após os ajustes dos ciclos 34 e 35. Isso encerra o refinamento solicitado de escala, lado, curva e inclinação. Não representa homologação de FPS, multiplayer com dois clientes, latência ou compatibilidade com mods externos.

O código atual utiliza protocolo 2.9 e schema 7. Cliente e servidor devem usar a mesma build. Foram preservados os IDs dos itens, modelos/texturas das espadas e progresso por identidade. Aizen continua futuro.

## Histórico das novas implementações, ponto a ponto

| Ciclo | Entrega | Arquivos/lógica completos |
|---|---|---|
| 30 | Estado automático após dano atribuído a entidade viva; expira em 200 ticks sem novo dano. HUD e limpeza de morte/reconexão/dimensão. | [Relatório 30](30-estado-combate-2026-10-12.md) |
| 31 | Stamina inicial de corrida, recuperação, exaustão, persistência e HUD. | [Relatório 31](31-stamina-corrida-2026-10-13.md) |
| 32 | Custo físico de ataques manuais e três zanpakutōs indestrutíveis, inclusive itens antigos. | [Relatório 32](32-ataques-basicos-zanpakutos-indestrutiveis-2026-10-13.md) |
| 33 | Máximo por Resistência, presença vinculada na cintura, sincronização pública mínima e saque/guarda nativos. | [Relatório 33](33-progressao-stamina-bainhas-saque-2026-10-13.md) |
| 34 | Redução/afunilamento/curvatura da bainha, correção anatômica do lado, ajuste do gesto e tooltip K. | [Relatório 34](34-refinamento-bainha-tooltip-resistencia-2026-10-13.md) |
| 35 | Ajuste final homologado: aumento de 15% e mais 18 graus para baixo. | [Relatório 35](35-escala-inclinacao-bainha-2026-10-13.md) |

## Contrato operacional vigente

### Stamina e Resistência

- Máximo = 100 + 100 × R / (R + 20), usando níveis comprados atuais de Resistência. R0 = 100; R10 ≈ 133,33; R20 = 150; R40 ≈ 166,67. Retornos decrescentes, limite 200.
- Não é implementação da escala futura de atributos 1–10.000 nem treino automático de Resistência. Sete atributos atuais continuam.
- Compra aumenta capacidade sem preencher saldo. Load completo deriva máximo pelos atributos; snapshots enviam máximo validado. Redução da capacidade limita o saldo.
- Sprint em deslocamento horizontal consome 5/s; tentativa manual contra alvo vivo com zanpakutō consome até 4. Mão vazia, arma vanilla, mineração e golpe no ar não recebem cobrança nova.
- Recuperação 20/s após 20 ticks sem esforço. A zero, sprint é interrompido e retorna em 10; ataque básico permanece permitido. Insistir em ataques renova a pausa mesmo a zero.
- Criativo/voo/passageiros são excluídos do esforço de corrida; ataque exclui criativo/espectador/evento cancelado/alvo morto/aliado. Fome e regras vanilla continuam.
- Técnicas especiais não consomem stamina adicional; reiatsu, manutenção de forma, cooldowns e maestria conservam seus próprios contratos. Modo de custo gratuito de reiatsu não isenta ataques físicos.
- Saldo, pausa e exaustão persistem; reconexão/dimensão não enchem barra, respawn restaura o máximo. Sem recuperação offline.
- ResourcesData, StaminaPolicy, StaminaService, BasicAttackStamina, PlayerData, TickHandler, CapabilityEvents, SyncHelper e ReiatsuHud implementam essas regras. Tooltip de Resistência na tela K menciona mitigação e stamina máxima em pt_br/en_us.

### Durabilidade

- Asauchi, Ryujin Jakka e Hyōrinmaru usam ZanpakutoItem indestrutível, com atributos ofensivos anteriores.
- Capacidade de desgaste zero, sem barra ou quebra; IDs preservados fazem espadas antigas também receberem a regra.
- Tag Damage antiga pode permanecer sem efeito; demais tags não são apagadas. Armas vanilla mantêm desgaste.
- NPC de reparo não foi criado, conforme escolha explícita de retirar durabilidade.

### Bainha e animação

- Somente a identidade vinculada carregada no inventário principal/hotbar ou mãos cria uma bainha. Duplicatas não multiplicam o visual; outras identidades e baús não contam.
- Estado público mínimo: ausente/guardada/mão principal/mão secundária. ZanpakutoCarry calcula; CharacterData.saveAppearance e SyncHelper distribuem a observadores; não expõe inventário completo e não grava presença no save persistente.
- PlayerModel +X é esquerda anatômica. Personagem destro usa bainha na esquerda; canhoto espelha. Empunhada mantém bainha vazia.
- Paletas próprias: Asauchi cinza escura, Ryujin escura/carbono, Hyōrinmaru azul-petróleo com cabo ciano/guarda em cruz. Geometria nova utiliza material vanilla; não altera a espada original.
- Perfil de três segmentos sobrepostos e afunilados: rotações internas aproximadas de três e quatro graus. Cabo, amarrações, guarda e ponteira seguem proporção reduzida.
- Ajuste final: escala 0,9775 (15% acima de 0,85 do ciclo 34), rotação X -26 graus (18 adicionais abaixo dos -8 anteriores), orientação Y ±25 graus. Continua menor que a primeira versão reta do ciclo 33. Curva interna modifica a inclinação local da ponta.
- ZanpakutoMotion, ZanpakutoDraw e ZanpakutoSheathLayer implementam transição de oito ticks ao selecionar/deselecionar a arma. Braço e mão/item em primeira pessoa se deslocam; guardar produz retorno e assentamento do cabo. Não atrasa ataque nem cria custo ou dano.
- Primeira observação, troca de identidade, remoção e troca direta entre mãos não inventam saque completo. Cache com chaves fracas, limpo sem jogador local; camada acompanha torso e skins default/slim, oculta em morte/espectador/invisibility.
- A animação é cosmética nativa simplificada; não simula contato físico preciso da lâmina com a bainha. Ação dedicada de embainhar sem trocar slot continua futura.

### Estado de combate

Combat State é detecção automática de conflito; não é Combat Mode manual. Seu efeito atual é indicador e estado transitório reutilizável. Ainda não penaliza logout, não muda recuperação de reiatsu, não fecha inventário e não impõe bloqueio de ações. Futuras regras podem consultá-lo, mas precisam de contrato próprio. Postura não existe ainda no código deste ciclo.

## Validação registrada

- Ciclo 33: build/check e 73 regressões; 63 GameTests obrigatórios aprovados. Ajustes finais de apresentação tiveram nova build/check com as mesmas 73 regressões.
- Ciclos 34 e 35: build/check e 73 regressões aprovados; não repetiram GameTests de lógica inalterada. Logs locais cycle33-validation.log, cycle33-visual-build.log, cycle34-validation.log e cycle35-validation.log em build.
- Referências Git: 605b8e6 (progressão/bainha/saque), a4a99ac (curva/lado/tooltip), 349f016 (escala/inclinação finais); commits de integração intermediários não são mudanças desta entrega documental.
- Ciclo 36: apenas documentação, revisão de links locais e diff; não executa nova build nem afirma testes de código novos. Aceite visual final recebido do usuário.

## Próximo passo recomendado — proposta, ainda sem implementação

A documentação M06-06E exige esquiva curta, responsiva, com stamina e foco em sair da trajetória. M06-06F separa essa ação fundamental de Shunpo/Sonído/Hirenkyaku. Portanto, a próxima fatia recomendada é esquiva direcional universal para personagens criados, sem depender da identidade da zanpakutō.

Proposta inicial de protótipo, valores sujeitos a aprovação e teste:

1. Tecla configurável, sugestão Alt esquerdo, combinada com direção WASD; sem direção, recuo curto. Não reutilizar teclas H/N/B/C/R/G/V/J/K/Z já ocupadas.
2. Custo de 15 stamina, alcance nominal até 2,5 blocos, deslocamento ao longo de aproximadamente cinco ticks, intervalo mínimo de 12 ticks entre ativações. Esses números não estão impostos pelos documentos-base e ainda não foram aplicados.
3. Servidor valida vida, personagem, espectador, reserva suficiente, intervalo, imobilização quando aplicável e condição de movimento. Pacote representa intenção/direção limitada, nunca posição final fornecida pelo cliente.
4. V1 terrestre: movimento horizontal sujeito à colisão vanilla, sem teleporte através de parede, sem extensão de salto/voo e sem alteração da regra de queda. Água, escadas, montaria e condições especiais serão delimitadas no contrato antes de implementar.
5. Falha de pré-condição não cobra recurso. Reserva insuficiente recusa esquiva; diferentemente do ataque básico, esta ação exige custo completo. Esquivar conta como esforço e reinicia pausa de regeneração.
6. Sem invulnerabilidade artificial na primeira versão. Manter reiatsu e maestria dos kits; feedback visual/sonoro curto com material vanilla e sem excesso de partículas.
7. Testes: direções, custo único, limite de pedidos, colisões, saldo insuficiente, concorrência com sprint/ataques, limpeza em morte/dimensão/reconexão e segundo cliente com latência. Nova fatia só será considerada homologada após playtest.

Depois: bloqueio frontal com Postura própria e Guard Break (M06-06A/B/C). Bloquear não deve automaticamente cobrar stamina e Postura ao mesmo tempo; Postura não é moeda, atributo nem recurso de esquiva. Parry segue adquirido por treinamento/técnica, não gratuito universal.

A seguir, considerar Hohō/Shunpo aprendido ou um pequeno conjunto de Kidō, conforme M06-07 e decisões da equipe. Migração de atributos/proficiência/treino, Combat Mode/hotbar, história de aquisição e Aizen exigem etapas próprias. Essa ordem aproveita a stamina homologada e consolida combate básico antes de outro kit/Bankai.