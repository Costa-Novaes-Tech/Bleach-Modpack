# Coordenação — comandos, Quincy e empunhadura Senbon

Data: 28/10/2026. Base observada: Feature-Poderes-bankais, d247bb0, árvore limpa antes deste planejamento. Direção aprovada pelo usuário após teste real. Este documento distribui implementação; não declara correções já executadas.

## Ordem e responsabilidade

1. Codex na Feature-Poderes-bankais: inventário completo e reorganização de bleachdev, preset de cheats, troca reversível dos kits, restauração/limpeza de ações e sincronização. Preservar progressão e inventário, evitando perda silenciosa do item substituído. Atualizar testes e usos dos comandos antigos.
2. Antigravity A: arco, técnicas, progressão e controles Quincy. Branch nova codex/quincy-correcao-integracao, da origin/Feature-Poderes-bankais atualizada, em checkout separado já existente.
3. Antigravity B: contato mão/katana no combo Senbon. Branch nova codex/senbon-empunhadura, da mesma base remota atualizada, em outro checkout separado já existente.

Sem novas pastas/worktrees, sem main, sem merges automáticos. Se não houver checkout livre, informar; não trocar a branch do checkout ativo de outra IA. Não trabalhar simultaneamente com duas IAs no mesmo checkout. Preservar alterações alheias; não resetar, limpar ou usar force-push. Branch existente exige conferir ancestralidade em vez de recriar.

## Contrato dos comandos — Codex

Árvore alvo: help; status; kit shinigami <identidade>; kit quincy; cheats all/off; cheats cooldowns on/off; cheats reiatsu on/off; progress max/points/mastery; reset combat; appearance; assets. Jogador que executa é alvo padrão; oferecer alvo explícito com sintaxe consistente e documentada, também utilizável pelo console. on significa cheat ATIVADO, portanto custo/cooldown desativado; explicar na ajuda.

Cheats all: ausência de custos Reiatsu, cooldowns desativados e limpos, Reiatsu cheio, máximos CONFIGURADOS de maestria de formas e técnicas do kit, elegibilidade/desbloqueios pertinentes e saldo mínimo de pontos de teste (proposta inicial: 10000; nunca somar repetidamente). Não confundir pontos de treinamento e Spiritual Points: mapear o campo funcional e apresentar o nome correto. Não conceder todas as habilidades de todas as raças. Não comprar maestria na progressão normal: isto é exceção administrativa explícita de QA. Não inflar atributos para facilitar combate. Stamina normal. cheats off restaura custos/cooldowns, não desfaz aquisições persistentes. Se all for um preset persistente entre trocas, max/configuração devem se aplicar ao novo kit de modo explícito; documentar persistência em logout e reinício.

Troca de kit é administração de laboratório, não aquisição racial de produção: cancelar estado antigo antes de mudar raça/identidade, encerrar câmera/efeitos/atores pertencentes ao jogador e sincronizar o perfil inteiro. Preservar progresso dos kits e não destruir itens/equipamento anterior; evitar duplicação ao repetir o comando. Não fazer restauração por apagar CharacterData ou initialize indiscriminado. Quincy -> Shinigami -> Quincy deve funcionar sem recriar personagem. Kit shinigami permite testar selada/Shikai/Bankai; Asauchi permanece sem Bankai.

Migrar bankai/quincy/zanpakuto bind e demais atalhos antigos para aliases transitórios com aviso e mesma implementação de serviço, sem duas lógicas concorrentes. A lista final de aliases deve partir de busca no código, testes e scripts. bind tem semântica restrita diferente de preparar Bankai; não ampliar silenciosamente esse alias. Depois do período de transição, poderá ser removido. Agrupar skill/mastery/points e reiatsu/cooldowns; manter ajustes finos para testes negativos. assets e appearance continuam com suas finalidades; bleachreload é fora do root, inventariar separadamente sem apagá-lo por conveniência.

Aceite: permissão negada sem mutação; erro de argumento legível; help e sugestões; falta de personagem; idempotência de all/kit; inventário cheio; dois jogadores; troca durante técnica/cena; limpar formações próprias sem apagar entidades alheias; off restaura custos; progresso antigo preservado e recarregável. GameTests reais do dispatcher e testes de persistência, além de cliente com ciclo completo de perfis.

## Fronteiras de arquivos

- Codex: BleachCommands.java e novos serviços de kit/cheats/reset, testes de dispatcher, ajuda/idiomas de comandos. Inventariar AssetBenchCommands/AppearanceCommands e preservar suas funções.
- Antigravity A: common/quincy, client/quincy, assets de arco/projéteis e testes Quincy. Pode corrigir o desvio Quincy em TechniqueService e DodgeService e a entrada B/rede específica, sem reescrever arquivos inteiros. Não editar BleachCommands, serviços de presets, CharacterData/PlayerData/ResourcesData globais sem coordenação: propor diff mínimo se indispensável. Codex reserva troca racial/kit; A reserva autorização de USO racial e aquisição de técnicas. Cheats e autorização devem continuar caminhos distintos.
- Antigravity B: três animações Senbon, geração correspondente, QA Senbon e correção específica do held-item Senbon se medida. Não editar mixin/transformação global compartilhada sem apresentar evidência e proposta. Não editar técnicas Quincy, comandos, Higashi, Benihime ou os ataques Senbon/C.
- Idiomas/registros compartilhados: A pode adicionar somente chaves Quincy necessárias; Codex integra preservando ambas as alterações. Não reformatar todo JSON. Documentação só em Docs/poderes, em arquivos por assunto desta rodada.

## Integração e evidência

AG A e B entregam commits focados na própria branch, base/hash final, comandos e exit codes, resumo de arquivos, marcos gráficos reais e limitações. Publicar as próprias branches se autorizado na conversa; nunca integrar na Feature ou main por conta própria. Um cliente Forge/Gradle pesado de cada vez nesta máquina; preparar arquivos em paralelo, coordenar janela de QA para não esgotar RAM.

Codex integra primeiro os comandos, depois Quincy, depois Senbon, revisando conflitos por comportamento; não aceitar apenas ausência de marcadores. Reexecutar check/build e GameTests na árvore combinada. Teste integrado: preparar Quincy -> arco/H/N/B/C -> cheats off e custos -> Shinigami Benihime selada C -> Senbon três golpes -> Higashi -> retornar Quincy. As duas últimas permanecem validação do usuário onde não houver nova evidência. Hyorinmaru e Zaraki preservadas; não reabrir design ou GDD.

Evidências mínimas, sem centenas de arquivos: vídeo curto contínuo ou sequência temporal suficiente por ação, frames de marcos com tick/tempo real, origem/commit/perfil e roteiro de reprodução. Separar sucesso de build, teste mecânico, inspeção gráfica e aceite artístico. Suítes anteriores: 158 regressões; 243 GameTests na repetição, com primeira falha de cadência do Tank registrada em build/merge-gametest-20261028.log. Não mascarar instabilidade de teste.
