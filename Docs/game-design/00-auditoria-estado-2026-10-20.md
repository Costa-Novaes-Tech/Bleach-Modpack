# Auditoria do estado do projeto — 20/10/2026

## Escopo

Atualização do checkout para o repositório indicado pelo usuário e comparação dos documentos `Docs/game-design/` com o código e os relatórios de entrega presentes em `main`. A presença de código não é tratada como teste nem aceite de experiência.

**Complemento documental de 20/10/2026:** mapa, cabeçalhos e handoffs foram alinhados ao estado já registrado nos módulos; a skill `bleach-game-design` e o [handoff vigente](00-handoff-atual.md) foram preparados. As correções abaixo não representam nova implementação de gameplay.

Não foram executados build, GameTests ou testes em jogo nesta auditoria. As contagens de validação abaixo são as registradas nos relatórios do ciclo 71.

## Ambiente local atualizado

| Item | Estado |
|---|---|
| Remoto `origin` | `https://github.com/Costa-Novaes-Tech/Bleach-Modpack.git` |
| Branch e commit | `main`, `8ace6520e67d27441974b9ad92b443ddb33f9882` — merge do PR #20, 20/10/2026 |
| Sincronização | `main` e `origin/main` iguais após fast-forward de 100 commits |
| Escala da atualização | 6.557 arquivos alterados no intervalo; inclui mapas da História, assets e documentação de QA, então a contagem não representa apenas código-fonte |
| Alterações locais preservadas | `.DS_Store` continua modificado; este relatório é um arquivo local novo |
| Java ativo nesta sessão | Temurin 21.0.11 |
| Java disponível para o projeto | Temurin 17.0.20.1 e Zulu 17.0.20 instalados; o manual do projeto exige Java 17 |
| Validação nesta auditoria | Nenhuma execução nova. O Gradle e o cliente não foram iniciados |

## Estado do game design nos documentos

| Documento | Estado declarado | Situação atual |
|---|---|---|
| `00-mapa-modular.md` | 01–03 decididos; 04 especificado; 05 especificado conceitualmente; 06 em especificação | Corrigido na preparação documental de 20/10, seguindo os estados dos módulos |
| `01-visao-e-pilares.md` | `DECIDIDO` | Visão e decisões D01–D06 continuam sendo os princípios do produto |
| `02-trilhas-de-progressao.md` | Decidido no nível arquitetural | PRG-01–PRG-08 fechados; reputação, posição, vínculo e conhecimento ainda dependem de sistemas posteriores |
| `03-storyline-macro.md` | Decidido no nível macro | STR-01–STR-07 fechados; não especifica as campanhas jogáveis completas |
| `04-racas-e-origens.md` | Conclusão declara `ESPECIFICADO` | Quatro identidades raciais e decisões RAC registradas; criação/continuidade e conteúdo concreto continuam em outros módulos |
| `05-atributos-e-recursos.md` | `ESPECIFICADO CONCEITUALMENTE` | Define cinco atributos fundamentais e recursos; calibração numérica e adaptação do modelo legado ainda não foram fechadas |
| `06-habilidades-e-combate.md` | `EM ESPECIFICAÇÃO` | Base universal descrita; M06-07 Shinigami é concluído estruturalmente no fim do arquivo; próximo bloco é M06-08 Hollow/Arrancar |

Ainda faltam documentos 07–16 do mapa: transformações, NPCs/mentores/facções, mobs/encontros, dimensões, itens/economia, quests, campanhas, interface, multiplayer e integração/roadmap. A descrição atual de M08 também não cobre toda a personalização de personagem que já entrou no código.

## O que já chegou ao código

| Área | Implementado em `main` | Limite ou pendência de design |
|---|---|---|
| Personagem e aparência | Editor de aparência, catálogo de skins, roster ampliado de NPCs e sincronização de aparência | A criação continua aceitando somente Shinigami; a identidade visual não implementa origens raciais |
| Conjuntos canônicos | Asauchi, Ryūjin Jakka, Hyōrinmaru, Senbonzakura, Benihime e Nozarashi/Berserk Mode; progresso por identidade, formas, técnicas e mastery | É um recorte de conteúdo Shinigami; as árvores e poderes Hollow/Arrancar, Quincy e Fullbringer não estão implementados |
| Progressão e recursos | Reiatsu, stamina, pontos de treino, sete categorias de atributos, mastery de técnicas/formas e persistência/migração | M05 descreve cinco atributos fundamentais. O modelo de sete categorias permanece; parte do contrato conceitual de Reiryoku/Reishi e a progressão social/narrativa não está completa |
| Combate | Better Combat para armas Bleach selecionadas, ataque básico com custo de stamina, esquiva direcional, guarda, postura e Guard Break | Não representa por si só o sistema completo de disciplinas, repertórios e balanceamento racial previsto em M06 |
| Técnicas e apresentação | Slots server-side, passivas, liberações, gestos/cinemáticas, ataques elementais e kits dos cinco conjuntos acima; serviços Ryūjin, Hyōrinmaru, Senbonzakura, Benihime e Nozarashi | Técnicas específicas já existem, mas a aquisição narrativa, o catálogo completo e os contratos raciais continuam incompletos |
| Mundos e viagem | Preset History Mode, dimensões Soul Society e Hueco Mundo declaradas por esse preset, mapas versionados e itens/serviço de viagem | Não presumir essas dimensões em mundos comuns. Isso não conclui campanhas, encontros ou regras territoriais de STR/M10 |
| Quests e NPCs | Framework data-driven, saga Soul Society provisória, objetivos/recompensas, sidequests de teste e NPCs interativos | Conteúdo padrão ainda é pequeno e usa objetivos/mobs de protótipo; mentorias, facções e campanhas de todas as raças faltam |
| Inimigos e cenas | Hollow e boss, Ice Dragon, execução Higashi e laboratório de cenas/cinematics | Não há catálogo completo de tiers, famílias e encontros; a aceitação visual de cenas tem gates próprios |
| Interface e QA | HUD e telas por layouts JSON, editor de aparência, ferramentas de bancada, regressões e GameTests | Há validação automatizada registrada, mas isso não substitui observação visual e teste multiplayer real |

## Avanço mais recente e qualidade da evidência

O merge da `main` inclui dimensões/History Mode, integração Nozarashi, personalização de aparência, melhorias de HUD e os avanços dos kits. Os documentos dos ciclos 63–71 registram uma progressão grande de implementação e validação.

No ciclo 71, o relatório registra `check`, `build`, `runGameTestServer` e preparação dos pacotes concluídos: **127 regressões e 151 GameTests aprovados** no perfil base e no perfil Photon, em execuções sequenciais. Esse resultado pertence à execução registrada no relatório; não foi repetido nesta auditoria.

O ciclo 71 também arquiva capturas reais do cliente e evidências da entrada Benihime e da execução Higashi. O relatório esclarece que os vídeos são montagens silenciosas de quadros, não gravações contínuas com áudio. A homologação artística final, a escuta/aprovação do áudio, cenários adicionais e o teste com dois clientes reais continuam pendentes. Portanto, automação aprovada não equivale a multiplayer ou aceite visual completo.

## Divergências identificadas e tratamento

1. **Mapa de módulos — corrigido:** estados alinhados a M04 especificado, M05 especificado conceitualmente e M06 em especificação, com M06-07 aprovado estruturalmente. O cabeçalho de M04 também foi alinhado à sua conclusão.
2. **Handoffs — corrigido:** os dois handoffs antigos foram marcados como históricos; `00-handoff-atual.md` registra M06-08 como continuidade. `Docs/poderes/00-CONTINUIDADE-CODEX.md` continua útil para os ciclos técnicos, embora suas regras históricas de branch/local precisem de confirmação neste checkout.
3. **Cabeçalho de M06 — corrigido:** agora reconhece M06-07 aprovado estruturalmente e identifica os blocos restantes. Nenhuma decisão racial nova foi acrescentada por essa correção.
4. **README e checklist MVP:** mantêm vários estados de setembro. Marcam dimensões e técnicas como futuras/ausentes embora já exista implementação; ainda são referência útil para pendências específicas de homologação, não um retrato completo da `main` de outubro.
5. **Design versus implementação:** M05 já decide conceitualmente cinco atributos e o código mantém sete categorias numéricas; a migração e conciliação com os contratos implementados precisam ser especificadas. Isso não reabre automaticamente a decisão conceitual. Várias funcionalidades foram construídas por ciclos de poderes sem atualizar os estados formais dos módulos 05–16.
6. **Revisões entre módulos — sinalizadas:** M05 registra revisões formais de M02, incluindo o papel dos Spiritual Points e o crescimento integrado dos atributos. M06-07 supera skill tree e rank adicional de disciplina em favor de repertório e Proficiência direta. M02 recebeu aviso para leitura dessas revisões; seu registro histórico foi preservado. A [base de conhecimento](00-base-conhecimento-minecraft-bleach.md) e a skill preparada orientam essa precedência.

## Próximos passos recomendados

1. Usar o mapa/handoff corrigidos e a skill `bleach-game-design` para abrir M06-08, lendo os contratos e realizando a pesquisa canônica específica antes de fechar novas regras.
2. Especificar/documentar a transição para os cinco atributos decididos em M05 e a separação das disciplinas, preservando saves e conciliando os contratos implementados. Se surgir motivo concreto para revisar a decisão, apresentar esse conflito à equipe antes de aprovar outro modelo.
3. Continuar M06-08, depois M06-09/10/11; criar os módulos 07–16 conforme dependências, começando por transformações, NPCs/mentores, mobs e integração mundo/quests.
4. Mapear cada decisão aprovada para IMPLEMENTADO, TESTADO e ACEITO separadamente, usando os relatórios de ciclo como evidência e registrando explicitamente o que ainda aguarda playtest.
5. Para nova build, selecionar Temurin 17 no `JAVA_HOME`; esta auditoria não verificou build no checkout atualizado.

## Fontes conferidas

- Game design: `Docs/game-design/00-mapa-modular.md` e documentos 01–06.
- Continuidade e entregas: `Docs/poderes/00-CONTINUIDADE-CODEX.md`, relatórios 63–71 e `Docs/poderes/qa/benihime/cycle71/`.
- Código: `common/data`, `common/technique`, `common/visual`, `common/compat`, `server/world`, telas de aparência/HUD e pacotes de GameTest em `src/main/java/com/bleachmod/`.
- Documentação geral: `Docs/README.md`, `Docs/planejamento/` e `history-maps/README.md`.
