# M10-03B — Validação e falha de chegada

**Data:** 23/10/2026. **Estado final:** DECIDIDO, resposta explícita recebida em 23/10/2026. [M10 responsável](../10-dimensoes-locais-e-estruturas.md), 8.8/12.7. 03A decidido: pontos de chegada estabelecidos nas skills básicas.

**Pergunta:** Como padrão das viagens do mod, recomendo validar a área de chegada antes da travessia e confirmar o espaço no momento de transferir. Se não houver posição válida nessa área para o corpo que chegará, a viagem é recusada e o jogador permanece na origem, com aviso. Não remove blocos nem procura outro local fora da área. Adotamos isso ou buscamos automaticamente uma posição próxima fora da área de chegada?

**Opções:**

1. Recusar se a área de chegada estiver inválida (recomendado)
2. Buscar posição próxima fora da área

**Resposta em 23/10/2026:** “Buscar posição próxima fora da área”. Segunda opção escolhida, diferente da recomendação inicial. Contrato em M10/8.8/12.7; limites/falha propostos separadamente em 03C.

Contrato comum proposto, sem repetir a escolha para cada origem ou fonte. Corpo de chegada respeita o ciclo de vida de M06/M07. Área, apoio/perigos, carregamento, falha/concorrência e recursos exigem ficha; a proposta não promete imunidade, devolução de gastos ou retorno automático. Sem pesquisa canônica/API nova, código, mapas, saves, build, testes ou publicação.

A escolha mantém o ponto estabelecido como referência de destino e permite busca próxima fora de sua área quando necessário. Não aprova raio/ordem de procura, busca ilimitada, destino alternativo ou travessia de barreiras/permissões. A recusa imediata da recomendação inicial não foi adotada.
