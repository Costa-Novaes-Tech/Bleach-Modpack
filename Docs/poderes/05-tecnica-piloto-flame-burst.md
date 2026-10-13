> **Estado vigente — ciclo 31, 13/10/2026:** stamina de corrida com pausa, recuperação, exaustão e HUD. Valores provisórios; ciclo 30 segue em teste visual. [Contrato, arquivos, lógica e roteiro](31-stamina-corrida-2026-10-13.md). Schema 6; protocolo 2.8. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 30, 12/10/2026:** primeira fatia de Combat State automático, indicador no HUD e limpeza transitória, sem mudar regeneração/stamina/atributos. [Lógica, arquivos, timeout provisório e testes](30-estado-combate-2026-10-12.md). Protocolo 2.7; schema 5 preservado. Combat Mode/hotbar seguem futuros; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 29, 12/10/2026:** ciclos anteriores homologados pelo usuário. Definições comuns dos 24 slots/formas de Ryujin e Hyōrinmaru consultadas por execução, acesso e HUD; valores preservados. [Arquivos, lógica e validação](29-definicoes-compartilhadas-2026-10-12.md) · [Contrato para novos poderes](contrato-criacao-novos-poderes.md). Registros anteriores abaixo são históricos; nova feature será discutida antes de outro kit.

> **Estado vigente — ciclo 28, 11/10/2026:** feedback de maestria sem arredondar limiares, avisos de novas liberações pelo treino, distinção de descoberta/compra/disponibilidade e indicação de treino suspenso em modo teste. [Arquivos, lógica e roteiros de visuais/maestria](28-feedback-maestria-roteiro-2026-10-11.md). Ganhos e requisitos preservados; ciclos abaixo são históricos.

> **Estado vigente — ciclo 27, 11/10/2026:** cortes, barreira, Zona Glacial, impacto de controle e trilha do dragão da Hyōrinmaru usam o emissor comum, mantendo aparência e combate. [Arquivos, lógica, valores e testes](27-migracao-slots-gelo-2026-10-11.md). Clima e terreno conservam sua lógica; registros abaixo são históricos.

> **Estado vigente — ciclo 26, 11/10/2026:** aura e liberação da Hyōrinmaru integradas ao emissor comum com audiência próxima e orçamentos explícitos, preservando a aparência aprovada. Bancada continua privada. [Arquivos, lógica, limites e roteiro](26-integracao-gelo-emissor-comum-2026-10-11.md). Os ciclos anteriores abaixo são históricos; Aizen e narrativa permanecem futuros.

> **Estado vigente — ciclo 25, 11/10/2026:** biblioteca comum com paletas fire/ice/reiatsu e formas aura/cut/beam/release; camadas compartilham orçamento. Bancada anterior preservada; nenhuma mudança nos kits homologados. [API, arquivos, limites e comparação](25-biblioteca-paletas-camadas-2026-10-11.md). Escolha visual e integração em produção são próximos gates; Aizen e narrativa continuam futuros.

> **Estado vigente — ciclo 24, 11/10/2026:** Asauchi/maestria do ciclo 23 homologadas pelo usuário. Catálogo vanilla e bancada administrativa de seis presets, três intensidades e duração limitada, sem alterações nos kits ou terreno. [Recursos, arquivos, lógica, limites e roteiro](24-catalogo-vanilla-bancada-visual-2026-10-11.md). Aizen e aquisição narrativa continuam futuros.

> **Estado vigente — ciclo 23, 07/10/2026:** ciclo 22 homologado pelo usuário. Asauchi independente com selada/Shikai/Bankai, mantendo todas as texturas/modelos e apenas bônus de dano nas liberações; treino válido por forma/técnica e desbloqueios graduais dos kits. Personagens antigos preservam repertório. Aizen permanece futuro. [Contrato operacional, lógica, arquivos, migração e roteiro de testes](23-asauchi-maestria-desbloqueios-2026-10-07.md). As seções abaixo registram os ciclos anteriores; propostas de história continuam abertas.

> **Estado vigente — ciclo 22, 07/10/2026:** restauração do clima agora sincroniza explicitamente os clientes e `/weather clear` corrige chuva visual presa; Ice Dragon Bankai com modelo maior e alcance 24. [Causa, lógica, arquivos, testes e roteiro final](22-clima-sincronizado-dragon-bankai-2026-10-07.md). Demais pontos homologados pelo usuário; encerramento definitivo aguarda este teste visual. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 21, 07/10/2026:** tornado centrado e móvel com miolo livre de três/raio externo nove; Zona Glacial Shikai dez/Bankai trinta com neve temporária no interior; clima com restauração e prioridade para comando manual; passivas de fogo Ryujin e resistência Hyōrinmaru. [Passo a passo, lógica, arquivos, valores e testes](21-correcao-tornado-zona-clima-passivas-2026-10-07.md). Os ciclos abaixo são históricos; esta revisão substitui o deslocamento frontal do tornado do ciclo 20.

> **Estado vigente — ciclo 20, 06/10/2026:** Ryujin com onda mais densa e tornado três blocos à frente; Hyōrinmaru com quatro slots selados, armadura temporária, hipotermia, criatura de gelo, asas/cauda Bankai, tempestade com restauração e HUD de habilidades. [Lógica, arquivos, valores e homologação](20-refinamento-gelo-clima-hud-2026-10-06.md). A decisão deste ciclo substitui a antiga selada restrita a dois cortes físicos. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 19, 06/10/2026:** identidade e maestria separadas, protótipo Hyōrinmaru em quatro slots, N da Ryujin com onda de quatro blocos na selada/Shikai/Bankai, aura Bankai de fumaça e retirada do X. [Implementação, arquivos, números, migração e homologação](19-hyorinmaru-identidade-onda-fogo-2026-10-06.md). Os estados e valores anteriores abaixo são históricos; história e balanceamento final continuam em desenvolvimento.

> **Revisão vigente — fogo ambiental e inicializador, 04/10/2026:** implementados focos temporários de Bankai com persistência/expiração e inicializador clicável na raiz. Katana, Muralha 18 × 2 × 8 e HUD do ciclo anterior preservados. [Arquivos, lógica, limpeza e roteiro de testes](14-fogo-ambiental-inicializador-2026-10-04.md). Os registros abaixo documentam os ciclos anteriores.

> **Estado vigente — 03/10/2026:** feature Ryūjin Jakka concluída por confirmação do usuário após `5a02b61`, na branch `Feature-Poderes-bankais`. Última validação de código: build, 49 regressões e 14 GameTests aprovados. [Histórico técnico passo a passo](12-historico-tecnico-passo-a-passo-ryujin.md) · [Contrato vigente](08-contrato-ryujin-circulo-tornado-morcegos.md). Esta revisão modifica somente `Docs/poderes`; registros antigos abaixo descrevem suas respectivas versões.


## Evolução do Flame Burst

1. X foi preservado em `TechniqueService.executeFlameBurst`: servidor cobra 20, aplica dano base 6 em raio 3 e cooldown 300 ticks.
2. `141205c`: `StatusData.clearTransformationState` preserva a recarga ao transformar, sem alterar o executor de X.
3. `5a02b61`: `StatusData.setFlameBurstCooldownTicks` atribui zero no modo de teste; `ResourcesData.consumeReiatsu` dispensa débito no modo gratuito; `BleachCommands` restaura as regras por jogador.
4. A última suíte passou com 49 regressões e 14 GameTests. O estado «não compilado» abaixo é histórico; não foi adicionado GameTest exclusivo de combate para X nesta revisão.

---

# Estado operacional — 03/10/2026

Consulte o [contrato atual de Ryūjin Jakka](08-contrato-ryujin-circulo-tornado-morcegos.md). O kit atual usa B para Muralha/Tornado e C para Morcegos/Corte. Corte: dano base 48, custo 45, cooldown 60 segundos, 100 blocos, 25° e destruição sem drops. Branch: Feature-Poderes-bankais. Feature concluída pelo usuário após `5a02b61`; histórico técnico e arquivos descritos nesta atualização.

## Registro histórico abaixo — valores e próximas tarefas anteriores não são o contrato vigente

# Técnica piloto — Flame Burst

## Estado

`IMPLEMENTADO — NÃO COMPILADO NESTE AMBIENTE — NÃO HOMOLOGADO`

## Decisões desta versão

| Propriedade | Valor |
| --- | --- |
| Identificador | `flame_burst` |
| Nome provisório | Flame Burst / Explosão de chamas |
| Requisito de Bankai | Nenhum |
| Requisito de personagem | Personagem criado e jogador vivo |
| Custo | 20 de reiatsu |
| Cooldown | 15 segundos, equivalentes a 300 ticks |
| Área | Raio de 3 blocos ao redor do jogador |
| Dano base provisório | 6 pontos de dano |
| Feedback | Partículas FLAME e LAVA + som de disparo de Blaze |
| Animação óssea | Não utilizada |
| Tecla inicial | X, remapeável no menu de controles |
| Persistência do cooldown | Não persiste em NBT; é estado transitório |

O dano base de 6 pontos e o raio de 3 blocos são suposições provisórias, pois a solicitação definiu custo, cooldown e área curta, mas não definiu o valor numérico do dano.

## Comportamento

A tecla X envia somente o identificador `flame_burst` ao servidor. O servidor verifica personagem criado, vida, espectador, intervalo mínimo de ações, cooldown e reiatsu. O servidor calcula e consome o custo, inicia o cooldown, cria os efeitos e aplica dano às entidades vivas dentro do raio.

A técnica pode ser usada com a forma Selada, Shikai ou Bankai ativa. Ela não exige Bankai.

## Cancelamento e estado

O cooldown é armazenado em `StatusData` apenas durante a sessão. Ele é reduzido no tick do servidor. Morte, logout e inicialização do personagem limpam o estado transitório. O cooldown não é salvo em NBT para evitar que uma sessão interrompida preserve uma penalidade transitória.

## Limitações conhecidas

- O cooldown ainda não possui indicador visual dedicado no HUD.

- O efeito público para outros jogadores usa partículas e som gerados no servidor; não há pacote visual customizado separado.

- Não há animação de modelo ou animação óssea.

- A área pode atingir outras entidades vivas, inclusive jogadores, porque uma regra de PvP/friendly fire ainda não foi definida.

- A técnica ainda não possui GameTest dedicado.

- A implementação precisa ser compilada e testada no Minecraft antes de ser considerada aceita.

## Critérios de aceitação

1. X funciona sem Bankai ativo.

1. X falha com mensagem contextual quando há menos de 20 reiatsu.

1. Uma execução consome exatamente 20 reiatsu.

1. Uma segunda execução dentro de 15 segundos não aplica custo, dano ou efeitos.

1. Após 15 segundos, a técnica pode ser executada novamente.

1. O dano ocorre apenas dentro da área curta definida.

1. Partículas e som aparecem na execução.

1. Morte e reconexão não deixam cooldown preso no estado persistido.

1. O cliente não consegue escolher dano, custo ou cooldown.

1. O build e o teste manual confirmam que a tecla pode ser remapeada.