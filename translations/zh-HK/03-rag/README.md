# Module 03: RAG (檢索增強生成)

## 目錄

- [你將學習什麼](../../../03-rag)
- [認識 RAG](../../../03-rag)
- [先決條件](../../../03-rag)
- [運作原理](../../../03-rag)
  - [文件處理](../../../03-rag)
  - [建立嵌入向量](../../../03-rag)
  - [語義搜尋](../../../03-rag)
  - [答案生成](../../../03-rag)
- [執行應用程式](../../../03-rag)
- [使用應用程式](../../../03-rag)
  - [上傳文件](../../../03-rag)
  - [提問](../../../03-rag)
  - [檢查來源參考](../../../03-rag)
  - [嘗試不同問題](../../../03-rag)
- [關鍵概念](../../../03-rag)
  - [分段策略](../../../03-rag)
  - [相似度分數](../../../03-rag)
  - [記憶體內儲存](../../../03-rag)
  - [上下文視窗管理](../../../03-rag)
- [何時使用 RAG](../../../03-rag)
- [後續步驟](../../../03-rag)

## 你將學習什麼

在前面的模組中，你學會了如何與 AI 進行對話並有效結構提示。但有一個根本限制：語言模型只能知道訓練時學到的內容。它們無法回答有關貴公司政策、專案文件或其他未被訓練內容的問題。

RAG（檢索增強生成）解決了這個問題。它不是嘗試教模型你的資訊（這既昂貴又不實際），而是給它能力去搜尋你的文件。當有人提問時，系統會找到相關資訊並加到提示中。模型根據檢索到的上下文來回答。

可以將 RAG 想像成給模型一座參考圖書館。當你提出問題，系統會：

1. **用戶查詢** – 你提出問題
2. **嵌入轉換** – 將問題轉換成向量
3. **向量搜尋** – 找出相似的文件片段
4. **上下文組裝** – 把相關片段加入提示中
5. **回應產生** – 大型語言模型根據上下文生成答案

這令模型的回答根植於你的實際資料，而不是靠訓練知識或胡亂編造答案。

## 認識 RAG

下圖展示核心概念：RAG 不僅依賴模型的訓練資料，而是給它參考你的文件庫，在每次生成答案前先進行查詢。

<img src="../../../translated_images/zh-HK/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

下方示意圖說明整個流程。用戶提問經過四個階段——嵌入轉換、向量搜尋、上下文組裝和答案生成——每個步驟都建立於前一個步驟之上：

<img src="../../../translated_images/zh-HK/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

本模組後續將詳細說明每個階段，並附上可運行和修改的程式碼。

## 先決條件

- 已完成 Module 01 （部署 Azure OpenAI 資源）
- 根目錄有 `.env` 檔案，內含 Azure 認證（由 Module 01 中 `azd up` 建立）

> **注意：** 若尚未完成 Module 01，請先依照那裡的部署說明進行。


## 運作原理

### 文件處理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

當你上傳文件時，系統會解析文件（PDF 或純文字），附加元資料如檔名，然後切割成多個片段——較小的部分以便切合模型的上下文視窗。這些片段會稍微重疊，避免在邊界遺失上下文。

```java
// 解析上載的檔案並包裝為 LangChain4j 文件
Document document = Document.from(content, metadata);

// 分割成有 30 個 Token 重疊的 300 Token 區塊
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

下圖視覺化說明此流程。請注意每個片段和鄰近片段共享一些標記詞 —— 30 個標記詞的重疊確保沒有重要上下文被割裂：

<img src="../../../translated_images/zh-HK/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 打開 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)，並詢問：
> -「LangChain4j 如何分割文件成片段，為何重疊很重要？」
> -「不同文件類型的最佳片段大小是多少？為什麼？」
> -「怎樣處理多語言或有特殊格式的文件？」

### 建立嵌入向量

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每個片段會被轉成一個數字表示，稱為嵌入向量——本質上是一種數學指紋，捕捉文字的意義。相似文字會產生相似的向量。

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

下圖類別圖展示這些 LangChain4j 元件的連接關係。`OpenAiOfficialEmbeddingModel` 把文字轉成向量，`InMemoryEmbeddingStore` 儲存向量及其原始 `TextSegment` 資料，`EmbeddingSearchRequest` 控制檢索參數，如 `maxResults` 和 `minScore`：

<img src="../../../translated_images/zh-HK/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

當嵌入向量存入後，相似內容自然會在向量空間中形成群集。下圖示範相關主題的文件會聚在一起，這正是語義搜索得以實現的基礎：

<img src="../../../translated_images/zh-HK/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### 語義搜尋

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

當你提出問題時，問題本身也會轉成嵌入向量。系統將問題向量與所有文件片段的向量做比較。它找到語意最相近的片段——不只是匹配關鍵字，而是實際語意相似。

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

下圖對比語義搜尋和傳統關鍵字搜尋：關鍵字搜尋查詢「vehicle（車輛）」時會漏掉「cars and trucks（汽車和卡車）」的片段，但語義搜尋理解它們相同意思，並把它列為高分匹配：

<img src="../../../translated_images/zh-HK/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 打開 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)，並詢問：
> -「相似度搜尋如何與嵌入向量配合運作？分數如何決定？」
> -「我應該用什麼相似度閾值？會對結果有什麼影響？」
> -「如果找不到相關文件怎麼辦？」

### 答案生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

系統將最相關的片段組合成一個結構化提示模板，包含明確指令、檢索上下文和用戶問題。模型只讀取這些特定片段並基於這些資訊回答 —— 它只能用眼前可見的內容，這防止了幻覺產生。

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

下圖展示此組裝過程——搜尋步驟中得分最高的片段注入至提示模板，`OpenAiOfficialChatModel` 生成有根據的答案：

<img src="../../../translated_images/zh-HK/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## 執行應用程式

**確認部署狀態：**

確定根目錄有 `.env` 檔（內含 Azure 認證，於 Module 01 建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若你已於 Module 01 使用 `./start-all.sh` 啟動所有應用程式，此模組已於 8081 端口運行。可跳過以下啟動指令，直接瀏覽 http://localhost:8081 。

**選擇 1：使用 Spring Boot Dashboard（推薦 VS Code 用戶）**

開發容器內含 Spring Boot Dashboard 擴充功能，提供視覺化介面管理所有 Spring Boot 應用。你可在 VS Code 左側活動欄找到 Spring Boot 圖示。

從 Spring Boot Dashboard 你可以：
- 查看工作區中所有 Spring Boot 應用
- 一鍵啟動/停止應用
- 實時檢視應用日誌
- 監控應用狀態

只需點擊 “rag” 旁的播放鍵啟動此模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**選擇 2：使用 shell 腳本**

啟動所有網頁應用（模組 01-04）：

**Bash:**
```bash
cd ..  # 從根目錄開始
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 從根目錄
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

這兩個腳本會自動從根目錄 `.env` 加載環境變數，且若缺少 JAR 檔會自動編譯。

> **提示：** 若你想先手動編譯所有模組再啟動：
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

瀏覽器打開 http://localhost:8081 。

**停止應用：**

**Bash:**
```bash
./stop.sh  # 僅此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只限呢個模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

此應用提供網頁介面供文件上傳與提問。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-HK/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG 應用介面 – 上傳文件並提問*

### 上傳文件

首先上傳文件 —— 測試時 TXT 檔效果最佳。本目錄有一個 `sample-document.txt`，內含 LangChain4j 功能、RAG 實作及最佳實踐資訊，非常適合測試系統。

系統會處理你的文件，切割成片段，再為每個片段建立嵌入向量。這個過程於上傳時自動進行。

### 提問

現在可以針對文件內容提出具體問題。試問一些文件中清楚說明的事實。系統搜尋相關片段，加入提示，再生成答案。

### 檢查來源參考

請注意每個答案伴有來源參考與相似度分數。該分數範圍為 0 至 1，顯示每個片段與問題的相關程度。分數越高意味匹配越好。這幫助你核對答案是否符合來源資料。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-HK/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*查詢結果，展示答案及來源參考與相關度分數*

### 嘗試不同問題

試著問不同類型問題：
- 具體事實：「主要主題是什麼？」
- 比較問題：「X 與 Y 有何不同？」
- 摘要問題：「請總結 Z 的要點」

觀察相關度分數如何依問題與文件匹配度而變化。

## 關鍵概念

### 分段策略

文件被切割成長度約 300 個標記詞的片段，且每片段帶有 30 個標記詞的重疊。這種平衡確保每片段背景資訊足夠有意義，且大小適中，可在提示中包含多個片段。

### 相似度分數

每個被檢索出的片段會帶有一個介於 0 與 1 之間的相似度分數，顯示它與用戶問題的吻合程度。下圖視覺化分數區間與系統如何利用它們過濾結果：

<img src="../../../translated_images/zh-HK/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

分數範圍：
- 0.7-1.0：高度相關，精確匹配
- 0.5-0.7：相關，背景良好
- 低於 0.5：被篩選，過於不相似

系統只擷取高於最低閾值的片段，以確保品質。

### 記憶體內儲存

本模組為簡化開發，使用記憶體內儲存。重新啟動應用時，上傳的文件會消失。正式系統會使用持久化向量資料庫，如 Qdrant 或 Azure AI Search。

### 上下文視窗管理

每個模型有最大上下文視窗限制。無法把大文件所有片段都包含進去。系統會選擇相關度最高的前 N 個片段（預設為 5），在限制內提供足夠的上下文，以確保回答精確。

## 何時使用 RAG

RAG 並非總是最佳方案。下方判斷指南幫助你判斷何時 RAG 可增值，以及何時簡單做法足夠，例如將內容直接加入提示或依賴模型本身知識：

<img src="../../../translated_images/zh-HK/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**當下列情況時，使用 RAG:**
- 回答有關專有文件的問題
- 資訊經常變動（政策、價格、規格）
- 準確度需要來源引用
- 內容太多無法在單一提示中包含
- 你需要可驗證、有根據的回應

**不建議使用 RAG 的情況：**
- 問題需要模型已具備的一般知識
- 需要實時數據（RAG 基於上傳文件運作）
- 內容足夠小可以直接包含在提示中

## 下一步

**下一單元：** [04-tools - 使用工具的 AI 代理](../04-tools/README.md)

---

**導覽：** [← 上一章：單元 02 - 提示工程](../02-prompt-engineering/README.md) | [返回主頁](../README.md) | [下一章：單元 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件是使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯的。儘管我們力求準確，請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。如涉及關鍵信息，建議尋求專業人工翻譯。我們不對因使用此翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->