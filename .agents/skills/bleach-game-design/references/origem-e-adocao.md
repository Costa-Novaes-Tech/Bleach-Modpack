# Origem e adoção — 20/10/2026

Esta skill é uma redação específica para o Bleach Mod, apoiada pelos contratos existentes em `Docs/game-design/`. Os links abaixo registram referências externas consultadas; não indicam instalação integral nem garantia de compatibilidade dos pacotes.

| Referência | Aproveitamento | Ajuste necessário |
|---|---|---|
| [ncdlek — game-design-architect](https://github.com/ncdlek/game-dev-agent-skills/blob/main/skills/game-design-architect/SKILL.md) | Revisão de regras, estados, feedback, falhas, integrações e entrega verificável | Preservar os 16 módulos locais, a pesquisa canônica e a fase conceitual; não impor o template externo de 12 módulos nem números ilustrativos como decisões. |
| [fcsouza — game-dev](https://github.com/fcsouza/agent-skills/blob/main/plugins/game-dev/README.md) | Biblioteca de ideias de design/narrativa a consultar por necessidade | O pacote apresenta comandos e hooks para Claude Code e engenharia web. Esse runtime de plugin não é necessário a esta skill. |
| [fcsouza — quest-narrative-coherence](https://github.com/fcsouza/agent-skills/blob/main/plugins/game-dev/narrative/quest-narrative-coherence/SKILL.md) | Conferir consistência de era, personagens, locais, pré-requisitos e recompensas antes de consolidar conteúdo | Usar documentos/registros locais e o contrato de mundo compartilhado; preservar propostas e conteúdo lateral legítimo. |

Os fluxos locais foram escritos em português, sem importar código, comandos, hooks, exemplos de banco de dados ou o corpo integral dos arquivos externos. Não reproduzir citações atribuídas a designers sem fonte verificável, nem tratar máximas genéricas de balanceamento como leis.

As versões externas consultadas são os arquivos públicos de `main` disponíveis na data acima; nenhum SHA externo foi verificado. Revisitar os links se uma atualização externa for relevante e registrar a nova data. A fonte de autoridade das decisões do mod continua sendo a equipe e sua documentação.

## Instalação e disponibilidade

Fonte mantida no repositório: `.agents/skills/bleach-game-design/`. Um vínculo no diretório de skills do usuário pode apontar para essa fonte para descoberta pelo Codex quando o chat estiver aberto no diretório pai do repositório. Verificar o vínculo e não substituir uma instalação existente de mesmo nome.

Os arquivos desta skill são instruções e referências. O plugin completo `game-dev` não é declarado instalado, e comandos `/game-dev:*` ou hooks externos não são declarados ativos. A skill de execução/QA `bleach-mod-dev` permanece independente.
