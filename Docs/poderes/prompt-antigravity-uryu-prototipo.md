# Prompt Antigravity — protótipo Uryū tradicional

Implemente por fases um protótipo jogável do Uryū tradicional como arqueiro de longo alcance. Direção e adaptação do kit aprovadas; números V0 são temporários autorizados para testes. Arco espiritual com modelo próprio/editável desde o protótipo; efeitos especiais finais serão discutidos com o usuário depois. Não precisa instalar bibliotecas novas por antecipação.

Regras comuns: trabalhar no checkout separado já atribuído a esta conversa. Você não está sozinho no repositório: preservar alterações de outras IAs, sem reset/clean/stash/force-push nem apagar arquivos para conseguir checkout. Não alternar a branch do checkout principal do Codex. Não criar pasta/worktree nova nesta tarefa. Dados transitórios de QA não entram em commits em massa; guardar pacote pequeno com fontes/hashes, marcos e limites. Documentação em Docs/poderes, atualizando o plano por assunto em vez de criar relatórios repetidos.

Antes de editar: ler AGENTS.md aplicáveis, Docs/poderes/00-CONTINUIDADE-CODEX.md, contrato118, estado-combate-senbon-higashi.md e o plano da frente. Seguir as skills do projeto que estiverem disponíveis e de fato acessíveis. Não inventar MCP instalado; usar ferramentas já verificadas. Git/status/branch/HEAD devem constar na entrega.

Criar BRANCH NOVA da nossa base atualizada Feature-Poderes-bankais, e não da main ou do HEAD da branch antiga. Conferir git status --short --branch e git worktree list; preservar e identificar mudanças locais antes de mudar checkout. Executar git fetch origin; conferir git log -1 --oneline origin/Feature-Poderes-bankais. Se a branch nova já existir, não resetar/recriar: comparar ancestralidade/conteúdo e informar. Após criar, registrar o hash-base. Não fazer merge automático para poderes nem main. Entregar commits focados na própria branch e testes; integração será revisada pelo Codex.

Critério de QA: movimento da arma/modelo precisa ser observado no Minecraft Forge real, não apenas em JSON, navegador ou Blockbench. Validar contato das mãos, origem de projétil, relógio real do clipe, destro/canhoto e primeira/terceira pessoa. Não ajustar fixtures/asserções apenas para passar. Distinguir build, teste automatizado, prova gráfica e aceite artístico. Nenhuma baixa automática por contagem de testes.

Comando de criação, somente após verificar o estado local:

```powershell
git switch --no-track -c codex/uryu-arqueiro-tradicional origin/Feature-Poderes-bankais
```

Leia integralmente Docs/poderes/plano-uryu-arqueiro-tradicional.md, incluindo Ficha V0, fontes e pendências. Consulte M04/Q1–Q7, M05/REV-SP-02, M06, M07/Quincy e M08a/8.5. O jogador continua personagem original; perfil Uryū não é Shikai/Bankai nem concede fundamentos ao equipar item. Antithesis/Vollständig e Letzt Stil estão fora desta entrega. Não criar outra barra ou tornar o arco uma zanpakutō para reutilizar um switch.

Kit V0: disparo comum48blocos/carga1s/6HP/2PE; H Seele Schneider48blocos/largura0,5/até3alvos/10HP/12PE/CD6s; N Licht Regen40blocos/cone20graus/12flechas em2s/2HP por acerto/teto8HP por alvo/20PE/CD10s; B Hirenkyaku6blocos horizontais/8PE+10stamina/CD6s; C Sprenger centro36blocos/raio6/altura4/preparo1,5s/reativação até6s da criação/18HP/teto8alvos/35PE/CD20s. A ficha explica detalhes de cobrança, colisão e expiração e prevalece sobre abreviações deste parágrafo. HP não é coração; não multiplicar todos os acertos como dano integral. PE usa o recurso funcional vigente, sem nova barra.

Fase0 — mapear entrada racial, recurso, atributos, capacidade de dano espiritual, foco, persistência e controles realmente implementados. Produzir mapa curto de interfaces e dependências. Escolhas rotineiras podem ser resolvidas; decisões novas de produto permanecem explícitas. Não reescrever o GDD. Se não existir aquisição pública adequada, oferecer acesso de QA restrito a mundo/ambiente descartável e declarar integração de produção pendente; não fingir jornada finalizada. Consumo/economia de Gintō/Seele não está fechado por estes números.

Fase1 — arco e UMA flecha. Blockbench para silhueta própria; GeckoLib se corda/partes articuladas exigirem, aproveitando recursos nativos de arco onde bastarem. Pose lateral, uma mão sustenta arco, outra puxa corda até o rosto; corda/flecha acompanham a mão; projétil sai do ponto físico correto. Validar antes de preencher com partículas. Não alterar o renderer/moveset das zanpakutō aprovadas.

Fase2 — integrar disparo/H/N, dano autoritativo, parede, alcance, projétil e gasto/cadência. Controle de colisão/hits e tetos por alvo; não ignorar defesa nem remover hurtTime para inflar rajadas. Aim e modelos legíveis.

Fase3 — B e C. Hirenkyaku preserva Alt+direção e exige seu fundamento próprio. Sprenger tem cinco vértices, catalisação Gintō e área fixa legível; automatismo de formação/reativação é nossa adaptação autorizada. Valide o piso LOCAL, inclusive cavernas e declives; não lançar vértices sob solo ou buscar teto por heightmap global. Sem dano através de paredes, sem destruir construções. Timeout/cancelamento/forma/arma/dimensão/logout removem estado conforme contrato; um campo por jogador. Continuação válida não fica bloqueada pelo cooldown inicial.

Fase4 — UI mínima, persistência do perfil e testes. Arco/fundamentos/poderes separados; SP/Mastery seguem contratos. Equipamento pode ser providenciado no laboratório, sem burlar critérios em produção. Testar save novo e recarregado, falta de recurso, projétil contra parede, inimigo móvel/grupo, reativação antecipada/tardia, múltiplos jogadores e limpeza.

Propriedade: priorizar novas classes/resources de namespace Quincy. Esta frente pode cuidar dos registros de arco/projéteis, PlayerData/subestado Quincy e rede específica necessários; alterações inevitáveis em CharacterData, despachantes, idioma/registro/rede globais devem ser pequenas, listadas e coordenadas com Codex, pois a outra frente migra uma técnica Benihime. Não reformatar arquivos compartilhados inteiros. Não modificar Benihime, Higashi, Senbon, Hyōrinmaru, Zaraki ou seus valores para facilitar o protótipo.

Avançar autonomamente de uma fase à próxima quando o critério estiver demonstrado; não pedir confirmação de rotina. Se uma dependência real impedir produção, entregar a fatia testável e explicar exatamente o limite, mantendo a parte independente em andamento. Rodar check/build, GameTests pertinentes e cliente real. Não dar como pronto sem prova do arco/corda/mãos e das quatro ações. Não publicar centenas de capturas; selecionar marcos e dados suficientes, com originais locais rastreáveis. Entregar commits focados, comandos/exit codes, limitações e roteiro curto para teste humano. Sem merge para poderes/main.
