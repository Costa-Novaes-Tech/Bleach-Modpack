# Histórico — M04 antes do adiamento de múltiplos personagens — 22/10/2026

Trecho anterior preservado literalmente. A direção vigente é REV-RAC27-01 no M04 ativo.

## Quarto bloco — Criação e continuidade — abertura em 22/10/2026

**Estado: EM ESPECIFICAÇÃO. RAC-26/RAC-27 e RAC-28A/B DECIDIDOS em 22/10/2026. RAC-29A/B, troca entre personagens, são PROPOSTOS e aguardam resposta.** M04 permanece em especificação: condições de troca e continuidade ainda precisam de fechamento. São adaptações de produto para Minecraft, sem atribuir seleção de personagens ou reset ao cânone de Bleach.

### Base já decidida

- RAC-02: escolha informada da origem e prólogo próprio, com fantasia, condição inicial e permanência compreensíveis.
- RAC-04/RAC-06: origem permanece; transições narrativas compatíveis e morte de gameplay têm regras próprias. Reiniciar/criar personagem é uma operação separada.
- M03/STR-06: cronologia do mundo e progresso pessoal são distintos. Um personagem novo não reinicia a era do servidor.
- REV-SP-02/M05-11.2: skills e transformações são compradas com SP; Mastery é desenvolvida pela prática. Não revisar esse contrato nesta rodada.
- M07-09C: trocar o Fullbring principal no mesmo personagem é um processo específico; não equivale a criar outra identidade nem autoriza troca livre de raça.

### RAC-26 — Em que momento a escolha inicial é confirmada?

**DECIDIDO em 22/10/2026. Resposta:** “Confirmar antes do prólogo (recomendado)”. Permitir rever a seleção até uma confirmação explícita, antes de começar o prólogo racial. Confirmar cria a identidade com aquela origem; o prólogo apresenta e ensina a escolha já feita. Não há uma segunda seleção de raça como recompensa ao terminar a introdução.

O resumo anterior à confirmação deve explicar a fantasia, o começo e a permanência da origem. Não precisa expor o catálogo inteiro de poderes ou exigir a escolha imediata de Zanpakutō, linhagem, Schrift ou Fullbring canônico. Voltar entre alternativas antes de confirmar não gera compras, recompensas ou progresso jogável.

| Alternativa | Efeito para o jogador | Avaliação |
|---|---|---|
| A — Confirmar antes do prólogo | Escolhe com informação e começa a viver a origem confirmada | Recomendada: separa seleção e jornada, evitando refazer uma introdução por seleção acidental |
| B — Confirmar somente depois de experimentar o prólogo | Pode testar uma abertura antes de assumir a origem | Exige definir o que acontece com itens, recompensas, ações no mundo e progresso ao voltar; experiência prévia não está especificada |

M04 define o compromisso da escolha; M14 define apresentação e comandos; M13 detalha a introdução; M16 define gravação e retomada. Sair ou falhar na confirmação não pode ser confundido com ter completado uma origem. A retomada precisa preservar a origem já confirmada e o andamento da introdução, sem tratar desconexão como nova seleção ou conceder novamente recompensas concluídas. Gravação e recuperação executáveis ficam em M16.

### RAC-27 — Como experimentar outra origem no mesmo mundo?

**DECIDIDO em 22/10/2026, com complemento do usuário em RAC-28A. Resposta integral:** “a opção recomendada mas com a possibilidade de resetar em algum npc”. Permitir mais de um personagem por conta no mesmo mundo/servidor, com **um único personagem ativo por vez**. Cada personagem mantém origem e progresso próprios: atributos, saldo e compras de SP, aprendizados, Proficiências, Masteries, poderes, campanha pessoal e reputação pessoal. Criar outro não copia essas conquistas nem apaga o anterior; voltar ao anterior recupera a sua jornada, sem somá-la à do outro.

| Alternativa | Benefício | Custo ou limite |
|---|---|---|
| A — Personagens separados no mesmo mundo | Permite conhecer outras raças e continuar com os mesmos amigos, preservando jornadas anteriores | Exige identidade persistente por personagem, separação de inventário/equipamento e regras de troca, propriedade e recompensas repetidas |
| B — Um personagem por conta em cada mundo/servidor | Reduz a complexidade de identidades e integrações com Minecraft e outros mods | Experimentar outra origem exige outro mundo/servidor ou uma política própria de reinício; esta alternativa não aprova apagar o personagem |

**Recorte da recomendação A:** o personagem anterior não permanece como NPC, pet ou combatente simultâneo. Inventário e equipamento não são copiados ou automaticamente carregados para a nova identidade. Regras normais de baús e comércio precisam ser tratadas em M11/M15; separação de progresso não equivale a proibir toda transferência física de itens. Era, terreno, construções e acontecimentos do mundo continuam pertencendo ao mesmo mundo compartilhado.

**Impacto técnico conhecido:** Minecraft e integrações de outros mods podem identificar o jogador pela conta. Separar personagens exige especificar quais dados são da conta, do personagem e do mundo; não se presume que tudo já seja isolado. Quantidade de personagens, disponibilidade na primeira entrega e suporte a mods externos ainda não foram escolhidos. Aprovação conceitual não declara esse suporte implementado.

### RAC-28 — Reset de personagem por NPC

#### RAC-28A — Existência do serviço

**DECIDIDO em 22/10/2026**, pelo complemento da resposta de RAC-27: “a opção recomendada mas com a possibilidade de resetar em algum npc”. Haverá a possibilidade de solicitar reset por um NPC, além de manter personagens separados. NPC, preço e frequência continuam pendentes; o alcance das perdas foi aprovado posteriormente em RAC-28B. A fala não é uma solicitação para apagar qualquer personagem/save real nesta sessão.

**Integrações:** M04 define o que reinicia; M08 define o prestador e sua disponibilidade; M11 trata itens/custo do serviço; M14 apresenta a confirmação; M15 trata regras de servidor e recompensas repetidas; M16 executa e persiste a operação com recuperação em caso de falha. O NPC solicita a operação ao sistema responsável, conforme o mapa modular.

#### RAC-28B — Alcance do reset

**DECIDIDO em 22/10/2026. Resposta:** “Reset Bleach; preservar itens comuns e XP (recomendado)”. [Proposta anterior preservada](historico/2026-10-22-proposta-m04-rac28b-reset.md). Reiniciar a **progressão de Bleach do personagem escolhido**, voltando às condições de um personagem novo e permitindo selecionar novamente a origem conforme RAC-26. Isso inclui atributos do mod, saldo e compras de SP, aprendizados, Proficiências, Masteries, poderes, evolução, campanha e reputação pessoais. Não há reembolso dos SP gastos ou transferência desse progresso para outra identidade. Os demais personagens, o mundo, suas construções e a era compartilhada permanecem.

Preservar inventário comum e experiência vanilla desse personagem, conforme a alternativa A aprovada. Manter um item físico não preserva poder vinculado, técnica, requisito ou compra apagados pelo reset. Equipamentos espirituais, itens de quest e outros itens vinculados precisam de destino próprio em M11/M12; não prometer destruição de todos, nova vinculação automática, recall, duplicação ou conservação de acesso especial. Armazenamento externo e propriedade continuam com suas regras; o reset não oferece restauração global de baús ou construções.

| Alternativa | Consequência | Avaliação |
|---|---|---|
| A — Reiniciar progressão Bleach, preservar inventário comum e XP vanilla | Permite recomeçar a jornada espiritual mantendo os recursos da experiência Minecraft; itens especiais dependem da ficha | Recomendada para preservar a exploração e construção acumuladas |
| B — Reiniciar também inventário e XP vanilla do personagem | Recomeço pessoal mais amplo, incluindo perdas materiais; baús externos, mundo e outros personagens não são apagados | Exige comunicar o alcance com precisão; não elimina por si só uso de armazenamento externo |

**Condições do pacote aprovado:** o reset é do personagem escolhido, exige confirmação explícita e ocorre fora de combate/encontro; preserva os demais personagens, mundo e construções. **Integridade a especificar em M16:** validar a identidade afetada e a ausência de execução comprometida; apresentar as perdas antes de confirmar; concluir integralmente ou preservar o estado anterior em caso de falha, sem recompensas parciais. A mudança de origem por reset recomeça a jornada pessoal, sem transformar uma identidade avançada em outra mantendo suas conquistas. Identificador, histórico técnico, itens vinculados, consequências persistentes, quotas de recompensas e retomada numa era avançada precisam de contratos explícitos antes da implementação. Não considerar reset um reembolso de SP ou um mecanismo automático de recuperação de Bankai/Letzt Stil.

**Pendências:** NPC/preço/frequência, destino de itens vinculados, protocolo de confirmação/falha e retomada da campanha na era atual. O reset volta às condições iniciais da origem escolhida; não entrega pontos ou atributos literalmente zerados quando o começo da origem tiver valores próprios. Criar outro personagem continua preservando o anterior; resetar explicitamente este personagem substitui sua progressão Bleach, sem prometer recuperá-la por trocar de slot.

### RAC-29 — Troca entre personagens

**PROPOSTO em 22/10/2026. Sem resposta registrada.** A troca conserva cada jornada, enquanto o reset RAC-28B reinicia a jornada escolhida. Este bloco não pergunta novamente sobre personagens separados ou sobre o NPC de reset.

#### RAC-29A — Acesso e condições da troca

**Recomendação:** oferecer uma tela de seleção acessível durante o jogo, com pedido validado pelo servidor. Trocar apenas fora de combate/encontro, sem perigo imediato, dano contínuo ativo ou execução de técnica em andamento. Abrir a tela não pausa o servidor nem torna o personagem invulnerável; se as condições deixarem de valer, a troca é recusada e a identidade atual ainda válida permanece ativa.

O pacote proposto encerra preparos e manifestações temporárias do personagem que sai, conforme os retornos dos perfis de M06/M07: formas temporárias desligadas, arco dissipado, Blut desligado e presença Natural. Não restaura Vida/Stamina/Energia, itens, SP ou domínio. Conquistas, evoluções permanentes e consequências próprias continuam vinculadas à identidade de origem; não transferi-las ao outro personagem nem curá-las ao retornar. Essa inclusão da troca de personagem como evento de saída é proposta aqui para o conjunto, sem repetir aprovação por raça.

Cooldowns/Recovery já iniciados continuam pelo relógio de simulação de M06-11V; guardar uma identidade não oferece treino ou regeneração por inatividade. Ataques autônomos, efeitos dependentes e montagens seguem sua responsabilidade original, conforme M06-11W/M07-C12: trocar não recolhe automaticamente projéteis nem concede seus resultados ao personagem que entrou. Créditos/recompensas e efeitos persistentes exigem ficha M09/M12/M15/M16; nenhuma duplicação de recompensa ou transferência de autoria é presumida.

| Alternativa | Benefício | Limite |
|---|---|---|
| A — Tela de seleção em condições seguras | Permite alternar jornadas sem depender de encontrar um prestador em cada origem/era | Exige validar segurança e preservar os estados de cada identidade; recomendada |
| B — Troca somente por NPC apropriado | Dá um ponto físico ao serviço e controla onde o pedido pode ocorrer | Exige acesso viável de todas as origens/eras ao serviço; reset por NPC já aprovado não decide esta opção automaticamente |

#### RAC-29B — Local de retorno

**Recomendação:** guardar posição e dimensão próprias de cada personagem e retomá-lo onde ele havia parado. O personagem que sai não transporta o próximo para sua posição, nem compartilha inventário, recursos ou permissões de acesso. Personagem novo segue sua abertura racial; não herda o local avançado do anterior.

| Alternativa | Efeito | Avaliação |
|---|---|---|
| A — Cada personagem retorna ao próprio local salvo | Mantém jornadas e viagens independentes; alternar não desloca o outro avatar pelo mapa | Recomendada |
| B — Personagem escolhido assume o local de quem saiu | Facilita reunir personagens na mesma base | Pode transportar iniciantes e corpos incompatíveis a dimensões/locais não conquistados; exige regras próprias para impedir atalhos |

**Validação proposta:** conferir dimensão disponível, acesso e espaço para o corpo-base no destino antes de concluir. Se o retorno salvo estiver inválido, recusar a troca sem remover a identidade atual ainda válida. Local perdido por alteração do terreno/mapa/dimensão precisa de uma via de recuperação em M10/M16 antes de entregar o recurso; esta proposta não escolhe teleporte corretivo, remoção de blocos ou reset de personagem como solução.

**Integração e critérios candidatos:** M14 fornece seleção/feedback; M15 define condições verificáveis de segurança e limites de uso; M16 garante que apenas uma identidade fique ativa e que posição, inventário e recursos sejam salvos/carregados uma vez. Conferir futuramente: recusa em combate/queda/dano contínuo; ameaça durante seleção; ausência de ganho de recursos; manutenção de cooldowns/consequências; retorno ao próprio local; destino inválido e falha/desconexão durante a troca. Nenhum desses cenários foi executado.

### Pendências para fechar o bloco após RAC-26/RAC-27

| Assunto | Decisão/entrega necessária | Responsável |
|---|---|---|
| Troca entre os personagens aprovados | RAC-29A/B propostos: acesso, encerramento de estados e retorno ao próprio local; aguardar respostas e especificar critérios verificáveis | M04/M06/M07/M10/M15/M16 |
| Inventário, propriedade e economia | Itens vinculados, recipientes, equipamentos, armazenamento, pets/construções, comércio e recompensas repetíveis; evitar cópia e ganho indevido sem presumir proibição de uso compartilhado | M11/M12/M15/M16 |
| Personagem novo numa era avançada | Abertura racial e acesso aos acontecimentos necessários, sem retroceder a cronologia global | M03/M10/M12/M13/M15 |
| Reinício, exclusão e retorno | Reset por NPC e alcance aprovados em RAC-28A/B. Exclusão de personagem, preço/limite e detalhes de retorno ainda não definidos. Não confundir criar outro, trocar e resetar | M04/M08/M11/M14/M15/M16 |
| Save existente e migração | Mapear progresso legado sem escolher outra origem ou apagar dados por inferência; definir plano de compatibilidade, recuperação e evidência antes de qualquer migração | M16, consultando M04/M05/M06/M07 |

**Critérios candidatos de revisão, não testes executados:** seleção ainda não confirmada não produz conquistas; personagem confirmado retoma sua origem; criar outro não transfere SP/compras/Mastery; voltar não duplica inventário; mudança de personagem não reinicia o mundo nem apaga consequências. A política concreta de troca e o inventário precisam ser especificados antes de validar esses casos em runtime.

**Próximo:** responder RAC-29A/B e concluir a revisão conceitual das fronteiras de continuidade/migração, sem votar novamente confirmação, personagens separados ou alcance do reset NPC. Só então avaliar o fechamento conceitual de M04 e seguir para M08. Sem implementação, alteração de saves ou testes do jogo nesta rodada.

