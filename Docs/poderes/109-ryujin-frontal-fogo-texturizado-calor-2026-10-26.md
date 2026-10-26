# 109 — Ryūjin frontal: fogo texturizado, calor e retorno

26/10/2026. Usuário transferiu a execução desta frente ao Codex, diretamente na Feature-Poderes-bankais. Base fe5a63e, checkout inicialmente limpo. Escopo: transformação Ryūjin; Senbonzakura/Better Combat/Hakuteiken continuam na frente paralela. Sem nova biblioteca de runtime, mudança de branches ou commit/push nesta rodada.

## Causas verificadas

- RyujinSunVisual usava POSITION_COLOR/QUADS sem textura, com corona e núcleo formados por leques/triângulos. A geometria ficava evidente no vídeo, independentemente da descrição de “fogo volumétrico”.
- Emissões de partículas estavam dentro do método render: a densidade dependia do FPS.
- Câmera tinha deslocamentos laterais em todos os planos e encerrava em alvo fixo atrás da âncora/FOV70.
- Fogo e câmera usavam posições aproximadas independentes da pose da katana; overlay preto também podia sugerir uma lâmina adicional.

## Implementação

### Chamas e atmosfera

- Removido o corpo de polígonos lisos e o overlay duplicado de lâmina. Carbonização continua representada pelo item Bankai existente.
- Novo sol em camadas de sprites animados FIRE_0/FIRE_1 do atlas de blocos do próprio Minecraft, emissivos, com distribuição em profundidade e ordenação de trás para frente. Sem PNG inventado, disco liso ou shader obrigatório.
- 144 cartões na massa e24 na coroa; absorção com6 percursos/16 elementos texturizados cada, total máximo264 cartões por cena. Limite de4 cenas visuais mais próximas por frame; não é a promessa de reproduzir infinitos lançadores sem custo.
- Centro8blocos atrás/8,5 acima da âncora; escala nominal9 e distribuição vertical ajustada para incluir a coroa no quadro. Fogo se concentra na lâmina por trajetórias curvas, sem explosão adicional ou dano novo.
- Emissões agora no client tick, uma vez por gameTime, até12 por cena e48 global/tick; opções de partículas reduzidas respeitadas. Fumaça/cinzas residuais na lâmina, brasas ao redor da invocação.
- Névoa e bordas quentes locais acompanham a massa de fogo e cedem após o selamento. Luz de apresentação no ator, sem editar blocos, weather, horário ou iluminação permanente. Não há distorção térmica de shader; não alegar que ela foi implementada.

### Âncoras, câmera e saída

- RyujinBankaiPresentation compartilha mão/ponta derivadas de RyujinChoreography e do lado da empunhadura. Emissão acompanha posição/direção atuais do ator; sol permanece na âncora original.
- Plano inicial frontal, sem offset lateral, FOV72 e espaço para o sol dentro das barras; aproximação30–52; detalhe frontal52–64, sustentação64–72; abertura72–88 e fala no plano frontal.
- O detalhe preserva rosto e espada inteira nas capturas destro/canhoto; não usa o close lateral extremo anterior.
- Retorno112–138 usa a perspectiva original, câmera nativa/clipping e FOV configurado, incluindo modificadores. Orbitamento para trás/primeira pessoa; aproximação frontal para F5 frontal. HUD/perspectiva retornam antes do fim140.
- Corrigido o frame de restauração à primeira pessoa: a flag detached da câmera ainda podia desenhar a cabeça depois da troca de modo. Supressão curta mantém o ator oculto também nesse frame; conferida nos ticks138/140.
- Legendas em marfim sobre fundo discreto e barras saindo progressivamente. Falas/OGGs e seus cues não foram alterados.

## Contratos preservados

Apresentação140ticks; release60/commit40; custos, cooldowns, passivas, dano, técnica Higashi, Investida/Rajada e regras de aquisição permanecem. Nenhuma invulnerabilidade, dano ou incêndio novo foi concedido ao sol. O fogo/passiva que já aparece no chão no momento do commit continua existente; esta rodada não o remove ou rebalanceia. Sem mudança de rede/schema ou dependência obrigatória Photon.

## QA e limites

Baseline check em fe5a63e aprovada. Novo GameTest verifica frontalidade/continuidade para três orientações, âncoras externas de ambas as mãos e endpoints reais de retorno sem atravessar o ator. Asserções antigas de FOV/distância do close foram atualizadas ao novo enquadramento, preservando os testes de commit/custo/cancelamento.

Laboratório novo, estritamente desenvolvimento + singleplayer/admin + save descartável Benihime-QA-68 e diretório run-benihime-qa:

```powershell
.\gradlew.bat -PbenihimeQa=true -PcinematicQaFast=true -PryujinBankaiQa=true -PqaAutoExit=true runClient --offline --console=plain
```

Oito cenários: dia/F5 traseiro, noite/F5 frontal, primeira pessoa, canhoto, parede próxima, deslocamento após commit, cancelamento pré e pós commit. Capturas do framebuffer a cada2ticks, JSONL com horário real/posição/ângulos/perspectiva e FPS observado. Não abre saves pessoais.

Primeira inspeção encontrou coroa cortada pela barra e cabeça visível no138; ambos corrigidos e bateria gráfica repetida. Capturas confirmam a nova aparência e marcos, não certificam todos os frames de movimento ou audibilidade. A mudança de posição do teste é teleporte controlado; não representa exploração livre com teclado/mouse.

Uma execução de GameTests encontrou falha de cadência no Hollow Brute (`[1,49,97,148,196,244]`); código/teste Hollow não foram modificados ou desativados. Log será preservado e a suíte repetida ao fechar a entrega. Não atribuir causa raiz apenas a esse registro.

## Fechamento

- Check/build final: exit0, 26s, **147 regressões aprovadas**.
- GameTests finais: exit0, 1m16s, **230 obrigatórios aprovados**. Falha anterior do Brute e assert antigo do detalhe preservados em QA; nenhuma alteração de Hollow para obter o resultado.
- Cliente final: exit0, 2m31s, **8 cenários / 616 capturas**. Dia/noite, lâmina dos dois lados, parede, retorno e cancelamentos conferidos nos marcos pertinentes. FPS no JSONL é amostrado, não benchmark ou garantia de fluidez contínua.
- [Manifesto](qa/cycle109/verification.json), [capturas e origem](qa/cycle109/captures.json), [prancha](qa/cycle109/contact.jpg), [prévia amostrada sem áudio](qa/cycle109/preview-sampled-day.mp4), [check](qa/cycle109/check.log), [GameTests](qa/cycle109/gametest.log), [cliente](qa/cycle109/client.log).
- JAR `build/libs/bleachmod-0.2.0.jar`, SHA-256 `1d27fa146bf78cd4e119ffb508e6068c34f612911e91021657392dcef1a76577`. OGGs comparados byte a byte com HEAD e preservados.
- Fontes/relatório continuam locais, sem commit/push. Logs rotativos dos testes preservados em build/cycle109-generated-root-logs; versões rastreadas restauradas.
- Photon, dois clientes, terreno irregular arbitrário e escuta subjetiva permanecem fora desta validação. A aparência do fogo utiliza os sprites vanilla, com estética Minecraft; o aceite artístico é do usuário.

