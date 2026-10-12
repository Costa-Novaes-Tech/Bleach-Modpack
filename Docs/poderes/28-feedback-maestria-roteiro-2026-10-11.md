> **Estado vigente — ciclo 30, 12/10/2026:** primeira fatia de Combat State automático, indicador no HUD e limpeza transitória, sem mudar regeneração/stamina/atributos. [Lógica, arquivos, timeout provisório e testes](30-estado-combate-2026-10-12.md). Protocolo 2.7; schema 5 preservado. Combat Mode/hotbar seguem futuros; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 29, 12/10/2026:** ciclos anteriores homologados pelo usuário. Definições comuns dos 24 slots/formas de Ryujin e Hyōrinmaru consultadas por execução, acesso e HUD; valores preservados. [Arquivos, lógica e validação](29-definicoes-compartilhadas-2026-10-12.md) · [Contrato para novos poderes](contrato-criacao-novos-poderes.md). Registros anteriores abaixo são históricos; nova feature será discutida antes de outro kit.

# Ciclo 28 — Feedback de maestria e roteiro de homologação

Data: 11/10/2026. Branch: Feature-Poderes-bankais. Continuidade autorizada enquanto o usuário testa os visuais.

## Revisão e mudanças

Os ganhos de treino já estavam implementados. Permanecem: +0,5 de maestria da forma por prática válida e +1 da técnica quando a ação pertence a um slot; intervalo global 20 ticks e repetição no mesmo alvo 100 ticks. Sem progresso ocioso, em alvos aliados/inválidos, com espada de outra identidade ou com cooldown/custo desativados para testes. Descoberta não compra vínculo automaticamente.

Foram corrigidos feedbacks concretos: HUD arredondava 19,5 para 20 apesar de o requisito não estar atingido; linha de técnica disponível não diferenciava seu domínio do progresso da forma; próxima transformação não distinguia treino, compra e disponibilidade. O menu K ainda orientava procurar missões, incompatível com o treino atual. Cruzar marcos por treino não tinha aviso explícito.

## Implementação e arquivos, passo a passo

1. `src/main/java/com/bleachmod/common/technique/MasteryFeedback.java`: função pura que apresenta até uma casa decimal sem arredondar para cima; snapshots de identidade, forma, slots disponíveis e formas descobertas. Compara antes/depois e retorna apenas novas liberações. Não muda dados nem concede acesso. Não há cache de notificações ou novo dado persistido.
2. `TechniqueProgression.java`, no mesmo diretório: centraliza constantes existentes 20 para descoberta da Shikai pelo treino selado e 25 para Bankai pelo treino Shikai. Slots conservam requisitos 0/20/40/60 na forma ativa. Descobertas persistidas e repertório legado permanecem compatíveis.
3. `MasteryPracticeService.java`: captura estado antes de um crédito aceito, mantém ganhos/restrições, executa desbloqueios existentes e envia mensagens ao próprio jogador apenas para as transições detectadas. Informa slot/forma ou descoberta da transformação com orientação sobre compra do vínculo. Sincronização completa existente atualiza o HUD. Uso bloqueado, repetição negada ou modo gratuito não gera notificações de progresso.
4. `src/main/java/com/bleachmod/client/hud/ZanpakutoHud.java`: mostra fração da maestria da forma, domínio da técnica e progresso para slot bloqueado. Forma bloqueada recebe indicação própria. Próxima forma informa treino atual/requisito, compra do vínculo ou disponibilidade conforme os dados reais. Mostra treino suspenso no modo administrativo gratuito; painel aumenta 14 unidades para acomodar essa linha. Atalhos continuam provenientes dos controles configurados pelo usuário.
5. `src/main/java/com/bleachmod/client/gui/StatusScreen.java`: mensagem de descoberta pendente agora orienta treino, mantendo custos, compra e requisitos anteriores.
6. `src/main/resources/assets/bleachmod/lang/pt_br.json` e `en_us.json`: traduções do feedback. Modelos, texturas e emissores de partículas não mudam neste ciclo.
7. `src/test/java/com/bleachmod/MvpRegressionTest.java`: verifica frações, limiar, novas transições, ausência de repetição, separação de identidade e Asauchi sem slots.
8. `src/main/java/com/bleachmod/gametest/MasteryGameTests.java`: golpe real cruza 19,5 → 20, descobre Shikai, não compra forma nem debita pontos; repetição imediata continua bloqueada. A suíte anterior cobre controles, defesa, passivas, modo teste e desbloqueios.

Documentos alterados exclusivamente em Docs/poderes. Aizen, história e provas narrativas continuam futuros. O domínio individual de técnica continua um indicador de prática; não foi criado bônus de dano ou redução de custo por esse domínio.

## Roteiro A — Visuais dos ciclos 26/27

Use um mundo de teste e personagem Shinigami. Se seu personagem já está preparado, mantenha-o. Os comandos abaixo requerem permissão administrativa e alteram o progresso desse personagem; não representam aquisição normal.

```text
/bleachdev zanpakuto bind @s hyorinmaru
/give @s bleachmod:hyorinmaru
/bleachdev skill set @s zanpakuto 2
/bleachdev mastery set @s zanpakuto sealed 60
/bleachdev mastery set @s zanpakuto shikai 60
/bleachdev mastery set @s zanpakuto bankai 60
/bleachdev cooldowns disable @s
/bleachdev reiatsu free @s
```

Equipe a Hyōrinmaru. Selecione/ative as formas pelos controles existentes (G para seleção e R para carga; respeite suas configurações). H/N/B/C são os slots padrão; confirme as teclas mostradas no HUD. Por padrão V retorna à forma anterior.

1. Selada: H corte frontal, N aberto, B armadura temporária, C controle de duelo. Confira neve/azul dos cortes, alcance e impacto com alvo hostil; armadura e controle devem permanecer com os comportamentos anteriores.
2. Shikai: confira anel de liberação/aura. H frontal com dragão, N aberto, B barreira, C Zona Glacial. Observe o interior inteiro da zona, não só a borda. Caminhe para longe enquanto o campo existir: ele deve permanecer na origem até expirar/cancelar.
3. Bankai: confira transformação/aura, corte frontal e dragão com tamanho/percurso anteriores, aberto, barreira e Zona Glacial mais ampla. Espere o término do campo e confirme restauração da neve temporária. Encerre Bankai e verifique restauração do clima; esse comportamento não foi alterado pela migração.
4. Repita H rapidamente: o dragão anterior deve ser substituído. Troque a espada, encerre a forma, morra ou troque de dimensão para confirmar limpeza dos efeitos aplicáveis. Partículas enviadas podem terminar sua duração natural.
5. Se possível, use observador próximo e em outra dimensão. Novas partículas são enviadas até 32 blocos de cada ponto, não simplesmente do jogador; numa zona de raio 30 o observador distante do centro ainda pode estar próximo da borda.
6. Rode `/bleachdev assets compose ice aura 3`: somente o executor deve ver a bancada. Encerre com `/bleachdev assets stop`.
7. Compare aparência e FPS no mesmo cenário/configuração de partículas da versão anterior. Contagem solicitada não prova desempenho ou equivalência artística na tela.

## Roteiro B — Treino e HUD do ciclo 28

Não use o personagem preparado com todos os marcos já liberados para validar bloqueios iniciais. Prefira outro personagem/mundo de teste com progresso novo: reduzir maestria por comando não apaga descobertas persistidas, por compatibilidade.

```text
/bleachdev cooldowns restore @s
/bleachdev reiatsu restore @s
/bleachdev reiatsu fill @s
```

1. Com Asauchi vinculada/equipada, dê golpes aceitos em zumbis ou outros hostis. Um crédito soma 0,5 na forma. Golpes no vazio e espera não treinam. No mesmo alvo, aguarde cinco segundos entre créditos; o intervalo global é um segundo. Reabastecer reiatsu por comando não ativa custo gratuito.
2. Para abreviar o marco em personagem novo, use `/bleachdev mastery set @s zanpakuto sealed 19.5`. HUD deve mostrar 19.5, sem indicar 20. Um golpe válido seguinte atinge 20 e envia aviso de descoberta Shikai. Não concede a transformação antes da compra do vínculo.
3. Abra K: confira a compra do vínculo quando descoberta e pontos forem suficientes. Se necessário, em teste administrativo use `/bleachdev points add @s 700`; faça a compra pela tela para validar o fluxo normal. Asauchi conserva somente os bônus de dano das formas, sem slots elementais.
4. Para Ryujin/Hyōrinmaru, teste uma identidade sem repertório legado previamente liberado: slot 1 disponível, demais bloqueados por maestria 20/40/60. Ao cruzar limiar por ação válida, recebe aviso de slot liberado; sua linha passa a mostrar domínio da técnica. Treino Shikai a 25 descobre Bankai, preservando a compra e a cadeia de transformação.
5. Alterne identidades: o progresso deve pertencer a cada zanpakuto e forma. Ao reconectar, descobertas continuam sem repetir avisos. Personagens antigos podem conservar habilidades já descobertas mesmo com maestria baixa.
6. Ative custo gratuito ou cooldown desativado: HUD deve indicar treino suspenso; use habilidades e confirme que o progresso não cresce. Restaure ambos ao terminar. Remapeie uma tecla e confira o HUD; teste escalas de interface e resoluções diferentes para detectar cortes/sobreposição.

## Validação

check/build offline aprovados; 66 regressões e 54 GameTests obrigatórios passaram. Após ajustar apenas o texto de orientação do menu K, check/build e as 66 regressões foram executados novamente e aprovados. Logs locais: build/cycle28-validation.log e build/cycle28-final-build.log. git diff --check aprovado. JAR atualizado: build/libs/bleachmod-0.2.0.jar. Modelos/texturas e documentos externos a Docs/poderes intactos. Homologação visual/FPS permanece com o usuário.
