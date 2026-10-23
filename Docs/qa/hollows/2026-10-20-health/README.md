# Balanceamento de vida dos Hollows básicos — 20/10/2026

Esta revisão reduz a vida máxima das três variantes do mesmo mob `bleachmod:hollow`.
Ela complementa a [modelagem inicial](../2026-10-20/README.md), a
[remasterização](../2026-10-20-remaster/README.md) e o
[ciclo de combate aéreo](../2026-10-20-aerial/README.md). Esses relatórios
descrevem os valores históricos de seus respectivos ciclos; a tabela abaixo é
o balanceamento atual.

| Variante | Vida anterior | Vida atual | Corações atuais |
|---|---:|---:|---:|
| Tank (`brute`) | 80 HP | **20 HP** | 10 |
| Pequeno (`swift`) | 24 HP | **6 HP** | 3 |
| Voador (`flying`) | 40 HP | **14 HP** | 7 |

Os três valores estão em `HollowVariant.java`, aplicados pelo `HollowEntity`
ao nascer e ao carregar um mundo salvo. O atributo padrão do Hollow comum
também é 20 HP, correspondente ao tank antes da escolha da variante. Um Hollow
salvo com vida acima do novo máximo é limitado ao máximo da própria variante ao
recarregar; um Hollow já ferido conserva a vida abaixo desse máximo. Os danos,
intervalos de ataque, movimento, modelos e spawn compartilhado continuam com os
valores do ciclo anterior. O boss usa seu próprio atributo de 50 HP.

## Verificação

O GameTest `variantsHaveDistinctCombatStatsAndRealHitboxes` verifica os máximos
**20 / 6 / 14** e a vida cheia das três variantes. O teste de salvamento foi
ajustado para testar cada variante ferida dentro do novo limite e uma tag antiga
com 80 HP, que deve ser limitada no carregamento. O cenário de spawn existente
verifica que as três variantes nascem com vida cheia. A captura de QA registra
`health` e `maxHealth` a partir das entidades no servidor do cliente Minecraft.

- `build` em Java 17 / Forge 47.4.10 / Minecraft 1.20.1, modo offline:
  **PASS**, incluindo `check` e **127 cenários de regressão**.
- `runGameTestServer`: **185 testes iniciados; 1 falha** repetida no teste
  `shibariisrequiredandhiasobiconsumesitsnet` (`BenihimeGameTests`, alvo Zombie),
  por “Net damaged or failed to bind”. Todos os GameTests dos Hollows passaram.
  O teste da Shibari foi mantido ativo. As duas saídas completas foram guardadas
  em [logs](logs/); esta falha não é atribuída à mudança de vida.
- Cliente Forge real, perfil base: **PASS, exit 0**. O trace confirmou
  **20/20**, **6/6** e **14/14 HP** para tank, pequeno e voador no mundo de QA.
- `git diff --check`: PASS.

O cenário visual usa o save descartável `Hollow-QA-1792533694457`, criado pelo
laboratório opt-in em `run/hollow-qa/aerial`, com luz diurna e terreno plano.
A vida foi medida no [trace do servidor integrado](evidence/combat.jsonl):
130 amostras do tank em 20/20, 120 do pequeno em 6/6 e 400 do voador em 14/14.
Os [metadados dos 577 frames reais](evidence/frames.jsonl) registram o framebuffer
do Minecraft e a GPU AMD Radeon RX 7600. Há uma seleção de PNGs intactos do
[grupo de frente](evidence/01-front.png), do [voador em detalhe](evidence/03-flight-detail.png)
e do [pequeno em combate](evidence/swift-700.png). Os PNGs originais completos
permanecem em `run/hollow-qa/aerial/hollow-evidence/Hollow-QA-1792533694457/screenshots/`.
A captura comprova spawn, aparência e valores de servidor; não mediu tempo
para derrotar cada Hollow sob dano de jogador. As amostras de FPS dessa arena
não representam um benchmark de carga.

Logs completos: [build](logs/build.txt), [GameTests final](logs/gametest-final.txt),
[primeira rodada de GameTests](logs/gametest-first-run.txt) e
[cliente](logs/client.txt).

## Reprodução e estado

```powershell
./gradlew.bat --offline build --console=plain
./gradlew.bat --offline runGameTestServer --console=plain
./gradlew.bat --offline -PhollowQa=true -PhollowQaExit=true runClient --console=plain
```

Nesta estação foi usado o launcher local ignorado `build/hollow-gradle.ps1`,
que seleciona Java 17 e reutiliza o ajuste de socket Windows em caminho ASCII.
Não houve instalação adicional. Branch no início:
`feat/Jigui/new-HollowBoss-model`, HEAD `45d790bcc49946f310127efbc93bcd5fb89eef87`.
Durante este ciclo, alterações paralelas já presentes do boss foram confirmadas
separadamente; esta revisão mudou apenas `HollowVariant.java`, o atributo base
em `HollowEntity.java`, os testes de variantes e o trace de QA. Sem commit ou push.

JAR: `build/libs/bleachmod-0.2.0.jar`.
SHA-256: `B31F9F4DA987B33BA8D696954B6BF6BD9415A3819CFE1536AB366718350E9779`.

Limites desta verificação: cliente integrado em perfil base e servidor dedicado
GameTest. Dois clientes remotos, Photon, noite, terreno irregular e carga com
muitos Hollows não foram repetidos para esta mudança numérica.
