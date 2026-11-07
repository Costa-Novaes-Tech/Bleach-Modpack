# Campanha Shinigami — implementação inicial

Data: 29/10/2026. Base: M13/10.32, commit `99064862`. Esta entrega implementa o catálogo de 28 missões e a seleção por personagem/contexto/progresso. As fichas narrativas continuam em [M13](../game-design/13-campanhas-e-sidequests.md); este arquivo descreve a integração executável.

## Regras implementadas

### Direção atual — primeira quest da Rukia por etapas (30/10/2026)

**Morte do Fishbone sem crédito — correção posterior de 30/10/2026:** após o processamento normal da vitória, a morte do alvo válido sem progresso no objetivo libera nova tentativa do combate, preservando a introdução e o vínculo com Rukia. Lava e golpes de outro jogador não concedem vitória ao proprietário. Mortes canceladas e eventos de uma tentativa antiga não reiniciam o confronto atual. Se um save já estiver travado com o boss ausente, interagir com a mesma Rukia verifica os alvos carregados da tentativa e libera a retomada quando não encontra um vivo; não força chunks. Uma tentativa anterior descarregada perde validade ao retomar e é rejeitada se voltar a carregar. As demais quests mantêm suas regras.

**Retomada após morte — ajuste posterior de 30/10/2026:** somente `MAIN-SUB-01` mantém checkpoints das etapas concluídas. Morte antes da introdução mantém o aceite e o destino; morte durante o boss remove o alvo anterior, renova a tentativa e permite **Tentar novamente** pelo Journal ou pela mesma Rukia, sem repetir a introdução. Morte depois da vitória mantém o objetivo de retorno, sem refazer o boss. Estado e vínculo persistem no respawn e no save. A regra padrão de falha completa das demais quests não foi alterada; outras missões poderão definir políticas próprias.

Nesta conversa, "iniciar o jogo/mundo" passa a significar um personagem da classe Shinigami. Ao começar sem progresso, apenas `MAIN-SUB-01` aparece no Journal. Aceitar pelo Journal registra a missão e fixa a navegação na Rukia `karakura_inicio`, sem combate. Interagir abre o diálogo introdutório sobre Shinigamis e Hollows; confirmar "Enfrentar o Hollow" cria o Hollow Boss. A vitória libera a etapa de voltar à mesma instância de Rukia, sem concluir a missão. O diálogo final apresenta o papel de Shinigami temporário; "Receber Asauchi e concluir" entrega a espada uma única vez e libera `MAIN-SUB-02` no Journal. Objetivos futuros ficam ocultos até a etapa anterior terminar. Isso substitui a entrega da Asauchi no início descrita abaixo.

Uma Rukia inicial autorada tem preferência. Se o mundo não possui esse contexto conhecido, o servidor usa uma Rukia próxima sem contexto ou procura chão livre próximo ao jogador para criar uma persistente. Não altera terreno nem força carregamento de chunks; se não houver chão válido, tenta novamente. A instância é compartilhada no mundo, e o progresso é individual. FakePlayers não disparam essa provisão automática.

A versão 2 migra a definição antiga reconhecida de um único Hollow/Boss, guardando `01.json.pre-stages.bak`. Ao vincular o progresso no login, uma tentativa antiga incompleta reinicia na apresentação com nova identidade de tentativa; conclusões e marcas de entrega anteriores permanecem. Não remove espadas de personagens existentes. Outras definições customizadas não recebem migração genérica.

### Ajuste solicitado em 30/10/2026 — Catálogo somente da campanha

O catálogo ativo fica limitado às 28 missões de M13/10.32. `QuestDefaults` deixa de gerar as três missões antigas de demonstração e as oito quests de teste dos NPCs. O carregamento usa somente as duas sagas e os arquivos numerados previstos no catálogo oficial, sem reativar missões extras presentes em saves antigos. Arquivos antigos e registros pessoais permanecem em disco, mas essas quests não aparecem no diário, no HUD ou nas ofertas dos NPCs. Esta direção substitui a preservação ativa do boss legado da Rukia descrita no relatório histórico abaixo.

A história principal mantém a prioridade máxima do HUD, acima de outras categorias e de fixações antigas. O Mission Journal ordena explicitamente pelo catálogo: 10 missões Substituto, depois 18 Soul Society, independentemente da ordem de leitura ou sincronização. A visibilidade pessoal por pré-requisitos permanece; missões concluídas ficam no histórico em sua posição cronológica.

### Ajuste solicitado em 30/10/2026 — Asauchi da abertura

A seleção de Shinigami não entrega mais a Asauchi. No primeiro início válido de `MAIN-SUB-01` ("Um Hollow na área"), Rukia entrega uma Asauchi e o catálogo novo usa `bleachmod:hollow_boss`. A entrega ocorre somente após validar o local e criar o encontro. Inventário cheio deixa a espada no chão; uma Asauchi já carregada dispensa outra cópia. A marca de entrega persiste no progresso, inclusive após falha/reentrada, evitando obter espadas reiniciando a missão. Não é recompensa de conclusão nem libera forma/skill.

Os JSONs já existentes em saves continuam preservados por `writeIfMissing`: o novo alvo do catálogo vale para mundos que ainda não receberam essa definição. Não há migração automática do objetivo ou do progresso antigo; a mudança de entrega da espada se aplica também ao início válido da definição anterior. Personagens existentes não perdem espadas.

- Por direção explícita do usuário em 29/10/2026, todas as novas missões exigem personagem criado e raça `shinigami`, inclusive após falha e ao iniciar o encontro.
- Futuras missões ficam ocultas na conversa e no diário até seus pré-requisitos. Progresso e visibilidade pertencem ao jogador.
- `shinigami_substitute` tem 10 missões; `shinigami_soul_society`, 18. O marco final do primeiro arco libera o segundo. Sem quests de Aizen, Don Kanonji ou Yamamoto acrescentadas ao roteiro.
- História documentada tem prioridade máxima no HUD, acima das antigas sagas de demonstração e das sidequests, mesmo quando outra missão foi fixada manualmente.
- `quest_giver` é o personagem da oferta; `offer_context` restringe essa representação; `target_npc` + `target_context` + dimensão apontam para o local de execução. UUID identifica a instância concreta. Aceite no NPC fixa a instância real quando ela também é o destino.
- Oferta pelo NPC e início pelo painel são distintos. Na missão de Kon, Urahara oferece na loja, e o encontro é iniciado no diário perto do NPC contextual da escola. Eventos autônomos podem ser aceitos no diário; o servidor inicia somente dentro da área válida (48 blocos do ponto, NPC carregado).
- Interagir com o NPC não inicia combate involuntariamente. Ataques e spawns pertencem a uma tentativa pessoal com UUID próprio; alvos de tentativas anteriores não concedem progresso, e seu carregamento é recusado quando o proprietário online já reiniciou/concluiu a missão; outro jogador não obtém o crédito do alvo alheio.
- Não são concedidos SP, itens, skills, formas ou Mastery por estas definições: o roteiro ainda não define recompensas adicionais.
- O epílogo é texto após o objetivo válido, sem teleporte, mudança global de era ou remoção do NPC de outros jogadores.

## Cadastro e posicionamento

Os defaults são copiados uma vez para `<save>/bleachmod/sagas/` e `<save>/bleachmod/quests/<saga>/`; arquivos existentes não são sobrescritos. Os IDs antigos e o histórico do jogador são preservados. O boss de teste da Rukia permanece na Rukia sem contexto do Overworld. Ofertas legadas ficam restritas aos NPCs sem contexto, evitando que a Rukia da prisão ou da execução ofereça uma missão antiga de Karakura. Os defaults novos estão em `src/main/resources/data/bleachmod/quests/story_catalog.json`.

NPCs sem contexto continuam com seu catálogo legado; não recebem silenciosamente a identidade de uma casa, escola ou estrutura. A geração de estruturas pode gravar o NBT `QuestContext`. Para configurar um NPC de mapa já colocado, um operador usa:

```mcfunction
/bleachnpc context @e[type=bleachmod:npc_rukia,sort=nearest,limit=1] karakura_inicio
```

Para posicionar uma nova instância na estrutura/área preparada:

```mcfunction
/summon bleachmod:npc_rukia ~ ~ ~ {QuestContext:"karakura_inicio"}
```

O comando valida personagem, contexto e dimensão, e oferece sugestões. A dimensão de Karakura é `minecraft:overworld`; a da segunda campanha é `bleachmod:soul_society`. O modo História já declara essa dimensão; um mundo comum sem ela não recebe uma dimensão adicional por este cadastro. **Não foram editados mapas `.mca`, saves pessoais ou geração de estruturas nesta entrega.** Os pontos precisam ser colocados nas áreas correspondentes antes de jogar o roteiro completo. A bússola usa posições reais registradas, sem inventar coordenadas nem carregar chunks à força.

## Catálogo e integrações pendentes

| Ordem | ID narrativo | Chave persistente | Oferta | Contexto da oferta | Destino | Situação do objetivo |
|---:|---|---|---|---|---|---|
| 1 | MAIN-SUB-01 | shinigami_substitute:1 | rukia | karakura_inicio | rukia / karakura_inicio | Confronto Hollow implementado; requer área preparada |
| 2 | MAIN-SUB-02 | shinigami_substitute:2 | rukia | karakura_orihime | rukia / karakura_orihime | Boss ainda não registrado |
| 3 | MAIN-SUB-03 | shinigami_substitute:3 | sado_yasutora | karakura_periquito | sado_yasutora / karakura_periquito | Boss ainda não registrado |
| 4 | MAIN-SUB-04 | shinigami_substitute:4 | urahara | karakura_loja | kon / karakura_escola | Confronto Hollow implementado; requer área preparada |
| 5 | MAIN-SUB-05 | shinigami_substitute:5 | rukia | karakura_cemiterio | rukia / karakura_cemiterio | Boss ainda não registrado |
| 6 | MAIN-SUB-06A | shinigami_substitute:6 | uryuu | karakura_isca | uryuu / karakura_isca | Confronto Hollow implementado; requer área preparada |
| 7 | MAIN-SUB-06B | shinigami_substitute:7 | Painel | karakura_menos | uryuu / karakura_menos | Boss ainda não registrado |
| 8 | MAIN-SUB-07 | shinigami_substitute:8 | Painel | karakura_prisao | kon / karakura_prisao | Boss ainda não registrado |
| 9 | MAIN-SUB-PREP-01 | shinigami_substitute:9 | urahara | karakura_campo | urahara / karakura_campo | Aguarda percurso/ator/mecânica específica |
| 10 | MAIN-SUB-08 | shinigami_substitute:10 | urahara | karakura_passagem | urahara / karakura_passagem | Aguarda percurso/ator/mecânica específica |
| 11 | MAIN-SS-01 | shinigami_soul_society:1 | jidanbo | ss_portao | jidanbo / ss_portao | Boss ainda não registrado |
| 12 | MAIN-SS-GANJU-01 | shinigami_soul_society:2 | ganju | ss_rukongai | ganju / ss_rukongai | Boss ainda não registrado |
| 13 | MAIN-SS-02 | shinigami_soul_society:3 | kukaku | ss_canhao | kukaku / ss_canhao | Aguarda percurso/ator/mecânica específica |
| 14 | MAIN-SS-03A | shinigami_soul_society:4 | Painel | ss_chegada_ikkaku | ikkaku / ss_chegada_ikkaku | Boss ainda não registrado |
| 15 | MAIN-SS-03B | shinigami_soul_society:5 | Painel | ss_chegada_yumichika | yumichika / ss_chegada_yumichika | Boss ainda não registrado |
| 16 | MAIN-SS-04 | shinigami_soul_society:6 | Painel | ss_jirobo | uryuu / ss_jirobo | Boss ainda não registrado |
| 17 | MAIN-SS-05 | shinigami_soul_society:7 | hanataro | ss_galerias | hanataro / ss_galerias | Aguarda percurso/ator/mecânica específica |
| 18 | MAIN-SS-06 | shinigami_soul_society:8 | Painel | ss_renji | renji / ss_renji | Boss ainda não registrado |
| 19 | MAIN-SS-07 | shinigami_soul_society:9 | Painel | ss_oitava | kyoraku / ss_oitava | Boss ainda não registrado |
| 20 | MAIN-SS-08 | shinigami_soul_society:10 | Painel | ss_kenpachi | kenpachi / ss_kenpachi | Boss ainda não registrado |
| 21 | MAIN-SS-09 | shinigami_soul_society:11 | Painel | ss_prisao | rukia / ss_prisao | Interação implementada; requer ponto no mapa |
| 22 | MAIN-SS-10 | shinigami_soul_society:12 | Painel | ss_mayuri | mayuri / ss_mayuri | Boss ainda não registrado |
| 23 | MAIN-SS-PREP-01 | shinigami_soul_society:13 | yoruichi | ss_treino | yoruichi / ss_treino | Aguarda percurso/ator/mecânica específica |
| 24 | MAIN-SS-11A | shinigami_soul_society:14 | Painel | ss_tosen | tosen / ss_tosen | Boss ainda não registrado |
| 25 | MAIN-SS-11B | shinigami_soul_society:15 | Painel | ss_komamura | komamura / ss_komamura | Boss ainda não registrado |
| 26 | MAIN-SS-FRONT-01 | shinigami_soul_society:16 | Painel | ss_soifon | soifon / ss_soifon | Boss ainda não registrado |
| 27 | MAIN-SS-12 | shinigami_soul_society:17 | Painel | ss_execucao_rukia | rukia / ss_execucao_rukia | Interação implementada; requer ponto no mapa |
| 28 | MAIN-SS-13 | shinigami_soul_society:18 | Painel | ss_execucao_byakuya | byakuya / ss_execucao_byakuya | Boss ainda não registrado |

`bleachmod:story_*` são contratos de identidade para as futuras representações de combate, deliberadamente não registrados como mobs de substituição. Ausência do boss gera aviso e impede iniciar/concluir o confronto; não spawna porco, mob genérico ou NPC de serviço. O catálogo permanece válido no reload. A futura integração pode registrar o ID reservado ou migrar explicitamente a definição. Um Kenpachi moderno ou uma versão posterior do personagem não substitui sua representação deste arco. Byakuya permanece uma única missão; fases Shikai/Bankai/Senkei pertencem ao futuro boss, sem três recompensas.

Ururu/Urahara preservam dois objetivos tipados (`PURSUIT`, `HIT`), e a documentação mantém 60 segundos / 30% como referência aprovada. O exercício, seus atores e o checkpoint de Ururu ainda não são executados nesta entrega. Expedição/retorno, esfera/lançamento, galerias e circuito também permanecem bloqueados até terem percursos e validadores reais; não foram reduzidos a falar com NPC ou esperar parado. Essas pendências já constam das fichas M13 e são adicionais aos bosses ausentes. A presença do JSON não significa campanha inteira jogável.

A composição de três Hollows em MAIN-SUB-06A é um default provisório de implementação; a quantidade é editável no JSON e precisa de calibração. Distância de início (48 blocos) e busca local de spawn (6–20 blocos, piso firme/espaço livre, somente chunks carregados) são parâmetros de integração, não valores canônicos. O sistema não altera terreno e pode recusar início sem espaço. Posicionar NPCs de combate exige área adequada ao encontro; a validação de colisão não certifica arenas/mapas de terceiros.

## Compatibilidade e verificação

Protocolo de rede `2.36`: servidor e clientes devem usar esta versão juntos. Contexto de entidade, destino, início e progresso são persistidos/sincronizados. Os novos NPCs Jidanbō, Ganju, Kūkaku, Hanatarō, Ururu e Kon usam Steve provisoriamente; skins existentes não foram trocadas. NPC e boss permanecem entidades distintas.

[Relatório e evidências](../qa/quests/2026-10-29-story/README.md).
