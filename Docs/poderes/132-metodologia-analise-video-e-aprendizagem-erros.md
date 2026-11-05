# 132 — Metodologia de Análise de Vídeo Frame a Frame e Aprendizagem com Erros

Data: 04/11/2026
Branch: `codex/senbon-hakuteiken-blockbench`
Autor: Antigravity Design Lab

## 1. Contexto e Objetivo
Para elevar o padrão de QA visual e cinematográfico do Bleach Mod a um nível profissional, o usuário autorizou expressamente a capacitação do assistente para ingestão e dissecação de gravações em vídeo (`.mp4`, `.webm`, etc.) de testes in-game (capturados via OBS, Windows Game Bar ou gravações de tela).

Esta metodologia estabelece a ponte entre gravações reais de gameplay, extração analítica de quadros e aprendizagem sistemática com defeitos geométricos/visuais observados.

---

## 2. Ferramentas Integradas e Fontes do Acervo

1. **Repositório Base no Acervo**:
   - `C:\Users\joaop\Documents\Skills para IA\01-Repositorios-GitHub\02-design-midia\claude-video-main`:
     - Motor `watch` com suporte multimodal (Gemini) e local (ffmpeg / whisperx).
2. **Utilitário Local Criado**:
   - `tools/scripts/video_inspector.py`:
     - Integração direta com binário estático `ffmpeg 9.0.2` via `imageio-ffmpeg` e manipulação de imagem com `Pillow 12.2.0`.
     - Subcomandos:
       - `info <video>`: Inspeciona resolução, duração, fps e codecs do vídeo.
       - `extract <video> --fps <rate> --max-frames <N> --storyboard <out.png>`: Extrai quadros cronometrados com timestamps decimais e gera folha de contato (storyboard) anotada.

---

## 3. Matriz de Aprendizagem de Erros Visuais (Catálogo de Defeitos & Soluções)

| Defeito Observado | Sintoma Visual | Causa Raiz Geométrica / Código | Solução Aprendida & Padrão Permanente |
| :--- | :--- | :--- | :--- |
| **"Leque de Varetas" / Pente de Costelas** | Asas parecem um leque de palitos espaçados, com vãos de ar vazios entre as penas. | Penas modeladas como caixas estreitas isoladas (`wx` pequeno ~2.0), com espaçamento angular radial que diverge nas pontas sem sobreposição. | Penas devem ter largura planar ampla (`wx` 3.8 a 4.6) e escalonamento em camadas $Z$ (sobreposição de 30% a 40%), formando uma cobertura de plumagem contínua e sem fendas. |
| **"Vão / Espaço Dorsal"** | Asas parecem flutuar desacopladas das costas do jogador, deixando um vazio de ar. | Offset em $Z$ muito alto ou centro de gravidade do nexo fora da coluna vertebral ($Z > 3.0$). | Nexo dorsal (`wing_nexus`) assentado como elytra diretamente contra as costas no plano $Z = 2.02..2.16$, centralizado no osso torácico. |
| **Assimetria Coronal / Torção Diagonal** | Asas parecem tortas quando vistas de cima (vista aérea), uma asa avançada e outra recuada. | Pivotagem esquelética assimétrica ou rotações em $Y$ aplicadas com sinais incorretos entre lados esquerdo e direito. | Espelhamento rigoroso com pivotagem em $X = 0.00$ e rotações no plano coronal rigorosamente simétricas ($Left = -Right$). |
| **Kissaki (Lâmina) Desalinhado** | Katana na postura Chūdan-no-kamae aponta para cima e para fora do alvo em vez da linha de visão frontal. | Rotações `rx` e `rz` do `mainItem` excessivas no blend de animação do `PlayerAnimator`. | Calibrar `rx` para $\approx -0.05F$ e `rz` próximo a zero, garantindo que o kissaki aponte rigorosamente ao longo de $-Z$ contra o oponente. |
| **Mochilão / Alças Cervicais Clunky** | Cubos pesados cinzas cobrindo a nuca e os ombros do jogador, destruindo o visual de shinigami. | Modelagem do nexo usando blocos de arnês/mochila em vez de emanação espiritual pura de reishi. | Nuca e ombros devem permanecer 100% livres; emissão pura de reishi emanando diretamente da fenda escapular da coluna. |

---

## 4. Fluxo de QA por Vídeo

1. **Ingestão**: Ao receber um vídeo `.mp4` na conversa ou no diretório do projeto:
   ```powershell
   python tools/scripts/video_inspector.py extract "<caminho_do_video>" --fps 4.0 --storyboard "tools/models/lab/video_qa/storyboard.png"
   ```
2. **Inspeção de Contato (Storyboard)**:
   - Abertura da folha de contato via `read_file` para avaliar arcos de transição, timing de poses e interpolação.
3. **Dissecação Crítica Frame a Frame**:
   - Leitura direta dos frames específicos nos pontos de transição (ex: decolagem, ápice do voo, impacto rasante).
4. **Verificação contra a Matriz**:
   - Cada um dos critérios da Matriz de Erros é auditado (sobreposição de penas, ausência de vão dorsal, simetria coronal, alinhamento de armas).
