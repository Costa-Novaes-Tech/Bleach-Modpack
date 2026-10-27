# 143 — Prompt Antigravity Higashi: transição anterior à câmera e alvo único

Copie para a conversa Higashi. Trabalhe em `codex/ryujin-bankai-sol-selamento`, checkout `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime`.

## Limites e estado inicial

NÃO fazer merge, rebase, push ou alterar main. Trabalhar no checkout e branch existentes. Não usar reset --hard, clean, restore de arquivos alheios ou aplicar patch cegamente. Você não está sozinho: preserve o trabalho do Codex e da outra conversa.

Há alterações locais de QA deixadas pelo Codex no seu checkout. Elas são INTENCIONAIS. Leia git diff antes de editar; não trate working tree sujo como lixo. Os patches de referência ficam em C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/Docs/poderes/qa/cycle141/. Não aplicar esses patches outra vez se já estiverem presentes. Não comitar logs, mundos, caches ou alterações de terceiros. Commits focados das suas correções podem ser entregues para revisão conforme autorização vigente; integração continua proibida.

Leia também, na raiz principal Bleach-Modpack, Docs/poderes/141-provas-isoladas-reais-senbon-higashi-2026-10-27.md e 137-storyboards-direcao-katanas-senbon-higashi-2026-10-27.md. Alguns documentos/evidências estão locais e não commitados nessa raiz: não assumir que git pull os trará à sua branch, nem fazer merge só para obtê-los. Ler pelos caminhos absolutos locais.

Use skills e instruções do projeto. Documentação/evidências novas em Docs/poderes. Não instalar bibliotecas, serviços ou ferramentas novas para substituir diagnóstico. Cliente Forge real é a prova gráfica. check/build não executa automaticamente runGameTestServer; test sozinho não substitui regressionTest.

## Preservar o avanço observado

Provas141, base8f4115b: Husk e Hollow alado iniciaram10/20HP (50%), ambos morreram pelo dano NORMAL; não são casos≤7%. Nos quadros observados durante a câmera, ficou uma vítima; dispatcher registrou bloqueio do original e a varianteFLYING foi preservada. Não descartar essas mudanças nem reiniciar o renderer todo.

Porém, no repeatHusk elapsed37 há flash vermelho do acerto antes da câmera, que aparece em39. A transição de morte ainda não está limpa. Não houve vídeo contínuo e há gaps: não anunciar ausência absoluta de duplicação ou de flashes de um frame. O corte da katana e a estética do fade NÃO foram homologados por essa prova de vítima única.

Evidências na raiz principal: Docs/poderes/qa/cycle141/higashi-repeat/lethal-contact.jpg, threshold-contact.jpg, qa-events.log e summary.json. NÃO confundir rótulos antigos de filenames DRAW/HOLD/ASH com idade real da POSES/BURNS.

## Fase1 — corrigir o harness antes de declarar resultados

As alterações QA locais do Codex reutilizaram threshold como cenário Hollow alado letal10/20. Separar nomes de forma explícita: lethal_husk, lethal_flying, threshold_real, survive, miss, ou equivalente. Atualizar expectativas e comando/sequence juntos, mantendo um teste real≤7% se alegar cobertura desse ramo. Não alterar produção para fazer o caso passar.

Expor telemetria opt-in de idade real POSES/BURNS, tick do dano/confirmação, recebimento de pacote, originalPresent e originalSuppressed. Associar a screenshot à MESMA observação; não nomear fase por elapsed-20. Manter aquecimento inicial e registrar frames faltantes. Limpar visualmente cada cenário de teste sem apagar construções ou mundo do usuário; fogo residual no cenário não prova fogo da vítima.

## Fase2 — diagnóstico da janela anterior à confirmação

Registrar ordem do dano normal letal, hurt/status3/60, pacote Higashi, render vermelho/tilt, remoção e primeira representação do replay. Encontrar a janela comprovada, não zerar hurtTime globalmente. Corrigir especificamente a transição das vítimas confirmadas sem afetar danos comuns.

Se estudar uma marcação transitória de tentativa, ela NÃO é execução: não pode abrir câmera, gerar cadáver/cinzas, matar visualmente ou ocultar de forma prolongada um sobrevivente. Ter timeout/rollback para dano cancelado, totem, aliado, invulnerável e sobrevivente. Não prever morte por dano bruto nem alterar o resultado servidor para adequar à cena. Se a solução depende de rede nova, justificar protocolo, servidor dedicado e limpeza; evitar ampliação desnecessária.

O objetivo visual: quando a apresentação confirmada começa, mostrar UMA vítima íntegra e consistente, sem flash vermelho/queda anterior perceptível, sem corpo duplicado ou reaparecimento. Não prometer eliminar causalmente um frame já desenhado; ajustar a arquitetura da janela e documentar restrições verificadas.

## Fase3 — validar sem regredir vítima única

Repetir Husk10/20 e HollowFLYING10/20, sobrevivente e erro, mais totem/cancelamento conforme suporte. Corpo original suprimido somente no contexto válido; réplica preserva asas/skin/equipamento e some uma vez. Não substituir texturas de todas camadas pela base para obter fade. Shader/RenderType precisa sustentar transparência: ligar blend global não é prova de fade correto.

Preservar pausa20ticks após terminar corte, IGNITE36/END68 quando mantidos esses marcos; medir por relógio real da apresentação. Retorno de câmera/HUD/FOV/controles e limpeza em morte/dimensão/logout. Observador não perde controle.

## Fase4 — próxima prova da arma, só após transição estável

Congelar VFX para avaliar HG-F01/F02 (quadril/preparação baixa), HG-F03 (diagonal ascendente) e HG-F04 (final alto). A prova deve ser da katana renderizada, não vetor bladeDirection. Não declarar que rightItemZERO garante vínculo correto; testar a cadeia real com display/pivôs do modelo. Examinar também a alegada cessão de ElementalActionVisuals: conferir se consulta ID do executor ou ID da vítima e qual mapa cada função indexa; não presumir que um if com nome isExecuting funciona para ambos.

Essa fase não autoriza reabrir transformaçãoRyūjin, dash/rajada, C, Hyōrinmaru, Senbon, Benihime ou Zaraki. Se transição ainda falhar, entregar reprodução e corrigir antes do acabamento do corte. Não adicionar partículas para esconder gesto.

## Comandos de partida e validação (PowerShell)

```powershell
Set-Location -LiteralPath 'C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-benihime'
git branch --show-current
git status --short --branch
git log -5 --oneline
git diff -- src/main/java/com/bleachmod/client/cinematic/CinematicQaCommands.java src/main/java/com/bleachmod/client/cinematic/CinematicQaSequence.java src/main/java/com/bleachmod/client/model/HigashiExecutionVisuals.java
git diff --check
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot'
if (-not (Test-Path -LiteralPath "$env:JAVA_HOME\bin\java.exe")) { throw 'JDK17 informado ausente; localizar JDK existente antes de continuar.' }
.\gradlew.bat runClient -PbenihimeQa=true -PcinematicQa=true -PhigashiPoseQa=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain
# Depois de corrigir e fechar o cliente:
.\gradlew.bat check build --offline --console=plain
```

Antes de repetir, confirme que o harness local contém estabilização e os casos desejados. Depois de separar os nomes novos, mantenha esse comando como entrada reprodutível ou documente o substituto exato. Bootstrap abre só saveQA descartável; se tela inicial bloquear, resolver a UI autorizada antes de afirmar que testes ocorreram. Rodar um cliente por vez.

Como há lógica de eventos/lifecycle, verificar runDirectory de GameTests descartável e executar a suíte pertinente:
```powershell
.\gradlew.bat runGameTestServer --offline --console=plain
```

Regressões150 de outra branch não são seu resultado; informar contagem real e comandos/exits. `test` sozinho não executa o JavaExec regressionTest neste projeto.

## Entrega

Antes/depois da janela vermelha com idade real, log de morte normal acima7%, vídeo contínuo quando possível ou screenshots com gaps declarados; comparação do mesmo alvo/renderer e condições. Estado separado: transiçãoPASS/FAIL, vítimaúnicaPASS/FAIL, katanaPENDENTE ou comprovada por HGquadros. Commits focados, sem logs/mundos. Nenhum merge/push. Não proclamar correção universal por teste solo em duas entidades.
