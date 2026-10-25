# M06 — CS-01D — Continuidade da janela de combate

**Data:** 23/10/2026. **Estado:** DECIDIDO por resposta de 23/10/2026. [Perfil em M06a/8.4](../06a-estado-de-combate.md).

## Heranças e escolha nova

CS-01A/B/C aprovados: envolvimento real, ambiente separado e saída após cessar conflito/ameaça com compromisso de encontro. M06-11U encerra preparos/manifestações; V preserva cooldowns/recuperações pelo relógio de simulação; W mantém os disparos autônomos elegíveis na região ativa. M09-06B mantém a tentativa para participantes aptos, e M09-08A a interrompe por descarregamento/reinício. Passagens comuns continuam fora de combate por M10-02B; reset/evolução conservam seus requisitos.

A extensão desse relógio à janela restante de **Combat State** é a escolha nova, junto da reavaliação do contexto. Não repetir aprovação do encerramento de formas, da duração dos cooldowns ou dos disparos autônomos. São regras do produto, sem nova afirmação canônica/API ou auditoria de runtime.

## Pergunta enviada

Para a marca de combate, recomendo preservar a janela restante após morrer, desconectar ou mudar de dimensão validamente. Ela conta pelo tempo de simulação do mundo e pausa com o servidor parado. Ao voltar, o jogo reavalia ameaças, efeitos e encontro atuais: um agressor antigo não segue por memória, mas um conflito ainda válido pode manter ou renovar o estado. Isso não restaura ataques/formas nem autoriza reentrada em boss. Adotamos essa continuidade ou limpamos a marca nessas transições e consideramos apenas a situação nova?

**Opções:** “Preservar janela e reavaliar o contexto (recomendado)” / “Limpar a marca e avaliar só a situação nova”. **Resposta em 23/10/2026:** “Preservar janela e reavaliar o contexto (recomendado)”. Alternativa de limpeza da marca não adotada.

## Regra aprovada e consequências

Preservar o restante legítimo, sem reiniciar duração integral nem aplicar multa nova por sair/morrer. Simulação ativa pode fazer a espera terminar offline; servidor parado pausa. Não contar avanço da hora do dia ou trocar de relógio ao viajar. Reavaliar ameaças/efeitos/encontro válidos conforme A/C, sem perseguir por UUID ou restaurar tentativa interrompida. Uma nova situação hostil segue seus gatilhos próprios; não marcar automaticamente quem voltou só por haver um inimigo próximo.

Alternativa limpa a janela entre transições e facilita a retomada, mas também permite antecipar a espera saindo/voltando. A política de PvP, reentrada em boss e punição por desconectar continua com suas fichas e M15; nenhuma aprovada por associação. D não libera portal comum em combate, cura, regen/treino offline ou reembolso de custos.

## Continuidade

Resposta aplicada em M06a e referências dos consumidores; retorno funcional COV-06 consolidado conceitualmente, sem pergunta pendente. Próximos: complementos necessários de M08/M09 e revisão do checkpoint para publicação. Fichas de efeitos e classificação de autoria/compatibilidade permanecem condições antes da implementação. Nenhum código, mapa/save, build, teste de jogo, commit ou push nesta proposta.
