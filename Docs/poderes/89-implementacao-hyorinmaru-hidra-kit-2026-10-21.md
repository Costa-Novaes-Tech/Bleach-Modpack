# Ciclo 89 — implementação da hidra, transformação e novo kit Hyōrinmaru

21/10/2026. Branch `Feature-Poderes-bankais`, referência inicial `c206585`. Trabalho iniciado após autorização de implementar os planos 86–88. Preservadas as alterações locais anteriores do ciclo84; nenhum commit, checkout, merge ou mudança no conteúdo Zaraki.

## Entrega funcional desta rodada

Primeira implementação jogável dos novos slots e da transformação. **Não equivale a homologação artística final.** Os storyboards continuam sendo referência para acabamento; não são capturas do jogo.

### Transformação — 200 ticks / 10 segundos

- Pose em pé, katana baixa e apoio da outra mão; fala inicial aos12 ticks.
- Manifestação aos44 ticks, oito blocos atrás da âncora da invocação. Três bases cristalinas, três pescoços com12 segmentos cada, cabeças e mandíbulas independentes.
- Curvas cúbicas com formação progressiva, oscilação e flexão para baixar as bocas durante a expiração. A mesma função espacial orienta a manifestação e a ofensiva; o relógio da entidade é sincronizado pelo servidor.
- Preparação/expiração aos108–144; dano somente aos120–144, depois do compromisso da Bankai aos120. Isso evita que a distância do alvo determine se recebe o multiplicador da forma antiga ou nova.
- Baforada frontal: alcance24, abertura nominal90°, altura limitada, paredes e política compartilhada de aliados/PvP. Dano-base8 uma vez por alvo; lentidãoII por4s e **Fragilidade Gélida**: próximo golpe direto do invocador ganha15% dentro de4s. Dano ambiental não consome a marca. Não é vulnerabilidade permanente ou novo atributo salvo.
- Sair mais de3 blocos da âncora suprime definitivamente o dano dessa invocação; não conclui a cena instantaneamente. A câmera retorna ao controle ao movimento. Invalidação, desequipar, dano/interrupção e encerramento limpam a manifestação conforme seus serviços.
- Cristalização aos120–166, fala do nome aos165 e devolução da câmera aos184–200. Asas com ponta/penas articuladas; cauda com sete articulações; três flores de quatro pétalas. Mantida a skin do jogador.
- Sons existentes da Hyōrinmaru reaproveitados com alcance/atenuação64, para a câmera afastada não silenciar o nome. Não foram criadas novas vozes nem alteradas as vozes da Benihime/Zaraki.

A neve combina partículas nativas com nuvens texturizadas usando sprite vanilla, permitindo ler a expiração na câmera aberta. Continua sendo um primeiro acabamento, não uma reprodução final das artes.

### Bankai

- **H — Ryūsenka:** avanço horizontal de até14 blocos, interrompido por colisão. Dano-base8 no contato; casulo acompanha os alvos acertados; ruptura-base6 após40 ticks de cada contato. Sem acertar, não há ruptura em alvo inventado. Gesto termina antes da espera do casulo.
- **N — Assalto da Hidra Glacial:** reinvoca uma única entidade com três cabeças conectadas, sem repetir a transformação. Avanço central, arco e mergulho escalonados; alcance nominal32, reduzido diante de obstáculos. Varredura usa a curva da cabeça, não toda a caixa diagonal do pescoço. Uma aplicação por cabeça/alvo, bases4+4+10; controle limitado para jogadores/alvos resistentes. Recolhimento aos48–56 ticks e resíduo de partículas até cerca de5s após o ataque.
- **B — Erupção Glacial:** frente em leque de32 blocos/~60°. Até40 âncoras visuais consultadas no terreno; espinhos facetados de8–14 blocos. Dano-base8 quando a frente chega e16 na ruptura, somente nos alvos antes atingidos e ainda válidos na área. Caminho pelo chão, chunks e paredes verificados; sem destruir ou substituir blocos. Não atravessa grandes vãos/precipícios: consulta de suporte usa margem vertical limitada.
- **C — Hyōten / zona ampliada e reativação:** comportamento anterior preservado. Não foi refeito todo o sistema climático/floral nesta rodada.

Referência de custo/recarga, na ordem H/N/B/C: **25/30/35/35 Reiatsu**, **8/12/16/15s**. Dano-base recebe os multiplicadores normais de forma/atributos e a defesa do alvo; não é o HP final garantido de qualquer entidade.

### Shikai e selada

- ShikaiH dragão único, N Corrente Crescente e C Zona Glacial preservados.
- **ShikaiB Sennen Hyōrō:** centro fixo apontado até24 blocos; raio10/diâmetro20; pilares de12–14 blocos, fechamento e duas aplicações-base6. Custo30, recarga14s. Não depende de continuar olhando para o centro.
- **SeladaB Revestimento de Geada:** novo slot pago com12 Reiatsu/12s de recarga; reaproveita armadura temporária de6s/25% de redução. Não cria armadura de inventário nem defesa permanente.
- SeladaH/N e Choque Glacial emC mantidos. Passivas15% Shikai/30% Bankai mantidas.

## Dados, autoria e compatibilidade

Protocolo **2.33**; schema geral9, revisão das técnicas4. Todos os clientes/servidor precisam da mesma build.

Ryūsenka mantém seu ID e sua proficiência ao mudar deN paraH. Investimento em Bankai `ice_dragon` passa ao novo `hydra_assault`; Bankai `sennen` passa a `ice_eruption`; Shikai `duel_control` passa a Shikai `sennen`. Mesclagem pelo maior valor, desbloqueios preservados, sem copiar progresso entre formas; chaves antigas ficam arquivadas. Revestimento novo não recebe automaticamente proficiência de uma técnica inexistente.

Fontes: `tools/models/hydra_organic.py`, gerador `ice_hydra.py`, fonte editável `tools/models/source/ice_hydra.bbmodel`, geometria/animações GeckoLib e `HydraMotion.java`. As poses curvas em jogo são procedurais; os clips de autoria no arquivo Blockbench são pontos de edição/referência e não substituem o controlador espacial. Não foi instalado novo editor, plugin ou biblioteca. GeckoLib, PlayerAnimator, Better Combat, recursos vanilla e perfil Photon/LDLib existentes foram reutilizados.

## Verificação e evidências

Linha de base: **137 regressões/168 GameTests**. Código de combate novo: **139 regressões/173 GameTests**, aprovados nos perfis base e Photon/LDLib. `check`, `build` e preparação dos dois pacotes concluídos; `ice_hydra.py --check` e `git diff --check` aprovados.

O primeiro teste falhou porque ainda tentava acionar o projétil aposentado pelo slotH. A cobertura do projétil legado foi mantida por configuração direta, e o slotN ganhou teste da hidra conectada. As expectativas dos novos testes de dano foram corrigidas para aplicar multiplicador da Bankai e armadura natural dos zumbis; nenhuma fórmula global foi reduzida para fazer o teste passar. Os novos testes cobrem curvas finitas/alcance, migração idempotente, bifurcação da hidra/limpeza, ruptura atrasada, chegada/ruptura da erupção, fragilidade consumível, paredes/cone da baforada e supressão ofensiva ao se afastar.

[Relatório de QA e arquivos](qa/cycle89/README.md), [verificação estruturada](qa/cycle89/verification.json). Capturas reais em cliente Forge: **oito cenários por perfil, 219 PNGs no base e220 no Photon**: transformação, asas/flores, quatro slots Bankai, Sennen Shikai e armadura selada. A primeira abertura do perfil Photon parou na tela de acessibilidade inicial; o processo de QA foi fechado normalmente, e o perfil isolado recebeu as opções do perfil de QA base, sem alterar opções do jogo principal. A repetição entrou no mundo corretamente.

Os MP4 são montagens dos PNGs amostrados a cada quatro ticks, com duração pelos ticks capturados; **não têm áudio, interpolação ou captura de todos os frames**. Os dados FPS são amostras de `Minecraft.getFps()` durante captura, em Intel Iris Xe/OpenGL4.6, render distance6/partículas completas, não um benchmark de frametime ou teste de carga de servidor. Não confundir servidor GameTest com teste de sincronização em dois clientes reais.

## Pendências para homologação e acabamento

1. Revisão do usuário das curvas/cabeças, da baforada, da pose da katana, asas e cauda em movimento. A malha ainda é estilizada para Minecraft; não tem o acabamento das ilustrações.
2. Refinamento dos clips editáveis Blockbench para acompanhar melhor as poses procedurais; dentes, silhueta e emissão/texturas podem receber outra rodada artística.
3. Campo C e clima: preservados. As capturas em bioma quente ainda podem mostrar chuva do sistema anterior; avaliar neve/clima visual separadamente, sem alterar uma passiva incidentalmente.
4. Teste dedicado com observador e dois clientes, latência/entrada tardia, PvP, noite, terreno muito irregular e espaço estreito. Não presumir sucesso desses cenários pelo singleplayer.
5. Escuta humana das falas/rugidos e avaliação de conforto da câmera. Os clipes montados de screenshots não contêm áudio nem demonstram cada frame intermediário.
6. Acabamento Photon dedicado permanece opcional e aberto. Esta implementação funciona com renderização base; não adiciona dependência obrigatória de Photon.

A lista obrigatória do ciclo72 continua vigente: não encerrar Benihime/Ryūjin, Uryū, Mentor/Proficiências ou outras frentes por concluir esta primeira versão da Hyōrinmaru. Aizen segue adiado, Gin fora da fila. NPCs de combate/quests de lore vêm após o fechamento da lista acordada.

## Pacotes para teste

`build/libs/bleachmod-0.2.0.jar`, `build/combat-test-pack` (5 JARs) e `build/photon-test-pack` (7 JARs). Mod idêntico nos dois pacotes, integridade ZIP aprovada e recursos comparados com as fontes sem divergências. SHA256 do mod: `71906bf3c42b5b359269a55a62c8e32135aea41a51a5ef51132e827fb5df27c9`.
