# Hollow tank: orientação dos ombros

> Histórico da primeira tentativa. A inclinação de +18° foi rejeitada na
> revisão do usuário e substituída pelo ajuste de anatomia e caminhada em
> [2026-10-27-tank-gait/README.md](2026-10-27-tank-gait/README.md), validado no cliente real.

Data: 2026-10-27. Branch: `main` (sem checkout ou commit).
Referência: `84738aeff63a04a6cc007fe7f1b2bdac86397d90`.
Estado inicial: `git status --short` vazio.

A rotação X de repouso de `right_arm` e `left_arm` passou de -18° para
+18° no gerador `tools/models/hollow_variants.py`. Isso inverte a inclinação
dos ombros, levando seu topo para trás. Os exports `hollow_brute.geo.json`
e `hollow_brute.bbmodel` foram regenerados; somente essas duas rotações
mudaram em cada asset. Pivôs, hierarquia, UVs e clips permanecem iguais.
A nova pose base também é herdada pelos antebraços e mãos.

## Verificação

- Baseline: todos os assets correspondiam semanticamente ao gerador.
  O `--check` direto falhou por comparar bytes LF com arquivos CRLF do checkout.
- Final: o validador existente `main()` com `--check` passou (exit 0),
  normalizando CRLF para LF apenas nas leituras em memória. Conferidos os
  três rigs, fontes Blockbench, exports, texturas, UVs e 12 clips de animação,
  incluindo loops, recuperação e referências de bones.
- `git diff --check`: exit 0.
- `gradlew.bat check --console=plain`: exit 1 antes de executar Gradle,
  pois `JAVA_HOME` não está definido e Java não está no PATH.
  As instalações encontradas em `.jdks` são Temurin 21 e 25; o projeto
  exige JDK 17. Build, GameTests e cliente não foram executados.
- Uma tentativa de copiar os assets para a pasta temporária do sistema
  falhou por acesso negado. A validação final foi feita somente em memória,
  sem alterar o validador ou normalizar outros arquivos do repositório.

**PENDENTE DE VALIDAÇÃO VISUAL** no Minecraft. A captura histórica
`2026-10-20-remaster/screenshots/03-tank-detail.png` foi consultada apenas
como referência anterior; não é evidência desta correção.

Com JDK 17 configurado, validar no laboratório existente:
`gradlew.bat -PhollowQa=true -PhollowQaExit=true runClient --console=plain`.
Conferir os ombros em repouso, caminhada e ataque. O laboratório cria um
mundo novo e grava capturas em `run/hollow-qa/aerial/hollow-evidence/`.

Estado final esperado: três arquivos de implementação modificados e este
relatório novo. Nenhum JAR foi gerado neste ciclo.
