# Glossário LangChain4j

## Índice

- [Conceitos Principais](../../../docs)
- [Componentes LangChain4j](../../../docs)
- [Conceitos de AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Engenharia de Prompt](../../../docs)
- [RAG (Geração com Recuperação)](../../../docs)
- [Agentes e Ferramentas](../../../docs)
- [Módulo Agente](../../../docs)
- [Protocolo de Contexto de Modelo (MCP)](../../../docs)
- [Serviços Azure](../../../docs)
- [Testes e Desenvolvimento](../../../docs)

Referência rápida para termos e conceitos usados ao longo do curso.

## Conceitos Principais

**Agente AI** - Sistema que usa AI para raciocinar e agir autonomamente. [Módulo 04](../04-tools/README.md)

**Cadeia** - Sequência de operações onde a saída alimenta o passo seguinte.

**Chunking** - Dividir documentos em pedaços menores. Típico: 300-500 tokens com sobreposição. [Módulo 03](../03-rag/README.md)

**Janela de Contexto** - Máximo de tokens que um modelo pode processar. GPT-5.2: 400K tokens.

**Embeddings** - Vetores numéricos que representam o significado do texto. [Módulo 03](../03-rag/README.md)

**Function Calling** - Modelo gera pedidos estruturados para chamar funções externas. [Módulo 04](../04-tools/README.md)

**Alucinação** - Quando modelos geram informações incorretas mas plausíveis.

**Prompt** - Entrada de texto para um modelo de linguagem. [Módulo 02](../02-prompt-engineering/README.md)

**Pesquisa Semântica** - Pesquisa por significado usando embeddings, não palavras-chave. [Módulo 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: sem memória. Stateful: mantém histórico da conversa. [Módulo 01](../01-introduction/README.md)

**Tokens** - Unidades básicas de texto que os modelos processam. Afetam custos e limites. [Módulo 01](../01-introduction/README.md)

**Encadeamento de Ferramentas** - Execução sequencial de ferramentas onde a saída informa a próxima chamada. [Módulo 04](../04-tools/README.md)

## Componentes LangChain4j

**AiServices** - Cria interfaces de serviço AI com tipagem segura.

**OpenAiOfficialChatModel** - Cliente unificado para modelos OpenAI e Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Cria embeddings usando cliente Oficial OpenAI (suporta ambos OpenAI e Azure OpenAI).

**ChatModel** - Interface principal para modelos de linguagem.

**ChatMemory** - Mantém o histórico da conversa.

**ContentRetriever** - Encontra pedaços relevantes de documentos para RAG.

**DocumentSplitter** - Divide documentos em pedaços.

**EmbeddingModel** - Converte texto em vetores numéricos.

**EmbeddingStore** - Armazena e recupera embeddings.

**MessageWindowChatMemory** - Mantém uma janela deslizante das mensagens recentes.

**PromptTemplate** - Cria prompts reutilizáveis com espaços reservados `{{variable}}`.

**TextSegment** - Pedaço de texto com metadados. Usado no RAG.

**ToolExecutionRequest** - Representa um pedido de execução de ferramenta.

**UserMessage / AiMessage / SystemMessage** - Tipos de mensagens na conversa.

## Conceitos AI/ML

**Few-Shot Learning** - Fornecimento de exemplos nos prompts. [Módulo 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - Modelos AI treinados em vastos dados textuais.

**Esforço de Raciocínio** - Parâmetro GPT-5.2 que controla a profundidade do pensamento. [Módulo 02](../02-prompt-engineering/README.md)

**Temperatura** - Controla aleatoriedade da saída. Baixa=determinístico, alta=criativo.

**Base de Dados Vetorial** - Base de dados especializada para embeddings. [Módulo 03](../03-rag/README.md)

**Zero-Shot Learning** - Realização de tarefas sem exemplos. [Módulo 02](../02-prompt-engineering/README.md)

## Guardrails - [Módulo 00](../00-quick-start/README.md)

**Defesa em Profundidade** - Abordagem de segurança em várias camadas combinando guardrails a nível de aplicação com filtros de segurança do fornecedor.

**Bloqueio Rígido** - O fornecedor lança erro HTTP 400 para violações graves de conteúdo.

**InputGuardrail** - Interface LangChain4j para validar entrada do utilizador antes de chegar ao LLM. Economiza custo e latência ao bloquear prompts nocivos precocemente.

**InputGuardrailResult** - Tipo de retorno para validação guardrail: `success()` ou `fatal("reason")`.

**OutputGuardrail** - Interface para validar respostas AI antes de devolver aos utilizadores.

**Filtros de Segurança do Fornecedor** - Filtros de conteúdo integrados pelos fornecedores AI (ex: GitHub Models) que detectam violações na API.

**Recusa Suave** - Modelo recusa educadamente responder sem lançar erro.

## Engenharia de Prompt - [Módulo 02](../02-prompt-engineering/README.md)

**Cadeia de Pensamento** - Raciocínio passo a passo para maior precisão.

**Saída Constrainida** - Aplicar formato ou estrutura específica.

**Alta Prontidão** - Padrão GPT-5.2 para raciocínio aprofundado.

**Baixa Prontidão** - Padrão GPT-5.2 para respostas rápidas.

**Conversa Multi-Turno** - Manter contexto através de trocas.

**Prompting Baseado em Papéis** - Definir persona do modelo via mensagens de sistema.

**Autorreflexão** - Modelo avalia e melhora a sua saída.

**Análise Estruturada** - Quadro de avaliação fixo.

**Padrão de Execução de Tarefa** - Planear → Executar → Resumir.

## RAG (Geração com Recuperação) - [Módulo 03](../03-rag/README.md)

**Pipeline de Processamento de Documento** - Carregar → dividir → embedar → armazenar.

**Armazenamento In-Memory de Embeddings** - Armazenamento não persistente para testes.

**RAG** - Combina recuperação com geração para fundamentar respostas.

**Pontuação de Similaridade** - Medida (0-1) de similaridade semântica.

**Referência de Fonte** - Metadados sobre conteúdo recuperado.

## Agentes e Ferramentas - [Módulo 04](../04-tools/README.md)

**Anotação @Tool** - Marca métodos Java como ferramentas invocáveis pela AI.

**Padrão ReAct** - Raciocinar → Agir → Observar → Repetir.

**Gestão de Sessão** - Contextos separados para utilizadores diferentes.

**Ferramenta** - Função que um agente AI pode chamar.

**Descrição da Ferramenta** - Documentação do propósito e parâmetros da ferramenta.

## Módulo Agente - [Módulo 05](../05-mcp/README.md)

**Anotação @Agent** - Marca interfaces como agentes AI com definição declarativa de comportamento.

**Agent Listener** - Gancho para monitorar execução do agente via `beforeAgentInvocation()` e `afterAgentInvocation()`.

**Escopo Agente** - Memória partilhada onde agentes armazenam saídas usando `outputKey` para agentes a jusante consumirem.

**AgenticServices** - Fábrica para criar agentes usando `agentBuilder()` e `supervisorBuilder()`.

**Workflow Condicional** - Roteia baseado em condições para diferentes agentes especialistas.

**Human-in-the-Loop** - Padrão de workflow que adiciona pontos de verificação humanos para aprovação ou revisão de conteúdo.

**langchain4j-agentic** - Dependência Maven para construção declarativa de agentes (experimental).

**Workflow em Loop** - Itera execução do agente até uma condição ser satisfeita (ex: pontuação de qualidade ≥ 0.8).

**outputKey** - Parâmetro de anotação do agente que especifica onde resultados são armazenados no Escopo Agente.

**Workflow Paralelo** - Executa vários agentes simultaneamente para tarefas independentes.

**Estratégia de Resposta** - Como o supervisor formula a resposta final: LAST, SUMMARY ou SCORED.

**Workflow Sequencial** - Executa agentes em ordem onde a saída flui para o passo seguinte.

**Padrão de Agente Supervisor** - Padrão agente avançado onde um LLM supervisor decide dinamicamente quais subagentes invocar.

## Protocolo de Contexto de Modelo (MCP) - [Módulo 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependência Maven para integração MCP no LangChain4j.

**MCP** - Protocolo de Contexto de Modelo: standard para conectar apps AI a ferramentas externas. Constrói uma vez, usa em todo lado.

**Cliente MCP** - Aplicação que se conecta a servidores MCP para descobrir e usar ferramentas.

**Servidor MCP** - Serviço que expõe ferramentas via MCP com descrições claras e esquemas de parâmetros.

**McpToolProvider** - Componente LangChain4j que embala ferramentas MCP para uso em serviços AI e agentes.

**McpTransport** - Interface para comunicação MCP. Implementações incluem Stdio e HTTP.

**Transporte Stdio** - Transporte local via processos stdin/stdout. Útil para acesso ao sistema de ficheiros ou ferramentas de linha de comandos.

**StdioMcpTransport** - Implementação LangChain4j que cria servidor MCP como subprocesso.

**Descoberta de Ferramentas** - Cliente consulta servidor por ferramentas disponíveis com descrições e esquemas.

## Serviços Azure - [Módulo 01](../01-introduction/README.md)

**Azure AI Search** - Pesquisa cloud com capacidades vetoriais. [Módulo 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Faz deploy de recursos Azure.

**Azure OpenAI** - Serviço AI empresarial da Microsoft.

**Bicep** - Linguagem Azure infrastructure-as-code. [Guia Infraestrutura](../01-introduction/infra/README.md)

**Nome do Deployment** - Nome para deploy de modelo no Azure.

**GPT-5.2** - Último modelo OpenAI com controlo de raciocínio. [Módulo 02](../02-prompt-engineering/README.md)

## Testes e Desenvolvimento - [Guia de Testes](TESTING.md)

**Dev Container** - Ambiente de desenvolvimento conteinerizado. [Configuração](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground gratuito de modelos AI. [Módulo 00](../00-quick-start/README.md)

**Testes In-Memory** - Testes com armazenamento em memória.

**Testes de Integração** - Testes com infraestrutura real.

**Maven** - Ferramenta de automação de build Java.

**Mockito** - Framework Java para mocking.

**Spring Boot** - Framework de aplicação Java. [Módulo 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso**:
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, por favor, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte oficial. Para informações críticas, recomenda-se a tradução profissional feita por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações erradas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->