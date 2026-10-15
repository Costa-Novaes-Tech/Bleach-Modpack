> Homologação posterior: usuário confirmou Senbonzakura/Byakuya concluídos e prontos no [ciclo 46](46-homologacao-senbonzakura-proposta-urahara-2026-10-15.md). A próxima frente é a discussão de Urahara.

# Ciclo 45 — Dash da Hakuteiken, jardim intenso e Senkei em área

Data: 15/10/2026. Branch `Feature-Poderes-bankais`. Após playtest positivo do [ciclo 44](44-senbonzakura-vento-prisao-hakuteiken-2026-10-15.md), o usuário aprovou especialmente o visual da Hakuteiken e pediu três ajustes: acompanhar sua estocada com dash mais longo, jardim mais intenso com raio 35/janela 2–6 segundos, reativação da Senkei atingindo a área inteira. Visual aprovado preservado; novos movimentos/parâmetros aguardam conferência em jogo. Esquiva/defesa homologadas, Aizen adiado. Próximo personagem é **Urahara, para discussão de kit**, sem implementação autorizada por este ciclo.

## Hakuteiken: o personagem acompanha a estocada

- Carga de **16 ticks / 0,8 s** preservada, com lâmina branca, asas e halo. Origem visual acompanha o jogador durante a preparação; direção horizontal é capturada no lançamento e permanece fixa.
- Depois da carga, avanço nominal de **16 blocos**, em **10 ticks / 0,5 s**, **1,6 bloco por tick**, substituindo a estocada instantânea de 12 blocos. Cada atualização divide a trajetória em passos de até **0,2 bloco**, validando o volume completo do corpo contra colisões, borda do mundo e chunks carregados antes de mover.
- Movimento calculado no servidor e sincronizado ao dono pela correção vanilla de posição, que também atualiza a posição aceita pela conexão. Não receber destino do cliente nem atravessar obstáculos para completar a distância. As asas acompanham o avanço; o traço branco cresce sobre o trecho já percorrido.
- Dano bruto **20**, no máximo uma vez por inimigo por execução, durante a passagem do jogador. Consulta varre os segmentos percorridos, com tolerância lateral 1,25 e validação de cobertura/linha de visão/alvos. Não aplicar antecipadamente todo o dano ao fim da carga nem atingir quem estiver além dos 16 blocos. Limite compartilhado de segurança: até **32 alvos distintos** por execução; aliados, NPCs de missão e demais exclusões continuam protegidos.
- Parede/colisão, limite do mundo, chunk indisponível, montaria, sono, elytra, cancelamento ou deslocamento externo superior a três blocos desde a última posição interrompem o avanço. Morte/logout/dimensão/perda de forma/arma/vínculo usam a limpeza central. Remover movimento horizontal ao concluir/cancelar; não deixar impulso residual.
- Sem invulnerabilidade, voo, custo adicional de stamina ou alteração de esquiva. Custos/cooldown continuam **40 reiatsu / 300 ticks**. Guarda X, ataque manual e esquiva seguem impedidos durante a execução; funcionamento criativo preservado.

O deslocamento é horizontal. Obstáculos podem reduzir os 16 blocos nominais. A sensação de movimento/correção vanilla e o acompanhamento visual precisam do playtest real do usuário, sobretudo com latência; os testes de servidor não medem suavidade de câmera.

## Jardim: mais pétalas, raio 35 e encerramento em seis segundos

| Parâmetro | Ciclo 44 | Ciclo 45 |
| --- | --- | --- |
| Raio fixo / diâmetro | 40 / 80 blocos | **35 / 70 blocos** |
| Início da reativação | 100 ticks / 5 s | **40 ticks / 2 s** |
| Prazo final | 200 ticks / 10 s | **120 ticks / 6 s** |
| Tapete | 64 pétalas a cada quatro ticks | **128 a cada dois ticks**, quatro vezes a emissão nominal por segundo |
| Flor | 64 por quadro | **128 por quadro** |
| Explosão radial/subida do jardim | 36 por quadro | **112 por quadro**, usando perfil próprio |

Janela de reativação: idade **>= 40 e < 120 ticks**, uma única vez, com proprietário ainda dentro do campo e equipamento/forma/vínculo legítimos. Sem reativação, o estado termina no tick 120; não há pulso final automático nem dano contínuo na espera.

A formação da flor continua tendo até **16 ticks**, mas é encurtada quando a reativação ocorre perto do limite: `min(16, 120 - idade - 1)`. No último tick válido, finaliza imediatamente. Assim, uma reativação aceita não perde a explosão por ficar sem tempo e o campo não permanece ativo depois dos seis segundos. Pétalas já emitidas continuam sua vida cosmética curta; isso não prolonga dano ou direito de reativar.

Centro permanece fixo no lançamento, inclusive após o jogador se deslocar. Dano bruto inicial/final **6/14**, cobertura, altura útil de três blocos, reativação incluída no custo e cooldown inicial de 400 ticks preservados. Retorno ao campo dentro da janela continua permitido.

Tapete agora ocupa também uma faixa de ar até 1,2 bloco acima da superfície, e a expansão inicial tem mais variação vertical. Guarda, cortes, aura e prisão mantêm as emissões suaves do ciclo 44; o reforço foi destinado ao jardim. Limites compartilhados passam a **256 emissões equivalentes por dono/tick no servidor** e **256 novas partículas por cliente/tick**, mantendo orçamento único entre camadas. Sobreposição e opções gráficas podem reduzir camadas. Nenhum bloco é colocado/removido; partículas não carregam chunks.

## Senkei / Ikka: reativação em 360 graus

O problema relatado vinha da execução anterior: Ikka chamava a mesma seleção de estocada frontal da Hakuteiken. A reativação agora usa **pulso circular completo de raio 14 blocos**, centrado na posição da reativação, após os 12 ticks de reunião das lâminas. Acerta frente, lados e costas, independentemente da direção da câmera.

Dano bruto **18**, uma aplicação por alvo, altura útil de três blocos, até 32 alvos por seleção, respeitando cobertura sólida, linha de visão e exclusões/aliados. Não atravessa paredes para simular um acerto em toda a área. Cena Senkei continua com raio visual 7, bônus ofensivo e espadas em duas fileiras; o ataque de reativação tem seu raio próprio de 14.

Visual de Ikka também mudou: 32 lâminas orientadas radialmente para dentro avançam da borda da área ao centro, seguidas de expansão de pétalas em 360 graus. Removido o feixe exclusivamente frontal dessa continuação. Espera de um segundo para reativar, duração Senkei de oito segundos, custo adicional zero, consumo da cena e cooldown do slot 3 preservados.

## Compatibilidade e arquivos

Protocolo **2.14**, schema persistente **8**. A alteração de protocolo garante que todos usem os novos perfis de intensidade e a geometria radial; cliente/servidor precisam do mesmo JAR. IDs, progresso por forma/identidade e modelos de espada/bainha não foram substituídos.

Arquivos: `SenbonPolicy`, `TechniqueDefinitions`, `SenbonzakuraTechniqueService`, `SenbonPetalsS2C`, `SenbonPetalEffects`, `SenbonConstructGeometry`, `Reference`, traduções pt_br/en_us; testes em `SenbonGameTests` e `MvpRegressionTest`. Documentação limitada a `Docs/poderes`.

## Validação

`check build runGameTestServer --offline --console=plain`: **91 regressões e 94 GameTests obrigatórios aprovados**. Log local `build/cycle45-validation.log`; artefato `build/libs/bleachmod-0.2.0.jar`.

Cinco cenários novos: varredura da Hakuteiken acertando alvos próximos/intermediários/finais uma vez sem dano antecipado; interrupção por parede sem dano atravessando cobertura; cancelamento em pleno avanço sem deslocamento/dano posterior; Ikka atingindo frente/lado/costas e preservando alvos fora da área/protegidos; reativação tardia do jardim concluindo antes do prazo. Cenários existentes atualizados para movimento de 16 blocos, raio 35 e reativação a partir de dois segundos/expiração em seis. Regressões adicionais conferem intensidade por perfil e geometria radial nos quatro quadrantes.

Revisar em jogo: câmera e deslocamento da Hakuteiken em primeira/terceira pessoa, colisão e continuidade das asas; jardim reforçado em terreno plano/irregular, reativação aos dois segundos e perto dos seis, expiração sem reativar; Ikka com inimigos ao redor e atrás de cobertura. Verificar observador com outro cliente e opções gráficas. Não afirmar homologação visual nova, FPS ou latência a partir dos testes automatizados.

Após a conferência desses ajustes, retomar com o usuário a **discussão de Urahara**. Mentor/Proficiências aguardam equipe, Gin segue removido e revisão de Hyōrinmaru permanece ao final do roadmap.
