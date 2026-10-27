# 124 — Hyōrinmaru: Animação Orgânica, Poses de Mandíbula e Alinhamento da Baforada

**Data:** 26/10/2026  
**Autor:** Antigravity  
**Branch:** `codex/ryujin-bankai-sol-selamento` (checkout `Bleach-Modpack-benihime`)  
**Contexto:** Prompt 125 — Refino Hyōrinmaru e Higashi em fases (Fase 2)

---

## 1. Visão Geral da Fase 2

A Fase 2 consolida a autoridade procedural única do runtime Java sobre a hidra glacial e resolve o desalinhamento geométrico da baforada de gelo, conectando fisicamente a emissão visual de partículas à boca de cada uma das três cabeças.

---

## 2. Poses de Mandíbula e Movimento Procedural

O comportamento da mandíbula é coordenado em `HydraMotion.java` e renderizado em tempo real por `IceDragonRenderer.java`, cobrindo três modos com transições suaves:

1. **Repouso e Locomoção (`localAge <= 44`):**
   - Ângulo de mandíbula em 0.0°.
   - Com o diastema refinado na Fase 1 (`Jaw 5`), os dentes superiores e inferiores encaixam em mordida tipo tesoura sem colisão de malha ou invasão das bochechas externas.
2. **Baforada de Congelamento (`localAge` 48..110):**
   - Transição preparatória suave a partir do tick 44.
   - Abertura anatômica ampla de $34.0^\circ$ (cabeça central) e $31.5^\circ$ (cabeças laterais) no intervalo do tick 64 ao 90.
   - Tremor sutil de esforço harmônico ($\approx \pm 0.75^\circ$) durante o disparo.
   - Fechamento progressivo a partir do tick 90 até retorno completo a 0° no tick 110.
3. **Rugido Celestial (`MODE_ROAR`):**
   - As cabeças apontam diretamente para o zênite ($+84^\circ$ de pitch em `headRotX`).
   - Abertura dramática máxima de $42.0^\circ$ (central) e $38.0^\circ$ (laterais), acompanhada de tremor sonoro ($\pm 1.5^\circ$) e fechamento pós-grito (tick 105..120).

---

## 3. Alinhamento Físico da Baforada (`HyorinmaruBreathVisual`)

### 3.1 Causa do Problema Anterior
Anteriormente, o ponto de origem de cada fita de baforada era calculado apenas na ponta do pescoço (`neckTip = anchor.subtract(forward.scale(8)).add(side.scale(local.x)).add(0, local.y + .4, 0).add(forward.scale(local.z))`).
Como o crânio e focinho projetam-se 2.6 blocos à frente do pescoço e inclinam-se ativamente para baixo e para os lados conforme `headRotX` e o yaw de cada cabeça, as partículas pareciam originar-se de trás do crânio/orelhas da criatura em vez de saírem do interior da boca aberta.

### 3.2 Correção Implementada
Em `HyorinmaruBreathVisual.java`, foi introduzida a projeção tridimensional do vetor diretor da cabeça:
```java
float rotX = HydraMotion.headRotX(false, head, age - 44);
float yawDeg = head == 1 ? -16.0F : head == 2 ? 16.0F : 0.0F;
float yawRad = (float) Math.toRadians(yawDeg);

double cosPitch = Math.cos(rotX), sinPitch = Math.sin(rotX);
double cosYaw = Math.cos(yawRad), sinYaw = Math.sin(yawRad);

Vec3 headDir = forward.scale(cosYaw * cosPitch)
                      .add(side.scale(sinYaw * cosPitch))
                      .add(0, sinPitch, 0);

Vec3 mouth = neckTip.add(headDir.scale(2.6)).add(0, -0.25, 0);
```
- A projeção acompanha fielmente o mergulho da cabeça (`rotX < 0`) e a rotação lateral de cada cabeça ($\pm 16^\circ$).
- O recuo vertical de $-0.25$ blocos posiciona o foco exatamente na fenda aberta entre o maxilar e a mandíbula inferior abaixada.
- As três fitas partem das três bocas abertas, convergem no ponto de confluência focal `merge` diante do Shinigami e explodem na torrente cônica de 36 blocos de alcance.

---

## 4. Validação e Resultados
- **Compilação e Testes Unitários/Regressão:** `./gradlew check --offline` executado com sucesso.
- **GameTests:** Suites de banco de dados, assets da hidra e release timeline preservadas intactas.
