# Module 04: 带工具的 AI 代理

## 目录

- [你将学到什么](../../../04-tools)
- [先决条件](../../../04-tools)
- [理解带工具的 AI 代理](../../../04-tools)
- [工具调用如何工作](../../../04-tools)
  - [工具定义](../../../04-tools)
  - [决策过程](../../../04-tools)
  - [执行](../../../04-tools)
  - [响应生成](../../../04-tools)
  - [架构：Spring Boot 自动装配](../../../04-tools)
- [工具链](../../../04-tools)
- [运行应用程序](../../../04-tools)
- [使用应用程序](../../../04-tools)
  - [尝试简单工具使用](../../../04-tools)
  - [测试工具链](../../../04-tools)
  - [查看对话流程](../../../04-tools)
  - [尝试不同请求](../../../04-tools)
- [关键概念](../../../04-tools)
  - [ReAct 模式（推理与行动）](../../../04-tools)
  - [工具描述的重要性](../../../04-tools)
  - [会话管理](../../../04-tools)
  - [错误处理](../../../04-tools)
- [可用工具](../../../04-tools)
- [何时使用基于工具的代理](../../../04-tools)
- [工具与 RAG 的比较](../../../04-tools)
- [下一步](../../../04-tools)

## 你将学到什么

到目前为止，你已经学会了如何与 AI 对话，如何有效构建提示，以及如何让回答基于你的文档作为依据。但这里仍然存在一个根本性限制：语言模型只能生成文本。它们无法查询天气、进行计算、访问数据库或与外部系统交互。

工具改变了这一点。通过给模型提供可以调用的函数，你将其从文本生成器转变为能够执行行动的代理。模型决定何时需要工具，选择哪个工具，以及传入什么参数。你的代码会执行函数并返回结果。模型将结果融入其回答中。

## 先决条件

- 完成了 [Module 01 - Introduction](../01-introduction/README.md)（部署了 Azure OpenAI 资源）
- 建议完成之前的模块（本模块中对比了工具与 RAG，参考了[Module 03 中的 RAG 概念](../03-rag/README.md)）
- 根目录含有带有 Azure 凭据的 `.env` 文件（由 Module 01 中的 `azd up` 创建）

> **注意：** 如果你还未完成 Module 01，请先按照那里提供的部署说明进行操作。

## 理解带工具的 AI 代理

> **📝 注意：** 本模块中的“代理”指的是增强了工具调用功能的 AI 助手。这与我们将在 [Module 05: MCP](../05-mcp/README.md) 中介绍的 **Agentic AI** 模式（具有规划、记忆和多步推理的自主代理）不同。

没有工具时，语言模型只能根据其训练数据生成文本。问它当前天气，只能猜测。给它工具，它就可以调用天气 API、进行计算，或查询数据库——然后将这些真实结果编织进回答中。

<img src="../../../translated_images/zh-CN/what-are-tools.724e468fc4de64da.webp" alt="无工具与有工具" width="800"/>

*无工具下模型只能猜测——有了工具则可调用 API、执行计算并返回实时数据。*

带工具的 AI 代理遵循一种 **推理与行动（Reasoning and Acting, ReAct）** 模式。模型不仅仅回应，而是思考所需内容，采取行动调用工具，观察结果，然后决定是重新行动还是给出最终答案：

1. **推理** — 代理分析用户问题，确定所需信息
2. **行动** — 代理选择合适的工具，生成正确参数，并调用该工具
3. **观察** — 代理接收工具输出，评估结果
4. **重复或回应** — 若需更多数据，循环返回步骤；否则生成自然语言回答

<img src="../../../translated_images/zh-CN/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct 模式" width="800"/>

*ReAct 循环——代理推理下一步行动，通过调用工具执行，观察结果，循环直至给出最终回复。*

这一过程是自动的。你定义工具及其描述，模型负责决定何时以及如何使用它们。

## 工具调用如何工作

### 工具定义

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

你定义带有清晰描述和参数规范的函数。模型在其系统提示中看到这些描述，并理解每个工具的用途。

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

// 助手由 Spring Boot 自动连接：
// - ChatModel bean
// - 来自 @Component 类的所有 @Tool 方法
// - 用于会话管理的 ChatMemoryProvider
```


下图详解了每个注解，以及它们如何帮助 AI 明白何时调用工具及传递哪些参数：

<img src="../../../translated_images/zh-CN/tool-definitions-anatomy.f6468546037cf28b.webp" alt="工具定义构成" width="800"/>

*工具定义构成——@Tool 告诉 AI 何时使用，@P 描述各参数，@AiService 在启动时连接所有部分。*

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 试试：** 打开 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java)，提出：
> - “如何用像 OpenWeatherMap 这样的真实天气 API 替代模拟数据？”
> - “什么样的工具描述才能帮助 AI 正确使用它？”
> - “工具实现中如何处理 API 错误和速率限制？”

### 决策过程

当用户问“西雅图天气如何？”时，模型不会随意选择工具。它会将用户意图与所有工具描述进行匹配，为每个工具打分，并选出最符合的那个。然后生成带有正确参数的结构化函数调用——此例中，将 `location` 设置为 `"Seattle"`。

如果没有工具匹配用户请求，模型则退回自身知识回答。若多工具匹配，则选择最具体的一个。

<img src="../../../translated_images/zh-CN/decision-making.409cd562e5cecc49.webp" alt="AI 如何决定使用哪个工具" width="800"/>

*模型对所有可用工具与用户意图进行评估，选择最佳匹配——这也是为何清晰具体的工具描述至关重要。*

### 执行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 自动装配所有带有 `@AiService` 接口的已注册工具，LangChain4j 自动执行工具调用。幕后完整调用过程由六个阶段组成——从用户自然语言问题到自然语言回答：

<img src="../../../translated_images/zh-CN/tool-calling-flow.8601941b0ca041e6.webp" alt="工具调用流程" width="800"/>

*端到端流程——用户提问，模型选择工具，LangChain4j 执行工具，模型将结果编入自然回答。*

如果你在 Module 00 中运行过 [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，已经见识了这一模式——`Calculator` 工具的调用方式相同。下图时序图展示该示例背后的具体流程：

<img src="../../../translated_images/zh-CN/tool-calling-sequence.94802f406ca26278.webp" alt="工具调用时序图" width="800"/>

*快起步演示中的工具调用循环——`AiServices` 将你的消息和工具模式发给 LLM，LLM 回复函数调用如 `add(42, 58)`，LangChain4j 本地执行 `Calculator` 方法，并将结果反馈给最终回答。*

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 试试：** 打开 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)，提问：
> - “ReAct 模式如何工作，为什么对 AI 代理有效？”
> - “代理如何决定使用哪个工具及调用顺序？”
> - “如果工具执行失败，我应如何稳健地处理错误？”

### 响应生成

模型接收天气数据并将其格式化为针对用户的自然语言响应。

### 架构：Spring Boot 自动装配

本模块采用 LangChain4j 的 Spring Boot 集成及声明式 `@AiService` 接口。启动时，Spring Boot 自动发现所有包含 `@Tool` 方法的 `@Component`，你的 `ChatModel` bean 以及 `ChatMemoryProvider` ——然后全部连接为单一 `Assistant` 接口，免去样板代码。

<img src="../../../translated_images/zh-CN/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot 自动装配架构" width="800"/>

*@AiService 接口将 ChatModel、工具组件和内存提供者连接起来——Spring Boot 自动处理所有装配。*

完整请求生命周期如下时序图——从 HTTP 请求进入，经过控制器、服务和自动装配的代理，一直到工具执行并返回：

<img src="../../../translated_images/zh-CN/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot 工具调用时序" width="800"/>

*完整的 Spring Boot 请求生命周期——HTTP 请求通过控制器和服务，进入自动装配的 Assistant 代理，自动组织调用 LLM 和工具。*

该方案的核心优势：

- **Spring Boot 自动装配** — 自动注入 ChatModel 和工具
- **@MemoryId 模式** — 自动的基于会话的记忆管理
- **单例实例** — Assistant 只创建一次，提升性能
- **类型安全执行** — Java 方法直接调用并自动类型转换
- **多轮编排** — 自动处理工具链调用
- **零样板** — 无需手动调用 `AiServices.builder()` 或管理内存 HashMap

手动调用 `AiServices.builder()` 的替代方案代码量多，且无法享受 Spring Boot 集成优势。

## 工具链

**工具链** — 基于工具的代理真正强大之处在于单个问题可能涉及多个工具。问“西雅图的天气是多少华氏度？”时，代理自动串联两个工具：先调用 `getCurrentWeather` 获取摄氏温度，再传给 `celsiusToFahrenheit` 转换温度——全部在一次对话轮内完成。

<img src="../../../translated_images/zh-CN/tool-chaining-example.538203e73d09dd82.webp" alt="工具链示例" width="800"/>

*工具链实战——代理先调用 getCurrentWeather，然后将摄氏度结果传入 celsiusToFahrenheit，给出综合答案。*

**优雅失败** — 当询问不在模拟数据中的城市天气时，工具返回错误消息，AI 解释无法提供帮助而不是崩溃。工具失败时安全处理。下图对比了两种情况——正确错误处理时代理捕获异常并给出帮助性响应，而未处理时整个应用崩溃：

<img src="../../../translated_images/zh-CN/error-handling-flow.9a330ffc8ee0475c.webp" alt="错误处理流程" width="800"/>

*工具失败时，代理捕获错误并返回易于理解的说明，防止应用崩溃。*

这一切都在一次对话轮内完成。代理自主编排多次工具调用。

## 运行应用程序

**验证部署：**

确认根目录含有带 Azure 凭据的 `.env` 文件（在 Module 01 中创建）。在本模块目录（`04-tools/`）下执行：

**Bash:**
```bash
cat ../.env  # 应该显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用程序：**

> **注意：** 如果你已通过根目录下的 `./start-all.sh` 启动所有模块（如 Module 01 说明），本模块已运行在端口 8084。你可以跳过启动命令，直接访问 http://localhost:8084 。

**方案 1：使用 Spring Boot Dashboard（推荐 VS Code 用户）**

开发容器中包含 Spring Boot Dashboard 扩展，提供视觉界面管理所有 Spring Boot 应用。你可以在 VS Code 左侧的活动栏中找到它（寻找 Spring Boot 图标）。

通过 Spring Boot Dashboard，你可以：
- 查看工作区内所有可用的 Spring Boot 应用
- 一键启动/停止应用
- 实时查看日志
- 监控应用状态

点击“tools”旁边的运行按钮即可启动本模块，或一次启动所有模块。

Spring Boot Dashboard 在 VS Code 中的样子如下：

<img src="../../../translated_images/zh-CN/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot Dashboard——集中启动、停止和监控所有模块*

**方案 2：使用 shell 脚本**

启动所有 Web 应用（01-04 模块）：

**Bash:**
```bash
cd ..  # 从根目录
./start-all.sh
```

**PowerShell：**
```powershell
cd ..  # 来自根目录
.\start-all.ps1
```

或者仅启动此模块：

**Bash：**
```bash
cd 04-tools
./start.sh
```

**PowerShell：**
```powershell
cd 04-tools
.\start.ps1
```

这两个脚本会自动从根目录的 `.env` 文件加载环境变量，如果 JAR 文件不存在，还会构建它们。

> **注意：** 如果你更喜欢先手动构建所有模块再启动：
>
> **Bash：**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell：**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

在浏览器中打开 http://localhost:8084 。

**停止运行：**

**Bash：**
```bash
./stop.sh  # 仅此模块
# 或
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell：**
```powershell
.\stop.ps1  # 仅此模块
# 或
cd ..; .\stop-all.ps1  # 所有模块
```

## 使用应用程序

该应用提供了一个网页界面，你可以通过它与具有天气和温度转换工具的 AI 代理交互。界面大致如下——包含快速启动示例和发送请求的聊天面板：

<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-CN/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI 代理工具界面 - 快速示例和交互式聊天工具*

### 尝试简单的工具使用

从一个简单请求开始：“将100华氏度转换为摄氏度”。代理能识别出需要使用温度转换工具，调用正确参数，返回结果。注意这感觉多么自然——你并没有指定使用哪个工具或如何调用它。

### 测试工具链

现在试试更复杂的请求：“西雅图的天气怎么样，并将它转换为华氏度？”观察代理分步骤处理。它先获取天气（返回摄氏度），识别出需要转换为华氏度，调用转换工具，然后将两个结果合并成一个响应。

### 查看对话流程

聊天界面会保存对话历史，允许你进行多轮交流。你可以看到之前的所有查询和回复，方便跟踪对话，了解代理如何在多轮交互中构建上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-CN/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多轮对话示例，显示简单转换、天气查询和工具链调用*

### 试验多样请求

尝试各种组合：
- 天气查询：“东京的天气怎么样？”
- 温度转换：“25摄氏度是多少开尔文？”
- 复合查询：“查一下巴黎的天气，告诉我是否超过20摄氏度”

注意代理如何理解自然语言并映射到合适的工具调用。

## 关键概念

### ReAct 模式（推理与执行）

代理在推理（决定做什么）和执行（调用工具）之间交替。该模式支持自主解决问题，而不仅仅是回应指令。

### 工具描述的重要性

工具描述的质量直接影响代理使用效果。清晰、具体的描述帮助模型理解何时以及如何调用每个工具。

### 会话管理

`@MemoryId` 注解启用自动基于会话的记忆管理。每个会话 ID 都有自己的 `ChatMemory` 实例，由 `ChatMemoryProvider` bean 管理，多个用户可以同时与代理交互且互不干扰。下图展示了根据会话 ID 将多个用户路由到独立内存存储的方式：

<img src="../../../translated_images/zh-CN/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*每个会话 ID 关联一个独立的对话历史——用户之间互不干扰。*

### 错误处理

工具可能失败——API 超时、参数无效、外部服务故障。生产环境中的代理需要错误处理，以便模型可以解释问题或尝试替代方案，而不是让整个应用崩溃。当工具抛出异常时，LangChain4j 会捕获并将错误信息反馈给模型，模型随后可以用自然语言解释问题。

## 可用工具

下图展示了你可以构建的广泛工具生态。本模块演示了天气和温度工具，但相同的 `@Tool` 模式适用于任何 Java 方法——从数据库查询到支付处理。

<img src="../../../translated_images/zh-CN/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*任何带有 @Tool 注解的 Java 方法都可以供 AI 调用——该模式扩展到数据库、API、邮件、文件操作等。*

## 何时使用基于工具的代理

并非所有请求都需要工具。决策取决于 AI 是否需要交互外部系统，还是能用自身知识回答。下图总结了何时工具有价值，何时不必要：

<img src="../../../translated_images/zh-CN/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*快速决策指南——工具适用于实时数据、计算和操作；一般知识和创造性任务不需要。*

## 工具 vs RAG

03 和 04 两个模块都扩展了 AI 能力，但方式根本不同。RAG 通过检索文件赋予模型访问**知识**的能力。工具通过调用函数赋予模型执行**操作**的能力。下图对比了两种方式的流程和权衡：

<img src="../../../translated_images/zh-CN/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG 从静态文档检索信息——工具执行操作并获取动态实时数据。许多生产系统结合两者使用。*

实际生产中，很多系统结合两者：RAG 用于基于文档构建答案，工具则用于抓取实时数据或执行操作。

## 后续步骤

**下一个模块：** [05-mcp - 模型上下文协议 (MCP)](../05-mcp/README.md)

---

**导航：** [← 上一节：模块 03 - RAG](../03-rag/README.md) | [返回主页](../README.md) | [下一节：模块 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译而成。尽管我们努力确保准确性，但请注意自动翻译可能存在错误或不准确之处。原始文档的原文版本应视为权威来源。对于重要信息，建议使用专业人工翻译。因使用本翻译所引起的任何误解或错误理解，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->