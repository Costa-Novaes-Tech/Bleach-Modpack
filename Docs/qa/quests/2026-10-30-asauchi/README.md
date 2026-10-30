# Asauchi no início da missão de Rukia

Data: 30/10/2026. Branch: `feat/jigui/quests`, base `74a4bb37`.

Pedido: selecionar Shinigami sem receber espada; receber Asauchi ao iniciar "Um Hollow na área", com o Hollow Boss.

## Mudanças

- Retirada da entrega de item de `ConfirmCharacterC2S`.
- Entrega no servidor após início válido de `MAIN-SUB-01`, com verificação de espada existente e fallback de drop para inventário cheio.
- Marca `starterAsauchiGranted` persistida no progresso e preservada ao falhar/reiniciar. Não é recompensa de conclusão.
- Catálogo novo da abertura aponta para `bleachmod:hollow_boss`. Saves com JSONs existentes não são sobrescritos nem têm objetivos/progresso migrados.
- Textos PT/EN informam a entrega no início. Fixture visual finaliza a entrada do boss somente para aplicar o dano de teste, sem remover a animação de produção.

## Validação

Baseline anterior desta conversa: 160 regressões e build aprovados. Após a alteração: `check build runGameTestServer --offline -PstoryQuestQa=true --console=plain`, exit 0, 161 regressões e 229 GameTests obrigatórios aprovados. Mundo de servidor isolado em `run-gametest/story-quest-start`. Java 17.0.20.1, Minecraft 1.20.1, Forge 47.4.10.

Cobertura nova: não receber espada ao inicializar personagem; início válido entrega uma; clique repetido não duplica; alvo real é Hollow Boss e sua morte libera a próxima missão; início inválido não entrega; remover a espada e reiniciar após falha não permite obter outra; marca persiste em NBT e reinício de tentativa. O caminho de confirmação foi inspecionado para verificar a remoção da entrega; o teste de inicialização usa FakePlayer, não o pacote de criação enviado por cliente.

O cache Gradle exigiu execução autorizada fora do sandbox. Uma compilação inicial falhou por uma chamada de API no teste, corrigida antes da execução final. Nenhuma dependência foi instalada.

Cliente Forge real: `runClient --offline -PstoryQuestQa=true --console=plain`, exit 0, `Story quest QA complete`, nove capturas reais em `run/story-quest-qa/evidence/Story-Quest-QA-1793364692536/`. O fixture confirmou Asauchi única, ator Hollow Boss e avanço após dano de teste. A captura `02-story-priority-hud.png` foi inspecionada: espada em primeira pessoa, Rukia e efeito de entrada do boss visíveis; o HUD está oculto pela cinematic nesse marco. Demais imagens foram geradas, sem inspeção visual individual nesta rodada. Não houve sessão com dois clientes nem medição de desempenho. Não há aceite visual de combate manual.

Logs: `run/story-asauchi-validation.log` e `run/story-asauchi-client.log`.
