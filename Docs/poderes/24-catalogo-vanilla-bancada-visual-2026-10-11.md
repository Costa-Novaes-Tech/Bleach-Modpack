> **Estado vigente — ciclo 34, 13/10/2026:** stamina do ciclo 33 homologada; bainha menor, afunilada e curva, lado anatômico corrigido com ajuste de gesto. Tooltip de Resistência na tela K informa stamina máxima. [Arquivos, causa, proporções e roteiro](34-refinamento-bainha-tooltip-resistencia-2026-10-13.md). Refinamento visual aguarda homologação; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 33, 13/10/2026:** stamina máxima por Resistência com retornos decrescentes e sem recarga por compra; bainha da arma vinculada e saque/guarda cosméticos nativos. [Fórmula, arquivos, sincronização, limites e roteiro](33-progressao-stamina-bainhas-saque-2026-10-13.md). Schema 7; protocolo 2.9. Visual e balanceamento aguardam playtest; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 32, 13/10/2026:** ciclo 31 e estado de combate homologados pelo usuário. Golpes básicos de zanpakutō gastam até 4 stamina por tentativa; técnicas preservam custos de reiatsu. As três espadas são indestrutíveis por decisão do usuário. [Contrato, arquivos, lógica e testes](32-ataques-basicos-zanpakutos-indestrutiveis-2026-10-13.md). Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 31, 13/10/2026:** stamina de corrida com pausa, recuperação, exaustão e HUD. Valores provisórios; ciclo 30 segue em teste visual. [Contrato, arquivos, lógica e roteiro](31-stamina-corrida-2026-10-13.md). Schema 6; protocolo 2.8. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 30, 12/10/2026:** primeira fatia de Combat State automático, indicador no HUD e limpeza transitória, sem mudar regeneração/stamina/atributos. [Lógica, arquivos, timeout provisório e testes](30-estado-combate-2026-10-12.md). Protocolo 2.7; schema 5 preservado. Combat Mode/hotbar seguem futuros; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 29, 12/10/2026:** ciclos anteriores homologados pelo usuário. Definições comuns dos 24 slots/formas de Ryujin e Hyōrinmaru consultadas por execução, acesso e HUD; valores preservados. [Arquivos, lógica e validação](29-definicoes-compartilhadas-2026-10-12.md) · [Contrato para novos poderes](contrato-criacao-novos-poderes.md). Registros anteriores abaixo são históricos; nova feature será discutida antes de outro kit.

> **Estado vigente — ciclo 28, 11/10/2026:** feedback de maestria sem arredondar limiares, avisos de novas liberações pelo treino, distinção de descoberta/compra/disponibilidade e indicação de treino suspenso em modo teste. [Arquivos, lógica e roteiros de visuais/maestria](28-feedback-maestria-roteiro-2026-10-11.md). Ganhos e requisitos preservados; ciclos abaixo são históricos.

> **Estado vigente — ciclo 27, 11/10/2026:** cortes, barreira, Zona Glacial, impacto de controle e trilha do dragão da Hyōrinmaru usam o emissor comum, mantendo aparência e combate. [Arquivos, lógica, valores e testes](27-migracao-slots-gelo-2026-10-11.md). Clima e terreno conservam sua lógica; registros abaixo são históricos.

> **Estado vigente — ciclo 26, 11/10/2026:** aura e liberação da Hyōrinmaru integradas ao emissor comum com audiência próxima e orçamentos explícitos, preservando a aparência aprovada. Bancada continua privada. [Arquivos, lógica, limites e roteiro](26-integracao-gelo-emissor-comum-2026-10-11.md). Os ciclos anteriores abaixo são históricos; Aizen e narrativa permanecem futuros.

> **Estado vigente — ciclo 25, 11/10/2026:** biblioteca comum com paletas fire/ice/reiatsu e formas aura/cut/beam/release; camadas compartilham orçamento. Bancada anterior preservada; nenhuma mudança nos kits homologados. [API, arquivos, limites e comparação](25-biblioteca-paletas-camadas-2026-10-11.md). Escolha visual e integração em produção são próximos gates; Aizen e narrativa continuam futuros.

# Ciclo 24 — Catálogo vanilla e bancada visual

Data: 11/10/2026. Branch: Feature-Poderes-bankais. Minecraft 1.20.1 / Forge 47.4.10.

## 1. Estado e escopo

O usuário homologou Asauchi e maestria do ciclo 23 e autorizou iniciar o reaproveitamento vanilla. Este ciclo entrega catálogo técnico e bancada de comparação de partículas, geometria, som e intensidade. Não altera kits, armas, modelos, texturas, atributos, progressão, clima ou inicializador. Nenhuma dependência nova.

A biblioteca de produção e aquisição narrativa são etapas posteriores, dependentes da seleção visual e do planejamento da equipe. Aizen permanece futuro. Os presets são demonstrações, não técnicas de combate.

## 2. Catálogo verificado

Inventário conferido nos arquivos locais da versão 1.20.1: client.jar/client-extra.jar do cache ForgeGradle e sounds.json do índice de assets 5. Esses caminhos servem para inspeção, não são dependências da instalação de outros jogadores. Referências minecraft:... usam recursos já instalados pelo jogo, sem copiar PNGs para o repositório.

Prefixo dos arquivos abaixo: assets/minecraft/textures/. Identificadores de partículas e sons não são caminhos de PNG.

| Família | Arquivos disponíveis | Aplicações e adaptações |
| --- | --- | --- |
| Gelo | block/ice.png, packed_ice.png, blue_ice.png | placas, lâminas e cristais; ajustar UV, tonalidade e transparência |
| Gelo rachado | block/frosted_ice_0.png até frosted_ice_3.png | estágios de ruptura/congelamento visual; alternar textura não muda física |
| Neve | block/snow.png; misc/powder_snow_outline.png | superfície e overlay de frio; camada/renderer ou sistema vanilla |
| Cristal | block/amethyst_block.png, amethyst_cluster.png, small_amethyst_bud.png, medium_amethyst_bud.png, large_amethyst_bud.png | estruturas espirituais, fragmentos e selos; geometria e paleta |
| Energia pulsante | block/sculk.png; sculk_vein.png e .mcmeta | marcas e veios animados; atlas/renderer deve respeitar animação |
| Carbonização | block/obsidian.png, crying_obsidian.png | superfícies queimadas e fissuras; adaptar cores |
| Fogo | block/fire_0.png, fire_1.png, soul_fire_0.png, soul_fire_1.png | planos de chama; geometria, transparência e animação do atlas |
| Correntes | block/chain.png | acessórios; criar segmentos/articulação, PNG não cria corrente |
| Energia sobreposta | misc/enchanted_glint_item.png, enchanted_glint_entity.png; entity/wither/wither_armor.png | camada móvel; precisa de render pass/animação |
| Feixe | entity/guardian_beam.png | raios espirituais; renderer/UV, não projétil pronto |
| Plano dimensional | entity/end_portal.png; environment/end_sky.png | referências de ambientação; efeito depende também do renderer |

### Modelos e sistemas

| Base | Possível uso | Limite |
| --- | --- | --- |
| Phantom | criaturas espirituais e asas | já usado no Ice Dragon; adaptar silhueta/UV |
| Ender Dragon | invocações maiores | renderer/animação mais complexos; não herdar comportamento de boss |
| Elytra | asas e acessórios | posicionamento, textura e animação |
| PlayerModel / humanoides | NPCs e camadas corporais | skin, proporções, poses e equipamento |
| Evoker Fangs | estacas surgindo do chão | adaptar entidade/duração/dano; bancada não invoca entidade |
| End Crystal | núcleo de ritual | visual próprio, sem herdar explosão |
| Modelos de itens / Armor Stand | referência de exposição | catálogo não coloca entidades persistentes |

### Partículas e sons

- Frio: snowflake, cloud e dust.
- Reiatsu: end_rod, enchant, enchanted_hit e electric_spark.
- Espírito: soul, soul_fire_flame e dragon_breath.
- Corte: sweep_attack e crit.
- Transição: dust_color_transition, reverse_portal e flash.
- Pulso: sonic_boom; efeito visual não herda dano do Warden.
- Sons confirmados: block.amethyst_block.chime/resonate, block.beacon.activate/ambient, block.chain.*, block.enchantment_table.use e entity.evoker.*.

Partículas e som são apresentação. Não aplicam dano, controle, maestria ou mudança de terreno por si mesmos.

### Material do projeto

As bases katana_handheld/ryujin_katana_handheld e overrides de formas orientam futuras katanas sem alterar as existentes. Hyōrinmaru já usa texturas vanilla, Phantom e packed_ice na armadura/asas/cauda. IceArmorLayer demonstra geometria própria com textura de bloco. Ryujin tem superfícies próprias homologadas de fogo/carbonização. HUD, ícones, NPCs e telas servem de referência de consistência; presença do arquivo não implica que esteja sem uso.

## 3. Bancada implementada

Comandos de operador nível 2. preview/status/stop atuam somente no executor jogador e não aceitam seletor de terceiros. list pode ser consultado no console.

```text
/bleachdev assets list
/bleachdev assets preview frost
/bleachdev assets preview reiatsu 2
/bleachdev assets preview soul 1
/bleachdev assets preview slash 1
/bleachdev assets preview beam 3
/bleachdev assets preview crystal 2
/bleachdev assets status
/bleachdev assets stop
```

| Preset | Geometria | Partículas | Som inicial |
| --- | --- | --- | --- |
| frost | anel baixo, raio 2 | snowflake + dust azul | vidro quebrando |
| reiatsu | espiral vertical de 3 blocos | end_rod + enchant | Beacon ativando |
| soul | espiral vertical de 3 blocos | soul + soul_fire_flame | soul_escape |
| slash | arco vertical de 2 blocos | sweep_attack + crit | golpe amplo |
| beam | linha de 6 blocos | electric_spark + end_rod | Guardian |
| crystal | espiral de raio 0,8 e altura 2,5 | dust violeta + reverse_portal | ametista |

Origem quatro blocos à frente, na altura dos pés; direção fixada no lançamento. Beam é linha de partículas, não renderer guardian_beam.png. Crystal não coloca cristais; Soul não invoca mobs. Modelos/superfícies catalogados são opções futuras, não todos demonstrados nesta primeira bancada.

### Orçamento e ciclo de vida

- Duração 100 ticks: cinco segundos a 20 TPS.
- Intensidades 1/2/3: 8/16/24 pontos com duas partículas por ponto.
- Emissão a cada quatro ticks: até 16/32/48 solicitações por frame, 400/800/1200 por sessão.
- Um preset por jogador; novo preview substitui o anterior. Até oito sessões por servidor.
- Som local uma vez, volume 0,35. Partículas enviadas diretamente ao dono, sem broadcast.
- Não força chunks: descarta amostras em chunks indisponíveis.
- Encerra por duração, stop, logout, morte, dimensão, descarga do mundo, encerramento do servidor ou distância acima de 32 blocos da origem.
- Deduplicação impede frames extras no mesmo tick. stop impede novas emissões; partículas/som já enviados terminam conforme duração vanilla.
- status mostra preset, ticks restantes e solicitações. Não mede FPS, GPU, TPS ou número realmente desenhado; configurações do cliente podem reduzir o visual.
- Não altera reiatsu, cooldowns, vínculo, maestria, NBT, blocos, mobs ou clima. Não exige personagem criado.

## 4. Arquivos e sequência técnica

1. common/visual/VanillaAssetBench.java: catálogo fechado, orçamento, geometria, emissão local, sessões limitadas e limpeza Forge.
2. server/commands/AssetBenchCommands.java: subcomandos, sugestões, permissão e validação. ID/intensidade inválidos preservam sessão existente.
3. server/commands/BleachCommands.java: conecta bancada à raiz administrativa.
4. assets/bleachmod/lang/pt_br.json e en_us.json: feedback administrativo.
5. gametest/AssetBenchGameTests.java: permissões, comandos inválidos, duração, deduplicação, isolamento dos dados, limpeza, capacidade e substituição.
6. src/test/java/com/bleachmod/MvpRegressionTest.java: geometria finita/limitada e catálogo/orçamento. MasteryGameTests.java: isolado verticalmente o alvo do teste de controle, evitando selecionar mobs de outras fixtures simultâneas; nenhuma lógica da habilidade alterada.
7. Docs/poderes: catálogo, manual, progresso e continuidade. Documentos fora desta pasta intactos.

Caminhos Java 1–3 e 5 começam em src/main/java/com/bleachmod. Não altera protocolo ou schema. O gerador é base de experimentação, sem inserção nos executores homologados.

## 5. Homologação e escolhas

1. Inicie com Iniciar-Teste em mundo de teste; escolha área aberta e espaço livre à frente.
2. Como operador, liste presets e compare cada um na intensidade 1.
3. Repita nas intensidades 2/3 com mesma câmera, cenário e configuração de partículas.
4. Use F3 para comparar FPS com repouso antes/depois; ferramentas existentes de perfil avaliam servidor. status não é benchmark.
5. Registre imagem/clipe, preset, intensidade, distância, configuração, legibilidade, FPS e preferência.
6. Com dois jogadores, confirme que cada um vê apenas sua própria bancada. Habilidades normais continuam independentes.
7. Confirme ausência de dano, perda de recursos, ganho de domínio e alterações de terreno/clima.
8. Teste stop, afastamento, morte, logout e dimensão; confirme ausência de novas emissões. Aguarde partículas antigas terminarem.
9. Sem OP, confirme recusa.
10. Escolha paletas/combinações antes de integrar novos assets ou biblioteca de produção.

Próxima etapa: selecionar uma família visual e compor efeitos com orçamento comum; adaptar somente modelos/superfícies necessários. Treinos/provas narrativas continuam com a equipe.

## 6. Validação

Validação final: check, build e runGameTestServer offline aprovados; 62 regressões e 46 GameTests obrigatórios passaram. Log local: build/cycle24-validation.log. git diff --check aprovado; modelos/texturas e documentos fora de Docs/poderes intactos. JAR: build/libs/bleachmod-0.2.0.jar. Homologação visual e FPS ficam pendentes do usuário.

Referências oficiais: [recursos Forge 1.20.1](https://docs.minecraftforge.net/en/1.20.1/concepts/resources/) e [partículas Forge 1.20.1](https://docs.minecraftforge.net/en/1.20.1/gameeffects/particles/).
