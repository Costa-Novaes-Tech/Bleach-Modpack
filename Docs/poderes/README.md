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
