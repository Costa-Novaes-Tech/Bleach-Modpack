# Ryūjin v16 — acabamento e compatibilidade gráfica

07/11/2026 · `Feature-Poderes-bankais` · base `9eb6a057`. Candidato local. Trilha B e relógio de 300 ticks usados nos ensaios. Plano aprovado (arquivo local `ryujin-olho-paredao-trilha-b-2026-11-07.md#próxima-rodada-de-acabamento--proposta-de-0711`).

O repositório inclui quadros selecionados. Os manifestos, logs e sequências integrais estão nas pastas locais indicadas.

## Acabamento

- **Áudio.** A faixa da Bankai segue o slider Música. Quando a partitura local começa, a faixa vanilla ativa do `MusicManager` é interrompida e o gerenciador volta a poder iniciar música assim que a partitura deixa de estar ativa. Isso preserva voz, efeitos, ambiente e volume escolhido. A prova força `Musics.GAME` antes e tenta reiniciá-la durante e depois da cena. O primeiro cliente empacotado com áudio (arquivo local `qa/codex/v16-base-b-20261107/manifest.json`) registrou `started=true blocked=true released=true canResume=true sliderPreserved=true`. A gravação de vídeo desse cliente não contém a saída do dispositivo. A captura OpenAL por WAV falhou no launcher empacotado e foi substituída pelo dispositivo normal no ensaio.
- **Olho.** A chama mantém a cobertura aprovada. A cauda se conecta à borda junto à têmpora anatômica direita, nasce com 0,076 bloco de largura, vira para cima e para trás e afina com continuidade de UV, largura e opacidade. Uma face cruzada preserva o volume de perfil dentro do mesmo orçamento. O fade termina no tick 202. [Quadro em perfil com skin composta](qa/codex/v16-eye-profile-20261107/screenshots/frame-t039.png).
- **Paredão.** Dez labaredas laterais baixas substituem dez cartões da base. Seis plumas de carvão substituem seis cartões da cortina intermediária, mantendo os 54 do fundo. As labaredas laterais seguem o terreno por amostra de colisão em cache a cada tick. O pico da cena continuou em 278 cartões, 192 partículas e uma luz no perfil HIGH com Shimmer.
- **Sol.** A variante de 132 trechos finos ficou mais leve que os 96 cubos largos do controle, com emendas ainda perceptíveis. A terceira variante usa três faixas curvas de 64 quadriláteros com vértices adjacentes coincidentes, aproveitando os bones e o relógio GeckoLib. O núcleo mantém o volume de 184 cubos e recebe outro campo de cor em gradiente. Os dois resource packs experimentais são reproduzíveis por `tools/models/lab/codex/ryujin01/build_solar_finish.py`; a seleção contínua usa o pack Full e a propriedade `bleachmod.ryujinContinuousSolar`. A ponte Blockbench Codex em 8788 não respondeu. A malha foi conferida no Forge a partir da geometria anterior e da conversão de coordenadas verificada no GeckoLib, sem nova exportação nativa do editor.
- **Câmera e selo.** O alvo do close baixou em direção à lâmina e o enquadramento com colisão limita a inclinação para preservar os pés dentro da área útil. O fade do calor e a limpeza do fogo visual continuam no selo. As cinzas frias continuam ancoradas na lâmina.

## Forge 1.20.1 empacotado

Foi usada a instalação isolada do laboratório com Forge 47.4.10, Java 17 e resolução 1280×720. O ensaio automatizado do JAR empacotado exige três propriedades de QA, pasta de laboratório, mundo novo com prefixo conhecido e servidor integrado. O launcher `tools/visual_lab/ryujin_matrix.py` preserva os hashes do JAR e dos mods em cada perfil e confirma o número de cenas concluídas.

| Perfil | Resultado registrado |
|---|---|
| Base, Embeddium 0.3.31 e Oculus 1.8.0 com shaders desligados | A cena noturna completou nos três perfis. |
| Oculus + Complementary r5.9.3 | A cena completou. Reapareceram erros de `BIOME_PALE_GARDEN`, `BIOME_SULFUR_CAVES` e `endFlashIntensity`. |
| Oculus + Complementary r5.2.2 LOW, renderer inicial | Dia e noite completaram, inclusive reload na noite. A captura mostrou que o shader ocultava as camadas personalizadas. Quadro de controle (arquivo local `qa/codex/v16-oculus-hidden-control-20261107/screenshots/frame-t112.png`). |
| Oculus + r5.2.2, tipos de renderização compatíveis | O olho, o sol e o paredão reapareceram no cliente real. O primeiro ensaio expôs o núcleo demais. Primeira captura compatível (arquivo local `qa/codex/v16-oculus-visible-first-20261107/screenshots/frame-t112.png`). |
| Oculus + r5.2.2 LOW, material emissivo da revisão | Dia e noite completaram, inclusive reload. O preset padrão saturou o núcleo e clareou excessivamente o fogo de dia. Dia (arquivo local `qa/codex/v16-oculus-r522-day-20261107/screenshots/frame-t112.png`) · Noite (arquivo local `qa/codex/v16-oculus-r522-night-20261107/screenshots/frame-t112.png`). |
| Oculus + r5.2.2 LOW, composição após o shader | Noite e dia irregular completaram com `shader-pack-post`. O material da Ryūjin volta a ser aplicado depois que o Oculus compõe o mundo. O olho continua no caminho de entidade reconhecido pelo shader. No recorte do núcleo no tick 112, a área quase branca caiu de 99,9% no preset padrão para 84,3% com apenas o tonemap mais suave e para 0% com a composição tardia. O halo e a linha artificial no horizonte deixaram de apagar o desenho das faixas no ensaio noturno. Tonemap (arquivo local `qa/codex/v16-oculus-tonemap-20261107/screenshots/frame-t112.png`) · [Noite](qa/codex/v16-oculus-composite-night-20261107/screenshots/frame-t112.png) · [Dia](qa/codex/v16-oculus-composite-day-20261107/screenshots/frame-t112.png). |
| Shimmer 0.3.0 + Oculus + r5.2.2 | Falha fatal `FabricBlockView` no Forge empacotado antes da cena. O log está em `build/visual-lab/run-benihime-qa/v16-shimmer-shader-r522/console.log`. |
| Shimmer solo | Os testes ON/OFF completaram. Na mesma amostra de terreno à direita no tick 112, o canal vermelho médio foi 78,3 com Shimmer e 38,5 com a luz desligada. No tick 220, ambos voltaram a 25,7. ON (arquivo local `qa/codex/v16-shimmer-light-on-20261107/manifest.json`) · OFF (arquivo local `qa/codex/v16-shimmer-light-off-20261107/manifest.json`). |

O r5.2.2 foi baixado do Modrinth, com SHA-512 conferido contra a publicação, e usado apenas no laboratório. O valor SHA-256 é `3b0cd5c36afebdcd74521a447ca5f2b0d6f9c1c8c8416a9f20d9bedc271a8e1c`. A seleção LOW usa as opções explícitas do `profile.LOW` do ZIP. O controle com Shimmer e o controle com Oculus permanecem em instâncias separadas.

O reload gráfico completou no perfil Oculus. A recarga forçada no meio da execução fechou streams dos OGG locais e registrou `ZipFile closed` no Sound Engine, também observado no controle com o renderer anterior. A validação de retomada da música vanilla foi feita em cenas sem recarga forçada.

Os pilotos próprios de bloom e refração operam antes do HUD, com máscara solar e teste de profundidade para preservar o primeiro plano. O Forge base concluiu as provas com cada efeito ligado separadamente. A posição e a inclinação da câmera coincidiram nos três frames do tick 112. Na região esquerda da corona, o bloom aumentou em 5,35 níveis a média do canal vermelho. A refração deslocou em até oito pixels a borda aparente de um painel de teste perto do halo. A região de terreno exterior e a barra do HUD amostradas ficaram idênticas ao controle. A integração com o pós-processamento do Oculus exige uma passagem própria e fica desativada nesse perfil. Controle (arquivo local `qa/codex/v16-post-control-20261107/manifest.json`) · Bloom (arquivo local `qa/codex/v16-bloom-20261107/manifest.json`) · Refração (arquivo local `qa/codex/v16-heat-20261107/manifest.json`).

O JAR de laboratório `46b91f5c54d7760f8e5893189f5fd2bcd754521e5bc27089a0b0178008ed56c9` foi usado nas duas provas da composição tardia e na repetição **Shimmer + bloom + refração + trilha B**. A sonda de áudio passou, os dois pós-efeitos estavam ativos e o perfil confirmou o pico de 278 cartões, 192 partículas próprias e uma luz. A limpeza foi confirmada após o selo e no fim. Este é o perfil que preservou a leitura das faixas e a iluminação do terreno na mesma cena. [Quadro do perfil combinado final](qa/codex/v16-final-shimmer-20261107/screenshots/frame-t112.png).

## Matriz de cena e critérios

`check build runGameTestServer --offline` concluiu com **187 regressões e 296 GameTests obrigatórios**. A matriz empacotada com Shimmer concluiu **15 cenários**, incluindo retorno em primeira pessoa, canhoto, parede, terreno irregular, cancelamentos e morte administrativa. As 11 provas de áudio aplicáveis registraram interrupção da música ativa, bloqueio durante a trilha B, retomada disponível e preservação do slider. Com Música em zero, a trilha B ficou silenciosa e o slider permaneceu no zero. A prova inicial com skin composta teve o fixture corrigido após detectar que havia cores sem as peças do catálogo.

- Noite com faixas contínuas, olho frontal e lâmina (arquivo local `qa/codex/v16-continuous-night-20261107/manifest.json`).
- [Pés dentro do quadro junto à parede](qa/codex/v16-near-wall-20261107/screenshots/frame-t112.png).
- Terreno irregular de dia (arquivo local `qa/codex/v16-irregular-day-20261107/manifest.json`) e limpeza após morte (arquivo local `qa/codex/v16-death-cleanup-20261107/manifest.json`).
- Controle diurno com fumaça desligada (arquivo local `qa/codex/v16-smoke-off-day-20261107/screenshots/frame-t112.png`).

Os GameTests cobrem o servidor de testes, incluindo cancelamento, estado da transformação e limpeza. A prova com um observador conectado a outro cliente e a validação do backend alternativo Photon/AAA continuam separadas da matriz singleplayer.

## Custo na Iris Xe

Três repetições por preset, 1280×720, Shimmer solo, mesmo JAR `7897fc25e0…`, sem screenshots ou vídeo. Janela de sustentação entre os ticks 112 e 175. Os números são intervalos entre quadros.

| Preset | Pico de cartões | Pico de partículas próprias | Mediana do p95 | Mediana do p99 |
|---|---:|---:|---:|---:|
| HIGH | 278 | 192 | 12,76 ms | 14,02 ms |
| LOW | 124 | 192 | 12,94 ms | 14,98 ms |
| Complementary r5.2.2 POTATO, detalhe de fogo HIGH | 278 | 192 | 22,68 ms | 26,16 ms |

LOW reduziu a geometria dos cartões. Nesta arena e nesta GPU, o p95 não melhorou de forma reproduzível. A linha POTATO usou o mesmo JAR, com Oculus e o material intermediário da calibração, sem Shimmer. Os dados individuais ficam em `build/visual-lab/run-benihime-qa/v16-current-perf-high`, `v16-current-perf-low` e `v16-verified-oculus-potato`.

## Repetir no laboratório

```powershell
python -B tools/models/lab/codex/ryujin01/build_solar_finish.py
python -B tools/visual_lab/ryujin_matrix.py v16-minha-prova --jar build/libs/bleachmod-0.2.0-v16-composite.jar --profile shimmer --solar continuous --bloom --heat --music-probe
python -B tools/visual_lab/ryujin_matrix.py v16-meu-complementary --jar build/libs/bleachmod-0.2.0-v16-composite.jar --profile shader --shader-zip build/visual-lab/downloads/ComplementaryReimagined_r5.2.2.zip --solar continuous --solar-tonemap --shader-composite
```

Use um rótulo novo por execução. O launcher abre um mundo descartável, carrega os packs locais B e de arte e encerra após o caso selecionado.
