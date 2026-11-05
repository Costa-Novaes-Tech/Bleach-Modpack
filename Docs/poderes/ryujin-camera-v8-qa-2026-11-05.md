# Ryūjin Bankai — câmera frontal em três tomadas (v8)

**05/11/2026.** `Feature-Poderes-bankais`: HEAD `5b86984` no início, avançou durante a rodada para `31f5e1c` por outro processo, que registrou a [integração v7](ryujin-v7-integracao-qa-2026-11-04.md). O Codex não fez commit nem merge. Esta rodada modifica apenas a câmera, seus GameTests, documentação e a skill. Os `.bbmodel` já modificados no início foram preservados e passaram a constar no commit externo; os worktrees `main`, Quincy e design não foram alterados por esta rodada.

## Decisão e implementação

O usuário pediu uma tomada frontal próxima quando a katana sobe, recuo panorâmico um pouco acima do jogador para mostrar explosão, chamas e sol inteiro atrás dele, e novo zoom frontal na lâmina em cinzas selada. `RyujinBankaiCamera.sample` agora mantém o olho no eixo frontal do conjurador durante as três tomadas:

| Beat autoral | Composição | Relação com o relógio real de 240 ticks |
| --- | --- | --- |
| 0–10 | Plano médio aproxima até olho a 4,3 blocos, FOV 48°, foco na katana erguida e rosto. | A aproximação termina antes da voz no tick real 40. |
| 10–15 | Close frontal sustenta o início de “Bankai”. | Voz começa no tick real 40; o recuo começa durante a fala. |
| 15–27 | Dolly frontal abre progressivamente até olho a 14,2 blocos à frente e 9 acima, FOV 80°. | A explosão principal no tick real 70 inicia durante o recuo; o plano já está largo na formação do sol. |
| 27–34 | Panorama frontal alto, foco entre jogador e sol, com coroa inteira entre as barras. | Inclui o sol pleno no tick real 112. |
| 34–64 | A câmera acompanha a contração e volta a se aproximar até 3,8 blocos à frente, FOV 48°. | O close final chega após o selamento no tick real 142, mostrando a lâmina carbonizada. |

O retorno posterior à perspectiva escolhida pelo jogador continua no controlador existente. Movimento/ataque após a liberação no tick 60 ainda pode encerrar a câmera local. A colisão com blocos evita atravessar paredes; perto delas, o plano aberto pode perder parte do sol, mas mantém o jogador no quadro. Não foi alterado dano, duração, commit/release ou áudio.

## Correção encontrada em QA

A primeira versão do panorama usou o FOV nominal para prever espaço do sol, mas a captura diurna `frame-t112` cortou as pontas superiores da coroa sob a barra preta. A causa era mirar baixo demais para o sol e não descontar as barras de 10% em cada borda. A posição final elevou o foco, recuou um pouco e ampliou o FOV para 80°. O GameTest agora verifica o campo vertical **visível entre barras**, com margem para o raio modelado e suas chamas; a captura repetida mostra a coroa completa.

## Provas

| Verificação | Resultado |
| --- | --- |
| `gradlew check --offline --console=plain` | **PASS**, 158 cenários de regressão, `run-benihime-qa/ryujin-v7/check-camera-v8.log`. |
| `gradlew runGameTestServer -PryujinV7GameTest=true --offline --console=plain` | **PASS**, **285/285** GameTests obrigatórios, `run-benihime-qa/ryujin-v7/gametest-camera-final.log`. Os testes verificam aproximação/recuo/segundo close, plano sempre frontal, velocidade da curva e campo visível do sol. |
| Cliente Forge `runClient` no mundo QA isolado | **12/12 cenários concluídos**, `BUILD SUCCESSFUL` em `run-benihime-qa/ryujin-v7/client-camera-matrix-final.log`: dia, noite, primeira pessoa, canhoto, parede, movimento, cancelamentos e forma suprema. |
| `git diff --check` | **PASS**; apenas aviso de conversão LF/CRLF em documento Markdown, sem erro de whitespace. O Codex não fez commit ou merge. |

Capturas diretas do cliente final: [zoom da fala](media/ryujin-camera-v8-2026-11-05/day-speech.png), [explosão](media/ryujin-camera-v8-2026-11-05/day-explosion.png), [panorama com sol inteiro](media/ryujin-camera-v8-2026-11-05/day-sun.png), [close da espada selada](media/ryujin-camera-v8-2026-11-05/day-seal.png), [sol à noite](media/ryujin-camera-v8-2026-11-05/night-sun.png), [fala com mão esquerda](media/ryujin-camera-v8-2026-11-05/left-speech.png), [parede próxima](media/ryujin-camera-v8-2026-11-05/wall-sun.png) e [retorno à primeira pessoa](media/ryujin-camera-v8-2026-11-05/first-return.png). Origem: `run-benihime-qa/ryujin-v7/ryujin-bankai-qa/cycle118-1793875459815/`; os nomes `frame-tNNN` usam o relógio do ensaio, que pode ter pequeno deslocamento em relação ao relógio real da cena.

## Limites para o aceite

As imagens provam composição em quadros selecionados, não fluidez subjetiva em vídeo contínuo nem mixagem sonora. O usuário ainda precisa avaliar a sequência completa com áudio no próprio cliente. Multiplayer com dois observadores e chegada tardia não foram testados nesta rodada. A inclusão dos OGGs fornecidos no JAR continua condicionada a direitos de redistribuição; o pack local e as camadas vanilla permanecem como descritos na v7. A skill `.agents/skills/bleach-mod-dev/SKILL.md` foi atualizada com a lição do campo visível entre barras e da colisão junto a paredes; sua eficácia em outra feature ainda não foi medida.
