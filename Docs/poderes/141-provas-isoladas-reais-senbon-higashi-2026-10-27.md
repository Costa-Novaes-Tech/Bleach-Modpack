# 141 — Provas isoladas reais: Senbon diagonal e Higashi vítima única

27/10/2026. Autorização explícita do usuário para executar testes isolados e continuar. Sem merge, commit, push ou mudança de branch. Feature-Poderes-bankais permaneceu em9984898 com documentos locais preservados. As duas frentes foram executadas em seus checkouts já existentes, sem copiar suas features para a nossa branch.

## Ambientes e alterações de teste

- Senbon: Bleach-Modpack-design, codex/design-poderes-criaturas, base6e4d767.
- Higashi: Bleach-Modpack-benihime, codex/ryujin-bankai-sol-selamento, base8f4115b.
- Java17.0.20.1, Forge1.20.1/47.4.10, perfil base sem Photon; logs de ambiente Higashi: Intel Iris Xe, heap2GiB, distância12. Não se mediu benchmark de desempenho.
- Ajustes locais SOMENTE de QA/observabilidade: ataque único no vazio, câmera frontal/perfil/costas centrada no jogador, capturas por tick, telemetria PlayerAnimator; Husk e Hollow alado10/20HP para morte normal letal, controles sobreviver/errar, logs de supressão/representação, saída automática e estabilização inicial. Nenhuma pose, textura, regra de morte/dano ou mecanismo de supressão foi corrigido nesta rodada.
- Diffs locais revisáveis em qa/cycle141/senbon-qa-only.patch e higashi-qa-only.patch. Alterações de QA ficaram nos worktrees das frentes, não commitadas; logs gerados não foram revertidos nem incluídos em commit. Não usar esses diretórios como se estivessem limpos ao integrar trabalho futuro.
- Cache Gradle e escrita nos worktrees exigiram execução com acesso ampliado. Nenhuma nova instalação. O primeiro lançamento Senbon usou caminho relativo errado para preparar script: interrompido antes do ensaio, corrigido e repetido; não contabilizado como prova.

## Execuções efetivas

1. Senbon: `./gradlew.bat runClient -PbenihimeQa=true -PsenbonCombatQa=true -PsenbonVfxDisable=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain`, exit0, BUILD SUCCESSFUL2m08s. Três vistas de um único ataque;188 linhas de captura. Mundo novo descartável criado pelo bootstrap, alvos retirados para não ocultar jogador. Somente melee VFX desligado; podem aparecer partículas residuais/passivas, não confundir com rastro do combo.
2. Higashi inicial: `./gradlew.bat runClient -PbenihimeQa=true -PcinematicQa=true -PhigashiPoseQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain`, exit0,4m50s. Quatro casos,373capturas. A primeira tela de acessibilidade bloqueou bootstrap; avançada via interface Windows autorizada. Husk inicial teve lacunas de carregamento; não homologado apenas com aquela amostra.
3. Higashi repetido: mesmo comando após120ticks de estabilização, exit0,3m12s,494capturas (116Husk/120Hollow/129sobrevivência/129erro). Câmera entrou nos letais e não entrou nos controles; servidor registrou morte dos letais e sobrevivência dos controles.

Essas execuções são cliente Forge real, não imagens geradas. Não foi executado runGameTestServer/check/regressionTest nesta rodada: o objetivo era prova gráfica isolada e os fontes QA foram compilados por runClient. Não reutilizar149/150 históricos como novo resultado.

## Resultado Senbon: REPROVADO na preparação do diagonal

[Referência versus captura real](qa/cycle141/senbon/diagonal-preparacao-reference-vs-forge.png).

Na vista perfil, age33 do cenário corresponde a uma pose ativa com rightArm(-1.7499833,0.37999332,-0.37999135) e rightItem(-0.15000999,0.24999666,-1.5499899). Os valores aproximam os keyframes autorais tick8 doS1: braço(-1.75,.38,-.38), item(-.15,.25,-1.55). Ainda assim, a captura mostra a lâmina apontando baixo/para trás, não elevada junto ao ombro. Portanto o clip e os canais existem e estão ativos, mas isso NÃO produz a pose visual desejada.

Não afirmar que todos os testes anteriores de parser eram inúteis; eliminam uma falha antiga, não resolvem esta. Próximo diagnóstico é composição de transformações: base do item, display herdado, ordem/sinais do item e braço e consumo pelos mixins da biblioteca. Senbon Shikai herda display ryujin_katana_handheld com rotação thirdperson[0,-90,55], além de rotações da geometria. Isso é um ponto a conferir, não causa única comprovada sem instrumentar matriz final.

`comboCount`0→1 no ensaio é estado do contador após um ataque, não demonstra que um segundo clip foi executado. Não mapear automaticamente esse contador para o ID do clip. Não usar tick38 de screenshot como “F02” sem telemetria. Não foram reabertos golpes2/3 ou VFX: o primeiro já bloqueia a sequência.

## Resultado Higashi: vítima única observada; cena completa NÃO homologada

[Contato Hollow alado](qa/cycle141/higashi-repeat/threshold-contact.jpg); [contato Husk](qa/cycle141/higashi-repeat/lethal-contact.jpg).

O nome histórico `threshold` foi reutilizado SOMENTE no harness desta prova para o Hollow alado. Neste ensaio NÃO É CASO DE LIMIAR: ambos letais iniciaram10/20HP,50% da vida. Runtime de produção não foi alterado. Identificação correta nos source.json/summary e log do servidor. Não reportar teste≤7% como realizado nesta rodada.

- Nos quadros observados durante a câmera, não apareceram cadáver vermelho e representação de cinzas juntos. Logger registrou chamadas do dispatcher bloqueadas para o ID original enquanto a silhueta era renderizada; depois, originalPresent=false e a representação continuou.
- Hollow manteve varianteFLYING, e asas/modelo alado permaneceram na apresentação. A variante correta agora tem evidência real neste caso, sem generalizar a todo renderer.
- Original morreu pelo dano; sobreviver/erro terminaram com alvo vivo e sem câmera de execução. É comportamento observado e logado no servidor integrado, não teste multiplayer.
- Na repetição Husk, elapsed37 ainda exibe o flash vermelho do acerto ANTES de entrar na câmera (primeiro quadro com câmera em39). Não há duas vítimas nesse quadro, mas a transição ainda não cumpre a proposta de morte visual completamente limpa.
- Capturas mostram escurecimento e desaparecimento; não medir alpha/qualidade estética como aprovados por isso. Corte e posicionamento da katana Higashi continuam sem aceite; não confundir correção da duplicação com reprodução dos quadros137.
- Há fogo em blocos ao redor de cenários sucessivos; não atribuir toda chama da imagem à própria vítima. Isso exige cenário visual mais limpo na próxima prova da arma.

## Limitações e honestidade das evidências

Capturas por tick não cobrem cada frame renderizado. Houve ticks faltantes, inclusive após estabilização; não garantir ausência de flash/duplicação de um único frame. Não há vídeo contínuo gravado de tela nesta rodada. GIFs são sequências de screenshots com100ms por imagem para inspeção, não tempo real nem prova de fluidez/TPS. `frames.jsonl` e arquivos originais têm origem recuperável.

Nomes antigos DRAW/CUT/HOLD em screenshots Higashi partem do relógio do cenário (elapsed-20), não do evento exato de confirmação: não usar o rótulo como medição de pose/pausa. Contatos novos usam apenas elapsed e estado real da câmera. Para medir20ticks precisos, próximo harness deve expor idade real de POSES/BURNS no mesmo frame. Esta prova tinha escopo de vítima única, não homologação da pausa.

Não foram testados: observador remoto, latência de servidor dedicado, players como vítimas, totem, aliados, espaços estreitos, outras variantes, matriz completa de armas. Limpeza automática dos mundos de prova não apaga evidências e nenhum save do usuário foi aberto.

## Próxima ação concreta

1. CongelarVFX e demais golpes; corrigir/calibrar primeiro a orientação real do SenbonS1 no mesmo cenário sem alvo. Demonstrar preparação alta antes de avançar.
2. Preservar a melhoria de supressão Higashi, sem merge. Corrigir transição antes da câmera (flash vermelho ainda observado) e provar corte em cenário limpo, com um único relógio e uma única vítima. Não regressar o renderer alado.
3. Só depois retornar aS2/S3 e acabamento. Hyōrinmaru e transformações aprovadas continuam preservadas. Nenhuma nova expansão/quest foi iniciada.
