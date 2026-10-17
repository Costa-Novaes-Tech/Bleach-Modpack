# Ciclo 56 — Repertório elemental, pose final e combos

Data: 17/10/2026. Branch `Feature-Poderes-bankais`, base `30c7e28`, Git limpo ao iniciar. Usuário aprovou a primeira fatia e pediu conclusão do repertório Ryūjin/Hyōrinmaru, refinamento cinematográfico, dragão Shikai com +5 blocos, Bankai maior que o Ender Dragon e combos próprios. Contrato anterior: ciclos 54/55, M05/M06, identidade/vínculo/maestria e contrato de novos poderes. Sem integração do Zaraki, fetch ou push.

## Entrega

Executores, metadata, HUD pt_br/en_us, gestos PlayerAnimator e efeitos foram integrados. A matriz abaixo substitui a proposta do ciclo 54. Os números são adaptação de gameplay, não medidas canônicas. Slots H/N/B/C respeitam vínculo, forma, maestria, reiatsu, cooldown e política compartilhada de aliados/PvP/NPCs.

### Ryūjin Jakka

| Forma | Tecla | Técnica e comportamento | Reiatsu | Recarga |
|---|---|---|---:|---:|
| Selada | H | Corte frontal físico, alcance 6; impacto após 6 ticks | 8 | 3 s |
| Selada | N | Varredura física mais larga, alcance 6; impacto após 6 ticks | 8 | 4 s |
| Selada | B/C | Sem técnica selada | — | — |
| Shikai | H | Rajada de Chamas: frente estreita, avança pelo chão até 8 blocos nos ticks 6–14; dano base 6 uma vez/alvo, para em parede | 15 | 4 s |
| Shikai | N | Leque de Fogo: varredura angular de −70° a +70° ao longo dos ticks 6–20, alcance 14; dano base 8 uma vez/alvo, exige visibilidade | 25 | 5 s |
| Shikai | B | Tornado de Fogo: anel acompanha o jogador por 10 s; centro livre de 3, raio externo 9, altura 10; executor anterior preservado | 40 | 15 s |
| Shikai | C | Jōkaku Enjō: região marcada até 10 à frente, raio 6 fixo; cúpula sobe em 20 ticks e dura 6 s, dano base 4/s após subir | 35 | 15 s |
| Bankai | H | Investida de Chamas: dash anterior de até 20 blocos com colisão/dano no percurso | 20 | 5 s |
| Bankai | N | Higashi: corte ofensivo concentrado de alcance 4; dano base 16 no tick 12, sem proteção adicional | 25 | 8 s |
| Bankai | B | Minami: até seis esqueletos carbonizados temporários, surgem no tick 16, duração total 9 s; perseguem hostis/jogadores elegíveis em 16 blocos | 40 | 15 s |
| Bankai | C | Corte de Vapor: preparação de 18 ticks e execução do corte anterior de 100 blocos, preservando destruição de terreno e blocos protegidos | 45 | 60 s |

A Rajada resolve uma frente estreita em movimento; o Leque resolve setores em sequência. Não são mais o mesmo cone com distâncias diferentes. A cúpula é uma ameaça de área, não uma parede sólida ou prisão inevitável. Minami usa Wither Skeleton vanilla como primeiro modelo de cinzas, sem AI vanilla, sem armas/drop/XP; deslocamento explícito com colisão, golpe base 3 e intervalo compartilhado de 20 ticks por alvo, independentemente dos seis soldados. Não concede maestria/quest por bater ou matar as próprias invocações. Terreno obstruído pode reduzir o número efetivamente invocado. Cancelamento/expiração descarta soldados; marca persistente rejeita órfãos ao carregar o mundo.

Ignição como botão, muralha reta e morcegos deixam a entrada do kit; seus auxiliares legados ainda existem internamente. Nishi não foi implementada. Higashi é ofensiva, como solicitado. Passivas de queimadura 4/8 s nas formas liberadas e bônus comuns de forma continuam; golpes selados não ganham fogo. Dano base passa pelo pipeline atual de atributos/forma/defesa, não é necessariamente a perda final de vida.

### Hyōrinmaru

| Forma | Tecla | Técnica e comportamento | Reiatsu | Recarga |
|---|---|---|---:|---:|
| Selada | H/N | Cortes físicos frontal/largo de alcance 6, impacto após 6 ticks | 8/8 | 5/8 s |
| Selada | B | Sem técnica selada; antiga Armadura ativa retirada da entrada | — | — |
| Selada | C | Choque Glacial: seleciona inimigo à frente até 5, frio e empurrão após 6 ticks | 18 | 15 s |
| Shikai | H | Dragão de Gelo: alcance da cabeça **15** (antes 10), mordida com expansão de caixa **0,7**; dano base 8 | 15 | 5 s |
| Shikai | N | Corrente Crescente: seleciona um alvo frontal visível até 12; dano base 3 no tick 8, frio e puxão breve; linha luminosa representa a corrente | 20 | 8 s |
| Shikai | B | Choque Glacial, mesma função de duelo da selada | 18 | 12 s |
| Shikai | C | Zona Glacial fixa, raio 10, duração 4 s; executor ambiental anterior preservado | 30 | 15 s |
| Bankai | H | Três dragões gigantes em 0°/±25°, alcance da cabeça 24, mordida expandida em 2,3; dano base 12, uma vez/alvo/conjunto | 20 | 5 s |
| Bankai | N | Ryūsenka: estocada avança até 6 blocos entre ticks 6–11, respeita colisão; dano base 14, frio e cruz de gelo no contato | 25 | 8 s |
| Bankai | B | Sennen Hyōrō: região fixa até 12 à frente, raio 5; doze pilares sobem e convergem, frio/puxão nos ticks 40–80 e dano base 6 nos ticks 50/80 | 30 | 12 s |
| Bankai | C | Zona Glacial ampliada, raio 30, duração 6 s. Entre **3 e 6 s**, C novamente consome o campo e inicia Hyōten Hyakkasō | 35 | 15 s |

Hyakkasō tem gesto de 24 ticks e impacto no tick 20: flores de gelo ao redor do campo original, dano base 10 e frio em todos os alvos elegíveis/visíveis dentro do raio 30. Não acompanha jogador/mira, não cobra reiatsu/recarga novamente, não dispara sozinha ao expirar. C cedo mantém o campo e informa espera; depois de 6 s não existe continuação. HUD muda o nome enquanto disponível. Não há execução garantida nem Bankai madura nesta entrega.

Sennen substitui Barreira de Geada; contém por frio/puxão, sem inserir blocos permanentes. Controle novo tem intervalo de recuperação de 80 ticks por alvo; jogadores e entidades com vida máxima >=100 recebem duração reduzida (até 20 ticks/lentidão II). Esse limiar é uma heurística provisória para chefes; não equivale a catálogo de compatibilidade com todos os mods. Dragões e campo mantêm suas regras anteriores de frio. Proteção passiva 15/30%, bônus comuns, asas/cauda, neve temporária e clima continuam.

## Apresentação

- Ryūjin Bankai: convergência das chamas mantida, transição final nos ticks aproximados 38–48 para pose contida, espada baixa atravessada à frente do corpo. Uma camada de postura mantém a pose após a liberação; ataques/guarda/gestos têm prioridade e a substituem. Espelhamento para canhotos. Lâmina carbonizada e referência Yamamoto preservadas. A correspondência exata de enquadramento e ângulos precisa de playtest visual.
- Liberação Bankai recebe contração suave de FOV de até 8%, sem troca forçada de perspectiva. Opção local `cinematicReleaseFov` em `config/bleachmod-client.toml`; desativar não altera tempos/custos servidor.
- Modelo GeckoLib: cabeça/focinho mais largos, mandíbula articulada aberta (30° com oscilação), duas fileiras de dentes e presas. Escala Bankai 5,25; extensão longitudinal neutra dos cubos ~33,1 blocos, maior que a largura de referência de 16 do Ender Dragon. Isto compara silhueta/escala, não volume da hitbox vanilla. Shikai mantém escala 0,85.
- Corpo/cauda são visuais. Só a cabeça aplica dano; tamanho visual não aumenta arbitrariamente alcance nem triplica dano nos cruzamentos. Bounds visuais ampliados para evitar corte prematuro do render; três entidades por disparo continuam o limite.
- Better Combat agora cobre Asauchi, Ryūjin e Hyōrinmaru. Ryūjin abre com corte descendente, depois horizontal/estocada; Hyōrinmaru abre com estocada e alterna cortes laterais. Três movimentos por arma, multiplicador 1, alcance 3: não duplicar dano/custo de stamina por alvo do mesmo swing. Senbonzakura/Benihime não foram incluídas nesse piloto automaticamente.
- Gestos e construções próprios usam PlayerAnimator e geometria translúcida nativa; prisão de fogo, frente estreita, leque, pilares e flores possuem formas distintas. Corrente e cinzas ainda são protótipos com recursos vanilla. Não foi instalado Photon/LDLib ou criado pack final de som/texturas nesta entrega; roadmap 51 permanece.

## Estado, persistência e limpeza

`ElementalTechniqueService` controla as timelines no servidor; máximo de quatro efeitos por dono, substituição por tipo e até 64 vítimas de dano por lançamento. Golpe físico, transformação, guarda e esquiva são recusados durante o gesto; os efeitos de área podem continuar após o gesto. Test mode remove custos/cooldowns, mas não salta a preparação. Troca de item/forma, morte/dimensão e cancelamento encerram efeitos; entrada de observador recebe idade/origem/direção no snapshot público. Cliente valida limites e só desenha: não decide dano.

Rede **2.20**, schema **9**, revisão de técnicas **2**. Cliente/servidor devem usar a mesma build. Ryūjin/Hyōrinmaru agora usam chaves semânticas de técnica, mantendo as numéricas antigas arquivadas. Leque/Tornado antigos da Bankai transferem progresso para as respectivas técnicas Shikai; dragões/zonas/cortes preservados mantêm seu progresso. Habilidades novas não herdam maestria de armadura/barreira apenas por ocuparem o mesmo botão. Maestria de forma e recursos não são zerados. Cooldowns continuam transitórios conforme contrato anterior; não foi introduzida persistência deles.

## Validação e teste visual

Validação final: 101 regressões e 127 GameTests obrigatórios aprovados, incluindo parser real GeckoLib/MoLang, dimensões/mandíbula, três ramos e deduplicação, alcance Shikai, parede, combos, preparação, recursos, aliados, migração, soldados/XP/limpeza, continuação fixa e Corte de Vapor com proteção de bedrock. Logs locais: `build/cycle56-final-validation.log`; revisão final de HUD/validação de snapshot: `build/cycle56-package.log`.

Doze testes de técnicas retiradas (muralha/morcegos/armadura/barreira/onda antiga) foram substituídos por dezesseis cenários do repertório novo. Outros testes de maestria e tornado foram adaptados ao impacto atrasado e à nova forma/tecla; expectativas de metadata e duração da zona foram atualizadas. A contagem total não demonstra equivalência visual.

Pacote: `build/combat-test-pack/bleach-bettercombat-piloto.zip`, cinco JARs. Dependências idênticas ao ciclo 55: Better Combat 1.9.0 Forge, PlayerAnimator 1.0.2-rc1 Forge, Cloth Config 11.1.136 Forge, GeckoLib 4.8.4 Forge (Modrinth aC5KMoNg, mclib 20 embutido). Quem já instalou as quatro dependências só substitui Bleach; sem duplicar versões.

Playtest prioritário: pose final Ryūjin em terceira pessoa frente/lado e primeira pessoa; ataque/bloqueio saindo da postura, canhoto/slim, três cabeças gigantes próximas de parede/câmera, alcance Shikai, entradas H/N/B/C de ambas as formas, janela C da zona, dois clientes/observador tardio e FPS com vários lançadores. Não houve sessão de cliente visual nem teste multiplayer real nesta entrega. Aceite do ciclo 55 não homologa automaticamente esta revisão.
