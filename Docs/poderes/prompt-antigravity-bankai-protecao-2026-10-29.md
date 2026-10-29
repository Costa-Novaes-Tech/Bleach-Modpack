# Antigravity — proteção durante ativação de Bankai

Regra de entrada obrigatória: após ler tudo, executar git fetch origin, comparar sua branch com a própria origin, origin/main e origin/Feature-Poderes-bankais e, com trabalho local preservado, executar git merge --no-ff origin/Feature-Poderes-bankais NA branch individual. Se já integrada, comprovar por ancestralidade. Implementar somente depois desse passo; nunca inverter o sentido do merge.

Leia integralmente este prompt, antigravity-frentes-2026-10-29.md e skills/bleach-antigravity-entrega/SKILL.md, todos em Docs/poderes. Execute primeiro a comparação e o merge poderes→sua branch exigidos no documento de coordenação. Só então implemente. Você não está sozinho: preserve alterações Codex/outras frentes e respeite propriedade de arquivos. Branch exclusiva codex/ryujin-protecao-ativacao, em checkout individual existente.

## Resultado aprovado

Ao ativar uma Bankai, o jogador fica temporariamente invencível durante sua ativação/apresentação. Contato de mob, empurrão, dano recebido e dano automático causado pela própria transformação não podem cancelar a cena Ryūjin. Não é proteção durante toda a duração da forma Bankai. Morte/remoção administrativa, desconexão, troca de dimensão e invalidação explícita da ativação encerram estados de forma segura.

## Fase1 — inventário e contrato

Leia ReleaseAnimationService, RyujinBankaiEntranceService/Client, testes RyujinBankaiGameTests e ciclos Benihime, Senbon, Hyorinmaru e Nozarashi. A release Ryūjin dura60ticks, commit40, mas apresentação240: proteger apenas busy60 não atende à cena. Liste início/fim reais de proteção por identidade, inclusive apresentações que sobrevivem ao commit. Zaraki já aceita: adaptar à proteção existente sem refatorar sua cena/pose/áudio. Não reabrir Hyorinmaru/Benihime visualmente.

Defina estado transitório autoritativo no servidor, término máximo e encerramento antecipado. Não depender somente de câmera cliente, flag NBT persistida ou invulnerable=true sem restauração. Preservar invulnerabilidade externa preexistente. Se saída voluntária libera a câmera/controle, proteção precisa terminar coerentemente; não permitir combate livre protegido depois de abandonar cena. Explicar como o servidor identifica essa saída sem confiar em duração arbitrária enviada pelo cliente.

## Fase2 — correção

Bloquear dano no ponto apropriado do fluxo Forge para evitar perda de vida e efeitos de acerto incidentais. Não aplicar cura posterior para simular imunidade. Não consumir totem, nem alterar armadura/guard/stamina como se o golpe tivesse acertado. Distinguir fonte de dano automático de entrada de ataque voluntária. Remover cancelamento indiscriminado RyujinBankaiEntranceService.hurt e hurtTime cliente conforme novo contrato, preservando cancelamentos válidos e restauração da câmera/áudio.

Cobrança/commit ocorrem uma vez. Interrupção não reverte Bankai já efetivada, não reembolsa/duplica recursos e não deixa efeito ou proteção órfãos. Proteção não deve ser renovada por spam de tecla nem concedida quando pedido de transformação falhar.

## Fase3 — QA e entrega

GameTests: mob/projétil/fogo/queda durante ativação, dano imediatamente após término, encerramento antes/depois do commit, troca de item/kit/dimensão, logout, reentrada e pedido inválido. Verificar proteção no trecho visual posterior ao tick60, totem não consumido, flag não persistida e imunidade externa preservada. Teste antigo que esperava cancelamento por dano deve mudar conscientemente; não apagar cobertura.

Forge real: ativar Ryūjin com slimes próximos e fogo/passiva atingindo mobs, observar cena completa e retorno; testar saída deliberada. Verificar no mínimo smoke test das outras Bankais afetadas pela política comum. Servidor dedicado/observador: registrar executado ou pendente, nunca inferir do solo. Não alterar visual/áudio aceitos para mascarar falha.

Entregar SHA de integração/base, diff focado, testes com exit code, motivo/tick de início/fim da proteção e capturas/trecho real. Sem merge final para poderes/main. Não afirmar ausência de regressões universais; indicar cobertura efetiva.
