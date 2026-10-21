# Ciclo 75 — testes Benihime/Higashi e próxima etapa

20/10/2026. Usuário pediu começar os testes e as próximas implementações da lista obrigatória. Branch `Feature-Poderes-bankais`, HEAD de referência `7621758`; alterações dos ciclos 72–74 já presentes e preservadas. Esta rodada prepara a revisão dos itens 1 e 2 e amplia somente o laboratório de QA, sem mudar habilidades de produção, balanceamento, áudio, rede 2.29, schema 9 ou Zaraki. Sem commit/push/merge.

## Preparação entregue

O perfil de desenvolvimento abre um mundo novo isolado com prefixo `Benihime-QA-68-`, sem usar o save pessoal. Com `-PbenihimeQa=true -PcinematicQa=true -PcinematicQaFast=true`, a sequência passa diretamente por três cenários da Higashi e deixa o cliente disponível depois.

Os comandos abaixo são exclusivos do laboratório: exigem desenvolvimento, singleplayer/admin e nome do save de QA. Não ficam disponíveis como atalhos de progressão nos mundos normais.

- `/cinematicqa benihime`: equipa/vincula Benihime, prepara formas/maestria/recursos no save de QA e inicia a transformação real. Permite repetir o palco, a luz, a câmera e o áudio do ciclo 74 sem configurar o personagem a cada tentativa.
- `/cinematicqa higashi threshold`: alvo com 7/100 de vida; deve executar por limiar.
- `/cinematicqa higashi lethal`: alvo com 10/20 de vida; deve morrer pelo dano normal e receber a apresentação de incineração, embora acima de 7% antes do impacto.
- `/cinematicqa higashi survive`: alvo com 1000/1000 de vida; deve sobreviver. A prévia/corte e fogo normal não devem virar execução confirmada, cadáver ou cinzas de morte.

As três Higashi agendam o golpe em 20 ticks. Manter os controles livres durante a rodada automática. O alvo de laboratório é removido após o período de observação; somente entidades marcadas com o UUID do operador são limpas.

O resultado do servidor agora registra `expectedAlive` e `passed`, distinguindo os casos positivos do negativo. A animação é avaliada separadamente pelos quadros do cliente; alvo morto não comprova enquadramento ou fluidez. As etiquetas DRAW/CUT/HOLD/IGNITE/ASH nos metadados são janelas do relógio QA, não confirmação de que uma execução aconteceu no caso sobrevivente.

## O que revisar com o usuário

### Item 1 — entrada Benihime

- Cena completa com áudio: “Bankai”, subida/abertura da cortina, apoios/elevação, nome completo e retorno.
- Equilíbrio do nome com a música após o fade antecipado; análise de sinal do ciclo 74 não substitui escuta.
- Piso e penumbra/área preservada sobre o jogador; lembrar que o piso é visual, sem colisão ou achatamento do mundo.
- Posição diante de parede/árvore e acompanhamento após a apresentação. A busca de espaço atua na entrada, sem desvio contínuo de obstáculos no acompanhamento.

### Item 2 — Higashi

- Preparação da lâmina junto ao quadril e corte diagonal de baixo para cima com contato perceptível.
- Pausa de dois segundos após a passagem, sem fogo/poof prematuros do cadáver.
- Câmera lateral com os dois participantes, incineração/cinzas e devolução da perspectiva.
- Mesma apresentação quando o dano normal mata acima do limiar; nenhuma incineração de execução quando o alvo sobrevive.

Não alterar o roteiro aprovado apenas por disponibilidade de uma ferramenta nova. Correções seguintes devem usar o feedback e os defeitos efetivamente observados.

## Validação desta rodada

`check build prepareCombatTestPack preparePhotonTestPack --offline --console=plain` passou em 2m16s, com **127 regressões** e geração dos dois pacotes. Como a alteração é de comandos/captura client-side do laboratório e não mudou contratos de produção, a suíte GameTest completa não foi repetida: a última referência permanece **155 GameTests em base e Photon**, do ciclo 74. Não atribuir essa execução anterior ao ciclo 75.

**Rodada gráfica concluída no cliente base**, em save novo de QA. Logs registram:

- [Dano normal letal](qa/benihime/cycle75/lethal/index.html): `victimAlive=false`, `expectedAlive=false`, `passed=true`.
- [Execução por limiar](qa/benihime/cycle75/threshold/index.html): `victimAlive=false`, `expectedAlive=false`, `passed=true`.
- [Sobrevivente](qa/benihime/cycle75/survive/index.html): `victimPresent=true`, `victimAlive=true`, `expectedAlive=true`, `passed=true` antes da limpeza do alvo de QA.

Foram capturados respectivamente 62, 70 e 66 quadros; as galerias exportam subconjuntos de marcos com PNGs originais e hashes, mantendo metadados completos. As amostras inspecionadas mostram os dois participantes na câmera lateral, corpo durante a pausa e início de ignição nos casos confirmados; no sobrevivente, retorno ao HUD com alvo vivo e fogo normal. Houve toast vanilla de avanço no caso letal e chamas de chão em primeiro plano: não são aprovação estética definitiva nem efeitos novos implementados nesta rodada. Contato e fluidez fina continuam sujeitos à revisão, sem inferir aprovação de todos os quadros pelo resultado de vida do servidor.

Avaliação do agente nos marcos inspecionados (0–5): enquadramento 4; legibilidade dos participantes/efeitos 3, com interferência do toast/fogo no primeiro plano; leitura da preparação/corte 3. Sem nota de fluidez integral, escuta ou desempenho a partir destas amostras.

O cliente foi deixado aberto após `Cinematic QA 75 automatic sequence complete; client remains available for manual testing`. O comando novo de Benihime foi compilado e disponibilizado, mas sua execução manual/escuta pelo usuário ainda não foi observada nesta rodada. Não compilar outra versão enquanto o usuário estiver testando.

[Log de check/build](qa/benihime/cycle75/validation-cycle75-check.log), [log do cliente até o fim da sequência](qa/benihime/cycle75/validation-cycle75-client.log) e [manifesto dos pacotes](qa/benihime/cycle75/delivery-manifest.json). Pacotes base/Photon com cinco/sete JARs conferidos, ZIPs íntegros. Photon não foi aberto graficamente nesta rodada; tampouco houve ensaio com dois clientes ou avaliação auditiva nova. As edições de código desta rodada estão somente em `CinematicQaCommands` e `CinematicQaSequence`.

## Próxima implementação da ordem obrigatória

Após a revisão da Higashi, o item 3 é **Investida e Rajada Ryūjin**. As duas já têm execução e coreografia; esta etapa é refinamento de apresentação, não criação de um kit novo:

- Investida: conferir antecipação do corpo, continuidade do movimento durante o deslocamento, rastro de chamas e recuperação ao parar/colidir. Preservar a autoridade do executor existente sobre movimento, custo e acertos.
- Rajada: conferir gesto de emissão e relação entre espada, direção das chamas e avanço do ataque. Preservar partículas de fogo aprovadas e sua distinção do Leque; não presumir nova quantidade de disparos, alcance, dano ou custo.

Depois seguem combate da espada/demais técnicas Benihime, Ishida, Mentor/Proficiências quando a equipe confirmar e Hyōrinmaru/Blockbench. NPCs de combate e quests da lore continuam propostos para depois da lista. Nenhum item é dado como homologado apenas por iniciar esta rodada.
