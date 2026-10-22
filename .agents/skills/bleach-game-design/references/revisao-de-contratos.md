# Revisão de contratos — Bleach dentro de Minecraft

Aplicar ao bloco em discussão, conforme seu nível de detalhe. Itens delegados precisam de destino e motivo, sem forçar números ou conteúdo antecipado.

## Ficha de uma decisão

| Campo | Conteúdo esperado |
|---|---|
| Identificação | ID estável do módulo/bloco, título e data |
| Estado | Proposto, decidido, pendente ou delegado; estados técnicos registrados separadamente |
| Experiência | O que muda nas escolhas e ações do jogador |
| Evidência | Fonte canônica/técnica e limite do que foi conferido |
| Herança | Decisões já aprovadas que restringem esta escolha |
| Alternativas | Consequências, custo de conteúdo e risco concreto de cada opção relevante |
| Recomendação | Opção preferida e sua justificativa |
| Contrato | Regras, limites, entradas, saídas e dono do estado |
| Revisão | Qual regra anterior muda, se aplicável; não promover uma proposta sem resposta |
| Aceitação | Resultado observável esperado e cenários necessários |
| Dependências | O que fica para outro bloco/módulo e por quê |

A documentação deve continuar legível. Usar essa ficha onde a rastreabilidade agrega valor, sem repetir os mesmos contratos em cada parágrafo.

## Capacidade ou sistema

- Separar natureza racial, disciplina, técnica, poder principal e transformação.
- Descrever condição inicial, requisitos de acesso, execução e condição final.
- Explicar custo e momento do pagamento, reativação e interrupção quando existirem.
- Declarar alvo, alcance, cobertura, aliados, pets e participação quando pertinentes.
- Diferenciar o efeito mecânico de sua apresentação; observador e usuário precisam receber o feedback apropriado.
- Explicar como atributos, Proficiência e Mastery contribuem sem criar progressões redundantes.
- Identificar o significado da falha e a recuperação; uma falha de ação não precisa causar perda permanente.

## Mundo Minecraft

| Situação | Questão a resolver quando aplicável |
|---|---|
| Construção e terreno | O jogador pode alterar a arena, bloquear passagem ou atingir sua própria base? |
| Inventário | Vínculo pertence ao personagem, ao item ou a ambos? Drop, baú, troca e morte contornam requisitos? |
| Sistemas vanilla | Armadura, encantamentos, escudo, comida, fome, dano e efeitos têm interação definida? |
| Progressão repetível | Mob preso, aliado, pet, automação ou AFK produz ganho indevido? |
| Movimento e áreas | Há colisão, destino válido e comportamento em água, desnível e espaço estreito? |
| Chunks e dimensões | O conteúdo funciona na região carregada e no tipo de mundo previsto? |
| Modos de jogo | Survival, Creative, Spectator e comandos administrativos são tratados conforme o escopo? |
| Ciclo de vida | Morte, logout, retorno, troca de dimensão e reinício preservam conquistas e limpam estado transitório? |
| Multiplayer | Servidor autoriza; progresso pessoal, party, encontro e mundo têm donos distintos? |
| Legibilidade | Câmera, partículas, som e UI permitem entender o combate e retornar ao controle? |

Não presumir pausa global para uma cena, regeneração/destruição de bases, keepInventory, sistema de claims, rolamento de rede ou persistência automática. Especificar a necessidade e confrontá-la com o comportamento existente.

## Fechamento por maturidade

- **Estrutural:** responsabilidades, compatibilidades, estados e dependências coerentes; números podem permanecer delegados.
- **Especificado para implementação:** regras executáveis e falhas relevantes resolvidas; contratos de dados e integrações suficientes; pendências não impeditivas explicitadas.
- **Implementado/testado:** arquivos e evidências identificáveis, cobrindo os cenários declarados. Código existente não demonstra a experiência completa.
- **Aceito:** aprovação do resultado pelo responsável, com o escopo dessa aprovação registrado.

Uma revisão identifica conflitos e lacunas; não garante ausência absoluta de omissões.
