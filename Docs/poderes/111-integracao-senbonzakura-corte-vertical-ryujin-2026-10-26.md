# 111 — Integração Senbonzakura e corte C vertical da Ryūjin

26/10/2026. Solicitação do usuário: revisar entrega Antigravity, fazer merge na Feature-Poderes-bankais e adaptar o C da Ryūjin para empunhadura dupla à frente, corte vertical e contato da katana no chão. Main e Zaraki preservadas; sem publicação no remoto nesta rodada.

## Git e proveniência

- Destino inicial77211b3, com alterações locais validadas do ciclo110. Origem codex/design-poderes-criaturas em d20cc3d com entrega Senbonzakura sem commit.
- Trabalho110 preservado no commit local6d3c9b3, na Feature.
- Entrega Antigravity revisada e registrada na branch de origem como08105c2. Não inclui cache de build, vídeo privado, arquivos do brain ou mudanças de logs.
- Merge local `db0f764`, estratégia ort, sem conflitos. Inclui perfil de esgrima de três ataques, animações próprias, alcance compatível com bônus Bankai, asas/halo/lâmina de Hakuteiken, mira durante preparação e direção fixada no lançamento.
- O documento109 do Antigravity descreve a base histórica dele (146 regressões/224 GameTests). Não usar esses números como teste do conjunto integrado; o109 da Ryūjin tem outro nome/caminho e ambos foram preservados.

## Ajustes de integração encontrados na revisão

- SignatureActionVisuals ainda gerava o overlay de swing antigo para Senbonzakura, competindo com o Better Combat. A geração de melee legado agora respeita a seleção pilot; técnicas continuam com suas animações autoritativas.
- Bônus de alcance só vale com Senbonzakura, identidade correta, Bankai e modificador presente; não reutilizar um modificador ainda não removido ao trocar de arma/forma. Teste verifica4,55 blocos de tolerância do servidor contra3,05 base, sem soma duplicada.
- Faixa de pétalas do combo só é enviada em swing permitido. Não muda dano/stamina.
- Comando manual do QA recebido não pode contornar o laboratório nem registrar em produção. Mantidos os requisitos de dev, singleplayer/admin, flag e save/diretório descartáveis.

## Nova coreografia C da Ryūjin

- 0..10ticks: aproxima mãos e centraliza a espada em frente ao peito.
- 10..27: sustenta empunhadura dupla com lâmina vertical; brasas/cinzas discretas convergem.
- 27..35: eleva o conjunto acima da cabeça, com preparação corporal curta.
- 35..40: descida vertical, sem giro de tronco lateral do desenho anterior.
- 40: ponta alcança o chão e efeito de contato inicia o ataque existente.
- 40..46: sustenta contato brevemente;46..56 retorna ao controle normal.

Ângulos aproximam os punhos sem deslocar os pivôs dos ombros. Rotação local do item participa da direção física usada pelos traços. Teste cobre ambas as mãos, distância entre punhos inferior a0,22bloco durante10..46, mão centralizada, direção descendente e ponta a até0,04bloco do plano do chão no40. Valores são aproximação do rig; comprovação artística depende das imagens do cliente. Política atual da mão secundária ocupada é preservada; QA usa mão secundária vazia.

O gesto continua56ticks/2,8s, dano no40/2s, custo45 e recarga1200. Não há câmera forçada no uso normal. Brasas foram reduzidas após a primeira inspeção porque escondiam as mãos. Explosão/convergência no solo acompanha a ponta, sem dano extra.

### Apoio e limites da destruição

A captura inicial em terreno comum mostrou queda do jogador quando o executor antigo removia inclusive sua sustentação. Agora preserva as colunas imediatamente sob a projeção horizontal da caixa do próprio conjurador. Destruição à frente, proteções de blocos e área/dano permanecem; GameTest verifica apoio preservado, pedra à frente removida e bedrock intacto.

O C antigo atualiza uma região grande de blocos no mesmo tick. A captura inicial em terra saltou do tick visual38 para58 durante o impacto, com aproximadamente0,9s entre registros, além da queda. Isso não certifica fluidez contínua. A rodada final usa terreno normal para conferir apoio e, separadamente, plataforma alta resistente para inspecionar o rig sem a carga de atualizações de chão. Otimização da destruição volumosa permanece vinculada ao item8 de eficiência; não ocultar essa diferença entre cenário de pose e cenário destrutivo.

## Testes

- Primeiro check/build após merge: exit0,33s,147 regressões.
- GameTests após revisão dos punhos/alcance:233 obrigatórios aprovados. Após proteção de apoio, suíte completa repetida: exit0,1m50s,233 aprovados.
- Senbonzakura no cliente real integrado: seis cenários em frente/perfil/costas, combo e Hakuteiken; casts com busy=true e conclusão de sequência registrados. Capturas inspecionadas mostram golpe/efeito de pétalas e asas/halo redesenhados; não representam aprovação artística automática nem modelo volumétrico completo de penas.
- Ryūjin: QA traseiro em terreno comum, frontal, perfil e canhoto em plataforma de observação. Capturas finais de contato e preparação inspecionadas em frente/perfil; espelhamento do item esquerdo calibrado localmente em+20graus nesta habilidade, sem alterar o modelo/ataques globais. A primeira tentativa de sinal oposto foi corrigida por inspeção real, não por alterar a asserção geométrica.
- Multiplayer, Photon, áudio subjetivo e primeira pessoa com ambos os braços visíveis não são presumidos por essa matriz de terceira pessoa.

## Fechamento

- Check/build final: exit0,28s,147 regressões aprovadas. GameTests finais233 aprovados; últimas mudanças de calibração são só apresentação/QA e não alteram a regra do impacto.
- Cliente: seis cenários Senbonzakura e quatro Ryūjin,218 registros amostrados nas fontes finais combinadas. A repetição isolada da mão esquerda substitui a captura anterior na seleção de evidências. [Origem e hashes](qa/cycle111/captures.json), [verificação](qa/cycle111/verification.json), [prancha](qa/cycle111/contact.jpg), [prévia do C em frente/perfil, amostrada e sem áudio](qa/cycle111/c-vertical-sampled-no-audio.mp4).
- Custo/dano/cooldown/impacto40 mantidos; diferença de terreno restrita ao apoio imediato do conjurador. A captura de contato em plataforma não comprova ausência de pausas na destruição do terreno comum; manter otimização de blocos no item8 do roadmap.
- JAR `build/libs/bleachmod-0.2.0.jar`, SHA256 `9125919121775634a258d4e18c83c1f1b381f56622187d0d21feccd3829fe1be`.
- Merge e registros são locais; main/Zaraki preservadas. Aceite artístico, áudio subjetivo, primeira pessoa com duas mãos, multiplayer e Photon não são promovidos por esse teste.

### Retomada de entrega e tarefa112

JAR regenerado após a retomada: check/build exit0,25s,147 regressões. Código runtime idêntico ao validado; SHA256 atual `6ab40297cc28b1200d39a3e6e8682aeb6ee8019a33502bf6159140aeeef9ee4e`. A [tarefa112](112-tarefa-revisao-combate-armas-maos-efeitos-2026-10-26.md) registra a nova reclamação transversal de arma/efeitos que não acompanham as mãos. Essa correção geral ainda está pendente; não declarar resolvida pelo C vertical ou pelo merge do Senbonzakura.
