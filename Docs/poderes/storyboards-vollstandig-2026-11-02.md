# Uryū — storyboards de combate Vollständig — 02/11/2026

## Estado e escopo

**Proposta de design para revisão humana.** Próximo passo do plano Quincy: transformar os papéis já registrados de H/N/B/C em sequências visuais legíveis antes de especificar implementação. Não é aceite de Ginrei, mudança de balanceamento, storyboard de transformação, especificação final de animação ou prova de cliente Forge.

Este bloco usa apenas o perfil Vollständig já discutido. A decisão sobre um armamento intermediário Ginrei permanece aberta e independente. Letzt Stil, Schrift/Antithesis, economia, aquisição de técnicas, destruição de blocos, falas e áudio ficam fora deste storyboard.

## Contratos preservados

- Vollständig já ativa no protótipo; a lacuna relatada é identidade e apresentação do kit.
- Manter arco Kojaku e empunhadura em mãos do jogador. Asas e quatro estrelas são manifestação visual; não são pais do arco, braços substitutos ou fonte de voo.
- Salva comum aprovada para teste: uma flecha central mais quatro auxiliares; carga de 12 ticks, alcance de 96 blocos e custo único de 4 Reiatsu. Dano-base e regras de hurtTime permanecem os já implementados.
- H preserva a técnica central e quatro auxiliares dentro do custo da técnica; N não dispara salvas recursivas. Não multiplicar dano por quadro nem introduzir aquisição de alvo ilimitada.
- N mantém o despacho existente de 12 projéteis em intervalos de 3 ticks. B mantém as três cargas atuais de Hirenkyaku. C mantém o compromisso de 30 ticks e a área/dano atuais. Nenhum valor é redefinido aqui.
- Ataques comuns mantêm mira e controle do jogador. Câmeras de perfil/aéreas são vistas de estudo/QA, não instrução para sequestrar a câmera em jogo.
- Estado, custo, acerto, interrupção e conclusão continuam sob autoridade do servidor. A apresentação responde aos eventos sincronizados sem decidir dano ou colisão.
- A bancada Quincy auditada desenha uma aproximação própria e não carrega os assets GeckoLib exportados; este storyboard não trata aquela bancada como validação dos assets.

## Convenções para todas as pranchas

Usar coordenadas do ator: frente `-Z`, costas `+Z`; declarar a mão principal e espelhar o conjunto corporal inteiro para destros/canhotos. Arco, corda e flecha permanecem presos à empunhadura e à mão correspondentes. Pés apoiados; quadril acompanha o tronco sem giro gratuito. As quatro estrelas permanecem distinguíveis e não cruzam a silhueta do jogador ou escondem o arco.

Cada quadro registra: tick ou evento que o dispara; vista de estudo (frente/perfil); postura de pés, quadril, tronco e braços; mão e ponto de empunhadura; orientação da ponta do arco/flecha; direção/alvo; asas e estrelas; projétil/impacto; VFX; áudio somente se houver arquivo/entrada fornecido pelo usuário; e recuperação/retorno ao controle. Eventos sem duração definida ficam como `evento do servidor — duração não especificada`, sem inventar ticks.

Quadros definem intenção e continuidade. A autoria posterior deve acrescentar intermediários e comparar transições; marcos isolados não bastam para provar fluidez.

## H — disparo de precisão com auxiliares

**Papel:** linha precisa do kit Vollständig. Preservar o disparo principal e quatro auxiliares já aprovados.

| Marco | Evento/tempo | Ação visual |
|---|---|---|
| H0 | Entrada aceita pelo servidor | Jogador mantém mira. Arco aponta ao alvo; mão de suporte ancora o centro e mão principal controla a corda. Asas entram em tensão discreta; quatro estrelas ficam separadas atrás do corpo. |
| H1 | Preparação correspondente ao evento real | Para disparo comum do arco, mostrar a tração aprovada de até 12 ticks. Para slot H, não inserir espera: usar apenas antecipação visual curta alinhada ao evento do servidor, pois H atualmente é despachado diretamente. A asa não segura o arco; não transladar a empunhadura pelo tórax. |
| H2 | Disparo/técnica confirmado pelo servidor | O projétil central segue o vetor autorizado. As quatro estrelas fazem uma contração curta e legível; não viram mãos ou armas equipadas. |
| H3 | Entidades auxiliares confirmadas | Quatro rastros discretos se separam da central sem prometer colisão, dano ou aquisição de alvo que o servidor não confirmou. |
| H4 | Fim da janela visual do disparo | Mão da corda relaxa e ombro acompanha; arco mantém orientação plausível. Asas e estrelas assentam em repouso vivo. Controle não fica preso. |

**Vista de QA:** perfil para empunhadura/corda e frente para confirmar silhueta, central e quatro auxiliares. Capturar trajetória contínua e impacto separadamente; captura de pose não prova cinco colisões.

## N — arsenal alternado das asas

**Papel:** cobertura sustentada por disparos que mostram a manifestação participando. Preservar 12 projéteis/3 ticks e a regra sem salvas recursivas.

| Marco | Evento/tempo | Ação visual |
|---|---|---|
| N0 | Entrada aceita | Arco e mira do jogador ficam livres e visíveis. As duas asas abrem com defasagem curta; estrelas ficam em dois pares reconhecíveis. |
| N1 | Primeira emissão | Asa e par de estrelas correspondente alinham-se ao mesmo setor frontal `-Z`. Pulso curto marca a origem; jogador não gira 180°. |
| N2 | Emissões alternadas, a cada 3 ticks | A origem alterna entre os lados, mantendo alvo/setor conforme o despacho real. Cada emissão tem pulso breve; não desenhar 12 feixes simultâneos nem ampliar quantidade. |
| N3 | Última emissão | Última estrela retorna à órbita atrás do jogador. Asas reduzem tensão progressivamente, sem corte instantâneo para pose neutra. |
| N4 | Recuperação | Arco fica na mão e orientação coerentes com a próxima ação. Efeitos residuais não escondem mira nem bloqueiam o HUD. |

**Vista de QA:** frente oblíqua para legibilidade das origens esquerda/direita; perfil para verificar que ambas apontam ao mesmo setor. Mostrar sequência com timestamps; não inferir acerto pela emissão.

## B — Hirenkyaku de três cargas

**Papel:** reposicionamento em três compromissos, preservando movimento e colisão que o serviço valida. Não transformar a técnica em voo contínuo.

| Marco | Evento/tempo | Ação visual |
|---|---|---|
| B0 | Primeira carga aceita | Centro de gravidade baixa; pé de apoio e ombro indicam a direção. Reishi comprime junto ao corpo, sem asa levantar o jogador. |
| B1 | Primeiro deslocamento confirmado | Corpo inclina ao vetor executado pelo servidor. Rastro curto fica no ponto de saída; não é entidade autônoma. |
| B2 | Janela entre cargas | Pose recupera o bastante para nova decisão. Arco permanece preso à mão; estrelas acompanham sem ultrapassar o jogador. |
| B3 | Segunda carga confirmada | Impulso acompanha o vetor realmente validado, sem direção fixa do storyboard. Rastro anterior dissipa. |
| B4 | Terceira carga confirmada | Último impulso preserva a mesma regra de colisão. Não adicionar salto, teleporte ou alcance. |
| B5 | Cargas esgotadas/cancelamento | Recuperação compatível com o estado real do jogador; nenhum afterimage vira hitbox. Controle e câmera continuam do jogador. |

**Vista de QA:** perfil lateral e câmera de observador para conferir origem/destino sem mover a câmera de jogo; registrar três posições e obstáculos. Se o serviço não expuser uma fase, marcar a lacuna de contrato em vez de inventá-la.

## C — convergência localizada

**Papel:** conclusão de área preparada com assinatura da manifestação. Preservar compromisso de 30 ticks, ponto, área, dano e elegibilidade atuais. Destruição de blocos não está aprovada.

| Marco | Evento/tempo | Ação visual |
|---|---|---|
| C0 | Ponto marcado/compromisso iniciado | Ponto autorizado pelo servidor recebe sinal discreto. Jogador continua legível como origem. Asas abrem sem cobrir a área. |
| C1 | Preparação durante os 30 ticks | Quatro estrelas orientam-se ao ponto e asas tensionam em pulsos espaçados. Linhas são telegráficas, não colisores. O ponto não acompanha o jogador se o servidor o fixou. |
| C2 | Fim do compromisso confirmado | Quatro linhas convergem de posições distintas da manifestação. Arco continua visível e não dispara salva adicional. |
| C3 | Aplicação do efeito confirmada | Conclusão localizada no ponto mostra o momento do efeito de área. VFX não oculta alvos atingidos nem sugere quebra de terreno. |
| C4 | Dissipação/interruptão | Linhas recolhem ou dissipam segundo o resultado informado. Se o servidor cancelar, não mostrar explosão/dano como conclusão. |
| C5 | Retorno | Estrelas retomam a órbita e asas assentam. Sem pose presa, dano tardio ou destruição visual não realizada. |

**Vistas de QA:** plano geral para relação jogador–ponto–área; perfil para trajetória e obstrução. O quadro não define raio, tipo de alvo nem nova regra de terreno.

## Pendências para revisão humana

1. Confirmar se a linguagem proposta distingue H (precisão), N (cobertura), B (reposicionamento) e C (área preparada) sem trocar mecânicas.
2. Decidir se algum storyboard precisa de outro marco antes de virar ficha técnica. Nenhum valor precisa ser aprovado para revisar a linguagem.
3. Depois, alinhar eventos com o contrato de estado/castId/fase/aim do plano. Não implementar integração de gameplay nesta etapa documental.

## Próximo passo após revisão

Converter sequências aprovadas em fichas técnicas, reconciliando eventos reais do servidor e contratos M07. Depois especificar controlador/animatic e prova vertical H/N/B/C no Forge. Revisar o rig A no runtime GeckoLib real antes de usar suas capturas como referência de animação de produção.
