# Ciclo 77 — auditoria do vídeo, gatilho Higashi e combate Benihime

20/10/2026. Branch `Feature-Poderes-bankais`. Base Git `b398fdc`, com alterações do ciclo 76 ainda presentes e preservadas. Sem checkout, commit, push ou merge. Higashi reaberta por defeito concreto informado pelo usuário; Zaraki, Ryūjin Investida/Rajada e entrada/modelo/áudio Benihime preservados.

## Auditoria do vídeo do usuário

Fonte: `Gravação de Tela 2026-10-20 223705.mp4`, 417,47 s, 1920×1020, 30 fps, faixa AAC estéreo. Análise local por amostras gerais de 8 s e trechos detalhados a 2 fps; marcos adicionais extraídos por segundo solicitado. [Origem e SHA256](qa/cycle77/video-usuario/source.json). Não foi realizada escuta subjetiva; não apresentar esse exame como nova homologação da mixagem.

**Parecer favorável à entrada Benihime no estado mostrado.** A fala “acho que a benehime está boa” do usuário é coerente com a melhora visual observável. Preservar esta construção e avançar para combate, sem novo redesenho da manifestação.

- Cerca de **1:30–1:38**: close do jogador, recuo e cortina ocultando o corpo. [Cortina aos 96 s](qa/cycle77/video-usuario/t096.png).
- Cerca de **1:40–1:48**: mão emerge pela abertura central, corpo passa de escuro para revelado e chega à posição de apoio. [Pega aos 100 s](qa/cycle77/video-usuario/t100.png), [corpo/apoios aos 104 s](qa/cycle77/video-usuario/t104.png). Não se observa o surgimento abrupto do corpo na frente do pano nas amostras. A noite/vegetação reduzem a leitura fina dos dedos e do contato com o piso; não provar por isso ausência de todo clipping.
- Cerca de **1:50–1:54**: elevação e transição à posição final dos fios. [110 s](qa/cycle77/video-usuario/t110.png), [114 s](qa/cycle77/video-usuario/t114.png). Silhueta e identidade reconhecíveis; cotovelos baixos, braços sem T rígido.
- **1:56–2:40**: controle normal e deslocamento vistos após a entrada; fios/manifestação acompanham a posição. [148 s](qa/cycle77/video-usuario/t148.png). Fios entram no primeiro plano em ângulos próximos (~2:24 e ~6:54): possível refinamento futuro de legibilidade, sem mudar a cena aceita agora.
- **6:28–6:52**: segunda sequência completa coerente com a primeira, incluindo abertura e elevação. [398 s](qa/cycle77/video-usuario/t398.png), [408 s](qa/cycle77/video-usuario/t408.png), [412 s](qa/cycle77/video-usuario/t412.png).

Julgamento do agente nos marcos (0–5): composição/identidade 4; sequência de revelação 4; levantamento 4; leitura fina mão/solo 3 sob noite/vegetação. Sem nota de escuta ou fluidez integral a 30 fps. Não há prova nova de dois clientes, morte/troca de dimensão, passagem em corredores ou desempenho controlado. Aceite visual deste cenário não elimina essas pendências técnicas.

**Higashi:** o vídeo alterna primeira/terceira pessoa, tentativas, alvos e outras técnicas; não contém telemetria de HP/mitigação suficiente para determinar o gatilho de cada tentativa. O relato do usuário é sustentado pelo código: a ativação chamava `HigashiExecutionService.preview` para qualquer alvo permitido e o cliente iniciava câmera/pose também com `confirmed=false`. Fogo normal, Rajada e Leque vistos no restante do vídeo não são prova de execução especial. Não imputar às capturas uma condição de HP não mostrada.

## Correção Higashi

- Removida a emissão antecipada de apresentação no cast. Pacotes antigos sem confirmação continuam decodificáveis, mas `startsExecution()` exige pacote válido e `confirmed=true`; o cliente rejeita a prévia antes de mudar câmera, pose ou efeitos.
- Cena longa/câmera/pausa/cinzas apenas após **morte realmente confirmada**: execução pelo limiar ≤7%, ou dano normal da Higashi suficiente para matar acima dele. Revalidar condições no impacto; cura, defesas, Totem no dano normal e cancelamento Forge não são previstos por estimativa de dano.
- Golpe normal mantém gesto curto e aproximação já existentes. Ao confirmar, a cena continua do marco de impacto, sem repetir uma preparação especulativa; pausa/ignição mantêm os marcos existentes. Alvo sobrevivente ou golpe sem alvo não recebem a cena longa.
- Pacotes confirmados duplicados do mesmo alvo não reiniciam a cena. Sem custo extra, alteração de dano/limiar, loot ou proteção de aliados. Formato do pacote/rede **2.29** e schema **9** mantidos.

## Próxima etapa implementada — espada e gestos Benihime

O ataque básico antigo reutilizava `dissection`/`nake` em seis ticks, misturando gesto de habilidade e swing vanilla. Agora tem três cortes próprios: diagonal, retorno e estocada curta, com inclinação/rotação contidas, braço auxiliar recolhido e recuperação. Primeira pessoa recebe deslocamentos pequenos; a lâmina deixa traço vermelho discreto, sem círculo genérico de técnica em cada ataque.

Oito gestos separados em `BenihimeChoreography`: Nake/incisão, escudo, abrir/recolher rede, detonação, dissecação, reconstrução/cura, reestruturação e abertura de caminho. Durações de apresentação anteriores preservadas. Modelos e design da bengala/lâmina não foram substituídos. PlayerAnimator permanece responsável pelas poses; efeitos fundamentais existentes continuam nos renderizadores do kit.

**Não há migração de hitboxes Better Combat.** Dano de ataque básico continua vanilla, imediato e sujeito ao seu cooldown; os novos gestos são cosméticos, sem adiar acerto para um keyframe. O combo visual não cria combo de dano ou alcance. Mecânicas homologadas de rede, dano coletivo, cura, cargas e passivas preservadas. Guarda/esquiva/liberação continuam com precedência; gestos não tomam a mão ocupada secundária. Senbon não recebe as novas curvas. Entrada/modelo/áudio da manifestação e Zaraki intocados.

## Laboratório e validação

Linha de base: `check runGameTestServer --offline --console=plain`, **129 regressões/155 GameTests**, exit 0, 2m26s. Primeiro check após correções: **132 regressões**, exit 0, 51 s. Novas regressões: confirmação do pacote; três cortes distintos/espelhados/recuperação; oito curvas com durações válidas/retorno.

Laboratório opt-in, singleplayer/admin/save descartável, sem comandos em produção. Quatro cenários Higashi: morte normal letal, limiar, sobrevivente, sem alvo; metadados registram câmera ativa e verificação de presença/vida. Em seguida, combos em selada/Shikai/Bankai, primeira pessoa e oito técnicas pelo executor real.

```powershell
.\gradlew.bat runClient -PbenihimeQa=true -PcinematicQa=true -PcinematicQaFast=true -PbenihimeCombatQa=true --offline --console=plain
```

FFmpeg 7.1 restaurado em `build/video-review-tools` por `imageio-ffmpeg==0.6.0`, wheel de 31,2 MB já em cache. Instalação sem índice não localizou distribuição; pip com cache concluiu. Sem instalação global, atualização de Java/mods ou envio de mídia a serviço externo.

Primeira sequência gráfica: estados de vida esperados nos quatro casos Higashi; câmera vista=true no letal/limiar e false no sobrevivente/sem alvo, quatro `passed=true`. Os doze cenários Benihime acionaram as leases esperadas. A captura de primeira pessoa ocultou a mão por usar hideGui; **inválida para julgar empunhadura**. Laboratório corrigido para manter HUD/mão nesse cenário, obter Y pelo terreno e não zerar força de ataque antes de cada golpe. Repetição em andamento; esses ajustes são de QA, sem alteração adicional de produção.

**Repetição gráfica final concluída no cliente base:** quatro cenários Higashi passaram no estado de vida e na presença/ausência da câmera. Foram amostrados 53/66/66/66 quadros; execução ativa em 35/38/0/0 respectivamente (letal/limiar/sobrevivente/sem alvo). Todos os doze cenários Benihime concluíram com alvos presentes; 51 registros por cenário (49 PNGs únicos em Path por dois ticks repetidos; demais com 51 PNGs). Nos quatro casos de melee foram registrados 18 quadros com gesto ativo, correspondendo aos três swings; técnicas mostraram leases/gestos esperados. HUD/mão preservados na repetição de primeira pessoa; espada visível e recuperando entre os golpes. O cliente foi encerrado normalmente pelo agente depois de restaurar câmera/HUD, antes das suítes finais.

Evidências e dados:

- [Higashi — marcos comparativos](qa/cycle77/higashi-0-marcos.jpg), [limiar — clipe](qa/cycle77/higashi/threshold/amostras-sem-audio.mp4), [sobrevivente — clipe](qa/cycle77/higashi/survive/amostras-sem-audio.mp4), [sem alvo — clipe](qa/cycle77/higashi/miss/amostras-sem-audio.mp4).
- [Benihime — três formas e primeira pessoa](qa/cycle77/benihime-combat-0-marcos.jpg), [espada em primeira pessoa](qa/cycle77/benihime-combat/shikai-first-person/amostras-sem-audio.mp4).
- [Shikai — gestos](qa/cycle77/benihime-combat-1-marcos.jpg), [Bankai — gestos](qa/cycle77/benihime-combat-2-marcos.jpg). Os oito nomes constam das leases no log; isso verifica acionamento visual, não uma nova homologação do dano/rede/cura dessas técnicas.
- [Resumo de amostras/FPS](qa/cycle77/capture-summary.json), [origem/hash dos PNGs selecionados](qa/cycle77/capture-manifest.json), [decodificação dos PNGs](qa/cycle77/png-validation.json). Os rótulos DRAW/HOLD/IGNITE da bancada indicam faixas temporais nominais; só `executionCamera` indica a cena especial. No sobrevivente/sem alvo esses nomes não representam uma execução.

Clipes são montados de quadros amostrados, sem áudio; não são gravação contínua. O concat PNG emitiu diagnóstico de decoder durante a primeira exportação; os 861 PNGs originais passaram em decodificação independente. Clipes regenerados usando JPEGs intermediários uniformes, preservando PNGs selecionados e intervalos de tick; [verificação de codificação/decodificação](qa/cycle77/video-validation.json). Não atribuir diferença de compressão ao jogo.

Medianas de FPS informado variaram de 21 no primeiro letal, durante carregamento, a 108 nos demais cenários. Sem benchmark/frametime, ensaio remoto ou hardware equivalente. Primeiro marco letal mostra chunks ainda não renderizados e há toast de avanço/fogo residual; usar o limiar para ler melhor o enquadramento. Nenhum controle/tela da Zaraki participou.

Avaliação do agente dos novos ataques (0–5 nos marcos): legibilidade da espada em primeira pessoa 4; diferenciação/recuperação do melee 3; distinção dos gestos de técnica 3. É uma primeira rodada para aceitação do usuário, sem afirmar animação cinematográfica perfeita nem alinhamento de dano a keyframe. **Pendente de validação visual do usuário** para os ataques novos. Servidor com dois clientes, offhand ocupado em jogo, mão dominante esquerda em jogo e reação a cancelamentos reais permanecem pendentes; curvas espelhadas/retorno foram cobertas pelas regressões.

Base final `check build runGameTestServer prepareCombatTestPack --offline --console=plain`: **132 regressões / 155 GameTests**, exit 0, 2m18s. Perfil Photon separado `-PphotonPilot=true check build runGameTestServer prepareCombatTestPack preparePhotonTestPack --offline --console=plain`: **132 regressões / 155 GameTests**, exit 0, 2m22s. Persistem diagnósticos recuperados do scanner/editor LDLib no dedicado; nenhum GameTest obrigatório falhou. Isso não é validação gráfica do editor ou ensaio com dois clientes.

[Log final base](qa/cycle77/validation/validation-cycle77-base-final.log), [log final Photon](qa/cycle77/validation/validation-cycle77-photon-final.log), [cliente final](qa/cycle77/validation/validation-cycle77-client-final.log), [manifesto de entrega](qa/cycle77/delivery-manifest.json). Pacotes em `build/combat-test-pack/mods` (cinco JARs) e `build/photon-test-pack` (sete JARs); CRC e identidade do mod em ambas as pastas/build/libs conferidos. Protocolos/dependências inalterados. `git diff --check` passou. Logs gerados na raiz restaurados ao estado inicial, sem remover mudanças de código/documentos anteriores. Sem commits. Sem assumir homologação estética dos ataques novos só pelas regressões.

## Propostas e próximo teste

Preservar entrada/modelo/áudio Benihime aceitos no cenário observado. Primeiro validar os novos três cortes em primeira e terceira pessoa, depois transições ataque → defesa → ataque e os oito gestos com alvos apropriados. Uma eventual redução de fios perto da câmera ou contraste de dedos deve ser uma correção pequena apoiada em nova captura; não reabrir todo o modelo por hipótese. Para Higashi, conferir ataque comum contra alvo resistente e execução real sem mover a câmera antes da confirmação. Refinamento posterior de enquadramento deve preservar esta regra de morte confirmada.

## Roadmap

Entrada Benihime com parecer visual favorável neste vídeo; preservar e fechar o feedback do usuário. Higashi recebe esta correção específica. Investida/Rajada do ciclo 76 preservadas, aceite artístico próprio não presumido. Item 4 (combate/técnicas Benihime) agora em teste. Depois Ishida (contrato Quincy), Mentor/Proficiências após equipe, Hyōrinmaru/Blockbench; NPCs de combate/quests da lore após a lista. Aizen adiado, Gin fora da fila. Não iniciar novo kit antes da validação desta frente.
