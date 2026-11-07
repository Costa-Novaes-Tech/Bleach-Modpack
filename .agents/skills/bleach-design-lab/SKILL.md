---
name: bleach-design-lab
description: Modelagem e animação do Bleach Mod no Blockbench pela ponte MCP, integração GeckoLib e QA no checkout separado de design do Antigravity.
---

# Laboratório de design Bleach

Leia o GEMINI.md efetivo e a continuidade atual; o onboarding de 22/10 é histórico. Caminhos, branches e escopos do prompt humano vigente prevalecem sobre aquele checkout antigo. Acervo: use as skills globais acervo-ia e eficiencia-memoria-ia já disponíveis; fontes preservadas em C:/Users/joaop/Documents/Skills para IA. Consulte apenas entradas pertinentes do catálogo. Fonte presente não implica MCP instalado. OmniRoute não foi validado neste host; não mude o modelo por economia.

## Autoria
1. `git status --short --branch`, diff dos arquivos e contratos de Docs/poderes. Capture baseline de bones/pivôs/UV/escala; conceito é referência, não asset utilizável automaticamente.
2. Confirme a ponte Blockbench exposta na sessão, projeto, formato e instância própria antes de mutar. Use get_status/list_outliner e load_project da cópia da feature atual; `ag_hydra_jaw.bbmodel` era um exercício histórico. Preserve a instância Codex/8788; Antigravity usa sua própria instância. Nunca execute_script.
3. Defina pivôs e preparação/execução/retorno próprios do modelo atual. O exemplo de mandíbula é apenas um caso: não aplica sua anatomia ao Quincy. Use medidas/check_model/check_rig conforme o fenômeno; inspecione as vistas e o movimento, sem expor base64 na conversa.
4. Salve cópia ag_ no laboratório. Nome de bone é contrato; preserve original. Novo modelo não deve ser só cubo genérico sem rig/UV.
5. Revalide o plugin e codec da instância atual; a ausência de GeckoLib em 22/10 não descreve automaticamente o estado presente. Não renomeie JSON genérico como geo.json nem chame um gerador Python de exportação nativa. Confira formato/conteúdo no disco e reproduza esses arquivos no consumidor real.

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
   - Escolha difusa/emissão pelo material e renderer reais; atlas duplo não é obrigatório para todo VFX. Sufixo `_glow` sozinho não instala uma camada. `FULL_BRIGHT` não remove necessariamente a luz direcional das faces nem ilumina blocos: na Ryūjin 1.20.1, foi necessário um shader de material próprio. Confira descarte alfa, profundidade e saturação em jogo; consulte a lição v13 em `bleach-model-pipeline` antes de copiar um RenderType.
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
   - Para inspeção local, `tools/media_qa/inspect_media.py` foi exercitado nas referências BleachCraft e no framebuffer Ryūjin: registra streams/hash, extrai tempos escolhidos e produz contato QA. Outros inspetores só devem ser citados após confirmar sua existência/interface no checkout.
   - Extração automática de frames a 2-10 FPS com registro de timestamp no nome de cada frame (`frame_001_00.50s.png`).
   - Contatos QA com tempos permitem localizar mudanças entre amostras; não comprovam todos os frames de um arco ou fluidez contínua. Referências de modelagem continuam individuais por ângulo/frame. Não avaliar áudio quando só imagens ou vídeo silencioso foram acessados.
2. **Matriz de Aprendizagem de Erros Visuais**:
   - Registrar sintoma, hipóteses e teste discriminante para cada defeito; só declarar causa matemática/geométrica após sustentá-la com evidência. Material, shader, câmera ou rig podem produzir sintomas parecidos. O caderno de testes e limites reutilizáveis está em `Docs/poderes/guia-transformacoes-modelagem-vfx-qa-2026-11-06.md`.
   - Antes de dar uma alteração como concluída, comparar os frames antes/depois sob critérios estritos de sobreposição de malha, alinhamento esquelético e silhueta em jogo.

