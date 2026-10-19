# Plano: personalização de skin do personagem

> Proposta de 18/10/2026, **ainda não implementada**. Parte da análise técnica da montagem de skins do Dragon Mine Z (DMZ) e a confronta com o código atual do Bleach Mod. Nada aqui é funcionalidade disponível.

## 1. Resumo

A personalização é **viável** e cabe na arquitetura atual. O fluxo pedido funciona assim: confirmar a raça, abrir o editor de aparência, montar cabelo, rosto e marcas inspirados em personagens do anime e confirmar tudo de uma vez.

O sistema do DMZ **não deve ser copiado como está**. O núcleo dele resolve um problema que o Bleach não tem: corpos não humanos (Oozaru, Frost Demon, Bio-Android). Por isso ele troca o renderer do jogador por GeckoLib. No Bleach, todos os caminhos previstos para as primeiras entregas são humanoides (Shinigami, Quincy, Fullbringer, Humano). O código atual também depende do `PlayerModel` vanilla em vários pontos:

| Dependência atual | Arquivo | O que quebraria com a troca de renderer do DMZ |
|---|---|---|
| Animações por playerAnimator (`ModifierLayer`) | `client/cinematic/CinematicDirector.java`, `client/berserk/NozarashiStance.java` | Liberação, Berserk e golpes do Nozarashi animam o `PlayerModel`; o GeckoLib exigiria refazer tudo em `.animation.json` |
| Bainha da Zanpakutō | `client/model/ZanpakutoSheathLayer.java` | Layer tipado para `PlayerModel` |
| Armadura de gelo | `client/model/IceArmorLayer.java` | Idem |
| Pele carmesim do Berserk | `client/berserk/BerserkSkinLayer.java`, `BerserkSkins.java` | Idem |
| Armaduras e mods de terceiros | vanilla | Compatibilidade nativa com o modelo vanilla |

**Recomendação:** manter o `PlayerModel` vanilla e aproveitar do DMZ as ideias, não a implementação:

1. dados de aparência persistidos na capability e sincronizados pelo servidor;
2. composição em camadas tingidas por cor (pele, olhos, cabelo);
3. cabelo 3D independente da textura;
4. sobrescritas visuais por forma (Shikai/Bankai).

Os pontos frágeis listados na análise (seções 21.1–21.9) são corrigidos desde o início: validação no servidor, catálogo explícito em vez de contagem de arquivos, uma única resolução de aparência e textura composta uma vez em vez de vários passes por quadro.

O Hollow é a exceção futura, por causa de Resurrección e corpos não humanos. A decisão sobre geometria própria (GeckoLib ou modelo Java) fica para a fase do Hollow e só vale para essas formas (ver §11).

## 2. O que já existe e é reaproveitado

| Evidência | Uso no plano |
|---|---|
| `common/data/CharacterData.java`: `race`, formas, `saveAppearance()` | `saveAppearance()` já é o snapshot visual enviado a quem rastreia o jogador; a aparência entra nele |
| `common/network/SyncHelper.appearance()` / `appearanceTo()` + `AppearanceSyncS2C` | Canal de sincronização visual pronto (`TRACKING_ENTITY_AND_SELF`); não precisa de pacote S2C novo |
| `client/network/ClientPacketHandler.apply()` → `PlayerData.load()` | Aplica o NBT parcial no cliente, como no DMZ |
| `common/network/c2s/ConfirmCharacterC2S` + `client/gui/ConfirmRaceScreen` | Ponto de entrada da criação; o editor entra entre os dois |
| `client/events/ClientForgeEvents` abre `ConfirmRaceScreen` enquanto `!hasCreatedCharacter()` | Mantém o bloqueio até a confirmação final |
| `client/berserk/BerserkSkins`: `NativeImage` → `DynamicTexture`, cache e `clear()` ao sair do mundo | **Precedente direto** da composição de textura proposta |
| `common/RegistryReload` (preparar/validar/instalar) + `SyncQuestRegistryS2C` | Padrão para carregar o catálogo de aparência por datapack e enviá-lo ao cliente |
| `PlayerData` schema 8, `Reference.NETWORK_PROTOCOL = "2.18"` | Subir para schema 9 e protocolo 2.19 |
| `Docs/arte/prompts-arte-mvp.md` | Ponto de partida para os prompts de arte das peças |

## 3. DMZ × Bleach: o que levar e o que deixar

| Aspecto do DMZ | Decisão para o Bleach | Motivo |
|---|---|---|
| Renderer vanilla cancelado por mixin + GeckoLib | **Não levar** (exceto Hollow futuro) | Quebra playerAnimator, layers atuais e compatibilidade; personagens humanoides não precisam |
| `null.png` + 10–25 passes de layers por quadro | **Trocar por textura composta**: as camadas viram uma `DynamicTexture` quando a aparência muda | Custo por quadro igual ao vanilla; resolve o risco 21.8 |
| Cores RGB por camada (`bodyColor`, `hairColor`, `eye1/2Color`) | **Levar** | Base de toda a personalização por cor |
| Olhos em 4 camadas (fundo, íris 1, íris 2, detalhe) | **Levar**, simplificado para 3 | Anime depende muito dos olhos |
| `CustomHair` procedural editável | **Não levar no início**; usar penteados pré-modelados | Editor procedural custa caro e não reproduz penteados canônicos; pode vir depois |
| `hair_base.png` plano + cabelo 3D | **Levar** | O plano cobre a nuca e a raiz; o 3D dá volume |
| `TextureCounter` (procura índices 0..100) | **Não levar**; catálogo JSON explícito | Elimina os riscos 21.2 e 21.3 |
| Aceitar índices do cliente sem validar | **Corrigir**: validador único no servidor | Risco 21.1 (alta prioridade) |
| `StatsSyncC2S` a cada clique no editor | **Não levar**; prévia apenas local e envio único ao confirmar | Ninguém mais precisa ver a prévia; menos pacotes, menos estado inválido |
| `PREVIEW_MODE` estático global | **Trocar** por uma aparência de prévia passada ao compositor | Risco 21.7 |
| Sobrescritas por forma (`FormData`) | **Levar** para `forms` do Bleach | Ex.: cabelo longo do Tensa Zangetsu, pele carmesim do Berserk |
| Fade de camadas, tint de aura | **Fase posterior** | Não bloqueia o MVP |
| Aliases de modelo duplicados em três classes | **Evitar**: um `ResolvedAppearance` único | Risco 21.9 |

## 4. Escopo

### Primeira entrega

- Opção **"Usar minha skin do Minecraft"**: padrão para personagens já existentes e escolha válida para quem não quer personalizar.
- Corpo: modelo clássico ou slim (braços finos), tom de pele por cor livre e preset de traje por raça (shihakushō no Shinigami).
- Rosto: olhos (formato + 2 cores), sobrancelhas (formato, cor do cabelo), boca e expressão base.
- Cabelo: penteado 3D + camada plana, cor primária e secundária.
- Marcas: tatuagens, cicatrizes e maquiagem (Renji, Hisagi, Ikkaku, Kenpachi e outros).
- Acessórios de cabeça e rosto: kenseikan, sinos do Kenpachi, tapa-olho, penas do Yumichika e afins.
- Prévia 3D giratória no editor e confirmação única junto com a raça.

### Depois

- Sobrescritas por forma (Shikai/Bankai), expressões dinâmicas (piscar, combate) e fade entre formas.
- Recustomização no jogo (NPC barbeiro/Academia ou item) e desbloqueio de peças por quest.
- Haori de capitão, máscaras Hollow (Hollowficação) e roupas por afiliação.
- Geometria própria do Hollow (§11).

### Fora

- Editor procedural de cabelo como o `CustomHair`.
- Upload de PNG arbitrário pelo jogador; quem quiser isso usa a própria skin do Minecraft.

## 5. Modelo de dados

### 5.1 `AppearanceData` (novo, em `common/data/`)

Classe separada, porque `CharacterData` já concentra Zanpakutō, formas e técnicas. Fica pendurada em `PlayerData` como `character`, `resources` e os outros dados.

| Campo | Tipo | Padrão | Observação |
|---|---|---|---|
| `useVanillaSkin` | `boolean` | `true` | Se verdadeiro, o resto é ignorado na renderização |
| `bodyModel` | `String` (`default`/`slim`) | `default` | Escolhe o `PlayerRenderer` vanilla correspondente |
| `skinColor` | `int` RGB | catálogo | Tom de pele |
| `outfit` | `String` id | por raça | Ex.: `shihakusho` |
| `eyes` | `String` id | catálogo | Formato dos olhos |
| `eyeColor1`, `eyeColor2` | `int` RGB | catálogo | Íris e detalhe/brilho |
| `brows` | `String` id | catálogo | Tingido com `hairColor` |
| `mouth` | `String` id | catálogo | Expressão base |
| `hair` | `String` id | catálogo | Penteado (3D + plano) |
| `hairColor`, `hairColor2` | `int` RGB | catálogo | Primária e mechas/pontas |
| `marks` | lista de ids, máx. 4 | vazia | Tatuagens, cicatrizes e maquiagem, com cor opcional |
| `accessories` | mapa `slot → id` | vazio | Slots `head`, `face`, `hair`; um item por slot |
| `version` | `int` | 1 | Migração do próprio bloco |

Use ids textuais (`hair:spiky_orange_inspired`), não índices: adicionar ou remover peças não desloca as escolhas salvas, e o servidor valida por existência.

### 5.2 Persistência e sincronização

- NBT sob `PlayerData` → `appearance`; incluído em `CharacterData.saveAppearance()` ou ao lado dele em `SyncHelper.appearance()` e `appearanceTo()`, para que quem rastreia o jogador receba tudo.
- **Schema 9:** saves antigos sem `appearance` carregam com `useVanillaSkin = true`, então nada muda visualmente para quem já joga.
- **Protocolo 2.19:** pacote C2S novo e payload de aparência ampliado.
- O clone de morte e dimensão já copia `PlayerData` (`CapabilityEvents`); confirmar que `appearance` entra no `copyFrom` e criar um teste para isso.

### 5.3 Catálogo de peças (data-driven)

Em vez de descobrir peças pelos nomes de arquivo, um catálogo declara o que existe:

```
data/bleachmod/appearance/<categoria>/<id>.json      ← servidor (validação, restrição, desbloqueio)
assets/bleachmod/appearance/hair/<id>.geo.json       ← cliente (geometria 3D)
assets/bleachmod/textures/appearance/<categoria>/... ← cliente (camadas PNG)
```

Exemplo conceitual de uma entrada:

```json
{
  "id": "hair:captain_kenseikan_style",
  "category": "hair",
  "name": "appearance.bleachmod.hair.captain_kenseikan_style",
  "inspiredBy": "byakuya",
  "races": ["shinigami"],
  "geometry": "bleachmod:appearance/hair/long_straight",
  "layers": [
    { "texture": "bleachmod:textures/appearance/hair/long_straight_base.png", "tint": "hair" },
    { "texture": "bleachmod:textures/appearance/hair/long_straight_shade.png", "tint": "hair2" }
  ],
  "flatLayer": { "texture": "bleachmod:textures/appearance/hair/long_straight_flat.png", "tint": "hair" },
  "defaultColors": { "hair": "#1A1A22" },
  "unlock": null
}
```

- `tint` aceita `skin`, `hair`, `hair2`, `eye1`, `eye2`, `none` ou uma cor fixa. Assim, os sinos do Kenpachi mantêm a cor original e o cabelo herda a cor escolhida.
- `races` restringe a peça (ex.: traje Quincy só para Quincy).
- `unlock` fica reservado para a fase de desbloqueio por quest.
- Carregado pelo padrão de `RegistryReload` e enviado ao cliente junto com `SyncQuestRegistryS2C` ou em um pacote irmão. Um JSON inválido mantém o catálogo anterior, como já acontece com quests e formas.

## 6. Rede e validação

### 6.1 Criação (primeira vez)

```
ConfirmRaceScreen ──(escolhe Shinigami, sem pacote)──▶ CharacterAppearanceScreen
     prévia 100% local; nenhum pacote por clique
                       │ Confirmar
                       ▼
ConfirmCharacterC2S(race, appearanceNbt)       ← payload ampliado
                       │ servidor
                       ▼
AppearanceValidator.sanitize(race, nbt) → AppearanceData válido
initializeShinigami() + appearance + Asauchi
SyncHelper.full(player) + SyncHelper.appearance(player)
```

A raça e a aparência são confirmadas no **mesmo pacote**, de forma atômica. Se o cliente fechar o jogo no meio, nada fica meio criado e `hasCreatedCharacter` continua falso. O botão "Voltar" do editor retorna à escolha de raça sem tocar o servidor.

### 6.2 Recustomização (fase posterior)

`UpdateAppearanceC2S(appearanceNbt)` só é aceito se o servidor tiver concedido uma janela de edição (`appearanceEditUntil`, aberta pelo NPC, item ou comando de operador). Isso impede a troca livre de visual no meio do combate, se o design quiser essa restrição (§12, D-SK3).

### 6.3 `AppearanceValidator` (servidor)

É uma função única, usada pela criação, pela recustomização e pelo `load()` do NBT:

- cada id precisa existir no catálogo, pertencer à categoria certa e ser permitido para a raça; senão, cai no padrão da raça;
- cores: inteiros RGB `0x000000–0xFFFFFF`; não aceitar strings livres;
- `marks` com no máximo 4 itens, sem duplicatas; `accessories` com um item por slot conhecido;
- peças com `unlock` só são aceitas se o jogador as tiver desbloqueado;
- o tamanho total do NBT recebido é limitado, rejeitando pacotes inflados.

Assim, o risco 21.1 do DMZ fica resolvido antes de existir.

## 7. Renderização no cliente

### 7.1 Visão geral

```
AppearanceData + forma ativa + expressão
            │
            ▼
AppearanceResolver → ResolvedAppearance (ids → texturas, cores finais, geometria)   ← única fonte
            │
   ┌────────┴─────────────────────────┐
   ▼                                  ▼
SkinCompositor                    HairLayer / AccessoryLayer
NativeImage 64×64 (ou 128×128)     ModelPart presos à cabeça do PlayerModel
pele → traje → rosto → marcas      tingidos por vertex color
→ cabelo plano
   │
   ▼
DynamicTexture registrada (cache por hash)
   │
   ▼
Mixin: AbstractClientPlayer#getSkinTextureLocation / #getModelName
   → PlayerRenderer vanilla desenha corpo, braço em 1ª pessoa, capa etc.
```

### 7.2 `SkinCompositor`: textura composta, não passes múltiplos

- Recebe um `ResolvedAppearance` e combina as PNGs em um `NativeImage`: multiplica os tons de cinza pela cor da camada e usa alpha-over na ordem **pele → sombreamento → traje → olhos (fundo, íris, brilho) → sobrancelhas → boca → marcas → cabelo plano (camada hat/nuca)**.
- Registra o resultado como `DynamicTexture` em `bleachmod:skin/<hash>`. O hash cobre todos os campos resolvidos (aparência + sobrescrita de forma + expressão).
- Cache LRU com limite (ex.: 64 texturas), liberado em `clear()` ao sair do mundo e no reload de resources, como `BerserkSkins.clear()` já faz em `ClientForgeEvents`.
- A composição de 64×64 ou 128×128 custa microssegundos, então pode rodar na render thread na primeira requisição. Até a textura ficar pronta, o jogador aparece com a skin vanilla.
- **Resolução:** o `PlayerModel` usa UVs proporcionais, então uma textura de 128×128 funciona sem mudar o modelo e dá o dobro de detalhe no rosto, o que é importante para olhos de anime. Decisão em D-SK1.
- **Custo por quadro:** o mesmo de uma skin vanilla, porque só se troca a textura. É a principal vantagem sobre o DMZ.

### 7.3 Mixin de cliente (primeiro mixin do projeto)

São duas injeções pequenas em `AbstractClientPlayer`:

- `getSkinTextureLocation()` → retorna a textura composta quando `useVanillaSkin == false`;
- `getModelName()` → retorna `default`/`slim` conforme `bodyModel`. O `EntityRenderDispatcher` usa esse valor para escolher o `PlayerRenderer` com braços largos ou finos.

Isso cobre terceira pessoa, braço em primeira pessoa, lista de jogadores e qualquer código que leia a skin, inclusive o `BerserkSkins`, que passaria a avermelhar a skin composta sem mudanças.

Infraestrutura nova necessária: plugin `org.spongepowered.mixin` no `build.gradle`, `bleachmod.mixins.json`, entrada `MixinConfigs` no manifesto do jar e documentação em `Docs/arquitetura/10-build-dependencias.md`.

**Alternativa sem mixin**, caso a equipe prefira não introduzir mixins: um `RenderLayer` que redesenha o modelo inteiro com a textura composta, como `BerserkSkinLayer` faz, mais `RenderArmEvent` para a primeira pessoa. Funciona, mas desenha o corpo duas vezes, a camada hat da skin vanilla aparece por baixo das áreas transparentes e não troca entre slim e clássico. Recomendo o mixin.

### 7.4 Cabelo 3D e acessórios

- **Geometria:** um carregador mínimo de `.geo.json` (formato Bedrock exportado pelo Blockbench: bones, cubos, pivô, rotação e UV em caixa) converte cada penteado em `ModelPart` via `CubeListBuilder`. Os modelos são carregados no reload de resources. Não precisa de GeckoLib: só cubos estáticos.
- **`HairLayer`** (novo `RenderLayer<AbstractClientPlayer, PlayerModel<…>>`, registrado em `ModEntityRenderers.playerLayers` ao lado dos layers atuais): aplica `getParentModel().head.translateAndRotate(pose)` e desenha cada camada da peça com `renderToBuffer(..., r, g, b, 1)` tingido pela cor resolvida. O cabelo acompanha automaticamente as animações do playerAnimator, porque segue o `head`.
- **`AccessoryLayer`**: mesmo mecanismo para kenseikan, sinos, penas e tapa-olho. Uma peça pode prender-se a `head`, `body` ou braços.
- **Regras de ocultação**: não desenhar com capacete equipado (com lista configurável de capacetes que mantêm o cabelo), invisibilidade, espectador ou corpo próprio em primeira pessoa. Esconder `model.hat` quando o penteado declarar `hidesHatLayer`, para evitar z-fighting.
- Física de cabelo e LOD como no DMZ ficam fora; os penteados são estáticos.

### 7.5 Expressões

- **Primeira entrega**: a boca e as sobrancelhas escolhidas formam a expressão base fixa.
- **Depois**: cada peça de olho ou boca pode declarar variantes (`neutral`, `blink`, `combat`). O compositor gera até 3 texturas por jogador, todas no cache. Um `ExpressionTracker` no cliente alterna entre elas: piscar a cada 3–6 s e `combat` quando `StatusData` indica combate ou forma liberada. Trocar a textura não custa nada por quadro.
- O Berserk passa a ser uma **sobrescrita de forma** (pele carmesim + olhos emissivos) dentro do resolvedor, em vez de um caso especial isolado.

## 8. Editor de aparência (`CharacterAppearanceScreen`)

Aberto por `ConfirmRaceScreen` ao escolher a raça, com o mesmo bloqueio (sem ESC e sem pausa).

- **Esquerda**: abas **Corpo** (modelo, pele, traje), **Rosto** (olhos, cores, sobrancelhas, boca), **Cabelo** (penteado, cores), **Marcas** e **Acessórios**. Cada aba mostra uma grade de miniaturas com o nome da peça e a etiqueta "inspirado em: Renji".
- **Centro**: prévia do jogador local com `InventoryScreen.renderEntityInInventoryFollowsMouse`, giro por arrasto e zoom no rosto. A prévia passa um `AppearanceData` temporário ao resolvedor (`AppearanceResolver.preview(...)`) e não altera a capability, evitando o problema do `PREVIEW_MODE` global.
- **Cores**: paleta de presets canônicos (laranja do Ichigo, vermelho do Renji, branco do Hitsugaya) + seletor HSV + campo hex.
- **Ações**: "Aleatório", "Usar minha skin do Minecraft", "Voltar" (para a raça) e "Confirmar", que envia `ConfirmCharacterC2S(race, appearance)`.
- Miniaturas da grade: renderizar a peça sobre uma cabeça neutra com a cor atual, ou usar ícones PNG feitos pela arte. Começar com ícones.
- Textos em `lang/pt_br.json` e `en_us.json`.

## 9. Sobrescritas por forma (fase posterior)

Campo opcional `appearance` em cada forma do registro de formas (`FormRegistry` / JSON de formas), com precedência **base < forma ativa**:

| Campo | Exemplo canônico |
|---|---|
| `hairOverride` | Tensa Zangetsu: cabelo longo; Bankai do Kenpachi (Berserk) |
| `hairColorOverride` | — |
| `skinTint` + `intensity` | Berserk carmesim (substitui `BerserkSkins`) |
| `eyesOverride` / olhos emissivos | Olhos amarelos do Berserk; olhos Hollow |
| `extraLayers` | Marcas de reiatsu, máscara Hollow parcial |
| `hideAccessories` | Remover o kenseikan durante uma transformação, se o design quiser |

Fade entre a base e a forma (DMZ `BodyLayerFadeTracker`) fica para quando houver mais de uma forma com visual próprio. A composição gera as duas texturas, e o fade pode ser um segundo passe translúcido curto apenas durante a transição.

## 10. Arte necessária

### 10.1 Pipeline

1. Template UV 64×64 ou 128×128 (decisão D-SK1) com as áreas do rosto marcadas.
2. Cada camada tingível é desenhada em **tons de cinza**; a cor vem do jogo. Partes com cor fixa (sinos, kenseikan) usam `tint: none`.
3. Cabelo e acessórios modelados no Blockbench (*Bedrock Model*), com UV em caixa e textura própria pequena (32×32 ou 64×64).
4. Ícone de 32×32 por peça para o editor.

### 10.2 Lista inicial sugerida (MVP de conteúdo)

| Categoria | Peças | Inspirações |
|---|---|---|
| Penteados (3D) | 10 | Ichigo, Rukia, Renji (rabo alto), Byakuya, Hitsugaya, Kenpachi (espetos), Orihime, Uryū, Ikkaku (careca), Yumichika |
| Olhos | 8 | Neutro, determinado (Ichigo), grande (Orihime/Rukia), fino (Byakuya/Gin), feroz (Kenpachi), turquesa (Hitsugaya)… |
| Sobrancelhas | 6 | Incluindo a tatuagem das sobrancelhas do Renji como marca separada |
| Bocas/expressões | 6 | Neutra, séria, sorriso, sorriso largo (Kenpachi), raiva, sorriso do Gin |
| Marcas | 6 | Tatuagens do Renji, "69" + cicatrizes do Hisagi, maquiagem vermelha do Ikkaku, cicatriz do Kenpachi, pintura do Mayuri, marca do Yumichika |
| Acessórios | 5 | Kenseikan, sinos, tapa-olho, penas do Yumichika, faixa de tenente |
| Trajes | 1–2 | Shihakushō (Shinigami); Quincy e Fullbringer entram com as raças |

Total de cerca de 45 peças. Pela política do projeto, os nomes devem indicar "inspirado em" e o conteúdo é fan-made, sem afiliação com os detentores da obra (ver `Docs/arquitetura/12-licenciamento-e-creditos.md`). **Não copiar** texturas nem modelos do DMZ (GPL ok, mas a política do projeto é não trazer assets temáticos) nem de outros mods.

## 11. Hollow e corpos não humanos

A Hollowficação parcial (máscara) cabe neste sistema como acessório ou camada de forma. Um Hollow jogável com corpo próprio (Menos, Resurrección com cauda, asas, quatro patas) não cabe no `PlayerModel`. Quando essa raça for desenhada:

- **Opção A (recomendada para avaliar):** modelo Java próprio por forma, trocado em `RenderPlayerEvent.Pre`, que cancela o vanilla só para aquela forma. As animações do playerAnimator não se aplicam nela.
- **Opção B:** GeckoLib apenas para formas Hollow, como o DMZ faz. Exige dependência nova (MIT) e animações próprias.

Nos dois casos, o resolvedor de aparência continua a única fonte de cores e camadas. A decisão fica para o módulo Hollow e não bloqueia este plano.

## 12. Decisões pendentes (equipe)

| Id | Pergunta | Recomendação |
|---|---|---|
| D-SK1 | Resolução da skin composta: 64×64 ou 128×128? | 128×128: o rosto ganha detalhe sem mudar o modelo |
| D-SK2 | Introduzir mixins no projeto? | Sim, 2 injeções em `AbstractClientPlayer` (§7.3) |
| D-SK3 | Recustomização livre ou só em NPC/item? | Só por NPC barbeiro ou item, com janela autorizada pelo servidor |
| D-SK4 | Peças de personagens livres desde o início ou desbloqueadas por quest/afiliação? | Básicas livres; marcas e acessórios icônicos (kenseikan, sinos) por progressão |
| D-SK5 | O traje é fixo pela raça ou escolhível? | Fixo no MVP; cargo e afiliação mudam depois (haori de capitão) |
| D-SK6 | Manter a opção "Usar minha skin do Minecraft"? | Sim, e como padrão para saves existentes |

## 13. Fases de implementação

| Fase | Entrega | Principais arquivos | Esforço estimado |
|---|---|---|---|
| **F0** | Fechar D-SK1–D-SK6; template UV e primeiras 5 peças de teste | este documento, `Docs/arte/` | 1–2 dias |
| **F1** | Dados, rede e validação sem visual: `AppearanceData`, schema 9, protocolo 2.19, catálogo por datapack + sync, `AppearanceValidator`, `ConfirmCharacterC2S` ampliado | `common/data/`, `common/appearance/` (novo), `common/network/`, `RegistryReload` | 3–4 dias |
| **F2** | Renderização da textura: `AppearanceResolver`, `SkinCompositor`, mixin `getSkinTextureLocation`/`getModelName`, cache e limpeza | `client/appearance/` (novo), `mixin/client/`, `build.gradle`, `ClientForgeEvents` | 3–5 dias |
| **F3** | Editor `CharacterAppearanceScreen` integrado após `ConfirmRaceScreen`, prévia, paleta, lang | `client/gui/` | 4–6 dias |
| **F4** | Cabelo 3D e acessórios: carregador `.geo.json`, `HairLayer`, `AccessoryLayer`, regras de ocultação | `client/appearance/`, `ModEntityRenderers` | 3–5 dias |
| **F5** | Conteúdo: ~45 peças do §10.2 | `assets/`, `data/` | depende da arte |
| **F6** | Formas e expressões: `appearance` nas formas, migrar Berserk para o resolvedor, piscar/combate | `FormData`, `client/berserk/`, resolvedor | 3–4 dias |
| **F7** | Recustomização (NPC/item/comando) e desbloqueios por quest | `common/network/c2s/UpdateAppearanceC2S`, quests | 2–3 dias |

Ordem obrigatória: F1 → F2 → F3. F4 pode correr em paralelo com F3. F5 acompanha F2 e F4 conforme a arte chega. As estimativas são de uma pessoa familiarizada com o código e não incluem a produção de arte.

Pela regra de conclusão do `Docs/README.md`, cada fase atualiza no mesmo trabalho `02-capability-system`, `06-rede-sincronizacao`, `07-persistencia-nbt`, `08-ui-hud`, `10-build-dependencias` e os manuais do jogador.

## 14. Testes

### Automatizados (`src/test` e GameTests)

- `AppearanceValidator`: id inexistente, categoria errada, raça não permitida, cor fora da faixa, `marks` acima do limite, NBT gigante e peça bloqueada → todos caem no padrão.
- Migração do schema 8 para o 9: save antigo carrega com `useVanillaSkin = true`.
- Ida e volta de NBT (`save` → `load`) e cópia em morte e troca de dimensão.
- `ConfirmCharacterC2S` com aparência inválida cria o personagem com o padrão; um segundo envio após a criação é ignorado.
- Catálogo: todo `geometry` e toda `texture` referenciados existem (teste de integridade de assets); JSON inválido mantém o catálogo anterior.
- Hash do `ResolvedAppearance` estável para entradas iguais e diferente quando qualquer campo muda.

### Manuais (dois clientes)

1. Criar personagem e conferir que o segundo cliente vê a mesma aparência ao entrar no alcance e ao relogar.
2. Alternar slim e clássico; conferir o braço em primeira pessoa.
3. Animações de liberação e Berserk com cabelo e acessórios acompanhando a cabeça.
4. Capacete equipado, invisibilidade, espectador e F5.
5. Bainha da Zanpakutō e armadura de gelo sobre a skin composta.
6. Reload de resources (F3+T) com a textura recomposta; sair e voltar ao mundo sem vazar texturas.
7. Opção "Usar minha skin do Minecraft" preservando a skin original.

## 15. Riscos

| Risco | Mitigação |
|---|---|
| Mods que substituem o renderer do jogador (Epic Fight, Figura e similares) ignoram a skin composta ou o cabelo | Como mexemos só na textura e em layers, a compatibilidade é melhor que a do DMZ; documentar os conflitos conhecidos |
| Primeiro mixin do projeto (build, refmap, mappings oficiais) | Configuração isolada em F2; alternativa por layer descrita em §7.3 |
| Volume de arte maior que o de código | Começar com 5 peças de teste em F0; catálogo explícito permite entrega incremental |
| Penteados 3D atravessando capuz, capacete ou animações extremas | Regras de ocultação + revisão das animações existentes em F4 |
| Vazamento de `DynamicTexture` | LRU com limite + `release()` em `clear()`, como `BerserkSkins` |
| Uso de personagens reais da obra | Nomes "inspirado em", sem logos; política do doc 12 |
