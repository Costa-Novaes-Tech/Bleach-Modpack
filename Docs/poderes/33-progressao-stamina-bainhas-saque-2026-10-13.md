> **Estado vigente — ciclo 34, 13/10/2026:** stamina do ciclo 33 homologada; bainha menor, afunilada e curva, lado anatômico corrigido com ajuste de gesto. Tooltip de Resistência na tela K informa stamina máxima. [Arquivos, causa, proporções e roteiro](34-refinamento-bainha-tooltip-resistencia-2026-10-13.md). Refinamento visual aguarda homologação; registros anteriores abaixo são históricos.

# Ciclo 33 — Progressão de stamina, bainhas e saque

Data: 13/10/2026. Branch: Feature-Poderes-bankais. Implementação autorizada pelo usuário após aprovar a proposta de progressão por Resistência, bainha estática e transição de saque/guarda. Não presume homologação visual deste ciclo ou do ciclo 32.

## Progressão e balanceamento

Base: M05, Resistência influencia stamina máxima; curva exata estava pendente. Nesta etapa, usa os níveis comprados de Resistência já existentes no código, sem migrar os sete atributos atuais para o sistema futuro de cinco atributos/escala 10.000.

Fórmula provisória: máximo = 100 + 100 × R / (R + 20), com R não negativo. R0 = 100, R10 ≈ 133,33, R20 = 150, R40 ≈ 166,67. Crescimento com retornos decrescentes e limite 200; valores extremos são limitados e calculados em double para evitar overflow. Maestria continua responsável pelo acesso às habilidades, não pelo recurso físico.

Comprar Resistência aumenta a capacidade sem adicionar stamina corrente. Reserva atual é preservada; redução do máximo limita o saldo ao novo máximo. Recuperação continua em 20/s após 20 ticks, corrida 5/s, ataque básico até 4 por tentativa; limiar de retomada da corrida 10. De zero até a reserva completa são aproximadamente 6 segundos na base 100 e até 11 próximos de 200, incluindo pausa. Respawn restaura o máximo novo. Custos especiais e reiatsu permanecem seus contratos anteriores. Valores ainda sujeitos a playtest.

## Bainha e animação

Uma bainha fica na cintura do lado oposto ao braço principal. Exibe somente a zanpakutō vinculada: precisa estar no inventário principal/hotbar ou em uma mão. Arma de outra identidade não cria bainha, e duplicatas não criam múltiplas bainhas. Baús, ender chest, bolsas aninhadas e itens no chão não contam como arma carregada.

Asauchi recebe bainha cinza escura e detalhes discretos; Ryujin marrom/carbono escuro; Hyōrinmaru azul-petróleo, cabo ciano e guarda em cruz. Geometria nova e materiais vanilla; não altera modelos ou texturas das espadas homologadas. Bainha fica vazia quando a respectiva espada está em qualquer mão, mesmo havendo cópia no inventário. Invisibility, espectador, morte e arma ausente ocultam a camada.

Transição cosmética de oito ticks (aproximadamente 0,4 segundo a 20 TPS) ao alternar guardada/empunhada. Braço alcança a cintura e assume a pose da arma; guardar faz gesto de retorno, com cabo assentando na bainha. Primeira pessoa recebe movimento de mão/item correspondente. Primeira observação, troca de identidade, remoção da arma e troca direta entre mãos não inventam um saque completo. Não adiciona custo, dano, trava, tecla nova ou atraso funcional de ataque. É uma animação nativa simplificada de pose/deslocamento; não usa esqueleto externo ou sequência cinematográfica de contato preciso entre lâmina, cabo e bainha.

Esta primeira fatia usa selecionar/deselecionar o slot vanilla como gatilho solicitado pelo usuário. M06-07HD deixa o controle de empunhar/embainhar para definição futura e prevê também uma ação independente de trocar slot: essa ação dedicada continua futura.

## Arquivos e modificações, passo a passo

1. `common/StaminaPolicy.java`: maximum(int resistance), curva comum utilizada pelo servidor e dados de progressão.
2. `common/data/ResourcesData.java`: máximo variável, setter validado sem recarga, recuperação e fill limitados pelo máximo. Snapshot envia maxStamina; NBT sanitiza os valores.
3. `common/data/PlayerData.java`: refreshDerivedResources deriva stamina junto com os recursos existentes. Compra de atributo já usa ProgressionService.sync/normalize e full sync, portanto aplica e distribui o novo máximo. Load completo recalcula capacidade pelos atributos; snapshot parcial de recursos usa máximo enviado pelo servidor. Schema 7 aditivo; técnicas legadas continuam somente para schema anterior a 5.
4. `client/hud/ReiatsuHud.java`, `pt_br.json`, `en_us.json`: barra proporcional e texto corrente/máximo variável. Números visuais arredondados para cima; regra de gasto usa valor real.
5. `common/ZanpakutoCarry.java`: determina item vinculado e estado 0 ausente, 1 guardado, 2 mão principal, 3 mão secundária. Verifica posse no inventário sem alterar itens. update informa somente mudança do estado.
6. `common/data/CharacterData.java`: carryState é projeção transitória pública, incluída em saveAppearance, não em save persistente. Leitura limitada a 0–3. Dados de identidade/formas existentes permanecem.
7. `server/events/TickHandler.java`: avalia presença durante tick do jogador e envia aparência somente quando o estado muda, incluindo retirada do item, morte/espectador. Sem mapa global de inventários ou itens duplicados.
8. `common/network/SyncHelper.java`: appearance/appearanceTo atualizam estado ao formar snapshot. Usa canal de tracking já existente; novos observadores recebem aparência correta sem acesso ao inventário completo.
9. `common/visual/ZanpakutoDraw.java`: temporização cosmética isolada e testável, reconhece transições, mão e identidade; sem alteração em combate.
10. `client/model/ZanpakutoMotion.java`: cache com chaves fracas de jogadores, sem referência ao jogador nos valores. RenderLivingEvent.Pre aplica pose de braço via ArmPose nativo e RenderHandEvent aplica deslocamento em primeira pessoa. Estado local usa inventário disponível; remoto usa projeção pública. O cache é limpo sem jogador local.
11. `client/model/ZanpakutoSheathLayer.java`: geometria própria acoplada ao torso para acompanhar caminhada/agachamento; materiais e cores de cada identidade com textura vanilla white_concrete. Espada empunhada continua sendo renderizada pelos modelos existentes, sem trocar suas texturas.
12. `client/events/ModEntityRenderers.java`: registra geometria e camada para skins default/slim junto com IceArmorLayer. `ClientForgeEvents.java`: limpeza do cache ao desconectar.
13. `Reference.java`: protocolo 2.9. Cliente e servidor precisam da mesma build. Não altera IDs de pacotes; acrescenta projeção aos snapshots existentes.
14. `MvpRegressionTest.java` e `gametest/CarryGameTests.java`: curva, limite, persistência, compra sem recarga, redução de máximo, timeline de mãos/identidades, presença vinculada, ausência e projeção sem inventário/persistência.

## Compatibilidade e limites

Load de personagem antigo conserva níveis comprados e stamina corrente; aplica a curva sem prêmio de recarga. Snapshot antigo sem novo máximo usa padrão existente até normalização. Não migra automaticamente o sistema de treinamento por uso de atributos proposto nos documentos gerais. Regras de maestria, técnicas, espadas indestrutíveis e limpeza de combate continuam.

As camadas são código nativo Forge/Minecraft 1.20.1. Sem download de assets externos ou dependência nova. A geometria e pose compilam, mas encaixe, beleza, clipping, primeira pessoa e leitura por outro cliente precisam de homologação visual. A armadura da Hyōrinmaru não foi alterada para acomodar o acessório; verificar sobreposição no playtest. A animação é visual e não faz validação física do movimento de sacar.

## Roteiro de homologação

1. Atualize cliente e servidor. Confira protocolo 2.9 e stamina no HUD. Com stamina parcialmente gasta, compre Resistência pela tela de atributos: máximo cresce, saldo não ganha recarga. Confira R20 = 150. Reconecte e confirme máximo/saldo. Respawn restaura o máximo.
2. Vincule Asauchi, deixe a espada em um slot não selecionado e use F5: uma bainha com cabo aparece na cintura. Repita com Ryujin e Hyōrinmaru. Modelos/texturas originais devem permanecer ao empunhar e transformar.
3. Coloque duas identidades no inventário: só a vinculada aparece. Duplique a própria arma: somente uma bainha. Retire todas as cópias para baú/jogue fora: bainha desaparece.
4. Selecione a espada na hotbar: observe gesto curto de saque e bainha vazia. Volte para slot vazio ou outro item: gesto de guarda/cabo. Teste primeira pessoa e F5, mão principal esquerda/direita e mão secundária. Troca rápida de slots não deve deixar pose presa.
5. Confira caminhada, corrida, agachamento, salto, skins default/slim, armadura normal e Hyōrinmaru Shikai/Bankai. Anote sobreposição e ângulo/altura da bainha para refinamento. Invisibility e espectador não devem exibir o acessório.
6. Em segundo cliente, observe guardada, saque, mão secundária, descarte e troca de identidade. Entre no alcance de um jogador já equipado: estado inicial correto, sem saque artificial. Cada jogador conserva seu próprio vínculo.
7. Reconfirme stamina da corrida/ataques básicos, técnicas só com reiatsu, estado de combate e durabilidade removida. Nenhuma ação deve aguardar a animação visual para executar.

## Validação e continuidade

`check build runGameTestServer --offline --console=plain`: 73 cenários de regressão e todos os 63 GameTests obrigatórios aprovados, log `build/cycle33-validation.log`. Após ajustes visuais de cor/guarda em primeira pessoa, build/check final registrado em `build/cycle33-visual-build.log`. JAR `build/libs/bleachmod-0.2.0.jar`. Build/check final também concluído com sucesso, repetindo os 73 cenários de regressão após os ajustes de apresentação. Sem homologação visual presumida.

Próxima etapa: refinamento de encaixe/pose conforme testes e ajuste da curva se necessário. Depois, esquiva curta com custo de stamina; controle dedicado de embainhar, postura e migração dos atributos em etapas separadas. Aizen permanece futuro.