# Continuidade do projeto — leia primeiro

Atualizado em 15/10/2026. Resumo de passagem para nova conversa; consulte o código/Git antes de alterar. Este arquivo evita precisar importar o histórico completo do chat. Não substitui instruções atuais do usuário.

## 1. Repositório e regras mantidas pelo usuário

- Projeto local: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack`.
- GitHub atual: https://github.com/Costa-Novaes-Tech/Bleach-Modpack. Origin local ainda usa GabrielCNovaesDev/Bleach-Modpack.git, que redireciona; não foi alterado.
- Branch de trabalho: `Feature-Poderes-bankais`. Nunca comitar na main. Commits locais são autorizados após validação; isso não autoriza push, merge, exclusões ou outras publicações não solicitadas.
- A branch antiga `Feature-Bankais-Poderes` foi excluída pelo usuário por duplicação/atraso. Não recriar nem confundir os nomes.
- Documentação desta frente: modificar somente `Docs/poderes`. Documentos de outras pastas podem ser consultados, mas não editados sem nova orientação.
- Preservar texturas/modelos homologados, especialmente Asauchi. Aizen e Bankai original para ele estão adiados.
- Trabalhar em português; informar alterações, validação e limites. Não pedir novamente autorização para tarefas já solicitadas; não presumir que proposta futura equivale a pedido de implementação.
- Conferir status, branch e instruções locais; preservar alterações de outras pessoas. Não havia AGENTS.md localizado na revisão anterior; conferir novamente se o repositório mudou.

## 2. Estado entregue e homologado

Minecraft 1.20.1, Forge 47.4.10, Java 17, Gradle wrapper. Mod 0.2.0; protocolo de rede 2.11; schema persistente 8. Cliente e servidor devem usar a mesma build.

- Ryujin Jakka e Hyōrinmaru possuem kits próprios nas formas selada/Shikai/Bankai, já testados pelo usuário. Seus detalhes históricos estão nos relatórios de poderes; não reabrir balanceamento/visuais sem solicitação.
- Uma zanpakutō vinculada por vez. Pode trocar identidade, mantendo progresso separado. Asauchi é identidade própria, com Shikai/Bankai de bônus de dano e sem slots elementais; modelos preservados.
- Maestria por prática válida de forma/técnica, requisitos graduais e feedback no HUD. Modos administrativos gratuitos suspendem treino. Personagens legados preservam repertório conforme migração anterior ao schema 5.
- Combat State automático: dano elegível inicia/renova 200 ticks. HUD informa combate; morte/reconexão/dimensão limpam estado. Não é Combat Mode manual. Ainda não altera recuperação de reiatsu ou penaliza logout.
- Stamina máxima = 100 + 100 × R / (R + 20), com R sendo nível comprado de Resistência; R20 = 150, limite 200. Compra não recarrega saldo; redução limita saldo. Ainda existem sete atributos atuais, não a migração futura de cinco atributos.
- Corrida em movimento gasta 5/s; tentativa manual de golpe com zanpakutō contra alvo vivo gasta até 4. Recupera 20/s após um segundo sem esforço. Exaustão impede sprint até recuperar 10; ataque básico continua permitido a zero. Técnicas especiais não recebem custo de stamina, apenas suas regras de reiatsu.
- Stamina, pausa e exaustão persistem; reconexão não recarrega, respawn restaura máximo. Tooltip de Resistência na tela K informa stamina máxima em pt_br/en_us.
- Asauchi/Ryujin/Hyōrinmaru são indestrutíveis, inclusive itens antigos com Damage salvo; sem barra. Armas vanilla continuam danificáveis; NPC reparador não foi criado.
- Arma vinculada no inventário aparece embainhada na cintura; empunhada mantém bainha vazia. Só uma bainha, mesmo com duplicatas. Snapshot público informa apenas ausência/guardada/mão principal/secundária.
- Bainha final homologada: curva afunilada, paletas por identidade, esquerda anatômica para destros e espelhamento para canhotos. Escala 0,9775; rotação X -26 graus; Y ±25 graus. Não alterar esses valores sem necessidade aprovada.
- Saque/guarda cosmético nativo de oito ticks, terceira/primeira pessoa; seleção do slot é o gatilho. Não trava ataque. Ação dedicada de embainhar sem mudar slot ainda não existe.
- Esquiva direcional implementada no ciclo 37, **aguardando playtest**: Alt esquerdo remapeável + direção, sem direção recuo, diagonais normalizadas; custo 15 stamina, cinco ticks, 2,5 blocos nominais, intervalo de 12 ticks. Somente terrestre, sem invulnerabilidade. Não confundir com Shunpo. [Contrato, restrições e roteiro](37-esquiva-direcional-stamina-2026-10-15.md).
- Bloqueio/Postura/Guard Break implementados juntos no ciclo 39, **aguardando playtest**: segurar X remapeável, escudo acima do personagem com preenchimento/rachaduras, Postura 100, abertura de 20 ticks ao zerar. Guarda mitiga golpes físicos frontais, sem cobrar stamina. Recuperação, persistência e bloqueios de ações no [relatório 39](39-bloqueio-postura-guard-break-2026-10-15.md). Guard Break agora impede esquiva durante a abertura. F mantém troca vanilla entre mãos.

Aceite final de stamina/progressão e aparência da bainha recebido. Isso não comprova FPS, latência ou homologação com dois clientes reais.

## 3. Onde retomar sem ler todo o histórico

1. Este arquivo.
2. [Consolidação dos ciclos 30–35 e próximos passos](36-consolidacao-homologacao-proximos-passos-2026-10-13.md): contratos, arquivos e validações.
3. Apenas os documentos/trechos pertinentes à tarefa: [combate M06](../game-design/06-habilidades-e-combate.md), [atributos M05](../game-design/05-atributos-e-recursos.md) ou [contrato de novos poderes](contrato-criacao-novos-poderes.md).

Relatórios antigos preservam decisões superadas e avisos de teste pendente da época. O aceite posterior e o código atual devem ser considerados antes de repetir trabalho. Módulos futuros listados no mapa de design não estão necessariamente escritos/implementados. O checklist MVP de setembro não representa sozinho o estado de outubro.

## 4. Mapa técnico mínimo

| Frente | Arquivos em src/main/java/com/bleachmod |
|---|---|
| Recursos/progressão | common/StaminaPolicy, StaminaService, BasicAttackStamina, ProgressionService; common/data/ResourcesData, PlayerData, AttributeData, StatusData |
| Presença e rede | common/ZanpakutoCarry; common/data/CharacterData; common/network/SyncHelper; server/events/TickHandler, CapabilityEvents |
| Bainha/gesto | client/model/ZanpakutoSheathLayer, ZanpakutoMotion; common/visual/ZanpakutoDraw; client/events/ModEntityRenderers, ClientForgeEvents |
| Interface e itens | client/hud/ReiatsuHud; client/gui/StatusScreen; client/input/ModKeybinds; common/item/ZanpakutoItem; init/ModItems |
| Poderes | common/technique; common/visual; gametest |
| Guarda/Postura | common/GuardPolicy, GuardService; common/data/GuardData; common/network/c2s/GuardC2S; client/hud/GuardVisuals |

Traduções: src/main/resources/assets/bleachmod/lang/pt_br.json e en_us.json. Testes gerais: src/test/java/com/bleachmod/MvpRegressionTest.java.

## 5. Git e validação conhecida

Antes do ciclo 37, HEAD era `0b09578` (handoff), precedido por `5bc94be` (consolidação) e `349f016` (bainha final). O ciclo 37 acrescenta a esquiva; consultar `git log` para o commit atual. Branch pode receber integrações da equipe; conferir novamente.

Ciclo 33: 73 regressões e 63 GameTests obrigatórios aprovados. Ciclos 34/35: check/build e 73 regressões aprovados, sem repetir GameTests de lógica inalterada. Logs locais em build/cycle33-validation.log e build/cycle35-validation.log; podem não existir em outro checkout. JAR local: build/libs/bleachmod-0.2.0.jar, gerado e não equivalente a release publicada.

Ciclo 37: check/build e **76 regressões**, **67 GameTests obrigatórios** aprovados. Logs `build/cycle37-validation.log` e `build/cycle37-integration.log`. Não equivale a homologação de teclado, visual ou multiplayer real.

Ciclo 39: check/build e **80 regressões**, **71 GameTests obrigatórios** aprovados. Logs `build/cycle39-validation.log` e `build/cycle39-integration.log`. Escudo/guarda/quebra ainda precisam de playtest visual e de rede. Schema 8 persiste Postura/pausa/quebra sem restaurar tecla pressionada; legado inicia Postura cheia, demais progressos preservados.

PowerShell, na raiz, com JDK 17 instalado:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot'
.\gradlew.bat check build --offline --console=plain
# Para alterações de lógica que exigem integração:
.\gradlew.bat runGameTestServer --offline --console=plain
```

O caminho do JDK é desta máquina; conferir em outro ambiente. Offline depende do cache já preparado. Não iniciar duas execuções Gradle simultâneas. Documentação isolada requer revisão de links/diff, não nova build. Nunca apresentar teste automatizado como homologação visual.

## 6. Feature escolhida e próximos passos

O usuário escolheu implementar esquiva direcional universal com stamina. Após discutir dois toques em WASD, preferiu **Alt + direção**. Implementação do ciclo 37 concluída para playtest; não voltar a propor dois toques como controle vigente. Após testar esta feature, retomar a escolha da próxima implementação com o usuário.

Contrato e limitações efetivas da esquiva estão no [relatório 37](37-esquiva-direcional-stamina-2026-10-15.md); homologação continua pendente. Ordem vigente após nova decisão do usuário: **Bloqueio/Postura + Guard Break juntos → Byakuya → Urahara → Mentor → Proficiências → revisão final de Hyōrinmaru/Tōshirō**. **Gin foi retirado do planejamento.** Defesa implementada para playtest no [relatório 39](39-bloqueio-postura-guard-break-2026-10-15.md); usuário testará a build atual com esquiva e defesa. **Mentor e Proficiências permanecem adiados, aguardando confirmação com a equipe; não iniciar enquanto pendentes.** Byakuya é o próximo kit; Urahara terá pesquisa e contrato próprios. A futura revisão de "hyori" refere-se neste contexto ao kit existente de Hyōrinmaru/Tōshirō, depende da pesquisa do usuário e foi colocada ao final. [Roadmap revisado](38-roadmap-defesa-treinamento-gin-byakuya-2026-10-15.md).

Outras opções revisadas: mentor com cadeia curta de treinamento; Hohō/Shunpo; pequeno repertório de Kidō; proficiência de Zanjutsu/Hakuda/Hohō/Kidō; migração dos atributos com preservação de saves; novos encontros Hollow/boss; Soul Society/viagens; cooperação. História e regras de aquisição dependem da equipe. Antes de trabalhar em dimensões/mobs, conferir integrações de outras branches para não duplicar trabalho.

Pontos de design a preservar: atributos ≠ proficiência ≠ maestria; Postura é exclusiva de Guard Break e separada da stamina; sem nível global e sem skill tree como caminho obrigatório; poder principal continua vínculo único. Próximo contrato de kit: Byakuya, após pesquisa do usuário e homologação da defesa. Aizen permanece reservado e adiado.
