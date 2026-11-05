# 125 — Prompt Antigravity: refino Hyōrinmaru e Higashi em fases

## Pedido para copiar à segunda conversa

Trabalhe na sua branch existente `codex/ryujin-bankai-sol-selamento`, no checkout separado já utilizado, apesar do nome histórico da branch. Não criar pasta nova. Antes de editar, conferir status, worktree, log e divergência com Feature-Poderes-bankais; sincronizar apenas preservando alterações locais e autorização Git vigente. Não trabalhar na main. Você não está sozinho: outra conversa cuida exclusivamente de Senbon; não editar seus assets, efeitos, QA nem documentos. Zaraki e Benihime aprovadas intocadas.

Leia instruções/skills locais, Docs/poderes/00-CONTINUIDADE-CODEX.md, 123 e esta revisão de planejamento. Use bleach-model-pipeline para Hyōrinmaru e bleach-mod-dev para integração/QA. Consulte os documentos83–99 pertinentes e contrato118; documentos antigos podem estar superados. Runtime atual prevalece sobre números históricos, exceto requisito explícito do usuário. Documentação nova só em Docs/poderes.

A autorização agora é refinar modelos/animações Hyōrinmaru e Higashi. A transformação Ryūjin atual está aprovada: não mudar seus12s, música, sol, câmera, fumaça ou absorção. Não reabrir investida/rajada, Minami ou o C Ryūjin. Higashi é uma técnica distinta do C; não confundir os serviços.

## Fase0 — inventário e linha de base

Mapeie geradores, fontes Blockbench, geo, UV/texturas, clips, nomes de bones e controladores Java efetivamente usados. Inspecione IceDragonRenderer, HydraMotion, IceDragonEntity, geradores ice_hydra.py/hydra_organic.py e assets antes de assumir disponibilidade ou compatibilidade. Registre proporções em blocos e pivôs atuais, capturas de perfil/frente/3/4 e detalhes da mandíbula em repouso/baforada.

Para Higashi, mapear RyujinChoreography, HigashiExecutionVisuals, HigashiCinematicPath, cliente cinematográfico e serviço de execução; registre qual camada manda em braço, item e câmera e os ticks atuais. Identificar disputas de controlador antes de adicionar outro.

## Fase1 — Hyōrinmaru: laboratório anatômico

Use Blockbench instalado pela ponte MCP local se operacional; não afirmar uso se apenas gerou JSON por script. Não instalar pacotes só para ampliar lista de ferramentas. Abra cópia de laboratório ag_ sem alterar abas alheias. execute_script continua desligado. Se indisponível, utilize fontes locais e registre a limitação.

Refino da hidra: três cabeças glaciais serpentinas imponentes, pescoços grossos e contínuos na raiz afinando gradualmente até o crânio. Evitar pescoço de tubo fino com cabeça grande e discos/articulações aparentes. Pescoços articulados em cadeia com sobreposição de gelo, sem lacunas ao curvar. Não aumentar cegamente escala global nem obstruir enquadramento aprovado.

Cabeça: focinho alongado, cristas glaciais, órbitas e olhos emissivos delimitados, maxilar superior separado da mandíbula inferior. Boca com profundidade escura, palato, língua discreta e dentes superiores/inferiores distribuídos sem blocos flutuantes. Nada de dentes atravessando lábios quando fecha. Abertura parte do côndilo, não do queixo ou ponta dos dentes. Provar sinal/eixo real no renderer, não confiar só na convenção do editor.

UV dedicado por material: gelo azul no núcleo, bordas claras, boca escura, presas opacas e olhos em canais próprios; sem boxUV sobreposto que transforme dentes em olhos. Evitar transparência extrema e excesso de faces coincidentes. Preferir silhueta e material com orçamento conhecido a milhares de cubos sem ganho.

## Fase2 — rig, animação orgânica e integração da hidra

Definir um controlador responsável por cada canal. Se Java procedural escreve mandíbula por tick e sobrescreve clip GeckoLib, resolver propriedade/composição explicitamente; não deixar dois autores redefinindo a mesma rotação. Nomes de bones são contrato com Java: preservar ou migrar todas referências e validadores juntos.

Emergência: raiz estabiliza, pescoços desenrolam com atraso progressivo e cabeças acompanham. Três cabeças têm pequenos offsets de fase, sem loop idêntico nem tremer. Baforada: antecipação de pescoço/mandíbula, abertura progressiva, sustentação com micromovimentos e fechamento amortecido. Maxilar inferior acompanha a cabeça em toda curva, nunca fica no mundo ou travado durante sopro. Baforada sai da boca real, não de um ponto parado no peito. Não mover cabeça e deixar dentes/efeito para trás.

Manter transformação atual e sua revelação na neve; ajustar pose visual sem mudar duração de áudio/commit por conveniência. No ataque triplo, preservar três saídas por trás do jogador, avanço até impacto e explosão sem retorno, alcance/dano/congelamento/resíduos existentes; não reintroduzir três projéteis retos sem pescoço nem conexão weather global. Validar obstrução por terreno/parede conforme serviço atual. Asas, cauda e flores do jogador devem compartilhar acabamento glacial, com asas articuladas e cauda sem atravessar o corpo; refinar após a mandíbula funcionar, sem redesign desnecessário do kit.

Atualizar a fonte mestre e regenerar exports: não editar somente geo ou textura gerada. Verificar comando --check suportado e executar, documentando idempotência. Salvar bbmodel editável, texturas e animações coerentes; não introduzir dependência obrigatória nova. Se precisar resolver UV, preserve arquivos originais no Git e não duplique grandes backups no repo.

## Fase3 — Higashi: coreografia de execução

Primeiro comparar estado atual com pedido; corrigir defeitos comprovados sem substituir transformação Ryūjin ou mudar o ataque supremo C. Silhueta esperada: recolher katana próxima ao quadril do próprio jogador, preparando um corte diagonal ascendente; ponta/gume orientados para varrer baixo→alto através do alvo. Punho, cotovelo, ombro, tronco e item acompanham o mesmo gesto. Evitar espada presa no torso enquanto os braços se mexem, mão atravessando guarda ou braço torcido. Não criar pausa antes de atravessar o alvo.

Marcos atuais de referência em HigashiCinematicPath: IMPACT12, SLASH_END16, IGNITE56, END88; conferir no checkout. A pausa dramática de40ticks começa depois do fim do corte, não no início da preparação. Preservar os2s após a passagem da lâmina: pose final firme, alvo visual legível e uma linha quente discreta no plano do corte. Após a pausa, ignição rápida, incineração e cinzas; retornar câmera apenas depois desse evento, sem limpar o alvo visual cedo demais.

Câmera lateral estável deve incluir ambos de corpo suficiente para ler espada e contato, sem ficar dentro do alvo ou orbitar até perder o gume. Escolher lado livre por colisão se necessário; fallback estável em corredor/parede; escala da composição considera alvo pequeno/grande sem FOV extremo. Não aplicar câmera de execução em golpe comum/erro/ataque que não mata. Observadores não devem ter câmera sequestrada.

Condição funcional a preservar: execução em alvo elegível≤7% OU morte realmente confirmada pelo dano normal da Higashi, mesmo se tinha>7% antes. Servidor é autoridade. Miss, sobrevivente, invulnerável, aliado, totem ou dano cancelado não autorizam incineração/morte fictícia. Não usar dano bruto previsto como prova de letalidade nem remover entidade antes de confirmação. Se o alvo morreu no servidor, usar apresentação transitória já existente para a pausa/cinzas; não ressuscitar, gerar outro drop ou duplicar XP/dano. Não mudar a regra para executar qualquer tentativa.

Preservar morte, atribuição, custos e tempos atuais; o objetivo é correção de pose/arma/câmera e clareza, não buff ou novo sistema universal. Ajustes compartilhados em RyujinChoreography devem restringir-se a Higashi/execução e provar que demais ações não mudaram. Não alterar mixin global para compensar uma pose local.

## Fase4 — validação e entrega por feature

Hyōrinmaru: vídeo real frente/perfil/3/4 mostrando abrir/sustentar/fechar as três bocas; ponto emissor acompanha cada boca; transformação completa, ataque triplo até três impactos, asas/cauda e retorno livre. Testar solo irregular, câmera próxima/distante, dia/noite e qualidade reduzida. Informar contagem de bones/cubos e custo observado quando medido. Não declarar multiplayer ou desempenho sem execução.

Higashi: casos threshold, lethal acima7%, survive, miss, aliado, invulnerável e totem/cancelamento quando ambiente permite; alvo pequeno/grande, parede, mudança de dimensão/desconexão durante apresentação. Vídeo contínuo lateral com preparação, corte, pausa2s e cinzas; marcos individuais sem esconder falhas pelo fogo. Demonstrar mão/arma com VFX desligado e repetir com efeito completo. Conferir retorno da câmera, HUD, FOV e controles em todos os encerramentos.

Executar check/regressionTest e GameTests pertinentes, sem alterar expectativas para aprovar regressões; testar cliente Forge real. Base sem Photon e perfil opcional separado se tocado; shader/editor não substitui jogo. Relatar o que não foi executado.

Separar commits/relatórios: (1) fontes/modelo/rig Hyōrinmaru; (2) integração/animação Hyōrinmaru; (3) Higashi. Assim o usuário poderá testar e integrar sem depender de aceitar as duas features juntas. Não fazer refatoração ampla fora do escopo. Continuar entre fases com resultados técnicos; aprovação artística permanece do usuário. Documentar arquivos, hashes, comandos, evidências e riscos. Nenhum merge na main.
