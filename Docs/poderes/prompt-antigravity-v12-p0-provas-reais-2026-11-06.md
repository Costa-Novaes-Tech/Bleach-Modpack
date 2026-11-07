# Antigravity B — corrigir testes e comprovar P0/Rukia

Continue no checkout `Bleach-Modpack-design`, branch `codex/senbon-hakuteiken-blockbench`, verificando estado antes de atuar. O commit auditado é `1839a3bb`. Não está sozinho; preserve mudanças e o arquivo Quincy não rastreado, que deve ser tratado com A, não incluído no seu commit. Sem merge/push. Código produtivo e regras de progressão permanecem somente leitura.

Leia o prompt v11 B e, para leitura no checkout principal, `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack/Docs/poderes/skills/bleach-antigravity-entrega/SKILL.md` e `revisao-ryujin-v13-e-entregas-antigravity-2026-11-06.md`. São atualizações preparadas pelo Codex, não execução/ativação presumida no seu ambiente. Escreva apenas seus testes, scripts, fixtures e documentos autorizados no v11.

## Primeiro: validade da prova

- Corrija as conclusões do relatório preservando o histórico. Compilação/check não executam automaticamente a classe GameTest adicionada. Localize os logs da execução concreta; o log encontrado pelo Codex era de 03/11, anterior à entrega. Não invente uma nova contagem nem declare falha executada sem rodar.
- No teste de relog, explique que estado é de PlayerData e que estado é do inventário do jogador. Um FakePlayer novo não é o jogador deslogado. Escolha um teste de serialização/idempotência com nome correto, restaurando o estado pertinente e comparando o delta da recompensa; relog real exige fechar/reabrir a sessão própria e verificar o mesmo jogador.
- No clone, exercite o handler/evento correto ou declare explicitamente a simulação. Capture SP e ranks após a compra inicial e exija igualdade exata, incluindo Mastery, claims e identidade da tentativa; `saldo >= 0` deixa passar perda total. Verifique o que falha se um dado for omitido, usando controle local de fixture e restaurando-a, sem alterar produção.
- Inventário cheio: observe o ItemEntity correto, quantidade e ausência de duplicação após novo diálogo; flag de quest não prova entrega no chão.
- Quincy: separe o diagnóstico de formas do reset transitório de login/clone. O teste atual chama normalize e exige um reset Quincy que essa função não realiza. Teste cada lifecycle pelo caminho real e relate o contrato racial antes de propor correção. Não implemente a proposta `quincy.base` nem altere schema/recompensas por este prompt.
- Auditor Python deve distinguir inventário estrutural de execução Java; o resultado Quincy codificado à mão é ilustração, não reprodução. Remova o caminho absoluto e declare exatamente o que foi validado.

## Depois: executar e observar

Rode as regressões/check e a suíte GameTest pertinente em diretório descartável confirmado, sem apagar locks ou saves. Preserve logs brutos, nomes dos testes, falhas/exit codes e hashes do código executado. Resolva falhas do próprio teste com base no comportamento, sem remover asserções para produzir verde. Não execute cliente/Gradle pesado junto de outra frente no host.

Só chame reinício/relog/dimensão/coop reais de aprovados depois de exercer os respectivos caminhos. Dois FakePlayers não provam duas conexões; ausência de cliente disponível deve resultar em entrega parcial, com o menor roteiro faltante. Não é necessário esconder esses gates para entregar os testes de serialização úteis.

Entregue matriz caso → entrada → asserção → falha detectável → resultado → evidência → limite. Em um segundo caso equivalente ainda não coberto, proponha e implemente a prova sem receber do Codex a asserção pronta; essa transferência ainda será avaliada. P1/P2 continuam propostas documentais e dependem das decisões humanas sobre ranks/recompensas/SP. Não crie mentoria ou quest duplicada nesta correção.
