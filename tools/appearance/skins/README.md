# Skins para recortar

Coloque aqui skins de Minecraft **64×64** em PNG, com o nome do personagem em minúsculas e `_` no lugar de espaços:

```
kenpachi.png
renji.png
toshiro_hitsugaya.png
```

Depois rode, na raiz do projeto:

```
python3 tools/appearance/split_skin.py --preview pecas.png
```

Cada skin vira até 4 peças no editor (traje, cabelo, olhos, boca) com o mesmo nome. Abra `pecas.png` para conferir o recorte.

## Origem e licença (opcional, mas necessário antes de publicar)

Ao lado do PNG, um JSON com o mesmo nome guarda de onde a skin veio e ajustes do recorte:

```json
{
  "source": { "author": "NomeDoAutor", "license": "CC-BY-4.0", "origin": "https://link-da-skin" },
  "skinTolerance": 30,
  "tintHair": true
}
```

- `skinTolerance`: diminua (ex.: 10–16) se roupa clara estiver sumindo por ser confundida com pele; aumente se sobrar pele no traje.
- `skinSample`: `[x, y]` de um pixel de pele na skin 64×64, quando a detecção automática errar.
- `tintHair`: `false` quando o "cabelo" é na verdade chapéu, máscara ou capacete e não deve mudar de cor.
- `mouthRow`: linha do rosto (0–7) onde começa a boca; padrão 6.
- `skip`: lista de peças a não gerar, ex. `["outfit"]`.

Sem o JSON, a skin entra como `"license": "unverified"`.
