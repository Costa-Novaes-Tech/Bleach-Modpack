# 96 — Sincronização, Atualização e Correção da Cinemática Hyōrinmaru

- **Data**: 22/10/2026
- **Workspace**: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design`
- **Branch**: `codex/design-poderes-criaturas`
- **Commit Local**: `c4d6e82270d6af018cdee6ac9fc8dc1d8f8c8d0e`
- **Suíte de Testes**: **141 cenários de regressão aprovados (Exit 0)** via `./tools/antigravity/Invoke-Design.ps1 -Mode Check`.

---

## 1. Motivação e Diagnóstico do Travamento

Ao testar a ativação da Bankai Hyōrinmaru no checkout `Bleach-Modpack-design`, o cliente apresentava comportamento obsoleto ("desatualizada e muito antiga e bugada") e a cinemática travava/congelava ("simplismente trava").

### Causa Raiz
A branch `codex/design-poderes-criaturas` estava estagnada no commit `830ad64`, que divergiu antes dos ciclos 86 a 93 desenvolvidos na branch `Feature-Poderes-bankais`. As inconsistências que causavam o travamento eram:
1. **Duração da Cinemática em Conflito**: O código antigo utilizava `DURATION = 70` (3.5s), enquanto as novas rotinas de rede e entidade já esperavam o ciclo completo de 400 ticks (20s). Isso causava encerramento abrupto ou loop infinito da câmera.
2. **Controlador GeckoLib Inválido**: `IceDragonEntity` tentava disparar `animation.ice_dragon.flight` (do Phantom vanilla), sem as novas animações de manifestação (`animation.ice_hydra.emerge` e `animation.ice_hydra.flight`), gerando exceções internas no GeckoLib e travamento da renderização.
3. **Ausência de Assets de Áudio**: O tema musical `hyorinmaru_theme.ogg` e as chamadas atualizadas de voz não estavam presentes na pasta de assets da branch, causando falha no carregador de som do Minecraft.

---

## 2. Mapa Completo das Alterações

### 2.1 Cinemática, Câmera e Efeitos Visuais (Cliente)
**Pasta**: `src/main/java/com/bleachmod/client/cinematic/` e `src/main/java/com/bleachmod/client/model/`

| Arquivo | Status | O que foi alterado / Função |
| :--- | :--- | :--- |
| `HyorinmaruCinematicClient.java` | Modificado | Expandida duração para 400 ticks (20s); criada interpolação contínua em 5 fases de câmera (close frontal, puxada orbital, revelação da hidra, tomada do topo e retorno suave respeitando a perspectiva F5 original do jogador); adicionado letterbox suave com tipografia Bankai. |
| `HyorinmaruEntranceAudio.java` | **Novo** | Gerencia a reprodução coordenada da trilha (`hyorinmaru_theme.ogg`), vozes japonesas (Bankai no tick 24, Nome no tick 330) e rugido da hidra (tick 216), com áudio imersivo sem atenuação para o invocador. |
| `HyorinmaruFrozenGround.java` | **Novo** | Renderizador de gelo temporário que se propaga radialmente no solo durante a emergência da hidra. |
| `HydraImpactVisuals.java` | **Novo** | Efeitos de impacto, fissuras e partículas nas colisões dos golpes da hidra. |
| `HyorinmaruBreathVisual.java` | **Novo** | Renderizador volumétrico da baforada glacial emitido pelas cabeças da criatura. |
| `IceDragonRenderer.java` | Modificado | Renderizador polimórfico GeckoLib 4.0 adaptado para alternar entre o dragão clássico e a nova Hidra de 3 Cabeças. |
| `IceArmorLayer.java` | Modificado | Camada de armadura de gelo adaptada para renderização no corpo do jogador durante a transformação. |

---

### 2.2 Lógica de Servidor, Ciclo de Vida e Entidade
**Pasta**: `src/main/java/com/bleachmod/common/technique/`, `common/visual/` e `entity/`

| Arquivo | Status | O que foi alterado / Função |
| :--- | :--- | :--- |
| `HyorinmaruCinematicService.java` | Modificado | Sincronizado ciclo autoritativo do servidor para 400 ticks (20s); remoção de disparo de som duplicado no tick 216 (agora gerenciado pelo cue de cliente); descarte seguro da entidade hidra ao concluir ou interromper. |
| `HyorinmaruTimeline.java` | **Novo** | Tabela central de constantes temporais (`SCALE = 2`, `DURATION = 400`, `COMMIT = 240`, marcos sonoros `BANKAI_VOICE = 24`, `NAME_VOICE = 330`, `HYDRA_ROAR = 216`). |
| `IceDragonEntity.java` | Modificado | Configurado controlador com `RawAnimation.begin().thenPlay("animation.ice_hydra.emerge").thenLoop("animation.ice_hydra.flight")`; corrigida imunidade solar do Phantom vanilla (`isSunBurnTick() = false`) e dano em área da hidra. |
| `ReleaseAnimationService.java` | Modificado | Integração da trava de animação (`duration = 400`, `commit = 240`) para Hyōrinmaru, sincronizando status de liberação com o servidor. |
| `HydraMotion.java` | **Novo** | Cinemática e trajetórias matemáticas dos pescoços e ataques de baforada da hidra. |
| `GlacialFragilityService.java` | **Novo** | Sistema de fragilidade e congelamento associado aos ataques gélidos da criatura. |

---

### 2.3 Assets de Áudio e Modelos de Produção
**Pasta**: `src/main/resources/assets/bleachmod/`

| Arquivo | Status | O que foi alterado / Função |
| :--- | :--- | :--- |
| `sounds/hyorinmaru_theme.ogg` | **Novo** | Trilha sonora orquestrada de ativação da Daiguren Hyōrinmaru. |
| `sounds/hyorinmaru_roar.ogg` | Modificado | Rugido da hidra remasterizado com ganho de +10 dB para audibilidade na câmera panorâmica aberta. |
| `sounds/hyorinmaru_voice_bankai.ogg` | Modificado | Áudio da fala japonesa "Bankai" otimizado e limpo. |
| `sounds/hyorinmaru_voice_name.ogg` | Modificado | Áudio da fala "Daiguren Hyōrinmaru" otimizado e balanceado. |
| `sounds.json` | Modificado | Registro dos novos eventos de som `theme.hyorinmaru`, `voice.hyorinmaru_bankai`, `voice.hyorinmaru_name` e `hyorinmaru.roar`. |
| `geo/ice_hydra.geo.json` | Modificado | Modelo GeckoLib da Hidra Glacial de Três Cabeças (pescoços articulados, asas e mandíbulas). |
| `animations/ice_hydra.animation.json` | Modificado | Clipes de animação de voo, ataque frontal e emergência da hidra. |
| `tools/models/source/ice_hydra.bbmodel` | Modificado | Arquivo fonte Blockbench completo da hidra de produção. |

---

### 2.4 Laboratório de Modelagem Blockbench (Frente Antigravity)
**Pasta**: `tools/models/lab/antigravity/` e `Docs/poderes/qa/antigravity/`

| Arquivo | Status | O que foi alterado / Função |
| :--- | :--- | :--- |
| `piloto01/ag_hydra_head_v2.bbmodel` | **Novo** | Modelo autoral da cabeça central e mandíbula da hidra com 62 cubos (chifres curvados de gelo em leque, boca com palato oco, garganta escura, dentes predadores em tesoura e nova animação de rugido de 2.4s). |
| `ag_hydra_jaw.bbmodel` | Preservado | Cópia inicial de 29 cubos preservada para histórico. |
| `piloto01/relatorio.md` | **Novo** | Relatório completo do piloto de modelagem com auditoria técnica, medições e confronto de avisos. |
| `piloto01/*.png` (30 arquivos) | **Novo** | Capturas comparativas estáticas (antes vs depois) e 14 poses dinâmicas renderizadas pelo Blockbench. |

---

## 3. Validação e Status Git

- **Branch**: `codex/design-poderes-criaturas`
- **Commit**: `c4d6e82`
- **Validação Gradle**:
  - Comando executado: `./tools/antigravity/Invoke-Design.ps1 -Mode Check`
  - Resultado: `BUILD SUCCESSFUL in 1m 19s` (`Exit 0`).
  - Regressões: **141 cenários aprovados** (anteriormente 137).
  - Log: [Check-20261022-182229.log](file:///c:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-design/Docs/poderes/qa/antigravity/Check-20261022-182229.log).
