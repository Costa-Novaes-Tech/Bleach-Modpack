# Módulo 06 — Habilidades e Combate

> **Direção vigente REV-SP-02 — 22/10/2026:** Spiritual Points compram **skills e transformações**, conforme esclarecimento explícito do usuário, usando Dragon Mine Z como referência para a compra. **Mastery é desenvolvida pela prática e não é comprada.** Requisitos/contexto disponibilizam a aquisição; SP paga a compra/desbloqueio; usar a capacidade depois segue os custos próprios de Energia/Stamina. [Contrato comum em M05/11.2](05-atributos-e-recursos.md#112-rev-sp-02--compra-de-skills-e-transformacoes-com-spiritual-points). Preços e exceções concretas permanecem nas fichas; a compra não pode ser removida genericamente por interpretação de M05/M06.

## Estado atual do módulo

**Status geral:** ESPECIFICADO CONCEITUALMENTE

> **Continuidade revisada — 22/10/2026:** o fechamento de M06 permanece; M07 já está especificado conceitualmente. Antes de M08, o usuário solicitou revisar M01–M07, especialmente M04. A revisão identificou pendência de criação/continuidade e sinalizou trechos raciais antigos; ver [Revisão transversal de M01–M07](00-revisao-modulos-01-a-07-2026-10-22.md). A menção a M07 como próximo no registro de 21/10 é histórica. Nenhum teste ou aceite novo.

**Revisão de continuidade:** 21/10/2026. Base universal e quatro caminhos raciais consolidados; M06-11 concluído conceitualmente, com A–Y aprovados. X/Y nas seções 45–46, interrupções na 47 e revisão de maturidade/pendências na 48. Próximo: M07 — Transformações e Domínio.

O fechamento define a arquitetura de combate, formação, recursos, estados e integrações. Fichas executáveis, catálogo, números, equipamento, conteúdo, políticas de multiplayer e tradução técnica continuam pendentes com responsáveis na seção 48. Não equivale a implementação, teste ou aceite de gameplay.

M06-07 — Shinigami, M06-08 — Hollow/Arrancar, M06-09 — Quincy e M06-10 — Fullbringer preservam suas aprovações e conclusões estruturais. Linhagens/Grimmjow, formas específicas e artes continuam para etapa posterior.

Consultar o [handoff vigente](00-handoff-atual.md), o [mapa modular](00-mapa-modular.md) e a [base datada](00-base-conhecimento-minecraft-bleach.md). Estados e encaminhamentos intermediários são históricos; a conclusão da seção 48 governa esta continuidade. Revisões M05/M06 prevalecem sobre descrições antigas de pontos, Proficiência, aquisição e repertórios.

---

# 1. Filosofia geral do sistema de combate

O sistema de combate deve oferecer profundidade sem transformar o mod em um fighting game excessivamente técnico.

A base deve ser simples de compreender:

- atacar;
- bloquear;
- esquivar;
- movimentar-se;
- utilizar técnicas.

A profundidade surge progressivamente através do repertório aprendido pelo personagem.

O jogador não recebe dezenas de ações fundamentais diferentes. Em vez disso, treinamento e técnicas modificam, expandem ou aproveitam ações básicas que ele já conhece.

Exemplo:

```text
ação básica:
Dodge

com treinamento:
Dodge + ataque
→ contra-ataque contextual

com outro treinamento:
Dodge lateral + ataque
→ técnica diferente
```

Portanto:

> O sistema fornece uma linguagem básica de combate; o treinamento determina o quanto o personagem consegue fazer com essa linguagem.

---

# 2. Bleach como referência e adaptação para gameplay

Bleach é a principal referência de identidade, fantasia, funcionamento conceitual e diferenciação entre poderes e estilos.

Entretanto, o mod não tentará reproduzir literalmente todas as regras demonstradas na obra.

O processo de adaptação será:

```text
referência em Bleach
↓
identidade e fantasia da capacidade
↓
necessidades do gameplay
↓
sistemas do mod
↓
adaptação final
```

O objetivo é fazer com que uma técnica continue sendo reconhecível e coerente com Bleach, mas funcione bem dentro de Minecraft e da arquitetura do mod.

### M06-P01 — DECIDIDO — Cânone como fundamento, gameplay como adaptação

As habilidades deverão preservar a identidade, fantasia e características reconhecíveis de suas referências em Bleach, mas seu funcionamento será adaptado conscientemente aos sistemas e objetivos do mod.

Fidelidade não significa reprodução literal.

### M06-P02 — DECIDIDO — Mecânicas devem servir à experiência

Custos, Stamina, Energia Espiritual, cooldowns, preparação, recovery, Mastery, condições e demais propriedades serão escolhidos considerando conjuntamente:

- coerência com Bleach;
- identidade da técnica;
- legibilidade;
- diversão;
- balanceamento;
- integração com os demais sistemas.

Nenhuma solução presente na obra possui prioridade automática sobre uma adaptação de gameplay melhor.

### M06-P03 — DECIDIDO — Preservar a fantasia, adaptar a regra

Quando uma mecânica da obra não funcionar adequadamente em Minecraft ou na arquitetura do mod, ela poderá ser adaptada sem receio, desde que sua fantasia e identidade permaneçam reconhecíveis.

---

# M06-01 — Arquitetura Universal de Habilidades

## 3. Categorias sistêmicas

As capacidades do personagem serão diferenciadas por sua função dentro do sistema.

### M06-01A — DECIDIDO

As habilidades serão organizadas conceitualmente em:

1. **Ações fundamentais de combate**
   - ataque;
   - defesa;
   - esquiva;
   - outras ações básicas.

2. **Técnicas ativas**
   - utilizadas deliberadamente pelo jogador;
   - normalmente acessíveis pela Combat Hotbar.

3. **Técnicas contextuais**
   - executadas através de uma ação ou situação específica;
   - podem utilizar ataques, esquivas, movimento, bloqueio e outros contextos sem exigir um slot próprio.

4. **Capacidades passivas**
   - produzem efeitos automaticamente quando suas condições são satisfeitas.

5. **Técnicas do poder principal**
   - pertencentes à Zanpakutō, Resurrección, Schrift, Fullbring ou equivalente do caminho.

6. **Formas e estados superiores**
   - pertencem principalmente ao Módulo 07 e não serão tratados como técnicas comuns da Combat Hotbar.

---

## 4. Infraestrutura comum não significa comportamento igual

As técnicas compartilharão uma infraestrutura sistêmica comum sempre que possível.

Essa infraestrutura poderá tratar elementos como:

- aquisição;
- validação;
- execução;
- custos;
- cooldown;
- atributos;
- Mastery;
- requisitos;
- condições;
- efeitos.

Isso não significa que todas as técnicas funcionem da mesma maneira.

Shunpo, Cero, Kidō, técnicas de Zanpakutō, capacidades Quincy e habilidades Hollow podem compartilhar a mesma linguagem de sistema sem possuir o mesmo comportamento, aquisição ou identidade.

### M06-01A — complemento decidido

> Mesma infraestrutura sistêmica não significa mesmo conteúdo, mesma progressão ou mesma experiência racial.

O objetivo é evitar a criação desnecessária de motores completamente separados para cada categoria de habilidade.

---

# 5. Combat State e Combat Mode

Foi estabelecida uma distinção entre dois sistemas que não devem ser confundidos.

## Combat State

O **Combat State** já definido anteriormente representa o estado interno em que o personagem está envolvido em uma situação de combate.

Pode ser ativado por eventos como:

- causar dano;
- receber dano;
- entrar em uma confrontação relevante.

Ele influencia principalmente regras sistêmicas, como regeneração e prevenção de exploits.

É automático.

## Combat Mode

O **Combat Mode** representa a postura/interface de combate controlada pelo jogador.

### M06-01B — DECIDIDO

Combat State e Combat Mode são sistemas distintos.

É possível estar:

```text
Combat State ON
Combat Mode OFF
```

Exemplo:

O jogador está minerando e recebe um ataque inesperado.

O sistema reconhece que ele está em combate, mas sua interface não deve obrigatoriamente trocar naquele instante.

Também pode existir:

```text
Combat State OFF
Combat Mode ON
```

Exemplo:

O jogador ativa antecipadamente sua interface de combate antes de iniciar uma luta.

### M06-01C — DECIDIDO

O Combat Mode será manual por padrão.

O jogador utilizará uma tecla para entrar ou sair desse modo.

Posteriormente, no módulo de interface/configurações, poderá existir uma opção para entrada automática em Combat Mode ao causar ou receber dano.

Essa automação não será o comportamento padrão.

---

# 6. Combat Hotbar

### M06-01D — DECIDIDO

Ataque, defesa, esquiva e outras ações fundamentais possuirão controles próprios.

Essas ações não ocuparão slots de técnicas.

### M06-01E — DECIDIDO

Ao ativar Combat Mode, a hotbar vanilla será visual e funcionalmente substituída pela **Combat Hotbar**.

Essa alteração não moverá nem substituirá fisicamente os itens reais da hotbar vanilla.

O inventário do jogador permanece intacto.

### M06-01F — DECIDIDO

A Combat Hotbar possuirá inicialmente:

**9 slots de técnicas.**

Controles padrão:

```text
1 → Técnica 1
2 → Técnica 2
3 → Técnica 3
4 → Técnica 4
5 → Técnica 5
6 → Técnica 6
7 → Técnica 7
8 → Técnica 8
9 → Técnica 9
```

Cada controle poderá ser remapeado nas configurações do Minecraft.

O número 9 permanece como decisão atual e só deverá ser revisado caso prototipagem ou playtests revelem um problema real.

### M06-01G — DECIDIDO EM PRINCÍPIO

Capacidades importantes de mobilidade racial ou espiritual poderão utilizar um controle dedicado quando legitimamente disponíveis.

Exemplos futuros podem incluir:

- Shunpo;
- Sonído;
- Hirenkyaku.

Elas não precisam ocupar um dos nove slots comuns de técnicas.

### M06-01H — DECIDIDO

Formas e transformações importantes não ocuparão slots comuns da Combat Hotbar.

O Módulo 07 definirá sua forma de ativação.

---

# M06-02 — Combate Básico

# 7. Filosofia do combate fundamental

### M06-02A — DECIDIDO

O combate fundamental será deliberadamente simples.

O jogador não deverá precisar dominar uma quantidade excessiva de comandos universais para ser funcional em combate.

A profundidade marcial surgirá principalmente através das técnicas aprendidas.

---

# 8. Contextos reconhecidos pelo combate

### M06-02B — DECIDIDO

O sistema reconhecerá contextos básicos que poderão produzir ações comuns ou servir como gatilho para técnicas aprendidas.

Exemplos:

```text
sequência normal de ataques

correr + atacar

ataque no ar

ataque após esquiva

direção + ataque
```

Esses contextos podem inicialmente executar apenas ações básicas.

Quando o jogador aprende uma técnica compatível, ela poderá modificar, substituir ou expandir aquela resposta.

---

# 9. Formas de ativação de técnicas

### M06-02C — DECIDIDO

As técnicas poderão possuir três modelos principais de ativação:

### Ativa

Executada diretamente pelo jogador, normalmente através da Combat Hotbar.

### Contextual

Executada quando determinada combinação de situação e input é satisfeita.

Exemplo:

```text
Dodge + ataque
```

Não precisa obrigatoriamente ocupar um slot.

### Passiva

Ativada automaticamente quando suas condições são satisfeitas.

---

# 10. Sequência básica de ataques

### M06-02D — DECIDIDO EM PRINCÍPIO

O ataque básico poderá possuir uma sequência simples e natural de golpes, potencialmente terminando em um finalizador mais forte.

Não existe necessidade atual de criar um botão universal exclusivo para "Heavy Attack".

Exemplo conceitual:

```text
ataque
↓
ataque
↓
ataque
↓
finisher
```

Estilos e técnicas aprendidas poderão modificar ou expandir essas sequências.

---

# 11. Uso da Stamina no combate básico

### M06-02I — DECIDIDO

Stamina representa esforço físico e defensivo de curto prazo, mas não deverá funcionar como um imposto constante sobre qualquer ação.

Ataques leves e triviais podem possuir custo inexistente ou desprezível.

Stamina deverá ter peso principalmente em ações relevantes, como:

- esquiva;
- técnicas físicas exigentes;
- determinados finalizadores;
- movimentos especiais;
- ações defensivas específicas;
- outras ações que realmente representem esforço.

Objetivo:

> Stamina cria ritmo e limita abuso, não obriga o jogador a parar de lutar a cada poucos segundos.

Modelo conceitual:

```text
Stamina cheia
→ alta liberdade

Stamina intermediária
→ escolhas começam a importar

Stamina baixa
→ risco

Stamina esgotada
→ vulnerabilidade
```

Os valores exatos pertencem ao balanceamento futuro.

---

# 12. Impacto, Stagger e deslocamento

### M06-02N — DECIDIDO

Ataques relevantes que acertam devem produzir feedback proporcional de impacto mesmo quando não interrompem mecanicamente o alvo.

### M06-02O — DECIDIDO

Stagger real será seletivo.

Ataques básicos comuns não deverão interromper continuamente o adversário.

Stagger poderá surgir de:

- ataques realmente impactantes;
- finalizadores;
- técnicas específicas;
- Guard Break;
- outras condições apropriadas.

### M06-02P — DECIDIDO EM PRINCÍPIO

O sistema deverá possuir proteção contra stunlock.

A implementação poderá utilizar:

- resistência temporária;
- diminishing returns;
- outro método equivalente.

A solução definitiva será determinada por protótipo e balanceamento.

### M06-02Q — DECIDIDO

Knockback forte não será um efeito universal dos ataques básicos.

O combate corpo a corpo deverá preservar proximidade suficiente para permitir continuidade.

### M06-02R — DECIDIDO EM PRINCÍPIO

Técnicas poderão possuir propriedades de controle quando apropriado:

- knockback;
- launch;
- knockdown;
- outros deslocamentos.

Essas propriedades não constituem requisitos universais para combos básicos.

---

# 13. Dano, interrupção e Super Armor

Foi estabelecida uma distinção fundamental:

```text
receber dano
≠
sofrer stagger
≠
ser interrompido
```

### M06-02S — DECIDIDO — REVISADO

Receber dano sozinho não interrompe automaticamente:

- preparação;
- carregamento;
- execução.

O jogador continuará sofrendo dano normalmente.

Ele poderá decidir continuar executando a técnica mesmo sob ataque.

Cancelamentos voluntários serão permitidos quando a própria técnica autorizar.

Interrupções forçadas ocorrerão principalmente através de efeitos de controle apropriados, como:

- stagger;
- knockdown;
- launch;
- outros definidos pela capacidade.

### M06-02T — DECIDIDO

**Super Armor** representa resistência adicional a efeitos de controle durante fases determinadas de uma técnica.

Não concede:

- invulnerabilidade;
- imunidade a dano;
- preservação de Vida;
- preservação de recursos.

O personagem continua podendo morrer durante Super Armor.

Sua função é permitir que determinadas ações resistam melhor a interrupções.

### M06-02U — DECIDIDO EM PRINCÍPIO

Técnicas com preparação ou carregamento poderão declarar individualmente:

- possibilidade de cancelamento voluntário;
- movimento permitido durante execução;
- tempo máximo de carga;
- resistência a controle;
- outras propriedades relevantes.

---

# 14. Zanjutsu, Hakuda e fundamentos de combate

### M06-02V — DECIDIDO

Combate armado e desarmado utilizarão a mesma arquitetura universal de combate.

Diferenças surgirão através de:

- movesets;
- propriedades;
- técnicas aprendidas;
- equipamento;
- contexto.

### M06-02W — DECIDIDO

Utilizar uma espada não significa automaticamente utilizar uma técnica de Zanjutsu.

Da mesma forma, atacar desarmado não significa automaticamente executar Hakuda avançado.

Existem fundamentos básicos de combate disponíveis antes do treinamento sofisticado.

Zanjutsu e Hakuda representam expansão do repertório através de treinamento.

Exemplo:

```text
possuir espada
→ permite combate armado básico

aprender Zanjutsu
→ expande e aprofunda esse combate
```

```text
estar desarmado
→ permite combate corporal básico

aprender Hakuda
→ expande e aprofunda esse combate
```

### M06-02X — DECIDIDO

Técnicas poderão possuir requisitos físicos ou contextuais de execução.

Exemplos:

```text
requer espada

requer estar desarmado

requer arma espiritual

requer estar no ar

requer bloqueio

requer alvo

requer movimento
```

Conhecer ou equipar uma técnica não significa necessariamente que ela possa ser executada em qualquer situação.

### M06-02Y — DECIDIDO

**Nota de revisão documental — 20/10/2026:** o modelo sem Proficiência apresentado abaixo foi superado por M06-07B/B.1/B.2 e pela seção 33 de M06-07. O contrato vigente inclui Proficiência da disciplina, técnicas e Mastery individual, além dos atributos; continua sem segundo rank comprado ou Level individual paralelo à Mastery. O texto original permanece para rastreabilidade.

Zanjutsu e Hakuda não possuirão um nível numérico genérico que substitua as técnicas.

Não haverá, por exemplo:

```text
Zanjutsu Level 57
Hakuda Level 34
```

O desenvolvimento ocorrerá principalmente através de:

- aquisição de técnicas;
- Mastery individual das técnicas;
- atributos fundamentais.

### M06-02Z — DECIDIDO EM PRINCÍPIO

Zanjutsu tende a aprofundar combate armado.

Hakuda tende a aprofundar combate corporal.

Essas tendências não serão tratadas como moldes absolutamente rígidos caso Bleach ou uma boa adaptação de gameplay justifiquem exceções.

---

# 15. Disciplinas gerais e estilos de personagens canônicos

Uma disciplina não representa um moveset idêntico entre todos os seus praticantes.

Personagens diferentes podem utilizar a mesma disciplina de maneiras muito distintas.

O jogador deve conseguir construir um personagem original que incorpore elementos de seus personagens favoritos sem precisar selecionar uma classe fechada baseada naquele personagem.

### M06-02AA — DECIDIDO — Disciplina e expressão individual

Uma disciplina representa um campo compartilhado de treinamento.

Praticantes diferentes podem desenvolver:

- repertórios diferentes;
- preferências;
- aplicações;
- combinações;
- estilos pessoais.

### M06-02AB — DECIDIDO — Técnicas gerais e associadas

O catálogo futuro distinguirá:

- técnicas gerais;
- técnicas institucionalmente ensináveis;
- técnicas avançadas;
- técnicas conhecidas principalmente através de determinados praticantes;
- técnicas pessoais ou não generalizáveis.

Uma habilidade não será considerada universal apenas porque pertence a uma disciplina geral.

### M06-02AC — DECIDIDO — Estilos sem classes fechadas

O jogador poderá incorporar aspectos do estilo de combate de personagens canônicos através das técnicas que aprende.

Não existirá obrigação de escolher:

```text
Estilo Yamamoto

Estilo Byakuya

Estilo Kenpachi
```

como classes permanentes.

O estilo final do personagem surgirá de seu repertório.

### M06-02AD — DECIDIDO — Poder exclusivo permanece separado

Técnicas que dependem diretamente de:

- Zanpakutō específica;
- Schrift;
- Resurrección;
- Fullbring;
- outro poder individual;

permanecem ligadas ao respectivo poder.

Elas não se tornam técnicas gerais apenas por envolverem espada, corpo, movimento ou energia.

### M06-02AE — DECIDIDO — Adaptação de estilos

Quando Bleach demonstrar um estilo característico sem formalizar técnicas nomeadas ou ensináveis, o mod poderá traduzir esse comportamento em gameplay.

Quando isso ocorrer, a implementação deverá ser tratada como adaptação do mod, e não apresentada como uma técnica canônica oficialmente nomeada.

---

# M06-03 — Aquisição e Aprendizado

# 16. Acesso, aprendizado e domínio

### M06-03A — DECIDIDO

A progressão de uma técnica será separada em três conceitos:

```text
ACESSO
↓
APRENDIZADO
↓
MASTERY
```

### Acesso

Determina se o personagem possui condições para tentar aprender a técnica.

Pode depender de:

- raça;
- estado;
- disciplina;
- storyline;
- mentor;
- local;
- técnica anterior;
- equipamento;
- conhecimento;
- Mastery;
- condições próprias da habilidade.

### Aprendizado

Momento em que a técnica efetivamente entra para o repertório.

### Mastery

Representa quanto o personagem domina aquela técnica após adquiri-la.

---

# 17. Múltiplos métodos de aquisição

### M06-03B — DECIDIDO

Não existirá uma única receita universal para adquirir todas as técnicas.

Uma técnica poderá ser:

- ensinada;
- descoberta;
- desenvolvida através de treinamento;
- derivada de fundamentos anteriores;
- liberada por acontecimentos narrativos;
- inerente a um poder;
- adquirida de outra maneira coerente com sua natureza.

---

# 18. Mentores não são lojas

### M06-03C — DECIDIDO

Mentores não funcionarão simplesmente como vendedores de habilidades.

Modelo desejado quando apropriado:

```text
mentor reconhece condições
↓
introduz conceito
↓
treinamento
↓
teste/aplicação
↓
técnica aprendida
```

Nem toda técnica precisará obrigatoriamente utilizar todas essas etapas.

Spiritual Points permanecem como moeda de compras da progressão, incluindo componentes compráveis de técnicas/habilidades conforme disponibilidade e ficha — REV-SP-01/M05-11.1. Isso não permite comprar domínio ou contornar requisitos.

> **Formulação anterior, qualificada em 22/10/2026:** “Spiritual Points não funcionarão como moeda universal para comprar técnicas.” Não usar essa frase como proibição geral de compras.

---

# 19. Repertórios diferentes entre mestres

### M06-03D — DECIDIDO

Instrutores diferentes poderão oferecer repertórios diferentes dentro da mesma disciplina.

Exemplo conceitual:

```text
Academia
→ fundamentos

instrutor experiente
→ técnicas intermediárias

especialista
→ repertório específico

mestre excepcional
→ técnicas avançadas
```

O objetivo é dar significado à exploração, aos personagens e às oportunidades de treinamento.

---

# 20. Pré-requisitos flexíveis

### M06-03E — DECIDIDO

Técnicas poderão exigir:

- fundamentos;
- outras técnicas;
- Mastery;
- narrativa;
- equipamento;
- estado;
- treinamento;
- outras condições pertinentes.

Entretanto, o sistema não presumirá árvores lineares rígidas para todo repertório.

Evitar:

```text
Skill A
↓
Skill B
↓
Skill C
↓
Skill D
```

apenas porque essa é a estrutura tradicional de skill tree.

Requisitos devem existir porque fazem sentido para aquela técnica.

---

# 21. Mastery como requisito, não moeda

### M06-03F — DECIDIDO

Mastery poderá servir como evidência de preparo para uma técnica avançada.

Ela não será consumida.

Exemplo:

```text
domínio do fundamento
+
mentor apropriado
+
treinamento
↓
nova técnica
```

---

# 22. Treinamento jogável

### M06-03G — DECIDIDO

Sempre que apropriado, o treinamento deverá ensinar simultaneamente:

- o personagem;
- o jogador.

Uma técnica contextual de contra-ataque, por exemplo, deveria preferencialmente exigir que o jogador pratique sua execução real em vez de apenas completar um objetivo genérico como:

```text
mate 10 inimigos
```

O treinamento pode envolver:

- timing;
- input;
- posicionamento;
- alvo;
- uso real da mecânica.

---

# 23. Técnicas do poder principal

### M06-03H — DECIDIDO

Técnicas vinculadas ao poder principal poderão possuir métodos próprios de aquisição.

Elas não precisam seguir o modelo institucional utilizado por disciplinas gerais.

Seu aprendizado poderá envolver:

- relação com o poder;
- descoberta;
- domínio;
- storyline;
- treino;
- outras condições próprias.

Formas e grandes despertares continuam pertencendo principalmente ao M07.

---

# 24. Construção livre de estilo

### M06-03I — DECIDIDO

Aprender uma técnica associada a determinado mestre ou personagem não vincula o jogador permanentemente ao estilo daquele personagem.

Dentro das compatibilidades legítimas do caminho, o jogador poderá combinar técnicas adquiridas de fontes diferentes.

Modelo desejado:

```text
aprendeu técnica A com um instrutor
+
técnica B com outro
+
técnica C através de treinamento
+
possui poder principal D
↓
estilo próprio
```

---

# M06-04 — Mastery das Técnicas

# 25. Função da Mastery

Mastery representa o domínio individual de uma técnica.

Ela responde:

> Quão bem meu personagem consegue executar esta técnica específica?

Não representa:

- atributo fundamental;
- nível de disciplina;
- poder bruto geral.

### M06-04A — DECIDIDO

Mastery poderá melhorar aspectos coerentes com a técnica, como:

- execução;
- precisão;
- eficiência;
- estabilidade;
- velocidade de preparação;
- recovery;
- controle;
- propriedades específicas.

Nem toda técnica precisa melhorar todos esses aspectos.

---

# 26. Mastery e dano

### M06-04B — DECIDIDO

Mastery influencia moderadamente o dano ou eficácia quando aplicável.

Entretanto:

> Potência, demais atributos relevantes e estados de poder continuam responsáveis pela maior parte da capacidade bruta.

Mastery melhora o quanto o personagem consegue extrair daquela técnica, mas não substitui sua base.

Modelo conceitual:

```text
atributos fundamentais
↓↓↓↓↓

estado / poder
↓↓↓

Mastery
↓
```

A influência exata será calibrada posteriormente.

---

# 27. Benefícios específicos por técnica

### M06-04C — DECIDIDO

Cada técnica poderá declarar quais propriedades sua Mastery melhora.

Exemplo conceitual:

```text
Cero:
- pequeno aumento de dano
- precisão
- eficiência
- carregamento
```

```text
técnica de Zanjutsu:
- pequeno aumento de dano
- timing
- recuperação
- consistência
```

```text
Shunpo:
- controle
- eficiência
- execução
```

Não existirá obrigação de aplicar o mesmo pacote de bônus a todas.

---

# 28. Limites da Mastery

### M06-04D — DECIDIDO

Mastery não deverá remover características fundamentais da técnica.

Exemplos:

```text
Mastery alta
≠
custo zero

Mastery alta
≠
toda técnica instantânea

Mastery alta
≠
eliminação total de risco
```

Ela representa refinamento.

Não representa remoção das regras da habilidade.

---

# 29. Escala universal

### M06-04E — DECIDIDO

Todas as técnicas que utilizarem Mastery compartilharão a mesma escala de progressão.

O valor máximo numérico ainda não foi decidido.

Exemplo meramente conceitual:

```text
Cero         X / MAX
Shunpo       X / MAX
Kidō         X / MAX
Zanjutsu X   X / MAX
```

A escala representa quanto aquela técnica foi dominada.

O mesmo percentual de Mastery não significa os mesmos bônus absolutos em técnicas diferentes.

---

# 30. Como Mastery cresce

### M06-04F — DECIDIDO

Mastery será desenvolvida principalmente por:

```text
uso válido
+
treinamento apropriado
+
desafios relevantes quando fizer sentido
```

Uso real da técnica deve constituir a fonte natural de progresso.

---

# 31. Anti-farm

### M06-04G — DECIDIDO

Spam vazio não deverá produzir progressão eficiente.

Exemplos de situações a evitar:

```text
disparar técnica centenas de vezes contra uma parede

atacar alvo irrelevante infinitamente

farm AFK

loops artificiais sem contexto
```

O sistema poderá considerar relevância e contexto internamente para reduzir ou anular formas triviais de exploração.

---

# 32. Retornos decrescentes de Mastery

### M06-04H — DECIDIDO EM PRINCÍPIO

Níveis elevados de Mastery poderão exigir proporcionalmente mais desenvolvimento.

A intenção é permitir:

```text
aprendizado inicial
→ relativamente perceptível

domínio intermediário
→ exige prática consistente

domínio extremo
→ conquista significativa
```

Isso não deverá se transformar em grind vazio baseado apenas em repetir a mesma ação milhares de vezes.

---

# 33. Simplicidade para o jogador

### M06-04I — DECIDIDO

O sistema interno de Mastery poderá considerar:

- contexto;
- relevância;
- retornos decrescentes;
- anti-exploit.

Entretanto, a lógica apresentada ao jogador deve permanecer simples:

> Praticar legitimamente uma técnica desenvolve seu domínio.

Não serão criadas várias moedas, barras ou subníveis paralelos para a mesma Mastery.

Profundidade interna não deve significar complexidade desnecessária para o jogador.

---

# M06-05 — Custos, Cooldowns e Execução

# 34. Recursos utilizados pelas técnicas

### M06-05G — DECIDIDO

Regra conceitual:

```text
STAMINA
→ esforço físico relevante

ENERGIA ESPIRITUAL
→ manifestação/utilização direta de poder espiritual

AMBOS
→ técnica cuja execução justifique claramente as duas exigências

NENHUM
→ quando outros mecanismos já limitarem adequadamente a ação
```

A classificação definitiva de cada técnica dependerá de sua identidade e de seu gameplay.

---

# 35. Duplo custo

### M06-05H — DECIDIDO

Consumir Stamina + Energia Espiritual simultaneamente não será o padrão para habilidades fortes.

Duplo custo deve existir porque a técnica realmente representa dupla exigência.

Não deverá ser utilizado apenas como punição genérica de balanceamento.

---

# 36. Técnicas sem custo direto

### M06-05I — DECIDIDO

Uma técnica poderá não possuir custo direto quando outros fatores já limitarem adequadamente seu uso.

Exemplos:

- condição de execução;
- recovery;
- timing;
- posição;
- cooldown;
- risco.

---

# 37. Evitar custo redundante

### M06-05J — DECIDIDO

Técnicas contextuais não cobrarão automaticamente novamente um recurso que já foi utilizado pela ação que as originou.

Exemplo:

```text
Dodge
→ Stamina

Dodge + contra-ataque
→ não significa automaticamente outro custo completo de Stamina
```

O custo deve representar o esforço real da sequência.

---

# 38. Balanceamento multidimensional

### M06-05K — DECIDIDO

Uma técnica não será balanceada apenas através de custo.

Ferramentas disponíveis incluem:

- recurso;
- preparação;
- execução;
- recovery;
- cooldown;
- alcance;
- precisão;
- condição;
- vulnerabilidade;
- risco;
- outras propriedades.

Isso permite que técnicas diferentes possuam perfis diferentes.

---

# 39. Modelos de consumo

### M06-05L — DECIDIDO

As técnicas poderão possuir:

### Custo instantâneo

Recurso consumido no momento apropriado da execução.

### Drenagem

Recurso consumido continuamente enquanto a capacidade permanece ativa.

### Híbrido

Custo de ativação + custo de manutenção.

---

# 40. Carregamento e pagamento

### M06-05M — DECIDIDO

Técnicas carregáveis poderão definir individualmente quando o recurso é consumido.

Possibilidades incluem:

- início;
- durante carregamento;
- execução;
- combinação dessas etapas.

Não existirá uma única regra obrigatória para todas.

---

# 41. Recovery

### M06-05R — DECIDIDO — REVISADO

Recovery representa o intervalo posterior à execução de uma técnica que regula a cadência entre skills.

Sua principal função é impedir encadeamento indiscriminado e spam excessivo de técnicas.

Fluxo:

```text
Técnica A
↓
execução
↓
Recovery
↓
outra técnica pode ser iniciada
```

A duração poderá variar conforme a técnica.

Recovery não significa obrigatoriamente que o personagem fica completamente imóvel ou incapaz de realizar qualquer ação.

Restrições adicionais dependerão da técnica.

---

# 42. Cooldown

### M06-05O — DECIDIDO — REFINADO

Ao ser utilizada, uma técnica entra em seu próprio cooldown.

Durante esse período:

```text
Técnica utilizada
→ indisponível

outras técnicas
→ continuam potencialmente disponíveis
```

Assim:

```text
Recovery
→ quando posso utilizar outra técnica

Cooldown
→ quando ESTA técnica pode ser reutilizada
```

Essa distinção é fundamental.

---

# 43. Sem Global Cooldown

### M06-05N — DECIDIDO

Não existirá um Global Cooldown universal que bloqueie todas as técnicas após qualquer uso.

O Recovery já regula a cadência geral entre habilidades.

---

# 44. Cooldown compartilhado

### M06-05P — DECIDIDO

Cooldowns poderão ser compartilhados quando duas capacidades forem variações ou manifestações da mesma habilidade e alterná-las permitiria ignorar artificialmente sua limitação.

Não haverá cooldown compartilhado simplesmente por pertencerem à mesma categoria.

Evitar:

```text
usou um Kidō
→ todos os Kidō bloqueados
```

ou:

```text
usou Zanjutsu
→ todo Zanjutsu bloqueado
```

sem uma justificativa específica.

---

# 45. Cargas

### M06-05Q — DECIDIDO EM PRINCÍPIO

O sistema poderá suportar técnicas com múltiplas cargas recuperáveis quando isso melhorar seu gameplay.

Cargas serão excepcionais.

Não constituem uma mecânica obrigatória para o repertório.

---

# 46. Mastery e cooldown

### M06-05S — DECIDIDO

Mastery não reduzirá universalmente cooldown.

Uma técnica específica poderá receber alguma melhoria limitada relacionada à disponibilidade se isso fizer sentido para sua identidade e balanceamento.

---

# 47. Atributos e cooldown

### M06-05T — DECIDIDO

Nenhum atributo universal reduzirá automaticamente todos os cooldowns.

Alterações de cooldown devem pertencer às regras específicas das capacidades quando necessário.

---

# M06-06 — Defesa, Esquiva e Movimentação

# 48. Defesa básica universal

### M06-06A — DECIDIDO

Todo personagem terá acesso a uma ação básica de bloqueio.

A eficiência da defesa poderá variar conforme:

- ataque recebido;
- meio utilizado para bloquear;
- equipamento;
- estado;
- técnica;
- treinamento;
- propriedades pertinentes.

Bloqueio universal não significa bloqueio igualmente eficiente em todas as situações.

---

# 49. Postura

A decisão anterior de utilizar Stamina diretamente para determinar Guard Break foi revisada.

Postura existirá como um medidor próprio.

Sua função será propositalmente extremamente limitada.

### M06-06B — DECIDIDO — REVISADO

O bloqueio utilizará um medidor de **Postura** separado da Stamina.

Sua função é representar:

> Quanto impacto a guarda ainda suporta antes de quebrar?

Ataques bloqueados reduzem Postura.

---

# 50. Guard Break

### M06-06C — DECIDIDO — REVISADO

Quando Postura chega a zero:

```text
Postura = 0
↓
Guard Break
↓
abertura temporária
```

O defensor sofre uma abertura apropriada que pode ser aproveitada pelo atacante.

---

# 51. Função exclusiva da Postura

### M06-06C.1 — DECIDIDO

Postura existe exclusivamente para o sistema de Guard Break.

Ela NÃO será utilizada como:

- recurso de técnicas;
- Stamina alternativa;
- medidor de Stagger;
- recurso de movimentação;
- atributo ofensivo;
- sistema de progressão;
- moeda;
- requisito geral de habilidades.

Sua existência resolve apenas um problema:

**quando a guarda deve quebrar.**

---

# 52. Recuperação de Postura

### M06-06C.2 — DECIDIDO EM PRINCÍPIO

Após deixar de sofrer pressão defensiva por tempo apropriado, a Postura deverá recuperar-se naturalmente.

Tempo e velocidade exatos serão definidos posteriormente através de protótipo e balanceamento.

Não será necessário consumir itens ou recursos apenas para recuperar Postura em condições normais.

---

# 53. Pressão diferente por ataque

### M06-06C.3 — DECIDIDO

Ataques e técnicas poderão causar quantidades diferentes de dano à Postura quando bloqueados.

Isso permite distinguir:

```text
ataque leve
→ baixa pressão

finisher
→ pressão maior

golpe pesado
→ pressão elevada

técnica especializada em quebrar guarda
→ pressão muito alta
```

Isso não transforma Postura em outro sistema ofensivo.

É apenas a forma de determinar quão bem determinado ataque pressiona uma guarda.

---

# 54. Stamina e Postura

Stamina e Postura possuem responsabilidades diferentes:

```text
STAMINA
→ esforço físico

POSTURA
→ estabilidade da guarda
```

Bloquear um golpe não consumirá automaticamente Postura e Stamina simultaneamente.

Custos adicionais só existirão quando uma situação ou técnica específica justificar.

O objetivo da Postura é justamente impedir que bloquear constantemente sobrecarregue a função da Stamina.

---

# 55. Parry

### M06-06D — DECIDIDO

Parry não será uma ferramenta avançada universal entregue gratuitamente a todos os personagens.

Será uma capacidade adquirida através de:

- técnica;
- treinamento;
- estilo;
- outro contexto apropriado.

Diferentes caminhos podem possuir formas diferentes de defesa avançada.

Não é necessário criar um equivalente racial artificial para todos.

---

# 56. Esquiva universal

### M06-06E — DECIDIDO

Todo personagem possuirá uma esquiva curta e responsiva.

Ela deverá:

- utilizar Stamina;
- priorizar reposicionamento;
- retirar o personagem da trajetória do ataque.

O sistema não deverá depender principalmente de longas janelas artificiais de invulnerabilidade.

Pequenas tolerâncias técnicas poderão ser utilizadas caso necessárias para produzir bom gameplay.

---

# 57. Esquiva e mobilidade especial são diferentes

### M06-06F — DECIDIDO

Dodge não é equivalente a:

- Shunpo;
- Sonído;
- Hirenkyaku;
- outras técnicas de deslocamento.

Dodge é uma ferramenta defensiva fundamental.

Mobilidade especial é uma capacidade aprendida.

---

# 58. Mobilidade dentro e fora do combate

### M06-06G — DECIDIDO

Técnicas de movimentação legitimamente disponíveis poderão ser utilizadas tanto em combate quanto fora dele.

Possíveis usos:

- combate;
- exploração;
- perseguição;
- fuga;
- travessia;
- reposicionamento.

A implementação deverá respeitar limitações técnicas do Minecraft, desempenho e balanceamento.

A sensação de velocidade e mobilidade possui prioridade sobre tentar reproduzir literalmente distâncias ou velocidades do anime.

---

# 59. Técnicas contextuais a partir de ações básicas

### M06-06H — DECIDIDO

Ações fundamentais poderão alimentar técnicas contextuais aprendidas.

Exemplos:

```text
Dodge + ataque

Block + ataque

movimento + ataque

movimentação especial + técnica
```

Isso permite aumentar a profundidade sem aumentar excessivamente a quantidade de teclas.

---

# 60. Cancelamentos

### M06-06I — DECIDIDO

Esquiva, bloqueio e movimentação não cancelarão universalmente qualquer técnica em qualquer momento.

Cada técnica poderá declarar em quais fases permite:

- movimentação;
- bloqueio;
- esquiva;
- cancelamento voluntário.

Isso preserva comprometimento e risco das ações.

---

# 61. Controle

### M06-06J — DECIDIDO

Controle será composto por propriedades específicas quando necessárias.

Exemplos:

```text
Stagger
Knockback
Launch
Knockdown
```

Esses efeitos não serão aplicados automaticamente a todo ataque.

---

# 62. Anti-stunlock

### M06-06K — DECIDIDO EM PRINCÍPIO

O sistema deverá impedir cadeias excessivas de controle que removam continuamente a capacidade de resposta do alvo.

A implementação exata permanece pendente.

Possibilidades futuras:

- diminishing returns;
- resistência temporária;
- outro método equivalente.

O multiplayer poderá exigir refinamentos adicionais posteriormente.

---

# 63. Esquiva direcional

### M06-06L — DECIDIDO

A esquiva deverá seguir a direção indicada pelo jogador.

Modelo esperado:

```text
W + Dodge
→ frente

S + Dodge
→ trás

A + Dodge
→ esquerda

D + Dodge
→ direita
```

O comportamento exato quando nenhuma direção é fornecida poderá ser validado em protótipo.

---

# 64. Controle manual da direção

### M06-06M — DECIDIDO

A esquiva não escolherá automaticamente a melhor direção para evitar um ataque.

A decisão espacial permanece responsabilidade do jogador.

O objetivo é preservar previsibilidade e controle manual.

---

# 65. Contexto direcional

### M06-06N — DECIDIDO

Técnicas aprendidas poderão considerar a direção da esquiva como parte de suas condições.

Exemplo conceitual:

```text
esquiva lateral + ataque
→ Técnica A

esquiva para trás + ataque
→ Técnica B

esquiva para frente + ataque
→ Técnica C
```

Não existe obrigação de criar essas três variações.

Elas poderão existir quando um estilo ou técnica específica justificar.

---

# 66. Arquitetura consolidada até M06-06

Até este ponto, o combate universal possui a seguinte organização:

```text
COMBATE
│
├── Ações fundamentais
│   ├── ataque
│   ├── bloqueio
│   └── esquiva
│
├── Técnicas
│   ├── ativas
│   ├── contextuais
│   ├── passivas
│   └── poder principal
│
├── Combat Mode
│   └── Combat Hotbar 1–9
│
├── Recursos
│   ├── Stamina
│   └── Energia Espiritual
│
├── Medidor defensivo
│   └── Postura
│       └── exclusivamente Guard Break
│
├── Execução
│   ├── preparação
│   ├── carregamento quando aplicável
│   ├── execução
│   ├── Recovery
│   └── Cooldown individual
│
├── Desenvolvimento
│   ├── aquisição
│   ├── aprendizado
│   └── Mastery individual
│
├── Defesa avançada
│   └── aprendida através de técnicas
│
├── Mobilidade
│   ├── Dodge universal
│   └── mobilidade espiritual/racial aprendida
│
└── Controle
    ├── Stagger
    ├── Knockback
    ├── Launch
    └── Knockdown
```

---

# 67. Princípios consolidados

O sistema desenvolvido até este ponto segue os seguintes princípios:

### Simples na base, profundo no desenvolvimento

O jogador começa com uma linguagem de combate pequena e compreensível.

Seu repertório aumenta conforme aprende.

### Técnica não é atributo

Atributos representam quem o personagem é em sua fundação.

Técnicas representam o que ele aprendeu.

Mastery representa o quanto domina aquilo.

### Poder bruto não substitui conhecimento

Possuir atributos altos não concede automaticamente técnicas.

### Conhecimento não substitui poder bruto

Mastery melhora a técnica, inclusive moderadamente sua eficácia, mas não substitui atributos fundamentais.

### Flexibilidade com limites

O jogador poderá combinar repertórios e inspirações de personagens diferentes quando legitimamente compatíveis.

Isso não significa acesso irrestrito a qualquer capacidade.

### Sem classes fechadas de personagem canônico

O jogador poderá se tornar parecido com determinado personagem através de seu repertório, sem precisar selecionar permanentemente uma classe baseada nele.

### Identidade racial sem simetria artificial

Raças utilizarão a arquitetura universal quando útil, mas poderão possuir experiências e repertórios profundamente diferentes.

### Complexidade somente quando possui função

Novos medidores, propriedades ou regras só deverão existir quando resolverem um problema claro.

A Postura exemplifica esse princípio:

```text
Postura
→ existe somente para Guard Break
```

### Bleach como referência, mod como adaptação

A obra orienta fantasia e coerência.

O gameplay determina a implementação final.

---

# 68. Pendências deliberadas

Os seguintes valores e detalhes permanecem propositalmente indefinidos:

- escala numérica máxima de Mastery;
- fórmulas de dano;
- influência exata de Mastery no dano;
- custos específicos de técnicas;
- valores de Stamina;
- valores de Energia Espiritual;
- durações de Recovery;
- cooldowns;
- velocidade de recuperação de Postura;
- dano de Postura;
- duração de Guard Break;
- detalhes de anti-stunlock;
- distâncias de Dodge;
- velocidades de movimentação especial;
- tempos de carregamento;
- curvas de progressão;
- balanceamento PvE/PvP.

Esses elementos deverão ser definidos posteriormente através de:

```text
arquitetura
+
conteúdo racial
+
protótipo
+
simulação
+
playtest
+
balanceamento
```

Não deverão ser fechados prematuramente apenas para preencher números.

---

# M06-07 — Estrutura Shinigami

## Estado do bloco

**Status:** ESPECIFICADO EM NÍVEL ESTRUTURAL

Este bloco define a estrutura de habilidades e combate do caminho Shinigami dentro do Módulo 06.

O objetivo não é catalogar neste momento todas as técnicas existentes em Bleach, mas estabelecer uma arquitetura capaz de receber posteriormente esse conteúdo sem exigir reformulação dos sistemas fundamentais.

A estrutura Shinigami é baseada em quatro grandes campos de formação:

- Zanjutsu;
- Hakuda;
- Hohō;
- Kidō.

Essas disciplinas compartilham os sistemas universais já definidos no M06, mas não precisam possuir a mesma quantidade de conteúdo, a mesma progressão concreta ou o mesmo comportamento.

---

# 1. Princípio de implementação incremental

## M06-P04 — DECIDIDO — Complexidade incremental

Os sistemas do mod devem ser projetados para permitir aprofundamento futuro, mas sua primeira implementação não precisa conter toda a profundidade prevista pela arquitetura.

A primeira versão deve priorizar:

- funcionamento sólido;
- identidade reconhecível;
- diversão;
- legibilidade;
- possibilidade de expansão futura.

Uma implementação simples não será considerada provisória ou inadequada apenas por possuir menos profundidade.

O objetivo é:

```text
V1
→ simples
→ funcional
→ divertida
→ coerente
→ extensível

versões posteriores
→ refinamentos
→ novas técnicas
→ novas interações
→ maior profundidade
```

Não será necessário desenvolver todas as nuances possíveis antes de validar o núcleo do combate.

---

## M06-P05 — DECIDIDO — Escopo inicial das disciplinas

Zanjutsu, Hakuda, Hohō e Kidō poderão possuir arquiteturas que suportem repertórios, aplicações contextuais e interações mais avançadas do que aquelas presentes em suas primeiras implementações.

A V1 deverá entregar um núcleo suficientemente interessante de cada disciplina sem exigir imediatamente:

- todo repertório canônico;
- todas as variações de estilo;
- todas as técnicas contextuais imaginadas;
- todas as interações avançadas entre disciplinas.

A expansão deverá aproveitar a mesma arquitetura sempre que possível.

---

# 2. Identidade geral do caminho Shinigami

## M06-07A — DECIDIDO — Formação ampla sem classes permanentes

Zanjutsu, Hakuda, Hohō e Kidō constituem campos gerais de formação Shinigami.

Eles não serão tratados como classes mutuamente exclusivas.

Um jogador poderá concentrar sua progressão inicialmente em determinada disciplina sem perder permanentemente acesso às demais.

Exemplo:

```text
Jogador A
→ foco inicial em Zanjutsu

Jogador B
→ foco inicial em Kidō + Hohō

Jogador C
→ foco inicial em Hakuda

todos continuam capazes de desenvolver
outras disciplinas posteriormente
```

A escolha principal será:

> no que o personagem investirá primeiro?

e não:

> qual disciplina ficará permanentemente bloqueada?

---

## M06-07B — DECIDIDO — REVISADO — Proficiência das disciplinas

As disciplinas possuirão uma progressão geral de **Proficiência**.

Essa decisão substitui propostas anteriores de utilizar apenas técnicas + Mastery ou de criar sistemas separados de Rank.

A Proficiência responde:

> Qual é o desenvolvimento geral do personagem nessa disciplina?

Exemplo conceitual:

```text
Zanjutsu
Proficiência X

Hakuda
Proficiência Y

Hohō
Proficiência Z

Kidō
Proficiência W
```

A escala numérica definitiva ainda não foi estabelecida.

---

## M06-07B.1 — DECIDIDO — Proficiência ≠ atributos ≠ Mastery

Cada camada possui responsabilidade própria.

```text
ATRIBUTOS
→ capacidade fundamental do personagem

PROFICIÊNCIA
→ desenvolvimento geral em uma disciplina

TÉCNICAS
→ conhecimentos e ações efetivamente aprendidos

MASTERY
→ domínio individual de uma técnica específica
```

Exemplo:

Um jogador pode possuir grande Proficiência de Zanjutsu e aprender uma nova técnica.

Nesse momento:

```text
Zanjutsu
→ Proficiência alta

nova técnica
→ Mastery baixa
```

O personagem é um espadachim experiente, mas ainda precisa dominar aquela nova execução.

---

## M06-07B.2 — DECIDIDO — Sem segundo nível individual

Técnicas individuais não possuirão simultaneamente:

```text
Level
+
Mastery
```

Após uma técnica ser adquirida, sua progressão individual será representada pela Mastery.

Evitar:

```text
Cero Level 27
Cero Mastery 53
```

ou:

```text
Técnica de Zanjutsu Level 18
Mastery 71
```

Esse tipo de duplicação foi descartado.

---

# 3. Como a Proficiência progride

## M06-07G — DECIDIDO — Progressão orgânica

A Proficiência será desenvolvida através de:

- utilização válida da disciplina;
- treinamento;
- técnicas relacionadas;
- combate relevante;
- desafios apropriados.

Não será criada uma moeda separada utilizada para comprar Proficiência.

Fluxo conceitual:

```text
prática legítima
+
treinamento
+
uso real
↓
progresso da disciplina
↓
Proficiência aumenta
```

---

## M06-07H — DECIDIDO — Progressão moderada

A progressão deverá exigir investimento real, mas não deverá depender de grind excessivo.

O objetivo é permitir que Proficiência e Mastery cresçam naturalmente enquanto o jogador:

- avança a storyline;
- enfrenta inimigos;
- realiza quests;
- treina;
- utiliza seu repertório.

Não é desejado exigir horas de repetição desconectadas do restante do jogo apenas para alcançar um marco necessário.

---

## M06-07I — DECIDIDO — Progressão simultânea

Uma utilização legítima de determinada técnica poderá desenvolver simultaneamente:

```text
Proficiência da disciplina
+
Mastery da técnica utilizada
```

As progressões não são redundantes.

Uma mede experiência geral naquele campo.

A outra mede domínio específico daquela execução.

---

## M06-07J — DECIDIDO — Anti-farm contextual

O sistema deverá reduzir ou impedir progressão através de:

- ações AFK;
- alvos completamente irrelevantes;
- repetição artificial;
- spam sem contexto;
- loops criados apenas para gerar progresso.

Entretanto, essas verificações deverão permanecer majoritariamente internas.

O jogador comum deverá compreender uma regra simples:

> praticar legitimamente a disciplina faz o personagem evoluir.

---

# 4. Aquisição das disciplinas e das técnicas

## M06-03C — DECIDIDO — REFINADO — Papel dos mestres

Mentores não funcionarão como lojas de habilidades.

Também não será necessário encontrar um NPC diferente ou retornar ao mesmo NPC para aprender individualmente toda técnica existente.

Mentores poderão ensinar:

- uma disciplina;
- seus fundamentos;
- um repertório especializado;
- uma técnica específica;
- um conceito necessário para progressão.

A granularidade dependerá da importância do conteúdo.

---

## M06-03D — DECIDIDO — REFINADO — Desenvolvimento autônomo

Depois que o personagem receber formação suficiente em determinada disciplina, parte de seu repertório poderá ser desenvolvida através da própria progressão.

Modelo:

```text
encontra mestre
↓
aprende fundamentos
↓
disciplina desbloqueada
↓
Proficiência cresce
↓
novas técnicas gerais tornam-se disponíveis
↓
personagem continua evoluindo sem retornar
ao mestre para cada capacidade
```

Isso evita transformar a progressão em uma sequência constante de viagens burocráticas entre NPCs.

---

## M06-07K — DECIDIDO — Técnicas especiais continuam especiais

Nem toda capacidade será adquirida automaticamente através de Proficiência.

Técnicas específicas poderão exigir:

- mentor;
- treinamento especial;
- evento;
- quest;
- descoberta;
- condição narrativa;
- conhecimento anterior;
- outra forma coerente de aquisição.

A regra geral será progressão natural.

Exceções existirão quando agregarem valor.

---

# 5. Repertórios especializados

Um repertório especializado representa uma maneira particular de aplicar uma disciplina.

Pode estar associado a:

- treinamento específico;
- escola;
- personagem;
- mestre;
- organização;
- tradição;
- aplicação avançada.

Ele não constitui uma nova classe.

---

## M06-07C — DECIDIDO — Formação geral e especialização

A progressão Shinigami possuirá uma base relativamente ampla de formação, enquanto conhecimentos intermediários, avançados ou especializados poderão estar distribuídos pelo mundo.

Mestres, instituições e organizações orientam a progressão sem aprisionar permanentemente o jogador a um único estilo.

---

## M06-07J.1 — DECIDIDO — Mestres podem ensinar repertórios

Um treinamento especializado poderá desbloquear conhecimento suficiente para desenvolver múltiplas técnicas relacionadas.

Exemplo:

```text
treinamento com mestre especializado
↓
repertório conhecido
↓
Técnica A passa a ser desenvolvível
Técnica B passa a ser desenvolvível
Técnica C exige maior Proficiência
```

Não é necessário:

```text
NPC → Técnica A

volta ao NPC → Técnica B

volta ao NPC → Técnica C
```

quando isso não acrescentar nada à experiência.

---

## M06-07T — DECIDIDO — Repertório não é classe

Aprender um repertório especializado não impede o personagem de posteriormente aprender outro repertório compatível.

O estilo final surge da combinação de conhecimentos desenvolvidos pelo próprio jogador.

---

## M06-07T.1 — DECIDIDO — Sem Proficiência adicional para repertórios

Repertórios especializados não possuirão, por padrão, uma segunda progressão de Proficiência.

Modelo:

```text
Zanjutsu
→ Proficiência geral

Repertório especializado
→ conhecido ou não conhecido

Técnicas desse repertório
→ Mastery individual
```

Evitar:

```text
Zanjutsu Proficiência
+
Repertório A Proficiência
+
Técnica X Mastery
```

sem necessidade real.

---

# 6. Visualização da progressão

## M06-07L — DECIDIDO — Lista/repertório em vez de Skill Tree

Uma Skill Tree tradicional não será utilizada como estrutura universal de aquisição.

A proposta de Skill Tree foi analisada e posteriormente descartada.

O motivo principal é que técnicas não serão simplesmente compradas através de um menu.

> **Qualificação REV-SP-01:** repertório e aprendizado contextual continuam, mas sua apresentação não elimina compras por Spiritual Points. Skill tree obrigatória e compra de Mastery não foram restauradas; o componente comprável de cada conteúdo precisa ser declarado.

A visualização futura deverá funcionar mais como um **repertório da disciplina**, podendo mostrar estados como:

```text
APRENDIDO
✓ Técnica A
  Mastery ...

DISPONÍVEL PARA DESENVOLVIMENTO
○ Técnica B

REQUER CONHECIMENTO ESPECIAL
◇ Técnica C

BLOQUEADO
🔒 Técnica D

NÃO DESCOBERTO
???
```

A interface definitiva pertence ao M14.

---

# 7. Técnicas híbridas

## M06-07D — DECIDIDO

Uma técnica poderá combinar fundamentos de múltiplas disciplinas.

Exemplo conceitual:

```text
Hakuda
+
Kidō
↓
técnica híbrida
```

A técnica híbrida continua sendo uma capacidade individual.

Não é necessário criar:

- uma quinta disciplina;
- uma Proficiência híbrida;
- outra árvore de progressão.

Ela poderá simplesmente exigir desenvolvimento apropriado nas disciplinas relacionadas.

---

# 8. Zanjutsu

## Identidade

Zanjutsu representa desenvolvimento geral no combate com espada.

Ele não representa automaticamente o poder específico da Zanpakutō.

Distinção fundamental:

```text
ZANJUTSU
→ como o personagem luta com a espada

ZANPAKUTŌ
→ o que aquela espada/poder específico consegue fazer
```

---

## M06-07E — DECIDIDO — Zanjutsu ≠ poder da Zanpakutō

Técnicas oriundas diretamente da habilidade específica de uma Zanpakutō pertencem ao conjunto do poder principal.

Elas não serão tratadas como técnicas gerais de Zanjutsu apenas porque são executadas utilizando uma espada.

---

## M06-07N — DECIDIDO — Fontes de Proficiência de Zanjutsu

A Proficiência de Zanjutsu poderá crescer através de:

- combate armado válido;
- utilização legítima de técnicas de Zanjutsu;
- treinamentos apropriados;
- desafios relacionados.

Kills poderão ajudar a determinar relevância de um combate, mas não serão a única ou principal medida de progresso.

Participação válida importa mais do que o último golpe.

---

## M06-07O — DECIDIDO — Mastery e Proficiência simultâneas

Utilizar validamente uma técnica de Zanjutsu poderá desenvolver:

```text
Mastery da técnica
+
Proficiência geral de Zanjutsu
```

---

## M06-07P — DECIDIDO — Anti-farm

Combater repetidamente alvos completamente irrelevantes, atacar objetos indefinidamente ou executar loops artificiais não deverá constituir estratégia eficiente de progressão.

---

## M06-07Q — DECIDIDO — Fundamentos evoluem

Nem toda melhoria no combate de espada precisa constituir uma técnica individual.

A Proficiência poderá ampliar ou refinar elementos fundamentais, como:

- sequências;
- finalizadores;
- transições;
- possibilidades contextuais;
- fluidez geral do moveset.

Isso não significa conceder grandes multiplicadores automáticos de dano.

---

## M06-07R — DECIDIDO — Técnicas gerais por progressão

Parte das técnicas gerais poderá tornar-se disponível através de marcos de Proficiência.

Dependendo da importância da capacidade:

```text
marco alcançado
→ fundamento incorporado automaticamente
```

ou:

```text
marco alcançado
→ técnica disponível para treinamento
→ processo simples de desenvolvimento
→ técnica adquirida
```

A granularidade não será obrigatoriamente igual para todo conteúdo.

---

## M06-07S — DECIDIDO — Proficiência como preparo

Determinados mestres, técnicas ou repertórios poderão exigir desenvolvimento mínimo de Zanjutsu.

Proficiência suficiente representa preparo.

Ela não concede automaticamente conhecimento especializado.

---

## M06-07U — DECIDIDO — Zanjutsu e Zanpakutō independentes

Alta Proficiência de Zanjutsu não concede automaticamente:

- Shikai;
- Bankai;
- técnicas específicas da Zanpakutō;
- Mastery dessas técnicas.

Essas progressões permanecem separadas.

---

# 9. Zanpakutō como poder vinculado

## M06-07V — DECIDIDO — Uma Zanpakutō vinculada

Cada personagem Shinigami possuirá uma identidade de Zanpakutō principal vinculada.

Essa identidade determina qual conjunto canônico de poder pertence àquele personagem.

A regra normal será:

```text
1 personagem
↓
1 Zanpakutō principal
```

O jogador não colecionará Zanpakutō para alternar poderes livremente.

---

## M06-07W — DECIDIDO — Espada como portadora técnica

A Zanpakutō será representada por um item/espada que carrega tecnicamente sua identidade.

Esse item poderá permitir ao sistema identificar:

- modelo;
- nome;
- poder;
- técnicas;
- liberações;
- estados;
- propriedades relacionadas.

Entretanto, a identidade não dependerá exclusivamente da posse física do item.

O personagem também possuirá registro de seu vínculo.

Conceitualmente:

```text
PLAYER
bound_zanpakuto = ryujin_jakka

ITEM
zanpakuto_id = ryujin_jakka
```

Os dois elementos trabalham em conjunto.

---

## M06-07X — DECIDIDO — Exclusividade do vínculo

Empunhar uma Zanpakutō incompatível não transfere automaticamente seu poder.

Exemplo:

```text
Jogador
→ vinculado a Senbonzakura

pega item de Ryūjin Jakka
↓
não passa a possuir Ryūjin Jakka
```

Trocar de item no inventário não substitui o poder principal do personagem.

---

## M06-07Y — DECIDIDO — Zanpakutō não é loot comum

A Zanpakutō vinculada não será tratada como uma espada comum cuja:

- perda;
- morte;
- roubo;
- duplicação;
- transferência;

passa automaticamente seu poder para outra pessoa.

O vínculo real permanece associado ao personagem.

A implementação deverá ser resiliente a problemas de inventário e multiplayer.

---

## M06-07Z — DECIDIDO EM PRINCÍPIO — Relação adaptada com a Zanpakutō

O relacionamento entre Shinigami e Zanpakutō continuará sendo parte importante da fantasia do caminho.

Entretanto, ele será adaptado às necessidades do mod.

Não será necessário reproduzir literalmente todas as formas de interação mostradas na obra.

O jogo poderá representar essa relação através de:

- quests;
- treinamentos;
- desafios;
- eventos;
- mundo interior;
- meditação;
- outras abstrações apropriadas.

Os detalhes pertencem ao M07.

---

## PENDENTE PARA M07

O M06 não fechará:

- como a Zanpakutō é atribuída;
- como ocorre o vínculo inicial;
- como seu nome é revelado;
- como Shikai é conquistada;
- como técnicas da Shikai progridem;
- como Bankai é conquistada;
- como o domínio das liberações funciona;
- quais requisitos existem entre essas etapas.

Esses elementos pertencem ao módulo de formas e poderes superiores.

---

# 10. Hakuda

## Identidade

Hakuda representa o desenvolvimento do corpo como instrumento de combate.

Ele compartilha a infraestrutura universal de combate, mas não será tratado simplesmente como “Zanjutsu sem espada”.

---

## M06-07HA — DECIDIDO — Proficiência de Hakuda

Hakuda utilizará a mesma arquitetura geral:

```text
Proficiência de Hakuda
+
técnicas de Hakuda
+
Mastery individual
```

Sua Proficiência poderá crescer através de:

- combate corporal válido;
- técnicas de Hakuda;
- treinamentos;
- desafios apropriados.

---

## M06-07HB — DECIDIDO — Moveset corporal

A evolução de Hakuda poderá modificar de maneira significativa o moveset desarmado.

Exemplos:

- sequências de socos;
- chutes;
- finalizadores;
- ataques contextuais;
- entradas;
- respostas após Dodge.

Nem todo movimento corporal será tratado como técnica individual.

---

## M06-07HC — DECIDIDO — Fundamento versus técnica

Movimentos que constituem partes naturais do combate corporal permanecerão como fundamentos.

Capacidades suficientemente distintas poderão tornar-se técnicas individuais.

Uma técnica individual poderá possuir:

- aquisição;
- custo;
- Recovery;
- Cooldown;
- requisitos;
- Mastery;
- efeitos próprios.

---

# 11. Hakuda e estado da Zanpakutō

## M06-07HD — DECIDIDO EM PRINCÍPIO — Empunhar/embainhar

A Zanpakutō poderá possuir estados de:

```text
empunhada
↕
embainhada
```

Essa alteração não exige mudar manualmente de slot ou manipular o inventário vanilla.

Seu objetivo é permitir transição natural entre combate armado e corporal.

O controle exato será definido posteriormente.

---

## M06-07HE — DECIDIDO EM PRINCÍPIO — Moveset por estado

Como regra fundamental:

```text
Zanpakutō empunhada
→ moveset armado/Zanjutsu

Zanpakutō embainhada
→ moveset corporal/Hakuda
```

Isso afeta principalmente ataques fundamentais.

---

## M06-07HF — DECIDIDO — Técnicas podem atravessar a fronteira

Hakuda não será rigidamente proibido enquanto uma arma estiver empunhada.

Uma técnica individual poderá declarar:

```text
requer combate desarmado
```

ou:

```text
permitida armado/desarmado
```

quando apropriado.

Isso possibilita estilos híbridos sem eliminar a identidade dos movesets fundamentais.

---

# 12. Stamina e Hakuda

## M06-07HG — DECIDIDO EM PRINCÍPIO

Hakuda possui relação natural com Stamina.

Entretanto:

```text
soco básico
chute básico
sequência fundamental
```

não deverão consumir grandes quantidades apenas por serem ataques corporais.

Stamina deverá possuir peso principalmente em:

- técnicas físicas exigentes;
- combos especiais;
- projeções;
- movimentos explosivos;
- ações de alto esforço.

---

# 13. Técnicas híbridas de Hakuda

## M06-07HH — DECIDIDO

Técnicas que misturam Hakuda com outra disciplina poderão exigir desenvolvimento relevante em ambas.

Ter alta Proficiência em apenas uma disciplina não concede automaticamente uma técnica híbrida importante.

---

# 14. Repertórios especializados de Hakuda

## M06-07HI — DECIDIDO

Hakuda poderá possuir repertórios especializados ensinados por mestres ou outras fontes apropriadas.

Esses repertórios representam aplicações particulares da disciplina.

Eles não constituem classes permanentes.

---

# 15. Técnicas de combo em Hakuda

## M06-07HJ — DECIDIDO — Combos como técnicas

Hakuda poderá possuir técnicas que representam sequências completas de golpes.

Exemplo:

```text
ativa Técnica X
↓
soco
↓
chute
↓
avanço
↓
novo impacto
↓
finalizador
```

A sequência inteira constitui uma única técnica.

Cada soco ou chute não precisa ser uma skill diferente.

---

## M06-07HK — DECIDIDO — Combos não substituem combate fundamental

Mesmo com técnicas de combo, o jogador continuará possuindo um moveset corporal funcional.

Hakuda não deverá se transformar em:

```text
pressiona skill
↓
personagem luta sozinho
↓
espera próximo cooldown
```

Técnicas complementam o combate fundamental.

---

## M06-07HL — DECIDIDO EM PRINCÍPIO — Comprometimento dos combos

Técnicas de combo poderão variar em:

- duração;
- mobilidade;
- cancelamento;
- Recovery;
- Stamina;
- controle;
- risco.

Combos mais longos ou fortes poderão exigir maior comprometimento.

---

# 16. Hohō

## M06-07HO-A — DECIDIDO — Hohō é uma disciplina

Hohō representa formação geral em movimentação e deslocamento avançado Shinigami.

Ele não será reduzido exclusivamente ao Shunpo.

---

## M06-07HO-B — DECIDIDO — Shunpo como técnica fundamental

Shunpo será uma capacidade fundamental aprendida dentro de Hohō.

Ele possuirá Mastery própria.

Distinção:

```text
Hohō Proficiência
→ experiência geral de movimentação Shinigami

Shunpo Mastery
→ domínio específico do Shunpo
```

---

## M06-07HO-C — DECIDIDO EM PRINCÍPIO — Acesso relativamente inicial ao Shunpo

Shunpo deverá ser adquirido em uma etapa relativamente inicial do desenvolvimento de Hohō, pois constitui parte importante da identidade Shinigami.

Aplicações mais sofisticadas permanecem posteriores.

Nenhum marco numérico está definido.

---

## M06-07HO-D — DECIDIDO — Progressão relevante

Hohō poderá progredir através de:

- Shunpo utilizado de maneira relevante;
- mobilidade aplicada ao combate;
- perseguição/evasão relevante;
- treinamentos;
- técnicas avançadas.

Simplesmente percorrer longas distâncias ou repetir Shunpo sem contexto não deverá constituir método eficiente de progressão.

---

## M06-07HO-E — DECIDIDO — Fundamento versus técnica

Direções e pequenas variações naturais de movimentação não serão transformadas em skills individuais.

Evitar:

```text
Shunpo esquerda
Shunpo direita
Shunpo trás
Shunpo frente
```

como quatro habilidades independentes.

Aplicações suficientemente distintas poderão existir como técnicas próprias.

---

## M06-07HO-F — DECIDIDO — Dodge ≠ Hohō

Dodge universal e Hohō permanecerão sistemas separados.

```text
Dodge
→ reposicionamento defensivo básico
→ universal
→ Stamina

Shunpo
→ mobilidade avançada
→ aprendida
→ Hohō
```

Mesmo usuários avançados de Hohō continuam possuindo Dodge.

---

# 17. Evolução do Shunpo

## M06-07HO-G — DECIDIDO

Mastery de Shunpo poderá melhorar moderadamente propriedades da própria técnica.

Possibilidades incluem:

- eficiência;
- execução;
- controle;
- Recovery;
- distância dentro de limites;
- velocidade dentro de limites.

Entretanto:

> Mastery melhora Shunpo; ela não concede automaticamente novas técnicas de Hohō.

Exemplo:

```text
Mastery alta de Shunpo
→ Shunpo melhor

não automaticamente:
→ Utsusemi
→ técnicas contextuais especiais
→ novas aplicações avançadas
```

Esses elementos permanecem capacidades independentes.

---

## M06-07HO-H — DECIDIDO — V1 simplificada

A primeira implementação de Shunpo poderá ser relativamente simples.

Modelo inicial aceitável:

```text
input dedicado
+
direção
+
deslocamento rápido
+
custo
+
Recovery
+
Cooldown apropriado
```

Sua Mastery inicialmente poderá modificar apenas alguns parâmetros.

A arquitetura deverá permitir aprofundamento posterior.

---

# 18. Kidō

## Identidade

Kidō representa o campo geral das artes espirituais Shinigami.

Sua estrutura principal será:

```text
KIDŌ
│
├── Hadō
├── Bakudō
├── Kaidō
└── técnicas especiais/não convencionais
```

Essas categorias não precisam possuir mecânicas idênticas.

---

## M06-07KI-A — DECIDIDO — Proficiência única

Kidō possuirá uma única Proficiência geral.

Não serão criadas, por padrão:

```text
Proficiência de Kidō
+
Proficiência de Hadō
+
Proficiência de Bakudō
+
Proficiência de Kaidō
```

Hadō, Bakudō e Kaidō representam categorias/conhecimentos dentro da disciplina.

---

## M06-07KI-B — DECIDIDO — Mastery individual

Cada Kidō suficientemente distinto será uma técnica individual e possuirá sua própria Mastery.

---

## M06-07KI-C — DECIDIDO — Repertório por Proficiência

Grande parte do repertório geral poderá ser disponibilizada através do desenvolvimento da Proficiência de Kidō.

Não será necessário retornar a um instrutor para cada feitiço.

Kidō especiais poderão continuar exigindo aquisição própria.

---

# 19. Numeração de Kidō

## M06-07KI-D — DECIDIDO — REFINADO

A numeração canônica será preservada quando aplicável.

Ela serve como importante referência de:

- identidade;
- dificuldade;
- complexidade;
- progressão.

Entretanto:

```text
Hadō #4
≠
Proficiência obrigatória 4

Hadō #90
≠
Proficiência obrigatória 90
```

Os requisitos serão definidos pelo design do mod.

Em geral, Kidō de numeração elevada tenderão a exigir maior desenvolvimento, sem existir conversão matemática obrigatória.

---

# 20. Kidō de nível baixo não se torna automaticamente inútil

## M06-07KI-H — DECIDIDO

Kidō de numeração menor poderá continuar útil para personagens avançados.

Sua relevância poderá permanecer através de:

- custo;
- velocidade;
- função;
- Mastery;
- atributos;
- aplicação tática.

Desbloquear Kidō de numeração maior não deverá necessariamente transformar todos os anteriores em versões obsoletas.

---

# 21. Execução e encantamento

## M06-07KI-E — DECIDIDO — REVISADO

Kidō poderá possuir diferentes modelos de execução.

Possibilidades:

```text
cast imediato

preparação

quick cast
versus
encantamento completo

canalização

execução específica
```

Nenhum modelo será obrigatório para toda a disciplina.

---

## Quick Cast × Encantamento

Quando apropriado, uma técnica poderá utilizar:

```text
TAP
→ execução rápida
→ encantamento omitido/reduzido
→ menor eficácia

HOLD
→ personagem realiza encantamento
→ maior preparação
→ versão mais completa
```

O jogador não precisará:

- falar no microfone;
- recitar pessoalmente;
- digitar encantamentos;
- memorizar textos.

A recitação pertence ao personagem.

Pode ser representada por:

- texto;
- animação;
- efeitos;
- áudio;
- tempo de preparação.

Essa estrutura não é apenas uma simplificação da V1; poderá permanecer como solução definitiva.

---

## M06-07KI-F — DECIDIDO — Mastery e encantamento

Mastery poderá melhorar a capacidade de utilizar determinado Kidō de forma abreviada.

Entretanto, Mastery elevada não elimina automaticamente a utilidade de uma execução completa quando esta fizer parte da técnica.

---

# 22. Recursos e atributos de Kidō

## M06-07KI-G — DECIDIDO

Energia Espiritual será o recurso primário esperado para a maior parte dos Kidō.

Outros elementos poderão variar conforme a técnica.

Atributos podem contribuir de formas diferentes.

Exemplo conceitual:

```text
Potência
→ capacidade ofensiva quando aplicável

Reserva
→ quantidade de energia disponível

Controle
→ precisão / eficiência / estabilidade

Mastery
→ domínio do Kidō específico
```

As fórmulas permanecem pendentes.

---

# 23. Hadō

Hadō representa principalmente a vertente ofensiva de Kidō.

Ele não deverá ser reduzido a uma sequência linear de:

```text
projétil pequeno
↓
projétil médio
↓
projétil grande
```

Diferentes Hadō poderão possuir funções distintas em:

- alcance;
- área;
- velocidade;
- pressão;
- dano;
- comportamento;
- preparação;
- custo.

O sistema de quick cast versus encantamento provavelmente terá grande utilidade em Hadō, mas continua dependente da técnica.

---

# 24. Bakudō

## M06-07KI-I — DECIDIDO — Identidade ampla

Bakudō não será tratado simplesmente como categoria de stun.

Poderá incluir funções como:

- contenção;
- controle;
- barreiras;
- defesa;
- selamento;
- rastreamento;
- utilidade;
- suporte.

Cada técnica preservará sua própria identidade.

---

## M06-07KI-J — DECIDIDO — Controle balanceado

Bakudō poderá produzir controle forte.

Entretanto, técnicas que retiram capacidade de resposta do alvo deverão respeitar:

- duração apropriada;
- anti-stunlock;
- resistência relevante;
- regras próprias da técnica;
- balanceamento PvP/PvE.

Controle deve gerar vantagem e oportunidade.

Não deve remover indefinidamente o direito de resposta do adversário.

---

## M06-07KI-N — DECIDIDO — Sem carregamento artificial

Bakudō não será obrigado a possuir:

```text
tap = versão fraca
hold = versão forte
```

Barreiras, contenções, defesas ou capacidades utilitárias poderão ser ativadas diretamente através de um único input quando isso produzir a melhor adaptação.

Uma barreira poderá simplesmente:

```text
input
↓
consumo de Energia
↓
barreira criada
↓
Cooldown
```

Preparação, encantamento ou canalização só serão adicionados quando fizerem sentido para aquela técnica.

---

# 25. Kaidō

## M06-07KI-K — DECIDIDO — Formação especializada

Kaidō utiliza a base geral de Kidō, mas conhecimento de cura deverá exigir formação especializada.

Alta Proficiência em Kidō não significa automaticamente saber curar.

Exemplo:

```text
Kidō desenvolvido
+
treinamento específico de Kaidō
↓
capacidade de utilizar cura
```

Não será criada obrigatoriamente uma segunda Proficiência exclusiva de Kaidō.

---

## M06-07KI-L — DECIDIDO EM PRINCÍPIO — Canalização

O comportamento fundamental de Kaidō poderá utilizar canalização.

Modelo conceitual:

```text
ativa/segura técnica
↓
canalização começa
↓
Energia Espiritual é consumida
↓
Vida do alvo é restaurada
↓
input é liberado
↓
canalização termina
```

Técnicas específicas poderão posteriormente possuir comportamentos diferentes.

---

## M06-07KI-M — DECIDIDO — Vida como função primária

A função primária de Kaidō será recuperação de Vida.

Kaidō básico não restaurará automaticamente:

- Vida;
- Stamina;
- Energia Espiritual;
- todas as condições negativas;

ao mesmo tempo.

Efeitos adicionais dependerão de capacidades específicas.

Isso evita loops ou excesso de funções concentradas numa única técnica.

---

# 26. Kidō de suporte

Não será criada uma categoria universal adicional denominada:

```text
Support Kidō
```

Grande parte das funções de suporte poderá permanecer dentro de Bakudō ou Kaidō conforme sua natureza.

Kidō que não se encaixem adequadamente nessas categorias poderão ser tratados individualmente como técnicas especiais.

---

# 27. Kidō especiais

Técnicas avançadas ou formas excepcionais de utilizar Kidō poderão futuramente incluir mecanismos próprios.

Exemplos conceituais:

- formas avançadas de encantamento;
- utilização simultânea;
- técnicas proibidas;
- execuções excepcionais;
- conhecimentos especiais.

Esses elementos não precisam estar presentes na primeira versão.

**Status:** BACKLOG / CONTEÚDO FUTURO

A arquitetura atual deverá apenas evitar impedir sua implementação posterior.

---

# 28. Estrutura Shinigami consolidada

```text
                         SHINIGAMI
                             │
             ┌───────────────┼───────────────┐
             │               │               │
        DISCIPLINAS      ZANPAKUTŌ       ATRIBUTOS
             │               │
   ┌─────────┼─────────┐     └── poder principal
   │         │         │
Zanjutsu   Hakuda    Hohō     Kidō
   │         │         │        │
   └─────────┴─────────┴────────┘
             │
        PROFICIÊNCIA
             │
             ↓
     REPERTÓRIO / TÉCNICAS
             │
             ↓
          MASTERY
```

Mais precisamente:

```text
ZANJUTSU
→ combate com espada

HAKUDA
→ combate corporal

HOHŌ
→ movimentação avançada

KIDŌ
→ artes espirituais

ZANPAKUTŌ
→ poder individual principal
```

Essas camadas podem interagir, mas não serão fundidas em um único sistema.

---

# 29. Relação entre disciplinas

Um Shinigami poderá construir seu próprio estilo combinando diferentes desenvolvimentos.

Exemplo:

```text
Zanjutsu alto
+
Hohō alto
+
Kidō moderado
+
Hakuda baixo
```

ou:

```text
Hakuda alto
+
Hohō alto
+
Kidō especializado
+
Zanjutsu moderado
```

A progressão não exige simetria entre as quatro disciplinas.

O personagem se diferencia principalmente pelo que decidiu desenvolver primeiro e pelo repertório que efetivamente adquiriu.

---

# 30. Relação entre disciplina, técnica e Mastery

Regra consolidada:

```text
DISCIPLINA
→ área geral de formação

PROFICIÊNCIA
→ experiência geral naquela área

TÉCNICA
→ capacidade específica aprendida

MASTERY
→ domínio individual daquela capacidade
```

Nenhum desses elementos deverá substituir os demais.

---

# 31. Regra de granularidade

Não haverá uma obrigação universal para decidir que toda ação precisa ser uma técnica.

A pergunta será:

> Esta ação possui identidade suficiente para justificar aquisição e Mastery próprias?

Se não:

```text
fundamento / moveset
```

Se sim:

```text
técnica individual
```

Essa regra se aplica a todas as disciplinas.

---

# 32. Primeira versão recomendada

A arquitetura permite uma primeira implementação relativamente enxuta.

## Zanjutsu V1

Pode começar com:

- moveset armado funcional;
- sequência básica;
- finalizador;
- bloqueio;
- Proficiência;
- pequeno conjunto de técnicas;
- Mastery dessas técnicas.

## Hakuda V1

Pode começar com:

- moveset desarmado;
- socos e chutes;
- sequência corporal;
- finalizador;
- algumas técnicas de combo;
- Proficiência;
- Mastery.

## Hohō V1

Pode começar com:

- Proficiência;
- Shunpo;
- movimentação direcional;
- custo;
- Recovery;
- Cooldown;
- Mastery de Shunpo.

## Kidō V1

Pode começar com:

- Proficiência;
- pequeno conjunto de Hadō;
- pequeno conjunto de Bakudō;
- sistema básico de encantamento quando necessário;
- pequena implementação de Kaidō caso esteja no escopo inicial;
- Mastery individual.

A V1 não precisa demonstrar todo o potencial da arquitetura para validá-la.

---

# 33. Decisões revisadas ou superadas

Para evitar conflitos futuros, ficam explicitamente registradas as propostas abandonadas.

## SUPERADO — Desenvolvimento apenas por atributos + técnicas + Mastery

Foi considerada insuficiente por não fornecer um indicador geral claro de desenvolvimento em cada disciplina.

**Substituído por:**

```text
atributos
+
Proficiência da disciplina
+
técnicas
+
Mastery
```

---

## SUPERADO — Níveis/Ranks separados de disciplina

Foi analisado um sistema de Ranks.

Posteriormente, concluiu-se que a **Proficiência direta da disciplina** oferece uma progressão mais simples e suficientemente clara.

Rank poderá existir futuramente apenas como rótulo visual derivado da Proficiência caso seja útil, não como uma segunda progressão obrigatória.

---

## SUPERADO — Skill Tree como estrutura de progressão

Foi analisada a utilização de Skill Trees.

A proposta foi descartada porque técnicas não serão adquiridas principalmente através de compra em menu.

> **Qualificação REV-SP-01:** repertório e aprendizado contextual continuam, mas sua apresentação não elimina compras por Spiritual Points. Skill tree obrigatória e compra de Mastery não foram restauradas; o componente comprável de cada conteúdo precisa ser declarado.

**Substituído por:**

> repertório/lista da disciplina.

---

## SUPERADO — NPC individual para toda técnica

A ideia de exigir mentor para cada habilidade foi considerada excessivamente burocrática para o tamanho potencial do repertório de Bleach.

**Substituído por:**

- formação inicial;
- progressão por Proficiência;
- desenvolvimento autônomo;
- mestres para repertórios e técnicas quando apropriado.

---

## SUPERADO — Tap/Hold obrigatório para todo Kidō

O modelo:

```text
tap = versão rápida
hold = versão completa
```

continua disponível e importante, mas não será obrigatório.

Cada Kidō utiliza apenas as formas de execução que realmente agreguem valor.

---

# 34. Pendências deliberadas

Ainda não serão fechados:

- escala máxima das Proficiências;
- velocidade de progressão;
- marcos exatos de desbloqueio;
- catálogo completo de técnicas;
- valores de custo;
- valores de dano;
- duração de Recovery;
- duração de Cooldown;
- fórmulas;
- repertórios canônicos completos;
- todos os mestres;
- todos os métodos especiais de aquisição;
- detalhes de UI;
- detalhes de Shikai e Bankai;
- progressão das técnicas específicas da Zanpakutō;
- balanceamento final PvE/PvP.

Esses elementos dependem de conteúdo, protótipo, playtest e módulos posteriores.

---

# 35. Auditoria final do M06-07

Após revisão das decisões do bloco Shinigami:

**Não foram identificadas contradições estruturais impeditivas.**

A arquitetura possui responsabilidades claras:

```text
Atributos
→ capacidade fundamental

Proficiência
→ desenvolvimento geral

Repertório
→ conhecimentos disponíveis

Técnicas
→ capacidades específicas

Mastery
→ domínio individual

Zanpakutō
→ poder principal vinculado

M07
→ formas e grandes liberações
```

Também não existe dependência obrigatória de Skill Tree, progressão baseada exclusivamente em NPCs ou grind excessivo.

A estrutura suporta uma primeira versão simples e expansão posterior sem exigir redesenho completo.

## Resultado da auditoria

**M06-07 — Estrutura Shinigami: APROVADO EM NÍVEL ESTRUTURAL**

O próximo bloco do Módulo 06 é:

# M06-08 — Hollow / Arrancar

## Estado do bloco — 20/10/2026

| Campo | Registro |
|---|---|
| Estado | `DECIDIDO` em nível estrutural; fechamento na seção 36. Fichas, parâmetros e integrações continuam em especificação |
| Responsável pela decisão | Equipe do Bleach Mod |
| Escopo atual | Regras gerais Hollow/Arrancar fechadas estruturalmente; responsabilidades pendentes reunidas na seção 36 |
| Decisões | M06-08A–D/F–K aprovados em 20/10/2026; M06-08E consolida diretrizes herdadas, sem aprovação dos candidatos de conteúdo |
| Herança | M04: H1–H9, RAC-06, RAC-20–25; M05; base universal M06 e revisões M06-07 |
| Evidência técnica | Leitura do checkout `8ace6520e67d27441974b9ad92b443ddb33f9882`; nenhuma implementação ou execução de gameplay nesta rodada |

**Histórico adicional recebido em 20/10/2026:** ver [reconciliação do handoff externo Hollow](00-reconciliacao-handoff-hollow-2026-10-20.md). O usuário recuperou discussões sobre predação, agência Gillian, identidade em Adjuchas e formas não mostradas. A análise e sua aprovação posterior estão separadas nesse registro. **M06-08G**, na seção 27, registra a direção aprovada e Grimmjow como primeira trajetória. Os IDs externos não substituem M06-08A–F. M04/H6 recebeu a revisão explícita REV-H6-01.

### 1. Objetivo e fantasia

Organizar um caminho que começa no próprio corpo, desenvolve capacidades espirituais e pode ramificar para Arrancar, preservando escolhas de treinamento e identidade individual.

O jogador deve compreender a diferença entre **evoluir**, **aprender uma capacidade**, **dominar sua execução** e **conquistar um poder principal**. Um estágio avançado não deve ser interpretado como conhecimento automático de todo o catálogo.

Esta rodada trata da estrutura. O funcionamento individual de Cero, Bala, Sonído, Hierro, percepção e outras capacidades será discutido em seguida, após conferir suas condições e exceções.

### 2. Contratos já decididos

Estas regras são herdadas e não estão sendo submetidas novamente à aprovação:

- H1/RAC-08: o corpo é a primeira arma; a compensação corporal inicial não significa superioridade permanente.
- H2/M05: fome e instinto não criam segunda barra permanente de manutenção; permanecem Vida, Stamina e Energia Espiritual. Postura mantém apenas sua função de Guard Break.
- H3/H4/RAC-24: sobrevivência, prática, grind válido e orientação contextual participam do aprendizado; não há Academia Hollow artificial nem contagem de mortes suficiente, sozinha, para grandes evoluções.
- H5: começo no Mundo Humano; Hueco Mundo é um marco posterior.
- H6–H9: evolução precede Arrancarização; Gillian, Adjuchas e Vasto Lorde oferecem ramificações compatíveis. Vasto Lorde é conquista permanente, exigente e determinística. Arrancarização anterior encerra a linha normal de evolução Hollow naquele ponto.
- RAC-20/23: um poder canônico principal por vez, conquistado por conteúdo associado ao personagem; desenvolver fundamentos não concede Pantera, Murciélago e Arrogante simultaneamente.
- RAC-06: derrota comum não muda natureza nem apaga a origem.
- M05/M06: atributos, Proficiência, aquisição e Mastery têm papéis distintos; Mastery não é comprada. Repertório substitui skill tree; técnicas não recebem Level e Mastery paralelos.

A pendência intermediária sobre Vasto Lorde em M04 já foi resolvida por H9, no fechamento posterior daquele documento.

### 3. Base canônica conferida e limites

Fontes consultadas em **20/10/2026**, com interpretação do texto japonês:

| Referência | Evidência acessível | Consequência para o bloco |
|---|---|---|
| [Glossário oficial TYBW](https://bleach-anime.com/keyword/), entrada `破面` | Arrancar é Hollow que adquiriu poderes Shinigami; sua arma sela capacidades próprias, liberadas em Resurrección. A entrada cita Hierro, Sonído e Cero. | Separar natureza, repertório geral e poder individual; não equiparar liberação a técnica comum. |
| Mesma entrada `破面` | Arrancars geralmente perderam a regeneração rápida Hollow. | Não presumir regeneração avançada universal nem transferência automática dessa capacidade. |
| [Ulquiorra — Bandai Namco](https://bleach-ros.bn-ent.net/character/?chara=ulquiorra-cifer), apresentação do personagem | Identifica a segunda liberação como particularidade de Ulquiorra entre os Espada. | Não criar Segunda Etapa como degrau obrigatório universal. |
| [Elenco de Hueco Mundo — TV Tokyo](https://www.tv-tokyo.co.jp/contents/bleach_test2/chara/index05.html), ficha de Aaroniero | Identifica Aaroniero como único Gillian entre os Espada e Glotonería como capacidade especial dele. | Classe de origem e posto são distintos; consumir um Hollow não concede universalmente cópia de suas técnicas. |

O site da Bandai descreve outro jogo licenciado: seus medidores, custos e combos não são regras deste mod. O glossário é um resumo oficial do anime; não comprova requisitos detalhados de todas as capacidades nem substitui cenas/capítulos do mangá quando necessários.

**Ainda a pesquisar antes das fichas:** elegibilidade e variantes de Cero/Bala; aquisição de Sonído; natureza e contrajogo de Hierro; Pesquisa; exceções de regeneração; anatomias e estágios Menos. Capítulo/episódio exato dessas condições não foi conferido nesta rodada. Não atribuir capacidades Arrancar a um Hollow recém-formado por extrapolação.

### 4. Modelo conceitual da primeira rodada

```text
IDENTIDADE E ESTADO EVOLUTIVO                 → M04 / M07
                ↓ compatibilidade
FUNDAMENTOS + REPERTÓRIO RACIAL               → M06
                ↓ aquisição apropriada
TÉCNICAS / CAPACIDADES + MASTERY APLICÁVEL     → M06

UM PODER CANÔNICO PRINCIPAL                   → RAC-20 / RAC-23
                ↓ técnicas próprias
LIBERAÇÃO E CONDIÇÕES DA FORMA                → M07
```

As setas indicam relações e requisitos possíveis, não uma sequência obrigatória de desbloqueios por menu.

### 5. M06-08A — DECIDIDO — Organização por campos de prática

**Decisão aprovada em 20/10/2026:** organizar o repertório geral em três campos funcionais. São categorias de design do mod, não três disciplinas oficialmente nomeadas na obra.

| Campo aprovado | O que organiza | Limite |
|---|---|---|
| Combate físico | Uso ofensivo do corpo e técnicas físicas compatíveis; combate armado Arrancar quando legitimamente disponível | Não concede formação Shinigami completa nem poderes individuais por empunhar uma espada |
| Emissão espiritual | Capacidades de projetar energia, como Cero e Bala, se adquiridas e compatíveis | Não presume que toda variante seja geral ou utilizável em qualquer estágio |
| Mobilidade | Desenvolvimento de deslocamentos raciais compatíveis, incluindo Sonído quando legitimamente disponível | Dodge universal continua separado; acesso a Sonído não é inicial por padrão |

**Percepção, proteção corporal e regeneração** permanecem capacidades a classificar individualmente. Não serão forçadas a esses campos nem receberão uma Proficiência extra apenas para completar a tabela. Preservar também a percepção espiritual universal e as skills de Reiatsu de M05, sem duplicá-las por raça.

O enquadramento de cada passiva e o compartilhamento de formação marcial com outras raças continuam abertos para as próximas rodadas. Por exemplo, classificar Hierro como proteção corporal não decide sua execução, progressão ou relação com Resistência/Postura.

**Alternativa considerada:** reproduzir as quatro disciplinas Shinigami com nomes Hollow. Isso facilitaria tabelas simétricas, mas criaria uma organização sem justificativa própria e conflitaria com H4/RAC-22. Não recomendo.

### 6. M06-08B — DECIDIDO — Desenvolvimento por campo e por capacidade

**Decisão aprovada em 20/10/2026:** alternativa A. Resposta da equipe: “Três campos com Proficiência + Mastery por técnica (recomendado)”. A escolha conjunta também aprova a organização de M06-08A. As alternativas abaixo ficam como registro da análise.

| Alternativa | Efeito para o jogador | Custo ou risco |
|---|---|---|
| **A. Proficiência por campo + Mastery individual — recomendada** | Prática física, emissão e mobilidade desenvolvem preparos distintos; cada técnica mantém seu domínio próprio | Acrescenta três indicadores e exige definir quais usos válidos alimentam cada campo |
| B. Uma Proficiência Hollow geral + Mastery individual | Um indicador geral simples para todo o repertório racial | Mistura práticas diferentes e pode se confundir com evolução; exige cuidados para não virar nível global racial |
| C. Aquisição + Mastery individual, sem Proficiência desses campos | Menos indicadores; desenvolvimento concentrado nas capacidades usadas | Não oferece leitura de preparo geral; precisa justificar a diferença em relação à formação aprovada em M06-07 |

**Por que recomendo A:** permite que um personagem experiente em combate físico ainda precise desenvolver emissão ou mobilidade. Reaproveita a distinção entre formação e domínio individual sem exigir que Hollow tenha a mesma estrutura institucional Shinigami.

A regra estrutural aprovada é:

```text
prática válida de um campo → Proficiência daquele campo
uso válido de técnica adquirida → Mastery daquela técnica
uso da técnica pertinente ao campo → ambos podem progredir
```

Não haverá segunda Proficiência de cada repertório, compra de Proficiência nem Level paralelo da técnica. Compatibilidade racial, conhecimento e requisitos específicos continuam necessários: Proficiência alta, sozinha, não concede transformação, poder principal ou toda técnica avançada.

Proficiência mede prática, não o tamanho do corpo ou a classe evolutiva. Receber dano passivamente, ficar AFK ou repetir ações sem relevância não vira treinamento eficiente. A regra para desenvolver passivas será decidida na ficha apropriada; não presumir XP por apanhar ou regenerar.

Escalas, velocidades e fórmulas continuam deliberadamente abertas. Também permanece para M07/M06-11 o tratamento de conhecimentos adquiridos quando o corpo/estado muda: preservar histórico e verificar compatibilidade de execução são questões distintas.

### 7. Adaptação para Minecraft e impacto técnico

Plataforma reconferida em `gradle.properties`/`build.gradle`: **Java 1.20.1, Forge 47.4.10, Java 17**.

| Interação | Contrato ou decisão a respeitar |
|---|---|
| Corpo, máscara e anatomia | São partes do personagem conforme M04; tamanho, hitbox, câmera, alcance, água e corredores exigem especificação própria, sem assumir escala literal do anime |
| Projéteis e áreas | Fichas futuras devem declarar colisão, alvo, aliados/pets e terreno; destruição de bases não é consequência automaticamente aprovada de Cero |
| Movimento | Respeitar colisão, destino válido, chunks e dimensões; não inferir atravessar paredes ou teleporte entre mundos |
| Recursos | Emissão utiliza a base espiritual de M05; Sonído tem Stamina como recurso principal já decidido. Duplo custo exige justificativa da técnica |
| Aprendizado | Treino intencional válido deve gerar progresso; spam vazio, automação e alvos irrelevantes seguem as restrições universais |
| Inventário e mundo aberto | Combate deve conviver com ferramentas, construção e hotbar vanilla preservada. Contratos de armas/vínculo/drop ficam para os blocos responsáveis |
| Mundo História | Hueco Mundo está declarado no preset História; não presumir sua presença em qualquer save comum |
| Ciclo de vida | Morte, logout e mudança de dimensão precisam distinguir conquista persistente e execução transitória; a política detalhada depende de M07/M06-11 |

A [documentação Forge sobre lados](https://docs.minecraftforge.net/en/1.20.x/concepts/sides/) e [rede](https://docs.minecraftforge.net/en/1.20.x/networking/simpleimpl/) fundamenta autoridade do servidor e validação de pedidos/destinos, inclusive no singleplayer. A apresentação visual não autoriza aquisição ou execução.

No checkout atual, [ConfirmCharacterC2S.java](../../src/main/java/com/bleachmod/common/network/c2s/ConfirmCharacterC2S.java) aceita somente Shinigami. A presença de entidades Hollow não comprova uma origem Hollow jogável. Estes campos decididos ainda não possuem implementação demonstrada.

### 8. Integrações e limites desta rodada

| Responsável | Entrada/saída ou trabalho posterior |
|---|---|
| M04 | Fornece natureza, origem, evolução e limite de um poder principal |
| M05 | Fornece atributos, recursos e skills universais de Reiatsu |
| M06-08, próximas rodadas | Define capacidades gerais/específicas, aquisição, execução, passivas e formação marcial Arrancar |
| M07 | Decide requisitos de evolução, Arrancarização, Resurrección, acesso por forma e consequências das transições |
| M08/M12/M13 | Concretizam guias, rivais, treinamentos e campanhas; não calculam combate |
| M10/M11 | Viagem, espaço disponível, equipamentos e economia |
| M14/M15/M16 | Apresentação da progressão, multiplayer, calibração, migrações e validação |

**Amostra conceitual mínima**, sem prometer uma entrega: acompanhar um fundamento físico inicial, uma capacidade de emissão e uma de movimento em estados compatíveis. Usar Cero/Sonído como casos de análise não os concede ao personagem inicial.

**Horizonte posterior:** ampliar o repertório e os poderes individuais por conteúdo pesquisado, sem catálogo ou quantidade obrigatória nesta rodada.

### 9. Critérios de revisão estrutural

Critérios utilizados para revisar a primeira rodada:

1. Distinguir evolução racial, preparo geral, aquisição, Mastery e poder principal.
2. Explicar por que o treino de um campo prepara técnicas daquele campo, sem ensinar automaticamente outros poderes.
3. Representar capacidades compatíveis de Hollow e Arrancar sem conceder todo o repertório nos estágios iniciais.
4. Tratar as exceções de regeneração e liberações sem transformá-las em regras universais.
5. Preservar os contratos de Minecraft e localizar as decisões ainda delegadas.

Esses critérios são documentais; não representam teste de gameplay nem aceite de uma implementação.

**Resultado de 20/10/2026:** a organização e as responsabilidades estão definidas em M06-08A/B. A elegibilidade concreta de cada capacidade, as passivas e as transições de corpo continuam abertas nos blocos/módulos identificados. A primeira rodada fecha somente a organização e o modelo de desenvolvimento; não fecha M06-08 inteiro.

### 10. Fechamento da primeira rodada — histórico

**Primeira rodada concluída:** M06-08A/B aprovados — **três campos funcionais com Proficiência própria e Mastery individual**.

**Encaminhamento daquela rodada:** combate físico e anatomia. Esse assunto foi desenvolvido nas seções 11–16 e teve a arquitetura aprovada em M06-08C. Catálogo, quantidade de corpos e medidas continuam abertos.

A sequência sugerida depois é emissão, mobilidade, capacidades defensivas/sensoriais e integração Arrancar. A ordem não antecipa aprovação das regras dessas capacidades.

### 11. Segunda rodada — Combate físico e anatomia

**Data:** 20/10/2026. **Estado:** arquitetura corporal `DECIDIDA` em M06-08C; especificação dos perfis e repertórios ainda aberta. Esta rodada não altera M06-08A/B.

#### Objetivo e referência

Definir como o corpo se expressa no combate fundamental e no repertório físico sem transformar cada membro em uma habilidade comprável ou cada aparência em uma classe permanente.

O [glossário oficial do estúdio Pierrot](https://pierrot.jp/title/bleach/keyword_ha.html), entrada `虚 / ホロウ`, descreve diversidade de formas e capacidades entre Hollows. Consulta em 20/10/2026; texto japonês interpretado. O próprio arquivo identifica suas informações como referentes à época de exibição.

Essa fonte sustenta a diversidade corporal; não fornece um editor procedural, catálogo de corpos jogáveis ou regras de alcance em blocos. A organização abaixo é uma **adaptação de design aprovada pela equipe**. Anatomias e técnicas de personagens concretos ainda exigirão sua referência individual.

#### Contratos herdados aplicados ao corpo

| Elemento | Aplicação das decisões existentes |
|---|---|
| Ataque, bloqueio e Dodge | Continuam ações fundamentais com controles próprios; não ocupam os nove slots de técnicas — M06-01D/M06-06 |
| Sequência básica e contexto | Compartilham a linguagem de sequência, corrida, ar e resposta após esquiva — M06-02; não exigem uma tecla nova por membro |
| Movimento natural versus técnica | Um golpe de moveset não precisa de aquisição/Mastery próprias; uma capacidade distinta pode justificar uma ficha individual — granularidade de M06-07 |
| Desenvolvimento físico | Prática válida contribui para a Proficiência de combate físico; técnica adquirida desenvolve sua Mastery — M06-08B |
| Stamina | Golpes triviais não recebem um imposto alto por serem corporais; esforço relevante segue M06-02I/M06-05 |
| Defesa | Corpo sem espada continua com bloqueio universal; eficiência depende do meio e do ataque. Isso não concede Hierro, parry ou invulnerabilidade — M06-06 |
| Poder individual | Anatomia ou golpe genérico não concede poder exclusivo de outro personagem — RAC-23/M06-02AD |

Essas aplicações não tornam todo ataque corporal uma técnica de Hakuda Shinigami. O campo físico Hollow/Arrancar continua possuindo a Proficiência aprovada; o compartilhamento de repertórios ensináveis entre raças será tratado na integração apropriada.

### 12. M06-08C — DECIDIDO — Perfis corporais com diferenças funcionais controladas

**Decisão aprovada em 20/10/2026:** alternativa A. Resposta explícita da equipe: “Perfis corporais com diferenças funcionais controladas (recomendado)”. Aprovação da arquitetura, mantendo catálogo, medidas e obtenção/transições para especificação posterior. As alternativas permanecem como histórico da análise.

| Alternativa | Experiência oferecida | Custo ou limite |
|---|---|---|
| **A. Perfis corporais definidos pelo mod, com movesets compatíveis — recomendada** | Corpos diferentes podem expressar golpes, cobertura e alcance diferentes, mantendo a mesma linguagem de controle | Cada perfil exige modelo, animações, colisão, câmera e revisão de combate; demanda um catálogo delimitado por entrega |
| B. Um mesmo corpo funcional, com diferenças somente cosméticas | Combate e espaços previsíveis; menor custo de produção | Reduz a diferença de gameplay entre anatomias e exige manter as aparências compatíveis com a mesma geometria funcional |
| C. Montagem livre de membros e proporções com efeitos mecânicos | Grande liberdade para compor o corpo | Multiplica combinações de animação, alcance e colisão; exige um sistema próprio de criação corporal e balanceamento |

**Recomendação: A.** Permite que o corpo participe da identidade Hollow com diferenças que possam ser compreendidas e revisadas. Cada entrega pode começar com uma amostra pequena; a aprovação da arquitetura não promete um número de modelos.

O perfil corporal representa o **corpo funcional atual**. Não é uma nova raça, evolução, barra de progresso ou classe definitiva. Não concede seleção livre de qualquer corpo em qualquer estágio.

**Contrato estrutural de A:**

1. O estado legítimo do personagem determina quais perfis são compatíveis; aquisição, escolha inicial e transições concretas serão definidas com M04/M07/M14. Esta rodada não escolhe entre seleção, concessão narrativa ou outro método de obtenção.
2. Cada perfil declara seu moveset fundamental, as características físicas relevantes e a compatibilidade com técnicas. A aparência cosmética utiliza variações coerentes com esse perfil.
3. Os controles fundamentais mantêm o significado. A animação e a maneira de executar o golpe acompanham o corpo disponível.
4. Características relevantes de alcance/cobertura/colisão pertencem ao perfil ou à técnica autorizada. Alterar textura, cor ou adorno cosmético não concede essas vantagens.
5. O tamanho do corpo não concede automaticamente Potência, Vida, Resistência ou vantagem absoluta. Atributos continuam responsáveis pela fundação; cada diferença funcional precisa de justificativa e calibração.
6. Uma técnica depende de aquisição e de anatomia/estado compatíveis. Um perfil com garras não concede toda técnica canônica que utiliza garras.
7. O perfil não redefine automaticamente equipamentos, inventário, fome, crafting ou mineração; mudanças nessas interações precisam de contrato nos módulos responsáveis.

#### Exemplo conceitual, sem aprovar modelos

| Situação hipotética | Resposta possível no modelo A |
|---|---|
| Corpo com braços adequados ao golpe | O comando de ataque executa uma sequência corporal compatível |
| Corpo cujo ataque natural usa a boca | O mesmo comando pode executar uma sequência de mordidas com animação e cobertura próprias |
| Cor de máscara ou marca corporal muda | O perfil mecânico e o repertório permanecem os mesmos |
| Técnica exige membro que o corpo atual não possui | A técnica não é executável nesse estado; trocar a aparência não contorna o requisito |

Mordida/patada/garra, quando usadas nesses exemplos, são descrições de ações possíveis, não nomes de técnicas canônicas novas. A escolha efetiva de modelos e repertórios permanece aberta.

### 13. Moveset e técnicas físicas — aplicação da granularidade

A distinção já aprovada entre fundamento e técnica será preservada na elaboração das fichas:

```text
corpo legitimamente disponível
        ↓
moveset fundamental compatível
        ↓ prática válida
Proficiência de combate físico

técnica física adquirida + corpo/estado compatível
        ↓ uso válido
Mastery da técnica + Proficiência pertinente
```

Não criar, apenas para preencher menus, aquisições e Masteries separadas para cada golpe de uma sequência natural. Uma técnica contextual ou combo especial pode ter identidade e contrato próprios conforme M06-02/M06-07.

O corpo permite executar o fundamento; não substitui aprendizado, preparo e domínio de uma técnica avançada. O repertório deve deixar clara essa diferença.

### 14. Critérios de Minecraft para desenvolver o modelo A

Os itens abaixo orientam a especificação e a validação posteriores a partir da arquitetura aprovada; não demonstram comportamento implementado nem fixam medidas finais.

| Interação | Critério para especificação |
|---|---|
| Corpo visível e volume atingível | Representação e colisão precisam ser legíveis para usuário e observadores; não exigir colisão individual de cada dente, chifre ou adorno |
| Alcance ofensivo | Definir origem, direção, distância e cobertura do golpe; contar membros ou contatos de animação não multiplica dano automaticamente |
| Parede e corredor | Um golpe ou membro aparente não autoriza acertar através de obstáculo sólido; cobertura e linha de contato dependem da ficha do ataque |
| Porta, teto, água e passagem estreita | Verificar passagem, câmera, posição de interação e vulnerabilidade em cada perfil; não normalizar todos os tamanhos nem adotar escala literal do anime por omissão |
| Terreno e construção | Ataque natural não aprova destruição de blocos, mineração especial ou aumento de alcance de interação com baús e bancadas |
| Ferramentas e ações do mundo | Distinguir ataque corporal de quebrar/colocar blocos e usar itens; ausência de arma inicial não pode ser confundida com uma regra de remoção da hotbar vanilla |
| Defesa e impacto | Preservar Postura/Guard Break e controle seletivo. Aparência robusta não gera imunidade automática a dano ou controle |
| Mais de um alvo | Atingir múltiplos alvos exige cobertura e regra próprias; não acontece apenas porque o modelo possui mais membros |
| Rede | Servidor valida perfil atual, requisitos e ação. Cliente apresenta o estado autorizado; aparência enviada não concede corpo funcional |

O princípio de validação vem de [Forge 1.20.x — SimpleImpl](https://docs.minecraftforge.net/en/1.20.x/networking/simpleimpl/). Não foi selecionada uma API para substituir a geometria do jogador; essa tradução técnica depende do perfil e do artefato 1.20.1 utilizados.

**Questão deliberadamente aberta:** como um Gillian e outros corpos de grande porte funcionam em espaços pequenos, trocas de dimensão e bases construídas. Aprovar perfis funcionais não aprova uma forma compacta livre, travessia de paredes ou redução automática ao passar por portas. M07/M10/M14 precisam resolver transições, locais e feedback com o contrato de combate.

### 15. Estado, implementação e dependências

| Camada | Responsabilidade na arquitetura |
|---|---|
| Identidade e estado evolutivo | M04/M07 fornecem a compatibilidade do corpo; M06 não promove evolução |
| Perfil funcional e moveset | M06 define ações, propriedades e requisitos; servidor aplica o estado autorizado |
| Aparência | Apresenta variações coerentes, sem conceder vantagens por alteração cosmética |
| Proficiência/Mastery | Continuam progressões separadas do perfil; não criar XP de cada anatomia ou membro |
| Mudança de corpo | Persistência de conhecimento, reaprendizado eventual e compatibilidade após evolução continuam para M07/M06-11; nenhuma perda/reset foi decidido aqui |
| Inventário/equipamento | M11 define restrições e vínculos quando necessários; não fechar proibição de ferramentas neste bloco |
| Conteúdo/aceite | Modelos, animações, apresentação e balanceamento precisam de evidência própria em M14–M16 |

No código lido em 20/10/2026, [AppearanceData.java](../../src/main/java/com/bleachmod/common/appearance/AppearanceData.java) guarda peças, cores, skin vanilla e variante slim; [AppearanceService.java](../../src/main/java/com/bleachmod/common/appearance/AppearanceService.java) valida esses dados. Isso não equivale a perfis corporais Hollow funcionais. [ConfirmCharacterC2S.java](../../src/main/java/com/bleachmod/common/network/c2s/ConfirmCharacterC2S.java) continua restringindo a criação a Shinigami.

O alcance de arma já aparece em [asauchi.json](../../src/main/resources/data/bleachmod/weapon_attributes/asauchi.json), e [SenbonzakuraTechniqueService.java](../../src/main/java/com/bleachmod/common/technique/SenbonzakuraTechniqueService.java) possui modificador de alcance de entidade condicionado à forma. São pontos de integração a revisar; não comprovam que animação corporal, alcance natural e colisão de novos corpos estejam resolvidos, nem autorizam copiar os valores da espada.

### 16. Revisão e continuidade da segunda rodada

**Amostra futura para avaliar a arquitetura:** contrastar dois corpos funcionais compatíveis com seus estados e ao menos uma variação cosmética do mesmo perfil. Dois casos de análise não são promessa de dois modelos na primeira entrega.

Critérios para a especificação posterior:

- o jogador reconhece ataque, defesa e esquiva ao mudar de corpo;
- diferenças cosméticas não alteram alcance, dano ou colisão;
- diferenças funcionais possuem efeitos compreensíveis e representação coerente;
- fundamentos não dependem de adquirir cada golpe como uma skill;
- uma anatomia não concede poderes exclusivos nem dispensa requisitos de técnica;
- combate, câmera, interação com blocos e estado observado no multiplayer têm contratos verificáveis;
- os casos de grande porte e transição permanecem visíveis como dependências, sem solução presumida.

**Resultado da segunda rodada:** M06-08C `DECIDIDO` — **perfis funcionais controlados**, com movimentos compatíveis e controles fundamentais comuns. Aparência cosmética não altera as propriedades funcionais do perfil. Não foi aprovado um editor livre de membros/proporções com vantagens mecânicas.

**Próximo assunto:** detalhar o desenvolvimento do repertório físico — aprendizado natural, Proficiência, fundamentos que evoluem e técnicas adquiridas — antes de avançar para emissão espiritual. Catálogo de corpos, seleção inicial, escalas, armas Arrancar e regras de transição ainda terão seu tratamento específico. A decisão corporal não fecha todo o campo físico nem M06-08 inteiro.

### 17. Terceira rodada — Desenvolvimento do repertório físico

**Data:** 20/10/2026. **Estado:** modelo de aquisição física `DECIDIDO` em M06-08D; M06-08A/B/C preservados. Fichas e calibração continuam abertas.

**Objetivo:** definir como o personagem passa de um corpo capaz de lutar para um repertório físico mais elaborado, sem exigir uma aquisição separada para cada golpe nem conceder todo o conhecimento ao alcançar uma Proficiência.

Esta é uma aplicação de game design dos contratos de H1/H4, M06-03, M06-04, M06-07 e M06-08A/B/C. A diversidade Hollow de BL-11 não demonstra uma árvore de aprendizado numérica na obra; esta rodada não introduz novas técnicas nomeadas, poderes originais ou requisitos atribuídos ao cânone.

#### O que já está decidido e permanece válido

| Camada | Contrato herdado |
|---|---|
| Corpo atual | Oferece moveset fundamental compatível; o jogador não precisa comprar separadamente cada mordida, soco ou golpe natural — H1/M06-08C |
| Atributos | Determinam a fundação da capacidade física; sua prática segue M05, sem se confundir com aprendizado de técnicas |
| Proficiência física | Cresce com prática válida do campo; não se compra nem exige um segundo rank — M06-08B |
| Técnica | Possui aquisição e requisitos próprios quando sua identidade justifica; conhecer não significa poder executar em qualquer corpo/estado — M06-03/M06-08C |
| Mastery | Desenvolve a execução individual após aquisição; não é moeda nem outra progressão de Level — M06-04/M06-08B |
| Orientação | Sobrevivência, guia, ambiente, rivais e personagens podem ensinar; não existe Academia Hollow equivalente — H4 |
| Repertório | É apresentado como lista/conhecimentos disponíveis, sem skill tree de compra — M06-07L |

O ponto decidido nesta rodada é o **modo de incorporação das técnicas físicas gerais**. Não houve nova votação sobre a existência de fundamentos, Proficiência, Mastery ou técnicas especiais.

### 18. M06-08D — DECIDIDO — Incorporação conforme a complexidade da capacidade

**Decisão aprovada em 20/10/2026:** alternativa A. Resposta explícita da equipe: “Modelo misto, conforme a complexidade da capacidade (recomendado)”. As alternativas abaixo permanecem como histórico da análise; não aprovam automaticamente fichas, números ou poderes individuais.

| Alternativa | Como o repertório geral avança | Consequência |
|---|---|---|
| **A. Modelo misto — recomendado** | Refinamentos simples entram naturalmente com o desenvolvimento; técnicas que introduzem uma execução distinta ficam disponíveis para prática apropriada; conhecimentos especializados mantêm suas condições próprias | Diferencia familiaridade corporal, aprendizado e domínio; exige classificar cada capacidade com clareza |
| B. Incorporação automática de todas as técnicas gerais elegíveis | Compatibilidade e marcos de Proficiência bastam para adquirir as técnicas classificadas como gerais; só as especializadas exigem aprendizado adicional | Fluxo mais direto; o personagem pode adquirir execuções que o jogador ainda não praticou |
| C. Prática explícita para cada ampliação do repertório | Além da compatibilidade e preparo, cada refinamento ou técnica geral exige uma demonstração própria | Maior participação em cada aquisição; pode tornar pequenas melhorias frequentes demais e burocráticas |

As três alternativas preservam o corpo funcional inicial, a Mastery individual e os requisitos de poderes/transformações. Nenhuma exige um NPC para cada capacidade.

**Recomendação: A.** Usar a menor exigência de aprendizado que tenha função real. Não transformar cada refinamento do moveset em uma quest; reservar prática específica para quando ela ensinar algo novo ao jogador.

#### Contrato estrutural aprovado de A

| Tipo de ampliação | Incorporação recomendada | Progressão posterior |
|---|---|---|
| Refinamento de fundamento | Pode entrar automaticamente ao cumprir o preparo pertinente; precisa ser comunicado | Integra o moveset e a Proficiência física, sem Mastery individual apenas para esse refinamento |
| Técnica física geral com execução distinta | Preparo/compatibilidade tornam a técnica desenvolvível; uma aplicação prática apropriada conclui a aquisição | Mastery individual; uso válido também pode desenvolver a Proficiência física |
| Repertório ou técnica especializada | Conhecimento/fonte/contexto apropriado, acrescido de preparo e prática quando fizerem sentido | Técnicas com Mastery própria; não criar outra Proficiência do repertório |
| Capacidade do poder principal | Continua sob a identidade canônica conquistada, seus requisitos e forma compatível | Domínio próprio; não é concedida pela Proficiência física isoladamente |

Cada ficha identificará se é fundamento, técnica geral, especializada ou do poder principal. Nem toda capacidade precisa de todos os passos; casos simples não recebem uma cerimônia de aprendizado por obrigação.

**O treino pode acontecer no gameplay normal.** Uma prática apropriada não significa necessariamente abrir uma quest, voltar ao mentor, entrar em arena exclusiva ou concluir um minigame. O conteúdo deve declarar a situação observável que comprova o aprendizado, respeitando os pré-requisitos já alcançados.

**Evitar requisito circular:** não exigir uso definitivo de uma técnica ainda indisponível para aprendê-la. O exercício pode utilizar ações fundamentais já conhecidas; se precisar de uma tentativa assistida da técnica nova, o conteúdo deve declarar esse contexto de treino e seus limites. Isso não autoriza usar livremente a capacidade fora dele.

**Preparo não é pagamento:** atingir a Proficiência ou Mastery requerida não consome esse progresso. Concluir treinamento não compra Mastery nem entrega seu domínio máximo.

### 19. Evolução dos fundamentos e prática válida

Aplicação de M06-07Q/R ao campo físico Hollow conforme M06-08D aprovado:

```text
corpo compatível e fundamentos disponíveis
               ↓ prática válida
Proficiência física e preparo geral
               ↓
refinamento simples do moveset OU técnica desenvolvível
               ↓ quando a capacidade justificar
aplicação prática e aquisição
               ↓ uso válido da técnica adquirida
Mastery individual + Proficiência física
```

Os refinamentos podem ampliar sequências, finalizadores, transições ou possibilidades contextuais compatíveis. Isso não aprova um catálogo dessas ações nem um multiplicador global automático de dano por Proficiência. Potência e demais atributos continuam com suas funções de M05.

Um novo fundamento deve preservar o significado dos controles. Se uma variação possuir escolhas ou restrições que tornem a substituição automática inadequada, sua ficha precisa prever a forma de utilização; o desenho de seleção/configuração pertence a M14. Não criar um botão universal para cada melhoria.

#### Exemplos de avaliação, sem requisitos numéricos finais

| Situação | Consequência coerente com os contratos |
|---|---|
| Combate corporal relevante com fundamentos | Pode desenvolver Proficiência física; não cria Mastery separada para cada golpe |
| Uso legítimo de técnica física adquirida | Pode desenvolver simultaneamente sua Mastery e a Proficiência física |
| Treino apropriado de contexto após esquiva | Pode servir à aquisição ou ao domínio conforme o estado da capacidade; não exige uma nova tecla por padrão |
| Inimigo derrotado por outro membro da party | Último golpe não deve ser o único indicador de participação; crédito concreto depende das regras de M15 |
| Ataques vazios, automação ou repetição sem relevância | Seguem as restrições de M06-04G/M06-07J; não constituem prática eficiente |
| Receber dano sem ação física pertinente | Não concede Proficiência física só por apanhar; prática de atributos continua sendo outro contrato de M05 |
| Mineração/construção | Não se torna treino de combate apenas por envolver esforço corporal |

Treinar deliberadamente continua válido. Um exercício legítimo com repetição não deve ser descartado apenas por ser repetível; o sistema precisa distinguir a prática prevista do loop artificial. Critérios e taxas serão calibrados em M15/M16, sem listas novas de moedas ou medidores para o jogador.

### 20. Execução, dados e fronteiras técnicas

Esta rodada decide aquisição, não valores ou novos contratos de dano. Uma técnica aprendida ainda consulta corpo/estado, alvo, recursos e restrições de execução de M06-05/M06-06. Aumentar o repertório não remove Recovery, custos ou condições por omissão.

| Informação | Responsabilidade |
|---|---|
| Classificação, requisitos e método de aquisição | Definição de conteúdo da capacidade, conforme M06 |
| Preparo, aquisição e Mastery pessoais | Estado de progressão do personagem, validado pelo servidor |
| Tentativa/exercício em andamento | Estado transitório do contexto; não se confunde com técnica já adquirida |
| Quest ou personagem instrutor | Observa ou solicita a operação; não altera diretamente as regras do combate |
| Comunicação | M14 deve distinguir refinamento incorporado, técnica desenvolvível, adquirida e temporariamente incompatível |
| Corpo futuro e migração | M07/M06-11/M16 tratam compatibilidade, preservação e tradução dos dados; esta rodada não decide reset por evolução |

A [documentação de capabilities do Forge 1.20.1](https://docs.minecraftforge.net/en/1.20.1/datastorage/capabilities/), reconsultada em 20/10/2026, exige tratamento explícito de sincronização e cópia dos dados na morte. A existência de uma capability não resolve essas integrações automaticamente.

No código lido nesta rodada, [MasteryPracticeService.java](../../src/main/java/com/bleachmod/common/technique/MasteryPracticeService.java) já limita prática por contexto, alvo e intervalo, mas restringe o crédito à raça Shinigami e ao grupo de formas Zanpakutō. [TechniqueProgression.java](../../src/main/java/com/bleachmod/common/technique/TechniqueProgression.java) usa identidade, formas, Mastery e slots dos kits atuais. [SkillsData.java](../../src/main/java/com/bleachmod/common/data/SkillsData.java) ainda oferece níveis compráveis com Training Points.

Essas implementações localizam o trabalho de integração; não representam a Proficiência física Hollow nem revisam as decisões M05/M06. Não copiar seus valores, critérios de dano ou compra de níveis como regras finais deste modelo. Nenhum código foi alterado ou executado para validar gameplay.

### 21. Revisão e continuidade da terceira rodada

**Amostra conceitual para especificação posterior:** um refinamento de fundamento, uma técnica geral com execução nova e uma capacidade especializada com condição própria. São papéis de conteúdo para revisar o sistema; não constituem técnicas inventadas ou catálogo aprovado.

Critérios documentais para revisar essa arquitetura e suas futuras fichas:

1. O Hollow inicial consegue combater usando seu corpo sem comprar cada movimento.
2. O preparo geral e o aprendizado de uma execução nova continuam distinguíveis.
3. A prática pode ocorrer durante a jornada, sem exigir uma quest por melhoria.
4. Não há requisito circular de usar uma técnica inacessível para adquiri-la.
5. Uma aquisição não concede Mastery máxima, transformação ou poder canônico exclusivo.
6. O treino deliberado permanece válido, com critérios de relevância e proteção contra loops artificiais.

**Resultado da terceira rodada:** M06-08D `DECIDIDO` — **modelo misto**, por complexidade e função da capacidade. Refinamentos simples podem ser incorporados naturalmente; técnicas com execução nova utilizam prática apropriada; capacidades especializadas preservam suas condições. Os contratos herdados foram consolidados sem nova aprovação.

**Próximo assunto:** integração de combate corporal/armado Arrancar, que ainda exige compatibilidade e referências próprias. A base de desenvolvimento físico está decidida, mas catálogo, fichas, calibração e particularidades Arrancar permanecem em especificação. Emissão espiritual será o bloco seguinte; não considerar todo M06-08 aprovado por fechar esta rodada.

### 22. Quarta rodada — Mentoria e combate físico Arrancar

**Data:** 20/10/2026. A equipe solicitou análise crítica do papel dos mestres e iniciativa de direção criativa. Esta rodada mantém M06-08D e concretiza as relações de aprendizado já previstas em RAC-12, RAC-20 e M06-03/M06-07.

**Avaliação de design:** mestres com conhecimentos e práticas próprias acrescentam razões para explorar, voltar a personagens e reconhecer o próprio desenvolvimento. Aprendizado autônomo sozinho não concretiza toda a promessa de relações e treinamento de M01. A mentoria deve oferecer uma experiência e um conhecimento identificáveis; um NPC que apenas aumenta o contador de pontos não cumpre essa função.

M06 define aqui o que o combate precisa receber da mentoria. A identidade final, disponibilidade por era, localização, diálogos e quests dos mestres pertencem a M08/M12/M13. Não se abre um segundo módulo nem se aprova um catálogo de NPCs nesta rodada.

### 23. M06-08E — DIRETRIZ HERDADA — Mestres, repertórios e provas

**Estado:** consolidação de decisões existentes, sem nova aprovação de poderes ou elenco. Existência de mestres, ensino de repertórios e conhecimento especializado já estão previstos em RAC-12/RAC-20, M06-03C/D refinados, M06-07J.1 e M06-08D. A autonomia aprovada não significa aprender tudo sem qualquer fonte externa.

| Papel | Contribuição para o aprendizado | Limite |
|---|---|---|
| Guia ou veterano inicial | Contextualiza a condição, orienta os fundamentos e permite consultar novamente o que foi ensinado | Não monopoliza toda a progressão nem entrega poder principal |
| Mestre de repertório | Ensina fundamentos de uma especialização ou conhecimento que permite desenvolver várias técnicas compatíveis | Não exige retorno para cada melhoria; repertório conhecido não significa técnicas dominadas |
| Rival ou avaliador | Expõe uma dificuldade, exige aplicação e reconhece a prova realizada | Confronto não o torna automaticamente aliado ou professor amistoso |
| Personagem associado ao poder principal | Participa da questline daquele poder, conforme sua personalidade e contexto | Técnica transferível, poder principal e liberação continuam contratos separados |

São funções, não quatro degraus fixos ou quatro NPCs obrigatórios para cada raça. Um personagem pode exercer mais de um papel quando houver coerência.

O conteúdo poderá exigir uma fonte/treinamento para conhecimentos especializados, conforme os requisitos da capacidade. A prática autônoma desenvolve o que o personagem legitimamente conhece; ela não substitui por padrão toda oportunidade de aprendizado do mundo.

#### Recomendações de direção criativa para o conteúdo posterior

As recomendações abaixo orientam a elaboração em M08/M12/M13 e ainda não são fichas aprovadas:

- **Um encontro deve mudar uma possibilidade de jogo.** Ao concluir uma formação, o jogador deve compreender ou poder executar algo identificável, como uma resposta contextual ou um repertório desenvolvível, conforme a capacidade aprovada.
- **Ensinar a dificuldade real.** Treino de distância utiliza distância; treino de resposta após esquiva utiliza essa situação. Uma contagem de mortes genéricas não demonstra, sozinha, esse conhecimento.
- **Retorno com propósito.** O mestre pode oferecer revisão do ensinamento e desafios posteriores. Não presumir pagamento por Mastery, bônus permanente de XP ou viagem obrigatória após cada marco.
- **O rival conserva sua personalidade.** Pressão, confronto e reconhecimento podem produzir aprendizado sem converter todo antagonista em um professor dócil.
- **Evitar perda de acesso por omissão.** Cada conhecimento importante precisa de política de disponibilidade/retomada em sua era. M08/M13 devem declarar a solução quando o personagem estiver morto, preso, ausente ou hostil; não presumir um NPC duplicado atemporal ou uma rota alternativa ainda não desenhada.
- **Técnica e corpo compatíveis.** Um mentor humanoide pode ensinar um conceito, mas uma execução dependente de membros/arma não passa automaticamente a funcionar em qualquer perfil Hollow.

#### Candidatos para estudo, com procedência

Fontes consultadas em **20/10/2026**. As descrições oficiais sustentam identidade/contexto; os papéis de ensino são propostas de adaptação, não fatos de que esses personagens tenham treinado o jogador na obra.

| Candidato | Evidência ou origem | Função candidata e cuidado |
|---|---|---|
| Hollow veterano original | Personagens originais de orientação são permitidos por M01/RAC-12 | Sobrevivência, leitura de ameaça e uso do corpo; não criar poderes exclusivos inéditos como recompensa |
| Neliel | A [ficha oficial japonesa](https://bleach-anime.com/character/) distingue a antiga Espada e sua condição corporal; a [ficha de Nel](https://bleach-anime.com/en/character/) registra cooperação com Ichigo | Estudar orientação cooperativa e formação física. Usar a condição/forma disponível naquela era; competência não prova disponibilidade constante como mestra |
| Grimmjow | A [ficha oficial japonesa](https://bleach-anime.com/character/) destaca busca por força/combate e cooperação circunstancial por contrato com Urahara | Estudar rival avaliador de pressão ofensiva e aplicação real. Não aprovar toda sua técnica como ensinável nem fazê-lo conceder Pantera por um duelo genérico |
| Harribel | A mesma fonte registra governo de Hueco Mundo após Aizen e captura pelos Quincy | Estudar uma relação de formação em contexto de facção, se a era permitir. Não localizá-la livremente como mestra durante a captura |

Neliel, Grimmjow e Harribel são candidatos, não uma sequência obrigatória de três mestres. A seleção final precisa de cenas/capacidades específicas e coerência com M03; a ficha promocional atual não determina como estarão em um arco anterior.

#### Menor exemplo de experiência para avaliar

**Proposta ilustrativa, sem criar uma quest completa ou técnica canônica nomeada:** um veterano demonstra uma abertura após um ataque comprometido; o jogador pratica reposicionar-se e responder no contexto correto; o exercício informa por que uma tentativa falhou e permite nova tentativa; o resultado pode satisfazer a prática de uma capacidade já prevista no repertório.

O critério é executar a situação pertinente, não simplesmente derrotar o instrutor. Aquisição, custo, segurança, falha, duração e repetibilidade precisam da ficha do conteúdo. Quando existir tentativa assistida de técnica ainda não adquirida, respeitar os limites de M06-08D.

### 24. M06-08F — DECIDIDO — Alternância corporal e armada por estado de empunhadura

**Decisão aprovada em 20/10/2026:** alternativa A. Resposta explícita da equipe: “Moveset orientado pela empunhadura (recomendado)”. As alternativas ficam como histórico; controles exatos, fichas e execução permanecem em especificação.

**Herança preservada:** M06-08A/B já reúne combate físico corporal e armado compatível no mesmo campo de Proficiência. Esta rodada não propõe outro rank de espada Arrancar, não iguala Arrancarização a formação Shinigami completa e não concede conhecimento especializado por equipar arma.

O [glossário oficial TYBW](https://bleach-anime.com/keyword/), entrada `破面`, descreve a arma Arrancar como recipiente de capacidades próprias e Resurrección como sua liberação. Isso fundamenta separar uso físico da arma, poder individual e forma; não estabelece os controles do mod.

| Alternativa | Funcionamento fundamental | Efeito |
|---|---|---|
| **A. Estado da arma orienta o moveset — recomendada** | Arma legitimamente empunhada orienta ataques fundamentais armados; arma guardada orienta o corpo; técnicas compatíveis podem combinar os dois | Leitura clara do personagem e continuidade com o princípio de empunhar/embainhar de M06-07, sem exigir classe permanente |
| B. Postura física selecionada independentemente da arma | O jogador escolhe um moveset corporal ou armado mesmo mantendo a arma visivelmente empunhada, respeitando os meios disponíveis | Mais controle direto do estilo básico; exige comunicar e controlar uma postura adicional além de Combat Mode e empunhadura |

**Recomendação: A.** O corpo continua relevante enquanto a arma está disponível, por técnicas e contextos adequados. A espada não deve apagar a formação corporal, e guardá-la não deve apagar o vínculo com o poder individual.

#### Contrato estrutural aprovado de A

| Estado/situação | Regra estrutural candidata |
|---|---|
| Arma empunhada e compatível | Ataque fundamental usa o moveset armado pertinente; não equivale a executar técnicas avançadas de Zanjutsu |
| Arma guardada e corpo compatível | Ataque fundamental usa o moveset corporal; a postura não elimina conhecimento ou poder vinculado |
| Técnica corporal enquanto armado | Pode existir se sua ficha admitir essa execução; não bloquear todo o repertório corporal por estar com a espada na mão |
| Técnica que exige meio específico | Consulta arma/corpo/estado reais; slot equipado ou troca cosmética não contornam requisitos |
| Troca durante preparação, execução ou Recovery | Respeita as permissões da ação; alternar não cancela universalmente técnicas nem reinicia cooldowns |
| Liberação altera corpo/arma | M07 e o poder canônico definem o perfil e as técnicas daquele estado; não presumir espada convencional em toda Resurrección |
| Uso de ferramentas/construção | Continua consultando o contexto de Minecraft e as regras de Combat Mode; não criar um item fictício por golpe corporal |

**Controle a definir em M14:** o gesto de empunhar/guardar deve ser simples, remapeável e comunicável. A decisão estrutural não seleciona tecla, animação ou política de slots/inventário. Vínculo, perda e recuperação da arma continuam para M11; guardá-la não cria por si só imunidade a perda.

#### Ensino aplicado à alternância

Um mestre pode ensinar um conceito de transição e abrir um repertório compatível; um rival pode exigir que o jogador aplique a transição sob pressão. Isso oferece aprendizado próprio sem criar um personagem canônico que ensine tudo.

**Não confundir:** aprender uma técnica de combate armado com um Arrancar não entrega sua Resurrección. Aprender um conceito corporal com um mestre de outra raça não altera natureza nem transfere capacidades exclusivas. A equivalência de cada técnica ensinável depende da ficha e da integração M06-11.

### 25. Integração dos mestres com sistemas existentes

| Responsável | Contrato necessário |
|---|---|
| M06 | Declara conhecimento oferecido, requisitos, exercício pertinente e execução das capacidades |
| M08 | Define o personagem, motivação, repertórios oferecidos, relações e condições do serviço |
| M09 | Define oponente/exercício quando houver combate; separa representação de treino do NPC de diálogo |
| M12/M13 | Orquestram início, prova, falha, retomada e recompensa; disponibilidade por era deve ser explícita |
| M14 | Comunica o que falta aprender e a alternância corpo/arma; não apresenta uma lista de compras como substituto do treino |
| M15 | Define participação pessoal/party e repetição concorrente; aprendizado de outro jogador não é automaticamente compartilhado |
| M16 | Integra e valida a fatia jogável, com evidências separadas de presença do NPC, treinamento e aquisição |

O [plano de NPCs existente](../planejamento/plano-npcs-quests.md) foi lido integralmente nesta rodada e é histórico técnico. No checkout atual, [QuestNpcEntity.java](../../src/main/java/com/bleachmod/entity/QuestNpcEntity.java) fornece interação com painel, permanece imóvel e é invulnerável. [QuestActionC2S.java](../../src/main/java/com/bleachmod/common/network/c2s/QuestActionC2S.java) valida o NPC e a proximidade ao iniciar quests vinculadas. Isso é infraestrutura de interação, não um mestre com demonstração e sparring já implementados.

O default de Ulquiorra em [QuestDefaults.java](../../src/main/java/com/bleachmod/common/quest/QuestDefaults.java) ainda usa uma tarefa de teste para derrotar endermen. A existência desse conteúdo não aprova sua qualidade como treinamento de Bleach. Demonstração, avaliação contextual, IA e feedback dos exercícios exigem integração própria; não alterar o NPC permanente para torná-lo um boss sem tratar sua continuidade.

### 26. Revisão e continuidade da quarta rodada

Critérios para o conteúdo posterior:

1. Cada formação apresenta o conhecimento oferecido e uma aplicação jogável coerente.
2. Existe espaço para descoberta e personagens relevantes sem bloquear toda melhoria autônoma.
3. Repertório conhecido, técnica adquirida, Mastery e poder principal permanecem distintos.
4. Personalidade, forma e disponibilidade do mestre correspondem à era do encontro.
5. O combate corporal continua funcional quando a arma não orienta o moveset; técnicas híbridas obedecem aos seus requisitos.
6. A alternância não concede técnicas, resets de tempo ou cancelamento universal.
7. Conversar com um NPC e completar uma tarefa de teste não são apresentados como implementação final de mentoria.

**Estado:** M06-08E consolida a mentoria já prevista; nomes, exercícios e funções específicas são candidatos de conteúdo para M08/M12/M13. **M06-08F está `DECIDIDO`**: empunhadura orienta o moveset fundamental, com técnicas corporais compatíveis possíveis enquanto armado e respeito às restrições de execução. Existência de mestres e ensino de repertórios foram preservados sem nova aprovação.

**Próximo assunto:** emissão espiritual, com pesquisa de Cero/Bala e suas condições. A base física tem agora organização, progressão, anatomia e alternância estruturais; fichas, catálogo, números, mentores concretos e integrações delegadas continuam abertos. Não declarar NPCs mentores ou combate Hollow/Arrancar implementados por concluir a documentação.

### 27. M06-08G — DECIDIDO — Linhagens, formas e primeira trajetória

**Aprovação em 20/10/2026:** após ler o parecer de reconciliação, o usuário escolheu Grimmjow como primeira linhagem, autorizou usar artes da comunidade como base para formas sem referência oficial e aceitou as demais recomendações. Resposta: “Grimmjow será nossa primeira [...] fora isso suas recomendações me parecem corretas”. Também pediu comunicação mais clara.

#### Direção aprovada

| Assunto | Regra em linguagem direta |
|---|---|
| Linhagem | O personagem segue uma identidade de poder canônico, que pode aparecer em Adjuchas e continuar até a Resurrección |
| Corpo | Cada etapa usa um perfil funcional definido pelo mod; estruturas podem ser reutilizadas quando fizer sentido, preservando a identidade visual |
| Personalização | Cores e adornos compatíveis podem variar; mudanças cosméticas não alteram golpes, alcance ou colisão |
| Referências visuais | Usar a forma oficial quando disponível; para etapas sem referência oficial localizada, pesquisar fan arts e utilizá-las como base para a adaptação do mod |
| Primeira linhagem no detalhamento futuro | Grimmjow / Pantera; as regras gerais do jogo continuam sendo especificadas antes das fichas e artes |
| Acesso ao poder | A relação/questline pode começar antes da Arrancarização; isso não entrega técnicas Arrancar ou Resurrección antes do estado compatível — REV-H6-01 |
| Arrancar em Gillian | A aquisição da identidade principal não depende exclusivamente de virar Adjuchas; a rota precisa de acesso compatível e não retorna à evolução normal encerrada |
| Predação e estabilidade | Preservar a predação para avanço natural e a agência do Gillian. Não adotar regressão passiva por tempo offline ou por pausar a caça para construir/explorar; outras condições de estabilidade continuam para M07 |

Estágio evolutivo, identidade do poder e corpo funcional continuam sendo informações diferentes. A Proficiência dos três campos e a Mastery individual permanecem como aprovadas; evolução não vira uma quarta barra de Proficiência. Uma linhagem não impede aprender técnicas gerais compatíveis.

O desenho de uma etapa pode ser adaptado; poderes jogáveis continuam seguindo M01/RAC-23. Uma fan art não concede automaticamente voo, regeneração, veneno ou novas técnicas exclusivas. A ficha precisa separar aparência, fundamentos e capacidades do poder.

#### Grimmjow: referências conhecidas e etapa que exige cuidado

Grimmjow é o primeiro caso aprovado porque sua identidade tem referências fortes em Adjuchas, Arrancar e Pantera. A [sinopse oficial do episódio 167](https://www.tv-tokyo.co.jp/anime/bleach_bestbout/episodes/) situa seu passado em Adjuchas; a [ficha oficial de Hueco Mundo](https://www.tv-tokyo.co.jp/contents/bleach_test2/chara/index05.html) identifica Pantera. Reconsulta em 20/10/2026; as fontes são resumos do anime, não leitura integral do mangá ou do episódio.

**Precisão necessária:** nas fontes conferidas, não foi confirmada uma forma Vasto Lorde específica de Grimmjow. Caso o jogador dessa linhagem siga até Vasto Lorde, a aparência dessa etapa precisará de referência adicional ou adaptação identificada do mod. Não registrar que já temos todas as formas canônicas de Grimmjow. O modelo final ainda não foi escolhido.

#### Como utilizar as artes da comunidade

Para cada referência selecionada, registrar autor, link da publicação e qual parte ajuda o desenho: silhueta, máscara, proporções ou continuidade com o poder. Comparar as propostas com as referências oficiais conhecidas. A escolha final da arte e do desenho será apresentada ao usuário; a autorização para pesquisar fan arts não aprova antecipadamente uma imagem específica.

As fontes ficam identificadas como **oficial**, **fan art** ou **adaptação final do mod**. Isso permite aproveitar boas ideias sem confundir sua origem. A escolha de uma referência visual também não resolve medidas, colisão ou animações no Minecraft.

#### Próximo trabalho e limites

**Ordem corrigida pelo usuário em 20/10/2026:** continuar o funcionamento geral do jogo conforme a documentação. O próximo bloco é emissão espiritual — Cero/Bala — seguido de mobilidade, capacidades defensivas/sensoriais e integração. A ficha específica de Grimmjow/Pantera, as formas e as artes ficam para depois. Grimmjow continua escolhido como primeira linhagem quando chegar o detalhamento de conteúdo; isso não antecipa essa etapa nem abre M07 antes do fechamento estrutural de M06.

Ainda pendentes: referência visual Vasto Lorde, capacidades por etapa, compatibilidade de cada linhagem com origens Arrancar, medidas, custos, condições de evolução e quests. O papel de Grimmjow deve respeitar sua personalidade; esta aprovação não fecha diálogo, serviço de mestre ou questline completa.

Arquitetura e primeira linhagem aprovadas; nenhuma implementação ou execução de gameplay nesta rodada.

### 28. Rodada conjunta — Emissão, mobilidade e capacidades corporais/sensoriais

**Data:** 20/10/2026. **Pedido:** desenvolver os três blocos gerais e fazer a revisão conjunta depois. **Estado atual:** M06-08H/I/J `DECIDIDOS` estruturalmente por respostas explícitas às três perguntas. A preparação anterior era proposta; a aprovação foi registrada depois, na seção 32. Detalhes de execução ainda abertos estão identificados, incluindo K.

O objetivo é definir o que o jogador aprende, quando pode usar, como executa e como melhora essas capacidades. Fichas de Grimmjow, outras linhagens, aparências e artes permanecem para depois. Custos, alcances, tempos e fórmulas não foram preenchidos arbitrariamente.

#### Referências conferidas nesta rodada

Consultas em **20/10/2026**, texto japonês interpretado; resumos oficiais do anime e fichas licenciadas, não leitura integral de capítulos/episódios. Fontes oficiais sustentam as identidades; as regras de execução propostas nas seções seguintes são adaptações do mod.

| Fonte | Parte conferida | Limite |
|---|---|---|
| [Pierrot — Glossário sa](https://pierrot.jp/title/bleach/keyword_sa.html) | `虚閃 / セロ`: feixe emitido pelo Menos | Não fornece curva de carga ou requisito numérico |
| [Pierrot — Retrospectiva do arco inicial](https://pierrot.jp/title/bleach/playback_01.html) | Trecho do Menos que enfrenta Ichigo/Uryū e dispara Cero | Sustenta uso anterior à Arrancarização; não prova disponibilidade inicial para qualquer Hollow |
| [Pierrot — Glossário ha](https://pierrot.jp/title/bleach/keyword_ha.html) | `虚弾 / バラ`: emissão Arrancar mais rápida e menos potente que Cero; `探査神経 / ペスキス`: leitura de pressão/características espirituais | Não determina cadência, interface ou alcance do mod |
| [TYBW — Glossário](https://bleach-anime.com/keyword/) | `破面`: Hierro, Sonído, Cero e perda geral da regeneração rápida Hollow | Não declara toda exceção nem aprendizagem automática |
| [Pierrot — Espada](https://pierrot.jp/title/bleach/chara_06.html) | Ulquiorra/Murciélago inclui regeneração rápida | Exemplo de exceção; não concede o poder a toda raça |
| [Brave Souls — Perfil de Ulquiorra](https://www.bleach-bravesouls.com/character/ulquiorra.html) | Descrição biográfica de regeneração, com limite em cérebro/órgãos | Fonte licenciada complementar; não importar regras de combate desse jogo |

Os arquivos Pierrot identificam seu conteúdo como relativo à época de exibição. Não generalizar a frase licenciada sobre exclusividade de Ulquiorra para todo material de Bleach sem pesquisa adicional. A página de Rebirth of Souls foi reconsultada: sua regeneração de medidor é mecânica daquele jogo, não regra deste mod.

**Herança obrigatória:** três campos de Proficiência; Mastery individual; percepção universal de M05; Stamina para Sonído; Energia Espiritual para emissão; Postura só para Guard Break; dano comum não interrompe toda técnica; nenhuma ação cancela universalmente outra; conhecimento, corpo e estado compatíveis continuam necessários.

### 29. M06-08H — DECIDIDO — Cero e Bala

**Aprovado em 20/10/2026.** Resposta explícita: “Adotar Cero carregável e Bala rápida (recomendado)”. Aprovação do funcionamento estrutural apresentado; curvas, valores e permissões específicas de ações simultâneas ainda exigem especificação. A alternativa de Cero com preparação fixa não foi escolhida.

#### Experiência recomendada

**Cero exige escolher uma abertura. Bala permite pressionar com mais rapidez.** As duas técnicas mantêm utilidade; Bala não é um Cero defeituoso e Cero não apaga Bala ao ser aprendido.

| Capacidade | Função proposta no combate | Execução base recomendada |
|---|---|---|
| Cero | Disparo potente e legível, com compromisso de preparação | Segurar para concentrar, soltar após preparo mínimo para disparar; carga limitada, com disparo ao atingir o máximo |
| Bala | Projétil rápido para pressão e aproveitamento de aberturas curtas | Ativação breve produz um disparo; novas ativações respeitam cadência, recurso e Recovery |

As comparações valem em condições equivalentes; não significam que qualquer Bala de um personagem forte seja inferior a qualquer Cero de outro personagem. Rajadas, feixes varridos, emissões múltiplas e variantes excepcionais precisam de contratos próprios.

**Alternativa viável:** Cero com preparação fixa e disparo automático, sem carga regulável. É mais simples de executar e calibrar, mas oferece menos escolha sobre o momento de liberar. Recomendo carga limitada pela decisão entre disparar antes ou assumir mais preparação; não usar crescimento ilimitado por tempo segurado.

#### Acesso e aprendizado aprovados estruturalmente

- Cero base torna-se desenvolvível a partir de Gillian, mediante preparo e aprendizado apropriados. Esse recorte é uma adaptação de acesso aprovada para o mod; a fonte sustenta Menos usando Cero, não uma lei de exclusividade para todos os casos da obra.
- Bala fica no repertório geral Arrancar desenvolvível, sem exigir obter um poder principal específico. Não é atribuída ao Hollow inicial.
- O aprendizado pode surgir do desenvolvimento compatível ou de orientação/treino. Aprender a concentrar, mirar e liberar é uma experiência apropriada; não exigir um NPC para cada melhoria.
- Treinamento pode usar fundamentos conhecidos ou uma tentativa assistida delimitada, conforme M06-08D. Não exigir usar livremente uma capacidade ainda não adquirida para desbloqueá-la.
- Cero e Bala possuem Masteries próprias, alimentando também a Proficiência de emissão quando a prática for válida. Aprender uma não entrega domínio da outra.
- Variantes avançadas de Cero não são prêmio automático por encher sua Mastery. Fonte, forma, conhecimento e restrições específicos continuam necessários; o catálogo fica para o conteúdo posterior.

M07 define como se alcança Gillian/Arrancar; M06 define a compatibilidade da capacidade. Nenhuma quest ou quantidade de presas foi definida aqui.

#### Contrato de execução recomendado

| Etapa | Cero base | Bala base |
|---|---|---|
| Início | Verificar aquisição, corpo, estado, recursos e permissões da ação atual | Mesmas verificações, sem preparação longa |
| Mira | Jogador ajusta durante a preparação; origem da emissão acompanha corpo compatível | Direção escolhida no disparo |
| Liberação | Após preparo mínimo, soltar dispara; máximo de carga dispara automaticamente | Uma ativação aceita produz um projétil |
| Custo | Energia Espiritual no disparo, proporcional à carga válida; verificar disponibilidade também ao liberar | Energia Espiritual por disparo aceito |
| Energia insuficiente na liberação | Encerrar com feedback, sem disparo parcial clandestino ou energia negativa | Recusar execução, sem projétil |
| Cancelamento | Antes do disparo, conforme a ação; sem cobrar o disparo, mas com curta recuperação para evitar repetição gratuita de ameaças | Depois de emitido, não recolher projétil nem devolver custo |
| Dano/controle | Dano comum não cancela automaticamente; controle relevante pode interromper conforme fases | Mesma base universal |
| Depois do disparo | Recovery e cooldown próprios | Recovery e cooldown/cadência próprios, mais breves na intenção de pressão rápida |

Valores exatos e curva de custo/eficácia continuam pendentes. Se a energia disponível limitar a carga, isso precisa ser informado antes de atingir uma liberação impossível; a interface não pode apresentar uma carga autorizada que o servidor já sabe não poder pagar. Política de interface e reservas técnicas de recurso será especificada na implementação, sem duplicar cobrança.

Para o Cero base descrito, direção fica definida na liberação e o efeito possui duração limitada; não é canalização infinita ou feixe com rastreamento automático. Preparar permite ajuste de mira e movimento limitado; disparar compromete a ação pelo tempo definido na ficha. **M06-08K fecha a permissão durante carga: bloquear, usar Dodge ou Sonído exige cancelar a carga e respeitar a recuperação.** Velocidade de movimento, giro e duração são calibração, não números canônicos.

Bala não interrompe automaticamente todas as ações do alvo a cada acerto. Cero não recebe Super Armor por ser carregado. Cero e Bala não compartilham cooldown só por pertencerem à emissão; Recovery já regula a passagem de uma técnica para outra.

#### Mastery, terreno e alvos

Mastery pode melhorar eficiência, estabilidade e preparação de Cero; precisão, eficiência e execução de Bala; eficácia ofensiva moderada conforme M05. Não remove o preparo mínimo ou converte as duas técnicas na mesma execução instantânea. Atributos continuam predominando no poder bruto.

**Para Minecraft, proposta base:** mira manual, colisão com obstáculos sólidos e ausência de dano a blocos. Paredes podem proteger; a posição da câmera não permite emitir do outro lado delas. Alcance, largura, persistência, múltiplos alvos e categoria de dano precisam de ficha. Cada alvo só recebe os impactos autorizados; cada quadro visual do feixe não vira outro acerto.

Aliados, pets e jogadores seguem a política de alvo/PvP de M15; enquanto ela não estiver fechada, não aprovar dano aliado universal nem exceções implícitas. Os efeitos devem respeitar essa mesma autorização. Destruição de terreno e choque entre feixes são assuntos separados, ainda não aprovados.

**Exemplo de escolha:** usar Bala para manter pressão sobre quem se reposiciona; preparar Cero quando o adversário se compromete com uma ação longa. Isso descreve funções propostas, sem garantir acerto ou stun.

### 30. M06-08I — DECIDIDO — Mobilidade Hollow/Arrancar

**Aprovado em 20/10/2026.** Resposta explícita: “Sonído direcional (recomendado)”. A equipe escolheu deslocamento por comando/direção manual, com trajeto e paredes respeitados, Stamina e uso dentro/fora de combate. A alternativa de destino pela mira não foi escolhida. Distância, velocidade e tratamento detalhado dos obstáculos continuam em especificação.

#### Experiência recomendada

**Sonído permite escolher rapidamente uma nova posição.** O jogador decide a direção; o sistema respeita o espaço disponível. Dodge continua sendo a esquiva curta universal.

| Assunto | Proposta e herança |
|---|---|
| Acesso | Sonído base desenvolvível na formação Arrancar, sem ser concedido ao Hollow recém-formado |
| Aprendizado | Conhecer o método e praticar deslocamento/parada em contexto útil; orientação pode ensinar a base e permitir desenvolvimento autônomo |
| Comando | Input dedicado/remapeável e direção manual; sem selecionar automaticamente o melhor flanco ou surgir atrás do alvo |
| Recurso | Stamina como principal, já decidida em M05; sem adicionar Energia por ser uma técnica rápida |
| Ritmo | Deslocamento curto e rápido por ativação, custo, Recovery e cooldown próprios |
| Progressão | Uso relevante desenvolve Mastery de Sonído e Proficiência de mobilidade |
| Exploração | Pode ser usado fora do combate; não exigir Combat State para funcionar |

**Alternativa viável:** Sonído dirigido à mira ou a um ponto selecionado. Pode facilitar alcançar locais precisos, mas exige seleção de destino e muda o controle de reposicionamento. Recomendo começar pelo deslocamento direcional; eventuais técnicas de chegada a alvo precisam de aquisição própria.

#### Colisão e consequências

Proposta: Sonído percorre um trajeto válido de alta velocidade. Visualmente pode parecer um desaparecimento breve; mecanicamente não atravessa parede, não ignora o corpo atual nem troca de dimensão.

- Validar trajeto e espaço do corpo inteiro; não basta o ponto final estar livre.
- Obstáculo limita o deslocamento ao trecho seguro. Se não houver trecho útil, recusar e comunicar sem cobrar a ação completa. Trecho útil executado usa o custo da ativação; o limiar será calibrado.
- Não gerar/carregar terreno distante para buscar um destino; validar região disponível e limites do mundo.
- Sonído não garante invulnerabilidade, limpeza de efeitos, quebra de mira ou cancelamento de ataque inimigo. O reposicionamento é a defesa principal.
- Não concede voo, sustentação aérea ou imunidade a dano de queda. Uso no ar depende das condições de movimento compatíveis; sua regra não cria sustentação por repetição ilimitada.
- Água, desnível, teto, blocos parciais e perfis grandes exigem verificação específica; não encolher Gillian para fazer o deslocamento caber.
- Durante preparação/execução de outra técnica, consultar suas permissões. Sonído não encerra automaticamente Recovery nem devolve custos/cooldowns.

Mastery pode melhorar eficiência, controle, parada e distância/velocidade moderadas. Não cria automaticamente versões especiais, clones ou técnicas exclusivas. A Proficiência de mobilidade não progride eficientemente por correr em círculos ou repetir deslocamentos sem contexto.

Um mestre pode propor alcançar uma posição e reagir a uma ameaça após parar; a capacidade é desenvolvida pelo jogador, não dominada instantaneamente ao conversar. Nenhum instrutor, local ou quest específica foi escolhido.

### 31. M06-08J — DECIDIDO — Hierro, Pesquisa e regeneração

**Aprovado em 20/10/2026.** Resposta explícita: “Aprovar essa organização (recomendado)”. Hierro passivo com Mastery própria, Pesquisa ligada à Mastery de Percepção Espiritual existente e regeneração rápida somente em corpos/poderes compatíveis. A aprovação da organização não determina todos os valores, fontes individuais ou o modo de ativação de cada regeneração especial.

Estas capacidades não viram três campos novos de Proficiência. Cada uma tem uma função própria e preserva os sistemas universais existentes.

#### Hierro — proteção corporal passiva

**Recomendação:** proteção passiva quando o corpo/estado Arrancar for compatível, sem botão de ativação e sem drenagem contínua de Energia. Sua formação permite desenvolver a capacidade; eficiência não é idêntica para todo Arrancar.

- Reduzir dano pertinente conforme Resistência, capacidade e Mastery; não usar Battle Power como uma chave automática de invulnerabilidade.
- Hierro possui Mastery defensiva conforme o contrato já previsto em M05. A aquisição e o grau inicial dependem da formação; não recebe Proficiência extra.
- Treino defensivo válido pode desenvolver domínio; receber dano parado, dano de aliado/pet ou loop de cura não constitui treino eficiente.
- Não equivale a bloquear: um ataque pode atingir o corpo protegido sem a guarda estar levantada. Postura continua governando apenas a guarda; Hierro não concede imunidade a Guard Break nem restaura Postura.
- Não concede Super Armor ou imunidade a controle por omissão. Armadura, encantamentos, efeitos e tipos de dano precisam de uma composição de defesa em M11/M15; não somar percentuais até anular todo dano.
- Não implica resistência a lava, sufocamento, vazio ou todo efeito ambiental. A ficha de defesa deve declarar as categorias cobertas.

**Alternativa:** ativar Hierro por tempo limitado, com custo e janela de defesa. Oferece uma decisão ativa, mas altera a fantasia de pele resistente e concorre com o bloqueio. Recomendo o passivo como base; defesas ativas especiais só entram se justificadas por capacidade própria.

#### Pesquisa — aplicação racial da percepção espiritual

**Recomendação:** Pesquisa é uma forma Arrancar de aplicar a percepção já decidida em M05. Exige acesso ao conhecimento compatível, mas usa a Mastery de Percepção Espiritual; não criar “Pesquisa Mastery” e “Percepção Mastery” medindo simultaneamente a mesma leitura básica.

| Camada | Funcionamento proposto |
|---|---|
| Percepção básica universal | Sente presenças muito evidentes, com informação vaga; não exige Pesquisa |
| Percepção treinada | Melhora direção, intensidade aproximada, distância estimada e reconhecimento de assinatura conhecida, conforme M05 |
| Pesquisa | Ativação breve de leitura focada, com pequeno custo de Energia e intervalo próprio; usa os fundamentos treinados e o método racial aprendido |

A ativação ajuda a procurar, comparar ou localizar aproximadamente uma presença. Não revela gratuitamente atributos, inventário, Battle Power exato, nome civil ou todo jogador no mundo. Uma leitura espiritual não identifica automaticamente um desconhecido.

Proposta de percepção por obstáculos: pode apontar uma presença aproximada dentro do alcance autorizado, inclusive fora da visão direta; não mostrar silhueta precisa permanente através de blocos. Supressão de Reiatsu, intensidade, familiaridade e domínio de percepção afetam a leitura. Não detectar não comprova que não há alguém oculto.

Pesquisar só o alcance/região disponíveis no servidor, sem gerar chunks ou consultar outra dimensão. Não enviar ao cliente dados ocultos que a apresentação deveria apenas esconder. A leitura não autoriza atacar através da parede ou fixa um alvo automaticamente.

Treino relevante de localização/reconhecimento desenvolve Percepção Espiritual. Apertar o botão repetidamente sem informação ou exercício pertinente não gera ganho eficiente. Técnicas sensoriais realmente distintas podem ter aquisição/Mastery próprias no futuro; não aprová-las por este nome racial.

**Alternativa:** Pesquisa como técnica separada com Mastery própria. Pode fazer sentido se possuir uma execução/função distinta o suficiente; duplicaria a progressão se as duas medidas cobrirem apenas alcance e precisão do mesmo sentido. Recomendo integrar o fundamento primeiro.

#### Regeneração — separar recuperação natural de capacidade especial

**Recomendação:** regeneração natural continua igual ao contrato universal de M05. Regeneração rápida só existe para corpo/estado/poder que realmente a suporte; não entregar como habilidade geral de todo Arrancar ou como recompensa automática de Proficiência elevada.

| Situação | Diretriz proposta |
|---|---|
| Recuperação natural | Lenta e fortemente reduzida/suspensa em combate, conforme M05 |
| Capacidade de regeneração rápida compatível | Pode recuperar Vida em combate, com ritmo, condições e consumo de Energia declarados na ficha |
| Arrancarização | Conferir o que permanece ou se perde; não transferir automaticamente toda regeneração da fase Hollow |
| Dano fatal | Vida zero segue derrota normal; regeneração não ressuscita nem impede universalmente a morte |
| Recursos | Não repõe simultaneamente Vida, Stamina e Energia; não financiar a própria recuperação em loop |
| Representação | Sem exigir um sistema de órgãos, amputação ou múltiplas barras de ferimento para a base do mod |

Ativação manual, resposta automática ao ferimento, duração e restrições dependem da capacidade concreta; não impor um único botão de cura a todo caso. Essa escolha fica para a ficha específica porque a raça não tem uma regeneração rápida universal. Também não presumir regeneração infinita ou recuperação de todo dano porque uma forma possui esse nome.

Quando houver domínio individual treinável, a ficha usa Mastery da própria capacidade, sem criar Proficiência de regeneração. Ganho exige contexto legítimo; dano autoprovocado e repetição de cura sem risco não são treino eficiente. Limites precisos, convivência com curas e efeitos que reduzem cura ficam para a ficha/M15, sem inventar técnicas antirregeneração nesta rodada.

### 32. Registro das respostas e fronteiras da aprovação

**M06-08H/I/J foram aprovados explicitamente em 20/10/2026**, após a apresentação das propostas e perguntas. Os textos de recomendação/alternativas das seções anteriores preservam o raciocínio; não mantêm aberta uma escolha já respondida. O estado e o alcance das aprovações estão no início de cada bloco.

Foram escolhidos: Cero carregável/Bala rápida, Sonído direcional e Hierro passivo/Pesquisa integrada/regeneração compatível. Não reapresentar essas escolhas como votação. Recursos, existência de Mastery e separação de Dodge continuam herdados. O recorte de aprendizado apresentado — Cero a partir de Gillian, Bala/Sonído na formação Arrancar — integra a escolha estrutural; não concede aquisição instantânea ao mudar de estado.

A revisão conjunta solicitada está registrada na seção 33. As lacunas de execução não são consideradas resolvidas apenas pela aprovação da arquitetura. A decisão nova identificada nessa revisão foi isolada em M06-08K e aprovada explicitamente depois, na seção 34.

| Responsável | Trabalho posterior |
|---|---|
| M06-08 / M06-11 | Consolidar escolhas e verificar interação com os contratos de combate e skills universais |
| M07 | Requisitos de evolução, Arrancarização, liberações e manutenção/perda de compatibilidade nas transições |
| M08/M12/M13 | Quem ensina, contexto/era, objetivos, falha e retomada; sem currículo específico nesta rodada |
| M10/M11 | Viagem, terreno, inventário, equipamentos e composição com regras de mundo |
| M14 | Comandos, carga, motivo de recusa, informação de percepção e feedback para observadores |
| M15/M16 | Alvos, cooperação/PvP, fórmulas, calibração, critérios de prática, migração e validação |

Estado persistente: conhecimento, Mastery e Proficiência pessoais. Estado transitório: carga, deslocamento, leitura focada, efeitos e execuções em curso. Servidor autoriza as operações; cliente apresenta o resultado. Morte/logout/troca de dimensão não podem converter execução transitória em aquisição, regenerar custo pago ou resetar limitações por omissão. A política exata de cada caso será fechada na integração responsável.

O checkout continua sem evidência de origem Hollow jogável e dos três campos implementados. Os pontos de código citados nas rodadas anteriores são localizações históricas de integração, não implementação destas propostas. Nenhum código, modelo, save ou dependência foi alterado; nenhum build ou teste de gameplay novo foi executado.

### 33. Revisão estrutural conjunta de H/I/J — 20/10/2026

**Escopo:** revisão documental das três escolhas, confrontadas com M05 (recursos, recuperação, Mastery e percepção), M06-02 (dano/controle), M06-03/07 (aquisição), M06-05/06 (execução, defesa e mobilidade) e M06-08A–G (campos, corpos e compatibilidade). Não é teste de gameplay, revisão integral de todas as técnicas de Bleach nem aceite de implementação.

**Resultado:** a organização aprovada é compatível com esses contratos. Não foi encontrada uma contradição que exija revogar H/I/J. Foi encontrada uma escolha de execução relevante: preservar ou cancelar a carga de Cero ao defender-se ou usar mobilidade especial. Ela foi submetida ao usuário e resolvida em K: cancelar para reagir, respeitando a recuperação. Os três blocos têm coerência estrutural revisada; parâmetros e contratos detalhados ainda delegados impedem tratá-los como prontos para implementação completa.

| Caso analisado | Conclusão documental | Pendência ou responsável |
|---|---|---|
| Primeira técnica de emissão/mobilidade | Aprendizado inicial não pode exigir Proficiência que só seja obtida usando livremente aquela mesma técnica; aplicar fundamentos conhecidos/tentativa assistida de M06-08D | Ficha de aquisição define evidência inicial sem requisito circular; M06/M12 |
| Cero, Bala e recuperação de Stamina | Emissão usa Energia; Sonído usa Stamina; esgotar um não apaga automaticamente o outro recurso | Valores em M15; respeitar exaustão e energia zero de M05 |
| Cero carregando e Sonído/Dodge/bloqueio | Lacuna identificada e resolvida: cancelar a carga antes dessas ações, respeitando recuperação | K aprovado, seção 34 |
| Cero seguido de Bala ou Sonído | Recovery regula o início da próxima técnica; cooldown de Cero não bloqueia toda a emissão/mobilidade | Calibrar durações; sem novo cooldown global |
| Dano recebido durante carga | Dano sozinho não cancela; controle pertinente pode interromper; Hierro não muda essa regra nem concede Super Armor | Fases/interrupções específicas continuam na ficha |
| Cancelar antes de pagar o disparo | A regra descrita de pagamento na liberação exige recuperação de cancelamento para não produzir ameaças repetidas sem compromisso | Duração e feedback em M14/M15; K fecha a relação com defesa/mobilidade |
| Menos/Gillian com Cero | Acesso a partir do estágio não significa corpo inicial com emissão livre; origem física do disparo respeita perfil e obstáculos | Medidas e perfil em M06/M07; não escolher artes/modelos agora |
| Hierro e guarda | Redução de dano não substitui Postura nem cancela pressão de guarda por consequência automática; é possível Guard Break mesmo com boa proteção corporal | Composição de defesa e efeitos específicos em M11/M15 |
| Hierro e Energia zero | A proteção passiva base não tem drenagem declarada; não inferir desligamento só porque a barra acabou. M05 desliga capacidades que realmente dependem de Energia | Estado/capacidade continuam sendo requisitos; defesas ativas específicas têm contrato próprio |
| Pesquisa e percepção universal | Método racial exige conhecimento compatível; treinar percepção não entrega automaticamente toda técnica racial. A mesma leitura não concede duas Masteries redundantes | Um crédito válido ao fundamento, não créditos duplicados por nomes diferentes |
| Pesquisa contra supressão/obstáculos | Leitura aproximada pode existir sem visão direta; não autoriza revelar informação exata ou atingir através do bloco | M14/M15 definem apresentação e oposição; M06 conserva limites |
| Regeneração rápida em combate | Capacidade especial pode alterar a recuperação natural; não copiar silenciosamente o atraso da regeneração natural nem conceder imunidade | Ficha define ativação, ritmo, custo e limite; M07/M15 tratam estados |
| Treino de passivas e cura | Permanecer recebendo dano ou repetir cura não é, por si só, prática válida; domínio defensivo exige contexto apropriado | Critérios/limites de prática em M15/M16; não criar outra barra |
| Construções, alvos e multiplayer | Colisão e ausência de destruição na base descrita preservam o terreno; aliados/pets continuam dependendo de política de alvo, sem dano aliado presumido | M10/M15; não declarar política de party concluída |
| Morte, logout e mudança de dimensão | Conhecimento/domínio e execução transitória têm responsabilidades distintas; não usar reconexão como restituição ou reset implícito | Casos de persistência/migração em M06-11/M07/M16 |

**Consolidações:** preservar a distinção entre acesso e aquisição; evitar requisito circular na primeira técnica; não contar Pesquisa e Percepção como dois treinos independentes da mesma leitura; não confundir Hierro com guarda/controle; não transformar regeneração especial em reposição universal de recursos.

**Limites do fechamento:** números, parâmetros de colisão, tipos de dano, empilhamento de defesas, política de aliados e fichas de regeneração continuam delegados de forma explícita. A mobilidade aérea/sustentação também não foi concedida por Sonído. Esses itens não foram simulados ou testados nesta revisão.

### 34. M06-08K — DECIDIDO — Carga de Cero e ações defensivas

**Aprovado em 20/10/2026.** Resposta explícita: “Cancelar a carga para reagir (recomendado)”. Durante a carga, caminhar de forma limitada e ajustar a mira são permitidos; bloqueio, Dodge ou Sonído exigem cancelar a preparação e respeitar a recuperação. A alternativa de preservar a carga nessas ações não foi escolhida.

**Origem:** lacuna encontrada na revisão conjunta. M06-06I já impede cancelamento universal; esta escolha define a regra específica do Cero base. Não reabre a carga regulável ou Sonído direcional.

| Alternativa | O que o jogador pode fazer | Consequência |
|---|---|---|
| **A — Cancelar a carga para bloquear, usar Dodge ou Sonído — recomendada** | Durante a carga, ajustar mira e caminhar de forma limitada; para essas ações, encerrar a preparação e respeitar sua recuperação | Mantém o compromisso de preparar um disparo potente e permite ao adversário ler a abertura |
| B — Preservar a carga durante essas ações, com permissões e custos próprios | Preparar, reposicionar-se/defender e então liberar sem reiniciar a carga | Favorece combate móvel, mas exige controlar a conservação da carga e a legibilidade da ameaça |

**Recomendação A:** Cero base com preparação legível e decisão entre continuar o ataque ou abandonar a carga para reagir. Uma técnica específica futura pode ter outra permissão se sua identidade justificar; isso não a concede por Mastery ou raça automaticamente.

Cancelar antes de disparar não cobra o disparo conforme a regra descrita em H, mas não remove a recuperação de cancelamento. Não significa sair instantaneamente para Sonído ou iniciar Bala sem respeitar a execução. O input exato para cancelar e a duração da recuperação pertencem a M14/M15; não acrescentar tecla fixa nesta decisão.

Hierro permanece aplicável durante a preparação enquanto a condição corporal for compatível, pois a proteção base é passiva. Isso não equivale a levantar a guarda nem altera a interrupção por controle. Cancelar a preparação perde a carga acumulada; uma tentativa seguinte começa de novo, sem restaurar ou transferir a carga.

**Estado:** decidido por resposta específica à pergunta encontrada na revisão. A aprovação anterior de Cero carregável foi preservada e complementada; não foi usada como aprovação presumida de K.

### 35. Resultado dos três blocos e continuidade

**Concluído nesta rodada:** registro das três respostas H/I/J, revisão estrutural conjunta e decisão complementar K. Emissão, mobilidade e capacidades defensivas/sensoriais estão organizadas sem exigir novos campos de Proficiência ou uma revisão dos recursos universais.

**Permanecem abertos:** parâmetros de execução e colisão, composição de defesas, alvos/participação, conteúdo concreto de aprendizado, fichas de capacidades especiais e regras de transição. Os destinos estão indicados na seção 32. Custos, tempos e alcances não foram calibrados, e não houve teste de gameplay.

Próximo passo documental: conferir o fechamento geral de M06-08 e seus encaminhamentos, sem antecipar fichas de Grimmjow, formas ou artes. Depois seguir M06-09 — Quincy, M06-10 — Fullbringer e M06-11 — Integração, conforme o mapa. A revisão destes três blocos não fecha automaticamente todo M06 nem os requisitos de evolução de M07.

### 36. Fechamento estrutural geral de M06-08 — 20/10/2026

**Resultado da revisão documental:** M06-08 pode avançar como estrutura decidida. A–D/F–K possuem aprovações explícitas; E consolida a mentoria herdada. Esta conclusão reúne as decisões já tomadas, sem aprovar novas capacidades, candidatos a mestre ou soluções para pendências. O módulo 06 permanece `EM ESPECIFICAÇÃO`.

Foram relidos integralmente os blocos de M06-08 e confrontadas suas fronteiras com o mapa, o handoff e os contratos pertinentes de M04/M05/M06. A revisão conjunta da seção 33 permanece válida. Não foi identificada outra decisão geral que impeça iniciar Quincy; isso não garante um catálogo exaustivo nem substitui as futuras fichas executáveis.

| Parte da experiência | Estrutura definida | O que ainda precisa ser produzido |
|---|---|---|
| Desenvolvimento | Três campos com Proficiência; técnicas/capacidades com Mastery pertinente | Fichas classificadas, evidências de aquisição, critérios e taxas de prática |
| Corpo e fundamentos | Perfis funcionais controlados, cosméticos sem vantagem; aprendizado misto | Catálogo, medidas, movesets, câmera e interações de cada perfil |
| Formação Arrancar | Empunhadura orienta o moveset; técnicas corporais compatíveis continuam possíveis | Armas concretas, alternância, permissões por ação e vínculos de inventário |
| Emissão | Cero carregável a partir de Gillian; Bala rápida Arrancar; cancelar carga para defender/mover | Curvas, custos, tempos, impactos e permissões detalhadas |
| Mobilidade | Sonído direcional Arrancar, com Stamina e trajeto válido | Medidas, colisões parciais, água, desníveis e condições aéreas |
| Defesa e leitura | Hierro passivo; Pesquisa usa Percepção Espiritual; regeneração especial exige compatibilidade | Categorias de dano, composição defensiva, apresentação da leitura e fichas de regeneração |
| Identidade e ensino | Um poder principal, continuidade de linhagem e fontes de aprendizado relevantes | Conteúdo de cada trajetória, mentores, exercícios e disponibilidade por era |

#### Pendências com destino e condição de retorno

| Responsável principal | Entrega necessária | Limite preservado até sua resolução |
|---|---|---|
| M06 — fichas de combate | Moveset, perfil funcional, alvo, custo, preparação, interrupção, conclusão e Recovery de cada capacidade | Estrutura aprovada não torna toda técnica pronta para implementação; M15 calibra, mas não substitui a ficha |
| M06-11 — integração | Compartilhamento de conhecimentos entre raças, prática, execução e ciclo de vida | Aprender um conceito não transfere natureza, poder exclusivo ou execução incompatível; não presumir reset de conquista |
| M07 — estados e transições | Evolução, agência Gillian, Arrancarização, Resurrección, estabilidade e compatibilidade antes/depois | Sem regressão passiva offline; sem Segunda Etapa universal ou retorno implícito à evolução Hollow encerrada |
| M06-11 / M07 / M10 | Separar deslocamento aéreo de combate, capacidade de sustentação por estado e restrições locais | Sonído não concede voo, sustentação ou imunidade de queda; a regra aérea de combate continua responsabilidade de M06 |
| M08 / M12 / M13 | Quem ensina, em qual era, como comprovar aprendizado e retomar conteúdo | Candidatos não são elenco aprovado; nenhum NPC obrigatório por pequena melhoria |
| M10 — travessias | Acesso a Hueco Mundo, Garganta e meios de viagem, destino, retorno e disponibilidade no tipo de save | Não conceder a todo Hollow abertura de passagem, viagem livre ou teleporte por aprender Sonído. Se houver técnica de abertura, sua execução precisa também de ficha M06 |
| M06 / M07 / M10 / M14 | Resolver corpos grandes em bases, passagens e chegada a outra dimensão | Sem forma compacta livre, redução automática ou passagem através de blocos por omissão |
| M11 / M15 | Inventário, ferramentas, arma vinculada, armadura, encantamentos e composição de defesas | Aparência corporal não redefine crafting; Hierro não anula todo dano por empilhamento implícito |
| M14 | Controles, apresentação, câmera, carga, motivo de recusa e informação sensorial | Feedback não concede aquisição nem revela informação oculta não autorizada |
| M15 / M16 | Política de alvos/party/PvP, calibração, migrações e evidências de execução | Sem presumir dano aliado, progresso compartilhado ou funcionamento em servidor dedicado |

**Amostra posterior para validar a estrutura:** fundamento físico em corpo compatível, aquisição de Cero em Gillian, integração corporal/armada Arrancar, Sonído com obstáculo, Hierro e leitura focada. Regeneração rápida só entra quando existir ficha compatível. São cenários de avaliação, não testes executados ou compromisso de entrega simultânea.

**Continuidade:** iniciar M06-09 — Quincy. As fichas de Grimmjow/Pantera, formas e artes permanecem adiadas por orientação do usuário. Não confundir o fechamento deste bloco com conclusão de M06, especificação das evoluções de M07 ou implementação de origem Hollow jogável.

---

# M06-09 — Quincy

## Estado do bloco — 20/10/2026

| Campo | Registro |
|---|---|
| Estado | `DECIDIDO` em nível estrutural; M06-09A–U aprovados. Fichas e integrações permanecem em especificação |
| Responsável pela decisão | Equipe do Bleach Mod |
| Escopo atual | Arco, desenvolvimento, Hirenkyaku, Blut e Gintō consolidados; revisão geral/pendências na seção 39. Próximo bloco: M06-10 — Fullbringer |
| Herança | RAC-09, RAC-19–25 e Q1–Q11 de M04, aplicando as revisões de M05/M06; três recursos e nenhum medidor pessoal adicional de Reishi |
| Evidência | Fontes oficiais consultadas em 20/10/2026; leitura documental e de pontos locais de integração; sem implementação ou teste de gameplay |

### 1. Objetivo e fantasia

O jogador descobre uma tradição espiritual, aprende a formar uma arma confiável e desenvolve precisão, controle e recursos para situações diferentes. O arco introduz o caminho; armas e poderes posteriores podem mudar a maneira de lutar, conforme Q10. Quincy não fica preso ao papel de arqueiro.

Começar pelo arco permite resolver o ciclo de combate mais frequente antes de ampliar o repertório. A organização Quincy está decidida em C, seção 8; a reação durante preparação foi fechada em D, seção 12. Não copiar automaticamente os três campos Hollow ou as quatro disciplinas Shinigami.

### 2. Herança vigente e cuidado com textos antigos

| Contrato | Consequência nesta rodada |
|---|---|
| RAC-09 / Q1 / Q5 | Humano de herança oculta/fragmentada, inicialmente independente; aprende Reishi e arco básico, sem começar treinado ou filiado ao Wandenreich |
| Q2 / Q3 / M05-03E | Sem flechas vanilla obrigatórias, coleta constante de partículas ou segunda barra pessoal de Reishi |
| M05, seções 42 e 23 | Ambiente pode influenciar eficiência; não bloqueia o sistema básico. Energia zero limita ações que dependem dela, preservando ações físicas/Stamina |
| Q6 / Q7 / Q8 / RAC-23 | Fundamentos não concedem Schrift; afiliação não é evolução; um poder principal por vez |
| RAC-19 | Consequência sistêmica distinta por eliminar Hollows permanece em backlog; não criar punição global Quincy ao definir seus disparos |
| M05 / M06-03/04/07 | Atributos, conhecimento, Proficiência e Mastery separados; treino autônomo e fontes de ensino, sem loja ou NPC obrigatório para cada melhoria |

**Aplicação das revisões vigentes:** Q11 e seus exemplos em M04 ainda descrevem Spiritual Points como compra principal de técnicas. Esse trecho histórico deve ser lido à luz de M05, REV-M02-02/04/05, e da aquisição refinada em M06-07: pontos principalmente nos atributos; técnicas por aquisição apropriada e Mastery; nenhum custo obrigatório em pontos presumido para todo marco. Não é uma decisão nova de Quincy nem autorização para retomar a árvore de compras.

Família, registros, comunidade e mentor independente já são fontes permitidas. A alma-guia orienta, mas não substitui domínio técnico de um instrutor. Quem ensina cada conhecimento, disponibilidade por era e exercícios pertencem a M08/M12/M13; esta rodada não escolhe Uryū, Ryūken ou outro personagem como mestre.

### 3. Base canônica e limites da pesquisa

Fontes reconsultadas em **20/10/2026**, com interpretação do japonês. Foram lidas as entradas pertinentes, não capítulos ou episódios integrais. A organização e os custos do mod são adaptações de design.

| Fonte e entrada | Evidência utilizada | Consequência |
|---|---|---|
| [TYBW — Glossário](https://bleach-anime.com/keyword/), `滅却師` | Flechas de Reishi, Hirenkyaku e Blut compõem o repertório Quincy | Planejar armas, movimento e proteção, sem limitar a raça a disparos |
| Mesma fonte, `血装` | Arterie ofensivo e Vene defensivo não operam simultaneamente; treinamento pode desenvolver Blut | Preservar a distinção na futura ficha; não presumir defesa passiva idêntica a Hierro nem exigir sorte de nascimento |
| Mesma fonte, `聖文字` e `滅却師完聖体` | Schrift tem contexto ligado a Yhwach; Vollständig e Letzt Stil são distintos | Separar poder principal, formação racial e forma; requisitos concretos continuam pendentes |
| [Pierrot — Glossário ha](https://pierrot.jp/title/bleach/keyword_ha.html), `飛簾脚` | Movimento rápido sobre fluxo de Reishi sob os pés | Identidade de movimento própria; Stamina como recurso vem da adaptação já aprovada em M05 |

O glossário TYBW é mutável e contém entradas recentes/promocionais; foram utilizadas as entradas indicadas, sem importar todo o conteúdo para todas as eras. O arquivo Pierrot avisa que retrata informações da época de exibição. Não foi verificada a totalidade das exceções de herança, armas, rituais, técnicas ou habilidades dos Sternritter. Gintō, Seele Schneider e capacidades avançadas exigem pesquisa específica antes de suas fichas.

### 4. M06-09A — DECIDIDO — Energia por disparo, sem manutenção contínua do arco básico

**Aprovado em 20/10/2026.** Resposta explícita: “Energia por disparo, sem custo para manter o arco (recomendado)”. A pergunta apresentou gasto de Energia por disparo, custo/cadência adequados à arma principal, arco formado sem consumo contínuo e influência ambiental integrada sem coleta manual. Aprovação estrutural; valores e curva ambiental não foram escolhidos.

| Alternativa | Experiência | Consequência |
|---|---|---|
| **A — Energia em cada disparo; manter o arco não consome continuamente — escolhida** | O jogador administra disparos e outras capacidades na mesma reserva espiritual | Exige calibrar custos e recuperação para a arma principal continuar funcional durante o combate |
| B — Disparos básicos gratuitos; Energia só nas técnicas especiais | A arma oferece ofensiva básica independente da reserva | Simplifica o uso contínuo, mas separa o disparo formado espiritualmente do gerenciamento normal desse recurso |

**Contrato aprovado:**

- Cada disparo básico aceito consome Energia Espiritual; não exige flechas de inventário.
- Manter o arco básico legitimamente formado não possui drenagem contínua. Isso não aprova buffs, técnicas, formas ou tiros gratuitos.
- Custo e cadência precisam permitir uso regular como arma principal; não copiar o custo de uma grande técnica para cada flecha fundamental. Não implica disparo infinito sem redução de ritmo.
- Reishi ambiental pode influenciar eficiência conforme M05/Q3, sem coleta manual obrigatória ou barra adicional. A fórmula continua aberta; nenhuma dimensão torna o fundamento racial inacessível por ausência absoluta de Reishi.
- Recursos insuficientes impedem o disparo dependente de Energia; não produzem valor negativo ou munição por contornar inventário. Ações físicas e Stamina continuam seguindo M05.
- Não presumir regeneração completa ao formar/guardar a arma, trocar modo ou entrar em outra dimensão.

J/K/L, seção 20, decidiram posteriormente foco físico equipado, manifestação básica sem custo adicional e visual possível com Energia insuficiente para tiro. A formação continua exigindo aprendizado legítimo. Equipamento/vínculo, obtenção/reposição e parâmetros seguem para M06/M11/M14; essas escolhas não definem toda arma canônica futura.

**Adaptação identificada:** gastar a barra pessoal por flecha é a representação de recurso escolhida para o mod. Não afirmar que a fonte oficial impõe esse pagamento numérico por projétil.

### 5. M06-09B — DECIDIDO — Preparar e soltar a flecha básica

**Aprovado em 20/10/2026.** Resposta explícita: “Segurar para preparar e soltar para disparar (recomendado)”. A pergunta apresentou preparo curto e limitado, liberação ao soltar e possibilidade de manter a mira no máximo sem continuar aumentando a força. Disparo imediato a cada toque não foi escolhido. Os complementos decididos nas rodadas seguintes e as integrações ainda delegadas estão identificados abaixo.

**Funcionamento estrutural aprovado:** segurar o comando de ataque prepara uma flecha; soltar dispara. A preparação tem limite. Atingir o máximo mantém a mira pronta até soltar/cancelar, sem aumentar indefinidamente a força ou disparar automaticamente. Não escolher tecla ou botão físico antes de M14. A soltura precoce foi decidida em F, seção 15; a duração mínima exata continua pendente de calibração.

| Alternativa | Efeito para o jogador | Cuidado |
|---|---|---|
| **A — Segurar prepara, soltar dispara — escolhida** | Controle direto do momento do tiro; familiaridade com o arco em Minecraft | Preparação curta e movimento precisam ser calibrados para manter ritmo; não transformar toda flecha em grande canalização |
| B — Apertar dispara, com cadência controlada | Combate de tiros imediatos e frequentes | Menos escolha de preparo no fundamento; a cadência e técnicas precisam dar identidade ao arco |

Recomendo A como base: ensina concentração, mira e liberação na primeira arma racial. Tiros rápidos, rajadas e múltiplas flechas podem existir em repertórios apropriados, sem serem concedidos automaticamente a todo Quincy nesta escolha.

**Detalhamento candidato para completar a execução:** a tabela não foi aprovada em conjunto pela resposta sobre o comando. A decisão posterior D, seção 12, fecha movimento/reação durante a preparação. E/F/G, seção 15, decidiram depois pagamento, soltura precoce e trajetória. J/K/L decidiram depois a manifestação/foco em nível estrutural. Equipamento concreto, meios, alvos e outras integrações continuam abertos; a tabela não deve ser tomada como aprovação integral de toda a execução.

| Etapa | Funcionamento proposto |
|---|---|
| Início | Verificar aprendizado, arma/foco legítimos, estado, Energia e permissões da ação atual |
| Preparação | Mira manual e movimento limitado; nenhuma ampliação infinita por segurar o comando |
| Soltura antecipada | Antes do mínimo, encerrar sem projétil; comunicar o motivo sem cobrar um disparo inexistente |
| Máximo de preparo | Pode manter a mira; não guarda a carga depois de cancelar, guardar a arma ou trocar de estado |
| Disparo | Revalidar e pagar Energia uma vez na emissão; direção escolhida pelo jogador |
| Cancelamento | Permissões e ausência de recuperação adicional antes do disparo decididas em D; E decide pagamento na emissão, sem custo de tiro ao cancelar antes dela |
| Depois de emitir | Flecha não retorna ao inventário nem devolve Energia; recuperação/cadência limitam novos tiros |
| Terreno/alvos | Projétil base com colisão sólida e sem destruição de blocos na proposta; alvos/aliados e escudos exigem as políticas de integração |

A e B tiveram respostas próprias. A aprova o modelo de consumo; B aprova o comando e o limite de preparo. Movimento, defesa, Dodge e Hirenkyaku durante preparação foram decididos posteriormente em D. E/F/G decidiram posteriormente pagamento na emissão, dano crescente e limitado após mínimo curto e trajetória reta. Tempos exatos, curva de dano e velocidade continuam para especificação/calibração. Não presumir autoaim, perfuração, dano por quadro visual ou comportamento de encantamentos vanilla. As capacidades especiais terão fichas próprias.

### 6. Integração, validação futura e continuidade

**Encaminhamento original após A/B; para a situação vigente, consultar a seção 39.**

| Responsável | Trabalho necessário |
|---|---|
| M06-09, próximas rodadas | Decidir execução básica; organizar Proficiência e Mastery; detalhar aprendizado, Hirenkyaku, Blut e repertórios conforme pesquisa |
| M05 / M15 | Calibrar custo, recuperação e influência ambiental para sustentar a arma principal sem tornar a reserva irrelevante |
| M07 / M08 / M12 / M13 | Formas, acesso avançado, instrutores, afiliações e acontecimentos; sem concluir Schrift/Vollständig nesta rodada |
| M10 / M11 | Condições ambientais, ferramentas/focos, troca/perda e interação com inventário |
| M14 / M06-11 / M16 | Comandos de combate e construção, feedback, autoridade, persistência e integração com dano/projéteis |

**Estado conhecido do código:** relido [ConfirmCharacterC2S.java](../../src/main/java/com/bleachmod/common/network/c2s/ConfirmCharacterC2S.java), que ainda aceita apenas criação Shinigami. Isso impede tratar origem Quincy como entrega demonstrada; não constitui auditoria exaustiva de todo possível código experimental Quincy. Nenhum código, biblioteca ou save foi alterado nesta rodada.

**Cenários posteriores de aceitação:** disparar sem flechas vanilla; manter arco sem drenagem contínua; respeitar Energia insuficiente; não cobrar duas vezes nem devolver custo após emissão; manter o fundamento utilizável nos ambientes previstos; distinguir aquisição do arco, prática e domínio; preservar hotbar/inventário reais ao alternar Combat Mode. A calibração precisa observar um combate real, e não apenas confirmar que um projétil aparece.

**Resultado desta rodada:** M06-09A/B aprovados por duas respostas explícitas. Não reapresentar custo por disparo, ausência de manutenção contínua ou comando de preparar/soltar como decisões abertas. Os detalhes candidatos acima não estão aprovados por associação.

**Encaminhamento da rodada A/B:** organizar desenvolvimento do repertório e completar a execução do arco. A classificação do disparo fundamental foi fechada em C; movimento/reação, em D. Ver a seção 14 para a continuidade atual. O fechamento de M06-08 não autoriza antecipar fichas de linhagem ou artes.

### 7. Formação Quincy — pesquisa e objetivo desta rodada

**Data:** 20/10/2026. **Estado:** organização estrutural decidida em M06-09C após resposta explícita; A/B preservados. Objetivo: permitir que práticas diferentes produzam formações diferentes, mantendo legível a diferença entre saber usar uma arma, conhecer uma técnica e dominá-la.

**Experiência pretendida:** quem combate com arco pode desenvolver boa formação de armamento e ainda precisar aprender a se deslocar, utilizar Blut ou executar fórmulas. Um conhecimento novo pode abrir possibilidades de prática sem exigir retorno ao instrutor a cada melhoria. Não existe classe permanente que bloqueie os outros campos.

#### Referências desta rodada

| Fonte consultada em 20/10/2026 | Evidência utilizada | Limite para o design |
|---|---|---|
| [TYBW — Glossário](https://bleach-anime.com/keyword/), `血装` | Blut possui aplicações ofensiva e defensiva, incompatíveis simultaneamente; pode ser aprendido por treinamento | Fundamenta tratá-lo como conhecimento específico; não define número de indicadores ou funcionamento de ativação |
| Mesma fonte, `飛廉脚` | Hirenkyaku utiliza fluxo de Reishi para deslocamento | Não define controles, distância, Proficiência ou requisitos do mod |
| Mesma fonte, `魂を切り裂くもの` | Seele Schneider tem usos próximos/distantes, incluindo disparo como flecha | Não confundir aparência de lâmina com formação Shinigami; não conceder a ferramenta nesta rodada |
| [Pierrot — Glossário sa](https://pierrot.jp/title/bleach/keyword_sa.html), `破芒陣 / シュプレンガー` | Sprenger combina uma preparação com Seele Schneider e Reishi condensado em tubos | Sustenta estudar fórmulas e ferramentas; não aprova explosão de blocos, receita ou custo de inventário |

Foram lidas as entradas indicadas, com interpretação do japonês, sem leitura integral de capítulos/episódios. Pierrot identifica seu arquivo como relativo à época de exibição; não generalizar sua descrição antiga de arma de lâmina como exclusividade de todas as eras. O exemplo de Sprenger sustenta preparação especializada, não um catálogo completo de Gintō. As três Proficiências aprovadas abaixo são organização do mod, não uma tríade oficial da obra.

### 8. M06-09C — DECIDIDO — Campos de prática, fundamentos e Masteries

**Aprovado em 20/10/2026.** Resposta explícita: “Três campos + Masteries específicas (recomendado)”. A pergunta apresentou os três campos, Gintō aprendido separadamente, técnicas distintas com Mastery, tiro fundamental sem Mastery própria e aplicações de Blut com domínio próprio. As alternativas permanecem como histórico; conteúdo, fórmulas e critérios numéricos não foram aprovados por associação.

**Organização aprovada:** três campos com Proficiência — **armamento espiritual**, **mobilidade** e **Gintō**. Gintō é uma formação aprendida separadamente em técnicas com tubos espirituais; não será pré-requisito universal do arco, da mobilidade ou da identidade Quincy. Técnicas distintas possuem Mastery pertinente; o tiro fundamental desenvolve armamento sem receber outra Mastery apenas para repetir aquele fundamento. Blut Vene e Blut Arterie têm Masteries próprias, sem Proficiência geral de Blut adicional.

| Alternativa | Como o personagem se desenvolve | Consequência |
|---|---|---|
| **A — Três campos + Masteries específicas — escolhida** | Prática com armas, deslocamento e fórmulas desenvolve preparos diferentes; capacidades como Blut mantêm domínio próprio | Exige classificar o que foi efetivamente praticado e produzir conteúdo de formação para Gintō |
| B — Uma Proficiência geral de manipulação de Reishi + Masteries | Uma formação racial única recebe as práticas Quincy; técnicas preservam domínio individual | Menos indicadores, mas disparar repetidamente pode fornecer preparo geral para práticas muito diferentes; exige restringir os acessos por conhecimento |
| C — Somente Masteries de capacidades | Progresso fica concentrado em cada execução | Dispensa indicadores de formação, mas exige resolver o desenvolvimento dos fundamentos sem criar uma Mastery para cada ação básica |

**Por que recomendo A:** diferencia experiências de treino e permite reconhecer especialização sem separar a raça em classes. Gintō oferece um campo de preparação e fórmulas para quem o aprende; não é categoria residual para tudo o que usa Reishi. A quantidade de campos decorre dessa organização, não da necessidade de repetir a quantidade Hollow.

#### Classificação na organização aprovada

| Camada | O que inclui | Desenvolvimento | O que não concede |
|---|---|---|---|
| Armamento espiritual | Formação/uso da arma legitimamente disponível, mira, disparo fundamental e combate armado compatível | Proficiência do campo; técnicas de execução distinta podem ter Mastery | Toda arma da obra, poder de uma Schrift ou acesso livre a qualquer forma de arma |
| Mobilidade | Formação racial de deslocamento, parada e reposicionamento | Proficiência do campo e Mastery de Hirenkyaku/técnicas distintas quando adquiridas | Voo, imunidade de queda, sustentação ou teleporte por omissão |
| Gintō | Conhecimentos e fórmulas que utilizam tubos espirituais, com ferramentas quando a técnica exigir | Proficiência do campo e Mastery de cada fórmula/técnica adquirida | Todas as fórmulas por obter um tubo; domínio de armas ou circulação corporal |
| Blut | Aplicações defensiva e ofensiva aprendidas | Mastery própria de Vene e de Arterie; sem outro indicador geral | Aquisição ao maximizar armamento, uso simultâneo das duas aplicações ou defesa equivalente a Hierro |
| Percepção e Reiatsu | Fundamentos e capacidades universais de M05 | Progressões já existentes, sem duplicação pelo nome racial | Leitura exata universal ou outro atributo de controle |
| Poder principal / formas | Técnicas da identidade canônica conquistada e estados compatíveis | Masteries pertinentes e contratos de M07 | Várias Schrifts simultâneas ou liberação por Proficiência alta isolada |

**Fronteira importante:** controle de Reishi é fundamento da tradição, e Controle continua atributo de M05. A organização A não cria outra Proficiência transversal alimentada automaticamente por todos os campos, nem outra barra de recurso. Treino inicial de concentração/formação pode pertencer à formação de armamento; outro exercício precisa declarar qual aplicação está ensinando. Não duplicar o mesmo progresso apenas porque toda aplicação utiliza Reishi.

**Tiro fundamental:** depois da aquisição legítima do arco, preparar, mirar e soltar são ações da arma. A organização aprovada não exige comprar cada disparo ou preencher uma Mastery do tiro comum paralela à Proficiência. Melhorias simples podem refinar o fundamento; execução realmente distinta justifica técnica e Mastery próprias. Esta classificação não aumenta automaticamente dano, remove o preparo aprovado em B ou garante acerto: atributos e execução do jogador mantêm suas funções.

**Blut:** a opção por Masteries das aplicações evita, nesta arquitetura, acrescentar um indicador de formação para o mesmo conjunto de capacidades. Hirenkyaku pertence a um campo que também organiza fundamentos de movimento; Gintō organiza um repertório de fórmulas. A distinção é de granularidade do mod, não uma afirmação canônica sobre quais artes são mais difíceis. Aquisição, ativação, custo, troca entre Vene/Arterie e critérios de treino continuam para a ficha posterior. A existência de Mastery defensiva de Vene já é prevista em M05.

#### Prática e atribuição de progresso

```text
fundamento conhecido + prática apropriada → Proficiência do campo pertinente
técnica adquirida + uso válido → Mastery da técnica
quando essa técnica exercita um campo → Proficiência pertinente também pode progredir
```

Um disparo comum não concede prática de Gintō, Blut e mobilidade porque consome Energia. Mover-se durante uma ação também não garante crédito de todas as artes. A ficha identifica o que é praticado; em uma técnica composta, pode identificar contribuições reais de mais de um campo, sem criar uma Proficiência híbrida ou recompensar repetidamente a mesma execução.

Para uma capacidade do poder principal, o progresso de campo só se aplica quando houver prática efetiva daquele fundamento. Ativar uma Schrift não treina automaticamente todo o repertório Quincy.

### 9. Aquisição e papel dos mestres — aplicação dos contratos herdados

**Não é nova votação sobre a existência de ensino.** RAC-12/Q5/Q6, M05 seção 40 e M06-03C/03D/M06-07 já permitem família, registros, tradição, orientação, mestres e desenvolvimento autônomo. C aprovou a organização dos campos; estes contratos herdados de aprendizado foram preservados.

| Momento | Contrato a preservar |
|---|---|
| Primeira arma | A formação inicial ensina reunir/moldar e executar; não exigir Proficiência obtida exclusivamente com uma arma ainda inacessível |
| Novo campo ou repertório | Conhecimento apropriado pode abrir fundamentos e múltiplas técnicas desenvolvíveis; não entrega Mastery máxima |
| Refinamento simples | Pode ser incorporado com o preparo pertinente; não precisa virar uma quest ou outra Mastery |
| Execução distinta | Preparo e prática apropriada podem concluir a aquisição; uma tentativa assistida tem limites próprios |
| Conhecimento especializado | Mantém fonte e condições próprias; Proficiência representa preparo e não substitui descoberta |
| Desenvolvimento autônomo | Trabalha o que foi legitimamente aprendido; não exige retorno ao mestre para cada melhoria |

Um instrutor de Gintō poderia abrir o estudo de fórmulas; um registro poderia ensinar um método compatível; um avaliador poderia exigir aplicação. São funções de conteúdo, não elenco, currículo ou quests aprovados. Encontrar um personagem canônico não o torna automaticamente professor de todos os fundamentos nem concede seu poder principal. Disponibilidade por era e retomada permanecem em M08/M12/M13.

**Não exigir todas as formações para continuar Quincy:** aprender Gintō é uma possibilidade de especialização. Uma capacidade composta pode exigir conhecimentos específicos quando a sua ficha justificar; isso não transforma Gintō em pedágio de toda a progressão racial. Também não cria escolha permanente que bloqueie outro campo depois.

### 10. Contratos de Minecraft e critérios de revisão

| Caso | Resposta coerente com a proposta e contratos herdados |
|---|---|
| Formar/guardar arco ou manter uma tecla pressionada | Não gera progresso apenas por tempo ativo; uso ou exercício pertinente precisa ser identificado |
| Treino intencional de mira | Pode ser válido sem exigir matar algo; ficha precisa de alvo/contexto observável, sem spam em bloco como substituto |
| Mobilidade antes de Hirenkyaku | Utilizar fundamentos conhecidos ou contexto assistido, sem requisito circular de executar livremente a técnica bloqueada |
| Receber dano com Vene ou repetir cura | Dano passivo/loop não é treino eficiente; avaliação defensiva precisa de contexto apropriado, conforme M05/M06 |
| Obter, trocar ou fabricar um tubo | Posse do item não equivale a conhecimento ou Mastery; receita, cargas e consumo continuam em M11/ficha |
| Flechas do arco versus ferramentas especializadas | Q2/A continuam dispensando flechas vanilla. Isso não torna gratuitos ou imateriais todos os instrumentos usados por outras técnicas |
| Usar Seele Schneider em preparação ou combate | Classificar cada aplicação efetiva; seu uso em fórmula não concede automaticamente experiência integral de todos os campos |
| Usar ferramenta, minerar ou construir | Não concede Proficiência Quincy só por executar atividade com um item na mão |
| Estado do personagem e inventário | Conhecimento, Proficiência e Mastery são pessoais; um item emprestado não transfere conquistas |
| Corpo a corpo sem armamento racial | Ações físicas universais continuam possíveis; compartilhamento de formação marcial entre raças depende de M06-11, sem criar outra Proficiência por omissão |
| Party, logout e mudança de dimensão | Progresso depende da prática pessoal válida; não compartilhar ou resetar conquistas automaticamente. Integração do ciclo de vida em M06-11/M16 |

**Critérios documentais:** distinguir formação de domínio; permitir primeiro aprendizado sem circularidade; não exigir Gintō para toda rota; preservar custos/comando A/B; não duplicar Controle/percepção; não conceder capacidades específicas por Proficiência; identificar critérios de prática antes de configurar ganhos.

**Amostra conceitual futura:** comparar tiro fundamental, uma técnica distinta de armamento, Hirenkyaku, uma fórmula pesquisada e uma aplicação de Blut. Isso permite observar formação e domínio com papéis diferentes. Não constitui catálogo aprovado, compromisso de primeira entrega ou teste executado.

### 11. Estado, integrações e continuidade desta rodada

**M06-09C está decidido por resposta explícita.** A regra de Mastery do tiro comum, a organização dos campos e a granularidade de Blut integraram a pergunta respondida. A/B foram preservados. A aprovação estrutural não define catálogo, números, currículo ou nomes de mestres; não reabrir a organização já escolhida.

M06 define classificação, aquisição e critérios de uso; M07 fornece compatibilidade por estado; M08/M12/M13 concretizam ensino; M11 trata ferramentas; M14 apresenta progresso e estados de conhecimento; M15/M16 calibram e integram. Escalas de Proficiência/Mastery, curvas de ganho e catálogo de fórmulas não foram preenchidos nesta rodada.

Não houve alteração de gameplay, build, save ou dependências, nem execução de teste em jogo. A leitura anterior do checkout continua sendo a referência conhecida; esta rodada não afirma auditoria nova de implementação Quincy.

**Continuidade após C:** movimento e reação durante preparação foram decididos na seção 12. Completar o restante da execução do arco antes das rodadas de Hirenkyaku e Blut. Pesquisar Gintō/ferramentas em detalhe quando a ficha correspondente for trabalhada; Sprenger não entra automaticamente como técnica disponível ou explosão de terreno.

### 12. M06-09D — DECIDIDO — Movimento e reação durante preparo do arco básico

**Aprovado em 20/10/2026.** Resposta explícita: “Cancelar a flecha para reagir (recomendado)”. A pergunta apresentou caminhada lenta e mira durante o preparo, cancelamento da flecha para bloquear/Dodge/Hirenkyaku, carga perdida e ausência de recuperação adicional por cancelar antes do disparo. Requisitos e custos da reação e recuperação após disparar permanecem válidos. A alternativa de preservar a flecha durante defesa/mobilidade não foi escolhida.

**Experiência aprovada:** o jogador pode abandonar um tiro ainda não disparado para reagir. Depois da reação, precisa preparar outra flecha. O disparo já realizado continua tendo compromisso e cadência próprios.

| Situação | Regra aprovada |
|---|---|
| Preparar flecha | Caminhar com velocidade reduzida e ajustar a mira; valores de movimento/giro ainda serão calibrados |
| Bloquear, usar Dodge ou Hirenkyaku antes do disparo | Cancelar a flecha preparada e sua carga; não transportar a preparação durante a reação |
| Recuperação de cancelamento | Não acrescentar uma recuperação só por abandonar o preparo do tiro fundamental |
| Ação de reação | Precisa estar legitimamente disponível; conserva seus próprios requisitos, recursos, limitações e recuperação |
| Novo tiro | Começa outra preparação; não retoma carga perdida e respeita a ação atual e tempos ainda vigentes |
| Depois de emitir a flecha | Recuperação/cadência do disparo continuam valendo; não usar cancelamento de preparo para apagar uma execução já concluída |

**Aplicação específica de M06-06I:** a permissão pertence à fase de preparação do arco básico. Não cria cancelamento universal de toda técnica Quincy, rajada ou poder principal. A decisão D não escolheu as fases defensivas depois do disparo; essa lacuna foi decidida posteriormente em I, seção 18, sem apagar a recuperação ofensiva.

**Distinção em relação ao Cero:** M06-08K exige recuperação após cancelar a carga de Cero. D não revisa K: o tiro fundamental do arco possui permissão própria de reação, sem recuperação adicional de cancelamento. Não copiar o contrato de uma grande emissão para todos os ataques básicos.

Pagamento e recuperação são aspectos diferentes. D aprova a ausência de recuperação adicional ao cancelar antes de disparar; o momento do pagamento foi concluído depois em E, seção 15: cobrar somente ao emitir, sem custo de tiro ao cancelar antes disso. Falhas complementares continuam identificadas separadamente, sem duplicar custo por preparação e disparo.

#### Encaminhamentos da execução

- M14 define o comando físico, prioridade de inputs e feedback; não exigir uma nova tecla apenas porque existe cancelamento.
- M06 completa a regra de solicitação inválida: por exemplo, tentar Hirenkyaku sem recurso não pode conceder deslocamento ou outra ação indevida. H, seção 18, decidiu manter o preparo ainda válido quando a reação é rejeitada.
- M15 calibra caminhada, preparação e cadência; não cria custo duplo para a reação só porque o arco estava sendo preparado.
- M06-11/M16 tratam concorrência, autoridade e ciclo de vida. Um cancelamento anterior não pode desfazer custo/efeito de flecha já emitida.

### 13. Revisão documental de C/D — 20/10/2026

| Caso confrontado com os contratos universais | Resultado e limite |
|---|---|
| Tiro comum versus técnica | C classifica o tiro como fundamento com Proficiência; não elimina custos e preparo de A/B |
| Técnicas distintas/Blut | Masteries próprias não criam ranks compráveis nem outra Proficiência de Controle/Reishi |
| Gintō e primeira arma | Formação separada; não vira requisito universal para avançar com o arco |
| Preparação → Dodge/Hirenkyaku | Cancelar a flecha sem recuperação adicional de cancelamento é permissão específica de D; a mobilidade continua exigindo disponibilidade e seus próprios custos |
| Reação → tiro | Não preservar carga nem pular a recuperação da reação; começar novamente quando a execução permitir |
| Disparo → tentativa de cancelar | D não elimina recuperação, custo ou projétil já emitido. E, seção 15, define pagamento somente na emissão |
| Cero versus arco | Contratos de cancelamento diferentes e explícitos; K permanece válido |
| Proficiência alta versus restrição de execução | Formação não contorna energia insuficiente, incompatibilidade, indisponibilidade ou restrições de fase |

**Resultado:** não foi identificado conflito que exija rever A/B, C ou D nesta revisão documental. Permanecem lacunas de execução abaixo. Sem teste em jogo ou demonstração de responsividade, cadência e equilíbrio; não declarar o arco completamente especificado para implementação.

### 14. Continuidade após C/D

**Encaminhamento histórico após C/D:** consumo, preparo e trajetória foram fechados depois em E/F/G. Naquele momento, a continuidade estava nas seções 18–19; falhas complementares continuam separadas. Formação/manifestação, foco e vínculo com inventário continuam nas fronteiras de M06/M11/M14.

Depois, detalhar Hirenkyaku e Blut em seus próprios blocos; conhecer seus campos/Masteries não aprova aquisição, custo, ativação ou elenco de ensino. Números ficam pendentes de calibração. Gintō ainda exige fichas pesquisadas. Esta rodada alterou apenas documentação; nenhuma implementação, build ou teste de gameplay foi executado.

### 15. Complemento do arco — consumo, preparo e trajetória — 20/10/2026

**Estado:** M06-09E/F/G `DECIDIDOS` por três respostas explícitas em 20/10/2026; A–D preservados. As propostas iniciais e alternativas foram mantidas como histórico, com as escolhas identificadas abaixo. Esta rodada completa o ciclo do tiro fundamental antes de Hirenkyaku e Blut; não abre fichas de linhagem ou seleção de artes.

**Referência e adaptação:** reconsultado o [glossário oficial TYBW](https://bleach-anime.com/keyword/) em 20/10/2026. O trecho sobre `散霊刻片（ライデンシェルベン）` menciona reunir Reishi do ambiente e formar arco/flechas; foi utilizado somente esse fundamento, sem importar o objeto descrito ou conteúdos promocionais de outras entradas. A fonte não estabelece curva de dano, pagamento na barra pessoal, trajetória universal ou controles. E/F/G são adaptações de gameplay aprovadas para o mod. Não foi lido capítulo/episódio integral nesta rodada.

**Herança aplicada:** A exige Energia por disparo e dispensa manutenção contínua; B exige segurar/soltar, preparo curto/limitado e possibilidade de mirar no máximo; C não cria Mastery do tiro comum; D permite abandonar o preparo para reagir sem recuperação adicional de cancelamento. M06-05M permite pagamento específico por capacidade; M06-05R/O e M06-06I preservam recuperação e permissões por fase.

#### M06-09E — DECIDIDO — Pagar somente na emissão aceita

**Aprovado em 20/10/2026.** Resposta explícita: “Cobrar somente ao emitir (recomendado)”. A pergunta incluiu ausência de custo de tiro ao preparar/cancelar, falta de Energia impedindo a emissão e ausência de reembolso após errar/colidir. A alternativa não escolhida permanece como histórico; parâmetros numéricos e integrações delegadas não são aprovados por associação.

**Regra aprovada:** cobrar uma vez quando a flecha é efetivamente emitida. Preparar, manter a mira ou cancelar antes disso não paga o custo do disparo. A regra trata a flecha fundamental; custo de manifestação inicial e outras técnicas continuam separados.

| Alternativa | Efeito para o jogador | Avaliação |
|---|---|---|
| **Pagar ao emitir — escolhida** | Pode mirar e desistir sem perder o custo do tiro; precisa ter Energia quando solta | Combina com D e deixa o gasto associado a um efeito que realmente saiu |
| Pagar ao iniciar o preparo | Compromete Energia antes de disparar, inclusive se desistir | Exige decidir reembolso ou perda por cancelamento e aumenta o custo de reagir |

**Contrato da decisão e requisitos de integração:**

- Verificar elegibilidade ao começar e revalidar recurso, arma/estado e permissão ao soltar; iniciar o preparo não reserva Energia nem garante emissão futura.
- Emissão aceita e débito formam uma operação autorizada pelo servidor. Uma solicitação não pode produzir duas flechas ou duas cobranças. Falha anterior à emissão não cobra um tiro.
- Se outra ação consumiu a reserva durante a mira, a soltura sem Energia suficiente encerra a tentativa sem flecha e comunica o impedimento. Não criar dívida, disparo pendente ou execução automática após regenerar.
- Cancelamento, interrupção anterior à emissão ou soltura inválida não pagam o custo de emissão. Seus efeitos sobre estados seguem as regras próprias, sem conceder reação indisponível.
- Uma flecha emitida conserva seu custo ao errar, colidir imediatamente ou encontrar proteção; não devolver Energia por ausência de dano.
- Com F, o custo da mesma flecha fundamental não aumenta com o tempo de preparo: tiro incompleto válido e tiro completo pagam o mesmo custo, dadas as mesmas condições de personagem/ambiente. Isso não congela a futura fórmula de eficiência de M05/M15.

**Limite:** ausência de pagamento prévio não concede regeneração extra, treino por manter a mira ou uso gratuito de Blut/Hirenkyaku. A interação da recuperação natural com estados de combate continua sob M05; não aprovar aqui outra regra de regeneração.

#### M06-09F — DECIDIDO — Mínimo curto, flecha incompleta válida e dano limitado

**Aprovado em 20/10/2026.** Resposta explícita: “Permitir flecha incompleta após mínimo curto (recomendado)”. A pergunta incluiu cancelamento antes do mínimo, dano reduzido depois dele, aumento limitado até o máximo e mesmo custo de Energia/velocidade do projétil. A alternativa não escolhida permanece como histórico; parâmetros numéricos e integrações delegadas não são aprovados por associação.

**Regra aprovada:** antes de um mínimo curto, soltar desfaz o preparo sem projétil. Depois desse mínimo, já pode emitir uma flecha com dano reduzido; continuar preparando aumenta o dano até o máximo. Atingido o máximo, manter a mira não dá força adicional, conforme B.

| Alternativa | Escolha no combate | Avaliação |
|---|---|---|
| **Tiro incompleto após mínimo curto — escolhida** | Soltar mais cedo para responder a uma abertura ou preparar completamente para aproveitar melhor a Energia | Mantém o arco responsivo e dá função ao preparo; exige calibrar dano e cadência |
| Só emitir depois do preparo completo | Toda flecha exige concluir a formação; soltar antes apenas cancela | Execução simples, mas perde a escolha entre rapidez e força |

**Contrato da decisão e requisitos de integração:**

- O mínimo não é um estágio de progressão ou requisito de Proficiência; é uma fase curta da execução conhecida.
- O benefício aprovado é dano crescente e limitado. Não acrescentar automaticamente aumento de velocidade, perfuração, explosão, alcance ou precisão assistida por preparar mais tempo.
- Flechas válidas incompletas e completas seguem a mesma regra de trajetória e velocidade da ficha básica. A mira e a antecipação do jogador continuam relevantes.
- Soltar antes do mínimo encerra a tentativa, não armazena comando para disparar depois; mostrar que a flecha não estava pronta. Sem custo de emissão em E e sem recuperação adicional por abandonar esse preparo em D.
- Emitir uma flecha incompleta continua sendo um disparo: paga em E e respeita a recuperação/cadência. Não usar tiro fraco para apagar a recuperação ou multiplicar ganhos de prática.
- Duração mínima/máxima, curva de dano e cadência permanecem para a ficha M06 e calibração M15. Verificar futuramente se os tiros completos compensam o tempo sem tornar os incompletos inúteis ou dominantes.

**Não revisa B:** esta é a definição complementar do que acontece antes de atingir o máximo. B já decidiu manter a mira no máximo, sem disparo automático. F aprova a relação entre preparo e dano; não escolhe a curva numérica final.

#### M06-09G — DECIDIDO — Projétil espiritual em linha reta

**Aprovado em 20/10/2026.** Resposta explícita: “Linha reta com tempo de viagem (recomendado)”. A pergunta incluiu mira manual sem perseguição, ausência de queda, tempo de viagem, término no primeiro impacto válido, obstáculos bloqueando e ausência de destruição de blocos/munição coletável. A alternativa não escolhida permanece como histórico; parâmetros numéricos e integrações delegadas não são aprovados por associação.

**Regra aprovada:** a flecha básica percorre uma linha reta na direção da mira no instante do disparo, com velocidade finita, sem queda gravitacional e sem perseguir o alvo. Possui tempo de viagem; não é acerto instantâneo.

| Alternativa | Efeito para o jogador | Avaliação |
|---|---|---|
| **Linha reta com tempo de viagem — escolhida** | Precisa mirar e antecipar o movimento do alvo; a altura não exige compensar uma parábola | Diferencia o fundamento espiritual e mantém deslocamento/cobertura relevantes |
| Trajetória com queda | Precisa também compensar distância e altura como num arco balístico | Acrescenta uma habilidade de mira, mas exige ensinar outra relação entre distância e preparo |

**Contrato da decisão e requisitos de integração:**

- Direção definida na emissão; mover a mira depois não curva a flecha. Mira manual não oferece acerto garantido.
- Colisão real com obstáculos: uma parede entre origem e alvo bloqueia a flecha. A câmera não permite nascer com o projétil do outro lado de uma construção.
- A flecha básica termina no primeiro impacto válido; não perfura por padrão. A elegibilidade de alvos, aliados/pets e escudos segue a integração de combate, ainda a especificar em M06-11/M15.
- Impacto em bloco não destrói terreno, não minera e não gera flecha coletável. Técnicas futuras que alterem essas propriedades exigem ficha própria.
- Alcance e duração de vida limitados; encerrar em limite de área carregada, sem carregar/gerar chunks para continuar o tiro. Medidas, fronteiras e sincronização pertencem à ficha M06/M06-11/M16.
- Água, portais e outros meios exigem tratamento explícito antes da implementação. Esta decisão não concede viagem entre dimensões ou imunidade universal a efeitos do ambiente.
- Velocidade, alcance e tamanho da colisão ficam para calibração. Aparência luminosa, partículas ou animação não calculam o impacto.

### 16. Revisão documental e dependências de E/F/G — 20/10/2026

| Situação futura de aceitação | Resultado esperado pelos contratos aprovados; ressalvas nas linhas identificadas |
|---|---|
| Preparar e reagir legitimamente antes de emitir | Perde a carga, sem custo de tiro nem recuperação extra de cancelamento; reação paga seus próprios custos |
| Soltar antes do mínimo | Sem flecha, custo ou disparo guardado; feedback legível |
| Soltar entre mínimo e máximo | Uma flecha com dano reduzido, custo normal e recuperação do tiro |
| Manter a mira no máximo | Sem força crescente, tiro automático ou drenagem de preparo |
| Energia torna-se insuficiente antes da soltura | Tentativa encerra sem emissão/cobrança; não dispara sozinha depois |
| Flecha emitida erra ou colide numa parede | Custo permanece; parede não é destruída e não há munição coletável |
| Dois pedidos para a mesma emissão | Não duplicar flecha, custo ou crédito de prática |
| Tentar reação indisponível durante preparo | Decidido posteriormente em H: rejeitar a reação com feedback e manter o preparo se ele ainda for válido; não perder a flecha só por um pedido que não pôde executar |
| Desconectar, morrer, guardar a arma ou trocar de dimensão durante preparo | Decidido posteriormente em H: limpar preparo transitório, sem transportar carga para a retomada; preservar conquistas pessoais conforme contratos existentes |

**Estados e responsáveis:** M06 possui preparo, emissão e fases de recuperação; o servidor autoriza custo/efeito e o estado transitório. M05/M15 possuem fórmulas e calibração de recursos/dano; M14 informa mínimo, máximo, falha e comandos; M11 resolve foco/vínculo/inventário; M06-11/M16 concretizam ciclo de vida, colisão e concorrência. Não acrescentar um novo medidor racial ou biblioteca por esta especificação.

As duas últimas linhas foram decididas posteriormente em H, seção 18; a reação após emitir foi decidida em I. A revisão não substitui ficha executável ou teste de gameplay. Números e interação com meios/escudos continuam delegados; manifestação inicial foi decidida posteriormente em J/K/L; a permissão de reação após emitir foi decidida em I.

### 17. Continuidade após E/F/G

**Encaminhamento histórico após E/F/G:** consumo na emissão, soltura parcial e trajetória básica aprovados. Reação inválida, limpeza do preparo e permissões após disparar foram decididas depois em H/I, seções 18–19. Naquele momento, a continuidade era manifestação/foco e vínculo com inventário, depois Hirenkyaku e Blut; essas rodadas foram concluídas depois. Não reabrir o modelo de campos ou as decisões anteriores. Sem implementação, build, alteração de save ou teste em jogo nesta rodada.

**Resultado da revisão após as três respostas:** não foi identificado conflito que exija rever A–D. E preserva o cancelamento sem custo do tiro; F mantém a decisão de segurar/soltar e o limite de preparo; G conserva mira manual e obstáculos relevantes. O preparo pode melhorar o dano pelo mesmo gasto, mas o equilíbrio entre rapidez, dano por tempo e eficiência energética depende de calibração futura. As recomendações complementares da seção 16 foram decididas posteriormente em H; I completa a reação após emitir. J/K/L, seção 20, completam a estrutura de manifestação/foco. Registro e revisão documental não demonstram funcionamento em jogo.


### 18. Falhas e recuperação do arco — 20/10/2026

**Estado:** M06-09H/I `DECIDIDOS` por duas respostas explícitas em 20/10/2026; A–G preservados. Esta rodada conclui as duas recomendações complementares da seção 16 e a lacuna de reação após emissão. São escolhas de gameplay do mod, baseadas nos contratos locais de execução; não representam novas afirmações sobre o cânone. Não houve nova pesquisa externa, alteração de código ou teste em jogo.

**Objetivo:** permitir que o jogador compreenda por que uma ação falhou, reaja durante o combate e reconheça quando pode começar outro ataque. Não criar nova barra ou comando obrigatório para cada transição.

#### M06-09H — DECIDIDO — Falha de reação preserva preparo válido; perda do estado limpa o preparo

**Aprovado em 20/10/2026.** Resposta explícita: “Manter preparo válido se a reação falhar (recomendado)”. A pergunta incluiu rejeitar reação indisponível com aviso, manter preparo ainda válido e limpar carga ao guardar/perder arma, morrer, desconectar ou mudar de dimensão, sem custo de tiro ou perda de conquistas. A alternativa não escolhida permanece como histórico; números e integrações delegadas continuam pendentes.

**Regra aprovada:** validar uma reação antes de trocar para ela. Se o jogador pedir Dodge, bloqueio ou Hirenkyaku indisponível, a reação falha com feedback e o preparo continua se ainda for legítimo. Se o arco é guardado, o personagem morre, desconecta ou muda de dimensão, limpar a flecha preparada; retomar exige novo preparo.

| Alternativa | Experiência | Avaliação |
|---|---|---|
| **Preservar preparo válido se a reação falhar — escolhida** | Tentar uma reação sem recurso não apaga uma flecha que o personagem ainda consegue preparar | Feedback precisa distinguir reação rejeitada de cancelamento realizado |
| Cancelar preparo mesmo com reação rejeitada | Pedir reação sempre desfaz a flecha, ainda que o personagem não consiga executar o pedido | Oferece um cancelamento implícito, mas pode frustrar a intenção de defender ou se mover |

**Contrato da decisão e requisitos de integração:**

- Reação aceita antes da emissão aplica D: descartar flecha/carga e executar a reação com requisitos e custos próprios, sem recuperação extra de cancelamento do tiro.
- Reação rejeitada não concede deslocamento, guarda, invulnerabilidade ou custo de tiro. Preservar o preparo somente se o personagem ainda satisfizer as condições dessa preparação; não usar essa regra para ignorar controle, perda da arma ou incompatibilidade de estado.
- Soltar o ataque depois de uma reação rejeitada ainda segue E/F: revalidar, emitir se possível e pagar uma vez. Não disparar automaticamente só porque a reação falhou.
- Guardar/perder acesso à arma, morte, desconexão, troca de dimensão ou interrupção incompatível eliminam o preparo. Não salvar carga para login, respawn ou retorno à arma.
- Limpar preparo antes da emissão não cobra o tiro, conforme E. Não remove conhecimento, Proficiência ou Masteries pessoais já conquistados.
- Essa limpeza não apaga custo, projétil ou recuperação de um disparo já realizado. Continuidade de projéteis emitidos e de tempos após morte/logout/troca de dimensão depende de M06-11/M16; não tratar como reset aprovado.
- Soltura e reação concorrentes precisam ter uma ordem única reconhecida pelo servidor. M06-11/M14/M16 detalham prioridade e comunicação; não autorizar uma mesma flecha a ser simultaneamente cancelada e emitida.

**Limite do cancelamento voluntário:** soltar antes do mínimo segue F; soltar após o mínimo dispara se válido. H não inventa um botão dedicado para descartar uma flecha pronta. M14 resolverá a apresentação/comando de guardar a arma e os conflitos com inventário/construção.

#### M06-09I — DECIDIDO — Reagir após emitir, preservando a recuperação ofensiva

**Aprovado em 20/10/2026.** Resposta explícita: “Permitir reação mantendo recuperação ofensiva (recomendado)”. A pergunta incluiu movimento, bloqueio, Dodge e Hirenkyaku disponíveis após emitir sem espera inicial adicional, preservando a recuperação do tiro e os impedimentos da reação atual antes do próximo ataque. A alternativa não escolhida permanece como histórico; números e integrações delegadas continuam pendentes.

**Regra aprovada:** depois que a flecha for emitida, permitir movimento comum, mira, bloqueio, Dodge e Hirenkyaku legitimamente disponíveis, mesmo antes de terminar a recuperação do tiro. Continuar impedindo iniciar outro tiro ou técnica ofensiva enquanto durar essa recuperação. As ações de reação mantêm restrições próprias.

| Alternativa | Experiência | Avaliação |
|---|---|---|
| **Permitir reação sem apagar recuperação — escolhida** | O personagem pode evitar uma ameaça após atirar; o próximo ataque mantém sua cadência | Exige distinguir permissão defensiva de liberação ofensiva |
| Esperar uma janela posterior para reagir | Depois de emitir, existe um trecho inicial em que ainda não pode bloquear/esquivar/usar mobilidade | Aumenta o compromisso por tiro e pode deixar o arco fundamental mais rígido; exige comunicar/calibrar essa janela |

**Contrato da decisão e requisitos de integração:**

- A permissão começa depois da emissão aceita, com flecha e pagamento já realizados. Não há uma janela adicional obrigatória do tiro básico antes de poder pedir essas reações.
- Bloqueio conserva suas condições de guarda/Postura; Dodge e Hirenkyaku conservam aprendizado, Stamina, trajetória e demais condições próprias. Arco formado não concede defesa perfeita, técnica não aprendida ou proteção automática.
- Movimento comum deixa a limitação específica de preparar/mirar o arco; velocidade e ações efetivas continuam sujeitas a guarda, controle e à reação atual. Não conceder corrida durante toda ação por omissão.
- Reagir não reduz, reinicia nem elimina o tempo restante da recuperação do tiro. O tempo do tiro continua decorrendo durante a reação; não fica guardado para ser pago novamente depois dela.
- Outro tiro ou técnica ofensiva só começa quando a recuperação ofensiva do tiro terminou **e** a ação atual permite atacar. Se a reação ainda estiver em execução/recuperação impeditiva, esperar por ela também. Não somar automaticamente todos os tempos em sequência nem usar a reação para zerá-los.
- Não permitir preparar antecipadamente outra flecha durante a recuperação ofensiva do tiro. Esta decisão não aprova fila automática de disparos, rajada ou ataque simultâneo com guarda/mobilidade.
- Uma reação indisponível após emitir falha sem restaurar Energia ou alterar a recuperação. H e as regras da ação atual continuam aplicáveis.
- A permissão é específica do tiro fundamental Quincy, conforme M06-06I. Outras técnicas, poderes principais e o cancelamento de Cero mantêm suas próprias fases; não criar liberação defensiva universal para todas as emissões.

**Exemplo sem números:** flecha emitida → Dodge aceito → recuperação do tiro continua enquanto o jogador se desloca. Terminou o tempo do tiro, mas Dodge ainda não permite atacar: esperar. Terminou Dodge, mas ainda resta tempo do tiro: esperar. Quando ambos permitem, começar outra preparação do zero.

### 19. Revisão documental, integrações e próximos blocos — 20/10/2026

| Situação futura de aceitação | Resultado esperado pelos contratos aprovados |
|---|---|
| Preparar e tentar Dodge sem Stamina | Sem Dodge; feedback; manter preparo se válido, sem custo de tiro |
| Preparar e aceitar reação válida | Descartar carga; pagar somente a reação; próximo preparo começa do zero |
| Perder estado/arma durante preparo | Limpar carga sem disparo, custo de tiro ou perda de conquistas |
| Emitir e bloquear/esquivar em seguida | Custo e flecha permanecem; reação executa suas regras; recuperação ofensiva não é apagada |
| Recuperação do tiro termina antes da reação | Novo ataque espera a ação atual permitir |
| Reação termina antes da recuperação do tiro | Novo ataque espera o tempo restante do tiro |
| Alternar guardar/formar arma após emitir | Não encurta recuperação nem devolve Energia; vínculo/inventário não transfere preparo |
| Pedidos concorrentes ou repetidos | Ordem única no servidor, sem emissão e cancelamento da mesma flecha, sem cobrança dupla |

**Resultado da revisão documental após as respostas:** H diferencia pedido de reação e reação realizada, preservando D; I concretiza a permissão por fase prevista em M06-06I e preserva a cadência de M06-05R. A permissão específica do tiro básico deixa iniciar as reações indicadas durante sua recuperação ofensiva, mas não libera outro ataque; não altera a regra de todas as técnicas. Não foi identificado conflito que exija rever A–G. Não há números calibrados ou evidência de responsividade em jogo.

#### Fronteira histórica após H/I: manifestação, foco e inventário

Este encaminhamento define responsabilidades; não escolhe silenciosamente um equipamento ou custo novo.

| Responsável | Pendência concreta antes da ficha executável |
|---|---|
| M06-09 / M11 | Definir se o arco básico exige foco físico; o que manifesta/guarda a arma; custo de formação inicial, se houver; mãos/arma disponíveis e troca de equipamento |
| M11 / M06-11 | Vínculo com personagem, posse, empréstimo, perda, drop, baú e morte; um item não transfere conhecimento ou domínio |
| M06-09 / M05 / M14 | Visual e comandos com Energia insuficiente; não confundir arco formado sem drenagem com disparo gratuito |
| M06-11 / M15 | Alvos elegíveis, aliados/pets, escudos, armaduras e interação com outros ataques |
| M06-11 / M10 / M16 | Água e outros meios, portais, origem junto de paredes, limites carregados e ciclo de vida de flechas emitidas |
| M14 / M15 / M16 | Feedback de preparo/falha, recuperação, latência e calibração de duração, dano, custo, velocidade, alcance e colisão |

**Encaminhamento histórico após H/I:** manifestação/foco foram decididos depois em J/K/L, seções 20–21. A seção 22 reúne a consolidação estrutural do arco, as pendências delegadas e a continuidade para Hirenkyaku/Blut. O arco e o bloco Quincy ainda não estão completamente especificados para implementação.

**Síntese de execução após A–I:** preparar permite caminhar lentamente e mirar; reação aceita descarta carga, reação rejeitada preserva preparo válido. Soltar antes do mínimo cancela; depois dele emite uma flecha, pagando uma vez. Após emissão, as reações disponíveis são permitidas e o tempo da recuperação ofensiva continua. Outra preparação/ataque só começa quando esse tempo terminou e a ação atual permite. Guardar a arma ou perder o estado durante preparo limpa a carga; não devolve custo ou apaga recuperação de tiro já emitido. Esta síntese não define foco físico, custo de manifestação, alvos ou regras de outras técnicas.


### 20. Manifestação, foco e mãos — 20/10/2026

**Estado:** M06-09J/K/L `DECIDIDOS` por três respostas explícitas em 20/10/2026; A–I preservados. Objetivo: definir como o arco básico entra e sai do combate e sua relação com equipamento, sem especificar todo o catálogo/economia de M11.

#### Referência e limite canônico

Consultado em 20/10/2026 o [glossário oficial Pierrot, grupo ka](https://pierrot.jp/title/bleach/keyword_ka.html), entrada `滅却十字 / クインシー・クロス`: identifica uma cruz especial utilizada pelos Quincy. A página informa que seu conteúdo corresponde à época de exibição. A entrada de `銀嶺弧雀` identifica uma arma específica de Uryū, sem concedê-la ao jogador.

A referência confirma o objeto, mas o resumo não estabelece obrigatoriedade universal, durabilidade, slot, vínculo, custo de manifestação ou uso de mãos para todo armamento Quincy. J/K/L são adaptações aprovadas para o **arco inicial**. Não generalizar para armas/poderes posteriores nem alegar leitura de capítulos ou episódios integrais.

**Herança:** Q1/Q2/Q6 de M04; Reishi e recursos de M05; M06-01B–F preserva Combat Mode manual, hotbar de técnicas e inventário real; A–I estabelece custo de tiro, preparo e permissões. Não exigir que o jogador saia da Combat Hotbar para selecionar um item a cada disparo.

#### M06-09J — DECIDIDO — Foco físico equipado para o arco inicial

**Aprovado em 20/10/2026.** Resposta explícita: “Foco físico equipado (recomendado)”. A pergunta incluiu foco físico equipado, Cruz Quincy como referência, aprendizado da formação separado da posse e foco não consumido por disparo; slot, obtenção e reposição permanecem para Itens. Alternativas não escolhidas ficam como histórico; números, conteúdo e integrações delegadas não são aprovados por associação.

**Regra aprovada:** exigir um foco físico Quincy equipado como instrumento do arco básico, com a Cruz Quincy como referência inicial. O conhecimento para manifestar pertence ao personagem; possuir o item não ensina nem transfere Proficiência/Mastery.

| Alternativa | Experiência | Avaliação |
|---|---|---|
| **Foco físico equipado — escolhida** | Descoberta e formação racial têm um instrumento concreto; equipamento e conhecimento têm funções distintas | Exige acesso inicial e reposição apropriados; slot e regras de perda precisam ser especificados |
| Formação sem foco obrigatório | Depois de aprender, o personagem manifesta o arco independentemente de um objeto | Simplifica recuperação e equipamento, mas reduz o papel material do instrumento na primeira arma |

**Contrato da decisão e fronteiras:**

- Aprender a formação básica e dispor de foco compatível equipado são condições diferentes. Nem empréstimo nem fabricação do item concedem domínio ou poder principal.
- O foco é item real; o arco de Reishi é manifestação temporária. Dissipar o arco não cria outro item, não consome um foco por flecha e não duplica equipamentos.
- Equipamento registrado para combate dispensa trocar manualmente de slot vanilla para cada manifestação/tiro. Não aprovar um acessório externo, biblioteca, slot novo ou rearranjo automático de inventário; a forma concreta de equipar pertence a M11/M14/M06-11.
- Guardar a manifestação pode manter o foco equipado. Retirar/perder o foco impede a manifestação básica e limpa preparo conforme H, preservando conquistas e tempos de um tiro já emitido.
- A exigência pertence ao arco básico. Não bloquear Hirenkyaku, Blut ou toda capacidade racial pela ausência do foco sem que sua própria ficha exija esse instrumento.
- M08/M11/M12/M13 devem garantir obtenção coerente com o primeiro aprendizado e uma via de reposição acessível sem precisar usar o arco perdido. Não aprovar aqui item indestrutível, keepInventory, vínculo intransferível ou recuperação automática.
- Catálogo, receita, preço, durabilidade, posse, drop e reposição concreta continuam em M11. A existência de foco não autoriza munição convencional obrigatória ou uma segunda barra de Reishi.

#### M06-09K — DECIDIDO — Manifestação sem custo adicional; Energia continua cobrada no tiro

**Aprovado em 20/10/2026.** Resposta explícita: “Sem custo adicional de manifestação (recomendado)”. A pergunta incluiu formar/guardar sem Energia adicional, manifestação visível mesmo sem recurso suficiente para tiro, recurso exigido para preparar/disparar e alternância sem recuperar recursos ou apagar tempos. Alternativas não escolhidas ficam como histórico; números, conteúdo e integrações delegadas não são aprovados por associação.

**Regra aprovada:** formar e dissipar o arco básico não cobram Energia adicional. Manter a manifestação continua sem drenagem, conforme A. Energia insuficiente pode deixar o arco visível, inclusive ao manifestá-lo, mas não permite preparar/emitir um disparo dependente do recurso.

| Alternativa | Experiência | Avaliação |
|---|---|---|
| **Sem custo de manifestação — escolhida** | Guardar para construir ou reagir não acrescenta outra cobrança ao uso frequente da arma principal | Exige preservar os tempos de combate ao alternar e comunicar claramente falta de Energia |
| Custo ao manifestar | Preparar o equipamento espiritual também compromete reserva | Pode desencorajar guardar o arco e cobrar repetidamente a transição entre construção e combate |

**Contrato da decisão e requisitos de integração:**

- A ausência de custo refere-se a formar/guardar o arco básico; disparos continuam sob A/E/F e as demais capacidades conservam custos próprios.
- Foco e aprendizado legítimos continuam necessários em J. Energia zero não concede disparo, proteção automática ou recuperação extra de recursos; o indicador deve mostrar por que o tiro não está disponível.
- Formar/guardar não restaura Energia, Stamina, Postura ou cooldowns e não reduz a recuperação ofensiva do tiro. Não contar essas alternâncias como prática de armamento por si só.
- Não criar outra carga de formação paralela ao preparo da flecha. A transição deve ser breve e legível, com duração/animação a especificar; controles, recuperação e estado vigente continuam valendo. Não aprovar alternância instantânea para escapar de qualquer execução.
- Energia perdida durante um preparo já iniciado segue E: revalidar na soltura, sem emitir se insuficiente. Não confundir manifestação visível com flecha pronta ou autorização garantida para atirar.

#### M06-09L — DECIDIDO — Manifestação manual e arco de duas mãos

**Aprovado em 20/10/2026.** Resposta explícita: “Manifestação manual com duas mãos (recomendado)”. A pergunta incluiu manifestação manual separada de entrar no modo de combate, duas mãos sem uso simultâneo de outro item, bloqueio/reação disponíveis e dissipar arco/preparo ao sair do modo sem mover itens ou apagar recuperação. Alternativas não escolhidas ficam como histórico; números, conteúdo e integrações delegadas não são aprovados por associação.

**Regra aprovada:** usar uma ação manual de formar/dissipar, integrada ao comando de armamento. Combat Mode e manifestação continuam estados separados; entrar no modo de combate não forma nem dispara o arco automaticamente. O arco básico usa as duas mãos enquanto está manifestado, sem uso simultâneo de outro item nessas mãos.

| Alternativa | Experiência | Avaliação |
|---|---|---|
| **Manifestar manualmente; duas mãos — escolhida** | Decide quando usa o arco e quando retorna a ferramentas/outros itens; combate corporal e técnicas compatíveis continuam possíveis | M14 deve tornar a transição simples e legível |
| Formar automaticamente ao atacar, ainda com duas mãos | Reduz uma ação inicial; primeiro ataque solicita manifestação e preparo | Precisa distinguir tentativa de formar de tentativa de disparar e resolver conflitos de input |

A alternativa compara a entrada no estado; esta rodada não propõe arco básico de uma mão.

**Contrato da decisão e requisitos de integração:**

- Usar duas mãos é ocupação funcional, sem mover/apagar o item real da mão secundária ou substituí-lo por cópia. Não usar simultaneamente escudo vanilla, comida, bloco ou outra arma enquanto o arco ocupa as mãos.
- O bloqueio fundamental universal e as reações legitimamente disponíveis continuam possíveis conforme D/I. Arco de duas mãos não remove o bloqueio nem garante defesa equivalente à de um escudo; eficiência e integração ficam nas fichas pertinentes.
- Técnica corporal compatível pode continuar possível. Outra técnica que precise das mãos declara sua compatibilidade/transição própria; esta decisão não dá uso irrestrito de Gintō enquanto segura o arco.
- Dissipar permite voltar ao uso de itens quando a ação atual e o contexto permitirem; limpa flecha preparada conforme H, sem preservar carga nem encurtar recuperação de tiro já emitido.
- Sair de Combat Mode dissipa a manifestação e limpa preparo, preservando inventário, conhecimento e tempos vigentes. Não retira o foco do equipamento nem limpa o Combat State sistêmico.
- M14 define tecla/remapeamento e comunicação, sem consumir um dos nove slots de técnicas para a ação fundamental de formar/guardar. Não aprovar nova tecla obrigatória nesta rodada.

### 21. Revisão documental e continuidade de J/K/L — 20/10/2026

| Caso futuro de aceitação | Resultado esperado pelos contratos aprovados |
|---|---|
| Receber foco sem conhecer formação | Posse não concede arco utilizável, Proficiência ou poder principal |
| Aprender formação sem foco equipado | Explicar instrumento ausente; não inventar arco/munição no inventário |
| Manifestar com Energia insuficiente | Pode exibir o arco; tiro permanece indisponível; sem carga fictícia |
| Entrar em Combat Mode | Não deslocar inventário real nem formar/disparar automaticamente |
| Guardar arco e voltar a ferramenta | Transição explícita, sem cobrança de manifestação e respeitando ações vigentes |
| Arco manifestado e item na mão secundária | Item permanece armazenado; uso simultâneo é bloqueado enquanto as mãos estiverem ocupadas |
| Reagir após emitir | I permanece válido, com custos da reação e recuperação ofensiva preservados |
| Perder foco ou sair do modo durante preparo | Limpar carga; nenhuma flecha guardada, custo de tiro ou perda de conquistas |
| Trocar foco/formar repetidamente | Sem duplicação, ganho por simples alternância ou reset de recursos/recuperação |

**Responsabilidades:** M06 possui manifestação, preparo e compatibilidade de mãos; M11 concretiza instrumento/equipamento e regras de perda/reposição; M14 define comandos e feedback; M06-11/M16 define autoridade, ciclo de vida e integração sem troca artificial de inventário. A revisão não fixa APIs nem prova implementação.

**Estado histórico após J/K/L:** A–L decididos. Estrutura do arco consolidada na seção 22, com bloqueadores de ficha e parâmetros delegados. Próximo: Hirenkyaku; depois Blut. Formar o arco não equivale a uma transformação de M07 nem concede armas canônicas avançadas. Sem alteração de código, build, save ou teste de gameplay.


### 22. Consolidação estrutural do arco Quincy — 20/10/2026

**Resultado:** a estrutura do arco básico pode avançar como decidida, conforme A/B/D–L. C organiza a formação Quincy e classifica o tiro fundamental sem Mastery própria. Todas essas escolhas possuem respostas explícitas; esta consolidação não aprova conteúdo novo. M06-09 e o módulo M06 continuam `EM ESPECIFICAÇÃO`.

#### Ciclo consolidado

| Momento | Regra vigente |
|---|---|
| Aprender e equipar | Formação pertence ao personagem; foco físico compatível equipado fornece o instrumento. Item não transfere conhecimento/Proficiência |
| Formar/guardar | Ação manual, sem custo adicional de Energia; manifestação ocupa duas mãos sem mover inventário real |
| Sem Energia suficiente | Arco pode continuar visível/ser manifestado; preparar/emitir tiro permanece indisponível |
| Preparar | Segurar comando, caminhar devagar e ajustar mira; mínimo curto e máximo limitado |
| Soltar | Antes do mínimo, cancelar; depois, emitir com dano conforme preparo, mesmo custo e velocidade nas mesmas condições |
| Pagar | Uma cobrança na emissão aceita; cancelar antes não paga tiro; errar/colidir depois não devolve custo |
| Trajetória | Linha reta com tempo de viagem, sem perseguição/queda; primeiro impacto válido encerra; não destrói blocos nem gera munição coletável |
| Reagir durante preparo | Reação aceita descarta carga; pedido rejeitado preserva preparo ainda válido; requisitos/custos da reação permanecem |
| Reagir após emitir | Movimento, bloqueio, Dodge e Hirenkyaku disponíveis são permitidos; recuperação ofensiva do tiro continua contando |
| Voltar a atacar | Começar novo preparo/ataque somente após a recuperação ofensiva e quando a ação atual permitir; não retomar carga perdida |
| Perder estado, foco ou sair do modo | Limpar preparo; sair do modo dissipa manifestação; preservar conquistas, inventário e tempos vigentes. Não desfazer tiro já emitido |

**Revisão documental:** não foi identificado conflito que exija rever A–I. O foco acrescenta requisito de equipamento sem reintroduzir munição física; K preserva cobrança por emissão; L preserva reações de D/I e os itens reais de M06-01E. Não extrapolar a exigência do foco para toda capacidade Quincy nem as permissões do arco para todas as técnicas. A política de Cero permanece própria.

#### Pendências e responsáveis antes de uma ficha executável

| Responsável | Trabalho pendente | Efeito na maturidade |
|---|---|---|
| M06 / M05 / M15 | Tempos mínimo/máximo e de recuperação, curva de dano, custo/eficiência, velocidade, alcance e colisão | Exige especificação/calibração; nenhum valor foi inventado nesta consolidação |
| M11 / M14 / M06-11 | Equipar foco, acesso inicial, posse, durabilidade, perda e reposição; comandos de manifestação e compatibilidade exata de mãos | Bloqueia ficha completa de equipamento/integração; não exige fechar toda economia agora |
| M06-11 / M15 | Alvos, aliados/pets, armadura, escudos e colisão com outros ataques | Bloqueia contrato completo de impacto/defesa |
| M06-11 / M10 / M16 | Água/outros meios, portais, limites carregados, concorrência e comportamento de projéteis/tempos já emitidos no ciclo de vida | Bloqueia execução completa em mundo persistente/multiplayer |
| M14 / M06-11 | Feedback de fases, recurso e falha; prioridade de comandos; alcance da manifestação fora do modo de combate | Concretiza controles sem reabrir entrada manual e dissipação ao sair do modo |
| M08 / M11 / M12 / M13 | Ensino inicial e obtenção/reposição do foco com contexto apropriado | Conteúdo ainda não escolhido; evitar requisito circular que dependa da arma perdida |
| M16 | Contratos de dados, integração do runtime, migração e evidência de validação futura | Não existe aceite de gameplay por concluir a documentação |

**Encaminhamento após consolidar o arco:** Hirenkyaku. Estudar acesso/aprendizado, direção e execução do deslocamento, custos e limites de percurso, relação com ar/terreno e campos/Mastery. M05 já fornece Stamina como recurso primário da mobilidade racial; C já classifica a técnica; D/I só definem suas permissões em relação ao arco. Nenhum desses contratos escolheu automaticamente direção, distância, voo ou aquisição de Hirenkyaku. Depois, Blut e repertórios conforme a ordem do bloco.

Fichas de linhagem e artes continuam adiadas. Esta atualização não altera código, build, dependências ou save e não executa teste de gameplay.


### 23. Hirenkyaku — base, alternativas e escopo — 20/10/2026

**Estado:** M06-09M/N/O `DECIDIDOS` por três respostas explícitas em 20/10/2026, registradas abaixo. A–L permanecem decididos. As propostas iniciais e alternativas ficam como histórico. Ar/terreno foi decidido depois em P; números e integrações continuam delegados. A aprovação estrutural não declara a técnica inteiramente especificada ou implementada.

**Experiência pretendida:** aprender a se reposicionar rapidamente como Quincy, tanto para lutar quanto para explorar. O arco pode continuar manifestado durante o movimento, mas sua carga não é transportada: D já exige descartá-la quando Hirenkyaku é aceito. Uma flecha já emitida continua seu percurso e sua recuperação continua contando, conforme I.

#### Referência conferida e limite

Reconsultadas em **20/10/2026** as entradas `飛簾脚` do [glossário Pierrot](https://pierrot.jp/title/bleach/keyword_ha.html) e `飛廉脚` do [glossário oficial TYBW](https://bleach-anime.com/keyword/). Ambas descrevem deslocamento rápido sobre fluxo de Reishi nos pés. A fonte TYBW o classifica como uma arte avançada de movimentação; isso orienta tratá-lo como aprendizado próprio, sem impor um rank numérico do mod. O arquivo Pierrot declara seu recorte de exibição; o site TYBW é mutável.

São resumos oficiais do anime, interpretados do japonês, não capítulos ou episódios integrais. Eles não definem tecla, duração, consumo, invulnerabilidade ou currículo. A comparação de TYBW com outras mobilidades não exige comportamento idêntico entre raças. O dispositivo de Mayuri citado nessa fonte é um exemplo específico, sem conceder equipamento ou acesso universal ao jogador.

#### Herança que não será votada novamente

| Contrato existente | Aplicação |
|---|---|
| M05, seções 18–20 e 25 | Stamina é recurso primário; ausência de recurso impede a mobilidade, sem penalidade longa adicional automática. M06 define cobrança individual |
| M06-09C | Campo de mobilidade com Proficiência e Hirenkyaku com Mastery própria; tiro comum não ensina a técnica |
| M06-03/07 e M06-09, seção 9 | Conhecimento, aquisição e domínio separados; ensino e treino autônomo existem, sem um NPC para cada melhoria |
| M06-06F/G | Hirenkyaku é aprendido, distinto do Dodge e utilizável dentro e fora de combate quando disponível |
| M06-09D/H/I | Pedido válido durante preparo descarta carga; pedido rejeitado preserva preparo ainda válido; após emitir, mobilidade pode reagir sem apagar recuperação ofensiva |
| M06-09J/L | Foco e duas mãos pertencem ao arco básico; não são exigências universais da mobilidade Quincy |

**Comparação de execução:** o impulso curto oferece um reposicionamento delimitado e leitura clara de início/fim. Um deslocamento mantido permite ajustar a rota durante mais tempo e pode servir à travessia, mas exige regras de consumo contínuo, direção e ações simultâneas. Selecionar destino pela mira facilita alcançar um ponto, mas exige validar todo o trajeto e pode disputar a mira do combate. Nenhuma dessas alternativas é exigida pelo resumo canônico.

**Recomendação aprovada em N:** impulso curto direcional. A precisão em combate e em espaços construídos justifica a escolha; não é necessário inventar uma duração contínua apenas para diferenciar os nomes raciais. Sua apresentação deve comunicar o fluxo de Reishi. Valores e sensação de controle ainda precisam de prototipagem futura.

### 24. M06-09M — DECIDIDO — Aprendizado próprio de mobilidade

**Aprovado em 20/10/2026.** Resposta explícita: “Aprendizado próprio de Hirenkyaku (recomendado)”. A pergunta incluiu aprendizado separado após os fundamentos iniciais de Reishi, orientação ou registro apropriado e prática de movimentação, sem exigir Wandenreich ou Schrift. O primeiro arco não concede a técnica automaticamente. Conteúdo e parâmetros delegados não são aprovados por associação.

**Regra aprovada:** aprender Hirenkyaku separadamente do arco, após os fundamentos iniciais de Reishi, com orientação ou registro apropriado e prática de deslocamento. Formar o primeiro arco não concede automaticamente a técnica.

| Alternativa | Consequência |
|---|---|
| **Aprendizado próprio — escolhida** | Dá função ao ensino de movimentação e distingue aprender a formar uma arma de conduzir o corpo |
| Concessão junto ao primeiro arco | Acelera o acesso à mobilidade, mas reúne duas aprendizagens distintas no mesmo marco |

**Contrato da decisão e requisitos de integração:** uma fonte legítima abre o método e a prática apropriada conclui a aquisição. A tentativa inicial usa fundamentos conhecidos ou contexto assistido; não exigir Mastery de Hirenkyaku ainda bloqueado. Depois de adquirido, uso pertinente desenvolve sua Mastery e a Proficiência de mobilidade. Não exigir Schrift, Wandenreich, Gintō ou retorno ao mestre para cada melhoria. Posse da Cruz Quincy e Proficiência alta de armamento, isoladamente, não substituem o aprendizado.

Mentores já são permitidos; M não vota novamente sua existência. Nome, localização, exercício e disponibilidade por era pertencem a M08/M12/M13. Esta decisão não exige uma quest longa exclusiva nem escolhe um personagem como professor.

### 25. M06-09N — DECIDIDO — Impulso curto direcional com Stamina

**Aprovado em 20/10/2026.** Resposta explícita: “Impulso curto direcional (recomendado)”. A pergunta incluiu comando dedicado, Stamina cobrada uma vez ao iniciar, colisão em todo o percurso e uso dentro e fora de combate, sem arco/foco ou Energia adicional. Ar e verticalidade ficaram expressamente para depois. Conteúdo e parâmetros delegados não são aprovados por associação.

**Regra aprovada:** comando dedicado de mobilidade mais direção escolhida pelo jogador produz um impulso curto, com percurso limitado e colisão. Usa Stamina, dentro e fora de combate. Não seleciona destino pela mira e manter a tecla pressionada não prolonga automaticamente a execução.

**Contrato da decisão e requisitos de integração:**

- Direção parte da entrada de movimento relativa à orientação do personagem; sem direção informada, usar a frente. Tecla concreta e remapeamento ficam em M14. Não ocupar obrigatoriamente um dos nove slots de técnicas.
- Validar disponibilidade, estado e recurso antes de aceitar; cobrar Stamina uma vez ao iniciar o impulso aceito. Pedido rejeitado não cobra a técnica. Não acrescentar Energia apenas por manipular Reishi; ausência de Energia, sozinha, não bloqueia esta mobilidade.
- Arco manifestado ou foco equipado não são necessários. Guardar/perder o arco não apaga conhecimento de Hirenkyaku. Outras restrições de estado continuam válidas.
- Respeitar colisão ao longo de todo o percurso, incluindo o volume do personagem. Parede limita o movimento; não atravessar blocos, destruir terreno ou escolher somente um ponto final livre do outro lado de um obstáculo. Colisão após começar não devolve automaticamente o custo.
- Não conceder invulnerabilidade, invisibilidade mecânica, dano de contato ou teleporte. Ser atingido segue as regras de dano/controle e interrupção existentes; não criar Super Armor por omissão.
- Repetir exige nova execução válida, recurso e tempos próprios. Não obter execuções automáticas infinitas ao segurar a tecla. Distância, velocidade, recuperação e eventual cooldown precisam de ficha/calibração, sem criar trava global de toda mobilidade.

**Fronteira aberta após N, resolvida parcialmente depois em P:** início no ar, deslocamento vertical, quedas, água e sustentação ainda não haviam sido escolhidos. P, seção 28, define impulso horizontal em chão/ar/água com gravidade e riscos preservados; sustentação e formas específicas continuam para seus próprios contratos. N não concede voo nem impõe uma proibição canônica de movimento aéreo. M06/M07/M10 e M06-11 devem fechar esse contrato antes de uma ficha executável; não aceitar encadeamento aéreo ilimitado por ausência de regra. Esta rodada define a execução básica, sem resolver esses meios por omissão.

**Alternativas não escolhidas:** deslocamento mantido consumiria Stamina ao longo da atividade e exigiria outro contrato de parada/ações; destino pela mira exigiria direção e seleção próprias. Não há compromisso de implementar todos os modelos como opções do jogador.

### 26. M06-09O — DECIDIDO — Encerrar o impulso para atacar ou defender

**Aprovado em 20/10/2026.** Resposta explícita: “Parar para agir, permitindo interrupção (recomendado)”. A pergunta incluiu interrupção por ataque, bloqueio ou Dodge disponível, sem recuperação extra pela parada; custo já pago e recuperação de tiros anteriores permanecem. Pedido indisponível mantém o impulso ainda válido. Conteúdo e parâmetros delegados não são aprovados por associação.

**Regra aprovada:** durante o impulso básico, não iniciar ataque, preparo de flecha, bloqueio ou Dodge simultâneos. Uma dessas ações, quando válida, pode interromper o deslocamento para começar, sem recuperação adicional apenas por essa parada. O custo de Hirenkyaku já iniciado permanece pago; tempos anteriores continuam valendo.

| Alternativa | Consequência |
|---|---|
| **Parar para agir, com interrupção voluntária — escolhida** | O jogador escolhe quanto reposicionamento aproveitar antes de reagir; não combina velocidade máxima com tiro/guarda simultâneos |
| Completar o impulso antes de agir | Compromisso maior com a direção, mas menor capacidade de corrigir uma decisão diante de ameaça |
| Permitir ações simultâneas | Facilita disparar/defender em alta velocidade; exige equilibrar mira, preparação e proteção durante o trajeto |

**Contrato da decisão e encontro com o arco:**

- A manifestação do arco pode permanecer, sem preservar carga descartada. Pedir um novo tiro válido interrompe Hirenkyaku e começa o preparo do zero, depois de terminar o deslocamento especial. Volta a valer a caminhada lenta do preparo.
- Se a recuperação de uma flecha anterior ainda proíbe atacar, pedir outro tiro é rejeitado; o impulso ainda válido continua. Não iniciar preparo antecipado nem deixar um disparo agendado.
- Bloqueio ou Dodge disponíveis interrompem o impulso e passam a aplicar custos, condições e recuperação próprios. Pedido indisponível é rejeitado com aviso e não interrompe o impulso ainda válido. Colisão ou perda de validade continuam podendo encerrá-lo independentemente do pedido.
- A interrupção voluntária não devolve Stamina, não apaga cooldowns e não encurta a recuperação ofensiva de tiro já emitido. Não acrescenta uma recuperação de cancelamento; as restrições próprias da ação que começa continuam válidas.
- Esta permissão pertence ao Hirenkyaku básico. Não libera cancelamento universal de técnicas, nem altera a recuperação de cancelamento de Cero. Técnicas contextuais futuras precisam de ficha própria.

### 27. Revisão documental, responsabilidades e continuidade — 20/10/2026

| Caso futuro de aceitação | Resultado esperado pelos contratos aprovados |
|---|---|
| Formar primeiro arco | Não concede Hirenkyaku automaticamente; aprendizado próprio disponível sem requisito circular |
| Tentar Hirenkyaku sem aprendizado ou Stamina | Não deslocar nem cobrar execução rejeitada; preparo de arco ainda válido permanece, conforme H |
| Usar com arco guardado, sem foco ou com Energia zero | Não rejeitar por esses motivos isolados; aprendizado, Stamina e estado precisam ser válidos |
| Aceitar durante preparo do arco | Limpar carga, cobrar mobilidade uma vez e deslocar; sem custo de flecha |
| Usar após uma flecha | Preservar projétil e recuperação ofensiva; tempo continua durante movimento |
| Pedir tiro válido durante impulso | Terminar deslocamento especial e começar novo preparo, sem preservar carga antiga |
| Pedir tiro ainda bloqueado pela recuperação | Rejeitar sem fila automática; manter impulso ainda válido |
| Encontrar parede ou corredor estreito | Validar percurso/volume; parar antes da obstrução sem romper terreno ou atravessar parede |
| Morrer, desconectar ou trocar de dimensão | Encerrar execução transitória sem perder conhecimento/Proficiência/Mastery; não retomar deslocamento automaticamente no retorno |

Os casos são critérios futuros de aceitação, não resultados observados. Servidor autoriza execução, custo e posição; cliente apresenta o movimento. M06-11/M16 concretizam concorrência, interrupção, ciclo de vida e sincronização com observadores, preservando custos/tempos legitimamente realizados.

**Pendências com responsáveis:** M06/M05/M15 definem valores de Stamina, deslocamento, fases, tempos e contribuições de atributos/Proficiência/Mastery; M06/M07/M10/M06-11 resolvem ar, desníveis, quedas, água, portais e limite de região carregada; M14 resolve comandos, prioridade e feedback; M08/M12/M13 concretizam ensino; M16 traduz os contratos para dados/runtime e validação futura. Não foi escolhida biblioteca de mobilidade ou implementação de rede.

**Resultado da revisão:** não foi identificado conflito que exija rever A–L. M preserva aprendizado e Proficiência/Mastery distintos; N aplica Stamina sem ampliar o requisito de foco do arco; O preserva descarte de carga e recuperação ofensiva anterior. Parar para agir não cobra novamente Hirenkyaku nem acrescenta espera de cancelamento. Futura calibração de recuperação/cooldown não pode anular essa permissão; os tempos de reutilização e as restrições da ação seguinte continuam próprios. Esta revisão é documental, sem medição de responsividade ou equilíbrio.

**Continuidade histórica após M/N/O:** alcance de terreno/ar e Blut foram trabalhados conjuntamente depois em P/Q/R, aprovados nas seções 28–32; consultar a consolidação atual na seção 33. Parâmetros e conteúdo com responsáveis podem permanecer delegados. M06-09 segue em especificação; nenhuma mudança de gameplay, build, dependências ou save, nem teste em jogo nesta rodada.


### 28. M06-09P — DECIDIDO — Hirenkyaku em terreno, ar e água — 20/10/2026

**Aprovado em 20/10/2026.** Resposta explícita: “Horizontal no chão, ar e água (recomendado)”. A pergunta incluiu gravidade, queda acumulada, colisões e riscos ambientais preservados; sem subir pela mira, criar piso sobre água, conceder voo ou respiração. A versão restrita ao chão não foi escolhida. Parâmetros, conteúdo e integrações delegados não foram fechados por associação.

**Pedido desta rodada:** definir as partes restantes e avançar para Blut sem uma interrupção entre os assuntos. P/Q/R foram apresentados juntos como propostas e depois aprovados por três respostas explícitas, registradas aqui. A–O permanecem decididos; alternativas ficam como histórico.

**Experiência aprovada:** o mesmo impulso curto pode reposicionar no chão, durante um salto/queda e na água. Sua direção especial é horizontal; gravidade e riscos do ambiente continuam. Não é preciso reaprender uma versão aquática ou aérea só para executar esse mesmo fundamento. Voo e sustentação aérea são capacidades distintas e não estão sendo concedidos.

| Situação | Contrato da decisão e requisitos de integração |
|---|---|
| Direção e desnível | Entrada de movimento relativa à orientação horizontal do personagem; inclinar a câmera para cima não produz impulso ascendente. No chão, degraus/desníveis só são vencidos dentro da capacidade de passagem normal do corpo; parede não vira escada |
| Salto ou queda | Permitir impulso horizontal com Stamina e tempos válidos. Gravidade e velocidade vertical continuam durante a execução; encerrar/reativar não zera queda acumulada nem concede pouso seguro |
| Repetição no ar | Cada impulso exige nova solicitação válida, custo e tempos próprios. Não suspender gravidade entre usos; não acrescentar contador de saltos aéreos por omissão. Eventual sustentação já adquirida segue sua ficha, sem ser criada por Hirenkyaku |
| Borda de plataforma | Pode sair da borda; passa a cair. Não criar piso, ponte ou pouso automático. A possibilidade de alcançar uma abertura depende do percurso real |
| Água e submersão | Permitir o mesmo impulso horizontal, respeitando o meio, colisão e limites calibrados. Não criar plataforma sobre água, respiração, imunidade a afogamento ou saída vertical automática |
| Lava e outros perigos | Não conceder proteção contra fogo, lava ou dano ambiental. Entrar em um perigo conserva suas consequências; parâmetros por meio precisam de ficha, sem presumir desempenho idêntico em todos os fluidos |
| Corredor, teto e obstáculo | Verificar todo o volume do personagem ao longo do movimento; parar antes da obstrução. Não atravessar parede nem destruir bloco; parada não cria dano de colisão novo |
| Limite do mundo ou região não carregada | Limitar ao trecho válido e seguro disponível; não mover para área sem validação nem solicitar carregamento remoto arbitrário para completar o impulso |
| Portal e dimensão | Hirenkyaku não abre portal nem transporta entre mundos. A regra normal de travessia permanece; se ocorrer mudança de dimensão, encerrar o impulso, sem retomá-lo no destino |

**Herança:** N mantém cobrança única ao aceitar a execução e colisão sem reembolso automático depois de iniciar; O permite parar por ação disponível. P não cancela carga de arco de outra forma, não altera recuperação ofensiva e não acrescenta Energia ao custo. Falha antes da aceitação continua sem cobrança da técnica.

**Alternativas não escolhidas:** limitar ao chão simplifica o primeiro protótipo, mas impediria reposicionar durante saltos comuns. Permitir direção tridimensional com sustentação ampliaria combate e travessia, exigindo fechar voo, altitude, quedas e contrajogo agora. Foi escolhido movimento horizontal nos meios acima para completar a base sem antecipar o sistema aéreo completo. São adaptações do mod: as entradas oficiais de Hirenkyaku não determinam gravidade, fluidos ou limites de chunks.

**Limite de fechamento:** P decide as restrições estruturais de percurso descritas acima. M06/M05/M15 ainda precisam calibrar velocidade, alcance, custo e reutilização; M06-11/M10/M16 concretizam colisão/meios/chunks/portais; M07 trata sustentação e formas específicas. Não declarar a técnica pronta para implementação antes desses contratos executáveis.

### 29. Blut — função, evidência e aprendizado — 20/10/2026

**Estado:** Q/R `DECIDIDOS` após respostas explícitas; classificação em Masteries próprias de Vene e Arterie já está decidida em C. Não criar outra Proficiência, barra de sangue, requisito de pureza sorteado ou poder principal novo.

**Referência oficial reconsultada em 20/10/2026:** [glossário TYBW](https://bleach-anime.com/keyword/), entradas `血装` e `滅却師`. A descrição liga Reishi na circulação a reforço corporal ofensivo ou defensivo; as duas aplicações não operam simultaneamente, e treinamento pode desenvolver a capacidade. A página também menciona diferenças de nascimento; isso não aprova genética aleatória ou Blut gratuito no início deste personagem. Origem e formação do mod continuam regidas por M04.

A pesquisa leu os resumos oficiais acessíveis, com interpretação do japonês. Não houve leitura integral de capítulo/episódio nem confirmação exaustiva de exceções. A fonte não define consumo por segundo, teclas, tempo de troca ou uma lista universal de tipos de dano. Em especial, o resumo não resolve sozinho se toda flecha ou fórmula deve receber bônus de Arterie. A delimitação aprovada em R é adaptação de design, não uma proibição canônica demonstrada para todas as obras.

**Aprendizado aprovado, incluído em Q:** ensino de circulação apropriado e prática específica abrem as aplicações; não conceder ambas por formar o arco, elevar Proficiência de armamento ou adquirir uma Schrift. Um mesmo repertório pode apresentar as duas, mas aquisição/prática e Mastery permanecem identificadas por aplicação. O primeiro exercício não exige executar livremente um Blut ainda bloqueado. Fontes e currículo pertencem a M08/M12/M13; nenhum professor ou ordem narrativa obrigatória foi escolhido.

### 30. M06-09Q — DECIDIDO — Blut manual, exclusivo e mantido com Energia

**Aprovado em 20/10/2026.** Resposta explícita: “Seleção manual com manutenção de Energia (recomendado)”. A pergunta incluiu aprendizado específico, estados desligado/Vene/Arterie, manutenção com Energia sem taxa adicional para ligar/desligar, transição breve sem os dois reforços e desligamento ao sair do modo de combate. Vene permite ataques comuns e Arterie permite guarda comum. Aplicações breves por ativação não foram escolhidas. Parâmetros, conteúdo e integrações delegados não foram fechados por associação.

**Regra aprovada:** após o aprendizado correspondente, o jogador seleciona **desligado**, **Vene** ou **Arterie**. Apenas uma aplicação pode estar ativa. Manter consome Energia Espiritual; ativar/desativar não cobra outra taxa fixa. Não ativar automaticamente ao atacar ou receber dano.

| Estado | Experiência aprovada |
|---|---|
| Desligado | Sem reforço e sem manutenção de Blut |
| Vene | Reforço defensivo corporal. Pode atacar normalmente, sem o bônus de Arterie |
| Arterie | Reforço ofensivo nos ataques elegíveis de R. Pode bloquear normalmente, sem a proteção adicional de Vene |
| Troca | Desativar aplicação anterior antes de ativar a próxima; transição breve, sem benefício de nenhuma das duas e sem sobreposição |

**Justificativa da escolha:** uma escolha sustentada dá ao jogador uma decisão entre proteção, reforço ofensivo e reserva de Energia para o arco. Troca automática por ataque/dano tenderia a resolver essa escolha pelo jogador. Cobrar somente por impacto incentivaria alternância instantânea em cada evento; manutenção torna a duração parte da decisão. A taxa ainda precisa de calibração para ser útil sem transformar Vene em proteção permanente gratuita.

**Contrato da decisão e requisitos de integração:**

- Selecionar apenas aplicação conhecida, com estado e Energia válidos. Validar a troca antes de abandonar a atual; pedido inválido preserva a aplicação ainda válida, sem custo novo. Se a atual perde validade, encerrá-la independentemente do pedido.
- Energia financia manutenção enquanto o estado estiver ativo, dentro ou fora de combate. Não cobrar Stamina adicional por manter Blut. Antes de conceder cada período de benefício, garantir seu pagamento; sem recurso, desligar. Não há período gratuito obtido por ligar/desligar repetidamente.
- Ativação e troca usam transição breve; desligar apenas remove o próprio reforço. Pedidos repetidos não reiniciam benefício grátis, não apagam recuperação/cooldown de outras ações e não encurtam a transição. Tempos e prioridade de comandos ficam na ficha/M14/M15.
- Mudar de aplicação não é ação ofensiva, mas depende da fase permitir essa ativação. Não cancela carga, ataque ou recuperação de outra técnica por omissão, nem cria trava global. Se o estado atual não permite trocar, rejeitar com feedback; não guardar uma troca automática para mais tarde.
- Arco e Hirenkyaku não exigem desligar um Blut já ativo, enquanto compatíveis e com recursos. Energia insuficiente para manter Blut encerra o reforço; Hirenkyaku ainda pode usar Stamina disponível, conforme N. Foco e ocupação das mãos pertencem ao arco, sem se estender a Blut.
- Morte, desconexão, troca de dimensão e perda de compatibilidade encerram aplicação/transição sem apagar aprendizado ou Masteries. Não reativar automaticamente ao retornar. Sair do modo de combate desliga Blut; essa regra é específica desta decisão e não confunde modo manual com Combat State automático.
- Manter ativo, alternar ou sofrer dano passivamente não gera Mastery por tempo. Vene exige contexto defensivo válido; Arterie, aplicação ofensiva elegível. Treino intencional pode ser válido; AFK, aliado/pet ou loop artificial não se tornam treino eficiente por usar Blut.

**Alternativas consideradas e não escolhidas:** proteção passiva automática com cobrança por dano simplifica o controle, mas reduz a escolha entre os reforços; janelas breves por ativação criam um ritmo de habilidades temporárias. Foram escolhidos estados mantidos, com seleção e transição legíveis. O contrato não determina teclas novas ou ocupação de slots; M14 apresenta as aplicações sem inventar uma Proficiência adicional.

### 31. M06-09R — DECIDIDO — Alcance dos reforços de Vene e Arterie

**Aprovado em 20/10/2026.** Resposta explícita: “Arterie em golpes de força corporal (recomendado)”. A pergunta incluiu golpes corporais e componentes armados dependentes da força corporal, sem bônus automático para flechas, projéteis ou Gintō; Vene protege contra dano de ataques elegíveis, sem cura ou invulnerabilidade. Incluir flechas básicas no bônus não foi escolhido. O recorte de Arterie é adaptação explícita do mod. Parâmetros, conteúdo e integrações delegados não foram fechados por associação.

**Regra aprovada:** Vene melhora a resistência corporal a dano de ataques elegíveis; Arterie reforça golpes corporais e a parcela de ataques armados que depende da força corporal. Arterie não aumenta automaticamente flechas, projéteis, Gintō ou todo poder principal.

| Aplicação | Efeito aprovado | Limites |
|---|---|---|
| Vene | Mitigação adicional do dano elegível que atinge o personagem enquanto a aplicação está ativa | Não é escudo invulnerável, cura, absorção de Energia ou uma nova barra; não regenera Postura nem impede Guard Break/controle automaticamente |
| Arterie | Reforço do dano corporal direto, inclusive componente físico elegível de ataque armado | Não concede arma, moveset ou técnica desconhecida; não aumenta todos os atributos ou a velocidade de Hirenkyaku |
| Arco/fórmulas | Mantêm custos, preparo e efeitos próprios | Sem bônus geral de Arterie por serem ataques Quincy; Vene não impede executá-los quando a ação permitir |
| Técnica composta | Sua ficha identifica quais componentes realmente recebem reforço | Não aplicar o mesmo bônus a todo o dano e novamente a cada parcela; exceção exige fonte/justificativa própria |

**Justificativa da escolha:** a referência enfatiza reforço do corpo. Delimitar o bônus evita transformar Arterie em multiplicador obrigatório de toda arma, fórmula e Schrift. Isso torna Arterie especialmente útil para repertórios próximos; Quincy já não é uma classe obrigatoriamente de arqueiro. A alternativa é estender o bônus à ofensiva espiritual, incluindo flechas, como adaptação explícita que exigiria recalibrar o arco e o alcance da escolha defensiva/ofensiva.

**Contrato da decisão e requisitos de impacto:**

- Vene é avaliado no momento em que o dano elegível é resolvido; Arterie, no contato do golpe/componente corporal elegível. Não aplicar benefício retroativo a um impacto já resolvido ou restaurar Vida perdida ao ligar a capacidade.
- Ataques físicos e espirituais diretos podem ser elegíveis à proteção de Vene, conforme classificação de dano. Queda, afogamento, fome, sufocamento, vazio e outras perdas ambientais não recebem proteção geral dessa capacidade. Fogo, veneno, dano contínuo e exceções exigem tabela explícita de integração em M06-11; não confundir a aparência de um ataque de fogo com todo dano ambiental.
- Guarda, armadura e Vene não reduzem o mesmo dano várias vezes pela mesma contribuição. M06-11/M15 definem ordem, categorias, limites e interações com encantamentos; defesa base e atributos permanecem. Não extrair imunidade do Battle Power ou de Mastery alta.
- Vene pode atuar durante preparo do arco e Hirenkyaku quando ativo e compatível, mas não preserva carga cancelada nem concede bloqueio simultâneo ao impulso. Arterie também não permite golpear durante Hirenkyaku: O exige encerrar o deslocamento especial.
- A Mastery de cada aplicação melhora moderadamente sua eficácia/eficiência; não substitui atributos, não concede ambas simultâneas e não elimina o custo de manutenção por simples completude do indicador.

**Fora desta base:** cura por Blut, extensões externas especiais e variantes associadas a personagens não são concedidas. Exigem referência e ficha próprias; esta rodada não transfere técnicas individuais ao repertório comum.

### 32. Revisão conjunta de Hirenkyaku e Blut — 20/10/2026

**Estado:** revisão documental após aprovação explícita de P/Q/R; sem evidência de gameplay. O avanço para Blut ocorreu na mesma rodada; não houve uma parada para decidir cada meio de deslocamento separadamente.

| Cenário futuro de aceitação | Resultado esperado pelos contratos aprovados |
|---|---|
| Usar Hirenkyaku durante queda | Movimento horizontal, gravidade e queda acumulada preservadas; sem pouso seguro automático |
| Repetir no ar ou atravessar água | Novos custos/tempos e colisão; sem voo, piso aquático ou respiração concedidos |
| Selecionar Vene e depois Arterie | Transição sem sobreposição; somente reforço selecionado após sua conclusão |
| Pedir aplicação desconhecida ou troca em fase inválida | Rejeitar e informar; manter estado anterior somente enquanto válido |
| Atacar com Vene / bloquear com Arterie | Ação comum disponível continua possível; não recebe o reforço da outra aplicação |
| Atirar com Arterie | Arco mantém contrato A–L; não ganha bônus geral da aplicação aprovada em R |
| Manter Vene enquanto prepara arco | Energia de manutenção e custo do tiro são obrigações distintas; não duplicar cobrança do tiro nem conceder benefício sem pagar |
| Energia se esgota durante manutenção | Encerrar Blut; ações físicas e Hirenkyaku seguem seus recursos/condições, sem paralisia total |
| Receber dano elegível, cair ou sofrer Guard Break | Vene atua somente no dano classificado; não elimina queda, controle ou a abertura da guarda |
| Morrer, sair ou trocar de dimensão | Limpar estados transitórios sem apagar aquisição/Proficiência/Mastery; sem retorno automático do impulso ou Blut |

**Dono do estado:** servidor autoriza movimento, seleção de Blut, custos e impacto. Conhecimento e Masteries pertencem ao personagem; itens e interface não transferem esses dados. Cliente mostra modo, transição, efeitos e motivo de falha; não decide bônus por aparência visual. Concorrência deve produzir uma única resolução por ação/evento, sem benefício ou cobrança duplicados.

**Responsabilidades antes de execução:** M06-11/M15 fecham classificação de dano, guarda/armadura, limites de mitigação e ordem de cálculo; M06/M05/M15 calibram manutenção, transição, eficácia, movimento e progressão; M08/M12/M13 concretizam ensino; M14 define comandos/feedback; M07 trata formas/sustentação/exceções; M10/M16 integram meios, chunks, dimensões, persistência e sincronização. Nenhum número, biblioteca nova ou personagem professor foi escolhido.

**Resultado da revisão:** não foi identificado conflito que exija revogar A–O. P completa os meios sem transformar Hirenkyaku em voo; Q respeita a separação de Energia/Stamina e das duas Masteries; R mantém o arco sem bônus automático de Arterie. Vene não é bloqueio, Arterie não é autorização de ataque simultâneo ao impulso, e nenhuma aplicação recupera carga ou apaga tempos. A manutenção de Blut concorre legitimamente com a Energia do tiro: se o recurso restante for insuficiente ao emitir, aplica-se E, sem flecha ou custo do tiro rejeitado; o que já foi pago pela manutenção não é devolvido.

**Continuidade:** P/Q/R aprovados, sem pergunta pendente nesta rodada. Hirenkyaku e Blut podem avançar como fechados estruturalmente, conforme seção 33. Próximo: repertórios e ferramentas Quincy, incluindo Gintō conforme pesquisa específica. Schrift e formas permanecem nas fronteiras narrativas e de M07; não conceder seu catálogo por fechar fundamentos. M06-09 continua em especificação. Esta rodada altera documentação; não modifica código, dependências ou saves, nem executa build/testes de gameplay.


### 33. Consolidação estrutural de Hirenkyaku e Blut — 20/10/2026

**Estado:** decisões estruturais aprovadas, com números, conteúdo e integrações delegados. Não equivale a ficha executável, implementação, teste ou aceite de gameplay; M06-09 continua `EM ESPECIFICAÇÃO`.

| Capacidade | Estrutura vigente | Pendências para execução |
|---|---|---|
| Hirenkyaku — M/N/O/P | Aprendizado próprio; comando dedicado e impulso horizontal em chão/ar/água; Stamina no início aceito; colisão/gravidade/queda preservadas; parar para ação válida, sem espera extra de cancelamento | Custo, velocidade, alcance, reutilização, regras executáveis por meio/colisão/chunks, controle de input e sincronização |
| Blut — Q/R e classificação C | Aplicações aprendidas; seleção manual desligado/Vene/Arterie; Energia de manutenção; troca breve sem sobreposição; Vene mitiga ataques elegíveis, Arterie reforça componentes de força corporal | Taxa de manutenção, transição, intensidade/eficiência, tabela de dano, armadura/guarda, comandos e apresentação |
| Arco com as duas capacidades — A–L e integração M–R | Reação válida descarta preparo; tiro emitido e sua recuperação permanecem; Blut não libera ações simultâneas proibidas e Arterie não aumenta a flecha básica | Ordenação de eventos, concorrência de recursos, casos especiais de estado e impacto, evidência futura de validação |

Responsáveis estão nas seções 27 e 32; nenhum item delegado foi resolvido por silêncio. Não reabrir M–R para escolher seus mesmos modelos. Ajustes que realmente alterem escolhas aprovadas precisam de revisão identificada. Depois de repertórios/ferramentas, revisar o conjunto Quincy antes de avançar para M06-10 — Fullbringer e M06-11 — Auditoria e Integração. Fichas de linhagem e artes continuam adiadas.


### 34. Repertórios e Gintō — registro recuperado — 20/10/2026

**Estado:** M06-09S/T/U `DECIDIDOS` por três respostas explícitas. As propostas foram discutidas e aprovadas na conversa, mas a gravação anterior falhou por falta de espaço. Este registro recupera as decisões sem exigir nova votação. A–R permanecem decididos.

C já aprovou Gintō como campo opcional com Proficiência e Mastery por fórmula distinta. Ensino por repertório e ausência de compra universal de técnicas não são decisões novas. Uma fonte apropriada pode apresentar fundamentos e várias fórmulas desenvolvíveis; aquisição exige prática pertinente, sem exigir uso livre de técnica ainda bloqueada. Posse/fabricação de um tubo não ensina a fórmula. Recarregar inventário, ficar ativo ou repetir ações irrelevantes não gera Mastery automaticamente.

**Referências consultadas na preparação de S/T/U, em 20/10/2026:** [Pierrot — glossário sa](https://pierrot.jp/title/bleach/keyword_sa.html), entrada `破芒陣 / シュプレンガー`, e [glossário ka](https://pierrot.jp/title/bleach/keyword_ka.html), entrada `滅却印`. A primeira descreve conteúdo condensado em tubo combinado com uma disposição de Seele Schneider e salienta o tempo de preparação; a segunda identifica o sinal/arranjo usado em fórmulas Quincy. Não demonstram montagem no chão para toda fórmula.

O arquivo oficial informa seu recorte da época de exibição. Não universalizar a antiga exclusividade de arma com lâmina para todas as eras. Não foram lidos capítulos, episódios ou o guia MASKED integralmente. Resultados secundários não fundamentam regras novas. Reuso, recarga, custos, quantidades e controles abaixo são adaptações aprovadas, sem alegar que essas páginas os estabelecem universalmente. Não houve nova pesquisa externa nesta recuperação documental.

### 35. M06-09S — DECIDIDO — Tubos recarregáveis com conteúdo preparado

**Aprovado em 20/10/2026.** Resposta explícita: “Tubos recarregáveis com conteúdo preparado (recomendado)”. A pergunta incluiu preparação fora de combate usando Energia, consumo de conteúdo pela fórmula e recipiente vazio preservado; sem nova cobrança pessoal pelo mesmo conteúdo no uso e com possibilidade de fórmulas básicas usando provisões mesmo com Energia atual baixa/zero. Recarga e reutilização foram identificadas como adaptações do mod.

**Regra aprovada:** tubos físicos reutilizáveis com conteúdo preparado. Fora de Combat State, um procedimento aprendido usa Energia Espiritual para preparar os tubos. Cada fórmula consome o conteúdo exigido; o recipiente permanece vazio para recarga posterior. Não cobrar novamente Energia pessoal pelo mesmo conteúdo na fórmula básica.

| Alternativa | Estado e consequência |
|---|---|
| Tubo recarregável com conteúdo preparado | **Escolhida:** levar provisões e preparar reposição posteriormente |
| Instrumento sem carga, com Energia por uso | Não escolhida: aproxima seu ciclo do de outras técnicas |
| Tubo preparado inteiramente descartável | Não escolhida: exige reposição contínua também do recipiente |

**Contrato da decisão e requisitos de integração:**

- Preparação exige conhecimento e recipiente disponível; fabricar, emprestar ou trocar item não transfere aquisição ou Mastery.
- Cobrar Energia da recarga ao concluir conteúdo válido, uma vez; cancelamento anterior não produz conteúdo pronto nem cobra a recarga concluída. Duração/condições executáveis pertencem a M11/M15, sem recarga passiva gratuita.
- A restrição de recarga usa Combat State automático; desligar Combat Mode manual não a contorna. Não acrescentar uma barra racial de Reishi: provisões são inventário.
- Cada fórmula declara quantidade de conteúdo/tubos. Capacidade, empilhamento, custo da carga, estoque transportável e obtenção pertencem a M11/M15; não presumir estoque ilimitado, bancada obrigatória ou biblioteca de acessórios.
- Conteúdo preparado pode alimentar fórmula básica com Energia pessoal zero quando os demais requisitos forem válidos. Não restaura tiro, manutenção de Blut ou outra ação dependente da reserva pessoal. Não aprova converter tubos em Energia do personagem.
- Custo pessoal adicional só existe em ficha específica quando justificar trabalho diferente do conteúdo já armazenado; não usar a mesma operação para cobrar duas vezes o mesmo recurso.
- M11 resolve vazios e transformação de pilhas sem duplicar recipiente ou exigir espaço livre fictício; define perda/reposição e acesso inicial sem requisito circular. Duração, valores e economia permanecem delegados.

### 36. M06-09T — DECIDIDO — Guardar manualmente o arco para Gintō

**Aprovado em 20/10/2026.** Resposta explícita: “Guardar arco manualmente para Gintō (recomendado)”. A pergunta incluiu seleção da fórmula pela Combat Hotbar, tubos disponíveis no inventário, arco guardado manualmente quando exigir mãos e ausência de seleção individual de tubos pela hotbar vanilla. Pedido incompatível falha com aviso, sem consumo ou perda de preparo válido do arco. Guarda automática não foi escolhida.

**Regra aprovada:** fórmula adquirida e equipada utiliza provisões acessíveis do inventário autorizado do personagem. Quando exige manipulação/gesto, guardar o arco de duas mãos antes; não executar simultaneamente uma manipulação incompatível com as mãos ocupadas.

- O slot da Combat Hotbar representa técnica; não cria, move ou duplica item físico. M14 define comando/feedback sem obrigar o jogador a selecionar cada tubo como item vanilla.
- A ficha identifica uma/duas mãos e gestos. Durante manipulação incompatível, não usar simultaneamente escudo, comida, bloco ou outra arma; não ejetar ou mover itens reais para liberar a animação.
- Provisões não são buscadas em baús distantes, chão, companheiro ou outra dimensão. M11 define armazenamento elegível concreto, sem presumir slot novo.
- Pedido inválido por arco empunhado, falta de provisões ou estado inadequado falha sem consumo ou guarda automática. Preparo de flecha ainda válido permanece. Guardar voluntariamente aplica H/L: limpar carga e preservar tempos.
- Blut ativo pode permanecer quando compatível e pago; Arterie não aumenta Gintō automaticamente, conforme R. Durante Hirenkyaku, a técnica depende de encerrar o deslocamento e satisfazer seus próprios requisitos, conforme O; não ganha simultaneidade por esta decisão.

### 37. M06-09U — DECIDIDO — Execução e consumo conforme a fórmula

**Aprovado em 20/10/2026.** Resposta explícita: “Execução e consumo conforme cada fórmula (recomendado)”. A pergunta incluiu técnicas com preparo direto e outras com montagem no mundo, consumo por etapa na liberação do conteúdo e ausência de reembolso depois de usá-lo, sem digitar encantamentos ou minijogo obrigatório. Simplificar todas para execução direta não foi escolhido.

**Regra aprovada:** preservar a execução pertinente de cada fórmula. Fórmulas diretas têm preparação própria e liberam efeito; fórmulas que exigem posicionamento/arranjo precisam de montagem real. Consumir conteúdo quando a etapa realmente o libera; cancelar antes preserva a provisão, enquanto erro ou interrupção posterior não devolvem conteúdo utilizado.

| Situação | Contrato da decisão e requisitos de integração |
|---|---|
| Execução direta | Validar conhecimento, mãos, provisões, alvo/local e estado; preparar; liberar conteúdo/efeito uma vez. Falha anterior não produz efeito nem consome conteúdo |
| Montagem no mundo | Cada etapa declara instrumento, posição válida e eventual conteúdo consumido. Uma tecla não monta silenciosamente todo o arranjo |
| Efeito liberado | Erro/colisão não devolve provisão; alvos, trajetória, área, aliados/pets, blocos e duração pertencem à ficha |
| Reação/interrupção | Guarda, Dodge, movimento e Hirenkyaku dependem da fase/ficha, sem copiar automaticamente cancelamento do arco ou do Cero |
| Preparação incompleta | Não completar/detonar de graça. Ficha define expiração/desmonte, persistência, recuperação de instrumentos, morte/logout e chunks; reset de sessão não devolve conteúdo já liberado |

Múltiplas etapas registram consumo por etapa, sem cobrar tudo novamente ao concluir. Preparações no mundo precisam de dono, validade e regras de interação; compartilhar ou aproveitar montagem de outra pessoa depende de M15. Sprenger é referência estrutural, não concessão de conteúdo inicial ou aprovação de destruição de blocos. Dano ao terreno precisa de contrato antes da implementação.

### 38. Revisão documental de repertórios/Gintō — 20/10/2026

| Caso futuro de aceitação | Resultado esperado pelos contratos aprovados |
|---|---|
| Receber tubo sem conhecer fórmula | Não concede técnica, Proficiência ou Mastery |
| Aprender repertório | Abre estudo apropriado, sem domínio/aquisição automática de todas as fórmulas |
| Cancelar recarga antes de completar | Sem conteúdo pronto ou cobrança de recarga concluída |
| Tentar recarregar em Combat State desligando modo manual | Recarga continua indisponível |
| Usar provisão pronta com Energia zero | Fórmula básica válida pode funcionar; arco/Blut não são restaurados |
| Pedir fórmula com arco incompatível | Falha informada, sem consumo/guarda automática; preparo válido permanece |
| Cancelar antes/depois de liberar conteúdo | Antes, provisão intacta; depois, compromisso permanece |
| Completar várias etapas | Cada conteúdo consumido uma vez, sem cobrança ou recipientes duplicados |

**Dono do estado:** servidor valida conhecimento, inventário/conteúdo, etapas, custo e efeito; cliente apresenta mãos, provisões, fase e falha. Reservar provisões durante preparo precisa impedir uso/transferência duplicados e revalidar antes do compromisso, sem impor solução de código. Quests/mestres solicitam aquisição/treino; não editam livremente Mastery ou inventário.

**Responsáveis:** M06 define fichas e compromissos; M11 define recipiente/conteúdo, recarga, quantidades, pilhas, obtenção e reposição; M08/M12/M13 concretizam ensino; M14 define seleção/feedback; M06-11/M10/M15 definem impacto, montagem, propriedade, cooperação e calibração; M16 integra dados, persistência, concorrência e validação. Catálogo e números permanecem para pesquisa/fichas próprias.

**Resultado:** S/T/U registrados após liberação de espaço. Não foi identificado conflito que exija rever A–R: o tiro básico continua sem munição física; os tubos pertencem às fórmulas que os exigem; foco do arco não se estende a toda capacidade Quincy; Arterie não amplia automaticamente Gintō; carga do arco não é preservada ao guardá-lo. A rodada apenas recupera e revisa as aprovações, sem implementação ou teste de gameplay. Segue a revisão geral Quincy.


### 39. Revisão geral e fechamento estrutural Quincy — 20/10/2026

**Resultado:** M06-09 pode avançar como `DECIDIDO` em nível estrutural, conforme as aprovações A–U. Não foi identificado conflito que exija revogar essas escolhas. Fichas executáveis, catálogo, conteúdo, calibração e integração permanecem pendentes; o módulo M06 continua `EM ESPECIFICAÇÃO`. A conclusão é documental, sem novo aceite de gameplay ou prova de implementação Quincy.

#### Escopo e rastreabilidade

Revisados os contratos do bloco Quincy, incluindo a recuperação de S/T/U; confrontados com M04/Q1–Q11 e a revisão do uso de Spiritual Points, M05 (recursos, recuperação, Mastery e Reishi), M06 universal (repertórios, execução, recuperação, defesa, mobilidade, modos e inventário) e a organização aprovada em M06-07. Esta revisão trata coerência do design, sem auditar todo o código ou expandir a pesquisa canônica para cada exceção.

| Parte | Decisões aprovadas | Estrutura consolidada |
|---|---|---|
| Arco básico | A/B/D–L | Foco equipado e manifestação manual de duas mãos; preparo limitado, emissão com pagamento único e trajetória reta; reações e recuperação próprias |
| Desenvolvimento | C e herança de M06-03/07 | Armamento, mobilidade e Gintō com Proficiência; técnicas distintas com Mastery; tiro comum sem Mastery redundante; Vene/Arterie com domínios próprios |
| Hirenkyaku | M/N/O/P | Aprendizado próprio; impulso horizontal em chão/ar/água, Stamina no início e colisão/gravidade preservadas; parar para agir |
| Blut | Q/R | Aprendizado específico; seleção manual e Energia de manutenção; Vene defensivo, Arterie em componentes de força corporal; sem simultaneidade |
| Gintō | S/T/U | Conteúdo preparado em tubos reutilizáveis; arco guardado quando exigir mãos; execução e consumo por etapa, conforme cada fórmula |
| Poder principal/formas | M04 e fronteiras de M07/M08/M13 | Fundamentos não concedem Schrift, Vollständig, Letzt Stil, armas ou técnicas individuais de todo o elenco |

#### Conferência das interações

| Ponto revisado | Conclusão e limite |
|---|---|
| Energia zero e Gintō | Não há geração gratuita: a fórmula básica utiliza conteúdo previamente preparado. A exceção não libera tiros/Blut sem Energia nem uma conversão universal de tubos em reserva pessoal |
| Arco sem munição e tubos físicos | Q2/A dispensam munição convencional do arco. Isso não elimina instrumentos e conteúdo de fórmulas que os exigem; tubos não viram pedágio do tiro ou de Hirenkyaku |
| Blut durante mira | Manutenção consome a mesma reserva do tiro. Revalidar na emissão: se faltar Energia, não emitir/cobrar flecha nem agendar disparo após regenerar. Não devolver manutenção já realizada |
| Arterie e flechas | O bônus corporal é uma adaptação aprovada em R; não estendê-lo ao arco para corrigir equilíbrio sem revisão explícita. Vene ainda permite atacar, sem bônus de Arterie |
| Gintō depois de um tiro | Guardar arco não apaga recuperação. Uma fórmula ofensiva também respeita o bloqueio ofensivo de I; uma fórmula de outra função depende de sua classificação e permissões de fase, não de liberação universal |
| Hirenkyaku e outras ações | O permite parar por ação disponível. Não transportar carga, golpear/guardar simultaneamente durante impulso ou ignorar recurso/estado da ação seguinte |
| Queda, água e Vene | P conserva gravidade, queda e riscos; Vene não se torna imunidade ambiental. Dano especial/contínuo permanece sujeito à tabela de integração, sem conceder proteção por aparência visual |
| Mãos e inventário | O foco não é a manifestação; duas mãos do arco não movem itens reais; T exige guardá-lo para manipulação incompatível. A hotbar de técnicas não substitui fisicamente recipientes ou equipamentos |
| Modo e estado de combate | Sair de Combat Mode limpa arco/Blut conforme L/Q, mas não limpa Combat State, tempos ou condições de recarga. S não permite recarregar em luta desligando a interface |
| Conhecimento e desenvolvimento | Item recebido, Proficiência alta ou abertura de repertório não concedem todas as técnicas. Acesso inicial pode usar fundamentos conhecidos/tentativas assistidas; não exigir domínio de técnica ainda bloqueada |
| Mundo persistente | Preparação colocada, projétil emitido e execução do personagem têm ciclos próprios. Limpar carga/Blut/impulso não implica apagar ou reembolsar tudo que já foi usado; detalhes têm responsáveis |

#### Condições de balanceamento e conteúdo

Os pontos abaixo são riscos a observar na especificação/validação futura, não defeitos demonstrados em jogo nem novas revisões de produto:

1. **Suprimentos de Gintō:** conteúdo armazenado amplia provisões disponíveis para a expedição. Capacidade, espaço ocupado, custo/tempo de recarga e eficácia das fórmulas precisam dar função ao preparo sem permitir que estoque torne Energia/ritmo irrelevantes. Não inventar um limite numérico nesta revisão nem criar outra barra racial para resolvê-lo.
2. **Função de Arterie:** o arco é a primeira arma, mas R não o fortalece. O repertório corporal/armado compatível precisa oferecer situações em que o reforço ofensivo seja útil; sem isso, Vene tende a ser a única escolha interessante no começo. M06/M08/M13/M15 devem avaliar disponibilidade e encontros, sem conceder armas/poderes individuais automaticamente.
3. **Custo de manter Blut:** calibrar em conjunto com a arma principal para que permanecer protegido tenha custo perceptível sem tornar o tiro fundamental impraticável. Comparar duração útil, cadência e alternativas; ainda não há valores ou ensaio em jogo.
4. **Transições de mãos:** guardar manualmente antes de Gintō é escolha aprovada. M14/protótipo deve torná-la breve e legível; não compensar desconforto com guarda automática silenciosa ou rearranjo de inventário.
5. **Preparações no mundo:** encontros precisam permitir uso situacional de fórmulas preparadas sem exigir uma arena intocável ou desmontar construções do jogador. Cooperação, desmonte e recuperação de instrumentos precisam de regras próprias.

#### Pendências antes de especificação executável

| Responsável | Entrega necessária | O que ainda bloqueia |
|---|---|---|
| M06 / M05 / M15 | Fichas com custos, tempos, curvas, alcance, velocidade, intensidade, eficiência e critérios de prática; classificação de técnicas ofensivas e outras funções | Implementar e avaliar ritmo/equilíbrio a partir de regras executáveis |
| M11 / M14 / M06-11 | Slot/forma concreta de equipar foco, mãos, provisões acessíveis, cargas/pilhas/vazios, recarga, obtenção, troca/perda e reposição sem circularidade | Integração completa de equipamento/inventário e recuperação de acesso |
| M06-11 / M15 | Alvos, aliados/pets, armadura, escudos, Postura, tipos de dano, ordem de mitigação e exceções | Resolução completa de ataque/defesa, inclusive Vene e técnicas compostas |
| M06-11 / M10 / M16 | Colisão por meio, portais, área carregada e ciclos de projéteis/arranjos/tempos já iniciados | Execução consistente no mundo persistente e multiplayer |
| M07 / M08 / M12 / M13 | Formas/poder principal, fontes de ensino, acesso por era, currículo e campanhas | Conteúdo concreto; não é concedido por encerrar fundamentos |
| M14 / M15 / M16 | Inputs concorrentes, feedback de fase/falha/recursos, legibilidade, latência e observação | Validação futura dos controles e sincronização |
| M16 | Dados, migração/compatibilidade, implementação e evidência futura de validação | Passar de decisão documental a entrega de gameplay |

Catálogo de armas/fórmulas e capacidades avançadas continua no horizonte do projeto; cada ficha deve identificar fonte, acesso e execução. Deixar seu conteúdo com responsáveis não o elimina do plano nem autoriza preenchê-lo com técnicas inventadas. Consequência sistêmica de eliminar Hollows, RAC-19, permanece backlog identificado, sem punição global criada nesta revisão.

#### Continuidade

A–U estão aprovados e não exigem outra votação para registrar ou concluir esta revisão. As decisões canônicas/adaptações permanecem identificadas; esta auditoria de contratos locais não acrescenta pesquisa de capítulos, episódios ou publicações recentes. Os encaminhamentos intermediários das seções anteriores são históricos; esta seção governa a continuidade atual.

**Próximo bloco:** M06-10 — Fullbringer, respeitando origem, poder principal e a integração de Minecraft. Depois, M06-11 — Auditoria e Integração. Fichas de linhagem/Grimmjow e seleção de artes continuam adiadas. Não houve alteração de código, build, dependências ou saves, nem execução de testes em jogo nesta revisão.

---

# M06-10 — Fullbringer

## Estado do bloco — 20/10/2026

| Campo | Situação |
|---|---|
| Estado | `DECIDIDO` em nível estrutural; fichas, conteúdo, números e integrações em especificação |
| Responsável pela decisão | Equipe/usuário, com recomendação de direção de game design |
| Herança | M04, RAC-13/F1–F13 e RAC-22/23; revisões de M05 e contratos universais/refinamentos de M06 |
| Rodada atual | A–F aprovados; revisão e fechamento estrutural nas seções 15–17 |
| Depois | M06-11 — Auditoria e Integração; pendências com responsáveis na seção 17 |
| Evidência técnica | Preparação documental; sem implementação ou teste de gameplay nesta rodada |

### 1. Objetivo, fantasia e escopo

O jogador descobre como interagir com a alma da matéria e transforma esse aprendizado em capacidades úteis de combate e exploração. Pode desenvolver os fundamentos antes de conquistar seu Fullbring canônico. O poder escolhido depois preserva seu vetor e sua identidade próprios.

Este bloco define fundamentos compartilhados, aprendizado, mobilidade e fronteiras de execução. Não escolhe o primeiro Fullbring do catálogo, suas formas, uma arte ou uma campanha completa. Completude/despertar e troca de poder dependem de M07; itens e recuperação de vetores dependem de M11.

### 2. Decisões herdadas — não votar novamente

- **M04/F1–F2:** manipulação básica em aplicações delimitadas, fundamentos separados do poder individual. Não existe autorização para deformar/arremessar/modificar livremente todo bloco do Minecraft.
- **F3–F4/F12:** poderes canônicos e vetores apropriados. Nenhum objeto sentimental obrigatório no prólogo; nem todo vetor é item de inventário ou objeto empunhado.
- **F6/F13/RAC-23:** domínio real importa; um poder canônico principal por vez. Fundamentos permanecem independentes de eventual troca, cujo processo especial ainda será especificado.
- **F7–F11:** Xcution é afiliação; conexão Hollow não cria corrupção/fome/evolução Hollow automática; treino intencional existe; outras dimensões oferecem gameplay; raça não determina classe fixa.
- **M05:** cinco atributos; Stamina é recurso principal de Bringer Light, sem obrigar custo/comportamento igual a Shunpo, Sonído ou Hirenkyaku. Energia alimenta capacidades espirituais conforme a ficha, sem nova barra racial.
- **M06 universal/M06-07:** acesso, aprendizado, Proficiência e Mastery distintos; lista de repertório, sem compra universal de técnicas, segundo rank de disciplina ou um NPC obrigatório por técnica. Técnicas pessoais não viram fundamentos compartilhados.

**Textos históricos:** F5/F6 e diagramas de M04 ainda incluem Spiritual Points na sequência de técnicas/desenvolvimento. Aplicar M05, `REV-M02-02/04/05`, e M06-03/07: pontos principalmente nos atributos; Mastery não comprável; nenhum preço universal presumido para técnica ou completude. A referência de M05 à reação do objeto como fonte possível de descoberta não torna esse objeto obrigatório no prólogo, em conflito com F12.

### 3. Pesquisa canônica consultada e limites

Consulta em **20/10/2026**, com interpretação dos textos japoneses. São resumos oficiais do anime; não houve leitura integral dos capítulos, episódios ou novels nesta preparação.

| Fonte exata | Informação conferida | Uso e limite |
|---|---|---|
| [Pierrot — glossário ha](https://pierrot.jp/title/bleach/keyword_ha.html), entradas `完現術` e `完現術者` (BL-11) | Extrair/usar a alma presente na matéria; potencializar sua ajuda; mudar a forma de matéria familiar/compatível | Base conceitual. Não define uma lista universal de alterações de terreno, números, controles ou indicadores de progresso. Arquivo ressalva a época de exibição. |
| [Pierrot — retrospectiva do arco do desaparecimento dos poderes](https://pierrot.jp/title/bleach/playback_16.html), episódios 343–366, trechos de treinamento (BL-17) | Aprendizado por experiência e orientação; poderes específicos de Riruka/Yukio participam de treinos; Ichigo desenvolve seu poder | Sustenta aprendizado contextual, sem transformar as condições particulares de Ichigo em currículo obrigatório de todo Fullbringer. Não autoriza roubo/transferência universal de poder ao jogador. |
| [Site oficial TYBW — personagens](https://bleach-anime.com/character/), Ginjō, Tsukishima, Yukio e Riruka (BL-05) | Pingente transformado em arma; marcador transformado e efeito sobre passado; dispositivo que medeia espaço; inserção em objetos com condições próprias | Confirma diversidade de vetores/efeitos. Não concede essas quatro capacidades aos fundamentos nem especifica suas fichas jogáveis. A página atual inclui contexto de TYBW, cuja era deve ser respeitada. |

A busca também encontrou a página oficial Shonen Jump do arco, mas sua abertura retornou essencialmente imagens. Não foi usada como prova textual adicional. Resultados secundários sobre Bringer Light não substituem a conferência de sua cena/descrição primária: detalhes de apoio no ar, água e verticalidade permanecem para a rodada de mobilidade, com fonte própria ou adaptação identificada. A pesquisa atual não justifica declarar essas regras canônicas já verificadas.

### 4. M06-10A — DECIDIDO — Uma Proficiência dos fundamentos e Mastery por técnica distinta

**Aprovação em 20/10/2026:** resposta explícita “Uma Proficiência dos fundamentos + Masteries por técnica (recomendado)”. A alternativa de duas Proficiências não foi escolhida.

**Experiência aprovada:** o jogador acompanha uma formação geral em **Fundamentos Fullbringer**. Aprender a agir sobre a matéria e desenvolver movimentação são aplicações desse conhecimento compartilhado. Capacidades com execução própria, como Bringer Light, possuem Mastery individual; o Fullbring canônico mantém seu desenvolvimento específico.

| Alternativa | Efeito prático | Avaliação |
|---|---|---|
| **Uma Proficiência dos fundamentos + Masteries individuais** | Um indicador para a formação compartilhada; repertório e técnicas continuam distintos | Recomendada: reconhece o princípio comum e evita dividir um catálogo básico ainda pequeno em progressões pouco diferentes |
| Duas Proficiências: manipulação da matéria e mobilidade | Permite destacar formação geral diferente em cada campo | Viável, mas separa aplicações do mesmo princípio; exige repertórios/práticas suficientes para dar utilidade a ambos os indicadores |

O número de Proficiências é **adaptação de design**, não classificação oficial da obra. Repetir os três campos de Hollow/Quincy não seria justificativa suficiente para criar um terceiro campo Fullbringer. O método de documentação permanece o mesmo; a organização deve servir à raça.

#### Contrato estrutural aprovado de A

1. Uma Proficiência chamada **Fundamentos Fullbringer**, adquirida organicamente por prática válida do repertório compartilhado. Não substitui o atributo Controle, a aquisição das técnicas ou os recursos de execução.
2. Refinamentos simples podem integrar fundamentos sem uma Mastery para cada gesto. Uma técnica distinta recebe aquisição e Mastery próprias conforme a granularidade de M06-07. O catálogo e essa classificação serão definidos nas próximas rodadas.
3. Bringer Light pode desenvolver sua Mastery e a Proficiência dos fundamentos por uso válido. Ter Proficiência elevada não ensina automaticamente a técnica nem concede voo ou outras variantes não adquiridas.
4. O Fullbring canônico, suas técnicas e estágios mantêm seus requisitos/domínio específicos. Não criar outra Proficiência geral do poder além das Masteries já cabíveis sem necessidade demonstrada. Completude e contribuição do domínio para formas pertencem a M07.
5. Usar um poder canônico não concede automaticamente progresso dos fundamentos. Uma ação só pode contar para ambos quando realmente praticar um fundamento e uma capacidade individual elegíveis; a ficha identifica as contribuições, sem duplicar recompensa pelo mesmo evento.
6. Correr, pular normalmente, possuir um objeto ou apenas gastar Energia não gera Proficiência Fullbringer por si só. Treino deliberado pode ser válido; loops sem aplicação relevante não serão estratégia eficiente de ganho.
7. Proficiência não é consumida, comprada com Spiritual Points ou convertida em moeda para obter poderes. Não cria classe fixa nem nova barra de manutenção em combate.

**Fronteira da aprovação:** A fecha a organização do desenvolvimento, sem aprovar por associação comando, custo individual, meios ou variantes de Bringer Light. Não exige nova votação para registrar a escolha. A–U Quincy permanecem preservados.

### 5. Aprendizado e mestres — aplicação da herança

Há espaço para descoberta inicial intuitiva, exercícios orientados e orientação de outro Fullbringer. Mestres podem ensinar repertórios e avaliar aplicação; sua existência já está aceita, sem nova votação. Não é necessário selecionar agora Ginjō, outro canônico ou um NPC original como instrutor obrigatório.

A primeira aprendizagem utiliza capacidades já disponíveis ou tentativas assistidas. Não exigir Mastery de Bringer Light antes de permitir aprendê-lo. A orientação ensina o personagem e o jogador, e não se reduz a comprar uma técnica ou acumular mortes sem relação com ela.

A formação inicial precisa oferecer combate fundamental e atividades úteis antes do poder principal, conforme M04 e M06-02. Usar arma vanilla não concede Zanjutsu avançado nem transforma essa arma em um Fullbring original. Corpo, equipamento e técnicas pessoais mantêm seus requisitos próprios.

### 6. Minecraft: limites a especificar e responsáveis

| Tema | Contrato/pendência e destino |
|---|---|
| Terreno e construções | F1 já exclui edição livre. Cada aplicação futura de matéria deve declarar efeito, alvo e eventual alteração persistente; M06/M10/M11/M15 tratam blocos, recipientes, propriedade e contrajogo. Nenhuma permissão de mineração, duplicação ou abertura de baús foi criada nesta rodada. |
| Mobilidade | M05 já define Stamina como recurso principal. B–F definem aprendizado, impulso/salto, pagamento, meios e saídas. Ficha ainda precisa de números, fases e integração; não copiar silenciosamente permissões de outra raça. |
| Percepção | Perceber alma da matéria como fundamento não equivale a rastrear qualquer mob/minério/baú através de paredes. Se surgir aplicação sensorial própria, delimitar seu alvo e sua relação com a Percepção Espiritual de M05/M06-11. |
| Vetor e inventário | Poderes podem ter vetor corporal, equipamento ou objeto. M06/M07/M11 devem definir ativação, ocupação de mãos/slots, vínculo, perda/reposição e morte, preservando F4/F12; não universalizar o foco do arco Quincy. |
| Persistência e autoridade | Personagem possui conhecimento/progresso; servidor autoriza prática e execução. A ficha futura define estados transitórios, morte/logout/dimensão/reinício e observadores; M06-11/M16 integram sem apagar conquistas por omissão. |
| Mundo carregado | Capacidades devem respeitar colisão e área disponível. Não pressupor carregamento remoto, reset de queda, passagem por paredes ou pausa global. Contrato concreto por técnica em M06, com M10/M16. |
| Conteúdo e calibração | M08/M12/M13 detalham instrutores, exercícios e aquisição; M05/M06/M15, números e prática válida; M14, apresentação. Esses módulos não aprovam retroativamente uma proposta ainda sem resposta. |

### 7. Critérios de revisão e continuidade

Critérios documentais para a organização aprovada: distinguir fundamentos e poder principal; mostrar o que cada prática desenvolve; permitir começar sem objeto pessoal/poder específico; preservar aquisição apropriada e nenhuma Mastery comprada; não repetir indicadores sem função; manter útil a formação geral em diferentes territórios.

Uma fatia futura para avaliar a experiência pode reunir descoberta/orientação, uma aplicação compartilhada delimitada e mobilidade aprendida, acompanhadas de prática/progresso legíveis. Isso é referência de avaliação, sem aprovar ainda técnica adicional, números, currículo, protótipo ou catálogo inicial de poderes.

**Continuidade após A:** organização decidida por resposta explícita; desenvolver aplicações básicas da matéria e Bringer Light. B/C/D abaixo foram aprovados posteriormente por respostas próprias. Ar/água, reações e contratos de vetor vêm na sequência, antes da revisão geral e de M06-11. Sem alteração de runtime ou teste em jogo.


### 8. Segunda rodada — Matéria e mobilidade — 20/10/2026

**A–D estão aprovados por respostas explícitas.** Na preparação desta rodada, B/C/D eram propostas; as aprovações estão registradas em cada decisão abaixo. A formação inicial recomendada aplica o conceito de alma da matéria ao próprio deslocamento. Exercícios simples sobre o chão podem ensinar a relação entre ação, apoio e impulso, sem exigir um editor de blocos nem conceder um poder individual genérico.

**Pesquisa adicional:** [TV Tokyo — sinopses dos episódios 342–354](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes29/index.html), consultada em 20/10/2026 (BL-18). A sinopse do episódio **346**, exibido em 01/11/2011, explica extrair a alma da matéria e potencializar o efeito. A do **352**, exibido em 13/12/2011, situa desenvolvimento de movimento de alta velocidade durante treino com Jackie. São resumos oficiais interpretados do japonês, sem episódio integral; não detalham nome/controles de Bringer Light, salto, pagamento, gravidade ou meios. BL-11 mantém a base conceitual. Os contratos abaixo são adaptações aprovadas no escopo das respostas, sem declarar cada regra como reprodução literal verificada do mangá.

**Direção de conteúdo recomendada:** usar impulso e salto como primeiras aplicações úteis do princípio, com orientação/prática conforme B. Isso não torna toda interação com matéria uma técnica de combate. Outras aplicações concretas devem demonstrar utilidade e referência/justificativa próprias; não inventar bônus universais de mineração, armadura, durabilidade, dano ou manipulação remota apenas para preencher o repertório.

### 9. M06-10B — DECIDIDO — Aprendizado próprio de Bringer Light

**Aprovação em 20/10/2026:** resposta explícita “Aprendizado próprio com prática (recomendado)”. Aquisição automática por Proficiência não foi escolhida.

**Funcionamento aprovado:** aprender após os exercícios iniciais com a alma da matéria, por orientação ou descoberta acompanhada de prática. O aprendizado pode ocorrer antes do Fullbring canônico, sem Xcution, objeto sentimental ou aquisição de outro poder como requisito obrigatório.

| Alternativa | Consequência |
|---|---|
| **Aprendizado próprio com prática** | O jogador aprende a executar o deslocamento, com oportunidade de orientação e desenvolvimento autônomo; recomendado pela importância mecânica da ação |
| Aquisição automática em marco de Proficiência | Menos etapas de aquisição, mas introduz uma ação importante sem exigir sua aplicação inicial |

Contrato estrutural aprovado:

- Fundamentos já conhecidos e tentativas assistidas permitem começar; não exigir Mastery da técnica ainda não aprendida.
- Proficiência pode indicar preparo e abrir treino, sem substituir conhecimento/aplicação exigidos. Não é gasta nem comprada para obter a técnica.
- O processo pode ensinar suas aplicações em etapas, sem exigir um mentor/quest diferente para cada variante. D, aprovado por resposta própria, é aplicação aprendida; não é concedido silenciosamente por aprender o deslocamento de C.
- Uso válido desenvolve Mastery de Bringer Light e a Proficiência dos fundamentos, conforme A. Treinamento repetível é legítimo; ganho por AFK, salto vanilla ou loop sem contexto não é presumido.
- M08/M12/M13 detalham fonte, exercícios e disponibilidade por era. Não foi escolhido Jackie ou outro personagem como mestre obrigatório do jogador, nem adotada a biografia de Ichigo.

### 10. M06-10C — DECIDIDO — Impulso curto direcional

**Aprovação em 20/10/2026:** resposta explícita “Impulso curto direcional (recomendado)”. Deslocamento mantido não foi escolhido.

**Experiência aprovada:** um comando dedicado inicia um impulso curto na direção escolhida pelo movimento do jogador. Pressionar sem direção pode usar a frente horizontal do personagem. Segurar o comando não estende o impulso nem inicia uma sequência automática.

A alternativa é deslocamento mantido com consumo contínuo: oferece sustentação maior, mas exige outro ritmo de interrupção/manutenção. A recomendação privilegia impulsos legíveis que possam ser usados para combate e travessia.

Contrato estrutural aprovado:

1. Exige técnica aprendida, estado compatível e Stamina suficiente. Uma cobrança de Stamina no início aceito; nenhuma Energia adicional, objeto-vetor ou Fullbring principal necessário. M05 já escolheu Stamina como recurso principal; C define sua aplicação individual.
2. Funciona dentro e fora de combate. Combat Mode e Combat State conservam suas funções; o primeiro não vira condição universal de exploração.
3. O impulso básico de C é horizontal. Olhar para cima não o converte em voo ou lançamento vertical. D aprova uma aplicação aprendida para salto a partir de apoio.
4. Percurso completo respeita volume corporal e colisão, incluindo construções e corredores. Não é teleporte, passagem por paredes, destruição de terreno ou ataque de contato.
5. Pedido inválido é rejeitado com feedback, sem custo. Se o deslocamento foi aceito/iniciado, uma colisão posterior pode encerrá-lo; não devolver automaticamente o custo já realizado.
6. Não concede invulnerabilidade, Super Armor ou imunidade ambiental por padrão. Não apaga cooldowns/Recovery anteriores ou o preparo de outra técnica para contornar suas regras.
7. Na aprovação inicial de C, ar/água e queda permaneciam abertos. E os complementa na seção 13: horizontal no chão/ar/água e salto com apoio, preservando física/riscos. Não importar outras permissões de Hirenkyaku por analogia.
8. Distância, velocidade, custo, duração e reuso ficam para complemento da ficha; F define reação/saída na seção 14. Nenhuma regra universal de cancelamento foi aprovada por C. A técnica iniciadora também precisa permitir usar mobilidade em sua fase atual, conforme M06-06I.

### 11. M06-10D — DECIDIDO — Salto impulsionado como aplicação aprendida da mobilidade

**Aprovação em 20/10/2026:** resposta explícita “Incluir salto impulsionado aprendido (recomendado)”. Manter apenas deslocamento horizontal não foi escolhido.

**Experiência aprovada:** após aprender essa aplicação, o jogador pode pedir um salto com impulso maior a partir de apoio válido no chão. Ajuda a alcançar desníveis e atravessar espaços, preservando a trajetória física no mundo. O salto normal continua disponível, sem ativação automática de Stamina extra a cada pulo.

**Granularidade aprovada:** uma aplicação de Bringer Light, com a **mesma Mastery**; aprendizado e contexto próprios, sem outra Proficiência ou Mastery exclusiva do salto. A alternativa é manter por enquanto apenas o deslocamento horizontal e adiar o salto impulsionado.

Contrato estrutural aprovado:

- Exige aplicação aprendida, apoio válido, espaço para iniciar e Stamina suficiente. Cobra uma vez no impulso aceito; sem Energia adicional ou item pessoal. Os valores podem diferir de C.
- Ativação deliberada que combine comando de mobilidade e salto, ou equivalente remapeável em M14; prioridade exata de inputs ainda será especificada. Não se soma automaticamente ao impulso de C como duas execuções/cobranças simultâneas.
- Apoio inicial é chão com sustentação válida; não pressupor que líquido, parede, escada ou ar sejam esse apoio. Apoios especiais e fase aérea pertencem ao complemento de mobilidade.
- O impulso altera a saída do salto, mantém colisão/gravidade e não cria bloco temporário, piso permanente ou dano a terreno. Obstáculo pode impedir/interromper a trajetória.
- Não concede voo, salto infinito, imunidade a queda nem pouso seguro automático. Interação quantitativa com a queda e eventual mobilidade durante o voo ainda devem ser especificadas; não reiniciar a queda artificialmente por apertar um comando.
- Não melhora automaticamente ataques aéreos/dano corporal, não aciona poder canônico e não ignora condições de equipamentos/estados. Técnicas futuras precisam de fichas próprias.
- Escala de salto, distância, reuso, compatibilidade com sprint/efeitos vanilla e regras de entrada/pouso ficam para M06/M06-11/M15. O chão não terá multiplicadores diferentes para cada bloco sem necessidade demonstrada.

### 12. Revisão documental e registro das respostas B/C/D

| Verificação | Situação documental |
|---|---|
| Uma Proficiência em A e mobilidade com Mastery própria | Coerentes: C e D podem praticar o mesmo campo/técnica sem criar outro indicador |
| Começo sem objeto e sem Fullbring principal | Preservado por B/C/D; manipulação inicial aplica-se ao ambiente, não exige foco Quincy ou item pessoal |
| Salto deliberado × salto vanilla | D aprova uma aplicação aprendida; pular normalmente não gera custo/progresso racial automático |
| Comando de impulso × comando de salto | M14/M06-11 devem definir prioridade; uma entrada não pode cobrar/executar os dois por acidente |
| Terreno construído | Não se autoriza remover/deformar blocos; colisões e espaços continuam relevantes |
| Progressão e aprendizagem | Fonte/prática concreta em B; sem pré-requisito circular, transferência de poder por objeto ou compra de Mastery |
| Mobilidade aérea e queda | Abertas para complemento, sem aprovar voo por omissão ou impor ao Fullbringer todas as restrições Quincy |
| Ataque/guarda/Dodge durante o impulso | Aberto para a próxima rodada; C/D não concedem cancelamento universal |

Critérios futuros de aceitação: aprender e entender o comando antes de depender dele; reposicionar-se respeitando paredes; usar salto impulsionado só quando pedido e aprendido; distinguir custo/feedback das duas aplicações; manter progresso e estados de combate coerentes após interrupção e ciclo de vida. Esses critérios ainda não foram executados em gameplay.

**Respostas explícitas registradas:** B — “Aprendizado próprio com prática (recomendado)”; C — “Impulso curto direcional (recomendado)”; D — “Incluir salto impulsionado aprendido (recomendado)”. Foram três escolhas próprias; não houve aprovação por associação a A. Não votar B/C/D novamente. A revisão documental não identificou conflito entre essas escolhas e A/M04/M05/M06 universal. Meios e reações ainda dependem de complemento próprio.

**Encaminhamento histórico após aprovação B/C/D:** complementar meios, trajetória/queda e reações, depois aprovados em E/F; a seção 17 governa a continuidade vigente. Vetores/poderes individuais e revisão geral vêm depois. Dados de progresso pertencem ao personagem e a execução ao servidor; integração, ciclo de vida e observação seguem para M06-11/M16. Sem mudança de runtime, build, dependências, saves ou teste de gameplay nesta rodada.


### 13. M06-10E — DECIDIDO — Impulso horizontal em chão, ar e água; salto com apoio

**Aprovação em 20/10/2026:** resposta explícita “Horizontal no chão, ar e água; salto com apoio (recomendado)”. Restringir o impulso básico ao chão não foi escolhido. Complementa C/D sem conceder voo. São regras de adaptação do mod; esta atualização de contratos não acrescenta comprovação canônica de apoios/meios.

Contrato estrutural aprovado:

1. O impulso direcional horizontal de C pode iniciar no chão, no ar e na água, conforme estado, recursos e colisão válidos. A mira vertical não transforma esse impulso em lançamento para cima.
2. No ar, a propulsão horizontal não suspende gravidade, zera velocidade vertical ou reinicia queda acumulada. Repetir impulsos não cria sustentação/voo. Não foi criado contador artificial de saltos/impulsos aéreos; recurso, reuso e estados continuam relevantes.
3. Na água, mantém as regras do meio: sem piso sobre a superfície, respiração adicional, imunidade a afogamento ou subida automática. Alterações legítimas de movimento/queda pela água continuam próprias do meio; apertar Bringer Light não apaga o risco por si só.
4. O salto impulsionado D exige apoio válido no chão. Não pode ser relançado no ar; usar C depois de saltar muda a propulsão horizontal quando disponível, sem conceder outro impulso vertical. Um novo apoio real é diferente de reutilizar o comando no ar.
5. Passagem em terreno irregular precisa respeitar espaço e apoio válidos do corpo. Desníveis não autorizam atravessar teto/parede ou escalar uma coluna por repetição do impulso horizontal.
6. Colisão em todo o percurso, riscos ambientais e limites do mundo permanecem. Uso na água não significa autorizar uma variante imune à lava/fogo. Não cria blocos, não destrói construções e não abre portais.
7. Capacidade não presume carregamento remoto de chunks nem viagem entre dimensões. Apoios especiais, interação com portais, detalhes de líquidos/escadas/blocos parciais e limites quantitativos ficam para ficha/integração M06-11/M10/M16.

### 14. M06-10F — DECIDIDO — Reagir preservando custos, tempos e queda

**Aprovação em 20/10/2026:** resposta explícita “Reagir preservando custos, tempos e queda (recomendado)”. Exigir terminar o impulso ou pousar antes de agir não foi escolhido.

Contrato estrutural aprovado:

- Durante a propulsão horizontal de C, ataque, bloqueio ou Dodge **disponível** pode encerrá-la para iniciar. Não há execução ofensiva/guarda simultânea à manutenção desse impulso nem recuperação extra apenas por encerrá-lo.
- Validar a nova ação antes da troca. Se estiver indisponível, rejeitar com feedback e manter o impulso ainda válido até seu encerramento normal. Isso não mantém um movimento cujo estado, trajeto ou tempo já tenha deixado de ser válido, nem cria uma fila automática de comandos.
- Depois de lançado o salto D, não é necessário esperar pousar para uma ação legitimamente permitida no ar. Encerrar propulsão horizontal não apaga o lançamento vertical, congela o personagem ou interrompe a queda. Ataque/guarda/Dodge não ganham permissão aérea que seus próprios contratos não ofereçam.
- Uma reação aplica seu próprio comportamento de movimento. A troca não devolve a Stamina paga por C/D, restaura o apoio de D ou reinicia artificialmente queda/reuso. Não confundir preservar a física do salto com proibir toda alteração horizontal legítima da reação.
- Recovery/cooldown anterior continua contando; uma ofensiva ainda bloqueada por esse tempo é indisponível. F permite reagir ao movimento, sem cancelar a restrição da técnica usada antes dele. Custos e condições da reação também permanecem.
- Voltar a usar Bringer Light exige disponibilidade e nova execução válida; trocar entre C/D ou sair/entrar em Combat Mode não reinicia tempos ou recursos.
- F declara permissões de saída de Bringer Light. Não permite iniciá-lo durante qualquer fase de Cero, Kidō, arma, fórmula ou Fullbring individual: a capacidade anterior também precisa permitir a transição, conforme M06-06I.

**Fronteira de calibração:** valores de reuso, Recovery e ritmo de C/D continuam pendentes, mas não podem reintroduzir silenciosamente a espera obrigatória até fim/pouso rejeitada em F. Se uma ficha futura precisar mudar essa escolha, registrar a revisão e sua justificativa.

### 15. Revisão estrutural da mobilidade — 20/10/2026

A–F foram respondidos explicitamente. A revisão documental não identificou conflito que exija revogar essas escolhas. A existência de impulso aéreo horizontal e salto com apoio não constitui contradição: são aplicações diferentes da mesma mobilidade, com uma Mastery compartilhada.

| Caso | Contrato consolidado |
|---|---|
| Primeiro aprendizado | Fundamentos/tentativas assistidas → prática própria B; sem Mastery prévia da técnica, poder canônico ou objeto obrigatório |
| Movimento no chão | C inicia impulso horizontal com uma cobrança de Stamina e colisão completa |
| Salto a partir do chão | D precisa estar aprendido, ter apoio/espaço e comando deliberado; custo próprio, sem acionar C junto por acidente |
| Impulso no ar após salto/queda | E permite C horizontal; sem segundo lançamento vertical, suspensão ou reset de queda |
| Uso na água | C horizontal conforme o meio; sem piso aquático, respiração ou proteção ambiental nova |
| Pedido de ataque durante impulso | Se válido, F encerra propulsão e inicia ação; se bloqueado por Recovery anterior, rejeita e preserva movimento ainda válido |
| Pedido de guarda/Dodge durante salto | Pode iniciar se disponível no ar; lançamento/gravidade não são apagados pela troca |
| Falta de Stamina ou estado incompatível | Execução não aceita não paga; zero não se converte em custo de Energia ou dívida |
| Colisão após início aceito | Pode encerrar deslocamento sem devolver custo pago; não concede passagem por obstáculo |
| Domínio elevado | Mastery melhora moderadamente a função conforme M05/M06-04; não remove custo, apoio, colisão ou risco de queda |
| Uso válido para evolução | Mastery de Bringer Light + Proficiência dos fundamentos conforme A; caminhada/pulo vanilla e spam vazio não geram o mesmo progresso |

**Riscos a observar na ficha/validação futura:** impulso útil em corredores e terreno construído; salto que ajude a explorar sem tornar quedas pouco legíveis; prioridade clara entre salto normal/C/D; parâmetros conjuntos que evitem contornar reuso alternando aplicações; comandos de reação que não apaguem velocidade vertical. São requisitos de avaliação, sem resultados de gameplay alegados.

### 16. Vetor, poder principal e combate — consolidação da herança

Este item aplica F1–F13/RAC-23 e os contratos universais; **não cria uma nova decisão G ou uma manifestação universal** para todos os Fullbrings.

| Camada | Regra vigente e fronteira |
|---|---|
| Combate fundamental | Corpo, arma e defesa usam M06-02/06 e suas compatibilidades. Fullbringer não ganha Zanjutsu/Hakuda avançado ou dano espiritual ilimitado por empunhar item vanilla. Não foi criado novo campo de Proficiência corporal. |
| Manipulação geral da matéria | C/D fornecem duas aplicações úteis de mobilidade, conforme F1. O princípio não concede telecinese livre, edição de blocos, radar de minério ou bônus universal de equipamento. Outras técnicas compartilhadas podem ser pesquisadas e especificadas depois; não foram eliminadas do horizonte. |
| Poder principal | Catálogo canônico, aquisição contextual e um principal por vez. Conhecer um personagem, receber um item ou elevar Proficiência não autoriza automaticamente suas capacidades. Técnicas pessoais continuam vinculadas ao poder que as permite. |
| Objeto/equipamento/corpo | Cada ficha respeita o vetor do poder, conforme F4. Não impor foco Quincy, duas mãos ou item sentimental a todos. M11 define representação e acesso; a técnica de M06/M07 declara o que precisa estar disponível para funcionar. |
| Item e progresso | Possuir/trocar/guardar o objeto não transfere por si só aquisição ou Mastery. Não presumir item indestrutível, retorno automático, keepInventory ou perda definitiva do poder. Política de perda/reposição e efeitos sobre técnicas dependentes permanece em M07/M11. |
| Manifestação e recursos | Ativação, guardar, manutenção, passivos, mãos e aplicações são próprios do poder/técnica. C/D não decidem custo de todo Fullbring, nem autorizam copiar manifestação gratuita do arco Quincy ou drenagem de Blut. |
| Formas/completude | M07 define estágios, condições, custos e papel do domínio. Mastery importa por F6; sua exigência não é uma compra em pontos nem uma obrigação de copiar as fases particulares de Ichigo. |
| Troca de poder | Processo especial pendente, sem loadout instantâneo; fundamentos já pertencem ao personagem. Consequência sobre Mastery específica deve ser decidida em M07/M11, não inferida de drop/baú. |
| Mentores, afiliação e era | M08/M12/M13 definem fontes e conteúdo contextual. Xcution não é condição racial nem acesso universal a todos os poderes. Não foi nomeado um mestre obrigatório nesta rodada. |

### 17. Fechamento estrutural de M06-10 — 20/10/2026

**Resultado:** M06-10 pode avançar como `DECIDIDO` **em nível estrutural**, com A–F aprovados e herança consolidada. O módulo M06 permanece `EM ESPECIFICAÇÃO`. Não há nova regra de poder individual sendo aprovada por este fechamento; essa camada conserva fichas e dependências próprias, como nas demais raças.

Rastreabilidade:

- **A:** “Uma Proficiência dos fundamentos + Masteries por técnica (recomendado)”.
- **B:** “Aprendizado próprio com prática (recomendado)”.
- **C:** “Impulso curto direcional (recomendado)”.
- **D:** “Incluir salto impulsionado aprendido (recomendado)”.
- **E:** “Horizontal no chão, ar e água; salto com apoio (recomendado)”.
- **F:** “Reagir preservando custos, tempos e queda (recomendado)”.

Não foi identificado conflito estrutural impeditivo entre A–F e a origem Fullbringer, recursos, aquisição, execução, Postura, combate fundamental ou exclusividade do poder principal. Proficiência não entrega um Fullbring canônico; mobilidade não concede capacidades de Tsukishima/Yukio/Riruka/Chad por analogia. Referências oficiais sustentam os princípios identificados; comandos, custos, meios e permissões aprovados são adaptações do mod. Esta revisão de contratos não acrescentou pesquisa canônica, leitura de capítulos/episódios ou consulta de APIs.

#### Pendências com responsável

| Responsável | Entrega necessária e condição de retorno |
|---|---|
| M06 / M05 / M15 | Fichas executáveis: distâncias, velocidades, altura, custo/reuso, fases, contribuição de Mastery, treino válido e limites conjuntos C/D. Calibrar sem remover as permissões aprovadas por silêncio. |
| M06-11 / M14 | Prioridade entre mobilidade, salto, ataque/guarda/Dodge e modo; feedback de aprendizado/falha/custo/colisão; nenhuma execução ou cobrança duplicada por uma entrada. |
| M06-11 / M10 / M16 | Colisões por meio/apoio, blocos parciais, água/líquidos, gravidade/queda, portais, limites/chunks e estado transitório em morte/logout/dimensão/reinício; preservar conquistas e observar/sincronizar movimento sem reset de risco por falha. |
| M06 / M07 / M11 | Fichas de Fullbrings/vetores: ativação, equipamento/mãos, formas, custos, disponibilidade, perda/reposição e troca de principal. Disponibilidade do vetor e conhecimento do personagem são campos distintos. |
| M06 / M08 / M12 / M13 | Fontes de ensino, exercícios, catálogo de técnicas compartilhadas/individuais e aquisição por era. Regras gerais não equivalem a conteúdo já produzido. |
| M06-11 / M15 | Alvos, dano, armadura/efeitos vanilla, aliados/pets e interação PvE/PvP por técnica; não conceder bônus de ataque por mobilidade ou poder de alteração persistente do mundo sem ficha. |
| M16 | Dados, migração/compatibilidade, implementação e evidências futuras em cliente/servidor/observadores; nenhuma validação de gameplay foi feita nesta rodada. |

A menor amostra já decidida para orientar futuras fichas contém fundamentos/Proficiência e Bringer Light com impulso e salto aprendidos. Não foi selecionado primeiro Fullbring canônico ou eliminado conteúdo futuro. Não é um protótipo pronto nem uma promessa de que números/ensino/construções já foram validados.

**Continuidade:** M06-07/08/09/10 estão fechados estruturalmente; seguir **M06-11 — Auditoria e Integração** para confrontar os quatro caminhos com a base universal e organizar os bloqueadores de especificação. A–F Fullbringer não exigem outra votação. Fichas de linhagem/Grimmjow, artes e implementação continuam fora desta rodada. Não houve alteração de runtime, build, dependências ou saves, nem execução de testes em jogo.

---

# M06-11 — Auditoria e Integração

## Estado do bloco — 21/10/2026

| Campo | Situação |
|---|---|
| Estado | `ESPECIFICADO CONCEITUALMENTE`; integração examinada concluída, com entregas executáveis delegadas na seção 48 |
| Responsável pela decisão | Equipe/usuário, com recomendação de direção de game design |
| Base | M05; M06-01–06; revisões e conclusões de M06-07/08/09/10 |
| Rodada atual | A–Y `DECIDIDOS`; X/Y nas seções 45–46, interrupções na 47 e fechamento conceitual na 48. Próximo: M07 |
| Evidência | Leitura documental dirigida, fonte técnica oficial e pontos locais de implementação; sem teste de gameplay |

### 1. Objetivo e experiência do jogador

Fazer as quatro jornadas funcionarem dentro do mesmo combate e do mesmo mundo Minecraft. O jogador deve entender por que uma ação falhou, o que sua defesa protege e quais riscos continuam presentes ao trocar de técnica, equipamento ou estado.

Este bloco confronta contratos e fecha escolhas de integração do combate. Não aprova todo o catálogo, formas, economia, política de party ou números finais. Uma pendência precisa de responsável e condição de retorno; simplesmente remetê-la a “balanceamento” não resolve seu funcionamento.

### 2. Escopo e limites da revisão

Na abertura, relidos os contratos universais de M06-01–06, as revisões de Proficiência, vínculo/empunhadura e Hohō/Kidō Shinigami, os contratos pertinentes de Sonído/Hierro/Pesquisa/regeneração, Blut e as conclusões dos quatro caminhos, além de Fullbringer E/F. Foram confrontados com recursos, mitigação, aprendizado e responsabilidades de M05 e com as revisões raciais já registradas. Isso não é uma nova leitura integral dos milhares de linhas de todos os módulos, pesquisa de todas as exceções de Bleach ou auditoria exaustiva do código.

As referências de Bleach já registradas sustentam as identidades das capacidades. A/B/C abaixo são adaptações do mod aprovadas por respostas explícitas, sem afirmar que a obra tenha uma fórmula universal de defesa ou que Vene neutralize todo veneno/fogo. Não houve nova consulta de capítulos ou episódios nesta abertura.

**Verificação técnica de apoio em 20/10/2026:** [notas oficiais de Minecraft Java 1.19.4, seção Damage Types](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-19-4), MC-11 na base datada. A publicação descreve tipos de dano definidos por dados e tags que governam sua interpretação. É referência histórica anterior ao alvo local 1.20.1, não recomendação de migração, fórmula do mod ou prova das tags de cada ataque existente. A consulta ao snapshot 23w06a foi apenas complementar; usar a publicação da versão final como referência principal.

Conferidos `gradle.properties` e a declaração de toolchain em `build.gradle`: alvo permanece Minecraft 1.20.1 / Forge 47.4.10 / Java 17. Lido integralmente [CombatEvents.java](../../src/main/java/com/bleachmod/server/events/CombatEvents.java): há redução por passivas selecionadas e um ramo de Resistência limitado a golpes físicos diretos identificados pelo código. Esse recorte não comprova a integração geral projetada nem revisa M05; não concluir a cobertura de todos os ataques olhando apenas esse arquivo. Nenhum código foi alterado.

### 3. Confronto dos contratos existentes

| Tema | Herança conferida | Resultado e limite |
|---|---|---|
| Formação geral | M06-07B/B.1/B.2, M06-08A/B, M06-09C, M06-10A | Quatro disciplinas Shinigami; três campos Hollow/Arrancar; três Quincy; um Fullbringer. Quantidades diferentes são intencionais, não contradição ou motivo para criar indicadores extras. |
| Aquisição e domínio | M06-03, M06-07 e aplicações raciais | Proficiência indica formação; não compra nem substitui a técnica. Primeira aprendizagem pode usar fundamentos/tentativa assistida. Não exige Mastery de uma capacidade ainda inacessível. |
| Fundamento e técnica | M06-07, seções 30–31; tiro Quincy; Bringer Light | Tiro comum não precisa de Mastery paralela; aplicações de impulso/salto compartilham a Mastery de Bringer Light. Não padronizar “uma Mastery por botão”. |
| Poder principal e instrumento | M06-02AD, M06-07V–Y, M06-09J/L, M06-10 seção 16 | Arma, foco e vetor cumprem funções diferentes. Receber item não transfere vínculo/conhecimento; foco Quincy e duas mãos não são requisitos universais dos Fullbrings. |
| Recursos | M05 e M06-05 | Mobilidades usam Stamina; emissões e outras capacidades usam o custo próprio. Gintō preparado usa conteúdo já financiado; Energia atual zero não elimina essa provisão. Nenhum deles ganha uma segunda barra racial. |
| Carga e reação | M06-08K; M06-09D/H/I | Cero cancelado exige recuperação; arco cancelado antes de emitir não tem recuperação adicional. Flecha emitida mantém recuperação ofensiva mesmo permitindo reações. São permissões específicas compatíveis com M06-05R/06I, não autorização geral de cancelar toda técnica. |
| Saída de mobilidade | M06-09O e M06-10F | Hirenkyaku/Bringer Light horizontal param para ação disponível; pedido inválido mantém movimento ainda válido. Shunpo e Sonído não receberam por analogia todas essas permissões. |
| Ar, água e salto | M06-09P, M06-10D/E e limites de Hohō/Sonído | Hirenkyaku/Bringer Light horizontal preservam gravidade/queda; salto Fullbringer exige apoio. Meios e fases de Shunpo/Sonído ainda precisam de complemento; não preencher com voo nem igualar silenciosamente as quatro técnicas. |
| Defesa e controle | M06-02S/T, M06-06, M06-08J/09R | Receber dano, mitigar Vida, perder Postura e sofrer controle são resultados distintos. Hierro/Vene não concedem Super Armor, guarda ou imunidade a Guard Break automaticamente. |
| Percepção | M05 seções 37–40; M06-08J | Pesquisa usa o domínio de Percepção Espiritual; nomes diferentes não duplicam ganho da mesma leitura. Percepção/Supressão/Liberação universais ainda precisam de contratos próprios; Pesquisa não resolve todo esse repertório. |
| Modo e estado de combate | M06-01B/C, M06-09L/Q/S | Desligar a interface dissipa arco/Blut conforme suas regras, mas não encerra Combat State nem autoriza recarga de Gintō durante luta. |
| Prática e persistência | M05/M06-04 e conclusões raciais | Uso válido pode desenvolver Proficiência e Mastery pertinentes; AFK ou gasto vazio não bastam. Conquistas e execuções transitórias são dados diferentes; duração offline, efeitos emitidos e preparações no mundo ainda exigem política explícita. |

**Parecer desta abertura:** não foi identificado conflito que exija revogar as aprovações raciais examinadas. Há ambiguidades de leitura e lacunas de integração reais. A conclusão não declara ausência de toda omissão ou prontidão para implementar M06 inteiro.

### 4. Correções de leitura e pendências identificadas

| Registro | Achado | Tratamento desta rodada |
|---|---|---|
| AUD-M06-01 — correção documental | M06-02Y ainda apresenta desenvolvimento sem Proficiência, superado por M06-07 | Nota explícita junto ao texto original; preserva o histórico e aponta B/B.1/B.2 e seção 33. Não é nova decisão. |
| AUD-M06-02 — continuidade | Algumas sínteses do handoff ainda apontam Fullbringer como próximo; a lista inicial omitia fichas Shinigami pendentes | Atualizar síntese vigente e tabela; marcar encaminhamentos anteriores como históricos. Os quatro fechamentos continuam estruturais. |
| AUD-M06-03 — alinhamento do mapa | Contrato de fronteira ainda falava em moeda que melhora técnicas e em árvores raciais, apesar das revisões M05/M06 | Redação alinhada a pontos principalmente nos atributos e repertórios; sem nova compra de técnicas ou skill tree. |
| INT-M06-01 — estrutura decidida | Integração de armadura, composição defensiva e dano contínuo de Vene | A/B/C aprovados, seções 5–7; revisão na 10. Fichas, categorias completas, ordem técnica e números continuam pendentes. |
| INT-M06-02 — estrutura complementada | Shunpo/Sonído tinham meios/permissões menos detalhados que Quincy/Fullbringer | D/E aprovados nas seções 12–13; revisão na 15. Fases executáveis/números ainda pendentes; M07 trata sustentação/formas, M10 travessias. |
| INT-M06-03 — repertório universal, complementado | F–N aprovados e revisados nas seções 21/27/32: percepção, ocultação, oposição e manutenção/uso da Liberação | O/P/Q complementam alcance, alvos e manifestação Natural nas seções 33–36. R/S/T complementam trocas, Blut e pagamentos nas seções 37–40. U/V/W complementam ciclo de vida e efeitos nas seções 41–44. X/Y e seção 47 consolidam modo/interrupções. Fichas e dependências executáveis na seção 48; M14 apresenta, M15 calibra, M08/M12/M13 fornecem ensino. Não conceder catálogo especializado por inferência. |
| INT-M06-04 — ciclo de vida complementado | U/V/W aprovados nas seções 41–44: execução encerrada, tempos preservados pelo relógio do mundo e distinção de efeitos autônomos/sustentados/persistentes | Modo/interrupções consolidados nas seções 45–47; faltam fichas e tradução técnica. M07 formas; M10/M11 montagens/mundo/instrumentos; M15 antidesconexão; M16 relógio/migração/implementação. Reset de cooldown encontrado no código diverge de V. |
| INT-M06-05 — fronteiras externas | Alvos, aliados/pets, mãos, equipamentos e integração vanilla exigem entregas concretas | M06 declara efeito/alvo permitido por técnica; M11 equipamento/reposição; M14 comandos; M15 política PvE/PvP/party; M16 tradução e evidências. Decisão local não fecha esses módulos inteiros. |

### 5. M06-11A — DECIDIDO — Equipamento útil e exceções explícitas de dano

**Aprovado em 20/10/2026.** Resposta explícita: “Proteções compatíveis; exceções por técnica (recomendado)”. A alternativa apresentada não foi escolhida; parâmetros, fichas e integrações delegados continuam pendentes.

**Experiência aprovada:** equipamento compatível continua tendo valor quando o jogador entra no combate espiritual. Ser uma flecha espiritual, Cero ou Kidō não significa automaticamente ignorar armadura, encantamentos ou a Resistência do personagem.

| Alternativa | Consequência |
|---|---|
| **Proteções elegíveis funcionam; exceções por técnica — recomendada** | Preserva preparação/equipamento e uma regra comum. Ataques especiais podem contornar proteções específicas quando sua ficha justificar e comunicar. |
| Separação geral: dano espiritual ignora armadura vanilla | Dá prioridade à defesa espiritual, mas reduz o valor do equipamento contra grande parte do combate e exige sustentar essa divisão no conteúdo. |

Contrato estrutural aprovado:

1. Ataques diretos físicos e espirituais do mod podem receber mitigação do atributo Resistência e de armadura/encantamentos/efeitos compatíveis. A regra-base é não criar uma exclusão apenas pelo rótulo “espiritual”. Percentuais e curvas continuam para M05/M15.
2. Cada componente declara se atinge corpo, área ou outro alvo, e quais proteções pode contornar. A não define quais ataques a guarda básica consegue bloquear: bloqueabilidade, direção/cobertura e pressão continuam próprias da ficha e do contrato de guarda.
3. Encantamento ou efeito só atua no evento que sua regra cobre. Não transformar Proteção contra Fogo em proteção de todo ataque com partículas vermelhas ou transformar uma técnica em dano mágico vanilla apenas pelo nome.
4. Dano ambiental/vanilla mantém suas regras específicas até revisão identificada. A decisão não concede ao atributo Resistência redução universal de vazio, fome, afogamento ou qualquer perda de Vida.
5. Não concede a todo corpo/forma todos os slots de armadura. Compatibilidade de vestir/usar equipamento pertence a M07/M11; sua eficácia depende de estar legitimamente disponível.
6. Perfurar armadura não significa ignorar também Resistência, Vene, Hierro, guarda e encantamentos. Cada exceção precisa de alcance explícito; dano especial não é uma chave de bypass total.
7. O recorte físico atualmente encontrado em CombatEvents é uma diferença a conciliar na implementação futura conforme A aprovada, não autorização para alterar código agora. Alvos vanilla, mobs espirituais e jogadores precisam de fichas coerentes; esta decisão não estabelece escala automática do mundo.

### 6. M06-11B — DECIDIDO — Compor defesas sem somar percentuais até imunidade

**Aprovado em 20/10/2026.** Resposta explícita: “Reduções sucessivas com limite conjunto (recomendado)”. A alternativa apresentada não foi escolhida; parâmetros, fichas e integrações delegados continuam pendentes.

**Herança já decidida:** Resistência não cresce até 100% de redução; Hierro/Vene não anulam todo dano por acumulação; Postura pertence exclusivamente à guarda. A escolha nova é como defesas independentes contribuem juntas.

| Alternativa | Consequência |
|---|---|
| **Reduções sucessivas sobre o dano restante, com limite conjunto — recomendada** | Equipamento, preparo corporal e reforço compatíveis podem contribuir. Cada redução atua no restante; ganhos acumulados são controlados. |
| Usar somente a maior proteção adicional | Facilita conter acumulação, mas pode tornar defesa treinada/equipamento irrelevante enquanto outra proteção for superior. |

Contrato estrutural aprovado:

- Resolver validade do impacto e da guarda; calcular pressão de Postura conforme o ataque bloqueado. A parcela que chega ao corpo recebe somente as proteções elegíveis, uma vez cada.
- Mitigações percentuais independentes se compõem sucessivamente, não por soma direta. Limite conjunto de mitigação corporal e curvas serão calibrados em M05/M15 para impedir imunidade produzida apenas pela acumulação.
- Não é fórmula linear para armadura nem uma ordem técnica definitiva de eventos Forge. A ordem completa entre armadura, encantamentos, efeitos, absorção e componentes do mod ainda requer especificação M06/M11/M15/M16; não calcular uma proteção e aplicá-la de novo pelo caminho vanilla.
- Capacidades incompatíveis não passam a coexistir; Vene/Arterie continuam exclusivas. Repetir a mesma aplicação não cria camadas independentes; variantes/substituições de uma proteção precisam de regra própria.
- Mitigação corporal não diminui automaticamente a pressão sofrida pela guarda. Reduzir dano de Vida com Hierro/Vene não transforma todo bloqueio em custo zero de Postura. Uma técnica pode alterar pressão se sua ficha realmente conceder isso.
- O limite aprovado é de acumulação corporal; não cria dano mínimo universal, não proíbe um bloqueio total que a ficha permita nem concede imunidade a controle. Nenhum valor de redução/teto/dano foi escolhido.

### 7. M06-11C — DECIDIDO — Vene no impacto; condições posteriores têm regras próprias

**Aprovado em 20/10/2026.** Resposta explícita: “Vene no impacto; condições separadas (recomendado)”. A alternativa apresentada não foi escolhida; parâmetros, fichas e integrações delegados continuam pendentes.

**Experiência aprovada:** Vene pode reduzir o dano do ataque que atingiu o personagem. Uma queimadura ou um veneno aplicado depois continua perigoso; manter Vene não os cura nem os neutraliza automaticamente.

| Evento | Recorte aprovado de Vene |
|---|---|
| Golpe corporal/armado, flecha, emissão ou explosão ofensiva elegível | Mitigar o dano do impacto enquanto Vene está ativo e pago, conforme R e composição aprovada |
| Impacto de ataque flamejante ou que aplica veneno | Mitigar a parcela direta elegível; aplicação da condição é uma operação distinta |
| Queimadura, veneno ou outra condição já aplicada | Sem mitigação adicional automática de Vene nos danos posteriores; sem cura/limpeza da condição |
| Canalização/área que continua causando impactos de ataque | Cada evento pode ser impacto elegível conforme ficha; duração contínua da técnica não a transforma automaticamente em veneno/queimadura residual |
| Fogo/lava do ambiente, queda, fome, afogamento, sufocamento ou vazio | Sem cobertura geral de Vene, preservando as exclusões ambientais de R |

**Alternativa:** Vene também reduz dano posterior de condições ofensivas expressamente classificadas. Pode ampliar sua utilidade, mas exige decidir quais condições entram e mantê-las distintas de fogo/veneno ambiental; não basta procurar se há um atacante registrado. Foi escolhido proteger o impacto e reservar resistência a condições para capacidades que realmente a declarem.

Contrato estrutural aprovado: dano, aplicação de condição e controle são componentes distintos. Reduzir um não cancela automaticamente os outros. Ausência de cobertura de Vene não desativa uma proteção vanilla que seja válida, nem decide por associação toda cobertura de Hierro ou do atributo Resistência. Essas defesas continuam exigindo suas tabelas; nenhuma resistência/cura especial foi concedida.

A decisão delimita o padrão adicional de Vene, sem alterar sua seleção manual, manutenção, troca, Mastery ou permissões de ataque. Os recortes específicos de fogo/veneno/dano contínuo estavam pendentes em M06-09R; C completa esse recorte estrutural por resposta explícita, com classificação/fichas e parâmetros ainda delegados.

### 8. Critérios futuros de aceitação e responsáveis

Os resultados abaixo são **critérios futuros de aceitação dos contratos A/B/C aprovados**; não são testes executados:

| Situação | Resultado esperado pelos contratos aprovados | Responsável |
|---|---|---|
| Mesmo ataque espiritual com/sem armadura elegível | Equipamento contribui, salvo exceção explícita; não é ignorado apenas por ser espiritual | M06/M11/M15/M16 |
| Arma/ataque que contorna uma defesa específica | Ignora somente o que a ficha declara; demais proteções elegíveis continuam consideradas | M06/M11/M15 |
| Combinar Resistência, armadura e Vene válidos | Reduções pertinentes uma vez; sem soma até imunidade ou reaplicação pelo pipeline vanilla | M05/M06/M11/M15/M16 |
| Bloquear com Hierro/Vene ativos | Proteção corporal não elimina pressão de Postura automaticamente; Guard Break permanece possível | M06/M15 |
| Receber ataque flamejante e continuar queimando | Separar impacto elegível e condição posterior; Vene não limpa nem protege automaticamente essa condição | M06/M11/M15/M16 |
| Sustentar uma emissão que acerta repetidamente | Classificar eventos ofensivos pela ficha, sem tratar todo dano ao longo do tempo como condição residual | M06/M15/M16 |
| Energia acabar antes de um novo impacto | Vene já encerrado não protege por efeito visual atrasado; custos e pagamentos anteriores permanecem | M06/M14/M16 |

A aprovação estrutural não fixa uma lista de ataques, canais, encantamentos ou percentuais. M06 deve fornecer ficha de componentes/elegibilidade; M11 integra equipamento/condições; M15 calibra o conjunto e as políticas de alvos; M16 implementa e produz evidências futuras. A distinção física/espiritual também não decide sozinha quem pode perceber ou atingir entidades espirituais: compatibilidade racial/alvo continua responsabilidade dos contratos pertinentes.

### 9. Continuidade

**A/B/C aprovados por três respostas explícitas em 20/10/2026**, registradas em cada decisão. Não exigem nova votação. Não reabrir as quatro estruturas raciais; preservar suas diferenças e as restrições de M06-08K, M06-09D/I/O/P/Q/R e M06-10D/E/F.

Depois de dano/defesa: complementar mobilidade Shunpo/Sonído, tratar o repertório universal de Reiatsu e fechar os contratos de estados/concorrência/ciclo de vida com responsabilidades externas claras. Somente então avaliar o fechamento conceitual de M06 e a passagem para M07. Números, catálogos e entregas posteriores continuam pendentes identificadas, sem transformar todas essas dependências em pré-requisito para discutir o próximo módulo.

Linhagens/Grimmjow, formas específicas, fan arts e implementação continuam adiadas. Esta rodada não altera runtime, dependências ou saves, nem executa build ou testes de Minecraft.

### 10. Registro das respostas A/B/C e revisão conjunta — 20/10/2026

**Três respostas explícitas recebidas e registradas:** A — “Proteções compatíveis; exceções por técnica (recomendado)”; B — “Reduções sucessivas com limite conjunto (recomendado)”; C — “Vene no impacto; condições separadas (recomendado)”. A separação geral de dano espiritual da armadura vanilla, a regra de usar somente a maior proteção adicional e a cobertura adicional de condições residuais por Vene não foram escolhidas.

| Relação conferida | Resultado documental |
|---|---|
| A com M05/Resistência | A completa a elegibilidade de ataques diretos físicos/espirituais. Mantém a curva limitada e as proteções compatíveis, sem conceder redução de toda perda ambiental. |
| B com M06-06/Postura | Mitigações corporais não substituem a guarda. Um bloqueio pode receber pressão de Postura mesmo quando o dano de Vida restante é pequeno ou zero; não calcular toda pressão a partir da Vida efetivamente perdida. Valores e cálculo da pressão continuam pendentes. |
| B com Hierro/Vene | Contribuições independentes elegíveis podem coexistir, uma vez cada. Defesas incompatíveis ou aplicações repetidas não viram novas camadas; o teto de acumulação corporal não transforma todo ataque em dano mínimo obrigatório. |
| C com Blut Q/R | Vene precisa estar válido, ativo e pago no impacto. Não desfaz dano anterior, não cura e não preserva proteção após esgotar Energia. Seleção/troca e Mastery permanecem. |
| C com ataque composto | Impacto, aplicação de condição e controle têm resoluções próprias. A redução do impacto não cancela automaticamente veneno/queimadura ou controle; se guarda/barreira impede a aplicação da condição é regra da capacidade/impacto, ainda a declarar na ficha. |
| C com canalização/área | Pulsos de ataque podem ser impactos elegíveis. Sua frequência e quantidade vêm da ficha/execução, sem multiplicar acertos por quadro visual, componente duplicado ou chamada repetida do mesmo evento. Não confundir com dano residual de condição já aplicada. |
| A/B com Minecraft | Equipamento e efeitos elegíveis continuam no cálculo; integrar uma vez ao fluxo vanilla. Não substituir todas as tags/regras de dano do jogo por uma categoria única de dano espiritual. |

**Resultado:** não foi identificado conflito que exija rever A/B/C ou as aprovações raciais relacionadas. Essa revisão de contratos locais não trouxe nova pesquisa canônica, não testou o cálculo e não especificou todas as exceções. A/B/C estão decididos estruturalmente; ordem técnica completa, tabela de componentes/defesas, armadura/encantamentos/absorção, teto/curvas e valores continuam com M05/M06/M11/M15/M16. Não foi aprovado dano aliado, escala de mobs ou equipamento universal para todos os corpos.

**Continuidade:** complementar a mobilidade básica Shunpo/Sonído, conforme seções 11–14 abaixo. As três decisões de dano não exigem outra votação.

### 11. Complemento de Shunpo e Sonído — base e recomendação — 20/10/2026

**Herança preservada:** M05 define Stamina como recurso principal; M06-06 separa Dodge/mobilidade aprendida e permite exploração; M06-07HO-A–H define Hohō, Shunpo inicial com Mastery própria, direção, custo, Recovery/cooldown e evolução moderada; M06-08I define Sonído Arrancar direcional, percurso/corpo e colisão, sem teleporte ou voo concedido. Nada disso exige nova votação.

**Lacuna da integração:** meios básicos, momento de pagamento de Shunpo e saídas para outras ações ainda não tinham o mesmo grau de definição dos blocos Quincy/Fullbringer. D/E completam esses pontos estruturalmente por respostas explícitas. Shunpo e Sonído continuam técnicas diferentes, com aquisição, Proficiência, Mastery, valores, animação/som e aplicações próprias. Compartilhar uma regra de comando/percurso não concede uma técnica racial a outro caminho nem iguala desempenho.

**Fonte reconsultada em 20/10/2026:** [glossário oficial TYBW](https://bleach-anime.com/keyword/), BL-03, entradas Hirenkyaku (`飛廉脚`) e Arrancar (`破面`). Identifica Shunpo e Sonído como técnicas semelhantes de movimento rápido e Sonído como capacidade Arrancar. Texto japonês interpretado, sem capítulo/episódio integral. A fonte não decide impulso horizontal em água/ar, pagamento de Stamina ou reação/cancelamento. Esses pontos abaixo são adaptações aprovadas para as versões básicas, não limites canônicos atribuídos à obra.

Recomendo uma base que permita reposicionamento rápido no espaço disponível, inclusive durante salto/queda, preservando o risco ambiental. Verticalidade livre, sustentação e aplicações especiais precisam de contratos próprios; o objetivo de D não é encerrar o combate aéreo de Bleach nem atribuir voo a todo iniciante.

### 12. M06-11D — DECIDIDO — Percurso e pagamento dos impulsos básicos Shunpo/Sonído

**Aprovado em 20/10/2026.** Resposta explícita: “Horizontal no chão, ar e água (recomendado)”. A alternativa apresentada não foi escolhida; valores, fichas e integrações delegados continuam pendentes.

| Alternativa | Consequência |
|---|---|
| **Horizontal no chão, ar e água; Stamina no início — recomendada** | Mantém uso útil em combate/exploração e durante uma queda, com custo e riscos legíveis; não exige pousar para todo reposicionamento. |
| Restringir essas versões básicas ao chão | Simplifica a primeira integração, mas impede reposicionamento em salto/queda e na água até outra aquisição. |

Contrato estrutural aprovado de D:

1. Técnica aprendida, natureza/estado e recurso válidos. Um comando dedicado inicia um impulso curto na direção horizontal indicada pelo movimento, com frente horizontal como referência quando não houver direção. Olhar para cima não lança para cima; segurar o comando não mantém voo nem repete automaticamente.
2. Permitir início no chão, no ar e na água, respeitando o corpo e o meio. No ar, preservar gravidade, velocidade vertical e queda acumulada. Não criar apoio espiritual, segundo salto vertical, sustentação ou reset de queda por ativar/repetir a técnica.
3. Cobrar Stamina uma vez no início aceito, sem Energia adicional para essas versões básicas. D concretiza esse momento para Shunpo e consolida o custo de ativação já descrito de Sonído. Energia zero não impede o uso se as demais condições estiverem satisfeitas.
4. Validar percurso e trecho útil antes de aceitar/cobrar. Sem trecho útil, rejeitar com aviso e sem custo. Depois de iniciado, obstáculo/encerramento do trajeto não devolve automaticamente o custo. Limiar, velocidade, distância e duração permanecem para ficha/calibração; não exigir destino completo livre para executar qualquer trecho seguro.
5. Colisão considera volume corporal durante todo o trajeto, incluindo construções, teto e blocos parciais. Não atravessar paredes, encolher o corpo, causar dano de contato ou remover blocos. Sonído continua limitado à natureza compatível; D não o concede ao Gillian por mencionar corpos grandes.
6. Na água, preservar física e riscos do meio; sem piso aquático, respiração extra, imunidade a afogamento/lava ou subida automática. Alteração legítima da queda pelo meio permanece; o comando não produz limpeza artificial do risco.
7. Não garante invulnerabilidade, Super Armor, desaparecimento mecânico da percepção/mira ou quebra de ataque inimigo. Apresentação rápida não equivale a essas propriedades; capacidades especiais precisam de ficha própria.
8. Limites do mundo e região carregada permanecem; sem carregar terreno remoto, criar portal ou trocar de dimensão pela técnica. Portais, escadas, apoios especiais e colisões concretas continuam em M06-11/M10/M16. Formas/sustentação em M07 não ficam concedidas nem proibidas por D.

D complementa M06-07HO-H/M06-08I; não altera aquisição, Proficiências/Masteries ou o uso dentro/fora de combate já aprovado. Não aprova todos os valores/reusos nem copia o salto aprendido Fullbringer para essas técnicas.

### 13. M06-11E — DECIDIDO — Encerrar o impulso Shunpo/Sonído para reagir

**Aprovado em 20/10/2026.** Resposta explícita: “Parar para reagir, preservando custos e tempos (recomendado)”. A alternativa apresentada não foi escolhida; valores, fichas e integrações delegados continuam pendentes.

| Alternativa | Consequência |
|---|---|
| **Parar para ação disponível, sem recuperação extra só pela parada — recomendada** | Permite responder à ameaça e aproveitar uma posição alcançada; não mantém simultaneamente o avanço especial e o ataque/guarda. |
| Exigir terminar o impulso antes dessas ações | Aumenta comprometimento do deslocamento, mas pode dificultar responder após uma colisão ou mudança próxima de posição. |

Contrato estrutural aprovado de E:

- Ataque, bloqueio ou Dodge disponível pode encerrar a propulsão para começar. O tempo restante do impulso não é, sozinho, impedimento para essa reação; não adicionar recuperação apenas pelo cancelamento do deslocamento.
- Validar a nova ação antes de abandonar a atual. Pedido indisponível é rejeitado com feedback e mantém o impulso enquanto ele ainda for válido; não estender duração, renovar custo ou enfileirar ação para depois.
- Custos pagos, cooldown/reuso e recuperações anteriores permanecem. Ofensiva ainda impedida por recuperação anterior continua indisponível; parar o avanço não permite ignorar o ataque/técnica usada antes dele.
- A recuperação própria de Shunpo/Sonído e as permissões das fases precisam ser especificadas respeitando essa saída. E refina o compromisso de execução previsto em HO-H/I: não apagar seus tempos nem reinterpretá-los depois como obrigação de terminar todo o impulso para qualquer reação, conforme E aprovada.
- Se uma reação puder ocorrer no ar, ela aplica seu movimento legítimo sem suspender gravidade, zerar queda ou restaurar apoio artificialmente. E não concede permissão aérea a uma ação que não a possua.
- E regula saída dessas mobilidades, não entrada durante toda técnica anterior. Cero continua exigindo cancelar carga e respeitar sua recuperação; arco mantém D/I próprios; Kidō, combos e poderes consultam suas fases. Conhecer Shunpo/Sonído não cria um cancelamento universal.
- Não concede golpe de teleporte, ataque durante a propulsão ou técnica contextual desconhecida. Repetir mobilidade exige novo pedido válido, custos e reuso próprios.

### 14. Critérios futuros de aceitação, pendências e continuidade

D/E estão **decididos por duas respostas próprias**, registradas nas seções 12–13; não foram aprovados por associação às regras Quincy/Fullbringer ou às respostas de dano A/B/C. Não aplicar o mesmo custo, alcance, velocidade ou efeito visual às quatro mobilidades por padronização.

| Situação futura | Resultado esperado pelos contratos D/E aprovados |
|---|---|
| Usar Shunpo/Sonído durante queda | Avanço horizontal, pagamento único aceito e queda preservada; sem voo por repetição |
| Trajeto parcialmente bloqueado | Usar trecho seguro útil se houver; nunca atravessar o obstáculo ou verificar só o destino final |
| Falta de Stamina/nenhum trecho útil | Rejeitar sem iniciar/cobrar; comunicar o motivo |
| Impulso iniciado encontra obstáculo | Encerrar/limitar ao trecho seguro, sem reembolso automático nem encolhimento corporal |
| Ataque disponível durante avanço | Encerrar propulsão e iniciar ataque; não manter avanço especial junto do golpe |
| Ataque impedido por recuperação anterior | Rejeitar e preservar impulso ainda válido; não apagar tempos ou agendar ataque |
| Pedir Sonído durante carga de Cero | Aplicar K do Cero, incluindo recuperação de cancelamento; E não libera saída imediata da carga |
| Executar em água ou reagir no ar | Preservar o meio/queda e requisitos da ação; sem respiração, apoio ou imunidade novos |

M06/M05/M15 fecham números, fases/reuso e treino válido de cada técnica; M14 concretiza comando/feedback; M06-11/M10/M16 tratam percurso, meios, portais, limites e ciclo de vida. Novos números ou efeitos especiais não estão aprovados. Os cenários são critérios futuros, sem teste de gameplay.

**Respostas D/E recebidas e revisão realizada na seção 15.** O próximo trabalho é desenvolver Percepção/Supressão/Liberação; depois estados/concorrência/ciclo de vida, antes de avaliar o fechamento conceitual de M06 e avanço a M07. Linhagens, artes e implementação continuam adiadas.

### 15. Registro de D/E e revisão das quatro mobilidades — 20/10/2026

**Respostas próprias:** D — “Horizontal no chão, ar e água (recomendado)”; E — “Parar para reagir, preservando custos e tempos (recomendado)”. Restrição das versões básicas ao chão e espera obrigatória até encerrar o impulso não foram escolhidas. Não voltar a votar direção, recurso, meios ou saídas já aprovados.

| Capacidade | Formação/domínio preservados | Percurso básico e saída após estas decisões |
|---|---|---|
| Shunpo | Hohō com Proficiência; técnica aprendida relativamente inicial e Mastery própria, HO-A–H | D/E completam impulso horizontal em chão/ar/água e saída para ação disponível; custo/reuso/fases específicos |
| Sonído | Formação Arrancar e Proficiência de mobilidade; aquisição e Mastery próprias, M06-08I | D/E completam meios e reações; conserva corpo/percurso válido e custo de ativação |
| Hirenkyaku | Formação Quincy, aprendizado próprio e Proficiência de mobilidade; Mastery da técnica, M06-09M–P | Contrato anterior preservado: impulso horizontal, Stamina no início, meios/queda e parar para agir |
| Bringer Light | Fundamentos Fullbringer com Proficiência; aprendizado próprio e Mastery compartilhada de suas aplicações, M06-10A–F | Impulso horizontal nos três meios; salto aprendido com apoio real e custo próprio, sem concedê-lo às outras mobilidades |

**Conferência das interações:**

- A existência de meios/saídas comuns não determina igualdade de alcance, velocidade, custo, reuso ou identidade. Cada técnica mantém ficha, aprendizado, expressão e desenvolvimento próprios; não criar uma “Mastery de dash” universal.
- Um ataque disponível pode encerrar o impulso conforme E; recuperação ofensiva anterior ainda torna o pedido indisponível. A reação autorizada não pode ser bloqueada somente pelo tempo que faltava para terminar o avanço, e parar não reinicia os tempos pagos/iniciados. A ficha deve declarar as permissões de reação e o efeito do Recovery próprio sem reintroduzir a espera universal rejeitada em E.
- Cero → Sonído continua seguindo K do Cero: cancelar a carga, perder preparo e respeitar recuperação antes de iniciar a mobilidade. E não revisa a capacidade de origem. Arco → Hirenkyaku mantém a permissão própria sem recuperação adicional de cancelamento antes do tiro; a diferença é intencional.
- Água/ar não concedem sustentação, mudança de dimensão, entrada garantida em todo portal ou proteção ambiental. Não atribuir às mobilidades uma proteção que A/B/C só regula para eventos de dano elegíveis.
- Blut já ativo e compatível mantém suas regras durante Hirenkyaku; Hierro permanece passivo compatível durante Sonído. Nenhum desses estados elimina custos, colisões, queda ou exige/permite guarda simultânea ao impulso.
- M06-05P já permite reuso compartilhado entre variações da mesma habilidade quando necessário para impedir contorno. Isso pode ser relevante para impulso/salto de Bringer Light; não aprova um cooldown global entre raças nem um segundo custo automático para a mesma entrada.

**Resultado documental:** não foi identificado conflito que exija rever D/E ou as decisões de Hirenkyaku/Bringer Light. Os cinco registros A–E estão aprovados estruturalmente. Ainda faltam fases e parâmetros executáveis, prioridade de comandos, colisões concretas/meios/apoios, política de portais/chunks e ciclo de vida, com os responsáveis da seção 14 e das conclusões raciais. Não houve nova implementação ou teste de gameplay.

**Próximo bloco de trabalho em M06-11:** Percepção Espiritual, Supressão de Reiatsu e Liberação de Reiatsu. M05 já aprova esses fundamentos como capacidades treináveis: não perguntar novamente se existem, nem converter Pesquisa em nova Mastery. A próxima rodada deve definir o que o jogador faz, recebe de informação e paga/mantém; oposição entre perceber/suprimir e alcance/limites precisam de contrato próprio. Números e ensino concreto seguem para M05/M15 e M08/M12/M13; nenhum modelo de ativação/custo novo está aprovado por esta indicação de continuidade.

Depois vêm estados/concorrência/ciclo de vida e a revisão de fechamento conceitual de M06. Não há pergunta pendente de A–E. M06 permanece `EM ESPECIFICAÇÃO`; M07, fichas de linhagens/Grimmjow, artes e implementação ainda não foram iniciados nesta rodada.


### 16. Percepção Espiritual — preparação e limites da evidência — 20/10/2026

**Estado atualizado em 20/10/2026:** M06-11F/G/H `DECIDIDOS` por três respostas próprias, registradas em cada decisão e revisadas na seção 21. Na preparação eram propostas; a autorização para continuar, por si só, não as aprovou. A–E permanecem decididos.

**Experiência pretendida:** perceber que há algo espiritual próximo, decidir investigar e obter pistas úteis sem receber automaticamente a posição exata de cada entidade. Alguém que esconde sua presença ainda pode escapar de uma leitura; alguém percebido não fica automaticamente visível ou vulnerável a ataques.

**Herança lida nesta preparação:** M05, seções 30–40 e 51, com números pendentes na 57; M06-01 e regras de execução/reação já confrontadas em M06-11; M06-08J/Pesquisa. Percepção natural vaga, capacidade treinável, Controle, Mastery, assinatura familiar e oposição por Supressão já existem no design. Não criar Proficiência universal de Reiatsu, segundo atributo sensorial ou Mastery paralela de Pesquisa. As aplicações de Supressão/Liberação e o catálogo de refinamentos da seção 39 de M05 ainda não estão especificados por este bloco.

**Pesquisa oficial em 20/10/2026:**

- Reconsultado [Pierrot, glossário ha](https://pierrot.jp/title/bleach/keyword_ha.html), BL-11, entrada `探査神経 / ペスキス`: relaciona Pesquisa à leitura de Reiatsu e características da alma. Sustenta a identidade sensorial; não fixa botões, custo, duração, alcance ou precisão do mod.
- Consultado [Pierrot, glossário ra](https://pierrot.jp/title/bleach/keyword_ra.html), BL-19, entrada `霊絡`: descreve uma manifestação visual específica relacionada à energia espiritual. Serve para distinguir técnicas sensoriais particulares do fundamento geral; não concede Reiraku automaticamente às quatro raças nem transforma toda percepção em fios visíveis.
- O glossário atual TYBW (BL-03) também foi aberto; esta rodada não utiliza suas outras entradas para acrescentar regras de percepção ou conteúdos recentes ao mod. Os resumos japoneses acima foram interpretados; não houve leitura integral de capítulos/episódios ou verificação de todas as exceções.

O modelo passivo/ativo e as escolhas de informação, pagamento e reação são **adaptações aprovadas por três respostas explícitas**, sem atribuí-las à obra. A busca local por nomes relacionados a Pesquisa/percepção/supressão não localizou um contrato implementado correspondente nesses nomes; isso não é auditoria exaustiva nem prova de ausência de funcionalidades equivalentes. Nenhum código foi alterado ou testado.

### 17. M06-11F — DECIDIDO — Percepção natural e leitura breve aprendida

**Aprovado em 20/10/2026.** Resposta explícita: “Percepção natural + leitura breve aprendida (recomendado)”. A alternativa apresentada não foi escolhida; parâmetros e integrações delegados continuam pendentes.

**Pergunta enviada:** adotar percepção natural sem custo, aprimorada pelo domínio, e uma leitura breve aprendida, com a mesma Mastery e pequeno custo de Energia na conclusão, mesmo sem presença detectável; cancelar antes não cobra nem entrega resultado.

| Alternativa | Efeito para o jogador |
|---|---|
| **Percepção natural + leitura breve aprendida — recomendada** | Mantém alertas naturais de presenças evidentes e oferece uma ação deliberada para investigar; informação mais elaborada exige oportunidade e recurso. |
| Somente percepção automática, aprimorada pelo treino | Dispensa a ação deliberada, mas concentra a leitura no feedback permanente e reduz a escolha de quando investigar. |

Contrato estrutural aprovado de F:

1. Preservar percepção natural de personagens espiritualmente conscientes, inclusive com Energia atual zero. Não cobrar manutenção por sentir uma presença evidente. Melhor domínio pode melhorar a sensibilidade natural; isso não equivale a um rastreamento contínuo preciso de todos os alvos.
2. A leitura deliberada exige aprendizado apropriado, tem começo e conclusão breves e usa a Mastery de Percepção Espiritual já existente. Pode ser aprendida nos caminhos compatíveis, sem conceder Pesquisa Arrancar a todos. Fonte de ensino e exercícios podem variar conforme M05/M08; primeira aprendizagem não exige Mastery prévia da própria capacidade.
3. Checar disponibilidade e Energia ao iniciar, revalidar ao concluir e cobrar uma vez junto da entrega da leitura. Falta de recurso na conclusão impede a leitura ativa e seu custo; não produz resultado parcial gratuito nem dispara depois automaticamente. Outros custos legítimos continuam pagos.
4. A tentativa válida concluída custa mesmo quando nenhum sinal pôde ser percebido. A mensagem deve significar “nenhuma presença detectada nesta leitura”, sem confirmar ausência absoluta. Cancelar antes da conclusão não paga esse custo e não recebe antecipadamente o resultado ativo.
5. Não há drenagem contínua por manter uma tela aberta ou observar o resultado já obtido. A leitura breve não fica ligada indefinidamente. Quantia, duração e intervalo de reuso precisam de ficha e calibração; valores ainda não foram propostos.
6. Pesquisa conserva aquisição/método racial, custo pequeno e intervalo próprios, e usa a mesma Mastery. Uma execução sensorial recebe uma cobrança e um resultado, sem cobrar novamente só por também usar Percepção. Alternar nomes/aplicações não deve permitir duplicar a mesma leitura ou contornar reuso; a relação concreta entre aplicações será especificada, sem presumir um cooldown global de todas as capacidades espirituais.
7. A informação e a reação são escolhas próprias de G/H. F não aprova os três blocos por associação. Meio de acesso dentro/fora do modo e apresentação dos comandos ficam com M14; nenhuma pausa do mundo é concedida.

**Prática herdada:** domínio nasce de exercícios/uso pertinente de localização e reconhecimento. Gasto de Energia, permanência online ou repetição vazia não bastam. Pesquisa e leitura geral não dão dois créditos pela mesma ação; não criar outra Proficiência. Reconhecer assinatura familiar não exige aprender gratuitamente todo o catálogo de rastreamento avançado.

### 18. M06-11G — DECIDIDO — Informação aproximada através de obstáculos

**Aprovado em 20/10/2026.** Resposta explícita: “Leitura aproximada através de obstáculos (recomendado)”. A alternativa apresentada não foi escolhida; parâmetros e integrações delegados continuam pendentes.

**Pergunta enviada:** permitir percepção através de obstáculos com direção/distância aproximadas conforme domínio, intensidade e Supressão; reconhecimento apenas de assinatura familiar, sem coordenadas, silhueta através de blocos, identidade desconhecida ou poder total exato. Alternativa: exigir visão direta para a leitura básica.

**Recomendação:** estender ao fundamento a possibilidade de leitura indireta já tratada em Pesquisa, preservando a diferença entre sentir uma presença e enxergá-la. Exigir visão direta torna a investigação menos útil em terreno construído; informação precisa e permanente reduziria o espaço para ocultação e exploração.

Contrato estrutural aprovado de G:

- Usar indícios de direção, faixas de distância e intensidade percebida; detalhes dependem da qualidade da leitura. Não prometer a informação completa em toda tentativa. Intensidade percebida não é o Battle Power real nem a reserva total de quem suprime sua presença.
- Reconhecimento de assinatura exige familiaridade adquirida e sinal suficiente. Não revela nome de desconhecido, ficha, inventário, afiliação ou intenção hostil automaticamente. Identificação já obtida por visão, diálogo ou interface autorizada não é apagada por essa limitação do sentido espiritual.
- Bloco comum não exige visão direta, mas isso não significa alcance infinito ou ignorar selos/barreiras que tenham contrato próprio. Distância tridimensional, alcance e elegibilidade são avaliados no mundo atual; não converter conhecimento de assinatura em bússola global.
- Leitura breve informa o estado percebido na conclusão. Não acompanha continuamente um alvo que se deslocou depois, nem se atualiza em tempo real por manter um indicador aberto. M14 define duração/apresentação e comunica que se trata da última leitura; interface não pode transformar aproximação em localização exata.
- Supressão, presença exteriorizada, distância, familiaridade e domínio influenciam a possibilidade/qualidade de perceber, conforme M05. A fórmula de oposição, limiares e interações com a futura Liberação permanecem abertos; não aprovar invisibilidade garantida ou vitória automática de maior Battle Power.
- Pesquisar somente entidades/sinais elegíveis dentro do alcance autorizado e região carregada do servidor, sem carregar terreno remoto, outra dimensão ou inventariar todos os jogadores. Não enviar dados ocultos exatos ao cliente para apenas mascará-los visualmente.
- Não conceder descoberta de minérios, baús, itens guardados ou todo bloco por Fullbring perceber a alma da matéria. Classificação sensorial de entidades e sinais concretos exige ficha; não presumir que todo mob vanilla tenha assinatura detectável equivalente a um personagem espiritual.
- Perceber não concede visão de entidades espirituais por si só, trava de mira, ataque através de parede ou autorização de dano. Compatibilidade de alvo, visão e regras de combate continuam separadas.

### 19. M06-11H — DECIDIDO — Caminhar e cancelar a leitura para reagir

**Aprovado em 20/10/2026.** Resposta explícita: “Caminhar e cancelar para reagir (recomendado)”. A alternativa apresentada não foi escolhida; parâmetros e integrações delegados continuam pendentes.

**Pergunta enviada:** durante a leitura breve, caminhar devagar e olhar ao redor; ataque, bloqueio, Dodge ou mobilidade disponíveis cancelam a concentração para começar, sem recuperação extra só pelo cancelamento. Pedido indisponível conserva a leitura ainda válida. Alternativa: ficar parado e cancelar manualmente.

**Recomendação:** uma oportunidade curta de concentração com reação deliberada mantém o ritmo de exploração e combate. A regra é própria desta leitura e não altera o compromisso de cancelamento do Cero ou de outras técnicas.

Contrato estrutural aprovado de H:

- Caminhada limitada e ajuste da visão durante a concentração, sem ataque/guarda/propulsão especial simultâneos. Velocidade exata pertence à ficha/calibração.
- Validar a nova ação antes de abandonar a leitura. Se disponível, encerrar a concentração e começar a reação com seus custos e requisitos. Se indisponível, avisar e preservar apenas a leitura ainda válida; não estendê-la, renovar prazo ou enfileirar pedido.
- Pelo modelo de F, cancelar antes de concluir não entrega leitura ativa nem paga seu custo. A percepção natural continua sujeita às suas próprias condições, sem usar o cancelamento para obter os detalhes ativos gratuitamente.
- Recuperações, recursos gastos e reusos anteriores permanecem. A recuperação ofensiva de um tiro anterior pode impedir atacar, mesmo que a leitura seja cancelável. H não cria permissão para iniciar leitura durante uma fase anterior que não a permita.
- Dano isolado não recebe interrupção automática por H; aplicar os contratos universais de controle/interrupção. H não concede Super Armor. O controle que invalida concentração, morte ou transição incompatível deve encerrá-la sem resultado tardio; detalhes do ciclo de vida serão confrontados no bloco seguinte.
- Caminhar no ar/água significa somente as permissões reais do meio. Não acrescentar apoio, voo, respiração ou apagar queda para completar a leitura. Condições concretas de início/meio, uso após conclusão e prioridade de comandos ainda precisam de ficha.

### 20. Critérios futuros, pendências e continuidade após F/G/H

F/G/H foram aprovados por três respostas separadas. A percepção apenas automática, a exigência de visão direta e a imobilidade com cancelamento manual não foram escolhidas. As respostas resolvem as escolhas estruturais, sem converter as pendências em especificação executável.

| Cenário futuro | Resultado esperado pelos contratos F/G/H aprovados |
|---|---|
| Energia atual zero e presença muito evidente próxima | Percepção natural ainda possível; leitura paga indisponível |
| Concluir leitura sem detectar presença | Pagar uma vez e comunicar ausência de sinal detectado, sem certificar local vazio |
| Reagir antes de concluir | Cancelar sem resultado/custo da leitura; reação válida paga seus próprios custos |
| Pedir reação impedida por recuperação anterior | Rejeitar pedido e manter concentração ainda válida, sem apagar tempos |
| Ler assinatura conhecida por trás de parede | Indício aproximado se o sinal for detectável; sem silhueta nem ataque autorizado |
| Alvo suprimido ou deslocado após leitura | Possível leitura inconclusiva ou informação já desatualizada; sem acompanhamento automático |
| Usar Pesquisa em vez do método geral | Manter aquisição racial e Mastery comum, sem pagamento/ganho duplicado pelo mesmo evento |

**Pendências com responsáveis:** M06-11 fecha oposição e compatibilidade com Supressão/Liberação, fases e ciclo de vida; M05/M15 calibram alcance, custo, tempo, precisão e curvas; M14 define comando e informação legível; M08/M12/M13 dão fontes/exercícios concretos; M10 trata dimensões/barreiras especiais; M15 trata política de alvos/party/PvP; M16 traduz, implementa e produz evidências. Nenhum valor arbitrário, novo catálogo sensorial ou técnica de rastreamento está concedido.

**Próximo passo:** a seção 21 registra a revisão das respostas; continuar com Supressão de Reiatsu e Liberação de Reiatsu. M06 permanece `EM ESPECIFICAÇÃO`. Esta atualização não altera gameplay, build, dependências ou saves e seus cenários não foram executados como testes.


### 21. Registro de F/G/H e revisão conjunta — 20/10/2026

**Respostas explícitas:** F — “Percepção natural + leitura breve aprendida (recomendado)”; G — “Leitura aproximada através de obstáculos (recomendado)”; H — “Caminhar e cancelar para reagir (recomendado)”. Nenhuma dessas perguntas permanece pendente; não voltar a votá-las.

| Interação conferida | Resultado documental |
|---|---|
| M05/percepção natural e Energia | Sentir presença muito evidente não depende de pagar leitura ativa. Energia zero não extingue esse sentido natural; domínio não converte a leitura vaga em radar preciso constante. |
| F/pagamento e H/reação | Conclusão válida cobra e entrega o resultado uma vez, mesmo sem sinal. Reação aceita antes da conclusão cancela sem esse custo/resultado. Reação negada não interrompe concentração válida nem dispensa a cobrança se a leitura depois concluir. |
| F/G e Supressão | Intensidade percebida pode estar reduzida ou inconclusiva; não detectar não certifica ausência. Oposição e manutenção de Supressão ainda exigem contrato próprio. |
| H/recuperações | Novo ataque ainda depende dos tempos anteriores e de suas próprias permissões. O cancelamento desta leitura não revisa K do Cero nem cria cancelamento universal de outras capacidades. |
| Pesquisa e método geral | Aprendizado racial preservado; uma Mastery de Percepção, sem duplicar crédito ou cobrança pela mesma execução. Reuso entre aplicações e parâmetros raciais ainda precisam ser concretizados. |
| G/terreno e identificação | Sentir através de bloco comum não mostra silhueta, inventário ou identidade desconhecida. Assinatura familiar exige sinal/familiaridade; informação visual ou narrativa legítima segue suas próprias regras. |
| G/tempo e servidor | Resultado corresponde à leitura concluída, sem seguir o alvo depois. Servidor decide elegibilidade/informação na região autorizada; cliente não recebe segredo exato para apenas escondê-lo na UI. |

**Resultado:** não foi identificado conflito que exija rever F/G/H ou a integração já aprovada de Pesquisa. Isso é revisão documental dirigida, sem teste de gameplay nem auditoria de todas as exceções. O sistema ainda precisa de números, fichas, alvos concretos, apresentação, ensino e ciclo de vida, com os responsáveis da seção 20.

**Continuidade:** preparar Supressão de Reiatsu e sua oposição à percepção; depois Liberação e estados/concorrência/ciclo de vida. A existência das três capacidades, a Mastery própria de Supressão e os estados Natural/Suprimida/Liberada são herdados de M05. Forma de manutenção, compatibilidade com ações e regra de oposição ainda não estão aprovadas por F/G/H.


### 22. Supressão de Reiatsu — preparação — 20/10/2026

**Estado atualizado em 20/10/2026:** M06-11I/J/K `DECIDIDOS` por três respostas próprias, registradas abaixo e revisadas na seção 27. Na preparação eram propostas; A–H continuam decididos. Esta rodada define a ocultação espiritual básica e sua oposição à percepção; não o catálogo de furtividade, disfarces, selos limitadores ou formas.

**Experiência pretendida:** aproximar-se ou explorar escondendo parte da própria presença, sem precisar gastar continuamente uma reserva de combate. Um adversário atento pode perceber indícios; ver o corpo ou ouvir a movimentação continua possível. O momento de partir para a ofensiva tem consequência legível.

**Base herdada:** M05, seções 32–40/51: estados Natural/Suprimida/Liberada, Controle e vazamento, Supressão aprendida com Mastery própria, assinatura familiar e leitura de intensidade potencialmente enganada pela ocultação. M06-11F/G/H define leitura breve paga, informação aproximada e cancelamento próprio. A existência dessas capacidades não está novamente em votação.

**Pesquisa complementar em 20/10/2026 e limites:**

- [TV Tokyo, sinopse do episódio 121](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes11/index.html), exibição de 11/04/2007 (BL-20): descreve a restrição de poder dos oficiais enviados ao Mundo Humano e sua liberação autorizada. Esse caso não estabelece o custo ou a eficácia da Supressão voluntária básica. Não importar o fator numérico da cena para todos os personagens, nem confundir ocultação com selo que limita capacidade de combate.
- [BS TV Tokyo, TYBW episódio 21, THE HEADLESS STAR](https://www.tv-tokyo.co.jp/broad_bstvtokyo4k/program/detail/202402/23289_202402252330.html), página da exibição de 25/02/2024 (BL-21): a sinopse descreve perceber mudança de Reiatsu e aproximação de uma presença familiar. Complementa direção/familiaridade de M05, sem fixar custo, probabilidade ou mecanismo de localização do mod. A data é da exibição dessa página, não alegação de estreia do episódio.
- Também localizado/lido o resumo [Pierrot, episódios 266–316](https://pierrot.jp/title/bleach/playback_14.html), trecho do retorno de Ichigo: relata ausência de Reiatsu sentida junto de poder demonstrado. Não foi usado como prova de que esse caso seja Supressão voluntária comum ou concessão de indetectabilidade por Battle Power.

Resumos japoneses interpretados; sem episódios/capítulos integrais. A pesquisa desta rodada não confirmou uma regra canônica universal para manutenção da Supressão, seu rompimento ao atacar ou oposição determinística. I/J/K são **adaptações aprovadas do mod por três respostas próprias**, apoiadas nos contratos locais; a pesquisa não as aprova por si só.

### 23. M06-11I — DECIDIDO — Supressão manual sem consumo de Energia

**Aprovado em 20/10/2026.** Resposta explícita: “Supressão manual sem consumo de Energia (recomendado)”. A alternativa apresentada não foi escolhida; fichas e calibração permanecem delegadas.

**Pergunta enviada:** capacidade aprendida, ligada/desligada manualmente, sem taxa inicial nem consumo contínuo; eficácia por Controle/Mastery e vazamento de grandes reservas mal controladas. Não reduz atributos nem torna o corpo invisível. Alternativa: manutenção de Energia.

**Recomendação:** custo em oportunidade, aprendizado e qualidade da ocultação. Uma drenagem contínua pode consumir o recurso estratégico durante exploração longa e incentivar desligar a capacidade fora de encontros já conhecidos. A versão gratuita precisa de limites de informação e ação claros; não pode prometer esconder qualquer personagem de qualquer observador.

Contrato estrutural aprovado de I:

1. Aprender Supressão de Reiatsu separadamente e usar a Mastery própria prevista em M05. Controle contribui para a eficácia; não há outro atributo, Proficiência ou barra de furtividade. Primeira aprendizagem não exige Mastery da habilidade ainda indisponível.
2. Ativação válida muda a manifestação de Natural para Suprimida; desligamento voluntário volta a Natural. Sem custo inicial, drenagem de Energia ou Stamina próprios dessa versão; Energia atual zero não a impede por si só. Não há prazo fixo presumido, mas as condições de compatibilidade continuam necessárias.
3. Reduzir o sinal exteriorizado, com eficácia conforme condição espiritual, Controle e domínio; Reserva alta mal controlada pode continuar perceptível. Não assegurar ocultação perfeita, escolher livremente um Battle Power falso ou adquirir assinatura de outra pessoa.
4. Não diminuir atributos, Vida/Energia máximas, recursos atuais ou poder efetivo por um multiplicador universal. Supressão básica não é o selo limitador de BL-20. Não aumentar regeneração, devolver recurso, apagar recuperação ou obter bônus ofensivo por alternar o estado.
5. Não esconder corpo, equipamento, passos, partículas/som legítimos das ações, nome visível por sistema autorizado ou lembrança de posição já vista. Não equivale a invisibilidade, agachar vanilla ou perda automática de aggro. Reação dos NPCs combina os sentidos efetivamente disponíveis, em M08/M09/M15.
6. Modo de combate, transições com Liberação/formas, morte/logout/dimensão e prioridade de entradas exigem o confronto de estados seguinte. I não decide esses casos por analogia com o desligamento de Blut, nem concede coexistência com Liberação de Reiatsu; são estados de manifestação diferentes em M05.

**Treino herdado:** exercícios apropriados de ocultação, controle e confronto com observação podem desenvolver Mastery. Manter o estado ligado, andar sem desafio relevante ou receber confirmações vazias repetidas não gera progresso eficiente. Mestres/registros/descoberta seguem fontes raciais de M05; não exigir um NPC para cada aprimoramento.

### 24. M06-11J — DECIDIDO — Ofensiva encerra Supressão básica

**Aprovado em 20/10/2026.** Resposta explícita: “Ofensiva encerra Supressão; retorno manual (recomendado)”. A alternativa apresentada não foi escolhida; fichas e calibração permanecem delegadas.

**Pergunta enviada:** permitir deslocamento, Dodge, mobilidade racial, guarda básica e leitura espiritual compatíveis; iniciar ataque/técnica ofensiva válida encerra Supressão e volta a Natural, sem retorno automático. Pedido recusado preserva o estado válido. Alternativa: manter Supressão durante ofensiva com vazamento.

**Recomendação:** saída clara ao começar a ofensiva. Permite preparar uma aproximação e escolher quando revelar-se, evitando que o iniciante ataque continuamente sob ocultação espiritual máxima. A alternativa de vazamento por ação é válida, porém exige classificar duração/intensidade de cada emissão e a recomposição da ocultação desde o início.

Contrato estrutural aprovado de J:

- Deslocamento normal, Dodge, Shunpo/Sonído/Hirenkyaku/Bringer Light, guarda básica e leitura de Percepção/Pesquisa não encerram a Supressão somente por seus nomes. Cada ação conserva aprendizado, pagamento, fases, corpo/percurso e disponibilidade. Nenhuma delas se torna silenciosa/invisível por isso; formas e técnicas especiais ainda podem ter incompatibilidades expressas.
- Ao aceitar o início efetivo de ataque básico ou técnica ofensiva, encerrar a Supressão antes da execução ofensiva; a manifestação volta a Natural. Não exigir acerto ou dano para revelar-se e não ligar automaticamente Liberação. Pedido indisponível, antes de ser aceito, não encerra ocultação válida nem cobra custo da ofensiva.
- Isso inclui preparo ofensivo aceito de Cero/arco: cancelar depois, falhar a emissão por falta de recurso ou errar não restaura a Supressão anterior. Custos do ataque continuam nos momentos já aprovados; romper ocultação não antecipa custo de flecha/Cero.
- Voltar a suprimir requer novo pedido manual válido; não programar retorno ao final de ataque/Recovery. A permissão exata de reativação durante fases e o tempo de estabelecimento ainda precisam de ficha, sem apagar os tempos anteriores. J não concede ofensiva durante mobilidade nem permite reagir onde a ação anterior não permita.
- Não classificar toda mineração, construção, uso de item ou interação como ataque por compartilhar um botão. Classificação de ações concretas e emissões de suporte fica na ficha/integração; efeitos/projéteis já emitidos não são apagados ou reembolsados por um pedido posterior de Supressão.
- Guarda ou percepção permitida não decide automaticamente compatibilidade de Blut, canalização de cura, barreira, transformação ou efeito sustentado. Esses estados precisam do confronto de concorrência. Dano recebido isolado não ganha uma regra universal nova de interrupção; controles e estados que tornem a manutenção inválida seguem os contratos próprios, sem Super Armor concedida.

J não proíbe estudar capacidades especializadas de combate oculto com fonte e ficha próprias no futuro; não concede nenhuma delas nem altera a técnica básica por precedentes de um personagem específico.

### 25. M06-11K — DECIDIDO — Oposição gradual conforme condições reais

**Aprovado em 20/10/2026.** Resposta explícita: “Resultado gradual conforme as condições (recomendado)”. A alternativa apresentada não foi escolhida; fichas e calibração permanecem delegadas.

**Pergunta enviada:** resultados graduais por domínio, Controle, presença emitida, distância e familiaridade; sem nova chance aleatória a cada clique. Nas mesmas condições, repetir não vence ocultação por sorte. Alternativa: probabilidade por tentativa.

**Recomendação:** leitura consistente e sensível a mudanças reais. O jogador melhora sua investigação ao aproximar-se, desenvolver domínio, conhecer uma assinatura ou perceber uma mudança de emissão, sem obter uma detecção garantida apenas por repetir o comando.

Contrato estrutural aprovado de K:

1. Servidor compara capacidade sensorial do observador e sinal exteriorizado do alvo, após sua Supressão válida, considerando distância, contexto e familiaridade. Não comparar apenas Battle Power nem usar Reserva atual como revelação automática de toda potência possível.
2. O resultado pode ir de nenhum sinal detectável a indício vago e informação mais clara, sempre limitado por G. A leitura clara continua aproximada; reconhecer assinatura ou estimar intensidade não dá coordenadas nem desfaz Supressão para todos os outros observadores.
3. Manter as mesmas condições relevantes não cria novas rolagens de sorte a cada leitura. Repetição válida continua pagando conforme F, sem bônus cumulativo de penetração por clique ou ganho eficiente de Mastery pela repetição vazia. A tentativa sem sinal não informa se o alvo está ausente, fora do alcance ou bem oculto.
4. Aproximação/afastamento, aprendizado/familiaridade adquiridos, estado/forma ou emissão alterados podem mudar o resultado. Não congelar detecção para todo o encontro, seguir automaticamente quem foi percebido ou revogar o direito de ocultar-se de novo quando as condições realmente mudarem.
5. Observadores podem obter resultados diferentes por seu domínio/contexto. Supressão não tem êxito/fracasso global para todos, e informação de um jogador não é transmitida automaticamente à party por K. Compartilhamento de interface pertence a M14/M15.
6. Controle, Mastery de Percepção e de Supressão mantêm funções próprias. Fórmulas, patamares, alcance tridimensional, precisão e barreiras especiais continuam com M05/M06/M10/M15. Não definir percentual de sucesso ou derrotar qualquer ocultação por um simples limiar de maior atributo.

### 26. Critérios futuros e próximos passos de Supressão

I/J/K foram aprovados por três respostas separadas. Manutenção de Energia, conservar Supressão durante a ofensiva com vazamento e chance aleatória por leitura não foram escolhidas. A–K não exigem nova votação; os cenários abaixo são critérios futuros, sem teste executado.

| Situação futura | Resultado esperado pelos contratos I/J/K aprovados |
|---|---|
| Explorar com Supressão válida e Energia atual zero | Estado possível sem consumo próprio; ocultação depende de Controle/domínio e sinal |
| Adversário vê o corpo ou ouve o movimento | Supressão espiritual não apaga esses indícios nem garante perda de aggro |
| Usar mobilidade ou leitura espiritual compatível | Preservar Supressão e cobrar a ação conforme sua regra; sem silêncio, voo ou custos novos |
| Iniciar Cero/arco e cancelar antes de emitir | Voltar a Natural no preparo ofensivo aceito; sem pagar disparo não emitido ou restaurar ocultação |
| Pedir ataque ainda impedido por recuperação | Rejeitar e manter ocultação válida; não renovar/resetar tempos |
| Repetir leitura nas mesmas condições contra alvo oculto | Mesma qualidade possível, sem nova chance por sorte; tentativa concluída mantém custo |
| Mudar distância/emissão ou conhecer uma assinatura | Reavaliar a informação autorizada pelas novas condições, sem acompanhamento permanente |
| Dois observadores com domínio/contextos distintos | Informação individual compatível, sem revelar globalmente o alvo |

**Dependências:** M06-11 fecha estados, suporte/passivas e ciclo de vida após Liberação; M05/M15 calibram oposição, tempo de estabelecimento, alcance e curvas; M08/M09 tratam ensino e reações de NPCs/inimigos; M14 define comandos/feedback, inclusive passagem a Natural; M10 trata fronteiras/barreiras; M16 implementa e valida com evidências futuras. Não criar multiplicador de selo, penalidade de combate, forma ou catálogo para preencher essas dependências.

**Continuidade:** I/J/K registrados e revisados na seção 27; depois Liberação de Reiatsu. M06 segue `EM ESPECIFICAÇÃO`; números, fichas e implementação não estão concluídos. Sem alteração de runtime, build, dependências ou saves e sem teste de gameplay nesta atualização.


### 27. Registro de I/J/K e revisão com Percepção — 20/10/2026

**Três respostas próprias:** I — “Supressão manual sem consumo de Energia (recomendado)”; J — “Ofensiva encerra Supressão; retorno manual (recomendado)”; K — “Resultado gradual conforme as condições (recomendado)”. Nenhuma pergunta I/J/K permanece pendente.

| Interação conferida | Resultado documental |
|---|---|
| I/M05 Controle e Reserva | Ocultação depende de condição, Controle e Mastery; não há garantia universal de desaparecer, consumo de recurso ou redução de atributos. Não confundir com selo limitador. |
| I/F e leitura sob Supressão | A ocultação gratuita não financia leitura: cada tentativa ativa válida concluída continua cobrando Energia, mesmo sem sinal. Energia zero conserva apenas as capacidades que não a exigem. |
| J/H e mobilidade/guarda | Ação compatível pode preservar Supressão, mas leitura ativa ainda precisa ser cancelada para reagir. O estado de manifestação não libera duas execuções incompatíveis simultâneas. |
| J/arco e Cero | Preparo ofensivo aceito encerra Supressão; cancelar não restaura o estado. Arco mantém cancelamento sem recuperação extra antes de emitir; Cero mantém K e sua recuperação. Nenhum paga tiro não emitido por causa dessa transição. |
| J/pedido recusado | Pedido ofensivo ainda indisponível não revela por transição automática; conserva Supressão válida e tempos anteriores. A regra não exige acertar para revelar um ataque aceito. |
| K/G e leitura incerta | Repetição nas mesmas condições não sorteia detecção, não acumula penetração e não certifica local vazio. Mudar distância/sinal/domínio/familiaridade pode alterar a qualidade da informação. |
| K/Pesquisa | Método racial aprendido permanece, com a mesma Mastery e limites informacionais. Não ganha exceção automática de detectar toda Supressão por seu nome. |
| Observadores e mundo | Resultado é individual, no alcance/região autorizados. Ocultação espiritual não apaga posição vista, som, ameaça já conhecida ou transmite informação automaticamente à party. |

**Resultado documental:** não foi identificado conflito que exija rever F–K ou os contratos raciais examinados. Não houve teste de oposição, simulação ou validação de stealth em jogo. Tempo/fases de ativação e reativação, custos de leitura, alvos/sinais concretos, parâmetros de oposição, NPCs e política de party continuam delegados.

**Continuidade:** Liberação de Reiatsu e depois estados/concorrência/ciclo de vida. Natural, Suprimida e Liberada são estados distintos; encerrar Supressão ao atacar não ativa Liberação automaticamente. Compatibilidade com formas, passivas/suporte e transições deve ser declarada, sem preencher por analogia com Blut.


### 28. Liberação de Reiatsu — base, pesquisa e fronteiras — 20/10/2026

**Estado desta rodada:** M06-11L/M/N `DECIDIDOS` por três respostas próprias, recebidas depois da apresentação das recomendações. Os contratos abaixo registram o escopo dessas escolhas e mantêm as pendências explícitas. Não houve aprovação por associação a I/J/K ou à simples continuidade do planejamento.

**Experiência:** escolher exteriorizar uma pressão espiritual maior, sustentando esse esforço enquanto combate ou se desloca. Um adversário de capacidade muito inferior pode ter dificuldade limitada de movimentação; um semelhante ainda enfrenta o personagem pelas regras normais de combate. Liberação não é transformação, aumento comprado de atributos nem dano de área gratuito.

**Herança preservada:** M05, seções 32–36/38, define Reiatsu como manifestação do poder existente, não barra ou Battle Power, os três estados de manifestação, Mastery própria de Liberação, opressão em diferenças significativas e resistência sem sexto atributo. M06-11F–K delimita percepção, pagamento da leitura, Supressão e oposição gradual. M06-05/06 continua responsável por fases, guarda, mobilidade, recursos e controle.

**Fonte complementar consultada em 20/10/2026:** [TV Tokyo, sinopse do episódio 308](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes26/index.html), exibição de 08/02/2011 (BL-22): o resumo relata pessoas perdendo a consciência sob a pressão de Aizen. Sustenta a existência narrativa de opressão intensa; não comprova manutenção de Energia, alcance, elegibilidade de alvos ou o efeito limitado escolhido para esta capacidade básica. Usado apenas o trecho do episódio 308; outros episódios da página não são importados como cânone ou conteúdo do mod. BL-20 já distingue restrição/liberação de poder por selo; não define a técnica voluntária básica.

Resumos oficiais japoneses interpretados, sem episódio/capítulo integral. Consumo contínuo, compatibilidade com combate e limitação de movimento são **adaptações aprovadas para o mod**. O caso narrativo de desmaio não concede paralisia ou remoção de comandos pela versão básica. M05 mantém possibilidades mais amplas; outras aplicações/exceções exigem fonte, ficha e decisão próprias.

### 29. M06-11L — DECIDIDO — Liberação manual com manutenção de Energia

**Aprovado em 20/10/2026.** Resposta explícita: “Liberação manual com manutenção de Energia (recomendado)”. Liberação breve com custo por uso não foi escolhida como modelo básico.

| Alternativa apresentada | Consequência |
|---|---|
| **Estado manual com Energia de manutenção — escolhido** | Permite sustentar presença e pressão durante ações, com um compromisso contínuo sobre o recurso estratégico. |
| Liberação breve por uso | Organiza a pressão em aplicações pontuais com custo individual, exigindo novos usos para repetir. |

Contrato estrutural aprovado de L:

1. Exigir aprendizado de Liberação de Reiatsu e usar sua Mastery própria prevista em M05. Exteriorizar somente o poder que o personagem possui; não aumentar atributos, conceder forma, desbloquear transformação ou melhorar automaticamente o dano das técnicas.
2. Ativação manual válida estabelece manifestação Liberada, com manutenção de Energia. Não há taxa adicional fixa por ligar/desligar nem uma segunda barra. O estado não depende de acertar ou de haver um alvo já conhecido para consumir manutenção.
3. Manutenção deve estar paga para conceder o benefício do período correspondente. Não produzir pressão ativa gratuita por alternar rapidamente o comando, aproveitar quadro visual ou executar antes da validação de recurso. Cadência de cobrança, valores e operação no servidor pertencem à ficha/calibração.
4. Falta de Energia para manter encerra Liberação e volta a Natural, sem saldo negativo, benefício não pago ou reativação automática quando o recurso voltar. Não reembolsar manutenção anterior. Falta de Energia não elimina a presença natural herdada de M05.
5. Desligamento voluntário retorna a Natural, preservando custos, recuperação, cooldowns e ações anteriores. Liberação e Supressão não coexistem; a ordem de pedidos, passagem entre estados e compatibilidade com formas será especificada no confronto de estados seguinte.
6. A manifestação Liberada pode tornar o sinal exteriorizado mais evidente conforme o poder e controle reais, sendo avaliada pela percepção F/G/K. Não revela automaticamente identidade, Battle Power real, atributos, inventário ou posição global a outros jogadores.

Tempo de estabelecimento, manutenção por unidade de tempo, parâmetros de intensidade e reuso próprio permanecem pendentes. Não copiar os valores de Blut nem acrescentar uma taxa de transformação por L.

### 30. M06-11M — DECIDIDO — Manter Liberação durante ações compatíveis

**Aprovado em 20/10/2026.** Resposta explícita: “Manter durante ações compatíveis (recomendado)”. Concentração que termina ao atacar ou usar mobilidade não foi escolhida para esta versão.

Contrato estrutural aprovado de M:

- Ataques, guarda básica, Dodge, mobilidades raciais e leitura espiritual disponíveis podem ocorrer mantendo Liberação compatível e paga. A capacidade não exige uma canalização imóvel que impeça essas ações somente por estar ativa.
- Cada ação mantém custos, requisitos e fases. Cero/arco preservam preparo, emissão e regras distintas de cancelamento; guarda e mobilidade continuam incompatíveis durante o impulso onde já definido; leitura breve ainda se cancela para reagir conforme H.
- Manutenção de Liberação e custo de uma leitura/ataque são obrigações diferentes sobre Energia. Não cobrar duas vezes pela mesma obrigação nem prometer um efeito para o qual o recurso não existe. Ordem concreta dos pagamentos concorrentes precisa de contrato de estados; Energia zero não bloqueia por associação Dodge/mobilidade válida paga com Stamina.
- Não concede bônus automático de dano, Super Armor, invulnerabilidade, redução de custo ou recuperação. Manter o estado não permite ignorar a recuperação anterior ou iniciar ação que a técnica atual não permita.
- Supressão continua incompatível com Liberação. Encerrar Supressão por ofensiva volta a Natural conforme J; não ativa Liberação automaticamente. O usuário precisa solicitar a capacidade pertinente, com aprendizado e recurso válidos.
- “Compatível” não aprova coexistência com toda forma, Blut, canalização de suporte ou passiva por omissão. M06-11/M07 deve declarar essas relações, sem confundir manifestar pressão com revelar o poder selado de uma arma.

### 31. M06-11N — DECIDIDO — Opressão básica limitada a grande diferença espiritual

**Aprovado em 20/10/2026.** Resposta explícita: “Opressão limitada com grande diferença espiritual (recomendado)”. Uma versão básica apenas sensorial não foi escolhida.

**Regra escolhida:** quando houver diferença espiritual muito grande e o alvo for elegível, a pressão da Liberação básica pode impor dificuldade limitada de movimentação comum. Contra capacidade semelhante, o efeito básico é pressão percebida, sem penalidade de movimento concedida por uma diferença pequena.

Contrato estrutural aprovado de N:

1. Verificar diferença significativa e resistência do alvo pelos elementos já previstos em M05: poder próprio, Resistência, estado e capacidades pertinentes. Não criar atributo novo ou decidir tudo por um Battle Power maior. Limiares e curva continuam pendentes.
2. Limitar o prejuízo à movimentação comum, preservando comandos e possibilidade de reação/escape. A versão básica não causa dano direto, paralisia, desmaio, stagger obrigatório, ataque compulsório, perda da mira ou ação forçada. Não altera automaticamente velocidade/alcance das técnicas de mobilidade aprendidas.
3. Não drenar diretamente Vida, Stamina, Energia ou Postura por esta regra; não conceder redução de defesa, dano extra, quebra de guarda ou alteração de cadência ofensiva. Outras interferências e reações de NPCs precisam de capacidade/regra própria; as possibilidades gerais de M05 não entram todas neste contrato básico.
4. Efeito de movimento, limite de acumulação, atualização e cessação precisam de ficha. Não permitir imobilização por sobrepor várias pressões básicas ou reativá-las repetidamente. A decisão de limitar o movimento não equivale a zerar gravidade, velocidade vertical ou queda acumulada em Minecraft.
5. Sentir a pressão não significa necessariamente sofrer prejuízo de movimento. A apresentação de desconforto deve respeitar esse recorte, sem retirar controle por câmera, animação, efeitos visuais ou áudio.
6. Na aprovação de N, alcance, obstáculos/barreiras, entidades elegíveis, aliados/party/pets e relação com pressão natural continuavam abertos. **Complementados por O/P/Q nas seções 33–36.** N, isoladamente, não aprovou uma aura global, imunidade de aliados, dano amigo, reação idêntica de todo mob vanilla ou pressão compulsória em todo território.

**Fronteira com M05:** N especifica esta Liberação básica. Não revoga todas as possibilidades futuras de opressão extrema nem concede técnicas superiores por inferência. Toda ampliação precisa ser explicitada e revista no escopo apropriado.

### 32. Registro de L/M/N e revisão dos fundamentos — 20/10/2026

**Três respostas explícitas:** L — “Liberação manual com manutenção de Energia (recomendado)”; M — “Manter durante ações compatíveis (recomendado)”; N — “Opressão limitada com grande diferença espiritual (recomendado)”. A–N não têm perguntas pendentes. L/M/N foram registrados após as respostas, sem fase escrita de proposta confundida com decisão.

| Relação conferida | Resultado documental |
|---|---|
| Natural / Suprimida / Liberada | Estados distintos de manifestação; Supressão e Liberação não coexistem. J encerra ocultação em Natural ao aceitar ofensiva; L não se ativa automaticamente. |
| Recursos e pagamento | Supressão não consome Energia; leitura ativa paga uma vez ao concluir; Liberação consome manutenção. Nenhum deles cria outra barra ou concede restituição por trocar de estado. |
| Percepção e Liberação | Sinal exteriorizado é avaliado por F/G/K, sem identidade ou localização global. Não converter pressão sentida em leitura precisa automática de todos os alvos. |
| Percepção sob pressão mantida | M pode preservar Liberação durante leitura paga; H ainda exige cancelar concentração para ação incompatível. O estado de manifestação não remove as restrições da execução. |
| Técnicas ofensivas e mobilidade | Recursos/fases permanecem. Sonído e outras mobilidades não ganham custo de Energia por associação à Liberação; a manutenção é obrigação independente. |
| Opressão e defesa/controle | N trata movimento comum de alvo muito inferior. A/B/C continuam tratando dano elegível; mitigação de Vida ou Postura não é automaticamente a fórmula de resistência à pressão. |
| Mastery e aquisição | Percepção/Pesquisa compartilham domínio, Supressão e Liberação têm Masteries próprias; nenhum rank/Proficiência universal adicional. Prática pertinente, não AFK ou gasto vazio, desenvolve domínio. |

**Parecer:** não foi identificado conflito que exija rever F–N ou as aprovações raciais examinadas. Esta é revisão documental de contratos, sem teste de gameplay, simulação ou evidência nova de implementação. O repertório universal avançou, mas M06 e Liberação ainda possuem lacunas identificadas; não declarar fechamento total por ter respostas para L/M/N.

**Encaminhamento após L/M/N, trabalhado nas seções 33–36:** alcance e obstáculos/barreiras de Liberação, alvos e efeitos em aliados/party/pets, além da relação com manifestação Natural. Depois consolidar transições, concorrência de estados/pagamentos, interrupção e ciclo de vida (morte, desconexão, dimensão/reinício), preservando conquistas e custos. Usar essas entregas para avaliar fechamento conceitual de M06; M07 continua na sequência, sem iniciar fichas de linhagem ou artes.

**Dependências:** M06 declara comportamento, elegibilidade e permissões; M05/M15 calibram intensidade, resistência, limites e custos; M07 trata compatibilidade de formas; M08/M09 tratam aprendizado e reações de NPCs/inimigos; M10 barreiras/dimensões; M14 comando/feedback; M15 políticas de party/PvP; M16 dados/implementação/evidências. Não transferir uma escolha estrutural ainda aberta apenas para “balanceamento”. Sem mudança de código, runtime, build, dependências ou saves, e sem teste de Minecraft nesta atualização.


### 33. M06-11O — DECIDIDO — Área limitada através de paredes comuns

**Aprovado em 20/10/2026.** Resposta explícita: “Área limitada que atravessa paredes comuns (recomendado)”. A alternativa de exigir caminho livre, com paredes bloqueando a pressão básica, não foi escolhida.

**Experiência:** uma parede comum não isola o adversário da pressão espiritual próxima. Afastar-se pode reduzir a pressão e sair do alcance encerra a dificuldade de movimento; construir um abrigo continua útil contra ataques que colidem com blocos, mas não funciona automaticamente como barreira espiritual.

Contrato estrutural aprovado de O:

1. Área centrada no emissor e acompanhando sua posição, em todas as direções, com distância tridimensional e limite finito. A intensidade diminui com a distância; isso não exige crescimento linear do alcance com atributo ou Mastery. Raio em blocos, curva e contribuição do domínio continuam para ficha/calibração M05/M06/M15.
2. Blocos e paredes comuns não bloqueiam a pressão básica nem exigem caminho livre. Não usar distância percorrida por corredores como substituto da distância espacial. Não destruir blocos, abrir portas ou empurrar entidades pela parede; O não concede colisão ofensiva ou alteração de terreno.
3. Uma barreira espiritual só reduz ou bloqueia esta pressão se o seu contrato declarar essa proteção. O nome “barreira”, uma parede resistente ou a existência de um escudo de dano não bastam. M06/M10 devem declarar cobertura, validade e passagem de pressão, distinguindo-a de leitura espiritual e de projéteis.
4. A penalidade de movimento só existe enquanto forem válidos alcance, intensidade suficiente, manutenção de L, grande diferença de N e elegibilidade de P. Sair dessa área válida encerra o efeito dessa fonte, sem deixar uma lentidão residual. Encerrar a Liberação também remove sua contribuição; a frequência de verificação no servidor e o feedback precisam de ficha, sem usar um efeito visual atrasado para prolongar a penalidade.
5. Avaliar somente o mundo atual e a região carregada/autorizada, sem carregar chunks remotos, propagar por portais ou alcançar outra dimensão. Recalcular condições quando emissor/alvo se deslocam; não deixar uma área permanente no ponto de ativação.
6. Alcance de opressão e alcance de percepção são contratos diferentes. Pode ser possível sentir uma presença sem estar sujeito à dificuldade de movimento. O não torna os dois raios iguais, nem dá ao emissor uma lista, localização ou confirmação de quem foi afetado através da parede.

**Limites herdados de N:** sem dano, paralisia, perda de comandos ou redução automática de velocidade/alcance da mobilidade técnica. Pressões sobrepostas não podem produzir imobilização por acumulação; forma de composição e teto continuam pendentes. O não concede Pressão Focalizada ou seleção livre de alcance/intensidade como uma técnica especializada já aprendida.

### 34. M06-11P — DECIDIDO — Opressão sobre adversários elegíveis

**Aprovado em 20/10/2026.** Resposta explícita: “Opressão sobre adversários elegíveis (recomendado)”. A alternativa de incluir também aliados e neutros elegíveis próximos não foi escolhida como padrão desta capacidade básica.

**Experiência:** liberar Reiatsu pode dificultar a movimentação de adversários muito inferiores, preservando a cooperação com aliados, party e pets. A proteção é uma **adaptação de design para o mod**, não afirmação de imunidade canônica por amizade ou raça.

| Categoria | Regra estrutural aprovada para a penalidade básica |
|---|---|
| Inimigo elegível do encontro/mundo | Pode sofrer a dificuldade se os demais requisitos N/O forem satisfeitos |
| Jogador adversário | Exige permissão válida de combate contra esse jogador, além dos requisitos N/O |
| Próprio emissor, aliado reconhecido e membro da party | Sem penalidade dessa Liberação básica |
| Pet do emissor ou de aliado/party protegidos | Protegido; propriedade/vínculo válidos precisam ser reconhecidos pelo servidor |
| Animal ou NPC neutro | Não entra automaticamente na opressão por estar perto ou por ser fraco |
| Observador espiritualmente capaz de sentir a presença | Percepção continua conforme F/G/K, independentemente de poder receber a penalidade |

Contrato estrutural aprovado de P:

- M06 define o efeito; relações de combate, vínculos e permissões são autorizados pelo servidor. M08/M09/M15 devem concretizar quem é aliado, inimigo e neutro em cada contexto. Mesma raça, proximidade, aparência, nome conhecido ou assinatura familiar não bastam para presumir aliança ou hostilidade.
- Permissão de PvP é requisito para afetar um jogador adversário, não uma declaração automática de que todo jogador próximo é inimigo. A capacidade não cria autorização de combate nem contorna proteção de aliado/party por apenas estar com PvP habilitado. Duelos, mudanças de vínculo e exceções precisam da política explícita de M15.
- Opressão não é percepção hostil: o sistema pode autorizar um efeito sem revelar ao jogador a identidade, posição ou intenção de cada alvo oculto. Não entregar nomes, contagem de afetados ou marcação automática como resultado dessa área. Reconhecimento por G/K continua individual e limitado.
- Nenhuma raça recebe vulnerabilidade ou imunidade universal por P. NPCs, mobs vanilla/espirituais e bosses precisam de classificação de elegibilidade e resistência, sem escalar todo o mundo pelo Battle Power do emissor. Neutralidade também não se converte automaticamente em hostilidade ao ligar a capacidade.
- A proteção aqui é contra a dificuldade de movimento desta Liberação básica. Não concede imunidade geral a ataques de área, efeitos de outras técnicas, perigo ambiental ou todos os poderes de Reiatsu; essas políticas continuam nas fichas e em M15.
- Reação narrativa de humanos/NPCs, medo, fuga, aggro, desmaio ou objetivos de intimidação não são concedidos por esta tabela. Pertencem a M08/M09/M12/M13 e exigem contrato próprio. Sentir uma presença não exige desempenhar a mesma reação de IA ou retirar o controle do jogador.
- Vínculo ou permissão que deixem de autorizar a penalidade precisam cessar sua aplicação; tempos e atualização concreta ficam na ficha. Não aprovar trocas instantâneas de party, novos sistemas administrativos ou acesso a dados secretos por esta decisão.

### 35. M06-11Q — DECIDIDO — Opressão básica exige Liberação deliberada

**Aprovado em 20/10/2026.** Resposta explícita: “Opressão básica exige Liberação deliberada (recomendado)”. Opressão passiva Natural por grande diferença não foi escolhida para esta capacidade básica.

**Experiência:** tornar-se muito forte não impõe automaticamente uma penalidade de movimento a quem está próximo. O jogador decide exteriorizar pressão e paga a manutenção para produzir o efeito básico autorizado.

| Manifestação | Relação com esta opressão básica |
|---|---|
| Natural | Presença pode ser sentida; sem dificuldade de movimento passiva desta capacidade |
| Suprimida | Sinal reduzido conforme I/K; vazamento pode ser percebido, sem conceder opressão ativa gratuita |
| Liberada válida e paga | Pode impor a penalidade limitada de N aos adversários elegíveis de P, dentro de O |

Q delimita a aplicação básica de M05, seções 33–36/38. As possibilidades gerais de opressão intensa ou de uma forma/personagem excepcional não são apagadas; também não se tornam efeitos universais. Uma exceção futura exige fonte, ficha e decisão próprias, incluindo consequências sobre aliados/neutros, sem usar “diferença extrema” para reintroduzir silenciosamente a alternativa não escolhida.

Falta de manutenção retorna a Natural conforme L e encerra a penalidade desta capacidade. Energia zero não apaga a presença natural, nem transforma a Supressão gratuita em selo redutor de poder. J continua levando a Natural ao aceitar ofensiva sob Supressão; não produz Liberação automática ou penalidade de movimento por esse retorno. Q não reduz atributos, reservas ou potência e não cobra Energia para existir no estado Natural.

### 36. Registro de O/P/Q, revisão e continuidade — 20/10/2026

**Três respostas explícitas:** O — “Área limitada que atravessa paredes comuns (recomendado)”; P — “Opressão sobre adversários elegíveis (recomendado)”; Q — “Opressão básica exige Liberação deliberada (recomendado)”. A–Q estão decididos nos escopos registrados, sem perguntas pendentes. As alternativas não escolhidas estão preservadas em cada seção; números e políticas externas continuam abertos.

**Pesquisa de apoio reconsultada em 20/10/2026:** BL-21, [sinopse oficial de TYBW 21](https://www.tv-tokyo.co.jp/broad_bstvtokyo4k/program/detail/202402/23289_202402252330.html), sobre perceber mudanças e presença familiar, e BL-22, [sinopse oficial do episódio 308](https://www.tv-tokyo.co.jp/anime/bleach_2012/episodes/episodes26/index.html), sobre opressão intensa. Textos japoneses interpretados, sem episódios/capítulos integrais. Não demonstram regra universal de paredes, raio, seleção de aliados, manutenção ou estado Natural; O/P/Q são adaptações aprovadas do mod, não conclusões canônicas dessas sinopses. Não foram importadas as demais entradas das páginas nem consultadas novas APIs nesta rodada.

| Relação/cenário para validação futura | Resultado esperado pelos contratos aprovados |
|---|---|
| Adversário muito inferior atrás de parede comum, dentro da área válida | O permite pressão, P/N ainda precisam autorizar a penalidade; sem revelar identidade/posição ao emissor |
| Alvo se afasta ou passa para outro andar | Usar distância tridimensional/intensidade; ao sair das condições válidas, cessar a contribuição dessa fonte |
| Área de dano bloqueada por um escudo | Isso não prova bloqueio de Reiatsu; consultar proteção expressa da barreira |
| Aliado/party/pet protegido perto do emissor | Sem penalidade básica; pode sentir a presença conforme percepção própria |
| Animal/NPC neutro ou jogador sem permissão de combate | Proximidade e inferioridade não autorizam automaticamente a penalidade |
| Emissor encerra Liberação ou não paga manutenção | Voltar a Natural e encerrar esta penalidade, preservando presença e custos anteriores |
| Grande poder Natural ou vazamento sob Supressão | Possível percepção, sem transformar vazamento em opressão básica gratuita |
| Várias fontes próximas | Sem imobilização por sobreposição; composição/teto e atualização ainda exigem ficha |

**Revisão dirigida:** não foi identificado conflito que exija revogar F–N ou as estruturas raciais examinadas. P complementa a elegibilidade aberta de N; O separa alcance mecânico de informação; Q delimita a manifestação que autoriza esta penalidade e preserva exceções como decisões futuras. Não confundir proteção de aliados com ocultação da assinatura nem pressão através de blocos com ataque, radar ou destruição através de paredes.

**Encaminhamento após O/P/Q, trocas/Blut/pagamentos trabalhados nas seções 37–40:** transições Natural/Suprimida/Liberada, concorrência com Blut/passivas/ações, pagamentos simultâneos e interrupção/ciclo de vida. Declarar morte, desconexão, mudança de dimensão e reinício, preservando conquistas, custos e tempos legítimos. Só depois avaliar fechamento conceitual de M06 e passagem para M07; linhagens/artes continuam adiadas.

**Pendências com responsáveis:** M06 fornece fichas/fases, atualização/cessação e composição de pressões; M05/M15 calibram alcance em blocos, intensidade, limiares, teto e consumo; M07 compatibiliza formas; M08/M09 classificam habitantes/relações e reações; M10 especifica barreiras e fronteiras; M14 apresenta comandos e feedback; M15 concretiza party/pets/PvP e mudanças de vínculo; M16 implementa e produz evidências. Critérios acima não foram executados como testes. Sem alteração de código, build, dependências ou saves; M06 continua `EM ESPECIFICAÇÃO`.

**Conferência documental final — 21/10/2026:** respostas O/P/Q de 20/10/2026 preservadas; continuidade e referências locais conferidas. Nenhum teste de implementação executado.


### 37. M06-11R — DECIDIDO — Troca direta validada de manifestação

**Aprovado em 21/10/2026.** Resposta explícita: “Troca direta validada (recomendado)”. Exigir desligar manualmente até Natural e depois emitir outro comando para o próximo estado não foi escolhido.

**Experiência:** escolher o estado pretendido em um único pedido. A troca preserva o compromisso da técnica em andamento; não serve para cancelar um ataque, contornar recuperação ou obter ocultação e opressão juntas.

Contrato estrutural aprovado de R:

1. Permitir solicitar Natural, Suprimida ou Liberada diretamente. Servidor valida aprendizado, compatibilidade, fase, reuso e recurso pertinente antes de abandonar a manifestação atual ainda válida. Pedido recusado comunica o motivo e conserva apenas o estado anterior que continua legítimo; não adia a solicitação para executá-la automaticamente depois.
2. Uma troca aceita retira os efeitos da manifestação anterior antes de estabelecer a nova. Não existe sobreposição entre Supressão e Liberação. Não exigir uma espera universal adicional nem dois comandos; tempos próprios de estabelecimento e restrições de fase continuam com a ficha. Um único pedido não significa que toda técnica se estabeleça instantaneamente.
3. Liberação → Supressão retira a pressão ativa antes de conceder ocultação. Supressão → Liberação encerra ocultação antes de conceder pressão paga. Se houver estabelecimento próprio, não antecipar seus benefícios; falha posterior não restaura automaticamente a manifestação abandonada. Voltar a Natural remove a manifestação deliberada, sem devolver manutenção já paga.
4. A troca não cancela preparação/execução de outra técnica, limpa cooldown ou encurta Recovery. Consultar as permissões da fase em andamento. Não usar R para manter Supressão durante preparo ofensivo: J ainda encerra ocultação no início aceito de arco/Cero e demais ofensivas; reativação exige uma fase que realmente a permita.
5. Pedir novamente o estado já ativo não concede novo período grátis, reinicia manutenção, renova uma janela nem duplica efeitos. Perda de validade do estado atual exige encerrá-lo independentemente do êxito do novo pedido. Nenhum pedido inválido preserva Liberação sem Energia.
6. Recursos de cada ação continuam nos momentos já decididos: R não cobra uma flecha ainda não emitida, antecipa leitura paga ou acrescenta taxa fixa para Supressão/Liberação. A disponibilidade da Liberação inclui o pagamento legítimo do primeiro período de manutenção; isso não é uma taxa de ativação separada.

R resolve a operação manual entre manifestações. A transição breve sem Vene/Arterie aprovada para Blut continua própria de Blut; não copiá-la como intervalo obrigatório dos três estados. Desligamento por morte, modo, dimensão ou desconexão pertence ao próximo bloco de ciclo de vida.

### 38. M06-11S — DECIDIDO — Blut compatível com os estados de manifestação

**Aprovado em 21/10/2026.** Resposta explícita: “Blut compatível com os estados (recomendado)”. Exigir desligar Blut para suprimir ou liberar Reiatsu não foi escolhido.

**Experiência:** um Quincy pode manter o reforço corporal escolhido e controlar a presença exteriorizada. Ocultar presença não desliga por si só sua defesa ou seu reforço físico; o jogador ainda precisa decidir quando abandonar a ocultação para atacar.

| Combinação conhecida e válida | Regra aprovada |
|---|---|
| Natural + Vene ou Arterie | Mantém a aplicação de Blut e seu custo |
| Suprimida + Vene ou Arterie | Compatível; ocultação depende de I/K, sem esconder sinais visuais de Blut |
| Liberada + Vene ou Arterie | Compatível; cada manutenção é paga separadamente conforme T |
| Vene + Arterie | Continua proibida a coexistência; troca breve sem os dois reforços |
| Suprimida + Liberada | Continua proibida; trocar conforme R |

Contrato estrutural aprovado de S:

- Exigir aquisição/compatibilidade racial e apenas uma aplicação conhecida de Blut. S não concede Blut a todas as raças nem Hierro a Quincy. Cada capacidade conserva Mastery própria, fases e limites de efeito, sem nova Proficiência ou barra.
- Ativar ou trocar Blut não se torna ofensiva por seu nome. Seu início depende de a fase permitir; a ofensiva efetiva posterior continua encerrando Supressão por J, mesmo com Arterie ativo. Reforço ofensivo não é autorização para golpear durante mobilidade ou preservar carga cancelada.
- Supressão controla emissão, sem garantir invisibilidade, apagar veias/efeitos visuais, esconder o corpo ou impor uma redução de atributos. Um observador pode perceber indícios pelos meios legítimos; a leitura espiritual continua condicionada por F/G/K. S não promete um valor fixo de vazamento de Blut nem detecção garantida.
- Liberação não amplia o reforço de Blut. Vene conserva proteção de impactos elegíveis e as exclusões de C; Arterie conserva o recorte corporal, sem bônus automático a flechas/Gintō. Manutenções diferentes não duplicam o custo de uma única aplicação.
- Hierro passivo continua pela compatibilidade do corpo/estado já aprovada, sem exigir desligamento apenas por ocultar ou liberar presença e sem ganhar manutenção de Energia. Isso é preservação da defesa existente, não autorização para acumular capacidades raciais incompatíveis.
- Formas, poderes individuais, regeneração especial, barreiras e canalizações de suporte precisam de suas próprias compatibilidades. S não permite todo efeito sustentado simultaneamente nem revoga desligamento de Blut ao sair do modo de combate. M07 conserva responsabilidade pelas formas.

**Fonte reconsultada em 21/10/2026:** BL-03, [glossário oficial TYBW, entrada 血装](https://bleach-anime.com/keyword/). Descreve reforço corporal por Reishi nos vasos, duas aplicações e impossibilidade de ativá-las juntas. Texto japonês interpretado; sem episódio/capítulo integral. Não estabelece a coexistência com Supressão/Liberação nem a manutenção/ordem de pagamentos do mod. S é adaptação aprovada, e R/T são regras de operação de jogo. Outras entradas recentes da página não foram incorporadas ao catálogo ou à cronologia.

### 39. M06-11T — DECIDIDO — Manutenção devida antes da ação

**Aprovado em 21/10/2026.** Resposta explícita: “Manutenção primeiro; Blut antes de Liberação (recomendado)”. Priorizar a ação solicitada, podendo deixar a manutenção sem recurso, não foi escolhido.

**Experiência:** custos que já venceram dos estados mantidos têm prioridade. Entre os dois, preservar primeiro o reforço corporal selecionado; o jogador pode desligar uma capacidade para reservar Energia às próximas ações, sem recuperar cobranças anteriores.

Ordem estrutural quando essas obrigações coincidem na mesma resolução:

1. Verificar quais estados continuam válidos e quais manutenções realmente estão devidas; não cobrar períodos futuros só porque houve um comando. Estado removido legitimamente deixa de gerar períodos futuros, sem estorno nem período gratuito.
2. Tentar pagar a manutenção de Blut devida. Se não houver Energia suficiente para essa cobrança, desligar a aplicação sem cobrança parcial ou benefício não financiado; sem trocar automaticamente Vene por Arterie ou o contrário.
3. Tentar pagar a manutenção de Liberação devida com a Energia restante. Falta de recurso encerra a manifestação Liberada em Natural e remove sua contribuição de opressão, conforme L/O/Q. Não encerrar Blut pago só porque Liberação não pôde ser mantida.
4. Resolver o custo da ação no seu momento próprio com o saldo disponível. Flecha paga na emissão; leitura paga na conclusão; preparo cancelado não recebe cobrança indevida. Se não houver recurso, aplicar a falha própria da ação, sem custo ou resultado dessa emissão/leitura e sem devolver manutenções já pagas.

**Limites da prioridade:**

- A ordem vale para obrigações coincidentes, não reserva antecipadamente toda Energia para efeitos futuros. Uma ação já paga legitimamente não é desfeita por uma cobrança posterior; no próximo vencimento, o estado que não puder pagar termina. Não reativar sozinho ao regenerar Energia.
- Blut tem prioridade seja Vene ou Arterie. T preserva a aplicação escolhida; não é uma troca automática para defesa. Se uma cobrança não puder ser paga, a outra ainda pode ser tentada com o saldo disponível: não cobrar duas manutenções como um pacote indivisível.
- Pedido de ação inválido não cobra o custo dessa ação, mas não suspende manutenções que venceriam independentemente dele. Não restaurar Supressão que J já encerrou no preparo ofensivo aceito apenas porque a emissão posterior falhou.
- Manutenção paga dá benefício somente ao período correspondente, sem reaplicar custo por impacto, quadro visual ou consulta de estado. T não cria taxa fixa de ligar/desligar, altera recursos máximos nem reduz os custos a zero por Mastery.
- Stamina continua separada: Dodge/mobilidade disponíveis podem funcionar sem Energia, com seu pagamento e suas fases. Gintō conserva o conteúdo já preparado; Energia pessoal atual zero não apaga essa provisão nem cobra novamente o mesmo conteúdo.
- Desligar voluntariamente remove o próprio benefício e evita períodos futuros; não desfaz cobrança já devida/resolvida, recupera Energia ou apaga tempos. A ficha deve declarar a ordem de pedidos válidos recebidos pelo servidor e o início de cada período, sem deixar a UI escolher a ordem dos débitos.

Valores, duração de cada período e tratamento técnico de comandos/eventos pertencem à ficha/M15/M16. T fecha a prioridade destes custos, sem atribuí-la ao cânone nem definir por associação a ordem de manutenção de todas as futuras transformações de M07.

### 40. Revisão de R/S/T e próximos passos — 21/10/2026

**Três respostas próprias:** R — “Troca direta validada (recomendado)”; S — “Blut compatível com os estados (recomendado)”; T — “Manutenção primeiro; Blut antes de Liberação (recomendado)”. A–T estão decididos nos escopos registrados, sem perguntas pendentes. R/S/T não aprovam por associação todas as regras de ciclo de vida.

| Cenário para validação futura | Resultado esperado pelos contratos aprovados |
|---|---|
| Pedir Supressão desconhecida enquanto libera pressão | Recusar; preservar Liberação apenas se ainda válida/paga |
| Trocar validamente de Liberação para Supressão | Retirar opressão antes de ocultar; sem sobreposição, estorno ou segundo comando obrigatório |
| Pedir ocultação em fase ofensiva incompatível | Recusar, sem cancelar ataque ou enfileirar retorno automático |
| Manter Vene sob Supressão e começar ataque válido | Ataque encerra Supressão em Natural; Vene permanece somente se válido/pago |
| Blut e Liberação devidos, com recurso apenas para Blut | Manter aplicação escolhida; Liberação termina; ação posterior depende do saldo |
| Não poder pagar Blut, mas ainda poder pagar Liberação | Encerrar Blut; tentar/manter Liberação com o saldo, sem exigir pacote completo |
| Recursos suficientes para manutenções, insuficientes para flecha/leitura | Manutenções legítimas permanecem; sem emitir/entregar resultado pago nem cobrar ação recusada |
| Energia se esgota, mas há Stamina ou Gintō preparado | Preservar permissões próprias; sem paralisia total ou destruição da provisão |

**Parecer dirigido:** não foi identificado conflito que exija revogar A–Q, M06-09Q/R ou as permissões raciais examinadas. R complementa a passagem entre manifestações; S resolve a coexistência até então aberta com Blut, preservando exclusividades; T completa a prioridade de custos sem mudar os momentos de pagamento das ações. Não houve simulação, teste de gameplay ou alteração de implementação.

**Encaminhamento após R/S/T, ciclo de vida e efeitos trabalhados nas seções 41–44:** encerrar a política de interrupção/ciclo de vida e modo de combate: morte, desconexão, mudança de dimensão, reinício, efeitos já emitidos e preservação de tempos/conquistas. Depois avaliar fechamento conceitual de M06 e passagem a M07. Fichas, números, dados, políticas de alvos e conteúdo continuam com responsáveis; linhagens/artes adiadas.

**Responsáveis:** M06 detalha fases e compatibilidades executáveis; M05/M15 calibram manutenções e saldo; M07 define formas e suas prioridades próprias; M10/M11 tratam mundo/instrumentos; M14 comunica estado/motivo da recusa; M16 traduz ordenação, persistência e sincronização em implementação/evidências. Sem código, build, dependências ou saves alterados e sem testes de Minecraft.


### 41. M06-11U — DECIDIDO — Encerrar estados transitórios; retorno manual

**Aprovado em 21/10/2026.** Resposta explícita: “Encerrar estados; retorno manual (recomendado)”. Restaurar automaticamente Supressão/Liberação quando possíveis ao retornar não foi escolhido.

**Experiência:** voltar ao jogo sem retomar sozinho um disparo, deslocamento ou consumo contínuo. O personagem conserva o que aprendeu, mas precisa ativar novamente as capacidades deliberadas.

Contrato estrutural aprovado de U:

1. Morte, desconexão, mudança de dimensão e reinício do mundo/servidor encerram cargas/preparos, leitura em concentração, propulsão de impulsos, Blut e manifestações deliberadas de Supressão/Liberação. A manifestação volta a Natural. Não salvar um comando para executar, completar uma leitura ou emitir uma carga automaticamente no retorno.
2. Conhecimento, Proficiências, Masteries e conquistas permanecem. Morte de gameplay não muda raça automaticamente, conforme RAC-06; transformações/evolução têm contrato próprio em M07. Limpar a execução não apaga o poder adquirido nem transforma um corpo compatível em outro por omissão.
3. Não reativar sozinho o estado ou começar nova manutenção ao retornar. Energia recuperada, item reequipado e dimensão novamente válida não substituem um novo pedido manual. Hierro e outras proteções realmente passivas dependem da compatibilidade atual; U não converte passivas em toggles manuais.
4. Preservar custos legitimamente pagos. Etapa não emitida/concluída conserva a ausência de cobrança já decidida para flecha/leitura; conteúdo de Gintō já usado não reaparece. Tempos iniciados seguem V; esta limpeza não concede reset de cooldown nem devolve recursos.
5. Remover a propulsão interrompida sem conceder deslocamento final, voo, apoio ou imunidade à queda. U não redefine a física da travessia/respawn nem limpa automaticamente veneno, queimadura, controle adversário ou perigos do local. M10/M15/M16 devem tratar a transição concreta, sem transformar sair/voltar em limpeza universal de condições.
6. Efeitos autônomos já emitidos seguem W. Encerrar a execução do personagem não desfaz impactos, cura dano anterior ou recolhe projéteis de volta. Montagens no mundo e inventário não são apagados em bloco como se fossem uma carga na mão.

A regra trata a manutenção/execução, não define perda de itens, keepInventory, punição de morte, quantidades de recursos do respawn, retorno de formas ou antidesconexão em PvP. M07/M10/M11/M15 têm essas responsabilidades. Em particular, não devolver um custo não significa proibir toda política normal de recuperação no respawn; essa política precisa ser declarada no seu escopo.

### 42. M06-11V — DECIDIDO — Tempos preservados pelo relógio do mundo

**Aprovado em 21/10/2026.** Resposta explícita: “Tempo do mundo, preservado entre sessões (recomendado)”. Contar cooldowns/recuperações apenas com o personagem conectado não foi escolhido.

**Experiência:** sair, morrer, viajar ou reiniciar não devolve imediatamente uma técnica. O tempo legítimo de espera pode passar com o servidor funcionando, mesmo sem o personagem conectado.

| Situação | Regra para cooldowns e recuperações já iniciados |
|---|---|
| Morrer/respawn ou mudar de dimensão | Preservar o restante, descontando somente tempo de simulação transcorrido |
| Desconectar com servidor simulando o mundo | Continuar a contagem pelo tempo de simulação, sem exigir personagem online |
| Mundo/servidor parado ou realmente pausado | Não avançar; preservar o restante para retomar |
| Reiniciar | Carregar o restante legítimo; não zerar nem descontar tempo com o servidor parado |
| Espera concluída | Técnica apenas potencialmente disponível; requisitos atuais e fases ainda precisam autorizar seu uso |

Contrato estrutural aprovado de V:

- Usar tempo de simulação autorizado pelo servidor, não relógio do cliente, FPS, horário civil ou mudança da hora do dia por comando. Atravessar dimensões não troca a referência para permitir antecipar ou reiniciar a espera.
- Preservar separadamente a recuperação ofensiva e o reuso da capacidade, sem fundi-los em um cooldown global. Cancelamento continua gerando os tempos que sua ficha declarar; V preserva os já iniciados, sem criar uma nova penalidade universal por morrer/sair.
- Não simular ataques ou manutenções encerradas por U enquanto offline. Descontar uma espera não concede treino, prática, regeneração offline ou provisão de Gintō gratuita. Valores de recurso no respawn e estados de encontro continuam com regras próprias.
- Retorno não repete comando anterior nem reaplica custo, dano ou benefício. Persistência deve guardar o necessário para reconstruir o restante uma vez, inclusive após reinício, sem duplicar ou reiniciar a duração integral.
- Partidas locais pausadas e servidores que deixem de simular seguem a condição real de simulação. A ficha técnica deve declarar a referência estável e o tratamento de salvamento/migração; não presumir que um contador de idade do jogador ou valor temporário já resolva a continuidade.

**Diferença de implementação localizada:** `StatusData.clearTransientState()` zera cooldowns de slots e reuso de Dodge; `save()/load()` não preservam esses cooldowns. `PlayerData.resetTransientState()` chama essa limpeza, e `CapabilityEvents`/`TickHandler` a usam em transições/login. A contagem dos slots aparece em tick do jogador. Esses pontos não demonstram o relógio aprovado; exigirão futura adequação M16, sem modificar código/saves nesta rodada. Não foi auditado todo temporizador ou serviço de técnica.

### 43. M06-11W — DECIDIDO — Disparos autônomos continuam na região ativa

**Aprovado em 21/10/2026.** Resposta explícita: “Disparos autônomos continuam na região ativa (recomendado)”. Encerrar também os disparos autônomos junto do emissor não foi escolhido.

**Experiência:** uma flecha espiritual já lançada não desaparece apenas porque o emissor morreu, desconectou ou viajou. Um canal que depende dele, porém, não continua atacando sem a presença/manutenção exigida.

| Classe de efeito do mod | Política aprovada |
|---|---|
| Disparo autônomo emitido | Continuar na região ativa de origem até impacto/expiração ou perda de validade própria, mesmo sem emissor presente |
| Canal/efeito que exige presença, concentração ou manutenção do emissor | Encerrar ao perder esse requisito; sem continuar emitindo pulsos gratuitamente |
| Ataque efêmero ao descarregar sua região ou reiniciar | Encerrar, sem reaparecer ou retomar depois |
| Montagem persistente no mundo | Exige contrato próprio de dono, instrumentos, duração, desmonte e persistência; não herda automaticamente a limpeza dos ataques efêmeros |

Contrato estrutural aprovado de W:

1. A ficha classifica o efeito. Uma técnica com partes autônomas e sustentadas encerra somente o que perde seus requisitos, sem converter toda emissão, área ou Cero em projétil independente por associação.
2. Disparo que continua conserva trajetória, colisões, limite de duração e autorização de alvo/impacto. Não ganha perseguição do emissor/alvo entre dimensões, novo alcance, benefício gratuito ou ataque a entidade não elegível por faltar o dono online. A ficha declara os parâmetros registrados na emissão e os que devem ser revalidados no impacto.
3. Não carregar chunks para manter o ataque vivo. Região descarregada e reinício encerram os ataques efêmeros do mod; persistência técnica não deve restaurá-los em um alvo dias depois ou reemitir um disparo no login. O custo já pago permanece, sem reembolso, recarga ou duplicação.
4. Impactos já resolvidos não são desfeitos. Condição já aplicada ao alvo não é apagada apenas pela saída do emissor quando for independente dele; sua duração, limpeza e persistência seguem a ficha. Uma condição realmente mantida pelo emissor perde efeito quando perde o requisito. W não reclassifica todo veneno ou fogo vanilla.
5. Registrar responsabilidade suficiente para alvos, permissões e eventuais resultados sem exigir que o objeto do jogador continue presente. Crédito de kill/quest, loot, prática válida offline e recompensas ficam para M09/M12/M15; W não promete crédito automático nem a duplicação de progresso no retorno. V não concede treino offline por deixar um projétil ativo.
6. Preparações de Gintō conservam consumo por etapa, dono e necessidade de ficha M06/M10/M11/M15/M16. Não detonar, completar, reembolsar nem apagar todos os instrumentos por desconexão sem a regra correspondente. Alteração legítima de blocos também não é desfeita por confundi-la com partícula temporária.

W governa ataques/efeitos do mod no escopo classificado. Não altera por associação todas as flechas, itens largados, entidades, condições e persistência de Minecraft ou de outros mods.

### 44. Revisão de U/V/W, evidência e continuidade — 21/10/2026

**Três respostas explícitas:** U — “Encerrar estados; retorno manual (recomendado)”; V — “Tempo do mundo, preservado entre sessões (recomendado)”; W — “Disparos autônomos continuam na região ativa (recomendado)”. A–W estão decididos nos escopos registrados, sem perguntas pendentes. Ainda falta concluir modo de combate/interrupções e avaliar a maturidade de M06.

**Base de pesquisa técnica:** MC-05, [Forge 1.20.1, Capabilities](https://docs.minecraftforge.net/en/1.20.1/datastorage/capabilities/), seções de sincronização e persistência após morte, reconsultadas em 21/10/2026. A documentação exige tratamento explícito de cópia/sincronização; não escolhe o que deve sobreviver ou o relógio de cooldown do jogo. U/V/W são adaptações aprovadas para este mod, sem nova afirmação canônica sobre morte/renascimento em Bleach nem capítulos/episódios novos consultados.

**Leitura local:** `CapabilityEvents.java`, `PlayerData.java` e `TickHandler.java` lidos integralmente; em `StatusData.java`, trechos dirigidos de cooldown/tick/limpeza/salvamento. A infraestrutura atual copia dados, limpa execução e repõe recursos no respawn, mas não comprova U/V/W como conjunto. O reset de cooldowns identificado em V é divergência localizada; os recursos do respawn e o retorno selado encontrado no código não viram decisões novas de M06 ou M07.

| Cenário para validação futura | Resultado esperado pelos contratos aprovados |
|---|---|
| Sair carregando arco/Cero ou concentrando leitura | Sem emissão/resultado atrasado e sem cobrança da etapa que não ocorreu; não retomar carga |
| Morrer/sair/viajar com Supressão, Liberação ou Blut | Encerrar estado mantido; Natural, sem apagar aquisição ou ligar sozinho depois |
| Trocar de dimensão durante impulso | Encerrar propulsão; preservar tempos/custos, sem conceder voo ou queda anulada pela técnica |
| Retornar antes de acabar o cooldown | Restante legítimo ainda limita; sem reset pelo login/respawn |
| Esperar desconectado com servidor em simulação | Contagem pode terminar; sem comando executado ou treino/regen offline concedido |
| Desligar/reiniciar com tempo restante | Retomar o restante, sem contar período parado ou restaurar estados deliberados |
| Emissor morre após lançar disparo autônomo | Disparo válido continua na origem ativa; sem nova cobrança ou perseguição entre dimensões |
| Região do ataque efêmero descarrega ou servidor reinicia | Ataque encerra sem replay, custo devolvido ou impacto duplicado |
| Montagem de Gintō fica incompleta no mundo | Aplicar a ficha própria, preservando consumo por etapa; sem resolução universal por W |

**Parecer dirigido:** não foi identificado conflito que exija revogar A–T, a limpeza do preparo Quincy ou a saída de Blut já aprovadas. U estende o contrato explícito às manifestações/execuções examinadas; V distingue espera legítima de execução transitória; W distingue efeito autônomo, canal e montagem persistente. Preservar custos não significa manter um canal ativo; encerrar um canal não significa apagar todo efeito que já saiu dele.

**Encaminhamento após U/V/W, trabalhado nas seções 45–48:** concluir relação de Supressão/Liberação/leitura com a saída de Combat Mode e consolidar interrupções por perda de requisito/controle, preservando as exceções já aprovadas de arco/Cero/mobilidades. Distinguir saída da interface de Combat State, efeitos sobre o mundo e política de antidesconexão/PvP. Depois produzir a revisão de maturidade para avaliar fechamento conceitual de M06 e passagem a M07. Linhagens/artes permanecem adiadas.

**Dependências:** M06 fornece fichas de fases, custos de interrupção e classificação dos efeitos; M07 formas; M09/M12/M15 resultados/encontros/antidesconexão e relações PvP; M10/M11 montagens, mundo e instrumentos; M14 feedback; M16 relógio persistente, migração segura, salvamento e sincronização. Não foram alterados código, build, dependências ou saves nem executados testes de Minecraft. Critérios acima são futuros, não evidência de implementação.


### 45. M06-11X — DECIDIDO — Capacidades espirituais independentes do modo

**Aprovado em 21/10/2026.** Resposta explícita: “Capacidades espirituais independentes do modo (recomendado)”. Encerrar também Supressão, Liberação e leitura ao sair do modo não foi escolhido.

**Experiência:** explorar, observar ou controlar a presença sem manter a Combat Hotbar aberta. Mudar a interface não interrompe por si só essas capacidades.

Contrato estrutural aprovado de X:

- Supressão, Liberação e leitura espiritual podem ser iniciadas/mantidas fora de Combat Mode, respeitando aquisição, compatibilidade, fases, recursos e informação já aprovados. X também não concede Pesquisa racial sem aprendizado; sua aplicação continua com o domínio comum e requisitos próprios.
- Sair do modo conserva uma Supressão válida ou Liberação paga; leitura em concentração pode concluir normalmente, pagando e entregando somente o resultado autorizado por F/G/H. A percepção natural continua independente de modo e de Energia.
- Ataque, guarda, Dodge e mobilidade ainda obedecem às regras de reação. Uma ação válida incompatível cancela a leitura conforme H; pedido recusado não a cancela por associação. Usar a hotbar vanilla não autoriza uma segunda execução simultânea incompatível.
- Ofensiva aceita continua encerrando Supressão por J. Mineração, construção e uso de item não são classificados como ofensiva apenas pelo botão: a integração deve declarar a ação concreta. Liberação continua pagando manutenção e obedecendo alvos/alcance de N/O/P; modo desligado não a torna gratuita ou inofensiva.
- Arco Quincy e Blut continuam dissipando/desligando ao sair do modo conforme M06-09L/Q. Se Blut e Liberação estavam ativos, sai apenas Blut por essa regra; não reembolsar período já pago. Custos futuros da Liberação permanecem próprios.
- M14 deve oferecer acesso e feedback apropriados fora da Combat Hotbar, sem decidir novas teclas ou mover itens nesta rodada. Fechar a interface não apaga Combat State, combate em andamento, regras de regeneração ou restrições de recarga de Gintō.

X complementa o acesso antes delegado de F e a relação com modo de I/L. Não revisa U: morte, logout, dimensão e reinício continuam encerrando essas execuções/manifestações deliberadas. Sair de uma interface e sair da sessão são eventos distintos.

### 46. M06-11Y — DECIDIDO — Sair da interface preservando o compromisso da execução

**Aprovado em 21/10/2026.** Resposta explícita: “Sair da interface preservando a execução (recomendado)”. Bloquear a saída do modo durante toda fase incompatível não foi escolhido.

**Experiência:** poder fechar a Combat Hotbar sem transformar isso em cancelamento gratuito de um golpe. O jogador retorna aos controles normais, mas ainda precisa respeitar mãos e fase antes de usar ferramentas/itens.

| Execução ao sair do modo | Regra aprovada |
|---|---|
| Preparo ofensivo cancelável | Encerrar conforme sua regra de cancelamento, perdendo carga e mantendo custos/recuperação pertinentes |
| Cero básico em carga | Cancelar sem cobrar disparo não emitido; conservar recuperação de cancelamento de K |
| Arco básico em preparo | Dissipar arco/preparo; sem custo de flecha não emitida nem recuperação extra pré-emissão; recuperação anterior permanece |
| Fase comprometida não cancelável | Concluir a fase; a troca de interface não a encurta ou autoriza outra execução incompatível |
| Supressão/Liberação/leitura válidas | Aplicar X; não cancelar só porque a hotbar mudou |
| Efeito já emitido | Aplicar W; sair da interface não recolhe nem reembolsa o efeito |

Contrato estrutural aprovado de Y:

1. Aceitar a saída da interface sem exigir uma permissão universal de cancelamento da técnica. Separar mudança de controles/apresentação da execução autorizada pelo servidor. Preparo cancelável segue sua regra; fase não cancelável mantém a execução comprometida, sem conservar uma carga abandonada para disparo posterior pelo controle vanilla.
2. A hotbar normal reaparece com os itens reais preservados. Usar, equipar ou interagir com esses itens depende das mãos e permissões da fase atual. Não permitir atacar, comer, colocar bloco ou usar escudo como segunda ação incompatível só por mudar a interface; também não bloquear toda interação durante qualquer Recovery por regra global.
3. Preservar custos pagos, recuperação ofensiva, cooldowns e consequências de cancelamento. Entrar novamente no modo não reinicia o ritmo, restaura carga, liga Blut ou manifesta o arco sozinho.
4. Se durante a execução ocorrer controle ou perda de requisito que efetivamente a invalide, aplicar a interrupção pertinente; Y não obriga concluir uma fase impossível nem concede Super Armor. M14 deve comunicar por que um item ainda não pode ser usado, sem exigir que o jogador deduza o estado de uma animação.
5. Declaração das fases e dos itens/ações compatíveis pertence à ficha M06/M11/M14. Y não determina automaticamente que toda técnica/canalização seja cancelável, nem concede troca universal de forma ou poder durante um ataque.

### 47. Consolidação de interrupções — contratos herdados

**Estado:** consolidação das decisões existentes, complementada por X/Y; não é uma terceira votação nem uma nova regra universal de controle.

| Evento | Tratamento conceitual preservado |
|---|---|
| Dano isolado | Aplicar dano normalmente; não interromper automaticamente toda preparação/execução por M06-02S |
| Controle apropriado | Interromper as fases/capacidades que esse controle realmente invalida; declarar resistência/Super Armor na ficha, sem imunidade a dano |
| Perda de arma, corpo, alvo/local ou outro requisito exigido | Encerrar ou recusar a parte que perdeu validade; não cobrar/emiti-la depois como se o requisito continuasse presente |
| Pedido de reação indisponível | Feedback e preservação da ação ainda válida, onde aprovado; não renovar prazo ou enfileirar comando |
| Reação disponível durante arco/leitura/mobilidade | Aplicar sua permissão específica; conservar custos e tempos anteriores |
| Reação durante carga de Cero | Cancelar e cumprir sua recuperação própria; X/Y não criam exceção gratuita |
| Falta de manutenção | Encerrar o estado não financiado conforme T; capacidades de outro recurso continuam potencialmente disponíveis |
| Morte/logout/dimensão/reinício | Aplicar U/V/W, separando execução, tempos, conquistas e efeitos no mundo |

Um controle de movimento não desliga automaticamente toda presença espiritual ou proteção corporal apenas pelo nome. Deve declarar quais requisitos/fases invalida; dano, Postura, controle, manutenção e manifestação continuam eixos distintos. Isso não concede imunidade de estados a todo controle: fichas que exijam concentração/presença precisam ser respeitadas. Hierro não é guarda; Vene não é Super Armor; Supressão não é invisibilidade.

Pedidos de interface e restauração de estado não fazem o mundo pausar. Combat State e política de antidesconexão mantêm responsabilidade M15; este fechamento não concede fuga segura, limpeza de ameaça ou recompensa por sair/voltar.

### 48. Fechamento conceitual de M06 — 21/10/2026

**Estado geral: `ESPECIFICADO CONCEITUALMENTE`.** M06-01–06 fornece a base universal; M06-07/08/09/10 têm fechamento estrutural; M06-11 conclui a integração conceitual examinada. A–Y têm respostas explícitas, com os respectivos escopos preservados. X — “Capacidades espirituais independentes do modo (recomendado)” e Y — “Sair da interface preservando a execução (recomendado)” encerram as escolhas desta rodada. Nenhuma pergunta A–Y está pendente.

**Escopo da revisão final:** confronto dirigido das conclusões raciais, contratos de recursos, execução, defesa, percepção, estados, modo e ciclo de vida; rastreabilidade das respostas e pendências. A conclusão aproveita as revisões anteriores identificadas, sem alegar nova leitura integral de todo código, catálogo, mangá ou de todos os documentos neste turno. Nesta rodada não houve nova pesquisa canônica/API, protótipo ou teste de gameplay. X/Y são adaptações aprovadas de operação do mod.

**Parecer:** não foi identificada decisão estrutural pendente neste recorte que impeça iniciar M07. As lacunas restantes têm entregas e condições de retorno abaixo. Não são resolvidas por omissão: impedem declarar M06 executável ou seu gameplay validado, mas não exigem paralisar a sequência modular até completar cada ficha do catálogo.

#### Estrutura concluída

| Domínio | Base consolidada |
|---|---|
| Formação e poder | Atributos, Proficiência, aquisição, repertório e Mastery separados; um poder principal, vínculos e compatibilidades próprios |
| Quatro jornadas | Disciplinas/campos raciais diferentes, fundamentos e ensino contextual; nenhuma simetria forçada ou compra de Mastery |
| Ritmo e recurso | Custos por execução, Recovery/reuso distintos, sem trava global; Stamina/Energia/provisões com funções próprias |
| Defesa e controle | Guarda/Postura, mitigação de Vida e controle separados; exceções por componente, sem imunidade por rótulo espiritual |
| Mobilidade | Impulsos com corpo/trajeto/gravidade e custos preservados; Bringer Light com salto aprendido, sem voo universal |
| Reiatsu | Percepção, Supressão e Liberação com aprendizado/estados/custos/informação; opressão básica deliberada e limitada |
| Modo e persistência | Interface separada da execução e Combat State; transições/custos, execução encerrada e tempos preservados; efeitos com ciclos próprios |

#### Entregas ainda necessárias e condição de retorno

| Responsável | Entrega pendente | Exigida antes de |
|---|---|---|
| M06 + M05/M15 | Ficha de cada capacidade: acesso, fases, custo/momento, alvo, alcance, colisão, reações, cancelamento, recuperação/reuso e desenvolvimento | Implementar ou considerar essa capacidade especificada para execução |
| M06 + M11/M15/M16 | Categorias completas de dano/proteções, guarda/escudo, ordem técnica de mitigação, absorção, exceções, teto e pressão de Postura | Aceitar resolução integrada de ataques/defesas, sem aplicar reduções duas vezes |
| M06 + M05/M15 | Números/curvas de recursos, alcance, intensidade, percepção/oposição, composição de pressões, prática e manutenção | Avaliar equilíbrio e ritmo; não preencher com valores inventados nesta conclusão |
| M07 + M06 | Formas, estados evolutivos, despertar, manutenção/reversão e mudanças de corpo/vetor/repertório | Ativar forma ou presumir sustentação aérea/compatibilidade de técnicas nela |
| M07/M10/M14 + M06 | Corpos grandes, meios/apoios, limites/portais e apresentação/colisão concretas | Aceitar essas transições e deslocamentos em mundo construído |
| M08/M12/M13 + M06 | Fontes de ensino, exercícios, requisitos de primeira aprendizagem, conteúdo por era e catálogo canônico com fontes próprias | Entregar a aquisição de cada repertório; nenhum NPC/candidato vira mestre aprovado por este fechamento |
| M11 + M06/M07 | Foco, arma/vetor, slots/mãos, provisões, troca/perda/reposição e interação com armadura/itens | Declarar completo o acesso por equipamento ou a recuperação após perda/morte |
| M06/M10/M11/M15 | Montagens persistentes: dono, etapas, consumo, duração, desmonte, cooperação e ciclo de vida | Usar uma montagem de Gintō/efeito persistente em mundo salvo |
| M14 + M06/M11 | Comandos dentro/fora do modo, prioridades de entrada, feedback de fase/recusa e informação autorizada | Aceitar controles e clareza para jogador/observadores |
| M15 + M09/M12 | PvE/PvP/party/pets, crédito, encontro, antidesconexão, recursos/penalidades de respawn e consequências como RAC-19 | Declarar multiplayer/campanha/economia de consequências completos |
| M16 + M06 | Relógio/estado persistente, migração, rede, implementação e evidências de cliente/servidor/observadores | Marcar implementado, testado ou aceito; reset de cooldown localizado continua divergência registrada |

A compatibilidade estrutural não transfere capacidades entre raças nem elimina exceções canônicas que ainda exigem fonte/ficha. Fichas de linhagem/Grimmjow e artes continuam adiadas por orientação do usuário; não são eliminadas do horizonte do projeto.

#### Menor amostra para futura validação e critérios

Usar os fundamentos já aprovados como referências comparativas: Shinigami armado/corporal e Shunpo; Hollow/Arrancar corporal, Cero e Sonído/Hierro/Pesquisa quando compatíveis; Quincy com arco, Hirenkyaku, Blut e uma fórmula cuja ficha esteja pronta; Fullbringer com fundamentos e Bringer Light horizontal/salto. Isso não seleciona um novo poder canônico, fórmula ou mestre e não promete implementar todas essas amostras numa única entrega. M16 escolherá a ordem de fatias jogáveis.

O avanço técnico exige comprovar, no recorte implementado: aprendizado sem requisito circular; cobrança única no momento devido; comportamento distinto de cancelamento de Cero/arco; colisão/queda; separação de mitigação/Postura/controle; informação sensorial limitada; coexistência/saída de estados; troca de modo sem bypass; e continuidade de tempos/efeitos entre eventos relevantes. Os critérios das seções anteriores continuam válidos. Não foram executados nesta revisão.

**Próximo módulo: M07 — Transformações e Domínio.** Começar pela estrutura comum que distingue evolução permanente, despertar adquirido e forma temporária; depois tratar acesso, ativação, manutenção, domínio, reversão e compatibilidades por raça. As decisões raciais já aprovadas são entradas, não novas perguntas. M07 ainda não tem documento próprio iniciado neste fechamento; sua abertura deve seguir o template do mapa e aprofundar as fontes necessárias. Nenhuma forma específica, custo universal de pontos, Segunda Etapa geral ou regra de regressão offline foi aprovada por antecipação.

**Limite do resultado:** fechamento de design conceitual, sem código, build, dependências, saves, commit/push ou testes de Minecraft. Documentos anteriores que dizem “M06 em especificação” são registros das rodadas anteriores; esta seção e o estado no início do módulo governam a continuidade atual. Manter implementação, teste e aceite separados.
