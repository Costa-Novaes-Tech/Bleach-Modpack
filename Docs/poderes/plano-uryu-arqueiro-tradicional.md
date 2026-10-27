# Uryū Ishida — plano do arqueiro tradicional

27/10/2026. Estado: planejamento; sem código, assets ou branch nova. Decisão explícita do usuário: arqueiro tradicional primeiro; TYBW depois. Slots abaixo são PROPOSTA, ainda não aprovados. Hyōrinmaru concluída por aceite explícito e retirada da fila ativa.

## Contrato e recorte

Referências: M04/Q1–Q7, M05/11.2 REV-SP-02, M06 e seus contratos de combate, M07/8.69–8.88, M08a/8.5. Arco/fundamentos/Gintō/Hirenkyaku/Schrift/Vollständig/Letzt Stil são aquisições distintas. Uryū é contato tradicional contextual, não professor universal. SP compra skills/formas; Mastery pela prática. Não chamar o kit de Shikai/Bankai, não mudar raça ao equipar um item e não acrescentar outra barra de recursos por conveniência.

Não incluir Antithesis/Vollständig nesta entrega. Letzt Stil pertence ao repertório tradicional, mas fica fora desta primeira entrega por recomendação: seu contrato exige suspensão persistente do acesso Quincy e recuperação viável; não é buff simples e não deve ser liberada sem sua jornada de retorno.

A busca dirigida em src/main/java não encontrou serviço Quincy equivalente aos kits de zanpakutō. CharacterData.knownIdentity/supportsForm e TechniqueDefinitions examinados são centrados em zanpakutō. Isso não é auditoria integral da raça. Fase0 deve mapear criação racial, recurso, equipamento/foco, entradas, persistência e requisitos existentes antes de decidir a integração. Não inserir Uryū como outra zanpakutō nem duplicar um sistema inteiro de personagem.

## Proposta de kit inicial

Ataque comum: formar arco/flecha de Reishi, puxar a corda, sustentar mira e soltar projétil. Sem flechas vanilla obrigatórias. Recurso espiritual segue o contrato racial; sem coleta manual constante de partículas. Arco próprio orientado por referências Kojaku/Ginrei Kojaku a escolher na fase visual, não retextura de um arco vanilla apresentada como modelo final.

- H — Seele Schneider: disparo especial de lâmina espiritual; trajetória e impacto legíveis. O efeito de penetração/ruptura é adaptação a especificar, sem ignorar toda defesa automaticamente. Não virar katana/novo moveset de espada nesta entrega.
- N — Licht Regen: rajada frontal de flechas, distinta do disparo carregado. Preparação, abertura controlada e recuperação; número visual não vira multiplicação ilimitada de dano. Collisions/tetos de acerto por alvo no servidor. Não usar chuva meteorológica global.
- B — Hirenkyaku: reposicionamento sobre fluxo de Reishi nos pés, sem conceder voo permanente. Preservar a esquiva Alt+direção. Requisito racial e aquisição próprios; equipar o arco não concede esse fundamento automaticamente. Distância, direção de entrada, custo e recargas serão definidos na ficha executável.
- C — Sprenger: preparação de um pentágono com cinco Seele Schneider e catalisação por Gintō. Sugerida área fixa e legível, preparação perceptível e detonação em reativação, com janela finita. Esse automatismo de colocação e a reativação são ADAPTAÇÕES PROPOSTAS. Não virar explosão instantânea sem preparo nem prisão inevitável; ter chance de sair da região.

Passiva funcional inicial: formação de arco/flechas por Reishi segundo os requisitos raciais, sem inventar crítico, regeneração ou buff gratuito. Mira/precisão pertencem ao comportamento do disparo; bônus numérico só com ficha aprovada.

## Direção visual e animação

Branco/azul-ciano; contornos finos e núcleo luminoso, cruz/foco reconhecível. Reishi converge para a mão antes de formar o arco. A corda estica com a mão de tração; flecha acompanha corda/empunhadura e nasce no ponto real de lançamento. Corpo lateral, base firme, mão de tração aproxima o rosto, soltura com retorno curto. Espelhar arco/tração para canhoto sem inverter duas vezes. Demonstrar essa mecânica SEM efeitos antes do kit completo.

Seele tem silhueta de lâmina e vibração controlada; Licht Regen usa agrupamento de flechas e ritmo, não nuvem opaca; Hirenkyaku deixa fluxo curto nos pés; Sprenger enfatiza vértices, conexões e convergência. Câmara de combate permanece controlável; não criar cinematic longa para cada tiro.

## Fases para o futuro prompt

0. Auditoria com mapa de implementado/ausente e interfaces compartilhadas, sem reescrever GDD.
1. Uma fatia jogável: modelo/rig editável de arco + formar/puxar/soltar UMA flecha, custo validado no servidor e contato mão/corda demonstrado no Forge real.
2. Integrar H/N individualmente, com testes de parede, alcance, alvo móvel, múltiplos alvos, gasto e repetição.
3. Hirenkyaku e Sprenger: requisitos/área/terreno/interrupção/expiração/limpeza/observador.
4. UI, persistência e balanceamento; não depender de comandos de QA para o uso normal.
5. Check/build, GameTests pertinentes e prova real de primeira/terceira pessoa, destro/canhoto, offhand, retorno ao controle e observador.

Antes de executar: fechar custos/dano/alcances/cooldowns, item/foco e Gintō/Seele como inventário/consumíveis conforme M11, forma de aquisição e entrada racial. Números finais não foram aprovados. Creative/QA pode fornecer equipamentos para teste; isso não implementa a jornada de produção. Não fechar essas decisões silenciosamente na IA.

## Ferramentas e propriedade

Reutilizar Forge/Java existentes, Blockbench+GeckoLib para modelo/corda se necessário, animação nativa de arco quando suficiente e PlayerAnimator para poses específicas. Better Combat não substitui automaticamente o controlador de um arco. Photon opcional para acabamento; mecânica/VFX base precisam funcionar sem ele. Sem novas dependências no plano inicial.

Branch proposta: codex/uryu-arqueiro-tradicional, baseada no mesmo commit atualizado de Feature-Poderes-bankais usado pela outra frente. Reutilizar checkout separado existente após conferir alterações; não criar pasta agora. IA possui namespace Quincy, modelos e assets próprios. Codex coordena interfaces gerais de raça/PlayerData/CharacterData, registros, rede, UI e persistência, evitando edição simultânea com a migração Benihime. Integrações comuns devem ser delimitadas em fase0, não aparecer como refatoração surpresa no fim.

## Fontes e limites de pesquisa

- Pierrot oficial, glossário ha: https://pierrot.jp/title/bleach/keyword_ha.html — Hirenkyaku como passo avançado sobre fluxo de Reishi nos pés; consultado27/10.
- Pierrot oficial, glossário sa: https://pierrot.jp/title/bleach/keyword_sa.html — Sprenger com Seele Schneider e Gintō, preparo demorado e explosão interna; Seele como arma de lâmina/Reishi vibratório. Fonte não aprova nossos slots, colocação automática ou balanceamento.
- Licht Regen: referência secundária https://bleach.fandom.com/wiki/Licht_Regen, resultado indexado remete ao mangá349; leitura integral indisponível402 no navegador de pesquisa. Conferir cena/arte antes de modelagem final, sem afirmar leitura integral de capítulo.
- Catálogo M08a tem referências oficiais adicionais de cruz/fonte e aprendizado. Consulta direta ao site TYBW retornou403; não tratar como fonte integral lida.
