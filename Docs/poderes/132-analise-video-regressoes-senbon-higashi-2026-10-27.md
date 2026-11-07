# 132 — Regressões do vídeo: katana Senbon e Higashi

27/10/2026. Usuário reportou regressões após integração e corrigiu a prioridade do diagnóstico: o problema principal Senbon é a katana não executar o combo organizado, não a faixa de pétalas. Hyōrinmaru foi aprovada e deve permanecer congelada.

Estado inspecionado: Feature-Poderes-bankais limpa/sincronizada com tracking local, HEADfe239b7 (`debug logs`), descendente do merge a18bfc8 e registro3faf3d9. Não houve fetch, mudança de código, execução de cliente ou teste nesta análise. Não associar automaticamente o binário gravado ao HEAD sem hash do JAR.

Vídeo original: C:/Users/joaop/Downloads/Gravação/Gravação de Tela 2026-10-27 075349.mp4;265.684.651 bytes;4min12,18s;1920×1020;30fps. Hash em qa/cycle132/source.json. Original preservado, extração local offline com FFmpeg do cache existente; sem envio externo. Visão geral fps1/8 e trechos5fps, não inspeção individual de todos os quadros. Rótulos do overview são aproximados; não usar seu primeiro rótulo como timestamp exato. Trechos detalhados senbon5–11s e execução234–240s. Não houve escuta independente nem telemetria de inputs/dano.

## Senbon: prioridade corrigida pelo usuário

No trecho frontal inicial, especialmente aproximadamente5,2–6,6s, arma e faixa de pétalas não descrevem a mesma trajetória. Mas o aceite exigido NÃO é apenas alinhar a faixa: a própria katana deve cumprir a preparação, direção e recuperação dos três golpes. A gravação não oferece isolamento completo de cada input/índice; não concluir apenas pela captura qual controlador ou bone causou o defeito.

Primeiro reconstruir arma/mão/antebraço sem partículas, comparar silhuetas e transições, só depois rastro. O rastro manual em SenbonCombatVisuals continua sendo aproximação independente e não deve servir como substituto da animação real. Canais rightItem presentes no JSON não demonstram que o item renderizado os consome corretamente. Verificar controlador ativo, transforms do item e aplicação simultânea de camadas. [Prompt133](133-prompt-correcao-senbon-katana-combo-2026-10-27.md).

## Higashi

A sequência234–240s mostra aproximação em primeira pessoa, entrada lateral por volta235s, alvo com fogo vanilla visível235,2–235,8s, aparência ainda presente depois do fogo e substituição por partículas por volta237,4s. O corte ascendente e sua preparação não são legíveis como solicitado. O usuário relata também apresentação de morte comum antes da incineração; tratar isso como falha a reproduzir, não normalizar.

Inspeção local: accept inicia pose em now-IMPACT e troca a câmera após confirmação; preparação0..10 já ocorreu fora do enquadramento lateral. A silhueta usa referência da própria entidade morta e guarda/restaura deathTime/hurtTime/fire para render. Isso exige auditoria de ordem de pacotes, remoção, cancelamento do renderer e aplicação de fogo externa: não é prova isolada da causa de cada frame. Não alegar que merge causou todos os problemas; parte do desenho já era anterior.

Nova decisão explícita: pausa pós-corte passa de40 para20ticks,1segundo nominal. Corte termina em16; mantendo demais marcos, IGNITE=36 e END=68 se a cauda de32ticks permanecer. Atualizar consumidores do relógio, retenção da silhueta, partículas, câmera e testes, não só uma constante. [Prompt134](134-prompt-correcao-higashi-katana-morte-pausa1s-2026-10-27.md).

## Prioridade vigente

Correção de regressões antes de expandir kit/ultimate ou iniciar missões. Hyōrinmaru aprovada no recorte do usuário; preservar fontes/rig/baforada. Senbon/Higashi voltam a pendentes de aceite visual. Ryūjin transformação e Benihime continuam preservadas. Roadmap126 de missões permanece próximo horizonte, não cancelado. Estes prompts não são despacho ou implementação automática; usuário os encaminha às conversas existentes.
