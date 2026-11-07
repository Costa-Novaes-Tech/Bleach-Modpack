# 105 — Integração das três branches e etapa de eficiência

25/10/2026. Usuário autorizou revisar/salvar/publicar cada frente em sua branch e integrar, nesta ordem, main → Feature-Poderes-bankais, design → Feature-Poderes-bankais e Ryūjin → Feature-Poderes-bankais. Autorizou acrescentar a etapa de eficiência/limpeza ao roadmap. Não autorizou limpeza imediata; manteve a preferência por várias conversas trabalhando em paralelo, com worktrees separados.

## Estado inicial e preservação

- Checkout principal na branch codex/ryujin-bankai-sol-selamento, base c4db442, com alterações de Ryūjin, áudios e documentos103/104 sem commit.
- Checkout design na branch codex/design-poderes-criaturas, base ec3f8bc, com SenbonStage, ajustes do cliente e GameTest/documentação/logs sem commit.
- Feature-Poderes-bankais em c4db442, igual ao remoto após fetch.
- Main remota atualizada26b73b3, com checkpoint do game design M01–M10. A revisão anterior desta conversa que indicava próximo M09 estava baseada no checkout antes do fetch; o handoff recebido agora aponta M11 com retorno M05, sem converter design conceitual em runtime.
- Nenhum processo Java/Javaw ativo no inventário inicial. Sem reset, stash, force-push, remoção de diretório ou commit na main.

## Commits e publicação das frentes

- Ryūjin: **1a30f87**, feat: add Ryujin Bankai sun sealing cinematic. Fontes, duas falas OGG, especificação103 e relatório104 preservados. Branch publicada com upstream origin/codex/ryujin-bankai-sol-selamento.
- Senbonzakura: **c130a5a**, feat: add Senbonzakura spiritual floor and petal backdrop. Fontes do palco, cliente, teste e logs/documentação preservados. Publicada em origin/codex/design-poderes-criaturas.
- O GitHub informa redirecionamento de GabrielCNovaesDev/Bleach-Modpack para Costa-Novaes-Tech/Bleach-Modpack; push foi aceito. Configuração do remote não foi alterada incidentalmente.

## Três merges na Feature-Poderes-bankais

1. **56321c5** — origin/main26b73b3 incorporada; documentação da equipe preservada integralmente.
2. **9bdcdc1** — design/Senbonzakura c130a5a incorporada.
3. **e6bef95** — Ryūjin1a30f87 incorporada.

Todos com commits de merge explícitos, estratégia ort, sem conflitos textuais. Isso não garante compatibilidade funcional: validação conjunta é registrada abaixo. As branches de origem e o checkout design permanecem disponíveis para as outras conversas. Não foi feito merge de volta para main.

## Roadmap

Adicionada a **etapa8: eficiência e limpeza**, no relatório72. Abrange espaço, duplicação, retenção de QA, fontes de autoria, builds/caches, caminhos de ferramentas e coordenação entre IAs. Não substitui os sete itens anteriores nem remove worktrees. Nenhum arquivo foi apagado.

## Validação combinada

- Check/build offline aprovado, exit0, 32s, **147 regressões**.
- GameTests offline em run-gametest descartável: exit0, 1m01s, **229 testes obrigatórios aprovados** (223 anteriores +1 palco Senbonzakura +5 Ryūjin).
- [Log do check](qa/cycle105/check.log), [GameTests](qa/cycle105/gametest.log) e [manifesto](qa/cycle105/verification.json).
- JAR integrado: `build/libs/bleachmod-0.2.0.jar`, SHA-256 `ad808800c420b54c31e3ed77e5d121a4e1b59ffb3fe611aaea3573debf4110ed`.
- Sem execução gráfica nova nesta rodada: pose, efeitos, escuta e performance da Ryūjin continuam para playtest; as capturas Senbonzakura anteriores são evidência da sua branch, não nova prova do conjunto. Photon, servidor dedicado com clientes e multiplayer não revalidados.
- Os merges não exigiram correção de código. O commit documental posterior acrescenta roadmap/relatório; não muda o JAR validado. Logs rotativos produzidos pelos testes foram preservados em build/cycle105-generated-root-logs (ignorado) e as versões rastreadas foram restauradas, evitando publicar ruído de execução.
