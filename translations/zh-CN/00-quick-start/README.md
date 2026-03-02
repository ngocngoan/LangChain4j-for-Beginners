# Module 00: 快速开始

## 目录

- [介绍](../../../00-quick-start)
- [什么是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依赖](../../../00-quick-start)
- [前提条件](../../../00-quick-start)
- [设置](../../../00-quick-start)
  - [1. 获取你的 GitHub 令牌](../../../00-quick-start)
  - [2. 设置你的令牌](../../../00-quick-start)
- [运行示例](../../../00-quick-start)
  - [1. 基础聊天](../../../00-quick-start)
  - [2. 提示模式](../../../00-quick-start)
  - [3. 函数调用](../../../00-quick-start)
  - [4. 文档问答（简单 RAG）](../../../00-quick-start)
  - [5. 负责任的 AI](../../../00-quick-start)
- [每个示例展示内容](../../../00-quick-start)
- [下一步](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 介绍

此快速入门旨在帮助你尽快使用 LangChain4j 启动运行。它涵盖了使用 LangChain4j 和 GitHub 模型构建 AI 应用程序的基础知识。在接下来的模块中，你将切换到 Azure OpenAI 和 GPT-5.2，深入探索每个概念。

## 什么是 LangChain4j？

LangChain4j 是一个简化构建 AI 驱动应用程序的 Java 库。你无需处理 HTTP 客户端和 JSON 解析，而是使用干净的 Java API。

LangChain 中的“链”指的是将多个组件串联起来——你可能将一个提示连接到模型，再连接到解析器，或者将多个 AI 调用串联起来，一个输出作为下一个输入。此快速开始侧重于基础知识，随后再探讨更复杂的链。

<img src="../../../translated_images/zh-CN/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*在 LangChain4j 中串联组件——通过构建模块连接创建强大的 AI 工作流*

我们将使用三个核心组件：

**ChatModel** — 用于与 AI 模型交互的接口。调用 `model.chat("prompt")` 得到响应字符串。我们使用 `OpenAiOfficialChatModel`，它支持与 OpenAI 兼容的端点，如 GitHub 模型。

**AiServices** — 创建类型安全的 AI 服务接口。定义方法，用 `@Tool` 注解它们，LangChain4j 负责协调。AI 会在需要时自动调用你的 Java 方法。

**MessageWindowChatMemory** — 维护对话历史。没有它，每个请求都是独立的。使用它后，AI 会记住之前的消息，并在多轮对话中保持上下文。

<img src="../../../translated_images/zh-CN/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架构——核心组件协同工作，为你的 AI 应用提供动力*

## LangChain4j 依赖

此快速入门在 [`pom.xml`](../../../00-quick-start/pom.xml) 中使用三个 Maven 依赖：

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official` 模块提供了连接 OpenAI 兼容 API 的 `OpenAiOfficialChatModel` 类。GitHub 模型使用相同的 API 格式，因此不需要特殊适配器——只需将基础 URL 指向 `https://models.github.ai/inference`。

`langchain4j-easy-rag` 模块提供自动文档拆分、嵌入和检索，方便你构建 RAG 应用，无需手动配置每个步骤。

## 前提条件

**使用开发容器？** Java 和 Maven 已预装。你只需要一个 GitHub 个人访问令牌。

**本地开发：**
- Java 21+，Maven 3.9+
- GitHub 个人访问令牌（见下方说明）

> **注意：** 本模块使用 GitHub 模型中的 `gpt-4.1-nano`。请勿修改代码中的模型名称——它已配置为与 GitHub 提供的模型兼容。

## 设置

### 1. 获取你的 GitHub 令牌

1. 前往 [GitHub 设置 → 个人访问令牌](https://github.com/settings/personal-access-tokens)
2. 点击“生成新令牌”
3. 设置描述性名称（例如 “LangChain4j Demo”）
4. 设置过期时间（建议7天）
5. 在“账户权限”下，找到“Models”，设置为“只读”
6. 点击“生成令牌”
7. 复制并保存令牌——你将无法再次看到它

### 2. 设置你的令牌

**选项 1：使用 VS Code（推荐）**

如果你使用 VS Code，将令牌添加到项目根目录的 `.env` 文件中：

如果 `.env` 文件不存在，请复制 `.env.example` 为 `.env`，或在根目录创建新 `.env` 文件。

**示例 `.env` 文件：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

然后你可以在资源管理器中右键点击任意示例文件（如 `BasicChatDemo.java`）并选择 **“Run Java”**，或者使用运行与调试面板的启动配置。

**选项 2：使用终端**

将令牌设置为环境变量：

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## 运行示例

**使用 VS Code：** 只需在资源管理器中右键点击任意示例文件，并选择 **“Run Java”**，或者使用运行与调试面板的启动配置（确保已先添加令牌到 `.env` 文件）。

**使用 Maven：** 你也可以从命令行运行：

### 1. 基础聊天

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. 提示模式

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

展示零样本、少样本、思维链和角色扮演提示。

### 3. 函数调用

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI 会在需要时自动调用你的 Java 方法。

### 4. 文档问答（简单 RAG）

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

使用 Easy RAG 自动嵌入和检索，根据你的文档提问。

### 5. 负责任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

观察 AI 安全过滤器如何阻止有害内容。

## 每个示例展示内容

**基础聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

从这里开始，了解 LangChain4j 的最简单用法。你将创建一个 `OpenAiOfficialChatModel`，用 `.chat()` 发送提示，并获得回复。这演示了基础：如何用自定义端点和 API 密钥初始化模型。理解这个模式后，所有其它内容都基于它。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) 并提问：
> - “如何在这段代码中将 GitHub 模型切换到 Azure OpenAI？”
> - “OpenAiOfficialChatModel.builder() 还能配置哪些参数？”
> - “如何添加流式响应，而不是等待完整回复？”

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

既然你知道如何与模型交互，让我们看看你说什么给它听。此示例使用相同模型设置，但展示五种不同提示模式。尝试零样本提示直接指令，少样本提示从示例学习，思维链提示展现推理步骤，角色提示设定上下文。你会看到相同模型依据请求方式产生截然不同的结果。

示例还展示了提示模板，这是一种强大的方法，用变量创建可重用提示。
以下示例展示使用 LangChain4j `PromptTemplate` 填充变量。AI 会根据指定目的地和活动作答。

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

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) 并提问：
> - “零样本与少样本提示有什么区别，何时使用？”
> - “温度参数如何影响模型的回答？”
> - “生产环境中有哪些防止提示注入攻击的技巧？”
> - “如何为常见模式创建可重用的 PromptTemplate 对象？”

**工具集成** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

这是 LangChain4j 展示强大功能的地方。你将使用 `AiServices` 创建一个 AI 助手，它可以调用你的 Java 方法。只需用 `@Tool("说明")` 注解方法，LangChain4j 会处理其余部分——AI 会根据用户请求自动决定何时使用哪个工具。这展示了函数调用，这是构建能执行操作的 AI 的关键技术，而不仅仅是回答问题。

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) 并提问：
> - “@Tool 注解如何工作，LangChain4j 在幕后做了什么？”
> - “AI 能否顺序调用多个工具以解决复杂问题？”
> - “如果工具抛出异常，应该如何处理错误？”
> - “如何集成真实 API，而不是这个计算器示例？”

**文档问答（简单 RAG）** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

这里你将看到使用 LangChain4j “简单 RAG” 的检索增强生成。文档被加载，自动拆分并嵌入到内存存储中，然后内容检索器在查询时为 AI 提供相关片段。AI 根据你的文档作答，而非其通用知识。

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) 并提问：
> - “与只用模型训练数据相比，RAG 如何防止 AI 幻觉？”
> - “这种简单方法与自定义 RAG 流程有何不同？”
> - “如何扩展以支持多文档或更大知识库？”

**负责任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

构建层层防御的 AI 安全。此示例展示两层保护协同工作：

**第一部分：LangChain4j 输入保护措施** — 在请求到达大语言模型前阻止危险提示。创建自定义保护措施，检测被禁关键词或模式。这些在你的代码中运行，快速且免费。

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**第二部分：提供商安全过滤** — GitHub 模型内置过滤，拦截保护措施可能遗漏的内容。你将看到严重违规的硬阻断（HTTP 400 错误）和 AI 礼貌拒绝的软拒绝。

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) 并提问：
> - “什么是 InputGuardrail，我如何创建自己的？”
> - “硬阻断和软拒绝有什么区别？”
> - “为什么要同时使用保护措施和提供商过滤？”

## 下一步

**下一模块：** [01-介绍 - LangChain4j 入门](../01-introduction/README.md)

---

**导航：** [← 返回主页](../README.md) | [下一页：模块 01 - 介绍 →](../01-introduction/README.md)

---

## 故障排除

### 第一次 Maven 构建

**问题：** 初次运行 `mvn clean compile` 或 `mvn package` 很慢（10-15 分钟）

**原因：** Maven 第一次构建时需要下载所有项目依赖（Spring Boot、LangChain4j 库、Azure SDK 等）。

**解决方案：** 这是正常现象。后续构建将更快，因为依赖已本地缓存。下载速度取决于你的网络状况。

### PowerShell 中的 Maven 命令语法

**问题：** Maven 命令失败，报错 `Unknown lifecycle phase ".mainClass=..."`
**原因**：PowerShell 将 `=` 解释为变量赋值操作符，破坏了 Maven 属性语法

**解决方案**：在 Maven 命令前使用停止解析操作符 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 操作符告诉 PowerShell 以字面形式将所有剩余参数传递给 Maven，而不进行解释。

### Windows PowerShell 表情符号显示

**问题**：在 PowerShell 中，AI 响应显示乱码（例如 `????` 或 `â??`）而不是表情符号

**原因**：PowerShell 默认编码不支持 UTF-8 表情符号

**解决方案**：在执行 Java 应用程序前运行此命令：
```cmd
chcp 65001
```

这会强制终端使用 UTF-8 编码。或者使用支持更好 Unicode 的 Windows Terminal。

### 调试 API 调用

**问题**：AI 模型出现身份验证错误、速率限制或意外响应

**解决方案**：示例中包含 `.logRequests(true)` 和 `.logResponses(true)`，用于在控制台显示 API 调用。这有助于排查身份验证错误、速率限制或意外响应。在生产环境中移除这些标志以减少日志噪音。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由AI翻译服务【Co-op Translator】（https://github.com/Azure/co-op-translator）翻译而成。虽然我们力求准确，但请注意，自动翻译可能存在错误或不准确之处。原始语言的文档应被视为权威来源。对于重要信息，建议使用专业人工翻译。我们不对因使用本翻译而产生的任何误解或误释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->