# Abertura Shinigami e diário vivo

> **Revisão 2, 28/10:** Rukia agora fica sentada e fixa junto ao poste; o jogador
> avança em pé até a espada elevada, sem se agachar. Fishbone entra pela esquerda
> dela/direita do jogador; explosão reforçada e áudio substituído por 1:27–1:30 do
> vídeo solicitado. A descrição abaixo registra a primeira entrega. Ver o
> [relatório atualizado](../qa/origem-shinigami/2026-10-28-revisao-2/README.md).

28/10/2026. Branch `feat/Jigui/classes`; base `8df8a64d`. Implementação local, sem commit.

## Direção solicitada

O usuário substituiu o próximo incremento da Academia por esta fatia: nascer no
Overworld, escolher Shinigami, receber os poderes de Rukia em uma cinematográfica
e enfrentar o primeiro Hollow. O acompanhamento passa a ser feito em um livro
atualizado pelas quests. Essa direção provisória tem precedência sobre o plano
anterior da etapa 2 neste incremento; não constitui revisão silenciosa de todos
os contratos de formação, carreira, recompensas ou eras do mod completo.

Fontes relacionadas: [dossiê da etapa 2](etapa-2-academia-shinigami-2026-10-28.md),
[M03 — história](../game-design/03-storyline-macro.md),
[M04 — origens](../game-design/04-racas-e-origens.md),
[M08 — NPCs](../game-design/08-npcs-mentores-e-faccoes.md) e
[M10 — lugares](../game-design/10-dimensoes-locais-e-estruturas.md).
O pedido atual fornece o encontro e a fala; documentos anteriores não autorizam
inventar a casa, a próxima missão, graduação, Mastery ou recompensas novas.

## Experiência implementada

1. A criação mantém a escolha de classe e aparência. Confirmar Shinigami registra
   a abertura e entrega o **Diário de jornada** no inventário.
2. A cena inicia automaticamente no Overworld quando o personagem estiver no
   chão e houver um corredor livre para os atores. O servidor tenta oito direções.
   Se não couber, os controles continuam normais e uma mensagem orienta a procurar
   terreno aberto e plano. Não há teleporte para outra dimensão.
3. Rukia aparece junto de um poste visual temporário, aponta a espada, diz
   “Você precisa se tornar um Ceifador de Almas” e avança a lâmina contra o peito.
4. O clarão e as partículas azuis revelam a espada do jogador. O Hollow avança;
   um corte de energia remove visualmente seu braço direito.
5. A câmera anterior volta, a espada fica selecionada e a missão de derrotar o
   Hollow é acompanhada pelo sistema existente. Rukia permanece no local durante
   a luta, sem combater ou oferecer quests de demonstração.
6. A morte do boss pelo dono conclui o encontro. O livro mostra a conclusão e as
   quests iniciadas/concluídas; o botão de missões leva ao diário já existente.
   A campanha de demonstração existente não foi reescrita neste incremento.

O livro também pode ser aberto pelo botão **Diário de jornada** do diário de
missões. Há receita sem forma com um livro vanilla e entrada na aba criativa.
O item contém a interface, não uma cópia dos dados do leitor: trocar o item não
transfere quests, poderes ou progresso. As páginas consultam os snapshots atuais
do personagem, inclusive enquanto a interface permanece aberta.
Ao fim, um capítulo de Formação preserva a apresentação de Zanjutsu, Hakuda,
Hohō e Kidō que estava no painel anterior, como material de consulta.

## Câmera e relógio

Referência: [Ichigo se torna um Shinigami](https://www.youtube.com/watch?v=5em1NSbBUyw).
Foram observados trechos em aproximadamente 0, 5, 10, 15, 20 e 23 segundos no
player do YouTube, sem baixar o vídeo. A montagem adapta detalhes da lâmina,
close de Rukia, contraplano do jogador, perfil, plano geral da explosão,
plano alto do corte e retorno ao jogador. Não replica o vídeo inteiro.

| Marco | Tick de simulação | Tempo a 20 TPS |
|---|---:|---:|
| Detalhe da espada | 0–37 | 0–1,9 s |
| Fala / close de Rukia | 38–105 | 1,9–5,3 s |
| Reação do jogador | 106–143 | 5,3–7,2 s |
| Avanço e contato da espada | 144–183; contato 174 | 7,2–9,2 s |
| Explosão e corrida do Hollow | 184–217 | 9,2–10,9 s |
| Corte e braço removido | 222 | 11,1 s |
| Retorno e combate | 240–260 | 12–13 s |

O relógio é do servidor; lag pode alongar a duração real. Câmera e poses usam a
mesma origem e direção do encontro, não coordenadas fixas do mapa. Movimento,
salto, ataque, uso e deltas de mouse são bloqueados durante a cena. O bloqueio de
movimento e as ações também têm verificação no servidor. O observador recebe a
apresentação dos atores, mas conserva sua câmera e seus controles.

## Persistência e interrupções

`OriginData` usa checkpoints pessoais: `NONE`, `PENDING`, `CINEMATIC`, `FIGHT`,
`RETRY`, `COMPLETE`, além do UUID e da última posição conhecida do boss.

- Personagens antigos permanecem em `NONE`: entrar em um save antigo não inicia
  compulsoriamente uma transformação ou apaga poderes. Testar a criação usa um
  personagem/mundo novo.
- Interromper a cinematográfica limpa atores temporários, libera ações e deixa
  o encontro pendente. Um checkpoint `CINEMATIC` salvo por queda do processo
  também volta a pendente ao entrar.
- Sair durante a luta preserva o boss e seu braço removido. Trocar de dimensão
  ou morrer na luta prepara nova tentativa; abrir o livro com o botão direito
  em terreno livre reinicia apenas o combate.
- Ausência em chunk descarregado não significa vitória. Se o boss faltar por
  dez segundos em seu último chunk integralmente carregado, habilita nova
  tentativa. Bosses antigos de uma tentativa encerrada são rejeitados ao carregar
  se o dono estiver online.
- Um segundo jogador não pode causar dano ou receber crédito pelo boss pessoal.
  O boss não escolhe espectadores ou NPCs como alvos. Não muda a era do servidor.
- A câmera restaura o modo anterior no encerramento, morte, troca de mundo ou
  desconexão; há timeout de segurança para perda da mensagem de encerramento.

## Assets e limites desta entrega

- Casa e localização definitiva: **ainda não criadas**, conforme o pedido. O
  encontro usa o chão existente e três displays de cerca como poste sem colisão,
  sem substituir blocos. O vínculo com a entrada da futura casa fica pendente.
- Rukia e Hollow usam os modelos/texturas já presentes. A pose e a apresentação
  da espada são específicas desta cena. O braço removido é um estado persistente
  do modelo; ainda não há peça de braço destacada caindo no chão.
- A fala tem legenda e áudio **provisório**, gerado localmente pela voz Microsoft
  Maria Desktop, SAPI/Windows, ritmo -1; duração aproximada de 4,91 s, Ogg Vorbis
  mono 22050 Hz. Não é áudio retirado do anime nem imitação da dubladora.
- Conversão local com FFmpeg 7.1, fornecido pelo pacote gratuito
  `imageio-ffmpeg` 0.7.0 instalado somente em `build/audio-tools` (ignorado pelo
  Git). Sem serviço cloud ou API paga.
- Não entrega a campanha completa, a Academia jogável, uma nova carreira ou
  números de recompensa. A integração seguinte deve especificar a missão após
  o primeiro boss e a relação futura com a formação.

## Validação

O perfil `-PoriginQa=true runClient` cria exclusivamente um mundo descartável em
`run/origin-qa`, usa o pacote real de confirmação de personagem, registra frames
do framebuffer e verifica mouse, posição, câmera, braço removido, espada
selecionada, abertura pelo item e conclusão sincronizada. A morte de QA usa dano
administrativo com fonte do dono; não substitui teste humano de dificuldade.

Resultados, arquivos e pendências visuais estão no relatório em
`Docs/qa/origem-shinigami/2026-10-28/README.md`. Servidor dedicado com jogador
conectado, dois clientes reais e Photon são validações separadas dos GameTests.
