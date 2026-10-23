# M06 — CS-01A/B/C — Estado automático de combate

**Data:** 23/10/2026. **Estado:** M06-CS-01A/B/C DECIDIDOS por respostas de 23/10/2026. [Perfis em M06a](../06a-estado-de-combate.md).

## Contexto e heranças

Retorno dirigido COV-06 da revisão M01–M10. M05-02B e M06-01B/C já aprovam Combat State automático separado da interface manual, ainda sem eventos exatos. RAC-30B/D exige capacidade espiritual de dano também contra o jogador; perceber/tentar atacar não garante efeito. RAC-31A foi aprovado por “No atendimento do NPC (recomendado)”; reset e passagens/evolução dependem de condições fora de combate. M09 já aprovou sentidos por perfil e tentativas limitadas sem rota/ataque viável.

Estas propostas são regras do mod. Nenhuma nova alegação canônica, API ou implementação nesta rodada; números finais continuam para calibração. As perguntas resolvem significado dos eventos, não taxas ou punições por logout.

## M06-CS-01A — Entrada e renovação

**Pergunta:** Para entrar em combate, recomendo contar uma ofensiva válida contra outro ser, uma tentativa real de ataque capaz de afetar o personagem (mesmo bloqueada/esquivada) ou sua participação num confronto iniciado. Cura/proteção efetiva de um participante também envolve quem ajudou; estar na party não basta. Abrir a interface, sentir Reiatsu, treinar no vazio ou receber uma tentativa incapaz de afetar seu corpo não ativa o estado. Adotamos esses eventos ou contamos apenas dano efetivamente causado/recebido e confrontos preparados?

**Opções:** “Envolvimento real, mesmo sem perder Vida (recomendado)” / “Somente dano efetivo e confrontos preparados”. **Resposta em 23/10/2026:** “Envolvimento real, mesmo sem perder Vida (recomendado)”. **DECIDIDO**; alternativa não adotada.

Eventos admitidos e compatíveis; não criar travamento de mira, revelar um agressor oculto ou propagar a marca a uma party inteira. Perfil M06a/8.1.

## M06-CS-01B — Ambiente e perigo

**Pergunta:** Para dano ambiental sem origem hostil, recomendo não ativar combate só por queda, lava ou afogamento. O perigo continua existindo: viagem, reset e evolução ficam bloqueados enquanto houver risco ou dano em curso incompatível com a operação, mesmo com o indicador fora de combate. Efeitos causados por um ataque mantêm sua classificação própria. Adotamos essa separação ou fazemos qualquer dano elegível também ativar o combate?

**Opções:** “Perigo separado do estado de combate (recomendado)” / “Qualquer dano elegível ativa combate”. **Resposta em 23/10/2026:** “Perigo separado do estado de combate (recomendado)”. **DECIDIDO**; alternativa não adotada.

Não aprova dano ambiental a todo tipo de corpo, imunidade, limpeza de condição ou acesso livre a serviço. Perfil M06a/8.2.

## M06-CS-01C — Saída e encontro preparado

**Pergunta:** Para sair de combate, recomendo uma breve janela sem novos eventos de envolvimento e sem ameaça atual capaz de engajar. Inimigo preso sem rota nem ataque viável não mantém o estado indefinidamente. Uma prova ou boss ainda em andamento mantém seus participantes em combate até um término ou saída válida previstos na ficha. Adotamos esse modelo ou basta passar um tempo sem ações/golpes, mesmo com ameaça ou encontro ativo?

**Opções:** “Cessar conflito e ameaça; respeitar encontro (recomendado)” / “Só prazo sem novas ações ou golpes”. **Resposta em 23/10/2026:** “Cessar conflito e ameaça; respeitar encontro (recomendado)”. **DECIDIDO**; alternativa não adotada.

Pausa normal de Recovery/cooldown não elimina uma ameaça viável. Contato/sentidos/alcance/percurso vêm dos perfis; não teleportar, curar mob ou punir arena construída para manter pressão. Perfil M06a/8.3.

## Continuidade

A/B/C aplicados; nenhuma resposta pendente dessas escolhas. Fechar integração de ciclo de vida e efeitos antes de declarar o retorno concluído. Depois complementos M08/M09 e revisão para publicação autorizada. Nenhum código, mundo, save, build, teste de jogo, commit ou push nesta abertura.
