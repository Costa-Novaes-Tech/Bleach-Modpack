# Remasterização dos Hollows — 20/10/2026

Este ciclo aperfeiçoa a [primeira versão](../2026-10-20/README.md), a partir do
feedback sobre semelhança com Iron Golem, rigidez, contraste e cauda desconectada.
Branch: `feat/Jigui/new-hollow-model`; HEAD de referência:
`8ace6520e67d27441974b9ad92b443ddb33f9882`. O trabalho anterior estava sem commit
e foi preservado. Snapshot dos fontes iniciais deste ciclo em
`build/hollow-remaster-baseline/`. Nenhum commit, checkout, instalação ou mudança
global de ambiente foi realizado.

## Resultado

O tank ganhou uma anatomia própria: pelve baixa, pernas curtas flexionadas,
costas arqueadas, tórax inclinado, máscara alongada, chifres recuados assimétricos,
crista dorsal, ombros irregulares e patas largas. Volumes menores e inclinados
substituem os grandes paralelepípedos retos. O rig passou de 12 para 25 bones,
incluindo peito, antebraços, punhos, dedos e pés. O tórax continua vazado.

A marcha de 1,25 segundo desloca a pelve, comprime o corpo a cada apoio e
contrarrotaciona o peito. Cabeça, cotovelos, mãos e dedos seguem com atraso,
em vez de oscilar todo o membro como uma peça única. O ataque envolve
preparação do braço e tronco, extensão, reação e retorno à postura.

O voador ganhou juntas externas nas asas. O movimento de recuperação dobra a
parte distal depois do movimento principal; cabeça estabilizada, tronco,
antebraços e pernas respondem em fases diferentes. O pequeno conserva sua
silhueta, com caminhada mais rápida e cauda reconstruída em quatro segmentos.
Cada bone da cauda começa exatamente no fim do anterior; as curvas pertencem
aos bones e propagam a transformação aos filhos. As juntas sobrepõem-se
ligeiramente e a curva sobe, mantendo a ponta afastada do chão.

As paletas têm sombras mais profundas, relevos mais claros, osso quente,
olhos âmbar e membranas rubras mais legíveis. O contraste foi conferido sob
iluminação diurna normal, sem camada emissiva nem dependência de Photon.

## Ataque e gameplay

A IA agora inicia a animação antes de aplicar dano. O contato ocorre após
**8 ticks no tank**, **3 no pequeno** e **5 no voador**, alinhados aos marcos
de 0,40 / 0,15 / 0,25 segundo dos respectivos clips. No contato, o servidor
revalida vida, alvo, dimensão, alcance, visibilidade e estado da IA. Sair do
alcance, trocar de alvo ou desativar a IA cancela o golpe pendente.

O intervalo base entre inícios de ataque continua em 20 ticks. Vida, dano,
velocidade, dimensões, chances de nascimento, persistência, loot e identidade
do mob permanecem como na primeira versão. A diferença intencional é a
antecipação do ataque: o dano deixa de acontecer instantaneamente no início
do gesto. O hook público `doHurtTarget` conserva sua semântica imediata para
chamadas externas; a IA natural usa `beginMeleeStrike` e o contato adiado.
O boss e a Zaraki não foram alterados.

## Evidência no cliente Minecraft

Captura final: `run/hollow-qa/remaster/hollow-evidence/Hollow-QA-1792519082046/`.
Foram registrados **257 PNGs reais** por `Screenshot.grab`, incluindo três
sequências a cada dois ticks. `frames.jsonl` preserva os metadados originais;
as referências de arquivo nele apontam para os PNGs dessa pasta de origem.
Uma seleção de PNGs, sem edição, está em `screenshots/` neste relatório.

Prévia animada montada exclusivamente desses framebuffers:

- [Tank: aproximação, golpe e recuperação — 10,1 s](previews/tank.gif)
- [Voador: asas e combate aéreo — 8,1 s](previews/flight.gif)
- [Pequeno: movimentação, ataque e cauda — 7,1 s](previews/swift.gif)

Os GIFs usam 768 × 432 e paleta de 128 cores para revisão. Não há frames
gerados/interpolados: a duração acompanha os ticks registrados. Os originais
são 1280 × 720. A imagem `03-tank-detail.png` mostra a nova silhueta;
`04-tail-detail.png` mostra as juntas e a curva contínua da cauda;
`tank-408.png` / `tank-420.png` mostram o alvo recebendo dano e a recuperação.

Ambiente: Java 17.0.20.1, Forge 47.4.10 / Minecraft 1.20.1, GeckoLib existente,
perfil base, heap do cliente 2 GiB, distância 6, AMD Radeon RX 7600. As amostras
registraram 109–119 FPS; isto não é um benchmark de carga. As câmeras, alvos e
capturas só existem no laboratório opt-in em ambiente de desenvolvimento.
O cliente final encerrou normalmente após a sequência, com exit code 0.

Avaliação observada, em escala de 0 a 5: identidade/silhueta do tank **4**,
articulação do movimento **4**, contraste **4**, continuidade da cauda **4**.
São modelos em volumes cúbicos articulados; a revisão não equivale a malhas
orgânicas suaves ou a uma homologação de todos os terrenos.

## Verificações

- Baseline deste ciclo: `check`, exit 0, **127 regressões**; log em
  `logs/baseline-check.txt`. Os 160 GameTests do ciclo anterior servem como
  referência histórica, não como uma baseline reexecutada antes desta edição.
- Final: `build` / `check`, exit 0, **127 regressões**; `logs/build.txt`.
- Final: `runGameTestServer`, exit 0, **163 testes obrigatórios aprovados**;
  `logs/gametest.txt`. Os três casos novos medem preparação/contato sem dano
  antecipado, fuga durante o preparo e cancelamento por IA desativada.
- `python tools/models/hollow_variants.py --check`, exit 0: dez clips, bones,
  UVs, valores finitos, continuidade dos loops, retorno das poses e juntas
  da cauda. Fontes Blockbench e exports GeckoLib reproduzíveis.
- Cliente real: `logs/client.txt`, exit 0, modelos e ataques demonstrados.
- `git diff --check`, exit 0.

Uma compilação intermediária precisou da importação de `InteractionHand`.
Os novos fixtures de tempo inicialmente não tinham linha de visão no ponto
escolhido; depois, entidades eram deslocadas durante a medição. O cenário foi
colocado numa plataforma alta e os casos isolados em lotes separados, mantendo
as asserções de dano e de tempo. A perseguição aérea continua coberta por um
teste autônomo, com alvo elevado, dentro da simulação real de entidades.
Nenhuma falha de teste foi desativada ou compensada alterando dano esperado.

Pendências fora desta revisão: multiplayer com duas conexões, Photon,
noite, terreno acidentado, espaços estreitos e carga com muitos mobs.
Capturas a 10 Hz permitem rever o ciclo, mas não substituem medição contínua
de frametime. Os PNGs e relatórios do ciclo inicial foram preservados.

## Reprodução e fontes

```powershell
python tools/models/hollow_variants.py --check
./gradlew.bat --offline runGameTestServer --console=plain
./gradlew.bat --offline build --console=plain
./gradlew.bat --offline -PhollowQa=true -PhollowQaExit=true runClient --console=plain
python tools/qa/hollow_evidence.py <pasta-hollow-evidence> <pasta-de-previews>
```

O modo QA cria um save novo em `run/hollow-qa/remaster`. Sem
`-PhollowQaExit=true`, o cliente permanece aberto após a captura.
Nesta máquina, o launcher temporário `build/hollow-gradle.ps1` mantém o ajuste
de socket Java no caminho curto ASCII já documentado no primeiro ciclo.

Edite `tools/models/source/hollow_*.bbmodel` no Blockbench. Os modelos e os dez
clips são gerados por `tools/models/hollow_variants.py`; o gerador sobrescreve
suas saídas, portanto transporte edições manuais de volta ao script ou exporte
manualmente os assets. As animações ficam no JSON compartilhado, com nomes
`animation.hollow.<variante>.<ação>`.

JAR: `build/libs/bleachmod-0.2.0.jar`.
SHA-256: `E7C68073C33BA479F85A796D340F9F3302B000D29ECDCB5F1A5DB515683AA014`.
