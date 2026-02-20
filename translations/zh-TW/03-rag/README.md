# 第二部分：RAG（檢索增強生成）

## 目錄

- [您將學到什麼](../../../03-rag)
- [理解 RAG](../../../03-rag)
- [先決條件](../../../03-rag)
- [運作方式](../../../03-rag)
  - [文件處理](../../../03-rag)
  - [創建嵌入](../../../03-rag)
  - [語意搜尋](../../../03-rag)
  - [回答生成](../../../03-rag)
- [執行應用程式](../../../03-rag)
- [使用應用程式](../../../03-rag)
  - [上傳文件](../../../03-rag)
  - [提問](../../../03-rag)
  - [檢查來源參考](../../../03-rag)
  - [嘗試不同問題](../../../03-rag)
- [核心概念](../../../03-rag)
  - [切塊策略](../../../03-rag)
  - [相似度分數](../../../03-rag)
  - [記憶體內存儲](../../../03-rag)
  - [上下文視窗管理](../../../03-rag)
- [何時使用 RAG](../../../03-rag)
- [下一步](../../../03-rag)

## 您將學到什麼

在前幾個單元中，您學會了如何與 AI 對話並有效地組織提示，但有一個根本限制：語言模型只知道它在訓練中學到的內容，無法回答關於您公司政策、專案文件或未曾訓練過的資訊。

RAG（檢索增強生成）解決了這個問題。它不是試圖教模型您的資訊（這既昂貴又不切實際），而是給它搜尋您文件的能力。當有人提問時，系統會找到相關資訊並將其包含到提示中，模型則根據檢索到的上下文來回答。

可以把 RAG 想成給模型一個參考圖書館。當您提問時，系統：

1. **使用者提問** — 您提出一個問題
2. **嵌入生成** — 將您的問題轉為向量
3. **向量搜尋** — 找相似的文件切塊
4. **上下文組合** — 將相關切塊加入提示
5. **回答生成** — LLM 根據上下文生成答案

這讓模型的回答依據您的實際資料，而不是僅依賴訓練知識或憑空捏造答案。

## 理解 RAG

以下圖示說明核心概念：RAG 並非只依賴模型的訓練數據，而是讓它在產生每個答案之前，能先參考您文件庫的資訊。

<img src="../../../translated_images/zh-TW/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

流程從使用者問題開始，經過四個階段——嵌入、向量搜尋、上下文組合和答案生成——每階段皆基於前階段建立：

<img src="../../../translated_images/zh-TW/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

本單元將深入說明每個階段，附上可執行且可修改的程式碼。

## 先決條件

- 已完成第一部分（已部署 Azure OpenAI 資源）
- 根目錄包含 `.env` 檔並有 Azure 憑證（由第一部分的 `azd up` 建立）

> **注意：** 若尚未完成第一部分，請先依照該部分部署指示操作。

## 運作方式

### 文件處理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

上傳文件時，系統會解析文件（PDF 或純文字）、附加元資料如檔案名稱，然後將文件拆分成多個切塊——較小的片段，能舒適地放入模型的上下文視窗。這些切塊會有些許重疊，以免在邊界處遺失上下文。

```java
// 解析上傳的檔案並包裝成 LangChain4j 文件
Document document = Document.from(content, metadata);

// 拆分成300個標記的區塊，重疊30個標記
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
下圖展示此處理視覺化過程。注意每個切塊與相鄰切塊共享約 30 個標記的重疊，確保沒有重要上下文被遺漏：

<img src="../../../translated_images/zh-TW/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 打開 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) 並詢問：
> - “LangChain4j 如何將文件切成切塊？為什麼重疊重要？”
> - “不同文件類型的最佳切塊大小是多少？為什麼？”
> - “如何處理多語言或特殊格式的文件？”

### 創建嵌入

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每個切塊會被轉換成數值表示，稱為嵌入——本質上是一種捕捉文本含義的數學指紋。相似的文本會產生相似的嵌入。

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
  
以下類別圖顯示 LangChain4j 如何組合這些元件。`OpenAiOfficialEmbeddingModel` 負責將文本轉為向量，`InMemoryEmbeddingStore` 儲存向量及其原始 `TextSegment` 資料，`EmbeddingSearchRequest` 控制檢索參數，如 `maxResults` 與 `minScore`：

<img src="../../../translated_images/zh-TW/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

嵌入儲存後，內容相似的向量會自動在空間中聚集。下圖顯示相關主題文件會成為鄰近點，這就是語意搜尋的基礎：

<img src="../../../translated_images/zh-TW/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### 語意搜尋

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

提問時，您的問題也會轉成嵌入，系統將它與所有文件切塊的嵌入做比較，找出意義最相似的切塊——不是單純關鍵字匹配，而是實際語意相符。

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
  
下圖將語意搜尋與傳統關鍵字搜尋對比。關鍵字搜尋「vehicle」會漏掉關於「cars and trucks」的切塊，而語意搜尋理解它們的同義意思，並將其作為高分匹配結果返回：

<img src="../../../translated_images/zh-TW/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試：** 打開 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 並詢問：
> - “相似度搜尋如何利用嵌入運作？分數如何決定？”
> - “應該設定多少相似度閾值？它如何影響結果？”
> - “找不到相關文件時該怎麼處理？”

### 回答生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

將最相關的切塊組合成結構化提示，內含明確指令、檢索到的上下文與使用者提問。模型僅依據這些切塊資訊回答，有效防止虛構答案。

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
  
下圖展示此組合過程——從搜尋步驟得到最高分的切塊被帶入提示範本，`OpenAiOfficialChatModel` 依此產生有根據的答案：

<img src="../../../translated_images/zh-TW/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## 執行應用程式

**確認部署：**

確認根目錄有 `.env` 檔並包含 Azure 憑證（前面第一部分部署時創建）：  
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```


**啟動應用程式：**

> **注意：** 若您透過第一部分的 `./start-all.sh` 已啟動全部應用，本部分已在 8081 埠運行，可直接跳過以下啟動指令，直接訪問 http://localhost:8081 。

**方法一：使用 Spring Boot Dashboard（建議 VS Code 用戶）**

開發容器內含 Spring Boot Dashboard 擴充套件，提供視覺化介面管理所有 Spring Boot 應用。您可在 VS Code 左側活動欄找到 Spring Boot 圖示。

在 Spring Boot Dashboard 中，您可以：
- 瀏覽工作區內所有 Spring Boot 應用
- 一鍵啟動/停止應用
- 實時查看應用日誌
- 監控應用狀態

點擊 "rag" 旁的播放按鈕啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-TW/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**方法二：使用指令腳本**

啟動所有網頁應用（1-4 模組）：

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


這兩個腳本會自動從根目錄 `.env` 載入環境變量，若 JAR 檔不存在也會自動編譯。

> **提示：** 若想先手動編譯所有模組再啟動：
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


在瀏覽器打開 http://localhost:8081 。

**停止應用：**

**Bash:**  
```bash
./stop.sh  # 僅限此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # 僅限此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```


## 使用應用程式

此應用提供網頁介面，支援文件上傳與提問。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-TW/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG 應用介面 — 上傳文件並提問*

### 上傳文件

從上傳文件開始 — 建議使用 TXT 檔測試。本目錄附有 `sample-document.txt`，包含 LangChain4j 功能、RAG 實作與最佳實務相關資訊，非常適合測試系統。

系統將自動處理文件、切塊並為每個切塊創建嵌入。

### 提問

針對文件內容提問明確且具體的問題。系統會搜尋相關切塊，將其納入提示，並生成答案。

### 檢查來源參考

每個回答都會附帶來源參考及相似度分數。這些分數（0 到 1）表示每個切塊與問題的相關程度，分數越高表示匹配越好。這讓您可對答案與來源材料進行核對。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-TW/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*查詢結果顯示答案及來源參考與關聯分數*

### 嘗試不同問題

嘗試以下不同類型問題：
- 具體事實：「主要主題是什麼？」
- 比較：「X 和 Y 的差別是什麼？」
- 摘要：「請總結 Z 的重點」

觀察關聯分數如何根據問題與文件內容的符合度變化。

## 核心概念

### 切塊策略

文件被拆成 300 個標記的切塊，且彼此重疊 30 個標記。此平衡確保每個切塊有足夠上下文且切塊夠小，方便在提示中包含多個切塊。

### 相似度分數

每個檢索到的切塊都帶有介於 0 到 1 的相似度分數，表示其與使用者問題的匹配程度。下圖視覺化分數範圍及系統如何用它們過濾結果：

<img src="../../../translated_images/zh-TW/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

分數範圍如下：
- 0.7-1.0：高度相關，完全匹配
- 0.5-0.7：相關，具良好上下文
- 低於 0.5：已過濾掉，差異過大

系統只會檢索高於閾值的切塊以確保品質。

### 記憶體內存儲

本模組為簡便起見採用記憶體內存儲，應用重啟後已上傳的文件會遺失。生產系統會使用持久化向量資料庫，如 Qdrant 或 Azure AI Search。

### 上下文視窗管理

每個模型皆有限制最大上下文視窗大小，無法塞入大型文件的所有切塊。系統會檢索最相關的 N 個切塊（預設 5 個），在保持限制內同時提供充足上下文以產生準確答案。

## 何時使用 RAG

RAG 並非所有情境都適用。下面決策指南協助您判斷何時 RAG 有助益，何時較簡單方法（如直接將內容放入提示或依賴模型內建知識）足夠：

<img src="../../../translated_images/zh-TW/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**使用 RAG 的時機：**
- 回答關於專有文件的問題
- 資訊經常變動（政策、價格、規格）
- 準確性需要來源引用
- 內容過大無法放入單一提示中
- 需要可驗證、有根據的回應

**以下情況不適合使用 RAG：**
- 問題需要模型已具備的一般知識
- 需要即時數據（RAG 適用於上傳的文件）
- 內容足夠小，可以直接包含在提示中

## 下一步

**下一模組：** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**導覽：** [← 上一頁：模組 02 - 提示工程](../02-prompt-engineering/README.md) | [回主頁](../README.md) | [下一頁：模組 04 - 工具 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我們力求準確，但請注意自動翻譯可能包含錯誤或不精確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->