# LangChain4j 词汇表

## 目录

- [核心概念](../../../docs)
- [LangChain4j 组件](../../../docs)
- [AI/ML 概念](../../../docs)
- [防护措施](../../../docs)
- [提示工程](../../../docs)
- [RAG（检索增强生成）](../../../docs)
- [代理与工具](../../../docs)
- [Agentic 模块](../../../docs)
- [模型上下文协议（MCP）](../../../docs)
- [Azure 服务](../../../docs)
- [测试与开发](../../../docs)

课程中使用的术语和概念快速参考。

## 核心概念

**AI 代理** - 使用 AI 进行推理和自主行动的系统。 [模块 04](../04-tools/README.md)

**链** - 一系列操作，输出作为下一步的输入。

**分块** - 将文档拆分成较小的片段。典型大小：300-500 令牌，带重叠。 [模块 03](../03-rag/README.md)

**上下文窗口** - 模型可处理的最大令牌数。GPT-5.2：40 万令牌（最多 27.2 万输入，12.8 万输出）。

**嵌入** - 表示文本意义的数值向量。 [模块 03](../03-rag/README.md)

**函数调用** - 模型生成结构化请求以调用外部函数。 [模块 04](../04-tools/README.md)

**幻觉** - 模型生成错误但看似合理的信息。

**提示** - 语言模型的文本输入。 [模块 02](../02-prompt-engineering/README.md)

**语义搜索** - 基于意义的搜索，使用嵌入而非关键词。 [模块 03](../03-rag/README.md)

**有状态 vs 无状态** - 无状态：无记忆。 有状态：保持对话历史。 [模块 01](../01-introduction/README.md)

**令牌** - 模型处理的基本文本单元。影响成本和限制。 [模块 01](../01-introduction/README.md)

**工具链** - 顺序执行工具，输出指导下一次调用。 [模块 04](../04-tools/README.md)

## LangChain4j 组件

**AiServices** - 创建类型安全的 AI 服务接口。

**OpenAiOfficialChatModel** - OpenAI 及 Azure OpenAI 模型的统一客户端。

**OpenAiOfficialEmbeddingModel** - 使用 OpenAI Official 客户端创建嵌入（支持 OpenAI 和 Azure OpenAI）。

**ChatModel** - 语言模型核心接口。

**ChatMemory** - 维护对话历史。

**ContentRetriever** - 查找与 RAG 相关的文档块。

**DocumentSplitter** - 将文档拆分成块。

**EmbeddingModel** - 将文本转为数值向量。

**EmbeddingStore** - 存储和检索嵌入。

**MessageWindowChatMemory** - 维护最近消息的滑动窗口。

**PromptTemplate** - 使用 `{{variable}}` 占位符创建可复用的提示。

**TextSegment** - 带元数据的文本块。用于 RAG。

**ToolExecutionRequest** - 表示工具执行请求。

**UserMessage / AiMessage / SystemMessage** - 对话消息类型。

## AI/ML 概念

**少样本学习** - 在提示中提供示例。 [模块 02](../02-prompt-engineering/README.md)

**大型语言模型（LLM）** - 在大量文本数据上训练的 AI 模型。

**推理努力** - GPT-5.2 控制思考深度的参数。 [模块 02](../02-prompt-engineering/README.md)

**温度** - 控制输出随机性。低=确定性，高=创造性。

**向量数据库** - 用于嵌入的专用数据库。 [模块 03](../03-rag/README.md)

**零样本学习** - 无需示例即可执行任务。 [模块 02](../02-prompt-engineering/README.md)

## 防护措施 - [模块 00](../00-quick-start/README.md)

**纵深防御** - 多层安全方法，结合应用级防护与提供商安全过滤器。

**硬性阻断** - 提供商对严重内容违规返回 HTTP 400 错误。

**输入防护** - LangChain4j 接口，用于验证用户输入在到达 LLM 前。提前阻断有害提示，节省成本和延迟。

**输入防护结果** - 防护验证返回类型：`success()` 或 `fatal("原因")`。

**输出防护** - 验证 AI 响应后再返回给用户的接口。

**提供商安全过滤器** - AI 提供商内置的内容过滤器（如 GitHub Models），在 API 层捕捉违规。

**软拒绝** - 模型礼貌地拒绝回答且不抛出错误。

## 提示工程 - [模块 02](../02-prompt-engineering/README.md)

**思路链** - 逐步推理以提升准确性。

**约束输出** - 强制指定格式或结构。

**高热情** - GPT-5.2 用于彻底推理的模式。

**低热情** - GPT-5.2 用于快速回答的模式。

**多轮对话** - 在多轮交流中保持上下文。

**角色提示** - 通过系统消息设置模型角色身份。

**自我反思** - 模型评估并改进自己的输出。

**结构化分析** - 固定的评估框架。

**任务执行模式** - 计划 → 执行 → 总结。

## RAG（检索增强生成） - [模块 03](../03-rag/README.md)

**文档处理流水线** - 加载 → 分块 → 嵌入 → 存储。

**内存嵌入存储** - 用于测试的非持久存储。

**RAG** - 结合检索与生成以增强回答依据。

**相似度分数** - 衡量语义相似度的分数（0-1）。

**来源引用** - 关于检索内容的元数据。

## 代理与工具 - [模块 04](../04-tools/README.md)

**@Tool 注解** - 标记 Java 方法为可由 AI 调用的工具。

**ReAct 模式** - 推理 → 行动 → 观察 → 重复。

**会话管理** - 为不同用户分离上下文。

**工具** - AI 代理可调用的功能。

**工具描述** - 工具用途和参数文档。

## Agentic 模块 - [模块 05](../05-mcp/README.md)

**@Agent 注解** - 标记接口为 AI 代理，使用声明式行为定义。

**代理监听器** - 通过 `beforeAgentInvocation()` 和 `afterAgentInvocation()` 监控代理执行的钩子。

**Agentic 范围** - 代理使用 `outputKey` 存储结果的共享内存，供下游代理消费。

**AgenticServices** - 使用 `agentBuilder()` 和 `supervisorBuilder()` 创建代理的工厂。

**条件工作流** - 基于条件路由至不同专业代理。

**人机协作** - 添加人工检查点以批准或审查内容的工作流模式。

**langchain4j-agentic** - 用于声明式代理构建的 Maven 依赖（实验性）。

**循环工作流** - 迭代执行代理直到满足条件（例如质量分 ≥ 0.8）。

**outputKey** - 代理注解参数，指定结果在 Agentic 范围中的存储位置。

**并行工作流** - 多代理同时运行，处理独立任务。

**响应策略** - 主管如何形成最终答案：LAST（最后）、SUMMARY（汇总）或 SCORED（评分）。

**顺序工作流** - 按顺序执行代理，输出传递下一步。

**主管代理模式** - 高级代理模式，主管 LLM 动态决定调用哪些子代理。

## 模型上下文协议（MCP） - [模块 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j 中用于 MCP 集成的 Maven 依赖。

**MCP** - 模型上下文协议：连接 AI 应用与外部工具的标准。一次构建，到处使用。

**MCP 客户端** - 连接 MCP 服务器以发现和使用工具的应用。

**MCP 服务器** - 通过 MCP 暴露工具的服务，提供清晰描述和参数 schema。

**McpToolProvider** - LangChain4j 组件，封装 MCP 工具供 AI 服务和代理使用。

**McpTransport** - MCP 通信接口，包含 Stdio 和 HTTP 实现。

**Stdio 传输** - 通过 stdin/stdout 的本地进程传输。适合访问文件系统或命令行工具。

**StdioMcpTransport** - LangChain4j 实现，作为子进程启动 MCP 服务器。

**工具发现** - 客户端查询服务器可用工具及其描述和 schema。

## Azure 服务 - [模块 01](../01-introduction/README.md)

**Azure AI Search** - 支持向量功能的云搜索。 [模块 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - 部署 Azure 资源。

**Azure OpenAI** - 微软企业 AI 服务。

**Bicep** - Azure 基础设施即代码语言。 [基础设施指南](../01-introduction/infra/README.md)

**部署名称** - Azure 中模型部署的名称。

**GPT-5.2** - 具有推理控制的最新 OpenAI 模型。 [模块 02](../02-prompt-engineering/README.md)

## 测试与开发 - [测试指南](TESTING.md)

**开发容器** - 容器化开发环境。 [配置](../../../.devcontainer/devcontainer.json)

**GitHub Models** - 免费 AI 模型试验场。 [模块 00](../00-quick-start/README.md)

**内存测试** - 使用内存存储进行测试。

**集成测试** - 使用真实基础设施的测试。

**Maven** - Java 构建自动化工具。

**Mockito** - Java 模拟框架。

**Spring Boot** - Java 应用框架。 [模块 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件使用AI翻译服务【Co-op Translator】（https://github.com/Azure/co-op-translator）翻译完成。尽管我们力求准确，但请注意，自动翻译可能存在错误或不准确之处。原始语言的文件应被视为权威来源。对于重要信息，建议采用专业人工翻译。我们不对因使用此翻译而产生的任何误解或误释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->