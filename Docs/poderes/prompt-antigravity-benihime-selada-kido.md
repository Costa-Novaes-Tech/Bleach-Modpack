# Prompt Antigravity — C selado Benihime: Hadō91

Substitua o Gigai Portátil do C SELADO por Hadō91 — Senju Kōten Taihō. Direção aprovada pelo usuário, números V0 autorizados para protótipo. Escopo: técnica selada, sua coreografia/efeitos e migração necessária. Combate básico, Shikai, Bankai, manifestação e transformação aprovados permanecem preservados.

Regras comuns: trabalhar no checkout separado já atribuído a esta conversa. Você não está sozinho no repositório: preservar alterações de outras IAs, sem reset/clean/stash/force-push nem apagar arquivos para conseguir checkout. Não alternar a branch do checkout principal do Codex. Não criar pasta/worktree nova nesta tarefa. Dados transitórios de QA não entram em commits em massa; guardar pacote pequeno com fontes/hashes, marcos e limites. Documentação em Docs/poderes, atualizando o plano por assunto em vez de criar relatórios repetidos.

Antes de editar: ler AGENTS.md aplicáveis, Docs/poderes/00-CONTINUIDADE-CODEX.md, contrato118, estado-combate-senbon-higashi.md e o plano da frente. Seguir as skills do projeto que estiverem disponíveis e de fato acessíveis. Não inventar MCP instalado; usar ferramentas já verificadas. Git/status/branch/HEAD devem constar na entrega.

Criar BRANCH NOVA da nossa base atualizada Feature-Poderes-bankais, e não da main ou do HEAD da branch antiga. Conferir git status --short --branch e git worktree list; preservar e identificar mudanças locais antes de mudar checkout. Executar git fetch origin; conferir git log -1 --oneline origin/Feature-Poderes-bankais. Se a branch nova já existir, não resetar/recriar: comparar ancestralidade/conteúdo e informar. Após criar, registrar o hash-base. Não fazer merge automático para poderes nem main. Entregar commits focados na própria branch e testes; integração será revisada pelo Codex.

Critério de QA: movimento da arma/modelo precisa ser observado no Minecraft Forge real, não apenas em JSON, navegador ou Blockbench. Validar contato das mãos, origem de projétil, relógio real do clipe, destro/canhoto e primeira/terceira pessoa. Não ajustar fixtures/asserções apenas para passar. Distinguir build, teste automatizado, prova gráfica e aceite artístico. Nenhuma baixa automática por contagem de testes.

Comando de criação, somente após verificar o estado local:

```powershell
git switch --no-track -c codex/benihime-selada-kido origin/Feature-Poderes-bankais
```

Leia integralmente Docs/poderes/plano-benihime-substituicao-selada.md. Alternativa de contrapulso não foi escolhida. Também consultar TechniqueDefinitions, BenihimeTechniqueService, BenihimePolicy, CharacterData e migrações existentes. Não redesenhar todo o kit.

Ficha V0: ponto de impacto até24blocos, raio4blocos, preparo1,2s,6lanças VISUAIS convergentes,16HP totais por alvo/ativação antes da defesa/escala, máximo8alvos, custo30pontos do recurso espiritual vigente, recarga14s. HP não significa coração. Seis lanças não dão seis vezes16HP. Sem TNT/destruição de blocos; nenhuma câmera forçada nova. Preservar o destaque/dano da suprema da Bankai.

Fase0 — inventariar o ID gigai e como desbloqueio/Mastery/saves antigos usam essa chave. Definir a transição para senju_koten_taiho sem apagar progresso ou outros slots. Registrar cobrança/interrupção e relógio do evento conforme contratos existentes. Números V0 são teste, não rebalanceamento global.

Fase1 — validar a pose SEM VFX: base firme, arma/bengala baixa numa mão, mão de conjuração se posiciona, torso acompanha e recupera. Cabo solidário à palma. Se offhand estiver ocupado, usar pose compatível sem mover item alheio ou exigir esvaziar inventário. Validar frente/perfil, destro/canhoto e primeira pessoa. O modelo Benihime tem sua própria base: não copiar offsets Ryūjin; usar medição real do renderer e o contrato118.

Fase2 — uma lança nasce do gesto e chega a ponto válido; origem, parede e timing corretos. Depois construir seis pontos luminosos no arco lateral/superior, convergência ritmada e clarão concentrado. Cor inicial rosa-pálido/branco, distinta dos fios Shikai. Efeitos legíveis sem cobrir toda a tela. Som provisório local existente, sem inventar que recebeu voz nova do usuário. Blockbench só se houver necessidade de modelo próprio; Photon opcional, funcionamento base obrigatório.

Fase3 — integrar C: ponto por mira aceita solo/alvo válido; selecionar destino no lançamento e resolver dano UMA vez na chegada sincronizada. Sem atravessar paredes. Distinção clara entre inimigos elegíveis, aliados e cenário. B Seis Hastes→C é combinação, não morte/controle garantido. Retirar o recuo e a criação de decoy ligados ao C antigo; limpar apenas entidades temporárias do serviço/owner, nunca remover mobs/armor stands em massa.

Fase4 — migrar descoberta/Mastery gigai→senju_koten_taiho em saves antigos e testar carregamento repetido sem duplicação. A propriedade da técnica e condições da forma selada continuam válidas. Preservar H/N/B, progressão de outros kits e Bankai. Alteração CharacterData/migração pode conflitar com Quincy: coordene interfaces com Codex, alterações pequenas e explícitas, sem reformatação global. Esta frente possui serviço/policy/choreografia/VFX Benihime, trecho Benihime de definições e sua migração; Quincy possui suas novas classes/registro/rede.

Fase5 — check/build, regressões e GameTests pertinentes, cliente Forge real. Cobrir alvo móvel/grupo, cast no solo, parede, limite de alcance, falta de recurso, interrupção, troca de arma/forma, logout, save antigo/novo; regressão curta das outras formas. Conferir braço e arma antes de aprovar partículas. Demonstrar recuperação ao controle e custo único; registrar o que não foi testado, especialmente observador remoto.

Avançar autonomamente entre fases verificadas. Não trocar critério de teste ou fixture para mascarar pose errada. Entregar commits focados na própria branch, pacote compacto de prova real com fontes/hashes e roteiro para usuário. Sem merge automático para poderes ou main; o Codex revisará a integração das duas frentes.
