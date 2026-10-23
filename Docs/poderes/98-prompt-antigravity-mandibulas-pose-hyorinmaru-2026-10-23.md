# 98 — Frente Antigravity: mandíbulas e pose da Hyōrinmaru

23/10/2026. Prompt autorizado pelo usuário. Codex trabalha em áudio e câmera na Feature-Poderes-bankais; Antigravity trabalha na integração visual em codex/design-poderes-criaturas. Este documento não foi enviado automaticamente ao aplicativo.

## Prompt para executar

Corrija as mandíbulas da hidra integrada e implemente a pose de transformação do jogador com PlayerAnimator, exclusivamente no checkout Bleach-Modpack-design, branch codex/design-poderes-criaturas. Confira cwd, HEAD, status e diffs; preserve as alterações de integração já existentes. Não está sozinho no projeto.

O Codex está corrigindo áudio e câmera no checkout Bleach-Modpack / Feature-Poderes-bankais. NÃO altere HyorinmaruCinematicClient.java, HyorinmaruEntranceAudio.java, HyorinmaruCameraPath.java, HyorinmaruTimeline.java, sounds.json, arquivos de áudio ou seus importadores. Não sincronize branches nesta rodada. Conserve duração de 400 ticks, commit 240 e cues 24/216/330; não mude dano, alcance, custos ou weather. Zaraki e Benihime permanecem intactas. Sem commit/push/merge/rebase/reset/stash.

Leia os contratos pertinentes, a skill bleach-design-lab e os documentos 94–97. Analise a gravação C:/Users/joaop/Downloads/Gravação/Gravação de Tela 2026-10-23 074526.mp4, especialmente 38–43 s. O modelo novo entrou no jogo, mas a mandíbula é controlada por IceDragonRenderer.setCustomAnimations -> HydraMotion.jaw; o clipe de laboratório não governa diretamente esse movimento. Verifique isso no código atual em vez de presumir que exportar o clipe basta.

### Mandíbulas

1. Confira hierarquia, geometria, pivôs, transformação de coordenadas Generic Model/GeckoLib e sinal de rotação. Não atribua a falha a um eixo antes de verificar. Os bones jaw_center/left/right existem, mas a atuação e o encaixe ainda precisam de revisão.
2. Escolha um responsável pelo canal de rotação e concilie GeckoLib com o controle procedural; não some nem sobrescreva movimentos por acidente. Leve em conta que sceneAge da manifestação começa depois do relógio global da transformação: confira os offsets reais.
3. Construa preparação, abertura progressiva, sustentação com movimento discreto de esforço e fechamento suave. Não precisa mastigar durante o sopro. A articulação deve permanecer conectada e os dentes não podem atravessar a boca. Dê uma pequena identidade temporal às cabeças sem dessincronizar o ataque.
4. Confira transformação E ataque triplo. Se necessário, ajuste origem visual da baforada à boca, preservando alcance e regras do dano. Mantenha fonte Blockbench, geradores, geo e animações coerentes.

### Pose do jogador

1. Inspecione como Asauchi, Hyorinmaru e as transformações existentes utilizam PlayerAnimator e seguram o item. Reutilize o sistema de prioridades e as travas atuais; não crie outro sistema global de pose.
2. Jogador em pé, pés apoiados, katana segurada com as DUAS mãos no cabo, à frente do corpo, lâmina apontada para baixo e afastada das pernas. Leve preparação do tronco e braços antes de “Bankai”, sustentação durante a manifestação, reação discreta à pressão do gelo e recuperação suave para combate.
3. Garanta que as mãos acompanhem a arma em vez de apenas aproximar os braços. Verifique primeira/terceira pessoa, modelo slim/normal, transformação e saída da pose. Considere mão principal esquerda conforme suporte existente.
4. Confira duração em ticks do clipe, playback speed, blending e prioridade diante de Better Combat, guarda, esquiva e movimento. Não deixe a pose ativa após término/cancelamento, morte, troca de item ou dimensão. Não confunda pose artística com uma nova regra de invulnerabilidade.

### Verificação e entrega

Confira se outro Minecraft/Gradle está ativo antes de iniciar; não encerre a sessão do usuário. Execute checks pertinentes e cliente Forge real em perfil/mundo descartáveis deste checkout. Capture perfil e três quartos próximos durante todo o ciclo de boca; registre também a empunhadura das duas mãos e a recuperação do jogador. Teste a visão padrão, F5 e o ataque triplo.

Uma sequência de poses em GIF não prova fluidez contínua. Grave vídeo real quando houver ferramenta disponível; se só conseguir amostras, declare o limite. Build verde não aprova a qualidade artística. Photon é VFX/partículas; não é shaderpack. Não afirme causa de corte de áudio: essa frente pertence ao Codex e a origem ainda está em revisão.

Documentação/evidências somente em Docs/poderes. Entregue arquivos alterados, versão/JAR, comandos/resultados, capturas reais e defeitos restantes. Não integre os arquivos de câmera/áudio do Codex por cópia. A integração entre as frentes será feita depois da revisão. Continue até uma versão experimental verificável; não declare homologação humana.
