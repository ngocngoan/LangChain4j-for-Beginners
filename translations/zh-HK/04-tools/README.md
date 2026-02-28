# Module 04: AI Agents with Tools

## Table of Contents

- [What You'll Learn](../../../04-tools)
- [Prerequisites](../../../04-tools)
- [Understanding AI Agents with Tools](../../../04-tools)
- [How Tool Calling Works](../../../04-tools)
  - [Tool Definitions](../../../04-tools)
  - [Decision Making](../../../04-tools)
  - [Execution](../../../04-tools)
  - [Response Generation](../../../04-tools)
  - [Architecture: Spring Boot Auto-Wiring](../../../04-tools)
- [Tool Chaining](../../../04-tools)
- [Run the Application](../../../04-tools)
- [Using the Application](../../../04-tools)
  - [Try Simple Tool Usage](../../../04-tools)
  - [Test Tool Chaining](../../../04-tools)
  - [See Conversation Flow](../../../04-tools)
  - [Experiment with Different Requests](../../../04-tools)
- [Key Concepts](../../../04-tools)
  - [ReAct Pattern (Reasoning and Acting)](../../../04-tools)
  - [Tool Descriptions Matter](../../../04-tools)
  - [Session Management](../../../04-tools)
  - [Error Handling](../../../04-tools)
- [Available Tools](../../../04-tools)
- [When to Use Tool-Based Agents](../../../04-tools)
- [Tools vs RAG](../../../04-tools)
- [Next Steps](../../../04-tools)

## What You'll Learn

到目前為止，你已經學會如何與 AI 進行對話、有效地結構化提示詞，以及在你的文件中紮根回應。但仍有一個根本的限制：語言模型只能生成文本。它們無法檢查天氣、進行計算、查詢數據庫或與外部系統互動。

工具改變了這一點。透過賦予模型可調用的函數，它將從一個文本生成器轉變成一個能夠採取行動的代理。模型決定何時需要工具、使用哪個工具，以及傳遞哪些參數。你的代碼執行函數並返回結果。模型將該結果整合到其回應中。

## Prerequisites

- 已完成 Module 01（已部署 Azure OpenAI 資源）
- 根目錄下有帶有 Azure 憑證的 `.env` 文件（由 Module 01 中的 `azd up` 創建）

> **注意：** 如果你尚未完成 Module 01，請先跟隨那裡的部署說明。

## Understanding AI Agents with Tools

> **📝 注意：** 本模組中的「代理」一詞指的是具備工具調用能力的 AI 助手。這與我們將在 [Module 05: MCP](../05-mcp/README.md) 中介紹的 **Agentic AI** 模式（具備規劃、記憶和多步推理的自主代理）不同。

沒有工具的語言模型只能根據訓練數據生成文字。詢問它目前的天氣，它只能猜測。給它工具，它就能調用天氣 API、進行計算或查詢數據庫，然後將這些實際結果融入回應中。

<img src="../../../translated_images/zh-HK/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*沒有工具時模型只能猜測 — 有了工具它就能調用 API、執行計算並返回即時資料。*

具備工具的 AI 代理遵循 **推理與行動（ReAct）** 模式。模型不僅僅是回應——它會思考所需資訊，通過調用工具來行動，觀察結果，再決定是繼續行動還是提供最終回答：

1. **推理** — 代理分析用戶問題並判斷需要哪些資訊
2. **行動** — 代理選擇適當工具，生成正確參數並調用
3. **觀察** — 代理接收工具輸出，評估結果
4. **重複或回應** — 若還需更多資料則循環，否則構造自然語言回答

<img src="../../../translated_images/zh-HK/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct 循環 —— 代理推理該做什麼，透過調用工具行動，觀察結果並循環直到能提供最終答案。*

這是自動進行的。你定義工具及其描述，模型負責決定何時以及如何使用它們。

## How Tool Calling Works

### Tool Definitions

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

你定義帶有清晰描述和參數規範的函數。模型在系統提示中看到這些描述，理解每個工具的功能。

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

// 助手由 Spring Boot 自動連接：
// - ChatModel bean
// - 來自 @Component 類別的所有 @Tool 方法
// - 用於會話管理的 ChatMemoryProvider
```

下圖分解了每個註解，顯示每部分如何協助 AI 理解何時調用工具以及應該傳遞哪些參數：

<img src="../../../translated_images/zh-HK/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*工具定義結構 —— @Tool 告訴 AI 何時使用，@P 描述每個參數，@AiService 在啟動時將所有組合在一起。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 打開 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) 並問：
> - 「如何整合像 OpenWeatherMap 這樣的真實天氣 API，而非模擬數據？」
> - 「什麼是幫助 AI 正確使用工具的好描述？」
> - 「如何在工具實現中處理 API 錯誤和頻率限制？」

### Decision Making

當用戶問「西雅圖的天氣如何？」時，模型不會隨機選擇工具。它會將用戶意圖與它能訪問的每個工具描述進行比較，為每個工具評分相關性，然後選擇最佳匹配。它生成帶有正確參數的結構化函數調用——在此例中，將 `location` 設為 `"Seattle"`。

如果沒有工具匹配用戶請求，模型會退回從自身知識回答。若有多個工具匹配，模型會選擇最具體的那個。

<img src="../../../translated_images/zh-HK/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*模型評估所有可用工具與用戶意圖的匹配度，選擇最佳匹配 —— 這就是為什麼編寫清晰且具體的工具描述如此重要。*

### Execution

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 自動將所有已註冊的工具與聲明式 `@AiService` 介面注入，LangChain4j 自動執行工具調用。整個工具調用的流程有六個階段——從用戶的自然語言問題一直返回到自然語言答案：

<img src="../../../translated_images/zh-HK/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*端到端流程 —— 用戶提問，模型選擇工具，LangChain4j 執行，模型將結果織入自然語言回應。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) Chat：** 打開 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) 並問：
> - 「ReAct 模式如何運作，為什麼它對 AI 代理有效？」
> - 「代理如何決定使用哪個工具以及執行順序？」
> - 「如果工具執行失敗會怎樣 —— 如何穩健地處理錯誤？」

### Response Generation

模型接收天氣數據並將其格式化成自然語言回應給用戶。

### Architecture: Spring Boot Auto-Wiring

本模組使用 LangChain4j 與 Spring Boot 整合的聲明式 `@AiService` 介面。啟動時 Spring Boot 掃描每個含有 `@Tool` 方法的 `@Component`、你的 `ChatModel` bean 和 `ChatMemoryProvider`，然後將它們全部注入到單一 `Assistant` 介面，且無需冗餘代碼。

<img src="../../../translated_images/zh-HK/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService 介面將 ChatModel、工具元件及記憶體提供者串在一起 — Spring Boot 自動處理所有注入。*

此方法的關鍵優勢：

- **Spring Boot 自動注入** — ChatModel 和工具元件自動注入
- **@MemoryId 模式** — 自動基於會話的記憶管理
- **單例** — Assistant 只建立一次，重用提高效能
- **型別安全執行** — 直接呼叫 Java 方法並自動轉換型別
- **多回合編排** — 自動處理工具鏈結
- **零冗餘代碼** — 無需手動呼叫 `AiServices.builder()` 或維護記憶 HashMap

替代方案（手動 `AiServices.builder()`）需要更多程式碼且無 Spring Boot 整合優勢。

## Tool Chaining

**工具鏈結** — 基於工具的代理真正強大之處在於當一個問題需要多個工具時。問「西雅圖的天氣是多少華氏度？」時，代理自動串連兩個工具：先調用 `getCurrentWeather` 取得攝氏溫度，再將該值傳給 `celsiusToFahrenheit` 作轉換——全部在同一個對話回合內。

<img src="../../../translated_images/zh-HK/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*工具鏈結實例 —— 代理先呼叫 getCurrentWeather，然後將攝氏結果傳入 celsiusToFahrenheit，最後給出綜合回答。*

以下為運行中的應用範例——代理於同一對話回合中串連兩個工具調用：

<a href="images/tool-chaining.png"><img src="../../../translated_images/zh-HK/tool-chaining.3b25af01967d6f7b.webp" alt="Tool Chaining" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*實際應用輸出 —— 代理自動串連 getCurrentWeather → celsiusToFahrenheit 一次完成。*

**優雅失敗** — 詢問不存在於模擬資料中的城市天氣時，工具會返回錯誤訊息，AI 解釋為何無法協助而不是直接崩潰。工具安全地失敗。

<img src="../../../translated_images/zh-HK/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*工具失敗時，代理捕捉錯誤並提供有用說明回覆，而非崩潰。*

這發生於單一對話回合內。代理自主協調多個工具調用。

## Run the Application

**驗證部署：**

確保根目錄存在含 Azure 憑證的 `.env` 文件（在 Module 01 過程中建立）：
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用：**

> **注意：** 若你已在 Module 01 使用 `./start-all.sh` 啟動所有應用，本模組已在 8084 埠運行。可跳過以下啟動指令，直接訪問 http://localhost:8084。

**選項一：使用 Spring Boot Dashboard（推薦 VS Code 用戶）**

開發容器包含 Spring Boot Dashboard 擴展，提供視覺化介面管理所有 Spring Boot 應用。於 VS Code 左側活動欄可找到（找 Spring Boot 圖標）。

透過 Spring Boot Dashboard，你可以：
- 查看工作區所有 Spring Boot 應用
- 一鍵啟動/停止應用
- 實時觀看應用日誌
- 監控應用狀態

點擊「tools」旁的播放按鈕啟動此模組，或一鍵啟動所有模組。

<img src="../../../translated_images/zh-HK/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

**選項二：使用 shell 腳本**

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

兩個腳本會自動從根目錄 `.env` 文件加載環境變量，若 JAR 不存在會自動編譯。

> **注意：** 若你偏好手動編譯所有模組，再啟動：
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

在瀏覽器開啟 http://localhost:8084 。

**停止：**

**Bash:**
```bash
./stop.sh  # 只有此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只限此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## Using the Application

應用提供一個網頁介面，你可與具備天氣及溫度轉換工具的 AI 代理互動。

<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-HK/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI 代理工具介面 — 快速範例和聊天介面供與工具互動*

### Try Simple Tool Usage
從一個簡單的請求開始：「將100華氏度轉換成攝氏度」。代理人識別它需要溫度轉換工具，以正確的參數調用它，然後返回結果。注意這感覺多麼自然——你沒有指定要使用哪一個工具，也沒有說明如何調用它。

### 工具鏈接測試

現在試試更複雜一點的：「西雅圖的天氣如何，並將其轉換成華氏度？」觀察代理人如何逐步處理這個問題。它首先獲取天氣（返回攝氏度），認識到需要轉換成華氏度，調用轉換工具，並將兩個結果合併成一個回應。

### 查看對話流程

聊天介面保留對話歷史，允許你進行多輪互動。你可以查看所有之前的查詢和回應，使你能輕鬆追蹤對話，理解代理人如何在多次交談中建立上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-HK/tools-conversation-demo.89f2ce9676080f59.webp" alt="帶有多個工具調用的對話" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多輪對話示範簡單轉換、天氣查詢和工具鏈接*

### 試驗不同的請求

嘗試各種組合：
- 天氣查詢：「東京的天氣如何？」
- 溫度轉換：「25°C是多少開爾文？」
- 綜合查詢：「查看巴黎的天氣，並告訴我是否超過20°C」

注意代理人如何解讀自然語言並映射到適當的工具調用。

## 關鍵概念

### ReAct 模式（推理與行動）

代理人在推理（決定做什麼）和執行（使用工具）之間交替。此模式使其具備自主解決問題的能力，而不僅僅是回應指令。

### 工具描述很重要

工具描述的品質直接影響代理人使用工具的效果。清晰、具體的描述有助模型理解何時以及如何調用每個工具。

### 會話管理

`@MemoryId` 註解啟用自動基於會話的記憶管理。每個會話ID都擁有由 `ChatMemoryProvider` 管理的獨立 `ChatMemory` 實例，因此多個使用者可以同時與代理人互動，且彼此對話不會混淆。

<img src="../../../translated_images/zh-HK/session-management.91ad819c6c89c400.webp" alt="使用 @MemoryId 的會話管理" width="800"/>

*每個會話ID對應獨立的對話歷史——使用者永遠看不到彼此的訊息。*

### 錯誤處理

工具可能失敗——API 逾時、參數可能無效、外部服務中斷。生產環境代理人需要錯誤處理機制，使模型可以說明問題或嘗試替代方案，而不是讓整個應用崩潰。當工具丟出異常時，LangChain4j 會捕獲它並將錯誤訊息回饋給模型，模型隨後能以自然語言解釋問題。

## 可用工具

下圖展示了你可以構建的廣泛工具生態系統。本模組展示了天氣及溫度工具，但相同的 `@Tool` 模式適用於任何 Java 方法——從資料庫查詢到支付處理皆可。

<img src="../../../translated_images/zh-HK/tool-ecosystem.aad3d74eaa14a44f.webp" alt="工具生態系統" width="800"/>

*任何標註了 @Tool 的 Java 方法都可供 AI 使用——此模式延伸至資料庫、API、電子郵件、檔案操作等。*

## 何時使用基於工具的代理人

<img src="../../../translated_images/zh-HK/when-to-use-tools.51d1592d9cbdae9c.webp" alt="何時使用工具" width="800"/>

*快速決策指南——工具適用於即時資料、計算與行動；一般知識與創意任務則不需要。*

**使用工具時機：**
- 回答需要即時資料（天氣、股價、庫存）
- 需執行超出簡單數學的計算
- 存取資料庫或 API
- 執行行動（發送郵件、建立工單、更新紀錄）
- 合併多個資料來源

**不使用工具時機：**
- 問題可從一般知識回答
- 回應純粹是對話性質
- 工具延遲會使體驗過於緩慢

## 工具 vs RAG

模組03和04都擴展了 AI 的能力，但方式根本不同。RAG 透過檢索文件給模型提供**知識**，而工具則讓模型能透過呼叫函數執行**操作**。

<img src="../../../translated_images/zh-HK/tools-vs-rag.ad55ce10d7e4da87.webp" alt="工具與RAG比較" width="800"/>

*RAG 從靜態文件檢索信息——工具執行操作並擷取動態、即時資料。許多生產系統結合兩者。*

實務上，許多生產系統混合使用這兩種方式：RAG 用於根據文件提供答案基礎，工具則用來擷取即時資料或執行動作。

## 下一步

**下一模組：** [05-mcp - 模型上下文協議 (MCP)](../05-mcp/README.md)

---

**導航：** [← 上一章：模組 03 - RAG](../03-rag/README.md) | [回主頁](../README.md) | [下一章：模組 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件以其母語版本為權威來源。對於重要資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->