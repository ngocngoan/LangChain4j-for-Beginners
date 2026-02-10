# 模块 02：使用 GPT-5.2 的提示工程

## 目录

- [你将学到什么](../../../02-prompt-engineering)
- [先决条件](../../../02-prompt-engineering)
- [理解提示工程](../../../02-prompt-engineering)
- [本模块如何使用 LangChain4j](../../../02-prompt-engineering)
- [核心模式](../../../02-prompt-engineering)
- [使用现有 Azure 资源](../../../02-prompt-engineering)
- [应用程序截图](../../../02-prompt-engineering)
- [探索这些模式](../../../02-prompt-engineering)
  - [低积极度与高积极度](../../../02-prompt-engineering)
  - [任务执行（工具前言）](../../../02-prompt-engineering)
  - [自我反思代码](../../../02-prompt-engineering)
  - [结构化分析](../../../02-prompt-engineering)
  - [多轮对话](../../../02-prompt-engineering)
  - [逐步推理](../../../02-prompt-engineering)
  - [受限输出](../../../02-prompt-engineering)
- [你真正学到的是什么](../../../02-prompt-engineering)
- [下一步](../../../02-prompt-engineering)

## 你将学到什么

在前一个模块中，你了解了内存如何辅助对话式 AI，并使用 GitHub 模型进行基本交互。现在我们将重点讲解如何提问——即提示本身——利用 Azure OpenAI 的 GPT-5.2。你构造提示的方式会显著影响得到的回答质量。

我们使用 GPT-5.2 是因为它引入了推理控制——你可以告诉模型在回答之前要思考多少。这使得不同的提示策略更加明显，并帮助你理解何时使用哪种方法。相比 GitHub 模型，Azure 对 GPT-5.2 的限流更少，我们也能从中受益。

## 先决条件

- 已完成模块 01（部署了 Azure OpenAI 资源）
- 根目录下有 `.env` 文件，包含 Azure 凭据（由模块 01 中的 `azd up` 创建）

> **注意：** 如果你还没完成模块 01，请先按照那里的部署说明操作。

## 理解提示工程

提示工程即设计输入文本，使你能够持续获得所需结果。这不仅是提问的问题，更是构建请求结构，使模型准确理解你的需求和输出方式。

你可以把它想象成在给同事下指令。“修复这个错误”太笼统了。“通过添加空检查修复 UserService.java 第 45 行的空指针异常”则具体清晰。语言模型也一样——具体和结构同样重要。

## 本模块如何使用 LangChain4j

本模块展示了基于之前模块中相同 LangChain4j 基础的高级提示模式，重点在提示结构和推理控制。

<img src="../../../translated_images/zh-CN/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4j 如何将你的提示连接到 Azure OpenAI GPT-5.2*

**依赖项** — 模块 02 使用 `pom.xml` 中定义的以下 langchain4j 依赖：
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel 配置** — [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

聊天模型作为 Spring Bean 手动配置，使用支持 Azure OpenAI 端点的官方 OpenAI 客户端。与模块 01 的关键区别在于我们如何构造发送到 `chatModel.chat()` 的提示，而不是模型本身的设置。

**系统消息和用户消息** — [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j 为了清晰分离消息类型。`SystemMessage` 设置 AI 的行为和上下文（如“你是代码审查员”），而 `UserMessage` 包含实际请求。这种分离使你能够保持 AI 行为在不同用户查询中的一致性。

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/zh-CN/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage 提供持久上下文，UserMessage 包含单独请求*

**多轮对话的 MessageWindowChatMemory** — 对于多轮对话模式，我们复用模块 01 的 `MessageWindowChatMemory`。每个会话拥有自己的内存实例，存储于 `Map<String, ChatMemory>` 中，支持多个并发会话而不混淆上下文。

**提示模板** — 这里的重点是提示工程，而非 LangChain4j 新 API。每种模式（低积极度、高积极度、任务执行等）都通过相同的 `chatModel.chat(prompt)` 方法，但使用精心构造的提示字符串。XML 标签、指令和格式都是提示文本的一部分，而非 LangChain4j 特性。

**推理控制** — GPT-5.2 的推理努力通过提示中的指令控制，比如“最多推理2步”或“充分探索”。这些是提示工程技术，而非 LangChain4j 配置。库只是将你的提示送给模型。

关键要点：LangChain4j 提供基础设施（通过 [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java) 连接模型，内存和消息处理通过 [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)），而本模块教你如何在该基础设施中编写有效提示。

## 核心模式

不是所有问题都需要同样方法。有些问题需要快速回答，有些需要深入思考。有些需要可见的推理过程，有些只要结果即可。本模块涵盖八种提示模式——每种针对不同场景优化。你将体验所有模式，学习何时使用最佳。

<img src="../../../translated_images/zh-CN/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*八种提示工程模式及其应用场景概览*

<img src="../../../translated_images/zh-CN/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*低积极度（快速直接）与高积极度（深入探索）推理方法对比*

**低积极度（快速且集中）** — 适用于简单问题，需要快速直接答案。模型进行最少推理——最多两步。适用于计算、查询或直接问题。

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **用 GitHub Copilot 探索：** 打开 [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)，尝试提问：
> - “低积极度和高积极度提示模式的区别是什么？”
> - “提示中的 XML 标签如何帮助构建 AI 回答结构？”
> - “何时使用自我反思模式，何时用直接指令？”

**高积极度（深度且透彻）** — 适用于复杂问题，需要全面分析。模型全面探索，展示详细推理。用于系统设计、架构决策或复杂调研。

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**任务执行（逐步推进）** — 适用于多步骤工作流程。模型先提供整体计划，工作时逐步解说，最后给出总结。用于迁移、实施或任何多步骤过程。

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

链式推理提示明确要求模型展示思考过程，提高复杂任务的准确性。逐步分解有助于人类和 AI 理解逻辑。

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 试试：** 询问此模式：
> - “如何为长时间运行操作调整任务执行模式？”
> - “生产环境中如何构建工具前言的最佳实践？”
> - “如何在 UI 中捕捉并展示中间进度？”

<img src="../../../translated_images/zh-CN/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*规划 → 执行 → 总结的多步骤任务工作流程*

**自我反思代码** — 用于生成生产级代码。模型生成代码，基于质量标准检查并迭代改进。适用于构建新功能或服务。

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

**结构化分析** — 用于一致性评估。模型采用固定框架审查代码（正确性、规范、性能、安全）。适合代码审查或质量评估。

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

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 试试：** 关于结构化分析提问：
> - “如何针对不同代码审查类型定制分析框架？”
> - “如何程序化解析和处理结构化输出？”
> - “如何确保不同审查会话中一致的严重级别？”

<img src="../../../translated_images/zh-CN/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*四类框架覆盖一致的代码审查及严重级别*

**多轮对话** — 适用于需要上下文的对话。模型记住之前消息，持续构建上下文。用于交互式帮助或复杂问答。

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

*对话上下文如何随多轮积累直到达到 token 上限*

**逐步推理** — 适用于需要可见逻辑的问题。模型显示每步明确的推理过程。适合数学题、逻辑谜题或需要理解思考过程的场景。

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

*将问题分解为明确逻辑步骤*

**受限输出** — 适用于要求特定格式的回答。模型严格遵守格式和长度规则。用于摘要或需要精确输出结构的场合。

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

*强制执行特定格式、长度和结构要求*

## 使用现有 Azure 资源

**验证部署：**

确保根目录存在 `.env` 文件，包含 Azure 凭据（模块 01 创建）：
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用程序：**

> **注意：** 如果你已经使用模块 01 中的 `./start-all.sh` 启动了所有应用，本模块已经在 8083 端口运行。你可以跳过下面的启动命令，直接访问 http://localhost:8083 。

**选项 1：使用 Spring Boot Dashboard（推荐 VS Code 用户）**

开发容器包含 Spring Boot Dashboard 扩展，提供管理所有 Spring Boot 应用的可视界面。你可以在 VS Code 左侧活动栏中找到它（查看 Spring Boot 图标）。

通过 Spring Boot Dashboard，你可以：
- 查看工作区中所有可用的 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监控应用状态

点击 “prompt-engineering” 旁的播放按钮开始此模块，或者一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**选项 2：使用 Shell 脚本**

启动所有 Web 应用（模块 01-04）：

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

或者仅启动本模块：

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

脚本会自动从根目录的 `.env` 文件加载环境变量，并在 JAR 不存在时构建它们。

> **注意：** 如果你想先手动构建所有模块再启动：
>
> **Bash：**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell：**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

在浏览器中打开 http://localhost:8083 。

**停止应用：**

**Bash：**
```bash
./stop.sh  # 仅此模块
# 或
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell：**
```powershell
.\stop.ps1  # 仅限此模块
# 或
cd ..; .\stop-all.ps1  # 所有模块
```

## 应用程序截图

<img src="../../../translated_images/zh-CN/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*主仪表板，显示所有 8 种提示工程模式及其特点和应用场景*

## 探索这些模式

网页界面让你体验不同的提示策略。每种模式解决不同问题——试试它们，看看每种方法的优势。

### 低积极度与高积极度

用低积极度提问一个简单问题，比如“200 的 15% 是多少？”你会立刻得到直接答案。现在用高积极度询问复杂问题，比如“为高流量 API 设计缓存策略”。观察模型如何放慢速度，提供详细推理。同一个模型，相同提问结构——但提示告诉它要思考多少。
<img src="../../../translated_images/zh-CN/low-eagerness-demo.898894591fb23aa0.webp" alt="低积极性演示" width="800"/>

*快速计算，推理最少*

<img src="../../../translated_images/zh-CN/high-eagerness-demo.4ac93e7786c5a376.webp" alt="高积极性演示" width="800"/>

*全面缓存策略（2.8MB）*

### 任务执行（工具前置提示）

多步骤工作流程受益于提前规划和过程讲解。模型会概述其将要做的事，讲述每一步，然后总结结果。

<img src="../../../translated_images/zh-CN/tool-preambles-demo.3ca4881e417f2e28.webp" alt="任务执行演示" width="800"/>

*创建带有逐步讲解的REST端点（3.9MB）*

### 自我反思代码

试试“创建一个邮件验证服务”。模型不仅生成代码然后停止，还会根据质量标准进行评估，识别缺点并改进。你会看到它反复迭代，直到代码达到生产标准。

<img src="../../../translated_images/zh-CN/self-reflecting-code-demo.851ee05c988e743f.webp" alt="自我反思代码演示" width="800"/>

*完整的邮件验证服务（5.2MB）*

### 结构化分析

代码审查需要一致的评估框架。模型使用固定类别（正确性、实践、性能、安全）和严重性级别来分析代码。

<img src="../../../translated_images/zh-CN/structured-analysis-demo.9ef892194cd23bc8.webp" alt="结构化分析演示" width="800"/>

*基于框架的代码审查*

### 多轮聊天

问“什么是Spring Boot？”，然后紧接着问“给我一个例子”。模型记得你的第一个问题，并专门给你一个Spring Boot的示例。没有记忆，第二个问题会太含糊。

<img src="../../../translated_images/zh-CN/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="多轮聊天演示" width="800"/>

*跨问题的上下文保持*

### 逐步推理

选择一个数学问题，分别使用逐步推理和低积极性尝试。低积极性只是给答案——快速但不透明。逐步推理则展示每个计算和决策。

<img src="../../../translated_images/zh-CN/step-by-step-reasoning-demo.12139513356faecd.webp" alt="逐步推理演示" width="800"/>

*带明确步骤的数学问题*

### 受限输出

当你需要特定格式或字数时，这种模式能严格执行。试着生成一个恰好100字的项目符号格式摘要。

<img src="../../../translated_images/zh-CN/constrained-output-demo.567cc45b75da1633.webp" alt="受限输出演示" width="800"/>

*带格式控制的机器学习摘要*

## 你真正学到的是什么

**推理努力改变一切**

GPT-5.2允许你通过提示控制计算努力。低努力意味着快速响应和最小探索。高努力意味着模型花时间深入思考。你学会根据任务复杂性匹配努力——简单问题不浪费时间，复杂决策也不匆忙。

**结构引导行为**

注意提示里的XML标签？它们不是装饰。模型比起自由文本更可靠地遵循结构化指令。当你需要多步骤过程或复杂逻辑时，结构帮助模型跟踪当前步骤和接下来发生的事。

<img src="../../../translated_images/zh-CN/prompt-structure.a77763d63f4e2f89.webp" alt="提示结构" width="800"/>

*结构良好的提示构成，有清晰部分和XML风格组织*

**通过自我评估保证质量**

自我反思模式通过明确质量标准来运作。你不是希望模型“正确完成”，而是告诉模型“正确”具体意味着什么：逻辑正确、错误处理、性能、安全性。模型因此能自我评估输出并改进。这让代码生成从赌博变成流程。

**上下文是有限的**

多轮对话通过每次请求包含消息历史实现。但有上限——每个模型都有最大Token数。随着对话增长，你需要策略保留相关上下文同时不超限。本模块向你展示记忆机制；以后你会学会何时总结、何时遗忘、何时检索。

## 下一步

**下一个模块：** [03-rag - RAG（检索增强生成）](../03-rag/README.md)

---

**导航：** [← 上一个：模块 01 - 介绍](../01-introduction/README.md) | [回到主页](../README.md) | [下一个：模块 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件使用 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。尽管我们力求准确，但请注意自动翻译可能存在错误或不准确之处。应以原始语言的原文档作为权威来源。对于重要信息，建议使用专业人工翻译。我们不对因使用此翻译而产生的任何误解或误释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->