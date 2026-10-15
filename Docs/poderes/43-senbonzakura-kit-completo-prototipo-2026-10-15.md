# Ciclo 43 — Senbonzakura: oito habilidades e protótipos de pétalas

Data: 15/10/2026. Branch `Feature-Poderes-bankais`. O usuário autorizou implementar o kit completo em uma execução prolongada, seguindo também as recomendações de parâmetros e aparência. Esta autorização sucede as propostas ainda abertas nos ciclos [41](41-byakuya-pesquisa-possibilidades-kit-2026-10-15.md) e [42](42-byakuya-cortes-campo-floral-alcance-2026-10-15.md). Valores abaixo são o balanceamento inicial implementado, sujeito ao playtest.

## Entrega e acesso

Nova identidade persistente `senbonzakura`, item `bleachmod:senbonzakura`, aba criativa, vínculo administrativo, progressão independente, quatro slots Shikai e quatro Bankai. A forma selada mantém combate normal e treino da forma; não recebe slots elementais. Não há aquisição narrativa nova: missões/mentor dependem da equipe. Item indestrutível, compatível com stamina de golpe manual, bainha e gesto já existentes.

Requisitos de domínio da forma por slot: **0/20/40/60**. Descobertas persistem como nos kits anteriores. Maestria e desbloqueios são separados por identidade; saves antigos não recebem o novo repertório completo pela migração de Ryujin/Hyōrinmaru. Schema **8** preservado: as chaves genéricas existentes comportam a identidade nova. Protocolo **2.12** pelo pacote visual adicional; cliente e servidor precisam da mesma build.

## Tabela implementada

Dano abaixo é valor bruto anterior a atributos, bônus de forma, armadura e demais regras comuns. Cooldowns contam ticks de servidor; segundos assumem 20 TPS. Custos são de reiatsu, sem taxa adicional de stamina nas técnicas.

| Forma/slot | Técnica | Execução e visual inicial | Custo | Cooldown |
| --- | --- | --- | --- | --- |
| Shikai 1 / H | Corte de pétalas | Arco horizontal de 140°, direita anatômica → esquerda, avanço em 6 ticks; até 6 blocos, dano 7, um acerto por alvo. | 10 | 60 ticks / 3 s |
| Shikai 2 / N | Corrente de pétalas | Fluxo estreito guiado pela mira durante 25 ticks, alcance 12; pulsos de dano 4 nos ticks 0/12/24; interrompido por blocos. | 15 | 100 / 5 s |
| Shikai 3 / B | Convergência de pétalas | Região apontada até 9 blocos, anel que converge por 12 ticks, pulso de raio 3/dano 9. | 20 | 140 / 7 s |
| Shikai 4 / C | Guarda de pétalas | Espiral defensiva por 60 ticks; redução de 45% contra golpes físicos/projéteis frontais elegíveis. Sem invulnerabilidade, não protege costas/magia/explosões. | 18 | 180 / 9 s |
| Bankai 1 / H | Cinco cortes de pétalas | Cinco arcos distintos, cada um de 6 ticks, início a cada 12; alcance 6 e dano 5 por corte/alvo. | 25 | 160 / 8 s |
| Bankai 2 / N | Jardim de pétalas | Casulo, explosão inicial, campo fixo de raio 10, contorno floral e segunda explosão contextual. | 35 | 400 / 20 s |
| Bankai 3 / B | Senkei | Círculo visual de 16 espadas de pétalas por 160 ticks, bônus ofensivo aditivo de 25%; reativação em Ikka Senjinka. | 25 | 240 / 12 s |
| Bankai 4 / C | Shūkei: Hakuteiken | Lâmina branca e asas de pétalas/luz durante carga de 16 ticks; estocada na direção inicial, até 10 blocos, dano 20. | 40 | 300 / 15 s |

Teclas são as configurações padrão existentes, todas remapeáveis. Senkei/Ikka/Hakuteiken são adaptações de combate, não reprodução exata de escala/animação do anime. Corte/Corrente/Convergência/Guarda/Jardim são nomes descritivos da adaptação; o jardim não é apresentado como Gōkei.

Cinco cortes: **direita → esquerda; esquerda → direita; diagonal de cima à direita para baixo à esquerda; diagonal de cima à esquerda para baixo à direita; vertical de baixo para cima**. Cada início de corte atualiza origem e orientação para o jogador poder conduzir a sequência. A direção do arco visual e a colisão utilizam a mesma geometria compartilhada. Um alvo só recebe um acerto por corte; imunidades vanilla entre acertos são respeitadas.

## Jardim fixo e reativação

- Centro capturado na ativação, mantido para os dois pulsos e o desenho do chão. Raio horizontal 10; dano limitado a diferença de altura de 3 blocos, com linha de visão do proprietário e do centro do pulso até o alvo.
- Casulo visual nos primeiros ticks; primeiro dano no tick 6, valor 6. Expansão radial e tapete de pétalas são cosméticos. **Nenhum dano contínuo durante a espera.**
- Reativação na mesma tecla, uma única vez, a partir de **100 ticks (5 s)** e antes de **200 ticks (10 s)** desde a ativação. O proprietário precisa estar dentro do raio e até 4 blocos de altura do centro, mantendo arma/forma/vínculo legítimos. Pode sair e voltar dentro da janela.
- Reativação não cobra reiatsu adicional, ignora apenas o cooldown já iniciado desta continuação. Campo floresce por **16 ticks**: contorno de cinco lóbulos dentro do raio, seguido de pulso de dano **14** e partículas ascendentes em toda a área. Não lança inimigos ao alto.
- Sem reativação, expira sem segundo pulso. Após iniciar a formação, sua conclusão pode ocorrer depois do tick 200. Novo campo continua sujeito ao cooldown inicial.
- Tapete/contorno projetados em superfícies encontradas por raycast local de ±3 blocos; regiões sem chão válido não desenham tapete, embora a seleção de dano continue obedecendo ao volume/linha de visão. Sem colocar/remover blocos, drops ou mudanças de bioma/clima.

## Senkei, alcance e ações simultâneas

Senkei permite uma reativação a partir de 20 ticks e antes de 160: **Ikka Senjinka**, sem custo adicional, consome a cena, reúne as espadas por 12 ticks e executa uma estocada de alcance 12/largura de tolerância 1,6/dano 18. Continua usando slot 3 para treino e cooldown; não constitui um nono slot independente. Hakuteiken encerra Senkei. Bônus de Senkei passa pelo cálculo aditivo de bônus de forma existente.

Passiva Bankai: **+1,5 bloco em `ENTITY_REACH`**, exclusivamente para entidades, com modificador transitório UUID único. Exige identidade vinculada, Bankai desbloqueada ativa e Senbonzakura na mão principal. Não modifica `BLOCK_REACH` nem substitui bônus vanilla/Forge do criativo. Sai ao desequipar, mudar forma/vínculo, morrer, desconectar ou trocar dimensão. A validação do ataque manual também remove bônus obsoleto antes de aceitar alcance.

Um ataque animado por dono; pedidos concorrentes de ataque são recusados antes de cobrar. Durante esse ataque, esquiva, guarda X e ataque manual são impedidos, mas movimentação normal segue disponível. Guarda X, esquiva ativa e Guard Break impedem novas técnicas pela autorização comum. Guarda de pétalas impede ativar guarda X e sua mitigação não acumula com outras reduções específicas de liberação/armadura de gelo: usa-se a maior. Usar item vanilla suspende a proteção de pétalas. Campo e cena podem coexistir dentro do orçamento visual. Não há acerto por simples aura, treino ocioso ou progresso em modo administrativo gratuito.

## Aparência, sincronização e limites

- Cinco modelos novos: selada com lâmina metálica, cabo violeta/faixas claras e tsuba dourada quadrada aberta; Shikai/Bankai com empunhadura sem lâmina; lâmina de Senkei; lâmina branca de Hakuteiken. Protótipos em geometria JSON, sem substituir modelos/texturas homologados de outras armas.
- Bainha branca, cabo violeta e guarda quadrada próprios; proporção, curva, inclinação e lado homologados permanecem. Estado público temporário da lâmina sincronizado para observadores, sem persistir carga/cena no save.
- Partícula própria de movimento controlado usa os doze sprites vanilla `minecraft:cherry_0` a `cherry_11`: pétalas rosas translúcidas, rastro curto, rotação e brilho. Hakuteiken mistura pétalas com `END_ROD` branco.
- Servidor decide fases/dano; pacote S2C carrega origem, direção, padrão e parâmetros, sem autoridade de dano no cliente. Frequência/audiência: quadros em ticks alternados de ataques/campo, aura a cada quatro ticks, observadores existentes até 64 blocos.
- Orçamento compartilhado **192 partículas equivalentes por dono/tick**, **256 emissões totais por cliente/tick** e até **32 alvos por consulta**. Quadros de arco 84, demais 96, aura 16. Camadas podem ser omitidas pelo orçamento e opções gráficas. Partículas já emitidas duram 18–26 ticks; não há promessa de FPS ou de número total constante em cena.
- Não carregar chunks para mirar, danificar ou desenhar partículas. Fluxo/estocadas respeitam blocos; explosões respeitam cobertura. Limpeza central existente remove ataques/campo/cena/guarda/alcance em mudanças de forma/equipamento/vínculo e eventos de ciclo de vida. Nenhuma entidade de pétala persistente ou terreno a restaurar.
- HUD traduzido pt_br/en_us: nomes, requisitos, custo/cooldown, prontidão/janela das continuações e bônus de alcance. O nome contextual muda para Florescer/Ikka quando disponível; a tecla do slot é a mesma.

## Arquivos principais

`common/technique/SenbonzakuraTechniqueService`, `SenbonPolicy`, `TechniqueDefinitions`, `TechniqueProgression`, `TechniqueService`, `MasteryPracticeService`; `common/visual/SenbonGeometry`; `common/network/s2c/SenbonPetalsS2C`; `client/particle/SenbonPetalParticle`, `SenbonPetalEffects`; `init/ModParticles`, `ModItems`, `ModCreativeTabs`; identidade/snapshots em `CharacterData`, `StatusData`, `SyncHelper`, `ClientPacketHandler`; integração em `CombatEvents`, `TickHandler`, `GuardService`, `DodgeService`, `BasicAttackStamina`, `ZanpakutoCarry`, limpeza Ryujin; propriedade visual em `BleachClient`, `ZanpakutoSheathLayer`, `ZanpakutoHud`; JSONs `senbonzakura*`, partícula e traduções. Testes em `SenbonGameTests`, catálogo compartilhado e `MvpRegressionTest`.

## Validação e roteiro de playtest

`check build runGameTestServer --offline --console=plain` aprovado: **86 regressões e 84 GameTests obrigatórios**, com JDK 17/Forge 47.4.10. Log local `build/cycle43-integration.log`; JAR `build/libs/bleachmod-0.2.0.jar`. Inclui os 73 cenários anteriores e 11 novos: acesso/maestria, ordem do corte Shikai, cinco acertos Bankai, corrente/convergência, guarda frontal, jardim fixo/prontidão/reativação única/expiração, Senkei/Ikka, carga Hakuteiken, alcance não cumulativo e cancelamento central.

Os testes de área novos executam em lotes individuais para não atingir alvos de outro cenário. O fixture de duelo de gelo anterior foi separado verticalmente das arenas vizinhas para testar seleção do alvo mais próximo sem interferência; lógica da habilidade de gelo preservada. Regressões acrescentadas verificam direções/diagonais, contorno/volume do campo, save por identidade, catálogo/continuações e rejeição de pacotes visuais inválidos.

Revisão adicional: cinco JSONs de modelos parseados, aliases de faces/parents e todas as texturas vanilla resolvidos; doze sprites de cerejeira encontrados no runtime local e traduções pt_br/en_us parseadas. Projeção ortográfica dos JSONs gerada e inspecionada em `build/senbon-model-preview.png`, sem representar captura em jogo. Diff sem erros de whitespace. Avisos preexistentes de depreciação ResourceLocation/Gradle permanecem. **Não houve homologação visual no cliente, medição de FPS nem sessão com dois clientes reais nesta entrega.**

Preparação administrativa em personagem Shinigami já criado, com cheats/OP:

```mcfunction
/give @s bleachmod:senbonzakura
/bleachdev zanpakuto bind @s senbonzakura
/bleachdev skill set @s zanpakuto 2
/bleachdev mastery set @s zanpakuto shikai 100
/bleachdev mastery set @s zanpakuto bankai 100
/bleachdev reiatsu free @s
/bleachdev cooldowns disable @s
```

Selecionar forma com G e carregar transformação com R como nos kits anteriores; V desce. Empunhar a nova arma. Repetir setup de maestria apenas para teste; aquisição normal segue o progresso existente. Modo gratuito suspende treino, mas preserva esperas de jardim/Ikka; desabilitar cooldown não encurta essas fases. Comandos administrativos de limpar cooldowns também cancelam efeitos ativos: executar antes de criar o jardim.

1. Em terceira pessoa, conferir tsuba/cabo/bainha; transformar e observar desaparecimento da lâmina, aura Bankai, lâmina Senkei e lâmina/asas brancas de Hakuteiken.
2. Shikai H diante de dois alvos, um à direita e outro à esquerda: frente de pétalas deve ir nessa ordem. N acompanha a mira; B converge na região apontada. Conferir bloqueio por parede e alvos atrás.
3. Shikai C: comparar golpe frontal/costas e projétil; X não acumula. Verificar retomada após expiração e custos/cooldowns em modo normal.
4. Bankai H: observar cinco direções ordenadas, dano em cortes separados, sem golpear manualmente ou esquivar no meio da sequência.
5. Bankai N em área plana: sair/voltar, esperar HUD liberar, reativar entre 5–10 s; flor e explosão devem ocorrer no centro original. Repetir sem reativar e tentar terceira ativação. Conferir terreno irregular, parede e mudança de altura.
6. Bankai B: espadas, mudança de lâmina e reativação após um segundo em Ikka. C: carga, asas e estocada frontal; não é teleportação/dash.
7. Alcance corpo a corpo contra entidade, mão vazia/troca de arma e retorno à Shikai; mineração não ganha distância. No criativo, lembrar invulnerabilidade vanilla para testar defesa.
8. Dois clientes com a mesma build: conferir observador, forma/lâmina/campo, cancelamento por morte/logout/dimensão e custo visual com várias Bankai. Homologar FPS/latência separadamente.

Próximos trabalhos: playtest e ajuste do kit entregue; depois Urahara, Uryū Ishida, Mentor/Proficiências após confirmação da equipe e revisão final de Hyōrinmaru/Tōshirō. Gin fora; Aizen adiado.
