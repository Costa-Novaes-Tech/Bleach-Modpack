# 149 — Integração das duas frentes para teste do usuário

27/10/2026. Merge solicitado explicitamente pelo usuário após auditoria148. Branch destino Feature-Poderes-bankais. Documentação/provas locais preservadas em f3e954d. Senbon834ae74 integrado por ec97869; Higashibe46530 integrado por18edea9. Sem conflitos manuais; MvpRegressionTest combinado automaticamente pelo Git. Main e branches de origem preservadas; sem push.

## O que está nesta versão

Senbon: correção de orientação da katanaS1, assets anteriores deS2/S3 e pacotes deQA. Higashi: confirmação pós-dano, hint/rollback/timeout, vítima única/variante alada, pausa20ticks e cenáriosQA explícitos. Preservadas críticas148: comparativos Senbon com fases distintas não homologam todas vistas; pré-câmera Higashi ainda precisa avaliar continuidade sem desaparecimento. KatanaHG continua sem aprovação. Merge não transforma esses itens emPASS.

## Validação integrada

Executar check/build e runGameTestServer offline sequencialmente, Java17, run-gametest/base descartável. Logs em build/integration149. Resultado final registrado abaixo após conclusão; não reutilizar resultados das branches como resultado integrado. Não abrir cliente por conta desta integração: o usuário fará o teste visual.

## Bateria curta do usuário

1. Senbon: começar por UM clique com câmera de perfil. Ponta deve subir junto ao ombro, atravessar a frente na diagonal e terminar baixa à esquerda. Verificar cabo na palma. Depois repetir de frente e como canhoto; conferir selada/Shikai/Bankai sem atribuir diferença de perspectiva à mesma fase automaticamente.
2. SeS1 funcionar, registrar poucos ataques encadeados para avaliar continuidade. Golpes2/3 ainda não estão homologados por esta rodada; registrar o que não corresponde ao retorno horizontal e estocada, sem considerar o pacoteS1 aprovação do combo inteiro. Não redesenhar partículas neste teste.
3. Higashi dano normal letal acima7%: usar mob vanilla e Hollow alado. Esperado: morte verdadeira, uma representação durante execução, variante/asa preservada e ausência de cadáver vermelho concorrente. Observar a transição antes da câmera: alvo some por algum instante? Há flash? Registrar também se replica reaparece ou desaparece seca.
4. Higashi≤7%, sobrevivente e vazio: threshold deve executar; sobrevivente e erro não abrem câmera nem deixam alvo oculto/preso. Registre sumiço transitório de sobrevivente, se houver; não é efeito aceitável por omissão.
5. CorteHG: recolha no quadril, diagonal ascendente,1segundo após fim e ignição/cinzas. Esse gesto permanece pendente: avaliar katana real, não somente faíscas. Confirmar retorno de câmera/HUD/controle; repetir uma vez perto de parede se quiser conferir composição.
6. Preservação: transformaçãoRyūjin aprovada e Hyōrinmaru não devem regredir. Não é necessário repetir repertório inteiro para essa integração. Multiplayer só considerado validado se executado com observador/servidor próprios.

Usar mundo de teste. Identificar forma/mão, mob e qual ação no vídeo. O caso que falhar deve ter pequeno trecho antes/depois, sem necessidade de gravação longa. Distribuir mesmoJAR em cliente/servidor; não misturar compilações antigas.

## Resultado final do conjunto integrado

check build offline: exit0, BUILD SUCCESSFUL2m12s,150regressões aprovadas. runGameTestServer offline: exit0, BUILD SUCCESSFUL3m31s,237GameTests obrigatórios aprovados. git diff --check sem erros. JAR build/libs/bleachmod-0.2.0.jar,15.940.215bytes, SHA256 `5bf0fdb4a82eb3dfb845adc4f86d22da77a159d03a34752c2946ac6be9843c12`. [Validação e proveniência](qa/cycle149/validation.json). Summaries de logs no mesmo diretório; logs originais em build/integration149. Sem novo teste gráfico: aceite visual do usuário pendente. Logs locais gerados foram mantidos fora dos commits.
