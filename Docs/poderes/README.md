**26/10 — [109: Ryūjin frontal e fogo texturizado](109-ryujin-frontal-fogo-texturizado-calor-2026-10-26.md):** Codex implementou diretamente na Feature a câmera frontal, sol com fogo animado do atlas vanilla, absorção curva, âncoras de pose compartilhadas, fumaça/cinzas por tick e apresentação quente local. Detalhe inclui rosto/lâmina; retorno às perspectivas reais e frame da cabeça corrigidos. Check/build147 regressões e230 GameTests; cliente8cenários/616capturas. Falha isolada anterior na cadência do Brute preservada, sem alterações de Hollow; suíte final aprovada. Áudios e contrato140/60/40 e demais poderes preservados. Playtest artístico/escuta, Photon e multiplayer pendentes; sem commit/push nesta rodada.

**26/10 — [112: revisão obrigatória arma/mão/efeitos](112-tarefa-revisao-combate-armas-maos-efeitos-2026-10-26.md):** usuário relata armas/poderes sem acompanhar braços nas animações novas. Comparação inicial confirma ausência de canais de item nos seis JSONs Benihime/Senbonzakura versus rightItem na referência nativa; causa completa ainda exige reprodução de renderer/camadas, principalmente em primeira pessoa. Tarefa transversal pendente, vinculada ao item4 e ao aceite do combate Senbonzakura. Preservar identidades Asauchi/Ryūjin/Hyōrinmaru, áreas fixas, mecânicas e demais itens do roadmap.

**26/10 — [111: integração Senbonzakura e C vertical Ryūjin](111-integracao-senbonzakura-corte-vertical-ryujin-2026-10-26.md):** Antigravity08105c2 integrado no merge localdb0f764, sem conflito;110 preservado em6d3c9b3. Revisão remove sobreposição de melee legado/Better Combat, isola QA e limita bônus de alcance ao Senbon Bankai. C com duas mãos, elevação e contato vertical no solo; brasas reduzidas e apoio do conjurador preservado. Check147 regressões/233 GameTests e10cenários gráficos amostrados. Aceite artístico pendente; pausas da destruição extensa ficam no item8 de eficiência. Sem push/main.

**26/10 — [110: Ryūjin40s/18s, Minami10 e C](110-ryujin-roteiro-longo-minami-corte-supremo-2026-10-26.md):** implementados roteiro em relógio de apresentação independente, trilha original com ducking, dez soldados de6HP com carvão/magma e persistência temporária no Pacífico, e preparação do C56ticks/impacto40. Mecânica release60/commit40, dano/custos/passivas preservados. Check/build147 regressões,231 GameTests e nove cenários reais amostrados. Aceite artístico/auditivo pendente; fontes locais sem commit/push. Demais itens obrigatórios e Senbonzakura paralela preservados.

**Integração25/10 — [108: Senbonzakura, katana única e encerramento](108-integracao-senbonzakura-katana-encerramento-2026-10-25.md):** entrega do design salva em d20cc3d e integrada em af090b1, sem conflitos. Preservada Ryūjin106/107. Arma/bainha sem duplicação na cena, dissolução gradual e retorno244–286 com perspectiva/FOV/HUD restaurados. Check/build147 regressões e229 GameTests obrigatórios aprovados; amostras de QA do design conferidas, cliente gráfico integrado/escuta e aceite do usuário pendentes.

**Integração25/10 — [107: refinamento Ryūjin](107-integracao-refinamento-ryujin-2026-10-25.md):** commit6ddf420 integrado em da73e58; conflito apenas no aviso de continuidade resolvido preservando105/106. Sol/áudio/câmera atualizados; check/build147 regressões e229 GameTests aprovados. Playtest gráfico e escuta pendentes; limites do diagnóstico de voz e do retorno fixo da câmera registrados no107. Mudanças locais Senbonzakura do checkout design não incluídas.

**Integração25/10 — [105: três merges e eficiência](105-integracao-tres-branches-roadmap-eficiencia-2026-10-25.md):** frentes Ryūjin1a30f87 e Senbonzakura c130a5a commitadas/publicadas nas próprias branches; main remota26b73b3, design e Ryūjin integradas nessa ordem na Feature-Poderes-bankais, sem conflitos textuais. Check/build147 regressões e229 GameTests obrigatórios aprovados. Nova etapa8 de eficiência/limpeza no roadmap72; worktrees preservados para IAs em paralelo, nenhum arquivo apagado. Cliente gráfico/escuta do conjunto e aceite Ryūjin ainda pendentes. Handoff GDD recebido da main aponta M11 com retorno M05, superando a referência anterior a M09.

> **Frente94 — preparação Antigravity:** [checkout separado, skill, prompts e dois MCPs locais](94-antigravity-design-onboarding-2026-10-22.md). Branch `codex/design-poderes-criaturas` preservada em830ad64; Codex continua na Feature. Autoria Blockbench real e6poses; check/build137 regressões; runtime MCP abriu cliente real,6cenários/exit0/autoexit, primeira configuração assistida por Codex. Não representa sincronização das branches ou integração do modelo de teste ao jogo.

**Atualização 23/10 — [ciclo 102: câmera Benihime na cortina](102-benihime-camera-pega-cortina-revelacao-2026-10-23.md):** close acompanha mão/borda, recua para rosto e apoios e reencontra o plano amplo. Cena 28 s, áudio, rig e gameplay preservados. Check/build 147 regressões; cliente Forge dia/noite e acompanhamento concluídos, com evidências amostradas. Homologação artística do movimento completo e perspectivas manuais pendentes. Sem commit/push/merge nesta rodada.


**Integração 23/10 — [ciclo 100: poderes + design + main](100-integracao-branches-poderes-design-main-2026-10-23.md):** resolvidos os conflitos de continuidade e remoção de logs; preservados câmera/fala do ciclo 99, hidra/pose da frente de design e correções/Hollows da equipe. Fonte Blockbench com caminho relativo entre checkouts. Check/build: 146 regressões; 223 GameTests obrigatórios aprovados. Cliente Forge integrado: 10 cenários/exit 0; branches de trabalho destinadas à mesma integração, sem force-push. Sem commit direto na main.

**Atualização 23/10 — [ciclo 99: fala Bankai e câmera Hyōrinmaru](99-hyorinmaru-fala-camera-coordenacao-2026-10-23.md):** fala refeita do WAV reenviado, sem fade fixo; preload e cauda discreta. Câmera com close, revelação, plano lateral e retorno suave ao F5. Check/build: 142 regressões; duas execuções reais de 10 cenários concluídas. Retorno orbital corrigido após inspeção gráfica. Escuta e demais perspectivas pendentes. [Prompt 98](98-prompt-antigravity-mandibulas-pose-hyorinmaru-2026-10-23.md) separa mandíbulas/PlayerAnimator na branch de design. Sem merge ou alteração de gameplay.

> **Implementação mais recente — ciclo91, 22/10/2026:** [hidra robusta, impactos explosivos sem retorno e clima local](91-hidra-robusta-explosoes-clima-local-2026-10-22.md). Rede2.34, schema9/revisão4. Base e Photon140 regressões/176 GameTests; critérios de homologação e evidências no relatório. Mantidas as pendências do roadmap.

> **Ciclo93 — teste limitado:** [controle real do Blockbench, mandíbula de laboratório e correção do rugido](93-blockbench-prova-controlada-rugido-hidras-2026-10-22.md). Modelo de produção preservado; execute_script desligado; integração/exportação GeckoLib e escuta humana pendentes.

> **Implementação atual — ciclo92:** [92: áudio Hyōrinmaru, transformação20s e gelo temporário](92-audio-hyorinmaru-transformacao-gelo-temporario-2026-10-22.md). Falas fornecidas pelo usuário, música reduzida, rugido mantido; cena20s, baforada36blocos e gelo visual temporário. Sem weather global. Rede2.35; resultados e pendências no relatório. Registros seguintes são antecedentes.

> **Próximos ajustes — ciclo90, somente planejamento:** [proporções da hidra, remoção de weather e três impactos explosivos sem retorno](90-planejamento-refino-hidra-impactos-sem-weather-2026-10-21.md). A versão implementada continua89; raio/dano das explosões ainda a fechar.

> **Implementação atual — ciclo89:** [hidra articulada, transformação e novo kit Hyōrinmaru](89-implementacao-hyorinmaru-hidra-kit-2026-10-21.md). Bankai H Ryūsenka / N Hidra / B Erupção / C preservado; Shikai B Sennen e selada B Revestimento. Rede2.33, schema9/revisão4. Base+Photon139 regressões/173 GameTests; homologação artística e multiplayer com dois clientes pendentes. Os avisos seguintes são registros anteriores.

> **Plano antecedente — ciclo88:** [novos slots, vídeos e três storyboards](88-revisao-kit-hyorinmaru-hidra-erupcao-storyboards-2026-10-21.md). Bankai H Ryūsenka / N Assalto da Hidra / B Erupção Glacial / C Hyōten; Shikai B Sennen ampliada. Supera o mapa do87, ainda sem alterações no código testável84.

> **Registro anterior — 21/10/2026:** branch de trabalho `Feature-Poderes-bankais`. Versão testável: [ciclo84](84-revisao-combate-benihime-hyorinmaru-2026-10-21.md). Próxima direção visual aceita para planejamento: [storyboard86](86-prototipo-hyorinmaru-hidra-baforada-transformacao-2026-10-21.md), detalhado no [plano de implementação e kit87](87-plano-implementacao-hyorinmaru-transformacao-kit-2026-10-21.md). Ciclos86–87 são documentação/protótipo, sem nova implementação. Ler [continuidade](00-CONTINUIDADE-CODEX.md) antes dos avisos históricos abaixo; não inferir autorização de checkout a partir deles.

> **Passagem para nova frente — 21/10/2026:** [Criação/design de poderes e criaturas com Blockbench](82-nova-frente-design-poderes-criaturas-blockbench-2026-10-21.md). Documento atualizado pelo código e ciclos até 81: orienta criar `codex/design-poderes-criaturas` a partir da base revisada, inventariar ferramentas e seguir autoria/export/QA real. A branch ainda não foi criada por esta atualização; sem código, instalações ou testes novos. Registros anteriores abaixo são históricos.

> **Ciclo 74 — apresentação Benihime, 20/10/2026:** [piso, penumbra/holofote, câmera e áudio](74-benihime-palco-penumbra-camera-audio-2026-10-20.md). Rede **2.29**, schema **9**; usar a mesma build em clientes/servidor. Piso temporário visual, sem colisão ou alterações nos blocos. Nome da Bankai com trilha reduzida e fala reforçada; aceite artístico/escuta continua pendente. Os avisos anteriores são históricos.

> **Estado vigente — ciclo 72, 20/10/2026:** [revisão geral do roadmap e game design](72-revisao-geral-roadmap-gamedesign-2026-10-20.md). Consolida entregas, aceites, pendências e divergências de documentação; os registros abaixo são históricos. Nome completo da Bankai ainda encoberto pela música; correção adiada nesta rodada. Sem código ou novos testes.

> **Ciclo 40 — 15/10/2026:** playtest recebido; escudo refinado, esquiva/guarda habilitadas no criativo sem voo e esquiva ampliada para 3,5 blocos nominais. Build, 80 regressões e 73 GameTests aprovados. [Contrato e nova conferência](40-refinamento-escudo-criativo-esquiva-ishida-2026-10-15.md). Ishida adicionado à fila; Byakuya continua próximo.

> **Ciclo 39 — 15/10/2026:** bloqueio/Postura/Guard Break juntos, escudo visível; segurar X configurável. Protocolo 2.11, schema 8. Build, 80 regressões e 71 GameTests obrigatórios aprovados; playtest pendente. [Contrato e roteiro](39-bloqueio-postura-guard-break-2026-10-15.md). Próximo kit: Byakuya; Mentor/Proficiências adiados para confirmação com a equipe.

> **Ciclo 37 — 15/10/2026:** esquiva Alt + direção implementada para playtest; protocolo 2.10, schema 7. Build, 76 regressões e 67 GameTests obrigatórios aprovados. [Contrato, limites, homologação e próximas opções](37-esquiva-direcional-stamina-2026-10-15.md). Os registros abaixo são históricos.

> **Nova conversa — entrada recomendada, 15/10/2026:** leia primeiro [Continuidade do projeto](00-CONTINUIDADE-CODEX.md). O resumo reúne estado homologado, branch/restrições, protocolo/schema, mapa técnico, validações e proposta seguinte. Não é necessário importar todo o histórico nem reler todos os relatórios. Os registros abaixo permanecem como histórico.

> **Estado vigente — ciclo 36, 13/10/2026:** proporção/inclinação finais da bainha homologadas pelo usuário; documentação consolidada de combate, stamina, armas indestrutíveis e saque/guarda. [Contratos atuais, arquivos, histórico e próxima proposta](36-consolidacao-homologacao-proximos-passos-2026-10-13.md). Próxima recomendação: esquiva curta com stamina, ainda sem implementação. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 35, 13/10/2026:** bainha ampliada em 15% e inclinada mais 18 graus para baixo, mantendo curva e lado corrigido. [Arquivo, valores e conferência visual](35-escala-inclinacao-bainha-2026-10-13.md). Ajuste final aguarda homologação; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 34, 13/10/2026:** stamina do ciclo 33 homologada; bainha menor, afunilada e curva, lado anatômico corrigido com ajuste de gesto. Tooltip de Resistência na tela K informa stamina máxima. [Arquivos, causa, proporções e roteiro](34-refinamento-bainha-tooltip-resistencia-2026-10-13.md). Refinamento visual aguarda homologação; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 33, 13/10/2026:** stamina máxima por Resistência com retornos decrescentes e sem recarga por compra; bainha da arma vinculada e saque/guarda cosméticos nativos. [Fórmula, arquivos, sincronização, limites e roteiro](33-progressao-stamina-bainhas-saque-2026-10-13.md). Schema 7; protocolo 2.9. Visual e balanceamento aguardam playtest; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 32, 13/10/2026:** ciclo 31 e estado de combate homologados pelo usuário. Golpes básicos de zanpakutō gastam até 4 stamina por tentativa; técnicas preservam custos de reiatsu. As três espadas são indestrutíveis por decisão do usuário. [Contrato, arquivos, lógica e testes](32-ataques-basicos-zanpakutos-indestrutiveis-2026-10-13.md). Registros anteriores abaixo são históricos.

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

## Planejamento que originou o ciclo 19 — 06/10/2026 (registro histórico)

A Ryujin permanece homologada e encerrada. Esta etapa somente documenta:

1. [Contrato de identidade, vínculo e maestria](16-contrato-identidade-vinculo-maestria-zanpakuto.md): um poder principal ativo, troca especial, progresso próprio e requisitos de liberações; decisões aprovadas distintas de propostas.
2. [Planejamento de Hyōrinmaru / Hitsugaya](17-plano-hyorinmaru-hitsugaya.md): escolha aprovada pelo usuário, gelo/controle de movimento, protótipo gradual e responsabilidades de história.
3. [Revisão de escopo e entrega à equipe](18-revisao-escopo-e-entrega-equipe-2026-10-06.md): dependências, diferenças entre protótipo e visão, e inventário das fontes.

Nenhum vínculo novo, técnica de gelo, migração ou campanha foi implementado por esses documentos. História, consequências da troca e números finais continuam abertos para a equipe.

# Poderes — referência operacional

Branch: `Feature-Poderes-bankais`. Atualização: 03/10/2026.

Estado: feature concluída por confirmação do usuário após `5a02b61`. Última suíte de código: 49 regressões/14 GameTests e build aprovados. Esta entrega atualiza exclusivamente documentos desta pasta.

## Entrega atual de acabamento (04/10)

Comece pelo [relatório 13](13-polimento-katana-liberacao-muralha-2026-10-04.md) para valores vigentes, geometria, superfícies geradas, efeitos, política de alvos, HUD e testes. O ciclo anterior abaixo continua encerrado; esta revisão é um novo acabamento autorizado.

## Ordem de leitura e histórico de implementação

1. [Histórico técnico passo a passo](12-historico-tecnico-passo-a-passo-ryujin.md): motivos, lógica, sequência e arquivos de cada feature. Distingue base H/N/X reaproveitada das entregas novas.
2. [Progresso](progresso.md): tabela de entregas `141205c` → `57966f8` → `5a02b61` e aceite final.
3. [Contrato](08-contrato-ryujin-circulo-tornado-morcegos.md): comportamento vigente, valores e ciclo de vida.
4. [Manual desta pasta](manual-do-jogador.md): modo de teste gratuito/sem recarga e restauração.
5. Relatórios 09/10/11: resultados de cada versão, sem substituir fatos antigos pelos números finais.

## Mapa dos demais documentos

| Documento | Atualização registrada |
| --- | --- |
| [05 — Flame Burst](05-tecnica-piloto-flame-burst.md) | Executor X preservado; integração aos overrides de custo/cooldown |
| [06 — Plano do kit](06-plano-kit-ryujin-jakka.md) | Propostas convertidas em entregas; substituição de Círculo; fechamento |
| [07 — Checklist H](07-checklist-f1-ignicao.md) | Ryujin como requisito; lógica de ignição/Dash e comandos de teste |
| [Migração](Migração-das-técnicas-para-Ryūjin-Jakka.md) | Autorização da arma, modelos próprios e distinção dos números antigos |
| [Auditoria](<Auditoria do snapshot da branch de poderes .md>) | Divergências resolvidas, commits verificáveis e escopo futuro preservado |

Cada documento contém atualização contextual com passos e arquivos; o texto original permanece identificado como histórico quando descreve uma versão anterior.

- [Consolidação da feature Ryūjin, comandos e modelos](11-fechamento-ryujin-comandos-modelos-2026-10-03.md): todas as entregas desta branch, instruções de teste, validação e pendências.
- [Contrato vigente do kit](08-contrato-ryujin-circulo-tornado-morcegos.md): teclas, formas, dimensões, custos, danos, recargas e ciclo de vida. O nome do arquivo conserva o histórico do Círculo, que foi substituído por Muralha.
- [Progresso atual](progresso.md): estado de homologação e registro histórico.
- [Manual do jogador](../jogador/manual-do-jogador.md): controles, progressão e comandos.
- [Entrega inicial](09-relatorio-ciclo-2026-10-03.md) e [revisão após teste](10-revisao-muralha-tornado-morcegos-2026-10-03.md): registros das etapas anteriores.

Planos antigos, auditorias e relatórios históricos não substituem o contrato vigente.
`main` não é branch de entrega desta feature.

## Entrega atual: fogo ambiental e inicializador

[Relatório 14](14-fogo-ambiental-inicializador-2026-10-04.md) documenta focos temporários de Bankai, persistência, limites e limpeza, e o inicializador Iniciar-Teste.bat. Build, 51 regressões e 18 GameTests aprovados; homologação visual e abertura interativa pendentes.

## Inicializador auditado — 04/10/2026

Feature Ryujin homologada e encerrada pelo usuário. Revisão de segurança limitada ao inicializador; correções de caminhos, links, preflight e sinalização de resíduos. [Auditoria e reprodução dos testes](15-auditoria-seguranca-inicializador-2026-10-04.md).

Resultado da auditoria: 18 cenários de segurança aprovados, zero falhas e fixtures temporárias removidas; gameplay permanece igual ao homologado.

## Entrega do ciclo 19 — 06/10/2026

- Ryujin: onda N de quatro blocos na selada/Shikai/Bankai, avanço 8/14 ticks, queda 6 ticks, residual completo 60/80 ticks; dano imediato e custos preservados. Aura contínua da lâmina Bankai com fumaça.
- X Flame Burst retirado; HUD e comandos de teste cobrem os quatro slots atuais. Protocolo 2.4 exige cliente/servidor atualizados juntos.
- Hyōrinmaru: novo item/katana de um gume, três modelos, quatro habilidades provisórias de gelo/controle, maestria e formas separadas. Selada com cortes físicos. Bind de operador mantém recargas, guarda progresso por identidade e retorna à selada. NBT schema 4 preserva saves antigos.
- Recompensas da campanha existente continuam vinculadas à Ryujin. História e desbloqueios definitivos do gelo permanecem com a equipe. Testes gratuitos suspendem ganho passivo de maestria.
- `check build runGameTestServer --offline`: **55 regressões e 24 GameTests aprovados**, JAR gerado. Geometria e integração automatizadas; aparência/FPS e balanceamento aguardam homologação do usuário.
- [Passo a passo, pesquisa, arquivos e comandos](19-hyorinmaru-identidade-onda-fogo-2026-10-06.md). Outros documentos fora de `Docs/poderes` e inicializador preservados.
## Revisão autorizada do ciclo 20 — 06/10/2026

A selada passa a ter quatro habilidades básicas de gelo: H/N de alcance seis, B de armadura temporária (25%/6s) e C de controle de alvo único com hipotermia. Isso substitui a proposta anterior de dois cortes sem gelo. Shikai/Bankai recebem mais neve, congelamento, criatura cosmética baseada em Phantom e armadura visual crescente; Bankai inclui asas/cauda e clima com concessão/retorno ao estado anterior. HUD informa habilidades, atalhos reais e requisitos vigentes de formas, sem novos patamares individuais arbitrários. A escala continua 0–100, com história e progressão final sob responsabilidade da equipe. [Implementação e limites do motor vanilla](20-refinamento-gelo-clima-hud-2026-10-06.md).
## Entrega do ciclo 20 — 06/10/2026

1. Ryujin: frente N com +150% de partículas por amostra; centro do tornado três blocos à frente para desenho e dano, com alcance/altura/custos preservados.
2. Hyōrinmaru selada: H/N de alcance seis com neve/som/lentidão; B proteção temporária de 25% por seis segundos; C controle de alvo único com empurrão, lentidão forte e hipotermia vanilla.
3. Liberações: mais partículas, hipotermia nos golpes/campos, criatura visual de Phantom com material de gelo, armadura em camadas, asas/cauda Bankai e pulso/aura de liberação.
4. Clima: tempestade durante Bankai no Overworld/biomas permitidos, neve local, referência cooperativa por jogador e restauração do clima anterior. Campos podem expirar sem encerrar o clima. Dados de clima recuperáveis após reinício; alterações posteriores por /weather preservadas.
5. HUD: nome, forma, maestria atual, quatro habilidades/atalhos reais, requisitos da configuração e próxima liberação. Protocolo 2.5 exige cliente/servidor atualizados juntos.
6. `check build runGameTestServer --offline`: **57 regressões e 29 GameTests aprovados**, build gerada. A posição do novo teste de tornado foi isolada verticalmente dos demais ataques de longo alcance para evitar interferência entre cenários paralelos. Não foi necessário mudar a lógica do tornado para corrigir a fixture.
7. Aparência, FPS e equilíbrio de duelo aguardam homologação no cliente. [Relatório com arquivos, decisões e roteiro](20-refinamento-gelo-clima-hud-2026-10-06.md). JAR: `build/libs/bleachmod-0.2.0.jar`; saída local: `build/ice-refinement-validation.log`, não versionada.

Documentação de outras pastas e inicializador preservados. Commit exclusivo na `Feature-Poderes-bankais`.

## Entrega atual — ciclo 23

O [relatório 23 — Asauchi, maestria e desbloqueios](23-asauchi-maestria-desbloqueios-2026-10-07.md) concentra o contrato jogável, sequência técnica, migração, valores, arquivos e homologação. Leia também o adendo do contrato 16 e o manual atualizado. O ciclo 22 foi aceito pelo usuário; esta entrega abre a progressão comum sem reabrir o polimento dos kits elementais. Aizen fica fora do escopo.

Validação final do ciclo 23: **60 regressões / 42 GameTests**, check e build aprovados. Texturas/modelos Asauchi intactos; documentos fora de Docs/poderes intactos. Homologação no cliente pendente.


## Ciclo 24 — catálogo vanilla e bancada visual

Ciclo 23 homologado. Use /bleachdev assets list; /bleachdev assets preview frost 1 (também reiatsu, soul, slash, beam, crystal; intensidades 1–3); /bleachdev assets status; /bleachdev assets stop. Requer OP, só envia efeitos ao próprio jogador, expira em cinco segundos e não altera combate, progressão ou mundo. [Catálogo e roteiro 24](24-catalogo-vanilla-bancada-visual-2026-10-11.md). Modelos/texturas catalogados são opções futuras; a bancada atual demonstra partículas, geometria e som.

Validação do ciclo 24: **62 regressões / 46 GameTests**, check e build aprovados. Catálogo/bancada prontos para comparação visual pelo usuário; nenhuma medição de FPS ou homologação visual afirmada.


## Ciclo 25 — biblioteca de camadas

Use /bleachdev assets palettes e /bleachdev assets compose ice aura 2. Paletas fire/ice/reiatsu; formas aura/cut/beam/release; intensidades 1–3. Uma composição local de cinco segundos substitui a anterior e compartilha orçamento entre material, acento e resíduo. Presets antigos permanecem; poderes existentes intactos. [Contrato, arquivos e roteiro](25-biblioteca-paletas-camadas-2026-10-11.md).

Validação do ciclo 25: **63 regressões / 48 GameTests**, check e build aprovados; escolha visual das composições pendente do usuário.

## Continuidade — ciclo 26

Integração em produção da aura e liberação de gelo: emissão compartilhada por lotes, mesma densidade/forma anterior e receptores próximos na mesma dimensão. Consulte o [relatório do ciclo 26](26-integracao-gelo-emissor-comum-2026-10-11.md) para arquivos, lógica, limites e testes. A próxima migração será escolhida após comparação em jogo; efeitos experimentais da bancada continuam separados dos kits.
## Continuidade — ciclo 27

Concluída a migração visual dos slots de gelo e da trilha do dragão para a biblioteca comum. As densidades e os valores de combate são os mesmos; a origem de campo/entidade determina o espaço de emissão, sem depender do deslocamento do conjurador. Consulte o [relatório do ciclo 27](27-migracao-slots-gelo-2026-10-11.md) para o passo a passo técnico e o roteiro de homologação. A revisão de treino de maestria e feedback do HUD é a próxima etapa após validação desta migração.
## Continuidade — ciclo 28

HUD e menu de vínculo agora distinguem progresso fracionário, domínio individual da técnica, descoberta e compra da forma. Prática válida avisa novas liberações; modo gratuito indica treino suspenso. Ganhos, custos e requisitos anteriores permanecem. O [relatório do ciclo 28](28-feedback-maestria-roteiro-2026-10-11.md) reúne arquivos, lógica e dois roteiros separados: visuais com modo administrativo e treino com custos/cooldowns normais.
## Continuidade — ciclo 29

Usuário homologou visuais, persistência e maestria. Executor, acesso e HUD agora consultam as 24 definições de Ryujin/Hyōrinmaru. Ignição possui consumo contínuo separado do custo inicial; Asauchi continua sem slots. Consulte o [relatório](29-definicoes-compartilhadas-2026-10-12.md) e o [contrato para novos poderes](contrato-criacao-novos-poderes.md). A próxima feature será discutida antes de retomar outra habilidade/Bankai; narrativa e Aizen seguem futuros.
## Continuidade — ciclo 30

Dano aceito entre entidades vivas ativa/renova o estado automático de combate do jogador. O indicador acompanha snapshot do servidor; estado expira após 200 ticks sem conflito ou ao limpar lifecycle. Não altera hotbar, regen, stamina ou habilidades. Consulte o [relatório do ciclo 30](30-estado-combate-2026-10-12.md); cliente/servidor exigem protocolo 2.7.

## Ciclo 36 — Consolidação e encerramento visual

Homologação final recebida do usuário para bainha, proporção e inclinação. Stamina/progressão e estado de combate aprovados nos testes anteriores. Consolidado o histórico técnico dos ciclos 30–35, arquivos, contratos e limites. [Relatório de fechamento e proposta de esquiva](36-consolidacao-homologacao-proximos-passos-2026-10-13.md). Próxima fatia proposta: esquiva direcional curta com stamina; depois bloqueio/Postura. Nenhuma dessas ações foi implementada neste ciclo documental.
# Entrega26/10 — ciclo119

[Ryūjin12s, áudio/câmera e evidências](119-entrega-ryujin-curta-audio-camera-2026-10-26.md):148 regressões,233 GameTests e12cenários reais amostrados. Aceite de voz/ritmo pendente; pausa do C em terreno continua no roadmap de eficiência. [Contrato comum118](118-contrato-comum-arma-maos-efeitos-2026-10-26.md) para os combos das frentes Antigravity. Mudanças locais ainda sem commit/push.
# Integração26/10 — ciclo120

[Benihime e Senbon integrados com a Ryūjin curta](120-integracao-antigravity-combates-benihime-senbon-2026-10-26.md): commits e merges locais sem conflitos,148 regressões,233 GameTests e10cenários reais amostrados. JAR e roteiro de playtest no relatório. Rastro fixo Senbon, fluidez do C em terreno e aceite artístico continuam pendentes.

# Revisão26/10 — ciclo121

[Main, planejamento futuro e impactos no roadmap](121-revisao-main-planejamento-futuro-2026-10-26.md): inventário completo dos textos e revisão funcional do GDD vigente, distinguindo decisões de implementação. Próximo bloco documentalM11/M05; lista obrigatória de poderes preservada. Sem merge ou alteração de runtime.
