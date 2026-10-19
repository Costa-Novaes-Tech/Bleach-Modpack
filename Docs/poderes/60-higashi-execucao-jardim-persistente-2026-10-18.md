# Ciclo 60 — execução Higashi e Jardim com cobertura persistente

Pedido de 18/10/2026: transformar Higashi em finalizador cinematográfico abaixo de 7% de vida e reforçar o Jardim de Senbonzakura, preservando pétalas dentro do campo durante a formação da flor. Branch `Feature-Poderes-bankais`; base local `3187551`, limpa antes do trabalho. Documentação restrita a `Docs/poderes`. Sem novas dependências, push ou merge.

## Higashi

- Continua sendo o slot N2 da Bankai Ryūjin, com custo 25, cooldown 160 ticks e impacto no tick 12. O gesto de preparo no quadril/corte e as chamas do ciclo 58 permanecem.
- No impacto, entre os alvos válidos do corte existente, **um alvo elegível mais alinhado à mira** recebe execução se tiver vida positiva **menor ou igual a 7% da vida máxima**. A avaliação ocorre antes do dano normal: atingir alguém acima de 7% não inicia execução retroativa. Os demais alvos recebem o golpe normal de dano bruto 16.
- Fonte própria `bleachmod:higashi_execution`, atribuída ao atacante, com dano decisivo e tags que ignoram armadura, escudo, encantamentos, resistência, absorção prática, frames de imunidade e Totem. Não depende de fogo: imunidade a chamas não neutraliza o finalizador. A Resistência física do mod não reduz essa fonte indireta. Não há multiplicação pelos bônus físicos nem crédito adicional manual de treino; a morte mantém autoria normal.
- Conserva a política compartilhada de alvos: aliados, NPCs protegidos, espectador, PvP desativado, entidades invulneráveis e jogadores em criativo/invulneráveis não se tornam vítimas da execução. Eventos Forge de dano/morte continuam podendo vetar o resultado. Não usa `remove`, `setHealth(0)` nem comandos de kill. Morte, loot e inventário seguem suas regras normais; a incineração é visual e não apaga itens.
- Só uma morte confirmada gera pacote S2C. Não existe pedido de execução vindo do cliente. Rede **2.23** exige cliente e servidor na mesma build; schema **9** e revisão de técnicas **2** mantidos.

## Câmera e incineração

Após a confirmação, chamas/faíscas envolvem o alvo e dão lugar a fumaça/cinzas ascendentes, por 32 ticks (1,6 s). O corpo deixa de ser desenhado após seis ticks; não é excluído pelo cliente. Observadores recebem a incineração, sem mudar suas câmeras.

Para o atacante, uma câmera local temporária faz um arco lateral aproximando o enquadramento do golpe. A câmera verifica colisão com blocos; não move jogador/alvo, não concede invulnerabilidade nem pausa o servidor. Retorna à câmera/perspectiva anterior ao terminar, ou antes em caso de dano recebido, morte, menu, ataque/uso manual, F5, mudança de câmera, mundo ou desconexão. Pode ser desativada com `cinematicExecutionCamera=false` em `config/bleachmod-client.toml`. A opção anterior de FOV das liberações continua independente.

Até oito incinerações locais, teto global de 256 partículas/tick, alcance visual 80 e redução no modo mínimo. Cinemática de execução não é um novo lock de combate. Não houve captura do cliente ou homologação da movimentação real de câmera nesta execução.

## Jardim de pétalas

Campo continua fixo, **raio 35**, reativação única a partir de dois e antes de seis segundos, danos brutos 6/14 e mesmos custos/cancelamentos. Formação da flor continua com até 16 ticks e encurta perto do prazo para concluir uma reativação aceita.

Há uma nova cobertura de pétalas desenhada junto ao terreno, usando sprite vanilla de cerejeira: até **1.200** amostras na configuração completa, 640 na reduzida e 240 na mínima. Dois terços mantêm suas âncoras no solo; um terço se desloca para o contorno floral. Assim, a formação da flor conserva o interior preenchido. Não são blocos nem entidades persistentes e não alteram o terreno.

Pétalas no ar também foram reforçadas: campo 192 por quadro emitido, formação combinando 96 de cobertura e 144 de flor, pulso final com 80 de flor e 160 ascendentes. A camada do chão continua por dois segundos após a explosão, diminuindo no último segundo; um fluxo ascendente adicional parte do interior durante os primeiros 12 ticks. As pétalas do chão não precisam desaparecer para produzir a subida.

Orçamentos: servidor mantém 256 partículas parametrizadas por estado/tick; cliente limita a camada aérea existente a 384/tick, a emissão extra ascendente a 192/tick e o chão a 4.096 quads globais. Até oito campos em cache; 128 consultas de terreno/tick, somente em chunks carregados; limpeza por expiração/mundo/desconexão. Superfícies muito desniveladas fora da busca vertical local podem ficar sem cobertura, sem mudar o dano. Estes limites não equivalem a um benchmark de FPS.

## Arquivos e validação

Execução: `HigashiExecutionService`, impacto em `ElementalTechniqueService`, `HigashiExecutionS2C`, `HigashiExecutionVisuals`, configuração visual, tipo/tags de dano e traduções. Jardim: `SenbonGardenGeometry`, `SenbonGardenFloor`, `SenbonPetalEffects`, contagens do pacote e emissão no executor Senbonzakura.

`check build prepareCombatTestPack runGameTestServer` aprovados: **110 regressões e 135 GameTests obrigatórios**, log local `build/cycle60-validation.log`. ZIP conferido com cinco JARs, integridade, classes/recursos novos, traduções e hash do Bleach iguais ao JAR compilado. Regressões cobrem limiar, geometria que preserva o interior e pacote confirmado; GameTests novos cobrem defesas/Totem, seleção única e proteção de aliados/alvos invulneráveis.

Pacote de teste: `build/combat-test-pack/bleach-bettercombat-piloto.zip`, cinco JARs. Mesmas quatro dependências do ciclo 59; substituir Bleach nos dois lados se elas já estiverem instaladas. Nenhum Photon/LDLib acrescentado. Hyōrinmaru e roadmap 51 preservados; Aizen adiado, Gin removido e integração Zaraki ainda depende do commit da equipe.

## Roteiro de playtest

1. Higashi Bankai contra alvo a 7%, abaixo e ligeiramente acima; testar armadura, resistência, Totem e mob imune a fogo. O finalizador só deve aparecer nos dois primeiros casos, se o golpe alcançar o alvo.
2. Dois alvos fracos no cone: confirmar um finalizador, dano normal nos demais, uma cobrança e cooldown normal. Conferir aliados, criativo, PvP desativado e cobertura sólida.
3. Primeira/terceira pessoa, destro/canhoto, F5/menu/dano no meio da câmera, terreno apertado e reconexão. Conferir restauração, cinzas e observador com outro cliente; o alvo jogador segue sua tela de morte normal.
4. Jardim no terreno plano e com pequenos desníveis, visão de cima/dentro/fora: primeira expansão, pétalas internas durante a flor, subida por toda a área e cobertura após a explosão. Reativar cedo e perto dos seis segundos; conferir configurações gráficas e FPS.

Não tratar testes de servidor como homologação visual, de FPS ou de PvP com dois clientes reais.
