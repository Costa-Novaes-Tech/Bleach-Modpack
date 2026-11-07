# BleachCraft × Ryūjin v13 — comparação atual e próximo acabamento

> A ordem integral de execução, métodos e critérios de cada sugestão estão consolidados no [plano completo v14](ryujin-v14-plano-completo-ordem-execucao-2026-11-06.md). Este documento preserva a análise visual e os requisitos das referências; o plano não significa implementação já feita.

06/11/2026 · `Feature-Poderes-bankais` · HEAD `9eb6a057`, com mudanças locais. Revisão de vídeos fornecidos pelo usuário e da captura real v13g. **v14 é proposta, não implementação.** Nenhum novo cliente/Gradle/Blockbench foi executado nesta análise; frentes Antigravity, runtime, modelo padrão e saves preservados.

## O que foi revisto

Nova extração via `tools/media_qa/inspect_media.py`: 18 amostras Hollow entre 34,4–41,2 s; 18 do trailer entre 36–86 s; 21 da nossa cena e 14 adicionais do selamento entre 9,4–11,3 s. **71 amostras temporais**, incluindo tempos repetidos, não 71 frames consecutivos nem análise integral de todos os frames. [Manifesto, hashes, contatos QA e 14 imagens selecionadas](qa/codex/comparacao-bleachcraft-ryujin-20261106/manifest.json). As demais extrações ficam em `build/visual-lab/reference-review-v14`.

Hashes das duas referências conferem com a análise de 05/11; o vídeo Ryūjin é o v13g silencioso, SHA-256 `8eaaaf4e47eb170e163a137e905df46299d3a4b1e4c0034de38213bb63e3140b`. Referências: 576×1024/30 fps, com edição vertical e áudio AAC. Ryūjin: 1280×720/20 fps, 16,3 s, 317 frames aceitos, três descartes e nove repetições na captura, sem áudio. Resolução, recortes e natureza promocional diferentes impedem comparar FPS, tamanho relativo de tela ou nível global do produto. Não houve escuta nesta revisão; propostas sonoras abaixo são da nossa direção, não avaliação auditiva do concorrente.

## Comparação visual e avanço

| Dimensão | Referência observada | Nosso estado atual e diferença restante |
|---|---|---|
| Massa principal | Hollow 35,2–36,4 s: anéis luminosos; 38,4–39,6: esfera branca/vermelha com faixas e fragmentação. Trailer 68 s: esfera ciano com borda e rastros. | Agora temos uma massa solar coesa, com luz e profundidade, em vez de depender só de raios/partículas. O centro ainda é relativamente uniforme e os aros têm regularidade mecânica. |
| Direção do efeito | Trailer 82–84 s: faixas ciano alongadas e término luminoso; na esfera de 68 s há rastros externos que tornam uma direção visível. | A Ryūjin tem convergência programada para a lâmina, mas a leitura final ainda é dominada pela redução do disco e chamas subindo. Melhorar continuidade/orientação do fluxo. |
| Ritmo | Hollow 34,4–36,8 s alterna anéis, intensificação e desaparecimento; 37,2–41,2 alterna revelação, esfera e exposição da forma. Existem cortes de edição. | Nossa cena já demonstra preparação, explosão, sustentação, absorção e retorno no mesmo ensaio. A sustentação possui pouca variação interna e o fechamento carece de uma pontuação própria. Não importar os cortes do trailer para esconder transições jogáveis. |
| Câmera e foco | Trailer 72–76 s valoriza rosto; depois há plano aberto e disparo. O leque dourado de 60–62 s fica atrás de uma silhueta escura. | Temos close frontal, abertura completa do sol e retorno. Falta transferir o foco do corpo para mão/guarda/lâmina no selamento. Na amostra 11 s, a espada escura ocupa pouco do plano e se mistura ao chão. |
| Hierarquia e contraste | Esferas/faixas têm núcleo claro, bordas coloridas, rastros e intervalos escuros. Algumas amostras brancas ocultam o personagem. | Unlit eliminou grande parte do contraste indevido das faces. Próximo ganho é organizar claro/escuro e transparência das chamas para preservar rosto/arma; aumentar brilho indiscriminadamente apagaria essas conquistas. |
| Fogo/fragmentos | Hollow 39,6 s mostra faixas separadas da esfera; trailer 66–68 s alterna fragmentos ciano e massa concentrada. | A cortina Ryūjin é volumosa, porém os sprites vanilla ampliados revelam repetição. Precisamos de línguas agrupadas e poucos rastros maiores, com partículas pequenas como acabamento. |
| Ambiente e luz | Cidade, floresta e deserto dão contraste e escala nas referências. Vídeos não identificam a implementação da luz/câmera. | Shimmer ON/OFF já provou luz em nossas superfícies neutras; o sol também tem material próprio. Mundo QA plano e uniforme não serve para julgar o acabamento ambiental de uma cena urbana com sombras. Falta comparação artística em arena controlada com profundidade. |
| Personagem | As referências alternam corpos/armas/formas e poses identificáveis. Não se infere o rig ou biblioteca de cada trecho. | Nossa pose de katana e mão real fazem parte do contrato. Refinar antecipação de ombro/punho, pegada e assentamento sem substituir a skin ou inventar uma pose incompatível. |
| Entrega jogável | Os trailers mostram momentos selecionados. Não comprovam cleanup, cancelamento, desempenho ou multiplayer. | Nossos registros v13 têm matriz, limites e restauração específicos. Isso aumenta confiança técnica, mas não autoriza declarar superioridade visual ou equivalência global ao BleachCraft. |

**Conclusão desta revisão:** aproximamos a presença e a coerência do efeito central. A maior diferença restante está no desenho temporal dos efeitos secundários, na variedade das superfícies e na condução da atenção até a espada selada. Não é necessário reconstruir novamente todo o sol antes de testar essas melhorias.

## Diagnóstico mais específico do selamento

Na captura atual, t176 corresponde a 8,795 s, t190 a 9,487 s, t202 a 10,104 s e t220 a 10,991 s, obtidos por `frames.jsonl.wallMs - video-capture.wall_start_ms`. Há precisão limitada ao relógio de captura/amostragem; não usar segundos de vídeo como substituto universal dos ticks da cena.

Nas amostras 9,4–9,7 s o núcleo contrai; em 9,8–10,0 predominam a chama junto à arma e partículas acima dela; por 10,1–10,2 a massa quente já desapareceu. Em 11 s o plano se aproxima, mas continua priorizando corpo/rosto. O vazio entre a perda da grande massa e a apresentação convincente da lâmina ajuda a explicar a sensação anticlimática relatada pelo usuário.

Inspeção de código: `RyujinFireLayout` já gera 32 cards em quatro correntes de absorção até pontos da lâmina; `RyujinSunVisual.emit` também dá velocidade inicial dirigida a partículas. **Não falta toda a lógica de convergência.** Porém `Card` não contém orientação/tangente e o renderer usa `up=(0,1,0)` para todos os sprites. Isso preserva línguas verticais mesmo ao longo de uma corrente diagonal. A correção proposta é orientar a geometria de fluxo ou usar ribbons; sua eficácia visual ainda precisa de teste. Fogo ambiente vertical pode continuar adequado.

## Sugestões de acabamento, por prioridade

| Ordem | Proposta | Como testar e limite |
|---|---|---|
| P0 — pré-fala | **Microchamas no olho direito e rastro lateral curto**, durante o enquadramento anterior a “Bankai”, conforme solicitação humana posterior. | Direito anatômico, independente da mão da espada; origem segue a cabeça. Conferir frente/perfis, yaw/pitch e oclusão. Detalhamento e referências abaixo; ainda só planejamento. |
| P0.1 | **Selamento com correntes curvas:** reaproveitar a camada de absorção para duas/quatro faixas afiladas orientadas pela trajetória, passando da corona/cortina à lâmina; pequenas partículas acompanham, em vez de substituir a faixa. | Primeiro mostrar só fluxo e katana, depois conjunto. Ver origem/destino, tangente, costuras, oclusão e mão espelhada. Reutilizar orçamento antes de acrescentar instâncias. Não prolongar sol/fogo/luz após o cutoff. |
| P0.2 | **Câmera da zanpakutō:** foco derivado da mão/guarda/lâmina, aproximação frontal deslocada para o lado da arma e pausa curta na espada selada. | Destro/canhoto, frente/perfil diagnóstico, primeira/terceira pessoa e parede. A câmera deve chegar antes de perdermos o último fluxo. Não mudar o compromisso frontal para órbita traseira sem nova direção humana. |
| P0.3 | **Última brasa e queda de energia:** última passagem luminosa na lâmina até t202; depois poucas cinzas/fumaça fria e um assentamento de punho. | Detalhe visível antes/depois do fim; rosto e mão sem cobertura. Esclarece a proposta anterior: depois de t202 não manter brasa quente, FLAME nem luz solar. Efeito frio tem cotagem e limpeza próprios. |
| P1.1 | **Fogo com agrupamento orgânico:** variar desenho/tamanho/fase entre poucos grupos, com línguas grandes atrás e baixas à frente; estudar atlas/flipbook autoral para substituir parte do sprite repetido. | Mesmos cards/área primeiro, camada isolada e depois composição. Variação deve ser contínua e estável por cena, sem reposicionar aleatoriamente tudo a cada frame. Preservar mão/face e acesso ao chão. |
| P1.2 | **Convecção e corona solar:** movimento lento de material no núcleo, irregularidade discreta nas faixas, pequenas línguas periféricas e alternância de opacidade/velocidade. | A/B de material com geometria fixa; depois ajuste de movimento. Núcleo legível sem bloom, silhueta/escala preservadas e sem novos raios rígidos. Não confundir fluxo de textura com luz de ambiente. |
| P1.3 | **Explosão com expansão e assentamento:** uma onda baixa de pressão/calor no solo e pulso curto de luz/FOV no impacto, seguido de brasas e poeira/cinza. | Desenhar a expansão radial sem atravessar o corpo nem criar fogo/dano/terreno novos. Conferir terreno irregular e colisão. Câmera de observadores permanece livre; evitar tremor sustentado durante todo o sol. |
| P1.4 | **Mixagem em três massas:** pré-aquecimento discreto → explosão/rugido → sucção/selamento seco e respiro. Duck de música/ambiente nas falas; queda do rugido valoriza o final. | Captura da mixagem real e escuta humana, com relógio dos cues. Música 15 s e direitos dos stems continuam decisões separadas. Não classificar áudio a partir desta prévia silenciosa. |
| P2.1 | **Distorção de calor localizada e bloom seletivo:** calor em volta/atrás do sol e área quente; halo moderado, sem lavar núcleo/rosto. | Piloto separado, só após destravar o perfil gráfico. Máscara exclui HUD/legendas; fallback base. Tint vermelho existente não comprova refração/distorção de calor. Compatibilidade e custo ainda desconhecidos. |
| P2.2 | **Arena de acabamento:** repetir a cena em pequeno cenário QA com fundo, pilares e chão irregular, dia/noite, além do plano neutro. | Mesmos marcos, câmera, resolução e preset; comparar com luz desligada/ligada. Não substituir a prova neutra por uma tomada bonita que esconda defeitos. |

Implementar todas as propostas simultaneamente impediria atribuir ganhos/falhas. Ordem sugerida: P0.1/P0.2 juntos para a prova vertical do selamento → P0.3/áudio → fogo/material/explosão → perfil gráfico/ambiente. Comparação Photon 1.x × AAA/Effekseer fica limitada a um efeito equivalente por vez, sem reescrever a cena nem supor qual biblioteca o concorrente usa. Nenhuma dependência nova é necessária para começar a prova de câmera/fluxo no renderer atual.

## Adição humana — olho direito e novas referências de enquadramento

**06/11, somente planejamento.** O usuário acrescentou pequenas partículas de chama no **olho direito do jogador**, com um rastro saindo para seu **lado direito**, antes da fala “Bankai”. Antigravity já está trabalhando nas próprias frentes; este acréscimo pertence exclusivamente à Ryūjin/Codex. Não foram alterados runtime, modelos, áudio, testes ou configuração do editor.

### Referencial e desenho do efeito

- Direito é **anatômico do personagem**. Na tomada frontal com cabeça alinhada, o olho/rastro aparecem à **esquerda da imagem**. O efeito permanece no olho direito mesmo quando a katana é empunhada pela mão esquerda; não espelhar junto com a arma.
- Origem presa ao ponto do olho na cabeça renderizada, seguindo posição/rotação da cabeça e postura do corpo. A direção do rastro acompanha o lado direito local da cabeça, junto à têmpora e para fora, com leve subida. Não usar eixo fixo do mundo ou posição fixa da tela.
- Desenho proposto: ponto de ignição pequeno, duas/quatro microchamas intermitentes e uma cauda curta afilada, com poucas brasas que perdem brilho ao se afastarem. Núcleo amarelo quente, borda laranja/vermelha; preservar o outro olho e a expressão.
- Valores **iniciais de ensaio**, não medidas extraídas das imagens: chama perto de meio/um pixel de modelo (aprox. 0,03–0,06 bloco); cauda de 0,2–0,4 bloco; até oito partículas vivas desta camada, dentro da cota própria da cena. Ajustar por leitura no close, evitando uma tocha grande no rosto.
- A emissão deve ser um sinal delicado de concentração, com atividade irregular suave e sem rajada semelhante a projétil. Se pontos soltos não descreverem a cauda, experimentar uma faixa muito fina em complemento; manter as pequenas partículas pedidas como elemento principal.
- Material emissivo localizado; não criar outra fonte de luz de ambiente para este detalhe. Manter a skin existente e conferir posição em skins com olhos deslocados, camada externa, cabelo e capacetes. Respeitar profundidade/oclusão; não prometer alinhamento universal por um único offset.
- Encerrar a emissão e limpar o rastro na transição para a explosão, no cancelamento ou na saída da cena. Na visão local em primeira pessoa, impedir que partículas coladas à câmera cubram a tela; observadores mantêm sua própria câmera e a apresentação do ator em mundo.

### Ritmo proposto na cena de 300 ticks

São **ticks reais**, sujeitos ao próximo storyboard/teste. Preservar fala em40, explosão principal em70, sol112–176, selamento até202 e retorno270–300; não ampliar novamente a duração só por acrescentar este detalhe.

| Intervalo | Plano e ação |
|---|---|
| 0–20 | Aproximação frontal: postura estável, katana baixa/lateral para fora e braço legível. Preparar espaço junto ao lado direito do rosto. |
| 20–32 | Primeiras microchamas no olho direito enquanto a tomada se estabelece; o rosto assume prioridade sobre o fogo de fundo. |
| 32–39 | Close frontal de rosto/ombros, com espaço lateral para ler o rastro. Intensificação discreta antes da fala, sem cobrir o rosto. |
| 40–52 | “Bankai”: olho já aceso, brilho curto no acento da fala; conservar a expressão e sincronizar o pré-impacto existente. |
| 52–70 | Rastro afina e desaparece enquanto a composição prepara explosão/abertura. O olho não vira uma segunda fonte dominante durante o sol. |
| 70–176 | Explosão → plano aberto frontal elevado → sol completo, corpo e chamas traseiras. Preservar o desenho principal aprovado como evolução. |
| 176–220 | Absorção dirigida à lâmina, câmera frontal da arma e pausa no selamento. A pequena assinatura do olho não substitui esse clímax. |

O close facial precisa de ensaio próprio, além do close posterior da lâmina. Reservar composição/tempo de atenção; não obrigar rosto, espada inteira e sol inteiro a caberem simultaneamente em todos os planos. A nova cadência inicial deve ser amostrada para velocidade/colisão antes de alterar a câmera existente.

### Função das três imagens recebidas

Originais preservados sem edição, com fontes e SHA-256 em [manifesto das referências](media/ryujin-v14-referencias-enquadramento-20261106/manifest.json). As imagens informam composição; a instrução do **olho direito** vem do pedido textual humano, não de uma medida ou efeito supostamente comprovado nelas.

1. [Referência 1 — pose frontal e chamas](media/ryujin-v14-referencias-enquadramento-20261106/ref-01-pose-frontal-chamas.png): usar estabilidade da postura, arma para fora, figura escura recortada pelo fogo e bordas quentes. Propor plano frontal médio para fala/pose, mantendo mão/guarda reconhecíveis; o novo close facial pode anteceder esse plano.
2. [Referência 2 — lâmina em chamas](media/ryujin-v14-referencias-enquadramento-20261106/ref-02-lamina-em-chamas.png): estudar empunhadura, eixo da espada e espaço livre no terço inferior. A lâmina precisa aparecer no enquadramento do seu próprio beat, inclusive no detalhe de selamento.
3. [Referência 3 — corpo inteiro e coroa solar](media/ryujin-v14-referencias-enquadramento-20261106/ref-03-sol-coroa.png): usar separação entre ator e massa traseira, coroa inteira e área de fogo no chão para o plano aberto. Preservar o núcleo solar atual; a imagem é referência de composição/escala, não decisão de trocar o sol por um portal vazio ou adicionar roupa/personagem novo.

Para novas folhas de planejamento futuras: arquivos separados de close facial frontal, perfil direito da cabeça/rastro, 3/4 diagnóstico, plano médio da katana, plano aberto solar e detalhe do selo, com mesmo ator/pose entre vistas. Não gerar imagens, modelar ou codar nesta rodada. O critério do próximo teste será reconhecer primeiro o olho/rastro, depois a fala/explosão, depois o sol e por fim a espada selada, sem competição constante entre todos os efeitos.

## Aprendizado transferível e estado

[Guia consolidado de transformações](guia-transformacoes-modelagem-vfx-qa-2026-11-06.md) reúne os testes já executados, as decisões reutilizáveis e as ligações aos artefatos. Skills de desenvolvimento/modelagem apontam para esse guia. A comparação atual informa propostas; **não demonstra ainda** que ribbons, novo material, calor ou mixagem melhoraram o resultado. Próximo teste deve preservar v13g como controle e produzir evidência do selamento novo, sem promover o piloto ou fazer merge automaticamente.
