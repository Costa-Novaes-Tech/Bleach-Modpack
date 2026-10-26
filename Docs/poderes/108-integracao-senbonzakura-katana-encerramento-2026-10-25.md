# 108 — Senbonzakura: katana única e encerramento integrado

25/10/2026. Usuário pediu revisar, commitar e integrar a segunda entrega do Antigravity para testar junto da Ryūjin. Checkout principal inicialmente limpo na Feature-Poderes-bankais5f8ff78; checkout design em c130a5a com alterações locais da Senbonzakura. Nenhum processo Java/Javaw ativo no inventário inicial.

## Revisão e salvamento

Revisados diff de cliente/câmera/pose/bainha, envelopes do palco e asserções do GameTest. Logs do checkout design confirmam224 GameTests obrigatórios aprovados e ClientQa final com BUILD SUCCESSFUL em5m38s. O relato descreve7 cenários; foram conferidas amostras de metadados e capturas específicas, não uma nova execução gráfica nem vídeo contínuo.

Captura do tick10 diurno mostra katana vertical no lado da mão principal, sem a segunda lâmina/bainha do problema anterior. Captura da primeira pessoa no tick286 mostra HUD, mira e arma restituídos. Telemetria pós-retorno mantém perspectiva/posição nos marcos amostrados; não certifica interação livre com mouse/teclado ou continuidade de todos os frames anteriores.

Entrega salva em **d20cc3d**, na branch codex/design-poderes-criaturas. Inclui fontes, teste, seção10 do relatório101 e logs novos do Antigravity. Nenhum arquivo de outras frentes incluído por conveniência.

## Integração

Merge na Feature-Poderes-bankais em **af090b1**, sem conflitos textuais. Preservados Ryūjin6ddf420/integração107 e demais poderes. Main e configuração dos worktrees não alteradas.

Mudanças principais:

- Item vanilla e bainha ocultos na janela de katana cenográfica única; mão/queda alinhadas e restauração no264.
- Dissolução progressiva de lâminas, fundo220–276, penumbra220–278 e piso224–280; cena continua290ticks.
- Retorno da câmera244–286 para perspectiva original, FOV incluindo modificadores de gameplay; pose termina284, HUD retorna278.
- Iluminação de apresentação do personagem e redução dos anéis rosa, sem alterar clima/custos/commit40/trava60.

## Validação e entrega

- Check/build offline: exit0, 31s, **147 regressões aprovadas**.
- GameTests offline: exit0, 57s, **229 obrigatórios aprovados**, incluindo Ryūjin e palco Senbonzakura.
- [Check](qa/cycle108/check.log), [GameTests](qa/cycle108/gametest.log), [manifesto integrado](qa/cycle108/verification.json).
- [Katana única no10](qa/cycle108/design-single-katana-t010.png) e [primeira pessoa no286](qa/cycle108/design-first-person-t286.png): amostras preservadas do QA anterior da branch design, com [metadados/hash/origem](qa/cycle108/source-captures.json). Não são capturas novas da integração.
- JAR build/libs/bleachmod-0.2.0.jar, SHA-256 `a9514a6172bc393348a2e74d9b097dba0285d4eadc49247c7ece8e67f82e3c01`.
- Qualidade visual/escuta e sensação do encerramento continuam para playtest; sem validação nova de Photon, multiplayer ou cliente gráfico integrado.
- Logs rotativos produzidos nesta rodada preservados em build/cycle108-generated-root-logs (ignorado) e originais rastreados restaurados. Nenhuma limpeza do projeto nesta entrega.
