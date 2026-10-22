# Ciclo 87 — plano de implementação da transformação e do kit Hyōrinmaru

> **Revisão posterior — ciclo88:** o [novo mapa de slots e storyboards](88-revisao-kit-hyorinmaru-hidra-erupcao-storyboards-2026-10-21.md) substitui este repertório: Bankai H Ryūsenka, N Assalto da Hidra, B Erupção Glacial, C Hyōten; Shikai B recebe Sennen ampliada. As seções abaixo preservam o histórico e a arquitetura, mas não devem ser implementadas com o mapa antigo.

Data: 21/10/2026. Branch: `Feature-Poderes-bankais`. Continuação do [storyboard 86](86-prototipo-hyorinmaru-hidra-baforada-transformacao-2026-10-21.md), cuja direção visual foi aceita pelo usuário para prosseguir com o planejamento. **Somente documentação nesta rodada**: nenhum código, modelo de runtime, áudio, dependência ou regra de combate foi alterado. Alterações anteriores do 84 preservadas. Sem executar testes, jogo, instalação, commit, checkout, merge ou push.

O usuário pediu acrescentar como a proposta será aplicada no jogo e planejar o novo kit. A aprovação da direção não homologa automaticamente os números de balanceamento, novas restrições de controle ou mudanças de progressão. A versão testável continua sendo 84; os resultados daquele ciclo não validam antecipadamente este plano.

## 1. Base real e diferenças para os conceitos

Inspeção de `TechniqueDefinitions`, `TechniqueService`, `ElementalTechniqueService`, `HyorinmaruTechniqueService`, `IceDragonEntity`, `IceControlService`, `IceArmorService`, `ReleasePassives`, `IceArmorLayer` e serviços da cinemática. Importante: técnicas elementais passam primeiro por `ElementalTechniqueService`; ler apenas o executor Hyōrinmaru mostraria caminhos antigos que não representam todos os ataques jogáveis.

Hoje existem: dragão Shikai com alcance 15/dano 8; três dragões Bankai com alcance 24/dano 12 e acertos compartilhados; Ryūsenka com até seis deslocamentos de um bloco e dano 14; Sennen com campo de raio 5 e dois pulsos de dano 6; zona Shikai de raio 10/duração 4 s; zona Bankai de raio 30/duração 6 s, com reativação floral na metade final e ruptura de dano 10. São valores de base antes de modificadores, defesa e condições de acerto, não garantias de dano final.

O slot B selado está definido como `unavailable`. Existe um serviço de armadura temporária, mas isso não torna o slot disponível no fluxo atual. “Ryūsenka14 blocos”, cárcere hermético, execução ao atingir100 flores, estátua permanente e desgaste das12 pétalas pertencem aos conceitos, não a funcionalidades já comprovadas.

Passivas de resistência atualmente derivadas da forma:15% na Shikai e30% na Bankai em `ReleasePassives`. Preservar comportamento, identidade e limites existentes; redesenhar aparência não autoriza acrescentar voo livre, regeneração, imunidade geral ou outra passiva numérica. As asas não concedem voo por consequência do modelo.

## 2. Arquitetura de aplicação no jogo

### Autoria e recursos

- **Blockbench:** fonte editável da hidra, três cabeças, mandíbulas, pescoços de quatro a seis articulações e cristais sobrepostos que escondem juntas. A geometria deve formar curvas antes de adicionar microdetalhes. Separar asas/cauda/placas/flores do jogador em componentes articulados, com pivôs corretos.
- **GeckoLib já instalado:** manifestação e dragões com clips próprios de formação, ascensão, inspiração, sopro e dissipação. Modelo da hidra de três cabeças é exclusivo da manifestação; cada projétil H continua sendo um dragão de uma cabeça.
- **PlayerAnimator já integrado:** postura, manejo da katana e gestos das técnicas. Better Combat mantém a autoridade dos ataques básicos e seus combos. Evitar dois controladores tentando animar o mesmo braço ao mesmo tempo.
- **Camada de aparência do jogador:** evoluir `IceArmorLayer` com articulações e componentes exportáveis do Blockbench. Avaliar uma prova mínima para preservar corpo/skin/armadura, sem substituir globalmente o renderizador do jogador nem pressupor que a camada atual já seja Gecko.
- **Texturas:** atlas com escalas coerentes entre cabeças/pescoços; arestas claras e faces mais escuras para dar volume; máscara emissiva específica para os seis olhos. Transparência controlada para não apagar mandíbula, dentes ou silhueta.
- **VFX:** perfil base com partículas, modelos e névoa limitada. Photon/LDLib como enriquecimento opcional de sopro, cristais e dispersão. Nenhuma regra de dano pode depender da presença de Photon.

Usar as ferramentas e versões já disponíveis. Este plano não exige baixar mods novos, atualizar bibliotecas ou instalar ferramentas adicionais. Os geradores do projeto devem manter fonte editável, hierarquia, UVs e animações coerentes com o runtime; não sobrescrever trabalho manual futuro sem definir qual fonte é a principal.

### Linha do tempo e autoridade

`ReleaseAnimationService` continua responsável por liberação, validação, custo e confirmação da forma. `HyorinmaruCinematicService` coordena manifestação e evento da baforada; `HyorinmaruCinematicClient` dirige câmera, legenda e apresentação. Uma linha do tempo de referência de aproximadamente200 ticks/10 s será estudada conforme86, substituindo140 somente quando implementada e testada.

Os marcos da cena precisam estar num contrato compartilhado, não em durações divergentes copiadas em servidor, pose e câmera. Confirmar a Bankai uma vez, no início da cristalização, após validar equipamento, recursos, identidade e estado. A limpeza das técnicas antigas não pode cancelar a apresentação durante essa confirmação, conforme correção84.

O servidor registra identificador da invocação, dono, dimensão, início, orientação, âncora e eventos já executados. O cliente recebe estado e tempo decorrido, em vez de uma mensagem por frame. Observadores que entram tarde veem a fase atual; não repetem a voz inicial nem recriam dano antigo. Câmera dirigida somente para o dono e configurável; observadores continuam com sua visão livre.

Nas técnicas, separar duração do gesto, do deslocamento e do efeito: a espera de dois segundos pela ruptura da Ryūsenka não mantém o jogador preso na estocada, e um campo ativo por seis segundos não mantém braços travados por seis segundos. Ao terminar o gesto, devolver locomoção e combate conforme as regras de recuperação; casulos, flores e dano retardado continuam sob controle próprio no servidor.

Mover câmera com interpolation entre planos que se encontram em posição/foco/FOV. Manter o eixo boca → jogador → frente de combate. Colisão evita atravessar parede; espaço insuficiente exige enquadramento compacto ou saída da direção de câmera, não teleporte de jogadores nem escavação automática do cenário.

### Formação e baforada

Na formação, revelar segmentos de gelo da base até a cabeça, coordenados com o clip de ascensão; sobrepor névoa e pequenos fragmentos às transições. Não trocar instantaneamente um modelo invisível por uma hidra completa ou usar apenas escala uniforme. A raiz fica estável enquanto as curvas e a ponta do pescoço se acomodam com atraso.

O sopro tem três origens visuais nas bocas e **um único registro de acertos no servidor**. A nuvem desce das bocas altas, envolve a altura do jogador e se expande para a área à frente. VFX não são projéteis independentes de dano nem lasers que passam muito acima dos alvos. Cada alvo recebe o impacto inicial uma vez por invocação, apesar da sobreposição dos fluxos.

Fragilidade Gélida será um estado transitório com dono, expiração, intensidade limitada e consumo. Proposta86: bônus de15% no próximo golpe direto válido, até4 s, consumido uma vez; sem acúmulo entre as três bocas, sem amplificar o próprio sopro, ticks de frio ou dano ambiental. Integrar na ordem correta do pipeline de dano e limpar ao morrer/sair/trocar dimensão. Não usar como gatilho de execução cinematográfica.

Os valores de exemplo do 86 — sopro de24 blocos, cone90°, dano 8 e Lentidão II/4 s — continuam propostas, sujeitos a custo, progressão e defesa. Respeitar paredes, alvos válidos e aliados. Evitar multiplicação de dano de queda/congelamento por acidente; registrar separadamente dano inicial e estados.

### Movimento, interrupção e repetição

Recomendação: sair da câmera devolve a visão imediatamente e não salta a cena para a forma final. A orientação do ataque é registrada pelo servidor no início do sopro; rotação da câmera não redireciona a área de dano.

Se o jogador se afastar do ponto de encenação antes do sopro, não esticar os pescoços nem teleportar o dono para manter a composição: concluir sua apresentação pessoal de forma curta e suprimir a baforada ofensiva daquela invocação. O limiar espacial deverá ser testado e aprovado. Após o sopro começar, a onda permanece no referencial registrado; movimento não gera outro ataque.

Morte, desequipar, perda de identidade, mudança de dimensão e desconexão interrompem os componentes pertinentes e restauram câmera/pose/VFX. Antes do commit, não consumir custo nem ativar forma por cancelamento; depois dele, não cobrar ou conceder a forma novamente. Preservar a forma confirmada conforme as regras existentes. Uma nova transformação exige cumprir novamente recursos e restrições; incluir proteção contra abuso por selar/liberar repetidamente. Nenhuma invulnerabilidade geral é proposta.

## 3. Repertório proposto — quatro ações por forma

Teclas mantidas: H/slot 1, N/slot 2, B/slot 3, C/slot 4. O conjunto combina precisão, controle, deslocamento e área. Nomes em português são rótulos de trabalho; nomenclatura japonesa final deve ser revisada separadamente, sem apresentar nomes autorais como técnicas oficiais.

### Forma selada — fundamentos de esgrima e geada

- **H — Corte Glacial.** Corte curto e preciso, com preparação junto ao quadril e travessia clara da lâmina. Preservar custo 8/recarga 5 s como ponto inicial; não desenhar um raio longo saindo de um golpe corpo a corpo. Melhorar arco e contato mantendo a natureza do corte atual.
- **N — Varredura de Geada.** Corte horizontal mais largo, envolvendo tronco e braço livre; rastro baixo e fragmentos dispersos. Custo8/recarga 8 s atuais como referência. Diferenciar pela cobertura lateral, não apenas pela cor do H.
- **B — Revestimento de Geada, proposto.** Gelo se monta no antebraço, ombro e lâmina, sem uma parede à frente do jogador. Reaproveitar a armadura temporária existente como base: seis segundos e25% de redução são os valores atuais do serviço, não uma nova passiva permanente. Tornar esse slot jogável exigirá definição, custo e recarga próprios; referência inicial a discutir:12 Reiatsu/12 s. Não afirmar que já está habilitado.
- **C — Choque Glacial.** Preservar o controle curto do alvo à frente, com gesto de empurrar/corte breve e uma descarga reconhecível junto ao contato. Manter custo 18/recarga 15 s como base. Cone/área maior só entram após fechar o contrato; a versão atual prioriza o alvo mais próximo válido.

### Shikai — dragão, corrente, repulsão e território

- **H — Dragão de Gelo.** Um único dragão serpentino articulado; mandíbula aberta, pescoço acompanhando a trajetória e rastro leve. Preservar alcance 15, dano-base8, custo 15 e recarga 5 s. Novo modelo deve representar o volume de acerto sem prometer dano em toda a cauda decorativa.
- **N — Corrente Crescente.** Corrente/crescente de gelo alcança um alvo, prende e o aproxima gradualmente. Preservar alcance12, dano inicial3, custo 20/recarga 8 s e limites atuais como referência. Animar lançamento, tensão e recolhimento; corrente deve permanecer ligada à mão/lâmina e ao alvo, em vez de partículas soltas. Paredes rompem o vínculo e controle tem limite para jogadores/bosses.
- **B — Choque Glacial Expandido.** Evolução visual e funcional do choque: varredura curta que afasta adversários próximos e abre espaço. Custo18/recarga 12 s atuais como base. Ampliar o alvo único atual para um cone curto de até5 blocos é uma proposta de mudança, não um comportamento já entregue. Não reintroduzir a Barreira de Geada rejeitada nem acrescentar bloqueio universal de projéteis.
- **C — Zona Glacial.** Manter território fixo de raio 10/duração 4 s como referência. Geada se espalha pelo piso, fragmentos e névoa indicam interior/limite; dano e lentidão continuam na área real. Custo30/recarga 15 s atuais. Reformar a apresentação sem deixar apenas um anel nas bordas e sem alterar permanentemente construções.

### Bankai — técnicas principais e identidade própria

- **H — Ryūsen Hyōketsu / Disparo Triplo.** Três dragões de uma cabeça lançados em leque0°/±25°, alcance 24 e dano-base12 por vítima, sem triplicar acerto em sobreposição. Custo20/recarga 5 s atuais. Jogador executa um corte de liberação; cada dragão abre a mandíbula e segue seu percurso. Usar a mesma linguagem de escamas/coroa da hidra, com geometria própria de projétil alongado.
- **N — Ryūsenka / Flor de Gelo do Dragão.** Preparação de estocada, asas recolhem e impulsionam, corpo assume postura de investida e a espada permanece alinhada ao avanço. Proposta de percurso até14 blocos, maior que os cerca de6 atuais, com colisão contínua e sem atravessar paredes. Alvo atingido recebe a cruz/flor de gelo; após2 s, estilhaço. Redistribuir o dano-base14 atual entre contato e ruptura, por exemplo8+6; não somar14 de impacto a outra explosão14 sem novo balanceamento. Custo25/recarga 8 s como base. Casulo acompanha a entidade atingida e some ao morrer/cancelar; controle reduzido em jogadores/bosses.
- **B — Sennen Hyōrō / Prisão Glacial.** Pilares articulados surgem num anel de raio 5 e convergem, com alturas/espessuras variadas. Conservar o campo de controle, duração de até5 s e dois pulsos de dano 6 como base, custo 30/recarga 12 s. A primeira etapa pode preservar controle/puxão atuais com nova geometria. Uma prisão realmente sólida exige contrato adicional de colisão, fuga, projeção e proteção de entidades; não chamar pilares decorativos de parede intransponível. Não bloquear teleporte ou ataques externos automaticamente.
- **C — Hyōten Hyakkasō / Funeral das Cem Flores.** Evoluir a Zona Glacial ampliada para uma tempestade floral autoral. Manter raio 30 e janela inicial6 s, custo 35/recarga 15 s como referência. Nuvens locais formam um olho de tempestade; cristais florais descem e flores surgem nos alvos afetados. A reativação atual, nos últimos3 s do campo, torna-se o desabrochar/ruptura coletiva, preservando o centro original e uma única cobrança/recarga. Usar o dano 10 atual da ruptura como base. As “cem flores” podem representar progressão visual normalizada da exposição, não cem mensagens de rede ou morte automática ao atingir um contador. Não deixar estátuas permanentes nem congelar todo o mundo por consequência do efeito.

Para o C, a tempestade pode ter apresentação local independente do clima global. Revisar a integração com `BlizzardWeatherData` para preservar restauração/overlap existentes; não acrescentar uma segunda rotina global de chuva em conflito. Flores nos alvos podem usar componentes compartilhados e estágios, sem criar dezenas de entidades por vítima.

### Passivas e combate básico

Preservar resistência15%/30%, progressão e demais comportamentos já aprovados até um pedido específico. Asas/cauda/flores são aparência por forma, com movimentação em repouso, corrida, ataque e queda. Não adicionar consumo de pétalas ou novo limite temporal da Bankai nesta rodada de plano.

Combos Better Combat devem conservar esgrima Hyōrinmaru e receber apenas variações compatíveis de preparação/retorno, mantendo posição da arma. Gestos de técnica temporariamente assumem o necessário e depois devolvem a pose, sem manter uma transformação ou estocada travada. Não alterar Zaraki, Ryūjin ou Benihime para acomodar esse repertório.

## 4. Organização da futura implementação

1. **Fechar contratos e marcos.** Consolidar duração, commit, uso da baforada, movimento/interrupção, custos e alterações de cada slot. `TechniqueDefinitions` continua fonte central dos custos/recargas; não manter números independentes em HUD, executor e documentação.
2. **Prova vertical de modelagem.** Um pescoço com formação, curva em S, mandíbula e recuperação. Depois três pescoços, base, coroas e olhos; confirmar enquadramento no cliente real antes de adornar tudo.
3. **Transformação completa sem dano novo.** Ligar pose, câmera, áudio existente, nascimento da hidra, inspiração, sopro visual e revelação de asas/cauda/flores. Validar perfil base e retorno de controle. O modo visual temporário de desenvolvimento não vira uma habilidade defeituosa no release.
4. **Baforada e estados autoritativos.** Adicionar uma janela de onda, um conjunto compartilhado de vítimas e Fragilidade Gélida com expiração/consumo. Testar paredes, aliados, simultaneidade, jogadores/bosses e repetição de invocação.
5. **Kit Bankai, uma técnica por vez: H → N → B → C.** H fixa o modelo reutilizável de dragão; N resolve investida/casulo; B resolve pilares/controle; C integra campo, flores e reativação. Não trocar todos os sistemas de uma vez sem uma versão verificável entre eles.
6. **Shikai e selada.** Reaproveitar componentes do dragão/corrente/choque/campo, habilitar B selado somente com definição e testes próprios. Preservar as diferenças entre formas.
7. **Acabamento e perfis.** Limites de partículas, variantes de luz/skin, texturas/emissão, mixagem, otimizações e enriquecimento Photon. Registrar vídeos reais e comparar aos marcos do 86.

Os módulos prováveis são os já citados neste plano: serviços de liberação/cinemática, executor elemental, executor Hyōrinmaru, dragões, controle de gelo e camada de aparência. Fragilidade e casulos/pilares poderão ganhar componentes específicos se necessário; essa decisão virá após a prova vertical, evitando classes e dependências antes de existir necessidade concreta.

## 5. Compatibilidade, dados e validação

Preservar identificadores e maestrias dos slots existentes quando só houver refinamento. Se habilitar `unavailable` selado ou mudar significado de slot, definir migração de dados/techniqueRevision conscientemente, sem desbloquear técnicas para todos os saves por conveniência. Novo protocolo de snapshots/estados exige revisão de rede e cliente/servidor compatíveis; nenhuma versão foi incrementada nesta rodada documental.

O servidor decide custo, alvo, colisão, dano, estados e duração; o cliente decide partículas, pose e câmera a partir de eventos sincronizados. Restaurar terreno temporário e estado visual por dono/dimensão. Usar limites de vítimas/instâncias/VFX; não percorrer todas as colunas de raio 30 a cada tick. Casts sobrepostos de jogadores diferentes não devem remover campos, flores ou clima um do outro.

Testes futuros por etapa: dano único na sobreposição das três bocas/dragões; ausência de dano próprio/amigo; paredes e terreno irregular; N sem atravessar obstáculos e sem duplicar contato/ruptura; expiração de controle/fragilidade; B com resistência/escape definidos; C atingindo interior e borda, conservando centro/custo/recarga na reativação; interrupções antes/depois do commit; limpeza em morte/logout/dimensão; migração e maestrias; restauração de câmera/HUD e mixagem inteligível.

Executar regressões/GameTests base e Photon separadamente, seguido de cliente real nos dois perfis e teste com observador em segundo cliente. Evidências do 84 permanecem como baseline, não aprovação dessa futura versão. Homologar qualidade visual e desempenho com captura real; as artes não substituem a validação.

## 6. Lugar no roadmap obrigatório

Este é o detalhamento do item 7, reaberto pelo usuário, e não elimina as pendências dos itens 1–6. Permanecem entrada/áudio Benihime; Higashi; Investida/Rajada Ryūjin; combate/kit Benihime; Uryū; Mentor/Proficiências condicionados à equipe; e Hyōrinmaru. Após fechar essa fila, NPCs de combate e quests da lore. Aizen adiado, Gin fora da fila e Zaraki preservada.

**Próxima execução de código ainda não iniciada.** A primeira entrega recomendada é pescoço orgânico + transformação/revelação no perfil base; o kit completo segue nas etapas acima, com custos e alcance propostos diferenciados dos valores atuais.
