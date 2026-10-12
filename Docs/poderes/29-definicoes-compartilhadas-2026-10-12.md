# Ciclo 29 — Definições comuns e contrato para novos poderes

Data: 12/10/2026. Branch: Feature-Poderes-bankais. Usuário homologou visuais, persistência e maestria dos ciclos anteriores; autorizou os três passos de consolidação.

## Entrega

Ryujin e Hyōrinmaru possuem catálogo imutável de 24 definições: duas identidades, três formas e quatro slots. Executor, acesso por maestria e HUD consultam os mesmos metadados. Asauchi permanece identidade independente com liberações que alteram apenas dano, sem slots elementais. Não foi criada nova habilidade, Bankai ou missão.

O contrato para desenvolver os próximos kits está em [contrato de criação de novos poderes](contrato-criacao-novos-poderes.md). A próxima feature será discutida após este fechamento; Aizen e narrativa permanecem futuros.

## Lógica e arquivos, passo a passo

1. `src/main/java/com/bleachmod/common/technique/TechniqueDefinitions.java`: registro imutável por identidade/forma/slot. Cada Definition contém ID, chave traduzível do nome, requisito de maestria, custo de ativação, cooldown em ticks e consumo contínuo por tick. Construção rejeita dados inválidos/duplicados; consultas desconhecidas não devolvem outro kit. As formas disponíveis de cada técnica são representadas pelas entradas correspondentes.
2. `TechniqueProgression.java`: disponibilidade exige uma definição real e consulta seu requisito; descobertas legadas continuam válidas. API anterior de requisito por slot delega à regra comum 0/20/40/60. Equipamento, identidade e liberação seguem as verificações existentes.
3. `TechniqueService.java`: lançamentos Ryujin consultam custo/cooldown da forma ativa; Ignição consulta consumo contínuo depois de verificar que está ativa e com equipamento válido. Constantes públicas anteriores passam a ser aliases do catálogo, preservando consumidores existentes. Dano, geometria, duração e efeitos ficam nos executores homologados.
4. `RyujinTechniqueService.java`: Muralha/Tornado e Morcegos consultam a definição do slot/forma. Mantém verificações de efeito já ativo, sucesso da invocação e limpeza antes de cobrança. Aliases de valores públicos continuam disponíveis.
5. `HyorinmaruTechniqueService.java`: remove arrays independentes de custos/cooldowns; cada slot consulta a definição correspondente. Mantém validação, alvo, controle, neve, clima e criatura.
6. `src/main/java/com/bleachmod/client/hud/ZanpakutoHud.java`: nomes e requisitos vêm do catálogo. Acrescenta linha de custo/cooldown base ou consumo por segundo para Ignição; painel cresce para acomodá-la. Controles continuam remapeáveis; modo gratuito mantém aviso de treino suspenso. Valores mostrados são os programados, não um contador de cooldown restante.
7. Traduções em `src/main/resources/assets/bleachmod/lang/pt_br.json` e `en_us.json`: custos/cooldown e consumo contínuo. Nenhum modelo ou textura alterado.
8. `src/test/java/com/bleachmod/MvpRegressionTest.java`: cobertura do catálogo completo, valores anteriores, unicidade, consultas desconhecidas e ausência de slots Asauchi.
9. `src/main/java/com/bleachmod/gametest/TechniqueDefinitionGameTests.java`: executa as 24 combinações com identidades/equipamento e formas preparados; compara débito/cooldown real com definição, verifica consumo da Ignição e repete com custos/cooldowns gratuitos. Cancela efeitos após cada caso. Suíte anterior conserva verificações de bloqueios, troca de identidade, aliados, persistência e encerramento.

Não há alteração de protocolo ou schema de personagem. A definição é código comum compartilhado, não novo datapack nem packet do cliente. Nenhum cache de capacidade é introduzido. Objetos de mundo/efeitos e assets não participam da inicialização estática do catálogo.

## Valores preservados

| Kit e forma | Custos H/N/B/C | Cooldowns H/N/B/C em segundos |
| --- | --- | --- |
| Ryujin selada/Shikai | 0/15/40/25 | 0/4/20/10 |
| Ryujin Bankai | 20/25/40/45 | 5/4/15/60 |
| Hyōrinmaru selada | 8/8/12/18 | 5/8/12/15 |
| Hyōrinmaru Shikai | 15/20/25/30 | 5/8/12/15 |
| Hyōrinmaru Bankai | 20/25/30/35 | 5/8/12/15 |

Ignição selada/Shikai custa zero para alternar e drena 0,05 reiatsu por tick (um por segundo a 20 TPS) enquanto ativa. Não recebeu cooldown novo. Os demais custos são por lançamento. Tabela descreve H/N/B/C padrão; HUD usa as teclas configuradas.

## Homologação complementar

1. Compare as quatro linhas de custo/cooldown no HUD em cada forma de ambos os kits. Ignição deve mostrar consumo contínuo, sem cobrança inicial inventada.
2. Com regras normais e reiatsu suficiente, lance cada slot e compare débito e cooldown; mantenha o roteiro dos ciclos 27/28 para visuais/maestria. Em slot bloqueado, tentativa não deve consumir recursos nem iniciar cooldown.
3. Ative flags administrativas gratuitas e confira que os executores continuam respeitando-as. HUD informa valores base e o aviso de modo teste; não afirma treinamento nesse modo.
4. Troque identidade/equipamento e confirme que não acessa o kit anterior. Asauchi não mostra slots. Reconfirme encerramento de campos, invocações e clima conforme os gatilhos anteriores.
5. Compare escalas/resoluções e remapeamento de teclas para detectar cortes ou sobreposição no HUD ampliado. Testes automatizados não substituem homologação de layout/FPS.

## Validação

check, build e runGameTestServer offline aprovados: 67 regressões e 55 GameTests obrigatórios. O novo GameTest executa as 24 definições com regras normais e modo gratuito. Log local: build/cycle29-validation.log. git diff --check aprovado. Modelos/texturas e documentos externos a Docs/poderes intactos; JAR atualizado em build/libs/bleachmod-0.2.0.jar. Homologação de layout do HUD ampliado permanece um teste complementar em cliente, sem afirmar FPS ou avaliação visual automatizada.
