# 模組 03：RAG（檢索增強生成）

## 目錄

- [視頻導覽](../../../03-rag)
- [你將學到什麼](../../../03-rag)
- [先決條件](../../../03-rag)
- [理解 RAG](../../../03-rag)
  - [本教程使用哪種 RAG 方法？](../../../03-rag)
- [運作原理](../../../03-rag)
  - [文件處理](../../../03-rag)
  - [建立向量嵌入](../../../03-rag)
  - [語意搜尋](../../../03-rag)
  - [答案生成](../../../03-rag)
- [執行應用程式](../../../03-rag)
- [使用應用程式](../../../03-rag)
  - [上傳文件](../../../03-rag)
  - [提問](../../../03-rag)
  - [檢查來源參考](../../../03-rag)
  - [嘗試問題](../../../03-rag)
- [關鍵概念](../../../03-rag)
  - [分塊策略](../../../03-rag)
  - [相似度分數](../../../03-rag)
  - [記憶體儲存](../../../03-rag)
  - [上下文視窗管理](../../../03-rag)
- [何時 RAG 重要](../../../03-rag)
- [後續步驟](../../../03-rag)

## 視頻導覽

觀看這個直播課程，了解如何開始使用本模組：

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## 你將學到什麼

在之前的模組中，你學會了如何與 AI 進行對話並有效地構建提示詞。但有一個根本限制：語言模型只知道訓練期間學到的東西。它們無法回答有關你公司政策、專案文件或任何未在訓練中涉及的資訊。

RAG（檢索增強生成）解決了這個問題。它不嘗試教模型你的資訊（這既昂貴又不實用），而是賦予它搜尋你的文件的能力。當有人提問時，系統會找到相關資訊並將其包含在提示中。模型根據這些檢索到的上下文來回答問題。

把 RAG 想像成給模型一個參考圖書館。當你提問時，系統：

1. **使用者查詢** — 你提出問題
2. **向量嵌入** — 將問題轉換成向量
3. **向量搜尋** — 找出相似的文件塊
4. **上下文組合** — 將相關塊加入提示
5. **回應** — LLM 根據上下文產生答案

這樣讓模型的回答有據可依，基於你的實際數據，而非僅靠訓練知識或自己杜撰答案。

## 先決條件

- 已完成 [模組 00 - 快速開始](../00-quick-start/README.md)（用於上述簡易 RAG 範例）
- 已完成 [模組 01 - 介紹](../01-introduction/README.md)（已部署 Azure OpenAI 資源，包括 `text-embedding-3-small` 嵌入模型）
- 根目錄有 `.env` 檔，包含 Azure 認證（由模組 01 中的 `azd up` 建立）

> **注意：** 如果還沒完成模組 01，請先依該模組說明完成部署。`azd up` 指令會部署 GPT 聊天模型及本模組使用的嵌入模型。

## 理解 RAG

下圖說明核心概念：不只是依賴模型的訓練資料，RAG 讓模型可以參考你的文件資料庫，然後再生成每個答案。

<img src="../../../translated_images/zh-MO/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*此圖展示標準 LLM（僅依訓練資料猜測）與 RAG 增強 LLM（先參考你的文件）之間的差異。*

以下是端到端流程圖。使用者提問經過四個階段 —— 嵌入、向量搜尋、上下文組合及答案生成 —— 每個環節相互連接：

<img src="../../../translated_images/zh-MO/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*此圖展示 RAG 流程端到端，使用者查詢經過嵌入、向量搜尋、上下文組合和答案生成。*

本模組後續將針對每一階段詳細介紹，並提供可執行與可修改的程式碼。

### 本教程使用哪種 RAG 方法？

LangChain4j 提供三種實作 RAG 的方式，抽象層級不同。下圖並排對比：

<img src="../../../translated_images/zh-MO/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*此圖比較 LangChain4j 的三種 RAG 方法 —— 簡易型、原生型與進階型，展示其關鍵元件及適用情境。*

| 方法 | 功能 | 權衡 |
|---|---|---|
| **簡易型 RAG** | 透過 `AiServices` 和 `ContentRetriever` 自動處理所有流程。你只需標注介面、附加檢索器，LangChain4j 在背後自動完成嵌入、搜索和提示組合。 | 代碼最少，但無法看到每步驟內的細節。 |
| **原生型 RAG** | 你自行調用嵌入模型、搜尋儲存庫、建立提示並生成答案，每步都明確可見。 | 程式碼較多，但每階段皆可見且可修改。 |
| **進階型 RAG** | 使用 `RetrievalAugmentor` 框架，可配置查詢轉換器、路由器、重排序器及內容注入器，適合製作生產級流程。 | 彈性最大，但開發複雜度顯著增加。 |

**本教程採用原生型。** RAG 管線的每一階段 —— 查詢嵌入、向量搜尋、上下文組合及答案生成 —— 在 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 中都有明確寫出。這是刻意為之：作為學習資源，看到並理解每個步驟遠比代碼量少重要。熟悉流程後，你可以轉向簡易型快速原型或進階型打造生產系統。

> **💡 已見過簡易型 RAG 嗎？** [快速開始模組](../00-quick-start/README.md) 包含文件問答範例（[`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)），使用簡易型 RAG —— LangChain4j 自動處理嵌入、搜尋和提示組合。本模組更進一步，拆開流程讓你親自呼叫、控制每個階段。

<img src="../../../translated_images/zh-MO/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*此圖展示 `SimpleReaderDemo.java` 中的簡易型 RAG 管線。與本模組使用的原生型對比：簡易型將嵌入、檢索與提示組合隱藏在 `AiServices` 和 `ContentRetriever` 背後 —— 你負責上傳文件、附加檢索器，隨後獲得答案；原生型則拆解整個流程，需你手動呼叫每個階段（嵌入、搜尋、組合上下文、生成），以獲得完全可見性和控制權。*

## 運作原理

本模組的 RAG 管線分為四個階段，使用者每次提問時依序執行。首先，已上傳的文件會被**解析並切分**成可管理小塊。這些小塊會轉成**向量嵌入**並儲存，方便後續以數學方法比較。接著系統執行**語意搜尋**找出最相關的片段，最後將這些上下文輸入到 LLM 以進行**答案生成**。下節將搭配程式碼與圖示逐步說明每個階段，先從第一步看起。

### 文件處理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

當你上傳文件時，系統會解析它（PDF 或純文字），附加檔案名稱等元資料，然後切分成多個小塊 —— 適合模型上下文視窗使用的小片段。這些小塊之間有輕微重疊，確保不會在邊界遺失重要上下文。

```java
// 解析上載嘅文件並包裝成 LangChain4j 文件
Document document = Document.from(content, metadata);

// 分割成每塊 300 個標記，重疊 30 個標記
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

下圖視覺化展示了此過程。每個小塊都與鄰近小塊共享部分標記 —— 30 個標記的重疊確保不會漏掉重要背景：

<img src="../../../translated_images/zh-MO/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*此圖示展示文件被分割成 300 標記的小塊，且相鄰塊之間重疊 30 標記，保留邊界上下文。*

> **🤖 嘗試與 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)，詢問：
> - 「LangChain4j 如何將文件切分成小塊？為何重疊重要？」
> - 「針對不同文件類型，最佳的塊大小是多少？原因是甚麼？」
> - 「怎麼處理多語言或含特殊格式的文件？」

### 建立向量嵌入

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每個文件小塊會被轉成數值表示，稱為向量嵌入 —— 基本上是一種將意義轉成數字的轉換器。嵌入模型不像聊天模型那樣「智能」；它不會執行指令、推理或回答問題。它的功能是將相似意義的文字映射到數學空間中彼此靠近 —— 例如「車」靠近「汽車」，「退款政策」靠近「退還款項」。可以把聊天模型想像成一個會對話的人；嵌入模型則是超強的分類系統。

<img src="../../../translated_images/zh-MO/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*此圖示展示嵌入模型如何將文字轉換成數值向量，使相似意義詞彙如「車」和「汽車」在向量空間中彼此靠近。*

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

下方類別圖顯示了 RAG 管線的兩条獨立流程與 LangChain4j 中實作它們的類別。**攝取流程**（上傳時執行一次）切分文件、嵌入小塊、透過 `.addAll()` 儲存。**查詢流程**（使用者每次提問時執行）將問題嵌入、使用 `.search()` 查找儲存、並將匹配上下文送到聊天模型。兩條流程共用 `EmbeddingStore<TextSegment>` 介面：

<img src="../../../translated_images/zh-MO/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*此圖展示 RAG 管線中攝取與查詢兩流程，以及它們如何透過嵌入儲存介面鍵合。*

一旦嵌入被儲存，相似內容自然在向量空間中聚集成群。下方視覺化展示有關連主題的文件在 3D 向量空間內成為鄰近點，促成語意搜尋的可能性：

<img src="../../../translated_images/zh-MO/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*此視覺圖示顯示相關文件在 3D 向量空間中群聚，技術文件、商業規則和常見問題形成各自區塊。*

當使用者查詢時，系統執行四步：先嵌入文件（一次完成），接著每次查詢嵌入問題，使用餘弦相似度比較問題向量與所有儲存向量，最後回傳前 K 名最高分塊。下圖解說每步與相關 LangChain4j 類別：

<img src="../../../translated_images/zh-MO/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*此圖展示四步嵌入搜尋流程：文件嵌入、問題嵌入、向量餘弦相似度比較及回傳 top-K 結果。*

### 語意搜尋

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

當你提問時，問題也會被嵌入成向量。系統比較你問題的嵌入向量與所有文件小塊的嵌入向量，找出意義最相似的片段 —— 不只是關鍵字匹配，而是實際的語意相似度。

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

下圖對比語意搜尋與傳統關鍵字搜尋。關鍵字搜尋針對「vehicle」找不到涵蓋「cars and trucks」的片段，而語意搜尋能理解語意相近，並將此片段當高分匹配回傳：

<img src="../../../translated_images/zh-MO/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*此圖對比關鍵字搜尋與語意搜尋，展示語意搜尋如何取得概念相關內容，即使關鍵字不相同。*

底層以餘弦相似度衡量相似性 —— 本質上是在問「這兩根箭頭指向同一方向嗎？」兩段文字可能用字完全不同，但意義相同時向量方向接近，分數接近 1.0：

<img src="../../../translated_images/zh-MO/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*此圖說明餘弦相似度作為嵌入向量之間的角度 — 向量越對齊，分數越接近 1.0，表示語義相似度越高。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天功能：** 打開 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 並詢問：
> - 「相似度搜尋如何透過嵌入向量運作？分數是如何決定的？」
> - 「我應該使用什麼相似度閾值？它如何影響結果？」
> - 「遇到找不到相關文件怎麼辦？」

### 答案生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最相關的文本區塊會組合成一個結構化提示，其中包含明確指示、檢索到的上下文和使用者的問題。模型會閱讀這些特定區塊並基於此資訊回答 — 它只能使用面前的內容，有助防止幻覺。

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

下圖展示了此組合過程 — 搜尋步驟中得分最高的區塊被注入提示模板，`OpenAiOfficialChatModel` 產生具根據的答案：

<img src="../../../translated_images/zh-MO/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*此圖說明如何將得分最高的區塊組合成結構化提示，使模型能從您的資料中生成根據性的答案。*

## 執行應用程式

**確認部署：**

請確保根目錄有 `.env` 檔案並包含 Azure 憑證（於模組 01 建立）：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果您已使用模組 01 的 `./start-all.sh` 命令啟動所有應用程式，本模組已在 8081 埠上執行。您可以略過下方啟動指令，直接開啟 http://localhost:8081 。

**選項 1：使用 Spring Boot 儀表板（建議 VS Code 使用者）**

開發容器包含 Spring Boot 儀表板擴充套件，提供視覺介面管理所有 Spring Boot 應用程式。您可在 VS Code 左側活動列找到（尋找 Spring Boot 圖示）。

在 Spring Boot 儀表板中，您可以：
- 查看工作區內所有可用 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時查看應用日誌
- 監控應用狀態

只需點擊「rag」旁的播放按鈕來啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*此截圖顯示 VS Code 中的 Spring Boot 儀表板，您可視覺化啟動、停止及監控應用。*

**選項 2：使用 shell 腳本**

啟動所有網頁應用程式（模組 01-04）：

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

或僅啟動此模組：

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

兩組腳本會自動從根目錄 `.env` 讀取環境變數，且若 JAR 不存在會自動建置。

> **注意：** 若您想手動建置所有模組後再啟動：
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

在瀏覽器開啟 http://localhost:8081 。

**停止應用：**

**Bash:**
```bash
./stop.sh  # 只限此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅限此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

此應用提供文件上載及問答的網頁介面。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-MO/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖顯示 RAG 應用介面，您可以上載文件並提問。*

### 上傳文件

從上傳文件開始 — TXT 檔最適合測試。本目錄提供一個 `sample-document.txt`，內容涵蓋 LangChain4j 特性、RAG 實作與最佳實務，適合用來測試系統。

系統會處理您的文件，拆分成區塊，並為每個區塊建立嵌入向量。此過程會在您上傳時自動完成。

### 提問

接著針對文件內容提出具體問題。嘗試問些文件明確敘述的事實。系統會搜尋相關區塊，將其納入提示並生成答案。

### 檢查來源參考

注意每個答案都包含來源參考與相似度分數。這些分數（0 到 1）顯示每個區塊與您問題的相關度。分數越高表示匹配越好。這能幫助您核對答案是否與來源資料一致。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-MO/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖展示查詢結果，包括生成的答案、來源參考及每個檢索區塊的相關度分數。*

### 問題實驗

嘗試不同類型的問題：
- 具體事實：「主題是什麼？」
- 比較：「X 與 Y 有什麼差別？」
- 摘要：「幫我總結 Z 的重點」

觀察相關度分數如何根據提問與文件內容的匹配度變化。

## 關鍵概念

### 區塊拆分策略

文件被拆成 300 代幣的區塊，重疊部分為 30 代幣。此平衡確保每個區塊有足夠上下文又能保持較小尺寸，方便於提示中包含多個區塊。

### 相似度分數

每個檢索到的區塊會帶有 0 到 1 的相似度分數，代表與使用者問題的匹配度。下圖視覺化分數範圍及系統如何利用閾值過濾結果：

<img src="../../../translated_images/zh-MO/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*此圖示分數範圍由 0 到 1，設定最低閾值為 0.5，過濾掉無關區塊。*

分數分佈：
- 0.7-1.0：高度相關，精確匹配
- 0.5-0.7：相關，具良好上下文
- 低於 0.5：過濾，差異太大

系統只檢索高於最低閾值的區塊以確保品質。

嵌入向量在語義聚類清晰時效果良好，但存在盲點。下圖展示常見失敗模式 — 區塊過大導致向量模糊、過小缺乏上下文、模糊詞指向多個聚類，以及 ID、零件號等精確匹配無法透過嵌入向量實現：

<img src="../../../translated_images/zh-MO/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*此圖示常見嵌入向量失敗模式：區塊過大、區塊過小、模糊詞指向多聚類、精確匹配查詢 (如 ID)。*

### 記憶體內存儲

本模組為簡化使用記憶體內存儲。重啟應用後，上傳的文件會遺失。生產環境會使用像是 Qdrant 或 Azure AI Search 等持久化向量資料庫。

### 上下文窗口管理

每個模型有最大上下文窗口限制。無法將大型文件的所有區塊全部納入。系統會檢索與問題最相關的前 N 個區塊（預設 5 個），在限制內提供足夠上下文以確保回答精確。

## 何時使用 RAG

RAG 並非所有情況皆合適。下圖決策指南助您判斷何時 RAG 有價值，何時較簡單的方法（如直接將內容納入提示或依賴模型內建知識）已足夠：

<img src="../../../translated_images/zh-MO/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*此圖為何時使用 RAG 的決策指南，顯示何時 RAG 具價值，何時簡單方法足夠。*

**何時使用 RAG：**
- 解答專屬文件相關問題
- 資訊經常變動（政策、價格、規格）
- 需準確附上來源
- 內容過大，無法於單一提示中容納
- 需要可驗證、有根據的回答

**何時不使用 RAG：**
- 問題只需模型已有的一般知識
- 需即時資料（RAG 僅適用上傳文件）
- 內容小，可直接納入提示中

## 下一步

**下一模組：** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**導覽：** [← 上一章：模組 02 - 提示工程](../02-prompt-engineering/README.md) | [回主頁](../README.md) | [下一章：模組 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件之原文版本應視為權威來源。對於重要資訊，建議採用專業人工翻譯。對於因使用本翻譯而引致之任何誤解或誤釋，本公司概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->