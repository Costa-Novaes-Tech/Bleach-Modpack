# 151 — Correção Hakuteiken v3: Orientação das Asas, Better Combat e Mar de Pétalas

**Data**: 05 de Novembro de 2026  
**Branch de Trabalho**: `codex/senbon-hakuteiken-blockbench`  
**Autorização**: Solicitado e autorizado pelo usuário em 05/11/2026  
**Contexto**: Análise de gravação em vídeo (`Gravação de Tela 2026-11-04 200919.mp4`), diagnóstico das falhas relatadas e implementação do plano estratégico v3.

---

## 1. Diagnóstico das Quatro Falhas em Jogo

A partir do teste prático gravado em vídeo pelo usuário e das capturas de tela fornecidas, identificamos com precisão as quatro causas-raiz:

1. **Asas Invertidas no Eixo Y (De cabeça para baixo)**:
   - **Sintoma**: As asas brancas apareciam com o nexo vertebral na altura do quadril/cintura e as penas estendidas para cima no céu, em formato de leque invertido.
   - **Causa**: O pipeline do `LivingEntityRenderer` no Minecraft aplica `scale(-1.0F, -1.0F, 1.0F)` para modelos vanilla (onde $+Y$ aponta para baixo). O `GeoObjectRenderer` do GeckoLib renderiza modelos Bedrock/Blockbench esperando $+Y$ apontando para o céu. Como a inversão de escala nunca era desfeita após o `body.translateAndRotate(pose)`, o GeckoLib renderizava todo o modelo alado invertido em $Y$ e $X$.
   - **Correção**: Inserido `pose.scale(-1.0F, -1.0F, 1.0F)` logo após `body.translateAndRotate(pose)`, cancelando a escala invertida e restaurando o eixo $+Y$ natural. O arco superior passou de $Y=399.0$ (abaixo da cintura) para $Y=170.0$ (ergue-se acima dos ombros até $Y=23$).

2. **Travamento de Membros e Supressão do Better Combat**:
   - **Sintoma**: Ao ativar a habilidade [C], os braços e pernas ficavam fixos numa pose estática contínua, sem voltar ao neutro e impedindo os combos de espada do Better Combat.
   - **Causa**: O `DirectPoseAnimation` registrado com prioridade `3950` mantinha `isActive() == true` contínuo durante `pattern == 2` (modo alado de 90 segundos). A camada de animação sobrepunha todos os canais de locomoção e ataque.
   - **Correção**: Condição atualizada para `s != null && s.pattern != 2 && !ZanpakutoReleaseAnimation.active(player)`. Durante o modo alado (`pattern == 2`), o driver desliga-se completamente (`isActive() == false`), devolvendo 100% do controle das articulações à movimentação vanilla e aos combos do Better Combat.

3. **Ausência do Mar de Pétalas no Chão**:
   - **Sintoma**: Nenhum carpete persistente de pétalas cobria o solo durante a forma Shūkei.
   - **Causa**: A chamada com formato `FIELD` só ocorria na transição de impacto e não era mantida no loop periódico do serviço de técnicas.
   - **Correção**: 
     - Adicionada emissão periódica de `s.draw(FIELD, p.position(), f, SenbonPolicy.HAKU_IMPACT_RADIUS, 1.0, 3)` a cada 10 ticks em `SenbonzakuraTechniqueService.java`.
     - Atualizado `SenbonGardenFloor.java` para que o padrão `3` mantenha o florescimento contínuo (`f.bloom = 1`, `f.finished = true`, `f.until = now + 25`).
     - Adicionada emissão de partículas suaves descendentes em `SenbonHakuteikenVisuals.java` (`vy = -0.045`) para simular pétalas caindo das asas e assentando no carpete.

4. **Isolamento Estrito das Demais Bankais**:
   - **Garantia**: Alterações 100% restritas a 3 classes do subsistema Senbonzakura (`SenbonHakuteikenVisuals`, `SenbonGardenFloor`, `SenbonzakuraTechniqueService`).
   - Nenhuma linha de código referente a Ryūjin Jakka, Hyōrinmaru, Nozarashi/Zaraki, Benihime ou Quincy foi modificada.

---

## 2. Validação e Testes Automatizados

1. **Compilação**: `./gradlew compileJava` concluído com sucesso (`BUILD SUCCESSFUL`).
2. **Suíte de Regressão Global**: `./gradlew check` aprovou todos os **158 cenários** sem falhas:
   - ryujin (10 cenas e gestos de fogo)
   - hyorinmaru (hidras e transformações)
   - nozarashi (kit 12 slots e animações berserk)
   - benihime (esgrima better combat e Hadō 91)
   - quincy (arco kojaku, seele schneider, licht regen, hirenkyaku, sprenger)
3. **QA em Jogo (`./gradlew runClient`)**:
   - Executadas as 4 perspectivas de câmera (`pass0-back`, `pass1-side`, `pass2-front`, `pass3-top`).
   - Capturadas 8 fases por ângulo: `t06-leap`, `t12-hover`, `t20-hover`, `t28-hover`, `t34-rasante`, `t40-impact`, `t46-transform` e `t50-attack`.
   - 11/11 ossos validados, leap 5.0m, dash 16.0m e dano 30.0 HP confirmados no harness.

---

## 3. Estado dos Arquivos no Repositório

- `src/main/java/com/bleachmod/client/model/SenbonHakuteikenVisuals.java`: Matriz de rotação corrigida, Better Combat desbloqueado, partículas de queda ativadas.
- `src/main/java/com/bleachmod/client/particle/SenbonGardenFloor.java`: Suporte a padrão 3 persistente de chão.
- `src/main/java/com/bleachmod/common/technique/SenbonzakuraTechniqueService.java`: Emissão periódica FIELD ativada no Hakuteiken.
- `src/main/java/com/bleachmod/client/cinematic/SenbonzakuraHakuteikenQaSequence.java`: Captura automatizada da fase de ataque `t50-attack`.
