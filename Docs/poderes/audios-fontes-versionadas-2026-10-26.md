# Fontes de áudio versionadas — migração para main

26/10/2026. Pedido explícito do usuário: mover somente áudios em uso no Bleach para o projeto e comitar na main. Exceção pontual à regra anterior de commits exclusivos na branch de poderes. Sem push automático.

## Destino e rastreabilidade

Pasta canônica: `tools/audio/source/`. Manifesto: [manifest.json](../../tools/audio/source/manifest.json), com caminhos relativos, SHA-256, tamanho, formato WAV, nome anterior e assets derivados conhecidos. Foram selecionados 11 WAVs atuais (68.665.666 bytes) da auditoria; vídeos, MP3s correspondentes, dois WAVs históricos e arquivos sem vínculo não entram nessa migração. A cópia Daiguren_Bankai_voice_clean.wav que já existia no repositório é mantida sem duplicação e com hash idêntico.

As fontes são para autoria/reedição. O jogo continua lendo seus OGGs em src/main/resources/assets/bleachmod/sounds. Não foram reexecutados importadores que sobrescrevem os OGGs; código Java, sounds.json, assets de runtime, custos, tempos, volumes e mixagens permanecem idênticos ao ponto inicial da main.

Esta main não contém necessariamente todos os refinamentos das branches de poderes/design. Guardar fontes de Senbonzakura/Ryujin aqui não integra automaticamente essas features nem substitui seus sons. O manifesto distingue assets conhecidos de assets presentes nesta main. Não usar a migração como pretexto para um merge de gameplay.

## Arquivos e novos endereços

### benihime

- `tools/audio/source/bankai_final_ordered_30s.wav` → `benihime_invocation_mix.ogg`
- `tools/audio/source/Bankai_voice_clean.wav` → `benihime_voice_bankai.ogg`
- `tools/audio/source/Kannonbiraki_Benihime_Aratame_voice_clean.wav` → `benihime_voice_name.ogg`, `benihime_invocation_mix.ogg`

### hyorinmaru

- `tools/audio/source/Daiguren_Bankai_voice_clean.wav` → `hyorinmaru_voice_bankai.ogg`
- `tools/audio/source/daiguren_hyorinmaru_ice_theme_adapted_20s.wav` → `hyorinmaru_theme.ogg`
- `tools/audio/source/Daiguren_Hyorinmaru_voice_clean.wav` → `hyorinmaru_voice_name.ogg`

### senbonzakura

- `tools/audio/source/Senbonzakura_Bankai_complete_voice.wav` → `senbonzakura_voice_bankai.ogg`
- `tools/audio/source/Senbonzakura_Kageyoshi_best_voice.wav` → `senbonzakura_voice_name.ogg`
- `tools/audio/source/Senbonzakura_music_only_extended30s.wav` → `senbonzakura_theme.ogg`

### ryujin

- `tools/audio/source/Zanka_no_Tachi_Bankai_fixed.wav` → `ryujin_voice_bankai.ogg`
- `tools/audio/source/Zanka_no_Tachi_only_main_voice.wav` → `ryujin_voice_name.ogg`

## Importação e verificação

Os importadores Benihime e Hyorinmaru agora usam tools/audio/source como --source-root padrão. O argumento continua disponível para uma pasta alternativa; conversões e filtros foram preservados. Scripts de outras branches devem apontar para essa mesma pasta quando forem integrados, sem recuperar caminhos absolutos antigos de Downloads.

```powershell
python tools/audio/verify_sources.py
python tools/audio/import_benihime_user_audio.py --ffmpeg "CAMINHO/ffmpeg.exe"
python tools/audio/import_hyorinmaru_user_audio.py --ffmpeg "CAMINHO/ffmpeg.exe"
```

Os dois comandos de importação reprocessam OGGs e só devem ser usados quando se deseja essa alteração; não são necessários para jogar ou compilar. O verificador é somente leitura e dispensa FFmpeg. Não executar convert_sounds.py indiscriminadamente para esses WAVs: sua rotina MP3/Zaraki foi preservada.

Os caminhos absolutos em relatórios/manifestos históricos são registros da origem à época, não dependências de runtime. Não foram reescritos ou falsificados. A resolução atual está neste documento e no manifesto. A origem/atribuição dos áudios fornecidos continua sujeita aos registros existentes; mover/commitar não concede uma licença nova.

## Execução segura e validação

A main foi aberta em um checkout isolado para preservar alterações concorrentes da Feature-Poderes-bankais e atualizada somente por fast-forward de origin/main (base 26b73b3). As cópias foram conferidas por hash antes de remover os originais de Downloads. A transferência final é feita somente após os testes e o commit: cada destino deve ter hash igual ao original e constar no commit. Nenhum outro arquivo da pasta externa deve ser movido.

Validação do ciclo: verificação dos 11 SHA-256/WAVs; casos negativos do verificador; --help dos importadores; check/build; comparação das árvores src/main/java e src/main/resources com a base para comprovar ausência de mudança no runtime. Resultados: 11 WAVs/SHA-256 aprovados; nove verificações de caminhos, casos negativos e árvores de runtime aprovadas; check/build concluído com sucesso com 146 cenários de regressão. Todos os 12 OGGs do JAR conferem com os recursos de runtime; fontes WAV de autoria não são empacotadas. Log local: build/audio-migration-2026-10-26/build.log. Sem escuta/cliente novo, pois os OGGs e a lógica permanecem inalterados.
