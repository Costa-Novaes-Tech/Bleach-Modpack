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
2. Crie uma linha de base (`baseline`) com resultados separados por etapa. Use os comandos Gradle existentes na raiz, por exemplo `./gradlew.bat check --console=plain` e `./gradlew.bat runGameTestServer --console=plain` no PowerShell Windows, somente após inspecionar o wrapper e confirmar que o diretório de execução/servidor de teste é descartável. Não confunda `test` com `regressionTest`.
3. Corrija somente problemas pertinentes à feature e regressões introduzidas; não mascare falhas preexistentes, não altere snapshots de referência para forçar sucesso, não desative testes. Reexecute os casos afetados.
4. **Execute `runClient` em sessão gráfica Windows autorizada** e abra um mundo de testes separado. Em modo headless/remoto sem tela, informe a limitação; não chame isso de homologação in-game.
5. Faça primeiro uma prova vertical mínima: **cortina sobe fechada → mão encontra borda → dedos agarram/abrem → primeiro avanço corporal → pose final**. Só depois refine rosto, corpo inteiro, cabelos, vestido, áudio, câmera e VFX.
6. Cada conclusão visual exige captura real do cliente Minecraft, idealmente vídeo para movimento + screenshots de marcos. Capture logs e metadados. Se o agente não consegue dirigir a GUI, abra/preconfigure o cenário e forneça instruções concretas para o usuário registrar a captura, sem simular aprovação.
7. Confirme compatibilidade e desempenho em servidor dedicado e observador; use perfil base sem Photon como referência e teste Photon/LDLib **separadamente**.

## Seleção e instalação de ferramentas (há autorização para preparar o ambiente)
- Comece com os recursos já instalados. Para qualquer download: preferir site oficial ou release oficial, confirmar origem e arquitetura Windows, versão, licença, tamanho, hash/assinatura quando disponível e comando; evitar executáveis de espelhos e scripts remotos opacos.
- Pode instalar **somente dependências gratuitas claramente necessárias para esta feature**, na menor quantidade possível e com escopo de usuário/projeto: Blockbench (editor/modelagem), plugin GeckoLib **pelo gerenciador oficial do Blockbench**, ferramentas FFmpeg/ffprobe e, se justificado, capturador de vídeo (OBS Studio ou recurso nativo). Avalie se é necessário baixar cada uma.
- Java 17 e Gradle já foram detectados; **não os atualize cegamente**. GeckoLib e PlayerAnimator já estão na configuração do mod: não instalar outra versão sem demonstrar compatibilidade. O JDK de teste deve corresponder ao alvo Java 17.
- Blender, Audacity, Playwright, ImageMagick, JUnit e plugins de edição só entram se uma necessidade específica persistir após testar o fluxo básico; Playwright **não** testa o cliente Minecraft Java, apenas uma prévia web opcional. Não instalar todos por padrão.
- Antes de mudanças de sistema, de PATH global, instalação com administrador, extensões não verificadas ou ferramentas pagas/serviços externos, apresente motivo e solicite autorização específica. Não transmitir vídeo, código ou áudio a provedores cloud sem consentimento explícito. Não usar APIs pagas por padrão.
- Cache e downloads Gradle inevitáveis em tarefas aprovadas devem ser identificados nos relatórios. Se uma instalação falhar, registre erro e siga pelos recursos locais quando viável.
- Consulte `references/ferramentas-e-instalacao.md` para plano e fontes oficiais.

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

## Delegação de trabalho
A **conversa principal do Codex** coordena implementação e pode delegar QA ao subagente de projeto `.codex/agents/bleach_visual_qa.toml` (`bleach_visual_qa`), se suportado pela versão local. O subagente herda permissões da conversa e **não deve editar os mesmos arquivos simultaneamente**. A ficha `references/perfil-agente.md` descreve o contrato operacional. Um `AGENTS.md` existente nunca deve ser sobrescrito automaticamente. A ativação explícita da skill é `$bleach-mod-dev`.
