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

Resultados finais, ambos com exit 0:

- Base: `check build runGameTestServer prepareCombatTestPack --offline --console=plain`: **137 regressões / 168 GameTests**.
- Photon/LDLib: `-PphotonPilot=true check build runGameTestServer prepareCombatTestPack preparePhotonTestPack --offline --console=plain`: **137 regressões / 168 GameTests**.
- `git diff --check` e comparação do gerador da hidra passaram.
- Pacotes base/Photon com cinco/sete JARs. O mod nos dois pacotes e em `build/libs` tem o mesmo SHA-256: `0b057bfac95f610e8383bf4c3b9ce20e726a134834c1807c65a70d6cf3eb63df`; CRC ZIP e assets conferidos. Manifesto em [packages.json](qa/cycle84/packages.json).

Cliente Forge real base executou **dez cenários e capturou 453 quadros**: seis cenários Hyōrinmaru / 102 quadros e quatro cenários Benihime / 351 quadros. Combate Benihime usa a tecla de ataque real e o fluxo Better Combat: alvos perderam vida nas formas selada, Shikai e Bankai e na perspectiva de primeira pessoa. Três animações próprias aparecem no registro carregado. O retorno de câmera/HUD e o encerramento normal do cliente constam no log.

Na transformação Hyōrinmaru, os quadros mostram a hidra inteira, a permanência após o commit, as três mandíbulas e o desaparecimento ao encerrar. A primeira passagem gráfica revelou fogo solar herdado do Phantom; corrigido e reexecutado na passagem final, sem esse fogo. A câmera se afasta mais do que na primeira proposta para comportar a escala efetiva.

Evidências:

- [Prévia Benihime](qa/cycle84/benihime-combate-amostrado.gif) e [prévia da transformação Hyōrinmaru](qa/cycle84/hyorinmaru-transformacao-amostrada.gif): GIFs derivados de capturas reais, redimensionados, **amostrados e sem áudio**. Não são gravações contínuas.
- [Hidra durante o rugido](qa/cycle84/01_bankai_transformation/frame-t104.png), [retorno de câmera](qa/cycle84/01_bankai_transformation/frame-t140.png) e [fim da sequência](qa/cycle84/01_bankai_transformation/frame-t152.png).
- [Manifesto de capturas](qa/cycle84/evidence.json): caminhos absolutos das fontes completas em `run-benihime-qa/base`, contagens, cópias selecionadas e hashes. Os `source-frames.jsonl` preservam a telemetria completa; nem todos os PNGs citados neles foram duplicados em Docs.
- [Baseline](qa/cycle84/cycle84-baseline.log), [cliente final](qa/cycle84/cycle84-combined-client.log), [base final](qa/cycle84/cycle84-base-final.log), [Photon final](qa/cycle84/cycle84-photon-final.log).

### Recuperação após interrupção

Durante a interrupção ocorreu externamente um reset/checkout para `codex/design-poderes-criaturas` e o commit830ad64 de artes. Os arquivos de código modificados deixaram o checkout; o relatório84 e o gerador Benihime foram incluídos nesse commit externo. O usuário confirmou retornar à `Feature-Poderes-bankais`; o agente realizou apenas esse switch autorizado e recompôs as correções, preservando a branch/commit de design. Não fez commit, reset, merge ou push.

Os assets recuperados foram comparados ao conteúdo da compilação gráfica. A desmontagem dos métodos das **20 classes de runtime/QA recuperadas é idêntica** à compilação usada nas capturas, sem depender de igualdade das informações de linha de debug: [comparação](qa/cycle84/recovery-bytecode.json). As baterias finais base e Photon foram executadas depois da recuperação.

### Auditoria visual e limites

Avaliação provisória por inspeção dos quadros (0–5): manejo/contrapeso Benihime 3, legibilidade dos cortes 3, enquadramento Hyōrinmaru 4, silhueta/modelagem da hidra 3. A preparação e os arcos ficaram mais claros, mas articulação fina dos cotovelos/punhos e proporções de cabeça, pescoço e asas ainda podem evoluir. A hidra continua um modelo de protótipo, com geometria visivelmente cúbica; olhos vermelhos têm textura, não canal emissivo implementado. Não confundir esta correção técnica com reprodução final das artes conceituais.

**PENDENTE DE VALIDAÇÃO VISUAL pelo usuário.** Inspeção por quadros/GIF não comprova fluidez contínua nem avaliação auditiva. Não houve nesta rodada escuta/mixagem, teste gráfico Photon, dois clientes/observador, estresse de partículas, variantes de skin/mão esquerda ou toda a matriz de terrenos/interrupções. GameTests cobrem servidor e carregamento dos assets, não substituem essas verificações. O modelo/cena da Benihime, suas vozes, Higashi e kit Zaraki não foram reformulados nesta rodada.

## Roadmap obrigatório

Esta rodada reabre os itens 4 (combate Benihime) e 7 (Hyōrinmaru) por instrução do usuário. Não cancela os itens 1–3 nem antecipa novos personagens. Permanecem: entrada/áudio Benihime com aceite final; Higashi; Investida/Rajada Ryūjin; combate e kit Benihime; Uryū; Mentor/Proficiências dependentes da equipe; Hyōrinmaru/Blockbench. Depois, NPCs de combate e quests da lore. Aizen adiado; Gin fora da fila. A ordem e os critérios completos continuam no documento 72 e nas atualizações subsequentes.

Estado final e resultados: [validation.json](qa/cycle84/validation.json). Logs gerados restaurados; modificações documentais apenas em Docs/poderes.
