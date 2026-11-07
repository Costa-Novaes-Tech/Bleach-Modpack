# Revisão Ryūjin v13 e entregas Antigravity A/B

06/11/2026. Revisão de fontes, Git, relatórios, logs existentes e imagens; **sem executar novos Gradle/GameTests/clientes, sem corrigir runtime e sem merge**. Principal: `Feature-Poderes-bankais`/`9eb6a057`. O usuário avaliou que a qualidade solar melhorou muito, mas câmera, posicionamento, animação, efeitos, áudio e especialmente o encerramento continuam abertos. Esse retorno aprova a evolução, não homologa toda a feature nem sua promoção ao padrão.

## Ryūjin: o que deu resultado

- **Presença visual:** volume contínuo, núcleo luminoso e espirais substituem parte da leitura de placas/raios. Candidato g com 280 cubos/33 bones, atlas preservado, export nativo, check_model=0 e geração idempotente. Aceite humano de melhora registrado nesta revisão.
- **Composição do fogo:** lâmina, base, campo traseiro e corona têm camadas próprias; movimento por ticks reais mantém atividade na sustentação. Cotagem efetiva: máximo observado 264 cards, 192 partículas e uma luz por cena do ensaio.
- **Luz de ambiente:** controle noturno Shimmer ON/OFF demonstrou aquecimento de terreno/pilares. Material próprio sem iluminação direcional resolve outro problema: a aparência das faces solares. São duas entregas diferentes.
- **Coreografia sincronizada:** cena de 15 s, sol sustentado entre 112–176, modelo/corona/luz com centro comum e absorção em direção à lâmina. Reload deixou de reiniciar o clipe no controle testado.
- **Segurança da cena:** suspensão de novas colocações de fogo físico preserva fogo existente. Câmera anterior, cancelamento e limpeza foram verificados nos cenários específicos.
- **Instrumentação útil:** 180 regressões no ciclo de código v13; matriz g com 12 cenários executados, limpeza aprovada nos oito da transformação; três repetições sem captura. Vídeo real do framebuffer e captura OpenAL por processo dão material recuperável para avaliação, sem confundir estatística com escuta ou fluidez perfeita. As 296 provas de servidor são de ciclo anterior, não reexecução v13.

Fontes: [v12](ryujin-v12-luz-distribuicao-desempenho-2026-11-06.md), [v13 e evidências](ryujin-v13-sol-continuo-acabamento-2026-11-06.md).

## Ryūjin: falhas e retrabalho, inclusive do Codex

| Problema | Resultado e aprendizado |
|---|---|
| Orientação, pivôs e câmera avaliados separadamente | Eixos corretos no editor não garantiram uma composição correta no cliente. Mais tarde foi necessário incluir o origin `(0.5,0.51,0.5)` de GeoObjectRenderer 4.8.4 e compartilhar âncora com FX/luz. Inspecionar a cadeia completa antes de refinar muitos detalhes. |
| Material solar ajustado antes de isolar o shader | Variantes moeda/engrenagem, placas e pintura volumétrica consumiram rodadas. O emissivo vanilla ainda usa normais; apenas mudar UV/cor não eliminou contraste metálico. O controle mudando somente shader deveria ter ocorrido mais cedo. |
| Relógio autoral usado também para atividade dos FX | Ampliar sustentação deixou emissão/movimento repetitivos. Separar relógio de fase de turbulência/giro e conferir todos os cues ao mudar duração. |
| Limites e exportação | Capacidade de ArrayList não era teto; codec de projeto não era export geo. Timeout MCP podia ocorrer após gravação. Houve também overflow de atlas interrompido pela asserção. Conferir arquivo efetivo antes da etapa dependente. |
| Enquadramento e reload | Um teste matemático não descontava corretamente letterbox/alcance; parede cortou o sol. Reload mudou pose dos aros. Correções têm provas pontuais, sem garantia para qualquer sala. |
| Cleanup no limite | Uma execução manteve fogo visual por dois frames no marco de ensaio 203. Foi preservada como falha; repetição posterior não reproduziu. Faltava o relógio local no primeiro CSV, impedindo fechar a causa. |
| Perfil gráfico opcional | Shimmer/Embeddium/Oculus falhou no userdev por `FabricBlockView`; combinação com shader pack permanece aberta. Não atribuir causa exclusiva sem controle, nem instalar API Fabric por tentativa. |
| Encerramento e áudio | Selamento ainda comunica principalmente contração/desaparecimento. A câmera termina centrada no corpo; a lâmina merece composição própria. Captura/métrica de áudio e stretch local da música não entregam por si sós mixagem artística final. |

## Pendências e próximo ensaio Ryūjin

Prioridade imediata: **construir o clímax do selamento**, antes de multiplicar novos efeitos ou dependências. Proposta para storyboard/teste; não altera gameplay nem os 300 ticks neste documento:

| Ticks reais | Intenção e prova necessária |
|---|---|
| 112–176 | Preservar leitura e sustentação do sol inteiro. Não antecipar o close a ponto de perder essa conquista. |
| 176–188 | Cortina e corona cedem ao fluxo que converge na zanpakutō. Correntes curvas devem ter destino visível; evitar apenas diminuir um disco. |
| 188–202 | Aproximação frontal, deslocada para o lado da mão dominante, mirando mão/guarda/lâmina. Mostrar o último fluxo chegando antes de sumir. Simetria destro/canhoto e colisão de câmera precisam de prova. |
| 202–220 | A última linha de brasa termina até o limite 202; depois, cinza/fumaça fria limitada e pausa legível na espada carbonizada. É proposta de FX; não autoriza prolongar chamas/luz quente além do cutoff nem enfraquecer o teste de cleanup. |
| 220–270 | Nome/pose final com espada legível e respiro visual/sonoro. Conferir fundo e contraste para que lâmina escura não desapareça. |
| 270–300 | Retorno ao controle e perspectiva anteriores, sem salto através do jogador ou bloqueio adicional. |

Áudio proposto: reduzir o rugido durante a convergência, acentuar sucção curta, impacto seco do selamento e queda de massa sonora; reservar espaço à fala e à música. Testar mixagem real, não aumentar todas as camadas juntas. Confirmar direitos e versão final de 15 s antes de incluir material no JAR.

Depois: poses/empunhadura e posicionamento, regularidade dos aros/contorno, repetição dos sprites de fogo, perfil gráfico empacotado e comparação Photon/AAA, cosméticos/Better Combat/PlayerAnimator, dedicado e dois clientes. O candidato solar continua QA até promoção deliberada. São pendências, não falhas todas reproduzidas nesta revisão.

## Antigravity A: alcance real da entrega Quincy

**Pontos confirmados:** branch correta isolada, HEAD ainda `9eb6a057` (sem commit Quincy novo); geometria com 11 bones/52 cubos; manifesto dos 26 artefatos QA sem divergência de hash; log de 06/11 em seu `run-gametest/base` registra **298/298**; seis resultados de bancada GeckoLib e imagens existentes. Há execução real e trabalho aproveitável. A alegação de 176 regressões requer o log da execução específica de `check`; `test` não chama por si só nosso executor customizado `regressionTest`.

**Lacunas e não conformidades verificadas:**

1. `package_qa_evidence.py` monta cada GIF com **três PNGs de 350 ms**. São resumos de poses, não vídeo contínuo nem prova suficiente de fluidez/preparação/retorno.
2. O PASS da bancada exige raça Quincy, ausência de `failure` e 11 bones; `cubes=52 satellites=4` são texto fixo nesse registro. A geometria tem essas contagens reais, mas o PASS **não mede** contato dorsal, oclusão, direção de disparo ou qualidade visual. Não usar a etiqueta para aprovar esses critérios.
3. O novo renderer está chamado pela bancada `QuincyManifestationQa`; a busca não encontrou consumidor produtivo adicional. `QuincyVollstandigVisuals` permanece anterior. Falta comprovar a manifestação refinada na ativação e nos H/N/B/C reais. O relatório em disco chama a entrega PARCIAL, enquanto o retorno colado anuncia validação completa: manter a classificação parcial.
4. `write_assets` do gerador escreve também `Bleach-Modpack-design/.../quincy_manifestation_v11.bbmodel` quando a pasta existe. Há arquivo Quincy não rastreado nesse worktree B. É quebra da propriedade entre frentes, mesmo que seja uma cópia deliberada. Remover o espelhamento automático antes de nova execução; não apagar a cópia alheia sem verificar autoria/uso.
5. `--check` reconstrói `bbmodel` mas não o compara ao arquivo `.bbmodel` entregue. Sua mensagem “runtime & lab ... synchronized” excede o que verifica. O teste de alinhamento da formação consulta posições do servidor, sem compará-las aos satélites transformados do modelo.
6. Não localizei registro nativo MCP/check_model/export nas evidências entregues. `.bbmodel` gerado e pranchas Python não comprovam que o Blockbench foi aberto/auditado. Isso é **não comprovado**, não prova de que nunca foi usado.
7. As capturas mostram regiões brancas saturadas e escuras fragmentando a leitura das asas. Registrar como observação artística a investigar; não atribuir causa a UV, shader ou transparência sem isolar camadas.

## Antigravity B: alcance real de P0/Rukia

Commit **1839a3bb**, 11 arquivos/1377 linhas adicionadas; produção preservada, seis fixtures e cinco GameTests escritos, proposta P1/P2 documental. O inventário e o diagnóstico estático Quincy/sealed são aproveitáveis. **P0 não está liberado**:

1. O relatório diz que GameTests específicos validaram/comprovaram os casos; a entrega registra compilação, `check` e script Python, mas não execução nova deles. O `latest.log` GameTest encontrado é **03/11, 285 testes**, anterior ao commit. Não há prova suficiente da execução da nova suíte; não chamar isso de falha executada ou fabricação deliberada.
2. `relogDoesNotRepeatAsauchiRewardOrLoseAttemptIdentity` carrega `PlayerData` em outro FakePlayer, não o inventário. Depois compara seu número de espadas ao do jogador original. Pelo código, não testa a situação de relog prometida e tende a falhar quando a recompensa original é preservada corretamente. Precisa restaurar inventário/identidade ou comparar delta, conforme o nível de teste.
3. `cloneOnDeath...` simula load/reset, não o evento `PlayerEvent.Clone`, e aceita SP `>=0` após adicionar 75 e comprar atributo. Saldo zerado passaria; ranks não têm igualdade verificada. Guardar valores após a compra e exigir igualdade exata.
4. `fullInventoryDropsAsauchiSafely` verifica conclusão/flag, não a existência/quantidade do ItemEntity no chão. Isso não prova entrega segura da espada.
5. O teste Quincy liga `vollstandigActive`, chama somente `ProgressionService.normalize` e exige a flag desligada. Essa função não chama o reset de Quincy. Separar diagnóstico dos campos de forma do lifecycle de login/clone antes de afirmar reprodução completa.
6. O auditor Python imprime fixtures e um resultado Quincy codificado à mão; exit 0 não valida persistência Java nem aquele diagnóstico. Caminho absoluto também prejudica portabilidade. Reinício real, dimensão, alvo ausente e coop com duas conexões continuam sem prova.

## Capacitação que esta auditoria justifica

O problema observado envolve **abrangência das asserções, calibração das conclusões, propriedade de arquivos e avaliação visual**, além de ferramenta/contexto a verificar. Não é correto resumir como “ele não roda Gradle”; há logs de execução real. Também não há evidência para prometer equivalência entre modelos com mais regras ou MCPs.

Atualização do [contrato existente](skills/bleach-antigravity-entrega/SKILL.md), sem substituir a persona global. [Plano de capacitação](plano-capacitacao-antigravity.md) passa a usar entregas pequenas, com critérios antes de executar, demonstrando um defeito detectado pelo próprio Antigravity e uma tarefa de transferência. [Prompt A](prompt-antigravity-v12-quincy-qa-e-isolamento-2026-11-06.md) e [prompt B](prompt-antigravity-v12-p0-provas-reais-2026-11-06.md) preparados para encaminhamento humano, não enviados. Leitura/ativação dessas atualizações pelo Antigravity ainda não foi observada.

Não foram alterados os dois worktrees, o aplicativo, modelos/provedores, contas ou MCPs. Não houve commit/merge/push. [Retornos originais e snapshots da auditoria](qa/codex/auditoria-antigravity-v11-20261106) preservam o estado revisado; futuras mudanças exigem comparação com os hashes, não presumir que esta conclusão descreva uma versão posterior.
