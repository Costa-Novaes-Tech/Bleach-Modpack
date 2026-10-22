# Hollows: cadência e remasterização aérea — 20/10/2026

Implementação do pedido de ataques distintos e passagens aéreas semelhantes às
dos Phantoms. Branch `feat/Jigui/new-hollow-model`, HEAD de referência
`8ace6520e67d27441974b9ad92b443ddb33f9882`. O trabalho anterior estava sem commit
e foi preservado; os relatórios dos dois ciclos anteriores continuam intactos.
Baseline local em `build/hollow-flight-baseline/`; estado original em
[status-before.txt](logs/status-before.txt). Nenhum commit ou push foi realizado.

## Comportamento entregue

| Variante | Intervalo entre inícios | Preparação até contato | Dano base |
|---|---:|---:|---:|
| Tank / brute | 48 ticks / 2,4 s | 8 ticks / 0,40 s | 12 |
| Pequeno / swift | 14 ticks / 0,7 s | 3 ticks / 0,15 s | 4 |
| Voador / flying | 28 ticks / 1,4 s dentro da passagem | 5 ticks / 0,25 s | 7 |

Tempos a 20 TPS. São intervalos mínimos; perseguição, perda de alcance e falta de
visibilidade podem adiar o próximo ataque. O servidor controla a cadência e
persiste o cooldown no NBT. Cancelar a preparação não libera um novo golpe.
O dano continua condicionado ao alcance e à linha de visão no instante do contato.

O voador circula, mergulha, tenta dois ou três golpes, recupera a pose e sobe para
se afastar. O afastamento dura de 60 a 100 ticks (3–5 s), seguido de 35 ticks de
circulação antes de retornar. Não causa dano nessa fase, mesmo com cooldown livre
e um alvo próximo. Alvos que escapam e caminhos inviáveis podem interromper uma
passagem antes de completar o combo (limites de aproximação e combate evitam
perseguição infinita). Dois ou três ataques não significam acerto garantido.

A navegação usa colisão normal. Perto do alvo, um segmento curto verifica todo o
volume do corpo antes de aproximar com precisão. A frenagem no alcance evita que
a inércia do mergulho invalide a própria preparação. Perda/morte do alvo limpa a
passagem e o ataque pendente. A IA terrestre também para no alcance e resolve o
último pequeno trecho da navegação, mantendo a pressão do pequeno contra alvos
estreitos. O timer privado de 20 ticks do goal vanilla deixou de limitar o swift.

Vida, dano, velocidades de deslocamento, dimensões, loot, spawn e identidade
compartilhada das variantes permanecem. O boss mantém sua IA e arte anteriores.

## Arte e animação

O voador passou de 16 bones / 77 cubos para **25 bones / 162 cubos**: tórax
inclinado independente, cintura afunilada, máscara alongada, olhos e chifres,
crista dorsal, braços e punhos segmentados, garras, pernas flexionadas e cauda
bifurcada. Cada asa possui três articulações, painéis de membrana, nervuras e
pontas próprias. Osso claro, corpo violeta escuro e membranas rubras reforçam
o contraste. O vazio do peito continua aberto.

Voo, mergulho, afastamento e ataque têm movimentos distintos. A defasagem entre
raízes e pontas das asas, peito, punhos, pés e cauda reduz a aparência de peça
rígida. A fase aérea é sincronizada pelo servidor. Há 12 clips no JSON compartilhado.
Geometria, texturas, fontes Blockbench e clips do tank e pequeno foram comparados
com a baseline e continuam idênticos byte a byte.

## Validação executada

| Etapa | Resultado |
|---|---|
| Baseline `check` | PASS, 127 cenários, exit 0 |
| `build` final (inclui `check` e `regressionTest`) | PASS, 127 cenários, exit 0 |
| `runGameTestServer` final | PASS, 169 testes obrigatórios, exit 0 |
| Gerador `--check` | PASS: assets reproduzíveis, UVs, bones, loops, recuperação e cauda |
| Preservação brute/swift | PASS: assets e clips idênticos à baseline |
| JAR | PASS: 3 variantes, 12 clips, goal aéreo e bytecode Java 17 |
| Cliente Forge real, perfil base | PASS: captura e encerramento automático, exit 0 |
| `git diff --check` | PASS |

Seis GameTests de combate verificam: seis ataques naturais do tank a cada 48 ticks;
seis do pequeno contra Husk estreito a cada 14; cooldown após cancelamento/reload;
cinco passagens reais do voador contra Husk com 2–3 tentativas, dano real,
intervalos >=28, altura/distância no recuo e nenhum dano durante o recuo;
limpeza após morte do alvo; solicitações repetidas do voador a cada 28 ticks.
Não são testes apenas de constantes: usam entidades, navegação, goals e dano reais.

Correções encontradas durante este ciclo: throttle vanilla de 20 ticks, navegação
parando fora do alcance ou ultrapassando alvos pequenos, sobreposição diagonal
de AABBs e consumo de MOVE_TO pelo FlyingMoveControl a cada tick. A captura
anterior revelou uma passagem que errou por inércia; a versão final inclui
frenagem e o GameTest prolongado com alvo estreito. Logs intermediários em
`build/` não substituem os logs finais anexados aqui.

## Evidência real

Origem final: `run/hollow-qa/aerial/hollow-evidence/Hollow-QA-1792521799570`.
**577 PNGs de 1280 × 720**, obtidos pelo framebuffer via `Screenshot.grab`.
[frames.jsonl](frames.jsonl) conserva os caminhos dos originais nessa pasta;
[combat.jsonl](combat.jsonl) registra a simulação do servidor a cada dois ticks.
Uma seleção sem edição está em [screenshots/](screenshots/).

- [Voador: passagens, golpes e afastamento — 36,1 s](previews/flight.gif)
- [Tank: cadência lenta — 11,1 s](previews/tank.gif)
- [Pequeno: pressão contínua — 10,1 s](previews/swift.gif)
- [Voador de frente](screenshots/03-flight-detail.png) / [asas por trás](screenshots/04-flight-rear.png)
- [Resumo quantitativo da captura](previews/combat-summary.json)

Os GIFs usam apenas frames reais, reduzidos para 768 × 432 / 128 cores, sem
interpolação; a duração acompanha os ticks. A câmera ampla preserva o voo e o
alvo, mas mostra menos detalhe anatômico que os PNGs de aproximação.

Na captura final, o tank realizou 5 acertos com intervalos
[48, 48, 48, 48] ticks; o pequeno realizou 15 acertos, todos os intervalos
registrados em 14 ticks. O voador iniciou 11 golpes em 5 passagens e
acertou 11; tentativas por passagem: [2, 2, 2, 3, 2].
Acertos por passagem: [2, 2, 2, 3, 2].
Os 4 afastamentos completos duraram [60, 60, 60, 60] ticks;
o último pode estar parcial no fim da captura. Todos registraram zero dano
durante o afastamento.

Ambiente: Windows, Java 17.0.20.1, Minecraft 1.20.1 / Forge 47.4.10,
GeckoLib já configurado no projeto, AMD Radeon RX 7600, heap de cliente 2 GiB,
distância de renderização 6, perfil base sem Photon. Amostras de FPS:
**108–119**, durante captura de PNG; não é benchmark de carga/frametime.
Avaliação visual de 0 a 5: silhueta/contraste do voador 4; articulação e leitura
do voo 4; legibilidade do afastamento 4; contato das garras 3 (volume cúbico e
alcance continuam aproximações). Câmera e VFX de Bankai não se aplicam a este ciclo.

Servidor dedicado GameTest: PASS. Multiplayer com duas conexões, observador
remoto, Photon, noite, terreno irregular, tetos baixos, carga com muitos mobs,
troca de dimensão/desconexão durante um mergulho: **PENDENTES**, sem presumir
homologação pela sessão singleplayer. Não foram instaladas dependências nem
alterados PATH/configurações globais; cache Gradle existente usado em modo offline.

## Reprodução e artefato

```powershell
python tools/models/hollow_variants.py --check
./gradlew.bat --offline runGameTestServer --console=plain
./gradlew.bat --offline build --console=plain
./gradlew.bat --offline -PhollowQa=true -PhollowQaExit=true runClient --console=plain
python tools/qa/hollow_evidence.py <pasta-hollow-evidence> <pasta-de-previews>
```

Use Java 17. Nesta máquina o launcher local ignorado `build/hollow-gradle.ps1`
reutiliza o ajuste de socket em caminho ASCII documentado nos ciclos anteriores.
O laboratório é opt-in, apenas em desenvolvimento, e cria um save novo em
`run/hollow-qa/aerial`; não abre mundos existentes. Sem `hollowQaExit`, o cliente
continua aberto após a captura.

Fontes editáveis: `tools/models/source/hollow_flying.bbmodel` e gerador
`tools/models/hollow_variants.py`. Transporte edições manuais de volta ao gerador
ou exporte os assets manualmente, pois o gerador sobrescreve suas saídas.
Principais fontes de comportamento: `HollowVariant.java`, `HollowEntity.java`,
`HollowFlightCombatGoal.java`; cobertura em `HollowCombatGameTests.java`.

JAR: `build/libs/bleachmod-0.2.0.jar` (15267820 bytes).
SHA-256: `A14D43784573759DFD756A2F904DFD84FB31AE62C78F57142585C18494959695`.
Logs finais: [build](logs/build.txt), [GameTests](logs/gametest.txt), [cliente](logs/client.txt).

Estado final: [status-after.txt](logs/status-after.txt).
Validação estrutural e preservação: [assets-check.txt](logs/assets-check.txt).
Logs gerados pelas regressões foram arquivados neste ciclo; o log versionado
da raiz voltou aos bytes originais, pois estava limpo na baseline.
