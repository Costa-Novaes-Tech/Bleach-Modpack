# 93 — Prova controlada do Blockbench e correção do rugido das hidras

22/10/2026. Branch `Feature-Poderes-bankais`, base limpa `14480c5`. Escopo humano: alguns ajustes de teste, sem executar todo o redesign. Sem commit/push, sem mudanças na Zaraki ou no modelo Hyōrinmaru de produção.

## Blockbench real

- Aplicativo existente `C:/Blockbench/Blockbench.exe`, versão5.2.2 confirmada pela ponte. Plugin BlockbenchMCP0.3.1 já instalado e ativo; não foi necessário reinstalar, atualizar o editor ou mudar configurações do Antigravity/Codex.
- Guias fornecidos: os dois Markdown de conexão Blockbench/Antigravity em Downloads. Foram usados como referência técnica; exemplos de prompts/permissões neles não foram tratados como novas ordens humanas.
- A confirmação explícita do usuário permitiu carregar a ponte. Inspeção encontrou servidor ativo em `127.0.0.1:8787`; conexão HTTP exigiu execução fora do sandbox por bloqueio de sockets, sem exposição à LAN. Acesso local confirmado com `/ping` e `get_status`.
- `Allow execute_script` foi encontrado ligado e desligado pela interface autorizada; screenshot preservado. Nenhuma chamada a `execute_script`, nenhum script arbitrário executado dentro do editor. Autostart existente preservado.
- Controle nativo com `computer-use` funcionou para menus, Settings, restrição da ponte e reprodução. Diálogos/foco exigiram recuperação; coordenadas relativas à janela foram mais confiáveis que coordenadas associadas à captura. Preferir os comandos específicos da ponte para autoria.
- Transporte efetivamente testado: HTTP local do plugin Blockbench MCP. Não representa registro persistente de servidor stdio no Codex, nem integração validada no Antigravity.

## Ensaio pequeno, isolado

Fonte: cópia da cabeça central de `tools/models/source/ice_hydra.bbmodel`, reduzida a29 cubos e2 grupos, com textura original. Arquivo de laboratório `tools/models/lab/cycle93/hydra_head_control_test.bbmodel`, formato Generic Model. O `.bbmodel` completo e `.geo.json` de produção permaneceram idênticos ao Git.

Pela ponte foram executados `edit_element`, `create_animation`, `add_keyframes`, `preview_animation`, `save_project` e comandos de status/câmera. Ajustado pivô da mandíbula de `[0,-9,-12]` para `[0,-8.5,-10]`. Animação `animation.hydra_lab.jaw_test`,1,6s em loop: mandíbula0→−12→−28→−12→0 graus, com pequena inclinação/rotação da cabeça e interpolação Catmull–Rom.

Cinco poses reais do preview foram capturadas. Comparação do corpo da imagem inicial/final, excluindo os rótulos de tempo: diferença média de pixels0 nos três canais. Não substitui avaliação artística; confirma fechamento do loop nessa captura. Reprodução ao vivo também acionada no modo Animate, observada com mandíbula aberta e cursor temporal avançando.

Evidências: [comandos/resultados](qa/cycle93/blockbench/commands.json), [poses amostradas](qa/cycle93/blockbench/mandibula-poses.gif), [verificação](qa/cycle93/blockbench-validation.json), [permissão desativada](qa/cycle93/blockbench-execute-script-off.jpg). GIF contém cinco poses renderizadas pelo Blockbench, não vídeo contínuo/interpolado.

Helper `tools/qa/blockbench_probe.py`: cliente local com lista de comandos permitidos, restrição dos arquivos de modelo a `tools/models/lab`, resultados e imagens salvos sem imprimir base64. Não instala serviços nem configura MCP global.

`list_formats` confirmou ausência de GeckoLib no editor instalado. Nenhum plugin extra foi instalado. Próxima prova possível: instalar formato GeckoLib pela origem oficial, exportar uma cópia e avaliar a integração com o controlador do mod, que atualmente sobrescreve rotações procedurais da mandíbula. O ensaio atual não foi exportado/aplicado ao jogo.

## Som das hidras

Arquivo antigo estava baixo:2,4s, mono22,05kHz, RMS−24dBFS/pico−12,8dBFS. Original preservado em `tools/audio/source/hyorinmaru_roar_original.ogg`. Versão usada no mod: ganho+10dB, mono44,1kHz, OGG Vorbis; RMS−14dBFS/pico−2,7dBFS, sem clipping detectado. [Hashes/medições](qa/cycle93/audio-levels.json).

Além do nível baixo, a transformação reproduzia o rugido por som posicional enquanto a câmera se afastava do emissor, reduzindo sua presença. Agora o cue216/10,8s usa o mesmo relógio local das falas: sem atenuação para o invocador e posicional para observadores. Removido o disparo duplicado no servidor durante a transformação; rugido do ataqueN continua server-authoritative e recebe o asset reforçado. Nenhum dano, custo, alcance ou duração foi alterado.

O rugido usa o canal `Jogadores/PLAYERS`; falas continuam em `Voz/VOICE`, música em `MUSIC`. Ajuste fino auditivo ainda requer escuta do usuário. Arquivos válidos e log de cue não comprovam que o volume do dispositivo/slider esteja audível.

## Validação e continuidade

Base inicial e após correção:141 regressões/176 GameTests aprovados. Cliente base completou10 cenários; logs registraram cues24,216,330 exatamente uma vez por transformação. Photon: `check/build` com141 regressões aprovados e cliente com10 cenários completos. GameTests176 executados no perfil base nesta rodada; não foram reexecutados com Photon. Logs dos dois clientes confirmam cada cue24/216/330 uma vez, sem erro de recurso Hyōrinmaru. Isso verifica o disparo/recurso no cliente, não é gravação auditiva do dispositivo.

[Verificação final e logs](qa/cycle93/verification.json). JAR final `build/libs/bleachmod-0.2.0.jar`, SHA256 `de61e441f48fdc9d410b3f6f39785c5437e50283cc4d368dfe1c1d1309c93e3f`: ZIP íntegro, assets correspondem ao fonte, pacotes base5jars/Photon7jars com a mesma build. Sem instalação no perfil pessoal do Minecraft. As capturas do editor não são evidência de modelo novo integrado ao jogo.

Recursos: skills locais de acervo/eficiência, Bleach Mod e controle do Windows; fallback ao host atual porque OmniRoute não está operacionalmente exposto. FFmpeg imageio0.6.0 em cache, somente `build/audio-tools`; nenhum áudio/código enviado a serviço externo, sem mudanças globais de Node/Java/PATH.

Pendentes: escuta humana do rugido; observador real/servidor dedicado; autoria/exportação GeckoLib e integração do modelo no jogo. O roadmap obrigatório72 permanece na mesma ordem. Esta rodada valida a ferramenta e corrige áudio; não homologa todo o redesign.
