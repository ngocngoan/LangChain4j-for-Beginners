# Module 04：具備工具功能的 AI 代理

## 目錄

- [影片導覽](../../../04-tools)
- [你將學到什麼](../../../04-tools)
- [先決條件](../../../04-tools)
- [了解具備工具功能的 AI 代理](../../../04-tools)
- [工具呼叫的運作原理](../../../04-tools)
  - [工具定義](../../../04-tools)
  - [決策過程](../../../04-tools)
  - [執行](../../../04-tools)
  - [回應產生](../../../04-tools)
  - [架構：Spring Boot 自動裝配](../../../04-tools)
- [工具鏈結](../../../04-tools)
- [執行應用程式](../../../04-tools)
- [使用應用程式](../../../04-tools)
  - [嘗試簡單工具使用](../../../04-tools)
  - [測試工具鏈結](../../../04-tools)
  - [查看對話流程](../../../04-tools)
  - [嘗試不同請求](../../../04-tools)
- [關鍵概念](../../../04-tools)
  - [ReAct 模式（推理與行動）](../../../04-tools)
  - [工具描述很重要](../../../04-tools)
  - [會話管理](../../../04-tools)
  - [錯誤處理](../../../04-tools)
- [可用工具](../../../04-tools)
- [何時使用基於工具的代理](../../../04-tools)
- [工具與 RAG 比較](../../../04-tools)
- [後續步驟](../../../04-tools)

## 影片導覽

觀看本直播課程，了解如何開始使用本模組：

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## 你將學到什麼

到目前為止，你已學會如何與 AI 進行對話、有效結構提示，以及如何將回答依據你的文件。然而仍存在基本限制：語言模型只能生成文字，無法查詢天氣、進行計算、查詢資料庫或與外部系統互動。

工具改變了這點。透過賦予模型可呼叫的函式，將它從純文字生成器轉變為能採取行動的代理。模型會決定何時需要工具、使用哪個工具，及傳遞什麼參數。你的程式碼執行函式並回傳結果，模型則將此結果整合進回應中。

## 先決條件

- 已完成[模組 01 - 介紹](../01-introduction/README.md)（部署 Azure OpenAI 資源）
- 建議完成之前的模組（本模組在 Tools vs RAG 比較中，參考了[模組 03 的 RAG 概念](../03-rag/README.md)）
- 根目錄含有 `.env` 檔，內含 Azure 認證（由模組 01 的 `azd up` 建立）

> **注意：** 如果尚未完成模組 01，請先依照那裡的部署說明操作。

## 了解具備工具功能的 AI 代理

> **📝 注意：** 本模組中「代理（agents）」指的是具備工具呼叫功能的 AI 助理。與我們在[模組 05：MCP](../05-mcp/README.md)中介紹的**自主代理 AI（Agentic AI）**—具備規劃、記憶及多步推理的代理—不同。

無工具時，語言模型只能基於訓練資料生成文字。問它當前天氣，它只能猜測。給它工具，模型便能呼叫天氣 API、進行計算或查詢資料庫，並將真實結果編織到回答中。

<img src="../../../translated_images/zh-TW/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*無工具時模型只能猜測，有工具時可呼叫 API、執行計算，回傳即時資料。*

具備工具的 AI 代理遵循**推理與行動（ReAct）**模式。模型不只是回應，而是思考需求、透過呼叫工具採取行動、觀察結果，然後決定是否繼續行動或給出最終答案：

1. **推理** — 代理分析使用者問題並判斷需要什麼資訊
2. **行動** — 選擇合適工具，產生正確參數並呼叫
3. **觀察** — 接收工具輸出並評估結果
4. **重複或回應** — 若需要更多資料，則迴圈；否則生成自然語言回答

<img src="../../../translated_images/zh-TW/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct 循環 — 代理推理行動方案、呼叫工具、觀察結果並多次迴圈，直到給出最終答案。*

此流程自動執行。你定義工具及其描述，模型負責決定何時、如何使用工具。

## 工具呼叫的運作原理

### 工具定義

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

你定義帶有清晰描述與參數規範的函式。模型透過系統提示看到這些描述，理解每個工具的功能。

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // 您的天氣查詢邏輯
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// 助理由 Spring Boot 自動配置：
// - ChatModel bean
// - 所有來自 @Component 類別的 @Tool 方法
// - 用於會話管理的 ChatMemoryProvider
```

下圖解析各註解，展示每個部分如何幫助 AI 理解何時呼叫工具及傳遞哪些引數：

<img src="../../../translated_images/zh-TW/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*工具定義結構 — @Tool 告訴 AI 何時使用，@P 描述每個參數，@AiService 在啟動時自動裝配。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) 並詢問：
> - 「我該如何整合像 OpenWeatherMap 這類真正的天氣 API，而非模擬資料？」
> - 「什麼樣的工具描述能幫助 AI 正確使用它？」
> - 「如何在工具實作中處理 API 錯誤及速率限制？」

### 決策過程

當使用者問「西雅圖的天氣如何？」時，模型不會隨機選擇工具。它會比對使用者意圖與已知工具描述，針對相關性打分並選出最佳匹配。接著產生帶有正確參數的結構化函式呼叫，此例中將 `location` 設為 `"Seattle"`。

若無工具匹配使用者請求，模型則退回以自身知識回答。若多工具匹配，則選最具體者。

<img src="../../../translated_images/zh-TW/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*模型根據使用者意圖評估所有可用工具並選擇最佳匹配，這就是為何撰寫清晰、具體的工具描述很重要。*

### 執行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 自動將所有已註冊工具與宣告式 `@AiService` 介面裝配，LangChain4j 自動執行工具呼叫。幕後，完整的工具呼叫流程涵蓋六個階段——從使用者自然語言問題到自然語言回答：

<img src="../../../translated_images/zh-TW/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*完整流程 — 使用者提問，模型選擇工具，LangChain4j 執行，模型將結果融入自然回應。*

如果你在模組 00 執行過 [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，你已看過此模式 —— `Calculator` 工具就是用相同流程呼叫。下面的時序圖精確顯示示範當中幕後發生了什麼：

<img src="../../../translated_images/zh-TW/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*Quick Start 示範中的工具呼叫循環 — `AiServices` 傳送訊息與工具結構給 LLM，LLM 以函式呼叫如 `add(42, 58)` 回應，LangChain4j 在本地執行 `Calculator` 方法並回傳結果給最終回答。*

> **🤖 嘗試使用 [GitHub Copilot](https://github.com/features/copilot) 聊天：** 開啟 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) 並詢問：
> - 「ReAct 模式如何運作，為何對 AI 代理有效？」
> - 「代理如何決定使用哪個工具及順序？」
> - 「工具執行失敗時會發生什麼，我該怎麼健全處理錯誤？」

### 回應產生

模型接收天氣資料，格式化並以自然語言回覆使用者。

### 架構：Spring Boot 自動裝配

本模組使用 LangChain4j 的 Spring Boot 整合及宣告式 `@AiService` 介面。啟動時，Spring Boot 探測所有包含 `@Tool` 方法的 `@Component`、你的 `ChatModel` bean 及 `ChatMemoryProvider`，然後全部裝配到單一 `Assistant` 介面，無需樣板程式碼。

<img src="../../../translated_images/zh-TW/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService 介面串聯 ChatModel、工具元件及記憶提供者 — Spring Boot 自動處理所有裝配。*

以下是請求生命週期完整時序圖——從 HTTP 請求經控制器、服務及自動裝配代理，直至工具執行完畢回傳：

<img src="../../../translated_images/zh-TW/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*完整 Spring Boot 請求流程 — HTTP 請求經控制器及服務傳至自動裝配的 Assistant 代理，該代理自動協調 LLM 與工具呼叫。*

此方法的主要優勢：

- **Spring Boot 自動裝配** — 自動注入 ChatModel 與工具
- **@MemoryId 模式** — 自動基於會話的記憶管理
- **單一實例** — Assistant 只建立一次，重用提升效能
- **型別安全執行** — Java 方法直接呼叫並完成型別轉換
- **多回合協調** — 自動處理工具鏈結
- **零樣板程式碼** — 無須手動呼叫 `AiServices.builder()` 或自行管理記憶 HashMap

手動使用 `AiServices.builder()` 的方法需更多程式碼，且無法享受 Spring Boot 整合優勢。

## 工具鏈結

**工具鏈結** — 當單一問題需要多個工具時，基於工具的代理展現真正的威力。問「西雅圖的天氣多少華氏度？」時，代理會自動串接兩個工具：先呼叫 `getCurrentWeather` 取得攝氏溫度，再將結果傳入 `celsiusToFahrenheit` 轉換成華氏，全部在一次對話回合完成。

<img src="../../../translated_images/zh-TW/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*工具鏈結示例 — 代理先呼叫 getCurrentWeather，將攝氏結果送入 celsiusToFahrenheit，最終給出綜合答案。*

**優雅的失敗** — 詢問模擬資料中不存在的城市天氣時，工具會回傳錯誤訊息，AI 會解釋無法協助，而非崩潰。工具失敗時安全處理。下圖比較兩種做法——正確錯誤處理時，代理捕捉例外並給予友善回應；未處理時整個應用崩潰：

<img src="../../../translated_images/zh-TW/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*工具失敗時，代理捕捉錯誤並提供有幫助的說明，而非崩潰。*

這在單回合對話中發生，代理自主協調多重工具呼叫。

## 執行應用程式

**驗證部署：**

確保根目錄存在 `.env` 檔並含有 Azure 認證（於模組 01 建立）。從本模組目錄（`04-tools/`）執行：

**Bash:**
```bash
cat ../.env  # 應顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若你已透過根目錄的 `./start-all.sh`（模組 01 中描述）啟動全部應用，本模組已在 8084 埠執行。可跳過以下啟動指令，直接前往 http://localhost:8084。

**方案 1：使用 Spring Boot 儀表板（建議 VS Code 用戶）**

開發容器內含 Spring Boot 儀表板擴充，提供視覺化介面管理所有 Spring Boot 應用。可在 VS Code 左側活動欄找到（尋找 Spring Boot 圖示）。

你可以透過 Spring Boot 儀表板：
- 查看工作區中所有可用的 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時檢視應用日誌
- 監控應用狀態
只要點擊「tools」旁邊的播放按鈕即可啟動此模組，或一次啟動所有模組。

以下是在 VS Code 中的 Spring Boot 儀表板畫面：

<img src="../../../translated_images/zh-TW/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot 儀表板 — 可從一處啟動、停止並監控所有模組*

**選項 2：使用 shell 腳本**

啟動所有 Web 應用程式（模組 01-04）：

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

這兩個腳本會自動從根目錄的 `.env` 檔案載入環境變數，如果 JAR 檔不存在，則會進行編譯。

> **注意：** 如果你偏好先手動編譯所有模組再啟動：
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

**停止方法：**

**Bash:**
```bash
./stop.sh  # 僅此模組
# 或者
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

此應用程式提供一個網頁介面，讓你可以與具備天氣查詢及溫度轉換工具的 AI 代理互動。以下是介面長什麼樣子 — 包含快速啟動示例和用於發送請求的聊天面板：

<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-TW/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI 代理工具介面 — 快速示例與互動聊天介面*

### 嘗試簡單工具使用

從簡單請求開始：「將 100 華氏度轉換為攝氏度」。代理會判斷需要使用溫度轉換工具，並以正確參數呼叫，然後回傳結果。這種流程感覺非常自然 — 你並不需要指定要用哪個工具或如何呼叫。

### 測試工具串接

現在試試比較複雜的請求：「西雅圖的天氣如何，並將結果轉換成華氏度？」觀看代理分步驟處理。首先取得天氣（回傳攝氏），判斷必須轉換為華氏度，再呼叫轉換工具，最後將兩者結果合併成一個回答。

### 查看對話流程

聊天介面會保留對話歷史，允許多輪互動。你可以看到所有先前的查詢及回覆，有助於追蹤對話並了解代理如何從多次交流中建立上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-TW/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多輪對話示範簡單轉換、天氣查詢和工具串接*

### 嘗試不同請求

嘗試各種組合：
- 天氣查詢：「東京的天氣如何？」
- 溫度轉換：「25°C 是多少開爾文？」
- 綜合查詢：「查詢巴黎的天氣，告訴我是否高於 20°C」

注意代理如何理解自然語言並將其映射到適合的工具呼叫。

## 關鍵概念

### ReAct 模式（推理與行動）

代理在推理（決定要做什麼）與行動（使用工具）之間交替。此模式使其能自動解決問題，而非僅僅照指令回應。

### 工具說明很重要

工具說明的品質直接影響代理使用工具的準確度。清楚、具體的說明有助模型理解何時以及如何呼叫每個工具。

### 會話管理

`@MemoryId` 標註可自動啟用基於會話的記憶管理。每個會話 ID 都會對應一個由 `ChatMemoryProvider` 受管理的獨立 `ChatMemory` 實例，因此多位用戶可同時互動，且對話不會互相混淆。下圖展示多用戶如何根據其會話 ID 導向各自隔離的記憶庫：

<img src="../../../translated_images/zh-TW/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*每個會話 ID 對應獨立的對話歷史 — 用戶彼此看不到對方訊息。*

### 錯誤處理

工具可能失敗 — API 超時、參數可能錯誤、外部服務可能宕機。生產環境中的代理需要錯誤處理，讓模型能解釋問題或嘗試替代方案，而不是整個應用崩潰。當工具拋出例外時，LangChain4j 會捕捉並將錯誤訊息回饋給模型，模型即可用自然語言說明問題。

## 可用工具

下圖展示你可以建構的廣泛工具生態系。此模組示範了天氣與溫度工具，但相同的 `@Tool` 模式可用於任何 Java 方法——從資料庫查詢到支付處理。

<img src="../../../translated_images/zh-TW/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*任何用 @Tool 標註的 Java 方法都可供 AI 使用 — 此模式可延伸至資料庫、API、電子郵件、檔案操作等。*

## 何時使用基於工具的代理

不是每個請求都需要工具。決策點在於 AI 是需要與外部系統互動，或僅從自身知識庫回答。下圖指南總結何時工具會增值，何時是多餘的：

<img src="../../../translated_images/zh-TW/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*簡易決策指南 — 工具用於即時資料、計算及執行動作；一般知識與創意任務則不需要。*

## 工具與 RAG

模組 03 與 04 都擴展 AI 的能力，但方式根本不同。RAG 透過檢索文件提供模型**知識**，工具則賦予模型呼叫函式執行**動作**的能力。下圖並列比較這兩種方法──從工作流程到其各自的取捨：

<img src="../../../translated_images/zh-TW/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG 從靜態文件檢索資訊 — 工具執行動作並取得動態、即時資料。許多生產系統會同時結合兩者。*

實務上，許多生產系統會同時運用兩者：RAG 用於基於文檔給出根據，工具用於擷取即時資料或執行操作。

## 後續步驟

**下一模組:** [05-mcp - 模型上下文協定 (MCP)](../05-mcp/README.md)

---

**導覽:** [← 上一節：模組 03 - RAG](../03-rag/README.md) | [回主頁](../README.md) | [下一節：模組 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：  
本文件由人工智慧翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我們力求準確，但請注意自動翻譯結果可能包含錯誤或不準確之處。文件的原始語言版本應視為權威版本。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯所產生的任何誤解或錯誤詮釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->