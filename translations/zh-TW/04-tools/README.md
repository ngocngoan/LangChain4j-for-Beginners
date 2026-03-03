# Module 04: 使用工具的 AI 代理

## 目錄

- [您將學習什麼](../../../04-tools)
- [先決條件](../../../04-tools)
- [理解帶有工具的 AI 代理](../../../04-tools)
- [工具呼叫的運作方式](../../../04-tools)
  - [工具定義](../../../04-tools)
  - [決策](../../../04-tools)
  - [執行](../../../04-tools)
  - [回應生成](../../../04-tools)
  - [架構：Spring Boot 自動連接](../../../04-tools)
- [工具串連](../../../04-tools)
- [執行應用程式](../../../04-tools)
- [使用應用程式](../../../04-tools)
  - [嘗試簡單工具使用](../../../04-tools)
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

## 您將學習什麼

到目前為止，您已經學會如何與 AI 進行對話、有效結構化提示語，以及如何讓回應在您的文件中有根據。但仍有一個根本限制：語言模型只能生成文字。它們無法查看天氣、進行計算、查詢資料庫或與外部系統互動。

工具改變了這一點。透過給模型存取可調用的函數，您將它從文字生成器轉變為可採取行動的代理。模型決定何時需要工具，使用哪個工具，以及傳遞哪些參數。您的程式碼執行該函數並回傳結果。模型將該結果納入回應中。

## 先決條件

- 已完成[Module 01 - 介紹](../01-introduction/README.md)（Azure OpenAI 資源已部署）
- 建議已完成前面模組（本模組在工具與 RAG 比較中參考了[Module 03 的 RAG 概念](../03-rag/README.md)）
- 根目錄有 `.env` 文件，包含 Azure 認證（由 Module 01 中的 `azd up` 建立）

> **注意：** 如果尚未完成 Module 01，請先依其部署說明操作。

## 理解帶有工具的 AI 代理

> **📝 注意：** 本模組中「代理」指的是具備工具呼叫能力的 AI 助手，這與我們會在[Module 05: MCP](../05-mcp/README.md) 中探討的**自治 AI**（具備規劃、記憶和多步推理的自主代理）不同。

沒有工具時，語言模型只能從訓練資料生成文字。問它當前天氣，它只能猜測。給它工具，它可以呼叫天氣 API、算數、查資料庫，並將真實結果融入回應中。

<img src="../../../translated_images/zh-TW/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*沒有工具時模組只能猜測，有了工具則能呼叫 API、執行計算，並回傳即時資料。*

具備工具的 AI 代理遵循一種**推理與行動（ReAct）**模式。模型不只是回應，而是思考需求、採取行動呼叫工具、觀察結果，再決定是繼續行動還是給出答案：

1. **推理** — 代理分析使用者提問並判斷需要的資訊
2. **行動** — 代理選擇合適的工具，產生正確參數並呼叫
3. **觀察** — 代理接收工具輸出並評估結果
4. **重複或回應** — 若需要更多資料則重複，否則撰寫自然語言答案

<img src="../../../translated_images/zh-TW/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct 週期 — 代理推理、呼叫工具、觀察結果，反覆直至能提供最終答案。*

這一切自動發生。您定義工具及其描述，模型負責決定何時及如何使用。

## 工具呼叫的運作方式

### 工具定義

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

您定義具有明確描述和參數規範的函數。模型會在系統提示中看到這些描述，了解每個工具的功能。

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

// 助手由 Spring Boot 自動配置：
// - ChatModel 介面
// - 來自 @Component 類別的所有 @Tool 方法
// - 用於會話管理的 ChatMemoryProvider
```

下圖細分每個註解並說明它們如何幫助 AI 理解何時呼叫工具及傳遞哪些參數：

<img src="../../../translated_images/zh-TW/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*工具定義構造 — @Tool 告訴 AI 何時使用，@P 描述參數，@AiService 在啟動時連接一切。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 打開 [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) 並問：
> - 「我如何整合像 OpenWeatherMap 這樣的實際天氣 API 來替代模擬資料？」
> - 「什麼樣的工具描述能幫助 AI 正確使用？」
> - 「在工具實作中如何處理 API 錯誤與速率限制？」

### 決策

當使用者問「西雅圖的天氣如何？」時，模型不會隨機選擇工具。它會將使用者意圖與可用工具描述逐一比對，評分其相關性，選擇最佳匹配。接著產生結構化函數呼叫，包括正確參數 — 這例中設定 `location` 為 `"Seattle"`。

若沒工具對應使用者請求，模型會退回利用自身知識回答。若多工具匹配，則挑最精確者。

<img src="../../../translated_images/zh-TW/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*模型評估所有工具對使用者意圖的相關性並選擇最佳匹配 — 因此撰寫清晰、具體的工具描述很重要。*

### 執行

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot 自動連接所有註冊工具的宣告式 `@AiService` 介面，LangChain4j 自動執行工具呼叫。幕後，一個完整工具呼叫經歷六個階段 — 從使用者的自然語言問題開始，直到自然語言回答：

<img src="../../../translated_images/zh-TW/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

*端對端流程 — 使用者問問題，模型選擇工具，LangChain4j 執行，模型將結果融入回應。*

> **🤖 使用 [GitHub Copilot](https://github.com/features/copilot) 聊天試試：** 打開 [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) 並問：
> - 「ReAct 模式如何運作，為何對 AI 代理有效？」
> - 「代理如何決定使用哪些工具及順序？」
> - 「若工具執行失敗，該如何健全處理錯誤？」

### 回應生成

模型接收天氣資料並格式化為自然語言給使用者回答。

### 架構：Spring Boot 自動連接

本模組使用 LangChain4j 的 Spring Boot 整合，採宣告式的 `@AiService` 介面。啟動時，Spring Boot 掃描所有含有 `@Tool` 方法的 `@Component`、您的 `ChatModel` Bean 和 `ChatMemoryProvider`，然後將它們整合至單一 `Assistant` 介面，完全無需樣板程式碼。

<img src="../../../translated_images/zh-TW/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService 介面將 ChatModel、工具元件和記憶提供者整合，Spring Boot 自動處理全部連接。*

此做法的主要優勢：

- **Spring Boot 自動連接** — ChatModel 和工具自動注入
- **@MemoryId 模式** — 自動會話記憶管理
- **單一實例** — Assistant 只建立一次以提升效能
- **型別安全執行** — Java 方法直接呼叫並進行型別轉換
- **多輪協調** — 自動處理工具串連
- **零樣板程式碼** — 無需手動呼叫 `AiServices.builder()`

手動 `AiServices.builder()` 等替代方式需要更多程式碼且缺少 Spring Boot 集成好處。

## 工具串連

**工具串連** — 基於工具的代理真正威力在於單一問題需要多工具時。例如問「西雅圖的天氣是多少華氏溫度？」代理會自動串接兩個工具：先呼叫 `getCurrentWeather` 取得攝氏溫度，再傳給 `celsiusToFahrenheit` 轉換 — 整個過程在同一輪對話中完成。

<img src="../../../translated_images/zh-TW/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*工具串連實例 — 代理先呼叫 getCurrentWeather，將攝氏結果傳給 celsiusToFahrenheit，最後給出綜合答案。*

**優雅失敗** — 若查詢的城市不在模擬資料中，工具會回傳錯誤訊息，AI 解釋無法協助而非當機。工具故障時安全處理。下圖比較兩種情況 — 有妥善錯誤處理時，代理捕獲例外並友善回應，無處理則整個應用程式當掉：

<img src="../../../translated_images/zh-TW/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*工具失敗時，代理捕捉錯誤並以幫助說明回應，而非當機。*

這整個流程在一輪對話內完成，代理自主協調多次工具呼叫。

## 執行應用程式

**驗證部署：**

確保根目錄存在 `.env` 檔案，包含 Azure 認證（在 Module 01 執行期間建立）。於本模組目錄（`04-tools/`）運行：

**Bash:**
```bash
cat ../.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # 應該顯示 AZURE_OPENAI_ENDPOINT、API_KEY、DEPLOYMENT
```

**啟動應用程式：**

> **注意：** 若您已於根目錄使用 `./start-all.sh` 啟動所有應用（如 Module 01 所述），本模組已在 8084 埠運行。可跳過以下啟動指令，直接訪問 http://localhost:8084 。

**選項 1：使用 Spring Boot Dashboard（建議 VS Code 用戶）**

開發容器包含 Spring Boot Dashboard 擴充，提供視覺化介面管理所有 Spring Boot 應用。可在 VS Code 左側 Activity Bar 找到（尋找 Spring Boot 圖示）。

從 Spring Boot Dashboard，您可以：
- 查看工作區所有 Spring Boot 應用
- 一鍵啟動/停止應用
- 即時查看應用日誌
- 監控應用狀態

只要點擊「tools」旁的播放按鈕啟動本模組，或同時啟動所有模組。

以下為 VS Code 中 Spring Boot Dashboard 畫面：

<img src="../../../translated_images/zh-TW/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code 中的 Spring Boot Dashboard — 一處啟動、停止及監控所有模組*

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

或只啟動本模組：

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

兩個腳本會自動從根目錄 `.env` 載入環境變數，並在 JAR 檔案不存在時自動建置。

> **注意：** 若您想先手動編譯所有模組再啟動：
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

於瀏覽器開啟 http://localhost:8084 。

**停止：**

**Bash:**
```bash
./stop.sh  # 僅限此模組
# 或
cd .. && ./stop-all.sh  # 所有模組
```

**PowerShell:**
```powershell
.\stop.ps1  # 僅此模組
# 或者
cd ..; .\stop-all.ps1  # 所有模組
```


## 使用應用程式

此應用提供網頁介面，讓您與能使用天氣及溫度轉換工具的 AI 代理互動。介面如下 — 包括快速啟動範例與對話面板用於發送請求：
<a href="images/tools-homepage.png"><img src="../../../translated_images/zh-TW/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools 介面" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools 介面 - 可與工具互動的快速範例與聊天介面*

### 嘗試簡單的工具使用

從簡單的請求開始：「將 100 華氏度轉換成攝氏度」。代理會識別出需要使用溫度轉換工具，並帶入正確參數呼叫，然後返回結果。注意這感覺多麼自然 —— 你並沒有指定使用哪個工具或如何呼叫它。

### 測試工具鏈結

現在試試更複雜的請求：「西雅圖的天氣如何並把溫度轉換成華氏度？」觀察代理如何分步執行。它先取得天氣（回傳攝氏溫度），識別出需要轉換成華氏度，呼叫轉換工具，並將兩者結果結合成一個回應。

### 查看對話流程

聊天介面會保留對話歷史，讓你可以進行多輪互動。你可以看到所有先前的查詢與回應，方便追蹤對話過程並理解代理如何在多次交換中累積上下文。

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/zh-TW/tools-conversation-demo.89f2ce9676080f59.webp" alt="多次呼叫工具的對話" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*多輪對話展示簡單轉換、天氣查詢和工具鏈結*

### 嘗試不同的請求

嘗試各種組合：
- 天氣查詢：「東京的天氣如何？」
- 溫度轉換：「25°C 是多少開爾文？」
- 結合查詢：「查詢巴黎天氣並告訴我是否高於 20°C」

注意代理如何將自然語言解讀並映射成適當的工具呼叫。

## 主要概念

### ReAct 模式（推理與行動）

代理交替進行推理（決定要做什麼）與行動（使用工具）。此模式讓代理能自主解決問題，而非僅僅回應指令。

### 工具描述的重要性

工具描述的品質會直接影響代理使用工具的效果。清晰而具體的描述有助模型判斷何時以及如何呼叫每個工具。

### 會話管理

`@MemoryId` 註解能夠實現自動的基於會話的記憶管理。每個會話 ID 擁有一個由 `ChatMemoryProvider` 管理的獨立 `ChatMemory` 實例，多個使用者可同時與代理互動，且不會混淆彼此的對話。下圖顯示多位使用者如何根據會話 ID 被導向隔離的記憶存儲：

<img src="../../../translated_images/zh-TW/session-management.91ad819c6c89c400.webp" alt="使用 @MemoryId 的會話管理" width="800"/>

*每個會話 ID 映射至獨立的對話歷史 —— 使用者互不見對方訊息。*

### 錯誤處理

工具可能會失敗 —— API 超時、參數無效、外部服務故障。生產環境的代理需要錯誤處理機制，讓模型能解釋問題或嘗試替代方案，而非讓整個應用程式崩潰。當工具丟出異常時，LangChain4j 會捕捉並將錯誤訊息反饋給模型，模型便能用自然語言描述問題。

## 可用工具

下圖顯示你可以構建的廣泛工具生態。本模組示範天氣與溫度相關工具，但相同的 `@Tool` 模式可用於任何 Java 方法 —— 從資料庫查詢到支付處理。

<img src="../../../translated_images/zh-TW/tool-ecosystem.aad3d74eaa14a44f.webp" alt="工具生態系" width="800"/>

*任何用 @Tool 註解的 Java 方法皆可供 AI 使用 —— 此模式延伸至資料庫、API、電子郵件、檔案操作等多種應用。*

## 何時使用基於工具的代理

並非所有請求都需使用工具。判斷標準在於 AI 是否需與外部系統互動，或能僅從自身知識回答。以下指南總結何時工具有價值，何時則不需要：

<img src="../../../translated_images/zh-TW/when-to-use-tools.51d1592d9cbdae9c.webp" alt="何時使用工具" width="800"/>

*快速決策指南 —— 工具用於即時數據、計算與操作；一般知識與創意任務則不需。*

## 工具與 RAG 的比較

模組 03 和 04 都擴展了 AI 的能力，但方式截然不同。RAG 透過檢索文件讓模型獲取 **知識**。工具讓模型透過呼叫函式取得執行 **行動** 的能力。下圖並列比較這兩種方式，涵蓋工作流程及彼此的取捨：

<img src="../../../translated_images/zh-TW/tools-vs-rag.ad55ce10d7e4da87.webp" alt="工具與 RAG 比較" width="800"/>

*RAG 從靜態文件檢索資訊 —— 工具執行操作並取得動態即時數據。許多生產系統同時結合兩者。*

實務上，多數生產系統同時採用兩者：RAG 為答案提供基礎文件依據，工具負責擷取即時資料或執行操作。

## 下一步

**下一模組：** [05-mcp - 模型上下文協議 (MCP)](../05-mcp/README.md)

---

**導覽：** [← 上一章節：模組 03 - RAG](../03-rag/README.md) | [回主頁](../README.md) | [下一章節：模組 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件係使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於確保翻譯的準確性，但請注意自動化翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋負責。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->