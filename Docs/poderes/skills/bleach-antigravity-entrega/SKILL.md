---
name: bleach-antigravity-entrega
description: Executar e auditar entregas do Bleach Mod no Antigravity, com contrato verificável, diagnóstico por evidência e validação real de gameplay e animações. Usar em tarefas deste projeto; não é configuração global nem promessa de capacidade do modelo.
---

# Contrato de entrega do Antigravity

## Entrada e contexto

Leia este contrato quando o prompt o indicar. Sua presença em Docs não comprova descoberta automática pelo aplicativo. Confirme os caminhos efetivos do checkout, branch, HEAD, upstream e diff antes de escrever. Não troque branch em checkout de outra conversa nem descarte alterações preexistentes.

Recupere primeiro 00-CONTINUIDADE-CODEX.md e o documento atual do assunto em Docs/poderes. Para conteúdo narrativo, consulte também o handoff GDD vigente. Documentos antigos são histórico, não decisão mais recente. Não carregue todos os relatórios de QA. Cite a origem de requisitos conflitantes e preserve decisões humanas; pergunte somente quando a resposta mudar um comportamento relevante.

Antes de implementar, registre brevemente: resultado observável; restrições; arquivos sob sua responsabilidade; hipótese e teste que pode refutá-la; critérios de aceitação. Não altere rede, registries ou serviços compartilhados sem coordenação explícita. Branch nova somente conforme autorização, baseada na Feature-Poderes-bankais atualizada. Sem merge automático por esta skill.

## Diagnóstico e execução

Separe observado, hipótese e desconhecido. Leia a cadeia real de execução antes de corrigir a camada que parece mais provável. Em animações: entrada → clip selecionado → canais ativos → corpo/braço → transformação do item → renderer → quadro final. Mão animada não comprova arma animada. Preview Python/Blockbench não comprova resultado Forge.

Faça a menor prova que diferencie as hipóteses. Depois de duas tentativas sem melhora demonstrável, interrompa a mesma estratégia e reveja hipótese, instrumento ou camada. Não compense defeito de pose com partículas, câmera ou deformação arbitrária do modelo. Preserve o que já foi aceito.

Use recursos existentes antes de adicionar dependências. MCP configurado, ferramenta disponível, chamada bem-sucedida e resultado correto são estados diferentes. Não instale bibliotecas, troque modelo/provedor ou amplie permissões apenas para parecer mais capaz.

## Animação e revisão visual

Defina marcos do movimento antes de editar: guarda, preparação, contato, passagem, recuperação e devolução do controle. Para cada marco, descreva posição/orientação da arma, contato com a mão, postura corporal, alvo e câmera. Valores ainda não definidos são hipóteses, não decisões do usuário.

Compare frente/perfil/costas e mão dominante no mesmo tempo efetivo do clip, com o mesmo estado de entrada. Não use apenas o tick global do cenário. Capture arma e braço sem VFX primeiro; depois verifique o conjunto com VFX. Reveja também os intervalos e transições: imagens isoladas não demonstram fluidez contínua.

Métricas precisam declarar unidade, conversão, referencial, geometria aproximada e limite. Distância entre segmentos centrais não prova ausência de colisão de malhas; ângulo de mandíbula não pode ser avaliado somente pela translação do bone. Telemetria deve acompanhar o ID real desde o início, sem depender da fase final que está tentando comprovar.

Não alegue ouvir áudio por log de reprodução. Se não houver escuta efetiva, entregue medições técnicas e declare audibilidade pendente. Se só puder analisar frames, não diga que assistiu ao vídeo integral.

## Validação e entrega

Teste conforme risco: build para integração; testes de comportamento para regras; Forge real para aparência e interação; dedicado/observador quando houver requisito multiplayer. Não rode toda a bateria repetidamente sem mudança ou dúvida nova. Não inicie dois clientes pesados ao mesmo tempo neste host.

Identifique branch/HEAD e, se houver alterações não commitadas, o diff exato usado no teste. Guarde comando, saída/exit code, cenário, entradas, logs e origem dos frames. Evidência deve apontar aos arquivos brutos; montagem e resumo são derivados. Retenha um conjunto compacto representativo, sem apagar provas únicas de regressões abertas.

Informe separadamente: implementado; compilado; comportamento testado; aparência observada; aceitação humana pendente. Use “parcial” ou “não observado” quando necessário. Não declare uma capacidade inteira validada a partir de um caso favorável. Não esconda falhas em contagens de testes aprovados.

Ao concluir: problema/causa sustentada, alteração, evidência, limites e instruções objetivas ao testador. Antes de commit, confira diff e exclua logs gerados e arquivos alheios. Commit/push/merge dependem da autorização da tarefa, não desta skill. Documentação nova fica em Docs/poderes; atualize o documento atual do assunto em vez de criar outro relatório repetido.

## Memória curta de correções

Registre no documento do assunto apenas decisões duráveis: sintoma → causa comprovada → correção → teste de regressão → limite. Não copie todo o chat. Em uma tarefa nova semelhante, derive a solução novamente do contrato e das evidências; não transplante ângulos, escalas ou soluções antigas sem verificar o novo modelo.
