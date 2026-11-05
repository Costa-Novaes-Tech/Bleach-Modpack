# Campanha Shinigami — QA de catálogo, NPCs e progressão

Data: 29/10/2026, America/Sao_Paulo. Branch: `feat/jigui/quests`. HEAD de referência: `99064862874ec466b73a52b3dacc27882997fc02`. As alterações anteriores de HUD, navegação e retirada do treino da Rukia foram preservadas; nenhum commit/push foi feito.

## Escopo e resultado

Catálogo de 28 missões da sequência M13/10.32, Shinigami obrigatório por direção do usuário nesta conversa, contexto e dimensão do NPC, visibilidade pessoal por conclusão anterior, prioridade máxima de história e início explícito de encontro. O objetivo desta entrega é integrar as definições e o fluxo de oferta/progresso; não declarar a campanha inteira jogável com bosses e mapas ainda ausentes.

O catálogo usa dois IDs de saga novos, preservando as três antigas missões de demonstração e os dados dos jogadores. Rukia mantém sua quest de boss legada no NPC sem contexto do Overworld; ela não aparece nas Rukias narrativas. As quests de história futuras ficam ocultas. NPC sem contexto conserva ofertas de teste; ele não ganha por inferência a identidade de uma estrutura.

[Guia de implementação, catálogo e configuração](../../../planejamento/implementacao-campanha-shinigami-2026-10-29.md). [Roteiro de referência](../../../game-design/13-campanhas-e-sidequests.md#1032-sequência-consolidada-dos-dois-primeiros-arcos).

## Ambiente

- Windows, Minecraft Java 1.20.1, Forge 47.4.10, Java 17.0.20.1 Adoptium, Gradle Wrapper 8.8.
- GeckoLib e PlayerAnimator já declarados no projeto; perfil base com Better Combat, sem Photon.
- JDK existente em `C:/Users/jguic/.gradle/jdks/eclipse_adoptium-17-amd64-windows/jdk-17.0.20.1+1`.
- Gradle executado com `--offline`. O sandbox negou o lock do cache do wrapper; comandos posteriores usaram a autorização de execução com o cache já instalado. Nenhuma ferramenta ou versão foi instalada/atualizada.
- Cliente: 1280×720, idioma pt_br, GUI 2 e 3, mundo plano criativo, dificuldade Normal, seed 102926, spawn natural e ciclo diurno desativados apenas no mundo de QA.
- O bootstrap só roda fora de produção, com `-PstoryQuestQa=true`, na pasta `run/story-quest-qa`, e sempre cria um mundo novo `Story-Quest-QA-*`.

## Verificações

Linha de base antes das novas alterações: `check --offline --console=plain`, exit 0, 154 cenários de regressão. Uma compilação intermediária já com a feature também passou, ainda com os 154 cenários antigos.

| Execução | Resultado observado |
|---|---|
| `check build runGameTestServer --offline --console=plain` | Exit 0, 160 regressões e 228 GameTests obrigatórios; `integration.log`, 3m45s |
| `check build runClient runGameTestServer -PstoryQuestQa=true --offline --console=plain` | Exit 0, 160 regressões, 228 GameTests, nove capturas reais; `client-integration.log`, 5m20s |
| Reexecução após identidade da tentativa | Exit 0, 160 regressões, 228 GameTests e nove capturas; `final-validation.log`, 4m59s |
| Reexecução após isolamento das ofertas legadas | Exit 0, 160 regressões, 228 GameTests e nove capturas; `release-validation.log`, 4m54s |

Os testes novos verificam:

- Comparação da ordem das 28 definições com a tabela real de M13/10.32, quantidade por arco, ausência de recompensas não especificadas e de repetibilidade.
- Desbloqueio pessoal sequencial, outro jogador sem avanço, transição de arco pelo marco final e exigência racial também em `FAILED`.
- JSON de ida/volta, rejeição de requisito racial opcional, contexto e dimensão, UUID persistente da tentativa e do destino.
- Prioridade acima de uma saga de demonstração fixada manualmente.
- Aceite por NPC errado/raça errada recusado; oferta legada não vaza para contexto narrativo.
- Boss não registrado permanece pendente, sem spawn substituto, vitória ou liberação da próxima missão.
- Abertura real com um único Hollow, cliques repetidos sem duplicação, alvo de outro proprietário ou de outra tentativa sem crédito, morte válida concluindo a missão e liberando a seguinte.

Os GameTests usam FakePlayer e mundo descartável. A validação visual usa o cliente Forge real. A vitória da abertura no cenário visual é acionada por dano de teste no alvo real, não por uma luta manual completa.

## Evidências visuais

As PNGs são capturas diretas do framebuffer do Minecraft por `Screenshot.grab`, não mockups. Os ticks e a escala estão em `frames.jsonl`. O cenário terminou automaticamente e registrou `Story quest QA complete` no log próprio do cliente.

Origem final: `run/story-quest-qa/evidence/Story-Quest-QA-1793327494499/`. As execuções anteriores continuam preservadas em suas pastas originais; a cópia em `screenshots/` corresponde à versão final, já com o isolamento do boss legado. Os nove arquivos foram inspecionados visualmente. Não houve falha de build, regressão ou GameTest nestas quatro execuções de integração. Os arquivos rotativos `logs/debug-*.log.gz` alterados pelo executor foram restaurados aos bytes da linha de base após os testes; os logs da feature permanecem nesta pasta.

| Captura | Observação |
|---|---|
| `01-rukia-opening-offer.png` | Oferta da primeira história na Rukia do contexto inicial; futuras ocultas |
| `02-story-priority-hud.png` | História acima de saga antiga fixada, alvo da abertura no mundo, bússola e distância para a Rukia correta |
| `03-next-mission-unlocked.png` | Depois da vitória, outra Rukia oferece o caso de Orihime; pendência do boss legível |
| `04-pending-boss-hud.png` | História aceita e rastreada, com aviso de conteúdo pendente e distância |
| `05-pending-boss-details.png` | Missão aceita, botão de início desabilitado enquanto o boss não existe |
| `06-other-race-no-story.png` | Alteração da raça apenas no fixture oculta a história naquela conversa |
| `07-wrong-context-no-story.png` | Rukia do cemitério não oferece a missão da residência de Orihime |
| `08-compact-hud.png` | GUI 3, título e navegação cabem sem encobrir a hotbar |
| `09-new-npc-placeholder.png` | Kon com Steve provisório, sem textura ausente; HUD oculto no fixture |

Avaliação subjetiva de legibilidade: 4/5. Em GUI 3, o card compacto prioriza título e navegação e omite a linha de objetivo/pendência; detalhes continuam na conversa/diário. O chat de feedback pode se sobrepor ao texto de status inferior, comportamento existente fora do card. A conversa de NPC conserva o layout atual, sem uma revisão artística nesta tarefa. Nenhum FPS/frametime foi medido.

## Limites e pendências explícitas

- Os confrontos Hollow comuns e as interações de contato/resgate possuem execução. Os pontos reais de mapa/contexto ainda precisam ser posicionados; mapas `.mca`, saves pessoais e geração de estruturas não foram alterados.
- IDs `bleachmod:story_*` reservam as representações dos bosses futuros. Não existe boss de substituição nem conclusão por ausência do inimigo. Integrar o modelo também exige implementar seu ator/perfil de combate correspondente.
- Perseguição/duelo de Ururu/Urahara, expedição/retorno, esfera/lançamento de Kūkaku, galerias de Hanatarō e circuito de Yoruichi estão cadastrados como objetivos tipados e bloqueados. Seus percursos/atores/validadores ainda não foram implementados. Não foram tratados como conversa ou simples espera; o checkpoint de Ururu é parte dessa integração futura, não uma funcionalidade testada nesta entrega.
- A interação de prisão/resgate na dimensão Soul Society não foi percorrida em mapa narrativo completo. Os testes visuais cobrem o fluxo inicial de Karakura.
- Três Hollows na crise Quincy e a distância de início de 48 blocos são defaults provisórios, documentados para edição/calibração, sem alegação canônica.
- Não houve sessão com dois clientes reais, servidor dedicado convencional com jogadores, perfil Photon ou medição de desempenho. O GameTestServer é uma validação de servidor real sem clientes e não substitui esses cenários.
- Party/crédito cooperativo, roubo de último golpe e catch-up continuam sem um contrato completo de participação. A regra atual exige o proprietário matar o alvo da sua tentativa; os testes não demonstram cooperação multiplayer.

## Reprodução

```powershell
$env:JAVA_HOME='C:/Users/jguic/.gradle/jdks/eclipse_adoptium-17-amd64-windows/jdk-17.0.20.1+1'
.\gradlew.bat check build runGameTestServer --offline --console=plain
.\gradlew.bat runClient -PstoryQuestQa=true --offline --console=plain
```

O cliente cria novo save na sua pasta própria e encerra após a sequência. Não reutilizar um save de jogador como fixture. Para testar em mapa autorado, configurar o NPC com `QuestContext` ou o comando de operador documentado no guia; não escolher coordenadas arbitrárias dentro de uma construção.
