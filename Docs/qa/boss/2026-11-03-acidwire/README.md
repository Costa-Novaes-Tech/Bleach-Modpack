# Acidwire — integração e QA do boss de Orihime

Data: 03/11/2026. Branch: `feat/jigui/quests`, referência `33e341d3`. As alterações anteriores da segunda quest foram preservadas; sem checkout, commit ou publicação.

Fonte fornecida pelo usuário: `C:/Users/jguic/Downloads/Acidwire.bbmodel`, SHA-256 `2aae78620af0b1dba9b0fdd389cb7f930759b8b09ee64cfec957cbdbe9be9c02`. Inspeção do JSON: 287 elementos, uma textura e três animações autoradas: `animation.acidwire.idle` (2 s, loop), `animation.acidwire.walk` (1,2 s, loop) e `animation.acidwire.attack` (1 s, uma execução).

Uma cópia byte a byte do original fica em `tools/models/source/acidwire.bbmodel`; o exportador é `tools/models/acidwire.py`, com validação por `--check`. Referências técnicas oficiais usadas pelo coordenador: [exportador Bedrock do Blockbench](https://github.com/JannisX11/blockbench/blob/master/js/formats/bedrock/bedrock.js), para espelhamento X, pivôs/rotações e faces UV superior/inferior, e [exportador de animação](https://github.com/JannisX11/blockbench/blob/master/js/formats/bedrock/bedrock_animation.js), para posição X e rotações X/Y. A referência narrativa [retrospectiva oficial do arco Substituto](https://pierrot.jp/title/bleach/playback_01.html), reconferida pelo coordenador em 03/11/2026, sustenta o caso do irmão de Orihime e sua resolução; não comprova dano, alcance ou repertório de combate do mod. Nome/modelo vêm do arquivo fornecido; movimento físico e números são adaptações provisórias.

Plataforma do checkout: Minecraft Java 1.20.1, Forge 47.4.10, Java 17, GeckoLib 4. Perfil base. O boss usa o ID canônico reservado `bleachmod:story_orihime_brother`; não herda o HollowBoss nem suas capacidades. Atributos são provisórios de integração e exigem calibração de gameplay: 160 HP, dano base 6, velocidade 0,25. O golpe tem duração de 20 ticks (1 s nominal), contato no tick 11 (0,55 s nominal), alcance de 2,5 blocos e cooldown de 36 ticks (1,8 s nominal). Não há fases, projéteis ou capacidades de outros bosses adicionadas nesta integração.

## Cenários preparados

`AcidwireGameTests` verifica com entidades reais: o golpe só causa dano no marco `WINDUP_TICKS`, não reinicia durante cooldown, pode ser evitado por afastamento e pode ser cancelado. A prova da quest inicia a segunda Rukia, verifica isolamento do alvo por proprietário, reinicia somente o combate após morte do jogador, rejeita tentativa antiga, recupera a morte ambiental do boss, mata o Acidwire real por dano do proprietário e exige a conversa final para conceder exatamente 100 XP uma vez e liberar Chad.

`AcidwireQaBootstrap` cria automaticamente um mundo plano novo em `run/acidwire-qa` com `-PacidwireQa=true`. Captura frente/lado/costas, caminhada e os marcos de preparação/impacto/recuperação do golpe. Em seguida executa a quest real por mensagens do cliente: aceite, introdução, boss, morte e respawn do jogador, retomada, morte ambiental do boss, nova tentativa, vitória, retorno e recompensa/liberação da missão seguinte. A demonstração usa a entidade real e dano real: não fabrica o progresso da vitória. Capturas de framebuffer incluem ticks e estado do ataque no manifesto.

| Verificação | Estado |
|---|---|
| Fonte Blockbench e três animações | PASSOU na verificação estrutural `python tools/models/acidwire.py --check`: 23 bones, 287 cubos, 3 clips, textura 512×512 |
| Compilação/check/build | PASSOU no servidor e na execução final com cliente, ambos exit code 0 |
| Regressões Java | PASSOU: 165 cenários no servidor e na execução final com cliente |
| GameTests do servidor Forge | PASSOU: todos os 238 testes obrigatórios, incluindo os 3 novos de Acidwire; execução encerrada com exit code 0 |
| Cliente gráfico, modelo e animações | PASSOU no recorte de importação e marcos observados: 15 capturas reais |
| Fluxo real completo da segunda quest | PASSOU: entidade real, morte/retomada, morte ambiental/retomada, vitória do proprietário, conversa final, 100 XP uma vez e liberação de Chad |
| Multiplayer com dois clientes e Photon | PENDENTE; fora do recorte inicial |

Mundos e artefatos de QA devem permanecer separados dos saves pessoais. Resultados abaixo distinguem funcionamento técnico e observação visual de aceite artístico ou calibração de dificuldade.


Primeira tentativa de compilação detectou sete chamadas sem supplier no `LazyOptional.orElseThrow`; as chamadas dos novos testes/bootstrap foram corrigidas para a API Forge disponível. A execução completa subsequente compilou e passou os testes após a correção. As vítimas dos testes de contato são Husks sem IA, imunes à queima por luz solar, evitando dano ambiental concorrente com o marco de ataque.

O servidor registrou explicitamente todos os 238 testes obrigatórios passando em `run/acidwire-server-validation.log` em 03/11/2026, às 20:33:53 (America/Sao_Paulo). O código executável do boss é o final desta revisão. A compilação desta execução havia ocorrido antes da troca do fixture Zombie para Husk: os testes passaram mesmo com o fixture anterior, e a compilação/check/build final do cliente incorporou a alteração isolada do fixture para Husk. Isso não altera IA, ataque, assets ou progressão de gameplay.

Limitação independente relatada pelo coordenador: o verificador legado `ice_hydra.py --check` aponta assets/source antigos divergentes, preexistentes e fora deste recorte; arquivos de Ice Hydra não foram alterados para forçar sucesso. A inspeção/importação de Acidwire usa o exportador determinístico do projeto; não houve sessão MCP de Blockbench nesta rodada.


## Execuções e evidências

Comandos executados pelo coordenador, Java Eclipse Adoptium 17.0.20.1+1 amd64, perfil base e caches locais:

```powershell
.\gradlew.bat check build runGameTestServer --offline -PacidwireQa=true --console=plain
.\gradlew.bat check build runClient --offline -PacidwireQa=true --console=plain
```

- Servidor: **238 testes obrigatórios passaram**, `BUILD SUCCESSFUL in 11m 2s`, exit code 0. [Log completo](server-validation.log).
- Cliente/check/build final: **165 regressões passaram**, `Acidwire QA complete` às 20:43:30 de 03/11/2026 em America/Sao_Paulo, `BUILD SUCCESSFUL in 4m 26s`, exit code 0. [Log completo](client-validation.log).
- Mundo descartável: `run/acidwire-qa/saves/Acidwire-QA-1793749351372`. As 15 imagens vieram de `run/acidwire-qa/evidence/Acidwire-QA-1793749351372/screenshots`, por `Minecraft Screenshot.grab`, framebuffer 1280×720. [Manifesto de ticks e estado de ataque](frames.jsonl).

## Inspeção visual

Todas as 15 capturas foram inspecionadas. Modelo, textura, máscara, braços e cauda aparecem no cliente sem textura ausente. A captura [03](screenshots/03-acidwire-rear-idle.png) mostra a **frente** com a máscara, [02](screenshots/02-acidwire-side-idle.png) mostra o perfil e [01](screenshots/01-acidwire-front-idle.png) mostra as **costas**. Os nomes front/rear de 01/03 ficaram invertidos pela orientação efetiva do preview; os arquivos originais e o manifesto foram preservados, sem manipular as imagens para corrigir rótulos. Durante o golpe o corpo está voltado para a vítima corretamente.

A [caminhada](screenshots/04-acidwire-walk.png) usa navegação real e apresenta pose diferente do repouso. Parte da cauda fica sob o painel lateral nessa captura; o perfil 02 mostra sua extensão. Os marcos do golpe mostram [preparação](screenshots/05-acidwire-claw-windup.png) no tick 3, [contato](screenshots/06-acidwire-claw-impact.png) no tick 11 e [recuperação](screenshots/07-acidwire-claw-recovery.png) no tick 17. O contato real é visível pela reação vermelha da vítima Husk; o timing foi validado também no GameTest. As imagens não substituem avaliação contínua da fluidez ou aceite artístico pelo autor do modelo.

A quest foi demonstrada com [introdução](screenshots/08-quest-introduction.png), [Acidwire real criado pelo encontro](screenshots/09-real-acidwire-quest-boss.png), [checkpoint após morte do jogador](screenshots/10-player-respawn-combat-checkpoint.png), [recuperação de morte ambiental do boss](screenshots/11-environmental-death-retry.png), [retorno após vitória](screenshots/12-victory-return-stage.png), [conversa final](screenshots/13-final-dialogue.png) e [recompensa marcada como recebida](screenshots/15-completed-dialogue-reward-claimed.png). Asserções no servidor confirmaram exatamente 100 XP, ausência de duplicação ao repetir a conversa/claim e liberação de Chad somente depois da conclusão.

O cliente de QA usa modo criativo e aplicação automatizada de dano real ao boss, identificado pelo proprietário da tentativa; a vitória não foi fabricada alterando progresso. Isso comprova integração e transições, mas não equivale a um playtest manual de dificuldade em sobrevivência. Alcance/dano/cooldown continuam provisórios. Multiplayer com dois clientes, Photon, medição de desempenho, áudio percebido e gravação contínua de movimento permanecem pendentes.
