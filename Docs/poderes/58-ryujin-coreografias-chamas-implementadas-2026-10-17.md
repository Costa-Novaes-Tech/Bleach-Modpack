# Ciclo 58 — Ryūjin: coreografias e chamas dos estudos aprovados

Data: 17/10/2026. Branch `Feature-Poderes-bankais`, entrada limpa em `6b59ef3`. Usuário aprovou os estudos do [ciclo 57](57-prototipos-correcao-visual-ryujin-2026-10-17.md) e pediu implementação de todos os movimentos/efeitos, buscando fidelidade às imagens. Esta entrega aplica a revisão ao mod; a comparação visual final no Minecraft continua sendo parte do playtest.

## O que mudou

`RyujinChoreography` contém curvas próprias de preparação, ação e recuperação para os dez ataques, além do selo da Bankai. Braço principal, mão livre quando vazia, tronco, pernas e item recebem transformações pelo PlayerAnimator. Primeira pessoa recebe movimentos de mão correspondentes via RenderHandEvent. As curvas usam ticks reais das técnicas, não a reprodução alongada dos GIFs. Nenhuma curva desloca a entidade no servidor.

A antiga camada permanente `RyujinBankaiReadyPose` foi removida, inclusive seu registro. O selo fica exclusivamente na timeline de 60 ticks: chamas convergem até o commit no tick 40; a espada se acomoda na lateral externa nos ticks 38–47, mantém a pose nos ticks 47–53 e retorna à animação normal até o tick 60. Orientação lateral invertida em relação à versão rejeitada; espelhada para canhotos. O cálculo da direção do fio considera a rotação dos elementos/modelo da katana, display de terceira pessoa e transformações vanilla de item na mão, em vez de inferir a direção apenas pelo yaw do braço. O FOV opcional anterior continua. Cancelamento/fim não mantém camada ociosa nem uma postura especial permanente.

`RyujinActionVisuals` emite partículas nativas de chama, chama pequena, fumaça, cinza e faíscas. Os ramos Ryūjin de superfícies translúcidas em `ElementalActionVisuals` foram retirados; as construções de gelo permanecem. Não foi preciso acrescentar dependências, importar uma imagem gerada como textura ou instalar Photon/LDLib.

| Estudo aprovado | Aplicação no runtime |
|---|---|
| Selada H | Preparação superior, corte descendente no tick 6, follow-through e recuperação até 16; sem fogo. |
| Selada N | Rotação lateral de quadril e ombro, varredura física no tick 6 e recuperação até 16; sem fogo. |
| Shikai H — Rajada | Descida de espada própria; frente estreita de partículas acompanha os ticks 6–14 e percorre os mesmos 8 blocos. Fumaça fica nas laterais. |
| Shikai N — Leque | Braço e tronco percorrem a varredura da direita para a esquerda; chamas distribuídas pelo setor em movimento dos ticks 6–20, arco aberto de 140°/alcance 14. Sem fita ou disco translúcido. |
| Shikai B — Tornado | Gesto de giro em 18 ticks com mudança de apoio das pernas; hélices nativas/anel móvel do executor anterior preservados. |
| Shikai C — Jōkaku | Preparação e gesto de apontamento; línguas de fogo em camadas/nervuras sobem na região fixa, com fechamento superior e vãos de leitura, substituindo a cúpula de quads. |
| Bankai H — Investida | Inclinação do tronco, espada à frente e apoios alternados durante os 16 ticks. Fogo e brasas acompanham o deslocamento existente. Retirado startAutoSpinAttack vanilla: a pose de tridente se sobrepunha à espada e podia introduzir ataque vanilla além do contato próprio. A Investida continua usando o movimento/colisão/dano do executor customizado, sem somar outro dash. |
| Bankai N — Higashi | Preparação na lateral do quadril com ombro recolhido, giro curto do tronco e golpe seco no tick 12; follow-through até 16 e retorno até 22. Fio luminoso quente com chamas pequenas/faíscas, arco compacto de fogo e pulso/sons no contato confirmado pelo servidor. Não foi transformada em projétil ou dash. |
| Bankai B — Minami | Gesto pesado até tocar o chão no tick 16; seis pontos de fogo baixo/cinzas e emergência visual escalonada dos esqueletos carbonizados. Mantidos até seis Wither Skeletons vanilla, sem novo modelo final. A subida é transformação de render, sem mover hitboxes para dentro do chão ou atrasar a AI customizada. |
| Bankai C — Corte de Vapor | Preparação alta sustentada, corte no tick 18, peso de tronco/pernas e recuperação até 32. Núcleo de chamas alaranjadas/faíscas substitui o fogo azul; a fumaça passa às laterais ao longo do caminho. Explosão/fumaça do impacto terminal continuam. |

## Limites e sincronização

- Dano, custos, recargas, maestria, slots, passivas e alcance continuam os do ciclo 56. Corte de Vapor mantém destruição/proteção de blocos. Não há bônus de dano por partícula nem pelo passo visual da Higashi. Remoção do auto-spin deixa a Investida na postura/collision box normal do jogador, em lugar da pose vanilla de Riptide; o teste confirma que não existe ataque giratório vanilla paralelo.
- Emissão nova no cliente por tick, não por frame, com teto global de 640 partículas/tick e teto de 128 por emissor/efeito. Opções mínima/reduzida e distância acima de 32 blocos reduzem amostras; audiência limitada a 64 blocos do dono. Esse orçamento não inclui emissões autoritativas anteriores do Tornado/Investida/Vapor nem o pulso confirmado da Higashi. Não é resultado de benchmark de FPS.
- Prioridade para o lançador local dentro do orçamento. Uma emissão de liberação por dono/tick; pausa não continua criando partículas. Limpeza por troca de mundo/desconexão e prazos dos snapshots. Curvas terminam com peso zero e não substituem animação normal fora da ação. Ataques básicos Better Combat/guarda retomam seu controle ao terminar o gesto.
- Minami acrescenta até seis IDs de entidades ao snapshot público após o surgimento e para observadores tardios. Cliente limita/valida a lista e só aplica emergência a esses Wither Skeletons; esqueletos vanilla fora dessa lista não são alterados. Render Pre/Post aplica e desfaz a translação visual; a posição/dano reais permanecem no servidor.
- **Rede 2.21**, schema persistente **9**, revisão de técnicas **2** mantida. Mesmo JAR nos dois lados. Nenhuma migração adicional de personagem nem reset de progresso nesta revisão.
- **Hyōrinmaru preservada:** executores, custos, efeitos, modelos e animações anteriores não foram refeitos. O adaptador compartilhado encaminha apenas as ações Ryūjin às curvas novas; os ramos de gelo continuam com sua implementação anterior.

## Validação e entrega

`check build prepareCombatTestPack runGameTestServer` aprovados: **104 regressões e 128 GameTests obrigatórios**, log local `build/cycle58-validation.log`. Três regressões novas verificam direção externa da katana nas duas mãos/encerramento, curvas finitas e contínuas com retorno ao movimento base, e preparação da Higashi distinta da Rajada. Teste Minami existente foi ampliado para confirmar que os seis IDs publicados são dos soldados daquele dono. Novo GameTest garante Investida customizada sem auto-spin e preserva custo/recarga/timeline. A suíte mantém cenários de aliados, gelo, campo fixo, dano, terreno, custo e cancelamento. Nenhum teste de gameplay foi removido.

Pacote atualizado: `build/combat-test-pack/bleach-bettercombat-piloto.zip`, cinco JARs. ZIP e JAR inspecionados: classes novas presentes, classe da pose permanente ausente. Dependências mantidas: Better Combat 1.9.0 Forge, PlayerAnimator 1.0.2-rc1 Forge, Cloth Config 11.1.136 Forge e GeckoLib 4.8.4 Forge (Modrinth aC5KMoNg, mclib 20 embutido). Quem já usa esse conjunto deve substituir apenas Bleach; cliente e servidor precisam atualizar juntos. Sem push ou merge.

Conferência de cliente ainda necessária: pose externa de frente/lado/costas, canhoto/slim, mão secundária ocupada, retorno à postura normal e bloqueio/combos após liberar; todos os H/N/B/C; visibilidade do fio e pulso Higashi; emergência Minami para dono/observador tardio; fumaça não escondendo o alvo e FPS com múltiplos lançadores/opção mínima de partículas. Não houve captura/renderização do Minecraft nesta execução nem teste com dois clientes reais. As imagens do ciclo 57 são referência artística, não evidência de igualdade pixel a pixel do runtime. Roadmap 51 e futura integração Zaraki permanecem.
