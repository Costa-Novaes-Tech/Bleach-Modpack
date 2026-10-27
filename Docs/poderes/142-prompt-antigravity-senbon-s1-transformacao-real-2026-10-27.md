# 142 — Prompt Antigravity Senbon: provar e corrigir orientação real do S1

Copie este pedido para a conversa Senbon. Trabalhe em `codex/design-poderes-criaturas`, checkout `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`.

## Limites e estado inicial

NÃO fazer merge, rebase, push ou alterar main. Trabalhar no checkout e branch existentes. Não usar reset --hard, clean, restore de arquivos alheios ou aplicar patch cegamente. Você não está sozinho: preserve o trabalho do Codex e da outra conversa.

Há alterações locais de QA deixadas pelo Codex no seu checkout. Elas são INTENCIONAIS. Leia git diff antes de editar; não trate working tree sujo como lixo. Os patches de referência ficam em C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/Docs/poderes/qa/cycle141/. Não aplicar esses patches outra vez se já estiverem presentes. Não comitar logs, mundos, caches ou alterações de terceiros. Commits focados das suas correções podem ser entregues para revisão conforme autorização vigente; integração continua proibida.

Leia também, na raiz principal Bleach-Modpack, Docs/poderes/141-provas-isoladas-reais-senbon-higashi-2026-10-27.md e 137-storyboards-direcao-katanas-senbon-higashi-2026-10-27.md. Alguns documentos/evidências estão locais e não commitados nessa raiz: não assumir que git pull os trará à sua branch, nem fazer merge só para obtê-los. Ler pelos caminhos absolutos locais.

Use skills e instruções do projeto. Documentação/evidências novas em Docs/poderes. Não instalar bibliotecas, serviços ou ferramentas novas para substituir diagnóstico. Cliente Forge real é a prova gráfica. check/build não executa automaticamente runGameTestServer; test sozinho não substitui regressionTest.

## O que já foi comprovado — não repetir diagnóstico antigo

A prova Codex141, base6e4d767, executou um único ataque, sem alvo e com melee VFX desligado, nas três vistas. No perfil, age33 do cenário apresenta:
- rightArm ≈ (-1.7499833, .37999332, -.37999135)
- rightItem ≈ (-.15000999, .24999666, -1.5499899)

Esses valores correspondem aproximadamente ao tick8 autoral do S1. Logo, clip e canais estão ativos. Mesmo assim, a katana renderizada aponta para baixo/para trás na preparação que deveria ter ponta elevada. Não declarar resolvido só porque parser/JSON passou. Evidência principal: na raiz principal, Docs/poderes/qa/cycle141/senbon/diagonal-preparacao-reference-vs-forge.png e 02_combo_profile/frame-t033.png. Telemetria/source.json preservam origem.

## Escopo fechado

Corrigir APENAS orientação/empunhadura e execução do golpe1 Senbon. Golpes2/3, rastro, partículas, ultimate e demais kits ficam congelados. Nada de mudar dano, alcance, attack speed ou movimento real para encaixar o estudo. Não editar Higashi/Hyōrinmaru/Ryūjin/Benihime/Zaraki.

1. Diagnóstico de transformações, antes de novo ajuste artístico

Mapeie a cadeia REAL: pose braço/cotovelo/bend → transform de mão → bone item PlayerAnimator → transformação fixa vanilla → display do modelo → orientação longitudinal da geometria. Identifique em qual biblioteca/mixin cada parte ocorre; não invente um PlayerAnimatorHeldItemMixin local se ele não existir no repo. Inspecione o código/dependência local pertinente antes de chamar uma API.

O modelo Senbon Shikai herda ryujin_katana_handheld, com thirdperson_righthand[0,-90,55], além das rotações de elementos. Verifique composição, unidades1/16, graus/radianos, ordem/sinais e mão dominante. Isso é hipótese dirigida, não justificativa para alterar o modelo pai compartilhado.

Registre transform final ou marcadores opt-in no QA da base do cabo e ponta renderizados, quando tecnicamente possível. Não usar bladeDirection calculado por fórmula independente como prova de orientação do item real. Compare tick da animação efetiva com age do cenário; comboCount que sobe após golpe não é ID confiável de outro clip.

2. Calibração mínima

No mesmo cenário, obter primeiro S1-F02: cabo na palma, mão junto ao próprio ombro direito, ponta alta e fora do rosto. Ajustar a parte responsável com menor escopo: asset específico Senbon/transform local da identidade. Não alterar display pai global ou mixin universal sem necessidade demonstrada e revisão separada. Não mover arma independentemente da mão para apenas imitar o desenho.

Depois validar S1-F04 (travessia diagonal) e S1-F06 (final baixo esquerdo), mantendo preparação→corte→recuperação. Um único input; frente/perfil/costas; partículas desligadas. Não reconstruir novamente todo combo. Mão livre apenas contrabalança, não duas mãos.

3. Critério para considerar o patch apto à revisão

Na captura do marco de preparação a lâmina deve estar alta, não só o braço. No perfil, cabo não flutua e ponta não está atrás do jogador. A diagonal deve atravessar a frente do corpo e terminar baixa à esquerda. Parar o input recupera a guarda. Provar lado a lado referência137 versus screenshot do JOGADOR desobstruído, com clip/tempo efetivo/partial tick identificados.

Se o primeiro marco falhar, continuar diagnóstico, não avançar paraS2/S3 nem informar concluído. Não é necessário pedir aprovação de rotina entre investigações; entregar para aceite humano somente resultado verificável.

## Comandos de inspeção e prova (PowerShell)

```powershell
Set-Location -LiteralPath 'C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-design'
git branch --show-current
git status --short --branch
git log -5 --oneline
git diff -- src/main/java/com/bleachmod/client/cinematic/SenbonzakuraCombatQaSequence.java
git diff --check
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot'
if (-not (Test-Path -LiteralPath "$env:JAVA_HOME\bin\java.exe")) { throw 'JDK17 informado ausente; localizar JDK existente antes de continuar.' }
.\gradlew.bat runClient -PbenihimeQa=true -PsenbonCombatQa=true -PsenbonVfxDisable=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain
# Depois da alteração funcional, validar fora de outro cliente/Gradle concorrente:
.\gradlew.bat check build --offline --console=plain
```

O runClient acima usa o QA local modificado pelo Codex: só3vistas, um ataque no vazio, câmera centrada, capturas e pose-telemetry.jsonl. Sem esse diff, o comando pode voltar a testar combo/Hakuteiken; confira antes. Melhorar o harness com opção exclusiva, se necessário, sem perder a prova mínima. Não deixar nome combo no diretório virar alegação de combo validado.

Só se mudar lógica que exige GameTests, conferir o runDirectory descartável configurado e executar:
```powershell
.\gradlew.bat runGameTestServer --offline --console=plain
```

Executar comandos sequencialmente; guardar saída/exit. Não lançar dois clientes na mesma máquina simultaneamente. Não apagar worlds de outras conversas; bootstrap cria save deQA próprio. Não chamar GIF de screenshots de vídeo contínuo: registrar cadência/gaps; preferir captura contínua existente quando disponível.

## Entrega esperada

Diff pequeno, explicação de qual transform estava errado, capturas reais de preparação/passagem/final, vídeo/seq temporal honesta, logs/telemetria, check/build e limites. Relatório diz PASS/FAIL por marco; não conclusão baseada em vetores auxiliares. Entregar o primeiro golpe para revisão, sem merge e sem progredir ao resto do kit.
