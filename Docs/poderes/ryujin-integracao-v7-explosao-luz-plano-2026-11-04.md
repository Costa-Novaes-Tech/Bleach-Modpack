# Ryūjin — plano de integração da cena v7 no cliente Forge

**Estado posterior:** a implementação e a prova inicial no Forge estão no [relatório v7 de 04/11/2026](ryujin-v7-integracao-qa-2026-11-04.md). As tabelas abaixo registram a proposta original; o usuário acrescentou um impacto menor e campo de fogo durante a fala “Bankai”, preservando a explosão principal após a palavra.

04/11/2026 · **proposta para validação humana, sem implementação de runtime nesta rodada**. Base inspecionada: `Feature-Poderes-bankais` em `5b86984`, árvore limpa; `main`, checkout Quincy e checkout de design do Antigravity preservados. O usuário aprovou a melhora espacial da v7 e autorizou planejar sua construção em jogo com explosão, chamas e luzes. O [ensaio Blockbench e retorno visual](ryujin-experimento-explosao-sol-modelado-2026-11-02.md#retorno-humano-e-revisão-v7--04112026) são referência de movimento, não prova em Forge.

## Contratos que permanecem

- Transformação cênica de **240 ticks reais / 12 s**. Release **60** e commit **40** de gameplay não mudam; sol/explosão são cenográficos, sem dano, incêndio em blocos, nova proteção, custo ou alcance.
- Uma mão na katana diante do corpo, **ponta para cima**, durante “Bankai”; explosão **após o fim audível** da palavra; sol atrás do jogador na câmera frontal/3⁄4; descida da lâmina para o lado de fora; absorção convergindo na arma; nome e retorno de câmera/controle.
- O controle pode ser liberado após o tick real 60 pelo fluxo atual. A apresentação não prolongará a trava da habilidade para preservar uma tomada de câmera.
- Perfil Forge base entrega a cena. Photon/LDLib é opcional e recebe validação separada; nenhuma nova dependência é requisito desta fase.

## Estado verificado e diferença para a cena desejada

| Parte | Código atual | Consequência para a integração |
| --- | --- | --- |
| Relógio | `RyujinBankaiTimeline` mapeia `[0,40,90,140,160,210,240]` ticks reais em beats `[0,12,30,52,60,112,140]`. Sol cresce desde o primeiro beat e chega ao máximo no tick real 40. | Retimar **câmera, pose, VFX, áudio e testes juntos**; o sol atual não espera a fala terminar. Um único tick real da cena deve alimentar todos os módulos e o clipe GeckoLib. |
| Cena | `RyujinBankaiEntranceClient` já sincroniza instâncias, PlayerAnimator, câmera, áudio, partículas e render; limita a quatro cenas próximas. | Integrar no diretor existente. Não criar segundo controlador de jogador, entidade permanente ou cena paralela com outro relógio. |
| Sol e chamas | `RyujinSunVisual` desenha 112 cartões internos, 20 de coroa, base no chão, vórtice e quatro fluxos de absorção com sprites vanilla. Limites declarados: 288 cartões por cena e 12 partículas por tick; cliente distribui até 48 partículas entre cenas. | Substituir o núcleo/coroa de sprites pelo modelo, manter e retimar camadas úteis. Impedir dois sóis simultâneos e aplicar limite efetivo por fase e distância. |
| Pose/câmera | `RyujinChoreography.bankaiEntrance` mantém a espada baixa desde a abertura. `RyujinBankaiCamera` já usa olho frontal e retorno em órbita. | Refazer apenas a pose da entrada; preservar os gestos de combate e o retorno existente. Testar proximidade de paredes e saída antecipada da câmera. |
| Luz | `RyujinSunVisual` já usa `entityTranslucentEmissive`/`FULL_BRIGHT`; o player tem realce local de luz de bloco até 11, e há fog/HUD quente. | Aproveitar essas camadas, sincronizando intensidade com a explosão. Nenhuma delas, nem uma glowmask, ilumina blocos fisicamente. |
| Referência GeckoLib | `QuincyManifestationQa` já usa `GeoObjectRenderer`, `GeoModel`, `GeoAnimatable` e tick próprio no Forge/GeckoLib 4.8.4 do repositório. | Adaptar o padrão para o VFX da Ryūjin e provar múltiplos conjuradores; não copiar o singleton de QA sem isolamento por cena. |

O arquivo OGG de “Bankai” foi verificado nesta rodada: **48 kHz / 1,350 s**, isto é, ~27 ticks a partir do cue real 40; termina nominalmente perto do tick 67. O OGG atual do nome mede **2,240 s** (~45 ticks), cabendo entre o cue real 160 e o retorno em 210. Essas durações de contêiner não substituem escuta: latência perceptiva, ataque do fonema e ducking precisam ser aprovados no cliente. O `ffprobe` não está no PATH desta sessão; as durações foram obtidas da taxa do cabeçalho Vorbis e do granule position final, sem modificar os arquivos.

## Sequência proposta, em ticks reais

| Janela | Jogador, câmera e áudio | Geometria, fogo e luz aparente |
| --- | --- | --- |
| **0–40** | Pés firmes; uma mão ergue a katana até ponta vertical à frente; câmera abre em 3⁄4 frontal. | Prenúncio discreto, sem sol formado nem explosão. |
| **40–68** | Cue “Bankai” em 40; pose vertical sustentada, voz/legenda nítidas; música e fogo recuam sob a fala. | Apenas brasas de preparação atrás; não encobrir rosto ou fio. |
| **68–70** | Curta pausa de segurança após o fim nominal do OGG; ajustar pela escuta. | Acúmulo atrás do jogador, ainda sem flash principal. |
| **70–84** | Reação breve, mão firme e ponta fora do rosto; câmera mantém silhueta inteira. | Primeiro um **muro ascendente de chamas** com volume em profundidade; flash/radiais da v7 ficam secundários. Clarão quente rápido no corpo e na imagem, sem branco de tela inteira. |
| **84–112** | Katana inicia descida contínua para o lado armado; perfil da arma legível. | Chamas se curvam para núcleo/manto/coroa do modelo; aros passam a girar em sentidos alternados; fogo periférico não cobre o manequim. |
| **112–116** | Pose baixa estabiliza por poucos ticks. | Sol pleno, escala inicial calibrada no cliente; contraste entre centro quente, contorno irregular e fundo. |
| **116–142** | Câmera acompanha selamento e lâmina real; braço não muda de lado. | Aros aceleram, volume contrai continuamente, chamas e 4 fitas principais convergem para pontos da lâmina; 12 faíscas da v7 são apoio, não a leitura principal. Sol some **na arma**, não no torso. |
| **142–160** | Mostrar lâmina carbonizada e rosto; fogo externo encerra. | Brasas residuais, fumaça fina, luz quente decai; sem resíduo do sol. |
| **160–210** | Cue do nome em 160; plano de revelação e ducking até fim audível. | Cinzas discretas, sem reabrir o sol. |
| **210–240** | Câmera volta à perspectiva anterior; peso da pose vai a zero e HUD/FOV são restaurados. | Emissores cessam, sem pop ou luz presa. |

Os marcos **70/84/112/116/142** são alvos da primeira implementação, não valores já aprovados em jogo. Preservar a duração total; alterar um marco exige atualizar o relógio compartilhado, amostras de QA e transições. O clipe v7 dura exatamente 12 s: ele deve receber `tick real + partialTick` e iniciar uma única vez por cena, sem blending que atrase o flash em relação à voz. A fase de explosão pode ser ajustada alguns ticks após escuta, mas deve continuar depois da palavra completa.

## Asset e renderização

1. **Derivar um VFX sem manequim:** gerar, a partir do [`.bbmodel` v7](../../tools/models/lab/codex/ryujin01/ryujin_bankai_activation_scene_v7_lab.bbmodel), um `.bbmodel`, `.geo.json`, `.animation.json`, atlas e glowmask somente com sol, flash e faíscas. O estudo contém 220 cubos; **197 são VFX** e 23 pertencem ao corpo/katana/reflexos de treino. Não renderizar um segundo jogador nem uma espada falsa em Forge. Preservar original e usar gerador idempotente com verificação de bones, UV, animação e PNG.
2. **Um centro espacial:** alinhar `sun_root`, `RyujinBankaiPresentation.sunCenter`, emissores de cartas/partículas, lâmina calculada e câmera na mesma base local `frente=-Z, costas=+Z`. Converter o modelo para o yaw do ator uma vez; evitar somar novamente o recuo já embutido no asset. A geometria v7 tem ~**13 blocos de largura** em escala nativa de 16 unidades/bloco; **1,4×** é só ponto inicial para comparar com o sol atual de ~18 blocos de diâmetro. A cena do usuário aprovou o tamanho no editor, mas o enquadramento real decide a escala final.
3. **Renderer:** adaptar o padrão `GeoObjectRenderer` do Quincy para uma instância animável por cena/conjurador, com animação v7 controlada pelo tick da apresentação. Testar a geometria opaca/translúcida e o atlas alfa em Forge antes de sobrepor brilho. Usar a glowmask apenas nas partes quentes com `AutoGlowingGeoLayer` se a API presente no GeckoLib 4.8.4 compilar; o [guia oficial de GeckoLib 4](https://github.com/bernie-g/geckolib/wiki/Emissive-Textures-%28Geckolib4%29/e8dedc50b78a16185d83a9b91c33abb1f115a59e) define a camada e o sufixo `_glowmask.png`. O [guia de controladores](https://github.com/bernie-g/geckolib/wiki/The-Animation-Controller-%28Geckolib4%29) exige cuidado com transições e múltiplos controladores. O código Quincy do checkout é a referência de assinatura concreta; não copiar exemplos da versão atual GeckoLib 5.
4. **Composição do fogo:** trocar somente os 132 cartões que formam o disco/coroa antiga; conservar/refazer base, vórtice e fitas de absorção. Na abertura, adicionar cartas de chamas grandes em camadas e partículas de apoio para que os raios modelados não dominem. Cartas renderizam em profundidade e são ordenadas por câmera; não usar planos opacos retangulares do ensaio reprovado. Garantir `<=288` cartas por cena e `<=12` partículas por tick/cena, respeitando o limite global atual de 48 e a opção de partículas do jogador. Reduzir camadas por distância/qualidade sem apagar o modelo principal.
5. **Luz sem dependência nova:** no perfil base, combinar núcleo e glowmask emissivos, chamas `FULL_BRIGHT`, realce do próprio jogador e aquecimento discreto de fog/HUD, com pulso máximo breve em 70–84 e queda até o selamento. Isso produz **luz aparente**, não propagação dinâmica em blocos. Não inserir/remove blocos luminosos para simular o sol. Iluminação real do ambiente por uma solução externa/Photon fica como experimento opcional separado, só após o perfil base e uma análise de compatibilidade/desempenho.
6. **Arma e pose:** manter `PlayerAnimator` existente e sua prioridade, implementar curva vertical→baixa no `RyujinChoreography` e conferir `RyujinActionVisuals`/display do item real. O alvo da absorção vem de `RyujinBankaiPresentation.blade` apenas enquanto essa pose realmente está ativa. Se o usuário liberar câmera/pose após tick 60 e mover/atacar, encerrar ou simplificar a fita direcionada à espada para não apontar à posição antiga; conservar forma e gameplay. Testar destro/canhoto e observador.

## Provas antes de considerar a integração aceita

1. **Gate de recursos:** extração idempotente, `validate_scene_v7.py` atual passando, VFX isolado sem bones de corpo/espada, recursos com paths e IDs corretos, PNG/glowmask de mesma fonte, transparência e pivôs validados. Não promover a v7 combinada diretamente.
2. **Gate de lógica:** regressões da sequência real/voz, explosão ausente antes de 70, formação/absorção/retorno contínuos, limite de emissores e desligamento em cancelamento, morte, mundo novo e repetição. GameTests do serviço preservam release 60/commit 40, custos, proteção e cancelamento. `check build` e GameTests em ambiente seguro, sem tocar o mundo existente de `run-gametest/base`.
3. **Gate gráfico no cliente real:** começar por um plano estacionário frontal dia/noite, com vídeo **contínuo e áudio** de 0–240 ticks e frames nos marcos 40, 67, 70, 84, 112, 116, 130, 142, 160, 205, 210 e 240. Depois frente/perfil/traseira, canhoto, parede, chão irregular, primeira pessoa e observador; validar FPS/frametime medidos e comparar perfil base com Photon opcional. Ajustar `RyujinBankaiQaSequence`: ela hoje amostra a cada 10 ticks e pode perder os marcos críticos; preservar seus mundos e provas existentes.
4. **Gate de interrupção e multiplayer:** movimento/ataque após 60, cancelamento antes/depois do commit, morte, dimensão, desconexão, segunda ativação, duas cenas simultâneas e observador tardio. Conferir remoção de modelo, partículas, som, realce do jogador, câmera e HUD. Não declarar multiplayer aprovado com teste singleplayer.
5. **Aceite humano:** o jogador reconhece a katana vertical durante a fala, explosão **de chamas** atrás depois da voz, sol imponente sem ocultar corpo, giro claro, absorção percorrendo as fitas até a lâmina, carbonização e retorno limpo. Som e brilho são avaliados na tela real; build verde ou preview Blockbench não bastam.

## Pontos de decisão para revisão

- **Recomendação de escala:** começar no diâmetro atual de ~18 blocos e só ampliar até ~24 se o teste frontal, paredes e FPS sustentarem. O usuário já considerou o tamanho da v7 suficiente no editor.
- **Recomendação de luz:** entregar primeiro o perfil base com brilho emissivo, realce do ator e aquecimento visual; tratar iluminação dinâmica de blocos como opção posterior, pois exige outra técnica/dependência e pode afetar desempenho.
- **Recomendação de apresentação:** manter o plano frontal/3⁄4 enquanto o sol existe. Na câmera atrás do personagem, um sol geometricamente atrás ocupa o primeiro plano; o caso deve ser legível sem prometer a mesma composição de silhueta.

Esta rodada apenas inspecionou código/recursos e registrou o plano. Nenhum asset foi promovido a `src/main/resources`, nenhum Java/Gradle/GameTest foi alterado, nenhum cliente foi executado e não houve commit ou merge pelo Codex. O commit `5b86984` e os merges anteriores já constavam no HEAD quando a inspeção começou; sua autoria/validação não é inferida aqui.
