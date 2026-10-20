# Ciclo 70 — roadmap e revisão da entrada Benihime

> **Atualização posterior — [ciclo 71](71-benihime-apoios-audio-higashi-execucao-2026-10-20.md):** áudios recebidos e implementação/testes autorizados pelo usuário. Entrada Benihime e Higashi receberam os ajustes descritos no novo relatório; a restrição desta rodada anterior a planejamento e a pendência de receber arquivos são históricas. Homologação final continua dependente de revisão no jogo.

Revisão documental de 20/10/2026. Pedido atual: planejar e revisar, sem código, nova animação ou substituição de áudio. O usuário buscará os áudios desejados para a habilidade. Esta orientação sucede a seleção provisória de voz do ciclo 69; não significa aprovação artística dos arquivos atuais.

## Estado do repositório e alcance desta rodada

Git limpo na abertura, checkout `main`, HEAD `50a2c44` (merge do PR #19). A branch de trabalho `Feature-Poderes-bankais`, em `e967748`, tinha conteúdo idêntico à main e foi retomada para esta revisão. Nenhum commit, push ou merge realizado nesta rodada; alterações restritas a `Docs/poderes`.

Este documento passa a orientar as próximas entregas. Os [ciclos 51](51-roadmap-better-combat-ferramentas-refatoracao-2026-10-16.md) e [61](61-revisao-roadmap-pendencias-integracao-zaraki-2026-10-18.md) permanecem como histórico; suas etapas antigas de aguardar Zaraki, instalar todas as bibliotecas ou iniciar a primeira refatoração elemental não representam o estado atual.

## Áudio — seleção do usuário pendente

- **Vozes e efeitos sonoros:** o usuário vai revisar e buscar os arquivos que deseja utilizar. As vozes japonesas sintetizadas e os efeitos atuais continuam provisórios; não regenerar, substituir ou remover recursos nesta rodada.
- **Música:** a trilha atual também permanece disponível para avaliação. O pedido de revisar vozes/efeitos não equivale a rejeitar a música. Reavaliar sua mixagem quando os novos sons forem escolhidos.
- **Próxima preparação:** identificar a fala inicial, o nome completo da Bankai e os efeitos escolhidos; registrar fonte, condições de uso, duração, recortes e níveis de volume antes de empacotar os arquivos.
- **Integração posterior:** ajustar os marcos às durações reais, equilibrar voz/música/efeitos, revisar legendas e atribuições, verificar distância de audição dos observadores e encerramento dos sons ao interromper a cena.
- **Aceite:** escuta do usuário e teste sincronizado dentro do Minecraft, incluindo outro jogador. Logs de cues e prévia de áudio fora do jogo não substituem esse aceite.

Os tempos atuais — “Bankai” no tick 28 e nome completo no 460 — são referências técnicas do [ciclo 69](69-benihime-em-pe-empunhadura-audio-japones-2026-10-19.md), não cortes obrigatórios para os áudios futuros.

## Revisão da animação — ordem de observação

A próxima conversa deve usar a [captura contínua real do ciclo 69](qa/benihime/cycle69/validation/minecraft-native-final.mp4) e os marcos das galerias como base. Os itens abaixo são critérios de revisão e possíveis ajustes; não são afirmações de defeitos novos já comprovados.

1. **Preparação e ocultação:** jogador de pé, duas mãos na espada voltada ao chão; cortina sobe sozinha e inteiramente fechada. Benihime permanece escondida, sem fresta ou aparição durante a subida do tecido.
2. **Pega central:** ombro, cotovelo e punho conduzem a aproximação; dedos e polegar fecham na borda antes de puxar. Conferir contato contínuo e ausência de atravessamento da mão no tecido. A outra mão deve participar da abertura e da sustentação.
3. **Saída e avanço:** rosto e tronco emergem progressivamente. Alternar apoio das mãos e deslocamento do corpo, com transferência de peso e avanço perceptível até a posição atrás do jogador. Evitar sensação de deslizar ou surgir no ponto final.
4. **Elevação:** apoiar, transferir peso e erguer o tronco em uma ação contínua; cotovelos flexionam e acompanham o corpo. Cabelo, mangas e vestido respondem com pequeno atraso e acomodação suave.
5. **Pose final e acompanhamento:** cotovelos baixos, braços sem aparência perpendicular, mãos articuladas e fios legíveis. Conferir alinhamento frontal com o jogador; após a cena, manter acompanhamento suave em deslocamentos e giros, sem saltos ou atravessamentos.
6. **Câmera e acabamento:** enquadrar pega, apoio e levantamento sem esconder o gesto. Revisar rosto sereno, cabelo e camadas da roupa; avaliar penumbra, transições e retorno ao controle em movimento contínuo.

Fluidez exige interpolação, curvas de aceleração e apoios coerentes. Acrescentar quadros-chave deve servir a esses movimentos; aumentar FPS da prévia ou duração total, sozinho, não resolve contato, peso ou clipping. A duração visual atual é de 560 ticks/28 segundos; qualquer mudança será discutida após a revisão e os áudios escolhidos.

## O que já existe e o que falta para encerrar a entrada

**Implementado e observado no cliente:** entrada articulada com cortina e revelação, avanço/elevação, pose dos fios e acompanhamento; correção do jogador em pé com espada para baixo e câmera frontal; áudio provisório e controles locais. O ciclo 69 registra 123 regressões/148 GameTests no perfil base e 148 GameTests no Photon. Esses resultados são históricos; nenhum teste foi reexecutado nesta revisão documental.

**Pendências para homologação final:**

- Revisar artisticamente a sequência completa, principalmente pega, dois braços, apoios, avanço, levantamento, roupa/cabelo e transição ao acompanhamento. Postura entregue não equivale a aprovação de toda a movimentação.
- Selecionar os áudios com o usuário e só então fechar sincronização, mixagem e escuta no jogo.
- Validar visualmente skins classic/slim, canhoto e mão secundária ocupada; chão irregular, interiores e colisão/enquadramento da câmera.
- Conferir interrupções e recuperação de câmera/controles/sons: morte, dimensão, desconexão, menus e alternância de perspectiva.
- Testar observador remoto, dois clientes e servidor dedicado; distinguir a perspectiva do dono e a de quem presencia a transformação.
- Medir desempenho com cenário e hardware registrados, incluindo frametimes. Os contadores de FPS existentes não constituem benchmark controlado.

O contrato de gameplay permanece: release no tick 60, commit no 40, custo único e nenhuma invulnerabilidade adicional. Revisar a apresentação não autoriza mudar esses números ou as passivas.

## Ferramentas — uso previsto e limites

- **PlayerAnimator:** postura e gestos do jogador, sem alterar globalmente as animações aprovadas da Zaraki.
- **Blockbench e GeckoLib:** ferramentas já preparadas para autoria de modelo, rig e animações. Há fonte editável, mas a manifestação atual continua procedural; migração integral para GeckoLib não foi concluída. Avaliar sua necessidade e escopo depois de identificar as limitações concretas na revisão.
- **Better Combat:** integração já existente; preservar a separação entre ataques físicos e técnicas do mod. Senbon/Benihime ainda não têm a mesma autoridade de hitboxes Better Combat dos kits elementais/Asauchi; uma migração exige contrato próprio.
- **Photon/LDLib:** piloto opcional existente. Ainda faltam avaliação gráfica/editor e comparação de desempenho; GameTests do perfil experimental não homologam seus efeitos visuais. Manter o perfil base independente.
- **FFmpeg e laboratório QA:** reutilizar na próxima implementação para comparar marcos e registrar vídeo contínuo do cliente real.
- **Audacity, Blender, Aseprite/LibreSprite e spark:** avaliar conforme necessidade de edição, autoria ou medição; esta revisão não instala ferramentas nem declara benchmark concluído.
- **Curios, SmartBrainLib e TerraBlender:** futuras decisões ligadas, respectivamente, ao contrato de equipamentos Quincy, IA e mundo. Coordenar IA/mundo com a equipe responsável, sem introduzi-las como requisito para terminar a entrada.

## Ordem consolidada das próximas entregas

1. **Benihime — entrada e transformação:** revisão visual conjunta e seleção de áudio; depois ajustes delimitados e validações acima. No momento, somente planejamento/documentação.
2. **Ryūjin — Higashi:** revisar animação, execução e câmera cinematográfica. Preservar a regra de execução a 7% de vida e demais regras existentes, salvo novo contrato de balanceamento.
3. **Ryūjin — Investida e Rajada de chamas:** revisar movimentação, animação do jogador e identidade visual dos ataques, preservando o uso de chamas aprovado.
4. **Benihime — demais técnicas:** revisar animações e efeitos do kit após fechar a entrada. As mecânicas homologadas não precisam ser refeitas como consequência de uma revisão visual.
5. **Uryū Ishida:** próximo novo personagem planejado; pesquisar e fechar contrato Quincy antes de implementar poderes/equipamentos.
6. **Mentor e Proficiências:** continuam aguardando confirmação de informações com a equipe. Retomar a revisão final mais ampla da Hyōrinmaru na etapa posterior combinada; seu estado atual foi considerado bom e não é prioridade desta rodada.

Compatibilidade, fontes editáveis e medições acompanham as etapas pertinentes; não substituem a ordem artística definida pelo usuário. A integração Zaraki já ocorreu; seu kit e suas animações permanecem aprovados e preservados. Byakuya/Senbon e os sistemas de stamina, Resistência, bainhas, esquiva e defesa/Guard Break não são reabertos por esta revisão. Aizen continua adiado; Gin permanece fora do planejamento.

**Próxima decisão:** revisar a entrada por trecho e registrar o que manter/corrigir, enquanto o usuário seleciona os áudios. Nenhuma implementação está sendo iniciada por este documento.
