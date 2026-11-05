# 131 — Integração Hyōrinmaru/Higashi e bateria do usuário

27/10/2026. Pedido explícito de merge. Branch Feature-Poderes-bankais: correções Senbon e documentos preservados em438628e; merge da codex/ryujin-bankai-sol-selamento (0675778) em a18bfc8, estratégia ort, sem conflitos. Senbon já integrado anteriormente, sem merge duplicado. Main e checkouts das outras conversas intactos. Logs modificados anteriores excluídos dos commits e preservados. Sem push nesta rodada.

`python tools/models/ice_hydra.py --check`: exit0; fonte, hierarquia, animações, textura e runtime correspondem. Check/build integrado aprovado; resultado abaixo. Nenhuma nova execução gráfica ou homologação artística nesta rodada. Protocolo2.36: distribuir o mesmo pacote para cliente e servidor.

## Teste manual prioritário

1. Senbon Bankai parado por10s: aura pode existir, mas nenhum corte/rastro deve iniciar sozinho. Atacar uma vez e parar: efeito termina, sem repetir. Repetir selada/Shikai.
2. Combo Senbon: diagonal→horizontal→estocada; verificar arma na mão e continuidade frente/perfil/costas, depois primeira pessoa. Testar destro/canhoto, ataque isolado e cliques encadeados, defesa interrompendo e troca de item. Gravar especialmente diferenças entre espada e rastro; este ainda usa aproximação manual e relógio fixo, pendência129/130.
3. Hyōrinmaru: invocação completa, observar três mandíbulas abrindo/sustentando/fechando sem travar ou dentes atravessando face; neve deve sair das bocas. Testar ataque triplo contra chão e parede, explosão sem retorno, pescoço sem buracos durante curva; conferir transformação e controle no fim.
4. Higashi: alvo≤7% deve executar; alvo acima7% que morre pelo dano também; alvo que sobrevive e golpe no vazio não devem abrir câmera de execução. Corte sai do quadril e sobe diagonalmente, pausa2s depois de atravessar e só então fogo/cinzas. Verificar ambos em quadro, lâmina seguindo mão, retorno de câmera/HUD/controles. Repetir perto de parede e corredor para flagrar troca brusca de flanco; registrar se a cena aborta quando ambos bloqueados.
5. Regressão rápida: uma transformação Ryūjin aprovada e uma Benihime, para confirmar preservação. Com outro jogador, repetir combo/sopro/execução como observador: efeito visível sem câmera do observador sequestrada. Se só houver solo, marcar multiplayer pendente.

C Senbon pode ser conferido funcionalmente (dash, parede e recuperação), mas não há novo refino visual dessa habilidade nos commits AG entregues. Não confundir ausência da melhoria com regressão deste merge. Não testar comandos de QA em mundo normal como se estivessem liberados: dependem de flags e mundo de laboratório.

Use mundo de testes para técnicas destrutivas. Um vídeo curto por bloco com forma/tecla e erro observado é suficiente; não é preciso repetir todo o kit. Registrar versão/commit a18bfc8 ou posterior contendo este merge, perspectiva e mão dominante.

## Resultado integrado

`./gradlew.bat check build --offline --console=plain`: exit0, BUILD SUCCESSFUL em2m13s,149 cenários de regressão aprovados. JAR gerado em build/libs/bleachmod-1.0.0.jar. Idempotência da hidra exit0. Não foram executados runGameTestServer nem cliente gráfico nesta rodada; teste visual permanece com o usuário.
