# Ciclo 35 — Ajuste final de escala e inclinação da bainha

Data: 13/10/2026. Branch: Feature-Poderes-bankais. Após teste visual do ciclo 34, o usuário pediu aumento aproximado de 15% no tamanho/grossura e mais 15–20 graus de inclinação para baixo.

## Modificação e lógica

`src/main/java/com/bleachmod/client/model/ZanpakutoSheathLayer.java`: escala uniforme passa de 0,85 para 0,9775 (0,85 × 1,15). Comprimento, espessura, cabo e acabamentos aumentam 15% juntos, conservando as proporções e a curva afunilada aprovadas. Rotação X passa de -8 para -26 graus, adicionando 18 graus para baixo à ponta em Z positivo. A pequena curvatura interna permanece, portanto a inclinação local da ponta é diferente da rotação da raiz.

O lado corrigido da cintura, espelhamento para canhotos, cores por identidade, presença no inventário, bainha vazia empunhada e animação de saque/guarda permanecem. Espadas na mão, stamina, atributos, custos e pacotes não mudam. Protocolo 2.9 e schema 7 mantidos. Documentação modificada somente em Docs/poderes.

## Validação

`gradlew check build --offline --console=plain`; build concluída com sucesso e 73 cenários de regressão aprovados. Log: `build/cycle35-validation.log`; JAR: `build/libs/bleachmod-0.2.0.jar`. Ajuste exclusivamente visual: não cria testes que repetem constantes da implementação. Homologação de proporção/encaixe depende do retorno visual do usuário. GameTests de lógica do ciclo 33 não foram reexecutados nesta etapa.

## Conferência no jogo

1. Atualize a build e compare bainha com espada empunhada em F5: aumento moderado de comprimento/grossura, curva conservada e ponta mais baixa.
2. Confira vistas frontal, lateral e traseira com Asauchi, Ryujin e Hyōrinmaru. Repita agachamento e armadura Bankai para conferir sobreposição.
3. Saque/guarde, teste mão secundária e canhotos. Não deve mudar o lado corrigido ou deixar a animação presa.

Após aprovação visual, manter a recomendação de próxima feature: esquiva curta com stamina.