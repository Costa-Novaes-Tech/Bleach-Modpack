# Ciclo 27 — Migração dos efeitos dos slots da Hyōrinmaru

Data: 11/10/2026. Branch: Feature-Poderes-bankais. Migrações autorizadas pelo usuário após o ciclo 26.

## Resultado e escopo

Os dois cortes, a barreira, a Zona Glacial, o impacto do controle selado e a trilha do dragão usam a biblioteca visual comum. Mantêm as partículas, cores, dispersões, posição, frequência e volumes anteriores. Não foram aplicados automaticamente os perfis experimentais da bancada nem importados assets externos.

Não se alteram dano, alvos, lentidão/hipotermia, resistência, knockback, alcance de combate, reiatsu, cooldown, maestria, desbloqueios, modelos ou texturas. O clima e sua emissão atmosférica permanecem em BlizzardWeatherData. A lógica de colocação/restauração da neve continua em GlacialSnowData e HyorinmaruTechniqueService. Aizen e aquisição narrativa seguem futuros.

## Arquivos e implementação, passo a passo

1. `src/main/java/com/bleachmod/common/visual/VisualFrame.java`: acrescenta `technique(owner, anchor, limit)`. Orçamento explícito de 1 a 1.056, dispersões finitas de até seis blocos por eixo e velocidade de até um. O limite maior é necessário para preservar o corte frontal Bankai, sem criar vários frames para contornar o orçamento. Construtor da bancada mantém teto 48/dispersão um; `nearby` do ciclo 26 mantém teto 160/dispersão um.
2. O frame de técnica guarda a origem da manifestação: jogador no lançamento do corte, origem fixa do campo, posição atual do dragão. Cada ponto deve ficar até 32 blocos dessa origem. A distância do receptor continua sendo até 32 blocos do ponto emitido, na mesma dimensão. Mover o conjurador não apaga a visualização de um campo fixo ainda válido. Morte, espectador ou mudança de dimensão da fonte impedem novas emissões naquele frame; chunks ausentes nunca são carregados.
3. `src/main/java/com/bleachmod/common/visual/IceTechniqueVisuals.java`: reúne os perfis aprovados. `Cut` valida slot/forma e fornece alcance, abertura e orçamento. Esses mesmos alcance/abertura são usados na seleção de alvos, evitando divergência entre descrição visual e geometria do corte. As posições avançam de meio em meio bloco; cada ponto mistura snowflake e dust azul (RGB 0,45/0,85/1), preservando escala 1/1,6.
4. `fieldPoint` mantém a barreira transversal e a distribuição espiral no interior do disco da Zona Glacial, usando ângulo áureo, fase temporal e raio proporcional à raiz da fração da amostra. Neve não fica restrita à borda. A altura adicional da Bankai e a dispersão vertical dois permanecem.
5. `src/main/java/com/bleachmod/common/technique/HyorinmaruTechniqueService.java`: substitui exclusivamente os loops/chamadas de partículas pelos perfis comuns. Custos, timers, condições de cancelamento, sons, dano/controle e neve no chão seguem suas implementações anteriores. Emite campos a cada quatro ticks; aplica controle/dano a cada dez. Barreira dura 60 ticks; Zona Glacial 80.
6. `src/main/java/com/bleachmod/entity/IceDragonEntity.java`: delega a trilha para `dragon`, com dez snowflakes por tick válido. Mantém Phantom sem IA, tamanho, velocidade, duração, descarte e caráter cosmético. O dano do corte continua separado da criatura.
7. `src/test/java/com/bleachmod/MvpRegressionTest.java`: verifica alcances/orçamentos e distribuição da Zona Glacial entre centro e periferia, além de rejeições dos perfis.
8. `src/main/java/com/bleachmod/gametest/AssetBenchGameTests.java`: testa densidade completa de todas as formas, orçamento compartilhado, rejeição de excesso, origem fixa com jogador deslocado, limites de dispersão, privacidade da bancada, ausência de mutação dos dados e trilha com conjurador inválido. A suíte anterior verifica combate, custos, aliados, cancelamento e clima.

## Valores preservados

| Efeito | Alcance ou raio | Partículas solicitadas por emissão completa |
| --- | --- | --- |
| Cortes selados, ambos slots | 6 | 120 |
| Frontal Shikai | 10 | 320 |
| Aberto Shikai | 6 | 192 |
| Frontal Bankai | 24 | 1.056 |
| Aberto Bankai | 8 | 352 |
| Barreira Shikai/Bankai | geometria transversal anterior | 128/224 |
| Zona Glacial Shikai/Bankai | 10/30 | 256/896 |
| Controle selado, impacto | seleção de alvo até 5 | 45 |
| Trilha do dragão | percurso anterior | 10 por tick |

Frontal usa meia abertura 15 graus; aberto 35. Contagens são solicitações por frame em chunks disponíveis, não partículas garantidas na tela. Cada receptor multiplica o tráfego; orçamento por frame não é limitador global do servidor. Não se afirma melhoria de FPS. Os limites preservam densidade homologada e restringem erros de emissão. Não há novas sessões, entidades auxiliares ou dados persistidos pela biblioteca.

## Homologação em jogo

1. Teste os dois cortes na selada, Shikai e Bankai; compare alcance, abertura, cor, densidade, dano e custo com o comportamento anterior.
2. Teste barreira e Zona Glacial nas duas liberações. Confirme partículas no interior da zona, controle/dano, neve temporária e restauração do terreno. Afaste-se da origem enquanto o campo existir para confirmar sua visualização independente do deslocamento.
3. Teste o dragão: mesmo tamanho/percurso e trilha. Repita lançamentos; apenas a criatura atual deve permanecer. Troque a espada, encerre a forma, morra e troque de dimensão para confirmar o descarte anterior.
4. Com segundo jogador, compare visão próxima, afastamento acima de 32 blocos dos pontos de emissão e outra dimensão. Reconfirme bancada privada e `assets stop`.
5. Confirme cooldown/custo, regras de aliados, evolução de maestria e restauração do clima. Compare FPS nas mesmas condições; homologação visual e desempenho dependem deste teste no cliente.

## Validação

check, build e runGameTestServer offline aprovados: 65 cenários de regressão e 53 GameTests obrigatórios. Log local: `build/cycle27-validation.log`. git diff --check aprovado. Documentação deste ciclo restrita a Docs/poderes; modelos, texturas e demais documentos intactos. JAR atualizado: `build/libs/bleachmod-0.2.0.jar`. Homologação visual/FPS não é afirmada pelos testes automatizados.

## Continuidade

Após homologação, revisar o treino de maestria e a clareza do feedback de desbloqueios no HUD, com dados de testes para identificar ajustes concretos. Não inventar missões/provas narrativas antes das definições da equipe. Refinamentos artísticos novos permanecem escolhas explícitas, independentes desta migração técnica.
