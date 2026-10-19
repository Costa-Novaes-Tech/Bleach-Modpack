# Ciclo 59 — Liberações e gestos dos conjuntos restantes

Data: 17/10/2026. Branch `Feature-Poderes-bankais`, base limpa `e789131`. Usuário autorizou levantar as animações/efeitos restantes, recomendar a direção e já implementar para testes. Não localizado AGENTS.md. Fontes de contrato: continuidade 00, roadmap 51, Senbonzakura 43–46, Benihime 47–50 e revisão visual Ryūjin 57/58. Sem fetch, merge, push ou integração presumida do Zaraki.

## Levantamento e direção adotada

Ryūjin recebeu a revisão própria no ciclo 58; Hyōrinmaru estava aceita por enquanto. Foram preservadas. Os conjuntos que ainda não tinham a mesma integração de movimentos do jogador/liberação eram **Senbonzakura, Benihime e Asauchi**. Seus modelos, cores e capacidades existentes não foram substituídos por novos poderes.

- **Byakuya:** gestos precisos e contidos, espada/mão conduzindo o percurso das pétalas, cinco cortes alternados, mãos reunindo/abrindo o campo e estocada avançada da Hakuteiken. Pétalas com vento, jardim de raio 35, Senkei e asas/lâmina branca aprovadas continuam como base.
- **Urahara:** incisões de espada, mãos compondo fios/redes, gesto de abertura de corredor e reconstrução corporal/cura. Manifestação articulada atrás do jogador preservada; surgimento gradual na Bankai e detalhes carmesins nos gestos.
- **Asauchi:** liberação espiritual contida, sem inventar técnicas elementais. Mãos/espada se alinham, brilho percorre a arma e a postura normal retorna. Cores azul na Shikai e vermelha na Bankai acompanham as formas já existentes.

PlayerAnimator, partículas vanilla e geometrias autorais já disponíveis bastam para esta rodada. Não foi instalado Photon/LDLib ou outro mod. Melhorar autoria dos modelos/material/som continua possível após o playtest, sem apresentar uma biblioteca ainda não integrada como recurso instalado.

## Transformações implementadas

O serviço comum `ReleaseAnimationService` agora cobre as cinco identidades existentes. Senbonzakura, Benihime e Asauchi passam a usar Shikai de **24 ticks com commit no 16** e Bankai de **60 ticks com commit no 40**, como as liberações anteriores. Débito ocorre uma vez no commit. Antes dele, cancelamento por perda de arma/estado mantém a forma anterior e não cobra. Durante a liberação aplicam-se os bloqueios de ação já existentes do serviço; não há invulnerabilidade. No fim o controle retorna às animações normais, sem criar pose ociosa permanente.

| Conjunto | Apresentação |
|---|---|
| Senbonzakura Shikai | Espada abaixa/conduz a dispersão, mão livre acompanha e pétalas se abrem ao redor do jogador. Modelo fantasmagórico aprovado permanece após o commit. |
| Senbonzakura Bankai | Duas fileiras de seis lâminas altas surgem nas laterais, crescem e se dissipam enquanto as pétalas se expandem. São construções visuais de liberação, sem entidades, dano ou hitboxes. |
| Benihime Shikai | Torção curta de espada/tronco e mão livre costurando fios carmesins; pontos luminosos brancos marcam a conclusão. |
| Benihime Bankai | Fios carmesins acompanham a liberação. Depois do commit, a manifestação existente cresce/faz fade até ficar completa nos últimos ticks; posição afastada, vestes, mãos articuladas e fios anteriores preservados. |
| Asauchi Shikai/Bankai | Alinhamento de espada e mão livre, brilho espiritual azul/vermelho e retorno à posição normal. Sem habilidades elementais novas nem mudança dos bônus de forma. |

FOV suave opcional existente também acompanha essas Bankais; opção `cinematicReleaseFov` preservada. Não há troca forçada de câmera nem uma sequência de vídeo pré-renderizada.

## Habilidades e corpo a corpo

`SignatureChoreography` define **18 curvas** próprias. `SignatureAnimationService` só publica o gesto depois de uma execução aceita; não autoriza, cobra, bloqueia ou atrasa habilidades. Origens, alcance, dano, cura, controle, cargas e impactos continuam nos executores homologados. As curvas voltam a peso zero quando terminam e cedem à guarda, abertura de guarda, esquiva ou uso de item. Mão secundária ocupada não recebe a pose da mão livre.

| Kit / ação | Gesto acrescentado |
|---|---|
| Senbon: corte / cinco cortes | Varredura dirigida; Bankai alterna sentidos e inclui cortes diagonais/último descendente de acordo com a sequência existente. |
| Fluxo de pétalas | Preparação superior curta e projeção da espada para a frente. |
| Convergência | Duas mãos recolhem/comprimem o fluxo. Prisão, levitação e dano contínuo anteriores preservados. |
| Guarda de pétalas | Espada e mão livre assumem postura protetora. Não modifica o bloqueio com X ou sua mitigação. |
| Jardim floral / reativação | Gesto de plantar/abrir o campo e de recolher/liberar a segunda explosão. Raio **35** e janela **2–6 s** permanecem. Reativação recebe o tempo restante efetivo do finalizador. |
| Senkei / Ikka | Abertura lateral e mãos conduzindo o fechamento. Lâminas de Senkei crescem nos primeiros dez ticks visuais; dano radial da reativação continua do servidor. |
| Hakuteiken | Preparação contida seguida de espada/tronco alinhados ao dash existente de 16 blocos após 16 ticks de carga. Asas e traço branco aprovados mantidos. |
| Benihime: Nake | Corte de projeção com ombro/tronco acompanhando o disparo. |
| Chikasumi | Postura protetora breve de espada e mão livre; escudo existente mantido. |
| Shibari | Mãos estendem e recolhem a rede. Diâmetro **20**, origem acima do solo, contração/puxão e captura anteriores preservados. |
| Hiasobi | Mãos fecham o gesto da rede e liberam o finalizador. Dano continua radial em todos os alvos elegíveis da rede. |
| Dissecção | Incisão curta e giro preciso de torso/ombro. |
| Reconstrução | Mãos sustentam o tratamento com detalhe luminoso nos pulsos; seleção de aliado, cura e três pulsos mantidos. |
| Reestruturação corporal | Mãos/braços se recompõem e fios carmesins contornam o corpo. Três impactos e buffs anteriores preservados. |
| Abertura de caminho | Braço aponta/abre o corredor; gesto curto acompanha os seis ticks do executor. Três cargas e uso vertical preservados. |

Também há alternância cosmética de golpes básicos para Senbonzakura/Benihime a partir do swing vanilla replicado. **Não foram migradas para as hitboxes Better Combat nesta rodada:** isso evita limitar o alcance passivo extra de Senbonzakura ao teto de três blocos do adaptador piloto. Dano, cooldown de ataque, stamina e política de alcance continuam no caminho anterior. Asauchi/Ryūjin/Hyōrinmaru mantêm seus perfis Better Combat. As animações cosméticas não equivalem a um novo sistema autoritativo de combos.

## Efeitos, rede e limpeza

- Pétalas próprias do mod continuam a base de Byakuya; partículas suaves acompanham os gestos, sem recriar todo o jardim nem substituir Senkei/Hakuteiken por chamas. Urahara usa poeira carmesim e pequenos detalhes brancos, preservando a rede, escudo, corredores e geometria articulada existentes.
- Gestos recebidos são validados por identidade, forma, tipo, idade e duração de até 60 ticks. Até 24 gestos no cliente; expiração por timeline e limpeza por mundo/desconexão. Registro no servidor é transitório, uma ação cosmética por dono, com cancelamento por identidade para que a limpeza do outro kit não apague uma animação válida.
- Snapshot público inclui `signatureView`, para dono e observador tardio. Não é gravado em NBT de personagem. Cancelamentos centrais, morte/perda de estado, forma, arma e prazo encerram o registro. Transformações usam o snapshot `releaseView` existente.
- Nova emissão de detalhes de habilidades tem teto global de 192 partículas/tick no cliente, distância de 64 blocos e redução pelas configurações gráficas. Liberações têm emissão local pequena por dono/tick. Os orçamentos anteriores de campo/rede/manifestação continuam separados; não se afirma benchmark de FPS.
- **Rede 2.22**, schema persistente **9** e revisão de técnicas **2** preservados. Cliente e servidor precisam usar a mesma build. Nenhum reset/migração adicional de maestria, recursos ou inventário.

## Validação e pacote

`check build prepareCombatTestPack runGameTestServer` aprovados: **107 regressões e 132 GameTests obrigatórios**, log `build/cycle59-validation.log`. Regressões novas cobrem as 18 curvas/espelhamento/fim, recuperação das três liberações e limites das lâminas de ativação. Quatro GameTests novos cobrem commit único das três Bankais/locks, cancelamento Shikai antes do débito, snapshot de execução aceita sem persistência ou cobrança extra, e ausência de gesto em execução rejeitada. Suíte anterior de gelo/fogo, campos, prisão, cura, cargas, dano, aliados, terreno e cancelamentos mantida; nenhum teste removido.

Pacote atualizado: `build/combat-test-pack/bleach-bettercombat-piloto.zip`, cinco JARs, mesmas quatro dependências do ciclo 58. Trocar somente Bleach se elas já estão instaladas. Sem push ou merge. Documentação desta frente mantida em `Docs/poderes`.

Ainda precisa de playtest: primeira/terceira pessoa, destro/canhoto e slim, mão secundária ocupada, retorno à postura normal, entrada de guarda durante gesto cosmético, cinco cortes, carga/dash Hakuteiken, janela do Jardim, Senkei e fechamento radial, Shibari/Hiasobi, cura/reestruturação/cargas verticais, manifestação no terreno e observador chegando durante a liberação. Não houve renderização/captura do cliente Minecraft ou teste real com dois clientes nesta execução. Não tratar aprovação dos visuais anteriores como homologação automática desta rodada.

Próximos passos recomendados após o teste: ajustar leitura e sincronismo com o feedback; então avaliar animação/modelo final dos inimigos e integração do Zaraki quando o commit da equipe estiver disponível. Aizen continua adiado; Gin fora do planejamento, Ishida e demais prioridades preservados no roadmap 51. Não foram criados novos personagens nesta entrega.
