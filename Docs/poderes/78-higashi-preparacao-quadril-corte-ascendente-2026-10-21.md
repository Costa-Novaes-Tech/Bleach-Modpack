# Ciclo78 — Higashi: quadril, manejo da katana e corte ascendente

21/10/2026, Feature-Poderes-bankais. Estado inicial com alterações dos ciclos76/77 não commitadas; preservadas. Usuário rejeitou o posicionamento/movimento do corte, sem revogar o gatilho de morte confirmada. Revisão com skill bleach-mod-dev. Sem checkout, commit, push ou merge.

## Diagnóstico e correção

A rotação anterior de preparação (`arm.z≈-1`) colocava a mão no lado oposto do tronco no referencial local. O rastro especial também desenhava uma diagonal independente, da esquerda para a direita, em vez de seguir a katana. Mudança de câmera ou mais partículas não corrigiria essa divergência.

- Preparação nos ticks6–10: braço da espada com roll positivo, mão baixa no quadril do mesmo lado, ponta avançada e baixa; apoio/torso participam do preparo.
- Passagem no tick12 e final no16: mão e lâmina passam da posição exterior baixa para dentro/acima, com braço subindo e rotação de tronco. Gestos de primeira pessoa também têm preparação baixa e levantamento.
- Normal e execução compartilham as mesmas chaves até16, evitando mudança de pegada no instante da confirmação. Execução segura o final até56 e recupera até88. Pausa de40ticks após o final do corte preservada.
- Rastro amostra três posições recentes da própria lâmina durante o arco, com chamas/faíscas contidas. Não traça um segundo golpe independente. Cálculo é aproximação local do rig, não leitura exata de cada pixel/bone em tempo de render; inspeção gráfica continua necessária.
- Mão esquerda espelhada e retorno a pose neutra cobertos pela regressão. Bainha continua na cintura: não confundir a peça escura atrás do quadril com a katana em movimento.

**Preservados:** morte confirmada como único gatilho da cena longa; execução≤7% ou dano normal letal; dano, custo, recarga, acerto no12, aproximação e colisões; cura/aliados/loot; câmera lateral e ignição; demais golpes Ryūjin e Zaraki. Rede2.29/schema9. Nenhuma alteração no kit Benihime, que está apenas em [planejamento de reformulação](79-proposta-reformulacao-benihime-selada-shikai-bankai-2026-10-21.md).

## Cliente real e evidências

```powershell
.\gradlew.bat runClient -PbenihimeQa=true -PcinematicQa=true -PcinematicQaFast=true -PhigashiPoseQa=true --offline --console=plain
```

Flag nova somente de laboratório, no save novo restrito Benihime-QA-68/singleplayer/admin/desenvolvimento. Primeiro caso registra preparação em primeira pessoa; limiar usa terceira pessoa traseira antes do impacto para inspecionar o quadril, sem ligar a câmera de execução antes da confirmação. Depois a apresentação passa à câmera lateral normal da execução. Casos sobrevivente/sem alvo permanecem em primeira pessoa com execução inativa. Captura por tick; relógio monotônico evita regravar ticks de uma correção de tempo. No jogo normal, a flag não interfere.

Quatro cenários de vida e câmera passaram: letal=true, limiar=true, sobrevivente=false e sem alvo=false para câmera; mortos apenas os dois primeiros. [Preparação/passagem/final](qa/cycle78/preparacao-corte-marcos.jpg), [limiar amostrado](qa/cycle78/threshold/amostras-sem-audio.mp4), [origens e hashes](qa/cycle78/capture-manifest.json), [resumo](qa/cycle78/capture-summary.json). Clipes são quadros amostrados sem áudio, não gravação contínua; intermediários JPEG para encoder, PNGs selecionados preservados.

Nos marcos inspecionados: preparação baixa vista de trás; mão avança e a lâmina termina apontada para cima junto ao alvo no enquadramento lateral. O primeiro cenário ainda tem carregamento inicial de chunks e fogo/toast de laboratório. Avaliação do agente (0–5): correção do lado do quadril4, legibilidade da lâmina3, continuidade entre normal/execução4. **Pendente de aceite visual do usuário**, especialmente a passagem rápida em terceira pessoa e a sensação da mão em primeira pessoa. Não afirmar fidelidade perfeita ao estudo só pela geometria. Sem novo ensaio de mão esquerda, dois clientes, terreno irregular ou áudio; espelhamento coberto por teste.

## Verificações

Linha de base `check --offline --console=plain`: 132 regressões, exit0,24s; última suíte155 GameTests anterior é do ciclo77, não reexecutada como baseline. Primeiro check após correção: **133 regressões**, exit0,40s. O novo teste exige mão no quadril correto e ponta indo de altura<0,5 a>1,9 com avanço e cruzamento lateral, além da mesma curva até16 em normal/execução. Outros testes de pose/câmera/limiar mantidos.

**Base final:** `check build runGameTestServer prepareCombatTestPack --offline --console=plain`, exit0, **133 regressões/155 GameTests**,2m09s. **Photon separado:** `-PphotonPilot=true check build runGameTestServer prepareCombatTestPack preparePhotonTestPack --offline --console=plain`, exit0, **133 regressões/155 GameTests**,1m37s. Persistem os diagnósticos recuperados conhecidos do scanner/editor LDLib no dedicado; não significam homologação gráfica do editor. Sem dois clientes ou Photon gráfico nesta rodada.

[Log base](qa/cycle78/validation/validation-cycle78-base-final.log), [log Photon](qa/cycle78/validation/validation-cycle78-photon-final.log), [cliente](qa/cycle78/validation/validation-cycle78-client.log), [manifesto de pacotes](qa/cycle78/delivery-manifest.json). Cinco/sete JARs, CRC e identidade do mod nas duas pastas/build/libs conferidos. `git diff --check` aprovado. Logs gerados na raiz restaurados; mudanças anteriores e documentação em Docs/poderes preservadas. O cliente QA foi encerrado após a sequência; não foi deixado para teste manual. Proposta Benihime não implica técnica nova disponível nesta entrega.
