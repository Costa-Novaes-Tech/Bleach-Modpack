# Ciclo 69 — Benihime em pé, empunhadura e áudio japonês

Data: 19/10/2026. Branch `Feature-Poderes-bankais`; HEAD de referência `ca9a06747819496bf36a1ad9e56be7dc6e16120a`. Sem commit/push/merge/reset/checkout. Alterações anteriores preservadas; Zaraki não modificada.

## Pedido e contrato

O usuário substituiu a postura ajoelhada por jogador em pé, segurando a espada com as duas mãos e ponta para o chão; pediu composição final frontal, vozes, música e efeitos. Escolheu voz provisória sintetizada em japonês, com interpretação inspirada na cena. A voz é genérica, sem imitação de ator.

A entrada continua em 560 ticks/28 segundos. Gameplay conserva release de 60 ticks, commit 40, custo único e regras de cancelamento existentes. A nova apresentação não acrescenta invulnerabilidade nem prolonga travas de combate. Rede **2.27**, schema **9**; cliente e servidor precisam da mesma build.

## Postura e câmera

`BenihimePlayerPose` mantém deslocamento corporal zero e alvo do rosto em 1,62 bloco. Braços, item principal e postura neutra têm entrada/saída suave. O suporte usa a mão livre; se houver outro item na mão secundária, ela conserva esse item em vez de sobrepor a empunhadura.

A solução calcula braços e compensações de item para destros/canhotos, respeitando o display do modelo Benihime. Não altera os JSONs globais da espada. O deslocamento lateral da câmera converge a zero entre os ticks 340–450; no plano final, jogador e manifestação estão no mesmo eixo frontal. Seguir o dono e restauração da perspectiva permanecem ativos.

**Correção motivada pelo teste real:** o primeiro cálculo pressupôs BendyLib, ausente do perfil usado. A captura mostrou mãos baixas e lâmina diagonal, apesar do teste matemático passar. Essa rodada é diagnóstica. O teste foi corrigido para reproduzir a cadeia realmente ativa, com BEND=0, sem instalar biblioteca que habilitasse dobras nas outras animações homologadas.

A solução nativa usa palmas em `(±0,75; 3; −√70,9375)` e `(±0,75; 6; −√70,9375)` em pixels do modelo. O cabo tem pontos 3,5/7,0294; a ponta chega a Y=23,4, antes do piso Y=24. Isso é validação numérica do modelo padrão, não garantia visual para todas as skins. Slim e itens secundários exigem inspeção específica.

## Voz, música e efeitos

Falas japonesas provisórias geradas offline por OpenJTalk/HTS Mei: `バンカイ` (1,227 s) e `カンノンビラキ ベニヒメ アラタメ` (2,730 s). Kana explícito conferido pelos fonemas; pitch genérico −7 semitons e velocidade 0,86. Qualidade de interpretação e pronúncia final precisam da escuta do usuário.

Música original de 28 s, aproximadamente +10,88 dB sobre a versão anterior; redução de volume durante 1,4–3,2 s e 22,8–27,1 s para acomodar as falas. Efeitos de cortina, revelação e fios por síntese original. Os apoios no chão mantêm o som discreto existente.

Marcos em ticks: fala Bankai 28; cortina 72; tração/pega 176; revelação 236; fios 450; nome completo 460. Música usa categoria MUSIC; falas VOICE; efeitos PLAYERS. A trilha é local ao dono; observadores recebem falas e efeitos posicionais. Menus/interrupção e fim da cena param as instâncias próprias, sem parada global dos sons. Cues consumidos são protegidos contra repetição após pequenos ajustes do relógio; observadores tardios não recebem falas antigas.

Configurações locais: `benihimeEntranceCamera`, `benihimeEntranceMusic`, `benihimeEntranceVoice`, `benihimeEntrancePenumbra`. Os controles de volume do Minecraft continuam independentes.

[Prévia de mixagem offline](media/69-benihime-postura-audio/mix-preview-OFFLINE.ogg): 28 segundos; pico PCM −1,645 dBFS/RMS −21,150 dBFS; encode/decode passaram, sem clipping. **Não é gravação do som do Minecraft.** Não foi usado microfone; a enumeração disponível não forneceu fonte de loopback para a captura de jogo.

[Licença e atribuição HTS Mei](licencas/69-voz-benihime-mei.md), CC BY 3.0. O aviso é copiado pelo Gradle para `META-INF/licenses/benihime-voice.txt`. Motor/modelos de autoria ficam isolados em `build/benihime-qa-69/tts`, não são dependências de execução do mod. Não houve instalação global, modificação de PATH ou serviço pago. Música/efeitos não usam samples do anime.

## Validação e evidências

Rodada inicial: 123 regressões e 148 GameTests aprovados, exit code 0, 1m54s. Essa rodada antecedeu a correção da suposição BendyLib; não prova a empunhadura nativa final. Logs em `build/benihime-qa-69/validation-base.log`.

Dois GameTests de empunhadura leem os JSONs reais e verificam contato, ponta para baixo, folga do piso e controles negativos (offset antigo/lâmina invertida). Um GameTest de áudio verifica os marcos, observador tardio, registros, OGGs e atribuição empacotada. Teste de postura foi atualizado para exigir altura constante e recuperação suave.

## Evidências do cliente real

- [Diagnóstico da biblioteca ausente](qa/benihime/cycle69/validation/diagnostic-bend-unavailable.png): pose inicial com espada diagonal; não é aprovação da solução final.
- [Laboratório nativo final](qa/benihime/cycle69/1792446837965/index.html): 287 PNGs originais e manifesto.
- [Release real diurna](qa/benihime/cycle69/1792446872915/index.html): 282 PNGs; tick 500 demonstra jogador de pé, mãos no cabo, lâmina vertical e composição frontal.
- [Release real noturna](qa/benihime/cycle69/1792446905076/index.html): 282 PNGs; repetição do roteiro, retorno à primeira pessoa e FOLLOW após translação/giro.
- [Vídeo real, sem áudio](qa/benihime/cycle69/validation/minecraft-native-final.mp4): 85 segundos, captura de janela a 20 FPS solicitados; 1696 quadros codificados e quatro descartes informados pelo FFmpeg. Decodificação completa passou. Não recebeu áudio adicionado na pós-produção.
- [Relatório da captura](qa/benihime/cycle69/validation/capture-report.json), com hashes e ambiente. Intel Iris Xe, heap 2 GiB, 12 threads disponíveis, render distance 6, resolução 1280×720. FPS informado durante amostragem: laboratório 36–101/mediana55, dia64–100/mediana89, noite64–97/mediana86. Não são frametimes nem benchmark controlado.

O serviço OpenAL iniciou; houve 12 cues no log final (seis por transformação), nas categorias previstas e sem falha de recurso dos novos sons. Isso confirma o envio ao mecanismo de áudio, não a avaliação humana da mixagem. O campo inicial de horário do ambiente noturno ainda contém 6000; `clientDayTime` por quadro acompanha a atualização para 18000, e os PNGs comprovam a noite.

A revisão independente confirmou o marco frontal e não identificou bloqueador novo de câmera/pose ou ciclo de vida do áudio. Avaliação provisória: postura4/5, empunhadura3/5, câmera4/5 e integração de áudio4/5; qualidade artística da voz, música e fluidez completa ficam sem nota conclusiva. A proteção adicional de cues por `EnumSet` foi revisada em código e incluída na build final; a captura não simula correções artificiais do relógio.

O jogador e a Benihime permaneceram no cenário plano de QA. Canhoto tem teste numérico; inspeção real de canhoto/slim, mão secundária ocupada, observador remoto, terreno irregular e ambientes estreitos continuam pendentes.

## Build e pacotes finais

**PASSOU, perfil base final:** `check build runGameTestServer prepareCombatTestPack preparePhotonTestPack --offline --console=plain`, exit code 0, 2m37s; 123 regressões e 148 GameTests. [Log](qa/benihime/cycle69/validation/validation-base-final.log).

**PASSOU, perfil Photon:** `-PphotonPilot=true runGameTestServer --offline --console=plain`, exit code 0, 2m10s; 148 GameTests. [Log](qa/benihime/cycle69/validation/validation-photon-final.log). O scanner/editor ainda apresenta erros recuperados já conhecidos; teste de servidor não é homologação gráfica de Photon nem de dois clientes reais.

Pacotes atualizados: `build/combat-test-pack/mods` (cinco JARs) e `build/photon-test-pack/mods` (sete JARs). Não há nova biblioteca obrigatória para executar o mod. As vozes/efeitos e sua atribuição foram conferidos dentro do JAR; JSONs validaram e `git diff --check` passou. [Manifesto de fontes, dependências e hashes](qa/benihime/cycle69/validation/delivery-manifest.json).

Reproduzir o laboratório: Java 17 e `./gradlew.bat -PbenihimeQa=true runClient --offline --console=plain`. São criados mundos QA novos em diretório isolado. Nenhum mundo pessoal foi utilizado. Os logs gerados na raiz foram restaurados ao estado inicial; cópias ficaram em `build/benihime-qa-69/generated-root-logs`.

**Status:** implementação disponível para teste; postura e câmera observadas no cliente real. Entonação/pronúncia artística, mixagem ouvida e cenários adicionais seguem pendentes de homologação.

## Continuidade

Este ciclo substitui somente a postura do jogador e o enquadramento/áudio da entrada do ciclo 68. Modelo da manifestação continua procedural; migração integral GeckoLib e acabamento de roupas/rosto não são concluídos por esta entrega. Terreno irregular, espaços apertados, duas instâncias reais e escuta/mix artística continuam sujeitos a validação. Preservar a ordem aprovada: primeiro esta entrada, depois Higashi, dash/Rajada Ryūjin e revisão do kit Benihime. Aizen, Mentor e Proficiências não foram antecipados.
