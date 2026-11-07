# Ryūjin v9 — referência, pose lateral e laboratório visual

> Relógio de 12 s abaixo preservado como histórico. Pedido posterior amplia a sustentação do sol: [v10 — 15 s, com 3,2 s de sol completo](ryujin-v10-duracao-sustentacao-sol-2026-11-05.md). As imagens v9 não são provas da duração nova.

**05/11/2026 — `Feature-Poderes-bankais`, base `9eb6a05`.** Trabalho local em andamento, sem commit/merge/push. Autorização do usuário: todos os blocos de qualidade, incluindo acabamento e perfil gráfico, com instalações de mods/dependências em laboratório e registro de conflitos. Ordem solicitada: **2 (shader) → 3 (VFX) → 4 (cosméticos) → 1 (arqueiro)**. Não alterar a missão Rukia, outros worktrees ou saves pessoais.

## Direção vigente

A referência fornecida é de composição e energia: rosto frontal sombreado pelas chamas, katana baixa no flanco de empunhadura, lâmina para fora, fogo variável até a cintura e grande massa atrás. Substitui, nesta revisão, a proposta anterior de invocação com ponta para cima. O jogador conserva sua skin; o manequim e o uniforme das imagens são referências de volume, não uma troca obrigatória por Yamamoto.

Sequência: preparação curta → pose firme/close frontal → fogo na lâmina e base irregular → explosão/campo traseiro → término da fala “Bankai” ainda próximo → recuo frontal elevado para mostrar o sol inteiro → giro acelerado/absorção na katana → plano médio da lâmina carbonizada → retorno ao controle.

Não implementar rachaduras do solo, dano, blocos de fogo, poderes novos ou destruição sugeridos incidentalmente por imagens. Sombreamento do ator é uma solução local de apresentação; não equivale a sombra física nem a luz dinâmica sobre o mundo.

## Relógio compartilhado

| Marco | Tick real / segundo | Composição |
|---|---|---|
| F1 | 28 / 1,4 s | Frontal próximo, corpo/arma legíveis, início de calor. |
| F2 | 48 / 2,4 s | Fala iniciada no tick 40; fogo variável até a cintura, rosto sombreado. |
| F3 | 84 / 4,2 s | Explosão disparada no tick 70; câmera começa a abrir mantendo o eixo frontal. |
| F4 | 112 / 5,6 s | Sol inteiro, margem para a coroa, jogador e campo de chamas abaixo. |
| F5 | 130 / 6,5 s | Absorção 116–142, rotação e fluxos convergindo na lâmina. |
| F6 | 160 / 8 s | Sol extinto, lâmina carbonizada com fio de brasa, fala do nome. |
| Retorno | 210–240 / 10,5–12 s | Câmera devolvida sem travar jogador. |

Contrato de gameplay preservado: release ocupado até 60, commit/debito único no tick 40. O relógio autoral remapeado não deve ser confundido com segundos nem com ticks reais.

Parâmetros atuais de câmera, relativos aos pés do ator e à sua frente horizontal: abertura `(frente 7,8; altura 2,5)`, FOV 58°; fala `(4,3; 2,65)`, foco em altura 1,65 e FOV 48°; panorama `(14,2; 9,0)`, foco em altura 6,8 deslocado 2,2 para trás e FOV 80°; selamento `(7,2; 3,4)`, FOV 60°; detalhe `(3,8; 2,35)`, FOV 48°. São números do código, sujeitos à proteção de colisão, e não medidas inferidas das imagens. A altura frontal varia aproximadamente entre 0,21 e 0,99 bloco multiplicada pelo envelope de ignição; nunca deve ocultar o rosto.

## Imagens de referência — cada arquivo contém uma vista ou um frame

Diretório: [`media/ryujin-model-frames-20261105`](media/ryujin-model-frames-20261105). Origem, prompts e seleção: [manifesto](media/ryujin-model-frames-20261105/manifest.json). São **imagens geradas por IA**, não capturas do mod. As dimensões de pixels não estabelecem medidas de rig.

| Vista da pose R0 | Limpa/lisa | Em blocos |
|---|---|---|
| Frente | [Frente lisa](media/ryujin-model-frames-20261105/R0-front-clean.png) | [Frente em blocos](media/ryujin-model-frames-20261105/R0-front-block.png) |
| Costas | [Costas lisas](media/ryujin-model-frames-20261105/R0-back-clean.png) | [Costas em blocos](media/ryujin-model-frames-20261105/R0-back-block.png) |
| Perfil esquerdo | [Perfil liso](media/ryujin-model-frames-20261105/R0-left-clean.png) | Usar [perfil do modelo nativo](media/ryujin-model-frames-20261105/native-pose-left.png). |
| Perfil direito | [Perfil liso](media/ryujin-model-frames-20261105/R0-right-clean.png) | Usar [perfil do modelo nativo](media/ryujin-model-frames-20261105/native-pose-right.png). |
| 3/4 | [3/4 liso](media/ryujin-model-frames-20261105/R0-threequarter-clean.png) | [3/4 em blocos](media/ryujin-model-frames-20261105/R0-threequarter-block.png) |

Os dois perfis gerados em blocos foram **rejeitados**: reposicionaram a lâmina ao tentar representar o encurtamento em perspectiva. Permanecem arquivados com `selected=false`; a orientação deve ser conferida no modelo nativo. Não tentar acomodar uma ilusão de perspectiva mudando a pose 3D.

| Frame | Imagem individual principal |
|---|---|
| F1 | [Close / aquecimento](media/ryujin-model-frames-20261105/F1-front-close-preheat.png) |
| F2 | [Close / chamas até a cintura](media/ryujin-model-frames-20261105/F2-front-close-waist-fire.png) |
| F3 | [Explosão traseira e abertura](media/ryujin-model-frames-20261105/F3-front-opening-explosion.png) |
| F4 | [Sol completo — revisão v2](media/ryujin-model-frames-20261105/F4-front-wide-sun-v2.png) |
| F5 | [Absorção em rotação](media/ryujin-model-frames-20261105/F5-front-wide-absorption.png) |
| F6 | [Katana selada — revisão v2](media/ryujin-model-frames-20261105/F6-front-medium-sealed-v2.png) |

F2 também possui [perfil direito](media/ryujin-model-frames-20261105/F2-right-profile-fire.png) e [costas](media/ryujin-model-frames-20261105/F2-back-fire.png), para estudar profundidade. F4 original foi rejeitado por cortar a coroa; F6 original, por manter fogo demais na lâmina. As versões v2 são as referências selecionadas. Não há montagem de vários ângulos dentro de uma imagem.

## Blockbench nativo

Ponte **Codex/8788**, Blockbench 5.2.2, formato GeckoLib, `execute_script` desativado. Instância Antigravity/8787 preservada. O plugin já instalado foi reutilizado; não houve reinstalação nem nova concessão de rede nesta etapa.

- Cena editável: [`ryujin_bankai_flame_wall_v9_lab.bbmodel`](../../tools/models/lab/codex/ryujin01/ryujin_bankai_flame_wall_v9_lab.bbmodel), derivada da v7 sem sobrescrevê-la: **244 cubos, 66 bones, 12 segundos**, atlas existente e 12 grupos de chama traseira com alturas/defasagens distintas.
- Projeto separado de pose: [`..._pose.bbmodel`](../../tools/models/lab/codex/ryujin01/ryujin_bankai_flame_wall_v9_lab_pose.bbmodel), 19 cubos/21 bones. Mantém o mesmo rig e retira cenografia para inspecionar mãos e arma.
- Geometria exportada pelo codec `bedrock`: [`...geo.json`](../../tools/models/lab/codex/ryujin01/ryujin_bankai_flame_wall_v9_lab.geo.json). É export de laboratório, não foi copiada para recursos do mod nem validada como novo asset runtime.
- A repetição do export após refinar keyframes expirou no MCP (61 s). A geometria não mudou nesse refinamento; o arquivo exportado anterior permanece válido como geometria. A animação atual está no `.bbmodel`; não declarar um `.animation.json` final exportado/testado nesta rodada.
- [Gerador reproduzível](../../tools/models/lab/codex/ryujin01/build_scene_v9.py). Preparação/retorno curtos com Catmull-Rom; sustentação estável, pés fixos. O primeiro ensaio estático gerou `no_visible_motion`; após corrigir a preparação, a análise apontou `all_linear`, corrigido nos canais do corpo. **`analyze_animation`: 0 problemas; `check_model`: 0 problemas** no modelo final.
- Orientação medida: frente **−Z**, direita do ator **+X**, chamas atrás **+Z**. Vista frontal espelha a direita do ator para a esquerda da imagem.

Capturas de pose do editor, uma por arquivo: [frente](media/ryujin-model-frames-20261105/native-pose-front.png), [direita](media/ryujin-model-frames-20261105/native-pose-right.png), [esquerda](media/ryujin-model-frames-20261105/native-pose-left.png), [costas](media/ryujin-model-frames-20261105/native-pose-back.png), [3/4](media/ryujin-model-frames-20261105/native-pose-threequarter.png). Foram capturadas durante a sustentação, antes do refinamento de interpolação de preparação/retorno; a pose sustentada não mudou.

Capturas frontais de cenografia do primeiro ensaio: [t28](media/ryujin-model-frames-20261105/native-F1-t28.png), [t48](media/ryujin-model-frames-20261105/native-F2-t48.png), [t84](media/ryujin-model-frames-20261105/native-F3-t84.png), [t112](media/ryujin-model-frames-20261105/native-F4-t112.png), [t130](media/ryujin-model-frames-20261105/native-F5-t130.png), [t160](media/ryujin-model-frames-20261105/native-F6-t160.png). O enquadramento é de laboratório com grandes margens; não reproduz os closes do diretor de câmera do jogo. As tiras de chama são placeholders de volume, sem pós-processamento físico.

## Mudanças no runtime e resultados reais

Pose de invocação lateral espelhada para canhotos; fogo de lâmina no aquecimento; frente de fogo com altura temporal variável; sombra local no ator; close mantido pela fala e recuo para revelar o sol. Limites existentes de partículas/cards e autoridade do servidor preservados. A nova câmera inicialmente falhou no teste de continuidade; a curva foi corrigida com velocidade contínua e limitada, **sem afrouxar o teste**.

| Verificação | Resultado / evidência local |
|---|---|
| `check` | **176 regressões aprovadas**; `build/visual-lab/v9-validation-final.log`. |
| GameTests em mundo descartável | **295/295 obrigatórios**, exit 0, `BUILD SUCCESSFUL`; mesmo log. |
| `build runClient`, caso `day_back` | **Exit 0**, `BUILD SUCCESSFUL`; `build/visual-lab/v9-client.log`, cena termina com `releaseBusy=false`, `scene={}`, `form=bankai`. |
| Capturas Forge | `run-benihime-qa/base/ryujin-bankai-qa/cycle118-1793924751588/01_day_back`; frames e `frames.jsonl`. |
| Dedicado / dois clientes / áudio escutado | **Não executados nesta revisão**. |

O init script redirecionou o GameTest, mas o ForgeGradle preservou o diretório do runClient configurado em `build.gradle`; a execução real ficou em `run-benihime-qa/base`, criando uma nova sessão QA. Não afirmar isolamento no diretório `build` com base apenas no script; os saves pessoais não foram usados. Corrigir a configuração de run antes de novas execuções que precisem de diretório próprio.

Inspeção visual dos frames t50/t112/t160: espada lateral e close frontal corretos; sol aparece inteiro atrás; controle retorna. **Acabamento pendente:** fogo em cartões ainda separado/repetitivo, campo traseiro pouco contínuo e algumas partículas persistem depois do selamento. O ensaio não é aceite artístico. Os FPS do JSON variam durante aquecimento (inclusive amostras 0); não são medição de p95 nem comparativo de desempenho. Vídeo contínuo com áudio e matriz canhoto/slim/noite/parede/observador ainda necessários.

## Instalações opcionais em perfis separados

Ferramenta local: [`tools/visual_lab/prepare_lab.py`](../../tools/visual_lab/prepare_lab.py). Forge **47.4.10 / Minecraft 1.20.1**, Java 17 existente. Runtime, dependências Python, bibliotecas, downloads, configurações e mundos ficam em `build/visual-lab`; nenhum perfil pessoal ou dependência obrigatória Gradle foi alterado. Identidade de desenvolvimento usada somente para cliente local, sem contas/tokens reais ou multiplayer externo.

| Perfil | Pacotes preparados | Estado nesta rodada |
|---|---|---|
| `base` | Bleach + GeckoLib 4.8.4, PlayerAnimator 1.0.2-rc1, Better Combat 1.9.0, Cloth Config 11.1.136 | Forge empacotado abriu mundo, mas interação ficou inconclusiva sob carga; não confundir com o QA userdev concluído acima. |
| `shader` | Embeddium 0.3.31, Oculus 1.8.0, Complementary Reimagined r5.9.3 | Log confirma shader carregado. Mundo demorou excessivamente; **comparativo/FPS pendente**, sem promoção ao pack estável. |
| `photon` | Photon 1.1.17 + LDLib 1.0.42 | Arquivos instalados no perfil; nova explosão solar/export equivalente ainda não autorados. O piloto existente de cinzas Higashi não equivale a este efeito. |
| `aaa` | AAA Particles 2.3.4 Forge | Arquivo oficial instalado no perfil; autoria/export Effekseer e A/B ainda pendentes. |
| `cosmetics` | Skin Layers 3D 1.11.4 + Not Enough Animations 1.12.7 | Arquivos preparados; resultado de carregamento/compatibilidade registrado na atualização abaixo. O erro `TRansition` anterior ocorreu no userdev e não prova conflito em produção. |

Atualização de carregamento: **cosméticos e Photon/LDLib chegaram ao menu** no Forge empacotado, em execuções separadas, sem o erro de remapeamento `TRansition` do userdev. Capturas: [cosméticos](media/ryujin-model-frames-20261105/forge-cosmetics-menu.jpg) e [Photon](media/ryujin-model-frames-20261105/forge-photon-menu.jpg). Isso não aprova a convivência em cena nem a versão solar de um efeito. AAA inicializou os framebuffers e o gerenciador Effekseer; confirmação visual do menu registrada ao encerrar o ensaio. Os avisos de sessão/Realms são esperados com identidade de teste local; não houve uso de multiplayer nem credenciais reais.

**AAA também chegou ao menu**: [captura individual](media/ryujin-model-frames-20261105/forge-aaa-menu.jpg), 10 mods reportados na tela. Cliente de cada ensaio encerrado antes do seguinte; nenhum cliente Minecraft de teste permaneceu aberto ao concluir esta etapa. Foram somente testes de carregamento desses três perfis; a implementação de explosões equivalentes e a avaliação dentro do mundo continuam abertas.

Frames reais selecionados para comparação com a referência: [close t50](media/ryujin-model-frames-20261105/forge-t050.png), [explosão t84](media/ryujin-model-frames-20261105/forge-t084.png), [sol inteiro t112](media/ryujin-model-frames-20261105/forge-t112.png), [absorção t130](media/ryujin-model-frames-20261105/forge-t130.png), [selamento t160](media/ryujin-model-frames-20261105/forge-t160.png) e [controle t240](media/ryujin-model-frames-20261105/forge-t240.png). Fonte e SHA-256 no manifesto. Alguns frames mostram transparência esparsa e fogo residual; preservar essas evidências ao planejar o próximo efeito.

Proveniência e hashes ficam em `build/visual-lab/artifacts.json` e `profile-manifest.json` de cada perfil. Modrinth: hash SHA-512 conferido contra metadados do publicador. AAA foi obtido do [arquivo oficial CurseForge 9239318](https://www.curseforge.com/minecraft/mc-mods/aaa-particles/files/9239318), SHA-256 local `6dbb0f581a86a96690f9c03f19bf9ceaa090d272136cae4e4b6cef8f5b47b453`; não alegar verificação independente de digest do publicador para esse arquivo. Instalação local não decide redistribuição: preservar os gates de licença do [plano do stack](plano-stack-visual-animacao-camera-shaders-2026-11-05.md), sobretudo Photon e shaderpack.

## Próximos gates, na ordem autorizada

1. **Ponto 2:** repetir base/Embeddium/Oculus sem shader/com shader no mesmo cenário plano, após estabilizar responsividade; capturar vídeo e frametime após aquecimento. Por enquanto, lentidão fica registrada para investigação, sem atribuí-la a um mod específico.
2. **Ponto 3:** construir um efeito curto equivalente de campo/explosão no Photon e no Effekseer/AAA; mesma âncora, duração, escala e seed. Testar cancelamento, reload e observador; promover no máximo um backend por evento. Sem fallback silencioso para dois efeitos simultâneos.
3. **Ponto 4:** testar slim/padrão, mão direita/esquerda, camadas 3D e posse dos braços entre NEA, PlayerAnimator e Better Combat em Forge empacotado.
4. **Ponto 1:** arqueiro Quincy por último; reaproveitar padrões aprovados, não a coreografia/paleta solar. Memória XERJ preservada: preferência GPL-3.0, escopo/atribuição a esclarecer, nenhuma autorização MIT nem mensagem externa presumida.

P0 de dados/saves e as propostas de progressão/mentores permanecem no roadmap, sem migrações nesta frente visual.
