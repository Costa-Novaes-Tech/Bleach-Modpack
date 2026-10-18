# Ciclo 49 — Shibari sobre o solo e Hiasobi em toda a rede

Branch `Feature-Poderes-bankais`, base limpa `aad0385`. Playtest recebido: a rede afundava quando lançada perto dos pés; Hiasobi atingia apenas parte dos inimigos; solicitado reforço da contenção sem alterar a velocidade de contração. Dimensão aprovada continua **20 blocos de diâmetro**.

## Correções

**Posição no chão:** o ponto da Shibari é projetado sobre a superfície de colisão próxima à mira, com margem de 0,1 bloco. A malha deixou de usar deslocamento vertical de -0,8: agora fica ligeiramente acima dessa âncora. Assim, mirar nos pés não enterra a malha nem a origem dos testes de cobertura. Se não houver superfície próxima, mantém o ponto aéreo da mira. Consulta apenas chunks carregados.

**Área e direção:** captura e explosão usam consulta radial própria, em 360 graus, com raio horizontal e tolerância vertical de três blocos. A seleção vem do centro da rede, sem depender da orientação ou visão direta do lançador. Cobertura sólida entre a rede e cada alvo continua protegendo-o; aliados, NPCs protegidos e regras de PvP permanecem respeitados. Limite de 64 alvos mantido.

**Hiasobi / tecla C:** ao ativar, consome a rede e guarda seu **raio atual**, em vez do raio fixo 4. A área fica fixada durante os 16 ticks de ignição, continua contendo os capturados e acende fios por toda a malha. Ao explodir, consulta todos os inimigos elegíveis presentes nessa área naquele instante, não apenas um alvo mirado ou a lista de capturados. Se usado cedo, cobre quase os 20 blocos de diâmetro; se usado tarde, cobre a rede já contraída. Uma detonação por rede, dano bruto 14, custo 25 e cooldown 10 s mantidos. Não causa dano a blocos.

**Contenção mais firme:** Lentidão V, cancelamento do impulso horizontal de fuga e correção da posição até 0,55 bloco/tick, em subpassos de no máximo 0,15 com colisão vanilla. Ganho de correção aumentado de 0,30 para 0,75; isso reduz a folga do alvo contra o laço. A posição desejada ainda deriva da mesma malha em contração: abertura de um segundo, contração de cinco segundos e raio mínimo 1,4 preservados. Perder a visão do lançador deixa de soltar o alvo. Teleporte para fora da margem de segurança, morte, aliança ou cancelamento encerram a contenção. Paredes impedem o arrasto físico; não há passagem forçada através de blocos.

## Validação e playtest

Rede **2.17**, schema **8**, JAR local `build/libs/bleachmod-0.2.0.jar`. Clientes e servidor devem atualizar juntos. Log local `build/cycle49-validation.log`.

Novos cenários reproduzem o problema com piso de pedra real e mira vertical nos pés, mobs em quatro quadrantes a oito blocos e um fora do raio. Outro teste detona a malha com o lançador olhando para fora e atrás de uma parede externa, verificando os quatro alvos periféricos e proteção de aliado/alvo externo. O teste de fuga aplica deslocamento horizontal contrário ao puxão e verifica contenção sem acelerar o relógio de contração. Regressão de geometria verifica que os vértices da rede não ficam abaixo da âncora.

Para playtest: mirar nos pés e no chão alguns blocos à frente; conferir a malha visível. Colocar mobs ao redor, pressionar C cedo e conferir dano em toda a borda; repetir após contrair. Virar de costas antes de C não deve mudar quais regiões são atingidas. Conferir colisões/pequenos desníveis e a sensação de contenção em outro jogador elegível. A validação automatizada não substitui essa avaliação em cliente real.


Validação final: **check/build, 98 regressões e 113 GameTests obrigatórios aprovados**. Log `build/cycle49-validation.log`. Inclui piso real/mira nos pés/quatro quadrantes, dano radial com lançador sem visão, proteção de aliado/alvo fora da área e resistência ao deslocamento de fuga. Malha acima da âncora verificada por regressão. Ainda requer conferência visual e de sensação da contenção no cliente.
