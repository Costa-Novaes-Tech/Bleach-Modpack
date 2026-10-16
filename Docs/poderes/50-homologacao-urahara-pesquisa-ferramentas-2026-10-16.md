# Ciclo 50 — Urahara homologado e pesquisa de ferramentas

Data da pesquisa: **16/10/2026**. Git limpo na `Feature-Poderes-bankais`, HEAD `f46d669`. Usuário homologou a feature após testar o ciclo 49. **Urahara/Benihime concluído e homologado**, junto dos kits/recursos já aceitos. Não reabrir ajustes automaticamente. Esta etapa é pesquisa e discussão; nenhuma biblioteca instalada, migração de modelos ou mudança de gameplay autorizada por este documento.

Base funcional: Minecraft 1.20.1, Forge 47.4.10, Java 17; mod 0.2.0, rede 2.17, schema 8. Última implementação validada com 98 regressões e 113 GameTests. Pesquisa documental não gera nova build nem comprova compatibilidade de bibliotecas em runtime.

## O que existe e o que era referência

`build.gradle` declara somente Forge como dependência funcional. O projeto já desenha seus efeitos/modelos por código e usa recursos vanilla, com sincronização própria. As dependências GeckoLib, mclib, TerraBlender, Curios, MariaDB/Hikari e JEI citadas em `Docs/arquitetura/10-build-dependencias.md` descrevem majoritariamente o Dragon Mine Z de referência, não uma instalação atual no Bleach. Não copiar versões/restrições históricas desse documento como decisões atuais.

Ferramenta de autoria é usada por quem cria conteúdo; biblioteca é carregada pelo mod em jogo. Um jogador não precisa instalar Blockbench/Aseprite para usar nossas texturas exportadas. Já uma integração com GeckoLib exige resolver e distribuir as dependências correspondentes. Um mod completo de combate também define regras, além de desenhar movimentos.

## Modelagem, texturas e áudio

| Ferramenta | Papel | Aplicação recomendada |
|---|---|---|
| [Blockbench](https://www.blockbench.net/) | Modelagem de baixo polígono, UV, pintura, bones/keyframes e plugins de exportação | Principal editor para zanpakutō, bainhas, Hollow, personagens e manifestações. Manter `.bbmodel` editável e exportações versionadas. |
| [Aseprite](https://www.aseprite.org/) | Pixel art, sprites animados e spritesheets | Texturas próprias de lâminas/máscaras, ícones e partículas. |
| [LibreSprite](https://github.com/LibreSprite/LibreSprite) | Editor livre de pixel art/animação | Alternativa para o fluxo de sprites sem depender do mesmo editor usado por outro integrante. |
| [Audacity](https://www.audacityteam.org/) | Edição/gravação de áudio | Produzir/cortar camadas de saque, vento, impacto, carga e liberação a partir de gravações/assets autorizados. |

Blockbench é a escolha inicial para o estilo Minecraft. Blender fica como opção complementar quando houver necessidade concreta de um fluxo de animação mais complexo; não é requisito para produzir o primeiro conteúdo. A biblioteca não cria arte pronta: a equipe ainda precisa modelar, pintar, animar e revisar o resultado em jogo.

Direção proposta para assets: silhueta única por arma, textura autoral, cabo/guarda reconhecíveis e leitura clara em primeira pessoa. Candidatos de orçamento inicial: itens 16/32 px e criaturas 64/128 px, ajustados por teste visual, sem tornar esses números limites universais. Para inimigos, máscara, buraco Hollow, membros e comportamento precisam formar uma identidade consistente. Variar só a textura de um zumbi seria uma etapa provisória.

## Bibliotecas e compatibilidade pesquisada

### GeckoLib 4 — primeira candidata para criaturas/itens animados

A [distribuição oficial filtrada para 1.20.1](https://www.curseforge.com/minecraft/mc-mods/geckolib/files/all?version=1.20.1) lista `geckolib-forge-1.20.1-4.8.4.jar`. É **candidata de versão**, ainda não testada neste projeto. A [instalação oficial GeckoLib 4](https://github.com/bernie-g/geckolib/wiki/Installation-%28Geckolib4%29) tem caminho específico para Forge 1.19.3–1.20.4 e cita mclib 20. Confirmar o grafo efetivo do artefato ao integrar, sem copiar templates de GeckoLib 5/versões novas.

Recomendação: começar com um Hollow piloto, com idle, andar, preparação, ataque, reação e morte. Em outra etapa, usar para itens/armaduras que precisem de movimento próprio ou migrar uma manifestação visual. Não é necessário converter todas as armas/modelos estáticos. Para animar o corpo do jogador, avaliar uma solução própria dessa finalidade.

### PlayerAnimator/PAL — piloto separado para o jogador

O [repositório oficial do PlayerAnimator](https://github.com/KosmX/minecraftPlayerAnimator) informa fim da manutenção regular, correções importantes conforme necessidade e recomenda **PAL** como sucessor. A [listagem oficial para 1.20.1](https://www.curseforge.com/minecraft/mc-mods/playeranimator/files/all?page=1&version=1.20.1) ainda oferece o Forge `player-animation-lib-forge-1.0.2-rc1+1.20.jar`.

O [PAL oficial](https://github.com/PlayerAnimationLibrary/PlayerAnimationLibrary) anuncia animações de Blender/Blockbench JSON, MoLang, keyframes de efeitos e pivôs/bones adicionais. Porém, essa descrição atual não comprova um artefato PAL compatível com nosso Forge 1.20.1. A [documentação legada do Zigy](https://github.com/ZigyTheBird/ZigysPlayerAnimatorAPI/wiki/Legacy-Getting-Started) menciona API 2.1.5 para 1.20.1 e dependência de Player Animator; **API legada e PAL atual não devem ser tratados como a mesma instalação**. A página de documentação atual e o filtro de arquivos PAL não ficaram acessíveis na consulta.

Decisão proposta: testar PlayerAnimator legado como opção concreta para esta plataforma e verificar PAL antes de escolhê-lo. Expor uma pequena interface interna para trocar o backend se necessário. Não incluir duas bibliotecas concorrentes ao mesmo tempo. Suporte em terceira pessoa não garante automaticamente animação correta dos braços em primeira pessoa, armaduras, bainha ou modelo slim; cada caso exige conferência.

### Photon — editor de efeitos, em bancada

O [README do ramo 1.20.1](https://github.com/Low-Drag-MC/Photon/tree/1.20.1) descreve editor de partículas e trilhas; as [propriedades do ramo](https://raw.githubusercontent.com/Low-Drag-MC/Photon/1.20.1/gradle.properties) identificam Photon 1.1.17 e plataformas Forge/Fabric. A [configuração oficial de exemplo 1.20.1](https://github.com/Low-Drag-MC/Photon-Arsenal/blob/1.20.1/settings.gradle) inclui LDLib/Photon para Forge. A documentação atual padrão do projeto já aponta Photon/LDLib2 para NeoForge 1.21.1: não aplicar esse conjunto ao nosso projeto.

Possível ganho: editor visual para trilhas de lâmina, círculos, raios, fogo, gelo e liberações. Precisa de piloto com versões compatíveis de Photon **1.x** e LDLib, verificação de shaders/transparência e comparação de desempenho. Não assumir que substituirá nossa geometria de pétalas, espadas, rede ou Benihime; manter o que já funciona e avaliar uma técnica nova de bancada.

### Expansões e desenvolvimento

- [Curios 1.20.x](https://github.com/TheIllusiveC4/Curios/tree/1.20.x): API de slots/equipamentos extras. Útil quando definirmos acessórios Quincy, distintivos, amuletos ou peças cosméticas. Não é necessária para a bainha atual e não deve mudar o vínculo único silenciosamente.
- [SmartBrainLib](https://github.com/Tslat/SmartBrainLib): candidata para organizar IA de inimigos. O [ramo 1.20](https://raw.githubusercontent.com/Tslat/SmartBrainLib/1.20/gradle.properties) declara Minecraft 1.20.1, mas usa configuração NeoForge 47.1.106. Antes de adotar no Forge 47.4.10, confirmar artefato, suporte e inicialização. Comparar com Goals/Brain vanilla no Hollow piloto; biblioteca não fornece um inimigo pronto.
- [TerraBlender](https://github.com/Glitchfiend/TerraBlender): integração de biomas, candidata quando houver contrato de worldgen. Não cria Soul Society/Hueco Mundo completos, nem é obrigatória para toda dimensão personalizada.
- [spark](https://spark.lucko.me/docs): ferramenta de diagnóstico para identificar custo de ticks/CPU/memória. Usar perfil representativo e métricas do cliente para comparar FPS/tempo de quadro; perfil de servidor sozinho não mede fluidez gráfica. Confirmar arquivo Forge 1.20.1 ao preparar o ambiente de diagnóstico.

MariaDB/Hikari não resolvem a demanda visual e não são necessários para os saves atuais. JEI é integração de receitas/consulta, pertinente quando esse conteúdo justificar sua adoção.

## Combate: animação versus mudança de sistema

O [guia oficial do Better Combat](https://github.com/ZsoltMolnarrr/BetterCombat#prerequisite) exige resolver conflitos com animação/modelo, alcance, timing/cooldown, controles e aparência da arma. Ele próprio oferece combos, detecção de área e integração de armas. São áreas já controladas pelo Bleach. A [proposta do Epic Fight](https://github.com/Antikythera-Studios/epicfight) também é uma reformulação ampla do combate. Logo, ambos são **alternativas de arquitetura**, não dependências pequenas para adicionar apenas uma animação. Não foi reproduzida incompatibilidade no Bleach; a restrição registrada no Dragon Mine Z é histórica e não prova isso aqui.

Recomendo primeiro animações próprias sobre nossos contratos: guarda com transição, esquiva com corpo inclinado, saque, postura de arma, gesto de Shikai/Bankai e reação visual à quebra. Depois, discutir combo de cortes/estocada com duração, cancelamento e momento de dano definidos. Uma animação de antecipação longa não combina automaticamente com dano manual imediato; alterar o momento do golpe é uma decisão de gameplay que deve ser tratada explicitamente.

Servidor continua decidindo custo, dano, colisão, alvo, interrupção e duração. Cliente apresenta pose, trilha, som e câmera opcionais. A sincronização deve enviar evento/estado e tempo de início, não cada bone em cada frame. Definir prioridade de ações para ataque/guarda/esquiva/liberação, cancelamento por morte/forma/item e opção de reduzir efeitos de câmera. As bibliotecas oferecem mecanismos, não essas regras do nosso jogo.

## Ordem recomendada para discussão

1. **Piloto de animações do jogador:** guarda/esquiva/liberação com um backend validado, observados por dois clientes; conferir primeira/terceira pessoa, slim/normal, arma, bainha e interrupções. Sem alterar os números homologados por consequência visual.
2. **Hollow piloto em Blockbench + GeckoLib 4:** modelo/textura próprios e seis animações, com ataque legível e código de dano do servidor.
3. **Pacote de arte e áudio:** texturas de armas, partículas autorais, máscaras, variações e sons com fontes registradas. Padronizar arquivos-fonte/exportações e estilo.
4. **Bancada Photon:** uma técnica de teste com trilha/liberação, medir custo e decidir se compensa a nova dependência.
5. Curios, IA mais elaborada e worldgen quando os respectivos módulos entrarem no escopo.

Essas são recomendações para escolha da próxima frente; não substituem o roadmap sem decisão do usuário. Uryū Ishida continua próximo kit de personagem, Mentor/Proficiências aguardam equipe, Hyōrinmaru permanece revisão final, Aizen adiado e Gin removido.

Validação desta entrega: Git/Gradle/documentos consultados, pesquisa em fontes primárias e revisão de links/diff. Sem executar testes de código nem alterar build/dependências, pois só documentação mudou. Compatibilidade em runtime, FPS e esforço real permanecem por medir nos pilotos propostos.
