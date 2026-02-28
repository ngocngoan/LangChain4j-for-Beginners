# Module 04: 有工具的 AI 代理

## 目錄

- [你將學到什麼](../../../04-tools)
- [先決條件](../../../04-tools)
- [了解有工具的 AI 代理](../../../04-tools)
- [工具呼叫如何運作](../../../04-tools)
  - [工具定義](../../../04-tools)
  - [決策制定](../../../04-tools)
  - [執行](../../../04-tools)
  - [回應生成](../../../04-tools)
  - [架構：Spring Boot 自動繫結](../../../04-tools)
- [工具串聯](../../../04-tools)
- [執行應用程式](../../../04-tools)
- [使用應用程式](../../../04-tools)
  - [嘗試簡單的工具使用](../../../04-tools)
  - [測試工具串聯](../../../04-tools)
  - [查看對話流程](../../../04-tools)
  - [嘗試不同的請求](../../../04-tools)
- [關鍵概念](../../../04-tools)
  - [ReAct 模式（推理與行動）](../../../04-tools)
  - [工具描述的重要性](../../../04-tools)
  - [工作階段管理](../../../04-tools)
  - [錯誤處理](../../../04-tools)
- [可用的工具](../../../04-tools)
- [何時使用基於工具的代理](../../../04-tools)
- [工具 vs RAG](../../../04-tools)
- [後續步驟](../../../04-tools)

## 你將學到什麼

到目前為止，你已經學會如何與 AI 進行對話、有效地結構化提示語句，並使回答能夠依據你的文件產生。但仍有一個根本的限制：語言模型只能產生文字。它們無法查詢天氣、執行計算、查詢資料庫或與外部系統互動。

工具改變了這一點。透過給模型存取可呼叫的函數，你將它從一個文字生成器轉變為能執行動作的代理。模型決定何時需要工具、使用哪個工具以及傳遞哪些參數。你的程式碼執行函數並回傳結果。模型將該結果整合到它的回答中。

## 先決條件

- 已完成 Module 01（已部署 Azure OpenAI 資源）
- 根目錄中有 `.env` 檔案，內含 Azure 憑證（由 Module 01 中的 `azd up` 建立）

> **注意：** 如果你尚未完成 Module 01，請先遵循那裡的部署說明。

## 了解有工具的 AI 代理

> **📝 注意：** 本模組所稱的「代理」是指具備工具呼叫能力的 AI 助手。這與我們在 [Module 05: MCP](../05-mcp/README.md) 中將介紹的 **Agentic AI** 模式（具備規劃、記憶和多步推理的自主代理）不同。

若無工具，語言模型只能根據訓練資料產生文字。問它今天天氣如何，它只能猜測。若提供工具，它便可呼叫天氣 API、執行計算或查詢資料庫，然後將這些實際結果融入回答中。

<img src="../../../translated_images/zh-MO/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*無工具時模型只能猜測 — 有工具時可呼叫 API、執行計算並回傳實時資料。*

有工具的 AI 代理遵循 **推理與行動（ReAct）** 模式。模型不僅回應，還會思考它需要什麼、透過呼叫工具行動、觀察結果，然後決定是否繼續行動或給出最終答案：

1. **推理** — 代理分析使用者問題，判斷需要哪些資訊
2. **行動** — 代理選擇適當工具，產生正確參數並呼叫它
3. **觀察** — 代理接受工具輸出並評估結果
4. **重複或回應** — 若還需更多資料，代理迴圈回到第一步；否則產生自然語言回答

<img src="../../../translated_images/zh-MO/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct 循環 — 代理推理、行動、觀察結果，並持續迴圈直到能給出最終回答。*

這一切皆自動發生。你定義工具及其描述；模型負責何時及如何使用它們的決策。

## 工具呼叫如何運作

### 工具定義

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

你定義有明確描述及參數規範的函數。模型會在系統提示中看到這些描述，並理解每個工具的功能。

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

// 助手自動由 Spring Boot 配置：
// - ChatModel bean
// - 來自 @Component 類別的所有 @Tool 方法
// - 用於會話管理的 ChatMemoryProvider
```

以下圖表解析每個註解，展示各部分如何幫助 AI 理解何時呼叫工具及傳遞哪些參數：

<img src="../../../translated_images/zh-MO/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*工具定義結構 — @Tool 告訴 AI 何時使用，@P 描述每個參數，@AiService 在啟動時將所有元件組合。*

> **🤖 利用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試看：** 開啟 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) 並提問：
> - 「如何整合像 OpenWeatherMap 這樣的真實天氣 API，而非使用模擬資料？」
> - 「什麼樣的工具描述最能幫助 AI 正確使用？」
> - 「在工具實作中該如何處理 API 錯誤與速率限制？」

### 決策制定

當使用者問「西雅圖的天氣如何？」時，模型並非隨意挑選工具。它會將使用者意圖與可用工具描述比對，評分相關性並選擇最佳匹配。接著產生結構化函數呼叫，帶入正確參數 — 例如把 `location` 設為 `"Seattle"`。

若無工具能匹配使用者請求，模型會退回自行根據知識回答。若有多個工具匹配，則選擇最具體的那個。

<img src="../../../translated_images/zh-MO/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*模型評估每個可用工具與使用者意圖的關聯性並選出最佳匹配 — 這就是為何撰寫清楚且具體的工具描述很重要。*

### 執行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 自動將具宣告式 `@AiService` 介面的所有已註冊工具注入，LangChain4j 自動執行工具呼叫。幕後完整的工具呼叫流程包含六個階段 — 從使用者的自然語言問題直到回傳自然語言回答：

<img src="../../../translated_images/zh-MO/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*端到端流程 — 使用者提問後模型選擇工具，LangChain4j 執行工具，模型將結果編織成自然回答回應。*

> **🤖 利用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試看：** 開啟 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) 並提問：
> - 「ReAct 模式如何運作？為什麼對 AI 代理很有效？」
> - 「代理怎麼決定用什麼工具及使用順序？」
> - 「若工具執行失敗，我該如何穩健處理錯誤？」

### 回應生成

模型接收天氣資料並將其格式化為自然語言回答給使用者。

### 架構：Spring Boot 自動繫結

本模組使用 LangChain4j 的 Spring Boot 整合，搭配宣告式 `@AiService` 介面。啟動時，Spring Boot 會發現所有包含 `@Tool` 方法的 `@Component`，你的 `ChatModel` bean，以及 `ChatMemoryProvider`，並將它們全部繫結到單一的 `Assistant` 介面，無需樣板程式碼。

<img src="../../../translated_images/zh-MO/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService 介面將 ChatModel、工具元件與記憶提供者結合 — Spring Boot 自動處理所有繫結。*

此方法的主要好處：

- **Spring Boot 自動繫結** — ChatModel 與工具自動注入
- **@MemoryId 模式** — 自動依工作階段管理記憶
- **單一實例** — Assistant 建立一次，重複使用提升效能
- **類型安全執行** — 直接呼叫 Java 方法，支援類型轉換
- **多回合調度** — 自動管理工具串聯
- **零樣板程式碼** — 無需手動呼叫 `AiServices.builder()` 或記憶 HashMap

替代方案（手動 `AiServices.builder()`）需寫更多程式碼，且無法享受 Spring Boot 整合的便利。

## 工具串聯

**工具串聯** — 基於工具的代理的真正威力在於一個問題需連結多個工具時。例如問「西雅圖的天氣是多少華氏？」時，代理會自動串聯兩個工具：先呼叫 `getCurrentWeather` 取得攝氏溫度，再將結果傳給 `celsiusToFahrenheit` 做轉換 — 全都在單一回合對話中完成。

<img src="../../../translated_images/zh-MO/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*工具串聯實例 — 代理先呼叫 getCurrentWeather，然後將攝氏結果傳入 celsiusToFahrenheit，最後回傳合成回答。*

實際運行的應用程式畫面如下 — 代理在單一對話回合中串聯兩個工具呼叫：

<a href="images/tool-chaining.png"><img src="../../../translated_images/zh-MO/tool-chaining.3b25af01967d6f7b.webp" alt="Tool Chaining" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*真實應用輸出 — 代理自動串聯 getCurrentWeather → celsiusToFahrenheit 於一回合。*

**優雅錯誤處理** — 詢問模型沒有模擬資料的城市天氣時，工具會回傳錯誤訊息，AI 解釋它無法提供協助而非崩潰。工具失敗也安全處理。

<img src="../../../translated_images/zh-MO/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*工具失敗時，代理捕捉錯誤並以有幫助的說明回應，避免程式崩潰。*

這在單一回合對話中完成。代理可自主調度多個工具呼叫。

## 執行應用程式

**驗證部署：**

確保根目錄有 `.env` 檔案，內含 Azure 憑證（在 Module 01 期間建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若你在 Module 01 使用 `./start-all.sh` 已啟動所有應用程式，本模組已在 8084 埠運行。可跳過下方啟動指令，直接瀏覽 http://localhost:8084。

**選項 1：使用 Spring Boot Dashboard（推薦 VS Code 使用者）**

開發環境的 container 包含 Spring Boot Dashboard 擴充功能，提供視覺化介面管理所有 Spring Boot 應用程式。在 VS Code 左側活動列中找到（尋找 Spring Boot 圖示）。

透過 Spring Boot Dashboard，你可以：
- 查看工作區中所有 Spring Boot 應用
- 一鍵啟動/停止應用程式
- 實時瀏覽應用日志
- 監控應用狀態

只需點擊「tools」旁的播放按鈕啟動本模組，或一次啟動所有模組。

<img src="../../../translated_images/zh-MO/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**選項 2：使用命令行腳本**

啟動所有 web 應用程式（模組 01-04）：

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

或者只啟動本模組：

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

兩個腳本皆會從根目錄 `.env` 檔自動載入環境變數，若無 JAR 則會自動編譯。

> **注意：** 若你希望先手動編譯所有模組再啟動：
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

在瀏覽器中開啟 http://localhost:8084 。

**停止應用程式：**

**Bash:**
```bash
./stop.sh  # 僅此模塊
# 或者
cd .. && ./stop-all.sh  # 所有模塊
```

**PowerShell:**
```powershell
.\stop.ps1  # 只有這個模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```


## 使用應用程式

該應用提供一個網頁介面，你可以與可存取天氣及溫度轉換工具的 AI 代理互動。

<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-MO/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI 代理工具介面 — 快速範例與聊天介面，方便與工具互動*

### 嘗試簡單的工具使用
先從一個簡單的請求開始：「將 100 華氏度轉換成攝氏度」。代理識別出需要使用溫度轉換工具，使用正確的參數呼叫該工具，並返回結果。注意這感覺多麼自然——你並沒有指定要用哪個工具或如何呼叫它。

### 測試工具鏈結

現在試試更複雜的：「西雅圖的天氣如何，並將它轉換成華氏度？」觀察代理分步處理。它先取得天氣（回傳攝氏度），辨識出需要轉換為華氏度，呼叫轉換工具，然後將兩個結果合併成一個回應。

### 查看對話流程

聊天介面維護對話歷史，讓你可以進行多輪互動。你可以看到之前的所有查詢與回應，方便追蹤對話並理解代理如何在多次交換中建立上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-MO/tools-conversation-demo.89f2ce9676080f59.webp" alt="多工具呼叫的對話" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多輪對話示範簡單轉換、天氣查詢及工具鏈結*

### 嘗試不同請求

試試各種組合：
- 天氣查詢：「東京的天氣如何？」
- 溫度轉換：「25°C 等於多少開爾文？」
- 複合查詢：「查詢巴黎的天氣，並告訴我是否高於 20°C」

注意代理如何理解自然語言並映射到適當的工具呼叫。

## 主要概念

### ReAct 模式（推理與行動）

代理在推理（決定要做什麼）與行動（使用工具）間交替。此模式讓系統能自主解決問題，而非僅僅回應指令。

### 工具描述很重要

工具的描述品質直接影響代理使用工具的效果。清晰、具體的描述幫助模型了解何時以及如何呼叫每個工具。

### 會話管理

`@MemoryId` 註解可啟用自動的基於會話的記憶管理。每個會話 ID 都會有自己由 `ChatMemoryProvider` 管理的 `ChatMemory` 實例，因此多用戶可以同時與代理互動，而不會彼此混淆對話。

<img src="../../../translated_images/zh-MO/session-management.91ad819c6c89c400.webp" alt="@MemoryId 的會話管理" width="800"/>

*每個會話 ID 對應獨立的對話歷史——用戶間看不到彼此的訊息。*

### 錯誤處理

工具可能失敗——API 逾時、參數無效、外部服務中斷。生產環境中代理需要錯誤處理，讓模型能解釋問題或嘗試替代方案，而不會整個應用當機。當工具拋出例外時，LangChain4j 會捕捉並將錯誤訊息回傳給模型，模型就能用自然語言說明問題。

## 可用工具

以下圖示展示你可以建立的廣泛工具生態系統。本模組示範了天氣及溫度相關工具，但相同的 `@Tool` 範式可用於任何 Java 方法——從資料庫查詢到支付處理。

<img src="../../../translated_images/zh-MO/tool-ecosystem.aad3d74eaa14a44f.webp" alt="工具生態系統" width="800"/>

*任何用 @Tool 標註的 Java 方法都能提供給 AI 使用——此範式延伸至資料庫、API、電子郵件、檔案操作等。*

## 何時使用基於工具的代理

<img src="../../../translated_images/zh-MO/when-to-use-tools.51d1592d9cbdae9c.webp" alt="何時使用工具" width="800"/>

*快速決策指南——工具適用於即時數據、計算與操作；一般知識和創意任務不需要。*

**適合使用工具時：**
- 需要回答即時數據問題（天氣、股價、庫存）
- 需要執行複雜計算
- 訪問資料庫或 API
- 執行操作（寄電子郵件、建立工單、更新紀錄）
- 結合多種數據來源

**不適用時：**
- 問題可由一般知識回答
- 回應純粹是對話性質
- 工具延遲會讓體驗太慢

## 工具 vs RAG

模組 03 和 04 都擴展了 AI 可做的事，但方式根本不同。RAG 透過檢索文件讓模型存取**知識**；工具則給模型通過呼叫函數來執行**操作**的能力。

<img src="../../../translated_images/zh-MO/tools-vs-rag.ad55ce10d7e4da87.webp" alt="工具與 RAG 的比較" width="800"/>

*RAG 從靜態文件檢索資訊，工具執行操作並擷取動態即時數據。許多生產系統兩者兼用。*

實務上，很多生產系統會兩者結合：RAG 用於根據文件佐證答案，工具用於擷取即時資料或執行操作。

## 下一步

**下一模組：** [05-mcp - 模型上下文協議 (MCP)](../05-mcp/README.md)

---

**導覽：** [← 上一個：模組 03 - RAG](../03-rag/README.md) | [回主頁](../README.md) | [下一個：模組 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們致力於準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的原文版本應被視為權威來源。對於重要資訊，建議使用專業人工翻譯。我們對因使用本翻譯而引起的任何誤解或誤譯概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->