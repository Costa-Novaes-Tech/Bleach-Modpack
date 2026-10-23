# M09-08A/B — Propostas de interrupção e relógios

**Data:** 23/10/2026. **Estado final:** DECIDIDOS, duas respostas explícitas recebidas em 23/10/2026. [M09 responsável](../09-mobs-bosses-e-encontros.md), 8.21/8.22 e 12.15/12.16. M09-01–07 aprovados; perguntas/opções originais preservadas e respostas abaixo aprovam as primeiras opções nos seus alcances.

## M09-08A

**Pergunta:** Se a região de uma prova ou confronto preparado deixar de ser simulada, ou o servidor reiniciar, recomendo encerrar a tentativa como interrompida, sem presumir vitória ou derrota em combate. Conquistas já registradas e gastos permanecem; voltar exige confirmar uma nova tentativa, usando o estado inicial ou checkpoint previsto. Não se retoma um golpe antigo ao carregar. Adotamos isso ou salvamos a tentativa para continuar exatamente de onde parou?

**Opções:**

1. Encerrar como interrompida; nova tentativa manual (recomendado)
2. Salvar e retomar a tentativa em andamento

**Resposta em 23/10/2026:** “Encerrar como interrompida; nova tentativa manual (recomendado)”.

## M09-08B

**Pergunta:** Para os tempos próprios dos mobs comuns — cooldowns e espera/recuperação natural — recomendo contar apenas enquanto a criatura estiver sendo simulada na região ativa. Descarregar a região ou parar o servidor pausa esses tempos e preserva a Vida salva, sem cura offline; golpes incompletos não retomam ao carregar. Isso não torna todo mob permanente nem muda o relógio já aprovado do jogador. Adotamos esse modelo ou contamos esses tempos pelo relógio do mundo mesmo com a criatura descarregada?

**Opções:**

1. Pausar tempos fora de simulação (recomendado)
2. Contar tempos pelo relógio do mundo

**Resposta em 23/10/2026:** “Pausar tempos fora de simulação (recomendado)”.

## Cobertura e limites

08A distingue interrupção de prova de resultado já confirmado, sem reset por logout individual quando outros continuam. 08B aprova tempo de simulação para mobs comuns salvos, sem cura offline nem alteração dos tempos do jogador. Não decide reentrada, escala/recompensa, entidades permanentemente salvas, despawn, limpeza de terceiros, carregamento forçado ou restauração de blocos. Adaptações de gameplay sem pesquisa externa/API nova, runtime, build, testes ou publicação.
