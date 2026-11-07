# 138 — Prompt Antigravity: reconstruir Senbon usando quadros137

Continue na branch existente codex/design-poderes-criaturas. A entrega anterior ainda não reproduz os três golpes. O usuário confirmou que partículas estão aceitáveis: defeito principal é PLAYER/KATANA não executar a coreografia. Não gastar esta etapa refazendo VFX ou ultimate.

Regras de trabalho: confira Git/status/branch antes de editar. Você não está sozinho; preserve mudanças de terceiros. NÃO FAZER MERGE nesta rodada, nem main, push ou instalação de novas dependências. Commit focado apenas conforme autorização vigente da sua conversa. Não trocar checkout de outra conversa nem sobrescrever documentos. Todos relatórios e evidências em Docs/poderes. Teste de código não aprova poses; execute cliente real ou declare limite.

Leia137 inteiro, abra media/137-storyboards/index.html e examine S1/S2/S3 quadro a quadro. Se não conseguir abrir HTML, ler PNGs e poses.json. Não inventar números de capítulo/frame: usar IDs fornecidos. As pranchas são metas esquemáticas e não modelo3D ou offsets prontos.

Fase1 — baseline/causa. No Forge real, partículas desligadas por flag existente, mostrar clip ativo e movimento de espada no combo. Confirmar parser corrigido, índice, velocidade, prioridades/blends e transform de item efetivamente aplicado. Inspecionar braço/item juntos: presença de rightItem no JSON não comprova render. Não deixar BetterCombat + Signature/Elemental competirem pela mesma ação. Não aplicar patch global para toda zanpakuto.

Fase2 — um golpe por vez. Começar S1. Reproduzir S1-F00..F07 e posições intermediárias T000..T030, sobretudo preparação altaF02, travessiaF04 e final baixoF06. Criar captura no MESMO marco e anexar comparação com desenho. Só seguir aoS2 quando não houver katana estática, mão deslizando ou ponta oposta. Não pedir confirmação de rotina: esse é controle interno de qualidade, aprovação artística final pertence ao usuário.

S2: usar S2-F00..F07, plano horizontal no peito; herdar S1final sem reset. S3: S3-F00..F07, ponta avança frontal e recolhe. Coreografia corpo/pés está no137 e deve ser adicionada ao esquema estático; cabeça mantém alvo. Uma mão; outra contrabalança. Validar frente/perfil e destro/canhoto. Se o perfil não mostra avanço da ponta noS3, não é estocada válida.

Converter coordenadas anatômicas em pose real de rig com pivôs do modelo e item. Verificar graus/radianos, unidades1/16 e ordem de rotação/display. Não copiar posiçõesXYZ em blocos para canal de item. Definir cabo solidário à palma e derivar ponta pela transformação real. Não mover o item separado para esconder braço errado. Preservar comprimento e gameplay existentes.

Fase3 — continuidade real. Reproduzir1→2→3→1 e isolados; cancelar/defender/voltar. Arquivos com endpoints iguais não bastam: inspecionar begin/end/return e comportamento do mixer BetterCombat. Não começar todo golpe do mesmo idle. Por outro lado, soltar clique deve recuperar idle sem ficar congelado no final baixo. Tempo de storyboard é régua autoral, mapear velocidade real e upswing; não mudar dano/alcance/stamina para facilitar.

Fase4 — reaplicar partículas existentes sem desviar foco. Validar faixa depois da katana, preservar eventoMELEE_TRAIL/protocolo/aura passiva. Se rastro revela divergência de transform, corrigir vínculo e relógio; não adicionar nuvem para esconder. Fora isso, partículas não precisam de redesign. Sem Hakuteiken/Garden nesta correção.

Fase5 — pacote de prova. Vídeos contínuos sem/comVFX, frente/perfil/costas, três formas, destro/canhoto, primeira pessoa e interrupção. Contatos de cada Fxx versus referência, declarar aproximações e diferenças. check/regressionTest e testes necessários. Se não rodou cliente, não dizer visualmente concluído. Não repetir relatório de matemática como se fosse captura. Entregar diff focado+causa, limitações e lista de marcos conformes/não conformes. Hyōrinmaru, Higashi, Ryūjin transformação, Benihime e Zaraki intocadas. SEM MERGE: aguardar revisão do Codex/usuário.
