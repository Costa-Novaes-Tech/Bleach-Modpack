# 113 — Auditoria da gravação e divisão em três frentes

26/10/2026. Base inspecionada: `Feature-Poderes-bankais`, `23b80e0`, sincronizada com origin. Esta rodada altera documentação e produz uma proposta sonora fora do runtime. Nenhuma implementação nova, checkout, commit, push ou instalação de dependência.

## Material e limites da avaliação

Gravação do usuário: `Gravando 2026-10-26 131228.mp4`, encontrada em `C:/Users/joaop/Downloads/Áudios e vídeos para revisão/`. Duração 263,30s, 1920×1020, 30fps. Hash, tamanho e origem em [manifest](qa/cycle113/manifest.json). Originais preservados fora do repositório.

Revisão geral por marcos exatos de 8s e inspeção de quadros consecutivos a 30fps em janelas críticas. Não foram examinados individualmente todos os cerca de 7.899 quadros. Contatos incluem timestamps e número de quadro com origem zero. A primeira extração experimental por filtro fps foi descartada como referência temporal; os arquivos `exact-overview` usam seleção de quadros exatos. Os contatos são evidência da gravação real, não renderizações de um protótipo.

Não houve escuta subjetiva validada nesta rodada. Mixagem proposta tem verificação técnica de amplitude; aprovação auditiva continua com o usuário. Vídeo sem telemetria de input não comprova quando cada clique foi realizado. Quadros quase repetidos não distinguem, sozinhos, trava do cliente, captura ou pose mantida.

## Veredito visual e hipóteses a reproduzir

1. **Ryūjin, aproximadamente 02:48–03:23:** a transformação ocupa um trecho longo demais. Entre 02:56 e 03:07 o sol continua dominante e a composição muda pouco; a energia não acompanha a duração. A imagem frontal melhorou, mas o jogador fica pequeno e o sol vira uma massa de cartões de fogo muito brilhantes. Encurtar a espera e organizar fogo, fumaça e cinzas por fase traz mais resultado que aumentar continuamente partículas. [Marcos do sol](qa/cycle113/transformation-2.jpg).
2. **Revelação, 03:20–03:24:** nome/pose e retorno aparecem, mas a câmera lateraliza e depois passa muito perto do personagem no retorno. Planejar um único percurso frontal, aproximar somente na lâmina e interpolar até a câmera anterior sem atravessar o corpo. [Retorno](qa/cycle113/transformation-4.jpg). A posição exata do input não está gravada: não atribuir automaticamente toda rotação ao caminho programado.
3. **Senbon, 00:43–00:45:** a espada realmente muda de orientação; não é correto afirmar que permanece imóvel em toda a gravação. Em 43,667s, quadro 1310, o rastro de pétalas aparece à direita; em 44,000–44,367s, quadros 1320–1331, arma/punho giram enquanto o rastro permanece deslocado. É necessário alinhar a passagem ativa da lâmina, o nascimento do rastro e o contato, sem confundir partículas que já foram emitidas com partículas ainda presas à arma. [Quadros consecutivos](qa/cycle113/senbon_melee-frames-2.jpg).
4. **Benihime e comparações iniciais:** há movimento visível de lâmina nos trechos comparativos; a crítica deve ser sobre empunhadura, arco, identidade e sincronização, não ausência total de movimento. O jogador troca armas enquanto o HUD ainda identifica Senbonzakura; perto de 03:40 também troca Asauchi/Benihime com HUD Ryūjin. Repetir com identidade vinculada e item corretos antes de concluir qual controlador está errado. Não usar Nozarashi como alvo de correção: Zaraki está preservada.
5. **C da Ryūjin, 03:51–03:53:** preparação frontal com espada elevada é legível. A partir de aproximadamente 231,8s há uma sequência quase congelada, inclusive partículas/elementos de cena, que compromete a leitura do golpe. Não considero a descida/impacto homologados por esse trecho. Investigar frametime e atualização de blocos no mesmo cenário, além do controlador; o ciclo111 já registrou hitch associado ao impacto em terreno. [Preparação](qa/cycle113/ryujin_supreme-frames-2.jpg) e [interrupção](qa/cycle113/ryujin_supreme-frames-3.jpg). Não compensar uma trava acelerando keyframes.

O ciclo112 confirmou que os seis assets customizados de Benihime/Senbon não têm canais rightItem/leftItem, enquanto a referência nativa possui canais de item. Isso orienta o diagnóstico; não prova que adicionar esses canais resolverá tudo. O item pode herdar o braço. Conferir pivô/display, renderer de primeira pessoa, espelhamento e camadas concorrentes antes de corrigir.

## Proposta Ryūjin: 12 segundos, sujeita ao aceite

- **0–2s:** câmera frontal entra com suavidade; jogador prepara a espada; ignição cresce atrás dele. Som grave curto e crepitação ascendente.
- **2–4,5s:** fala “Bankai” sem alterar pitch/velocidade. Sol atinge seu tamanho final, pulsa uma vez; jogador permanece legível à frente. Música e rugido do fogo recuam sob a voz.
- **4,5–7s:** gesto de selamento contínuo. Fogo contrai do exterior ao centro e segue até a lâmina; fumaça e cinzas deixam clara a perda do volume solar. Sucção crescente termina seca, sem explosão longa.
- **7–8s:** breve silêncio relativo, lâmina carbonizada revelada com aproximação moderada. Espada apontada para fora do corpo; pose final só durante a transformação.
- **8–10,5s:** nome completo com fundo baixo, brasas discretas e leve tremulação de calor. Evitar chama diante do rosto e da espada.
- **10,5–12s:** câmera e pose retornam suavemente ao combate. Nada de cortes para dentro do personagem ou offsets persistentes.

Não basta comprimir o áudio de 40s ou acelerar todas as curvas. Reautorizar duração das fases, cues de áudio/legenda e curvas de câmera/pose em um único relógio. Manter contrato de gameplay vigente: custo, commit, liberação/cancelamento, dano e proteção não ganham duração adicional pela cinematográfica. Verificar caminho compacto/configuração existente antes de substituí-lo; não manter duas durações contraditórias acidentalmente.

## Amostra sonora original, fora do mod

[Prévia com as falas existentes](qa/cycle113/ryujin-12s-mix-preview.mp3) e [fundo original com efeitos](qa/cycle113/ryujin-12s-original-bed.mp3). Síntese procedural local com Python/NumPy e FFmpeg disponível; sem serviço pago, download de música, clonagem de voz ou nova biblioteca runtime. Camadas: drone grave, pulso de ignição, ruído/estalos de fogo, sucção e nota de revelação. É um estudo de ritmo e espaço da fala, não trilha final homologada.

Fala Bankai decodificada: 1,35s, proposta em 2s; nome: 2,24s, proposta em 8s. Vozes já existentes no projeto, preservadas quanto a pitch/velocidade. Fundo recua em ambas as falas. Pico linear do mix antes da codificação aproximadamente 0,581; RMS −22,56dBFS. Não houve escuta subjetiva confirmada nem normalização de volume percebido/LUFS; verificar inteligibilidade no jogo, inclusive com sliders baixos. Código reproduzível em [package_review.py](qa/cycle113/package_review.py). Não referenciado por sounds.json nem instalado em recursos.

## Divisão proposta, sem despachar outras conversas

**Frente Codex — contrato comum e Ryūjin.** Investigar composição Better Combat/PlayerAnimator/item e âncoras compartilhadas; resolver a causa comum primeiro, sem uniformizar identidades. Reautorizar Ryūjin para a duração aprovada, música/SFX/câmera; validar C em terreno com medição de frametime. Responsável por compatibilidade, mixins/render compartilhado, regressões globais, integração e documentos de continuidade. Havendo necessidade de mudar contrato comum, entregar a alteração e comunicar a base antes dos agentes concluírem suas animações.

**Frente Antigravity 1 — Senbonzakura.** Combo próprio com arma realmente empunhada e rastro sincronizado; refino visual de Hakuteiken somente onde necessário à leitura da arma. Preservar palco/cinemática aprovada, Jardim fixo e mecânicas. [Prompt](114-prompt-antigravity-senbon-combate-2026-10-26.md).

**Frente Antigravity 2 — Benihime.** Combo próprio limpo com identidade técnica, canal de item e punho coerentes; efeitos presos à lâmina onde apropriado. Não reabrir a entrada da boneca validada nem implementar novo kit/rebalanceamento nesta tarefa. [Prompt](115-prompt-antigravity-benihime-combate-2026-10-26.md).

As três frentes precisam de checkouts isolados para trabalhar simultaneamente; branches no mesmo diretório não isolam edições. Não criar/alternar worktrees nesta rodada de planejamento. Dois agentes não devem editar o mesmo renderer/controlador comum. Entregar primeiro um golpe completo demonstrado no Minecraft real; depois expandir o combo.

## Roadmap obrigatório preservado

1. Benihime entrada validada pelo usuário; preservar, tratar áudio apenas por feedback específico.
2. Higashi: revisão final do usuário pendente; não reabrir execução como parte deste diagnóstico.
3. Ryūjin investida/rajada aprovadas: preservar. Transformação longa agora rejeitada por duração; replanejamento acima.
4. Benihime combate/kit e revisão transversal112, incluindo Senbon. Substituição da habilidade selada de boneca continua decisão de design pendente.
5. Uryū Ishida: contrato Quincy com equipe antes de implementação.
6. Mentor e Proficiências: adiados para confirmação da equipe.
7. Hyōrinmaru: melhorias de rig/modelo/transformação no Blockbench, sem reabrir nesta rodada.
8. Eficiência e limpeza: medir partículas, frametimes e impacto de alterações de terreno; inventário seguro antes de remover/arquivar qualquer coisa. Nenhuma pasta apagada agora.

Após esses itens: NPCs de combate e quests de lore. Aizen adiado; Gin fora; Zaraki intacta. Builds/GameTests históricos dos ciclos anteriores não foram reexecutados nesta revisão documental e não homologam os movimentos do vídeo.
