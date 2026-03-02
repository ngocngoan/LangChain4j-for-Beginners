# LangChain4j Glossary

## Table of Contents

- [Core Concepts](../../../docs)
- [LangChain4j Components](../../../docs)
- [AI/ML Concepts](../../../docs)
- [Guardrails](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agents and Tools](../../../docs)
- [Agentic Module](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure Services](../../../docs)
- [Testing and Development](../../../docs)

Quick reference for terms and concepts wey dem dey use for di whole course.

## Core Concepts

**AI Agent** - System wey dey use AI to reason and to act on e own. [Module 04](../04-tools/README.md)

**Chain** - Sequence of operations wey di output go enter di next step.

**Chunking** - Breaking documents into small small pieces. Typical: 300-500 tokens wit overlap. [Module 03](../03-rag/README.md)

**Context Window** - Maximum tokens wey model fit process. GPT-5.2: 400K tokens (up to 272K input, 128K output).

**Embeddings** - Number vectors wey represent wetin text mean. [Module 03](../03-rag/README.md)

**Function Calling** - Model dey generate structured requests to call external functions. [Module 04](../04-tools/README.md)

**Hallucination** - When models dem generate wrong but e fit look like correct information.

**Prompt** - Text wey you enter for language model. [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - Search by meaning using embeddings, no be keywords. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: no memory. Stateful: e dey remember conversation history. [Module 01](../01-introduction/README.md)

**Tokens** - Basic text units wey models dey process. E affect cost and limits. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Sequential tool action wey output dey inform next call. [Module 04](../04-tools/README.md)

## LangChain4j Components

**AiServices** - E dey create type-safe AI service interfaces.

**OpenAiOfficialChatModel** - Unified client for OpenAI and Azure OpenAI models.

**OpenAiOfficialEmbeddingModel** - E dey create embeddings using OpenAI Official client (e support both OpenAI and Azure OpenAI).

**ChatModel** - Core interface for language models.

**ChatMemory** - E dey maintain conversation history.

**ContentRetriever** - E dey find relevant document chunks for RAG.

**DocumentSplitter** - E dey break documents into chunks.

**EmbeddingModel** - E dey convert text to number vectors.

**EmbeddingStore** - E dey store and retrieve embeddings.

**MessageWindowChatMemory** - E dey maintain sliding window of recent messages.

**PromptTemplate** - E dey create reusable prompts wit `{{variable}}` placeholders.

**TextSegment** - Text chunk wit metadata. Dem dey use for RAG.

**ToolExecutionRequest** - E represent tool execution request.

**UserMessage / AiMessage / SystemMessage** - Conversation message types.

## AI/ML Concepts

**Few-Shot Learning** - E mean say you go provide examples inside prompts. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI models wey dem train on plenty text data.

**Reasoning Effort** - GPT-5.2 parameter wey e control how deep di thinkin be. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - E dey control wetin output randomness go be. Low=deterministic, high=creative.

**Vector Database** - Special database for embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - E mean say you no need give example before dem perform task. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Multi-layer security approach wey combine application-level guardrails wit provider safety filters.

**Hard Block** - Provider dey throw HTTP 400 error if content violation strong like dat.

**InputGuardrail** - LangChain4j interface wey dey check user input before e reach LLM. Na so e save cost and time by blocking bad prompts early.

**InputGuardrailResult** - E mean type wey guardrail validation go return: `success()` or `fatal("reason")`.

**OutputGuardrail** - Interface wey dem dey check AI response before dem return to users.

**Provider Safety Filters** - Built-in content filters from AI providers (like GitHub Models) wey fit catch violations for API level.

**Soft Refusal** - Model dey politely talk say e no go fit answer without throwing error.

## Prompt Engineering - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Step by step reasoning for better accuracy.

**Constrained Output** - To force output follow particular format or structure.

**High Eagerness** - GPT-5.2 pattern wey e dey reason well well.

**Low Eagerness** - GPT-5.2 pattern wey e quick answer.

**Multi-Turn Conversation** - E mean maintaining context for every exchange.

**Role-Based Prompting** - To set model personality through system messages.

**Self-Reflection** - Model go check and improve e own output.

**Structured Analysis** - Fixed framework for evaluation.

**Task Execution Pattern** - Plan → Execute → Summarize.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - Load → chunk → embed → store.

**In-Memory Embedding Store** - Storage wey no permanent for testing.

**RAG** - E combine retrieval with generation to give grounded response.

**Similarity Score** - Measure (0-1) of how semantic similarity e be.

**Source Reference** - Metadata about content wey dem retrieve.

## Agents and Tools - [Module 04](../04-tools/README.md)

**@Tool Annotation** - E dey mark Java methods as AI-callable tools.

**ReAct Pattern** - Reason → Act → Observe → Repeat.

**Session Management** - Separate context for different users.

**Tool** - Function wey AI agent fit call.

**Tool Description** - Documentation of tool purpose and parameters.

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - E dey mark interfaces as AI agents wit declarative behavior definition.

**Agent Listener** - Hook wey e dey monitor agent execution through `beforeAgentInvocation()` and `afterAgentInvocation()`.

**Agentic Scope** - Shared memory wey agents dey store output wit `outputKey` for other agents to use.

**AgenticServices** - Factory wey e dey create agents using `agentBuilder()` and `supervisorBuilder()`.

**Conditional Workflow** - Route wey e depend on conditions to different specialist agents.

**Human-in-the-Loop** - Workflow pattern wey add human checkpoints for approval or content review.

**langchain4j-agentic** - Maven dependency for declarative agent building (experimental).

**Loop Workflow** - E mean to keep iterate agent execution until condition done (e.g., quality score ≥ 0.8).

**outputKey** - Agent annotation parameter wey e tell where results go dey store inside Agentic Scope.

**Parallel Workflow** - Run many agents at once for independent tasks.

**Response Strategy** - How supervisor go put final answer together: LAST, SUMMARY, or SCORED.

**Sequential Workflow** - E mean to execute agents in order where output dey flow to next step.

**Supervisor Agent Pattern** - Advanced agentic pattern wey supervisor LLM dey decide dynamically which sub-agents to call.

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven dependency for MCP integration inside LangChain4j.

**MCP** - Model Context Protocol: na standard wey dey connect AI apps to external tools. Build once, fit use everywhere.

**MCP Client** - Application wey dey connect to MCP servers to find and use tools.

**MCP Server** - Service wey dey expose tools via MCP wit clear descriptions and parameter schemas.

**McpToolProvider** - LangChain4j component wey dey wrap MCP tools so AI services and agents fit use am.

**McpTransport** - Interface for MCP communication. E get implementation like Stdio and HTTP.

**Stdio Transport** - Local process transport via stdin/stdout. E dey useful for filesystem access or command-line tools.

**StdioMcpTransport** - LangChain4j implementation wey dey spawn MCP server as subprocess.

**Tool Discovery** - Client dey ask server for tools wey dey available wit descriptions and schemas.

## Azure Services - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Cloud search wey get vector capabilities. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - E deploy Azure resources.

**Azure OpenAI** - Microsoft enterprise AI service.

**Bicep** - Azure infrastructure-as-code language. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Name wey you go give model deployment for Azure.

**GPT-5.2** - Latest OpenAI model wey reason plenti. [Module 02](../02-prompt-engineering/README.md)

## Testing and Development - [Testing Guide](TESTING.md)

**Dev Container** - Containerized development environment. [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Free AI model playground. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Testing wit in-memory storage.

**Integration Testing** - Testing wit real infrastructure.

**Maven** - Java build automation tool.

**Mockito** - Java mocking framework.

**Spring Boot** - Java application framework. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:  
Dis document don translate wit AI translation service wey dem dey call [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we dey try make am correct, abeg sabi say automated translation fit get some errors or wahala. Di original document wey e dey in im own language na im be di correct source. If na serious mata, e beta make person wey sabi translate am well well do am for you. We no go take any blame if pesin misunderstand or use dis translation wrongly.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->