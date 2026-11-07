# Ryūjin v16 — playtest no cliente

Use uma instância Forge 1.20.1 separada ou uma cópia do perfil e do mundo. A prova de referência usou Java 17, Forge 47.4.10, GeckoLib 4.8.4, Better Combat 1.9.0, Player Animator 1.0.2-rc1 e Cloth Config 11.1.136. [Relatório técnico](ryujin-v16-acabamento-e-perfis-forge-2026-11-07.md).

## Testar pelo projeto com Gradle

O log `run/logs/latest.log` da sessão de 07/11 mostrou `forgeclientuserdev` e `Reloading ResourceManager: vanilla, mod_resources`. O `runClient` usa `sourceSets.main` e a pasta `run`. A chama do olho depende de `ryujinV14Pilot`. O sol contínuo exige também `ryujinContinuousSolar` e o marcador de atlas carregado pelo pack. Uma execução comum mantém essas opções desligadas.

Na raiz do projeto, abra o PowerShell e inicie o cliente de teste assim. As opções valem para essa execução.

```powershell
$ryujinPreviousJavaOptions = $env:JAVA_TOOL_OPTIONS
try {
    $env:JAVA_TOOL_OPTIONS = "$ryujinPreviousJavaOptions -Dbleachmod.ryujinV14Pilot=true -Dbleachmod.ryujinContinuousSolar=true"
    .\gradlew.bat --no-daemon runClient
} finally {
    $env:JAVA_TOOL_OPTIONS = $ryujinPreviousJavaOptions
}
```

Em **Opções → Pacotes de Recursos**, mova **RyujinSunV16Full** para o lado ativo com a seta da miniatura e clique em **Concluído**. O ZIP foi preparado em `run/resourcepacks`. Para testar o áudio, ative `RyujinAudio15Local` e `RyujinAudio15HeavyMusicLocal` e deixe **HeavyMusic acima de Audio15Local**. Use um mundo de teste ou cópia de um save. A chama do olho e o sol contínuo podem ser avaliados nesse perfil com as dependências existentes.

Se precisar reconstruir o pack, rode `python -B tools/models/lab/codex/ryujin01/build_solar_finish.py` com Pillow. Na pasta gerada `build/visual-lab/ryujin-v16-packs/RyujinSunV16Full`, copie `tools/models/lab/codex/ryujin01/ryujin_bankai_continuous_sun_v13_lab_glowmask.png` para `assets/bleachmod/textures/entity/ryujin_bankai_sun_glowmask.png`. Copie a pasta inteira para `run/resourcepacks` e ative no menu.

## Testar pelo launcher Forge

1. Em uma cópia da instância Forge 1.20.1, abra a pasta da instância pelo launcher. Feche o jogo e mova o JAR antigo do Bleach Mod para fora de `mods`. Copie `build/libs/bleachmod-0.2.0.jar` para `mods`. Esse é o nome padrão do build da branch. Para reconstruí-lo, rode `.\gradlew.bat check build --offline --console=plain` com o cache já preparado. O JAR histórico `bleachmod-0.2.0-v16-composite.jar` pertence à bancada anterior, com hash no relatório técnico.
2. Copie os três ZIPs de `run/resourcepacks` para `resourcepacks` da instância. Ative-os pelo menu na ordem indicada acima. Para testar olho e sol, adicione aos argumentos Java `-Dbleachmod.ryujinV14Pilot=true -Dbleachmod.ryujinContinuousSolar=true`.
3. Para repetir o perfil com iluminação do terreno, instale **Shimmer 0.3.0 Forge** em uma instância separada. Use estes argumentos Java para combinar o Shimmer com bloom e refração próprios.

```text
-Dbleachmod.ryujinV14Pilot=true -Dbleachmod.ryujinContinuousSolar=true -Dbleachmod.ryujinSolarBloom=true -Dbleachmod.ryujinHeatRefraction=true
```

Para o segundo ensaio, use outro perfil com **Embeddium 0.3.31 e Oculus 1.8.0**. Coloque o Complementary Reimagined **r5.2.2** na pasta `shaderpacks`, selecione o preset **LOW** e use os ajustes de calibração do ensaio, `BLOOM_STRENGTH=0.036` e `T_EXPOSURE=0.95`. Use estes argumentos Java.

```text
-Dbleachmod.ryujinV14Pilot=true -Dbleachmod.ryujinContinuousSolar=true -Dbleachmod.ryujinShaderComposite=true
```

## Roteiro curto de aceite

1. Com Ryūjin vinculada e Bankai desbloqueada, equipe a katana, esteja em Shikai e selecione Bankai com **G** ou no menu **Z**. Carregue com **R**. Com maestria para transformação instantânea, use **Shift + R**. **V** desce uma forma para repetir.
2. **Primeira execução à noite.** Deixe a música ambiente vanilla começar, então ative a Bankai. Confira se a trilha B assume a cena, se “Bankai” e “Zanka no Tachi” permanecem inteligíveis e se a música vanilla volta a poder tocar ao fim. Repita uma vez com o slider Música em zero e observe se voz e efeitos continuam audíveis com seus sliders ativos.
3. **Leitura em quatro marcos.** Antes da fala, procure a raiz da chama no olho direito anatômico e a cauda subindo pela têmpora. Na explosão, veja o fogo baixo nas laterais e o paredão ao fundo. No plano aberto, procure as faixas contínuas e, com Shimmer, a luz em pedra ou grama. No selo, confira a convergência na katana, as cinzas subindo e a câmera voltando ao modo inicial.
4. **Variação curta.** Repita de dia perto de uma parede ou degrau, e depois em canhoto. Os pés devem caber no quadro quando houver espaço, e a chama continua no olho direito. Experimente interromper a câmera com **F5** ou movimento após a ativação e confira o retorno dos controles e a limpeza de luz/fogo.
5. **Só depois compare o Complementary.** Use o mesmo local e horário. Observe se as faixas continuam legíveis, se o terreno recebe as sombras do perfil e se há efeitos atravessando blocos ou apagando o personagem. A recarga de recursos no meio da cena é um ensaio separado. O visual já concluiu essa recarga e os streams de áudio registraram `ZipFile closed`.

Para relatar uma diferença, informe o perfil, a hora aproximada no vídeo, a mão dominante e se havia parede ou desnível. Um vídeo de 15 s com o áudio do jogo resolve mais dúvidas de timing do que um quadro isolado.

Ainda aguardam validação a cena com observador em um segundo cliente, a comparação de um mesmo efeito no Photon e no AAA/Effekseer e a continuidade de áudio em recarga forçada. A matriz e os comandos da bancada estão no [relatório da v16](ryujin-v16-acabamento-e-perfis-forge-2026-11-07.md).
