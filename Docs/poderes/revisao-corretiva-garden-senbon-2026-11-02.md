# Revisão Corretiva — Garden Senbonzakura (02/11/2026)

Documento de auditoria técnica e evidências de entrega da revisão de 02/11/2026, respondendo às lacunas apontadas na auditoria do Codex sobre o commit `8543d6d`.

---

## 1. Ambiente, Nuvem e Estado do Git

- **Branch exclusiva**: `codex/senbon-garden-prisao`
- **Checkout utilizado**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- **Base remota**: `origin/Feature-Poderes-bankais` @ `04ef0d1` (ancestralidade confirmada com `git merge-base --is-ancestor origin/Feature-Poderes-bankais HEAD`, exit code 0).
- **Disponibilidade de Nuvem**:
  - Verificado: O ambiente Antigravity nesta sessão local não possui agente ou checkout em nuvem exposto via MCP/ferramentas.
  - Conforme instrução, foi utilizado estritamente o checkout local dedicado `Bleach-Modpack-design`, confirmando que estava livre e sem disputa de arquivos com outras frentes.
- **Preservação de Branches Concorrentes e Worktrees**:
  - `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack` mantém `Feature-Poderes-bankais` @ `04ef0d1` com notas locais do Codex (`00-CONTINUIDADE-CODEX.md` e `continuidade-quincy-senbon-xerj-2026-10-31.md`). Essas notas foram preservadas e nenhum arquivo compartilhado foi sobrescrito.
  - `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime` mantém `codex/quincy-rig-manifestacao` @ `8228ab1`.
  - Nenhuma operação destrutiva (`git add .`, reset, clean, force-push ou stash genérico) foi utilizada.

---

## 2. Resolução das Lacunas Técnicas

### Lacuna 1: Medição e Validação Exata do Fixture no Teste do Teto de 16
- **Arquivo**: `src/main/java/com/bleachmod/gametest/SenbonGameTests.java` (`gardenPrisonCapturesUpToSixteenTargetsAndSortsByProximity`).
- **Problema anterior**: O teste anterior apenas validava `delta > 0` nos primeiros 16 alvos e zero nos demais.
- **Correção aplicada**:
  1. Todos os 20 alvos e o alvo ocluído tiveram suas armaduras e capacetes removidos (`ARMOR = 0`, capacete vazio) para garantir o comportamento nominal do fixture desarmurado.
  2. Baseline registrado logo antes da reativação do bloom (`beforeBloom` no tick 44).
  3. O teste foi estendido para 140 ticks (delay de verificação em tick 125, após os 3 pulsos da prisão disparados em ticks 81, 101 e 121).
  4. Validação estrita por alvo:
     - Cada um dos 16 alvos capturados (índices 0..15) sofre exatamente **21.0 net HP** de dano na 2ª fase: 5.0 HP base no bloom burst (7.5 net) + 3 pulsos de 3.0 HP base (4.5 net cada) = 14.0 HP base * 1.5 multiplicador de Bankai = 21.0 net HP (`Math.abs(delta - 21.0F) < 0.01F`).
     - Alvos além do teto (índices 16..19) sofrem rigorosamente **0.0 net HP** (delta == 0.0F).
     - Alvo ocluído por parede de pedra sólida sofre rigorosamente **0.0 net HP** (wallDelta == 0.0F).

### Lacuna 2: Contenção Vertical Contínua de Alvos com Levitação Externa Forte e Longa
- **Arquivo**: `src/main/java/com/bleachmod/common/technique/SenbonzakuraTechniqueService.java` (`tickGardenPrison`).
- **Problema anterior**: Alvos com levitação externa forte (`amplifier > 0`) ou longa (`duration > 4`) entravam em um bypass que não monitorava a subida nem aplicava a contenção contra o teto e o teto da habilidade (`floorY + 2.5m`).
- **Correção aplicada**:
  1. Preservação estrita dos efeitos externos: instâncias externas de `MobEffects.LEVITATION` não são removidas, nem substituídas, nem rebaixadas em amplificador ou duração.
  2. Aplicação universal de contenção: todos os alvos válidos na prisão recebem contenção vertical.
  3. Para alvos com levitação externa ativa, uma contenção antecipada (`targetThresholdY = maxAllowedY - 0.12`) neutraliza o impulso discreto do `LivingEntity.travel()` da física vanilla da Levitação, impedindo que a entidade ultrapasse `floorY + 2.5m` durante a integração de tick.
  4. Bloqueio contínuo contra teto sólido: detecta colisão de cabeça (`aboveHead.isSolid()`) ou colisão iminente com a bounding box; anula $V_y \le 0$ e mantém o alvo amortecido com Slow Falling + Slowness sem atravessar blocos sólidos.
- **Validação no GameTest**:
  - `gardenPrisonContinuousHeightCeilingAndExternalLevitation` ampliado com:
    - Alvo aberto: ascensão $\le 2.55\text{m}$ (real observada: 2.45m), sem levitação residual após a expiração.
    - Alvo sob teto sólido: zero penetração na bounding box do bloco de pedra.
    - Alvo com Levitação forte (amplificador 1, duração 200 ticks): ascensão monitorada a cada tick mantida estritamente $\le 2.55\text{m}$ (pico observado: 2.48m > 0.3m); amplificador 1 preservado; efeito ativo ao final.
    - Alvo com Levitação longa (amplificador 0, duração 250 ticks): ascensão mantida $\le 2.55\text{m}$; duração preservada sem truncamento; efeito ativo ao final.
    - Alvo com Levitação forte sob teto sólido (`strongCeilingTarget`): zero penetração na bounding box do teto; amplificador 1 preservado; efeito ativo ao final.

### Lacuna 3: Evidência Visual do Garden no Cliente Forge
- **Arquivos**:
  - `build.gradle` (exposição da propriedade `bleachmod.senbonGardenQa`).
  - `src/main/java/com/bleachmod/client/cinematic/SenbonzakuraGardenQaSequence.java` (sequência autoritativa de QA visual in-game).
  - `Docs/poderes/qa/garden-senbon-20261102/` (pacote de evidências com 12 frames, manifest com SHA-256, contact sheet e GIF contínuo).
- **Metodologia de QA**:
  - Execução real no cliente Forge via `./gradlew runClient -PbenihimeQa=true -PsenbonGardenQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain`.
  - Mundo plano descartável (`Benihime-QA-68-...`), horário do meio-dia, céu limpo, sem comandos simulados ou preview arbitrário.
  - Cast inicial no tick 0 -> reativação de bloom no tick 45 -> formação floral no tick 61 -> contenção com envoltório e subida limitada -> 3 pulsos -> dissipação e fade-out monotônico do piso.
  - Captura de 12 frames representativos (ticks 10, 25, 45, 55, 62, 70, 82, 102, 122, 140, 160, 180) com close frontal e visão geral.
  - Geração de composite contact sheet (`garden-senbon-contact.jpg`, SHA-256: `09a1dbb8a5c26e1a...`) e animação contínua (`garden-senbon-sequence.gif`, SHA-256: `7a1f7384132d8034...`).

---

## 3. Resultados dos Comandos Executados

| Etapa | Comando Executado | Duração | Exit Code | Resultado |
|---|---|---|---|---|
| Compilação Java | `./gradlew compileJava --offline --console=plain` | 53s | 0 | BUILD SUCCESSFUL |
| Testes de Regressão | `./gradlew check --console=plain` | 1m 02s | 0 | BUILD SUCCESSFUL (158/158 PASS) |
| Servidor GameTests | `./gradlew runGameTestServer --console=plain` | 4m 04s | 0 | BUILD SUCCESSFUL (284/284 PASS) |
| Cliente Forge QA | `./gradlew runClient -PbenihimeQa=true -PsenbonGardenQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain` | 1m 50s | 0 | BUILD SUCCESSFUL (12 frames capturados) |

---

## 4. Manifesto de Evidências Visuais (`manifest.json`)

- **Diretório**: `Docs/poderes/qa/garden-senbon-20261102/`
- **Contact Sheet**: `garden-senbon-contact.jpg` (SHA-256: `09a1dbb8a5c26e1a26d7f02bbf955aa8f04746a5d7c50a0ca979d39ca2576ca6`)
- **Sequência Animada**: `garden-senbon-sequence.gif` (SHA-256: `7a1f7384132d8034177eb0c874e0d9b6a9e10ea8f9f6e5ae10103738096f9bf1`)
- **Tabela de Frames**:
  - `frame-010.png` (tick 10): Cast inicial Garden N - expansão do tapete de pétalas (raio 35). SHA-256: `777ef5f60b48ba5d01276da8cc0131f70dcfa3a4ab4a0bffe789f72abbb3ebd8`
  - `frame-025.png` (tick 25): Tapete interno ativo e primeira explosão de pétalas (6 HP base). SHA-256: `835023f5178374a21bc8570d148eb39e84238b8368bda09453498c7ee60d59b8`
  - `frame-045.png` (tick 45): Reativação commitada dentro da janela (`bloom: 1`). SHA-256: `d8df269ef79b456d57e469f2d73b1fdd420e726b06de8818f1a1df92aaae4586`
  - `frame-055.png` (tick 55): Animação de formação e elevação da flor central. SHA-256: `40a841b31fe6c2a95220ca09dec779b0073b22c0af8fe26a23580a1ad44746e7`
  - `frame-062.png` (tick 62): 2ª explosão floral / burst 5 HP - inicialização da prisão (60 ticks, `gardenPrison: 60`). SHA-256: `1cc19a3e9f5693c943f4dd7388fab1bc60adb89e996f0c774bfeb5d05e807330`
  - `frame-070.png` (tick 70): Alvos capturados levitam até 2.5m sob envoltório espiral de pétalas. SHA-256: `ad34519e19fd68c2091a90a0deae2891ddc6bcf010a045e3c4c79a875c3589a3`
  - `frame-082.png` (tick 82): Pulso 1 de dano (3 HP base) e contenção vertical sob teto. SHA-256: `df7df57fe8f154fa31327f3ee7148ff0c25bb81358de94f5e5e297083b95a5fa`
  - `frame-102.png` (tick 102): Pulso 2 de dano (3 HP base) - close frontal dos alvos e teto. SHA-256: `519e9f41d3b5c4dded2c746afb50ccd9bd2b449646ef1a297fed8abc0067426d`
  - `frame-122.png` (tick 122): Pulso 3 de dano (3 HP base) - encerramento dos 60 ticks de prisão. SHA-256: `c6519cf544558a5d2df331dc265daaf83fd1a9f5087de82d20556bc0b412587c`
  - `frame-140.png` (tick 140): Dissipação da prisão e início do fade-out monotônico do piso. SHA-256: `13264b3c2954a1dfd9a60e0a55e2d6349942a445dbeeaee11e0a98f12ee7a469`
  - `frame-160.png` (tick 160): Fade-out suave (20 ticks) preservando o chão sem cortes secos. SHA-256: `645831518f883d6e53c4c810b48dbfa097a82987dc31be9582d929788d752be8`
  - `frame-180.png` (tick 180): Conclusão do ciclo de vida sem efeitos órfãos nem resíduos. SHA-256: `c8e034caea4083cf4b1df8ca0b1c0bc44e5904d0d6118d531a896d8e87e14dd5`

---

## 5. Relato de Integração e Impedimento de Checkout Local

Conforme determinado nas instruções de integração:
> *"Se a integração segura exigir operar no checkout `Feature-Poderes-bankais` já ativo em outro worktree, não tente trocar essa branch a partir de um segundo checkout local. Entregue commit/evidências e reporte o impedimento para o Codex concluir a integração. A autorização de merge não autoriza sobrescrever trabalho concorrente."*

- **Impedimento verificado**:
  - A branch alvo `Feature-Poderes-bankais` está com checkout ativo no worktree primário `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack`.
  - O Git impede tecnicamente qualquer tentativa de fazer `git checkout Feature-Poderes-bankais` no worktree de design (`fatal: 'Feature-Poderes-bankais' is already checked out at '...'`).
  - Além disso, o worktree primário do Codex possui arquivos documentais locais não comitados que devem ser rigorosamente preservados.
- **Estado pronto para integração**:
  - A branch `codex/senbon-garden-prisao` contém a história completa e ancestralidade direta sobre `origin/Feature-Poderes-bankais` (`04ef0d1`).
  - Todos os 158 testes de regressão e 284 GameTests autoritativos foram executados e aprovados com 100% de sucesso.
  - A evidência visual real do cliente Forge foi arquivada e verificada.
  - O merge na `Feature-Poderes-bankais` pode ser executado diretamente no checkout primário do Codex (`git merge --no-ff codex/senbon-garden-prisao`) de forma limpa e sem conflitos.
