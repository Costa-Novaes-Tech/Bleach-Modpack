# 112 — Tarefa obrigatória: combate, armas, mãos e efeitos

26/10/2026. Feedback explícito do usuário: mãos/braços se movem, mas Zanpakutō e poderes das animações novas não acompanham a execução, perdendo identidade. Referências preservadas: Asauchi, Ryūjin e Hyōrinmaru. **Estado: tarefa adicionada e comparação estrutural inicial concluída; correção transversal de runtime ainda pendente.** Não confundir o C vertical entregue no111 com a homologação dos combos básicos de todos os kits.

## Evidências verificadas

- Asauchi usa o perfil herdado `bettercombat:sword`; Ryūjin e Hyōrinmaru combinam animações nativas específicas de corte/retorno/estocada no weapon_attributes. Não substituí-las globalmente para uniformizar os kits.
- As seis animações próprias de Benihime/Senbonzakura em attack_animations declaram braços/corpo/pernas/cabeça, mas não declaram rightItem/leftItem.
- A referência nativa `one_handed_slash_horizontal_right`, extraída do Better Combat1.9.0 Forge1.20.1 instalado, contém12 keyframes de rightItem, além das curvas dos braços. Comparação reproduzível com caminhos, contagens e hashes em [comparison.json](qa/cycle112/comparison.json).
- A ausência de canais do item é uma diferença concreta, não prova suficiente de que seja a única causa de arma parada. Item pode herdar transformação do braço; verificar o renderer, as posições/rotações locais e a composição de camadas antes de adicionar compensações.
- O111 removeu a sobreposição do melee legado no Senbonzakura e calibrou a pose/item do C da Ryūjin. Isso não comprova o vínculo arma/mão em todos os golpes novos, nem em primeira pessoa.

## Ordem da revisão e critérios

1. Reproduzir em primeira e terceira pessoa, destro/canhoto, os três golpes básicos de Benihime e Senbonzakura, em selada/Shikai/Bankai. Capturar arma inteira e mão, com marcos de preparação, passagem, contato e recuperação; conferir mão secundária livre/ocupada.
2. Comparar a mesma execução com Asauchi, Ryūjin e Hyōrinmaru. Medir movimento da ponta/guarda da arma relativo ao punho; separar falta de keyframes, pivô/display incorreto, espelhamento e controlador concorrente. Usar nativos como referência funcional, mantendo identidade de cada kit.
3. Auditar contratos de carregamento dos JSONs attack_animations/player_animation, herança do perfil e first-person rendering; conferir rightItem/leftItem, mudanças de posição local e interação com body/arm/bend. Não copiar movimentos nativos às cegas ou modificar todas as poses para corrigir uma.
4. Ajustar animações e item display/rig conforme a causa reproduzida. Garantir que a lâmina realmente varra o percurso e recupere posição, com empunhadura coerente, sem escorregar, atravessar mãos ou permanecer imóvel.
5. Vincular rastros e efeitos presos à lâmina às âncoras reais da arma e ao tempo do golpe. Projéteis partem do ponto/momento correto e seguem seu trajeto; áreas fixas aprovadas, como Jardim do Senbonzakura e rede da Shibari, continuam fixas. Não fazer todo poder seguir a mão por conveniência.
6. Verificar transições combo → defesa → técnica → transformação e retorno, sem controladores deixarem offsets residuais. Preservar custos, stamina por golpe, alcance/hitbox, cooldown, autorização do servidor e aliados.
7. Validar no Minecraft real os movimentos e o contato ponta/punho/efeito. Teste estrutural/compilação não encerra a tarefa. Exigir playtest artístico do usuário; observador multiplayer/Photon ficam com status próprio, sem presumir aprovação.

## Encaixe no roadmap

Revisão transversal obrigatória vinculada ao item4 (combate da Benihime), à homologação do novo combate do Senbonzakura e às próximas identidades. Precisa fechar antes de considerar esses combates homologados. Preservar a ordem e os demais itens já obrigatórios, inclusive eficiência/limpeza, Mentor/Proficiências adiados e expansão futura para NPCs/quests. Zaraki permanece intacta.
