# Auditoria dos novos retornos Antigravity A/B

06/11/2026. Principal `Feature-Poderes-bankais`/`9eb6a057`. Fontes, Git, logs e imagens existentes revisados; **sem novos Gradle/GameTests/clientes, sem mutação nos worktrees, sem commit/merge/push**. [Originais, snapshots e hashes](qa/codex/auditoria-antigravity-v12-20261106/manifest.json). Esta revisão atualiza o estado da [auditoria anterior](revisao-ryujin-v13-e-entregas-antigravity-2026-11-06.md); não apaga os defeitos antigos nem os mantém como se nada tivesse sido corrigido.

## A — Quincy: melhorou isolamento e classificação, ciclo ainda parcial

Branch `codex/quincy-vollstandig-refino-v11`, HEAD `9eb6a057`; refinamento permanece não commitado. Avanços confirmados por leitura:

- Espelhamento automático do `.bbmodel` para o worktree B removido do gerador. A cópia existente em B foi preservada.
- `--check` agora compara o `.bbmodel` por conteúdo, além de geometria/animação/texturas; imprime prefixos de hash. O Codex não reexecutou o gerador nesta auditoria.
- Saída do cliente passou a `STRUCTURAL_OK`, declarando silhueta/fluidez pendentes. Os seis registros atuais e os 46 arquivos do manifesto conferem em hash.
- Renderer oferece `DIFFUSE_ONLY`, `GLOW_ONLY`, `DUAL_LAYER`; há imagens reais dessas condições. O isolamento é uma ferramenta útil e evidencia contribuição de difusa/emissão ao contraste fragmentado.
- Retorno declara falha real de conexão Blockbench/8787; não apresenta auditoria nativa bem-sucedida. Registrar **Blockbench pendente**, sem concluir que esteja indisponível em qualquer sessão futura ou na instância Codex.

### Pendências encontradas na própria prova

1. **Endpoints ausentes, comprovados:** não existem `emergence-arc-t40.png` nem `settle-arc-t16.png`. A bancada captura usando `wait`, incrementa e termina ao atingir `maxTicks`; não chega à captura do endpoint configurado. `emergence-arc.png`/`settle-arc.png` genéricos não substituem automaticamente a pose renderizada no tick final correto.
2. **Contagem do retorno diverge dos arquivos:** GIF de abertura tem **5 quadros**, retorno **4**, ambos com 180 ms por quadro; não 6/5. Durações derivadas: 0,90 s e 0,72 s, diferentes das durações autorais 2,0/0,8 s. São amostras, sem reprodução temporal fiel nem vídeo contínuo.
3. **Empacotador ignora ausência:** só inclui PNGs que existem. O contato mantém um painel vazio rotulado como fim da abertura; primeiro quadro também exibe câmera/iluminação anteriores, diferente das amostras diurnas seguintes. Estabilizar cenário e capturar depois da renderização do estado pretendido. O pacote precisa acusar falta de evidência, não preencher rótulo sem imagem.
4. **Diagnóstico de material é promissor, mas a exclusividade não está provada:** limiares RGB em recorte com fundo não isolam automaticamente pixels da malha. As capturas sugerem participação da difusa nas regiões escuras e da emissão nas brancas; “exclusivamente” e contraste físico 25:1 exigem controle/máscara e método apropriados. Congelar mesmo tempo efetivo do clipe e câmera; depois suavizar difusa/máscara e comparar antes/depois. Ainda não houve correção artística demonstrada desse material.
5. **Regressão:** `gradlew test` não substitui o `check`/executor customizado `regressionTest`. O retorno usa sucesso de `test` para sugerir ausência de regressão; falta log novo da suíte pertinente às mudanças atuais. Os 298 GameTests verificados anteriormente continuam histórico, não uma nova execução presumida neste ciclo.
6. **Caminho produtivo:** refinamento segue demonstrado na bancada. Ativação e H/N/B/C com o renderer refinado, observador, desempenho e matriz de aparência continuam sem prova de integração. Ausência de alteração em gameplay é preservação correta do escopo, não integração visual concluída.
7. **Proveniência do pacote:** a pasta/manifesto atual agrega os GIFs e capturas históricos v11 aos artefatos novos. Hash válido não significa mesma execução ou mesma versão. Separar conjunto por ciclo ou registrar runID, tempo de captura e fonte/diff de cada grupo; preservar histórico fora da seleção que representa o ensaio atual.

**Parecer A:** aceitar como avanço de instrumentação/isolamento e entrega parcial. Não homologar o ciclo visual como completo. Próxima entrega pequena: endpoints realmente renderizados, pacote que verifica arquivos, reprodução com tempos corretos, material antes/depois, Blockbench quando acessível e caminho produtivo explícito. Não exige recomeçar todas as referências.

## B — P0/Rukia: avanço técnico comprovado no servidor, P0 global aberto

Novo commit **8e221002**, após `1839a3bb`, na branch `codex/senbon-hakuteiken-blockbench`: dez arquivos alterados, restritos a testes, scripts, fixtures e relatório; produção preservada. Arquivo Quincy não rastreado continua fora do commit.

Log real de `run-gametest/base/logs/latest.log` registra em **06/11, 19:51:26 no relógio do log**, **302/302 GameTests obrigatórios aprovados**. Classe nova compilada antes da execução; fonte tem sete métodos GameTest. O log geral não enumera individualmente cada sucesso: não inventar uma lista nominal de resultados inexistente. O Codex não reexecutou a suíte. A alegação de `check`/176 veio no retorno; guardar também o stdout/exit específico se for usado como gate de integração.

Correções confirmadas:

- Restauração simulada agora recupera capability **e inventário** no mesmo GameProfile e verifica uma Asauchi/delta zero no inventário e chão. Nome e limite do teste distinguem simulação de relog real.
- SP/rank/Mastery passaram a igualdade exata, com tentativa, alvo e checkpoint preservados. O custo100 observado no teste é contrato do código atual, não nova aprovação de tabela de preços de progressão.
- Inventário cheio verifica ItemEntity, item e quantidade, além de não duplicar no segundo diálogo.
- Normalização Quincy/sealed e reset transitório foram separados; a anomalia de normalização foi reproduzida, **não corrigida**.
- Novo caso de boss ausente chama recuperação pela Rukia e verifica nova tentativa/reinício do encontro, exercitando outro caso com asserções pertinentes.
- Script Python usa caminho relativo e removeu a emulação manual do resultado Java.

Limites restantes:

- Clone continua simulado por save/load/reset/setActiveForm; não dispara o evento Forge completo. Relog com conexão real, encerramento/reabertura de mundo, dimensão e coop com dois clientes continuam gates próprios. “Homologação P0” no título não deve abranger esses testes ainda ausentes.
- Auditor Python ainda só emite aviso para schema diferente e termina com mensagem fixa de seis fixtures validadas. Parse/hash são úteis; ele não é um validador rigoroso que rejeita violações. Próximo exercício pequeno: em cópia temporária, fixture inválida/incompleta precisa gerar falha explícita e conclusão proporcional.
- Ranks legados, tratamento das recompensas e eventual patch racial continuam dependentes de proposta/revisão de produto. Não liberar P1/P2 por inferência nem corrigir produção nesta frente documental/testes.

**Parecer B:** aceitar a melhora dos testes e a prova headless disponível como parcial de servidor. É uma evolução concreta em relação ao retorno anterior. Não equivale a liberar migração de saves, coop ou toda a progressão; commit ainda não foi integrado à branch principal.

## Aprendizado e coordenação

A aplicou parte do feedback e criou controles úteis; falta a conferência final dos próprios artefatos. B corrigiu asserções e executou prova antes ausente, demonstrando melhora no método e aplicação em outro caso. Não há comparação controlada para atribuir ganho de inteligência ou equivalência entre modelos.

O contrato de entrega recebeu a lição verificável: conferir endpoints, frames/duração reais e rejeição de arquivos faltantes; warnings não podem terminar numa aprovação incondicional. Frentes A/B permanecem próprias. O Codex continua responsável pela Ryūjin, cujo plano completo está em [ordem e método v14](ryujin-v14-plano-completo-ordem-execucao-2026-11-06.md). Nenhum prompt foi enviado automaticamente nem merge autorizado por esta auditoria.
