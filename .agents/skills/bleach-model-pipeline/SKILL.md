---
name: bleach-model-pipeline
description: Pipeline completo de modelagem, rig, texturização procedural, teste em laboratório Blockbench e integração GeckoLib/Java no Bleach Mod.
---

# Pipeline Completo de Modelagem, Laboratório e Integração — Bleach Mod

Este documento orienta modelagem e animação no Bleach-Modpack. Não presume um checkout/branch específico nem concede permissão para instalar ferramentas ou alterar gameplay. Confira a raiz, branch, HEAD, alterações locais e disponibilidade real das ferramentas em cada tarefa.

## 0. Limites do runtime e camadas de animação — revisado em 02/11/2026

- Antes de editar, confira se o MCP Blockbench está realmente exposto nesta sessão. Catálogo, instalação/configuração no host e conexão operacional são estados distintos. Não invente nomes de ferramentas nem substitua um MCP desconectado por chamadas arbitrárias de shell/HTTP. Se faltar conexão, relate-a e use somente uma alternativa já acessível e autorizada.
- Distinga três peças na preparação do laboratório: a dependência GeckoLib do mod declarada no Gradle, o plugin **GeckoLib Models & Animations** dentro do Blockbench para autoria/exportação e a ponte Blockbench MCP com servidor registrado no cliente Codex. Uma não instala nem prova as outras. Confira plugin, processo, porta e ferramentas expostas antes de afirmar que o editor está operável.
- Em QA visual, hash do arquivo confirma integridade da captura, não o conteúdo alegado na legenda. Verifique se enquadramento mostra alvo/efeito e se a amostragem temporal basta para julgar fluidez; poucos quadros repetidos num GIF não provam transição contínua.
- Uma prova GeckoLib deve carregar os mesmos `.geo.json`, textura e `.animation.json` que serão usados pelo mod. Um harness que redesenha geometria e poses à mão pode validar câmera/fixture, mas **não** valida parser, rig, clipes ou playback dos exports.
- A existência de um clipe no JSON ou num GameTest só prova que o recurso foi encontrado. Se `bones` estiver vazio, trate-o como marcador temporal até confirmar o movimento no renderer e no cliente; neste repo, `ice_hydra.emerge`, `assault` e `roar` são clipes vazios enquanto a Hydra também tem lógica procedural (`tools/models/hydra_organic.py`, `IceDragonRenderer`).
- Na GeckoLib 4, cada `AnimationController` toca uma animação por vez. Controllers podem atuar ao mesmo tempo, mas não devem disputar os mesmos bones/canais: controllers registrados depois têm precedência na resolução. Planeje propriedade dos bones e valide blend/retorno ao repouso no runtime.
- Ações de combate continuam autoritativas no servidor. Use sincronização/trigger do servidor para iniciar animações de ação quando a classe animável permitir; keyframes de som/partículas são apresentação do cliente e não devem determinar dano, alvo ou colisão.
- O editor de animação do Blockbench oferece timeline, keyframes, interpolation/graph editor e canais de Animate Effects. Isso ajuda na autoria, mas preview do editor continua separado do playback em GeckoLib/Forge.

Referências primárias consultadas para estas regras: [GeckoLib 4 Animation Controller](https://github.com/bernie-g/geckolib/wiki/The-Animation-Controller-%28Geckolib4%29), [GeckoLib 4 Triggerable Animations](https://github.com/bernie-g/geckolib/wiki/Triggerable-Animations-%28Geckolib4%29), [GeckoLib 4 Keyframe Triggers](https://github.com/bernie-g/geckolib/wiki/Keyframe-Triggers-%28Geckolib4%29) e [Blockbench Animation Overview](https://blockbench.net/wiki/guides/blockbench-overview-tips/).

### Lição verificada no laboratório Codex em 04/11/2026

- Se outra IA usa o Blockbench, confira projeto ativo antes de qualquer mutação. Para ensaio próprio, use perfil/processo separado e porta MCP diferente; neste ensaio, Antigravity permaneceu em `8787` e Codex operou em `8788`. Desative `execute_script` no editor e no cadastro MCP.
- No formato `geckolib_model` do plugin GeckoLib 4.2.5, `single_texture` e `box_uv` estavam ativos. Seis texturas de cores sólidas colocadas em `add_cubes` acabaram referenciadas como textura `0` no `.bbmodel`; o preview ficou todo amarelo. Para este modelo, `pack_uv` + `detail_cubes` num atlas único coloriu 139 cubos, e `check_model` caiu de 26 conflitos de faces para zero após deslocar planos coincidentes.
- Nesta combinação Blockbench 5.2.2 / BlockbenchMCP 0.3.1 / GeckoLib 4.2.5, `export_project` chamou o codec de **projeto** e gravou conteúdo `.bbmodel` apesar do caminho `.geo.json`. Para geometria real, `export_model` com `codec: "bedrock"` acionou o codec que o plugin GeckoLib estende: confira `minecraft:geometry`, identificador e bones antes de confiar na extensão do arquivo. Na criação, passe `geometry_name` sem o prefixo `geometry.` para evitar `geometry.geometry.*`.
- O clipe foi exportado pelo menu **File → Export → Export GeckoLib Animations** do editor; valide `format_version`, `animation_length` e bones no JSON salvo. O atlas PNG foi extraído sem transformação da textura embutida no `.bbmodel`. Mesmo com ambos válidos, ainda falta reproduzir os assets exatos no cliente Forge.
- Antes de `set_project_meta`, `paint_texture` ou `save_project`, exija `get_status` da **mesma ponte** e confira nome, contagem de cubos e textura. Trocar a aba na UI não foi suficiente neste ensaio: uma tentativa atingiu o projeto de pose em memória; o nome foi restaurado sem salvar, e `load_project` da fonte correta resolveu a seleção. Salve variantes com nomes novos e valide a fonte no disco.
- Em `geckolib_model` com textura única, `add_plane` + `create_vfx_texture` gerou retângulos visíveis e 99 alertas (`no_texture`, `zero_uv`, `degenerate_size`) em 11 planos. Não promova essa variante a asset. A pintura alfa de faces no atlas já empacotado produziu chamas recortadas com `check_model=0`, mas transparência/renderização ainda exigem prova no cliente Forge.
- `analyze_animation` reportou `no_visible_motion` no sol apesar de 171 keyframes e previews com escala/rotação: o analisador mede deslocamento de extremidades de rigs, que este VFX não tem. Para props e efeitos, combine amostragem visual temporal com verificação dos canais/bones exportados; não trate esse veredito isolado como prova de ausência de movimento.
- A exportação `export_model` do atlas 256×4096 levou 70 s, acima do timeout de 60 s do cliente MCP, mas gravou o arquivo. Ajuste o timeout da ponte e confirme conteúdo/identificador no disco antes de repetir uma exportação que parece ter falhado.
- Para ensaiar uma cena conjunta sem alterar gameplay, componha manequim, arma e VFX no mesmo projeto GeckoLib, com `sun_root` separado e fases de preparação, fala, explosão, formação, giro, absorção e retorno. Amostre quadros em tempos nomeados; keyframes exportados mostram movimento programado, mas não provam leitura visual nem sincronização no cliente.
- Cubos de reflexo com espessura inferior a 1 unidade geraram quatro avisos de Box UV e 16 `zero_uv`; ampliar a espessura para 1 e repintar o atlas nas novas coordenadas UV eliminou ambos. Inspecione o validador **do editor** além do `check_model` da ponte, pois eles não reportaram a mesma coisa na primeira passagem.
- Para brilho seletivo no GeckoLib 4, gere `<textura>_glowmask.png` transparente fora das faces quentes e registre a necessidade da camada `AutoGlowingGeoLayer` no renderer. O preview do Blockbench mostra textura e animação, não iluminação dinâmica do mundo. Só chame o efeito de luz concluído após prova no cliente Forge, incluindo dia/noite e desempenho.
- Retorno humano da Ryūjin v6: uma separação correta no eixo (`get_orientation`: frente `-Z`, sol em `+Z`) ainda pode parecer invertida numa câmera traseira, pois o sol atrás do ator entra entre jogador e observador. Capture `front`, `front_right`, `back`/`back_right` com rótulos e inspecione a sobreposição na cabeça; marque frente do manequim se o placeholder não tiver rosto legível. Não descarte a percepção do usuário por causa de coordenadas corretas.
- Absorção por escala em poucos pontos foi rejeitada visualmente. No ensaio v7, preserve o sol até a formação, use contração monotônica amostrada em intervalos curtos, acelere a rotação de aros alternados, torça línguas e alinhe faíscas ao ponto de partida deslocado; compare quadros adjacentes e o destino na katana. Previews e `analyze_animation=0` não substituem o efeito contínuo de chamas/partículas e a câmera no cliente.

---

## 1. Ciclo de Vida do Asset

### Lição de correntes e efeitos na cabeça — Ryūjin v14, 06/11/2026

- Para ribbons por segmentos, compartilhar posição, tangente e largura nas junções; orientação pela corda de cada segmento pode abrir fendas mesmo com centros contínuos. Testar curvas com câmera de frente/perfil e direção degenerada; incluir os quads no orçamento.
- Um efeito no olho usa o espaço da cabeça renderizada, não a mão dominante ou direita da tela. Na vanilla PlayerModel usada aqui, X negativo é direita anatômica; o manequim nativo Y-up usa outra convenção. Provar lado em destro/canhoto e depois rotação/oclusão; não aplicar esse sinal a rigs diferentes sem inspeção.
- Gerar uma versão nova invalida o vínculo do export/preview anterior com o arquivo atual. Registrar fonte e hash por rodada, reabrir/exportar e distinguir estudo de staging do asset/sampler produtivo. [Implementação e limites v14](../../../Docs/poderes/ryujin-v14-implementacao-fluxo-olho-testes-2026-11-06.md).

### Lição de material e âncora — Ryūjin v13, 06/11/2026

- Ao alinhar um VFX GeckoLib a partículas/luzes externas, inspecione as transformações do renderer da versão efetiva além dos bones. No `GeoObjectRenderer` 4.8.4, `preRender` soma `(0.5,0.51,0.5)`; o piloto solar precisou cancelar esse origin de objeto para coincidir com sua âncora de mundo. Não aplique essa compensação a renderers/versões diferentes sem conferir a implementação e o resultado em jogo.
- `FULL_BRIGHT` e o nome “emissive” não garantem um material sem sombreado direcional. No Minecraft 1.20.1, o VSH `rendertype_entity_translucent_emissive` ainda mistura luz pelas normais. A Ryūjin isolou a causa mantendo geometria/textura e trocando somente o shader; depois adotou no piloto um material unlit com descarte alfa e fog. Compare transparência/profundidade e reload; o preview do Blockbench não valida esse shader.
- Para um volume dividido em cubos, derive cores das posições 3D compartilhadas, em vez de reiniciar um gradiente em cada face. Isso corrige emendas de pintura, mas não prova que contraste causado por iluminação foi resolvido. Valide separadamente UVs, material e silhueta. Refinar a grade muda o orçamento de cubos e o empacotamento do atlas: asserções devem impedir exportar um bake fora da área reservada.
- Fonte, controles rejeitados e limites: [Ryūjin v13](../../../Docs/poderes/ryujin-v13-sol-continuo-acabamento-2026-11-06.md). Controle f: 180 regressões, shader próprio e cleanup/reload no Forge; g: export nativo/check_model=0, 280 cubos, matriz de 12 cenários e três medições sem captura. Humano confirmou melhora visual, mantendo acabamento aberto; isso não homologa o conjunto nem shader packs.
- O [guia de transformações](../../../Docs/poderes/guia-transformacoes-modelagem-vfx-qa-2026-11-06.md) reúne a sequência de prova vertical, responsabilidades e resultados reutilizáveis. Separar geometria, pintura, shader, emissor e luz nos controles; preservar material de referência individual por ângulo/frame. Vídeo promocional ajuda a estudar hierarquia/ritmo, sem identificar automaticamente a biblioteca ou garantir que cada efeito funcione em gameplay contínuo.

### Regra de pranchas para Blockbench — decisão do usuário em 05/11/2026

- Entregar **um arquivo por ângulo e por frame**, sem grades com várias vistas. Se uma geração mudar a empunhadura ao representar um perfil, rejeitar a versão e gerar a vista a partir do modelo nativo; não alterar o rig para coincidir com uma perspectiva incorreta. Manter imagens rejeitadas identificadas no manifesto, sem usá-las como contrato.
- Para props muito maiores que o ator, manter um projeto de pose separado: o enquadramento automático do conjunto pode tornar mãos/arma ilegíveis. Capturas de sustentação não provam preparação/retorno; analisar movimento, corrigir postura estática e curvas mecânicas, e conferir o resultado no cliente.
- Em perfis Blockbench isolados, confirmar porta e opção de scripts a cada retomada: a instalação do plugin não garante que 8788/`execute_script=false` continuem selecionados. Operar somente na instância própria, preservando 8787/Antigravity. Fontes e resultados deste ensaio: [Ryūjin v9](../../../Docs/poderes/ryujin-v9-pranchas-chamas-laboratorio-2026-11-05.md).

- Trate storyboard para modelagem como **folha de referência**, com frente, costas, perfis e 3/4 em escala/orientação coerentes; inclua vista superior quando a profundidade de jogador, prop e VFX importar.
- Para cada pose, produza pares equivalentes: versão **limpa/lisa**, com volumes essenciais, e versão **em blocos** para converter em cubos/planos no Blockbench. Compare silhueta, pivôs, mão/arma e proporção nas mesmas vistas antes de modelar.
- Remova cenário, adornos imaginados, partículas e iluminação que escondam geometria. Desenhe VFX, câmera e emissão em folhas separadas. Rotule eixos, frente/costas, escala conhecida, ossos/pivôs e versão; não apresente medidas estimadas por IA como fatos verificados.
- Na Ryūjin, seguir [o plano P0–P7 e os gates de validação](../../../Docs/poderes/plano-poses-storyboards-modelagem-2026-11-05.md). A mesma disciplina se aplica às próximas pranchas Quincy. O editor e o cliente Forge continuam sendo a prova de animação e encaixe.
- Aprendizado das pranchas Quincy em 05/11: conferir etiquetas de eixo entre folhas, escala entre variantes e se a linha “em blocos” realmente usa cuboides. Uma geração trouxe rótulos X contraditórios e segmentos afilados; a revisão de montagem v2 explicitou barras retangulares sobrepostas. Registrar a fonte escolhida e corrigir no editor antes de exportar, sem tratar texto da imagem como contrato do rig existente.

```
  [1. Concepção & Referência]
             │ (Análise visual de proporção, silhueta e pivôs)
             ▼
  [2. Laboratório Blockbench (MCP)]
             │ (Cópia ag_ isolada em tools/models/lab/antigravity/)
             ▼
  [3. Pipeline Mestre de Assets]
             │ (tools/models/hydra_organic.py + ice_hydra.py)
             ▼
  [4. Texturização & UV Atlas Dedicado]
             │ (Mapeamento per-face explícito em quadrantes dedicados)
             ▼
  [5. Integração com GeckoLib & Java Procedural]
             │ (IceDragonRenderer, HydraMotion, ZanpakutoReleaseAnimation)
             ▼
  [6. Validação em Três Níveis: Check -> GameTests -> ClientQa]
             │ (Evidências visuais e telemetria em Docs/poderes/qa/antigravity/)
             ▼
  [7. Documentação Técnica em Docs/poderes/]
```

---

## 2. Protocolo de Laboratório Blockbench (MCP)

1. **Editor Compartilhado**:
   - Nunca altere modelos abertos pertencentes a outros autores.
   - Sempre utilize `load_project` ou crie uma nova aba salvando cópias com prefixo `ag_` na pasta `tools/models/lab/antigravity/<etapa>/`.
   - O comando `execute_script` permanece estritamente desativado.
2. **Pivôs e Hierarquia Canônica**:
   - Nomes de bones representam contratos públicos com o código Java (`root`, `head_center`, `jaw_center`, `neck_{name}_{1..12}`).
   - Pivôs anatômicos devem ser cravados no ponto real de articulação física (ex: `jaw` em `[0, -8.5, -10]` na base condilar, nunca na ponta dos dentes).

---

## 3. Arquitetura de Geração e Consistência Mestra

Para que modificações não desapareçam quando os geradores do projeto forem executados novamente, os assets não devem ser editados apenas como arquivos estáticos, mas integrados na raiz do pipeline:

1. **Definição Anatômica**:
   - `tools/models/hydra_organic.py`: Estruturação da geometria tridimensional, cálculo de raios dinâmicos, subdivisão de segmentos e animações base GeckoLib.
2. **Gerador e Exportador Mestre**:
   - `tools/models/ice_hydra.py`: Síntese procedural de texturas RGBA, compilação de geometrias `.geo.json`, clipes `.animation.json` e projeto `.bbmodel`.
3. **Checagem Idempotente Obrigatória**:
   - `python tools/models/ice_hydra.py --check`
   - O comando deve retornar `Exit 0` confirmando que todos os arquivos de runtime (`geo.json`, `animation.json`, `texture.png`, `.bbmodel`) correspondem estritamente à fonte.

---

## 4. Texturização Procedural e Mapeamento UV Atlas

1. **Zonas UV Dedicadas**:
   - Evitar mapeamento de caixa cego (`box UV` a partir de `0, 0`), que causa sobreposição desastrosa em paletas sensíveis (como dentes, olhos ou boca).
   - Estruturar quadrantes específicos no atlas (ex: 128x128):
     - `Zone 0`: Olhos (`0..15, 0..15`)
     - `Zone 1`: Presas e Dentes (`16..31, 0..15`)
     - `Zone 2`: Garganta / Cavidade Oral Profunda (`0..15, 16..31`)
     - `Zone 3`: Língua e Palato (`16..31, 16..31`)
     - `Zone 4`: Chifres e Estrias Glaciais (`0..31, 32..47`)
     - `Zone 5`: Penas e Asas Translúcidas (`32..63, 0..31`)
     - `Zone 6`: Placas Ventrais Gulares / Gastrosteges (`0..31, 48..79`)
     - `Zone 7`: Espinhos Glaciais e Cristas Dorsais (`32..63, 48..79`)
     - `Zone 8`: Matriz de Escamas em Diamante (`64..95, 48..79`)
     - `Zone 9`: Colarinhos e Aros Articulares (`96..127, 48..79`)
     - `Zone 10`: Facetas Prismáticas de Núcleo (`0..63, 80..111`)
2. **Mapeamento Per-Face Explícito**:
   - Cada face exposta (`north`, `south`, `east`, `west`, `up`, `down`) recebe suas coordenadas $[U, V, U+W, V+H]$ exatas, garantindo arestas geadas (`RGB(240, 252, 255)`), sulcos sombreados e destaques especulares.

---

## 5. Coordenação entre GeckoLib e Controle Procedural Java

1. **Convenção de Rotação e Sinais**:
   - O GeckoLib 4.0 aplica rotações de bone em torno de `Axis.XP`.
   - No sistema de coordenadas de modelos Minecraft (onde $-Z$ é frontal e $+Y$ é superior), uma rotação **positiva** em $X$ eleva a geometria para cima (empurrando a mandíbula para dentro do crânio), enquanto uma rotação **negativa** em $X$ abaixa a mandíbula abrindo a boca.
   - Qualquer cálculo procedural em Java deve aplicar `(float)Math.toRadians(-angleDeg)`.
2. **Curvas de Atuação Orgânica em 6 Fases**:
   - `Fase 1 (Repouso)`: $0^\circ$, hermeticamente selado com escudos condilares.
   - `Fase 2 (Tensão/Aviso)`: $+0.5^\circ$, leve contração antes do rugido.
   - `Fase 3 (Abertura)`: Rampa suave cosenoidal até abertura máxima ($34^\circ$).
   - `Fase 4 (Sustentação Estável)`: Abertura firme durante o fluxo/sopro, com micro-tremor de Reiatsu ($0.75^\circ \cdot \sin$), eliminando oscilações mastigatórias artificiais.
   - `Fase 5 (Fechamento)`: Desaceleração suave.
   - `Fase 6 (Snap de Mordida em Bote)`: Fechamento instantâneo no instante exato do impacto (`impactTick`).
3. **Escalonamento Temporal de Criaturas com Múltiplas Cabeças**:
   - Aplicar offsets orgânicos entre as cabeças (ex: $0.0$, $2.5$ e $4.0$ ticks) para evitar movimento mecânico síncrono em bloco.

---

## 6. Poses do Jogador com PlayerAnimator

1. **Cinemática Direta e Ancoragem**:
   - Ancorar os pés rigidamente no plano de solo ($Y = 24.0$).
   - Manter afastamento frontal seguro do cabo e da lâmina ($Z \le -6.0$ pixels, tipicamente $Z = -8.422$) para evitar interseção poligonal com o tórax ou pernas.
   - Empunhadura bimanual precisa: mão primária em $Y = 3.0$ e secundária em $Y = 6.0$, repousando contra a tsuba sem tocar na lâmina afiada.
   - Direção da espada estritamente descendente ($Y > 0.999999$).
2. **Dinâmica Temporal e Liberação Limpa**:
   - Interpolação acompanhando marcos cinemáticos (preparação, acúmulo, erupção, explosão, recuperação).
   - No tick final da transformação/habilidade, atenuar o peso (`weight`) exatamente a `0.0`, com limpeza integral de variáveis para garantir **zero travas residuais** de movimento no jogador.
3. **Compatibilidade Dupla**:
   - Suporte verificado tanto para modelos padrão (Steve - 4px) quanto slim (Alex - 3px).
   - Suporte a 1ª pessoa (`RenderHandEvent` com pitch e translação correspondentes) e 3ª pessoa.

---

## 7. Roteiro Obrigatório de Validação e Evidências

Antes de concluir qualquer entrega de design ou modelo:
1. `python tools/models/ice_hydra.py --check` $\to$ Consistência total.
2. `./tools/antigravity/Invoke-Design.ps1 -Mode Check` $\to$ Compilação limpa (`Exit 0`).
3. `./tools/antigravity/Invoke-Design.ps1 -Mode GameTests` $\to$ Suíte de testes headless aprovada com 100% de sucesso (`Exit 0`).
4. `./tools/antigravity/Invoke-Design.ps1 -Mode ClientQa` $\to$ Cliente Minecraft Forge real executando a suíte de cenários com auto-exit (`Exit 0`), coletando FPS e telemetria.
5. **Arquivamento de Evidências**:
   - Extrair capturas e arquivos `.jsonl` em `Docs/poderes/qa/antigravity/<subpasta>/`.
   - Registrar relatório formal detalhado em `Docs/poderes/<num>-<descricao>.md`.


---

## 8. Padrão de Engenharia de VFX, Materiais Emissivos e Câmeras (Atualização 04/11/2026)

1. **Camadas Emissivas & Atlas Duplo**:
   - Para entidades e adereços de Reishi (Quincy Vollständig) ou Bankai (Senbonzakura Hakuteiken), separar a geometria difusa (`textura.png`) da camada emissiva (`textura_glow.png`).
   - A camada de brilho é renderizada via `RenderType.entityTranslucentEmissive(texture)` ou `RenderType.eyes(texture)` com `LightTexture.FULL_BRIGHT`, garantindo emissividade pura e ausência de sombras ambientais que prejudiquem a leitura da energia espiritual.
2. **Trilhas Contínuas de Lâmina e Projéteis (*Ribbon Trails*)**:
   - Golpes cortantes e salvas de flechas (Licht Regen) devem utilizar buffers circulares de interpolação temporal conectados por tiras de triângulos (`TRIANGLE_STRIP` no `VertexConsumer`).
   - Elimina o uso de partículas pontuais desconectadas em favor de arcos contínuos de corte e feixes esticados de energia com desvanecimento exponencial de transparência (alfa 1.0 $\to$ 0.0).
3. **Coreografia Cinemática de Câmeras**:
   - Habilidades de alto impacto utilizam a integração com `CinematicDirector` no evento `ViewportEvent.ComputeCameraAngles`.
   - Estruturação em múltiplos planos (*Shots*): plano baixo de lançamento, plano orbital heróico em ângulo holandês (*Dutch angle* de 3°–5°), mergulho rasante com zoom de FOV (teleobjetiva de 50°–60°) e plano aberto de impacto no solo com *Screen Shake* quadrático e retorno suave ao controle do jogador.
