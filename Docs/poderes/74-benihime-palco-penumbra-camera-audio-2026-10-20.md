# Ciclo 74 — palco Benihime, penumbra, câmera e mixagem

20/10/2026. Usuário autorizou implementar os ajustes do [vídeo do ciclo 73](73-analise-video-usuario-entrada-benihime-2026-10-20.md), acrescentar piso/holofote e aumentar a clareza do nome da Bankai, com testes na máquina. Branch `Feature-Poderes-bankais`, referência inicial `7621758`, com documentação dos ciclos 72/73 já presente e preservada. Sem commit/push/merge. Zaraki e demais kits não foram alterados.

## Contrato preservado

- Cena visual de 560 ticks/28 segundos; release de gameplay 60 ticks, commit no 40, custo único, sem invulnerabilidade nova.
- Nenhum bloco do mundo é removido/substituído para apresentar o palco. Não há teleporte do jogador, alteração de colisões, horário do mundo ou efeito de status na apresentação de produção.
- Schema permanece **9**. Rede passa a **2.29**: o servidor informa a orientação escolhida do palco; cliente/servidor devem usar a mesma versão.
- A pose do jogador permanece em pé, espada baixa e empunhadura dupla. Fonte/runtime procedural da Benihime mantido; não houve migração integral para GeckoLib.

## Piso e iluminação

Um piso escuro temporário se forma durante a subida da cortina, abrangendo o jogador, a saída e os apoios. O plano está na altura dos pés do jogador, com pequeno afastamento visual para evitar conflito com a superfície. Durante a cena, os apoios procedurais usam esse plano comum; ao terminar, a adaptação existente ao terreno e o acompanhamento são retomados gradualmente. O piso é visual: não vira plataforma física, não permite caminhar sobre abismos e não achata o terreno real.

O palco possui uma área clara sob o jogador. A penumbra aumenta na subida da cortina, alcança o ambiente e a frente da cena e preserva uma elipse suave sobre a projeção do personagem. O efeito se desfaz ao erguer o tronco. Há controles locais de apresentação já existentes; não foi acrescentada dependência de shaders/Photon.

É uma composição visual com piso e máscara de tela, não simulação física de holofote, tecido, luz volumétrica ou sombras do mundo. Preserva a iluminação renderizada do personagem no recorte, sem iluminar globalmente os blocos ou exigir um novo renderizador para jogadores. A área preservada acompanha a projeção do ator na câmera, inclusive para observadores que recebem a cena.

**Diagnóstico visual e correção:** a primeira rodada real mostrou piso correto, mas sem penumbra na câmera dirigida. O vetor convencional da câmera não acompanhava a orientação aplicada pelo evento Forge. O cálculo agora usa a direção efetiva da tomada para intensidade e projeção da área preservada. O reteste precisa ser usado como representação final; a primeira rodada permanece diagnóstico histórico.

## Câmera e posicionamento

No retorno à primeira pessoa, a câmera faz uma aproximação orbital para a parte de trás antes de chegar aos olhos. O modelo local não é renderizado quando essa aproximação final entra no volume da cabeça. A regra é restrita à cena Benihime local e ao retorno à primeira pessoa; não troca o renderizador geral nem interfere na câmera Zaraki/Higashi.

Ao iniciar, o servidor avalia sete direções no hemisfério atrás do jogador, priorizando a original e penalizando desvios maiores. Amostras de volumes/rastreamentos procuram reduzir interferências de paredes e vegetação; chunks ausentes não são carregados para compor a cena. A direção selecionada é sincronizada (`stageYaw`) separadamente do yaw original do jogador. O close inicial conserva a direção original do ator.

Esta é uma busca limitada no início da invocação, não um solucionador universal de posicionamento em cavernas ou espaços fechados. Se todas as opções estiverem obstruídas, pode continuar havendo oclusão; não há remoção de terreno, imunidade visual a paredes ou teleporte de segurança do personagem. A retomada do acompanhamento parte da orientação final da manifestação, mas conserva o comportamento de seguir o jogador; não foi implementado desvio contínuo de obstáculos nessa fase.

## Áudio e avaliação disponível

A faixa fornecida já contém vozes e música. Para manter “Bankai” e os tempos originais, o novo tratamento concentra a correção no trecho final:

- Nome completo mantém início aproximado em **23,065 s** e duração da fala limpa de **4,124 s**; mix conserva **28 s**.
- Trilha/mix de fundo começa a reduzir em **20,8 s**, chega a **18%** em **22,8 s** e permanece baixa até terminar. O fade final existente é preservado.
- A fala limpa alinhada ao sample original reforça o nome, com alvo nominal de **+4 dB** frente à referência anterior ao ciclo 71; o ciclo 71 usava +2,5 dB. O OGG isolado também recebe +4 dB. Não aumentar todas as falas ou antecipar o nome.
- Limitador no trecho final preserva margem na codificação OGG. O modo combinado continua evitando disparar um segundo cue local por cima; observadores e modo sem música usam as falas separadas existentes.

O trecho do nome no vídeo recebido foi localizado por correlação, em aproximadamente **3:56,248**. A projeção da fala limpa sobre o sinal gravado dá relação fala/resíduo aproximada de **3,29 dB**, coerente com o feedback de música muito presente. No asset combinado anterior, 3,26 dB; na versão nova, **19,08 dB**, melhora aproximada de **15,82 dB** nesse método. O resíduo inclui música, compressão e diferenças entre fontes; não é medição exata de stem musical separado, nota de inteligibilidade ou avaliação subjetiva de pronúncia.

Pico nativo estéreo final de aproximadamente **−2,90 dBFS**, sem amostras fora de escala. Conferência correta usa os canais nativos: o downmix mono normalizado em energia pode elevar artificialmente o pico, portanto não deve ser usado para declarar saturação da faixa estéreo.

**Limitação explícita:** houve análise do sinal do vídeo e dos assets, não escuta subjetiva pelo agente nem captura de loopback do jogo. A preferência de mixagem e inteligibilidade ainda exige escuta do usuário. O importador é reproduzível; originais em Downloads foram preservados. Nenhum áudio enviado a serviço externo.

## Cenários e critérios de QA

Linha de base: `check runGameTestServer --offline --console=plain`, **127 regressões/151 GameTests**, passou em 1m53s. Tentativa inicial restrita falhou no lock do cache Gradle; execução autorizada com acesso ao cache passou.

Primeiras verificações após implementação passaram; novos GameTests elevam o total a **155**, cobrindo duração/finalização do piso e luz, área preservada do jogador, limites/orientação de palco, trajetória de retorno e snapshot de orientação com obstáculo, sem girar/mover o jogador ou cobrar recursos.

O cliente foi lançado com `-PbenihimeQa=true -PbenihimeStageQa=true`, em diretório isolado e save novo com prefixo de QA. O laboratório inclui bancada, invocações diurna/noturna, acompanhamento, parede, vegetação e cancelamento por deslocamento. A flag extra não se ativa em produção: exige ambiente de desenvolvimento, singleplayer/admin, diretório e nome de save de QA.

**Correção do próprio ensaio:** a primeira preparação de obstáculos usou Y=4, mas o chão superplano deste preset fica em Y negativo; o personagem caía e a cena encerrava cedo. O cenário agora consulta a altura real, e verifica tempo mínimo da cena para não registrar esse encerramento como aprovação. Não usar as capturas curtas dessa primeira tentativa como evidência de sucesso diante de obstáculos.

### Fechamento de validação

**Base final:** `check build runGameTestServer prepareCombatTestPack --offline --console=plain` passou com **127 regressões e 155 GameTests**, em **2m06s**, depois de encerrar o cliente. O teste de snapshot com parede, custo/commit original e integrações Zaraki fazem parte da suíte. [Log final base](qa/benihime/cycle74/validation/validation-cycle74-release-base.log).

**Photon final:** `-PphotonPilot=true check build runGameTestServer prepareCombatTestPack preparePhotonTestPack --offline --console=plain` passou, sequencialmente após o base, com **127 regressões e 155 GameTests**, em **2m16s**. Persistem os erros recuperados conhecidos do scanner/editor Photon tentando carregar classes de cliente no servidor; a suíte concluiu sem testes obrigatórios falhando. Não é homologação gráfica do editor. [Log final Photon](qa/benihime/cycle74/validation/validation-cycle74-release-photon.log).

Pacotes finais conferidos: `build/combat-test-pack/mods` com **cinco JARs** e `build/photon-test-pack/mods` com **sete JARs**. Integridade ZIP, protocolo 2.29, áudio idêntico aos recursos finais e presença das atribuições conferidos no JAR próprio. [Manifesto/hash dos pacotes](qa/benihime/cycle74/validation/delivery-manifest.json). Nenhuma biblioteca obrigatória nova.

**Cliente real — reteste inspecionado:**

- [Bancada e marcos próximos](qa/benihime/cycle74/bench/index.html): piso, cortina fechada, sombra no entorno e recorte do jogador observados. São 24 quadros selecionados entre 287, com ângulos de mão/lateral do laboratório.
- [Invocação diurna](qa/benihime/cycle74/day/index.html): 282 quadros exportados e montagem amostrada, sem áudio, com intervalos reais de captura. Mostra aumento da penumbra, área clara sob o jogador, saída/elevação e retorno. Não é gravação contínua de todos os quadros renderizados.
- [Invocação noturna](qa/benihime/cycle74/night/index.html): 19 marcos selecionados entre 282; segue a iluminação noturna original do ator no recorte preservado, não um aumento físico de luz sobre sua skin. Acompanhamento registrado depois das invocações diurna/noturna.
- [Parede](qa/benihime/cycle74/wall/index.html): manifestação apresentada ao lado da obstrução, sem o paredão encobrir seu corpo como no vídeo anterior. Cena completa, término em `elapsed=580` incluindo 20 ticks de preparo QA; câmera `FIRST_PERSON` ao final.
- [Vegetação](qa/benihime/cycle74/vegetation/index.html): mesma checagem com barreira de folhas; corpo legível no lado livre. Cena completa, término em `elapsed=579`, câmera restaurada. Ensaio controlado não equivale a reproduzir todas as árvores/encostas do vídeo do usuário.
- [Cancelamento](qa/benihime/cycle74/cancel/index.html): deslocamento administrativo de três blocos no tick 210 do ensaio; cena encerrou no 211. Captura posterior sem piso/cortina/penumbra/HUD cinematográfico preso, com câmera `FIRST_PERSON`. Foi interrupção por deslocamento, não ensaio de morte, dimensão ou desconexão.

Os logs finais do laboratório registram `passed=true` nos três cenários adicionais, agora com critério de duração e perspectiva. O retorno da câmera já não mostra o rosto preenchendo a tela nos marcos finais inspecionados; a direção final acompanha o olhar do jogador, não um enquadramento frontal fixo. Cliente encerrado intencionalmente após os ensaios para rodar as suítes; eventual saída não zero da tarefa gráfica por esse encerramento não foi apresentada como crash do mod.

**Avaliação visual do agente (0–5, restrita ao material inspecionado):** contatos/mãos 3; leitura da saída/elevação 3; câmera 4; palco/penumbra 4; legibilidade nos obstáculos controlados 4. Sem nota de fluidez integral ou desempenho: há amostragem, IO de screenshots e ausência de benchmark. Roupa/mãos/rosto seguem estilizados e a transferência de peso não virou simulação física.

Ambiente capturado: Intel Iris Xe, 1280×720, heap de 2 GiB e distância de renderização 6. Metadados originais e hashes nas galerias; seleção/subconjunto explicitados nos manifestos. Os contadores de FPS não representam medição de frametimes.

Permanecem pendentes: julgamento artístico/escuta do usuário, observador remoto/dois clientes, interiores sem espaço livre, combinações de skins/mãos e todas as interrupções não exercitadas, benchmark e inspeção gráfica/editor Photon. A suíte de servidor no perfil experimental não encerra essas validações.

Comparação de áudio disponível: [antes — trecho final](qa/benihime/cycle74/audio/before-name.wav), [depois — trecho final](qa/benihime/cycle74/audio/after-name.wav) e [análise de sinal](qa/benihime/cycle74/audio/audio-signal-analysis.json). São prévias offline dos assets, sem efeitos adicionais do jogo ou sliders do usuário. [Importação reproduzível](qa/benihime/cycle74/audio/audio-import.json). [Log do reteste gráfico](qa/benihime/cycle74/validation/validation-cycle74-client-retest.log).

`git diff --check` e links documentais conferidos. Logs/rotações gerados na raiz pelos ensaios foram retirados do diff, preservando o conteúdo versionado anterior de `logs/debug.log`; os logs pertinentes à entrega foram arquivados em `Docs/poderes`. Alterações documentais dos ciclos anteriores preservadas. Sem commit/push/merge.

## Continuidade

Esta entrega trabalha somente o **item 1 obrigatório** do [roadmap 72](72-revisao-geral-roadmap-gamedesign-2026-10-20.md). Ele permanece aberto até a revisão artística/escuta do usuário e as validações pendentes identificadas. Correção de combate da espada Benihime segue no item 4; Hyōrinmaru/Blockbench na etapa posterior. Mentor/Proficiências aguardam equipe, Aizen adiado, Gin fora da fila de poderes.
