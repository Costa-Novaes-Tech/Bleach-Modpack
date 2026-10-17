# Ciclo 57 — Correção de direção visual da Ryūjin, antes de implementar

Data: 17/10/2026. Branch `Feature-Poderes-bankais`, HEAD de entrada `31ad11b`, Git limpo ao iniciar. **Esta entrega contém somente documentação, imagens conceituais e estudos animados. Nenhum arquivo do mod em src, dependência, build ou pacote jogável foi alterado.**

## Feedback recebido

Hyōrinmaru está boa no momento; não revisar seu kit nem seus efeitos nesta etapa. A revisão Ryūjin do ciclo 56 foi considerada um downgrade visual pelo usuário. Correções solicitadas:

- Katana da pose Yamamoto aponta para dentro; deve apontar para fora do corpo, no lado externo da mão que a segura.
- A pose ficou permanente; só deve existir por um momento dentro da transformação Bankai, com retorno automático à postura normal.
- Rajada e Leque podem conservar suas funções, mas as superfícies quase translúcidas devem dar lugar novamente às partículas de chamas em todo o repertório Ryūjin.
- Higashi precisa de efeito especial legível e movimento corporal próprio, não apenas um golpe curto pouco perceptível.
- Antes de codificar a correção no mod, apresentar imagens e animações curtas de cada ataque e explicar sua implementação.

## Material entregue para revisão

[Galeria de 11 animações e quadros](media/57-ryujin-previs/index.html). Funciona localmente, sem bibliotecas ou conexão externa. Seleção por ataque, GIF animado, três quadros de preparação/execução/recuperação e descrição técnica. Os dois painéis ilustrados ficam nas seções expansíveis da galeria.

[Painel conceitual geral](media/57-ryujin-previs/12-conceito-geral.png) e [storyboard detalhado da Higashi](media/57-ryujin-previs/13-conceito-higashi.png): ferramenta integrada image_gen, prompts integrais registrados em [prompts-imagegen.md](media/57-ryujin-previs/prompts-imagegen.md). São metas de direção artística; não capturas do jogo nem assets instalados. Silhueta, exagero de luz e faíscas devem ser adaptados à câmera/escala reais. O desenho geral do Leque sugere um círculo; o contrato continua sendo um arco aberto de 140°, não um giro de 360°.

Os onze GIFs e trinta e três quadros-chave são renderizações procedurais autorais com personagem em blocos, feitas pelo [gerador reproduzível](media/57-ryujin-previs/gerar_previs.py). Não foram gerados extraindo frames das imagens image_gen, nem representam os keyframes finais de PlayerAnimator. A duração de 2,8–4 s facilita leitura do estudo; não muda as durações atuais de dano/custo/cooldown. O personagem simplificado e a aproximação ortográfica servem para comparar movimento e sequência, não homologar acabamento final.

## Direção de cada ataque

| Prévia | Movimento idealizado | Efeito proposto / como levar ao mod |
|---|---|---|
| [Transformação Bankai](media/57-ryujin-previs/01-selo-bankai.gif) | Chamas convergem; braço posiciona a espada na lateral externa; lâmina baixa e apontada para fora; pausa breve; recuperação | Remover a camada ociosa permanente `RyujinBankaiReadyPose`. Manter só keyframes temporários dentro de `ZanpakutoReleaseAnimation`; espelhar braço/item para canhotos e zerar os ajustes ao finalizar/cancelar. Não travar o jogador nessa pose após a liberação. |
| [Selada H — Corte frontal](media/57-ryujin-previs/02-corte-frontal.gif) | Preparação superior, corte descendente, retorno | Animar ombro, braço e transferência de peso; manter golpe físico, sem adicionar fogo à selada. |
| [Selada N — Varredura](media/57-ryujin-previs/03-varredura.gif) | Base aberta, quadril gira, braço acompanha arco horizontal, recuperação | Animação distinta do corte frontal; separar rotação de tronco e ombro para evitar espada atravessando o corpo. |
| [Shikai H — Rajada](media/57-ryujin-previs/04-rajada.gif) | Golpe descendente projeta frente estreita que percorre o chão | Retirar o quad translúcido; emissores de FLAME/SMALL_FLAME acompanham a frente, com brasas e fumaça curta. O visual avança com a trajetória, não preenche a área toda instantaneamente. |
| [Shikai N — Leque](media/57-ryujin-previs/05-leque.gif) | Corte horizontal da direita para a esquerda, com rotação de quadril e ombro | Distribuir chamas no setor que a espada está cortando, rastro curto atrás e dissipação; substituir a fita translúcida. Preservar o arco aberto e alcance atuais. |
| [Shikai B — Tornado](media/57-ryujin-previs/06-tornado.gif) | Giro de espada e corpo inicia hélices de fogo | Conservar as partículas do tornado, acrescentar gesto próprio e variação de brasas/fumaça; centro livre legível. Não criar hitbox extra nas hélices. |
| [Shikai C — Jōkaku](media/57-ryujin-previs/07-jokaku.gif) | Espada indica a região, fogo nasce na borda e sobe fechando a cúpula | Retirar a malha translúcida; chamas em línguas verticais e nervuras, com vãos para enxergar a luta. Manter região fixa e executor do dano. |
| [Bankai H — Investida](media/57-ryujin-previs/08-investida.gif) | Tronco inclina, espada avança e pernas acompanham o deslocamento real | Animação sincronizada ao dash existente; fogo/brasas no percurso, rastro curto; não somar um segundo movimento artificial ou aumentar alcance por efeito visual. |
| [Bankai N — Higashi](media/57-ryujin-previs/09-higashi.gif) | Espada recolhida na lateral do quadril, ombros armados; giro de quadril libera corte curto e seco; follow-through fora do corpo; recuperação | Preset exclusivo PlayerAnimator, fio branco quente/amarelo, chamas compactas acompanhando a lâmina, faíscas e pulso breve no contato. Não virar projétil nem cone longo. O pequeno passo da prévia representa peso corporal; deslocamento adicional de gameplay não foi decidido. |
| [Bankai B — Minami](media/57-ryujin-previs/10-minami.gif) | Espada toca o chão, tronco acompanha, soldados surgem em sequência | Fogo baixo nos pontos de surgimento, cinzas ascendentes e emergência visual dos soldados; rachaduras do conceito são apenas visuais. Sem novos blocos, drops, dano de terreno ou soldados extras. |
| [Bankai C — Corte de Vapor](media/57-ryujin-previs/11-vapor.gif) | Preparação alta e pesada, golpe decisivo, finalização | Frente concentrada de chamas e brasas, fumaça abrindo nas laterais; conservar leitura do caminho. Preservar alcance, destruição e blocos protegidos do executor, alinhando emissão ao impacto após a preparação. |

## Implementação após a revisão visual

1. Corrigir orientação da espada e retirar registro/uso da pose permanente da Ryūjin. Uma pose temporária no final da timeline substitui a camada ociosa. Conferir de frente, lado, costas, primeira pessoa, canhoto e mão secundária ocupada; testar retorno neutro ao terminar/cancelar e ataques/guarda logo depois.
2. Separar presets de movimento: descida da Rajada, rotação lateral do Leque, giro do Tornado, apontamento Jōkaku, avanço da Investida, giro curto do Higashi, contato com chão Minami e preparação pesada do Vapor. Braço/item/tronco/pernas devem concordar; não reutilizar a mesma rotação genérica para todos.
3. Substituir somente os ramos de desenho Ryūjin em `ElementalActionVisuals`: remover quads/ribbons e emitir partículas nativas por tick, com origem/direção/idade do snapshot. Para o fio branco quente, preferir detalhe luminoso curto e quente sobre a lâmina, sem placas semitransparentes ocupando a área inteira. Não requer Photon/LDLib para este primeiro refinamento.
4. Sincronizar emissão e gesto ao instante autoritativo de cada técnica. Impactos continuam sendo decididos no servidor. Eventual mudança de antecipação que altere o frame do dano deve ser explícita e testada; tempos alongados do GIF não são contrato de servidor. Densidades da prévia são ilustrativas: ajustar orçamento por dono/tick, distância e opção mínima de partículas.
5. Não alterar Hyōrinmaru, passivas, custos, slots, maestria, dano, alcance ou recargas por motivo cosmético. Manter migração e regras de alvo/aliados/PvP/colisão existentes. Atualização do JAR só depois de levar a direção visual revisada ao runtime e validá-la.

## Conferência desta entrega

Onze GIFs abertos com Pillow: 42–60 frames, conteúdo diferente entre preparação e execução. PNGs inspecionados visualmente (painel geral, Higashi conceitual e quadros procedurais). JavaScript da galeria validado com `node --check`; referências locais verificadas. Interação testada com DOM mínimo em Node; não houve playtest de cliente Minecraft nesta etapa. Nenhum GameTest/build repetido, pois o código jogável está inalterado.

Próximo passo: revisar estes movimentos com o usuário e então aplicar a correção visual à Ryūjin. O ciclo 56 continua sendo a build atual, com os problemas relatados acima ainda presentes nela. Não apresentar esta documentação como correção já aplicada ao JAR. Roadmap 51 e integração futura do Zaraki preservados.
