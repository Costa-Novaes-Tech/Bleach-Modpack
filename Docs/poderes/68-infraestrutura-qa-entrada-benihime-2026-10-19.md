# Ciclo 68 — infraestrutura de QA e entrada da Bankai da Benihime

Data: 19/10/2026. Branch: `Feature-Poderes-bankais`. Referência inicial: `ca9a06747819496bf36a1ad9e56be7dc6e16120a`.

**Implementação executável com evidências reais; homologação visual completa ainda pendente.** Build, servidor, assets e inspeção visual são resultados separados.

## Escopo e preservação

O usuário autorizou instalar o pacote local de skill/subagente, preparar ferramentas gratuitas necessárias, implementar a entrada e testar no Minecraft real. Foram preservadas as alterações documentais já existentes; não houve autorização para commit, push ou merge. A implementação da Zaraki permanece referência de sincronização, sem alteração de seu kit ou cinematográfica.

Documentos de contexto: ciclos 36–39 e 63 para Zaraki; ciclos 65–67 para storyboard, cortina, mãos e rosto. A alteração atual introduz uma primeira integração executável da sequência aprovada, com infraestrutura de evidências, sem declarar equivalência artística integral com a referência do anime.

## Skill, papel de QA e caminhos

- Skill de projeto: [bleach-mod-dev](../../.agents/skills/bleach-mod-dev/SKILL.md), com três referências e script de preflight examinados.
- Papel de QA: [bleach_visual_qa.toml](../../.codex/agents/bleach_visual_qa.toml), sintaxe TOML validada. Inicialmente usado por delegação manual; após a retomada, o tipo `bleach_visual_qa` apareceu na ferramenta e foi instanciado com sucesso como `final_visual_audit`, com revisão independente somente leitura.
- A skill aparece no catálogo de recursos desta sessão. A biblioteca central fornecida pelo usuário é a origem preservada do pacote.
- A sugestão genérica da skill para `Docs/qa/benihime` foi conciliada com a instrução específica do usuário: documentos e evidências documentadas ficam em `Docs/poderes/qa/benihime`.
- Logs e ferramentas locais de trabalho ficam em `build/benihime-qa-68`; mundos descartáveis em `run-benihime-qa/base`. Esses diretórios não substituem fontes editáveis e evidências arquivadas.

## Ambiente e ferramentas

Base revalidada: Minecraft 1.20.1, Forge 47.4.10, Java Temurin 17.0.20.1, Gradle Wrapper 8.8, GeckoLib 4.8.4, PlayerAnimator 1.0.2-rc1, Better Combat 1.9.0 e Cloth Config 11.1.136. Photon/LDLib continua perfil experimental separado.

Blockbench **5.2.2 portátil para Windows** foi obtido da release oficial. O manifesto local registra URL, tamanho, hash SHA-256 conferido e assinatura Authenticode válida. O editor foi aberto com a fonte editável e o gerenciador oficial mostrou **GeckoLib Models & Animations 4.2.5 instalado** no perfil usado para autoria. Essa é a versão do plugin de edição, não uma atualização da biblioteca GeckoLib do mod.

O pacote `Blockbench_x64_5.2.2.zip` identificado como conteúdo macOS não foi executado; o executável portátil Windows correto foi utilizado. O manifesto ainda deve manter separadas a arquitetura do launcher PE e a do payload Electron, caso esta última não tenha sido examinada.

FFmpeg 7.1 foi reutilizado de um pacote local existente e copiado para o ambiente isolado; versão, hash e smoke de codificação/decodificação foram registrados. `ffprobe` não foi localizado e não foi necessário para a inspeção atual. Não foram instalados Blender, Audacity, OBS ou Playwright. Java, Forge e dependências centrais não foram atualizados indiscriminadamente. Não houve instalação global, alteração de PATH global ou uso de APIs pagas.

Rastreabilidade: `build/benihime-qa-68/tools-manifest.json`.

## Contrato e implementação atual

O relógio visual é de **560 ticks / 28 segundos**. O contrato de gameplay continua com **60 ticks de release e commit em 40**, debitando o custo uma vez. A apresentação longa não prolonga automaticamente o bloqueio de ações nem concede invulnerabilidade.

`BenihimeEntranceService` publica idade, posição e yaw iniciais para o dono e observadores. O cenário fica ancorado durante a entrada; cancelamento, condições inválidas, movimento significativo e término removem o estado transitório. Foi corrigida uma dependência da ordem de eventos no tick 40: a apresentação precisa sobreviver mesmo quando seu evento ocorre antes do commit da release.

Sequência integrada: ajoelhar → cortina sobe fechada → aproximação e pega na borda → abertura central → avanço com apoios alternados → elevação do tronco → pose dos fios → retorno de câmera e postura. A versão final dos braços foi revista para cotovelos abaixo dos ombros, antebraços elevados à frente e pulsos menos afastados, reduzindo a aparência de pose em T.

O modelo em execução é **geometria procedural nativa articulada**, com IK dos braços e geração de superfícies. **Ainda não é um modelo GeckoLib exportado em execução.** A fonte `tools/models/source/benihime_entrance.bbmodel` é uma base editável de autoria; a instalação do plugin não conclui a migração de toda a manifestação para esse pipeline. O arquivo e o gerador devem ser preservados fora de `build`.

Após a entrada, a manifestação usa o mesmo desenho final em idle e acompanha posição e direção do jogador. `BenihimeFollowMotion` aplica aproximação exponencial e rotação pelo arco angular curto, mantendo direção válida em giros de 180° e reiniciando em teleporte. A passagem conserva a âncora e direção finais antes da aproximação à posição atual. O teste FOLLOW deslocou o jogador seis blocos em X, três em Z e girou sua orientação a 45 graus.

Uma execução real revelou erro de unidade na postura: a translação global `POSITION body` do PlayerAnimator usa **blocos**, embora pivôs individuais possam usar pixels. O deslocamento `-6` enterrava o jogador. A correção usa `BenihimePlayerPose`, com offset limitado a **-0,55 bloco**, dobra de pernas e altura do rosto compartilhada com a câmera. As novas capturas diurnas e noturnas confirmam jogador visível e inteiro, com a câmera ampla rebaixada para preservar as pernas. Imagens anteriores ao ajuste permanecem diagnósticas.

## Áudio

Foi criada uma faixa atmosférica original de 28 segundos por síntese matemática local, sem samples externos e **sem voz**. Ogg Vorbis estéreo, 22.050 Hz; codificação e decodificação concluídas com exit code 0. O relatório `build/benihime-qa-68/benihime-audio.json` registra hash, duração e medições PCM.

As janelas de redução da trilha reservam espaço para futuras falas. Legendas não significam que as vozes já foram gravadas. **Audição no jogo, qualidade artística da faixa e mixagem com efeitos permanecem pendentes.**

## Laboratório do cliente real

`runClient -PbenihimeQa=true` seleciona o diretório isolado `run-benihime-qa/base` e ativa um bootstrap opt-in. Ele cria um mundo com nome novo `Benihime-QA-68-*`; o preparador no servidor configura personagem/equipamento somente nesse contexto de desenvolvimento. Não abre nem substitui mundos pessoais.

Comandos locais de desenvolvimento, exigindo singleplayer e permissão 2:

- `/benihimeqa play`: inicia laboratório visual local.
- `/benihimeqa record`: inicia amostragem de capturas; no fluxo automático também inspeciona marcos.
- `/benihimeqa release`: solicita uma transformação real pelo fluxo do servidor, no mundo QA permitido.
- `/benihimeqa seek <0..559>`: pausa explicitamente em um instante para inspeção.
- `/benihimeqa angle <front|side|hand>`: escolhe perspectiva de inspeção.
- `/benihimeqa capture`: agenda PNG após a renderização e fechamento da interface.
- `/benihimeqa stop`: limpa laboratório e restaura câmera.

O laboratório passou a encerrar a cinematográfica e seguir o jogador automaticamente; `seek` continua sendo pausa deliberada. O laboratório local demonstra o renderizador. A opção `release` é necessária para comprovar também a integração servidor/cliente; nenhum deles substitui um teste com dois clientes.

PNG é obtido por `Minecraft Screenshot.grab` do framebuffer real. Metadados registram tick, cenário, perspectiva, posição, resolução e FPS informado pelo cliente; quando disponível, registram GPU/renderizador, heap e distância de renderização. Capturas são amostradas aproximadamente a cada dois ticks. O vídeo produzido pelo coletor usa intervalos de `wallMs`, com duração variável e pausas reais: **não é gravação de todos os quadros nem prova de fluidez a 60 FPS**.

Ferramenta de coleta: `tools/qa/benihime_evidence.py`. Evidências devem manter originais e manifesto, sem substituir execuções anteriores.

## Resultados técnicos e evidências

- **Baseline — PASSOU:** `check build runGameTestServer --offline --console=plain`, Java 17; exit code 0, 1m44s; 123 regressões e 139 GameTests obrigatórios. Log: `build/benihime-qa-68/baseline.log`.
- **Rodada posterior — PASSOU:** 123 regressões e 144 GameTests obrigatórios; exit code 0; `build/benihime-qa-68/validation-final.log`, duração 1m08s. Essa rodada antecede o novo teste e a última correção da postura.
- **Validação completa base — PASSOU:** `build/benihime-qa-68/validation-completa.log`, exit code 0, 1m41s; 123 regressões e 145 GameTests obrigatórios, incluindo unidade da postura.
- **Perfil Photon/LDLib — PASSOU:** `check build runGameTestServer prepareCombatTestPack preparePhotonTestPack -PphotonPilot=true --offline --console=plain`, exit code 0, 4m23s; 123 regressões e 145 GameTests obrigatórios. Log: `build/benihime-qa-68/validation-photon.log`. Isso é teste de servidor, não homologação gráfica de Photon. A rodada final base recompilou os ajustes posteriores de apresentação; os seis testes de lógica da entrada não mudaram após esta execução Photon.
- **Assets — PARCIAL:** geometria numérica, continuidade da passagem ao idle e contrato de pose cobertos por testes; decodificação do áudio passou. Exportação/runtime GeckoLib, textura final e revisão artística continuam pendentes.
- **Cliente Forge — EXECUTADO:** mundos QA e capturas reais produzidos. Isso comprova execução gráfica; não equivale a homologação integral da cena.

A execução inicial arquivada [1792428781823](qa/benihime/cycle68/1792428781823/index.html) mostra cortina fechada no tick 136, abertura central com dedos visíveis no 210 e mãos no chão plano no 340. O tick 500 também registra a pose horizontal que motivou a revisão dos braços. As imagens são observações daquele código, não da versão final.

Rodadas locais `1792429356045` (laboratório) e `1792429391050` (release real) em `run-benihime-qa/base/benihime-qa` registram braços revistos e o cenário FOLLOW. Os metadados do FOLLOW real indicam deslocamento da origem de `[0.5,-60,0.5]` para `[6.5,-60,3.5]`; a inspeção dessa rodada identificou o erro do jogador enterrado. **São rodadas diagnósticas superadas pela correção de postura; aguardam substituição por evidência nova, sem apagar os originais.**

## Evidências finais e leitura da matriz

Galerias reais arquivadas, originais PNG, hashes e MP4 amostrado:

- [Laboratório e aproximações](qa/benihime/cycle68/1792443995953/index.html): 286 imagens, incluindo mão no tick 172 e vista lateral no tick 320.
- [Release real diurna](qa/benihime/cycle68/1792444030884/index.html): 296 imagens. Ticks 120/500 mostram jogador inteiro, cortina fechada e braços finais relaxados; FOLLOW antes/depois.
- [Release real noturna](qa/benihime/cycle68/1792444063082/index.html): 290 imagens, repetição da entrada e FOLLOW. O campo `dayTime` inicial ainda contém 6000 porque o cliente não recebera a atualização `/time set midnight`; imagens e log demonstram noite. Os originais não foram corrigidos artificialmente. Novas capturas passam a registrar `clientDayTime` por quadro.

Ambiente: Intel Iris Xe, 12 threads disponíveis, heap máximo 2 GiB, distância de renderização 6, 1280×720. FPS informado nos metadados: laboratório 26–103 (mediana 75); release diurna 60–106 (mediana 95); noturna 77–102 (mediana 97). São amostras do contador do Minecraft, sob captura, não benchmark ou medição de frametime.

As imagens acima precedem apenas o último refinamento do retorno da câmera e a penumbra dirigida. Essa revisão foi motivada por auditoria: o retorno antigo apontava sempre três blocos atrás, ignorando a perspectiva inicial. O novo retorno interpola até a perspectiva original e respeita o pitch; a penumbra depende de olhar/distância/fase, com opção local de desligar. Rodada complementar registrada separadamente abaixo.

- **B01 — PASSOU no cenário frontal plano:** cortina opaca e fechada antes da revelação; lateral completa ainda precisa validação.
- **B02/B03 — PARCIAL:** mão e dedos na abertura central demonstrados; clipping e contato contínuos não homologados por screenshots.
- **B04/B05 — PARCIAL:** apoios alternados, avanço e elevação executados; chão plano observado e comprimentos dos braços testados. Transferência de peso ainda requer revisão artística contínua.
- **B06 — PARCIAL:** roupa/cabelo/rosto presentes; não são textura final nem física de tecido/cabelo.
- **B07 — PASSOU para o ajuste solicitado:** cotovelos baixos e antebraços elevados substituem braços horizontais; rosto sereno e jogador inteiro visíveis.
- **B08 — PARCIAL:** ajoelhar/close e enquadramento corrigidos; áudio ouvido/vozes pendentes. Último retorno de perspectiva tem rodada complementar.
- **B09 — PASSOU no cenário plano:** retorno de controle e manifestação acompanhando translação/giro demonstrados no FOLLOW diurno/noturno; não prova todos os cenários.
- **B10/B11 — PENDENTE:** terreno irregular e espaço estreito não homologados.
- **B12 — PASSOU no cenário plano:** dia/noite observados, com ressalva do metadado inicial de horário.
- **B13 — PENDENTE:** outras skins e observador externo não homologados.
- **B14 — PARCIAL:** GameTests de cancelamento/equipamento/movimento/dimensão/morte; restauração visual sob todas as interrupções e desconexão ainda pendente.
- **B15 — PENDENTE:** servidor de GameTests passou; isso não é dois clientes reais em servidor dedicado.
- **B16 — PARCIAL:** perfil Photon/LDLib passou nos testes de servidor; inspeção gráfica conjunta ainda pendente.
- **B17 — PARCIAL:** hardware/perfil/FPS amostrados; frametime e benchmark comparativo pendentes.

## Rodada final — câmera, penumbra e captura contínua

A revisão encontrou dois defeitos no retorno: alvo traseiro fixo mesmo quando a origem era primeira pessoa, e pontos de foco opostos cruzando um vetor nulo. Foram corrigidos por destino conforme a perspectiva original, pitch preservado e interpolação angular pelo arco curto. O raycast parte do olho do jogador. A distância de terceira pessoa é quatro blocos; a equivalência exata à câmera vanilla junto de obstáculos ainda não foi homologada.

A nova `benihimeEntrancePenumbra` escurece suavemente a imagem conforme distância, direção do olhar e fase da entrada. É configuração local opcional; não modifica iluminação do mundo, status ou regras do combate. Vozes não foram adicionadas.

Evidências da versão final, posteriores às correções:

- [Laboratório final e detalhes](qa/benihime/cycle68/1792444963540/index.html): 287 PNGs.
- [Release real diurna final](qa/benihime/cycle68/1792444998518/index.html): 282 PNGs; ticks 536/548/554 mostram giro progressivo até a visão original, sem colapso do foco ou passagem frontal pelo rosto vista no diagnóstico.
- [Release real noturna final](qa/benihime/cycle68/1792445030710/index.html): 282 PNGs; horário por quadro acompanha a atualização de 6000 para 18000; FOLLOW e retorno à primeira pessoa registrados.
- [Gravação contínua final do Minecraft](qa/benihime/cycle68/validation/minecraft-continuous-final.mp4): 85 segundos, janela Minecraft somente, sem áudio/microfone, 1688 quadros codificados, alvo de 20 FPS, oito descartes informados pelo FFmpeg. Contém final do laboratório e as duas releases reais. Decodificação completa terminou com exit code 0. Hash/procedência em [continuous-final.json](qa/benihime/cycle68/validation/continuous-final.json).
- [Logs e ferramentas arquivados](qa/benihime/cycle68/validation/tools-manifest.json). O vídeo anterior está identificado como diagnóstico e não valida a última câmera.

No último cenário, com captura contínua simultânea, o contador de FPS informou: laboratório 27–61 (mediana 51), dia 42–60 (mediana 60), noite 28–61 (mediana 59). Isso não é comparação controlada com a rodada anterior, nem garante fluidez constante; captura/ativação da janela alteram as condições. Não foram medidos frametimes.

O coletor agora encerra a amostragem após o FOLLOW noturno. A última série permaneceu com 282 quadros até fechar o cliente; reexecuções manuais não devem ser anexadas inadvertidamente ao ensaio automático concluído.

Avaliação artística provisória, baseada nos marcos inspecionados: contato 3/5, articulação 3/5, câmera 3/5, VFX 2/5 e legibilidade 4/5. Fluidez contínua não recebe nota conclusiva nesta revisão. Geometria ainda apresenta facetas e acabamento de protótipo; essas notas não são aprovação do usuário.

**B08/B09:** correção e retorno à primeira pessoa demonstrados no cenário plano. Áudio ouvido, outras perspectivas com obstáculos e interrupções gráficas completas permanecem pendentes. A gravação sem som não valida mixagem.

**Photon:** o servidor passou, mas o scanner LDLib registrou tentativas recuperadas de carregar `FXEditor` no lado dedicado. O log preserva os erros/avisos; não é um perfil livre de avisos nem prova de editor gráfico funcionando em servidor.

## Build de entrega e como reproduzir

**PASSOU:** `check build runGameTestServer prepareCombatTestPack preparePhotonTestPack --offline --console=plain`, Java 17; exit code 0, 2m14s; 123 regressões e todos os 145 GameTests obrigatórios. [Log final](qa/benihime/cycle68/validation/validation-entrega.log). `git diff --check` passou. Os 100 JSONs de recursos, TOML do agente e correspondência entre gerador e fonte `.bbmodel` validaram; áudio e vídeo tiveram decodificação completa bem-sucedida.

Pacotes atualizados: `build/combat-test-pack/mods` contém cinco JARs; `build/photon-test-pack/mods` contém sete. Ambos contêm o mesmo `bleachmod-0.2.0.jar`, com a nova classe e o OGG incluídos. [Manifesto da entrega](qa/benihime/cycle68/validation/delivery-manifest.json) registra hashes do JAR, dependências e fontes alteradas. Cliente/servidor devem compartilhar a mesma build de protocolo **2.26**; schema persistente segue 9.

Para repetir o cenário automático: definir `JAVA_HOME` para o JDK 17 já usado e executar `./gradlew.bat -PbenihimeQa=true runClient --offline --console=plain`. Aguardar laboratório, release real diurna, FOLLOW e repetição noturna. `/benihimeqa seek` pausa intencionalmente; `/benihimeqa stop` limpa a bancada e restaura a câmera. O ensaio foi executado no perfil base; acrescentar `-PphotonPilot=true` seleciona outro diretório, mas sua inspeção gráfica ainda não foi homologada.

Foram restaurados apenas os logs gerados na raiz, que estavam limpos/ausentes no status inicial; cópias ficaram em `build/benihime-qa-68/generated-root-logs`. Alterações anteriores do usuário e índices do Git foram preservados. Não houve commit, push, merge, reset ou checkout. HEAD permanece `ca9a06747819496bf36a1ad9e56be7dc6e16120a`, branch `Feature-Poderes-bankais`; nenhum arquivo do kit/cena Zaraki foi modificado.

## Continuidade e limites

A primeira integração e o laboratório estão implementados. A homologação artística ainda exige observar movimentos completos, ouvir a mixagem e validar terreno irregular, ambientes estreitos e dois clientes reais. A fonte Blockbench é base de autoria; a migração integral para runtime GeckoLib não foi concluída. Depois da entrada, retomar Higashi, dash/Rajada Ryūjin e revisão do kit Benihime, na ordem do usuário. Zaraki permanece intacta.

**Status desta revisão: PENDENTE DE VALIDAÇÃO VISUAL COMPLETA.**
