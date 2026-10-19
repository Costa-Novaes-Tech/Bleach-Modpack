# Matriz de homologação no Minecraft — Bankai da Benihime

## Contrato a derivar da implementação atual
Criar tabela de marcos `{fase, tempo/tick, gatilho, player, Benihime, cortina, câmera, som/VFX, ativação efetiva, desbloqueio}`. Não preencher tempos arbitrariamente: extrair do plano existente e da implementação; alinhar pontos à lógica de jogo (ticks) e à renderização interpolada (partial ticks). Preserve as regras Benihime, sem copiá-las da Zaraki.

## Cenários prioritários
| ID | Cena / teste | O que comprovar em Minecraft real | Evidência |
|---|---|---|---|
| B01 | Cortina fechada e elevação | Nada da manifestação visível antes do marco; oclusão sem fresta | PNG frente e lateral, vídeo |
| B02 | Mão encontra cortina | Alinhamento da palma/punho, rotação de dedos, sem clipping | Sequência de quadros ou vídeo curto |
| B03 | Cortina abre | Bordas/dobras, tempo correto, sem atravessar malha, sem transparência antecipada | Vídeo + frames início/meio/fim |
| B04 | Primeiro apoio no chão | Mão firme no terreno; não patina; cotovelo e ombro coerentes | Vídeo lateral com câmera estável |
| B05 | Um avanço completo | Alternância de apoios e transferência de peso, quadril acompanha, sem deslizamento | Vídeo e logs de âncoras/ticks |
| B06 | Roupa/cabelos/dedos | Movimento secundário fluido sem penetrar braços ou piso | Vídeo frontal/lateral |
| B07 | Rosto e pose final | Expressão serena, rosto legível, transição sem estalo | PNGs iluminação controlada |
| B08 | Sincronização de cena | Jogador ajoelha/empunha espada; Benihime, câmera e som nos marcos corretos | Vídeo com áudio; eventos de log |
| B09 | Retorno ao controle | Câmera e controles restaurados; estado de Bankai no marco certo | Log de estados + vídeo |
| B10 | Terreno irregular | Mãos/chão, cortina e pés sem clipping; comportamento tolerável ou fallback | Vídeo em 3 alturas |
| B11 | Espaço apertado | Câmera não entra em bloco; ocultação/abort apropriado | PNGs + log de colisão |
| B12 | Dia/noite e luz mista | Interior escuro opaco, textura sem artefatos, VFX legível | Par de PNGs mesma câmera |
| B13 | Skins/observador | Sem deformar jogador, apresentação legível para terceira pessoa | Capturas do observador |
| B14 | Cancelamento/morte/dimensão | Sem estado preso, entidades órfãs, animação persistente ou controle travado | GameTest + vídeo quando gráfico |
| B15 | Server dedicado + 2 clientes | Sincronia da manifestação e marcos, ausência de crash/dependência cliente no servidor | Logs separados + vídeo 2 clientes |
| B16 | Base vs Photon/LDLib | Essencial funciona no base; piloto acrescenta só VFX opcionais | Duas execuções registradas |
| B17 | Desempenho | FPS/frametime antes/durante/depois, partículas e memória em perfil de vídeo idêntico | Medições registradas, não estimativas |

## Ordem de implementação
Primeiro B01–B05 no perfil base; não gastar esforço em VFX avançados antes de provar a mão na cortina e um avanço completo. Depois B06–B09, variações B10–B13, robustez B14–B16 e performance B17. Rever prioridades conforme riscos descobertos.

## Evidências e comparação
- Salvar PNGs sem redimensionar na pasta de QA, com nomes `Bxx-<perfil>-<iluminacao>-<perspectiva>-<marco>.png`. Guardar vídeos curtos `Bxx-<perfil>.mp4` quando houver captura autorizada.
- Para regressões visuais, fixar seed, clima, horário, resource packs, escala/FOV, resolução, distância/câmera, skin, versão do mod, lista de mods, settings gráficos e hardware. Documentar os pontos que não ficaram determinísticos.
- Cada cenário: `PASSOU`, `FALHOU` ou `PENDENTE`, passo a passo, esperado vs observado, evidência e link relativo.
- A nota 0–5 de contato/fluidez/câmera/VFX é julgamento de QA, não substitui vídeo nem critérios binários. Não declarar homólogo sem capturas in-game.
- Números de FPS e frametime exigem medição real, em perfil equivalente; nunca concluir 60 FPS com base somente em FPS médio de outra cena.

## Limitações importantes
- GameTest verifica mundo/servidor, não pixels do cliente.
- Preview do Blockbench verifica autoria, não iluminação e interpolação do Forge.
- Playwright cobre só visualizador web; sua saída não é evidência de Minecraft.
- Se não houver acesso automatizado ao desktop, o usuário deverá executar algumas ações de GUI ou compartilhar vídeo/captura. Registrar limitação.
