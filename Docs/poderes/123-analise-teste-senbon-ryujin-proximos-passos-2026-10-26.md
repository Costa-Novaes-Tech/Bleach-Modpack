# 123 — Teste do usuário: Senbon e Ryūjin; próximos passos

26/10/2026. Branch Feature-Poderes-bankais, referência ef64c45, inicialmente limpa e sincronizada. Somente documentação; sem código, build, cliente, commit, merge ou controle da interface nesta revisão.

## Fonte e limites

Vídeo original: `C:/Users/joaop/Downloads/Gravação/Gravação de Tela 2026-10-26 195049.mp4`, 197.941.211 bytes, 188,27s, 1920×1020, 30fps. SHA256 `0b9e3c8547836b720f80afad3167c99d6e104c4bf7279a4d2d436304e874bfea`.

Visão geral amostrada a cada seis segundos e extração de trechos a 30fps para inspeção por contatos. Não é inspeção individual de todos os quadros do vídeo. Contatos selecionados e metadados em [qa/cycle123](qa/cycle123/source.json); originais preservados. Não houve avaliação auditiva independente nem medição de FPS nesta revisão.

## Observações

- Aproximadamente 00:18–00:24 e 00:54–01:00: duas entradas da Ryūjin, com sol, absorção e revelação da lâmina carbonizada. O usuário aprovou a versão atual: preservar duração, câmera, áudio e VFX, sem outra rodada estética agora.
- Antes de aproximadamente 01:54, a troca do item não corresponde sempre ao HUD, que ainda informa Ryūjin. Não atribuir esses movimentos ao perfil Senbon corretamente vinculado.
- 01:58,2–01:59,2: na vista frontal, a lâmina sobe junto ao ombro e percorre a frente do corpo. Isso fornece uma referência legível para o primeiro golpe; a mão livre participa da postura. O usuário autorizou seguir aos próximos golpes. Não equivale à aprovação de todo o combo ou de todos os ângulos.
- Trecho posterior em Bankai mistura postura, pétalas e técnicas: por exemplo, em 02:50 surgem lâminas de uma habilidade. Não tratar toda emissão de partículas como rastro do ataque básico, nem usar esses frames como prova isolada do combo.
- Não há telemetria de teclas ou ensaio controlado de acerto; dano, offhand, canhoto, interrupções e observador multiplayer continuam sem comprovação nesta gravação.

## Próxima entrega

[Prompt 124](124-prompt-antigravity-senbon-golpes2-3-2026-10-26.md): reconstruir retorno horizontal e estocada, com continuidade real entre os três golpes e arma acompanhando a mão. Primeiro sem partículas; depois de aceite, tratar o rastro em etapa separada sob contrato118. Ryūjin e Benihime aprovadas ficam preservadas.

## Roadmap obrigatório vigente

1. Entrada Benihime: aprovada; preservar.
2. Higashi: revisão final do usuário ainda pendente; aprovação da transformação Ryūjin não fecha automaticamente execução/Higashi.
3. Investida e rajada Ryūjin aprovadas; transformação curta e VFX atuais aprovados nesta rodada. Preservar.
4. Combate Benihime aprovado; substituir a habilidade selada de boneca/isca continua pendente de definição. Não homologar todo o kit por extensão. Revisão transversal arma/mão/efeitos112/118 continua: Senbon etapa2 agora, rastro etapa3 depois.
5. Uryū Ishida: fechar contrato Quincy com a equipe antes de implementar poderes.
6. Mentor e Proficiências: implementação depende das confirmações da equipe; documentação conceitual existente deve ser respeitada.
7. Hyōrinmaru: refino Blockbench/rig, dragões e transformação conforme documentos vigentes, com QA de articulação e resultado no jogo.
8. Eficiência e limpeza: inventário seguro antes de apagar qualquer coisa; permanece a pausa do C Ryūjin ao destruir terreno, registrada119. Aceite artístico não comprova solução de desempenho.

Depois da lista obrigatória: NPCs de combate e quests de lore. GDD paralelo: M11 e revisão M05 conforme121. Aizen adiado; Gin fora; Zaraki preservada. Nenhuma nova implementação atribuída à segunda conversa Antigravity apenas por a Ryūjin estar aprovada.

## Estado das integrações

Senbon etapa1 já integrado2127c26 e documentado122; turbilhão Ryūjin já integrado6a2da98 e documentado no outro122. Relatórios anteriores registram testes; não foram repetidos nesta rodada documental. Não pedir novamente esses mesmos merges como se ainda estivessem pendentes.
