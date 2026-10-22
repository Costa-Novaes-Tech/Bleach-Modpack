# Ciclo 90 — proporções da hidra, impactos explosivos e remoção do vínculo com weather

21/10/2026. Branch conferida: `Feature-Poderes-bankais`; Git limpo no início desta revisão. Pedido do usuário: acrescentar correções ao planejamento, **sem codar**. Esta rodada altera apenas documentos em Docs/poderes. Versão implementada continua sendo89; não houve build, execução do jogo ou mudança em habilidades/modelos.

Este documento supera a proposta de retorno/recolhimento do Assalto da Hidra nos ciclos88–89 e a preservação do vínculo climático do89. As outras pendências de acabamento do89 continuam abertas.

## 1. Proporções e construção

**Direção aprovada:** hidras maiores/mais robustas, corrigindo a relação entre pescoços finos e cabeças grandes. Trabalhar a espessura dos pescoços e sua sustentação, preservando a escala imponente das cabeças; não ampliar uniformemente a mesma malha e manter a desproporção.

Recomendação para a próxima autoria: aumentar inicialmente a espessura dos pescoços em cerca de40–60%, principalmente na base e no trecho intermediário, com afunilamento gradual próximo à mandíbula. Esse intervalo é uma referência de protótipo, não medida homologada. Conferir a silhueta frontal, lateral e durante o mergulho antes de fixar números.

Melhorar transições entre segmentos, cristais sobrepostos e volume sob a cabeça para esconder o aspecto de tubos articulados. Ajustar espaçamento e curvas para as três massas não atravessarem umas às outras. O volume visível e a região de contato devem continuar coerentes; espessura decorativa não pode multiplicar dano automaticamente.

Permanecem no plano: detalhar focinho, dentes, mandíbula, chifres e texturas; antecipação dos movimentos; preparação com as duas mãos na katana; câmera de revelação; asas/cauda e diferenciação dos espinhos das habilidades.

## 2. Bankai independente do clima do Minecraft

**Direção aprovada:** ativar a Bankai não deve iniciar tempestade, chuva ou trovões, nem controlar o weather do mundo.

- Remover da Hyōrinmaru o acionamento de clima global e as conexões indiretas dessa ativação com o serviço climático. Auditar também campo C/passivas para não restar um caminho que religue a tempestade.
- Produzir a atmosfera glacial com neve, névoa, vento, gelo, iluminação/efeitos locais e áudio da habilidade. A transformação e os ataques devem funcionar em qualquer condição climática natural.
- Preservar chuva/tempo já existentes por causas naturais, comandos ou outros sistemas. Não substituir a ligação antiga por um comando global de tempo limpo.
- Revisar limpeza/migração de eventuais estados climáticos deixados pela implementação anterior, respeitando a propriedade desses estados. Restaurar somente alterações cuja origem no mod esteja comprovada; não apagar arbitrariamente o clima do mundo.
- Preservar as passivas de combate15%/30% e as regras aprovadas do kit. Identificar qualquer bônus condicionado ao clima antes da remoção, para que o desvínculo não altere o balanceamento silenciosamente.

Critério de teste: ativar/desativar Bankai, trocar de dimensão, morrer e desconectar sem modificar clima ou temporizadores do mundo; repetir em tempo limpo e em chuva natural, com outro observador.

## 3. Bankai N — três investidas com explosões, sem retorno

**Mudança aprovada:** as três cabeças/pescoços surgem atrás do jogador, avançam como a referência de vídeo e **não retornam**. Cada avanço termina em impacto explosivo na região atingida, espalhando mais gelo e dano em área.

### Sequência visual revisada

1. Três estruturas de gelo se formam atrás do invocador, com pescoços mais grossos e curvas distintas. A invocação de combate continua curta; não repete toda a transformação da Bankai.
2. As cabeças se projetam por três trajetórias legíveis: avanço, arco lateral e mergulho. Manter a referência de movimento do vídeo analisado no88 e antecipação que permita reconhecer as regiões de impacto.
3. Ao alcançar a região de impacto ou uma superfície bloqueadora válida, a respectiva cabeça explode em neve, estilhaços e expansão radial de gelo.
4. A desintegração percorre o pescoço da ponta para a base. Não reproduzir recolhimento, recuo até o jogador, nem retorno à postura de espera depois do golpe.
5. Permanecem marcas locais de gelo/geada, névoa baixa e neve em dispersão. Três impactos escalonados devem ser percebidos separadamente, mesmo quando suas áreas se sobrepõem.

A hidra da **transformação** continua sendo uma cena própria de formação/baforada/revelação. A ordem de não retornar se refere ao ataque ofensivoN; não trocar a baforada da invocação por essas explosões sem novo ajuste do roteiro.

### Acerto e balanceamento a fechar

O ataque passa a privilegiar dano de impacto e explosão em área. O antigo orçamento4+4+10 de contatos não deve ser somado integralmente a novas explosões, gerando dano extra por acidente. Reavaliar a distribuição entre contato e explosão; aumentar a cobertura exige validar o dano total sobre alvos nas sobreposições.

Recomendação: uma resolução de explosão por cabeça, com alvos deduplicados e limite explícito de acúmulo das três áreas. O acerto deve ocorrer junto ao impacto visível, não durante a preparação. Paredes interrompem o avanço e limitam o dano da explosão; aliados, invocador e PvP seguem a política compartilhada.

Alcance nominal32 permanece como referência do89. **Raio das explosões, dano, intensidade do congelamento, custo/recarga e cadência exata ainda precisam ser definidos no contrato da próxima implementação.** Para o primeiro estudo, raio de cerca de6 blocos por impacto é uma sugestão, não um valor aprovado pelo usuário.

O pedido autoriza espalhar gelo e dano em área; não implica destruir blocos permanentemente. Preferir primeiro resíduos visuais/cobertura temporária e local, com duração e orçamento limitados; qualquer alteração real de terreno deve ter regra e restauração próprias.

## 4. Ordem de trabalho atualizada

1. Fechar proporções/volumes e a silhueta da hidra, comparando com o jogador em todas as poses.
2. Desvincular weather e validar que o clima do mundo permanece intacto.
3. Refazer a coreografia doN: surgimento atrás → avanço/arco/mergulho → impacto → explosão → desintegração, sem retorno.
4. Fechar raio/dano/controles e sincronizar acerto, explosão, gelo residual e áudio com o servidor.
5. Refinar transformação, pose/câmera e apresentação de asas/cauda; revisar os outros ataques conforme89.
6. Validar nos perfis base/Photon, terreno irregular, paredes e observador multiplayer; depois homologação do usuário.

## 5. Verificações para a implementação futura

- Pescoços robustos sem interseções aparentes, junções abertas ou cabeças desproporcionais.
- Três saídas por trás do jogador; três impactos identificáveis; nenhuma animação de retorno.
- Cada cabeça só explode uma vez, inclusive ao encontrar parede, perder alvo ou receber atualização tardia.
- Dano de área coerente com o gelo visível e sem duplicação por cabeça/partícula/frame.
- Resíduos limitados e removidos corretamente; sem destruição de blocos implícita.
- Interrupções, morte, dimensão, desequipar e desconexão não deixam entidades, explosões pendentes ou weather modificado.
- Continuidade do restante do roadmap obrigatório do72: esta revisão não encerra Hyōrinmaru, Benihime, Ryūjin ou as demais frentes. Zaraki preservado; Aizen adiado; Gin fora da fila.
