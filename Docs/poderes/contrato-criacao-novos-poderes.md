> **Estado vigente — ciclo 32, 13/10/2026:** ciclo 31 e estado de combate homologados pelo usuário. Golpes básicos de zanpakutō gastam até 4 stamina por tentativa; técnicas preservam custos de reiatsu. As três espadas são indestrutíveis por decisão do usuário. [Contrato, arquivos, lógica e testes](32-ataques-basicos-zanpakutos-indestrutiveis-2026-10-13.md). Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 31, 13/10/2026:** stamina de corrida com pausa, recuperação, exaustão e HUD. Valores provisórios; ciclo 30 segue em teste visual. [Contrato, arquivos, lógica e roteiro](31-stamina-corrida-2026-10-13.md). Schema 6; protocolo 2.8. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 30, 12/10/2026:** primeira fatia de Combat State automático, indicador no HUD e limpeza transitória, sem mudar regeneração/stamina/atributos. [Lógica, arquivos, timeout provisório e testes](30-estado-combate-2026-10-12.md). Protocolo 2.7; schema 5 preservado. Combat Mode/hotbar seguem futuros; registros anteriores abaixo são históricos.

# Contrato operacional — criação e homologação de novos poderes

Versão inicial consolidada em 12/10/2026, ciclo 29. Complementa o [contrato de identidade, vínculo e maestria](16-contrato-identidade-vinculo-maestria-zanpakuto.md), os kits homologados e as definições compartilhadas. Não aprova automaticamente um novo personagem ou conjunto.

## Identidade e escopo

Uma zanpakuto é escolha de identidade única ativa, passível de troca. Progresso por identidade/forma/técnica permanece separado e persistido; trocar arma ou vínculo não concede progresso de outra identidade. A habilidade exige raça/grupo/formas compatíveis, forma desbloqueada e equipamento correspondente. Asauchi conserva o modelo/textura atual e somente bônus de dano nas liberações; não preencher slots elementais para ela.

Antes de implementar outro conjunto, aprovar inspiração, função de combate, formas disponíveis e tabela dos quatro slots. Aquisição narrativa, provas e história dependem da equipe. Aizen permanece futuro; nenhuma Bankai inventada está aprovada neste contrato.

## Definição única

Para cada combinação identidade/forma/slot, registrar em TechniqueDefinitions:

| Campo | Unidade/regra |
| --- | --- |
| identity | ID persistente conhecido, vinculado a equipamento próprio |
| form | sealed, shikai ou bankai conforme suporte explícito |
| slot | 1 a 4; tecla vem do controle configurado |
| id | nome técnico estável da habilidade; não chave de uma entidade temporária |
| nameKey | tradução em pt_br/en_us |
| mastery | requisito de acesso naquela forma, hoje 0/20/40/60 |
| cost | reiatsu na ativação, finito e não negativo |
| cooldownTicks | ticks de servidor, não segundos; 20 ticks por segundo nominal |
| drainPerTick | consumo contínuo, separado da cobrança inicial |

Não duplicar esses valores no HUD e executor. Se um futuro kit precisar de regras diferentes de slots, estender a política comum e seus testes explicitamente. Dano, área, duração e estados específicos continuam nos executores/serviços de geometria; este catálogo ainda não padroniza todos os parâmetros de combate. Não tratar zero custo inicial como ausência de consumo contínuo.

## Mapa de implementação

1. Identidade: CharacterData e migração/compatibilidade de saves; equipamento em ModItems e recursos locais; suporte de formas. Nunca reutilizar o ID de outro kit para contornar vínculo.
2. Metadados: estender validação de identidades e entradas de TechniqueDefinitions; conferir unicidade e cobertura das formas suportadas. Adicionar traduções do nome. Consultar find/require sem fallback para outro personagem.
3. Executor: registrar roteamento explícito em TechniqueService; autorização compartilhada antes de custo/cooldown/efeitos. Consultar a definição da identidade/forma/slot e usar setters/consumeReiatsu existentes para preservar flags administrativas. Manter orçamento/checagens de invocação e rollback quando criação falhar; não cobrar um lançamento rejeitado.
4. Combate: validar alvos por TechniqueTargets, dimensão, distância e linha de visão conforme contrato da técnica. Dano/controle que treinam usam MasteryPracticeService com slot correto; efeitos passivos não recebem crédito por atalho.
5. Visuais: usar VisualFrame/EffectLibrary ou perfil específico homologado. Compartilhar um orçamento entre camadas por atualização; declarar audiência, origem, frequência e expiração. Não carregar chunks para partículas nem criar frames extras para contornar limite. Texturas vanilla são referenciadas; assets externos exigem avaliação de licença e adequação antes da integração.
6. Ciclo de vida: cancelamento por forma/identidade/equipamento conforme efeito, morte, logout, dimensão, unload e parada do servidor. Descartar invocações antigas ao substituir e restaurar alterações temporárias. Clima/terreno persistidos precisam de propriedade, expiração, coexistência de jogadores e recuperação após reinício.
7. HUD: nome, requisito e custo/cooldown vêm das definições; atalhos são remapeáveis. Diferenciar domínio da forma e técnica, descobertas e compra do vínculo; compatibilidade de saves não pode ser apresentada como reset de desbloqueios.
8. Testes e docs: regressões puras em src/test/java, integração em gametest, relatório em Docs/poderes com decisões, arquivos, lógica, valores, limites e roteiro. Documentos de outras pastas não são alterados por esse fluxo.

## Maestria e aquisição

Ganhos atuais: 0,5 por crédito válido para forma, um para técnica; limites de repetição e janela de intenção de MasteryPracticeService. Nenhum ganho ocioso nem em modo de custos/cooldowns gratuitos. Descoberta prática Shikai ocorre em 20 da selada; Bankai em 25 da Shikai após descoberta da Shikai. Compra e cadeia de transformação permanecem; não conceder habilidade por mensagem do cliente. Maestria individual não concede novos bônus de dano/custo implicitamente.

Personagens legados podem preservar repertório descoberto. Para testar bloqueios iniciais, usar personagem novo; reduzir maestria por comando não apaga descobertas. Qualquer mudança de schema/protocolo exige migração explícita e testes de save/sincronização.

## Critérios de aceite

- Todas as definições possuem nome traduzido, custo/cooldown e requisito válidos; não há combinações duplicadas ou fallback entre kits.
- Lançamentos normais, falhas e modo administrativo correspondem aos metadados; habilidade bloqueada não debita nem inicia efeito.
- Identidade/forma/progresso são isolados e persistidos; alvos aliados/inválidos não recebem efeitos indevidos ou crédito.
- Cancelamento, substituição de invocações, clima e terreno não deixam estado órfão; multiplayer e dimensões são verificados conforme alcance do efeito.
- check/build e GameTests pertinentes passam; homologação visual/layout/FPS é registrada separadamente pelo teste em cliente.
- Relatório descreve riscos/limites reais e comportamento final, sem afirmar testes não realizados.

Commits validados são feitos na Feature-Poderes-bankais, nunca na main. Merge permanece com o usuário. Uma futura branch diferente deve ser autorizada explicitamente; esse contrato não altera a autorização atual.

## Estado atual e próximos trabalhos

Ryujin e Hyōrinmaru usam o catálogo do ciclo 29. Os testes de ambos são a referência inicial para outro kit. Antes de retomar outra habilidade/Bankai, discutir uma feature independente que beneficie o projeto; não implementar a proposta sem definição do próximo escopo. Narrativa e Aizen não são dependências para fechar esta consolidação.
