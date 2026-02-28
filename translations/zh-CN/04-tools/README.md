# 模块 04：带工具的 AI 代理

## 目录

- [你将学到什么](../../../04-tools)
- [先决条件](../../../04-tools)
- [理解带工具的 AI 代理](../../../04-tools)
- [工具调用是如何工作的](../../../04-tools)
  - [工具定义](../../../04-tools)
  - [决策](../../../04-tools)
  - [执行](../../../04-tools)
  - [响应生成](../../../04-tools)
  - [架构：Spring Boot 自动装配](../../../04-tools)
- [工具链](../../../04-tools)
- [运行应用](../../../04-tools)
- [使用应用](../../../04-tools)
  - [尝试简单工具使用](../../../04-tools)
  - [测试工具链](../../../04-tools)
  - [查看会话流程](../../../04-tools)
  - [尝试不同请求](../../../04-tools)
- [关键概念](../../../04-tools)
  - [ReAct 模式（推理与行动）](../../../04-tools)
  - [工具描述很重要](../../../04-tools)
  - [会话管理](../../../04-tools)
  - [错误处理](../../../04-tools)
- [可用工具](../../../04-tools)
- [何时使用基于工具的代理](../../../04-tools)
- [工具 vs RAG](../../../04-tools)
- [下一步](../../../04-tools)

## 你将学到什么

到目前为止，你已经学会了如何与 AI 进行对话、有效构建提示，并将回答依托于你的文档内容。但仍存在一个根本限制：语言模型只能生成文本。它们不能查询天气、执行计算、访问数据库，或与外部系统交互。

工具改变了这一切。通过赋予模型调用函数的能力，你将它从文本生成器转变为可以执行动作的代理。模型决定何时需要工具、使用哪个工具以及传递什么参数。你的代码执行该函数并返回结果。模型将该结果纳入其回答中。

## 先决条件

- 完成模块 01（Azure OpenAI 资源已部署）
- 根目录下存在 `.env` 文件，包含 Azure 凭证（由模块 01 的 `azd up` 创建）

> **注意：** 如果你还没有完成模块 01，请先按照那里的部署说明进行操作。

## 理解带工具的 AI 代理

> **📝 注意：** 本模块中的“代理”一词指的是增强了调用工具能力的 AI 助手。这与我们将在[模块 05：MCP](../05-mcp/README.md)中介绍的具有规划、记忆和多步推理的自主代理型 AI (**Agentic AI**) 模式不同。

没有工具时，语言模型只能基于训练数据生成文本。问它当前天气，它只能猜测。提供工具后，它可以调用天气 API、执行计算或查询数据库——然后将这些真实结果融入回答中。

<img src="../../../translated_images/zh-CN/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*没有工具时，模型只能猜测——有了工具，它可以调用 API、运行计算，并返回实时数据。*

带工具的 AI 代理遵循一种 **推理与行动（ReAct）** 模式。模型不仅仅是回答——它思考自己需要什么，采取行动调用工具，观察结果，然后决定是继续行动还是给出最终答案：

1. **推理** — 代理分析用户问题，确定所需信息
2. **行动** — 代理选择合适的工具，生成正确参数并调用
3. **观察** — 代理接收工具输出并评估结果
4. **重复或答复** — 若还需更多数据，代理复回步骤；否则构造自然语言答案

<img src="../../../translated_images/zh-CN/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct 循环——代理推理需要做什么，通过调用工具采取行动，观察结果，并循环，直到给出最终答案。*

这一过程是自动进行的。你定义工具及其描述，模型负责决策何时以及如何使用它们。

## 工具调用是如何工作的

### 工具定义

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

你定义具有清晰描述和参数规范的函数。模型在系统提示中看到这些描述，理解每个工具的功能。

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // 你的天气查询逻辑
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// Assistant 由 Spring Boot 自动连接：
// - ChatModel bean
// - 来自 @Component 类的所有 @Tool 方法
// - 用于会话管理的 ChatMemoryProvider
```

下图详细解析了所有注解，展示了每一部分如何帮助 AI 理解何时调用工具，以及传递哪些参数：

<img src="../../../translated_images/zh-CN/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*工具定义结构——@Tool 告诉 AI 何时使用，@P 描述每个参数，@AiService 在启动时将所有内容自动装配。*

> **🤖 通过 [GitHub Copilot](https://github.com/features/copilot) 聊天试试：**打开 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) 并提问：
> - “如果使用真实天气 API（如 OpenWeatherMap）替代模拟数据，如何集成？”
> - “怎样的工具描述才能帮助 AI 正确使用工具？”
> - “如何在工具实现中处理 API 错误和频率限制？”

### 决策

当用户问“西雅图的天气怎么样？”时，模型不会随机选工具。它会将用户意图与所有工具描述对比，评估相关性打分，选择最匹配的那个。然后生成结构化的函数调用及正确参数——此处是设置 `location` 为 `"Seattle"`。

若无工具匹配用户请求，模型则退回到自身知识回答。若多工具匹配，则选最具体的。

<img src="../../../translated_images/zh-CN/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*模型评估每个可用工具与用户意图的匹配度，选择最佳匹配——因此编写清晰、具体的工具描述至关重要。*

### 执行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 自动装配声明式的 `@AiService` 接口及所有注册的工具，LangChain4j 自动执行工具调用。在后台，一个完整的工具调用经历六个阶段——从用户的自然语言问题，到最终的自然语言答案：

<img src="../../../translated_images/zh-CN/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*端到端流程——用户提问，模型选工具，LangChain4j 执行，模型将结果编织成自然回答。*

> **🤖 通过 [GitHub Copilot](https://github.com/features/copilot) 聊天试试：**打开 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) 并提问：
> - “ReAct 模式是如何工作的，它为何对 AI 代理有效？”
> - “代理如何决定使用哪个工具及调用顺序？”
> - “如果工具执行失败，会怎样？如何健壮地处理错误？”

### 响应生成

模型接收天气数据并将其格式化为用户的自然语言回答。

### 架构：Spring Boot 自动装配

本模块使用 LangChain4j 的 Spring Boot 集成，基于声明式的 `@AiService` 接口。启动时，Spring Boot 发现所有包含 `@Tool` 方法的 `@Component`，你的 `ChatModel` Bean 和 `ChatMemoryProvider` ——然后将它们全部装配到单一的 `Assistant` 接口，无需任何样板代码。

<img src="../../../translated_images/zh-CN/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService 接口将 ChatModel、工具组件和内存提供者串联在一起——Spring Boot 自动处理所有装配。*

此方法的主要优点：

- **Spring Boot 自动装配** —— ChatModel 和工具自动注入
- **@MemoryId 模式** —— 自动基于会话管理记忆
- **单实例** —— Assistant 仅创建一次，提升性能
- **类型安全执行** —— Java 方法直接调用并自动类型转换
- **多轮编排** —— 自动处理工具链调用
- **零样板代码** —— 无需手动调用 `AiServices.builder()` 或内存 HashMap

替代方案（手动 `AiServices.builder()`）需要更多代码，且无法享受 Spring Boot 集成优势。

## 工具链

**工具链** —— 基于工具的代理真正强大之处在于单个问题需使用多个工具。问“西雅图的天气是多少华氏度？”时，代理会自动链式调用两个工具：先调用 `getCurrentWeather` 获取摄氏温度，然后将结果传给 `celsiusToFahrenheit` 转换——所有在一次对话轮次内完成。

<img src="../../../translated_images/zh-CN/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*工具链演示——代理先调用 getCurrentWeather，然后将摄氏温度传递给 celsiusToFahrenheit，给出综合答案。*

以下是实际应用中的效果——代理在一次对话轮次内链式调用两个工具：

<a href="images/tool-chaining.png"><img src="../../../translated_images/zh-CN/tool-chaining.3b25af01967d6f7b.webp" alt="Tool Chaining" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*实际应用输出——代理自动链式调用 getCurrentWeather → celsiusToFahrenheit，一次完成。*

**优雅失败** —— 若请求一个模拟数据中不存在的城市天气，工具返回错误信息，AI 会说明无法提供帮助，而不会崩溃。工具失败时安全处理。

<img src="../../../translated_images/zh-CN/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*工具失败时，代理捕捉错误并以有用的说明回应，而非崩溃。*

这都在一次对话轮次内完成。代理自主编排多个工具调用。

## 运行应用

**验证部署：**

确保根目录存在 `.env` 文件，包含 Azure 凭证（模块 01 创建）：
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用：**

> **注意：** 如果你已经通过模块 01 的 `./start-all.sh` 启动了所有应用，本模块已在端口 8084 运行。可以跳过以下启动命令，直接访问 http://localhost:8084 。

**选项 1：使用 Spring Boot Dashboard（VS Code 用户推荐）**

开发容器内置了 Spring Boot Dashboard 扩展，提供可视化界面来管理所有 Spring Boot 应用。你可以在 VS Code 左侧活动栏找到它（寻找 Spring Boot 图标）。

通过 Spring Boot Dashboard， 你可以：
- 查看工作区内所有可用 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监控应用状态

点击 “tools” 旁的播放按钮即可启动此模块，或一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**选项 2：使用 shell 脚本**

启动所有 Web 应用（模块 01-04）：

**Bash:**
```bash
cd ..  # 从根目录
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 从根目录
.\start-all.ps1
```

或单独启动本模块：

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

两个脚本都会自动从根目录 `.env` 加载环境变量，如果 JAR 不存在会自动构建。

> **注意：** 如果你想先手动构建所有模块再启动：
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

在浏览器打开 http://localhost:8084 。

**停止命令：**

**Bash:**
```bash
./stop.sh  # 仅此模块
# 或
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell:**
```powershell
.\stop.ps1  # 仅此模块
# 或
cd ..; .\stop-all.ps1  # 所有模块
```

## 使用应用

该应用提供一个网页界面，让你与具备天气和温度转换工具的 AI 代理交互。

<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-CN/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI 代理工具界面 - 快速示例和聊天界面，用于与工具交互*

### 尝试简单工具使用
开始一个简单请求：“将华氏100度转换为摄氏度”。代理识别到需要温度转换工具，调用它并传入正确参数，然后返回结果。注意这过程多么自然——你没指定用哪个工具或者如何调用它。

### 工具链测试

现在尝试更复杂的请求：“西雅图今天天气如何，并将其转换为华氏度？”观察代理如何分步处理。它先获取天气（返回摄氏度），识别到需要转换为华氏度，调用转换工具，并将两个结果合并成一个回复。

### 查看对话流程

聊天界面保留了对话历史，使你能进行多轮交互。你可以看到所有之前的问题和回答，便于追踪对话内容，理解代理如何通过多轮累积构建上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-CN/tools-conversation-demo.89f2ce9676080f59.webp" alt="多工具调用的对话示例" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多轮对话示例，展示简单转换、天气查询及工具链调用*

### 试验不同请求

尝试多种组合：
- 天气查询：“东京今天天气怎么样？”
- 温度转换：“25°C是多少开尔文？”
- 组合查询：“查查巴黎天气，告诉我是否超过20°C”

留意代理如何理解自然语言并映射到适合的工具调用。

## 关键概念

### ReAct 模式（推理与行动）

代理在推理（决定下一步动作）与行动（调用工具）之间切换。该模式支持自主解决问题，而不只是被动执行指令。

### 工具描述很重要

工具描述质量直接影响代理调用效果。清晰且具体的描述，帮助模型理解何时以及如何调用每个工具。

### 会话管理

`@MemoryId` 注解实现基于会话的自动内存管理。每个会话ID对应一个由 `ChatMemoryProvider` 管理的独立 `ChatMemory` 实例，支持多用户同时与代理交互且对话内容不混淆。

<img src="../../../translated_images/zh-CN/session-management.91ad819c6c89c400.webp" alt="利用 @MemoryId 进行会话管理" width="800"/>

*每个会话ID映射到独立的对话历史——用户之间消息互不干扰。*

### 错误处理

工具可能失败——API超时、参数无效、外部服务不可用。生产环境代理需要错误处理机制，让模型能解释问题或尝试备选方案，而非整个应用崩溃。当工具抛出异常，LangChain4j 会捕获并将错误信息反馈给模型，模型随后用自然语言说明问题。

## 可用工具

下图展示了你可构建的工具生态系统。本模块演示天气和温度工具，但相同的 `@Tool` 模式适用于任意Java方法——无论是数据库查询还是支付处理。

<img src="../../../translated_images/zh-CN/tool-ecosystem.aad3d74eaa14a44f.webp" alt="工具生态系统" width="800"/>

*任何带有 @Tool 注解的Java方法都能供AI调用——此模式可扩展至数据库、API、邮件、文件操作等。*

## 何时使用基于工具的代理

<img src="../../../translated_images/zh-CN/when-to-use-tools.51d1592d9cbdae9c.webp" alt="何时使用工具" width="800"/>

*快速决策指南——工具适合实时数据、计算和操作需求，常识和创意任务无需工具。*

**适用工具场景：**
- 回答需实时数据（天气、股价、库存）
- 需要复杂计算
- 访问数据库或API
- 执行操作（发邮件、建工单、更新记录）
- 综合多个数据源

**不适用工具场景：**
- 问题可用通用知识回答
- 回复纯粹是对话式
- 工具延迟会影响体验流畅度

## 工具与RAG对比

模块03和04均扩展了AI能力，但方式根本不同。RAG通过检索文档给模型提供**知识**。工具则通过调用函数赋予模型执行**操作**的能力。

<img src="../../../translated_images/zh-CN/tools-vs-rag.ad55ce10d7e4da87.webp" alt="工具与RAG对比" width="800"/>

*RAG从静态文档检索信息——工具执行操作并获取动态实时数据。许多生产系统将二者结合使用。*

实际场景中，很多系统同时用RAG和工具组合：RAG用于根据文档提供答案，工具用于获取实时数据或执行操作。

## 下一步

**下一个模块:** [05-mcp - 模型上下文协议（MCP）](../05-mcp/README.md)

---

**导航：** [← 上一个：模块03 - RAG](../03-rag/README.md) | [返回首页](../README.md) | [下一个：模块05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文档由AI翻译服务[Co-op Translator](https://github.com/Azure/co-op-translator)翻译而成。虽然我们力求准确，但请注意，自动翻译可能存在错误或不准确之处。请以原语言版本的文档作为权威来源。对于重要信息，建议使用专业人工翻译。对于因使用本翻译而引起的任何误解或误释，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->