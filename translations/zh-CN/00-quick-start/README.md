# Module 00: 快速开始

## 目录

- [介绍](../../../00-quick-start)
- [什么是 LangChain4j？](../../../00-quick-start)
- [LangChain4j 依赖](../../../00-quick-start)
- [先决条件](../../../00-quick-start)
- [设置](../../../00-quick-start)
  - [1. 获取你的 GitHub 令牌](../../../00-quick-start)
  - [2. 设置你的令牌](../../../00-quick-start)
- [运行示例](../../../00-quick-start)
  - [1. 基本聊天](../../../00-quick-start)
  - [2. 提示模式](../../../00-quick-start)
  - [3. 函数调用](../../../00-quick-start)
  - [4. 文档问答（Easy RAG）](../../../00-quick-start)
  - [5. 负责任的 AI](../../../00-quick-start)
- [每个示例展示什么](../../../00-quick-start)
- [下一步](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 介绍

本快速入门旨在让你尽快开始使用 LangChain4j。它涵盖了使用 LangChain4j 和 GitHub Models 构建 AI 应用程序的基本内容。在接下来的模块中，你将使用 Azure OpenAI 和 LangChain4j 构建更高级的应用。

## 什么是 LangChain4j？

LangChain4j 是一个简化构建 AI 驱动应用的 Java 库。你不用处理复杂的 HTTP 客户端和 JSON 解析，而是使用简洁的 Java API。

LangChain 中的“链”指的是将多个组件串联起来 —— 你可能会将一个提示链到一个模型，再链到解析器，或多个 AI 调用串联，前一个输出作为下一个输入。本快速入门先介绍基础概念，再探讨更复杂的链。

<img src="../../../translated_images/zh-CN/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j 链接概念" width="800"/>

*LangChain4j 中的组件链 —— 通过组合构建强大的 AI 工作流*

我们会使用三个核心组件：

**ChatModel** —— 用于 AI 模型交互的接口。调用 `model.chat("prompt")` 即得到响应字符串。我们使用 `OpenAiOfficialChatModel`，它兼容 OpenAI API 规范，支持 GitHub Models 等端点。

**AiServices** —— 创建类型安全的 AI 服务接口。定义方法并用 `@Tool` 注解，LangChain4j 会负责调用。AI 需要时自动调用你的 Java 方法。

**MessageWindowChatMemory** —— 维护对话历史。没有它，每次请求是独立的；有了它，AI 能记住之前消息，实现多轮上下文。

<img src="../../../translated_images/zh-CN/architecture.eedc993a1c576839.webp" alt="LangChain4j 架构" width="800"/>

*LangChain4j 架构 —— 核心组件协同工作，为你的 AI 应用赋能*

## LangChain4j 依赖

本快速入门使用了 [`pom.xml`](../../../00-quick-start/pom.xml) 中的三个 Maven 依赖：

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

`langchain4j-open-ai-official` 模块提供 `OpenAiOfficialChatModel` 类，连接 OpenAI 兼容的 API。GitHub Models 使用相同 API 格式，所以无需特殊适配，只需把基础 URL 指向 `https://models.github.ai/inference`。

`langchain4j-easy-rag` 模块自动完成文档拆分、嵌入和检索，让你无须手动配置即可构建 RAG 应用。

## 先决条件

**使用开发容器？** Java 和 Maven 已预装。只需准备一个 GitHub 个人访问令牌。

**本地开发：**
- Java 21+，Maven 3.9+
- GitHub 个人访问令牌（请参阅下方说明）

> **注意：** 本模块使用 GitHub Models 的 `gpt-4.1-nano`，请勿修改代码中的模型名称 —— 它已配置为兼容 GitHub 可用模型。

## 设置

### 1. 获取你的 GitHub 令牌

1. 访问 [GitHub 设置 → 个人访问令牌](https://github.com/settings/personal-access-tokens)
2. 点击“生成新令牌”
3. 设置描述名（例如 “LangChain4j Demo”）
4. 设置过期时间（建议 7 天）
5. 在“账户权限”中找到“Models”，设置为“只读”
6. 点击“生成令牌”
7. 复制并保存你的令牌 —— 之后无法再查看

### 2. 设置你的令牌

**方案一：使用 VS Code（推荐）**

如果你使用 VS Code，将令牌添加到项目根目录下的 `.env` 文件：

如果 `.env` 文件不存在，可复制 `.env.example` 为 `.env`，或新建 `.env` 文件。

**示例 `.env` 文件：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env
GITHUB_TOKEN=your_token_here
```

之后你可以在资源管理器中右键任意示例文件（例如 `BasicChatDemo.java`），选择 **“Run Java”**，或用运行和调试面板中的启动配置。

**方案二：使用终端**

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

**使用 VS Code：** 直接在资源管理器中右键单击任意示例文件，选择 **“Run Java”**，或使用运行和调试面板中的启动配置（确保先在 `.env` 文件添加了令牌）。

**使用 Maven：** 你也可以通过命令行运行：

### 1. 基本聊天

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

展示零样本、少样本、思维链和角色扮演提示模式。

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

### 4. 文档问答（Easy RAG）

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

使用 Easy RAG 进行文档问答，自动完成嵌入和检索。

### 5. 负责任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

展示 AI 安全过滤器如何阻止有害内容。

## 每个示例展示什么

**基本聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

从这里开始，了解 LangChain4j 最简单的使用方式。你将创建一个 `OpenAiOfficialChatModel`，调用 `.chat()` 发送提示，并获取响应。此示例演示基础内容：如何用自定义端点和 API 密钥初始化模型。掌握此模式后，其他用法均基于此。

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 利用 [GitHub Copilot](https://github.com/features/copilot) Chat 尝试：** 打开 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)，询问：
> - “如何将代码中的 GitHub Models 切换为 Azure OpenAI？”
> - “OpenAiOfficialChatModel.builder() 还能配置哪些参数？”
> - “如何实现流式响应，而不是等待完整响应？”

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

既然你懂得如何和模型交互，接下来探索应该对模型说什么。本示例用相同模型配置，展示五种不同的提示模式。尝试零样本提示用于直接指令，少样本提示学习示例，思维链提示展示推理步骤，角色扮演提示设定上下文。你将看到同一模型因提示形式不同，输出效果大相径庭。

示例还演示了提示模板，这是一种强大且可复用的带变量提示构造方式。
下例展示用 LangChain4j 的 `PromptTemplate` 填充变量，AI 会基于给定的目的地和活动进行回答。

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

> **🤖 利用 [GitHub Copilot](https://github.com/features/copilot) Chat 尝试：** 打开 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)，询问：
> - “零样本和少样本提示有何不同？何时应分别使用？”
> - “温度参数如何影响模型响应？”
> - “有什么技巧可防止生产环境提示注入攻击？”
> - “如何创建用于常用模式的可复用 PromptTemplate 对象？”

**工具集成** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

这里演示 LangChain4j 的强大功能。你将使用 `AiServices` 创建一个 AI 助手，可调用你的 Java 方法。只需用 `@Tool("描述")` 注解方法，LangChain4j 会管理调用 —— AI 会根据用户请求自动决定何时使用工具。这展示了函数调用，是构建能执行操作而非仅回答问题的 AI 的关键技术。

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

> **🤖 利用 [GitHub Copilot](https://github.com/features/copilot) Chat 尝试：** 打开 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，询问：
> - “@Tool 注解如何工作，LangChain4j 在幕后做了什么？”
> - “AI 可以顺序调用多个工具解决复杂问题吗？”
> - “如果工具抛出异常，应如何处理错误？”
> - “如何将真实 API 接入替代此计算器示例？”

**文档问答（Easy RAG）** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

这里展示了使用 LangChain4j “Easy RAG” 进行的 RAG（检索增强生成）。文档自动加载、拆分、嵌入到内存存储，查询时内容检索器提供相关块给 AI。AI 根据你的文档回答，而非凭借一般知识。

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

> **🤖 利用 [GitHub Copilot](https://github.com/features/copilot) Chat 尝试：** 打开 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)，询问：
> - “RAG 怎样避免 AI 幻觉，相较于直接使用模型训练数据？”
> - “这个简单方案和自定义 RAG 管道有何区别？”
> - “如何扩展以处理多个文档或更大知识库？”

**负责任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

构建多层防护的 AI 安全。本示例展示两层保护协同工作：

**第一部分：LangChain4j 输入护栏** —— 在到达 LLM 之前阻止危险提示，创建自定义护栏以检查禁用关键字或模式。代码中运行，快速且无费用。

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

**第二部分：提供商安全过滤器** —— GitHub Models 内置过滤，会捕捉护栏可能遗漏的违规。严重违规会返 400 错误硬阻止，轻微违规则礼貌拒绝。

> **🤖 利用 [GitHub Copilot](https://github.com/features/copilot) Chat 尝试：** 打开 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)，询问：
> - “什么是 InputGuardrail？如何创建自定义？”
> - “硬阻止和软拒绝有何区别？”
> - “为何同时使用护栏和提供商过滤器？”

## 下一步

**下一个模块：** [01-introduction - 在 Azure 上使用 LangChain4j 和 gpt-5 入门](../01-introduction/README.md)

---

**导航：** [← 返回主目录](../README.md) | [下一个：模块 01 - 介绍 →](../01-introduction/README.md)

---

## 故障排除

### 第一次 Maven 构建

**问题：** 初次执行 `mvn clean compile` 或 `mvn package` 很慢（10-15 分钟）

**原因：** Maven 首次构建需下载所有项目依赖（Spring Boot，LangChain4j 库，Azure SDK 等）。

**解决方案：** 这是正常现象。后续构建会快很多，因为依赖已缓存。下载时间取决于网络速度。

### PowerShell Maven 命令语法

**问题：** Maven 命令失败，报错 `Unknown lifecycle phase ".mainClass=..."`
**原因**：PowerShell 将 `=` 解释为变量赋值操作符，导致 Maven 属性语法出错

**解决方案**：在 Maven 命令前使用停止解析操作符 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 操作符告诉 PowerShell 将剩余所有参数原样传递给 Maven，而不做解释。

### Windows PowerShell 表情符号显示

**问题**：AI 响应在 PowerShell 中显示乱码（如 `????` 或 `â??`）而非表情符号

**原因**：PowerShell 默认编码不支持 UTF-8 表情符号

**解决方案**：在执行 Java 应用程序之前运行此命令：
```cmd
chcp 65001
```

这会强制终端使用 UTF-8 编码。或者，可以使用对 Unicode 支持更好的 Windows Terminal。

### 调试 API 调用

**问题**：AI 模型发生身份验证错误、速率限制或返回意外响应

**解决方案**：示例中包含 `.logRequests(true)` 和 `.logResponses(true)`，用于在控制台显示 API 调用。这有助于排查身份验证错误、速率限制或意外响应。生产环境中请移除这些标记以减少日志噪音。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由人工智能翻译服务[Co-op Translator](https://github.com/Azure/co-op-translator)翻译而成。虽然我们力求准确，但请注意自动翻译可能存在错误或不准确之处。原始语言的原版文件应被视为权威来源。对于重要信息，建议采用专业人工翻译。因使用本翻译而产生的任何误解或曲解，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->