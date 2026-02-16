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
  - [4. 文档问答 (RAG)](../../../00-quick-start)
  - [5. 负责任的 AI](../../../00-quick-start)
- [每个示例展示内容](../../../00-quick-start)
- [下一步](../../../00-quick-start)
- [故障排除](../../../00-quick-start)

## 介绍

此快速入门旨在帮助你尽快启动并运行 LangChain4j。它涵盖了使用 LangChain4j 和 GitHub 模型构建 AI 应用的绝对基础。在接下来的模块中，你将使用 Azure OpenAI 和 LangChain4j 构建更高级的应用。

## 什么是 LangChain4j？

LangChain4j 是一个简化构建 AI 驱动应用的 Java 库。你无需处理 HTTP 客户端和 JSON 解析，而是使用清晰的 Java API。

LangChain 中的“链”指的是将多个组件串联起来——你可能会把提示链到模型再链到解析器，或者将多个 AI 调用串联，其中一个输出作为下一个输入。本快速入门专注于基本原理，然后再探索更复杂的链。

<img src="../../../translated_images/zh-CN/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j 中的链式组件 - 通过组合构建强大的 AI 工作流*

我们将使用三个核心组件：

**ChatLanguageModel** - AI 模型交互接口。调用 `model.chat("prompt")` 并获取响应字符串。我们使用 `OpenAiOfficialChatModel`，它适用于兼容 OpenAI 的端点，比如 GitHub 模型。

**AiServices** - 创建类型安全的 AI 服务接口。定义方法，使用 `@Tool` 注解，LangChain4j 负责调用调度。AI 会在需要时自动调用你的 Java 方法。

**MessageWindowChatMemory** - 维持对话历史。没有它，每次请求都是独立的。有了它，AI 记住先前消息，跨多轮保持上下文。

<img src="../../../translated_images/zh-CN/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j 架构 - 核心组件协同工作，为你的 AI 应用提供支持*

## LangChain4j 依赖

本快速入门在[`pom.xml`](../../../00-quick-start/pom.xml)中使用了两个 Maven 依赖：

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
```

`langchain4j-open-ai-official` 模块提供了连接 OpenAI 兼容 API 的 `OpenAiOfficialChatModel` 类。GitHub 模型使用相同的 API 格式，无需特殊适配器——只需将基础 URL 指向 `https://models.github.ai/inference`。

## 前提条件

**使用 Dev Container？** Java 和 Maven 已预装。你只需一个 GitHub 个人访问令牌。

**本地开发：**
- Java 21+，Maven 3.9+
- GitHub 个人访问令牌（以下提供说明）

> **注意：** 本模块使用 GitHub 模型中的 `gpt-4.1-nano`。不要修改代码中的模型名称——它已配置为与 GitHub 可用模型兼容。

## 设置

### 1. 获取你的 GitHub 令牌

1. 访问 [GitHub 设置 → 个人访问令牌](https://github.com/settings/personal-access-tokens)
2. 点击“生成新令牌”
3. 设置描述性名称（例如“LangChain4j 演示”）
4. 设置过期时间（建议 7 天）
5. 在“账户权限”中找到“Models”，设置为“只读”
6. 点击“生成令牌”
7. 复制并保存令牌——以后无法再次查看

### 2. 设置你的令牌

**选项 1：使用 VS Code（推荐）**

如果你使用 VS Code，将令牌添加到项目根目录的 `.env` 文件：

如果 `.env` 文件不存在，请复制 `.env.example` 到 `.env`，或在项目根目录新建 `.env` 文件。

**示例 `.env` 文件：**
```bash
# 在 /workspaces/LangChain4j-for-Beginners/.env 中
GITHUB_TOKEN=your_token_here
```

然后你只需在资源管理器中右键点击任意演示文件（如 `BasicChatDemo.java`），选择 **“Run Java”**，或通过运行和调试面板使用启动配置。

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

**使用 VS Code：** 在资源管理器中右键点击任意演示文件，选择 **“Run Java”**，或使用运行和调试面板中的启动配置（确保先将令牌添加到 `.env` 文件）。

**使用 Maven：** 你也可以通过命令行运行：

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

展示零样本、少样本、思维链和基于角色的提示。

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

### 4. 文档问答 (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

针对 `document.txt` 中的内容提问。

### 5. 负责任的 AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

了解 AI 安全过滤器如何阻止有害内容。

## 每个示例展示内容

**基础聊天** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

从这里开始，了解最简单的 LangChain4j。你将创建一个 `OpenAiOfficialChatModel`，用 `.chat()` 发送提示，并获得回复。演示基础内容：如何用自定义端点和 API 密钥初始化模型。掌握此模式后，其他内容都基于此构建。

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)，问：
> - “如何在这段代码中从 GitHub 模型切换到 Azure OpenAI？”
> - “OpenAiOfficialChatModel.builder() 还能配置哪些参数？”
> - “如何添加流式响应，而不是等待完整响应？”

**提示工程** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

既然你知道如何与模型对话，让我们探索你对它说什么。此演示使用相同模型配置，展示了五种不同的提示模式。尝试零样本提示以直接指令，少样本提示以学习示例，思维链提示以揭示推理步骤，以及基于角色的提示以设定上下文。你将看到同一模型根据提示构造方式，表现出截然不同的结果。

该演示还展示了提示模板，这是创建可重用提示并包含变量的强大方式。
以下示例使用 LangChain4j 的 `PromptTemplate` 填充变量。AI 将根据提供的目的地和活动进行回答。

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

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)，问：
> - “零样本和少样本提示有什么区别，何时该用哪种？”
> - “temperature 参数如何影响模型回答？”
> - “在生产环境中，有哪些技巧防止提示注入攻击？”
> - “如何为常见模式创建可重用的 PromptTemplate 对象？”

**工具集成** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

这是 LangChain4j 功能强大的地方。你将使用 `AiServices` 创建一个可以调用你 Java 方法的 AI 助手。只需用 `@Tool("description")` 注解方法，LangChain4j 会处理剩下的——AI 会根据用户提问自动决定使用哪个工具。这演示了函数调用，这是构建能执行动作而不仅仅答问的 AI 的关键技术。

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，问：
> - “@Tool 注解是怎么工作的，LangChain4j 后台如何处理？”
> - “AI 能否串联调用多个工具解决复杂问题？”
> - “如果工具抛出异常怎么办？应该如何处理错误？”
> - “如何集成真实 API，而不是这个计算器示例？”

**文档问答 (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

这里你将看到 RAG（检索增强生成）的基础。你不是单靠模型训练数据，而是加载 [`document.txt`](../../../00-quick-start/document.txt) 的内容并包括在提示中。AI 根据你的文档而非通用知识回答。这是构建能与自己数据协作系统的第一步。

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **注意：** 这种简单方法将整个文档加载进提示。对于大文件（>10KB），你会超出上下文限制。模块 03 涵盖了分块和向量搜索，用于生产环境的 RAG 系统。

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)，问：
> - “相比模型训练数据，RAG 如何防止 AI 幻觉？”
> - “这种简单方法和用向量嵌入做检索有什么区别？”
> - “如何扩展以处理多个文档或更大知识库？”
> - “如何构造提示确保 AI 只使用提供的上下文？”

**负责任的 AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

通过多层防御构建 AI 安全。此演示展示了两层保护协同工作：

**第一部分：LangChain4j 输入护栏** - 在提示达到大语言模型前阻止危险提示。创建自定义护栏，检测禁止关键字或模式。这在你的代码里运行，快速且免费。

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

**第二部分：供应商安全过滤器** - GitHub 模型内置过滤器捕获护栏可能遗漏的内容。你会看到严重违规的硬阻断（HTTP 400 错误）和温和拒绝，AI 会委婉拒绝。

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)，问：
> - “什么是 InputGuardrail，我如何创建自己的？”
> - “硬阻断和软拒绝有什么区别？”
> - “为什么要同时使用护栏和供应商过滤器？”

## 下一步

**下一个模块：** [01-introduction - LangChain4j 和 Azure 上的 gpt-5 入门](../01-introduction/README.md)

---

**导航：** [← 返回主页面](../README.md) | [下一个：模块 01 - 介绍 →](../01-introduction/README.md)

---

## 故障排除

### 第一次 Maven 构建

**问题：** 初次执行 `mvn clean compile` 或 `mvn package` 需要较长时间（10-15 分钟）

**原因：** Maven 需要在首次构建时下载所有项目依赖（Spring Boot、LangChain4j 库、Azure SDK 等）。

**解决方法：** 这是正常现象。后续构建会快得多，因为依赖已缓存在本地。下载时间取决于你的网络速度。
### PowerShell Maven 命令语法

**问题**：Maven 命令失败，报错 `Unknown lifecycle phase ".mainClass=..."`

**原因**：PowerShell 将 `=` 解释为变量赋值运算符，导致 Maven 属性语法被破坏

**解决方案**：在 Maven 命令前使用停止解析运算符 `--%`：

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` 运算符告诉 PowerShell 将剩余的所有参数字面传递给 Maven，不做任何解释。

### Windows PowerShell 表情符号显示

**问题**：AI 回答在 PowerShell 中显示乱码（例如 `????` 或 `â??`）而不是表情符号

**原因**：PowerShell 默认编码不支持 UTF-8 表情符号

**解决方案**：在执行 Java 应用之前运行此命令：
```cmd
chcp 65001
```

这会强制终端使用 UTF-8 编码。或者，使用 Windows Terminal，其对 Unicode 支持更好。

### 调试 API 调用

**问题**：认证错误、速率限制或 AI 模型返回意外响应

**解决方案**：示例中包含 `.logRequests(true)` 和 `.logResponses(true)`，以在控制台显示 API 调用。这有助于排查认证错误、速率限制或意外响应。生产环境中请移除这些标志以减少日志噪声。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由人工智能翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译。虽然我们力求准确，但请注意，自动翻译可能存在错误或不准确之处。请以原始语言版本的文件作为权威来源。对于重要信息，建议采用专业人工翻译。我们对因使用本翻译而产生的任何误解或误译不承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->