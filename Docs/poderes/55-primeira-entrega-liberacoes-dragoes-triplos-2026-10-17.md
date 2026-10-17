# Ciclo 55 — Primeira entrega: liberações animadas e dragões triplos

Data: 17/10/2026. Branch `Feature-Poderes-bankais`, base limpa `7f9c743`. Usuário autorizou seguir com a proposta 54, rejeitou Nishi defensiva, pediu identidade distinta para Rajada/Leque, selo de chamas na espada ao liberar Bankai Ryūjin e três dragões simultâneos em direções distintas na Bankai Hyōrinmaru. A imagem fornecida orienta a espada abaixada/horizontal e recolhimento do fogo. Não contém instruções técnicas.

**Esta é a primeira fatia funcional prevista ao final da proposta 54: animação comum de liberação e novo dragão. Não é a entrega de todo o repertório substituto.** Rajada/Leque, Higashi, prisão de fogo, Minami, corrente crescente, Ryūsenka, Sennen Hyōrō e continuação floral ainda entram nas próximas etapas de refatoração. O kit antigo continua nas demais teclas até essa migração explícita. Não declarar que Nishi foi implementada ou removida do runtime: ela só existia na proposta.

## 1. Ajustes fechados na direção do kit

- **Nishi sai da proposta. Substituta recomendada: Higashi — Corte de Aniquilação**, um golpe ofensivo de contato com calor concentrado no fio. Ataque curto, preparação marcada e impacto incisivo, distinto do Corte de Vapor de grande alcance e da Investida de deslocamento. Não concede apagamento literal de entidades, defesa ignorada ou dano infinito. [Referência pesquisada](https://bleach.fandom.com/wiki/Zanka_no_Tachi%2C_Higashi%3A_Kyokujitsujin).
- Ennetsu Jigoku foi considerada como alternativa ofensiva durante a discussão, mas a recomendação final é Higashi para a Bankai. Ennetsu permanece alternativa de Shikai; não confundir sua forma de referência na obra. Ainda é proposta, sem executor novo neste ciclo.
- **Rajada:** frente estreita que avança pelo solo e alcança inimigos em sequência; direção de corte descendente. **Leque:** arco lateral que acompanha a espada da direita anatômica para a esquerda, com cobertura angular e alcance radial. A diferença será trajetória/área/timing, além de distância. Ainda não trocar os executores neste ciclo nem afirmar que o dano instantâneo antigo já foi migrado para essa timeline.
- **Dragão Bankai:** três cabeças/modelos independentes no mesmo disparo, um central e dois diagonais a ±25°. Dano/maestria uma vez por alvo no conjunto, não três acertos sobrepostos. Implementado abaixo.
- Passivas e roadmap geral continuam preservados; Zaraki ainda aguarda commit identificado da equipe. Nenhum fetch/merge feito.

## 2. Liberações implementadas

`ReleaseAnimationService` controla uma timeline transitória por jogador para Shikai/Bankai de Ryūjin e Hyōrinmaru. Depois da solicitação de transformação válida, começa a pose: **24 ticks na Shikai (commit em 16)** ou **60 ticks na Bankai (commit em 40)**. O ponto de commit valida novamente acesso/saldo e muda a forma uma única vez, com a cobrança existente. Não adiciona um segundo custo. Inclui a entrada de transformação instantânea por maestria: ela evita o carregamento de input anterior, mas ainda apresenta a liberação. A animação é uma fase nova após o carregamento normal, com tempos iniciais sujeitos a playtest.

Antes do commit: trocar arma/identidade/forma/dimensão, morrer ou perder a elegibilidade básica cancela sem transformar/cobrar. Depois do commit: cancelamento não desfaz a forma nem cobra de novo. A recuperação termina a pose. As técnicas anteriores do dono são encerradas ao iniciar; ataques físicos, técnicas novas, defesa e esquiva não iniciam durante a timeline. Não se adiciona invulnerabilidade nem interrupção automática por todo dano comum. Caminhar e girar câmera continuam permitidos; sprint é desligado. Não foi introduzida câmera forçada/FOV/tremor.

O custo só é reservado logicamente pela intenção; é consumido no commit. Se o saldo deixar de permitir a liberação nesse instante, ela cancela. Dreno da forma antiga continua sujeito às regras existentes durante preparação. Entrada repetida não cria outro runtime. Descida/retorno forçado a uma forma diferente invalida a timeline; desconexão/servidor encerrado limpam o estado. Runtime não vai ao save; schema continua 8.

`releaseView` acompanha os snapshots de aparência, inclusive `appearanceTo` para quem começa a observar depois. Carrega identidade, forma-alvo, duração e tempo decorrido. O cliente ancora o tempo na recepção e termina sozinho no prazo; snapshot vazio cancela. Classes cliente ficam em `client/model/ZanpakutoReleaseAnimation`. PlayerAnimator registra camada 4000 acima da guarda 3000 e Better Combat 2000, sem um segundo ataque físico.

### Direção visual desta versão

- **Ryūjin Shikai:** braço com espada elevado, mão livre de apoio, fogo acompanha a lâmina e a liberação vigente ocorre no commit.
- **Ryūjin Bankai:** espada abaixada/inclinada, braço livre passa pelo trajeto da lâmina; fluxos circulares de chama convergem para a espada, raio diminui e dá lugar à fumaça. No commit, assume a lâmina Bankai e efeitos já existentes. O selo representa as chamas da própria apresentação, não apaga fogo mundial/outros jogadores. A aura periódica antiga pausa durante a timeline para não competir com a convergência.
- **Hyōrinmaru Shikai:** gesto de espada com condensação/neve e efeito de liberação no commit.
- **Hyōrinmaru Bankai:** gesto, condensação, forma de gelo no commit e abertura gradual das asas durante recuperação. Placas/cauda existentes são preservadas; não foram totalmente remodeladas neste piloto. A anatomia completa cabeça-de-dragão no braço/flores/geometria de armadura continua na etapa de revisão visual do kit.

Primeira pessoa tem movimento de espada próprio e mantém modo de mão vanilla durante a liberação; terceira pessoa mostra gesto corporal/mão livre quando vazia. Saque cosmético cede à liberação. Partículas têm audiência de 64 blocos, emissão por tick e redução em configuração mínima. Não se afirma que isto já reproduz uma cinemática final do anime ou a câmera do vídeo Zaraki.

## 3. Novo dragão: GeckoLib integrado

Dependência exata **GeckoLib Forge 1.20.1 4.8.4**, fixada no Maven Modrinth pelo ID de versão **`aC5KMoNg`**. O número `4.8.4` sozinho resolve artefatos homônimos de outras plataformas/versões Minecraft; por isso o ID é obrigatório nesta integração. `mods.toml` exige a linha 4.8.4–4.8.x. O próprio JAR contém `mclib-20.jar` via JarJar; não adicionar outro mod mclib solto.

Fontes: [distribuição oficial da versão exata](https://modrinth.com/mod/geckolib/version/aC5KMoNg), [entidades GeckoLib 4](https://github.com/bernie-g/geckolib/wiki/Geckolib-Entities-%28Geckolib4%29). API conferida no JAR resolvido. PlayerAnimator/Better Combat/Cloth Config mantêm as versões anteriores. Photon/LDLib ainda não foram instalados; continuam na bancada de VFX do roadmap.

Modelo autoral serpentino: cabeça, focinho, mandíbula, chifres, bigodes, dez segmentos e ponta de cauda; animação de voo ondula corpo e mandíbula com MoLang. Recursos editáveis `geo/ice_dragon.geo.json` e `animations/ice_dragon.animation.json`, importáveis no fluxo de edição de geometria/animação; material atual reutiliza `packed_ice` vanilla. Não foi criado PNG por cópia de arte do anime. O renderer Gecko substitui o renderer Phantom; a classe base/registry Phantom é conservada internamente para compatibilidade de entidade/atributos, sem usar sua silhueta na renderização.

### Combate

- Shikai: um dragão, alcance nominal de cabeça 10, velocidade 0,7/tick. Bankai: três, alcance 24, velocidade 0,8/tick. Rotas em leque 0°/−25°/+25°, sem homing. Modelos Bankai maiores.
- O dano anterior instantâneo em cone do slot H liberado foi substituído por dano na caixa varrida da cabeça a cada avanço. Dano base 8/12 e frio/lentidão originais mantidos. Corpo/cauda são visuais, sem uma segunda hitbox de dano.
- Conjunto compartilha IDs atingidos: até 64 alvos, no máximo um dano direto por alvo por disparo. Frio vanilla pode continuar sua consequência periódica, conforme sistema de gelo anterior.
- Parede sólida interrompe trajetória; chunks ausentes não são carregados à força. Elegibilidade de alvo e linha de visão verificadas. Disparo não fura parede, não dá dano nos aliados e não fornece dano por toque da entidade vanilla.
- Novo disparo substitui o grupo anterior do dono. Troca de forma/item, morte/dimensão, cancelamento e prazo removem as entidades. `shouldBeSaved=false`; sem persistência, drops ou alvo selecionável. Não dá voo ao jogador.
- Zonas, Choque Glacial, Barreira antiga e demais slots ainda conservam sua implementação anterior nesta fatia; a substituição completa será uma migração de repertório própria.

## 4. Validação e instalação

**check/build/pacote aprovados: 99 regressões e 123 GameTests obrigatórios**, log `build/cycle55-validation-final.log`. Cinco GameTests novos: commit/custo/locks/snapshot tardio, interrupção antes do commit, três ramos e deduplicação/cancelamento, parede sólida, e leitura real de geometria/MoLang pelos adaptadores GeckoLib. Testes antigos de dragão foram atualizados para esperar chegada do projétil; teste de transformação gratuita agora aguarda o commit. Primeiro ensaio encontrou a expectativa antiga de transformação imediata; corrigida para o novo contrato assíncrono, não desativada.

Rede **2.19**, schema **8**. Cliente e servidor precisam da mesma build e dependências. `prepareCombatTestPack` passa a gerar **cinco JARs**: Bleach, Better Combat, PlayerAnimator, Cloth Config e GeckoLib (mclib embutido). ZIP atualizado `build/combat-test-pack/bleach-bettercombat-piloto.zip`. Quem vinha do ciclo 53 deve atualizar Bleach **e adicionar GeckoLib Forge correto**; evitar duplicatas e o artefato Fabric/1.21 de mesmo número de versão.

Ainda pendentes: playtest de cliente das poses/modelo/material; primeira/terceira pessoa, slim/normal, canhoto, mão secundária, múltiplos lançadores, observador chegando durante a liberação e dois clientes reais. Testes de parser/servidor não homologam renderização nem FPS. Não foi iniciada nova sessão Minecraft por ferramenta de UI nesta entrega.

Próximo passo do plano aprovado: conferir esta fatia e levar a mesma timeline aos ataques, concluir novo kit Hyōrinmaru, depois Ryūjin ofensiva com Rajada/Leque distintos. Fechar migração de técnicas/slots sem zerar maestria de forma. Higashi substitui Nishi na direção proposta. Demais personagens/prioridades seguem no roadmap 51.
