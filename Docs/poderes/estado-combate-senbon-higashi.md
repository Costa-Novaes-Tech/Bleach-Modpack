# Estado vigente — combate Senbonzakura e execução Higashi

Atualização: 28/10/2026. Branch Feature-Poderes-bankais. Este documento será atualizado por assunto, evitando mais um relatório completo para cada rodada. Antecedentes:137–149; direção137 continua como referência artística. Não substitui o GDD nem o contrato118.

**Integração28/10:** Senbon3969cbf incorporado no merge1715d26. Conferência de capturas reais e SHA256 do bruto; correção de unidades do relatório registrada ao final. Suíte integrada com Quincy/HUD:158regressões e258GameTests. Arte final do combo continua para teste humano. Higashi não foi reanimada nesta rodada.

## Escopo e decisões preservadas

Senbon: uma mão; S1 diagonal descendente, S2 retorno horizontal no peito, S3 estocada central, repetição1→2→3. Não redesenhar pétalas/ultimate nesta correção. Hyōrinmaru, Asauchi, Benihime, Zaraki e transformação Ryūjin aprovadas permanecem preservadas.

Higashi: o usuário confirmou em27/10 que o defeito atual é a coreografia/espada; o restante funciona. Alteração restrita ao replay visual confirmado: recolha no quadril, corte ascendente, final alto e recuperação. Dano, limiar7%, morte letal normal, confirmação, câmera, vítima única, cinzas e relógio de ignição inalterados. SLASH_END16, IGNITE36: pausa20ticks/1segundo depois do corte.

## Diagnóstico confirmado e erros que não devem voltar

1. O QA anterior da Senbon acionava somenteS1. Manter tecla de ataque em30..100ticks no modo de combo comprovou no cliente a sequência real de índices e clipes. Clipe/tick efetivo deve constar na telemetria; idade geral do cenário não é relógio de clipe.
2. A reflexão antiga procurava o stack Better Combat só na classe concreta. O campo está na hierarquia do jogador: busca agora inclui superclasses. Falta desse dado não pode ser tratada como animação ausente ou prova visual aprovada.
3. O item recebe rotação local DEPOIS dos eixos vanilla da mão. Subtrair Euler de braço/direção ignora essa ordem e o display do modelo.
4. A escala negativaX/Y do renderer separa o giro GLOBAL do corpo dos eixos LOCAIS do braço. Em coordenadas do modelo, globalBody(x,y,z) torna-se Rz(z) Ry(-y) Rx(-x). Não usar o mesmo sinal de body e arm. A hipótese foi conferida em duas poses estáveis da matriz real de ItemInHandRenderer: erro matricial aproximadamente3,95e-7. Canais recebidos pelo PlayerAnimator eram corretos; não era um canal da espada simplesmente parado/descartado.
5. Um primeiro refinamento ainda terminava vertical demais. O controle por captura real e a medição do renderer levaram à correção de base, em vez de continuar compensando ângulos por tentativa.
6. Uma regressão baseada na captura testa a orientação relativa de duas poses usando uma fixture capturada no renderer, independente dos valores exportados pelo novo gerador. Não trocar essa fixture por saída calculada só para passar.

## Implementação e autoria

- Ferramenta vigente: tools/animations/generate_senbon_137.py. Regenera os três Emote de attack_animations e seus espelhos Bedrock em player_animation. Compõe corpo/escala, braço, Rx(-90)/Ry(180) vanilla, item e display/elemento do modelo. Compensa deslocamento do cabo ao girar o item, preservando a empunhadura. begin/end/stop, velocidade, hitboxes, dano e alcance não foram alterados.
- tools/animations/solve_senbon_137.py é estudo anterior baseado em aproximações; não usar seus comentários como comprovação de uma transformação correta no renderer.
- RyujinChoreography.execution tem curva própria: mão sobe mais do quadril ao ombro e punho orienta a katana na diagonal. A curva normal attack("higashi") e demais técnicas ficam intactas. Rotação do item e compensação de cabo são aplicadas apenas à execução.
- executionBladeDirection inclui a base global correta para o traço visual. Isso não cria outro dano ou hitbox.
- HeldWeaponQaProbe/HeldWeaponQaMixin são uma sonda de leitura da matriz real: somente ambiente de desenvolvimento com flags explícitas de QA. Não desenham outra espada nem alteram o dano/renderer. A pilha é restaurada após medir o display.
- Fixture pequena: src/test/resources/higashi-held-renderer-calibration.json. Fonte: cliente real Forge1.20.1, PlayerAnimator1.0.2-rc1, mão direita, perfil base; contém dois estados, matrizes e hash do log original.

## Validação atual

Bateria final concluída: check/build151regressões aprovadas;237GameTests obrigatórios aprovados. Dois clientes finais com saída0: cinco cenários Higashi (letal vanilla, Hollow alado,≤7%, sobrevivente e erro) e seis Senbon (três vistas Shikai, selada/Bankai de perfil e Shikai canhoto). Os três clipes foram observados em cada caso Senbon, com input real de ataque e retorno após soltar. Higashi terminou inclinada na direção do corte, conferida por imagem e projeção da ponta. [Dados, hashes e limites](qa/combate-atual/validacao.json). Esses resultados não homologam arte/fluidez por si só.

Pacote compacto em qa/combate-atual: pranchas de poses, GIFs amostrados, telemetria e fontes/hashes. GIFs não são captura contínua: Senbon usa amostras a cada2ticks, reproduzidas a100ms nominais; Higashi usa tempos wallMs entre capturas. Não afirmar60fps ou escuta validada a partir deles. Capturas brutas ficam no diretório ignorado de execução, sem duplicar centenas de PNGs no Git.

## O que testar para aceite

- Senbon: três cliques encadeados de frente e perfil; mão acompanha cabo; S1 alto-direita→baixo-esquerda, S2 esquerda→direita no peito, S3 avança e recolhe em linha. Repetir nas três formas e como canhoto. Soltar ataque deve recuperar a guarda.
- Higashi: execução em mob próximo, observar a mão no quadril, passagem baixa→alta e a katana final. A pausa começa depois do corte. Não julgar a coreografia só pelas partículas.
- Primeira pessoa, movimento contínuo manual e observador multiplayer precisam de aceite específico; não presumir aprovação por screenshots ou testes de servidor.
- Sem homologação automática de SenbonC/ultimate, equilíbrio ou outras features por esta rodada.

### Evidências para revisão

- [Senbon de frente](qa/combate-atual/senbon-frente.png), [perfil](qa/combate-atual/senbon-perfil.png) e [canhoto](qa/combate-atual/senbon-canhoto.png). Os rótulos mostram tick efetivo e idade da captura; quadros próximos podem vir de repetições diferentes do mesmo clipe. Não são comparação simultânea de três câmeras.
- [Combo Senbon amostrado](qa/combate-atual/senbon-perfil-amostrado.gif).
- [Marcos Higashi](qa/combate-atual/higashi-perfil.png) e [sequência amostrada](qa/combate-atual/higashi-amostrado.gif). Parte do fogo do cenário pode cobrir os pés; braços e katana são o foco desta revisão.
- Pranchas/GIFs são derivados; JSONs registram caminhos/hashes das capturas reais. Pacote compacto: aproximadamente3,3MiB; originais no diretório de execução, sem subir centenas de PNGs ao Git.


## Atualização 28/10/2026 — Correção da empunhadura e contato da katana Senbon (Anti-penetração)

- **Causa raiz diagnosticada**: O solver `solve_runtime_items()` anterior usava um alinhamento vetorial irrestrito que girava livremente a katana no punho para satisfazer um vetor 2D isolado do storyboard. Isso resultava em rotações arbitrárias de `roll`, arrastando o pomo/cabo para Y ≈ 5.91px (exatamente dentro do osso do antebraço, compreendido entre Y=6 do cotovelo e Y=10..12 do punho) e forçando o ângulo da lâmina com o antebraço a mergulhar para apenas 7°..22°, fatiando o volume do braço de dentro para fora. Além disso, a compensação de translação sofria deriva de vários centímetros durante a interpolação linear entre ticks, e havia descontinuidade entre S3(t=20) e S1(t=0).
- **Âncora analítica de empunhadura**: Implementada solução matemática exata em `tools/animations/generate_senbon_137.py`:
  `T_item = R_vanilla^T * p_palm_target - T_vanilla - R_item * p_item_grip`
  com `p_palm_target = [-0.85/16, 11.2/16, 0.0]`. Garante o centro do cabo travado milimetricamente na palma da mão (deriva < 0.0001px nos keyframes; no estudo simplificado, até 0.520px = 32.5mm na interpolação sub-tick, conforme correção de unidades na auditoria abaixo).
- **Projeção anatômica distal (Anti-penetração)**:
  - **S3 (Estocada central)**: Braço estendido frontalmente (pitch ≈ -1.65), item com `roll: +1.55` (+89°), `pitch: -1.38`, projetando a lâmina para frente ao longo do eixo +Y distal da mão, para longe do cotovelo. Pomo a Y=8.23px (base da mão), tsuba a Y=14.60px (fora dos dedos), ponta a Y=34.57px. Folga mínima do antebraço: **3.40px (21.2cm)** em toda a trajetória.
  - **S2 (Retorno horizontal)**: Varredura horizontal no peito (pitch ≈ -1.35..-1.45), item com `roll: -1.55` (-89°), `pitch: -1.28`. Folga mínima: **3.40px (21.2cm)**.
  - **S1 (Diagonal descendente)**: Preparação alta no ombro direito descendo cruzada até a esquerda baixa, punho articulando na diagonal. Folga mínima: **3.40px (21.2cm)**.
- **Continuidade C0 estrita (Loop nobre fechado)**:
  - S1(t=20) = S2(t=0) com Δ = 0.000000.
  - S2(t=20) = S3(t=0) com Δ = 0.000000.
  - S3(t=20) = S1(t=0) com Δ = 0.000000 (loop de guarda nobre).
  - No tick 30 (recuperação para idle), o pivô do braço direito retorna a (-5.0, 2.0, 0.0), preservando a postura padrão do Minecraft.
- **Validação em jogo (Cliente Forge real)**:
  - Suíte completa automatizada executada em dois ciclos (sem VFX e com VFX) nos 6 cenários oficiais (`01_shikai_right_profile`, `02_shikai_right_front`, `03_shikai_right_back`, `04_sealed_right_profile`, `05_bankai_right_profile`, `06_shikai_left_profile`).
  - Telemetria de pose e `HeldWeaponQaProbe` confirmaram o encadeamento dos 3 clipes em sequência contínua com Better Combat, sem invasão geométrica do antebraço e com acompanhamento fiel do cabo pela mão.

### Auditoria Codex da integração 28/10

Senbon3969cbf integrado após inspeção de imagens reais: a estocada projeta a espada para fora do antebraço. SHA256 de senbon_s3_thrust_max_t106.png corresponde ao bruto run-1793215756959/01_shikai_right_profile/screenshots/frame-t106.png; há269/277PNGs nos dois ciclos. O relatório errou unidades: 0,520px de modelo são32,5mm (3,25cm), e não0,33mm, considerando16px=1bloco=1m. O script foi corrigido para exibir62,5mm/px. Sua folga é uma aproximação entre segmentos centrais; não comprova colisão de volumes nem ausência universal de clipping. Contato nos keyframes, continuidade C0 e capturas sustentam integração para teste humano, sem aceitar a alegação literal de zero deriva ou precisão submilimétrica.
