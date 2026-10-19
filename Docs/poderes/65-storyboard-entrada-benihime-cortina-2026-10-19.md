# Ciclo 65 — entrada Benihime: cortina, revelação e fios

> Prévia visual refinada no [ciclo 66](66-previa-benihime-abertura-central-penumbra-2026-10-19.md): passagem central, presença contínua na penumbra, novas camadas e efeito do olhar. Arquivos 65 preservados para comparação.

19/10/2026. Branch `Feature-Poderes-bankais`, HEAD `ca9a067`. Documentos do ciclo 64 já estavam no índice ao começar; preservados. Pedido atual autoriza storyboard, roteiro, prévia animada e novo estudo de aparência da manifestação. **Foco exclusivo na entrada/transformação Benihime. Zaraki permanece aprovado e não pode ser alterado.** Ajustes futuros de Higashi, dash/Rajada Ryūjin e kit Benihime ficam registrados para depois desta entrada.

## Material produzido

- [Galeria com vídeo, capítulos, velocidades e pranchas](media/65-benihime-entrada/index.html).
- [Storyboard conceitual ilustrado, oito quadros](media/65-benihime-entrada/01-storyboard-conceitual.png).
- [Estudo de aparência, silhueta, mãos e articulação](media/65-benihime-entrada/05-design-manifestacao.png).
- [Prévia 3D animada MP4, 22 s, 960×540, 24 fps](media/65-benihime-entrada/03-previa-animada.mp4).
- [Versão GIF, 10 fps](media/65-benihime-entrada/04-previa-animada.gif) e [quadros técnicos](media/65-benihime-entrada/02-storyboard-tecnico.jpg).
- [Gerador reproduzível da prévia](media/65-benihime-entrada/gerar_previa.py), [timeline](media/65-benihime-entrada/timeline.json) e [prompts das pranchas](media/65-benihime-entrada/prompts-imagegen.md).

As duas pranchas são direção artística criada com geração de imagens. A prévia 3D usa geometria procedural simplificada, movimentos articulados e câmera em perspectiva; não é vídeo montado apenas alternando imagens. Ela serve para avaliar sequência, duração, encenação e espaço. O modelo/tecido ainda não têm o acabamento da ilustração, não são modelo final exportável GeckoLib e não foram instalados no mod. Vozes e efeitos sonoros não foram adicionados; falas estão indicadas por legendas provisórias. Não tratar aprovação da sequência como homologação automática do visual final.

## Roteiro vigente solicitado

O roteiro atual sucede os 5–6 segundos propostos no ciclo 64. O usuário acrescentou entrada lenta, pausa no chão e segunda postura. A versão de estudo usa **22 segundos**, com quase três segundos inteiros sustentando a pose inclinada. Duração de gameplay, janela de bloqueio e instante de ativação ainda precisam ser fechados a partir da revisão desta prévia; a build permanece com sua duração anterior de três segundos e commit no tick 40.

1. **0–2 s — frontal / ajoelhar.** Câmera fecha à frente do jogador, conforme a visão frontal pedida como “segunda pessoa”. Ambos os joelhos descem, tronco acompanha e a espada fica baixa no lado externo, sem atravessar o corpo. A pose não exige trocar a skin por Urahara: a figura loira é um manequim de estudo.
2. **2–3,6 s — rosto / Bankai.** Câmera aproxima o rosto e deixa uma pausa curta para a palavra “Bankai”. A voz será produzida/integrada depois, conforme solicitado. Minecraft não possui articulação facial vanilla detalhada: o close deve funcionar com a skin e o movimento da cabeça, sem prometer sincronismo labial inexistente.
3. **3,6–5,2 s — recuo / cortina.** Câmera recua para revelar o jogador pequeno e uma cortina preta alta atrás dele. Ela surge e assenta; não fica agitando antes da interação. Na entrada, a malha deve transmitir tecido opaco com pregas, não fumaça nem anel de portal.
4. **5,2–8 s — primeira mão.** Uma mão surge lentamente de dentro, afasta a borda do tecido e revela punho/antebraço. As pregas respondem primeiro perto dos dedos e depois propagam o movimento. Ombro, cotovelo e punho precisam pertencer à mesma cadeia, sem membros flutuantes. A prévia simplifica a oclusão; esse contato é item de acabamento obrigatório no modelo final.
5. **8–10,2 s — rosto.** Plano mais próximo mostra Benihime espiando entre as dobras: olhos fechados, expressão calma, laços e mechas negras. A mão ainda sustenta a abertura. Não revelar a figura inteira antes desse momento.
6. **10,2–12,6 s — corpo inteiro.** Retorno ao plano frontal amplo. Ombros, vestes e corpo atravessam a cortina; as mãos buscam o chão. A figura não é esticada verticalmente para parecer que nasceu. O usuário continua enquadrado no espaço entre os braços.
7. **12,6–15,5 s — assinatura no chão.** Figura inclinada atrás do jogador, ambas as mãos apoiadas no chão. Sustentar a pose por quase três segundos. Pequena respiração, dedos acomodando apoio, roupa e cabelo assentando; sem retorno imediato ao repouso e sem tremer toda a câmera.
8. **15,5–18,6 s — tronco e mãos sobem.** Base/vestes permanecem ancoradas; tronco se ergue, cotovelos acompanham, punhos giram e dedos assumem postura suspensa. Os fios começam a pender das mãos, terminando acima/ao redor do jogador. A intenção não é levantar a figura inteira rigidamente.
9. **18,6–21 s — fios / nome completo.** Plano final frontal baixo mostra a escala, o jogador e as duas mãos acima dele. O jogador fala “Kannonbiraki Benihime Aratame”. Legenda provisória no estudo; a gravação futura deve ditar a duração exata dessa pausa. Fios finos não devem esconder o rosto do jogador.
10. **21–22 s — estabilização e saída.** Reserva de tempo para concluir o gesto e devolver a câmera com suavidade. Na prévia, mantém-se o enquadramento final; a recuperação para a perspectiva real do jogador será produzida na integração. Não afirmar que a prévia já demonstra essa restauração.

## Aparência e movimento propostos

Preservar pele clara, olhos fechados, cabelo negro com laços/mechas, vestes carmesins em camadas e braços expostos. Trocar a sensação de boneca de cubos grandes por volumes menores, transições de ombro/cotovelo/punho, rosto com mandíbula/nariz discretos e mãos com dedos distinguíveis. Pintura de textura deverá reforçar tecido, costuras e rosto em vez de depender somente de cor lisa.

A prancha traz vistas e uma referência de escala artística. O jogador vanilla é aproximadamente 1,8 bloco, não um bloco como indicado num rótulo da ilustração; não usar a régua artística para calcular alcance/hitbox. Conservar inicialmente a altura de referência de aproximadamente dez blocos. O afastamento atual de cerca de 11 blocos é a base espacial; qualquer ajuste para que as mãos fiquem sobre o jogador deve ser decidido junto das proporções dos braços e câmera, não deslocando a hitbox do jogador.

Separar as animações: `curtain_settle`, `hand_emerge`, `face_reveal`, `body_exit`, `ground_hold`, `torso_rise`, `thread_hold`, `idle_secondary` e `release_recovery`. Cabelo e tecido continuam alguns quadros após cada gesto; pulsos pequenos de respiração/dedos mantêm a presença sem destruir a pose. O movimento de marionete aprovado aparece na postura final dos fios, enquanto a pose inclinada é a assinatura da entrada.

## Ferramentas e limites de produção

**Já usado nesta entrega:** image_gen para arte conceitual; Python/NumPy/Pillow para geometria/câmera, FFmpeg temporário para codificar MP4/GIF e HTML local para reprodução. Nenhuma dependência de jogo foi acrescentada. Não foi usado Blender/Blockbench para gerar estes arquivos; são a próxima etapa de autoria proposta.

**Para levar ao jogo:** Blockbench para rig/keyframes da manifestação em formato GeckoLib compatível com a versão 4 já fixada; PlayerAnimator para ajoelhar/arma/recuperação do jogador; controlador específico de câmera Benihime; geometria articulada da cortina e fios. A documentação oficial do [GeckoLib Getting Started](https://github.com/bernie-g/geckolib/wiki/Getting-Started) descreve o fluxo de modelos/animações no Blockbench, com orientação de GeckoLib 4. Não copiar caminhos/APIs do GeckoLib 5 para Forge 1.20.1.

O [guia oficial de expressões do Blockbench](https://blockbench.net/wiki/guides/animation-expressions/) descreve curvas, respiração e movimentos periódicos exportáveis via GeckoLib. Isso ajuda no movimento secundário; não substitui poses principais e seus keyframes.

Blender é opção de autoria/estudo para anatomia, malha e tecido; [simulação de tecido](https://docs.blender.org/UATEST/manual/en/dev/physics/cloth/introduction.html) e [bake/cache](https://docs.blender.org/UATEST/manual/en/dev/physics/cloth/settings/cache.html) permitem estudar o contato mão/pano. A proposta para o mod é reproduzir o resultado com faixas/controles leves e movimentos gravados; não instalar um simulador de tecido pesado no runtime.

Importante para a aparência: um modelo de malha livre do Blender não entra automaticamente no pipeline atual `.geo.json`. O caminho conservador é modelar com elementos suportados, pivôs, planos e textura; se o rosto/braços exigirem malha livre para atingir a arte, validar um renderizador próprio antes de prometer fidelidade. A própria manifestação atual já tem geometria procedural: GeckoLib pode dirigir partes, mas a migração não é uma simples troca de textura. Photon fica opcional para pequenos fios/fragmentos, sem encobrir modelo/mãos e sem substituir corpo, câmera ou tecido.

## Contrato de integração a fechar depois da revisão

- Timeline única relaciona corpo, manifestação, cortina, câmera, legendas e áudio futuro. Nenhuma alteração em dano, cura, cargas ou técnicas agora.
- Não copiar a invulnerabilidade do Zaraki para esta transformação. Câmera é apresentação cliente; forma/custo continuam autoritativos no servidor.
- Ajoelhar é pose visual de PlayerAnimator; não reduzir caixa de colisão ou introduzir agachar mecânico silenciosamente. Caso se deseje isso, abrir contrato separado.
- Câmera não acompanha yaw bruto do mouse durante a cena; usa âncora/direção estáveis, enquadramento previsível e recuperação da perspectiva anterior.
- Definir posições alternativas para cavernas, água e jogador no ar. As mãos têm apoio visual adaptado à superfície; não atravessam blocos para encontrar um chão arbitrário.
- Observadores recebem a idade correta da sequência. Dono vê as tomadas; espectadores próximos veem corpo/manifestação sem perder sua própria câmera.
- Cancelamento, morte, troca de mundo/arma e desconexão removem cortina, poses e câmera. Nenhuma alteração no controlador/assets da transformação Zaraki.

## Verificação da prévia

MP4 decodificado integralmente sem erro; duração 22 s e 528 quadros, sem áudio. GIF em loop com 220 quadros. Quadros iniciais, mão, rosto, apoio e fios inspecionados. Galeria com reprodução, seleção de capítulo, velocidades e três pranchas. O código do gerador é somente autoria de documentação, não implementação no mod. `src`, Gradle e dependências permanecem sem alterações. Não houve build/GameTest por não existir mudança de runtime.

**Próxima execução:** incorporar feedback sobre enquadramento, ritmo, cortina e aparência; então produzir o modelo/rig e a ativação em jogo. Não iniciar automaticamente as revisões futuras Ryūjin/kit Benihime antes de concluir esta entrada.
