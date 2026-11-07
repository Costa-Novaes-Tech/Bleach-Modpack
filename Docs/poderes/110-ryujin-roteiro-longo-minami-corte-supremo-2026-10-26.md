# 110 — Ryūjin: roteiro longo, Minami e preparação do C

26/10/2026. Implementação autorizada pelo usuário após revisão da gravação `Gravação de Tela 2026-10-26 080957.mp4` (62,85s). Branch Feature-Poderes-bankais, base 77211b3, inicialmente limpa. Sem checkout/merge/commit/push e sem alterar Zaraki, Senbonzakura ou o checkout de design.

## Direção aprovada e implementação

- Apresentação padrão de 800 ticks / 40s nominais; configuração cliente `ryujinLongEntrance=false` escolhe 360 ticks / 18s. Arquivo `bleachmod-client.toml` na pasta config da instância. Não há troca automática por primeira ativação: são duas durações selecionáveis pelo usuário.
- Relógio real separado do relógio de direção 0..140. Marcos longos: 0..5s preparação/ignição; 5..14s crescimento; Bankai em14s; sustentação até20s; absorção20..29s; revelação29..34s; nome em34s; retorno37..40s. O modo compacto preserva a ordem e reserva pelo menos2,5s para a fala nominal de2,24s.
- Curvas de preparação e condução da espada durante a absorção, sem mudar sua orientação externa final. Câmera frontal e retorno às três perspectivas do ciclo109 preservados. Legendas usam tempo real de fala para não ficarem esticadas pelo cenário.
- Música atmosférica original sintetizada localmente em versões40/18s, com notas graves, quintas, pulsos e camada de ruído de combustão/sucção. Sem copiar música externa ou imitar voz. OGGs das falas existentes preservados. Música no canal MUSIC, voz no VOICE; ducking nos trechos falados, fade na saída. `ryujinEntranceMusic=false` desliga a trilha. Som nativo de fogo também reduz durante a fala.
- Servidor conserva release60/commit40, custo/cooldown e vulnerabilidade atuais. Apresentação longa não concede proteção, não pausa o mundo e não prolonga busy. Movimento/ataque após60ticks reais, F5, dano e condições inválidas encerram a câmera conforme o comportamento existente; o modo40s pressupõe assistir voluntariamente. A Bankai mecânica ativa antes do selamento cenográfico; não sincronizar commit com a cena sem nova decisão de gameplay.

## Minami

- Até10 soldados, em duas fileiras desencontradas, somente com chão/colisão/chunk válidos. Em espaço obstruído pode haver menos unidades; não criar dentro de blocos para completar a contagem.
- Perfil frágil agora tem6HP máximos e atuais, não apenas vida inicial reduzida. Usa o tipo de entidade, pacote e rig vanilla de wither skeleton. Uma subclasse de servidor transitória impede o despawn automático no Pacífico; sem novo registro de EntityType e sem persistência entre sessões (orphan rejeita reloads).
- Material procedural de carvão com fissuras de magma e olhos/rachaduras emissivos, aplicado por camada de render somente aos IDs de Minami sincronizados. Esqueletos vanilla não recebem a textura.
- Direção de corpo/cabeça, formação sem alvo e animação de golpe; movimento controlado já existente permanece. Sem alegar navegação vanilla completa em obstáculos ou rig novo.
- Ataques usam o dano3 existente, atribuído ao dono, e fogo4s existente do kit Bankai. Intervalo compartilhado de20ticks por alvo preservado: dez corpos não significam dez acertos simultâneos. Não aplicar efeito Wither. Sem drops/XP; morte com cinzas/fumaça; encerramento remove soldados.

## C — Corte de Vapor concentrado

- Gesto56ticks /2,8s, impacto no40, em lugar do gesto32/impacto18. Preparação próxima ao quadril, concentração até35, corte35..40 e recuperação até56. Corpo/pernas, mão da arma e mão auxiliar acompanham a curva para ambas as empunhaduras.
- Brasas convergem para a lâmina; trilha curta no momento do corte e sons de pressão. Chamas da linha de impacto começam no quarto bloco para preservar leitura próxima à câmera; área de dano não foi removida nesses primeiros blocos.
- Câmera permanece livre. Dano, alcance, custo45, cooldown1200 e regras preexistentes de destruição de blocos preservados. Cancelar o cast antes do40 não pode causar impacto tardio.

## Validação e fechamento

- Baseline check: exit0,30s.
- Primeiro check/build: exit0,1m39s,147 regressões. Inclui validação da geração dos assets e compilação da camada/áudio.
- Primeira suíte: falhou em `ashArmyMovesAttacksAndCleansWithoutVanillaExperience` na asserção conjunta dano/fogo; contagem10, snapshot e vida passaram. Diagnóstico: alvo71,63HP/fogo0 e save de GameTests com chuva/trovão; roof local no fixture removeu a exposição à chuva sem alterar clima global ou regras de ignição. Teste mantido e suíte final aprovada.
- QA real em save descartável pelo comando abaixo; cenários dia40s, noite18s, primeira pessoa, canhoto, parede, deslocamento e cancelamentos pré/pós commit, além de Minami+C. Capturas amostradas não são vídeo contínuo nem audição humana.

```powershell
.\gradlew.bat -PbenihimeQa=true -PcinematicQaFast=true -PryujinBankaiQa=true -PqaAutoExit=true runClient --offline --console=plain
```

Fontes dos assets: [gerador local](qa/cycle110/generate_assets.py). WAVs intermediários ficam em build/cycle110-audio; assets OGG/PNG em assets/bleachmod. Nenhuma biblioteca de runtime nova. Aprovação artística e mixagem final pertencem ao playtest do usuário; multiplayer/Photon/terreno arbitrário não são presumidos.

### Resultados finais

- **Check/build:** exit0,18s,147 regressões aprovadas. **GameTests:** exit0,1m50s,231 obrigatórios aprovados após a subclasse de soldados. Sem mudanças em Hollow ou desativação de testes.
- **Cliente real:** nove cenários com432 registros selecionados das execuções pertinentes; [origens e hashes](qa/cycle110/captures.json). A sequência40s completa vem da repetição com energia de QA desativada; os sete cenários compactos/interrupção vêm da primeira rodada; Minami+C vêm da rodada final. Não confundir os totais combinados com uma única captura contínua.
- Primeiro ensaio longo revelou esgotamento da reserva100 do laboratório; custo desativado apenas no cenário de apresentação e kit. O consumo de formas em produção permanece. Nome de2,24s preservado byte a byte e janela compacta de2,7s antes do fim.
- O laboratório Pacífico eliminava os esqueletos hostis vanilla. Subclasse transitória corrigida e provada no cliente com10IDs/corpos visíveis em `client-peaceful-soldiers.log`. Rodada final usa Normal e alvo de treino imóvel para observar deslocamento. A tropa ainda converge/aglomera em alvo único; não há pathfinding avançado, escalada ou nova animação GeckoLib para soldados.
- Para a captura isolada do C, o laboratório remove a tropa no134 e aplica rotação sincronizada antes do cast140; isso evita câmera entre os aliados. Limpeza explícita só faz parte do cenário de QA, não do uso normal do C.
- Vozes preservadas; [assets/hashes](qa/cycle110/assets.json). Trilha40s: pico-9dBFS antes do ganho/ducking; análise técnica não substitui escuta. Sem advertência de som desconhecido nos trechos de validação inspecionados.
- [Verificação](qa/cycle110/verification.json), [prancha real](qa/cycle110/contact.jpg), [prévia amostrada de40s sem áudio](qa/cycle110/preview-sampled-long-no-audio.mp4). Preview usa os intervalos reais de captura, sem interpolar quadros e sem reconstruir áudio como se fosse gravação do jogo.
- JAR `build/libs/bleachmod-0.2.0.jar`, SHA256: `9dee56d3fa931aebb9e6e45b0a41940d068135e0c2db43177578ec460deb6c03`.
- Permanecem pendentes aceite artístico/auditivo do usuário, multiplayer, Photon e navegação por obstáculos variados. Sem commit/push nesta execução. Fontes novas e documentação estão locais na Feature.
