# Mapas do History Mode

Mapas pré-construídos que o mod copia para dentro de cada mundo novo criado com o tipo **Modo História**
(`HistoryMapInstaller`). Nas execuções do Gradle (`runClient`, `runServer`, `Iniciar-Teste.bat`) a propriedade
`bleachmod.mapsDir` aponta para esta pasta; fora delas o mod procura `<pasta do jogo>/bleachmod/maps/`.

| Pasta | Dimensão | Origem | Spawn |
|---|---|---|---|
| `human_world/` | `minecraft:overworld` | Horizon City - Advanced World (CurseForge), 1.20.1 | 19.5, 65, -116.5 |
| `soul_society/` | `bleachmod:soul_society` | SoulSociety (Planet Minecraft), 1.12.2 → 1.20.1 | 282.5, 121, -33.5 |
| `hueco_mundo/` | `bleachmod:hueco_mundo` | LasNoches REFLEXION (Planet Minecraft), 1.11.2 → 1.20.1 | 2528.9, 41, 2540.9 |

Spawns e dimensões ficam em `maps.json`. Cada save guarda uma cópia dele
(`<save>/bleachmod/maps_installed.json`), então mudanças só valem para mundos criados depois.

## Atualizar ou adicionar um mapa

1. O mapa precisa ser Java e de versão **igual ou anterior à 1.20.1**; versões mais novas não abrem.
2. Abra uma cópia no cliente 1.20.1 e use **Editar → Otimizar mundo** antes de copiar.
3. Copie somente `region/`, `entities/` e `poi/` (nunca `level.dat`, `playerdata` ou `data/`).
   Não entre no mundo antes de copiar: o jogo gera terreno nas bordas.
4. Registre a pasta em `maps.json`; dimensões novas também precisam de `dimension_type` e de entrada em
   `data/bleachmod/worldgen/world_preset/history_mode.json`.

Os mapas são de terceiros: não redistribuir publicamente sem autorização dos autores.
