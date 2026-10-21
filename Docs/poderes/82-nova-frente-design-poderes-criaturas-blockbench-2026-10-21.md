# Nova frente — criação e design de poderes e criaturas

Atualização: 21/10/2026. Documento de passagem e instrução operacional para outra IA/conversa. Referência local inspecionada: `3725f85`, branch `Feature-Poderes-bankais`, árvore limpa antes desta atualização. Verificar novamente o Git; o hash identifica a inspeção, não uma ordem para restaurar versões.

## 1. Missão e limites desta passagem

Você dará continuidade ao Bleach Mod, criando e refinando poderes, criaturas, modelos, texturas, animações, VFX, áudio e apresentações em Minecraft Java. Use as ferramentas instaladas e os componentes já integrados, em especial Blockbench, GeckoLib, PlayerAnimator e Better Combat. Comece com uma fatia pequena, funcional e demonstrável no cliente real.

O pedido que originou este documento foi **preparar instruções para criar uma nova branch**. A branch ainda não foi criada por esta atualização; nenhuma criatura, habilidade ou asset foi implementado aqui. Não interpretar exemplos de poderes ou ferramentas como aprovação para instalar tudo ou substituir sistemas homologados.

Ao receber do usuário a tarefa de iniciar esta frente, crie a branch descrita abaixo antes de editar código/assets. Identifique a primeira entrega com base na solicitação da nova conversa e no roadmap; não escolha sozinho uma Bankai/personagem que foi adiado.

## 2. Entrada obrigatória e criação da nova branch

1. Confirme o workspace: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack`.
2. Procure instruções locais atuais, consulte `.agents/skills/bleach-mod-dev/SKILL.md` e os trechos relevantes de suas referências. Informe o uso da skill. Não sobrescreva AGENTS.md, skills ou configuração de agentes para iniciar esta frente.
3. Inspecione status, branch, log e diferenças antes de qualquer mudança. Preserve alterações de outras conversas e trabalhos da equipe.
4. Nome proposto da nova branch: **`codex/design-poderes-criaturas`**. É um nome sugerido neste documento; seguir outro nome explícito do usuário se houver.
5. Base recomendada: o estado atual revisado de **`Feature-Poderes-bankais`**, que contém os ciclos recentes. Não partir silenciosamente de main ou de um estado antigo: comparar histórico e confirmar que o conteúdo necessário está presente.
6. Se a branch proposta não existir e a base estiver adequada, criá-la localmente. Se já existir, inspecionar o que contém antes de reutilizar; nunca recriá-la à força.

Comandos ilustrativos, executar somente após essas verificações:

```powershell
git status --short
git branch --show-current
git log -1 --oneline
git branch --list codex/design-poderes-criaturas
# Com base revisada, árvore compatível e nome ainda livre:
git switch -c codex/design-poderes-criaturas Feature-Poderes-bankais
```

Com alterações alheias, não usar reset, clean, checkout forçado ou stash automático. Preservar o trabalho e escolher isolamento apropriado; pedir orientação apenas quando houver conflito real sobre o que deve entrar na base. Não mover o trabalho de outra conversa.

O pedido de nova branch é uma exceção à antiga orientação de trabalhar exclusivamente na Feature-Poderes-bankais para esta nova frente. **Nunca desenvolver/comitar diretamente em main.** Criar branch não publica nada. Commit, push, merge e instalação devem respeitar a autorização vigente do usuário; não deduzir autorização de publicação deste texto. Não executar pull/rebase/merge de terceiros como etapa automática.

A antiga Feature-Bankais-Poderes foi informada como removida no GitHub; uma referência local ainda existe nesta inspeção. Não usá-la como base, recriá-la remotamente ou excluí-la apenas para cumprir esta tarefa.

## 3. Contexto do jogo e regras que continuam valendo

- Minecraft 1.20.1, Forge 47.4.10, Java 17, Gradle Wrapper 8.8; mod 0.2.0. Runtime inspecionado: protocolo **2.31**, schema **9**, revisão de técnicas **3**. Confirmar em Reference, PlayerData e CharacterData antes de alterar.
- Jogador original dentro do universo Bleach, progressão contextual e poder principal de identidade única. Uma zanpakutō vinculada por vez, com progresso próprio; outra identidade não transfere automaticamente seus poderes.
- Asauchi, Ryūjin Jakka, Hyōrinmaru, Senbonzakura/Byakuya, Benihime e Zaraki/Nozarashi já têm implementação em graus distintos. Não tratar esses kits como inexistentes.
- Stamina, esquiva, guarda/Postura, maestria e treino já têm implementação. O plano antigo de começar a esquiva foi superado. Ler os serviços atuais antes de propor reconstrução.
- Sete atributos no código: Zanjutsu, Hakuda, Vitalidade, Resistência, Kidō, Reserva e Controle. Cinco atributos universais e modelo disciplinar futuro ainda exigem migração própria. Não converter durante autoria visual.
- Separar atributo, proficiência geral e maestria individual. Postura serve à quebra de guarda; não é outro custo geral de técnicas. Técnicas especiais não recebem stamina adicional por conveniência de VFX.
- Espadas indestrutíveis, bainhas e vínculo visual, persistência, maestria por prática e migrações anteriores devem continuar funcionando. Preservar especialmente modelos/texturas da Asauchi.
- Ryūjin/Hyōrinmaru e fundamentos tiveram homologações anteriores; revisões recentes de animação não herdam automaticamente esse aceite. Zaraki está integrado/aprovado e deve permanecer intacto nesta frente. Senbon/Byakuya também não deve ser remodelado incidentalmente.
- Aizen: poderes adiados; não excluído de narrativa/NPCs. Gin: retirado da fila desta frente, sem ordem de apagar recursos narrativos.
- A equipe já desenvolve mais Hollows segundo o usuário: conferir contratos/commits/branches antes de duplicar entidades, IA ou modelos.
- Documentação nova desta frente permanece em **Docs/poderes**; evidências podem usar **Docs/poderes/qa/<feature>**. Não modificar Docs/game-design, Docs/arte ou outros documentos sem nova orientação. Esses locais são fontes de consulta, não autorização de edição.

## 4. Estado recente que a nova IA precisa preservar

### Benihime, ciclo 81

Entrada de 560 ticks no local de invocação; caminhar libera câmera/pose do jogador sem pular a revelação. Ao concluir, a manifestação acompanha o dono. Transformação conserva commit no tick 40 e liberação curta de 60 ticks. Cancelamentos pertinentes continuam exigindo limpeza de câmera, áudio e estado.

Integração efetiva ao Better Combat com três ataques próprios: diagonal, retorno e estocada. Alcance do perfil 3,0; controle/validação no servidor continuam. Não confundir gesto cosmético com a autoridade real de acerto.

Selada possui H saque da bengala, N Jūgeki Byakurai, B seis hastes e C Gigai portátil. A isca visual atual é um manequim temporário, não um personagem humano autorado em Blockbench. Possível alvo de refinamento futuro, sem alterar mecânica por consequência do modelo.

Hiasobi independente: fios, alcance 18, três contatos; interação com rede preservada. Costura Final: raio 12, base total 40 e ruptura que pode destruir terreno permanentemente dentro da política de materiais/proteções existente. Não ampliar destruição ou alterar custos por causa de uma animação mais imponente.

### Ryūjin e Hyōrinmaru

Higashi foi reaberta/revisada: cena deve depender de morte confirmada, sem prévia indevida para alvo sobrevivente; preparação junto ao quadril e corte ascendente possuem revisão recente. Investida/Rajada têm refinamentos, e o dash retomou corpo horizontal/giratório sem ativar um ataque Riptide vanilla.

Hyōrinmaru já possui IceDragonEntity/GeoEntityRenderer e arquivos `geo/ice_dragon.geo.json` e `animations/ice_dragon.animation.json`. Melhorar dragões, rig, textura e transformação deve reaproveitar esse ponto de integração, sem voltar a tratar a criatura como simples Phantom sem modelo próprio.

### Mundo e sistemas compartilhados

Criação/aparência, telas JSON, NPCs, quests, dimensões/mapas e Modo História têm implementação parcial. Campanha completa, ensino, proficiências e raças jogáveis completas não estão concluídos. Mapas presentes não equivalem a portais finais nem a uma campanha pronta. Não reabrir tudo durante a produção de um asset.

## 5. Roadmap de referência e escolha da primeira fatia

A ordem registrada no ciclo 72, com complementos até o ciclo 81, é:

1. Fechar apresentação da entrada Benihime, movimento, escuta e aceite pertinente.
2. Revalidar Higashi revisada.
3. Revalidar Investida/Rajada Ryūjin e giro do dash.
4. Revisar combate e técnicas Benihime, incluindo a entrega recente de selada/Hiasobi/Costura Final.
5. Contratar e implementar o kit Uryū Ishida.
6. Mentor/Proficiências, após confirmação da equipe.
7. Refinar Hyōrinmaru: dragões, animações e transformação com Blockbench.

São entregas/revisões em estados diferentes, não sete features inteiramente ausentes. Nova branch não significa que todos os itens foram concluídos. Se o usuário direcionar esta frente para um item específico ou criatura nova, registrar essa mudança de foco sem marcar artificialmente as pendências anteriores como encerradas.

Depois da lista, a direção proposta é NPCs de combate e quests da lore, alinhada ao trabalho Hollow da equipe. NPC combatente não implica ajudante canônico nas lutas de boss: preservar os contratos narrativos.

Para iniciar, apresentar um contrato curto: personagem/criatura, fantasia visual, silhueta, dimensão/escala, modelo, ações, estados, ataques, materiais, animações, regras preservadas e cena de demonstração. Delimitar o que será efetivamente entregue antes do polimento amplo.

## 6. Ferramentas disponíveis, registradas e opcionais

### Já integradas ao projeto

- GeckoLib 4.8.4; intervalo declarado 4.8.4 a menor que 4.9. Para entidades/modelos animados, usar a API efetivamente instalada.
- PlayerAnimator 1.0.2-rc1: animações do corpo do jogador compatíveis com a estrutura existente. Não aplicar formato de criatura diretamente ao esqueleto do player.
- Better Combat 1.9.0 e Cloth Config 11.1.136: perfis de ataque e dependências atuais. Conservar autoridade/validação e formatos próprios.
- Photon/LDLib: perfil opcional, separado. Base funcional sem esses mods. Não converter Photon em dependência obrigatória inadvertidamente.
- Scripts locais para modelos, animações, áudio e GUI em tools; bancada AssetBenchCommands e laboratórios de QA existentes. Descobrir opções reais antes de inventar comandos.

### Autoria local — verificar antes de usar

O ciclo 68 registra Blockbench portátil Windows 5.2.2 e plugin GeckoLib Models & Animations 4.2.5 pelo gerenciador oficial. Versão do plugin de edição não é versão do GeckoLib runtime. FFmpeg 7.1 foi reutilizado localmente. O manifesto referido fica em build/benihime-qa-68/tools-manifest.json, diretório de trabalho que pode não existir num clone novo; não é fonte garantida.

Na inspeção de 21/10, Java e Git foram encontrados no PATH; Blockbench, FFmpeg/ffprobe, Blender e Audacity não foram encontrados por Get-Command. Isso não prova ausência de executáveis portáteis. Localizar os recursos registrados e verificar arquivos/manifestações antes de concluir que precisam ser instalados. Não há nova comprovação de abertura desses editores nesta rodada documental.

Blender, Audacity, Krita/GIMP, OBS e geração de imagens são alternativas condicionais, não instalações/contas confirmadas. Usar somente se resolverem uma necessidade real: malha complexa, pintura, edição/mixagem, captura ou concept. Concept gerado não é automaticamente textura UV ou modelo de produção. Ler as skills relevantes se usar ferramentas de imagem/GUI.

Preferir ferramentas locais gratuitas necessárias, fontes oficiais e versões compatíveis. Não atualizar Java/dependências cegamente. Mudanças de sistema/PATH global, administrador, ferramentas pagas ou envio de código/assets/áudio para nuvem exigem autorização específica conforme o contexto. Não copiar assets comerciais ou extrair recursos de outros jogos sem licença. Registrar origem e atribuição dos recursos externos.

## 7. Pipeline de produção — do conceito ao jogo

1. **Inventário:** examinar modelos/fontes/rigs atuais, escalas, materiais, âncoras, animações e receptor runtime. Registrar o que pode ser reaproveitado.
2. **Contrato visual:** silhueta, proporção de katana/criatura/corpo, leitura em combate, paleta, materiais e contraste. Separar aparência de dano/alcance.
3. **Prova vertical:** um modelo simples, uma ação e uma cena real; provar carregamento e estados antes de produzir dezenas de animações.
4. **Fonte editável:** guardar `.bbmodel` em tools/models/source ou convenção existente adequada, com nomes distintos por asset. Conservar fontes e passos de geração reproduzíveis. Não editar só um binário/export sem fonte.
5. **Rig e UV:** pivôs coerentes, hierarquia de bones, simetria e lados anatômicos, articulações suficientes sem excesso; UV sem regiões involuntariamente esticadas ou escondidas. Nomear bones estáveis para âncoras de VFX/armas.
6. **Animação:** idle/movimento e preparação/impacto/recuperação dos ataques pertinentes. Keyframes suaves, apoio/peso, mãos/cabo e contato com o chão. Som, câmera e VFX seguem marcos claros; não criar custo/hitbox a partir de frame visual no cliente.
7. **Export correto:** GeckoLib usa `.geo.json`/`.animation.json`/textura; item JSON e animações PlayerAnimator/Better Combat seguem formatos distintos. Mapear nomes de bones e IDs entre autor/editor/runtime. Preservar a ligação de resources e registries.
8. **Integração:** entidades e combate no servidor; renderer, partículas, áudio e câmera no cliente. Dados públicos mínimos, tracking/observador e limpeza em morte/cancelamento/dimensão/logout. Assets ausentes devem gerar diagnóstico claro.
9. **Polimento e limites:** orçamento de partículas, distância de audiência, geometria/UVs/texturas, compatibilidade com skins/bainhas/armaduras, áudio com fala inteligível e alcance de câmera. Medir desempenho no cenário real, não inventar metas cumpridas.
10. **Regressão e evidências:** testar e documentar como descrito abaixo. Entrega só fica homologada quando o critério correspondente tiver evidência e/ou aceite exigido.

Atenção: `tools/models/source/benihime_entrance.bbmodel` é fonte de autoria/scaffold. O script tools/models/benihime_entrance.py declara explicitamente que o runtime atual usa geometria procedural. Exportar/editar esse arquivo sozinho **não atualiza** automaticamente a Benihime em jogo. Uma migração integral exige contrato próprio e comparação com o comportamento existente. Da mesma forma, não tornar BendyLib global e alterar poses homologadas por conveniência.

## 8. Código e recursos a localizar por responsabilidade

- Dados/rede: common/data, common/network, Reference.java e serviços de progressão/techniques.
- Criaturas: entity, registries e renderizadores/modelos de cliente; IceDragonRenderer é uma referência GeckoLib existente.
- Benihime: client/cinematic/BenihimeEntranceClient, BenihimeEntranceAudio e rotinas de palco; client/particle/BenihimeRenderer; common/visual/BenihimeStage e geometria; localizar serviços de técnicas atuais.
- Jogador/combate: PlayerAnimator, assets/bleachmod/player_animation, assets/bleachmod/attack_animations e perfis de Better Combat. Runtime e fontes podem ter schemas diferentes.
- Modelos/assets: src/main/resources/assets/bleachmod/geo, animations, textures, models e sounds; src/photonPilot para recursos opcionais.
- Fontes/ferramentas: tools/models, tools/animations, tools/audio, tools/qa, tools/gui; não misturar cache e evidência com fonte necessária ao build.
- Testes: MvpRegressionTest e gametest; laboratórios atuais opt-in de Benihime/Ryūjin. Não abrir comandos de debug para jogadores de release.

## 9. Validação exigida e critérios de aceite

Antes de executar tarefas, ler build.gradle e confirmar diretórios descartáveis. Usar JDK 17 e wrapper da raiz. Os laboratórios possuem flags e diretórios por perfil; não reutilizar o mundo pessoal como cenário destrutivo. Não executar Gradle/cliente concorrentes no mesmo diretório.

```powershell
.\gradlew.bat check build --offline --console=plain
.\gradlew.bat runGameTestServer --offline --console=plain
# Quando a mudança afeta o perfil opcional:
.\gradlew.bat check build runGameTestServer -PphotonPilot=true --offline --console=plain
# Cliente gráfico: confirmar antes as flags/diretório QA adequados em build.gradle.
.\gradlew.bat runClient --console=plain
```

Offline depende de cache preparado. Não confundir test com regressionTest. Não alterar teste, snapshot ou tolerância só para ocultar falha. Executar a suíte apropriada à alteração; builds não comprovam aparência, áudio, performance ou multiplayer.

Validar estrutura de JSON/PNG/rig/UV/keyframes e referências de recursos; então demonstrar no Minecraft Forge real. Registrar PNGs e vídeo quando movimento for critério: fonte, cenário, perfil, commit e marcos. Preview no Blockbench é etapa de autoria, não homologação in-game. Se a sessão não tiver controle gráfico disponível, declarar a limitação, preparar cenário e fornecer passos concretos ao usuário; não simular aprovação.

Matriz mínima pertinente: dia/noite, terreno plano/irregular, paredes/espaço estreito, primeira/terceira pessoa, skins e armaduras aplicáveis, cancelamento/morte/dimensão/logout e restauração de controle/câmera/som. Separar base de Photon; separar singleplayer de servidor dedicado e observador em segundo cliente. Quantificar FPS/frametime somente quando medidos, com hardware/configuração.

Para poder: timing, alvo, custo, cooldown, dano/controle, duplicação e griefing/proteções. Para criatura: escala/hitbox separadas, navegação, alvo, colisão, dano, morte, limpeza e persistência. Para animação: apoio, dedos/mão/cabo, clipping, transições, leitura e sincronismo. Destruição do mundo só com contrato explícito e política existente.

## 10. Evidência conhecida e leitura seletiva

O ciclo 81 registra 137 regressões e 164 GameTests em cada perfil base/Photon, pacotes cinco/sete JARs, 18 cenários gráficos Benihime/1645 quadros e cinco Ryūjin/180 quadros. São resultados históricos do relatório, **não reexecutados nesta documentação**. Aceite artístico do usuário, escuta pertinente, Photon gráfico e dois clientes continuam distintos/pendentes conforme cada item. Não herdar marcação verde de um cenário diferente.

Leia nesta ordem, aprofundando somente os pontos necessários:

1. Este documento, depois [continuidade vigente](00-CONTINUIDADE-CODEX.md).
2. [Roadmap geral 72](72-revisao-geral-roadmap-gamedesign-2026-10-20.md) e [entrega 81](81-benihime-selada-bettercombat-entrada-dash-ryujin-2026-10-21.md). O 81 sucede valores/propostas antigas de Benihime e protocolo.
3. [Contrato de novos poderes](contrato-criacao-novos-poderes.md), [integração/Zaraki 63](63-integracao-main-zaraki-aparencia-historia-2026-10-19.md) e [.agents/skills/bleach-mod-dev/SKILL.md](../../.agents/skills/bleach-mod-dev/SKILL.md).
4. [Game design M06](../game-design/06-habilidades-e-combate.md), [atributos M05](../game-design/05-atributos-e-recursos.md), [mapa modular](../game-design/00-mapa-modular.md) e [roadmap visual](../arte/roadmap-visual.md), conforme o objeto da tarefa.

Nomes de módulos futuros não provam existência de contrato completo. Cabeçalhos históricos de setembro e os ciclos 30–36 não descrevem sozinhos o runtime atual. Mais antigos só precisam ser lidos para resolver uma dúvida específica; não importar todo o chat para recomeçar.

## 11. Entrega de cada ciclo e primeiro retorno esperado

Antes de produzir, informe: branch/base real, alterações preexistentes, feature escolhida, assets reaproveitados, ferramenta necessária e primeira prova vertical. Faça escolhas rotineiras dentro do pedido; esclareça somente lacunas que afetem escopo/contrato ou autorizações específicas.

Ao concluir, registre em Docs/poderes: problema/fantasia, mudanças e arquivos, fontes editáveis/exports, integração, versões/perfis, comandos/exit codes, evidências reais, testes aprovados/falhos/não executados e pendências. Atualize continuidade/índice sem apagar registros históricos. Não marcar trabalho de outra branch como integrado sem verificar.

Primeiro objetivo recomendado da nova conversa: revisar a entrega mais recente com o usuário e selecionar uma única fatia de autoria na ordem vigente — por exemplo refinamento do dragão de gelo quando essa etapa for escolhida, ou criatura inédita contratada com a equipe. A sugestão não autoriza começar Ishida, outro Hollow ou uma nova Bankai sem a escolha correspondente.

### Prompt para iniciar a nova conversa

> Leia Docs/poderes/82-nova-frente-design-poderes-criaturas-blockbench-2026-10-21.md, confira o Git e as instruções locais. Prepare a nova frente em codex/design-poderes-criaturas a partir da base revisada de Feature-Poderes-bankais, preservando alterações existentes e a main. Revalide ferramentas e dependências; siga a skill bleach-mod-dev. Apresente o estado atual e a primeira fatia de criação/design antes de implementá-la. Use Blockbench/GeckoLib/PlayerAnimator/Better Combat conforme o tipo de asset, mantenha o perfil base independente de Photon e valide visualmente no Minecraft real. Não publique, instale ferramentas pagas ou substitua conteúdo homologado sem autorização pertinente.