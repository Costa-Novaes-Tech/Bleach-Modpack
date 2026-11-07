# Ryūjin · áudio de 15 s para teste local

07/11/2026. Integração na branch `Feature-Poderes-bankais`, base `9eb6a057`. O pacote novo usa o relógio da cena aprovado no v14e. [Briefing enviado ao Manus](prompt-manus-ryujin-audio-v14e-2026-11-07.md).

## Auditoria da entrega

- O manifesto avulso tem o mesmo conteúdo do manifesto do ZIP. Os 38 arquivos de áudio conferem em tamanho e SHA-256. Todos foram decodificados, com duração dentro da tolerância de 15 ms e amostras abaixo do limite de saturação. Os WAVs são masters de 48 kHz. O pacote fornece efeitos e vozes mono, trilhas e prévias estéreo.
- Os 14 stems avulsos conferidos coincidem com seus pares no ZIP. O WAV avulso `preview_voice_fx_only_15s.wav` diverge no hash e nas amostras, embora tenha 15 s e o mesmo tamanho. A integração usa exclusivamente os stems do ZIP conferidos pelo manifesto. As prévias compostas ficam como material de escuta.
- A comparação do fornecedor indica que A e B variam a música. Vozes, efeitos e marcos são compartilhados. As notas de referência contêm links e descrição de influência, com a minutagem ainda em aberto. A nota de origem atribui vozes ao usuário e efeitos/música às ferramentas do Manus. Ela declara que os stems não contêm áudio de cena do anime. O escopo desta instalação é uso pessoal local.
- O loop real de rugido dura 2,67 s. A medida de diferença entre o primeiro e o último valor do OGG é aproximadamente 0,002 em amplitude normalizada. A naturalidade da emenda ainda pede escuta. O novo selo é a revisão do pacote conferido, com 0,88 s. A cauda `seal_ash` tem 4,88 s e cabe entre o selo e o fim da cena.

## Mixagem e instalação

O gerador `tools/models/lab/codex/ryujin01/install_audio_15s.py` lê os arquivos do ZIP após verificar seus hashes. Ele produz dois resource packs locais em `run/resourcepacks`, com origem por arquivo em `provenance.json`. Os packs ficam disponíveis no menu de recursos. A ordem é de menor para maior prioridade.

1. Coloque **RyujinAudio15Local.zip** acima dos packs antigos da Ryūjin. Ele traz as duas vozes `clean`, a música A e os oito efeitos.
2. Para a trilha mais pesada, coloque **RyujinAudio15HeavyMusicLocal.zip** acima de A. Esse pack sobrepõe apenas a música. Para ouvir A novamente, desative a sobreposição.

O gerador preserva os OGGs fornecidos. A seleção ocorre pelo marcador do pack de áudio. Os recursos ficam fora do JAR e os eventos existentes servem de base para a sobreposição de voz, música e cinco efeitos. A ausência do pack mantém disponíveis os recursos anteriores.

| Tick | Resultado integrado |
|---|---|
| 0 e 40 | Pré-aquecimento e fala “Bankai”. O acento de chama sob a palavra tem ganho discreto. |
| 70 | Explosão do pacote com ganho reduzido durante a gravação de voz. O ganho volta suavemente em cinco ticks, após a janela do take real. |
| 72 | Propagação, estalos e rugido entram como três camadas independentes. O rugido repete. |
| 176–202 | A propagação e os estalos já estão terminando ao começar a absorção. O rugido perde força e encerra no selo. |
| 202 | Fecho do pacote e cauda baixa de cinzas. |
| 220–300 | “Zanka no Tachi” com música e cinzas reduzidas durante a fala. A cauda perde força no retorno e os sons da cena são limpos por dono. |

As versões limpas duram 1,813 s e 1,877 s. A janela da voz começa no tick real em que o cliente a dispara, com 37 e 38 ticks de sustentação, respectivamente. A sobreposição de “Bankai” com a explosão é intencional, com ganho menor até a fala terminar. O arranjo final substitui a cama antiga de fogo e o empilhamento de impactos vanilla quando os stems novos correspondentes estão presentes.

## Resultado no Forge

O `check` passou com **185 regressões**. O Forge concluiu duas cenas com A e uma com B. Os logs mostram o recurso musical vindo de A na primeira execução e da sobreposição B na segunda. Cada passagem disparou duas falas, oito efeitos do pacote, a música e o acento vanilla de chama. Os indicadores visuais de limpeza passaram após o selo e no fim.

O dispositivo OpenAL **Wave File Writer** gravou a mixagem real do jogo em estéreo. Os recortes de 15 s tiveram pico de amostra máximo de **−7,55 dBFS em A** e **−7,74 dBFS em B**, com zero amostras saturadas. A segunda tomada A tem 321 quadros capturados, zero descartes de fila e seis repetições. A junção do áudio com o vídeo usa alinhamento aproximado, orientado pelo relógio da captura e pela correlação das falas. As medições caracterizam o sinal. A avaliação de clareza, peso e naturalidade continua em escuta humana. [Metadados e trechos do log](qa/codex/v15-audio-15s-20261107/engine-mix-evidence.json).

As prévias pessoais estão em `build/visual-lab/v15-audio-a-previa.mp4` e `build/visual-lab/v15-audio-b-cena.wav`. Os arquivos brutos do motor e as prévias ficam no diretório ignorado de laboratório.

## Como repetir

Para reconstruir os packs a partir do ZIP original:

```powershell
python tools/models/lab/codex/ryujin01/install_audio_15s.py 'C:/Users/joaop/Downloads/AudioRyujin_prompt_15s_package (1).zip' run/resourcepacks
```

Os perfis descartáveis são `v15-audio-a` e `v15-audio-b`. Para escutar a cena com saída de som normal, rode o seguinte comando fora do ambiente de captura Wave. Troque o perfil para B para comparar a música.

```powershell
.\gradlew.bat runClient -PshimmerPilot=true -PbenihimeQa=true -PcinematicQaFast=true -PryujinBankaiQa=true -PryujinBankaiQaCase=1 -PryujinV14Pilot=true -PryujinVisualQaProfile=v15-audio-a --offline --console=plain
```

A prova técnica usa resolução do pack pelo cliente, eventos de áudio e captura do motor OpenAL. A revisão humana deve julgar a clareza de “Bankai” às 3,50 s, a emenda do rugido, a leitura dos estalos e o contraste entre o fecho e a última fala.
