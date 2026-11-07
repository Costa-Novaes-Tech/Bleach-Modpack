# BleachCraft: vídeos, mods de apoio e lições para o Bleach Mod

**05/11/2026.** Pesquisa a partir dos dois MP4 fornecidos pelo usuário, dos seis prints de relações do CurseForge e de páginas oficiais dos autores. Complementa a [análise inicial](analise-concorrencia-bleachcraft-rebirth-2026-11-05.md). O trabalho foi somente de leitura do produto concorrente: não houve instalação de mod, inspeção de JAR, cópia de assets, teste de FPS ou execução do pack. Nossas alterações nesta rodada são documentais.

## Proveniência e método

| Vídeo local | SHA-256 | Dados verificados | Alcance da análise |
|---|---|---|---|
| `C:\Users\joaop\Downloads\Hollow progression in our bleach Minecraft mod_ BleachCraft Rebirth ... _ TikTok.mp4` | `83145465983b052065aea07dacee2ff11bb603957e5df9fa1c207d57732011bb` | 85,8 s, 576×1024, 30 fps, HEVC, faixa AAC | Quadros em intervalos de 5 s e sequência 34–45 s de 1 em 1 s. |
| `C:\Users\joaop\Downloads\Download.mp4` | `eccbe8484388f0e16a270652aa1851c4229738e30a1f5a7c0c52cd815e400c58` | 97,87 s, 576×1024, 30 fps, H.264, faixa AAC | Quadros em intervalos de 6 s e sequência 76–87 s de 1 em 1 s. |

Inspeção com `tools/media_qa/inspect_media.py` e FFmpeg; amostras adicionais e folhas de contato estão localmente em `build/bleachcraft-video-review/` (diretório de build ignorado pelo Git). O próprio MP4 e seu hash são a fonte recuperável; os quadros extraídos não foram incorporados ao repositório. Os arquivos contêm áudio, mas **não houve escuta/transcrição confiável** nesta rodada; não se avaliou música, fala, mixagem, espacialização ou sincronização sonora. O recorte temporal aproximado abaixo vem de amostragem visual, não de decupagem quadro a quadro integral. Cortes, enquadramento vertical, shader e edição promocional limitam inferências sobre jogabilidade contínua.

## Vídeo 1 — progressão Hollow

| Tempo aproximado | Observado no vídeo | Lição de apresentação; não é prova de implementação |
|---|---|---|
| 0–20 s | Cartelas de evolução; Beast Hollow com formas de escorpião segmentado e ciclope. | Silhuetas e escala diferenciam estágios imediatamente. Modelos específicos e sua origem técnica não são identificáveis. |
| 25–34 s | Figura encapuzada em salão; “Evolution III / Adjuchas” e forma quadrúpede clara no deserto pálido. | Cada estágio tem corpo, cor e cenário próprios, o que dá leitura de progressão mesmo sem HUD. |
| 34–39 s | Encontro no deserto; anel branco aparece, cresce/brilha, corte para esfera de riscos vermelhos e brancos e entrada de “Vasto Lorde”. | A sequência cria preparação visual e impacto radial, mas cortes escondem duração real, recuperação e eventual controle do jogador. |
| 40–50 s | Humanoide menor de máscara; deslocamentos e golpes, arcos brancos/azuis, marca vermelha no chão e grande explosão laranja/roxa. | Vasto Lorde mantém contraste de silhueta com as formas bestiais. Golpes têm sinais de direção e área; quadros de brilho intenso podem encobrir alvo/atacante. |
| 55–70 s | Esfera branca/vermelha e cartela “The Mask Torn / Arrancar”; cenas de Resurrección com cortes rosa/ciano, grande parede ciano e espiral azul/branca. | Identidade de técnicas por paleta, geometria e amplitude. Não se comprova a ordem de desbloqueio ou o número de skills jogáveis. |
| 75–85 s | “Las Noches / The Hub”, encontro com Gin Ichimaru; área vermelha no solo e anéis vermelhos verticais. | Arena e sinalização de área ajudam a ler o encontro com o boss. Ainda é montagem, sem evidência de aviso efetivamente justo ou desempenho. |

**O que o vídeo prova:** apresentação visual de várias formas, ataques e ambientes. **O que ele não prova:** regras de evolução, persistência de save, combate contínuo, cancelamento, sincronização entre dois jogadores, FPS, ou qual biblioteca desenha VFX/câmera. Não atribuir a esfera ou os anéis a AAA Particles/GeckoLib apenas pela aparência.

## Vídeo 2 — trailer promocional

| Tempo aproximado | Observado no vídeo | Lição de apresentação; não é prova de implementação |
|---|---|---|
| 0–30 s | “New Bleach Game!”, ambientes urbanos/noturnos e externos com luz e sombras marcadas; crédito visual “Created by IceeNetwork”. | Mundo e iluminação são parte da percepção de qualidade, mas o trailer pode usar shader/configuração diferente do pack distribuído. |
| 36–48 s | Coluna ciano luminosa e personagem com arma/segmentos ciano flutuantes ao redor. | Um prop animado legível junto ao corpo pode vender poder melhor que partículas sozinhas; não se identifica o rig ou pipeline. |
| 54–72 s | Encontro escuro, leque radial dourado atrás de personagem, círculo ciano com fragmentos e close de personagem. | Composição combina modelo, VFX, cena e enquadramento. O leque posterior é referência de silhueta para Ryūjin/Quincy, sem copiar geometria. |
| 76–87 s | Close do personagem, corte para avenida; de 81 a 85 s surge disparo comprido ciano que atravessa a rua e termina em clarão. | A amostra de 1 s mostra início → crescimento/projeção → persistência curta → impacto/limpeza; falta áudio e quadro a quadro fino para cravar tempos de preparação e keyframes. |
| 90–97 s | Cartela final “BleachCraft Rebirth / Coming Soon”. | O material é promocional e não identifica versão/build atual. Não tratá-lo como demonstração da versão 26.2/NeoForge de 22/10/2026. |

**Síntese visual:** os dois vídeos usam silhueta, câmera, efeitos radiais e ambiente para comunicar progressão/poder. Há cenas com brilho suficientemente forte para esconder detalhes por instantes; em nosso QA vale preservar leitura da pose, alvo e limite da área em ambos os lados da transformação. A qualidade percebida depende da combinação, não de uma única biblioteca.

## Ecossistema: o que cada mod de apoio realmente faz

O núcleo [BleachCraft: Rebirth](https://www.curseforge.com/minecraft/mc-mods/bleachcraft-rebirth) atual lista **26.2/NeoForge**, e sua [relação pública de dependências](https://www.curseforge.com/minecraft/mc-mods/bleachcraft-rebirth/relations/dependencies) declara **GeckoLib como obrigatória**. Isto comprova a dependência, mas não que cada animação observada seja produzida por GeckoLib. O [pack oficial](https://www.curseforge.com/minecraft/modpacks/bleachcraft-official-modpack) de `crayoladev` e o [Rebirth Chronicles](https://www.curseforge.com/minecraft/modpacks/bleach-rebirth-chronicles-bleachcraft-rebirth) de `CursedFoundry` são produtos distintos. Os 25 nomes lidos dos prints pertencem ao recorte compartilhado pelo usuário; página de relações indexada apresentou mais nomes e contagem de 29 em outro momento. Sem manifesto de versão fixada, **não afirmar que a união dos nomes roda junta na mesma versão**.

### Animação, personagem, render e performance

| Mod dos prints | Função comprovada | Aplicação e limite para nosso Forge 1.20.1 |
|---|---|---|
| [GeckoLib](https://www.curseforge.com/minecraft/mc-mods/geckolib) | Animação 3D por keyframes, concorrência de animações e eventos de som/partícula. | **Já está em nosso `build.gradle` e `mods.toml`**. Usar para entidades, acessórios/props e modelos rigados; validar no cliente o vínculo entre prop, corpo e câmera. Dependência do BleachCraft não revela seu código. |
| [Not Enough Animations](https://www.curseforge.com/minecraft/mc-mods/not-enough-animations) | Melhora a representação em terceira pessoa de ações vanilla (comer, arco, escudo, nado etc.); cliente e visual. Há [arquivo Forge 1.20.1](https://www.curseforge.com/minecraft/mc-mods/not-enough-animations/files/6738627). | Candidato de **compatibilidade/legibilidade**, não ferramenta de autoria de poses Bankai. Testar sobreposição com nosso PlayerAnimator e Better Combat, com opções individuais desligáveis. |
| [Skin Layers 3D](https://www.curseforge.com/minecraft/mc-mods/skin-layers-3d) | Extruda a segunda camada da skin; cliente, transparência, volta ao 2D além de ~12 blocos; o autor declara compatibilidade com Player Animator/Better Combat. Há [Forge 1.20.1](https://www.curseforge.com/minecraft/mc-mods/skin-layers-3d/files/all?page=1&version=1.20.1). | Volume cosmético, **não esqueleto novo nem animação**. Ensaio com skin classic/slim, armadura/roupa, Bankai, primeira/terceira pessoa, observador e distância. A declaração genérica de compatibilidade não homologa nossa combinação. |
| [Skin Wardrobe](https://www.curseforge.com/minecraft/mc-mods/skin-wardrobe/files/all?page=1&pageSize=20&showAlphaFiles=hide&version=1.20.1) | Busca SkinsMC, prévia 3D e favoritos no cliente; tem Forge 1.20.1. | Ferramenta de escolha de skin, separada de Skin Layers 3D e de animação. Não tratar skins externas como assets nossos. |
| [Entity Culling](https://www.curseforge.com/minecraft/mc-mods/entityculling/files/all?page=1&version=1.20.1) | Oculta entidades/blocos-entidade sem visibilidade para melhorar render; cliente, com Forge 1.20.1. | Medir perfil com/sem em arena de múltiplos NPCs. Não presumir ganho para partículas do sol ou shaders. |

Para **animar o jogador**, o nosso stack já inclui [PlayerAnimator](https://github.com/KosmX/minecraftPlayerAnimator): camadas com prioridades, transparência e `ModifierLayer` com fade; o autor aconselha correspondência rigorosa entre modelo e rig. Better Combat usa esse caminho para golpes. GeckoLib deve cuidar dos modelos apropriados; eventos de gameplay e sincronização ficam sob controle do mod/servidor. O autor de PlayerAnimator adverte que GeckoLib não é garantido em toda combinação, razão para QA de composição no nosso cliente. Os vídeos não permitem deduzir que BleachCraft use PlayerAnimator ou Better Combat.

### APIs, progressão, multiplayer e experiência do pack

| Grupo | Papel real e fontes | Recomendação para nós |
|---|---|---|
| APIs entre loaders | [Architectury](https://docs.architectury.dev/plugin/introduction/) organiza módulo comum e implementações por loader; [Balm](https://www.curseforge.com/minecraft/mc-mods/balm) abstrai eventos, networking e configuração entre loaders. [MezzConfig](https://www.curseforge.com/minecraft/mc-mods/mezzconfig) cobre configuração, validação, sincronização e migração; há [arquivo Forge 1.20.1](https://www.curseforge.com/minecraft/mc-mods/mezzconfig/files/9019056). | Estudar contratos e migração. Nosso projeto é Forge 1.20.1; adicionar Architectury/Balm só com objetivo multiplataforma/dependência concreta. MezzConfig pode ser avaliado quando a configuração atual exigir uma função específica. |
| Regras contextuais | [Shogi](https://www.curseforge.com/minecraft/mc-mods/shogi) define efeitos e condições dinâmicas; [Waystones 26.1+](https://www.curseforge.com/minecraft/mc-mods/waystones) usa Shogi para requisitos de teleporte. A página de Shogi lista Fabric/NeoForge 26.x, sem Forge 1.20.1 verificado. | Bom padrão de **regras declarativas** para mentor, aquisição e viagem; não dependência direta nesta base. A classificação anterior de Shogi como “conteúdo lateral” estava errada e foi corrigida. |
| Quest e equipe | [FTB Quests](https://www.curseforge.com/minecraft/mc-mods/ftb-quests-forge) é questbook com equipes; [FTB Teams](https://github.com/FTBTeam/FTB-Teams) compartilha progresso de grupo; [FTB Library](https://www.curseforge.com/minecraft/mc-mods/ftb-library-forge) é infraestrutura UI/config FTB. [ID's Questwright](https://www.curseforge.com/minecraft/mc-mods/ids-questwright) gera livros a partir de receitas, avanços, estruturas e dimensões, exige FTB Quests e tem Forge 1.20.1. | Tomar onboarding e coop como referência, preservando quests autorais e contrato M15/party. Questwright não escreve automaticamente lore de Rukia/mentores nem substitui nosso `QuestService`. Nenhum print isolado confirma que FTB Quests estava na mesma versão do pack. |
| Loot, morte e interface | [Lootr](https://www.curseforge.com/minecraft/mc-mods/lootr) fornece loot por jogador; [Mob Health HUD](https://www.curseforge.com/minecraft/mc-mods/mob-health-hud-damage-indicator) apresenta vida/dano; [Corpse](https://www.curseforge.com/minecraft/mc-mods/corpse) guarda itens na morte. | Comparar leitura de combate e responsabilidade de recompensa. Qualquer adoção precisa teste de duplicação de loot/quest, morte, relog e multiplayer, ligado ao P0 de saves. |
| Exploração/inventário | [Waystones](https://www.curseforge.com/minecraft/mc-mods/waystones) usa Balm e altera viagem; [Nature's Compass](https://www.curseforge.com/minecraft/mc-mods/natures-compass) localiza biomas; [Sophisticated Backpacks](https://www.curseforge.com/minecraft/mc-mods/sophisticated-backpacks) + Sophisticated Core ampliam armazenamento; [Iron Apples: Overhauled](https://www.curseforge.com/minecraft/mc-mods/iron-apples-overhauled) exige [Iron Core](https://www.curseforge.com/minecraft/mc-mods/iron-core) e altera sobrevivência; Iron Chests/Furnaces/Generators são progressão de utilidades. | Não são técnicas de animação. Entram como decisões de ritmo, economia, dimensão e balanceamento do pack, não como pré-requisito de Bankai. |
| Outros utilitários dos prints | Mouse Tweaks, Simple Voice Chat, CursedFoundry Loading Screen, Apex Hosting Partner Link, `inzhefop's Core`. | QoL, voz, apresentação, promoção ou biblioteca de terceiros, sem evidência de contribuição direta aos efeitos dos vídeos. Voice Chat pode ser útil em servidores de teste, mas não é pipeline de áudio da transformação. |

A página de relações indexada do **Rebirth Chronicles** também mostrou **Chat Heads, Corpse, Iron Chests, Iron Core, Iron Furnaces, Iron Generators, JourneyMap e JEI** além dos nomes dos prints. Essa é uma fonte com data/versão possivelmente diferente; conferir o manifesto do arquivo exato antes de tratar lista como composição completa. JourneyMap/JEI ajudam navegação/receitas, Chat Heads é UI; os demais pertencem a morte, armazenamento ou utilidades. O item [Iron Furnaces (Fabric & NeoForge), de Astryxion](https://www.curseforge.com/minecraft/mc-mods/iron-furnaces-fabric-neoforge), **não** é o projeto homônimo de outro autor; a página consultada não lista Forge 1.20.1 para essa variante. [Iron Generators](https://www.curseforge.com/minecraft/mc-mods/iron-generators) produz energia; `inzhefop's Core` é biblioteca/utilitários do mesmo autor. Não há indício ali de um “motor secreto” de animações. O antigo [AAA Particles BleachCraft fork](https://www.curseforge.com/minecraft/mc-mods/aaa-particles-bleachcraft-fork) é projeto legado/depreciado; não prova uso pelo núcleo atual.

## Arquitetura que vale treinar em nosso projeto

1. **Corpo do jogador:** esboçar poses em Blockbench com rig que corresponda ao PlayerAnimator; exportar clipes curtos de preparação, ação, hold e retorno. Definir prioridade, máscara/ossos afetados, fade, cancelamento e comportamento durante golpe Better Combat.
2. **Prop e entidade:** GeckoLib para forma, arma, aro, halo, Hollow e entidade cênica. Em cada evento visual, registrar âncora (`jogador`, `mão`, `mundo`, `alvo`), duração e orientação. Não transferir animação do prop para o corpo por suposição.
3. **VFX e som:** servidor decide forma/dano/custo e emite evento único; cliente reproduz partículas, som e câmera com relógio comum. Limitar densidade, distância e opacidade. Mostrar o limite da área antes do impacto e manter a silhueta do atacante em quadros críticos.
4. **Cosméticos:** ensaiar Skin Layers 3D e Not Enough Animations em perfil isolado, mantendo o jogo base como comparação. Prioridade de teste: transformação Ryūjin (pose frontal + sol atrás), golpe Senbon, armadura, skin slim/classic e observador multiplayer.
5. **Saves e API:** antes de agregar bibliotecas de progressão, concluir o [P0](p0-auditoria-dados-saves-2026-11-05.md) e escolher contrato de requisitos/recompensas. Interfaces próprias bem delimitadas para `QuestService`, party, mentor e aquisição facilitam integração eventual sem transplantar arquitetura 26.2/NeoForge.

## Experimentos propostos, com critérios observáveis

| Ordem | Ensaio | Aceite mínimo |
|---|---|
| E1 | Matriz base × Not Enough Animations × Skin Layers 3D × ambos, em cópia de perfil 1.20.1/Forge. | Sem crash; pose Bankai e combo aparecem sem duplicar braço/arma; skin não atravessa armadura de modo grave; opções que conflitem podem ser desativadas. |
| E2 | Protótipo de 4 fases do jogador com PlayerAnimator e prop GeckoLib sincronizados. | Preparação inequívoca, golpe legível, hold estável e retorno/cancelamento sem snap; observador vê a mesma ordem e orientação. |
| E3 | VFX de um ataque comparável aos beats 81–85 s do segundo vídeo, com aviso de área, feixe, impacto e dissipação. | Limite visível antes do dano; pose/alvo legíveis; origem e direção consistentes em primeira/terceira pessoa; métricas de FPS em cena repetível. |
| E4 | Cena longa Hollow/mentor com silhuetas por etapa e consequência de save. | Transição visual explica estágio; aquisição, quest e relog seguem P0; sem depender de montagem/corte de trailer para esconder retorno. |

**Prioridade recomendada:** E1/E2 antes de mudar API; são estudos diretamente relacionados ao pedido de animações mais coesas. E3 após a camada do corpo estar correta. Escolher mods de pack de progressão somente depois de definir M15, economia e fixture P0. Nenhuma instalação foi feita por esta pesquisa.

## Estado das skills

Os achados reutilizáveis deste estudo foram registrados aqui. Antes de inserir regra na skill `bleach-mod-dev`/`bleach-model-pipeline`, validar E1/E2 em cliente real: distinção Skin Layers/PlayerAnimator/GeckoLib, prioridade e compatibilidade são guias de teste, ainda não um padrão homologado para o nosso runtime.
