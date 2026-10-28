# Prompt — Antigravity A: corrigir o protótipo Quincy

Implemente e demonstre as correções, por fases, sem expandir o kit. Leia AGENTS.md, skills locais pertinentes, Docs/poderes/00-CONTINUIDADE-CODEX.md, coordenacao-correcoes-kits-comandos-2026-10-28.md, plano-uryu-arqueiro-tradicional.md e o prompt anterior do Uryu. A coordenação nova define propriedade e substitui instruções conflitantes antigas de editar comandos.

Você não está sozinho no repositório. Use um checkout separado EXISTENTE e livre, sem nova pasta. Confira status, worktrees, fetch origin e hash-base; crie codex/quincy-correcao-integracao de origin/Feature-Poderes-bankais. Não usar main ou HEAD antigo como base. Se branch já existir, comparar, preservar e informar; sem reset/stash/clean/force-push. Não trocar branch no checkout do Codex ou da outra IA.

## Diagnóstico obrigatório

Vídeo: C:/Users/joaop/Downloads/Gravação/Gravação de Tela 2026-10-28 125756.mp4 (1m57,83s). Prints enviados pelo usuário mostram objeto preto/magenta enorme em ambas as perspectivas. Codex extraiu marcos locais em build/review-video-20261028 do checkout principal: são evidências auxiliares, não recursos de runtime. Se não puder abrir o vídeo, use os prints/quadros e registre a limitação; não finja assisti-lo. Não envie arquivos a serviços externos.

Prioridade zero: reparar o recurso realmente carregado. Investigar logs de bake/modelo/textura, herança e overrides de pulling, referências recursivas, chaves de textura, UVs e conteúdo empacotado. PNG existente no disco não prova modelo válido. Não cobrir o erro com partículas nem substituir o arco próprio por vanilla final. Demonstrar idle/carga/disparo, item no inventário, primeira/terceira pessoa e destro/canhoto, sem quadrado magenta e sem obstruir a mira. Depois calibrar duas mãos, corda e origem da flecha. Blockbench pode apoiar; cliente real é o aceite técnico visual.

## Fases

1. Uma flecha e arco legíveis, com comparativo antes/depois. Manter valores V0 e identidade Quincy.
2. Corrigir autorização: isQuincyActive atualmente aceita raça OU arco, e TechniqueService despacha antes da autorização comum. Possuir item não concede raça/fundamentos/técnicas. Mapear aquisição existente e separar acesso de QA administrativo do uso de produção; não improvisar uma campanha ou custo novo. Não resolver chamando autorização exclusiva de zanpakuto. Verificar também disparo básico, canalização, morte, espectador e bloqueios de ação pertinentes.
3. Corrigir Hirenkyaku: remover substituição da esquiva comum em DodgeService; Alt+direção mantém a esquiva já homologada. B deve receber direção de movimento atual, sem confiar em coordenadas de destino enviadas pelo cliente; sem input, recua. Atualmente executeSlot envia (0,0), por isso não segue WASD. Validar dados de rede, custos 8PE/10stamina, CD6s, deslocamento6, colisão por volume e apoio real, paredes finas, água/vegetação e precipícios. Não tornar mobilidade imune a controles/cenas. Stamina continua cobrada quando só o cheat Reiatsu está ativo.
4. H/N/C: provar mecânica com alvos controlados, não slimes aleatórios. Seele até3alvos únicos/parede; Licht12flechas/2s/teto8HP por alvo/alcance40; Sprenger cinco âncoras/piso local/centro36/R6/preparo1,5s/janela6s/18HP/até8alvos/35PE uma vez/CD20s. Área e preparação precisam ser visíveis e coerentes com hitbox; identificar o que falha, não redesenhar habilidade por suposição. Falta de recurso, parede, timeout, cancelamento, troca de kit, dimensão e logout devem limpar estados próprios. Não liberar dano através de blocos, resetar hurtTime ou destruir terreno.

Não editar BleachCommands ou implementar a alternância Quincy/Shinigami: Codex cuida disso. Fornecer interfaces mínimas de cancelamento/limpeza Quincy que o serviço de troca do Codex possa chamar. Se precisar alterar ResourcesData/PlayerData/CharacterData, entregar justificativa e diff proposto antes de sobrepor o trabalho comum. Usar autorização normal e cheats por caminhos explícitos, sem bypass de produção.

## Entrega

Uma fase só fica validada com teste adequado: check/build, GameTests dos casos positivos e negativos, e Forge real para apresentação. Testar configuração base sem Photon. Sem baixar bibliotecas desnecessárias. Coordenar janela de execução gráfica: apenas um cliente pesado por vez nesta máquina. Preservar outras Bankais e comandos novos do Codex. Provas compactas em Docs/poderes/qa/quincy-correcao-20261028; resumo de resultado no plano Quincy, incluindo pendências. Commit focado na branch própria, base/hash final, exit codes, roteiro humano e limitações. SEM MERGE para Feature/main. Não dizer homologado por build ou por geração de screenshots: inspecionar de fato.

## Evidência adicional do Codex — 28/10

Na execução real de comandos, o Forge registrou erros nos quatro modelos quincy_bow e pulling_0/1/2. Exemplo em build/devcommands-client-final.log, linha848: `JsonParseException: Invalid rotation -12.0 found, only -45/-22.5/0/22.5/45 allowed`, lançado por BlockElement.Deserializer.getAngle. Não é prova de PNG ausente: o bake foi rejeitado por rotação de elemento incompatível com o formato vanilla. Conferir todos os elementos e o gerador; corrigir a exportação/modelagem apropriada sem simplesmente trocar todos os ângulos e destruir a silhueta. JSON sintaticamente válido não valida as restrições do loader. Acrescentar verificação que pegue este caso e comprovar bake sem erro no cliente, além de textura e empunhadura. Este erro foi mantido nesta branch para não sobrepor sua propriedade do arco.

A frente Codex implementou DevKitService para troca administrativa e cheats; `allEnabled(ServerPlayer)` identifica o preset de QA de sessão. Não refazer inicialização destrutiva de PlayerData em comandos. O cancelamento de projéteis/ações Quincy deve expor uma interface por dono para integração posterior com reset/troca; não editar o serviço de comandos em paralelo.
