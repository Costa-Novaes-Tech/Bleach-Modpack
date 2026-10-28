# Bleachdev — kits e testes rápidos

Implementação Codex de 28/10/2026 na Feature-Poderes-bankais. Uso administrativo, requer permissão de operador 2 e personagem já criado. Não altera regras de aquisição normais do mod. Acesso rápido: `/bleachdev` ou `/bleachdev help`. Ajuda paginada por assunto: `/bleachdev help kits`, `help cheats`, `help progress` e `help legacy`. Confirmações comuns são curtas; detalhes ficam em status.

## Roteiro principal

```text
/bleachdev kit shinigami benihime
/bleachdev cheats all
/bleachdev kit quincy
/bleachdev kit shinigami senbonzakura
/bleachdev cheats off
/bleachdev status
```

O alvo padrão é quem executa. Nos comandos novos, acrescentar um jogador/selector ao final para alvo explícito; por exemplo `/bleachdev kit shinigami benihime @s` e `/bleachdev cheats all NomeDoJogador`. Console precisa informar alvo.

`kit shinigami` aceita asauchi, ryujin_jakka, hyorinmaru, senbonzakura, benihime e nozarashi. Entrega/equipa a arma, prepara habilidade e requisitos das formas liberadas para o laboratório, deixa o personagem na forma selada e seleciona Bankai como próximo destino quando aplicável. Asauchi permanece selada, sem Bankai. Não inicia uma transformação automaticamente.

`kit quincy` preserva identidade e progresso Shinigami arquivados, muda o perfil de teste para Quincy, equipa o Kojaku e remove os grupos/formas Shinigami ativos. Para voltar, usar `kit shinigami <identidade>`. Não chama inicialização de personagem, não zera atributos, pontos, maestrias ou reserva derivada.

A arma anterior vai para o inventário. Se a nova arma já estiver no inventário, ocorre troca de posições, preservando personalização do item. Repetir o kit não acumula novas armas. Sem espaço para preservar o item anterior, falha antes de mudar raça/kit. A mão secundária não é alterada.

## Preset e ajustes finos

- `/bleachdev cheats all [jogador]`: desativa custos de Reiatsu e cooldowns, limpa ações/cooldowns atuais, preenche Reiatsu, maximiza formas/técnicas do kit Shinigami ou domínio/técnicas Quincy e garante saldo mínimo de 10000 pontos. Repetir não soma outros 10000. Usa o recurso existente trainingPoints, apresentado no jogo como Spirit Points; não cria moeda nova. Stamina e atributos não são maximizados.
- `/bleachdev cheats off [jogador]`: restaura custos e cooldowns normais. Não apaga pontos/maestrias/desbloqueios concedidos.
- `/bleachdev cheats cooldowns on|off [jogador]`: on ativa o cheat, portanto remove a espera. Limpa os contadores atuais sem descartar uma técnica de duas etapas em curso. Desmarca o preset all automático.
- `/bleachdev cheats reiatsu on|off [jogador]`: on remove custo de Reiatsu, sem remover custo de stamina. Desmarca o preset all automático.
- `/bleachdev progress max [jogador]`: concessões de progressão do kit, sem ativar cheats de custo/cooldown.
- `/bleachdev progress points <quantidade> [jogador]`: adiciona pontos, respeitando limite existente de 1000000.
- `/bleachdev progress mastery <sealed|shikai|bankai> <valor> [jogador]`: ajusta maestria da forma atual Shinigami e disponibiliza técnicas elegíveis. Não é ajuste de dano/atributos nem compra normal de maestria.

O preset all acompanha trocas de kit durante a sessão. Os overrides e o preset deixam de valer ao sair/morrer/reiniciar; concessões de progresso permanecem no save. Fazer uma alteração individual de custo/cooldown sai do modo all, preservando os outros toggles já aplicados.

**Quincy atualizado28/10:** domínio e maestrias das quatro técnicas agora são independentes, aparecem no HUD e permanecem no save/troca de kit. all/progress max também maximizam o Quincy; off não apaga esses valores. Aquisição narrativa/economia Gintō ainda são protótipo, sem criação de Shikai/Bankai ou concessão TYBW.

## Limpeza e diagnóstico

`/bleachdev reset combat [jogador]` encerra ações e cenas do dono, limpa cooldowns e canalizações, para movimento/uso de item e devolve câmera de cenas canceladas. Preserva progressão e forma ativa. Passivas permanentes dessa forma podem reaparecer no tick seguinte, o que é esperado. A troca de kit também faz essa limpeza, antes de ativar o novo perfil.

Sprenger: remove somente nós carregados pertencentes ao dono, em todas as dimensões já carregadas, sem forçar chunks ou usar uma caixa de busca do tamanho do mundo. Ação Licht Regen e estado da formação são encerrados. A frente Quincy continua responsável pelo restante do ciclo de vida de projéteis/limpeza da sua implementação.

Higashi: reset administrativo remove pose/câmera do atacante; cinzas de uma vítima já morta terminam normalmente, sem ressuscitar a vítima ou reexibir sua morte vanilla. Não modifica coreografia/dano/pausa.

Zaraki: sua cena não foi alterada. Se a transformação ininterruptível estiver acontecendo, o comando de troca/reset rejeita a ação com uma mensagem para aguardar o término.

`/bleachdev status [jogador]` é somente leitura e diferencia kit ativo da identidade zanpakutō arquivada. Mostra forma/próximo destino quando aplicável, Reiatsu, flags, pontos e maestrias de formas. `appearance` e `assets` continuam ferramentas especializadas. `/bleachreload quests` continua separado por ser recarga administrativa de registries.

## Transição dos comandos antigos

Os caminhos antigos continuam temporariamente compatíveis com aviso; isto evita quebrar scripts/testes e hábitos durante a migração. Eles ainda aparecem no autocomplete nesta etapa. Não é a remoção definitiva da árvore antiga.

- `bankai <jogador> [identidade]`: encaminha ao serviço de kit Shinigami; agora permite voltar do Quincy.
- `quincy <jogador>`: encaminha ao serviço de kit Quincy.
- `zanpakuto bind <jogador> <identidade>`: vínculo limitado, sem conceder formas nem limpar cooldowns. Preserva a semântica antiga; para preparar e trocar raça, usar kit.
- `reiatsu free/restore`, `cooldowns disable/restore/clear`: use os toggles e reset novos. Os atalhos antigos de clear/disable encerram ações conforme sua finalidade histórica.
- `mastery set`, `points add`, `skill set`, `reiatsu fill`, `asauchi give`, `inspect`: compatibilidade de ajustes finos; help/status orientam para o fluxo novo. Asauchi give continua um give explícito, distinto da seleção idempotente de kit.

Nenhum alias antigo é uma segunda implementação da troca racial. Remoção futura depende de migrar consumidores e confirmar com a equipe; a primeira entrega prioriza recuperar testes e manter compatibilidade.

## Validação e próximos testes

Servidor: sete novos GameTests de dispatcher cobrem ida/volta de kits, concessões idempotentes, custo restaurado, cooldown, permissões, inventário cheio/troca de item, reset de formação própria, persistência, aliases e status somente leitura. A suíte combinada atingiu 250 GameTests aprovados; regressões existentes: 158 aprovadas. Prova gráfica opt-in: runClient com benihimeQa, devCommandsQa, cinematicQaFast e qaAutoExit. Na execução final, os sete cenários do cliente passaram: Benihime/all, Quincy, Senbon/all, cheats off, início da entrada Benihime, reset com câmera de volta à primeira pessoa e ajuda. Evidências e hashes em [qa/comandos-20261028](qa/comandos-20261028/manifest.json). Cliente exit0. Multiplayer e outras perspectivas de restauração não foram demonstrados nesta rodada.

Teste humano: kit Benihime -> cheats all -> selada C -> Quincy -> kit Senbon -> três golpes -> kit Ryujin -> Higashi -> cheats off -> repetir ação verificando gasto e espera. Não homologar o arco ou combo Senbon pela correção dos comandos; as frentes AG precisam entregar suas próprias evidências.
