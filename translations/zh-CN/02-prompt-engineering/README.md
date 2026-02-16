# Module 02：使用 GPT-5.2 进行提示工程

## 目录

- [你将学习什么](../../../02-prompt-engineering)
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
- [应用程序截图](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低热情 vs 高热情](../../../02-prompt-engineering)
  - [任务执行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代码](../../../02-prompt-engineering)
  - [结构化分析](../../../02-prompt-engineering)
  - [多轮聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限输出](../../../02-prompt-engineering)
- [你真正学到的是什么](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你将学习什么

<img src="../../../translated_images/zh-CN/what-youll-learn.c68269ac048503b2.webp" alt="你将学习什么" width="800"/>

在上一个模块中，你了解了记忆如何支持对话式 AI，并使用 GitHub 模型进行基本交互。现在，我们将重点放在如何提问——提示本身——使用 Azure OpenAI 的 GPT-5.2。你构造提示的方式会极大地影响你获得的回答质量。我们从回顾基础提示技术开始，接着进入八种充分利用 GPT-5.2 功能的高级模式。

我们使用 GPT-5.2 是因为它引入了推理控制——你可以告诉模型在回答前需要思考多少。这使得不同的提示策略更加明显，并帮助你理解何时使用哪种方法。相比 GitHub 模型，我们还将受益于 Azure 对 GPT-5.2 更少的速率限制。

## 先决条件

- 完成模块 01（部署了 Azure OpenAI 资源）
- 根目录下有包含 Azure 凭据的 `.env` 文件（由模块 01 中的 `azd up` 创建）

> **注意：** 如果还没有完成模块 01，请先按照那里的部署说明操作。

## 理解提示工程

<img src="../../../translated_images/zh-CN/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什么是提示工程？" width="800"/>

提示工程是设计输入文本，使你持续获得所需结果的过程。它不仅仅是提问——而是结构化请求，让模型准确理解你想要什么以及如何提供。

你可以把它想象成给同事下指令。“修复这个Bug”含糊不清。“通过添加空指针检查修复 UserService.java 第 45 行的空指针异常”非常具体。语言模型也一样——具体和结构化非常重要。

<img src="../../../translated_images/zh-CN/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的作用" width="800"/>

LangChain4j 提供基础设施——模型连接、记忆和消息类型——而提示模式则是通过该基础设施发送的精心结构化文本。关键构建块是 `SystemMessage`（设置 AI 的行为和角色）和 `UserMessage`（承载你的实际请求）。

## 提示工程基础

<img src="../../../translated_images/zh-CN/five-patterns-overview.160f35045ffd2a94.webp" alt="五种提示工程模式概览" width="800"/>

在深入本模块的高级模式之前，我们先回顾五种基础提示技术。这些是每个提示工程师都应了解的构建块。如果你已经完成了[快速入门模块](../00-quick-start/README.md#2-prompt-patterns)，你就见过它们的实际应用——以下是背后的概念框架。

### 零样本提示

最简单的方法：直接给模型指令，无需示例。模型完全依赖其训练理解并执行任务。这适用于预期行为明显的简单请求。

<img src="../../../translated_images/zh-CN/zero-shot-prompting.7abc24228be84e6c.webp" alt="零样本提示" width="800"/>

*不带示例的直接指令——模型仅从指令推断任务*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 回复：“积极的”
```

**适用场景：** 简单分类、直接提问、翻译，或任何模型无需额外指导即可完成的任务。

### 少样本提示

提供示例展示你希望模型遵循的模式。模型从示例中学习预期的输入输出格式，并将其应用于新输入。这显著提高了在所需格式或行为不明显的任务中的一致性。

<img src="../../../translated_images/zh-CN/few-shot-prompting.9d9eace1da88989a.webp" alt="少样本提示" width="800"/>

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

**适用场景：** 定制分类、一致格式、特定领域任务，或零样本结果不稳定时。

### 思维链

让模型逐步展示推理过程。模型不是直接给出答案，而是明确拆解问题、逐步推理。这提升了数学、逻辑和多步骤推理任务的准确性。

<img src="../../../translated_images/zh-CN/chain-of-thought.5cff6630e2657e2a.webp" alt="思维链提示" width="800"/>

*逐步推理——将复杂问题分解为明确的逻辑步骤*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型显示：15 - 8 = 7，然后 7 + 12 = 19 个苹果
```

**适用场景：** 数学题、逻辑谜题、调试，或任何显示推理过程可提升准确性和信任的任务。

### 基于角色的提示

在提问前为 AI 设定角色或身份。这样提供的上下文会影响回答的语气、深度和重点。“软件架构师”给出的建议与“初级开发者”或“安全审计员”不同。

<img src="../../../translated_images/zh-CN/role-based-prompting.a806e1a73de6e3a4.webp" alt="基于角色的提示" width="800"/>

*设定上下文和身份——同一问题根据分配的角色得到不同回答*

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

**适用场景：** 代码审查、辅导、领域专门分析，或当你需要针对特定专业水平或视角定制回答时。

### 提示模板

创建带变量占位符的可重用提示。无需每次都写新提示，只需定义模板，填充不同的值。LangChain4j 的 `PromptTemplate` 类使用 `{{variable}}` 语法简化此过程。

<img src="../../../translated_images/zh-CN/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*带变量占位符的可重用提示——一个模板，多种用途*

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

**适用场景：** 多次查询不同输入、批量处理、构建可复用的 AI 工作流，或任何提示结构固定但数据变化的场景。

---

这五个基础为大多数提示任务提供了坚实的工具包。本模块其余内容将基于它们，介绍**八种高级模式**，利用 GPT-5.2 的推理控制、自我评估和结构化输出能力。

## 高级模式

基础铺垫完毕，接下来介绍本模块独有的八种高级模式。不是所有问题都需要相同方式，有的问题需要快速回答，有的需要深入思考，有的需要显式推理，有的只要结果。下面每种模式都针对不同场景优化——而 GPT-5.2 的推理控制让这些区别更加明显。

<img src="../../../translated_images/zh-CN/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八种提示工程模式" width="800"/>

*八种提示工程模式及其适用案例概览*

<img src="../../../translated_images/zh-CN/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 推理控制" width="800"/>

*GPT-5.2 的推理控制允许你指定模型思考量——从快速直接回答到深度探索*

<img src="../../../translated_images/zh-CN/reasoning-effort.db4a3ba5b8e392c1.webp" alt="推理努力对比" width="800"/>

*低热情（快速，直接） vs 高热情（透彻，探索）推理方法*

**低热情（快速且聚焦）** - 适用于简单问题，希望快速直接回答。模型推理步骤最少——最多两步。用于计算、查找或简单问题。

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

> 💡 **用 GitHub Copilot 探索：** 打开 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) 并提问：
> - “低热情和高热情提示模式有什么区别？”
> - “提示中的 XML 标签如何帮助结构化 AI 的回答？”
> - “什么时候应该用自我反思模式，什么时候用直接指令？”

**高热情（深度且全面）** - 适合复杂问题，需要全面分析。模型会深入探索并展示详尽推理。用于系统设计、架构决策或复杂研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任务执行（逐步进展）** - 用于多步骤工作流。模型提前制定计划，执行时逐步叙述进展，最后总结。适合迁移、实施或任何多步骤流程。

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

思维链提示明确要求模型展示推理过程，提升复杂任务的准确性。逐步分解帮助人机更好理解逻辑。

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：** 询问该模式：
> - “如何将任务执行模式调整为长时间运行的操作？”
> - “生产环境中如何最佳组织工具前言？”
> - “怎样捕获并在界面显示中间进展？”

<img src="../../../translated_images/zh-CN/task-execution-pattern.9da3967750ab5c1e.webp" alt="任务执行模式" width="800"/>

*计划 → 执行 → 总结 的多步任务工作流*

**自我反思代码** - 生成生产质量代码。模型生成遵循生产标准和正确错误处理的代码。适合构建新功能或服务。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-CN/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循环" width="800"/>

*迭代改进循环——生成、评估、发现问题、改进、重复*

**结构化分析** - 一致性评估。模型使用固定框架（正确性、实践、性能、安全、可维护性）审查代码。用于代码审查或质量评估。

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

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：** 询问结构化分析：
> - “如何为不同类型代码审查定制分析框架？”
> - “如何程序化解析并使用结构化输出？”
> - “如何确保不同审查会话间的严重级别一致？”

<img src="../../../translated_images/zh-CN/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="结构化分析模式" width="800"/>

*用于一致代码审查的框架，含严重级别*

**多轮聊天** - 适用于需要上下文的对话。模型记住前置消息并在此基础上构建。适合交互式帮助或复杂问答。

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

*对话上下文在多轮累积直至达到令牌限制*

**逐步推理** - 适合需要可见逻辑的问题。模型展示每一步的明确推理。用于数学题、逻辑谜题，或需要了解思考过程的情况。

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

*将问题分解为明确的逻辑步骤*

**受限输出** - 对回答有严格格式要求。模型严格遵守格式和长度规则。用于摘要或需要精确输出结构的场景。

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

确保根目录有包含 Azure 凭据的 `.env` 文件（模块 01 期间创建）：
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT，API_KEY，DEPLOYMENT
```

**启动应用程序：**

> **注意：** 如果你已经在模块 01 中通过 `./start-all.sh` 启动了所有应用，本模块已经在端口 8083 运行。你可以跳过下面的启动命令，直接访问 http://localhost:8083。

**选项 1：使用 Spring Boot Dashboard（推荐 VS Code 用户）**

开发容器包括 Spring Boot Dashboard 扩展，它提供一个可视化界面管理所有 Spring Boot 应用程序。你可以在 VS Code 左侧活动栏中找到它（寻找 Spring Boot 图标）。
从 Spring Boot 仪表板，您可以：
- 查看工作区中所有可用的 Spring Boot 应用程序
- 一键启动/停止应用程序
- 实时查看应用程序日志
- 监控应用程序状态

只需点击“prompt-engineering”旁边的播放按钮即可启动此模块，或一次启动所有模块。

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

或者只启动此模块：

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

这两个脚本会自动从根目录的 `.env` 文件加载环境变量，如果 JAR 文件不存在，会自动构建。

> **注意：** 如果您更喜欢先手动构建所有模块再启动：
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

在浏览器中打开 http://localhost:8083 。

**停止方式：**

**Bash:**
```bash
./stop.sh  # 仅此模块
# 或者
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell:**
```powershell
.\stop.ps1  # 仅此模块
# 或者
cd ..; .\stop-all.ps1  # 所有模块
```

## 应用程序截图

<img src="../../../translated_images/zh-CN/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主仪表板显示所有 8 种提示工程模式及其特征和使用场景*

## 探索这些模式

Web 界面让您可以尝试不同的提示策略。每种模式解决不同的问题——尝试它们，看看每种方法何时最有效。

### 低与高“求知欲”

用低求知欲问一个简单问题，比如“200 的 15% 是多少？”。您会马上得到直接的答案。现在用高求知欲问一个复杂问题，比如“为高流量 API 设计缓存策略”。观察模型如何减慢速度，给出详细推理。同一个模型，同样的问题结构——但提示告诉它要做多少思考。

<img src="../../../translated_images/zh-CN/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*快速计算，推理最少*

<img src="../../../translated_images/zh-CN/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*全面的缓存策略（2.8MB）*

### 任务执行（工具前导语）

多步骤工作流受益于预先规划和过程叙述。模型会概述它要做的事情，叙述每一步，最后总结结果。

<img src="../../../translated_images/zh-CN/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*创建 REST 端点，逐步叙述（3.9MB）*

### 自我反思代码

试试“创建一个邮箱验证服务”。模型不仅生成代码就停，而是生成后根据质量标准评估，发现缺陷并改进。您会看到它反复迭代，直到代码符合生产标准。

<img src="../../../translated_images/zh-CN/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*完整的邮箱验证服务（5.2MB）*

### 结构化分析

代码审查需要一致的评估框架。模型使用固定类别（正确性、最佳实践、性能、安全性）和严重性等级来分析代码。

<img src="../../../translated_images/zh-CN/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*基于框架的代码审查*

### 多轮对话

问“什么是 Spring Boot？”，然后紧接着问“给我一个例子”。模型记住您第一个问题，专门给您一个 Spring Boot 例子。没有记忆，第二个问题会太模糊。

<img src="../../../translated_images/zh-CN/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*跨问题保持上下文*

### 逐步推理

选择一个数学题，分别用逐步推理和低求知欲尝试。低求知欲直接给答案——快但不透明。逐步推理展示每个计算和决策。

<img src="../../../translated_images/zh-CN/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*带明确步骤的数学题*

### 受约束的输出

当您需要特定格式或字数时，此模式强制严格遵循。试试生成一个恰好 100 词的项目符号格式摘要。

<img src="../../../translated_images/zh-CN/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*机器学习总结及格式控制*

## 您真正学到的是什么

**推理努力改变一切**

GPT-5.2 允许您通过提示控制计算努力。低努力意味着响应快速且探索最少。高努力意味着模型花时间深入思考。您正在学会根据任务复杂度匹配努力——简单问题不浪费时间，复杂决策不草率。

**结构引导行为**

注意提示中的 XML 标签？它们不是装饰。模型比起自由文本更可靠地遵循结构化指令。需要多步骤流程或复杂逻辑时，结构帮助模型跟踪位置和下一步。

<img src="../../../translated_images/zh-CN/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*结构良好的提示结构解剖，含明确分区和 XML 风格组织*

**通过自我评估保证质量**

自我反思模式通过明确质量标准起作用。不是指望模型“做正确”，而是告诉它“正确”意味着：逻辑正确、错误处理、性能、安全性。模型随后能评估自身输出并改进。这让代码生成变成一个有流程的过程，而不是随机博弈。

**上下文有限**

多轮对话通过每次请求包含消息历史来实现。但有上限——每个模型都有最大令牌数。随着对话增长，您需要策略保持相关上下文同时避免超限。这个模块展示内存如何工作；后续您将学习何时总结、何时遗忘、何时取回。

## 下一步

**下一个模块：** [03-rag - RAG (检索增强生成)](../03-rag/README.md)

---

**导航：** [← 上一：模块 01 - 介绍](../01-introduction/README.md) | [返回主页](../README.md) | [下一：模块 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由人工智能翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译而成。虽然我们尽力确保翻译的准确性，但请注意，自动翻译可能存在错误或不准确之处。原始文件的母语版本应被视为权威来源。对于关键内容，建议使用专业人工翻译。我们不对因使用本翻译而产生的任何误解或误释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->