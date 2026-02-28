# Module 03: RAG（檢索增強生成）

## 目錄

- [影片導覽](../../../03-rag)
- [你將學到什麼](../../../03-rag)
- [先決條件](../../../03-rag)
- [理解 RAG](../../../03-rag)
  - [本教學使用哪種 RAG 方法？](../../../03-rag)
- [運作原理](../../../03-rag)
  - [文件處理](../../../03-rag)
  - [創建嵌入向量](../../../03-rag)
  - [語義搜尋](../../../03-rag)
  - [答案生成](../../../03-rag)
- [執行應用程式](../../../03-rag)
- [使用應用程式](../../../03-rag)
  - [上傳文件](../../../03-rag)
  - [提出問題](../../../03-rag)
  - [檢查來源參考](../../../03-rag)
  - [實驗提問](../../../03-rag)
- [關鍵概念](../../../03-rag)
  - [分塊策略](../../../03-rag)
  - [相似度分數](../../../03-rag)
  - [記憶體中儲存](../../../03-rag)
  - [上下文視窗管理](../../../03-rag)
- [何時 RAG 很重要](../../../03-rag)
- [後續步驟](../../../03-rag)

## 影片導覽

觀看這段直播說明如何開始使用此模組：

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="LangChain4j 的 RAG 直播課程" width="800"/></a>

## 你將學到什麼

在前面的模組中，你學會了如何與 AI 對話以及如何有效組織提示，但有一個根本限制：語言模型只能利用訓練階段所學的知識。它們無法回答有關貴公司政策、專案文件或任何未訓練過的資訊的問題。

RAG（檢索增強生成）解決了這個問題。它不嘗試教模型你的資料（這既昂貴又不切實際），而是讓模型有能力搜尋你的文件。當有人提問時，系統會找出相關資訊並將其包含在提示中，模型便根據擷取到的上下文回答。

可以把 RAG 想成給模型一本參考圖書館。當你問一個問題時，系統會：

1. **使用者提問** - 你提出問題
2. **嵌入向量** - 將問題轉成向量
3. **向量搜尋** - 找出相似的文件片段
4. **上下文組合** - 將相關片段加入提示
5. **回答生成** - LLM 根據上下文生成答案

這讓模型的回答以你的真實資料為依據，而不僅靠訓練知識或憑空猜測。

## 先決條件

- 完成 [Module 00 - 快速開始](../00-quick-start/README.md)（針對上面提及的 Easy RAG 範例）
- 完成 [Module 01 - 概論](../01-introduction/README.md)（部署 Azure OpenAI 資源，包括 `text-embedding-3-small` 嵌入模型）
- 根目錄下有包含 Azure 憑證的 `.env` 檔案（由 Module 01 中的 `azd up` 建立）

> **注意：** 若尚未完成 Module 01，請先依照該模組的部署指示操作。`azd up` 指令會同時部署 GPT 聊天模型及本模組使用的嵌入模型。

## 理解 RAG

下圖說明核心概念：RAG 不只依賴模型訓練資料，而是給模型一本你的文件參考庫，以便在每次生成答案前先查閱。

<img src="../../../translated_images/zh-TW/what-is-rag.1f9005d44b07f2d8.webp" alt="什麼是 RAG" width="800"/>

*此圖展示標準 LLM（僅依背景訓練做推測）與 RAG 增強 LLM（先查閱文件再回覆）之間的差別。*

以下展示各部件如何串接，使用者的提問透過四個階段流動 —— 嵌入、向量搜索、組合上下文與回答生成，每階段建立在前一階段：

<img src="../../../translated_images/zh-TW/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG 架構" width="800"/>

*此圖示範完整 RAG 流程——使用者提問經過嵌入、向量搜索、組合上下文及回答生成階段。*

本模組後面的內容會詳細說明每個階段，並附上可執行與修改的程式碼。

### 本教學使用哪種 RAG 方法？

LangChain4j 提供三種 RAG 實作方式，抽象程度各異。下圖並排比較它們：

<img src="../../../translated_images/zh-TW/rag-approaches.5b97fdcc626f1447.webp" alt="LangChain4j 中的三種 RAG 方法" width="800"/>

*此圖比較了 LangChain4j 三種 RAG 方法——Easy、Native 與 Advanced——展示各自組成及適用情況。*

| 方法 | 功能說明 | 取捨 |
|---|---|---|
| **Easy RAG** | 透過 `AiServices` 和 `ContentRetriever` 自動組線。你只需註解介面，附加檢索器，LangChain4j 便處理嵌入、搜尋與提示組合。 | 需寫最少程式碼，但不見各階段細節。 |
| **Native RAG** | 你自行呼叫嵌入模型、搜尋儲存庫、組合提示並生成答案——每個步驟明確呈現。 | 程式較多，但可看見並修改每一階段。 |
| **Advanced RAG** | 採用 `RetrievalAugmentor` 框架，可插拔查詢轉換器、路由器、重排序器及內容注入器，適合生產級管線。 | 彈性最高，但實作較複雜。 |

**本教學採用 Native 方法。** RAG 管線的各階段 —— 問句嵌入、向量儲存庫搜尋、上下文組合、回答生成 —— 都明確寫在 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)。這是有意爲之：作為學習材料，看到並理解每個階段比簡化程式碼更重要。一旦熟悉整體流程，你可以轉用 Easy RAG 來快速雛型開發，或者用 Advanced RAG 進行生產系統。

> **💡 已用過 Easy RAG？** [快速開始模組](../00-quick-start/README.md)包含 Document 問答範例（[`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)），採用 Easy RAG —— LangChain4j 會自動負責嵌入、搜尋與提示組合。本模組則打開這條管線，讓你親自操作並理解各階段。

<img src="../../../translated_images/zh-TW/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG 管線 - LangChain4j" width="800"/>

*此圖示範來自 `SimpleReaderDemo.java` 的 Easy RAG 管線。與本模組採用的 Native 方法比較：Easy RAG 將嵌入、檢索及提示組合隱藏於 `AiServices` 和 `ContentRetriever` 後端 —— 你只要匯入文件、指定檢索器，即可直接獲得答案。Native 方法則拆開管線，讓你自己呼叫嵌入、搜尋、組合與生成階段，獲得完全可視化與控制權。*

## 運作原理

本模組的 RAG 管線分為四階段，每當使用者提問便依序執行。首先，上傳的文件會被**解析與拆分成塊**，成為模型上下文視窗能輕鬆處理的尺寸。這些塊會略微重疊，以免在邊界遺失上下文。

接著，塊會被轉成**向量嵌入**並存入儲存庫，以便數學比對。收到查詢後，系統執行**語義搜尋**找出最相關的內容，最後將這些內容作為上下文傳給 LLM，進行**答案生成**。以下各節搭配程式碼與圖示逐步說明，每步驟詳細看起來如下。

### 文件處理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

上傳文件時，系統會解析（PDF 或純文本）、附加檔名等元數據，然後拆成較小的塊——方便模型上下文視窗存放。這些塊會略微重疊，避免在界線處遺失重要上下文。

```java
// 解析上傳的檔案並包裝成 LangChain4j 文件
Document document = Document.from(content, metadata);

// 分割成每段 300 個標記，重疊 30 個標記
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

下圖顯示視覺上的拆解流程。注意每個塊與相鄰塊之間有些許重疊——重疊 30 個 token 確保不遺漏重要上下文：

<img src="../../../translated_images/zh-TW/document-chunking.a5df1dd1383431ed.webp" alt="文件拆塊" width="800"/>

*此圖示範將文件拆分成 300 token 的塊，每塊間有 30 token 的重疊，保留邊界上下文。*

> **🤖 試試用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 打開 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) 並提問：
> - 「LangChain4j 如何拆文件成塊？為何重疊重要？」
> - 「不同文件類型最佳的塊大小是多少？原因是什麼？」
> - 「如何處理多語言或特殊格式的文件？」

### 創建嵌入向量

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每個塊會轉成名為嵌入向量的數字表示——簡言之，是將語意轉成數字的工具。嵌入模型不像聊天模型那樣具備「智慧」，它不能理解指令、推理或答問。它能做的是將文字映射到一個數學空間，令相近意思的詞語彼此靠近——像「car」和「automobile」相近，「refund policy」和「return my money」相近。你可以把聊天模型想成一個能交談的人，嵌入模型則是超級優秀的資料歸檔系統。

<img src="../../../translated_images/zh-TW/embedding-model-concept.90760790c336a705.webp" alt="嵌入模型概念" width="800"/>

*此圖示範嵌入模型如何將文字轉成數字向量，使語意相近詞彙（如「car」與「automobile」）在向量空間中彼此靠近。*

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

下圖的類別圖展示 RAG 管線中兩條流程以及 LangChain4j 對應類別。在**資料攝取流程**（上傳時執行一次），文件被拆分、嵌入、並透過 `.addAll()` 儲存。**查詢流程**（每次用戶提問執行）會將問題嵌入向量、透過 `.search()` 搜尋儲存庫，並將匹配結果作為上下文傳給聊天模型。兩者共用 `EmbeddingStore<TextSegment>` 介面連結：

<img src="../../../translated_images/zh-TW/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG 類別" width="800"/>

*此圖示範 RAG 管線的兩條流程——資料攝取與查詢——如何透過共享的 EmbeddingStore 介面連結。*

嵌入向量儲存完成後，類似內容會自然聚在向量空間附近。下圖展示有關聯題材的文件點群靠得很近，使語義搜尋成為可能：

<img src="../../../translated_images/zh-TW/vector-embeddings.2ef7bdddac79a327.webp" alt="向量嵌入空間" width="800"/>

*此圖顯示具有相似語意的文件在三維向量空間中聚集，技術文件、商業規則和常見問題各自形成群組。*

使用者搜尋時，系統會依序進行四步：一次性嵌入文件、每次搜尋嵌入查詢、使用餘弦相似度比較查詢向量與所有文件向量，最後回傳分數最高的 K 個片段。下圖解說每步及其 LangChain4j 類別：

<img src="../../../translated_images/zh-TW/embedding-search-steps.f54c907b3c5b4332.webp" alt="嵌入搜尋步驟" width="800"/>

*此圖展示四步嵌入搜尋程序：嵌入文件、嵌入查詢、使用餘弦相似度比較向量、回傳前 K 名結果。*

### 語義搜尋

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

當你提問時，問題也會被嵌入成向量。系統比較這個問題嵌入與文件片段的嵌入，找出語意最接近的內容──不僅是關鍵字匹配，更是深入意義上的相似。

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

下圖對比語義搜尋與傳統關鍵字搜尋。關鍵字搜尋「vehicle」會錯過提到「cars and trucks」的片段，但語義搜尋能理解它們意思相同，並回傳高分結果：

<img src="../../../translated_images/zh-TW/semantic-search.6b790f21c86b849d.webp" alt="語義搜尋" width="800"/>

*此圖比對基於關鍵字搜尋和語義搜尋，展示語義搜尋即便沒有確切關鍵字也能找出相關概念的內容。*

背後使用的相似度指標是餘弦相似度 —— 可視為「這兩條箭頭指向的方向是否相同？」兩個片段可能用詞全然不同，但若其向量指向相同方向，分數會接近 1.0：

<img src="../../../translated_images/zh-TW/cosine-similarity.9baeaf3fc3336abb.webp" alt="餘弦相似度" width="800"/>
*此圖示說明餘弦相似度作為嵌入向量之間的角度 — 越對齊的向量分數越接近 1.0，代表語義相似度越高。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 嘗試：** 開啟 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 並詢問：
> - 「相似性搜尋如何與嵌入一起運作，分數如何決定？」
> - 「我應該使用什麼相似度門檻，以及它如何影響結果？」
> - 「如何處理找不到相關文件的情況？」

### 答案生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

最相關的區塊會被組裝進一個結構化的 prompt，包含明確指令、檢索到的上下文與使用者問題。模型會閱讀這些特定區塊並根據這些資訊回覆 — 它只能使用呈現在眼前的內容，避免產生幻覺。

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

下圖展示這個組裝過程 — 來自搜尋步驟的最高分區塊被注入到 prompt 範本，並由 `OpenAiOfficialChatModel` 產生一個有依據的答案：

<img src="../../../translated_images/zh-TW/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*此圖展示如何將最高分的區塊組裝成結構化 prompt，使模型能從你的數據生成有依據的答案。*

## 執行應用程式

**確認部署：**

確保根目錄存在 `.env` 檔案且包含 Azure 資格證書（在模組 01 創建）：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 如果你已在模組 01 使用 `./start-all.sh` 啟動所有應用程式，本模組已在 8081 埠運行。你可以跳過以下啟動指令，直接造訪 http://localhost:8081。

**選項 1：使用 Spring Boot 儀表板（推薦給 VS Code 用戶）**

開發容器包含 Spring Boot 儀表板擴充，提供視覺介面管理所有 Spring Boot 應用程式。你可在 VS Code 左側活動欄找到它（尋找 Spring Boot 圖示）。

透過 Spring Boot 儀表板，你可以：
- 查看工作區內所有 Spring Boot 應用程式
- 一鍵啟動/停止應用程式
- 即時檢視應用程式日誌
- 監控應用狀態

只要點擊「rag」旁的播放按鈕即可啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*此截圖展示 VS Code 中的 Spring Boot 儀表板，提供啟動、停止與監控應用程式的視覺介面。*

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

或僅啟動本模組：

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

這兩個腳本會自動從根目錄 `.env` 檔案載入環境變數，且若 JAR 檔不存在會自動建置。

> **注意：** 若你想先手動建置所有模組再啟動：
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
./stop.sh  # 僅限此模組
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

此應用程式提供網頁介面用於文件上傳與提問。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-TW/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖展示 RAG 應用程式介面，您可以上傳文件並提出問題。*

### 上傳文件

先上傳文件 — TXT 檔用於測試最合適。本目錄有 `sample-document.txt`，包含 LangChain4j 功能、RAG 實作與最佳實踐資訊，非常適合系統測試。

系統會處理你的文件，拆分成多個區塊，並為每個區塊建立嵌入。這會在上傳時自動完成。

### 提問

接著針對文件內容提出具體問題。試問一些文件中明確陳述的事實。系統會搜尋相關區塊，納入 prompt，並產生答案。

### 查看來源參考

注意每個答案都附有來源參考及相似度分數。這些分數（0 到 1）顯示每個區塊與你的問題相關程度，分數越高表示匹配越好。這讓你能對照來源資料驗證答案。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-TW/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*此截圖展示查詢結果，含生成的答案、來源參考以及每個檢索區塊的相關度分數。*

### 問題實驗

嘗試不同類型問題：
- 具體事實：「主要主題是什麼？」
- 比較：「X 跟 Y 有什麼差異？」
- 摘要：「請總結關於 Z 的重點」

觀察相似度分數如何依據問題是否匹配文件內容而變化。

## 關鍵概念

### 區塊切分策略

文件被拆成 300 代幣的區塊，且重疊 30 個代幣。此平衡確保每個區塊都有足夠上下文且保持尺寸適中，能在 prompt 中包含多個區塊。

### 相似度分數

每個檢索出的區塊都帶有介於 0 到 1 的相似度分數，表示其與用戶問題的匹配程度。下圖視覺化了分數範圍與系統如何利用它們過濾結果：

<img src="../../../translated_images/zh-TW/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*此圖示顯示分數範圍從 0 到 1，設定 0.5 最低門檻過濾不相關區塊。*

分數範圍：
- 0.7-1.0：高度相關，精確匹配
- 0.5-0.7：相關，具良好上下文
- 低於 0.5：被過濾，不相似

系統只檢索高於最低門檻的區塊以確保品質。

嵌入在語義群聚良好時表現不錯，但也會有盲點。下圖展示常見失敗模式 — 區塊過大產生模糊向量、過小缺乏上下文、模糊詞彙指向多個群聚，以及像 ID、零件號的精確匹配查詢嵌入無法處理：

<img src="../../../translated_images/zh-TW/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*此圖示展示常見嵌入失敗模式：區塊過大、過小、指向多個群聚的模糊詞彙，以及像 ID 的精確匹配查詢。*

### 記憶體儲存

本模組為簡便採用記憶體儲存。重新啟動應用程式時，上傳的文件會遺失。生產環境會用持久化向量資料庫如 Qdrant 或 Azure AI Search。

### 上下文視窗管理

每個模型有最大上下文視窗限制。無法包含大型文件的每個區塊。系統檢索前 N 個最相關區塊（預設 5 個）以保持限制內同時提供足夠上下文作答。

## 何時使用 RAG

RAG 並非一直是正確方案。以下決策指南說明何時在 RAG 有效價值，何時只用簡單方法即可 — 例如直接將內容寫入 prompt 或依賴模型內建知識：

<img src="../../../translated_images/zh-TW/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*此圖示為何時 RAG 有價值與何時簡化方法足夠的決策指南。*

**使用 RAG 的時機：**
- 回答專有文件相關問題
- 資訊經常變動（政策、價格、規格）
- 需要準確帶來源引用
- 內容太大無法放進單一 prompt
- 需要可驗證、依據資料的回應

**不使用 RAG 的時機：**
- 問題依賴模型已知的一般知識
- 需要即時資料（RAG 運作於已上傳文件）
- 內容足夠小可直接放入 prompt

## 下一步

**下一模組：** [04-tools - 搭配工具的 AI 代理](../04-tools/README.md)

---

**導覽：** [← 上一個：模組 02 - Prompt 工程](../02-prompt-engineering/README.md) | [回主頁](../README.md) | [下一個：模組 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於重要資訊，建議使用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->