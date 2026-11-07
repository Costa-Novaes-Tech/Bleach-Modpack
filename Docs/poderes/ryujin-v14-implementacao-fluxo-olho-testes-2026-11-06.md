# Ryūjin v14 — fluxo, olho, câmera, fogo e áudio

06/11/2026. Trabalho local em `Feature-Poderes-bankais`, base `9eb6a057`. Continuação autorizada do [plano v14](ryujin-v14-plano-completo-ordem-execucao-2026-11-06.md). Sem commit, merge ou push; sem modificar os checkouts Antigravity A Quincy e B P0/Rukia. **Candidato experimental, ainda não promovido ao caminho padrão nem homologado artisticamente.**

## Implementação local

- **Transporte do selamento:** quatro correntes, oito segmentos cada, com curva quadrática, afilamento, pulsação e tangentes compartilhadas nos encontros. `RyujinSealFlow` calcula trajetórias; `RyujinFlowRenderer` orienta a largura em relação à câmera. Destinos distribuídos na lâmina; origem acompanha o centro solar. Substituem os 32 cards verticais antigos durante a absorção, entram na contagem de quads e terminam até202.
- **Câmera:** aproximação frontal facial antes de Bankai e transição para o lado da mão da arma no selo. Chega enquanto ainda existe fluxo e segura a lâmina carbonizada antes de voltar ao nome. Curva amostrada em sub-ticks; versão anterior permanece como controle. O detalhe da espada pode cortar parte da cabeça: avaliar essa escolha e a colisão no cliente, sem deduzir aprovação do teste matemático.
- **Olho direito:** camada no `PlayerModel` real, após a transformação da cabeça, respeitando profundidade/invisibilidade. Coordenada negativa X é direita anatômica nesse modelo, independente da mão. Envelope20–68. Candidato inicial tinha seis microchamas separadas, com aparência de agulhas; d usa quatro microchamas e quatro segmentos de rastro unido, cota total oito. Não é overlay nem nova fonte de luz.
- **Chamas:** vida das partículas quentes encurtada durante a absorção; emissão concentrada na lâmina, encerrada até202. Depois, cinza curta/esparsa. Flipbook autoral de duas variantes × oito frames, fases estáveis por card e variação de altura por grupos da cortina. Mantidos limites288 cards/192 partículas, sem fogo físico novo durante a apresentação. Alguns sprites ainda lembram cones; material não está aprovado.
- **Áudio d:** sampler `RyujinAudioEnvelope` separa pré-aquecimento, fogo e rugido. Pré-aquecimento termina na explosão; fogo/rugido decaem176–202 e param no selo. Rugido controlado também para observador posicional, conservando duck de fala e limpeza por dono. Trilha/voz/SFX locais continuam com direitos separados; não foram incorporados ao JAR por esta alteração. Regressão de ganho não prova escuta nem mixagem final.
- **Telemetria d:** CSV mantém relógio do ensaio e do cliente; JSON acrescenta p50/p95/p99 em oito fases usando o relógio do cliente. Trata-se de cadência entre frames, não tempo GPU. Capturas continuam declaradas no relatório.

Ativação por `-PryujinV14Pilot=true` no perfil QA; a propriedade também ativa o alinhamento solar do piloto. O volume solar contínuo g segue aplicado por resource pack QA próprio. Fora desse perfil o JAR normal continua com os assets solares anteriores. Não apresentar a existência do código como promoção padrão.

## Autoria e assets

`tools/models/lab/codex/ryujin01/build_seal_v14.py` gera os três materiais RGBA e o estudo `ryujin_seal_v14_scene_lab.bbmodel`. Estudo d: **367 cubos/106 bones**, incluindo manequim, efeitos antigos de staging, 32 segmentos e oito peças do olho. Não é o asset produtivo de280 cubos, não substitui PlayerAnimator nem valida a camada de skin do Minecraft. O olho do estudo usa eixos Y-up/+X direito e plano fora do manequim; o runtime usa coordenadas da cabeça vanilla. A equivalência é de composição, não uma exportação direta do sampler Java.

Prévia/export nativo a: Blockbench Codex/8788, `check_model=0`, geometria real166414bytes; imagens em [media/ryujin-seal-v14-20261106](media/ryujin-seal-v14-20261106). Essa prova antecede as mudanças b/d; não atribuí-la ao arquivo regenerado atual. Reexportação/validação final deve ser registrada abaixo quando executada. Nenhuma ação em8787.

## Execuções e controles preservados

| Controle | Resultado observado | Limite |
|---|---|---|
| Compilação inicial | Falha por referência `mc` ausente no callback de FOV, corrigida; check seguinte182 regressões. | Falha preservada em `build/visual-lab/v14-seal-check.log`; não ocultar como execução verde. |
| a com vídeo | Exit0, porém38 quadros aceitos/81 descartes de fila/284 repetições, p95 8250ms e apenas duas amostras na sustentação. | **Captura rejeitada para fluidez/desempenho.** Sem causa isolada comprovada. [Pacote da falha](qa/codex/v14a-capture-stall-20261106). |
| a sem vídeo, duas repetições | p95 sustentação55,83/22,92ms; cleanup verdadeiro. | Com screenshots. Correntes finas e partículas antigas escondendo a lâmina motivaram b. |
| b interrompida | 183 regressões; encerramento durante carregamento, `ConcurrentModificationException` em `TextureManager.close`, sem QA. | `BUILD SUCCESSFUL` não prova cena executada. Contexto de interrupção preservado; causa do erro não isolada. |
| b retomada | Duas cenas noturnas, p95 28,03/15,24ms; limpeza no selo/fim. Olho no lado direito e fluxo visível. | Sem vídeo/escuta; rastro separado ainda parecia agulhas. [r02](qa/codex/v14b-eye-seal-r02-20261106). |
| c fogo autoral | Check183; duas cenas noturnas, p95 24,42/15,34ms; até264 cards/192 partículas/1 luz; cleanup verdadeiro nos dois relógios. | Screenshots ligados, áudio mudo, sem vídeo. Material tem repetição/cones a refinar. [r02 com fonte e hashes](qa/codex/v14c-authored-fire-r02-20261106/manifest.json). |
| d matriz | Check184 e 12 cenários concluídos, exit0. Cleanup no selo e término dos oito casos da transformação; até264 cards/192 partículas/1 luz. | Parede teve só um PNG apesar de telemetria completa; não aprovar câmera pela contagem de casos. Quatro casos do kit não chegam a302. [Resumo e lacunas](qa/codex/v14d-matrix-20261106/summary.json). |
| d parede AV | Exit0, vídeo16,3s/20fps, 258 quadros aceitos/0 descartes de fila/68 repetições, encoder0; cleanup verdadeiro. | Primeiros quadros têm congelamento: CSV registra intervalo2042ms e salto2→29; não prova olho/fala inicial. Restante mostra sol/fluxo/lâmina, mas a câmera corta parte da cabeça no detalhe. [Metadados e imagens](qa/codex/v14d-wall-av-20261106/manifest.json). |
| d sem captura | Três repetições IrisXe1280×720/HIGH, p95 sustentação21,19/15,38/14,26ms, cleanup verdadeiro em todas. | Vsync off/cap120, sem PNG/vídeo/áudio/Blockbench concorrente; não é medição GPU nem prova de60FPS em todo cenário. [Oito fases e CSV](qa/codex/v14d-performance-20261106/summary.json). |

Os tempos b/c não demonstram ganho causal contra v13: mesmo host com variabilidade, aquecimento e captura de PNG. Fonte bruta nos logs `build/visual-lab/v14*-client.log`; baselines de código a/b/c preservados em `build/visual-lab`. QA gráfico usa mundos `Benihime-QA-68-*` e perfis `run-benihime-qa/visual-*`, sem saves pessoais.

## Próximos gates e ordem

1. Matriz d concluída com lacunas registradas; repetição da parede mostra fases tardias, não homologa abertura. Quatro casos do kit encerram antes do fim da transformação e não provam cleanup302. Rever detalhe da lâmina sem cortar a cabeça e sem perder leitura no escuro.
2. Estudo nativo/export final conferidos; refinamento artístico do olho/cortina ainda aberto. Não confundir repouso com pose temporal nas vistas detalhadas.
3. Captura noturna em duas passagens e medição sem captura concluídas abaixo; falta escuta humana para ritmo, rugido e selo, além de calibração audiovisual mais precisa.
4. Refinar cortina/corona e convecção do núcleo com controles de material separados. Onda de pressão no solo precisa de projeção/oclusão, orçamento e cliente; ainda não implementada. Glint final e assentamento do punho também não estão declarados entregues.
5. Ensaios separados para cabeça yaw/pitch/oclusão/skins/armadura, 4:3, reload d, dedicado e dois clientes. Shader pack/calor/bloom, Photon × AAA e cosméticos seguem os gates experimentais do plano; não inferir suporte deles pelo shader solar próprio.
6. Revisão humana e decisão explícita de promoção dos assets/caminho padrão, depois licença de distribuição e integração. Não existe merge automático de A/B.

## Fechamento das provas desta rodada

**Blockbench final:** estudo367/106 reaberto pela ponte8788 e exportado pelo codec `bedrock`, geometria171027bytes. O primeiro check encontrou cinco sobreposições dos elementos novos do olho e oito avisos de lado sem par. O gerador separou os planos das microchamas e do rastro; novo check: zero sobreposições, **oito avisos `unpaired_side` mantidos**, pois a assimetria direita é uma exigência humana. Não espelhar o efeito para satisfazer a heurística. [Fonte/export/check/previews com hashes](media/ryujin-seal-v14-20261106/d-source-manifest.json).

Previews temporais separados:39/188/208, frente/perfil direito. A escala automática do conjunto deixa o olho pequeno; dois detalhes adicionais usam câmera explícita e mostram **geometria em repouso**, pois `preview_animation` restaura a pose antes de `screenshot_views`. Não rotular esses detalhes como tick39. O cliente continua sendo a prova principal do olho. Removidas as duplicatas de canal/tempo202 no estudo e desativados os sparks antigos de staging; **idempotência final e ausência de duplicatas passaram**. Fonte bbmodel SHA `9d02d06e72b27acff6d8a215142be2b98dd87d6f4561a1d403396cbf90861d1b`, geometria com106 bones. Instância própria encerrada antes do benchmark; nenhuma ação em8787.

**Áudio e observabilidade:** a captura do motor abriu `Wave File Writer`, WAV48,34s/48kHz/estéreo, SHA `c9c9ea6f27172e9bb2054086c3b2b4dd589b62a80dc0f763f277cfac39e7d36a`. Pico por canal−2,6dBFS/RMS−29,9dBFS, sem escuta ou true-peak oversampling. O inspetor antigo fazia downmix para mono e reportou+0,4dBFS: isso não era o pico dos canais originais. Correção preserva canais ao decodificar; controles de seno mono/estéreo correlacionado a0,5 deram−6,0dBFS/1s nos dois casos. Não reduzir a mixagem às cegas por aquele valor de downmix. Resultados históricos permanecem históricos, sem reescrever suas medições como se fossem do novo método.

Prévia local de parede em `build/visual-lab/v14d-ryujin-preview-av.mp4`, com offset aproximado25,606508s por timestamps de arquivo/processo, sem sincronismo em amostras. WAV/stems/MP4 continuam ignorados e fora do JAR/Docs. Não apresentar a prévia de parede como prova completa da abertura.

**Noturno após aquecimento:** nova execução exit0 em1m55, duas passagens no mesmo processo. r01:304 quadros/0 descartes/12 repetições; r02:320 quadros/1 descarte/7 repetições, encoder0,16,35s/20fps. O contato temporal r02 mostra abertura/olho/Bankai, panorama, fluxo e espada final. Cleanup no selo/fim verdadeiro; máximo264/192/1. Não é benchmark: p95 sustentação26,20ms com captura. [Prova e fontes](qa/codex/v14d-night-av-20261106/manifest.json).

Prévia para revisão: `build/visual-lab/v14d-night-r02-preview-av.mp4`, SHA `6d4f7ecea0483111a50fb6538ca8193dc78e7f994bbbed8bfdceb3da29d42d7a`, H264+AACestéreo. WAV64,94s/48kHz, pico por canal−2,9dBFS; sincronização aproximada44,631027s. **Não houve escuta do agente nem aceite artístico.** Ainda há repetição/cones na cortina, brilho forte nos aros e corte de cabeça na câmera da espada; avaliar leitura noturna da arma e preenchimento frio sem reativar a fonte quente após202.

As repetições usam a mesma área do mundo QA: fogo físico de gameplay pode aparecer **entre** cenas depois da suspensão terminar e continuar como fogo preexistente na próxima. Isso não é prova de emissão física durante a cinematográfica nem comparação com terreno rigorosamente idêntico. O próximo harness deve preparar/aquecer áreas equivalentes novas por repetição, preservando a regra de não apagar fogo alheio. As medições acima descrevem o ensaio observado e não um ganho causal de versão.

[Hashes das fontes finais do candidato](qa/codex/v14d-runtime-sources-20261106/manifest.json). Comando de revisão local, sem gravador/microfone: `./gradlew.bat runClient -PshimmerPilot=true -PbenihimeQa=true -PcinematicQaFast=true -PryujinBankaiQa=true -PryujinBankaiQaCase=1 -PryujinV14Pilot=true -PryujinVisualQaProfile=v14d-audio --offline --console=plain`. Esse perfil já contém os packs locais; omitir `qaAutoExit` mantém o mundo QA aberto. Não copiar os packs restritos para distribuição.

**Skills:** `bleach-mod-dev` e `bleach-model-pipeline` atualizadas com continuidade das ribbons, espaço anatômico da cabeça, vínculo versão/export e telemetria por fase. Ambos frontmatters passaram `quick_validate.py` em UTF-8. Isso é melhoria de instrução recuperável; transferência para outra feature/IA ainda não demonstrada.

## Aprendizado reutilizável

Uma curva de centros contínua pode produzir fendas se cada segmento recalcula sua largura pela própria corda. Compartilhar tangentes analíticas e larguras nos pontos comuns melhora a continuidade; ainda testar vistas degeneradas/câmera na direção da curva. No olho, validar direita anatômica no sistema da **cabeça renderizada**, separado da mão dominante e da direita da tela. Um sampler correto não garante leitura artística: a versão b estava no lado correto e ainda parecia agulhas.

Falha de captura e falha do efeito são resultados diferentes. Expor arquivos ausentes, quadros repetidos, relógio local e captura ativa; nunca usar um vídeo congelado como benchmark ou herdar sucesso nativo de um asset anterior após regeneração. Métricas por fase ajudam a localizar custo da abertura/selamento que uma média da sustentação omite.
