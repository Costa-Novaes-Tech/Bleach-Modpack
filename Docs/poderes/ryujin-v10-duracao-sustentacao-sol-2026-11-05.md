# Ryūjin v10 — tempo de sustentação do sol

**05/11/2026, `Feature-Poderes-bankais`, base `9eb6a05`.** Pedido do usuário: dar mais tempo de exposição ao sol com a sequência de transformação mais completa. Preservar v9 como referência histórica e o trabalho dos outros checkouts. Sem commit/merge/push.

## Diagnóstico e decisão

A v9 chegava ao sol completo no tick **112** e começava a absorvê-lo no **116**: apenas **4 ticks / 0,2 s** de sustentação. O número de frames desenhados não determina a duração; é necessário aumentar esse intervalo no relógio real. A mudança acrescenta **60 ticks / 3 s** somente à sustentação. Total passa de **240 para 300 ticks (12 → 15 s)**, com sol completo por **64 ticks / 3,2 s**. Preparação, close/fala “Bankai”, explosão e formação mantêm a velocidade anterior; não se aplica câmera lenta à sequência inteira.

| Marco | v9 | v10 |
|---|---|---|
| “Bankai” / commit | 40 / 2 s | 40 / 2 s |
| Explosão | 70 / 3,5 s | 70 / 3,5 s |
| Sol completo / panorama frontal | 112 / 5,6 s | 112 / 5,6 s |
| Começo da absorção | 116 / 5,8 s | **176 / 8,8 s** |
| Fim da absorção / selamento | 142 / 7,1 s | **202 / 10,1 s** |
| “Zanka no Tachi” | 160 / 8 s | **220 / 11 s** |
| Retorno de câmera | 210–240 / 10,5–12 s | **270–300 / 13,5–15 s** |

O panorama mantém o mesmo eixo frontal, altura, FOV e posição enquanto o sol sustenta. Aros continuam girando; em seguida, a absorção conserva seus 26 ticks e aceleração próprios. Pose permanece firme e chamas continuam sendo animadas pelo cliente. A v9 e suas capturas de 12 s continuam arquivadas, sem relabelá-las como evidência de 15 s.

## Implementação e dependências de tempo

- `RyujinBankaiTimeline`: novo mapeamento real, relógio autoral 0..140 preservado. As duas preferências históricas usam o mesmo roteiro de 15 s.
- Áudio de absorção e selamento e ganho do fogo usam os marcos compartilhados; não permanecem presos a 116/142. Fala inicial e explosão não mudam de momento.
- O renderizador GeckoLib tinha uma saída fixa no tick 142. Agora deriva o término do sol do fim da absorção, **202**, evitando desaparecer no meio da sustentação.
- Novo clipe [`ryujin_bankai_sun_v10.animation.json`](../../src/main/resources/assets/bleachmod/animations/ryujin_bankai_sun_v10.animation.json), 15 s: inserção da sustentação com continuidade angular dos aros e deslocamento dos keyframes posteriores. Clipe original preservado. As curvas não lineares são amostradas na fronteira inserida; a reprodução em jogo é o gate visual.
- [Gerador](../../tools/models/lab/codex/ryujin01/build_scene_v10.py) cria o clipe e dois projetos de laboratório separados: [`cena v10`](../../tools/models/lab/codex/ryujin01/ryujin_bankai_sustained_sun_v10_lab.bbmodel) e [`pose v10`](../../tools/models/lab/codex/ryujin01/ryujin_bankai_sustained_sun_v10_lab_pose.bbmodel). Não sobrescreve v9. A geometria não muda.
- QA passa a terminar depois do novo fim e captura também sustentação, absorção e retorno nos novos ticks.

## Gameplay e áudio

Não foi aumentada a duração da forma Bankai em combate. O ajuste é da **transformação/apresentação**. Movimento é liberado no tick 60, commit/custo único no 40; atacar/usar técnica continua encerrando a apresentação segundo as regras existentes. **A proteção de ativação já acompanha a cena**, portanto seu máximo também passa para 300 ticks, cancelável por ação de combate. Isso é registrado explicitamente para revisão de balanceamento, sem alterar outras Bankais.

A faixa musical candidata fornecida pelo usuário tem 12 s e **não foi alongada, repetida nem acelerada/desacelerada**. Ela pode terminar antes da devolução da câmera de 15 s; validar a mixagem e preparar uma versão musical mais longa ou uma extensão instrumental depois. Fogo ambiente e cues continuam separados. Nenhuma escuta artística foi simulada.

## Validação desta revisão

Resultados finais e caminhos das capturas são registrados abaixo após a execução. Não reutilizar o resultado 176/295 ou os frames t112/t160 da v9 como prova do novo roteiro.

- Regressão: relógio inversível/monotônico, sustentação com raio completo e sem absorção, FOV panorâmico, posição estável do clipe e giro dos aros.
- GameTest: expiração/proteção no novo fim, sem prolongar release nem duplicar custo.
- Cliente real: conferir sol ainda visível entre t112 e t175, absorção t176–202, voz t220 e controle devolvido no fim. Vídeo contínuo com áudio, observador e matriz ampliada continuam gates separados.
- A primeira tentativa de abrir v10 aguardou uma permissão de arquivo; após a autorização pontual do usuário, a cena v10 de 15 s abriu na ponte Codex/8788, com 244 cubos e 66 bones. **`check_model`: 0 problemas; `analyze_animation`: 0 problemas**, com preparação e retorno mensuráveis e pés estáveis. Conferência nativa realizada para v10, sem herdar o resultado de v9.

Inspeção no Minecraft: [meio da sustentação, t144](media/ryujin-duration-v10-20261105/screenshots/frame-t144.png) e [fim da sustentação, t175](media/ryujin-duration-v10-20261105/screenshots/frame-t175.png) mostram o sol inteiro atrás e giro dos aros com o mesmo panorama. [t202](media/ryujin-duration-v10-20261105/screenshots/frame-t202.png) já não mostra o sol; [t300](media/ryujin-duration-v10-20261105/screenshots/frame-t300.png) mostra HUD/câmera devolvidos e Bankai preservada. Ainda há fogo visível nos quadros finais; esta revisão de duração não encerra a auditoria da origem/limpeza dessas chamas nem o acabamento visual.

Diagnóstico da abertura do editor: diálogo nativo “Plugin Permission” pedindo `access and change files on your computer`. Usuário autorizou novamente **uma vez**, somente na instância Codex, e a abertura foi confirmada; nenhum acesso permanente foi concedido pelo Codex. As capturas [nativa t144 / 7,2 s](media/ryujin-duration-v10-20261105/native-t144.png) e [nativa t175 / 8,75 s](media/ryujin-duration-v10-20261105/native-t175.png) mostram sustentação e giro em arquivos separados. O enquadramento automático do editor deixa margens grandes; a composição final é a do cliente Minecraft. [Estrutura](media/ryujin-duration-v10-20261105/native-model-check.json) e [análise de movimento](media/ryujin-duration-v10-20261105/native-motion-analysis.json) arquivadas. A validação técnica não substitui aceite artístico do usuário.


## Resultados executados

**177 regressões aprovadas** no check/build final; **295/295 GameTests obrigatórios** no mundo QA descartável, incluindo expiração no novo fim. Logs: `build/visual-lab/v10-duration-client.log` e `build/visual-lab/v10-duration-validation.log`. O cliente `day_back` concluiu com exit 0/`BUILD SUCCESSFUL`, `releaseBusy=false`, `scene={}` e forma Bankai preservada.

Capturas e telemetria desta execução: [manifesto v10](media/ryujin-duration-v10-20261105/manifest.json), [frames.jsonl](media/ryujin-duration-v10-20261105/frames.jsonl). Não são vídeo contínuo nem comprovação de áudio escutado.
