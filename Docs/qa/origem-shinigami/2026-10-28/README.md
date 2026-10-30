# QA — abertura Shinigami e livro de jornada

> Snapshot da primeira entrega. Para pose sentada, aproximação do jogador em pé,
> explosão reforçada, Fishbone lateral e áudio do vídeo, ver
> [revisão 2](../2026-10-28-revisao-2/README.md).

28/10/2026 · `feat/Jigui/classes` · HEAD de referência `8df8a64d`.

**Resultado:** incremento funcional validado no cliente Forge real e em
GameTestServer dedicado. Primeira versão da apresentação; casa e poste definitivo,
voz final, braço destacado e acabamento de animação ainda são trabalho artístico.
Dois clientes reais e Photon permanecem pendentes.

## Evidência e reprodução

- Minecraft 1.20.1, Forge 47.4.10, Java Temurin 17.0.20.1, Windows.
- Perfil base sem Photon; AMD Radeon RX 7600, OpenGL 4.6.
- Cliente 1280×720, escala de GUI 2, distâncias de render/simulação 5.
- Mundo novo e descartável: `Origin-QA-1793211684909`, em `run/origin-qa`.
  Superflat, sobrevivência, dificuldade Normal, sem spawn natural. O jogador de
  QA recebe invulnerabilidade exclusivamente na fixture. Não há esse benefício
  permanente no fluxo de produção.
- Frames originais: `run/origin-qa/evidence/Origin-QA-1793211684909/screenshots/`.
  Capturas da cinematográfica por tick, mais cinco marcos posteriores. Metadados em
  [frames.jsonl](frames.jsonl), preservados sem alteração.
- [Prévia MP4](abertura-preview.mp4): sequência temporal dos frames reais do
  framebuffer, com intervalos derivados dos ticks. **Não é gravação contínua da
  tela.** A fala foi inserida a partir do mesmo asset Ogg do jogo, com atraso
  derivado do marco da fala no tick 38 e do primeiro frame disponível. Os efeitos sonoros do cliente não foram
  gravados nesse arquivo. [Proveniência da mídia](media-origin.json).
- O avanço final da quest usa um golpe de 10000 de dano com fonte do dono na
  fixture. Demonstra o fluxo de morte/crédito/livro, não a dificuldade da luta.
- Reproduzir automaticamente: `./gradlew.bat --offline -PoriginQa=true runClient --console=plain`.
  Perfil desativado por padrão e bloqueado em ambiente de produção.
- Reproduzir manualmente: mundo novo, escolher Shinigami, confirmar aparência,
  permanecer no chão em terreno aberto no Overworld. Abrir o livro com o botão
  direito; iniciar outras missões pelo botão do diário. Mundo antigo não força
  a abertura novamente.

## Verificações concluídas

| Verificação | Resultado e evidência |
|---|---|
| Baseline antes do incremento | `check` verde; 146 cenários de regressão. [Log](origin-baseline.log) |
| `check build runGameTestServer` final | **PASS**, Gradle `BUILD SUCCESSFUL`, 146 regressões e **230 GameTests obrigatórios aprovados**. [Log](origin-validation.log) |
| Servidor após o ajuste da direção inicial | **PASS**, 230 testes obrigatórios e `BUILD SUCCESSFUL`. [Log final](origin-validation-final-server.log) |
| Cliente final | **PASS**, `Origin QA COMPLETE`, `BUILD SUCCESSFUL`. [Log](origin-client.log) |
| Confirmação de personagem | Pacote real `ConfirmCharacterC2S`; estado e inventário sincronizados |
| Mouse | A fixture chama `Entity.turn(40,30)` durante a cena, o mesmo caminho dos deltas de mouse; yaw do jogador permanece bloqueado |
| Âncora e câmera | Posição permanece na âncora, câmera dirige sete enquadramentos; modo anterior `FIRST_PERSON` restaurado |
| Transferência | Lâmina avança, jogador reage, clarão azul e partículas, espada equipada no cliente |
| Boss | Avança de z local 14 para 4,7; braço presente antes e ausente após tick 222; IA liberada ao fim |
| Livro | Aberto pelo uso real do item; a mesma tela passa de luta a conclusão após a morte do boss |
| Referência de formação | Páginas finais apresentam as quatro disciplinas do painel anterior |
| Direção no mundo | Fixture começa a 67 graus; câmera, atores e orientação do dono sincronizados nessa direção |
| Cancelamento no servidor | GameTest inicia a cena, rejeita início duplicado/ação, cancela e confirma liberação e descarte dos atores |
| Cena completa no servidor | GameTest avança o relógio até o combate e verifica checkpoint, quest, IA, braço e espada |
| Persistência | GameTests de cópia/save/sync parcial/legado/UUID inválido; braço persistente e restrição de alvos do boss |

Os cinco GameTests deste incremento são `OriginGameTests`; os outros 225 foram
preservados. O arranque dedicado detectou e permitiu corrigir uma referência de
interface que inicialmente vazava para a classe do item no servidor. As tentativas
intermediárias estão em `build/origin-*-attempt*.log`, fora do versionamento.
O primeiro cenário de QA também foi corrigido para carregar o chunk antes de
calcular a altura do teleporte da fixture. Nenhuma dessas falhas foi ocultada
como resultado aprovado.

## Inspeção visual dos marcos finais

| Marco | Esperado e observado | Avaliação inicial (0–5) |
|---|---|---:|
| [Detalhe](scene-020.png), tick 20 | Lâmina e mão em destaque; sem UI de criação sobreposta | 3 |
| [Rukia](scene-080.png), tick 80 | Close legível, poste ao fundo, legenda exata | 4 |
| [Reação](scene-114.png), tick 114 | Contraplano do jogador, com a skin atual preservada | 3 |
| [Perfil](scene-148.png), tick 148 | Rukia, espada e jogador no mesmo plano; poste provisório | 3 |
| [Contato](scene-174.png), tick 174 | Avanço contra a região do peito, recuo visual do tronco | 3 |
| [Explosão](scene-186.png), tick 186 | Clarão azul e emissão sem destruir terreno | 4 |
| [Avanço](scene-216.png), tick 216 | Hollow mais perto dos dois, ainda com os dois braços | 3 |
| [Corte](scene-224.png), tick 224 | Plano alto inclui o boss inteiro e o braço removido | 3 |
| [Controle](01-control-restored.png) | HUD e primeira pessoa voltam, espada selecionada, objetivo 0/1 | 4 |
| [Livro durante luta](02-live-book-fight.png) | Item abre páginas vivas; Rukia permanece no encontro | 4 |
| [Livro após vitória](03-live-book-complete.png) | Mensagem muda para conclusão sem fechar/reabrir o livro | 4 |
| [Disciplinas](05-study-notes-disciplines.png) e [continuação](04-study-notes-last-page.png) | Conteúdo do antigo painel preservado em páginas do livro | 4 |

As notas são inspeção inicial das capturas, não aceitação artística definitiva.
O clipe fornece continuidade por amostragem de ticks; FPS, frametime e latência
não foram medidos. Não concluir desempenho a partir dessas imagens.

## Pendências explícitas

- Casa e ponto de encenação na entrada; modelo final do poste. O chão superflat
  das capturas é um laboratório, não o cenário final da história.
- Voz de Rukia definitiva. A atual é síntese local SAPI/Maria Desktop, provisória.
- Animação refinada de mãos/contato e corte; braço destacado com trajetória.
- QA manual de combate/dificuldade, terrenos irregulares, área estreita,
  pausa/desconexão/reentrada em luta, morte e troca de dimensão no cliente real.
  Existem tratamentos de código; esses cenários não foram homologados visualmente.
- Sessão com observador em segundo cliente, jogador conectado a servidor
  dedicado e perfil Photon/LDLib. GameTestServer não equivale a esses testes.

## Estado do checkout

Já havia mudanças não commitadas da etapa 2: AcademyData, painel Formação,
sincronização, traduções, fixture de UI e documentação/QA. Foram preservadas;
o acesso Formação passou a abrir o livro solicitado. Nenhum checkout, reset,
stash, commit, push, merge ou mudança na Zaraki foi feito.

O incremento acrescenta checkpoint de origem, serviço do encontro, pacote de
cena (protocolo 2.36), câmera/poses e bloqueio de mouse, item/tela/receita do livro,
quest inicial, estado do braço no Hollow e novas fixtures. Logs compactados da
raiz já apareciam modificados; não foram restaurados por cima do trabalho local.
