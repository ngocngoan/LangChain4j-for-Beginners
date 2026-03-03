# Module 03: RAG (檢索增強生成)

## Table of Contents

- [Video Walkthrough](../../../03-rag)
- [What You'll Learn](../../../03-rag)
- [Prerequisites](../../../03-rag)
- [Understanding RAG](../../../03-rag)
  - [Which RAG Approach Does This Tutorial Use?](../../../03-rag)
- [How It Works](../../../03-rag)
  - [Document Processing](../../../03-rag)
  - [Creating Embeddings](../../../03-rag)
  - [Semantic Search](../../../03-rag)
  - [Answer Generation](../../../03-rag)
- [Run the Application](../../../03-rag)
- [Using the Application](../../../03-rag)
  - [Upload a Document](../../../03-rag)
  - [Ask Questions](../../../03-rag)
  - [Check Source References](../../../03-rag)
  - [Experiment with Questions](../../../03-rag)
- [Key Concepts](../../../03-rag)
  - [Chunking Strategy](../../../03-rag)
  - [Similarity Scores](../../../03-rag)
  - [In-Memory Storage](../../../03-rag)
  - [Context Window Management](../../../03-rag)
- [When RAG Matters](../../../03-rag)
- [Next Steps](../../../03-rag)

## Video Walkthrough

觀看此直播課程，講解如何開始使用本模組：

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

在前面的模組中，你學會了如何與 AI 進行對話，並有效構建提示語。但有一個根本限制：語言模型只知道它在訓練期間學到的東西。它無法回答關於你公司政策、專案文件或任何未被訓練過的資訊的問題。

RAG（檢索增強生成）解決了這個問題。它不是試圖教模型你的資訊（這既昂貴又不實際），而是賦予模型搜尋你的文件的能力。當有人提問時，系統會找到相關資訊並包含在提示中，模型根據檢索到的上下文來回答。

你可以將 RAG 想像成給模型一個參考圖書館。當你提出問題時，系統：

1. **使用者查詢** — 你提出問題  
2. **嵌入** — 將問題轉換成向量  
3. **向量搜尋** — 找到相似的文件片段  
4. **上下文組合** — 將相關片段加入提示  
5. **回應** — 大型語言模型根據上下文生成答案  

這讓模型的回應基於你的實際數據，而不是依賴其訓練知識或捏造答案。

## Prerequisites

- 完成 [Module 00 - Quick Start](../00-quick-start/README.md)（用於本模組後面提及的簡易 RAG 範例）
- 完成 [Module 01 - Introduction](../01-introduction/README.md)（已部署 Azure OpenAI 資源，包括 `text-embedding-3-small` 嵌入模型）
- 根目錄下有包含 Azure 憑證的 `.env` 檔案（由模組 01 中的 `azd up` 建立）

> **注意：** 如果尚未完成 Module 01，請先依該模組的部署說明操作。`azd up` 命令會部署本模組使用的 GPT 聊天模型與嵌入模型。

## Understanding RAG

下方圖示說明核心概念：RAG 不僅依賴模型訓練數據，還給模型提供了你的文件參考庫，讓它在生成每個答案前先查閱這些資料。

<img src="../../../translated_images/zh-HK/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*此圖展示標準大型語言模型（依訓練資料推測）與 RAG 增強大型語言模型（先查閱你的文件）的不同。*

以下展示整個流程如何連結成端到端體系。使用者問題經過四個階段——嵌入、向量搜尋、上下文組合及答案生成——階段層層遞進：

<img src="../../../translated_images/zh-HK/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*此圖顯示端到端 RAG 管道——使用者查詢依序經過嵌入、向量搜尋、上下文組合和答案生成。*

本模組接下來將詳細說明各個階段，並附上可執行與修改的程式碼。

### Which RAG Approach Does This Tutorial Use?

LangChain4j 提供三種 RAG 實作方式，抽象層級各異。下圖並列比較：

<img src="../../../translated_images/zh-HK/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*此圖比較 LangChain4j 的三種 RAG 方法——Easy、Native 和 Advanced——展示主要組件及使用場合。*

| 方式 | 功能說明 | 均衡考量 |
|---|---|---|
| **Easy RAG** | 透過 `AiServices` 和 `ContentRetriever` 自動串接所有流程。你只需註解接口，掛上檢索器，LangChain4j 背後自動執行嵌入、搜尋及提示組合。 | 代碼最少，但看不見每個步驟的運作。 |
| **Native RAG** | 你自己呼叫嵌入模型，搜尋存儲，組提示，生成答案——明確拆分每個步驟。 | 代碼較多，但每階段清晰可見且可修改。 |
| **Advanced RAG** | 使用帶可插拔查詢轉換器、路由器、重排序器及內容注入器的 `RetrievalAugmentor` 框架，適用於生產級管線。 | 彈性最大，但複雜度也最高。 |

**本教程採用 Native 方式。** RAG 管線的每一階段——查詢嵌入、向量搜尋、上下文組合與答案生成——都明確寫於 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)。此設計是故意如此：作為學習資源，比起代碼簡短，更重要的是看到並理解每個階段如何運作。一旦熟悉整體架構，便可轉向 Easy RAG 快速原型或 Advanced RAG 生產系統。

> **💡 已經看過 Easy RAG 範例？** [Quick Start 模組](../00-quick-start/README.md) 包含一個使用 Easy RAG 方法的文件問答範例（[`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)），LangChain4j 可自動執行嵌入、搜尋及提示組合。此模組進一步拆解該管線，讓你掌控各階段。

下圖展現 Quick Start 範例中的 Easy RAG 管線。注意 `AiServices` 和 `EmbeddingStoreContentRetriever` 如何隱藏複雜度——你只需載入文件、掛上檢索器，便能獲得答案。本模組的 Native 方法則拆解這些隱藏步驟：

<img src="../../../translated_images/zh-HK/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*此圖源自 `SimpleReaderDemo.java` 的 Easy RAG 管線。比對本模組使用的 Native 方法：Easy RAG 透過 `AiServices` 和 `ContentRetriever` 將嵌入、檢索和提示組合隱藏起來——你載入文件、掛檢索器，輕鬆獲答案。而 Native 方法則自己呼叫每個階段（嵌入、搜尋、組合上下文、生成）以提供完全的可視性與控制權。*

## How It Works

本模組的 RAG 管線分為四個階段，每次使用者提問時依序執行。首先，上傳文件被 **解析與切塊** 成可管理的區塊。這些區塊接著轉成 **向量嵌入** 並存儲，以進行數學比對。當查詢進來，系統執行 **語義搜尋** 找出最相關區塊，最後將它們當作上下文交給大型語言模型進行 **答案生成**。以下章節逐步解析每一環節並附上程式碼與圖示。我們先從第一步開始。

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

當你上傳文件時，系統會解析它（PDF 或純文字），附加檔名等元資料，然後將文件切成多個切塊——較小的片段，方便放入模型的上下文視窗。這些切塊彼此有些許重疊，以免上下文在分界處丟失。

```java
// 解析已上傳的檔案並包裝成 LangChain4j 文件
Document document = Document.from(content, metadata);

// 分割成每塊 300 個 token，並重疊 30 個 token
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

下圖視覺化展示此流程。注意每個切塊與鄰近切塊共享部分標記——30 標記重疊確保不會遺失重要上下文：

<img src="../../../translated_images/zh-HK/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*此圖展示文件被拆成 300 標記切塊，每個切塊有 30 標記重疊，維護切塊邊界的上下文連續性。*

> **🤖 用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 開啟 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) 並問：
> - 「LangChain4j 如何將文件拆成切塊，為什麼重疊重要？」
> - 「不同文件類型的最佳切塊大小是什麼？為什麼？」
> - 「如何處理多語言或特殊格式文件？」

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每個切塊會被轉成一種數值表示，稱為嵌入——本質上是將意義轉成數字。嵌入模型不像聊天模型「聰明」，它無法執行指令、推理或回答問題。它能做的是將文本映射到一個數學空間，讓意義相似的文字在向量空間中互相靠近——「汽車」和「車子」會靠得很近，「退款政策」會和「退錢」附近。你可以把聊天模型想像成一個會說話的人，而嵌入模型就是超級優秀的檔案系統。

下圖視覺化此概念——輸入文字，產出數值向量，意義相近的文字彼此位置相近：

<img src="../../../translated_images/zh-HK/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*此圖示範嵌入模型如何將文字轉成數值向量，讓像是「汽車」和「車子」這類相近意思的詞，在向量空間中彼此靠近。*

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

下圖為類圖，描繪 RAG 管線的兩條流程及 LangChain4j 對應類別。**資料匯入流程**（於上傳時執行一次）拆文件、嵌入切塊，透過 `.addAll()` 儲存。**查詢流程**（每次用戶提問時執行）嵌入問題、透過 `.search()` 搜尋存儲，再將匹配的上下文傳給聊天模型。兩條流程共用 `EmbeddingStore<TextSegment>` 接口：

<img src="../../../translated_images/zh-HK/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*此圖顯示 RAG 管線的兩條流程——資料匯入與查詢——如何透過共用的 EmbeddingStore 連結。*

嵌入存入後，相似內容會自然於向量空間群聚。下圖展示相關主題的文件如何聚集為附近點群，讓語義搜尋成為可能：

<img src="../../../translated_images/zh-HK/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*此視覺化展示在三維向量空間中，相關的文件如技術文件、商業規則和常見問題，會聚集成不同群組。*

使用時，系統遵循四步驟：文檔先嵌入一次，每次搜索嵌入查詢，利用餘弦相似度比對查詢向量和所有存向量，並回傳最高分的前 K 個切塊。下圖逐步說明流程及相關 LangChain4j 類別：

<img src="../../../translated_images/zh-HK/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*此圖展示嵌入搜尋的四步驟：嵌入文檔、嵌入查詢、利用餘弦相似度比較向量，並返回前 K 名結果。*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

當你提問時，問題本身也會被嵌入。系統將你的問題嵌入向量與所有文件切塊嵌入向量比對，找出意義最相近的切塊——不僅是字面關鍵詞匹配，而是真正的語義相似。

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

下圖比較語義搜尋與傳統關鍵詞搜尋。關鍵詞搜尋「vehicle」會漏掉一個談論「汽車與卡車」的切塊，但語義搜尋明白它們意思相同，並將其視為高分匹配結果：

<img src="../../../translated_images/zh-HK/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*此圖比較基於關鍵字的搜尋和語義搜尋，展示語義搜尋如何擷取即使關鍵詞不同，但概念相近的內容。*
在底層，使用餘弦相似度來衡量相似性 — 本質上是在問「這兩支箭頭是否指向相同方向？」兩個文本區塊可以用完全不同的詞語，但如果它們的意思相同，它們的向量就會指向相同方向，分數接近 1.0：

<img src="../../../translated_images/zh-HK/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*此圖說明餘弦相似度為嵌入向量間的角度 — 向量越一致，分數越接近 1.0，表示語意相似度越高。*

> **🤖 試試 [GitHub Copilot](https://github.com/features/copilot) Chat：** 打開 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 並詢問：
> - 「相似度搜尋如何使用嵌入向量運作，分數由什麼決定？」
> - 「我應該使用什麼相似度閾值？這如何影響結果？」
> - 「如果找不到相關文件，要怎麼處理？」

### 答案生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最相關的文本區塊會組裝成一個結構化提示，其中包含明確指令、取回的上下文，及用戶問題。模型依據這些特定區塊來回答 — 它只能使用眼前的資料，這防止了幻覺現象。

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

下圖展示此組裝流程 — 從搜尋步驟中獲得最高分的區塊會注入到提示模板中，而 `OpenAiOfficialChatModel` 生成基於真實資訊的答案：

<img src="../../../translated_images/zh-HK/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*此圖顯示如何將最高分的文本區塊組裝成結構化提示，使模型能從你的資料中生成有根據的答案。*

## 運行應用程式

**確認部署：**

確保根目錄有 `.env` 檔案，內含 Azure 憑證（於模組 01 建立）。從本模組目錄（`03-rag/`）執行：

**Bash:**
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已從根目錄使用 `./start-all.sh` 啟動所有應用程式（如模組 01 所述），本模組已在 8081 埠執行。你可以跳過以下啟動指令，直接瀏覽 http://localhost:8081。

**選項 1：使用 Spring Boot Dashboard（推薦給 VS Code 用戶）**

開發容器內建 Spring Boot Dashboard 擴充功能，提供可視化介面管理所有 Spring Boot 應用程式。可在 VS Code 左側活動欄找到（尋找 Spring Boot 圖示）。

從 Spring Boot Dashboard 你可以：
- 查看工作區內所有可用 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 實時查看應用程式日誌
- 監控應用程式狀態

只需點擊「rag」旁的播放按鈕即可啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*此截圖展示 VS Code 的 Spring Boot Dashboard，讓你可視化啟動、停止並監控應用程式。*

**選項 2：使用 shell 腳本**

啟動所有網頁應用程式（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄開始
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 從根目錄開始
.\start-all.ps1
```

或只啟動本模組：

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

兩個腳本會自動從根目錄 `.env` 載入環境變數，且如果 JAR 存在，就會進行建置。

> **注意：** 若你想在啟動前手動建置所有模組：
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

在瀏覽器打開 http://localhost:8081。

**停止指令：**

**Bash:**
```bash
./stop.sh  # 只有此模塊
# 或者
cd .. && ./stop-all.sh  # 所有模塊
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

此應用程式提供文件上傳及提問的網頁介面。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-HK/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖展示 RAG 應用界面，讓你上傳文件並提出問題。*

### 上傳文件

從上傳文件開始 — TXT 格式最適合測試。本目錄提供了一份 `sample-document.txt`，內容關於 LangChain4j 功能、RAG 實作與最佳實踐，適合用來測試系統。

系統會處理你的文件，拆分成多個區塊，並為每個區塊建立嵌入向量。這些過程會在上傳時自動執行。

### 提問

現在可以針對文件內容問具體問題。試著問內容中明確說明的事實。系統會搜尋相關區塊，將它們包含到提示中，並生成答案。

### 檢查來源引用

每個答案都帶有帶有相似度分數的來源引用。這些分數（0 至 1）顯示每個區塊跟你的問題貼合度。分數越高，表示匹配越好。這讓你能夠根據原始資料驗證答案。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-HK/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖展示查詢結果，包括生成的答案、來源引用及每個檢索區塊的相關性分數。*

### 嘗試問不同問題

試試不同類型的問題：
- 具體事實：「主要主題是什麼？」
- 比較：「X 和 Y 有什麼不同？」
- 摘要：「總結關於 Z 的重點」

觀察相關性分數如何依你的問題與文件內容匹配度變化。

## 主要概念

### 拆分策略

文件被拆成 300 代幣的區塊，重疊 30 代幣。此平衡讓每個區塊擁有足夠上下文，且大小適中，能在提示中包含多個區塊。

### 相似度分數

每個檢索的區塊都附帶 0 到 1 範圍的相似度分數，用來表示和用戶問題的匹配程度。下圖視覺化分數範圍及系統如何利用它們過濾結果：

<img src="../../../translated_images/zh-HK/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*此圖示範分數範圍從 0 到 1，最低閾值為 0.5，用以過濾不相關區塊。*

分數範圍：
- 0.7-1.0：高度相關，精確匹配
- 0.5-0.7：相關，良好上下文
- 低於 0.5：被過濾，過於不相似

系統只會檢索高於最低閾值的區塊以保證品質。

嵌入向量適用於意義分群清晰的情境，但也有盲點。下圖展示常見失效模式 — 區塊過大導致向量模糊，區塊過小缺乏上下文，模糊詞彙連向多個群集，以及基於嵌入無法運作的精確匹配查詢（如ID、零件號）：

<img src="../../../translated_images/zh-HK/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*此圖展示嵌入失效的常見模式：區塊過大、區塊過小、模糊詞彙指向多重群集、及ID等精確匹配查詢。*

### 記憶體內存儲

此模組為簡易起見使用記憶體內存儲。每次重新啟動應用，已上傳文件會遺失。生產系統則使用持久性向量資料庫，如 Qdrant 或 Azure AI Search。

### 上下文視窗管理

各模型有最大上下文視窗限制。你不能把大型檔案的所有區塊都包含進去。系統會檢索前 N 個最相關的區塊（預設為 5）以確保在限制內，並提供足夠上下文來產生正確答案。

## 何時使用 RAG

RAG 並非總是最佳方案。下圖決策指南幫助你判斷何時使用 RAG 具有價值，何時簡單方式已足夠 — 比如將內容直接放入提示或倚賴模型內建知識：

<img src="../../../translated_images/zh-HK/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*此圖示決策指南說明何時 RAG 有價值，何時簡單方法已足夠。*

## 下一步

**下一模組：** [04-tools - 使用工具的 AI 代理](../04-tools/README.md)

---

**導覽：** [← 上一節：模組 02 - 提示工程](../02-prompt-engineering/README.md) | [回主頁](../README.md) | [下一節：模組 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於提供準確的翻譯，但請注意自動翻譯可能包含錯誤或不準確之處。原始語言的文件應視為權威版本。對於重要資訊，建議使用專業人工翻譯。我們不對因使用本翻譯而引致的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->