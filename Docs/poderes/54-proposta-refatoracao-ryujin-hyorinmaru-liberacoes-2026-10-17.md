# Ciclo 54 — Pesquisa e proposta: Ryūjin, Hyōrinmaru e liberações animadas

> Entrega posterior: [ciclo 56](56-repertorio-elemental-cinematica-combos-2026-10-17.md) implementa o repertório revisado completo, combos Ryūjin/Hyōrinmaru, pose final e dragões gigantes. Consulte sua matriz vigente; descrições abaixo registram o estado histórico.

> Atualização: usuário autorizou seguir, rejeitou Nishi defensiva e pediu Rajada/Leque distintos, selo de chamas e três dragões Bankai. [Ciclo 55](55-primeira-entrega-liberacoes-dragoes-triplos-2026-10-17.md) registra a direção corrigida (Higashi ofensivo no lugar de Nishi) e entrega a primeira fatia de liberações/dragões com GeckoLib. Demais técnicas abaixo continuam roteiro de refatoração, não implementação completa.

Data: 17/10/2026. Branch `Feature-Poderes-bankais`, base limpa `ca5c365`. **Estado: proposta para discussão; sem alteração de código, assets, dependências ou build nesta etapa.** O pedido é pesquisar novo repertório, preservar capacidades selecionadas e passivas e planejar animações de técnicas/transformação com a qualidade demonstrada pela referência Zaraki. O commit da equipe ainda não está disponível.

## 1. Decisões recebidas e limites

- Ryūjin: preservar Corte de Vapor, Rajada de Chamas, Investida de Chamas, Tornado de Fogo e Leque de Fogo, permitindo melhorar sua adaptação/animação/VFX.
- Hyōrinmaru: preservar Choque Glacial, Zona Glacial, Dragão de Gelo com novo modelo e forma de ataque, e Zona Glacial ampliada. Barreira de Geada deve ser substituída.
- Preservar passivas existentes: queimadura Ryūjin 4/8 s nas liberações, proteção Hyōrinmaru 15/30%, bônus comuns de forma e comportamentos ambientais existentes. Armadura selada e Ignição são técnicas ativas, não entram automaticamente nessa preservação de passivas.
- O usuário quer animações de ativação de Shikai/Bankai com identidade da obra. Melhorias de apresentação não autorizam dano, invulnerabilidade, recompensas ou migração de progressão implícitos.
- Ler M06/M05, contrato de novos poderes e identidade/vínculo/maestria. Skill externo `C:/Users/joaop/Downloads/SKILL.md` (game-design-architect) aplicado a entradas/estados/feedback e distinção entre proposta e contrato implementável. Seus arquivos auxiliares não estão disponíveis; a documentação da equipe permanece a base.

## 2. Pesquisa e rastreabilidade

Fontes oficiais confirmam contexto/personagem; a wiki comunitária ajuda a localizar técnicas e referências do mangá/anime. Não apresentar descrição de wiki, jogo licenciado ou fanfic como texto primário da obra. Algumas páginas individuais Fandom retornaram bloqueio; trechos indexados estavam acessíveis. Não houve acesso integral aos capítulos nem nova análise quadro a quadro do episódio nesta pesquisa.

| Referência | O que sustenta / aplicação |
|---|---|
| [Anime oficial, TYBW 06 — THE FIRE](https://bleach-anime.com/en/story/?id=06) e [perfil oficial](https://bleach-anime.com/en/character/?chara=14) | Contexto da Bankai de Yamamoto; episódio para conferir direção visual. A sinopse não especifica todas as mecânicas. |
| [Higashi: Kyokujitsujin](https://bleach.fandom.com/wiki/Zanka_no_Tachi%2C_Higashi%3A_Kyokujitsujin) | Calor concentrado no fio; índice cita cap. 508 e episódio 372 (TYBW 06). Não significa dano infinito no mod. |
| [Yamamoto — repertório](https://bleach.fandom.com/wiki/Genry%C5%ABsai_Shigekuni_Yamamoto) | Nishi, Minami e Kita como referências de manto térmico, mortos de cinzas e corte final. |
| [Jōkaku Enjō](https://bleach.fandom.com/wiki/J%C5%8Dkaku_Enj%C5%8D) | Prisão envolvente de fogo; índice cita cap. 316/episódio 215. |
| [Hitsugaya — Zanpakutō](https://bleach.fandom.com/wiki/T%C5%8Dshir%C5%8D_Hitsugaya) | Dragão oriental, corrente com lâmina crescente, Ryūsenka e Hyōten Hyakkasō. Números/slots sugeridos abaixo são nossos. |
| [Hitsugaya contra Luppi](https://bleach.fandom.com/wiki/T%C5%8Dshir%C5%8D_Hitsugaya_vs._Luppi_Antenor) | Referência de pilares convergentes de Sennen Hyōrō. |
| [Gunchō Tsurara](https://bleach.fandom.com/wiki/Gunch%C5%8D_Tsurara) | Alternativa de projéteis de gelo; candidata de reserva, não instalada no kit proposto. |

Não usar guias sem procedência encontrados pela busca como base; houve resultados misturando nomes/forma madura e habilidades de fanfic. Não importar os nomes criados por outros jogos para o dragão como se fossem necessariamente o nome original do ataque no mangá.

## 3. Ryūjin — proposta de distribuição

Direção: Shikai com fogo volumoso e controle de espaço; Bankai com lâmina escura, calor concentrado e efeitos mais precisos. **Mover Leque e Tornado da Bankai para Shikai é uma recomendação nova**, não mudança já aplicada. Mantém os cinco ataques pedidos dentro de quatro slots por forma e abre espaço para técnicas reconhecíveis da Bankai.

### Selada

Recomendo fundamentos de espada/combos físicos e, no máximo, dois cortes dedicados: saque frontal e varredura. B/C podem ficar sem técnica nesta etapa; não preencher quatro slots artificialmente. Retirar fogo ativo da selada é uma proposta de maior fidelidade, ainda não aplicada. Não conceder parry universal nem novo deslocamento gratuito. Combos físicos passam pelo adaptador Better Combat antes de migrar o kit.

### Shikai

| Tecla proposta | Técnica | Apresentação e função proposta | Origem |
|---|---|---|---|
| H | Rajada de Chamas | Corte descendente lança uma frente de fogo estreita que percorre o chão; impacto acontece quando a frente alcança o alvo. Antecipação e rastro distinguem preparação de execução. | Preservada e redesenhada; nome do mod |
| N | Leque de Fogo | Corte horizontal amplo gera arco de fogo de curta/média distância. Mais largura que a Rajada, sem ser apenas a mesma onda ampliada. | Preservada; mudança de forma proposta |
| B | Tornado de Fogo | Preservar anel móvel e miolo livre; espirais contínuas, cinzas, base legível e som de circulação. Jogador gira a espada para iniciar. | Preservada; adaptação do mod, sem alegar técnica canônica homônima |
| C | Jōkaku Enjō — Prisão de Chamas | Chamas erguem-se ao redor da região marcada e fecham uma cúpula. Contenção temporária com borda perigosa e janela de reação, sem aprisionamento inevitável. | Nova, inspirada na obra |

Ignição como botão e Morcegos de Fogo saem da proposta. A antiga muralha reta é substituída pela prisão; não descrever isso como preservação da mesma geometria. Alcances/custos dos ataques preservados são referências de baseline, não automaticamente transferidos entre formas: Leque 14, Tornado externo 9/interno 3; números finais exigem contrato por função.

### Bankai — Zanka no Tachi

| Tecla proposta | Técnica | Apresentação e função proposta | Relação com a obra |
|---|---|---|---|
| H | Investida de Chamas | Avanço com espada preparada e golpe de contato. Menos cortina de fogo, mais fio incandescente, cinza e uma trilha térmica estreita. | Preservada; aproximação estética ao calor de Higashi, sem chamar o dash de técnica canônica |
| N | Nishi: Zanjitsu Gokui — Manto Térmico | Ativação curta: contorno quente e pressão próxima ao corpo. Mitigação limitada e retaliação de contato, sem imunidade total. | Nova adaptação de Nishi; potência e duração pendentes |
| B | Minami — Exército de Cinzas | Espada toca o chão; figuras carbonizadas emergem em sequência e avançam sobre hostis. Proposta de teto: seis manifestações por lançador, prazo curto, sem drops/XP de morte e limpeza por dono. | Nova adaptação de Minami; quantidade é limite de gameplay |
| C | Corte de Vapor — versão Kita | Preservar ataque culminante; preparação pesada, corte nítido e sequência de impacto no percurso. Branco quente/cinza em vez de uma nuvem opaca escondendo o alvo. | Preservada, inspirada em Kita: Tenchi Kaijin; “Corte de Vapor” permanece nome do mod |

Higashi orienta lâmina/contato/gesto físico da Bankai; não acrescentar por inferência uma nova passiva de apagar entidades, ignorar defesa ou destruir blocos. Os cinco ataques selecionados estão preservados como conceitos, mas timing, geometria e distribuição por forma mudam nesta proposta. A destruição de terreno do Corte atual precisa de decisão explícita ao fechar seu contrato: não remover, ampliar ou reinterpretar silenciosamente esse comportamento.

## 4. Hyōrinmaru — proposta de distribuição

Direção: dragão serpentino oriental, corrente crescente na Shikai e gelo com volume/silhueta, articulado por movimento. A Bankai cobre mão/braço com cabeça de dragão, garras, asas, cauda e flores de gelo. Forma madura/completa fica fora desta primeira revisão; pétalas não devem ser descritas incorretamente como prova de que a Bankai necessariamente acaba ao sumirem.

### Selada

Recomendo dois cortes físicos opcionais e **Choque Glacial mantido em C**, conforme escolha do usuário. O frio na selada será uma exceção assumida da adaptação, não declarado regra canônica. A Armadura de Gelo ativa deixa o repertório proposto; defesa com X continua disponível. Choque também pode integrar Shikai B, conservando a capacidade de duelo curta.

### Shikai

| Tecla proposta | Técnica | Apresentação e função proposta | Origem |
|---|---|---|---|
| H | Dragão de Gelo | Modelo autoral com cabeça, mandíbula, corpo segmentado e cauda; emerge da ponta e percorre uma trajetória curva legível. Colisão e dano acompanham a cabeça/corpo ativo, com um acerto por alvo por disparo. | Preservada; substituir Phantom e dano instantâneo anterior |
| N | Corrente Crescente | Corrente visível sai do pomo; crescente gira, alcança um inimigo e aplica contenção curta por gelo. Não confundir com o antigo leque chamado Corrente de Gelo. | Nova adaptação do acessório Shikai |
| B | Choque Glacial | Golpe curto de espada/arranque de frio, com reação de gelo e empurrão no alvo à frente. Gesto breve, impacto localizado. | Preservada, disponível também na selada |
| C | Zona Glacial | Preservar campo fixo de raio 10 como baseline; frio se espalha pelo solo em etapas, com crostas finas e neve entre elas. | Preservada; adaptação do controle atmosférico/umidade |

### Bankai — Daiguren Hyōrinmaru

| Tecla proposta | Técnica | Apresentação e função proposta | Origem |
|---|---|---|---|
| H | Dragão de Gelo ampliado | Preservar baseline de alcance 24; mesmo modelo articulado, maior volume e movimento definido. Evitar homing perfeito; voo do dragão não dá voo gratuito ao jogador. | Preservada e redesenhada |
| N | Ryūsenka | Avanço curto com estocada; impacto forma estrutura de gelo semelhante a cruz/botão floral, seguida de fragmentação. | Nova, inspirada na obra |
| B | Sennen Hyōrō | Pilares surgem ao redor da região marcada e convergem para fechar uma prisão por tempo limitado. Aviso no piso, cobertura e regras de fuga/controle explícitas. | Nova; substitui a Barreira de Geada |
| C | Zona Glacial ampliada / Hyōten Hyakkasō | Primeiro uso preserva zona fixa de raio 30; proposta de segundo uso após preparação faz flores de gelo crescerem nos alvos elegíveis presentes e resolve um impacto final. | Zona preservada; complemento novo inspirado na obra |

A continuação C é recomendação, não reativação já existente. Baseline para discussão: preparar por 3 s, janela de confirmação de 3 a 6 s; se não confirmar, expirar sem finalizador. Forma, raio, custos e recarga do primeiro uso não devem mudar por mero reaproveitamento desse padrão. Flowers não significam execução garantida nem morte na centésima flor. Não aplicar dano/controle a aliado ou criar prisão permanente em PvP; bosses precisam de política de controle reduzido.

Gunchō Tsurara (salva de projéteis) e Hyōryū Senbi (corte de gelo) são alternativas de repertório, não novos botões cumulativos nesta proposta. Preferir definir bem quatro técnicas e suas continuações antes de ampliar hotbar.

## 5. Liberações: animação de verdade, sincronizada

O vídeo Zaraki analisado no ciclo 51 sustenta a referência de apresentação (pose, pausa, aura, ativação), mas não revela a API usada pelo colega. Não copiar assets nem presumir que seu executor já foi integrado. Criar um serviço comum de liberação com presets por identidade, compatível com reaproveitamento posterior do commit.

| Forma | Direção de animação recomendada |
|---|---|
| Ryūjin Shikai | Empunhadura firme → espada sobe/aponta → chama percorre a lâmina a partir da guarda → expansão curta → postura pronta. |
| Ryūjin Bankai | Espada abaixa → chamas se recolhem à lâmina → breve pausa e cinzas suspensas → lâmina carbonizada/fio quente → pulso de calor e postura contida. |
| Hyōrinmaru Shikai | Espada descreve arco → névoa condensa → corrente/crescente aparecem → silhueta do dragão cruza a liberação → postura pronta. |
| Hyōrinmaru Bankai | Espada e braço alinham → cabeça de dragão envolve a mão → gelo percorre corpo/cauda → asas se abrem articuladas → flores de gelo e pulso de neve → postura pronta. |

Baselines exclusivamente de apresentação: Shikai 24 ticks (1,2 s), Bankai 60 ticks (3 s). Devem ser conciliados com o tempo de transformação já existente, nunca acrescentar um segundo carregamento sem decisão. Não usar uma explosão genérica idêntica para todas as armas.

Contrato para implementação: `preparação → liberação/commit → recuperação → livre`. Servidor valida início, custo, desbloqueio, identidade, forma e tempo; forma muda uma vez no ponto de commit. Cliente recebe ação/identidade/dono/tempo inicial e apresenta bones/VFX/sons. Late tracking recebe estado e tempo restante. Entrada duplicada é rejeitada. Desconexão/morte/dimensão/troca de arma limpam runtime; se interromper antes do commit, manter forma anterior, com política de custo explicitada no contrato final. Se interromper depois, não cobrar nem transformar novamente.

Recomendação de inputs: sem ataques/técnicas/guarda/esquiva simultâneos durante preparação de liberação; câmera pode girar, deslocamento reduzido a definir. Não há invulnerabilidade automática nem interrupção por todo dano comum. Câmera cinematográfica local é opcional; opção reduzida usa a mesma duração/condição e mantém HUD legível. Cancelar restaura perspectiva/FOV originais. Observadores veem o corpo/efeito, não têm câmera sequestrada. Keyframe cosmético nunca é autoridade de dano.

## 6. Ferramentas e responsabilidades

- **Better Combat 1.9.0 já instalado:** combos físicos, movimentos de corte/estocada e perfis por arma. Presets locais como `one_handed_slash_horizontal_right`, `one_handed_stab`, `two_handed_slash_vertical_right` são pontos de partida, não o repertório final. Animação especial não pode gerar ataque físico extra junto do executor da técnica. [Fonte 1.20.1 e guia de animações próprias](https://github.com/ZsoltMolnarrr/BetterCombat/tree/1.20.1).
- **PlayerAnimator já instalado:** poses/gestos próprios de liberação e técnicas, arbitrados com guarda/Better Combat. Ele não fornece automaticamente uma cinemática Bankai. O guia Better Combat dessa linha usa rig/exportador Blender; verificar exportação efetiva antes de prometer que JSON de qualquer editor é intercambiável.
- **Blockbench + GeckoLib 4, planejados:** modelo autoral de dragão e componentes articulados. Primeiro um piloto pequeno no Forge 1.20.1; compatibilidade do dragão não comprova automaticamente integração com camada de jogador/armadura. Não migrar corpo do jogador para GeckoLib por inferência. [Instalação GeckoLib 4](https://github.com/bernie-g/geckolib/wiki/Installation-%28Geckolib4%29).
- **Photon 1.x + LDLib, planejados:** trilhas, chamas em camadas, névoa e explosões. [README do ramo correto](https://raw.githubusercontent.com/Low-Drag-MC/Photon/1.20.1/README.md) e [propriedades](https://raw.githubusercontent.com/Low-Drag-MC/Photon/1.20.1/gradle.properties) indicam editor de VFX/trilhas e ramo Forge/Fabric 1.20.1; isso não é validação de runtime no Bleach. Conferir versões/dependências na bancada.
- **Audacity / editor de textura / spark:** áudio autoral em camadas, superfícies/partículas próprias e medição. Guardar fontes editáveis e origem dos assets. Efeitos orçamentados por técnica/audiência, emissões reduzidas à distância e opção de intensidade.

Qualidade: gesto coincide com direção do ataque; faixa de impacto legível; gelo tem cabeça/corpo/placas reconhecíveis; fogo distingue lâmina, arco, área e calor. Mais partículas não substituem geometria/articulação. Modelo/VFX não aplicam dano por conta própria.

## 7. Ordem de trabalho e fechamento pendente

Manter roadmap 51: concluir validação visual/multiplayer da base → integrar autoria/GeckoLib e bancada VFX → usar liberação Hyōrinmaru + dragão como primeira fatia → refatorar Hyōrinmaru → Ryūjin → Ishida. Serviço comum de liberações entra antes dos novos kits e será reutilizado neles. Mentor/Proficiências aguardam equipe; Aizen adiado/Gin removido; Zaraki conciliado quando disponível.

Antes de modificar o kit: fechar distribuição por forma (especialmente Leque/Tornado), comportamento da selada e continuações; completar timings/custos/alcance/controle/interrupções/terreno/prática. Técnicas preservadas devem manter IDs semânticos quando possível. Técnicas realmente substituídas recebem IDs próprios e migração explícita; maestria de forma não deve zerar, treino/cooldown por slot precisam ser reconciliados antes de trocar slots.

Validação desta etapa: código/documentação e fontes consultados, pesquisa registrada, diff revisado. Nenhuma suíte executada novamente porque só documentos mudaram. Última build permanece ciclo 53, rede 2.18/schema 8, 99 regressões/118 GameTests; isso não testa as capacidades propostas aqui.
