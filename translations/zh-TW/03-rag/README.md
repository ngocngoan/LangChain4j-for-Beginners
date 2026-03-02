# Module 03: RAG（檢索增強生成）

## 目錄

- [影片導覽](../../../03-rag)
- [你將學到什麼](../../../03-rag)
- [先決條件](../../../03-rag)
- [理解 RAG](../../../03-rag)
  - [本教程使用哪種 RAG 方法？](../../../03-rag)
- [工作原理](../../../03-rag)
  - [文件處理](../../../03-rag)
  - [建立嵌入](../../../03-rag)
  - [語意搜尋](../../../03-rag)
  - [答案生成](../../../03-rag)
- [執行應用程式](../../../03-rag)
- [使用應用程式](../../../03-rag)
  - [上傳文件](../../../03-rag)
  - [提問](../../../03-rag)
  - [檢查來源參考](../../../03-rag)
  - [嘗試不同問題](../../../03-rag)
- [關鍵概念](../../../03-rag)
  - [切塊策略](../../../03-rag)
  - [相似度分數](../../../03-rag)
  - [記憶體儲存](../../../03-rag)
  - [上下文視窗管理](../../../03-rag)
- [何時使用 RAG](../../../03-rag)
- [下一步](../../../03-rag)

## 影片導覽

觀看此現場講解，了解如何開始使用本模組：

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## 你將學到什麼

在前面的模組中，你學會了如何與 AI 進行對話並有效構建提示。但有一個根本限制：語言模型只知道它在訓練期間學到的知識。它無法回答關於你公司的政策、你的專案文件或其他未經訓練的信息。

RAG（檢索增強生成）解決了這個問題。與其嘗試教模型你的資訊（這既昂貴又不切實際），你給它能力去檢索你的文件。當有人提問時，系統會找到相關資訊並將其納入提示中。模型接著根據這些檢索到的上下文來回答。

把 RAG 想像成給模型一個參考圖書館。當你提問時，系統：

1. **使用者查詢** - 你提出問題
2. **嵌入** - 將你的問題轉為向量
3. **向量搜尋** - 找到相似的文件切塊
4. **上下文組合** - 把相關切塊添加到提示中
5. **回應** - LLM 根據上下文生成答案

這使模型的回應基於你的實際資料，而不是僅依賴其訓練知識或臆造答案。

## 先決條件

- 完成 [Module 00 - 快速開始](../00-quick-start/README.md)（用於本模組後面引用的 Easy RAG 範例）
- 完成 [Module 01 - 介紹](../01-introduction/README.md)（Azure OpenAI 資源已部署，包括 `text-embedding-3-small` 嵌入模型）
- 根目錄有 `.env` 檔，包含 Azure 憑證（由 Module 01 中的 `azd up` 建立）

> **注意：** 如果你尚未完成 Module 01，請先按照該模組中的部署說明執行。`azd up` 指令同時部署 GPT 聊天模型及本模組使用的嵌入模型。

## 理解 RAG

下圖說明了核心概念：RAG 不單依賴模型的訓練資料，而是讓模型先查閱你的文件參考庫，再生成每個答案。

<img src="../../../translated_images/zh-TW/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*此圖展示標準 LLM（從訓練資料猜測）與 RAG 增強型 LLM（先咨詢你的文件）的區別。*

以下說明整個流程的連接方式。使用者的問題經歷四個階段 —— 嵌入、向量搜尋、上下文組合及答案生成 —— 串聯起來：

<img src="../../../translated_images/zh-TW/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*此圖展示完整的 RAG 管線—使用者查詢依序經歷嵌入、向量搜尋、上下文組合與答案生成。*

本模組之後將詳細走訪每個階段，包含可執行且可修改的程式碼。

### 本教程使用哪種 RAG 方法？

LangChain4j 提供三種實作 RAG 的方式，抽象層不同。下圖並列比較：

<img src="../../../translated_images/zh-TW/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*此圖比較 LangChain4j 的三種 RAG 方法 —— Easy、Native 和 Advanced —— 顯示其主要組件及適用情境。*

| 方法 | 功能說明 | 權衡 |
|---|---|---|
| **Easy RAG** | 透過 `AiServices` 和 `ContentRetriever` 自動連線各項功能。你只需標註介面並附加檢索器，LangChain4j 背後處理嵌入、搜尋和提示組合。 | 程式碼最少，但無法看到每步發生的細節。 |
| **Native RAG** | 你自行呼叫嵌入模型、搜尋資料庫、構建提示、生成答案，每一步都清楚明白。 | 程式碼較多，但每階段都可見且可修改。 |
| **Advanced RAG** | 利用 `RetrievalAugmentor` 架構，配合可插拔的查詢轉換器、路由器、重排器和內容注入器，打造生產級管線。 | 靈活度最高，但複雜度也大幅提升。 |

**本教程採用 Native 方式。** RAG 管線的每個階段 —— 問句嵌入、向量搜尋、上下文組合及答案生成 —— 都明確寫在 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 中。這是故意為之：作為學習資源，讓你能完整看到並理解每個步驟比縮減程式碼更重要。當你熟悉各部分如何串接後，可進階使用 Easy RAG 做快速原型，或使用 Advanced RAG 建置生產系統。

> **💡 已經見過 Easy RAG 的實作嗎？** [快速開始模組](../00-quick-start/README.md) 中包含一個文件問答範例（[`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)），採用 Easy RAG 方式 —— LangChain4j 自動處理嵌入、搜尋和提示組合。本模組則進一步拆解該流程，讓你能自行控制每個階段。

下圖展示快速開始範例中的 Easy RAG 管線。可見 `AiServices` 和 `EmbeddingStoreContentRetriever` 將所有複雜度隱藏——你只需載入文件、附加檢索器，便能取得答案。本模組的 Native 方式則拆解這些隱藏步驟：

<img src="../../../translated_images/zh-TW/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*此圖展示 `SimpleReaderDemo.java` 中的 Easy RAG 管線。與本模組使用的 Native 方式相比：Easy RAG 把嵌入、檢索與提示組合隱藏在 `AiServices` 和 `ContentRetriever` 背後 —— 你只是載入文件、附加檢索器，然後直接取得答案。本模組的 Native 方式則讓你自行呼叫各階段（嵌入、搜尋、組合上下文、生成），獲得完整透明且可控的流程。*

## 工作原理

本模組的 RAG 管線分成四個階段，當使用者提問時依序執行。首先，上傳的文件會被 **解析並切塊** 成容易處理的小段。這些切塊接著被轉成 **向量嵌入**，並儲存用以數學比較。當有查詢時，系統會執行 **語意搜尋** 找出最相關切塊，最後將這些切塊作為上下文傳給 LLM 進行 **答案生成**。以下章節逐步說明每個階段，包含實際程式碼與圖表。先來看第一步。

### 文件處理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

當你上傳文件時，系統會解析它（PDF 或純文字），附加檔名等元資料，然後將文件切分為切塊 —— 能在模型上下文視窗內舒適處理的較小段落。這些切塊略有重疊，避免上下文在切割點丟失。

```java
// 解析上傳的檔案並包裝成 LangChain4j 文件
Document document = Document.from(content, metadata);

// 分割成 300 代幣的區塊，重疊 30 代幣
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

下圖展示視覺化的運作方式。請注意每個切塊與鄰近切塊間共享一些令牌 —— 30 令牌的重疊確保不會有重要上下文落在切割縫隙中：

<img src="../../../translated_images/zh-TW/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*此圖展示文件如何被切成每塊 300 令牌且相互重疊 30 令牌的切塊，確保切塊邊界上下文連貫。*

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) 聊天：**開啟 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)，並提出：
> - 「LangChain4j 如何將文件切成塊？為什麼重疊很重要？」
> - 「不同文件類型的最佳切塊大小是多少？為什麼？」
> - 「如何處理多語言或特殊格式的文件？」

### 建立嵌入

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每個切塊會被轉成一種數值表示，稱為嵌入 —— 本質上是一個意義到數字的轉換器。嵌入模型不像聊天模型那樣「智能」；它不能理解指令、推理或回答問題。它做得到的是將文本映射到一個數學空間，意義相近的詞彙會落在鄰近位置 —— 「car」與「automobile」、「refund policy」與「return my money」靠得很近。把聊天模型想成一個能互動的人，而嵌入模型是超好的檔案歸檔系統。

下圖視覺化此概念 —— 文本輸入，數字向量輸出，類似意義的文本在向量空間中相互接近：

<img src="../../../translated_images/zh-TW/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*此圖展示嵌入模型如何將文本轉為數值向量，讓「car」和「automobile」等相似意義詞彙在向量空間中靠近。*

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

下圖呈現 RAG 管線中兩條分流及其對應的 LangChain4j 類別。**攝取流程**（上傳時執行一次）分割文件、產生切塊嵌入，並透過 `.addAll()` 儲存。**查詢流程**（每次使用者提問執行）將問題嵌入，透過 `.search()` 搜索存儲，回傳匹配的上下文給聊天模型。兩流程皆連接同一個 `EmbeddingStore<TextSegment>` 介面：

<img src="../../../translated_images/zh-TW/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*此圖呈現 RAG 管線中的兩條流程——攝取與查詢——及其如何透過共用 EmbeddingStore 介面銜接。*

一旦嵌入儲存完成，類似內容會自然聚集在向量空間中。下圖示範關聯主題的文件如何在三維向量空間中集群，實現語意搜尋的基礎：

<img src="../../../translated_images/zh-TW/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*此視覺化呈現相關文件如何在三維向量空間聚集，技術文件、商務規則與常見問題組成不同群組。*

當使用者搜尋時，系統執行四個步驟：文件嵌入只做一次、每次搜尋對查詢進行嵌入、使用餘弦相似度比較查詢向量和所有儲存向量，最後回傳排名前 K 的切塊。下圖走訪每步及相應 LangChain4j 類別：

<img src="../../../translated_images/zh-TW/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*此圖示範四步嵌入搜尋流程：文件嵌入、查詢嵌入、利用餘弦相似度比較向量、回傳前 K 結果。*

### 語意搜尋

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

當你提出問題時，問題本身也會被轉為嵌入。系統將你的問題嵌入與所有文件切塊的嵌入比對。它找出語意最相近的切塊 —— 不只匹配關鍵字，而是實際上的語意相似。

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

下圖對照語意搜尋與傳統關鍵字搜尋。搜尋「vehicle」的關鍵字法會錯過「cars and trucks」的切塊，但語意搜尋明白兩者意義相同，將該切塊高分回傳：

<img src="../../../translated_images/zh-TW/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*此圖比較關鍵字搜尋與語意搜尋，展示語意搜尋如何在關鍵字不同時也能檢索到概念相關內容。*
底層中，相似度是使用餘弦相似度測量 — 本質上是在問「這兩支箭頭是否指向相同方向？」兩段文字的用詞可以完全不同，但如果意思相同，它們的向量指向相同方向，得分接近 1.0：

<img src="../../../translated_images/zh-TW/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*此圖示意餘弦相似度為嵌入向量間的角度 — 向量越一致，得分越接近 1.0，表示語意相似度越高。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 開啟 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 並詢問：
> - 「相似度搜尋如何使用嵌入向量運作？分數是怎麼決定的？」
> - 「應該使用什麼相似度門檻？這會如何影響結果？」
> - 「如果找不到相關文件，我應該怎麼處理？」

### 答案產生

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最相關的文字片段會組裝成結構化提示，包括明確指示、檢索到的上下文和使用者問題。模型讀取這些特定片段並根據這些資訊回答 — 它只能使用眼前的內容，避免幻覺。

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

下圖顯示此組裝的運作方式 — 搜尋步驟中最高分的片段注入提示模板，`OpenAiOfficialChatModel` 則生成有根據的答案：

<img src="../../../translated_images/zh-TW/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*此圖顯示如何將最高得分的片段組裝成結構化提示，讓模型從你的數據中生成有根據的答案。*

## 執行應用程式

**確認部署：**

確保專案根目錄存在 `.env` 檔案，內含 Azure 憑證（於模組 01 建立）。於此模組目錄 (`03-rag/`) 中執行：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已從根目錄使用 `./start-all.sh` 啟動所有應用程式（如模組 01 所述），此模組已在 8081 埠執行。你可以跳過以下啟動指令，直接造訪 http://localhost:8081。

**選項 1：使用 Spring Boot Dashboard（建議 VS Code 用戶）**

開發容器包含 Spring Boot Dashboard 擴充套件，提供視覺介面管理所有 Spring Boot 應用。你可在 VS Code 左側活動列找到（尋找 Spring Boot 圖示）。

透過 Spring Boot Dashboard，你可以：
- 查看工作區中所有可用的 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時觀看應用日誌
- 監控應用狀態

點擊「rag」旁的播放按鈕啟動此模組，或一次啟動全部模組。

<img src="../../../translated_images/zh-TW/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*此截圖展示 VS Code 中的 Spring Boot Dashboard，讓你可視化啟動、停止與監控應用。*

**選項 2：使用 Shell 腳本**

啟動所有網頁應用（模組 01-04）：

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

或只啟動此模組：

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

兩套腳本會自動從根目錄 `.env` 載入環境變數，若 JAR 不存在則會建構。

> **注意：** 若你偏好先手動建構所有模組，再啟動：
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

於瀏覽器開啟 http://localhost:8081 。

**停止應用程式：**

**Bash:**
```bash
./stop.sh  # 僅限此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

應用提供文件上傳及提問的網頁介面。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-TW/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖顯示 RAG 應用介面，供上傳文件與提問。*

### 上傳文件

先上傳一個文件 — TXT 格式最適合測試。本目錄中提供一份 `sample-document.txt`，涵蓋 LangChain4j 特性、RAG 實作與最佳實務，非常適合測試系統。

系統會處理你的文件，拆分成多個片段，並為每個片段建立嵌入向量。這會在上傳時自動執行。

### 提問

現在針對文件內容提出具體問題。試著問一些文件中明確敘述的事實性問題。系統會搜尋相關片段，將它們包含進提示，並生成答案。

### 檢查來源引用

注意每個答案都附有來源參考及相似度分數。這些分數 (0 到 1) 表示片段與你問題的相關程度。分數越高表示匹配越好。這讓你能核對答案是否符合原始資料。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-TW/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖顯示查詢結果，包括生成答案、來源參考以及每個擷取片段的相關性分數。*

### 嘗試不同問題

試試不同類型的問題：
- 具體事實：「主要主題是什麼？」
- 比較：「X 和 Y 有什麼不同？」
- 摘要：「總結關於 Z 的要點」

觀察相關性分數如何依你的問題與文件內容符合程度改變。

## 核心概念

### 分段策略

文件分割成 300 個 token 的片段，重疊 30 個 token。此平衡確保每段具有足夠上下文且大小適中，能在提示中包含多個片段。

### 相似度分數

每個擷取的片段都會帶有 0 到 1 的相似度分數，表示與用戶問題的匹配程度。下圖展示分數範圍及系統如何用它來篩選結果：

<img src="../../../translated_images/zh-TW/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*此圖展示 0 到 1 的分數範圍，系統設定最低 0.5 門檻以過濾無關片段。*

分數範圍為：
- 0.7-1.0：高度相關，精確匹配
- 0.5-0.7：相關，具良好上下文
- 低於 0.5：過濾掉，過於不同

系統僅擷取高於最低門檻的片段以確保品質。

嵌入在語意群聚清晰時效果良好，但也存在盲點。下圖顯示常見失敗模式 — 片段過大造成向量模糊，片段過小缺乏上下文，模糊詞彙對應多個群聚，以及精確匹配查詢（ID、零件號）完全無法用嵌入：

<img src="../../../translated_images/zh-TW/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*此圖顯示常見嵌入失敗模式：片段過大、片段過小、模糊詞彙對應多群聚，以及像 ID 這類精確匹配無法用嵌入解決。*

### 記憶體儲存

此模組使用記憶體儲存以簡化設計。重啟應用時，已上傳文件會遺失。正式環境會使用持久向量資料庫，例如 Qdrant 或 Azure AI Search。

### 上下文視窗管理

每個模型均有最大上下文視窗大小限制。無法涵蓋大型文件的所有片段。系統會擷取前 N 個最高相關片段（預設 5 個），在限制內提供足夠上下文以產生準確答案。

## RAG 何時重要

RAG 並非總是最佳解決方案。下圖決策指南幫助你判斷何時 RAG 有幫助，何時簡單作法（直接將內容包含在提示或依賴模型內建知識）即可：

<img src="../../../translated_images/zh-TW/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*此圖展示何時使用 RAG 可增值，何時可用簡單方法即可應付的決策指南。*

## 後續步驟

**下一模組：** [04-tools - 使用工具的 AI 代理](../04-tools/README.md)

---

**導覽：** [← 上一節：模組 02 - 提示工程](../02-prompt-engineering/README.md) | [回主頁](../README.md) | [下一節：模組 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件是使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯的。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不準確之處。文件的原始語言版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤譯負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->