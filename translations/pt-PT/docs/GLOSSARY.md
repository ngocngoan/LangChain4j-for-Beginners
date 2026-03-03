# Glossário LangChain4j

## Índice

- [Conceitos Principais](../../../docs)
- [Componentes LangChain4j](../../../docs)
- [Conceitos de IA/ML](../../../docs)
- [Guardrails](../../../docs)
- [Engenharia de Prompt](../../../docs)
- [RAG (Geração Aumentada por Recuperação)](../../../docs)
- [Agentes e Ferramentas](../../../docs)
- [Módulo Agente](../../../docs)
- [Protocolo de Contexto do Modelo (MCP)](../../../docs)
- [Serviços Azure](../../../docs)
- [Testes e Desenvolvimento](../../../docs)

Referência rápida para termos e conceitos usados ao longo do curso.

## Conceitos Principais

**Agente de IA** - Sistema que usa IA para raciocinar e agir autonomamente. [Módulo 04](../04-tools/README.md)

**Cadeia** - Sequência de operações onde a saída alimenta o passo seguinte.

**Divisão em Pedaços (Chunking)** - Dividir documentos em partes menores. Típico: 300-500 tokens com sobreposição. [Módulo 03](../03-rag/README.md)

**Janela de Contexto** - Número máximo de tokens que um modelo pode processar. GPT-5.2: 400K tokens (até 272K de entrada, 128K de saída).

**Embeddings** - Vetores numéricos que representam o significado do texto. [Módulo 03](../03-rag/README.md)

**Chamada de Função** - Modelo gera pedidos estruturados para chamar funções externas. [Módulo 04](../04-tools/README.md)

**Alucinação** - Quando modelos geram informação incorreta mas plausível.

**Prompt** - Texto de entrada para um modelo de linguagem. [Módulo 02](../02-prompt-engineering/README.md)

**Pesquisa Semântica** - Pesquisa pelo significado usando embeddings, não palavras-chave. [Módulo 03](../03-rag/README.md)

**Com Estado vs Sem Estado** - Sem estado: sem memória. Com estado: mantém histórico da conversa. [Módulo 01](../01-introduction/README.md)

**Tokens** - Unidades básicas de texto que os modelos processam. Afetam custos e limites. [Módulo 01](../01-introduction/README.md)

**Encadeamento de Ferramentas** - Execução sequencial de ferramentas onde a saída informa a chamada seguinte. [Módulo 04](../04-tools/README.md)

## Componentes LangChain4j

**AiServices** - Cria interfaces de serviço de IA com segurança de tipos.

**OpenAiOfficialChatModel** - Cliente unificado para os modelos OpenAI e Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Cria embeddings usando o cliente oficial OpenAI (suporta OpenAI e Azure OpenAI).

**ChatModel** - Interface central para modelos de linguagem.

**ChatMemory** - Mantém histórico da conversa.

**ContentRetriever** - Encontra pedaços relevantes de documentos para RAG.

**DocumentSplitter** - Divide documentos em pedaços.

**EmbeddingModel** - Converte texto em vetores numéricos.

**EmbeddingStore** - Armazena e recupera embeddings.

**MessageWindowChatMemory** - Mantém janela deslizante das mensagens recentes.

**PromptTemplate** - Cria prompts reutilizáveis com espaços reservados `{{variable}}`.

**TextSegment** - Pedaço de texto com metadados. Usado no RAG.

**ToolExecutionRequest** - Representa pedido de execução de ferramenta.

**UserMessage / AiMessage / SystemMessage** - Tipos de mensagens na conversa.

## Conceitos de IA/ML

**Few-Shot Learning** - Fornecer exemplos nos prompts. [Módulo 02](../02-prompt-engineering/README.md)

**Modelo de Linguagem Grande (LLM)** - Modelos de IA treinados com grandes volumes de texto.

**Esforço de Raciocínio** - Parâmetro do GPT-5.2 que controla profundidade de pensamento. [Módulo 02](../02-prompt-engineering/README.md)

**Temperatura** - Controla aleatoriedade da saída. Baixa=determinístico, alta=criativo.

**Base de Dados Vetorial** - Base de dados especializada para embeddings. [Módulo 03](../03-rag/README.md)

**Zero-Shot Learning** - Realizar tarefas sem exemplos. [Módulo 02](../02-prompt-engineering/README.md)

## Guardrails - [Módulo 00](../00-quick-start/README.md)

**Defesa em Profundidade** - Abordagem multi-camadas de segurança combinando guardrails a nível de aplicação com filtros de segurança do fornecedor.

**Bloqueio Rígido** - Fornecedor retorna erro HTTP 400 para violações graves de conteúdo.

**InputGuardrail** - Interface LangChain4j para validar input do utilizador antes de atingir o LLM. Economiza custo e latência bloqueando prompts prejudiciais cedo.

**InputGuardrailResult** - Tipo de retorno para validação de guardrail: `success()` ou `fatal("reason")`.

**OutputGuardrail** - Interface para validar respostas de IA antes de devolver aos utilizadores.

**Filtros de Segurança do Fornecedor** - Filtros integrados de conteúdo dos fornecedores de IA (ex.: GitHub Models) que detectam violações a nível de API.

**Recusa Suave** - Modelo educadamente recusa responder sem gerar erro.

## Engenharia de Prompt - [Módulo 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Raciocínio passo a passo para maior precisão.

**Saída Constrangida** - Imposição de formato ou estrutura específicos.

**Alta Diligência** - Padrão GPT-5.2 para raciocínio detalhado.

**Baixa Diligência** - Padrão GPT-5.2 para respostas rápidas.

**Conversação Multi-Turno** - Manutenção de contexto ao longo das trocas.

**Prompting Baseado em Papéis** - Definição da persona do modelo via mensagens do sistema.

**Auto-Reflexão** - Modelo avalia e melhora a sua própria saída.

**Análise Estruturada** - Quadro fixo de avaliação.

**Padrão de Execução de Tarefa** - Planear → Executar → Resumir.

## RAG (Geração Aumentada por Recuperação) - [Módulo 03](../03-rag/README.md)

**Pipeline de Processamento de Documentos** - Carregar → dividir → embedar → armazenar.

**Armazenamento de Embeddings em Memória** - Armazenamento não persistente para testes.

**RAG** - Combina recuperação com geração para fundamentar respostas.

**Pontuação de Similaridade** - Medida (0-1) de similaridade semântica.

**Referência de Fonte** - Metadados sobre conteúdo recuperado.

## Agentes e Ferramentas - [Módulo 04](../04-tools/README.md)

**Anotação @Tool** - Marca métodos Java como ferramentas acessíveis por IA.

**Padrão ReAct** - Raciocinar → Agir → Observar → Repetir.

**Gestão de Sessão** - Contextos separados para utilizadores diferentes.

**Ferramenta** - Função que um agente IA pode chamar.

**Descrição da Ferramenta** - Documentação do propósito e parâmetros da ferramenta.

## Módulo Agente - [Módulo 05](../05-mcp/README.md)

**Anotação @Agent** - Marca interfaces como agentes de IA com definição declarativa de comportamento.

**Agent Listener** - Gancho para monitorizar execução do agente via `beforeAgentInvocation()` e `afterAgentInvocation()`.

**Agentic Scope** - Memória partilhada onde agentes guardam resultados usando `outputKey` para agentes seguintes consumirem.

**AgenticServices** - Fábrica para criar agentes usando `agentBuilder()` e `supervisorBuilder()`.

**Workflow Condicional** - Roteamento baseado em condições para diferentes agentes especializados.

**Human-in-the-Loop** - Padrão de workflow que adiciona pontos de controlo humanos para aprovação ou revisão de conteúdo.

**langchain4j-agentic** - Dependência Maven para construção declarativa de agentes (experimental).

**Loop Workflow** - Iterar a execução do agente até uma condição ser cumprida (ex.: pontuação de qualidade ≥ 0.8).

**outputKey** - Parâmetro de anotação do agente que especifica onde armazenar resultados no Agentic Scope.

**Parallel Workflow** - Executar vários agentes simultaneamente para tarefas independentes.

**Estratégia de Resposta** - Como o supervisor formula a resposta final: LAST, SUMMARY, ou SCORED.

**Sequential Workflow** - Executar agentes em ordem, onde a saída flui para o passo seguinte.

**Padrão de Agente Supervisor** - Padrão agente avançado onde um supervisor LLM decide dinamicamente quais sub-agentes invocar.

## Protocolo de Contexto do Modelo (MCP) - [Módulo 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependência Maven para integração MCP em LangChain4j.

**MCP** - Protocolo de Contexto do Modelo: padrão para ligação de apps de IA a ferramentas externas. Construir uma vez, usar em todo o lado.

**Cliente MCP** - Aplicação que conecta a servidores MCP para descobrir e usar ferramentas.

**Servidor MCP** - Serviço que expõe ferramentas via MCP com descrições claras e esquemas de parâmetros.

**McpToolProvider** - Componente LangChain4j que embrulha ferramentas MCP para uso em serviços IA e agentes.

**McpTransport** - Interface para comunicação MCP. Implementações incluem Stdio e HTTP.

**Transporte Stdio** - Transporte local via stdin/stdout. Útil para acesso ao sistema de ficheiros ou ferramentas linha de comando.

**StdioMcpTransport** - Implementação LangChain4j que inicia servidor MCP como subprocesso.

**Descoberta de Ferramentas** - Cliente consulta servidor para ferramentas disponíveis com descrições e esquemas.

## Serviços Azure - [Módulo 01](../01-introduction/README.md)

**Azure AI Search** - Pesquisa na cloud com capacidades vetoriais. [Módulo 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Implementa recursos Azure.

**Azure OpenAI** - Serviço empresarial de IA da Microsoft.

**Bicep** - Linguagem Azure de infra-estrutura como código. [Guia de Infraestrutura](../01-introduction/infra/README.md)

**Nome da Implementação** - Nome para a implementação do modelo no Azure.

**GPT-5.2** - Último modelo OpenAI com controlo de raciocínio. [Módulo 02](../02-prompt-engineering/README.md)

## Testes e Desenvolvimento - [Guia de Testes](TESTING.md)

**Dev Container** - Ambiente de desenvolvimento containerizado. [Configuração](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground gratuito de modelos IA. [Módulo 00](../00-quick-start/README.md)

**Testes em Memória** - Testes com armazenamento em memória.

**Testes de Integração** - Testes com infraestrutura real.

**Maven** - Ferramenta de automação de build Java.

**Mockito** - Framework de mocking Java.

**Spring Boot** - Framework de aplicações Java. [Módulo 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:
Este documento foi traduzido utilizando o serviço de tradução automática [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos pela precisão, por favor tenha em conta que traduções automáticas podem conter erros ou imprecisões. O documento original na sua língua nativa deve ser considerado a fonte autorizada. Para informação crítica, recomenda-se a tradução profissional efetuada por um humano. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes da utilização desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->