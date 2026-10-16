# Ciclo 52 — Compatibilização inicial Better Combat / Asauchi

Data: 16/10/2026. Branch `Feature-Poderes-bankais`, base limpa `6770df9`. Usuário autorizou começar a compatibilização enquanto providencia o commit da implementação Zaraki/Nozarashi da equipe. Esta entrega prepara uma base independente e um piloto; não implementa Zaraki nem presume que os arquivos do colega já foram recebidos.

## Dependências fixadas

| Componente | Artefato Modrinth fixado |
|---|---|
| Better Combat | `maven.modrinth:better-combat:1.9.0+1.20.1-forge` |
| PlayerAnimator | `maven.modrinth:playeranimator:1.0.2-rc1+1.20-forge` |
| Cloth Config | `maven.modrinth:cloth-config:11.1.136+forge` |

Forge 47.4.10 / Minecraft 1.20.1 / Java 17 mantidos. `build.gradle` usa repositório Modrinth limitado ao grupo correspondente; versões ficam em `gradle.properties`. `mods.toml` passa a exigir Better Combat 1.9.x, cuja metadata exige PlayerAnimator/Cloth Config. Os três mods externos não foram incorporados ao JAR do Bleach. MixinExtras presente no grafo empacotado do Better Combat não foi adicionado em outra versão concorrente.

Fontes consultadas: [código Better Combat 1.20.1](https://github.com/ZsoltMolnarrr/BetterCombat/tree/1.20.1), [distribuição oficial](https://modrinth.com/mod/better-combat), [PlayerAnimator](https://modrinth.com/mod/playeranimator), [Cloth Config](https://modrinth.com/mod/cloth-config). O JAR externo Better Combat do pacote foi comparado com o SHA512 publicado pela API Modrinth. Nenhum código do Zaraki foi copiado ou inventado.

Rede Bleach agora **2.18**, schema **8**. Cliente e servidor devem atualizar o Bleach e ter as dependências. O JAR isolado desta versão não constitui instalação completa.

## Piloto: Asauchi

Perfil explícito em `data/bleachmod/weapon_attributes/asauchi.json`, herdando a sequência de espada do Better Combat: corte horizontal de um lado, corte do outro lado e estocada. Alcance declarado **3 blocos**, três multiplicadores **1,0**, sem dual wield e sem transformar o item em arma de duas mãos. Offhand continua disponível; o piloto não cria um segundo vínculo de arma. Modelos/texturas homologados da Asauchi e da bainha não foram editados.

Preparação/recuperação e animações vêm do perfil herdado e da velocidade de ataque da arma; não foi acrescentado finalizador de dano aumentado. Dados de perfil podem ser refinados após playtest. As formas da Asauchi continuam com os bônus já existentes e sem slots elementais.

O fallback automático do Better Combat para os demais itens do namespace `bleachmod` é bloqueado por uma adaptação de registro. Portanto, Ryūjin, Hyōrinmaru, Senbonzakura e Benihime continuam no caminho anterior até revisão explícita. A biblioteca ainda tem seu comportamento normal para armas vanilla/outros mods; a limitação do piloto refere-se às armas do Bleach. Quando Zaraki chegar, sua inclusão nessa política será conciliada com o código/recursos do colega.

## Adaptação e responsabilidades

`BetterCombatCompat` mantém um contexto de servidor delimitado por cada requisição de golpe físico. O Better Combat 1.9.0 define seu `comboCount` antes de iterar os alvos e retorna a -1 após o processamento. Uma pequena injeção no método que ele acrescenta ao jogador acompanha essa fronteira. Isso foi verificado no GameTest usando o setter real e ataques reais, não apenas chamando manualmente a política de cobrança.

- Stamina continua cobrando **até 4 por tentativa elegível**, uma vez por golpe individual. Dois alvos no mesmo golpe não custam oito. Cada golpe seguinte do combo tem sua cobrança própria. Whiff sem alvo elegível continua sem cobrança por esse ponto, conforme política anterior; criativo preserva a exceção existente.
- Lista de IDs já processados impede atingir novamente o mesmo alvo dentro da mesma requisição. Limite de 64 alvos por contexto. O contexto não entra no save nem numa mensagem nova de cliente.
- Guarda, Guard Break, esquiva, carregamento, dash de fogo e técnicas ocupadas impedem o acerto no servidor. As condições são reavaliadas no instante do ataque: iniciar antes de entrar num estado bloqueado não autoriza acertar depois.
- Servidor também verifica alvo elegível, distância até a caixa do alvo (3,05 blocos com tolerância), linha de visão e rejeita alvos claramente atrás do atacante. Não se afirma reproduzir integralmente no servidor a hitbox geométrica do cliente Better Combat; continuam relevantes as validações do próprio backend e o teste com clientes reais.
- Dano continua passando uma vez pelo `CombatEvents` e pelas fórmulas Bleach. Sem segundo dano manual nem segundo multiplicador de combo acrescentado por nós. Prática continua passando pelos limites existentes; efeito de animação não concede XP.
- Dual wield fica desativado quando uma das mãos contém arma/item Bleach, mantendo a regra de uma arma vinculada. Não altera a política global de outras armas.

No cliente, evento de início do Better Combat e tick local cancelam preparação de Asauchi se guarda/estado incompatível estiver ativo. Usam também snapshot visível de guarda/quebra e tecla X. Essa apresentação não substitui a checagem de servidor. Para o ataque físico piloto, entrar na guarda/esquiva antes do acerto impede o golpe; isso não concede cancelamento universal às técnicas elementais.

### Pontos de integração

- `common/compat/BetterCombatCompat`: contexto, permissões, limite/deduplicação de alvos e cobrança.
- `common/BasicAttackStamina`: consulta se a tentativa já foi cobrada naquele contexto.
- `client/compat/BetterCombatClientCompat` e registro em `BleachClient`: cancelamento visual de preparação incompatível.
- Três mixins: fronteira de `setComboCount`, exclusão de fallback de kits ainda não migrados e bloqueio de dual wield Bleach.
- `bleachmod.mixins.json`: configuração comum, sem classe cliente no servidor; manifesto e argumentos de execução declarados no Gradle.

As injeções dependem da API/estrutura da linha fixada do Better Combat e têm falha explícita se o alvo esperado desaparecer. Rever esses pontos ao trocar de versão ou conciliar a implementação do colega. Não declarar compatibilidade arbitrária com PAL, Epic Fight ou outra versão de Better Combat.

## Build e pacote de teste

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot'
# Primeira resolução precisa de rede; depois do cache, offline funciona:
.\gradlew.bat check build prepareCombatTestPack runGameTestServer --offline --console=plain
```

`prepareCombatTestPack` gera `build/combat-test-pack/mods` com o Bleach e os três mods externos **originais**, em vez dos JARs remapeados de desenvolvimento. Copiar os quatro arquivos para a instalação Forge de teste e para o servidor correspondente, substituindo o Bleach anterior e evitando duplicatas desses mods. O ZIP local, quando presente, contém essa pasta `mods` pronta para extrair. Não modifica automaticamente a instalação usada pelo usuário.

Para testar: personagem Shinigami, `/give @s bleachmod:asauchi`, vínculo `/bleachdev zanpakuto bind @s asauchi`, arma na mão principal. Testar os três movimentos em sobrevivência, contra um alvo e um grupo; confirmar custo por golpe. Conferir X e Alt+direção antes/durante a preparação, forma/item, offhand, primeira/terceira pessoa, modelo slim/normal e outro cliente observando. Depois conferir rapidamente as quatro zanpakutō não migradas, principalmente alcance/passiva Senbonzakura e golpes durante técnicas.

## Validação e próximos passos

**Check/build, pacote de mods, 99 regressões e 118 GameTests obrigatórios aprovados**, com dependências carregadas e mixins aplicados no servidor de testes. Log `build/cycle52-validation.log`. Cinco GameTests novos verificam perfil/isolamento/dual wield, fronteira real do contexto e cobrança por múltiplos alvos, estados bloqueados, alvos distantes/atrás e mudança de estado após o início. Uma regressão nova cobre a política pura de estados. Manifesto, configuração de mixins, classe do adaptador e perfil conferidos dentro do JAR.

Isso valida o núcleo de servidor no ambiente de desenvolvimento. **Ainda não houve playtest de cliente, teste com dois clientes reais ou validação visual da bainha/poses.** Não declarar homologação completa do Better Combat ou da etapa 1 do roadmap com base apenas nos GameTests.

Próximo passo quando o commit Zaraki estiver disponível: identificar branch/commit e versões, comparar Gradle/mixins/perfis/modelos/animações e integrar o que já existe na equipe. Resolver conflitos por responsabilidade, sem dois executores para o mesmo golpe e sem perder poderes/progresso. Não antecipar refatoração de gelo/fogo antes de estabilizar esse contrato comum.
