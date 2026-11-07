# 127 — Plano Executivo: Hakuteiken (C Senbonzakura) em Blockbench, GeckoLib e PlayerAnimator

**Data**: 03/11/2026  
**Frente**: Antigravity Design Lab & Modelagem  
**Branch Exclusiva**: `codex/senbon-hakuteiken-blockbench` (originada da base atualizada de conferência/orquestração)  
**Status**: Planejamento executivo e setup de bancada concluídos; Blockbench MCP conectado (`5.2.2`).

---

## 1. Decisões Humanas Vigentes

Em alinhamento direto com o usuário em 03/11/2026:
1. **Mecânica Preservada Integralmente**:
   - Windup de 16 ticks (preparação com mira dinâmica).
   - Trava de vetor no tick 16.
   - Dash linear de até 16 blocos (1.6 blocos/tick).
   - Dano base, custos de Reiatsu/Stamina e regras de colisão e amortecimento em paredes permanecem intactos em `SenbonzakuraTechniqueService`.
2. **Identidades Visuais Distintas no Blockbench**:
   - **Senbonzakura (Shukei: Hakuteiken)**: Asas de plumagem nobre branca (estilo Byakuya), halo de energia radiante nas costas, lâmina de luz branca pura e transições de penas em sakura.
   - **Quincy (Vollständig)**: Asas de cristal de Reishi azulado/luminoso, 4 estrelas satélites orbitais independentes, silhueta angulada e agressiva.
3. **Renderer Integrado em Branch Própria**:
   - O novo renderer acoplando o modelo GeckoLib e poses do PlayerAnimator será implementado e validado **estritamente nesta branch** (`codex/senbon-hakuteiken-blockbench`), sem impactar checkouts ou branches de terceiros.
4. **Ponte MCP Blockbench Operacional**:
   - Blockbench 5.2.2 conectado com plugins `BlockbenchMCP` e `GeckoLib Models & Animations`. Formatos `geckolib_model` e `bedrock` disponíveis.

---

## 2. Diagnóstico da Geometria Atual vs. Meta de Refatoração

A auditoria (`audit_complexity`) do protótipo preliminar (`ag_senbon_hakuteiken.bbmodel`) revelou:
- Apenas 28 cubos em um grid rígidamente alinhado aos eixos (profundidade de osso = 1, sem rotações nos cubos).
- Asas compostas por um único grupo rígido (`celestial_wing_left` e `celestial_wing_right`), impossibilitando o arqueamento aerodinâmico durante o dash ou o bater de asas natural.
- Renderização em jogo atualmente delegada a malhas procedurais Java em `SenbonConstructGeometry.WINGS`.

### Nova Estrutura Anatômica do Modelo (`ag_senbon_hakuteiken_v2`)
Formato: `geckolib_model` (GeckoLib Animated Model).

```
root (pivot: [0, 0, 0])
├── halo_root (pivot: [0, 24, 4])
│   └── celestial_halo (disco luminoso de Reishi / 16 a 24 prismas radiais com transparência emissiva)
├── wing_left_root (pivot: [-2, 22, 3] - raiz escapular)
│   ├── wing_left_mid (pivot: [-8, 26, 4] - articulação do cotovelo da asa)
│   │   ├── wing_left_tip (pivot: [-18, 32, 5] - penas primárias de ponta)
│   │   └── wing_left_feathers_lower (penas secundárias pendentes)
├── wing_right_root (pivot: [2, 22, 3] - raiz escapular)
│   ├── wing_right_mid (pivot: [8, 26, 4] - articulação do cotovelo da asa)
│   │   ├── wing_right_tip (pivot: [18, 32, 5] - penas primárias de ponta)
│   │   └── wing_right_feathers_lower (penas secundárias pendentes)
└── hakuteiken_blade (pivot: [0, 10, -5] - projeção da lâmina sagrada condensada)
```

---

## 3. Roteiro de Clipes e Animações GeckoLib

1. `animation.hakuteiken.windup` (Ticks 0–16 / Duração: ~0.8s):
   - Asas abrem-se amplamente em arco elevado, com ligeira oscilação ondulatória nas penas primárias.
   - O halo acelera sua rotação axial suave.
   - Posição imponente de concentração de poder.
2. `animation.hakuteiken.dash` (Ticks 16–25 / Duração: ~0.5s / Loop: Hold):
   - Transição brusca e aerodinâmica: as raízes giram para trás (`pitch` negativo, asas recolhidas rente ao dorso).
   - Pontas das asas arqueadas para trás formando silhueta de seta/dardo.
   - Halo inclina-se para frente como propulsor de energia.
3. `animation.hakuteiken.impact_dissipate` (Duração: ~0.6s):
   - Expansão rápida e dissolução das asas (fade e abertura), cedendo lugar às partículas radiais de pétalas já homologadas.

---

## 4. Integração com PlayerAnimator (Pose do Jogador)

- Arquivo alvo: `src/main/resources/assets/bleachmod/player_animation/hakuteiken_dash.json` (formato Bedrock animation do PlayerAnimator).
- Postura nobre de esgrima:
  - `right_arm`: Estendido à frente empunhando a espada em estocada firme horizontal.
  - `left_arm`: Recolhido discretamente para trás para equilíbrio dinâmico da silhueta.
  - `body` / `head`: Inclinados ligeiramente para a frente no vetor da investida, mantendo alinhamento estrito com a mira do jogador.
  - Pernas em pose de propulsão aerodinâmica.

---

## 5. Plano de Entrega e Verificação Técnica

1. **Modelagem no Blockbench**:
   - Construir o rig articulado e texturizar a plumagem branca / halo emissivo com UV mapeado limpo.
   - Salvar em `tools/models/lab/antigravity/senbonzakura01/ag_senbon_hakuteiken_v2.bbmodel`.
   - Exportar `geo.json` e `animation.json`.
2. **Validação de Código e Compilação**:
   - Rodar `./gradlew compileJava --console=plain` e checar 0 warnings/erros.
   - Validar suite de GameTests existentes de Hakuteiken (`./gradlew runGameTestServer`) para garantir não-regressão mecânica.
3. **Bancada e QA Visual In-Game**:
   - Testar no cliente Forge local da branch com `-PbenihimeQa=true -PsenbonGardenQa=true` e sequência visual em frente, perfil e costas.
   - Capturar evidências sem saturação cega.
