# Ciclo 46 — Senbonzakura homologada e discussão de Urahara

Data: 15/10/2026. Branch `Feature-Poderes-bankais`. O usuário confirmou: Bankai e poderes de Senbonzakura/Byakuya concluídos e prontos. **Kit do ciclo 45 homologado**, junto da esquiva/defesa já concluídas. Próxima frente escolhida: **discutir o kit de Kisuke Urahara / Benihime**. Esta entrega registra pesquisa e proposta; não autoriza implementação, custos ou parâmetros finais.

Base funcional mantida: protocolo 2.14, schema 8, commit de implementação `a433ffc`; 91 regressões/94 GameTests aprovados no ciclo 45. Documentação isolada não gera nova build nem comprova FPS/multiplayer. Git estava limpo na branch correta ao iniciar.

## Inspiração e fontes

A [página oficial de palavras-chave do anime](https://bleach-anime.com/keyword/), verbete 観音開紅姫改メ, descreve a manifestação gigante e a reestruturação por contato, limitada à área de atuação da Bankai. Isso orienta uma identidade de manipulação técnica, com ações ofensivas e alterações de estruturas. Não significa poder irrestrito sobre todo o mapa.

Para nomes e repertório, consultar [Kisuke Urahara](https://bleach.fandom.com/wiki/Kisuke_Urahara), [Chikasumi no Tate](https://bleach.fandom.com/wiki/Chikasumi_no_Tate) e [Shibari, Benihime](https://bleach.fandom.com/wiki/Shibari%2C_Benihime). Referências secundárias distinguem energia carmesim, defesa, amarração e explosões encadeadas. Reconstrução corporal e abertura de passagem também inspiram as adaptações abaixo. Versões de jogos e técnicas de filmes não devem ser apresentadas como repertório do mangá automaticamente.

## Identidade proposta para o projeto

Urahara como combatente de preparação e combinações. Shikai permite controlar o ritmo e montar uma armadilha; Bankai muda o confronto por reestruturação, com escolhas de ataque, recuperação e mobilidade. Preservar vínculo único e progresso próprio da futura identidade `benihime` caso esse ID seja aprovado.

Selada: bengala/espada oculta como proposta visual. Shikai: lâmina própria e energia vermelha; evitar reutilizar a aparência de outra zanpakutō. Repertório especial na selada continua **a decidir**: a exceção aprovada para Senbonzakura não se aplica automaticamente a todos os kits.

## Shikai — primeira tabela candidata

| Slot | Nome de referência | Adaptação proposta |
| --- | --- | --- |
| 1 | Nake, Benihime | Disparo/corte carmesim de trajetória clara. Ataque principal de média distância, com impacto legível. |
| 2 | Chikasumi no Tate | Barreira hexagonal vermelha diante do jogador, com resistência limitada e quebra visível; interação com projéteis a definir. |
| 3 | Shibari, Benihime | Rede vermelha lançada numa região/alvo, imobilização curta ou forte lentidão, com fios reconhecíveis e possibilidade de escapar. |
| 4 | Hiasobi, Benihime, Juzutsunagi | Detonação encadeada sobre a rede: pontos vermelhos acendem e explodem em sequência, premiando a preparação do slot 3. |

Combo central: **rede → detonação**, com disparo e barreira permitindo criar a oportunidade. Ainda decidir se o slot 4 exige rede ativa ou se pode criar uma linha explosiva mais fraca quando usado sozinho. Kamisori é alternativa para o slot de ataque, caso o usuário prefira um corte conduzido; não preencher um quinto slot silenciosamente.

## Bankai — Kannonbiraki Benihime Aratame

A reestruturação é a habilidade de referência; os quatro nomes em português abaixo são **rótulos descritivos da nossa adaptação**, não quatro técnicas oficialmente nomeadas na obra.

| Slot | Rótulo de gameplay | Possibilidade inicial |
| --- | --- | --- |
| 1 | Dissecação | Uma mão de Benihime anuncia e traça uma incisão na região escolhida. Dano e uma janela curta de exposição; sem morte instantânea ou mutilação permanente. |
| 2 | Reconstrução | Costuras recompõem o próprio jogador, com cura limitada e tempo de execução. Possibilidade de selecionar aliado ainda precisa de escolha explícita de escopo. |
| 3 | Reestruturação corporal | Reorganizar temporariamente braço/arma para uma sequência curta de golpes reforçados. Costuras mostram duração e consumo; sem alterar permanentemente atributos comprados. |
| 4 | Abertura de caminho | Traçar um corredor costurado para reposicionamento ou passagem através de barreiras de técnicas compatíveis. Travessia de terreno real é uma alternativa mais ambiciosa que precisa de contrato próprio. |

Escolha inicial recomendada: raio de atuação médio, por exemplo **16–18 blocos como candidato**, para que posicionamento importe. Nenhum desses valores foi aprovado. A manifestação pode acompanhar o jogador por trás ou ficar ancorada; recomendar acompanhamento para um kit móvel, sujeito à preferência do usuário.

O slot 4 deve manter identidade própria: corredor anunciado, com entrada/saída legíveis e interação com barreiras, em vez de repetir simplesmente o dash ofensivo da Hakuteiken. Se adotarmos passagem por blocos, fechar primeiro quais materiais, largura, distância, restauração e permissões; na primeira versão, recomendar geometria visual e barreiras de técnicas do mod, preservando construções.

## Direção visual

- Bankai com Benihime gigante atrás do jogador, braços articulados/mãos animadas e vestimenta carmesim. Modelo próprio como peça central; silhueta legível é mais importante que volume de partículas.
- Mãos iniciam os gestos das habilidades; costuras brancas/vermelhas conectam área ou corpo afetado. Incisão, recomposição e corredor devem ter desenhos diferentes.
- Shikai com superfícies de energia, hexágonos, fios e pequenos pontos de detonação. Partículas complementam a geometria, usando a experiência aprovada de Senkei/Hakuteiken.
- A figura pode começar como manifestação visual sem IA de pet; ser atacável, ter vida ou agir sozinha são decisões adicionais, ainda não aprovadas.

## Decisões que faltam

Primeiro escolher o peso de **suporte a aliados** na Bankai: cura apenas própria ou reconstrução direcionada a aliados também. Depois definir acompanhamento/âncora da manifestação, comportamento independente do slot 4 Shikai e possibilidade de travessia real no corredor. Fechar essas escolhas antes de custo, dano, cooldown, raio final e passiva.

Não implementar poderes, item, invocação ou recursos visuais de Urahara ainda. Aizen permanece adiado; após Urahara, fila vigente segue com Uryū Ishida, Mentor/Proficiências após equipe e revisão final de Hyōrinmaru/Tōshirō. Gin segue removido.

Validação deste ciclo: leitura do Git/continuidade/roadmap, fontes consultadas e revisão de documentação/links/diff. Sem novo teste automatizado porque nenhum código ou asset funcional foi alterado.
