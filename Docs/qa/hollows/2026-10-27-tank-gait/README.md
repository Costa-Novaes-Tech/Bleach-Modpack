# Hollow tank: braços e cotovelos durante a caminhada

2026-10-27, branch `main`, referência `84738aeff63a04a6cc007fe7f1b2bdac86397d90`.
Continuação da correção dos ombros; as alterações anteriores estavam presentes
no início deste ciclo. Nenhum checkout ou commit foi feito.

## Correção

A primeira tentativa colocou +18° nos braços, somados aos +11° do tórax.
Isso levava os cotovelos para trás. Os antebraços ainda tinham +25° na mesma
direção, ampliando o problema. As três referências de caminhada fornecidas
pelo usuário orientaram a nova pose: braços pendentes, pequena flexão dos
cotovelos para a frente e alternância com a perna contrária.

- Braços: -11° em X para compensar o tórax; abertura lateral de 6° para
  acomodar as mãos grandes ao lado dos quadris.
- Cotovelos: -12° na pose base; caminhada entre -18° e -10°, sempre com
  flexão para a frente. Punhos relaxados em +4°, com oscilação pequena.
- Caminhada: balanço de 14° dos braços, oposto ao da perna do mesmo lado,
  centrado sob os ombros. Mantidos o ciclo de 1,25 s e o movimento pesado
  do tronco, quadris e pernas do Hollow.

Gerador, geometria GeckoLib, fonte Blockbench e animação exportada estão
sincronizados. Não foram alterados atributos, IA, hitboxes ou clips das
outras variantes. O clip de ataque permanece igual e herda a nova pose base.

## Validação

- Baseline e final: `check`, **146 regressões aprovadas**.
- `build`: aprovado, com JDK Temurin 17.0.20.1, Forge 47.4.10 e Minecraft 1.20.1.
- `runGameTestServer`: **223 testes obrigatórios aprovados**, no diretório
  descartável `run-gametest/base`.
- Validador existente do gerador: três rigs e 12 clips aprovados, incluindo
  exports, fontes, texturas, bones, UVs, loops e recuperação. Como no ciclo
  anterior, CRLF foi normalizado somente em memória para a comparação com LF.
- Checagem cinemática: 251 amostras por braço usando as convenções de
  transformação do GeckoLib local. Cotovelos entre -0,204 e +0,219 blocos
  em Z em relação aos ombros; flexão sempre para a frente; balanço dos
  braços sempre oposto ao das pernas. Esses valores são calculados, não
  medições de pixels das capturas.
- Cliente real: caminhada registrada de perfil, frente e costas; ataque,
  contato no golem e recuperação inspecionados em uma segunda execução.
  Capturas via `Minecraft Screenshot.grab`, sem imagens sintéticas.
- `git diff --check`: aprovado.

O acesso ao cache Gradle exigiu execução fora do sandbox após bloqueio do
arquivo `.lck`. Nenhuma dependência foi instalada; execução com `--offline`.

## Evidências

- [Perfil — 6 s, 60 frames](previews/gait-side.gif)
- [Frente — 7,5 s, 75 frames](previews/gait-front.gif)
- [Costas — 7,1 s, 71 frames](previews/gait-rear.gif)
- PNGs originais selecionados em `screenshots/`; metadados em
  `gait-frames.jsonl` e deslocamento real do servidor em `gait-motion.jsonl`.
- Logs completos em `logs/`.

Os GIFs são montagens dos framebuffers reais, com tempos derivados dos ticks,
sem interpolação. Originais em:

```
run/hollow-qa/aerial/hollow-evidence/Hollow-QA-1793143248391
run/hollow-qa/aerial/hollow-evidence/Hollow-QA-1793143394772
```

Foram capturados 206 frames da caminhada; deslocamento real de 39,74 blocos
no percurso registrado. Perfil base, AMD Radeon RX 7600, 1280 × 720,
distância de renderização 6. FPS observado nas capturas: 117–119, sem
pretensão de benchmark. Inspeção: cotovelos próximos ao corpo, antebraços
flexionados para a frente e mãos sem atravessar os quadris nos marcos
inspecionados. A malha continua com a silhueta pesada e cúbica do tank.

Multiplayer com dois clientes, Photon e terrenos irregulares não foram
avaliados neste ciclo.

## Reprodução

O novo `hollowGaitQa` só atua dentro do laboratório existente de desenvolvimento,
em um mundo novo com prefixo `Hollow-QA-`; não afeta partidas normais.

```powershell
$env:JAVA_HOME = 'C:/Users/jguic/.jdks/temurin-17.0.20.1'
./gradlew.bat --offline check build --console=plain
./gradlew.bat --offline -PhollowQa=true -PhollowGaitQa=true -PhollowQaExit=true runClient --console=plain
./gradlew.bat --offline runGameTestServer --console=plain
./gradlew.bat --offline -PhollowQa=true -PhollowQaExit=true runClient --console=plain
```

JAR: `build/libs/bleachmod-0.2.0.jar`.
SHA-256: `2D9519FD5EC562CB053C7E1967D92BEFEEB1EDBC8BD5F020723EB27972BFB123`.
