# 103 — Ryūjin: sol de chamas, selamento e transformação curta

25/10/2026. Planejamento e handoff ao Antigravity, sem implementação, build, execução do jogo, checkout ou criação de branch nesta rodada. Referência local: Feature-Poderes-bankais, árvore inicialmente limpa. A autorização é para preparar uma nova conversa e uma nova branch, no diretório existente; não criar outro clone/worktree/pasta de projeto.

## Evidências recebidas e limites

Vídeo original: `C:/Users/joaop/Downloads/Gravando 2026-10-25 150636.mp4`, 17,47 s, 1920×1080, 30 fps. Foram extraídos quadros a cada segundo e marcos adicionais em 0,3/1/1,3/1,6/1,9/2,2/3,2/9,2 s. [Prancha dos quadros](103-ryujin-referencia-frames-2026-10-25.jpg).

A gravação contém pausa, navegação e repetição do trecho; os 17,47 s não são a duração da transformação. Os quadros mostram silhueta diante de fogo monumental, crescimento rápido do fogo, clareamento/cessação e pose com braço baixo, lâmina carbonizada para fora do corpo. Não oferecem uma curva contínua confiável de todos os movimentos de punho. A absorção visível do sol por fios de fogo até a lâmina é a adaptação explicitamente pedida pelo usuário, não uma mecânica canônica demonstrada por estes quadros. Não confundir o vídeo de referência com evidência do mod funcionando.

Análise numérica dos WAVs com FFmpeg; silêncio detectado a -35 dB por pelo menos 80 ms, limiar indicativo, não alinhamento fonético certificado:

- `Zanka_no_Tachi_Bankai_fixed.wav`: mono PCM16, 192 kHz, 1,35 s; início acima do limiar em 0,148 s; silêncio final detectado a partir de 1,234 s. Média -17,3 dBFS, pico amostral -1,5 dBFS.
- `Zanka_no_Tachi_name_fixed.wav`: mesmo formato, 3,05 s; início acima do limiar em 0,248 s; intervalo baixo em 2,665–2,879 s, com energia depois. Não cortar a cauda automaticamente. Média -17,5 dBFS, pico amostral -1,5 dBFS.
- MP3s são alternativas; usar WAV como fonte de conversão, preservar originais. Não houve escuta subjetiva nesta análise. Cliques, inteligibilidade e equilíbrio com efeitos permanecem para teste auditivo.

## Direção proposta — 140 ticks / 7 segundos nominais

Substitui a sugestão anterior de dez segundos para esta entrada. É um alvo de implementação ajustável após escuta e preview, sem alongar a trava de gameplay.

1. **0–12 / 0–0,6 s:** câmera assume plano frontal em três quartos, suficientemente aberto para mostrar jogador inteiro e o sol. Jogador firma os pés, peito ereto, espada baixa e dirigida para fora. Fogo cresce atrás, preservando silhueta.
2. **12–30 / 0,6–1,5 s:** cue da voz “Bankai” no tick12; fonema esperado aproximadamente no15. Sol chega ao máximo, sustentado brevemente. Nada de órbita longa ou close que o esconda.
3. **30–52 / 1,5–2,6 s:** línguas da superfície do sol se curvam para a espada; colapso vai da periferia ao núcleo. Energia converge ao gume ao longo da lâmina, termina na ponta; fogo externo cessa. A fala Bankai completa seu arquivo aproximadamente no39. Poeira/cinzas seguem para cima, evitando uma explosão radial que conte a história contrária.
4. **52–60 / 2,6–3 s:** breve silêncio relativo. Lâmina inteiramente carbonizada, fumaça fina. Câmera se aproxima moderadamente e termina frontal em três quartos, corpo e espada inteira legíveis. Sem flash branco de tela inteira.
5. **60–124 / 3–6,2 s:** cue “Zanka no Tachi” no60; início audível previsto no65, arquivo completo no121. Sustentar a pose de Yamamoto, com respiração discreta e recuperação curta de punho. Legenda do nome aproximadamente65–124. Não iniciar o retorno no meio da fala.
6. **124–140 / 6,2–7 s:** câmera retorna à perspectiva original; pose perde peso até zero e libera combate normal. Cinzas/calor remanescentes discretos se dissipam. Sem pose permanente depois da entrada.

Legenda Bankai: aparecer por volta do15, saída suave até42; nome: entrada por volta65, saída até124. Afinar após escuta no jogo. Não reproduzir as duas falas simultaneamente.

## Sol e posição da arma

Sol cenográfico composto de volume/camadas de chamas, núcleo amarelo quente, corpo laranja e borda irregular vermelha. Ponto inicial para escala: diâmetro18 blocos, centro aproximadamente8 blocos atrás e9 acima dos pés; ajustar aos frames reais antes de fixar. Não é um círculo plano, disco amarelo uniforme, bola translúcida ou milhares de partículas sem limite. Distribuir fogo estruturado, turbulência lenta no núcleo e línguas mais rápidas na borda, com profundidade lateral suficiente para a câmera e observadores.

O plano do sol fica atrás do ator. Evitar partículas na face/empunhadura, clipping no solo e quadro saturado. Ancorar posição/orientação no início da cena: não perseguir câmera ou mouse. A câmera inicial deve mostrar o contorno completo ou uma escala deliberadamente monumental, sem cortar acidentalmente toda a coroa. Escurecimento local curto pode valorizar o fogo; mundo volta ao normal quando o fogo se sela. Não copiar piso/cortina/fundo de pétalas da outra Bankai.

Absorção deve usar âncoras reais da arma/pose, compartilhadas entre renderizadores. Não mirar no peito, na mão vazia ou na posição antiga do item. Na pose final, ponta se afasta do eixo do corpo no lado da mão armada; espelhar coerentemente para canhotos, sem trocar a empunhadura. Medir direções no modelo, conferir em frente e perfil. Não resolver só levantando o braço: transformações do item influem no eixo da lâmina.

## Aplicação técnica proposta

Reutilizar Forge/PlayerAnimator/Better Combat e renderização nativa já existentes. Estudar `RyujinChoreography`, `RyujinActionVisuals`, `ZanpakutoReleaseAnimation`, `RyujinFlamePresentation`, `RyujinReleaseEffects`, `ReleaseAnimationService`, `CinematicDirector` e os serviços de Benihime/Hyōrinmaru/Senbonzakura. Esses nomes existem no checkout de referência; revalidar caminhos na execução. Não instalar biblioteca por conveniência. Photon pode ser complemento posterior; o perfil base deve entregar a cena inteira.

Separar timeline, câmera, pose, áudio e campo de fogo. Curvas avaliadas com partial ticks, independentes do FPS; contagem/seleção de emissores determinística e limitada. Pose de transformação ganha prioridade apenas durante a cena e devolve controle às animações normais. Não editar as curvas aprovadas da Investida/Rajada para construir esta entrada.

Contrato local observado: Ryūjin Bankai usa release60/commit40. Preservar custo único, commit40 e trava60. A apresentação140 é camada própria, não motivo para tornar o release/busy140, criar invulnerabilidade, dano do sol ou incêndios. Explicitar cancelamento antes/depois do commit e a diferença entre sair da câmera e cancelar a forma. Não mudar forma adquirida após commit por simples restauração visual. Conferir como os serviços atuais tratam movimento/ataque, morte, desequipar, dimensão e desconexão; testes devem verificar o comportamento aprovado existente.

Sincronizar por instante inicial e idade autoritativa, com tratamento de observador tardio. Não tocar voz Bankai atrasada sobre a fala nominal. Falas locais não devem perder volume porque a câmera se afastou; observadores recebem áudio espacial apropriado, sem duplicação. Limpar todas as instâncias/estados ao sair do mundo.

Converter os WAVs para OGG Vorbis mono48k com encoder local disponível. Não usar MP3 como intermediário, não cortar início por detecção automática e não aplicar fade que coma fonemas. Manter margem de pico; medir true peak se usar limiter/gain. As vozes já têm picos altos, portanto não aplicar +4/+10 dB cegamente. Atenuar o rugido do fogo sob as duas falas, recuperar na absorção e baixar para o nome. Nenhuma música nova foi fornecida nesta rodada; não inventar download/trilha obrigatória. Caso reutilize trilha existente pertinente, documentar origem e ducking e conferir por escuta.

## Prompt para NOVA conversa do Antigravity

Você vai implementar SOMENTE a transformação cinematográfica da Bankai Ryūjin/Zanka no Tachi, conforme este documento103 e seus quadros. Leia-o integralmente e trate números de escala como ponto inicial de calibração, mantendo a direção artística e os contratos explícitos.

**Ambiente e branch:** trabalhe no checkout existente `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack`, não no checkout de design onde a outra conversa trabalha na Senbonzakura. Não criar outra pasta, clone, worktree ou nova conversa automaticamente. Leia AGENTS.md, skills pertinentes e continuidade. Verifique branch, HEAD, status e processos Java/Gradle. A base pretendida é `Feature-Poderes-bankais`; crie nela a nova branch `codex/ryujin-bankai-sol-selamento`. Se já existir, inspecione e retome o trabalho pertinente; não recrie/reset. Se houver mudanças alheias que impeçam a troca segura, preserve-as e informe o conflito, sem stash/reset ou commit automático. Não levar alterações locais de Senbonzakura para a branch nova. Não executar checkout em uso por outra conversa. A referência c4db442 era histórica durante a preparação; revalide HEAD, não force esse commit como base.

**Escopo:** sol monumental de chamas atrás do jogador → fala Bankai → absorção até a katana carbonizada → pose baixa para fora → nome completo → retorno. Alvo140ticks, cue12/60, respeitando os WAVs anexados. Preserve os kits aprovados, principalmente Investida/Rajada, Benihime, Senbonzakura, Hyōrinmaru e Zaraki. Não implementar aqui a troca de habilidade selada Benihime; é outra frente. Não modificar main, fazer merge/rebase, commit ou push nesta rodada.

**Fases de trabalho:**

1. Registre baseline, inventário dos controladores e contrato release60/commit40; identifique duplicação potencial de pose, som e partículas. Confirme dependências realmente carregadas. Leia103 e57/58/76/81 para não repetir os erros de orientação/pose permanente. Use102 apenas como referência de câmera/QA, sem modificar Benihime.
2. Faça prova vertical jogável com timeline140, sol, absorção, pose e retorno. Importe/converta as duas falas preservando originais. Se precisar de pequenos ajustes reversíveis de escala, faça e explique com comparação visual; não peça aprovação para cada parâmetro.
3. Refine movimento, profundidade/estrutura do sol, caminho das chamas até a lâmina real, carbonização, legendas, ducking e continuidade de câmera. Mostre pose frontal e perfil. A fonte de inspiração não exige skin de Yamamoto: precisa funcionar na skin do jogador.
4. Valide check/build, regressões pertinentes e GameTests do ciclo de transformação/custo/cancelamento. Rode Gradle sequencialmente. Coordene o uso do cliente com a conversa Senbonzakura: não feche o jogo alheio nem dispare outro QA na mesma instalação enquanto estiver em uso.
5. Execute Minecraft Forge real em save QA isolado. Capture marcos0/12/30/40/52/60/65/100/124/140 e após término. Teste dia/noite, primeira e duas terceiras pessoas, canhoto/destro, parede, terreno irregular, movimento/ataque durante cena, cancelamento antes/depois do commit, morte/troca de dimensão/desequipar, duas ativações e observador. Marque explicitamente cenários não executados. Não declarar multiplayer validado por singleplayer.
6. Ouça as falas com efeitos no cliente; se não houver capacidade auditiva real, peça avaliação ao usuário e registre pendente. Compare imagem da pose com os frames originais. Meça desempenho/orçamento de partículas, sem inventar FPS. Vídeo contínuo é preferível para fluidez; screenshots só comprovam marcos.

**Aceite:** sol atrás e estruturado; absorção legível, sem explosão que substitua o selamento; arma carbonizada para fora; fala completa; pose encerra; câmera e HUD restaurados; nenhuma alteração incidental de custo/dano/recarga/weather/blocos; perfil base funciona sem Photon. Entregue JAR identificável, hashes, comandos/exit codes, evidências e pendências em Docs/poderes, sem auto-homologação artística.

## Continuidade desta frente

Usuário validou entrada Benihime e Investida/Rajada em25/10; Higashi será validada depois. Troca da boneca selada Benihime está solicitada, alternativa ainda em discussão. Este plano não implementa essa troca nem marca os sete itens obrigatórios como concluídos. Nenhuma ferramenta nova foi instalada; somente FFmpeg/Python locais foram usados para análise e extração.
