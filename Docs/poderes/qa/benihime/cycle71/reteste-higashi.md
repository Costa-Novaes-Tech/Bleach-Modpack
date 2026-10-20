# Ciclo 71 — reteste Higashi após primeira correção visual

Capturas de 20/10/2026, executadas em mundo QA novo pelo perfil base e sequência automática rápida. Este registro complementa a [revisão intermediária](revisao-visual-intermediaria.md), preservada como evidência das falhas anteriores.

- [Dano normal letal acima de 7%](1792501306868-lethal/index.html): 60 PNGs; a primeira amostra disponível é o tick de captura 9. Não existe imagem desta execução para os ticks 0–8.
- [Execução a 7%](1792501314299-threshold/index.html): 70 PNGs, até o tick 130.

As galerias incluem PNG/JSON originais, hashes, prancha de contato e vídeo amostrado silencioso. Os intervalos VFR vêm de `wallMs`; nenhuma interpolação foi aplicada.

## Observado

- A preparação de lâmina baixa e a elevação diagonal ficam mais legíveis. O jogador avança e os pés permanecem junto ao chão.
- A câmera lateral mostra os dois participantes e permanece durante a espera e a incineração.
- Nos marcos HOLD inspecionados, o corpo permanece em pé, sem o fogo vanilla e sem as partículas brancas de morte que apareciam na primeira sequência.
- Na fase ASH, o corpo já não é visível e as partículas de fogo/cinzas continuam. O retorno à primeira pessoa/HUD foi observado no tick de captura 112 de ambos os cenários. As partículas ainda vivas após esse retorno não significam câmera presa.

## Limite artístico identificado

O passo reduz bastante a distância anterior, e a trilha de partículas cruza o alvo. Entretanto, nos marcos de fim do corte, a ponta real da katana ainda aparenta terminar antes do corpo do Husk. Aderência literal ao pedido “a espada atravessa o inimigo” permanece PENDENTE de ajuste/validação. Isso foi comunicado ao agente principal antes da conclusão.

Os números de tick nesta galeria são contados desde o início do preparo QA; o cast é agendado para 20 ticks depois. A marca textual de fase é calculada pela sequência QA e não substitui o relógio do pacote de apresentação recebido pelo cliente, que pode chegar alguns ticks depois.

Sincronismo entre dois clientes, avaliação gráfica Photon, escuta dos áudios e qualidade artística completa não foram homologados por este reteste.
