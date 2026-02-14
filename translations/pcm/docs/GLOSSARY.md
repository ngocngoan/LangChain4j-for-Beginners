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

Quick reference for terms and concepts wey dem dey use all through di course.

## Core Concepts

**AI Agent** - System wey dey use AI reason and act on im own. [Module 04](../04-tools/README.md)

**Chain** - Sequence of operations wey output dey feed into di next step.

**Chunking** - To break documents into small small pieces dem. Normal: 300-500 tokens with overlap. [Module 03](../03-rag/README.md)

**Context Window** - Maximum tokens wey model fit process. GPT-5.2: 400K tokens.

**Embeddings** - Number vectors wey represent di meaning of text. [Module 03](../03-rag/README.md)

**Function Calling** - Model dey generate structured requests to call outside functions. [Module 04](../04-tools/README.md)

**Hallucination** - When models dey generate wrong but plausible info.

**Prompt** - Text wey dem input give language model. [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - Search based on meaning by using embeddings, no be keywords. [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: no memory. Stateful: e dey keep conversation history. [Module 01](../01-introduction/README.md)

**Tokens** - Basic text units wey models dey process. E dey affect cost and limits. [Module 01](../01-introduction/README.md)

**Tool Chaining** - Tool dey run one after another where output dey inform next call. [Module 04](../04-tools/README.md)

## LangChain4j Components

**AiServices** - E dey create type-safe AI service interfaces.

**OpenAiOfficialChatModel** - Unified client for OpenAI and Azure OpenAI models.

**OpenAiOfficialEmbeddingModel** - E dey create embeddings by using OpenAI Official client (e support both OpenAI and Azure OpenAI).

**ChatModel** - Core interface for language models.

**ChatMemory** - E dey keep conversation history.

**ContentRetriever** - E dey find relevant document chunks for RAG.

**DocumentSplitter** - E dey break documents into chunks.

**EmbeddingModel** - E dey convert text into number vectors.

**EmbeddingStore** - E store and retrieve embeddings.

**MessageWindowChatMemory** - E dey maintain sliding window of recent messages.

**PromptTemplate** - E dey create reusable prompts with `{{variable}}` placeholders.

**TextSegment** - Text chunk wey get metadata. E dey used for RAG.

**ToolExecutionRequest** - E represent tool execution request.

**UserMessage / AiMessage / SystemMessage** - Types of conversation message.

## AI/ML Concepts

**Few-Shot Learning** - E mean say you provide examples for prompts. [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - AI models wey dem train for plenty text data.

**Reasoning Effort** - GPT-5.2 parameter wey dey control how deep e go reason. [Module 02](../02-prompt-engineering/README.md)

**Temperature** - E dey control how random output go be. Low=deterministic, high=creative.

**Vector Database** - Special database for embeddings. [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - E mean say e fit do task without example. [Module 02](../02-prompt-engineering/README.md)

## Guardrails - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - Multi-layer security approach wey combine application-level guardrails with provider safety filters.

**Hard Block** - Provider go throw HTTP 400 error for bad bad content violation.

**InputGuardrail** - LangChain4j interface wey dey validate user input before e reach LLM. E save cost and reduce latency by blocking harmful prompts early.

**InputGuardrailResult** - Return type for guardrail validation: `success()` or `fatal("reason")`.

**OutputGuardrail** - Interface wey dey validate AI responses before e go users.

**Provider Safety Filters** - Built-in content filters from AI providers (e.g., GitHub Models) wey dey catch violations for API level.

**Soft Refusal** - Model dey politely refuse to answer without throwing error.

## Prompt Engineering - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - Step by step reasoning for better accuracy.

**Constrained Output** - Enforce specific format or structure.

**High Eagerness** - GPT-5.2 pattern for thorough reasoning.

**Low Eagerness** - GPT-5.2 pattern for quick answers.

**Multi-Turn Conversation** - E mean say e dey keep context across exchanges.

**Role-Based Prompting** - E mean say you set model persona through system messages.

**Self-Reflection** - Model dey evaluate and improve im own output.

**Structured Analysis** - Fixed evaluation framework.

**Task Execution Pattern** - Plan → Execute → Summarize.

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - Load → chunk → embed → store.

**In-Memory Embedding Store** - Storage wey no dey permanent for testing.

**RAG** - E mean say e combine retrieval with generation to ground responses.

**Similarity Score** - Measure (0-1) wey show semantic similarity.

**Source Reference** - Metadata about retrieved content.

## Agents and Tools - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Marks Java methods as AI-callable tools.

**ReAct Pattern** - Reason → Act → Observe → Repeat.

**Session Management** - Separate contexts for different users.

**Tool** - Function an AI agent fit call.

**Tool Description** - Documentation of tool purpose and parameters.

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - Marks interfaces as AI agents with declarative behavior definition.

**Agent Listener** - Hook for monitoring agent execution through `beforeAgentInvocation()` and `afterAgentInvocation()`.

**Agentic Scope** - Shared memory where agents dey store outputs using `outputKey` for other agents wey follow to use.

**AgenticServices** - Factory for creating agents by using `agentBuilder()` and `supervisorBuilder()`.

**Conditional Workflow** - Route based on condition to different specialist agents.

**Human-in-the-Loop** - Workflow pattern wey add human checkpoint for approval or content review.

**langchain4j-agentic** - Maven dependency for declarative agent building (experimental).

**Loop Workflow** - Make agent run repeat until condition meet (e.g., quality score ≥ 0.8).

**outputKey** - Agent annotation parameter wey dey specify where results go store for Agentic Scope.

**Parallel Workflow** - Run multiple agents at di same time for independent tasks.

**Response Strategy** - How supervisor go formulate final answer: LAST, SUMMARY, or SCORED.

**Sequential Workflow** - Run agents one by one where output dey flow go next step.

**Supervisor Agent Pattern** - Advanced agentic pattern wey supervisor LLM dey dynamically decide which sub-agents to use.

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - Maven dependency for MCP integration inside LangChain4j.

**MCP** - Model Context Protocol: standard way to connect AI apps to outside tools. Build once, use everywhere.

**MCP Client** - Application wey connect to MCP servers to find and use tools.

**MCP Server** - Service wey expose tools via MCP with clear descriptions and parameter schemas.

**McpToolProvider** - LangChain4j component wey wrap MCP tools for AI services and agents use.

**McpTransport** - Interface for MCP communication. Implementations include Stdio and HTTP.

**Stdio Transport** - Local process transport wey use stdin/stdout. Good for filesystem access or command-line tools.

**StdioMcpTransport** - LangChain4j implementation wey run MCP server as subprocess.

**Tool Discovery** - Client dey ask server for tools wey dey available with descriptions and schemas.

## Azure Services - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Cloud search wey get vector capabilities. [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - E dey deploy Azure resources.

**Azure OpenAI** - Microsoft enterprise AI service.

**Bicep** - Azure infrastructure-as-code language. [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Name wey you go use for model deployment inside Azure.

**GPT-5.2** - Di latest OpenAI model wey get reasoning control. [Module 02](../02-prompt-engineering/README.md)

## Testing and Development - [Testing Guide](TESTING.md)

**Dev Container** - Containerized development environment. [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - Free AI model playground. [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - Testing wey use in-memory storage.

**Integration Testing** - Testing wey use real infrastructure.

**Maven** - Java build automation tool.

**Mockito** - Java mocking framework.

**Spring Boot** - Java application framework. [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document na translation wey AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) help do. Even though we dey try make am correct well well, make you still sabi say automated translation fit get some mistakes or no too correct. Di original document wey e dey for im own language na di correct one. If na serious matter, e better make professional human translator do am. We no go take responsibility if person no understand well or interpret am wrong because of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->