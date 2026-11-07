# Ryūjin v12 — luz, distribuição e desempenho

Data: 06/11/2026. Trabalho local em `Feature-Poderes-bankais`, base `9eb6a05`; sem commit, merge ou push. Codex responsável pela Ryūjin; Antigravity A Quincy e B P0/Rukia preservados. Este relatório é incremental: pendências continuam abertas até prova específica.

## Implementado nesta rodada

- Sampler puro `RyujinFireLayout`: lâmina 36 cards, base 40, cortina traseira sustentada 108, corona 48 e absorção dirigida 32. Teto rígido 288/instância, máximo observado 264. A turbulência usa ticks reais; a sustentação do roteiro não congela o movimento.
- Base ancorada ao chão e limite de cintura no semiplano dianteiro. Cortina em três profundidades, bordas menores e massa traseira que persiste enquanto o sol está exposto. Perfis LOW/MEDIUM/HIGH reduzem cards sem mudar o relógio.
- Partículas próprias rastreadas por dono, limitadas a 192 vivas/instância; chama removida no selamento e limpeza na saída da cena. Não limpa partículas de terceiros. Null blade tratado.
- Adaptador Shimmer opcional por reflexão, uma fonte por cena visível, máximo quatro. Cor quente, raio até 18 blocos; acompanha o sol/absorção, remove no término/cancelamento/reload. Sem criação de blocos de luz nem dependência obrigatória no mod base.
- Perfis QA separados `run-benihime-qa/visual-<nome>`, contadores e cadência real entre quadros em CSV/JSON. Medição não é tempo GPU e capturas afetam resultados.
- Regra v11 preservada: suspender novas colocações de fogo físico durante a cena, sem apagar fogo prévio.

## Provas obtidas

1. `build/visual-lab/v12-fire-base-client.log`: `check build`, 178 regressões, cliente `day_back`, saída 0. Ajuste posterior da forma da cortina foi compilado e passou `check` no teste de luz ON.
2. `build/visual-lab/v12-shimmer-night-client.log`: Shimmer 0.3.0, cliente noturno, API carregada, saída 0.
3. Controle ON: `run-benihime-qa/visual-light-probe/ryujin-bankai-qa/cycle118-1793964161621/02_night_front`.
4. Controle OFF: `run-benihime-qa/visual-light-probe/ryujin-bankai-qa/cycle118-1793964306714/02_night_front`.

Inspeção dos dois `screenshots/frame-t144.png`: pilares brancos e terreno recebem luz quente apenas no controle ON; isso distingue iluminação do ambiente do brilho emissivo do modelo. Comparação qualitativa; partículas não têm estado exatamente idêntico. Iluminação depois do selamento deve ser conferida separadamente.

Hardware detectado: Intel Iris Xe. Primeira medição ON, com screenshots: p50 12,81 ms, p95 28,09 ms, p99 112,95 ms; OFF p50 14,81 ms, p95 23,48 ms, p99 26,89 ms. Execuções frias e com captura: **não** comprovam custo isolado nem homologam desempenho. Repetir sem screenshots, informar resolução/configuração e comparar mediana de execuções equivalentes.

## Recursos isolados

Shimmer Forge 1.20.1 0.3.0, versão Modrinth `BeAfFYQQ`, SHA-256 local `cbf4e4379e6ce0dcfcedd21444fd146a9231d537a14790ec8736e4dc4c7faa9a`. API conferida no JAR oficial; piloto local não significa inclusão obrigatória no pack.

Effekseer portátil 1.80.8 baixado da distribuição oficial, ZIP SHA-256 verificado `99a61cd6dae02a63a2025e615e73f0c8f63683e9c141539bc0ab25b6f0b64c45`, em `build/visual-lab/tools/effekseer-1.80.8`. Até este checkpoint não foi executado nem criado um efeito autoral. Presença do editor não prova integração AAA.

## Gates ainda abertos

- Refinar aparência repetitiva das placas da cortina; conferir lâmina/rosto em close e sol completo.
- Medir qualidade reduzida e iluminação sem screenshot; testar Embeddium/Oculus/Complementary separadamente, sem presumir compatibilidade Shimmer.
- Produzir e comparar VFX autoral Photon/AAA em cena equivalente; bibliotecas presentes não equivalem a efeito aprovado.
- Recarga de recursos em teste pontual; morte/dimensão/desconexão, servidor dedicado com clientes reais e dois clientes ainda sem homologação. Matriz singleplayer atualizada descrita abaixo.
- Vídeo contínuo e áudio real do jogo, mixagem e adaptação musical 12→15 s; licença/atribuição de redistribuição da música ainda não fixadas.
- Aceite artístico humano. Skills já atualizadas com lições verificadas; nenhum gate aberto deve ser rotulado como concluído.

## Controle ampliado e captura audiovisual

O acabamento posterior foi conferido no caso noturno `cycle118-1793965460909/02_night_front` do perfil `visual-music15`: tintura quente reduz o branco repetido das bases e a distribuição da cortina usa mais variação lateral/profundidade. [Capturas selecionadas e hashes](qa/codex/v12-finish-medium-20261106/manifest.json). O sol continua propositalmente estilizado em aros; isso não é homologação da silhueta final.

Seis replays sem screenshots, 1280×720, Iris Xe, vsync OFF, cap 120, fonte solar ON, mesma cena noturna: [métricas e CSVs](qa/codex/v12-performance-20261106/summary.json). HIGH usou 264 cards; MEDIUM 132, ambos até 192 partículas próprias e uma luz. P95 HIGH: 21,33 / 26,59 / 13,41 ms; MEDIUM: 46,82 / 17,75 / 14,87 ms. Aquecimento e variação do host compartilhado são grandes; **não há prova suficiente de ganho isolado do MEDIUM**. Mantido HIGH como padrão, níveis menores disponíveis. Nos três replays MEDIUM, `post_seal_clean=true` (cards/chamas/luzes zerados após 202) e `post_end_clean=true` (todas as partículas próprias zeradas após 300). Isso cobre término normal de uma cena, não F3+T/dimensão/multiplayer.

Shader: `v12-shader-light-low.log`, Embeddium 0.3.31 + Oculus 1.8.0 + Complementary Reimagined r5.9.3 LOW + Shimmer 0.3.0. Falhou ao entrar no mundo userdev com `Cannot find class net/fabricmc/fabric/api/blockview/v2/FabricBlockView`; referência encontrada em `WorldSlice` do JAR Embeddium. [Log e crash preservados](qa/codex/v12-shader-failed-20261106/client.log). Não atribuir isoladamente ao Shimmer nem instalar Fabric API em Forge para contornar por tentativa. Há [relato semelhante no projeto Embeddium](https://github.com/FiniteReality/embeddium/issues/395); não prova que a causa seja idêntica. A [documentação Shimmer para shader authors](https://github.com/Low-Drag-MC/Shimmer/wiki/Shader-Compatibility.-%28For-Shader-Dev%29) também exige integração do shader para luz colorida; presença de Oculus não prova iluminação compatível. Pack normal preservado.

Música: `install_candidate_music_15s.py` gera uma alternativa local usando `atempo=0.8`, sem mudar a altura das notas, com fades curtos e duração 15 s. Fonte original e packs de 12 s preservados. OGG SHA-256 `8169f1dceea11a0d28f4bb958e388abe024f8c8ee7fee989d881d78232fa88a5`, 48 kHz estéreo, pico −4,8 dBFS/RMS −19,7 dBFS. Tempo musical foi alterado: qualidade artística precisa de escuta. Sem redistribuição/alteração do JAR.

O backend [Wave File Writer do OpenAL Soft](https://github.com/kcat/openal-soft/blob/master/alsoftrc.sample), configurado só no processo QA com [ALSOFT_CONF e ALSOFT_DRIVERS](https://github.com/kcat/openal-soft/blob/master/docs/env-vars.txt), gravou efetivamente a mixagem do jogo; log confirma dispositivo `Wave File Writer` e os dois packs locais carregados. Primeira captura WAV: 36,72 s, 48 kHz, pico −0,7 dBFS. Sem microfone, loopback do desktop ou envio externo. Métricas não são escuta nem teste do hardware de saída.

`RyujinQaVideoRecorder` foi testado em `visual-video15`, `cycle118-1793965779779/02_night_front`: framebuffer real, H.264 1280×720/20 fps, 16,3 s, 296 quadros capturados e 30 repetições para preservar o tempo, zero descartes por fila e encoder exit 0. Relógio de captura/cadência é separado do roteiro. Repetições explicitadas no JSON; vídeo não é benchmark. WAV deste mesmo processo: 36,64 s, 48 kHz, pico −1,0 dBFS. Prévia com AAC estéreo criada em `build/visual-lab/v12-ryujin-preview-av.mp4`, SHA-256 `a06d73e1f6b5f40e8ee0501f286dbaad5cfa12ccc9056143ce1808695788b01b`. Sincronização usa timestamps de criação do WAV e início do framebuffer, offset 16,687578 s: **aproximada**, sem calibração em amostras. Vídeo/áudio ficam em diretórios ignorados porque os stems de voz têm direitos separados. Não commitá-los por arrasto.

Skill `bleach-mod-dev` atualizada com relógios, cotas efetivas/cleanup, controles de luz, variabilidade de frametime e captura OpenAL. Validador oficial executado com `python -X utf8` (Windows cp1252 falhou ao ler Unicode sem esse parâmetro): `Skill is valid!`. Corrigido o inspetor de mídia para criar o diretório de saída em análise somente de áudio, validado no WAV/OGG reais. Nenhuma alteração em skill global ou no trabalho Antigravity.

## Matriz final e gate do servidor

`build runGameTestServer -I tools/visual_lab/runtime_qa.init.gradle --offline --console=plain`: saída 0, **296/296 GameTests obrigatórios**. `check` da captura contínua confirmou **178 regressões**. JAR base compilado, sem incluir Shimmer ou música candidata no arquivo.

Cliente final com Shimmer/HIGH: **12/12 cenários executados**, saída 0, `BUILD SUCCESSFUL in 4m 50s`. [Índice dos cenários, contadores e perspectivas finais](qa/codex/v12-final-matrix-20261106/summary.json), [log cliente](qa/codex/v12-final-matrix-20261106/client.log), [servidor/build](qa/codex/v12-final-matrix-20261106/server-build.log), [hashes dos fontes e JAR da matriz](qa/codex/v12-source-manifest-20261106.json).

Nos oito casos da transformação, os contadores confirmam limpeza de cards/chamas/luzes depois do selamento e de todas as partículas próprias no término. Perspectivas finais: dia terceira traseira; noite/frontal e canhoto frontal; primeira pessoa retorna a primeira; movimento/cancelamentos retornam à terceira traseira. Cancelar antes do commit mantém Shikai; depois mantém Bankai. Os quatro casos de exército/corte terminam no tick 220; `post_end_samples=0`/`post_end_clean=false` significa que **não observaram o marco 302**, não falha de cleanup desses casos. Não convertê-los em prova adicional do término da transformação.

Inspeção: t50 canhoto espelha lâmina/chamas; t300 primeira pessoa retorna com item; **perto da parede, a câmera segura recua menos e corta parte superior do sol**. A execução técnica passou, mas o requisito artístico de sol inteiro **não** foi aprovado nesse espaço estreito. Continua pendente desenhar uma alternativa de enquadramento para confinamento (sem sacrificar o personagem ou atravessar blocos). O HUD de estatísticas do Shimmer visível fora da cinemática é habilitado pelo próprio mod em ambiente de desenvolvimento, conforme `LightCounter.Render.enable = isDevelopmentEnvironment()` no source JAR; não é parte do HUD produtivo do Bleach.

### Estado dos gates após esta rodada

| Gate | Estado e próximo passo |
|---|---|
| T0 fogo físico | Regra implementada e GameTest passou; fogo existente e retomada preservados. |
| T1 distribuição | Novas lâmina/base/cortina/corona e absorção em cliente real; aceite artístico e plano estreito pendentes. |
| T2 iluminação | ON/OFF Shimmer solo comprovado; shader+Oculus userdev em conflito, pack empacotado ainda sem prova. |
| T3 Photon × AAA | Aberto: portátil Effekseer preparado, sem efeito autoral comparável integrado. Não bloqueia fallback nativo. |
| T4 ciclo de vida | Normal e dois cancelamentos comprovados em singleplayer; reload em teste pontual, morte/dimensão/logout/2 clientes pendentes. GameTest dedicado não equivale a 2 clientes reais. |
| T5 cosméticos | Aberto: convivência animada em Forge empacotado e perfis gráficos opcionais. |
| T6 audiovisual | Vídeo contínuo e mixagem OpenAL capturados; sincronização aproximada, escuta/mixagem artística e direitos de redistribuição pendentes. |

Próxima decisão artística pode usar a prévia local de 16,3 s; o ambiente normal e saves pessoais não foram alterados. Nenhuma aprovação artística foi inferida de testes verdes.

## Ajustes posteriores à matriz

O teste pontual de recarga em plena sustentação do sol concluiu (`v12-resource-reload.log`, saída 0): recarga solicitada em 144, concluída, luz presente nos 46 frames medidos entre 150–175 e cleanup pós-selamento/término correto. [Metadados e frames](qa/codex/v12-resource-reload-20261106/manifest.json). A inspeção t160, porém, revelou os aros retomando uma pose radial diferente do controle sem reload; sucesso técnico da recarga não equivale a continuidade artística.

Correções locais em validação posterior: `RyujinBankaiCamera.confined` calcula mira/FOV pelos limites dos pés e do sol, reserva letterbox, preserva olho seguro e eixo frontal, com transição proporcional à obstrução e FOV limitado a 110°. Primeiro teste restrito mostrou núcleo/aros completos, ainda com raios na borda superior; margem aumentada para o alcance externo. Regressão geométrica passou em 4:3 e 16:9 e preserva o plano livre. `Sun` usa o hook protegido `AnimationController.adjustTick` verificado na GeckoLib 4.8.4 para devolver o tempo da cena após o bookkeeping padrão, evitando que reload reinicie o clip. **179 regressões** passaram; conferir o resultado visual pontual antes de chamar essas duas correções de homologadas.

Teste manual de referência, sem abrir saves pessoais: executar `./gradlew.bat runClient -PshimmerPilot=true -PbenihimeQa=true -PcinematicQaFast=true -PryujinBankaiQa=true -PryujinBankaiQaCase=1 -PryujinVisualQaProfile=video15`. O perfil criado nesta rodada contém os packs locais de SFX e música; omitir `qaAutoExit` mantém o mundo QA aberto ao terminar. Sem as variáveis `ALSOFT_*` do gravador, o cliente usa a saída normal de áudio. Não copiar JARs de produção de mods para o userdev como substituto das dependências remapeadas. Shimmer continua opcional e não foi adicionado ao pack normal do usuário.

**Resultado posterior de câmera/reload:** `v12-confined-reload-final.log` saiu 0, com 179 regressões. O [t160 final](qa/codex/v12-confined-reload-final-20261106/screenshots/frame-t160.png) mostra núcleo, aros e raios dentro do quadro na parede do fixture e aros em pose contínua depois do reload. [Manifesto](qa/codex/v12-confined-reload-final-20261106/manifest.json). Confinamento extremo ainda pode exceder o teto de 110°; não prometer enquadramento universal.

**Ressalva de cleanup registrada, não apagada:** nesse teste, `post_seal_clean=false`: dois frames no tick de ensaio 203 ainda tinham 32 cards/165 chamas próprias, com zero luzes; no 204 já estavam limpos e `post_end_clean=true`. Não chamar esse critério estrito de aprovado. Foi acrescentada telemetria do tick local da apresentação, mantendo a métrica anterior intacta. Repetição [auditoria de relógios](qa/codex/v12-playback-clock-audit-20261106/manifest.json), `v12-playback-clock-audit.log`, saída 0: ambos os critérios de selamento e término passaram, com `max_trial_playback_delta_ticks=0`. A ocorrência anterior **não se reproduziu**; como não havia tick local naquele CSV, não atribuir definitivamente a diferença a rede/reload. Manter monitoramento desse limite em futuras validações, sem esconder a execução que falhou. Fonte e runtime seguem removendo fogo próprio no fim da fase local; a auditoria não alterou o cutoff.

**Nova solicitação humana:** refinamento estrutural/material do sol passa à [v13](ryujin-v13-sol-continuo-acabamento-2026-11-06.md), pois suas peças destoam dos novos FX. A prévia audiovisual v12 antecede os últimos ajustes de parede/reload e a nova geometria; as capturas pontuais acima são a prova desses ajustes. Nenhum aceite artístico inferido.
