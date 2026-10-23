# 99 — Hyōrinmaru: fala completa e câmera dirigida

23/10/2026. Branch Feature-Poderes-bankais, base 48e8f58. Escopo autorizado: áudio de “Bankai” e câmera; Antigravity recebe mandíbula/pose no checkout de design. Sem commit, push ou merge.

## Diagnóstico e implementação

- Vídeo do usuário de 07:45:26 analisado na rodada anterior. A fala gravada correspondia ao stem até seu trecho final; não comprovamos interrupção pelo SoundManager. O importador anterior aplicava fade de saída fixo em 1,02 s por 30 ms.
- Usuário reenviou o WAV após informar que havia apagado os originais. A fonte tem 1,05 s e hash 0703c3834add12358156e06e1668fb02b4aa9d2c9527f08e1fb4dde7772cea87, igual ao registro do ciclo 92. Cópia preservada em tools/audio/source/Daiguren_Bankai_voice_clean.wav.
- Reprocessado diretamente da fonte, sem o fade fixo: ganho anterior preservado, ambiente discreto de 55/105 ms e 120 ms de silêncio final. Stem mono 44,1 kHz, aproximadamente 1,275 s, pico decodificado -1,0 dBFS. Não foi inventada ou reconstruída nenhuma sílaba. Música, fala do nome e rugido permanecem os mesmos.
- Evento da fala curta deixa de usar streaming e passa a preload. Isso reduz a dependência de abertura do stream no cue; não é afirmação de que streaming era a causa do corte percebido.
- HyorinmaruCameraPath define enquadramentos locais: aproximação do rosto durante a fala, recuo antes da emergência, órbita ampla, vista lateral da baforada e retorno ao jogador transformado durante o nome. FOV dirigido 42–64°, interpolação quintic com velocidades suaves nas junções.
- Retorno ao F5 anterior interpola orientação por ângulos, evitando colapso ao misturar focos opostos. A primeira captura revelou uma passagem perto demais do rosto; a versão final faz órbita ao redor do jogador entre ticks 360–400. Jogador local é ocultado somente na aproximação final da primeira pessoa para não mostrar interior da cabeça. Colisão de câmera existente e liberação por input mantidas. Letterbox entra/sai suavemente.
- Duração 400 ticks, commit 240, cues 24/216/330, dano, custos, alcance, weather e demais zanpakutos preservados. Nenhuma integração dos assets experimentais do Antigravity foi feita nesta rodada.

## Coordenação

Prompt copiável: [98 — mandíbulas e pose](98-prompt-antigravity-mandibulas-pose-hyorinmaru-2026-10-23.md). O Antigravity não deve modificar os arquivos de câmera, áudio ou relógio desta frente. Ele deve conciliar GeckoLib/Java da mandíbula e criar pose PlayerAnimator em pé, duas mãos no cabo e lâmina para baixo. Relógio de manifestação e relógio global exigem offsets explícitos. Integração entre branches será posterior, sem cópias indiscriminadas.

## Validação

- Check/build: aprovado, 142 cenários de regressão; log em qa/cycle99/check.log. Novo cenário verifica continuidade/limites do caminho e direção unitária no retorno entre visões opostas.
- Áudio: fonte original preservada, OGG decodificável e metadados de processamento em qa/cycle99/audio-refinement.json. Escuta e preferência artística permanecem do usuário; logs de cue não provam audibilidade.
- Cliente Forge real: duas execuções concluídas com exit 0, 10 cenários cada; primeira 4m22s, final 2m30s usando cinematicQaFast existente para pular a bancada Benihime. Cues da execução final observados em 24/217/330 (rugido um tick após o marco, dentro da tolerância existente). Logs e manifesto em qa/cycle99/client-final.log e verification.json.
- Capturas finais reais: qa/cycle99/camera-marcos.jpg e retorno-final.jpg. Close, revelação, baforada lateral e órbita de retorno ao F5 traseiro inspecionados; não houve gravação contínua de áudio/vídeo nesta rodada. Primeira execução preservada como antecedente e não representa o retorno corrigido.
- Primeira pessoa/F5 frontal, colisões em paredes, terreno irregular e interrupções adicionais continuam pendentes de playtest; as verificações matemáticas não substituem essas condições reais.
- GameTests não reexecutados nesta rodada de apresentação; não houve mudança de gameplay. Photon, observador e dois clientes não revalidados.

## Teste manual

Ativar Hyōrinmaru com o jogador parado: conferir “Bankai” completa, close inicial, revelação das três cabeças, baforada em plano lateral e nome com foco no jogador. Repetir em primeira pessoa e nos dois F5; verificar paredes, input de saída e retorno ao FOV anterior. Não usar o checkout de design para avaliar esta câmera sem integração explícita.
