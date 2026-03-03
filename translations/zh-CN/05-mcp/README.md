# 模块 05：模型上下文协议（MCP）

## 目录

- [你将学到什么](../../../05-mcp)
- [什么是 MCP？](../../../05-mcp)
- [MCP 的工作原理](../../../05-mcp)
- [智能体模块](../../../05-mcp)
- [运行示例](../../../05-mcp)
  - [先决条件](../../../05-mcp)
- [快速开始](../../../05-mcp)
  - [文件操作（Stdio）](../../../05-mcp)
  - [监督智能体](../../../05-mcp)
    - [运行演示](../../../05-mcp)
    - [监督智能体的工作原理](../../../05-mcp)
    - [FileAgent 如何在运行时发现 MCP 工具](../../../05-mcp)
    - [响应策略](../../../05-mcp)
    - [理解输出](../../../05-mcp)
    - [智能体模块功能说明](../../../05-mcp)
- [关键概念](../../../05-mcp)
- [恭喜！](../../../05-mcp)
  - [下一步是什么？](../../../05-mcp)

## 你将学到什么

你已经构建了对话式 AI，掌握了提示工程，能让回答基于文档内容，且创建了带工具的智能体。但所有那些工具都是专门为你的特定应用定制的。如果你可以让你的 AI 访问任何人都能创建和共享的标准化工具生态系统呢？在本模块中，你将学习如何利用模型上下文协议（MCP）和 LangChain4j 的智能体模块做到这一点。我们首先展示一个简单的 MCP 文件读取器，然后展示如何轻松地将其集成到使用监督智能体模式的高级智能体工作流中。

## 什么是 MCP？

模型上下文协议（MCP）正是解决这个问题——为 AI 应用发现和使用外部工具提供标准化方式。你不再需要为每个数据源或服务编写定制集成，只需连接到以一致格式暴露其能力的 MCP 服务器。你的 AI 智能体就可以自动发现并使用这些工具。

下图展示了区别——没有 MCP，每个集成都需要定制点对点连线；有了 MCP，一种协议即可连接你的应用和任何工具：

<img src="../../../translated_images/zh-CN/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 之前：复杂的点对点集成。MCP 之后：一个协议，无限可能。*

MCP 解决了 AI 开发中的根本问题：每次集成都必须定制。想访问 GitHub？写定制代码。想读文件？定制代码。想查询数据库？定制代码。而且这些集成都无法被其它 AI 应用所复用。

MCP 让这一切标准化。一台 MCP 服务器以清晰的描述和参数模式暴露工具。任何 MCP 客户端都可以连接、发现可用工具并使用它们。一次构建，到处使用。

下图展示了该架构——一台 MCP 客户端（你的 AI 应用）连接多台 MCP 服务器，每台服务器通过标准协议暴露各自工具集：

<img src="../../../translated_images/zh-CN/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型上下文协议架构——标准化工具发现和执行*

## MCP 的工作原理

在底层，MCP 使用分层架构。你的 Java 应用（MCP 客户端）发现可用工具，通过传输层（Stdio 或 HTTP）发送 JSON-RPC 请求，MCP 服务器执行业务并返回结果。下图分解了该协议的每个层级：

<img src="../../../translated_images/zh-CN/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 底层工作流程——客户端发现工具，交换 JSON-RPC 消息，通过传输层执行操作。*

**服务器-客户端架构**

MCP 采用客户端-服务器模型。服务器提供工具——读取文件、查询数据库、调用 API。客户端（你的 AI 应用）连接服务器并使用他们的工具。

要在 LangChain4j 中使用 MCP，添加以下 Maven 依赖：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**工具发现**

当你的客户端连接 MCP 服务器时，会询问，“你有哪些工具？”服务器会返回带描述和参数模式的工具列表。你的 AI 智能体然后可以基于用户请求决定使用哪些工具。下图展示了这个握手过程——客户端发送 `tools/list` 请求，服务器返回它的工具列表及其描述和参数模式：

<img src="../../../translated_images/zh-CN/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在启动时发现可用工具——现在知道有哪些功能可用，并能决定使用哪些。*

**传输机制**

MCP 支持不同的传输机制。两种选项是 Stdio（用于本地子进程通信）和可流式 HTTP（用于远程服务器）。本模块演示 Stdio 传输：

<img src="../../../translated_images/zh-CN/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 传输机制：远程服务器用 HTTP，本地进程用 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

用于本地进程。应用程序以子进程方式启动服务器，通过标准输入/输出通信。适合文件系统访问或命令行工具。

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

`@modelcontextprotocol/server-filesystem` 服务器暴露以下工具，均在你指定的目录沙箱中运行：

| 工具 | 描述 |
|------|------|
| `read_file` | 读取单个文件的内容 |
| `read_multiple_files` | 一次读取多个文件 |
| `write_file` | 创建或覆盖文件 |
| `edit_file` | 有针对性的查找替换编辑 |
| `list_directory` | 列出路径下的文件和目录 |
| `search_files` | 递归搜索匹配模式的文件 |
| `get_file_info` | 获取文件元数据（大小、时间戳、权限） |
| `create_directory` | 创建目录（包含父目录） |
| `move_file` | 移动或重命名文件或目录 |

下图展示了 Stdio 传输在运行时的工作方式——你的 Java 应用以子进程启动 MCP 服务器，通过 stdin/stdout 管道通信，无需网络或 HTTP：

<img src="../../../translated_images/zh-CN/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 传输实况——你的应用以子进程启动 MCP 服务器，通过 stdin/stdout 管道通信。*

> **🤖 尝试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)，提问：
> - "Stdio 传输是如何工作的？什么时候应该用它代替 HTTP？"
> - "LangChain4j 如何管理启动的 MCP 服务器进程的生命周期？"
> - "让 AI 访问文件系统的安全考虑有哪些？"

## 智能体模块

MCP 提供了标准化工具，LangChain4j 的**智能体模块**则提供了一种声明式方式来构建协调这些工具的智能体。`@Agent` 注解和 `AgenticServices` 允许你通过接口定义智能体行为，而非命令式代码。

在本模块中，你将探索**监督智能体**模式——一种高级智能体 AI 方法，其中“监督者”智能体根据用户请求动态决定调用哪些子智能体。我们将把 MCP 支持的文件访问能力赋给其中一个子智能体，结合两种概念。

要使用智能体模块，添加以下 Maven 依赖：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **注意：** `langchain4j-agentic` 模块使用单独的版本属性（`langchain4j.mcp.version`），因为它的发布节奏与 LangChain4j 核心库不同。

> **⚠️ 实验性:** `langchain4j-agentic` 模块是**实验性**的，可能会发生变化。构建 AI 助手的稳定方式仍是使用 `langchain4j-core` 搭配自定义工具（模块 04）。

## 运行示例

### 先决条件

- 完成 [模块 04 - 工具](../04-tools/README.md)（本模块基于自定义工具概念并与 MCP 工具作比较）
- 根目录有包含 Azure 凭据的 `.env` 文件（由模块 01 中的 `azd up` 创建）
- Java 21 及以上，Maven 3.9 及以上
- Node.js 16 及以上和 npm（用于 MCP 服务器）

> **注意：** 如果你还未设置环境变量，请参见 [模块 01 - 介绍](../01-introduction/README.md) 了解部署说明（`azd up` 会自动创建 `.env` 文件），或将 `.env.example` 复制为根目录下的 `.env` 并填写你的参数。

## 快速开始

**使用 VS Code：** 在资源管理器中右键任何演示文件，选择**“运行 Java”**，或使用运行和调试面板的启动配置（确保先配置好 `.env` 文件含 Azure 凭据）。

**使用 Maven：** 也可以在命令行运行以下示例。

### 文件操作（Stdio）

演示基于本地子进程的工具。

**✅ 无需额外先决条件** - MCP 服务器会自动启动。

**使用启动脚本（推荐）：**

启动脚本会自动从根目录的 `.env` 文件加载环境变量：

**Bash：**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell：**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**使用 VS Code：** 右键点击 `StdioTransportDemo.java`，选择**“运行 Java”**（确保 `.env` 配置正确）。

应用程序会自动启动文件系统 MCP 服务器并读取本地文件。注意子进程管理由程序处理。

**期望输出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### 监督智能体

**监督智能体模式** 是一种灵活的智能体 AI 形式。监督智能体使用大语言模型自主决定根据用户请求调用哪些智能体。下一个示例中，我们结合 MCP 支持的文件访问和 LLM 智能体，创建一个监督的“读取文件→报告”工作流。

演示中，`FileAgent` 使用 MCP 文件系统工具读取文件，`ReportAgent` 生成包含执行摘要（一句话）、3 个关键点和建议的结构化报告。监督智能体自动协调该流程：

<img src="../../../translated_images/zh-CN/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*监督者使用其 LLM 决定调用哪些智能体及顺序——无需硬编码路由。*

下面是我们的文件到报告流水线的具体工作流：

<img src="../../../translated_images/zh-CN/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 通过 MCP 工具读取文件，然后 ReportAgent 将原始内容转化为结构化报告。*

下图序列图追踪了完整监督者协调过程——从启动 MCP 服务器，通过监督者的自主智能体选择，到 stdio 上的工具调用及最终报告生成：

<img src="../../../translated_images/zh-CN/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*监督者自主调用 FileAgent（FileAgent 通过 stdio 调用 MCP 服务器读取文件），然后调用 ReportAgent 生成结构化报告——每个智能体将输出存储于共享的智能体作用域。*

每个智能体将输出存储在**智能体作用域**（共享内存）中，允许后续智能体读取前面结果。这展现了 MCP 工具如何无缝集成进智能体工作流——监督者无需知道文件如何读取，只需知道 `FileAgent` 会做。

#### 运行演示

启动脚本会自动从根目录的 `.env` 文件加载环境变量：

**Bash：**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell：**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**使用 VS Code：** 右键点击 `SupervisorAgentDemo.java`，选择**“运行 Java”**（确保 `.env` 配置正确）。

#### 监督者的工作原理

在构建智能体之前，你需要先把 MCP 传输连接到客户端并包装成 `ToolProvider`。这样，MCP 服务器的工具才会对你的智能体可用：

```java
// 从传输创建一个MCP客户端
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// 将客户端包装为ToolProvider — 这将MCP工具桥接到LangChain4j中
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

现在你可以将 `mcpToolProvider` 注入任何需要 MCP 工具的智能体：

```java
// 第一步：FileAgent 使用 MCP 工具读取文件
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 具有用于文件操作的 MCP 工具
        .build();

// 第二步：ReportAgent 生成结构化报告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor 协调整个文件 → 报告的工作流程
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 返回最终报告
        .build();

// Supervisor 根据请求决定调用哪些代理
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### FileAgent 如何在运行时发现 MCP 工具

你可能会问：**`FileAgent` 是怎么知道该怎么用 npm 文件系统工具的？**答案是它不知道——**LLM** 通过工具模式在运行时推断出来。

`FileAgent` 接口只是一个**提示定义**，它没有任何硬编码的 `read_file`、`list_directory` 或其他 MCP 工具知识。整体流程如下所示：
1. **服务器启动：** `StdioMcpTransport` 启动 `@modelcontextprotocol/server-filesystem` npm 包作为子进程  
2. **工具发现：** `McpClient` 发送 `tools/list` JSON-RPC 请求给服务器，服务器返回工具名称、描述和参数模式（例如 `read_file` — *“读取文件的完整内容”* — `{ path: string }`）  
3. **模式注入：** `McpToolProvider` 包装这些发现的模式并将其提供给 LangChain4j  
4. **LLM 决策：** 当调用 `FileAgent.readFile(path)` 时，LangChain4j 向 LLM 发送系统消息、用户消息、**以及工具模式列表**。LLM 阅读工具描述并生成工具调用（例如 `read_file(path="/some/file.txt")`）  
5. **执行：** LangChain4j 拦截工具调用，通过 MCP 客户端将其路由回 Node.js 子进程，获取结果并反馈给 LLM  

这与上面描述的相同的[工具发现](../../../05-mcp)机制，但专门应用于代理工作流。`@SystemMessage` 和 `@UserMessage` 注释指导 LLM 的行为，而注入的 `ToolProvider` 赋予它**能力** — LLM 在运行时将两者桥接起来。

> **🤖 试试使用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：** 打开 [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) 并询问：  
> - “这个代理如何知道调用哪个 MCP 工具？”  
> - “如果我从代理构建器中移除 ToolProvider 会发生什么？”  
> - “工具模式如何传递给 LLM？”  

#### 响应策略

配置 `SupervisorAgent` 时，可以指定在子代理完成任务后，它应该如何形成最终答复。下面的图展示了三种可用策略 — LAST 直接返回最终代理的输出，SUMMARY 通过 LLM 综合所有输出，SCORED 则选择对原始请求评分更高的结果：

<img src="../../../translated_images/zh-CN/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*监督者形成最终回答的三种策略 — 根据你是希望获得最后一个代理的输出、综合摘要还是评分最高的选项来选择。*

可用策略如下：

| 策略 | 说明 |
|----------|-------------|
| **LAST** | 监督者返回最后调用的子代理或工具的输出。当工作流中最终代理专门设计为产生完整最终答案时（例如研究流程中的“摘要代理”）非常有用。 |
| **SUMMARY** | 监督者使用自身的内部语言模型（LLM）综合整个交互及所有子代理输出的摘要，然后将该摘要作为最终回答返回。这为用户提供了清晰的聚合答案。 |
| **SCORED** | 系统使用内部 LLM 对 LAST 响应和交互 SUMMARY 两者进行评分，对原始用户请求进行比较，返回分数更高的输出。 |

完整实现见 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)。

> **🤖 试试使用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：** 打开 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 并询问：  
> - “监督者如何决定调用哪些代理？”  
> - “监督者和顺序工作流模式有什么区别？”  
> - “我如何自定义监督者的规划行为？”  

#### 理解输出

运行演示时，你会看到监督者如何协调多个代理的结构化演练。下面是每个部分的含义：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**标题**介绍工作流概念：一个从文件读取到报告生成的聚焦管道。

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```
  
**工作流图** 展示代理间的数据流。每个代理都有特定任务：  
- **FileAgent** 使用 MCP 工具读取文件并将原始内容存储在 `fileContent`  
- **ReportAgent** 使用这些内容生成结构化报告存储在 `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**用户请求** 显示任务。监督者解析请求并决定依次调用 FileAgent → ReportAgent。

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```
  
**监督者编排** 展示两步流程实际运行：  
1. **FileAgent** 通过 MCP 读取文件并存储内容  
2. **ReportAgent** 接收内容生成结构化报告

监督者根据用户请求**自主**做出这些决策。

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```
  
#### 代理模块功能说明

示例演示了代理模块的几个高级特性。我们特别看下 Agentic Scope 和 Agent Listeners。

**Agentic Scope** 显示多代理共享内存，代理使用 `@Agent(outputKey="...")` 存储结果，这允许：  
- 后续代理访问先前代理的输出  
- 监督者综合最终响应  
- 你检查各代理产生的内容

下图展示 Agentic Scope 如何作为共享内存运作于文件到报告的工作流 — FileAgent 写入 `fileContent`，ReportAgent 读取该内容并写入自己的 `report`：

<img src="../../../translated_images/zh-CN/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope 作为共享内存 — FileAgent 写 `fileContent`，ReportAgent 读取并写 `report`，代码读取最终结果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // 来自 FileAgent 的原始文件数据
String report = scope.readState("report");            // 来自 ReportAgent 的结构化报告
```
  
**Agent Listeners** 允许监控和调试代理执行。演示中逐步输出来自挂载在每次代理调用上的 AgentListener：  
- **beforeAgentInvocation** — 监督者选择代理时调用，便于查看选择了哪个代理及原因  
- **afterAgentInvocation** — 代理完成时调用，显示其结果  
- **inheritedBySubagents** — 为 true 时监听继承层级内所有代理  

下图展示完整的 Agent Listener 生命周期，包括 `onError` 如何处理代理执行过程中的失败：

<img src="../../../translated_images/zh-CN/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners 挂载在执行生命周期中 — 监控代理开始、完成或错误。*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // 传播到所有子代理
    }
};
```
  
除了监督者模式，`langchain4j-agentic` 模块还提供几种强大的工作流模式。下图展示了五种模式 — 从简单顺序管道到人类参与的审批工作流：

<img src="../../../translated_images/zh-CN/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*五种代理编排工作流模式 — 从简单顺序管道到人类介入审批流程。*

| 模式 | 说明 | 用例 |
|---------|-------------|----------|
| **Sequential** | 按顺序执行代理，输出流向下一个 | 管道：研究 → 分析 → 报告 |
| **Parallel** | 同时运行代理 | 独立任务：天气 + 新闻 + 股票 |
| **Loop** | 迭代直到满足条件 | 质量评分：细化直到分数 ≥ 0.8 |
| **Conditional** | 基于条件路由 | 分类 → 路由到专家代理 |
| **Human-in-the-Loop** | 添加人工检查点 | 审批流程，内容审核 |

## 关键概念

了解了 MCP 和代理模块的实际应用后，我们总结何时使用各方法。

MCP 最大优势之一是其不断扩展的生态系统。下图展示单一通用协议如何连接 AI 应用与各种 MCP 服务端 — 从文件系统和数据库访问到 GitHub、邮件、网页爬取等：

<img src="../../../translated_images/zh-CN/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 创造了通用协议生态系统 — 任何 MCP 兼容的服务器都能与任何 MCP 客户端协作，实现跨应用工具共享。*

**MCP** 适合你想利用现有工具生态、构建可被多个应用共享的工具、通过标准协议整合第三方服务或无需改动代码即可替换工具实现时使用。

**代理模块** 最适合你想通过 `@Agent` 注解声明式定义代理、需要顺序/循环/并行等工作流编排、偏好接口定义而非命令式代码，或多代理共享输出（通过 `outputKey`）的场景。

**监督者代理模式** 在工作流事先不可预测、需要让 LLM 决定流程、多种专用代理动态编排、构建多能力路由的对话系统，或追求最灵活自适应代理行为的场景中表现突出。

为帮助你在自定义 `@Tool` 方法（模块04）与 MCP 工具（本模块）之间做选择，下图突出关键权衡 — 自定义工具提供紧耦合和完全类型安全以支持特定业务逻辑，MCP 工具则提供标准化可复用的集成：

<img src="../../../translated_images/zh-CN/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*何时使用自定义 @Tool 方法与 MCP 工具 — 自定义工具适合特定业务逻辑，确保类型安全；MCP 工具适合跨应用的标准化集成。*

## 恭喜！

你已经完成 LangChain4j 初学者课程中的全部五个模块！下面是你完成的完整学习旅程回顾 — 从基础聊天一直到 MCP 驱动的代理系统：

<img src="../../../translated_images/zh-CN/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*你通过的五个模块学习旅程 — 从基础聊天到 MCP 驱动的代理系统。*

你已经掌握：

- 如何构建带记忆的对话 AI（模块01）  
- 不同任务的提示工程模式（模块02）  
- 用 RAG 在文档中构建基础反应（模块03）  
- 使用自定义工具创建基本 AI 代理（助手）（模块04）  
- 使用 LangChain4j MCP 和代理模块集成标准化工具（模块05）  

### 接下来做什么？

完成模块后，探索 [测试指南](../docs/TESTING.md) 以实操 LangChain4j 测试概念。

**官方资源：**  
- [LangChain4j 文档](https://docs.langchain4j.dev/) - 完整指南和 API 参考  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - 源代码与示例  
- [LangChain4j 教程](https://docs.langchain4j.dev/tutorials/) - 各类用例分步教程  

感谢你完成本课程！

---

**导航：** [← 上一节：模块 04 - 工具](../04-tools/README.md) | [返回主目录](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由人工智能翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译。尽管我们力求准确，但请注意，自动翻译可能存在错误或不准确之处。原始文件的原文版本应被视为权威来源。如涉及重要信息，建议采用专业人工翻译。因使用本翻译产生的任何误解或误释，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->