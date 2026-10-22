---
name: bleach-game-design
description: "Continuar, especificar e revisar o game design do Bleach Mod para Minecraft Java/Forge, respeitando os módulos e decisões do repositório. Use em documentação de raças, combate, progressão, transformações, mundos, quests e campanhas deste projeto; a execução e o QA do mod têm fluxo próprio."
---

# Bleach Game Design

Revisão: 20/10/2026. Papel: coautoria crítica da documentação de um mod de Bleach integrado a Minecraft. Preservar exploração, construção, sobrevivência e mundo persistente ao especificar a experiência espiritual e narrativa.

## Localizar o contexto vigente

**Direção explícita — 22/10/2026, REV-SP-02/M05-11.2:** Spiritual Points compram **skills e transformações**, usando Dragon Mine Z como referência de compra. **Mastery cresce pela prática, não é comprada.** Requisitos/contexto disponibilizam aquisição; SP paga o desbloqueio; Energia/Stamina custeiam o uso conforme a capacidade. Não limitar SP aos atributos ou tornar a compra de formas apenas uma exceção eventual. Conciliar os trechos anteriores de M05/M06/M07 nesse escopo; preços/exceções e representação de níveis exigem ficha. Não copiar todo o outro mod ou criar SP por cada ativação.

Use o repositório que contém esta skill: da pasta da skill, subir três níveis. Confirme `git status --short --branch`, HEAD e os arquivos antes de editar; não dependa de branch ou caminho de uma conversa anterior.

Leia o [handoff vigente](../../../Docs/game-design/00-handoff-atual.md), o [mapa modular](../../../Docs/game-design/00-mapa-modular.md) e a [base datada de Minecraft/Bleach](../../../Docs/game-design/00-base-conhecimento-minecraft-bleach.md). Depois leia integralmente o bloco/documento trabalhado e os contratos de que ele depende. Buscas e cabeçalhos ajudam a localizar revisões; não substituem a leitura necessária à decisão.

O mapa possui **16 módulos** e um template próprio. Use essa estrutura. O ponto de continuidade vem do handoff e dos documentos, não de uma etapa fixada nesta skill.

M05 revisa explicitamente partes de M02; M06-07 revisa modelos de disciplinas, aquisição e apresentação da progressão. Procure `REV-M02`, `REVISADO`, `SUPERADO`, `M06-07` e as conclusões antes de recuperar uma regra antiga. Uma aprovação revisada prevalece no seu escopo; divergência de código não revisa o produto por si só.

## Pesquisar sem inventar cânone ou compatibilidade

Para cada regra relevante, ligar **referência de Bleach → adaptação aprovada → funcionamento em Minecraft → evidência de validação**.

- Seguir D05: mangá como base; anime oficial e publicações licenciadas identificados como complemento; filler/original/adaptação rotulados. Registrar fonte exata, continuidade, data de consulta e capítulo/episódio/seção quando acessível.
- Verificar publicações recentes e informações mutáveis. Índice, trailer ou anúncio só sustentam o que mostram; não declarar leitura de obras restritas nem importar habilidades de outro jogo como regra do mangá.
- Conferir versão e loader no build local. O recorte pesquisado em 20/10/2026 é Java 1.20.1 / Forge 47.4.10 / Java 17. Revalidar se houver mudança, sem misturar APIs Bedrock, NeoForge, Fabric ou posteriores.
- Se uma referência decisiva estiver inacessível, marcar a afirmação como pendente e continuar a parte independente. Não completar exceções raciais ou números com lembranças incertas.

## Conduzir a discussão

Trabalhar um bloco principal por vez. Distinguir o que já foi decidido, o que está sendo proposto e o que foi delegado a outro módulo.

Comparar alternativas concretas, explicar o efeito para o jogador e recomendar uma. Agrupar poucas decisões estruturais por rodada. Aproveitar respostas já dadas; não pedir novamente autorização para registrar uma decisão aprovada.

Preparar análises e rascunhos revisáveis dentro do escopo autorizado. Marcar propostas como propostas até a resposta explícita do usuário. Quando uma decisão real impedir o fechamento, apresentar a pergunta com recomendação e desenvolver o restante que não depende dela. Autorizar a preparação da skill não aprova automaticamente novas regras de raça ou poder.

### Contratos comuns antes de perguntas repetidas — ajuste de 22/10/2026

Por solicitação do usuário, consolidar regras já aprovadas em contratos comuns com cobertura explícita; cada forma/raça declara um perfil e apenas suas diferenças/exceções. Preservar IDs, respostas e datas como evidência. Apresentar um pacote por bloco e perguntar somente decisões realmente novas, ampliações de escopo, consequências ou conflitos. Não repetir aprovação de custo, interrupção, interface, equipamento ou ciclo de vida quando o perfil já estiver coberto pelo contrato vigente. Ampliação ainda não aprovada é proposta uma única vez para a classe pertinente; depois as fichas a referenciam. Não uniformizar natureza permanente, conquista, manutenção, retorno ou riscos especiais para reduzir perguntas. Usar M07/seções 7.1/7.2 como exemplo vigente; não fixar nelas o ponto de continuidade futuro.

Preservar estes contratos enquanto não houver revisão explícita:

- personagem original, catálogo de poderes canônicos e duplicação multiplayer como adaptação; um poder principal por vez;
- origem, natureza, evolução, forma, cargo e afiliação separados; morte de gameplay não altera raça automaticamente;
- cinco atributos fundamentais; Spiritual Points compram skills e transformações, além do investimento nos atributos — esclarecimento explícito REV-SP-02 do usuário em 22/10/2026, M05/11.2; compra/desbloqueio e ativação distintos; Mastery pela prática, não comprável;
- Proficiência da disciplina, repertório e Mastery individual separados; skill tree e segundo rank obrigatório de disciplina superados;
- fundamentos e desenvolvimento autônomo possíveis; não exigir um NPC para cada técnica;
- mundo compartilhado, progresso pessoal e campanha racial separados; requisitos de catch-up ainda dependem de M15;
- custos, fórmulas e calibração deliberadamente abertos permanecem pendentes até o módulo e a evidência apropriados.

## Especificar e revisar

Usar o template do mapa; adequar o detalhe ao estado do módulo. Um bloco estrutural pode fechar responsabilidades e delegar números, sem se declarar pronto para implementação.

Ao especificar uma mecânica, usar [a revisão de contratos e casos de Minecraft](references/revisao-de-contratos.md). Em quests, campanhas, personagens e eventos, usar também [a revisão de coerência narrativa](references/coerencia-narrativa.md). Não carregar catálogos inteiros para uma alteração simples.

Registrar unidades quando houver valores: blocos, ticks, segundos nominais, HP ou porcentagem. Distinguir tick de servidor e quadro de animação. Hipóteses numéricas autorizadas devem ser rotuladas como provisórias, com objetivo de validação; não preencher números apenas para completar uma ficha.

Definir proprietário do estado, entradas/saídas entre módulos e invariantes. Para execução, considerar custo, alvo, início, manutenção, interrupção, conclusão, feedback e recuperação. Definir o comportamento necessário à experiência sem impor infraestrutura externa ou outra biblioteca sem necessidade demonstrada.

## Registrar continuidade e qualidade

Após decisões aprovadas, atualizar o bloco correspondente com data, IDs e revisão substituída quando houver. Atualizar mapa/handoff quando o ponto de continuidade mudar; preservar o histórico útil.

Manter separados: decisão, especificação, implementação, teste e aceite. `AUDITADO` descreve a revisão realizada; não implica aprovação artística nem funcionamento observado. Citar resultados históricos como históricos. Documentação não exige iniciar o cliente ou uma bateria de gameplay.

Antes de entregar, conferir consistência, referências locais, unidades e diff. Relatar o que mudou, o que continua aberto e o próximo bloco. Para implementação ou QA solicitado, consultar a skill existente `bleach-mod-dev` e as instruções atuais, sem converter esta tarefa documental em mudança de runtime.

## Referências externas selecionadas

Este fluxo foi escrito para os contratos do repositório. A [nota de origem e adoção](references/origem-e-adocao.md) explica o aproveitamento de `game-design-architect` e de materiais narrativos de `fcsouza/game-dev`, além dos limites dessa adoção. As referências externas são apoio de análise e não substituem as decisões locais.
