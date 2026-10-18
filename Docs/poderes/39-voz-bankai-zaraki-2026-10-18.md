# Ciclo 39 — Voz "Bankai" do Zaraki na transformação

Data: 18/10/2026. Branch: `bankai-zaraki`. Base: `04f7964`.

## Pedido

Tocar na cinemática do Berserk Mode, junto com o que já existe, o áudio de ~4 s do personagem falando "Bankai", entregue pelo usuário em MP3 (`sounds/bankai_zaraki.mp3`).

## Formato

O Minecraft só reproduz **OGG Vorbis**, e só arquivos **mono** são posicionais (o volume cai com a distância). Com autorização do usuário, foi instalado o pacote `soundfile` (PyPI) no Python local, usado só para converter; ele não entra no mod.

- Fonte preservada em `tools/audio/source/bankai_zaraki.mp3` (fora de `assets`, que não aceita MP3).
- `tools/audio/convert_sounds.py`: mixa para mono, normaliza o pico para 0,9 (o original tinha pico 0,21) e grava `assets/bleachmod/sounds/bankai_zaraki.ogg` (4,16 s, 44,1 kHz, 37 KB). Para trocar ou adicionar falas, coloque o MP3 em `tools/audio/source/` e rode o script.

## Sincronia

Curva de volume da fala: trecho inicial em 0–0,6 s, silêncio, **"Ban-"** em 1,1–1,3 s, **"-KAAAI"** gritado de 1,7 a 3,0 s (pico em 2,9 s) e cauda até 4,1 s. Com a voz começando no **tick 0** da cinemática:

| Fala | Momento da cinemática |
| --- | --- |
| "Ban-" (~tick 22–26) | Primeiro soco no chão (tick 24) |
| "-KAAAI" (ticks 34–60) | Passa pelos socos 32 e 40 e pela erupção (tick 45) |
| Cauda (até tick 83) | Antes do segundo rugido (tick 68) e do golpe final (tick 124) |

Até o fim da fala (`NozarashiReleaseEffects.VOICE_TICKS = 84`), os sons vanilla que competiam com ela foram abaixados: batimentos 3→1,2; rosnados 2→1; socos (bigorna 1,4→0,8; explosão 0,8→0,4); erupção (explosão 3→1,8; sonic boom 2→1,2; rugido em camadas 3→1,2). O segundo rugido e o golpe final continuam no volume máximo.

## Arquivos

| Arquivo | Mudança |
| --- | --- |
| `init/ModSounds.java` | Registry `SOUND_EVENTS` com `voice.bankai_zaraki` (alcance variável) |
| `BleachCommon.java` | Registra `ModSounds` no barramento do mod |
| `assets/bleachmod/sounds.json` | Declara o evento e a legenda |
| `lang/pt_br.json`, `en_us.json` | Legenda "Zaraki grita: Bankai!" / "Zaraki shouts: Bankai!" |
| `NozarashiReleaseEffects.java` | Toca a voz no início da cinemática (volume 2,5, ouvida a ~40 blocos) e abaixa os sons concorrentes |

## Validação

- `compileJava`; `regressionTest` com teste novo: `sounds.json` declara o evento, o arquivo existe, é Ogg Vorbis **mono** e dura entre 3,5 s e `VOICE_TICKS`.
- `runGameTestServer`: 64/64 em 3 execuções seguidas. Na primeira execução após compilar, `validSwordPracticeHasLimitsAndDoesNotAccumulateWhileIdle` (maestria, sem relação com áudio) falhou uma vez de forma intermitente.
- **Não verificado no cliente:** audição real, volume relativo à cinemática e a sincronia percebida.

## Observação de licença

O contrato pede avaliação de licença para assets externos. Este áudio foi fornecido pelo usuário; se for um trecho do anime, sua redistribuição no modpack público depende de autorização dos detentores dos direitos. O código não depende do conteúdo: basta trocar o OGG.
