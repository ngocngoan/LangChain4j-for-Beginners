# 模块 02：使用 GPT-5.2 的提示工程

## 目录

- [你将学到什么](../../../02-prompt-engineering)
- [先决条件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基础](../../../02-prompt-engineering)
  - [零样本提示](../../../02-prompt-engineering)
  - [少样本提示](../../../02-prompt-engineering)
  - [思路链](../../../02-prompt-engineering)
  - [基于角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [高级模式](../../../02-prompt-engineering)
- [使用现有 Azure 资源](../../../02-prompt-engineering)
- [应用程序截图](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低渴望 vs 高渴望](../../../02-prompt-engineering)
  - [任务执行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代码](../../../02-prompt-engineering)
  - [结构化分析](../../../02-prompt-engineering)
  - [多轮聊天](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限输出](../../../02-prompt-engineering)
- [你真正学到的](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你将学到什么

<img src="../../../translated_images/zh-CN/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

在上一模块中，你了解了记忆如何支持对话式 AI，并使用 GitHub 模型进行基本交互。现在我们将重点关注如何提问——即提示本身——使用 Azure OpenAI 的 GPT-5.2。你构建提示的方式会极大影响获得的回复质量。我们从基础的提示技术回顾开始，然后进入利用 GPT-5.2 功能的八种高级模式。

我们之所以使用 GPT-5.2，是因为它引入了推理控制——你可以告诉模型回答前应思考多少。这使得不同的提示策略更清晰易见，帮助你理解何时使用哪种方法。相比 GitHub 模型，我们还能享受 Azure 对 GPT-5.2 更少的速率限制。

## 先决条件

- 完成模块 01（部署了 Azure OpenAI 资源）
- 根目录下有 `.env` 文件，包含 Azure 凭证（由模块 01 的 `azd up` 创建）

> **注意：**如果尚未完成模块 01，请先按照那里的部署说明操作。

## 理解提示工程

<img src="../../../translated_images/zh-CN/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

提示工程是设计输入文本以持续获得所需结果的技术。它不仅仅是问问题——而是构建请求，使模型能准确理解你的需求以及如何实现。

把它想象成给同事下达指令。“修复 bug”很模糊。“通过添加空检查修复 UserService.java 第 45 行的空指针异常”则具体。语言模型也是如此——具体性和结构同样重要。

<img src="../../../translated_images/zh-CN/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j 提供基础架构——模型连接、记忆和消息类型——而提示模式则是通过该架构发送的精心结构化文本。主要构建块是 `SystemMessage`（设置 AI 的行为和角色）和 `UserMessage`（承载你的实际请求）。

## 提示工程基础

<img src="../../../translated_images/zh-CN/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

在深入本模块的高级模式之前，让我们回顾五种基础提示技术。这些是每个提示工程师应该掌握的构建块。如果你已学习过[快速入门模块](../00-quick-start/README.md#2-prompt-patterns)，你已经见过它们的应用——这里是它们背后的概念框架。

### 零样本提示

最简单的方法：直接给模型指令而不提供示例。模型完全依赖训练理解和执行任务。适用于对预期行为显而易见的简单请求。

<img src="../../../translated_images/zh-CN/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*无示例的直接指令——模型仅从指令推断任务*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 响应：“正面”
```

**使用时机：** 简单分类、直接提问、翻译或任何模型无需额外指导即可处理的任务。

### 少样本提示

提供示例演示你希望模型遵循的模式。模型从示例学习预期的输入输出格式，并应用于新输入。极大改善目标格式或行为不明显任务的一致性。

<img src="../../../translated_images/zh-CN/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*通过示例学习——模型识别模式并应用于新输入*

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

**使用时机：** 自定义分类、一致格式化、特定领域任务，或零样本结果不稳定时。

### 思路链

要求模型逐步展示推理过程。模型不跳过直接给出答案，而是分解问题，明确每一步。提升数学、逻辑和多步骤推理任务的准确度。

<img src="../../../translated_images/zh-CN/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*逐步推理——将复杂问题分解为明确逻辑步骤*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型展示：15 - 8 = 7，然后 7 + 12 = 19 个苹果
```

**使用时机：** 数学题、逻辑谜题、调试或任何展示推理过程可提升准确度和信任度的任务。

### 基于角色的提示

在提问前设定 AI 的身份或角色。这提供背景，影响回答的语气、深度和重点。“软件架构师”给出的建议和“初级开发人员”或“安全审核员”大相径庭。

<img src="../../../translated_images/zh-CN/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*设定上下文和角色——相同问题依角色不同得不同回答*

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

**使用时机：** 代码审查、辅导、领域特定分析，或需要针对特定专业水平或视角定制回答时。

### 提示模板

创建可复用的提示，带变量占位符。无需每次重写提示，定义一次模板，填入不同值。LangChain4j 的 `PromptTemplate` 类用 `{{variable}}` 语法简化了这一操作。

<img src="../../../translated_images/zh-CN/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*带变量占位符的复用提示——一个模板，多次使用*

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

**使用时机：** 不同输入的重复查询、批量处理、构建可复用 AI 工作流，或任何提示结构固定但数据变化的场景。

---

这五项基础为你提供了大多数提示任务的坚实工具包。本模块余下内容将在此基础上构建，介绍利用 GPT-5.2 推理控制、自我评估和结构化输出能力的**八种高级模式**。

## 高级模式

基础介绍完毕，我们进入本模块特色的八种高级模式。不是所有问题都需相同方法。有些问题需快速解答，有些需深度思考。有些需展示推理，有些只要结果。以下每种模式针对不同场景优化——且 GPT-5.2 的推理控制使差异更明显。

<img src="../../../translated_images/zh-CN/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八种提示工程模式及其使用场景概览*

<img src="../../../translated_images/zh-CN/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 的推理控制可指定模型应做多少思考——从快速直接到深度探索*

<img src="../../../translated_images/zh-CN/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*低渴望（快速直接）与高渴望（彻底探索）推理方法比较*

**低渴望（快速且聚焦）** - 针对希望快速、直接回答的简单问题。模型执行最小推理——最多两步。适用于计算、查询或直接问题。

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **用 GitHub Copilot 探索：** 打开 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)，提问：
> - "低渴望和高渴望提示模式有什么区别？"
> - "提示中的 XML 标签如何帮助结构化 AI 的回答？"
> - "什么时候该用自我反思模式，什么时候用直接指令？"

**高渴望（深度且全面）** - 适用于需要全面分析的复杂问题。模型彻底探索并展示详细推理。适合系统设计、架构决策或复杂研究。

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任务执行（逐步进展）** - 面向多步骤工作流程。模型提供事先计划，边做边叙述每步，最后总结。适合迁移、实施或任意多步骤过程。

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

思路链提示明确要求模型展示推理过程，提升复杂任务的准确度。逐步分解有助于人类与 AI 理解逻辑。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) 聊天尝试：** 询问此模式：
> - "如何将任务执行模式适配长时间运行的操作？"
> - "生产应用中构建工具前言的最佳实践有哪些？"
> - "怎样在 UI 中捕获并显示中间进度更新？"

<img src="../../../translated_images/zh-CN/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*计划 → 执行 → 总结的多步骤任务工作流程*

**自我反思代码** - 用于生成生产级代码。模型生成代码，依据质量标准审查并迭代改进。适合构建新功能或服务时使用。

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/zh-CN/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*迭代改进循环——生成、评估、识别问题、改进、重复*

**结构化分析** - 用于一致性评估。模型使用固定框架审查代码（正确性、实践、性能、安全）。适合代码审查或质量评估。

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天尝试结构化分析：**
> - "如何为不同类型代码审查定制分析框架？"
> - "用编程方式解析并操作结构化输出的最佳方法是什么？"
> - "如何确保不同审查会话中严重等级的一致性？"

<img src="../../../translated_images/zh-CN/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*四类框架支持一致代码审查，带严重性等级*

**多轮聊天** - 针对需上下文的对话。模型记忆前置消息并不断构建。适用于互动帮助或复杂问答。

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

*对话上下文在多轮过程中累计，直到达到令牌限制*

**逐步推理** - 需展示明确逻辑的问题。模型为每一步提供显式推理。适合数学题、逻辑谜题，或需要理解决策过程时。

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

*将问题分解为明确的逻辑步骤*

**受限输出** - 针对需特定格式要求的回复。模型严格遵守格式和长度规则。适用于摘要或需精确输出结构的情况。

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

*要求特定格式、长度和结构规则*

## 使用现有 Azure 资源

**验证部署：**

确保根目录下存在包含 Azure 凭证的 `.env` 文件（模块 01 部署时创建）：
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用：**

> **注意：** 如果你已在模块 01 中通过 `./start-all.sh` 启动所有应用，本模块已在端口 8083 运行。你可以跳过以下启动命令，直接访问 http://localhost:8083。

**选项 1：使用 Spring Boot Dashboard（推荐 VS Code 用户）**

开发容器包含 Spring Boot Dashboard 扩展，提供可视界面管理所有 Spring Boot 应用。在 VS Code 左侧活动栏中找到 Spring Boot 图标即可访问。
从 Spring Boot 仪表盘，您可以：
- 查看工作区中所有可用的 Spring Boot 应用程序
- 一键启动/停止应用程序
- 实时查看应用程序日志
- 监控应用程序状态

只需点击“prompt-engineering”旁边的播放按钮即可启动此模块，或者一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**选项 2：使用 shell 脚本**

启动所有Web应用程序（模块 01-04）：

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

这两个脚本都会自动从根目录 `.env` 文件加载环境变量，如果 JAR 文件不存在则会构建它们。

> **注意：** 如果您更喜欢在启动前手动构建所有模块：
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

在浏览器中打开 http://localhost:8083。

**停止：**

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

*主仪表盘显示所有 8 种 prompt engineering 模式及其特征和使用场景*

## 探索模式

网页界面允许您尝试不同的提示策略。每种模式解决不同的问题——试试看，了解每种方法何时最有效。

### 低主动性 vs 高主动性

使用低主动性提问简单问题，比如“200 的 15% 是多少？”。您会立即得到直接答案。现在，用高主动性提问复杂问题，如“为高流量 API 设计缓存策略”。观察模型放慢速度，提供详细推理。同一个模型，同样的问题结构——但提示告诉它思考多少。

<img src="../../../translated_images/zh-CN/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*快速计算，推理最少*

<img src="../../../translated_images/zh-CN/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*详尽缓存策略（2.8MB）*

### 任务执行（工具前言）

多步工作流受益于提前规划和过程叙述。模型会概述它将做什么，叙述每一步，然后总结结果。

<img src="../../../translated_images/zh-CN/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*分步骤叙述创建REST端点（3.9MB）*

### 自我反思代码

试试“创建一个邮件验证服务”。模型不仅生成代码并停止，而是生成后按质量标准评估，识别弱点并改进。您会看到它不断迭代，直到代码达生产标准。

<img src="../../../translated_images/zh-CN/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*完整的邮件验证服务（5.2MB）*

### 结构化分析

代码审查需要一致的评估框架。模型使用固定类别（正确性、实践、性能、安全）并按严重级别分析代码。

<img src="../../../translated_images/zh-CN/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*基于框架的代码审查*

### 多轮对话

问“什么是 Spring Boot？”，随即跟问“给我一个示例”。模型记得第一个问题，并针对性地给出一个 Spring Boot 示例。没了记忆，第二个问题会太模糊。

<img src="../../../translated_images/zh-CN/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*问题间的上下文保持*

### 逐步推理

选一个数学题，用逐步推理和低主动性分别试试。低主动性只给答案——快速但不透明。逐步推理展示了每步计算和决策。

<img src="../../../translated_images/zh-CN/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*带明确步骤的数学题*

### 受限输出

当需要特定格式或字数时，这种模式强制严格遵守。试着生成一个正好100字的项目符号格式总结。

<img src="../../../translated_images/zh-CN/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*带格式控制的机器学习总结*

## 你真正学到的是什么

**推理努力改变一切**

GPT-5.2 通过提示让你控制计算努力。低努力意味着快速响应且探索有限。高努力意味着模型花时间深入思考。你正在学习如何匹配任务复杂度调整努力——简单问题别浪费时间，复杂决策也别急。

**结构引导行为**

注意提示中的 XML 标签？它们不是装饰。模型比起自由文本，更能可靠遵循结构化指令。需要多步或复杂逻辑时，结构帮助模型知道自己在哪步、接下来做什么。

<img src="../../../translated_images/zh-CN/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*结构化良好的提示示例，含清晰区块和 XML 风格组织*

**通过自我评估提升质量**

自我反思模式通过明确质量标准工作。不再靠模型“自己做对”，而是告诉它“对”是什么：逻辑正确、错误处理、性能、安全。模型可以自评并改进。这让代码生成不再是运气，而是过程。

**上下文是有限的**

多轮对话依赖每次请求包含历史消息。但有上限——每个模型都有最大token数。随着对话增长，你需要策略保持相关上下文且不过载。本模块展示记忆工作原理；后续学习何时总结、何时遗忘、何时检索。

## 下一步

**下一个模块：** [03-rag - RAG（检索增强生成）](../03-rag/README.md)

---

**导航：** [← 上一篇：模块 01 - 入门](../01-introduction/README.md) | [回到主页](../README.md) | [下一篇：模块 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件通过AI翻译服务[Co-op Translator](https://github.com/Azure/co-op-translator)进行翻译。虽然我们力求准确，但请注意自动翻译可能存在错误或不准确之处。原始文件的母语版本应被视为权威来源。对于重要信息，建议采用专业人工翻译。我们不对因使用本翻译而产生的任何误解或误释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->