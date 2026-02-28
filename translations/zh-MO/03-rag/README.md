# Module 03: RAG (檢索增強生成)

## 目錄

- [影片導覽](../../../03-rag)
- [你將學到什麼](../../../03-rag)
- [先決條件](../../../03-rag)
- [認識 RAG](../../../03-rag)
  - [本教程使用哪種 RAG 方法？](../../../03-rag)
- [運作原理](../../../03-rag)
  - [文件處理](../../../03-rag)
  - [建立嵌入向量](../../../03-rag)
  - [語意搜尋](../../../03-rag)
  - [生成答案](../../../03-rag)
- [執行應用程式](../../../03-rag)
- [使用應用程式](../../../03-rag)
  - [上傳文件](../../../03-rag)
  - [提問](../../../03-rag)
  - [查看來源參考](../../../03-rag)
  - [試驗問題](../../../03-rag)
- [關鍵概念](../../../03-rag)
  - [分塊策略](../../../03-rag)
  - [相似度分數](../../../03-rag)
  - [記憶體儲存](../../../03-rag)
  - [上下文視窗管理](../../../03-rag)
- [RAG 何時重要](../../../03-rag)
- [後續步驟](../../../03-rag)

## 影片導覽

觀看本現場課程，說明如何開始使用本模組：[RAG with LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## 你將學到什麼

在之前的模組中，你學會了如何與 AI 進行對話並有效地結構化提示。但是有一個根本限制：語言模型只能知道它在訓練時學到的內容。它無法回答關於你公司政策、專案文件或任何未接受訓練的資訊。

RAG（檢索增強生成）解決了這個問題。與其試著教模型你的資訊（代價高且不實際），不如讓它有能力搜尋你的文件。當有人提問時，系統會找到相關資訊並將其包含在提示中。模型接著根據該檢索到的上下文回答。

你可以把 RAG 想像成給模型一個參考圖書館。當你提問時，系統會：

1. **使用者查詢** - 你提出問題  
2. **嵌入** - 將您的問題轉換成向量  
3. **向量搜尋** - 尋找類似的文件區塊  
4. **上下文組合** - 將相關區塊添加到提示中  
5. **回應** - 大型語言模型根據上下文生成回答  

這讓模型的回答依據你的實際數據，而不是依賴其訓練知識或胡亂生成答案。

## 先決條件

- 完成 [Module 00 - 快速入門](../00-quick-start/README.md)（針對上述的 Easy RAG 範例）  
- 完成 [Module 01 - 介紹](../01-introduction/README.md)（已部署 Azure OpenAI 資源，包括 `text-embedding-3-small` 嵌入模型）  
- 在根目錄有 `.env` 檔案，包含 Azure 認證（由 Module 01 的 `azd up` 指令建立）  

> **注意：** 如果尚未完成 Module 01，請先按照那裡的部署說明操作。 `azd up` 指令會同時部署 GPT 聊天模型和本模組使用的嵌入模型。

## 認識 RAG

下圖說明核心概念：不只依賴模型本身的訓練資料，RAG 給模型一個你的文件參考庫，讓它在生成每個答案前先查閱。

<img src="../../../translated_images/zh-MO/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*此圖展示標準大型語言模型（從訓練資料推測答案）與 RAG 強化的模型（先查閱你文件）的差別。*

這裡展示端對端如何串接。使用者問題透過四個階段流轉—嵌入、向量搜尋、上下文組裝與答案生成—每一步都基於前一步：

<img src="../../../translated_images/zh-MO/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*此圖展示端對端的 RAG 管線—使用者查詢經過嵌入、向量搜尋、上下文組裝，最後生成回答。*

本模組接下來將詳解每個階段，並附可執行及修改的程式碼。

### 本教程使用哪種 RAG 方法？

LangChain4j 提供三種實現 RAG 的方式，抽象層各不相同。下圖並排比較它們：

<img src="../../../translated_images/zh-MO/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*此圖比較 LangChain4j 三種 RAG 方法—Easy、Native 及 Advanced，展示關鍵元件與適用時機。*

| 方法 | 功能說明 | 權衡點 |
|---|---|---|
| **Easy RAG** | 透過 `AiServices` 和 `ContentRetriever` 自動接線。你注解接口，掛載檢索器，LangChain4j 在背後處理嵌入、搜尋與提示組合。 | 程式碼極簡，但看不到每步細節。 |
| **Native RAG** | 手動呼叫嵌入模型、搜尋庫、建立提示與生成答案—逐步顯示且可修改。 | 程式碼較多，但各階段明確且可調整。 |
| **Advanced RAG** | 使用 `RetrievalAugmentor` 框架，具可插拔查詢轉換器、路由器、再排序器與內容注入器，適合生產級管線。 | 彈性最大，複雜度亦最高。 |

**本教程採用 Native 方法。** RAG 管線每個步驟 — 問句嵌入、向量庫搜尋、上下文組合與答案生成 — 都在 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 中明確呈現。這是刻意設計的：作為學習資源，比起簡化代碼，更重要的是你能看見並理解每個階段。當你熟悉各部分如何串接後，可升級到 Easy RAG 快速原型，或 Advanced RAG 用於生產系統。

> **💡 已經看過 Easy RAG 的實例了嗎？** [快速入門模組](../00-quick-start/README.md) 有文檔問答範例 ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java))，使用 Easy RAG 方式 — LangChain4j 自動處理嵌入、搜尋與提示組合。本模組則是進一步拆解管線，讓你看到並控制每個階段。

<img src="../../../translated_images/zh-MO/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*此圖展示來自 `SimpleReaderDemo.java` 的 Easy RAG 管線。與本模組採用的 Native 方法比較：Easy RAG 將嵌入、檢索與提示組合封裝在 `AiServices` 和 `ContentRetriever` 之下 — 你只需載入文件並附加檢索器，即可獲得回答。Native 方式則拆開管線，由你逐步呼叫 （嵌入、搜尋、組合上下文、生成答案），完全可見且可控。*

## 運作原理

本模組中，RAG 管線拆解成四個階段，每次使用者提問時會依序執行。首先，上傳的文件會被**解析並分塊**為可管理大小的片段。這些片段接著被轉換成**向量嵌入**並儲存，以便可進行數學上的比較。當查詢進來時，系統執行**語意搜尋**找出最相關的片段，並最終將它們作為上下文傳給大型語言模型產生**回答**。下列章節將配合程式碼與圖示詳述每個階段。先看第一步。

### 文件處理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

當你上傳文件時，系統會解析該文件（PDF 或純文字），附加諸如檔名等元資料，然後把它分割成多個區塊—根據模型上下文視窗大小切成較小片段。區塊間會略有重疊，避免在邊界處失去理解上下文。

```java
// 解析上載的文件並包裝成 LangChain4j 文件
Document document = Document.from(content, metadata);

// 分割為 300 字符塊，重疊 30 字符
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
下圖視覺化說明切分過程。注意每個區塊和鄰近區塊共享部分 token — 30 個 token 的重疊確保不遺失重要上下文：

<img src="../../../translated_images/zh-MO/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*此圖展示文件被分割成每塊約 300 個 token，鄰塊間重疊 30 個 token，保留邊界上下文。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天嘗試：** 打開 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) 並提問：  
> - 「LangChain4j 如何將文件分成區塊？為什麼重疊很重要？」  
> - 「不同文件類型的最佳分塊大小是多少？為什麼？」  
> - 「如何處理多語言文件或格式特殊的文件？」

### 建立嵌入向量

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每個區塊都會轉換成稱為嵌入向量的數值表示—本質上是將語意轉成數字的工具。嵌入模型不像聊天模型一樣「有智慧」；它無法遵循指令、推理或回答問題。它所能做的是把文字映射到數學空間，讓含義相近的文本彼此接近 — 例如「car」和「automobile」鄰近，「refund policy」和「return my money」也貸相近。你可把聊天模型當成可對話的人，而嵌入模型則是極佳的分類系統。

<img src="../../../translated_images/zh-MO/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*圖示展示嵌入模型如何將文字轉為數值向量，相似意義（如「car」與「automobile」）在向量空間鄰近。*

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
  
下方類別圖展示 RAG 管線中兩個獨立流程和實現它們的 LangChain4j 類別。**攝取流程**（上傳時執行一次）負責切文檔、嵌入區塊並透過 `.addAll()` 儲存。**查詢流程**（每次提問時執行）先嵌入問題，透過 `.search()` 搜尋庫並將符合的上下文傳給聊天模型。兩流程共用 `EmbeddingStore<TextSegment>` 介面：

<img src="../../../translated_images/zh-MO/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*此圖示 RAG 管線兩大流程—攝取與查詢—及其如何透過共用 EmbeddingStore 介面連接。*

一旦嵌入向量被儲存，類似內容會自然在向量空間中聚群。下圖可視化展示相關主題的文件如何集中為鄰近點，讓語意搜尋成為可能：

<img src="../../../translated_images/zh-MO/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*此視覺化展示相關文件在 3D 向量空間中聚合，像技術文件、商業規則與常見問題分布形成明確群組。*

當使用者搜尋時，系統執行四步驟：文件先嵌入一次，每次搜尋再嵌入查詢，使用餘弦相似度比較查詢向量與所有已儲存向量，並回傳得分最高的前 K 個區塊。下圖介紹每一步及涉及的 LangChain4j 類別：

<img src="../../../translated_images/zh-MO/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*此圖展示四步驟嵌入搜尋流程：文件嵌入、查詢嵌入、餘弦相似度比較，並回傳頂端結果。*

### 語意搜尋

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

當你提出問題，你的問題也會被轉為嵌入向量。系統會將你的問題嵌入與所有文件區塊嵌入相比較。它找到最相近含義的區塊—不只是關鍵字匹配，而是真正的語意相似。

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
  
下圖比較語意搜尋與傳統關鍵字搜尋。對「vehicle」（交通工具）使用關鍵字搜尋會錯過「cars and trucks」的區塊，但語意搜尋能理解它們意義相同，並把它當作高分匹配回傳：

<img src="../../../translated_images/zh-MO/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*此圖比較關鍵字搜尋和語意搜尋，顯示語意搜尋可找出概念相關的內容，即使關鍵字不同也能命中。*

在底層，相似度以餘弦相似度衡量—本質是在問「這兩支箭頭方向是否相同？」兩個區塊可用完全不同字詞，但若意義相同，它們向量指向相同方向，分數接近 1.0：

<img src="../../../translated_images/zh-MO/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*此圖示餘弦相似度為嵌入向量間的夾角—越對齊的向量分數越接近 1.0，表示更高語意相似度。*
> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 打開 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 並詢問：
> - "相似度搜索如何透過 embeddings 運作？分數是怎麼決定的？"
> - "應該使用什麼相似度閾值？這會如何影響結果？"
> - "若找不到相關文件，該如何處理？"

### 回答生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最相關的區段會被組合成一個結構化的提示，包含明確的指令、檢索到的上下文，以及使用者的提問。模型會閱讀這些特定區段並根據該資訊回答 — 它只能使用眼前的內容，避免產生幻覺。

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

以下圖示展示了這個組合過程 — 從搜索步驟中得分最高的區段注入到提示範本裡，`OpenAiOfficialChatModel` 則生成有根據的答案：

<img src="../../../translated_images/zh-MO/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*此圖示說明最高分區段如何被組合成結構化提示，使模型能從你的數據產生有根據的答案。*

## 運行應用程式

**驗證部署：**

確保根目錄有 `.env` 檔案，且內有 Azure 憑證（於模組 01 建立）：

**Bash:**
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已使用模組 01 的 `./start-all.sh` 啟動所有應用，則此模組已運行於埠號 8081。你可以跳過下面的啟動指令，直接訪問 http://localhost:8081。

**選項 1：使用 Spring Boot Dashboard（推薦 VS Code 使用者）**

開發容器包含 Spring Boot Dashboard 擴充功能，提供視覺化介面管理所有 Spring Boot 應用。你可以在 VS Code 左側活動欄找到（尋找 Spring Boot 圖標）。

透過 Spring Boot Dashboard，你可以：
- 查看工作區中所有可用的 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時查看應用日誌
- 監控應用狀態

只需點擊 "rag" 旁的播放按鈕啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*此截圖顯示 VS Code 中的 Spring Boot Dashboard，可視化地啟動、停止及監控應用。*

**選項 2：使用 shell 腳本**

啟動所有網頁應用（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 從根目錄
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

兩個腳本會自動從根目錄 `.env` 檔案載入環境變數，並在 JAR 檔案不存在時編譯。

> **注意：** 如果你想先手動編譯所有模組，再啟動：
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

在瀏覽器中開啟 http://localhost:8081 。

**停止：**

**Bash:**
```bash
./stop.sh  # 僅限此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只限此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

此應用提供文件上傳與提問的網頁介面。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-MO/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖展示了 RAG 應用介面，你可上傳文件並提出問題。*

### 上傳文件

先上傳文件 — TXT 檔測試效果最佳。此目錄提供 `sample-document.txt`，內容涵蓋 LangChain4j 功能、RAG 實作及最佳實踐，適合測試系統。

系統會處理你的文件，將其切割成多個區塊，並為每個區塊建立 embeddings。這會自動在上傳時完成。

### 提問

現在針對文件內容提出具體問題。試試明確且文件中有明確陳述的事實性問題。系統會搜尋相關區塊、將它們加入提示，並生成答案。

### 檢視來源參考

注意每個答案都會附帶帶有相似度分數的來源參考。這些分數（0 到 1）顯示每個區塊對於你提問的相關程度。分數越高，匹配度越佳。這讓你能檢核答案是否與原始資料吻合。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-MO/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖顯示查詢結果，含生成的答案、來源參考及每個檢索區塊的相關分數。*

### 嘗試不同問題

試試不同類型的問題：
- 具體事實：「主要主題是什麼？」
- 比較：「X 和 Y 有何不同？」
- 概述：「摘要關於 Z 的要點」

觀察相關分數如何依問題與文件內容的符合度而變動。

## 關鍵概念

### 區塊切割策略

文件被切成 300 字元的區塊，並重疊 30 字元。此平衡確保每個區塊具足夠上下文而有意義，同時大小適中可在提示中包含多個區塊。

### 相似度分數

每個檢索到的區塊都會搭配 0 至 1 之間的相似度分數，顯示與使用者問題的匹配程度。下圖視覺化分數範圍及系統如何用它們篩選結果：

<img src="../../../translated_images/zh-MO/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*此圖示顯示從 0 到 1 的分數範圍，最低門檻為 0.5，用以過濾不相關的區塊。*

分數範圍：
- 0.7-1.0：高度相關，精確匹配
- 0.5-0.7：相關，上下文良好
- 低於 0.5：被過濾，差異過大

系統僅檢索高於最低門檻的區塊以確保品質。

Embeddings 在意義清楚分群時效果佳，但存在盲點。以下圖示顯示常見失效模式 — 區塊過大導致向量混濁、區塊過小導致上下文不足、含糊詞彙指向多個群集，以及精確匹配查詢（如 ID、零件號）根本不適用 embeddings：

<img src="../../../translated_images/zh-MO/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*此圖示展示常見 embedding 失效模式：區塊過大、區塊過小、含糊詞彙指向多群集，以及精確匹配查詢（如 ID）失效。*

### 記憶體內存儲

此模組使用記憶體內存儲以簡化。重啟應用時，上傳的文件會消失。生產系統會利用持久化向量資料庫，例如 Qdrant 或 Azure AI Search。

### 上下文視窗管理

每款模型有最大上下文視窗限制。無法包含大型文件的所有區塊。系統擷取排名最高的 N 個區塊（預設 5 個），在限制內提供充足上下文以獲得準確答案。

## RAG 何時重要

RAG 並非總是合適解法。以下決策指引幫助你判斷何時 RAG 能增值，何時簡單作法就夠 — 例如直接把內容置入提示，或仰賴模型內建知識：

<img src="../../../translated_images/zh-MO/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*此圖表展示何時使用 RAG 有利，何時應簡單方法即可。*

**建議使用 RAG 時機：**
- 回答專有文件相關問題
- 資訊頻繁變動（政策、價格、規格）
- 需準確來源歸屬
- 內容過大無法塞入單一提示
- 需要可驗證、有根據的回覆

**不建議使用 RAG 時機：**
- 問題需要模型本身具備的一般知識
- 需要即時資料（RAG 基於已上傳文件）
- 內容夠小可直接放入提示

## 下一步

**下一模組：** [04-tools - 使用工具的 AI 代理](../04-tools/README.md)

---

**導覽：** [← 上一：模組 02 - 提示工程](../02-prompt-engineering/README.md) | [返回主頁](../README.md) | [下一：模組 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於翻譯準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們對因使用此翻譯而引起的任何誤解或誤釋概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->