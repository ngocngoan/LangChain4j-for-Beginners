# 模块 01：LangChain4j 入门

## 目录

- [视频演示](../../../01-introduction)
- [你将学到什么](../../../01-introduction)
- [先决条件](../../../01-introduction)
- [理解核心问题](../../../01-introduction)
- [理解令牌（Tokens）](../../../01-introduction)
- [内存如何工作](../../../01-introduction)
- [如何使用 LangChain4j](../../../01-introduction)
- [部署 Azure OpenAI 基础设施](../../../01-introduction)
- [本地运行应用程序](../../../01-introduction)
- [使用应用程序](../../../01-introduction)
  - [无状态聊天（左侧面板）](../../../01-introduction)
  - [有状态聊天（右侧面板）](../../../01-introduction)
- [后续步骤](../../../01-introduction)

## 视频演示

观看此直播课程，讲解如何开始使用本模块：[LangChain4j 入门 - 直播课程](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## 你将学到什么

如果你完成了快速入门，就已经了解了如何发送提示并获取响应。这是基础，但真实应用需要更多功能。本模块将教你如何构建会记忆上下文并维护状态的对话式 AI——这是一次性演示和生产就绪应用之间的区别。

本指南中我们将使用 Azure OpenAI 的 GPT-5.2，因为它先进的推理能力让不同模式的行为更加明显。添加内存后，你会清晰看到差异。这使得理解各组件为你的应用带来的价值更容易。

你将构建一个演示两种模式的应用程序：

**无状态聊天** - 每次请求相互独立。模型不记得之前的消息。这是快速入门中使用的模式。

**有状态会话** - 每次请求包含对话历史。模型在多轮对话中保持上下文。这是生产应用所需的模式。

## 先决条件

- 具备 Azure 订阅并已开通 Azure OpenAI 访问权限
- Java 21，Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **注意：** 提供的开发容器中已预装 Java、Maven、Azure CLI 和 Azure Developer CLI（azd）。

> **注意：** 本模块使用 Azure OpenAI 上的 GPT-5.2。部署通过 `azd up` 自动配置——请勿修改代码中的模型名称。

## 理解核心问题

语言模型是无状态的。每个 API 调用都是独立的。如果你先发消息说“我叫约翰”，然后问“我叫什么名字？”，模型并不知道你刚才自我介绍过。它把每次请求都当作是你第一次对话。

这对简单问答来说没问题，但对真实应用没用。客服机器人要记住你告诉它的信息。个人助理需要上下文。任何多轮对话都需要记忆。

<img src="../../../translated_images/zh-CN/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*无状态（独立调用）和有状态（上下文感知）对话的区别*

## 理解令牌（Tokens）

在深入对话之前，理解令牌很重要——它是语言模型处理文本的基本单位：

<img src="../../../translated_images/zh-CN/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*文本如何被拆分成令牌的示例——“I love AI!”被拆分成4个独立处理单元*

令牌是 AI 模型衡量和处理文本的方式。词语、标点，甚至空格都可能是令牌。模型一次能处理的令牌数量有限（GPT-5.2 为400,000个令牌，输入最多272,000个令牌，输出最多128,000个令牌）。理解令牌有助于你管理会话长度和成本。

## 内存如何工作

聊天内存通过维护对话历史来解决无状态问题。在将请求发送给模型之前，框架会预先添加相关的先前消息。当你问“我叫什么名字？”，系统实际上发送了整段对话历史，这样模型才能看到你之前说过“我叫约翰”。

LangChain4j 提供了内存实现，自动处理这些。你选择保留多少消息，框架管理上下文窗口。

<img src="../../../translated_images/zh-CN/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory 维护最近消息的滑动窗口，自动丢弃旧消息*

## 如何使用 LangChain4j

本模块基于快速入门扩展，集成了 Spring Boot 并添加了对话内存。各部分如何组合如下：

**依赖** - 添加两个 LangChain4j 库：

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

**聊天模型** - 配置 Azure OpenAI 作为 Spring Bean（[LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)）：

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

构建器从由 `azd up` 设置的环境变量读取凭证。将 `baseUrl` 设置为你的 Azure 端点，使 OpenAI 客户端能用于 Azure OpenAI。

**对话内存** - 使用 MessageWindowChatMemory 跟踪聊天历史（[ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)）：

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

用 `withMaxMessages(10)` 创建内存，保持最近10条消息。用带类型的包装器添加用户和 AI 消息：`UserMessage.from(text)` 和 `AiMessage.from(text)`。通过 `memory.messages()` 获取历史并发送给模型。该服务为每个对话 ID 存储独立的内存实例，支持多用户同时聊天。

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天试试：** 打开 [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)，提问：
> - “MessageWindowChatMemory 在窗口满时如何决定丢弃哪些消息？”
> - “我能否用数据库实现自定义内存存储，而不是内存中的？”
> - “我如何添加摘要功能以压缩旧对话历史？”

无状态聊天端点完全跳过内存——就像快速入门那样调用 `chatModel.chat(prompt)`。有状态端点则向内存添加消息，检索历史，并将上下文包含在每次请求中。模型配置相同，模式不同。

## 部署 Azure OpenAI 基础设施

**Bash:**
```bash
cd 01-introduction
azd up  # 选择订阅和位置（推荐使用 eastus2）
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 选择订阅和位置（推荐 eastus2）
```

> **注意：** 若出现超时错误（`RequestConflict: Cannot modify resource ... provisioning state is not terminal`），只需再次运行 `azd up`。Azure 资源可能还在后台部署，重试可以让部署在资源到达终态时完成。

此操作将：
1. 部署带 GPT-5.2 和 text-embedding-3-small 模型的 Azure OpenAI 资源
2. 自动生成项目根目录下的 `.env` 文件，包含凭证
3. 配置所有必需的环境变量

**部署遇到问题？** 请参阅[基础设施说明文档](infra/README.md)，获得详细故障排除指南，包括子域名冲突、手动 Azure 门户部署步骤和模型配置建议。

**验证部署是否成功：**

**Bash:**
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 应该显示 AZURE_OPENAI_ENDPOINT、API_KEY 等。
```

> **注意：** `azd up` 命令会自动生成 `.env` 文件。如果你需要稍后更新它，可以手动编辑 `.env` 文件，或重新运行以下命令生成：
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

## 本地运行应用程序

**验证部署：**

确保根目录存在含有 Azure 凭证的 `.env` 文件：

**Bash:**
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**启动应用程序：**

**选项 1：使用 Spring Boot Dashboard（推荐 VS Code 用户）**

开发容器内置 Spring Boot Dashboard 扩展，提供可视界面管理所有 Spring Boot 应用程序。你可以在 VS Code 左侧活动栏找到该扩展（查找 Spring Boot 图标）。

在 Spring Boot Dashboard 中，你可以：
- 查看工作区内所有可用的 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监控应用状态

只需点击“introduction”旁的播放按钮启动本模块，或者一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

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

或者只启动本模块：

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

这两个脚本会自动从根目录 `.env` 文件加载环境变量，并在 JAR 不存在时构建它们。

> **注意：** 如果你想先手动构建所有模块再启动：
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

在浏览器中打开 http://localhost:8080 。

**停止应用：**

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

## 使用应用程序

应用程序提供了一个网页界面，左右并排展示两个聊天实现。

<img src="../../../translated_images/zh-CN/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*仪表盘展示简单聊天（无状态）和对话聊天（有状态）两个选项*

### 无状态聊天（左侧面板）

先尝试这个。先问“我叫约翰”，然后立即问“我叫什么名字？”模型不会记住，因为每条消息都是独立的。这演示了基础语言模型集成的核心问题——没有对话上下文。

<img src="../../../translated_images/zh-CN/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI 不记得你之前说的名字*

### 有状态聊天（右侧面板）

现在在这里重复同样的流程。问“我叫约翰”，再问“我叫什么名字？”这次模型记住了。区别在于 MessageWindowChatMemory——它维护对话历史，并将其包含在每次请求中。这就是生产级对话式 AI 的工作方式。

<img src="../../../translated_images/zh-CN/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI 记得你之前说的名字*

两边面板都使用相同的 GPT-5.2 模型，唯一区别是内存。这清晰展示了内存为你的应用带来的价值，以及为何它对真实用例必不可少。

## 后续步骤

**下一个模块：** [02-提示工程 - 使用 GPT-5.2 的提示工程](../02-prompt-engineering/README.md)

---

**导航：** [← 上一节：模块 00 - 快速入门](../00-quick-start/README.md) | [回到首页](../README.md) | [下一节：模块 02 - 提示工程 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件使用 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。虽然我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版本的文件应被视为权威来源。对于重要信息，建议使用专业人工翻译。因使用本翻译而引起的任何误解或错误解释，我们不承担任何责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->