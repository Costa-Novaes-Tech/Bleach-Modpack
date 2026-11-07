# Ishida — pranchas de referência para modelagem

05/11/2026 · `Feature-Poderes-bankais`, base `9eb6a05`. Produção de imagens pela ferramenta integrada `image_gen`, conforme pedido do usuário e regra de pares liso/em blocos. Nenhuma aprovação humana destas imagens deve ser presumida. Fontes: plano Uryū, storyboard de transformação de 30/10 e plano de poses/pranchas de 05/11.

## Entregas disponíveis

| Prancha | Conteúdo |
| --- | --- |
| [Ishida base](media/quincy-model-sheets-20261105/ishida-base-multivistas-liso-blocos-v1.png) | Frente, costas, dois perfis, 3/4, cabeça e esquema de casaco; pares de aparência lisa e construção em blocos. |
| [Vollständig completo](media/quincy-model-sheets-20261105/ishida-vollstandig-multivistas-liso-blocos-v1.png) | Frente, costas, perfil e 3/4, esquema superior, duas asas e quatro estrelas. |
| [Asas/estrelas e montagem v2](media/quincy-model-sheets-20261105/quincy-asas-estrelas-construcao-v2.png) | Peças, pivôs dorsais, perfil/projeção superior, dobramento e três vistas da estrela; revisão dos segmentos inferiores para construção por cubos retangulares sobrepostos. |

[Prompts completos e proveniência](media/quincy-model-sheets-20261105/prompts-proveniencia.json). As imagens são estudos de construção, não `.bbmodel`, export GeckoLib, rig homologado ou captura Forge. O corpo Ishida é referência/NPC proposto: não substitui a skin do jogador. As duas asas segmentadas e quatro estrelas seguem a adaptação do projeto, não uma reprodução canônica certificada do anime.

## Evolução do arco — pendência explícita

A prancha pretendida compara **Kojaku → Ginrei Kojaku → armamento na Vollständig**, em mesma escala e com a empunhadura fixa, vistas frontal/lateral/3/4 e pares liso/em blocos. Ginrei continua proposta de armamento intermediário; desenhá-lo não implementa progressão ou nova transformação corporal. A Vollständig não exige uma troca automática do item do jogador.

As duas solicitações de imagem dos arcos falharam na moderação de saída do gerador, categoria `violence`, inclusive a versão somente com objetos em exposição. Não houve imagem válida dos arcos nesta rodada. IDs e prompts estão na proveniência. Não substituí por imagem inexistente nem usei API paga alternativa. A prancha precisa ser concluída antes da modelagem completa do armamento.

## Inspeção e correções necessárias no editor

- As figuras base mostram roupa, cabelo e separação de membros com clareza; as proporções entre linhas lisa/em blocos ainda variam levemente. Fixar escala pelo rig real do Minecraft, não pelos quadradinhos da imagem.
- A forma completa mostra quatro estrelas nas vistas de conjunto; no perfil pares podem se sobrepor. Usar a planta superior e estrelas independentes no rig para confirmar profundidade e contagem.
- **O rodapé da prancha Vollständig inverte as etiquetas de X esquerda/direita em relação à prancha de montagem.** Para autoria, declarar frente `-Z`, costas `+Z`, esquerda anatômica `-X`, direita `+X` na convenção deste estudo e conferir no editor antes de exportar. Não copiar o rodapé conflitante.
- A prancha completa sugere asas ciano quase planas; a montagem v2 mostra peças brancas com faixas ciano e maior espessura. A montagem v2 é a referência de construção proposta; definir material final antes de exportar. Nenhuma das imagens autoriza alterar a fonte Antigravity sem revisão.
- Os nomes de bones/segmentos são uma proposta de decomposição, não um contrato para renomear os bones existentes. O rig integrado possui contratos prévios: preservar nomes públicos ou produzir mapeamento explícito.
- A primeira montagem gerada ainda continha formas afiladas na linha de blocos; foi corrigida na v2 selecionada. Mesmo na v2, validar cubos, rotações, UV, interseções e custo no Blockbench; raster não fornece geometria ou coordenadas exatas.
- A Vollständig ainda não tem perfil direito isolado com asas; derivar por simetria no editor e capturar ambas as vistas antes de aceitar o modelo.

## Próximo uso

**Retomada solicitada pelo usuário:** pranchas documentadas como etapa de referência visual. O [plano revisto de qualidade e mods](revisao-qualidade-videos-stack-2026-11-05.md) conecta essas imagens à futura modelagem/rig, ao ensaio do arqueiro PlayerAnimator/GeckoLib e aos testes de VFX/shader. A implementação de novos modelos 3D destas pranchas ainda não ocorreu; o rig integrado anterior permanece preservado.

Conferir silhueta com o usuário, finalizar referência do arco e estabelecer uma fonte de proporções. Em perfil Codex isolado, prototipar corpo/arma e asas sem modificar o projeto Antigravity; comparar frente/perfil/costas e poses de guarda, tração, soltura e retorno. Depois reproduzir os assets exatos no Forge. Imagens e hash confirmam entrega dos arquivos, não qualidade de animação ou funcionamento no jogo.
