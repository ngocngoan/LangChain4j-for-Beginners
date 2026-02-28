# 模块 03：RAG（检索增强生成）

## 目录

- [视频演示](../../../03-rag)
- [你将学到什么](../../../03-rag)
- [先决条件](../../../03-rag)
- [理解 RAG](../../../03-rag)
  - [本教程使用哪种 RAG 方法？](../../../03-rag)
- [工作原理](../../../03-rag)
  - [文档处理](../../../03-rag)
  - [创建嵌入](../../../03-rag)
  - [语义搜索](../../../03-rag)
  - [答案生成](../../../03-rag)
- [运行应用程序](../../../03-rag)
- [使用应用程序](../../../03-rag)
  - [上传文档](../../../03-rag)
  - [提问](../../../03-rag)
  - [检查来源引用](../../../03-rag)
  - [尝试不同问题](../../../03-rag)
- [关键概念](../../../03-rag)
  - [分块策略](../../../03-rag)
  - [相似度得分](../../../03-rag)
  - [内存存储](../../../03-rag)
  - [上下文窗口管理](../../../03-rag)
- [何时使用 RAG](../../../03-rag)
- [后续步骤](../../../03-rag)

## 视频演示

观看本现场讲解，了解如何开始使用本模块：[LangChain4j 的 RAG - 直播场次](https://www.youtube.com/watch?v=_olq75ZH_eY)

## 你将学到什么

在之前的模块中，你学习了如何与 AI 进行对话以及如何有效地构建提示。但有一个根本性的限制：语言模型只知道它们在训练期间学到的内容。它们无法回答有关你的公司政策、项目文档，或任何未被训练的内容的问题。

RAG（检索增强生成）解决了这个问题。它不是试图教模型你的信息（这既昂贵又不切实际），而是赋予模型搜索你文档的能力。当有人提问时，系统会查找相关信息并将其包含在提示中。模型随后基于检索到的上下文进行回答。

你可以把 RAG 想象成给模型配备了一个参考图书馆。当你提问时，系统执行：

1. **用户查询** - 你提出问题  
2. **嵌入** - 将你的问题转换为向量  
3. **向量搜索** - 找到相似的文档块  
4. **上下文组装** - 将相关文档块加入提示  
5. **响应** - LLM 根据上下文生成答案  

这使模型的回答基于你的实际数据，而不是依赖其训练知识或凭空编造答案。

## 先决条件

- 完成 [模块 00 - 快速开始](../00-quick-start/README.md)（用于上述易用 RAG 示例）  
- 完成 [模块 01 - 介绍](../01-introduction/README.md)（已部署 Azure OpenAI 资源，包括 `text-embedding-3-small` 嵌入模型）  
- 根目录下有 `.env` 文件，包含 Azure 凭证（由模块 01 中的 `azd up` 创建）  

> **注意：** 如果尚未完成模块 01，请先按照那里的部署说明操作。`azd up` 命令将部署本模块使用的 GPT 聊天模型和嵌入模型。

## 理解 RAG

下图展示了核心概念：RAG 不仅依赖模型的训练数据，还为其提供一个包含你文档的参考库，以便在生成每个答案前进行查询。

<img src="../../../translated_images/zh-CN/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*此图显示了标准 LLM（根据训练数据猜测）与 RAG 增强 LLM（先参考你的文档）的区别。*

下面展示了端到端的各个环节。用户的提问依次经过四个阶段 —— 嵌入、向量搜索、上下文组装和答案生成 —— 每个阶段建立在前一个基础上：

<img src="../../../translated_images/zh-CN/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*此图展示了端到端的 RAG 流程：用户查询经过嵌入、向量搜索、上下文组装和答案生成。*

本模块后续内容将逐步讲解每个阶段，配有可运行和可修改的代码。

### 本教程使用哪种 RAG 方法？

LangChain4j 提供三种实现 RAG 的方式，抽象层级不同。下图并排比较了它们：

<img src="../../../translated_images/zh-CN/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*此图比较了 LangChain4j 的三种 RAG 方法 —— 易用（Easy）、原生（Native）和高级（Advanced） —— 展示主要组件和使用时机。*

| 方法 | 功能 | 权衡 |
|---|---|---|
| **易用 RAG** | 通过 `AiServices` 和 `ContentRetriever` 自动连接所有步骤。你只需注解接口、附加检索器，LangChain4j 会自动处理嵌入、搜索和提示组装。 | 代码最少，但看不到每一步的细节。 |
| **原生 RAG** | 你逐步调用嵌入模型、搜索存储、构建提示和生成答案——每一步显式操作。 | 代码更多，但每个阶段都清晰可见且可修改。 |
| **高级 RAG** | 使用带有可插拔查询转换器、路由器、重排序器和内容注入器的 `RetrievalAugmentor` 框架，适合生产级管线。 | 灵活性最高，但复杂度显著增加。 |

**本教程采用原生方法。** RAG 流程的每一步——查询嵌入、向量搜索、上下文组装和答案生成——都在 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 中显式编写。这是有意为之：作为学习资料，比起代码最简，更重要的是让你看到并理解每个阶段。一旦你熟悉了各环节如何组合，可以升级到易用 RAG 进行快速原型开发，或高级 RAG 用于生产系统。

> **💡 已经看过易用 RAG？** [快速开始模块](../00-quick-start/README.md) 包含一个文档问答示例 ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java))，采用易用 RAG —— LangChain4j 自动处理嵌入、搜索和提示组装。本模块进一步揭示该流程，让你自行控制每个阶段。

<img src="../../../translated_images/zh-CN/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*此图展示了 `SimpleReaderDemo.java` 中的易用 RAG 流程。与本模块使用的原生方法相比，易用 RAG 将嵌入、检索和提示组装封装在 `AiServices` 和 `ContentRetriever` 后面 —— 你只需加载文档、附加检索器并获取答案。原生方法拆开这条流程，让你自行调用每个阶段（嵌入、搜索、上下文组装、生成），从而获得完全的可见性和控制权。*

## 工作原理

本模块中的 RAG 流程分为四个阶段，每当用户提问时顺序运行。首先，上传的文档会被**解析并拆分成块**，这些块规模适中，适合模型的上下文窗口。这些文档块被转换为**向量嵌入**并存储，以便进行数学比较。查询到来时，系统进行**语义搜索**以找出最相关的文档块，最终将这些作为上下文传递给 LLM 做**答案生成**。以下章节详细讲解每步的代码与示意图。先看第一步。

### 文档处理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

上传文档后，系统解析它（支持 PDF 或纯文本），附加元数据如文件名，然后拆分成块 —— 这些块尺寸适中，能轻松放入模型的上下文窗口。文档块之间稍有重叠，避免边界处丢失上下文。

```java
// 解析上传的文件并将其包装在 LangChain4j 文档中
Document document = Document.from(content, metadata);

// 分割成300令牌的块，每块重叠30令牌
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

下图从视觉上展示了这个过程。注意每个文档块与相邻块共享的一些标记 —— 30 标记的重叠确保上下文无缝传递：

<img src="../../../translated_images/zh-CN/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*此图展示了文档被拆分成每块 300 标记，重叠 30 标记的块，保证块边界处的上下文连续。*

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)，并询问：
> - “LangChain4j 是如何拆分文档成块的？为什么重叠很重要？”
> - “不同文档类型的最佳块大小是多少？为什么？”
> - “如何处理多语言或带特殊格式的文档？”

### 创建嵌入

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每个文档块被转换为数值表示，称为嵌入 —— 本质上是将语义转为数字。嵌入模型不像聊天模型那样“智能”；它不会执行指令、推理或回答问题。它做的是将文本映射到数学空间中，使语义相近的文本彼此靠近 —— 比如 “car” 接近 “automobile”，“refund policy” 接近 “return my money”。可以把聊天模型比作可交谈的人，而嵌入模型就像超强的文件归档系统。

<img src="../../../translated_images/zh-CN/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*此图展示了嵌入模型如何将文本转成数值向量，令语义相近的词汇（如“car”和“automobile”）在向量空间中彼此接近。*

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

下图 UML 类图显示了 RAG 流程中两条独立的数据流和 LangChain4j 实现它们的类。**摄取流程**（上传时运行一次）拆分文档、嵌入文档块，并通过 `.addAll()` 保存。**查询流程**（每次用户提问时运行）嵌入问题、通过 `.search()` 搜索存储，并将匹配的上下文传给聊天模型。两个流程通过共享的 `EmbeddingStore<TextSegment>` 接口相连：

<img src="../../../translated_images/zh-CN/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*此图展示了 RAG 流程中的两个数据流 —— 摄取和查询 —— 以及它们如何通过共享的 EmbeddingStore 接口连接。*

嵌入存入存储后，语义相似的内容自然会在向量空间中聚集。下图展示了以相关话题为中心的文档如何形成集群，这就是语义搜索得以实现的原因：

<img src="../../../translated_images/zh-CN/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*此图展示了相关文档在三维向量空间中聚类，诸如技术文档、业务规则和常见问题形成了各自的组。*

当用户进行搜索时，系统遵循四步流程：第一次将文档嵌入，搜索时嵌入查询，对查询向量和所有存储向量用余弦相似度比较，返回得分最高的前 K 个文档块。下图展示每步及对应的 LangChain4j 类：

<img src="../../../translated_images/zh-CN/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*此图展示嵌入搜索的四步过程：嵌入文档，嵌入查询，用余弦相似度比较向量，返回前 K 结果。*

### 语义搜索

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

你提问时，问题也被转换成嵌入。系统将你的问题嵌入与所有文档块的嵌入进行比较，找到语义最相近的文档块 —— 不只是关键词匹配，而是真正的语义相似。

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

下图对比了语义搜索与传统关键词搜索。关键词搜索“vehicle”会错过关于“cars and trucks”的文档块，而语义搜索理解它们含义相同，且把该文档块作为高分匹配返回：

<img src="../../../translated_images/zh-CN/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*此图比较了基于关键词的搜索和语义搜索，显示语义搜索能检索概念相关内容，即使关键词不同。*

底层采用余弦相似度衡量相似度 —— 本质上是问“这两根箭头是否指向同一方向？”即使两个文档块用词完全不同，但如果含义相同，其向量方向一致，得分接近 1.0：

<img src="../../../translated_images/zh-CN/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*此图展示余弦相似度是两个嵌入向量间的夹角，更接近 1.0 表示向量更一致，即语义相似度更高。*
> **🤖 试试使用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：** 打开 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 并询问：
> - “相似度搜索如何利用嵌入工作？评分由什么决定？”
> - “我应该使用什么相似度阈值？它如何影响结果？”
> - “如果没有找到相关文档，我该如何处理？”

### 答案生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最相关的文本块将被组装成一个结构化提示，其中包含明确的指令、检索到的上下文和用户的问题。模型读取这些特定的文本块并基于这些信息给出回答 — 它只能使用眼前的信息，这避免了幻觉的产生。

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

下面的图表展示了这一组装过程 —— 在搜索步骤中得分最高的文本块被注入到提示模板中，`OpenAiOfficialChatModel` 据此生成一个有依据的答案：

<img src="../../../translated_images/zh-CN/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*该图展示了如何将得分最高的文本块组装进一个结构化提示，使模型能够基于你的数据生成有依据的回答。*

## 启动应用程序

**验证部署：**

确认根目录下存在带有 Azure 凭据的 `.env` 文件（在模块 01 中创建）：

**Bash:**
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 应显示 AZURE_OPENAI_ENDPOINT，API_KEY，DEPLOYMENT
```

**启动应用程序：**

> **注意：** 如果你已经在模块 01 中使用 `./start-all.sh` 启动了所有应用程序，此模块已经在 8081 端口运行。你可以跳过下面的启动命令，直接访问 http://localhost:8081。

**选项 1：使用 Spring Boot Dashboard（推荐 VS Code 用户）**

开发容器内包含 Spring Boot Dashboard 扩展，提供一个可视化界面管理所有 Spring Boot 应用。你可以在 VS Code 左侧活动栏中找到它（寻找 Spring Boot 图标）。

通过 Spring Boot Dashboard，你可以：
- 查看工作区内所有可用的 Spring Boot 应用
- 一键启动/停止应用
- 实时查看应用日志
- 监控应用状态

只需点击“rag”旁边的播放按钮即可启动本模块，或者一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*该截图展示了 VS Code 的 Spring Boot Dashboard，你可以通过它可视化地启动、停止和监控应用。*

**选项 2：使用 shell 脚本**

启动所有 web 应用（模块 01-04）：

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

这两个脚本会自动从根目录 `.env` 文件加载环境变量，并在 JAR 文件不存在时自动构建。

> **注意：** 如果你更倾向于在启动之前手动构建所有模块：
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

在浏览器中打开 http://localhost:8081。

**停止：**

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

该应用提供了用于文档上传和提问的网页界面。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-CN/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*该截图展示了 RAG 应用界面，你可以在这里上传文档并提问。*

### 上传文档

先上传一个文档 —— 测试时建议使用 TXT 文件。本目录提供了一个 `sample-document.txt`，其中包含关于 LangChain4j 功能、RAG 实现和最佳实践的信息 — 非常适合测试系统。

系统会处理你的文档，拆分成文本块，并为每个块创建嵌入。这在你上传时会自动完成。

### 提问

现在可以针对文档内容提具体问题。尝试问一些文档中明确说明的事实性问题。系统会搜索相关文本块，将它们包含到提示中，然后生成答案。

### 查看来源引用

每个答案都包含带有相似度分数的来源引用。这些分数在 0 到 1 之间，显示每个文本块与你提问的相关度。分数越高表示匹配越好。这样你就可以核查答案是否符合源材料。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-CN/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*该截图展示了查询结果，包括生成的答案、来源引用以及每个检索块的相关性评分。*

### 试验不同问题

尝试问不同类型的问题：
- 具体事实：“主要话题是什么？”
- 对比：“X 和 Y 有什么区别？”
- 总结：“总结关于 Z 的关键点”

观察相关性分数如何根据你的问题与文档内容匹配程度变化。

## 关键概念

### 文本拆分策略

文档被拆成 300 令牌的文本块，块与块之间重叠 30 令牌。这种平衡保证每个文本块既包含足够上下文以保持意义，同时又足够小以便能在提示中包含多个块。

### 相似度分数

每个检索到的文本块都有一个 0 到 1 之间的相似度分数，表示它与用户提问的匹配程度。下图可视化了分数范围及系统如何用它们过滤结果：

<img src="../../../translated_images/zh-CN/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*图示分数范围从 0 到 1，最小阈值为 0.5，过滤掉不相关的文本块。*

分数范围为：
- 0.7-1.0：高度相关，精确匹配
- 0.5-0.7：相关，有良好上下文
- 低于 0.5：过滤掉，差异太大

系统只检索超过最小阈值的文本块以保证结果质量。

嵌入在意义清晰聚类时表现良好，但也有盲点。下图展示了常见的失败模式 —— 文本块过大导致向量模糊，文本块过小缺失上下文，模糊词语对应多个聚类，精确匹配查找（比如 ID、零件号）根本不适合用嵌入：

<img src="../../../translated_images/zh-CN/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*图中展示了常见的嵌入失败模式：文本块太大、太小，模糊词语指向多个聚类，以及像 ID 这样的精确匹配查找不能用嵌入。*

### 内存存储

本模块为了简化使用了内存存储。重启应用后，上传的文档会丢失。生产环境一般使用持久化向量数据库，如 Qdrant 或 Azure AI Search。

### 上下文窗口管理

每个模型有最大上下文窗口，不能包含所有大文档的文本块。系统检索最相关的 N 个文本块（默认 5 块），以保证在限制内提供足够的上下文，获得准确答案。

## 何时使用 RAG

RAG 并非总是最佳方案。下图决策指南帮你判断何时 RAG 有价值，何时可以用更简单方案 —— 例如直接将内容包含在提示中，或仅依赖模型内置知识：

<img src="../../../translated_images/zh-CN/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*图示了何时 RAG 有价值，何时简单方案足够的决策指南。*

**何时使用 RAG：**
- 回答专有文档相关问题
- 信息频繁更新（政策、价格、规格）
- 需要准确的来源引用
- 内容太大，无法放入单个提示
- 需要可验证、有依据的回答

**何时不适用 RAG：**
- 问题为模型已有的通用知识
- 需要实时数据（RAG 仅对上传文档有效）
- 内容足够小，能直接放进提示中

## 接下来的步骤

**下一模块:** [04-tools - 使用工具的 AI 代理](../04-tools/README.md)

---

**导航:** [← 上一模块：02 - 提示工程](../02-prompt-engineering/README.md) | [回主目录](../README.md) | [下一模块：04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文档通过 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻译而成。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原文的母语版本应被视为权威来源。对于重要信息，建议使用专业人工翻译。我们不对因使用此翻译而产生的任何误解或误释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->