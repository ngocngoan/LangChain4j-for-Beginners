# 模块 04：带工具的 AI 代理

## 目录

- [你将学到什么](../../../04-tools)
- [前提条件](../../../04-tools)
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
  - [尝试简单的工具使用](../../../04-tools)
  - [测试工具链](../../../04-tools)
  - [查看对话流程](../../../04-tools)
  - [尝试不同请求](../../../04-tools)
- [关键概念](../../../04-tools)
  - [ReAct 模式（推理与行动）](../../../04-tools)
  - [工具描述很重要](../../../04-tools)
  - [会话管理](../../../04-tools)
  - [错误处理](../../../04-tools)
- [可用工具](../../../04-tools)
- [何时使用基于工具的代理](../../../04-tools)
- [工具与 RAG 的比较](../../../04-tools)
- [下一步](../../../04-tools)

## 你将学到什么

到目前为止，你已经学会了如何与 AI 对话，有效结构化提示，并将响应基于你的文档内容。但仍存在一个根本性限制：语言模型只能生成文本。它们无法查询天气、执行计算、查询数据库或与外部系统交互。

工具改变了这一点。通过赋予模型可调用的函数访问权限，你将其从文本生成器转变为可以采取行动的代理。模型决定何时需要工具，使用哪个工具，以及传递什么参数。你的代码执行该函数并返回结果。模型将该结果整合到响应中。

## 前提条件

- 已完成 [模块 01 - 介绍](../01-introduction/README.md)（已部署 Azure OpenAI 资源）
- 推荐完成之前的模块（本模块在“工具与 RAG 比较”中引用了[模块 03 的 RAG 概念](../03-rag/README.md)）
- 根目录下存在 `.env` 文件并包含 Azure 凭据（由模块 01 中的 `azd up` 创建）

> **注意：** 如果还未完成模块 01，请先按照那里提供的部署说明进行操作。

## 理解带工具的 AI 代理

> **📝 注意：** 本模块中“代理”一词指带有工具调用能力的 AI 助手。这与我们将在[模块 05: MCP](../05-mcp/README.md)介绍的 **Agentic AI** 模式（具备规划、记忆和多步推理的自主代理）不同。

没有工具时，语言模型只能根据训练数据生成文本。问它当前天气，它必须猜测。给它工具，它可以调用天气 API、执行计算或查询数据库，然后将真实结果融入回答。

<img src="../../../translated_images/zh-CN/what-are-tools.724e468fc4de64da.webp" alt="无工具与有工具对比" width="800"/>

*无工具时模型只能猜测——有工具时它可以调用 API、运行计算并返回实时数据。*

带工具的 AI 代理遵循一个 **推理与行动（ReAct）** 模式。模型不仅仅响应——它思考所需内容，通过调用工具来行动，观察结果，继而决定是否再次行动或给出最终答案：

1. **推理** — 代理分析用户问题，确定所需信息
2. **行动** — 代理选择合适工具，生成正确参数并调用
3. **观察** — 代理接收工具输出并评估结果
4. **重复或响应** — 若需更多数据，则循环；否则生成自然语言回答

<img src="../../../translated_images/zh-CN/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct 模式" width="800"/>

*ReAct 循环——代理推理应该做什么，通过调用工具采取行动，观察结果，并循环直至输出最终答案。*

这一过程自动完成。你定义工具及其描述，模型负责决策何时以及如何使用它们。

## 工具调用如何工作

### 工具定义

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

你定义带有清晰描述和参数规范的函数。模型在系统提示中看到这些描述，明白每个工具的作用。

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

// Assistant 被 Spring Boot 自动连接，包含：
// - ChatModel bean
// - 所有来自 @Component 类的 @Tool 方法
// - 用于会话管理的 ChatMemoryProvider
```

下方图示拆解了每个注解，展示每部分如何帮助 AI 理解何时调用工具及传递哪些参数：

<img src="../../../translated_images/zh-CN/tool-definitions-anatomy.f6468546037cf28b.webp" alt="工具定义结构" width="800"/>

*工具定义结构——@Tool 告诉 AI 何时使用，@P 描述每个参数，@AiService 在启动时将所有组件自动装配。*

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java)，并询问：
> - “如何集成像 OpenWeatherMap 这样的真实天气 API，而非模拟数据？”
> - “什么样的工具描述能帮助 AI 正确使用它？”
> - “如何在工具实现中处理 API 错误和调用频率限制？”

### 决策过程

当用户问 “西雅图的天气怎么样？” 时，模型不会随机选择工具。它会将用户意图与所有工具描述进行匹配，给每个工具进行相关性评分，选择最佳匹配。再生成带有正确参数的结构化函数调用——在此例中，设置 `location` 为 `"Seattle"`。

如果无工具匹配用户请求，模型则回退到自身知识库回答。若多个工具匹配，则选择最具体的一个。

<img src="../../../translated_images/zh-CN/decision-making.409cd562e5cecc49.webp" alt="AI 如何决定使用哪个工具" width="800"/>

*模型针对用户意图评估所有可用工具并选择最佳匹配——这就是为何撰写清晰具体的工具描述很重要。*

### 执行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 自动装配声明式 `@AiService` 接口中所有已注册工具，LangChain4j 自动执行工具调用。在后台，完整的工具调用经过六个阶段——从用户自然语言问题到最终的自然语言答案：

<img src="../../../translated_images/zh-CN/tool-calling-flow.8601941b0ca041e6.webp" alt="工具调用流程" width="800"/>

*端到端流程——用户提问，模型选择工具，LangChain4j 执行调用，模型将结果编织成自然回应。*

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)，并询问：
> - “ReAct 模式如何工作，为什么它对 AI 代理有效？”
> - “代理如何决定使用哪个工具以及使用顺序？”
> - “如果工具执行失败了会怎么样——我该如何稳健地处理错误？”

### 响应生成

模型接收天气数据，并格式化为自然语言响应给用户。

### 架构：Spring Boot 自动装配

本模块使用 LangChain4j 的 Spring Boot 集成，配合声明式的 `@AiService` 接口。启动时，Spring Boot 会发现所有包含 `@Tool` 方法的 `@Component`、你的 `ChatModel` Bean 以及 `ChatMemoryProvider`，然后将它们全部装配进单个 `Assistant` 接口，省去任何样板代码。

<img src="../../../translated_images/zh-CN/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot 自动装配架构" width="800"/>

*@AiService 接口将 ChatModel、工具组件和内存提供者绑定在一起——Spring Boot 自动完成所有装配。*

此方法的关键好处：

- **Spring Boot 自动装配** —  ChatModel 和工具自动注入
- **@MemoryId 模式** — 自动基于会话的内存管理
- **单实例** — Assistant 创建一次并重用，提高性能
- **类型安全执行** — 直接调用 Java 方法并进行类型转换
- **多轮编排** — 自动处理工具链调用
- **零样板代码** — 无需手动调用 `AiServices.builder()` 或管理内存 HashMap

另一种方法（手动使用 `AiServices.builder()`）需要更多代码，且无法享受 Spring Boot 集成的优势。

## 工具链

**工具链** — 基于工具代理的真正威力体现在单个问题需要多个工具时。问 “西雅图的华氏温度是多少？” 时，代理自动链式调用两个工具：先调用 `getCurrentWeather` 获取摄氏温度，然后将结果传递给 `celsiusToFahrenheit` 转换——均在单次对话轮次内完成。

<img src="../../../translated_images/zh-CN/tool-chaining-example.538203e73d09dd82.webp" alt="工具链示例" width="800"/>

*工具链实战——代理先调用 getCurrentWeather，再将摄氏温度结果传入 celsiusToFahrenheit，最终给出合并答案。*

**优雅失败** — 查询模拟数据中不存在的城市天气时，工具返回错误消息，AI 会说明无法帮助，而不是崩溃。工具安全失败。下图对比了两种处理方式——有适当错误处理时，代理捕获异常并做出帮助性回应，否则整个应用崩溃：

<img src="../../../translated_images/zh-CN/error-handling-flow.9a330ffc8ee0475c.webp" alt="错误处理流程" width="800"/>

*工具失败时，代理捕获错误并做出帮助性说明，而非崩溃应用。*

这发生在单轮对话内。代理自主编排多个工具调用。

## 运行应用程序

**验证部署：**

确保根目录存在 `.env` 文件并含 Azure 凭据（由模块 01 创建）。从模块目录 (`04-tools/`) 运行：

**Bash:**
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**启动应用程序：**

> **注意：** 如果你已经使用根目录的 `./start-all.sh` 启动所有应用（模块 01 介绍中提到的），本模块已经在 8084 端口运行。你可以跳过下面的启动命令，直接访问 http://localhost:8084 。

**方案一：使用 Spring Boot Dashboard（推荐 VS Code 用户）**

开发容器包含 Spring Boot Dashboard 扩展，带来可视化管理所有 Spring Boot 应用的界面。可在 VS Code 左侧活动栏找到（寻找 Spring Boot 图标）。

通过 Spring Boot Dashboard 你可以：
- 查看工作区内所有 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监测应用状态

点击“tools”旁的播放按钮启动本模块，或者一次性启动所有模块。

下面是 VS Code 中的 Spring Boot Dashboard 界面：

<img src="../../../translated_images/zh-CN/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot Dashboard——统一启动、停止和监控所有模块。*

**方案二：使用 shell 脚本**

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

或者只启动本模块：

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

两个脚本会自动从根目录 `.env` 文件加载环境变量，如果缺少 JAR 包则自动构建。

> **注意：** 如果你更倾向于先手动构建所有模块再启动：
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

在浏览器打开 http://localhost:8084。

**停止运行：**

**Bash:**
```bash
./stop.sh  # 仅此模块
# 或者
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell:**
```powershell
.\stop.ps1  # 仅此模块
# 或
cd ..; .\stop-all.ps1  # 所有模块
```

## 使用应用程序

该应用提供网页界面，允许你与具备天气和温度转换工具访问权限的 AI 代理交互。界面如下——包括快速启动示例和聊天面板供发送请求：
<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-CN/tools-homepage.4b4cd8b2717f9621.webp" alt="AI代理工具界面" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI代理工具界面——快速示例和用于与工具交互的聊天界面*

### 试用简单工具

从一个简单请求开始：“将100华氏度转换为摄氏度”。代理识别出需要温度转换工具，使用正确参数调用它，并返回结果。注意这种感觉多么自然——你没有指定使用哪个工具，也没有说明如何调用它。

### 测试工具链

现在试试更复杂的：“西雅图的天气如何，并把它转换成华氏度？”观察代理如何分步骤操作。它先获取天气（返回摄氏度），然后识别需要转换成华氏度，调用转换工具，最后将两个结果合并成一个响应。

### 查看对话流程

聊天界面保存对话历史，允许你进行多轮交互。你可以看到之前的所有查询和回复，方便追踪对话并理解代理如何在多次交流中建立上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-CN/tools-conversation-demo.89f2ce9676080f59.webp" alt="多次调用工具的对话示例" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多轮对话展示简单转换、天气查询和工具链使用*

### 试验不同请求

尝试各种组合：
- 天气查询：“东京的天气如何？”
- 温度转换：“25摄氏度是多少开尔文？”
- 组合查询：“查一下巴黎的天气，并告诉我是否超过20摄氏度”

注意代理如何理解自然语言并映射到合适的工具调用。

## 关键概念

### ReAct 模式（推理与行动）

代理在推理（决定做什么）和行动（使用工具）之间交替。这个模式使其具备自治解决问题的能力，而不仅仅是响应指令。

### 工具描述的重要性

工具描述的质量直接影响代理的使用效果。清晰、具体的描述帮助模型理解何时以及如何调用每个工具。

### 会话管理

`@MemoryId` 注解支持自动的基于会话的记忆管理。每个会话ID拥有自己的 `ChatMemory` 实例，由 `ChatMemoryProvider` bean 管理，因此多个用户可以同时与代理交互，且各自的对话不会混淆。下图展示了如何根据会话ID将多个用户路由到隔离的存储：

<img src="../../../translated_images/zh-CN/session-management.91ad819c6c89c400.webp" alt="带 @MemoryId 的会话管理" width="800"/>

*每个会话ID映射到隔离的对话历史——用户永远看不到对方的消息。*

### 错误处理

工具可能失败——API超时、参数无效、外部服务中断。生产环境的代理需要错误处理，以便模型能解释问题或尝试替代方案，而不是让整个应用崩溃。当工具抛出异常时，LangChain4j 捕获并将错误消息反馈给模型，模型随后可用自然语言解释问题。

## 可用工具

下图展示了可构建的广泛工具生态。本模块演示了天气和温度工具，但相同的 `@Tool` 模式适用于任何Java方法——从数据库查询到支付处理。

<img src="../../../translated_images/zh-CN/tool-ecosystem.aad3d74eaa14a44f.webp" alt="工具生态" width="800"/>

*任何用 @Tool 注解的Java方法都可供AI使用——这个模式扩展到数据库、API、邮件、文件操作等。*

## 何时使用基于工具的代理

并非所有请求都需要工具。关键在于AI是否需要与外部系统交互，或仅凭自身知识回答。以下指南总结了工具何时发挥作用，何时无需使用：

<img src="../../../translated_images/zh-CN/when-to-use-tools.51d1592d9cbdae9c.webp" alt="何时使用工具" width="800"/>

*快速决策指南——工具适用于实时数据、计算和操作；通用知识和创意任务则不需要。*

## 工具 VS RAG

第03和04模块都扩展了AI的能力，但方式根本不同。RAG通过检索文档为模型提供**知识**。工具则让模型拥有调用函数进行**行动**的能力。下图对比这两种方法——从各自的工作流程到它们之间的权衡：

<img src="../../../translated_images/zh-CN/tools-vs-rag.ad55ce10d7e4da87.webp" alt="工具与RAG比较" width="800"/>

*RAG从静态文档中检索信息——工具执行操作并获取动态实时数据。许多生产系统结合两者使用。*

实际上，许多生产系统同时采用两种方法：RAG用于在文档中基准答案，工具用于获取实时数据或执行操作。

## 下一步

**下一模块：** [05-mcp - 模型上下文协议 (MCP)](../05-mcp/README.md)

---

**导航：** [← 上一模块：03 - RAG](../03-rag/README.md) | [返回主页](../README.md) | [下一模块：05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译。尽管我们力求准确，但请注意自动翻译可能存在错误或不准确之处。原始语言的文档应被视为权威来源。对于重要信息，建议使用专业人工翻译。对于因使用本翻译而产生的任何误解或误释，我们概不负责。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->