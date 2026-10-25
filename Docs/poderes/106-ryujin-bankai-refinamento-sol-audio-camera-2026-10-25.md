# 106 — Refinamento da Bankai de Ryūjin Jakka: Sol Estruturado, Restauração de Áudio e Nova Câmera

**Data:** 25/10/2026  
**Checkout:** `Bleach-Modpack`  
**Branch:** `codex/ryujin-bankai-sol-selamento` (base `1a30f87`)  
**Documentos de Referência:** [103-ryujin-bankai-sol-selamento-plano-prompt-2026-10-25.md](103-ryujin-bankai-sol-selamento-plano-prompt-2026-10-25.md) e [104-ryujin-bankai-sol-selamento-report.md](104-ryujin-bankai-sol-selamento-report.md)  

---

## 1. Contexto e Objetivos

Com base no feedback do playtest da Bankai de Ryūjin Jakka / Zanka no Tachi, foram implementados três refinamentos fundamentais na branch `codex/ryujin-bankai-sol-selamento`:
1. **Substituição e Restauração da Fala Nominal:** Adoção da fonte isolada `Zanka_no_Tachi_only_main_voice.wav` (2,24 s, mono PCM16/192 kHz), tratamento da interferência de áudio e readequação de ducking e legendas.
2. **Reconstrução do Sol Cenográfico:** Substituição de polígonos sólidos e leques triangulares por fogo estruturado volumétrico (núcleo quente de plasma, manto convectivo com profundidade 3D, coroa irregular com línguas móveis) e absorção por fitas/línguas curvas contínuas que se desprendem da periferia e convergem para a lâmina real.
3. **Nova Direção de Câmera:** Caminho físico refinado em 5 atos (aberto frontal com desvio sutil $\rightarrow$ aproximação moderada $\rightarrow$ close de detalhe da mão e lâmina carbonizada após selamento $\rightarrow$ abertura para rosto, tronco e espada no nome $\rightarrow$ retorno suave ao gameplay), com espelhamento completo para destros e canhotos.

---

## 2. Restauração e Processamento do Áudio

### 2.1. Análise Espectral da Fonte
- **Arquivo original preservado:** `C:/Users/joaop/Downloads/Zanka_no_Tachi_only_main_voice.wav` (430.080 amostras a 192 kHz, duração 2,2400 s).
- A inspeção algorítmica por STFT e autocorrelação revelou duas anomalias externas:
  1. *Artefato pré-voz (0,180s a 0,260s):* Tom espúrio com fundamental em 186 Hz e pico a -36,4 dBFS antes do ataque vocal de Yamamoto em 0,272 s.
  2. *Interferência externa (1,430s a 1,560s):* Presença de componente vocal/harmônica estranha no intervalo de silêncio entre o final de "-no" (~1,38 s) e o início do fonema plosivo "Ta-" (~1,58 s), apresentando fundamental em 221,5 Hz (forte correlação periódica de 0,766) com pico em -17,3 dBFS.

### 2.2. Tratamento e Camuflagem Acústica
- **Entrada limpa (0,000s a 0,268s):** Aplicação de curva S de fade-in eliminando completamente o artefato de 186 Hz sem afetar o ataque de "Zan".
- **Atenuação pontual (1,400s a 1,575s):** Redução suave em notch (-26 dB) da interferência de 221 Hz com janelas de cosseno.
- **Efeito de selamento térmico:** Introdução na janela 1,435s a 1,555s de um sutil chiado de calor e micro-estalo de brasa orgânico sintetizado (-33 dBFS de pico, faixa 2–6 kHz), mascarando perfeitamente qualquer resíduo sem prejudicar a inteligibilidade de "Tachi".
- **Decaimento e masterização:** Fade suave no sibilante final de "-chi" (2,15s a 2,23s) e pico normalizado a -1,0 dBFS.
- **Transcodificação:** Arquivo codificado para Ogg Vorbis mono 48 kHz em `src/main/resources/assets/bleachmod/sounds/ryujin_voice_name.ogg` (taxa ~88 kbps, 24 KB).
- **Sincronização:** Cue no tick 60, início da fala no tick ~65, término no tick 105. Subtítulo ajustado para a janela dos ticks 64 a 106.

---

## 3. Reconstrução do Sol de Chamas (`RyujinSunVisual.java`)

1. **Núcleo Incandescente (Core):** Esfera volumétrica central de plasma branco-amarelado (`255, 250, 220, alpha=235`) com pulsação térmica.
2. **Manto Convectivo Estruturado (Mid-Flames):** 10 colunas e labaredas volumétricas ascendentes com profundidade tridimensional real ao longo do eixo `forward` ($\pm 1,8$ blocos), eliminando a impressão de fatias poligonais 2D.
3. **Coroa Irregular e Línguas Móveis:** 16 línguas de fogo perimétricas com turbulência senoidal multiescala (`turb = 1.0 + 0.22*sin(4*theta) + 0.12*cos(7*theta)`), conferindo contorno vivo e dinâmico de fogueira monumental.
4. **Absorção por Fitas Curvas Convexa-Helicoidais:** 8 línguas de fogo desprendem-se da borda do sol (cujo raio encolhe continuamente de 9,0 para 0,0 blocos dos ticks 30 a 52) e viajam em arcos térmicos 3D que contornam o jogador e afunilam diretamente na lâmina real da katana (da empunhadura à ponta). O sol cessa no tick 52 sem explosão externa ou sumiço instantâneo.
5. **Lâmina Carbonizada e Fumaça Fina:** Acabamento em preto carvão fosco calcinado (`18, 14, 12`), veio incandescente de brasa viva no gume (`245, 85, 12`) e micro-partículas orçadas de fumaça cinza e cinzas ascendentes (`SMOKE`) durante os ticks 52..124.

---

## 4. Nova Direção de Câmera (`RyujinBankaiCamera.java`)

- **Ato 1 (Ticks 0..30):** Plano frontal 3/4 amplo com desvio lateral discreto (distância ~10,8 blocos, FOV 68°), mostrando corpo inteiro em silhueta contra o sol de 18 blocos.
- **Ato 2 (Ticks 30..52):** Dolly físico moderado de aproximação (distância de ~10,8 para ~6,4 blocos, FOV de 68° para 58°), mantendo 100% legível o trajeto das línguas de fogo descendo do sol para a lâmina.
- **Ato 3 (Ticks 52..68):** Close-up de detalhe na mão e na katana carbonizada (distância ~2,1 blocos, FOV 48°), enquadrando a lâmina apontada para fora e para baixo no lado da empunhadura sem cortar a ponta e sem atravessar o corpo do jogador.
- **Ato 4 (Ticks 68..115):** Recuo suave para plano médio 3/4 (distância ~4,5 blocos, FOV 56°) enquadrando semblante, tronco e espada durante a pronúncia do nome ("Zanka no Tachi"), com respiração senoidal sutil.
- **Ato 5 (Ticks 115..140):** Transição e retorno contínuo para a câmera de gameplay vanilla (distância 4,0 blocos atrás, FOV 70°).
- **Espelhamento Canhoto:** Todos os planos incorporam o parâmetro `sign` (1,0 para destros, -1,0 para canhotos), garantindo que o enquadramento de detalhe e os planos 3/4 acompanhem o lado da empunhadura.

---

## 5. Validação Automatizada

- **`compileJava --offline`:** Concluído com código 0 (39s).
- **`test --offline`:** Concluído com código 0 (25s).
- **`runGameTestServer --offline`:** Concluído com código 0 (2m 07s).
  - **`228 GAME TESTS COMPLETE: All 228 required tests passed :)`**
  - Asserções de câmera ampla, detalhe (< 3,5 blocos, FOV 48), plano médio (~4,8 blocos, FOV 56), espelhamento canhoto, retorno ao gameplay (FOV 70, atrás do jogador) e legendas (64..106) validadas numericamente.
- **`check build --offline`:** Concluído com código 0 (38s).
  - **`PASS: 147 regression scenarios`**, JAR gerado em `build/libs/bleachmod-0.2.0.jar`.
