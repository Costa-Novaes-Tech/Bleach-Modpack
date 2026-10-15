# Ciclo 40 — Escudo refinado, testes no criativo e esquiva de 3,5 blocos

Data: 15/10/2026. Branch: `Feature-Poderes-bankais`. Protocolo **2.11**, schema **8**, sem novos campos de rede/save.

## Retorno do usuário

O usuário realizou testes da build com esquiva e defesa e pediu: melhorar o desenho do escudo, permitir as duas ações no criativo, aumentar a esquiva para 3,5 blocos e acrescentar "Ichida" ao planejamento. Nome registrado como **Uryū Ishida**, após Urahara; Byakuya continua próximo. Gin segue retirado, Aizen reservado, Mentor/Proficiências aguardam a equipe e Hyōrinmaru/Tōshirō permanece como revisão final.

Esse retorno confirma que houve playtest e define ajustes concretos; não implica homologação irrestrita de multiplayer, latência ou do novo visual.

## Mudanças efetivas

### Criativo

- Removida a exclusão por `isCreative()` nos serviços de esquiva e guarda. Personagem criado continua obrigatório.
- Esquiva no criativo funciona no chão, com voo desativado; voo/elytra, espectador e demais restrições anteriores continuam recusados. Não adiciona esquiva aérea.
- Guarda pode ser levantada/baixada no criativo, com snapshot público do escudo. A restrição de voo ativo permanece.
- Esquiva continua custando 15 stamina, com intervalo de 12 ticks, sem recarga automática por estar no criativo. Bloqueio continua sem custo de stamina; regras de Postura preservadas.
- Não remove a invulnerabilidade vanilla do criativo nem obriga mobs a atacar esse modo. Para observar perda de Postura por dano real, utilizar sobrevivência/aventura. O suporte ao criativo habilita input/ação/visual; não força eventos de dano que o vanilla normalmente descarta.

### Alcance da esquiva

- Velocidade horizontal passa de **0,5 para 0,7 bloco por tick**, mantendo cinco ticks: **3,5 blocos nominais**.
- Custo, intervalo, direção normalizada, pausa de recuperação e colisões permanecem. A validação usa o volume varrido do passo ampliado, não só seu ponto final.
- Distância real ainda depende de física vanilla, colisões e rede. Não é teleporte ou garantia geométrica de 3,5 blocos em qualquer situação.

### Desenho do escudo

- Nova silhueta mais detalhada, borda dupla com brilho/sombra, painel interno em duas tonalidades e emblema central.
- Preenchimento e barra continuam indicando a reserva. Rachaduras progressivas são escuras e contrastantes; estado crítico usa âmbar; quebra separa metades vermelhas com deslocamento vertical.
- Geometria compartilhada em `common/visual/GuardShield.java`, sem dependências de renderização. HUD e símbolo acima do personagem usam os mesmos retângulos.
- Retângulos agrupam sequências horizontais da mesma cor, substituindo um quad por pixel e reduzindo operações de desenho. Não houve medição de FPS.
- Símbolo no mundo reposicionado para manter distância do nome com o novo tamanho; HUD ajustado para acomodar o desenho e o texto. Visuais/modelos das espadas e bainhas não foram alterados.
- Conferência visual estática dos estados 100%, 60%, 20% e quebra, renderizada a partir da mesma classe usada no jogo: `build/guard-preview/shield-states.png`. Arquivo gerado localmente, sem compromisso de existir em outro checkout. Não equivale a inspeção do renderer em cliente real.

## Arquivos e testes

- `common/DodgePolicy.java`, `DodgeService.java`, `GuardService.java`: alcance e elegibilidade.
- `common/visual/GuardShield.java`, `client/hud/GuardVisuals.java`: desenho compartilhado e renderização.
- `gametest/DodgeGameTests.java`: distância esperada atualizada; novo teste de esquiva criativa com movimento/custo e rejeição de voo/espectador.
- `gametest/GuardGameTests.java`: novo teste de levantar/soltar guarda criativa, snapshot para observador e rejeição de espectador.
- Documentação somente em `Docs/poderes`; roadmap e continuidade atualizados.

`check build runGameTestServer --offline --console=plain` aprovado: **80 regressões e 73 GameTests obrigatórios**, incluindo os dois novos de criativo. Log: `build/cycle40-integration.log`. JAR atualizado em `build/libs/bleachmod-0.2.0.jar`. Diff e links locais revisados; desenho estático conferido visualmente. Aparência em cliente real e mudanças de movimento ainda aguardam nova conferência do usuário.

## Nova conferência em jogo

1. Criativo no chão, sem voo: testar Alt + direções, alcance ampliado e custo de stamina. Confirmar recusa em voo e espectador; voltar a sobrevivência para conferir paridade.
2. Criativo: segurar/soltar X, remapear e observar o escudo em primeira/terceira pessoa. Repetir com segundo cliente na mesma build.
3. Sobrevivência: testar perda de Postura e quebra, sem esperar que a invulnerabilidade criativa gere impactos. Conferir preenchimento, rachaduras, cores, separação das metades, nome e legibilidade em escalas de GUI diferentes.
4. Repetir esquiva contra parede/quina e borda, incluindo diagonal, para conferir o passo de 0,7 sem atravessar obstáculos ou prolongar queda.

Próxima frente mantida: fechar o contrato de Byakuya com a pesquisa do usuário antes do código de seu kit. Ishida foi incluído no planejamento, sem iniciar implementação ou presumir um repertório já aprovado.
