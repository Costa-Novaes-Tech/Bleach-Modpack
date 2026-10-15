# Ciclo 42 — Byakuya: cortes de pétalas, campo floral e alcance

Data: 15/10/2026. Branch: `Feature-Poderes-bankais`. Discussão de design e registro das escolhas do usuário, **sem implementação de poderes**. Complementa a [pesquisa do ciclo 41](41-byakuya-pesquisa-possibilidades-kit-2026-10-15.md) e substitui sua proposta de onda/maré para o slot 1.

## Escolhas recebidas

- Usuário gostou da premissa geral das adaptações; demais técnicas continuam em discussão, não houve aceite final de toda a tabela.
- **Shikai, habilidade 1:** corte horizontal de pétalas da **direita anatômica do jogador para sua esquerda**. As pétalas devem percorrer o caminho do golpe progressivamente, produzindo leitura de corte de anime.
- **Bankai, habilidade 1:** sequência de **cinco cortes** da mesma lógica visual, em direções diferentes e numa ordem definida; construir após concluir a versão Shikai.
- **Nova habilidade Bankai de área:** envolver o jogador em pétalas, primeira explosão radial, espalhar pétalas sobre o chão e permitir reativação após cinco segundos, com limite. Na reativação, formar uma flor na borda do campo e explodir novamente por toda a área, elevando pétalas.
- **Centro fixo confirmado pelo usuário:** área ancorada no ponto da primeira explosão, não acompanha o jogador. Flor e segunda explosão continuam centradas nesse ponto, mesmo que o usuário se desloque.
- **Passiva Bankai pretendida:** presença visual de pétalas e aumento do alcance do ataque corpo a corpo. Valor e condições precisam de contrato final.

Não apresentar os nomes/efeitos novos como técnicas canônicas oficialmente nomeadas. São adaptações propostas pelo usuário, inspiradas no controle de lâminas de Senbonzakura.

## Recursos visuais disponíveis

Sim: a versão Java 1.20 já inclui partículas rosas das folhas de cerejeira e o bloco Pink Petals. [Notas oficiais de Java 1.20](https://feedback.minecraft.net/hc/en-us/articles/16499677456781-Minecraft-Java-Edition-1-20-Trails-Tales), seção Cherry Groves.

A inspeção do runtime local 1.20.1/Forge 47.4.10 confirma a classe `net.minecraft.client.particle.CherryParticle`. A presença da aparência nativa não significa que a física natural da partícula siga um arco de espada ou mantenha um desenho estável no chão. Para reproduzir precisamente o pedido, recomendar **aproveitar a aparência/textura vanilla com movimento/tempo controlados pelo mod**, em vez de depender somente de emissão aleatória de partículas naturais.

- Cortes: frente de partículas avança no arco com rastro curto; poucas pétalas podem se desprender e cair após a passagem.
- Campo: distribuição visual persistente sobre superfícies válidas; desenho floral estável e animação ascendente na segunda explosão.
- Não colocar/remover blocos Pink Petals para desenhar a técnica: representar o tapete visualmente evita alterar construções, criar drops e exigir restauração de terreno.
- Um orçamento visual compartilhado por técnica/atualização, com distância e quantidade de campos limitadas. A `VisualFrame` existente oferece limites de emissão, mas a animação precisa pode exigir um renderer/partícula próprio e snapshot compacto. Não afirmar que o executor atual já suporta tudo sem extensão.
- Servidor determina fases, área e acertos; clientes desenham o efeito. A quantidade de partículas e a opção gráfica não podem modificar o dano.

## Shikai 1 — Corte horizontal

Fluxo proposto: preparação breve → frente de lâminas surge à direita → percorre arco frontal → termina à esquerda → rastro se dissipa.

- Direção direita/esquerda é relativa ao corpo/yaw do jogador, não ao observador ou à tela. Testar em primeira/terceira pessoa e com observador de frente.
- O arco deve aparecer em sequência ao longo de alguns ticks, não como um semicírculo inteiro desenhado de uma só vez.
- A colisão ofensiva deve acompanhar a parte do arco que está passando naquele instante. Não aplicar todo o dano na ativação e só depois mostrar o golpe.
- Uma cobrança por execução; cada alvo elegível recebe no máximo um crédito de acerto desse corte. Partículas extras não geram acertos extras.
- Recomendação inicial: orientar pelo yaw no começo do corte, mantendo a trajetória estável até terminar. Travar a direção do ataque não implica travar a câmera ou o jogador.
- Alcance, abertura do arco, espessura, duração, dano, custo e interação com paredes ainda não foram escolhidos. Respeitar obstáculos e aliados conforme o contrato comum.

## Bankai 1 — Cinco cortes

Sequência inicial **sugerida, ainda não aprovada**:

1. Horizontal, direita → esquerda.
2. Horizontal, esquerda → direita.
3. Diagonal, alto à direita → baixo à esquerda.
4. Diagonal, alto à esquerda → baixo à direita.
5. Vertical, baixo → cima, como encerramento.

Cada um reutiliza a lógica do Shikai com plano/orientação próprios. Recomenda-se separar ritmo, espessura e intensidade visual para que a sequência seja legível; não transformar os cinco golpes em emissão simultânea.

Propostas de regras: custo único para a sequência, direção fixada no início de cada corte, mira ajustável entre cortes e limite de um acerto por alvo em cada corte. Definir orçamento de dano total; não multiplicar automaticamente o dano do Shikai por cinco. A compatibilidade com intervalos de imunidade vanilla deve ser decidida antes do código, sem limpar indiscriminadamente a invulnerabilidade dos alvos. Definir cancelamento por morte, troca de forma/arma, controle ou perda do vínculo e movimento permitido durante a execução.

## Bankai de área — Campo floral em duas explosões

Nome de trabalho sugerido: **Jardim de Senbonzakura**; rótulo do mod, não técnica canônica nomeada.

| Fase | Pedido do usuário / intenção | Parâmetros sugeridos, ainda pendentes |
| --- | --- | --- |
| Ativação | Pétalas envolvem o personagem e se expandem em todas as direções. | Preparação visual curta, uma primeira onda de dano e nenhum efeito defensivo implícito por estar coberto de pétalas. |
| Campo armado | Pétalas espalhadas pela área de combate, centro fixo. | Raio inicial de **10 blocos** como candidato; um campo por jogador, sem carregar chunks. |
| Espera | Reativação somente depois de **cinco segundos**. | Contar **100 ticks de servidor** desde a ativação; indicar no HUD quando pronta. |
| Reativação | Pétalas formam flor no limite do campo. | **Uma reativação** por campo, em janela candidata entre **5 e 10 segundos**; mesma tecla do slot. |
| Formação/finalização | Segunda explosão por toda a área, pétalas sobem. | Desenho candidato de **cinco lóbulos**, inteiramente dentro do raio máximo, com aviso curto antes do pulso final. |
| Encerramento | Efeito acaba sem campos órfãos. | Expiração sem segunda explosão se a janela acabar; cancelamento por morte/logout/dimensão/perda de forma/arma/vínculo. |

**Confirmados:** área fixa e atraso mínimo de cinco segundos. **Não confirmados:** raio 10, prazo final de 10 segundos, número de lóbulos, duração do aviso, custo/dano e condições de posição para reativar. O limite de uma reativação é a recomendação para concretizar o limite pedido pelo usuário, não uma decisão já recebida.

O centro deve ser definido pelo ponto da primeira explosão. A flor no chão é um desenho visual dentro dessa área; o pulso final atinge a área elegível inteira, não apenas a linha do contorno. Subida das pétalas não implica lançar inimigos para o alto.

Recomendação para reativação: jogador ainda dentro do campo e na mesma dimensão; a forma floral permanece no centro original. Isso evita detonar remotamente de muito longe. Se o jogador sair, a área permanece ancorada; definir se pode voltar dentro da janela. Não recentrar a técnica na posição atual ao receber o segundo pedido.

Não somar dano contínuo durante a espera automaticamente: proposta inicial de dano apenas nas duas explosões, com presença do campo anunciando a ameaça. Sugestão de custo pago na ativação, reativação incluída; cooldown começa na primeira ativação e impede sobreposição de campos. Todas essas escolhas precisam de aceite antes da implementação.

Definir altura útil, terreno irregular, linha de visão/cobertura, água e locais sem chão. Não atravessar paredes/pavimentos por usar uma seleção radial indiscriminada. Colisão/dano e terreno de apoio são regras distintas; um círculo visual visto de cima não autoriza dano em qualquer altura.

## Passiva Bankai — Alcance de ataque

- Ativa somente com Senbonzakura vinculada, Bankai legítima ativa e equipamento correspondente na mão principal. Bônus sugerido inicial: **+1,5 bloco**, ainda sujeito a aprovação.
- Aumentar alcance contra entidades para o ataque corpo a corpo, com cliente e servidor consistentes. Não aumentar distância de mineração, colocação de blocos ou alcance de todas as técnicas.
- A fonte local do Forge 47.4.10 confirma `ForgeMod.ENTITY_REACH`, atributo distinto de `BLOCK_REACH`, com sincronização própria. O criativo já possui ajuste vanilla/Forge adicional; não substituir o alcance base por um número fixo ou conceder bônus duplicado nesse modo.
- Aplicar modificador identificável e reversível, sem acumular cópias a cada tick. Remover ao sair da Bankai, trocar arma/vínculo, morrer, desconectar ou mudar de dimensão; reavaliar no carregamento.
- Aura discreta de pétalas ao redor do jogador durante a Bankai e rastro curto no ataque estendido, com orçamento visual próprio. Bônus de alcance não concede automaticamente acerto em área, dano extra, ataque através de paredes ou crédito de maestria passivo.

## Efeito sobre a tabela de slots

| Slot | Shikai | Bankai — encaixe candidato |
| --- | --- | --- |
| 1 | Corte horizontal direita → esquerda, escolhido pelo usuário. | Sequência de cinco cortes, escolhida como direção geral. |
| 2 | Corrente concentrada — ainda em discussão. | Campo floral de duas explosões — sugestão de posição para a habilidade nova. |
| 3 | Convergência — ainda em discussão. | Senkei, com eventual Ikka Senjinka contextual — ainda em discussão. |
| 4 | Guarda de pétalas — ainda em discussão. | Hakuteiken — ainda em discussão. |

O campo floral ocuparia o lugar antes sugerido para Gōkei. Não aprovar essa substituição silenciosamente: Gōkei permanece como alternativa a discutir; não chamar o novo campo de Gōkei para fingir equivalência canônica. Se ambos forem desejados, revisar os quatro slots e contextos antes do código. Passiva de alcance não ocupa slot.

## Próxima fatia recomendada

Fechar os parâmetros e construir **primeiro o corte Shikai 1**, incluindo aparência da arma liberada e trajetória visual sincronizada. Homologar direção/ritmo/colisão; depois reutilizar esse motor para os cinco cortes Bankai. Campo floral e passiva têm contratos próprios e podem vir em ciclos seguintes do mesmo kit. Os demais slots continuam abertos à discussão pedida pelo usuário.

Validação desta entrega: inspeção read-only do Git, fontes locais, pesquisa oficial e revisão de links/diff. Nenhuma alteração de código/JAR, build ou teste automatizado novo. Base funcional permanece ciclo 40, protocolo 2.11/schema 8; os testes anteriores não validam essas propostas ainda não implementadas.
