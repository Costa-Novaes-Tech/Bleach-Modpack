# Primeira missão de Rukia por etapas

30/10/2026. Branch `feat/jigui/quests`, base `74a4bb37`, preservadas as alterações anteriores da conversa.

## Direção e comportamento

"Iniciar jogo/mundo" neste recorte significa personagem Shinigami. Em personagem sem progresso há uma única missão visível: **Um Hollow na área**. Não recebe Asauchi na escolha da classe.

1. Aceitar no Mission Journal registra a missão, mostra o HUD e vincula a bússola ao UUID da primeira Rukia (`karakura_inicio`). Não cria combate.
2. Interagir com essa Rukia abre o diálogo introdutório sobre Hollows e Shinigamis. Confirmar **Enfrentar o Hollow** inicia o boss, sem espada.
3. Matar o Hollow Boss da própria tentativa libera **Volte e converse com Rukia**. A missão continua aceita e a próxima continua oculta.
4. Interagir novamente abre o diálogo de Shinigami temporário. Confirmar **Receber Asauchi e concluir** entrega uma Asauchi e conclui. Inventário cheio recebe drop; repetição da ação não duplica.
5. **Laços além da morte** aparece no Journal, sem início automático.

Objetivos futuros ficam ocultos. Etapas, UUID do destino e tentativa são persistidos. Outra instância de Rukia, outro jogador, alvo de tentativa antiga ou interação distante não substituem o encontro correto. Perder a tentativa reinicia a apresentação sem entrega antecipada.

Sem uma Rukia inicial conhecida no mapa, a provisão procura uma Rukia local sem contexto ou cria uma persistente em chão livre próximo. Não altera terreno nem carrega chunks à força; chão inválido impede provisão até nova tentativa. A primeira instância conhecida é reutilizada pelos jogadores. FakePlayers não disparam essa provisão automática.

## Migração

Definição versão 2. A versão anterior reconhecida (um único Hollow/Boss) recebe backup `01.json.pre-stages.bak`. O progresso antigo incompleto é vinculado ao fluxo novo no login, reiniciando a apresentação e a identidade da tentativa. Progresso concluído, entregas e espadas antigas são preservados. Não existe migração genérica para definições customizadas arbitrárias.

## Validação observada

- Baseline anterior: 162 regressões, 229 GameTests e cliente real aprovados.
- `check build runGameTestServer runClient --offline -PstoryQuestQa=true --console=plain`: exit 0, **163 regressões e 230 GameTests obrigatórios aprovados**; log `run/rukia-stages-release.log`.
- Validação final do cliente com provisão automática (nenhuma Rukia inicial colocada pelo fixture), aceitação pelo pacote real do Journal e confirmações pelo pacote de diálogo: `check build runClient --offline -PstoryQuestQa=true --console=plain`, exit 0, **163 regressões**, `Story quest QA complete`; log `run/rukia-stages-client-spawn.log`.
- Minecraft 1.20.1, Forge 47.4.10, Java 17.0.20.1; cache acessado com autorização, sem instalar dependências.
- Primeira rodada precisou atualizar o teste de navegação que ainda esperava um ofertante no aceite. Segunda rodada falhou em cancelamento de ataque do boss; a provisão foi restrita a jogadores reais para evitar interferência nos FakePlayers das fixtures. A rodada final completa passou.

Regressões novas cobrem ocultação das etapas, persistência e migração com backup. GameTests cobrem aceite sem combate/espada, diálogo inicial, alvo real, crédito pessoal/tentativa, retorno obrigatório, mesma Rukia, próxima missão oculta até conclusão, entrega única, inventário cheio e falha/reinício sem obter espada.

Capturas finais: `run/story-quest-qa/evidence/Story-Quest-QA-1793368152036/`, sete PNGs e `frames.jsonl`. Inspecionados nesse cenário os marcos de bússola, retorno e Journal concluído; os dois diálogos e Journal inicial foram também inspecionados na execução anterior `Story-Quest-QA-1793368069358`. Legibilidade subjetiva: 4/5, layout existente da conversa preservado. Cópias dos sete PNGs finais acompanham este relatório.

O cenário usa modo criativo e dano de teste no boss real após finalizar sua entrada pela API de fixture. Não demonstra balanceamento de luta manual sem espada, desempenho, combate cooperativo ou dois clientes reais. A provisão automática foi observada em mundo plano; terrenos/mapas específicos continuam exigindo validação própria. Não houve commit/push.
