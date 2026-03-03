# 模块 05：模型上下文协议 (MCP)

## 目录

- [你将学习什么](../../../05-mcp)
- [什么是 MCP？](../../../05-mcp)
- [MCP 如何工作](../../../05-mcp)
- [Agentic 模块](../../../05-mcp)
- [运行示例](../../../05-mcp)
  - [先决条件](../../../05-mcp)
- [快速开始](../../../05-mcp)
  - [文件操作（Stdio）](../../../05-mcp)
  - [监督代理](../../../05-mcp)
    - [运行演示](../../../05-mcp)
    - [监督者如何工作](../../../05-mcp)
    - [响应策略](../../../05-mcp)
    - [理解输出](../../../05-mcp)
    - [Agentic 模块功能说明](../../../05-mcp)
- [关键概念](../../../05-mcp)
- [恭喜！](../../../05-mcp)
  - [接下来是什么？](../../../05-mcp)

## 你将学习什么

你已经构建了对话式 AI，掌握了提示，基于文档生成了有根据的回复，并创建了带工具的代理。但所有这些工具都是为你的特定应用程序定制构建的。如果你能让你的 AI 访问一个任何人都能创建和共享的标准化工具生态系统该多好？在本模块中，你将学习如何通过模型上下文协议（MCP）和 LangChain4j 的 agentic 模块来实现这一目标。我们首先展示一个简单的 MCP 文件读取器，然后演示它如何轻松集成到使用监督代理模式的高级 agentic 工作流中。

## 什么是 MCP？

模型上下文协议（MCP）正是为此提供了一种标准方式，让 AI 应用程序能够发现和使用外部工具。你无需为每个数据源或服务编写定制集成，只需连接到以一致格式公开其能力的 MCP 服务器。你的 AI 代理就能自动发现并使用这些工具。

下图展示了区别——没有 MCP，每个集成都需要定制的点对点连接；有了 MCP，一个协议即可连接你的应用程序与任何工具：

<img src="../../../translated_images/zh-CN/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*在 MCP 之前：复杂的点对点集成。在 MCP 之后：一个协议，无限可能。*

MCP 解决了 AI 开发中的一个根本问题：每个集成都定制。想访问 GitHub？定制代码。想读文件？定制代码。想查询数据库？定制代码。且这些集成都无法与其他 AI 应用共用。

MCP 使其标准化。MCP 服务器以清晰的描述和架构公开工具。任何 MCP 客户端都能连接、发现可用工具并使用它们。一次构建，处处可用。

下图说明了此架构——单个 MCP 客户端（你的 AI 应用）连接多个 MCP 服务器，每个服务器通过标准协议暴露自身工具集：

<img src="../../../translated_images/zh-CN/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*模型上下文协议架构——标准化的工具发现和执行*

## MCP 如何工作

在底层，MCP 使用分层架构。你的 Java 应用（MCP 客户端）发现可用工具，通过传输层（Stdio 或 HTTP）发送 JSON-RPC 请求，MCP 服务器执行操作并返回结果。下图细分了该协议的各层：

<img src="../../../translated_images/zh-CN/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 工作原理——客户端发现工具，交换 JSON-RPC 消息，通过传输层执行操作。*

**服务器-客户端架构**

MCP 使用客户端-服务器模型。服务器提供工具——读取文件、查询数据库、调用 API。客户端（你的 AI 应用）连接服务器并使用其工具。

要在 LangChain4j 中使用 MCP，添加以下 Maven 依赖：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```
  
**工具发现**

当客户端连接到 MCP 服务器时，会询问“你有哪些工具？”服务器会返回一份可用工具清单，每个工具带有描述和参数架构。你的 AI 代理可根据用户请求决定使用哪些工具。下图展示了这段握手过程——客户端发送 `tools/list` 请求，服务器返回其可用工具及描述参数架构：

<img src="../../../translated_images/zh-CN/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI 在启动时发现可用工具——它现在知道有哪些能力可用，可以决定使用哪些工具。*

**传输机制**

MCP 支持不同的传输机制。两个选项是 Stdio（用于本地子进程通信）和可流式 HTTP（用于远程服务器）。本模块演示 Stdio 传输：

<img src="../../../translated_images/zh-CN/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 传输机制：远程服务器用 HTTP，本地进程用 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

用于本地进程。你的应用作为子进程启动服务器，并通过标准输入输出通信。适合文件系统访问或命令行工具。

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
  
`@modelcontextprotocol/server-filesystem` 服务器暴露以下工具，均限制在你指定的目录沙盒内：

| 工具 | 描述 |
|------|------|
| `read_file` | 读取单个文件内容 |
| `read_multiple_files` | 一次读取多个文件 |
| `write_file` | 创建或覆盖文件 |
| `edit_file` | 有针对性的查找替换编辑 |
| `list_directory` | 列出路径下文件和目录 |
| `search_files` | 递归搜索匹配模式的文件 |
| `get_file_info` | 获取文件元数据（大小、时间戳、权限） |
| `create_directory` | 创建目录（包含父目录） |
| `move_file` | 移动或重命名文件或目录 |

下图展示了 Stdio 传输的运行时流程——你的 Java 应用将 MCP 服务器作为子进程启动，并通过 stdin/stdout 管道通信，期间不涉及网络或 HTTP ：

<img src="../../../translated_images/zh-CN/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 传输实际运行图——你的应用作为子进程启动 MCP 服务器，通过 stdin/stdout 管道通信。*

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) 并问：
> - “Stdio 传输如何工作，何时应该用它而非 HTTP？”
> - “LangChain4j 如何管理所启动 MCP 服务器进程的生命周期？”
> - “允许 AI 访问文件系统有哪些安全隐患？”

## Agentic 模块

虽然 MCP 提供了标准化工具，LangChain4j 的 **agentic 模块** 提供了一种声明式方式来构建协调这些工具的代理。`@Agent` 注解和 `AgenticServices` 让你通过接口定义代理行为，而不是命令式编码。

在本模块中，你将探索 **监督代理** 模式——一个先进的 agentic AI 方法，其中一个“监督”代理基于用户请求动态决定调用哪些子代理。我们将结合两种概念，赋予子代理基于 MCP 的文件访问能力。

要使用 agentic 模块，添加以下 Maven 依赖：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
  
> **注意：** `langchain4j-agentic` 模块使用单独的版本属性(`langchain4j.mcp.version`)，因为其发布时间表与 LangChain4j 核心库不同。

> **⚠️ 实验性：** `langchain4j-agentic` 模块为**实验性质**，可能会发生变化。构建 AI 助手的稳定方式仍是使用 `langchain4j-core` 和自定义工具（见模块 04）。

## 运行示例

### 先决条件

- 完成 [模块 04 - 工具](../04-tools/README.md)（本模块基于自定义工具概念，并与 MCP 工具做比较）
- 根目录下有包含 Azure 凭据的 `.env` 文件（由模块 01 中的 `azd up` 创建）
- Java 21+，Maven 3.9+
- Node.js 16+ 和 npm（用于 MCP 服务器）

> **注意：** 如果你还未设置环境变量，参见 [模块 01 - 介绍](../01-introduction/README.md) 获取部署说明（`azd up` 会自动创建 `.env` 文件），或将 `.env.example` 复制为根目录下的 `.env` 并填写你的值。

## 快速开始

**使用 VS Code：** 只需在资源管理器中右键任意演示文件，选择 **“Run Java”**，或从运行和调试面板使用启动配置（确保先正确配置 `.env` 文件中的 Azure 凭据）。

**使用 Maven：** 你也可以在命令行通过以下示例运行。

### 文件操作（Stdio）

演示基于本地子进程的工具。

**✅ 无需先决条件** — MCP 服务器会自动启动。

**使用启动脚本（推荐）：**

启动脚本会自动从根目录 `.env` 文件加载环境变量：

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
  
**使用 VS Code：** 右键 `StdioTransportDemo.java` 选择 **“Run Java”**（确保 `.env` 文件已配置）。

应用自动启动文件系统 MCP 服务器并读取本地文件。注意子进程管理为你处理了所有细节。

**预期输出：**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```
  
### 监督代理

**监督代理模式** 是一种**灵活的** agentic AI 形式。监督者使用 LLM 自主决定根据用户请求调用哪些代理。在下一个示例中，我们结合 MCP 支持的文件访问和 LLM 代理，创建一个“文件读取 → 报告”受监督的工作流。

演示中，`FileAgent` 使用 MCP 文件系统工具读取文件，`ReportAgent` 基于读取的内容生成含执行摘要（一句）、3 个重点和建议的结构化报告。监督者自动协调这整个流程：

<img src="../../../translated_images/zh-CN/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*监督者使用其 LLM 判断调用哪些代理及调用顺序——无需硬编码路由。*

我们的文件到报告管道具体工作流如下：

<img src="../../../translated_images/zh-CN/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent 通过 MCP 工具读取文件，ReportAgent 将原始内容转为结构化报告。*

每个代理将其输出存储在**Agentic 作用域**（共享内存）中，下游代理可访问前序结果。此演示说明 MCP 工具如何无缝集成到 agentic 工作流——监督者无需了解文件是*如何*读取的，只需知道 `FileAgent` 会读取。

#### 运行演示

启动脚本会自动从根目录 `.env` 文件加载环境变量：

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
  
**使用 VS Code：** 右键 `SupervisorAgentDemo.java` 选择 **“Run Java”**（确保 `.env` 文件已配置）。

#### 监督者如何工作

构建代理前，需先将 MCP 传输连接至客户端，并将其封装成 `ToolProvider`。这样，MCP 服务器的工具才可供代理使用：

```java
// 从传输创建一个MCP客户端
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// 将客户端包装为ToolProvider——这将MCP工具桥接到LangChain4j中
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```
  
现在，你可以将 `mcpToolProvider` 注入任何需要 MCP 工具的代理：

```java
// 第一步：FileAgent使用MCP工具读取文件
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 具有用于文件操作的MCP工具
        .build();

// 第二步：ReportAgent生成结构化报告
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// 主管协调文件→报告的工作流
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 返回最终报告
        .build();

// 主管根据请求决定调用哪些代理
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```
  
#### 响应策略

配置 `SupervisorAgent` 时，你需指定子代理完成任务后，监督者应如何向用户形成最终回答。下图显示了三种可用策略——LAST 直接返回最后一个代理输出，SUMMARY 通过 LLM 汇总所有输出，SCORED 则选分数更高的回应：

<img src="../../../translated_images/zh-CN/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*监督者形成最终响应的三种策略：根据你希望获得最后一个代理输出、合成摘要还是最高评分结果来选择。*

可用策略说明：

| 策略 | 描述 |
|------|------|
| **LAST** | 监督者返回最后调用的子代理或工具的输出。当工作流中的最终代理专门设计为生成完整最终答案时，此策略很有用（例如，研究流程中的“摘要代理”）。 |
| **SUMMARY** | 监督者使用其内部语言模型（LLM）综合整个交互以及所有子代理输出的摘要，并将该摘要作为最终响应返回。这为用户提供了清晰的聚合答案。 |
| **SCORED** | 系统使用内部 LLM 对 LAST 响应和 SUMMARY 摘要分别相对于原始用户请求评分，返回得分较高的输出。 |
参见 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 查看完整实现。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天尝试：** 打开 [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) 并询问：
> - “Supervisor 如何决定调用哪些代理？”
> - “Supervisor 和顺序工作流模式有什么区别？”
> - “我如何自定义 Supervisor 的规划行为？”

#### 理解输出内容

运行演示时，你会看到 Supervisor 如何协调多个代理的结构化演练。下述是每个部分的含义：

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**标题栏**介绍了工作流概念：一个从文件读取到报告生成的专注管道。

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

**工作流图**显示了代理之间的数据流。每个代理具有特定角色：
- **FileAgent** 使用 MCP 工具读取文件并将原始内容存储在 `fileContent`
- **ReportAgent** 消费该内容并生成结构化报告，存储在 `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**用户请求**展示了任务，Supervisor 解析后决定调用 FileAgent → ReportAgent。

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

**Supervisor 编排**展示了两步流程的执行：
1. **FileAgent** 通过 MCP 读取文件并存储内容
2. **ReportAgent** 接收内容并生成结构化报告

Supervisor 基于用户请求**自主**做出了这些决定。

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

#### Agentic 模块功能说明

该示例演示了 agentic 模块的多个高级功能。让我们细看 Agentic Scope 和 Agent Listeners。

**Agentic Scope** 显示了代理使用 `@Agent(outputKey="...")` 存储结果的共享内存。它允许：
- 后续代理访问前序代理的输出
- Supervisor 综合生成最终响应
- 你检查每个代理产生的内容

下图展示了 Agentic Scope 作为共享内存在文件到报告工作流中的工作方式 — FileAgent 将输出写入 `fileContent` 键，ReportAgent 读取该键并将其输出写入 `report`：

<img src="../../../translated_images/zh-CN/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope 起共享内存作用 — FileAgent 写入 `fileContent`，ReportAgent 读取它并写入 `report`，你的代码读取最终结果。*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // 来自 FileAgent 的原始文件数据
String report = scope.readState("report");            // 来自 ReportAgent 的结构化报告
```

**Agent Listeners** 使你能监控和调试代理执行。演示中你看到的逐步输出来自监听代理调用的 AgentListener：
- **beforeAgentInvocation** - Supervisor 选择代理时调用，显示选择了哪个代理及其原因
- **afterAgentInvocation** - 代理完成时调用，显示其结果
- **inheritedBySubagents** - 若为 true，监听所有代理层级中的代理

下图显示完整的 Agent Listener 生命周期，包括 `onError` 如何处理代理执行中的失败：

<img src="../../../translated_images/zh-CN/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners 钩入执行生命周期 — 监控代理启动、完成或遇到错误的时刻。*

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

除了 Supervisor 模式外，`langchain4j-agentic` 模块还提供了多种强大工作流模式。下图显示了全部五种——从简单的顺序管道到人工审批工作流：

<img src="../../../translated_images/zh-CN/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*五种代理工作流模式——从简单的顺序管道到人工打回审批工作流。*

| 模式 | 描述 | 适用场景 |
|---------|-------------|----------|
| **顺序** | 依次执行代理，输出流向下一个 | 管道流程：调研 → 分析 → 报告 |
| **并行** | 同时运行多个代理 | 独立任务：天气 + 新闻 + 股票 |
| **循环** | 迭代直到满足条件 | 质量评分：迭代直到分数 ≥ 0.8 |
| **条件** | 基于条件进行路由 | 分类 → 路由到专家代理 |
| **人工介入** | 添加人工检查点 | 审批流程，内容复审 |

## 关键概念

既然你已深入了解 MCP 和 agentic 模块的实战，下面总结何时使用哪种方案。

MCP 最大优势之一是其不断成长的生态系统。下图显示了单一通用协议如何将 AI 应用连接到多种 MCP 服务器——从文件系统和数据库访问到 GitHub、电子邮件、网页抓取等：

<img src="../../../translated_images/zh-CN/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP 构建了一个通用协议生态——任何 MCP 兼容服务器都能与任意 MCP 兼容客户端协作，实现跨应用工具共享。*

**MCP** 适合想利用现有工具生态、构建多个应用共享的工具、通过标准协议集成第三方服务，或无需更改代码即可替换工具实现的场景。

**Agentic 模块** 最适合想要通过 `@Agent` 注解声明式定义代理、需要工作流编排（顺序、循环、并行）、偏好基于接口设计代理而非命令式代码，或结合多个通过 `outputKey` 共享输出的代理。

**Supervisor Agent 模式** 擅长工作流不可预先确定并需要 LLM 决策、多位多专代理需动态调度、打造路由多种能力的对话系统，或追求最高灵活性与自适应代理行为时。

为了帮助你在 Module 04 的自定义 `@Tool` 方法和本模块的 MCP 工具间选择，下图对关键权衡进行了对比——自定义工具提供紧耦合和完备类型安全的应用专属逻辑，MCP 工具则提供标准化、可复用的集成：

<img src="../../../translated_images/zh-CN/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*何时使用自定义 @Tool 方法 vs MCP 工具——自定义工具支持应用特定逻辑且具类型安全，MCP 工具提供跨应用标准集成。*

## 恭喜！

你已完成 LangChain4j 初学者课程的全部五个模块！下面展示的是你完成的全部学习路径——从基础对话到 MCP 驱动的 agentic 系统：

<img src="../../../translated_images/zh-CN/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*你的全部学习旅程 —— 从基础聊天到 MCP 驱动的 agentic 系统。*

你已学会：

- 如何构建具备记忆的对话 AI（模块 01）
- 针对不同任务的提示工程模式（模块 02）
- 使用 RAG 将回答落地至文档（模块 03）
- 用自定义工具创建基础 AI 代理（助手）（模块 04）
- 将标准化工具与 LangChain4j MCP 及 Agentic 模块集成（模块 05）

### 接下来？

完成模块后，探索 [测试指南](../docs/TESTING.md) 以查看 LangChain4j 测试概念的实际应用。

**官方资源：**
- [LangChain4j 文档](https://docs.langchain4j.dev/) - 全面指南与 API 参考
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - 源代码与示例
- [LangChain4j 教程](https://docs.langchain4j.dev/tutorials/) - 针对各种用例的循序渐进教程

感谢你完成本课程！

---

**导航：** [← 上一：模块 04 - 工具](../04-tools/README.md) | [返回主目录](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由人工智能翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译而成。虽然我们力求准确，但请注意，自动翻译可能存在错误或不准确之处。以原始语言的原文文件为权威版本。对于重要信息，建议采用专业人工翻译。因使用本翻译而引起的任何误解或误释，我们概不负责。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->