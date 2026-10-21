# Ciclo 84 — combate Benihime e revisão da Hyōrinmaru

Pedido: melhorar manejo da espada e movimento corporal da Benihime; revisar as tentativas recentes da Hyōrinmaru. Branch inicial `Feature-Poderes-bankais`, limpa, HEAD `c206585`, acompanhando origin. Sem checkout, reset, commit, merge ou push. Zaraki preservada.

## Diagnóstico da branch

As mudanças dos ciclos 82–83 já estavam integradas pelo PR22. O plano descrevia 140 ticks, mas a câmera/manifestação usavam 70 e a liberação do jogador 60. Além da compressão e da interrupção visual, o evento que confirma a Bankai limpava as técnicas e descartava sua própria hidra. A câmera a cinco blocos cortava um modelo que ultrapassa dez blocos de altura. As capturas anteriores em `media/hyorinmaru-qa` mostram essa perda de enquadramento e desaparecimento.

O renderizador selecionava a hidra de três cabeças para **qualquer** dragão de Bankai. Portanto o ataque de três projéteis virava três hidras, perdendo a composição dos três dragões previamente aprovada. A rotina adicional de neve também não filtrava identidade/equipamento, apesar de ser chamada para todos os jogadores.

No gerador Blockbench, os grupos eram exportados como raízes independentes e nenhuma animação era incluída no `.bbmodel`. As faces Gecko usavam retângulos de quatro coordenadas em `uv`, sem `uv_size`, em lugar do formato por face suportado. O argumento `--check` regravava arquivos em vez de conferir.

Na Benihime, o perfil Better Combat era carregado, porém a preparação tinha pouco arco, o braço livre permanecia praticamente congelado e corpo/cabeça/pernas não acompanhavam adequadamente os golpes. Alterar apenas a coreografia antiga de habilidades não corrigiria esses ataques básicos.

## Alterações

- Três curvas originais de esgrima Benihime: preparação alta e corte diagonal, retorno transversal, estocada com recuo e extensão. Nove marcos por canal, contrapeso do braço livre, transferência visual de peso, contrarrotação da cabeça e recuperação. Autoria comum em `tools/models/benihime_combat.py`, com runtime Better Combat e referência editável gerados juntos. Alcance, multiplicadores, upswing e hitboxes básicos preservados.
- Transformação Hyōrinmaru sincronizada em 140 ticks para liberação, pose, câmera e manifestação; demais identidades continuam com suas durações anteriores. A limpeza de técnicas durante o evento de confirmação não encerra a apresentação que está sendo confirmada. Interrupções reais continuam removendo-a.
- Hidra cenográfica identificada pelo tamanho sincronizado da entidade Phantom, separada dos projéteis. Três dragões de ataque voltam a usar o modelo alongado detalhado anterior, com escala, trajetória e dano preservados.
- Hidra oito blocos atrás do jogador, crescimento suavizado e animação própria de emergência/abertura das três mandíbulas. Câmera recua até aproximadamente 25 blocos, enquadra o conjunto e retorna a partir da posição efetiva do último plano, respeitando a perspectiva anterior. Teclas/dano devolvem o controle da câmera.
- UVs por face corrigidas; dentes nas três bocas e asas segmentadas em cristais. Fonte Blockbench com hierarquia única e animações, coerente com os arquivos de runtime. `--check` agora compara sem escrever.
- Aura adicional limitada à Hyōrinmaru equipada e reduzida de uma emissão a cada dois ticks para oito, sem disputar a revelação.
- Cancelamento limpa manifestação também após mudança de dimensão; dano interrompe a liberação; desconexão e parada do servidor removem estado temporário.
- Rede **2.32**: clientes e servidor precisam do mesmo pacote. Schema 9 e revisão de técnicas 3 preservados.

## Verificação

Skill utilizada: `.agents/skills/bleach-mod-dev/SKILL.md`. Ferramentas locais: Java 17, Gradle Wrapper, Better Combat/PlayerAnimator/GeckoLib já instalados e geradores Python do projeto. Sem instalar bibliotecas novas ou enviar arquivos a serviços externos.

Baseline: `check runGameTestServer --offline --console=plain` passou com **137 regressões e 165 GameTests**, exit 0 (`build/cycle84-baseline.log`).

Testes adicionados: carregamento real do modelo/animação da hidra e UVs, duração integral até depois do commit, remoção ao concluir e interrupção por desequipar. Comparação de assets: `python tools/models/ice_hydra.py --check`.

Resultados finais, evidências do cliente e limitações são registrados abaixo após as execuções.

## Roadmap obrigatório

Esta rodada reabre os itens 4 (combate Benihime) e 7 (Hyōrinmaru) por instrução do usuário. Não cancela os itens 1–3 nem antecipa novos personagens. Permanecem: entrada/áudio Benihime com aceite final; Higashi; Investida/Rajada Ryūjin; combate e kit Benihime; Uryū; Mentor/Proficiências dependentes da equipe; Hyōrinmaru/Blockbench. Depois, NPCs de combate e quests da lore. Aizen adiado; Gin fora da fila. A ordem e os critérios completos continuam no documento 72 e nas atualizações subsequentes.
