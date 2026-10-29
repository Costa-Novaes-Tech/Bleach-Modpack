# Revisão dos testes de 29/10 — Ryūjin, Quincy e Senbon

Pedido: analisar gravação e investigar construção das animações, antes de organizar próximas correções. Sem alteração de runtime, merge ou execução do Minecraft nesta rodada.

**Decisão posterior do usuário em29/10:** a proposta abaixo de manter dano durante a cena foi substituída por invencibilidade temporária durante a ativação da Bankai. A distribuição também mudou: três AG com proteção/projéteis/Garden; Codex substitui combo Senbon por clipes prontos. Ver antigravity-frentes-2026-10-29.md. Este relatório preserva o diagnóstico e as propostas anteriores como histórico, não como contrato contrário à decisão humana.

## Evidência e limites

Fonte: C:/Users/joaop/Downloads/Gravação/Gravação de Tela 2026-10-29 075014.mp4, 240,41s, 1920x1020,30fps. Revisão panorâmica de80quadros espaçados em3s; inspeção de270quadros consecutivos nos intervalos59–60s,195–197s e222–228s. Derivados em build/review-20261029, descartáveis; original preservado. Não é inspeção manual de todos os aproximadamente7200quadros nem avaliação auditiva. Tempos dos derivados relativos ao início solicitado, arredondados à grade30fps; não são telemetria de ticks do jogo.

Código conferido na Feature-Poderes-bankais, HEAD5b5587c, status inicialmente limpo. A gravação não informa seu hash de build; correspondência exata com o JAR local não comprovada. As causas abaixo foram verificadas no código atual, sem reprodução adicional. Leitor imageio-ffmpeg0.7.0 preparado isoladamente em build/video-analysis-runtime usando wheel em cache; nenhuma dependência do mod alterada.

## Observações

- 0–1:50 aproximadamente: kit Quincy, disparos, mobilidade e Sprenger. HUD de técnicas/maestria aparece; indicador de testes/custos impede concluir cobrança pela barra cheia. Flecha/efeito de impacto no intervalo59–60s não permite medir trajetória inteira ou queda em blocos sem instrumentação.
- 2:03 em diante: Senbon selada/Shikai, depois transformação e habilidades Bankai. Em2:33–2:36, slime em primeiro plano obstrui fortemente a cinematográfica. Em3:15–3:17, quadros consecutivos não demonstram de modo legível toda a sequência diagonal/retorno/estocada; movimento do jogador e vista traseira distante limitam julgar contato de cabo/antebraço. Não declarar causa exclusiva do JSON a partir desse trecho.
- Ryūjin inicia apresentação aproximadamente3:37; quadros3:42–3:48 mostram sol, absorção e retorno abrupto à HUD por volta3:46,6. Há mobs próximos e fogo, mas vídeo não identifica listener, origem do dano ou entrada voluntária causadora. Relato humano de cancelamento por contato é compatível com vulnerabilidades abaixo, sem provar que empurrão sem dano sozinho cancela.
- Benihime aceita pelo usuário. Este vídeo não constitui nova bateria dedicada da Benihime; não reabrir o kit.

## Ryūjin: diagnóstico e correção proposta

RyujinBankaiEntranceService.hurt cancela tanto quando o jogador recebe dano quanto quando ele é a origem do dano. RyujinBankaiEntranceClient também desliga câmera por hurtTime. Eventos ambientais/passivos podem, portanto, conflitar com a própria apresentação. Há teste existente que exige cancelamento por dano: precisa ser deliberadamente atualizado junto com o novo contrato, não removido para deixar a suíte verde.

Proposta: toque/empurrão/dano comum não terminam apresentação por si só; dano real continua existindo, sem invulnerabilidade implícita. Dano automático de fogo/passiva não equivale a saída voluntária. Manter encerramento por morte, desconexão, dimensão, arma/identidade inválida e saída deliberada prevista. Conciliar cliente/servidor, áudio, pose e retorno suave. Sem reiniciar, cobrar novamente ou reverter Bankai já efetivada. Não alterar Zaraki/Benihime globalmente.

QA necessário: empurrão sem dano, dano de mob, projétil, fogo próprio causando dano em mob, morte e saída voluntária; antes/depois do commit da forma. Registrar motivo/tick do encerramento. Evitar usar a geração de fogo destrutiva como solução de proteção da cena.

## Quincy: o que já existe e o que mudar

QuincyBowItem já consumeReiatsu(COMMON_SHOT_PE_COST=2) no servidor por disparo válido. Cheats podem ignorar custo; soltar antes da carga mínima não dispara. Não adicionar cobrança duplicada. Testar cheats off, energia insuficiente, cancelamento e liberação válida.

QuincyArrowEntity é própria, mas herda AbstractArrow, não desliga gravidade no código conferido; limites comuns48blocos e LichtRegen40. QuincyArrowRenderer herda ArrowRenderer com textura própria, portanto trocar apenas textura não muda toda a silhueta/iluminação.

Proposta V0 para discussão: tiro comum com núcleo branco e halo ciano emissivo, rastro fino e dissipação curta no impacto; sem madeira/penas. Voo reto sem gravidade, precisão estável, alcance experimental64blocos e duração máxima para não acumular entidades. Preservar colisão com paredes, aliados/proteções, crédito de maestria e teto de dano da chuva. Distância/duração/visual são parâmetros propostos, não novo balanceamento homologado. Chuva mantém direção descendente própria; não deve virar tiro horizontal ao compartilhar projétil.

Referências humanas: arco branco com cruzes azuis e variante luminosa ciano; usar como identidade visual, sem importar automaticamente poderes TYBW. Manifestação na mão/flecha carregada/projétil voando devem compartilhar identidade. Nenhuma textura gerada nesta revisão.

## Como os golpes são construídos

data/bleachmod/weapon_attributes define a sequência/hitbox/upswing/ID da animação do Better Combat. Asauchi herda bettercombat:sword. Ryujin usa two_handed_slash_vertical_right, one_handed_slash_horizontal_right e one_handed_stab. Hyorinmaru usa one_handed_stab e cortes horizontais esquerdo/direito.

Senbon aponta a bleachmod:senbonzakura_diagonal, return e thrust. assets/bleachmod/attack_animations contém clipes próprios no formato Emote, com ticks/easing e canais de corpo/braço/item. assets/bleachmod/player_animation contém versões Bedrock para registro PlayerAnimator/cinemáticas conforme consumidor. Esses formatos não são intercambiáveis; presença dos dois arquivos não comprova qual clip tocou no combate. Gerador tools/animations/generate_senbon_137.py gera ambos e resolve deslocamento rightItem. PlayerAnimator aplica transformações; Better Combat seleciona sequência/cadência; pose/câmera de técnica em Java é outra camada.

Próxima prova: mesma situação de ataque livre, parado, sem VFX e sem QA impondo pose; comparar uma arma pronta e Senbon, observando ID/tempo do clip e matriz real corpo/braço/item. Só depois corrigir clipes ou composição. Não copiar rotações do modelo antigo às cegas. Comparar slim/normal e canhoto, selada/Shikai/Bankai, primeira/terceira pessoa. Rastro aprovado não comprova coreografia.

## Garden N: segunda explosão como prisão ampla

Atualmente finishField aplica FIELD_BLOOM_DAMAGE14 uma vez e apaga o campo; não mantém prisão. Preservar centro fixo, raio35, primeira explosão e reativação2–6s. Depois da flor/segunda explosão, criar fase própria de aprisionamento — não prolongar indevidamente a janela de reativação.

Proposta V0: capturar alvos válidos no volume do campo na segunda explosão; envolvê-los individualmente em pétalas, levitação controlada por3s e três pulsos de dano separados por1s. Distribuir o dano hoje concentrado entre explosão/pulsos antes de aumentar dano total. Não transformar toda a área em voo infinito: teto de elevação, checagem de teto/paredes/terreno e lease curto do efeito. Sem exigir mira direta; seleção relativa ao centro fixo, não visão atual do conjurador. Não atravessar paredes nem afetar aliados/espectadores.

Reusar conceitos da Convergência, mas não copiar sua remoção por hasLineOfSight do dono: isso faria o Garden depender novamente da direção/posição do jogador. Definir volume vertical e critérios de saída na ficha. Limites de alvos/VFX e distribuição de dano precisam ser declarados e testados em multidão. Encerrar por morte, troca de dimensão/kit e término; não remover efeito alheio nem deixar levitação persistente. Preservar pétalas no interior e forma de flor na borda.

## Frentes propostas e avaliação do Antigravity

1. Codex: Ryūjin/interrupção e contrato compartilhado de saída, sem alterar as outras Bankais.
2. Antigravity A: Quincy, primeiro física/custo, depois renderer/rastro e confirmação real em1ª/3ªpessoa.
3. Antigravity B: diagnóstico Senbon e prova isolada de katana; só após essa prova, Garden em fase separada, evitando nova mistura de coreografia e VFX.

Sem despacho, branches novas ou implementação autorizada por este documento. Usar o contrato skills/bleach-antigravity-entrega/SKILL.md como leitura explícita. Critérios antes de codar, arquivos exclusivos, evidência sem/com partículas e marcos no mesmo tempo efetivo. Este caso é exercício assistido com diagnóstico disponível; não apresentá-lo depois como avaliação cega de capacidade.
