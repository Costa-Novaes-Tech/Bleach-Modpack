# Ryūjin v11 — bateria de testes, iluminação e divisão das frentes

> **Divisão revisada pelo usuário em 06/11:** toda a Ryūjin fica com o Codex; Antigravity A assume Quincy/Vollständig na nova branch e B permanece em P0/Rukia. Ver [divisão vigente e checkout preparado](divisao-frentes-v11-2026-11-06.md). Os gates técnicos abaixo continuam válidos; a delegação anterior de VFX Ryūjin à frente A foi substituída.

**06/11/2026.** Planejamento revisável na `Feature-Poderes-bankais`, HEAD `9eb6a05`, com trabalho v9/v10 ainda local e não commitado. Não equivale a uma bateria executada. Base técnica anterior: **177 regressões, 295 GameTests, um cliente day_back de 15 s e conferência Blockbench v10**; resultados e limites no [relatório v10](ryujin-v10-duracao-sustentacao-sol-2026-11-05.md). Sem merge, push, mensagens externas ou novas instalações nesta revisão.

## Contrato preservado e decisão nova

Manter **300 ticks / 15 s**, sol completo de **112 a 176 / 3,2 s**, absorção 176–202, nome 220 e retorno 270–300. Movimento liberado em 60, commit/custo em 40. A proteção existente acompanha a cena e cancela segundo as regras de combate; não alterar outras Bankais nesta frente. A quantidade de frames de referência não determina duração.

**Decisão explícita do usuário em 06/11:** suspender a criação de **fogo físico** durante a apresentação, conservar o fogo visual e retomar a emissão passiva quando a cena autoritativa terminar ou for cancelada. Preservar o ciclo de vida de fogo já existente, inclusive de outros jogadores. A simples liberação local da câmera por movimento não encerra a cena do servidor; usar o estado autoritativo existente. Não converter essa decisão numa remoção geral de fogo da Bankai em combate.

**Execução posterior:** consultar [v12 — luz, distribuição e desempenho](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md) para o estado atual. O diagnóstico abaixo descreve a versão anterior à reconstrução; não é uma lista de defeitos ainda todos presentes. A divisão final é Codex Ryūjin / Antigravity A Quincy / B P0-Rukia.

## Diagnóstico do acabamento — evidência de código, não só preferência

Em `RyujinSunVisual.render`:

- A lâmina recebe **18 cartões**, com altura aproximadamente **0,16–0,32 bloco** vezes a ignição. Isso dá uma borda fina, insuficiente para o revestimento de chamas pedido pelo usuário.
- O campo da fala usa 48 cartões e desaparece após o começo da explosão. A cortina da explosão usa 84 cartões, com âncoras que sobem, e seu envelope termina no beat autoral **31**, no meio da sustentação estendida. Não existe uma cortina traseira baixa e densa sustentada por toda a fase do sol.
- Permanecem 36 cartões na coroa elevada, 32 na base e 28 no turbilhão de até 3,6 blocos ao redor do corpo. Essa distribuição ajuda a explicar a concentração visual sobre o personagem e os vazios atrás dele. É uma explicação estática a confirmar com camadas isoladas no cliente.
- `emit` calcula a seed a partir de `(long) age`, sendo `age` o relógio autoral. Entre 112 e 176 ele avança somente de 30 a 32: pode repetir os mesmos pontos de emissão por vários ticks. Separar **progresso de fase** de **movimento/seed por tempo real** no próximo ensaio.
- `MAX_FIRE_CARDS=288` é usado como capacidade inicial de `ArrayList`, não como corte rígido da lista. Antes de aumentar densidade, medir/enforçar quotas por camada; não presumir que a constante já limita tudo.

**Origem adicional de fogo no encerramento:** `RyujinReleaseEffects.release` solicita até oito colocações físicas na entrada; `aura` solicita até três a cada 40 ticks após o release. `SpiritFlameService` coloca blocos reais, com vida de 100 ticks, persistência e dano de contato. Há, portanto, uma fonte de fogo de gameplay que independe do emissor cênico. Isso não prova a origem de cada pixel dos frames finais, mas invalida a hipótese de que todo fogo restante seja apenas partícula esquecida.

Foi acrescentada a condição de cena inativa **somente nas duas chamadas de emissão física**. Partículas/som visuais, dano do fogo existente, expiração e persistência de `SpiritFlameService` ficam preservados. Novo GameTest verifica suspensão de release/aura, preservação de dois fogos preexistentes e retomada após cancelamento. Resultado próprio registrado ao fim da execução; ainda é necessária conferência visual no cliente.

**Resultado executado da correção T0:** `check build runGameTestServer` exit 0, **177 regressões e 296/296 GameTests obrigatórios**, incluindo `ryujin_scene_world_fire`. Log `build/visual-lab/v11-world-fire-gate.log`, mundo descartável configurado pelo init script. Nenhum resultado T1–T6 foi produzido por essa execução; a nova iluminação e os efeitos de maior densidade continuam propostas.

**Cliente da correção T0:** caso `day_back` em mundo QA novo concluiu com exit 0, `releaseBusy=false`, `scene={}`, `form=bankai` e `BUILD SUCCESSFUL`; log `build/visual-lab/v11-world-fire-client.log`. Os frames [t202](qa/codex/v11-world-fire-gate-20261106/frame-t202.png) e [t300](qa/codex/v11-world-fire-gate-20261106/frame-t300.png) conferidos mostram o selamento e a devolução do controle sem os blocos de fogo presentes nesses mesmos marcos da v10. A preservação/retomada foi provada pelo GameTest; esses dois frames sozinhos não provam dano, continuidade de movimento ou mixagem. [Manifesto com fonte/JAR/hashes](qa/codex/v11-world-fire-gate-20261106/manifest.json). Este é um subconjunto executado de T0; flags para isolar todas as camadas ainda precisam ser construídas.

## Como reconstruir a distribuição

Coordenadas relativas ao ator: `p = pés + direita*x + cima*y − frente*d`, em blocos. Profundidade positiva `d` fica **atrás**. Mão/lâmina usam as âncoras reais do item, inclusive canhoto/slim. Medidas abaixo são **pontos de partida de protótipo**, sujeitos a inspeção, não valores já homologados.

1. **Revestimento da lâmina:** continuidade em pelo menos 80% do trecho cortante, com emissão da guarda à ponta, línguas de aproximadamente 0,35–0,9 bloco e variação temporal. Reduzir altura perto da mão/tsuba. Provar primeiro que o fogo segue o item correto, depois aumentar densidade; não usar a cabeça como origem.
2. **Base traseira:** fogo ligado ao chão, principalmente atrás/lados, cobrindo vazios entre ator e cortina. Referência inicial: largura 6–10 blocos, profundidade 1–5, alturas baixas e irregulares. Frente abaixo da cintura, pés e espada reconhecíveis.
3. **Cortina sustentada:** massas sobrepostas entre profundidades 3–7, largura 10–15 e alturas variadas, ligando o chão à região inferior/lateral do sol. Sustentar até o começo da absorção, com pulsos e circulação sem congelar os pontos de emissão.
4. **Coroa solar:** conservar escala e giro aprovados como direção; completar bordas e vazios com fogo, sem concentrar todo o orçamento acima do jogador. Não cobrir o disco inteiro com um único cartão opaco.
5. **Absorção:** contrair fluxos de cada camada para a lâmina, desligar as fontes cênicas e atenuar sua luz. No fim, deixar fio de brasa/cinzas. Distinguir isso da retomada de fogo físico de gameplay depois da cena.

Reusar sprites/atlas e curvas antes de multiplicar partículas. Manter o orçamento atual de partículas, com limite global e por cena, até medir. Acrescentar quotas e contadores de cartões por camada; priorizar lâmina/cortina e reduzir detalhe periférico em perfil baixo. Seed deve usar cena/dono/tick real de início e tick real de emissão, independente de FPS; curvas de fase continuam no relógio compartilhado.

## Sol como fonte de luz — objetivo e caminho técnico

**Requisito novo registrado:** iluminar visualmente chão, paredes e personagens próximos a partir do sol e acompanhar sua formação/absorção. Brilho da textura, bloom e iluminação do ambiente são resultados diferentes. O `FULL_BRIGHT` atual não cria essa fonte de luz no mundo.

**Primeiro candidato de laboratório: Shimmer Forge 1.20.1, release 0.3.0.** A API pública expõe criação/remoção de luzes pontuais, posição, cor e raio: [LightManager da branch 1.20](https://github.com/Low-Drag-MC/Shimmer/blob/1.20/Common/src/main/java/com/lowdragmc/shimmer/client/light/LightManager.java), [ColorPointLight](https://github.com/Low-Drag-MC/Shimmer/blob/1.20/Common/src/main/java/com/lowdragmc/shimmer/client/light/ColorPointLight.java). São fontes de pesquisa; conferir os símbolos no artefato fixado antes de integrar. Não há instalação nem teste Shimmer nesta rodada.

O [release 0.3.0](https://github.com/Low-Drag-MC/Shimmer/releases/tag/1.20.1-0.3.0) registra correção de compatibilidade com **Oculus 1.7.0**. O [relato antigo #125](https://github.com/Low-Drag-MC/Shimmer/issues/125) envolve **Shimmer 0.2.4/Oculus 1.7.0/Embeddium 0.3.28**. Não declarar o conflito inevitável hoje nem assumir que a correção homologa nosso **Oculus 1.8.0/Embeddium 0.3.31/Complementary r5.9.3**.

Prova mínima: uma luz pontual presa ao centro real do sol, paleta quente, intensidade/raio pela fase e remoção idempotente por cena. Ensaiar raios 12/18/24 blocos, começando com **uma** luz; fontes auxiliares somente se necessárias e medidas. Evitar uma luz por partícula. Luz deixa de existir no selamento, cancelamento, logout, dimensão, reload ou descarregamento do dono; ausência da biblioteca conserva o renderer base.

Perfis separados: base → Shimmer isolado → Shimmer/Embeddium → combinação com Oculus sem shader → Complementary ativo. Avançar só após sucesso da etapa anterior. Essa expansão pertence ao ponto **2** já escolhido. Não instalar todos os backends de luz juntos. O teste de parede deve revelar vazamento/ausência de oclusão; luz pontual colorida não garante sombras físicas novas.

**Alternativa limitada:** a [API RyoamicLights](https://github.com/ThinkingStudios/RyoamicLights/wiki/API) registra luminância 0–15 de entidades/itens e tem mudanças de entrypoint conforme a versão. É candidata a iluminação visual convencional, não prova de luz colorida do nosso sol, que hoje é renderizado sem entidade própria. Exigiria adaptador e ensaio exato de Forge/loader. Só abrir essa alternativa se a prova principal falhar num critério concreto.

Não usar alteração de dia/clima, Night Vision ou colocação de blocos de luz no save como substituto desse efeito visual. A iluminação cênica não deve alterar spawn, crescimento, dano ou terreno. O fogo físico de combate permanece uma responsabilidade separada.

## Bateria executável e gates

Cada ensaio tem branch/HEAD, diff/hash do JAR e assets, perfil completo, cenário/seed, hora, resolução, render distance, estado de particles, comando, exit code e evidência bruta. Vídeo contínuo com áudio para ritmo/mixagem; capturas individuais para ângulos/marcos. Medir frametime real, não deduzir p95 de amostras esparsas de FPS.

**T0 — causas e suspensão física.** Flags de laboratório para observar isoladamente lâmina/base/cortina/coroa/turbilhão/partículas/luz. Comparar lado a lado no mesmo estado, sem desligar todos os efeitos do cliente. GameTest da suspensão física e preservação/retomada; identificar separadamente blocos de fogo e partículas. Aceite: nenhuma nova colocação física pela entrada/aura enquanto a cena estiver ativa; comportamento de combate retoma pelos ciclos existentes.

**T1 — composição base.** Frente, 3/4, ambos perfis e observador traseiro; quatro direções do mundo; mão direita/esquerda e padrão/slim. Marcos reais 40,70,84,112,144,175,176,190,202,220,270,300. Aceite: revestimento contínuo na lâmina, maior massa conectada ao solo atrás do ator durante toda a sustentação, frente variável abaixo da cintura, rosto legível, sol inteiro e mesma pose/câmera de 15 s. Provar expansão em profundidade com perfil, não só pela imagem frontal.

**T2 — iluminação e shader (ponto 2).** Noite em arena com piso neutro, parede e blocos de referência em distâncias conhecidas; repetir de dia. Mesma câmera/exposição. Comparar luz ligada/desligada com bloom desligado e fonte fora do enquadramento: superfícies devem mudar, não apenas o halo na tela. Provar queda com distância e limpeza após selamento. Registrar se há oclusão, vazamentos e luz em outros personagens. Aceite parcial separado para base, Shimmer e shader; renderer carregado não basta.

**T3 — Photon × AAA/Effekseer (ponto 3).** Mesma lâmina/cortina/coroa, paleta, duração, seed e intensidade aproximada; um backend por camada/evento. Guardar fonte editável e export realmente reproduzido no cliente. Incluir cold load separado do replay aquecido, transparência, cancelamento, reload, dois donos e remoção. A escolha é pela melhora observada, não pelo nome da biblioteca. Fallback base continua disponível.

**T4 — ciclo de vida e rede.** Cancelar antes/depois de commit, atacar/esquivar/defender/usar técnica, mover/liberar só a câmera, morrer, trocar dimensão, sair/voltar e reload de recursos. Servidor dedicado com um dono e um observador; depois dois donos simultâneos. Aceite: sem duplicação de custo/voz/emissão, sem câmera forçada no observador, sem luz órfã e fogo físico suspenso apenas durante a cena do respectivo dono. Testes de imortalidade/vida útil mantidos.

**T5 — cosméticos e carga (ponto 4).** Skin Layers 3D/NEA separados e juntos no Forge empacotado, com PlayerAnimator/Better Combat. Conferir propriedade dos braços/arma, slim, mão esquerda e retorno ao idle. Somar 1→2→4 cenas apenas após estabilizar uma. Rodar três replays aquecidos por perfil em 1280×720, distância de render 6 e configuração idêntica. Registrar p50/p95/p99, memória, instâncias/luzes/cards e partículas. Meta provisória de custo: até 20% de aumento de p95 sobre baseline estável; discutir meta absoluta após medir o hardware. Não aprovar um baseline já impraticável só por variação percentual pequena.

**T6 — som e acabamento.** Música de 12 s em sequência de 15 s: decidir versão estendida/trecho instrumental, sem time-stretch automático da voz. Capturar loopback do áudio do jogo localmente, verificar clipping/ducking e conferir perceptualmente a mixagem. Direitos e atribuição da música ainda precisam dos termos de distribuição. Fotografias e eventos de áudio em log não são prova de escuta.

Depois de cada mudança: regressões direcionadas → check/build → GameTests pertinentes → cliente e evidência. Repetir suíte completa em pontos de integração, não a cada parâmetro de textura. Nenhum resultado da v10 deve ser reapresentado como T0–T6 executados.

## Divisão de trabalho recomendada

**Codex:** integração Ryūjin, densidade/âncoras, relógios de fase/movimento, limpeza, luz dinâmica e compatibilidade gráfica. Responsabilidade pelos arquivos Java produtivos Ryūjin, adaptador de iluminação e integração final dos assets. Manter `BleachClient`, sincronização, Gradle e regressão compartilhada sob um responsável por vez.

**Antigravity A — Quincy/Vollständig:** [prompt vigente](prompt-antigravity-v11-quincy-vollstandig-refino-2026-11-06.md). Nova branch própria derivada da de poderes, com imagens-modelo individuais e pares liso/em blocos, refinamento de rig/texturas/clipes e prova dos exports no runtime. Pode avançar em paralelo ao trabalho Ryūjin do Codex, sem editar seus módulos. A regra anterior de A produzir VFX Ryūjin está superada. Editor/MCP Antigravity 8787, Codex 8788; não presumir acesso local em nuvem.

**Antigravity B — P0/Rukia, recomendado para avanço paralelo de lore:** [prompt pronto](prompt-antigravity-v11-p0-rukia-fixtures-2026-11-06.md). Auditoria pós-merge, fixtures sintéticas/copiadas autorizadas, relog/clone/claim/coop e proposta de fatia mentor/skill com requisitos ainda abertos. Sem conversão de ranks, novos preços/recompensas ou duplicação de MAIN-SUB-01. Essa frente pode avançar sem esperar a iluminação; uma IA só deve começar A e B simultaneamente se tiver isolamento e capacidade reais.

**Fila posterior do Antigravity:** compatibilidade cosmética T5; depois auditoria focada Garden/Hakuteiken dos riscos já registrados. Sem reabrir Benihime homologada ou alterar poderes por conveniência. Só iniciar trabalho produtivo novo quando a responsabilidade e o escopo estiverem atribuídos.

Cada executor usa branch/checkout próprio acordado e registra base; não reseta/stasha alterações compartilhadas. Commits locais nas branches de frente apenas no escopo autorizado pelo usuário ao encaminhar o prompt. **Sem merge automático destas novas frentes**, sem main e sem mensagens externas. A autorização antiga de integração de outros retornos não implica integrar estes trabalhos futuros.

Os arquivos v10 e este plano ainda não estão em commits: outro worktree/cloud não os recebe automaticamente. O prompt contém o relógio mínimo; fornecer o pacote de documentos/fixtures necessário ou aguardar um checkpoint explicitamente autorizado. Não sobrescrever a árvore atual para criar esse checkpoint.

## Roadmap de produto preservado

Ordem visual **2 → 3 → 4 → 1** mantida: diagnóstico/composição base como preparação controlada; luz/shader; comparação de VFX; cosméticos; arqueiro Quincy. O sol como fonte de luz passa a ser critério explícito de T2, ainda sem homologação.

Em paralelo: complementar **P0** pós-Rukia e validar persistência. **P1** aquisição por SP e **P2** mentor piloto dependem de mapa de ranks, política de recompensas de Mastery, ficha de skill/fonte/custo e fixture de relog; **P3** é aceite humano/coop de MAIN-SUB-01, não implementá-la de novo. Preservar cinco atributos universais, ausência de nível global, SP para compras e Mastery por prática; sem novas regras de progressão nesta revisão.

Contexto XERJ preservado: preferência GPL-3.0, escopo/atribuição por esclarecer, nenhuma autorização MIT ou e-mail enviado presumidos. Instalar bibliotecas de terceiros não concede direitos sobre assets/código do concorrente.
