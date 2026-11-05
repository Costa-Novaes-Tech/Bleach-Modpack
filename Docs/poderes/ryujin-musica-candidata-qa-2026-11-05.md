# Ryūjin — música candidata de 12 s para escuta local

**05/11/2026.** Branch `Feature-Poderes-bankais`, HEAD inicial `31f5e1c`. O usuário informou que compôs a música e pode licenciá-la ao mod. Essa declaração registra autoria e possibilidade de licença; ainda não escolhe os termos de redistribuição nem constitui concessão GPL-3.0. Nenhum áudio desta rodada foi incorporado ao JAR, enviado a terceiros, commitado ou mesclado.

## Preparação

- Entrada escolhida: `ryujin_jakka_bankai_music_12s_final.wav`, SHA-256 `e647a0f503aac2c1959a872086cb8ec99c35975ebef4fa8d6a49efff981bcb90`. PCM 24-bit, 48 kHz, estéreo, 12,000 s. O MP3 fornecido tem a mesma duração declarada, mas um deslocamento de decodificação de aproximadamente 23 ms; por isso a conversão parte do WAV.
- `tools/models/lab/codex/ryujin01/install_candidate_music.py` produz Vorbis 48 kHz estéreo de 12,000 s, SHA-256 `a2d7a4721e050a802b7ab6c51b76198772b4f832922a7f85f984d7fb935b8f0e`, e o instala em `run/resourcepacks/RyujinCandidateMusicLocal` e `run-benihime-qa/ryujin-v7/resourcepacks/RyujinCandidateMusicLocal`. Ambos os diretórios são ignorados pelo Git. O pack substitui somente `sounds/ryujin_entrance_score.ogg`; o evento e o `sounds.json` existentes continuam responsáveis pela reprodução.
- O script habilita o pack apenas em `run-benihime-qa/ryujin-v7/options.txt`, após `RyujinV7LocalAudio`. O `run/options.txt` normal permaneceu com `resourcePacks:[]`. Cada pack contém `provenance.json` com os hashes e o estado de uso local.
- Medição isolada do OGG: pico −4,9 dBFS, RMS −19,6 dBFS. A partitura original do JAR mede RMS −19,0 dBFS; a candidata tem menos energia entre 3,5–5,8 s e mais entre 5,8–12 s. Essas medidas não demonstram timbre, qualidade artística ou volume percebido no jogo. A mixagem atual usa a categoria *Music*, ganho 0,55 com ducking sob as falas e fade ao final.

## Teste no cliente

Comando executado: `./gradlew.bat runClient -PbenihimeQa=true -PcinematicQaFast=true -PryujinBankaiQa=true -PryujinBankaiQaCase=0 -PryujinV7Qa=true -PqaAutoExit=true --offline --console=plain`. Log: `run-benihime-qa/ryujin-v7/client-music-candidate.log` (ignorado pelo Git). Resultado: exit 0, `BUILD SUCCESSFUL in 59s`. O cliente registrou `Reloading ResourceManager: vanilla, mod_resources, file/RyujinV7LocalAudio, file/RyujinCandidateMusicLocal`; o motor OpenAL iniciou, a cena `day_back` foi preparada e concluída em `THIRD_PERSON_BACK`. Não apareceu falha de carregamento de som do Bleach Mod. O log comprova o carregamento dos packs e a conclusão da cena, mas não mede o som que saiu dos alto-falantes nem comprova, por si só, a prioridade do arquivo OGG entre packs.

## Próxima revisão

Para escuta no perfil normal, abrir **Opções → Pacotes de recursos** e ativar `RyujinV7LocalAudio` e `RyujinCandidateMusicLocal`, deixando a música candidata acima do pack de efeitos na prioridade. Testar a cena frontal com áudio do jogo em volume Music e Players conhecidos; comparar a voz “Bankai”, o impacto, a formação do sol, a absorção e a fala final. Uma gravação contínua com áudio permite avaliar sincronismo e mixagem; este host ainda não recebe áudio como entrada de escuta. Após o aceite, decidir se a composição será distribuída no mod e registrar a licença/atribuição expressa antes de substituir o OGG do JAR. Os efeitos Manus continuam com direitos separados.
