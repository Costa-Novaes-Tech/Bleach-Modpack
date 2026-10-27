# 118 — Contrato comum para as frentes de combate

26/10/2026. Branch Codex `Feature-Poderes-bankais`, base58e3e4e. Este contrato complementa112–116; não declara os combos Benihime/Senbon homologados antes de suas provas no cliente.

## Diagnóstico confirmado e responsabilidade

- Better Combat é o controlador dos ataques básicos das cinco armas pilotadas. `SignatureActionVisuals` já exclui esses itens da geração de melee legado desde111. Portanto, não reintroduzir um segundo swing procedural para corrigir os assets customizados.
- `BetterCombatClientCompat` cancela preparação quando guarda, esquiva, técnica ou liberação exigem exclusividade. Não remover esse bloqueio para fazer um ataque aparecer.
- As técnicas recebem gesto autoritativo do servidor em SignatureActionVisuals/ElementalActionVisuals (camada3500), a liberação geral tem4000 e a entrada Ryūjin4500. Esses valores são prioridades locais do mod, não a prioridade alegada do Better Combat. `isActive` e limpeza são parte do contrato: estado final deve devolver as transformações recebidas, sem offset residual.
- Os seis assets customizados sem rightItem/leftItem diferem do nativo instalado. Ausência desses canais não impede herança do braço. Não há prova de um único offset global errado. Item, braço e display devem ser corrigidos por golpe/modelo quando necessário.
- Inspeção do Better Combat1.9.0 instalado: `CustomAnimationPlayer` deriva de `KeyframeAnimationPlayer` e muda o modo de primeira pessoa durante wind-down; `AttackAnimationSubStack` compõe modificadores de velocidade, espelhamento e ajuste. Não comparar o tick bruto do JSON com tempo real sem considerar velocidade do ataque. Inspeção local com javap; não é validação visual nem pesquisa de outra versão na internet.
- Inspeção do PlayerAnimator1.0.2-rc1 instalado: `HeldItemMixin.changeItemLocation` consulta ROTATION e POSITION de rightItem/leftItem no AnimationApplier ativo. POSITION é escalada por0,0625 (1/16) antes da aplicação. Isso confirma que esses canais são consumidos pelo renderer e que seus valores não são coordenadas mundiais. O mixin também aplica o bend do braço; uma âncora que ignora bend/display/corpo pode divergir da espada visível.
- **Rastro Senbon:** `BetterCombatCompat.comboChanged` envia um `SenbonPetalsS2C.ARC` com origem `player.position + (0,1.2,0)` e direção do olhar, no combo1. É um arco autoritativo fixo, não uma amostragem da ponta da espada. Isso explica a diferença de trajetória observada sem provar falha do rig. O emissor legado permanece por enquanto; a frente Senbon deve entregar o novo emissor ligado à arma e pedir ao Codex a remoção do emissor comum no mesmo conjunto integrado, evitando duplicação ou apagar o efeito sem substituto.

## Interface entre autores de assets e integração

1. Cada agente entrega um golpe completo primeiro: recurso efetivamente carregado, wind-up, passagem ativa, contato e recuperação. Não substituir o combo inteiro antes dessa prova.
2. Coordenadas dos JSONs são do PlayerAnimator/Better Combat, com unidade/flag degrees declaradas; não converter tudo para radianos sem conferir o recurso. O modelo e seu display acrescentam outra transformação. `rightItem/leftItem` são ajustes locais, não posição mundial da espada.
3. BC já usa espelhamento; não espelhar duas vezes uma animação destinada ao mesmo controlador. Registrar comportamento destro/canhoto e mão secundária ocupada; nosso mixin impede dual wield de duas armas vinculadas.
4. Coreografia própria pode ter canais do item, mas o punho não deve perder contato com a guarda. Não deslocar ombros por POSITION para compensar empunhadura sem diagnóstico. Comparar com Asauchi/Ryūjin/Hyorin preservadas.
5. Efeito preso à lâmina amostra a arma durante a passagem ativa; partículas já emitidas podem continuar no mundo. Projéteis deixam a mão no lançamento. Jardim, rede Shibari e demais áreas aprovadas fixas continuam fixas.
6. As funções `RyujinChoreography.bladeHand/bladeDirection` são aproximações do rig/modelo Ryūjin, com base de display específica. NÃO são uma API universal para os modelos Senbon/Benihime. A frente deve demonstrar a âncora no seu modelo; se exigir hook no renderer/controlador comum, entregar assinatura, ponto de captura e prova ao Codex antes de editar esses arquivos.
7. Arma inteira e punho precisam aparecer em capturas sem partículas e depois com efeito. Primeira e terceira pessoa são provas separadas. Ausência de input gravado não demonstra que cada quadro está em fase de ataque.

## Propriedade de arquivos e fluxo de integração

Codex mantém compatibilidade/mixins, controladores comuns, contrato de âncoras, Ryūjin e documentos globais. Senbon e Benihime mantêm seus assets/perfis e relatórios exclusivos conforme114/115. Nenhuma alteração de dano, stamina, cooldown, alcance ou servidor pode ser escondida em ajuste visual. Sem mudanças Zaraki.

Os agentes podem desenvolver o primeiro golpe nos seus checkouts isolados, seguindo esta interface. Mudança comum precisa de diff e comunicação coordenada; não é necessário esperar uma API universal inexistente para diagnosticar os assets. A integração final exige remover controladores/emissores redundantes junto com seus substitutos e repetir a mesma cena antes/depois no Minecraft real.

Estado: diagnóstico de fonte e contrato publicados nesta frente; vínculo completo dos combos customizados continua sob as entregas114/115 e aceite visual. Nada neste documento homologa duas mãos em primeira pessoa ou observador multiplayer.

## Calibração confirmada em27/10

O giro global body precede a escala negativaX/Y do renderer; no espaço local do braço, sua composição usa Rz(z) Ry(-y) Rx(-x). Item, braço e corpo não recebem a mesma conversão de sinais. Medição real, fixture de regressão, gerador atualizado e limites estão no [estado do combate](estado-combate-senbon-higashi.md). Não reutilizar subtração de Euler nem aplicar a base de um modelo a todas as identidades sem medição. Esta calibração não modifica controladores globais de outras zanpakutos.
