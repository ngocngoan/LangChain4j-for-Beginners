# Module 02: 使用 GPT-5.2 的提示工程

## 目录

- [视频演示](../../../02-prompt-engineering)
- [你将学到什么](../../../02-prompt-engineering)
- [先决条件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基础](../../../02-prompt-engineering)
  - [零次提示](../../../02-prompt-engineering)
  - [少量示例提示](../../../02-prompt-engineering)
  - [链式思维](../../../02-prompt-engineering)
  - [基于角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [高级模式](../../../02-prompt-engineering)
- [使用现有 Azure 资源](../../../02-prompt-engineering)
- [应用截图](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低急切度 vs 高急切度](../../../02-prompt-engineering)
  - [任务执行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代码](../../../02-prompt-engineering)
  - [结构化分析](../../../02-prompt-engineering)
  - [多轮聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限输出](../../../02-prompt-engineering)
- [你真正学到的是什么](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 视频演示

观看此现场直播，了解如何开始本模块：

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## 你将学到什么

<img src="../../../translated_images/zh-CN/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

在上一个模块中，您已经了解了记忆如何支持对话式 AI，并使用了 GitHub 模型进行基本交互。现在我们将重点关注如何提问——即提示本身——利用 Azure OpenAI 的 GPT-5.2。提示的结构对您获得的回答质量有显著影响。我们先回顾基础提示技巧，然后进入利用 GPT-5.2 功能的八个高级模式。

我们使用 GPT-5.2 是因为它引入了推理控制——您可以告诉模型在回答之前需要进行多少思考。这让不同的提示策略更清晰，也帮助您理解何时使用哪种方法。相比 GitHub 模型，Azure 对 GPT-5.2 的调用限制更少，这也是优势之一。

## 先决条件

- 完成模块 01 （已部署 Azure OpenAI 资源）
- 根目录下含有包含 Azure 凭据的 `.env` 文件（由模块 01 中的 `azd up` 创建）

> **注意：** 如果尚未完成模块 01，请先按照那里部署说明操作。

## 理解提示工程

<img src="../../../translated_images/zh-CN/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

提示工程就是设计输入文本，以持续获得所需结果。它不仅仅是提问，而是构造请求，让模型准确理解您的需求及交付方式。

把它想象成给同事下指令。“修复bug”很模糊。而“修复 UserService.java 第 45 行的空指针异常，添加空值检测”就很具体。语言模型也是如此——具体和结构化很重要。

<img src="../../../translated_images/zh-CN/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j 提供了基础设施——模型连接、记忆和消息类型——而提示模式只是您通过这些基础设施发送的精心结构化文本。关键构建模块是 `SystemMessage`（设定 AI 的行为与角色）和 `UserMessage`（承载您的实际请求）。

## 提示工程基础

<img src="../../../translated_images/zh-CN/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

在深入本模块的高级模式之前，先复习五种基础提示技巧。这些是每位提示工程师都应该掌握的构建基石。如果您已经学习过[快速入门模块](../00-quick-start/README.md#2-prompt-patterns)，那就见过这些的实际应用了——这里是它们背后的概念框架。

### 零次提示

最简单的方法：给模型直接指令，无需示例。模型完全依赖其训练来理解和执行任务。这适合预期行为明显的简单请求。

<img src="../../../translated_images/zh-CN/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*无示例的直接指令——模型仅从指令推断任务*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回应：“积极”
```

**适用场景：** 简单分类、直接问答、翻译或任何模型无需额外指导即可处理的任务。

### 少量示例提示

提供示例展示您希望模型遵循的模式。模型从示例中学习期望的输入输出格式，并应用于新输入。对于格式或行为不明显的任务，这显著提高一致性。

<img src="../../../translated_images/zh-CN/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*从示例学习——模型识别模式并应用于新输入*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**适用场景：** 自定义分类、格式一致性、领域专属任务，或零次提示结果不稳定时。

### 链式思维

让模型逐步展示推理过程。模型不是直接给出答案，而是分解问题，逐一解决。提高数学、逻辑和多步骤推理任务的准确度。

<img src="../../../translated_images/zh-CN/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*逐步推理——将复杂问题拆解成明确的逻辑步骤*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型显示：15 - 8 = 7，然后 7 + 12 = 19 个苹果
```

**适用场景：** 数学题、逻辑谜题、调试，或任何显示推理过程能提升准确度和可信度的任务。

### 基于角色的提示

在提问前设定 AI 的身份或角色。这提供了上下文，影响回答的语气、深度和重点。“软件架构师”和“初级开发者”或“安全审计员”给出的建议不同。

<img src="../../../translated_images/zh-CN/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*设定上下文和身份——同一问题根据指定角色获得不同回答*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**适用场景：** 代码审查、辅导、领域专属分析，或需要针对特定专业水平或视角定制回复时。

### 提示模板

创建可复用的带变量占位符的提示。不用每次写新提示，只需定义一次模板，填入不同变量即可。LangChain4j 的 `PromptTemplate` 类支持使用 `{{variable}}` 语法方便实现。

<img src="../../../translated_images/zh-CN/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*可复用模板带变量占位——一个模板，多种使用*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

**适用场景：** 多次查询不同输入、批量处理、构建可复用 AI 工作流，或任何提示结构固定但数据变化的场景。

---

以上五大基础给你大多数提示任务的坚实工具。接下来本模块建立在其上，推出利用 GPT-5.2 推理控制、自我评估和结构化输出能力的**八个高级模式**。

## 高级模式

基础讲完，我们来看使本模块独特的八个高级模式。不是所有问题都需同一方法。有的需要快速回答，有的需深入思考。有的要显式推理，有的只要结果。下面的每种模式都针对不同情境优化——而 GPT-5.2 的推理控制让差异更明显。

<img src="../../../translated_images/zh-CN/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八个提示工程模式概览及其用例*

<img src="../../../translated_images/zh-CN/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 的推理控制让你指定模型思考量——从快速直接答复到深入探究*

**低急切度（快速且聚焦）** - 适用于需要快速、直接答案的简单问题。模型进行最少推理——最多 2 步。用于计算、查询或简单问答。

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **用 GitHub Copilot 探索：** 打开 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 询问：
> - “低急切度和高急切度提示模式的区别是什么？”
> - “提示中的 XML 标签如何帮助结构化 AI 回答？”
> - “什么时候用自我反思模式，什么时候用直接指令？”

**高急切度（深入彻底）** - 用于复杂问题，需全面分析。模型彻底探索并显示详细推理。适合系统设计、架构决策或复杂研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任务执行（逐步进展）** - 用于多步骤工作流。模型先给出计划，边执行边叙述，再总结。用于迁移、实现或任何多步骤流程。

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

链式思维提示明确要求模型展示推理过程，提高复杂任务的准确度。逐步分解帮助人类和 AI 共同理解逻辑。

> **🤖 试试 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 问问该模式：
> - “如何调整任务执行模式以支持长时间运行操作？”
> - “生产应用中组织工具前言的最佳实践是什么？”
> - “如何在 UI 中捕获并显示中间进展更新？”

<img src="../../../translated_images/zh-CN/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*计划 → 执行 → 总结的多步骤任务工作流*

**自我反思代码** - 用于生成符合生产标准代码。模型自动生成符合生产规范、带有正确错误处理的代码。用于构建新功能或服务。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-CN/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*迭代改进循环——生成、评估、识别问题、改进、重复*

**结构化分析** - 用于一致性评估。模型用固定框架（正确性、实践、性能、安全性、可维护性）审查代码。用于代码审查或质量评估。

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 试试 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 询问结构化分析：
> - “如何为不同类型代码审查自定义分析框架？”
> - “程序化解析和处理结构化输出的最佳方法？”
> - “如何确保不同审查会话之间严重级别一致？”

<img src="../../../translated_images/zh-CN/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*一致代码审查的框架，带有严重级别*

**多轮聊天** - 用于需要上下文的对话。模型记忆之前消息并基于它们构建。适合交互式帮助或复杂问答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-CN/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*对话上下文在多轮中积累，直到达到令牌限制*

**逐步推理** - 针对需要显式逻辑的问答。模型为每一步展示明确推理。适用数学题、逻辑谜题，或需要理解思维过程的场景。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-CN/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*将问题拆解为明确的逻辑步骤*

**受限输出** - 需要符合特定格式要求的回答。模型严格遵循格式和长度规则。用于摘要或要求精确输出结构的场景。

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-CN/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*强制执行特定格式、长度和结构的要求*

## 使用现有 Azure 资源

**验证部署：**

确保根目录下存在包含 Azure 凭据的 `.env` 文件（模块 01 部署时已创建）：
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用：**

> **注意：** 如果您已经使用模块 01 的 `./start-all.sh` 启动了所有应用，本模块将在端口 8083 运行。您可以跳过以下启动命令，直接访问 http://localhost:8083。
**选项 1：使用 Spring Boot 仪表板（推荐 VS Code 用户）**

开发容器包含了 Spring Boot 仪表板扩展，它提供了一个可视化界面来管理所有 Spring Boot 应用程序。你可以在 VS Code 左侧的活动栏中找到它（寻找 Spring Boot 图标）。

通过 Spring Boot 仪表板，你可以：
- 查看工作区中所有可用的 Spring Boot 应用程序
- 一键启动/停止应用程序
- 实时查看应用程序日志
- 监控应用程序状态

只需点击“prompt-engineering”旁边的播放按钮即可启动此模块，或者一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**选项 2：使用 shell 脚本**

启动所有 Web 应用程序（模块 01-04）：

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

或者仅启动此模块：

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

两个脚本会自动从根目录的 `.env` 文件加载环境变量，如果 JAR 文件不存在则会构建它们。

> **注意：** 如果你更喜欢先手动构建所有模块再启动：
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

在浏览器中打开 http://localhost:8083。

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

## 应用程序截图

<img src="../../../translated_images/zh-CN/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主仪表板显示全部 8 种提示工程模式及其特点和用例*

## 探索模式

Web 界面让你可以尝试不同的提示策略。每种模式解决不同的问题——尝试它们，看看哪种方法最适合。

> **注意：流式与非流式** — 每个模式页面都提供两个按钮：**🔴 流响应（实时）** 和 **非流式** 选项。流式使用服务器发送事件（SSE），能够在模型生成令牌时实时显示，因此你可以立即看到进展。非流式则是在收到完整响应后才显示。对于需要深入推理的提示（例如 高积极性、自我反思代码），非流式调用可能非常耗时——有时长达几分钟，且没有任何可见反馈。**在尝试复杂提示时使用流式响应**，这样可以看到模型的运作过程，避免误以为请求已超时。
>
> **注意：浏览器要求** — 流式功能使用 Fetch Streams API（`response.body.getReader()`），需要完整的浏览器（Chrome、Edge、Firefox、Safari）。它**不**支持 VS Code 内置的简单浏览器（Simple Browser），因为其 Webview 不支持 ReadableStream API。如果你使用简单浏览器，非流式按钮仍然可正常工作——只有流式按钮受影响。想要完整体验，请在外部浏览器中打开 `http://localhost:8083`。

### 低积极性与高积极性

用低积极性提出一个简单问题，比如“15% 的 200 是多少？”。你会立即得到直接答案。再用高积极性提出复杂问题，比如“为高流量 API 设计缓存策略”。点击 **🔴 流响应（实时）**，观察模型详细推理的令牌逐个出现。是同一个模型，同样的问题结构——但提示告诉它需要多少思考。

### 任务执行（工具前言）

多步骤工作流受益于事前规划和进度描述。模型会先概述执行计划，逐步叙述每个步骤，最后总结结果。

### 自我反思代码

尝试“创建一个电子邮件验证服务”。模型不会仅仅生成代码后停止，而是会生成、根据质量标准进行评估、识别缺陷并改进。你将看到它不断迭代，直到代码符合生产标准。

### 结构化分析

代码审查需要一致的评估框架。模型使用固定类别（正确性、实践、性能、安全）及其严重程度来分析代码。

### 多轮聊天

问“什么是 Spring Boot？”，然后紧接着问“给我一个示例”。模型会记住你的第一个问题，针对 Spring Boot 具体给出示例。没有记忆的话，第二个问题过于模糊。

### 逐步推理

选一个数学题，用逐步推理和低积极性分别尝试。低积极性只给答案——快速但不透明。逐步推理则展示所有计算和决策过程。

### 受限输出

当你需要特定格式或字数时，这个模式强制严格遵守。试着生成一个正好 100 字的项目符号格式总结。

## 你真正学到的

**推理努力决定一切**

GPT-5.2 让你通过提示控制计算努力。低努力意味着快速响应且探索有限。高努力则代表模型会花时间深度思考。你在学习根据任务复杂度匹配努力程度——简单问题别浪费时间，复杂决策也别急于求成。

**结构引导行为**

注意提示中的 XML 标签？它们不是装饰品。模型遵循结构化指令比自由文本更可靠。当你需要多步流程或复杂逻辑时，结构帮助模型跟踪当前位置和下一步。

<img src="../../../translated_images/zh-CN/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*结构良好的提示解析，包含清晰段落和 XML 风格组织*

**通过自我评估保证质量**

自我反思模式通过明确质量标准发挥作用。你不再寄希望于模型“正确完成”，而是明确告诉它“正确”的含义：逻辑正确、错误处理、性能、安全。模型随后能评估自身输出并改进。这使代码生成从随机变成了一种流程。

**上下文是有限的**

多轮对话依赖于每次请求包含消息历史。但有最大令牌数限制。随着对话变长，你需要策略保持相关上下文而不超限。本模块展示了记忆如何工作；后续你会学到何时总结、何时忘记、何时调用。

## 下一步

**下一个模块：** [03-rag - RAG（检索增强生成）](../03-rag/README.md)

---

**导航：** [← 上一页：模块 01 - 简介](../01-introduction/README.md) | [返回主目录](../README.md) | [下一页：模块 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件使用 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。虽然我们力求准确，但请注意自动翻译可能包含错误或不准确之处。请以原始语言的原版文件为权威来源。对于重要信息，建议使用专业人工翻译。我们不对因使用本翻译内容而引起的任何误解或误释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->