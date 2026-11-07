# Prompt — Antigravity A: VFX autorais isolados da Ryūjin

> **SUPERADO em 06/11/2026 — não executar este prompt.** O usuário atribuiu toda a Ryūjin ao Codex e mudou a frente A para [Quincy/Vollständig na nova branch isolada](prompt-antigravity-v11-quincy-vollstandig-refino-2026-11-06.md). A frente B P0/Rukia permanece. Conteúdo abaixo preservado somente como histórico; suas permissões de arquivo não ampliam o escopo atual.

Você vai produzir os assets VFX de laboratório da transformação Ryūjin. **Você não está sozinho no repositório:** preserve alterações, branches, worktrees e instâncias de editor das outras conversas. Não reverta arquivos alheios. Este prompt é preparação de trabalho; não foi despachado automaticamente pelo Codex.

Leia primeiro `Docs/poderes/00-CONTINUIDADE-CODEX.md`, `ryujin-v11-bateria-testes-roadmap-divisao-2026-11-06.md`, o relatório v10 e as skills do projeto pertinentes. Confira Git/status/HEAD/worktrees e versão exata Forge 1.20.1/47.4.10. Use checkout/branch própria acordada; não faça checkout, reset, stash ou merge sobre árvore ocupada. Nunca comite na main. O v10 ainda está local: se os documentos/assets faltarem no seu checkout, peça o pacote ou reporte a ausência; não presuma que `9eb6a05` contém o roteiro novo.

## Responsabilidade exclusiva

- Fontes, texturas autorais, projetos e exports de laboratório em `tools/vfx/lab/antigravity/ryujin11/`.
- Documentos/manifestos em `Docs/poderes/antigravity-ryujin11-vfx-*`; evidências em `Docs/poderes/qa/antigravity/ryujin11-vfx/`.
- Não editar Java produtivo Ryūjin, `BleachClient`, Gradle raiz, rede, timeline, áudio, proteção, quests, saves pessoais ou a regressão compartilhada. Codex fará a integração produtiva.
- Blockbench do Antigravity, se disponível e próprio: porta 8787. Não alterar instância/perfil Codex 8788. Verifique a capacidade real do host; cloud não tem automaticamente o MCP/editor/GPU local. Não ampliar permissões de plugin por conta própria.

## Resultado esperado

Criar versões comparáveis de três camadas: **revestimento contínuo da lâmina**, **cortina traseira ligada ao solo** e **coroa do sol**, com variação e absorção. O problema atual é pouco fogo na arma/atrás e concentração acima do corpo. Não resolver apenas multiplicando um emissor na cabeça.

Relógio obrigatório: 300 ticks/15 s; fala/commit 40, explosão 70, sol completo 112, sustentação até 176, absorção 176–202, nome 220, retorno 270–300. Não alongar fala nem alterar gameplay. Fogo físico ficará suspenso durante a cena pela integração Codex, retomando depois; seus exports são apresentação, sem dano/blocos/hitboxes.

Âncoras de entrada: `actor`, `bladeHand`, `bladeTip`, `sunCenter`, direção e seed da cena. Manter emissão ao longo da lâmina, inclusive para canhoto; cada asset deve declarar sua origem e escala. Blockbench: frente −Z, direita do ator +X, atrás +Z; 16 unidades de modelo equivalem a um bloco quando aplicável. O runtime transforma esses eixos; não fixar um eixo global como se todo jogador olhasse ao norte.

## Execução

1. Conferir ferramentas disponíveis. Photon **1.1.17 + LDLib 1.0.42** e AAA Particles **2.3.4 Forge**, autoria Effekseer, são os candidatos fixados. Não usar API/documentação NeoForge 1.21 no Forge 1.20.1. Não instalar outro conjunto só por catálogo; instalações pré-autorizadas continuam limitadas ao laboratório e fontes oficiais.
2. Autorizar três camadas separadas com preparação, ação, sustentação animada e dissipação. Guardar fonte editável e export. Testar um backend por vez; nenhum evento deve duplicar Photon e AAA. Se um editor/backend não estiver operacional, documentar capacidade e limitação, entregar a outra fonte como parcial e não inventar export/execução.
3. Usar mesmo volume, paleta quente, duração e âncoras nas alternativas. Lâmina revestida em pelo menos 80% do trecho cortante, menor fogo junto à mão; frente abaixo da cintura; cortina sustentada atrás por todo o sol completo. Variar altura/fase, mantendo rosto e arma reconhecíveis. Giro dos aros é do modelo; não duplicá-lo num segundo sol de partículas.
4. Movimento/seed de emissão acompanha tempo real da cena, independente de FPS; progresso de fase acompanha o relógio autoral. Guardar quotas de instâncias, taxa de emissão, vida máxima e materiais. Não criar uma fonte de luz por partícula; a integração de luz é do Codex.
5. Reproduzir os exports reais em um cliente/harness de laboratório já autorizado e isolado do seu checkout. Descobrir comandos/APIs existentes; não inventar comandos nem substituir o asset por geometria reescrita. Evitar criar outro projeto Forge inteiro quando o laboratório existente bastar. Se não conseguir carregar, entregar erro bruto e interface mínima de integração, sem alterar arquivos compartilhados.
6. Capturar frente e perfil nos ticks 70,112,144,175,190,202; vídeo contínuo para sustentar alegação de movimento. Cada imagem em arquivo separado. Testar start/stop/replay e limpeza; guardar originais, SHA-256, duração, versões, perfil, comandos, exit codes e limitações. Medição só vale no hardware realmente usado.

## Entrega

Manifesto com arquivos fonte/export, âncora/unidade, duração, seed, limites e regra de destruição. Comparativo honesto: produzido, parseado, carregado no cliente, visto em vídeo ou pendente. Não apresentar menu carregado como A/B visual concluído. Conferir licença dos backends e autoria dos assets; não copiar código/texturas/áudio de BleachCraft/XERJ. Preferência do projeto GPL-3.0 não equivale a autorização MIT de XERJ.

Informe diff e commits **da sua frente**, sem incluir alterações alheias. A integração, escolha final de backend e aceite artístico ficam com Codex/usuário. **Não fazer merge, push, publicação nem envio externo automaticamente.** Ao terminar, atualizar skills pertinentes apenas com lições comprovadas e de forma aditiva, preservando alterações existentes.
