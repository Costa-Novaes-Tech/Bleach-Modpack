# M06a — Estado automático de combate

## 1. Estado do bloco

| Campo | Estado em 23/10/2026 |
|---|---|
| Estado | `ESPECIFICADO CONCEITUALMENTE` no recorte funcional COV-06; parâmetros, fichas e implementação pendentes |
| Novas escolhas | M06-CS-01A/B/C/D `DECIDIDOS`; nenhuma resposta pendente deste bloco |
| Heranças | M05-02B; M06-01B/C; RAC-30A/B/C/D e RAC-31A; contratos de encontro, viagem, evolução e reset |
| Escopo | Entrada, renovação e saída de Combat State; confronto, ameaça efetiva e perigo ambiental |
| Implementação/teste/aceite | Nenhum nesta rodada |

Entrada: [revisão de cobertura M01–M10](00-revisao-cobertura-m01-a-m10-2026-10-23.md). [M06](06-habilidades-e-combate.md) continua responsável pela execução; este complemento aprofunda o significado do estado automático, sem reabrir o modo manual.

## 2. Objetivo

Definir quando o personagem está envolvido numa luta, quais fatos mantêm esse envolvimento e quando pode voltar a atividades que exigem estar fora de combate. Não deixar a programação escolher essa política apenas por um número de segundos desde o último dano.

## 3. Experiência do jogador

Conseguir entender por que viagem, recarga de Gintō, evolução ou reset estão indisponíveis. Bloquear ou esquivar não deve significar que uma luta em andamento desapareceu; um inimigo que apenas olha de trás de uma barreira intransponível não deve criar um bloqueio eterno. Direção aprovada em A/C, sem fixar o período final de saída.

## 4. Base e limite da pesquisa

Heranças documentais lidas: M05/24, M06/5, M04/RAC-30–31, M09/8.4–8.5/8.11 e revisão COV-06. A obra inspira ameaças, técnicas e sentidos, mas não estabelece um Combat State universal que controle menus, regeneração e serviços. As escolhas abaixo são regras de produto do mod. Nenhuma nova alegação canônica, biblioteca ou API foi pesquisada nesta abertura.

## 5. Adaptação para Minecraft

Separar **interface manual**, **estado automático**, **perigo local** e **participação em encontro preparado**. O jogador pode fechar a Combat Hotbar durante uma luta; pode sofrer um risco ambiental fora de qualquer luta; pode participar de um boss com intervalos entre golpes. Uma única marca de dano não resolve essas quatro situações.

## 6. Escopo e fronteiras

O estado pertence a cada personagem e será autorizado pelo servidor. Estar na mesma party, região ou dimensão não o espalha automaticamente. A intenção é produzir uma regra comum para as quatro origens; corpo/compatibilidade dos ataques e condições do encontro continuam com seus contratos próprios.

Não definir neste bloco preços, tempo final de saída, regeneração, punição por desconectar, PvP obrigatório, reposicionamento de entidades ou sistema global de ameaça. Limiares de distância/tempo precisam de calibração em M05/M06/M09/M15. Continuidade da marca e reavaliação ao morrer, retornar ou viajar foram aprovadas em D; não são deduzidas apenas das regras de cooldown. Interrupção de preparos/manifestações e disparos autônomos mantêm M06-11U/V/W, sem nova votação.

## 7. Modelo conceitual

| Informação | Função | Limite |
|---|---|---|
| Evento de envolvimento | Iniciar/renovar a marca pessoal quando A permitir | Não concede dano, alvo ou recompensa |
| Ameaça atual efetiva | Impedir que uma luta viável desapareça só por ausência momentânea de impacto, conforme C | Não usar só UUID, proximidade ou intenção da IA |
| Condição hostil em curso | Considerar ataque/controle ainda atuante conforme sua ficha e A/C | Efeito visual ou presença antiga não renova por omissão |
| Participação em encontro | Manter compromisso da tentativa conforme sua ficha e C | Aceitar uma quest não equivale a iniciar uma prova |
| Perigo ambiental | Validar segurança/execução da atividade conforme B | Não precisa significar confronto com agressor |
| Janela sem conflito | Autorizar saída depois de cessar o envolvimento, conforme C | Não apagar cooldown, dívida de recurso ou consequência |

As informações podem ser avaliadas sem criar novas barras, atributos ou uma ordem universal de alvos. O jogador recebe uma explicação do bloqueio, sem revelar a posição de um inimigo que não percebe.

## 8. Regras e estados das decisões

### 8.1. M06-CS-01A — Envolvimento real, inclusive ataque evitado e apoio efetivo — DECIDIDO

**Resposta em 23/10/2026:** “Envolvimento real, mesmo sem perder Vida (recomendado)”.

**Regra aprovada:** iniciar/renovar Combat State por uma ofensiva válida contra outro ser elegível, por sofrer uma tentativa real de ataque capaz de afetar o corpo atual, ou por entrar efetivamente num confronto iniciado. Não exigir perda de Vida: golpe bloqueado, esquivado ou controle hostil válido também pode caracterizar envolvimento. A tentativa precisa ameaçar o personagem por alcance, trajetória/área e compatibilidade; anunciar um ataque contra ele do outro lado do mapa não basta.

Para quem ataca, a ofensiva precisa ter execução admitida e alvo/área de interação legítimos. Um golpe pode errar porque o alvo reagiu, sem deixar de ser confronto. Pedido recusado, fase de preparo cancelada antes de qualquer ato hostil, ataque no vazio sem ser ameaçado e interação incapaz de afetar aquele alvo não criam combate só pela animação. Não exigir travamento de mira ou informação secreta de alvo para avaliar ataques manuais; servidor valida o evento sem revelar coordenadas ocultas.

Cura/proteção que efetivamente ajude um participante nessa luta também envolve quem a prestou. Curar sem efeito, ter um aliado na party, estar perto ou manter um benefício irrelevante não bastam. O evento não divide SP/loot, não permite PvP proibido nem transfere hostilidade entre todos os integrantes. Ataques a criaturas passivas que sejam alvos válidos podem envolver o agressor; não precisa que a presa tenha atacado primeiro.

RAC-30B/D permanece: um golpe comum de zumbi sem capacidade de afetar o corpo espiritual não é ameaça efetiva por esse meio. A IA pode detectar/tentar atacar conforme C de M04; isso não prova envolvimento real do alvo. Armas/técnicas espiritualmente compatíveis, outros meios elegíveis do repertório ou uma ofensiva válida do próprio jogador podem produzir o evento pertinente.

Abrir a interface, manifestar uma forma sem ofensiva, sentir Reiatsu ou usar mobilidade disponível fora de combate não iniciam luta por si. Condições ofensivas contínuas devem declarar a ação/efeito real e sua autoria quando aplicável; não criar combate por partículas ou manter um agressor fictício para todo efeito ambiental.

**Alternativa não adotada:** entrar/renovar apenas por dano efetivo causado/recebido e início de confronto preparado. É mais simples, mas deixa guarda, esquiva, controle e suporte eficaz sem envolvimento automático entre impactos.

### 8.2. M06-CS-01B — Perigo ambiental separado de confronto — DECIDIDO

**Resposta em 23/10/2026:** “Perigo separado do estado de combate (recomendado)”.

**Regra aprovada:** queda, lava, afogamento e outros perigos sem origem hostil não ativam/renovam Combat State por si. Se houver perigo atual ou dano/condição incompatível com a operação, viagem, reset ou evolução ficam indisponíveis **mesmo fora de combate**, conforme a ficha da operação. Não basta um indicador OFF para se transformar num local impróprio ou fugir de dano em curso por um serviço.

Isso não torna todo dano ambiental elegível para todo corpo: natureza/compatibilidade continuam pertinentes. Também não confunde hostilidade e ambiente: empurrão hostil, queimadura aplicada por um ataque e armadilha ofensiva podem ter autoria/efeito válido conforme A e a ficha; não perdem o vínculo apenas porque o dano final ocorre no ambiente. Não atribuir um agressor por adivinhação ou renovar combate para sempre por dano passado.

**Alternativa não adotada:** qualquer dano elegível também ativa o estado, inclusive ambiental, preservando as demais validações da atividade. Simplifica o indicador, mas faz cair de uma altura ou sofrer fome significar combate por algum tempo, mesmo sem adversário.

### 8.3. M06-CS-01C — Janela sem conflito e sem ameaça efetiva; compromisso de encontro — DECIDIDO

**Resposta em 23/10/2026:** “Cessar conflito e ameaça; respeitar encontro (recomendado)”.

**Regra aprovada:** sair depois de uma breve janela sem eventos de envolvimento, sem efeito hostil atuante que mantenha a luta e sem ameaça atual efetivamente capaz de engajar. O período final será calibrado, sem escolher um número universal nesta proposta. Uma pausa normal entre os golpes, cooldown ou Recovery do inimigo não encerram sozinhos uma luta ainda viável.

Usar informação atual legítima, hostilidade dirigida e solução real de alcance/percurso/ataque conforme M09. Proximidade, raça/facção inimiga, pista antiga ou mob selecionando um alvo não bastam. Um agressor confinado sem rota e sem ataque viável não renova indefinidamente o estado. Perder contato e buscar por uma pista não equivale a saber a posição atual; um obstáculo comum tampouco garante fuga se ainda existir ataque ou aproximação legítima.

Uma ofensiva nova válida pode renovar o estado mesmo de uma posição vantajosa. Não punir arena construída com teleporte, cura do mob, invulnerabilidade ou proibição de grind. Encerrar Combat State não obriga a criatura a esquecer o que viu nem concede vitória ou recompensa; M09 conserva seu ciclo de busca/retorno.

Encontros preparados iniciados mantêm seus participantes em combate durante a tentativa até término ou saída válida definidos na ficha. Um canto sem alcance ou intervalo de fase não encerra o compromisso do boss por omissão. M09 conserva falha coletiva, prazo de abandono, interrupção por descarregamento/reinício e checkpoints explícitos. Não criar uma saída de encontro pelo comando de fechar a interface.

Depois de encerrar o confronto, ameaça ou efeito pertinentes, aplica-se a janela restante; terminar uma quest não apaga outro adversário/efeito real ainda em curso. Números e comunicação pertencem às fichas, sem acrescentar uma segunda penalidade de recuperação.

**Alternativa não adotada:** sair ao completar o prazo sem novas ações/golpes, mesmo com ameaça capaz de atacar ou encontro preparado ainda ativo. Evita avaliação de ameaça, mas permite passar a estado OFF durante luta por se manter sem receber impactos.

### 8.4. M06-CS-01D — Preservar a janela restante e reavaliar o contexto — DECIDIDO

**Ampliação decidida em D:** estender a preservação do tempo legítimo à janela do Combat State. M06-11V já aprovou o relógio de cooldowns/recuperações; não aprovou automaticamente toda regra do estado contextual. U já encerra as execuções deliberadas e W já separa efeitos autônomos/sustentados. Não repetir essas decisões nem tratar a marca automática como transformação temporária.

**Resposta em 23/10/2026:** “Preservar janela e reavaliar o contexto (recomendado)”.

**Regra aprovada:** morte, desconexão, mudança de dimensão válida e reinício não zeram a janela de combate ainda restante. Contá-la pelo tempo de simulação autorizado do mundo: avança enquanto o servidor simula, mesmo offline; pausa se o mundo/servidor parar. Não usar horário civil, FPS, avanço da noite por cama ou troca da referência entre dimensões. Preservar o restante real, sem reiniciar a duração integral nem criar uma multa adicional pela transição.

Ao retornar/reaparecer ou assumir o novo local válido, conferir eventos e condições atuais. Ameaça de envolvimento anterior só continua quando ainda houver informação/hostilidade/meio legítimos para engajar nesse contexto; memória antiga não teleporta o agressor ou rastreia o jogador entre dimensões. Novos eventos de A ou encontro/efeito ainda válidos podem manter/renovar o estado. Se não houver contexto atuante e a janela tiver terminado, retornar fora de combate. Não transformar a mera proximidade de outro inimigo numa entrada diferente da escolhida em A.

Morrer/sair não faz os demais participantes abandonarem o boss: M09-06B mantém a tentativa com participantes aptos. O personagem ausente/morto não mantém a tentativa sozinho por UUID; morte, saída válida e interrupção são avaliadas pela ficha. Reinício/descarregamento encerra a tentativa dirigida como interrompida conforme M09-08A; não restaurar o compromisso antigo no retorno. Reentrada, substituição e crédito de vitória continuam com M09/M15, sem autorização automática por preservar a janela.

Mudança de dimensão válida não significa que passagens comuns passem a funcionar em combate: M10-02B continua exigindo estado OFF, além das demais condições. D trata continuidade quando a transição legitimamente acontecer, sem criar fuga por portal. Reset RAC-31A continua fora de combate/encontro; a operação não é um atalho para zerar a marca em andamento.

**Alternativa não adotada:** limpar a janela nessas transições e avaliar apenas a situação nova. Simplifica a retomada, mas torna sair/voltar ou morrer uma maneira de antecipar o término da espera de combate. Não altera por si as penalidades, cooldowns ou formas já aprovados, e precisaria de outra resposta para esse uso de limpeza.

**Pergunta:** Para a marca de combate, recomendo preservar a janela restante após morrer, desconectar ou mudar de dimensão validamente. Ela conta pelo tempo de simulação do mundo e pausa com o servidor parado. Ao voltar, o jogo reavalia ameaças, efeitos e encontro atuais: um agressor antigo não segue por memória, mas um conflito ainda válido pode manter ou renovar o estado. Isso não restaura ataques/formas nem autoriza reentrada em boss. Adotamos essa continuidade ou limpamos a marca nessas transições e consideramos apenas a situação nova?

**Opções:** “Preservar janela e reavaliar o contexto (recomendado)” / “Limpar a marca e avaliar só a situação nova”. **Resposta em 23/10/2026:** “Preservar janela e reavaliar o contexto (recomendado)”. [Pergunta integral e heranças](historico/2026-10-23-proposta-m06-cs01d-continuidade.md).

### 8.5. Aplicação de A/B/C a efeitos e perigos — integração das decisões aprovadas

Um novo impacto/aplicação ofensiva elegível conta conforme A; controle ou efeito hostil ainda atuante mantém o envolvimento que sua ficha declarar conforme C. A autoria relevante e a validade pertencem ao evento/efeito, sem exigir enxergar o emissor para validar o resultado e sem revelar sua posição ao alvo. Ataque sem efeito ou sem compatibilidade não vira envolvimento só por registro de dono.

W permite que certos disparos autônomos já emitidos continuem na região ativa. Um impacto real desses ataques continua sujeito a A e à autorização do alvo, mesmo após a saída do emissor; a responsabilidade deve ser registrada sem simular uma nova técnica offline. Um evento atribuído ao emissor pode atualizar a marca pertinente; não concede treino, SP, quest, loot ou regeneração offline por associação. M09/M12/M15/M16 classificam crédito e identidade da jornada, sem transferir resultados de uma jornada apagada a outra só por manter a mesma conta.

Encerrar canal/condição por perda de requisito remove sua contribuição atual; não apaga os eventos anteriores nem o restante da janela. Condições independentes já aplicadas conservam o ciclo próprio aprovado em W; U não concede cura universal no login/morte. Após acabar o vínculo ofensivo legítimo, um risco remanescente do mundo pode continuar impedindo uma operação por B, sem inventar agressor eterno ou manter combate por cada bloco que alguém colocou no passado.

Classificar queimaduras, venenos, armadilhas, empurrões e efeitos mistos **quando suas fichas forem oferecidas**: componente ofensivo, componente ambiental, alvo compatível, autoria, duração e encerramento. Reutilizar esses dados nos consumidores; não escolher nova imunidade, dano ou limpeza para preencher a tabela de Combat State. Valores e classificação concreta ainda são entregas antes da implementação, sem substituir as regras gerais por decisões ad hoc no código.

### 8.6. Fechamento funcional do retorno COV-06 — 23/10/2026

| Lacuna de funcionamento | Resolução aprovada / entrega restante |
|---|---|
| Entrada/renovação sem exigir perda de Vida | A: ofensiva/tentativa real compatível, apoio efetivo e confronto iniciado; sem party ou interface automáticas |
| Dano ambiental e operação perigosa | B: separar perigo de confronto e validar a operação, sem criar imunidade ou agressor fictício |
| Agressor sem solução e saída | C + M09: ameaça precisa de informação/meio atuais; janela sem conflito, sem apagar o ciclo da criatura |
| Encontro preparado | C + M09-06/08: compromisso, término/saída e interrupção próprios; sem reentrada presumida |
| Morte/logout/dimensão/reinício | D: preservar janela pelo relógio de simulação e reavaliar contexto, sem restaurar execuções ou encontro interrompido |
| Efeitos já emitidos e autoria | A/B/C + U/W/8.5: considerar efeitos reais válidos; classificação concreta e crédito antes da entrega de cada ficha |
| Consumidores | Referências em M04/M05/M06/M07/M08/M09/M10; números, comunicação e implementação por responsáveis |

A/B/C/D aprovados; nenhuma pergunta pendente deste retorno. COV-06 está resolvido no recorte funcional conceitual, sem certificar números, código, todas as fichas de efeitos ou teste de jogo. Não delegar novas escolhas de funcionamento à programação por omissão; um caso que contrariar os contratos retorna ao dono antes de ser implementado.

## 9. Integrações e consumidores

| Responsável | Contrato/entrega |
|---|---|
| M04/M08 | Reset RAC-31A no atendimento e fora de combate/encontro; atendimento protegido não garante segurança do jogador |
| M05 | Consumir o estado para regeneração sem alterar taxas pela abertura deste documento; calibrar tempos e recursos |
| M06/M07 | Classificar execução/efeitos; recarga de Gintō e evolução usam o estado automático, sem confundir modo manual |
| M09 | Informar sentidos, ameaça efetiva e participação/término/abandono da tentativa; classificação real dos meios de ataque |
| M10 | Passagens comuns fora de combate; validar também corpo, espaço, acesso e perigo pertinente conforme B aprovado |
| M14 | Comunicar motivo de bloqueio e distinguir combate/perigo/compromisso de encontro, sem exigir nova HUD agora |
| M15/M16 | Autoridade, sincronização, ciclo de vida e abuso/PvP; não inventar uma punição por logout por associação |

M10 não chama uma passagem segura apenas porque a área de chegada cabe; esse requisito é diferente do estado de saída. Do mesmo modo, Combat State OFF não concede acesso territorial ou compra de skill.

## 10. Matriz de casos — A/B/C aprovados

| Situação | Resultado recomendado | Base |
|---|---|---|
| Abrir Combat Hotbar sem adversário | Não inicia estado automático | Herança M06-01B/C |
| Ataque legítimo esquivado/bloqueado | Envolve participantes pertinentes, mesmo sem perda de Vida | A aprovado |
| Zumbi usa golpe comum contra corpo espiritual que ele não pode ferir | Tentativa da IA não basta para marcar o alvo | RAC-30B/D + A aprovado |
| Jogador usa ofensiva compatível contra presa válida | Envolve o atacante, mesmo se a presa for passiva | A aprovado |
| Curar/proteger efetivamente participante da luta | Envolve quem ajudou; não toda a party | A aprovado |
| Queda ou lava sem origem hostil | Perigo próprio; sem combate automático só pelo dano | B aprovado |
| Agressor sem nenhum meio válido de chegar/atingir | Não mantém ameaça efetiva indefinidamente; eventos anteriores ainda respeitam a janela | C aprovado |
| Inimigo ainda pode aproximar/atacar legitimamente | Não sair só porque o jogador evita golpes por alguns instantes | C aprovado |
| Prova iniciada com boss em intervalo de fase | Mantém compromisso do encontro | C aprovado |
| Confronto acabou, mas outro ataque/efeito hostil continua válido | Resolver o envolvimento remanescente antes da saída | A/C aprovados |

## 11. Visão completa e entregas de conteúdo

As fichas precisam identificar ameaça, compatibilidade e autoria dos efeitos que oferecem. Uma receita global de “qualquer mob por perto” ou “qualquer dano” não substitui esses dados. Treino acompanhado pode ser um encontro próprio sem conceder agressividade permanente ao mestre; fonte protegida e representação de combate continuam distintas.

Morte/logout/retorno/dimensão/reinício foram aprovados em D, preservando U/V/W e o ciclo de encontro de M09. Atribuição de efeitos segue 8.5 e as fichas pertinentes; comunicação aos consumidores continua necessária. Não usar este escopo para reabrir formas, exigir novos ranks ou decidir políticas de punição multiplayer ainda não aprovadas.

## 12. Decisões abertas

M06-CS-01A/B/C decididos em 23/10/2026, sem respostas pendentes dessas três escolhas. [Perguntas e respostas integrais](historico/2026-10-23-propostas-m06-cs01abc-estado-de-combate.md) preservadas. A — eventos reais/apoio; B — ambiente separado de confronto; C — saída por cessar conflito/ameaça e respeitar encontro. **M06-CS-01D decidido em 8.4**, preservando janela restante e reavaliando o contexto entre transições; nenhuma resposta pendente deste bloco. Não confundir a marca automática com forma temporária ou presumir que o relógio de cooldown já aprovou a extensão. Fichas de efeitos, números, abuso/PvP e gravação mantêm suas entregas.

## 13. Critérios futuros de validação

Bloquear/esquivar não libera recarga/viagem em luta ainda vigente; tentativa incapaz de afetar não trava um corpo espiritual; curador efetivo participa sem tag automática da party; perigo pode impedir uma operação sem inventar adversário; agressor confinado não mantém bloqueio eterno; boss não é abandonado por fechar Combat Mode; terminar um encontro não apaga outro conflito; posição/raça/recursos e relógios continuam seus contratos. Cenários documentais derivados das escolhas aprovadas; nenhum executado.

## 14. Impacto técnico conhecido

Abertura documental, sem auditoria de código, eventos de dano/IA, relógio ou cliente nesta rodada. Não confirma que o runtime atual ofereça os sinais necessários nem escolhe biblioteca, pacote ou armazenamento. Nenhuma alteração de código, mundo, save, build ou teste de jogo.

## 15. Continuidade

A/B/C/D aprovados e referências integradas em M04/M05/M06/M07/M08/M09/M10; retorno funcional COV-06 consolidado conceitualmente, preservando as entregas executáveis das fichas. Depois, complementos funcionais necessários de M08/M09 e revisão do checkpoint autorizado para publicação. M10 continua em especificação de conteúdo; não declarar o jogo completo especificado ou implementado porque este retorno de sistema avançou.
