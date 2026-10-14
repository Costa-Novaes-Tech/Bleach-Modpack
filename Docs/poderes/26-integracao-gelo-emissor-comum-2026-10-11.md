> **Estado vigente — ciclo 35, 13/10/2026:** bainha ampliada em 15% e inclinada mais 18 graus para baixo, mantendo curva e lado corrigido. [Arquivo, valores e conferência visual](35-escala-inclinacao-bainha-2026-10-13.md). Ajuste final aguarda homologação; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 34, 13/10/2026:** stamina do ciclo 33 homologada; bainha menor, afunilada e curva, lado anatômico corrigido com ajuste de gesto. Tooltip de Resistência na tela K informa stamina máxima. [Arquivos, causa, proporções e roteiro](34-refinamento-bainha-tooltip-resistencia-2026-10-13.md). Refinamento visual aguarda homologação; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 33, 13/10/2026:** stamina máxima por Resistência com retornos decrescentes e sem recarga por compra; bainha da arma vinculada e saque/guarda cosméticos nativos. [Fórmula, arquivos, sincronização, limites e roteiro](33-progressao-stamina-bainhas-saque-2026-10-13.md). Schema 7; protocolo 2.9. Visual e balanceamento aguardam playtest; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 32, 13/10/2026:** ciclo 31 e estado de combate homologados pelo usuário. Golpes básicos de zanpakutō gastam até 4 stamina por tentativa; técnicas preservam custos de reiatsu. As três espadas são indestrutíveis por decisão do usuário. [Contrato, arquivos, lógica e testes](32-ataques-basicos-zanpakutos-indestrutiveis-2026-10-13.md). Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 31, 13/10/2026:** stamina de corrida com pausa, recuperação, exaustão e HUD. Valores provisórios; ciclo 30 segue em teste visual. [Contrato, arquivos, lógica e roteiro](31-stamina-corrida-2026-10-13.md). Schema 6; protocolo 2.8. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 30, 12/10/2026:** primeira fatia de Combat State automático, indicador no HUD e limpeza transitória, sem mudar regeneração/stamina/atributos. [Lógica, arquivos, timeout provisório e testes](30-estado-combate-2026-10-12.md). Protocolo 2.7; schema 5 preservado. Combat Mode/hotbar seguem futuros; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 29, 12/10/2026:** ciclos anteriores homologados pelo usuário. Definições comuns dos 24 slots/formas de Ryujin e Hyōrinmaru consultadas por execução, acesso e HUD; valores preservados. [Arquivos, lógica e validação](29-definicoes-compartilhadas-2026-10-12.md) · [Contrato para novos poderes](contrato-criacao-novos-poderes.md). Registros anteriores abaixo são históricos; nova feature será discutida antes de outro kit.

> **Estado vigente — ciclo 28, 11/10/2026:** feedback de maestria sem arredondar limiares, avisos de novas liberações pelo treino, distinção de descoberta/compra/disponibilidade e indicação de treino suspenso em modo teste. [Arquivos, lógica e roteiros de visuais/maestria](28-feedback-maestria-roteiro-2026-10-11.md). Ganhos e requisitos preservados; ciclos abaixo são históricos.

> **Estado vigente — ciclo 27, 11/10/2026:** cortes, barreira, Zona Glacial, impacto de controle e trilha do dragão da Hyōrinmaru usam o emissor comum, mantendo aparência e combate. [Arquivos, lógica, valores e testes](27-migracao-slots-gelo-2026-10-11.md). Clima e terreno conservam sua lógica; registros abaixo são históricos.

# Ciclo 26 — Primeira integração visual em produção

Data: 11/10/2026. Branch: Feature-Poderes-bankais.

## Entrega

A aura e a liberação da Hyōrinmaru passam pelo emissor comum. A aparência homologada serve de perfil inicial: nenhum acento azul, nuvem ou geometria experimental da bancada foi aplicado automaticamente. As composições do ciclo 25 continuam propostas para comparação. Não foram importadas texturas ou bibliotecas externas.

## Implementação, passo a passo

1. `src/main/java/com/bleachmod/common/visual/VisualFrame.java`: conserva o construtor privado ao jogador, com teto de 48 partículas. Acrescenta a fábrica `nearby(owner, limit)`, com orçamento explícito entre 1 e 160 e envio aos jogadores da mesma dimensão, até 32 blocos de cada ponto de emissão. Usa pacotes vanilla sem distância forçada.
2. O mesmo frame soma todas as camadas e lotes; cada lote possui contagem e dispersões independentes em X/Y/Z. Se não couber no saldo, é rejeitado inteiro. Valores não finitos, negativos, acima dos limites, fonte morta/espectadora, mundo incompatível, pontos distantes e chunks indisponíveis são recusados. Nenhum chunk é carregado pelo emissor.
3. `src/main/java/com/bleachmod/common/visual/IceManifestation.java`: encapsula o perfil aprovado de gelo. Liberação Shikai: 24 pontos em anel de raio 1,5; Bankai: 40 pontos de raio 3. Todos ficam 0,8 bloco acima da origem e emitem quatro snowflakes por ponto, com dispersões 0,2/0,8/0,2 e velocidade 0,08. Totais: 96 e 160 solicitações por liberação completa em chunks disponíveis.
4. Aura: centro 1,2 bloco acima do jogador; quatro snowflakes na Shikai/armadura selada ou dez na Bankai. Dispersões X 0,4/0,9, Y 0,6, Z 0,4; velocidade 0,015.
5. `src/main/java/com/bleachmod/common/technique/HyorinmaruReleaseEffects.java`: delega apenas a emissão de partículas ao perfil. Mantém os gatilhos por identidade/formas, intervalo de oito ticks da aura e sons anteriores. Não cria sessão, entidade, tarefa agendada ou efeito persistido; cada frame termina com a chamada. Partículas enviadas expiram no cliente pela duração vanilla.
6. `src/main/java/com/bleachmod/gametest/AssetBenchGameTests.java`: verifica audiência próxima/privada, distância, dimensão, fonte morta, lotes inválidos, rejeição integral de excesso e densidades aprovadas, além da ausência de mudanças nos dados do personagem e bloco de origem.
7. `src/test/java/com/bleachmod/MvpRegressionTest.java`: verifica anéis centrados, raio/altura preservados e índices inválidos. Demais regressões de habilidades continuam executadas.

## Limites e compatibilidade

O orçamento é por frame e conta partículas solicitadas, sem multiplicar pelo número de receptores; não mede partículas desenhadas, FPS ou largura de banda total. Mais jogadores próximos implicam mais pacotes. Não há limitador global de todos os efeitos. Bancada continua somente para seu executor e com teto 48; combate não usa sua sessão administrativa.

Dano, custos, cooldowns, maestria, desbloqueios, clima, neve no terreno, modelos e texturas permanecem com o comportamento anterior. Ryujin, Asauchi, dragão e efeitos dos quatro slots não foram migrados neste ciclo. Aizen e aquisição narrativa continuam trabalhos futuros. A rejeição de chunks indisponíveis é intencional; não se força carregamento para completar um anel visual na borda de chunks ativos.

## Roteiro de homologação

1. Com Hyōrinmaru vinculada e as formas desbloqueadas, compare a transformação Shikai e Bankai no mesmo cenário usado anteriormente. Confirme anel, densidade, som e aura.
2. Na selada, ative a armadura temporária; confirme aura durante sua duração. Encerre a transformação/armadura, morra ou troque de dimensão; confirme que não surgem novas partículas da manifestação anterior. As já enviadas devem expirar naturalmente.
3. Com outro jogador próximo, confirme que ambos veem a manifestação. Afaste o observador para mais de 32 blocos dos pontos de emissão e teste outra dimensão; ele não deve receber novos pacotes desse efeito.
4. Execute `/bleachdev assets compose ice aura 3`; somente o executor deve ver a demonstração. Use `/bleachdev assets stop` e confirme as habilidades normais depois.
5. Reconfirme os quatro slots, recursos, desbloqueios e restauração do clima. Observe FPS com configuração idêntica à referência; este ciclo não afirma ganhos de desempenho nem homologação visual automática.

## Próximo passo

Após comparar este primeiro uso do emissor, escolher uma manifestação concreta dos slots de gelo para a próxima migração, respeitando seus alcances e durações. Mudanças artísticas novas devem ser avaliadas separadamente da migração técnica. Os perfis experimentais continuam disponíveis para essa seleção.

## Validação

check, build e runGameTestServer offline aprovados: 64 cenários de regressão e 50 GameTests obrigatórios. Log local: `build/cycle26-validation.log`. Revisão de diff aprovada; documentos externos à pasta de poderes e assets intactos. JAR atualizado: `build/libs/bleachmod-0.2.0.jar`. Homologação visual em cliente e comparação de FPS continuam a cargo do teste em jogo.
