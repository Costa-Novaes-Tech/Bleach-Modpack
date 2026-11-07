# 107 — Revisão e integração do refinamento Ryūjin

25/10/2026. Usuário entregou retorno do Antigravity e pediu verificar e, se possível, integrar na branch de poderes. Referência inicial: checkout principal limpo em codex/ryujin-bankai-sol-selamento, commit6ddf420, um commit à frente do remoto. Feature-Poderes-bankais em b002b7b.

## Entrega revisada

Commit6ddf420 altera sol/absorção, câmera de cinco atos e espelhamento, fala nominal OGG, legendas/ducking e asserções de GameTest; relatório106 e continuidade acompanham a entrega. Duração140ticks e contrato de gameplay não foram retimados pelo refinamento. A revisão foi de fonte/diff/documentação; não equivale a inspeção gráfica ou escuta.

O checkout design estava em c130a5a com mudanças locais em ZanpakutoSheathLayer, SenbonAtmosphere, SenbonCameraPath e SenbonzakuraPlayerPose. Essas alterações ainda em andamento não foram incluídas, revertidas, commitadas ou modificadas nesta rodada.

## Integração

Merge6ddf420 → Feature-Poderes-bankais concluído em **da73e58**. Único conflito textual em Docs/poderes/00-CONTINUIDADE-CODEX.md, pois as duas frentes acrescentaram avisos no início. Preservados os registros105 e106; nenhum conflito de código. Main e worktree design preservados.

## Limites encontrados na revisão

- O relatório106 associa frequência/autocorrelação a uma voz externa e a fonemas específicos. Essas medidas isoladas não identificam locutor/palavras nem certificam que a edição preservou a fala; exigir escuta do resultado. Atenuação e máscara implementadas são tratamento proposto, não restauração auditivamente homologada.
- Expressões como “100% legível”, ponta sem corte e ausência de clipping não são demonstradas pelos asserts de distância/FOV. Conferir sol, fitas e a lâmina real no cliente, destro/canhoto e com cenário.
- A curva da câmera ainda termina em alvo fixo atrás da âncora/FOV70. Sua coincidência com a perspectiva/configuração real anterior não é garantida para primeira pessoa/F5 frontal, rotação ou movimento; testar a transição final e registrar eventuais saltos. Não foi feita correção incidental dessa arquitetura durante o merge.
- Há pequenas diferenças entre intervalos descritos no relatório e os efetivos no código (detalhe até66, abertura66–74). Fonte/timeline vigente devem orientar o playtest, não uma garantia verbal de fluidez.
- O número228 do relatório106 pertence à branch Ryūjin isolada. A integração deve validar também o teste do palco Senbonzakura, sem excluir casos para igualar contagens.

## Validação integrada

- Check/build offline: exit0, 31s, **147 regressões aprovadas**.
- GameTests offline: exit0, 1m10s, **229 obrigatórios aprovados**, incluindo o palco Senbonzakura.
- [Check](qa/cycle107/check.log), [GameTests](qa/cycle107/gametest.log), [manifesto](qa/cycle107/verification.json).
- JAR integrado build/libs/bleachmod-0.2.0.jar, SHA-256 `b21b215d07a884cbcfd7d41a0d6aa8d7d1a085aa514f1e1d951eecd2b4b4c642`.
- Sem cliente gráfico/escuta, Photon ou multiplayer novos. Resultado pronto para playtest, não homologação artística.
- Logs rotativos gerados preservados em build/cycle107-generated-root-logs e originais rastreados restaurados.
