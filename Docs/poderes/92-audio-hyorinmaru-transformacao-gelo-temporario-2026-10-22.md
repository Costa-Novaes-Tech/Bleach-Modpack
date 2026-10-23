# 92 — Áudio e transformação prolongada da Daiguren Hyōrinmaru

Data: 22/10/2026. Branch `Feature-Poderes-bankais`, referência `c26a4e3`. Sem checkout, commit, push ou alterações na Zaraki. Mudanças anteriores90–91 preservadas.

## Implementação

- Transformação passa de200 para400 ticks,20 segundos a20TPS. Movimento, crescimento, câmera, cristalização e liberação usam o mesmo relógio. A autoria de movimento é amostrada continuamente à metade da velocidade; não significa criação de novos keyframes ou substituição do modelo.
- “Bankai” fornecido pelo usuário: tick24/1,2s; duração1,05s. Nome completo: tick330/16,5s; duração2,5s. O nome termina antes do fade final.
- Trilha fornecida:20s, estéreo,44,1kHz, Vorbis; ganho0,35 (−9,12dB), redução adicional0,18 (−14,89dB) nas falas, entrada suave e fade19–20s. Falas mono,44,1kHz, normalizadas com headroom; sem síntese ou alteração do intérprete.
- Música no canal `MUSIC`, somente para o invocador; falas no canal `VOICE`, sem atenuação para o invocador e posicionais para observadores. Sliders de áudio continuam respeitados. Rastreamento tardio não reinicia a trilha nem reproduz falas passadas. Cancelamento/fim/troca de mundo param os sons desta cena. Cues têm proteção contra duplicação em ressincronizações.
- Rugido original da hidra preservado no tick216/10,8s. O efeito secundário de growl durante o nome foi reduzido de0,6 para0,12 para não mascarar a voz.
- Hidra aparece no tick88/4,4s; formação mais lenta e inspeção ampla. Commit da forma no tick240/12s, sem cobrança duplicada. Revelação aproxima a câmera e retorna ao controle até400.
- Baforada ofensiva216–288 ticks/10,8–14,4s; nuvem visual dissipa até312/15,6s. Alcance24→36 blocos. Mantidos cone frontal, bloqueio por paredes, exclusão do invocador/aliados, supressão ofensiva ao se afastar e um único dano-base8 por alvo/cena. Duração maior não multiplica os acertos. Lentidão e marca de fragilidade permanecem.
- Piso: revestimento visual das faces superiores reais com texturas vanilla de gelo compacto e gelo azul, crescendo perto do jogador e das raízes da hidra; respeita altura/forma do suporte. Não substitui blocos nem altera colisão, inventários ou saves. Evita fluidos e blocos com entidades; remove um fragmento se seu bloco original mudar. Dissipa entre17,6–20s; cancelamento remove imediatamente. Cache limitado a6 âncoras e distância96 blocos, sem chunks carregados para o efeito.
- Leitura de luz do piso usa a posição acima do suporte. A primeira captura detectou o piso escuro por leitura dentro do bloco opaco; corrigido antes da validação final.
- Weather: mantida a remoção implementada91. Transformação/kit não adquirem tempestade. Chuva natural não é limpa à força; rotina de recuperação dos saves climáticos antigos permanece.
- Rede2.35; clientes/servidor precisam da mesma build. Schema9 e revisão de técnicas4 preservados. N/H/B/C e demais kits sem reequilíbrio nesta rodada.

## Importação e avaliação de áudio

`tools/audio/import_hyorinmaru_user_audio.py` converte os três WAVs originais e gera relatório de hashes/filtros, além de uma prévia de escuta. MP3s alternativos não foram usados para evitar nova compressão antes da conversão. Originais em Downloads preservados.

Ferramenta local: imageio-ffmpeg0.6.0, wheel PyPI já em cache, instalada somente em `build/audio-tools`; sem PATH/global/admin. Executável e fontes têm hashes no relatório. Nenhum áudio enviado a serviço externo.

Medições dos OGGs decodificados, com volumes MUSIC/VOICE iguais: fala “Bankai” −12,3dBFS RMS contra trilha −54,48dBFS na sua janela; nome −13,89dBFS contra −46,29dBFS. Picos das falas decodificadas próximos de−0,8/−0,7dBFS. Isso verifica margem e separação de níveis, não substitui escuta humana. A prévia offline não inclui áudio capturado do dispositivo nem o rugido disparado pelo servidor.

## Validação

Base inicial:140 regressões/176 GameTests. Base atual:141 regressões/176 GameTests, `check build runGameTestServer prepareCombatTestPack` concluído. Após correções exclusivas do cliente, nova captura base concluída. Photon final:141 regressões/176 GameTests, `check build runGameTestServer prepareCombatTestPack preparePhotonTestPack` concluído. Todas as execuções offline, Java17/Forge47.4.10; warnings antigos de depreciação e Netty registrados, sem falha das tarefas.

Testes adicionais verificam cue sem replay, canais Vorbis corretos e baforada a32 blocos/fora de36 sem acertos repetidos. Continuam passando os casos de parede, deslocamento, interrupção e clima natural do ciclo91.

Cliente Forge real:10 cenários completos por perfil, capturas pelo framebuffer a cada4ticks de servidor, transformação acompanhada até412ticks. Inspeção direta das capturas base corrigidas e Photon confirmou piso iluminado, hidras, nuvem ampliada e restauração da grama ao final. Logs confirmam uma fala no tick24 e outra330 por ativação, sem erro de recurso Hyōrinmaru. Isso não é gravação do áudio do dispositivo.

Evidências: [verificação e metadados](qa/cycle92/verification.json), [marcos da transformação](qa/cycle92/transformacao-marcos.png), [movimento base](qa/cycle92/base-01_bankai_transformation.gif), [movimento Photon](qa/cycle92/photon-01_bankai_transformation.gif), [prévia de mixagem sem rugido](qa/cycle92/mixagem-previa-sem-rugido.ogg). GIFs são capturas reais amostradas, sem interpolação ou áudio; não representam captura de vídeo contínua nem benchmark. O diagnóstico inicial de iluminação também foi preservado.

Avaliação visual desta rodada,0–5: iluminação/revestimento plano4; legibilidade da baforada4; câmera/revelação4; fluidez procedural das hidras3. Empunhadura/modelagem fina permanecem no acabamento artístico do ciclo91; esta rodada prolonga o movimento existente. Áudio sem nota de escuta: apenas níveis, arquivos e disparo dos cues verificados.

JAR `build/libs/bleachmod-0.2.0.jar`, SHA256 `fcb78aa113a86036c5a6f100cda57e2556929e13198f48ce460d595ba7b7a9d1`. ZIP íntegro, todos os assets correspondem aos arquivos-fonte, pacotes base5jars e Photon7jars incluem a mesma build. Nenhuma instalação no Minecraft pessoal: usar o pacote de teste gerado.

## Próximos testes humanos e limites

Escutar com `Música` e `Voz/Fala` ligados; comparar a voz com rugido/efeitos, testar cancelamento, movimento e nova invocação. Conferir gelo em terreno irregular, fade do piso e restauração de câmera. Este revestimento afeta somente a aparência superior do terreno: não cria uma plataforma sólida ou dano no mundo.

Duas perspectivas em servidor dedicado, latência/rastreamento tardio e apreciação auditiva/artística permanecem pendentes. Mantidos os ajustes de modelo/empunhadura, autoria Blockbench, demais habilidades e a lista obrigatória do roadmap72. Este ciclo não homologa a feature inteira nem muda a ordem das próximas frentes.
