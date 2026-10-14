> **Estado vigente — ciclo 36, 13/10/2026:** proporção/inclinação finais da bainha homologadas pelo usuário; documentação consolidada de combate, stamina, armas indestrutíveis e saque/guarda. [Contratos atuais, arquivos, histórico e próxima proposta](36-consolidacao-homologacao-proximos-passos-2026-10-13.md). Próxima recomendação: esquiva curta com stamina, ainda sem implementação. Registros anteriores abaixo são históricos.

> **Estado vigente — ciclo 35, 13/10/2026:** bainha ampliada em 15% e inclinada mais 18 graus para baixo, mantendo curva e lado corrigido. [Arquivo, valores e conferência visual](35-escala-inclinacao-bainha-2026-10-13.md). Ajuste final aguarda homologação; registros anteriores abaixo são históricos.

# Ciclo 34 — Refinamento da bainha e descrição de Resistência

Data: 13/10/2026. Branch: Feature-Poderes-bankais. Usuário homologou progressão de stamina e funcionamento da bainha, solicitando proporção menor, curvatura e troca do lado observado nas imagens. Não presume homologação visual do refinamento.

## Problema e correção

A geometria anterior era uma peça de 23 unidades, espessa e reta; destacava-se mais que a própria espada. O código tratava X negativo como esquerda, mas no modelo PlayerModel esse lado corresponde ao braço direito. Portanto, o nome da variável não refletia a posição real observada pelo usuário.

Agora a bainha usa três segmentos sobrepostos e afunilados, com mudanças suaves de aproximadamente três e quatro graus. Comprimento longitudinal aproximado de 15,5 unidades, com escala 0,85: aproximadamente 43% menor que o corpo anterior. Espessura inicial 1,3 × 1 unidade antes da escala, também menor; cabo, amarrações, guarda e ponteira acompanham a nova proporção. Acabamento da ponta acompanha a curva. Não modifica a espada na mão.

X positivo coloca a bainha na esquerda anatômica do personagem destro; canhotos espelham para direita. O ângulo lateral passa de 35 para 25 graus, inclinação de 12 para oito, e a posição fica ligeiramente mais frontal. O gesto do braço passa a alcançar o lado corrigido; a inclinação em primeira pessoa também respeita o braço usado, inclusive mão secundária.

## Arquivos e mudanças

1. `client/model/ZanpakutoSheathLayer.java`: substitui bloco único por hierarquia scabbard/middle/tip, com rotação e redução de seção; fittings/tip_cap seguem a ponta. Redimensiona grip/wrap/guardas e corrige sinal do deslocamento lateral. Preserva paletas das três identidades, condições de presença, bainha vazia ao empunhar, invisibilidade e skins default/slim.
2. `client/model/ZanpakutoMotion.java`: inverte componente de alcance lateral para acompanhar a cintura corrigida; primeira pessoa calcula braço principal/secundário para espelhar inclinação. Duração continua oito ticks, exclusivamente visual e sem atraso em combate.
3. `assets/bleachmod/lang/pt_br.json` e `en_us.json`: tooltip `attribute.bleachmod.resistance.desc` informa redução de dano físico e aumento de stamina máxima com ganhos decrescentes. A tela K já consulta essa chave no botão de distribuir pontos, portanto não necessita mudança de layout ou lógica da tela.
4. Documentação atualizada somente em Docs/poderes. Fórmula, custos, persistência, protocolo 2.9, schema 7, vínculo e sincronização conservam o ciclo 33. Modelos/texturas das espadas permanecem intactos.

## Validação

`gradlew check build --offline --console=plain`, log local `build/cycle34-validation.log`; build concluída com sucesso e 73 cenários de regressão aprovados. JAR atualizado: `build/libs/bleachmod-0.2.0.jar`. Ajuste visual e traduções: regressão existente e compilação são suficientes para verificação automatizada desta mudança; nenhuma homologação visual presumida e nenhum teste novo espelhando geometria foi criado. Os 63 GameTests do ciclo 33 seguem como validação da lógica inalterada, não foram reexecutados neste ciclo.

## Roteiro visual curto

1. Atualize a build, guarde a zanpakutō e observe em F5 de frente/lado/costas: bainha menor, mais fina, curva discreta e lado esquerdo do personagem destro. O lado na tela muda conforme câmera; referência é o corpo do personagem.
2. Saque e guarde, teste primeira pessoa, mão secundária e braço principal esquerdo. Confira gesto coerente com a cintura e ausência de pose presa ao trocar rapidamente slots.
3. Compare Asauchi, Ryujin e Hyōrinmaru com a espada empunhada; verifique agachamento, armadura e Bankai de gelo. Encaixe/proporção continuam sujeitos ao retorno visual do usuário.
4. Abra K e passe o cursor no botão de Resistência: tooltip deve mencionar stamina máxima, em português ou inglês conforme idioma. Compra conserva capacidade variável sem recarga gratuita, já homologada.

## Continuidade

Após homologação do encaixe, retomar planejamento da esquiva curta com stamina. Nenhuma habilidade, bankai, NPC ou migração de atributos nova neste ciclo.

## Homologação posterior — 13/10/2026

Usuário aprovou o visual final após o ciclo 35. Encaixe/proporção/inclinação encerrados neste escopo. [Consolidação e próximos passos](36-consolidacao-homologacao-proximos-passos-2026-10-13.md). Pendências de teste descritas acima registram o estado histórico da entrega e não invalidam esse aceite visual.
