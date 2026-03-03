# Module 01: 开始使用 LangChain4j

## 目录

- [视频演练](../../../01-introduction)
- [你将学到什么](../../../01-introduction)
- [先决条件](../../../01-introduction)
- [理解核心问题](../../../01-introduction)
- [理解 Tokens](../../../01-introduction)
- [内存如何工作](../../../01-introduction)
- [本模块如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基础设施](../../../01-introduction)
- [本地运行应用](../../../01-introduction)
- [使用应用](../../../01-introduction)
  - [无状态聊天（左侧面板）](../../../01-introduction)
  - [有状态聊天（右侧面板）](../../../01-introduction)
- [下一步](../../../01-introduction)

## 视频演练

观看本直播课程，讲解如何开始使用本模块：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 你将学到什么

在快速入门中，你使用了 GitHub 模型发送提示、调用工具、构建 RAG 流水线并测试安全保护。这些演示展示了可能性——现在我们转向 Azure OpenAI 和 GPT-5.2，开始构建生产级应用。本模块专注于具有记忆上下文与维护状态能力的会话 AI —— 这是那些快速入门演示背后使用但未解释的概念。

在本指南中我们将始终使用 Azure OpenAI 的 GPT-5.2，因为它的高级推理能力使不同模式的行为更加明显。添加内存后，你会清晰见到差异，这有助于理解各组件为你的应用带来的价值。

你将构建一个演示两种模式的应用：

**无状态聊天** - 每次请求独立。模型不记得之前的消息。这是你在快速入门中使用的模式。

**有状态对话** - 每次请求包含会话历史。模型维护多轮会话上下文。这是生产应用所需。

## 先决条件

- 拥有访问 Azure OpenAI 的 Azure 订阅
- Java 21，Maven 3.9 及以上版本
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的开发容器中已预装 Java、Maven、Azure CLI 和 Azure Developer CLI (azd)。

> **注意：** 本模块使用 Azure OpenAI 上的 GPT-5.2。部署通过 `azd up` 自动配置 - 不要修改代码中的模型名称。

## 理解核心问题

语言模型是无状态的。每个 API 调用都是独立的。如果你发送“我叫 John”，然后问“我叫什么名字？”，模型不会知道你刚刚介绍过自己。它把每个请求都当作是与你的“第一次对话”。

这对于简单问答可以，但对真正的应用没用。客服机器人需要记住你告诉他们的信息。个人助理需要上下文。任何多轮对话都需要记忆。

下图对比了两种方式——左侧是无状态调用，忘记了你的名字；右侧是由 ChatMemory 支持的有状态调用，记住了名字。

<img src="../../../translated_images/zh-CN/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*无状态（独立调用）和有状态（上下文感知）会话的区别*

## 理解 Tokens

在深入会话之前，理解 token 很重要——它是语言模型处理的文本基本单位：

<img src="../../../translated_images/zh-CN/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*文本如何拆分为 tokens 的示例 —— “I love AI!” 被拆成4个独立的处理单元*

token 是 AI 模型测量和处理文本的方式。单词、标点符号甚至空格都可以是 token。你的模型对一次能处理的 token 数有限制（GPT-5.2 最大400,000个，包括最多272,000输入token和128,000输出token）。理解 token 有助于管理对话长度和成本。

## 内存如何工作

聊天内存通过维护对话历史解决了无状态问题。在将请求发送给模型之前，框架会预先添加相关的前面消息。当你问“我叫什么名字？”时，系统实际上发送了整个对话历史，使模型看到你之前说过“我叫 John”。

LangChain4j 提供了自动处理这部分的内存实现。你可以选择保留多少条消息，框架自动管理上下文窗口。下图展示了 MessageWindowChatMemory 如何维护最近消息的滑动窗口。

<img src="../../../translated_images/zh-CN/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 维护最近消息的滑动窗口，自动丢弃旧消息*

## 本模块如何使用 LangChain4j

本模块在快速入门基础上集成了 Spring Boot 并添加了对话内存。组件关系如下：

**依赖项** - 添加两个 LangChain4j 库：

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

**聊天模型** - 配置 Azure OpenAI 作为 Spring bean（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

构建器从 `azd up` 设置的环境变量读取凭据。将 `baseUrl` 设置为你的 Azure 端点，使 OpenAI 客户端可适配 Azure OpenAI。

**会话内存** - 使用 MessageWindowChatMemory 跟踪聊天历史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

使用 `withMaxMessages(10)` 创建内存，保留最近10条消息。使用类型包装器 `UserMessage.from(text)` 和 `AiMessage.from(text)` 添加用户和 AI 消息。用 `memory.messages()` 获取历史并发送给模型。服务为每个会话 ID 存储独立的内存实例，支持多个用户同时聊天。

> **🤖 试试 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) 并询问：
> - “MessageWindowChatMemory 满窗口时如何决定丢弃哪些消息？”
> - “我能用数据库实现自定义内存存储而非内存中存储吗？”
> - “如何添加摘要功能压缩旧对话历史？”

无状态聊天接口完全跳过内存 —— 直接调用 `chatModel.chat(prompt)`，如快速入门。状态接口则向内存添加消息，获取历史，并将上下文包含在请求中。模型配置相同，模式不同。

## 部署 Azure OpenAI 基础设施

**Bash：**
```bash
cd 01-introduction
azd up  # 选择订阅和位置（推荐 eastus2）
```

**PowerShell：**
```powershell
cd 01-introduction
azd up  # 选择订阅和位置（推荐eastus2）
```

> **注意：** 如果遇到超时错误 (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`)，只需重新运行 `azd up`。Azure 资源可能仍在后台部署，重试能让部署在资源达到终态后完成。

此操作将：
1. 部署带 GPT-5.2 和 text-embedding-3-small 模型的 Azure OpenAI 资源
2. 自动在项目根目录生成 `.env` 文件，其中包含凭据
3. 设置所有必需的环境变量

**部署遇到问题？** 请查看 [基础设施 README](infra/README.md) 获得详细故障排查，包括子域名冲突、手动 Azure 门户部署步骤及模型配置指导。

**验证部署是否成功：**

**Bash：**
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell：**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 命令会自动生成 `.env` 文件。如果稍后需要更新，你可以编辑 `.env` 文件，或者通过运行以下命令重新生成：
>
> **Bash：**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell：**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## 本地运行应用

**验证部署：**

确认在根目录有 `.env` 文件并包含 Azure 凭据。在本模块目录（`01-introduction/`）运行：

**Bash：**
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell：**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用：**

**选项1：使用 Spring Boot Dashboard（推荐 VS Code 用户）**

开发容器中包含了 Spring Boot Dashboard 扩展，提供可视化界面管理所有 Spring Boot 应用。可在 VS Code 左侧活动栏找到（Spring Boot 图标）。

通过 Spring Boot Dashboard，你可以：
- 查看工作区中的所有 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监视应用状态

点击 “introduction” 旁的播放按钮启动本模块，或一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot Dashboard —— 一处启动、停止和监视所有模块*

**选项2：使用 shell 脚本**

启动所有 Web 应用（模块 01-04）：

**Bash：**
```bash
cd ..  # 从根目录
./start-all.sh
```

**PowerShell：**
```powershell
cd ..  # 来自根目录
.\start-all.ps1
```

或者只启动本模块：

**Bash：**
```bash
cd 01-introduction
./start.sh
```

**PowerShell：**
```powershell
cd 01-introduction
.\start.ps1
```

两个脚本都会自动从根目录 `.env` 文件加载环境变量，且如果 JAR 不存在会自动构建。

> **注意：** 如果希望先手动构建所有模块然后再启动：
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

在浏览器打开 http://localhost:8080 。

**停止运行：**

**Bash：**
```bash
./stop.sh  # 仅此模块
# 或
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell：**
```powershell
.\stop.ps1  # 仅此模块
# 或
cd ..; .\stop-all.ps1  # 所有模块
```

## 使用应用

该应用提供了两个并列的聊天实现的网页界面。

<img src="../../../translated_images/zh-CN/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*仪表盘展示了简单聊天（无状态）与对话聊天（有状态）选项*

### 无状态聊天（左侧面板）

先试试这个。问“我叫 John”，然后马上问“我叫什么名字？”模型不会记住，因为每条消息都是独立的。这展示了基础语言模型集成的核心问题 —— 无对话上下文。

<img src="../../../translated_images/zh-CN/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不记得你之前说的名字*

### 有状态聊天（右侧面板）

现在在这里重复相同步骤。问“我叫 John”，再问“我叫什么名字？”这次它记住了。区别是 MessageWindowChatMemory —— 它维护对话历史，并在每次请求时包含上下文信息。这就是生产级会话 AI 的实现方式。

<img src="../../../translated_images/zh-CN/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 记得对话早前告诉它的名字*

两个面板使用的是同一个 GPT-5.2 模型。唯一的不同是内存。这能清楚展示内存给你的应用带来的价值，以及为什么它对真实用例至关重要。

## 下一步

**下一模块：** [02-prompt-engineering - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**导航：** [← 上一节：模块 00 - 快速入门](../00-quick-start/README.md) | [返回首页](../README.md) | [下一节：模块 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件由AI翻译服务[Co-op Translator](https://github.com/Azure/co-op-translator)翻译而成。尽管我们努力保证准确性，但请注意，自动翻译可能包含错误或不准确之处。原始文件的原文应被视为权威来源。对于重要信息，建议使用专业人工翻译。因使用本翻译而产生的任何误解或误释，我们概不负责。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->