# M11-04A — Recursos ao renascer após morte comum — DECIDIDO

**Proposta e aprovação:** 28/10/2026, America/Maceio. **Resposta literal:** “Vida, Stamina, Energia e fome cheias (recomendado)”. [Proposta e contrato em M11/12.12](../11-itens-e-economia.md#1212-m11-04a--decidido--recursos-ao-renascer-após-morte-comum); [retorno M05/17.3](../05-atributos-e-recursos.md#173-retorno-m05m11--recursos-ao-renascer--decidido--28102026).

**Heranças:** morte comum não muda raça ou estágio permanente; M10-08B/C/D define local/corpo de retorno; M07-G01 encerra manifestações temporárias; M06/M06a preservam tempos legítimos e consequências especiais conservam seus próprios contratos. 03A/B decidiram alimentação/recuperação, não recursos ao renascer.

**Recomendação:** Vida, Stamina e Energia Espiritual cheias nos limites válidos do corpo-base permanente, e barra de fome cheia no renascimento após morte comum. Saciedade oculta inicial ainda precisa de ficha, sem pressupor saturação máxima. Permite retomar a jornada sem espera obrigatória por recurso ou inanição herdada.

Não restaurar máximos temporários, formas, poderes suspensos, reparos de Bankai ou gastos anteriores. Preservar os tempos de combate; não dar imunidade, limpar universalmente efeitos ou definir itens/XP. Login, sono, viagem e correção de posição não recebem a reposição; Hardcore não recebe uma vida nova. O possível uso da morte para abastecer recursos deve ser observado na futura validação, sem criar penalidades não aprovadas.

**Alternativa:** Vida e barra de fome cheias, Stamina/Energia parciais recuperadas pelas regras normais de M05; frações e viabilidade de retomada ainda precisam de ficha.

**Pergunta:** Depois de uma morte comum, recomendo renascer com Vida, Stamina e Energia cheias nos limites do corpo-base e com a barra de fome cheia. Assim o jogador consegue retomar a jornada. Formas continuam encerradas, os tempos de combate são preservados e consequências especiais dos poderes não são curadas. Itens e XP serão tratados separadamente. Adotamos essa reposição ou Vida/fome cheias com Stamina/Energia parciais, recuperadas normalmente?

**Opções:** “Vida, Stamina, Energia e fome cheias (recomendado)” / “Vida/fome cheias; Stamina/Energia parciais”.

**Evidência e limite:** proposta de adaptação, sem alegação canônica de ressurreição. Leitura dirigida de `CapabilityEvents.respawn` em `26b73b3` encontrou preenchimento de Stamina e do recurso legado `currentReiatsu`; o método não distingue explicitamente morte de outras causas do evento. Isso não aprova a política de produto, não comprova integração de Vida/fome ou consequências especiais e não certifica comportamento em jogo. A futura realização deve distinguir morte comum de outros retornos.

Somente documentação local, sem alteração de recursos/código, build, teste de jogo ou publicação.

**Aplicação após a resposta:** recomendação adotada em M11/8.13/12.12 e M05/17.3, com os limites acima. A alternativa permanece histórica. Próximo bloco: posse/perda/reposição dos instrumentos básicos, sem aprovação automática de sua política material.
