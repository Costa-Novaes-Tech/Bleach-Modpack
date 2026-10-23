# Primeiro boss Hollow — remodelagem e combate

Data: 20/10/2026. Branch: `feat/Jigui/new-HollowBoss-model`.
Commit de referência: `45d790bcc49946f310127efbc93bcd5fb89eef87`.
Workspace inicialmente limpo. Sem checkout, commit, push ou mudanças na Zaraki/Benihime.

## Resultado implementado

Modelo original feito a partir das três referências fornecidas pelo usuário: máscara branca larga com sorriso e olhos amarelos, corpo cinza violáceo com marcas vermelhas, cabeça adiantada entre os ombros sem pescoço exposto, costas arqueadas, barbatana dorsal, braços compridos com mãos de apoio e pernas articuladas. O furo no peito atravessa a geometria; não é um decalque preto.

Altura de 5,4 blocos (3 × player de 1,8); colisão de 2,8 × 5,4. Rig GeckoLib com 30 bones, 222 volumes e cinco animações: idle, caminhada apoiada nos braços, ataque básico, investida e soco duplo. Modelo, textura e animações estão reunidos no arquivo editável `tools/models/source/hollow_boss.bbmodel`; o gerador reproduz os recursos do jogo.

Mantidos o tipo `bleachmod:hollow_boss` e a integração com a missão da Rukia. O balanceamento final usa 100 HP e dano básico de 5. Velocidade de movimento reduzida para 0,23 e resistência a empurrão elevada para 0,75. Nome localizado: Hollow do Primeiro Encontro / First Encounter Hollow.

| Golpe | Preparação | Recarga entre inícios | Dano |
|---|---:|---:|---:|
| Básico | 12 ticks / 0,6 s | 36 ticks / 1,8 s | 5 |
| Investida | 16 ticks / 0,8 s | 140 ticks / 7 s | 10, uma vez por vítima |
| Soco duplo no chão | 24 ticks / 1,2 s | 200 ticks / 10 s | 16 no centro → 4 na borda, raio de 5 blocos |

Tempos em 20 TPS. A investida fixa a direção na preparação, percorre até 18 passos de 0,85 bloco, respeita paredes/apoio e varre inclusive o deslocamento final. A IA escolhe a investida à distância e o soco duplo de perto; continua usando ataques básicos durante as recargas dos especiais. O slam avisa a área com partículas, aplica dano na altura do chão, respeita paredes e não destrói blocos. O centro fica 2,5 blocos à frente do boss.

Estado e tick do golpe são sincronizados; dano só é aplicado no servidor. Morte, AI desativada, perda/troca de alvo e troca de dimensão do alvo cancelam o golpe pendente. As três recargas persistem no NBT; recarregar a entidade não restaura um acerto pendente nem permite burlar a recarga.

## Validação

Ambiente: Windows, Java 17.0.20.1, Gradle 8.8, Minecraft 1.20.1, Forge 47.4.10, GeckoLib já instalado, PlayerAnimator 1.0.2-rc1. Dependências resolvidas em cache com `--offline`; nenhum download/instalação adicional.

| Etapa | Resultado |
|---|---|
| Baseline `check` | PASS, 127 regressões |
| Baseline `runGameTestServer` | PASS, 169 testes obrigatórios, exit 0 |
| `build` final (inclui `check`) | PASS, 127 regressões, exit 0, 20 s |
| `check` após implementação | PASS, 127 regressões, exit 0 |
| Suíte isolada com os primeiros 15 casos do boss | PASS, 184 obrigatórios, exit 0 |
| Suíte final com teste do último segmento da investida | PASS, 185 obrigatórios, exit 0, 55 s |
| Ajuste para 100 HP / dano básico 5: `build` | PASS, 127 regressões, exit 0, 26 s |
| Ajuste para 100 HP / dano básico 5: `runGameTestServer` | PASS, 185 obrigatórios, exit 0, 50 s |
| `python tools/models/hollow_boss.py --check` | PASS, fontes e exports idênticos, bones/UVs/recovery/altura válidos |
| `python tools/models/hollow_variants.py --check` | PASS, três Hollows existentes intactos |

Os 16 testes novos verificam dimensões/identidade, contato do ataque básico, investida real, acerto único, último segmento, parede, recarga exata aos 140 ticks, persistência, dano radial, oclusão/altura, ausência de destruição, IA natural e cancelamentos por morte/noAI/perda/troca de alvo. A validação de identidade também confirma que uma instância nova nasce com vida máxima e vida atual em 100, além de dano básico exatamente 5.

Duas rodadas anteriores no diretório compartilhado `run-gametest/base` tiveram uma falha em `shibariIsRequiredAndHiasobiConsumesItsNet` (os 15 testes do boss passaram). Corrigimos uma falha real de isolamento: os novos cenários agora restauram todos os blocos e limpam entidades também por `@AfterBatch`, inclusive após falha. A suíte passou integralmente no diretório novo `run-gametest/boss-remaster` e passou novamente com o caso adicional. A causa específica das duas falhas antigas não foi isolada entre terreno residual e ordem/posição de batches. Nenhum teste ou código de técnica da Benihime foi alterado para forçar sucesso. Logs das tentativas foram preservados.

## Reprodução

Com o Java 17 e as opções locais do launcher já configurados:

```powershell
./gradlew.bat --offline check build --console=plain
./gradlew.bat --offline -PbossQa=true runGameTestServer --console=plain
./gradlew.bat --offline -PbossQa=true -PbossQaExit=true runClient --console=plain
python tools/models/hollow_boss.py --check
```

O cliente QA usa `run/boss-qa/base`, cria um save novo `Boss-QA-<timestamp>` e captura o framebuffer real. Não abre mundos existentes. Sem `bossQaExit`, permanece aberto após a sequência. Preparação/captura são bloqueadas em produção; não há comando de debug liberado em release.

Para conferir manualmente em mundo com cheats: `/summon bleachmod:hollow_boss ~ ~ ~`. O boss existente da missão mantém o mesmo ID.

## Escopo de homologação

Servidor GameTest dedicado: PASS. Cliente real singleplayer, perfil base: capturado, detalhes e medidas finais abaixo. Dois clientes remotos/observador: NÃO EXECUTADO. Perfil Photon/LDLib: NÃO EXECUTADO (não é dependência obrigatória da feature). Noite, terreno acidentado e FPS em combate de vários bosses não foram medidos; não presumir esses resultados a partir da arena plana diurna.

## Cliente final e evidências reais

Execução final: `Boss-QA-1792524068943`, em `run/boss-qa/base/boss-evidence/`. Cliente Forge real, exit 0, 1m23s. Arena plana de dia, perfil base sem Photon, GPU observada AMD Radeon RX 7600. Os frames capturados registraram 117–119 FPS; isso é uma amostra dessa arena, não um benchmark geral.

Capturas intactas e GIFs em `evidence/`; metadados em `frames.jsonl`, trace independente do servidor em `combat.jsonl`, resumo em `combat-summary.json`. Os GIFs são montados dos PNGs reais com duração obtida dos ticks registrados; não usam interpolação sintética. PNGs completos das sequências permanecem no diretório original do mundo QA.

O trace visual original registra dano básico de 8 porque foi capturado antes do ajuste final de balanceamento. O valor atual de 5 é verificado pelos GameTests e pelo build final deste ajuste. A investida permanece com dano 10, e o slam registrou 15,28 contra a vítima a 0,3 bloco do centro (500 → 484,72), coerente com a queda linear de 16 para 4. A investida percorreu 15,3 blocos na arena livre. O teste dedicado cobre o contato que só ocorre nesse último passo.

Sensores dos dois punhos foram medidos com matrizes reais do GeckoLib, ativados somente pelo perfil de desenvolvimento. No frame `slam-655.png`, tick sincronizado 24, ambos registraram Y=-60,01585, com chão em Y=-60. Nos ticks 23–27, a diferença máxima de ambos para o plano do chão foi menor que 0,04 bloco. O ponto médio horizontal dos punhos no tick 24 ficou em X=0,5 / Z=3,0699, frente ao centro de dano X=0,5 / Z=3,0. Valores brutos preservados em `contact-measurements.json`.

| Aspecto observado no cenário | Nota 0–5 | Evidência / limite |
|---|---:|---|
| Escala e silhueta | 4 | Front e detalhe com NPC Ichigo de 1,8 bloco à mesma profundidade; corpo 3× mais alto |
| Máscara e marcas | 4 | Olhos amarelos, sorriso/dentes, marcas vermelhas e furo realmente vazado |
| Costas e braços de apoio | 4 | Lateral e traseira mostram corcunda, barbatana e braços até o chão |
| Soco duplo / contato | 4 | Preparação acima da cabeça, agachamento e dois punhos medidos junto ao chão; penetração residual <0,04 bloco |
| Legibilidade dos golpes | 4 | GIFs de melee/charge/slam e dano confirmado pelo servidor |
| Caminhada | 3 | Apoio alternado dos braços e articulação das pernas; avaliação restrita ao chão plano |

A revisão intermediária `review2/` foi mantida para comparação; nela os sensores mostraram os punhos aproximadamente 0,55 bloco acima do chão. A versão final ajusta o agachamento com compensação nas pernas e antecipa o contato visual para acompanhar a entrega do estado ao cliente. O teleporte entre cenários é preparado antes da captura para não confundir a interpolação de reposicionamento com a animação do golpe.

Prévia final: [modelo e escala](evidence/04-mask-detail.png), [lateral](evidence/02-side-hunch-arms.png), [costas](evidence/03-rear-dorsal-fin.png), [soco duplo](evidence/slam.gif), [investida](evidence/charge.gif), [básico](evidence/melee.gif), [caminhada](evidence/walk.gif).

JAR final: `build/libs/bleachmod-0.2.0.jar` (15314255 bytes). SHA-256: `c244069d33aff6389de7b5549e80457651a02d30f39272f31256148c672a5c89`. Os três recursos do boss foram comparados byte a byte entre fonte e JAR reobfuscado: PASS.

Estado final do workspace registrado em `git-status-after.txt` e resumo dos arquivos já versionados em `diff-stat.txt` (arquivos novos listados no status). Logs gerados na raiz foram limpos/restaurados ao estado inicial; nenhum commit foi criado.
