# Glossário LangChain4j

## Índice

- [Conceitos Básicos](../../../docs)
- [Componentes LangChain4j](../../../docs)
- [Conceitos de AI/ML](../../../docs)
- [Guardrails](../../../docs)
- [Engenharia de Prompt](../../../docs)
- [RAG (Geração com Recuperação)](../../../docs)
- [Agentes e Ferramentas](../../../docs)
- [Módulo Agentic](../../../docs)
- [Protocolo de Contexto de Modelo (MCP)](../../../docs)
- [Serviços Azure](../../../docs)
- [Teste e Desenvolvimento](../../../docs)

Referência rápida para termos e conceitos usados ao longo do curso.

## Conceitos Básicos

**Agente de IA** - Sistema que usa IA para raciocinar e agir de forma autônoma. [Módulo 04](../04-tools/README.md)

**Cadeia** - Sequência de operações onde a saída alimenta a próxima etapa.

**Fragmentação** - Dividir documentos em partes menores. Típico: 300-500 tokens com sobreposição. [Módulo 03](../03-rag/README.md)

**Janela de Contexto** - Máximo de tokens que um modelo pode processar. GPT-5.2: 400K tokens (até 272K entrada, 128K saída).

**Embeddings** - Vetores numéricos que representam o significado do texto. [Módulo 03](../03-rag/README.md)

**Chamada de Função** - Modelo gera requisições estruturadas para chamar funções externas. [Módulo 04](../04-tools/README.md)

**Alucinação** - Quando modelos geram informações incorretas mas plausíveis.

**Prompt** - Entrada de texto para um modelo de linguagem. [Módulo 02](../02-prompt-engineering/README.md)

**Busca Semântica** - Busca por significado usando embeddings, não por palavras-chave. [Módulo 03](../03-rag/README.md)

**Sem Estado vs Com Estado** - Sem estado: sem memória. Com estado: mantém histórico da conversa. [Módulo 01](../01-introduction/README.md)

**Tokens** - Unidades básicas de texto que modelos processam. Afeta custos e limites. [Módulo 01](../01-introduction/README.md)

**Encadeamento de Ferramentas** - Execução sequencial de ferramentas onde a saída informa a próxima chamada. [Módulo 04](../04-tools/README.md)

## Componentes LangChain4j

**AiServices** - Cria interfaces de serviço de IA com tipagem segura.

**OpenAiOfficialChatModel** - Cliente unificado para modelos OpenAI e Azure OpenAI.

**OpenAiOfficialEmbeddingModel** - Cria embeddings usando cliente oficial OpenAI (suporta OpenAI e Azure OpenAI).

**ChatModel** - Interface central para modelos de linguagem.

**ChatMemory** - Mantém histórico da conversa.

**ContentRetriever** - Encontra fragmentos de documentos relevantes para RAG.

**DocumentSplitter** - Divide documentos em fragmentos.

**EmbeddingModel** - Converte texto em vetores numéricos.

**EmbeddingStore** - Armazena e recupera embeddings.

**MessageWindowChatMemory** - Mantém janela deslizante das mensagens recentes.

**PromptTemplate** - Cria prompts reutilizáveis com espaços reservados `{{variable}}`.

**TextSegment** - Fragmento de texto com metadados. Usado em RAG.

**ToolExecutionRequest** - Representa requisição de execução de ferramenta.

**UserMessage / AiMessage / SystemMessage** - Tipos de mensagens na conversa.

## Conceitos de AI/ML

**Few-Shot Learning** - Fornecer exemplos nos prompts. [Módulo 02](../02-prompt-engineering/README.md)

**Modelo de Linguagem Grande (LLM)** - Modelos de IA treinados com grandes volumes de texto.

**Esforço de Raciocínio** - Parâmetro do GPT-5.2 que controla profundidade do raciocínio. [Módulo 02](../02-prompt-engineering/README.md)

**Temperatura** - Controla a aleatoriedade da saída. Baixa=determinístico, alta=criativo.

**Banco de Dados Vetorial** - Banco especializado em embeddings. [Módulo 03](../03-rag/README.md)

**Zero-Shot Learning** - Realizar tarefas sem exemplos. [Módulo 02](../02-prompt-engineering/README.md)

## Guardrails - [Módulo 00](../00-quick-start/README.md)

**Defesa em Profundidade** - Abordagem de segurança multicamadas que combina guardrails no nível da aplicação com filtros de segurança do provedor.

**Bloqueio Rígido** - Provedor retorna erro HTTP 400 para violações graves de conteúdo.

**InputGuardrail** - Interface LangChain4j para validar entrada do usuário antes de chegar ao LLM. Economiza custo e latência bloqueando prompts nocivos cedo.

**InputGuardrailResult** - Tipo de retorno para validação guardrail: `success()` ou `fatal("razão")`.

**OutputGuardrail** - Interface para validar respostas de IA antes de retornar ao usuário.

**Filtros de Segurança do Provedor** - Filtros de conteúdo incorporados dos provedores de IA (ex.: GitHub Models) que detectam violações na API.

**Recusa Suave** - Modelo educadamente se recusa a responder sem gerar erro.

## Engenharia de Prompt - [Módulo 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Raciocínio passo a passo para melhor precisão.

**Saída Confinada** - Aplicar formato ou estrutura específicos.

**Alta Vontade** - Padrão GPT-5.2 para raciocínio aprofundado.

**Baixa Vontade** - Padrão GPT-5.2 para respostas rápidas.

**Conversação Multi-Turno** - Manter contexto entre trocas.

**Prompt por Papel** - Definir persona do modelo por mensagens do sistema.

**Autorreflexão** - Modelo avalia e melhora sua saída.

**Análise Estruturada** - Estrutura fixa de avaliação.

**Padrão de Execução de Tarefa** - Planejar → Executar → Resumir.

## RAG (Geração com Recuperação) - [Módulo 03](../03-rag/README.md)

**Pipeline de Processamento de Documento** - Carregar → fragmentar → embedar → armazenar.

**Armazenamento de Embeddings em Memória** - Armazenamento não persistente para testes.

**RAG** - Combina recuperação com geração para fundamentar respostas.

**Pontuação de Similaridade** - Medida (0-1) de similaridade semântica.

**Referência de Fonte** - Metadados sobre o conteúdo recuperado.

## Agentes e Ferramentas - [Módulo 04](../04-tools/README.md)

**Anotação @Tool** - Marca métodos Java como ferramentas chamáveis pela IA.

**Padrão ReAct** - Raciocinar → Agir → Observar → Repetir.

**Gerenciamento de Sessão** - Contextos separados para usuários diferentes.

**Ferramenta** - Função que um agente de IA pode chamar.

**Descrição da Ferramenta** - Documentação do propósito e parâmetros da ferramenta.

## Módulo Agentic - [Módulo 05](../05-mcp/README.md)

**Anotação @Agent** - Marca interfaces como agentes de IA com definição declarativa de comportamento.

**Agent Listener** - Hook para monitorar execução do agente via `beforeAgentInvocation()` e `afterAgentInvocation()`.

**Agentic Scope** - Memória compartilhada onde agentes armazenam saídas usando `outputKey` para consumo por agentes downstream.

**AgenticServices** - Fábrica para criar agentes usando `agentBuilder()` e `supervisorBuilder()`.

**Fluxo Condicional** - Direcionar com base em condições para agentes especialistas diferentes.

**Humano no Loop** - Padrão de fluxo com checkpoints humanos para aprovação ou revisão de conteúdo.

**langchain4j-agentic** - Dependência Maven para criação declarativa de agentes (experimental).

**Fluxo de Repetição** - Iterar execução de agente até condição ser atendida (ex.: pontuação de qualidade ≥ 0.8).

**outputKey** - Parâmetro da anotação do agente que especifica onde resultados são armazenados em Agentic Scope.

**Fluxo Paralelo** - Executar múltiplos agentes simultaneamente para tarefas independentes.

**Estratégia de Resposta** - Como o supervisor formula a resposta final: LAST, SUMMARY ou SCORED.

**Fluxo Sequencial** - Executar agentes em ordem onde saída flui para a próxima etapa.

**Padrão Supervisor Agent** - Padrão agentic avançado onde um LLM supervisor decide dinamicamente quais subagentes chamar.

## Protocolo de Contexto de Modelo (MCP) - [Módulo 05](../05-mcp/README.md)

**langchain4j-mcp** - Dependência Maven para integração MCP no LangChain4j.

**MCP** - Protocolo de Contexto de Modelo: padrão para conectar apps de IA a ferramentas externas. Construa uma vez, use em todo lugar.

**Cliente MCP** - Aplicação que conecta a servidores MCP para descobrir e usar ferramentas.

**Servidor MCP** - Serviço que expõe ferramentas via MCP com descrições claras e esquemas de parâmetros.

**McpToolProvider** - Componente LangChain4j que embrulha ferramentas MCP para uso em serviços e agentes de IA.

**McpTransport** - Interface para comunicação MCP. Implementações incluem Stdio e HTTP.

**Transporte Stdio** - Transporte local por meio de stdin/stdout. Útil para acesso ao sistema de arquivos ou ferramentas de linha de comando.

**StdioMcpTransport** - Implementação LangChain4j que inicia servidor MCP como subprocesso.

**Descoberta de Ferramentas** - Cliente consulta servidor por ferramentas disponíveis com descrições e esquemas.

## Serviços Azure - [Módulo 01](../01-introduction/README.md)

**Azure AI Search** - Busca na nuvem com capacidade vetorial. [Módulo 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Implanta recursos Azure.

**Azure OpenAI** - Serviço de IA empresarial da Microsoft.

**Bicep** - Linguagem de infraestrutura como código da Azure. [Guia de Infraestrutura](../01-introduction/infra/README.md)

**Nome da Implantação** - Nome para implantação do modelo no Azure.

**GPT-5.2** - Modelo OpenAI mais recente com controle de raciocínio. [Módulo 02](../02-prompt-engineering/README.md)

## Teste e Desenvolvimento - [Guia de Testes](TESTING.md)

**Dev Container** - Ambiente de desenvolvimento conteinerizado. [Configuração](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Playground gratuito de modelos de IA. [Módulo 00](../00-quick-start/README.md)

**Teste em Memória** - Teste com armazenamento em memória.

**Teste de Integração** - Teste com infraestrutura real.

**Maven** - Ferramenta de automação de build Java.

**Mockito** - Framework Java para mock.

**Spring Boot** - Framework de aplicação Java. [Módulo 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automáticas podem conter erros ou imprecisões. O documento original em sua língua nativa deve ser considerado a fonte autoritária. Para informações críticas, recomenda-se tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações incorretas decorrentes do uso desta tradução.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->