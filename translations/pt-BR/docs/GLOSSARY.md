# Glossário LangChain4j

## Índice

- [Conceitos Básicos](../../../docs)
- [Componentes LangChain4j](../../../docs)
- [Conceitos de IA/ML](../../../docs)
- [Guardrails](../../../docs)
- [Engenharia de Prompt](../../../docs)
- [RAG (Geração com Recuperação)](../../../docs)
- [Agentes e Ferramentas](../../../docs)
- [Módulo Agente](../../../docs)
- [Protocolo de Contexto do Modelo (MCP)](../../../docs)
- [Serviços Azure](../../../docs)
- [Testes e Desenvolvimento](../../../docs)

Referência rápida para termos e conceitos usados ao longo do curso.

## Conceitos Básicos

**Agente de IA** - Sistema que usa IA para raciocinar e agir autonomamente. [Módulo 04](../04-tools/README.md)

**Cadeia** - Sequência de operações onde a saída alimenta a próxima etapa.

**Fragmentação** - Quebra de documentos em pedaços menores. Típico: 300-500 tokens com sobreposição. [Módulo 03](../03-rag/README.md)

**Janela de Contexto** - Máximo de tokens que um modelo pode processar. GPT-5.2: 400K tokens.

**Embeddings** - Vetores numéricos que representam o significado do texto. [Módulo 03](../03-rag/README.md)

**Chamadas de Função** - Modelo gera requisições estruturadas para chamar funções externas. [Módulo 04](../04-tools/README.md)

**Alucinação** - Quando modelos geram informações incorretas, porém plausíveis.

**Prompt** - Entrada de texto para um modelo de linguagem. [Módulo 02](../02-prompt-engineering/README.md)

**Busca Semântica** - Busca pelo significado usando embeddings, não por palavras-chave. [Módulo 03](../03-rag/README.md)

**Com Estado vs Sem Estado** - Sem estado: sem memória. Com estado: mantém histórico da conversa. [Módulo 01](../01-introduction/README.md)

**Tokens** - Unidades básicas de texto que modelos processam. Afeta custos e limites. [Módulo 01](../01-introduction/README.md)

**Cadeia de Ferramentas** - Execução sequencial de ferramentas onde a saída informa a próxima chamada. [Módulo 04](../04-tools/README.md)

## Componentes LangChain4j

**AiServices** - Cria interfaces de serviço de IA com tipagem segura.

**OpenAiOfficialChatModel** - Cliente unificado para modelos OpenAI e Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Cria embeddings usando cliente oficial OpenAI (suporta OpenAI e Azure OpenAI).

**ChatModel** - Interface central para modelos de linguagem.

**ChatMemory** - Mantém histórico da conversa.

**ContentRetriever** - Encontra fragmentos relevantes para RAG.

**DocumentSplitter** - Divide documentos em pedaços.

**EmbeddingModel** - Converte texto em vetores numéricos.

**EmbeddingStore** - Armazena e recupera embeddings.

**MessageWindowChatMemory** - Mantém janela deslizante das mensagens recentes.

**PromptTemplate** - Cria prompts reutilizáveis com espaços reservados `{{variable}}`.

**TextSegment** - Fragmento de texto com metadados. Usado em RAG.

**ToolExecutionRequest** - Representa solicitação de execução de ferramenta.

**UserMessage / AiMessage / SystemMessage** - Tipos de mensagens na conversa.

## Conceitos de IA/ML

**Few-Shot Learning** - Fornecer exemplos nos prompts. [Módulo 02](../02-prompt-engineering/README.md)

**Modelo de Linguagem Grande (LLM)** - Modelos de IA treinados com grande volume de texto.

**Esforço de Raciocínio** - Parâmetro do GPT-5.2 que controla profundidade do raciocínio. [Módulo 02](../02-prompt-engineering/README.md)

**Temperatura** - Controla aleatoriedade da saída. Baixa=determinístico, alta=criativo.

**Banco de Dados Vetorial** - Banco especializado para embeddings. [Módulo 03](../03-rag/README.md)

**Zero-Shot Learning** - Realizar tarefas sem exemplos. [Módulo 02](../02-prompt-engineering/README.md)

## Guardrails - [Módulo 00](../00-quick-start/README.md)

**Defesa em Profundidade** - Abordagem de segurança em camadas combinando guardrails no nível da aplicação com filtros de segurança do provedor.

**Bloqueio Rigoroso** - Provedor retorna erro HTTP 400 para violações severas de conteúdo.

**InputGuardrail** - Interface LangChain4j para validar entrada do usuário antes de chegar ao LLM. Economiza custo e latência bloqueando prompts nocivos precocemente.

**InputGuardrailResult** - Tipo de retorno da validação do guardrail: `success()` ou `fatal("razão")`.

**OutputGuardrail** - Interface para validar respostas da IA antes de devolver ao usuário.

**Filtros de Segurança do Provedor** - Filtros embutidos de conteúdo de provedores de IA (ex. GitHub Models) que capturam violações na camada da API.

**Recusa Suave** - Modelo recusa educadamente responder sem lançar erro.

## Engenharia de Prompt - [Módulo 02](../02-prompt-engineering/README.md)

**Cadeia de Pensamento** - Raciocínio passo a passo para maior precisão.

**Saída Constrangida** - Imposição de formato ou estrutura específica.

**Alta Vontade** - Padrão do GPT-5.2 para raciocínio minucioso.

**Baixa Vontade** - Padrão do GPT-5.2 para respostas rápidas.

**Conversa Multi-Turn** - Manter contexto durante trocas.

**Prompt por Papel** - Definir persona do modelo via mensagens de sistema.

**Autorreflexão** - Modelo avalia e melhora sua saída.

**Análise Estruturada** - Framework fixo de avaliação.

**Padrão de Execução de Tarefas** - Planejar → Executar → Resumir.

## RAG (Geração com Recuperação) - [Módulo 03](../03-rag/README.md)

**Pipeline de Processamento de Documentos** - Carregar → fragmentar → embedar → armazenar.

**Armazenamento de Embeddings em Memória** - Armazenamento não persistente para testes.

**RAG** - Combina recuperação com geração para fundamentar respostas.

**Pontuação de Similaridade** - Medida (0-1) de similaridade semântica.

**Referência de Fonte** - Metadados sobre o conteúdo recuperado.

## Agentes e Ferramentas - [Módulo 04](../04-tools/README.md)

**Anotação @Tool** - Marca métodos Java como ferramentas chamáveis por IA.

**Padrão ReAct** - Raciocinar → Agir → Observar → Repetir.

**Gerenciamento de Sessão** - Contextos separados para usuários diferentes.

**Ferramenta** - Função que um agente de IA pode chamar.

**Descrição da Ferramenta** - Documentação do propósito e parâmetros da ferramenta.

## Módulo Agente - [Módulo 05](../05-mcp/README.md)

**Anotação @Agent** - Marca interfaces como agentes de IA com definição declarativa de comportamento.

**Agent Listener** - Gancho para monitorar execução do agente via `beforeAgentInvocation()` e `afterAgentInvocation()`.

**Escopo Agente** - Memória compartilhada onde agentes armazenam saídas usando `outputKey` para agentes subsequentes consumirem.

**AgenticServices** - Fábrica para criação de agentes usando `agentBuilder()` e `supervisorBuilder()`.

**Workflow Condicional** - Direcionar com base em condições para agentes especialistas diferentes.

**Humano no Loop** - Padrão de workflow que adiciona checkpoints humanos para aprovação ou revisão de conteúdo.

**langchain4j-agentic** - Dependência Maven para construção declarativa de agentes (experimental).

**Loop Workflow** - Iterar execução do agente até condição ser satisfeita (ex.: pontuação de qualidade ≥ 0.8).

**outputKey** - Parâmetro de anotação do agente que especifica onde resultados são armazenados no Escopo Agente.

**Workflow Paralelo** - Executar múltiplos agentes simultaneamente para tarefas independentes.

**Estratégia de Resposta** - Como supervisor formula resposta final: LAST, SUMMARY, ou SCORED.

**Workflow Sequencial** - Executar agentes em ordem onde a saída flui para a próxima etapa.

**Padrão Supervisor Agent** - Padrão avançado onde um LLM supervisor decide dinamicamente quais sub-agentes invocar.

## Protocolo de Contexto do Modelo (MCP) - [Módulo 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependência Maven para integração MCP no LangChain4j.

**MCP** - Model Context Protocol: padrão para conectar apps de IA a ferramentas externas. Crie uma vez, use em todo lugar.

**Cliente MCP** - Aplicação que conecta a servidores MCP para descobrir e usar ferramentas.

**Servidor MCP** - Serviço que expõe ferramentas via MCP com descrições claras e esquemas de parâmetros.

**McpToolProvider** - Componente LangChain4j que envolve ferramentas MCP para uso em serviços e agentes de IA.

**McpTransport** - Interface para comunicação MCP. Implementações incluem Stdio e HTTP.

**Transporte Stdio** - Transporte de processo local via stdin/stdout. Útil para acesso a sistema de arquivos ou ferramentas de linha de comando.

**StdioMcpTransport** - Implementação LangChain4j que inicializa servidor MCP como subprocesso.

**Descoberta de Ferramentas** - Cliente consulta servidor por ferramentas disponíveis com descrições e esquemas.

## Serviços Azure - [Módulo 01](../01-introduction/README.md)

**Azure AI Search** - Busca em nuvem com capacidades vetoriais. [Módulo 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Implanta recursos Azure.

**Azure OpenAI** - Serviço empresarial de IA da Microsoft.

**Bicep** - Linguagem de infraestrutura como código para Azure. [Guia de Infraestrutura](../01-introduction/infra/README.md)

**Nome da Implantação** - Nome para implantação de modelo no Azure.

**GPT-5.2** - Modelo OpenAI mais recente com controle de raciocínio. [Módulo 02](../02-prompt-engineering/README.md)

## Testes e Desenvolvimento - [Guia de Testes](TESTING.md)

**Dev Container** - Ambiente de desenvolvimento conteinerizado. [Configuração](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground de modelos de IA gratuito. [Módulo 00](../00-quick-start/README.md)

**Testes In-Memory** - Testes com armazenamento em memória.

**Testes de Integração** - Testes com infraestrutura real.

**Maven** - Ferramenta de automação de build Java.

**Mockito** - Framework de mocking para Java.

**Spring Boot** - Framework de aplicações Java. [Módulo 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido usando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->