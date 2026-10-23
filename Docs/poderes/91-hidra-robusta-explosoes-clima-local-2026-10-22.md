# Ciclo 91 — hidra robusta, impactos explosivos e clima local

22/10/2026. Branch Feature-Poderes-bankais, referência inicial c26a4e3. Implementação autorizada pelo usuário a partir do plano90. Preservadas as alterações documentais pendentes90; sem checkout, commit, merge ou alteração do conteúdo Zaraki.

## Contrato aplicado

- Pescoços50% mais grossos na base e no trecho intermediário, afunilamento proporcional, bases mais largas e transições sobrepostas. Altura das cabeças em espera aumentada de13,8/11,4 para16,6/13,7 blocos, aproximadamente20%. Cabeças preservam a escala; nova sustentação sob o crânio.
- Três cabeças/pescoços saem por trás do jogador. Avanços escalonados com impacto aos24/34/44 ticks de vida da manifestação de ataque. Não há curva de retorno: cada ponta permanece no ponto atingido e desaparece, com desintegração dos segmentos da ponta para a base nos12 ticks seguintes.
- Alvo ou região determinados pela mira no início do ataque. Prioriza inimigo visível no corredor da mira; em seguida superfície apontada; sem alvo/superfície, usa alcance nominal32. A região fica fixa após a invocação, sem perseguição indefinida.
- Três pontos de impacto projetados no terreno e limitados por paredes, sincronizados na entidade juntamente com uma máscara de resolução. Observador tardio recebe os pontos/estado, sem recalcular acerto ou dano no cliente.
- **Explosão por cabeça:** raio6, dano-base8. Teto por alvo/invocação18 de dano-base nas sobreposições; não soma os antigos contatos4+4+10. Multiplicadores de forma/atributos, armadura e invulnerabilidade normais continuam vigentes. Um alvo pode receber menos que esse teto; nenhum iframe é removido para forçar acerto.
- Nenhum dano de contato durante a preparação/travessia. Dano e controle ocorrem somente nas explosões do servidor, com política compartilhada de aliados/PvP e linha de visão a partir do impacto. Uma resolução por cabeça, sem repetição por frame, segmento ou partícula. Controle mantido em60 ticks, amplificador1 para jogadores/alvos resistentes e3 para os demais.
- Custo30 e recarga12s doN preservados. Raio6/dano8/teto18 são parâmetros desta primeira implementação para playtest, não homologação de balanceamento pelo usuário.
- Resíduos locais: placas/cristais visuais, neve e estilhaços. Cristais crescem radialmente após o impacto e dissipam em aproximadamente5s. Amostragem do chão e paredes para o visual; sem colocar, remover ou destruir blocos. Orçamento de partículas/entidades e superfícies em cache limitado pelo renderizador.

## Clima e transformação

A Hyōrinmaru deixou de chamar a aquisição periódica de clima global. O método que ligava Bankai ao weather foi removido; C e passivas não o reativam. A neve/atmosfera locais continuam funcionando sem exigir chuva ou bioma frio. Não existe novo comando global de tempo limpo.

BlizzardWeatherData permanece como suporte à recuperação de estados de versões antigas e seus testes de restauração; nenhum executor do kit atual adquire uma nova lease. Limpeza legada respeita o mecanismo anterior de reconhecimento do estado/alteração por comando. Não remover indiscriminadamente o arquivo salvo nem apagar chuva natural.

Passivas de resistência15%/30%, habilidades H/B/C e os kits selado/Shikai preservados. A transformação mantém formação/baforada/revelação, não usa as explosões doN. Câmera de revelação agora aproxima o jogador entre156–184 ticks, depois da neve, antes de devolver o controle aos184–200. Modelo também mais robusto na transformação.

## Arquitetura e recursos

Rede **2.34**: novos dados sincronizados de impactos exigem mesma versão em todos os clientes/servidor. Schema geral9 e revisão de técnicas4 preservados; sem nova migração de proficiências.

HydraMotion separa progressão de avanço e desintegração; IceDragonEntity define mira, pontos, máscara, explosões e teto por alvo. IceDragonRenderer aplica os mesmos pontos na malha. HydraImpactVisuals renderiza gelo transitório com consulta de superfícies em cache. Modelo/fonte Blockbench regenerados por tools/models/ice_hydra.py e hydra_organic.py. Nenhuma nova ferramenta/dependência instalada; perfil Photon continua opcional.

## Verificação

Linha de base aprovada:139 regressões e173 GameTests. A primeira bateria com a nova coreografia/clima passou140/175; adicionados depois mira/terreno e teste do ponto de impacto apontado. Baterias finais: **140 regressões e176 GameTests**, aprovados em base e Photon/LDLib; `check`, `build`, pacotes e validação do gerador concluídos. Após essas baterias, somente apresentação/QA receberam ajustes de máscara, cache e espera do servidor, conferidos na compilação/cliente. Nenhuma homologação artística é presumida por build ou GameTests.

[QA e capturas reais](qa/cycle91/README.md), [verificação estruturada](qa/cycle91/verification.json). Cenários de cliente: transformação, asas/flores, H/N/B/C Bankai, Sennen Shikai, Revestimento selado, N com parede e N em chão irregular. O roteiro passou a esperar o término pelo servidor, com ticks do servidor nos metadados: a primeira captura revelou que o encerramento anterior podia antecipar o fim do último cenário. Essa primeira execução não é a evidência final.

As animações de evidência são GIFs derivados dos PNGs do framebuffer Forge, amostrados a cada quatro ticks, sem áudio ou interpolação. Não são vídeos contínuos nem artes conceituais. Nenhum FFmpeg novo foi instalado; a conversão usa Pillow já disponível. FPS registrado é amostragem durante captura, não benchmark de frametime ou prova de capacidade multiplayer.

## Pendências que permanecem

- Avaliação do usuário das proporções, peso das cabeças, explosões e novo enquadramento; refinamento de dentes, chifres/texturas e aparência da katana/mãos.
- Revisão completa de poses de combate, asas/cauda e identidade individual das demais técnicas conforme89; esta rodada prioriza o contrato90 e a câmera de revelação.
- Observador em servidor dedicado/dois clientes reais, latência e entrada tardia; avaliação auditiva humana e conforto visual.
- Acabamento opcional Photon autorado, sem torná-lo obrigatório.
- A lista obrigatória72 continua aberta; não avançar para NPCs/quests de lore como se essas pendências tivessem sido encerradas. Zaraki preservado, Aizen adiado, Gin fora da fila.

## Entrega para playtest

Captura final completa:10 cenários em cada perfil,294 PNGs no base e292 no Photon. As sequências finais aguardaram o servidor e alcançaram108 ticks no último cenário; a armadura selada foi acompanhada até140. Animações GIF amostradas e metadados ficam em qa/cycle91. No cenário irregular, o alvo oculto pelo relevo permaneceu protegido; não considerar esse resultado uma falha de dano ou prova de que a habilidade atravessa terreno. O traçado é conservador diante de obstáculos.

JAR: build/libs/bleachmod-0.2.0.jar. Pacotes: build/combat-test-pack (5 JARs) e build/photon-test-pack (7), com o mesmo mod. SHA256: `0f3ff0fbbcdf42c9d731b80e35b298972daf2346eeb9c9886b53d31f510ad2e4`. ZIP íntegro; recursos conferidos com as fontes sem divergências. Sem commit.
