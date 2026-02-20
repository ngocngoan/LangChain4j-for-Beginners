# Module 02: 使用 GPT-5.2 进行提示工程

## 目录

- [你将学习什么](../../../02-prompt-engineering)
- [先决条件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [提示工程基础](../../../02-prompt-engineering)
  - [零样本提示](../../../02-prompt-engineering)
  - [少样本提示](../../../02-prompt-engineering)
  - [思路链](../../../02-prompt-engineering)
  - [基于角色的提示](../../../02-prompt-engineering)
  - [提示模板](../../../02-prompt-engineering)
- [高级模式](../../../02-prompt-engineering)
- [使用现有的 Azure 资源](../../../02-prompt-engineering)
- [应用截图](../../../02-prompt-engineering)
- [探索模式](../../../02-prompt-engineering)
  - [低热情与高热情](../../../02-prompt-engineering)
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

在上一模块中，你了解了记忆如何支持对话式 AI，并使用 GitHub 模型进行了基本交互。现在，我们将专注于如何提问——也就是提示本身，使用 Azure OpenAI 的 GPT-5.2。你构建提示的方式会显著影响答案的质量。我们将从复习基本的提示技术开始，然后进入利用 GPT-5.2 完整能力的八种高级模式。

我们选用 GPT-5.2 是因为它引入了推理控制——你可以告诉模型在回答前需要思考多少。这使得不同的提示策略更加明显，帮助你理解何时使用哪种方法。相比 GitHub 模型，Azure 对 GPT-5.2 的速率限制更少，这也是我们的优势。

## 先决条件

- 已完成模块 01（部署了 Azure OpenAI 资源）
- 根目录中有包含 Azure 凭据的 `.env` 文件（由模块 01 中的 `azd up` 创建）

> **注意：** 如果还没完成模块 01，请先按照那里的部署说明操作。

## 理解提示工程

<img src="../../../translated_images/zh-CN/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="什么是提示工程？" width="800"/>

提示工程是设计输入文本，以稳定获得所需结果的艺术。它不仅仅是提问，而是结构化请求，让模型准确理解你想要什么以及如何实现。

把它想象成给同事下指令。“修复 bug”很模糊，“修复 UserService.java 第 45 行的空指针异常，添加空检查”则具体得多。语言模型的工作机制也一样——具体性和结构化很重要。

<img src="../../../translated_images/zh-CN/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j 的作用" width="800"/>

LangChain4j 提供基础设施——模型连接、记忆和消息类型，而提示模式只是通过这些基础设施传递的精心构造文本。关键构建块是 `SystemMessage`（设置 AI 行为和角色）和 `UserMessage`（携带你的实际请求）。

## 提示工程基础

<img src="../../../translated_images/zh-CN/five-patterns-overview.160f35045ffd2a94.webp" alt="五种提示工程模式概述" width="800"/>

在深入本模块的高级模式之前，先回顾五种基础提示技术。这是每个提示工程师应知的构建块。如果你已经完成了 [快速入门模块](../00-quick-start/README.md#2-prompt-patterns)，你已经见过它们的实际应用——这里是它们的概念框架。

### 零样本提示

最简单的方式：无示例直接给模型指令。模型完全依赖训练理解并执行任务。适合简单且预期行为明确的请求。

<img src="../../../translated_images/zh-CN/zero-shot-prompting.7abc24228be84e6c.webp" alt="零样本提示" width="800"/>

*无示例直接指令——模型仅通过指令推断任务*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// 响应：“积极”
```
  
**适用场景：** 简单分类、直接问题、翻译，或模型无需额外指导即可处理的任何任务。

### 少样本提示

提供示例，展示想让模型遵循的模式。模型从示例中学习期望的输入输出格式，并应用于新输入。这大大提高了当期望格式或行为不明显时的一致性。

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
  
**适用场景：** 定制分类、一致格式、领域特定任务，或零样本结果不稳定时。

### 思路链

让模型展示逐步推理。模型不会直接给出答案，而是拆解问题，明确地展示每个步骤。提升数学、逻辑及多步骤推理任务的准确性。

<img src="../../../translated_images/zh-CN/chain-of-thought.5cff6630e2657e2a.webp" alt="思路链提示" width="800"/>

*逐步推理——将复杂问题分解为明确的逻辑步骤*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// 模型显示：15 - 8 = 7，然后7 + 12 = 19个苹果
```
  
**适用场景：** 数学题、逻辑难题、调试，或任何展示推理过程能提升准确性和可信度的任务。

### 基于角色的提示

在提问前设定 AI 的身份或角色。提供上下文，塑造回答的语气、深度和重点。“软件架构师”与“初级开发者”或“安全审计员”的建议完全不同。

<img src="../../../translated_images/zh-CN/role-based-prompting.a806e1a73de6e3a4.webp" alt="基于角色的提示" width="800"/>

*设定上下文和身份——相同问题根据分配角色获得不同答案*

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
  
**适用场景：** 代码审查、辅导、领域特定分析，或需要根据特定专业水平或视角调整回答时。

### 提示模板

创建带变量占位符的可复用提示。无需每次写新提示，只要定义一次模板，再填入不同值。LangChain4j 的 `PromptTemplate` 类通过 `{{variable}}` 语法轻松实现。

<img src="../../../translated_images/zh-CN/prompt-templates.14bfc37d45f1a933.webp" alt="提示模板" width="800"/>

*带变量占位符的可复用提示——一个模板，多种用途*

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

这五个基础技巧为大多数提示任务提供了扎实工具包。本模块剩余内容基于它们，介绍**八种高级模式**，充分利用 GPT-5.2 的推理控制、自我评估和结构化输出能力。

## 高级模式

基础内容讲完，接下来介绍本模块独有的八种高级模式。不同问题需要不同方法。有些问题需快速回答，有些需深度思考。有些要求展示推理过程，有些只要结果。下面的每个模式针对不同场景优化——而 GPT-5.2 的推理控制使差异更明显。

<img src="../../../translated_images/zh-CN/eight-patterns.fa1ebfdf16f71e9a.webp" alt="八种提示工程模式" width="800"/>

*八种提示工程模式及其使用场景概览*

<img src="../../../translated_images/zh-CN/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 推理控制" width="800"/>

*GPT-5.2 的推理控制让你指定模型需要多少思考——从快速直接答复到深入探索*

**低热情（快速且聚焦）** - 适用简单问题，追求快速直接回答。模型最少推理——最多 2 步。用于计算、查询或简单问题。

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
> - “提示中的 XML 标签如何帮助结构化 AI 的答复？”
> - “什么时候该用自我反思模式，什么时候用直接指令？”

**高热情（深入且彻底）** - 适合复杂问题，需求全面分析。模型进行充分探索，展示详细推理。用于系统设计、架构决策或复杂研究。

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**任务执行（逐步推进）** - 面向多步骤工作流。模型提供预先计划，边执行边描述步骤，最后给出总结。用于迁移、实现或任意多步骤过程。

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
  
思路链提示明确让模型展现推理过程，提升复杂任务准确性。逐步拆解逻辑帮助人类和 AI 理解流程。

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 询问此模式：
> - “如何改编任务执行模式以支持长时间运行操作？”
> - “在生产应用中，如何构造工具前言的最佳实践？”
> - “如何捕获并显示 UI 中的中间进度更新？”

<img src="../../../translated_images/zh-CN/task-execution-pattern.9da3967750ab5c1e.webp" alt="任务执行模式" width="800"/>

*计划 → 执行 → 总结，多步骤任务完整工作流*

**自我反思代码** - 用于生成生产质量代码。模型生成符合生产标准、带完善错误处理的代码。适合构建新功能或服务时使用。

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/zh-CN/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="自我反思循环" width="800"/>

*迭代改进循环——生成，评估，识别问题，改进，重复*

**结构化分析** - 确保评审的一致性。模型根据固定框架审查代码（正确性、实践、性能、安全性和可维护性）。用于代码审查或质量评估。

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
  
> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 询问结构化分析：
> - “如何为不同类型代码审查定制分析框架？”
> - “程序化解析并处理结构化输出的最佳方法？”
> - “如何确保不同审查会话中的严重级别一致？”

<img src="../../../translated_images/zh-CN/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="结构化分析模式" width="800"/>

*用于标准化代码审查及严重级别的框架*

**多轮聊天** - 适用需上下文的对话。模型记忆前次消息并基于其构建回答。用于交互式帮助或复杂问答场景。

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

*多轮对话中上下文如何积累直至达到令牌限制*

**逐步推理** - 面向需展示显式逻辑的任务。模型针对每一步展示明确推理。适合数学题、逻辑难题，或需了解思考过程时。

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

*将问题拆解为明确的逻辑步骤*

**受限输出** - 要求输出格式严格的场景。模型严格遵守格式和长度规则。用于摘要或需精准输出结构时。

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

*强制执行特定的格式、长度和结构要求*

## 使用现有的 Azure 资源

**验证部署：**

确保根目录有包含 Azure 凭据的 `.env` 文件（由模块 01 创建）：
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```
  
**启动应用：**

> **注意：** 如果你已用模块 01 的 `./start-all.sh` 启动了所有应用，本模块应用已经在 8083 端口运行。你可以跳过下面的启动命令，直接访问 http://localhost:8083。

**选项 1：使用 Spring Boot Dashboard（推荐给 VS Code 用户）**

开发容器包含 Spring Boot Dashboard 扩展，为你提供可视界面管理所有 Spring Boot 应用。它位于 VS Code 左侧活动栏（找 Spring Boot 图标）。

你可以通过 Spring Boot Dashboard：
- 查看工作区内所有可用 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监控应用状态
只需点击“prompt-engineering”旁边的播放按钮即可启动此模块，或一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

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

或者只启动该模块：

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

两个脚本都会自动从根目录的 `.env` 文件加载环境变量，如果 JAR 文件不存在则进行构建。

> **注意：** 如果你更愿意在启动前手动构建所有模块：
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

**停止方法：**

**Bash:**
```bash
./stop.sh  # 仅限此模块
# 或者
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell:**
```powershell
.\stop.ps1  # 仅此模块
# 或
cd ..; .\stop-all.ps1  # 所有模块
```

## 应用截图

<img src="../../../translated_images/zh-CN/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主仪表盘展示了所有 8 种提示工程模式及其特点和使用场景*

## 探索这些模式

Web 界面允许你尝试不同的提示策略。每种模式解决不同的问题——试试看，了解每种方法何时最有效。

### 低主动性与高主动性

用低主动性模式问一个简单问题，比如“15% 的 200 是多少？”。你会立即得到直接答案。现在用高主动性模式问一个复杂的问题，比如“为高流量 API 设计缓存策略”。观察模型如何放慢速度并提供详细推理。同一个模型，同样的问题结构——但提示告诉它需要多少思考。

<img src="../../../translated_images/zh-CN/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*快速计算，推理最少*

<img src="../../../translated_images/zh-CN/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*全面的缓存策略（2.8MB）*

### 任务执行（工具预置）

多步工作流受益于事先规划和过程讲述。模型会先概述它将做什么，讲述每一步，然后总结结果。

<img src="../../../translated_images/zh-CN/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*逐步讲述创建 REST 端点（3.9MB）*

### 自我反思代码

尝试“创建一个电子邮件验证服务”。模型不仅仅生成代码然后停止，而是生成代码、根据质量标准进行评估、发现弱点并改进。你将看到它不断迭代，直到代码达到生产标准。

<img src="../../../translated_images/zh-CN/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*完整的电子邮件验证服务（5.2MB）*

### 结构化分析

代码审查需要一致的评估框架。模型使用固定类别（正确性、实践、性能、安全性）及严重等级对代码进行分析。

<img src="../../../translated_images/zh-CN/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*基于框架的代码审查*

### 多轮对话

问“什么是 Spring Boot？”然后立刻跟进“给我举个例子”。模型会记住你的第一个问题，并专门给出 Spring Boot 的示例。如果没有记忆，第二个问题将太模糊。

<img src="../../../translated_images/zh-CN/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*问题间的上下文保持*

### 逐步推理

选择一个数学问题，分别用逐步推理和低主动性进行尝试。低主动性只给答案——快速但不透明。逐步推理会展示每个计算和决策过程。

<img src="../../../translated_images/zh-CN/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*带明确步骤的数学题*

### 受限输出

当你需要特定格式或字数时，这种模式强制严格遵守。尝试生成一个恰好 100 个词且采用项目符号格式的总结。

<img src="../../../translated_images/zh-CN/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*带格式控制的机器学习总结*

## 你真正学到的是什么

**推理努力改变一切**

GPT-5.2 允许你通过提示控制计算努力。低努力意味着快速响应，探索最少。高努力意味着模型花时间深入思考。你正在学习根据任务复杂度匹配努力——不要在简单问题上浪费时间，但也别匆忙做复杂决策。

**结构指导行为**

注意提示中的 XML 标签？它们不是装饰。模型比自由文本更可靠地遵循结构化指令。当你需要多步流程或复杂逻辑时，结构帮助模型跟踪当前位置和接下来要做的事。

<img src="../../../translated_images/zh-CN/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*良好结构化提示的构成，带有清晰章节和 XML 风格组织*

**通过自我评估保证质量**

自我反思模式通过明确质量标准工作。不再寄希望于模型“正确做”，而是告诉它“正确”意味着什么：逻辑正确、错误处理、性能、安全。模型可以自我评价输出并改进。这使代码生成从随机过程变成可控流程。

**上下文是有限的**

多轮对话通过包含消息历史实现。但有限制——每个模型都有最大令牌数。随着对话增长，你需要策略保持相关上下文同时不超限。本模块展示了记忆怎么工作；后面你会学到何时总结、何时遗忘、何时检索。

## 下一步

**下一个模块：** [03-rag - RAG（检索增强生成）](../03-rag/README.md)

---

**导航：** [← 上一个：模块 01 - 介绍](../01-introduction/README.md) | [回到主页](../README.md) | [下一个：模块 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由人工智能翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译完成。虽然我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版本的文件应被视为权威来源。对于重要信息，建议使用专业人工翻译。因使用本翻译而产生的任何误解或误读，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->