# Ryūjin v13 — acabamento do sol junto aos efeitos

**Comparação posterior com as referências:** [BleachCraft × estado atual e proposta v14](comparacao-bleachcraft-ryujin-v13-e-plano-v14-2026-11-06.md). Nova inspeção de mídia confirma ganho na massa/luz e destaca o final na katana. Há correntes dirigidas no código, mas seus cards ficam verticais; orientação por ribbons e novo foco de câmera são próximos ensaios, ainda sem implementação. [Guia recuperável](guia-transformacoes-modelagem-vfx-qa-2026-11-06.md) reúne resultados e limites desta sequência de testes.

**Retorno humano posterior, 06/11:** qualidade melhorou muito; permanecem ajustes de câmera/posição, animações/FX e áudio. Selamento foi destacado como anticlimático: [retrospectiva e proposta do próximo ensaio](revisao-ryujin-v13-e-entregas-antigravity-2026-11-06.md), com convergência na lâmina e close/pausa na espada carbonizada, mantendo os 15 s como hipótese inicial. Não é homologação final nem implementação já feita dessa nova direção.

Decisão do usuário em 06/11/2026: com os novos efeitos/luzes, as partes separadas do modelo solar passaram a parecer um downgrade. **Polir o sol é prioridade obrigatória da etapa completa**, sob responsabilidade Codex. Base `Feature-Poderes-bankais`/`9eb6a05`; trabalho Antigravity A Quincy e B P0/Rukia preservado. Sem commit/merge/push.

## Direção de construção

- Preservar tamanho, posição traseira, giro alternado, cena de 15 s e sol sustentado entre 112–176. Não compensar o problema apenas aumentando bloom ou partículas.
- Núcleo com volume contínuo e textura compartilhada entre as peças, reduzindo a aparência de lâminas horizontais empilhadas. Aumentar sua presença dentro do conjunto, mantendo o limite externo aprovado.
- Filamentos curvos sobrepostos, espessura e profundidade variáveis. Reduzir intervalos regulares e dentes que lembram engrenagens; não adicionar novos raios retos.
- Bordas com línguas de chama curvas e afiladas, integradas aos filamentos, sem esconder a rotação ou virar uma roda rígida.
- Material quente com massa central clara, transição dourada/laranja e borda escura. Textura em coordenadas contínuas, evitando uma cor diferente por cubo/linha. Emissivo continua separado de luz ambiental.
- Absorção preserva aceleração e destino na lâmina. Conferir o novo volume durante a contração; nova geometria não pode se separar em peças soltas no final.

## Execução e aceites

1. Preservar fontes/exports v10 e criar variante v13 reproduzível, com projeto próprio no laboratório Codex. Manter nomes públicos dos bones ou adaptar seus consumidores explicitamente. Meta inicial até 300 cubos solares, sem aumentar os cards/partículas ou fontes de luz.
2. Vistas nativas individuais frontal, perfil e 3/4 para comprovar continuidade e profundidade; versões de material e de inspeção geométrica, sem FX cobrindo defeitos. Se gerar novas referências IA, seguir pares liso/em blocos, um arquivo por ângulo/frame, sem detalhes inventados de cenário.
3. Validar `.bbmodel`, `.geo.json`, atlas/glowmask e clipe exatos no Blockbench Codex/8788. Scripts desativados; nenhuma mutação na instância Antigravity/8787. Nesta retomada a ponte estava indisponível, e foi reaberto o perfil temporário Codex já existente; `get_status` confirmou Blockbench 5.2.2, sem projeto aberto.
4. A/B no Minecraft: v10 e v13 com mesma câmera, luz e densidade; núcleo legível, sol completo, aro perceptível sem aspecto mecânico, absorção coesa. Não promover só pelo preview do editor.
5. Conferir custo, cleanup e reload depois da nova geometria. Só então escolher a variante padrão; a existência do protótipo não constitui aceite humano.

## Pendências globais preservadas

[Relatório v12 e matriz de gates](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md): shader userdev em conflito, comparação Photon/AAA, cosméticos animados, multiplayer e escuta/mixagem/licenças. A nova prioridade do sol entra antes da comparação artística final; não encerra ou substitui esses gates.

## Primeiro ciclo de autoria

Gerador reproduzível `tools/models/lab/codex/ryujin01/build_sun_v13.py`, preservando os originais. Dois ensaios nativos foram rejeitados antes de adoção: v13a lembrava moeda/engrenagem; v13b tinha perfil de placas horizontais empilhadas. Cópias identificadas no laboratório; não são candidatos a distribuir.

A variante v13c constrói um elipsoide voxelizado preenchido e faz união gulosa em cuboides; usa 72 cuboides no volume em vez de faixas de latitude. Filamentos espirais acompanham a superfície frontal. Removidas as hastes radiais e a corona geométrica antiga; as chamas/corona de runtime permanecem e não tiveram sua densidade aumentada. **188 cubos / 33 bones / 15 s**, frente -Z, names públicos preservados. Mesmo footprint externo dos FX, sem aumentar luzes/partículas.

GeckoLib instalado declara `optional_box_uv: true`; isso foi conferido no plugin real. UVs por face projetam a mesma textura nas peças do volume. A primeira projeção espelhava cada face norte individualmente e criava blocos de cor: inverter a coordenada U da face norte eliminou essa emenda no preview. Não generalizar o sentido para todos os formatos sem testar. Atlas único 256×4096, com regiões novas fora das UVs originais; originais intactos.

`get_status` confirmou o projeto correto; **check_model=0** na variante c. Geometria exportada pelo codec nativo **bedrock**, 106119 bytes na primeira exportação (verificar hash/tamanho final no manifesto, não usar esse tamanho como identidade). [Vistas separadas por ângulo/frame](media/ryujin-sun-v13-20261106): frontal, 3/4 e perfil em 6,5 s e 9,5 s. São capturas do editor e não arte conceitual; ainda não constituem aprovação artística.

Foi preparado `RyujinSunV13Local` somente em `run-benihime-qa/visual-sun-v13/resourcepacks`, substituindo geometria/atlas/glowmask pelos exports exatos. O primeiro teste Forge concluiu, mas a variante c foi rejeitada: aspecto metálico e deslocamento entre corpo solar e corona. [Evidência preservada](qa/codex/v13c-sun-rejected-20261106/manifest.json). O JAR e o perfil normal continuam com o modelo anterior; variantes seguintes são pilotos isolados.

## Correções comprovadas no cliente

- **Âncora compartilhada:** a inspeção do JAR GeckoLib **4.8.4** mostrou que `GeoObjectRenderer.preRender` acrescenta `(0.5, 0.51, 0.5)` ao origin de objeto. Somar apenas as posições dos bones não incluía esse deslocamento. O piloto cancela esse origin e usa `RyujinSolarPose` para levar modelo, corona e luz do mesmo centro traseiro até a lâmina. A contração deixa de disputar dois deslocamentos. Regressão cobre início, término e progresso para as duas mãos.
- **Material contínuo:** o atlas passa a avaliar a mesma função de cor em posições 3D nas seis faces de cada cubo, com padding. Projeções 2D independentes não preservavam temperatura entre faces. Isso reduziu diferenças de pintura, mas **não resolveu sozinho o contraste de iluminação nas arestas**.
- **Causa do aspecto metálico:** o shader vanilla `rendertype_entity_translucent_emissive.vsh` do Minecraft 1.20.1 ainda chama `minecraft_mix_light` com `Normal`. `FULL_BRIGHT` não elimina essa luz direcional. A mudança isolada para o shader Eyes, mantendo a pintura e geometria e, removeu grande parte desse contraste; captura real em `visual-sun-v13e`, ciclo `118-1793991399255`. Eyes foi controle de diagnóstico, não material final: não possui descarte alfa adequado ao volume.
- **Material solar próprio:** `RyujinSolarShader` registra um shader sem luz direcional, com descarte alfa `<0.1`, fog e transparência convencional. Fallback explícito Eyes se o carregamento falhar. RenderType escreve cor/profundidade; o piloto evita a segunda passagem AutoGlowing. Não acrescenta bloom nem iluminação do mundo; a luz de superfícies continua sendo Shimmer opcional. Compatibilidade com shader packs externos continua pendente.
- **Faixas:** faces laterais transparentes evitam paredes metálicas; UVs acompanham o comprimento completo das espirais. Pontas têm fade e as geometrias antigas de aro fragmentado/faíscas foram removidas, mantendo seus bones públicos vazios para preservar o contrato do clipe.

## Controle f — shader e recarga aprovados tecnicamente

`check runClient`, perfil `visual-sun-v13f`, cena noturna, `ryujinSolarPosePilot=true`, `ryujinQaReload=true`: saída **0**, **180 regressões**, shader registrado antes e depois da recarga em plena sustentação. [Manifesto e log](qa/codex/v13f-unlit-reload-20261106/manifest.json), [telemetria](qa/codex/v13f-unlit-reload-20261106/performance.json). `solar_shader=solar-unlit`, `post_seal_clean=true`, `post_end_clean=true`, diferença entre relógios 0; limites 264 cards / 192 partículas próprias / 1 luz respeitados. Capturas ativas; p95 desta execução não é benchmark comparativo.

As imagens t160/190 confirmam corpo e corona juntos, núcleo quente e absorção coesa. O t144 contém a barra da recarga de recursos e não serve como imagem artística limpa. O contorno ainda tinha degraus grandes; portanto f não encerrou o acabamento. [Vistas nativas f](media/ryujin-sun-v13-20261106/f), uma por ângulo/frame, são referência geométrica com iluminação do editor, diferente do material real.

## Candidato g — contorno refinado

Grade do elipsoide reduzida de 8 para **5 unidades**, união em 184 cuboides de volume mais 96 segmentos das espirais: **280 cubos / 33 bones / 15 s**, abaixo da meta 300. Mantém dimensões externas e cotas de FX/luz. Bake de baixa frequência a 0,75 texel/unidade preserva o atlas 256×4096; último shelf termina em 1900, antes dos filamentos em 2624. O primeiro bake a 1 texel/unidade excedeu a área reservada e foi interrompido pela asserção, antes de substituir o candidato; não foi exportado como válido.

`check_model=0`, exportação nativa bedrock **139208 bytes**, PNG SHA-256 `86829008f7f885a27da41d60a6ad75b117ca408d362584fc96a4479d0a057f7d`. [Vistas nativas g](media/ryujin-sun-v13-20261106/g), individuais. Fonte reproduzível no gerador; export exato em `tools/models/lab/codex/ryujin01`. Matriz cliente/vídeo em `visual-sun-v13g` concluída conforme resultado abaixo. O campo `pending` do manifesto do gerador enumera verificações externas à autoria; a evidência de execução dessas verificações está nos manifestos QA, não é inferida pelo gerador.

**Estado de adoção:** `ryujinSolarPosePilot` permanece desligado por padrão; só os resource packs QA substituem a geometria. Nenhum aceite artístico humano, instalação no pack normal ou merge é inferido dos testes técnicos.

## Resultado da matriz g

`runClient`, Shimmer/HIGH, piloto solar, vídeo framebuffer: **12/12 cenários executados**, saída **0**, `BUILD SUCCESSFUL in 5m 4s`. [Índice e telemetria](qa/codex/v13g-final-matrix-20261106/summary.json), [log](qa/codex/v13g-final-matrix-20261106/client.log), [fontes/hashes](qa/codex/v13g-final-matrix-20261106/source-manifest.json). Gerador reexecutado: seis arquivos produzidos mantiveram hashes idênticos; export nativo preservado.

Nos oito casos da transformação, `post_seal_clean` e `post_end_clean` passaram, shader `solar-unlit`, máximo 264 cards/192 partículas/1 luz. Retorno de primeira pessoa e perspectivas anteriores preservados; cancelamento antes do commit termina em Shikai, depois em Bankai. Casos 9–12 terminam em 220: ausência de amostra após 302 continua sendo **não observado**, não prova de cleanup do término da transformação.

Inspecionados dia/noite, lâmina canhota na explosão, sol em parede e absorção: massa central mais contínua, brilho sem escurecer por face, corona envolvendo o mesmo centro e sol inteiro dentro do fixture confinado. Restam degraus finos na silhueta e faixas ainda regulares; melhora técnica não encerra o aceite artístico. A cortina usa sprites vanilla ampliados, com repetição visível de perto; acabamento Photon/AAA permanece gate separado. Sem novos cubos de fogo físico durante a cena.

Vídeo noturno local `build/visual-lab/v13g-ryujin-preview-silent.mp4`: H.264 1280×720, **20 fps / 16,3 s, sem áudio**, SHA-256 `8eaaaf4e47eb170e163a137e905df46299d3a4b1e4c0034de38213bb63e3140b`. 317 quadros aceitos, três descartes por fila, nove repetições, encoder exit 0; não é prova de cadência sem captura. [Inspeção temporal](qa/codex/v13g-final-matrix-20261106/inspection.json) e contato interno de QA amostram sustentação e absorção; as referências geométricas continuam em arquivos individuais por ângulo/frame.

### Reabrir para avaliação humana

Na raiz do projeto, com Java/Gradle existentes, o comando usa somente o perfil QA preparado, roda uma cena noturna e mantém o cliente aberto ao fim. Não acrescenta Shimmer ao pack pessoal nem substitui o modelo do JAR:

```powershell
.\gradlew.bat runClient -PshimmerPilot=true -PbenihimeQa=true -PcinematicQaFast=true -PryujinBankaiQa=true -PryujinBankaiQaCase=1 -PryujinSolarPosePilot=true -PryujinVisualQaProfile=sun-v13g --offline --console=plain
```

Esse perfil está silencioso para inspeção visual. Para mixagem, retomar o perfil específico de áudio após combinar os packs locais e os direitos dos stems, sem chamar o vídeo silencioso de teste sonoro.

## Custo sem captura e fechamento técnico do ciclo

Três repetições noturnas no mesmo perfil/mesma versão, sem screenshot nem vídeo, saída **0**: [metadados](qa/codex/v13g-performance-20261106/summary.json), [log](qa/codex/v13g-performance-20261106/client.log). Intel Iris Xe, 1280×720, vsync desligado, limite 120, HIGH/Shimmer. p95 **26,13 / 16,50 / 17,25 ms**, p99 **29,44 / 20,26 / 20,50 ms**. Orçamentos mantidos e limpeza nos três ensaios. Host compartilhado, primeira execução mais lenta; não é comparação causal com a geometria v12 nem medição GPU isolada. Não houve aumento do atlas, luzes ou partículas para mascarar o volume.

Skills de projeto `bleach-model-pipeline` e `bleach-mod-dev` atualizadas com causa, controle e limite do shader/âncora; frontmatter validado. Continuidades e roadmap atualizados. Nenhuma skill global, mundo pessoal ou frente Antigravity alterada. Branch `Feature-Poderes-bankais`, HEAD `9eb6a057`; sem commit/merge/push. Os 296 GameTests anteriores continuam a prova disponível do servidor; **não foram reexecutados neste ciclo de material/geometria cliente**.

Próxima revisão deve decidir núcleo/contorno/faixas pela prévia atual, antes de promover o piloto. Gates externos permanecem: compatibilidade em Forge empacotado com shader pack (falha userdev v12 preservada), VFX Photon/AAA e repetição dos cards, cosméticos animados/Better Combat, dois clientes/dedicado e mixagem/licenças. O fallback Eyes do material é contingência registrada, não equivalência garantida ao descarte alfa do shader solar; se aparecer `fallback-eyes`, interromper a homologação do material e investigar o carregamento.
