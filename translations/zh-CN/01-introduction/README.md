# 模块 01：LangChain4j 入门

## 目录

- [你将学到什么](../../../01-introduction)
- [先决条件](../../../01-introduction)
- [理解核心问题](../../../01-introduction)
- [理解 Token](../../../01-introduction)
- [内存工作原理](../../../01-introduction)
- [如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基础设施](../../../01-introduction)
- [本地运行应用程序](../../../01-introduction)
- [使用应用程序](../../../01-introduction)
  - [无状态聊天（左侧面板）](../../../01-introduction)
  - [有状态聊天（右侧面板）](../../../01-introduction)
- [下一步](../../../01-introduction)

## 你将学到什么

如果你完成了快速入门，你已经了解了如何发送提示并获取响应。这是基础，但真实应用需要更多。本模块教你如何构建记住上下文并维护状态的对话式 AI——这就是一次性演示和生产就绪应用之间的区别。

我们在本指南中将始终使用 Azure OpenAI 的 GPT-5.2，因为其高级推理能力能更清晰地展示不同模式的行为。当你添加内存时，你会清楚看到差异。这使你更容易理解每个组件为你的应用带来的价值。

你将构建一个应用，演示两种模式：

**无状态聊天** - 每个请求都是独立的。模型不会记住之前的消息。这就是你在快速入门中使用的模式。

**有状态对话** - 每个请求都包含对话历史记录。模型在多轮对话中保持上下文。这是生产应用所必需的。

## 先决条件

- 拥有 Azure 订阅并且具备 Azure OpenAI 访问权限
- Java 21，Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** Java、Maven、Azure CLI 和 Azure Developer CLI (azd) 已预装在所提供的开发容器中。

> **注意：** 本模块使用 Azure OpenAI 上的 GPT-5.2。部署通过 `azd up` 自动配置 —— 不要修改代码中的模型名称。

## 理解核心问题

语言模型是无状态的。每次 API 调用都是独立的。如果你发送“我叫 John”，然后问“我叫什么名字？”，模型根本不知道你刚做了自我介绍。它把每个请求都当成你这辈子第一次对话。

这对于简单的问答来说没问题，但对真实应用没用。客服机器人需要记得你告诉过它什么。个人助理需要上下文。任何多轮对话都需要记忆。

<img src="../../../translated_images/zh-CN/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="无状态与有状态对话" width="800"/>

*无状态（独立调用）与有状态（感知上下文）对话的区别*

## 理解 Token

在深入对话之前，重要的是理解 Token —— 语言模型处理的文本基本单位：

<img src="../../../translated_images/zh-CN/token-explanation.c39760d8ec650181.webp" alt="Token 解释" width="800"/>

*文本如何被拆分成 Token 的示例——“I love AI!” 被拆成4个独立处理单元*

Token 是 AI 模型衡量和处理文本的方式。单词、标点，甚至空格都可以是 Token。你的模型有一次处理的最大 Token 数限制（GPT-5.2 为 400,000，最多 272,000 输入 Token 和 128,000 输出 Token）。理解 Token 有助于你管理对话长度和成本。

## 内存工作原理

聊天内存通过维护对话历史解决了无状态问题。在将请求发送到模型之前，框架会在前面添加相关的先前消息。当你问“我叫什么名字？”时，系统其实是发送了整个对话历史，这样模型才能看到你之前说过“我叫 John”。

LangChain4j 提供了自动处理这部分的内存实现。你选择要保留多少消息，框架管理上下文窗口。

<img src="../../../translated_images/zh-CN/memory-window.bbe67f597eadabb3.webp" alt="内存窗口概念" width="800"/>

*MessageWindowChatMemory 维护一个最近消息的滑动窗口，自动丢弃旧消息*

## 如何使用 LangChain4j

本模块扩展了快速入门，将 Spring Boot 集成进来并添加对话内存。组件组合如下：

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

**聊天模型** - 将 Azure OpenAI 配置为 Spring Bean（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

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

构建器从 `azd up` 设置的环境变量读取凭据。将 `baseUrl` 设置为你的 Azure 端点，使 OpenAI 客户端能与 Azure OpenAI 一起工作。

**对话内存** - 通过 MessageWindowChatMemory 跟踪聊天历史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

创建内存时使用 `withMaxMessages(10)` 保留最近 10 条消息。用类型包装器添加用户和 AI 消息：`UserMessage.from(text)` 和 `AiMessage.from(text)`。用 `memory.messages()` 获取历史，再传给模型。服务按对话 ID 存储独立内存实例，允许多个用户同时聊天。

> **🤖 试试 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)，问：
> - “MessageWindowChatMemory 在窗口满时如何决定丢弃哪些消息？”
> - “我能否用数据库而非内存实现自定义内存存储？”
> - “如何添加摘要功能压缩旧对话历史？”

无状态聊天端点完全跳过内存——就像快速入门那样用 `chatModel.chat(prompt)`。有状态端点则将消息添加到内存，检索历史，并随请求发送上下文。模型配置相同，模式不同。

## 部署 Azure OpenAI 基础设施

**Bash：**
```bash
cd 01-introduction
azd up  # 选择订阅和位置（推荐 eastus2）
```

**PowerShell：**
```powershell
cd 01-introduction
azd up  # 选择订阅和位置（推荐 eastus2）
```

> **注意：** 如果遇到超时错误（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），只需再次运行 `azd up`。Azure 资源可能仍在后台部署中，重试可以确保部署在资源达到终态后完成。

这将会：
1. 部署包含 GPT-5.2 和 text-embedding-3-small 模型的 Azure OpenAI 资源
2. 在项目根目录自动生成带凭据的 `.env` 文件
3. 设置所有必需的环境变量

**部署有问题？** 请参阅 [基础设施 README](infra/README.md) 获取详细的故障排除指导，包括子域名冲突、手动 Azure 门户部署步骤及模型配置建议。

**验证部署是否成功：**

**Bash：**
```bash
cat ../.env  # 应该显示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell：**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 命令会自动生成 `.env` 文件。如果需要后续更新，你可以手动编辑 `.env` 文件，或者通过运行以下命令重新生成：
>
> **Bash：**
> ```bash
> cd ..
> bash .azd-env.sh
> ```

> **PowerShell：**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## 本地运行应用程序

**验证部署：**

确保根目录存在带有 Azure 凭据的 `.env` 文件：

**Bash：**
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell：**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用程序：**

**选项 1：使用 Spring Boot 仪表盘（推荐 VS Code 用户）**

开发容器包含了 Spring Boot 仪表盘扩展，提供管理所有 Spring Boot 应用的可视化界面。你可以在 VS Code 左侧的活动栏中找到它（寻找 Spring Boot 图标）。

通过 Spring Boot 仪表盘，你可以：
- 查看工作区内所有可用的 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监控应用状态

只需点击“introduction”旁的播放按钮启动本模块，或一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot 仪表盘" width="400"/>

**选项 2：使用 shell 脚本**

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
cd 01-introduction
./start.sh
```

**PowerShell：**
```powershell
cd 01-introduction
.\start.ps1
```

两个脚本都会自动加载根目录的 `.env` 文件中的环境变量，并且在 JAR 不存在时构建它们。

> **注意：** 如果你想在启动前手动构建所有模块：
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

打开浏览器访问 http://localhost:8080。

**停止命令：**

**Bash：**
```bash
./stop.sh  # 仅此模块
# 或
cd .. && ./stop-all.sh  # 所有模块
```

**PowerShell：**
```powershell
.\stop.ps1  # 仅此模块
# 或者
cd ..; .\stop-all.ps1  # 所有模块
```

## 使用应用程序

应用提供了一个带有并排两种聊天实现的网页界面。

<img src="../../../translated_images/zh-CN/home-screen.121a03206ab910c0.webp" alt="应用首页" width="800"/>

*仪表盘显示简单聊天（无状态）和对话式聊天（有状态）两种选项*

### 无状态聊天（左侧面板）

先试试这个。先问“我叫 John”，然后立即问“我叫什么名字？”模型不会记住，因为每条消息都是独立的。这展示了基础语言模型集成的核心问题——无对话上下文。

<img src="../../../translated_images/zh-CN/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="无状态聊天演示" width="800"/>

*AI 不记得你上一条消息中说的名字*

### 有状态聊天（右侧面板）

现在在这里尝试相同的顺序。先问“我叫 John”，再问“我叫什么名字？”这次它记得。区别是在于 MessageWindowChatMemory——它维护对话历史，并在每次请求中包含这些内容。这就是生产对话式 AI 的工作方式。

<img src="../../../translated_images/zh-CN/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="有状态聊天演示" width="800"/>

*AI 记得你对话中早些时候说的名字*

两个面板使用相同的 GPT-5.2 模型。唯一区别是内存。这清楚展示了内存对你的应用带来的价值及其对真实用例的重要性。

## 下一步

**下一个模块：** [02-prompt-engineering - 使用 GPT-5.2 进行提示工程](../02-prompt-engineering/README.md)

---

**导航：** [← 上一页：模块 00 - 快速入门](../00-quick-start/README.md) | [返回主页](../README.md) | [下一页：模块 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件通过人工智能翻译服务[Co-op Translator](https://github.com/Azure/co-op-translator)进行翻译。虽然我们力求准确，但请注意自动翻译可能存在错误或不准确之处。原始语言的文档应被视为权威来源。对于重要信息，建议使用专业人工翻译。对于因使用本翻译导致的任何误解或误释，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->