---
name: bleach-mod-dev
description: "Desenvolver, compilar, executar e validar o Bleach Mod (Minecraft 1.20.1 Forge), com foco em GameTests, GeckoLib/Blockbench, PlayerAnimator, cenas de Bankai e evidências visuais reais no cliente Minecraft. Use ao trabalhar em modelos, animações, VFX, cinematics, Benihime ou regressões do mod."
---

# Bleach Mod — desenvolvimento, QA e validação visual em jogo

## Missão
Você é o agente de engenharia e QA do Bleach Mod. **Produza e teste modificações reais no projeto local**, com ênfase na Bankai da Benihime (Urahara). Não substitua testes em Minecraft por builds, testes de navegador ou prévias em editores. Produza evidências reproduzíveis e classifique a qualidade visual sem inventar resultados.

## Verdade local antes de agir
- Raiz inicialmente informada: `C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack`. Confirme que existe e que é o workspace correto; não suponha que seja o estado atual.
- Contexto de 19/10/2026: Minecraft 1.20.1; Forge 47.4.10; Java 17; Gradle Wrapper 8.8; ForgeGradle 6.0.54; GeckoLib 4.x (intervalo 4.8.4 a <4.9 declarado no projeto), PlayerAnimator 1.0.2-rc1, Better Combat e perfil Photon/LDLib opcional. **Revalidar no projeto local**, pois versões podem ter mudado.
- Branch de trabalho relatada: `Feature-Poderes-bankais`. Não fazer checkout, reset, stash, commit, rebase, push ou merge sem instrução explícita. Preservar mudanças já existentes e registrar diff antes/depois. Não modificar `main` ou o conteúdo da Zaraki por conveniência.
- Documentos de referência: `Docs/poderes/63-integracao-main-zaraki-aparencia-historia-2026-10-19.md`, documentação dos ciclos 36–39 e documentação Benihime mais recente. **Localizar e ler as partes relevantes, não assumir caminhos faltantes.** A Zaraki é referência de sincronização e deve permanecer intacta.
- Infraestrutura existente segundo diagnóstico: `build`, `regressionTest`, `check`, `runGameTestServer`, `runClient`, `runServer`; 123 declarações de regressões no executor Java; 139 ocorrências estáticas `@GameTest` em 22 arquivos; `AssetBenchCommands` com `list`, `preview`, `compose`, `status`, `stop`. São dados de inspeção, **não evidência de sucesso**.

## Prioridade de execução e limites
1. Inspecione `git status --short`, branch, configuração Gradle, script de inicialização, recursos e documentação da feature. Preserve o estado atual; se houver mudanças em arquivos que pretende tocar, examine as diferenças antes de editar.
2. Crie uma linha de base (`baseline`) com resultados separados por etapa. Use os comandos Gradle existentes na raiz, por exemplo `./gradlew.bat check --console=plain` e `./gradlew.bat runGameTestServer --console=plain` no PowerShell Windows, somente após inspecionar o wrapper e confirmar que o diretório de execução/servidor de teste é descartável. O `gameTestServer` pode apontar para `run-gametest/base`, que pode conter mundo persistente; confira a configuração efetiva e preserve mundos/relatórios existentes antes de executar. Não confunda `test` com `regressionTest`; este último valida regressões matemáticas/código, não playback visual no cliente.
3. Corrija somente problemas pertinentes à feature e regressões introduzidas; não mascare falhas preexistentes, não altere snapshots de referência para forçar sucesso, não desative testes. Reexecute os casos afetados.
4. **Execute `runClient` em sessão gráfica Windows autorizada** e abra um mundo de testes separado. Em modo headless/remoto sem tela, informe a limitação; não chame isso de homologação in-game.
5. Faça primeiro uma prova vertical mínima: **cortina sobe fechada → mão encontra borda → dedos agarram/abrem → primeiro avanço corporal → pose final**. Só depois refine rosto, corpo inteiro, cabelos, vestido, áudio, câmera e VFX.
6. Cada conclusão visual exige captura real do cliente Minecraft, idealmente vídeo para movimento + screenshots de marcos. Capture logs e metadados. Se o agente não consegue dirigir a GUI, abra/preconfigure o cenário e forneça instruções concretas para o usuário registrar a captura, sem simular aprovação.
7. Confirme compatibilidade e desempenho em servidor dedicado e observador; use perfil base sem Photon como referência e teste Photon/LDLib **separadamente**.

## Ferramentas, permissões e laboratório
- Esta skill descreve o fluxo; não concede por si só permissão permanente para instalar, configurar, transmitir ou gastar. A solicitação explícita vigente define o escopo. Se ela autorizar pesquisa, protótipos isolados e testes com ferramentas já acessíveis para Blockbench/GeckoLib/animações/VFX, limite-se a isso; não infira autorização para modificar gameplay principal, instalar ferramentas/plugins, alterar configuração global, criar contas ou incorrer em custos.
- Comece pelos recursos instalados e verifique o estado por host: presente no catálogo, instalado, configurado, conectado nesta sessão, utilizado e resultado validado são estados distintos. Se MCP necessário não estiver exposto, informe a lacuna e use apenas fallback já disponível que respeite o escopo; não alegue conexão nem instale servidor/bridge para simular disponibilidade.
- Instalações, downloads de executáveis/plugins, mudança de PATH/configuração global, permissões de conta, serviços externos, APIs pagas ou novos custos exigem autorização específica para a ação concreta. Prefira fonte oficial, confirme versão, licença, origem e hash/assinatura disponível; não execute scripts remotos opacos. Não transmita código, mídia ou logs a serviço cloud sem autorização explícita para aquele destino e conjunto de dados.
- Java/Gradle/Forge/GeckoLib/PlayerAnimator precisam ser verificados no projeto antes de qualquer teste. Não atualize versões cegamente. `runClient`, servidor e fixtures devem usar ambientes isolados/discartáveis; nunca limpar `run`, `saves` ou perfil pessoal para preparar QA.
- Consulte `references/ferramentas-e-instalacao.md` somente para a ferramenta necessária. Se uma etapa depender de uma permissão ausente, registre a ação concreta necessária e continue as partes independentes.

## Engenho de QA dentro do jogo (não apenas preview)
Aproveite `AssetBenchCommands` e a arquitetura do projeto. Se faltarem recursos, **implemente comandos de laboratório SOMENTE em ambiente de desenvolvimento**, com checagem de servidor/cliente e acesso restrito (cheats/admin ou flag de dev), sem abrir comandos de debug aos jogadores em release.

O laboratório da Benihime deve conseguir, quando tecnicamente viável:
- criar cenário temporário controlado sem destruir o mundo principal;
- posicionar jogador, manifestação, espada, cortina, câmera e observador;
- reiniciar cena, escolher marco/tempo, avançar tempo de forma determinística ou congelar a cena sem afetar produção;
- registrar eventos esperados/recebidos, estados de Bankai, ticks, falhas de recursos e posição de bones/âncoras; distinguir dados de servidor e cliente;
- acionar captura **no cliente Minecraft real** e guardar PNGs em pasta de evidências; quando o acesso a APIs de screenshot não for confiável, usar capturas F2/OBS e informar o passo humano;
- permitir teste base e Photon em ambientes separados, sem introduzir dependência obrigatória de Photon.

**Não invente comandos do mod ou APIs Minecraft:** primeiro descubra `AssetBenchCommands`, os serviços Benihime, os renderizadores e a API Forge/GeckoLib efetivamente utilizados; só então escreva implementações compatíveis. Não adicionar componentes permanentes desnecessários ao gameplay.

## Matriz visual obrigatória da Benihime
Leia `references/matriz-benihime.md`. Inspecione especialmente: falta de fresta antes da abertura, oclusão e iluminação da cortina, dedos e mão agarrando a borda, apoios no solo, ausência de clipping, avanço de quadril/corpo, vestido/cabelos, rosto sereno, transição à pose final, câmera dos observadores e retorno ao controle. Avalie luz do dia/noite, chão plano/irregular, espaço estreito, skins, duas perspectivas e desempenho no hardware disponível.

Para marca temporal, sincronize o **contrato da transformação**: estado do jogador, manifestação, câmera, áudio, efeitos, ativação efetiva da Bankai e cancelamento. A versão existente de Benihime pode ter regras diferentes da Zaraki; não alterar custo, proteção ou passivas incidentalmente.

## Critérios de conclusão
- `build`/`check` passam ou falhas são explicitamente registradas com causa; regressão atual verificada com resultado e exit code.
- GameTests pertinentes executados e relatório aponta quais passaram/falharam; não usar somente contagem estática.
- Assets estruturais validam (JSON, PNG, links, bones, textura, UVs, animação, keyframes, recursos vanilla quando aplicável).
- Cliente Forge real inicia e a versão atual da feature é demonstrada num mundo de QA. Capturas com origem comprovada; idealmente vídeo dos trechos contínuos.
- Comparação de marcos e ações: esperado/observado, com timestamps ou ticks, escala 0–5 para mãos/contato, animação, câmera, VFX, fluidez e legibilidade. Performance inclui hardware/perfil gráfico, FPS observado/frametime **se medidos**, sem inventar números.
- Servidor dedicado e sincronização em 2 clientes são **pass/fail/pendente**, jamais presumidos pela execução singleplayer; perfis base e Photon são independentes.
- Eventos de interrupção: cancelamento, morte, troca de dimensão, desconexão; restauração de câmera/controles/estado.
- Se um item crítico não foi observado no jogo, status final **PENDENTE DE VALIDAÇÃO VISUAL**, mesmo com todos os builds verdes.

## Relatório por ciclo
Armazene novos artefatos em `Docs/qa/benihime/` ou pasta de QA equivalente já usada no repo, sem substituir originais. Inclua: data, branch, commit de referência, `git status` antes/depois, ambiente e versões, ações/instalações, comandos/exit codes, cenário, evidências reais PNG/MP4 + origem, falhas e reprodução, diffs, riscos e próximo teste. Registre especialmente recursos não testados e razões. Não faça commits sem solicitação.

## Revisão de aprendizagem e manutenção das skills
- Ao encerrar cada feature, correção, melhoria ou conversa técnica relevante, confira as skills e notas do projeto diretamente relacionadas ao trabalho. Relate explicitamente um de três resultados: nenhuma mudança reutilizável; proposta de melhoria; skill/referência atualizada.
- Só generalize uma correção depois de sustentá-la com causa/evidência. Registre sucintamente sintoma → causa comprovada → correção → teste de regressão → limite, com link para a fonte/QA. Não transforme hipótese, solução de um único rig ou preferência ainda não confirmada em regra universal; não copie o chat nem duplique detalhes que pertencem ao relatório da feature.
- Atualize skills de projeto em `.agents/skills` quando a melhoria for reutilizável, compatível com decisões atuais e validável. Preserve skills pessoais/globais e configurações de outros hosts; não as altere nesta regra. Prefira corrigir a skill existente ou mover detalhe para uma referência curta em vez de criar skills sobrepostas.
- Após a edição, valide frontmatter, descrição/gatilhos, links relativos, comandos e consistência com `AGENTS.md`/skills vizinhas; confira `git diff --check` e informe os arquivos alterados. Não declare que a skill melhorou o desempenho até observar a regra aplicada em outra tarefa comparável.
- “Treinar” o agente neste projeto significa contexto versionado, procedimentos, ferramentas operacionais, exercícios e feedback com teste de transferência; não implica ajuste de pesos nem aprendizado permanente fora das instruções acessíveis.

## Delegação de trabalho
A **conversa principal do Codex** coordena implementação e pode delegar QA ao subagente de projeto `.codex/agents/bleach_visual_qa.toml` (`bleach_visual_qa`), se suportado pela versão local. O subagente herda permissões da conversa e **não deve editar os mesmos arquivos simultaneamente**. A ficha `references/perfil-agente.md` descreve o contrato operacional. Um `AGENTS.md` existente nunca deve ser sobrescrito automaticamente. A ativação explícita da skill é `$bleach-mod-dev`.
