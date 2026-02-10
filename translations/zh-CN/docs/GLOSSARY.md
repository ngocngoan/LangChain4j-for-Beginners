# LangChain4j 术语表

## 目录

- [核心概念](../../../docs)
- [LangChain4j 组件](../../../docs)
- [AI/ML 概念](../../../docs)
- [Guardrails](../../../docs)
- [提示工程](../../../docs)
- [RAG（检索增强生成）](../../../docs)
- [代理和工具](../../../docs)
- [Agentic 模块](../../../docs)
- [模型上下文协议（MCP）](../../../docs)
- [Azure 服务](../../../docs)
- [测试与开发](../../../docs)

课程中使用的术语和概念快速参考。

## 核心概念

**AI 代理** - 使用 AI 进行推理和自主行动的系统。 [模块 04](../04-tools/README.md)

**链（Chain）** - 操作序列，输出作为下一步输入。

**分块（Chunking）** - 将文档拆分成更小的片段。典型大小：300-500 令牌，并带有重叠。 [模块 03](../03-rag/README.md)

**上下文窗口** - 模型可处理的最大令牌数。GPT-5.2：400K 令牌。

**嵌入（Embeddings）** - 表示文本含义的数值向量。 [模块 03](../03-rag/README.md)

**函数调用（Function Calling）** - 模型生成结构化请求以调用外部函数。 [模块 04](../04-tools/README.md)

**幻觉（Hallucination）** - 模型生成错误但看似合理的信息。

**提示（Prompt）** - 语言模型的文本输入。 [模块 02](../02-prompt-engineering/README.md)

**语义搜索（Semantic Search）** - 使用嵌入按含义搜索，而非关键词。 [模块 03](../03-rag/README.md)

**有状态与无状态** - 无状态：无记忆。有状态：维持对话历史。 [模块 01](../01-introduction/README.md)

**令牌（Tokens）** - 模型处理的基本文本单位。影响成本和限制。 [模块 01](../01-introduction/README.md)

**工具链（Tool Chaining）** - 顺序工具执行，输出影响下一次调用。 [模块 04](../04-tools/README.md)

## LangChain4j 组件

**AiServices** - 创建类型安全的 AI 服务接口。

**OpenAiOfficialChatModel** - OpenAI 和 Azure OpenAI 模型的统一客户端。

**OpenAiOfficialEmbeddingModel** - 使用 OpenAI 官方客户端创建嵌入（支持 OpenAI 和 Azure OpenAI）。

**ChatModel** - 语言模型的核心接口。

**ChatMemory** - 维护对话历史。

**ContentRetriever** - 查找相关文档块以供 RAG 使用。

**DocumentSplitter** - 将文档拆分成块。

**EmbeddingModel** - 将文本转换为数值向量。

**EmbeddingStore** - 存储和检索嵌入。

**MessageWindowChatMemory** - 维护最近消息的滑动窗口。

**PromptTemplate** - 使用 `{{variable}}` 占位符创建可重用提示。

**TextSegment** - 带元数据的文本块。用于 RAG。

**ToolExecutionRequest** - 表示工具执行请求。

**UserMessage / AiMessage / SystemMessage** - 对话消息类型。

## AI/ML 概念

**少样本学习（Few-Shot Learning）** - 在提示中提供示例。 [模块 02](../02-prompt-engineering/README.md)

**大型语言模型（LLM）** - 基于大量文本数据训练的 AI 模型。

**推理力度（Reasoning Effort）** - GPT-5.2 参数，控制思考深度。 [模块 02](../02-prompt-engineering/README.md)

**温度（Temperature）** - 控制输出随机性。低=确定性，高=创造性。

**向量数据库（Vector Database）** - 专门存储嵌入的数据库。 [模块 03](../03-rag/README.md)

**零样本学习（Zero-Shot Learning）** - 无需示例执行任务。 [模块 02](../02-prompt-engineering/README.md)

## Guardrails - [模块 00](../00-quick-start/README.md)

**纵深防御（Defense in Depth）** - 多层安全方法，结合应用层 Guardrails 和供应商安全过滤器。

**硬阻断（Hard Block）** - 供应商对严重内容违规返回 HTTP 400 错误。

**输入 Guardrail（InputGuardrail）** - LangChain4j 接口，验证用户输入在到达 LLM 前，节省成本和延迟，提前拦截有害提示。

**输入 Guardrail 结果（InputGuardrailResult）** - Guardrail 验证的返回类型：`success()` 或 `fatal("reason")`。

**输出 Guardrail（OutputGuardrail）** - 验证 AI 响应后再返回用户的接口。

**供应商安全过滤器** - AI 供应商内置的内容过滤器（如 GitHub Models），在 API 层捕获违规。

**软拒绝（Soft Refusal）** - 模型礼貌拒绝回答，但不报错。

## 提示工程 - [模块 02](../02-prompt-engineering/README.md)

**链式思考（Chain-of-Thought）** - 逐步推理以提高准确性。

**受限输出（Constrained Output）** - 强制特定格式或结构。

**高推理力度（High Eagerness）** - GPT-5.2 模式，全面推理。

**低推理力度（Low Eagerness）** - GPT-5.2 模式，快速回答。

**多轮对话（Multi-Turn Conversation）** - 在交流中保持上下文。

**基于角色的提示（Role-Based Prompting）** - 通过系统消息设置模型角色。

**自我反思（Self-Reflection）** - 模型评估并改进输出。

**结构化分析（Structured Analysis）** - 固定的评估框架。

**任务执行模式（Task Execution Pattern）** - 计划 → 执行 → 总结。

## RAG（检索增强生成） - [模块 03](../03-rag/README.md)

**文档处理流程** - 加载 → 分块 → 嵌入 → 存储。

**内存嵌入存储** - 非持久存储用于测试。

**RAG** - 结合检索和生成，使回答基础更可靠。

**相似度分数** - 语义相似度的度量（0-1）。

**来源引用** - 关于检索内容的元数据。

## 代理和工具 - [模块 04](../04-tools/README.md)

**@Tool 注解** - 标记 Java 方法为可由 AI 调用的工具。

**ReAct 模式** - 推理 → 行动 → 观察 → 重复。

**会话管理** - 为不同用户分离上下文。

**工具（Tool）** - AI 代理可以调用的函数。

**工具描述** - 说明工具目的和参数。

## Agentic 模块 - [模块 05](../05-mcp/README.md)

**@Agent 注解** - 标记接口为 AI 代理，使用声明式行为定义。

**代理监听器（Agent Listener）** - 通过 `beforeAgentInvocation()` 和 `afterAgentInvocation()` 监控代理执行的钩子。

**Agentic 范围** - 共享内存，代理使用 `outputKey` 存储输出，供下游代理使用。

**AgenticServices** - 使用 `agentBuilder()` 和 `supervisorBuilder()` 创建代理的工厂。

**条件工作流（Conditional Workflow）** - 根据条件路由到不同的专家代理。

**人机协同（Human-in-the-Loop）** - 工作流模式，添加人工检查点以审批或审查内容。

**langchain4j-agentic** - 用于声明式代理构建的 Maven 依赖（实验性）。

**循环工作流（Loop Workflow）** - 迭代执行代理直到满足条件（如质量分数 ≥ 0.8）。

**outputKey** - 代理注解参数，指定结果存储于 Agentic 范围中的位置。

**并行工作流（Parallel Workflow）** - 同时运行多个代理以处理独立任务。

**响应策略（Response Strategy）** - 监督者如何形成最终答案：LAST、SUMMARY 或 SCORED。

**顺序工作流（Sequential Workflow）** - 按顺序执行代理，输出传递到下一步。

**监督代理模式（Supervisor Agent Pattern）** - 高级 Agentic 模式，监督者 LLM 动态决定调用哪些子代理。

## 模型上下文协议（MCP） - [模块 05](../05-mcp/README.md)

**langchain4j-mcp** - 用于 LangChain4j 中 MCP 集成的 Maven 依赖。

**MCP** - 模型上下文协议：连接 AI 应用与外部工具的标准，构建一次，到处使用。

**MCP 客户端** - 连接 MCP 服务器，发现并使用工具的应用程序。

**MCP 服务器** - 通过 MCP 对外提供工具，带有清晰描述和参数模式的服务。

**McpToolProvider** - LangChain4j 组件，封装 MCP 工具以在 AI 服务和代理中使用。

**McpTransport** - MCP 通信接口。实现包括 Stdio 和 HTTP。

**Stdio 传输** - 通过 stdin/stdout 的本地进程传输。适用于文件系统访问或命令行工具。

**StdioMcpTransport** - LangChain4j 实现，作为子进程启动 MCP 服务器。

**工具发现（Tool Discovery）** - 客户端查询服务器获取可用工具及描述和模式。

## Azure 服务 - [模块 01](../01-introduction/README.md)

**Azure AI 搜索** - 向量功能的云搜索服务。 [模块 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - 部署 Azure 资源的命令行工具。

**Azure OpenAI** - 微软的企业 AI 服务。

**Bicep** - Azure 基础设施即代码语言。 [基础设施指南](../01-introduction/infra/README.md)

**部署名称** - Azure 中模型部署的名称。

**GPT-5.2** - 具备推理控制的最新 OpenAI 模型。 [模块 02](../02-prompt-engineering/README.md)

## 测试与开发 - [测试指南](TESTING.md)

**开发容器（Dev Container）** - 容器化开发环境。 [配置](../../../.devcontainer/devcontainer.json)

**GitHub 模型** - 免费的 AI 模型试玩场。 [模块 00](../00-quick-start/README.md)

**内存测试** - 使用内存存储进行测试。

**集成测试** - 使用真实基础设施的测试。

**Maven** - Java 构建自动化工具。

**Mockito** - Java 模拟框架。

**Spring Boot** - Java 应用框架。 [模块 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文档通过 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。虽然我们力求准确，但请注意自动翻译可能存在错误或不准确之处。请以原始语言的文档为权威版本。对于重要信息，建议采用专业人工翻译。我们不对因使用本翻译而产生的任何误解或错误解释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->