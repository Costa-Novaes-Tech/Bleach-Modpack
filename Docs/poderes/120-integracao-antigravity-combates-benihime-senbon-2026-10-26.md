# 120 — Integração dos combates Benihime e Senbonzakura

26/10/2026. Pedido: revisar as duas entregas Antigravity, registrar alterações pendentes e fazer merge na `Feature-Poderes-bankais` para o playtest conjunto.

## Git e preservação

As duas origens estavam limpas e já commitadas. Benihime: `codex/ryujin-bankai-sol-selamento`, commits4afa5b2 e **cdbfe59**; nome da branch é histórico. Senbon: `codex/design-poderes-criaturas`, **8e73765**, com merge569f4d6 que já continha a Benihime. Não havia novas alterações não commitadas dos agentes a registrar.

Nossa Ryūjin12s, áudio/câmera e contrato118 estavam locais. Foram preservados no commit **fdca0de** antes da integração. Merge Benihime **5e7973b**; merge Senbon **9713af1**, ambos com estratégiaort, sem conflitos. O teste Java modificado por ambas as frentes foi combinado automaticamente, preservando a regressão da Ryūjin e os novos nomes/canais Benihime. A ancestralidade dos dois destinos foi conferida no HEAD integrado.

Main, worktrees, logs rotativos e relatório local de mídias de outra conversa preservados. Nenhum reset, stash, exclusão ou force-push. Esta rodada registra commits e merges locais; não publica no remoto. A branch Benihime possui um commit local à frente do remoto, mas ele já está incluído no histórico integrado. Não é uma entrega perdida ou pendente de commit.

## O que entrou e revisão crítica

- **Benihime:** ciclo de duas estocadas (`benihime_thrust`, `benihime_thrust_2`) seguido de corte horizontal (`benihime_slash`). Isso inclui mudança do perfil para FORWARD_BOX/0°, FORWARD_BOX/0°, HORIZONTAL_PLANE/100°; não descrever como alteração puramente cosmética. Multiplicador1, alcance e upswing0,45 preservados. Emote e duplicatas Bedrock atualizados com canal de item; begin8/end20/stop30. Recursos diagonal/return removidos sem referências remanescentes no runtime/perfil/testes examinados.
- **Senbonzakura:** canais rightItem e ajustes de braço/corpo nos três assets e duplicatas; perfil de arma preservado. Seus tempos continuambegin0/end12/stop20: não afirmar que recebeu o mesmo fade-in8ticks da Benihime. A suavidade precisa do playtest, especialmente ao encadear golpes.
- **Contrato comum:** a análise de bytecode sustenta que canais do item são consumidos pelo PlayerAnimator, mas sua ausência não demonstra sozinha causa universal de travamento. A linguagem de “causa raiz comprovada” no relatório de origem é mais forte que a evidência disponível. Mantido o contrato118 e a necessidade de verificar cada modelo/controlador.
- **Rastro de pétalas:** nenhum dos agentes alterou o emissor fixo em BetterCombatCompat. Ele continua um ARC do servidor, não uma âncora real da lâmina. A integração dos assets não encerra essa pendência nem toda a revisão transversal112.
- **Relatórios de origem:** o Senbon cita147 regressões/224 GameTests e “sem commit”; são registros que não representam o estado integrado atual. O relatório Benihime cita `test` como prova de regressões e GameTests ainda em execução; `test` isolado não substitui o runner `regressionTest` deste projeto. Utilizar os resultados abaixo. Os relatórios originais foram preservados como proveniência, sem promover suas alegações a homologação.

As entregas não modificaram código Java de gameplay/render da Ryūjin, entradas cinematográficas, Zaraki, Hyorin ou bibliotecas. Fonte Ryūjin12s/áudio permanece idêntica ao commitfdca0de. A entrada da boneca Benihime continua aprovada e fora desta alteração.

## Validação do conjunto integrado

- `check build --offline`: exit0, **148 regressões**,1m36s. Inclui parsing Java dos novos nomes/canais Benihime nos dois formatos.
- `runGameTestServer --offline`: exit0, **233 testes obrigatórios aprovados**,2m52s. Não reutilizada a contagem do relatório Antigravity.
- Verificação estática adicional: seis animações com canal rightItem, números finitos, perfil Benihime na ordem esperada e multiplicadores1. Hashes de fontes e JAR em [assets.json](qa/cycle120/assets.json); script reproduzível [verify_merged_assets.py](qa/cycle120/verify_merged_assets.py).
- Cliente Senbon integrado: exit0,1m54s, seis cenários, combo frontal/perfil/costas e Hakuteiken frontal/perfil/costas. Três casts Hakuteiken com busy=true registrados. Capturas próprias desta integração, além das referências recebidas.
- Cliente Benihime integrado: exit0,1m46s, quatro cenários, combate selado/Shikai/Bankai e Shikai em primeira pessoa. Registry carregou exatamente `benihime_slash`, `benihime_thrust`, `benihime_thrust_2`; sequência restaurou câmera/HUD. O laboratório mistura input do cliente e ataques diretos de QA: capturas são prova de apresentação/carregamento, não medição isolada de dano/stamina por golpe.

Total: **10 cenários,403 registros amostrados,30 PNGs selecionados**. [Origem, ticks e hashes](qa/cycle120/client-captures.json); pranchas [1](qa/cycle120/client-contact-1.jpg), [2](qa/cycle120/client-contact-2.jpg), [3](qa/cycle120/client-contact-3.jpg). Mundos descartáveis novos, sem abrir save pessoal. [Build](qa/cycle120/check-summary.txt), [GameTests](qa/cycle120/gametest-summary.txt), [Senbon](qa/cycle120/client-senbon-summary.txt), [Benihime](qa/cycle120/client-benihime-summary.txt). O script de empacotamento reconhece os formatos distintos dos manifestos de captura dos dois laboratórios; originais preservados.

Inspeção visual confirma espada/movimento visíveis e estocada Benihime projetada à frente nas amostras. Capturas por marcos não comprovam fluidez contínua, todos os golpes em cada perspectiva, canhoto, secundária ocupada ou observador multiplayer. A estética e a sensação do encadeamento continuam sujeitas ao aceite do usuário. Não homologar toda a tarefa112 por compilação ou por essas amostras.

## O que testar agora

1. Benihime em selada/Shikai/Bankai: conferir duas estocadas e corte horizontal, retorno ao início do combo, aderência da guarda ao punho e transição para defesa/técnica. Repetir em primeira/terceira pessoa.
2. Senbon: corte diagonal, retorno horizontal e estocada; conferir posição da espada, recuperação, cadência e Hakuteiken. Rastro fixo ainda pendente, sem declarar resolvido se ficar deslocado.
3. Ryūjin: transformação curta com falas2s/8s, música/fogo e retorno da câmera do ciclo119. C destrutivo mantém a pausa em terreno documentada; não foi otimizado novamente nesta integração.

JAR integrado: `build/libs/bleachmod-0.2.0.jar`, SHA256 **b7c0ff101a3efbd90c915383b8c7d6bcb97b10773745261ef68fed8fe74fd4e0**. Substitui o JAR anterior do ciclo119 para o teste das três frentes juntas. Não exige novas dependências.

## Pendências preservadas

Aceite artístico/auditivo do usuário; vínculo completo espada/punho/VFX e substituição coordenada do emissor Senbon; canhoto/secundária/observador; eficiência da destruição do C; demais itens obrigatórios do roadmap72. Nenhuma limpeza de pasta/worktree nesta rodada. Aizen adiado, Gin fora, Zaraki preservada. NPCs e quests permanecem depois da lista obrigatória.
