# Ciclo 79 — proposta de reformulação Benihime: selada, Shikai e Bankai

21/10/2026. **Planejamento solicitado, sem aplicar novas habilidades nesta rodada.** Branch Feature-Poderes-bankais, mudanças anteriores preservadas. Entrada/modelo/áudio da manifestação considerados bons no vídeo do ciclo77; não redesenhar essa cena para trocar o kit. Usuário quer ampliar a forma selada e tornar o repertório menos simples/fraco. Esta proposta amplia o item 4 da lista obrigatória, sem apagar itens posteriores.

## Inventário real do código atual

Fontes: `TechniqueDefinitions`, `TechniqueService`, `BenihimeTechniqueService`, `BenihimePolicy` e `StatusData`. Valores abaixo são **base de código**, antes de atributos, maestria, defesas e eventos; 1 coração = 2 pontos de vida. Recargas pressupõem 20 ticks/s; modo de cooldown de teste pode mascará-las.

**Selada:** modelo de bengala/espada disfarçada, combate básico vanilla com três gestos cosméticos do ciclo77. Sem quatro técnicas próprias registradas: definições Benihime existem apenas em Shikai/Bankai, e o executor especial exige uma dessas formas. Sem regeneração/aura Bankai na selada.

**Shikai — quatro teclas H/N/B/C:**

1. **H — Nake:** disparo/corte carmesim que percorre até 14 blocos; dano base8, cada alvo atingido uma vez, para em bloco. Custo12 Reiatsu; CD3s. Visual de corte vermelho.
2. **N — Chikasumi no Tate:** escudo carmesim frontal como construção visual; proteção atual de quatro segundos, capacidade24 e redução85% dos danos processados pelo escudo. Custo18; CD7s. Não tratar a malha frontal como prova de colisão direcional: o evento de absorção precisa de auditoria própria antes de converter em parry.
3. **B — Shibari:** rede de **20 blocos de diâmetro**, expansão inicial de um segundo e contração posterior; captura entidades hostis, aplica lentidão e puxa ao centro mantendo contato com o terreno. Vida da rede100ticks/5s depois da criação. Custo20; CD8s. Alcance de seleção12; centro/fixação no chão e captura preservados como referência homologada.
4. **C — Hiasobi, Juzutsunagi:** exige rede ativa, mantém a contenção enquanto aparecem nódulos/fios e detona todos os inimigos na área atual da rede após16ticks. Dano base14; custo25; CD10s. Sem rede não dispara. A dependência torna essa tecla pouco disponível fora da combinação.

**Bankai — quatro teclas H/N/B/C:**

1. **H — Dissecação:** escolhe um ponto até16blocos, área ofensiva de raio4; após12ticks aplica dano base12 e exposição por3s (+20% pelo processamento de dano do kit). Custo25; CD7s. Apresentação de incisão/costuras.
2. **N — Reconstrução:** cura aliado válido apontado ou o próprio usuário; alcance12, três pulsos de2HP em1,5s, totalaté6HP. Revalida distância/visibilidade e rejeita alvo com vida cheia. Custo28; CD12s. Visual de fios/pulsos de reparo.
3. **B — Reestruturação corporal:** cinco segundos com três cargas; próximos ataques corpo a corpo têm bônus base30% e efeito de incisão no acerto. Custo22; CD10s. É a maior candidata a mudança de comportamento: hoje o resultado principal é um buff, apesar do visual de braços/costuras.
4. **C — Abertura de caminho:** três cargas antes da recarga final de13s, custo30 por uso; desenha corredor de até12blocos e atravessa usuário/aliados elegíveis, incluindo direção vertical. Corredor dura3s; respeita espaço carregado, borda e colisões. Remove projéteis inimigos no trajeto e pode desfazer o escudo Benihime de inimigo interceptado. Não perfura livremente blocos sólidos.

**Passivas Bankai:** usuário recupera1HP/s; aliados de equipe e pets próprios válidos recebem1HP/2s num raio8 com visibilidade, sem empilhamento livre entre suportes. Não cura todos os mobs próximos indistintamente. Cura de aliados e regeneração pessoal foram pedidos expressamente e são mantidos nesta proposta. A manifestação acompanha o jogador; presença visual não é uma nona habilidade de dano automática.

## Pesquisa: referência da obra e adaptação

- [VIZ, volume73](https://www.viz.com/manga-books/manga/bleach-volume-73/product/5544/paperback): fonte editorial oficial para o contexto da revelação da Bankai, não documentação de valores de jogo.
- [Bleach Wiki, Kisuke Urahara](https://bleach.fandom.com/wiki/Kisuke_Urahara): índice secundário para Nake, escudo, Tsuppane e rede/detonação. Busca consultada; leitura integral retornou402, portanto não alegar inspeção direta das páginas do mangá. Referência resumida: energia carmesim na Shikai; rede seguida de explosões encadeadas; contramedida analisada, em vez de refletir tudo indiscriminadamente.
- [Jūgeki Byakurai](https://bleach.fandom.com/wiki/J%C5%ABgeki_Byakurai_%28Kaiz%C5%8D_Kid%C5%8D%29), [Gigai portátil](https://bleach.fandom.com/wiki/Kisuke_Urahara_vs._Yammy_Llargo) e [uso de Rikujōkōrō](https://bleach.fandom.com/es/wiki/Kisuke_Urahara/Historia): índices secundários para Kidō/equipamentos do personagem. Não são poderes inerentes da espada selada; colocá-los nesse loadout é adaptação de controle do mod.
- [Resumo com referências de capítulos](https://en.wikipedia.org/wiki/List_of_Bleach_characters): referência secundária à Bankai como reparo, ruptura e otimização; não licença para editar qualquer alvo/território sem limites.
- [Bandai Namco, Rebirth of Souls1.10](https://www.bandainamcoent.com/news/bleach-rebirth-of-souls-version-1-10-updates): fonte oficial de **outro jogo**, com Kamisori Benihime e limites de conversão de combos. Inspiração para variações/contrajogo; valores ou regras desse jogo não são cânone nem contrato do mod.

Todos os alcances, tempos, sequências adicionais e condições sugeridos a seguir são **adaptações propostas**, não poderes nomeados/valores canônicos. Não reproduzir animações, áudios ou assets comerciais desses jogos.

## Direção recomendada: quatro ações por forma, doze no conjunto

Manter H/N/B/C, guardaX e esquivaAlt+direção. A selada expressa esgrima, Kidō e preparo; Shikai controla energia vermelha e redes; Bankai remodela corpo/campo e participa fisicamente por mãos/costuras. Não copiar as mesmas quatro técnicas com dano multiplicado em cada forma.

### Selada — novo conjunto proposto

1. **H — Saque/estocada da bengala:** retirar lâmina, passo curto e corte preciso, seguido de recuperação. É adaptação de esgrima, sem inventar liberação elemental antes da Shikai. Visual deve mostrar bengala, cabo e lâmina coerentes.
2. **N — Jūgeki Byakurai:** tiro fino de Kidō, com preparação clara e alcance maior que o melee; janela para punir inimigo preso. Não é versão vermelha reduzida de Nake.
3. **B — Rikujōkōrō adaptado:** seis hastes luminosas prendem um alvo brevemente, reduzindo controle em bosses/PvP. É Kidō no loadout Urahara, não propriedade da bengala.
4. **C — Gigai portátil:** deixa isca visível e executa reposicionamento curto validado por colisão. Engana IA elegível, sem obrigar a mira de outro jogador, invulnerabilidade longa ou dano enorme gratuito. Difere da esquiva universal por criar uma isca e custar Reiatsu/recarga própria.

Desbloqueios precisam conversar com a progressão da equipe; não acrescentar mentor/proficiência novos por consequência automática. A primeira implementação pode liberar em bancada, mas a distribuição final deve seguir o contrato de ensino.

### Shikai — reformular mantendo a identidade

1. **H — Nake com variação Kamisori:** toque lança corte rápido; carga curta cria lâmina carmesim mais pesada e controlável no lançamento. Silhuetas e tempos diferentes, sem projétil que persegue automaticamente. Primeiro escolher se o carregado substitui o rápido ou usa segunda ativação; evitar teclado extra.
2. **N — Chikasumi + Tsuppane:** defesa sustentada curta com janela inicial de contramedida precisa. Sucesso devolve energia limitada/anula projétil compatível; falha conserva escudo comum. Lista de projéteis permitidos, teto de retorno e consumo do escudo, sem reflexão universal de golpes de boss/mods desconhecidos.
3. **B — Shibari:** preservar diâmetro20, chão e contração agressiva homologados; enfatizar abertura da malha, nós, fios tensos e captura legível. Melhoria principal é interação com os outros ataques, sem repetir o bug da rede enterrada.
4. **C — Hiasobi encadeada:** nódulos inflamam em sequência pelos fios e convergem nos presos. Fora da rede, proposta de pequena armadilha de nódulos para que a tecla não fique inutilizável; dentro, versão maior/mais eficiente. **Versão autônoma é adaptação nova**, exige fechar se o usuário prefere isso ou manter dependência da rede com recompensa maior.

### Bankai — mais ação da manifestação e efeitos concretos

1. **H — Dissecação por costuras:** mãos/gesto da Benihime dirigem incisões sucessivas num trajeto/área marcada; primeira fase marca/corta, reativação fecha a costura e causa ruptura. Substituir o único dano de raio4 por apresentação/combinação mais expressivas, mantendo teto de alvos e defesa/aliados. Sem execução inevitável ou membro decepado persistente.
2. **N — Reconstrução dirigida:** mãos envolvendo o usuário/aliado, linhas fechando ferimentos e cura forte perceptível; possível limpeza de debuffs selecionados. Preservar suporte, limitar cura simultânea e evitar remover qualquer maldição de outro mod. Movimento da manifestação precisa acompanhar o ponto de aplicação sem reabrir a invocação.
3. **B — Remodelação de combate:** braço reconstruído visivelmente, três golpes especiais com animações próprias e terceiro golpe de ruptura. O usuário continua lutando, com um efeito ativo reconhecível em cada carga, em vez de apenas receber30% invisível. Qualquer alcance/velocidade adicional exige hitbox/atributos servidor compatíveis; não conceder alcance só pelo desenho.
4. **C — Costura de passagem:** manter três cargas, verticalidade e uso por aliados; abrir uma fenda/corredor visual e permitir saída com corte direcional. Interação com barreiras energéticas por allowlist, sem destruir construções ou atravessar parede sólida arbitrariamente. Visual de costura abrindo/fechando diferencia de teleporte simples.

**Passivas mantidas:** regeneração pessoal e cura de aliados. Aumentos de números só depois de medir dano/cura efetivos contra alvos de mesmo nível, com e sem Resistência e maestria. Aumentar impacto ofensivo deve envolver acerto confiável, combinação e participação da manifestação, não apenas quatro multiplicadores maiores.

## Como executar depois de fechar o conjunto

1. Fechar quais quatro ações entram em cada forma e os modos de carga/reativação; manter os IDs existentes quando a habilidade evolui, para preservar maestria. Criar quatro IDs de selada sem sobrescrever os oito atuais; mapear migração se houver substituição real.
2. Contrato servidor: custos, recargas, cargas, alcance real, alvos/equipes/PvP, resiliência de bosses, colisão, interrupção, limpeza em morte/dimensão e limites por tick. HUD deve mostrar carga/reativação/sucesso de contramedida.
3. Prova vertical por habilidade: preparação → emissão → contato → efeito → recuperação. PlayerAnimator nas poses; Blockbench/GeckoLib quando houver necessidade de novos modelos/rigs, sem substituir globalmente o renderer do jogador. Manifestação atual preservada, com ações de mãos/braços adicionadas por estado; Photon/LDLib continua opcional para acabamento.
4. Ordem sugerida: selada4 → Shikai4 → Bankai4, cada fatia testável; depois balanceamento integrado e regressão de invocação, cura, rede e cargas. Não instalar novas bibliotecas por padrão.
5. Testar dano efetivo/tempo de abate e custo total, em vez de usar apenas alvo com1000HP e cooldown de laboratório. Comparar com Ryūjin/Senbon em condições iguais. Não depender do futuro sistema Mentor para medir a primeira fatia.

Status: proposta para discussão, **nenhuma dessas doze ações novas foi aplicada**. O kit atual descrito acima segue no código. A correção de coreografia Higashi é uma implementação separada, registrada no ciclo78.
