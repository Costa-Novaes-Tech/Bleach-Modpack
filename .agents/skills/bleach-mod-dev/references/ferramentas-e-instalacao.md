# Ferramentas — decisões de instalação no Windows

## Ambiente observado em 19/10/2026 (revalidar)
- Java Temurin 17.0.20.1, Gradle 8.8 via `gradlew.bat`, Node 22.18, Python 3.13, Git 2.50. JDK já atende projeto; Playwright/Blockbench/Blender/FFmpeg não encontrados em PATH/locais inspecionados.
- Minecraft 1.20.1 Forge 47.4.10, GeckoLib 4.x 4.8.4–<4.9, PlayerAnimator e Photon/LDLib opcionais descritos pela configuração local. Não atualizar bibliotecas só por haver uma versão mais nova.

## Prioridade de obtenção
| Ferramenta | Quando instalar | Fonte primária | Atenção |
|---|---|---|---|
| Gradle Wrapper | Já incluído | wrapper no repositório | Inspecionar jar/distributionUrl antes de rodar; downloads via Maven/Gradle |
| Blockbench | Modelagem/edição de Benihime, fonte `.bbmodel` | https://www.blockbench.net/ | Versão/OS; guardar fonte editável fora de `build` |
| Plugin GeckoLib para Blockbench | Para exportar `.geo.json` e animação | Plugin browser dentro do Blockbench; https://github.com/bernie-g/geckolib/wiki/Making-Your-Models-%28Blockbench%29 | Escolher formato GeckoLib correto e verificar compatibilidade com 4.x |
| FFmpeg/ffprobe | Inspeção de MP4, quadros, recortes e conversões | https://ffmpeg.org/download.html | Windows binários fornecidos por links oficiais; verificar distribuição/licença antes de instalar |
| OBS Studio | Gravar vídeo real do Minecraft e análise temporal | https://obsproject.com/ | Começar com perfil leve; nunca presumir GPU sobrando |
| Audacity | Se necessário para voz/áudio da cinematics | https://www.audacityteam.org/ | Opcional após teste de áudio in-game |
| Blender | Só se Blockbench for insuficiente | https://www.blender.org/ | Não exportar para GeckoLib presumindo conversão automática |
| Playwright | Só com visualizador web real implementado | https://playwright.dev/ | Não testa renderizador Java nem GUI nativa Minecraft |

## Protocolo
1. `Get-Command`, `where.exe` e versionamento para identificar binários existentes; não concluir ausência absoluta apenas porque não aparecem no PATH.
2. Ler README, LICENSE, notas de release, arquitetura e requisitos dos links oficiais. Confirmar se pacote é freeware/open source e compatível com Windows e versões fixadas.
3. Escolher um instalador ou versão portátil verificável; observar permissões, hash e eventual reinicialização. Se exigir administrador, alterar PATH global, aceitar licença especial ou criar custos/contas, pedir confirmação.
4. Registrar URL real, versão, data, hash/assinatura quando disponível, pasta, comando e resultado da instalação; jamais baixar de mirror não verificado.
5. Verificar função com teste leve. Não usar `Invoke-Expression` com script web remoto, `curl | powershell`, `winget ... --accept-*` sem inspeção, nem instalar extensões desconhecidas silenciosamente.
6. Dependências de Gradle referentes ao projeto podem ser resolvidas pelo wrapper durante build autorizado; documentar os downloads.

## Comandos de diagnóstico sugeridos (somente após inspeção)
No PowerShell na raiz do projeto:
```powershell
Get-Command java, javac, git, node, python, ffmpeg, ffprobe, obs64 -ErrorAction SilentlyContinue
java -version
.\gradlew.bat --version
```

Tarefas existentes (não são equivalentes):
```powershell
.\gradlew.bat regressionTest --console=plain
.\gradlew.bat check --console=plain
.\gradlew.bat runGameTestServer --console=plain
.\gradlew.bat runClient --console=plain
```
`runClient` requer GUI; `runGameTestServer` mede testes GameTest no servidor, não a imagem. Verificar `build.gradle` e separar diretórios/perfis de testes. Não executar em paralelo no mesmo diretório de run.

## Limites da automação visual
- Screenshot de Minecraft Java é F2 (e F1 oculta HUD); originais ficam na pasta de screenshots do diretório do jogo **efetivamente usado pelo cliente de desenvolvimento**, que pode não ser `%APPDATA%\.minecraft`.
- Gravação do movimento real: OBS ou gravador nativo Windows, com consentimento para captura. Em alternativa, um comando/hook dev-only de screenshot do cliente pode produzir marcos após verificar a API específica do projeto.
- Um web viewer + Playwright compara previews, não comprova shader, mixin, câmera ou interpolação no Forge real.

## Referências
- Forge GameTest: https://docs.minecraftforge.net/en/1.20.x/misc/gametest/
- GeckoLib Blockbench: https://github.com/bernie-g/geckolib/wiki/Making-Your-Models-%28Blockbench%29
- GeckoLib 4 modelos: https://github.com/bernie-g/geckolib/wiki/Geo-Models-%28Geckolib4%29
- Captura oficial Minecraft: https://help.minecraft.net/hc/en-us/articles/40719065932557-Take-and-Manage-Screenshots-in-Minecraft-Java-Edition
- Playwright: https://playwright.dev/docs/intro
