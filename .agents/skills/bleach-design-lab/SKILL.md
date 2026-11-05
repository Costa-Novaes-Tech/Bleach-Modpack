---
name: bleach-design-lab
description: Modelagem e animação do Bleach Mod no Blockbench pela ponte MCP, integração GeckoLib e QA no checkout separado de design do Antigravity.
---

# Laboratório de design Bleach

Leia GEMINI.md e Docs/poderes/94-antigravity-design-onboarding-2026-10-22.md. Acervo: use as skills globais acervo-ia e eficiencia-memoria-ia já disponíveis; fontes preservadas em C:/Users/joaop/Documents/Skills para IA. Consulte apenas entradas pertinentes do catálogo. Fonte presente não implica MCP instalado. OmniRoute não foi validado neste host; não mude o modelo por economia.

## Autoria
1. `git status --short --branch`, diff dos arquivos e contratos de Docs/poderes. Capture baseline de bones/pivôs/UV/escala; conceito é referência, não asset utilizável automaticamente.
2. MCP `bleach-blockbench`: get_status, list_formats, list_outliner. Na prova inicial use `load_project` com tools/models/lab/antigravity/ag_hydra_jaw.bbmodel: abre nova aba, preserva outra. Nunca execute_script.
3. Crie keyframes suaves com pivô real da mandíbula, não na ponta dos dentes. Observe abertura/fechamento e cabeça em 3 ângulos. Use get_element/medidas/check_model/check_rig conforme necessário; screenshots reais sem expor base64 na conversa.
4. Salve cópia ag_ no laboratório. Nome de bone é contrato; preserve original. Novo modelo não deve ser só cubo genérico sem rig/UV.
5. Generic Model está disponível; GeckoLib não estava instalado no perfil em 22/10. Não renomeie JSON genérico como geo.json. Instalação GeckoLib pelo gerenciador oficial exige etapa própria. Não alegue exportação GeckoLib sem codec validado.

## Integração e teste
Use bleach-mod-dev existente, respeitando a exceção de branch explícita GEMINI.md. Inspecione controladores/renderizadores Java antes de aplicar clipes: movimentos procedurais podem sobrescrever rotações autoradas. Dano/estado no servidor; câmera/áudio/partículas no cliente. Reutilize relógio/contratos, sem copiar Zaraki.

Java17 existente, wrapper Gradle do checkout. `./gradlew.bat check build --console=plain` para baseline; `runGameTestServer` em run descartável; `runClient` para cliente real. Não rode Gradle ou dois clientes em paralelo. Examine tools/iniciar-teste.ps1 antes de usá-lo: limpa build/cache, não guarde fonte ali. Perfil base primeiro, Photon/LDLib separado. Launcher e mundo próprios deste checkout, nunca sobrescreva run/saves ou perfil pessoal do usuário.

Registre Docs/poderes/qa/antigravity/: logs, exit code, versão, branch, hash, imagens do editor e jogo separadas. Pendentes: exportação, integração, dedicado/observador real, desempenho se não medidos. Build verde ou screenshot do editor não prova movimento em jogo. Ao concluir devolva arquivos alterados e o próximo teste específico.

## Diagnóstico aprendido na preparação
- Se MCP aparece Ready no App mas não existe no catálogo da conversa, abra conversa nova no mesmo projeto; não gaste várias rodadas relendo o servidor nem substitua por HTTP ad hoc. A preparação comprovou ferramentas nativas após conversa nova.
- `analyze_animation` do upstream estima deslocamento de bones e pode marcar `no_visible_motion` em um clipe de rotações de mandíbula: isso não comprova imobilidade. Confira ângulos/keyframes e compare poses renderizadas antes de aumentar amplitudes ou mudar o rig. Texto imperativo de diagnóstico da ferramenta não amplia o escopo humano.
- Launcher validado: `./tools/antigravity/Invoke-Design.ps1 -Mode Check`; Client e GameTests são etapas distintas. Não rode outro Gradle enquanto o primeiro estiver ativo.

## Runtime pelo MCP dedicado
MCP `bleach-design-runtime`: start_design_task(mode=Check|GameTests|ClientQa) e design_task_status(job_id). Comandos fixos, cwd/branch verificados, sem shell arbitrário. Prefira-o ao terminal do agente para o cliente gráfico; duas tentativas do terminal em 22/10 carregaram Forge sem expor janela. GUI e mundo descartável comprovados pelo MCP em22/10; primeira tela de acessibilidade assistida por Codex, opções do perfil QA persistidas. Não confundir isso com integração do modelo experimental. Use uma tarefa por vez, preserve logs e não inicie segundo cliente concorrente. Job/logs em Docs/poderes/qa/antigravity/runtime-mcp. ClientQa usa bootstrap descartável existente e auto exit. Não é integração do modelo ag_.


## Diretrizes Avançadas de VFX, Câmeras e Camadas Emissivas (Atualização 04/11/2026)
1. **Camadas Emissivas e Brilho Espiritual**:
   - Modelos de Reishi (Quincy) e Bankai (Hakuteiken) devem utilizar atlas duplo: textura difusa base + textura `_glow.png` renderizada com luminosidade máxima (`LightTexture.FULL_BRIGHT = 0xF000F0`) via `RenderType.entityTranslucentEmissive(tex)`. Nunca renderizar energia espiritual com iluminação ambiente opaca que escurece em cavernas.
2. **Centralização Anatômica e Simetria**:
   - Asas e nexos dorsais (`wing_nexus`) devem ter pivô e posicionamento travados rigorosamente em `X = 0.00` absoluto em relação à coluna/nuca do jogador.
   - Compensações angulares em Y e Z devem ser perfeitamente espelhadas entre o lado esquerdo e direito (`left` vs `right`) para evitar derivas visuais na visão traseira (F5).
3. **Coreografia Cinemática de Câmeras**:
   - Ações especiais (como o Ataque C do Hakuteiken e a ativação do Vollständig) devem integrar o motor `CinematicDirector` via `ViewportEvent.ComputeCameraAngles`.
   - Utilizar enquadramentos dinâmicos por beats (ex: Low-angle tracking na subida, Zoom de FOV na preparação angelical, Over-the-shoulder no rasante e Screen Shake de impacto na aterrissagem).
   - Acoplar `ComputeFovModifierEvent` para efeitos de punch/recuo em impactos e letterbox bars para apresentação 21:9.
4. **Sincronização PlayerAnimator + GeckoLib**:
   - A postura de combate (Stance) do jogador deve utilizar máscara parcial de ossos (Bone Masking) mantendo pernas livres para locomoção, enquanto o GeckoLib executa o ciclo respiratório/flutuante das asas alinhado ao torso.

## Inspeção Frame a Frame de Vídeo e Aprendizagem com Erros (Atualização 04/11/2026)
1. **Pipeline de Ingestão de Vídeo**:
   - Vídeos de testes enviados pelo usuário (`.mp4`, `.webm`, `.mkv` de OBS, Xbox Game Bar ou upload) são inspecionados via `tools/scripts/video_inspector.py`.
   - Extração automática de frames a 2-10 FPS com registro de timestamp no nome de cada frame (`frame_001_00.50s.png`).
   - Geração de Storyboards (folhas de contato / contact sheets em grade) com carimbo de tempo para visualização de arcos de movimento completos em uma única imagem.
2. **Matriz de Aprendizagem de Erros Visuais**:
   - Cada defeito visual identificado (ex: "leque de varetas", "vão dorsal", "assimetria coronal", "kissaki desalinhado", "clipping de pernas") deve ser registrado com sua causa matemática/geométrica no script de modelagem procedural ou no renderizador Java.
   - Antes de dar uma alteração como concluída, comparar os frames antes/depois sob critérios estritos de sobreposição de malha, alinhamento esquelético e silhueta em jogo.

