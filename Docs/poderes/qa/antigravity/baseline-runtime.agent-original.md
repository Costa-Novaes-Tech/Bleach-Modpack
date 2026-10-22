# Baseline de Execução — Checkout Design (`Bleach-Modpack-design`)

**Data**: 22/10/2026  
**Workspace**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`  
**Branch**: `codex/design-poderes-criaturas` (ahead 1 em relação à origin, divergência histórica 7 commits Feature / 1 design em relação ao checkout do Codex)  
**HEAD**: `830ad64`  
**JDK**: `C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot` (Java 17.0.20.1+1)  
**Launcher oficial**: `tools/antigravity/Invoke-Design.ps1`  

---

## 1. Baseline Gradle Check (`check build`)

### Tentativa 1 (Interrompida)
- **Log**: [`Check-20261022-143614.log`](file:///c:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/Check-20261022-143614.log)
- **Exit Code**: Interrompido por erro de script
- **Causa**: No PowerShell 5.1 com `$ErrorActionPreference = 'Stop'`, o redirecionamento `*> $log` converteu mensagens informativas de stderr do javac/Mixin processor (`Note: SpongePowered MIXIN Annotation Processor Version=0.8.5`) em `NativeCommandError`, abortando o script prematuramente durante a fase `:compileJava`.
- **Ação corretiva**: O Codex atualizou `Invoke-Design.ps1` para disparar o Gradle via `cmd.exe /c` com redirecionamentos nativos separados (`-RedirectStandardOutput $log` e `-RedirectStandardError "$log.stderr.log"`), preservando o exit code real sem alterar código do mod.

### Tentativa 2 (Aprovada)
- **Log**: [`Check-20261022-144127.log`](file:///c:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/Check-20261022-144127.log)
- **Stderr**: [`Check-20261022-144127.log.stderr.log`](file:///c:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/Check-20261022-144127.log.stderr.log) (0 bytes)
- **Exit Code**: `0` (BUILD SUCCESSFUL em 45s)
- **Tarefas executadas**: 14 tarefas acionáveis (8 executadas, 6 up-to-date)
- **Suíte de Regressão**: **137 cenários executados — 137 APROVADOS (0 falhas)**
  - Guard / Posture / Stamina: 13 cenários PASS
  - Nozarashi / PlayerAnimator: 2 cenários PASS
  - Benihime / Fencing / Voice / Balance / Higashi: 31 cenários PASS
  - Hyorinmaru / Ice Release / Asset Bench: 14 cenários PASS
  - Asauchi / Mastery / Progression: 7 cenários PASS
  - Cooldowns / Geometry / Bounding / Raycasts: 18 cenários PASS
  - Attributes / HUD / GUI Layouts / Menus: 15 cenários PASS
  - Senbonzakura / Visuals / Morph: 16 cenários PASS
  - Poses / Releases / Gestures / Animation Leases: 15 cenários PASS
  - History Mode / Appearance / Skin Composition / Schemas: 6 cenários PASS

---

## 2. Validação de Cliente (`ClientQa`)

Comando executado:
```powershell
Invoke-Design.ps1 -Mode ClientQa
# Flags aplicadas: -PbenihimeQa=true -PhyorinmaruQa=true -PqaAutoExit=true runClient
```

### Tentativa 1 (Sem Janela / Hidden WindowStyle)
- **Log**: [`ClientQa-20261022-144323.log`](file:///c:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/ClientQa-20261022-144323.log)
- **Exit Code**: `-1`
- **Diagnóstico**: O launcher utilizava `WindowStyle Hidden` para o processo `cmd.exe`. O Forge inicializou completamente os registros, OpenAL e atlases de textura até `textures/atlas/mob_effects.png-atlas` (14:44:56), porém ficou sem janela visível (`MainWindowHandle = 0`). O processo 7116 foi encerrado operacionalmente. Nenhum save pessoal foi acessado.

### Tentativa 2 (WindowStyle Normal / Limitação do Host Antigravity)
- **Log**: [`ClientQa-20261022-144918.log`](file:///c:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/ClientQa-20261022-144918.log)
- **Stderr**: [`ClientQa-20261022-144918.log.stderr.log`](file:///c:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/ClientQa-20261022-144918.log.stderr.log)
- **Exit Code**: `1` (Process finished with non-zero exit value `-1`)
- **Comportamento observado**:
  1. Forge carregou recursos, sound engine OpenAL e atlases de textura normalmente até `[Render thread/INFO] [minecraft/TextureAtlas]: Created: 128x128x0 minecraft:textures/atlas/mob_effects.png-atlas` (14:50:29).
  2. Em seguida, a criação da superfície de renderização gráfica GLFW da janela principal do Minecraft não concluiu o ciclo até a tela de título (`TitleScreen`), impedindo que o gatilho `BenihimeQaBootstrap` / `HyorinmaruQaSequence` entrasse em ação para gerar o mundo descartável e as capturas automáticas.
  3. **Limitação real registrada**: Processos filhos gráficos OpenGL/GLFW iniciados a partir do ambiente de background task do Antigravity no Windows carecem de sessão interativa de desktop com display swapchain ativo. Conforme instrução expressa, não se inventa homologação in-game: o client completo requer execução manual pelo usuário ou terminal interativo de desktop.

---

## 3. Estado dos Artefatos e Isolamento

1. **Checkout Codex**: Intacto. Nenhuma alteração foi realizada fora do checkout `Bleach-Modpack-design`.
2. **Branch**: `codex/design-poderes-criaturas`. Sem commit, push, merge ou rebase automáticos.
3. **Código de Produção do Mod**: Intacto. Nenhum código Java de gameplay, Zaraki ou produção foi alterado para mascarar falhas.
4. **Laboratório Blockbench**: O modelo de ensaio [ag_hydra_jaw.bbmodel](file:///c:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/tools/models/lab/antigravity/ag_hydra_jaw.bbmodel) permanece restrito a `tools/models/lab/antigravity` e **não foi exportado nem integrado** à árvore do mod ou ao Minecraft.
