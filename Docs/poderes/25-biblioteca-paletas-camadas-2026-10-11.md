> **Estado vigente — ciclo 27, 11/10/2026:** cortes, barreira, Zona Glacial, impacto de controle e trilha do dragão da Hyōrinmaru usam o emissor comum, mantendo aparência e combate. [Arquivos, lógica, valores e testes](27-migracao-slots-gelo-2026-10-11.md). Clima e terreno conservam sua lógica; registros abaixo são históricos.

> **Estado vigente — ciclo 26, 11/10/2026:** aura e liberação da Hyōrinmaru integradas ao emissor comum com audiência próxima e orçamentos explícitos, preservando a aparência aprovada. Bancada continua privada. [Arquivos, lógica, limites e roteiro](26-integracao-gelo-emissor-comum-2026-10-11.md). Os ciclos anteriores abaixo são históricos; Aizen e narrativa permanecem futuros.

# Ciclo 25 — Biblioteca comum de efeitos e composição

Data: 11/10/2026. Branch: Feature-Poderes-bankais. Continuidade autorizada após a bancada vanilla.

## 1. Entrega e fronteiras

Biblioteca reutilizável de paletas, geometria e camadas, conectada à bancada. A escolha visual final ainda depende de comparação pelo usuário. Não altera os poderes homologados, modelos, texturas, maestria, custos, cooldowns, clima ou terreno. Aizen continua futuro. Treinos/provas de aquisição narrativa continuam aguardando especificação da equipe.

Não se migram emissores das habilidades existentes nesta entrega: a integração em produção deve respeitar sua aparência homologada e ser feita por família após escolha visual. Este ciclo fornece API comum e demonstrações reais para decidir essa integração.

## 2. Contrato de paletas e formas

| Paleta | Material principal | Acento | Resíduo | Som inicial |
| --- | --- | --- | --- | --- |
| fire | flame | dust laranja | smoke | Blaze |
| ice | snowflake | dust azul | cloud | vidro |
| reiatsu | end_rod | dust violeta | enchant | Beacon |

Paletas são propostas iniciais revisáveis. Reiatsu violeta não define uma cor obrigatória para todo personagem ou uma habilidade de Aizen. Não foram importados assets externos.

| Forma visual | Geometria | Movimento |
| --- | --- | --- |
| aura | espiral de raio 0,9 e altura até 3 | fase gira com idade do efeito |
| cut | arco de largura 4 e altura até 2,3 | apresentação contínua para comparação |
| beam | linha de 6 blocos com espiral estreita | fase da espiral avança |
| release | anel com elevação | raio aumenta de 1 até próximo de 3 em ciclos de 20 ticks |

São 12 combinações. Em cut/release não se executa um ataque nem transformação: são descritores visuais. A bancada mantém cinco segundos para permitir comparação; futuros lançamentos poderão escolher uma duração compatível com sua técnica.

Cada emissão distribui quatro de cada seis amostras no material principal, uma no acento e uma no resíduo. A distribuição pode ter pequena diferença na última sequência incompleta. As três camadas compartilham o orçamento, sem triplicar o número de partículas.

## 3. API e lógica, passo a passo

1. EffectLibrary.java: enums fechados Palette/Shape, Profile imutável e ID estável paleta/forma; resolução rejeita valores desconhecidos. Geometria é independente da seleção de partículas. Paletas usam apenas recursos vanilla.
2. VisualFrame.java: contador compartilhado de um frame, limite absoluto 48. Intensidades 1/2/3 dão orçamento 16/32/48. Recusa posições não finitas, spread/speed inválidos, distância acima de 32, mundo incompatível, dono morto/espectador e chunks indisponíveis. Não carrega chunks nem muda mundo.
3. EffectLibrary.emit: seleciona material/acento/resíduo e usa o mesmo VisualFrame para todas as camadas. O orçamento pertence ao frame; emissores adicionais que recebam esse mesmo objeto não podem ultrapassá-lo.
4. VanillaAssetBench.java: tanto presets antigos como composições usam VisualFrame. Retém uma sessão por dono, oito simultâneas, 100 ticks, intervalo quatro, origem quatro blocos à frente, som local único e limpeza anterior.
5. AssetBenchCommands.java: acrescenta palettes e compose. Requer OP e jogador executor; comando inválido não substitui sessão. preview/list/status/stop continuam compatíveis.
6. Traduções pt_br/en_us: ajuda de paletas e formas, aproveitando feedback de início/status existente.
7. MasteryGameTests: preparo da célula aberta remove zumbis de fixtures anteriores e aguarda cinco ticks para registrar o alvo; alteração somente no teste de alvo único, sem alterar o executor. AssetBenchGameTests/MvpRegressionTest: validam todas as combinações, orçamento compartilhado, posições inválidas, rejeições/permissões, ausência de mutação de personagem e manutenção dos testes anteriores.

Java principal em src/main/java/com/bleachmod/common/visual e server/commands. Testes de integração em gametest e regressões em src/test/java/com/bleachmod. Documentação alterada apenas em Docs/poderes.

O limite é por objeto VisualFrame, não um limitador global retroativo de todos os efeitos do mod. Na bancada, deduplicação e uma sessão por jogador garantem um frame por intervalo. Futuras técnicas devem reutilizar o mesmo frame entre camadas e implementar sua própria duração, audiência e encerramento; não criar vários frames para contornar o orçamento. Nenhum efeito é acoplado a dano, prática ou recursos.

API de composição para futuras integrações: criar Profile, obter um VisualFrame para o receptor/mundo, chamar EffectLibrary.emit com origem, direção horizontal normalizada, intensidade válida e idade do efeito. A API atual envia somente ao receptor; broadcast de combate e políticas de distância são próximos contratos, não comportamento implementado aqui.

## 4. Comandos e homologação

```text
/bleachdev assets palettes
/bleachdev assets compose fire aura 1
/bleachdev assets compose ice cut 2
/bleachdev assets compose reiatsu beam 3
/bleachdev assets compose ice release 2
/bleachdev assets status
/bleachdev assets stop
```

Compare as três paletas na mesma forma/intensidade. Depois compare aura/cut/beam/release da paleta preferida. Use intensidade 1 antes de 2/3, mesmo cenário/câmera e configuração de partículas. F3 ajuda a observar FPS; status conta solicitações, não desempenho ou partículas realmente desenhadas.

Verifique contraste sobre terreno claro/escuro, ocupação da visão, percepção de movimento e persistência do resíduo. Cada camada deve enriquecer o efeito sem ocultar o alvo. Em dois jogadores, confirme privacidade das demonstrações. Use stop e aguarde partículas previamente enviadas desaparecerem naturalmente.

Reexecute presets antigos, confirme rejeição sem OP e com paleta/forma/intensidade inválidas. Confirme ausência de dano, consumo, alterações no mapa, clima ou maestria. Composições substituem o preset anterior, sem acumular sessões.

Próximo gate: escolher família e combinações favoritas. Então integrar gradualmente uma manifestação visual concreta, preservando comportamento de combate e números homologados. Modelos/superfícies do catálogo 24 continuam opções para adaptações futuras, sem substituição automática.

## 5. Validação

Validação final: check, build e runGameTestServer offline aprovados; 63 regressões e 48 GameTests obrigatórios passaram. Log local: build/cycle25-validation.log. git diff --check aprovado; modelos/texturas e documentos fora de Docs/poderes intactos. JAR: build/libs/bleachmod-0.2.0.jar. Nenhuma homologação visual ou medição de FPS é afirmada nesta entrega.
