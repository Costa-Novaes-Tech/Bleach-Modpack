# 119 — Ryūjin curta, áudio e revisão de integração

26/10/2026. Branch `Feature-Poderes-bankais`, base58e3e4e. Implementação local autorizada pelo prompt116. Main, Zaraki e assets de combate exclusivos das duas frentes Antigravity preservados. Sem commit/push nesta rodada.

## Entrega implementada

- Transformação reduzida de40s/18s para **240ticks/12s**. Preferência antiga `ryujinLongEntrance` permanece legível por compatibilidade, mas ambos os valores agora usam12s; ela não reativa a versão rejeitada. Relógio cênico0..140 convertido para tempo real nos marcos0/40/90/140/160/210/240. Release60/commit40 de gameplay não foram estendidos.
- Bankai em2s; sol sustentado até4,5s; selamento4,5–7s; revelação e aproximação da lâmina7–8s; nome em8s; retorno10,5–12s. Legendas e janelas sonoras seguem os cues em tempo real, sem acelerar a voz.
- Fundo musical original reautorado para12s e fogo em stem próprio. Música segue slider Música; fogo segue Jogadores; vozes seguem Voz. Música/fogo recuam sob as duas falas e param no cancelamento/encerramento. Os OGGs de fala existentes não foram regravados nem substituídos. Novos stems são síntese procedural local, com fonte e hashes em [generate_audio.py](qa/cycle118/generate_audio.py) e [audio-manifest.json](qa/cycle118/audio-manifest.json). Não houve instalação ou serviço pago.
- Câmera mantém eixo frontal durante criação, selamento e revelação. Na devolução à primeira pessoa, conserva distância enquanto contorna o jogador e só então se aproxima por trás; não interpola em linha reta através do rosto. Corpo e câmera encerram com o relógio curto. Cena normal não deixa a pose Yamamoto permanente.
- Varredura do C usa conjunto primitivo de colunas X/Z em vez de recriar/hashar BlockPos para todos os volumes3D sobrepostos. Mesma área de terreno, proteção do apoio/blocos protegidos e remoção vanilla; custo45, recarga1200, dano48 e contato no40 preservados. Log de duração está restrito à flag de QA.
- Contrato comum de arma/mão/efeitos consolidado no [118](118-contrato-comum-arma-maos-efeitos-2026-10-26.md). Não apliquei offsets globais ou alterações aos combos dos agentes sem prova. O arco básico fixo Senbon continua identificado como emissor legado que precisa ser substituído em conjunto com a entrega de âncora da frente Senbon.

## Validação realizada e evidências

Java17/Forge1.20.1, perfil base, dependências existentes e Gradle offline. `check build`: exit0,148 regressões; execução inicial1m39s. GameTestServer: exit0,233 obrigatórios aprovados,3m01s. Build/check final após ajustes de apresentação/QA: exit0,148 regressões,25s. [Resumos iniciais](qa/cycle118/check-summary.txt), [GameTests](qa/cycle118/gametest-summary.txt) e [cliente](qa/cycle118/client-summary.txt). A pequena atualização final de QA não alterou contrato de gameplay.

Cliente real: exit0 após5m24s, mundo novo descartável `Benihime-QA-68-1793034131113`, sem abrir save pessoal. Doze cenários: dia com retorno traseiro; noite frontal; retorno à primeira pessoa; canhoto; parede próxima; deslocamento após commit; cancelamento antes/depois do commit; Minami+C em terreno; C frontal/perfil/canhoto em plataforma alta.433 registros amostrados;19 screenshots selecionados com origem/hash em [captures.json](qa/cycle118/captures.json), [prancha](qa/cycle118/contact.jpg), [prévia amostrada sem áudio](qa/cycle118/ryujin-short-sampled-no-audio.mp4). Capturas por marcos não são vídeo contínuo nem certificam ausência de microtravamentos.

Inspeção visual: sol frontal e contração visíveis; espada apontada para fora na revelação, espelhada à esquerda; nome aparece na fase curta; retorno à primeira pessoa inspecionado nos ticks220/236/242, sem rosto/corpo invadindo o quadro final. Cancelamento pré-commit deixa Shikai do cenário e pós-commit mantém Bankai. Ao final dos cenários, releaseBusy=false e cena vazia. Essa matriz não substitui aprovação artística do usuário.

O caso de deslocamento automatizado usa **teleporte** após commit; demonstra deslocamento sem rollback da forma, mas não comprova a saída por tecla física. O caminho de liberação por WASD/ataque após60ticks está preservado e precisa do playtest abaixo. Recursos gratuitos foram habilitados somente no laboratório para impedir que consumo passivo truncasse a inspeção dos12s; consumo normal tem cobertura separada no servidor.

## C: diagnóstico e pendência de eficiência

O impacto examinou3845 colunas. Em terreno comum, a execução medida no servidor durou438,82ms; nos três cenários elevados sem terreno destrutível à frente,5,96/7,85/8,96ms. Não são medições antes/depois no mesmo cenário e não provam um fator de aceleração da otimização. O tempo inclui toda `applyConcentratedSteamCut`, não apenas uma operação de bloco.

No caso com terreno, capturas saltam do178 ao182 com1033ms de intervalo; nos cenários de pose, pares próximos ao impacto ficam em torno de100ms, conforme amostragem a cada2ticks. O intervalo inclui overhead das capturas e não equivale a frametime contínuo medido. O contraste sustenta investigar destruição/updates/efeitos de bloco; não sustenta afirmar que a pausa foi resolvida por keyframes.

**Pendente:** otimização de destruição volumosa/efeitos e medição controlada no item8 do roadmap. O C não fica homologado em fluidez por capturas em plataforma. Qualquer distribuição da remoção de blocos por vários ticks exige discutir as consequências de gameplay antes, em vez de alterar silenciosamente o contrato do golpe.

## O que testar agora

1. Transformar Shikai→Bankai parado, em primeira e terceira pessoa. Conferir se12s têm impacto suficiente, se o sol encolhe até a espada e se a revelação é legível.
2. Ouvir Bankai aos2s e nome aos8s. Avaliar vozes completas, música/fogo recuados e ausência de corte no final. Testar Música baixa/desligada e Voz/Jogadores separadamente. **Escuta subjetiva ainda pendente**; métricas de amplitude não comprovam qualidade ou inteligibilidade.
3. Conferir volta da câmera e pose; repetir canhoto e perto de parede. Após3s, mover/atacar ou usarF5/menu para conferir liberação conforme o caminho atual; não esperar bloqueio artificial pelos12s.
4. Usar C em terreno de teste e em área já vazia. Observar preparação/descida/contato e registrar pausa; essa diferença continua pendente. Não usar mundo importante para um golpe que já destrói terreno.

Multiplayer/observador, sliders baixos no jogo e interrupção por troca de dimensão não receberam nova homologação nesta matriz. Sem mudanças de Hyorin, Higashi, dash/rajada aprovados ou transformação Benihime. As entregas114/115 e posterior integração continuam necessárias para fechar a revisão transversal112.

JAR final: `build/libs/bleachmod-0.2.0.jar`, SHA256 `ccb7fdf373f8fc1b8eb0e33817d095cdd87f9fc77b682a0348c0532f02cf2bae`. [Verificação final](qa/cycle118/verification.json) confirma também que as duas falas têm o mesmo SHA256 da base58e3e4e. Prompts114/115 receberam referência explícita ao contrato118; não foram enviados automaticamente às outras conversas.

## Estado Git e retomada

Fontes, sons e documentação desta entrega permanecem locais. Logs rotativos rastreados em `logs/` apareceram modificados durante a execução; não foram apagados, restaurados ou incluídos em commit. O relatório não rastreado `qa/media-audit-2026-10-26/` de outra conversa também foi preservado. Nenhuma limpeza de pasta/worktree ocorreu. A tentativa anterior de empacotar evidências/build foi interrompida por limite do revisor automático antes de executar; a retomada concluiu as duas operações com sucesso.

Próximo trabalho: playtest do usuário de ritmo/voz/câmera; receber prova de um golpe de cada frente Antigravity conforme118; integrar âncoras/emissor sem controladores duplicados; manter prioridade de eficiência do C. Os demais itens obrigatórios do roadmap72 continuam intactos, inclusive Uryū com contrato da equipe, Mentor/Proficiências adiados, Hyorin Blockbench e limpeza segura; NPCs/quests depois. Aizen adiado, Gin fora, Zaraki preservada.
