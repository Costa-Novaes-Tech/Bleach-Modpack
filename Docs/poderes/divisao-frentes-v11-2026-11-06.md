# Divisão vigente — Codex Ryūjin, Antigravity A Quincy, B P0/Rukia

**06/11/2026 — decisão explícita do usuário.** Substitui a proposta de Antigravity A produzir VFX Ryūjin do plano v11 anterior.

- **Codex:** responsabilidade integral por efeitos e desenvolvimento produtivo da Ryūjin: fogo visual/físico na cena, lâmina, sol, luz dinâmica, shader, animação/câmera/áudio, integração e testes. O restante da bateria T0–T6 continua no [plano v11](ryujin-v11-bateria-testes-roadmap-divisao-2026-11-06.md).
- **Antigravity A:** referência, modelagem, rig, texturas, animação e integração visual Quincy/Vollständig, na nova branch `codex/quincy-vollstandig-refino-v11`, derivada de `Feature-Poderes-bankais`/`9eb6a05`. [Prompt vigente](prompt-antigravity-v11-quincy-vollstandig-refino-2026-11-06.md), com arquivos permitidos, contratos e provas.
- **Antigravity B:** P0/persistência e Rukia, fixtures e preparação documental de progressão/mentor, sem migrações nem novas recompensas. [Prompt preservado](prompt-antigravity-v11-p0-rukia-fixtures-2026-11-06.md).

Checkout A preparado: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-quincy-refino-v11`. Não reutiliza nem altera os checkouts antigos Quincy/Senbon/main. O checkout principal permanece `Feature-Poderes-bankais`, com as alterações locais Ryūjin preservadas e ainda não commitadas.

Preparação da branch não significa implementação de modelos. O handoff inclui cópias identificadas de documentação, referências e skills pertinentes; o manifesto no novo checkout registra hashes e origem. Não copiar a árvore suja nem supor que as mudanças locais do Codex já pertencem ao commit base. Arquivos locais de handoff devem ser preservados/identificados, não apagados para limpar status.

Regras para novas imagens Quincy: **uma imagem por ângulo/frame**, pares liso/em blocos, frente/costas/perfis/3/4/superior em escala coerente, detalhes/pivôs separados e cenário/VFX fora da folha de geometria. Aplicar `.agents/skills/bleach-model-pipeline/SKILL.md`. As pranchas anteriores são estudos, não aceite; manter selecionadas/rejeitadas e seus motivos. Modelo nativo corrige divergências de perspectiva/eixo da geração.

A pode avançar em paralelo à Ryūjin: a ordem 2→3→4→1 continua organizando os testes e a integração **do Codex**, mas o trabalho Quincy visual foi explicitamente antecipado para o Antigravity. Serviços/gameplay Quincy, dano/custos/voo/formas, aquisição, Ginrei e destruição do C não entram nesse refinamento.

Arquivos compartilhados (`BleachClient`, Gradle, rede, helpers globais, regressão comum) continuam com um responsável por vez; A propõe patch mínimo se precisar deles, sem editá-los por conveniência. A pode editar seus recursos e classes visuais Quincy listados no prompt. B usa testes/fixtures próprios; não toca esses módulos nem os da Ryūjin.

Prompts preparados para o usuário encaminhar; nenhuma mensagem ao Antigravity foi enviada pelo Codex. Desenvolvimento paralelo não autoriza merge/push. Entregar evidências e commits para auditoria; integrar à branch de poderes somente após autorização específica. Nuvem/editor/MCP/GPU devem ser verificados por host; dados pessoais/saves não são enviados por inferência.
