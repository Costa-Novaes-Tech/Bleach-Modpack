# Ciclo 76 — Investida e Rajada Ryūjin

20/10/2026. Branch `Feature-Poderes-bankais`, referência inicial `b398fdc`; árvore limpa antes das verificações (o Gradle depois alterou logs gerados). Sem checkout, commit, push ou merge. O usuário considerou **Higashi boa no estado atual** e autorizou avançar antes da nova bateria. Higashi e Zaraki não receberam alterações de produção.

## Contrato e implementação

Item 3 da [ordem obrigatória](72-revisao-geral-roadmap-gamedesign-2026-10-20.md), seguindo os [estudos aprovados](57-prototipos-correcao-visual-ryujin-2026-10-17.md) e a [implementação anterior](58-ryujin-coreografias-chamas-implementadas-2026-10-17.md).

- **Rajada (Shikai, H):** preparação alta, corte descendente no tick 6, sustentação/recuo e recuperação até o tick 16. Braço auxiliar acompanha o gesto quando a mão secundária está vazia. A frente de fogo tem crista irregular, centro mais alto, bordas menores, cinzas e fumaça lateral; substitui a grade retangular rígida. Partículas nativas FLAME/SMALL_FLAME, sem quad preenchido translúcido. Frente continua avançando de 0 a 8 blocos nos ticks 6–14, na direção registrada pelo servidor. Não se torna um Leque.
- **Investida (Bankai, H):** tronco inclinado, lâmina apontada à frente, braço auxiliar recolhido, passadas alternadas em ciclos menores e recuperação no tick 16. O rastro amostra o deslocamento efetivo, inclusive curvas e variação vertical, em vez de prolongar a direção inicial. Sem emissões de percurso parado; sem ponte de partículas em teleportes acima de 3 blocos por amostra. Cache separado por entidade/ativação e limpo no término, desequipar, afastamento ou troca de mundo.
- **Legibilidade:** explosão inicial da Investida reduzida de 68 para 40 partículas de servidor; rastro de servidor de 25 para 15 por tick, com cinzas em vez das duas partículas de lava. O traço client-side conecta o percurso com até 36 chamas e quatro cinzas por tick, além da lâmina. Orçamento client-side anterior mantido: 640 global/tick, 128 por emissor, redução pela configuração de partículas/distância. Esse orçamento não inclui partículas enviadas pelo servidor.

**Preservados:** dano, custo, recarga, autoridade de colisão/movimento/acertos, proteção de aliados, duração da Investida de 16 ticks a 1,25 bloco/tick (aproximadamente 20 blocos sem colisão), alcance/duração da Rajada, Leque e demais técnicas. Nenhum segundo dash ou invulnerabilidade. As poses continuam cosméticas via PlayerAnimator; nenhuma hitbox Better Combat foi migrada. Protocolo **2.29**, schema **9**, mod **0.2.0**. Sem bibliotecas novas, Photon obrigatório ou instalação de ferramentas.

## Laboratório e reprodução

Novo `RyujinQaSequence`, restrito a desenvolvimento, flags explícitas, servidor integrado/admin, diretório separado `run-benihime-qa` e save novo com prefixo `Benihime-QA-68-`. Não altera saves pessoais nem disponibiliza comando de produção.

```powershell
.\gradlew.bat runClient -PbenihimeQa=true -PcinematicQaFast=true -PryujinQa=true --offline --console=plain
```

Quatro cenários: Rajada aberta/parede e Investida aberta/parede. Três Husks de laboratório com 1000 HP, nos 4/8/12 blocos; parede local a seis blocos. Altura obtida do terreno, sem assumir Y=4. Investida aberta inclui mudança de direção para verificar o rastro. Capturas do framebuffer nos ticks 17–52 do cenário, com posição/tick/FPS observado; câmera lateral só para inspeção. Retorno à câmera/HUD anteriores após a sequência. Vida/deslocamento são registrados pelo servidor; a suíte GameTest é a referência para as regras, não uma inferência a partir da imagem.

## Validação

Linha de base antes de editar: `check runGameTestServer --offline --console=plain`, exit 0, **127 regressões / 155 GameTests**, 2m08s. Primeira compilação/check após as mudanças: **129 regressões**, exit 0, 1m00s. Dois cenários novos cobrem amostragem de curvas/teleporte e limites da crista/pose/recuperação. Uma inicialização preliminar do cliente foi encerrada pelo agente para carregar a orientação final da lâmina; não é apresentada como execução visual aprovada.

A primeira sequência gráfica concluiu e restaurou câmera/HUD. Deslocamento aberto medido: 19,7134 blocos (inclui curva); diante da parede: 5,20 blocos. A configuração pacífica herdada fez os Husks desaparecerem (`health=-1`): resultados de alvos desta sequência são **inválidos**. Laboratório corrigido para NORMAL antes da criação; repetir sem alterar gameplay. Marcos inspecionados mostram chamas nativas em faixa e corpo visível na Investida, mas não homologam fluidez integral. A repetição em NORMAL concluiu os quatro cenários. Saúde registrada dos alvos originalmente com 1000 HP (4/8/12 blocos): Rajada aberta [990,9152; 990,9152; 1000]; Rajada com parede [990,9152; 1000; 1000]; Investida aberta [992,096; 992,096; 992,096]; Investida com parede [992,096; 1000; 1000]. Esses totais incluem o processamento normal do jogo, não uma medição isolada do dano nominal. Deslocamento aberto nesta repetição: **19,2984 blocos** com mudança de direção/contatos; com parede: **5,20 blocos**. Todos terminaram com flameDashTicks=0. O cliente foi fechado de forma normal pelo agente após a sequência, antes da compilação final; não foi deixado executando.

[Evidência conjunta dos marcos](qa/ryujin/cycle76/marcos.jpg), [origem/hash dos PNGs](qa/ryujin/cycle76/capture-manifest.json), [log do cliente corrigido](qa/ryujin/cycle76/validation/validation-cycle76-client-final.log). Clipes **sem áudio montados de quadros amostrados**, com intervalos de tick preservados; não são gravação contínua:

- [Rajada aberta](qa/ryujin/cycle76/barrage_open/amostras-sem-audio.mp4): 14 quadros; carregamento inicial de chunks/captura atrasada limitam a observação da preparação.
- [Rajada com parede](qa/ryujin/cycle76/barrage_wall/amostras-sem-audio.mp4): 36 quadros; sequência mais legível de preparo/emissão/retorno.
- [Investida aberta](qa/ryujin/cycle76/dash_open/amostras-sem-audio.mp4): 34 quadros; avanço, contatos e rastro distribuído no percurso.
- [Investida com parede](qa/ryujin/cycle76/dash_wall/amostras-sem-audio.mp4): 36 quadros; bloqueio do avanço, recuperação e dissipação.

FPS informado pelo cliente durante as amostras (mínimo/mediana/máximo): Rajada aberta 1/6/28; Rajada parede 34/50/79; Investida aberta 34/41/83; Investida parede 51/66/85. Valores do HUD/API em desenvolvimento, sob captura e carregamento; **não são benchmark de desempenho nem medição de frametime**. Save plano criativo novo, 1280×720, Java17/Forge47.4.10, perfil base, sem observador remoto. Não foi realizada escuta, inspeção gráfica Photon, dois clientes, terreno irregular ou ensaio específico de primeira pessoa nesta rodada.

Avaliação do agente nos marcos (0–5): distinção Rajada/Leque 4; legibilidade do corpo/rastro 3; enquadramento lateral de laboratório 4. Preparação da Rajada aberta prejudicada pelo carregamento: pendente de observação limpa. Sem nota de fluidez contínua e sem afirmar reprodução exata das artes por aprovação técnica. **Pendente de validação visual do usuário**, especialmente espada/passadas em terceira pessoa e transições em primeira pessoa. **Suítes finais:** base `check build runGameTestServer prepareCombatTestPack --offline --console=plain`: exit 0, **129 regressões/155 GameTests**, 1m47s. Perfil opcional `-PphotonPilot=true check build runGameTestServer prepareCombatTestPack preparePhotonTestPack --offline --console=plain`, executado depois e sem cliente aberto: exit 0, **129 regressões/155 GameTests**, 1m37s. Persistem os diagnósticos recuperados conhecidos do scanner/editor LDLib no dedicado; nenhum GameTest obrigatório falhou. Isso não equivale a teste gráfico do editor Photon ou multiplayer.

[Log base final](qa/ryujin/cycle76/validation/validation-cycle76-base-final.log), [log Photon final](qa/ryujin/cycle76/validation/validation-cycle76-photon-final.log), [manifesto dos pacotes](qa/ryujin/cycle76/delivery-manifest.json). Cinco/sete JARs e CRC conferidos; JAR do mod idêntico nas duas pastas e em build/libs, incluindo a nova geometria. Diretórios de entrega: `build/combat-test-pack/mods` e `build/photon-test-pack`. Nenhuma mudança de versão/dependências. `git diff --check` sem erros. Logs gerados na raiz restaurados ao conteúdo anterior; somente código pertinente e documentação/evidências sob Docs/poderes permanecem no diff.

## Próximo teste manual

Com Ryūjin equipada, usar **H em Shikai** para Rajada e **H em Bankai** para Investida. Conferir em primeira/terceira pessoa, em campo aberto e com parede: preparação/corte e emissão; lâmina/passadas e rastro durante curva; recuperação ao parar. A câmera lateral das evidências não faz parte dos ataques normais. Não é necessário repetir Higashi para aceitar esta alteração, salvo regressão observada. Revisão do áudio/entrada Benihime continua na pendência própria.

## Roadmap preservado

Item 2 aceito no estado atual pelo usuário; sem reabrir Higashi. Item 3 segue para teste artístico do usuário após esta entrega. Item 1 (entrada/áudio Benihime) continua pendente de revisão final. Depois: **combate da espada e demais técnicas Benihime → Ishida com contrato Quincy → Mentor/Proficiências após equipe → Hyōrinmaru/Blockbench**. Mais Hollows estão em desenvolvimento informado pela equipe; NPCs de combate e quests da lore são a frente proposta após concluir a lista. Aizen adiado; Gin fora da fila de poderes.
