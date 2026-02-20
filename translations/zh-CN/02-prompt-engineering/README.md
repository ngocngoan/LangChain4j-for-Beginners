# 模块 02：使用 GPT-5.2 的提示工程

## 目录

- [你将学到什么](../../../02-prompt-engineering)
- [先决条件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基础](../../../02-prompt-engineering)
  - [零样本提示](../../../02-prompt-engineering)
  - [少样本提示](../../../02-prompt-engineering)
  - [思维链](../../../02-prompt-engineering)
  - [基于角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [高级模式](../../../02-prompt-engineering)
- [使用现有 Azure 资源](../../../02-prompt-engineering)
- [应用截屏](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低积极性与高积极性](../../../02-prompt-engineering)
  - [任务执行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代码](../../../02-prompt-engineering)
  - [结构化分析](../../../02-prompt-engineering)
  - [多轮聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限输出](../../../02-prompt-engineering)
- [你真正学到的是什么](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你将学到什么

<img src="../../../translated_images/zh-CN/what-youll-learn.c68269ac048503b2.webp" alt="你将学到什么" width="800"/>

在上一个模块中，你已经了解了内存如何支持对话式 AI，并使用 GitHub 模型进行基本交互。现在我们将重点放在提问方式——提示本身——使用 Azure OpenAI 的 GPT-5.2。你如何构建提示将极大影响得到的回答质量。我们从回顾基础提示技术开始，然后进入八种可以充分利用 GPT-5.2 功能的高级模式。

我们使用 GPT-5.2 是因为它引入了推理控制——你可以告诉模型在回答前思考多少。这使得不同的提示策略更明显，帮助你理解何时使用哪种方法。相比 GitHub 模型，Azure 对 GPT-5.2 的调用频率限制更少，这也带来了好处。

## 先决条件

- 完成模块 01（已部署 Azure OpenAI 资源）
- 根目录下有包含 Azure 凭据的 `.env` 文件（由模块 01 中的 `azd up` 创建）

> **注意：** 如果你还没有完成模块 01，请先按照那里的部署说明进行。

## 理解提示工程

<img src="../../../translated_images/zh-CN/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什么是提示工程？" width="800"/>

提示工程是设计输入文本，使你始终获得所需结果的方法。这不仅仅是提问——而是结构化请求，让模型明确理解你的需求以及怎么满足它。

把它想象成给同事下指令。“修复 bug”太模糊。“通过添加空值检查修复 UserService.java 第 45 行的空指针异常”才具体。语言模型也是如此——具体与结构很重要。

<img src="../../../translated_images/zh-CN/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的位置" width="800"/>

LangChain4j 提供基础设施——模型连接、内存和消息类型——而提示模式只是通过这些基础设施发送的精心结构化文本。关键构建块是 `SystemMessage`（设置 AI 的行为和角色）和 `UserMessage`（传递你的实际请求）。

## 提示工程基础

<img src="../../../translated_images/zh-CN/five-patterns-overview.160f35045ffd2a94.webp" alt="五种提示工程模式概览" width="800"/>

在深入本模块的高级模式前，先回顾五种基础提示技术。这些是每个提示工程师都应了解的构建块。如果你已经浏览过[快速上手模块](../00-quick-start/README.md#2-prompt-patterns)，你已见过它们的实际应用——以下是它们背后的概念框架。

### 零样本提示

最简单的方法：给模型一个直接指令但不提供示例。模型完全依赖训练来理解和执行任务。适用于期望行为显而易见的简单请求。

<img src="../../../translated_images/zh-CN/zero-shot-prompting.7abc24228be84e6c.webp" alt="零样本提示" width="800"/>

*不提供示例的直接指令——模型从指令中推断任务*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回复：“积极的”
```

**适用场景：** 简单分类、直接提问、翻译或模型无需额外指导即可处理的任务。

### 少样本提示

提供示例，展示你希望模型遵循的模式。模型从示例中学习输入输出格式，并应用于新的输入。这大幅提升了格式或行为不明显任务的一致性。

<img src="../../../translated_images/zh-CN/few-shot-prompting.9d9eace1da88989a.webp" alt="少样本提示" width="800"/>

*从示例中学习——模型识别模式并应用于新输入*

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

**适用场景：** 定制分类、一致格式、特定领域任务，或零样本结果不一致时。

### 思维链

要求模型展示逐步推理。模型不直接给出答案，而是分解问题并明确推理每个部分。这提高了数学、逻辑和多步骤推理任务的准确度。

<img src="../../../translated_images/zh-CN/chain-of-thought.5cff6630e2657e2a.webp" alt="思维链提示" width="800"/>

*逐步推理——将复杂问题拆解为明确的逻辑步骤*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型显示：15 - 8 = 7，然后 7 + 12 = 19 个苹果
```

**适用场景：** 数学问题、逻辑谜题、调试，或任何显示推理过程能提升准确性与信任度的任务。

### 基于角色的提示

先为 AI 设定身份或角色，再提问。这样提供上下文，影响回答的语气、深度和重点。“软件架构师”给出的建议不同于“初级开发人员”或“安全审计员”。

<img src="../../../translated_images/zh-CN/role-based-prompting.a806e1a73de6e3a4.webp" alt="基于角色的提示" width="800"/>

*设定上下文和身份——同一问题根据角色不同得到不同回答*

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

**适用场景：** 代码审查、辅导、专业领域分析，或需要针对具体专业水平或视角的回答时。

### 提示模板

创建带变量占位符的可重用提示。无需每次重写提示，定义一次模板，再填入不同值。LangChain4j 的 `PromptTemplate` 类用 `{{variable}}` 语法简单实现。

<img src="../../../translated_images/zh-CN/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*带变量占位符的可重用提示——一个模板，多种应用*

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

**适用场景：** 多次查询不同输入、批量处理、构建可重用 AI 工作流，或提示结构固定仅数据改变的情况。

---

这五个基础给予你绝大多数提示任务的坚实工具包。本模块剩余内容在此基础上，推出**八个高级模式**，充分利用 GPT-5.2 的推理控制、自我评估和结构化输出能力。

## 高级模式

掌握基础后，我们进入本模块的八个高级模式。并非所有问题都适用相同方法。有的问题需快速回答，有的需深入思考。有的需要展示推理，有的只要结果。以下每种模式都针对不同场景进行优化——GPT-5.2 的推理控制使这些差异更显著。

<img src="../../../translated_images/zh-CN/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八种提示模式" width="800"/>

*八种提示工程模式及其适用场景概览*

<img src="../../../translated_images/zh-CN/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 的推理控制" width="800"/>

*GPT-5.2 的推理控制允许你指定模型应思考多少——从快速直答到深度探索*

**低积极性（快速且聚焦）** - 适用于简单问题，需快速、直接回答。模型做最少推理——最多 2 步。适用于计算、查找或简单问题。

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

> 💡 **用 GitHub Copilot 探索：** 打开 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)，提问：
> - “低积极性和高积极性提示模式有什么区别？”
> - “提示中的 XML 标签如何帮助构建 AI 返回内容？”
> - “什么时候用自我反思模式，什么时候用直接指令？”

**高积极性（深入且全面）** - 适用于复杂问题，需全面分析。模型深入探索，展示详细推理。适合系统设计、架构决策或复杂调研。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任务执行（逐步进展）** - 适用于多步骤工作流。模型事先规划，执行时讲解每步，最后给出总结。适合迁移、实施或任何多步骤流程。

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

思维链提示明确要求模型展示推理过程，提升复杂任务的准确性。逐步分解有助于人类和 AI 理解逻辑。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天尝试：** 询问此模式：
> - “如何将任务执行模式改造用于长时间运行的操作？”
> - “生产应用中结构化工具前言的最佳实践是什么？”
> - “如何捕获并在 UI 中显示中间进展更新？”

<img src="../../../translated_images/zh-CN/task-execution-pattern.9da3967750ab5c1e.webp" alt="任务执行模式" width="800"/>

*规划 → 执行 → 总结的多步骤工作流*

**自我反思代码** - 生成生产级代码。模型按生产标准生成代码，完善错误处理。构建新功能或服务时使用。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-CN/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循环" width="800"/>

*迭代改进循环——生成、评估、发现问题、改进、重复*

**结构化分析** - 用于一致评估。模型使用固定框架审查代码（正确性、实践、性能、安全、可维护性）。适合代码审查或质量评估。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天尝试：** 询问结构化分析：
> - “如何定制不同类型代码审查的分析框架？”
> - “以编程方式解析和利用结构化输出的最佳方法？”
> - “如何确保不同审查会话间严重级别的一致性？”

<img src="../../../translated_images/zh-CN/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="结构化分析模式" width="800"/>

*带严重级别的统一代码审查框架*

**多轮聊天** - 适用于需要上下文的对话。模型记住之前消息并在此基础上回答。适合交互式帮助或复杂问答。

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/zh-CN/context-memory.dff30ad9fa78832a.webp" alt="上下文记忆" width="800"/>

*多轮对话中上下文逐步积累直至达到令牌限制*

**逐步推理** - 适用于需要显式逻辑的问题。模型展示每步推理。适合数学题、逻辑谜题或需要理解决策过程的场景。

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-CN/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="逐步推理模式" width="800"/>

*将问题拆解为明确逻辑步骤*

**受限输出** - 适用于格式有严格要求的回答。模型严格遵守格式和长度规则。适合摘要或需要精确输出结构的场景。

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

<img src="../../../translated_images/zh-CN/constrained-output-pattern.0ce39a682a6795c2.webp" alt="受限输出模式" width="800"/>

*强制执行特定格式、长度和结构要求*

## 使用现有 Azure 资源

**验证部署：**

确保根目录下存在包含 Azure 凭据的 `.env` 文件（模块 01 部署时创建）：
```bash
cat ../.env  # 应该显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用：**

> **注意：** 如果你已经使用模块 01 的 `./start-all.sh` 启动了所有应用，本模块的服务已经在 8083 端口运行，你可以跳过下面的启动命令，直接访问 http://localhost:8083 。

**选项 1：使用 Spring Boot 仪表盘（推荐 VS Code 用户）**

开发容器内置了 Spring Boot 仪表盘扩展，提供可视化界面管理所有 Spring Boot 应用。你可以在 VS Code 左侧活动栏找到（查找 Spring Boot 图标）。

通过 Spring Boot 仪表盘，你可以：
- 查看工作区内所有可用的 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监控应用状态
只需点击“prompt-engineering”旁边的播放按钮即可启动此模块，或一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**选项2：使用 shell 脚本**

启动所有 web 应用（模块01-04）：

**Bash：**
```bash
cd ..  # 从根目录
./start-all.sh
```

**PowerShell：**
```powershell
cd ..  # 从根目录
.\start-all.ps1
```

或者只启动此模块：

**Bash：**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell：**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

这两个脚本会自动从根目录的 `.env` 文件加载环境变量，如果 JAR 文件不存在，则会构建它们。

> **注意：** 如果你更喜欢在启动之前手动构建所有模块：
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

在浏览器中打开 http://localhost:8083。

**停止方式：**

**Bash：**
```bash
./stop.sh  # 仅此模块
# 或
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell：**
```powershell
.\stop.ps1  # 仅此模块
# 或者
cd ..; .\stop-all.ps1  # 所有模块
```

## 应用截图

<img src="../../../translated_images/zh-CN/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主控制面板显示所有8种提示工程模式及其特性和使用场景*

## 探索模式

网页界面允许你尝试不同的提示策略。每种模式解决不同的问题——试试它们，看看每种方法何时表现最佳。

> **注意：流式与非流式** — 每个模式页面都提供两个按钮：**🔴 流式响应（实时）**和一个**非流式**选项。流式使用服务器发送事件（SSE）实时展示模型生成的标记，因此你可以立即看到进展。非流式选项则等待整个响应完成后才显示。对于触发深度推理的提示（例如，高积极性、自我反思代码），非流式调用可能需要很长时间——有时长达数分钟——且没有任何可见反馈。**在尝试复杂提示时使用流式功能**，这样你可以看到模型工作过程，避免误以为请求超时。
>
> **注意：浏览器要求** — 流式功能使用 Fetch Streams API（`response.body.getReader()`），需要完整浏览器环境（Chrome、Edge、Firefox、Safari）。它**不支持** VS Code 内置的简单浏览器(Simple Browser)，因为其 webview 不支持 ReadableStream API。如果你使用简单浏览器，非流式按钮仍然正常工作，只有流式按钮受影响。请在外部浏览器中打开 `http://localhost:8083` 以获得完整体验。

### 低积极性 vs 高积极性

用低积极性问一个简单问题，如“200的15%是多少？”，你会立即获得直接答案。现在用高积极性问个复杂问题，如“为高流量API设计缓存策略”。点击**🔴 流式响应（实时）**，观察模型详细推理逐字生成。相同模型，相同问题结构——但提示指示了需要多少思考。

### 任务执行（工具开场白）

多步骤工作流受益于事先规划和进度叙述。模型先概述将做什么，边做边叙述每一步，最后总结结果。

### 自我反思代码

试试“创建一个电子邮件验证服务”。模型不仅生成代码然后停止，还会生成、依据质量标准评估、识别弱点并改进。你将看到它不断迭代，直到代码符合生产标准。

### 结构化分析

代码审查需要一致的评估框架。模型使用固定分类（正确性、实践、性能、安全）及严重性等级来分析代码。

### 多轮对话

问“什么是Spring Boot？”然后立即接着问“给我一个示例”。模型记得你的第一个问题，并针对性地给出Spring Boot示例。如果没有记忆，第二个问题太过模糊。

### 逐步推理

选择一个数学题，用逐步推理和低积极性分别尝试。低积极性只给答案——快速但不透明。逐步推理展示了每个计算和决策过程。

### 受限输出

当你需要特定格式或字数时，这种模式强制严格遵守。试试生成一个恰好100字的项目符号格式摘要。

## 你真正学到的是什么

**推理努力改变一切**

GPT-5.2 让你通过提示控制计算努力。低努力意味着快速响应和最小探索。高努力意味着模型花时间进行深入思考。你正在学习根据任务复杂度匹配努力程度——不要在简单问题上浪费时间，也不要匆忙做复杂决策。

**结构引导行为**

你注意到提示里的 XML 标签了吗？它们不是装饰。模型比起自由文本，更可靠地遵循结构化指令。当你需要多步骤流程或复杂逻辑时，结构有助于模型跟踪当前位置及后续步骤。

<img src="../../../translated_images/zh-CN/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*结构良好的提示示例，具有清晰的部分和XML样式组织*

**通过自我评估保证质量**

自我反思模式通过明确质量标准工作。你不是希望模型“做对”，你明确告诉它“做对”的标准是什么：逻辑正确、错误处理、性能、安全。模型可以自我评估输出并改进。这将代码生成从抽奖变成了一个过程。

**上下文是有限的**

多轮对话通过每次请求附带消息历史工作。但有上限——每个模型有最大令牌数。随着对话增长，你需要策略保持相关上下文而不超标。此模块演示记忆如何工作；后来你将学会何时总结、何时忘记、何时检索。

## 接下来

**下一模块:** [03-rag - RAG（检索增强生成）](../03-rag/README.md)

---

**导航：** [← 上一节：模块01 - 介绍](../01-introduction/README.md) | [返回主页](../README.md) | [下一节：模块03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由AI翻译服务[Co-op Translator](https://github.com/Azure/co-op-translator)翻译而成。虽然我们力求准确，但请注意自动翻译可能存在错误或不准确之处。原始文件的母语版本应被视为权威来源。对于重要信息，建议采用专业人工翻译。对于因使用本翻译而产生的任何误解或误释，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->