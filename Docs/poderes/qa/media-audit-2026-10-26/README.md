> Atualização posterior, autorizada pelo usuário: 11 WAVs atuais foram movidos para tools/audio/source na main, commit 84738ae. Checkout: C:\Users\joaop\.codex\worktrees\audio-source-main\Bleach-Modpack. Os 28 arquivos restantes estão intactos. Veja relocation-to-main.json para os novos endereços e hashes. O texto abaixo preserva a auditoria anterior à transferência.

# Auditoria local — áudios e vídeos para revisão

Data: 26/10/2026. Pasta: `C:\Users\joaop\Downloads\Áudios e vídeos para revisão`. Auditoria sem exclusão, movimento ou alteração dos arquivos originais. Relatório local não commitado/publicado.

## Contexto recuperado

Branch principal inspecionada: Feature-Poderes-bankais, HEAD 23b80e0; checkout de design codex/design-poderes-criaturas, HEAD 08105c2. Senbonzakura integrada e C vertical Ryūjin validados no 111; revisão transversal de armas/mãos/efeitos pendente no 112. Durante a auditoria surgiram alterações documentais concorrentes dos ciclos113–115 e QA113: referências rechecadas, sem editar esses arquivos. O vídeo de 26/10 agora tem nome/hash confirmado no novo manifesto113. Runtime 2.35/schema9. Novas evidências não equivalem a aceite artístico.

## Resultado

39 arquivos, 1203.50 MiB (1.175 GiB): 27 áudios e 12 vídeos.
- Fonte WAV atual: 11 arquivos / 65.48 MiB.
- Fonte histórica: 2 arquivos / 6.16 MiB.
- Vídeo de referência / QA: 8 arquivos / 979.87 MiB.
- Versão MP3 correspondente: 13 arquivos / 3.04 MiB.
- Sem vínculo localizado: 5 arquivos / 148.94 MiB.

**Prioridade de preservação:** 11 WAVs atuais. Só Daiguren_Bankai_voice_clean.wav tem cópia binária idêntica em tools/audio/source nos dois checkouts examinados; 10 fontes atuais não têm outra cópia idêntica versionada localizada nesse escopo. O jogo toca OGGs empacotados, mas apagar os WAVs elimina fontes de reedição/reprodução dos imports.

Nenhum arquivo recebe aprovação automática para exclusão. Os MP3s correspondem aos WAVs com correlação PCM acima de 0,9998; isso não os torna duplicatas SHA-256 nem torna o WAV removível. Fontes históricas e vídeos mantêm valor de referência.

## Método, evidências e limites

- 2565 arquivos de texto inspecionados nos dois checkouts, incluindo registros de QA e scripts. Busca literal por nome completo com limites de palavra, stem e SHA-256; sem resultados inflados por Bankai_voice_clean dentro de Daiguren_Bankai_voice_clean.
- Atualização incremental de 7 caminhos alterados/criados por outra conversa durante a auditoria, preservados sem edição; isso não é uma nova varredura integral de alterações futuras.
- SHA-256 de todos os 39 originais calculado e conferido novamente no encerramento: nenhum mudou. Tamanho/mtime também foram preservados.
- Metadados identificados nos 39; todos os 27 áudios decodificados sem erro por FFmpeg local. Cada um dos 12 vídeos teve três quadros amostrados; não se afirma reprodução integral nem escuta subjetiva.
- Sons derivados conferidos no sounds.json e nos controladores de áudio Benihime/Hyorinmaru/Senbonzakura/Ryujin. Quando havia manifesto, comparados hash de origem e hash de saída atual. Importações históricas podem divergir do OGG atual após refinamento; isso foi registrado, não ocultado.
- Referência Ryūjin atual sustentada pelos relatórios 104/106, registro de eventos e comparação PCM; não há manifesto de importação correspondente localizado. Correlação ajuda a corroborar, não substitui prova de proveniência criptográfica.
- Os 13 pares MP3/WAV foram comparados em PCM mono 8 kHz com correlação cruzada; não é teste de identidade binária nem avaliação perceptiva de qualidade.
- Não foram examinados todos os blobs históricos do Git, cópias em outras pastas de Downloads, backups externos ou nuvem. “Sem vínculo” vale somente para esse escopo e para os nomes/hashes atuais.
- Os dois checkouts pertencem ao mesmo repositório: cópias neles não são dois backups independentes. A checagem de duplicatas considera bytes/SHA-256, não apenas nomes.
- A pasta atual foi reorganizada: referências antigas apontam a Downloads diretamente. Importadores aceitam --source-root; usar a pasta atual quando reprocessar, sem editar os originais. Não foram executados importadores/conversores que sobrescrevem sons.
- Sem novo build/GameTest: tarefa de rastreabilidade de arquivos, sem mudanças de gameplay ou dependências.

## Fontes atuais e recursos derivados

### bankai_final_ordered_30s.wav
Fonte associada a benihime_invocation_mix.ogg.

- benihime_invocation_mix.ogg; eventos: benihime.invocation_mix; registros de importação com hash de origem: 4; saída atual idêntica ao registro: 2.

### Bankai_voice_clean.wav
Fonte associada a benihime_voice_bankai.ogg.

- benihime_voice_bankai.ogg; eventos: voice.benihime_bankai; registros de importação com hash de origem: 2; saída atual idêntica ao registro: 2.

### Daiguren_Bankai_voice_clean.wav
Fonte associada a hyorinmaru_voice_bankai.ogg.

- hyorinmaru_voice_bankai.ogg; eventos: voice.hyorinmaru_bankai; registros de importação com hash de origem: 4; saída atual idêntica ao registro: 2.
- Cópia(s) idêntica(s): C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\tools\audio\source\Daiguren_Bankai_voice_clean.wav; C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-design\tools\audio\source\Daiguren_Bankai_voice_clean.wav

### daiguren_hyorinmaru_ice_theme_adapted_20s.wav
Fonte associada a hyorinmaru_theme.ogg.

- hyorinmaru_theme.ogg; eventos: hyorinmaru.theme; registros de importação com hash de origem: 2; saída atual idêntica ao registro: 2.

### Daiguren_Hyorinmaru_voice_clean.wav
Fonte associada a hyorinmaru_voice_name.ogg.

- hyorinmaru_voice_name.ogg; eventos: voice.hyorinmaru_name; registros de importação com hash de origem: 2; saída atual idêntica ao registro: 2.

### Kannonbiraki_Benihime_Aratame_voice_clean.wav
Fonte associada a benihime_voice_name.ogg, benihime_invocation_mix.ogg.

- benihime_voice_name.ogg; eventos: voice.benihime_name; registros de importação com hash de origem: 4; saída atual idêntica ao registro: 2.
- benihime_invocation_mix.ogg; eventos: benihime.invocation_mix; registros de importação com hash de origem: 0; saída atual idêntica ao registro: 0.

### Senbonzakura_Bankai_complete_voice.wav
Fonte associada a senbonzakura_voice_bankai.ogg.

- senbonzakura_voice_bankai.ogg; eventos: voice.senbonzakura_bankai; registros de importação com hash de origem: 1; saída atual idêntica ao registro: 1.

### Senbonzakura_Kageyoshi_best_voice.wav
Fonte associada a senbonzakura_voice_name.ogg.

- senbonzakura_voice_name.ogg; eventos: voice.senbonzakura_name; registros de importação com hash de origem: 1; saída atual idêntica ao registro: 1.

### Senbonzakura_music_only_extended30s.wav
Fonte associada a senbonzakura_theme.ogg.

- senbonzakura_theme.ogg; eventos: senbonzakura.theme; registros de importação com hash de origem: 1; saída atual idêntica ao registro: 1.

### Zanka_no_Tachi_Bankai_fixed.wav
Fonte associada a ryujin_voice_bankai.ogg.

- ryujin_voice_bankai.ogg; eventos: voice.ryujin_bankai; registros de importação com hash de origem: 0; saída atual idêntica ao registro: 0.

### Zanka_no_Tachi_only_main_voice.wav
Fonte associada a ryujin_voice_name.ogg.

- ryujin_voice_name.ogg; eventos: voice.ryujin_name; registros de importação com hash de origem: 0; saída atual idêntica ao registro: 0.

## Inventário individual completo

### Fonte WAV atual

- **bankai_final_ordered_30s.wav** — 21.97 MiB; 30.0 s. Fonte associada a benihime_invocation_mix.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\benihime\cycle71\validation\audio-analysis.json:317`.
- **Bankai_voice_clean.wav** — 0.33 MiB; 0.9 s. Fonte associada a benihime_voice_bankai.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\benihime\cycle71\validation\audio-analysis.json:627`.
- **Daiguren_Bankai_voice_clean.wav** — 0.39 MiB; 1.05 s. Fonte associada a hyorinmaru_voice_bankai.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\99-hyorinmaru-fala-camera-coordenacao-2026-10-23.md:8`.
- **daiguren_hyorinmaru_ice_theme_adapted_20s.wav** — 14.65 MiB; 20.0 s. Fonte associada a hyorinmaru_theme.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\cycle92\audio-import.json:20`.
- **Daiguren_Hyorinmaru_voice_clean.wav** — 0.92 MiB; 2.5 s. Fonte associada a hyorinmaru_voice_name.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\cycle92\audio-import.json:12`.
- **Kannonbiraki_Benihime_Aratame_voice_clean.wav** — 1.51 MiB; 4.12 s. Fonte associada a benihime_voice_name.ogg, benihime_invocation_mix.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\benihime\cycle71\validation\audio-analysis.json:646`.
- **Senbonzakura_Bankai_complete_voice.wav** — 1.21 MiB; 3.3 s. Fonte associada a senbonzakura_voice_bankai.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-design\build\cycle101-audio\import.json:4`.
- **Senbonzakura_Kageyoshi_best_voice.wav** — 1.23 MiB; 3.35 s. Fonte associada a senbonzakura_voice_name.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-design\build\cycle101-audio\import.json:12`.
- **Senbonzakura_music_only_extended30s.wav** — 21.97 MiB; 30.0 s. Fonte associada a senbonzakura_theme.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-design\build\cycle101-audio\import.json:20`.
- **Zanka_no_Tachi_Bankai_fixed.wav** — 0.49 MiB; 1.35 s. Fonte associada a ryujin_voice_bankai.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\103-ryujin-bankai-sol-selamento-plano-prompt-2026-10-25.md:13`.
- **Zanka_no_Tachi_only_main_voice.wav** — 0.82 MiB; 2.24 s. Fonte associada a ryujin_voice_name.ogg. Preservar o original; OGG convertido não substitui fonte.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\106-ryujin-bankai-refinamento-sol-audio-camera-2026-10-25.md:13`.

### Fonte histórica

- **bankai_comparison_original_30s_final.wav** — 5.05 MiB; 30.0 s. Faixa de comparação Benihime documentada e identificada por hash; não é a entrada do importador atual. Arquivar; não confundir com a fonte atual.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\benihime\cycle71\validation\audio-analysis.json:7`.
- **Zanka_no_Tachi_name_fixed.wav** — 1.12 MiB; 3.05 s. Fonte antiga do nome Ryūjin, substituída por only_main_voice no ciclo 106; conservar histórico. Arquivar; não confundir com a fonte atual.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\103-ryujin-bankai-sol-selamento-plano-prompt-2026-10-25.md:14`.

### Vídeo de referência / QA

- **Bing Vídeos.mp4** — 14.39 MiB; 90.01 s. Referência anime/Bleach para Benihime; citada no ciclo 64. Preservar como referência/evidência, mesmo sem uso pelo jogo.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\64-revisao-planejamento-animacoes-benihime-ryujin-2026-10-19.md:54`.
- **Gravando 2026-09-20 193722.mp4** — 36.78 MiB; 37.43 s. Gameplay do Bleach Mod, espada/chamas/HUD; sem referência textual encontrada. Preservar como referência/evidência, mesmo sem uso pelo jogo.
- **Gravando 2026-10-20 201016.mp4** — 462.27 MiB; 461.13 s. QA da entrada Benihime; nome e hash encontrados nos registros do projeto. Preservar como referência/evidência, mesmo sem uso pelo jogo.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\benihime\cycle73\origem.json:3`.
- **Gravando 2026-10-23 161602.mp4** — 35.30 MiB; 32.5 s. Referência de anime/Bleach para liberação de Bankai/Senbonzakura; sem nome/hash localizado no projeto. Preservar como referência/evidência, mesmo sem uso pelo jogo.
- **Gravando 2026-10-25 150636.mp4** — 19.16 MiB; 17.47 s. Referência de Zanka no Tachi, citada no planejamento Ryūjin do ciclo 103. Preservar como referência/evidência, mesmo sem uso pelo jogo.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\103-ryujin-bankai-sol-selamento-plano-prompt-2026-10-25.md:7`.
- **Gravando 2026-10-25 164851.mp4** — 82.49 MiB; 82.0 s. QA/gameplay Senbonzakura, citado no relatório 101 atualizado. Preservar como referência/evidência, mesmo sem uso pelo jogo.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\101-senbonzakura-kageyoshi-cinematica-e-arte-conceitual-2026-10-23.md:413`.
- **Gravando 2026-10-26 131228.mp4** — 264.43 MiB; 263.3 s. Gameplay recente de combate do Bleach Mod; nome/hash confirmados no ciclo113/manifest criado durante esta auditoria. Preservar para revisão atual de rig/arma. Preservar como referência/evidência, mesmo sem uso pelo jogo.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\cycle113\manifest.json:7`.
- **WhatsApp Video 2026-10-16 at 09.12.56.mp4** — 65.05 MiB; 48.97 s. QA/gameplay Bleach com Senbonzakura; citado no ciclo 51. Preservar como referência/evidência, mesmo sem uso pelo jogo.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\51-roadmap-better-combat-ferramentas-refatoracao-2026-10-16.md:25`.

### Versão MP3 correspondente

- **bankai_comparison_original_30s.mp3** — 0.69 MiB; 30.04 s. Correspondente de bankai_comparison_original_30s_final.wav; correlação PCM de áudio 0.999981. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\benihime\cycle71\validation\audio-validation.json:4`.
- **bankai_final_ordered_30s.mp3** — 0.69 MiB; 30.02 s. Correspondente de bankai_final_ordered_30s.wav; correlação PCM de áudio 0.999983. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\benihime\cycle71\validation\audio-validation.json:18`.
- **Bankai_voice_clean.mp3** — 0.02 MiB; 0.94 s. Correspondente de Bankai_voice_clean.wav; correlação PCM de áudio 0.999998. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\benihime\cycle71\validation\audio-validation.json:32`.
- **Daiguren_Bankai_voice_clean.mp3** — 0.03 MiB; 1.08 s. Correspondente de Daiguren_Bankai_voice_clean.wav; correlação PCM de áudio 1.000000. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
- **daiguren_hyorinmaru_ice_theme_adapted_20s.mp3** — 0.46 MiB; 20.04 s. Correspondente de daiguren_hyorinmaru_ice_theme_adapted_20s.wav; correlação PCM de áudio 0.999841. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
- **Daiguren_Hyorinmaru_voice_clean.mp3** — 0.06 MiB; 2.54 s. Correspondente de Daiguren_Hyorinmaru_voice_clean.wav; correlação PCM de áudio 0.999999. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
- **Kannonbiraki_Benihime_Aratame_voice_clean.mp3** — 0.10 MiB; 4.15 s. Correspondente de Kannonbiraki_Benihime_Aratame_voice_clean.wav; correlação PCM de áudio 1.000000. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
  Evidência recuperável: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack\Docs\poderes\qa\benihime\cycle71\validation\audio-validation.json:46`.
- **Senbonzakura_Bankai_complete_voice.mp3** — 0.08 MiB; 3.34 s. Correspondente de Senbonzakura_Bankai_complete_voice.wav; correlação PCM de áudio 1.000000. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
- **Senbonzakura_Kageyoshi_best_voice.mp3** — 0.08 MiB; 3.38 s. Correspondente de Senbonzakura_Kageyoshi_best_voice.wav; correlação PCM de áudio 0.999999. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
- **Senbonzakura_music_only_extended30s.mp3** — 0.69 MiB; 30.02 s. Correspondente de Senbonzakura_music_only_extended30s.wav; correlação PCM de áudio 0.999829. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
- **Zanka_no_Tachi_Bankai_fixed.mp3** — 0.03 MiB; 1.39 s. Correspondente de Zanka_no_Tachi_Bankai_fixed.wav; correlação PCM de áudio 0.999998. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
- **Zanka_no_Tachi_name_fixed.mp3** — 0.07 MiB; 3.1 s. Correspondente de Zanka_no_Tachi_name_fixed.wav; correlação PCM de áudio 0.999997. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.
- **Zanka_no_Tachi_only_main_voice.mp3** — 0.05 MiB; 2.28 s. Correspondente de Zanka_no_Tachi_only_main_voice.wav; correlação PCM de áudio 0.999999. Mesma gravação provável, não duplicata binária. Secundário ao WAV; eventual limpeza somente após backup e confirmação.

### Sem vínculo localizado

- **20261020_141842.mp4** — 55.13 MiB; 29.9 s. Gravação vertical de apresentação pessoal; não mostra o mod nas três amostras. Separar para confirmação; não excluir nesta auditoria.
- **20261020_142226.mp4** — 9.68 MiB; 28.81 s. Gravação vertical de apresentação pessoal; não mostra o mod nas três amostras. Separar para confirmação; não excluir nesta auditoria.
- **Gravando 2026-10-23 182203.mp4** — 73.32 MiB; 70.53 s. Desktop, configurações de gravação e navegador/conversa; não mostra o mod nas amostras. Separar para confirmação; não excluir nesta auditoria.
- **WhatsApp Audio 2026-10-09 at 17.33.11.ogg** — 0.50 MiB; 228.81 s. Áudio de 228,81 s decodificado; sem referência por nome/hash/cópia. Conteúdo não transcrito nem escutado; finalidade precisa de confirmação. Separar para confirmação; não excluir nesta auditoria.
- **WhatsApp Video 2026-10-16 at 09.24.13.mp4** — 10.31 MiB; 71.24 s. Navegador com páginas de painel/finanças; não mostra o mod nas amostras. Separar para confirmação; não excluir nesta auditoria.

## Próxima ação sugerida

1. Preservar os 11 WAVs atuais e providenciar backup dos dez sem cópia versionada idêntica antes de qualquer limpeza.
2. Arquivar os oito vídeos Bleach e as duas fontes históricas, distinguindo evidência do projeto de recurso empacotado.
3. Os 13 MP3s são candidatos de menor prioridade para retenção apenas se o usuário confirmar backup/WAVs e não precisar dessas versões para outros usos; o espaço economizado é pequeno.
4. Pedir confirmação de finalidade para os quatro vídeos sem conteúdo Bleach nas amostras e o WhatsApp Audio de 228,81 s. Não excluir por ausência de referência.
5. Se autorizada uma limpeza futura, executar um plano separado, com caminhos absolutos conferidos e sem tocar nas fontes preservadas.

## Arquivos desta auditoria

- inventario.csv: uma linha para cada original e recomendação.
- inventory-evidence.json: hashes, caminhos/linhas, correspondências, metadados e comparações de áudio.
- inventory-summary.json: resumo técnico final com referências corrigidas e classificação.
- Prancha visual local em build/media-audit-2026-10-26/video-contact-sheet.jpg, mantida fora da pasta versionável por conter também cenas pessoais/alheias ao mod.
