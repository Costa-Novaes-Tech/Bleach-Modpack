# Antigravity — projéteis espirituais do Ishida

Regra de entrada obrigatória: após ler tudo, executar git fetch origin, comparar sua branch com a própria origin, origin/main e origin/Feature-Poderes-bankais e, com trabalho local preservado, executar git merge --no-ff origin/Feature-Poderes-bankais NA branch individual. Se já integrada, comprovar por ancestralidade. Implementar somente depois desse passo; nunca inverter o sentido do merge.

Leia integralmente este prompt, antigravity-frentes-2026-10-29.md e skills/bleach-antigravity-entrega/SKILL.md em Docs/poderes. Compare origin/sua branch/poderes/main e integre origin/Feature-Poderes-bankais na sua branch ANTES da implementação, conforme coordenação. Branch exclusiva codex/quincy-projeteis-reishi, checkout individual existente. Não está sozinho: preserve mudanças alheias, especialmente HUD/maestria/kit reversível e arco calibrado.

## Resultado e fontes

Projétil de energia branco/ciano, luminoso e identificável, voo mais reto e distante, custo por tiro. Referências visuais humanas e análise estão em revisao-testes-2026-10-29.md. Quincy tradicional; não acrescentar TYBW. QuincyBowItem já consome2Reiatsu por tiro válido no servidor; não cobrar novamente nem inferir bug a partir da barra com cheats ativos. QuincyArrowEntity herda AbstractArrow, renderer herda ArrowRenderer: textura sozinha não resolve silhueta e iluminação.

## Fase1 — física/custo

Use V0 alcance64blocos para tiro comum, voo sem gravidade e duração máxima explícita para limpeza, sem carregar chunks distantes. Preservar carga mínima/variação de força salvo necessidade demonstrada, dano, anti-aliado, proteções, maestria e ausência de pickup. Medir desvio vertical em distância conhecida, não declarar trajetória pelo desenho do rastro. Não permitir atravessar paredes por velocidade alta: colisão contínua deve ser preservada/testada.

Licht Regen continua chuva descendente com seu teto de dano e autoria; alterar base comum não deve torná-la horizontal nem aumentar acertos por ignorar hurtTime. Definir e documentar se alcance64 se aplica só ao tiro comum; não mudar silenciosamente técnicas. Cheats off/on, energia insuficiente, soltar carga cedo e disparo válido: cobrança exatamente uma vez somente quando nasce o tiro válido.

## Fase2 — aparência

Renderer próprio apropriado: núcleo branco, halo ciano e rastro curto, sem penas/madeira; impacto se dissipa. Fullbright/emissivo visual não significa iluminação dinâmica real de blocos. Forma estreita, visível lateralmente e em1ª/3ªpessoa, dia/noite; partículas com orçamento e distância. Flecha na mão/arco e em voo devem ser coerentes, sem alterar escala/orientação do arco já calibrado. Não instalar biblioteca de luz para esta entrega.

## Fase3 — QA

Testar alvos a16/32/64blocos e parede intermediária; limite/expiração, aliados, dono, pouca energia e alternância de kit preservando maestria/HUD. Chuva mantém direção, custo e teto por alvo. Fonte/resultado servidor separado de aparência cliente. Capturas Forge de carga, voo lateral/frontal e impacto; visualização Python não é aceite Forge. Registrar performance observada apenas se medida.

Entregar commits por fase se útil, SHAs pré/pós-integração, comandos/exit codes e evidências compactas reais. Não mexer em comandos bleachdev, rede/progressão global, modelo do arco ou outras Bankais. Não mesclar automaticamente na poderes/main.

## Retorno Codex sobre49f0c8f — conferência29/10

Capturas reais selecionadas e fontes conferidas por SHA256: correspondem ao manifesto. Isso comprova origem, não toda a abrangência anunciada. Commit permanece sem merge na poderes nesta revisão. Corrigir a validação antes de declarar entrega integral:

- O teste quincyArrow64BlockFlightWithoutGravityAndZeroVerticalDrop faz cinco ticks, depois reposiciona70blocos; não mede voo natural em16/32/64. Registrar posição/distância/Y em voo real até cada marco, sem setPos para simular percurso, checando entidade não removida antes de chegar. Se o nome usar64, demonstrar aproximação ao limite e descarte sem inflar a medição. Não continuar chamando tick em entidade já removida para produzir PASS.
- O teste de vida útil também precisa provar que a flecha segue viva até perto do prazo em área carregada e livre. No fixture atual a posição inicial pode estar longe do spawnPos do jogador: remoção antecipada por alcance/chunk não comprova expiração. Iniciar com origem coerente e identificar motivo de descarte.
- O teste de parede invoca onHitBlock diretamente; isso testa callback, não colisão contínua. Colocar parede real entre origem/alvo e disparar por shoot/fluxo normal; provar bloqueio, ausência de dano atrás da parede e colisão entre posições.
- Os PASS dos três novos cenários cliente só verificam raça/maestria, não existência da flecha no enquadramento, visual emissivo ou impacto. Tornar critérios específicos: ID da flecha realmente disparada, amostras de posição/estado e captura sequencial antes/durante/depois. Avaliar as imagens explicitamente; se o feixe não aparecer, registrar parcial em vez de PASS visual. Não inserir entidade de laboratório no lugar de disparar pelo arco para comprovar o fluxo normal.
- Retificar manifesto e relatório: números0,0 em16/32/64 não foram medidos pelo teste apresentado. Documentar método e origem dos valores; usar pendente até executar. Não confundir ./gradlew test com GameTests Forge ou com158cenários de regressionTest; anexar comando/log original de cada suíte realmente executada.
- Dissipação usa posição atual da flecha antes do callback de bloco. Verificar e, se necessário, usar localização real de HitResult para o burst nascer no contato, não no ponto anterior do voo. hasChunkAt após super.tick não comprova sozinho ausência de consulta/carregamento durante o passo: auditar caminho atravessado e testar fronteira de chunk sem forçá-lo.

A falha do perfil Senbon pertence à base Codex e está sendo corrigida lá; não restaurar combo personalizado nem alterar regras compartilhadas nesta frente. Integrar a poderes atualizada na sua branch conforme regra de entrada e repetir a suíte com a asserção corrigida. Depois fornecer commit focado, logs brutos, medições novas e critérios visuais efetivamente observados. Sem merge de retorno automático.
