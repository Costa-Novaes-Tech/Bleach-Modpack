# Ciclo 71 — plano de validação visual

Preparado em 20/10/2026, branch `Feature-Poderes-bankais`. O código está sendo alterado pelo agente principal; este plano não declara resultados de uma build nova.

## Ambiente e isolamento

- Minecraft Forge 1.20.1; Java 17. Confirmar versões efetivas no log de inicialização.
- Perfil base: `./gradlew.bat -PbenihimeQa=true runClient --offline --console=plain`, diretório `run-benihime-qa/base`.
- Perfil Photon: executar separadamente com `-PphotonPilot=true`; não presumir equivalência visual pelo GameTest.
- Bootstrap cria save novo `Benihime-QA-68-<timestamp>`; nunca abrir nem reutilizar mundo pessoal.
- Capturas originais: `run-benihime-qa/base/benihime-qa/<timestamp>/screenshots`, acompanhadas de `frames.jsonl` e `environment.json`.
- Empacotamento: `python tools/qa/benihime_evidence.py <diretório-da-captura> --output-root Docs/poderes/qa/benihime/cycle71`.

## Ordem de execução após liberação da build

1. Cliente inicia sem crash; registrar versões, renderizador e configurações.
2. Entrada no plano, de dia: cortina fechada, pega central, apoio e avanço, levantamento, pose final, retorno da câmera e FOLLOW.
3. Entrada à noite com mesmos marcos; verificar ocultação e legibilidade.
4. Terreno irregular no save descartável: apoios em três alturas (referência, +1 e +2 blocos), conferindo contato com o solo, comprimento constante dos membros e comportamento de fallback.
5. Higashi com alvo abaixo de 7%: preparação no quadril, corte diagonal ascendente, espada atravessa, pausa de dois segundos, incineração/cinzas e só então retorno da câmera.
6. Higashi com alvo acima de 7% mas cujo dano normal seja letal: mesmo efeito de execução confirmado, sem pré-autorizar morte por estimativa.
7. Controles negativos: alvo que sobrevive, aliado, invulnerável e cura antes do impacto; nenhuma incineração falsa.
8. Interrupção por dano/morte, mudança de dimensão, saída e troca de câmera; verificar limpeza.
9. Áudio: comparar falas e música escolhidas com cues reais. Logs comprovam disparo, não qualidade de escuta; sem captura de loopback, registrar pendência.
10. Dedicated + dois clientes e comparativo Photon: resultados próprios; singleplayer não comprova sincronismo remoto.

## Critérios de evidência

PNG de marco não comprova fluidez completa. Vídeo de capturas amostradas será identificado como tal; não declarar captura contínua se não houver. Para cada cenário, registrar PASSOU/FALHOU/PENDENTE com reprodução, links, observação e limitações. Não modificar Zaraki, gameplay incidental ou arquivos de runtime/testes de responsabilidade do principal.

## Estado inicial

PENDENTE: aguardando agente principal concluir implementação e liberar início do cliente. Nenhum teste novo nem aprovação artística nesta preparação.
