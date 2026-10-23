# Hollow: três variantes no mesmo mob

Implementação em 20/10/2026 na branch `feat/Jigui/new-hollow-model`.
Referência inicial: `8ace6520e67d27441974b9ad92b443ddb33f9882`; árvore inicialmente limpa.
Nenhum commit, instalação ou troca de branch foi realizado.

O ovo existente `bleachmod:hollow_spawn_egg` continua criando `bleachmod:hollow`.
Cada nascimento escolhe **brute**, **swift** ou **flying**, com probabilidade igual (1/3).
O sorteio independe dos demais membros do grupo e vale para ovo, nascimento natural,
spawner e `/summon` sem NBT. Não foram criadas novas entidades, ovos ou tabelas de loot.
As condições e os grupos de spawn anteriores foram mantidos.

| Variante | Vida (HP) | Dano por golpe (HP) | Velocidade base | Colisão: largura × altura |
|---|---:|---:|---:|---:|
| Bruto | 80 | 12 | 0,20 | 1,50 × 3,40 blocos |
| Ágil | 24 | 4 | 0,38 | 0,65 × 1,35 blocos |
| Voador | 40 | 7 | 0,27; voo 0,32 | 0,95 × 2,15 blocos |

Valores de dano são anteriores a armadura/defesas. A perseguição usa multiplicador
1,2 da IA existente. O voador usa `FlyingPathNavigation`, `FlyingMoveControl`,
movimento tridimensional, patrulha acima do terreno e imunidade a dano de queda.
A colisão permanece ativa: ele não atravessa blocos. Os terrestres usam navegação
de solo e gravidade. O alcance corpo a corpo considera a largura das entidades e
exige linha de visão. O boss mantém modelo, atributos e comportamento anteriores;
esta primeira etapa altera apenas o Hollow padrão.

`HollowVariant` é uma string NBT persistida e sincronizada por `SynchedEntityData`.
Salvar/recarregar não sorteia novamente nem cura o mob. Mundos antigos e ids
inválidos usam `brute`. Atributos, dimensões e navegação são reaplicados no load.
O nome público continua sendo Hollow, preservando a identidade do mob.

## Uso e edição

```mcfunction
/give @s bleachmod:hollow_spawn_egg
/summon bleachmod:hollow ~ ~ ~
/summon bleachmod:hollow ~ ~ ~ {HollowVariant:"brute"}
/summon bleachmod:hollow ~ ~ ~ {HollowVariant:"swift"}
/summon bleachmod:hollow ~ ~ ~ {HollowVariant:"flying"}
```

As fontes editáveis estão em `tools/models/source/hollow_*.bbmodel`.
Abra no Blockbench; os bones seguem a mesma nomenclatura dos exports GeckoLib.
As animações compartilhadas ficam em
`src/main/resources/assets/bleachmod/animations/hollow.animation.json` e podem ser
importadas no editor Bedrock/GeckoLib, associadas ao rig aberto.
Os arquivos `.geo.json` e PNGs usados pelo jogo ficam em `assets/bleachmod/geo/`
e `assets/bleachmod/textures/entity/`.

`python tools/models/hollow_variants.py` gera as fontes e os exports originais;
**sobrescreve esses arquivos**. Depois de editar manualmente um `.bbmodel`, exporte
o resultado pelo editor ou transporte a alteração para o gerador antes de executá-lo.
`python tools/models/hollow_variants.py --check` verifica reprodutibilidade,
hierarquia, tamanhos e UVs. Nenhum pacote Python externo é necessário.

Os três modelos têm máscaras ósseas, olhos, dentes, garras e um buraco realmente
vazado no tórax. Bruto: ombros largos, chifres e braços pesados; ágil: corpo pequeno,
corcova e cauda articulada; voador: membranas, ossos das asas e garras pendentes.
GeckoLib controla repouso, caminhada, voo e golpe disparado pelo servidor.
As referências fornecidas pelo usuário foram inspiração visual; os assets foram
criados no projeto, sem copiar pixels das imagens.

## Ambiente e reprodução

Minecraft 1.20.1, Forge 47.4.10, Gradle 8.8, Java 17.0.20.1 (Temurin), GeckoLib
4.8.x já instalado, perfil base sem Photon. Nenhuma dependência foi adicionada.
O Gradle foi usado offline; o verificador de versão interno do Forge fez a consulta
usual na inicialização do cliente. Não houve download de dependências Gradle.

Com Java 17 selecionado:

```powershell
./gradlew.bat --offline check --console=plain
./gradlew.bat --offline runGameTestServer --console=plain
./gradlew.bat --offline build --console=plain
./gradlew.bat --offline -PhollowQa=true runClient --console=plain
python tools/models/hollow_variants.py --check
```

O cliente opt-in cria um mundo novo `Hollow-QA-<timestamp>` em
`run/hollow-qa/base`, prepara três exemplares e captura frente, costas e movimento.
Nunca abre um save existente. O servidor de GameTests usa `run-gametest/base`.
Todo o laboratório exige ambiente de desenvolvimento e flag explícita.

Nesta máquina, o sandbox inicialmente impediu a escrita do lock em `.gradle`.
Após autorização de execução externa, Java apresentou `Unable to establish
loopback connection` / `UnixDomainSockets.connect0: Invalid argument`.
A correção local foi apontar `-Djdk.net.unixdomain.tmpdir` para o caminho curto
ASCII do `build` (`C:\Users\USURIO~1\DOCUME~1\GitHub\BLEACH~1\build`) em
`JAVA_TOOL_OPTIONS`, junto de `WindowsSelectorProvider` e preferência IPv4.
Não foi alterado PATH, Java ou configuração global. A baseline inicial ficou
bloqueada pelo ambiente; a primeira execução efetiva já incluiu a feature.

## Evidências e alcance da validação

As imagens nesta pasta são cópias sem edição dos PNGs de `Screenshot.grab` no
cliente real. Origem:
`run/hollow-qa/base/hollow-evidence/Hollow-QA-1792516893296/`.
`frames.jsonl` guarda ticks, origem do framebuffer, GPU e FPS observados.
Cliente 1280 × 720, distância de renderização 6, AMD Radeon RX 7600, heap máximo
2 GiB; as amostras registraram 118–119 FPS, sem constituir benchmark de carga.

- `screenshots/01-front.png`: máscaras, três escalas e silhuetas distintas.
- `screenshots/02-rear.png`: cauda, asas e continuidade dos furos através do tórax.
- `screenshots/03-motion-280.png` a `03-motion-340.png`: progressão temporal real,
  com voador suspenso e asas em diferentes poses, além de deslocamento terrestre.

Inspeção visual: silhuetas/escala **4/5**, legibilidade das máscaras **3/5**,
animação amostrada **3/5**. É uma primeira versão de arte em volumes cúbicos;
as capturas não demonstram movimento contínuo nem validam contato do golpe.
O comportamento aéreo e os atributos são cobertos adicionalmente por GameTests.
Dia/chão plano/singleplayer base: observado. Noite, terreno irregular, espaços
estreitos, vídeo, batalha extensa, dois clientes e perfil Photon: **pendentes**.
O teste de sincronização aplica os valores de entidade do pacote a um observador;
isso não equivale a uma sessão multiplayer com duas conexões reais.

Resultados finais e logs são registrados em `validation.txt` nesta pasta.
