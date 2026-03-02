# 模块 03：RAG（检索增强生成）

## 目录

- [视频讲解](../../../03-rag)
- [你将学到什么](../../../03-rag)
- [先决条件](../../../03-rag)
- [了解 RAG](../../../03-rag)
  - [本教程使用哪种 RAG 方法？](../../../03-rag)
- [工作原理](../../../03-rag)
  - [文档处理](../../../03-rag)
  - [创建嵌入](../../../03-rag)
  - [语义搜索](../../../03-rag)
  - [答案生成](../../../03-rag)
- [运行应用](../../../03-rag)
- [使用应用](../../../03-rag)
  - [上传文档](../../../03-rag)
  - [提问](../../../03-rag)
  - [查看来源引用](../../../03-rag)
  - [试验问题](../../../03-rag)
- [关键概念](../../../03-rag)
  - [分块策略](../../../03-rag)
  - [相似度评分](../../../03-rag)
  - [内存存储](../../../03-rag)
  - [上下文窗口管理](../../../03-rag)
- [何时使用 RAG](../../../03-rag)
- [下一步](../../../03-rag)

## 视频讲解

观看这个现场课程，了解如何开始本模块：

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## 你将学到什么

在前面的模块中，你学习了如何与 AI 对话以及如何有效地构建提示。但存在一个根本性限制：语言模型只知道训练时学到的内容。它们不能回答关于你公司政策、项目文档或任何它们未被训练过的信息的问题。

RAG（检索增强生成）解决了这个问题。它不是试图教模型你的信息（这既昂贵又不切实际），而是赋予它搜索你文档的能力。当有人提问时，系统会找到相关信息并将其包含进提示。模型随后基于检索到的上下文生成答案。

可以把 RAG 想象成给模型配备了一座参考图书馆。当你提问时，系统会：

1. **用户查询** - 你提出问题
2. **嵌入** - 将你的问题转换为向量
3. **向量搜索** - 找到相似的文档分块
4. **上下文组装** - 将相关分块添加进提示
5. **回答** - 大语言模型基于上下文生成答案

这让模型的回答基于你真实的数据，而不是仅依赖其训练知识或编造答案。

## 先决条件

- 完成 [模块 00 - 快速入门](../00-quick-start/README.md)（用于上述的 Easy RAG 示例）
- 完成 [模块 01 - 介绍](../01-introduction/README.md)（Azure OpenAI 资源已部署，包括 `text-embedding-3-small` 嵌入模型）
- 在根目录有包含 Azure 凭据的 `.env` 文件（由模块 01 中的 `azd up` 创建）

> **注意：** 如果你还没有完成模块 01，请先按照那里介绍的部署说明操作。`azd up` 命令会同时部署本模块使用的 GPT 聊天模型和嵌入模型。

## 了解 RAG

下面的图示阐释了核心概念：RAG 并不依赖模型的训练数据，而是为它配备一个你的文档参考库，每次生成答案前先查询该库。

<img src="../../../translated_images/zh-CN/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*此图显示了标准大语言模型（基于训练数据猜测）与 RAG 增强大语言模型（先查询你的文档）的区别。*

这是完整的流程。用户提问经过四个阶段——嵌入、向量搜索、上下文组装和答案生成——每个环节依次构建：

<img src="../../../translated_images/zh-CN/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*此图显示端到端 RAG 流程——用户查询经过嵌入、向量搜索、上下文组装，再生成答案。*

本模块其余内容将逐步讲解每个阶段，并附带可运行和修改的代码。

### 本教程使用哪种 RAG 方法？

LangChain4j 提供三种实现 RAG 的方式，抽象层级不同。下图并列比较：

<img src="../../../translated_images/zh-CN/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*此图比较了 LangChain4j 的三种 RAG 方法——Easy, Native 和 Advanced——展示其关键组件和使用场景。*

| 方法 | 功能 | 权衡 |
|---|---|---|
| **Easy RAG** | 通过 `AiServices` 和 `ContentRetriever` 自动连接所有环节。你只需注解接口，附加检索器，LangChain4j 会自动处理嵌入、搜索和提示组装。 | 代码量最少，但看不到每个步骤内部细节。 |
| **Native RAG** | 你自行调用嵌入模型，搜索存储，构建提示，生成回答——每一步都明确写出。 | 代码量较多，但每个环节都清晰可见且可修改。 |
| **Advanced RAG** | 使用 `RetrievalAugmentor` 框架，支持插件式查询变换器、路由器、重排器和内容注入器，适合生产级管道。 | 灵活度最高，但复杂度大幅增加。 |

**本教程使用 Native 方式。** RAG 流水线中的每一步——嵌入查询、搜索向量存储、组装上下文和生成回答——都在 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 中明确写出。这是有意为之：作为学习资源，看懂每个环节比代码简洁更重要。熟悉流程后，你可以转向 Easy RAG 做快速原型，或 Advanced RAG 做生产系统。

> **💡 已经见识过 Easy RAG 了吗？** [快速入门模块](../00-quick-start/README.md)包含了一个文档问答示例（[`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)），采用 Easy RAG 方法——LangChain4j 自动处理嵌入、搜索和提示组装。本模块进一步拆解该流水线，让你能亲自调用和控制每个阶段。

<img src="../../../translated_images/zh-CN/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*本图展示了 `SimpleReaderDemo.java` 中的 Easy RAG 流水线。对比本模块使用的 Native 方法：Easy RAG 把嵌入、检索和提示组装隐藏在 `AiServices` 和 `ContentRetriever` 后面，你只需上传文档，附加检索器，便可获得回答。Native 方法把流程拆开，由你分别调用（嵌入、搜索、组装上下文、生成回答），获得完全的可见性和控制权。*

## 工作原理

本模块的 RAG 流水线分为四个阶段，每次用户提问时顺序执行。首先，上传的文档被**解析且分块**成可管理的小片段。然后这些分块被转换成**向量嵌入**并存储，以便进行数学对比。收到查询后，系统执行**语义搜索**找出最相关的分块，最后将它们作为上下文传给大语言模型，进行**答案生成**。以下章节逐步解释各阶段的代码与图示。先来看第一个步骤。

### 文档处理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

当你上传文档时，系统会解析（PDF 或纯文本），附加元数据如文件名，然后将文档拆成分块——适合模型上下文窗口的小段。这些分块会略有重叠，防止边界上下文丢失。

```java
// 解析上传的文件并将其封装到 LangChain4j 文档中
Document document = Document.from(content, metadata);

// 分割成300个令牌的块，重叠30个令牌
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

下图直观展示了这个过程。注意每个分块与相邻分块共享部分令牌——30 个令牌的重叠确保没有关键上下文被遗漏：

<img src="../../../translated_images/zh-CN/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*本图示展示文档被切成长度为 300 令牌、重叠 30 令牌的片段，保持分块边界的上下文连贯性。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天尝试：** 打开 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)，并询问：
> - “LangChain4j 如何将文档拆分成分块？为什么重叠很重要？”
> - “不同文档类型的最优分块大小是多少？为什么？”
> - “如何处理多语言或含有特殊格式的文档？”

### 创建嵌入

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每个分块被转换成一种数值表示，称为嵌入——本质上是将含义转为数字的转换器。嵌入模型不像聊天模型那样“智能”；它不会听指令、推理或回答问题。它能做的是将文本映射到一个数学空间，相似含义的词语彼此靠近——比如“car”（车）与“automobile”（汽车）相邻，“refund policy”（退款政策）与“return my money”（退还我的钱）相近。把聊天模型看成人，你可以跟它聊天；嵌入模型则是极好的文件分类系统。

<img src="../../../translated_images/zh-CN/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*此图示范了嵌入模型如何将文本转为数值向量，类似含义的词组——如“car”和“automobile”——向量空间中距离较近。*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```

下图类图展示了 RAG 流水线中的两个独立流程及 LangChain4j 中相应的类。**摄取流程**（上传时运行一次）负责拆分文档、嵌入分块并通过 `.addAll()` 存储。**查询流程**（用户每次提问时运行）负责嵌入问题、通过 `.search()` 搜索并将匹配的上下文传给聊天模型。两个流程共享 `EmbeddingStore<TextSegment>` 接口：

<img src="../../../translated_images/zh-CN/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*此图显示 RAG 流水线中的两个流程——摄取和查询——以及它们如何通过共享的 EmbeddingStore 接口连接。*

嵌入存储后，相似内容自然在向量空间聚类。下图展示相关主题文档在三维向量空间如何聚集成群，这就是语义搜索得以实现的原因：

<img src="../../../translated_images/zh-CN/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*此可视化展示了技术文档、商业规则和常见问题等主题如何在三维向量空间中各自形成聚类。*

用户搜索时，系统遵循四步：文档先被嵌入一次，查询每次被嵌入，系统使用余弦相似度对比查询向量和所有存储向量，然后返回 top-K 高分分块。下图演示了每一步及对应的 LangChain4j 类：

<img src="../../../translated_images/zh-CN/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*此图示四步嵌入搜索过程：嵌入文档、嵌入查询、用余弦相似度比较向量、返回 top-K 结果。*

### 语义搜索

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

当你提问时，问题同样被转换成一个嵌入向量。系统将你的问题嵌入与所有文档分块嵌入进行比较。它找到含义最相似的分块——不仅仅是匹配关键字，而是基于语义的相似度。

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```

下图对比了语义搜索与传统关键字搜索。关键字搜索“vehicle”（车辆）会遗漏“cars and trucks”（汽车和卡车）这段内容，而语义搜索能理解它们表达的相同含义，把它当做高分匹配返回：

<img src="../../../translated_images/zh-CN/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*此图比较基于关键字的搜索与语义搜索，展示语义搜索如何检索概念相关内容，即使精确关键字不同。*

在底层，相似度使用余弦相似度衡量——本质上是判断“两条箭头是否指向同一方向”。两个分块即使用词完全不同，但若含义相近，它们的向量方向相似，得分接近 1.0：

<img src="../../../translated_images/zh-CN/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*此图展示了余弦相似度作为嵌入向量之间的角度——向量越对齐，得分越接近1.0，表示语义相似度越高。*

> **🤖 试试用[GitHub Copilot](https://github.com/features/copilot) Chat：** 打开[`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)并提问：
> - “相似度搜索是如何通过嵌入工作的，是什么决定得分的？”
> - “我应该使用什么相似度阈值，它如何影响结果？”
> - “如果找不到相关文档，我该如何处理？”

### 答案生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最相关的文本块被组装成一个结构化的提示，包含明确的指令、检索到的上下文和用户的问题。模型根据这些特定的文本块读取并回答——它只能使用眼前的信息，从而防止幻觉生成。

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```
  
下图展示了这种组装的实际操作——搜索步骤中的最高得分文本块被注入到提示模板中，`OpenAiOfficialChatModel`生成基于事实的回答：

<img src="../../../translated_images/zh-CN/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*此图演示了如何将最高得分的文本块组装成结构化提示，让模型从您的数据中生成基于事实的回答。*

## 运行应用程序

**验证部署：**

确保根目录存在包含 Azure 凭据的 `.env` 文件（已在模块01创建）：

**Bash:**
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```
  
**PowerShell:**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```
  
**启动应用程序：**

> **注意：** 如果你已经用模块01的 `./start-all.sh` 启动了所有应用程序，本模块已在端口8081运行。你可以跳过以下启动命令，直接访问 http://localhost:8081 。

**选项1：使用 Spring Boot Dashboard（推荐给 VS Code 用户）**

开发容器包含 Spring Boot Dashboard 扩展，提供可视化界面管理所有 Spring Boot 应用程序。你可以在 VS Code 左侧活动栏找到（寻找 Spring Boot 图标）。

通过 Spring Boot Dashboard，你可以：
- 看到工作区中所有可用的 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监控应用状态

只需点击“rag”旁的播放按钮启动此模块，或者一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*此截图展示了 VS Code 中的 Spring Boot Dashboard，您可以在此可视化地启动、停止及监控应用。*

**选项2：使用 shell 脚本**

启动所有 Web 应用（模块01-04）：

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
cd 03-rag
./start.sh
```
  
**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```
  
这两个脚本会自动从根目录 `.env` 文件加载环境变量，并在缺少 JAR 文件时自动构建。

> **注意：** 如果你想先手动构建所有模块再启动：
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
在浏览器中打开 http://localhost:8081 。

**停止应用：**

**Bash:**
```bash
./stop.sh  # 仅此模块
# 或者
cd .. && ./stop-all.sh  # 所有模块
```
  
**PowerShell:**
```powershell
.\stop.ps1  # 仅此模块
# 或
cd ..; .\stop-all.ps1  # 所有模块
```
  
## 使用应用程序

该应用提供网页界面用于文档上传与提问。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-CN/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此屏幕截图展示了 RAG 应用界面，您可以上传文档并提出问题。*

### 上传文档

首先上传一个文档——TXT 文件最适合测试。此目录下提供了一个 `sample-document.txt`，包含关于 LangChain4j 功能、RAG 实现及最佳实践的信息——适合用来测试系统。

系统会处理你的文档，将其拆分成文本块，并为每个文本块创建嵌入。上传时这些操作自动完成。

### 提问

现在问一些关于文档内容的具体问题。试试一些文档中清楚陈述的事实。系统会搜索相关文本块，将它们包含进提示，并生成答案。

### 检查来源引用

每个答案都附带了包含相似度分数的来源引用。这些分数（0到1）显示每个文本块与您问题的相关度。分数越高，匹配越好。这样您就能对照源材料核实答案。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-CN/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截图展示了查询结果，包括生成的答案、来源引用及每个检索文本块的相关度分数。*

### 试验不同问题

试着问不同类型的问题：
- 具体事实：“主要主题是什么？”
- 比较：“X 和 Y 有什么区别？”
- 总结：“总结关于 Z 的关键点”

观察相关度分数如何随问题与文档内容匹配的程度变化。

## 关键概念

### 拆块策略

文档被拆分为300个标记长度的块，重叠30个标记。此配置平衡了文本块上下文的丰富度和保证文本块大小足够小，以便在提示中包含多个块。

### 相似度分数

每个检索的文本块都会附带一个0到1之间的相似度分数，表明它与用户问题的匹配程度。下图展示了分数区间及系统如何利用它们过滤结果：

<img src="../../../translated_images/zh-CN/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*此图展示了0到1的分数区间，以及以0.5为最低阈值过滤掉无关文本块的机制。*

分数范围为0到1：
- 0.7-1.0：高度相关，精准匹配
- 0.5-0.7：相关，有良好上下文
- 低于0.5：被过滤掉，相关度太低

系统只检索高于最小阈值的文本块，以保证质量。

嵌入在语义聚类清晰时表现良好，但也有盲区。下图展示了常见失败模式——文本块过大导致向量模糊，文本块过小缺乏上下文，模糊术语指向多个聚类，精确匹配查找（如ID，零件号）无法通过嵌入实现：

<img src="../../../translated_images/zh-CN/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*此图展示了常见嵌入失败模式：文本块过大、文本块过小、模糊术语指向多个聚类以及像 ID 这类精确匹配查找。*

### 内存存储

本模块为简便起见使用内存存储。重启应用时，上传的文档会丢失。生产系统使用持久化向量数据库，如 Qdrant 或 Azure AI Search。

### 上下文窗口管理

每个模型有最大上下文窗口限制。不能包含大型文档中的所有文本块。系统检索最相关的N个文本块（默认5个），以保持在限制内，同时提供充足的上下文以获得准确回答。

## RAG 何时有意义

RAG 并非总是最佳方案。下图决策指南帮助你判断何时使用 RAG 有价值，何时简单方法（如将内容直接包含在提示或依赖模型内置知识）就足够了：

<img src="../../../translated_images/zh-CN/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*此图展示了判断何时使用 RAG 和何时采用更简单方法的决策指南。*

**适用 RAG 场景：**
- 回答专有文档相关问题
- 信息频繁变动（政策、价格、规格）
- 需要准确的来源归属
- 内容太大，无法装入单个提示
- 需要可验证、基于事实的响应

**不适用 RAG 场景：**
- 问题涉及模型已有的一般知识
- 需要实时数据（RAG 基于上传文档）
- 内容足够小，可以直接放入提示

## 后续步骤

**下一个模块：** [04-tools - 带工具的 AI 代理](../04-tools/README.md)

---

**导航：** [← 上一页：模块 02 - 提示工程](../02-prompt-engineering/README.md) | [回到首页](../README.md) | [下一页：模块 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：  
本文件使用 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译完成。虽然我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版本的文档应被视为权威来源。对于关键信息，建议使用专业人工翻译。我们不对因使用本翻译而产生的任何误解或误读承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->