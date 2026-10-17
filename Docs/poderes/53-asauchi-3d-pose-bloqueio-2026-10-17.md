# Ciclo 53 — Asauchi 3D e pose de bloqueio das zanpakutō

Data: 17/10/2026. Branch `Feature-Poderes-bankais`, base limpa `1cc2729`. Usuário aprovou a animação Better Combat da Asauchi e pediu atualizar seu formato para o padrão das demais armas e acrescentar posição de bloqueio ao segurar X em todas as zanpakutō atuais. A atualização do modelo Asauchi foi expressamente autorizada neste pedido, sucedendo a restrição anterior de preservá-lo. A equipe ainda não publicou o commit Zaraki/Nozarashi.

## Entrega

- **Asauchi selada, Shikai e Bankai** passam de sprite plano para geometria 3D: cabo com faixas, pomo, guarda com contorno octogonal, habaki, lâmina curva segmentada, fio de um lado, dorso e ponta afunilada. Seguem proporção/transforms do padrão `ryujin_katana_handheld`. Paleta selada prateada, Shikai azul e Bankai vermelha, com cabo escuro. As texturas PNG anteriores permanecem no projeto e como referência de partícula; os volumes usam materiais vanilla. A mudança visual não aumenta alcance de combate.
- **Pose de guarda** comum a Asauchi, Ryūjin Jakka, Hyōrinmaru, Senbonzakura e Benihime, em todas as formas, inclusive variantes de lâmina de Senbonzakura. Identificação pela classe `ZanpakutoItem`; novas armas dessa classe herdam o suporte. Não depende de habilitar combos Better Combat para os demais kits.
- Espada elevada à frente do corpo, braço de apoio levantado quando a outra mão está vazia; espelhamento para canhotos e suporte à mão secundária. Se houver zanpakutō nas duas mãos, a principal recebe a pose. Transição suave de aproximadamente quatro ticks para entrar e sair; ao quebrar a postura, perder a arma ou entrar em estado incompatível, a pose é retirada imediatamente.
- Terceira pessoa usa uma camada PlayerAnimator de prioridade 3000, acima do ataque Better Combat 2000 da versão fixada, para braços/ajustes de item. Pernas e cabeça permanecem livres. Primeira pessoa mantém apresentação vanilla durante a guarda e recebe transformação de mão própria. O saque/guarda cosmético de troca de slot cede à pose defensiva enquanto esta estiver visível.
- O gatilho é **`GuardData.shownActive()`**, snapshot já sincronizado pelo servidor, para o jogador e observadores. Segurar a tecla sem elegibilidade não desenha uma falsa defesa. `shownBroken()` remove a pose. HUD/escudo e regras de redução, postura, custos, esquiva e dano continuam no contrato dos ciclos 39/40/52.

Não há dependência adicional. Better Combat 1.9.0 + PlayerAnimator + Cloth Config continuam nas versões do ciclo 52. Rede **2.18**, schema **8** mantidos: esta entrega acrescenta apresentação local e recursos de modelo, sem alterar payload ou save.

## Arquivos

- `client/model/ZanpakutoGuardPose.java`: registro da camada, seleção de mão, interpolação e primeira pessoa. Classes cliente restritas a `Dist.CLIENT`; mapa fraco de transições limpo na desconexão, sem referência ao jogador no valor.
- `client/BleachClient.java`: registro da fábrica durante setup do cliente.
- `client/model/ZanpakutoMotion.java`: precedência da guarda sobre o saque cosmético e limpeza das transições.
- `assets/bleachmod/models/item/asauchi_katana.json`: geometria compartilhada das três formas.
- Modelos `asauchi.json`, `asauchi_shikai.json`, `asauchi_bankai.json`: materiais, herança e overrides de forma.

## Verificação e instalação

`check build prepareCombatTestPack runGameTestServer --offline --console=plain`: **build aprovada, 99 regressões e 118 GameTests obrigatórios aprovados**. Log `build/cycle53-validation.log`. Nenhum teste novo foi criado para espelhar ângulos cosméticos; a suíte existente confere que a entrega continua carregando no servidor e preserva as regras de combate. Modelos JSON, heranças, referências de materiais e presença dos novos arquivos no JAR foram conferidos separadamente.

Pacote atualizado: `build/combat-test-pack/bleach-bettercombat-piloto.zip`, com os quatro JARs em `mods/`. Em instalação que já tem as mesmas três bibliotecas, basta substituir o JAR Bleach e reiniciar o cliente. Evitar JAR Bleach antigo duplicado. `prepareCombatTestPack` recria a pasta de mods; o ZIP é uma cópia local para esta entrega.

Conferência manual pendente: Asauchi nas três formas em inventário/chão/mão; combo em primeira/terceira pessoa; X parado/andando/agachado, soltar X, Guard Break, esquiva, troca de slot, mão secundária, canhoto, skin normal/slim; observar os cinco kits e variantes de Senbonzakura em outro cliente. Teste automatizado de servidor não homologa aparência nem observação com dois clientes reais. O aceite recebido nesta conversa refere-se à animação Asauchi do ciclo 52, não ainda ao modelo/pose novos.

## Continuidade do roadmap

O [roadmap do ciclo 51](51-roadmap-better-combat-ferramentas-refatoracao-2026-10-16.md) permanece vigente: estabilizar Better Combat e conciliar o commit Zaraki da equipe → autoria Blockbench/GeckoLib → VFX Photon/LDLib, áudio e desempenho → revisão Hyōrinmaru → revisão Ryūjin → Ishida. Mentor/Proficiências aguardam equipe; Aizen adiado e Gin removido. Bibliotecas futuras continuam planejadas, sem instalação automática nesta entrega. Comparar versões, perfis, modelos e executores quando o código do colega estiver disponível.
