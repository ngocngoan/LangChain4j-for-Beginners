# Module 03: RAG（檢索增強生成）

## 目錄

- [你將學到什麼](../../../03-rag)
- [了解 RAG](../../../03-rag)
- [先決條件](../../../03-rag)
- [運作原理](../../../03-rag)
  - [文件處理](../../../03-rag)
  - [建立嵌入](../../../03-rag)
  - [語義搜尋](../../../03-rag)
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
  - [記憶體內儲存](../../../03-rag)
  - [上下文視窗管理](../../../03-rag)
- [何時使用 RAG](../../../03-rag)
- [下一步](../../../03-rag)

## 你將學到什麼

在之前的模組中，你學會了如何與 AI 對話以及如何有效構建提示。但有一個根本限制：語言模型只知道訓練期間所學的內容。它們無法回答關於你公司政策、專案文件或未經訓練的資訊。

RAG（檢索增強生成）解決了這個問題。它不是試圖教模型你的資訊（這既昂貴又不實際），而是賦予它搜尋你的文件的能力。當有人提出問題時，系統找到相關資訊並將其包含在提示中。模型基於這些檢索到的上下文給出答案。

想像 RAG 就像是給模型一本參考圖書館。當你提出問題時，系統會：

1. **使用者查詢** — 你提出問題
2. **嵌入** — 將問題轉換為向量
3. **向量搜尋** — 找到相似的文件切塊
4. **上下文組裝** — 將相關切塊加入提示
5. **回應** — LLM 根據上下文生成答案

這讓模型的回應立基於你的實際資料，而不是依賴訓練知識或臆造答案。

## 了解 RAG

下圖說明了核心概念：RAG 不僅仰賴模型的訓練資料，而是在生成每個答案前，賦予它可以查閱你文件的參考圖書館。

<img src="../../../translated_images/zh-MO/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

下面展示端到端的流程。一個使用者的問題經過四個階段——嵌入、向量搜尋、上下文組裝以及答案生成——每個階段都在前一階段基礎上構建：

<img src="../../../translated_images/zh-MO/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

本模組接下來會詳細介紹每個階段，並附上可操作和修改的程式碼。

## 先決條件

- 完成模組 01（部署 Azure OpenAI 資源）
- 根目錄下有 `.env` 檔案，並包含 Azure 認證（由模組 01 的 `azd up` 建立）

> **注意：** 如果你尚未完成模組 01，請先遵循那裡的部署指示。

## 運作原理

### 文件處理

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

當你上傳文件時，系統會解析它（PDF 或純文字），附加元資料如檔案名稱，然後將其拆成切塊——較小的段落，能適當放入模型的上下文視窗。這些切塊稍微重疊，避免邊界處遺失上下文。

```java
// 解析已上傳的檔案並包裝成 LangChain4j 文件
Document document = Document.from(content, metadata);

// 分割為 300 個字元的區塊，重疊 30 個字元
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
下圖視覺展示此流程。注意每個切塊和鄰近切塊共享部分 token——30 個 token 的重疊保證不會錯失重要上下文：

<img src="../../../translated_images/zh-MO/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：**  
> 開啟 [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) 並問：  
> - 「LangChain4j 如何切分文件成切塊，為什麼重疊很重要？」  
> - 「不同文件類型的最佳切塊大小是多少？為什麼？」  
> - 「如何處理多語言或特殊格式的文件？」

### 建立嵌入

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

每個切塊都會被轉成數值表示，稱為嵌入（embedding）——本質上是捕捉文字意義的數學指紋。相似文字會產生相似嵌入。

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
  
下圖類別圖展示這些 LangChain4j 組件如何連接。`OpenAiOfficialEmbeddingModel` 將文字轉成向量，`InMemoryEmbeddingStore` 儲存向量與原始 `TextSegment` 資料，`EmbeddingSearchRequest` 控制檢索參數如 `maxResults` 和 `minScore`：

<img src="../../../translated_images/zh-MO/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

嵌入存放後，類似內容就自然在向量空間中聚成簇。下方視覺化展示相關主題文件如何聚成鄰近點，這正是語義搜尋的基礎：

<img src="../../../translated_images/zh-MO/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### 語義搜尋

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

當你提出問題，問題也會被轉成嵌入。系統將你的問題嵌入與文件切塊嵌入進行比對。找到具有最高語義相似度的切塊——不只是關鍵詞匹配，而是實際語義上的相似。

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
  
下圖對比語義搜尋與傳統關鍵詞搜尋。搜尋「vehicle」的關鍵詞，會錯過提到「cars and trucks」的切塊，但語義搜尋明白它們相同意思，並將其列為高分匹配：

<img src="../../../translated_images/zh-MO/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 嘗試用 [GitHub Copilot](https://github.com/features/copilot) Chat：**  
> 開啟 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 並問：  
> - 「相似度搜尋如何運作？分數如何決定？」  
> - 「相似度閾值應該怎麼設？會怎樣影響結果？」  
> - 「找不到相關文件時怎麼處理？」

### 答案生成

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

系統將最相關的切塊組裝成結構化提示，包含明確指令、檢索到的上下文和使用者問題。模型基於這些切塊回答問題——只能用眼前的內容，避免幻覺（hallucination）。

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
  
下圖展示此上下文組裝過程——搜尋步驟中分數最高的切塊注入提示範本，`OpenAiOfficialChatModel` 根據上下文生成具根據性的回答：

<img src="../../../translated_images/zh-MO/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## 執行應用程式

**驗證部署：**

確保根目錄有 `.env`，並包含 Azure 認證（由模組 01 派生）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```
  
**啟動應用程式：**

> **注意：** 如果你從模組 01 已用 `./start-all.sh` 啟動所有應用程式，本模組已在 8081 埠執行。你可以跳過以下啟動指令，直接前往 http://localhost:8081。

**方案一：使用 Spring Boot 儀表板（推薦 VS Code 使用者）**

開發容器包含 Spring Boot 儀表板擴充功能，提供管理所有 Spring Boot 應用的視覺介面。可在 VS Code 左側活動欄找到（尋找 Spring Boot 圖示）。

透過 Spring Boot 儀表板，你可以：  
- 查看工作區所有 Spring Boot 應用  
- 單鍵啟動/停止應用  
- 即時查看應用日誌  
- 監控應用狀態  

點擊 "rag" 旁的播放按鈕啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**方案二：使用 shell 腳本**

啟動所有 Web 應用（模組 01-04）：

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
  
兩個腳本會自動從根 `.env` 載入環境變數，且如果 JAR 不存在會自動建置。

> **注意：** 若想手動建置所有模組再啟動：  
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
  
在瀏覽器開啟 http://localhost:8081 。

**停止應用：**

**Bash:**
```bash
./stop.sh  # 僅此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```
  
**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```
  
## 使用應用程式

此應用提供文件上傳及提問的網頁介面。

<a href="images/rag-homepage.png"><img src="../../../translated_images/zh-MO/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG 應用介面——上傳文件並提問*

### 上傳文件

先上傳一份文件——TXT 檔測試效果最佳。本目錄提供 `sample-document.txt`，其中包含 LangChain4j 功能、RAG 實作及最佳實踐說明，非常適合測試系統。

系統會處理你上傳的文件，拆成切塊，並為每個切塊建立嵌入。上傳時會自動完成這些步驟。

### 提問

現在，對文件內容提出具體問題。嘗試問文件中明確陳述的事實。系統會搜尋相關切塊，將其包含在提示中，並生成答案。

### 檢查來源參考

每個答案都會附帶來源參考及相似度分數。這些分數（0 到 1）顯示該切塊與你的問題相關程度。分數越高，匹配越好。你可以依此內容核對答案的來源。

<a href="images/rag-query-results.png"><img src="../../../translated_images/zh-MO/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*查詢結果顯示答案附帶來源參考和相關性分數*

### 嘗試不同問題

嘗試問不同類型的問題：  
- 具體事實：「主要主題是什麼？」  
- 比較問題：「X 與 Y 有何不同？」  
- 摘要：「請總結關於 Z 的重點」  

留意根據問題與文件內容的匹配度，相關性分數如何變化。

## 關鍵概念

### 切塊策略

文件拆為 300 token 的切塊，且相鄰切塊重疊 30 token。這平衡了每個切塊的上下文資訊豐富度，且切塊大小足夠小，可在一個提示中包含多個切塊。

### 相似度分數

每個檢索到的切塊都會附帶介於 0 到 1 的相似度分數，表示該切塊與使用者問題的匹配程度。下圖視覺化分數範圍及系統如何依此過濾結果：

<img src="../../../translated_images/zh-MO/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

分數範圍：  
- 0.7-1.0：高度相關、完全匹配  
- 0.5-0.7：相關，提供良好上下文  
- 低於 0.5：被過濾，太不相似  

系統僅檢索高於最低門檻的切塊，確保質量。

### 記憶體內儲存

本模組為簡便起見使用記憶體內儲存。重啟程式時，上傳的文檔資料會消失。生產系統會使用持久向量資料庫，如 Qdrant 或 Azure AI Search。

### 上下文視窗管理

每個模型有最大上下文視窗大小限制。無法放入巨量文檔的所有切塊。系統只取最相關的前 N 個切塊（預設為 5），兼顧限制與足夠上下文，產生精準回答。

## 何時使用 RAG

RAG 並非總是最佳方案。下圖協助你判斷何時 RAG 有價值，何時以簡單方法即可——如將內容直接包含在提示或依賴模型內建知識：

<img src="../../../translated_images/zh-MO/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**在以下情況下使用 RAG：**
- 回答有關專有文件的問題
- 資訊經常變更（政策、價格、規格）
- 準確性需要來源引用
- 內容太大，無法放入單一提示中
- 需要可驗證、有根據的回應

**以下情況不要使用 RAG：**
- 問題需要模型已具備的一般知識
- 需要即時數據（RAG 僅適用於上傳的文件）
- 內容足夠小，可直接包含在提示中

## 下一步

**下一單元：** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**導航：** [← 上一章：Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [返回主頁](../README.md) | [下一章：Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們致力於準確性，請注意自動翻譯可能包含錯誤或不準確之處。原始文件以其母語版本為權威來源。對於重要資訊，建議採用專業人工翻譯。我們對因使用本翻譯而引起的任何誤解或誤釋概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->