# 100 — Integração poderes/design e resolução de conflitos com main

23/10/2026. Pedido explícito do usuário: revisar as duas branches, resolver os conflitos entre elas e com main, conferir commits locais pendentes. Trabalho realizado na Feature-Poderes-bankais; nenhum commit/push direto na main.

## Estado inicial verificado após fetch

- Feature-Poderes-bankais: 1791be5, igual à origin/Feature-Poderes-bankais, checkout limpo.
- codex/design-poderes-criaturas: 04bbea2, igual à origin/codex/design-poderes-criaturas, checkout separado limpo.
- origin/main: 3a37ca7. A referência main local estava em 50a2c44, 14 commits atrás, sem commits exclusivos.
- Nenhum commit alcançável pelas branches locais estava ausente de todas as referências remotas. As mudanças anteriormente locais já haviam sido publicadas pelo usuário antes desta rodada.

## Causas e resolução

1. Feature × design: conflito de conteúdo em Docs/poderes/00-CONTINUIDADE-CODEX.md. Preservados o ciclo 99 (fala/câmera), os antecedentes 93/94 e um resumo de integração da frente de design. A redação antiga da frente de design ainda tratava uma hipótese de travamento como fato e continha caracteres de controle; substituída por referência explícita à retificação do documento 96.
2. Feature × origin/main: conflito modify/delete em logs/debug.log. Main já havia removido os logs do versionamento e adicionado /logs/ ao .gitignore. Mantida essa decisão; conteúdo conflitante preservado como evidência em qa/cycle100/debug-log-preservado-antes-remocao.log.
3. Problema de portabilidade encontrado na revisão: tools/models/source/ice_hydra.bbmodel continha caminho absoluto da textura no checkout de design. A comparação estrutural encontrou apenas essa diferença em relação ao gerador. Gerador e fonte passam a usar caminho relativo ao arquivo Blockbench; geometria, PNG e animações de runtime não foram alterados por essa correção. `python tools/models/ice_hydra.py --check` passou após regeneração.

## Integração preservada

- Design: cabeças/pescoços, mandíbulas, pose de duas mãos e efeitos de gelo da Hyōrinmaru.
- Poderes: fala refeita do WAV original, preload, câmera dirigida e retorno orbital do ciclo 99.
- Main: correções de persistência/troca de dimensão, seleção de forma/cooldowns, HUD, build reproduzível, Hollows/boss e documentação da equipe, preservados pelo merge.
- Revisadas as combinações automáticas em build.gradle, CharacterData, StatusData, idiomas e executor de regressões. Rede 2.35 preservada; não há alteração oportunista da Zaraki.

## Validação e publicação

- Merge design → Feature registrado em d67f101.
- Check/build integrado passou: 146 cenários de regressão, 1m35s.
- Gerador da hidra: fonte/hierarquia/animações/textura/runtime coincidem.
- GameTests: todos os 223 testes obrigatórios aprovados, exit 0, 2m53s. Inclui as novas provas de empunhadura e os casos de Hollows/persistência. Cliente Forge real integrado: 10 cenários executados, exit 0, 2m51s. Capturas reais de close/empunhadura, revelação, baforada e retorno em qa/cycle100/integracao-marcos.jpg; áudio/câmera e pose/modelo coexistem. Isso não substitui homologação artística ou escuta do usuário.
- Destino da integração: concluir o merge de origin/main na Feature e avançar a branch de design por fast-forward para a mesma revisão. Publicação das duas será feita sem force-push; main não recebe commit direto. Conferir refs remotas e merge-tree após a publicação. O fetch antes da publicação confirmou bases remotas inalteradas.

## Limites

Integração e testes técnicos não representam nova homologação artística dos modelos. Multiplayer com observador e servidor dedicado com dois clientes não é comprovado por GameTests ou cliente singleplayer. A documentação importada de Docs/game-design é da equipe; documentos novos desta rodada ficam em Docs/poderes.
