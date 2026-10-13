> **Estado vigente — ciclo 33, 13/10/2026:** stamina máxima por Resistência com retornos decrescentes e sem recarga por compra; bainha da arma vinculada e saque/guarda cosméticos nativos. [Fórmula, arquivos, sincronização, limites e roteiro](33-progressao-stamina-bainhas-saque-2026-10-13.md). Schema 7; protocolo 2.9. Visual e balanceamento aguardam playtest; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 32, 13/10/2026:** ciclo 31 e estado de combate homologados pelo usuário. Golpes básicos de zanpakutō gastam até 4 stamina por tentativa; técnicas preservam custos de reiatsu. As três espadas são indestrutíveis por decisão do usuário. [Contrato, arquivos, lógica e testes](32-ataques-basicos-zanpakutos-indestrutiveis-2026-10-13.md). Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 31, 13/10/2026:** stamina de corrida com pausa, recuperação, exaustão e HUD. Valores provisórios; ciclo 30 segue em teste visual. [Contrato, arquivos, lógica e roteiro](31-stamina-corrida-2026-10-13.md). Schema 6; protocolo 2.8. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 30, 12/10/2026:** primeira fatia de Combat State automático, indicador no HUD e limpeza transitória, sem mudar regeneração/stamina/atributos. [Lógica, arquivos, timeout provisório e testes](30-estado-combate-2026-10-12.md). Protocolo 2.7; schema 5 preservado. Combat Mode/hotbar seguem futuros; registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 29, 12/10/2026:** ciclos anteriores homologados pelo usuário. Definições comuns dos 24 slots/formas de Ryujin e Hyōrinmaru consultadas por execução, acesso e HUD; valores preservados. [Arquivos, lógica e validação](29-definicoes-compartilhadas-2026-10-12.md) · [Contrato para novos poderes](contrato-criacao-novos-poderes.md). Registros anteriores abaixo são históricos; nova feature será discutida antes de outro kit.

> **Estado vigente — ciclo 28, 11/10/2026:** feedback de maestria sem arredondar limiares, avisos de novas liberações pelo treino, distinção de descoberta/compra/disponibilidade e indicação de treino suspenso em modo teste. [Arquivos, lógica e roteiros de visuais/maestria](28-feedback-maestria-roteiro-2026-10-11.md). Ganhos e requisitos preservados; ciclos abaixo são históricos.

> **Estado vigente — ciclo 27, 11/10/2026:** cortes, barreira, Zona Glacial, impacto de controle e trilha do dragão da Hyōrinmaru usam o emissor comum, mantendo aparência e combate. [Arquivos, lógica, valores e testes](27-migracao-slots-gelo-2026-10-11.md). Clima e terreno conservam sua lógica; registros abaixo são históricos.

> **Estado vigente — ciclo 26, 11/10/2026:** aura e liberação da Hyōrinmaru integradas ao emissor comum com audiência próxima e orçamentos explícitos, preservando a aparência aprovada. Bancada continua privada. [Arquivos, lógica, limites e roteiro](26-integracao-gelo-emissor-comum-2026-10-11.md). Os ciclos anteriores abaixo são históricos; Aizen e narrativa permanecem futuros.

> **Estado vigente — ciclo 25, 11/10/2026:** biblioteca comum com paletas fire/ice/reiatsu e formas aura/cut/beam/release; camadas compartilham orçamento. Bancada anterior preservada; nenhuma mudança nos kits homologados. [API, arquivos, limites e comparação](25-biblioteca-paletas-camadas-2026-10-11.md). Escolha visual e integração em produção são próximos gates; Aizen e narrativa continuam futuros.

> **Estado vigente — ciclo 24, 11/10/2026:** Asauchi/maestria do ciclo 23 homologadas pelo usuário. Catálogo vanilla e bancada administrativa de seis presets, três intensidades e duração limitada, sem alterações nos kits ou terreno. [Recursos, arquivos, lógica, limites e roteiro](24-catalogo-vanilla-bancada-visual-2026-10-11.md). Aizen e aquisição narrativa continuam futuros.

> **Estado vigente — ciclo 23, 07/10/2026:** ciclo 22 homologado pelo usuário. Asauchi independente com selada/Shikai/Bankai, mantendo todas as texturas/modelos e apenas bônus de dano nas liberações; treino válido por forma/técnica e desbloqueios graduais dos kits. Personagens antigos preservam repertório. Aizen permanece futuro. [Contrato operacional, lógica, arquivos, migração e roteiro de testes](23-asauchi-maestria-desbloqueios-2026-10-07.md). As seções abaixo registram os ciclos anteriores; propostas de história continuam abertas.

> **Estado vigente — ciclo 22, 07/10/2026:** restauração do clima agora sincroniza explicitamente os clientes e `/weather clear` corrige chuva visual presa; Ice Dragon Bankai com modelo maior e alcance 24. [Causa, lógica, arquivos, testes e roteiro final](22-clima-sincronizado-dragon-bankai-2026-10-07.md). Demais pontos homologados pelo usuário; encerramento definitivo aguarda este teste visual. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 21, 07/10/2026:** tornado centrado e móvel com miolo livre de três/raio externo nove; Zona Glacial Shikai dez/Bankai trinta com neve temporária no interior; clima com restauração e prioridade para comando manual; passivas de fogo Ryujin e resistência Hyōrinmaru. [Passo a passo, lógica, arquivos, valores e testes](21-correcao-tornado-zona-clima-passivas-2026-10-07.md). Os ciclos abaixo são históricos; esta revisão substitui o deslocamento frontal do tornado do ciclo 20.

> **Estado vigente — ciclo 20, 06/10/2026:** Ryujin com onda mais densa e tornado três blocos à frente; Hyōrinmaru com quatro slots selados, armadura temporária, hipotermia, criatura de gelo, asas/cauda Bankai, tempestade com restauração e HUD de habilidades. [Lógica, arquivos, valores e homologação](20-refinamento-gelo-clima-hud-2026-10-06.md). A decisão deste ciclo substitui a antiga selada restrita a dois cortes físicos. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 19, 06/10/2026:** identidade e maestria separadas, protótipo Hyōrinmaru em quatro slots, N da Ryujin com onda de quatro blocos na selada/Shikai/Bankai, aura Bankai de fumaça e retirada do X. [Implementação, arquivos, números, migração e homologação](19-hyorinmaru-identidade-onda-fogo-2026-10-06.md). Os estados e valores anteriores abaixo são históricos; história e balanceamento final continuam em desenvolvimento.

# Auditoria de segurança do Iniciar-Teste

Data: 04/10/2026. Branch: `Feature-Poderes-bankais`. Base: `987dc96`.
O usuário homologou a Ryujin e encerrou a feature de poderes. Este ciclo revisa
exclusivamente o inicializador e seus testes; não altera gameplay, assets ou saves.

## Resultado e escopo

Revisados `Iniciar-Teste.bat` e `tools/iniciar-teste.ps1`. A limpeza continua restrita
a `build`, `.test-launcher-cache`, `run/logs` e `run/crash-reports` do próprio projeto.
Mundos, opções, configurações, screenshots, fontes, Git e downloads compartilhados
não entram nessa lista. Nenhum teste excluiu ou alterou os arquivos de controle externos.

O primeiro teste da versão anterior recusou um junction interno e preservou o arquivo
externo. A revisão encontrou melhorias necessárias na validação e no tratamento de
falhas, e um problema reproduzido com nomes de pastas contendo colchetes.

## Correções, lógica e arquivos

1. **Simulação consistente:** anteriormente `DryRun` retornava antes da inspeção de
   links internos. Agora usa `Assert-TestOutput`, a mesma validação da exclusão real,
   e só imprime os caminhos depois de verificar todos. Não cria lock, roda wrapper
   ou altera arquivos.
2. **Inspeção sem seguir links:** substituída a enumeração recursiva por uma pilha
   de diretórios. Cada filho é inspecionado antes de entrar nele. Qualquer reparse
   point é recusado; isso inclui junctions, ciclos e links quebrados detectáveis
   pelo provedor. `Get-TestPathItem` usa caminho literal e tolera apenas inexistência;
   erros de acesso continuam interrompendo a operação.
3. **Validação antes de apagar:** todos os destinos são verificados antes da primeira
   exclusão, e novamente imediatamente antes de cada remoção. Um arquivo comum
   com nome `build` é recusado, em vez de ser tratado como diretório gerado.
   Validação que falha antes da limpeza não remove o cache em `finally`.
4. **Lock protegido:** `.test-launcher.lock` deve ser um arquivo comum, sem links.
   A abertura exclusiva impede outro inicializador de limpar uma build em uso.
   Links ou diretórios com esse nome são recusados antes de abrir o handle.
5. **Cache bloqueado não significa sucesso:** falha na limpeza final agora define
   código de saída 1 e informa que o cache não foi removido. O BAT conserva esse
   código. Uma execução posterior limpa os resíduos quando o bloqueio termina.
6. **Caminhos literais:** `Push-Location -LiteralPath` corrige o erro reproduzido com
   colchetes. Espaços, `&` e `!` também integram as fixtures. O BAT desativa expansão
   atrasada para preservar `!` ao expandir o caminho da própria pasta.

Somente os dois arquivos do inicializador mudaram em produção. Criado
`tools/test-iniciar-teste.ps1` para repetir a verificação automaticamente.

## Testes isolados e reproduzíveis

Comando:

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File tools/test-iniciar-teste.ps1 -ReportPath build/launcher-security-report.json
```

Resultado final: **18 cenários aprovados, zero falhas, todas as fixtures temporárias removidas**.

Executado com Windows PowerShell 5.1.26100.9444. As fixtures usam uma pasta exclusiva em TEMP,
com nomes contendo caracteres especiais. O wrapper simulado registra chamadas e
cria arquivos de teste; **não compila o mod, baixa dependências ou abre Minecraft**.
O caso de concorrência mantém um inicializador real executando esse wrapper simulado.
Um helper temporário mantém um arquivo aberto para simular falha de limpeza final.
Todos os helpers terminam, e a rotina final remove as fixtures sem seguir seus links.

Cobertura:

- DryRun sem mutação; entrada BAT sem entrada interativa; BuildOnly/Offline.
- Limpeza restrita, remoção de cache antigo, arquivos protegidos byte a byte.
- Ordem build → cliente, duas execuções sequenciais e cache removido nas duas.
- Falha de build impede cliente; falha de cliente retorna erro e remove cache.
- Junctions no build, cache e `run`, link interno, ciclo, link quebrado e lock.
- Preflight inválido não limpa parcialmente o build antes de recusar o link.
- Arquivo comum no lugar de build; projeto incompleto sem wrapper.
- Duas execuções simultâneas: segunda recusada, build ativa preservada.
- Arquivo de saída aberto/bloqueado: interrupção sem chamar wrapper.
- Cache bloqueado ao terminar: erro visível; próxima execução recupera a limpeza.

O relatório JSON gerado em `build` registra o resultado de cada cenário. Essa pasta
é um artefato de verificação, ignorado no Git e substituído no próximo teste/build.
As verificações do mod homologadas no ciclo anterior permanecem: 51 regressões e
18 GameTests. Não foram repetidas porque este ciclo modifica somente scripts/documentos.

## Resíduos esperados e limites

- **Saída normal:** cache de projeto temporário removido e lock liberado. Permanece
  um único arquivo `.test-launcher.lock` de zero bytes, para coordenar execuções.
  Não é processo, não mantém memória e não acumula dados.
- **Artefatos úteis:** build/JAR/relatórios e logs da sessão permanecem para uso e
  diagnóstico, e são limpos antes da próxima execução. Saves/configurações são dados
  do jogador e permanecem deliberadamente.
- **Downloads compartilhados:** Gradle/Forge/JDK podem ocupar disco e são reutilizados.
  O inicializador não apaga caches globais nem tenta limpar outras aplicações.
- **Encerramento forçado/quebra de energia:** `finally` não pode ser garantido quando
  o processo é morto. Cache temporário pode permanecer até a próxima execução;
  o teste de cache antigo/recuperação valida a limpeza posterior. Não afirmamos
  que o disco ficará sem qualquer artefato imediatamente após um encerramento forçado.
- **Processos externos:** lock coordena este inicializador, não builds da IDE ou um
  Minecraft iniciado manualmente. Feche esses processos antes de iniciar o teste.
  Arquivos bloqueados causam erro; não são encerrados à força pelo script.
- **Políticas do Windows:** `ExecutionPolicy Bypass` vale apenas para o processo
  iniciado. Não altera política persistente, Registro, serviços, inicialização do
  Windows ou instalação de Java. Não exige execução como administrador.
- **Modelo de segurança:** valida acidentes e configurações locais comuns; não é
  sandbox para um repositório malicioso. O Gradle executa o código do projeto.
  Também não oferece isolamento contra outro processo malicioso que troque diretórios
  entre a validação e a exclusão. Nenhum cenário desse tipo foi certificado.

Conclusão operacional: as proteções testadas preservam os dados de controle e tornam
falhas de limpeza visíveis. A feature Ryujin permanece homologada e encerrada.
