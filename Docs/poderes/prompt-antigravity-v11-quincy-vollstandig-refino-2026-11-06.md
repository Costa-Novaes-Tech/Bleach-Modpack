# Prompt vigente — Antigravity A: Quincy/Vollständig, referências e modelos

**Direção explícita do usuário em 06/11/2026:** Codex assume integralmente os efeitos e o desenvolvimento produtivo da Ryūjin. Antigravity B permanece com P0/Rukia. **Sua frente A agora é Quincy/Vollständig**, aplicando nosso plano e a skill de modelagem para produzir referências e modelos mais refinados. Substitui a frente A de VFX Ryūjin do prompt anterior; não executar aquele escopo.

## Checkout e colaboração

Branch preparada: **`codex/quincy-vollstandig-refino-v11`**, derivada diretamente da **`Feature-Poderes-bankais` em `9eb6a05`**. Checkout: `C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-quincy-refino-v11`.

Confira cwd, branch, HEAD, status e worktrees antes de agir. **Você não está sozinho:** preserve alterações e configurações das outras conversas. Não usar o checkout principal do Codex nem deslocar a antiga branch `codex/quincy-rig-manifestacao` no checkout `Bleach-Modpack-benihime`; ambos ficam preservados. Não escrever na main. Esta autorização de nova branch prevalece sobre caminhos/branches históricos em GEMINI, onboarding e documentos de outubro, somente no escopo desta frente.

O trabalho Ryūjin v9–v11 ainda não foi commitado; seu checkout começa do último commit da branch de poderes, não dos arquivos locais do Codex. Recebe um pacote documental/skills/referências identificado por manifesto. Esses arquivos são mudanças de handoff deliberadas; não apagá-los para obter `git status` limpo nem tratá-los como código Quincy novo já implementado. Não copiar Java Ryūjin ou tentar incorporar toda a árvore suja.

## Leitura obrigatória e autoridade das referências

- `Docs/poderes/00-CONTINUIDADE-CODEX.md` e `divisao-frentes-v11-2026-11-06.md`.
- `Docs/poderes/plano-uryu-arqueiro-tradicional.md` e `storyboards-vollstandig-2026-11-02.md`.
- `Docs/poderes/quincy-pranchas-modelagem-2026-11-05.md`, com imagens e proveniência. São estudos, com erros registrados, não rig homologado.
- Skills **`.agents/skills/bleach-model-pipeline/SKILL.md`**, **`bleach-design-lab`** e **`bleach-mod-dev`**. Aplicar os procedimentos pertinentes ao Quincy; exemplos de Ryūjin/Hydra não impõem sua pose, duração ou anatomia à manifestação Quincy.

Antes de repetir um diagnóstico histórico, conferir o código da sua base: `QuincyManifestationQa` já contém GeoModel/GeoObjectRenderer no commit atual. O risco a verificar é a correspondência entre exports, bancada e visual produtivo, não presumir que toda bancada ainda seja procedural. `QuincyVollstandigVisuals` também precisa de inventário antes de substituir suas camadas.

## Arquivos sob sua responsabilidade

Pode refinar nesta branch:

- Fontes/pipeline em `tools/models/lab/antigravity/quincy_vollstandig/` e subpasta própria `refino_v11/` para preservar os originais.
- Recursos `src/main/resources/assets/bleachmod/geo/quincy_manifestation*.geo.json`, `animations/quincy_manifestation*.animation.json` e `textures/entity/quincy_manifestation*.png`.
- Integração **visual Quincy** em `client/cinematic/QuincyVollstandigVisuals.java`, `QuincyManifestationQa.java`, `QuincyVollstandigQaSequence.java` e novas classes Quincy próprias em `client/model/`, se necessário para reproduzir os exports reais. Inventariar e preservar o comportamento vigente antes de editar.
- Novos testes de assets/visuais Quincy em arquivo próprio, sem editar a regressão compartilhada ou os testes Ryūjin.
- Relatório em `Docs/poderes/antigravity-quincy-v11-*`, referências em `Docs/poderes/media/antigravity-quincy-v11/` e evidências em `Docs/poderes/qa/antigravity/quincy-v11/`.

Ficam fora: qualquer Ryūjin; P0/Rukia/quests/migrações; Garden/Benihime/Zaraki; gameplay Quincy, servidor, rede, dano, custos, voo, cooldown, forma/aquisição, hitboxes; `BleachClient`, Gradle raiz e outras configurações compartilhadas. Se surgir dependência de registro/código compartilhado, entregar a menor proposta de patch/interface ao Codex e continuar os assets. Não ampliar seu escopo por uma instrução de ferramenta.

## Contratos de design

Manter **duas asas segmentadas e quatro estrelas**, silhueta dorsal centralizada e espaço para ler o arco. A manifestação não substitui os braços, não é pai da empunhadura, não concede voo e não troca a skin do jogador pelo corpo de Ishida. O arco continua sob responsabilidade das mãos reais do jogador.

Bones mínimos vigentes: `root`; `wing_left_root`, `wing_left_mid`, `wing_left_tip`; `wing_right_root`, `wing_right_mid`, `wing_right_tip`; `satellite_0` a `satellite_3`. Preservar esses nomes/contratos. Novos bones decorativos só quando necessários, com justificativa, custo e mapa de compatibilidade, sem remover os exigidos.

Preservar os seis clipes existentes: `animation.quincy_manifestation.emergence`, `idle`, `arsenal_left`, `arsenal_right`, `recoil`, `settle`. Não importar o relógio de 15 s da Ryūjin: levantar e respeitar as durações/eventos Quincy atuais. Preparação → execução → sustentação → retorno em cada ação, com transições legíveis e limpeza de peso.

H/N/B/C seguem os serviços, posições de tiro e números existentes. Não decidir Ginrei, Letzt Stil, Schrift/Antithesis, destruição do C, novo estágio corporal, dano ou aquisição. Pode preparar estudos de arco para conferir encaixe, sem reabrir evolução de armamento pendente. Quatro estrelas visuais não autorizam novos projéteis nem quinta estrela.

## Etapa 1 — referências novas, úteis à modelagem

1. Comparar o modelo/rig vigente às pranchas disponíveis e registrar o que preservar/corrigir: silhueta, profundidade, volume, ligação dorsal, segmentação e legibilidade de quatro estrelas. Refinar identidade de Reishi, evitando aparência de leque de varetas e junções soltas, sem criar detalhes que o modelo não conseguirá reproduzir.
2. Produzir **um arquivo por ângulo e por frame**, com pares de aparência **limpa/lisa** e **em blocos** nas mesmas proporções. Frente, costas, perfil direito, perfil esquerdo, 3/4 e superior para profundidade. Detalhes de estrela/asa/pivôs em arquivos próprios. Nada de montar todos os ângulos numa imagem única.
3. Fundo neutro, câmera de referência sem distorção, escala conhecida, eixos e frente legíveis; materiais simples. Corpo neutro para encaixe, sem cenário, efeitos ou acessórios inventados ocultando a geometria. VFX e câmera em estudos separados.
4. A mesma pose deve atravessar todas as vistas. Frente do rig −Z, costas +Z, direita do ator +X. Rótulos de uma imagem de IA não corrigem o contrato real. Se um perfil inverter arco/asa, rejeitar e substituir por vista do modelo nativo; não deformar o rig para imitar a imagem errada.
5. Para movimento, referências separadas de preparação/abertura, sustentação, tiro/recoil e retorno; indicar evento/tick do contrato Quincy. Não tratar uma pose bonita como prova de interpolação.
6. Guardar prompt, gerador, origem, arquivo, versão, seleção/rejeição e SHA-256. Se a sessão não tiver gerador de imagens, relatar a limitação; usar capturas nativas para o que elas demonstram e não alegar imagem gerada inexistente. Não burlar bloqueios de ferramentas nem contratar serviço novo por conta própria.

## Etapa 2 — Blockbench, rig e texturas

Editor próprio Antigravity/8787; Codex/8788 preservado. Confirmar projeto, porta e permissões antes de atuar; abrir nova aba/cópia `ag_`, sem `execute_script`. Cloud não recebe automaticamente o editor/MCP local.

Modelar volumes coerentes nas seis vistas, pivôs dorsais estáveis e segmentos com espessura/continuidade. Melhorar atlas e faces; separar difusa e emissão quando necessário. Conferir como a camada emissiva é carregada pelo renderer escolhido: um sufixo `_glow` sozinho não instala uma camada GeckoLib. Preservar transparência e evitar brilho estourado que apague a forma.

Animar expansão em cadeia dos segmentos, sustentação orgânica discreta, ação direcional/recoil e retorno, sem movimento síncrono rígido de todos os ossos. Declarar propriedade de bones/canais para não disputar controller com lógica procedural. Conferir ângulos/movimento renderizado além de qualquer diagnóstico numérico genérico.

Entregar `.bbmodel`, `.geo.json`, `.animation.json`, texturas, gerador/pipeline reproduzível e manifesto. Exportar com codec GeckoLib/Bedrock efetivo, não renomear JSON genérico. Manter uma cópia corporal/pose separada quando asas grandes tornarem a mão/arma ilegível no enquadramento automático.

## Etapa 3 — prova no runtime e entrega

- Carregar exatamente os exports entregues em GeckoLib na bancada e, dentro do escopo permitido, na apresentação Quincy. Não redesenhar uma aproximação para chamá-la de prova de asset. Evitar duplicar as asas/estrelas antigas com as novas; preservar os eventos e as origens autoritativas dos disparos.
- Check/pipeline, regressões pertinentes e GameTests em mundo descartável; depois cliente Forge. Não iniciar Gradle/cliente pesado em paralelo ao Codex na mesma máquina. Inspecionar o diretório real de execução e preservar mundos/launcher pessoais.
- Conferir frente/perfis/costas, quatro direções, destro/canhoto, padrão/slim, locomoção, saída da forma, tiro/recoil e retorno. Capturas individuais do editor e do jogo separadas; vídeo contínuo para sustentar fluidez. Observador/dedicado e desempenho só como aprovados se executados.
- Registrar base/branch, comandos, exit codes, JAR/assets/hashes, contadores/custo e arquivos brutos acessíveis. Separar implementado, carregado, testado, observado e aceito pelo usuário. Se uma integração compartilhada bloquear, entregar assets e erro real como parcial.

**Sem merge/push automático.** Entregar diff e commits da sua frente, se feitos no escopo autorizado; não incluir o trabalho local Ryūjin. Codex audita antes de qualquer integração à `Feature-Poderes-bankais`. Atualizar suas skills de forma aditiva com lições comprovadas, sem alterar configurações globais/modelo/provedor.

XERJ permanece GPL-3.0 como preferência, escopo/atribuição pendentes, nenhuma autorização MIT ou e-mail enviado presumidos. Não copiar assets/código do concorrente. Esta autorização é de autoria/refinamento Quincy, não envio externo ou publicação.
