# Ciclo 41 — Byakuya: pesquisa e possibilidades de kit

Data: 15/10/2026. Branch: `Feature-Poderes-bankais`. Pesquisa/planejamento solicitados pelo usuário, **sem implementação de poderes e sem aprovação final dos slots**. Git inicialmente limpo e alinhado à referência local de origin.

## Direção proposta

Byakuya é o próximo kit, antes de Urahara e Ishida. Recomenda-se usar Senbonzakura como identidade de **controle de lâminas a média distância**, com alternativas de cerco, proteção ativa e combate concentrado no Senkei. Diferenciar o kit das áreas persistentes de fogo/gelo existentes. Não reduzir a Bankai a um aumento genérico de dano.

O [perfil oficial do anime](https://bleach-anime.com/en/character/) confirma a associação de Byakuya a Senbonzakura. As descrições detalhadas abaixo usam referências secundárias que apontam capítulos/episódios; separar mangá/anime de técnicas criadas para jogos. Não assumir que toda variante listada em jogos pertence ao repertório da obra.

## Referências da obra

| Referência | Característica resumida | Possibilidade no mod — proposta |
| --- | --- | --- |
| Senbonzakura/Shikai | Lâmina fragmentada; fragmentos lembram pétalas. | Fluxos direcionais e convergência, com alcance/linha de visão definidos. |
| Senbonzakura Kageyoshi/Bankai | Volume maior de lâminas, usado ofensiva e defensivamente. | Mais cobertura e padrões distintos; orçamento visual independente da quantidade de fragmentos fictícios. |
| Gōkei | Cerco esférico que converge sobre o alvo. | Formação anunciada, intervalo de reação e dano limitado; não uma execução inevitável. |
| Senkei | Pétalas reunidas em espadas, priorizando ofensiva. | Estado temporário de duelo, com espadas ao redor e perda de acesso à proteção de pétalas. |
| Ikka Senjinka | Lançamento conjunto das espadas de Senkei. | Finalização contextual do estado Senkei. |
| Shūkei: Hakuteiken | Concentração das espadas em uma lâmina; asas/halo brancos. | Preparação curta, golpe de alto compromisso e encerramento da concentração após a tentativa. Asas visuais não concedem voo automaticamente. |

Fontes: [Senbonzakura e Gōkei no perfil de Byakuya](https://bleach.fandom.com/wiki/Byakuya_Kuchiki), [Senkei e Ikka Senjinka](https://bleach.fandom.com/wiki/Senkei), [Shūkei: Hakuteiken](https://bleach.fandom.com/wiki/Sh%C5%ABkei:_Hakuteiken).

Senkei e Ikka Senjinka não devem ser confundidos: montar o conjunto de espadas e dispará-lo são ações diferentes. A referência de Ikka Senjinka consultada aponta o capítulo 671 do mangá; a pesquisa desta entrega não confirmou episódio de adaptação dessa técnica. Não rotular a proposta inteira como repertório exclusivamente verificado no anime.

## Shikai — alternativas de aplicação

Os nomes em português abaixo são rótulos funcionais propostos para o mod, não nomes canônicos de técnicas independentes:

1. **Onda de pétalas:** ataque frontal amplo, voltado a pressionar mais de um alvo.
2. **Corrente concentrada:** fluxo estreito para precisão, com mais dependência de mira e duração limitada.
3. **Convergência de lâminas:** aproximar fragmentos de direções distintas sobre uma região selecionada; diferente de reproduzir Gōkei completo no Shikai.
4. **Guarda de pétalas:** proteção ativa curta, com custo de reiatsu e limites próprios; sem invulnerabilidade automática.
5. **Construção perfurante:** alternativa ofensiva inspirada no controle mais refinado do Shikai, caso o usuário prefira menos defesa.

Recomendação inicial: onda, corrente, convergência e guarda. A construção perfurante é alternativa ao terceiro ou quarto slot, não uma quinta tecla obrigatória.

Mira pode ser fixada ao lançar (mais previsível) ou guiada durante uma canalização (maior controle, implementação mais exigente). Recomendação: começar com onda/convergência fixas e corrente guiada somente se esse controle for prioridade aprovada. Não adicionar busca automática irrestrita de alvos.

## Proposta de distribuição em quatro slots

| Slot | Shikai — rótulo de adaptação | Bankai — proposta de execução |
| --- | --- | --- |
| 1 | Onda de pétalas | Maré de lâminas: cobertura ampla em direção escolhida. |
| 2 | Corrente concentrada | Gōkei: cerco com formação e convergência anunciadas. |
| 3 | Convergência de lâminas | Senkei: entrar em estado ofensivo; reativação candidata a Ikka Senjinka. |
| 4 | Guarda de pétalas | Shūkei: Hakuteiken: concentrar lâminas para uma finalização. |

Assim, o kit pode contemplar os recursos icônicos sem criar quinta tecla. **Reativação de Senkei para Ikka Senjinka é uma decisão de interface do mod**, não uma regra de ativação canônica. Exige estender explicitamente as definições comuns para o contexto, custos/cooldowns e HUD, sem duplicar valores ou reutilizar ID de outra habilidade.

Alternativa mais simples: manter Senkei sem reativação na primeira entrega e incluir Ikka Senjinka em um segundo ciclo. Alternativa de foco ofensivo: usar Ikka como slot próprio, trocando uma técnica da tabela. A seleção continua pendente do usuário.

Forma selada: recomendação de espada física própria e prática normal, sem fragmentação antes do Shikai. Não preencher quatro slots selados com Kidō só para completar a grade. A política atual de definições/cobertura deverá aceitar explicitamente esse repertório vazio se a decisão for aprovada.

## Técnicas de Byakuya que pertencem a outros sistemas

Shunpo, Senka e Utsusemi são referências de mobilidade; Byakurai, Sōkatsui, Rikujōkōrō e Dankū integram o repertório de Kidō usado pelo personagem. [Referência das técnicas aprendidas](https://bleach.fandom.com/wiki/Byakuya_Kuchiki).

Podem compor um personagem inspirado nele no futuro, mas não serão concedidas automaticamente pelo vínculo com Senbonzakura. Preservar as separações do M06 entre poder principal, técnica aprendida, disciplina, proficiência e maestria. A implementação dos novos sistemas adiados pela equipe não precisa ser antecipada para fazer o kit de lâminas.

## Regras de adaptação recomendadas

- Petalas são pequenas lâminas na fantasia; não criar milhares de entidades físicas. Representar seus padrões por volumes de efeito no servidor e visuais limitados no cliente.
- Custo das técnicas em reiatsu, com cooldown e eventual manutenção declarados. Esquiva, Postura e bloqueio universal mantêm suas regras próprias.
- A guarda de pétalas não deve multiplicar mitigação com X, armadura de gelo ou outra defesa sem política explícita. Recomenda-se uma única defesa especial ativa por vez, com compatibilidade definida antes do código.
- A zona de segurança de Senbonzakura protege o usuário das próprias lâminas na referência; não significa imunidade contra inimigos. Para gameplay, proteger dono/aliados dos próprios efeitos e considerar uma zona de menor pressão perto do usuário. Raio e interação com o ataque físico ainda precisam de decisão. [Referência](https://bleach.fandom.com/wiki/Byakuya_Kuchiki).
- Gōkei e Senkei não devem alterar blocos permanentemente ou prender jogadores com uma arena de bedrock. Círculos/espadas podem delimitar visualmente a área, com regra de saída explicitamente definida.
- Hakuteiken e Ikka precisam de aviso, possibilidade de falhar e encerramento claro. Não copiar letalidade cinematográfica como morte instantânea universal.
- O aumento do Shikai após treinamento pode ser traduzido em desbloqueios/modos, se aprovado, em vez de outra identidade de zanpakutō ou novo nível global. Não adicionar bônus de atributos por maestria implicitamente.
- Visual novo de Senbonzakura deve respeitar seus próprios modelos/texturas; preservar Asauchi/Ryujin/Hyōrinmaru e a geometria de bainha já homologada. Aparência da arma fragmentada exige tratamento visual próprio e teste em primeira/terceira pessoa.

## Pontos para fechar antes de implementar

1. Aprovar os quatro slots de cada forma e se Ikka entra já como reativação de Senkei ou em ciclo posterior.
2. Escolher mira fixa/guiada e seleção de alvo/área de Gōkei.
3. Definir a proteção de pétalas e como ela interage com Postura/guarda universal.
4. Definir duração/saída de Senkei e se Hakuteiken exige Senkei ativo ou pode concentrar a Bankai diretamente como adaptação.
5. Aprovar tabela de custo, alcance, dano, manutenção, cooldown e maestria; materiais e orçamento visual; migração/rede e roteiro de validação.

Esses pontos completam o [contrato de novos poderes](contrato-criacao-novos-poderes.md). A pesquisa desta entrega não autoriza iniciar o kit com uma tabela ainda não escolhida.

## Validação desta entrega

Somente documentos em `Docs/poderes`, com revisão de links locais/diff. Sem nova build, testes de código ou alterações no JAR. Base funcional permanece ciclo 40, protocolo 2.11/schema 8; 80 regressões e 73 GameTests obrigatórios aprovados na entrega anterior.
