# Benihime — substituição do Gigai no kit selado

27/10/2026. Estado: direção APROVADA pelo usuário em27/10, sem código ou nova animação nesta rodada. Decisão existente: usuário rejeitou a boneca deixada para trás e pediu outra habilidade. Usuário concordou com Hadō91 como substituto; calibração inicial autorizada para protótipo.

## Estado conferido no código

Slots selados em TechniqueDefinitions e BenihimeTechniqueService: H Saque da Bengala (cane_draw); N Jūgeki Byakurai (byakurai); B Rikujōkōrō — Seis Hastes (six_rods); C Gigai Portátil (gigai). O C atual recua por pathEnd de6blocos e deployGigai cria um decoy temporário. Definição atual: custo22, recarga240ticks. Esses números são estado anterior, não balanceamento aprovado para o substituto.

## Substituta aprovada: Hadō91 — Senju Kōten Taihō

Dar ao selado um finalizador ofensivo com preparação clara: múltiplos pontos/lanças luminosas surgem ao redor do conjurador, convergem para um local selecionado e explodem numa área concentrada. Nome e uso por Urahara possuem referência secundária de cânone; quantidades, tempos, área e interação com o kit são adaptações a fechar.

Papel: explosão ofensiva com compromisso/preparo, em vez do recurso evasivo rejeitado. H mantém ação próxima, N é disparo preciso, B contém por tempo limitado e C aproveita a abertura para convergência. B→C é combinação possível, não execução garantida em PvP. A nova C é destaque do selado, sem superar automaticamente a suprema da Bankai nem mudar seu dano aprovado.

Sequência proposta: assumir postura estável com bengala/lâmina baixa; mão de conjuração se posiciona; pontos de Kidō aparecem em arco lateral/alto; lanças convergem com pequenos intervalos; clarão concentrado, poeira/luz e recuperação. Cor proposta: rosa-pálido/branco com bordas discretas, distinguindo Kidō selado dos fios e cortes da Shikai. Normalmente sem câmera forçada. Quantidade de6–8pontos é sugestão visual, não quantidade fixa de hits aprovados.

Selecionar ponto por mira com parede/solo/alvo válidos; não exigir sempre um inimigo selecionado para mostrar o ataque. Proposta: destino fixado no lançamento, com telegraph e possibilidade de escapar. Usar validação autoritativa e teto de dano por alvo; seis efeitos não significam seis explosões integrais acumuladas. Preservar construções na versão inicial; explosão visual não implica TNT ou destruição de blocos.

Não manifestar a boneca da Bankai na forma selada. A identidade é domínio de Kidō e a bengala. A nova ação ocupa só o C selado; combos básicos aprovados, Shikai, Bankai, áudio e transformação permanecem sob seus contratos atuais.

## Alternativa histórica — não escolhida

Contrapulso de Kidō: interceptação curta de ameaça frontal seguida de contra-ataque. Seria adaptação original identificada, não técnica canônica inventada. Cobre sobrevivência melhor, mas corre risco de repetir o escudo da Shikai; por isso Hadō91 é a recomendação principal.

## Migração e integração necessárias

CharacterData usa IDs de técnica nas chaves de progresso Benihime. Trocar gigai por outro ID sem migrar perde descoberta/Mastery do C. Preservar desbloqueio, progresso e demais slots com alias/migração explícitos, cobrindo save antigo e novo. Limpar somente decoys temporários pertencentes ao serviço/jogador; não remover armor stands/mobs do mundo em massa.

A IA deve possuir BenihimeTechniqueService/Policy, seus efeitos/animação específicos e textos com prefixo Benihime. Alterações em TechniqueDefinitions/CharacterData/migrações serão coordenadas com Codex, pois a frente Quincy também depende de dados/entrada gerais. Não reformatar arquivos compartilhados inteiros.

## Fases para o futuro prompt

0. Conferir a ficha V0 autorizada, contrato de interrupção e integração. Inventariar IDs e saves afetados; não repetir a escolha do Hadō91 já aprovada.
1. Nova pose de conjuração SEM partículas: mão/arma solidárias, braço livre coerente e alternativa para offhand ocupado. Medir no renderer do modelo Benihime; não copiar offsets Ryūjin.
2. Uma lança de Kidō com origem real na pose e impacto, para provar lançamento/colisão.
3. Convergência completa e impacto concentrado; manter ritmo/legibilidade/performance.
4. Integrar C, migrar progresso e retirar decoy/recuo associados a esse slot.
5. Check/regressões/GameTests pertinentes, cliente real selado, primeira/terceira pessoa, destro/canhoto, parede, alvo móvel, grupo, sobrevivente, troca de forma e reconexão. Regressão curta preserva Shikai/Bankai e melee aprovados.

Ferramentas: PlayerAnimator/Better Combat existentes nas responsabilidades adequadas, renderer/VFX nativo e Blockbench se um projétil exigir modelo próprio. Photon apenas opcional. Sem nova biblioteca necessária por antecipação. Branch proposta codex/benihime-selada-kido, do mesmo commit de poderes usado por Uryū; checkout separado existente, sem trocar branch/pasta agora.

## Fases de implementação e status

- [x] **Fase 0 — Inventário, Contratos e Parâmetros V0**: ID `senju_koten_taiho`, custo 30 PE, recarga 280 ticks (14s), 24 ticks (1.2s) de conjuração, 16 HP base antes de defesa, raio 4 blocos, até 8 alvos.
- [x] **Fase 1 — Coreografia e Poses**: Base firme, bengala baixa, mão de conjuração posicionada, torso acompanhando e recuperação a neutro (peso 0) ao final de 24 ticks (`BenihimeChoreography`, `SignatureChoreography`).
- [x] **Fase 2 — Geometria e VFX das Lanças**: 6 lanças em arco convergentes com nós de luz, trajetória interpolada e clarão/onda de choque no impacto, sem destruição de blocos ou sobreposição opaca de tela (`BenihimeGeometry`, `BenihimeFrameS2C`).
- [x] **Fase 3 — Serviço de Combate e Lógica de Seleção**: Mira direta em alvo elegível ou no solo/bloco até 24 blocos, sem travessia de paredes (LOS via `line`), resolução única de dano na convergência (sem multiplicação por lança), exclusão de aliados via `TechniqueTargets.allowed` e remoção do recoil/decoy do C antigo (`BenihimeTechniqueService`).
- [x] **Fase 4 — Migração de Progresso e Saves**: Revisão 5 em `CharacterData`, migrando `sealed|gigai` e `sealed|4` para `sealed|senju_koten_taiho` de forma idempotente, conservando descoberta e Mastery.
- [x] **Fase 5 — Verificação e Provas Reais**:
  - Testes unitários de regressão (`./gradlew.bat test`): 100% aprovados, cobrindo definição, migração, coreografia e geometria finita.
  - Servidor GameTest real do Minecraft Forge (`./gradlew.bat runGameTestServer`): 243/243 testes aprovados, incluindo `sealedHado91PreparesAndConvergesOnTargetWithoutBlockDestruction`.
  - Execução de Client QA no Forge (`Invoke-Design.ps1 -Mode ClientQa`): Concluído com código de saída 0.

## Referências

- Código conferido: TechniqueDefinitions, BenihimeTechniqueService, CharacterData e pt_br.json.
- Contrato118 e estado-combate-senbon-higashi: espaços global/local e validação de espada real. A calibração é específica do modelo; repetir medição para Benihime.
- Referência secundária do nome/uso: https://bleach.fandom.com/wiki/Senju_K%C5%8Dten_Taih%C5%8D e https://bleach.fandom.com/wiki/Kisuke_Urahara, resultados indexados consultados27/10. Não foi lido capítulo integral. Mangá/cena e arte devem ser conferidos antes de aprovar reprodução fiel de pose e sequência; nossa implementação proposta ainda é adaptação.

## Ficha V0 — Hadō91 para teste

Substituir somente C selado. Alcance do ponto de impacto24blocos; raio4blocos; preparação1,2s;6lanças visuais convergentes;16HP de dano total máximo por alvo por ativação, antes da defesa/escala existente; teto8alvos; custo30pontos do recurso espiritual atual; recarga14s. Esses são valores de protótipo, não balanceamento final.

Uma única resolução autoritativa do dano na convergência;6lanças não causam6vezes16HP. Alvo/local válido e linha de visão antes da cobrança. Fixar destino no lançamento, travel visual curto sincronizado ao impacto; paredes impedem atingir através delas. Sem destruição de blocos nesta versão. Preservar regra comum de interrupção e recuperação do controlador; nenhuma câmera cinematográfica nova.

Migração gigai→senju_koten_taiho conserva descoberta/Mastery do slot, sem alterar os outros slots/identidades. Preservar poder e apresentação da suprema Bankai;16HP não é instrução para ultrapassá-la por escala multiplicada. Custos/cooldown do Gigai citados acima são só histórico.
