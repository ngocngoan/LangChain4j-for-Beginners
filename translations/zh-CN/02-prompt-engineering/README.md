# Module 02: 使用 GPT-5.2 进行提示工程

## 目录

- [视频讲解](../../../02-prompt-engineering)
- [你将学到什么](../../../02-prompt-engineering)
- [先决条件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基础](../../../02-prompt-engineering)
  - [零样本提示](../../../02-prompt-engineering)
  - [少样本提示](../../../02-prompt-engineering)
  - [链式思维](../../../02-prompt-engineering)
  - [基于角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [高级模式](../../../02-prompt-engineering)
- [使用现有 Azure 资源](../../../02-prompt-engineering)
- [应用截图](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低意愿与高意愿](../../../02-prompt-engineering)
  - [任务执行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代码](../../../02-prompt-engineering)
  - [结构化分析](../../../02-prompt-engineering)
  - [多轮对话](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限输出](../../../02-prompt-engineering)
- [你真正学到了什么](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 视频讲解

观看此直播课程，了解如何开始本模块：[Prompt Engineering with LangChain4j - 直播课程](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## 你将学到什么

<img src="../../../translated_images/zh-CN/what-youll-learn.c68269ac048503b2.webp" alt="你将学到什么" width="800"/>

在上一模块中，你了解了内存如何赋能对话式 AI，并使用 GitHub 模型进行基础交互。现在我们将重点关注如何提问——也就是提示本身——使用 Azure OpenAI 的 GPT-5.2。你设计提示的方式会极大影响收到的回答质量。我们从回顾基础提示技术开始，然后进入八个高级模式，充分利用 GPT-5.2 的能力。

我们使用 GPT-5.2，是因为它引入了推理控制——你可以告诉模型在回答前需要思考多少。这让不同的提示策略更明显，帮助你理解何时使用每种方法。相比 GitHub 模型，Azure 对 GPT-5.2 的限流更少，这也带来更多便利。

## 先决条件

- 完成模块 01（已部署 Azure OpenAI 资源）
- 根目录下有 `.env` 文件，内含 Azure 凭据（由模块 01 中的 `azd up` 创建）

> **注意：** 如果还未完成模块 01，请先按照那里的部署说明操作。

## 理解提示工程

<img src="../../../translated_images/zh-CN/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什么是提示工程？" width="800"/>

提示工程是设计输入文本，使你能持续获得所需结果的过程。它不仅仅是提问——而是构建请求，让模型准确理解你的需求和交付方式。

把它想象成给同事下指令。“修复错误”太模糊了；“通过添加空检查修复 UserService.java 第 45 行的空指针异常”则很具体。语言模型也一样——具体性和结构很重要。

<img src="../../../translated_images/zh-CN/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的作用" width="800"/>

LangChain4j 提供基础设施——模型连接、内存和消息类型——而提示模式只是你通过该基础设施发送的精心结构化文本。关键构建块是 `SystemMessage`（设置 AI 的行为和角色）和 `UserMessage`（携带你的实际请求）。

## 提示工程基础

<img src="../../../translated_images/zh-CN/five-patterns-overview.160f35045ffd2a94.webp" alt="五种提示工程模式概览" width="800"/>

在进入本模块高级模式前，我们先回顾五种基础提示技术。这是每位提示工程师都应了解的基石。如果你已经学习过[快速入门模块](../00-quick-start/README.md#2-prompt-patterns)，就见识过它们的实际应用——这里是背后的概念框架。

### 零样本提示

最简单的方法：直接给模型指令，无需示例。模型完全依赖训练理解和执行任务。适合行为明显的直接请求。

<img src="../../../translated_images/zh-CN/zero-shot-prompting.7abc24228be84e6c.webp" alt="零样本提示" width="800"/>

*无示例直接指令——模型从指令中推断任务*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 响应：“积极的”
```
  
**使用场景：** 简单分类、直接问答、翻译，或任何无需额外指导的任务。

### 少样本提示

提供示例，演示你希望模型遵循的模式。模型从示例中学习期望的输入输出格式，并应用到新输入上。对于期望格式或行为不明显的任务，这能显著提升一致性。

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
  
**使用场景：** 自定义分类、一致格式、特定领域任务，或零样本结果不稳定时。

### 链式思维

让模型展示推理步骤。模型不直接给出答案，而是拆解问题，逐步解决。提升数学、逻辑和多步推理任务的准确度。

<img src="../../../translated_images/zh-CN/chain-of-thought.5cff6630e2657e2a.webp" alt="链式思维提示" width="800"/>

*逐步推理——将复杂问题拆解成明确逻辑步骤*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型显示：15 - 8 = 7，然后7 + 12 = 19个苹果
```
  
**使用场景：** 数学题、逻辑谜题、调试，或展示推理过程提高准确度和信任度的任务。

### 基于角色的提示

设置 AI 扮演的身份或角色，再提问。这提供上下文，影响回答的语气、深度和重点。“软件架构师”给出的建议与“初级开发者”或“安全审核员”不同。

<img src="../../../translated_images/zh-CN/role-based-prompting.a806e1a73de6e3a4.webp" alt="基于角色的提示" width="800"/>

*设定上下文和角色——同一问题因角色不同而有不同回答*

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
  
**使用场景：** 代码评审、辅导、领域分析，或需要针对特定专业水平或视角的回答。

### 提示模板

创建可复用的提示，带有变量占位符。不用每次都写新提示，只需定义模板，传入不同值。LangChain4j 的 `PromptTemplate` 类通过 `{{variable}}` 语法简化操作。

<img src="../../../translated_images/zh-CN/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*带变量占位符的可复用提示——一套模板，多种用途*

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
  
**使用场景：** 多次查询不同输入、批量处理、构建可复用 AI 流程，或提示结构固定但数据变化的场景。

---

这五个基础构成你处理大多数提示任务的坚实工具箱。本模块其余内容将基于它们，介绍**八个高级模式**，利用 GPT-5.2 的推理控制、自我评估和结构化输出能力。

## 高级模式

基础知识讲完，我们进入本模块的八个高级模式，这些让本模块独树一帜。不同问题需不同策略——有些问题需快速答复，有些需深入思考。有些需要显式推理，有些只需结果。以下各模式针对不同场景优化——GPT-5.2 的推理控制让这些差异更明显。

<img src="../../../translated_images/zh-CN/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八种提示工程模式" width="800"/>

*八种提示工程模式及其适用场景概览*

<img src="../../../translated_images/zh-CN/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 推理控制" width="800"/>

*GPT-5.2 推理控制让你指定模型思考程度——从快速直接回答到深度探究*

**低意愿（快速且聚焦）** - 针对简单问题，迅速给出直接回答。模型推理最少——最多两步。适用于计算、查询或直接问题。

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
  
> 💡 **用 GitHub Copilot 探索：** 打开 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)，尝试问：
> - “低意愿提示和高意愿提示模式的区别是什么？”
> - “提示中 XML 标签如何帮助结构化 AI 的回答？”
> - “什么时候应该用自我反思模式，什么时候用直接指令？”

**高意愿（深度且全面）** - 针对复杂问题，提供全面分析。模型深度探索，展示详细推理。适合系统设计、架构决策或复杂调研。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**任务执行（逐步进展）** - 用于多步骤工作流。模型先给出计划，工作时边执行边讲解，最后总结。适合迁移、实施或任何多步流程。

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
  
链式思维提示明确要求模型展示推理过程，提升复杂任务准确度。逐步拆解利于人机理解逻辑。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 试试：** 询问此模式：
> - “我如何为长时间运行操作调整任务执行模式？”
> - “生产环境中结构工具前言的最佳实践是什么？”
> - “如何在 UI 中捕获和显示中间进度更新？”

<img src="../../../translated_images/zh-CN/task-execution-pattern.9da3967750ab5c1e.webp" alt="任务执行模式" width="800"/>

*计划 → 执行 → 总结的多步骤工作流*

**自我反思代码** - 用于生成生产质量代码。模型按照生产标准生成代码，含完善的错误处理。适合构建新功能或服务时使用。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/zh-CN/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循环" width="800"/>

*迭代改进循环 - 生成、评估、发现问题、改进、重复*

**结构化分析** - 用于一致性评估。模型使用固定框架（正确性、规范、性能、安全、可维护性）审查代码。适合代码审查或质量评估。

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
  
> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 试试结构化分析：**
> - “如何针对不同类型代码评审定制分析框架？”
> - “以编程方式解析和处理结构化输出的最佳方法是什么？”
> - “如何确保不同评审会话中的严重性级别一致？”

<img src="../../../translated_images/zh-CN/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="结构化分析模式" width="800"/>

*带严重级别的代码审查一致性框架*

**多轮对话** - 适合需要上下文的会话。模型记忆之前消息，基于历次互动构建对话。适合互动帮助或复杂问答。

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

*多轮对话中上下文累积，直至达到令牌限制*

**逐步推理** - 适用于需要显式逻辑的难题。模型展示逐步推理。适合数学题、逻辑谜题或需要理解思考过程的场景。

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

*将问题拆解成明确的逻辑步骤*

**受限输出** - 用于有具体格式要求的回答。模型严格遵守格式和长度规则。适合摘要或需要精确输出结构的场景。

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

*强制特定格式、长度和结构要求*

## 使用现有 Azure 资源

**验证部署：**

确保根目录下存在 `.env` 文件，内含 Azure 凭据（模块 01 部署时创建）：  
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```
  
**启动应用：**

> **注意：** 如果你已通过模块 01 的 `./start-all.sh` 启动了所有应用，本模块已运行在端口 8083。你可以跳过下面的启动命令，直接访问 http://localhost:8083 。

**选项 1：使用 Spring Boot Dashboard（推荐 VS Code 用户）**
开发容器包含了 Spring Boot Dashboard 扩展，它提供了一个可视化界面来管理所有 Spring Boot 应用程序。您可以在 VS Code 左侧的活动栏中找到它（寻找 Spring Boot 图标）。

通过 Spring Boot Dashboard，您可以：
- 查看工作区内所有可用的 Spring Boot 应用程序
- 一键启动/停止应用程序
- 实时查看应用程序日志
- 监控应用程序状态

只需点击“prompt-engineering”旁边的播放按钮即可启动该模块，或一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**选项2：使用 shell 脚本**

启动所有 Web 应用程序（模块 01-04）：

**Bash:**
```bash
cd ..  # 从根目录
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 从根目录开始
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

两个脚本都会自动从根目录的 `.env` 文件加载环境变量，如果 JAR 文件不存在，会自动构建。

> **注意：** 如果您希望在启动前手动构建所有模块：
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
# 或者
cd ..; .\stop-all.ps1  # 所有模块
```

## 应用程序截图

<img src="../../../translated_images/zh-CN/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主仪表板显示了所有 8 种提示工程模式及它们的特性和用例*

## 探索模式

网页界面让您可以尝试不同的提示策略。每种模式解决不同的问题——尝试它们，看看哪种方式最有效。

> **注意：流式与非流式** — 每个模式页面都提供两个按钮：**🔴 流式响应（实时）** 和 **非流式** 选项。流式使用服务器发送事件（SSE），在模型生成令牌时实时显示进度，让您立即看到响应。非流式选项则是等完整响应生成后再显示。对于触发深度推理的提示（例如高亢激励、自我反思代码），非流式调用可能非常耗时——甚至几分钟——且无任何反馈。**在尝试复杂提示时请使用流式，这样您可以看到模型的工作过程，避免以为请求超时。**
>
> **注意：浏览器要求** — 流式功能使用 Fetch Streams API（`response.body.getReader()`），需要完整浏览器支持（Chrome、Edge、Firefox、Safari）。它**不**支持 VS Code 内置的 Simple Browser，因为其 Webview 不支持 ReadableStream API。如果您使用 Simple Browser，非流式按钮仍然正常运行——仅流式按钮失效。请在外部浏览器中打开 `http://localhost:8083` 以获得完整体验。

### 低亢激励 vs 高亢激励

使用低亢激励提出一个简单问题，比如“200 的 15% 是多少？”您会得到即时、直接的答案。现在，用高亢激励问一个复杂问题，比如“为高流量 API 设计一个缓存策略”。点击 **🔴 流式响应（实时）**，观察模型逐个令牌显示详细推理。同一个模型、相同的问题结构——但提示让模型决定投入多少思考。

### 任务执行（工具前置）

多步骤工作流程受益于预先规划和过程叙述。模型会概述它将做什么，逐步叙述每一步，最后总结结果。

### 自我反思代码

尝试“创建一个邮箱验证服务”。模型不仅生成代码然后停止，它还会根据质量标准评估代码，识别不足，并进行改进。您将看到它反复迭代，直到代码达到生产标准。

### 结构化分析

代码审查需要一致的评估框架。模型使用固定分类（正确性、实践、性能、安全性）和严肃级别来分析代码。

### 多轮会话

问“什么是 Spring Boot？”然后立即问“给我一个示例”。模型记住您的第一个问题，专门给出一个 Spring Boot 示例。没有记忆，第二个问题会太模糊。

### 逐步推理

选择一个数学题，分别用逐步推理和低亢激励试试。低亢激励直接给答案——快速但不透明。逐步推理会展示每个计算和决策步骤。

### 受限输出

当您需要特定格式或字数时，这种模式强制严格遵守。尝试生成一个正好 100 字、以要点形式呈现的总结。

## 您真正学到的是什么

**推理努力改变一切**

GPT-5.2 允许您通过提示控制计算努力程度。低努力意味着快速回复和最少探索。高努力意味着模型花时间深入思考。您正在学习如何根据任务复杂度调整努力——简单问题不浪费时间，复杂决策不仓促。

**结构指导行为**

注意提示中的 XML 标签？它们不是装饰。模型更可靠地遵循结构化指令，而非自由文本。当您需要多步骤流程或复杂逻辑时，结构帮助模型追踪当前位置和下一步。

<img src="../../../translated_images/zh-CN/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*结构良好的提示示例，具有清晰的区块和 XML 式组织*

**质量通过自我评估**

自我反思模式通过明确质量标准来实现。您不是寄希望于模型“做对”，而是清楚告诉它“对”的标准：逻辑正确、错误处理、性能、安全。然后模型能评估自身输出并改进。这让代码生成不再靠运气，而是流程化。

**上下文是有限的**

多轮对话通过包含消息历史实现，但有最大令牌限制。随着对话增长，您需要策略来保留相关上下文而不超限。本模块展示了记忆如何工作，后续您将学习何时总结、何时遗忘、何时检索。

## 下一步

**下一个模块：** [03-rag - RAG（检索增强生成）](../03-rag/README.md)

---

**导航：** [← 上一节：模块 01 - 介绍](../01-introduction/README.md) | [返回主目录](../README.md) | [下一节：模块 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译而成。尽管我们努力确保翻译的准确性，但请注意自动翻译可能存在错误或不准确之处。原始语言版本的文件应被视为权威来源。对于重要信息，建议进行专业人工翻译。因使用本翻译内容而产生的任何误解或误释，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->