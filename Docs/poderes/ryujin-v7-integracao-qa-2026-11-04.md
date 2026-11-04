# Ryūjin Bankai v7 — implementação e prova inicial no Forge

**04/11/2026.** Branch verificada: `Feature-Poderes-bankais`, base `5b86984`. Trabalho local sem commit/merge. Os `.bbmodel` originais da sessão de modelagem já apareciam modificados no `git status` no início; o Codex não os editou. O checkout `main` e os worktrees Quincy/design permaneceram separados. Este relatório acompanha o [plano](ryujin-integracao-v7-explosao-luz-plano-2026-11-04.md), atualizado pela orientação do usuário: pequeno impacto sonoro e campo de fogo **durante** “Bankai”, explosão principal **depois** da palavra.

## O que foi integrado

- `export_bankai_vfx.py` deriva da cena Blockbench v7 um sol editável sem manequim nem katana falsa: **33 bones, 197 cubos**, clipe de 12 s, atlas e glowmask. O gerador não altera o `.bbmodel` fonte; `validate_scene_v7.py` passou com 0 problemas. O renderer GeckoLib 4.8.4 usa um animatable por conjurador, escala do `sun_root` no relógio real e `AutoGlowingGeoLayer`. A meia volta de yaw corrigiu a primeira captura, em que o sol aparecia entre câmera e jogador.
- O relógio mantém **240 ticks reais**, release 60/commit 40. Voz “Bankai” no tick 40; campo atrás e na base com impacto discreto na fala; explosão principal no **70**; sol formado até **112–116**; absorção **116–142**; nome no **160**; retorno até **240**. PlayerAnimator ergue a katana com ponta para cima durante a fala e a baixa para fora do corpo durante a formação. Aros e núcleo modelados recebem fogo de coroa, partículas e quatro fluxos até a lâmina. O fogo cenográfico tem limite de **288 cards/cena**, **12 partículas/cena/tick** e **48 partículas/tick** no cliente.
- O perfil base usa material emissivo, glowmask, realce do jogador e fog/HUD avermelhado. Isso é **luz aparente**, não ilumina blocos fisicamente. A música `ryujin_entrance_score.ogg` que já existia toca baixa e é reduzida sob a voz; o fogo ambiente existente também é reduzido. O impacto menor usa sons vanilla; explosão/absorção/selamento novos são opcionais num resource pack local.

## Áudio enviado pelo usuário

`MANIFEST.json` e `LICENSE_AND_SOURCE.txt` em `C:\Users\joaop\Downloads` atribuem os stems ao ElevenLabs via Manus, mono Vorbis 48 kHz, durações: `preheat` 3,5 s, `flame_explosion` 2,3 s, `solar_roar_loop` 2,46 s, `flame_absorption` 1,3 s e `blade_seal` 0,9 s. O script `install_local_audio_pack.py` verifica SHA-256 do manifesto e instala os cinco OGGs em `run/resourcepacks/RyujinV7LocalAudio` e no diretório ignorado de QA `run-benihime-qa/ryujin-v7/resourcepacks/RyujinV7LocalAudio`. O pack foi carregado pelo cliente Forge no teste. O preview combinado de 12 s e os WAVs masters não são tocados em paralelo para não duplicar a mixagem.

**Direitos:** o manifesto fornecido limita o uso a projeto pessoal/não comercial e não concede redistribuição GPL. Por isso nenhum desses OGGs foi colocado em `src/main/resources` ou no JAR. O código usa os stems só se o pack local estiver ativo; sem ele, música existente, fala e camadas vanilla continuam. Direitos de distribuição ou troca por material compatível continuam pendentes. O carregamento do pack e o disparo de eventos não substituem aprovação auditiva humana da mixagem.

## Validação técnica e visual

| Prova | Resultado observado | Limite |
| --- | --- | --- |
| `python validate_scene_v7.py` | 220 cubos/54 bones na cena fonte, atlas e clipe de 12 s; 0 problemas | Valida fonte, não enquadramento no jogo. |
| `gradlew check --offline` | **PASS**, 158 regressões, log `run-benihime-qa/ryujin-v7/check-final-v7.log`, após o ajuste final de câmera/escala. | Regressão não substitui revisão audiovisual. |
| `gradlew runGameTestServer -PryujinV7GameTest=true --offline` | **PASS**, **285/285 GameTests obrigatórios** após o último ajuste, log `run-benihime-qa/ryujin-v7/gametest-final-v7.log`. Uma execução intermediária detectou passo excessivo da câmera frontal; a janela de interpolação foi alongada e a suíte repetida. | Server não valida render nem audição. |
| Cliente Forge `runClient` no mundo descartável `run-benihime-qa/ryujin-v7` | Primeiros testes revelaram yaw invertido, altura excessiva e câmera cortada perto de parede; corrigidos. Matriz executou **12/12 cenários** até `BUILD SUCCESSFUL` (`matrix-v7.log`), incluindo dia/noite, primeira pessoa, canhoto, parede, movimento, cancelamentos e forma suprema. Capturas finais diurna e de parede após os últimos ajustes mostram personagem enquadrado, fogo na fala, parede atrás, sol posterior e retração. | A matriz antecedeu o último ajuste de câmera/escala; dia e parede foram repetidos com o código final. Capturas são frames, sem audição ou vídeo contínuo. |

Frames do cliente real, caso noturno frontal: [fala t050](media/ryujin-v7-qa-2026-11-04/night-t050.png), [explosão t084](media/ryujin-v7-qa-2026-11-04/night-t084.png), [sol t112](media/ryujin-v7-qa-2026-11-04/night-t112.png), [retração t130](media/ryujin-v7-qa-2026-11-04/night-t130.png), [limpeza t142](media/ryujin-v7-qa-2026-11-04/night-t142.png). Origem: `run-benihime-qa/ryujin-v7/ryujin-bankai-qa/cycle118-1793828703072/02_night_front/screenshots/`. Os nomes `frame-tNNN` usam o relógio do ensaio de QA; a apresentação começa pelo serviço no servidor e pode ter deslocamento de alguns ticks na captura, então os cues exatos são os do relógio de cena no código. O caso noturno anterior registrou 37 frames, mediana do contador FPS 96, mínimo 5 em inicialização; isso **não** é medição de frametime sustentado.

Capturas finais do caso diurno, repetido após a correção da curva de câmera: [fala/campo t050](media/ryujin-v7-qa-2026-11-04/day-t050.png), [fim da fala t067](media/ryujin-v7-qa-2026-11-04/day-t067.png), [explosão t084](media/ryujin-v7-qa-2026-11-04/day-t084.png), [sol e coroa t112](media/ryujin-v7-qa-2026-11-04/day-t112.png), [contração t130](media/ryujin-v7-qa-2026-11-04/day-t130.png), [limpeza t142](media/ryujin-v7-qa-2026-11-04/day-t142.png). Caso junto à parede após ajuste de foco: [fala t050](media/ryujin-v7-qa-2026-11-04/wall-t050.png), [explosão t084](media/ryujin-v7-qa-2026-11-04/wall-t084.png). Origens: `cycle118-1793831257593/01_day_back/` e `cycle118-1793830561536/05_near_wall/`, dentro de `run-benihime-qa/ryujin-v7/ryujin-bankai-qa/`. O ensaio diurno final terminou com `BUILD SUCCESSFUL` em `final-day-last-v7.log`.

As chamas de bloco que continuam no chão depois da apresentação são emissões pré-existentes de `SpiritFlameService` na forma Bankai, limitadas por sua própria duração de 100 ticks. Não foram criadas pelo novo sol nem demonstram que o renderer ficou preso. Movimento após 60 desliga a pose/câmera local e as fitas direcionadas à espada; a cena no mundo pode continuar até 240 conforme contrato.

## Ainda a validar

- Revisão humana **com áudio**: separação entre fala, impacto menor, explosão principal e música; tamanho e densidade do sol em vídeo contínuo. O usuário já havia aprovado o tamanho da v7 no Blockbench, não este resultado no jogo.
- Duas conexões reais e observador tardio; alinhamento do clipe GeckoLib em cenário de chegada tardia. A matriz de 12 cenários concluiu tecnicamente, mas a inspeção humana de canhoto, primeira pessoa, movimento e interrupções continua útil. Perto da parede, a câmera mantém o ator no quadro, embora a parede possa ocultar parte do sol.
- Perfil Photon e iluminação física de blocos não foram implementados nem testados. Direitos de distribuição dos novos OGGs seguem pendentes.

## Aprendizado incorporado

`.agents/skills/bleach-mod-dev/SKILL.md` ganhou o procedimento de validar eixo Blockbench versus yaw Minecraft no cliente, prender a escala crítica ao tick real da cena quando o clipe GeckoLib começa tarde, e manter áudio de direitos restritos em pack local verificável. A utilidade dessa regra em outro projeto ainda não foi medida.
