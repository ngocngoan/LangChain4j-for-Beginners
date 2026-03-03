# Module 04: 使用工具的 AI 代理

## 目錄

- [你將學到什麼](../../../04-tools)
- [先決條件](../../../04-tools)
- [了解使用工具的 AI 代理](../../../04-tools)
- [工具調用如何運作](../../../04-tools)
  - [工具定義](../../../04-tools)
  - [決策](../../../04-tools)
  - [執行](../../../04-tools)
  - [回應生成](../../../04-tools)
  - [架構：Spring Boot 自動連線](../../../04-tools)
- [工具串聯](../../../04-tools)
- [運行應用程式](../../../04-tools)
- [使用應用程式](../../../04-tools)
  - [嘗試簡單工具使用](../../../04-tools)
  - [測試工具串聯](../../../04-tools)
  - [查看對話流程](../../../04-tools)
  - [嘗試不同請求](../../../04-tools)
- [關鍵概念](../../../04-tools)
  - [ReAct 模式（推理與行動）](../../../04-tools)
  - [工具描述的重要性](../../../04-tools)
  - [會話管理](../../../04-tools)
  - [錯誤處理](../../../04-tools)
- [可用工具](../../../04-tools)
- [何時使用基於工具的代理](../../../04-tools)
- [工具與 RAG 比較](../../../04-tools)
- [下一步](../../../04-tools)

## 你將學到什麼

到目前為止，你已學會如何與 AI 進行對話、有效結構化提示語，並根據你的文件去基礎回應。但仍存在一個根本限制：語言模型只能產生文本。它們無法檢查天氣、執行計算、查詢資料庫或與外部系統互動。

工具改變了這點。透過讓模型能夠呼叫函數，你將它從一個文字生成器轉變為可以採取行動的代理。模型決定何時需要工具、使用哪個工具以及傳遞什麼參數。你的程式碼執行函數並返回結果。模型將該結果融入回應中。

## 先決條件

- 完成 [Module 01 - Introduction](../01-introduction/README.md) （已部署 Azure OpenAI 資源）
- 建議完成前幾個模組（本模組在工具與 RAG 比較中使用了 [Module 03 的 RAG 概念](../03-rag/README.md)）
- 根目錄有 `.env` 文件包含 Azure 認證（由 Module 01 的 `azd up` 建立）

> **注意：** 若未完成 Module 01，請先跟隨該模組的部署說明。

## 了解使用工具的 AI 代理

> **📝 注意：** 本模組中「代理」一詞是指具備工具呼叫功能的 AI 助理。這與我們將在 [Module 05: MCP](../05-mcp/README.md) 探討的 **Agentic AI** 模式（具備規劃、記憶和多步推理的自主代理）不同。

沒有工具時，語言模型只能基於訓練資料生成文本。問它當前天氣，它只能猜測。給它工具，它就能呼叫天氣 API、執行計算或查詢資料庫，然後將真實結果融入回應。

<img src="../../../translated_images/zh-MO/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*沒有工具時模型只能猜測 — 使用工具後可以呼叫 API、執行計算並回傳即時數據。*

具工具的 AI 代理遵循一種 **推理與行動（ReAct）** 模式。模型不只是回應 — 它會思考所需資訊，透過呼叫工具採取行動，觀察結果，然後決定是否再次行動或給出最終答案：

1. **推理** — 代理分析使用者問題，判斷所需資訊
2. **行動** — 選擇正確工具，產生正確參數並呼叫
3. **觀察** — 收到工具的輸出並評估結果
4. **重複或回應** — 若需要更多資料，迴圈回第一步；否則產生自然語言答案

<img src="../../../translated_images/zh-MO/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct 循環 — 代理思考要做什麼，透過呼叫工具行動，再觀察結果並迴圈，直到提供最終答案。*

這是自動發生的。你定義工具及其描述，模型負責決策何時及如何使用它們。

## 工具調用如何運作

### 工具定義

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

你定義具明確描述和參數規範的函數。模型會在系統提示中看到這些描述並理解每個工具的功能。

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // 你的天氣查詢邏輯
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// 助理由 Spring Boot 自動連接：
// - ChatModel bean
// - 來自 @Component 類別的所有 @Tool 方法
// - 用於會話管理的 ChatMemoryProvider
```

下圖拆解每個註解，展示它們如何幫助 AI 瞭解何時呼叫工具與傳遞哪些參數：

<img src="../../../translated_images/zh-MO/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*工具定義結構 — @Tool 告訴 AI 何時使用，@P 描述各參數，@AiService 在啟動時線上連結全部。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java)，詢問：
> - 「如何整合像 OpenWeatherMap 這種真實天氣 API 取代模擬資料？」
> - 「什麼才是能協助 AI 正確使用的好工具描述？」
> - 「如何在工具實作中處理 API 錯誤與流量限制？」

### 決策

使用者問「西雅圖的天氣如何？」，模型不會隨機挑工具。它會將使用者意圖與所有可用工具描述比對，對相關度打分並選擇最佳匹配，然後產生具正確參數的函數調用——此例中，將 `location` 設為 `"Seattle"`。

若無工具符合請求，模型則退回以自身知識回答。若多個工具符合，採用最具體者。

<img src="../../../translated_images/zh-MO/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*模型評估每個工具對使用者意圖的相關性並選出最佳匹配 — 這就是為何清晰、具體的工具描述很重要。*

### 執行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 自動線上連結所有註冊的 `@AiService` 介面與工具，LangChain4j 自動執行工具調用。背後，一個完整的工具調用從使用者自然語言問題到自然語言回答流經六個階段：

<img src="../../../translated_images/zh-MO/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*端到端流程 — 使用者提問，模型選擇工具，LangChain4j 執行工具，模型將結果編織成自然回應。*

如果你有執行過 Module 00 的 [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，已看過此模式 — `Calculator` 工具也是如此調用。下圖顯示該示範背後的序列流程：

<img src="../../../translated_images/zh-MO/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*快速入門示範的工具呼叫循環 — `AiServices` 將訊息與工具架構發給 LLM，LLM 回傳像 `add(42, 58)` 這樣的函數呼叫，LangChain4j 在本地執行 `Calculator` 方法，並回傳結果以給出最終答案。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)，詢問：
> - 「ReAct 模式如何運作且為何對 AI 代理有效？」
> - 「代理如何決定要使用哪個工具及順序？」
> - 「如果工具執行失敗要怎麼處理錯誤才穩健？」

### 回應生成

模型接收天氣資料並將其格式化為自然語言回應給使用者。

### 架構：Spring Boot 自動連線

本模組使用 LangChain4j 與 Spring Boot 整合的宣告式 `@AiService` 介面。啟動時，Spring Boot 發現所有帶有 `@Tool` 方法的 `@Component`、你的 `ChatModel` Bean 和 `ChatMemoryProvider`，然後將它們全部線上連結成一個 `Assistant` 介面，無需樣板碼。

<img src="../../../translated_images/zh-MO/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService 介面將 ChatModel、工具元件與記憶提供者串接 — Spring Boot 全自動處理連線。*

以下是完整的請求生命週期序列圖 — 從 HTTP 請求經過控制器、服務與自動線上代理，一直到工具執行與回傳：

<img src="../../../translated_images/zh-MO/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*完整的 Spring Boot 請求生命週期 — HTTP 請求流經控制器和服務到自動線上 Assistant 代理，該代理自動協調 LLM 和工具調用。*

此做法的主要優點：

- **Spring Boot 自動連線** — 自動注入 ChatModel 與工具
- **@MemoryId 模式** — 自動的基於會話的記憶管理
- **單一實例** — Assistant 建立一次後重複使用提升效能
- **型別安全執行** — 直接呼叫 Java 方法並自動轉換型別
- **多回合編排** — 自動處理工具串聯
- **零樣板碼** — 無需手動呼叫 `AiServices.builder()` 或使用記憶 HashMap

手動呼叫 `AiServices.builder()` 的替代方案需要更多程式碼，且缺少 Spring Boot 整合的便利。

## 工具串聯

**工具串聯** — 當單一問題需要多個工具時，基於工具的代理展現出真正威力。問「西雅圖的天氣幾華氏度？」時，代理會自動串鏈兩個工具：先呼叫 `getCurrentWeather` 取得攝氏溫度，再將該值傳給 `celsiusToFahrenheit` 轉換，全部在單一對話回合完成。

<img src="../../../translated_images/zh-MO/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*工具串聯實例 — 代理先呼叫 getCurrentWeather，然後將攝氏結果傳給 celsiusToFahrenheit，最後給出合併答覆。*

**優雅失敗** — 詢問不在模擬資料中的城市天氣時，工具會回傳錯誤訊息，AI 解釋無法提供協助而非崩潰。工具安全失敗。下圖對比兩種做法 — 適當錯誤處理時，代理捕捉例外並給予有用回應，否則整個應用崩潰：

<img src="../../../translated_images/zh-MO/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*當工具失敗時，代理捕捉錯誤並以有用解釋回應而非崩潰。*

這在單一對話回合中完成。代理自動編排多次工具呼叫。

## 運行應用程式

**確認部署：**

確保根目錄有 `.env` 文件包含 Azure 認證（在 Module 01 期間建立）。從本模組目錄（`04-tools/`）執行以下命令：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若你已從根目錄使用 `./start-all.sh` 啟動所有應用（如 Module 01 所述），則本模組已在 8084 端口運行，可跳過以下啟動指令，直接訪問 http://localhost:8084 。

**方案一：使用 Spring Boot Dashboard（推薦 VS Code 使用者）**

開發容器內含 Spring Boot Dashboard 擴展，它提供視覺化介面管理所有 Spring Boot 應用。你可於 VS Code 左側活動列找到（尋找 Spring Boot 圖示）。

你可從 Spring Boot Dashboard：
- 查看工作區內所有 Spring Boot 應用
- 一鍵啟動/停止應用
- 實時查看應用日誌
- 監控應用狀態

只需點擊 "tools" 旁的播放按鈕啟動本模組，或一次啟動所有模組。

以下為 VS Code 中 Spring Boot Dashboard 畫面：

<img src="../../../translated_images/zh-MO/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot Dashboard — 一處啟動、停止並監控所有模組*

**方案二：使用 Shell 腳本**

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

或者只啟動此模組：

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

這兩個腳本會自動從根目錄的 `.env` 檔案中載入環境變數，並且如果 JAR 檔案不存在，會自動編譯它們。

> **注意：** 如果你想在啟動前手動編譯所有模組：
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

在瀏覽器中打開 http://localhost:8084 。

**停止方法：**

**Bash:**
```bash
./stop.sh  # 僅此模組
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

此應用程式提供一個網頁介面，讓你與擁有天氣及溫度轉換工具的 AI 代理互動。界面如下所示 — 包含快速開始範例及用於發送請求的聊天面板:

<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-MO/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI 代理工具介面 — 快速範例及與工具互動的聊天介面*

### 試試簡單的工具使用

以簡單請求開始："將 100 華氏度轉換為攝氏度"。代理會辨識需要使用溫度轉換工具，並以正確參數呼叫它，最後回傳結果。你會發現這過程很自然 — 你不需要指定使用哪個工具或如何呼叫。

### 測試工具串鏈

接著嘗試更複雜的："西雅圖的天氣如何，並轉換為華氏度？" 觀察代理如何分步執行。它先取得天氣（回傳攝氏），知道需要轉換為華氏，呼叫溫度轉換工具，並將兩項結果結合成一個回應。

### 查看對話流程

聊天介面會保留對話歷史，讓你進行多輪互動。你可以看到所有先前的查詢與回應，方便追蹤對話並理解代理如何在多次交流中建立上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-MO/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多輪對話展示簡單轉換、天氣查詢和工具串鏈*

### 嘗試不同請求

嘗試各種組合：
- 天氣查詢："東京的天氣怎麼樣？"
- 溫度轉換："25°C 是多少開爾文？"
- 結合查詢："查一下巴黎的天氣，告訴我是否超過 20°C"

你會看到代理如何解讀自然語言並映射到適當的工具呼叫。

## 重要概念

### ReAct 模式（推理與行動）

代理會在推理（決定要做什麼）和行動（使用工具）間交替。這種模式讓它能自主解決問題，而不只是被動回應指令。

### 工具描述很重要

工具描述的品質直接影響代理使用它們的效果。清晰、具體的描述幫助模型判斷何時以及如何呼叫每個工具。

### 會話管理

`@MemoryId` 註解使得會話記憶管理自動化。每個會話 ID 都有自己獨立的 `ChatMemory` 實例，由 `ChatMemoryProvider` bean 管理，因此多個用戶可以同時和代理互動，彼此對話不會混淆。下圖說明了多用戶如何根據會話 ID 導向隔離的記憶庫：

<img src="../../../translated_images/zh-MO/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*每個會話 ID 都對應到獨立的對話歷史 — 用戶無法看到彼此的訊息。*

### 錯誤處理

工具可能失敗——API 超時、參數無效、外部服務中斷。生產環境代理需要錯誤處理機制，讓模型可以解釋問題或嘗試替代方案，而非整個應用崩潰。當工具拋出異常時，LangChain4j 會攔截並將錯誤訊息回傳給模型，模型接著會用自然語言解釋問題。

## 可用工具

下圖展示你可構建的工具生態系。這個模組示範天氣和溫度工具，但同樣的 `@Tool` 模式適用於任何 Java 方法 — 從資料庫查詢到支付處理。

<img src="../../../translated_images/zh-MO/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*任何用 @Tool 註解的 Java 方法都能給 AI 使用 — 這個模式可擴展到資料庫、API、電子郵件、檔案操作等。*

## 何時使用工具型代理

並非所有請求都需要工具。決定關鍵在於 AI 是否需要與外部系統交互，或能否靠自身知識回答。下圖總結何時工具有用，何時不必使用：

<img src="../../../translated_images/zh-MO/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*快速決策指南 — 工具用於即時資料、計算與操作；一般知識和創意任務不需要。*

## 工具與 RAG 之比較

模組 03 與 04 都擴充 AI 功能，但基本不同。RAG 讓模型能存取**知識**，透過文件檢索；工具則賦予模型執行**操作**的能力，像是呼叫函式。下圖並列這兩種方法，說明各自運作流程與取捨：

<img src="../../../translated_images/zh-MO/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG 從靜態文件中檢索資訊 — 工具執行動作並取用動態即時資料。許多生產系統會兩者兼用。*

實務上許多生產環境會結合兩者：RAG 用於以文件為基礎的答案支持，工具負責取得即時資料或執行操作。

## 下一步

**下一模組：** [05-mcp - 模型上下文協議 (MCP)](../05-mcp/README.md)

---

**導航：** [← 上一章：模組 03 - RAG](../03-rag/README.md) | [返回主頁](../README.md) | [下一章：模組 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件乃使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我們致力於翻譯準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件之母語版本應視為權威來源。對於重要資訊，建議採用專業人工翻譯。我們對於因使用本翻譯所引致之任何誤解或錯誤詮釋概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->