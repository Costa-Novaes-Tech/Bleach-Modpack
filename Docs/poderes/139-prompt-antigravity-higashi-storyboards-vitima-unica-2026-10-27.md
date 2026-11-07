# 139 — Prompt Antigravity: Higashi por quadros, vítima única

Continue na branch existente codex/ryujin-bankai-sol-selamento. O usuário confirmou: TODAS as execuções do vídeo092434 foram por dano normal letal, nenhum alvo estava abaixo7%. Esse caso é a prioridade. A entrega atual ainda mostra cadáver vermelho+representação para cinzas e katana inadequada. Não considerar isso resolvido por ter criado uma réplica.

Regras de trabalho: confira Git/status/branch antes de editar. Você não está sozinho; preserve mudanças de terceiros. NÃO FAZER MERGE nesta rodada, nem main, push ou instalação de novas dependências. Commit focado apenas conforme autorização vigente da sua conversa. Não trocar checkout de outra conversa nem sobrescrever documentos. Todos relatórios e evidências em Docs/poderes. Teste de código não aprova poses; execute cliente real ou declare limite.

Leia137 e quadros HG-F00..F08/ticksT000..T068 em media/137-storyboards. O HTML é referência esquemática, sem garantia de rig/renderer: converter para modelo real e comparar capturas, não copiar offsets.

Fase1 — linha do tempo da morte. Registrar instante de dano, resultado letal, pacote de execução, evento3/60, remoção original, criação/cancelamento réplica e render. Reprodução com Hollow alado semelhante ao vídeo e mob vanilla. Explicar onde surgem duas vítimas. Só isExecuting no momento do pacote não impede evento anterior. Não ocultar todas entidades nem atrasar morte globalmente.

Definir UM proprietário da apresentação por UUID/ID estável+sessão da execução. Antes de desenhar réplica, original deve estar inequivocamente suprimido nos passes base, camadas, fogo e sombra pertinentes, desde que se tenha confirmação suficiente. Não desenhar original+réplica nem um frame e depois outro. Remoção da entidade não pode revelar cadáver vermelho. Não ocultar por ID reciclado noutra entidade após limpeza.

Aparecimento deve preservar identidade e variante: tipo, escala, dados visuais sincronizados necessários, asas, skin, equipamento e pose. create(type)+equipment não equivale a snapshot completo de criatura customizada. Clonar só dados visuais permitidos, sem capability/inventário autoritativo/efeitos de gameplay. Se renderer não suportar representação segura, fallback declarado, sem trocar alado por humanoide aleatório. Escolher captura de aparência a tempo, não presumir que a entidade ainda existe quando pacote chegar.

Fase2 — replay com katana. Após letalidade confirmada, HG-F00 câmera lateral e vítima única; F01recolha no quadril; F02preparação baixa; F03corte ascendente real; F04fim alto. Mão/cabo fixos, braço e corpo coordenados. Mostrar SEM VFX. Espada não fica baixa enquanto braço sobe nem atravessa cabeça/coxa. Não basta adicionar faísca ao vetor matemático se item renderizado não o segue.

Verificar canal de item/rotação/display e prioridade em HigashiExecutionVisuals, sem modificar transformglobal usado pela transformaçãoRyūjin. Replay não reaplica dano, não duplica executor e não reposiciona física. Vítima real morta no servidor; apresentação só cliente. Provar drops/XP uma vez, incluindo morte normal acima7%, totem/cancelamento e sobrevivente sem execução.

Fase3 — pausa e dissolução conforme HG. Fim corte16, pausa16→36, ignição36, fade36→48, cinzas48→68 e retorno. Durante pausa, uma única vítima, sem queda vermelha/fogo prematuro. Corpo escurece e some uma vez; não reaparece nem perde asas. Volume de cinza acompanha alvo em vez de painel plano. Compare HG-F05/F06/F07 com cliente. No HG-F08, câmera/HUD/FOV/controle restaurados e nenhuma cópia órfã.

Para renderizadores complexos, não substituir indiscriminadamente textura de TODAS as camadas pela textura base ao aplicar alpha (armadura, asas, olhos e materiais podem ter texturas próprias). Preservar RenderType/material apropriado ou fallback explícito. Não declarar suporte universal por teste em esqueleto. Se usar referência do original como fallback de player, demonstrar ausência de duplicação e manipulação vazando a outros passes.

Fase4 — composição. Câmera lateral mostra preparação e arco inteiro com ambos em quadro; flanco estável, sem giro180º. Em parede, impedir clipping; se alvo aéreo/jogador saltando, apresentar posições verdadeiras e apoio quando existe, sem congelar ambos em altura inventada. Testar retorno/interrupção e observador sem sequestro de câmera.

Fase5 — evidências/entrega. Vídeo contínuo dos casos letais normais (alado e vanilla), threshold adicional, sobreviver/miss/totem/aliado, dia e corredor. Registrar cada HG-Fxx em captura real versus esquema; medir pausa20ticks no relógio da apresentação. check/regressionTest e runGameTestServer pertinente com comandos/exits; GameTests não aprovam cena. Declarar não testados. Primeiro vítimaúnica, depois coreografia, depois efeitos; commits focados. Não modificar Hyōrinmaru aprovada, Senbon, C Ryūjin ou transformação. SEM MERGE: entregar para auditoria, não integrar automaticamente.
