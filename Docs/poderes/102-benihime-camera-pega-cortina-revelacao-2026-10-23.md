# 102 — Benihime: câmera na pega e revelação da cortina

23/10/2026. Usuário aprovou a direção detalhada: jogador → cortina → mão → rosto → corpo inteiro → composição final. Implementação na Feature-Poderes-bankais, base a12b4d1, checkout inicialmente limpo. Sem alterações na branch de design, commit, push ou merge nesta rodada.

## Direção implementada

- 0–72 ticks / 0–3,6 s: close inicial e fala preservados.
- 72–108 / 3,6–5,4 s: recuo interpolado a partir do close real, sem o salto anterior de posição/FOV. Cortina sobe fechada e penumbra existente permanece.
- 108–138 / 5,4–6,9 s: aproximação em três quartos até a abertura central.
- 138–176 / 6,9–8,8 s: close sustentado em dedos, punho, borda e parte do antebraço. Foco calculado com hemAnchor e sample().left().wrist(), as mesmas âncoras procedurais do tecido/mão.
- 176–236 / 8,8–11,8 s: recuo acompanhado da puxada; foco migra progressivamente da pega para a cabeça real do rig. A figura continua oculta pelo tecido e pela sombra original até a abertura.
- 236–260 / 11,8–13 s: enquadramento abre para o corpo e o segundo braço, com espaço para os apoios.
- 260–280 / 13–14 s: reencontro contínuo com o plano amplo estabelecido; desse ponto até o final, preservada a composição anterior de avanço, levantamento, fios e retorno.

O deslocamento físico faz a aproximação principal; FOV 42° no detalhe, 50° na revelação e 58° no plano amplo. Interpolação quintic nas novas passagens; partial ticks, colisão com blocos, restauração de perspectiva e controle existentes preservados. A bancada estática de inspeção mantém suas vistas específicas.

## Contratos preservados

Cena 560 ticks / 28 s; fases geométricas, dedos/apoios, poses do jogador, câmera de observadores, áudio, música, cue da fala do nome, penumbra, acompanhamento posterior e regras de gameplay não foram retimados. Nenhum alongamento de trava, invulnerabilidade, custo ou dano. Não houve mudança no modelo da Benihime, Ryūjin ou Senbonzakura.

O close ocorre após a formação completa do piso de apresentação (tick 108), quando o rig usa a referência plana; por isso a câmera pode amostrar as âncoras locais do mesmo rig sem inventar outro alinhamento de terreno.

## Validação

- Check/build: aprovado, 147 regressões; conferência final 1m12s. Novo cenário verifica trajetória finita/contínua, foco na borda durante a pega e reencontro com a composição ampla; não substitui inspeção visual.
- Cliente Forge real: execução concluída, exit 0. Duas transformações autoritativas server-release e acompanhamento após translação/giro: 282 capturas de dia e 284 na repetição noturna. Conferidos os marcos de pega, abertura, apoios e pose final. A noite foi sincronizada após os primeiros 10 quadros (6000 → 18000); as tomadas novas já estão em 18000. Os 10 cenários automáticos Hyōrinmaru seguintes também terminaram; isso é fumaça de integração, não nova homologação artística dela.
- Evidências: [dia](qa/cycle102/1792799655443/index.html), [noite](qa/cycle102/1792799687599/index.html), [prancha de comparação](qa/cycle102/camera-contact-sheet.jpg) e [log do cliente](qa/cycle102/client.log). Vídeos são montagens de screenshots amostrados nos intervalos reais, sem áudio; não comprovam fluidez de todos os frames ou avaliação auditiva.
- Auditoria visual: mão aparece junto à borda, câmera permanece fora do tecido; rosto surge ainda como silhueta e ganha leitura ao avançar. Aos 280 ticks, o plano amplo inclui ambos os apoios e o jogador; pose/fios finais preservados. Penumbra não foi alterada. Terreno irregular, paredes e retorno em perspectivas diferentes continuam no roteiro manual.
- GameTests de gameplay não reexecutados: alteração restrita à apresentação. Photon/observador e outras condições não presumidos.

## Teste do usuário

Ativar a Bankai parado e observar: cortina fechada, dedos surgindo pela abertura, mão inteira no close, tecido acompanhando a tração, rosto revelado sem salto, segundo braço/apoios visíveis antes do avanço e retorno ao F5. Se optar por sair da câmera com movimento, a regra existente permanece. Conferir depois primeira pessoa, F5 frontal, paredes e terreno irregular. Homologação artística depende do playtest.

## Artefato final

JAR `build/libs/bleachmod-0.2.0.jar`; SHA-256 `4c70c5f8eaf89a69264000212dfb6145a6e560a99c7250246b9bd59c2f9538b0`. [Verificação e limites](qa/cycle102/verification.json), [check final](qa/cycle102/final-check.log). Logs rotativos gerados pelo teste foram preservados em qa/cycle102/generated-root-logs e os originais rastreados restaurados; nenhum log de execução alheia foi descartado.
