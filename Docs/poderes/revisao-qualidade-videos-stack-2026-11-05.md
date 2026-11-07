# Revisão de qualidade — vídeos, modelos Quincy e stack visual

> **Atualização 06/11:** este documento registra a análise anterior ao sol v13. Para o estado atual, consultar [nova comparação com 71 amostras e proposta v14](comparacao-bleachcraft-ryujin-v13-e-plano-v14-2026-11-06.md) e [guia consolidado dos testes/aprendizados](guia-transformacoes-modelagem-vfx-qa-2026-11-06.md). Não usar as lacunas históricas abaixo como se todas permanecessem sem avanço.

**05/11/2026 · `Feature-Poderes-bankais`, HEAD `9eb6a05`.** Revisão documental e de mídia; worktrees main/Quincy/design preservados. Consolida o [plano visual](plano-stack-visual-animacao-camera-shaders-2026-11-05.md), o [ensaio já executado](ensaio-qualidade-stack-animacao-2026-11-05.md) e as [pranchas Quincy](quincy-pranchas-modelagem-2026-11-05.md). Nesta revisão não foram instalados mods, alterado gameplay ou executado Minecraft. Testes anteriores conservam suas datas/limites.

## Estado da autoria Quincy

Foram produzidas **três pranchas raster**: Ishida base, Vollständig e montagem de asas/estrelas v2, com versões lisa/em blocos. O usuário recebeu as imagens e pediu sua documentação e a retomada do plano de qualidade. Isso autoriza usá-las como base de estudo; não equivale a um modelo completo integrado ou aceite de gameplay.

A evolução visual Kojaku → Ginrei → armamento Vollständig continua sem imagem final: duas gerações foram bloqueadas pelo gerador. Também faltam fonte `.bbmodel` nova, escala medida no rig real, UV/atlas, animações exportadas dessas novas propostas e prova no Forge. O rig Antigravity existente continua uma entrega separada; não foi substituído pelas pranchas. Corrigir diferenças entre vistas, eixo X rotulado de modo conflitante e proporções antes da modelagem.

## Nova inspeção dos vídeos

Reutilizadas as fontes e hashes do [relatório de mídia](bleachcraft-videos-ecossistema-2026-11-05.md). Extraídos mais **24 quadros**, dois trechos de 12 amostras espaçadas em 0,2 s, via `tools/media_qa/inspect_media.py`:

- `Download.mp4`, **80,8–83,0 s**: `build/bleachcraft-video-review/revisao-feixe-20261105/`, com `inspection.json`, frames e `contact-sheet.png`.
- Progressão Hollow, **34,5–36,7 s**: `build/bleachcraft-video-review/revisao-anel-20261105/`, mesma estrutura.

São amostras de seis em seis frames de uma fonte 30 fps, não inspeção de todos os frames nem prova de fluidez contínua. Não houve nova escuta do áudio. Resolução/compressão e edição do trailer limitam a identificação de materiais, rig e desempenho.

| Observação visual | Como reproduzir o princípio no nosso mod — hipótese de implementação |
| --- | --- |
| Trailer: ponto amarelo junto ao personagem antes de a forma ciano se projetar, seguido por duas faixas compridas com centro parcialmente aberto. | Preparação corporal e foco pequeno; duas faixas/fitas em geometria com textura animada, opacidade e borda controladas. Não tentar reconstruir toda a forma só com uma nuvem de partículas. |
| As faixas ciano mudam de abertura/orientação ao longo das amostras; há massa clara junto ao chão à direita. | Separar origem, direção, extensão e largura; ruído UV/flipbook nas faixas e emissores distintos para contato/terminação. A imagem não prova que a massa no chão seja dano, impacto em alvo ou iluminação dinâmica. |
| Hollow: anéis brancos precedem anéis laranja mais intensos; o plano do anel muda e fragmentos ocupam a periferia. | Geometria anular ou ribbon com máscara, rotação e curva de escala; fragmentos de duração curta; brilho na superfície emissiva. Torus, planos e ribbons podem produzir aparência parecida; a fonte não identifica o método. |
| Mudanças de forma têm corpos e proporções distintos, cenário e cortes reforçam a progressão. | Modelos/rigs próprios por estágio e animação de transição; câmera e edição para apresentação. Isso demanda autoria Hollow e contratos de evolução, não aparece ao adicionar um mod cosmético. |
| Close frontal seguido de plano aberto e ambiente com sombras longas no trailer. | Direção de câmera com foco e FOV planejados, cenário simples de comparação e iluminação consistente. Não se pode concluir se a câmera observada pertence ao gameplay ou à gravação. |

**Meta realista:** reproduzir a clareza de uma ação completa e seu impacto visual em uma cena nossa. O trailer não fornece evidência para prometer equivalência global de qualidade, FPS, multiplayer ou conteúdo. O efeito precisa funcionar e retornar ao controle sem depender de cortes promocionais.

## Stack recomendado e estado verificado

| Camada | Escolha para o ensaio | Estado e finalidade |
| --- | --- | --- |
| Autoria 3D | Blockbench + plugin GeckoLib + ponte Codex isolada | Ferramentas de geometria, pivôs e keyframes; imagens servem de referência. Confirmar conexão da ponte no início da execução; a pesquisa atual não testou a conexão. |
| Corpo e combate | PlayerAnimator + Better Combat existentes | `gradle.properties` fixa PlayerAnimator `1.0.2-rc1+1.20-forge`; `mods.toml` pede Better Combat `[1.9.0,1.10)`. Criar poses e transições de verdade; coordenar prioridade e membros afetados. A [biblioteca PlayerAnimator](https://github.com/KosmX/minecraftPlayerAnimator) é a base de animação do jogador, não biblioteca de VFX. |
| Asas, estrelas, sol e criaturas | GeckoLib existente | `mods.toml` exige `[4.8.4,4.9)`. [GeckoLib](https://github.com/bernie-g/geckolib) oferece keyframes e eventos; reproduzir os exports exatos, com pivôs e âncoras conferidos. Roupa 3D longa requer geometria/rig próprio; extrusão da skin não resolve saias ou casacos articulados. |
| VFX A | Photon **1.1.17** + LDLib **1.0.42**, piloto atual | Gradle usa dependências opcionais, e `mods.toml` restringe Photon `[1.1.17,1.2)` / LDLib `[1.0.42,1.1)`. Aproveitar o piloto para provar um efeito autoral. O [README atual](https://github.com/Low-Drag-MC/Photon) descreve Photon 2.x, incluindo recursos novos de iluminação/pós-processamento: não atribuí-los ao nosso 1.x. |
| VFX B | AAA Particles **2.3.4 Forge 1.20.1** + Effekseer | A [página do autor](https://www.curseforge.com/minecraft/mc-mods/aaa-particles) lista arquivo específico 1.20.1/Forge; o [repositório](https://github.com/ChloePrime/AAAParticles) explica o carregamento Effekseer, mas o exemplo Gradle ainda cita 1.4.11. Fixar release e verificar sua API antes de integrar. [Effekseer](https://effekseer.github.io/Helps/18x/Tool/en/ToolReference/index.html) fornece autoria por nós, curvas e hierarquias de emissão. Ainda é candidato, não instalação atual. |
| Render/shader | Perfil cliente Embeddium + Oculus + Complementary Reimagined | [Embeddium](https://www.curseforge.com/minecraft/mc-mods/embeddium) otimiza render e oferece Forge 1.20.1; [Oculus 1.8.0](https://www.curseforge.com/minecraft/mc-mods/oculus/files/6020952) carrega shaders nessa base. [Complementary](https://github.com/ComplementaryDevelopment/ComplementaryReimagined) é a proposta de aparência ambiental. Manter os candidatos anteriores de Embeddium 0.3.31/Oculus 1.8.0, conferindo os arquivos exatos; fixar também shaderpack e preset no ensaio. Nenhuma combinação foi homologada com nosso pacote. |
| Acabamento de skin | Skin Layers 3D | [Autor](https://github.com/tr7zw/3d-Skin-Layers) confirma extrusão da segunda camada, cliente e compatibilidade declarada com PlayerAnimator/Better Combat; também registra possíveis sombras estranhas e clipping com armadura. Não fornece um novo esqueleto/corpo de anime. E1 local permanece inconclusivo. |
| Gestos vanilla | Not Enough Animations | [Autor](https://github.com/tr7zw/NotEnoughAnimations) descreve ações em terceira pessoa e opções individuais. Útil fora dos clipes especiais, mas testar disputa de braços e visibilidade de item no arco. A falha anterior de userdev não prova falha no Forge distribuído. |
| Câmera | `CinematicDirector` e controladores próprios existentes | Evoluir a curva e o enquadramento do mod. Um mod de câmera adicional ainda não resolve necessidade demonstrada. Ataques comuns preservam controle/mira e observador não recebe câmera forçada. |

**Distribuição:** a [licença publicada Photon](https://www.curseforge.com/minecraft/mc-mods/photon/license) distingue o mod e o conteúdo criado com suas ferramentas e traz condições não comerciais para o mod; verificar o artefato fixado antes do pack final. NEA publica restrições específicas à redistribuição fora de CurseForge. Esses pontos afetam empacotamento, não justificam afirmar que nossos efeitos autorais precisam mudar de licença. Não copiar código/assets do concorrente.

## Como alcançar a meta com menos retrabalho

1. **Corpo e prop primeiro — E2:** usar um arqueiro de teste com arco atual e asas já existentes. Produzir guarda → elevação → tração → soltura → recuperação. A mão do arco permanece na empunhadura, a mão da corda encontra a corda e os pés sustentam o movimento. Fazer lado/frente/costas, mão esquerda/direita e observador. Não exigir a conclusão do desenho Ginrei para testar isso.
2. **Uma composição de efeito — E3:** usar o trecho de explosão Ryūjin como ensaio principal já planejado, preservando a cena aceita. Fazer versão base, Photon 1.x e AAA/Effekseer com mesma origem, duração, volume e paleta; uma versão por vez. Dividir o efeito em preparação, massa principal, contorno/fragmentos, sustentação e dissipação. Um estudo posterior de faixa ciano pode reaproveitar o contrato para Quincy sem copiar a geometria do vídeo.
3. **Um relógio e âncoras explícitas:** início/cancelamento do servidor alimentam corpo, item, prop, câmera, áudio e VFX. Desenho não decide dano, alcance ou destruição. Guardar ID de cena, dono, início, direção, ponto e duração; evitar enviar cada partícula pela rede.
4. **Shader depois da leitura base:** comparar base → Embeddium → Oculus sem shader → Complementary ativo. Manter cena, hora, resolução e câmera. Testar máscara emissiva, transparência e exposição. Superfície brilhante, halo/bloom e luz real projetada no terreno são três resultados diferentes; demonstrar cada um antes de declarar iluminação dinâmica concluída.
5. **Cosméticos — E1:** testar NEA, Skin Layers e ambos em **Forge empacotado isolado**, usando JAR do mod. O erro anterior em biblioteca embutida no userdev pede esse ambiente; repetir o mesmo lançamento não resolve. E1 pode avançar em paralelo ao trabalho E2, mas não é pré-requisito de novas poses.
6. **Decisão de promoção:** escolher um backend VFX pelo resultado e custo; conservar fallback base. Só depois aplicar padrão a outras Bankais, Hollow e lore. APIs Architectury/Balm, questbooks e inventários continuam decisões por necessidade funcional, não ferramentas para obter o efeito do trailer.

## Critérios de comparação

| Critério | Evidência necessária |
| --- | --- |
| Silhueta | Ler corpo, arma, duas asas/quatro estrelas e direção sem bloom ou partículas ocultando a pose. |
| Movimento | Vídeo contínuo de preparação até retorno, sem salto de punho/item, clipping grave ou braço preso. |
| VFX | Origem acompanha a âncora correta; faixa/anel tem volume e dissipação legíveis; alvo e área não somem sob brilho. |
| Câmera | Close mostra rosto/arma, plano aberto não corta manifestação, colisão respeitada e retorno funciona. |
| Luz | Comparação dia/noite e shader desligado/ligado distingue emissivo, bloom e luz no mundo. |
| Desempenho | Mesma GPU/CPU, resolução, distância e preset registrados; após aquecer, comparar mediana/p95 de frametime e pico de instâncias. Registrar primeira compilação/carga separadamente. Meta de 60 fps = orçamento de 16,7 ms por frame é um alvo de ensaio, não medição nem promessa. |
| Multiplayer | Dono e segundo cliente veem a mesma ordem/origem; múltiplos casts não trocam animação e cancelamento limpa efeitos. |

**Próxima entrega concreta sugerida:** um clipe curto de arqueiro e prop sincronizado no cliente, sem VFX intenso, acompanhado por uma comparação base/shader da Ryūjin existente. Isso mede duas lacunas distintas antes de redesenhar todos os poderes. O benchmark de VFX A/B vem depois da pose/âncora estável. P0 de saves e aceites Rukia/Hakuteiken continuam no roadmap próprio.
