# Guia de transformações — aprendizado recuperável da Ryūjin

**Adição v14:** [implementação, falhas e provas](ryujin-v14-implementacao-fluxo-olho-testes-2026-11-06.md). Ribbons precisam compartilhar tangentes/larguras nas junções, além de centros contínuos. No olho, direita anatômica pertence ao espaço da cabeça real, independente da mão/tela; a leitura artística exige controle próprio. Captura a foi rejeitada por stalls; controles b/c sem vídeo e check184 não encerram os gates de movimento/mixagem/multiplayer. Não reutilizar o export do estudo a para validar o modelo d regenerado. Telemetria por fase usa o relógio do cliente, mantendo também os critérios estritos do ensaio.

Consolidado em 06/11/2026. Procedimento do projeto para novas Bankais, manifestações e props; não impõe a outras habilidades os 15 s, 280 cubos, cores, pose ou alcance da Ryūjin. Contexto versionado, ferramentas e testes de transferência constituem o treinamento deste fluxo; a existência do guia não comprova aplicação por outra IA ou melhoria já medida.

## Contrato antes do acabamento

Especificar o que a transformação deve comunicar em preparação, execução, sustentação, finalização e devolução do controle. Para cada marco: personagem/arma, prop/VFX, câmera, áudio e evento autoritativo. Separar preferência artística, comportamento de gameplay aprovado e hipótese a experimentar. Na Ryūjin, o fim precisa mostrar **energia selada na espada**; desaparecer o prop é apenas um evento técnico.

Guardar uma referência individual por ângulo/frame, com pares liso/em blocos quando for material para modelagem. Manter escala, eixo, empunhadura e pose entre vistas; efeitos/cenário não devem esconder a geometria. Prancha IA é referência, não medida do rig. Preferir vistas nativas para corrigir contradições. Contatos temporais de QA são índices de inspeção, não substitutos dessas folhas de modelo.

## Responsabilidade de cada camada

| Camada | Responsabilidade e prova |
|---|---|
| Servidor | Estado, custo, interrupção, duração e regras de combate. Eventos/cues gráficos não concedem dano ou desbloqueios por conta própria. |
| PlayerAnimator/item | Pose, mão dominante, contato real com a arma e retorno. O manequim do editor ajuda a planejar; não vira uma cópia do jogador em produção. Conferir conflitos com Better Combat/cosméticos no consumidor real. |
| GeckoLib/Blockbench | Prop, rig, pivôs, UVs e clipes. Preservar nomes consumidos por Java e reproduzir os exports exatos. Gerador Python e arquivo `.bbmodel` não comprovam operação nativa do editor. |
| Layout VFX | Origem/destino, volume, direção e tempo por camada. Separar fogo ambiente vertical de fluxo dirigido; posição amostrada numa curva não orienta automaticamente a superfície que a desenha. |
| Material | Cor, transparência, brilho da superfície e profundidade. `FULL_BRIGHT`, shader sem normais e bloom são conceitos distintos. |
| Luz de ambiente | Alteração de superfícies externas ao efeito. Provar com controle ON/OFF e desligamento; biblioteca opcional não deve quebrar fallback base. |
| Câmera | Dirigir a atenção ao sujeito de cada beat, respeitando colisão, letterbox, mão dominante, controles e perspectiva anterior. Um close do corpo não comprova detalhe da espada. |
| Áudio | Cues, dinâmica, prioridade de voz e música, espacialização e fim dos loops. Separar arquivo carregado, nível medido, mixagem gravada e avaliação por escuta. |

## Sequência que reduz retrabalho

1. **Prova vertical pequena:** um começo, evento principal e fim com corpo/arma/prop; material simples e poucas partículas. Identificar todos os consumidores antes de polir.
2. **Eixos e transformações:** conferir unidades, pivôs, frente/costas e cadeia completa até o espaço do mundo. Modelos, emissor e luz devem compartilhar âncoras explícitas. Não transplantar compensações de um renderer para outro sem inspecioná-lo.
3. **Relógios e cues:** fase autoral controla envelopes; tempo real controla atividade durante uma sustentação. Ao mudar duração, revisar clipe, câmera, som, proteção, cancelamento e QA como um conjunto.
4. **Geometria/material isolados:** conferir export e atlas; testar uma hipótese por vez. Se textura parece metálica, isolar o shader antes de refazer toda a malha. Se fluxo não é legível, conferir orientação/cadência antes de aumentar densidade.
5. **Composição progressiva:** corpo/arma → prop → VFX → luz → câmera final → áudio. Voltar a camadas isoladas quando surgir defeito; não encobri-lo com bloom, fumaça ou edição.
6. **Validação proporcional:** regressões relevantes e cliente real, depois cenários de risco afetados. Repetir toda a suíte só quando alterações/falhas justificarem. Dedicado/observador e perfis opcionais são provas próprias.
7. **Promoção deliberada:** manter controle anterior, hashes e roteiro para o usuário. Distinguir melhora observada, aceite humano e versão realmente padrão no JAR/pack.

## Caderno dos experimentos e limites

| Experimento da Ryūjin | Resultado comprovado | Lição recuperável / fonte |
|---|---|---|
| Origem/codec e autoria nativa | MCP Codex/8788 isolado, scripts desativados; codec de projeto e de geometria diferentes. | Confirmar projeto/instância antes de mutar; validar conteúdo do arquivo, não extensão. Timeout não determina se a gravação ocorreu. [Skill pipeline](../../.agents/skills/bleach-model-pipeline/SKILL.md). |
| Duração/sustentação | Cena estendida para 300 ticks; sol inteiro 112–176. | Quantidade de frames de referência não define duração. Sincronizar todos os consumidores e preservar atividade por tempo real. [v10](ryujin-v10-duracao-sustentacao-sol-2026-11-05.md). |
| Fogo físico durante a cena | Suspensão de novas colocações, preservação de fogo preexistente e retomada após fim/cancelamento. | Separar partícula/card de bloco com dano/persistência; não limpar fogo de terceiros. 296 GameTests do ciclo de servidor anterior, não reexecução no ciclo v13. [v11](ryujin-v11-bateria-testes-roadmap-divisao-2026-11-06.md), [v12](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md). |
| Layout e cotas | v12/v13: até 264 cards observados, 192 partículas próprias e uma luz na cena medida; cleanup por dono. | Capacidade inicial de lista não é teto; limitar criação e rastrear vida/remoção efetivas. Os números são deste efeito/hardware, não orçamento universal. [v12](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md). |
| Luz Shimmer ON/OFF | Superfícies neutras aquecidas no controle noturno; luz acompanha e limpa. | Emissivo não prova iluminação de terreno. Adaptador opcional, versão/API verificadas e fallback explícito. Não inferir compatibilidade com shader pack. [v12](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md). |
| Material e âncora solares | Vanilla emissivo ainda mistura normais; controle Eyes e shader solar unlit corrigiram grande parte da leitura metálica. Origin extra de GeoObjectRenderer 4.8.4 precisou ser compensado. | Causa discriminada mantendo variáveis constantes. Cor 3D contínua resolve pintura; shader resolve outra camada. Compensação `(0.5,0.51,0.5)` pertence à versão/renderer inspecionados. [v13](ryujin-v13-sol-continuo-acabamento-2026-11-06.md). |
| Geometria refinada | Candidato g 280 cubos/33 bones, export nativo, check_model=0 e geração idempotente; humano confirma grande melhora. | Maior detalhe não garante ganho: variantes moeda/placas foram rejeitadas. Guardar amostras rejeitadas com motivo, sem promovê-las. [v13 e manifesto de fontes](qa/codex/v13g-final-matrix-20261106/source-manifest.json). |
| Parede/reload | Ajuste de câmera no fixture e clipe seguindo relógio da cena evitaram corte/reinício observados; controle f passou recarga com shader próprio. | Reservar letterbox/alcance real, medir a curva completa e testar reload. Não prometer enquadramento de qualquer sala. [v12](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md), [controle f](qa/codex/v13f-unlit-reload-20261106/manifest.json). |
| Cleanup no limite | Um ensaio v12 reteve fogo em dois frames no marco 203; não reproduziu depois. Nova telemetria inclui relógio local sem apagar o critério estrito. | Expor falha e adicionar a sonda que faltava. Uma repetição verde não prova causa nem apaga a execução anterior. [v12](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md). |
| Regressão/matriz v13 | 180 regressões no ciclo de código; g executou 12 cenários. Cleanup passou nos oito da transformação; quatro de kit encerram antes do marco 302. | “Não observado” é diferente de falha ou aprovação. Contagem de testes não substitui avaliação estética. [Matriz g](qa/codex/v13g-final-matrix-20261106/summary.json). |
| Custo sem captura | Iris Xe/720p/HIGH: p95 26,13 / 16,50 / 17,25 ms em três repetições, vsync off/cap120. | Registrar aquecimento e variabilidade do host compartilhado. Não chamar de ganho causal contra outro modelo nem de tempo GPU. [Medições g](qa/codex/v13g-performance-20261106/summary.json). |
| Vídeo/áudio | Framebuffer FFmpeg 20 fps e mixagem OpenAL por processo capturados; prévia g silenciosa. | Registrar repetições/descartes e perturbação da captura. Timestamp de arquivo não assegura sincronismo em amostras. Medição/transcrição não é escuta. Direitos dos stems e música continuam separados. [v12](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md), [vídeo g](qa/codex/v13g-final-matrix-20261106/inspection.json). |
| Piloto gráfico | Userdev Shimmer/Embeddium/Oculus falhou com FabricBlockView ausente. | Falha userdev não prova incompatibilidade do pack empacotado nem culpa exclusiva de uma biblioteca. Destravar com controles incrementais e log bruto. [v12](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md). |

## Como usar as referências externas

Comparar função visual equivalente: concentração, expansão, sustentação, transporte e finalização. Marcar tempo, sujeito que deve ser lido e limites do material. Trailer recortado/vertical, render diferente e música editada não são benchmark contra gameplay contínuo. Não inferir biblioteca, desenho interno, FPS ou multiplayer da aparência. Reproduzir princípios com assets próprios, sem copiar modelos/texturas/sons do concorrente.

A [revisão atual dos vídeos](comparacao-bleachcraft-ryujin-v13-e-plano-v14-2026-11-06.md) localiza a nova lacuna: correntes de absorção já existem, porém os cards continuam verticais. **Diagnóstico de código observado; solução por ribbons ainda proposta.** O próximo teste deve verificar se a orientação e o enquadramento tornam o destino na lâmina legível, não apenas se a nova classe compila.

## Transferência para a próxima implementação

Complemento verificado na [auditoria Antigravity v12](auditoria-antigravity-retornos-v12-2026-11-06.md): garantir que o endpoint existe e foi renderizado antes de encerrar a etapa; validar quantidade/duração do GIF/vídeo e rejeitar arquivo obrigatório ausente. Um manifesto de hashes pode misturar ciclos e um contato pode ter painel vazio: nenhuma dessas formas de empacotamento prova completude. Classificar avisos de dados inválidos como tal, sem concluir aprovação incondicional. Esses controles se aplicam também às próximas capturas Ryūjin.

Reutilizar sampler/âncoras/relógio/telemetria quando seus contratos couberem; adaptar pose, câmera, paleta e duração à nova habilidade. Uma arquitetura compartilhada se justifica após mais de um consumidor real, evitando criar uma API genérica só para um efeito.

Exercício de transferência: escolher uma ação Quincy ou outra manifestação de escopo pequeno; o executor identifica uma falha, formula duas causas possíveis e executa um controle que as distingue, preservando o estado anterior. Entregar fonte/diff, export, marcos comparáveis, vídeo quando pertinente, resultado e limite. Separar intervenção assistida de descoberta autônoma. Os prompts A/B de capacitação já preparados continuam sob encaminhamento humano; este guia não foi automaticamente instalado/ativado no Antigravity.

Ao terminar cada ciclo, atualizar o documento da feature com sintoma → causa comprovada → correção → teste → limite; mover para a skill apenas o aprendizado transferível. Esta consolidação indexa resultados existentes e nova análise de mídia; não inventa testes novos nem encerra áudio, shader pack, cosméticos ou multiplayer.

**07/11 — provas transferíveis da v16.** No Forge empacotado, o Complementary pôde concluir a cena mesmo com as camadas personalizadas invisíveis. Compare captura, material efetivamente usado e log de compilação do shader. Use os tipos de renderização reconhecidos pelo pack e depois calibre a exposição em dia e noite. Para pós-processamento próprio antes do HUD, confira qual matriz o evento fornece e proteja o primeiro plano com profundidade. Para trilha de cena, inicie deliberadamente uma música vanilla antes, force nova tentativa durante e confirme a liberação após o cancelamento, sem mudar o slider. Uma redução de 278 para 124 cartões manteve o p95 perto de 13 ms no controle local, por isso a contagem de instâncias precisa ser acompanhada de medição real. [Implementação, controles e provas](ryujin-v16-acabamento-e-perfis-forge-2026-11-07.md).
