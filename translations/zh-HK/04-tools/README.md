# Module 04: 帶工具的 AI 代理

## 目錄

- [你將會學到什麼](../../../04-tools)
- [先決條件](../../../04-tools)
- [理解帶工具的 AI 代理](../../../04-tools)
- [工具調用是如何運作的](../../../04-tools)
  - [工具定義](../../../04-tools)
  - [決策制定](../../../04-tools)
  - [執行](../../../04-tools)
  - [回應生成](../../../04-tools)
  - [架構：Spring Boot 自動注入](../../../04-tools)
- [工具串連](../../../04-tools)
- [運行應用程式](../../../04-tools)
- [使用應用程式](../../../04-tools)
  - [嘗試簡單的工具使用](../../../04-tools)
  - [測試工具串連](../../../04-tools)
  - [查看對話流程](../../../04-tools)
  - [嘗試不同的請求](../../../04-tools)
- [關鍵概念](../../../04-tools)
  - [ReAct 模式（推理與行動）](../../../04-tools)
  - [工具描述的重要性](../../../04-tools)
  - [會話管理](../../../04-tools)
  - [錯誤處理](../../../04-tools)
- [可用工具](../../../04-tools)
- [何時使用基於工具的代理](../../../04-tools)
- [工具與 RAG 的比較](../../../04-tools)
- [下一步](../../../04-tools)

## 你將會學到什麼

到目前為止，你已學會如何與 AI 對話、有效結構提示，以及將回應建立於你的文件之上。但仍有一個根本限制：語言模型只能產生文字。它們無法查詢天氣、執行計算、查詢資料庫或與外部系統互動。

工具改變了這一點。通過賦予模型可呼叫的功能，將它從一個文本產生器轉變成一個可以採取行動的代理。模型決定何時需要工具、用哪個工具，以及傳入何參數。你的程式碼負責執行該功能並回傳結果。模型將該結果納入回應。

## 先決條件

- 完成 [Module 01 - 介紹](../01-introduction/README.md)（已部署 Azure OpenAI 資源）
- 建議完成先前模組（本模組中比較工具與 RAG 時引用了 [Module 03 的 RAG 概念](../03-rag/README.md)）
- 根目錄有 `.env` 檔案並包含 Azure 認證（由 Module 01 中的 `azd up` 建立）

> **注意：** 若尚未完成 Module 01，請先遵循該模組的部署說明。

## 理解帶工具的 AI 代理

> **📝 注意：** 本模組中「代理」一詞指的是加強工具調用能力的 AI 助手。這不同於我們將在 [Module 05: MCP](../05-mcp/README.md) 中介紹的 **Agentic AI** 模式（具備規劃、記憶和多步推理的自主代理）。

沒有工具的情況下，語言模型只能根據其訓練資料生成文字。問它當前天氣，它只能猜測。給它工具，它可以呼叫天氣 API、執行計算或查詢資料庫 — 然後將這些真實結果編織到其回應中。

<img src="../../../translated_images/zh-HK/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*沒有工具時模型只能猜測 — 有了工具就能呼叫 API、執行計算並回傳即時資料。*

帶工具的 AI 代理遵循 **推理與行動 (ReAct)** 模式。模型不只是回應 — 它會思考自己需要什麼，採取行動呼叫工具，觀察結果，再決定是否再次行動或給出最終答案：

1. **推理** — 代理分析用戶問題並判斷所需資訊
2. **行動** — 代理選擇合適工具，產生正確參數並呼叫它
3. **觀察** — 代理收到工具輸出並評估結果
4. **重複或回應** — 若仍需更多資料，代理回圈執行，否則組合自然語言回答

<img src="../../../translated_images/zh-HK/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct 週期 — 代理推理應做何事，透過呼叫工具採取行動，觀察結果，並循環直到能提供最終答案。*

這一流程是自動的。你定義工具及其描述，模型負責何時及如何使用它們的決策。

## 工具調用是如何運作的

### 工具定義

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

你定義清楚的函數、明確描述與參數規範。模型在系統提示中看到這些描述，了解每個工具的功能。

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

// Assistant 會被 Spring Boot 自動連接：
// - ChatModel bean
// - 所有來自 @Component 類別的 @Tool 方法
// - 用於會話管理的 ChatMemoryProvider
```

下圖拆解每個註解，展示如何幫助 AI 了解何時呼叫工具、要傳入哪些參數：

<img src="../../../translated_images/zh-HK/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*工具定義剖析 — @Tool 告訴 AI 何時使用它，@P 描述每個參數，@AiService 在啟動時將所有項目整合。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java)，並問：
> - 「如何整合像 OpenWeatherMap 這類的真實天氣 API 而非模擬資料？」
> - 「什麼是有效的工具描述，有助 AI 正確使用？」
> - 「如何在工具實作中處理 API 錯誤和速率限制？」

### 決策制定

當用戶問「西雅圖的天氣如何？」時，模型不會隨機選擇工具。它會將用戶意圖與所有可用工具描述比對，根據相關度打分並選擇最匹配者。接著生成結構化函數呼叫及相應參數 — 這裡將 `location` 設為 `"Seattle"`。

若沒有工具符合用戶要求，模型就會根據自身知識回答。若多個工具符合，則挑選最具體的。

<img src="../../../translated_images/zh-HK/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*模型評估每個可用工具與用戶意圖匹配度，選出最佳工具 — 因此撰寫清晰具體的工具描述非常重要。*

### 執行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 自動注入所有註冊工具的宣告式 `@AiService` 介面，LangChain4j 會自動執行工具調用。幕後完整工具調用流程涵蓋六個階段 — 從用戶的自然語言問句到最終自然語言回答：

<img src="../../../translated_images/zh-HK/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*端到端流程 — 用戶提問，模型選擇工具，LangChain4j 執行該工具，模型將結果織入自然回應中。*

若你執行過 Module 00 的 [ToolIntegrationDemo](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)，已經見過這個模式 — `Calculator` 工具以相同方式被調用。下圖的序列圖展示該測試背後的實際流程：

<img src="../../../translated_images/zh-HK/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*快速入門演示的工具調用循環 — `AiServices` 送出訊息和工具結構給 LLM，LLM 回傳類似 `add(42, 58)` 的函數呼叫，LangChain4j 本地執行 `Calculator` 方法，回傳結果以產生最終回應。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) Chat 試試看：** 開啟 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)，並問：
> - 「ReAct 模式如何運作，且為何對 AI 代理有效？」
> - 「代理如何決定使用哪個工具以及順序？」
> - 「如果工具執行失敗該怎麼辦？如何強健地處理錯誤？」

### 回應生成

模型接收天氣資料，將其格式化成自然語言回應給用戶。

### 架構：Spring Boot 自動注入

本模組使用 LangChain4j 與 Spring Boot 的整合，並以宣告式 `@AiService` 介面運作。啟動時 Spring Boot 掃描所有含有 `@Tool` 方法的 `@Component` 元件，還有你的 `ChatModel` Bean 和 `ChatMemoryProvider`，然後自動將它們注入至單一的 `Assistant` 介面，完全無需重複程式碼。

<img src="../../../translated_images/zh-HK/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService 介面將 ChatModel、工具元件和記憶體提供者結合 — Spring Boot 自動處理所有注入。*

以下圖展示從 HTTP 請求、控制器、服務，到自動注入代理及工具執行的完整序列流程：

<img src="../../../translated_images/zh-HK/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*完整的 Spring Boot 請求生命週期 — HTTP 請求流經控制器及服務，再進入自動注入的 Assistant 代理，該代理自動協調 LLM 與工具調用。*

此架構的主要優勢：

- **Spring Boot 自動注入** — ChatModel 與工具自動注入
- **@MemoryId 模式** — 自動的基於會話的記憶管理
- **單一實例** — Assistant 只建立一次以提升效能
- **類型安全執行** — 直接調用 Java 方法並支援類型轉換
- **多輪協調** — 自動處理工具串連
- **零樣板碼** — 無需手動呼叫 `AiServices.builder()` 或維護記憶體 HashMap

其他做法（手動使用 `AiServices.builder()`）需要更多程式碼且無法享有 Spring Boot 整合優勢。

## 工具串連

**工具串連** — 基於工具的代理強大之處在於當一個問題需要多個工具時。問「西雅圖的天氣是多少華氏溫度？」代理會自動串連兩個工具：先呼叫 `getCurrentWeather` 取得攝氏溫度，再將該值傳給 `celsiusToFahrenheit` 轉換 — 全程在一次對話回合中完成。

<img src="../../../translated_images/zh-HK/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*工具串連展示 — 代理先呼叫 getCurrentWeather，接著將攝氏結果傳入 celsiusToFahrenheit，並回覆綜合答案。*

**優雅失敗** — 若查詢模擬資料中沒有的城市，工具回傳錯誤訊息，AI 會解釋無法協助而非崩潰。工具失敗時不會造成應用崩潰。下圖比較兩種做法 — 適當錯誤處理時，代理抓取例外並提供幫助性回應，否則整個系統當掉：

<img src="../../../translated_images/zh-HK/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*當工具失敗時，代理會捕捉錯誤並提供說明性回應，而非系統崩潰。*

這全都在一次對話回合中完成，代理自動協調多重工具呼叫。

## 運行應用程式

**驗證部署：**

確認根目錄存在包含 Azure 認證的 `.env` 檔案（模組01執行時建立）。在本模組目錄(`04-tools/`)執行：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若你已從根目錄使用 `./start-all.sh` 啟動所有應用（如 Module 01 說明），本模組已在 8084 端口運行。可跳過以下啟動指令，直接訪問 http://localhost:8084 。

**選項 1：使用 Spring Boot Dashboard（推薦 VS Code 用戶）**

開發容器包含 Spring Boot Dashboard 擴充功能，提供管理所有 Spring Boot 應用的可視化介面。你可在 VS Code 左側活動列找到 Spring Boot 圖示。

透過 Spring Boot Dashboard，你可以：
- 查看工作區所有可用的 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時檢視應用日誌
- 監控應用狀態

只需點擊 "tools" 旁的播放按鈕啟動本模組，或一次啟動所有模組。

以下為 VS Code 中的 Spring Boot Dashboard 截圖：

<img src="../../../translated_images/zh-HK/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 的 Spring Boot Dashboard — 從單一視窗啟動、停止並監控所有模組*

**選項 2：使用 shell 腳本**

啟動所有 Web 應用（模組 01-04）：

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

或者只啟動這個模組：

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

這兩個腳本會自動從根目錄的 `.env` 檔案載入環境變量，並且如果 JAR 不存在會自動構建。

> **注意：** 如果你想先手動構建所有模組後再啟動：
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

在你的瀏覽器中打開 http://localhost:8084 。

**停止服務：**

**Bash:**
```bash
./stop.sh  # 僅限此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 只限於此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```

## 使用應用程式

此應用程式提供一個網頁介面，讓你可以與具備天氣與溫度轉換工具的 AI 代理互動。以下是介面的樣子 — 它包含快速開始範例與一個聊天面板用來發送請求：

<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-HK/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI 代理工具介面 — 快速範例和用來與工具互動的聊天介面*

### 嘗試簡單工具應用

從一個簡單的請求開始：「將 100 華氏度轉換為攝氏度」。代理人會識別出需要使用溫度轉換工具，並用正確參數呼叫它，然後返回結果。注意過程多麼自然 — 你並未指定使用哪個工具或怎麼呼叫。

### 測試工具鏈結

現在嘗試更複雜一點的：「西雅圖的天氣如何？並轉換為華氏」。觀察代理人如何步驟式處理。它先取得天氣狀況（回傳攝氏度），然後判斷需要轉換為華氏度，呼叫轉換工具，最後把兩個結果整合成一個回應。

### 查看對話流程

聊天介面會保留歷史對話，允許你進行多輪互動。你可以看到所有先前的查詢與回應，便於追蹤對話並理解代理如何在多次交換中建立上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-HK/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多輪對話展示簡單轉換、天氣查詢與工具鏈結*

### 嘗試不同請求組合

嘗試各種組合：
- 天氣查詢：「東京的天氣如何？」
- 溫度轉換：「25°C 是多少開爾文？」
- 複合查詢：「查詢巴黎天氣，告訴我是否超過 20°C」

注意代理是如何將自然語言解析並映射到適當工具呼叫。

## 主要概念

### ReAct 模式（推理與行動）

代理會在推理（決定做什麼）與行動（使用工具）之間交替。這種模式讓代理能自主解決問題，而不只是被動回應指令。

### 工具描述很重要

工具的描述質量直接影響代理如何使用。清晰且具體的描述幫助模型理解何時及怎樣呼叫每個工具。

### 會話管理

`@MemoryId` 註解啟用自動的基於會話的記憶管理。每個會話 ID 都有自己獨立的 `ChatMemory` 實例，由 `ChatMemoryProvider` bean 管理，讓多位使用者能同時和代理互動，彼此的對話不會混淆。以下示意圖展示多用戶依據會話 ID 被導向隔離的記憶庫：

<img src="../../../translated_images/zh-HK/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*每個會話 ID 對應獨立的對話歷史 — 使用者彼此看不到對方訊息。*

### 錯誤處理

工具可能會失敗 — API 逾時、參數錯誤、外部服務故障。生產環境中的代理需要錯誤處理，讓模型可以解釋問題或嘗試替代方案，而不是整個程式崩潰。當工具拋出例外時，LangChain4j 會捕捉並將錯誤訊息傳回模型，模型可用自然語言解釋問題。

## 可用工具

下圖展示你可以建立的廣泛工具生態系。此模組示範天氣和溫度工具，但同樣的 `@Tool` 模式適用於任何 Java 方法 — 從資料庫查詢到支付處理都行。

<img src="../../../translated_images/zh-HK/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*任何加上 @Tool 註解的 Java 方法都能被 AI 使用 — 這個模式向資料庫、API、電子郵件、檔案操作等各方延伸。*

## 何時使用工具型代理

不是每個請求都需要工具。決策基於 AI 是否需要與外部系統互動，或能靠自身知識回答。下方指南總結何時工具最有價值，何時不必要：

<img src="../../../translated_images/zh-HK/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*快速決策指南—工具用於即時資料、計算與執行；一般知識和創意任務則不需。*

## 工具與 RAG 比較

模組 03 與 04 都擴展 AI 能力，但方式根本不同。RAG 讓模型透過檢索文件接觸**知識**，工具讓模型藉由呼叫函數執行**動作**。下圖比較這兩種方法——從工作流程到取捨：

<img src="../../../translated_images/zh-HK/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG 從靜態文件檢索信息，工具則執行動作並取得動態即時資料。許多生產系統結合這兩者。*

實務上，多數生產系統會結合兩者：使用 RAG 根據文件提供基礎資訊，用工具取得即時數據或執行作業。

## 下一步

**下一模組：** [05-mcp - 模型上下文協議 (MCP)](../05-mcp/README.md)

---

**導覽：** [← 上一章：模組 03 - RAG](../03-rag/README.md) | [返回首頁](../README.md) | [下一章：模組 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 所翻譯。雖然我們致力於確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始語言版本的文件應視為權威來源。對於重要資訊，建議聘請專業人工翻譯。我們對因使用此翻譯而產生的任何誤解或誤釋概不負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->