# 131 — Homologação Visual do Senbonzakura Shūkei: Hakuteiken em Múltiplos Cenários

- **Data**: 04/11/2026
- **Branch**: `codex/senbon-hakuteiken-blockbench`
- **Autor**: Antigravity Design Lab
- **Status**: Homologado com 100% de Aprovação (4/4 Passos QA + 158 Regressões Unitárias)

---

## 1. Sumário Executivo

A partir da análise forense das 7 capturas de tela fornecidas pelo usuário, implementamos e validamos a refatoração completa do **Senbonzakura Shūkei: Hakuteiken** no Minecraft 1.20.1 Forge com GeckoLib 4.8.4 e PlayerAnimator.

Todos os 5 defeitos visuais identificados foram resolvidos:
1. **Gap Físico Dorsal Eliminado**: O ponto de articulação do `wing_nexus` e as raízes das asas foram recuados para $Z \in [1.85, 2.15]$ voxels, penetrando diretamente nas escápulas do Shinigami. O pivô de inclinação do tronco no client foi travado em $(0.0, 1.35, 0.125)$, garantindo que mesmo durante saltos, mergulhos rasantes ou agachamento, as asas permaneçam 100% fundidas ao dorso.
2. **Torção Diagonal em Yaw Eliminada**: As animações no Blockbench foram reconstruídas com rotação em $Y$ estritamente zerada ($ry = 0.0^\circ$) nas raízes e no nexus. No runtime Java (`SenbonHakuteikenVisuals.java`), a orientação da asa é derivada estritamente de `living.yBodyRot`, mantendo a envergadura perfeitamente perpendicular à linha dos ombros na visão de topo (*Top-down*).
3. **Volume 3D Arqueado das Asas**: A curva paramétrica das asas agora possui curvatura côncava em $Z$ ($Z = 1.85 \to 3.80 \to 2.05$), criando um berço volumétrico tridimensional em vez de uma lâmina/fita plana 1D.
4. **Penas Afiladas Orgânicas**: As penas de voo foram divididas em dois segmentos escalonados (haste primária com $68\%$ do comprimento e ponta afilada afunilada com $32\%$ do comprimento e $58\%$ da largura). Isso eliminou o corte reto horizontal estilo "pente de ripas de madeira" na visão traseira.
5. **Postura de Combate Chūdan-no-kamae Contínua**: Corrigido o ciclo de vida do PlayerAnimator. O layer de animação agora permanece ativo continuamente durante todo o Modo Alado (90 segundos), mantendo o jogador com a katana empunhada para a frente na altura do tórax com as duas mãos, eliminando o boneco inerte de Steve (*vanilla idle*).

---

## 2. Validação em Múltiplos Cenários

Conforme solicitado, a suíte de testes cinematográficos automatizados (`SenbonzakuraHakuteikenQaSequence.java`) foi expandida para cobrir 3 cenários distintos de ambiente e interferência:

1. **Cenário A: Campo Aberto (Passes 0 e 1)**:
   - Validação da visão traseira (*Back*) e lateral (*Side Profile*).
   - Salto vertical autoritativo de 5.0m, pairar majestoso de 1.0s, rasante diagonal de 16 blocos e aterrissagem com tapete de pétalas de 7m de raio.
2. **Cenário B: Câmara Encoberta / Ambiente Fechado (Pass 2 - Frontal)**:
   - Estrutura de pedra maciça com teto a $Y = -52$ (8 blocos acima do chão) e colunas laterais de blocos de pedra (*Stone Bricks*) a $X = \pm 5$.
   - Entidades de interferência no corredor: Zombie e Skeleton gerados nas proximidades para testar colisão e oclusão de visão.
   - Resultado: A habilidade sobe até 5.0m sem colidir ou travar no teto de pedra, e o rasante conclui perfeitamente com a câmera interna enquadrando a postura marcial frontal.
3. **Cenário C: Arena com Múltiplas Entidades (Pass 3 - Top-Down)**:
   - 3 entidades hostis adicionais flanqueando a área de impacto.
   - Visão de topo zenital validando a simetria horizontal estrita das asas e a cobertura do mar de pétalas de 7m no solo.

---

## 3. Evidências de QA e Métricas

### Regressões Unitárias (`./gradlew check --console=plain`)
- **Resultado**: `BUILD SUCCESSFUL in 53s`
- **Status**: `PASS: 158 regression scenarios` (100% aprovados, zero falhas).

### Execução em Jogo Real (`./gradlew runClient ...`)
- **Resultado**: `BUILD SUCCESSFUL in 1m 31s`
- **Resultados consolidados em `results.txt`**:
  - `pass0-back PASS bones=11/11 playerAnimLoaded=true layerRegistered=true leapY=5.00 distMoved=16.00 damageDealt=30.0 transform=true mayfly=true reach=6.05 offBonus=0.35 failure=null`
  - `pass1-side PASS bones=11/11 playerAnimLoaded=true layerRegistered=true leapY=5.00 distMoved=16.00 damageDealt=30.0 transform=true mayfly=true reach=6.05 offBonus=0.35 failure=null`
  - `pass2-front PASS bones=11/11 playerAnimLoaded=true layerRegistered=true leapY=5.00 distMoved=16.00 damageDealt=30.0 transform=true mayfly=true reach=6.05 offBonus=0.35 failure=null`
  - `pass3-top PASS bones=11/11 playerAnimLoaded=true layerRegistered=true leapY=5.00 distMoved=16.00 damageDealt=30.0 transform=true mayfly=true reach=6.05 offBonus=0.35 failure=null`
