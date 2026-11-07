# Ryūjin v14e · olho, câmera frontal e cinzas

07/11/2026. Revisão do candidato v14 na branch `Feature-Poderes-bankais`, base `9eb6a057`. O usuário escolheu dar mais espaço ao jogador na abertura e aceitou um corte leve na corona. As cinzas sobem da katana após o selo e continuam depois do nome da Bankai, durante o close.

## Rosto e textura

A face frontal da cabeça vanilla ocupa 8×8 texels a partir de (8, 8) na skin 64×64. Foram inspecionadas as 18 texturas de jogador embarcadas no cliente 1.20.1, nove identidades nos modelos wide e slim. O olho direito ocupa U=9–10 e V=12, com contagem a partir de zero. O centro no espaço da cabeça é X=−2/16, Y=−3,5/16. O cubo base chega a Z=−4/16 e a camada externa vanilla a Z=−4,5/16. O centro antigo tinha Y=−0,245, perto do limite superior do olho. Os modelos padrão dão uma referência confiável para essas skins. Skins personalizadas podem pintar olhos em outra altura, deslocá-los ou cobrir a região. [Como as skins Java são mapeadas no modelo](https://www.minecraft.net/en-us/article/what-is-minecraft-skin).

O candidato cobre a região padrão com uma base emissiva de 2×1 pixels, usa dez chamas e seis trechos de rastro, e estende a cauda lateral de 0,28 para 0,54 bloco. A camada segue a cabeça renderizada. O pico continua antes da fala e a cauda se apaga até o tick 68. Conferir cabelo, capacete e skins personalizadas antes de avaliar cobertura fora da amostra padrão.

## Enquadramento e selamento

No primeiro print, rosto e arma têm presença. No segundo, o terreno ocupa boa parte do quadro e o jogador perde escala. No panorama de sustentação, o candidato aproxima a câmera de 14,2 para 7 blocos, baixa a altura de 9 para 2,1 e usa FOV de 66° em vez de 80°. A referência é um observador à frente, perto da altura dos olhos, com leve olhar para cima para manter o núcleo solar legível. A composição dá prioridade ao jogador, à lâmina e ao núcleo. A franja da corona pode tocar o quadro.

No selo, o ponto de mira sobe em relação à lâmina para acomodar a coluna de cinzas e parte maior do personagem. A primeira passagem manteve o close até cerca do tick 260. Ela mostrou a fumaça acima da katana no tick 265, quando o recuo já havia começado. O OGG da fala do nome tem 2,24 s e entra no tick 220, o que coloca o fim do arquivo perto do tick 265. A versão final usa o próprio close como início da transição de retorno no tick 270.

A emissão parte de posições entre a guarda e a ponta, com fumaça e cinza ascendentes, deriva lateral e redução gradual. Ela começa no tick 202 e termina por volta do 267. As partículas expiram até o fim da cena. O brilho quente do sol e o fogo do efeito anterior encerram no selo.

## Prova e próximos controles

O `check` passou com 184 regressões. O Forge concluiu a cena frontal noturna com a câmera original `THIRD_PERSON_FRONT` restaurada. No quadro 112, o jogador aparece com quase o triplo da altura visual do v14d, o núcleo solar permanece visível e parte da franja superior sai pelo quadro. No quadro 265 a arma continua em close com cinzas e fumaça ascendentes. A amostra registra 19 partículas do efeito, zero chamas e zero luz solar. O quadro 270 ainda mostra a mesma composição na entrada do retorno. Os indicadores de limpeza passaram após o selo e após o encerramento. [Capturas e dados do ensaio](qa/codex/v14e-frontal-20261107/night/manifest.json).

O perfil descartável `v14e-final-night` usa os mesmos recursos visuais do v14d e áudio silenciado. A duração do OGG foi inspecionada separadamente. A captura confirma o alinhamento com o relógio da cena e a presença do rastro depois do tempo nominal da fala.

O teste diurno com parede capturou a preparação no tick 39, a abertura no 112 e o close no 265. A proteção de colisão mantém a câmera à frente do personagem. O núcleo permanece visível e os pés tangenciam a barra inferior nesse cenário. A telemetria confirma a limpeza após o selo e após o encerramento. [Capturas e dados junto à parede](qa/codex/v14e-frontal-20261107/wall/manifest.json).

Para repetir a cena noturna no cliente local, use o perfil de QA já preparado. O parâmetro `-PqaAutoExit=true` fecha o cliente após a execução automática.

```powershell
.\gradlew.bat runClient -PshimmerPilot=true -PbenihimeQa=true -PcinematicQaFast=true -PryujinBankaiQa=true -PryujinBankaiQaCase=1 -PryujinV14Pilot=true -PryujinVisualQaProfile=v14e-final-night -PqaAutoExit=true --offline --console=plain
```

- Na revisão artística, comparar o olho e o rastro nos ticks 32 e 39 com o v14d. Confirmar a legibilidade da lâmina no escuro e a margem inferior da abertura junto a paredes.
- A rodada testa a composição. Medições de desempenho exigem nova passagem sem captura.
- A câmera baixa dá mais destaque às chamas que cruzam o primeiro plano. A próxima revisão visual da cortina deve tratar as bases repetidas e os cones em camadas, mantendo o rosto e a guarda da katana legíveis. Seguem a prova com outras skins, a revisão de mixagem e os pilotos gráficos.
