# Module 03: RAG（檢索增強生成）

## 目錄

- [影片導覽](../../../03-rag)
- [你將會學習到什麼](../../../03-rag)
- [先決條件](../../../03-rag)
- [認識 RAG](../../../03-rag)
  - [本教學使用哪種 RAG 方式？](../../../03-rag)
- [運作原理](../../../03-rag)
  - [文件處理](../../../03-rag)
  - [建立嵌入向量](../../../03-rag)
  - [語義搜尋](../../../03-rag)
  - [生成答案](../../../03-rag)
- [執行應用程式](../../../03-rag)
- [使用應用程式](../../../03-rag)
  - [上傳文件](../../../03-rag)
  - [提問](../../../03-rag)
  - [查看來源引用](../../../03-rag)
  - [實驗問答](../../../03-rag)
- [關鍵概念](../../../03-rag)
  - [切割策略](../../../03-rag)
  - [相似度分數](../../../03-rag)
  - [記憶體存儲](../../../03-rag)
  - [上下文視窗管理](../../../03-rag)
- [RAG 何時重要](../../../03-rag)
- [後續步驟](../../../03-rag)

## 影片導覽

觀看這個直播課程，說明如何開始本模組的操作：[RAG with LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## 你將會學習到什麼

在之前的模組中，你學會了如何與 AI 進行對話及有效結構化提示。但有一個根本性限制：語言模型只能知道訓練期間學到的知識。它們無法回答關於你公司政策、專案文件，或任何沒被訓練到的資訊。

RAG（檢索增強生成）解決了這個問題。與其試圖教會模型你的資訊（既昂貴又不切實際），你改為賦予它檢索文件的能力。當有人提問時，系統會找到相關資訊並將其加入提示中。模型接著根據擷取的上下文來回答。

把 RAG 想像成給模型一個參考圖書館。當你提問時，系統：

1. **使用者查詢** — 你提問
2. **嵌入向量** — 將你的問題轉換成向量
3. **向量搜尋** — 找出相似的文件片段
4. **上下文組裝** — 將相關片段加入提示
5. **回應** — 大型語言模型根據上下文產生答案

這讓模型的回答以你的真實資料為根據，而非仰賴訓練知識或憑空編造答案。

## 先決條件

- 完成 [Module 00 - 快速開始](../00-quick-start/README.md)（用於上述簡易 RAG 範例）
- 完成 [Module 01 - 介紹](../01-introduction/README.md)（部署 Azure OpenAI 資源，包括 `text-embedding-3-small` 嵌入向量模型）
- 根目錄下 `.env` 檔案包含 Azure 認證（由 Module 01 中的 `azd up` 建立）

> **注意：** 如果尚未完成 Module 01，請先照那邊的部署說明執行。`azd up` 指令會同時部署 GPT 聊天模型及本模組使用的嵌入模型。

## 認識 RAG

下圖說明核心概念：RAG 並不只仰賴模型的訓練資料，而是給它一個你的文件參考庫，以供生成每個答案前查詢。

<img src="../../../translated_images/zh-HK/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*這張圖展示標準大型語言模型（從訓練資料中猜測）與 RAG 增強型大型語言模型（先查資料文件）的差異。*

以下是端到端連接的流程。一個使用者提問經歷四個階段──嵌入、向量搜尋、上下文組裝、生成回答──每一步依序進行：

<img src="../../../translated_images/zh-HK/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*這張圖展示完整的 RAG 流程──使用者查詢經過嵌入、向量搜尋、上下文組裝，最後生成回答。*

接下來本模組會逐步介紹每個階段，附有可執行且可修改的程式碼。

### 本教學使用哪種 RAG 方式？

LangChain4j 提供三種實作 RAG 的方法，抽象層級各有不同。下圖橫向比較三者：

<img src="../../../translated_images/zh-HK/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*這張圖比較了 LangChain4j 的三種 RAG 方法──簡易（Easy）、原生（Native）及進階（Advanced）──標示主要元件與適用情境。*

| 方法 | 功能 | 權衡 |
|---|---|---|
| **簡易 RAG** | 全自動連接 `AiServices` 和 `ContentRetriever`。你只需標註介面並附加檢索器，LangChain4j 會在背後處理嵌入、搜尋與提示組裝。 | 程式碼極少，但你不會看到每步驟的細節。 |
| **原生 RAG** | 由你逐步呼叫嵌入模型、搜尋存儲庫、構建提示、生成回答──每一步明確呈現。 | 程式碼較多，但每個階段都清楚且可修改。 |
| **進階 RAG** | 使用帶有可插拔查詢轉換器、路由器、重新排序器及內容注入器的 `RetrievalAugmentor` 框架，適合生產級管線。 | 彈性最大，但複雜度顯著提高。 |

**本教學採用原生做法。** RAG 管線的每個步驟──查詢嵌入、向量庫搜尋、上下文組裝、答案生成──都在 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 中明確撰寫。這是有意為之：作為學習資源，讓你看見並理解每個階段比精簡程式碼更重要。一旦熟悉工作流程，你可以轉向簡易 RAG 進行快速原型，或進階 RAG 用於生產系統。

> **💡 已看過簡易 RAG？** [快速開始模組](../00-quick-start/README.md) 包含文件問答範例（[`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)）使用簡易 RAG，LangChain4j 自動負責嵌入、搜尋與提示組裝。本模組則打開該管線，讓你看見並控制每個階段。

<img src="../../../translated_images/zh-HK/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*這張圖展示 `SimpleReaderDemo.java` 的簡易 RAG 管線。比起本模組使用的原生做法──原生做法明確呼叫每個階段（嵌入、搜尋、組裝上下文、生成），提供完全透明度與控制權；簡易做法則將嵌入、檢索與提示組裝隱藏在 `AiServices` 和 `ContentRetriever` 後面──你上傳文件、附加檢索器、然後直接獲得答案。*

## 運作原理

本模組的 RAG 管線分為四個連續階段，使用者每提問一次就執行一遍。首先，將上傳的文件**解析並分割成多個片段**。這些片段接著被轉換為**向量嵌入**並儲存以利數學比較。當收到查詢時，系統會透過**語義搜尋**找到最相關的片段，最後將他們作為上下文傳遞給大型語言模型以**生成答案**。以下段落依序說明每個階段，附帶實際程式碼和示意圖。讓我們從第一步開始。

### 文件處理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

當你上傳文件時，系統會解析PDF或純文字檔案，附加檔名等元資料，然後將文件切割成片段──適合模型上下文視窗大小的小段。這些片段間有輕微重疊，避免在邊界處丟失上下文。

```java
// 解析上載的檔案並包裝成 LangChain4j 文件
Document document = Document.from(content, metadata);

// 分割成 300 個標記的區塊，重疊 30 個標記
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

下面示意圖直觀展示此過程。注意每個片段與鄰近片段共享部分 token──30 token 的重疊確保不漏掉任何重要上下文：

<img src="../../../translated_images/zh-HK/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*此圖展示文件被切分為每段 300 token，重疊 30 token，以保留片段邊界處的上下文。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 開啟 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)，然後問：
> - "LangChain4j 如何切割文件成多個片段？為什麼重疊很重要？"
> - "不同文件類型的最佳切割大小是多少？為什麼？"
> - "如何處理多語言或具特殊格式的文件？"

### 建立嵌入向量

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每個片段都會被轉換成數值表示，稱為嵌入向量──本質上一種意義轉數字的轉換器。嵌入模型不像聊天模型有「智慧」，它無法執行指令、推理或回答問題。它能做的，是將文字映射到數學空間，在其中相似意義會靠得很近──例如「car」會與「automobile」鄰近，「refund policy」與「return my money」接近。你可以想像聊天模型像是一個可以對話的人；嵌入模型則是超優秀的資料整理系統。

<img src="../../../translated_images/zh-HK/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*這張圖示範嵌入模型如何將文字轉成數值向量，讓相似意義（如「car」和「automobile」）在向量空間中緊密相鄰。*

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

下面的類別圖顯示 RAG 管線中兩個獨立流程與 LangChain4j 中所用類別。**攝取流程**（檔案上傳時執行一次）切割文件、向各片段嵌入並透過 `.addAll()` 儲存。**查詢流程**（使用者每次提問執行）將問題嵌入，透過 `.search()` 搜索存儲庫，並將找到的上下文傳給聊天模型。兩者匯聚於共用的 `EmbeddingStore<TextSegment>` 介面：

<img src="../../../translated_images/zh-HK/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*這張圖示範 RAG 管線兩個流程──攝取與查詢──如何透過共用的 EmbeddingStore 相互連接。*

一旦嵌入向量被存入，相關內容自然在向量空間中聚類。下圖顯示相關主題的文件如何形成鄰近點，使得語義搜尋得以實現：

<img src="../../../translated_images/zh-HK/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*此可視化呈現相關文檔如何在三維向量空間中聚集，技術文件、業務規則和常見問題形成不同群組。*

使用者搜尋時，系統執行四個步驟：文件一次嵌入、每次查詢對問題嵌入、使用餘弦相似度比對問題向量與所有存儲向量，並返回前 K 個最高分片段。下圖說明每步和所用 LangChain4j 類別：

<img src="../../../translated_images/zh-HK/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*此圖示四步嵌入搜尋流程：嵌入文件、嵌入問題、以餘弦相似度比較向量，並返回前 K 結果。*

### 語義搜尋

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

當你提問時，問題也會被轉成嵌入向量。系統比較問題嵌入與所有文件片段的嵌入，尋找語義最接近的片段──不只是關鍵詞匹配，而是真正的語義相似。

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

下圖比較語義搜尋與傳統關鍵詞搜尋。關鍵詞搜尋「vehicle」會錯過提到「cars and trucks」的片段，但語義搜尋理解它們同義，並將該片段回傳為高分相符：

<img src="../../../translated_images/zh-HK/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*此圖比較關鍵詞搜尋與語義搜尋，展示語義搜尋能回傳概念相關內容，即使關鍵詞不完全匹配。*

背後使用的相似度度量方法是餘弦相似度──本質是判斷「這兩支箭頭是否同向？」兩個片段用詞完全不同，但若意義相近，向量即會指向相同方向，得分接近 1.0：

<img src="../../../translated_images/zh-HK/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*這張圖示範餘弦相似度為嵌入向量間角度──越一致的向量分數越接近 1.0，代表語義相似度越高。*
> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 打開 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 並提問：
> - 「相似度搜索如何透過嵌入運作，評分是如何決定的？」
> - 「我應該使用什麼相似度閾值，這會如何影響結果？」
> - 「若找不到相關文件該如何處理？」

### 回答生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最相關的段落會被組裝成一個結構化的提示，其中包含明確的指示、檢索到的上下文以及用戶的問題。模型根據這些特定段落閱讀並回答問題 — 它只能使用眼前資訊，這避免了幻覺問題。

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

以下圖示展示了這個組裝過程 — 來自搜索階段的最高得分段落被注入提示範本，`OpenAiOfficialChatModel` 則透過這些內容生成有根據的答案：

<img src="../../../translated_images/zh-HK/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*此圖示展示了如何將最高得分的段落組裝為結構化提示，使模型能根據您的資料生成有根據的回答。*

## 執行應用程式

**確認部署：**

確保根目錄下有 `.env` 文件，內含 Azure 憑證（於模組 01 創建）：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果您已在模組 01 使用 `./start-all.sh` 啟動所有應用程式，本模組已在 8081 埠運行。您可以跳過下方啟動指令，直接訪問 http://localhost:8081。

**方案 1：使用 Spring Boot 儀表板（推薦給 VS Code 用戶）**

開發容器中包含 Spring Boot 儀表板擴充，提供視覺化介面管理所有 Spring Boot 應用程式。您可以在 VS Code 左側的活動欄找到它（尋找 Spring Boot 圖示）。

透過 Spring Boot 儀表板，您可以：
- 查看工作區內所有可用的 Spring Boot 應用程式
- 一鍵啟動／停止應用程式
- 即時查看應用程式日誌
- 監控應用程式狀態

只需點擊「rag」旁的播放鈕即可啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*此截圖顯示 VS Code 中的 Spring Boot 儀表板，您可視覺化啟動、停止並監控應用程式。*

**方案 2：使用 shell 腳本**

啟動所有網頁應用程式（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 從根目錄開始
.\start-all.ps1
```

或者只啟動本模組：

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

兩個腳本都會自動從根目錄的 `.env` 檔載入環境變數，且若 JAR 檔不存在會自動編譯。

> **注意：** 若您想先手動編譯所有模組再啟動：
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

在瀏覽器開啟 http://localhost:8081。

**停止應用程式：**

**Bash:**
```bash
./stop.sh  # 僅此模塊
# 或
cd .. && ./stop-all.sh  # 所有模塊
```

**PowerShell:**
```powershell
.\stop.ps1  # 只有呢個模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

此應用程式提供文件上傳與提問的網頁介面。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-HK/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖顯示 RAG 應用介面，您可上傳文件並提出問題。*

### 上傳文件

首先上傳一個文件 — TXT 格式最適合測試。此目錄內有附贈的 `sample-document.txt`，提供了 LangChain4j 功能、RAG 實作及最佳實務資訊，非常適合用來測試系統。

系統會自動處理您的文件，拆分成段落並為每個段落建立嵌入。您只需一上傳即可完成這些步驟。

### 提出問題

接著針對文件內容提出具體問題。請嘗試提問明確且文件中明確敘述的事實。系統會搜尋相關段落、將它們包含進提示中，並生成答案。

### 檢查來源引用

每個答案均會包含來源參考及相似度分數。這些分數（介於 0 到 1）展現該段落與您的問題的相關度，分數越高表示匹配度越好。這允許您將答案與原始資料驗證。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-HK/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖展示查詢結果，包括生成的答案、來源參考及每個檢索到段落的相關分數。*

### 嘗試不同問題

嘗試不同類型的問題：
- 具體事實：「主要議題是什麼？」
- 比較：「X 和 Y 差別在哪？」
- 摘要：「請總結 Z 的要點」

觀察相似度分數如何根據您的問題與文件內容匹配度而改變。

## 核心概念

### 拆分策略

文件會拆分成 300 個 tokens 的段落，且有 30 個 tokens 的重疊。這樣的平衡確保每段有足夠上下文具意義，同時段落足夠小，可在提示中包含多段。

### 相似度分數

每個檢索回來的段落都有一個介於 0 到 1 的相似度分數，表示它與用戶問題的匹配度。下圖視覺化分數範圍及系統如何利用它們過濾結果：

<img src="../../../translated_images/zh-HK/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*此圖示展示分數區間從 0 到 1，並設有 0.5 的最低閾值來過濾不相關段落。*

分數範圍：
- 0.7-1.0：高度相關，精確匹配
- 0.5-0.7：相關，提供良好上下文
- 低於 0.5：被過濾，不足以匹配

系統只檢索高於最低閾值的段落以保證品質。

嵌入在意義清楚聚合時效果良好，但存在盲點。下圖示常見失效模式 — 段落太大造成向量模糊，段落太小缺乏上下文，模糊詞彙指向多重聚合，以及 ID、零件號等精確匹配擷取根本不適用嵌入：

<img src="../../../translated_images/zh-HK/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*此圖示展示常見嵌入失效模式：段落過大、過小、多義詞指向多聚合，以及 ID 等精確匹配。*

### 記憶體存儲

本模組為簡便使用記憶體存儲。重啟應用程式會失去上傳文件。生產系統會使用持久化向量資料庫，如 Qdrant 或 Azure AI Search。

### 上下文窗口管理

每種模型有最大上下文視窗限制。無法包含大型文件的每個段落。系統會檢索前 N 個最相關的段落（預設為 5 個），在限制內提供足夠上下文以獲得準確答案。

## 何時使用 RAG 很重要

RAG 並非總是正確方案。下圖決策指南幫助您判斷何時 RAG 有價值，何時較簡單手法（如直接在提示內含內容，或依賴模型內建知識）即足夠：

<img src="../../../translated_images/zh-HK/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*此圖展示何時 RAG 有價值，何時簡單方案夠用的決策指南。*

**適用 RAG 時機：**
- 回答專有文件相關問題
- 資訊頻繁變動（政策、價格、規格）
- 需要準確的來源引用
- 內容過大無法放入同一提示
- 需要可驗證、有根據的回應

**不適用 RAG 時機：**
- 問題需一般模型已具備的知識
- 需要實時資料（RAG 只基於上傳文件運作）
- 內容足夠小能直接放入提示

## 下一步

**下一模組：** [04-tools - 使用工具的 AI 代理](../04-tools/README.md)

---

**導覽：** [← 上一章：模組 02 - 提示工程](../02-prompt-engineering/README.md) | [回主頁](../README.md) | [下一章：模組 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們盡力確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的原文應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用此翻譯而引起的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->