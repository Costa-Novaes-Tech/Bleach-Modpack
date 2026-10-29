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
