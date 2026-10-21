# Ciclo 80 — Benihime: fios independentes, Costura Final e ações da manifestação

21/10/2026, branch Feature-Poderes-bankais, referência b398fdc. Implementação autorizada pelo usuário após discussão79. Skill bleach-mod-dev aplicada. Alterações anteriores dos ciclos76–79 preservadas, sem checkout, commit, push ou merge. Zaraki, Higashi e a cena/áudio de entrada Benihime preservados nesta rodada.

## Contrato implementado

### Shikai
- H Nake e N escudo conservam os contratos existentes.
- B Shibari mantém diâmetro20/raio10, expansão20ticks, contração100ticks, captura no chão e contenção agressiva homologadas. Nós visuais nas bordas reforçam a leitura da malha; nenhuma antecipação de captura ou alteração no cronômetro.
- C Hiasobi **sem rede** passa a lançar cinco fios cortantes curvos por uma faixa à frente, até12blocos, dois contatos nos ticks6/16 de dano base7 cada. Lentidão breve12ticks, custo25, recarga200ticks. Não instala armadilha. A faixa tem tolerância lateral1,5 em torno do segmento, somada à caixa do alvo, e respeita visibilidade/colisão.
- C Hiasobi **com rede ativa próxima** preserva a detonação coletiva no raio atual, contenção e dano base14 no tick16. Não depende da mira sobre cada inimigo nem da visibilidade do dono até todos os presos. A visibilidade da área parte da própria rede. A rede é consumida na ativação.

### Bankai — H/N/B/C

**Diretriz de balanceamento confirmada pelo usuário:** Costura Final deve ser a técnica de maior dano do kit Benihime por execução completa. O contrato atual já atende em dano base total:30, contra14 da Hiasobi e12 da Dissecação. Preservar essa hierarquia nas próximas alterações; dano efetivo depende de defesa, progressão e permanência do alvo nos pulsos. Isso não significa maior dano por segundo ao repetir habilidades durante toda uma janela de recarga.
- H **Dissecação:** mesmo ponto até16blocos/raio4; dois cortes base6 nos ticks12/24. Exposição20% por60ticks renovada no contato. Custo25/CD140. A soma base continua12, mas defesa e imunidade são processadas separadamente em cada contato; não prometer dano final idêntico em todos os alvos.
- N **Reconstrução:** preserva três pulsos de2HP, alvo aliado válido ou usuário, alcance12/custo28/CD240. Mãos se recolhem num gesto de reparo, com fios ligando a manifestação ao beneficiário. Regeneração pessoal e aura aliada preservadas.
- B **Abertura de Caminho:** transferida de C para B, mantendo três cargas, custo30 por uso e recarga260 após a terceira. Até12blocos, inclusive vertical. Busca destinos seguros em passos de0,5 até1bloco, em vez de rejeitar todo percurso menor que3. Ao terminar o gesto de6ticks, processa imediatamente a travessia, sem depender da cadência global de4ticks do corredor. Revalida colisões no instante da travessia, chão no horizontal, chunks/borda/altura. Não atravessa blocos sólidos nem escava. Corredor para aliados permanece3s.
- C **Costura Final:** suprema original adaptada, não nome canônico atribuído à obra. Campo fixo raio8, selecionado até12blocos e ajustado ao chão como a Shibari. Aviso inicial12ticks, quatro incisões base5 a cada12ticks e ruptura final base10 no tick60: total base30 em3s. Custo50/CD560ticks(28s). Benihime ergue as mãos, tensiona fios e fecha o gesto sobre o campo. Círculo costurado, nós ascendentes e ruptura distinguem esta ação do deslocamento.
- Costura Final não altera blocos, não executa instantaneamente e não força deslocamento. Aplica lentidão breve com intensidade menor em jogadores. Seleção limitada a64 alvos hostis por pulso, sem dano em aliados/pets protegidos pelo contrato comum; revalida visibilidade a partir do campo. Afasta-se mais de24blocos, desequipa, morre, troca forma/dimensão ou perde estado válido: cancela o restante. Custo/recarga já pagos não são devolvidos.

A antiga Reestruturação corporal de B deixa de ser habilidade ativa; seu progresso é arquivado/migrado. O kit selado continua como proposta79: esta autorização priorizou fios e reformulação Bankai, sem confirmar os quatro Kidō/equipamentos sugeridos para a selada.

## Animação, ferramentas e compatibilidade
Usados recursos existentes: rig procedural detalhado, IK de braços de comprimento fixo, dedos articulados, vértices nativos e gestos do jogador compatíveis com PlayerAnimator. Sem instalar bibliotecas/ferramentas novas. A boneca move mãos, braços, ombros e tronco com curvas por habilidade; fios alcançam o campo sem esticar artificialmente os braços. Cada gesto retorna ao repouso e o acompanhamento continua. A cortina/entrada e as gravações fornecidas não foram refeitas.

QUEEN transmite alvo/fase da ação a cada2ticks; cliente interpola o tempo entre amostras. Fios autônomos e campo têm tipos próprios de frame, orçamento existente de4frames por dono/tick e leases curtas. Protocolo **2.30** exige cliente/servidor iguais. Photon/LDLib permanece opcional; essencial funciona no perfil base.

Persistência: schema9 conservado, techniqueRevision3. Benihime passa de chaves numéricas a IDs estáveis: antigo Bankai4→path agoraB; antigo Bankai3→final_stitch agoraC; Shikai1–4 seguem suas técnicas. Maestria/desbloqueios e chaves antigas arquivadas são preservados, sem somar valores indevidamente na repetição da migração. Outras identidades não mudam de layout.

## Verificação e evidências
Baseline antes das alterações: check/runGameTestServer offline, exit0;133 regressões/155 GameTests. Primeira rodada:133 regressões, duas falhas de fixtures antigas (Caminho ainda emC; expiração da exposição antes do segundo corte). Corrigidos os contratos; não removidos os casos. Rodada seguinte:135 regressões/156 GameTests, exit0.
Base final: check/build/runGameTestServer/prepareCombatTestPack, exit0, **135 regressões/158 GameTests**. Inclui Hiasobi autônoma e parede, combo radial, suprema/aliados/aviso, cancelamento por desequipar, Caminho horizontal/vertical/novas paredes/três cargas e migração de progresso.

**Photon final separado:** -PphotonPilot=true check/build/runGameTestServer/prepareCombatTestPack/preparePhotonTestPack, exit0, **135 regressões/158 GameTests**,1m19s. Diagnósticos recuperados do scanner/editor LDLib no servidor permanecem; não se confundem com GameTest falhando. Base final1m18s. Pacotes finais com5/7JARs, CRCs válidos e SHA256 do mod idêntico a build/libs: [manifesto](qa/cycle80/delivery-manifest.json).

**Cliente Forge base real:** runClient -PbenihimeQa=true -PcinematicQaFast=true -PbenihimeCombatQa=true, exit0,3min incluindo carga/preparo/captura. Save novo de laboratório, seed681019, plano, dia, cheats/criativo, distância6, FOV padrão70°, gráficos modo1, máximo120FPS, framebuffer1280×720. Treze cenários: selada/Shikai/Bankai melee (três), primeira pessoa Shikai, Nake, escudo, Shibari, Hiasobi com rede, Dissecação, Reconstrução, Caminho, Costura Final e Hiasobi autônoma. Cada cenário88quadros nos ticks28–115: **1144 capturas reais**. [Lista e origem](qa/cycle80/all-scenarios.json), [seleção](qa/cycle80/contact-final.jpg).

Logs confirmam execução/gesto e acertos: Dissecação1000→979,9264; Costura Final1000→955,72; Hiasobi autônoma1000→983,46875 no Husk da bancada. Valores efetivos incluem personagem/defesas e não substituem os valores base do contrato. Cura não causa dano ao alvo-laboratório. O teste gráfico não mede cura de aliado nem todas as três cargas em sequência; esses contratos passaram no servidor. Câmera/HUD da bancada restaurados ao concluir; cliente QA encerrado pelo fechamento normal da própria janela.

Parecer visual do agente: gestos distintos nas mãos/braços, conexão até alvo/campo, alvo reagindo aos cortes e leitura da suprema conferidos no cliente. Notas0–5: participação corporal4, leitura dos fios4, retorno ao repouso3, detalhe dos dedos a distância3. Enquadramento amplo mostra a área e toda a manifestação; ainda exige revisão próxima dos dedos/contato e julgamento artístico do usuário. Nenhuma alegação de fidelidade perfeita ao anime. Não houve nova inspeção da entrada completa, terreno irregular, mão esquerda ou duas perspectivas simultâneas nesta rodada. Mãos são IK/curvas procedurais, não animações novas exportadas de GeckoLib.

Os contadores amostrados de FPS durante os sete cenários selecionados variaram de75 a119: [manifesto das capturas](qa/cycle80/capture-manifest.json). São leituras do cliente durante screenshots, sem benchmark controlado/frametime ou nova coleta de hardware; não provar ausência de custo de desempenho. A suprema foi o mínimo75. Photon gráfico/editor e servidor com **dois clientes reais continuam pendentes**; GameTest dedicado não equivale a sincronização observada em duas máquinas.

Clipes abaixo são **sequências amostradas do framebuffer, 20FPS/4,4s, sem áudio**, e não gravações contínuas ou avaliação auditiva. Originais selecionados em [frames](qa/cycle80/frames); todos os1144 originais permanecem nos diretórios run-benihime-qa registrados no manifesto, fora do controle de versão.
- [Hiasobi autônoma](qa/cycle80/hiasobi-standalone-sampled.mp4)
- [Dissecação](qa/cycle80/dissection-sampled.mp4)
- [Reconstrução](qa/cycle80/reconstruction-sampled.mp4)
- [Abertura de Caminho](qa/cycle80/path-sampled.mp4)
- [Costura Final](qa/cycle80/final-stitch-sampled.mp4)

[Log base](qa/cycle80/validation/benihime80-base-final.log), [Photon](qa/cycle80/validation/benihime80-photon-final.log), [cliente](qa/cycle80/validation/benihime80-client.log); primeira falha e repetição preservadas na mesma pasta. Cinco MP4 decodificados sem erro. git diff --check aprovado; logs gerados na raiz restaurados ao estado inicial, preservando as alterações anteriores. Sem instalações novas. **PENDENTE DE VALIDAÇÃO VISUAL DO USUÁRIO**, com aprovação técnica e gráfica de laboratório nos limites descritos.

## Bateria manual recomendada
1. Shikai C sem rede: fios acertam alvos próximos e distantes à frente; parede impede o dano.
2. Shikai B mirando o chão e C: contração/alcance20 preservados e todos os presos recebem a detonação.
3. Bankai H/N: observar dois cortes e mãos de reparo; conferir cura própria/aliada sem perder a identidade da entrada.
4. Bankai B: usar três vezes em horizontal/subida/descida; conferir travessia e recarga após a terceira, com cooldowns de teste desligados.
5. Bankai C: aviso, incisões e final na área, sem acertar aliado; caminhar, mudar a mira e desequipar durante a técnica para conferir campo fixo/cancelamento.
6. Combate básico/primeira pessoa e transformação: procurar disputa de animações, dedos deslocados e retorno ruim ao repouso. Feedback visual do usuário continua necessário.

O item4 do roadmap permanece em validação, não encerrado por build verde. Uryū, Mentor/Proficiências e Hyōrinmaru/Blockbench mantêm a ordem obrigatória; depois NPCs de combate/quests. Aizen adiado, Gin removido.
