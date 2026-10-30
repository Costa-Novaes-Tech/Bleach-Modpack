# Academia — primeiro incremento técnico

> Registro histórico deste primeiro incremento. Na continuação do mesmo dia,
> o painel Formação foi substituído pelo livro de jornada e a abertura passou
> a seguir a história no Overworld. Ver
> [direção e implementação atuais](abertura-shinigami-e-livro-2026-10-28.md).

28/10/2026, branch `feat/Jigui/classes`, base `8df8a64d`. Pedido: começar
implementações básicas do dossiê sem depender de escolhas novas de conteúdo.

## O que entrou

- `AcademyData`: registro pessoal versionado da formação. Um Shinigami novo
  aguarda orientação; a API de início do treino exige esse estado e é idempotente.
  Não há graduação automática, prova, compra ou recompensa implícita.
- Integração ao save completo, cópia de PlayerData e sincronização do dono.
  Atualizações parciais de recursos não apagam a formação. Limpezas transitórias
  de morte/dimensão não reiniciam o registro permanente.
- Migração aditiva: saves existentes sem registro, inclusive sem schemaVersion,
  ficam identificados como anteriores à formação, sem apagar poderes ou presumir
  matrícula/conclusão. Dados desconhecidos não concedem avanço.
- Painel “Formação” pelo diário dos Shinigamis: situação atual e apresentação
  de Zanjutsu, Hakuda, Hohō e Kidō. Leitura informativa, sem ganho de domínio.
  Voltar retorna ao diário. Textos pt_br/en_us.
- Dois GameTests de persistência, limpeza, cópia, dados inválidos, repetição e
  migração; laboratório gráfico ampliado para conferir sync e navegação reais.

O registro usa um CompoundTag adicional `academy`, com versão interna 1.
O schema principal 9 e o formato dos pacotes existentes permanecem; o tag
opcional viaja por PlayerSync/ProgressionSync e não é distribuído a observadores.
Ainda não existe solicitação C2S para progredir a formação. O futuro instrutor
deverá validar a interação no servidor e solicitar a sincronização.

## Arquivos principais

- `common/data/AcademyData.java` e `PlayerData.java`.
- `common/network/SyncHelper.java`.
- `client/gui/AcademyScreen.java`, `JournalScreen.java`, `ClassSelectionQaBootstrap.java`.
- `gametest/AcademyGameTests.java` e traduções pt_br/en_us.

Caminhos Java relativos a `src/main/java/com/bleachmod/`.

## Verificação e reprodução

Linha de base: `check`, 146 regressões aprovadas. Primeira implementação:
`check build runGameTestServer`, 146 regressões e 225 GameTests aprovados.
Cliente Forge em mundo novo: sincronização da formação, acesso pelo diário,
retorno e inspeção em GUI 2/3 aprovados, exit 0.

Rodada final após reforçar migração sem versão e sincronização incremental:
146 regressões e 225 GameTests aprovados, exit 0. Cliente repetido após a
revisão do texto informativo, exit 0; painel e capturas finais inspecionados.
As 11 chaves de tradução existem nos dois idiomas e os JSONs foram parseados.
JAR final: `build/libs/bleachmod-0.2.0.jar`, SHA-256
`3B59654CF46BC1268FE9ABC4147B900334CBFFF0BA2055B6E07CAF615C581B5F`.

```powershell
$env:JAVA_HOME = 'C:/Users/jguic/.jdks/temurin-17.0.20.1'
./gradlew.bat --offline check build runGameTestServer --console=plain
./gradlew.bat --offline -PclassSelectionQa=true runClient --console=plain
```

JDK Temurin 17.0.20.1, Forge 47.4.10, Minecraft 1.20.1. Execução offline;
aprovação do cache Gradle fora do sandbox utilizada. Mundo gráfico descartável
`Class-Selection-QA-*`, servidor de testes em `run-gametest/base`.
Logs e capturas ficam em `Docs/qa/academia/2026-10-28-base/`.
As imagens são framebuffers Minecraft; o laboratório aciona as telas/widgets.
Sem validação de dois clientes ou Photon nesta rodada.

## Escopo restante

Este é o registro e a apresentação da formação, ainda sem currículo executável.
Instrutor, recepção, mapa/percurso, shinai e sua compatibilidade/alvo de treino,
exercícios, SP/Shunpo e critérios de Asauchi/graduação continuam nas fichas do
[dossiê](etapa-2-academia-shinigami-2026-10-28.md). A entrega imediata da Asauchi
do protótipo não foi removida sem uma rota substituta funcional.

Nenhum teleporte, NPC temporário de campanha, classe extra, preço ou prêmio foi
inventado para preencher essas dependências. Nenhum save existente foi aberto,
nenhum commit/push realizado. Logs compactados rastreados da raiz foram
atualizados pela execução habitual do Gradle, sem alteração intencional.
