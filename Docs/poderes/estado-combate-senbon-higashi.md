# Estado vigente — combate Senbonzakura e execução Higashi

Atualização: 27/10/2026. Branch Feature-Poderes-bankais. Este documento será atualizado por assunto, evitando mais um relatório completo para cada rodada. Antecedentes:137–149; direção137 continua como referência artística. Não substitui o GDD nem o contrato118.

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
