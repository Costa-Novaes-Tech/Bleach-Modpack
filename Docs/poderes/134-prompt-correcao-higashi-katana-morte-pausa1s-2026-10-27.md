# 134 — Prompt de correção: Higashi, morte visual, katana e pausa1s

## Copiar para a conversa Antigravity Higashi

Usuário reprovou Higashi após vídeo27/10 075349. Leia Docs/poderes/132 e contatos execution-1/2 em qa/cycle132, prompt125 e contrato118. Preserve Hyōrinmaru: foi aprovada; não refinar mais modelo, rig ou sopro nesta correção. Transformação Ryūjin, investida/rajada, C e Benihime preservados. Outra conversa corrige Senbon.

Usar branch existente codex/ryujin-bankai-sol-selamento, sem criar pasta. Conferir estado real e ancestralidade: sua entrega0675778 já foi integrada na Feature em a18bfc8. Sincronizar com cuidado para não sobrescrever correções Senbon438628e/protocolo2.36 ou documentos Codex. Você não está sozinho; não reverter alterações alheias, não usar reset destrutivo nem main.

## Fase1 — reproduzir e explicar a falha

Vídeo detalhado234–240s: entrada lateral tardia, alvo já em fogo vanilla antes da incineração, pose retida, pouca leitura do corte e só depois substituição por partículas. Usuário relata criatura exibindo morte normal; reproduza essa ocorrência e registre a ordem real de dano/evento de morte/pacote/remoção/render. Não concluir causa só pela aparência. Fazer caso controlado de alvo humanoide comum e do modelo usado no vídeo, se disponível.

Auditar HigashiExecutionVisuals.accept (inicia relógio no IMPACT), BURNS e referência viva da entidade, silhouette/hideIncinerated, dispatcher de fogo externo, deathTime/hurtTime, mixin de puff e event60. Ocultar um puff não elimina morte, tilt ou fogo de todas entidades. O renderer pode depender de estados além de deathTime; não ficar restaurando meia dúzia de campos e presumir estabilidade.

## Fase2 — autoridade de morte e apresentação estável

Servidor continua decisivo:≤7% em alvo elegível OU letalidade normal realmente confirmada. Sobrevivente, miss, aliado, invulnerável, totem e cancelamento não disparam execução. Nada de morte visual especulativa, dano dobrado, ressurreição, drop duplicado ou entidade extra autoritativa.

Separar alvo de gameplay da representação temporária de apresentação. Preservar aparência/escala/equipamento/pose necessários por snapshot seguro quando disponível; não clonar capability/inventário de gameplay nem manter cadáver simulando entidade viva. Para entidades não suportadas, usar fallback explícito e limpo, sem renderer quebrado. Avaliar snapshot existente ou adaptador cliente com renderer apropriado. Garantir uma única representação visível: nem cadáver vanilla + cópia, nem reaparição entre fogo e cinzas.

Não mostrar queda/tilt/puff de morte normal antes da incineração confirmada. Evitar chama vanilla vertical antecipada cobrindo contato; efeitos da própria execução controlam o momento do fogo. Não remover eventos de morte globalmente ou impedir dano/fogo vanilla fora das vítimas confirmadas. Limpar estado visual após fim, morte do executor, logout, dimensão, cancelamento e recursos indisponíveis.

## Fase3 — katana e câmera realmente mostram o golpe

O roteiro deve ser visível: jogador recolhe a Ryūjin junto ao próprio quadril; braço/punho posicionam o gume; corte diagonal de baixo para cima atravessa o alvo; espada termina elevada do outro lado e sustenta a pose. Não apenas braço mexendo com espada parada e não giro genérico. Verificar grip/display, item channels, mixin, prioridades e corpo usando uma coreografia e um relógio. Comparar sem partículas, depois ligar VFX.

Hoje accept começa no IMPACT12 depois da confirmação: a câmera não pode mostrar preparação0..10 que já passou. Resolver isso conscientemente. Caminho preferencial para avaliar: após confirmação autoritativa, apresentar replay visual curto de preparação+corte e alvo representado, com câmera lateral antes da recolha, sem repetir hit/dano nem deslocar entidade real. Durante a apresentação deve haver um só executor/uma só espada visíveis; se usar pose do jogador real, não criar duplicata ou descolar corpo visível da colisão de modo enganoso. Descrever restrições em multiplayer e preservação de controles. Não simplesmente alterar start para zero sem resolver o cadáver e a cronologia de eventos.

Se outra arquitetura for necessária, justificar como mostra preparação sem abrir câmera de execução contra sobrevivente. Não disparar câmera em toda tentativa para contornar falta de confirmação. Não exigir previsão infalível de dano bruto, porque defesa/totem/cancelamento podem alterar resultado.

Câmera lateral estável enquadra ambos e ponta da espada, inclusive alvo grande. Evitar FOV extremo, clipping, troca180graus no meio do golpe e retorno antes da cinza. Escolher flanco seguro uma vez, ajustar distância com continuidade; fallback de espaço impossível deve ser limpo. Observadores veem efeito, sem câmera sequestrada. Testar corredor e parede, não apenas plano vazio.

## Fase4 — pausa aprovada de1segundo

Substitui explicitamente40ticks do prompt125: pausa de20ticks nominais APÓS terminar o corte. Se SLASH_END continuar16, IGNITE=36; com cauda de32ticks, END=68. Se replay visual requer mapa distinto, separar relógio apresentação do instante autoritativo, mantendo20ticks entre fim da lâmina e ignição.

Atualizar todos consumidores: Choreography, pose/weight, retenção da representação, fogo, partículas, câmera/HUD, limpeza, mensagens e regressões de tempo. Buscar constantes/hardcodes40/56/88 pertinentes, sem substituir números indiscriminadamente. Não reduzir32ticks de cinzas por acidente nem contar pausa desde chegada do pacote.

Após a pausa: ignição curta do corpo, incineração progressiva legível e cinzas; nenhum sumiço seco com corpo reaparecendo. Efeitos ajustados ao volume do alvo e orçamento de partículas, sem retângulo de fogo cobrindo tudo. Áudio existente suficiente; não reabrir trilha da transformação.

## Fase5 — prova e patch focado

Executar check/regressionTest e GameTests Higashi pertinentes com comandos/exits e expectativas novas justificadas pela pausa aprovada. GameTests servidor não aprovam câmera ou skin. Prova em Forge real: sobrevivente/miss sem execução; threshold e lethal acima7% com execução; totem/aliado/imune quando disponíveis; dois tamanhos de alvo, câmera inicial primeira/terceira, corredor, retorno de HUD/controle e interrupção. Confirmar recompensa/drop uma vez e nenhuma alteração em transformação Ryūjin aprovada.

Entregar vídeo CONTÍNUO sem efeitos do saque/corte e depois com efeito completo, marcos preparação/contato/fim+20ticks/ignição/cinzas, logs da confirmação e condição de captura. Comparar ao vídeo do usuário. Documentar limitações, não alegar homologação artística. Commits separados: morte/apresentação; arma/câmera; timing/testes. Nenhum ajuste Senbon ou Hyōrinmaru. Não expandir kit até remover regressão.
