# 模块 01：LangChain4j 入门

## 目录

- [视频演示](../../../01-introduction)
- [您将学习到的内容](../../../01-introduction)
- [先决条件](../../../01-introduction)
- [理解核心问题](../../../01-introduction)
- [了解 Tokens](../../../01-introduction)
- [内存的工作原理](../../../01-introduction)
- [LangChain4j 的使用方式](../../../01-introduction)
- [部署 Azure OpenAI 基础设施](../../../01-introduction)
- [本地运行应用](../../../01-introduction)
- [使用应用](../../../01-introduction)
  - [无状态聊天（左侧面板）](../../../01-introduction)
  - [有状态聊天（右侧面板）](../../../01-introduction)
- [后续步骤](../../../01-introduction)

## 视频演示

观看本直播课程，讲解如何开始使用本模块：

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 您将学习到的内容

如果您完成了快速入门，已经看到如何发送提示并获得响应。这是基础，但实际应用需要更多功能。本模块将教您如何构建能够记忆上下文并维护状态的对话式 AI —— 这就是一次性演示和生产级应用之间的区别。

本指南我们全程使用 Azure OpenAI 的 GPT-5.2，因为它的高级推理能力使各种模式的行为更加明显。当添加记忆功能时，效果尤为显著。这让您更容易理解每个组件为您的应用带来了什么。

您将构建一个演示两种模式的应用：

**无状态聊天** - 每次请求相互独立。模型不记得之前的消息。这也是快速入门中使用的模式。

**有状态对话** - 每次请求都包含对话历史。模型在多个回合中维持上下文。这是生产应用所需的。

## 先决条件

- 拥有具备 Azure OpenAI 访问权限的 Azure 订阅
- Java 21，Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的开发容器中已预装 Java、Maven、Azure CLI 和 Azure Developer CLI (azd)。

> **注意：** 本模块使用 Azure OpenAI 上的 GPT-5.2。部署通过 `azd up` 自动配置 —— 请勿修改代码中的模型名称。

## 理解核心问题

语言模型是无状态的。每次 API 调用都是独立的。如果您发送“我的名字是 John”，然后问“我叫什么名字？”，模型并不知道您刚才自我介绍了。它把每个请求都当作是您第一次对话。

这对简单问答问题还可以，但对真正的应用毫无用处。客服机器人需要记住你告诉它的内容。个人助理也需要上下文。任何多轮对话都需要记忆。

<img src="../../../translated_images/zh-CN/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*无状态（独立调用）和有状态（上下文感知）对话的区别*

## 了解 Tokens

在深入对话之前，理解 tokens 很重要 —— 它们是语言模型处理文本的基本单位：

<img src="../../../translated_images/zh-CN/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*文本如何拆分为 tokens 的示例 —— “I love AI!” 被分成 4 个独立处理单元*

Tokens 是 AI 模型衡量和处理文本的单位。单词、标点，甚至空格都可能是 tokens。您的模型有一次能处理的最大 tokens 限制（GPT-5.2 为 400,000 个 tokens，最多 272,000 输入 tokens 和 128,000 输出 tokens）。理解 tokens 有助于管理对话长度和成本。

## 内存的工作原理

聊天记忆通过维护对话历史解决了无状态问题。在将请求发送给模型之前，框架会在前面添加相关的先前消息。当您问“我叫什么名字？”时，系统实际上发送了整段对话历史，让模型看到您此前说了“我的名字是 John”。

LangChain4j 提供了自动处理这一流程的内存实现。您只需选择保留多少消息，框架会管理上下文窗口。

<img src="../../../translated_images/zh-CN/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 维护最近消息的滑动窗口，自动丢弃旧消息*

## LangChain4j 的使用方式

本模块在快速入门基础上扩展，集成了 Spring Boot 并添加了对话记忆。各部分如何协同：

**依赖项** — 添加两个 LangChain4j 库：

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

**聊天模型** — 配置 Azure OpenAI 作为 Spring bean（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

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

构建器从 `azd up` 设置的环境变量读取凭据。将 `baseUrl` 设置为您的 Azure 终结点，使 OpenAI 客户端可与 Azure OpenAI 配合使用。

**对话记忆** — 使用 MessageWindowChatMemory 跟踪聊天历史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

使用 `withMaxMessages(10)` 创建内存，保留最近 10 条消息。通过类型包装器 `UserMessage.from(text)` 和 `AiMessage.from(text)` 添加用户和 AI 消息。通过 `memory.messages()` 获取历史，并发送给模型。服务根据会话 ID 存储单独的内存实例，允许多个用户同时聊天。

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 打开 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) 并问：
> - “MessageWindowChatMemory 如何决定窗口满时丢弃哪些消息？”
> - “我可以使用数据库实现自定义内存存储，而不是内存内存储吗？”
> - “如何添加摘要功能来压缩旧对话历史？”

无状态聊天端点完全跳过内存 —— 仅调用 `chatModel.chat(prompt)`，和快速入门一样。有状态端点则将消息添加到内存，检索历史，并每次请求都包含上下文。模型配置相同，模式不同。

## 部署 Azure OpenAI 基础设施

**Bash:**
```bash
cd 01-introduction
azd up  # 选择订阅和位置（推荐 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 选择订阅和位置（推荐 eastus2）
```

> **注意：** 若遇到超时错误（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），只需再运行一次 `azd up`。Azure 资源可能仍在后台部署，重试可等待资源进入终态，完成部署。

这将：
1. 部署 Azure OpenAI 资源，包含 GPT-5.2 和 text-embedding-3-small 模型
2. 自动在项目根目录生成包含凭据的 `.env` 文件
3. 设置所有必需的环境变量

**如部署遇到问题？** 请参阅 [基础设施自述文件](infra/README.md)，获取详细故障排除信息，包括子域名冲突、手动 Azure 门户部署步骤和模型配置指南。

**验证部署成功：**

**Bash:**
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 应该显示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 命令会自动生成 `.env` 文件。如需更新，可手动编辑 `.env` 文件，或通过运行以下命令重新生成：
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## 本地运行应用

**验证部署：**

确保根目录存在包含 Azure 凭据的 `.env` 文件：

**Bash:**
```bash
cat ../.env  # 应该显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用：**

**选项1：使用 Spring Boot Dashboard（推荐 VS Code 用户）**

开发容器内已包含 Spring Boot Dashboard 扩展，为所有 Spring Boot 应用提供可视化管理界面。您可以在 VS Code 左侧活动栏找到它（Spring Boot 图标）。

通过 Spring Boot Dashboard，您可以：
- 查看工作区内所有可用的 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监控应用状态

点击“introduction”旁的播放按钮启动本模块，或一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**选项2：使用 Shell 脚本**

启动所有 Web 应用（模块 01-04）：

**Bash:**
```bash
cd ..  # 从根目录开始
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 从根目录
.\start-all.ps1
```

或者仅启动本模块：

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

这两个脚本会自动从根目录 `.env` 文件加载环境变量，且在缺少构建的 JAR 文件时会自动构建。

> **注意：** 若您希望先手动构建所有模块再启动：
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

打开浏览器访问 http://localhost:8080 。

**停止运行：**

**Bash:**
```bash
./stop.sh  # 仅限此模块
# 或
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell:**
```powershell
.\stop.ps1  # 此模块仅
# 或
cd ..; .\stop-all.ps1  # 所有模块
```

## 使用应用

该应用提供了一个网页界面，左右并列展示两个聊天实现。

<img src="../../../translated_images/zh-CN/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*仪表盘显示“简单聊天（无状态）”和“对话聊天（有状态）”选项*

### 无状态聊天（左侧面板）

先尝试这里。问“我的名字是 John”，然后紧接着问“我叫什么名字？”模型不会记住，因为每条消息都是独立的。这正展示了基础语言模型集成的核心问题 —— 没有对话上下文。

<img src="../../../translated_images/zh-CN/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不会记住您上条消息中的名字*

### 有状态聊天（右侧面板）

再在这里试一次相同的顺序。先说“我的名字是 John”，然后问“我叫什么名字？”这次它记住了。区别在于 MessageWindowChatMemory —— 它维护对话历史，并将其包含在每次请求中，这正是生产对话式 AI 的工作方式。

<img src="../../../translated_images/zh-CN/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 记住了对话早先您的名字*

两边都用的是相同的 GPT-5.2 模型。唯一不同是是否有记忆。这清晰表明了记忆为应用带来的价值，也说明了它对实际应用的重要性。

## 后续步骤

**下一模块：** [02-prompt-engineering - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**导航：** [← 上一模块：00-快速入门](../00-quick-start/README.md) | [返回首页](../README.md) | [下一模块：02-提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文档使用 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始文档的母语版本应被视为权威来源。对于重要信息，建议使用专业人工翻译。我们不对因使用本翻译而产生的任何误解或错误解释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->