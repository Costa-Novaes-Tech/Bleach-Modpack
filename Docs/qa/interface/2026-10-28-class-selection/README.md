# Seleção inicial de classes — 28/10/2026

## Entrega

A abertura de personagem mostra quatro cartões: Shinigami, Hollow/Arrancar,
Quincy e Fullbringer. Shinigami continua disponível e abre a personalização;
os demais exibem cadeado e “Em breve”, com clique desabilitado. O cartão
disponível cresce suavemente até 1,035 vezes pelo hover ou foco de teclado,
com retorno amortecido e espaço reservado entre cartões.

A arte aprovada pelo usuário em 28/10 foi preservada: Ichigo, Grimmjow,
Uryū e Chad são representantes visuais, sem conceder seus poderes ao escolher
a origem. O fundo anterior da Soul Society permanece sob uma camada escura.
As colunas do atlas são recortadas no render, preservando proporções e rostos.
Nomes, indicações e instruções têm traduções pt_br/en_us.

Escolher é uma prévia local. A confirmação de origem/aparência continua na
tela seguinte e o servidor continua aceitando somente Shinigami. Voltar não
cria personagem, dá arma ou registra outra origem. Esta entrega não modifica
o prólogo, o equipamento inicial ou a progressão racial.

## Estado local e arquivos

Base `main`, `c8d22297`; nenhum checkout, commit ou push. A retomada já tinha
alterações em ConfirmRaceScreen, a arte nova e cinco logs compactados na raiz.
Esses logs anteriores foram preservados.

- `src/main/java/com/bleachmod/client/gui/ConfirmRaceScreen.java`: cartões e hover.
- `src/main/resources/assets/bleachmod/textures/gui/character/class_portraits.png`: atlas.
- `src/main/resources/assets/bleachmod/lang/{pt_br,en_us}.json`: 13 chaves por idioma.
- `ClassSelectionQaBootstrap.java` e perfil `classSelectionQa` no build: laboratório.

## Validação

Minecraft 1.20.1, Forge 47.4.10, Temurin 17.0.20.1. Sem downloads de dependências;
Gradle executado com `--offline`, fora do sandbox para acessar locks do cache local.

- Antes das edições finais: `check build` passou, com 146 cenários de regressão.
- Cliente real final: execução concluída com exit 0, oito capturas de framebuffer.
- Quatro cartões; somente Shinigami ativo; três cliques bloqueados sem navegação.
- Clique Shinigami abre aparência; Voltar retorna; preview não cria personagem.
- Tab alcança a opção disponível; hover e foco produzem a ampliação.
- Inspeção visual: escalas GUI 1, 2 e 3, em janela de 1280 × 720.
- JSON dos dois idiomas parseado e cobertura das 13 chaves comparada.
- Rodada final: `check build runGameTestServer` passou (exit 0): 146 regressões
  e 223 GameTests obrigatórios aprovados. Log completo em `logs/validation.log`.

Os ponteiros de hover e cliques são injetados pelo laboratório na própria UI
Minecraft. As imagens vêm de `Screenshot.grab`, não de uma prévia web ou imagem
sintética. A verificação de foco usa o evento Tab da tela. As capturas finais
estão em `screenshots/`; dimensões e ticks em `frames.jsonl`.

O primeiro lançamento não iniciou a sequência porque o diretório do jogo
terminava em `/.`; o guard do laboratório foi corrigido com normalização de
caminho. Uma primeira rodada revelou tooltip sobre o cabeçalho; ele foi
removido e a rodada gráfica foi repetida. O laboratório limpa toasts somente
no seu mundo de QA para permitir inspeção da interface.

## Reprodução

```powershell
$env:JAVA_HOME = 'C:/Users/jguic/.jdks/temurin-17.0.20.1'
./gradlew.bat --offline -PclassSelectionQa=true runClient --console=plain
./gradlew.bat --offline check build runGameTestServer --console=plain
```

O perfil cria um mundo novo `Class-Selection-QA-*` dentro de
`run/class-selection-qa`, captura os estados e encerra. Só funciona em
desenvolvimento, com flag explícita e nesse diretório. Nenhum save existente
é aberto. Sem o perfil, a tela aparece pelo fluxo normal de criação.

## Arte

Gerada em 27/10 pela ferramenta integrada `image_gen`, como um atlas 1536 × 1024
com quatro colunas. O original foi copiado para o recurso do projeto sem edição
da imagem. SHA-256: `E43916D2DE7D7E57AE2348EA0D7614042FAA358A6C08001097ECD466FEA24E36`.
Prompt em [art-prompt.txt](art-prompt.txt).

## Limites

Aceite artístico final pelo usuário ainda aberto. A inspeção gráfica foi feita
em pt_br; en_us teve validação de chaves. Dois clientes, Photon e outras
resoluções não foram avaliados. O JAR gerado fica em `build/libs`; não foi
instalado automaticamente em launcher externo.

JAR final: `build/libs/bleachmod-0.2.0.jar`.
SHA-256: `9D80C476E234779FC10A93164274D7C172941CB772B39B2197CE2C991ECC184F`.
