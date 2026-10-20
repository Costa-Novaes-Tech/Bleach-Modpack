# Ciclo 71 — apoios Benihime, áudio fornecido e execução Higashi

Pedido de 20/10/2026: implementação e testes autorizados, inclusive controle da máquina. Esta rodada sucede a restrição a planejamento do [ciclo 70](70-roadmap-revisao-benihime-audio-2026-10-20.md). Branch `Feature-Poderes-bankais`, referência `e967748`; as alterações documentais do ciclo 70 já estavam presentes e foram preservadas. Nenhum commit, push ou merge solicitado ou realizado.

## Benihime — proporção e terreno

Braço e antebraço passam de 3,90/4,05 para 3,15/3,30 blocos: redução total de aproximadamente 19%. Ombros mais próximos do torso, tronco mais baixo e inclinado durante o apoio, mãos mais próximas da linha do corpo e juntas com encaixe e costura visíveis reforçam a aparência de boneca articulada. O levantamento e a posição final conservam cotovelos baixos e mãos elevadas para os fios.

Cada apoio consulta as formas de colisão do terreno no cliente. As alturas são independentes para as mãos; cintura/torso acompanham a distribuição dos apoios. A IK conserva o comprimento dos dois segmentos, limitando o alcance em vez de esticar os braços. A palma e os dedos usam a altura local, e a barra do vestido recebe adaptação aproximada por amostras do solo.

O alcance vertical de consulta é limitado e não carrega chunks. Falta de superfície próxima usa a referência da cena; desníveis além de dois blocos não constituem escalada física ou solução universal de terreno. A roupa continua procedural e não possui simulação física de tecido. Em casos fora do alcance anatômico, o alvo do punho é limitado; contato perfeito não é prometido nesses casos.

A fonte Blockbench foi atualizada e conferida pelo gerador. Continua sendo fonte de autoria, sem migração integral do runtime para GeckoLib. Postura do jogador em pé, empunhadura dupla, acompanhamento após a cena e contrato Benihime de 560 ticks visuais/release60/commit40/custo único permanecem.

## Áudios fornecidos

Os oito arquivos recebidos foram decodificados e identificados por hash; os WAVs foram escolhidos como fontes. A análise por correlação identifica as falas dentro de `bankai_final_ordered_30s.wav`: “Bankai” em aproximadamente 2,60 s e nome completo em 24,265 s. As versões comparison ficam como referências, sem substituir a faixa ordered.

Para encaixar a invocação nos 28 segundos existentes, a faixa ordered foi recortada de 1,20 a 29,20 s, sem acelerar ou alterar o tom das vozes. Isso posiciona “Bankai” em 1,40 s e o nome completo em aproximadamente 23,065 s. O evento da fala limpa final passou ao tick 461; a legenda permanece até o tick 545 para acomodar sua duração de aproximadamente 4,124 s. Aplicados redução de volume e fades nas extremidades do mix; originais em Downloads preservados.

- Dono com música e voz habilitadas: toca a faixa combinada; não reproduz as falas limpas por cima.
- Sem música: toca as falas limpas nos respectivos marcos.
- Sem voz: usa o instrumental original do projeto como alternativa, pois a faixa entregue já contém diálogo e não há stem instrumental separado.
- Observadores: falas limpas posicionais e efeitos existentes; trilha continua local ao dono.

**Limite do mix combinado:** seu volume acompanha a categoria MUSIC. Não é possível ajustar separadamente música e voz dentro de um único arquivo já misturado. A configuração de voz escolhe o modo de reprodução no início da cena. Uma faixa instrumental separada permitirá mixagem independente futuramente.

Novos OGGs decodificam sem amostras saturadas; mix com pico aproximado de −2,90 dBFS. Isso valida o recurso, não a preferência artística de escuta. Atribuição/origem estão registradas [separadamente](licencas/71-audios-fornecidos-benihime.md) e empacotadas; os novos arquivos não receberam a licença das vozes sintetizadas antigas.

## Higashi — dano letal e direção de cena

Mantida a execução de um alvo elegível com até 7% de vida no impacto. Acrescentada a mesma apresentação para a primeira morte confirmada causada pelo dano normal da Higashi, mesmo acima do limiar. O caminho de dano normal conserva defesa, absorção, Totem e eventos Forge; não há execução antecipada baseada em estimativa de dano. Aliados e invulneráveis continuam sujeitos à política existente. Custo e área inicial de quatro blocos preservados. Um avanço curto durante o preparo aproxima o jogador até cerca de um bloco do alvo: passos de até 0,7 bloco, limite total de 2,8, com verificação de chunk/colisão e sincronização para o cliente. Nos alvos de QA a três blocos, o avanço é de aproximadamente dois blocos. Se o caminho estiver bloqueado, não atravessa sólidos; a composição não foi homologada em todos os obstáculos.

A prévia pode enquadrar um alvo ainda saudável, mas não confirma morte. Se o alvo sobreviver ou o golpe for cancelado, não há incineração. Cura antes do impacto continua podendo impedir a execução de limiar.

Ordem da cena, em ticks de jogo:

1. 0–10: preparar a espada junto ao quadril e girar o torso.
2. 10–16: corte diagonal ascendente; impacto de gameplay no tick 12. Pose e trilha de partículas usam a direção nova.
3. 16–56: manter o término do corte durante 40 ticks, equivalentes a dois segundos a 20 TPS.
4. 56–88: ignição, incineração e cinzas; câmera retorna depois desse trecho.

A câmera permanece lateral e enquadra atacante e alvo. Uma referência de renderização do alvo morto mantém sua silhueta durante a pausa, sem adiar a morte do servidor, ressuscitar entidades ou gerar outros drops. O modelo desaparece nos primeiros instantes da ignição, dando lugar às chamas/cinzas. A flag de fogo do cadáver é ocultada também antes do passe externo do renderizador; um mixin exclusivamente de cliente suprime apenas o evento vanilla de poof de morte enquanto esse alvo confirmado está em espera. As duas correções vieram de falhas vistas no cliente real, não apenas de testes de geometria. Interrupções por movimento, dano, menu ou troca de perspectiva continuam liberando a câmera.

## Ferramentas e validação

FFmpeg 7.1 preparado apenas em `build/benihime-qa-71/python-tools`, pelo pacote oficial PyPI `imageio-ffmpeg==0.6.0`, reutilizando download em cache. Sem instalação global, mod obrigatório novo ou serviço externo de áudio. Minecraft/Forge Java17, PlayerAnimator, Better Combat e perfil Photon existentes mantidos. Rede atualizada para **2.28**, schema de personagem **9** preservado; cliente e servidor devem usar o mesmo pacote.

Linha de base antes das alterações: `check runGameTestServer --offline --console=plain` passou, 126 regressões e 148 GameTests, 2m17s. Uma tentativa sem elevação falhou ao abrir o lock do cache Gradle; a execução autorizada com acesso ao cache concluiu.

Validação base final, isolada após encerrar o cliente: `check build runGameTestServer prepareCombatTestPack --offline --console=plain` passou, **127 regressões e 151 GameTests**, 1m41s; log `validation-cycle71-release-base.log`. Novos casos cobrem letalidade normal acima do limiar, pausa após a passagem da lâmina e comprimento constante em apoios com alturas distintas. O teste antigo da pose final foi expresso proporcionalmente aos novos segmentos; checagens de comprimento e finitude foram mantidas. Rodadas de diagnóstico anteriores falharam no tempo antigo de recuperação da Higashi e na expectativa antiga do antebraço, corrigidos antes da rodada final. Uma rodada intermediária também falhou por classes ausentes (`NoClassDefFoundError`) enquanto outra compilação de cliente alterava o mesmo diretório de classes. Esse resultado não foi usado como aprovação: cliente encerrado e validação final repetida sequencialmente, passando também nos testes de integração Nozarashi/Zaraki. Um novo teste de regressão exige que o término ascendente aponte para a frente, impedindo o erro observado de espada voltada para trás.

**Photon final:** `-PphotonPilot=true check build runGameTestServer prepareCombatTestPack preparePhotonTestPack --offline --console=plain` passou, **127 regressões/151 GameTests**, 1m32s, executado depois do base. [Log base](qa/benihime/cycle71/validation/validation-cycle71-release-base.log), [log Photon](qa/benihime/cycle71/validation/validation-cycle71-release-photon.log) e [manifesto da entrega](qa/benihime/cycle71/validation/delivery-manifest.json). Scanner/editor Photon mantém os avisos recuperados conhecidos; não equivale a teste gráfico desse perfil.

Pacotes: `build/combat-test-pack/mods` com cinco JARs e `build/photon-test-pack/mods` com sete. JAR próprio: `build/libs/bleachmod-0.2.0.jar`. Recursos OGG, JSONs, proveniência e integridade ZIP conferidos nos pacotes. Novas dependências obrigatórias de Minecraft: nenhuma.

Reproduzir QA completo em save novo: `./gradlew.bat -PbenihimeQa=true -PcinematicQa=true runClient --offline --console=plain`. Para repetir apenas as duas Higashi, adicionar `-PcinematicQaFast=true`. Os comandos `/cinematicqa` são exclusivamente de desenvolvimento, com acesso local/admin e nome de mundo de QA obrigatório.

Evidências gráficas e limitações finais estão no [relatório visual do ciclo 71](qa/benihime/cycle71/relatorio-visual-final.md). Há capturas da entrada de dia/noite, apoio em alturas +1/+2 e dos dois gatilhos Higashi, com contato da lâmina, pausa sem fogo/poof antecipados, cinzas e retorno à primeira pessoa. Os vídeos são montagens de quadros amostrados e silenciosos, não gravações contínuas com áudio do jogo. A ferramenta de controle de janela travou; a sequência foi repetida pelo laboratório interno em mundo QA novo. A automação adicional só funciona com flags explícitas de desenvolvimento, singleplayer/admin e prefixo de save QA, sem comandos liberados em produção. Resultados de GameTest não equivalem à homologação de fluidez, escuta ou multiplayer com dois clientes.

## Continuidade

Revisar no jogo a nova proporção/apoios, o encaixe do áudio escolhido e a cena Higashi. Preservar Zaraki. Próximos trabalhos permanecem Investida/Rajada Ryūjin e demais técnicas Benihime, depois Ishida; Mentor/Proficiências aguardam equipe, Hyōrinmaru sem revisão ampla nesta rodada. Aizen adiado e Gin fora do planejamento.
