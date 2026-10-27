# 145 — Antigravity Senbon: QA e pacote de evidências para auditoria Codex

27/10/2026. Encaminhar à conversa Senbon existente. Sem merge, push ou alteração main. Branch codex/design-poderes-criaturas; checkout Bleach-Modpack-design. Usuário autoriza que você execute os QAs e produza as capturas para Codex auditar diretamente pelos arquivos locais. Não abrir ou enviar mensagens a outra conversa do Codex por conta própria; entrega é relatório+arquivos e caminhos absolutos para o usuário encaminhar.

## Auditoria da entrega e escopo

Codex leu retorno e commit e0a79a6 e abriu as pranchas preparação/final. A preparação melhorou efetivamente: ponta alta em vez de baixa/para trás. É avanço visual real, mas NÃO aprovação definitiva de empunhadura, arco completo ouS2/S3.

A tabela de vetores resulta da cadeia matemática; não chamar de matriz final medida no render sem demonstrar instrumentação dessa matriz. Erro angular pequeno não comprova posição da mão, contato do cabo, ausência de clipping, nem blending. GIF de screenshots100ms não comprova fluidez em tempo real. RótulosPASS/verde das pranchas são vereditos do autor, não aprovação Codex/usuário.

Não refazer todo o clip. Preserve correção de direção e faça QA focado de S1, relatando defeitos antes de editar qualquer outra fase. S2/S3 e VFX continuam congelados. Nenhuma intervenção em Higashi/Hyōrinmaru/Ryūjin/Benihime/Zaraki.

## Preparação

Confira Git/status/diff. Preserve instrumentação local Codex em SenbonzakuraCombatQaSequence. Não limpar logs/temporários/documentos alheios por conveniência. Ler137,141,142 da raiz principal Bleach-Modpack/Docs/poderes (alguns arquivos são locais não commitados). Não aplicar patch QA já presente novamente.

Corrigir apenas lacunas do harness: câmeras desobstruídas e fixas no jogador, sem inimigo na frente; um só input; aquecimento antes do primeiro cenário; captura do tempo real do clip com velocidade e partialtick. `comboCount` não basta como identificação. Casos precisam de origem documentada, nomes únicos e sem seleção automática ambígua por pasta “mais recente”.

## Comandos (PowerShell)

```powershell
Set-Location -LiteralPath 'C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-design'
git status --short --branch
git log -4 --oneline
git diff -- src/main/java/com/bleachmod/client/cinematic/SenbonzakuraCombatQaSequence.java
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot'
.\gradlew.bat runClient -PbenihimeQa=true -PsenbonCombatQa=true -PsenbonVfxDisable=true -PcinematicQaFast=true -PqaAutoExit=true --offline --console=plain
# Só após mudanças novas; evitar repetir build inalterado apenas para gerar números:
.\gradlew.bat check build --offline --console=plain
```

Confirmar antes que o harness ainda testa sóS1 nos três ângulos. Rodar um cliente por vez; fechar por saída normal do teste. Não rodar GameTests por mudança puramente visual sem necessidade concreta.

## Capturas exigidas

A. S1 isolado em Shikai, destro, frente/perfil/costas: guarda, recolha, ponta alta, início, travessia, fim e recuperação. Repetir selada e Bankai depois da prova principal para verificar display e geometria; canhoto ao menos preparação/contato/final e sequência. Não corrigir todas formas por offset global sem mostrar regressões.

B. Vídeo contínuo em velocidade real do golpe e idle final, com tick/clip sobreposto ou sidecar sincronizado. Preferir ferramenta de captura já instalada. Se só conseguir screenshots, informar gaps, amostragem, duração de GIF e que fluidez permanece pendente; não transformar amostragem artificial em prova de tempo real.

C. Para cada marco S1-Fxx, quadro real frontal+perfil com: mão dominante, cabo/guarda e ponta identificáveis, cabeça livre, relação mão/ombro e ponta/solo. Compare proporções normalizadas pelo tamanho do jogador; não comparar só vetor da ponta nem exigir igualdade pixel a pixel ao boneco esquemático137. Garantir que frame selecionado pertence ao marco real da animação, não age fixo arbitrário.

Critérios: cabo preso à palma; preparação junto ao ombro; corte atravessa a frente; final mão/lâmina à esquerda anatômica (não apenas ponta inclinada à esquerda com mão parada à direita); retorno ao idle sem salto. Registrar qualquer diferença de braço/corpo, não declarar “100% firme” por offsety=-2.

## Entrega auditável diretamente

Criar pasta única Docs/poderes/qa/cycle145-senbon/<run-id>/ contendo:
- manifest.json: branch, commit, diffSHA256/dirty files, comando, flags, exit, runDirectory, mundoQA, renderer, Java/Forge, origem de captura.
- timeline.jsonl: arquivo, worldTick, idade real clip, partialtick, speed/upswing, clip/identidade, forma/mão, pose braço/item e transform final se medido. Distinguir valores medidos de calculados.
- PNGs originais e comparativos com IDs137; GIF/vídeo com cadência declarada. Listar faltantes e não usar captura antiga como nova.
- parecer por marco: observado, divergente e pendente; paths absolutos, hash de cada artefato e log resumido com comando/exit. Não enviar logs completos com credenciais/tokens.

Relatório curto em Docs/poderes com tabela textual de critérios e próximos ajustes estritamente necessários. O usuário encaminhará os caminhos ao Codex, que abrirá PNGs/vídeos/metadados diretamente. Sem merge e sem expandirS2/S3 até revisão dessa prova.
