# Ciclo81 — vídeo, entrada Benihime, esgrima Better Combat, selada e dash Ryūjin

21/10/2026. Feature-Poderes-bankais, referência b398fdc. Skill bleach-mod-dev. Usuário reabriu qualidade do combate, Hiasobi, interrupção da entrada ao andar e postura do dash, autorizando kit selado e suprema mais imponente/destrutiva. Alterações anteriores76–80 preservadas. Sem checkout, commit, push ou merge; Zaraki e seus arquivos não foram alterados.

## Vídeo e causas

Fonte: Gravação de Tela2026-10-21 083725.mp4,401,53s,1920×1020,30FPS,422390862bytes. [Identificação/hash](qa/cycle81/video/source.json), [visão geral](qa/cycle81/video/overview.jpg), [entrada](qa/cycle81/video/entrance-contact.jpg), [combate](qa/cycle81/video/melee-contact.jpg), [dash](qa/cycle81/video/dash-contact.jpg). Inspeção por quadros, sem avaliação auditiva subjetiva.
- Primeira parte mostra golpes da Benihime/forma selada com gestos breves e efeitos vermelhos, sem o conjunto de ataques físicos que o usuário espera. A arma ainda estava excluída do Better Combat por BetterCombatCompat; as curvas cosméticas77/80 não equivalem a um repertório BC completo.
- Aproximadamente145–173s: montagem/revelação, pose final e saída da câmera. Quadros não registram as teclas pressionadas nem provam sozinhos o instante de cancelamento por movimento. Código e reprodução própria confirmam o defeito: servidor e cliente descartavam a cena ao afastar1,5bloco do ponto inicial.
- Trecho posterior de Ryūjin mostra investidas com aparência de corrida. O ciclo76 havia removido o giro Riptide para favorecer a pose de katana; usuário agora pede recuperar essa identidade de giro.

## Entrada: caminhar não pula a revelação
Andar sai da câmera e libera a pose cinematográfica do jogador, mas a cortina/Benihime continuam a sequência de560ticks no local da invocação. Ao terminar, a manifestação passa ao acompanhamento. Não bloquear movimento por28s. Limite defensivo passa a512blocos para deslocamento/teleporte extremo; morte, dimensão, equipamento e demais invalidações continuam limpando a cena. Ataque/guarda/esquiva e dano ainda podem interromper conforme o contrato anterior: esta correção é especificamente de caminhada.

Commit/custo de transformação continuam no tick40, término da liberação curta60, apresentação560; câmera e áudio usam os mesmos marcos anteriores. Não dar custo ou invulnerabilidade novos por causa da correção.

GameTest desloca o jogador5blocos após o commit e exige manutenção do relógio/âncora/custo. Cliente real caminhou **4,217blocos**: cena ativa no tick240 da bancada e encerrada normalmente no590 (iniciou no20, portanto acabou no580). Sem salto antecipado à boneca pronta. Após a conclusão, câmera/HUD restaurados e acompanhamento observado em frames.

## Benihime: três ataques próprios no Better Combat
Perfil próprio de alcance3,0, multiplicadores1,0 e três animações originais: diagonal, corte de retorno e estocada. Categoria bleach_benihime_fencing; hitboxes vertical75°, horizontal95° e forward box. Cadência segue o cooldown da arma/BC; upswing0,45. Esgrima com punho/torso e apoio contido diferencia a bengala da Ryūjin pesada. Sem dual wield. O servidor conserva alcance3,05, visibilidade, cone, deduplicação e cobrança de stamina uma vez por pedido de ataque.

Arquivos runtime em assets/bleachmod/attack_animations; fontes Bedrock correspondentes em player_animation. Registries têm formatos distintos: runtime BC lê emote, PlayerAnimator normaliza torso→body. Os arquivos foram escritos para este projeto, sem copiar animações comerciais. As curvas antigas de melee não são mais aplicadas sobre a Benihime; gestos das técnicas continuam separados. Senbon e Zaraki não foram habilitados no piloto.

Parser de PlayerAnimator e registry do cliente confirmam os três IDs bleachmod:benihime_diagonal/return/thrust. Bancada aciona a tecla real de ataque do cliente, passando pelo pedido BC, em vez de chamar somente Player.attack/swing. Dano apareceu em selada/Shikai/Bankai e na primeira pessoa; revisão artística do usuário continua necessária.

## Selada — quatro técnicas implementadas
Adaptações do repertório Urahara proposto79; Kidō/equipamentos não são propriedades canônicas da espada selada.
- H **Saque da Bengala:** corte curto, alcance4, contato no6, dano base8, gesto16ticks. Custo8/CD60.
- N **Jūgeki Byakurai:** disparo fino até22blocos, contato no8, dano base10 no primeiro alvo válido do trajeto; termina18ticks. Custo12/CD100. Parede interrompe o raio.
- B **Rikujōkōrō/Seis Hastes:** um alvo apontado até18; seis hastes visuais e lentidão forte por3s. Para jogadores/entidades sem troca de dimensão, intensidade menor e24ticks. Não causa dano e não prende todos os mobs da área. Custo18/CD180.
- C **Gigai Portátil:** recuo seguro de até6blocos e isca por3s no ponto anterior. Custo22/CD240. Usa direção horizontal contrária ao yaw, inclusive olhando para cima/baixo. Revalida passagem e suporte antes da travessia; não concede invulnerabilidade. Mobs elegíveis que já estavam mirando o dono podem passar a mirar a isca; não força mira de PvP.
- Isca inicial é manequim ArmorStand com cabeça do usuário e roupa verde, **protótipo visual**, sem novo modelo humano exportado. Invulnerável/temporária, sem ser fonte de equipamentos. Removida ao expirar/invalidação, e descartada se reaparecer de um save após reinício. Maestria do Gigai exige distração real de inimigo elegível; recuar no vazio não gera treino.
- Selada não recebe regeneração/aura Bankai. Desbloqueios seguem as exigências normais de maestria0/20/40/60; não conceder as quatro técnicas automaticamente a personagens novos.

## Hiasobi e Costura Final
**Hiasobi independente:** alcance18 (antes12), faixa com tolerância lateral2,7 ao segmento, nove fios ondulados, três contatos6 nos ticks8/18/28: total base18. Gesto1,4s, custo25/CD200 preservados. Não é armadilha. Corrigida seleção quando a origem está dentro da caixa expandida de um alvo próximo; alvos atrás e cobertos continuam excluídos. Com rede ativa, mantém detonação radial base14 e gesto16ticks; captura/contração Shibari preservadas.

**Costura Final:** raio12 (antes8), anéis adicionais,24costuras radiais, mão/torso/dedos e conexão até o campo. Duração3s, aviso12ticks, quatro contatos7 e final12: **total base40**, a técnica de maior dano do kit por execução completa. Custo50/CD560 preservados. Defesas, progresso e permanência no campo alteram o resultado efetivo.

Na ruptura final, pode destruir terreno permanentemente. Reutiliza a política pública já existente em BerserkTerrain, sem editar Zaraki: cratera de raio6, profundidade base1, centro poupado1,8, orçamento global420blocos/12destroços/20quebras ruidosas do helper. Cobertura natural não conta como profundidade: remoção real pode atravessar folhas/plantas e mais camadas reconhecidas como cobertura. Nunca prometer uma camada exata. Muda o mundo: **não é piso visual nem reconstrução automática**.

Whitelist de materiais naturais, sem minérios/baús/blocos com entidade, fluidos ou blocos indestrutíveis; tijolos/tábuas ficam fora. mayInteract e Forge BreakEvent por bloco respeitam proteções/claims que os implementem. Pedra/terra colocadas por jogador são indistinguíveis de material natural: construções desses materiais precisam de proteção de área. Sem drops; destroços físicos limitados. Teste exige terreno intacto antes do final e preservação de baú/stone_bricks depois. Não é destruição indiscriminada da esfera inteira de raio12.

## Dash Ryūjin
Visual de corpo horizontal/giratório durante a investida de16ticks, braços à frente/pernas sem marcha e rastro de chamas preservado. Rotação do modelo, com câmera estável; não ativar startAutoSpinAttack, que acrescentaria comportamento de colisão/dano vanilla. Executor continua dono do movimento, parede, custo20/CD100, contato único por alvo e cerca de20blocos livres.

A orientação final usa quaternion alinhando o eixo vertical do corpo ao vetor de viagem, incluindo subida/descida sem eixo degenerado. Teste numérico exige comprimento unitário e alinhamento para frente/diagonal/cima/baixo. A câmera não gira junto com o corpo.

Cliente real: percurso com curva18,355blocos, dois alvos atingidos; parede limita a5,20blocos e protege os alvos posteriores. Nova rodada após o quaternion final acrescentou subida: Y-60→-41,25 (**18,75blocos**), com giro válido; no tick75 já havia voltado ao chão, portanto displacement0 desse resultado tardio não significa que não subiu. Capturas mostram postura horizontal e giro; primeira pessoa/observador remoto do novo giro ainda exigem validação específica.

## Ferramentas, rede e verificações
Minecraft1.20.1/Forge47.4.10/Java17/Gradle8.8. Better Combat1.9.0/PlayerAnimator1.0.2rc1/Gecko4.8.4; Photon1.1.17/LDLib1.0.42 opcional. Protocolo2.31 para novos frames de Kidō/hastes/isca; schema9/techniqueRevision3 mantidos, novos IDs selados aditivos. Cliente/servidor precisam da mesma versão.

Skill e matriz de QA consultadas. Sem nova biblioteca do mod ou instalação global. O iniciador do usuário limpou ferramentas temporárias em build; FFmpeg restaurado do wheel cached imageio-ffmpeg0.6.0 em build/video-review-tools,31,2MB, sem upload do vídeo ou uso de serviço pago.

Baseline135regressões/158GameTests, exit0. Rodadas intermediárias documentadas nos logs: contagem56→60 intencional pelos quatro slots; normalização torso/body no parser; falha real AABB da Hiasobi corrigida; fixtures seladas precisavam de maestria100 para exercitar N/B/C. Nenhum requisito de autorização foi relaxado para fazer testes passar. Base intermediária136/163, exit0. **Base final:** check/build/runGameTestServer/prepareCombatTestPack, exit0, **137regressões/164GameTests**,1m27s. O caso adicional exige Byakurai à queima-roupa no alvo mais próximo, sem atravessá-lo para acertar o distante. **Photon final separado:** -PphotonPilot=true check/build/runGameTestServer/prepareCombatTestPack/preparePhotonTestPack, exit0, **137regressões/164GameTests**,1m03s. Diagnósticos recuperados conhecidos de scanner/editor LDLib não impediram a suíte; gráfico/editor Photon não foi homologado. [Manifesto](qa/cycle81/delivery-manifest.json):5/7JARs, CRCs válidos e mod idêntico em build/libs e ambos os pacotes. Logs de [base](qa/cycle81/validation/benihime81-final-verified-base.log), [Photon](qa/cycle81/validation/benihime81-final-photon.log), [cliente Benihime](qa/cycle81/validation/benihime81-client.log) e [cliente Ryūjin final](qa/cycle81/validation/benihime81-ryujin-final-client.log). Os clientes QA foram encerrados normalmente após as baterias; git diff --check aprovado, logs gerados na raiz restaurados, nenhuma alteração ou commit em main.

Cliente base: **18cenários Benihime/1645quadros reais +5cenários Ryūjin/180quadros na rodada final** em1280×720, mundo plano novo seed681019, renderDistance6, FOV70°, gráfico1, máximo120FPS. [Manifesto](qa/cycle81/capture-manifest.json), [contato de combate](qa/cycle81/combat-check.jpg), [dash](qa/cycle81/dash-check.jpg). [Manifesto Ryūjin](qa/cycle81/ryujin-capture-manifest.json), [subida](qa/cycle81/dash-up-check.jpg). Os quadros e clipes são amostragem do framebuffer, não gravação contínua. Invocação amostrada a cada4ticks, demais a cada tick; MP4s codificados20FPS, sem áudio. Contador FPS registrado nos metadados, sem benchmark/frametime controlado. Rodada final Ryūjin:20–117FPS amostrados (mínimo20 no primeiro cenário/carga inicial); dash aberto89–113 e subida97–115. Sem inferir desempenho multiplayer ou ausência de stutter desses valores. Não houve nova coleta completa de hardware.

Homologação do usuário, dois clientes reais, editor/Photon gráfico, nova escuta e teste integral irregular/espremido continuam pendentes. A prova de caminhada/BC/kit selado não aprova automaticamente todas as animações. Parecer do agente: causa de cancelamento resolvida no laboratório; leitura de área e giro melhores, mas esgrima, tamanho da Hiasobi e qualidade do manequim pedem seu teste.

## Evidências de apresentação
Clipes amostrados20FPS, sem áudio, com originais e proveniência nos manifestos. Não são gravações contínuas; a invocação foi capturada em5amostras/s.
- [Esgrima Better Combat](qa/cycle81/sealed-melee-sampled.mp4) e [primeira pessoa](qa/cycle81/shikai-first-person-sampled.mp4).
- [Hiasobi independente](qa/cycle81/hiasobi-standalone-sampled.mp4) e [Costura Final/destruição](qa/cycle81/final-stitch-sampled.mp4).
- [Saque](qa/cycle81/sealed-draw-sampled.mp4), [Byakurai](qa/cycle81/sealed-byakurai-sampled.mp4), [seis hastes](qa/cycle81/sealed-rods-sampled.mp4) e [Gigai](qa/cycle81/sealed-gigai-sampled.mp4).
- [Entrada/caminhada](qa/cycle81/entrance-walk-sampled.mp4), [dash final](qa/cycle81/dash_open-sampled.mp4) e [subida](qa/cycle81/dash_up-sampled.mp4).

Parecer técnico/visual0–5 do agente: participação da esgrima3, continuidade da entrada após andar4, leitura da Hiasobi4, impacto da suprema4, orientação/giro do dash4. Não constituem homologação artística. A bancada usa enquadramentos de inspeção, não uma nova câmera obrigatória para ataques comuns. Limites: manequim Gigai ainda é protótipo; retorno da mão/katana e giro em primeira pessoa/mão esquerda precisam de revisão do usuário. Nenhuma nova avaliação auditiva da mixagem.
Logs intermediários e finais em [validation](qa/cycle81/validation). Onze MP4s decodificados sem erro; seleções PNG originais em [frames](qa/cycle81/frames). Sem novas ferramentas globais ou dependências de produção.

## Teste manual e continuidade
1. Invocar Bankai e andar durante a cortina: câmera pode sair; revelação deve continuar no local até terminar e então seguir.
2. Atacar normalmente em selada/Shikai/Bankai e primeira pessoa: procurar diagonal/retorno/estocada e transições de guardaX.
3. Na selada, H/N/B/C com maestria apropriada; Gigai requer saída apoiada e desobstruída.
4. Hiasobi sem rede, perto/longe/lateral e com parede; depois combo com Shibari.
5. Costura Final em terreno de teste: maior raio/dano e ruptura destrutiva final; preservar áreas valiosas com proteção.
6. Dash Ryūjin em espaço livre/parede e ângulos diferentes; sem corrida ou câmera rolando.

Item1 reaberto por caminhada e corrigido para revalidação; item3 revisado pelo novo pedido de giro; item4 agora inclui esgrima BC e selada implementada. Os demais itens da lista obrigatória72 continuam: Uryū Ishida, Mentor/Proficiências com equipe, Hyōrinmaru/Blockbench; depois NPCs de combate e quests. Nake/escudo Shikai não receberam automaticamente carga/parry só por estarem sugeridos79. Aizen adiado, Gin removido, Zaraki preservado.
