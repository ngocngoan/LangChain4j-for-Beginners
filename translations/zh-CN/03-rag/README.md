# Module 03: RAG（检索增强生成）

## 目录

- [你将学到什么](../../../03-rag)
- [理解RAG](../../../03-rag)
- [先决条件](../../../03-rag)
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
  - [实验提问](../../../03-rag)
- [关键概念](../../../03-rag)
  - [分块策略](../../../03-rag)
  - [相似度评分](../../../03-rag)
  - [内存存储](../../../03-rag)
  - [上下文窗口管理](../../../03-rag)
- [何时使用RAG](../../../03-rag)
- [后续步骤](../../../03-rag)

## 你将学到什么

在之前的模块中，你学会了如何与AI对话以及如何有效构建提示。但有一个根本限制：语言模型只能基于训练时学到的内容。它们无法回答关于你公司政策、项目文档或任何未被训练的内容的问题。

RAG（检索增强生成）解决了这个问题。它不是试图教模型你的信息（这既昂贵又不切实际），而是赋予它在你的文档中搜索的能力。当有人提问时，系统会找到相关信息并将其包含在提示中。模型随后基于检索到的上下文进行回答。

可以把RAG看作给模型提供了一个参考图书馆。当你提问时，系统：

1. **用户查询** - 你提出问题  
2. **嵌入** - 将你的问题转换为向量  
3. **向量搜索** - 找到相似的文档块  
4. **上下文组装** - 将相关块添加到提示中  
5. **响应** - 语言模型根据上下文生成答案  

这使模型的回答基于你的实际数据，而不是仅凭训练知识或虚构答案。

## 理解RAG

下面的图解说明了核心概念：RAG不是只依赖模型的训练数据，而是给它一个你的文档参考库，让它在生成每个答案之前进行查阅。

<img src="../../../translated_images/zh-CN/what-is-rag.1f9005d44b07f2d8.webp" alt="什么是RAG" width="800"/>

以下展示了端到端的流程。用户的问题经过四个阶段——嵌入、向量搜索、上下文组装和答案生成——每一步都建立在前一步的基础上：

<img src="../../../translated_images/zh-CN/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG架构" width="800"/>

本模块剩余内容将详细讲解每个阶段，配有你可以运行和修改的代码。

## 先决条件

- 完成模块01（已部署Azure OpenAI资源）  
- 根目录下的`.env`文件包含Azure凭据（由模块01中的`azd up`创建）  

> **注意：** 如果尚未完成模块01，请先按照该模块的部署说明操作。

## 工作原理

### 文档处理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

当你上传文档时，系统会解析它（PDF或纯文本格式），附加如文件名等元数据，然后将其分割成适合模型上下文窗口的小块。这些块有轻微重叠，确保边界上下文不丢失。

```java
// 解析上传的文件并将其封装在 LangChain4j 文档中
Document document = Document.from(content, metadata);

// 拆分成300个标记的块，重叠30个标记
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
下面的图示直观展示了这一过程。注意每个块与邻居共享部分标记——30个标记的重叠确保重要上下文不会错失：

<img src="../../../translated_images/zh-CN/document-chunking.a5df1dd1383431ed.webp" alt="文档分块" width="800"/>

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)，询问：  
> - “LangChain4j如何将文档拆分成块，为什么重叠很重要？”  
> - “不同文档类型的最佳分块大小是多少，为什么？”  
> - “如何处理多语言或带特殊格式的文档？”

### 创建嵌入

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每个块都会被转换成称为嵌入的数值表示——本质上是捕捉文本含义的数学指纹。相似文本会生成相似的嵌入。

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
  
下面的类图展示了这些LangChain4j组件如何连接。`OpenAiOfficialEmbeddingModel`负责文本到向量的转换，`InMemoryEmbeddingStore`存储向量及其原始的`TextSegment`数据，`EmbeddingSearchRequest`控制检索参数如`maxResults`和`minScore`：

<img src="../../../translated_images/zh-CN/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG类" width="800"/>

嵌入存储完成后，内容相近的文档自然聚集在向量空间中。下面的可视化展示了相关主题文档如何成为相邻点，这正是语义搜索可行的原因：

<img src="../../../translated_images/zh-CN/vector-embeddings.2ef7bdddac79a327.webp" alt="向量嵌入空间" width="800"/>

### 语义搜索

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

当你提问时，问题也会被转换成嵌入。系统会将你的问题嵌入与所有文档块的嵌入进行比较。它寻找语义最相似的块——不仅仅是关键词匹配，而是真正的语义相似度。

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
  
下图对比了语义搜索与传统关键词搜索。针对“vehicle”的关键词搜索会错过包含“cars and trucks”的块，而语义搜索理解它们含义相同，并将其作为高分匹配返回：

<img src="../../../translated_images/zh-CN/semantic-search.6b790f21c86b849d.webp" alt="语义搜索" width="800"/>

> **🤖 试试用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打开 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)，询问：  
> - “嵌入的相似度搜索如何工作？什么决定评分？”  
> - “应该使用什么相似度阈值？它如何影响结果？”  
> - “如果找不到相关文档，如何处理？”

### 答案生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最相关的文档块会被组装成结构化提示，包含明确指令、检索到的上下文和用户的问题。模型阅读这些特定块并基于这些信息回答——它只能使用眼前信息，防止幻觉生成。

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
  
下图展示了这一组装过程——搜索阶段评分最高的文档块被注入提示模板，`OpenAiOfficialChatModel`基于此生成有依据的答案：

<img src="../../../translated_images/zh-CN/context-assembly.7e6dd60c31f95978.webp" alt="上下文组装" width="800"/>

## 运行应用程序

**验证部署：**

确保根目录存在包含Azure凭据的`.env`文件（在模块01中创建）：
```bash
cat ../.env  # 应显示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```
  
**启动应用程序：**

> **注意：** 如果你已经使用模块01中的`./start-all.sh`启动了所有应用程序，本模块已运行在8081端口，可以跳过以下启动步骤，直接访问 http://localhost:8081。

**选项1：使用Spring Boot仪表板（推荐VS Code用户）**

开发容器内含Spring Boot仪表板扩展，提供可视化界面管理所有Spring Boot应用。在VS Code左侧活动栏（寻找Spring Boot图标）中可以找到。

通过Spring Boot仪表板，你可以：
- 查看工作区内所有Spring Boot应用  
- 一键启动/停止应用  
- 实时查看应用日志  
- 监控应用状态  

点击“rag”旁的播放按钮启动本模块，或一次启动所有模块。

<img src="../../../translated_images/zh-CN/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot仪表板" width="400"/>

**选项2：使用shell脚本**

启动所有Web应用（模块01-04）：

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
  
或只启动本模块：

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
  
两个脚本都会自动从根目录`.env`文件加载环境变量，如果JAR包不存在，会自动构建。

> **注意：** 如果你想在启动前手动构建所有模块：  
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
  
在浏览器打开：http://localhost:8081。

**停止应用：**

**Bash:**  
```bash
./stop.sh  # 仅限此模块
# 或者
cd .. && ./stop-all.sh  # 所有模块
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # 仅限此模块
# 或者
cd ..; .\stop-all.ps1  # 所有模块
```
  

## 使用应用程序

该应用提供文档上传和提问的网页界面。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-CN/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG应用界面" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG应用界面 - 上传文档并提问*

### 上传文档

首先上传文档——TXT文件最适合测试。该目录下提供了一个`sample-document.txt`，包含关于LangChain4j功能、RAG实现及最佳实践的信息，适合测试系统。

系统会处理你的文档，分块，并为每个块创建嵌入。上传时自动完成。

### 提问

现在针对文档内容提具体问题。尝试提问文档中明确描述的事实。系统会搜索相关块，将它们包含在提示中并生成答案。

### 检查来源引用

注意每个答案都包含带有相似度评分的来源引用。评分（0到1）显示每个块与你问题的相关度。评分越高匹配越好，可帮助你核对答案与源材料是否一致。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-CN/rag-query-results.6d69fcec5397f355.webp" alt="RAG查询结果" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*查询结果，显示带来源引用和相关度评分的答案*

### 实验提问

尝试不同类型的问题：  
- 具体事实：“主要话题是什么？”  
- 比较：“X和Y有什么区别？”  
- 总结：“总结关于Z的要点”  

观察相关度评分如何根据提问与文档匹配程度变化。

## 关键概念

### 分块策略

文档被拆分为300标记大小的块，块间有30标记的重叠。这种平衡保证每块有足够上下文且足够小，可在提示中包含多个块。

### 相似度评分

每个检索出的块都有一个0到1之间的相似度分数，表示它与用户问题的匹配程度。下图展示了评分范围及系统如何用它们筛选结果：

<img src="../../../translated_images/zh-CN/similarity-scores.b0716aa911abf7f0.webp" alt="相似度评分" width="800"/>

评分范围：
- 0.7-1.0：高度相关，精确匹配  
- 0.5-0.7：相关，良好上下文  
- 低于0.5：被过滤，差异太大  

系统只检索高于最低阈值的块以保证质量。

### 内存存储

本模块使用内存存储以简化示例。重启应用时上传文档会丢失。生产环境会使用持久化向量数据库，如Qdrant或Azure AI搜索。

### 上下文窗口管理

每个模型有最大上下文窗口限制。不能将大文档的所有块都包括进来。系统会检索相关度最高的前N个块（默认5个），既满足限制又提供足够上下文生成准确答案。

## 何时使用RAG

RAG并非总是适用。下图的决策指引帮助你判断什么时候RAG带来价值，什么时候简单方法更合适，比如直接将内容包含进提示或依赖模型内置知识：

<img src="../../../translated_images/zh-CN/when-to-use-rag.1016223f6fea26bc.webp" alt="何时使用RAG" width="800"/>

**适用RAG的情况：**
- 回答有关专有文件的问题
- 信息经常变化（政策、价格、规格）
- 准确性需要来源归属
- 内容过大，无法放入单个提示中
- 需要可验证、有根据的回答

**不使用 RAG 的情况：**
- 问题需要模型已经具备的一般知识
- 需要实时数据（RAG 基于上传的文档）
- 内容足够小，可以直接包含在提示中

## 下一步

**下一个模块：** [04-tools - AI Agent 与工具](../04-tools/README.md)

---

**导航：** [← 上一页：模块 02 - 提示工程](../02-prompt-engineering/README.md) | [返回主页面](../README.md) | [下一页：模块 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**：
本文件由人工智能翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 译制。尽管我们力求准确，但请注意，自动翻译可能包含错误或不准确之处。原始语言版本的文档应被视为权威来源。对于重要信息，建议采用专业人工翻译。我们不对因使用此翻译而产生的任何误解或误释承担责任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->