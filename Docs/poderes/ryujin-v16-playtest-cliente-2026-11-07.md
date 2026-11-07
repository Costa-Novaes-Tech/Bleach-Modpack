# Ryūjin v16 — playtest no cliente

Use uma instância Forge 1.20.1 separada ou uma cópia do perfil e do mundo. A prova de referência usou Java 17, Forge 47.4.10, GeckoLib 4.8.4, Better Combat 1.9.0, Player Animator 1.0.2-rc1 e Cloth Config 11.1.136. [Relatório técnico](ryujin-v16-acabamento-e-perfis-forge-2026-11-07.md).

## Instalar no perfil de teste

1. Em `mods`, substitua o JAR anterior do Bleach Mod por `build/libs/bleachmod-0.2.0-v16-composite.jar`. Guarde o anterior fora de `mods`. O SHA-256 do JAR validado é `46b91f5c54d7760f8e5893189f5fd2bcd754521e5bc27089a0b0178008ed56c9`. Para reconstruir a branch, use `.\gradlew.bat check build --offline --console=plain` com o cache já preparado.
2. Copie `build/visual-lab/playtest-ryujin-v16/RyujinSunV16Full.zip`, `run/resourcepacks/RyujinAudio15Local.zip` e `run/resourcepacks/RyujinAudio15HeavyMusicLocal.zip` para `resourcepacks`. Ative os três e coloque **HeavyMusic acima de Audio15Local**. O ZIP visual reúne os mesmos recursos efetivos do perfil de QA. As vozes e a música ficam nos packs locais. Se precisar gerar novamente o visual a partir da branch, rode `python -B tools/models/lab/codex/ryujin01/build_solar_finish.py` com Pillow e adicione `tools/models/lab/codex/ryujin01/ryujin_bankai_continuous_sun_v13_lab_glowmask.png` à pasta gerada como `assets/bleachmod/textures/entity/ryujin_bankai_sun_glowmask.png` e copie `build/visual-lab/ryujin-v16-packs/RyujinSunV16Full` para `resourcepacks`.
3. Comece com **Shimmer 0.3.0 Forge** em `mods`, em uma instância sem Oculus. Acrescente estes argumentos Java da instância.

```text
-Dbleachmod.ryujinV14Pilot=true -Dbleachmod.ryujinContinuousSolar=true -Dbleachmod.ryujinSolarBloom=true -Dbleachmod.ryujinHeatRefraction=true
```

Para o segundo ensaio, use outro perfil com **Embeddium 0.3.31 e Oculus 1.8.0**, o Complementary Reimagined **r5.2.2** na pasta `shaderpacks` e o arquivo `ComplementaryReimagined_r5.2.2.zip.txt` de `build/visual-lab/playtest-ryujin-v16` ao lado do ZIP. Selecione o shader no menu. Use estes argumentos.

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
