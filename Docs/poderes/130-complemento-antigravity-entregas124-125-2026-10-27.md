# 130 — Complemento aos retornos Antigravity

27/10/2026. Encaminhamento proposto ao usuário; nenhuma mensagem enviada a outras conversas.

## Conversa Senbon

Continue124 a partir do estado efetivo, sem declarar concluídas fases apenas capturadas. Leia129 antes de sincronizar: Codex corrigiu localmente colisão AURA/melee com shape12/protocolo2.36, retirou início local concorrente e corrigiu dupla inversão do canhoto. Não sobrescrever essas mudanças, não presumir que estão commitadas e não fazer merge com checkout sujo sem coordenação.

Prioridade1: compartilhar relógio/progresso real Better Combat no rastro e aproximar posição/direção da lâmina da mesma coreografia dos assets, com yaw/pitch e canhoto corretos. SWING_DURATION14 e trajetórias locais independentes não provam sincronismo. Comparar render da espada versus rastro em marcos de cada golpe e duas velocidades reais suportadas. Medir latência do evento autoritativo local/observador; se introduzir predição, usar ID estável da ação para não reiniciar ao confirmar. Não reutilizar AURA ou alterar mecânica para facilitar.

Prioridade2: implementar o novo visual do Hakuteiken especificado124. Os commits entregues não mudam constructos/efeitos desse ataque; capturar efeito existente não substitui refino. Mostrar diff e antes/depois na mesma cena: concentração na lâmina, asas legíveis, impulso, rastro no avanço, parada em parede e dissipação. Nada de dano visual à frente de colisão real.

Prioridade3: passe visual Jardim/Senkei/cortes/Shikai conforme124, cada habilidade em diff e evidência separados. Preservar alcance e comportamento. Check/build e GameTests pertinentes com comandos/exit/resultados; cliente real para aceite visual. Não declarar matriz multiplayer concluída por imagens solo.

## Conversa Hyōrinmaru/Higashi

Revisão129 reconhece os diffs99fbcc0/1e7aefc/0675778, mas estes ainda não estão integrados na Feature. Antes de ampliar, entregar QA gráfico do modelo/mandíbula/sopro e execução. Sem MCP Blockbench é válido usar geradores, informando corretamente o método; não chamar isso de teste no editor.

Mostrar três bocas em abrir/sustentar/fechar com alinhamento do emissor durante movimento, pescoços em flexão e condições de terreno. Demonstrar a câmera Higashi com parede dos dois lados e corredor: a troca instantânea de flanco pode produzir corte180graus; avaliar e estabilizar conforme evidência. Comprovar golpe sem execução em miss/survive e execução em threshold/lethal confirmado, mais pausa40ticks e retorno após cinzas. Totem/aliado/cancelamento não podem executar.

Check/build não executa automaticamente runGameTestServer: fornecer comando e relatório do servidor se alegar GameTests aprovados. Evidência de vídeo/frames reais e limites explícitos. Refino asas/cauda e demais fases125 só encerrado quando houver diff pertinente e prova, não por resumo amplo. Não alterar a transformação Ryūjin aprovada nem Senbon.
